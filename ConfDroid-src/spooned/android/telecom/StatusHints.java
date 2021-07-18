/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.telecom;


/**
 * Contains status label and icon displayed in the in-call UI.
 */
public final class StatusHints implements android.os.Parcelable {
    private final java.lang.CharSequence mLabel;

    private final android.graphics.drawable.Icon mIcon;

    private final android.os.Bundle mExtras;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public StatusHints(android.content.ComponentName packageName, java.lang.CharSequence label, int iconResId, android.os.Bundle extras) {
        this(label, iconResId == 0 ? null : android.graphics.drawable.Icon.createWithResource(packageName.getPackageName(), iconResId), extras);
    }

    public StatusHints(java.lang.CharSequence label, android.graphics.drawable.Icon icon, android.os.Bundle extras) {
        mLabel = label;
        mIcon = icon;
        mExtras = extras;
    }

    /**
     *
     *
     * @return A package used to load the icon.
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public android.content.ComponentName getPackageName() {
        // Minimal compatibility shim for legacy apps' tests
        return new android.content.ComponentName("", "");
    }

    /**
     *
     *
     * @return The label displayed in the in-call UI.
     */
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * The icon resource ID for the icon to show.
     *
     * @return A resource ID.
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public int getIconResId() {
        // Minimal compatibility shim for legacy apps' tests
        return 0;
    }

    /**
     *
     *
     * @return An icon displayed in the in-call UI.
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getIcon(android.content.Context context) {
        return mIcon.loadDrawable(context);
    }

    /**
     *
     *
     * @return An icon depicting the status.
     */
    public android.graphics.drawable.Icon getIcon() {
        return mIcon;
    }

    /**
     *
     *
     * @return Extra data used to display status.
     */
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeCharSequence(mLabel);
        out.writeParcelable(mIcon, 0);
        out.writeParcelable(mExtras, 0);
    }

    public static final android.os.Parcelable.Creator<android.telecom.StatusHints> CREATOR = new android.os.Parcelable.Creator<android.telecom.StatusHints>() {
        public android.telecom.StatusHints createFromParcel(android.os.Parcel in) {
            return new android.telecom.StatusHints(in);
        }

        public android.telecom.StatusHints[] newArray(int size) {
            return new android.telecom.StatusHints[size];
        }
    };

    private StatusHints(android.os.Parcel in) {
        mLabel = in.readCharSequence();
        mIcon = in.readParcelable(getClass().getClassLoader());
        mExtras = in.readParcelable(getClass().getClassLoader());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if ((other != null) && (other instanceof android.telecom.StatusHints)) {
            android.telecom.StatusHints otherHints = ((android.telecom.StatusHints) (other));
            return (java.util.Objects.equals(otherHints.getLabel(), getLabel()) && java.util.Objects.equals(otherHints.getIcon(), getIcon())) && java.util.Objects.equals(otherHints.getExtras(), getExtras());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return (java.util.Objects.hashCode(mLabel) + java.util.Objects.hashCode(mIcon)) + java.util.Objects.hashCode(mExtras);
    }
}

