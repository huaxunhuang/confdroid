/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.view.inputmethod;


/**
 * A container object with which input methods can send content files to the target application.
 */
public final class InputContentInfo implements android.os.Parcelable {
    /**
     * The content URI that may or may not have a user ID embedded by
     * {@link ContentProvider#maybeAddUserId(Uri, int)}.  This always preserves the exact value
     * specified to a constructor.  In other words, if it had user ID embedded when it was passed
     * to the constructor, it still has the same user ID no matter if it is valid or not.
     */
    @android.annotation.NonNull
    private final android.net.Uri mContentUri;

    /**
     * The user ID to which {@link #mContentUri} belongs to.  If {@link #mContentUri} already
     * embedded the user ID when it was specified then this fields has the same user ID.  Otherwise
     * the user ID is determined based on the process ID when the constructor is called.
     *
     * <p>CAUTION: If you received {@link InputContentInfo} from a different process, there is no
     * guarantee that this value is correct and valid.  Never use this for any security purpose</p>
     */
    @android.annotation.UserIdInt
    private final int mContentUriOwnerUserId;

    @android.annotation.NonNull
    private final android.content.ClipDescription mDescription;

    @android.annotation.Nullable
    private final android.net.Uri mLinkUri;

    @android.annotation.NonNull
    private com.android.internal.inputmethod.IInputContentUriToken mUriToken;

    /**
     * Constructs {@link InputContentInfo} object only with mandatory data.
     *
     * @param contentUri
     * 		Content URI to be exported from the input method.
     * 		This cannot be {@code null}.
     * @param description
     * 		A {@link ClipDescription} object that contains the metadata of
     * 		{@code contentUri} such as MIME type(s). This object cannot be {@code null}. Also
     * 		{@link ClipDescription#getLabel()} should be describing the content specified by
     * 		{@code contentUri} for accessibility reasons.
     */
    public InputContentInfo(@android.annotation.NonNull
    android.net.Uri contentUri, @android.annotation.NonNull
    android.content.ClipDescription description) {
        /* link Uri */
        this(contentUri, description, null);
    }

    /**
     * Constructs {@link InputContentInfo} object with additional link URI.
     *
     * @param contentUri
     * 		Content URI to be exported from the input method.
     * 		This cannot be {@code null}.
     * @param description
     * 		A {@link ClipDescription} object that contains the metadata of
     * 		{@code contentUri} such as MIME type(s). This object cannot be {@code null}. Also
     * 		{@link ClipDescription#getLabel()} should be describing the content specified by
     * 		{@code contentUri} for accessibility reasons.
     * @param linkUri
     * 		An optional {@code http} or {@code https} URI. The editor author may provide
     * 		a way to navigate the user to the specified web page if this is not {@code null}.
     * @throws InvalidParameterException
     * 		if any invalid parameter is specified.
     */
    public InputContentInfo(@android.annotation.NonNull
    android.net.Uri contentUri, @android.annotation.NonNull
    android.content.ClipDescription description, @android.annotation.Nullable
    android.net.Uri linkUri) {
        /* throwException */
        android.view.inputmethod.InputContentInfo.validateInternal(contentUri, description, linkUri, true);
        mContentUri = contentUri;
        mContentUriOwnerUserId = android.content.ContentProvider.getUserIdFromUri(mContentUri, android.os.UserHandle.myUserId());
        mDescription = description;
        mLinkUri = linkUri;
    }

    /**
     *
     *
     * @return {@code true} if all the fields are valid.
     * @unknown 
     */
    public boolean validate() {
        return /* throwException */
        android.view.inputmethod.InputContentInfo.validateInternal(mContentUri, mDescription, mLinkUri, false);
    }

    /**
     * Constructs {@link InputContentInfo} object with additional link URI.
     *
     * @param contentUri
     * 		Content URI to be exported from the input method.
     * 		This cannot be {@code null}.
     * @param description
     * 		A {@link ClipDescription} object that contains the metadata of
     * 		{@code contentUri} such as MIME type(s). This object cannot be {@code null}. Also
     * 		{@link ClipDescription#getLabel()} should be describing the content specified by
     * 		{@code contentUri} for accessibility reasons.
     * @param linkUri
     * 		An optional {@code http} or {@code https} URI. The editor author may provide
     * 		a way to navigate the user to the specified web page if this is not {@code null}.
     * @param throwException
     * 		{@code true} if this method should throw an
     * 		{@link InvalidParameterException}.
     * @throws InvalidParameterException
     * 		if any invalid parameter is specified.
     */
    private static boolean validateInternal(@android.annotation.NonNull
    android.net.Uri contentUri, @android.annotation.NonNull
    android.content.ClipDescription description, @android.annotation.Nullable
    android.net.Uri linkUri, boolean throwException) {
        if (contentUri == null) {
            if (throwException) {
                throw new java.lang.NullPointerException("contentUri");
            }
            return false;
        }
        if (description == null) {
            if (throwException) {
                throw new java.lang.NullPointerException("description");
            }
            return false;
        }
        final java.lang.String contentUriScheme = contentUri.getScheme();
        if (!"content".equals(contentUriScheme)) {
            if (throwException) {
                throw new java.security.InvalidParameterException("contentUri must have content scheme");
            }
            return false;
        }
        if (linkUri != null) {
            final java.lang.String scheme = linkUri.getScheme();
            if ((scheme == null) || ((!scheme.equalsIgnoreCase("http")) && (!scheme.equalsIgnoreCase("https")))) {
                if (throwException) {
                    throw new java.security.InvalidParameterException("linkUri must have either http or https scheme");
                }
                return false;
            }
        }
        return true;
    }

    /**
     *
     *
     * @return Content URI with which the content can be obtained.
     */
    @android.annotation.NonNull
    public android.net.Uri getContentUri() {
        // Fix up the content URI when and only when the caller's user ID does not match the owner's
        // user ID.
        if (mContentUriOwnerUserId != android.os.UserHandle.myUserId()) {
            return android.content.ContentProvider.maybeAddUserId(mContentUri, mContentUriOwnerUserId);
        }
        return mContentUri;
    }

    /**
     *
     *
     * @return {@link ClipDescription} object that contains the metadata of {@code #getContentUri()}
    such as MIME type(s). {@link ClipDescription#getLabel()} can be used for accessibility
    purpose.
     */
    @android.annotation.NonNull
    public android.content.ClipDescription getDescription() {
        return mDescription;
    }

    /**
     *
     *
     * @return An optional {@code http} or {@code https} URI that is related to this content.
     */
    @android.annotation.Nullable
    public android.net.Uri getLinkUri() {
        return mLinkUri;
    }

    /**
     * Update the internal state of this object to be associated with the given token.
     *
     * <p>TODO(yukawa): Come up with an idea to make {@link InputContentInfo} immutable.</p>
     *
     * @param token
     * 		special URI token obtained from the system.
     * @unknown 
     */
    public void setUriToken(com.android.internal.inputmethod.IInputContentUriToken token) {
        if (mUriToken != null) {
            throw new java.lang.IllegalStateException("URI token is already set");
        }
        mUriToken = token;
    }

    /**
     * Requests a temporary read-only access permission for content URI associated with this object.
     *
     * <p>Does nothing if the temporary permission is already granted.</p>
     */
    public void requestPermission() {
        if (mUriToken == null) {
            return;
        }
        try {
            mUriToken.take();
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    /**
     * Releases a temporary read-only access permission for content URI associated with this object.
     *
     * <p>Does nothing if the temporary permission is not granted.</p>
     */
    public void releasePermission() {
        if (mUriToken == null) {
            return;
        }
        try {
            mUriToken.release();
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        android.net.Uri.writeToParcel(dest, mContentUri);
        dest.writeInt(mContentUriOwnerUserId);
        mDescription.writeToParcel(dest, flags);
        android.net.Uri.writeToParcel(dest, mLinkUri);
        if (mUriToken != null) {
            dest.writeInt(1);
            dest.writeStrongBinder(mUriToken.asBinder());
        } else {
            dest.writeInt(0);
        }
    }

    private InputContentInfo(@android.annotation.NonNull
    android.os.Parcel source) {
        mContentUri = Uri.CREATOR.createFromParcel(source);
        mContentUriOwnerUserId = source.readInt();
        mDescription = this.CREATOR.createFromParcel(source);
        mLinkUri = Uri.CREATOR.createFromParcel(source);
        if (source.readInt() == 1) {
            mUriToken = IInputContentUriToken.Stub.asInterface(source.readStrongBinder());
        } else {
            mUriToken = null;
        }
    }

    /**
     * Used to make this class parcelable.
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InputContentInfo> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InputContentInfo>() {
        @java.lang.Override
        public android.view.inputmethod.InputContentInfo createFromParcel(android.os.Parcel source) {
            return new android.view.inputmethod.InputContentInfo(source);
        }

        @java.lang.Override
        public android.view.inputmethod.InputContentInfo[] newArray(int size) {
            return new android.view.inputmethod.InputContentInfo[size];
        }
    };

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

