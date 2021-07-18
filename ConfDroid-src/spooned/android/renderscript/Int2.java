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
 * Provides two int fields packed.
 */
public class Int2 {
    public int x;

    public int y;

    public Int2() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Int2(int i) {
        this.x = this.y = i;
    }

    public Int2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     *
     * @unknown 
     */
    public Int2(android.renderscript.Int2 source) {
        this.x = source.x;
        this.y = source.y;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param a
     * 		
     */
    public void add(android.renderscript.Int2 a) {
        this.x += a.x;
        this.y += a.y;
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
    public static android.renderscript.Int2 add(android.renderscript.Int2 a, android.renderscript.Int2 b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
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
    public static android.renderscript.Int2 add(android.renderscript.Int2 a, int b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x + b;
        result.y = a.y + b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     */
    public void sub(android.renderscript.Int2 a) {
        this.x -= a.x;
        this.y -= a.y;
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
    public static android.renderscript.Int2 sub(android.renderscript.Int2 a, android.renderscript.Int2 b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
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
    public static android.renderscript.Int2 sub(android.renderscript.Int2 a, int b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x - b;
        result.y = a.y - b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     */
    public void mul(android.renderscript.Int2 a) {
        this.x *= a.x;
        this.y *= a.y;
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
    public static android.renderscript.Int2 mul(android.renderscript.Int2 a, android.renderscript.Int2 b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
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
    public static android.renderscript.Int2 mul(android.renderscript.Int2 a, int b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x * b;
        result.y = a.y * b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     */
    public void div(android.renderscript.Int2 a) {
        this.x /= a.x;
        this.y /= a.y;
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
    public static android.renderscript.Int2 div(android.renderscript.Int2 a, android.renderscript.Int2 b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
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
    public static android.renderscript.Int2 div(android.renderscript.Int2 a, int b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x / b;
        result.y = a.y / b;
        return result;
    }

    /**
     *
     *
     * @unknown Vector Modulo
     * @param a
     * 		
     */
    public void mod(android.renderscript.Int2 a) {
        this.x %= a.x;
        this.y %= a.y;
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
    public static android.renderscript.Int2 mod(android.renderscript.Int2 a, android.renderscript.Int2 b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
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
    public static android.renderscript.Int2 mod(android.renderscript.Int2 a, int b) {
        android.renderscript.Int2 result = new android.renderscript.Int2();
        result.x = a.x % b;
        result.y = a.y % b;
        return result;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public int length() {
        return 2;
    }

    /**
     *
     *
     * @unknown set vector negate
     */
    public void negate() {
        this.x = -x;
        this.y = -y;
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public int dotProduct(android.renderscript.Int2 a) {
        return ((int) ((x * a.x) + (y * a.y)));
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
    public static int dotProduct(android.renderscript.Int2 a, android.renderscript.Int2 b) {
        return ((int) ((b.x * a.x) + (b.y * a.y)));
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
    public void addMultiple(android.renderscript.Int2 a, int factor) {
        x += a.x * factor;
        y += a.y * factor;
    }

    /**
     *
     *
     * @unknown set vector value by Int2
     * @param a
     * 		
     */
    public void set(android.renderscript.Int2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    /**
     *
     *
     * @unknown set the vector field value by Int
     * @param a
     * 		
     * @param b
     * 		
     */
    public void setValues(int a, int b) {
        this.x = a;
        this.y = b;
    }

    /**
     *
     *
     * @unknown return the element sum of vector
     * @return 
     */
    public int elementSum() {
        return ((int) (x + y));
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
    }
}

