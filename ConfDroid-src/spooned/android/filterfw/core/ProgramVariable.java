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
public class ProgramVariable {
    private android.filterfw.core.Program mProgram;

    private java.lang.String mVarName;

    public ProgramVariable(android.filterfw.core.Program program, java.lang.String varName) {
        mProgram = program;
        mVarName = varName;
    }

    public android.filterfw.core.Program getProgram() {
        return mProgram;
    }

    public java.lang.String getVariableName() {
        return mVarName;
    }

    public void setValue(java.lang.Object value) {
        if (mProgram == null) {
            throw new java.lang.RuntimeException(("Attempting to set program variable '" + mVarName) + "' but the program is null!");
        }
        mProgram.setHostValue(mVarName, value);
    }

    public java.lang.Object getValue() {
        if (mProgram == null) {
            throw new java.lang.RuntimeException(("Attempting to get program variable '" + mVarName) + "' but the program is null!");
        }
        return mProgram.getHostValue(mVarName);
    }
}

