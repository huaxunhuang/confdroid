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
public class DrawRectFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "colorRed", hasDefault = true)
    private float mColorRed = 0.8F;

    @android.filterfw.core.GenerateFieldPort(name = "colorGreen", hasDefault = true)
    private float mColorGreen = 0.8F;

    @android.filterfw.core.GenerateFieldPort(name = "colorBlue", hasDefault = true)
    private float mColorBlue = 0.0F;

    private final java.lang.String mVertexShader = "attribute vec4 aPosition;\n" + (("void main() {\n" + "  gl_Position = aPosition;\n") + "}\n");

    private final java.lang.String mFixedColorFragmentShader = "precision mediump float;\n" + ((("uniform vec4 color;\n" + "void main() {\n") + "  gl_FragColor = color;\n") + "}\n");

    private android.filterfw.core.ShaderProgram mProgram;

    public DrawRectFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
        addMaskedInputPort("box", android.filterfw.format.ObjectFormat.fromClass(android.filterfw.geometry.Quad.class, android.filterfw.core.FrameFormat.TARGET_SIMPLE));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        mProgram = new android.filterfw.core.ShaderProgram(context, mVertexShader, mFixedColorFragmentShader);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext env) {
        // Get input frame
        android.filterfw.core.Frame imageFrame = pullInput("image");
        android.filterfw.core.Frame boxFrame = pullInput("box");
        // Get the box
        android.filterfw.geometry.Quad box = ((android.filterfw.geometry.Quad) (boxFrame.getObjectValue()));
        box = box.scaled(2.0F).translated(-1.0F, -1.0F);
        // Create output frame with copy of input
        android.filterfw.core.GLFrame output = ((android.filterfw.core.GLFrame) (env.getFrameManager().duplicateFrame(imageFrame)));
        // Draw onto output
        output.focus();
        renderBox(box);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    private void renderBox(android.filterfw.geometry.Quad box) {
        final int FLOAT_SIZE = 4;
        // Get current values
        float[] color = new float[]{ mColorRed, mColorGreen, mColorBlue, 1.0F };
        float[] vertexValues = new float[]{ box.p0.x, box.p0.y, box.p1.x, box.p1.y, box.p3.x, box.p3.y, box.p2.x, box.p2.y };
        // Set the program variables
        mProgram.setHostValue("color", color);
        mProgram.setAttributeValues("aPosition", vertexValues, 2);
        mProgram.setVertexCount(4);
        // Draw
        mProgram.beginDrawing();
        android.opengl.GLES20.glLineWidth(1.0F);
        android.opengl.GLES20.glDrawArrays(android.opengl.GLES20.GL_LINE_LOOP, 0, 4);
    }
}

