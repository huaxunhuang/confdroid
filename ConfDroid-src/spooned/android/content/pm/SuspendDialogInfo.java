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
package android.content.pm;


/**
 * A container to describe the dialog to be shown when the user tries to launch a suspended
 * application.
 * The suspending app can customize the dialog's following attributes:
 * <ul>
 * <li>The dialog icon, by providing a resource id.
 * <li>The title text, by providing a resource id.
 * <li>The text of the dialog's body, by providing a resource id or a string.
 * <li>The text on the neutral button which starts the
 * {@link android.content.Intent#ACTION_SHOW_SUSPENDED_APP_DETAILS SHOW_SUSPENDED_APP_DETAILS}
 * activity, by providing a resource id.
 * </ul>
 * System defaults are used whenever any of these are not provided, or any of the provided resource
 * ids cannot be resolved at the time of displaying the dialog.
 *
 * @unknown 
 * @see PackageManager#setPackagesSuspended(String[], boolean, PersistableBundle, PersistableBundle,
SuspendDialogInfo)
 * @see Builder
 */
@android.annotation.SystemApi
public final class SuspendDialogInfo implements android.os.Parcelable {
    private static final java.lang.String TAG = android.content.pm.SuspendDialogInfo.class.getSimpleName();

    private static final java.lang.String XML_ATTR_ICON_RES_ID = "iconResId";

    private static final java.lang.String XML_ATTR_TITLE_RES_ID = "titleResId";

    private static final java.lang.String XML_ATTR_DIALOG_MESSAGE_RES_ID = "dialogMessageResId";

    private static final java.lang.String XML_ATTR_DIALOG_MESSAGE = "dialogMessage";

    private static final java.lang.String XML_ATTR_BUTTON_TEXT_RES_ID = "buttonTextResId";

    private final int mIconResId;

    private final int mTitleResId;

    private final int mDialogMessageResId;

    private final java.lang.String mDialogMessage;

    private final int mNeutralButtonTextResId;

    /**
     *
     *
     * @return the resource id of the icon to be used with the dialog
     * @unknown 
     */
    @android.annotation.DrawableRes
    public int getIconResId() {
        return mIconResId;
    }

    /**
     *
     *
     * @return the resource id of the title to be used with the dialog
     * @unknown 
     */
    @android.annotation.StringRes
    public int getTitleResId() {
        return mTitleResId;
    }

    /**
     *
     *
     * @return the resource id of the text to be shown in the dialog's body
     * @unknown 
     */
    @android.annotation.StringRes
    public int getDialogMessageResId() {
        return mDialogMessageResId;
    }

    /**
     *
     *
     * @return the text to be shown in the dialog's body. Returns {@code null} if
    {@link #getDialogMessageResId()} returns a valid resource id.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String getDialogMessage() {
        return mDialogMessage;
    }

    /**
     *
     *
     * @return the text to be shown
     * @unknown 
     */
    @android.annotation.StringRes
    public int getNeutralButtonTextResId() {
        return mNeutralButtonTextResId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void saveToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        if (mIconResId != android.content.res.Resources.ID_NULL) {
            com.android.internal.util.XmlUtils.writeIntAttribute(out, android.content.pm.SuspendDialogInfo.XML_ATTR_ICON_RES_ID, mIconResId);
        }
        if (mTitleResId != android.content.res.Resources.ID_NULL) {
            com.android.internal.util.XmlUtils.writeIntAttribute(out, android.content.pm.SuspendDialogInfo.XML_ATTR_TITLE_RES_ID, mTitleResId);
        }
        if (mDialogMessageResId != android.content.res.Resources.ID_NULL) {
            com.android.internal.util.XmlUtils.writeIntAttribute(out, android.content.pm.SuspendDialogInfo.XML_ATTR_DIALOG_MESSAGE_RES_ID, mDialogMessageResId);
        } else {
            com.android.internal.util.XmlUtils.writeStringAttribute(out, android.content.pm.SuspendDialogInfo.XML_ATTR_DIALOG_MESSAGE, mDialogMessage);
        }
        if (mNeutralButtonTextResId != android.content.res.Resources.ID_NULL) {
            com.android.internal.util.XmlUtils.writeIntAttribute(out, android.content.pm.SuspendDialogInfo.XML_ATTR_BUTTON_TEXT_RES_ID, mNeutralButtonTextResId);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.content.pm.SuspendDialogInfo restoreFromXml(org.xmlpull.v1.XmlPullParser in) {
        final android.content.pm.SuspendDialogInfo.Builder dialogInfoBuilder = new android.content.pm.SuspendDialogInfo.Builder();
        try {
            final int iconId = com.android.internal.util.XmlUtils.readIntAttribute(in, android.content.pm.SuspendDialogInfo.XML_ATTR_ICON_RES_ID, android.content.res.Resources.ID_NULL);
            final int titleId = com.android.internal.util.XmlUtils.readIntAttribute(in, android.content.pm.SuspendDialogInfo.XML_ATTR_TITLE_RES_ID, android.content.res.Resources.ID_NULL);
            final int buttonTextId = com.android.internal.util.XmlUtils.readIntAttribute(in, android.content.pm.SuspendDialogInfo.XML_ATTR_BUTTON_TEXT_RES_ID, android.content.res.Resources.ID_NULL);
            final int dialogMessageResId = com.android.internal.util.XmlUtils.readIntAttribute(in, android.content.pm.SuspendDialogInfo.XML_ATTR_DIALOG_MESSAGE_RES_ID, android.content.res.Resources.ID_NULL);
            final java.lang.String dialogMessage = com.android.internal.util.XmlUtils.readStringAttribute(in, android.content.pm.SuspendDialogInfo.XML_ATTR_DIALOG_MESSAGE);
            if (iconId != android.content.res.Resources.ID_NULL) {
                dialogInfoBuilder.setIcon(iconId);
            }
            if (titleId != android.content.res.Resources.ID_NULL) {
                dialogInfoBuilder.setTitle(titleId);
            }
            if (buttonTextId != android.content.res.Resources.ID_NULL) {
                dialogInfoBuilder.setNeutralButtonText(buttonTextId);
            }
            if (dialogMessageResId != android.content.res.Resources.ID_NULL) {
                dialogInfoBuilder.setMessage(dialogMessageResId);
            } else
                if (dialogMessage != null) {
                    dialogInfoBuilder.setMessage(dialogMessage);
                }

        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.content.pm.SuspendDialogInfo.TAG, "Exception while parsing from xml. Some fields may default", e);
        }
        return dialogInfoBuilder.build();
    }

    @java.lang.Override
    public int hashCode() {
        int hashCode = mIconResId;
        hashCode = (31 * hashCode) + mTitleResId;
        hashCode = (31 * hashCode) + mNeutralButtonTextResId;
        hashCode = (31 * hashCode) + mDialogMessageResId;
        hashCode = (31 * hashCode) + java.util.Objects.hashCode(mDialogMessage);
        return hashCode;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof android.content.pm.SuspendDialogInfo)) {
            return false;
        }
        final android.content.pm.SuspendDialogInfo otherDialogInfo = ((android.content.pm.SuspendDialogInfo) (obj));
        return ((((mIconResId == otherDialogInfo.mIconResId) && (mTitleResId == otherDialogInfo.mTitleResId)) && (mDialogMessageResId == otherDialogInfo.mDialogMessageResId)) && (mNeutralButtonTextResId == otherDialogInfo.mNeutralButtonTextResId)) && java.util.Objects.equals(mDialogMessage, otherDialogInfo.mDialogMessage);
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder("SuspendDialogInfo: {");
        if (mIconResId != android.content.res.Resources.ID_NULL) {
            builder.append("mIconId = 0x");
            builder.append(java.lang.Integer.toHexString(mIconResId));
            builder.append(" ");
        }
        if (mTitleResId != android.content.res.Resources.ID_NULL) {
            builder.append("mTitleResId = 0x");
            builder.append(java.lang.Integer.toHexString(mTitleResId));
            builder.append(" ");
        }
        if (mNeutralButtonTextResId != android.content.res.Resources.ID_NULL) {
            builder.append("mNeutralButtonTextResId = 0x");
            builder.append(java.lang.Integer.toHexString(mNeutralButtonTextResId));
            builder.append(" ");
        }
        if (mDialogMessageResId != android.content.res.Resources.ID_NULL) {
            builder.append("mDialogMessageResId = 0x");
            builder.append(java.lang.Integer.toHexString(mDialogMessageResId));
            builder.append(" ");
        } else
            if (mDialogMessage != null) {
                builder.append("mDialogMessage = \"");
                builder.append(mDialogMessage);
                builder.append("\" ");
            }

        builder.append("}");
        return builder.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeInt(mIconResId);
        dest.writeInt(mTitleResId);
        dest.writeInt(mDialogMessageResId);
        dest.writeString(mDialogMessage);
        dest.writeInt(mNeutralButtonTextResId);
    }

    private SuspendDialogInfo(android.os.Parcel source) {
        mIconResId = source.readInt();
        mTitleResId = source.readInt();
        mDialogMessageResId = source.readInt();
        mDialogMessage = source.readString();
        mNeutralButtonTextResId = source.readInt();
    }

    SuspendDialogInfo(android.content.pm.SuspendDialogInfo.Builder b) {
        mIconResId = b.mIconResId;
        mTitleResId = b.mTitleResId;
        mDialogMessageResId = b.mDialogMessageResId;
        mDialogMessage = (mDialogMessageResId == android.content.res.Resources.ID_NULL) ? b.mDialogMessage : null;
        mNeutralButtonTextResId = b.mNeutralButtonTextResId;
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.SuspendDialogInfo> CREATOR = new android.content.pm.Creator<android.content.pm.SuspendDialogInfo>() {
        @java.lang.Override
        public android.content.pm.SuspendDialogInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.SuspendDialogInfo(source);
        }

        @java.lang.Override
        public android.content.pm.SuspendDialogInfo[] newArray(int size) {
            return new android.content.pm.SuspendDialogInfo[size];
        }
    };

    /**
     * Builder to build a {@link SuspendDialogInfo} object.
     */
    public static final class Builder {
        private int mDialogMessageResId = android.content.res.Resources.ID_NULL;

        private java.lang.String mDialogMessage;

        private int mTitleResId = android.content.res.Resources.ID_NULL;

        private int mIconResId = android.content.res.Resources.ID_NULL;

        private int mNeutralButtonTextResId = android.content.res.Resources.ID_NULL;

        /**
         * Set the resource id of the icon to be used. If not provided, no icon will be shown.
         *
         * @param resId
         * 		The resource id of the icon.
         * @return this builder object.
         */
        @android.annotation.NonNull
        public android.content.pm.SuspendDialogInfo.Builder setIcon(@android.annotation.DrawableRes
        int resId) {
            com.android.internal.util.Preconditions.checkArgument(android.content.res.ResourceId.isValid(resId), "Invalid resource id provided");
            mIconResId = resId;
            return this;
        }

        /**
         * Set the resource id of the title text to be displayed. If this is not provided, the
         * system will use a default title.
         *
         * @param resId
         * 		The resource id of the title.
         * @return this builder object.
         */
        @android.annotation.NonNull
        public android.content.pm.SuspendDialogInfo.Builder setTitle(@android.annotation.StringRes
        int resId) {
            com.android.internal.util.Preconditions.checkArgument(android.content.res.ResourceId.isValid(resId), "Invalid resource id provided");
            mTitleResId = resId;
            return this;
        }

        /**
         * Set the text to show in the body of the dialog. Ignored if a resource id is set via
         * {@link #setMessage(int)}.
         * <p>
         * The system will use {@link String#format(Locale, String, Object...) String.format} to
         * insert the suspended app name into the message, so an example format string could be
         * {@code "The app %1$s is currently suspended"}. This is optional - if the string passed in
         * {@code message} does not accept an argument, it will be used as is.
         *
         * @param message
         * 		The dialog message.
         * @return this builder object.
         * @see #setMessage(int)
         */
        @android.annotation.NonNull
        public android.content.pm.SuspendDialogInfo.Builder setMessage(@android.annotation.NonNull
        java.lang.String message) {
            com.android.internal.util.Preconditions.checkStringNotEmpty(message, "Message cannot be null or empty");
            mDialogMessage = message;
            return this;
        }

        /**
         * Set the resource id of the dialog message to be shown. If no dialog message is provided
         * via either this method or {@link #setMessage(String)}, the system will use a
         * default message.
         * <p>
         * The system will use {@link android.content.res.Resources#getString(int, Object...)
         * getString} to insert the suspended app name into the message, so an example format string
         * could be {@code "The app %1$s is currently suspended"}. This is optional - if the string
         * referred to by {@code resId} does not accept an argument, it will be used as is.
         *
         * @param resId
         * 		The resource id of the dialog message.
         * @return this builder object.
         * @see #setMessage(String)
         */
        @android.annotation.NonNull
        public android.content.pm.SuspendDialogInfo.Builder setMessage(@android.annotation.StringRes
        int resId) {
            com.android.internal.util.Preconditions.checkArgument(android.content.res.ResourceId.isValid(resId), "Invalid resource id provided");
            mDialogMessageResId = resId;
            return this;
        }

        /**
         * Set the resource id of text to be shown on the neutral button. Tapping this button starts
         * the {@link android.content.Intent#ACTION_SHOW_SUSPENDED_APP_DETAILS} activity. If this is
         * not provided, the system will use a default text.
         *
         * @param resId
         * 		The resource id of the button text
         * @return this builder object.
         */
        @android.annotation.NonNull
        public android.content.pm.SuspendDialogInfo.Builder setNeutralButtonText(@android.annotation.StringRes
        int resId) {
            com.android.internal.util.Preconditions.checkArgument(android.content.res.ResourceId.isValid(resId), "Invalid resource id provided");
            mNeutralButtonTextResId = resId;
            return this;
        }

        /**
         * Build the final object based on given inputs.
         *
         * @return The {@link SuspendDialogInfo} object built using this builder.
         */
        @android.annotation.NonNull
        public android.content.pm.SuspendDialogInfo build() {
            return new android.content.pm.SuspendDialogInfo(this);
        }
    }
}

