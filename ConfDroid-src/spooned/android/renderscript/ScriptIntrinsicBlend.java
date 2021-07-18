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
 * Intrinsic kernels for blending two {@link android.renderscript.Allocation} objects.
 */
/* public void forEachStamp(Allocation ain, Allocation aout) {
blend(36, ain, aout);
}

public void forEachRed(Allocation ain, Allocation aout) {
blend(37, ain, aout);
}

public void forEachGreen(Allocation ain, Allocation aout) {
blend(38, ain, aout);
}

public void forEachBlue(Allocation ain, Allocation aout) {
blend(39, ain, aout);
}

public void forEachHue(Allocation ain, Allocation aout) {
blend(40, ain, aout);
}

public void forEachSaturation(Allocation ain, Allocation aout) {
blend(41, ain, aout);
}

public void forEachColor(Allocation ain, Allocation aout) {
blend(42, ain, aout);
}

public void forEachLuminosity(Allocation ain, Allocation aout) {
blend(43, ain, aout);
}
 */
public class ScriptIntrinsicBlend extends android.renderscript.ScriptIntrinsic {
    ScriptIntrinsicBlend(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
    }

    /**
     * Supported elements types are {@link Element#U8_4}
     *
     * @param rs
     * 		The RenderScript context
     * @param e
     * 		Element type for inputs and outputs
     * @return ScriptIntrinsicBlend
     */
    public static android.renderscript.ScriptIntrinsicBlend create(android.renderscript.RenderScript rs, android.renderscript.Element e) {
        // 7 comes from RS_SCRIPT_INTRINSIC_ID_BLEND in rsDefines.h
        long id = rs.nScriptIntrinsicCreate(7, e.getID(rs));
        return new android.renderscript.ScriptIntrinsicBlend(id, rs);
    }

    private void blend(int id, android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        if (!ain.getElement().isCompatible(android.renderscript.Element.U8_4(mRS))) {
            throw new android.renderscript.RSIllegalArgumentException("Input is not of expected format.");
        }
        if (!aout.getElement().isCompatible(android.renderscript.Element.U8_4(mRS))) {
            throw new android.renderscript.RSIllegalArgumentException("Output is not of expected format.");
        }
        forEach(id, ain, aout, null, opt);
    }

    /**
     * Sets dst = {0, 0, 0, 0}
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachClear(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachClear(ain, aout, null);
    }

    /**
     * Sets dst = {0, 0, 0, 0}
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachClear(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(0, ain, aout, opt);
    }

    /**
     * Get a KernelID for the Clear kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDClear() {
        return createKernelID(0, 3, null, null);
    }

    /**
     * Sets dst = src
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachSrc(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachSrc(ain, aout, null);
    }

    /**
     * Sets dst = src
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachSrc(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(1, ain, aout, null);
    }

    /**
     * Get a KernelID for the Src kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDSrc() {
        return createKernelID(1, 3, null, null);
    }

    /**
     * Sets dst = dst
     *
     * This is a NOP.
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachDst(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        // NOP
    }

    /**
     * Sets dst = dst
     *
     * This is a NOP.
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachDst(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        // N, optOP
    }

    /**
     * Get a KernelID for the Dst kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDDst() {
        return createKernelID(2, 3, null, null);
    }

    /**
     * Sets dst = src + dst * (1.0 - src.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachSrcOver(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachSrcOver(ain, aout, null);
    }

    /**
     * Sets dst = src + dst * (1.0 - src.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachSrcOver(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(3, ain, aout, opt);
    }

    /**
     * Get a KernelID for the SrcOver kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDSrcOver() {
        return createKernelID(3, 3, null, null);
    }

    /**
     * Sets dst = dst + src * (1.0 - dst.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachDstOver(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachDstOver(ain, aout, null);
    }

    /**
     * Sets dst = dst + src * (1.0 - dst.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachDstOver(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(4, ain, aout, opt);
    }

    /**
     * Get a KernelID for the DstOver kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDDstOver() {
        return createKernelID(4, 3, null, null);
    }

    /**
     * Sets dst = src * dst.a
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachSrcIn(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachSrcIn(ain, aout, null);
    }

    /**
     * Sets dst = src * dst.a
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachSrcIn(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(5, ain, aout, opt);
    }

    /**
     * Get a KernelID for the SrcIn kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDSrcIn() {
        return createKernelID(5, 3, null, null);
    }

    /**
     * Sets dst = dst * src.a
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachDstIn(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachDstIn(ain, aout, null);
    }

    /**
     * Sets dst = dst * src.a
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachDstIn(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(6, ain, aout, opt);
    }

    /**
     * Get a KernelID for the DstIn kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDDstIn() {
        return createKernelID(6, 3, null, null);
    }

    /**
     * Sets dst = src * (1.0 - dst.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachSrcOut(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachSrcOut(ain, aout, null);
    }

    /**
     * Sets dst = src * (1.0 - dst.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachSrcOut(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(7, ain, aout, opt);
    }

    /**
     * Get a KernelID for the SrcOut kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDSrcOut() {
        return createKernelID(7, 3, null, null);
    }

    /**
     * Sets dst = dst * (1.0 - src.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachDstOut(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachDstOut(ain, aout, null);
    }

    /**
     * Sets dst = dst * (1.0 - src.a)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachDstOut(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(8, ain, aout, opt);
    }

    /**
     * Get a KernelID for the DstOut kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDDstOut() {
        return createKernelID(8, 3, null, null);
    }

    /**
     * dst.rgb = src.rgb * dst.a + (1.0 - src.a) * dst.rgb
     * dst.a = dst.a
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachSrcAtop(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachSrcAtop(ain, aout, null);
    }

    /**
     * dst.rgb = src.rgb * dst.a + (1.0 - src.a) * dst.rgb
     * dst.a = dst.a
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachSrcAtop(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(9, ain, aout, opt);
    }

    /**
     * Get a KernelID for the SrcAtop kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDSrcAtop() {
        return createKernelID(9, 3, null, null);
    }

    /**
     * dst = dst.rgb * src.a + (1.0 - dst.a) * src.rgb
     * dst.a = src.a
     * Note: Before API 23, the alpha channel was not correctly set.
     *       Please use with caution when targeting older APIs.
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachDstAtop(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachDstAtop(ain, aout, null);
    }

    /**
     * dst = dst.rgb * src.a + (1.0 - dst.a) * src.rgb
     * dst.a = src.a
     * Note: Before API 23, the alpha channel was not correctly set.
     *       Please use with caution when targeting older APIs.
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachDstAtop(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(10, ain, aout, opt);
    }

    /**
     * Get a KernelID for the DstAtop kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDDstAtop() {
        return createKernelID(10, 3, null, null);
    }

    /**
     * Sets dst = {src.r ^ dst.r, src.g ^ dst.g, src.b ^ dst.b, src.a ^ dst.a}
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachXor(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachXor(ain, aout, null);
    }

    /**
     * Sets dst = {src.r ^ dst.r, src.g ^ dst.g, src.b ^ dst.b, src.a ^ dst.a}
     *
     * <b>Note:</b> this is NOT the Porter/Duff XOR mode; this is a bitwise xor.
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachXor(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(11, ain, aout, opt);
    }

    /**
     * Get a KernelID for the Xor kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDXor() {
        return createKernelID(11, 3, null, null);
    }

    // //////
    /* public void forEachNormal(Allocation ain, Allocation aout) {
    blend(12, ain, aout);
    }

    public void forEachAverage(Allocation ain, Allocation aout) {
    blend(13, ain, aout);
    }
     */
    /**
     * Sets dst = src * dst
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachMultiply(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachMultiply(ain, aout, null);
    }

    /**
     * Sets dst = src * dst
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachMultiply(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(14, ain, aout, opt);
    }

    /**
     * Get a KernelID for the Multiply kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDMultiply() {
        return createKernelID(14, 3, null, null);
    }

    /* public void forEachScreen(Allocation ain, Allocation aout) {
    blend(15, ain, aout);
    }

    public void forEachDarken(Allocation ain, Allocation aout) {
    blend(16, ain, aout);
    }

    public void forEachLighten(Allocation ain, Allocation aout) {
    blend(17, ain, aout);
    }

    public void forEachOverlay(Allocation ain, Allocation aout) {
    blend(18, ain, aout);
    }

    public void forEachHardlight(Allocation ain, Allocation aout) {
    blend(19, ain, aout);
    }

    public void forEachSoftlight(Allocation ain, Allocation aout) {
    blend(20, ain, aout);
    }

    public void forEachDifference(Allocation ain, Allocation aout) {
    blend(21, ain, aout);
    }

    public void forEachNegation(Allocation ain, Allocation aout) {
    blend(22, ain, aout);
    }

    public void forEachExclusion(Allocation ain, Allocation aout) {
    blend(23, ain, aout);
    }

    public void forEachColorDodge(Allocation ain, Allocation aout) {
    blend(24, ain, aout);
    }

    public void forEachInverseColorDodge(Allocation ain, Allocation aout) {
    blend(25, ain, aout);
    }

    public void forEachSoftDodge(Allocation ain, Allocation aout) {
    blend(26, ain, aout);
    }

    public void forEachColorBurn(Allocation ain, Allocation aout) {
    blend(27, ain, aout);
    }

    public void forEachInverseColorBurn(Allocation ain, Allocation aout) {
    blend(28, ain, aout);
    }

    public void forEachSoftBurn(Allocation ain, Allocation aout) {
    blend(29, ain, aout);
    }

    public void forEachReflect(Allocation ain, Allocation aout) {
    blend(30, ain, aout);
    }

    public void forEachGlow(Allocation ain, Allocation aout) {
    blend(31, ain, aout);
    }

    public void forEachFreeze(Allocation ain, Allocation aout) {
    blend(32, ain, aout);
    }

    public void forEachHeat(Allocation ain, Allocation aout) {
    blend(33, ain, aout);
    }
     */
    /**
     * Sets dst = min(src + dst, 1.0)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachAdd(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachAdd(ain, aout, null);
    }

    /**
     * Sets dst = min(src + dst, 1.0)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachAdd(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(34, ain, aout, opt);
    }

    /**
     * Get a KernelID for the Add kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDAdd() {
        return createKernelID(34, 3, null, null);
    }

    /**
     * Sets dst = max(dst - src, 0.0)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     */
    public void forEachSubtract(android.renderscript.Allocation ain, android.renderscript.Allocation aout) {
        forEachSubtract(ain, aout, null);
    }

    /**
     * Sets dst = max(dst - src, 0.0)
     *
     * @param ain
     * 		The source buffer
     * @param aout
     * 		The destination buffer
     * @param opt
     * 		LaunchOptions for clipping
     */
    public void forEachSubtract(android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions opt) {
        blend(35, ain, aout, opt);
    }

    /**
     * Get a KernelID for the Subtract kernel.
     *
     * @return Script.KernelID The KernelID object.
     */
    public android.renderscript.Script.KernelID getKernelIDSubtract() {
        return createKernelID(35, 3, null, null);
    }
}

