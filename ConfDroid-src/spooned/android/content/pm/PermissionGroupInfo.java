/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Information you can retrieve about a particular security permission
 * group known to the system.  This corresponds to information collected from the
 * AndroidManifest.xml's &lt;permission-group&gt; tags.
 */
public class PermissionGroupInfo extends android.content.pm.PackageItemInfo implements android.os.Parcelable {
    /**
     * A string resource identifier (in the package's resources) of this
     * permission's description.  From the "description" attribute or,
     * if not set, 0.
     */
    @android.annotation.StringRes
    public int descriptionRes;

    /**
     * A string resource identifier (in the package's resources) used to request the permissions.
     * From the "request" attribute or, if not set, 0.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.StringRes
    public int requestRes;

    /**
     * A string resource identifier (in the package's resources) used as subtitle when requesting
     * only access while in the foreground.
     *
     * From the "requestDetail" attribute or, if not set, {@link android.content.res.Resources#ID_NULL}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.StringRes
    public final int requestDetailResourceId;

    /**
     * A string resource identifier (in the package's resources) used when requesting background
     * access. Also used when requesting both foreground and background access.
     *
     * From the "backgroundRequest" attribute or, if not set, {@link android.content.res.Resources#ID_NULL}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.StringRes
    public final int backgroundRequestResourceId;

    /**
     * A string resource identifier (in the package's resources) used as subtitle when requesting
     * background access.
     *
     * From the "backgroundRequestDetail" attribute or, if not set, {@link android.content.res.Resources#ID_NULL}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.StringRes
    public final int backgroundRequestDetailResourceId;

    /**
     * The description string provided in the AndroidManifest file, if any.  You
     * probably don't want to use this, since it will be null if the description
     * is in a resource.  You probably want
     * {@link PermissionInfo#loadDescription} instead.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence nonLocalizedDescription;

    /**
     * Flag for {@link #flags}, corresponding to <code>personalInfo</code>
     * value of {@link android.R.attr#permissionGroupFlags}.
     */
    public static final int FLAG_PERSONAL_INFO = 1 << 0;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "FLAG_" }, value = { android.content.pm.PermissionGroupInfo.FLAG_PERSONAL_INFO })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Flags {}

    /**
     * Additional flags about this group as given by
     * {@link android.R.attr#permissionGroupFlags}.
     */
    @android.content.pm.PermissionGroupInfo.Flags
    public int flags;

    /**
     * Prioritization of this group, for visually sorting with other groups.
     */
    public int priority;

    /**
     *
     *
     * @unknown 
     */
    public PermissionGroupInfo(@android.annotation.StringRes
    int requestDetailResourceId, @android.annotation.StringRes
    int backgroundRequestResourceId, @android.annotation.StringRes
    int backgroundRequestDetailResourceId) {
        this.requestDetailResourceId = requestDetailResourceId;
        this.backgroundRequestResourceId = backgroundRequestResourceId;
        this.backgroundRequestDetailResourceId = backgroundRequestDetailResourceId;
    }

    /**
     *
     *
     * @deprecated Should only be created by the system.
     */
    @java.lang.Deprecated
    public PermissionGroupInfo() {
        this(android.content.res.Resources.ID_NULL, android.content.res.Resources.ID_NULL, android.content.res.Resources.ID_NULL);
    }

    /**
     *
     *
     * @deprecated Should only be created by the system.
     */
    @java.lang.Deprecated
    public PermissionGroupInfo(@android.annotation.NonNull
    android.content.pm.PermissionGroupInfo orig) {
        super(orig);
        descriptionRes = orig.descriptionRes;
        requestRes = orig.requestRes;
        requestDetailResourceId = orig.requestDetailResourceId;
        backgroundRequestResourceId = orig.backgroundRequestResourceId;
        backgroundRequestDetailResourceId = orig.backgroundRequestDetailResourceId;
        nonLocalizedDescription = orig.nonLocalizedDescription;
        flags = orig.flags;
        priority = orig.priority;
    }

    /**
     * Retrieve the textual description of this permission.  This
     * will call back on the given PackageManager to load the description from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the label can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a CharSequence containing the permission's description.
    If there is no description, null is returned.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence loadDescription(@android.annotation.NonNull
    android.content.pm.PackageManager pm) {
        if (nonLocalizedDescription != null) {
            return nonLocalizedDescription;
        }
        if (descriptionRes != 0) {
            java.lang.CharSequence label = pm.getText(packageName, descriptionRes, null);
            if (label != null) {
                return label;
            }
        }
        return null;
    }

    public java.lang.String toString() {
        return ((((("PermissionGroupInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + name) + " flgs=0x") + java.lang.Integer.toHexString(flags)) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeInt(descriptionRes);
        dest.writeInt(requestRes);
        dest.writeInt(requestDetailResourceId);
        dest.writeInt(backgroundRequestResourceId);
        dest.writeInt(backgroundRequestDetailResourceId);
        android.text.TextUtils.writeToParcel(nonLocalizedDescription, dest, parcelableFlags);
        dest.writeInt(flags);
        dest.writeInt(priority);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.PermissionGroupInfo> CREATOR = new android.content.pm.Creator<android.content.pm.PermissionGroupInfo>() {
        public android.content.pm.PermissionGroupInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.PermissionGroupInfo(source);
        }

        public android.content.pm.PermissionGroupInfo[] newArray(int size) {
            return new android.content.pm.PermissionGroupInfo[size];
        }
    };

    private PermissionGroupInfo(android.os.Parcel source) {
        super(source);
        descriptionRes = source.readInt();
        requestRes = source.readInt();
        requestDetailResourceId = source.readInt();
        backgroundRequestResourceId = source.readInt();
        backgroundRequestDetailResourceId = source.readInt();
        nonLocalizedDescription = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        flags = source.readInt();
        priority = source.readInt();
    }
}

