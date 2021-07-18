/**
 * Copyright (C) 2008-2012 The Android Open Source Project
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
 * The parent class for all executable scripts. This should not be used by
 * applications.
 */
public class Script extends android.renderscript.BaseObj {
    /**
     * KernelID is an identifier for a Script + root function pair. It is used
     * as an identifier for ScriptGroup creation.
     *
     * This class should not be directly created. Instead use the method in the
     * reflected or intrinsic code "getKernelID_funcname()".
     */
    public static final class KernelID extends android.renderscript.BaseObj {
        android.renderscript.Script mScript;

        int mSlot;

        int mSig;

        KernelID(long id, android.renderscript.RenderScript rs, android.renderscript.Script s, int slot, int sig) {
            super(id, rs);
            mScript = s;
            mSlot = slot;
            mSig = sig;
            guard.open("destroy");
        }
    }

    private final android.util.SparseArray<android.renderscript.Script.KernelID> mKIDs = new android.util.SparseArray<android.renderscript.Script.KernelID>();

    /**
     * Only to be used by generated reflected classes.
     */
    protected android.renderscript.Script.KernelID createKernelID(int slot, int sig, android.renderscript.Element ein, android.renderscript.Element eout) {
        android.renderscript.Script.KernelID k = mKIDs.get(slot);
        if (k != null) {
            return k;
        }
        long id = mRS.nScriptKernelIDCreate(getID(mRS), slot, sig);
        if (id == 0) {
            throw new android.renderscript.RSDriverException("Failed to create KernelID");
        }
        k = new android.renderscript.Script.KernelID(id, mRS, this, slot, sig);
        mKIDs.put(slot, k);
        return k;
    }

    /**
     * InvokeID is an identifier for an invoke function. It is used
     * as an identifier for ScriptGroup creation.
     *
     * This class should not be directly created. Instead use the method in the
     * reflected or intrinsic code "getInvokeID_funcname()".
     */
    public static final class InvokeID extends android.renderscript.BaseObj {
        android.renderscript.Script mScript;

        int mSlot;

        InvokeID(long id, android.renderscript.RenderScript rs, android.renderscript.Script s, int slot) {
            super(id, rs);
            mScript = s;
            mSlot = slot;
        }
    }

    private final android.util.SparseArray<android.renderscript.Script.InvokeID> mIIDs = new android.util.SparseArray<android.renderscript.Script.InvokeID>();

    /**
     * Only to be used by generated reflected classes.
     */
    protected android.renderscript.Script.InvokeID createInvokeID(int slot) {
        android.renderscript.Script.InvokeID i = mIIDs.get(slot);
        if (i != null) {
            return i;
        }
        long id = mRS.nScriptInvokeIDCreate(getID(mRS), slot);
        if (id == 0) {
            throw new android.renderscript.RSDriverException("Failed to create KernelID");
        }
        i = new android.renderscript.Script.InvokeID(id, mRS, this, slot);
        mIIDs.put(slot, i);
        return i;
    }

    /**
     * FieldID is an identifier for a Script + exported field pair. It is used
     * as an identifier for ScriptGroup creation.
     *
     * This class should not be directly created. Instead use the method in the
     * reflected or intrinsic code "getFieldID_funcname()".
     */
    public static final class FieldID extends android.renderscript.BaseObj {
        android.renderscript.Script mScript;

        int mSlot;

        FieldID(long id, android.renderscript.RenderScript rs, android.renderscript.Script s, int slot) {
            super(id, rs);
            mScript = s;
            mSlot = slot;
            guard.open("destroy");
        }
    }

    private final android.util.SparseArray<android.renderscript.Script.FieldID> mFIDs = new android.util.SparseArray();

    /**
     * Only to be used by generated reflected classes.
     */
    protected android.renderscript.Script.FieldID createFieldID(int slot, android.renderscript.Element e) {
        android.renderscript.Script.FieldID f = mFIDs.get(slot);
        if (f != null) {
            return f;
        }
        long id = mRS.nScriptFieldIDCreate(getID(mRS), slot);
        if (id == 0) {
            throw new android.renderscript.RSDriverException("Failed to create FieldID");
        }
        f = new android.renderscript.Script.FieldID(id, mRS, this, slot);
        mFIDs.put(slot, f);
        return f;
    }

    /**
     * Only intended for use by generated reflected code.
     */
    protected void invoke(int slot) {
        mRS.nScriptInvoke(getID(mRS), slot);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    protected void invoke(int slot, android.renderscript.FieldPacker v) {
        if (v != null) {
            mRS.nScriptInvokeV(getID(mRS), slot, v.getData());
        } else {
            mRS.nScriptInvoke(getID(mRS), slot);
        }
    }

    /**
     * Only intended for use by generated reflected code.
     */
    protected void forEach(int slot, android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.FieldPacker v) {
        forEach(slot, ain, aout, v, null);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    protected void forEach(int slot, android.renderscript.Allocation ain, android.renderscript.Allocation aout, android.renderscript.FieldPacker v, android.renderscript.Script.LaunchOptions sc) {
        // TODO: Is this necessary if nScriptForEach calls validate as well?
        mRS.validate();
        mRS.validateObject(ain);
        mRS.validateObject(aout);
        if (((ain == null) && (aout == null)) && (sc == null)) {
            throw new android.renderscript.RSIllegalArgumentException("At least one of input allocation, output allocation, or LaunchOptions is required to be non-null.");
        }
        long[] in_ids = null;
        if (ain != null) {
            in_ids = mInIdsBuffer;
            in_ids[0] = ain.getID(mRS);
        }
        long out_id = 0;
        if (aout != null) {
            out_id = aout.getID(mRS);
        }
        byte[] params = null;
        if (v != null) {
            params = v.getData();
        }
        int[] limits = null;
        if (sc != null) {
            limits = new int[6];
            limits[0] = sc.xstart;
            limits[1] = sc.xend;
            limits[2] = sc.ystart;
            limits[3] = sc.yend;
            limits[4] = sc.zstart;
            limits[5] = sc.zend;
        }
        mRS.nScriptForEach(getID(mRS), slot, in_ids, out_id, params, limits);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    protected void forEach(int slot, android.renderscript.Allocation[] ains, android.renderscript.Allocation aout, android.renderscript.FieldPacker v) {
        // FieldPacker is kept here to support regular params in the future.
        forEach(slot, ains, aout, v, null);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    protected void forEach(int slot, android.renderscript.Allocation[] ains, android.renderscript.Allocation aout, android.renderscript.FieldPacker v, android.renderscript.Script.LaunchOptions sc) {
        // TODO: Is this necessary if nScriptForEach calls validate as well?
        // FieldPacker is kept here to support regular params in the future.
        mRS.validate();
        if (ains != null) {
            for (android.renderscript.Allocation ain : ains) {
                mRS.validateObject(ain);
            }
        }
        mRS.validateObject(aout);
        if ((ains == null) && (aout == null)) {
            throw new android.renderscript.RSIllegalArgumentException("At least one of ain or aout is required to be non-null.");
        }
        long[] in_ids;
        if (ains != null) {
            in_ids = new long[ains.length];
            for (int index = 0; index < ains.length; ++index) {
                in_ids[index] = ains[index].getID(mRS);
            }
        } else {
            in_ids = null;
        }
        long out_id = 0;
        if (aout != null) {
            out_id = aout.getID(mRS);
        }
        byte[] params = null;
        if (v != null) {
            params = v.getData();
        }
        int[] limits = null;
        if (sc != null) {
            limits = new int[6];
            limits[0] = sc.xstart;
            limits[1] = sc.xend;
            limits[2] = sc.ystart;
            limits[3] = sc.yend;
            limits[4] = sc.zstart;
            limits[5] = sc.zend;
        }
        mRS.nScriptForEach(getID(mRS), slot, in_ids, out_id, params, limits);
    }

    /**
     * Only intended for use by generated reflected code.  (General reduction)
     */
    protected void reduce(int slot, android.renderscript.Allocation[] ains, android.renderscript.Allocation aout, android.renderscript.Script.LaunchOptions sc) {
        mRS.validate();
        if ((ains == null) || (ains.length < 1)) {
            throw new android.renderscript.RSIllegalArgumentException("At least one input is required.");
        }
        if (aout == null) {
            throw new android.renderscript.RSIllegalArgumentException("aout is required to be non-null.");
        }
        for (android.renderscript.Allocation ain : ains) {
            mRS.validateObject(ain);
        }
        long[] in_ids = new long[ains.length];
        for (int index = 0; index < ains.length; ++index) {
            in_ids[index] = ains[index].getID(mRS);
        }
        long out_id = aout.getID(mRS);
        int[] limits = null;
        if (sc != null) {
            limits = new int[6];
            limits[0] = sc.xstart;
            limits[1] = sc.xend;
            limits[2] = sc.ystart;
            limits[3] = sc.yend;
            limits[4] = sc.zstart;
            limits[5] = sc.zend;
        }
        mRS.nScriptReduce(getID(mRS), slot, in_ids, out_id, limits);
    }

    long[] mInIdsBuffer;

    Script(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
        mInIdsBuffer = new long[1];
        /* The constructors for the derived classes (including ScriptIntrinsic
        derived classes and ScriptC derived classes generated by Slang
        reflection) seem to be simple enough, so we just put the guard.open()
        call here, rather than in the end of the constructor for the derived
        class. This, of course, assumes the derived constructor would not
        throw any exception after calling this constructor.

        If new derived classes are added with more complicated constructors
        that throw exceptions, this call has to be (duplicated and) moved
        to the end of each derived class constructor.
         */
        guard.open("destroy");
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void bindAllocation(android.renderscript.Allocation va, int slot) {
        mRS.validate();
        mRS.validateObject(va);
        if (va != null) {
            android.content.Context context = mRS.getApplicationContext();
            if (context.getApplicationInfo().targetSdkVersion >= 20) {
                final android.renderscript.Type t = va.mType;
                if (((t.hasMipmaps() || t.hasFaces()) || (t.getY() != 0)) || (t.getZ() != 0)) {
                    throw new android.renderscript.RSIllegalArgumentException("API 20+ only allows simple 1D allocations to be " + "used with bind.");
                }
            }
            mRS.nScriptBindAllocation(getID(mRS), va.getID(mRS), slot);
        } else {
            mRS.nScriptBindAllocation(getID(mRS), 0, slot);
        }
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, float v) {
        mRS.nScriptSetVarF(getID(mRS), index, v);
    }

    public float getVarF(int index) {
        return mRS.nScriptGetVarF(getID(mRS), index);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, double v) {
        mRS.nScriptSetVarD(getID(mRS), index, v);
    }

    public double getVarD(int index) {
        return mRS.nScriptGetVarD(getID(mRS), index);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, int v) {
        mRS.nScriptSetVarI(getID(mRS), index, v);
    }

    public int getVarI(int index) {
        return mRS.nScriptGetVarI(getID(mRS), index);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, long v) {
        mRS.nScriptSetVarJ(getID(mRS), index, v);
    }

    public long getVarJ(int index) {
        return mRS.nScriptGetVarJ(getID(mRS), index);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, boolean v) {
        mRS.nScriptSetVarI(getID(mRS), index, v ? 1 : 0);
    }

    public boolean getVarB(int index) {
        return mRS.nScriptGetVarI(getID(mRS), index) > 0 ? true : false;
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, android.renderscript.BaseObj o) {
        mRS.validate();
        mRS.validateObject(o);
        mRS.nScriptSetVarObj(getID(mRS), index, o == null ? 0 : o.getID(mRS));
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, android.renderscript.FieldPacker v) {
        mRS.nScriptSetVarV(getID(mRS), index, v.getData());
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void setVar(int index, android.renderscript.FieldPacker v, android.renderscript.Element e, int[] dims) {
        mRS.nScriptSetVarVE(getID(mRS), index, v.getData(), e.getID(mRS), dims);
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public void getVarV(int index, android.renderscript.FieldPacker v) {
        mRS.nScriptGetVarV(getID(mRS), index, v.getData());
    }

    public void setTimeZone(java.lang.String timeZone) {
        mRS.validate();
        try {
            mRS.nScriptSetTimeZone(getID(mRS), timeZone.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public static class Builder {
        android.renderscript.RenderScript mRS;

        Builder(android.renderscript.RenderScript rs) {
            mRS = rs;
        }
    }

    /**
     * Only intended for use by generated reflected code.
     */
    public static class FieldBase {
        protected android.renderscript.Element mElement;

        protected android.renderscript.Allocation mAllocation;

        protected void init(android.renderscript.RenderScript rs, int dimx) {
            mAllocation = android.renderscript.Allocation.createSized(rs, mElement, dimx, android.renderscript.Allocation.USAGE_SCRIPT);
        }

        protected void init(android.renderscript.RenderScript rs, int dimx, int usages) {
            mAllocation = android.renderscript.Allocation.createSized(rs, mElement, dimx, android.renderscript.Allocation.USAGE_SCRIPT | usages);
        }

        protected FieldBase() {
        }

        public android.renderscript.Element getElement() {
            return mElement;
        }

        public android.renderscript.Type getType() {
            return mAllocation.getType();
        }

        public android.renderscript.Allocation getAllocation() {
            return mAllocation;
        }

        // @Override
        public void updateAllocation() {
        }
    }

    /**
     * Class for specifying the specifics about how a kernel will be
     * launched.
     *
     * This class can specify a potential range of cells on which to
     * run a kernel.  If no set is called for a dimension then this
     * class will have no impact on that dimension when the kernel
     * is executed.
     *
     * The forEach kernel launch will operate over the intersection of
     * the dimensions.
     *
     * Example:
     * LaunchOptions with setX(5, 15)
     * Allocation with dimension X=10, Y=10
     * The resulting forEach run would execute over:
     * x = 5 to 9 (inclusive) and
     * y = 0 to 9 (inclusive).
     */
    public static final class LaunchOptions {
        private int xstart = 0;

        private int ystart = 0;

        private int xend = 0;

        private int yend = 0;

        private int zstart = 0;

        private int zend = 0;

        private int strategy;

        /**
         * Set the X range. xstartArg is the lowest coordinate of the range,
         * and xendArg-1 is the highest coordinate of the range.
         *
         * @param xstartArg
         * 		Must be >= 0
         * @param xendArg
         * 		Must be > xstartArg
         * @return LaunchOptions
         */
        public android.renderscript.Script.LaunchOptions setX(int xstartArg, int xendArg) {
            if ((xstartArg < 0) || (xendArg <= xstartArg)) {
                throw new android.renderscript.RSIllegalArgumentException("Invalid dimensions");
            }
            xstart = xstartArg;
            xend = xendArg;
            return this;
        }

        /**
         * Set the Y range. ystartArg is the lowest coordinate of the range,
         * and yendArg-1 is the highest coordinate of the range.
         *
         * @param ystartArg
         * 		Must be >= 0
         * @param yendArg
         * 		Must be > ystartArg
         * @return LaunchOptions
         */
        public android.renderscript.Script.LaunchOptions setY(int ystartArg, int yendArg) {
            if ((ystartArg < 0) || (yendArg <= ystartArg)) {
                throw new android.renderscript.RSIllegalArgumentException("Invalid dimensions");
            }
            ystart = ystartArg;
            yend = yendArg;
            return this;
        }

        /**
         * Set the Z range. zstartArg is the lowest coordinate of the range,
         * and zendArg-1 is the highest coordinate of the range.
         *
         * @param zstartArg
         * 		Must be >= 0
         * @param zendArg
         * 		Must be > zstartArg
         * @return LaunchOptions
         */
        public android.renderscript.Script.LaunchOptions setZ(int zstartArg, int zendArg) {
            if ((zstartArg < 0) || (zendArg <= zstartArg)) {
                throw new android.renderscript.RSIllegalArgumentException("Invalid dimensions");
            }
            zstart = zstartArg;
            zend = zendArg;
            return this;
        }

        /**
         * Returns the current X start
         *
         * @return int current value
         */
        public int getXStart() {
            return xstart;
        }

        /**
         * Returns the current X end
         *
         * @return int current value
         */
        public int getXEnd() {
            return xend;
        }

        /**
         * Returns the current Y start
         *
         * @return int current value
         */
        public int getYStart() {
            return ystart;
        }

        /**
         * Returns the current Y end
         *
         * @return int current value
         */
        public int getYEnd() {
            return yend;
        }

        /**
         * Returns the current Z start
         *
         * @return int current value
         */
        public int getZStart() {
            return zstart;
        }

        /**
         * Returns the current Z end
         *
         * @return int current value
         */
        public int getZEnd() {
            return zend;
        }
    }
}

