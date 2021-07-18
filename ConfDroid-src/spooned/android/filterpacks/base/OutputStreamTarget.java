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
package android.filterpacks.base;


/**
 *
 *
 * @unknown 
 */
public class OutputStreamTarget extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "stream")
    private java.io.OutputStream mOutputStream;

    public OutputStreamTarget(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addInputPort("data");
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        android.filterfw.core.Frame input = pullInput("data");
        java.nio.ByteBuffer data;
        if (input.getFormat().getObjectClass() == java.lang.String.class) {
            java.lang.String stringVal = ((java.lang.String) (input.getObjectValue()));
            data = java.nio.ByteBuffer.wrap(stringVal.getBytes());
        } else {
            data = input.getData();
        }
        try {
            mOutputStream.write(data.array(), 0, data.limit());
            mOutputStream.flush();
        } catch (java.io.IOException exception) {
            throw new java.lang.RuntimeException(("OutputStreamTarget: Could not write to stream: " + exception.getMessage()) + "!");
        }
    }
}

