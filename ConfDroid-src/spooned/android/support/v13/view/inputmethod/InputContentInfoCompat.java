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
 * Helper for accessing features in InputContentInfo introduced after API level 13 in a backwards
 * compatible fashion.
 */
public final class InputContentInfoCompat {
    private interface InputContentInfoCompatImpl {
        @android.support.annotation.NonNull
        android.net.Uri getContentUri();

        @android.support.annotation.NonNull
        android.content.ClipDescription getDescription();

        @android.support.annotation.Nullable
        android.net.Uri getLinkUri();

        @android.support.annotation.Nullable
        java.lang.Object getInputContentInfo();

        void requestPermission();

        void releasePermission();
    }

    private static final class BaseInputContentInfoCompatImpl implements android.support.v13.view.inputmethod.InputContentInfoCompat.InputContentInfoCompatImpl {
        @android.support.annotation.NonNull
        private final android.net.Uri mContentUri;

        @android.support.annotation.NonNull
        private final android.content.ClipDescription mDescription;

        @android.support.annotation.Nullable
        private final android.net.Uri mLinkUri;

        public BaseInputContentInfoCompatImpl(@android.support.annotation.NonNull
        android.net.Uri contentUri, @android.support.annotation.NonNull
        android.content.ClipDescription description, @android.support.annotation.Nullable
        android.net.Uri linkUri) {
            mContentUri = contentUri;
            mDescription = description;
            mLinkUri = linkUri;
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public android.net.Uri getContentUri() {
            return mContentUri;
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public android.content.ClipDescription getDescription() {
            return mDescription;
        }

        @android.support.annotation.Nullable
        @java.lang.Override
        public android.net.Uri getLinkUri() {
            return mLinkUri;
        }

        @android.support.annotation.Nullable
        @java.lang.Override
        public java.lang.Object getInputContentInfo() {
            return null;
        }

        @java.lang.Override
        public void requestPermission() {
            return;
        }

        @java.lang.Override
        public void releasePermission() {
            return;
        }
    }

    private static final class Api25InputContentInfoCompatImpl implements android.support.v13.view.inputmethod.InputContentInfoCompat.InputContentInfoCompatImpl {
        @android.support.annotation.NonNull
        final java.lang.Object mObject;

        public Api25InputContentInfoCompatImpl(@android.support.annotation.NonNull
        java.lang.Object inputContentInfo) {
            mObject = inputContentInfo;
        }

        public Api25InputContentInfoCompatImpl(@android.support.annotation.NonNull
        android.net.Uri contentUri, @android.support.annotation.NonNull
        android.content.ClipDescription description, @android.support.annotation.Nullable
        android.net.Uri linkUri) {
            mObject = android.support.v13.view.inputmethod.InputContentInfoCompatApi25.create(contentUri, description, linkUri);
        }

        @java.lang.Override
        @android.support.annotation.NonNull
        public android.net.Uri getContentUri() {
            return android.support.v13.view.inputmethod.InputContentInfoCompatApi25.getContentUri(mObject);
        }

        @java.lang.Override
        @android.support.annotation.NonNull
        public android.content.ClipDescription getDescription() {
            return android.support.v13.view.inputmethod.InputContentInfoCompatApi25.getDescription(mObject);
        }

        @java.lang.Override
        @android.support.annotation.Nullable
        public android.net.Uri getLinkUri() {
            return android.support.v13.view.inputmethod.InputContentInfoCompatApi25.getLinkUri(mObject);
        }

        @java.lang.Override
        @android.support.annotation.Nullable
        public java.lang.Object getInputContentInfo() {
            return mObject;
        }

        @java.lang.Override
        public void requestPermission() {
            android.support.v13.view.inputmethod.InputContentInfoCompatApi25.requestPermission(mObject);
        }

        @java.lang.Override
        public void releasePermission() {
            android.support.v13.view.inputmethod.InputContentInfoCompatApi25.releasePermission(mObject);
        }
    }

    private final android.support.v13.view.inputmethod.InputContentInfoCompat.InputContentInfoCompatImpl mImpl;

    /**
     * Constructs {@link InputContentInfoCompat}.
     *
     * @param contentUri
     * 		content URI to be exported from the input method. This cannot be
     * 		{@code null}.
     * @param description
     * 		a {@link ClipDescription} object that contains the metadata of
     * 		{@code contentUri} such as MIME type(s). This object cannot be
     * 		{@code null}. Also {@link ClipDescription#getLabel()} should be describing
     * 		the content specified by {@code contentUri} for accessibility reasons.
     * @param linkUri
     * 		an optional {@code http} or {@code https} URI. The editor author may provide
     * 		a way to navigate the user to the specified web page if this is not
     * 		{@code null}.
     */
    public InputContentInfoCompat(@android.support.annotation.NonNull
    android.net.Uri contentUri, @android.support.annotation.NonNull
    android.content.ClipDescription description, @android.support.annotation.Nullable
    android.net.Uri linkUri) {
        if (android.support.v4.os.BuildCompat.isAtLeastNMR1()) {
            mImpl = new android.support.v13.view.inputmethod.InputContentInfoCompat.Api25InputContentInfoCompatImpl(contentUri, description, linkUri);
        } else {
            mImpl = new android.support.v13.view.inputmethod.InputContentInfoCompat.BaseInputContentInfoCompatImpl(contentUri, description, linkUri);
        }
    }

    private InputContentInfoCompat(@android.support.annotation.NonNull
    android.support.v13.view.inputmethod.InputContentInfoCompat.InputContentInfoCompatImpl impl) {
        mImpl = impl;
    }

    /**
     *
     *
     * @return content URI with which the content can be obtained.
     */
    @android.support.annotation.NonNull
    public android.net.Uri getContentUri() {
        return mImpl.getContentUri();
    }

    /**
     *
     *
     * @return {@link ClipDescription} object that contains the metadata of {@code #getContentUri()}
    such as MIME type(s). {@link ClipDescription#getLabel()} can be used for accessibility
    purpose.
     */
    @android.support.annotation.NonNull
    public android.content.ClipDescription getDescription() {
        return mImpl.getDescription();
    }

    /**
     *
     *
     * @return an optional {@code http} or {@code https} URI that is related to this content.
     */
    @android.support.annotation.Nullable
    public android.net.Uri getLinkUri() {
        return mImpl.getLinkUri();
    }

    /**
     * Creates an instance from a framework android.view.inputmethod.InputContentInfo object.
     *
     * <p>This method always returns {@code null} on API &lt;= 24.</p>
     *
     * @param inputContentInfo
     * 		an android.view.inputmethod.InputContentInfo object, or {@code null}
     * 		if none.
     * @return an equivalent {@link InputContentInfoCompat} object, or {@code null} if not
    supported.
     */
    @android.support.annotation.Nullable
    public static android.support.v13.view.inputmethod.InputContentInfoCompat wrap(@android.support.annotation.Nullable
    java.lang.Object inputContentInfo) {
        if (inputContentInfo == null) {
            return null;
        }
        if (!android.support.v4.os.BuildCompat.isAtLeastNMR1()) {
            return null;
        }
        return new android.support.v13.view.inputmethod.InputContentInfoCompat(new android.support.v13.view.inputmethod.InputContentInfoCompat.Api25InputContentInfoCompatImpl(inputContentInfo));
    }

    /**
     * Gets the underlying framework android.view.inputmethod.InputContentInfo object.
     *
     * <p>This method always returns {@code null} on API &lt;= 24.</p>
     *
     * @return an equivalent android.view.inputmethod.InputContentInfo object, or {@code null} if
    not supported.
     */
    @android.support.annotation.Nullable
    public java.lang.Object unwrap() {
        return mImpl.getInputContentInfo();
    }

    /**
     * Requests a temporary read-only access permission for content URI associated with this object.
     *
     * <p>Does nothing if the temporary permission is already granted.</p>
     */
    public void requestPermission() {
        mImpl.requestPermission();
    }

    /**
     * Releases a temporary read-only access permission for content URI associated with this object.
     *
     * <p>Does nothing if the temporary permission is not granted.</p>
     */
    public void releasePermission() {
        mImpl.releasePermission();
    }
}

