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
 * A group of kernels that are executed
 * together with one execution call as if they were a single kernel
 * <p>
 * In addition to kernels, a script group may contain invocable functions as well.
 * A script group may take inputs and generate outputs, which are consumed and
 * produced by its member kernels.
 * Inside a script group, outputs from one kernel can be passed to another kernel as inputs.
 * The API disallows cyclic dependencies among kernels in a script group,
 * effectively making it a directed acyclic graph (DAG) of kernels.
 * <p>
 * Grouping kernels together allows for more efficient execution. For example,
 * runtime and compiler optimization can be applied to reduce computation and
 * communication overhead, and to make better use of the CPU and the GPU.
 */
public final class ScriptGroup extends android.renderscript.BaseObj {
    private static final java.lang.String TAG = "ScriptGroup";

    android.renderscript.ScriptGroup.IO[] mOutputs;

    android.renderscript.ScriptGroup.IO[] mInputs;

    static class IO {
        android.renderscript.Script.KernelID mKID;

        android.renderscript.Allocation mAllocation;

        IO(android.renderscript.Script.KernelID s) {
            mKID = s;
        }
    }

    static class ConnectLine {
        ConnectLine(android.renderscript.Type t, android.renderscript.Script.KernelID from, android.renderscript.Script.KernelID to) {
            mFrom = from;
            mToK = to;
            mAllocationType = t;
        }

        ConnectLine(android.renderscript.Type t, android.renderscript.Script.KernelID from, android.renderscript.Script.FieldID to) {
            mFrom = from;
            mToF = to;
            mAllocationType = t;
        }

        android.renderscript.Script.FieldID mToF;

        android.renderscript.Script.KernelID mToK;

        android.renderscript.Script.KernelID mFrom;

        android.renderscript.Type mAllocationType;
    }

    static class Node {
        android.renderscript.Script mScript;

        java.util.ArrayList<android.renderscript.Script.KernelID> mKernels = new java.util.ArrayList<android.renderscript.Script.KernelID>();

        java.util.ArrayList<android.renderscript.ScriptGroup.ConnectLine> mInputs = new java.util.ArrayList<android.renderscript.ScriptGroup.ConnectLine>();

        java.util.ArrayList<android.renderscript.ScriptGroup.ConnectLine> mOutputs = new java.util.ArrayList<android.renderscript.ScriptGroup.ConnectLine>();

        int dagNumber;

        android.renderscript.ScriptGroup.Node mNext;

        Node(android.renderscript.Script s) {
            mScript = s;
        }
    }

    /**
     * An opaque class for closures
     * <p>
     * A closure represents a function call to a kernel or invocable function,
     * combined with arguments and values for global variables. A closure is
     * created using the {@link android.renderscript.ScriptGroup.Builder2#addKernel} or
     * {@link android.renderscript.ScriptGroup.Builder2#addInvoke}
     * method.
     */
    public static final class Closure extends android.renderscript.BaseObj {
        private java.lang.Object[] mArgs;

        private android.renderscript.Allocation mReturnValue;

        private java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> mBindings;

        private android.renderscript.ScriptGroup.Future mReturnFuture;

        private java.util.Map<android.renderscript.Script.FieldID, android.renderscript.ScriptGroup.Future> mGlobalFuture;

        private android.renderscript.FieldPacker mFP;

        private static final java.lang.String TAG = "Closure";

        Closure(long id, android.renderscript.RenderScript rs) {
            super(id, rs);
        }

        Closure(android.renderscript.RenderScript rs, android.renderscript.Script.KernelID kernelID, android.renderscript.Type returnType, java.lang.Object[] args, java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> globals) {
            super(0, rs);
            mArgs = args;
            mReturnValue = android.renderscript.Allocation.createTyped(rs, returnType);
            mBindings = globals;
            mGlobalFuture = new java.util.HashMap<android.renderscript.Script.FieldID, android.renderscript.ScriptGroup.Future>();
            int numValues = args.length + globals.size();
            long[] fieldIDs = new long[numValues];
            long[] values = new long[numValues];
            int[] sizes = new int[numValues];
            long[] depClosures = new long[numValues];
            long[] depFieldIDs = new long[numValues];
            int i;
            for (i = 0; i < args.length; i++) {
                fieldIDs[i] = 0;
                retrieveValueAndDependenceInfo(rs, i, null, args[i], values, sizes, depClosures, depFieldIDs);
            }
            for (java.util.Map.Entry<android.renderscript.Script.FieldID, java.lang.Object> entry : globals.entrySet()) {
                java.lang.Object obj = entry.getValue();
                android.renderscript.Script.FieldID fieldID = entry.getKey();
                fieldIDs[i] = fieldID.getID(rs);
                retrieveValueAndDependenceInfo(rs, i, fieldID, obj, values, sizes, depClosures, depFieldIDs);
                i++;
            }
            long id = rs.nClosureCreate(kernelID.getID(rs), mReturnValue.getID(rs), fieldIDs, values, sizes, depClosures, depFieldIDs);
            setID(id);
            guard.open("destroy");
        }

        Closure(android.renderscript.RenderScript rs, android.renderscript.Script.InvokeID invokeID, java.lang.Object[] args, java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> globals) {
            super(0, rs);
            mFP = android.renderscript.FieldPacker.createFromArray(args);
            mArgs = args;
            mBindings = globals;
            mGlobalFuture = new java.util.HashMap<android.renderscript.Script.FieldID, android.renderscript.ScriptGroup.Future>();
            int numValues = globals.size();
            long[] fieldIDs = new long[numValues];
            long[] values = new long[numValues];
            int[] sizes = new int[numValues];
            long[] depClosures = new long[numValues];
            long[] depFieldIDs = new long[numValues];
            int i = 0;
            for (java.util.Map.Entry<android.renderscript.Script.FieldID, java.lang.Object> entry : globals.entrySet()) {
                java.lang.Object obj = entry.getValue();
                android.renderscript.Script.FieldID fieldID = entry.getKey();
                fieldIDs[i] = fieldID.getID(rs);
                retrieveValueAndDependenceInfo(rs, i, fieldID, obj, values, sizes, depClosures, depFieldIDs);
                i++;
            }
            long id = rs.nInvokeClosureCreate(invokeID.getID(rs), mFP.getData(), fieldIDs, values, sizes);
            setID(id);
            guard.open("destroy");
        }

        /**
         * Destroys this Closure and the Allocation for its return value
         */
        public void destroy() {
            super.destroy();
            if (mReturnValue != null) {
                mReturnValue.destroy();
            }
        }

        protected void finalize() throws java.lang.Throwable {
            // Set null mReturnValue to avoid double-destroying it, in case its
            // finalizer races ahead.
            mReturnValue = null;
            super.finalize();
        }

        private void retrieveValueAndDependenceInfo(android.renderscript.RenderScript rs, int index, android.renderscript.Script.FieldID fid, java.lang.Object obj, long[] values, int[] sizes, long[] depClosures, long[] depFieldIDs) {
            if (obj instanceof android.renderscript.ScriptGroup.Future) {
                android.renderscript.ScriptGroup.Future f = ((android.renderscript.ScriptGroup.Future) (obj));
                obj = f.getValue();
                depClosures[index] = f.getClosure().getID(rs);
                android.renderscript.Script.FieldID fieldID = f.getFieldID();
                depFieldIDs[index] = (fieldID != null) ? fieldID.getID(rs) : 0;
            } else {
                depClosures[index] = 0;
                depFieldIDs[index] = 0;
            }
            if (obj instanceof android.renderscript.ScriptGroup.Input) {
                android.renderscript.ScriptGroup.Input unbound = ((android.renderscript.ScriptGroup.Input) (obj));
                if (index < mArgs.length) {
                    unbound.addReference(this, index);
                } else {
                    unbound.addReference(this, fid);
                }
                values[index] = 0;
                sizes[index] = 0;
            } else {
                android.renderscript.ScriptGroup.Closure.ValueAndSize vs = new android.renderscript.ScriptGroup.Closure.ValueAndSize(rs, obj);
                values[index] = vs.value;
                sizes[index] = vs.size;
            }
        }

        /**
         * Returns the future for the return value
         *
         * @return a future
         */
        public android.renderscript.ScriptGroup.Future getReturn() {
            if (mReturnFuture == null) {
                mReturnFuture = new android.renderscript.ScriptGroup.Future(this, null, mReturnValue);
            }
            return mReturnFuture;
        }

        /**
         * Returns the future for a global variable
         *
         * @param field
         * 		the field ID for the global variable
         * @return a future
         */
        public android.renderscript.ScriptGroup.Future getGlobal(android.renderscript.Script.FieldID field) {
            android.renderscript.ScriptGroup.Future f = mGlobalFuture.get(field);
            if (f == null) {
                // If the field is not bound to this closure, this will return a future
                // without an associated value (reference). So this is not working for
                // cross-module (cross-script) linking in this case where a field not
                // explicitly bound.
                java.lang.Object obj = mBindings.get(field);
                if (obj instanceof android.renderscript.ScriptGroup.Future) {
                    obj = ((android.renderscript.ScriptGroup.Future) (obj)).getValue();
                }
                f = new android.renderscript.ScriptGroup.Future(this, field, obj);
                mGlobalFuture.put(field, f);
            }
            return f;
        }

        void setArg(int index, java.lang.Object obj) {
            if (obj instanceof android.renderscript.ScriptGroup.Future) {
                obj = ((android.renderscript.ScriptGroup.Future) (obj)).getValue();
            }
            mArgs[index] = obj;
            android.renderscript.ScriptGroup.Closure.ValueAndSize vs = new android.renderscript.ScriptGroup.Closure.ValueAndSize(mRS, obj);
            mRS.nClosureSetArg(getID(mRS), index, vs.value, vs.size);
        }

        void setGlobal(android.renderscript.Script.FieldID fieldID, java.lang.Object obj) {
            if (obj instanceof android.renderscript.ScriptGroup.Future) {
                obj = ((android.renderscript.ScriptGroup.Future) (obj)).getValue();
            }
            mBindings.put(fieldID, obj);
            android.renderscript.ScriptGroup.Closure.ValueAndSize vs = new android.renderscript.ScriptGroup.Closure.ValueAndSize(mRS, obj);
            mRS.nClosureSetGlobal(getID(mRS), fieldID.getID(mRS), vs.value, vs.size);
        }

        private static final class ValueAndSize {
            public ValueAndSize(android.renderscript.RenderScript rs, java.lang.Object obj) {
                if (obj instanceof android.renderscript.Allocation) {
                    value = ((android.renderscript.Allocation) (obj)).getID(rs);
                    // Special value for size to tell the runtime and driver that
                    // the value is an Allocation
                    size = -1;
                } else
                    if (obj instanceof java.lang.Boolean) {
                        value = (((java.lang.Boolean) (obj)).booleanValue()) ? 1 : 0;
                        size = 4;
                    } else
                        if (obj instanceof java.lang.Integer) {
                            value = ((java.lang.Integer) (obj)).longValue();
                            size = 4;
                        } else
                            if (obj instanceof java.lang.Long) {
                                value = ((java.lang.Long) (obj)).longValue();
                                size = 8;
                            } else
                                if (obj instanceof java.lang.Float) {
                                    value = java.lang.Float.floatToRawIntBits(((java.lang.Float) (obj)).floatValue());
                                    size = 4;
                                } else
                                    if (obj instanceof java.lang.Double) {
                                        value = java.lang.Double.doubleToRawLongBits(((java.lang.Double) (obj)).doubleValue());
                                        size = 8;
                                    }





            }

            public long value;

            public int size;
        }
    }

    /**
     * An opaque class for futures
     * <p>
     * A future represents an output of a closure, either the return value of
     * the function, or the value of a global variable written by the function.
     * A future is created by calling the {@link Closure#getReturn}  or
     * {@link Closure#getGlobal} method.
     */
    public static final class Future {
        android.renderscript.ScriptGroup.Closure mClosure;

        android.renderscript.Script.FieldID mFieldID;

        java.lang.Object mValue;

        Future(android.renderscript.ScriptGroup.Closure closure, android.renderscript.Script.FieldID fieldID, java.lang.Object value) {
            mClosure = closure;
            mFieldID = fieldID;
            mValue = value;
        }

        android.renderscript.ScriptGroup.Closure getClosure() {
            return mClosure;
        }

        android.renderscript.Script.FieldID getFieldID() {
            return mFieldID;
        }

        java.lang.Object getValue() {
            return mValue;
        }
    }

    /**
     * An opaque class for script group inputs
     * <p>
     * Created by calling the {@link Builder2#addInput} method. The value
     * is assigned in {@link ScriptGroup#execute(Object...)} method as
     * one of its arguments. Arguments to the execute method should be in
     * the same order as intputs are added using the addInput method.
     */
    public static final class Input {
        // Either mFieldID or mArgIndex should be set but not both.
        java.util.List<android.util.Pair<android.renderscript.ScriptGroup.Closure, android.renderscript.Script.FieldID>> mFieldID;

        // -1 means unset. Legal values are 0 .. n-1, where n is the number of
        // arguments for the referencing closure.
        java.util.List<android.util.Pair<android.renderscript.ScriptGroup.Closure, java.lang.Integer>> mArgIndex;

        java.lang.Object mValue;

        Input() {
            mFieldID = new java.util.ArrayList<android.util.Pair<android.renderscript.ScriptGroup.Closure, android.renderscript.Script.FieldID>>();
            mArgIndex = new java.util.ArrayList<android.util.Pair<android.renderscript.ScriptGroup.Closure, java.lang.Integer>>();
        }

        void addReference(android.renderscript.ScriptGroup.Closure closure, int index) {
            mArgIndex.add(android.util.Pair.create(closure, java.lang.Integer.valueOf(index)));
        }

        void addReference(android.renderscript.ScriptGroup.Closure closure, android.renderscript.Script.FieldID fieldID) {
            mFieldID.add(android.util.Pair.create(closure, fieldID));
        }

        void set(java.lang.Object value) {
            mValue = value;
            for (android.util.Pair<android.renderscript.ScriptGroup.Closure, java.lang.Integer> p : mArgIndex) {
                android.renderscript.ScriptGroup.Closure closure = p.first;
                int index = p.second.intValue();
                closure.setArg(index, value);
            }
            for (android.util.Pair<android.renderscript.ScriptGroup.Closure, android.renderscript.Script.FieldID> p : mFieldID) {
                android.renderscript.ScriptGroup.Closure closure = p.first;
                android.renderscript.Script.FieldID fieldID = p.second;
                closure.setGlobal(fieldID, value);
            }
        }

        java.lang.Object get() {
            return mValue;
        }
    }

    private java.lang.String mName;

    private java.util.List<android.renderscript.ScriptGroup.Closure> mClosures;

    private java.util.List<android.renderscript.ScriptGroup.Input> mInputs2;

    private android.renderscript.ScriptGroup.Future[] mOutputs2;

    ScriptGroup(long id, android.renderscript.RenderScript rs) {
        super(id, rs);
        guard.open("destroy");
    }

    ScriptGroup(android.renderscript.RenderScript rs, java.lang.String name, java.util.List<android.renderscript.ScriptGroup.Closure> closures, java.util.List<android.renderscript.ScriptGroup.Input> inputs, android.renderscript.ScriptGroup.Future[] outputs) {
        super(0, rs);
        mName = name;
        mClosures = closures;
        mInputs2 = inputs;
        mOutputs2 = outputs;
        long[] closureIDs = new long[closures.size()];
        for (int i = 0; i < closureIDs.length; i++) {
            closureIDs[i] = closures.get(i).getID(rs);
        }
        long id = rs.nScriptGroup2Create(name, android.renderscript.RenderScript.getCachePath(), closureIDs);
        setID(id);
        guard.open("destroy");
    }

    /**
     * Executes a script group
     *
     * @param inputs
     * 		Values for inputs to the script group, in the order as the
     * 		inputs are added via {@link Builder2#addInput}.
     * @return Outputs of the script group as an array of objects, in the order
    as futures are passed to {@link Builder2#create}.
     */
    public java.lang.Object[] execute(java.lang.Object... inputs) {
        if (inputs.length < mInputs2.size()) {
            android.util.Log.e(android.renderscript.ScriptGroup.TAG, ((((this.toString() + " receives ") + inputs.length) + " inputs, ") + "less than expected ") + mInputs2.size());
            return null;
        }
        if (inputs.length > mInputs2.size()) {
            android.util.Log.i(android.renderscript.ScriptGroup.TAG, ((((this.toString() + " receives ") + inputs.length) + " inputs, ") + "more than expected ") + mInputs2.size());
        }
        for (int i = 0; i < mInputs2.size(); i++) {
            java.lang.Object obj = inputs[i];
            if ((obj instanceof android.renderscript.ScriptGroup.Future) || (obj instanceof android.renderscript.ScriptGroup.Input)) {
                android.util.Log.e(android.renderscript.ScriptGroup.TAG, ((this.toString() + ": input ") + i) + " is a future or unbound value");
                return null;
            }
            android.renderscript.ScriptGroup.Input unbound = mInputs2.get(i);
            unbound.set(obj);
        }
        mRS.nScriptGroup2Execute(getID(mRS));
        java.lang.Object[] outputObjs = new java.lang.Object[mOutputs2.length];
        int i = 0;
        for (android.renderscript.ScriptGroup.Future f : mOutputs2) {
            java.lang.Object output = f.getValue();
            if (output instanceof android.renderscript.ScriptGroup.Input) {
                output = ((android.renderscript.ScriptGroup.Input) (output)).get();
            }
            outputObjs[i++] = output;
        }
        return outputObjs;
    }

    /**
     * Sets an input of the ScriptGroup. This specifies an
     * Allocation to be used for kernels that require an input
     * Allocation provided from outside of the ScriptGroup.
     *
     * @deprecated Set arguments to {@link #execute(Object...)} instead.
     * @param s
     * 		The ID of the kernel where the allocation should be
     * 		connected.
     * @param a
     * 		The allocation to connect.
     */
    public void setInput(android.renderscript.Script.KernelID s, android.renderscript.Allocation a) {
        for (int ct = 0; ct < mInputs.length; ct++) {
            if (mInputs[ct].mKID == s) {
                mInputs[ct].mAllocation = a;
                mRS.nScriptGroupSetInput(getID(mRS), s.getID(mRS), mRS.safeID(a));
                return;
            }
        }
        throw new android.renderscript.RSIllegalArgumentException("Script not found");
    }

    /**
     * Sets an output of the ScriptGroup. This specifies an
     * Allocation to be used for the kernels that require an output
     * Allocation visible after the ScriptGroup is executed.
     *
     * @deprecated Use return value of {@link #execute(Object...)} instead.
     * @param s
     * 		The ID of the kernel where the allocation should be
     * 		connected.
     * @param a
     * 		The allocation to connect.
     */
    public void setOutput(android.renderscript.Script.KernelID s, android.renderscript.Allocation a) {
        for (int ct = 0; ct < mOutputs.length; ct++) {
            if (mOutputs[ct].mKID == s) {
                mOutputs[ct].mAllocation = a;
                mRS.nScriptGroupSetOutput(getID(mRS), s.getID(mRS), mRS.safeID(a));
                return;
            }
        }
        throw new android.renderscript.RSIllegalArgumentException("Script not found");
    }

    /**
     * Execute the ScriptGroup.  This will run all the kernels in
     * the ScriptGroup.  No internal connection results will be visible
     * after execution of the ScriptGroup.
     *
     * @deprecated Use {@link #execute} instead.
     */
    public void execute() {
        mRS.nScriptGroupExecute(getID(mRS));
    }

    /**
     * Helper class to build a ScriptGroup. A ScriptGroup is
     * created in two steps.
     * <p>
     * First, all kernels to be used by the ScriptGroup should be added.
     * <p>
     * Second, add connections between kernels. There are two types
     * of connections: kernel to kernel and kernel to field.
     * Kernel to kernel allows a kernel's output to be passed to
     * another kernel as input. Kernel to field allows the output of
     * one kernel to be bound as a script global. Kernel to kernel is
     * higher performance and should be used where possible.
     * <p>
     * A ScriptGroup must contain a single directed acyclic graph (DAG); it
     * cannot contain cycles. Currently, all kernels used in a ScriptGroup
     * must come from different Script objects. Additionally, all kernels
     * in a ScriptGroup must have at least one input, output, or internal
     * connection.
     * <p>
     * Once all connections are made, a call to {@link #create} will
     * return the ScriptGroup object.
     *
     * @deprecated Use {@link Builder2} instead.
     */
    public static final class Builder {
        private android.renderscript.RenderScript mRS;

        private java.util.ArrayList<android.renderscript.ScriptGroup.Node> mNodes = new java.util.ArrayList<android.renderscript.ScriptGroup.Node>();

        private java.util.ArrayList<android.renderscript.ScriptGroup.ConnectLine> mLines = new java.util.ArrayList<android.renderscript.ScriptGroup.ConnectLine>();

        private int mKernelCount;

        /**
         * Create a Builder for generating a ScriptGroup.
         *
         * @param rs
         * 		The RenderScript context.
         */
        public Builder(android.renderscript.RenderScript rs) {
            mRS = rs;
        }

        // do a DFS from original node, looking for original node
        // any cycle that could be created must contain original node
        private void validateCycle(android.renderscript.ScriptGroup.Node target, android.renderscript.ScriptGroup.Node original) {
            for (int ct = 0; ct < target.mOutputs.size(); ct++) {
                final android.renderscript.ScriptGroup.ConnectLine cl = target.mOutputs.get(ct);
                if (cl.mToK != null) {
                    android.renderscript.ScriptGroup.Node tn = findNode(cl.mToK.mScript);
                    if (tn.equals(original)) {
                        throw new android.renderscript.RSInvalidStateException("Loops in group not allowed.");
                    }
                    validateCycle(tn, original);
                }
                if (cl.mToF != null) {
                    android.renderscript.ScriptGroup.Node tn = findNode(cl.mToF.mScript);
                    if (tn.equals(original)) {
                        throw new android.renderscript.RSInvalidStateException("Loops in group not allowed.");
                    }
                    validateCycle(tn, original);
                }
            }
        }

        private void mergeDAGs(int valueUsed, int valueKilled) {
            for (int ct = 0; ct < mNodes.size(); ct++) {
                if (mNodes.get(ct).dagNumber == valueKilled)
                    mNodes.get(ct).dagNumber = valueUsed;

            }
        }

        private void validateDAGRecurse(android.renderscript.ScriptGroup.Node n, int dagNumber) {
            // combine DAGs if this node has been seen already
            if ((n.dagNumber != 0) && (n.dagNumber != dagNumber)) {
                mergeDAGs(n.dagNumber, dagNumber);
                return;
            }
            n.dagNumber = dagNumber;
            for (int ct = 0; ct < n.mOutputs.size(); ct++) {
                final android.renderscript.ScriptGroup.ConnectLine cl = n.mOutputs.get(ct);
                if (cl.mToK != null) {
                    android.renderscript.ScriptGroup.Node tn = findNode(cl.mToK.mScript);
                    validateDAGRecurse(tn, dagNumber);
                }
                if (cl.mToF != null) {
                    android.renderscript.ScriptGroup.Node tn = findNode(cl.mToF.mScript);
                    validateDAGRecurse(tn, dagNumber);
                }
            }
        }

        private void validateDAG() {
            for (int ct = 0; ct < mNodes.size(); ct++) {
                android.renderscript.ScriptGroup.Node n = mNodes.get(ct);
                if (n.mInputs.size() == 0) {
                    if ((n.mOutputs.size() == 0) && (mNodes.size() > 1)) {
                        java.lang.String msg = "Groups cannot contain unconnected scripts";
                        throw new android.renderscript.RSInvalidStateException(msg);
                    }
                    validateDAGRecurse(n, ct + 1);
                }
            }
            int dagNumber = mNodes.get(0).dagNumber;
            for (int ct = 0; ct < mNodes.size(); ct++) {
                if (mNodes.get(ct).dagNumber != dagNumber) {
                    throw new android.renderscript.RSInvalidStateException("Multiple DAGs in group not allowed.");
                }
            }
        }

        private android.renderscript.ScriptGroup.Node findNode(android.renderscript.Script s) {
            for (int ct = 0; ct < mNodes.size(); ct++) {
                if (s == mNodes.get(ct).mScript) {
                    return mNodes.get(ct);
                }
            }
            return null;
        }

        private android.renderscript.ScriptGroup.Node findNode(android.renderscript.Script.KernelID k) {
            for (int ct = 0; ct < mNodes.size(); ct++) {
                android.renderscript.ScriptGroup.Node n = mNodes.get(ct);
                for (int ct2 = 0; ct2 < n.mKernels.size(); ct2++) {
                    if (k == n.mKernels.get(ct2)) {
                        return n;
                    }
                }
            }
            return null;
        }

        /**
         * Adds a Kernel to the group.
         *
         * @param k
         * 		The kernel to add.
         * @return Builder Returns this.
         */
        public android.renderscript.ScriptGroup.Builder addKernel(android.renderscript.Script.KernelID k) {
            if (mLines.size() != 0) {
                throw new android.renderscript.RSInvalidStateException("Kernels may not be added once connections exist.");
            }
            // android.util.Log.v("RSR", "addKernel 1 k=" + k);
            if (findNode(k) != null) {
                return this;
            }
            // android.util.Log.v("RSR", "addKernel 2 ");
            mKernelCount++;
            android.renderscript.ScriptGroup.Node n = findNode(k.mScript);
            if (n == null) {
                // android.util.Log.v("RSR", "addKernel 3 ");
                n = new android.renderscript.ScriptGroup.Node(k.mScript);
                mNodes.add(n);
            }
            n.mKernels.add(k);
            return this;
        }

        /**
         * Adds a connection to the group.
         *
         * @param t
         * 		The type of the connection. This is used to
         * 		determine the kernel launch sizes on the source side
         * 		of this connection.
         * @param from
         * 		The source for the connection.
         * @param to
         * 		The destination of the connection.
         * @return Builder Returns this
         */
        public android.renderscript.ScriptGroup.Builder addConnection(android.renderscript.Type t, android.renderscript.Script.KernelID from, android.renderscript.Script.FieldID to) {
            // android.util.Log.v("RSR", "addConnection " + t +", " + from + ", " + to);
            android.renderscript.ScriptGroup.Node nf = findNode(from);
            if (nf == null) {
                throw new android.renderscript.RSInvalidStateException("From script not found.");
            }
            android.renderscript.ScriptGroup.Node nt = findNode(to.mScript);
            if (nt == null) {
                throw new android.renderscript.RSInvalidStateException("To script not found.");
            }
            android.renderscript.ScriptGroup.ConnectLine cl = new android.renderscript.ScriptGroup.ConnectLine(t, from, to);
            mLines.add(new android.renderscript.ScriptGroup.ConnectLine(t, from, to));
            nf.mOutputs.add(cl);
            nt.mInputs.add(cl);
            validateCycle(nf, nf);
            return this;
        }

        /**
         * Adds a connection to the group.
         *
         * @param t
         * 		The type of the connection. This is used to
         * 		determine the kernel launch sizes for both sides of
         * 		this connection.
         * @param from
         * 		The source for the connection.
         * @param to
         * 		The destination of the connection.
         * @return Builder Returns this
         */
        public android.renderscript.ScriptGroup.Builder addConnection(android.renderscript.Type t, android.renderscript.Script.KernelID from, android.renderscript.Script.KernelID to) {
            // android.util.Log.v("RSR", "addConnection " + t +", " + from + ", " + to);
            android.renderscript.ScriptGroup.Node nf = findNode(from);
            if (nf == null) {
                throw new android.renderscript.RSInvalidStateException("From script not found.");
            }
            android.renderscript.ScriptGroup.Node nt = findNode(to);
            if (nt == null) {
                throw new android.renderscript.RSInvalidStateException("To script not found.");
            }
            android.renderscript.ScriptGroup.ConnectLine cl = new android.renderscript.ScriptGroup.ConnectLine(t, from, to);
            mLines.add(new android.renderscript.ScriptGroup.ConnectLine(t, from, to));
            nf.mOutputs.add(cl);
            nt.mInputs.add(cl);
            validateCycle(nf, nf);
            return this;
        }

        /**
         * Creates the Script group.
         *
         * @return ScriptGroup The new ScriptGroup
         */
        public android.renderscript.ScriptGroup create() {
            if (mNodes.size() == 0) {
                throw new android.renderscript.RSInvalidStateException("Empty script groups are not allowed");
            }
            // reset DAG numbers in case we're building a second group
            for (int ct = 0; ct < mNodes.size(); ct++) {
                mNodes.get(ct).dagNumber = 0;
            }
            validateDAG();
            java.util.ArrayList<android.renderscript.ScriptGroup.IO> inputs = new java.util.ArrayList<android.renderscript.ScriptGroup.IO>();
            java.util.ArrayList<android.renderscript.ScriptGroup.IO> outputs = new java.util.ArrayList<android.renderscript.ScriptGroup.IO>();
            long[] kernels = new long[mKernelCount];
            int idx = 0;
            for (int ct = 0; ct < mNodes.size(); ct++) {
                android.renderscript.ScriptGroup.Node n = mNodes.get(ct);
                for (int ct2 = 0; ct2 < n.mKernels.size(); ct2++) {
                    final android.renderscript.Script.KernelID kid = n.mKernels.get(ct2);
                    kernels[idx++] = kid.getID(mRS);
                    boolean hasInput = false;
                    boolean hasOutput = false;
                    for (int ct3 = 0; ct3 < n.mInputs.size(); ct3++) {
                        if (n.mInputs.get(ct3).mToK == kid) {
                            hasInput = true;
                        }
                    }
                    for (int ct3 = 0; ct3 < n.mOutputs.size(); ct3++) {
                        if (n.mOutputs.get(ct3).mFrom == kid) {
                            hasOutput = true;
                        }
                    }
                    if (!hasInput) {
                        inputs.add(new android.renderscript.ScriptGroup.IO(kid));
                    }
                    if (!hasOutput) {
                        outputs.add(new android.renderscript.ScriptGroup.IO(kid));
                    }
                }
            }
            if (idx != mKernelCount) {
                throw new android.renderscript.RSRuntimeException("Count mismatch, should not happen.");
            }
            long[] src = new long[mLines.size()];
            long[] dstk = new long[mLines.size()];
            long[] dstf = new long[mLines.size()];
            long[] types = new long[mLines.size()];
            for (int ct = 0; ct < mLines.size(); ct++) {
                android.renderscript.ScriptGroup.ConnectLine cl = mLines.get(ct);
                src[ct] = cl.mFrom.getID(mRS);
                if (cl.mToK != null) {
                    dstk[ct] = cl.mToK.getID(mRS);
                }
                if (cl.mToF != null) {
                    dstf[ct] = cl.mToF.getID(mRS);
                }
                types[ct] = cl.mAllocationType.getID(mRS);
            }
            long id = mRS.nScriptGroupCreate(kernels, src, dstk, dstf, types);
            if (id == 0) {
                throw new android.renderscript.RSRuntimeException("Object creation error, should not happen.");
            }
            android.renderscript.ScriptGroup sg = new android.renderscript.ScriptGroup(id, mRS);
            sg.mOutputs = new android.renderscript.ScriptGroup.IO[outputs.size()];
            for (int ct = 0; ct < outputs.size(); ct++) {
                sg.mOutputs[ct] = outputs.get(ct);
            }
            sg.mInputs = new android.renderscript.ScriptGroup.IO[inputs.size()];
            for (int ct = 0; ct < inputs.size(); ct++) {
                sg.mInputs[ct] = inputs.get(ct);
            }
            return sg;
        }
    }

    /**
     * Represents a binding of a value to a global variable in a
     * kernel or invocable function. Used in closure creation.
     */
    public static final class Binding {
        private final android.renderscript.Script.FieldID mField;

        private final java.lang.Object mValue;

        /**
         * Returns a Binding object that binds value to field
         *
         * @param field
         * 		the Script.FieldID of the global variable
         * @param value
         * 		the value
         */
        public Binding(android.renderscript.Script.FieldID field, java.lang.Object value) {
            mField = field;
            mValue = value;
        }

        /**
         * Returns the field ID
         */
        android.renderscript.Script.FieldID getField() {
            return mField;
        }

        /**
         * Returns the value
         */
        java.lang.Object getValue() {
            return mValue;
        }
    }

    /**
     * The builder class for creating script groups
     * <p>
     * A script group is created using closures (see class {@link Closure}).
     * A closure is a function call to a kernel or
     * invocable function. Each function argument or global variable accessed inside
     * the function is bound to 1) a known value, 2) a script group input
     * (see class {@link Input}), or 3) a
     * future (see class {@link Future}).
     * A future is the output of a closure, either the return value of the
     * function or a global variable written by that function.
     * <p>
     * Closures are created using the {@link #addKernel} or {@link #addInvoke}
     * methods.
     * When a closure is created, futures from previously created closures
     * can be used as its inputs.
     * External script group inputs can be used as inputs to individual closures as well.
     * An external script group input is created using the {@link #addInput} method.
     * A script group is created by a call to the {@link #create} method, which
     * accepts an array of futures as the outputs for the script group.
     * <p>
     * Closures in a script group can be evaluated in any order as long as the
     * following conditions are met:
     * 1) a closure must be evaluated before any other closures that take its
     * futures as inputs;
     * 2) all closures added before an invoke closure must be evaluated
     * before it;
     * and 3) all closures added after an invoke closure must be evaluated after
     * it.
     * As a special case, the order that the closures are added is a legal
     * evaluation order. However, other evaluation orders are possible, including
     * concurrently evaluating independent closures.
     */
    public static final class Builder2 {
        android.renderscript.RenderScript mRS;

        java.util.List<android.renderscript.ScriptGroup.Closure> mClosures;

        java.util.List<android.renderscript.ScriptGroup.Input> mInputs;

        private static final java.lang.String TAG = "ScriptGroup.Builder2";

        /**
         * Returns a Builder object
         *
         * @param rs
         * 		the RenderScript context
         */
        public Builder2(android.renderscript.RenderScript rs) {
            mRS = rs;
            mClosures = new java.util.ArrayList<android.renderscript.ScriptGroup.Closure>();
            mInputs = new java.util.ArrayList<android.renderscript.ScriptGroup.Input>();
        }

        /**
         * Adds a closure for a kernel
         *
         * @param k
         * 		Kernel ID for the kernel function
         * @param returnType
         * 		Allocation type for the return value
         * @param args
         * 		arguments to the kernel function
         * @param globalBindings
         * 		bindings for global variables
         * @return a closure
         */
        private android.renderscript.ScriptGroup.Closure addKernelInternal(android.renderscript.Script.KernelID k, android.renderscript.Type returnType, java.lang.Object[] args, java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> globalBindings) {
            android.renderscript.ScriptGroup.Closure c = new android.renderscript.ScriptGroup.Closure(mRS, k, returnType, args, globalBindings);
            mClosures.add(c);
            return c;
        }

        /**
         * Adds a closure for an invocable function
         *
         * @param invoke
         * 		Invoke ID for the invocable function
         * @param args
         * 		arguments to the invocable function
         * @param globalBindings
         * 		bindings for global variables
         * @return a closure
         */
        private android.renderscript.ScriptGroup.Closure addInvokeInternal(android.renderscript.Script.InvokeID invoke, java.lang.Object[] args, java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> globalBindings) {
            android.renderscript.ScriptGroup.Closure c = new android.renderscript.ScriptGroup.Closure(mRS, invoke, args, globalBindings);
            mClosures.add(c);
            return c;
        }

        /**
         * Adds a script group input
         *
         * @return a script group input, which can be used as an argument or a value to
        a global variable for creating closures
         */
        public android.renderscript.ScriptGroup.Input addInput() {
            android.renderscript.ScriptGroup.Input unbound = new android.renderscript.ScriptGroup.Input();
            mInputs.add(unbound);
            return unbound;
        }

        /**
         * Adds a closure for a kernel
         *
         * @param k
         * 		Kernel ID for the kernel function
         * @param argsAndBindings
         * 		arguments followed by bindings for global variables
         * @return a closure
         */
        public android.renderscript.ScriptGroup.Closure addKernel(android.renderscript.Script.KernelID k, android.renderscript.Type returnType, java.lang.Object... argsAndBindings) {
            java.util.ArrayList<java.lang.Object> args = new java.util.ArrayList<java.lang.Object>();
            java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> bindingMap = new java.util.HashMap<android.renderscript.Script.FieldID, java.lang.Object>();
            if (!seperateArgsAndBindings(argsAndBindings, args, bindingMap)) {
                return null;
            }
            return addKernelInternal(k, returnType, args.toArray(), bindingMap);
        }

        /**
         * Adds a closure for an invocable function
         *
         * @param invoke
         * 		Invoke ID for the invocable function
         * @param argsAndBindings
         * 		arguments followed by bindings for global variables
         * @return a closure
         */
        public android.renderscript.ScriptGroup.Closure addInvoke(android.renderscript.Script.InvokeID invoke, java.lang.Object... argsAndBindings) {
            java.util.ArrayList<java.lang.Object> args = new java.util.ArrayList<java.lang.Object>();
            java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> bindingMap = new java.util.HashMap<android.renderscript.Script.FieldID, java.lang.Object>();
            if (!seperateArgsAndBindings(argsAndBindings, args, bindingMap)) {
                return null;
            }
            return addInvokeInternal(invoke, args.toArray(), bindingMap);
        }

        /**
         * Creates a script group
         *
         * @param name
         * 		name for the script group. Legal names can only contain letters, digits,
         * 		'-', or '_'. The name can be no longer than 100 characters.
         * 		Try to use unique names, to avoid name conflicts and reduce
         * 		the cost of group creation.
         * @param outputs
         * 		futures intended as outputs of the script group
         * @return a script group
         */
        public android.renderscript.ScriptGroup create(java.lang.String name, android.renderscript.ScriptGroup.Future... outputs) {
            if ((((name == null) || name.isEmpty()) || (name.length() > 100)) || (!name.equals(name.replaceAll("[^a-zA-Z0-9-]", "_")))) {
                throw new android.renderscript.RSIllegalArgumentException("invalid script group name");
            }
            android.renderscript.ScriptGroup ret = new android.renderscript.ScriptGroup(mRS, name, mClosures, mInputs, outputs);
            mClosures = new java.util.ArrayList<android.renderscript.ScriptGroup.Closure>();
            mInputs = new java.util.ArrayList<android.renderscript.ScriptGroup.Input>();
            return ret;
        }

        private boolean seperateArgsAndBindings(java.lang.Object[] argsAndBindings, java.util.ArrayList<java.lang.Object> args, java.util.Map<android.renderscript.Script.FieldID, java.lang.Object> bindingMap) {
            int i;
            for (i = 0; i < argsAndBindings.length; i++) {
                if (argsAndBindings[i] instanceof android.renderscript.ScriptGroup.Binding) {
                    break;
                }
                args.add(argsAndBindings[i]);
            }
            for (; i < argsAndBindings.length; i++) {
                if (!(argsAndBindings[i] instanceof android.renderscript.ScriptGroup.Binding)) {
                    return false;
                }
                android.renderscript.ScriptGroup.Binding b = ((android.renderscript.ScriptGroup.Binding) (argsAndBindings[i]));
                bindingMap.put(b.getField(), b.getValue());
            }
            return true;
        }
    }

    /**
     * Destroy this ScriptGroup and all Closures in it
     */
    public void destroy() {
        super.destroy();
        // ScriptGroup created using the old Builder class does not
        // initialize the field mClosures
        if (mClosures != null) {
            for (android.renderscript.ScriptGroup.Closure c : mClosures) {
                c.destroy();
            }
        }
    }
}

