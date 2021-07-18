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
public class NativeFrame extends android.filterfw.core.Frame {
    private int nativeFrameId = -1;

    NativeFrame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager) {
        super(format, frameManager);
        int capacity = format.getSize();
        nativeAllocate(capacity);
        setReusable(capacity != 0);
    }

    @java.lang.Override
    protected synchronized void releaseNativeAllocation() {
        nativeDeallocate();
        nativeFrameId = -1;
    }

    @java.lang.Override
    protected synchronized boolean hasNativeAllocation() {
        return nativeFrameId != (-1);
    }

    @java.lang.Override
    public int getCapacity() {
        return getNativeCapacity();
    }

    /**
     * Returns the native frame's Object value.
     *
     * If the frame's base-type is not TYPE_OBJECT, this returns a data buffer containing the native
     * data (this is equivalent to calling getData().
     * If the frame is based on an object type, this type is expected to be a subclass of
     * NativeBuffer. The NativeBuffer returned is only valid for as long as the frame is alive. If
     * you need to hold on to the returned value, you must retain it.
     */
    @java.lang.Override
    public java.lang.Object getObjectValue() {
        // If this is not a structured frame, return our data
        if (getFormat().getBaseType() != android.filterfw.core.FrameFormat.TYPE_OBJECT) {
            return getData();
        }
        // Get the structure class
        java.lang.Class structClass = getFormat().getObjectClass();
        if (structClass == null) {
            throw new java.lang.RuntimeException("Attempting to get object data from frame that does " + "not specify a structure object class!");
        }
        // Make sure it is a NativeBuffer subclass
        if (!android.filterfw.core.NativeBuffer.class.isAssignableFrom(structClass)) {
            throw new java.lang.RuntimeException("NativeFrame object class must be a subclass of " + "NativeBuffer!");
        }
        // Instantiate a new empty instance of this class
        android.filterfw.core.NativeBuffer structData = null;
        try {
            structData = ((android.filterfw.core.NativeBuffer) (structClass.newInstance()));
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(("Could not instantiate new structure instance of type '" + structClass) + "'!");
        }
        // Wrap it around our data
        if (!getNativeBuffer(structData)) {
            throw new java.lang.RuntimeException("Could not get the native structured data for frame!");
        }
        // Attach this frame to it
        structData.attachToFrame(this);
        return structData;
    }

    @java.lang.Override
    public void setInts(int[] ints) {
        assertFrameMutable();
        if ((ints.length * android.filterfw.core.NativeFrame.nativeIntSize()) > getFormat().getSize()) {
            throw new java.lang.RuntimeException(((("NativeFrame cannot hold " + ints.length) + " integers. (Can only hold ") + (getFormat().getSize() / android.filterfw.core.NativeFrame.nativeIntSize())) + " integers).");
        } else
            if (!setNativeInts(ints)) {
                throw new java.lang.RuntimeException("Could not set int values for native frame!");
            }

    }

    @java.lang.Override
    public int[] getInts() {
        return getNativeInts(getFormat().getSize());
    }

    @java.lang.Override
    public void setFloats(float[] floats) {
        assertFrameMutable();
        if ((floats.length * android.filterfw.core.NativeFrame.nativeFloatSize()) > getFormat().getSize()) {
            throw new java.lang.RuntimeException(((("NativeFrame cannot hold " + floats.length) + " floats. (Can only hold ") + (getFormat().getSize() / android.filterfw.core.NativeFrame.nativeFloatSize())) + " floats).");
        } else
            if (!setNativeFloats(floats)) {
                throw new java.lang.RuntimeException("Could not set int values for native frame!");
            }

    }

    @java.lang.Override
    public float[] getFloats() {
        return getNativeFloats(getFormat().getSize());
    }

    // TODO: This function may be a bit confusing: Is the offset the target or source offset? Maybe
    // we should allow specifying both? (May be difficult for other frame types).
    @java.lang.Override
    public void setData(java.nio.ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        byte[] bytes = buffer.array();
        if ((length + offset) > buffer.limit()) {
            throw new java.lang.RuntimeException(((("Offset and length exceed buffer size in native setData: " + (length + offset)) + " bytes given, but only ") + buffer.limit()) + " bytes available!");
        } else
            if (getFormat().getSize() != length) {
                throw new java.lang.RuntimeException((((("Data size in setData does not match native frame size: " + "Frame size is ") + getFormat().getSize()) + " bytes, but ") + length) + " bytes given!");
            } else
                if (!setNativeData(bytes, offset, length)) {
                    throw new java.lang.RuntimeException("Could not set native frame data!");
                }


    }

    @java.lang.Override
    public java.nio.ByteBuffer getData() {
        byte[] data = getNativeData(getFormat().getSize());
        return data == null ? null : java.nio.ByteBuffer.wrap(data);
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        assertFrameMutable();
        if (getFormat().getNumberOfDimensions() != 2) {
            throw new java.lang.RuntimeException("Attempting to set Bitmap for non 2-dimensional native frame!");
        } else
            if ((getFormat().getWidth() != bitmap.getWidth()) || (getFormat().getHeight() != bitmap.getHeight())) {
                throw new java.lang.RuntimeException("Bitmap dimensions do not match native frame dimensions!");
            } else {
                android.graphics.Bitmap rgbaBitmap = android.filterfw.core.Frame.convertBitmapToRGBA(bitmap);
                int byteCount = rgbaBitmap.getByteCount();
                int bps = getFormat().getBytesPerSample();
                if (!setNativeBitmap(rgbaBitmap, byteCount, bps)) {
                    throw new java.lang.RuntimeException("Could not set native frame bitmap data!");
                }
            }

    }

    @java.lang.Override
    public android.graphics.Bitmap getBitmap() {
        if (getFormat().getNumberOfDimensions() != 2) {
            throw new java.lang.RuntimeException("Attempting to get Bitmap for non 2-dimensional native frame!");
        }
        android.graphics.Bitmap result = android.graphics.Bitmap.createBitmap(getFormat().getWidth(), getFormat().getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        int byteCount = result.getByteCount();
        int bps = getFormat().getBytesPerSample();
        if (!getNativeBitmap(result, byteCount, bps)) {
            throw new java.lang.RuntimeException("Could not get bitmap data from native frame!");
        }
        return result;
    }

    @java.lang.Override
    public void setDataFromFrame(android.filterfw.core.Frame frame) {
        // Make sure frame fits
        if (getFormat().getSize() < frame.getFormat().getSize()) {
            throw new java.lang.RuntimeException((((("Attempting to assign frame of size " + frame.getFormat().getSize()) + " to ") + "smaller native frame of size ") + getFormat().getSize()) + "!");
        }
        // Invoke optimized implementations if possible
        if (frame instanceof android.filterfw.core.NativeFrame) {
            nativeCopyFromNative(((android.filterfw.core.NativeFrame) (frame)));
        } else
            if (frame instanceof android.filterfw.core.GLFrame) {
                nativeCopyFromGL(((android.filterfw.core.GLFrame) (frame)));
            } else
                if (frame instanceof android.filterfw.core.SimpleFrame) {
                    setObjectValue(frame.getObjectValue());
                } else {
                    super.setDataFromFrame(frame);
                }


    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((("NativeFrame id: " + nativeFrameId) + " (") + getFormat()) + ") of size ") + getCapacity();
    }

    static {
        java.lang.System.loadLibrary("filterfw");
    }

    private native boolean nativeAllocate(int capacity);

    private native boolean nativeDeallocate();

    private native int getNativeCapacity();

    private static native int nativeIntSize();

    private static native int nativeFloatSize();

    private native boolean setNativeData(byte[] data, int offset, int length);

    private native byte[] getNativeData(int byteCount);

    private native boolean getNativeBuffer(android.filterfw.core.NativeBuffer buffer);

    private native boolean setNativeInts(int[] ints);

    private native boolean setNativeFloats(float[] floats);

    private native int[] getNativeInts(int byteCount);

    private native float[] getNativeFloats(int byteCount);

    private native boolean setNativeBitmap(android.graphics.Bitmap bitmap, int size, int bytesPerSample);

    private native boolean getNativeBitmap(android.graphics.Bitmap bitmap, int size, int bytesPerSample);

    private native boolean nativeCopyFromNative(android.filterfw.core.NativeFrame frame);

    private native boolean nativeCopyFromGL(android.filterfw.core.GLFrame frame);
}

