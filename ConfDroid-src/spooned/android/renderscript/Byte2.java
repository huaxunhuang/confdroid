/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * Class for exposing the native RenderScript byte2 type back to the Android system.
 */
public class Byte2 {
    public byte x;

    public byte y;

    public Byte2() {
    }

    public Byte2(byte initX, byte initY) {
        x = initX;
        y = initY;
    }

    /**
     *
     *
     * @unknown 
     */
    public Byte2(android.renderscript.Byte2 source) {
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
    public void add(android.renderscript.Byte2 a) {
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
    public static android.renderscript.Byte2 add(android.renderscript.Byte2 a, android.renderscript.Byte2 b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x + b.x));
        result.y = ((byte) (a.y + b.y));
        return result;
    }

    /**
     *
     *
     * @unknown Vector add
     * @param value
     * 		
     */
    public void add(byte value) {
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
    public static android.renderscript.Byte2 add(android.renderscript.Byte2 a, byte b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x + b));
        result.y = ((byte) (a.y + b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param a
     * 		
     */
    public void sub(android.renderscript.Byte2 a) {
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
    public static android.renderscript.Byte2 sub(android.renderscript.Byte2 a, android.renderscript.Byte2 b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x - b.x));
        result.y = ((byte) (a.y - b.y));
        return result;
    }

    /**
     *
     *
     * @unknown Vector subtraction
     * @param value
     * 		
     */
    public void sub(byte value) {
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
    public static android.renderscript.Byte2 sub(android.renderscript.Byte2 a, byte b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x - b));
        result.y = ((byte) (a.y - b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param a
     * 		
     */
    public void mul(android.renderscript.Byte2 a) {
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
    public static android.renderscript.Byte2 mul(android.renderscript.Byte2 a, android.renderscript.Byte2 b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x * b.x));
        result.y = ((byte) (a.y * b.y));
        return result;
    }

    /**
     *
     *
     * @unknown Vector multiplication
     * @param value
     * 		
     */
    public void mul(byte value) {
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
    public static android.renderscript.Byte2 mul(android.renderscript.Byte2 a, byte b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x * b));
        result.y = ((byte) (a.y * b));
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param a
     * 		
     */
    public void div(android.renderscript.Byte2 a) {
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
    public static android.renderscript.Byte2 div(android.renderscript.Byte2 a, android.renderscript.Byte2 b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x / b.x));
        result.y = ((byte) (a.y / b.y));
        return result;
    }

    /**
     *
     *
     * @unknown Vector division
     * @param value
     * 		
     */
    public void div(byte value) {
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
    public static android.renderscript.Byte2 div(android.renderscript.Byte2 a, byte b) {
        android.renderscript.Byte2 result = new android.renderscript.Byte2();
        result.x = ((byte) (a.x / b));
        result.y = ((byte) (a.y / b));
        return result;
    }

    /**
     *
     *
     * @unknown get vector length
     * @return 
     */
    public byte length() {
        return 2;
    }

    /**
     *
     *
     * @unknown set vector negate
     */
    public void negate() {
        this.x = ((byte) (-x));
        this.y = ((byte) (-y));
    }

    /**
     *
     *
     * @unknown Vector dot Product
     * @param a
     * 		
     * @return 
     */
    public byte dotProduct(android.renderscript.Byte2 a) {
        return ((byte) ((x * a.x) + (y * a.y)));
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
    public static byte dotProduct(android.renderscript.Byte2 a, android.renderscript.Byte2 b) {
        return ((byte) ((b.x * a.x) + (b.y * a.y)));
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
    public void addMultiple(android.renderscript.Byte2 a, byte factor) {
        x += a.x * factor;
        y += a.y * factor;
    }

    /**
     *
     *
     * @unknown set vector value by Byte2
     * @param a
     * 		
     */
    public void set(android.renderscript.Byte2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    /**
     *
     *
     * @unknown set the vector field value by Char
     * @param a
     * 		
     * @param b
     * 		
     */
    public void setValues(byte a, byte b) {
        this.x = a;
        this.y = b;
    }

    /**
     *
     *
     * @unknown return the element sum of vector
     * @return 
     */
    public byte elementSum() {
        return ((byte) (x + y));
    }

    /**
     *
     *
     * @unknown get the vector field value by index
     * @param i
     * 		
     * @return 
     */
    public byte get(int i) {
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
    public void setAt(int i, byte value) {
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
    public void addAt(int i, byte value) {
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
     * @unknown copy the vector to Char array
     * @param data
     * 		
     * @param offset
     * 		
     */
    public void copyTo(byte[] data, int offset) {
        data[offset] = x;
        data[offset + 1] = y;
    }
}

