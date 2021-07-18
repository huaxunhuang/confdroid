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
public class GLTextureTarget extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "texId")
    private int mTexId;

    public GLTextureTarget(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("frame", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("frame");
        android.filterfw.core.FrameFormat format = android.filterfw.format.ImageFormat.create(input.getFormat().getWidth(), input.getFormat().getHeight(), android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        android.filterfw.core.Frame frame = context.getFrameManager().newBoundFrame(format, android.filterfw.core.GLFrame.EXISTING_TEXTURE_BINDING, mTexId);
        // Copy to our texture frame
        frame.setDataFromFrame(input);
        frame.release();
    }
}

