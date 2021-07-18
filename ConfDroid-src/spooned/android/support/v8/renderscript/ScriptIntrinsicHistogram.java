/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Intrinsic Histogram filter.
 */
public class ScriptIntrinsicHistogram extends android.support.v8.renderscript.ScriptIntrinsic {
    private android.support.v8.renderscript.Allocation mOut;

    // API level for the intrinsic
    private static final int INTRINSIC_API_LEVEL = 19;

    protected ScriptIntrinsicHistogram(long id, android.support.v8.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     * Create an intrinsic for calculating the histogram of an uchar
     * or uchar4 image.
     *
     * Supported elements types are
     * {@link Element#U8_4}, {@link Element#U8_3},
     * {@link Element#U8_2}, {@link Element#U8}
     *
     * @param rs
     * 		The RenderScript context
     * @param e
     * 		Element type for inputs
     * @return ScriptIntrinsicHistogram
     */
    public static android.support.v8.renderscript.ScriptIntrinsicHistogram create(android.support.v8.renderscript.RenderScript rs, android.support.v8.renderscript.Element e) {
        if ((((!e.isCompatible(android.support.v8.renderscript.Element.U8_4(rs))) && (!e.isCompatible(android.support.v8.renderscript.Element.U8_3(rs)))) && (!e.isCompatible(android.support.v8.renderscript.Element.U8_2(rs)))) && (!e.isCompatible(android.support.v8.renderscript.Element.U8(rs)))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Unsupported element type.");
        }
        long id;
        boolean mUseIncSupp = rs.isUseNative() && (android.os.Build.VERSION.SDK_INT < android.support.v8.renderscript.ScriptIntrinsicHistogram.INTRINSIC_API_LEVEL);
        id = rs.nScriptIntrinsicCreate(9, e.getID(rs), mUseIncSupp);
        android.support.v8.renderscript.ScriptIntrinsicHistogram si = new android.support.v8.renderscript.ScriptIntrinsicHistogram(id, rs);
        si.setIncSupp(mUseIncSupp);
        return si;
    }

    /**
     * Process an input buffer and place the histogram into the
     * output allocation. The output allocation may be a narrower
     * vector size than the input. In this case the vector size of
     * the output is used to determine how many of the input
     * channels are used in the computation. This is useful if you
     * have an RGBA input buffer but only want the histogram for
     * RGB.
     *
     * 1D and 2D input allocations are supported.
     *
     * @param ain
     * 		The input image
     */
    public void forEach(android.support.v8.renderscript.Allocation ain) {
        forEach(ain, null);
    }

    /**
     * Process an input buffer and place the histogram into the
     * output allocation. The output allocation may be a narrower
     * vector size than the input. In this case the vector size of
     * the output is used to determine how many of the input
     * channels are used in the computation. This is useful if you
     * have an RGBA input buffer but only want the histogram for
     * RGB.
     *
     * 1D and 2D input allocations are supported.
     *
     * @param ain
     * 		The input image
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEach(android.support.v8.renderscript.Allocation ain, android.support.v8.renderscript.Script.LaunchOptions opt) {
        if (ain.getType().getElement().getVectorSize() < mOut.getType().getElement().getVectorSize()) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Input vector size must be >= output vector size.");
        }
        if ((((!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8(mRS))) && (!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8_2(mRS)))) && (!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8_3(mRS)))) && (!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8_4(mRS)))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Input type must be U8, U8_1, U8_2 or U8_4.");
        }
        forEach(0, ain, null, null, opt);
    }

    /**
     * Set the coefficients used for the RGBA to Luminocity
     * calculation. The default is {0.299f, 0.587f, 0.114f, 0.f}.
     *
     * Coefficients must be >= 0 and sum to 1.0 or less.
     *
     * @param r
     * 		Red coefficient
     * @param g
     * 		Green coefficient
     * @param b
     * 		Blue coefficient
     * @param a
     * 		Alpha coefficient
     */
    public void setDotCoefficients(float r, float g, float b, float a) {
        if ((((r < 0.0F) || (g < 0.0F)) || (b < 0.0F)) || (a < 0.0F)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Coefficient may not be negative.");
        }
        if ((((r + g) + b) + a) > 1.0F) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Sum of coefficients must be 1.0 or less.");
        }
        android.support.v8.renderscript.FieldPacker fp = new android.support.v8.renderscript.FieldPacker(16);
        fp.addF32(r);
        fp.addF32(g);
        fp.addF32(b);
        fp.addF32(a);
        setVar(0, fp);
    }

    /**
     * Set the output of the histogram.  32 bit integer types are
     * supported.
     *
     * @param aout
     * 		The output allocation
     */
    public void setOutput(android.support.v8.renderscript.Allocation aout) {
        mOut = aout;
        if ((((((((mOut.getType().getElement() != android.support.v8.renderscript.Element.U32(mRS)) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.U32_2(mRS))) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.U32_3(mRS))) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.U32_4(mRS))) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.I32(mRS))) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.I32_2(mRS))) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.I32_3(mRS))) && (mOut.getType().getElement() != android.support.v8.renderscript.Element.I32_4(mRS))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Output type must be U32 or I32.");
        }
        if ((((mOut.getType().getX() != 256) || (mOut.getType().getY() != 0)) || mOut.getType().hasMipmaps()) || (mOut.getType().getYuv() != 0)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Output must be 1D, 256 elements.");
        }
        setVar(1, aout);
    }

    /**
     * Process an input buffer and place the histogram into the
     * output allocation. The dot product of the input channel and
     * the coefficients from 'setDotCoefficients' are used to
     * calculate the output values.
     *
     * 1D and 2D input allocations are supported.
     *
     * @param ain
     * 		The input image
     */
    public void forEach_Dot(android.support.v8.renderscript.Allocation ain) {
        forEach_Dot(ain, null);
    }

    /**
     * Process an input buffer and place the histogram into the
     * output allocation. The dot product of the input channel and
     * the coefficients from 'setDotCoefficients' are used to
     * calculate the output values.
     *
     * 1D and 2D input allocations are supported.
     *
     * @param ain
     * 		The input image
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEach_Dot(android.support.v8.renderscript.Allocation ain, android.support.v8.renderscript.Script.LaunchOptions opt) {
        if (mOut.getType().getElement().getVectorSize() != 1) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Output vector size must be one.");
        }
        if ((((!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8(mRS))) && (!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8_2(mRS)))) && (!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8_3(mRS)))) && (!ain.getType().getElement().isCompatible(android.support.v8.renderscript.Element.U8_4(mRS)))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Input type must be U8, U8_1, U8_2 or U8_4.");
        }
        forEach(1, ain, null, null, opt);
    }

    /**
     * Get a KernelID for this intrinsic kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.support.v8.renderscript.Script.KernelID getKernelID_Separate() {
        return createKernelID(0, 3, null, null);
    }

    /**
     * Get a FieldID for the input field of this intrinsic.
     *
     * @return Script.FieldID The FieldID object.
     */
    public android.support.v8.renderscript.Script.FieldID getFieldID_Input() {
        return createFieldID(1, null);
    }
}

