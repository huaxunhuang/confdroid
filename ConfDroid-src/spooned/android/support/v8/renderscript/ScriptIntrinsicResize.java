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
 * Intrinsic for performing a resize of a 2D allocation.
 */
public class ScriptIntrinsicResize extends android.support.v8.renderscript.ScriptIntrinsic {
    private android.support.v8.renderscript.Allocation mInput;

    // API level for the intrinsic
    private static final int INTRINSIC_API_LEVEL = 21;

    protected ScriptIntrinsicResize(long id, android.support.v8.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     * Supported elements types are {@link Element#U8}, {@link Element#U8_2}, {@link Element#U8_3}, {@link Element#U8_4}
     * {@link Element#F32}, {@link Element#F32_2}, {@link Element#F32_3}, {@link Element#F32_4}
     *
     * @param rs
     * 		The RenderScript context
     * @return ScriptIntrinsicResize
     */
    public static android.support.v8.renderscript.ScriptIntrinsicResize create(android.support.v8.renderscript.RenderScript rs) {
        long id;
        boolean mUseIncSupp = rs.isUseNative() && (android.os.Build.VERSION.SDK_INT < android.support.v8.renderscript.ScriptIntrinsicResize.INTRINSIC_API_LEVEL);
        id = rs.nScriptIntrinsicCreate(12, 0, mUseIncSupp);
        android.support.v8.renderscript.ScriptIntrinsicResize si = new android.support.v8.renderscript.ScriptIntrinsicResize(id, rs);
        si.setIncSupp(mUseIncSupp);
        return si;
    }

    /**
     * Set the input of the resize.
     * Must match the element type supplied during create.
     *
     * @param ain
     * 		The input allocation.
     */
    public void setInput(android.support.v8.renderscript.Allocation ain) {
        android.support.v8.renderscript.Element e = ain.getElement();
        if ((((((((!e.isCompatible(android.support.v8.renderscript.Element.U8(mRS))) && (!e.isCompatible(android.support.v8.renderscript.Element.U8_2(mRS)))) && (!e.isCompatible(android.support.v8.renderscript.Element.U8_3(mRS)))) && (!e.isCompatible(android.support.v8.renderscript.Element.U8_4(mRS)))) && (!e.isCompatible(android.support.v8.renderscript.Element.F32(mRS)))) && (!e.isCompatible(android.support.v8.renderscript.Element.F32_2(mRS)))) && (!e.isCompatible(android.support.v8.renderscript.Element.F32_3(mRS)))) && (!e.isCompatible(android.support.v8.renderscript.Element.F32_4(mRS)))) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Unsupported element type.");
        }
        mInput = ain;
        setVar(0, ain);
    }

    /**
     * Get a FieldID for the input field of this intrinsic.
     *
     * @return Script.FieldID The FieldID object.
     */
    public android.support.v8.renderscript.Script.FieldID getFieldID_Input() {
        return createFieldID(0, null);
    }

    /**
     * Resize copy the input allocation to the output specified. The
     * Allocation is rescaled if necessary using bi-cubic
     * interpolation.
     *
     * @param aout
     * 		Output allocation. Element type must match
     * 		current input.  Must not be same as input.
     */
    public void forEach_bicubic(android.support.v8.renderscript.Allocation aout) {
        if (aout == mInput) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Output cannot be same as Input.");
        }
        forEach_bicubic(aout, null);
    }

    /**
     * Resize copy the input allocation to the output specified. The
     * Allocation is rescaled if necessary using bi-cubic
     * interpolation.
     *
     * @param aout
     * 		Output allocation. Element type must match
     * 		current input.
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEach_bicubic(android.support.v8.renderscript.Allocation aout, android.support.v8.renderscript.Script.LaunchOptions opt) {
        forEach(0, ((android.support.v8.renderscript.Allocation) (null)), aout, null, opt);
    }

    /**
     * Get a KernelID for this intrinsic kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.support.v8.renderscript.Script.KernelID getKernelID_bicubic() {
        return createKernelID(0, 2, null, null);
    }
}

