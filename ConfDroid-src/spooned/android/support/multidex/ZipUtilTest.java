/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.multidex;


/**
 * Tests of ZipUtil class.
 *
 * The test assumes that ANDROID_BUILD_TOP environment variable is defined and point to the top of a
 * built android tree. This is the case when the console used for running the tests is setup for
 * android tree compilation.
 */
public class ZipUtilTest {
    private static final java.io.File zipFile = new java.io.File(java.lang.System.getenv("ANDROID_BUILD_TOP"), "out/target/common/obj/JAVA_LIBRARIES/android-support-multidex_intermediates/javalib.jar");

    @org.junit.BeforeClass
    public static void setupClass() throws java.io.IOException, java.util.zip.ZipException {
        // just verify the zip is valid
        new java.util.zip.ZipFile(android.support.multidex.ZipUtilTest.zipFile).close();
    }

    @org.junit.Test
    public void testCrcDoNotCrash() throws java.io.IOException {
        long crc = android.support.multidex.ZipUtil.getZipCrc(android.support.multidex.ZipUtilTest.zipFile);
        java.lang.System.out.println("crc is " + crc);
    }

    @org.junit.Test
    public void testCrcRange() throws java.io.IOException {
        java.io.RandomAccessFile raf = new java.io.RandomAccessFile(android.support.multidex.ZipUtilTest.zipFile, "r");
        android.support.multidex.ZipUtil.CentralDirectory dir = android.support.multidex.ZipUtil.findCentralDirectory(raf);
        byte[] dirData = new byte[((int) (dir.size))];
        int length = dirData.length;
        int off = 0;
        raf.seek(dir.offset);
        while (length > 0) {
            int read = raf.read(dirData, off, length);
            if (length == (-1)) {
                throw new java.io.EOFException();
            }
            length -= read;
            off += read;
        } 
        raf.close();
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(dirData);
        java.util.Map<java.lang.String, java.util.zip.ZipEntry> toCheck = new java.util.HashMap<java.lang.String, java.util.zip.ZipEntry>();
        while (buffer.hasRemaining()) {
            buffer = buffer.slice();
            buffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
            java.util.zip.ZipEntry entry = android.support.multidex.ZipEntryReader.readEntry(buffer);
            toCheck.put(entry.getName(), entry);
        } 
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(android.support.multidex.ZipUtilTest.zipFile);
        org.junit.Assert.assertEquals(zip.size(), toCheck.size());
        java.util.Enumeration<? extends java.util.zip.ZipEntry> ref = zip.entries();
        while (ref.hasMoreElements()) {
            java.util.zip.ZipEntry refEntry = ref.nextElement();
            java.util.zip.ZipEntry checkEntry = toCheck.get(refEntry.getName());
            org.junit.Assert.assertNotNull(checkEntry);
            org.junit.Assert.assertEquals(refEntry.getName(), checkEntry.getName());
            org.junit.Assert.assertEquals(refEntry.getComment(), checkEntry.getComment());
            org.junit.Assert.assertEquals(refEntry.getTime(), checkEntry.getTime());
            org.junit.Assert.assertEquals(refEntry.getCrc(), checkEntry.getCrc());
            org.junit.Assert.assertEquals(refEntry.getCompressedSize(), checkEntry.getCompressedSize());
            org.junit.Assert.assertEquals(refEntry.getSize(), checkEntry.getSize());
            org.junit.Assert.assertEquals(refEntry.getMethod(), checkEntry.getMethod());
            org.junit.Assert.assertArrayEquals(refEntry.getExtra(), checkEntry.getExtra());
        } 
        zip.close();
    }

    @org.junit.Test
    public void testCrcValue() throws java.io.IOException {
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(android.support.multidex.ZipUtilTest.zipFile);
        java.util.Enumeration<? extends java.util.zip.ZipEntry> ref = zip.entries();
        byte[] buffer = new byte[0x2000];
        while (ref.hasMoreElements()) {
            java.util.zip.ZipEntry refEntry = ref.nextElement();
            if (refEntry.getSize() > 0) {
                java.io.File tmp = java.io.File.createTempFile("ZipUtilTest", ".fakezip");
                java.io.InputStream in = zip.getInputStream(refEntry);
                java.io.OutputStream out = new java.io.FileOutputStream(tmp);
                int read = in.read(buffer);
                while (read != (-1)) {
                    out.write(buffer, 0, read);
                    read = in.read(buffer);
                } 
                in.close();
                out.close();
                java.io.RandomAccessFile raf = new java.io.RandomAccessFile(tmp, "r");
                android.support.multidex.ZipUtil.CentralDirectory dir = new android.support.multidex.ZipUtil.CentralDirectory();
                dir.offset = 0;
                dir.size = raf.length();
                long crc = android.support.multidex.ZipUtil.computeCrcOfCentralDir(raf, dir);
                org.junit.Assert.assertEquals(refEntry.getCrc(), crc);
                raf.close();
                tmp.delete();
            }
        } 
        zip.close();
    }

    @org.junit.Test
    public void testInvalidCrcValue() throws java.io.IOException {
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(android.support.multidex.ZipUtilTest.zipFile);
        java.util.Enumeration<? extends java.util.zip.ZipEntry> ref = zip.entries();
        byte[] buffer = new byte[0x2000];
        while (ref.hasMoreElements()) {
            java.util.zip.ZipEntry refEntry = ref.nextElement();
            if (refEntry.getSize() > 0) {
                java.io.File tmp = java.io.File.createTempFile("ZipUtilTest", ".fakezip");
                java.io.InputStream in = zip.getInputStream(refEntry);
                java.io.OutputStream out = new java.io.FileOutputStream(tmp);
                int read = in.read(buffer);
                while (read != (-1)) {
                    out.write(buffer, 0, read);
                    read = in.read(buffer);
                } 
                in.close();
                out.close();
                java.io.RandomAccessFile raf = new java.io.RandomAccessFile(tmp, "r");
                android.support.multidex.ZipUtil.CentralDirectory dir = new android.support.multidex.ZipUtil.CentralDirectory();
                dir.offset = 0;
                dir.size = raf.length() - 1;
                long crc = android.support.multidex.ZipUtil.computeCrcOfCentralDir(raf, dir);
                org.junit.Assert.assertNotEquals(refEntry.getCrc(), crc);
                raf.close();
                tmp.delete();
            }
        } 
        zip.close();
    }
}

