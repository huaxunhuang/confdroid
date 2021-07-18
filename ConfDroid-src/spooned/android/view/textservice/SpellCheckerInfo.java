/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.view.textservice;


/**
 * This class is used to specify meta information of a spell checker.
 */
public final class SpellCheckerInfo implements android.os.Parcelable {
    private static final java.lang.String TAG = android.view.textservice.SpellCheckerInfo.class.getSimpleName();

    private final android.content.pm.ResolveInfo mService;

    private final java.lang.String mId;

    private final int mLabel;

    /**
     * The spell checker setting activity's name, used by the system settings to
     * launch the setting activity.
     */
    private final java.lang.String mSettingsActivityName;

    /**
     * The array of subtypes.
     */
    private final java.util.ArrayList<android.view.textservice.SpellCheckerSubtype> mSubtypes = new java.util.ArrayList<>();

    /**
     * Constructor.
     *
     * @unknown 
     */
    public SpellCheckerInfo(android.content.Context context, android.content.pm.ResolveInfo service) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mService = service;
        android.content.pm.ServiceInfo si = service.serviceInfo;
        mId = new android.content.ComponentName(si.packageName, si.name).flattenToShortString();
        final android.content.pm.PackageManager pm = context.getPackageManager();
        int label = 0;
        java.lang.String settingsActivityComponent = null;
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, android.view.textservice.SpellCheckerSession.SERVICE_META_DATA);
            if (parser == null) {
                throw new org.xmlpull.v1.XmlPullParserException(("No " + android.view.textservice.SpellCheckerSession.SERVICE_META_DATA) + " meta-data");
            }
            final android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            final java.lang.String nodeName = parser.getName();
            if (!"spell-checker".equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with spell-checker tag");
            }
            android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.SpellChecker);
            label = sa.getResourceId(com.android.internal.R.styleable.SpellChecker_label, 0);
            settingsActivityComponent = sa.getString(com.android.internal.R.styleable.SpellChecker_settingsActivity);
            sa.recycle();
            final int depth = parser.getDepth();
            // Parse all subtypes
            while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                if (type == org.xmlpull.v1.XmlPullParser.START_TAG) {
                    final java.lang.String subtypeNodeName = parser.getName();
                    if (!"subtype".equals(subtypeNodeName)) {
                        throw new org.xmlpull.v1.XmlPullParserException("Meta-data in spell-checker does not start with subtype tag");
                    }
                    final android.content.res.TypedArray a = res.obtainAttributes(attrs, com.android.internal.R.styleable.SpellChecker_Subtype);
                    android.view.textservice.SpellCheckerSubtype subtype = new android.view.textservice.SpellCheckerSubtype(a.getResourceId(com.android.internal.R.styleable.SpellChecker_Subtype_label, 0), a.getString(com.android.internal.R.styleable.SpellChecker_Subtype_subtypeLocale), a.getString(com.android.internal.R.styleable.SpellChecker_Subtype_languageTag), a.getString(com.android.internal.R.styleable.SpellChecker_Subtype_subtypeExtraValue), a.getInt(com.android.internal.R.styleable.SpellChecker_Subtype_subtypeId, 0));
                    mSubtypes.add(subtype);
                }
            } 
        } catch (java.lang.Exception e) {
            android.util.Slog.e(android.view.textservice.SpellCheckerInfo.TAG, "Caught exception: " + e);
            throw new org.xmlpull.v1.XmlPullParserException("Unable to create context for: " + si.packageName);
        } finally {
            if (parser != null)
                parser.close();

        }
        mLabel = label;
        mSettingsActivityName = settingsActivityComponent;
    }

    /**
     * Constructor.
     *
     * @unknown 
     */
    public SpellCheckerInfo(android.os.Parcel source) {
        mLabel = source.readInt();
        mId = source.readString();
        mSettingsActivityName = source.readString();
        mService = android.content.pm.ResolveInfo.CREATOR.createFromParcel(source);
        source.readTypedList(mSubtypes, this.CREATOR);
    }

    /**
     * Return a unique ID for this spell checker.  The ID is generated from
     * the package and class name implementing the method.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Return the component of the service that implements.
     */
    public android.content.ComponentName getComponent() {
        return new android.content.ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
    }

    /**
     * Return the .apk package that implements this.
     */
    public java.lang.String getPackageName() {
        return mService.serviceInfo.packageName;
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
        dest.writeInt(mLabel);
        dest.writeString(mId);
        dest.writeString(mSettingsActivityName);
        mService.writeToParcel(dest, flags);
        dest.writeTypedList(mSubtypes);
    }

    /**
     * Used to make this class parcelable.
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textservice.SpellCheckerInfo> CREATOR = new android.os.Parcelable.Creator<android.view.textservice.SpellCheckerInfo>() {
        @java.lang.Override
        public android.view.textservice.SpellCheckerInfo createFromParcel(android.os.Parcel source) {
            return new android.view.textservice.SpellCheckerInfo(source);
        }

        @java.lang.Override
        public android.view.textservice.SpellCheckerInfo[] newArray(int size) {
            return new android.view.textservice.SpellCheckerInfo[size];
        }
    };

    /**
     * Load the user-displayed label for this spell checker.
     *
     * @param pm
     * 		Supply a PackageManager used to load the spell checker's resources.
     */
    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        if ((mLabel == 0) || (pm == null))
            return "";

        return pm.getText(getPackageName(), mLabel, mService.serviceInfo.applicationInfo);
    }

    /**
     * Load the user-displayed icon for this spell checker.
     *
     * @param pm
     * 		Supply a PackageManager used to load the spell checker's resources.
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return mService.loadIcon(pm);
    }

    /**
     * Return the raw information about the Service implementing this
     * spell checker.  Do not modify the returned object.
     */
    public android.content.pm.ServiceInfo getServiceInfo() {
        return mService.serviceInfo;
    }

    /**
     * Return the class name of an activity that provides a settings UI.
     * You can launch this activity be starting it with
     * an {@link android.content.Intent} whose action is MAIN and with an
     * explicit {@link android.content.ComponentName}
     * composed of {@link #getPackageName} and the class name returned here.
     *
     * <p>A null will be returned if there is no settings activity.
     */
    public java.lang.String getSettingsActivity() {
        return mSettingsActivityName;
    }

    /**
     * Return the count of the subtypes.
     */
    public int getSubtypeCount() {
        return mSubtypes.size();
    }

    /**
     * Return the subtype at the specified index.
     *
     * @param index
     * 		the index of the subtype to return.
     */
    public android.view.textservice.SpellCheckerSubtype getSubtypeAt(int index) {
        return mSubtypes.get(index);
    }

    /**
     * Used to make this class parcelable.
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public void dump(final java.io.PrintWriter pw, final java.lang.String prefix) {
        pw.println((prefix + "mId=") + mId);
        pw.println((prefix + "mSettingsActivityName=") + mSettingsActivityName);
        pw.println(prefix + "Service:");
        mService.dump(new android.util.PrintWriterPrinter(pw), prefix + "  ");
        final int N = getSubtypeCount();
        for (int i = 0; i < N; i++) {
            final android.view.textservice.SpellCheckerSubtype st = getSubtypeAt(i);
            pw.println((((prefix + "  ") + "Subtype #") + i) + ":");
            pw.println(((((prefix + "    ") + "locale=") + st.getLocale()) + " languageTag=") + st.getLanguageTag());
            pw.println(((prefix + "    ") + "extraValue=") + st.getExtraValue());
        }
    }
}

