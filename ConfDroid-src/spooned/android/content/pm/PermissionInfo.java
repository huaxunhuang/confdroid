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
 * known to the system.  This corresponds to information collected from the
 * AndroidManifest.xml's &lt;permission&gt; tags.
 */
public class PermissionInfo extends android.content.pm.PackageItemInfo implements android.os.Parcelable {
    /**
     * A normal application value for {@link #protectionLevel}, corresponding
     * to the <code>normal</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_NORMAL = 0;

    /**
     * Dangerous value for {@link #protectionLevel}, corresponding
     * to the <code>dangerous</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_DANGEROUS = 1;

    /**
     * System-level value for {@link #protectionLevel}, corresponding
     * to the <code>signature</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_SIGNATURE = 2;

    /**
     *
     *
     * @deprecated Use {@link #PROTECTION_SIGNATURE}|{@link #PROTECTION_FLAG_PRIVILEGED}
    instead.
     */
    @java.lang.Deprecated
    public static final int PROTECTION_SIGNATURE_OR_SYSTEM = 3;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = false, prefix = { "PROTECTION_" }, value = { android.content.pm.PermissionInfo.PROTECTION_NORMAL, android.content.pm.PermissionInfo.PROTECTION_DANGEROUS, android.content.pm.PermissionInfo.PROTECTION_SIGNATURE, android.content.pm.PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Protection {}

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>privileged</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_PRIVILEGED = 0x10;

    /**
     *
     *
     * @deprecated Old name for {@link #PROTECTION_FLAG_PRIVILEGED}, which
    is now very confusing because it only applies to privileged apps, not all
    apps on the system image.
     */
    @java.lang.Deprecated
    public static final int PROTECTION_FLAG_SYSTEM = 0x10;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>development</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_DEVELOPMENT = 0x20;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>appop</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_APPOP = 0x40;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>pre23</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_PRE23 = 0x80;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>installer</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_INSTALLER = 0x100;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>verifier</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_VERIFIER = 0x200;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>preinstalled</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_PREINSTALLED = 0x400;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>setup</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_SETUP = 0x800;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>instant</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_INSTANT = 0x1000;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>runtime</code> value of
     * {@link android.R.attr#protectionLevel}.
     */
    public static final int PROTECTION_FLAG_RUNTIME_ONLY = 0x2000;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>oem</code> value of
     * {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_OEM = 0x4000;

    /**
     * Additional flag for {${link #protectionLevel}, corresponding
     * to the <code>vendorPrivileged</code> value of
     * {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_VENDOR_PRIVILEGED = 0x8000;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>text_classifier</code> value of
     * {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER = 0x10000;

    /**
     * Additional flag for {${link #protectionLevel}, corresponding
     * to the <code>wellbeing</code> value of
     * {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_WELLBEING = 0x20000;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding to the
     * {@code documenter} value of {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_DOCUMENTER = 0x40000;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding to the
     * {@code configurator} value of {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_CONFIGURATOR = 0x80000;

    /**
     * Additional flag for {${link #protectionLevel}, corresponding
     * to the <code>incident_report_approver</code> value of
     * {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_INCIDENT_REPORT_APPROVER = 0x100000;

    /**
     * Additional flag for {@link #protectionLevel}, corresponding
     * to the <code>app_predictor</code> value of
     * {@link android.R.attr#protectionLevel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    public static final int PROTECTION_FLAG_APP_PREDICTOR = 0x200000;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "PROTECTION_FLAG_" }, value = { android.content.pm.PermissionInfo.PROTECTION_FLAG_PRIVILEGED, android.content.pm.PermissionInfo.PROTECTION_FLAG_SYSTEM, android.content.pm.PermissionInfo.PROTECTION_FLAG_DEVELOPMENT, android.content.pm.PermissionInfo.PROTECTION_FLAG_APPOP, android.content.pm.PermissionInfo.PROTECTION_FLAG_PRE23, android.content.pm.PermissionInfo.PROTECTION_FLAG_INSTALLER, android.content.pm.PermissionInfo.PROTECTION_FLAG_VERIFIER, android.content.pm.PermissionInfo.PROTECTION_FLAG_PREINSTALLED, android.content.pm.PermissionInfo.PROTECTION_FLAG_SETUP, android.content.pm.PermissionInfo.PROTECTION_FLAG_INSTANT, android.content.pm.PermissionInfo.PROTECTION_FLAG_RUNTIME_ONLY, android.content.pm.PermissionInfo.PROTECTION_FLAG_OEM, android.content.pm.PermissionInfo.PROTECTION_FLAG_VENDOR_PRIVILEGED, android.content.pm.PermissionInfo.PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER, android.content.pm.PermissionInfo.PROTECTION_FLAG_WELLBEING, android.content.pm.PermissionInfo.PROTECTION_FLAG_DOCUMENTER, android.content.pm.PermissionInfo.PROTECTION_FLAG_CONFIGURATOR, android.content.pm.PermissionInfo.PROTECTION_FLAG_INCIDENT_REPORT_APPROVER, android.content.pm.PermissionInfo.PROTECTION_FLAG_APP_PREDICTOR })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ProtectionFlags {}

    /**
     * Mask for {@link #protectionLevel}: the basic protection type.
     *
     * @deprecated Use #getProtection() instead.
     */
    @java.lang.Deprecated
    public static final int PROTECTION_MASK_BASE = 0xf;

    /**
     * Mask for {@link #protectionLevel}: additional flag bits.
     *
     * @deprecated Use #getProtectionFlags() instead.
     */
    @java.lang.Deprecated
    public static final int PROTECTION_MASK_FLAGS = 0xfff0;

    /**
     * The level of access this permission is protecting, as per
     * {@link android.R.attr#protectionLevel}. Consists of
     * a base permission type and zero or more flags. Use the following functions
     * to extract them.
     *
     * <pre>
     * int basePermissionType = permissionInfo.getProtection();
     * int permissionFlags = permissionInfo.getProtectionFlags();
     * </pre>
     *
     * <p></p>Base permission types are {@link #PROTECTION_NORMAL},
     * {@link #PROTECTION_DANGEROUS}, {@link #PROTECTION_SIGNATURE}
     * and the deprecated {@link #PROTECTION_SIGNATURE_OR_SYSTEM}.
     * Flags are listed under {@link android.R.attr#protectionLevel}.
     *
     * @deprecated Use #getProtection() and #getProtectionFlags() instead.
     */
    @java.lang.Deprecated
    public int protectionLevel;

    /**
     * The group this permission is a part of, as per
     * {@link android.R.attr#permissionGroup}.
     */
    @android.annotation.Nullable
    public java.lang.String group;

    /**
     * Flag for {@link #flags}, corresponding to <code>costsMoney</code>
     * value of {@link android.R.attr#permissionFlags}.
     */
    public static final int FLAG_COSTS_MONEY = 1 << 0;

    /**
     * Flag for {@link #flags}, corresponding to <code>removed</code>
     * value of {@link android.R.attr#permissionFlags}.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.SystemApi
    public static final int FLAG_REMOVED = 1 << 1;

    /**
     * Flag for {@link #flags}, corresponding to <code>hardRestricted</code>
     * value of {@link android.R.attr#permissionFlags}.
     *
     * <p> This permission is restricted by the platform and it would be
     * grantable only to apps that meet special criteria per platform
     * policy.
     */
    public static final int FLAG_HARD_RESTRICTED = 1 << 2;

    /**
     * Flag for {@link #flags}, corresponding to <code>softRestricted</code>
     * value of {@link android.R.attr#permissionFlags}.
     *
     * <p>This permission is restricted by the platform and it would be
     * grantable in its full form to apps that meet special criteria
     * per platform policy. Otherwise, a weaker form of the permission
     * would be granted. The weak grant depends on the permission.
     */
    public static final int FLAG_SOFT_RESTRICTED = 1 << 3;

    /**
     * Flag for {@link #flags}, corresponding to <code>immutablyRestricted</code>
     * value of {@link android.R.attr#permissionFlags}.
     *
     * <p>This permission is restricted immutably which means that its
     * restriction state may be specified only on the first install of
     * the app and will stay in this initial whitelist state until
     * the app is uninstalled.
     */
    public static final int FLAG_IMMUTABLY_RESTRICTED = 1 << 4;

    /**
     * Flag for {@link #flags}, indicating that this permission has been
     * installed into the system's globally defined permissions.
     */
    public static final int FLAG_INSTALLED = 1 << 30;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "FLAG_" }, value = { android.content.pm.PermissionInfo.FLAG_COSTS_MONEY, android.content.pm.PermissionInfo.FLAG_REMOVED, android.content.pm.PermissionInfo.FLAG_HARD_RESTRICTED, android.content.pm.PermissionInfo.FLAG_SOFT_RESTRICTED, android.content.pm.PermissionInfo.FLAG_IMMUTABLY_RESTRICTED, android.content.pm.PermissionInfo.FLAG_INSTALLED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Flags {}

    /**
     * Additional flags about this permission as given by
     * {@link android.R.attr#permissionFlags}.
     */
    @android.content.pm.PermissionInfo.Flags
    public int flags;

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
     * Some permissions only grant access while the app is in foreground. Some of these permissions
     * allow to add background capabilities by adding another permission.
     *
     * If this is such a permission, this is the name of the permission adding the background
     * access.
     *
     * From the "backgroundPermission" attribute or, if not set null
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    @android.annotation.Nullable
    public final java.lang.String backgroundPermission;

    /**
     * The description string provided in the AndroidManifest file, if any.  You
     * probably don't want to use this, since it will be null if the description
     * is in a resource.  You probably want
     * {@link PermissionInfo#loadDescription} instead.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence nonLocalizedDescription;

    /**
     *
     *
     * @unknown 
     */
    public static int fixProtectionLevel(int level) {
        if (level == android.content.pm.PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM) {
            level = android.content.pm.PermissionInfo.PROTECTION_SIGNATURE | android.content.pm.PermissionInfo.PROTECTION_FLAG_PRIVILEGED;
        }
        if (((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_VENDOR_PRIVILEGED) != 0) && ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_PRIVILEGED) == 0)) {
            // 'vendorPrivileged' must be 'privileged'. If not,
            // drop the vendorPrivileged.
            level = level & (~android.content.pm.PermissionInfo.PROTECTION_FLAG_VENDOR_PRIVILEGED);
        }
        return level;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public static java.lang.String protectionToString(int level) {
        java.lang.String protLevel = "????";
        switch (level & android.content.pm.PermissionInfo.PROTECTION_MASK_BASE) {
            case android.content.pm.PermissionInfo.PROTECTION_DANGEROUS :
                protLevel = "dangerous";
                break;
            case android.content.pm.PermissionInfo.PROTECTION_NORMAL :
                protLevel = "normal";
                break;
            case android.content.pm.PermissionInfo.PROTECTION_SIGNATURE :
                protLevel = "signature";
                break;
            case android.content.pm.PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM :
                protLevel = "signatureOrSystem";
                break;
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_PRIVILEGED) != 0) {
            protLevel += "|privileged";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_DEVELOPMENT) != 0) {
            protLevel += "|development";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_APPOP) != 0) {
            protLevel += "|appop";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_PRE23) != 0) {
            protLevel += "|pre23";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_INSTALLER) != 0) {
            protLevel += "|installer";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_VERIFIER) != 0) {
            protLevel += "|verifier";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_PREINSTALLED) != 0) {
            protLevel += "|preinstalled";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_SETUP) != 0) {
            protLevel += "|setup";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_INSTANT) != 0) {
            protLevel += "|instant";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_RUNTIME_ONLY) != 0) {
            protLevel += "|runtime";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_OEM) != 0) {
            protLevel += "|oem";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_VENDOR_PRIVILEGED) != 0) {
            protLevel += "|vendorPrivileged";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER) != 0) {
            protLevel += "|textClassifier";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_WELLBEING) != 0) {
            protLevel += "|wellbeing";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_DOCUMENTER) != 0) {
            protLevel += "|documenter";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_CONFIGURATOR) != 0) {
            protLevel += "|configurator";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_INCIDENT_REPORT_APPROVER) != 0) {
            protLevel += "|incidentReportApprover";
        }
        if ((level & android.content.pm.PermissionInfo.PROTECTION_FLAG_APP_PREDICTOR) != 0) {
            protLevel += "|appPredictor";
        }
        return protLevel;
    }

    /**
     *
     *
     * @unknown 
     */
    public PermissionInfo(@android.annotation.Nullable
    java.lang.String backgroundPermission) {
        this.backgroundPermission = backgroundPermission;
    }

    /**
     *
     *
     * @deprecated Should only be created by the system.
     */
    @java.lang.Deprecated
    public PermissionInfo() {
        this(((java.lang.String) (null)));
    }

    /**
     *
     *
     * @deprecated Should only be created by the system.
     */
    @java.lang.Deprecated
    public PermissionInfo(@android.annotation.NonNull
    android.content.pm.PermissionInfo orig) {
        super(orig);
        protectionLevel = orig.protectionLevel;
        flags = orig.flags;
        group = orig.group;
        backgroundPermission = orig.backgroundPermission;
        descriptionRes = orig.descriptionRes;
        requestRes = orig.requestRes;
        nonLocalizedDescription = orig.nonLocalizedDescription;
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

    /**
     * Return the base permission type.
     */
    @android.content.pm.PermissionInfo.Protection
    public int getProtection() {
        return protectionLevel & android.content.pm.PermissionInfo.PROTECTION_MASK_BASE;
    }

    /**
     * Return the additional flags in {@link #protectionLevel}.
     */
    @android.content.pm.PermissionInfo.ProtectionFlags
    public int getProtectionFlags() {
        return protectionLevel & (~android.content.pm.PermissionInfo.PROTECTION_MASK_BASE);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("PermissionInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + name) + "}";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeInt(protectionLevel);
        dest.writeInt(flags);
        dest.writeString(group);
        dest.writeString(backgroundPermission);
        dest.writeInt(descriptionRes);
        dest.writeInt(requestRes);
        android.text.TextUtils.writeToParcel(nonLocalizedDescription, dest, parcelableFlags);
    }

    /**
     *
     *
     * @unknown 
     */
    public int calculateFootprint() {
        int size = name.length();
        if (nonLocalizedLabel != null) {
            size += nonLocalizedLabel.length();
        }
        if (nonLocalizedDescription != null) {
            size += nonLocalizedDescription.length();
        }
        return size;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isHardRestricted() {
        return (flags & android.content.pm.PermissionInfo.FLAG_HARD_RESTRICTED) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isSoftRestricted() {
        return (flags & android.content.pm.PermissionInfo.FLAG_SOFT_RESTRICTED) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isRestricted() {
        return isHardRestricted() || isSoftRestricted();
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isAppOp() {
        return (protectionLevel & android.content.pm.PermissionInfo.PROTECTION_FLAG_APPOP) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isRuntime() {
        return getProtection() == android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.PermissionInfo> CREATOR = new android.content.pm.Creator<android.content.pm.PermissionInfo>() {
        @java.lang.Override
        public android.content.pm.PermissionInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.PermissionInfo(source);
        }

        @java.lang.Override
        public android.content.pm.PermissionInfo[] newArray(int size) {
            return new android.content.pm.PermissionInfo[size];
        }
    };

    private PermissionInfo(android.os.Parcel source) {
        super(source);
        protectionLevel = source.readInt();
        flags = source.readInt();
        group = source.readString();
        backgroundPermission = source.readString();
        descriptionRes = source.readInt();
        requestRes = source.readInt();
        nonLocalizedDescription = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
    }
}

