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
package android.support.v13.view.inputmethod;


/**
 * Helper for accessing features in {@link EditorInfo} introduced after API level 13 in a backwards
 * compatible fashion.
 */
public final class EditorInfoCompat {
    private interface EditorInfoCompatImpl {
        void setContentMimeTypes(@android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.Nullable
        java.lang.String[] contentMimeTypes);

        @android.support.annotation.NonNull
        java.lang.String[] getContentMimeTypes(@android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo);
    }

    private static final java.lang.String[] EMPTY_STRING_ARRAY = new java.lang.String[0];

    private static final class BaseEditorInfoCompatImpl implements android.support.v13.view.inputmethod.EditorInfoCompat.EditorInfoCompatImpl {
        private static java.lang.String CONTENT_MIME_TYPES_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";

        @java.lang.Override
        public void setContentMimeTypes(@android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.Nullable
        java.lang.String[] contentMimeTypes) {
            if (editorInfo.extras == null) {
                editorInfo.extras = new android.os.Bundle();
            }
            editorInfo.extras.putStringArray(android.support.v13.view.inputmethod.EditorInfoCompat.BaseEditorInfoCompatImpl.CONTENT_MIME_TYPES_KEY, contentMimeTypes);
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public java.lang.String[] getContentMimeTypes(@android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo) {
            if (editorInfo.extras == null) {
                return android.support.v13.view.inputmethod.EditorInfoCompat.EMPTY_STRING_ARRAY;
            }
            java.lang.String[] result = editorInfo.extras.getStringArray(android.support.v13.view.inputmethod.EditorInfoCompat.BaseEditorInfoCompatImpl.CONTENT_MIME_TYPES_KEY);
            return result != null ? result : android.support.v13.view.inputmethod.EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
    }

    private static final class Api25EditorInfoCompatImpl implements android.support.v13.view.inputmethod.EditorInfoCompat.EditorInfoCompatImpl {
        @java.lang.Override
        public void setContentMimeTypes(@android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.Nullable
        java.lang.String[] contentMimeTypes) {
            android.support.v13.view.inputmethod.EditorInfoCompatApi25.setContentMimeTypes(editorInfo, contentMimeTypes);
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public java.lang.String[] getContentMimeTypes(@android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo) {
            java.lang.String[] result = android.support.v13.view.inputmethod.EditorInfoCompatApi25.getContentMimeTypes(editorInfo);
            return result != null ? result : android.support.v13.view.inputmethod.EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
    }

    private static final android.support.v13.view.inputmethod.EditorInfoCompat.EditorInfoCompatImpl IMPL;

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastNMR1()) {
            IMPL = new android.support.v13.view.inputmethod.EditorInfoCompat.Api25EditorInfoCompatImpl();
        } else {
            IMPL = new android.support.v13.view.inputmethod.EditorInfoCompat.BaseEditorInfoCompatImpl();
        }
    }

    /**
     * Sets MIME types that can be accepted by the target editor if the IME calls
     * {@link InputConnectionCompat#commitContent(InputConnection, EditorInfo,
     * InputContentInfoCompat, int, Bundle)}.
     *
     * @param editorInfo
     * 		the editor with which we associate supported MIME types
     * @param contentMimeTypes
     * 		an array of MIME types. {@code null} and an empty array means that
     * 		{@link InputConnectionCompat#commitContent(
     * 		InputConnection, EditorInfo, InputContentInfoCompat, int, Bundle)
     * 		is not supported on this Editor
     */
    public static void setContentMimeTypes(@android.support.annotation.NonNull
    android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.Nullable
    java.lang.String[] contentMimeTypes) {
        android.support.v13.view.inputmethod.EditorInfoCompat.IMPL.setContentMimeTypes(editorInfo, contentMimeTypes);
    }

    /**
     * Gets MIME types that can be accepted by the target editor if the IME calls
     * {@link InputConnectionCompat#commitContent(InputConnection, EditorInfo,
     * InputContentInfoCompat, int, Bundle)}
     *
     * @param editorInfo
     * 		the editor from which we get the MIME types
     * @return an array of MIME types. An empty array means that {@link InputConnectionCompat#commitContent(InputConnection, EditorInfo, InputContentInfoCompat,
    int, Bundle)} is not supported on this editor
     */
    @android.support.annotation.NonNull
    public static java.lang.String[] getContentMimeTypes(android.view.inputmethod.EditorInfo editorInfo) {
        return android.support.v13.view.inputmethod.EditorInfoCompat.IMPL.getContentMimeTypes(editorInfo);
    }
}

