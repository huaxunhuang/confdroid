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
 *
 *
 * @unknown 
 */
public abstract class Frame {
    public static final int NO_BINDING = 0;

    public static final long TIMESTAMP_NOT_SET = -2;

    public static final long TIMESTAMP_UNKNOWN = -1;

    private android.filterfw.core.FrameFormat mFormat;

    private android.filterfw.core.FrameManager mFrameManager;

    private boolean mReadOnly = false;

    private boolean mReusable = false;

    private int mRefCount = 1;

    private int mBindingType = android.filterfw.core.Frame.NO_BINDING;

    private long mBindingId = 0;

    private long mTimestamp = android.filterfw.core.Frame.TIMESTAMP_NOT_SET;

    Frame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager) {
        mFormat = format.mutableCopy();
        mFrameManager = frameManager;
    }

    Frame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager, int bindingType, long bindingId) {
        mFormat = format.mutableCopy();
        mFrameManager = frameManager;
        mBindingType = bindingType;
        mBindingId = bindingId;
    }

    public android.filterfw.core.FrameFormat getFormat() {
        return mFormat;
    }

    public int getCapacity() {
        return getFormat().getSize();
    }

    public boolean isReadOnly() {
        return mReadOnly;
    }

    public int getBindingType() {
        return mBindingType;
    }

    public long getBindingId() {
        return mBindingId;
    }

    public void setObjectValue(java.lang.Object object) {
        assertFrameMutable();
        // Attempt to set the value using a specific setter (which may be more optimized), and
        // fall back to the setGenericObjectValue(...) in case of no match.
        if (object instanceof int[]) {
            setInts(((int[]) (object)));
        } else
            if (object instanceof float[]) {
                setFloats(((float[]) (object)));
            } else
                if (object instanceof java.nio.ByteBuffer) {
                    setData(((java.nio.ByteBuffer) (object)));
                } else
                    if (object instanceof android.graphics.Bitmap) {
                        setBitmap(((android.graphics.Bitmap) (object)));
                    } else {
                        setGenericObjectValue(object);
                    }



    }

    public abstract java.lang.Object getObjectValue();

    public abstract void setInts(int[] ints);

    public abstract int[] getInts();

    public abstract void setFloats(float[] floats);

    public abstract float[] getFloats();

    public abstract void setData(java.nio.ByteBuffer buffer, int offset, int length);

    public void setData(java.nio.ByteBuffer buffer) {
        setData(buffer, 0, buffer.limit());
    }

    public void setData(byte[] bytes, int offset, int length) {
        setData(java.nio.ByteBuffer.wrap(bytes, offset, length));
    }

    public abstract java.nio.ByteBuffer getData();

    public abstract void setBitmap(android.graphics.Bitmap bitmap);

    public abstract android.graphics.Bitmap getBitmap();

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setDataFromFrame(android.filterfw.core.Frame frame) {
        setData(frame.getData());
    }

    protected boolean requestResize(int[] newDimensions) {
        return false;
    }

    public int getRefCount() {
        return mRefCount;
    }

    public android.filterfw.core.Frame release() {
        if (mFrameManager != null) {
            return mFrameManager.releaseFrame(this);
        } else {
            return this;
        }
    }

    public android.filterfw.core.Frame retain() {
        if (mFrameManager != null) {
            return mFrameManager.retainFrame(this);
        } else {
            return this;
        }
    }

    public android.filterfw.core.FrameManager getFrameManager() {
        return mFrameManager;
    }

    protected void assertFrameMutable() {
        if (isReadOnly()) {
            throw new java.lang.RuntimeException("Attempting to modify read-only frame!");
        }
    }

    protected void setReusable(boolean reusable) {
        mReusable = reusable;
    }

    protected void setFormat(android.filterfw.core.FrameFormat format) {
        mFormat = format.mutableCopy();
    }

    protected void setGenericObjectValue(java.lang.Object value) {
        throw new java.lang.RuntimeException("Cannot set object value of unsupported type: " + value.getClass());
    }

    protected static android.graphics.Bitmap convertBitmapToRGBA(android.graphics.Bitmap bitmap) {
        if (bitmap.getConfig() == android.graphics.Bitmap.Config.ARGB_8888) {
            return bitmap;
        } else {
            android.graphics.Bitmap result = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, false);
            if (result == null) {
                throw new java.lang.RuntimeException("Error converting bitmap to RGBA!");
            } else
                if (result.getRowBytes() != (result.getWidth() * 4)) {
                    throw new java.lang.RuntimeException("Unsupported row byte count in bitmap!");
                }

            return result;
        }
    }

    protected void reset(android.filterfw.core.FrameFormat newFormat) {
        mFormat = newFormat.mutableCopy();
        mReadOnly = false;
        mRefCount = 1;
    }

    /**
     * Called just before a frame is stored, such as when storing to a cache or context.
     */
    protected void onFrameStore() {
    }

    /**
     * Called when a frame is fetched from an internal store such as a cache.
     */
    protected void onFrameFetch() {
    }

    // Core internal methods ///////////////////////////////////////////////////////////////////////
    protected abstract boolean hasNativeAllocation();

    protected abstract void releaseNativeAllocation();

    final int incRefCount() {
        ++mRefCount;
        return mRefCount;
    }

    final int decRefCount() {
        --mRefCount;
        return mRefCount;
    }

    final boolean isReusable() {
        return mReusable;
    }

    final void markReadOnly() {
        mReadOnly = true;
    }
}

