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
 * Intrinsic for applying a 3x3 convolve to an allocation.
 */
public final class ScriptIntrinsicConvolve3x3 extends android.renderscript.ScriptIntrinsic {
    private final float[] mValues = new float[9];

    private android.renderscript.Allocation mInput;

    private ScriptIntrinsicConvolve3x3(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     * Supported elements types are {@link Element#U8}, {@link Element#U8_2}, {@link Element#U8_3}, {@link Element#U8_4},
     * {@link Element#F32}, {@link Element#F32_2}, {@link Element#F32_3}, and {@link Element#F32_4}.
     *
     * <p> The default coefficients are:
     * <code>
     * <p> [ 0,  0,  0 ]
     * <p> [ 0,  1,  0 ]
     * <p> [ 0,  0,  0 ]
     * </code>
     *
     * @param rs
     * 		The RenderScript context
     * @param e
     * 		Element type for intputs and outputs
     * @return ScriptIntrinsicConvolve3x3
     */
    public static android.renderscript.ScriptIntrinsicConvolve3x3 create(android.renderscript.RenderScript rs, android.renderscript.Element e) {
        float[] f = new float[]{ 0, 0, 0, 0, 1, 0, 0, 0, 0 };
        if ((((((((!e.isCompatible(android.renderscript.Element.U8(rs))) && (!e.isCompatible(android.renderscript.Element.U8_2(rs)))) && (!e.isCompatible(android.renderscript.Element.U8_3(rs)))) && (!e.isCompatible(android.renderscript.Element.U8_4(rs)))) && (!e.isCompatible(android.renderscript.Element.F32(rs)))) && (!e.isCompatible(android.renderscript.Element.F32_2(rs)))) && (!e.isCompatible(android.renderscript.Element.F32_3(rs)))) && (!e.isCompatible(android.renderscript.Element.F32_4(rs)))) {
            throw new android.renderscript.RSIllegalArgumentException("Unsuported element type.");
        }
        long id = rs.nScriptIntrinsicCreate(1, e.getID(rs));
        android.renderscript.ScriptIntrinsicConvolve3x3 si = new android.renderscript.ScriptIntrinsicConvolve3x3(id, rs);
        si.setCoefficients(f);
        return si;
    }

    /**
     * Set the input of the 3x3 convolve.
     * Must match the element type supplied during create.
     *
     * @param ain
     * 		The input allocation.
     */
    public void setInput(android.renderscript.Allocation ain) {
        mInput = ain;
        setVar(1, ain);
    }

    /**
     * Set the coefficients for the convolve.
     *
     * <p> The convolve layout is:
     * <code>
     * <p> [ 0,  1,  2 ]
     * <p> [ 3,  4,  5 ]
     * <p> [ 6,  7,  8 ]
     * </code>
     *
     * @param v
     * 		The array of coefficients to set
     */
    public void setCoefficients(float[] v) {
        android.renderscript.FieldPacker fp = new android.renderscript.FieldPacker(9 * 4);
        for (int ct = 0; ct < mValues.length; ct++) {
            mValues[ct] = v[ct];
            fp.addF32(mValues[ct]);
        }
        setVar(0, fp);
    }

    /**
     * Apply the filter to the input and save to the specified
     * allocation.
     *
     * @param aout
     * 		Output allocation. Must match creation element
     * 		type.
     */
    public void forEach(android.renderscript.Allocation aout) {
        forEach(0, ((android.renderscript.Allocation) (null)), aout, null);
    }

    /**
     * Apply the filter to the input and save to the specified
     * allocation.
     *
     * @param aout
     * 		Output allocation. Must match creation element
     * 		type.
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEach(android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        forEach(0, ((android.renderscript.Allocation) (null)), aout, null, opt);
    }

    /**
     * Get a KernelID for this intrinsic kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelID() {
        return createKernelID(0, 2, null, null);
    }

    /**
     * Get a FieldID for the input field of this intrinsic.
     *
     * @return Script.FieldID The FieldID object.
     */
    public android.renderscript.Script.FieldID getFieldID_Input() {
        return createFieldID(1, null);
    }
}

