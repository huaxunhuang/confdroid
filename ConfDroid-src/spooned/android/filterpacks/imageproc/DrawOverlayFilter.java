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
package android.filterpacks.imageproc;


/**
 *
 *
 * @unknown 
 */
public class DrawOverlayFilter extends android.filterfw.core.Filter {
    private android.filterfw.core.ShaderProgram mProgram;

    public DrawOverlayFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        android.filterfw.core.FrameFormat imageFormatMask = android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        addMaskedInputPort("source", imageFormatMask);
        addMaskedInputPort("overlay", imageFormatMask);
        addMaskedInputPort("box", android.filterfw.format.ObjectFormat.fromClass(android.filterfw.geometry.Quad.class, android.filterfw.core.FrameFormat.TARGET_SIMPLE));
        addOutputBasedOnInput("image", "source");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext env) {
        // Get input frame
        android.filterfw.core.Frame sourceFrame = pullInput("source");
        android.filterfw.core.Frame overlayFrame = pullInput("overlay");
        android.filterfw.core.Frame boxFrame = pullInput("box");
        // Get the box
        android.filterfw.geometry.Quad box = ((android.filterfw.geometry.Quad) (boxFrame.getObjectValue()));
        box = box.translated(1.0F, 1.0F).scaled(2.0F);
        mProgram.setTargetRegion(box);
        // Create output frame with copy of input
        android.filterfw.core.Frame output = env.getFrameManager().newFrame(sourceFrame.getFormat());
        output.setDataFromFrame(sourceFrame);
        // Draw onto output
        mProgram.process(overlayFrame, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

