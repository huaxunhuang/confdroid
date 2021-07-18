/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.content;


/**
 * Provides utility methods for matching MIME type filters used in ContentProvider.
 *
 * <p>Wildcards are allowed only instead of the entire type or subtype with a tree prefix.
 * Eg. image\/*, *\/* is a valid filter and will match image/jpeg, but image/j* is invalid and
 * it will not match image/jpeg. Suffixes and parameters are not supported, and they are treated
 * as part of the subtype during matching. Neither type nor subtype can be empty.
 *
 * <p><em>Note: MIME type matching in the Android framework is case-sensitive, unlike the formal
 * RFC definitions. As a result, you should always write these elements with lower case letters,
 * or use {@link android.content.Intent#normalizeMimeType} to ensure that they are converted to
 * lower case.</em>
 *
 * <p>MIME types can be null or ill-formatted. In such case they won't match anything.
 *
 * <p>MIME type filters must be correctly formatted, or an exception will be thrown.
 * Copied from support library.
 * {@hide }
 */
public final class MimeTypeFilter {
    private MimeTypeFilter() {
    }

    private static boolean mimeTypeAgainstFilter(@android.annotation.NonNull
    java.lang.String[] mimeTypeParts, @android.annotation.NonNull
    java.lang.String[] filterParts) {
        if (filterParts.length != 2) {
            throw new java.lang.IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
        }
        if (filterParts[0].isEmpty() || filterParts[1].isEmpty()) {
            throw new java.lang.IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
        }
        if (mimeTypeParts.length != 2) {
            return false;
        }
        if ((!"*".equals(filterParts[0])) && (!filterParts[0].equals(mimeTypeParts[0]))) {
            return false;
        }
        if ((!"*".equals(filterParts[1])) && (!filterParts[1].equals(mimeTypeParts[1]))) {
            return false;
        }
        return true;
    }

    /**
     * Matches one nullable MIME type against one MIME type filter.
     *
     * @return True if the {@code mimeType} matches the {@code filter}.
     */
    public static boolean matches(@android.annotation.Nullable
    java.lang.String mimeType, @android.annotation.NonNull
    java.lang.String filter) {
        if (mimeType == null) {
            return false;
        }
        final java.lang.String[] mimeTypeParts = mimeType.split("/");
        final java.lang.String[] filterParts = filter.split("/");
        return android.content.MimeTypeFilter.mimeTypeAgainstFilter(mimeTypeParts, filterParts);
    }

    /**
     * Matches one nullable MIME type against an array of MIME type filters.
     *
     * @return The first matching filter, or null if nothing matches.
     */
    @android.annotation.Nullable
    public static java.lang.String matches(@android.annotation.Nullable
    java.lang.String mimeType, @android.annotation.NonNull
    java.lang.String[] filters) {
        if (mimeType == null) {
            return null;
        }
        final java.lang.String[] mimeTypeParts = mimeType.split("/");
        for (java.lang.String filter : filters) {
            final java.lang.String[] filterParts = filter.split("/");
            if (android.content.MimeTypeFilter.mimeTypeAgainstFilter(mimeTypeParts, filterParts)) {
                return filter;
            }
        }
        return null;
    }

    /**
     * Matches multiple MIME types against an array of MIME type filters.
     *
     * @return The first matching MIME type, or null if nothing matches.
     */
    @android.annotation.Nullable
    public static java.lang.String matches(@android.annotation.Nullable
    java.lang.String[] mimeTypes, @android.annotation.NonNull
    java.lang.String filter) {
        if (mimeTypes == null) {
            return null;
        }
        final java.lang.String[] filterParts = filter.split("/");
        for (java.lang.String mimeType : mimeTypes) {
            final java.lang.String[] mimeTypeParts = mimeType.split("/");
            if (android.content.MimeTypeFilter.mimeTypeAgainstFilter(mimeTypeParts, filterParts)) {
                return mimeType;
            }
        }
        return null;
    }

    /**
     * Matches multiple MIME types against an array of MIME type filters.
     *
     * @return The list of matching MIME types, or empty array if nothing matches.
     */
    @android.annotation.NonNull
    public static java.lang.String[] matchesMany(@android.annotation.Nullable
    java.lang.String[] mimeTypes, @android.annotation.NonNull
    java.lang.String filter) {
        if (mimeTypes == null) {
            return new java.lang.String[]{  };
        }
        final java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        final java.lang.String[] filterParts = filter.split("/");
        for (java.lang.String mimeType : mimeTypes) {
            final java.lang.String[] mimeTypeParts = mimeType.split("/");
            if (android.content.MimeTypeFilter.mimeTypeAgainstFilter(mimeTypeParts, filterParts)) {
                list.add(mimeType);
            }
        }
        return list.toArray(new java.lang.String[list.size()]);
    }
}

