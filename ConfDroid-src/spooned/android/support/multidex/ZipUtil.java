/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Apache Harmony HEADER because the code in this class comes mostly from ZipFile, ZipEntry and
 * ZipConstants from android libcore.
 */
package android.support.multidex;


/**
 * Tools to build a quick partial crc of zip files.
 */
final class ZipUtil {
    static class CentralDirectory {
        long offset;

        long size;
    }

    /* redefine those constant here because of bug 13721174 preventing to compile using the
    constants defined in ZipFile
     */
    private static final int ENDHDR = 22;

    private static final int ENDSIG = 0x6054b50;

    /**
     * Size of reading buffers.
     */
    private static final int BUFFER_SIZE = 0x4000;

    /**
     * Compute crc32 of the central directory of an apk. The central directory contains
     * the crc32 of each entries in the zip so the computed result is considered valid for the whole
     * zip file. Does not support zip64 nor multidisk but it should be OK for now since ZipFile does
     * not either.
     */
    static long getZipCrc(java.io.File apk) throws java.io.IOException {
        java.io.RandomAccessFile raf = new java.io.RandomAccessFile(apk, "r");
        try {
            android.support.multidex.ZipUtil.CentralDirectory dir = android.support.multidex.ZipUtil.findCentralDirectory(raf);
            return android.support.multidex.ZipUtil.computeCrcOfCentralDir(raf, dir);
        } finally {
            raf.close();
        }
    }

    /* Package visible for testing */
    static android.support.multidex.ZipUtil.CentralDirectory findCentralDirectory(java.io.RandomAccessFile raf) throws java.io.IOException, java.util.zip.ZipException {
        long scanOffset = raf.length() - android.support.multidex.ZipUtil.ENDHDR;
        if (scanOffset < 0) {
            throw new java.util.zip.ZipException("File too short to be a zip file: " + raf.length());
        }
        /* ".ZIP file comment"'s max length */
        long stopOffset = scanOffset - 0x10000;
        if (stopOffset < 0) {
            stopOffset = 0;
        }
        int endSig = java.lang.Integer.reverseBytes(android.support.multidex.ZipUtil.ENDSIG);
        while (true) {
            raf.seek(scanOffset);
            if (raf.readInt() == endSig) {
                break;
            }
            scanOffset--;
            if (scanOffset < stopOffset) {
                throw new java.util.zip.ZipException("End Of Central Directory signature not found");
            }
        } 
        // Read the End Of Central Directory. ENDHDR includes the signature
        // bytes,
        // which we've already read.
        // Pull out the information we need.
        raf.skipBytes(2);// diskNumber

        raf.skipBytes(2);// diskWithCentralDir

        raf.skipBytes(2);// numEntries

        raf.skipBytes(2);// totalNumEntries

        android.support.multidex.ZipUtil.CentralDirectory dir = new android.support.multidex.ZipUtil.CentralDirectory();
        dir.size = java.lang.Integer.reverseBytes(raf.readInt()) & 0xffffffffL;
        dir.offset = java.lang.Integer.reverseBytes(raf.readInt()) & 0xffffffffL;
        return dir;
    }

    /* Package visible for testing */
    static long computeCrcOfCentralDir(java.io.RandomAccessFile raf, android.support.multidex.ZipUtil.CentralDirectory dir) throws java.io.IOException {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        long stillToRead = dir.size;
        raf.seek(dir.offset);
        int length = ((int) (java.lang.Math.min(android.support.multidex.ZipUtil.BUFFER_SIZE, stillToRead)));
        byte[] buffer = new byte[android.support.multidex.ZipUtil.BUFFER_SIZE];
        length = raf.read(buffer, 0, length);
        while (length != (-1)) {
            crc.update(buffer, 0, length);
            stillToRead -= length;
            if (stillToRead == 0) {
                break;
            }
            length = ((int) (java.lang.Math.min(android.support.multidex.ZipUtil.BUFFER_SIZE, stillToRead)));
            length = raf.read(buffer, 0, length);
        } 
        return crc.getValue();
    }
}

