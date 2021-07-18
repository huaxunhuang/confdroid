/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class FilterFunction {
    private android.filterfw.core.Filter mFilter;

    private android.filterfw.core.FilterContext mFilterContext;

    private boolean mFilterIsSetup = false;

    private android.filterfw.core.FilterFunction.FrameHolderPort[] mResultHolders;

    private class FrameHolderPort extends android.filterfw.core.StreamPort {
        public FrameHolderPort() {
            super(null, "holder");
        }
    }

    public FilterFunction(android.filterfw.core.FilterContext context, android.filterfw.core.Filter filter) {
        mFilterContext = context;
        mFilter = filter;
    }

    public android.filterfw.core.Frame execute(android.filterfw.core.KeyValueMap inputMap) {
        int filterOutCount = mFilter.getNumberOfOutputs();
        // Sanity checks
        if (filterOutCount > 1) {
            throw new java.lang.RuntimeException((("Calling execute on filter " + mFilter) + " with multiple ") + "outputs! Use executeMulti() instead!");
        }
        // Setup filter
        if (!mFilterIsSetup) {
            connectFilterOutputs();
            mFilterIsSetup = true;
        }
        // Make sure GL environment is active
        boolean didActivateGLEnv = false;
        android.filterfw.core.GLEnvironment glEnv = mFilterContext.getGLEnvironment();
        if ((glEnv != null) && (!glEnv.isActive())) {
            glEnv.activate();
            didActivateGLEnv = true;
        }
        // Setup the inputs
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : inputMap.entrySet()) {
            if (entry.getValue() instanceof android.filterfw.core.Frame) {
                mFilter.pushInputFrame(entry.getKey(), ((android.filterfw.core.Frame) (entry.getValue())));
            } else {
                mFilter.pushInputValue(entry.getKey(), entry.getValue());
            }
        }
        // Process the filter
        if (mFilter.getStatus() != android.filterfw.core.Filter.STATUS_PROCESSING) {
            mFilter.openOutputs();
        }
        mFilter.performProcess(mFilterContext);
        // Create result handle
        android.filterfw.core.Frame result = null;
        if ((filterOutCount == 1) && mResultHolders[0].hasFrame()) {
            result = mResultHolders[0].pullFrame();
        }
        // Deactivate GL environment if activated
        if (didActivateGLEnv) {
            glEnv.deactivate();
        }
        return result;
    }

    public android.filterfw.core.Frame executeWithArgList(java.lang.Object... inputs) {
        return execute(android.filterfw.core.KeyValueMap.fromKeyValues(inputs));
    }

    public void close() {
        mFilter.performClose(mFilterContext);
    }

    public android.filterfw.core.FilterContext getContext() {
        return mFilterContext;
    }

    public android.filterfw.core.Filter getFilter() {
        return mFilter;
    }

    public void setInputFrame(java.lang.String input, android.filterfw.core.Frame frame) {
        mFilter.setInputFrame(input, frame);
    }

    public void setInputValue(java.lang.String input, java.lang.Object value) {
        mFilter.setInputValue(input, value);
    }

    public void tearDown() {
        mFilter.performTearDown(mFilterContext);
        mFilter = null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mFilter.getName();
    }

    private void connectFilterOutputs() {
        int i = 0;
        mResultHolders = new android.filterfw.core.FilterFunction.FrameHolderPort[mFilter.getNumberOfOutputs()];
        for (android.filterfw.core.OutputPort outputPort : mFilter.getOutputPorts()) {
            mResultHolders[i] = new android.filterfw.core.FilterFunction.FrameHolderPort();
            outputPort.connectTo(mResultHolders[i]);
            ++i;
        }
    }
}

