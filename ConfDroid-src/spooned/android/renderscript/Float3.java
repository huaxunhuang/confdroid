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
 * Provides three float fields packed.
 */
public class Float3 {
    public float x;

    public float y;

    public float z;

    public Float3() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Float3(android.renderscript.Float3 data) {
        this.x = data.x;
        this.y = data.y;
        this.z = data.z;
    }

    public Float3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
    public static android.renderscript.Float3 add(android.renderscript.Float3 a, android.renderscript.Float3 b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        res.z = a.z + b.z;
        return res;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(android.renderscript.Float3 value) {
        x += value.x;
        y += value.y;
        z += value.z;
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
    public static android.renderscript.Float3 add(android.renderscript.Float3 a, float b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x + b;
        res.y = a.y + b;
        res.z = a.z + b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(android.renderscript.Float3 value) {
        x -= value.x;
        y -= value.y;
        z -= value.z;
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
    public static android.renderscript.Float3 sub(android.renderscript.Float3 a, android.renderscript.Float3 b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        res.z = a.z - b.z;
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
    public static android.renderscript.Float3 sub(android.renderscript.Float3 a, float b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x - b;
        res.y = a.y - b;
        res.z = a.z - b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(android.renderscript.Float3 value) {
        x *= value.x;
        y *= value.y;
        z *= value.z;
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
    public static android.renderscript.Float3 mul(android.renderscript.Float3 a, android.renderscript.Float3 b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        res.z = a.z * b.z;
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
    public static android.renderscript.Float3 mul(android.renderscript.Float3 a, float b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x * b;
        res.y = a.y * b;
        res.z = a.z * b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(android.renderscript.Float3 value) {
        x /= value.x;
        y /= value.y;
        z /= value.z;
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
    public static android.renderscript.Float3 div(android.renderscript.Float3 a, android.renderscript.Float3 b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        res.z = a.z / b.z;
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
    public static android.renderscript.Float3 div(android.renderscript.Float3 a, float b) {
        android.renderscript.Float3 res = new android.renderscript.Float3();
        res.x = a.x / b;
        res.y = a.y / b;
        res.z = a.z / b;
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
    public java.lang.Float dotProduct(android.renderscript.Float3 a) {
        return new java.lang.Float(((x * a.x) + (y * a.y)) + (z * a.z));
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
    public static java.lang.Float dotProduct(android.renderscript.Float3 a, android.renderscript.Float3 b) {
        return new java.lang.Float(((b.x * a.x) + (b.y * a.y)) + (b.z * a.z));
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
    public void addMultiple(android.renderscript.Float3 a, float factor) {
        x += a.x * factor;
        y += a.y * factor;
        z += a.z * factor;
    }

    /**
     *
     *
     * @unknown set vector value by float3
     * @param a
     * 		
     */
    public void set(android.renderscript.Float3 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    /**
     *
     *
     * @unknown set vector negate
     */
    public void negate() {
        x = -x;
        y = -y;
        z = -z;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public int length() {
        return 3;
    }

    /**
     *
     *
     * @unknown return the element sum of vector
     * @return 
     */
    public java.lang.Float elementSum() {
        return new java.lang.Float((x + y) + z);
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
            case 2 :
                return z;
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
    public void addAt(int i, float value) {
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
     * @unknown set the vector field value
     * @param x
     * 		
     * @param y
     * 		
     * @param z
     * 		
     */
    public void setValues(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
        data[offset + 2] = z;
    }
}

