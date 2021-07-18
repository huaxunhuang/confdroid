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
 * Vector version of the basic double type.
 * Provides four double fields packed.
 */
public class Double4 {
    public double x;

    public double y;

    public double z;

    public double w;

    public Double4() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Double4(android.renderscript.Double4 data) {
        this.x = data.x;
        this.y = data.y;
        this.z = data.z;
        this.w = data.w;
    }

    public Double4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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
    public static android.renderscript.Double4 add(android.renderscript.Double4 a, android.renderscript.Double4 b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        res.z = a.z + b.z;
        res.w = a.w + b.w;
        return res;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(android.renderscript.Double4 value) {
        x += value.x;
        y += value.y;
        z += value.z;
        w += value.w;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(double value) {
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
    public static android.renderscript.Double4 add(android.renderscript.Double4 a, double b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x + b;
        res.y = a.y + b;
        res.z = a.z + b;
        res.w = a.w + b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(android.renderscript.Double4 value) {
        x -= value.x;
        y -= value.y;
        z -= value.z;
        w -= value.w;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(double value) {
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
    public static android.renderscript.Double4 sub(android.renderscript.Double4 a, double b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x - b;
        res.y = a.y - b;
        res.z = a.z - b;
        res.w = a.w - b;
        return res;
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
    public static android.renderscript.Double4 sub(android.renderscript.Double4 a, android.renderscript.Double4 b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        res.z = a.z - b.z;
        res.w = a.w - b.w;
        return res;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(android.renderscript.Double4 value) {
        x *= value.x;
        y *= value.y;
        z *= value.z;
        w *= value.w;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(double value) {
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
    public static android.renderscript.Double4 mul(android.renderscript.Double4 a, android.renderscript.Double4 b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        res.z = a.z * b.z;
        res.w = a.w * b.w;
        return res;
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
    public static android.renderscript.Double4 mul(android.renderscript.Double4 a, double b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x * b;
        res.y = a.y * b;
        res.z = a.z * b;
        res.w = a.w * b;
        return res;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(android.renderscript.Double4 value) {
        x /= value.x;
        y /= value.y;
        z /= value.z;
        w /= value.w;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(double value) {
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
    public static android.renderscript.Double4 div(android.renderscript.Double4 a, double b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x / b;
        res.y = a.y / b;
        res.z = a.z / b;
        res.w = a.w / b;
        return res;
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
    public static android.renderscript.Double4 div(android.renderscript.Double4 a, android.renderscript.Double4 b) {
        android.renderscript.Double4 res = new android.renderscript.Double4();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        res.z = a.z / b.z;
        res.w = a.w / b.w;
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
    public double dotProduct(android.renderscript.Double4 a) {
        return (((x * a.x) + (y * a.y)) + (z * a.z)) + (w * a.w);
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
    public static double dotProduct(android.renderscript.Double4 a, android.renderscript.Double4 b) {
        return (((b.x * a.x) + (b.y * a.y)) + (b.z * a.z)) + (b.w * a.w);
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
    public void addMultiple(android.renderscript.Double4 a, double factor) {
        x += a.x * factor;
        y += a.y * factor;
        z += a.z * factor;
        w += a.w * factor;
    }

    /**
     *
     *
     * @unknown Set vector value by double4
     * @param a
     * 		
     */
    public void set(android.renderscript.Double4 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.w = a.w;
    }

    /**
     *
     *
     * @unknown Set vector negate
     */
    public void negate() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
    }

    /**
     *
     *
     * @unknown Get vector length
     * @return 
     */
    public int length() {
        return 4;
    }

    /**
     *
     *
     * @unknown Return the element sum of vector
     * @return 
     */
    public double elementSum() {
        return ((x + y) + z) + w;
    }

    /**
     *
     *
     * @unknown Get the vector field value by index
     * @param i
     * 		
     * @return 
     */
    public double get(int i) {
        switch (i) {
            case 0 :
                return x;
            case 1 :
                return y;
            case 2 :
                return z;
            case 3 :
                return w;
            default :
                throw new java.lang.IndexOutOfBoundsException("Index: i");
        }
    }

    /**
     *
     *
     * @unknown Set the vector field value by index
     * @param i
     * 		
     * @param value
     * 		
     */
    public void setAt(int i, double value) {
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
     * @unknown Add the vector field value by index
     * @param i
     * 		
     * @param value
     * 		
     */
    public void addAt(int i, double value) {
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
     * @unknown Set the vector field value
     * @param x
     * 		
     * @param y
     * 		
     * @param z
     * 		
     * @param w
     * 		
     */
    public void setValues(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     *
     *
     * @unknown Copy the vector to double array
     * @param data
     * 		
     * @param offset
     * 		
     */
    public void copyTo(double[] data, int offset) {
        data[offset] = x;
        data[offset + 1] = y;
        data[offset + 2] = z;
        data[offset + 3] = w;
    }
}

