/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.core;


/**
 * A frame that serializes any assigned values. Such a frame is used when passing data objects
 * between threads.
 *
 * @unknown 
 */
public class SerializedFrame extends android.filterfw.core.Frame {
    /**
     * The initial capacity of the serialized data stream.
     */
    private static final int INITIAL_CAPACITY = 64;

    /**
     * The internal data streams.
     */
    private android.filterfw.core.SerializedFrame.DirectByteOutputStream mByteOutputStream;

    private java.io.ObjectOutputStream mObjectOut;

    /**
     * An unsynchronized output stream that writes data to an accessible byte array. Callers are
     * responsible for synchronization. This is more efficient than a ByteArrayOutputStream, as
     * there are no array copies or synchronization involved to read back written data.
     */
    private class DirectByteOutputStream extends java.io.OutputStream {
        private byte[] mBuffer = null;

        private int mOffset = 0;

        private int mDataOffset = 0;

        public DirectByteOutputStream(int size) {
            mBuffer = new byte[size];
        }

        private final void ensureFit(int bytesToWrite) {
            if ((mOffset + bytesToWrite) > mBuffer.length) {
                byte[] oldBuffer = mBuffer;
                mBuffer = new byte[java.lang.Math.max(mOffset + bytesToWrite, mBuffer.length * 2)];
                java.lang.System.arraycopy(oldBuffer, 0, mBuffer, 0, mOffset);
                oldBuffer = null;
            }
        }

        public final void markHeaderEnd() {
            mDataOffset = mOffset;
        }

        public final int getSize() {
            return mOffset;
        }

        public byte[] getByteArray() {
            return mBuffer;
        }

        @java.lang.Override
        public final void write(byte[] b) {
            write(b, 0, b.length);
        }

        @java.lang.Override
        public final void write(byte[] b, int off, int len) {
            ensureFit(len);
            java.lang.System.arraycopy(b, off, mBuffer, mOffset, len);
            mOffset += len;
        }

        @java.lang.Override
        public final void write(int b) {
            ensureFit(1);
            mBuffer[mOffset++] = ((byte) (b));
        }

        public final void reset() {
            mOffset = mDataOffset;
        }

        public final android.filterfw.core.SerializedFrame.DirectByteInputStream getInputStream() {
            return new android.filterfw.core.SerializedFrame.DirectByteInputStream(mBuffer, mOffset);
        }
    }

    /**
     * An unsynchronized input stream that reads data directly from a provided byte array. Callers
     * are responsible for synchronization and ensuring that the byte buffer is valid.
     */
    private class DirectByteInputStream extends java.io.InputStream {
        private byte[] mBuffer;

        private int mPos = 0;

        private int mSize;

        public DirectByteInputStream(byte[] buffer, int size) {
            mBuffer = buffer;
            mSize = size;
        }

        @java.lang.Override
        public final int available() {
            return mSize - mPos;
        }

        @java.lang.Override
        public final int read() {
            return mPos < mSize ? mBuffer[mPos++] & 0xff : -1;
        }

        @java.lang.Override
        public final int read(byte[] b, int off, int len) {
            if (mPos >= mSize) {
                return -1;
            }
            if ((mPos + len) > mSize) {
                len = mSize - mPos;
            }
            java.lang.System.arraycopy(mBuffer, mPos, b, off, len);
            mPos += len;
            return len;
        }

        @java.lang.Override
        public final long skip(long n) {
            if ((mPos + n) > mSize) {
                n = mSize - mPos;
            }
            if (n < 0) {
                return 0;
            }
            mPos += n;
            return n;
        }
    }

    SerializedFrame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager) {
        super(format, frameManager);
        setReusable(false);
        // Setup streams
        try {
            mByteOutputStream = new android.filterfw.core.SerializedFrame.DirectByteOutputStream(android.filterfw.core.SerializedFrame.INITIAL_CAPACITY);
            mObjectOut = new java.io.ObjectOutputStream(mByteOutputStream);
            mByteOutputStream.markHeaderEnd();
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Could not create serialization streams for " + "SerializedFrame!", e);
        }
    }

    static android.filterfw.core.SerializedFrame wrapObject(java.lang.Object object, android.filterfw.core.FrameManager frameManager) {
        android.filterfw.core.FrameFormat format = android.filterfw.format.ObjectFormat.fromObject(object, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        android.filterfw.core.SerializedFrame result = new android.filterfw.core.SerializedFrame(format, frameManager);
        result.setObjectValue(object);
        return result;
    }

    @java.lang.Override
    protected boolean hasNativeAllocation() {
        return false;
    }

    @java.lang.Override
    protected void releaseNativeAllocation() {
    }

    @java.lang.Override
    public java.lang.Object getObjectValue() {
        return deserializeObjectValue();
    }

    @java.lang.Override
    public void setInts(int[] ints) {
        assertFrameMutable();
        setGenericObjectValue(ints);
    }

    @java.lang.Override
    public int[] getInts() {
        java.lang.Object result = deserializeObjectValue();
        return result instanceof int[] ? ((int[]) (result)) : null;
    }

    @java.lang.Override
    public void setFloats(float[] floats) {
        assertFrameMutable();
        setGenericObjectValue(floats);
    }

    @java.lang.Override
    public float[] getFloats() {
        java.lang.Object result = deserializeObjectValue();
        return result instanceof float[] ? ((float[]) (result)) : null;
    }

    @java.lang.Override
    public void setData(java.nio.ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        setGenericObjectValue(java.nio.ByteBuffer.wrap(buffer.array(), offset, length));
    }

    @java.lang.Override
    public java.nio.ByteBuffer getData() {
        java.lang.Object result = deserializeObjectValue();
        return result instanceof java.nio.ByteBuffer ? ((java.nio.ByteBuffer) (result)) : null;
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        assertFrameMutable();
        setGenericObjectValue(bitmap);
    }

    @java.lang.Override
    public android.graphics.Bitmap getBitmap() {
        java.lang.Object result = deserializeObjectValue();
        return result instanceof android.graphics.Bitmap ? ((android.graphics.Bitmap) (result)) : null;
    }

    @java.lang.Override
    protected void setGenericObjectValue(java.lang.Object object) {
        serializeObjectValue(object);
    }

    private final void serializeObjectValue(java.lang.Object object) {
        try {
            mByteOutputStream.reset();
            mObjectOut.writeObject(object);
            mObjectOut.flush();
            mObjectOut.close();
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(((("Could not serialize object " + object) + " in ") + this) + "!", e);
        }
    }

    private final java.lang.Object deserializeObjectValue() {
        try {
            java.io.InputStream inputStream = mByteOutputStream.getInputStream();
            java.io.ObjectInputStream objectStream = new java.io.ObjectInputStream(inputStream);
            return objectStream.readObject();
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(("Could not deserialize object in " + this) + "!", e);
        } catch (java.lang.ClassNotFoundException e) {
            throw new java.lang.RuntimeException(("Unable to deserialize object of unknown class in " + this) + "!", e);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("SerializedFrame (" + getFormat()) + ")";
    }
}

