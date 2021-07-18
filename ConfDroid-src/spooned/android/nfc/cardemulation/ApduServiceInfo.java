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
package android.nfc.cardemulation;


/**
 *
 *
 * @unknown 
 */
public final class ApduServiceInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "ApduServiceInfo";

    /**
     * The service that implements this
     */
    final android.content.pm.ResolveInfo mService;

    /**
     * Description of the service
     */
    final java.lang.String mDescription;

    /**
     * Whether this service represents AIDs running on the host CPU
     */
    final boolean mOnHost;

    /**
     * Mapping from category to static AID group
     */
    final java.util.HashMap<java.lang.String, android.nfc.cardemulation.AidGroup> mStaticAidGroups;

    /**
     * Mapping from category to dynamic AID group
     */
    final java.util.HashMap<java.lang.String, android.nfc.cardemulation.AidGroup> mDynamicAidGroups;

    /**
     * Whether this service should only be started when the device is unlocked.
     */
    final boolean mRequiresDeviceUnlock;

    /**
     * The id of the service banner specified in XML.
     */
    final int mBannerResourceId;

    /**
     * The uid of the package the service belongs to
     */
    final int mUid;

    /**
     * Settings Activity for this service
     */
    final java.lang.String mSettingsActivityName;

    /**
     *
     *
     * @unknown 
     */
    public ApduServiceInfo(android.content.pm.ResolveInfo info, boolean onHost, java.lang.String description, java.util.ArrayList<android.nfc.cardemulation.AidGroup> staticAidGroups, java.util.ArrayList<android.nfc.cardemulation.AidGroup> dynamicAidGroups, boolean requiresUnlock, int bannerResource, int uid, java.lang.String settingsActivityName) {
        this.mService = info;
        this.mDescription = description;
        this.mStaticAidGroups = new java.util.HashMap<java.lang.String, android.nfc.cardemulation.AidGroup>();
        this.mDynamicAidGroups = new java.util.HashMap<java.lang.String, android.nfc.cardemulation.AidGroup>();
        this.mOnHost = onHost;
        this.mRequiresDeviceUnlock = requiresUnlock;
        for (android.nfc.cardemulation.AidGroup aidGroup : staticAidGroups) {
            this.mStaticAidGroups.put(aidGroup.category, aidGroup);
        }
        for (android.nfc.cardemulation.AidGroup aidGroup : dynamicAidGroups) {
            this.mDynamicAidGroups.put(aidGroup.category, aidGroup);
        }
        this.mBannerResourceId = bannerResource;
        this.mUid = uid;
        this.mSettingsActivityName = settingsActivityName;
    }

    public ApduServiceInfo(android.content.pm.PackageManager pm, android.content.pm.ResolveInfo info, boolean onHost) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.ServiceInfo si = info.serviceInfo;
        android.content.res.XmlResourceParser parser = null;
        try {
            if (onHost) {
                parser = si.loadXmlMetaData(pm, android.nfc.cardemulation.HostApduService.SERVICE_META_DATA);
                if (parser == null) {
                    throw new org.xmlpull.v1.XmlPullParserException(("No " + android.nfc.cardemulation.HostApduService.SERVICE_META_DATA) + " meta-data");
                }
            } else {
                parser = si.loadXmlMetaData(pm, android.nfc.cardemulation.OffHostApduService.SERVICE_META_DATA);
                if (parser == null) {
                    throw new org.xmlpull.v1.XmlPullParserException(("No " + android.nfc.cardemulation.OffHostApduService.SERVICE_META_DATA) + " meta-data");
                }
            }
            int eventType = parser.getEventType();
            while ((eventType != org.xmlpull.v1.XmlPullParser.START_TAG) && (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                eventType = parser.next();
            } 
            java.lang.String tagName = parser.getName();
            if (onHost && (!"host-apdu-service".equals(tagName))) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with <host-apdu-service> tag");
            } else
                if ((!onHost) && (!"offhost-apdu-service".equals(tagName))) {
                    throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with <offhost-apdu-service> tag");
                }

            android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            if (onHost) {
                android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.HostApduService);
                mService = info;
                mDescription = sa.getString(com.android.internal.R.styleable.HostApduService_description);
                mRequiresDeviceUnlock = sa.getBoolean(com.android.internal.R.styleable.HostApduService_requireDeviceUnlock, false);
                mBannerResourceId = sa.getResourceId(com.android.internal.R.styleable.HostApduService_apduServiceBanner, -1);
                mSettingsActivityName = sa.getString(com.android.internal.R.styleable.HostApduService_settingsActivity);
                sa.recycle();
            } else {
                android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.OffHostApduService);
                mService = info;
                mDescription = sa.getString(com.android.internal.R.styleable.OffHostApduService_description);
                mRequiresDeviceUnlock = false;
                mBannerResourceId = sa.getResourceId(com.android.internal.R.styleable.OffHostApduService_apduServiceBanner, -1);
                mSettingsActivityName = sa.getString(com.android.internal.R.styleable.HostApduService_settingsActivity);
                sa.recycle();
            }
            mStaticAidGroups = new java.util.HashMap<java.lang.String, android.nfc.cardemulation.AidGroup>();
            mDynamicAidGroups = new java.util.HashMap<java.lang.String, android.nfc.cardemulation.AidGroup>();
            mOnHost = onHost;
            final int depth = parser.getDepth();
            android.nfc.cardemulation.AidGroup currentGroup = null;
            // Parsed values for the current AID group
            while ((((eventType = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                tagName = parser.getName();
                if (((eventType == org.xmlpull.v1.XmlPullParser.START_TAG) && "aid-group".equals(tagName)) && (currentGroup == null)) {
                    final android.content.res.TypedArray groupAttrs = res.obtainAttributes(attrs, com.android.internal.R.styleable.AidGroup);
                    // Get category of AID group
                    java.lang.String groupCategory = groupAttrs.getString(com.android.internal.R.styleable.AidGroup_category);
                    java.lang.String groupDescription = groupAttrs.getString(com.android.internal.R.styleable.AidGroup_description);
                    if (!android.nfc.cardemulation.CardEmulation.CATEGORY_PAYMENT.equals(groupCategory)) {
                        groupCategory = android.nfc.cardemulation.CardEmulation.CATEGORY_OTHER;
                    }
                    currentGroup = mStaticAidGroups.get(groupCategory);
                    if (currentGroup != null) {
                        if (!android.nfc.cardemulation.CardEmulation.CATEGORY_OTHER.equals(groupCategory)) {
                            android.util.Log.e(android.nfc.cardemulation.ApduServiceInfo.TAG, ("Not allowing multiple aid-groups in the " + groupCategory) + " category");
                            currentGroup = null;
                        }
                    } else {
                        currentGroup = new android.nfc.cardemulation.AidGroup(groupCategory, groupDescription);
                    }
                    groupAttrs.recycle();
                } else
                    if (((eventType == org.xmlpull.v1.XmlPullParser.END_TAG) && "aid-group".equals(tagName)) && (currentGroup != null)) {
                        if (currentGroup.aids.size() > 0) {
                            if (!mStaticAidGroups.containsKey(currentGroup.category)) {
                                mStaticAidGroups.put(currentGroup.category, currentGroup);
                            }
                        } else {
                            android.util.Log.e(android.nfc.cardemulation.ApduServiceInfo.TAG, "Not adding <aid-group> with empty or invalid AIDs");
                        }
                        currentGroup = null;
                    } else
                        if (((eventType == org.xmlpull.v1.XmlPullParser.START_TAG) && "aid-filter".equals(tagName)) && (currentGroup != null)) {
                            final android.content.res.TypedArray a = res.obtainAttributes(attrs, com.android.internal.R.styleable.AidFilter);
                            java.lang.String aid = a.getString(com.android.internal.R.styleable.AidFilter_name).toUpperCase();
                            if (android.nfc.cardemulation.CardEmulation.isValidAid(aid) && (!currentGroup.aids.contains(aid))) {
                                currentGroup.aids.add(aid);
                            } else {
                                android.util.Log.e(android.nfc.cardemulation.ApduServiceInfo.TAG, "Ignoring invalid or duplicate aid: " + aid);
                            }
                            a.recycle();
                        } else
                            if (((eventType == org.xmlpull.v1.XmlPullParser.START_TAG) && "aid-prefix-filter".equals(tagName)) && (currentGroup != null)) {
                                final android.content.res.TypedArray a = res.obtainAttributes(attrs, com.android.internal.R.styleable.AidFilter);
                                java.lang.String aid = a.getString(com.android.internal.R.styleable.AidFilter_name).toUpperCase();
                                // Add wildcard char to indicate prefix
                                aid = aid.concat("*");
                                if (android.nfc.cardemulation.CardEmulation.isValidAid(aid) && (!currentGroup.aids.contains(aid))) {
                                    currentGroup.aids.add(aid);
                                } else {
                                    android.util.Log.e(android.nfc.cardemulation.ApduServiceInfo.TAG, "Ignoring invalid or duplicate aid: " + aid);
                                }
                                a.recycle();
                            }



            } 
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

    /**
     * Returns a consolidated list of AIDs from the AID groups
     * registered by this service. Note that if a service has both
     * a static (manifest-based) AID group for a category and a dynamic
     * AID group, only the dynamically registered AIDs will be returned
     * for that category.
     *
     * @return List of AIDs registered by the service
     */
    public java.util.List<java.lang.String> getAids() {
        final java.util.ArrayList<java.lang.String> aids = new java.util.ArrayList<java.lang.String>();
        for (android.nfc.cardemulation.AidGroup group : getAidGroups()) {
            aids.addAll(group.aids);
        }
        return aids;
    }

    public java.util.List<java.lang.String> getPrefixAids() {
        final java.util.ArrayList<java.lang.String> prefixAids = new java.util.ArrayList<java.lang.String>();
        for (android.nfc.cardemulation.AidGroup group : getAidGroups()) {
            for (java.lang.String aid : group.aids) {
                if (aid.endsWith("*")) {
                    prefixAids.add(aid);
                }
            }
        }
        return prefixAids;
    }

    /**
     * Returns the registered AID group for this category.
     */
    public android.nfc.cardemulation.AidGroup getDynamicAidGroupForCategory(java.lang.String category) {
        return mDynamicAidGroups.get(category);
    }

    public boolean removeDynamicAidGroupForCategory(java.lang.String category) {
        return mDynamicAidGroups.remove(category) != null;
    }

    /**
     * Returns a consolidated list of AID groups
     * registered by this service. Note that if a service has both
     * a static (manifest-based) AID group for a category and a dynamic
     * AID group, only the dynamically registered AID group will be returned
     * for that category.
     *
     * @return List of AIDs registered by the service
     */
    public java.util.ArrayList<android.nfc.cardemulation.AidGroup> getAidGroups() {
        final java.util.ArrayList<android.nfc.cardemulation.AidGroup> groups = new java.util.ArrayList<android.nfc.cardemulation.AidGroup>();
        for (java.util.Map.Entry<java.lang.String, android.nfc.cardemulation.AidGroup> entry : mDynamicAidGroups.entrySet()) {
            groups.add(entry.getValue());
        }
        for (java.util.Map.Entry<java.lang.String, android.nfc.cardemulation.AidGroup> entry : mStaticAidGroups.entrySet()) {
            if (!mDynamicAidGroups.containsKey(entry.getKey())) {
                // Consolidate AID groups - don't return static ones
                // if a dynamic group exists for the category.
                groups.add(entry.getValue());
            }
        }
        return groups;
    }

    /**
     * Returns the category to which this service has attributed the AID that is passed in,
     * or null if we don't know this AID.
     */
    public java.lang.String getCategoryForAid(java.lang.String aid) {
        java.util.ArrayList<android.nfc.cardemulation.AidGroup> groups = getAidGroups();
        for (android.nfc.cardemulation.AidGroup group : groups) {
            if (group.aids.contains(aid.toUpperCase())) {
                return group.category;
            }
        }
        return null;
    }

    public boolean hasCategory(java.lang.String category) {
        return mStaticAidGroups.containsKey(category) || mDynamicAidGroups.containsKey(category);
    }

    public boolean isOnHost() {
        return mOnHost;
    }

    public boolean requiresUnlock() {
        return mRequiresDeviceUnlock;
    }

    public java.lang.String getDescription() {
        return mDescription;
    }

    public int getUid() {
        return mUid;
    }

    public void setOrReplaceDynamicAidGroup(android.nfc.cardemulation.AidGroup aidGroup) {
        mDynamicAidGroups.put(aidGroup.getCategory(), aidGroup);
    }

    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        return mService.loadLabel(pm);
    }

    public java.lang.CharSequence loadAppLabel(android.content.pm.PackageManager pm) {
        try {
            return pm.getApplicationLabel(pm.getApplicationInfo(mService.resolvePackageName, android.content.pm.PackageManager.GET_META_DATA));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return mService.loadIcon(pm);
    }

    public android.graphics.drawable.Drawable loadBanner(android.content.pm.PackageManager pm) {
        android.content.res.Resources res;
        try {
            res = pm.getResourcesForApplication(mService.serviceInfo.packageName);
            android.graphics.drawable.Drawable banner = res.getDrawable(mBannerResourceId);
            return banner;
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.e(android.nfc.cardemulation.ApduServiceInfo.TAG, "Could not load banner.");
            return null;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(android.nfc.cardemulation.ApduServiceInfo.TAG, "Could not load banner.");
            return null;
        }
    }

    public java.lang.String getSettingsActivityName() {
        return mSettingsActivityName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder out = new java.lang.StringBuilder("ApduService: ");
        out.append(getComponent());
        out.append(", description: " + mDescription);
        out.append(", Static AID Groups: ");
        for (android.nfc.cardemulation.AidGroup aidGroup : mStaticAidGroups.values()) {
            out.append(aidGroup.toString());
        }
        out.append(", Dynamic AID Groups: ");
        for (android.nfc.cardemulation.AidGroup aidGroup : mDynamicAidGroups.values()) {
            out.append(aidGroup.toString());
        }
        return out.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof android.nfc.cardemulation.ApduServiceInfo))
            return false;

        android.nfc.cardemulation.ApduServiceInfo thatService = ((android.nfc.cardemulation.ApduServiceInfo) (o));
        return thatService.getComponent().equals(this.getComponent());
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
        dest.writeInt(mOnHost ? 1 : 0);
        dest.writeInt(mStaticAidGroups.size());
        if (mStaticAidGroups.size() > 0) {
            dest.writeTypedList(new java.util.ArrayList<android.nfc.cardemulation.AidGroup>(mStaticAidGroups.values()));
        }
        dest.writeInt(mDynamicAidGroups.size());
        if (mDynamicAidGroups.size() > 0) {
            dest.writeTypedList(new java.util.ArrayList<android.nfc.cardemulation.AidGroup>(mDynamicAidGroups.values()));
        }
        dest.writeInt(mRequiresDeviceUnlock ? 1 : 0);
        dest.writeInt(mBannerResourceId);
        dest.writeInt(mUid);
        dest.writeString(mSettingsActivityName);
    }

    public static final android.os.Parcelable.Creator<android.nfc.cardemulation.ApduServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.nfc.cardemulation.ApduServiceInfo>() {
        @java.lang.Override
        public android.nfc.cardemulation.ApduServiceInfo createFromParcel(android.os.Parcel source) {
            android.content.pm.ResolveInfo info = android.content.pm.ResolveInfo.CREATOR.createFromParcel(source);
            java.lang.String description = source.readString();
            boolean onHost = source.readInt() != 0;
            java.util.ArrayList<android.nfc.cardemulation.AidGroup> staticAidGroups = new java.util.ArrayList<android.nfc.cardemulation.AidGroup>();
            int numStaticGroups = source.readInt();
            if (numStaticGroups > 0) {
                source.readTypedList(staticAidGroups, android.nfc.cardemulation.AidGroup.CREATOR);
            }
            java.util.ArrayList<android.nfc.cardemulation.AidGroup> dynamicAidGroups = new java.util.ArrayList<android.nfc.cardemulation.AidGroup>();
            int numDynamicGroups = source.readInt();
            if (numDynamicGroups > 0) {
                source.readTypedList(dynamicAidGroups, android.nfc.cardemulation.AidGroup.CREATOR);
            }
            boolean requiresUnlock = source.readInt() != 0;
            int bannerResource = source.readInt();
            int uid = source.readInt();
            java.lang.String settingsActivityName = source.readString();
            return new android.nfc.cardemulation.ApduServiceInfo(info, onHost, description, staticAidGroups, dynamicAidGroups, requiresUnlock, bannerResource, uid, settingsActivityName);
        }

        @java.lang.Override
        public android.nfc.cardemulation.ApduServiceInfo[] newArray(int size) {
            return new android.nfc.cardemulation.ApduServiceInfo[size];
        }
    };

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.println(((("    " + getComponent()) + " (Description: ") + getDescription()) + ")");
        pw.println("    Static AID groups:");
        for (android.nfc.cardemulation.AidGroup group : mStaticAidGroups.values()) {
            pw.println("        Category: " + group.category);
            for (java.lang.String aid : group.aids) {
                pw.println("            AID: " + aid);
            }
        }
        pw.println("    Dynamic AID groups:");
        for (android.nfc.cardemulation.AidGroup group : mDynamicAidGroups.values()) {
            pw.println("        Category: " + group.category);
            for (java.lang.String aid : group.aids) {
                pw.println("            AID: " + aid);
            }
        }
        pw.println("    Settings Activity: " + mSettingsActivityName);
    }
}

