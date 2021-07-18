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
 * Provides two double fields packed.
 */
public class Double2 {
    public double x;

    public double y;

    public Double2() {
    }

    /**
     *
     *
     * @unknown 
     */
    public Double2(android.renderscript.Double2 data) {
        this.x = data.x;
        this.y = data.y;
    }

    public Double2(double x, double y) {
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
    public static android.renderscript.Double2 add(android.renderscript.Double2 a, android.renderscript.Double2 b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void add(android.renderscript.Double2 value) {
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
    public void add(double value) {
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
    public static android.renderscript.Double2 add(android.renderscript.Double2 a, double b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void sub(android.renderscript.Double2 value) {
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
    public static android.renderscript.Double2 sub(android.renderscript.Double2 a, android.renderscript.Double2 b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void sub(double value) {
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
    public static android.renderscript.Double2 sub(android.renderscript.Double2 a, double b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void mul(android.renderscript.Double2 value) {
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
    public static android.renderscript.Double2 mul(android.renderscript.Double2 a, android.renderscript.Double2 b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void mul(double value) {
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
    public static android.renderscript.Double2 mul(android.renderscript.Double2 a, double b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void div(android.renderscript.Double2 value) {
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
    public static android.renderscript.Double2 div(android.renderscript.Double2 a, android.renderscript.Double2 b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public void div(double value) {
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
    public static android.renderscript.Double2 div(android.renderscript.Double2 a, double b) {
        android.renderscript.Double2 res = new android.renderscript.Double2();
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
    public double dotProduct(android.renderscript.Double2 a) {
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
    public static java.lang.Double dotProduct(android.renderscript.Double2 a, android.renderscript.Double2 b) {
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
    public void addMultiple(android.renderscript.Double2 a, double factor) {
        x += a.x * factor;
        y += a.y * factor;
    }

    /**
     *
     *
     * @unknown Set vector value by double2
     * @param a
     * 		
     */
    public void set(android.renderscript.Double2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    /**
     *
     *
     * @unknown Set vector negate
     */
    public void negate() {
        x = -x;
        y = -y;
    }

    /**
     *
     *
     * @unknown Get vector length
     * @return 
     */
    public int length() {
        return 2;
    }

    /**
     *
     *
     * @unknown Return the element sum of vector
     * @return 
     */
    public double elementSum() {
        return x + y;
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
     */
    public void setValues(double x, double y) {
        this.x = x;
        this.y = y;
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
    }
}

