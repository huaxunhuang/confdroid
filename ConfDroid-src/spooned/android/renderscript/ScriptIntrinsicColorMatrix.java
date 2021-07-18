/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Intrinsic for applying a color matrix to allocations.
 *
 * If the element type is {@link Element.DataType#UNSIGNED_8},
 * it is converted to {@link Element.DataType#FLOAT_32} and
 * normalized from (0-255) to (0-1). If the incoming vector size
 * is less than four, a {@link Element#F32_4} is created by
 * filling the missing vector channels with zero. This value is
 * then multiplied by the 4x4 color matrix as performed by
 * rsMatrixMultiply(), adding a {@link Element#F32_4}, and then
 * writing it to the output {@link Allocation}.
 *
 * If the ouptut type is unsigned, the value is normalized from
 * (0-1) to (0-255) and converted. If the output vector size is
 * less than four, the unused channels are discarded.
 *
 * Supported elements types are {@link Element#U8}, {@link Element#U8_2}, {@link Element#U8_3}, {@link Element#U8_4},
 * {@link Element#F32}, {@link Element#F32_2}, {@link Element#F32_3}, and {@link Element#F32_4}.
 */
public final class ScriptIntrinsicColorMatrix extends android.renderscript.ScriptIntrinsic {
    private final android.renderscript.Matrix4f mMatrix = new android.renderscript.Matrix4f();

    private final android.renderscript.Float4 mAdd = new android.renderscript.Float4();

    private ScriptIntrinsicColorMatrix(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     * Create an intrinsic for applying a color matrix to an
     * allocation.
     *
     * @param rs
     * 		The RenderScript context
     * @param e
     * 		Element type for inputs and outputs, As of API 19,
     * 		this parameter is ignored. The Element type check is
     * 		performed in the kernel launch.
     * @deprecated Use the single argument version as Element is now
    ignored.
     * @return ScriptIntrinsicColorMatrix
     */
    @java.lang.Deprecated
    public static android.renderscript.ScriptIntrinsicColorMatrix create(android.renderscript.RenderScript rs, android.renderscript.Element e) {
        return android.renderscript.ScriptIntrinsicColorMatrix.create(rs);
    }

    /**
     * Create an intrinsic for applying a color matrix to an
     * allocation.
     *
     * @param rs
     * 		The RenderScript context
     * @return ScriptIntrinsicColorMatrix
     */
    public static android.renderscript.ScriptIntrinsicColorMatrix create(android.renderscript.RenderScript rs) {
        long id = rs.nScriptIntrinsicCreate(2, 0);
        return new android.renderscript.ScriptIntrinsicColorMatrix(id, rs);
    }

    private void setMatrix() {
        android.renderscript.FieldPacker fp = new android.renderscript.FieldPacker(16 * 4);
        fp.addMatrix(mMatrix);
        setVar(0, fp);
    }

    /**
     * Set the color matrix which will be applied to each cell of
     * the image.
     *
     * @param m
     * 		The 4x4 matrix to set.
     */
    public void setColorMatrix(android.renderscript.Matrix4f m) {
        mMatrix.load(m);
        setMatrix();
    }

    /**
     * Set the color matrix which will be applied to each cell of the image.
     * This will set the alpha channel to be a copy.
     *
     * @param m
     * 		The 3x3 matrix to set.
     */
    public void setColorMatrix(android.renderscript.Matrix3f m) {
        mMatrix.load(m);
        setMatrix();
    }

    /**
     * Set the value to be added after the color matrix has been
     * applied. The default value is {0, 0, 0, 0}
     *
     * @param f
     * 		The float4 value to be added.
     */
    public void setAdd(android.renderscript.Float4 f) {
        mAdd.x = f.x;
        mAdd.y = f.y;
        mAdd.z = f.z;
        mAdd.w = f.w;
        android.renderscript.FieldPacker fp = new android.renderscript.FieldPacker(4 * 4);
        fp.addF32(f.x);
        fp.addF32(f.y);
        fp.addF32(f.z);
        fp.addF32(f.w);
        setVar(1, fp);
    }

    /**
     * Set the value to be added after the color matrix has been
     * applied. The default value is {0, 0, 0, 0}
     *
     * @param r
     * 		The red add value.
     * @param g
     * 		The green add value.
     * @param b
     * 		The blue add value.
     * @param a
     * 		The alpha add value.
     */
    public void setAdd(float r, float g, float b, float a) {
        mAdd.x = r;
        mAdd.y = g;
        mAdd.z = b;
        mAdd.w = a;
        android.renderscript.FieldPacker fp = new android.renderscript.FieldPacker(4 * 4);
        fp.addF32(mAdd.x);
        fp.addF32(mAdd.y);
        fp.addF32(mAdd.z);
        fp.addF32(mAdd.w);
        setVar(1, fp);
    }

    /**
     * Set a color matrix to convert from RGB to luminance. The alpha channel
     * will be a copy.
     */
    public void setGreyscale() {
        mMatrix.loadIdentity();
        mMatrix.set(0, 0, 0.299F);
        mMatrix.set(1, 0, 0.587F);
        mMatrix.set(2, 0, 0.114F);
        mMatrix.set(0, 1, 0.299F);
        mMatrix.set(1, 1, 0.587F);
        mMatrix.set(2, 1, 0.114F);
        mMatrix.set(0, 2, 0.299F);
        mMatrix.set(1, 2, 0.587F);
        mMatrix.set(2, 2, 0.114F);
        setMatrix();
    }

    /**
     * Set the matrix to convert from YUV to RGB with a direct copy of the 4th
     * channel.
     */
    public void setYUVtoRGB() {
        mMatrix.loadIdentity();
        mMatrix.set(0, 0, 1.0F);
        mMatrix.set(1, 0, 0.0F);
        mMatrix.set(2, 0, 1.13983F);
        mMatrix.set(0, 1, 1.0F);
        mMatrix.set(1, 1, -0.39465F);
        mMatrix.set(2, 1, -0.5806F);
        mMatrix.set(0, 2, 1.0F);
        mMatrix.set(1, 2, 2.03211F);
        mMatrix.set(2, 2, 0.0F);
        setMatrix();
    }

    /**
     * Set the matrix to convert from RGB to YUV with a direct copy of the 4th
     * channel.
     */
    public void setRGBtoYUV() {
        mMatrix.loadIdentity();
        mMatrix.set(0, 0, 0.299F);
        mMatrix.set(1, 0, 0.587F);
        mMatrix.set(2, 0, 0.114F);
        mMatrix.set(0, 1, -0.14713F);
        mMatrix.set(1, 1, -0.28886F);
        mMatrix.set(2, 1, 0.436F);
        mMatrix.set(0, 2, 0.615F);
        mMatrix.set(1, 2, -0.51499F);
        mMatrix.set(2, 2, -0.10001F);
        setMatrix();
    }

    /**
     * Invoke the kernel and apply the matrix to each cell of input
     * {@link Allocation} and copy to the output {@link Allocation}.
     *
     * If the vector size of the input is less than four, the
     * remaining components are treated as zero for the matrix
     * multiply.
     *
     * If the output vector size is less than four, the unused
     * vector components are discarded.
     *
     * @param ain
     * 		Input allocation
     * @param aout
     * 		Output allocation
     */
    public void forEach(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEach(ain, aout, null);
    }

    /**
     * Invoke the kernel and apply the matrix to each cell of input
     * {@link Allocation} and copy to the output {@link Allocation}.
     *
     * If the vector size of the input is less than four, the
     * remaining components are treated as zero for the matrix
     * multiply.
     *
     * If the output vector size is less than four, the unused
     * vector components are discarded.
     *
     * @param ain
     * 		Input allocation
     * @param aout
     * 		Output allocation
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEach(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        if ((((((((!ain.getElement().isCompatible(android.renderscript.Element.U8(mRS))) && (!ain.getElement().isCompatible(android.renderscript.Element.U8_2(mRS)))) && (!ain.getElement().isCompatible(android.renderscript.Element.U8_3(mRS)))) && (!ain.getElement().isCompatible(android.renderscript.Element.U8_4(mRS)))) && (!ain.getElement().isCompatible(android.renderscript.Element.F32(mRS)))) && (!ain.getElement().isCompatible(android.renderscript.Element.F32_2(mRS)))) && (!ain.getElement().isCompatible(android.renderscript.Element.F32_3(mRS)))) && (!ain.getElement().isCompatible(android.renderscript.Element.F32_4(mRS)))) {
            throw new android.renderscript.RSIllegalArgumentException("Unsuported element type.");
        }
        if ((((((((!aout.getElement().isCompatible(android.renderscript.Element.U8(mRS))) && (!aout.getElement().isCompatible(android.renderscript.Element.U8_2(mRS)))) && (!aout.getElement().isCompatible(android.renderscript.Element.U8_3(mRS)))) && (!aout.getElement().isCompatible(android.renderscript.Element.U8_4(mRS)))) && (!aout.getElement().isCompatible(android.renderscript.Element.F32(mRS)))) && (!aout.getElement().isCompatible(android.renderscript.Element.F32_2(mRS)))) && (!aout.getElement().isCompatible(android.renderscript.Element.F32_3(mRS)))) && (!aout.getElement().isCompatible(android.renderscript.Element.F32_4(mRS)))) {
            throw new android.renderscript.RSIllegalArgumentException("Unsuported element type.");
        }
        forEach(0, ain, aout, null, opt);
    }

    /**
     * Get a KernelID for this intrinsic kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelID() {
        return createKernelID(0, 3, null, null);
    }
}

