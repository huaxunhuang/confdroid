/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Tests for disabling and enabling application components.
 *
 * Note: These tests are on the slow side.  This is probably because most of the tests trigger the
 * package settings file to get written out by the PackageManagerService.  Better, more unit-y test
 * would fix this.
 */
public class ComponentTest extends android.test.AndroidTestCase {
    private android.content.pm.PackageManager mPackageManager;

    private android.content.Intent mDisabledActivityIntent;

    private android.content.Intent mEnabledActivityIntent;

    private android.content.Intent mDisabledServiceIntent;

    private android.content.Intent mEnabledServiceIntent;

    private android.content.Intent mDisabledReceiverIntent;

    private android.content.Intent mEnabledReceiverIntent;

    private android.content.Intent mDisabledAppEnabledActivityIntent;

    private static final java.lang.String ENABLED_PACKAGENAME = "com.android.frameworks.coretests.enabled_app";

    private static final java.lang.String DISABLED_PACKAGENAME = "com.android.frameworks.coretests.disabled_app";

    private static final java.lang.String DISABLED_ACTIVITY_CLASSNAME = com.android.frameworks.coretests.enabled_app.DisabledActivity.class.getName();

    private static final android.content.ComponentName DISABLED_ACTIVITY_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.DISABLED_ACTIVITY_CLASSNAME);

    private static final java.lang.String ENABLED_ACTIVITY_CLASSNAME = com.android.frameworks.coretests.enabled_app.EnabledActivity.class.getName();

    private static final android.content.ComponentName ENABLED_ACTIVITY_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.ENABLED_ACTIVITY_CLASSNAME);

    private static final java.lang.String DISABLED_SERVICE_CLASSNAME = com.android.frameworks.coretests.enabled_app.DisabledService.class.getName();

    private static final android.content.ComponentName DISABLED_SERVICE_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.DISABLED_SERVICE_CLASSNAME);

    private static final java.lang.String DISABLED_PROVIDER_CLASSNAME = com.android.frameworks.coretests.enabled_app.DisabledProvider.class.getName();

    private static final android.content.ComponentName DISABLED_PROVIDER_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.DISABLED_PROVIDER_CLASSNAME);

    private static final java.lang.String DISABLED_PROVIDER_NAME = com.android.frameworks.coretests.enabled_app.DisabledProvider.class.getName();

    private static final java.lang.String ENABLED_SERVICE_CLASSNAME = com.android.frameworks.coretests.enabled_app.EnabledService.class.getName();

    private static final android.content.ComponentName ENABLED_SERVICE_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.ENABLED_SERVICE_CLASSNAME);

    private static final java.lang.String DISABLED_RECEIVER_CLASSNAME = com.android.frameworks.coretests.enabled_app.DisabledReceiver.class.getName();

    private static final android.content.ComponentName DISABLED_RECEIVER_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.DISABLED_RECEIVER_CLASSNAME);

    private static final java.lang.String ENABLED_RECEIVER_CLASSNAME = com.android.frameworks.coretests.enabled_app.EnabledReceiver.class.getName();

    private static final android.content.ComponentName ENABLED_RECEIVER_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.ENABLED_RECEIVER_CLASSNAME);

    private static final java.lang.String ENABLED_PROVIDER_CLASSNAME = com.android.frameworks.coretests.enabled_app.EnabledProvider.class.getName();

    private static final android.content.ComponentName ENABLED_PROVIDER_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.ComponentTest.ENABLED_PROVIDER_CLASSNAME);

    private static final java.lang.String ENABLED_PROVIDER_NAME = com.android.frameworks.coretests.enabled_app.EnabledProvider.class.getName();

    private static final java.lang.String DISABLED_APP_ENABLED_ACTIVITY_CLASSNAME = com.android.frameworks.coretests.enabled_app.EnabledActivity.class.getName();

    private static final android.content.ComponentName DISABLED_APP_ENABLED_ACTIVITY_COMPONENTNAME = new android.content.ComponentName(android.content.pm.ComponentTest.DISABLED_PACKAGENAME, android.content.pm.ComponentTest.DISABLED_APP_ENABLED_ACTIVITY_CLASSNAME);

    private static final java.lang.String TEST_CATEGORY = "com.android.frameworks.coretests.enabled_app.TEST_CATEGORY";

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mPackageManager = mContext.getPackageManager();
        mDisabledActivityIntent = new android.content.Intent();
        mDisabledActivityIntent.setComponent(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME);
        mEnabledActivityIntent = new android.content.Intent();
        mEnabledActivityIntent.setComponent(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME);
        mDisabledServiceIntent = new android.content.Intent();
        mDisabledServiceIntent.setComponent(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME);
        mEnabledServiceIntent = new android.content.Intent();
        mEnabledServiceIntent.setComponent(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME);
        mDisabledReceiverIntent = new android.content.Intent("android.intent.action.ENABLED_APP_DISABLED_RECEIVER");
        mDisabledReceiverIntent.setComponent(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME);
        mEnabledReceiverIntent = new android.content.Intent("android.intent.action.ENABLED_APP_ENABLED_RECEIVER");
        mEnabledReceiverIntent.setComponent(android.content.pm.ComponentTest.ENABLED_RECEIVER_COMPONENTNAME);
        mDisabledAppEnabledActivityIntent = new android.content.Intent();
        mDisabledAppEnabledActivityIntent.setComponent(android.content.pm.ComponentTest.DISABLED_APP_ENABLED_ACTIVITY_COMPONENTNAME);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testContextNotNull() throws java.lang.Exception {
        assertNotNull(mContext);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testResolveDisabledActivity() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mDisabledActivityIntent, 0);
        assertNull(info);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveActivity(mDisabledActivityIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(info2);
        assertNotNull(info2.activityInfo);
        assertFalse(info2.activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testResolveEnabledActivity() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mEnabledActivityIntent, 0);
        assertNotNull(info);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        assertTrue(info.activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testQueryDisabledActivity() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryIntentActivities(mDisabledActivityIntent, 0);
        assertEquals(0, infoList.size());
        final java.util.List<android.content.pm.ResolveInfo> infoList2 = mPackageManager.queryIntentActivities(mDisabledActivityIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertEquals(1, infoList2.size());
        final android.content.pm.ResolveInfo info = infoList2.get(0);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        assertFalse(info.activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testQueryEnabledActivity() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryIntentActivities(mEnabledActivityIntent, 0);
        assertEquals(1, infoList.size());
        final android.content.pm.ResolveInfo info = infoList.get(0);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        assertTrue(info.activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGetDisabledActivityInfo() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        try {
            mPackageManager.getActivityInfo(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, 0);
            fail("Attempt to get info on disabled component should fail.");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // expected
        }
        final android.content.pm.ActivityInfo activityInfo = mPackageManager.getActivityInfo(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(activityInfo);
        assertFalse(activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetEnabledActivityInfo() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ActivityInfo activityInfo = mPackageManager.getActivityInfo(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, 0);
        assertNotNull(activityInfo);
        assertTrue(activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testEnableActivity() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mDisabledActivityIntent, 0);
        assertNull(info);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveActivity(mDisabledActivityIntent, 0);
        assertNotNull(info2);
        assertNotNull(info2.activityInfo);
        assertFalse(info2.activityInfo.enabled);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryIntentActivities(mDisabledActivityIntent, 0);
        assertEquals(1, infoList.size());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDisableActivity() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mEnabledActivityIntent, 0);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveActivity(mEnabledActivityIntent, 0);
        assertNull(info2);
        final android.content.pm.ResolveInfo info3 = mPackageManager.resolveActivity(mEnabledActivityIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(info3);
        assertNotNull(info3.activityInfo);
        assertTrue(info3.activityInfo.enabled);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryIntentActivities(mEnabledActivityIntent, 0);
        assertEquals(0, infoList.size());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testResolveDisabledService() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveService(mDisabledServiceIntent, 0);
        assertNull(info);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveService(mDisabledServiceIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(info2);
        assertNotNull(info2.serviceInfo);
        assertFalse(info2.serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testResolveEnabledService() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveService(mEnabledServiceIntent, 0);
        assertNotNull(info);
        assertNotNull(info);
        assertNotNull(info.serviceInfo);
        assertTrue(info.serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testQueryDisabledService() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryIntentServices(mDisabledServiceIntent, 0);
        assertEquals(0, infoList.size());
        final java.util.List<android.content.pm.ResolveInfo> infoList2 = mPackageManager.queryIntentServices(mDisabledServiceIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertEquals(1, infoList2.size());
        final android.content.pm.ResolveInfo info = infoList2.get(0);
        assertNotNull(info);
        assertNotNull(info.serviceInfo);
        assertFalse(info.serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testQueryEnabledService() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryIntentServices(mEnabledServiceIntent, 0);
        assertEquals(1, infoList.size());
        final android.content.pm.ResolveInfo info = infoList.get(0);
        assertNotNull(info);
        assertNotNull(info.serviceInfo);
        assertTrue(info.serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGetDisabledServiceInfo() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        try {
            mPackageManager.getServiceInfo(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, 0);
            fail("Attempt to get info on disabled component should fail.");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // expected
        }
        final android.content.pm.ServiceInfo serviceInfo = mPackageManager.getServiceInfo(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(serviceInfo);
        assertFalse(serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetEnabledServiceInfo() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ServiceInfo serviceInfo = mPackageManager.getServiceInfo(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME, 0);
        assertNotNull(serviceInfo);
        assertTrue(serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testEnableService() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveService(mDisabledServiceIntent, 0);
        assertNull(info);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveService(mDisabledServiceIntent, 0);
        assertNotNull(info2);
        assertNotNull(info2.serviceInfo);
        assertFalse(info2.serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDisableService() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveService(mEnabledServiceIntent, 0);
        assertNotNull(info);
        assertNotNull(info.serviceInfo);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_SERVICE_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveService(mEnabledServiceIntent, 0);
        assertNull(info2);
        final android.content.pm.ResolveInfo info3 = mPackageManager.resolveService(mEnabledServiceIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(info3);
        assertNotNull(info3.serviceInfo);
        assertTrue(info3.serviceInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testQueryDisabledReceiver() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryBroadcastReceivers(mDisabledReceiverIntent, 0);
        assertEquals(0, infoList.size());
        final java.util.List<android.content.pm.ResolveInfo> infoList2 = mPackageManager.queryBroadcastReceivers(mDisabledReceiverIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertEquals(1, infoList2.size());
        final android.content.pm.ResolveInfo info = infoList2.get(0);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        assertFalse(info.activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testQueryEnabledReceiver() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> infoList = mPackageManager.queryBroadcastReceivers(mEnabledReceiverIntent, 0);
        assertEquals(1, infoList.size());
        final android.content.pm.ResolveInfo info = infoList.get(0);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        assertTrue(info.activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGetDisabledReceiverInfo() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        try {
            mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, 0);
            fail("Attempt to get info on disabled component should fail.");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // expected
        }
        final android.content.pm.ActivityInfo activityInfo = mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(activityInfo);
        assertFalse(activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetEnabledReceiverInfo() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ActivityInfo activityInfo = mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.ENABLED_RECEIVER_COMPONENTNAME, 0);
        assertNotNull(activityInfo);
        assertTrue(activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testEnableReceiver() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        try {
            mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, 0);
            fail("Attempt to get info on disabled component should fail.");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // expected
        }
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ActivityInfo activityInfo = mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, 0);
        assertNotNull(activityInfo);
        assertFalse(activityInfo.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDisableReceiver() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ActivityInfo activityInfo = mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.ENABLED_RECEIVER_COMPONENTNAME, 0);
        assertNotNull(activityInfo);
        assertTrue(activityInfo.enabled);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        try {
            mPackageManager.getReceiverInfo(android.content.pm.ComponentTest.DISABLED_RECEIVER_COMPONENTNAME, 0);
            fail("Attempt to get info on disabled component should fail.");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // expected
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testResolveEnabledProvider() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ProviderInfo providerInfo = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.ENABLED_PROVIDER_NAME, 0);
        assertNotNull(providerInfo);
        assertTrue(providerInfo.enabled);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testResolveDisabledProvider() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ProviderInfo providerInfo = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.DISABLED_PROVIDER_NAME, 0);
        assertNull(providerInfo);
        android.content.pm.ProviderInfo providerInfo2 = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.DISABLED_PROVIDER_NAME, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(providerInfo2);
        assertFalse(providerInfo2.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testEnableProvider() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ProviderInfo providerInfo = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.DISABLED_PROVIDER_NAME, 0);
        assertNull(providerInfo);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ProviderInfo providerInfo2 = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.DISABLED_PROVIDER_NAME, 0);
        assertNotNull(providerInfo2);
        assertFalse(providerInfo2.enabled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDisableProvider() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ProviderInfo providerInfo = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.ENABLED_PROVIDER_NAME, 0);
        assertNotNull(providerInfo);
        assertTrue(providerInfo.enabled);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.ProviderInfo providerInfo2 = mPackageManager.resolveContentProvider(android.content.pm.ComponentTest.ENABLED_PROVIDER_NAME, 0);
        assertNull(providerInfo2);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testQueryEnabledProvider() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        java.lang.String enabledProviderProcessName = getComponentProcessName(android.content.pm.ComponentTest.ENABLED_PROVIDER_NAME);
        android.content.pm.PackageInfo pi = mPackageManager.getPackageInfo(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, 0);
        java.util.List<android.content.pm.ProviderInfo> providerInfoList = mPackageManager.queryContentProviders(enabledProviderProcessName, pi.applicationInfo.uid, 0);
        assertNotNull(providerInfoList);
        assertEquals(1, providerInfoList.size());
        assertEquals(android.content.pm.ComponentTest.ENABLED_PROVIDER_CLASSNAME, providerInfoList.get(0).name);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testQueryDisabledProvider() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_PROVIDER_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.pm.PackageInfo pi = mPackageManager.getPackageInfo(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, 0);
        java.lang.String disabledProviderProcessName = getComponentProcessName(android.content.pm.ComponentTest.DISABLED_PROVIDER_NAME);
        java.util.List<android.content.pm.ProviderInfo> providerInfoList = mPackageManager.queryContentProviders(disabledProviderProcessName, pi.applicationInfo.uid, 0);
        assertNull(providerInfoList);
        java.util.List<android.content.pm.ProviderInfo> providerInfoList2 = mPackageManager.queryContentProviders(disabledProviderProcessName, pi.applicationInfo.uid, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(providerInfoList2);
        assertEquals(1, providerInfoList2.size());
        assertEquals(android.content.pm.ComponentTest.DISABLED_PROVIDER_CLASSNAME, providerInfoList2.get(0).name);
    }

    private java.lang.String getComponentProcessName(java.lang.String componentNameStr) {
        android.content.pm.ComponentInfo providerInfo = mPackageManager.resolveContentProvider(componentNameStr, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        return providerInfo.processName;
    }

    public void DISABLED_testResolveEnabledActivityInDisabledApp() throws java.lang.Exception {
        mPackageManager.setApplicationEnabledSetting(android.content.pm.ComponentTest.DISABLED_PACKAGENAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_APP_ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mDisabledAppEnabledActivityIntent, 0);
        assertNull(info);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveActivity(mDisabledAppEnabledActivityIntent, android.content.pm.PackageManager.GET_DISABLED_COMPONENTS);
        assertNotNull(info2);
        assertNotNull(info2.activityInfo);
        assertTrue(info2.activityInfo.enabled);
    }

    public void DISABLED_testEnableApplication() throws java.lang.Exception {
        mPackageManager.setApplicationEnabledSetting(android.content.pm.ComponentTest.DISABLED_PACKAGENAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_APP_ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mDisabledAppEnabledActivityIntent, 0);
        assertNull(info);
        mPackageManager.setApplicationEnabledSetting(android.content.pm.ComponentTest.DISABLED_PACKAGENAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveActivity(mDisabledAppEnabledActivityIntent, 0);
        assertNotNull(info2);
        assertNotNull(info2.activityInfo);
        assertTrue(info2.activityInfo.enabled);
    }

    public void DISABLED_testDisableApplication() throws java.lang.Exception {
        mPackageManager.setApplicationEnabledSetting(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.ENABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        final android.content.pm.ResolveInfo info = mPackageManager.resolveActivity(mEnabledActivityIntent, 0);
        assertNotNull(info);
        assertNotNull(info.activityInfo);
        assertTrue(info.activityInfo.enabled);
        mPackageManager.setApplicationEnabledSetting(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
        final android.content.pm.ResolveInfo info2 = mPackageManager.resolveActivity(mEnabledActivityIntent, 0);
        assertNull(info2);
        // Clean up
        mPackageManager.setApplicationEnabledSetting(android.content.pm.ComponentTest.ENABLED_PACKAGENAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNonExplicitResolveAfterEnabling() throws java.lang.Exception {
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, android.content.pm.PackageManager.DONT_KILL_APP);
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN, null);
        intent.addCategory(android.content.pm.ComponentTest.TEST_CATEGORY);
        final java.util.List<android.content.pm.ResolveInfo> launchables = mPackageManager.queryIntentActivities(intent, 0);
        int numItems = launchables.size();
        assertEquals(0, numItems);
        mPackageManager.setComponentEnabledSetting(android.content.pm.ComponentTest.DISABLED_ACTIVITY_COMPONENTNAME, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP);
        final java.util.List<android.content.pm.ResolveInfo> launchables2 = mPackageManager.queryIntentActivities(intent, 0);
        int numItems2 = launchables2.size();
        assertEquals(1, numItems2);
    }
}

