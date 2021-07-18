/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.content;


/**
 * A cache of services that export the {@link android.content.ISyncAdapter} interface.
 *
 * @unknown 
 */
public class SyncAdaptersCache extends android.content.pm.RegisteredServicesCache<android.content.SyncAdapterType> {
    private static final java.lang.String TAG = "Account";

    private static final java.lang.String SERVICE_INTERFACE = "android.content.SyncAdapter";

    private static final java.lang.String SERVICE_META_DATA = "android.content.SyncAdapter";

    private static final java.lang.String ATTRIBUTES_NAME = "sync-adapter";

    private static final android.content.SyncAdaptersCache.MySerializer sSerializer = new android.content.SyncAdaptersCache.MySerializer();

    @com.android.internal.annotations.GuardedBy("mServicesLock")
    private android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.lang.String[]>> mAuthorityToSyncAdapters = new android.util.SparseArray();

    @android.annotation.UnsupportedAppUsage
    public SyncAdaptersCache(android.content.Context context) {
        super(context, android.content.SyncAdaptersCache.SERVICE_INTERFACE, android.content.SyncAdaptersCache.SERVICE_META_DATA, android.content.SyncAdaptersCache.ATTRIBUTES_NAME, android.content.SyncAdaptersCache.sSerializer);
    }

    public android.content.SyncAdapterType parseServiceAttributes(android.content.res.Resources res, java.lang.String packageName, android.util.AttributeSet attrs) {
        android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.SyncAdapter);
        try {
            final java.lang.String authority = sa.getString(com.android.internal.R.styleable.SyncAdapter_contentAuthority);
            final java.lang.String accountType = sa.getString(com.android.internal.R.styleable.SyncAdapter_accountType);
            if (android.text.TextUtils.isEmpty(authority) || android.text.TextUtils.isEmpty(accountType)) {
                return null;
            }
            final boolean userVisible = sa.getBoolean(com.android.internal.R.styleable.SyncAdapter_userVisible, true);
            final boolean supportsUploading = sa.getBoolean(com.android.internal.R.styleable.SyncAdapter_supportsUploading, true);
            final boolean isAlwaysSyncable = sa.getBoolean(com.android.internal.R.styleable.SyncAdapter_isAlwaysSyncable, false);
            final boolean allowParallelSyncs = sa.getBoolean(com.android.internal.R.styleable.SyncAdapter_allowParallelSyncs, false);
            final java.lang.String settingsActivity = sa.getString(com.android.internal.R.styleable.SyncAdapter_settingsActivity);
            return new android.content.SyncAdapterType(authority, accountType, userVisible, supportsUploading, isAlwaysSyncable, allowParallelSyncs, settingsActivity, packageName);
        } finally {
            sa.recycle();
        }
    }

    @java.lang.Override
    protected void onServicesChangedLocked(int userId) {
        synchronized(mServicesLock) {
            android.util.ArrayMap<java.lang.String, java.lang.String[]> adapterMap = mAuthorityToSyncAdapters.get(userId);
            if (adapterMap != null) {
                adapterMap.clear();
            }
        }
        super.onServicesChangedLocked(userId);
    }

    public java.lang.String[] getSyncAdapterPackagesForAuthority(java.lang.String authority, int userId) {
        synchronized(mServicesLock) {
            android.util.ArrayMap<java.lang.String, java.lang.String[]> adapterMap = mAuthorityToSyncAdapters.get(userId);
            if (adapterMap == null) {
                adapterMap = new android.util.ArrayMap();
                mAuthorityToSyncAdapters.put(userId, adapterMap);
            }
            // If the mapping exists, return it
            if (adapterMap.containsKey(authority)) {
                return adapterMap.get(authority);
            }
            // Create the mapping and cache it
            java.lang.String[] syncAdapterPackages;
            final java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>> serviceInfos;
            serviceInfos = getAllServices(userId);
            java.util.ArrayList<java.lang.String> packages = new java.util.ArrayList<>();
            for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> serviceInfo : serviceInfos) {
                if (authority.equals(serviceInfo.type.authority) && (serviceInfo.componentName != null)) {
                    packages.add(serviceInfo.componentName.getPackageName());
                }
            }
            syncAdapterPackages = new java.lang.String[packages.size()];
            packages.toArray(syncAdapterPackages);
            adapterMap.put(authority, syncAdapterPackages);
            return syncAdapterPackages;
        }
    }

    @java.lang.Override
    protected void onUserRemoved(int userId) {
        synchronized(mServicesLock) {
            mAuthorityToSyncAdapters.remove(userId);
        }
        super.onUserRemoved(userId);
    }

    static class MySerializer implements android.content.pm.XmlSerializerAndParser<android.content.SyncAdapterType> {
        public void writeAsXml(android.content.SyncAdapterType item, org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            out.attribute(null, "authority", item.authority);
            out.attribute(null, "accountType", item.accountType);
        }

        public android.content.SyncAdapterType createFromXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            final java.lang.String authority = parser.getAttributeValue(null, "authority");
            final java.lang.String accountType = parser.getAttributeValue(null, "accountType");
            return android.content.SyncAdapterType.newKey(authority, accountType);
        }
    }
}

