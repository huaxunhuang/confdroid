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
package android.content.pm;


/**
 * The {@link com.android.server.pm.PackageManagerService} maintains some
 * {@link IntentFilterVerificationInfo}s for each domain / package name.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class IntentFilterVerificationInfo implements android.os.Parcelable {
    private static final java.lang.String TAG = android.content.pm.IntentFilterVerificationInfo.class.getName();

    private static final java.lang.String TAG_DOMAIN = "domain";

    private static final java.lang.String ATTR_DOMAIN_NAME = "name";

    private static final java.lang.String ATTR_PACKAGE_NAME = "packageName";

    private static final java.lang.String ATTR_STATUS = "status";

    private android.util.ArraySet<java.lang.String> mDomains = new android.util.ArraySet();

    private java.lang.String mPackageName;

    private int mMainStatus;

    /**
     *
     *
     * @unknown 
     */
    public IntentFilterVerificationInfo() {
        mPackageName = null;
        mMainStatus = android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_UNDEFINED;
    }

    /**
     *
     *
     * @unknown 
     */
    public IntentFilterVerificationInfo(java.lang.String packageName, android.util.ArraySet<java.lang.String> domains) {
        mPackageName = packageName;
        mDomains = domains;
        mMainStatus = android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_UNDEFINED;
    }

    /**
     *
     *
     * @unknown 
     */
    public IntentFilterVerificationInfo(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        readFromXml(parser);
    }

    /**
     *
     *
     * @unknown 
     */
    public IntentFilterVerificationInfo(android.os.Parcel source) {
        readFromParcel(source);
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    public int getStatus() {
        return mMainStatus;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setStatus(int s) {
        if ((s >= android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_UNDEFINED) && (s <= android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_NEVER)) {
            mMainStatus = s;
        } else {
            android.util.Log.w(android.content.pm.IntentFilterVerificationInfo.TAG, "Trying to set a non supported status: " + s);
        }
    }

    public java.util.Set<java.lang.String> getDomains() {
        return mDomains;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDomains(android.util.ArraySet<java.lang.String> list) {
        mDomains = list;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getDomainsString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String str : mDomains) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    java.lang.String getStringFromXml(org.xmlpull.v1.XmlPullParser parser, java.lang.String attribute, java.lang.String defaultValue) {
        java.lang.String value = parser.getAttributeValue(null, attribute);
        if (value == null) {
            java.lang.String msg = (((("Missing element under " + android.content.pm.IntentFilterVerificationInfo.TAG) + ": ") + attribute) + " at ") + parser.getPositionDescription();
            android.util.Log.w(android.content.pm.IntentFilterVerificationInfo.TAG, msg);
            return defaultValue;
        } else {
            return value;
        }
    }

    int getIntFromXml(org.xmlpull.v1.XmlPullParser parser, java.lang.String attribute, int defaultValue) {
        java.lang.String value = parser.getAttributeValue(null, attribute);
        if (android.text.TextUtils.isEmpty(value)) {
            java.lang.String msg = (((("Missing element under " + android.content.pm.IntentFilterVerificationInfo.TAG) + ": ") + attribute) + " at ") + parser.getPositionDescription();
            android.util.Log.w(android.content.pm.IntentFilterVerificationInfo.TAG, msg);
            return defaultValue;
        } else {
            return java.lang.Integer.parseInt(value);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void readFromXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mPackageName = getStringFromXml(parser, android.content.pm.IntentFilterVerificationInfo.ATTR_PACKAGE_NAME, null);
        if (mPackageName == null) {
            android.util.Log.e(android.content.pm.IntentFilterVerificationInfo.TAG, "Package name cannot be null!");
        }
        int status = getIntFromXml(parser, android.content.pm.IntentFilterVerificationInfo.ATTR_STATUS, -1);
        if (status == (-1)) {
            android.util.Log.e(android.content.pm.IntentFilterVerificationInfo.TAG, "Unknown status value: " + status);
        }
        mMainStatus = status;
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals(android.content.pm.IntentFilterVerificationInfo.TAG_DOMAIN)) {
                java.lang.String name = getStringFromXml(parser, android.content.pm.IntentFilterVerificationInfo.ATTR_DOMAIN_NAME, null);
                if (!android.text.TextUtils.isEmpty(name)) {
                    mDomains.add(name);
                }
            } else {
                android.util.Log.w(android.content.pm.IntentFilterVerificationInfo.TAG, "Unknown tag parsing IntentFilter: " + tagName);
            }
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        } 
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToXml(org.xmlpull.v1.XmlSerializer serializer) throws java.io.IOException {
        serializer.attribute(null, android.content.pm.IntentFilterVerificationInfo.ATTR_PACKAGE_NAME, mPackageName);
        serializer.attribute(null, android.content.pm.IntentFilterVerificationInfo.ATTR_STATUS, java.lang.String.valueOf(mMainStatus));
        for (java.lang.String str : mDomains) {
            serializer.startTag(null, android.content.pm.IntentFilterVerificationInfo.TAG_DOMAIN);
            serializer.attribute(null, android.content.pm.IntentFilterVerificationInfo.ATTR_DOMAIN_NAME, str);
            serializer.endTag(null, android.content.pm.IntentFilterVerificationInfo.TAG_DOMAIN);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getStatusString() {
        return android.content.pm.IntentFilterVerificationInfo.getStatusStringFromValue(((long) (mMainStatus)) << 32);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getStatusStringFromValue(long val) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        switch (((int) (val >> 32))) {
            case android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_ALWAYS :
                sb.append("always : ");
                sb.append(java.lang.Long.toHexString(val & 0xffffffff));
                break;
            case android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_ASK :
                sb.append("ask");
                break;
            case android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_NEVER :
                sb.append("never");
                break;
            case android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_ALWAYS_ASK :
                sb.append("always-ask");
                break;
            case android.content.pm.PackageManager.INTENT_FILTER_DOMAIN_VERIFICATION_STATUS_UNDEFINED :
            default :
                sb.append("undefined");
                break;
        }
        return sb.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(android.os.Parcel source) {
        mPackageName = source.readString();
        mMainStatus = source.readInt();
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        source.readStringList(list);
        mDomains.addAll(list);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mPackageName);
        dest.writeInt(mMainStatus);
        dest.writeStringList(new java.util.ArrayList(mDomains));
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.IntentFilterVerificationInfo> CREATOR = new android.content.pm.Creator<android.content.pm.IntentFilterVerificationInfo>() {
        public android.content.pm.IntentFilterVerificationInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.IntentFilterVerificationInfo(source);
        }

        public android.content.pm.IntentFilterVerificationInfo[] newArray(int size) {
            return new android.content.pm.IntentFilterVerificationInfo[size];
        }
    };
}

