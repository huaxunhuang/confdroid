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
public class ProgramPort extends android.filterfw.core.FieldPort {
    protected java.lang.String mVarName;

    public ProgramPort(android.filterfw.core.Filter filter, java.lang.String name, java.lang.String varName, java.lang.reflect.Field field, boolean hasDefault) {
        super(filter, name, field, hasDefault);
        mVarName = varName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Program " + super.toString();
    }

    @java.lang.Override
    public synchronized void transfer(android.filterfw.core.FilterContext context) {
        if (mValueWaiting) {
            try {
                java.lang.Object fieldValue = mField.get(mFilter);
                if (fieldValue != null) {
                    android.filterfw.core.Program program = ((android.filterfw.core.Program) (fieldValue));
                    program.setHostValue(mVarName, mValue);
                    mValueWaiting = false;
                }
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.RuntimeException(("Access to program field '" + mField.getName()) + "' was denied!");
            } catch (java.lang.ClassCastException e) {
                throw new java.lang.RuntimeException(("Non Program field '" + mField.getName()) + "' annotated with ProgramParameter!");
            }
        }
    }
}

