/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.view.inputmethod;


/**
 *
 *
 * @unknown 
 */
public final class InputConnectionInspector {
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.GET_SELECTED_TEXT, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.SET_COMPOSING_REGION, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.COMMIT_CORRECTION, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.REQUEST_CURSOR_UPDATES, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.DELETE_SURROUNDING_TEXT_IN_CODE_POINTS, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.GET_HANDLER, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.CLOSE_CONNECTION, android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.COMMIT_CONTENT })
    public @interface MissingMethodFlags {
        /**
         * {@link InputConnection#getSelectedText(int)} is available in
         * {@link android.os.Build.VERSION_CODES#GINGERBREAD} and later.
         */
        int GET_SELECTED_TEXT = 1 << 0;

        /**
         * {@link InputConnection#setComposingRegion(int, int)} is available in
         * {@link android.os.Build.VERSION_CODES#GINGERBREAD} and later.
         */
        int SET_COMPOSING_REGION = 1 << 1;

        /**
         * {@link InputConnection#commitCorrection(CorrectionInfo)} is available in
         * {@link android.os.Build.VERSION_CODES#HONEYCOMB} and later.
         */
        int COMMIT_CORRECTION = 1 << 2;

        /**
         * {@link InputConnection#requestCursorUpdates(int)} is available in
         * {@link android.os.Build.VERSION_CODES#LOLLIPOP} and later.
         */
        int REQUEST_CURSOR_UPDATES = 1 << 3;

        /**
         * {@link InputConnection#deleteSurroundingTextInCodePoints(int, int)}} is available in
         * {@link android.os.Build.VERSION_CODES#N} and later.
         */
        int DELETE_SURROUNDING_TEXT_IN_CODE_POINTS = 1 << 4;

        /**
         * {@link InputConnection#deleteSurroundingTextInCodePoints(int, int)}} is available in
         * {@link android.os.Build.VERSION_CODES#N} and later.
         */
        int GET_HANDLER = 1 << 5;

        /**
         * {@link InputConnection#closeConnection()}} is available in
         * {@link android.os.Build.VERSION_CODES#N} and later.
         */
        int CLOSE_CONNECTION = 1 << 6;

        /**
         * {@link InputConnection#commitContent(InputContentInfo, int, Bundle)} is available in
         * {@link android.os.Build.VERSION_CODES#N} MR-1 and later.
         */
        int COMMIT_CONTENT = 1 << 7;
    }

    private static final java.util.Map<java.lang.Class, java.lang.Integer> sMissingMethodsMap = java.util.Collections.synchronizedMap(new java.util.WeakHashMap<>());

    @android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    public static int getMissingMethodFlags(@android.annotation.Nullable
    final android.view.inputmethod.InputConnection ic) {
        if (ic == null) {
            return 0;
        }
        // Optimization for a known class.
        if (ic instanceof android.view.inputmethod.BaseInputConnection) {
            return 0;
        }
        // Optimization for a known class.
        if (ic instanceof android.view.inputmethod.InputConnectionWrapper) {
            return ((android.view.inputmethod.InputConnectionWrapper) (ic)).getMissingMethodFlags();
        }
        return android.view.inputmethod.InputConnectionInspector.getMissingMethodFlagsInternal(ic.getClass());
    }

    @android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    public static int getMissingMethodFlagsInternal(@android.annotation.NonNull
    final java.lang.Class clazz) {
        final java.lang.Integer cachedFlags = android.view.inputmethod.InputConnectionInspector.sMissingMethodsMap.get(clazz);
        if (cachedFlags != null) {
            return cachedFlags;
        }
        int flags = 0;
        if (!android.view.inputmethod.InputConnectionInspector.hasGetSelectedText(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.GET_SELECTED_TEXT;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasSetComposingRegion(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.SET_COMPOSING_REGION;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasCommitCorrection(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.COMMIT_CORRECTION;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasRequestCursorUpdate(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.REQUEST_CURSOR_UPDATES;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasDeleteSurroundingTextInCodePoints(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.DELETE_SURROUNDING_TEXT_IN_CODE_POINTS;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasGetHandler(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.GET_HANDLER;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasCloseConnection(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.CLOSE_CONNECTION;
        }
        if (!android.view.inputmethod.InputConnectionInspector.hasCommitContent(clazz)) {
            flags |= android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.COMMIT_CONTENT;
        }
        android.view.inputmethod.InputConnectionInspector.sMissingMethodsMap.put(clazz, flags);
        return flags;
    }

    private static boolean hasGetSelectedText(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("getSelectedText", int.class);
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasSetComposingRegion(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("setComposingRegion", int.class, int.class);
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasCommitCorrection(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("commitCorrection", android.view.inputmethod.CorrectionInfo.class);
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasRequestCursorUpdate(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("requestCursorUpdates", int.class);
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasDeleteSurroundingTextInCodePoints(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("deleteSurroundingTextInCodePoints", int.class, int.class);
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasGetHandler(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("getHandler");
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasCloseConnection(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("closeConnection");
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasCommitContent(@android.annotation.NonNull
    final java.lang.Class clazz) {
        try {
            final java.lang.reflect.Method method = clazz.getMethod("commitContent", android.view.inputmethod.InputContentInfo.class, int.class, android.os.Bundle.class);
            return !java.lang.reflect.Modifier.isAbstract(method.getModifiers());
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    public static java.lang.String getMissingMethodFlagsAsString(@android.view.inputmethod.InputConnectionInspector.MissingMethodFlags
    final int flags) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        boolean isEmpty = true;
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.GET_SELECTED_TEXT) != 0) {
            sb.append("getSelectedText(int)");
            isEmpty = false;
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.SET_COMPOSING_REGION) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("setComposingRegion(int, int)");
            isEmpty = false;
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.COMMIT_CORRECTION) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("commitCorrection(CorrectionInfo)");
            isEmpty = false;
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.REQUEST_CURSOR_UPDATES) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("requestCursorUpdate(int)");
            isEmpty = false;
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.DELETE_SURROUNDING_TEXT_IN_CODE_POINTS) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("deleteSurroundingTextInCodePoints(int, int)");
            isEmpty = false;
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.GET_HANDLER) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("getHandler()");
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.CLOSE_CONNECTION) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("closeConnection()");
        }
        if ((flags & android.view.inputmethod.InputConnectionInspector.MissingMethodFlags.COMMIT_CONTENT) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("commitContent(InputContentInfo, Bundle)");
        }
        return sb.toString();
    }
}

