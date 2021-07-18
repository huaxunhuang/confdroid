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
 * The FixedRotationFilter rotates the input image clockwise, it only accepts
 * 4 rotation angles: 0, 90, 180, 270
 *
 * @unknown 
 */
public class FixedRotationFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "rotation", hasDefault = true)
    private int mRotation = 0;

    private android.filterfw.core.ShaderProgram mProgram = null;

    public FixedRotationFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        android.filterfw.core.Frame input = pullInput("image");
        if (mRotation == 0) {
            pushOutput("image", input);
            return;
        }
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        // Create program if not created already
        if (mProgram == null) {
            mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        }
        android.filterfw.core.MutableFrameFormat outputFormat = inputFormat.mutableCopy();
        int width = inputFormat.getWidth();
        int height = inputFormat.getHeight();
        android.filterfw.geometry.Point p1 = new android.filterfw.geometry.Point(0.0F, 0.0F);
        android.filterfw.geometry.Point p2 = new android.filterfw.geometry.Point(1.0F, 0.0F);
        android.filterfw.geometry.Point p3 = new android.filterfw.geometry.Point(0.0F, 1.0F);
        android.filterfw.geometry.Point p4 = new android.filterfw.geometry.Point(1.0F, 1.0F);
        android.filterfw.geometry.Quad sourceRegion;
        switch (((int) (java.lang.Math.round(mRotation / 90.0F))) % 4) {
            case 1 :
                sourceRegion = new android.filterfw.geometry.Quad(p3, p1, p4, p2);
                outputFormat.setDimensions(height, width);
                break;
            case 2 :
                sourceRegion = new android.filterfw.geometry.Quad(p4, p3, p2, p1);
                break;
            case 3 :
                sourceRegion = new android.filterfw.geometry.Quad(p2, p4, p1, p3);
                outputFormat.setDimensions(height, width);
                break;
            case 0 :
            default :
                sourceRegion = new android.filterfw.geometry.Quad(p1, p2, p3, p4);
                break;
        }
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(outputFormat);
        // Set the source region
        mProgram.setSourceRegion(sourceRegion);
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

