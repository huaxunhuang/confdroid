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


public class AppCacheTest extends android.test.AndroidTestCase {
    private static final boolean localLOGV = false;

    public static final java.lang.String TAG = "AppCacheTest";

    public final long MAX_WAIT_TIME = 60 * 1000;

    public final long WAIT_TIME_INCR = 10 * 1000;

    private static final long THRESHOLD = 5;

    private static final long ACTUAL_THRESHOLD = 10;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        if (android.content.pm.AppCacheTest.localLOGV)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Cleaning up cache directory first");

        cleanUpCacheDirectory();
    }

    void cleanUpDirectory(java.io.File pDir, java.lang.String dirName) {
        java.io.File testDir = new java.io.File(pDir, dirName);
        if (!testDir.exists()) {
            return;
        }
        java.lang.String[] fList = testDir.list();
        for (int i = 0; i < fList.length; i++) {
            java.io.File file = new java.io.File(testDir, fList[i]);
            if (file.isDirectory()) {
                cleanUpDirectory(testDir, fList[i]);
            } else {
                file.delete();
            }
        }
        testDir.delete();
    }

    void cleanUpCacheDirectory() {
        java.io.File testDir = mContext.getCacheDir();
        if (!testDir.exists()) {
            return;
        }
        java.lang.String[] fList = testDir.list();
        if (fList == null) {
            testDir.delete();
            return;
        }
        for (int i = 0; i < fList.length; i++) {
            java.io.File file = new java.io.File(testDir, fList[i]);
            if (file.isDirectory()) {
                cleanUpDirectory(testDir, fList[i]);
            } else {
                file.delete();
            }
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testDeleteAllCacheFiles() {
        java.lang.String testName = "testDeleteAllCacheFiles";
        cleanUpCacheDirectory();
    }

    void failStr(java.lang.String errMsg) {
        android.util.Log.w(android.content.pm.AppCacheTest.TAG, "errMsg=" + errMsg);
        fail(errMsg);
    }

    void failStr(java.lang.Exception e) {
        android.util.Log.w(android.content.pm.AppCacheTest.TAG, "e.getMessage=" + e.getMessage());
        android.util.Log.w(android.content.pm.AppCacheTest.TAG, "e=" + e);
    }

    long getFreeStorageBlks(android.os.StatFs st) {
        st.restat("/data");
        return st.getFreeBlocks();
    }

    long getFreeStorageSize(android.os.StatFs st) {
        st.restat("/data");
        return ((long) (st.getFreeBlocks())) * ((long) (st.getBlockSize()));
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testFreeApplicationCacheAllFiles() throws java.lang.Exception {
        boolean TRACKING = true;
        android.os.StatFs st = new android.os.StatFs("/data");
        long blks1 = getFreeStorageBlks(st);
        long availableMem = getFreeStorageSize(st);
        java.io.File cacheDir = mContext.getCacheDir();
        assertNotNull(cacheDir);
        createTestFiles1(cacheDir, "testtmpdir", 5);
        long blks2 = getFreeStorageBlks(st);
        if (android.content.pm.AppCacheTest.localLOGV || TRACKING)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("blk1=" + blks1) + ", blks2=") + blks2);

        // this should free up the test files that were created earlier
        if (!invokePMFreeApplicationCache(availableMem)) {
            fail("Could not successfully invoke PackageManager free app cache API");
        }
        long blks3 = getFreeStorageBlks(st);
        if (android.content.pm.AppCacheTest.localLOGV || TRACKING)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "blks3=" + blks3);

        verifyTestFiles1(cacheDir, "testtmpdir", 5);
    }

    public void testFreeApplicationCacheSomeFiles() throws java.lang.Exception {
        android.os.StatFs st = new android.os.StatFs("/data");
        long blks1 = getFreeStorageBlks(st);
        java.io.File cacheDir = mContext.getCacheDir();
        assertNotNull(cacheDir);
        createTestFiles1(cacheDir, "testtmpdir", 5);
        long blks2 = getFreeStorageBlks(st);
        android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("blk1=" + blks1) + ", blks2=") + blks2);
        long diff = (blks1 - blks2) - 2;
        if (!invokePMFreeApplicationCache(diff * st.getBlockSize())) {
            fail("Could not successfully invoke PackageManager free app cache API");
        }
        long blks3 = getFreeStorageBlks(st);
        // blks3 should be greater than blks2 and less than blks1
        if (!((blks3 <= blks1) && (blks3 >= blks2))) {
            failStr((("Expected " + (blks1 - blks2)) + " number of blocks to be freed but freed only ") + (blks1 - blks3));
        }
    }

    /**
     * This method opens an output file writes to it, opens the same file as an input
     * stream, reads the contents and verifies the data that was written earlier can be read
     */
    public void openOutFileInAppFilesDir(java.io.File pFile, java.lang.String pFileOut) {
        java.io.FileOutputStream fos = null;
        try {
            fos = new java.io.FileOutputStream(pFile);
        } catch (java.io.FileNotFoundException e1) {
            failStr("Error when opening file " + e1);
            return;
        }
        try {
            fos.write(pFileOut.getBytes());
            fos.close();
        } catch (java.io.FileNotFoundException e) {
            failStr(e.getMessage());
        } catch (java.io.IOException e) {
            failStr(e.getMessage());
        }
        int count = pFileOut.getBytes().length;
        byte[] buffer = new byte[count];
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(pFile);
            fis.read(buffer, 0, count);
            fis.close();
        } catch (java.io.FileNotFoundException e) {
            failStr("Failed when verifing output opening file " + e.getMessage());
        } catch (java.io.IOException e) {
            failStr("Failed when verifying output, reading from written file " + e);
        }
        java.lang.String str = new java.lang.String(buffer);
        assertEquals(str, pFileOut);
    }

    /* This test case verifies that output written to a file
    using Context.openFileOutput has executed successfully.
    The operation is verified by invoking Context.openFileInput
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testAppFilesCreateFile() {
        java.lang.String fileName = "testFile1.txt";
        java.lang.String fileOut = "abcdefghijklmnopqrstuvwxyz";
        android.content.Context con = super.getContext();
        try {
            java.io.FileOutputStream fos = con.openFileOutput(fileName, android.content.Context.MODE_PRIVATE);
            fos.close();
        } catch (java.io.FileNotFoundException e) {
            failStr(e);
        } catch (java.io.IOException e) {
            failStr(e);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testAppCacheCreateFile() {
        java.lang.String fileName = "testFile1.txt";
        java.lang.String fileOut = "abcdefghijklmnopqrstuvwxyz";
        android.content.Context con = super.getContext();
        java.io.File file = new java.io.File(con.getCacheDir(), fileName);
        openOutFileInAppFilesDir(file, fileOut);
        cleanUpCacheDirectory();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAppCreateCacheFiles() {
        java.io.File cacheDir = mContext.getCacheDir();
        java.lang.String testDirName = "testtmp";
        java.io.File testTmpDir = new java.io.File(cacheDir, testDirName);
        testTmpDir.mkdir();
        int numDirs = 3;
        java.io.File[] fileArr = new java.io.File[numDirs];
        for (int i = 0; i < numDirs; i++) {
            fileArr[i] = new java.io.File(testTmpDir, "dir" + (i + 1));
            fileArr[i].mkdir();
        }
        byte[] buffer = getBuffer();
        android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Size of bufer=" + buffer.length);
        for (int i = 0; i < numDirs; i++) {
            for (int j = 1; j <= i; j++) {
                java.io.File file1 = new java.io.File(fileArr[i], ("testFile" + j) + ".txt");
                java.io.FileOutputStream fos = null;
                try {
                    fos = new java.io.FileOutputStream(file1);
                    for (int k = 1; k < 10; k++) {
                        fos.write(buffer);
                    }
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "wrote 10K bytes to " + file1);
                    fos.close();
                } catch (java.io.FileNotFoundException e) {
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Excetion =" + e);
                    fail("Error when creating outputstream " + e);
                } catch (java.io.IOException e) {
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Excetion =" + e);
                    fail("Error when writing output " + e);
                }
            }
        }
    }

    byte[] getBuffer() {
        java.lang.String sbuffer = "a";
        for (int i = 0; i < 10; i++) {
            sbuffer += sbuffer;
        }
        return sbuffer.getBytes();
    }

    long getFileNumBlocks(long fileSize, long blkSize) {
        long ret = fileSize / blkSize;
        if ((ret * blkSize) < fileSize) {
            ret++;
        }
        return ret;
    }

    // @LargeTest
    public void testAppCacheClear() {
        java.lang.String dataDir = "/data/data";
        android.os.StatFs st = new android.os.StatFs(dataDir);
        long blkSize = st.getBlockSize();
        long totBlks = st.getBlockCount();
        long availableBlks = st.getFreeBlocks();
        long thresholdBlks = (totBlks * android.content.pm.AppCacheTest.THRESHOLD) / 100L;
        java.lang.String testDirName = "testdir";
        // create directory in cache
        java.io.File testDir = new java.io.File(mContext.getCacheDir(), testDirName);
        testDir.mkdirs();
        byte[] buffer = getBuffer();
        int i = 1;
        if (android.content.pm.AppCacheTest.localLOGV)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("availableBlks=" + availableBlks) + ", thresholdBlks=") + thresholdBlks);

        long createdFileBlks = 0;
        int imax = 300;
        while ((availableBlks > thresholdBlks) && (i < imax)) {
            java.io.File testFile = new java.io.File(testDir, ("testFile" + i) + ".txt");
            if (android.content.pm.AppCacheTest.localLOGV)
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("Creating " + i) + "th test file ") + testFile);

            int jmax = i;
            i++;
            java.io.FileOutputStream fos;
            try {
                fos = new java.io.FileOutputStream(testFile);
            } catch (java.io.FileNotFoundException e) {
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Failed creating test file:" + testFile);
                continue;
            }
            boolean err = false;
            for (int j = 1; j <= jmax; j++) {
                try {
                    fos.write(buffer);
                } catch (java.io.IOException e) {
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Failed to write to file:" + testFile);
                    err = true;
                }
            }
            try {
                fos.close();
            } catch (java.io.IOException e) {
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Failed closing file:" + testFile);
            }
            if (err) {
                continue;
            }
            createdFileBlks += getFileNumBlocks(testFile.length(), blkSize);
            st.restat(dataDir);
            availableBlks = st.getFreeBlocks();
        } 
        st.restat(dataDir);
        long availableBytes = st.getFreeBlocks() * blkSize;
        long shouldFree = (android.content.pm.AppCacheTest.ACTUAL_THRESHOLD - android.content.pm.AppCacheTest.THRESHOLD) * totBlks;
        // would have run out of memory
        // wait for some time and confirm cache is deleted
        try {
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Sleeping for 2 minutes...");
            java.lang.Thread.sleep((2 * 60) * 1000);
        } catch (java.lang.InterruptedException e) {
            fail("Exception when sleeping " + e);
        }
        boolean removedFlag = false;
        long existingFileBlks = 0;
        for (int k = 1; k < i; k++) {
            java.io.File testFile = new java.io.File(testDir, ("testFile" + k) + ".txt");
            if (!testFile.exists()) {
                removedFlag = true;
                if (android.content.pm.AppCacheTest.localLOGV)
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, testFile + " removed");

            } else {
                existingFileBlks += getFileNumBlocks(testFile.length(), blkSize);
            }
        }
        if (android.content.pm.AppCacheTest.localLOGV)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("createdFileBlks=" + createdFileBlks) + ", existingFileBlks=") + existingFileBlks);

        long fileSize = createdFileBlks - existingFileBlks;
        // verify fileSize number of bytes have been cleared from cache
        if (android.content.pm.AppCacheTest.localLOGV)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("deletedFileBlks=" + fileSize) + " shouldFreeBlks=") + shouldFree);

        if ((fileSize > (shouldFree - blkSize)) && (fileSize < (shouldFree + blkSize))) {
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "passed");
        }
        assertTrue("Files should have been removed", removedFlag);
    }

    // createTestFiles(new File(super.getContext().getCacheDir(), "testtmp", "dir", 3)
    void createTestFiles1(java.io.File cacheDir, java.lang.String testFilePrefix, int numTestFiles) {
        byte[] buffer = getBuffer();
        for (int i = 0; i < numTestFiles; i++) {
            java.io.File file1 = new java.io.File(cacheDir, (testFilePrefix + i) + ".txt");
            java.io.FileOutputStream fos = null;
            try {
                fos = new java.io.FileOutputStream(file1);
                for (int k = 1; k < 10; k++) {
                    fos.write(buffer);
                }
                fos.close();
            } catch (java.io.FileNotFoundException e) {
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Exception =" + e);
                fail("Error when creating outputstream " + e);
            } catch (java.io.IOException e) {
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Exception =" + e);
                fail("Error when writing output " + e);
            }
            try {
                // introduce sleep for 1 s to avoid common time stamps for files being created
                java.lang.Thread.sleep(1000);
            } catch (java.lang.InterruptedException e) {
                fail("Exception when sleeping " + e);
            }
        }
    }

    void verifyTestFiles1(java.io.File cacheDir, java.lang.String testFilePrefix, int numTestFiles) {
        java.util.List<java.lang.String> files = new java.util.ArrayList<java.lang.String>();
        for (int i = 0; i < numTestFiles; i++) {
            java.io.File file1 = new java.io.File(cacheDir, (testFilePrefix + i) + ".txt");
            if (file1.exists()) {
                files.add(file1.getName());
            }
        }
        if (files.size() > 0) {
            fail("Files should have been deleted: " + java.util.Arrays.toString(files.toArray(new java.lang.String[files.size()])));
        }
    }

    void createTestFiles2(java.io.File cacheDir, java.lang.String rootTestDirName, java.lang.String subDirPrefix, int numDirs, java.lang.String testFilePrefix) {
        android.content.Context con = super.getContext();
        java.io.File testTmpDir = new java.io.File(cacheDir, rootTestDirName);
        testTmpDir.mkdir();
        java.io.File[] fileArr = new java.io.File[numDirs];
        for (int i = 0; i < numDirs; i++) {
            fileArr[i] = new java.io.File(testTmpDir, subDirPrefix + (i + 1));
            fileArr[i].mkdir();
        }
        byte[] buffer = getBuffer();
        for (int i = 0; i < numDirs; i++) {
            for (int j = 1; j <= i; j++) {
                java.io.File file1 = new java.io.File(fileArr[i], (testFilePrefix + j) + ".txt");
                java.io.FileOutputStream fos = null;
                try {
                    fos = new java.io.FileOutputStream(file1);
                    for (int k = 1; k < 10; k++) {
                        fos.write(buffer);
                    }
                    fos.close();
                } catch (java.io.FileNotFoundException e) {
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Exception =" + e);
                    fail("Error when creating outputstream " + e);
                } catch (java.io.IOException e) {
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Exception =" + e);
                    fail("Error when writing output " + e);
                }
                try {
                    // introduce sleep for 10 ms to avoid common time stamps for files being created
                    java.lang.Thread.sleep(10);
                } catch (java.lang.InterruptedException e) {
                    fail("Exception when sleeping " + e);
                }
            }
        }
    }

    class PackageDataObserver extends android.content.pm.IPackageDataObserver.Stub {
        public boolean retValue = false;

        private boolean doneFlag = false;

        public void onRemoveCompleted(java.lang.String packageName, boolean succeeded) throws android.os.RemoteException {
            synchronized(this) {
                retValue = succeeded;
                doneFlag = true;
                notifyAll();
            }
        }

        public boolean isDone() {
            return doneFlag;
        }
    }

    android.content.pm.IPackageManager getPm() {
        return IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
    }

    boolean invokePMDeleteAppCacheFiles() throws java.lang.Exception {
        try {
            java.lang.String packageName = mContext.getPackageName();
            android.content.pm.AppCacheTest.PackageDataObserver observer = new android.content.pm.AppCacheTest.PackageDataObserver();
            // wait on observer
            synchronized(observer) {
                getPm().deleteApplicationCacheFiles(packageName, observer);
                long waitTime = 0;
                while ((!observer.isDone()) || (waitTime > MAX_WAIT_TIME)) {
                    observer.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!observer.isDone()) {
                    throw new java.lang.Exception("timed out waiting for PackageDataObserver.onRemoveCompleted");
                }
            }
            return observer.retValue;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "Failed to get handle for PackageManger Exception: " + e);
            return false;
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "InterruptedException :" + e);
            return false;
        }
    }

    boolean invokePMFreeApplicationCache(long idealStorageSize) throws java.lang.Exception {
        try {
            java.lang.String packageName = mContext.getPackageName();
            android.content.pm.AppCacheTest.PackageDataObserver observer = new android.content.pm.AppCacheTest.PackageDataObserver();
            // wait on observer
            synchronized(observer) {
                getPm().freeStorageAndNotify(null, idealStorageSize, observer);
                long waitTime = 0;
                while ((!observer.isDone()) || (waitTime > MAX_WAIT_TIME)) {
                    observer.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!observer.isDone()) {
                    throw new java.lang.Exception("timed out waiting for PackageDataObserver.onRemoveCompleted");
                }
            }
            return observer.retValue;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "Failed to get handle for PackageManger Exception: " + e);
            return false;
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "InterruptedException :" + e);
            return false;
        }
    }

    boolean invokePMFreeStorage(long idealStorageSize, android.content.pm.AppCacheTest.FreeStorageReceiver r, android.app.PendingIntent pi) throws java.lang.Exception {
        try {
            // Spin lock waiting for call back
            synchronized(r) {
                getPm().freeStorage(null, idealStorageSize, pi.getIntentSender());
                long waitTime = 0;
                while ((!r.isDone()) && (waitTime < MAX_WAIT_TIME)) {
                    r.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!r.isDone()) {
                    throw new java.lang.Exception("timed out waiting for call back from PendingIntent");
                }
            }
            return r.getResultCode() == 1;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "Failed to get handle for PackageManger Exception: " + e);
            return false;
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "InterruptedException :" + e);
            return false;
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testDeleteAppCacheFiles() throws java.lang.Exception {
        java.lang.String testName = "testDeleteAppCacheFiles";
        java.io.File cacheDir = mContext.getCacheDir();
        createTestFiles1(cacheDir, "testtmpdir", 5);
        assertTrue(invokePMDeleteAppCacheFiles());
        // confirm files dont exist
        verifyTestFiles1(cacheDir, "testtmpdir", 5);
    }

    class PackageStatsObserver extends android.content.pm.IPackageStatsObserver.Stub {
        public boolean retValue = false;

        public android.content.pm.PackageStats stats;

        private boolean doneFlag = false;

        public void onGetStatsCompleted(android.content.pm.PackageStats pStats, boolean succeeded) throws android.os.RemoteException {
            synchronized(this) {
                retValue = succeeded;
                stats = pStats;
                doneFlag = true;
                notifyAll();
            }
        }

        public boolean isDone() {
            return doneFlag;
        }
    }

    public android.content.pm.PackageStats invokePMGetPackageSizeInfo() throws java.lang.Exception {
        try {
            java.lang.String packageName = mContext.getPackageName();
            android.content.pm.AppCacheTest.PackageStatsObserver observer = new android.content.pm.AppCacheTest.PackageStatsObserver();
            // wait on observer
            synchronized(observer) {
                getPm().getPackageSizeInfo(packageName, android.os.UserHandle.myUserId(), observer);
                long waitTime = 0;
                while ((!observer.isDone()) || (waitTime > MAX_WAIT_TIME)) {
                    observer.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!observer.isDone()) {
                    throw new java.lang.Exception("Timed out waiting for PackageStatsObserver.onGetStatsCompleted");
                }
            }
            if (android.content.pm.AppCacheTest.localLOGV)
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, (((("OBSERVER RET VALUES code=" + observer.stats.codeSize) + ", data=") + observer.stats.dataSize) + ", cache=") + observer.stats.cacheSize);

            return observer.stats;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "Failed to get handle for PackageManger Exception: " + e);
            return null;
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "InterruptedException :" + e);
            return null;
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetPackageSizeInfo() throws java.lang.Exception {
        java.lang.String testName = "testGetPackageSizeInfo";
        android.content.pm.PackageStats stats = invokePMGetPackageSizeInfo();
        assertTrue(stats != null);
        // confirm result
        if (android.content.pm.AppCacheTest.localLOGV)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, (((("code=" + stats.codeSize) + ", data=") + stats.dataSize) + ", cache=") + stats.cacheSize);

    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetSystemSharedLibraryNames() throws java.lang.Exception {
        try {
            java.lang.String[] sharedLibs = getPm().getSystemSharedLibraryNames();
            if (android.content.pm.AppCacheTest.localLOGV) {
                for (java.lang.String str : sharedLibs) {
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, str);
                }
            }
        } catch (android.os.RemoteException e) {
            fail("Failed invoking getSystemSharedLibraryNames with exception:" + e);
        }
    }

    class FreeStorageReceiver extends android.content.BroadcastReceiver {
        public static final java.lang.String ACTION_FREE = "com.android.unit_tests.testcallback";

        private boolean doneFlag = false;

        public boolean isDone() {
            return doneFlag;
        }

        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equalsIgnoreCase(android.content.pm.AppCacheTest.FreeStorageReceiver.ACTION_FREE)) {
                if (android.content.pm.AppCacheTest.localLOGV)
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Got notification: clear cache succeeded " + getResultCode());

                synchronized(this) {
                    doneFlag = true;
                    notifyAll();
                }
            }
        }
    }

    // TODO: flaky test, omit from LargeTest for now
    // @LargeTest
    public void testFreeStorage() throws java.lang.Exception {
        boolean TRACKING = true;
        android.os.StatFs st = new android.os.StatFs("/data");
        long blks1 = getFreeStorageBlks(st);
        if (android.content.pm.AppCacheTest.localLOGV || TRACKING)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Available free blocks=" + blks1);

        long availableMem = getFreeStorageSize(st);
        java.io.File cacheDir = mContext.getCacheDir();
        assertNotNull(cacheDir);
        createTestFiles1(cacheDir, "testtmpdir", 5);
        long blks2 = getFreeStorageBlks(st);
        if (android.content.pm.AppCacheTest.localLOGV || TRACKING)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Available blocks after writing test files in application cache=" + blks2);

        // Create receiver and register it
        android.content.pm.AppCacheTest.FreeStorageReceiver receiver = new android.content.pm.AppCacheTest.FreeStorageReceiver();
        mContext.registerReceiver(receiver, new android.content.IntentFilter(android.content.pm.AppCacheTest.FreeStorageReceiver.ACTION_FREE));
        android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(mContext, 0, new android.content.Intent(android.content.pm.AppCacheTest.FreeStorageReceiver.ACTION_FREE), 0);
        // Invoke PackageManager api
        if (!invokePMFreeStorage(availableMem, receiver, pi)) {
            fail("Could not invoke PackageManager free storage API");
        }
        long blks3 = getFreeStorageBlks(st);
        if (android.content.pm.AppCacheTest.localLOGV || TRACKING)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Available blocks after freeing cache" + blks3);

        assertEquals(receiver.getResultCode(), 1);
        mContext.unregisterReceiver(receiver);
        // Verify result
        verifyTestFiles1(cacheDir, "testtmpdir", 5);
    }

    /* utility method used to create observer and check async call back from PackageManager.
    ClearApplicationUserData
     */
    boolean invokePMClearApplicationUserData() throws java.lang.Exception {
        try {
            java.lang.String packageName = mContext.getPackageName();
            android.content.pm.AppCacheTest.PackageDataObserver observer = new android.content.pm.AppCacheTest.PackageDataObserver();
            // wait on observer
            synchronized(observer) {
                /* TODO: Other users */
                getPm().clearApplicationUserData(packageName, observer, 0);
                long waitTime = 0;
                while ((!observer.isDone()) || (waitTime > MAX_WAIT_TIME)) {
                    observer.wait(WAIT_TIME_INCR);
                    waitTime += WAIT_TIME_INCR;
                } 
                if (!observer.isDone()) {
                    throw new java.lang.Exception("timed out waiting for PackageDataObserver.onRemoveCompleted");
                }
            }
            return observer.retValue;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "Failed to get handle for PackageManger Exception: " + e);
            return false;
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w(android.content.pm.AppCacheTest.TAG, "InterruptedException :" + e);
            return false;
        }
    }

    void verifyUserDataCleared(java.io.File pDir) {
        if (android.content.pm.AppCacheTest.localLOGV)
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Verifying " + pDir);

        if (pDir == null) {
            return;
        }
        java.lang.String[] fileList = pDir.list();
        if (fileList == null) {
            return;
        }
        int imax = fileList.length;
        // look recursively in user data dir
        for (int i = 0; i < imax; i++) {
            if (android.content.pm.AppCacheTest.localLOGV)
                android.util.Log.i(android.content.pm.AppCacheTest.TAG, (("Found entry " + fileList[i]) + "in ") + pDir);

            if ("lib".equalsIgnoreCase(fileList[i])) {
                if (android.content.pm.AppCacheTest.localLOGV)
                    android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Ignoring lib directory");

                continue;
            }
            fail((pDir + " should be empty or contain only lib subdirectory. Found ") + fileList[i]);
        }
    }

    java.io.File getDataDir() {
        try {
            android.content.pm.ApplicationInfo appInfo = getPm().getApplicationInfo(mContext.getPackageName(), 0, android.os.UserHandle.myUserId());
            return new java.io.File(appInfo.dataDir);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Pacakge manager dead", e);
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testClearApplicationUserDataWithTestData() throws java.lang.Exception {
        java.io.File cacheDir = mContext.getCacheDir();
        createTestFiles1(cacheDir, "testtmpdir", 5);
        if (android.content.pm.AppCacheTest.localLOGV) {
            android.util.Log.i(android.content.pm.AppCacheTest.TAG, "Created test data Waiting for 60seconds before continuing");
            java.lang.Thread.sleep(60 * 1000);
        }
        assertTrue(invokePMClearApplicationUserData());
        // confirm files dont exist
        verifyUserDataCleared(getDataDir());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testClearApplicationUserDataWithNoTestData() throws java.lang.Exception {
        assertTrue(invokePMClearApplicationUserData());
        // confirm files dont exist
        verifyUserDataCleared(getDataDir());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testClearApplicationUserDataNoObserver() throws java.lang.Exception {
        getPm().clearApplicationUserData(mContext.getPackageName(), null, android.os.UserHandle.myUserId());
        // sleep for 1 minute
        java.lang.Thread.sleep(60 * 1000);
        // confirm files dont exist
        verifyUserDataCleared(getDataDir());
    }
}

