/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.app.admin;


/**
 * This class is used to specify meta information of a device administrator
 * component.
 */
public final class DeviceAdminInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "DeviceAdminInfo";

    /**
     * A type of policy that this device admin can use: device owner meta-policy
     * for an admin that is designated as owner of the device.
     *
     * @unknown 
     */
    public static final int USES_POLICY_DEVICE_OWNER = -2;

    /**
     * A type of policy that this device admin can use: profile owner meta-policy
     * for admins that have been installed as owner of some user profile.
     *
     * @unknown 
     */
    public static final int USES_POLICY_PROFILE_OWNER = -1;

    /**
     * A type of policy that this device admin can use: limit the passwords
     * that the user can select, via {@link DevicePolicyManager#setPasswordQuality}
     * and {@link DevicePolicyManager#setPasswordMinimumLength}.
     *
     * <p>To control this policy, the device admin must have a "limit-password"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_LIMIT_PASSWORD = 0;

    /**
     * A type of policy that this device admin can use: able to watch login
     * attempts from the user, via {@link DeviceAdminReceiver#ACTION_PASSWORD_FAILED},
     * {@link DeviceAdminReceiver#ACTION_PASSWORD_SUCCEEDED}, and
     * {@link DevicePolicyManager#getCurrentFailedPasswordAttempts}.
     *
     * <p>To control this policy, the device admin must have a "watch-login"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_WATCH_LOGIN = 1;

    /**
     * A type of policy that this device admin can use: able to reset the
     * user's password via
     * {@link DevicePolicyManager#resetPassword}.
     *
     * <p>To control this policy, the device admin must have a "reset-password"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_RESET_PASSWORD = 2;

    /**
     * A type of policy that this device admin can use: able to force the device
     * to lock via{@link DevicePolicyManager#lockNow} or limit the
     * maximum lock timeout for the device via
     * {@link DevicePolicyManager#setMaximumTimeToLock}.
     *
     * <p>To control this policy, the device admin must have a "force-lock"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_FORCE_LOCK = 3;

    /**
     * A type of policy that this device admin can use: able to factory
     * reset the device, erasing all of the user's data, via
     * {@link DevicePolicyManager#wipeData}.
     *
     * <p>To control this policy, the device admin must have a "wipe-data"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_WIPE_DATA = 4;

    /**
     * A type of policy that this device admin can use: able to specify the
     * device Global Proxy, via {@link DevicePolicyManager#setGlobalProxy}.
     *
     * <p>To control this policy, the device admin must have a "set-global-proxy"
     * tag in the "uses-policies" section of its meta-data.
     *
     * @unknown 
     */
    public static final int USES_POLICY_SETS_GLOBAL_PROXY = 5;

    /**
     * A type of policy that this device admin can use: force the user to
     * change their password after an administrator-defined time limit.
     *
     * <p>To control this policy, the device admin must have an "expire-password"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_EXPIRE_PASSWORD = 6;

    /**
     * A type of policy that this device admin can use: require encryption of stored data.
     *
     * <p>To control this policy, the device admin must have a "encrypted-storage"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_ENCRYPTED_STORAGE = 7;

    /**
     * A type of policy that this device admin can use: disables use of all device cameras.
     *
     * <p>To control this policy, the device admin must have a "disable-camera"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_DISABLE_CAMERA = 8;

    /**
     * A type of policy that this device admin can use: disables use of keyguard features.
     *
     * <p>To control this policy, the device admin must have a "disable-keyguard-features"
     * tag in the "uses-policies" section of its meta-data.
     */
    public static final int USES_POLICY_DISABLE_KEYGUARD_FEATURES = 9;

    /**
     *
     *
     * @unknown 
     */
    public static class PolicyInfo {
        public final int ident;

        public final java.lang.String tag;

        public final int label;

        public final int description;

        public final int labelForSecondaryUsers;

        public final int descriptionForSecondaryUsers;

        public PolicyInfo(int ident, java.lang.String tag, int label, int description) {
            this(ident, tag, label, description, label, description);
        }

        public PolicyInfo(int ident, java.lang.String tag, int label, int description, int labelForSecondaryUsers, int descriptionForSecondaryUsers) {
            this.ident = ident;
            this.tag = tag;
            this.label = label;
            this.description = description;
            this.labelForSecondaryUsers = labelForSecondaryUsers;
            this.descriptionForSecondaryUsers = descriptionForSecondaryUsers;
        }
    }

    static java.util.ArrayList<android.app.admin.DeviceAdminInfo.PolicyInfo> sPoliciesDisplayOrder = new java.util.ArrayList<android.app.admin.DeviceAdminInfo.PolicyInfo>();

    static java.util.HashMap<java.lang.String, java.lang.Integer> sKnownPolicies = new java.util.HashMap<java.lang.String, java.lang.Integer>();

    static android.util.SparseArray<android.app.admin.DeviceAdminInfo.PolicyInfo> sRevKnownPolicies = new android.util.SparseArray<android.app.admin.DeviceAdminInfo.PolicyInfo>();

    static {
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_WIPE_DATA, "wipe-data", com.android.internal.R.string.policylab_wipeData, com.android.internal.R.string.policydesc_wipeData, com.android.internal.R.string.policylab_wipeData_secondaryUser, com.android.internal.R.string.policydesc_wipeData_secondaryUser));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_RESET_PASSWORD, "reset-password", com.android.internal.R.string.policylab_resetPassword, com.android.internal.R.string.policydesc_resetPassword));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_LIMIT_PASSWORD, "limit-password", com.android.internal.R.string.policylab_limitPassword, com.android.internal.R.string.policydesc_limitPassword));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_WATCH_LOGIN, "watch-login", com.android.internal.R.string.policylab_watchLogin, com.android.internal.R.string.policydesc_watchLogin, com.android.internal.R.string.policylab_watchLogin, com.android.internal.R.string.policydesc_watchLogin_secondaryUser));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_FORCE_LOCK, "force-lock", com.android.internal.R.string.policylab_forceLock, com.android.internal.R.string.policydesc_forceLock));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_SETS_GLOBAL_PROXY, "set-global-proxy", com.android.internal.R.string.policylab_setGlobalProxy, com.android.internal.R.string.policydesc_setGlobalProxy));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_EXPIRE_PASSWORD, "expire-password", com.android.internal.R.string.policylab_expirePassword, com.android.internal.R.string.policydesc_expirePassword));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_ENCRYPTED_STORAGE, "encrypted-storage", com.android.internal.R.string.policylab_encryptedStorage, com.android.internal.R.string.policydesc_encryptedStorage));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_DISABLE_CAMERA, "disable-camera", com.android.internal.R.string.policylab_disableCamera, com.android.internal.R.string.policydesc_disableCamera));
        android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.add(new android.app.admin.DeviceAdminInfo.PolicyInfo(android.app.admin.DeviceAdminInfo.USES_POLICY_DISABLE_KEYGUARD_FEATURES, "disable-keyguard-features", com.android.internal.R.string.policylab_disableKeyguardFeatures, com.android.internal.R.string.policydesc_disableKeyguardFeatures));
        for (int i = 0; i < android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.size(); i++) {
            android.app.admin.DeviceAdminInfo.PolicyInfo pi = android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.get(i);
            android.app.admin.DeviceAdminInfo.sRevKnownPolicies.put(pi.ident, pi);
            android.app.admin.DeviceAdminInfo.sKnownPolicies.put(pi.tag, pi.ident);
        }
    }

    /**
     * The BroadcastReceiver that implements this device admin component.
     */
    final android.content.pm.ActivityInfo mActivityInfo;

    /**
     * Whether this should be visible to the user.
     */
    boolean mVisible;

    /**
     * The policies this administrator needs access to.
     */
    int mUsesPolicies;

    /**
     * Constructor.
     *
     * @param context
     * 		The Context in which we are parsing the device admin.
     * @param resolveInfo
     * 		The ResolveInfo returned from the package manager about
     * 		this device admin's component.
     */
    public DeviceAdminInfo(android.content.Context context, android.content.pm.ResolveInfo resolveInfo) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        this(context, resolveInfo.activityInfo);
    }

    /**
     * Constructor.
     *
     * @param context
     * 		The Context in which we are parsing the device admin.
     * @param activityInfo
     * 		The ActivityInfo returned from the package manager about
     * 		this device admin's component.
     * @unknown 
     */
    public DeviceAdminInfo(android.content.Context context, android.content.pm.ActivityInfo activityInfo) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mActivityInfo = activityInfo;
        android.content.pm.PackageManager pm = context.getPackageManager();
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = mActivityInfo.loadXmlMetaData(pm, android.app.admin.DeviceAdminReceiver.DEVICE_ADMIN_META_DATA);
            if (parser == null) {
                throw new org.xmlpull.v1.XmlPullParserException(("No " + android.app.admin.DeviceAdminReceiver.DEVICE_ADMIN_META_DATA) + " meta-data");
            }
            android.content.res.Resources res = pm.getResourcesForApplication(mActivityInfo.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            java.lang.String nodeName = parser.getName();
            if (!"device-admin".equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with device-admin tag");
            }
            android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.DeviceAdmin);
            mVisible = sa.getBoolean(com.android.internal.R.styleable.DeviceAdmin_visible, true);
            sa.recycle();
            int outerDepth = parser.getDepth();
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
                if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                    continue;
                }
                java.lang.String tagName = parser.getName();
                if (tagName.equals("uses-policies")) {
                    int innerDepth = parser.getDepth();
                    while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
                        if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                            continue;
                        }
                        java.lang.String policyName = parser.getName();
                        java.lang.Integer val = android.app.admin.DeviceAdminInfo.sKnownPolicies.get(policyName);
                        if (val != null) {
                            mUsesPolicies |= 1 << val.intValue();
                        } else {
                            android.util.Log.w(android.app.admin.DeviceAdminInfo.TAG, (("Unknown tag under uses-policies of " + getComponent()) + ": ") + policyName);
                        }
                    } 
                }
            } 
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new org.xmlpull.v1.XmlPullParserException("Unable to create context for: " + mActivityInfo.packageName);
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    DeviceAdminInfo(android.os.Parcel source) {
        mActivityInfo = android.content.pm.ActivityInfo.CREATOR.createFromParcel(source);
        mUsesPolicies = source.readInt();
    }

    /**
     * Return the .apk package that implements this device admin.
     */
    public java.lang.String getPackageName() {
        return mActivityInfo.packageName;
    }

    /**
     * Return the class name of the receiver component that implements
     * this device admin.
     */
    public java.lang.String getReceiverName() {
        return mActivityInfo.name;
    }

    /**
     * Return the raw information about the receiver implementing this
     * device admin.  Do not modify the returned object.
     */
    public android.content.pm.ActivityInfo getActivityInfo() {
        return mActivityInfo;
    }

    /**
     * Return the component of the receiver that implements this device admin.
     */
    @android.annotation.NonNull
    public android.content.ComponentName getComponent() {
        return new android.content.ComponentName(mActivityInfo.packageName, mActivityInfo.name);
    }

    /**
     * Load the user-displayed label for this device admin.
     *
     * @param pm
     * 		Supply a PackageManager used to load the device admin's
     * 		resources.
     */
    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        return mActivityInfo.loadLabel(pm);
    }

    /**
     * Load user-visible description associated with this device admin.
     *
     * @param pm
     * 		Supply a PackageManager used to load the device admin's
     * 		resources.
     */
    public java.lang.CharSequence loadDescription(android.content.pm.PackageManager pm) throws android.content.res.Resources.NotFoundException {
        if (mActivityInfo.descriptionRes != 0) {
            return pm.getText(mActivityInfo.packageName, mActivityInfo.descriptionRes, mActivityInfo.applicationInfo);
        }
        throw new android.content.res.Resources.NotFoundException();
    }

    /**
     * Load the user-displayed icon for this device admin.
     *
     * @param pm
     * 		Supply a PackageManager used to load the device admin's
     * 		resources.
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return mActivityInfo.loadIcon(pm);
    }

    /**
     * Returns whether this device admin would like to be visible to the
     * user, even when it is not enabled.
     */
    public boolean isVisible() {
        return mVisible;
    }

    /**
     * Return true if the device admin has requested that it be able to use
     * the given policy control.  The possible policy identifier inputs are:
     * {@link #USES_POLICY_LIMIT_PASSWORD}, {@link #USES_POLICY_WATCH_LOGIN},
     * {@link #USES_POLICY_RESET_PASSWORD}, {@link #USES_POLICY_FORCE_LOCK},
     * {@link #USES_POLICY_WIPE_DATA},
     * {@link #USES_POLICY_EXPIRE_PASSWORD}, {@link #USES_ENCRYPTED_STORAGE},
     * {@link #USES_POLICY_DISABLE_CAMERA}.
     */
    public boolean usesPolicy(int policyIdent) {
        return (mUsesPolicies & (1 << policyIdent)) != 0;
    }

    /**
     * Return the XML tag name for the given policy identifier.  Valid identifiers
     * are as per {@link #usesPolicy(int)}.  If the given identifier is not
     * known, null is returned.
     */
    public java.lang.String getTagForPolicy(int policyIdent) {
        return android.app.admin.DeviceAdminInfo.sRevKnownPolicies.get(policyIdent).tag;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.util.ArrayList<android.app.admin.DeviceAdminInfo.PolicyInfo> getUsedPolicies() {
        java.util.ArrayList<android.app.admin.DeviceAdminInfo.PolicyInfo> res = new java.util.ArrayList<android.app.admin.DeviceAdminInfo.PolicyInfo>();
        for (int i = 0; i < android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.size(); i++) {
            android.app.admin.DeviceAdminInfo.PolicyInfo pi = android.app.admin.DeviceAdminInfo.sPoliciesDisplayOrder.get(i);
            if (usesPolicy(pi.ident)) {
                res.add(pi);
            }
        }
        return res;
    }

    /**
     *
     *
     * @unknown 
     */
    public void writePoliciesToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        out.attribute(null, "flags", java.lang.Integer.toString(mUsesPolicies));
    }

    /**
     *
     *
     * @unknown 
     */
    public void readPoliciesFromXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mUsesPolicies = java.lang.Integer.parseInt(parser.getAttributeValue(null, "flags"));
    }

    public void dump(android.util.Printer pw, java.lang.String prefix) {
        pw.println(prefix + "Receiver:");
        mActivityInfo.dump(pw, prefix + "  ");
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("DeviceAdminInfo{" + mActivityInfo.name) + "}";
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        mActivityInfo.writeToParcel(dest, flags);
        dest.writeInt(mUsesPolicies);
    }

    /**
     * Used to make this class parcelable.
     */
    public static final android.os.Parcelable.Creator<android.app.admin.DeviceAdminInfo> CREATOR = new android.os.Parcelable.Creator<android.app.admin.DeviceAdminInfo>() {
        public android.app.admin.DeviceAdminInfo createFromParcel(android.os.Parcel source) {
            return new android.app.admin.DeviceAdminInfo(source);
        }

        public android.app.admin.DeviceAdminInfo[] newArray(int size) {
            return new android.app.admin.DeviceAdminInfo[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}

