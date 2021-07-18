/**
 * Copyright (C) 2007-2008 The Android Open Source Project
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
 * This class is used to specify meta information of an input method.
 *
 * <p>It should be defined in an XML resource file with an {@code <input-method>} element.
 * For more information, see the guide to
 * <a href="{@docRoot }guide/topics/text/creating-input-method.html">
 * Creating an Input Method</a>.</p>
 *
 * @see InputMethodSubtype
 * @unknown ref android.R.styleable#InputMethod_settingsActivity
 * @unknown ref android.R.styleable#InputMethod_isDefault
 * @unknown ref android.R.styleable#InputMethod_supportsSwitchingToNextInputMethod
 */
public final class InputMethodInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "InputMethodInfo";

    /**
     * The Service that implements this input method component.
     */
    final android.content.pm.ResolveInfo mService;

    /**
     * IME only supports VR mode.
     */
    final boolean mIsVrOnly;

    /**
     * The unique string Id to identify the input method.  This is generated
     * from the input method component.
     */
    final java.lang.String mId;

    /**
     * The input method setting activity's name, used by the system settings to
     * launch the setting activity of this input method.
     */
    final java.lang.String mSettingsActivityName;

    /**
     * The resource in the input method's .apk that holds a boolean indicating
     * whether it should be considered the default input method for this
     * system.  This is a resource ID instead of the final value so that it
     * can change based on the configuration (in particular locale).
     */
    final int mIsDefaultResId;

    /**
     * An array-like container of the subtypes.
     */
    @android.annotation.UnsupportedAppUsage
    private final android.view.inputmethod.InputMethodSubtypeArray mSubtypes;

    private final boolean mIsAuxIme;

    /**
     * Caveat: mForceDefault must be false for production. This flag is only for test.
     */
    private final boolean mForceDefault;

    /**
     * The flag whether this IME supports ways to switch to a next input method (e.g. globe key.)
     */
    private final boolean mSupportsSwitchingToNextInputMethod;

    /**
     *
     *
     * @param service
     * 		the {@link ResolveInfo} corresponds in which the IME is implemented.
     * @return a unique ID to be returned by {@link #getId()}. We have used
    {@link ComponentName#flattenToShortString()} for this purpose (and it is already
    unrealistic to switch to a different scheme as it is already implicitly assumed in
    many places).
     * @unknown 
     */
    public static java.lang.String computeId(@android.annotation.NonNull
    android.content.pm.ResolveInfo service) {
        final android.content.pm.ServiceInfo si = service.serviceInfo;
        return new android.content.ComponentName(si.packageName, si.name).flattenToShortString();
    }

    /**
     * Constructor.
     *
     * @param context
     * 		The Context in which we are parsing the input method.
     * @param service
     * 		The ResolveInfo returned from the package manager about
     * 		this input method's component.
     */
    public InputMethodInfo(android.content.Context context, android.content.pm.ResolveInfo service) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        this(context, service, null);
    }

    /**
     * Constructor.
     *
     * @param context
     * 		The Context in which we are parsing the input method.
     * @param service
     * 		The ResolveInfo returned from the package manager about
     * 		this input method's component.
     * @param additionalSubtypes
     * 		additional subtypes being added to this InputMethodInfo
     * @unknown 
     */
    public InputMethodInfo(android.content.Context context, android.content.pm.ResolveInfo service, java.util.List<android.view.inputmethod.InputMethodSubtype> additionalSubtypes) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mService = service;
        android.content.pm.ServiceInfo si = service.serviceInfo;
        mId = android.view.inputmethod.InputMethodInfo.computeId(service);
        boolean isAuxIme = true;
        boolean supportsSwitchingToNextInputMethod = false;// false as default

        mForceDefault = false;
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.lang.String settingsActivityComponent = null;
        boolean isVrOnly;
        int isDefaultResId = 0;
        android.content.res.XmlResourceParser parser = null;
        final java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> subtypes = new java.util.ArrayList<android.view.inputmethod.InputMethodSubtype>();
        try {
            parser = si.loadXmlMetaData(pm, android.view.inputmethod.InputMethod.SERVICE_META_DATA);
            if (parser == null) {
                throw new org.xmlpull.v1.XmlPullParserException(("No " + android.view.inputmethod.InputMethod.SERVICE_META_DATA) + " meta-data");
            }
            android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            java.lang.String nodeName = parser.getName();
            if (!"input-method".equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with input-method tag");
            }
            android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.InputMethod);
            settingsActivityComponent = sa.getString(com.android.internal.R.styleable.InputMethod_settingsActivity);
            isVrOnly = sa.getBoolean(com.android.internal.R.styleable.InputMethod_isVrOnly, false);
            isDefaultResId = sa.getResourceId(com.android.internal.R.styleable.InputMethod_isDefault, 0);
            supportsSwitchingToNextInputMethod = sa.getBoolean(com.android.internal.R.styleable.InputMethod_supportsSwitchingToNextInputMethod, false);
            sa.recycle();
            final int depth = parser.getDepth();
            // Parse all subtypes
            while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                if (type == org.xmlpull.v1.XmlPullParser.START_TAG) {
                    nodeName = parser.getName();
                    if (!"subtype".equals(nodeName)) {
                        throw new org.xmlpull.v1.XmlPullParserException("Meta-data in input-method does not start with subtype tag");
                    }
                    final android.content.res.TypedArray a = res.obtainAttributes(attrs, com.android.internal.R.styleable.InputMethod_Subtype);
                    final android.view.inputmethod.InputMethodSubtype subtype = new android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder().setSubtypeNameResId(a.getResourceId(com.android.internal.R.styleable.InputMethod_Subtype_label, 0)).setSubtypeIconResId(a.getResourceId(com.android.internal.R.styleable.InputMethod_Subtype_icon, 0)).setLanguageTag(a.getString(com.android.internal.R.styleable.InputMethod_Subtype_languageTag)).setSubtypeLocale(a.getString(com.android.internal.R.styleable.InputMethod_Subtype_imeSubtypeLocale)).setSubtypeMode(a.getString(com.android.internal.R.styleable.InputMethod_Subtype_imeSubtypeMode)).setSubtypeExtraValue(a.getString(com.android.internal.R.styleable.InputMethod_Subtype_imeSubtypeExtraValue)).setIsAuxiliary(a.getBoolean(com.android.internal.R.styleable.InputMethod_Subtype_isAuxiliary, false)).setOverridesImplicitlyEnabledSubtype(a.getBoolean(com.android.internal.R.styleable.InputMethod_Subtype_overridesImplicitlyEnabledSubtype, false)).setSubtypeId(/* use Arrays.hashCode */
                    a.getInt(com.android.internal.R.styleable.InputMethod_Subtype_subtypeId, 0)).setIsAsciiCapable(a.getBoolean(com.android.internal.R.styleable.InputMethod_Subtype_isAsciiCapable, false)).build();
                    if (!subtype.isAuxiliary()) {
                        isAuxIme = false;
                    }
                    subtypes.add(subtype);
                }
            } 
        } catch (android.content.pm.PackageManager.NameNotFoundException | java.lang.IndexOutOfBoundsException | java.lang.NumberFormatException e) {
            throw new org.xmlpull.v1.XmlPullParserException("Unable to create context for: " + si.packageName);
        } finally {
            if (parser != null)
                parser.close();

        }
        if (subtypes.size() == 0) {
            isAuxIme = false;
        }
        if (additionalSubtypes != null) {
            final int N = additionalSubtypes.size();
            for (int i = 0; i < N; ++i) {
                final android.view.inputmethod.InputMethodSubtype subtype = additionalSubtypes.get(i);
                if (!subtypes.contains(subtype)) {
                    subtypes.add(subtype);
                } else {
                    android.util.Slog.w(android.view.inputmethod.InputMethodInfo.TAG, (("Duplicated subtype definition found: " + subtype.getLocale()) + ", ") + subtype.getMode());
                }
            }
        }
        mSubtypes = new android.view.inputmethod.InputMethodSubtypeArray(subtypes);
        mSettingsActivityName = settingsActivityComponent;
        mIsDefaultResId = isDefaultResId;
        mIsAuxIme = isAuxIme;
        mSupportsSwitchingToNextInputMethod = supportsSwitchingToNextInputMethod;
        mIsVrOnly = isVrOnly;
    }

    InputMethodInfo(android.os.Parcel source) {
        mId = source.readString();
        mSettingsActivityName = source.readString();
        mIsDefaultResId = source.readInt();
        mIsAuxIme = source.readInt() == 1;
        mSupportsSwitchingToNextInputMethod = source.readInt() == 1;
        mIsVrOnly = source.readBoolean();
        mService = android.content.pm.ResolveInfo.CREATOR.createFromParcel(source);
        mSubtypes = new android.view.inputmethod.InputMethodSubtypeArray(source);
        mForceDefault = false;
    }

    /**
     * Temporary API for creating a built-in input method for test.
     */
    public InputMethodInfo(java.lang.String packageName, java.lang.String className, java.lang.CharSequence label, java.lang.String settingsActivity) {
        /* isAuxIme */
        /* subtypes */
        /* isDefaultResId */
        /* forceDefault */
        /* supportsSwitchingToNextInputMethod */
        /* isVrOnly */
        this(android.view.inputmethod.InputMethodInfo.buildDummyResolveInfo(packageName, className, label), false, settingsActivity, null, 0, false, true, false);
    }

    /**
     * Temporary API for creating a built-in input method for test.
     *
     * @unknown 
     */
    public InputMethodInfo(android.content.pm.ResolveInfo ri, boolean isAuxIme, java.lang.String settingsActivity, java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault) {
        /* supportsSwitchingToNextInputMethod */
        /* isVrOnly */
        this(ri, isAuxIme, settingsActivity, subtypes, isDefaultResId, forceDefault, true, false);
    }

    /**
     * Temporary API for creating a built-in input method for test.
     *
     * @unknown 
     */
    public InputMethodInfo(android.content.pm.ResolveInfo ri, boolean isAuxIme, java.lang.String settingsActivity, java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault, boolean supportsSwitchingToNextInputMethod, boolean isVrOnly) {
        final android.content.pm.ServiceInfo si = ri.serviceInfo;
        mService = ri;
        mId = new android.content.ComponentName(si.packageName, si.name).flattenToShortString();
        mSettingsActivityName = settingsActivity;
        mIsDefaultResId = isDefaultResId;
        mIsAuxIme = isAuxIme;
        mSubtypes = new android.view.inputmethod.InputMethodSubtypeArray(subtypes);
        mForceDefault = forceDefault;
        mSupportsSwitchingToNextInputMethod = supportsSwitchingToNextInputMethod;
        mIsVrOnly = isVrOnly;
    }

    private static android.content.pm.ResolveInfo buildDummyResolveInfo(java.lang.String packageName, java.lang.String className, java.lang.CharSequence label) {
        android.content.pm.ResolveInfo ri = new android.content.pm.ResolveInfo();
        android.content.pm.ServiceInfo si = new android.content.pm.ServiceInfo();
        android.content.pm.ApplicationInfo ai = new android.content.pm.ApplicationInfo();
        ai.packageName = packageName;
        ai.enabled = true;
        si.applicationInfo = ai;
        si.enabled = true;
        si.packageName = packageName;
        si.name = className;
        si.exported = true;
        si.nonLocalizedLabel = label;
        ri.serviceInfo = si;
        return ri;
    }

    /**
     * Return a unique ID for this input method.  The ID is generated from
     * the package and class name implementing the method.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Return the .apk package that implements this input method.
     */
    public java.lang.String getPackageName() {
        return mService.serviceInfo.packageName;
    }

    /**
     * Return the class name of the service component that implements
     * this input method.
     */
    public java.lang.String getServiceName() {
        return mService.serviceInfo.name;
    }

    /**
     * Return the raw information about the Service implementing this
     * input method.  Do not modify the returned object.
     */
    public android.content.pm.ServiceInfo getServiceInfo() {
        return mService.serviceInfo;
    }

    /**
     * Return the component of the service that implements this input
     * method.
     */
    public android.content.ComponentName getComponent() {
        return new android.content.ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
    }

    /**
     * Load the user-displayed label for this input method.
     *
     * @param pm
     * 		Supply a PackageManager used to load the input method's
     * 		resources.
     */
    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        return mService.loadLabel(pm);
    }

    /**
     * Load the user-displayed icon for this input method.
     *
     * @param pm
     * 		Supply a PackageManager used to load the input method's
     * 		resources.
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return mService.loadIcon(pm);
    }

    /**
     * Return the class name of an activity that provides a settings UI for
     * the input method.  You can launch this activity be starting it with
     * an {@link android.content.Intent} whose action is MAIN and with an
     * explicit {@link android.content.ComponentName}
     * composed of {@link #getPackageName} and the class name returned here.
     *
     * <p>A null will be returned if there is no settings activity associated
     * with the input method.</p>
     */
    public java.lang.String getSettingsActivity() {
        return mSettingsActivityName;
    }

    /**
     * Returns true if IME supports VR mode only.
     *
     * @unknown 
     */
    public boolean isVrOnly() {
        return mIsVrOnly;
    }

    /**
     * Return the count of the subtypes of Input Method.
     */
    public int getSubtypeCount() {
        return mSubtypes.getCount();
    }

    /**
     * Return the Input Method's subtype at the specified index.
     *
     * @param index
     * 		the index of the subtype to return.
     */
    public android.view.inputmethod.InputMethodSubtype getSubtypeAt(int index) {
        return mSubtypes.get(index);
    }

    /**
     * Return the resource identifier of a resource inside of this input
     * method's .apk that determines whether it should be considered a
     * default input method for the system.
     */
    public int getIsDefaultResourceId() {
        return mIsDefaultResId;
    }

    /**
     * Return whether or not this ime is a default ime or not.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isDefault(android.content.Context context) {
        if (mForceDefault) {
            return true;
        }
        try {
            if (getIsDefaultResourceId() == 0) {
                return false;
            }
            final android.content.res.Resources res = context.createPackageContext(getPackageName(), 0).getResources();
            return res.getBoolean(getIsDefaultResourceId());
        } catch (android.content.pm.PackageManager.NameNotFoundException | android.content.res.Resources.NotFoundException e) {
            return false;
        }
    }

    public void dump(android.util.Printer pw, java.lang.String prefix) {
        pw.println((((((((prefix + "mId=") + mId) + " mSettingsActivityName=") + mSettingsActivityName) + " mIsVrOnly=") + mIsVrOnly) + " mSupportsSwitchingToNextInputMethod=") + mSupportsSwitchingToNextInputMethod);
        pw.println((prefix + "mIsDefaultResId=0x") + java.lang.Integer.toHexString(mIsDefaultResId));
        pw.println(prefix + "Service:");
        mService.dump(pw, prefix + "  ");
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("InputMethodInfo{" + mId) + ", settings: ") + mSettingsActivityName) + "}";
    }

    /**
     * Used to test whether the given parameter object is an
     * {@link InputMethodInfo} and its Id is the same to this one.
     *
     * @return true if the given parameter object is an
    {@link InputMethodInfo} and its Id is the same to this one.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this)
            return true;

        if (o == null)
            return false;

        if (!(o instanceof android.view.inputmethod.InputMethodInfo))
            return false;

        android.view.inputmethod.InputMethodInfo obj = ((android.view.inputmethod.InputMethodInfo) (o));
        return mId.equals(obj.mId);
    }

    @java.lang.Override
    public int hashCode() {
        return mId.hashCode();
    }

    /**
     *
     *
     * @unknown 
     * @return {@code true} if the IME is a trusted system component (e.g. pre-installed)
     */
    public boolean isSystem() {
        return (mService.serviceInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isAuxiliaryIme() {
        return mIsAuxIme;
    }

    /**
     *
     *
     * @return true if this input method supports ways to switch to a next input method.
     * @unknown 
     */
    public boolean supportsSwitchingToNextInputMethod() {
        return mSupportsSwitchingToNextInputMethod;
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
        dest.writeString(mId);
        dest.writeString(mSettingsActivityName);
        dest.writeInt(mIsDefaultResId);
        dest.writeInt(mIsAuxIme ? 1 : 0);
        dest.writeInt(mSupportsSwitchingToNextInputMethod ? 1 : 0);
        dest.writeBoolean(mIsVrOnly);
        mService.writeToParcel(dest, flags);
        mSubtypes.writeToParcel(dest);
    }

    /**
     * Used to make this class parcelable.
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InputMethodInfo> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InputMethodInfo>() {
        @java.lang.Override
        public android.view.inputmethod.InputMethodInfo createFromParcel(android.os.Parcel source) {
            return new android.view.inputmethod.InputMethodInfo(source);
        }

        @java.lang.Override
        public android.view.inputmethod.InputMethodInfo[] newArray(int size) {
            return new android.view.inputmethod.InputMethodInfo[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

