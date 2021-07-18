/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.accounts;


/**
 * A {@link Parcelable} value type that contains information about an account authenticator.
 */
public class AuthenticatorDescription implements android.os.Parcelable {
    /**
     * The string that uniquely identifies an authenticator
     */
    public final java.lang.String type;

    /**
     * A resource id of a label for the authenticator that is suitable for displaying
     */
    public final int labelId;

    /**
     * A resource id of a icon for the authenticator
     */
    public final int iconId;

    /**
     * A resource id of a smaller icon for the authenticator
     */
    public final int smallIconId;

    /**
     * A resource id for a hierarchy of PreferenceScreen to be added to the settings page for the
     * account. See {@link AbstractAccountAuthenticator} for an example.
     */
    public final int accountPreferencesId;

    /**
     * The package name that can be used to lookup the resources from above.
     */
    public final java.lang.String packageName;

    /**
     * Authenticator handles its own token caching and permission screen
     */
    public final boolean customTokens;

    /**
     * A constructor for a full AuthenticatorDescription
     */
    public AuthenticatorDescription(java.lang.String type, java.lang.String packageName, int labelId, int iconId, int smallIconId, int prefId, boolean customTokens) {
        if (type == null)
            throw new java.lang.IllegalArgumentException("type cannot be null");

        if (packageName == null)
            throw new java.lang.IllegalArgumentException("packageName cannot be null");

        this.type = type;
        this.packageName = packageName;
        this.labelId = labelId;
        this.iconId = iconId;
        this.smallIconId = smallIconId;
        this.accountPreferencesId = prefId;
        this.customTokens = customTokens;
    }

    public AuthenticatorDescription(java.lang.String type, java.lang.String packageName, int labelId, int iconId, int smallIconId, int prefId) {
        this(type, packageName, labelId, iconId, smallIconId, prefId, false);
    }

    /**
     * A factory method for creating an AuthenticatorDescription that can be used as a key
     * to identify the authenticator by its type.
     */
    public static android.accounts.AuthenticatorDescription newKey(java.lang.String type) {
        if (type == null)
            throw new java.lang.IllegalArgumentException("type cannot be null");

        return new android.accounts.AuthenticatorDescription(type);
    }

    private AuthenticatorDescription(java.lang.String type) {
        this.type = type;
        this.packageName = null;
        this.labelId = 0;
        this.iconId = 0;
        this.smallIconId = 0;
        this.accountPreferencesId = 0;
        this.customTokens = false;
    }

    private AuthenticatorDescription(android.os.Parcel source) {
        this.type = source.readString();
        this.packageName = source.readString();
        this.labelId = source.readInt();
        this.iconId = source.readInt();
        this.smallIconId = source.readInt();
        this.accountPreferencesId = source.readInt();
        this.customTokens = source.readByte() == 1;
    }

    /**
     *
     *
     * @unknown 
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Returns the hashcode of the type only.
     */
    public int hashCode() {
        return type.hashCode();
    }

    /**
     * Compares the type only, suitable for key comparisons.
     */
    public boolean equals(java.lang.Object o) {
        if (o == this)
            return true;

        if (!(o instanceof android.accounts.AuthenticatorDescription))
            return false;

        final android.accounts.AuthenticatorDescription other = ((android.accounts.AuthenticatorDescription) (o));
        return type.equals(other.type);
    }

    public java.lang.String toString() {
        return ("AuthenticatorDescription {type=" + type) + "}";
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(packageName);
        dest.writeInt(labelId);
        dest.writeInt(iconId);
        dest.writeInt(smallIconId);
        dest.writeInt(accountPreferencesId);
        dest.writeByte(((byte) (customTokens ? 1 : 0)));
    }

    /**
     * Used to create the object from a parcel.
     */
    public static final android.os.Parcelable.Creator<android.accounts.AuthenticatorDescription> CREATOR = new android.os.Parcelable.Creator<android.accounts.AuthenticatorDescription>() {
        /**
         *
         *
         * @unknown 
         */
        public android.accounts.AuthenticatorDescription createFromParcel(android.os.Parcel source) {
            return new android.accounts.AuthenticatorDescription(source);
        }

        /**
         *
         *
         * @unknown 
         */
        public android.accounts.AuthenticatorDescription[] newArray(int size) {
            return new android.accounts.AuthenticatorDescription[size];
        }
    };
}

