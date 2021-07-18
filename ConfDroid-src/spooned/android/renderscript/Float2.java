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
 * Vector version of the basic float type.
 * Provides two float fields packed.
 */
public class Float2 {
    public float x;

    public float y;

    public Float2() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Float2(android.renderscript.Float2 data) {
        this.x = data.x;
        this.y = data.y;
    }

    public Float2(float x, float y) {
        this.x = x;
        this.y = y;
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
    public static android.renderscript.Float2 add(android.renderscript.Float2 a, android.renderscript.Float2 b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        return res;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(android.renderscript.Float2 value) {
        x += value.x;
        y += value.y;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(float value) {
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
    public static android.renderscript.Float2 add(android.renderscript.Float2 a, float b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x + b;
        res.y = a.y + b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(android.renderscript.Float2 value) {
        x -= value.x;
        y -= value.y;
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
    public static android.renderscript.Float2 sub(android.renderscript.Float2 a, android.renderscript.Float2 b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        return res;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(float value) {
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
    public static android.renderscript.Float2 sub(android.renderscript.Float2 a, float b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x - b;
        res.y = a.y - b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(android.renderscript.Float2 value) {
        x *= value.x;
        y *= value.y;
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
    public static android.renderscript.Float2 mul(android.renderscript.Float2 a, android.renderscript.Float2 b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        return res;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(float value) {
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
    public static android.renderscript.Float2 mul(android.renderscript.Float2 a, float b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x * b;
        res.y = a.y * b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(android.renderscript.Float2 value) {
        x /= value.x;
        y /= value.y;
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
    public static android.renderscript.Float2 div(android.renderscript.Float2 a, android.renderscript.Float2 b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        return res;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(float value) {
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
    public static android.renderscript.Float2 div(android.renderscript.Float2 a, float b) {
        android.renderscript.Float2 res = new android.renderscript.Float2();
        res.x = a.x / b;
        res.y = a.y / b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public float dotProduct(android.renderscript.Float2 a) {
        return (x * a.x) + (y * a.y);
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
    public static float dotProduct(android.renderscript.Float2 a, android.renderscript.Float2 b) {
        return (b.x * a.x) + (b.y * a.y);
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
    public void addMultiple(android.renderscript.Float2 a, float factor) {
        x += a.x * factor;
        y += a.y * factor;
    }

    /**
     *
     *
     * @unknown set vector value by float2
     * @param a
     * 		
     */
    public void set(android.renderscript.Float2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    /**
     *
     *
     * @unknown set vector negate
     */
    public void negate() {
        x = -x;
        y = -y;
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
     * @unknown return the element sum of vector
     * @return 
     */
    public float elementSum() {
        return x + y;
    }

    /**
     *
     *
     * @unknown get the vector field value by index
     * @param i
     * 		
     * @return 
     */
    public float get(int i) {
        switch (i) {
            case 0 :
                return x;
            case 1 :
                return y;
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
    public void setAt(int i, float value) {
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
    public void addAt(int i, float value) {
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
     * @unknown set the vector field value
     * @param x
     * 		
     * @param y
     * 		
     */
    public void setValues(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     *
     * @unknown copy the vector to float array
     * @param data
     * 		
     * @param offset
     * 		
     */
    public void copyTo(float[] data, int offset) {
        data[offset] = x;
        data[offset + 1] = y;
    }
}

