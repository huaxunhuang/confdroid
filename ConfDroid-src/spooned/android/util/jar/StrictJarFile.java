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
package android.util.jar;


/**
 * A subset of the JarFile API implemented as a thin wrapper over
 * system/core/libziparchive.
 *
 * @unknown for internal use only. Not API compatible (or as forgiving) as
{@link java.util.jar.JarFile}
 */
public final class StrictJarFile {
    private final long nativeHandle;

    // NOTE: It's possible to share a file descriptor with the native
    // code, at the cost of some additional complexity.
    private final java.io.RandomAccessFile raf;

    private final android.util.jar.StrictJarManifest manifest;

    private final android.util.jar.StrictJarVerifier verifier;

    private final boolean isSigned;

    private final dalvik.system.CloseGuard guard = dalvik.system.CloseGuard.get();

    private boolean closed;

    public StrictJarFile(java.lang.String fileName) throws java.io.IOException, java.lang.SecurityException {
        this(fileName, true, true);
    }

    /**
     *
     *
     * @param verify
     * 		whether to verify the file's JAR signatures and collect the corresponding
     * 		signer certificates.
     * @param signatureSchemeRollbackProtectionsEnforced
     * 		{@code true} to enforce protections against
     * 		stripping newer signature schemes (e.g., APK Signature Scheme v2) from the file, or
     * 		{@code false} to ignore any such protections. This parameter is ignored when
     * 		{@code verify} is {@code false}.
     */
    public StrictJarFile(java.lang.String fileName, boolean verify, boolean signatureSchemeRollbackProtectionsEnforced) throws java.io.IOException, java.lang.SecurityException {
        this.nativeHandle = android.util.jar.StrictJarFile.nativeOpenJarFile(fileName);
        this.raf = new java.io.RandomAccessFile(fileName, "r");
        try {
            // Read the MANIFEST and signature files up front and try to
            // parse them. We never want to accept a JAR File with broken signatures
            // or manifests, so it's best to throw as early as possible.
            if (verify) {
                java.util.HashMap<java.lang.String, byte[]> metaEntries = getMetaEntries();
                this.manifest = new android.util.jar.StrictJarManifest(metaEntries.get(java.util.jar.JarFile.MANIFEST_NAME), true);
                this.verifier = new android.util.jar.StrictJarVerifier(fileName, manifest, metaEntries, signatureSchemeRollbackProtectionsEnforced);
                java.util.Set<java.lang.String> files = manifest.getEntries().keySet();
                for (java.lang.String file : files) {
                    if (findEntry(file) == null) {
                        throw new java.lang.SecurityException(((fileName + ": File ") + file) + " in manifest does not exist");
                    }
                }
                isSigned = verifier.readCertificates() && verifier.isSignedJar();
            } else {
                isSigned = false;
                this.manifest = null;
                this.verifier = null;
            }
        } catch (java.io.IOException | java.lang.SecurityException e) {
            android.util.jar.StrictJarFile.nativeClose(this.nativeHandle);
            libcore.io.IoUtils.closeQuietly(this.raf);
            throw e;
        }
        guard.open("close");
    }

    public android.util.jar.StrictJarManifest getManifest() {
        return manifest;
    }

    public java.util.Iterator<java.util.zip.ZipEntry> iterator() throws java.io.IOException {
        return new android.util.jar.StrictJarFile.EntryIterator(nativeHandle, "");
    }

    public java.util.zip.ZipEntry findEntry(java.lang.String name) {
        return android.util.jar.StrictJarFile.nativeFindEntry(nativeHandle, name);
    }

    /**
     * Return all certificate chains for a given {@link ZipEntry} belonging to this jar.
     * This method MUST be called only after fully exhausting the InputStream belonging
     * to this entry.
     *
     * Returns {@code null} if this jar file isn't signed or if this method is
     * called before the stream is processed.
     */
    public java.security.cert.Certificate[][] getCertificateChains(java.util.zip.ZipEntry ze) {
        if (isSigned) {
            return verifier.getCertificateChains(ze.getName());
        }
        return null;
    }

    /**
     * Return all certificates for a given {@link ZipEntry} belonging to this jar.
     * This method MUST be called only after fully exhausting the InputStream belonging
     * to this entry.
     *
     * Returns {@code null} if this jar file isn't signed or if this method is
     * called before the stream is processed.
     *
     * @deprecated Switch callers to use getCertificateChains instead
     */
    @java.lang.Deprecated
    public java.security.cert.Certificate[] getCertificates(java.util.zip.ZipEntry ze) {
        if (isSigned) {
            java.security.cert.Certificate[][] certChains = verifier.getCertificateChains(ze.getName());
            // Measure number of certs.
            int count = 0;
            for (java.security.cert.Certificate[] chain : certChains) {
                count += chain.length;
            }
            // Create new array and copy all the certs into it.
            java.security.cert.Certificate[] certs = new java.security.cert.Certificate[count];
            int i = 0;
            for (java.security.cert.Certificate[] chain : certChains) {
                java.lang.System.arraycopy(chain, 0, certs, i, chain.length);
                i += chain.length;
            }
            return certs;
        }
        return null;
    }

    public java.io.InputStream getInputStream(java.util.zip.ZipEntry ze) {
        final java.io.InputStream is = getZipInputStream(ze);
        if (isSigned) {
            android.util.jar.StrictJarVerifier.VerifierEntry entry = verifier.initEntry(ze.getName());
            if (entry == null) {
                return is;
            }
            return new android.util.jar.StrictJarFile.JarFileInputStream(is, ze.getSize(), entry);
        }
        return is;
    }

    public void close() throws java.io.IOException {
        if (!closed) {
            guard.close();
            android.util.jar.StrictJarFile.nativeClose(nativeHandle);
            libcore.io.IoUtils.closeQuietly(raf);
            closed = true;
        }
    }

    private java.io.InputStream getZipInputStream(java.util.zip.ZipEntry ze) {
        if (ze.getMethod() == java.util.zip.ZipEntry.STORED) {
            return new android.util.jar.StrictJarFile.RAFStream(raf, ze.getDataOffset(), ze.getDataOffset() + ze.getSize());
        } else {
            final android.util.jar.StrictJarFile.RAFStream wrapped = new android.util.jar.StrictJarFile.RAFStream(raf, ze.getDataOffset(), ze.getDataOffset() + ze.getCompressedSize());
            int bufSize = java.lang.Math.max(1024, ((int) (java.lang.Math.min(ze.getSize(), 65535L))));
            return new android.util.jar.StrictJarFile.ZipInflaterInputStream(wrapped, new java.util.zip.Inflater(true), bufSize, ze);
        }
    }

    static final class EntryIterator implements java.util.Iterator<java.util.zip.ZipEntry> {
        private final long iterationHandle;

        private java.util.zip.ZipEntry nextEntry;

        EntryIterator(long nativeHandle, java.lang.String prefix) throws java.io.IOException {
            iterationHandle = android.util.jar.StrictJarFile.nativeStartIteration(nativeHandle, prefix);
        }

        public java.util.zip.ZipEntry next() {
            if (nextEntry != null) {
                final java.util.zip.ZipEntry ze = nextEntry;
                nextEntry = null;
                return ze;
            }
            return android.util.jar.StrictJarFile.nativeNextEntry(iterationHandle);
        }

        public boolean hasNext() {
            if (nextEntry != null) {
                return true;
            }
            final java.util.zip.ZipEntry ze = android.util.jar.StrictJarFile.nativeNextEntry(iterationHandle);
            if (ze == null) {
                return false;
            }
            nextEntry = ze;
            return true;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    private java.util.HashMap<java.lang.String, byte[]> getMetaEntries() throws java.io.IOException {
        java.util.HashMap<java.lang.String, byte[]> metaEntries = new java.util.HashMap<java.lang.String, byte[]>();
        java.util.Iterator<java.util.zip.ZipEntry> entryIterator = new android.util.jar.StrictJarFile.EntryIterator(nativeHandle, "META-INF/");
        while (entryIterator.hasNext()) {
            final java.util.zip.ZipEntry entry = entryIterator.next();
            metaEntries.put(entry.getName(), libcore.io.Streams.readFully(getInputStream(entry)));
        } 
        return metaEntries;
    }

    static final class JarFileInputStream extends java.io.FilterInputStream {
        private final android.util.jar.StrictJarVerifier.VerifierEntry entry;

        private long count;

        private boolean done = false;

        JarFileInputStream(java.io.InputStream is, long size, android.util.jar.StrictJarVerifier.VerifierEntry e) {
            super(is);
            entry = e;
            count = size;
        }

        @java.lang.Override
        public int read() throws java.io.IOException {
            if (done) {
                return -1;
            }
            if (count > 0) {
                int r = super.read();
                if (r != (-1)) {
                    entry.write(r);
                    count--;
                } else {
                    count = 0;
                }
                if (count == 0) {
                    done = true;
                    entry.verify();
                }
                return r;
            } else {
                done = true;
                entry.verify();
                return -1;
            }
        }

        @java.lang.Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws java.io.IOException {
            if (done) {
                return -1;
            }
            if (count > 0) {
                int r = super.read(buffer, byteOffset, byteCount);
                if (r != (-1)) {
                    int size = r;
                    if (count < size) {
                        size = ((int) (count));
                    }
                    entry.write(buffer, byteOffset, size);
                    count -= size;
                } else {
                    count = 0;
                }
                if (count == 0) {
                    done = true;
                    entry.verify();
                }
                return r;
            } else {
                done = true;
                entry.verify();
                return -1;
            }
        }

        @java.lang.Override
        public int available() throws java.io.IOException {
            if (done) {
                return 0;
            }
            return super.available();
        }

        @java.lang.Override
        public long skip(long byteCount) throws java.io.IOException {
            return libcore.io.Streams.skipByReading(this, byteCount);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static class ZipInflaterInputStream extends java.util.zip.InflaterInputStream {
        private final java.util.zip.ZipEntry entry;

        private long bytesRead = 0;

        public ZipInflaterInputStream(java.io.InputStream is, java.util.zip.Inflater inf, int bsize, java.util.zip.ZipEntry entry) {
            super(is, inf, bsize);
            this.entry = entry;
        }

        @java.lang.Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws java.io.IOException {
            final int i;
            try {
                i = super.read(buffer, byteOffset, byteCount);
            } catch (java.io.IOException e) {
                throw new java.io.IOException((("Error reading data for " + entry.getName()) + " near offset ") + bytesRead, e);
            }
            if (i == (-1)) {
                if (entry.getSize() != bytesRead) {
                    throw new java.io.IOException((("Size mismatch on inflated file: " + bytesRead) + " vs ") + entry.getSize());
                }
            } else {
                bytesRead += i;
            }
            return i;
        }

        @java.lang.Override
        public int available() throws java.io.IOException {
            if (android.util.jar.StrictJarFile.this.closed) {
                // Our superclass will throw an exception, but there's a jtreg test that
                // explicitly checks that the InputStream returned from ZipFile.getInputStream
                // returns 0 even when closed.
                return 0;
            }
            return super.available() == 0 ? 0 : ((int) (entry.getSize() - bytesRead));
        }
    }

    /**
     * Wrap a stream around a RandomAccessFile.  The RandomAccessFile is shared
     * among all streams returned by getInputStream(), so we have to synchronize
     * access to it.  (We can optimize this by adding buffering here to reduce
     * collisions.)
     *
     * <p>We could support mark/reset, but we don't currently need them.
     *
     * @unknown 
     */
    public static class RAFStream extends java.io.InputStream {
        private final java.io.RandomAccessFile sharedRaf;

        private long endOffset;

        private long offset;

        public RAFStream(java.io.RandomAccessFile raf, long initialOffset, long endOffset) {
            sharedRaf = raf;
            offset = initialOffset;
            this.endOffset = endOffset;
        }

        public RAFStream(java.io.RandomAccessFile raf, long initialOffset) throws java.io.IOException {
            this(raf, initialOffset, raf.length());
        }

        @java.lang.Override
        public int available() throws java.io.IOException {
            return offset < endOffset ? 1 : 0;
        }

        @java.lang.Override
        public int read() throws java.io.IOException {
            return libcore.io.Streams.readSingleByte(this);
        }

        @java.lang.Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws java.io.IOException {
            synchronized(sharedRaf) {
                final long length = endOffset - offset;
                if (byteCount > length) {
                    byteCount = ((int) (length));
                }
                sharedRaf.seek(offset);
                int count = sharedRaf.read(buffer, byteOffset, byteCount);
                if (count > 0) {
                    offset += count;
                    return count;
                } else {
                    return -1;
                }
            }
        }

        @java.lang.Override
        public long skip(long byteCount) throws java.io.IOException {
            if (byteCount > (endOffset - offset)) {
                byteCount = endOffset - offset;
            }
            offset += byteCount;
            return byteCount;
        }
    }

    private static native long nativeOpenJarFile(java.lang.String fileName) throws java.io.IOException;

    private static native long nativeStartIteration(long nativeHandle, java.lang.String prefix);

    private static native java.util.zip.ZipEntry nativeNextEntry(long iterationHandle);

    private static native java.util.zip.ZipEntry nativeFindEntry(long nativeHandle, java.lang.String entryName);

    private static native void nativeClose(long nativeHandle);
}

