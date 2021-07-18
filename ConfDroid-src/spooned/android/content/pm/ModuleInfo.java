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
 * Information you can retrieve about a particular system
 * module.
 */
public final class ModuleInfo implements android.os.Parcelable {
    // NOTE: When adding new data members be sure to update the copy-constructor, Parcel
    // constructor, and writeToParcel.
    /**
     * Public name of this module.
     */
    private java.lang.CharSequence mName;

    /**
     * The package name of this module.
     */
    private java.lang.String mPackageName;

    /**
     * Whether or not this module is hidden from the user.
     */
    private boolean mHidden;

    // TODO: Decide whether we need an additional metadata bundle to support out of band
    // updates to ModuleInfo.
    // 
    // private Bundle mMetadata;
    /**
     *
     *
     * @unknown 
     */
    public ModuleInfo() {
    }

    /**
     *
     *
     * @unknown 
     */
    public ModuleInfo(android.content.pm.ModuleInfo orig) {
        mName = orig.mName;
        mPackageName = orig.mPackageName;
        mHidden = orig.mHidden;
    }

    /**
     *
     *
     * @unknown Sets the public name of this module.
     */
    public android.content.pm.ModuleInfo setName(java.lang.CharSequence name) {
        mName = name;
        return this;
    }

    /**
     * Gets the public name of this module.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getName() {
        return mName;
    }

    /**
     *
     *
     * @unknown Sets the package name of this module.
     */
    public android.content.pm.ModuleInfo setPackageName(java.lang.String packageName) {
        mPackageName = packageName;
        return this;
    }

    /**
     * Gets the package name of this module.
     */
    @android.annotation.Nullable
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     *
     *
     * @unknown Sets whether or not this package is hidden.
     */
    public android.content.pm.ModuleInfo setHidden(boolean hidden) {
        mHidden = hidden;
        return this;
    }

    /**
     * Gets whether or not this package is hidden.
     */
    public boolean isHidden() {
        return mHidden;
    }

    /**
     * Returns a string representation of this object.
     */
    public java.lang.String toString() {
        return ((("ModuleInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + mName) + "}";
    }

    /**
     * Describes the kinds of special objects contained in this object.
     */
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public int hashCode() {
        int hashCode = 0;
        hashCode = (31 * hashCode) + java.util.Objects.hashCode(mName);
        hashCode = (31 * hashCode) + java.util.Objects.hashCode(mPackageName);
        hashCode = (31 * hashCode) + java.lang.Boolean.hashCode(mHidden);
        return hashCode;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.content.pm.ModuleInfo)) {
            return false;
        }
        final android.content.pm.ModuleInfo other = ((android.content.pm.ModuleInfo) (obj));
        return (java.util.Objects.equals(mName, other.mName) && java.util.Objects.equals(mPackageName, other.mPackageName)) && (mHidden == other.mHidden);
    }

    /**
     * Flattens this object into the given {@link Parcel}.
     */
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeCharSequence(mName);
        dest.writeString(mPackageName);
        dest.writeBoolean(mHidden);
    }

    private ModuleInfo(android.os.Parcel source) {
        mName = source.readCharSequence();
        mPackageName = source.readString();
        mHidden = source.readBoolean();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.ModuleInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.ModuleInfo>() {
        public android.content.pm.ModuleInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.ModuleInfo(source);
        }

        public android.content.pm.ModuleInfo[] newArray(int size) {
            return new android.content.pm.ModuleInfo[size];
        }
    };
}

