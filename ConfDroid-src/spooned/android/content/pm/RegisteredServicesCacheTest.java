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
 * limitations under the License
 */
package android.content.pm;


/**
 * Tests for {@link android.content.pm.RegisteredServicesCache}
 */
public class RegisteredServicesCacheTest extends android.test.AndroidTestCase {
    private static final int U0 = 0;

    private static final int U1 = 1;

    private static final int UID1 = 1;

    private static final int UID2 = 2;

    // Represents UID of a system image process
    private static final int SYSTEM_IMAGE_UID = 20;

    private final android.content.pm.ResolveInfo r1 = new android.content.pm.ResolveInfo();

    private final android.content.pm.ResolveInfo r2 = new android.content.pm.ResolveInfo();

    private final android.content.pm.RegisteredServicesCacheTest.TestServiceType t1 = new android.content.pm.RegisteredServicesCacheTest.TestServiceType("t1", "value1");

    private final android.content.pm.RegisteredServicesCacheTest.TestServiceType t2 = new android.content.pm.RegisteredServicesCacheTest.TestServiceType("t2", "value2");

    private java.io.File mDataDir;

    private java.io.File mSyncDir;

    private java.util.List<android.content.pm.UserInfo> mUsers;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        java.io.File cacheDir = mContext.getCacheDir();
        mDataDir = new java.io.File(cacheDir, "testServicesCache");
        android.os.FileUtils.deleteContents(mDataDir);
        mSyncDir = new java.io.File(mDataDir, "system/" + android.content.pm.RegisteredServicesCache.REGISTERED_SERVICES_DIR);
        mSyncDir.mkdirs();
        mUsers = new java.util.ArrayList<>();
        mUsers.add(new android.content.pm.UserInfo(0, "Owner", android.content.pm.UserInfo.FLAG_ADMIN));
        mUsers.add(new android.content.pm.UserInfo(1, "User1", 0));
    }

    public void testGetAllServicesHappyPath() {
        android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, android.content.pm.RegisteredServicesCacheTest.UID2));
        assertEquals(2, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(2, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertNotEmptyFileCreated(cache, android.content.pm.RegisteredServicesCacheTest.U0);
        // Make sure all services can be loaded from xml
        cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        assertEquals(2, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
    }

    public void testGetAllServicesReplaceUid() {
        android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, android.content.pm.RegisteredServicesCacheTest.UID2));
        cache.getAllServices(android.content.pm.RegisteredServicesCacheTest.U0);
        // Invalidate cache and clear update query results
        cache.invalidateCache(android.content.pm.RegisteredServicesCacheTest.U0);
        cache.clearServicesForQuerying();
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, android.content.pm.RegisteredServicesCacheTest.SYSTEM_IMAGE_UID));
        java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType>> allServices = cache.getAllServices(android.content.pm.RegisteredServicesCacheTest.U0);
        assertEquals(2, allServices.size());
        java.util.Set<java.lang.Integer> uids = new java.util.HashSet<>();
        for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType> srv : allServices) {
            uids.add(srv.uid);
        }
        assertTrue("UID must be updated to the new value", uids.contains(android.content.pm.RegisteredServicesCacheTest.SYSTEM_IMAGE_UID));
        assertFalse("UID must be updated to the new value", uids.contains(android.content.pm.RegisteredServicesCacheTest.UID2));
    }

    public void testGetAllServicesServiceRemoved() {
        android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, android.content.pm.RegisteredServicesCacheTest.UID2));
        assertEquals(2, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(2, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        // Re-read data from disk and verify services were saved
        cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        assertEquals(2, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        // Now register only one service and verify that another one is removed
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        assertEquals(1, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(1, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
    }

    public void testGetAllServicesMultiUser() {
        android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        int u1uid = android.os.UserHandle.getUid(android.content.pm.RegisteredServicesCacheTest.U1, 0);
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U1, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, u1uid));
        assertEquals(1, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(1, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(1, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U1));
        assertEquals(1, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U1));
        assertEquals("No services should be available for user 3", 0, cache.getAllServicesSize(3));
        // Re-read data from disk and verify services were saved
        cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        assertEquals(1, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(1, cache.getPersistentServicesSize(android.content.pm.RegisteredServicesCacheTest.U1));
        assertNotEmptyFileCreated(cache, android.content.pm.RegisteredServicesCacheTest.U0);
        assertNotEmptyFileCreated(cache, android.content.pm.RegisteredServicesCacheTest.U1);
    }

    public void testOnRemove() {
        android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, android.content.pm.RegisteredServicesCacheTest.UID1));
        int u1uid = android.os.UserHandle.getUid(android.content.pm.RegisteredServicesCacheTest.U1, 0);
        cache.addServiceForQuerying(android.content.pm.RegisteredServicesCacheTest.U1, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, u1uid));
        assertEquals(1, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(1, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U1));
        // Simulate ACTION_USER_REMOVED
        cache.onUserRemoved(android.content.pm.RegisteredServicesCacheTest.U1);
        // Make queryIntentServices(u1) return no results for U1
        cache.clearServicesForQuerying();
        assertEquals(1, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U0));
        assertEquals(0, cache.getAllServicesSize(android.content.pm.RegisteredServicesCacheTest.U1));
    }

    public void testMigration() {
        // Prepare "old" file for testing
        java.lang.String oldFile = "<?xml version=\'1.0\' encoding=\'utf-8\' standalone=\'yes\' ?>\n" + ((("<services>\n" + "    <service uid=\"1\" type=\"type1\" value=\"value1\" />\n") + "    <service uid=\"100002\" type=\"type2\" value=\"value2\" />\n") + "<services>\n");
        java.io.File file = new java.io.File(mSyncDir, android.content.pm.RegisteredServicesCacheTest.TestServicesCache.SERVICE_INTERFACE + ".xml");
        android.os.FileUtils.copyToFile(new java.io.ByteArrayInputStream(oldFile.getBytes()), file);
        int u0 = 0;
        int u1 = 1;
        android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        assertEquals(1, cache.getPersistentServicesSize(u0));
        assertEquals(1, cache.getPersistentServicesSize(u1));
        assertNotEmptyFileCreated(cache, u0);
        assertNotEmptyFileCreated(cache, u1);
        // Check that marker was created
        java.io.File markerFile = new java.io.File(mSyncDir, android.content.pm.RegisteredServicesCacheTest.TestServicesCache.SERVICE_INTERFACE + ".xml.migrated");
        assertTrue("Marker file should be created at " + markerFile, markerFile.exists());
        // Now introduce 2 service types for u0: t1, t2. type1 will be removed
        cache.addServiceForQuerying(0, r1, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t1, 1));
        cache.addServiceForQuerying(0, r2, android.content.pm.RegisteredServicesCacheTest.newServiceInfo(t2, 2));
        assertEquals(2, cache.getAllServicesSize(u0));
        assertEquals(0, cache.getAllServicesSize(u1));
        // Re-read data from disk. Verify that services were saved and old file was ignored
        cache = new android.content.pm.RegisteredServicesCacheTest.TestServicesCache();
        assertEquals(2, cache.getPersistentServicesSize(u0));
        assertEquals(0, cache.getPersistentServicesSize(u1));
    }

    private static android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType> newServiceInfo(android.content.pm.RegisteredServicesCacheTest.TestServiceType type, int uid) {
        return new android.content.pm.RegisteredServicesCache.ServiceInfo<>(type, null, uid);
    }

    private void assertNotEmptyFileCreated(android.content.pm.RegisteredServicesCacheTest.TestServicesCache cache, int userId) {
        java.io.File dir = new java.io.File(cache.getUserSystemDirectory(userId), android.content.pm.RegisteredServicesCache.REGISTERED_SERVICES_DIR);
        java.io.File file = new java.io.File(dir, android.content.pm.RegisteredServicesCacheTest.TestServicesCache.SERVICE_INTERFACE + ".xml");
        assertTrue("File should be created at " + file, file.length() > 0);
    }

    /**
     * Mock implementation of {@link android.content.pm.RegisteredServicesCache} for testing
     */
    private class TestServicesCache extends android.content.pm.RegisteredServicesCache<android.content.pm.RegisteredServicesCacheTest.TestServiceType> {
        static final java.lang.String SERVICE_INTERFACE = "RegisteredServicesCacheTest";

        static final java.lang.String SERVICE_META_DATA = "RegisteredServicesCacheTest";

        static final java.lang.String ATTRIBUTES_NAME = "test";

        private android.util.SparseArray<java.util.Map<android.content.pm.ResolveInfo, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType>>> mServices = new android.util.SparseArray();

        public TestServicesCache() {
            super(android.content.pm.RegisteredServicesCacheTest.this.mContext, android.content.pm.RegisteredServicesCacheTest.TestServicesCache.SERVICE_INTERFACE, android.content.pm.RegisteredServicesCacheTest.TestServicesCache.SERVICE_META_DATA, android.content.pm.RegisteredServicesCacheTest.TestServicesCache.ATTRIBUTES_NAME, new android.content.pm.RegisteredServicesCacheTest.TestSerializer());
        }

        @java.lang.Override
        public android.content.pm.RegisteredServicesCacheTest.TestServiceType parseServiceAttributes(android.content.res.Resources res, java.lang.String packageName, android.util.AttributeSet attrs) {
            return null;
        }

        @java.lang.Override
        protected java.util.List<android.content.pm.ResolveInfo> queryIntentServices(int userId) {
            java.util.Map<android.content.pm.ResolveInfo, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType>> map = mServices.get(userId, new java.util.HashMap<android.content.pm.ResolveInfo, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType>>());
            return new java.util.ArrayList<>(map.keySet());
        }

        @java.lang.Override
        protected java.io.File getUserSystemDirectory(int userId) {
            java.io.File dir = new java.io.File(mDataDir, "users/" + userId);
            dir.mkdirs();
            return dir;
        }

        @java.lang.Override
        protected java.util.List<android.content.pm.UserInfo> getUsers() {
            return mUsers;
        }

        @java.lang.Override
        protected android.content.pm.UserInfo getUser(int userId) {
            for (android.content.pm.UserInfo user : getUsers()) {
                if (user.id == userId) {
                    return user;
                }
            }
            return null;
        }

        @java.lang.Override
        protected java.io.File getDataDirectory() {
            return mDataDir;
        }

        void addServiceForQuerying(int userId, android.content.pm.ResolveInfo resolveInfo, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType> serviceInfo) {
            java.util.Map<android.content.pm.ResolveInfo, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType>> map = mServices.get(userId);
            if (map == null) {
                map = new java.util.HashMap<>();
                mServices.put(userId, map);
            }
            map.put(resolveInfo, serviceInfo);
        }

        void clearServicesForQuerying() {
            mServices.clear();
        }

        int getPersistentServicesSize(int user) {
            return getPersistentServices(user).size();
        }

        int getAllServicesSize(int user) {
            return getAllServices(user).size();
        }

        @java.lang.Override
        protected boolean inSystemImage(int callerUid) {
            return callerUid == android.content.pm.RegisteredServicesCacheTest.SYSTEM_IMAGE_UID;
        }

        @java.lang.Override
        protected android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType> parseServiceInfo(android.content.pm.ResolveInfo resolveInfo) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            int size = mServices.size();
            for (int i = 0; i < size; i++) {
                java.util.Map<android.content.pm.ResolveInfo, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType>> map = mServices.valueAt(i);
                android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.pm.RegisteredServicesCacheTest.TestServiceType> serviceInfo = map.get(resolveInfo);
                if (serviceInfo != null) {
                    return serviceInfo;
                }
            }
            throw new java.lang.IllegalArgumentException("Unexpected service " + resolveInfo);
        }

        @java.lang.Override
        public void onUserRemoved(int userId) {
            super.onUserRemoved(userId);
        }
    }

    static class TestSerializer implements android.content.pm.XmlSerializerAndParser<android.content.pm.RegisteredServicesCacheTest.TestServiceType> {
        public void writeAsXml(android.content.pm.RegisteredServicesCacheTest.TestServiceType item, org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            out.attribute(null, "type", item.type);
            out.attribute(null, "value", item.value);
        }

        public android.content.pm.RegisteredServicesCacheTest.TestServiceType createFromXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            final java.lang.String type = parser.getAttributeValue(null, "type");
            final java.lang.String value = parser.getAttributeValue(null, "value");
            return new android.content.pm.RegisteredServicesCacheTest.TestServiceType(type, value);
        }
    }

    static class TestServiceType implements android.os.Parcelable {
        final java.lang.String type;

        final java.lang.String value;

        public TestServiceType(java.lang.String type, java.lang.String value) {
            this.type = type;
            this.value = value;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.content.pm.RegisteredServicesCacheTest.TestServiceType that = ((android.content.pm.RegisteredServicesCacheTest.TestServiceType) (o));
            return type.equals(that.type) && value.equals(that.value);
        }

        @java.lang.Override
        public int hashCode() {
            return (31 * type.hashCode()) + value.hashCode();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((("TestServiceType{" + "type='") + type) + '\'') + ", value='") + value) + '\'') + '}';
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(type);
            dest.writeString(value);
        }

        public TestServiceType(android.os.Parcel source) {
            this(source.readString(), source.readString());
        }

        public static final android.content.pm.Creator<android.content.pm.RegisteredServicesCacheTest.TestServiceType> CREATOR = new android.content.pm.Creator<android.content.pm.RegisteredServicesCacheTest.TestServiceType>() {
            public android.content.pm.RegisteredServicesCacheTest.TestServiceType createFromParcel(android.os.Parcel source) {
                return new android.content.pm.RegisteredServicesCacheTest.TestServiceType(source);
            }

            public android.content.pm.RegisteredServicesCacheTest.TestServiceType[] newArray(int size) {
                return new android.content.pm.RegisteredServicesCacheTest.TestServiceType[size];
            }
        };
    }
}

