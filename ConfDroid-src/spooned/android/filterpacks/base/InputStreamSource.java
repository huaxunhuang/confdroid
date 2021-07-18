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
public class InputStreamSource extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFinalPort(name = "target")
    private java.lang.String mTarget;

    @android.filterfw.core.GenerateFieldPort(name = "stream")
    private java.io.InputStream mInputStream;

    @android.filterfw.core.GenerateFinalPort(name = "format", hasDefault = true)
    private android.filterfw.core.MutableFrameFormat mOutputFormat = null;

    public InputStreamSource(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        int target = android.filterfw.core.FrameFormat.readTargetString(mTarget);
        if (mOutputFormat == null) {
            mOutputFormat = android.filterfw.format.PrimitiveFormat.createByteFormat(target);
        }
        addOutputPort("data", mOutputFormat);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        int fileSize = 0;
        java.nio.ByteBuffer byteBuffer = null;
        // Read the file
        try {
            java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = mInputStream.read(buffer)) > 0) {
                byteStream.write(buffer, 0, bytesRead);
                fileSize += bytesRead;
            } 
            byteBuffer = java.nio.ByteBuffer.wrap(byteStream.toByteArray());
        } catch (java.io.IOException exception) {
            throw new java.lang.RuntimeException(("InputStreamSource: Could not read stream: " + exception.getMessage()) + "!");
        }
        // Put it into a frame
        mOutputFormat.setDimensions(fileSize);
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(mOutputFormat);
        output.setData(byteBuffer);
        // Push output
        pushOutput("data", output);
        // Release pushed frame
        output.release();
        // Close output port as we are done here
        closeOutputPort("data");
    }
}

