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
 * Vector version of the basic long type.
 * Provides three long fields packed.
 */
public class Long3 {
    public long x;

    public long y;

    public long z;

    public Long3() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Long3(long i) {
        this.x = this.y = this.z = i;
    }

    public Long3(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     *
     * @unknown 
     */
    public Long3(android.renderscript.Long3 source) {
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
    public void add(android.renderscript.Long3 a) {
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
    public static android.renderscript.Long3 add(android.renderscript.Long3 a, android.renderscript.Long3 b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        return result;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(long value) {
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
    public static android.renderscript.Long3 add(android.renderscript.Long3 a, long b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x + b;
        result.y = a.y + b;
        result.z = a.z + b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     */
    public void sub(android.renderscript.Long3 a) {
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
    public static android.renderscript.Long3 sub(android.renderscript.Long3 a, android.renderscript.Long3 b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(long value) {
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
    public static android.renderscript.Long3 sub(android.renderscript.Long3 a, long b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x - b;
        result.y = a.y - b;
        result.z = a.z - b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     */
    public void mul(android.renderscript.Long3 a) {
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
    public static android.renderscript.Long3 mul(android.renderscript.Long3 a, android.renderscript.Long3 b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        result.z = a.z * b.z;
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(long value) {
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
    public static android.renderscript.Long3 mul(android.renderscript.Long3 a, long b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x * b;
        result.y = a.y * b;
        result.z = a.z * b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     */
    public void div(android.renderscript.Long3 a) {
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
    public static android.renderscript.Long3 div(android.renderscript.Long3 a, android.renderscript.Long3 b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        result.z = a.z / b.z;
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(long value) {
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
    public static android.renderscript.Long3 div(android.renderscript.Long3 a, long b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x / b;
        result.y = a.y / b;
        result.z = a.z / b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     */
    public void mod(android.renderscript.Long3 a) {
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
    public static android.renderscript.Long3 mod(android.renderscript.Long3 a, android.renderscript.Long3 b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        result.z = a.z % b.z;
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param value
     * 		
     */
    public void mod(long value) {
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
    public static android.renderscript.Long3 mod(android.renderscript.Long3 a, long b) {
        android.renderscript.Long3 result = new android.renderscript.Long3();
        result.x = a.x % b;
        result.y = a.y % b;
        result.z = a.z % b;
        return result;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public long length() {
        return 3;
    }

    /**
     *
     *
     * @unknown set vector negate
     */
    public void negate() {
        this.x = -x;
        this.y = -y;
        this.z = -z;
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public long dotProduct(android.renderscript.Long3 a) {
        return ((long) (((x * a.x) + (y * a.y)) + (z * a.z)));
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
    public static long dotProduct(android.renderscript.Long3 a, android.renderscript.Long3 b) {
        return ((long) (((b.x * a.x) + (b.y * a.y)) + (b.z * a.z)));
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
    public void addMultiple(android.renderscript.Long3 a, long factor) {
        x += a.x * factor;
        y += a.y * factor;
        z += a.z * factor;
    }

    /**
     *
     *
     * @unknown set vector value by Long3
     * @param a
     * 		
     */
    public void set(android.renderscript.Long3 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    /**
     *
     *
     * @unknown set the vector field value by Long
     * @param a
     * 		
     * @param b
     * 		
     * @param c
     * 		
     */
    public void setValues(long a, long b, long c) {
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
    public long elementSum() {
        return ((long) ((x + y) + z));
    }

    /**
     *
     *
     * @unknown get the vector field value by index
     * @param i
     * 		
     * @return 
     */
    public long get(int i) {
        switch (i) {
            case 0 :
                return ((long) (x));
            case 1 :
                return ((long) (y));
            case 2 :
                return ((long) (z));
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
    public void setAt(int i, long value) {
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
    public void addAt(int i, long value) {
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
     * @unknown copy the vector to long array
     * @param data
     * 		
     * @param offset
     * 		
     */
    public void copyTo(long[] data, int offset) {
        data[offset] = ((long) (x));
        data[offset + 1] = ((long) (y));
        data[offset + 2] = ((long) (z));
    }
}

