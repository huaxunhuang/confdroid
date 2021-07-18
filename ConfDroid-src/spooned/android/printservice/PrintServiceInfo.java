/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.printservice;


/**
 * This class describes a {@link PrintService}. A print service knows
 * how to communicate with one or more printers over one or more protocols
 * and exposes printers for use by the applications via the platform print
 * APIs.
 *
 * @see PrintService
 * @see android.print.PrintManager
 * @unknown 
 */
public final class PrintServiceInfo implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = android.printservice.PrintServiceInfo.class.getSimpleName();

    private static final java.lang.String TAG_PRINT_SERVICE = "print-service";

    private final java.lang.String mId;

    private boolean mIsEnabled;

    private final android.content.pm.ResolveInfo mResolveInfo;

    private final java.lang.String mSettingsActivityName;

    private final java.lang.String mAddPrintersActivityName;

    private final java.lang.String mAdvancedPrintOptionsActivityName;

    /**
     * Creates a new instance.
     *
     * @unknown 
     */
    public PrintServiceInfo(android.os.Parcel parcel) {
        mId = parcel.readString();
        mIsEnabled = parcel.readByte() != 0;
        mResolveInfo = parcel.readParcelable(null);
        mSettingsActivityName = parcel.readString();
        mAddPrintersActivityName = parcel.readString();
        mAdvancedPrintOptionsActivityName = parcel.readString();
    }

    /**
     * Creates a new instance.
     *
     * @param resolveInfo
     * 		The service resolve info.
     * @param settingsActivityName
     * 		Optional settings activity name.
     * @param addPrintersActivityName
     * 		Optional add printers activity name.
     * @param advancedPrintOptionsActivityName
     * 		Optional advanced print options activity.
     */
    public PrintServiceInfo(android.content.pm.ResolveInfo resolveInfo, java.lang.String settingsActivityName, java.lang.String addPrintersActivityName, java.lang.String advancedPrintOptionsActivityName) {
        mId = new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name).flattenToString();
        mResolveInfo = resolveInfo;
        mSettingsActivityName = settingsActivityName;
        mAddPrintersActivityName = addPrintersActivityName;
        mAdvancedPrintOptionsActivityName = advancedPrintOptionsActivityName;
    }

    /**
     * Return the component name for this print service.
     *
     * @return The component name for this print service.
     */
    @android.annotation.NonNull
    public android.content.ComponentName getComponentName() {
        return new android.content.ComponentName(mResolveInfo.serviceInfo.packageName, mResolveInfo.serviceInfo.name);
    }

    /**
     * Creates a new instance.
     *
     * @param resolveInfo
     * 		The service resolve info.
     * @param context
     * 		Context for accessing resources.
     * @return The created instance.
     */
    public static android.printservice.PrintServiceInfo create(android.content.pm.ResolveInfo resolveInfo, android.content.Context context) {
        java.lang.String settingsActivityName = null;
        java.lang.String addPrintersActivityName = null;
        java.lang.String advancedPrintOptionsActivityName = null;
        android.content.res.XmlResourceParser parser = null;
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        parser = resolveInfo.serviceInfo.loadXmlMetaData(packageManager, android.printservice.PrintService.SERVICE_META_DATA);
        if (parser != null) {
            try {
                int type = 0;
                while ((type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
                    type = parser.next();
                } 
                java.lang.String nodeName = parser.getName();
                if (!android.printservice.PrintServiceInfo.TAG_PRINT_SERVICE.equals(nodeName)) {
                    android.util.Log.e(android.printservice.PrintServiceInfo.LOG_TAG, ("Ignoring meta-data that does not start with " + android.printservice.PrintServiceInfo.TAG_PRINT_SERVICE) + " tag");
                } else {
                    android.content.res.Resources resources = packageManager.getResourcesForApplication(resolveInfo.serviceInfo.applicationInfo);
                    android.util.AttributeSet allAttributes = android.util.Xml.asAttributeSet(parser);
                    android.content.res.TypedArray attributes = resources.obtainAttributes(allAttributes, com.android.internal.R.styleable.PrintService);
                    settingsActivityName = attributes.getString(com.android.internal.R.styleable.PrintService_settingsActivity);
                    addPrintersActivityName = attributes.getString(com.android.internal.R.styleable.PrintService_addPrintersActivity);
                    advancedPrintOptionsActivityName = attributes.getString(com.android.internal.R.styleable.PrintService_advancedPrintOptionsActivity);
                    attributes.recycle();
                }
            } catch (java.io.IOException ioe) {
                android.util.Log.w(android.printservice.PrintServiceInfo.LOG_TAG, "Error reading meta-data:" + ioe);
            } catch (org.xmlpull.v1.XmlPullParserException xppe) {
                android.util.Log.w(android.printservice.PrintServiceInfo.LOG_TAG, "Error reading meta-data:" + xppe);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(android.printservice.PrintServiceInfo.LOG_TAG, "Unable to load resources for: " + resolveInfo.serviceInfo.packageName);
            } finally {
                if (parser != null) {
                    parser.close();
                }
            }
        }
        return new android.printservice.PrintServiceInfo(resolveInfo, settingsActivityName, addPrintersActivityName, advancedPrintOptionsActivityName);
    }

    /**
     * The accessibility service id.
     * <p>
     * <strong>Generated by the system.</strong>
     * </p>
     *
     * @return The id.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * If the service was enabled when it was read from the system.
     *
     * @return The id.
     */
    public boolean isEnabled() {
        return mIsEnabled;
    }

    /**
     * Mark a service as enabled or not
     *
     * @param isEnabled
     * 		If the service should be marked as enabled.
     */
    public void setIsEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }

    /**
     * The service {@link ResolveInfo}.
     *
     * @return The info.
     */
    public android.content.pm.ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    /**
     * The settings activity name.
     * <p>
     * <strong>Statically set from
     * {@link PrintService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return The settings activity name.
     */
    public java.lang.String getSettingsActivityName() {
        return mSettingsActivityName;
    }

    /**
     * The add printers activity name.
     * <p>
     * <strong>Statically set from
     * {@link PrintService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return The add printers activity name.
     */
    public java.lang.String getAddPrintersActivityName() {
        return mAddPrintersActivityName;
    }

    /**
     * The advanced print options activity name.
     * <p>
     * <strong>Statically set from
     * {@link PrintService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return The advanced print options activity name.
     */
    public java.lang.String getAdvancedOptionsActivityName() {
        return mAdvancedPrintOptionsActivityName;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flagz) {
        parcel.writeString(mId);
        parcel.writeByte(((byte) (mIsEnabled ? 1 : 0)));
        parcel.writeParcelable(mResolveInfo, 0);
        parcel.writeString(mSettingsActivityName);
        parcel.writeString(mAddPrintersActivityName);
        parcel.writeString(mAdvancedPrintOptionsActivityName);
    }

    @java.lang.Override
    public int hashCode() {
        return 31 + (mId == null ? 0 : mId.hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.printservice.PrintServiceInfo other = ((android.printservice.PrintServiceInfo) (obj));
        if (mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else
            if (!mId.equals(other.mId)) {
                return false;
            }

        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PrintServiceInfo{");
        builder.append("id=").append(mId);
        builder.append("isEnabled=").append(mIsEnabled);
        builder.append(", resolveInfo=").append(mResolveInfo);
        builder.append(", settingsActivityName=").append(mSettingsActivityName);
        builder.append(", addPrintersActivityName=").append(mAddPrintersActivityName);
        builder.append(", advancedPrintOptionsActivityName=").append(mAdvancedPrintOptionsActivityName);
        builder.append("}");
        return builder.toString();
    }

    public static final android.os.Parcelable.Creator<android.printservice.PrintServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.printservice.PrintServiceInfo>() {
        @java.lang.Override
        public android.printservice.PrintServiceInfo createFromParcel(android.os.Parcel parcel) {
            return new android.printservice.PrintServiceInfo(parcel);
        }

        @java.lang.Override
        public android.printservice.PrintServiceInfo[] newArray(int size) {
            return new android.printservice.PrintServiceInfo[size];
        }
    };
}

