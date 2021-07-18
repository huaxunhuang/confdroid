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
 * Helper for accessing features in {@link InputConnection} introduced after API level 13 in a
 * backwards compatible fashion.
 */
public final class InputConnectionCompat {
    private interface InputConnectionCompatImpl {
        boolean commitContent(@android.support.annotation.NonNull
        android.view.inputmethod.InputConnection inputConnection, @android.support.annotation.NonNull
        android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfo, int flags, @android.support.annotation.Nullable
        android.os.Bundle opts);

        @android.support.annotation.NonNull
        android.view.inputmethod.InputConnection createWrapper(@android.support.annotation.NonNull
        android.view.inputmethod.InputConnection ic, @android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.NonNull
        android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener callback);
    }

    static final class BaseInputContentInfoCompatImpl implements android.support.v13.view.inputmethod.InputConnectionCompat.InputConnectionCompatImpl {
        private static java.lang.String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";

        private static java.lang.String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";

        private static java.lang.String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";

        private static java.lang.String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";

        private static java.lang.String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";

        private static java.lang.String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";

        private static java.lang.String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";

        @java.lang.Override
        public boolean commitContent(@android.support.annotation.NonNull
        android.view.inputmethod.InputConnection inputConnection, @android.support.annotation.NonNull
        android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfo, int flags, @android.support.annotation.Nullable
        android.os.Bundle opts) {
            final android.os.Bundle params = new android.os.Bundle();
            params.putParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_CONTENT_URI_KEY, inputContentInfo.getContentUri());
            params.putParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_DESCRIPTION_KEY, inputContentInfo.getDescription());
            params.putParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_LINK_URI_KEY, inputContentInfo.getLinkUri());
            params.putInt(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_FLAGS_KEY, flags);
            params.putParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_OPTS_KEY, opts);
            // TODO: Support COMMIT_CONTENT_RESULT_RECEIVER.
            return inputConnection.performPrivateCommand(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_ACTION, params);
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public android.view.inputmethod.InputConnection createWrapper(@android.support.annotation.NonNull
        android.view.inputmethod.InputConnection ic, @android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.NonNull
        android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener onCommitContentListener) {
            java.lang.String[] contentMimeTypes = android.support.v13.view.inputmethod.EditorInfoCompat.getContentMimeTypes(editorInfo);
            if (contentMimeTypes.length == 0) {
                return ic;
            }
            final android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener listener = onCommitContentListener;
            return /* mutable */
            new android.view.inputmethod.InputConnectionWrapper(ic, false) {
                @java.lang.Override
                public boolean performPrivateCommand(java.lang.String action, android.os.Bundle data) {
                    if (android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.handlePerformPrivateCommand(action, data, listener)) {
                        return true;
                    }
                    return super.performPrivateCommand(action, data);
                }
            };
        }

        static boolean handlePerformPrivateCommand(@android.support.annotation.Nullable
        java.lang.String action, @android.support.annotation.NonNull
        android.os.Bundle data, @android.support.annotation.NonNull
        android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener onCommitContentListener) {
            if (!android.text.TextUtils.equals(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_ACTION, action)) {
                return false;
            }
            if (data == null) {
                return false;
            }
            android.os.ResultReceiver resultReceiver = null;
            boolean result = false;
            try {
                resultReceiver = data.getParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_RESULT_RECEIVER);
                final android.net.Uri contentUri = data.getParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_CONTENT_URI_KEY);
                final android.content.ClipDescription description = data.getParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_DESCRIPTION_KEY);
                final android.net.Uri linkUri = data.getParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_LINK_URI_KEY);
                final int flags = data.getInt(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_FLAGS_KEY);
                final android.os.Bundle opts = data.getParcelable(android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl.COMMIT_CONTENT_OPTS_KEY);
                final android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfo = new android.support.v13.view.inputmethod.InputContentInfoCompat(contentUri, description, linkUri);
                result = onCommitContentListener.onCommitContent(inputContentInfo, flags, opts);
            } finally {
                if (resultReceiver != null) {
                    resultReceiver.send(result ? 1 : 0, null);
                }
            }
            return result;
        }
    }

    private static final class Api25InputContentInfoCompatImpl implements android.support.v13.view.inputmethod.InputConnectionCompat.InputConnectionCompatImpl {
        @java.lang.Override
        public boolean commitContent(@android.support.annotation.NonNull
        android.view.inputmethod.InputConnection inputConnection, @android.support.annotation.NonNull
        android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfo, int flags, @android.support.annotation.Nullable
        android.os.Bundle opts) {
            return android.support.v13.view.inputmethod.InputConnectionCompatApi25.commitContent(inputConnection, inputContentInfo.unwrap(), flags, opts);
        }

        @android.support.annotation.Nullable
        @java.lang.Override
        public android.view.inputmethod.InputConnection createWrapper(@android.support.annotation.Nullable
        android.view.inputmethod.InputConnection inputConnection, @android.support.annotation.NonNull
        android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.Nullable
        android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener onCommitContentListener) {
            final android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener listener = onCommitContentListener;
            return android.support.v13.view.inputmethod.InputConnectionCompatApi25.createWrapper(inputConnection, new android.support.v13.view.inputmethod.InputConnectionCompatApi25.OnCommitContentListener() {
                @java.lang.Override
                public boolean onCommitContent(java.lang.Object inputContentInfo, int flags, android.os.Bundle opts) {
                    android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfoCompat = android.support.v13.view.inputmethod.InputContentInfoCompat.wrap(inputContentInfo);
                    return listener.onCommitContent(inputContentInfoCompat, flags, opts);
                }
            });
        }
    }

    private static final android.support.v13.view.inputmethod.InputConnectionCompat.InputConnectionCompatImpl IMPL;

    static {
        if (android.support.v4.os.BuildCompat.isAtLeastNMR1()) {
            IMPL = new android.support.v13.view.inputmethod.InputConnectionCompat.Api25InputContentInfoCompatImpl();
        } else {
            IMPL = new android.support.v13.view.inputmethod.InputConnectionCompat.BaseInputContentInfoCompatImpl();
        }
    }

    /**
     * Calls commitContent API, in a backwards compatible fashion.
     *
     * @param inputConnection
     * 		{@link InputConnection} with which commitContent API will be called
     * @param editorInfo
     * 		{@link EditorInfo} associated with the given {@code inputConnection}
     * @param inputContentInfo
     * 		content information to be passed to the editor
     * @param flags
     * 		{@code 0} or {@link #INPUT_CONTENT_GRANT_READ_URI_PERMISSION}
     * @param opts
     * 		optional bundle data. This can be {@code null}
     * @return {@code true} if this request is accepted by the application, no matter if the request
    is already handled or still being handled in background
     */
    public static boolean commitContent(@android.support.annotation.NonNull
    android.view.inputmethod.InputConnection inputConnection, @android.support.annotation.NonNull
    android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.NonNull
    android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfo, int flags, @android.support.annotation.Nullable
    android.os.Bundle opts) {
        final android.content.ClipDescription description = inputContentInfo.getDescription();
        boolean supported = false;
        for (java.lang.String mimeType : android.support.v13.view.inputmethod.EditorInfoCompat.getContentMimeTypes(editorInfo)) {
            if (description.hasMimeType(mimeType)) {
                supported = true;
                break;
            }
        }
        if (!supported) {
            return false;
        }
        return android.support.v13.view.inputmethod.InputConnectionCompat.IMPL.commitContent(inputConnection, inputContentInfo, flags, opts);
    }

    /**
     * When this flag is used, the editor will be able to request temporary access permissions to
     * the content URI contained in the {@link InputContentInfoCompat} object, in a similar manner
     * that has been recommended in
     * <a href="{@docRoot }training/secure-file-sharing/index.html">Sharing Files</a>.
     *
     * <p>Make sure that the content provider owning the Uri sets the
     * {@link android.R.attr#grantUriPermissions grantUriPermissions} attribute in its manifest or
     * included the {@code &lt;grant-uri-permissions&gt;} tag.</p>
     *
     * <p>Supported only on API &gt;= 25.</p>
     *
     * <p>On API &lt;= 24 devices, IME developers need to ensure that the content URI is accessible
     * only from the target application, for example, by generating a URL with a unique name that
     * others cannot guess. IME developers can also rely on the following information of the target
     * application to do additional access checks in their {@link android.content.ContentProvider}.
     * </p>
     * <ul>
     *     <li>On API &gt;= 23 {@link EditorInfo#packageName} is guaranteed to not be spoofed, which
     *     can later be compared with {@link android.content.ContentProvider#getCallingPackage()} in
     *     the {@link android.content.ContentProvider}.
     *     </li>
     *     <li>{@link android.view.inputmethod.InputBinding#getUid()} is guaranteed to not be
     *     spoofed, which can later be compared with {@link android.os.Binder#getCallingUid()} in
     *     the {@link android.content.ContentProvider}.</li>
     * </ul>
     */
    public static int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 0x1;

    /**
     * Listener for commitContent method call, in a backwards compatible fashion.
     */
    public interface OnCommitContentListener {
        /**
         * Intercepts InputConnection#commitContent API calls.
         *
         * @param inputContentInfo
         * 		content to be committed
         * @param flags
         * 		{@code 0} or {@link #INPUT_CONTENT_GRANT_READ_URI_PERMISSION}
         * @param opts
         * 		optional bundle data. This can be {@code null}
         * @return {@code true} if this request is accepted by the application, no matter if the
        request is already handled or still being handled in background. {@code false} to use the
        default implementation
         */
        boolean onCommitContent(android.support.v13.view.inputmethod.InputContentInfoCompat inputContentInfo, int flags, android.os.Bundle opts);
    }

    /**
     * Creates a wrapper {@link InputConnection} object from an existing {@link InputConnection}
     * and {@link OnCommitContentListener} that can be returned to the system.
     *
     * <p>By returning the wrapper object to the IME, the editor can be notified by
     * {@link OnCommitContentListener#onCommitContent(InputContentInfoCompat, int, Bundle)}
     * when the IME calls
     * {@link InputConnectionCompat#commitContent(InputConnection, EditorInfo,
     * InputContentInfoCompat, int, Bundle)} and the corresponding Framework API that is available
     * on API &gt;= 25.</p>
     *
     * @param inputConnection
     * 		{@link InputConnection} to be wrapped
     * @param editorInfo
     * 		{@link EditorInfo} associated with the given {@code inputConnection}
     * @param onCommitContentListener
     * 		the listener that the wrapper object will call
     * @return a wrapper {@link InputConnection} object that can be returned to the IME
     * @throws IllegalArgumentException
     * 		when {@code inputConnection}, {@code editorInfo}, or
     * 		{@code onCommitContentListener} is {@code null}
     */
    @android.support.annotation.NonNull
    public static android.view.inputmethod.InputConnection createWrapper(@android.support.annotation.NonNull
    android.view.inputmethod.InputConnection inputConnection, @android.support.annotation.NonNull
    android.view.inputmethod.EditorInfo editorInfo, @android.support.annotation.NonNull
    android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new java.lang.IllegalArgumentException("inputConnection must be non-null");
        }
        if (editorInfo == null) {
            throw new java.lang.IllegalArgumentException("editorInfo must be non-null");
        }
        if (onCommitContentListener == null) {
            throw new java.lang.IllegalArgumentException("onCommitContentListener must be non-null");
        }
        return android.support.v13.view.inputmethod.InputConnectionCompat.IMPL.createWrapper(inputConnection, editorInfo, onCommitContentListener);
    }
}

