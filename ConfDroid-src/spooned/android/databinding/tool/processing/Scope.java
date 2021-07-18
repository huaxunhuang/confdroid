/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool.processing;


/**
 * Utility class to keep track of "logical" stack traces, which we can use to print better error
 * reports.
 */
public class Scope {
    private static java.lang.ThreadLocal<android.databinding.tool.processing.Scope.ScopeEntry> sScopeItems = new java.lang.ThreadLocal<android.databinding.tool.processing.Scope.ScopeEntry>();

    static java.util.List<android.databinding.tool.processing.ScopedException> sDeferredExceptions = new java.util.ArrayList<android.databinding.tool.processing.ScopedException>();

    public static void enter(final android.databinding.tool.store.Location location) {
        android.databinding.tool.processing.Scope.enter(new android.databinding.tool.processing.scopes.LocationScopeProvider() {
            @java.lang.Override
            public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
                return java.util.Arrays.asList(location);
            }
        });
    }

    public static void enter(android.databinding.tool.processing.scopes.ScopeProvider scopeProvider) {
        android.databinding.tool.processing.Scope.ScopeEntry peek = android.databinding.tool.processing.Scope.sScopeItems.get();
        android.databinding.tool.processing.Scope.ScopeEntry entry = new android.databinding.tool.processing.Scope.ScopeEntry(scopeProvider, peek);
        android.databinding.tool.processing.Scope.sScopeItems.set(entry);
    }

    public static void exit() {
        android.databinding.tool.processing.Scope.ScopeEntry entry = android.databinding.tool.processing.Scope.sScopeItems.get();
        android.databinding.tool.util.Preconditions.checkNotNull(entry, "Inconsistent scope exit");
        android.databinding.tool.processing.Scope.sScopeItems.set(entry.mParent);
    }

    public static void defer(android.databinding.tool.processing.ScopedException exception) {
        android.databinding.tool.processing.Scope.sDeferredExceptions.add(exception);
    }

    private static void registerErrorInternal(java.lang.String msg, int scopeIndex, android.databinding.tool.processing.scopes.ScopeProvider... scopeProviders) {
        if ((scopeProviders == null) || (scopeProviders.length <= scopeIndex)) {
            android.databinding.tool.processing.Scope.defer(new android.databinding.tool.processing.ScopedException(msg));
        } else
            if (scopeProviders[scopeIndex] == null) {
                android.databinding.tool.processing.Scope.registerErrorInternal(msg, scopeIndex + 1, scopeProviders);
            } else {
                try {
                    android.databinding.tool.processing.Scope.enter(scopeProviders[scopeIndex]);
                    android.databinding.tool.processing.Scope.registerErrorInternal(msg, scopeIndex + 1, scopeProviders);
                } finally {
                    android.databinding.tool.processing.Scope.exit();
                }
            }

    }

    /**
     * Convenience method to add an error in a known list of scopes, w/o adding try catch flows.
     * <p>
     * This code actually starts entering the given scopes 1 by 1, starting from 0. When list is
     * consumed, it creates the exception and defers if possible then exits from the provided
     * scopes.
     * <p>
     * Note that these scopes are added on top of the already existing scopes.
     *
     * @param msg
     * 		The exception message
     * @param scopeProviders
     * 		The list of additional scope providers to enter. Null scopes are
     * 		automatically ignored.
     */
    public static void registerError(java.lang.String msg, android.databinding.tool.processing.scopes.ScopeProvider... scopeProviders) {
        android.databinding.tool.processing.Scope.registerErrorInternal(msg, 0, scopeProviders);
    }

    public static void assertNoError() {
        if (android.databinding.tool.processing.Scope.sDeferredExceptions.isEmpty()) {
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.util.HashSet<java.lang.String> messages = new java.util.HashSet<java.lang.String>();
        for (android.databinding.tool.processing.ScopedException ex : android.databinding.tool.processing.Scope.sDeferredExceptions) {
            final java.lang.String message = ex.getMessage();
            if (!messages.contains(message)) {
                sb.append(message).append("\n");
                messages.add(message);
            }
        }
        throw new java.lang.RuntimeException("Found data binding errors.\n" + sb.toString());
    }

    static java.lang.String produceScopeLog() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("full scope log\n");
        android.databinding.tool.processing.Scope.ScopeEntry top = android.databinding.tool.processing.Scope.sScopeItems.get();
        while (top != null) {
            android.databinding.tool.processing.scopes.ScopeProvider provider = top.mProvider;
            sb.append("---").append(provider).append("\n");
            if (provider instanceof android.databinding.tool.processing.scopes.FileScopeProvider) {
                sb.append("file:").append(((android.databinding.tool.processing.scopes.FileScopeProvider) (provider)).provideScopeFilePath()).append("\n");
            }
            if (provider instanceof android.databinding.tool.processing.scopes.LocationScopeProvider) {
                android.databinding.tool.processing.scopes.LocationScopeProvider loc = ((android.databinding.tool.processing.scopes.LocationScopeProvider) (provider));
                sb.append("loc:");
                java.util.List<android.databinding.tool.store.Location> locations = loc.provideScopeLocation();
                if (locations == null) {
                    sb.append("null\n");
                } else {
                    for (android.databinding.tool.store.Location location : locations) {
                        sb.append(location).append("\n");
                    }
                }
            }
            top = top.mParent;
        } 
        sb.append("---\n");
        return sb.toString();
    }

    static android.databinding.tool.processing.ScopedErrorReport createReport() {
        android.databinding.tool.processing.Scope.ScopeEntry top = android.databinding.tool.processing.Scope.sScopeItems.get();
        java.lang.String filePath = null;
        java.util.List<android.databinding.tool.store.Location> locations = null;
        while ((top != null) && ((filePath == null) || (locations == null))) {
            android.databinding.tool.processing.scopes.ScopeProvider provider = top.mProvider;
            if ((locations == null) && (provider instanceof android.databinding.tool.processing.scopes.LocationScopeProvider)) {
                locations = android.databinding.tool.processing.Scope.findAbsoluteLocationFrom(top, ((android.databinding.tool.processing.scopes.LocationScopeProvider) (provider)));
            }
            if ((filePath == null) && (provider instanceof android.databinding.tool.processing.scopes.FileScopeProvider)) {
                filePath = ((android.databinding.tool.processing.scopes.FileScopeProvider) (provider)).provideScopeFilePath();
            }
            top = top.mParent;
        } 
        return new android.databinding.tool.processing.ScopedErrorReport(filePath, locations);
    }

    private static java.util.List<android.databinding.tool.store.Location> findAbsoluteLocationFrom(android.databinding.tool.processing.Scope.ScopeEntry entry, android.databinding.tool.processing.scopes.LocationScopeProvider top) {
        java.util.List<android.databinding.tool.store.Location> locations = top.provideScopeLocation();
        if ((locations == null) || locations.isEmpty()) {
            return null;
        }
        if (locations.size() == 1) {
            return java.util.Arrays.asList(locations.get(0).toAbsoluteLocation());
        }
        // We have more than 1 location. Depending on the scope, we may or may not want all of them
        java.util.List<android.databinding.tool.store.Location> chosen = new java.util.ArrayList<android.databinding.tool.store.Location>();
        for (android.databinding.tool.store.Location location : locations) {
            android.databinding.tool.store.Location absLocation = location.toAbsoluteLocation();
            if (android.databinding.tool.processing.Scope.validatedContained(entry.mParent, absLocation)) {
                chosen.add(absLocation);
            }
        }
        return chosen.isEmpty() ? locations : chosen;
    }

    private static boolean validatedContained(android.databinding.tool.processing.Scope.ScopeEntry parent, android.databinding.tool.store.Location absLocation) {
        if (parent == null) {
            return true;
        }
        android.databinding.tool.processing.scopes.ScopeProvider provider = parent.mProvider;
        if (!(provider instanceof android.databinding.tool.processing.scopes.LocationScopeProvider)) {
            return android.databinding.tool.processing.Scope.validatedContained(parent.mParent, absLocation);
        }
        java.util.List<android.databinding.tool.store.Location> absoluteParents = android.databinding.tool.processing.Scope.findAbsoluteLocationFrom(parent, ((android.databinding.tool.processing.scopes.LocationScopeProvider) (provider)));
        for (android.databinding.tool.store.Location location : absoluteParents) {
            if (location.contains(absLocation)) {
                return true;
            }
        }
        return false;
    }

    private static class ScopeEntry {
        android.databinding.tool.processing.scopes.ScopeProvider mProvider;

        android.databinding.tool.processing.Scope.ScopeEntry mParent;

        public ScopeEntry(android.databinding.tool.processing.scopes.ScopeProvider scopeProvider, android.databinding.tool.processing.Scope.ScopeEntry parent) {
            mProvider = scopeProvider;
            mParent = parent;
        }
    }
}

