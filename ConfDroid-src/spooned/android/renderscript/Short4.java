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
package android.renderscript;


/**
 * Vector version of the basic short type.
 * Provides four short fields packed.
 */
public class Short4 {
    public short x;

    public short y;

    public short z;

    public short w;

    public Short4() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Short4(short i) {
        this.x = this.y = this.z = this.w = i;
    }

    public Short4(short x, short y, short z, short w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     *
     *
     * @unknown 
     */
    public Short4(android.renderscript.Short4 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
        this.w = source.w;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param a
     * 		
     */
    public void add(android.renderscript.Short4 a) {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;
        this.w += a.w;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 add(android.renderscript.Short4 a, android.renderscript.Short4 b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x + b.x));
        result.y = ((short) (a.y + b.y));
        result.z = ((short) (a.z + b.z));
        result.w = ((short) (a.w + b.w));
        return result;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(short value) {
        x += value;
        y += value;
        z += value;
        w += value;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 add(android.renderscript.Short4 a, short b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x + b));
        result.y = ((short) (a.y + b));
        result.z = ((short) (a.z + b));
        result.w = ((short) (a.w + b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     */
    public void sub(android.renderscript.Short4 a) {
        this.x -= a.x;
        this.y -= a.y;
        this.z -= a.z;
        this.w -= a.w;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 sub(android.renderscript.Short4 a, android.renderscript.Short4 b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x - b.x));
        result.y = ((short) (a.y - b.y));
        result.z = ((short) (a.z - b.z));
        result.w = ((short) (a.w - b.w));
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(short value) {
        x -= value;
        y -= value;
        z -= value;
        w -= value;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 sub(android.renderscript.Short4 a, short b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x - b));
        result.y = ((short) (a.y - b));
        result.z = ((short) (a.z - b));
        result.w = ((short) (a.w - b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     */
    public void mul(android.renderscript.Short4 a) {
        this.x *= a.x;
        this.y *= a.y;
        this.z *= a.z;
        this.w *= a.w;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 mul(android.renderscript.Short4 a, android.renderscript.Short4 b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x * b.x));
        result.y = ((short) (a.y * b.y));
        result.z = ((short) (a.z * b.z));
        result.w = ((short) (a.w * b.w));
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(short value) {
        x *= value;
        y *= value;
        z *= value;
        w *= value;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 mul(android.renderscript.Short4 a, short b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x * b));
        result.y = ((short) (a.y * b));
        result.z = ((short) (a.z * b));
        result.w = ((short) (a.w * b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     */
    public void div(android.renderscript.Short4 a) {
        this.x /= a.x;
        this.y /= a.y;
        this.z /= a.z;
        this.w /= a.w;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 div(android.renderscript.Short4 a, android.renderscript.Short4 b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x / b.x));
        result.y = ((short) (a.y / b.y));
        result.z = ((short) (a.z / b.z));
        result.w = ((short) (a.w / b.w));
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(short value) {
        x /= value;
        y /= value;
        z /= value;
        w /= value;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 div(android.renderscript.Short4 a, short b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x / b));
        result.y = ((short) (a.y / b));
        result.z = ((short) (a.z / b));
        result.w = ((short) (a.w / b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     */
    public void mod(android.renderscript.Short4 a) {
        this.x %= a.x;
        this.y %= a.y;
        this.z %= a.z;
        this.w %= a.w;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 mod(android.renderscript.Short4 a, android.renderscript.Short4 b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x % b.x));
        result.y = ((short) (a.y % b.y));
        result.z = ((short) (a.z % b.z));
        result.w = ((short) (a.w % b.w));
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param value
     * 		
     */
    public void mod(short value) {
        x %= value;
        y %= value;
        z %= value;
        w %= value;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static android.renderscript.Short4 mod(android.renderscript.Short4 a, short b) {
        android.renderscript.Short4 result = new android.renderscript.Short4();
        result.x = ((short) (a.x % b));
        result.y = ((short) (a.y % b));
        result.z = ((short) (a.z % b));
        result.w = ((short) (a.w % b));
        return result;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public short length() {
        return 4;
    }

    /**
     *
     *
     * @unknown set vector negate
     */
    public void negate() {
        this.x = ((short) (-x));
        this.y = ((short) (-y));
        this.z = ((short) (-z));
        this.w = ((short) (-w));
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public short dotProduct(android.renderscript.Short4 a) {
        return ((short) ((((x * a.x) + (y * a.y)) + (z * a.z)) + (w * a.w)));
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @param b
     * 		
     * @return 
     */
    public static short dotProduct(android.renderscript.Short4 a, android.renderscript.Short4 b) {
        return ((short) ((((b.x * a.x) + (b.y * a.y)) + (b.z * a.z)) + (b.w * a.w)));
    }

    /**
     *
     *
     * @unknown Vector add Multiple
     * @param a
     * 		
     * @param factor
     * 		
     */
    public void addMultiple(android.renderscript.Short4 a, short factor) {
        x += a.x * factor;
        y += a.y * factor;
        z += a.z * factor;
        w += a.w * factor;
    }

    /**
     *
     *
     * @unknown set vector value by Short4
     * @param a
     * 		
     */
    public void set(android.renderscript.Short4 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.w = a.w;
    }

    /**
     *
     *
     * @unknown set the vector field value by Short
     * @param a
     * 		
     * @param b
     * 		
     * @param c
     * 		
     * @param d
     * 		
     */
    public void setValues(short a, short b, short c, short d) {
        this.x = a;
        this.y = b;
        this.z = c;
        this.w = d;
    }

    /**
     *
     *
     * @unknown return the element sum of vector
     * @return 
     */
    public short elementSum() {
        return ((short) (((x + y) + z) + w));
    }

    /**
     *
     *
     * @unknown get the vector field value by index
     * @param i
     * 		
     * @return 
     */
    public short get(int i) {
        switch (i) {
            case 0 :
                return ((short) (x));
            case 1 :
                return ((short) (y));
            case 2 :
                return ((short) (z));
            case 3 :
                return ((short) (w));
            default :
                throw new java.lang.IndexOutOfBoundsException("Index: i");
        }
    }

    /**
     *
     *
     * @unknown set the vector field value by index
     * @param i
     * 		
     * @param value
     * 		
     */
    public void setAt(int i, short value) {
        switch (i) {
            case 0 :
                x = value;
                return;
            case 1 :
                y = value;
                return;
            case 2 :
                z = value;
                return;
            case 3 :
                w = value;
                return;
            default :
                throw new java.lang.IndexOutOfBoundsException("Index: i");
        }
    }

    /**
     *
     *
     * @unknown add the vector field value by index
     * @param i
     * 		
     * @param value
     * 		
     */
    public void addAt(int i, short value) {
        switch (i) {
            case 0 :
                x += value;
                return;
            case 1 :
                y += value;
                return;
            case 2 :
                z += value;
                return;
            case 3 :
                w += value;
                return;
            default :
                throw new java.lang.IndexOutOfBoundsException("Index: i");
        }
    }

    /**
     *
     *
     * @unknown copy the vector to short array
     * @param data
     * 		
     * @param offset
     * 		
     */
    public void copyTo(short[] data, int offset) {
        data[offset] = ((short) (x));
        data[offset + 1] = ((short) (y));
        data[offset + 2] = ((short) (z));
        data[offset + 3] = ((short) (w));
    }
}

