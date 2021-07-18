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
package android.content.pm;


/**
 * Cache of registered services. This cache is lazily built by interrogating
 * {@link PackageManager} on a per-user basis. It's updated as packages are
 * added, removed and changed. Users are responsible for calling
 * {@link #invalidateCache(int)} when a user is started, since
 * {@link PackageManager} broadcasts aren't sent for stopped users.
 * <p>
 * The services are referred to by type V and are made available via the
 * {@link #getServiceInfo} method.
 *
 * @unknown 
 */
public abstract class RegisteredServicesCache<V> {
    private static final java.lang.String TAG = "PackageManager";

    private static final boolean DEBUG = false;

    protected static final java.lang.String REGISTERED_SERVICES_DIR = "registered_services";

    public final android.content.Context mContext;

    private final java.lang.String mInterfaceName;

    private final java.lang.String mMetaDataName;

    private final java.lang.String mAttributesName;

    private final android.content.pm.XmlSerializerAndParser<V> mSerializerAndParser;

    protected final java.lang.Object mServicesLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mServicesLock")
    private final android.util.SparseArray<android.content.pm.RegisteredServicesCache.UserServices<V>> mUserServices = new android.util.SparseArray<android.content.pm.RegisteredServicesCache.UserServices<V>>(2);

    private static class UserServices<V> {
        @com.android.internal.annotations.GuardedBy("mServicesLock")
        final java.util.Map<V, java.lang.Integer> persistentServices = com.google.android.collect.Maps.newHashMap();

        @com.android.internal.annotations.GuardedBy("mServicesLock")
        java.util.Map<V, android.content.pm.RegisteredServicesCache.ServiceInfo<V>> services = null;

        @com.android.internal.annotations.GuardedBy("mServicesLock")
        boolean mPersistentServicesFileDidNotExist = true;

        @com.android.internal.annotations.GuardedBy("mServicesLock")
        boolean mBindInstantServiceAllowed = false;
    }

    @com.android.internal.annotations.GuardedBy("mServicesLock")
    private android.content.pm.RegisteredServicesCache.UserServices<V> findOrCreateUserLocked(int userId) {
        return findOrCreateUserLocked(userId, true);
    }

    @com.android.internal.annotations.GuardedBy("mServicesLock")
    private android.content.pm.RegisteredServicesCache.UserServices<V> findOrCreateUserLocked(int userId, boolean loadFromFileIfNew) {
        android.content.pm.RegisteredServicesCache.UserServices<V> services = mUserServices.get(userId);
        if (services == null) {
            services = new android.content.pm.RegisteredServicesCache.UserServices<V>();
            mUserServices.put(userId, services);
            if (loadFromFileIfNew && (mSerializerAndParser != null)) {
                // Check if user exists and try loading data from file
                // clear existing data if there was an error during migration
                android.content.pm.UserInfo user = getUser(userId);
                if (user != null) {
                    android.util.AtomicFile file = createFileForUser(user.id);
                    if (file.getBaseFile().exists()) {
                        if (android.content.pm.RegisteredServicesCache.DEBUG) {
                            android.util.Slog.i(android.content.pm.RegisteredServicesCache.TAG, java.lang.String.format("Loading u%s data from %s", user.id, file));
                        }
                        java.io.InputStream is = null;
                        try {
                            is = file.openRead();
                            readPersistentServicesLocked(is);
                        } catch (java.lang.Exception e) {
                            android.util.Log.w(android.content.pm.RegisteredServicesCache.TAG, "Error reading persistent services for user " + user.id, e);
                        } finally {
                            libcore.io.IoUtils.closeQuietly(is);
                        }
                    }
                }
            }
        }
        return services;
    }

    // the listener and handler are synchronized on "this" and must be updated together
    private android.content.pm.RegisteredServicesCacheListener<V> mListener;

    private android.os.Handler mHandler;

    @android.annotation.UnsupportedAppUsage
    public RegisteredServicesCache(android.content.Context context, java.lang.String interfaceName, java.lang.String metaDataName, java.lang.String attributeName, android.content.pm.XmlSerializerAndParser<V> serializerAndParser) {
        mContext = context;
        mInterfaceName = interfaceName;
        mMetaDataName = metaDataName;
        mAttributesName = attributeName;
        mSerializerAndParser = serializerAndParser;
        migrateIfNecessaryLocked();
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction(android.content.Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(android.content.Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(android.content.Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        mContext.registerReceiverAsUser(mPackageReceiver, UserHandle.ALL, intentFilter, null, null);
        // Register for events related to sdcard installation.
        android.content.IntentFilter sdFilter = new android.content.IntentFilter();
        sdFilter.addAction(android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mContext.registerReceiver(mExternalReceiver, sdFilter);
        // Register for user-related events
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        sdFilter.addAction(android.content.Intent.ACTION_USER_REMOVED);
        mContext.registerReceiver(mUserRemovedReceiver, userFilter);
    }

    private void handlePackageEvent(android.content.Intent intent, int userId) {
        // Don't regenerate the services map when the package is removed or its
        // ASEC container unmounted as a step in replacement.  The subsequent
        // _ADDED / _AVAILABLE call will regenerate the map in the final state.
        final java.lang.String action = intent.getAction();
        // it's a new-component action if it isn't some sort of removal
        final boolean isRemoval = android.content.Intent.ACTION_PACKAGE_REMOVED.equals(action) || android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action);
        // if it's a removal, is it part of an update-in-place step?
        final boolean replacing = intent.getBooleanExtra(android.content.Intent.EXTRA_REPLACING, false);
        if (isRemoval && replacing) {
            // package is going away, but it's the middle of an upgrade: keep the current
            // state and do nothing here.  This clause is intentionally empty.
        } else {
            int[] uids = null;
            // either we're adding/changing, or it's a removal without replacement, so
            // we need to update the set of available services
            if (android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action) || android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
                uids = intent.getIntArrayExtra(android.content.Intent.EXTRA_CHANGED_UID_LIST);
            } else {
                int uid = intent.getIntExtra(android.content.Intent.EXTRA_UID, -1);
                if (uid > 0) {
                    uids = new int[]{ uid };
                }
            }
            generateServicesMap(uids, userId);
        }
    }

    private final android.content.BroadcastReceiver mPackageReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            final int uid = intent.getIntExtra(android.content.Intent.EXTRA_UID, -1);
            if (uid != (-1)) {
                handlePackageEvent(intent, android.os.UserHandle.getUserId(uid));
            }
        }
    };

    private final android.content.BroadcastReceiver mExternalReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            // External apps can't coexist with multi-user, so scan owner
            handlePackageEvent(intent, UserHandle.USER_SYSTEM);
        }
    };

    private final android.content.BroadcastReceiver mUserRemovedReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int userId = intent.getIntExtra(android.content.Intent.EXTRA_USER_HANDLE, -1);
            if (android.content.pm.RegisteredServicesCache.DEBUG) {
                android.util.Slog.d(android.content.pm.RegisteredServicesCache.TAG, ("u" + userId) + " removed - cleaning up");
            }
            onUserRemoved(userId);
        }
    };

    public void invalidateCache(int userId) {
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            user.services = null;
            onServicesChangedLocked(userId);
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args, int userId) {
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.services != null) {
                fout.println(("RegisteredServicesCache: " + user.services.size()) + " services");
                for (android.content.pm.RegisteredServicesCache.ServiceInfo<?> info : user.services.values()) {
                    fout.println("  " + info);
                }
            } else {
                fout.println("RegisteredServicesCache: services not loaded");
            }
        }
    }

    public android.content.pm.RegisteredServicesCacheListener<V> getListener() {
        synchronized(this) {
            return mListener;
        }
    }

    public void setListener(android.content.pm.RegisteredServicesCacheListener<V> listener, android.os.Handler handler) {
        if (handler == null) {
            handler = new android.os.Handler(mContext.getMainLooper());
        }
        synchronized(this) {
            mHandler = handler;
            mListener = listener;
        }
    }

    private void notifyListener(final V type, final int userId, final boolean removed) {
        if (android.content.pm.RegisteredServicesCache.DEBUG) {
            android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, (("notifyListener: " + type) + " is ") + (removed ? "removed" : "added"));
        }
        android.content.pm.RegisteredServicesCacheListener<V> listener;
        android.os.Handler handler;
        synchronized(this) {
            listener = mListener;
            handler = mHandler;
        }
        if (listener == null) {
            return;
        }
        final android.content.pm.RegisteredServicesCacheListener<V> listener2 = listener;
        handler.post(() -> {
            try {
                listener2.onServiceChanged(type, userId, removed);
            } catch ( th) {
                android.util.Slog.wtf(TAG, "Exception from onServiceChanged", android.content.pm.th);
            }
        });
    }

    /**
     * Value type that describes a Service. The information within can be used
     * to bind to the service.
     */
    public static class ServiceInfo<V> {
        @android.annotation.UnsupportedAppUsage
        public final V type;

        public final android.content.pm.ComponentInfo componentInfo;

        @android.annotation.UnsupportedAppUsage
        public final android.content.ComponentName componentName;

        @android.annotation.UnsupportedAppUsage
        public final int uid;

        /**
         *
         *
         * @unknown 
         */
        public ServiceInfo(V type, android.content.pm.ComponentInfo componentInfo, android.content.ComponentName componentName) {
            this.type = type;
            this.componentInfo = componentInfo;
            this.componentName = componentName;
            this.uid = (componentInfo != null) ? componentInfo.applicationInfo.uid : -1;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((("ServiceInfo: " + type) + ", ") + componentName) + ", uid ") + uid;
        }
    }

    /**
     * Accessor for the registered authenticators.
     *
     * @param type
     * 		the account type of the authenticator
     * @return the AuthenticatorInfo that matches the account type or null if none is present
     */
    public android.content.pm.RegisteredServicesCache.ServiceInfo<V> getServiceInfo(V type, int userId) {
        synchronized(mServicesLock) {
            // Find user and lazily populate cache
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.services == null) {
                generateServicesMap(null, userId);
            }
            return user.services.get(type);
        }
    }

    /**
     *
     *
     * @return a collection of {@link RegisteredServicesCache.ServiceInfo} objects for all
    registered authenticators.
     */
    public java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<V>> getAllServices(int userId) {
        synchronized(mServicesLock) {
            // Find user and lazily populate cache
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.services == null) {
                generateServicesMap(null, userId);
            }
            return java.util.Collections.unmodifiableCollection(new java.util.ArrayList<android.content.pm.RegisteredServicesCache.ServiceInfo<V>>(user.services.values()));
        }
    }

    public void updateServices(int userId) {
        if (android.content.pm.RegisteredServicesCache.DEBUG) {
            android.util.Slog.d(android.content.pm.RegisteredServicesCache.TAG, "updateServices u" + userId);
        }
        java.util.List<android.content.pm.RegisteredServicesCache.ServiceInfo<V>> allServices;
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            // If services haven't been initialized yet - no updates required
            if (user.services == null) {
                return;
            }
            allServices = new java.util.ArrayList<>(user.services.values());
        }
        android.util.IntArray updatedUids = null;
        for (android.content.pm.RegisteredServicesCache.ServiceInfo<V> service : allServices) {
            long versionCode = service.componentInfo.applicationInfo.versionCode;
            java.lang.String pkg = service.componentInfo.packageName;
            android.content.pm.ApplicationInfo newAppInfo = null;
            try {
                newAppInfo = mContext.getPackageManager().getApplicationInfoAsUser(pkg, 0, userId);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                // Package uninstalled - treat as null app info
            }
            // If package updated or removed
            if ((newAppInfo == null) || (newAppInfo.versionCode != versionCode)) {
                if (android.content.pm.RegisteredServicesCache.DEBUG) {
                    android.util.Slog.d(android.content.pm.RegisteredServicesCache.TAG, (((("Package " + pkg) + " uid=") + service.uid) + " updated. New appInfo: ") + newAppInfo);
                }
                if (updatedUids == null) {
                    updatedUids = new android.util.IntArray();
                }
                updatedUids.add(service.uid);
            }
        }
        if ((updatedUids != null) && (updatedUids.size() > 0)) {
            int[] updatedUidsArray = updatedUids.toArray();
            generateServicesMap(updatedUidsArray, userId);
        }
    }

    /**
     *
     *
     * @return whether the binding to service is allowed for instant apps.
     */
    public boolean getBindInstantServiceAllowed(int userId) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.MANAGE_BIND_INSTANT_SERVICE, "getBindInstantServiceAllowed");
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            return user.mBindInstantServiceAllowed;
        }
    }

    /**
     * Set whether the binding to service is allowed or not for instant apps.
     */
    public void setBindInstantServiceAllowed(int userId, boolean allowed) {
        mContext.enforceCallingOrSelfPermission(Manifest.permission.MANAGE_BIND_INSTANT_SERVICE, "setBindInstantServiceAllowed");
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            user.mBindInstantServiceAllowed = allowed;
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    protected boolean inSystemImage(int callerUid) {
        java.lang.String[] packages = mContext.getPackageManager().getPackagesForUid(callerUid);
        if (packages != null) {
            for (java.lang.String name : packages) {
                try {
                    android.content.pm.PackageInfo packageInfo = /* flags */
                    mContext.getPackageManager().getPackageInfo(name, 0);
                    if ((packageInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
                        return true;
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.util.List<android.content.pm.ResolveInfo> queryIntentServices(int userId) {
        final android.content.pm.PackageManager pm = mContext.getPackageManager();
        int flags = (android.content.pm.PackageManager.GET_META_DATA | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_AWARE) | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_UNAWARE;
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.mBindInstantServiceAllowed) {
                flags |= android.content.pm.PackageManager.MATCH_INSTANT;
            }
        }
        return pm.queryIntentServicesAsUser(new android.content.Intent(mInterfaceName), flags, userId);
    }

    /**
     * Populate {@link UserServices#services} by scanning installed packages for
     * given {@link UserHandle}.
     *
     * @param changedUids
     * 		the array of uids that have been affected, as mentioned in the broadcast
     * 		or null to assume that everything is affected.
     * @param userId
     * 		the user for whom to update the services map.
     */
    private void generateServicesMap(int[] changedUids, int userId) {
        if (android.content.pm.RegisteredServicesCache.DEBUG) {
            android.util.Slog.d(android.content.pm.RegisteredServicesCache.TAG, (("generateServicesMap() for " + userId) + ", changed UIDs = ") + java.util.Arrays.toString(changedUids));
        }
        final java.util.ArrayList<android.content.pm.RegisteredServicesCache.ServiceInfo<V>> serviceInfos = new java.util.ArrayList<>();
        final java.util.List<android.content.pm.ResolveInfo> resolveInfos = queryIntentServices(userId);
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            try {
                android.content.pm.RegisteredServicesCache.ServiceInfo<V> info = parseServiceInfo(resolveInfo);
                if (info == null) {
                    android.util.Log.w(android.content.pm.RegisteredServicesCache.TAG, "Unable to load service info " + resolveInfo.toString());
                    continue;
                }
                serviceInfos.add(info);
            } catch (org.xmlpull.v1.XmlPullParserException | java.io.IOException e) {
                android.util.Log.w(android.content.pm.RegisteredServicesCache.TAG, "Unable to load service info " + resolveInfo.toString(), e);
            }
        }
        synchronized(mServicesLock) {
            final android.content.pm.RegisteredServicesCache.UserServices<V> user = findOrCreateUserLocked(userId);
            final boolean firstScan = user.services == null;
            if (firstScan) {
                user.services = com.google.android.collect.Maps.newHashMap();
            }
            java.lang.StringBuilder changes = new java.lang.StringBuilder();
            boolean changed = false;
            for (android.content.pm.RegisteredServicesCache.ServiceInfo<V> info : serviceInfos) {
                // four cases:
                // - doesn't exist yet
                // - add, notify user that it was added
                // - exists and the UID is the same
                // - replace, don't notify user
                // - exists, the UID is different, and the new one is not a system package
                // - ignore
                // - exists, the UID is different, and the new one is a system package
                // - add, notify user that it was added
                java.lang.Integer previousUid = user.persistentServices.get(info.type);
                if (previousUid == null) {
                    if (android.content.pm.RegisteredServicesCache.DEBUG) {
                        changes.append("  New service added: ").append(info).append("\n");
                    }
                    changed = true;
                    user.services.put(info.type, info);
                    user.persistentServices.put(info.type, info.uid);
                    if (!(user.mPersistentServicesFileDidNotExist && firstScan)) {
                        /* removed */
                        notifyListener(info.type, userId, false);
                    }
                } else
                    if (previousUid == info.uid) {
                        if (android.content.pm.RegisteredServicesCache.DEBUG) {
                            changes.append("  Existing service (nop): ").append(info).append("\n");
                        }
                        user.services.put(info.type, info);
                    } else
                        if (inSystemImage(info.uid) || (!containsTypeAndUid(serviceInfos, info.type, previousUid))) {
                            if (android.content.pm.RegisteredServicesCache.DEBUG) {
                                if (inSystemImage(info.uid)) {
                                    changes.append("  System service replacing existing: ").append(info).append("\n");
                                } else {
                                    changes.append("  Existing service replacing a removed service: ").append(info).append("\n");
                                }
                            }
                            changed = true;
                            user.services.put(info.type, info);
                            user.persistentServices.put(info.type, info.uid);
                            /* removed */
                            notifyListener(info.type, userId, false);
                        } else {
                            // ignore
                            if (android.content.pm.RegisteredServicesCache.DEBUG) {
                                changes.append("  Existing service with new uid ignored: ").append(info).append("\n");
                            }
                        }


            }
            java.util.ArrayList<V> toBeRemoved = com.google.android.collect.Lists.newArrayList();
            for (V v1 : user.persistentServices.keySet()) {
                // Remove a persisted service that's not in the currently available services list.
                // And only if it is in the list of changedUids.
                if ((!containsType(serviceInfos, v1)) && containsUid(changedUids, user.persistentServices.get(v1))) {
                    toBeRemoved.add(v1);
                }
            }
            for (V v1 : toBeRemoved) {
                if (android.content.pm.RegisteredServicesCache.DEBUG) {
                    changes.append("  Service removed: ").append(v1).append("\n");
                }
                changed = true;
                user.persistentServices.remove(v1);
                user.services.remove(v1);
                /* removed */
                notifyListener(v1, userId, true);
            }
            if (android.content.pm.RegisteredServicesCache.DEBUG) {
                android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, "user.services=");
                for (V v : user.services.keySet()) {
                    android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, (("  " + v) + " ") + user.services.get(v));
                }
                android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, "user.persistentServices=");
                for (V v : user.persistentServices.keySet()) {
                    android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, (("  " + v) + " ") + user.persistentServices.get(v));
                }
            }
            if (android.content.pm.RegisteredServicesCache.DEBUG) {
                if (changes.length() > 0) {
                    android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, (((("generateServicesMap(" + mInterfaceName) + "): ") + serviceInfos.size()) + " services:\n") + changes);
                } else {
                    android.util.Log.d(android.content.pm.RegisteredServicesCache.TAG, ((("generateServicesMap(" + mInterfaceName) + "): ") + serviceInfos.size()) + " services unchanged");
                }
            }
            if (changed) {
                onServicesChangedLocked(userId);
                writePersistentServicesLocked(user, userId);
            }
        }
    }

    protected void onServicesChangedLocked(int userId) {
        // Feel free to override
    }

    /**
     * Returns true if the list of changed uids is null (wildcard) or the specified uid
     * is contained in the list of changed uids.
     */
    private boolean containsUid(int[] changedUids, int uid) {
        return (changedUids == null) || com.android.internal.util.ArrayUtils.contains(changedUids, uid);
    }

    private boolean containsType(java.util.ArrayList<android.content.pm.RegisteredServicesCache.ServiceInfo<V>> serviceInfos, V type) {
        for (int i = 0, N = serviceInfos.size(); i < N; i++) {
            if (serviceInfos.get(i).type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsTypeAndUid(java.util.ArrayList<android.content.pm.RegisteredServicesCache.ServiceInfo<V>> serviceInfos, V type, int uid) {
        for (int i = 0, N = serviceInfos.size(); i < N; i++) {
            final android.content.pm.RegisteredServicesCache.ServiceInfo<V> serviceInfo = serviceInfos.get(i);
            if (serviceInfo.type.equals(type) && (serviceInfo.uid == uid)) {
                return true;
            }
        }
        return false;
    }

    @com.android.internal.annotations.VisibleForTesting
    protected android.content.pm.RegisteredServicesCache.ServiceInfo<V> parseServiceInfo(android.content.pm.ResolveInfo service) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.ServiceInfo si = service.serviceInfo;
        android.content.ComponentName componentName = new android.content.ComponentName(si.packageName, si.name);
        android.content.pm.PackageManager pm = mContext.getPackageManager();
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, mMetaDataName);
            if (parser == null) {
                throw new org.xmlpull.v1.XmlPullParserException(("No " + mMetaDataName) + " meta-data");
            }
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            java.lang.String nodeName = parser.getName();
            if (!mAttributesName.equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException(("Meta-data does not start with " + mAttributesName) + " tag");
            }
            V v = parseServiceAttributes(pm.getResourcesForApplication(si.applicationInfo), si.packageName, attrs);
            if (v == null) {
                return null;
            }
            final android.content.pm.ServiceInfo serviceInfo = service.serviceInfo;
            return new android.content.pm.RegisteredServicesCache.ServiceInfo<V>(v, serviceInfo, componentName);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new org.xmlpull.v1.XmlPullParserException("Unable to load resources for pacakge " + si.packageName);
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    /**
     * Read all sync status back in to the initial engine state.
     */
    private void readPersistentServicesLocked(java.io.InputStream is) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
        parser.setInput(is, java.nio.charset.StandardCharsets.UTF_8.name());
        int eventType = parser.getEventType();
        while ((eventType != org.xmlpull.v1.XmlPullParser.START_TAG) && (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            eventType = parser.next();
        } 
        java.lang.String tagName = parser.getName();
        if ("services".equals(tagName)) {
            eventType = parser.next();
            do {
                if ((eventType == org.xmlpull.v1.XmlPullParser.START_TAG) && (parser.getDepth() == 2)) {
                    tagName = parser.getName();
                    if ("service".equals(tagName)) {
                        V service = mSerializerAndParser.createFromXml(parser);
                        if (service == null) {
                            break;
                        }
                        java.lang.String uidString = parser.getAttributeValue(null, "uid");
                        final int uid = java.lang.Integer.parseInt(uidString);
                        final int userId = android.os.UserHandle.getUserId(uid);
                        final android.content.pm.RegisteredServicesCache.UserServices<V> user = /* loadFromFileIfNew */
                        findOrCreateUserLocked(userId, false);
                        user.persistentServices.put(service, uid);
                    }
                }
                eventType = parser.next();
            } while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT );
        }
    }

    private void migrateIfNecessaryLocked() {
        if (mSerializerAndParser == null) {
            return;
        }
        java.io.File systemDir = new java.io.File(getDataDirectory(), "system");
        java.io.File syncDir = new java.io.File(systemDir, android.content.pm.RegisteredServicesCache.REGISTERED_SERVICES_DIR);
        android.util.AtomicFile oldFile = new android.util.AtomicFile(new java.io.File(syncDir, mInterfaceName + ".xml"));
        boolean oldFileExists = oldFile.getBaseFile().exists();
        if (oldFileExists) {
            java.io.File marker = new java.io.File(syncDir, mInterfaceName + ".xml.migrated");
            // if not migrated, perform the migration and add a marker
            if (!marker.exists()) {
                if (android.content.pm.RegisteredServicesCache.DEBUG) {
                    android.util.Slog.i(android.content.pm.RegisteredServicesCache.TAG, ("Marker file " + marker) + " does not exist - running migration");
                }
                java.io.InputStream is = null;
                try {
                    is = oldFile.openRead();
                    mUserServices.clear();
                    readPersistentServicesLocked(is);
                } catch (java.lang.Exception e) {
                    android.util.Log.w(android.content.pm.RegisteredServicesCache.TAG, "Error reading persistent services, starting from scratch", e);
                } finally {
                    libcore.io.IoUtils.closeQuietly(is);
                }
                try {
                    for (android.content.pm.UserInfo user : getUsers()) {
                        android.content.pm.RegisteredServicesCache.UserServices<V> userServices = mUserServices.get(user.id);
                        if (userServices != null) {
                            if (android.content.pm.RegisteredServicesCache.DEBUG) {
                                android.util.Slog.i(android.content.pm.RegisteredServicesCache.TAG, (("Migrating u" + user.id) + " services ") + userServices.persistentServices);
                            }
                            writePersistentServicesLocked(userServices, user.id);
                        }
                    }
                    marker.createNewFile();
                } catch (java.lang.Exception e) {
                    android.util.Log.w(android.content.pm.RegisteredServicesCache.TAG, "Migration failed", e);
                }
                // Migration is complete and we don't need to keep data for all users anymore,
                // It will be loaded from a new location when requested
                mUserServices.clear();
            }
        }
    }

    /**
     * Writes services of a specified user to the file.
     */
    private void writePersistentServicesLocked(android.content.pm.RegisteredServicesCache.UserServices<V> user, int userId) {
        if (mSerializerAndParser == null) {
            return;
        }
        android.util.AtomicFile atomicFile = createFileForUser(userId);
        java.io.FileOutputStream fos = null;
        try {
            fos = atomicFile.startWrite();
            org.xmlpull.v1.XmlSerializer out = new com.android.internal.util.FastXmlSerializer();
            out.setOutput(fos, java.nio.charset.StandardCharsets.UTF_8.name());
            out.startDocument(null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            out.startTag(null, "services");
            for (java.util.Map.Entry<V, java.lang.Integer> service : user.persistentServices.entrySet()) {
                out.startTag(null, "service");
                out.attribute(null, "uid", java.lang.Integer.toString(service.getValue()));
                mSerializerAndParser.writeAsXml(service.getKey(), out);
                out.endTag(null, "service");
            }
            out.endTag(null, "services");
            out.endDocument();
            atomicFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            android.util.Log.w(android.content.pm.RegisteredServicesCache.TAG, "Error writing accounts", e1);
            if (fos != null) {
                atomicFile.failWrite(fos);
            }
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    protected void onUserRemoved(int userId) {
        synchronized(mServicesLock) {
            mUserServices.remove(userId);
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.util.List<android.content.pm.UserInfo> getUsers() {
        return getUsers(true);
    }

    @com.android.internal.annotations.VisibleForTesting
    protected android.content.pm.UserInfo getUser(int userId) {
        return android.os.UserManager.get(mContext).getUserInfo(userId);
    }

    private android.util.AtomicFile createFileForUser(int userId) {
        java.io.File userDir = getUserSystemDirectory(userId);
        java.io.File userFile = new java.io.File(userDir, ((android.content.pm.RegisteredServicesCache.REGISTERED_SERVICES_DIR + "/") + mInterfaceName) + ".xml");
        return new android.util.AtomicFile(userFile);
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.io.File getUserSystemDirectory(int userId) {
        return android.os.Environment.getUserSystemDirectory(userId);
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.io.File getDataDirectory() {
        return android.os.Environment.getDataDirectory();
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.util.Map<V, java.lang.Integer> getPersistentServices(int userId) {
        return findOrCreateUserLocked(userId).persistentServices;
    }

    public abstract V parseServiceAttributes(android.content.res.Resources res, java.lang.String packageName, android.util.AttributeSet attrs);
}

