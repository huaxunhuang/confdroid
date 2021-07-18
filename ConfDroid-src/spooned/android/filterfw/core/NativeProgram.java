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
public class NativeProgram extends android.filterfw.core.Program {
    private int nativeProgramId;

    private boolean mHasInitFunction = false;

    private boolean mHasTeardownFunction = false;

    private boolean mHasSetValueFunction = false;

    private boolean mHasGetValueFunction = false;

    private boolean mHasResetFunction = false;

    private boolean mTornDown = false;

    public NativeProgram(java.lang.String nativeLibName, java.lang.String nativeFunctionPrefix) {
        // Allocate the native instance
        allocate();
        // Open the native library
        java.lang.String fullLibName = ("lib" + nativeLibName) + ".so";
        if (!openNativeLibrary(fullLibName)) {
            throw new java.lang.RuntimeException((("Could not find native library named '" + fullLibName) + "' ") + "required for native program!");
        }
        // Bind the native functions
        java.lang.String processFuncName = nativeFunctionPrefix + "_process";
        if (!bindProcessFunction(processFuncName)) {
            throw new java.lang.RuntimeException((((("Could not find native program function name " + processFuncName) + " in library ") + fullLibName) + "! ") + "This function is required!");
        }
        java.lang.String initFuncName = nativeFunctionPrefix + "_init";
        mHasInitFunction = bindInitFunction(initFuncName);
        java.lang.String teardownFuncName = nativeFunctionPrefix + "_teardown";
        mHasTeardownFunction = bindTeardownFunction(teardownFuncName);
        java.lang.String setValueFuncName = nativeFunctionPrefix + "_setvalue";
        mHasSetValueFunction = bindSetValueFunction(setValueFuncName);
        java.lang.String getValueFuncName = nativeFunctionPrefix + "_getvalue";
        mHasGetValueFunction = bindGetValueFunction(getValueFuncName);
        java.lang.String resetFuncName = nativeFunctionPrefix + "_reset";
        mHasResetFunction = bindResetFunction(resetFuncName);
        // Initialize the native code
        if (mHasInitFunction && (!callNativeInit())) {
            throw new java.lang.RuntimeException("Could not initialize NativeProgram!");
        }
    }

    public void tearDown() {
        if (mTornDown)
            return;

        if (mHasTeardownFunction && (!callNativeTeardown())) {
            throw new java.lang.RuntimeException("Could not tear down NativeProgram!");
        }
        deallocate();
        mTornDown = true;
    }

    @java.lang.Override
    public void reset() {
        if (mHasResetFunction && (!callNativeReset())) {
            throw new java.lang.RuntimeException("Could not reset NativeProgram!");
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        tearDown();
    }

    @java.lang.Override
    public void process(android.filterfw.core.Frame[] inputs, android.filterfw.core.Frame output) {
        if (mTornDown) {
            throw new java.lang.RuntimeException("NativeProgram already torn down!");
        }
        android.filterfw.core.NativeFrame[] nativeInputs = new android.filterfw.core.NativeFrame[inputs.length];
        for (int i = 0; i < inputs.length; ++i) {
            if ((inputs[i] == null) || (inputs[i] instanceof android.filterfw.core.NativeFrame)) {
                nativeInputs[i] = ((android.filterfw.core.NativeFrame) (inputs[i]));
            } else {
                throw new java.lang.RuntimeException(("NativeProgram got non-native frame as input " + i) + "!");
            }
        }
        // Get the native output frame
        android.filterfw.core.NativeFrame nativeOutput = null;
        if ((output == null) || (output instanceof android.filterfw.core.NativeFrame)) {
            nativeOutput = ((android.filterfw.core.NativeFrame) (output));
        } else {
            throw new java.lang.RuntimeException("NativeProgram got non-native output frame!");
        }
        // Process!
        if (!callNativeProcess(nativeInputs, nativeOutput)) {
            throw new java.lang.RuntimeException("Calling native process() caused error!");
        }
    }

    @java.lang.Override
    public void setHostValue(java.lang.String variableName, java.lang.Object value) {
        if (mTornDown) {
            throw new java.lang.RuntimeException("NativeProgram already torn down!");
        }
        if (!mHasSetValueFunction) {
            throw new java.lang.RuntimeException("Attempting to set native variable, but native code does not " + "define native setvalue function!");
        }
        if (!callNativeSetValue(variableName, value.toString())) {
            throw new java.lang.RuntimeException(("Error setting native value for variable '" + variableName) + "'!");
        }
    }

    @java.lang.Override
    public java.lang.Object getHostValue(java.lang.String variableName) {
        if (mTornDown) {
            throw new java.lang.RuntimeException("NativeProgram already torn down!");
        }
        if (!mHasGetValueFunction) {
            throw new java.lang.RuntimeException("Attempting to get native variable, but native code does not " + "define native getvalue function!");
        }
        return callNativeGetValue(variableName);
    }

    static {
        java.lang.System.loadLibrary("filterfw");
    }

    private native boolean allocate();

    private native boolean deallocate();

    private native boolean nativeInit();

    private native boolean openNativeLibrary(java.lang.String libName);

    private native boolean bindInitFunction(java.lang.String funcName);

    private native boolean bindSetValueFunction(java.lang.String funcName);

    private native boolean bindGetValueFunction(java.lang.String funcName);

    private native boolean bindProcessFunction(java.lang.String funcName);

    private native boolean bindResetFunction(java.lang.String funcName);

    private native boolean bindTeardownFunction(java.lang.String funcName);

    private native boolean callNativeInit();

    private native boolean callNativeSetValue(java.lang.String key, java.lang.String value);

    private native java.lang.String callNativeGetValue(java.lang.String key);

    private native boolean callNativeProcess(android.filterfw.core.NativeFrame[] inputs, android.filterfw.core.NativeFrame output);

    private native boolean callNativeReset();

    private native boolean callNativeTeardown();
}

