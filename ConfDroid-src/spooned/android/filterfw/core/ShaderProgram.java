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
public class ShaderProgram extends android.filterfw.core.Program {
    private int shaderProgramId;

    private int mMaxTileSize = 0;

    // Keep a reference to the GL environment, so that it does not get deallocated while there
    // are still programs living in it.
    private android.filterfw.core.GLEnvironment mGLEnvironment;

    private android.filterfw.core.StopWatchMap mTimer = null;

    private void setTimer() {
        mTimer = new android.filterfw.core.StopWatchMap();
    }

    // Used from native layer for creating empty wrapper only!
    private ShaderProgram() {
    }

    private ShaderProgram(android.filterfw.core.NativeAllocatorTag tag) {
    }

    public ShaderProgram(android.filterfw.core.FilterContext context, java.lang.String fragmentShader) {
        mGLEnvironment = android.filterfw.core.ShaderProgram.getGLEnvironment(context);
        allocate(mGLEnvironment, null, fragmentShader);
        if (!compileAndLink()) {
            throw new java.lang.RuntimeException("Could not compile and link shader!");
        }
        this.setTimer();
    }

    public ShaderProgram(android.filterfw.core.FilterContext context, java.lang.String vertexShader, java.lang.String fragmentShader) {
        mGLEnvironment = android.filterfw.core.ShaderProgram.getGLEnvironment(context);
        allocate(mGLEnvironment, vertexShader, fragmentShader);
        if (!compileAndLink()) {
            throw new java.lang.RuntimeException("Could not compile and link shader!");
        }
        this.setTimer();
    }

    public static android.filterfw.core.ShaderProgram createIdentity(android.filterfw.core.FilterContext context) {
        android.filterfw.core.ShaderProgram program = android.filterfw.core.ShaderProgram.nativeCreateIdentity(android.filterfw.core.ShaderProgram.getGLEnvironment(context));
        program.setTimer();
        return program;
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        deallocate();
    }

    public android.filterfw.core.GLEnvironment getGLEnvironment() {
        return mGLEnvironment;
    }

    @java.lang.Override
    public void process(android.filterfw.core.Frame[] inputs, android.filterfw.core.Frame output) {
        if (mTimer.LOG_MFF_RUNNING_TIMES) {
            mTimer.start("glFinish");
            android.opengl.GLES20.glFinish();
            mTimer.stop("glFinish");
        }
        // Get the GL input frames
        // TODO: We do the same in the NativeProgram... can we find a better way?!
        android.filterfw.core.GLFrame[] glInputs = new android.filterfw.core.GLFrame[inputs.length];
        for (int i = 0; i < inputs.length; ++i) {
            if (inputs[i] instanceof android.filterfw.core.GLFrame) {
                glInputs[i] = ((android.filterfw.core.GLFrame) (inputs[i]));
            } else {
                throw new java.lang.RuntimeException(("ShaderProgram got non-GL frame as input " + i) + "!");
            }
        }
        // Get the GL output frame
        android.filterfw.core.GLFrame glOutput = null;
        if (output instanceof android.filterfw.core.GLFrame) {
            glOutput = ((android.filterfw.core.GLFrame) (output));
        } else {
            throw new java.lang.RuntimeException("ShaderProgram got non-GL output frame!");
        }
        // Adjust tiles to meet maximum tile size requirement
        if (mMaxTileSize > 0) {
            int xTiles = ((output.getFormat().getWidth() + mMaxTileSize) - 1) / mMaxTileSize;
            int yTiles = ((output.getFormat().getHeight() + mMaxTileSize) - 1) / mMaxTileSize;
            setShaderTileCounts(xTiles, yTiles);
        }
        // Process!
        if (!shaderProcess(glInputs, glOutput)) {
            throw new java.lang.RuntimeException("Error executing ShaderProgram!");
        }
        if (mTimer.LOG_MFF_RUNNING_TIMES) {
            android.opengl.GLES20.glFinish();
        }
    }

    @java.lang.Override
    public void setHostValue(java.lang.String variableName, java.lang.Object value) {
        if (!setUniformValue(variableName, value)) {
            throw new java.lang.RuntimeException(("Error setting uniform value for variable '" + variableName) + "'!");
        }
    }

    @java.lang.Override
    public java.lang.Object getHostValue(java.lang.String variableName) {
        return getUniformValue(variableName);
    }

    public void setAttributeValues(java.lang.String attributeName, float[] data, int componentCount) {
        if (!setShaderAttributeValues(attributeName, data, componentCount)) {
            throw new java.lang.RuntimeException(("Error setting attribute value for attribute '" + attributeName) + "'!");
        }
    }

    public void setAttributeValues(java.lang.String attributeName, android.filterfw.core.VertexFrame vertexData, int type, int componentCount, int strideInBytes, int offsetInBytes, boolean normalize) {
        if (!setShaderAttributeVertexFrame(attributeName, vertexData, type, componentCount, strideInBytes, offsetInBytes, normalize)) {
            throw new java.lang.RuntimeException(("Error setting attribute value for attribute '" + attributeName) + "'!");
        }
    }

    public void setSourceRegion(android.filterfw.geometry.Quad region) {
        setSourceRegion(region.p0.x, region.p0.y, region.p1.x, region.p1.y, region.p2.x, region.p2.y, region.p3.x, region.p3.y);
    }

    public void setTargetRegion(android.filterfw.geometry.Quad region) {
        setTargetRegion(region.p0.x, region.p0.y, region.p1.x, region.p1.y, region.p2.x, region.p2.y, region.p3.x, region.p3.y);
    }

    public void setSourceRect(float x, float y, float width, float height) {
        setSourceRegion(x, y, x + width, y, x, y + height, x + width, y + height);
    }

    public void setTargetRect(float x, float y, float width, float height) {
        setTargetRegion(x, y, x + width, y, x, y + height, x + width, y + height);
    }

    public void setClearsOutput(boolean clears) {
        if (!setShaderClearsOutput(clears)) {
            throw new java.lang.RuntimeException(("Could not set clears-output flag to " + clears) + "!");
        }
    }

    public void setClearColor(float r, float g, float b) {
        if (!setShaderClearColor(r, g, b)) {
            throw new java.lang.RuntimeException(((((("Could not set clear color to " + r) + ",") + g) + ",") + b) + "!");
        }
    }

    public void setBlendEnabled(boolean enable) {
        if (!setShaderBlendEnabled(enable)) {
            throw new java.lang.RuntimeException(("Could not set Blending " + enable) + "!");
        }
    }

    public void setBlendFunc(int sfactor, int dfactor) {
        if (!setShaderBlendFunc(sfactor, dfactor)) {
            throw new java.lang.RuntimeException(((("Could not set BlendFunc " + sfactor) + ",") + dfactor) + "!");
        }
    }

    public void setDrawMode(int drawMode) {
        if (!setShaderDrawMode(drawMode)) {
            throw new java.lang.RuntimeException(("Could not set GL draw-mode to " + drawMode) + "!");
        }
    }

    public void setVertexCount(int count) {
        if (!setShaderVertexCount(count)) {
            throw new java.lang.RuntimeException(("Could not set GL vertex count to " + count) + "!");
        }
    }

    public void setMaximumTileSize(int size) {
        mMaxTileSize = size;
    }

    public void beginDrawing() {
        if (!beginShaderDrawing()) {
            throw new java.lang.RuntimeException("Could not prepare shader-program for drawing!");
        }
    }

    private static android.filterfw.core.GLEnvironment getGLEnvironment(android.filterfw.core.FilterContext context) {
        android.filterfw.core.GLEnvironment result = (context != null) ? context.getGLEnvironment() : null;
        if (result == null) {
            throw new java.lang.NullPointerException("Attempting to create ShaderProgram with no GL " + "environment in place!");
        }
        return result;
    }

    static {
        java.lang.System.loadLibrary("filterfw");
    }

    private native boolean allocate(android.filterfw.core.GLEnvironment glEnv, java.lang.String vertexShader, java.lang.String fragmentShader);

    private native boolean deallocate();

    private native boolean compileAndLink();

    private native boolean shaderProcess(android.filterfw.core.GLFrame[] inputs, android.filterfw.core.GLFrame output);

    private native boolean setUniformValue(java.lang.String name, java.lang.Object value);

    private native java.lang.Object getUniformValue(java.lang.String name);

    public native boolean setSourceRegion(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3);

    private native boolean setTargetRegion(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3);

    private static native android.filterfw.core.ShaderProgram nativeCreateIdentity(android.filterfw.core.GLEnvironment glEnv);

    private native boolean setShaderClearsOutput(boolean clears);

    private native boolean setShaderBlendEnabled(boolean enable);

    private native boolean setShaderBlendFunc(int sfactor, int dfactor);

    private native boolean setShaderClearColor(float r, float g, float b);

    private native boolean setShaderDrawMode(int drawMode);

    private native boolean setShaderTileCounts(int xCount, int yCount);

    private native boolean setShaderVertexCount(int vertexCount);

    private native boolean beginShaderDrawing();

    private native boolean setShaderAttributeValues(java.lang.String attributeName, float[] data, int componentCount);

    private native boolean setShaderAttributeVertexFrame(java.lang.String attributeName, android.filterfw.core.VertexFrame vertexData, int type, int componentCount, int strideInBytes, int offsetInBytes, boolean normalize);
}

