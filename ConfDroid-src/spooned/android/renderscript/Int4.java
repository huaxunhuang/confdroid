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
 * Vector version of the basic int type.
 * Provides four int fields packed.
 */
public class Int4 {
    public int x;

    public int y;

    public int z;

    public int w;

    public Int4() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Int4(int i) {
        this.x = this.y = this.z = this.w = i;
    }

    public Int4(int x, int y, int z, int w) {
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
    public Int4(android.renderscript.Int4 source) {
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
    public void add(android.renderscript.Int4 a) {
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
    public static android.renderscript.Int4 add(android.renderscript.Int4 a, android.renderscript.Int4 b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        result.w = a.w + b.w;
        return result;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(int value) {
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
    public static android.renderscript.Int4 add(android.renderscript.Int4 a, int b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x + b;
        result.y = a.y + b;
        result.z = a.z + b;
        result.w = a.w + b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     */
    public void sub(android.renderscript.Int4 a) {
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
    public static android.renderscript.Int4 sub(android.renderscript.Int4 a, android.renderscript.Int4 b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        result.w = a.w - b.w;
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(int value) {
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
    public static android.renderscript.Int4 sub(android.renderscript.Int4 a, int b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x - b;
        result.y = a.y - b;
        result.z = a.z - b;
        result.w = a.w - b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     */
    public void mul(android.renderscript.Int4 a) {
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
    public static android.renderscript.Int4 mul(android.renderscript.Int4 a, android.renderscript.Int4 b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        result.z = a.z * b.z;
        result.w = a.w * b.w;
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(int value) {
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
    public static android.renderscript.Int4 mul(android.renderscript.Int4 a, int b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x * b;
        result.y = a.y * b;
        result.z = a.z * b;
        result.w = a.w * b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     */
    public void div(android.renderscript.Int4 a) {
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
    public static android.renderscript.Int4 div(android.renderscript.Int4 a, android.renderscript.Int4 b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        result.z = a.z / b.z;
        result.w = a.w / b.w;
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(int value) {
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
    public static android.renderscript.Int4 div(android.renderscript.Int4 a, int b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x / b;
        result.y = a.y / b;
        result.z = a.z / b;
        result.w = a.w / b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     */
    public void mod(android.renderscript.Int4 a) {
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
    public static android.renderscript.Int4 mod(android.renderscript.Int4 a, android.renderscript.Int4 b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        result.z = a.z % b.z;
        result.w = a.w % b.w;
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param value
     * 		
     */
    public void mod(int value) {
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
    public static android.renderscript.Int4 mod(android.renderscript.Int4 a, int b) {
        android.renderscript.Int4 result = new android.renderscript.Int4();
        result.x = a.x % b;
        result.y = a.y % b;
        result.z = a.z % b;
        result.w = a.w % b;
        return result;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public int length() {
        return 4;
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
        this.w = -w;
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public int dotProduct(android.renderscript.Int4 a) {
        return ((int) ((((x * a.x) + (y * a.y)) + (z * a.z)) + (w * a.w)));
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
    public static int dotProduct(android.renderscript.Int4 a, android.renderscript.Int4 b) {
        return ((int) ((((b.x * a.x) + (b.y * a.y)) + (b.z * a.z)) + (b.w * a.w)));
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
    public void addMultiple(android.renderscript.Int4 a, int factor) {
        x += a.x * factor;
        y += a.y * factor;
        z += a.z * factor;
        w += a.w * factor;
    }

    /**
     *
     *
     * @unknown set vector value by Int4
     * @param a
     * 		
     */
    public void set(android.renderscript.Int4 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.w = a.w;
    }

    /**
     *
     *
     * @unknown set the vector field value by Int
     * @param a
     * 		
     * @param b
     * 		
     * @param c
     * 		
     * @param d
     * 		
     */
    public void setValues(int a, int b, int c, int d) {
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
    public int elementSum() {
        return ((int) (((x + y) + z) + w));
    }

    /**
     *
     *
     * @unknown get the vector field value by index
     * @param i
     * 		
     * @return 
     */
    public int get(int i) {
        switch (i) {
            case 0 :
                return ((int) (x));
            case 1 :
                return ((int) (y));
            case 2 :
                return ((int) (z));
            case 3 :
                return ((int) (w));
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
    public void setAt(int i, int value) {
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
    public void addAt(int i, int value) {
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
     * @unknown copy the vector to int array
     * @param data
     * 		
     * @param offset
     * 		
     */
    public void copyTo(int[] data, int offset) {
        data[offset] = ((int) (x));
        data[offset + 1] = ((int) (y));
        data[offset + 2] = ((int) (z));
        data[offset + 3] = ((int) (w));
    }
}

