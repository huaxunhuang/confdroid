/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.nfc.cardemulation;


/**
 *
 *
 * @unknown 
 */
public final class NfcFServiceInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "NfcFServiceInfo";

    /**
     * The service that implements this
     */
    final android.content.pm.ResolveInfo mService;

    /**
     * Description of the service
     */
    final java.lang.String mDescription;

    /**
     * System Code of the service
     */
    final java.lang.String mSystemCode;

    /**
     * System Code of the service registered by API
     */
    java.lang.String mDynamicSystemCode;

    /**
     * NFCID2 of the service
     */
    final java.lang.String mNfcid2;

    /**
     * NFCID2 of the service registered by API
     */
    java.lang.String mDynamicNfcid2;

    /**
     * The uid of the package the service belongs to
     */
    final int mUid;

    /**
     *
     *
     * @unknown 
     */
    public NfcFServiceInfo(android.content.pm.ResolveInfo info, java.lang.String description, java.lang.String systemCode, java.lang.String dynamicSystemCode, java.lang.String nfcid2, java.lang.String dynamicNfcid2, int uid) {
        this.mService = info;
        this.mDescription = description;
        this.mSystemCode = systemCode;
        this.mDynamicSystemCode = dynamicSystemCode;
        this.mNfcid2 = nfcid2;
        this.mDynamicNfcid2 = dynamicNfcid2;
        this.mUid = uid;
    }

    public NfcFServiceInfo(android.content.pm.PackageManager pm, android.content.pm.ResolveInfo info) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.ServiceInfo si = info.serviceInfo;
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, android.nfc.cardemulation.HostNfcFService.SERVICE_META_DATA);
            if (parser == null) {
                throw new org.xmlpull.v1.XmlPullParserException(("No " + android.nfc.cardemulation.HostNfcFService.SERVICE_META_DATA) + " meta-data");
            }
            int eventType = parser.getEventType();
            while ((eventType != org.xmlpull.v1.XmlPullParser.START_TAG) && (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                eventType = parser.next();
            } 
            java.lang.String tagName = parser.getName();
            if (!"host-nfcf-service".equals(tagName)) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with <host-nfcf-service> tag");
            }
            android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.HostNfcFService);
            mService = info;
            mDescription = sa.getString(com.android.internal.R.styleable.HostNfcFService_description);
            mDynamicSystemCode = null;
            mDynamicNfcid2 = null;
            sa.recycle();
            java.lang.String systemCode = null;
            java.lang.String nfcid2 = null;
            final int depth = parser.getDepth();
            while ((((eventType = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                tagName = parser.getName();
                if (((eventType == org.xmlpull.v1.XmlPullParser.START_TAG) && "system-code-filter".equals(tagName)) && (systemCode == null)) {
                    final android.content.res.TypedArray a = res.obtainAttributes(attrs, com.android.internal.R.styleable.SystemCodeFilter);
                    systemCode = a.getString(com.android.internal.R.styleable.SystemCodeFilter_name).toUpperCase();
                    if ((!android.nfc.cardemulation.NfcFCardEmulation.isValidSystemCode(systemCode)) && (!systemCode.equalsIgnoreCase("NULL"))) {
                        android.util.Log.e(android.nfc.cardemulation.NfcFServiceInfo.TAG, "Invalid System Code: " + systemCode);
                        systemCode = null;
                    }
                    a.recycle();
                } else
                    if (((eventType == org.xmlpull.v1.XmlPullParser.START_TAG) && "nfcid2-filter".equals(tagName)) && (nfcid2 == null)) {
                        final android.content.res.TypedArray a = res.obtainAttributes(attrs, com.android.internal.R.styleable.Nfcid2Filter);
                        nfcid2 = a.getString(com.android.internal.R.styleable.Nfcid2Filter_name).toUpperCase();
                        if (((!nfcid2.equalsIgnoreCase("RANDOM")) && (!nfcid2.equalsIgnoreCase("NULL"))) && (!android.nfc.cardemulation.NfcFCardEmulation.isValidNfcid2(nfcid2))) {
                            android.util.Log.e(android.nfc.cardemulation.NfcFServiceInfo.TAG, "Invalid NFCID2: " + nfcid2);
                            nfcid2 = null;
                        }
                        a.recycle();
                    }

            } 
            mSystemCode = (systemCode == null) ? "NULL" : systemCode;
            mNfcid2 = (nfcid2 == null) ? "NULL" : nfcid2;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new org.xmlpull.v1.XmlPullParserException("Unable to create context for: " + si.packageName);
        } finally {
            if (parser != null)
                parser.close();

        }
        // Set uid
        mUid = si.applicationInfo.uid;
    }

    public android.content.ComponentName getComponent() {
        return new android.content.ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
    }

    public java.lang.String getSystemCode() {
        return mDynamicSystemCode == null ? mSystemCode : mDynamicSystemCode;
    }

    public void setOrReplaceDynamicSystemCode(java.lang.String systemCode) {
        mDynamicSystemCode = systemCode;
    }

    public java.lang.String getNfcid2() {
        return mDynamicNfcid2 == null ? mNfcid2 : mDynamicNfcid2;
    }

    public void setOrReplaceDynamicNfcid2(java.lang.String nfcid2) {
        mDynamicNfcid2 = nfcid2;
    }

    public java.lang.String getDescription() {
        return mDescription;
    }

    public int getUid() {
        return mUid;
    }

    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        return mService.loadLabel(pm);
    }

    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return mService.loadIcon(pm);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder out = new java.lang.StringBuilder("NfcFService: ");
        out.append(getComponent());
        out.append(", description: " + mDescription);
        out.append(", System Code: " + mSystemCode);
        if (mDynamicSystemCode != null) {
            out.append(", dynamic System Code: " + mDynamicSystemCode);
        }
        out.append(", NFCID2: " + mNfcid2);
        if (mDynamicNfcid2 != null) {
            out.append(", dynamic NFCID2: " + mDynamicNfcid2);
        }
        return out.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof android.nfc.cardemulation.NfcFServiceInfo))
            return false;

        android.nfc.cardemulation.NfcFServiceInfo thatService = ((android.nfc.cardemulation.NfcFServiceInfo) (o));
        if (!thatService.getComponent().equals(this.getComponent()))
            return false;

        if (!thatService.mSystemCode.equalsIgnoreCase(this.mSystemCode))
            return false;

        if (!thatService.mNfcid2.equalsIgnoreCase(this.mNfcid2))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return getComponent().hashCode();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        mService.writeToParcel(dest, flags);
        dest.writeString(mDescription);
        dest.writeString(mSystemCode);
        dest.writeInt(mDynamicSystemCode != null ? 1 : 0);
        if (mDynamicSystemCode != null) {
            dest.writeString(mDynamicSystemCode);
        }
        dest.writeString(mNfcid2);
        dest.writeInt(mDynamicNfcid2 != null ? 1 : 0);
        if (mDynamicNfcid2 != null) {
            dest.writeString(mDynamicNfcid2);
        }
        dest.writeInt(mUid);
    }

    public static final android.os.Parcelable.Creator<android.nfc.cardemulation.NfcFServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.nfc.cardemulation.NfcFServiceInfo>() {
        @java.lang.Override
        public android.nfc.cardemulation.NfcFServiceInfo createFromParcel(android.os.Parcel source) {
            android.content.pm.ResolveInfo info = android.content.pm.ResolveInfo.CREATOR.createFromParcel(source);
            java.lang.String description = source.readString();
            java.lang.String systemCode = source.readString();
            java.lang.String dynamicSystemCode = null;
            if (source.readInt() != 0) {
                dynamicSystemCode = source.readString();
            }
            java.lang.String nfcid2 = source.readString();
            java.lang.String dynamicNfcid2 = null;
            if (source.readInt() != 0) {
                dynamicNfcid2 = source.readString();
            }
            int uid = source.readInt();
            android.nfc.cardemulation.NfcFServiceInfo service = new android.nfc.cardemulation.NfcFServiceInfo(info, description, systemCode, dynamicSystemCode, nfcid2, dynamicNfcid2, uid);
            return service;
        }

        @java.lang.Override
        public android.nfc.cardemulation.NfcFServiceInfo[] newArray(int size) {
            return new android.nfc.cardemulation.NfcFServiceInfo[size];
        }
    };

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.println(((("    " + getComponent()) + " (Description: ") + getDescription()) + ")");
        pw.println("    System Code: " + getSystemCode());
        pw.println("    NFCID2: " + getNfcid2());
    }
}

