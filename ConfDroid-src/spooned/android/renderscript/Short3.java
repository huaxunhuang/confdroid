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
 * Provides three short fields packed.
 */
public class Short3 {
    public short x;

    public short y;

    public short z;

    public Short3() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Short3(short i) {
        this.x = this.y = this.z = i;
    }

    public Short3(short x, short y, short z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     *
     * @unknown 
     */
    public Short3(android.renderscript.Short3 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param a
     * 		
     */
    public void add(android.renderscript.Short3 a) {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;
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
    public static android.renderscript.Short3 add(android.renderscript.Short3 a, android.renderscript.Short3 b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x + b.x));
        result.y = ((short) (a.y + b.y));
        result.z = ((short) (a.z + b.z));
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
    public static android.renderscript.Short3 add(android.renderscript.Short3 a, short b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x + b));
        result.y = ((short) (a.y + b));
        result.z = ((short) (a.z + b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     */
    public void sub(android.renderscript.Short3 a) {
        this.x -= a.x;
        this.y -= a.y;
        this.z -= a.z;
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
    public static android.renderscript.Short3 sub(android.renderscript.Short3 a, android.renderscript.Short3 b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x - b.x));
        result.y = ((short) (a.y - b.y));
        result.z = ((short) (a.z - b.z));
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
    public static android.renderscript.Short3 sub(android.renderscript.Short3 a, short b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x - b));
        result.y = ((short) (a.y - b));
        result.z = ((short) (a.z - b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     */
    public void mul(android.renderscript.Short3 a) {
        this.x *= a.x;
        this.y *= a.y;
        this.z *= a.z;
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
    public static android.renderscript.Short3 mul(android.renderscript.Short3 a, android.renderscript.Short3 b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x * b.x));
        result.y = ((short) (a.y * b.y));
        result.z = ((short) (a.z * b.z));
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
    public static android.renderscript.Short3 mul(android.renderscript.Short3 a, short b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x * b));
        result.y = ((short) (a.y * b));
        result.z = ((short) (a.z * b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     */
    public void div(android.renderscript.Short3 a) {
        this.x /= a.x;
        this.y /= a.y;
        this.z /= a.z;
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
    public static android.renderscript.Short3 div(android.renderscript.Short3 a, android.renderscript.Short3 b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x / b.x));
        result.y = ((short) (a.y / b.y));
        result.z = ((short) (a.z / b.z));
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
    public static android.renderscript.Short3 div(android.renderscript.Short3 a, short b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x / b));
        result.y = ((short) (a.y / b));
        result.z = ((short) (a.z / b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     */
    public void mod(android.renderscript.Short3 a) {
        this.x %= a.x;
        this.y %= a.y;
        this.z %= a.z;
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
    public static android.renderscript.Short3 mod(android.renderscript.Short3 a, android.renderscript.Short3 b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x % b.x));
        result.y = ((short) (a.y % b.y));
        result.z = ((short) (a.z % b.z));
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
    public static android.renderscript.Short3 mod(android.renderscript.Short3 a, short b) {
        android.renderscript.Short3 result = new android.renderscript.Short3();
        result.x = ((short) (a.x % b));
        result.y = ((short) (a.y % b));
        result.z = ((short) (a.z % b));
        return result;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public short length() {
        return 3;
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
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public short dotProduct(android.renderscript.Short3 a) {
        return ((short) (((x * a.x) + (y * a.y)) + (z * a.z)));
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
    public static short dotProduct(android.renderscript.Short3 a, android.renderscript.Short3 b) {
        return ((short) (((b.x * a.x) + (b.y * a.y)) + (b.z * a.z)));
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
    public void addMultiple(android.renderscript.Short3 a, short factor) {
        x += a.x * factor;
        y += a.y * factor;
        z += a.z * factor;
    }

    /**
     *
     *
     * @unknown set vector value by Short3
     * @param a
     * 		
     */
    public void set(android.renderscript.Short3 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
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
     */
    public void setValues(short a, short b, short c) {
        this.x = a;
        this.y = b;
        this.z = c;
    }

    /**
     *
     *
     * @unknown return the element sum of vector
     * @return 
     */
    public short elementSum() {
        return ((short) ((x + y) + z));
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
    }
}

