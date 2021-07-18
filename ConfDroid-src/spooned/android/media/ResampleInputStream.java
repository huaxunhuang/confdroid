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
package android.media;


/**
 * ResampleInputStream
 *
 * @unknown 
 */
public final class ResampleInputStream extends java.io.InputStream {
    static {
        java.lang.System.loadLibrary("media_jni");
    }

    private static final java.lang.String TAG = "ResampleInputStream";

    // pcm input stream
    private java.io.InputStream mInputStream;

    // sample rates, assumed to be normalized
    private final int mRateIn;

    private final int mRateOut;

    // input pcm data
    private byte[] mBuf;

    private int mBufCount;

    // length of 2:1 fir
    private static final int mFirLength = 29;

    // helper for bytewise read()
    private final byte[] mOneByte = new byte[1];

    /**
     * Create a new ResampleInputStream, which converts the sample rate
     *
     * @param inputStream
     * 		InputStream containing 16 bit PCM.
     * @param rateIn
     * 		the input sample rate.
     * @param rateOut
     * 		the output sample rate.
     * 		This only handles rateIn == rateOut / 2 for the moment.
     */
    public ResampleInputStream(java.io.InputStream inputStream, int rateIn, int rateOut) {
        // only support 2:1 at the moment
        if (rateIn != (2 * rateOut))
            throw new java.lang.IllegalArgumentException("only support 2:1 at the moment");

        rateIn = 2;
        rateOut = 1;
        mInputStream = inputStream;
        mRateIn = rateIn;
        mRateOut = rateOut;
    }

    @java.lang.Override
    public int read() throws java.io.IOException {
        int rtn = read(mOneByte, 0, 1);
        return rtn == 1 ? 0xff & mOneByte[0] : -1;
    }

    @java.lang.Override
    public int read(byte[] b) throws java.io.IOException {
        return read(b, 0, b.length);
    }

    @java.lang.Override
    public int read(byte[] b, int offset, int length) throws java.io.IOException {
        if (mInputStream == null)
            throw new java.lang.IllegalStateException("not open");

        // ensure that mBuf is big enough to cover requested 'length'
        int nIn = ((((length / 2) * mRateIn) / mRateOut) + android.media.ResampleInputStream.mFirLength) * 2;
        if (mBuf == null) {
            mBuf = new byte[nIn];
        } else
            if (nIn > mBuf.length) {
                byte[] bf = new byte[nIn];
                java.lang.System.arraycopy(mBuf, 0, bf, 0, mBufCount);
                mBuf = bf;
            }

        // read until we have enough data for at least one output sample
        while (true) {
            int len = ((((mBufCount / 2) - android.media.ResampleInputStream.mFirLength) * mRateOut) / mRateIn) * 2;
            if (len > 0) {
                length = (len < length) ? len : (length / 2) * 2;
                break;
            }
            // TODO: should mBuf.length below be nIn instead?
            int n = mInputStream.read(mBuf, mBufCount, mBuf.length - mBufCount);
            if (n == (-1))
                return -1;

            mBufCount += n;
        } 
        // resample input data
        android.media.ResampleInputStream.fir21(mBuf, 0, b, offset, length / 2);
        // move any unused bytes to front of mBuf
        int nFwd = (length * mRateIn) / mRateOut;
        mBufCount -= nFwd;
        if (mBufCount > 0)
            java.lang.System.arraycopy(mBuf, nFwd, mBuf, 0, mBufCount);

        return length;
    }

    /* @Override
    public int available() throws IOException {
    int nsamples = (mIn - mOut + mInputStream.available()) / 2;
    return ((nsamples - mFirLength) * mRateOut / mRateIn) * 2;
    }
     */
    @java.lang.Override
    public void close() throws java.io.IOException {
        try {
            if (mInputStream != null)
                mInputStream.close();

        } finally {
            mInputStream = null;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        if (mInputStream != null) {
            close();
            throw new java.lang.IllegalStateException("someone forgot to close ResampleInputStream");
        }
    }

    // 
    // fir filter code JNI interface
    // 
    private static native void fir21(byte[] in, int inOffset, byte[] out, int outOffset, int npoints);
}

