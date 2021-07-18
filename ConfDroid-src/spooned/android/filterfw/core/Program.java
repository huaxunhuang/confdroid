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
public abstract class Program {
    public abstract void process(android.filterfw.core.Frame[] inputs, android.filterfw.core.Frame output);

    public void process(android.filterfw.core.Frame input, android.filterfw.core.Frame output) {
        android.filterfw.core.Frame[] inputs = new android.filterfw.core.Frame[1];
        inputs[0] = input;
        process(inputs, output);
    }

    public abstract void setHostValue(java.lang.String variableName, java.lang.Object value);

    public abstract java.lang.Object getHostValue(java.lang.String variableName);

    public void reset() {
    }
}

