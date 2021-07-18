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
public class SimpleFrame extends android.filterfw.core.Frame {
    private java.lang.Object mObject;

    SimpleFrame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager) {
        super(format, frameManager);
        initWithFormat(format);
        setReusable(false);
    }

    static android.filterfw.core.SimpleFrame wrapObject(java.lang.Object object, android.filterfw.core.FrameManager frameManager) {
        android.filterfw.core.FrameFormat format = android.filterfw.format.ObjectFormat.fromObject(object, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        android.filterfw.core.SimpleFrame result = new android.filterfw.core.SimpleFrame(format, frameManager);
        result.setObjectValue(object);
        return result;
    }

    private void initWithFormat(android.filterfw.core.FrameFormat format) {
        final int count = format.getLength();
        final int baseType = format.getBaseType();
        switch (baseType) {
            case android.filterfw.core.FrameFormat.TYPE_BYTE :
                mObject = new byte[count];
                break;
            case android.filterfw.core.FrameFormat.TYPE_INT16 :
                mObject = new short[count];
                break;
            case android.filterfw.core.FrameFormat.TYPE_INT32 :
                mObject = new int[count];
                break;
            case android.filterfw.core.FrameFormat.TYPE_FLOAT :
                mObject = new float[count];
                break;
            case android.filterfw.core.FrameFormat.TYPE_DOUBLE :
                mObject = new double[count];
                break;
            default :
                mObject = null;
                break;
        }
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
        return mObject;
    }

    @java.lang.Override
    public void setInts(int[] ints) {
        assertFrameMutable();
        setGenericObjectValue(ints);
    }

    @java.lang.Override
    public int[] getInts() {
        return mObject instanceof int[] ? ((int[]) (mObject)) : null;
    }

    @java.lang.Override
    public void setFloats(float[] floats) {
        assertFrameMutable();
        setGenericObjectValue(floats);
    }

    @java.lang.Override
    public float[] getFloats() {
        return mObject instanceof float[] ? ((float[]) (mObject)) : null;
    }

    @java.lang.Override
    public void setData(java.nio.ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        setGenericObjectValue(java.nio.ByteBuffer.wrap(buffer.array(), offset, length));
    }

    @java.lang.Override
    public java.nio.ByteBuffer getData() {
        return mObject instanceof java.nio.ByteBuffer ? ((java.nio.ByteBuffer) (mObject)) : null;
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        assertFrameMutable();
        setGenericObjectValue(bitmap);
    }

    @java.lang.Override
    public android.graphics.Bitmap getBitmap() {
        return mObject instanceof android.graphics.Bitmap ? ((android.graphics.Bitmap) (mObject)) : null;
    }

    private void setFormatObjectClass(java.lang.Class objectClass) {
        android.filterfw.core.MutableFrameFormat format = getFormat().mutableCopy();
        format.setObjectClass(objectClass);
        setFormat(format);
    }

    @java.lang.Override
    protected void setGenericObjectValue(java.lang.Object object) {
        // Update the FrameFormat class
        // TODO: Take this out! FrameFormats should not be modified and convenience formats used
        // instead!
        android.filterfw.core.FrameFormat format = getFormat();
        if (format.getObjectClass() == null) {
            setFormatObjectClass(object.getClass());
        } else
            if (!format.getObjectClass().isAssignableFrom(object.getClass())) {
                throw new java.lang.RuntimeException((((("Attempting to set object value of type '" + object.getClass()) + "' on ") + "SimpleFrame of type '") + format.getObjectClass()) + "'!");
            }

        // Set the object value
        mObject = object;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("SimpleFrame (" + getFormat()) + ")";
    }
}

