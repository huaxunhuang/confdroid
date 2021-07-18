/**
 * Copyright (C) 2006 The Android Open Source Project
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


/* ---------- Recommended install location tests ---- */
/* TODO's
check version numbers for upgrades
check permissions of installed packages
how to do tests on updated system apps?
verify updates to system apps cannot be installed on the sdcard.
 */
public class PackageManagerTests extends android.test.AndroidTestCase {
    private static final boolean localLOGV = true;

    public static final java.lang.String TAG = "PackageManagerTests";

    public final long MAX_WAIT_TIME = 25 * 1000;

    public final long WAIT_TIME_INCR = 5 * 1000;

    private static final java.lang.String SECURE_CONTAINERS_PREFIX = "/mnt/asec";

    private static final int APP_INSTALL_AUTO = com.android.internal.content.PackageHelper.APP_INSTALL_AUTO;

    private static final int APP_INSTALL_DEVICE = com.android.internal.content.PackageHelper.APP_INSTALL_INTERNAL;

    private static final int APP_INSTALL_SDCARD = com.android.internal.content.PackageHelper.APP_INSTALL_EXTERNAL;

    private boolean mOrigState;

    void failStr(java.lang.String errMsg) {
        android.util.Log.w(android.content.pm.PackageManagerTests.TAG, "errMsg=" + errMsg);
        fail(errMsg);
    }

    void failStr(java.lang.Exception e) {
        failStr(e.getMessage());
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mOrigState = checkMediaState(Environment.MEDIA_MOUNTED);
        if (!mountMedia()) {
            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "sdcard not mounted? Some of these tests might fail");
        }
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        // Restore media state.
        boolean newState = checkMediaState(Environment.MEDIA_MOUNTED);
        if (newState != mOrigState) {
            if (mOrigState) {
                mountMedia();
            } else {
                unmountMedia();
            }
        }
        tearDown();
    }

    private class TestInstallObserver extends android.app.PackageInstallObserver {
        public int returnCode;

        private boolean doneFlag = false;

        @java.lang.Override
        public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) {
            android.util.Log.d(android.content.pm.PackageManagerTests.TAG, (((("onPackageInstalled: code=" + returnCode) + ", msg=") + msg) + ", extras=") + extras);
            synchronized(this) {
                this.returnCode = returnCode;
                doneFlag = true;
                notifyAll();
            }
        }

        public boolean isDone() {
            return doneFlag;
        }
    }

    abstract class GenericReceiver extends android.content.BroadcastReceiver {
        private boolean doneFlag = false;

        boolean received = false;

        android.content.Intent intent;

        android.content.IntentFilter filter;

        abstract boolean notifyNow(android.content.Intent intent);

        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (notifyNow(intent)) {
                synchronized(this) {
                    received = true;
                    doneFlag = true;
                    this.intent = intent;
                    notifyAll();
                }
            }
        }

        public boolean isDone() {
            return doneFlag;
        }

        public void setFilter(android.content.IntentFilter filter) {
            this.filter = filter;
        }
    }

    class InstallReceiver extends android.content.pm.PackageManagerTests.GenericReceiver {
        java.lang.String pkgName;

        InstallReceiver(java.lang.String pkgName) {
            this.pkgName = pkgName;
            android.content.IntentFilter filter = new android.content.IntentFilter(android.content.Intent.ACTION_PACKAGE_ADDED);
            filter.addDataScheme("package");
            super.setFilter(filter);
        }

        public boolean notifyNow(android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (!android.content.Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                return false;
            }
            android.net.Uri data = intent.getData();
            java.lang.String installedPkg = data.getEncodedSchemeSpecificPart();
            if (pkgName.equals(installedPkg)) {
                return true;
            }
            return false;
        }
    }

    private android.content.pm.PackageManager getPm() {
        return mContext.getPackageManager();
    }

    private android.content.pm.IPackageManager getIPm() {
        android.content.pm.IPackageManager ipm = IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        return ipm;
    }

    public void invokeInstallPackage(android.net.Uri packageURI, int flags, android.content.pm.PackageManagerTests.GenericReceiver receiver, boolean shouldSucceed) {
        android.content.pm.PackageManagerTests.TestInstallObserver observer = new android.content.pm.PackageManagerTests.TestInstallObserver();
        mContext.registerReceiver(receiver, receiver.filter);
        try {
            // Wait on observer
            synchronized(observer) {
                synchronized(receiver) {
                    getPm().installPackage(packageURI, observer, flags, null);
                    long waitTime = 0;
                    while ((!observer.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                        try {
                            observer.wait(WAIT_TIME_INCR);
                            waitTime += WAIT_TIME_INCR;
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Interrupted during sleep", e);
                        }
                    } 
                    if (!observer.isDone()) {
                        fail("Timed out waiting for packageInstalled callback");
                    }
                    if (shouldSucceed) {
                        if (observer.returnCode != android.content.pm.PackageManager.INSTALL_SUCCEEDED) {
                            fail("Package installation should have succeeded, but got code " + observer.returnCode);
                        }
                    } else {
                        if (observer.returnCode == android.content.pm.PackageManager.INSTALL_SUCCEEDED) {
                            fail("Package installation should fail");
                        }
                        /* We'll never expect get a notification since we
                        shouldn't succeed.
                         */
                        return;
                    }
                    // Verify we received the broadcast
                    waitTime = 0;
                    while ((!receiver.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                        try {
                            receiver.wait(WAIT_TIME_INCR);
                            waitTime += WAIT_TIME_INCR;
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Interrupted during sleep", e);
                        }
                    } 
                    if (!receiver.isDone()) {
                        fail("Timed out waiting for PACKAGE_ADDED notification");
                    }
                }
            }
        } finally {
            mContext.unregisterReceiver(receiver);
        }
    }

    public void invokeInstallPackageFail(android.net.Uri packageURI, int flags, int expectedResult) {
        android.content.pm.PackageManagerTests.TestInstallObserver observer = new android.content.pm.PackageManagerTests.TestInstallObserver();
        try {
            // Wait on observer
            synchronized(observer) {
                getPm().installPackage(packageURI, observer, flags, null);
                long waitTime = 0;
                while ((!observer.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                    try {
                        observer.wait(WAIT_TIME_INCR);
                        waitTime += WAIT_TIME_INCR;
                    } catch (java.lang.InterruptedException e) {
                        android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Interrupted during sleep", e);
                    }
                } 
                if (!observer.isDone()) {
                    fail("Timed out waiting for packageInstalled callback");
                }
                assertEquals(expectedResult, observer.returnCode);
            }
        } finally {
        }
    }

    android.net.Uri getInstallablePackage(int fileResId, java.io.File outFile) {
        android.content.res.Resources res = mContext.getResources();
        java.io.InputStream is = null;
        try {
            is = res.openRawResource(fileResId);
        } catch (android.content.res.Resources.NotFoundException e) {
            failStr("Failed to load resource with id: " + fileResId);
        }
        android.os.FileUtils.setPermissions(outFile.getPath(), (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IRWXO, -1, -1);
        assertTrue(android.os.FileUtils.copyToFile(is, outFile));
        android.os.FileUtils.setPermissions(outFile.getPath(), (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IRWXO, -1, -1);
        return android.net.Uri.fromFile(outFile);
    }

    private android.content.pm.PackageParser.Package parsePackage(android.net.Uri packageURI) throws android.content.pm.PackageParser.PackageParserException {
        final java.lang.String archiveFilePath = packageURI.getPath();
        android.content.pm.PackageParser packageParser = new android.content.pm.PackageParser();
        java.io.File sourceFile = new java.io.File(archiveFilePath);
        android.content.pm.PackageParser.Package pkg = packageParser.parseMonolithicPackage(sourceFile, 0);
        packageParser = null;
        return pkg;
    }

    private boolean checkSd(long pkgLen) {
        java.lang.String status = android.os.Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        long sdSize = -1;
        android.os.StatFs sdStats = new android.os.StatFs(android.os.Environment.getExternalStorageDirectory().getPath());
        sdSize = ((long) (sdStats.getAvailableBlocks())) * ((long) (sdStats.getBlockSize()));
        // TODO check for thresholds here
        return pkgLen <= sdSize;
    }

    private boolean checkInt(long pkgLen) {
        android.os.StatFs intStats = new android.os.StatFs(android.os.Environment.getDataDirectory().getPath());
        long intSize = ((long) (intStats.getBlockCount())) * ((long) (intStats.getBlockSize()));
        long iSize = ((long) (intStats.getAvailableBlocks())) * ((long) (intStats.getBlockSize()));
        // TODO check for thresholds here?
        return pkgLen <= iSize;
    }

    private static final int INSTALL_LOC_INT = 1;

    private static final int INSTALL_LOC_SD = 2;

    private static final int INSTALL_LOC_ERR = -1;

    private int getInstallLoc(int flags, int expInstallLocation, long pkgLen) {
        // Flags explicitly over ride everything else.
        if ((flags & android.content.pm.PackageManager.INSTALL_EXTERNAL) != 0) {
            return android.content.pm.PackageManagerTests.INSTALL_LOC_SD;
        } else
            if ((flags & android.content.pm.PackageManager.INSTALL_INTERNAL) != 0) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_INT;
            }

        // Manifest option takes precedence next
        if (expInstallLocation == android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL) {
            if (checkSd(pkgLen)) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_SD;
            }
            if (checkInt(pkgLen)) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_INT;
            }
            return android.content.pm.PackageManagerTests.INSTALL_LOC_ERR;
        }
        if (expInstallLocation == android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY) {
            if (checkInt(pkgLen)) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_INT;
            }
            return android.content.pm.PackageManagerTests.INSTALL_LOC_ERR;
        }
        if (expInstallLocation == android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO) {
            // Check for free memory internally
            if (checkInt(pkgLen)) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_INT;
            }
            // Check for free memory externally
            if (checkSd(pkgLen)) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_SD;
            }
            return android.content.pm.PackageManagerTests.INSTALL_LOC_ERR;
        }
        // Check for settings preference.
        boolean checkSd = false;
        int userPref = getDefaultInstallLoc();
        if (userPref == android.content.pm.PackageManagerTests.APP_INSTALL_DEVICE) {
            if (checkInt(pkgLen)) {
                return android.content.pm.PackageManagerTests.INSTALL_LOC_INT;
            }
            return android.content.pm.PackageManagerTests.INSTALL_LOC_ERR;
        } else
            if (userPref == android.content.pm.PackageManagerTests.APP_INSTALL_SDCARD) {
                if (checkSd(pkgLen)) {
                    return android.content.pm.PackageManagerTests.INSTALL_LOC_SD;
                }
                return android.content.pm.PackageManagerTests.INSTALL_LOC_ERR;
            }

        // Default system policy for apps with no manifest option specified.
        // Check for free memory internally
        if (checkInt(pkgLen)) {
            return android.content.pm.PackageManagerTests.INSTALL_LOC_INT;
        }
        return android.content.pm.PackageManagerTests.INSTALL_LOC_ERR;
    }

    private void assertInstall(android.content.pm.PackageParser.Package pkg, int flags, int expInstallLocation) {
        try {
            java.lang.String pkgName = pkg.packageName;
            android.content.pm.ApplicationInfo info = getPm().getApplicationInfo(pkgName, 0);
            assertNotNull(info);
            assertEquals(pkgName, info.packageName);
            java.io.File dataDir = android.os.Environment.getDataDirectory();
            java.lang.String appInstallPath = new java.io.File(dataDir, "app").getPath();
            java.lang.String drmInstallPath = new java.io.File(dataDir, "app-private").getPath();
            java.io.File srcDir = new java.io.File(info.sourceDir);
            java.lang.String srcPath = srcDir.getParentFile().getParent();
            java.io.File publicSrcDir = new java.io.File(info.publicSourceDir);
            java.lang.String publicSrcPath = publicSrcDir.getParentFile().getParent();
            long pkgLen = new java.io.File(info.sourceDir).length();
            java.lang.String expectedLibPath = new java.io.File(new java.io.File(info.sourceDir).getParentFile(), "lib").getPath();
            int rLoc = getInstallLoc(flags, expInstallLocation, pkgLen);
            if (rLoc == android.content.pm.PackageManagerTests.INSTALL_LOC_INT) {
                if ((flags & android.content.pm.PackageManager.INSTALL_FORWARD_LOCK) != 0) {
                    assertTrue("The application should be installed forward locked", (info.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_FORWARD_LOCK) != 0);
                    android.content.pm.PackageManagerTests.assertStartsWith("The APK path should point to the ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, srcPath);
                    android.content.pm.PackageManagerTests.assertStartsWith("The public APK path should point to the ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, publicSrcPath);
                    android.content.pm.PackageManagerTests.assertStartsWith("The native library path should point to the ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, info.nativeLibraryDir);
                    try {
                        java.lang.String compatLib = new java.io.File(info.dataDir + "/lib").getCanonicalPath();
                        assertEquals("The compatibility lib directory should be a symbolic link to " + info.nativeLibraryDir, info.nativeLibraryDir, compatLib);
                    } catch (java.io.IOException e) {
                        fail(("compat check: Can't read " + info.dataDir) + "/lib");
                    }
                } else {
                    assertFalse((info.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_FORWARD_LOCK) != 0);
                    assertEquals(appInstallPath, srcPath);
                    assertEquals(appInstallPath, publicSrcPath);
                    android.content.pm.PackageManagerTests.assertStartsWith("Native library should point to shared lib directory", expectedLibPath, info.nativeLibraryDir);
                    assertDirOwnerGroupPermsIfExists("Native library directory should be owned by system:system and 0755", SYSTEM_UID, SYSTEM_UID, (((S_IRWXU | S_IRGRP) | S_IXGRP) | S_IROTH) | S_IXOTH, info.nativeLibraryDir);
                }
                assertFalse((info.flags & android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0);
                // Make sure the native library dir is not a symlink
                final java.io.File nativeLibDir = new java.io.File(info.nativeLibraryDir);
                if (nativeLibDir.exists()) {
                    try {
                        assertEquals("Native library dir should not be a symlink", info.nativeLibraryDir, nativeLibDir.getCanonicalPath());
                    } catch (java.io.IOException e) {
                        fail("Can't read " + nativeLibDir.getPath());
                    }
                }
            } else
                if (rLoc == android.content.pm.PackageManagerTests.INSTALL_LOC_SD) {
                    if ((flags & android.content.pm.PackageManager.INSTALL_FORWARD_LOCK) != 0) {
                        assertTrue("The application should be installed forward locked", (info.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_FORWARD_LOCK) != 0);
                    } else {
                        assertFalse("The application should not be installed forward locked", (info.privateFlags & android.content.pm.ApplicationInfo.PRIVATE_FLAG_FORWARD_LOCK) != 0);
                    }
                    assertTrue(("Application flags (" + info.flags) + ") should contain FLAG_EXTERNAL_STORAGE", (info.flags & android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0);
                    // Might need to check:
                    // ((info.privateFlags & ApplicationInfo.PRIVATE_FLAG_FORWARD_LOCK) != 0)
                    android.content.pm.PackageManagerTests.assertStartsWith("The APK path should point to the ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, srcPath);
                    android.content.pm.PackageManagerTests.assertStartsWith("The public APK path should point to the ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, publicSrcPath);
                    android.content.pm.PackageManagerTests.assertStartsWith("The native library path should point to the ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, info.nativeLibraryDir);
                    // Make sure the native library in /data/data/<app>/lib is a
                    // symlink to the ASEC
                    final java.io.File nativeLibSymLink = new java.io.File(info.dataDir, "lib");
                    assertTrue("Native library symlink should exist at " + nativeLibSymLink.getPath(), nativeLibSymLink.exists());
                    try {
                        assertEquals((nativeLibSymLink.getPath() + " should be a symlink to ") + info.nativeLibraryDir, info.nativeLibraryDir, nativeLibSymLink.getCanonicalPath());
                    } catch (java.io.IOException e) {
                        fail("Can't read " + nativeLibSymLink.getPath());
                    }
                } else {
                    // TODO handle error. Install should have failed.
                    fail("Install should have failed");
                }

        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            failStr("failed with exception : " + e);
        }
    }

    private void assertDirOwnerGroupPermsIfExists(java.lang.String reason, int uid, int gid, int perms, java.lang.String path) {
        if (!new java.io.File(path).exists()) {
            return;
        }
        final android.system.StructStat stat;
        try {
            stat = android.system.Os.lstat(path);
        } catch (android.system.ErrnoException e) {
            throw new java.lang.AssertionError((((reason + "\n") + "Got: ") + path) + " does not exist");
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (!S_ISDIR(stat.st_mode)) {
            sb.append("\nExpected type: ");
            sb.append(android.content.pm.S_IFDIR);
            sb.append("\ngot type: ");
            sb.append(stat.st_mode & S_IFMT);
        }
        if (stat.st_uid != uid) {
            sb.append("\nExpected owner: ");
            sb.append(uid);
            sb.append("\nGot owner: ");
            sb.append(stat.st_uid);
        }
        if (stat.st_gid != gid) {
            sb.append("\nExpected group: ");
            sb.append(gid);
            sb.append("\nGot group: ");
            sb.append(stat.st_gid);
        }
        if ((stat.st_mode & (~S_IFMT)) != perms) {
            sb.append("\nExpected permissions: ");
            sb.append(java.lang.Integer.toOctalString(perms));
            sb.append("\nGot permissions: ");
            sb.append(java.lang.Integer.toOctalString(stat.st_mode & (~S_IFMT)));
        }
        if (sb.length() > 0) {
            throw new java.lang.AssertionError(reason + sb.toString());
        }
    }

    private static void assertStartsWith(java.lang.String prefix, java.lang.String actual) {
        android.content.pm.PackageManagerTests.assertStartsWith("", prefix, actual);
    }

    private static void assertStartsWith(java.lang.String description, java.lang.String prefix, java.lang.String actual) {
        if (!actual.startsWith(prefix)) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(description);
            sb.append("\nExpected prefix: ");
            sb.append(prefix);
            sb.append("\n     got: ");
            sb.append(actual);
            sb.append('\n');
            throw new java.lang.AssertionError(sb.toString());
        }
    }

    private void assertNotInstalled(java.lang.String pkgName) {
        try {
            android.content.pm.ApplicationInfo info = getPm().getApplicationInfo(pkgName, 0);
            fail(pkgName + " shouldnt be installed");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    class InstallParams {
        android.net.Uri packageURI;

        android.content.pm.PackageParser.Package pkg;

        InstallParams(java.lang.String outFileName, int rawResId) throws android.content.pm.PackageParser.PackageParserException {
            this.pkg = getParsedPackage(outFileName, rawResId);
            this.packageURI = android.net.Uri.fromFile(new java.io.File(pkg.codePath));
        }

        InstallParams(android.content.pm.PackageParser.Package pkg) {
            this.packageURI = android.net.Uri.fromFile(new java.io.File(pkg.codePath));
            this.pkg = pkg;
        }

        long getApkSize() {
            java.io.File file = new java.io.File(pkg.codePath);
            return file.length();
        }
    }

    private android.content.pm.PackageManagerTests.InstallParams sampleInstallFromRawResource(int flags, boolean cleanUp) throws java.lang.Exception {
        return installFromRawResource("install.apk", R.raw.install, flags, cleanUp, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    static final java.lang.String PERM_PACKAGE = "package";

    static final java.lang.String PERM_DEFINED = "defined";

    static final java.lang.String PERM_UNDEFINED = "undefined";

    static final java.lang.String PERM_USED = "used";

    static final java.lang.String PERM_NOTUSED = "notused";

    private void assertPermissions(java.lang.String[] cmds) {
        final android.content.pm.PackageManager pm = getPm();
        java.lang.String pkg = null;
        android.content.pm.PackageInfo pkgInfo = null;
        java.lang.String mode = android.content.pm.PackageManagerTests.PERM_DEFINED;
        int i = 0;
        while (i < cmds.length) {
            java.lang.String cmd = cmds[i++];
            if (cmd == android.content.pm.PackageManagerTests.PERM_PACKAGE) {
                pkg = cmds[i++];
                try {
                    pkgInfo = pm.getPackageInfo(pkg, android.content.pm.PackageManager.GET_PERMISSIONS | android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    pkgInfo = null;
                }
            } else
                if ((((cmd == android.content.pm.PackageManagerTests.PERM_DEFINED) || (cmd == android.content.pm.PackageManagerTests.PERM_UNDEFINED)) || (cmd == android.content.pm.PackageManagerTests.PERM_USED)) || (cmd == android.content.pm.PackageManagerTests.PERM_NOTUSED)) {
                    mode = cmds[i++];
                } else {
                    if (mode == android.content.pm.PackageManagerTests.PERM_DEFINED) {
                        try {
                            android.content.pm.PermissionInfo pi = pm.getPermissionInfo(cmd, 0);
                            assertNotNull(pi);
                            assertEquals(pi.packageName, pkg);
                            assertEquals(pi.name, cmd);
                            assertNotNull(pkgInfo);
                            boolean found = false;
                            for (int j = 0; (j < pkgInfo.permissions.length) && (!found); j++) {
                                if (pkgInfo.permissions[j].name.equals(cmd)) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                fail("Permission not found: " + cmd);
                            }
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            throw new java.lang.RuntimeException(e);
                        }
                    } else
                        if (mode == android.content.pm.PackageManagerTests.PERM_UNDEFINED) {
                            try {
                                pm.getPermissionInfo(cmd, 0);
                                throw new java.lang.RuntimeException("Permission exists: " + cmd);
                            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            }
                            if (pkgInfo != null) {
                                boolean found = false;
                                for (int j = 0; (j < pkgInfo.permissions.length) && (!found); j++) {
                                    if (pkgInfo.permissions[j].name.equals(cmd)) {
                                        found = true;
                                    }
                                }
                                if (found) {
                                    fail("Permission still exists: " + cmd);
                                }
                            }
                        } else
                            if ((mode == android.content.pm.PackageManagerTests.PERM_USED) || (mode == android.content.pm.PackageManagerTests.PERM_NOTUSED)) {
                                boolean found = false;
                                for (int j = 0; (j < pkgInfo.requestedPermissions.length) && (!found); j++) {
                                    if (pkgInfo.requestedPermissions[j].equals(cmd)) {
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    fail("Permission not requested: " + cmd);
                                }
                                if (mode == android.content.pm.PackageManagerTests.PERM_USED) {
                                    if (pm.checkPermission(cmd, pkg) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                        fail("Permission not granted: " + cmd);
                                    }
                                } else {
                                    if (pm.checkPermission(cmd, pkg) != android.content.pm.PackageManager.PERMISSION_DENIED) {
                                        fail("Permission granted: " + cmd);
                                    }
                                }
                            }


                }

        } 
    }

    private android.content.pm.PackageParser.Package getParsedPackage(java.lang.String outFileName, int rawResId) throws android.content.pm.PackageParser.PackageParserException {
        android.content.pm.PackageManager pm = mContext.getPackageManager();
        java.io.File filesDir = mContext.getFilesDir();
        java.io.File outFile = new java.io.File(filesDir, outFileName);
        android.net.Uri packageURI = getInstallablePackage(rawResId, outFile);
        android.content.pm.PackageParser.Package pkg = parsePackage(packageURI);
        return pkg;
    }

    /* Utility function that reads a apk bundled as a raw resource
    copies it into own data directory and invokes
    PackageManager api to install it.
     */
    private void installFromRawResource(android.content.pm.PackageManagerTests.InstallParams ip, int flags, boolean cleanUp, boolean fail, int result, int expInstallLocation) throws java.lang.Exception {
        android.content.pm.PackageManager pm = mContext.getPackageManager();
        android.content.pm.PackageParser.Package pkg = ip.pkg;
        android.net.Uri packageURI = ip.packageURI;
        if ((flags & android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING) == 0) {
            // Make sure the package doesn't exist
            try {
                android.content.pm.ApplicationInfo appInfo = pm.getApplicationInfo(pkg.packageName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
                android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.DeleteReceiver(pkg.packageName);
                invokeDeletePackage(pkg.packageName, 0, receiver);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        try {
            if (fail) {
                invokeInstallPackageFail(packageURI, flags, result);
                if ((flags & android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING) == 0) {
                    assertNotInstalled(pkg.packageName);
                }
            } else {
                android.content.pm.PackageManagerTests.InstallReceiver receiver = new android.content.pm.PackageManagerTests.InstallReceiver(pkg.packageName);
                invokeInstallPackage(packageURI, flags, receiver, true);
                // Verify installed information
                assertInstall(pkg, flags, expInstallLocation);
            }
        } finally {
            if (cleanUp) {
                cleanUpInstall(ip);
            }
        }
    }

    /* Utility function that reads a apk bundled as a raw resource
    copies it into own data directory and invokes
    PackageManager api to install it.
     */
    private android.content.pm.PackageManagerTests.InstallParams installFromRawResource(java.lang.String outFileName, int rawResId, int flags, boolean cleanUp, boolean fail, int result, int expInstallLocation) throws java.lang.Exception {
        android.content.pm.PackageManagerTests.InstallParams ip = new android.content.pm.PackageManagerTests.InstallParams(outFileName, rawResId);
        installFromRawResource(ip, flags, cleanUp, fail, result, expInstallLocation);
        return ip;
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallNormalInternal() throws java.lang.Exception {
        sampleInstallFromRawResource(0, true);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallFwdLockedInternal() throws java.lang.Exception {
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, true);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        mountMedia();
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_EXTERNAL, true);
    }

    /* ------------------------- Test replacing packages -------------- */
    class ReplaceReceiver extends android.content.pm.PackageManagerTests.GenericReceiver {
        java.lang.String pkgName;

        static final int INVALID = -1;

        static final int REMOVED = 1;

        static final int ADDED = 2;

        static final int REPLACED = 3;

        int removed = android.content.pm.PackageManagerTests.ReplaceReceiver.INVALID;

        // for updated system apps only
        boolean update = false;

        ReplaceReceiver(java.lang.String pkgName) {
            this.pkgName = pkgName;
            filter = new android.content.IntentFilter(android.content.Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(android.content.Intent.ACTION_PACKAGE_ADDED);
            if (update) {
                filter.addAction(android.content.Intent.ACTION_PACKAGE_REPLACED);
            }
            filter.addDataScheme("package");
            super.setFilter(filter);
        }

        public boolean notifyNow(android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            android.net.Uri data = intent.getData();
            java.lang.String installedPkg = data.getEncodedSchemeSpecificPart();
            if ((pkgName == null) || (!pkgName.equals(installedPkg))) {
                return false;
            }
            if (android.content.Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                removed = android.content.pm.PackageManagerTests.ReplaceReceiver.REMOVED;
            } else
                if (android.content.Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    if (removed != android.content.pm.PackageManagerTests.ReplaceReceiver.REMOVED) {
                        return false;
                    }
                    boolean replacing = intent.getBooleanExtra(android.content.Intent.EXTRA_REPLACING, false);
                    if (!replacing) {
                        return false;
                    }
                    removed = android.content.pm.PackageManagerTests.ReplaceReceiver.ADDED;
                    if (!update) {
                        return true;
                    }
                } else
                    if (android.content.Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
                        if (removed != android.content.pm.PackageManagerTests.ReplaceReceiver.ADDED) {
                            return false;
                        }
                        removed = android.content.pm.PackageManagerTests.ReplaceReceiver.REPLACED;
                        return true;
                    }


            return false;
        }
    }

    /* Utility function that reads a apk bundled as a raw resource
    copies it into own data directory and invokes
    PackageManager api to install first and then replace it
    again.
     */
    private void sampleReplaceFromRawResource(int flags) throws java.lang.Exception {
        android.content.pm.PackageManagerTests.InstallParams ip = sampleInstallFromRawResource(flags, false);
        boolean replace = (flags & android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING) != 0;
        android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "replace=" + replace);
        android.content.pm.PackageManagerTests.GenericReceiver receiver;
        if (replace) {
            receiver = new android.content.pm.PackageManagerTests.ReplaceReceiver(ip.pkg.packageName);
            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Creating replaceReceiver");
        } else {
            receiver = new android.content.pm.PackageManagerTests.InstallReceiver(ip.pkg.packageName);
        }
        try {
            invokeInstallPackage(ip.packageURI, flags, receiver, replace);
            if (replace) {
                assertInstall(ip.pkg, flags, ip.pkg.installLocation);
            }
        } finally {
            cleanUpInstall(ip);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFailNormalInternal() throws java.lang.Exception {
        sampleReplaceFromRawResource(0);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFailFwdLockedInternal() throws java.lang.Exception {
        sampleReplaceFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFailSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        sampleReplaceFromRawResource(android.content.pm.PackageManager.INSTALL_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceNormalInternal() throws java.lang.Exception {
        sampleReplaceFromRawResource(android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFwdLockedInternal() throws java.lang.Exception {
        sampleReplaceFromRawResource(android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING | android.content.pm.PackageManager.INSTALL_FORWARD_LOCK);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        sampleReplaceFromRawResource(android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING | android.content.pm.PackageManager.INSTALL_EXTERNAL);
    }

    /* -------------- Delete tests --- */
    private static class DeleteObserver extends android.content.pm.IPackageDeleteObserver.Stub {
        private java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);

        private int mReturnCode;

        private final java.lang.String mPackageName;

        private java.lang.String mObservedPackage;

        public DeleteObserver(java.lang.String packageName) {
            mPackageName = packageName;
        }

        public boolean isSuccessful() {
            return mReturnCode == android.content.pm.PackageManager.DELETE_SUCCEEDED;
        }

        public void packageDeleted(java.lang.String packageName, int returnCode) throws android.os.RemoteException {
            mObservedPackage = packageName;
            mReturnCode = returnCode;
            mLatch.countDown();
        }

        public void waitForCompletion(long timeoutMillis) {
            final long deadline = android.os.SystemClock.uptimeMillis() + timeoutMillis;
            long waitTime = timeoutMillis;
            while (waitTime > 0) {
                try {
                    boolean done = mLatch.await(waitTime, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (done) {
                        assertEquals(mPackageName, mObservedPackage);
                        return;
                    }
                } catch (java.lang.InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                waitTime = deadline - android.os.SystemClock.uptimeMillis();
            } 
            throw new java.lang.AssertionError("Timeout waiting for package deletion");
        }
    }

    class DeleteReceiver extends android.content.pm.PackageManagerTests.GenericReceiver {
        java.lang.String pkgName;

        DeleteReceiver(java.lang.String pkgName) {
            this.pkgName = pkgName;
            android.content.IntentFilter filter = new android.content.IntentFilter(android.content.Intent.ACTION_PACKAGE_REMOVED);
            filter.addDataScheme("package");
            super.setFilter(filter);
        }

        public boolean notifyNow(android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (!android.content.Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                return false;
            }
            android.net.Uri data = intent.getData();
            java.lang.String installedPkg = data.getEncodedSchemeSpecificPart();
            if (pkgName.equals(installedPkg)) {
                return true;
            }
            return false;
        }
    }

    public boolean invokeDeletePackage(final java.lang.String pkgName, int flags, android.content.pm.PackageManagerTests.GenericReceiver receiver) throws java.lang.Exception {
        android.content.pm.ApplicationInfo info = getPm().getApplicationInfo(pkgName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        mContext.registerReceiver(receiver, receiver.filter);
        try {
            android.content.pm.PackageManagerTests.DeleteObserver observer = new android.content.pm.PackageManagerTests.DeleteObserver(pkgName);
            getPm().deletePackage(pkgName, observer, flags | android.content.pm.PackageManager.DELETE_ALL_USERS);
            observer.waitForCompletion(MAX_WAIT_TIME);
            android.content.pm.PackageManagerTests.assertUninstalled(info);
            // Verify we received the broadcast
            // TODO replace this with a CountDownLatch
            synchronized(receiver) {
                long waitTime = 0;
                while ((!receiver.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                    receiver.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!receiver.isDone()) {
                    throw new java.lang.Exception("Timed out waiting for PACKAGE_REMOVED notification");
                }
            }
            return receiver.received;
        } finally {
            mContext.unregisterReceiver(receiver);
        }
    }

    private static void assertUninstalled(android.content.pm.ApplicationInfo info) throws java.lang.Exception {
        java.io.File nativeLibraryFile = new java.io.File(info.nativeLibraryDir);
        assertFalse("Native library directory should be erased", nativeLibraryFile.exists());
    }

    public void deleteFromRawResource(int iFlags, int dFlags) throws java.lang.Exception {
        android.content.pm.PackageManagerTests.InstallParams ip = sampleInstallFromRawResource(iFlags, false);
        boolean retainData = (dFlags & android.content.pm.PackageManager.DELETE_KEEP_DATA) != 0;
        android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.DeleteReceiver(ip.pkg.packageName);
        try {
            assertTrue(invokeDeletePackage(ip.pkg.packageName, dFlags, receiver));
            android.content.pm.ApplicationInfo info = null;
            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "okay4");
            try {
                info = getPm().getApplicationInfo(ip.pkg.packageName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                info = null;
            }
            if (retainData) {
                assertNotNull(info);
                assertEquals(info.packageName, ip.pkg.packageName);
                java.io.File file = new java.io.File(info.dataDir);
                assertTrue(file.exists());
            } else {
                assertNull(info);
            }
        } catch (java.lang.Exception e) {
            failStr(e);
        } finally {
            cleanUpInstall(ip);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteNormalInternal() throws java.lang.Exception {
        deleteFromRawResource(0, 0);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteFwdLockedInternal() throws java.lang.Exception {
        deleteFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, 0);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        deleteFromRawResource(android.content.pm.PackageManager.INSTALL_EXTERNAL, 0);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteNormalInternalRetainData() throws java.lang.Exception {
        deleteFromRawResource(0, android.content.pm.PackageManager.DELETE_KEEP_DATA);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteFwdLockedInternalRetainData() throws java.lang.Exception {
        deleteFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, android.content.pm.PackageManager.DELETE_KEEP_DATA);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteSdcardRetainData() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        deleteFromRawResource(android.content.pm.PackageManager.INSTALL_EXTERNAL, android.content.pm.PackageManager.DELETE_KEEP_DATA);
    }

    /* sdcard mount/unmount tests ***** */
    class SdMountReceiver extends android.content.pm.PackageManagerTests.GenericReceiver {
        java.lang.String[] pkgNames;

        boolean status = true;

        SdMountReceiver(java.lang.String[] pkgNames) {
            this.pkgNames = pkgNames;
            android.content.IntentFilter filter = new android.content.IntentFilter(android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            super.setFilter(filter);
        }

        public boolean notifyNow(android.content.Intent intent) {
            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "okay 1");
            java.lang.String action = intent.getAction();
            if (!android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) {
                return false;
            }
            java.lang.String[] rpkgList = intent.getStringArrayExtra(android.content.Intent.EXTRA_CHANGED_PACKAGE_LIST);
            for (java.lang.String pkg : pkgNames) {
                boolean found = false;
                for (java.lang.String rpkg : rpkgList) {
                    if (rpkg.equals(pkg)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    status = false;
                    return true;
                }
            }
            return true;
        }
    }

    class SdUnMountReceiver extends android.content.pm.PackageManagerTests.GenericReceiver {
        java.lang.String[] pkgNames;

        boolean status = true;

        SdUnMountReceiver(java.lang.String[] pkgNames) {
            this.pkgNames = pkgNames;
            android.content.IntentFilter filter = new android.content.IntentFilter(android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            super.setFilter(filter);
        }

        public boolean notifyNow(android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (!android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
                return false;
            }
            java.lang.String[] rpkgList = intent.getStringArrayExtra(android.content.Intent.EXTRA_CHANGED_PACKAGE_LIST);
            for (java.lang.String pkg : pkgNames) {
                boolean found = false;
                for (java.lang.String rpkg : rpkgList) {
                    if (rpkg.equals(pkg)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    status = false;
                    return true;
                }
            }
            return true;
        }
    }

    android.os.storage.IMountService getMs() {
        android.os.IBinder service = android.os.ServiceManager.getService("mount");
        if (service != null) {
            return IMountService.Stub.asInterface(service);
        } else {
            android.util.Log.e(android.content.pm.PackageManagerTests.TAG, "Can't get mount service");
        }
        return null;
    }

    boolean checkMediaState(java.lang.String desired) {
        try {
            java.lang.String mPath = android.os.Environment.getExternalStorageDirectory().getPath();
            java.lang.String actual = getMs().getVolumeState(mPath);
            if (desired.equals(actual)) {
                return true;
            } else {
                return false;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.content.pm.PackageManagerTests.TAG, "Exception while checking media state", e);
            return false;
        }
    }

    boolean mountMedia() {
        // We can't mount emulated storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return true;
        }
        if (checkMediaState(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        final java.lang.String path = android.os.Environment.getExternalStorageDirectory().toString();
        android.os.storage.StorageListener observer = new android.os.storage.StorageListener(android.os.Environment.MEDIA_MOUNTED);
        android.os.storage.StorageManager sm = ((android.os.storage.StorageManager) (mContext.getSystemService(android.content.Context.STORAGE_SERVICE)));
        sm.registerListener(observer);
        try {
            // Wait on observer
            synchronized(observer) {
                int ret = getMs().mountVolume(path);
                if (ret != android.os.storage.StorageResultCode.OperationSucceeded) {
                    throw new java.lang.Exception("Could not mount the media");
                }
                long waitTime = 0;
                while ((!observer.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                    observer.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!observer.isDone()) {
                    throw new java.lang.Exception("Timed out waiting for unmount media notification");
                }
                return true;
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.content.pm.PackageManagerTests.TAG, "Exception : " + e);
            return false;
        } finally {
            sm.unregisterListener(observer);
        }
    }

    private boolean unmountMedia() {
        // We can't unmount emulated storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return true;
        }
        if (checkMediaState(Environment.MEDIA_UNMOUNTED)) {
            return true;
        }
        final java.lang.String path = android.os.Environment.getExternalStorageDirectory().getPath();
        android.os.storage.StorageListener observer = new android.os.storage.StorageListener(android.os.Environment.MEDIA_UNMOUNTED);
        android.os.storage.StorageManager sm = ((android.os.storage.StorageManager) (mContext.getSystemService(android.content.Context.STORAGE_SERVICE)));
        sm.registerListener(observer);
        try {
            // Wait on observer
            synchronized(observer) {
                getMs().unmountVolume(path, true, false);
                long waitTime = 0;
                while ((!observer.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                    observer.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!observer.isDone()) {
                    throw new java.lang.Exception("Timed out waiting for unmount media notification");
                }
                return true;
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.content.pm.PackageManagerTests.TAG, "Exception : " + e);
            return false;
        } finally {
            sm.unregisterListener(observer);
        }
    }

    private boolean mountFromRawResource() throws java.lang.Exception {
        // Install pkg on sdcard
        android.content.pm.PackageManagerTests.InstallParams ip = sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_EXTERNAL, false);
        if (android.content.pm.PackageManagerTests.localLOGV)
            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Installed pkg on sdcard");

        boolean origState = checkMediaState(Environment.MEDIA_MOUNTED);
        boolean registeredReceiver = false;
        android.content.pm.PackageManagerTests.SdMountReceiver receiver = new android.content.pm.PackageManagerTests.SdMountReceiver(new java.lang.String[]{ ip.pkg.packageName });
        try {
            if (android.content.pm.PackageManagerTests.localLOGV)
                android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Unmounting media");

            // Unmount media
            assertTrue(unmountMedia());
            if (android.content.pm.PackageManagerTests.localLOGV)
                android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Unmounted media");

            // Register receiver here
            android.content.pm.PackageManager pm = getPm();
            mContext.registerReceiver(receiver, receiver.filter);
            registeredReceiver = true;
            // Wait on receiver
            synchronized(receiver) {
                if (android.content.pm.PackageManagerTests.localLOGV)
                    android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Mounting media");

                // Mount media again
                assertTrue(mountMedia());
                if (android.content.pm.PackageManagerTests.localLOGV)
                    android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Mounted media");

                if (android.content.pm.PackageManagerTests.localLOGV)
                    android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Waiting for notification");

                long waitTime = 0;
                // Verify we received the broadcast
                waitTime = 0;
                while ((!receiver.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                    receiver.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!receiver.isDone()) {
                    failStr("Timed out waiting for EXTERNAL_APPLICATIONS notification");
                }
                return receiver.received;
            }
        } catch (java.lang.InterruptedException e) {
            failStr(e);
            return false;
        } finally {
            if (registeredReceiver) {
                mContext.unregisterReceiver(receiver);
            }
            // Restore original media state
            if (origState) {
                mountMedia();
            } else {
                unmountMedia();
            }
            if (android.content.pm.PackageManagerTests.localLOGV)
                android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Cleaning up install");

            cleanUpInstall(ip);
        }
    }

    /* Install package on sdcard. Unmount and then mount the media.
    (Use PackageManagerService private api for now)
    Make sure the installed package is available.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testMountSdNormalInternal() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        assertTrue(mountFromRawResource());
    }

    void cleanUpInstall(android.content.pm.PackageManagerTests.InstallParams ip) throws java.lang.Exception {
        if (ip == null) {
            return;
        }
        java.lang.Runtime.getRuntime().gc();
        final java.lang.String packageName = ip.pkg.packageName;
        android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Deleting package : " + packageName);
        android.content.pm.ApplicationInfo info = null;
        try {
            info = getPm().getApplicationInfo(packageName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (android.content.pm.PackageManager.NameNotFoundException ignored) {
        }
        android.content.pm.PackageManagerTests.DeleteObserver observer = new android.content.pm.PackageManagerTests.DeleteObserver(packageName);
        getPm().deletePackage(packageName, observer, android.content.pm.PackageManager.DELETE_ALL_USERS);
        observer.waitForCompletion(MAX_WAIT_TIME);
        try {
            if (info != null) {
                android.content.pm.PackageManagerTests.assertUninstalled(info);
            }
        } finally {
            java.io.File outFile = new java.io.File(ip.pkg.codePath);
            if ((outFile != null) && outFile.exists()) {
                outFile.delete();
            }
        }
    }

    private void cleanUpInstall(java.lang.String pkgName) throws java.lang.Exception {
        if (pkgName == null) {
            return;
        }
        android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "Deleting package : " + pkgName);
        try {
            android.content.pm.ApplicationInfo info = getPm().getApplicationInfo(pkgName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null) {
                android.content.pm.PackageManagerTests.DeleteObserver observer = new android.content.pm.PackageManagerTests.DeleteObserver(pkgName);
                getPm().deletePackage(pkgName, observer, android.content.pm.PackageManager.DELETE_ALL_USERS);
                observer.waitForCompletion(MAX_WAIT_TIME);
                android.content.pm.PackageManagerTests.assertUninstalled(info);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationInternal() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_internal, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationAuto() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_auto, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationUnspecified() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_unspecified, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationFwdLockedFlagSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_unspecified, android.content.pm.PackageManager.INSTALL_FORWARD_LOCK | android.content.pm.PackageManager.INSTALL_EXTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationFwdLockedSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* Install a package on internal flash via PackageManager install flag. Replace
    the package via flag to install on sdcard. Make sure the new flag overrides
    the old install location.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFlagInternalSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = 0;
        int rFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        android.content.pm.PackageManagerTests.InstallParams ip = sampleInstallFromRawResource(iFlags, false);
        android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.ReplaceReceiver(ip.pkg.packageName);
        int replaceFlags = rFlags | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        try {
            invokeInstallPackage(ip.packageURI, replaceFlags, receiver, true);
            assertInstall(ip.pkg, rFlags, ip.pkg.installLocation);
        } catch (java.lang.Exception e) {
            failStr("Failed with exception : " + e);
        } finally {
            cleanUpInstall(ip);
        }
    }

    /* Install a package on sdcard via PackageManager install flag. Replace
    the package with no flags or manifest option and make sure the old
    install location is retained.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFlagSdcardInternal() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = 0;
        android.content.pm.PackageManagerTests.InstallParams ip = sampleInstallFromRawResource(iFlags, false);
        android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.ReplaceReceiver(ip.pkg.packageName);
        int replaceFlags = rFlags | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        try {
            invokeInstallPackage(ip.packageURI, replaceFlags, receiver, true);
            assertInstall(ip.pkg, iFlags, ip.pkg.installLocation);
        } catch (java.lang.Exception e) {
            failStr("Failed with exception : " + e);
        } finally {
            cleanUpInstall(ip);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationReplaceInternalSdcard() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = 0;
        int iApk = R.raw.install_loc_internal;
        int rFlags = 0;
        int rApk = R.raw.install_loc_sdcard;
        android.content.pm.PackageManagerTests.InstallParams ip = installFromRawResource("install.apk", iApk, iFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
        android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.ReplaceReceiver(ip.pkg.packageName);
        int replaceFlags = rFlags | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        try {
            android.content.pm.PackageManagerTests.InstallParams rp = installFromRawResource("install.apk", rApk, replaceFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
            assertInstall(rp.pkg, replaceFlags, rp.pkg.installLocation);
        } catch (java.lang.Exception e) {
            failStr("Failed with exception : " + e);
        } finally {
            cleanUpInstall(ip);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestInstallLocationReplaceSdcardInternal() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = 0;
        int iApk = R.raw.install_loc_sdcard;
        int rFlags = 0;
        int rApk = R.raw.install_loc_unspecified;
        android.content.pm.PackageManagerTests.InstallParams ip = installFromRawResource("install.apk", iApk, iFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
        int replaceFlags = rFlags | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        try {
            android.content.pm.PackageManagerTests.InstallParams rp = installFromRawResource("install.apk", rApk, replaceFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
            assertInstall(rp.pkg, replaceFlags, ip.pkg.installLocation);
        } catch (java.lang.Exception e) {
            failStr("Failed with exception : " + e);
        } finally {
            cleanUpInstall(ip);
        }
    }

    class MoveReceiver extends android.content.pm.PackageManagerTests.GenericReceiver {
        java.lang.String pkgName;

        static final int INVALID = -1;

        static final int REMOVED = 1;

        static final int ADDED = 2;

        int removed = android.content.pm.PackageManagerTests.MoveReceiver.INVALID;

        MoveReceiver(java.lang.String pkgName) {
            this.pkgName = pkgName;
            filter = new android.content.IntentFilter(android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            filter.addAction(android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            super.setFilter(filter);
        }

        public boolean notifyNow(android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            android.util.Log.i(android.content.pm.PackageManagerTests.TAG, "MoveReceiver::" + action);
            if (android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
                java.lang.String[] list = intent.getStringArrayExtra(android.content.Intent.EXTRA_CHANGED_PACKAGE_LIST);
                if (list != null) {
                    for (java.lang.String pkg : list) {
                        if (pkg.equals(pkgName)) {
                            removed = android.content.pm.PackageManagerTests.MoveReceiver.REMOVED;
                            break;
                        }
                    }
                }
                removed = android.content.pm.PackageManagerTests.MoveReceiver.REMOVED;
            } else
                if (android.content.Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) {
                    if (removed != android.content.pm.PackageManagerTests.MoveReceiver.REMOVED) {
                        return false;
                    }
                    java.lang.String[] list = intent.getStringArrayExtra(android.content.Intent.EXTRA_CHANGED_PACKAGE_LIST);
                    if (list != null) {
                        for (java.lang.String pkg : list) {
                            if (pkg.equals(pkgName)) {
                                removed = android.content.pm.PackageManagerTests.MoveReceiver.ADDED;
                                return true;
                            }
                        }
                    }
                }

            return false;
        }
    }

    public boolean invokeMovePackage(java.lang.String pkgName, int flags, android.content.pm.PackageManagerTests.GenericReceiver receiver) throws java.lang.Exception {
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean invokeMovePackageFail(java.lang.String pkgName, int flags, int errCode) throws java.lang.Exception {
        throw new java.lang.UnsupportedOperationException();
    }

    private int getDefaultInstallLoc() {
        int origDefaultLoc = android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO;
        try {
            origDefaultLoc = Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.DEFAULT_INSTALL_LOCATION);
        } catch (android.provider.Settings.SettingNotFoundException e1) {
        }
        return origDefaultLoc;
    }

    private void setInstallLoc(int loc) {
        Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.DEFAULT_INSTALL_LOCATION, loc);
    }

    /* Tests for moving apps between internal and external storage */
    /* Utility function that reads a apk bundled as a raw resource
    copies it into own data directory and invokes
    PackageManager api to install first and then replace it
    again.
     */
    private void moveFromRawResource(java.lang.String outFileName, int rawResId, int installFlags, int moveFlags, boolean cleanUp, boolean fail, int result) throws java.lang.Exception {
        int origDefaultLoc = getDefaultInstallLoc();
        android.content.pm.PackageManagerTests.InstallParams ip = null;
        try {
            setInstallLoc(PackageHelper.APP_INSTALL_AUTO);
            // Install first
            ip = installFromRawResource("install.apk", rawResId, installFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            android.content.pm.ApplicationInfo oldAppInfo = getPm().getApplicationInfo(ip.pkg.packageName, 0);
            if (fail) {
                assertTrue(invokeMovePackageFail(ip.pkg.packageName, moveFlags, result));
                android.content.pm.ApplicationInfo info = getPm().getApplicationInfo(ip.pkg.packageName, 0);
                assertNotNull(info);
                assertEquals(oldAppInfo.flags, info.flags);
            } else {
                // Create receiver based on expRetCode
                android.content.pm.PackageManagerTests.MoveReceiver receiver = new android.content.pm.PackageManagerTests.MoveReceiver(ip.pkg.packageName);
                boolean retCode = invokeMovePackage(ip.pkg.packageName, moveFlags, receiver);
                assertTrue(retCode);
                android.content.pm.ApplicationInfo info = getPm().getApplicationInfo(ip.pkg.packageName, 0);
                assertNotNull("ApplicationInfo for recently installed application should exist", info);
                if ((moveFlags & android.content.pm.PackageManager.MOVE_INTERNAL) != 0) {
                    assertTrue("ApplicationInfo.FLAG_EXTERNAL_STORAGE flag should NOT be set", (info.flags & android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0);
                    android.content.pm.PackageManagerTests.assertStartsWith("Native library dir should be in dataDir", info.dataDir, info.nativeLibraryDir);
                } else
                    if ((moveFlags & android.content.pm.PackageManager.MOVE_EXTERNAL_MEDIA) != 0) {
                        assertTrue("ApplicationInfo.FLAG_EXTERNAL_STORAGE flag should be set", (info.flags & android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0);
                        android.content.pm.PackageManagerTests.assertStartsWith("Native library dir should point to ASEC", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, info.nativeLibraryDir);
                        final java.io.File nativeLibSymLink = new java.io.File(info.dataDir, "lib");
                        android.content.pm.PackageManagerTests.assertStartsWith("The data directory should have a 'lib' symlink that points to the ASEC container", android.content.pm.PackageManagerTests.SECURE_CONTAINERS_PREFIX, nativeLibSymLink.getCanonicalPath());
                    }

            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            failStr("Pkg hasnt been installed correctly");
        } catch (java.lang.Exception e) {
            failStr("Failed with exception : " + e);
        } finally {
            if (ip != null) {
                cleanUpInstall(ip);
            }
            // Restore default install location
            setInstallLoc(origDefaultLoc);
        }
    }

    private void sampleMoveFromRawResource(int installFlags, int moveFlags, boolean fail, int result) throws java.lang.Exception {
        moveFromRawResource("install.apk", R.raw.install, installFlags, moveFlags, true, fail, result);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testMoveAppInternalToExternal() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int installFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int moveFlags = android.content.pm.PackageManager.MOVE_EXTERNAL_MEDIA;
        boolean fail = false;
        int result = android.content.pm.PackageManager.MOVE_SUCCEEDED;
        sampleMoveFromRawResource(installFlags, moveFlags, fail, result);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testMoveAppInternalToInternal() throws java.lang.Exception {
        int installFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int moveFlags = android.content.pm.PackageManager.MOVE_INTERNAL;
        boolean fail = true;
        int result = android.content.pm.PackageManager.MOVE_FAILED_INVALID_LOCATION;
        sampleMoveFromRawResource(installFlags, moveFlags, fail, result);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testMoveAppExternalToExternal() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int installFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int moveFlags = android.content.pm.PackageManager.MOVE_EXTERNAL_MEDIA;
        boolean fail = true;
        int result = android.content.pm.PackageManager.MOVE_FAILED_INVALID_LOCATION;
        sampleMoveFromRawResource(installFlags, moveFlags, fail, result);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testMoveAppExternalToInternal() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int installFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int moveFlags = android.content.pm.PackageManager.MOVE_INTERNAL;
        boolean fail = false;
        int result = android.content.pm.PackageManager.MOVE_SUCCEEDED;
        sampleMoveFromRawResource(installFlags, moveFlags, fail, result);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testMoveAppForwardLocked() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int installFlags = android.content.pm.PackageManager.INSTALL_FORWARD_LOCK;
        int moveFlags = android.content.pm.PackageManager.MOVE_EXTERNAL_MEDIA;
        boolean fail = false;
        int result = android.content.pm.PackageManager.MOVE_SUCCEEDED;
        sampleMoveFromRawResource(installFlags, moveFlags, fail, result);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testMoveAppFailInternalToExternalDelete() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int installFlags = 0;
        int moveFlags = android.content.pm.PackageManager.MOVE_EXTERNAL_MEDIA;
        boolean fail = true;
        final int result = android.content.pm.PackageManager.MOVE_FAILED_DOESNT_EXIST;
        int rawResId = R.raw.install;
        int origDefaultLoc = getDefaultInstallLoc();
        android.content.pm.PackageManagerTests.InstallParams ip = null;
        try {
            android.content.pm.PackageManager pm = getPm();
            setInstallLoc(PackageHelper.APP_INSTALL_AUTO);
            // Install first
            ip = installFromRawResource("install.apk", R.raw.install, installFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            // Delete the package now retaining data.
            android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.DeleteReceiver(ip.pkg.packageName);
            invokeDeletePackage(ip.pkg.packageName, android.content.pm.PackageManager.DELETE_KEEP_DATA, receiver);
            assertTrue(invokeMovePackageFail(ip.pkg.packageName, moveFlags, result));
        } catch (java.lang.Exception e) {
            failStr(e);
        } finally {
            if (ip != null) {
                cleanUpInstall(ip);
            }
            // Restore default install location
            setInstallLoc(origDefaultLoc);
        }
    }

    /* Test that an install error code is returned when media is unmounted
    and package installed on sdcard via package manager flag.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallSdcardUnmount() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        boolean origState = checkMediaState(Environment.MEDIA_MOUNTED);
        try {
            // Unmount sdcard
            assertTrue(unmountMedia());
            // Try to install and make sure an error code is returned.
            installFromRawResource("install.apk", R.raw.install, android.content.pm.PackageManager.INSTALL_EXTERNAL, false, true, android.content.pm.PackageManager.INSTALL_FAILED_MEDIA_UNAVAILABLE, android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO);
        } finally {
            // Restore original media state
            if (origState) {
                mountMedia();
            } else {
                unmountMedia();
            }
        }
    }

    /* Unmount sdcard. Try installing an app with manifest option to install
    on sdcard. Make sure it gets installed on internal flash.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallManifestSdcardUnmount() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        boolean origState = checkMediaState(Environment.MEDIA_MOUNTED);
        try {
            // Unmount sdcard
            assertTrue(unmountMedia());
            android.content.pm.PackageManagerTests.InstallParams ip = new android.content.pm.PackageManagerTests.InstallParams("install.apk", R.raw.install_loc_sdcard);
            installFromRawResource(ip, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
        } finally {
            // Restore original media state
            if (origState) {
                mountMedia();
            } else {
                unmountMedia();
            }
        }
    }

    /* ---------- Recommended install location tests ---- */
    /* PrecedenceSuffixes:
    Flag : FlagI, FlagE, FlagF
    I - internal, E - external, F - forward locked, Flag suffix absent if not using any option.
    Manifest: ManifestI, ManifestE, ManifestA, Manifest suffix absent if not using any option.
    Existing: Existing suffix absent if not existing.
    User: UserI, UserE, UserA, User suffix absent if not existing.
     */
    /* Install an app on internal flash */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagI() throws java.lang.Exception {
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_INTERNAL, true);
    }

    /* Install an app on sdcard. */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_EXTERNAL, true);
    }

    /* Install an app forward-locked. */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagF() throws java.lang.Exception {
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, true);
    }

    /* Install an app with both internal and external flags set. should fail */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIE() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install, android.content.pm.PackageManager.INSTALL_EXTERNAL | android.content.pm.PackageManager.INSTALL_INTERNAL, false, true, android.content.pm.PackageManager.INSTALL_FAILED_INVALID_INSTALL_LOCATION, android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO);
    }

    /* Install an app with both internal and forward-lock flags set. */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIF() throws java.lang.Exception {
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK | android.content.pm.PackageManager.INSTALL_INTERNAL, true);
    }

    /* Install an app with both external and forward-lock flags set. */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagEF() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        sampleInstallFromRawResource(android.content.pm.PackageManager.INSTALL_FORWARD_LOCK | android.content.pm.PackageManager.INSTALL_EXTERNAL, true);
    }

    /* Install an app with both internal and external flags set with forward
    lock. Should fail.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIEF() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install, (android.content.pm.PackageManager.INSTALL_FORWARD_LOCK | android.content.pm.PackageManager.INSTALL_INTERNAL) | android.content.pm.PackageManager.INSTALL_EXTERNAL, false, true, android.content.pm.PackageManager.INSTALL_FAILED_INVALID_INSTALL_LOCATION, android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO);
    }

    /* Install an app with both internal and manifest option set.
    should install on internal.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIManifestI() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_internal, android.content.pm.PackageManager.INSTALL_INTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    /* Install an app with both internal and manifest preference for
    preferExternal. Should install on internal.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIManifestE() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, android.content.pm.PackageManager.INSTALL_INTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    /* Install an app with both internal and manifest preference for
    auto. should install internal.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIManifestA() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_auto, android.content.pm.PackageManager.INSTALL_INTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    /* Install an app with both external and manifest option set.
    should install externally.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagEManifestI() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_internal, android.content.pm.PackageManager.INSTALL_EXTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* Install an app with both external and manifest preference for
    preferExternal. Should install externally.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagEManifestE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, android.content.pm.PackageManager.INSTALL_EXTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* Install an app with both external and manifest preference for
    auto. should install on external media.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagEManifestA() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_auto, android.content.pm.PackageManager.INSTALL_EXTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* Install an app with fwd locked flag set and install location set to
    internal. should install internally.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagFManifestI() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_internal, android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    /* Install an app with fwd locked flag set and install location set to
    preferExternal. Should install externally.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagFManifestE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* Install an app with fwd locked flag set and install location set to auto.
    should install externally.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagFManifestA() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_auto, android.content.pm.PackageManager.INSTALL_FORWARD_LOCK, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO);
    }

    /* The following test functions verify install location for existing apps.
    ie existing app can be installed internally or externally. If install
    flag is explicitly set it should override current location. If manifest location
    is set, that should over ride current location too. if not the existing install
    location should be honoured.
    testFlagI/E/F/ExistingI/E -
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIExistingI() throws java.lang.Exception {
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_INTERNAL | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagIExistingE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_INTERNAL | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagEExistingI() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagEExistingE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagFExistingI() throws java.lang.Exception {
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_FORWARD_LOCK | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testFlagFExistingE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_FORWARD_LOCK | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, -1);
    }

    /* The following set of tests verify the installation of apps with
    install location attribute set to internalOnly, preferExternal and auto.
    The manifest option should dictate the install location.
    public void testManifestI/E/A
    TODO out of memory fall back behaviour.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestI() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_internal, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestA() throws java.lang.Exception {
        installFromRawResource("install.apk", R.raw.install_loc_auto, 0, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    /* The following set of tests verify the installation of apps
    with install location attribute set to internalOnly, preferExternal and auto
    for already existing apps. The manifest option should take precedence.
    TODO add out of memory fall back behaviour.
    testManifestI/E/AExistingI/E
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestIExistingI() throws java.lang.Exception {
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install_loc_internal, rFlags, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestIExistingE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install_loc_internal, rFlags, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestEExistingI() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, rFlags, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestEExistingE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install_loc_sdcard, rFlags, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestAExistingI() throws java.lang.Exception {
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install_loc_auto, rFlags, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManifestAExistingE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, -1);
        // Replace now
        installFromRawResource("install.apk", R.raw.install_loc_auto, rFlags, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* The following set of tests check install location for existing
    application based on user setting.
     */
    private int getExpectedInstallLocation(int userSetting) {
        int iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED;
        boolean enable = getUserSettingSetInstallLocation();
        if (enable) {
            if (userSetting == com.android.internal.content.PackageHelper.APP_INSTALL_AUTO) {
                iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO;
            } else
                if (userSetting == com.android.internal.content.PackageHelper.APP_INSTALL_EXTERNAL) {
                    iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL;
                } else
                    if (userSetting == com.android.internal.content.PackageHelper.APP_INSTALL_INTERNAL) {
                        iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY;
                    }


        }
        return iloc;
    }

    private void setExistingXUserX(int userSetting, int iFlags, int iloc) throws java.lang.Exception {
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        // First install.
        installFromRawResource("install.apk", R.raw.install, iFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        int origSetting = getDefaultInstallLoc();
        try {
            // Set user setting
            setInstallLoc(userSetting);
            // Replace now
            installFromRawResource("install.apk", R.raw.install, rFlags, true, false, -1, iloc);
        } finally {
            setInstallLoc(origSetting);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testExistingIUserI() throws java.lang.Exception {
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_INTERNAL;
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        setExistingXUserX(userSetting, iFlags, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testExistingIUserE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_EXTERNAL;
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        setExistingXUserX(userSetting, iFlags, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testExistingIUserA() throws java.lang.Exception {
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_AUTO;
        int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
        setExistingXUserX(userSetting, iFlags, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testExistingEUserI() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_INTERNAL;
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        setExistingXUserX(userSetting, iFlags, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testExistingEUserE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_EXTERNAL;
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        setExistingXUserX(userSetting, iFlags, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testExistingEUserA() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_AUTO;
        int iFlags = android.content.pm.PackageManager.INSTALL_EXTERNAL;
        setExistingXUserX(userSetting, iFlags, android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL);
    }

    /* The following set of tests verify that the user setting defines
    the install location.
     */
    private boolean getUserSettingSetInstallLocation() {
        try {
            return Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.SET_INSTALL_LOCATION) != 0;
        } catch (android.provider.Settings.SettingNotFoundException e1) {
        }
        return false;
    }

    private void setUserSettingSetInstallLocation(boolean value) {
        Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.SET_INSTALL_LOCATION, value ? 1 : 0);
    }

    private void setUserX(boolean enable, int userSetting, int iloc) throws java.lang.Exception {
        boolean origUserSetting = getUserSettingSetInstallLocation();
        int origSetting = getDefaultInstallLoc();
        try {
            setUserSettingSetInstallLocation(enable);
            // Set user setting
            setInstallLoc(userSetting);
            // Replace now
            installFromRawResource("install.apk", R.raw.install, 0, true, false, -1, iloc);
        } finally {
            // Restore original setting
            setUserSettingSetInstallLocation(origUserSetting);
            setInstallLoc(origSetting);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testUserI() throws java.lang.Exception {
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_INTERNAL;
        int iloc = getExpectedInstallLocation(userSetting);
        setUserX(true, userSetting, iloc);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testUserE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_EXTERNAL;
        int iloc = getExpectedInstallLocation(userSetting);
        setUserX(true, userSetting, iloc);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testUserA() throws java.lang.Exception {
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_AUTO;
        int iloc = getExpectedInstallLocation(userSetting);
        setUserX(true, userSetting, iloc);
    }

    /* The following set of tests turn on/off the basic
    user setting for turning on install location.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testUserPrefOffUserI() throws java.lang.Exception {
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_INTERNAL;
        int iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED;
        setUserX(false, userSetting, iloc);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testUserPrefOffUserE() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_EXTERNAL;
        int iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED;
        setUserX(false, userSetting, iloc);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testUserPrefOffA() throws java.lang.Exception {
        int userSetting = com.android.internal.content.PackageHelper.APP_INSTALL_AUTO;
        int iloc = android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED;
        setUserX(false, userSetting, iloc);
    }

    static final java.lang.String[] BASE_PERMISSIONS_DEFINED = new java.lang.String[]{ android.content.pm.PackageManagerTests.PERM_PACKAGE, "com.android.unit_tests.install_decl_perm", android.content.pm.PackageManagerTests.PERM_DEFINED, "com.android.frameworks.coretests.NORMAL", "com.android.frameworks.coretests.DANGEROUS", "com.android.frameworks.coretests.SIGNATURE" };

    static final java.lang.String[] BASE_PERMISSIONS_UNDEFINED = new java.lang.String[]{ android.content.pm.PackageManagerTests.PERM_PACKAGE, "com.android.frameworks.coretests.install_decl_perm", android.content.pm.PackageManagerTests.PERM_UNDEFINED, "com.android.frameworks.coretests.NORMAL", "com.android.frameworks.coretests.DANGEROUS", "com.android.frameworks.coretests.SIGNATURE" };

    static final java.lang.String[] BASE_PERMISSIONS_USED = new java.lang.String[]{ android.content.pm.PackageManagerTests.PERM_PACKAGE, "com.android.frameworks.coretests.install_use_perm_good", android.content.pm.PackageManagerTests.PERM_USED, "com.android.frameworks.coretests.NORMAL", "com.android.frameworks.coretests.DANGEROUS", "com.android.frameworks.coretests.SIGNATURE" };

    static final java.lang.String[] BASE_PERMISSIONS_NOTUSED = new java.lang.String[]{ android.content.pm.PackageManagerTests.PERM_PACKAGE, "com.android.frameworks.coretests.install_use_perm_good", android.content.pm.PackageManagerTests.PERM_NOTUSED, "com.android.frameworks.coretests.NORMAL", "com.android.frameworks.coretests.DANGEROUS", "com.android.frameworks.coretests.SIGNATURE" };

    static final java.lang.String[] BASE_PERMISSIONS_SIGUSED = new java.lang.String[]{ android.content.pm.PackageManagerTests.PERM_PACKAGE, "com.android.frameworks.coretests.install_use_perm_good", android.content.pm.PackageManagerTests.PERM_USED, "com.android.frameworks.coretests.SIGNATURE", android.content.pm.PackageManagerTests.PERM_NOTUSED, "com.android.frameworks.coretests.NORMAL", "com.android.frameworks.coretests.DANGEROUS" };

    /* Ensure that permissions are properly declared. */
    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallDeclaresPermissions() throws java.lang.Exception {
        android.content.pm.PackageManagerTests.InstallParams ip = null;
        android.content.pm.PackageManagerTests.InstallParams ip2 = null;
        try {
            // **: Upon installing a package, are its declared permissions published?
            int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
            int iApk = R.raw.install_decl_perm;
            ip = installFromRawResource("install.apk", iApk, iFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip.pkg, iFlags, ip.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_DEFINED);
            // **: Upon installing package, are its permissions granted?
            int i2Flags = android.content.pm.PackageManager.INSTALL_INTERNAL;
            int i2Apk = R.raw.install_use_perm_good;
            ip2 = installFromRawResource("install2.apk", i2Apk, i2Flags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip2.pkg, i2Flags, ip2.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_USED);
            // **: Upon removing but not deleting, are permissions retained?
            android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.DeleteReceiver(ip.pkg.packageName);
            try {
                invokeDeletePackage(ip.pkg.packageName, android.content.pm.PackageManager.DELETE_KEEP_DATA, receiver);
            } catch (java.lang.Exception e) {
                failStr(e);
            }
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_DEFINED);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_USED);
            // **: Upon re-installing, are permissions retained?
            ip = installFromRawResource("install.apk", iApk, iFlags | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip.pkg, iFlags, ip.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_DEFINED);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_USED);
            // **: Upon deleting package, are all permissions removed?
            try {
                invokeDeletePackage(ip.pkg.packageName, 0, receiver);
                ip = null;
            } catch (java.lang.Exception e) {
                failStr(e);
            }
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_UNDEFINED);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_NOTUSED);
            // **: Delete package using permissions; nothing to check here.
            android.content.pm.PackageManagerTests.GenericReceiver receiver2 = new android.content.pm.PackageManagerTests.DeleteReceiver(ip2.pkg.packageName);
            try {
                invokeDeletePackage(ip2.pkg.packageName, 0, receiver);
                ip2 = null;
            } catch (java.lang.Exception e) {
                failStr(e);
            }
            // **: Re-install package using permissions; no permissions can be granted.
            ip2 = installFromRawResource("install2.apk", i2Apk, i2Flags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip2.pkg, i2Flags, ip2.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_NOTUSED);
            // **: Upon installing declaring package, are sig permissions granted
            // to other apps (but not other perms)?
            ip = installFromRawResource("install.apk", iApk, iFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip.pkg, iFlags, ip.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_DEFINED);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_SIGUSED);
            // **: Re-install package using permissions; are all permissions granted?
            ip2 = installFromRawResource("install2.apk", i2Apk, i2Flags | android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip2.pkg, i2Flags, ip2.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_NOTUSED);
            // **: Upon deleting package, are all permissions removed?
            try {
                invokeDeletePackage(ip.pkg.packageName, 0, receiver);
                ip = null;
            } catch (java.lang.Exception e) {
                failStr(e);
            }
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_UNDEFINED);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_NOTUSED);
            // **: Delete package using permissions; nothing to check here.
            try {
                invokeDeletePackage(ip2.pkg.packageName, 0, receiver);
                ip2 = null;
            } catch (java.lang.Exception e) {
                failStr(e);
            }
        } finally {
            if (ip2 != null) {
                cleanUpInstall(ip2);
            }
            if (ip != null) {
                cleanUpInstall(ip);
            }
        }
    }

    /* Ensure that permissions are properly declared. */
    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallOnSdPermissionsUnmount() throws java.lang.Exception {
        android.content.pm.PackageManagerTests.InstallParams ip = null;
        boolean origMediaState = checkMediaState(Environment.MEDIA_MOUNTED);
        try {
            // **: Upon installing a package, are its declared permissions published?
            int iFlags = android.content.pm.PackageManager.INSTALL_INTERNAL;
            int iApk = R.raw.install_decl_perm;
            ip = installFromRawResource("install.apk", iApk, iFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
            assertInstall(ip.pkg, iFlags, ip.pkg.installLocation);
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_DEFINED);
            // Unmount media here
            assertTrue(unmountMedia());
            // Mount media again
            mountMedia();
            // Check permissions now
            assertPermissions(android.content.pm.PackageManagerTests.BASE_PERMISSIONS_DEFINED);
        } finally {
            if (ip != null) {
                cleanUpInstall(ip);
            }
        }
    }

    /* This test creates a stale container via MountService and then installs
    a package and verifies that the stale container is cleaned up and install
    is successful.
    Please note that this test is very closely tied to the framework's
    naming convention for secure containers.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallSdcardStaleContainer() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        boolean origMediaState = checkMediaState(Environment.MEDIA_MOUNTED);
        try {
            // Mount media first
            mountMedia();
            java.lang.String outFileName = "install.apk";
            int rawResId = R.raw.install;
            android.content.pm.PackageManager pm = mContext.getPackageManager();
            java.io.File filesDir = mContext.getFilesDir();
            java.io.File outFile = new java.io.File(filesDir, outFileName);
            android.net.Uri packageURI = getInstallablePackage(rawResId, outFile);
            android.content.pm.PackageParser.Package pkg = parsePackage(packageURI);
            assertNotNull(pkg);
            // Install an app on sdcard.
            installFromRawResource(outFileName, rawResId, android.content.pm.PackageManager.INSTALL_EXTERNAL, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            // Unmount sdcard
            unmountMedia();
            // Delete the app on sdcard to leave a stale container on sdcard.
            android.content.pm.PackageManagerTests.GenericReceiver receiver = new android.content.pm.PackageManagerTests.DeleteReceiver(pkg.packageName);
            assertTrue(invokeDeletePackage(pkg.packageName, 0, receiver));
            mountMedia();
            // Reinstall the app and make sure it gets installed.
            installFromRawResource(outFileName, rawResId, android.content.pm.PackageManager.INSTALL_EXTERNAL, true, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        } catch (java.lang.Exception e) {
            failStr(e.getMessage());
        } finally {
            if (origMediaState) {
                mountMedia();
            } else {
                unmountMedia();
            }
        }
    }

    /* This test installs an application on sdcard and unmounts media.
    The app is then re-installed on internal storage. The sdcard is mounted
    and verified that the re-installation on internal storage takes precedence.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallSdcardStaleContainerReinstall() throws java.lang.Exception {
        // Do not run on devices with emulated external storage.
        if (android.os.Environment.isExternalStorageEmulated()) {
            return;
        }
        boolean origMediaState = checkMediaState(Environment.MEDIA_MOUNTED);
        try {
            // Mount media first
            mountMedia();
            java.lang.String outFileName = "install.apk";
            int rawResId = R.raw.install;
            android.content.pm.PackageManager pm = mContext.getPackageManager();
            java.io.File filesDir = mContext.getFilesDir();
            java.io.File outFile = new java.io.File(filesDir, outFileName);
            android.net.Uri packageURI = getInstallablePackage(rawResId, outFile);
            android.content.pm.PackageParser.Package pkg = parsePackage(packageURI);
            assertNotNull(pkg);
            // Install an app on sdcard.
            installFromRawResource(outFileName, rawResId, android.content.pm.PackageManager.INSTALL_EXTERNAL, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            // Unmount sdcard
            unmountMedia();
            // Reinstall the app and make sure it gets installed on internal storage.
            installFromRawResource(outFileName, rawResId, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            mountMedia();
            // Verify that the app installed is on internal storage.
            assertInstall(pkg, 0, android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY);
        } catch (java.lang.Exception e) {
            failStr(e.getMessage());
        } finally {
            if (origMediaState) {
                mountMedia();
            } else {
                unmountMedia();
            }
        }
    }

    /* The following series of tests are related to upgrading apps with
    different certificates.
     */
    private int APP1_UNSIGNED = R.raw.install_app1_unsigned;

    private int APP1_CERT1 = R.raw.install_app1_cert1;

    private int APP1_CERT2 = R.raw.install_app1_cert2;

    private int APP1_CERT1_CERT2 = R.raw.install_app1_cert1_cert2;

    private int APP1_CERT3_CERT4 = R.raw.install_app1_cert3_cert4;

    private int APP1_CERT3 = R.raw.install_app1_cert3;

    private int APP2_UNSIGNED = R.raw.install_app2_unsigned;

    private int APP2_CERT1 = R.raw.install_app2_cert1;

    private int APP2_CERT2 = R.raw.install_app2_cert2;

    private int APP2_CERT1_CERT2 = R.raw.install_app2_cert1_cert2;

    private int APP2_CERT3 = R.raw.install_app2_cert3;

    private android.content.pm.PackageManagerTests.InstallParams replaceCerts(int apk1, int apk2, boolean cleanUp, boolean fail, int retCode) throws java.lang.Exception {
        int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
        java.lang.String apk1Name = "install1.apk";
        java.lang.String apk2Name = "install2.apk";
        android.content.pm.PackageParser.Package pkg1 = getParsedPackage(apk1Name, apk1);
        try {
            android.content.pm.PackageManagerTests.InstallParams ip = installFromRawResource(apk1Name, apk1, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            installFromRawResource(apk2Name, apk2, rFlags, false, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            return ip;
        } catch (java.lang.Exception e) {
            failStr(e.getMessage());
        } finally {
            if (cleanUp) {
                cleanUpInstall(pkg1.packageName);
            }
        }
        return null;
    }

    /* Test that an app signed with two certificates can be upgraded by the
    same app signed with two certificates.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchAllCerts() throws java.lang.Exception {
        replaceCerts(APP1_CERT1_CERT2, APP1_CERT1_CERT2, true, false, -1);
    }

    /* Test that an app signed with two certificates cannot be upgraded
    by an app signed with a different certificate.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchNoCerts1() throws java.lang.Exception {
        replaceCerts(APP1_CERT1_CERT2, APP1_CERT3, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Test that an app signed with two certificates cannot be upgraded
    by an app signed with a different certificate.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchNoCerts2() throws java.lang.Exception {
        replaceCerts(APP1_CERT1_CERT2, APP1_CERT3_CERT4, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Test that an app signed with two certificates cannot be upgraded by
    an app signed with a subset of initial certificates.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchSomeCerts1() throws java.lang.Exception {
        replaceCerts(APP1_CERT1_CERT2, APP1_CERT1, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Test that an app signed with two certificates cannot be upgraded by
    an app signed with the last certificate.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchSomeCerts2() throws java.lang.Exception {
        replaceCerts(APP1_CERT1_CERT2, APP1_CERT2, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Test that an app signed with a certificate can be upgraded by app
    signed with a superset of certificates.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchMoreCerts() throws java.lang.Exception {
        replaceCerts(APP1_CERT1, APP1_CERT1_CERT2, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Test that an app signed with a certificate can be upgraded by app
    signed with a superset of certificates. Then verify that the an app
    signed with the original set of certs cannot upgrade the new one.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceMatchMoreCertsReplaceSomeCerts() throws java.lang.Exception {
        android.content.pm.PackageManagerTests.InstallParams ip = replaceCerts(APP1_CERT1, APP1_CERT1_CERT2, false, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
        try {
            int rFlags = android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING;
            installFromRawResource("install.apk", APP1_CERT1, rFlags, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        } catch (java.lang.Exception e) {
            failStr(e.getMessage());
        } finally {
            if (ip != null) {
                cleanUpInstall(ip);
            }
        }
    }

    /**
     * The following tests are related to testing KeySets-based key rotation
     */
    /* Check if an apk which does not specify an upgrade-keyset may be upgraded
    by an apk which does
     */
    public void testNoKSToUpgradeKS() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_unone, R.raw.keyset_sa_ua, true, false, -1);
    }

    /* Check if an apk which does specify an upgrade-keyset may be downgraded to
    an apk which does not
     */
    public void testUpgradeKSToNoKS() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ua, R.raw.keyset_sa_unone, true, false, -1);
    }

    /* Check if an apk signed by a key other than the upgrade keyset can update
    an app
     */
    public void testUpgradeKSWithWrongKey() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ua, R.raw.keyset_sb_ua, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Check if an apk signed by its signing key, which is not an upgrade key,
    can upgrade an app.
     */
    public void testUpgradeKSWithWrongSigningKey() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ub, R.raw.keyset_sa_ub, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Check if an apk signed by its upgrade key, which is not its signing key,
    can upgrade an app.
     */
    public void testUpgradeKSWithUpgradeKey() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ub, R.raw.keyset_sb_ub, true, false, -1);
    }

    /* Check if an apk signed by its upgrade key, which is its signing key, can
    upgrade an app.
     */
    public void testUpgradeKSWithSigningUpgradeKey() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ua, R.raw.keyset_sa_ua, true, false, -1);
    }

    /* Check if an apk signed by multiple keys, one of which is its upgrade key,
    can upgrade an app.
     */
    public void testMultipleUpgradeKSWithUpgradeKey() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ua, R.raw.keyset_sab_ua, true, false, -1);
    }

    /* Check if an apk signed by multiple keys, one of which is its signing key,
    but none of which is an upgrade key, can upgrade an app.
     */
    public void testMultipleUpgradeKSWithSigningKey() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sau_ub, R.raw.keyset_sa_ua, true, true, android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE);
    }

    /* Check if an apk which defines multiple (two) upgrade keysets is
    upgrade-able by either.
     */
    public void testUpgradeKSWithMultipleUpgradeKeySets() throws java.lang.Exception {
        replaceCerts(R.raw.keyset_sa_ua_ub, R.raw.keyset_sa_ua, true, false, -1);
        replaceCerts(R.raw.keyset_sa_ua_ub, R.raw.keyset_sb_ub, true, false, -1);
    }

    /* Check if an apk's sigs are changed after upgrading with a non-signing
    key.

    TODO: consider checking against hard-coded Signatures in the Sig-tests
     */
    public void testSigChangeAfterUpgrade() throws java.lang.Exception {
        // install original apk and grab sigs
        installFromRawResource("tmp.apk", R.raw.keyset_sa_ub, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        android.content.pm.PackageManager pm = getPm();
        java.lang.String pkgName = "com.android.frameworks.coretests.keysets";
        android.content.pm.PackageInfo pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should only have one signature, sig A", pi.signatures.length == 1);
        java.lang.String sigBefore = pi.signatures[0].toCharsString();
        // install apk signed by different upgrade KeySet
        installFromRawResource("tmp2.apk", R.raw.keyset_sb_ub, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should only have one signature, sig B", pi.signatures.length == 1);
        java.lang.String sigAfter = pi.signatures[0].toCharsString();
        assertFalse("Package signatures did not change after upgrade!", sigBefore.equals(sigAfter));
        cleanUpInstall(pkgName);
    }

    /* Check if an apk's sig is the same  after upgrading with a signing
    key.
     */
    public void testSigSameAfterUpgrade() throws java.lang.Exception {
        // install original apk and grab sigs
        installFromRawResource("tmp.apk", R.raw.keyset_sa_ua, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        android.content.pm.PackageManager pm = getPm();
        java.lang.String pkgName = "com.android.frameworks.coretests.keysets";
        android.content.pm.PackageInfo pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should only have one signature, sig A", pi.signatures.length == 1);
        java.lang.String sigBefore = pi.signatures[0].toCharsString();
        // install apk signed by same upgrade KeySet
        installFromRawResource("tmp2.apk", R.raw.keyset_sa_ua, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should only have one signature, sig A", pi.signatures.length == 1);
        java.lang.String sigAfter = pi.signatures[0].toCharsString();
        assertTrue("Package signatures changed after upgrade!", sigBefore.equals(sigAfter));
        cleanUpInstall(pkgName);
    }

    /* Check if an apk's sigs are the same after upgrading with an app with
    a subset of the original signing keys.
     */
    public void testSigRemovedAfterUpgrade() throws java.lang.Exception {
        // install original apk and grab sigs
        installFromRawResource("tmp.apk", R.raw.keyset_sab_ua, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        android.content.pm.PackageManager pm = getPm();
        java.lang.String pkgName = "com.android.frameworks.coretests.keysets";
        android.content.pm.PackageInfo pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should have two signatures, sig A and sig B", pi.signatures.length == 2);
        java.util.Set<java.lang.String> sigsBefore = new java.util.HashSet<java.lang.String>();
        for (int i = 0; i < pi.signatures.length; i++) {
            sigsBefore.add(pi.signatures[i].toCharsString());
        }
        // install apk signed subset upgrade KeySet
        installFromRawResource("tmp2.apk", R.raw.keyset_sa_ua, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should only have one signature, sig A", pi.signatures.length == 1);
        java.lang.String sigAfter = pi.signatures[0].toCharsString();
        assertTrue("Original package signatures did not contain new sig", sigsBefore.contains(sigAfter));
        cleanUpInstall(pkgName);
    }

    /* Check if an apk's sigs are added to after upgrading with an app with
    a superset of the original signing keys.
     */
    public void testSigAddedAfterUpgrade() throws java.lang.Exception {
        // install original apk and grab sigs
        installFromRawResource("tmp.apk", R.raw.keyset_sa_ua, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        android.content.pm.PackageManager pm = getPm();
        java.lang.String pkgName = "com.android.frameworks.coretests.keysets";
        android.content.pm.PackageInfo pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should only have one signature, sig A", pi.signatures.length == 1);
        java.lang.String sigBefore = pi.signatures[0].toCharsString();
        // install apk signed subset upgrade KeySet
        installFromRawResource("tmp2.apk", R.raw.keyset_sab_ua, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        pi = pm.getPackageInfo(pkgName, android.content.pm.PackageManager.GET_SIGNATURES);
        assertTrue("Package should have two signatures, sig A and sig B", pi.signatures.length == 2);
        java.util.Set<java.lang.String> sigsAfter = new java.util.HashSet<java.lang.String>();
        for (int i = 0; i < pi.signatures.length; i++) {
            sigsAfter.add(pi.signatures[i].toCharsString());
        }
        assertTrue("Package signatures did not change after upgrade!", sigsAfter.contains(sigBefore));
        cleanUpInstall(pkgName);
    }

    /* Check if an apk gains signature-level permission after changing to the a
    new signature, for which a permission should be granted.
     */
    public void testUpgradeSigPermGained() throws java.lang.Exception {
        // install apk which defines permission
        installFromRawResource("permDef.apk", R.raw.keyset_permdef_sa_unone, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        // install apk which uses permission but does not have sig
        installFromRawResource("permUse.apk", R.raw.keyset_permuse_sb_ua_ub, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        // verify that package does not have perm before
        android.content.pm.PackageManager pm = getPm();
        java.lang.String permPkgName = "com.android.frameworks.coretests.keysets_permdef";
        java.lang.String pkgName = "com.android.frameworks.coretests.keysets";
        java.lang.String permName = "com.android.frameworks.coretests.keysets_permdef.keyset_perm";
        assertFalse("keyset permission granted to app without same signature!", pm.checkPermission(permName, pkgName) == android.content.pm.PackageManager.PERMISSION_GRANTED);
        // upgrade to apk with perm signature
        installFromRawResource("permUse2.apk", R.raw.keyset_permuse_sa_ua_ub, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        assertTrue("keyset permission not granted to app after upgrade to same sig", pm.checkPermission(permName, pkgName) == android.content.pm.PackageManager.PERMISSION_GRANTED);
        cleanUpInstall(permPkgName);
        cleanUpInstall(pkgName);
    }

    /* Check if an apk loses signature-level permission after changing to the a
    new signature, from one which a permission should be granted.
     */
    public void testUpgradeSigPermLost() throws java.lang.Exception {
        // install apk which defines permission
        installFromRawResource("permDef.apk", R.raw.keyset_permdef_sa_unone, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        // install apk which uses permission, signed by same sig
        installFromRawResource("permUse.apk", R.raw.keyset_permuse_sa_ua_ub, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        // verify that package does not have perm before
        android.content.pm.PackageManager pm = getPm();
        java.lang.String permPkgName = "com.android.frameworks.coretests.keysets_permdef";
        java.lang.String pkgName = "com.android.frameworks.coretests.keysets";
        java.lang.String permName = "com.android.frameworks.coretests.keysets_permdef.keyset_perm";
        assertTrue("keyset permission not granted to app with same sig", pm.checkPermission(permName, pkgName) == android.content.pm.PackageManager.PERMISSION_GRANTED);
        // upgrade to apk without perm signature
        installFromRawResource("permUse2.apk", R.raw.keyset_permuse_sb_ua_ub, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        assertFalse("keyset permission not revoked from app which upgraded to a " + "different signature", pm.checkPermission(permName, pkgName) == android.content.pm.PackageManager.PERMISSION_GRANTED);
        cleanUpInstall(permPkgName);
        cleanUpInstall(pkgName);
    }

    /**
     * The following tests are related to testing KeySets-based API
     */
    /* testGetSigningKeySetNull - ensure getSigningKeySet() returns null on null
    input and when calling a package other than that which made the call.
     */
    public void testGetSigningKeySet() throws java.lang.Exception {
        android.content.pm.PackageManager pm = getPm();
        java.lang.String mPkgName = mContext.getPackageName();
        java.lang.String otherPkgName = "com.android.frameworks.coretests.keysets_api";
        android.content.pm.KeySet ks;
        try {
            ks = pm.getSigningKeySet(null);
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            ks = pm.getSigningKeySet("keysets.test.bogus.package");
            assertTrue(false);// should have thrown

        } catch (java.lang.IllegalArgumentException e) {
        }
        installFromRawResource("keysetApi.apk", R.raw.keyset_splat_api, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        try {
            ks = pm.getSigningKeySet(otherPkgName);
            assertTrue(false);// should have thrown

        } catch (java.lang.SecurityException e) {
        }
        cleanUpInstall(otherPkgName);
        ks = pm.getSigningKeySet(mContext.getPackageName());
        assertNotNull(ks);
    }

    /* testGetKeySetByAlias - same as getSigningKeySet, but for keysets defined
    by this package.
     */
    public void testGetKeySetByAlias() throws java.lang.Exception {
        android.content.pm.PackageManager pm = getPm();
        java.lang.String mPkgName = mContext.getPackageName();
        java.lang.String otherPkgName = "com.android.frameworks.coretests.keysets_api";
        android.content.pm.KeySet ks;
        try {
            ks = pm.getKeySetByAlias(null, null);
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            ks = pm.getKeySetByAlias(null, "keysetBogus");
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            ks = pm.getKeySetByAlias("keysets.test.bogus.package", null);
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            ks = pm.getKeySetByAlias("keysets.test.bogus.package", "A");
            assertTrue(false);// should have thrown

        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            ks = pm.getKeySetByAlias(mPkgName, "keysetBogus");
            assertTrue(false);// should have thrown

        } catch (java.lang.IllegalArgumentException e) {
        }
        installFromRawResource("keysetApi.apk", R.raw.keyset_splat_api, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        try {
            ks = pm.getKeySetByAlias(otherPkgName, "A");
            assertTrue(false);// should have thrown

        } catch (java.lang.SecurityException e) {
        }
        cleanUpInstall(otherPkgName);
        ks = pm.getKeySetByAlias(mPkgName, "A");
        assertNotNull(ks);
    }

    public void testIsSignedBy() throws java.lang.Exception {
        android.content.pm.PackageManager pm = getPm();
        java.lang.String mPkgName = mContext.getPackageName();
        java.lang.String otherPkgName = "com.android.frameworks.coretests.keysets_api";
        android.content.pm.KeySet mSigningKS = pm.getSigningKeySet(mPkgName);
        android.content.pm.KeySet mDefinedKS = pm.getKeySetByAlias(mPkgName, "A");
        try {
            assertFalse(pm.isSignedBy(null, null));
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            assertFalse(pm.isSignedBy(null, mSigningKS));
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            assertFalse(pm.isSignedBy(mPkgName, null));
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            assertFalse(pm.isSignedBy("keysets.test.bogus.package", mDefinedKS));
        } catch (java.lang.IllegalArgumentException e) {
        }
        assertFalse(pm.isSignedBy(mPkgName, mDefinedKS));
        assertFalse(pm.isSignedBy(mPkgName, new android.content.pm.KeySet(new android.os.Binder())));
        assertTrue(pm.isSignedBy(mPkgName, mSigningKS));
        installFromRawResource("keysetApi.apk", R.raw.keyset_splat_api, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        assertFalse(pm.isSignedBy(otherPkgName, mDefinedKS));
        assertTrue(pm.isSignedBy(otherPkgName, mSigningKS));
        cleanUpInstall(otherPkgName);
        installFromRawResource("keysetApi.apk", R.raw.keyset_splata_api, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        assertTrue(pm.isSignedBy(otherPkgName, mDefinedKS));
        assertTrue(pm.isSignedBy(otherPkgName, mSigningKS));
        cleanUpInstall(otherPkgName);
    }

    public void testIsSignedByExactly() throws java.lang.Exception {
        android.content.pm.PackageManager pm = getPm();
        java.lang.String mPkgName = mContext.getPackageName();
        java.lang.String otherPkgName = "com.android.frameworks.coretests.keysets_api";
        android.content.pm.KeySet mSigningKS = pm.getSigningKeySet(mPkgName);
        android.content.pm.KeySet mDefinedKS = pm.getKeySetByAlias(mPkgName, "A");
        try {
            assertFalse(pm.isSignedBy(null, null));
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            assertFalse(pm.isSignedBy(null, mSigningKS));
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            assertFalse(pm.isSignedBy(mPkgName, null));
            assertTrue(false);// should have thrown

        } catch (java.lang.NullPointerException e) {
        }
        try {
            assertFalse(pm.isSignedByExactly("keysets.test.bogus.package", mDefinedKS));
        } catch (java.lang.IllegalArgumentException e) {
        }
        assertFalse(pm.isSignedByExactly(mPkgName, mDefinedKS));
        assertFalse(pm.isSignedByExactly(mPkgName, new android.content.pm.KeySet(new android.os.Binder())));
        assertTrue(pm.isSignedByExactly(mPkgName, mSigningKS));
        installFromRawResource("keysetApi.apk", R.raw.keyset_splat_api, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        assertFalse(pm.isSignedByExactly(otherPkgName, mDefinedKS));
        assertTrue(pm.isSignedByExactly(otherPkgName, mSigningKS));
        cleanUpInstall(otherPkgName);
        installFromRawResource("keysetApi.apk", R.raw.keyset_splata_api, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        assertFalse(pm.isSignedByExactly(otherPkgName, mDefinedKS));
        assertFalse(pm.isSignedByExactly(otherPkgName, mSigningKS));
        cleanUpInstall(otherPkgName);
    }

    /**
     * The following tests are related to testing the checkSignatures api.
     */
    private void checkSignatures(int apk1, int apk2, int expMatchResult) throws java.lang.Exception {
        checkSharedSignatures(apk1, apk2, true, false, -1, expMatchResult);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesAllMatch() throws java.lang.Exception {
        int apk1 = APP1_CERT1_CERT2;
        int apk2 = APP2_CERT1_CERT2;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_MATCH);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesNoMatch() throws java.lang.Exception {
        int apk1 = APP1_CERT1;
        int apk2 = APP2_CERT2;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_NO_MATCH);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSomeMatch1() throws java.lang.Exception {
        int apk1 = APP1_CERT1_CERT2;
        int apk2 = APP2_CERT1;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_NO_MATCH);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSomeMatch2() throws java.lang.Exception {
        int apk1 = APP1_CERT1_CERT2;
        int apk2 = APP2_CERT2;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_NO_MATCH);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesMoreMatch() throws java.lang.Exception {
        int apk1 = APP1_CERT1;
        int apk2 = APP2_CERT1_CERT2;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_NO_MATCH);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesUnknown() throws java.lang.Exception {
        int apk1 = APP1_CERT1_CERT2;
        int apk2 = APP2_CERT1_CERT2;
        java.lang.String apk1Name = "install1.apk";
        java.lang.String apk2Name = "install2.apk";
        android.content.pm.PackageManagerTests.InstallParams ip1 = null;
        try {
            ip1 = installFromRawResource(apk1Name, apk1, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            android.content.pm.PackageManager pm = mContext.getPackageManager();
            // Delete app2
            java.io.File filesDir = mContext.getFilesDir();
            java.io.File outFile = new java.io.File(filesDir, apk2Name);
            int rawResId = apk2;
            android.net.Uri packageURI = getInstallablePackage(rawResId, outFile);
            android.content.pm.PackageParser.Package pkg = parsePackage(packageURI);
            getPm().deletePackage(pkg.packageName, null, android.content.pm.PackageManager.DELETE_ALL_USERS);
            // Check signatures now
            int match = checkSignatures(ip1.pkg.packageName, pkg.packageName);
            assertEquals(android.content.pm.PackageManager.SIGNATURE_UNKNOWN_PACKAGE, match);
        } finally {
            if (ip1 != null) {
                cleanUpInstall(ip1);
            }
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallNoCertificates() throws java.lang.Exception {
        int apk1 = APP1_UNSIGNED;
        java.lang.String apk1Name = "install1.apk";
        android.content.pm.PackageManagerTests.InstallParams ip1 = null;
        try {
            installFromRawResource(apk1Name, apk1, 0, false, true, android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
        } finally {
        }
    }

    /* The following tests are related to apps using shared uids signed with
    different certs.
     */
    private int SHARED1_UNSIGNED = R.raw.install_shared1_unsigned;

    private int SHARED1_CERT1 = R.raw.install_shared1_cert1;

    private int SHARED1_CERT2 = R.raw.install_shared1_cert2;

    private int SHARED1_CERT1_CERT2 = R.raw.install_shared1_cert1_cert2;

    private int SHARED2_UNSIGNED = R.raw.install_shared2_unsigned;

    private int SHARED2_CERT1 = R.raw.install_shared2_cert1;

    private int SHARED2_CERT2 = R.raw.install_shared2_cert2;

    private int SHARED2_CERT1_CERT2 = R.raw.install_shared2_cert1_cert2;

    private void checkSharedSignatures(int apk1, int apk2, boolean cleanUp, boolean fail, int retCode, int expMatchResult) throws java.lang.Exception {
        java.lang.String apk1Name = "install1.apk";
        java.lang.String apk2Name = "install2.apk";
        android.content.pm.PackageParser.Package pkg1 = getParsedPackage(apk1Name, apk1);
        android.content.pm.PackageParser.Package pkg2 = getParsedPackage(apk2Name, apk2);
        try {
            // Clean up before testing first.
            cleanUpInstall(pkg1.packageName);
            cleanUpInstall(pkg2.packageName);
            installFromRawResource(apk1Name, apk1, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            if (fail) {
                installFromRawResource(apk2Name, apk2, 0, false, true, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            } else {
                installFromRawResource(apk2Name, apk2, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
                int match = checkSignatures(pkg1.packageName, pkg2.packageName);
                assertEquals(expMatchResult, match);
            }
        } finally {
            if (cleanUp) {
                cleanUpInstall(pkg1.packageName);
                cleanUpInstall(pkg2.packageName);
            }
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSharedAllMatch() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1_CERT2;
        int apk2 = SHARED2_CERT1_CERT2;
        boolean fail = false;
        int retCode = -1;
        int expMatchResult = android.content.pm.PackageManager.SIGNATURE_MATCH;
        checkSharedSignatures(apk1, apk2, true, fail, retCode, expMatchResult);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSharedNoMatch() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT2;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
        int expMatchResult = -1;
        checkSharedSignatures(apk1, apk2, true, fail, retCode, expMatchResult);
    }

    /* Test that an app signed with cert1 and cert2 cannot be replaced when
    signed with cert1 alone.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSharedSomeMatch1() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1_CERT2;
        int apk2 = SHARED2_CERT1;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
        int expMatchResult = -1;
        checkSharedSignatures(apk1, apk2, true, fail, retCode, expMatchResult);
    }

    /* Test that an app signed with cert1 and cert2 cannot be replaced when
    signed with cert2 alone.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSharedSomeMatch2() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1_CERT2;
        int apk2 = SHARED2_CERT2;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
        int expMatchResult = -1;
        checkSharedSignatures(apk1, apk2, true, fail, retCode, expMatchResult);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCheckSignaturesSharedUnknown() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1_CERT2;
        int apk2 = SHARED2_CERT1_CERT2;
        java.lang.String apk1Name = "install1.apk";
        java.lang.String apk2Name = "install2.apk";
        android.content.pm.PackageManagerTests.InstallParams ip1 = null;
        try {
            ip1 = installFromRawResource(apk1Name, apk1, 0, false, false, -1, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
            android.content.pm.PackageManager pm = mContext.getPackageManager();
            // Delete app2
            android.content.pm.PackageParser.Package pkg = getParsedPackage(apk2Name, apk2);
            getPm().deletePackage(pkg.packageName, null, android.content.pm.PackageManager.DELETE_ALL_USERS);
            // Check signatures now
            int match = checkSignatures(ip1.pkg.packageName, pkg.packageName);
            assertEquals(android.content.pm.PackageManager.SIGNATURE_UNKNOWN_PACKAGE, match);
        } finally {
            if (ip1 != null) {
                cleanUpInstall(ip1);
            }
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFirstSharedMatchAllCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT1;
        int rapk1 = SHARED1_CERT1;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_MATCH);
        replaceCerts(apk1, rapk1, true, false, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceSecondSharedMatchAllCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT1;
        int rapk2 = SHARED2_CERT1;
        checkSignatures(apk1, apk2, android.content.pm.PackageManager.SIGNATURE_MATCH);
        replaceCerts(apk2, rapk2, true, false, -1);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFirstSharedMatchSomeCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1_CERT2;
        int apk2 = SHARED2_CERT1_CERT2;
        int rapk1 = SHARED1_CERT1;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        checkSharedSignatures(apk1, apk2, false, false, -1, android.content.pm.PackageManager.SIGNATURE_MATCH);
        installFromRawResource("install.apk", rapk1, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, true, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceSecondSharedMatchSomeCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1_CERT2;
        int apk2 = SHARED2_CERT1_CERT2;
        int rapk2 = SHARED2_CERT1;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        checkSharedSignatures(apk1, apk2, false, false, -1, android.content.pm.PackageManager.SIGNATURE_MATCH);
        installFromRawResource("install.apk", rapk2, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, true, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFirstSharedMatchNoCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT1;
        int rapk1 = SHARED1_CERT2;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        checkSharedSignatures(apk1, apk2, false, false, -1, android.content.pm.PackageManager.SIGNATURE_MATCH);
        installFromRawResource("install.apk", rapk1, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, true, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceSecondSharedMatchNoCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT1;
        int rapk2 = SHARED2_CERT2;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        checkSharedSignatures(apk1, apk2, false, false, -1, android.content.pm.PackageManager.SIGNATURE_MATCH);
        installFromRawResource("install.apk", rapk2, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, true, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceFirstSharedMatchMoreCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT1;
        int rapk1 = SHARED1_CERT1_CERT2;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        checkSharedSignatures(apk1, apk2, false, false, -1, android.content.pm.PackageManager.SIGNATURE_MATCH);
        installFromRawResource("install.apk", rapk1, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, true, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testReplaceSecondSharedMatchMoreCerts() throws java.lang.Exception {
        int apk1 = SHARED1_CERT1;
        int apk2 = SHARED2_CERT1;
        int rapk2 = SHARED2_CERT1_CERT2;
        boolean fail = true;
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        checkSharedSignatures(apk1, apk2, false, false, -1, android.content.pm.PackageManager.SIGNATURE_MATCH);
        installFromRawResource("install.apk", rapk2, android.content.pm.PackageManager.INSTALL_REPLACE_EXISTING, true, fail, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    /**
     * Unknown features should be allowed to install. This prevents older phones
     * from rejecting new packages that specify features that didn't exist when
     * an older phone existed. All older phones are assumed to have those
     * features.
     * <p>
     * Right now we allow all packages to be installed regardless of their
     * features.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testUsesFeatureUnknownFeature() throws java.lang.Exception {
        int retCode = android.content.pm.PackageManager.INSTALL_SUCCEEDED;
        installFromRawResource("install.apk", R.raw.install_uses_feature, 0, true, false, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testInstallNonexistentFile() throws java.lang.Exception {
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_INVALID_URI;
        java.io.File invalidFile = new java.io.File("/nonexistent-file.apk");
        invokeInstallPackageFail(android.net.Uri.fromFile(invalidFile), 0, retCode);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetVerifierDeviceIdentity() throws java.lang.Exception {
        android.content.pm.PackageManager pm = getPm();
        android.content.pm.VerifierDeviceIdentity id = pm.getVerifierDeviceIdentity();
        assertNotNull("Verifier device identity should not be null", id);
    }

    public void testGetInstalledPackages() throws java.lang.Exception {
        java.util.List<android.content.pm.PackageInfo> packages = getPm().getInstalledPackages(0);
        assertNotNull("installed packages cannot be null", packages);
        assertTrue("installed packages cannot be empty", packages.size() > 0);
    }

    public void testGetUnInstalledPackages() throws java.lang.Exception {
        java.util.List<android.content.pm.PackageInfo> packages = getPm().getInstalledPackages(android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        assertNotNull("installed packages cannot be null", packages);
        assertTrue("installed packages cannot be empty", packages.size() > 0);
    }

    /**
     * Test that getInstalledPackages returns all the data specified in flags.
     */
    public void testGetInstalledPackagesAll() throws java.lang.Exception {
        int flags = ((((((((android.content.pm.PackageManager.GET_ACTIVITIES | android.content.pm.PackageManager.GET_GIDS) | android.content.pm.PackageManager.GET_CONFIGURATIONS) | android.content.pm.PackageManager.GET_INSTRUMENTATION) | android.content.pm.PackageManager.GET_PERMISSIONS) | android.content.pm.PackageManager.GET_PROVIDERS) | android.content.pm.PackageManager.GET_RECEIVERS) | android.content.pm.PackageManager.GET_SERVICES) | android.content.pm.PackageManager.GET_SIGNATURES) | android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;
        java.util.List<android.content.pm.PackageInfo> packages = getPm().getInstalledPackages(flags);
        assertNotNull("installed packages cannot be null", packages);
        assertTrue("installed packages cannot be empty", packages.size() > 0);
        android.content.pm.PackageInfo packageInfo = null;
        // Find the package with all components specified in the AndroidManifest
        // to ensure no null values
        for (android.content.pm.PackageInfo pi : packages) {
            if ("com.android.frameworks.coretests.install_complete_package_info".equals(pi.packageName)) {
                packageInfo = pi;
                break;
            }
        }
        assertNotNull("activities should not be null", packageInfo.activities);
        assertNotNull("configPreferences should not be null", packageInfo.configPreferences);
        assertNotNull("instrumentation should not be null", packageInfo.instrumentation);
        assertNotNull("permissions should not be null", packageInfo.permissions);
        assertNotNull("providers should not be null", packageInfo.providers);
        assertNotNull("receivers should not be null", packageInfo.receivers);
        assertNotNull("services should not be null", packageInfo.services);
        assertNotNull("signatures should not be null", packageInfo.signatures);
    }

    /**
     * Test that getInstalledPackages returns all the data specified in
     * flags when the GET_UNINSTALLED_PACKAGES flag is set.
     */
    public void testGetUnInstalledPackagesAll() throws java.lang.Exception {
        int flags = (((((((((android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES | android.content.pm.PackageManager.GET_ACTIVITIES) | android.content.pm.PackageManager.GET_GIDS) | android.content.pm.PackageManager.GET_CONFIGURATIONS) | android.content.pm.PackageManager.GET_INSTRUMENTATION) | android.content.pm.PackageManager.GET_PERMISSIONS) | android.content.pm.PackageManager.GET_PROVIDERS) | android.content.pm.PackageManager.GET_RECEIVERS) | android.content.pm.PackageManager.GET_SERVICES) | android.content.pm.PackageManager.GET_SIGNATURES) | android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;
        java.util.List<android.content.pm.PackageInfo> packages = getPm().getInstalledPackages(flags);
        assertNotNull("installed packages cannot be null", packages);
        assertTrue("installed packages cannot be empty", packages.size() > 0);
        android.content.pm.PackageInfo packageInfo = null;
        // Find the package with all components specified in the AndroidManifest
        // to ensure no null values
        for (android.content.pm.PackageInfo pi : packages) {
            if ("com.android.frameworks.coretests.install_complete_package_info".equals(pi.packageName)) {
                packageInfo = pi;
                break;
            }
        }
        assertNotNull("activities should not be null", packageInfo.activities);
        assertNotNull("configPreferences should not be null", packageInfo.configPreferences);
        assertNotNull("instrumentation should not be null", packageInfo.instrumentation);
        assertNotNull("permissions should not be null", packageInfo.permissions);
        assertNotNull("providers should not be null", packageInfo.providers);
        assertNotNull("receivers should not be null", packageInfo.receivers);
        assertNotNull("services should not be null", packageInfo.services);
        assertNotNull("signatures should not be null", packageInfo.signatures);
    }

    public void testInstall_BadDex_CleanUp() throws java.lang.Exception {
        int retCode = android.content.pm.PackageManager.INSTALL_FAILED_DEXOPT;
        installFromRawResource("install.apk", R.raw.install_bad_dex, 0, true, true, retCode, android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED);
    }
}

