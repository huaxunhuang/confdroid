/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.drm;


/**
 * Stream that applies a {@link DrmManagerClient} transformation to data before
 * writing to disk, similar to a {@link FilterOutputStream}.
 *
 * @unknown 
 */
public class DrmOutputStream extends java.io.OutputStream {
    private static final java.lang.String TAG = "DrmOutputStream";

    private final android.drm.DrmManagerClient mClient;

    private final android.os.ParcelFileDescriptor mPfd;

    private final java.io.FileDescriptor mFd;

    private int mSessionId = android.drm.DrmManagerClient.INVALID_SESSION;

    /**
     *
     *
     * @param pfd
     * 		Opened with "rw" mode.
     */
    public DrmOutputStream(android.drm.DrmManagerClient client, android.os.ParcelFileDescriptor pfd, java.lang.String mimeType) throws java.io.IOException {
        mClient = client;
        mPfd = pfd;
        mFd = pfd.getFileDescriptor();
        mSessionId = mClient.openConvertSession(mimeType);
        if (mSessionId == android.drm.DrmManagerClient.INVALID_SESSION) {
            throw new java.net.UnknownServiceException("Failed to open DRM session for " + mimeType);
        }
    }

    public void finish() throws java.io.IOException {
        final android.drm.DrmConvertedStatus status = mClient.closeConvertSession(mSessionId);
        if (status.statusCode == android.drm.DrmConvertedStatus.STATUS_OK) {
            try {
                android.system.Os.lseek(mFd, status.offset, android.system.OsConstants.SEEK_SET);
            } catch (android.system.ErrnoException e) {
                e.rethrowAsIOException();
            }
            libcore.io.IoBridge.write(mFd, status.convertedData, 0, status.convertedData.length);
            mSessionId = android.drm.DrmManagerClient.INVALID_SESSION;
        } else {
            throw new java.io.IOException("Unexpected DRM status: " + status.statusCode);
        }
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        if (mSessionId == android.drm.DrmManagerClient.INVALID_SESSION) {
            android.util.Log.w(android.drm.DrmOutputStream.TAG, "Closing stream without finishing");
        }
        mPfd.close();
    }

    @java.lang.Override
    public void write(byte[] buffer, int offset, int count) throws java.io.IOException {
        java.util.Arrays.checkOffsetAndCount(buffer.length, offset, count);
        final byte[] exactBuffer;
        if (count == buffer.length) {
            exactBuffer = buffer;
        } else {
            exactBuffer = new byte[count];
            java.lang.System.arraycopy(buffer, offset, exactBuffer, 0, count);
        }
        final android.drm.DrmConvertedStatus status = mClient.convertData(mSessionId, exactBuffer);
        if (status.statusCode == android.drm.DrmConvertedStatus.STATUS_OK) {
            libcore.io.IoBridge.write(mFd, status.convertedData, 0, status.convertedData.length);
        } else {
            throw new java.io.IOException("Unexpected DRM status: " + status.statusCode);
        }
    }

    @java.lang.Override
    public void write(int oneByte) throws java.io.IOException {
        libcore.io.Streams.writeSingleByte(this, oneByte);
    }
}

