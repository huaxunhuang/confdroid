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
 * Intrinsic for converting RGB to RGBA by using a 3D lookup table.  The
 * incoming r,g,b values are use as normalized x,y,z coordinates into a 3D
 * allocation.  The 8 nearest values are sampled and linearly interpolated.  The
 * result is placed in the output.
 */
public final class ScriptIntrinsic3DLUT extends android.renderscript.ScriptIntrinsic {
    private android.renderscript.Allocation mLUT;

    private android.renderscript.Element mElement;

    private ScriptIntrinsic3DLUT(long id, android.renderscript.RenderScript rs, android.renderscript.Element e) {
        super(id, rs);
        mElement = e;
    }

    /**
     * Supported elements types are {@link Element#U8_4}
     *
     * The defaults tables are identity.
     *
     * @param rs
     * 		The RenderScript context
     * @param e
     * 		Element type for intputs and outputs
     * @return ScriptIntrinsic3DLUT
     */
    public static android.renderscript.ScriptIntrinsic3DLUT create(android.renderscript.RenderScript rs, android.renderscript.Element e) {
        long id = rs.nScriptIntrinsicCreate(8, e.getID(rs));
        if (!e.isCompatible(android.renderscript.Element.U8_4(rs))) {
            throw new android.renderscript.RSIllegalArgumentException("Element must be compatible with uchar4.");
        }
        return new android.renderscript.ScriptIntrinsic3DLUT(id, rs, e);
    }

    /**
     * Sets the {@link android.renderscript.Allocation} to be used as the lookup table.
     *
     * The lookup table must use the same {@link android.renderscript.Element} as the intrinsic.
     */
    public void setLUT(android.renderscript.Allocation lut) {
        final android.renderscript.Type t = lut.getType();
        if (t.getZ() == 0) {
            throw new android.renderscript.RSIllegalArgumentException("LUT must be 3d.");
        }
        if (!t.getElement().isCompatible(mElement)) {
            throw new android.renderscript.RSIllegalArgumentException("LUT element type must match.");
        }
        mLUT = lut;
        setVar(0, mLUT);
    }

    /**
     * Invoke the kernel and apply the lookup to each cell of ain
     * and copy to aout.
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
     * Invoke the kernel and apply the lookup to each cell of ain
     * and copy to aout.
     *
     * @param ain
     * 		Input allocation
     * @param aout
     * 		Output allocation
     * @param opt
     * 		Launch options for kernel
     */
    public void forEach(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
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

