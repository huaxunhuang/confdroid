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
package android.support.v8.renderscript;


/**
 * Utility class for packing arguments and structures from Android system objects to
 * RenderScript objects.
 *
 * This class is only intended to be used to support the
 * reflected code generated by the RS tool chain.  It should not
 * be called directly.
 */
public class FieldPacker {
    public FieldPacker(int len) {
        mPos = 0;
        mLen = len;
        mData = new byte[len];
        mAlignment = new java.util.BitSet();
    }

    public FieldPacker(byte[] data) {
        // Advance mPos to the end of the buffer, since we are copying in the
        // full data input.
        mPos = data.length;
        mLen = data.length;
        mData = data;
        mAlignment = new java.util.BitSet();
        // TODO: We should either have an actual FieldPacker copy constructor
        // or drop support for computing alignment like this. As it stands,
        // subAlign() can never work correctly for copied FieldPacker objects.
    }

    static android.support.v8.renderscript.FieldPacker createFromArray(java.lang.Object[] args) {
        android.support.v8.renderscript.FieldPacker fp = new android.support.v8.renderscript.FieldPacker(android.support.v8.renderscript.RenderScript.sPointerSize * 8);
        for (java.lang.Object arg : args) {
            fp.addSafely(arg);
        }
        fp.resize(fp.mPos);
        return fp;
    }

    public void align(int v) {
        if ((v <= 0) || ((v & (v - 1)) != 0)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("argument must be a non-negative non-zero power of 2: " + v);
        }
        while ((mPos & (v - 1)) != 0) {
            mAlignment.flip(mPos);
            mData[mPos++] = 0;
        } 
    }

    public void subalign(int v) {
        if ((v & (v - 1)) != 0) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("argument must be a non-negative non-zero power of 2: " + v);
        }
        while ((mPos & (v - 1)) != 0) {
            mPos--;
        } 
        if (mPos > 0) {
            while (mAlignment.get(mPos - 1) == true) {
                mPos--;
                mAlignment.flip(mPos);
            } 
        }
    }

    public void reset() {
        mPos = 0;
    }

    public void reset(int i) {
        if ((i < 0) || (i > mLen)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("out of range argument: " + i);
        }
        mPos = i;
    }

    public void skip(int i) {
        int res = mPos + i;
        if ((res < 0) || (res > mLen)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("out of range argument: " + i);
        }
        mPos = res;
    }

    public void addI8(byte v) {
        mData[mPos++] = v;
    }

    public byte subI8() {
        subalign(1);
        return mData[--mPos];
    }

    public void addI16(short v) {
        align(2);
        mData[mPos++] = ((byte) (v & 0xff));
        mData[mPos++] = ((byte) (v >> 8));
    }

    public short subI16() {
        subalign(2);
        short v = 0;
        v = ((short) ((mData[--mPos] & 0xff) << 8));
        v = ((short) (v | ((short) (mData[--mPos] & 0xff))));
        return v;
    }

    public void addI32(int v) {
        align(4);
        mData[mPos++] = ((byte) (v & 0xff));
        mData[mPos++] = ((byte) ((v >> 8) & 0xff));
        mData[mPos++] = ((byte) ((v >> 16) & 0xff));
        mData[mPos++] = ((byte) ((v >> 24) & 0xff));
    }

    public int subI32() {
        subalign(4);
        int v = 0;
        v = (mData[--mPos] & 0xff) << 24;
        v = v | ((mData[--mPos] & 0xff) << 16);
        v = v | ((mData[--mPos] & 0xff) << 8);
        v = v | (mData[--mPos] & 0xff);
        return v;
    }

    public void addI64(long v) {
        align(8);
        mData[mPos++] = ((byte) (v & 0xff));
        mData[mPos++] = ((byte) ((v >> 8) & 0xff));
        mData[mPos++] = ((byte) ((v >> 16) & 0xff));
        mData[mPos++] = ((byte) ((v >> 24) & 0xff));
        mData[mPos++] = ((byte) ((v >> 32) & 0xff));
        mData[mPos++] = ((byte) ((v >> 40) & 0xff));
        mData[mPos++] = ((byte) ((v >> 48) & 0xff));
        mData[mPos++] = ((byte) ((v >> 56) & 0xff));
    }

    public long subI64() {
        subalign(8);
        long v = 0;
        byte x = 0;
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 56L)));
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 48L)));
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 40L)));
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 32L)));
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 24L)));
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 16L)));
        x = mData[--mPos];
        v = ((long) (v | ((((long) (x)) & 0xff) << 8L)));
        x = mData[--mPos];
        v = ((long) (v | (((long) (x)) & 0xff)));
        return v;
    }

    public void addU8(short v) {
        if ((v < 0) || (v > 0xff)) {
            android.util.Log.e("rs", ("FieldPacker.addU8( " + v) + " )");
            throw new java.lang.IllegalArgumentException("Saving value out of range for type");
        }
        mData[mPos++] = ((byte) (v));
    }

    public void addU16(int v) {
        if ((v < 0) || (v > 0xffff)) {
            android.util.Log.e("rs", ("FieldPacker.addU16( " + v) + " )");
            throw new java.lang.IllegalArgumentException("Saving value out of range for type");
        }
        align(2);
        mData[mPos++] = ((byte) (v & 0xff));
        mData[mPos++] = ((byte) (v >> 8));
    }

    public void addU32(long v) {
        if ((v < 0) || (v > 0xffffffffL)) {
            android.util.Log.e("rs", ("FieldPacker.addU32( " + v) + " )");
            throw new java.lang.IllegalArgumentException("Saving value out of range for type");
        }
        align(4);
        mData[mPos++] = ((byte) (v & 0xff));
        mData[mPos++] = ((byte) ((v >> 8) & 0xff));
        mData[mPos++] = ((byte) ((v >> 16) & 0xff));
        mData[mPos++] = ((byte) ((v >> 24) & 0xff));
    }

    public void addU64(long v) {
        if (v < 0) {
            android.util.Log.e("rs", ("FieldPacker.addU64( " + v) + " )");
            throw new java.lang.IllegalArgumentException("Saving value out of range for type");
        }
        align(8);
        mData[mPos++] = ((byte) (v & 0xff));
        mData[mPos++] = ((byte) ((v >> 8) & 0xff));
        mData[mPos++] = ((byte) ((v >> 16) & 0xff));
        mData[mPos++] = ((byte) ((v >> 24) & 0xff));
        mData[mPos++] = ((byte) ((v >> 32) & 0xff));
        mData[mPos++] = ((byte) ((v >> 40) & 0xff));
        mData[mPos++] = ((byte) ((v >> 48) & 0xff));
        mData[mPos++] = ((byte) ((v >> 56) & 0xff));
    }

    public void addF32(float v) {
        addI32(java.lang.Float.floatToRawIntBits(v));
    }

    public float subF32() {
        return java.lang.Float.intBitsToFloat(subI32());
    }

    public void addF64(double v) {
        addI64(java.lang.Double.doubleToRawLongBits(v));
    }

    public double subF64() {
        return java.lang.Double.longBitsToDouble(subI64());
    }

    public void addObj(android.support.v8.renderscript.BaseObj obj) {
        if (obj != null) {
            if (android.support.v8.renderscript.RenderScript.sPointerSize == 8) {
                addI64(obj.getID(null));
                addI64(0);
                addI64(0);
                addI64(0);
            } else {
                addI32(((int) (obj.getID(null))));
            }
        } else {
            if (android.support.v8.renderscript.RenderScript.sPointerSize == 8) {
                addI64(0);
                addI64(0);
                addI64(0);
                addI64(0);
            } else {
                addI32(0);
            }
        }
    }

    public void addF32(android.support.v8.renderscript.Float2 v) {
        addF32(v.x);
        addF32(v.y);
    }

    public void addF32(android.support.v8.renderscript.Float3 v) {
        addF32(v.x);
        addF32(v.y);
        addF32(v.z);
    }

    public void addF32(android.support.v8.renderscript.Float4 v) {
        addF32(v.x);
        addF32(v.y);
        addF32(v.z);
        addF32(v.w);
    }

    public void addF64(android.support.v8.renderscript.Double2 v) {
        addF64(v.x);
        addF64(v.y);
    }

    public void addF64(android.support.v8.renderscript.Double3 v) {
        addF64(v.x);
        addF64(v.y);
        addF64(v.z);
    }

    public void addF64(android.support.v8.renderscript.Double4 v) {
        addF64(v.x);
        addF64(v.y);
        addF64(v.z);
        addF64(v.w);
    }

    public void addI8(android.support.v8.renderscript.Byte2 v) {
        addI8(v.x);
        addI8(v.y);
    }

    public void addI8(android.support.v8.renderscript.Byte3 v) {
        addI8(v.x);
        addI8(v.y);
        addI8(v.z);
    }

    public void addI8(android.support.v8.renderscript.Byte4 v) {
        addI8(v.x);
        addI8(v.y);
        addI8(v.z);
        addI8(v.w);
    }

    public void addU8(android.support.v8.renderscript.Short2 v) {
        addU8(v.x);
        addU8(v.y);
    }

    public void addU8(android.support.v8.renderscript.Short3 v) {
        addU8(v.x);
        addU8(v.y);
        addU8(v.z);
    }

    public void addU8(android.support.v8.renderscript.Short4 v) {
        addU8(v.x);
        addU8(v.y);
        addU8(v.z);
        addU8(v.w);
    }

    public void addI16(android.support.v8.renderscript.Short2 v) {
        addI16(v.x);
        addI16(v.y);
    }

    public void addI16(android.support.v8.renderscript.Short3 v) {
        addI16(v.x);
        addI16(v.y);
        addI16(v.z);
    }

    public void addI16(android.support.v8.renderscript.Short4 v) {
        addI16(v.x);
        addI16(v.y);
        addI16(v.z);
        addI16(v.w);
    }

    public void addU16(android.support.v8.renderscript.Int2 v) {
        addU16(v.x);
        addU16(v.y);
    }

    public void addU16(android.support.v8.renderscript.Int3 v) {
        addU16(v.x);
        addU16(v.y);
        addU16(v.z);
    }

    public void addU16(android.support.v8.renderscript.Int4 v) {
        addU16(v.x);
        addU16(v.y);
        addU16(v.z);
        addU16(v.w);
    }

    public void addI32(android.support.v8.renderscript.Int2 v) {
        addI32(v.x);
        addI32(v.y);
    }

    public void addI32(android.support.v8.renderscript.Int3 v) {
        addI32(v.x);
        addI32(v.y);
        addI32(v.z);
    }

    public void addI32(android.support.v8.renderscript.Int4 v) {
        addI32(v.x);
        addI32(v.y);
        addI32(v.z);
        addI32(v.w);
    }

    public void addU32(android.support.v8.renderscript.Long2 v) {
        addU32(v.x);
        addU32(v.y);
    }

    public void addU32(android.support.v8.renderscript.Long3 v) {
        addU32(v.x);
        addU32(v.y);
        addU32(v.z);
    }

    public void addU32(android.support.v8.renderscript.Long4 v) {
        addU32(v.x);
        addU32(v.y);
        addU32(v.z);
        addU32(v.w);
    }

    public void addI64(android.support.v8.renderscript.Long2 v) {
        addI64(v.x);
        addI64(v.y);
    }

    public void addI64(android.support.v8.renderscript.Long3 v) {
        addI64(v.x);
        addI64(v.y);
        addI64(v.z);
    }

    public void addI64(android.support.v8.renderscript.Long4 v) {
        addI64(v.x);
        addI64(v.y);
        addI64(v.z);
        addI64(v.w);
    }

    public void addU64(android.support.v8.renderscript.Long2 v) {
        addU64(v.x);
        addU64(v.y);
    }

    public void addU64(android.support.v8.renderscript.Long3 v) {
        addU64(v.x);
        addU64(v.y);
        addU64(v.z);
    }

    public void addU64(android.support.v8.renderscript.Long4 v) {
        addU64(v.x);
        addU64(v.y);
        addU64(v.z);
        addU64(v.w);
    }

    public android.support.v8.renderscript.Float2 subFloat2() {
        android.support.v8.renderscript.Float2 v = new android.support.v8.renderscript.Float2();
        v.y = subF32();
        v.x = subF32();
        return v;
    }

    public android.support.v8.renderscript.Float3 subFloat3() {
        android.support.v8.renderscript.Float3 v = new android.support.v8.renderscript.Float3();
        v.z = subF32();
        v.y = subF32();
        v.x = subF32();
        return v;
    }

    public android.support.v8.renderscript.Float4 subFloat4() {
        android.support.v8.renderscript.Float4 v = new android.support.v8.renderscript.Float4();
        v.w = subF32();
        v.z = subF32();
        v.y = subF32();
        v.x = subF32();
        return v;
    }

    public android.support.v8.renderscript.Double2 subDouble2() {
        android.support.v8.renderscript.Double2 v = new android.support.v8.renderscript.Double2();
        v.y = subF64();
        v.x = subF64();
        return v;
    }

    public android.support.v8.renderscript.Double3 subDouble3() {
        android.support.v8.renderscript.Double3 v = new android.support.v8.renderscript.Double3();
        v.z = subF64();
        v.y = subF64();
        v.x = subF64();
        return v;
    }

    public android.support.v8.renderscript.Double4 subDouble4() {
        android.support.v8.renderscript.Double4 v = new android.support.v8.renderscript.Double4();
        v.w = subF64();
        v.z = subF64();
        v.y = subF64();
        v.x = subF64();
        return v;
    }

    public android.support.v8.renderscript.Byte2 subByte2() {
        android.support.v8.renderscript.Byte2 v = new android.support.v8.renderscript.Byte2();
        v.y = subI8();
        v.x = subI8();
        return v;
    }

    public android.support.v8.renderscript.Byte3 subByte3() {
        android.support.v8.renderscript.Byte3 v = new android.support.v8.renderscript.Byte3();
        v.z = subI8();
        v.y = subI8();
        v.x = subI8();
        return v;
    }

    public android.support.v8.renderscript.Byte4 subByte4() {
        android.support.v8.renderscript.Byte4 v = new android.support.v8.renderscript.Byte4();
        v.w = subI8();
        v.z = subI8();
        v.y = subI8();
        v.x = subI8();
        return v;
    }

    public android.support.v8.renderscript.Short2 subShort2() {
        android.support.v8.renderscript.Short2 v = new android.support.v8.renderscript.Short2();
        v.y = subI16();
        v.x = subI16();
        return v;
    }

    public android.support.v8.renderscript.Short3 subShort3() {
        android.support.v8.renderscript.Short3 v = new android.support.v8.renderscript.Short3();
        v.z = subI16();
        v.y = subI16();
        v.x = subI16();
        return v;
    }

    public android.support.v8.renderscript.Short4 subShort4() {
        android.support.v8.renderscript.Short4 v = new android.support.v8.renderscript.Short4();
        v.w = subI16();
        v.z = subI16();
        v.y = subI16();
        v.x = subI16();
        return v;
    }

    public android.support.v8.renderscript.Int2 subInt2() {
        android.support.v8.renderscript.Int2 v = new android.support.v8.renderscript.Int2();
        v.y = subI32();
        v.x = subI32();
        return v;
    }

    public android.support.v8.renderscript.Int3 subInt3() {
        android.support.v8.renderscript.Int3 v = new android.support.v8.renderscript.Int3();
        v.z = subI32();
        v.y = subI32();
        v.x = subI32();
        return v;
    }

    public android.support.v8.renderscript.Int4 subInt4() {
        android.support.v8.renderscript.Int4 v = new android.support.v8.renderscript.Int4();
        v.w = subI32();
        v.z = subI32();
        v.y = subI32();
        v.x = subI32();
        return v;
    }

    public android.support.v8.renderscript.Long2 subLong2() {
        android.support.v8.renderscript.Long2 v = new android.support.v8.renderscript.Long2();
        v.y = subI64();
        v.x = subI64();
        return v;
    }

    public android.support.v8.renderscript.Long3 subLong3() {
        android.support.v8.renderscript.Long3 v = new android.support.v8.renderscript.Long3();
        v.z = subI64();
        v.y = subI64();
        v.x = subI64();
        return v;
    }

    public android.support.v8.renderscript.Long4 subLong4() {
        android.support.v8.renderscript.Long4 v = new android.support.v8.renderscript.Long4();
        v.w = subI64();
        v.z = subI64();
        v.y = subI64();
        v.x = subI64();
        return v;
    }

    public void addMatrix(android.support.v8.renderscript.Matrix4f v) {
        for (int i = 0; i < v.mMat.length; i++) {
            addF32(v.mMat[i]);
        }
    }

    public android.support.v8.renderscript.Matrix4f subMatrix4f() {
        android.support.v8.renderscript.Matrix4f v = new android.support.v8.renderscript.Matrix4f();
        for (int i = v.mMat.length - 1; i >= 0; i--) {
            v.mMat[i] = subF32();
        }
        return v;
    }

    public void addMatrix(android.support.v8.renderscript.Matrix3f v) {
        for (int i = 0; i < v.mMat.length; i++) {
            addF32(v.mMat[i]);
        }
    }

    public android.support.v8.renderscript.Matrix3f subMatrix3f() {
        android.support.v8.renderscript.Matrix3f v = new android.support.v8.renderscript.Matrix3f();
        for (int i = v.mMat.length - 1; i >= 0; i--) {
            v.mMat[i] = subF32();
        }
        return v;
    }

    public void addMatrix(android.support.v8.renderscript.Matrix2f v) {
        for (int i = 0; i < v.mMat.length; i++) {
            addF32(v.mMat[i]);
        }
    }

    public android.support.v8.renderscript.Matrix2f subMatrix2f() {
        android.support.v8.renderscript.Matrix2f v = new android.support.v8.renderscript.Matrix2f();
        for (int i = v.mMat.length - 1; i >= 0; i--) {
            v.mMat[i] = subF32();
        }
        return v;
    }

    public void addBoolean(boolean v) {
        addI8(((byte) (v ? 1 : 0)));
    }

    public boolean subBoolean() {
        byte v = subI8();
        if (v == 1) {
            return true;
        }
        return false;
    }

    public final byte[] getData() {
        return mData;
    }

    /**
     * Get the actual length used for the FieldPacker.
     *
     * @unknown 
     */
    public int getPos() {
        return mPos;
    }

    private static void addToPack(android.support.v8.renderscript.FieldPacker fp, java.lang.Object obj) {
        if (obj instanceof java.lang.Boolean) {
            fp.addBoolean(((java.lang.Boolean) (obj)).booleanValue());
            return;
        }
        if (obj instanceof java.lang.Byte) {
            fp.addI8(((java.lang.Byte) (obj)).byteValue());
            return;
        }
        if (obj instanceof java.lang.Short) {
            fp.addI16(((java.lang.Short) (obj)).shortValue());
            return;
        }
        if (obj instanceof java.lang.Integer) {
            fp.addI32(((java.lang.Integer) (obj)).intValue());
            return;
        }
        if (obj instanceof java.lang.Long) {
            fp.addI64(((java.lang.Long) (obj)).longValue());
            return;
        }
        if (obj instanceof java.lang.Float) {
            fp.addF32(((java.lang.Float) (obj)).floatValue());
            return;
        }
        if (obj instanceof java.lang.Double) {
            fp.addF64(((java.lang.Double) (obj)).doubleValue());
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Byte2) {
            fp.addI8(((android.support.v8.renderscript.Byte2) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Byte3) {
            fp.addI8(((android.support.v8.renderscript.Byte3) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Byte4) {
            fp.addI8(((android.support.v8.renderscript.Byte4) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Short2) {
            fp.addI16(((android.support.v8.renderscript.Short2) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Short3) {
            fp.addI16(((android.support.v8.renderscript.Short3) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Short4) {
            fp.addI16(((android.support.v8.renderscript.Short4) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Int2) {
            fp.addI32(((android.support.v8.renderscript.Int2) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Int3) {
            fp.addI32(((android.support.v8.renderscript.Int3) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Int4) {
            fp.addI32(((android.support.v8.renderscript.Int4) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Long2) {
            fp.addI64(((android.support.v8.renderscript.Long2) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Long3) {
            fp.addI64(((android.support.v8.renderscript.Long3) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Long4) {
            fp.addI64(((android.support.v8.renderscript.Long4) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Float2) {
            fp.addF32(((android.support.v8.renderscript.Float2) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Float3) {
            fp.addF32(((android.support.v8.renderscript.Float3) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Float4) {
            fp.addF32(((android.support.v8.renderscript.Float4) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Double2) {
            fp.addF64(((android.support.v8.renderscript.Double2) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Double3) {
            fp.addF64(((android.support.v8.renderscript.Double3) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Double4) {
            fp.addF64(((android.support.v8.renderscript.Double4) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Matrix2f) {
            fp.addMatrix(((android.support.v8.renderscript.Matrix2f) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Matrix3f) {
            fp.addMatrix(((android.support.v8.renderscript.Matrix3f) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.Matrix4f) {
            fp.addMatrix(((android.support.v8.renderscript.Matrix4f) (obj)));
            return;
        }
        if (obj instanceof android.support.v8.renderscript.BaseObj) {
            fp.addObj(((android.support.v8.renderscript.BaseObj) (obj)));
            return;
        }
    }

    private static int getPackedSize(java.lang.Object obj) {
        if (obj instanceof java.lang.Boolean) {
            return 1;
        }
        if (obj instanceof java.lang.Byte) {
            return 1;
        }
        if (obj instanceof java.lang.Short) {
            return 2;
        }
        if (obj instanceof java.lang.Integer) {
            return 4;
        }
        if (obj instanceof java.lang.Long) {
            return 8;
        }
        if (obj instanceof java.lang.Float) {
            return 4;
        }
        if (obj instanceof java.lang.Double) {
            return 8;
        }
        if (obj instanceof android.support.v8.renderscript.Byte2) {
            return 2;
        }
        if (obj instanceof android.support.v8.renderscript.Byte3) {
            return 3;
        }
        if (obj instanceof android.support.v8.renderscript.Byte4) {
            return 4;
        }
        if (obj instanceof android.support.v8.renderscript.Short2) {
            return 4;
        }
        if (obj instanceof android.support.v8.renderscript.Short3) {
            return 6;
        }
        if (obj instanceof android.support.v8.renderscript.Short4) {
            return 8;
        }
        if (obj instanceof android.support.v8.renderscript.Int2) {
            return 8;
        }
        if (obj instanceof android.support.v8.renderscript.Int3) {
            return 12;
        }
        if (obj instanceof android.support.v8.renderscript.Int4) {
            return 16;
        }
        if (obj instanceof android.support.v8.renderscript.Long2) {
            return 16;
        }
        if (obj instanceof android.support.v8.renderscript.Long3) {
            return 24;
        }
        if (obj instanceof android.support.v8.renderscript.Long4) {
            return 32;
        }
        if (obj instanceof android.support.v8.renderscript.Float2) {
            return 8;
        }
        if (obj instanceof android.support.v8.renderscript.Float3) {
            return 12;
        }
        if (obj instanceof android.support.v8.renderscript.Float4) {
            return 16;
        }
        if (obj instanceof android.support.v8.renderscript.Double2) {
            return 16;
        }
        if (obj instanceof android.support.v8.renderscript.Double3) {
            return 24;
        }
        if (obj instanceof android.support.v8.renderscript.Double4) {
            return 32;
        }
        if (obj instanceof android.support.v8.renderscript.Matrix2f) {
            return 16;
        }
        if (obj instanceof android.support.v8.renderscript.Matrix3f) {
            return 36;
        }
        if (obj instanceof android.support.v8.renderscript.Matrix4f) {
            return 64;
        }
        if (obj instanceof android.support.v8.renderscript.BaseObj) {
            if (android.support.v8.renderscript.RenderScript.sPointerSize == 8) {
                return 32;
            } else {
                return 4;
            }
        }
        return 0;
    }

    static android.support.v8.renderscript.FieldPacker createFieldPack(java.lang.Object[] args) {
        int len = 0;
        for (java.lang.Object arg : args) {
            len += android.support.v8.renderscript.FieldPacker.getPackedSize(arg);
        }
        android.support.v8.renderscript.FieldPacker fp = new android.support.v8.renderscript.FieldPacker(len);
        for (java.lang.Object arg : args) {
            android.support.v8.renderscript.FieldPacker.addToPack(fp, arg);
        }
        return fp;
    }

    private boolean resize(int newSize) {
        if (newSize == mLen) {
            return false;
        }
        byte[] newData = new byte[newSize];
        java.lang.System.arraycopy(mData, 0, newData, 0, mPos);
        mData = newData;
        mLen = newSize;
        return true;
    }

    private void addSafely(java.lang.Object obj) {
        boolean retry;
        final int oldPos = mPos;
        do {
            retry = false;
            try {
                android.support.v8.renderscript.FieldPacker.addToPack(this, obj);
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                mPos = oldPos;
                resize(mLen * 2);
                retry = true;
            }
        } while (retry );
    }

    private byte[] mData;

    private int mPos;

    private int mLen;

    private java.util.BitSet mAlignment;
}

