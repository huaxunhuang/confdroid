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
public class VertexFrame extends android.filterfw.core.Frame {
    private int vertexFrameId = -1;

    VertexFrame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager) {
        super(format, frameManager);
        if (getFormat().getSize() <= 0) {
            throw new java.lang.IllegalArgumentException("Initializing vertex frame with zero size!");
        } else {
            if (!nativeAllocate(getFormat().getSize())) {
                throw new java.lang.RuntimeException("Could not allocate vertex frame!");
            }
        }
    }

    @java.lang.Override
    protected synchronized boolean hasNativeAllocation() {
        return vertexFrameId != (-1);
    }

    @java.lang.Override
    protected synchronized void releaseNativeAllocation() {
        nativeDeallocate();
        vertexFrameId = -1;
    }

    @java.lang.Override
    public java.lang.Object getObjectValue() {
        throw new java.lang.RuntimeException("Vertex frames do not support reading data!");
    }

    @java.lang.Override
    public void setInts(int[] ints) {
        assertFrameMutable();
        if (!setNativeInts(ints)) {
            throw new java.lang.RuntimeException("Could not set int values for vertex frame!");
        }
    }

    @java.lang.Override
    public int[] getInts() {
        throw new java.lang.RuntimeException("Vertex frames do not support reading data!");
    }

    @java.lang.Override
    public void setFloats(float[] floats) {
        assertFrameMutable();
        if (!setNativeFloats(floats)) {
            throw new java.lang.RuntimeException("Could not set int values for vertex frame!");
        }
    }

    @java.lang.Override
    public float[] getFloats() {
        throw new java.lang.RuntimeException("Vertex frames do not support reading data!");
    }

    @java.lang.Override
    public void setData(java.nio.ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        byte[] bytes = buffer.array();
        if (getFormat().getSize() != bytes.length) {
            throw new java.lang.RuntimeException("Data size in setData does not match vertex frame size!");
        } else
            if (!setNativeData(bytes, offset, length)) {
                throw new java.lang.RuntimeException("Could not set vertex frame data!");
            }

    }

    @java.lang.Override
    public java.nio.ByteBuffer getData() {
        throw new java.lang.RuntimeException("Vertex frames do not support reading data!");
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        throw new java.lang.RuntimeException("Unsupported: Cannot set vertex frame bitmap value!");
    }

    @java.lang.Override
    public android.graphics.Bitmap getBitmap() {
        throw new java.lang.RuntimeException("Vertex frames do not support reading data!");
    }

    @java.lang.Override
    public void setDataFromFrame(android.filterfw.core.Frame frame) {
        // TODO: Optimize
        super.setDataFromFrame(frame);
    }

    public int getVboId() {
        return getNativeVboId();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("VertexFrame (" + getFormat()) + ") with VBO ID ") + getVboId();
    }

    static {
        java.lang.System.loadLibrary("filterfw");
    }

    private native boolean nativeAllocate(int size);

    private native boolean nativeDeallocate();

    private native boolean setNativeData(byte[] data, int offset, int length);

    private native boolean setNativeInts(int[] ints);

    private native boolean setNativeFloats(float[] floats);

    private native int getNativeVboId();
}

