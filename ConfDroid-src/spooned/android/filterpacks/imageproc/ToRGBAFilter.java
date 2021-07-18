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
public class ToRGBAFilter extends android.filterfw.core.Filter {
    private int mInputBPP;

    private android.filterfw.core.Program mProgram;

    private android.filterfw.core.FrameFormat mLastFormat = null;

    public ToRGBAFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        android.filterfw.core.MutableFrameFormat mask = new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_BYTE, android.filterfw.core.FrameFormat.TARGET_NATIVE);
        mask.setDimensionCount(2);
        addMaskedInputPort("image", mask);
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return getConvertedFormat(inputFormat);
    }

    public android.filterfw.core.FrameFormat getConvertedFormat(android.filterfw.core.FrameFormat format) {
        android.filterfw.core.MutableFrameFormat result = format.mutableCopy();
        result.setMetaValue(android.filterfw.format.ImageFormat.COLORSPACE_KEY, android.filterfw.format.ImageFormat.COLORSPACE_RGBA);
        result.setBytesPerSample(4);
        return result;
    }

    public void createProgram(android.filterfw.core.FilterContext context, android.filterfw.core.FrameFormat format) {
        mInputBPP = format.getBytesPerSample();
        if ((mLastFormat != null) && (mLastFormat.getBytesPerSample() == mInputBPP))
            return;

        mLastFormat = format;
        switch (mInputBPP) {
            case 1 :
                mProgram = new android.filterfw.core.NativeProgram("filterpack_imageproc", "gray_to_rgba");
                break;
            case 3 :
                mProgram = new android.filterfw.core.NativeProgram("filterpack_imageproc", "rgb_to_rgba");
                break;
            default :
                throw new java.lang.RuntimeException(("Unsupported BytesPerPixel: " + mInputBPP) + "!");
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        createProgram(context, input.getFormat());
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(getConvertedFormat(input.getFormat()));
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

