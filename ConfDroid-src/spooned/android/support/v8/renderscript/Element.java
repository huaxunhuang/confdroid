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
package android.support.v8.renderscript;


/**
 * <p>An Element represents one item within an {@link android.support.v8.renderscript.Allocation}.  An Element is roughly
 * equivalent to a C type in a RenderScript kernel. Elements may be basic or
 * complex. Some basic elements are</p> <ul> <li>A single float value
 * (equivalent to a float in a kernel)</li> <li>A four-element float vector
 * (equivalent to a float4 in a kernel)</li> <li>An unsigned 32-bit integer
 * (equivalent to an unsigned int in a kernel)</li> <li>A single signed 8-bit
 * integer (equivalent to a char in a kernel)</li> </ul> <p>A complex element is
 * roughly equivalent to a C struct and contains a number of basic or complex
 * Elements. From Java code, a complex element contains a list of sub-elements
 * and names that represents a particular data structure. Structs used in RS
 * scripts are available to Java code by using the
 * {@code ScriptField_structname} class that is reflected from a particular
 * script.</p>
 *
 * <p>Basic Elements are comprised of a {@link android.support.v8.renderscript.Element.DataType} and a {@link android.support.v8.renderscript.Element.DataKind}. The DataType encodes C
 * type information of an Element, while the DataKind encodes how that Element
 * should be interpreted by a {@link android.support.v8.renderscript.Sampler}.
 * Note that {@link android.support.v8.renderscript.Allocation} objects with
 * DataKind {@link android.support.v8.renderscript.Element.DataKind#USER} cannot
 * be used as input for a {@link android.support.v8.renderscript.Sampler}. In
 * general, {@link android.support.v8.renderscript.Allocation} objects that are
 * intended for use with a {@link android.support.v8.renderscript.Sampler}
 * should use bitmap-derived Elements such as
 * {@link android.support.v8.renderscript.Element#RGBA_8888} or {@link android.support.v8.renderscript#Element.A_8}.</p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating an application that uses RenderScript,
 * read the
 * <a href="{@docRoot }guide/topics/renderscript/index.html">RenderScript</a>
 * developer guide.</p>
 * </div>
 */
public class Element extends android.support.v8.renderscript.BaseObj {
    int mSize;

    android.support.v8.renderscript.Element[] mElements;

    java.lang.String[] mElementNames;

    int[] mArraySizes;

    int[] mOffsetInBytes;

    int[] mVisibleElementMap;

    android.support.v8.renderscript.Element.DataType mType;

    android.support.v8.renderscript.Element.DataKind mKind;

    boolean mNormalized;

    int mVectorSize;

    private void updateVisibleSubElements() {
        if (mElements == null) {
            return;
        }
        int noPaddingFieldCount = 0;
        int fieldCount = mElementNames.length;
        // Find out how many elements are not padding
        for (int ct = 0; ct < fieldCount; ct++) {
            if (mElementNames[ct].charAt(0) != '#') {
                noPaddingFieldCount++;
            }
        }
        mVisibleElementMap = new int[noPaddingFieldCount];
        // Make a map that points us at non-padding elements
        for (int ct = 0, ctNoPadding = 0; ct < fieldCount; ct++) {
            if (mElementNames[ct].charAt(0) != '#') {
                mVisibleElementMap[ctNoPadding++] = ct;
            }
        }
    }

    /**
     *
     *
     * @return element size in bytes
     */
    public int getBytesSize() {
        return mSize;
    }

    /**
     * Returns the number of vector components. 2 for float2, 4 for
     * float4, etc.
     *
     * @return element vector size
     */
    public int getVectorSize() {
        return mVectorSize;
    }

    /**
     * DataType represents the basic type information for a basic element.  The
     * naming convention follows.  For numeric types it is FLOAT,
     * SIGNED, or UNSIGNED followed by the _BITS where BITS is the
     * size of the data.  BOOLEAN is a true / false (1,0)
     * represented in an 8 bit container.  The UNSIGNED variants
     * with multiple bit definitions are for packed graphical data
     * formats and represent vectors with per vector member sizes
     * which are treated as a single unit for packing and alignment
     * purposes.
     *
     * MATRIX the three matrix types contain FLOAT_32 elements and are treated
     * as 32 bits for alignment purposes.
     *
     * RS_* objects.  32 bit opaque handles.
     */
    public enum DataType {

        NONE(0, 0),
        // FLOAT_16 (1, 2),
        FLOAT_32(2, 4),
        FLOAT_64(3, 8),
        SIGNED_8(4, 1),
        SIGNED_16(5, 2),
        SIGNED_32(6, 4),
        SIGNED_64(7, 8),
        UNSIGNED_8(8, 1),
        UNSIGNED_16(9, 2),
        UNSIGNED_32(10, 4),
        UNSIGNED_64(11, 8),
        BOOLEAN(12, 1),
        UNSIGNED_5_6_5(13, 2),
        UNSIGNED_5_5_5_1(14, 2),
        UNSIGNED_4_4_4_4(15, 2),
        MATRIX_4X4(16, 64),
        MATRIX_3X3(17, 36),
        MATRIX_2X2(18, 16),
        RS_ELEMENT(1000),
        RS_TYPE(1001),
        RS_ALLOCATION(1002),
        RS_SAMPLER(1003),
        RS_SCRIPT(1004);
        int mID;

        int mSize;

        DataType(int id, int size) {
            mID = id;
            mSize = size;
        }

        DataType(int id) {
            mID = id;
            mSize = 4;
            if (android.support.v8.renderscript.RenderScript.sPointerSize == 8) {
                mSize = 32;
            }
        }
    }

    /**
     * The special interpretation of the data if required.  This is primarly
     * useful for graphical data.  USER indicates no special interpretation is
     * expected.  PIXEL is used in conjunction with the standard data types for
     * representing texture formats.
     */
    public enum DataKind {

        USER(0),
        PIXEL_L(7),
        PIXEL_A(8),
        PIXEL_LA(9),
        PIXEL_RGB(10),
        PIXEL_RGBA(11),
        PIXEL_DEPTH(12),
        PIXEL_YUV(13);
        int mID;

        DataKind(int id) {
            mID = id;
        }
    }

    /**
     * Return if a element is too complex for use as a data source for a Mesh or
     * a Program.
     *
     * @return boolean
     */
    public boolean isComplex() {
        if (mElements == null) {
            return false;
        }
        for (int ct = 0; ct < mElements.length; ct++) {
            if (mElements[ct].mElements != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elements could be simple, such as an int or a float, or a
     * structure with multiple sub elements, such as a collection of
     * floats, float2, float4. This function returns zero for simple
     * elements or the number of sub-elements otherwise.
     *
     * @return number of sub-elements in this element
     */
    public int getSubElementCount() {
        if (mVisibleElementMap == null) {
            return 0;
        }
        return mVisibleElementMap.length;
    }

    /**
     * For complex elements, this function will return the
     * sub-element at index
     *
     * @param index
     * 		index of the sub-element to return
     * @return sub-element in this element at given index
     */
    public android.support.v8.renderscript.Element getSubElement(int index) {
        if (mVisibleElementMap == null) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Element contains no sub-elements");
        }
        if ((index < 0) || (index >= mVisibleElementMap.length)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Illegal sub-element index");
        }
        return mElements[mVisibleElementMap[index]];
    }

    /**
     * For complex elements, this function will return the
     * sub-element name at index
     *
     * @param index
     * 		index of the sub-element
     * @return sub-element in this element at given index
     */
    public java.lang.String getSubElementName(int index) {
        if (mVisibleElementMap == null) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Element contains no sub-elements");
        }
        if ((index < 0) || (index >= mVisibleElementMap.length)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Illegal sub-element index");
        }
        return mElementNames[mVisibleElementMap[index]];
    }

    /**
     * For complex elements, some sub-elements could be statically
     * sized arrays. This function will return the array size for
     * sub-element at index
     *
     * @param index
     * 		index of the sub-element
     * @return array size of sub-element in this element at given index
     */
    public int getSubElementArraySize(int index) {
        if (mVisibleElementMap == null) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Element contains no sub-elements");
        }
        if ((index < 0) || (index >= mVisibleElementMap.length)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Illegal sub-element index");
        }
        return mArraySizes[mVisibleElementMap[index]];
    }

    /**
     * This function specifies the location of a sub-element within
     * the element
     *
     * @param index
     * 		index of the sub-element
     * @return offset in bytes of sub-element in this element at given index
     */
    public int getSubElementOffsetBytes(int index) {
        if (mVisibleElementMap == null) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Element contains no sub-elements");
        }
        if ((index < 0) || (index >= mVisibleElementMap.length)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Illegal sub-element index");
        }
        return mOffsetInBytes[mVisibleElementMap[index]];
    }

    /**
     *
     *
     * @return element data type
     */
    public android.support.v8.renderscript.Element.DataType getDataType() {
        return mType;
    }

    /**
     *
     *
     * @return element data kind
     */
    public android.support.v8.renderscript.Element.DataKind getDataKind() {
        return mKind;
    }

    /**
     * Utility function for returning an Element containing a single Boolean.
     *
     * @param rs
     * 		Context to which the element will belong.
     * @return Element
     */
    public static android.support.v8.renderscript.Element BOOLEAN(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_BOOLEAN == null) {
            rs.mElement_BOOLEAN = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.BOOLEAN);
        }
        return rs.mElement_BOOLEAN;
    }

    /**
     * Utility function for returning an Element containing a single UNSIGNED_8.
     *
     * @param rs
     * 		Context to which the element will belong.
     * @return Element
     */
    public static android.support.v8.renderscript.Element U8(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_U8 == null) {
            rs.mElement_U8 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8);
        }
        return rs.mElement_U8;
    }

    /**
     * Utility function for returning an Element containing a single SIGNED_8.
     *
     * @param rs
     * 		Context to which the element will belong.
     * @return Element
     */
    public static android.support.v8.renderscript.Element I8(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_I8 == null) {
            rs.mElement_I8 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.SIGNED_8);
        }
        return rs.mElement_I8;
    }

    public static android.support.v8.renderscript.Element U16(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_U16 == null) {
            rs.mElement_U16 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_16);
        }
        return rs.mElement_U16;
    }

    public static android.support.v8.renderscript.Element I16(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_I16 == null) {
            rs.mElement_I16 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.SIGNED_16);
        }
        return rs.mElement_I16;
    }

    public static android.support.v8.renderscript.Element U32(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_U32 == null) {
            rs.mElement_U32 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_32);
        }
        return rs.mElement_U32;
    }

    public static android.support.v8.renderscript.Element I32(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_I32 == null) {
            rs.mElement_I32 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.SIGNED_32);
        }
        return rs.mElement_I32;
    }

    public static android.support.v8.renderscript.Element U64(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_U64 == null) {
            rs.mElement_U64 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_64);
        }
        return rs.mElement_U64;
    }

    public static android.support.v8.renderscript.Element I64(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_I64 == null) {
            rs.mElement_I64 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.SIGNED_64);
        }
        return rs.mElement_I64;
    }

    public static android.support.v8.renderscript.Element F32(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_F32 == null) {
            rs.mElement_F32 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.FLOAT_32);
        }
        return rs.mElement_F32;
    }

    public static android.support.v8.renderscript.Element F64(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_F64 == null) {
            rs.mElement_F64 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.FLOAT_64);
        }
        return rs.mElement_F64;
    }

    public static android.support.v8.renderscript.Element ELEMENT(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_ELEMENT == null) {
            rs.mElement_ELEMENT = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.RS_ELEMENT);
        }
        return rs.mElement_ELEMENT;
    }

    public static android.support.v8.renderscript.Element TYPE(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_TYPE == null) {
            rs.mElement_TYPE = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.RS_TYPE);
        }
        return rs.mElement_TYPE;
    }

    public static android.support.v8.renderscript.Element ALLOCATION(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_ALLOCATION == null) {
            rs.mElement_ALLOCATION = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.RS_ALLOCATION);
        }
        return rs.mElement_ALLOCATION;
    }

    public static android.support.v8.renderscript.Element SAMPLER(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_SAMPLER == null) {
            rs.mElement_SAMPLER = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.RS_SAMPLER);
        }
        return rs.mElement_SAMPLER;
    }

    public static android.support.v8.renderscript.Element SCRIPT(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_SCRIPT == null) {
            rs.mElement_SCRIPT = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.RS_SCRIPT);
        }
        return rs.mElement_SCRIPT;
    }

    public static android.support.v8.renderscript.Element A_8(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_A_8 == null) {
            rs.mElement_A_8 = android.support.v8.renderscript.Element.createPixel(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8, android.support.v8.renderscript.Element.DataKind.PIXEL_A);
        }
        return rs.mElement_A_8;
    }

    public static android.support.v8.renderscript.Element RGB_565(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_RGB_565 == null) {
            rs.mElement_RGB_565 = android.support.v8.renderscript.Element.createPixel(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_5_6_5, android.support.v8.renderscript.Element.DataKind.PIXEL_RGB);
        }
        return rs.mElement_RGB_565;
    }

    public static android.support.v8.renderscript.Element RGB_888(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_RGB_888 == null) {
            rs.mElement_RGB_888 = android.support.v8.renderscript.Element.createPixel(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8, android.support.v8.renderscript.Element.DataKind.PIXEL_RGB);
        }
        return rs.mElement_RGB_888;
    }

    public static android.support.v8.renderscript.Element RGBA_5551(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_RGBA_5551 == null) {
            rs.mElement_RGBA_5551 = android.support.v8.renderscript.Element.createPixel(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_5_5_5_1, android.support.v8.renderscript.Element.DataKind.PIXEL_RGBA);
        }
        return rs.mElement_RGBA_5551;
    }

    public static android.support.v8.renderscript.Element RGBA_4444(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_RGBA_4444 == null) {
            rs.mElement_RGBA_4444 = android.support.v8.renderscript.Element.createPixel(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_4_4_4_4, android.support.v8.renderscript.Element.DataKind.PIXEL_RGBA);
        }
        return rs.mElement_RGBA_4444;
    }

    public static android.support.v8.renderscript.Element RGBA_8888(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_RGBA_8888 == null) {
            rs.mElement_RGBA_8888 = android.support.v8.renderscript.Element.createPixel(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8, android.support.v8.renderscript.Element.DataKind.PIXEL_RGBA);
        }
        return rs.mElement_RGBA_8888;
    }

    public static android.support.v8.renderscript.Element F32_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_FLOAT_2 == null) {
            rs.mElement_FLOAT_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.FLOAT_32, 2);
        }
        return rs.mElement_FLOAT_2;
    }

    public static android.support.v8.renderscript.Element F32_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_FLOAT_3 == null) {
            rs.mElement_FLOAT_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.FLOAT_32, 3);
        }
        return rs.mElement_FLOAT_3;
    }

    public static android.support.v8.renderscript.Element F32_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_FLOAT_4 == null) {
            rs.mElement_FLOAT_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.FLOAT_32, 4);
        }
        return rs.mElement_FLOAT_4;
    }

    public static android.support.v8.renderscript.Element F64_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_DOUBLE_2 == null) {
            rs.mElement_DOUBLE_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.FLOAT_64, 2);
        }
        return rs.mElement_DOUBLE_2;
    }

    public static android.support.v8.renderscript.Element F64_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_DOUBLE_3 == null) {
            rs.mElement_DOUBLE_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.FLOAT_64, 3);
        }
        return rs.mElement_DOUBLE_3;
    }

    public static android.support.v8.renderscript.Element F64_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_DOUBLE_4 == null) {
            rs.mElement_DOUBLE_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.FLOAT_64, 4);
        }
        return rs.mElement_DOUBLE_4;
    }

    public static android.support.v8.renderscript.Element U8_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_UCHAR_2 == null) {
            rs.mElement_UCHAR_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8, 2);
        }
        return rs.mElement_UCHAR_2;
    }

    public static android.support.v8.renderscript.Element U8_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_UCHAR_3 == null) {
            rs.mElement_UCHAR_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8, 3);
        }
        return rs.mElement_UCHAR_3;
    }

    public static android.support.v8.renderscript.Element U8_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_UCHAR_4 == null) {
            rs.mElement_UCHAR_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_8, 4);
        }
        return rs.mElement_UCHAR_4;
    }

    public static android.support.v8.renderscript.Element I8_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_CHAR_2 == null) {
            rs.mElement_CHAR_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_8, 2);
        }
        return rs.mElement_CHAR_2;
    }

    public static android.support.v8.renderscript.Element I8_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_CHAR_3 == null) {
            rs.mElement_CHAR_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_8, 3);
        }
        return rs.mElement_CHAR_3;
    }

    public static android.support.v8.renderscript.Element I8_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_CHAR_4 == null) {
            rs.mElement_CHAR_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_8, 4);
        }
        return rs.mElement_CHAR_4;
    }

    public static android.support.v8.renderscript.Element U16_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_USHORT_2 == null) {
            rs.mElement_USHORT_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_16, 2);
        }
        return rs.mElement_USHORT_2;
    }

    public static android.support.v8.renderscript.Element U16_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_USHORT_3 == null) {
            rs.mElement_USHORT_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_16, 3);
        }
        return rs.mElement_USHORT_3;
    }

    public static android.support.v8.renderscript.Element U16_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_USHORT_4 == null) {
            rs.mElement_USHORT_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_16, 4);
        }
        return rs.mElement_USHORT_4;
    }

    public static android.support.v8.renderscript.Element I16_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_SHORT_2 == null) {
            rs.mElement_SHORT_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_16, 2);
        }
        return rs.mElement_SHORT_2;
    }

    public static android.support.v8.renderscript.Element I16_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_SHORT_3 == null) {
            rs.mElement_SHORT_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_16, 3);
        }
        return rs.mElement_SHORT_3;
    }

    public static android.support.v8.renderscript.Element I16_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_SHORT_4 == null) {
            rs.mElement_SHORT_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_16, 4);
        }
        return rs.mElement_SHORT_4;
    }

    public static android.support.v8.renderscript.Element U32_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_UINT_2 == null) {
            rs.mElement_UINT_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_32, 2);
        }
        return rs.mElement_UINT_2;
    }

    public static android.support.v8.renderscript.Element U32_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_UINT_3 == null) {
            rs.mElement_UINT_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_32, 3);
        }
        return rs.mElement_UINT_3;
    }

    public static android.support.v8.renderscript.Element U32_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_UINT_4 == null) {
            rs.mElement_UINT_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_32, 4);
        }
        return rs.mElement_UINT_4;
    }

    public static android.support.v8.renderscript.Element I32_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_INT_2 == null) {
            rs.mElement_INT_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_32, 2);
        }
        return rs.mElement_INT_2;
    }

    public static android.support.v8.renderscript.Element I32_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_INT_3 == null) {
            rs.mElement_INT_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_32, 3);
        }
        return rs.mElement_INT_3;
    }

    public static android.support.v8.renderscript.Element I32_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_INT_4 == null) {
            rs.mElement_INT_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_32, 4);
        }
        return rs.mElement_INT_4;
    }

    public static android.support.v8.renderscript.Element U64_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_ULONG_2 == null) {
            rs.mElement_ULONG_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_64, 2);
        }
        return rs.mElement_ULONG_2;
    }

    public static android.support.v8.renderscript.Element U64_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_ULONG_3 == null) {
            rs.mElement_ULONG_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_64, 3);
        }
        return rs.mElement_ULONG_3;
    }

    public static android.support.v8.renderscript.Element U64_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_ULONG_4 == null) {
            rs.mElement_ULONG_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.UNSIGNED_64, 4);
        }
        return rs.mElement_ULONG_4;
    }

    public static android.support.v8.renderscript.Element I64_2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_LONG_2 == null) {
            rs.mElement_LONG_2 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_64, 2);
        }
        return rs.mElement_LONG_2;
    }

    public static android.support.v8.renderscript.Element I64_3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_LONG_3 == null) {
            rs.mElement_LONG_3 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_64, 3);
        }
        return rs.mElement_LONG_3;
    }

    public static android.support.v8.renderscript.Element I64_4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_LONG_4 == null) {
            rs.mElement_LONG_4 = android.support.v8.renderscript.Element.createVector(rs, android.support.v8.renderscript.Element.DataType.SIGNED_64, 4);
        }
        return rs.mElement_LONG_4;
    }

    public static android.support.v8.renderscript.Element MATRIX_4X4(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_MATRIX_4X4 == null) {
            rs.mElement_MATRIX_4X4 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.MATRIX_4X4);
        }
        return rs.mElement_MATRIX_4X4;
    }

    public static android.support.v8.renderscript.Element MATRIX_3X3(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_MATRIX_3X3 == null) {
            rs.mElement_MATRIX_3X3 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.MATRIX_3X3);
        }
        return rs.mElement_MATRIX_3X3;
    }

    public static android.support.v8.renderscript.Element MATRIX_2X2(android.support.v8.renderscript.RenderScript rs) {
        if (rs.mElement_MATRIX_2X2 == null) {
            rs.mElement_MATRIX_2X2 = android.support.v8.renderscript.Element.createUser(rs, android.support.v8.renderscript.Element.DataType.MATRIX_2X2);
        }
        return rs.mElement_MATRIX_2X2;
    }

    Element(long id, android.support.v8.renderscript.RenderScript rs, android.support.v8.renderscript.Element[] e, java.lang.String[] n, int[] as) {
        super(id, rs);
        mSize = 0;
        mVectorSize = 1;
        mElements = e;
        mElementNames = n;
        mArraySizes = as;
        mType = android.support.v8.renderscript.Element.DataType.NONE;
        mKind = android.support.v8.renderscript.Element.DataKind.USER;
        mOffsetInBytes = new int[mElements.length];
        for (int ct = 0; ct < mElements.length; ct++) {
            mOffsetInBytes[ct] = mSize;
            mSize += mElements[ct].mSize * mArraySizes[ct];
        }
        updateVisibleSubElements();
    }

    Element(long id, android.support.v8.renderscript.RenderScript rs, android.support.v8.renderscript.Element.DataType dt, android.support.v8.renderscript.Element.DataKind dk, boolean norm, int size) {
        super(id, rs);
        if (((dt != android.support.v8.renderscript.Element.DataType.UNSIGNED_5_6_5) && (dt != android.support.v8.renderscript.Element.DataType.UNSIGNED_4_4_4_4)) && (dt != android.support.v8.renderscript.Element.DataType.UNSIGNED_5_5_5_1)) {
            if (size == 3) {
                mSize = dt.mSize * 4;
            } else {
                mSize = dt.mSize * size;
            }
        } else {
            mSize = dt.mSize;
        }
        mType = dt;
        mKind = dk;
        mNormalized = norm;
        mVectorSize = size;
    }

    Element(long id, android.support.v8.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /* Get an identical dummy Element for Compat Context */
    public long getDummyElement(android.support.v8.renderscript.RenderScript mRS) {
        return mRS.nIncElementCreate(mType.mID, mKind.mID, mNormalized, mVectorSize);
    }

    /**
     * Create a custom Element of the specified DataType.  The DataKind will be
     * set to USER and the vector size to 1 indicating non-vector.
     *
     * @param rs
     * 		The context associated with the new Element.
     * @param dt
     * 		The DataType for the new element.
     * @return Element
     */
    static android.support.v8.renderscript.Element createUser(android.support.v8.renderscript.RenderScript rs, android.support.v8.renderscript.Element.DataType dt) {
        android.support.v8.renderscript.Element.DataKind dk = android.support.v8.renderscript.Element.DataKind.USER;
        boolean norm = false;
        int vecSize = 1;
        long id = rs.nElementCreate(dt.mID, dk.mID, norm, vecSize);
        return new android.support.v8.renderscript.Element(id, rs, dt, dk, norm, vecSize);
    }

    /**
     * Create a custom vector element of the specified DataType and vector size.
     * DataKind will be set to USER. Only primitive types (FLOAT_32, FLOAT_64,
     * SIGNED_8, SIGNED_16, SIGNED_32, SIGNED_64, UNSIGNED_8, UNSIGNED_16,
     * UNSIGNED_32, UNSIGNED_64, BOOLEAN) are supported.
     *
     * @param rs
     * 		The context associated with the new Element.
     * @param dt
     * 		The DataType for the new Element.
     * @param size
     * 		Vector size for the new Element.  Range 2-4 inclusive
     * 		supported.
     * @return Element
     */
    public static android.support.v8.renderscript.Element createVector(android.support.v8.renderscript.RenderScript rs, android.support.v8.renderscript.Element.DataType dt, int size) {
        if ((size < 2) || (size > 4)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Vector size out of range 2-4.");
        }
        switch (dt) {
            // Support only primitive integer/float/boolean types as vectors.
            case FLOAT_32 :
            case FLOAT_64 :
            case SIGNED_8 :
            case SIGNED_16 :
            case SIGNED_32 :
            case SIGNED_64 :
            case UNSIGNED_8 :
            case UNSIGNED_16 :
            case UNSIGNED_32 :
            case UNSIGNED_64 :
            case BOOLEAN :
                {
                    android.support.v8.renderscript.Element.DataKind dk = android.support.v8.renderscript.Element.DataKind.USER;
                    boolean norm = false;
                    long id = rs.nElementCreate(dt.mID, dk.mID, norm, size);
                    return new android.support.v8.renderscript.Element(id, rs, dt, dk, norm, size);
                }
            default :
                {
                    throw new android.support.v8.renderscript.RSIllegalArgumentException("Cannot create vector of " + "non-primitive type.");
                }
        }
    }

    /**
     * Create a new pixel Element type.  A matching DataType and DataKind must
     * be provided.  The DataType and DataKind must contain the same number of
     * components.  Vector size will be set to 1.
     *
     * @param rs
     * 		The context associated with the new Element.
     * @param dt
     * 		The DataType for the new element.
     * @param dk
     * 		The DataKind to specify the mapping of each component in the
     * 		DataType.
     * @return Element
     */
    public static android.support.v8.renderscript.Element createPixel(android.support.v8.renderscript.RenderScript rs, android.support.v8.renderscript.Element.DataType dt, android.support.v8.renderscript.Element.DataKind dk) {
        if (!(((((((dk == android.support.v8.renderscript.Element.DataKind.PIXEL_L) || (dk == android.support.v8.renderscript.Element.DataKind.PIXEL_A)) || (dk == android.support.v8.renderscript.Element.DataKind.PIXEL_LA)) || (dk == android.support.v8.renderscript.Element.DataKind.PIXEL_RGB)) || (dk == android.support.v8.renderscript.Element.DataKind.PIXEL_RGBA)) || (dk == android.support.v8.renderscript.Element.DataKind.PIXEL_DEPTH)) || (dk == android.support.v8.renderscript.Element.DataKind.PIXEL_YUV))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Unsupported DataKind");
        }
        if (!(((((dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_8) || (dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_16)) || (dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_5_6_5)) || (dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_4_4_4_4)) || (dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_5_5_5_1))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Unsupported DataType");
        }
        if ((dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_5_6_5) && (dk != android.support.v8.renderscript.Element.DataKind.PIXEL_RGB)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Bad kind and type combo");
        }
        if ((dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_5_5_5_1) && (dk != android.support.v8.renderscript.Element.DataKind.PIXEL_RGBA)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Bad kind and type combo");
        }
        if ((dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_4_4_4_4) && (dk != android.support.v8.renderscript.Element.DataKind.PIXEL_RGBA)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Bad kind and type combo");
        }
        if ((dt == android.support.v8.renderscript.Element.DataType.UNSIGNED_16) && (dk != android.support.v8.renderscript.Element.DataKind.PIXEL_DEPTH)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Bad kind and type combo");
        }
        int size = 1;
        switch (dk) {
            case PIXEL_LA :
                size = 2;
                break;
            case PIXEL_RGB :
                size = 3;
                break;
            case PIXEL_RGBA :
                size = 4;
                break;
        }
        boolean norm = true;
        long id = rs.nElementCreate(dt.mID, dk.mID, norm, size);
        return new android.support.v8.renderscript.Element(id, rs, dt, dk, norm, size);
    }

    /**
     * Check if the current Element is compatible with another Element.
     * Primitive Elements are compatible if they share the same underlying
     * size and type (i.e. U8 is compatible with A_8). User-defined Elements
     * must be equal in order to be compatible. This requires strict name
     * equivalence for all sub-Elements (in addition to structural equivalence).
     *
     * @param e
     * 		The Element to check compatibility with.
     * @return boolean true if the Elements are compatible, otherwise false.
     */
    public boolean isCompatible(android.support.v8.renderscript.Element e) {
        // Try strict BaseObj equality to start with.
        if (this.equals(e)) {
            return true;
        }
        // Ignore mKind because it is allowed to be different (user vs. pixel).
        // We also ignore mNormalized because it can be different. The mType
        // field must not be NONE since we require name equivalence for
        // all user-created Elements.
        return (((mSize == e.mSize) && (mType != android.support.v8.renderscript.Element.DataType.NONE)) && (mType == e.mType)) && (mVectorSize == e.mVectorSize);
    }

    /**
     * Builder class for producing complex elements with matching field and name
     * pairs.  The builder starts empty.  The order in which elements are added
     * is retained for the layout in memory.
     */
    public static class Builder {
        android.support.v8.renderscript.RenderScript mRS;

        android.support.v8.renderscript.Element[] mElements;

        java.lang.String[] mElementNames;

        int[] mArraySizes;

        int mCount;

        int mSkipPadding;

        /**
         * Create a builder object.
         *
         * @param rs
         * 		
         */
        public Builder(android.support.v8.renderscript.RenderScript rs) {
            mRS = rs;
            mCount = 0;
            mElements = new android.support.v8.renderscript.Element[8];
            mElementNames = new java.lang.String[8];
            mArraySizes = new int[8];
        }

        /**
         * Add an array of elements to this element.
         *
         * @param element
         * 		
         * @param name
         * 		
         * @param arraySize
         * 		
         */
        public android.support.v8.renderscript.Element.Builder add(android.support.v8.renderscript.Element element, java.lang.String name, int arraySize) {
            if (arraySize < 1) {
                throw new android.support.v8.renderscript.RSIllegalArgumentException("Array size cannot be less than 1.");
            }
            // Skip padding fields after a vector 3 type.
            if (mSkipPadding != 0) {
                if (name.startsWith("#padding_")) {
                    mSkipPadding = 0;
                    return this;
                }
            }
            if (element.mVectorSize == 3) {
                mSkipPadding = 1;
            } else {
                mSkipPadding = 0;
            }
            if (mCount == mElements.length) {
                android.support.v8.renderscript.Element[] e = new android.support.v8.renderscript.Element[mCount + 8];
                java.lang.String[] s = new java.lang.String[mCount + 8];
                int[] as = new int[mCount + 8];
                java.lang.System.arraycopy(mElements, 0, e, 0, mCount);
                java.lang.System.arraycopy(mElementNames, 0, s, 0, mCount);
                java.lang.System.arraycopy(mArraySizes, 0, as, 0, mCount);
                mElements = e;
                mElementNames = s;
                mArraySizes = as;
            }
            mElements[mCount] = element;
            mElementNames[mCount] = name;
            mArraySizes[mCount] = arraySize;
            mCount++;
            return this;
        }

        /**
         * Add a single element to this Element.
         *
         * @param element
         * 		
         * @param name
         * 		
         */
        public android.support.v8.renderscript.Element.Builder add(android.support.v8.renderscript.Element element, java.lang.String name) {
            return add(element, name, 1);
        }

        /**
         * Create the element from this builder.
         *
         * @return Element
         */
        public android.support.v8.renderscript.Element create() {
            mRS.validate();
            android.support.v8.renderscript.Element[] ein = new android.support.v8.renderscript.Element[mCount];
            java.lang.String[] sin = new java.lang.String[mCount];
            int[] asin = new int[mCount];
            java.lang.System.arraycopy(mElements, 0, ein, 0, mCount);
            java.lang.System.arraycopy(mElementNames, 0, sin, 0, mCount);
            java.lang.System.arraycopy(mArraySizes, 0, asin, 0, mCount);
            long[] ids = new long[ein.length];
            for (int ct = 0; ct < ein.length; ct++) {
                ids[ct] = ein[ct].getID(mRS);
            }
            long id = mRS.nElementCreate2(ids, sin, asin);
            return new android.support.v8.renderscript.Element(id, mRS, ein, sin, asin);
        }
    }
}

