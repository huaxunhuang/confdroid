/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.camera2.legacy;


/**
 * A renderer class that manages the GL state, and can draw a frame into a set of output
 * {@link Surface}s.
 */
public class SurfaceTextureRenderer {
    private static final java.lang.String TAG = android.hardware.camera2.legacy.SurfaceTextureRenderer.class.getSimpleName();

    private static final boolean DEBUG = false;

    private static final int EGL_RECORDABLE_ANDROID = 0x3142;// from EGL/eglext.h


    private static final int GL_MATRIX_SIZE = 16;

    private static final int VERTEX_POS_SIZE = 3;

    private static final int VERTEX_UV_SIZE = 2;

    private static final int EGL_COLOR_BITLENGTH = 8;

    private static final int GLES_VERSION = 2;

    private static final int PBUFFER_PIXEL_BYTES = 4;

    private static final int FLIP_TYPE_NONE = 0;

    private static final int FLIP_TYPE_HORIZONTAL = 1;

    private static final int FLIP_TYPE_VERTICAL = 2;

    private static final int FLIP_TYPE_BOTH = android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_HORIZONTAL | android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_VERTICAL;

    private android.opengl.EGLDisplay mEGLDisplay = android.opengl.EGL14.EGL_NO_DISPLAY;

    private android.opengl.EGLContext mEGLContext = android.opengl.EGL14.EGL_NO_CONTEXT;

    private android.opengl.EGLConfig mConfigs;

    private class EGLSurfaceHolder {
        android.view.Surface surface;

        android.opengl.EGLSurface eglSurface;

        int width;

        int height;
    }

    private java.util.List<android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder> mSurfaces = new java.util.ArrayList<android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder>();

    private java.util.List<android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder> mConversionSurfaces = new java.util.ArrayList<android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder>();

    private java.nio.ByteBuffer mPBufferPixels;

    // Hold this to avoid GC
    private volatile android.graphics.SurfaceTexture mSurfaceTexture;

    private static final int FLOAT_SIZE_BYTES = 4;

    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * android.hardware.camera2.legacy.SurfaceTextureRenderer.FLOAT_SIZE_BYTES;

    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;

    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

    // Sampling is mirrored across the horizontal axis
    private static final float[] sHorizontalFlipTriangleVertices = new float[]{ // X, Y, Z, U, V
    -1.0F, -1.0F, 0, 1.0F, 0.0F, 1.0F, -1.0F, 0, 0.0F, 0.0F, -1.0F, 1.0F, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0.0F, 1.0F };

    // Sampling is mirrored across the vertical axis
    private static final float[] sVerticalFlipTriangleVertices = new float[]{ // X, Y, Z, U, V
    -1.0F, -1.0F, 0, 0.0F, 1.0F, 1.0F, -1.0F, 0, 1.0F, 1.0F, -1.0F, 1.0F, 0, 0.0F, 0.0F, 1.0F, 1.0F, 0, 1.0F, 0.0F };

    // Sampling is mirrored across the both axes
    private static final float[] sBothFlipTriangleVertices = new float[]{ // X, Y, Z, U, V
    -1.0F, -1.0F, 0, 1.0F, 1.0F, 1.0F, -1.0F, 0, 0.0F, 1.0F, -1.0F, 1.0F, 0, 1.0F, 0.0F, 1.0F, 1.0F, 0, 0.0F, 0.0F };

    // Sampling is 1:1 for a straight copy for the back camera
    private static final float[] sRegularTriangleVertices = new float[]{ // X, Y, Z, U, V
    -1.0F, -1.0F, 0, 0.0F, 0.0F, 1.0F, -1.0F, 0, 1.0F, 0.0F, -1.0F, 1.0F, 0, 0.0F, 1.0F, 1.0F, 1.0F, 0, 1.0F, 1.0F };

    private java.nio.FloatBuffer mRegularTriangleVertices;

    private java.nio.FloatBuffer mHorizontalFlipTriangleVertices;

    private java.nio.FloatBuffer mVerticalFlipTriangleVertices;

    private java.nio.FloatBuffer mBothFlipTriangleVertices;

    private final int mFacing;

    /**
     * As used in this file, this vertex shader maps a unit square to the view, and
     * tells the fragment shader to interpolate over it.  Each surface pixel position
     * is mapped to a 2D homogeneous texture coordinate of the form (s, t, 0, 1) with
     * s and t in the inclusive range [0, 1], and the matrix from
     * {@link SurfaceTexture#getTransformMatrix(float[])} is used to map this
     * coordinate to a texture location.
     */
    private static final java.lang.String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\n" + ((((((("uniform mat4 uSTMatrix;\n" + "attribute vec4 aPosition;\n") + "attribute vec4 aTextureCoord;\n") + "varying vec2 vTextureCoord;\n") + "void main() {\n") + "  gl_Position = uMVPMatrix * aPosition;\n") + "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n") + "}\n");

    /**
     * This fragment shader simply draws the color in the 2D texture at
     * the location from the {@code VERTEX_SHADER}.
     */
    private static final java.lang.String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n" + ((((("precision mediump float;\n" + "varying vec2 vTextureCoord;\n") + "uniform samplerExternalOES sTexture;\n") + "void main() {\n") + "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n") + "}\n");

    private float[] mMVPMatrix = new float[android.hardware.camera2.legacy.SurfaceTextureRenderer.GL_MATRIX_SIZE];

    private float[] mSTMatrix = new float[android.hardware.camera2.legacy.SurfaceTextureRenderer.GL_MATRIX_SIZE];

    private int mProgram;

    private int mTextureID = 0;

    private int muMVPMatrixHandle;

    private int muSTMatrixHandle;

    private int maPositionHandle;

    private int maTextureHandle;

    private android.hardware.camera2.legacy.PerfMeasurement mPerfMeasurer = null;

    private static final java.lang.String LEGACY_PERF_PROPERTY = "persist.camera.legacy_perf";

    public SurfaceTextureRenderer(int facing) {
        mFacing = facing;
        mRegularTriangleVertices = java.nio.ByteBuffer.allocateDirect(android.hardware.camera2.legacy.SurfaceTextureRenderer.sRegularTriangleVertices.length * android.hardware.camera2.legacy.SurfaceTextureRenderer.FLOAT_SIZE_BYTES).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        mRegularTriangleVertices.put(android.hardware.camera2.legacy.SurfaceTextureRenderer.sRegularTriangleVertices).position(0);
        mHorizontalFlipTriangleVertices = java.nio.ByteBuffer.allocateDirect(android.hardware.camera2.legacy.SurfaceTextureRenderer.sHorizontalFlipTriangleVertices.length * android.hardware.camera2.legacy.SurfaceTextureRenderer.FLOAT_SIZE_BYTES).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        mHorizontalFlipTriangleVertices.put(android.hardware.camera2.legacy.SurfaceTextureRenderer.sHorizontalFlipTriangleVertices).position(0);
        mVerticalFlipTriangleVertices = java.nio.ByteBuffer.allocateDirect(android.hardware.camera2.legacy.SurfaceTextureRenderer.sVerticalFlipTriangleVertices.length * android.hardware.camera2.legacy.SurfaceTextureRenderer.FLOAT_SIZE_BYTES).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticalFlipTriangleVertices.put(android.hardware.camera2.legacy.SurfaceTextureRenderer.sVerticalFlipTriangleVertices).position(0);
        mBothFlipTriangleVertices = java.nio.ByteBuffer.allocateDirect(android.hardware.camera2.legacy.SurfaceTextureRenderer.sBothFlipTriangleVertices.length * android.hardware.camera2.legacy.SurfaceTextureRenderer.FLOAT_SIZE_BYTES).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        mBothFlipTriangleVertices.put(android.hardware.camera2.legacy.SurfaceTextureRenderer.sBothFlipTriangleVertices).position(0);
        android.opengl.Matrix.setIdentityM(mSTMatrix, 0);
    }

    private int loadShader(int shaderType, java.lang.String source) {
        int shader = android.opengl.GLES20.glCreateShader(shaderType);
        checkGlError("glCreateShader type=" + shaderType);
        android.opengl.GLES20.glShaderSource(shader, source);
        android.opengl.GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        android.opengl.GLES20.glGetShaderiv(shader, android.opengl.GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, ("Could not compile shader " + shaderType) + ":");
            android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, " " + android.opengl.GLES20.glGetShaderInfoLog(shader));
            android.opengl.GLES20.glDeleteShader(shader);
            // TODO: handle this more gracefully
            throw new java.lang.IllegalStateException("Could not compile shader " + shaderType);
        }
        return shader;
    }

    private int createProgram(java.lang.String vertexSource, java.lang.String fragmentSource) {
        int vertexShader = loadShader(android.opengl.GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(android.opengl.GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }
        int program = android.opengl.GLES20.glCreateProgram();
        checkGlError("glCreateProgram");
        if (program == 0) {
            android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Could not create program");
        }
        android.opengl.GLES20.glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        android.opengl.GLES20.glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        android.opengl.GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        android.opengl.GLES20.glGetProgramiv(program, android.opengl.GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != android.opengl.GLES20.GL_TRUE) {
            android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Could not link program: ");
            android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, android.opengl.GLES20.glGetProgramInfoLog(program));
            android.opengl.GLES20.glDeleteProgram(program);
            // TODO: handle this more gracefully
            throw new java.lang.IllegalStateException("Could not link program");
        }
        return program;
    }

    private void drawFrame(android.graphics.SurfaceTexture st, int width, int height, int flipType) {
        checkGlError("onDrawFrame start");
        st.getTransformMatrix(mSTMatrix);
        /* smOffset */
        android.opengl.Matrix.setIdentityM(mMVPMatrix, 0);
        // Find intermediate buffer dimensions
        android.util.Size dimens;
        try {
            dimens = android.hardware.camera2.legacy.LegacyCameraDevice.getTextureSize(st);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            // Should never hit this.
            throw new java.lang.IllegalStateException("Surface abandoned, skipping drawFrame...", e);
        }
        float texWidth = dimens.getWidth();
        float texHeight = dimens.getHeight();
        if ((texWidth <= 0) || (texHeight <= 0)) {
            throw new java.lang.IllegalStateException("Illegal intermediate texture with dimension of 0");
        }
        // Letterbox or pillar-box output dimensions into intermediate dimensions.
        android.graphics.RectF intermediate = /* left */
        /* top */
        /* right */
        /* bottom */
        new android.graphics.RectF(0, 0, texWidth, texHeight);
        android.graphics.RectF output = /* left */
        /* top */
        /* right */
        /* bottom */
        new android.graphics.RectF(0, 0, width, height);
        android.graphics.Matrix boxingXform = new android.graphics.Matrix();
        boxingXform.setRectToRect(output, intermediate, android.graphics.Matrix.ScaleToFit.CENTER);
        boxingXform.mapRect(output);
        // Find scaling factor from pillar-boxed/letter-boxed output dimensions to intermediate
        // buffer dimensions.
        float scaleX = intermediate.width() / output.width();
        float scaleY = intermediate.height() / output.height();
        // Intermediate texture is implicitly scaled to 'fill' the output dimensions in clip space
        // coordinates in the shader.  To avoid stretching, we need to scale the larger dimension
        // of the intermediate buffer so that the output buffer is actually letter-boxed
        // or pillar-boxed into the intermediate buffer after clipping.
        /* offset */
        /* x */
        /* y */
        /* z */
        android.opengl.Matrix.scaleM(mMVPMatrix, 0, scaleX, scaleY, 1);
        if (android.hardware.camera2.legacy.SurfaceTextureRenderer.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, (((((((((("Scaling factors (S_x = " + scaleX) + ",S_y = ") + scaleY) + ") used for ") + width) + "x") + height) + " surface, intermediate buffer size is ") + texWidth) + "x") + texHeight);
        }
        // Set viewport to be output buffer dimensions
        android.opengl.GLES20.glViewport(0, 0, width, height);
        if (android.hardware.camera2.legacy.SurfaceTextureRenderer.DEBUG) {
            android.opengl.GLES20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F);
            android.opengl.GLES20.glClear(android.opengl.GLES20.GL_DEPTH_BUFFER_BIT | android.opengl.GLES20.GL_COLOR_BUFFER_BIT);
        }
        android.opengl.GLES20.glUseProgram(mProgram);
        checkGlError("glUseProgram");
        android.opengl.GLES20.glActiveTexture(android.opengl.GLES20.GL_TEXTURE0);
        android.opengl.GLES20.glBindTexture(android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);
        java.nio.FloatBuffer triangleVertices;
        switch (flipType) {
            case android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_HORIZONTAL :
                triangleVertices = mHorizontalFlipTriangleVertices;
                break;
            case android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_VERTICAL :
                triangleVertices = mVerticalFlipTriangleVertices;
                break;
            case android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_BOTH :
                triangleVertices = mBothFlipTriangleVertices;
                break;
            default :
                triangleVertices = mRegularTriangleVertices;
                break;
        }
        triangleVertices.position(android.hardware.camera2.legacy.SurfaceTextureRenderer.TRIANGLE_VERTICES_DATA_POS_OFFSET);
        /* normalized */
        android.opengl.GLES20.glVertexAttribPointer(maPositionHandle, android.hardware.camera2.legacy.SurfaceTextureRenderer.VERTEX_POS_SIZE, android.opengl.GLES20.GL_FLOAT, false, android.hardware.camera2.legacy.SurfaceTextureRenderer.TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices);
        checkGlError("glVertexAttribPointer maPosition");
        android.opengl.GLES20.glEnableVertexAttribArray(maPositionHandle);
        checkGlError("glEnableVertexAttribArray maPositionHandle");
        triangleVertices.position(android.hardware.camera2.legacy.SurfaceTextureRenderer.TRIANGLE_VERTICES_DATA_UV_OFFSET);
        /* normalized */
        android.opengl.GLES20.glVertexAttribPointer(maTextureHandle, android.hardware.camera2.legacy.SurfaceTextureRenderer.VERTEX_UV_SIZE, android.opengl.GLES20.GL_FLOAT, false, android.hardware.camera2.legacy.SurfaceTextureRenderer.TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices);
        checkGlError("glVertexAttribPointer maTextureHandle");
        android.opengl.GLES20.glEnableVertexAttribArray(maTextureHandle);
        checkGlError("glEnableVertexAttribArray maTextureHandle");
        /* count */
        /* transpose */
        /* offset */
        android.opengl.GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        /* count */
        /* transpose */
        /* offset */
        android.opengl.GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);
        /* offset */
        /* count */
        android.opengl.GLES20.glDrawArrays(android.opengl.GLES20.GL_TRIANGLE_STRIP, 0, 4);
        checkGlError("glDrawArrays");
    }

    /**
     * Initializes GL state.  Call this after the EGL surface has been created and made current.
     */
    private void initializeGLState() {
        mProgram = createProgram(android.hardware.camera2.legacy.SurfaceTextureRenderer.VERTEX_SHADER, android.hardware.camera2.legacy.SurfaceTextureRenderer.FRAGMENT_SHADER);
        if (mProgram == 0) {
            throw new java.lang.IllegalStateException("failed creating program");
        }
        maPositionHandle = android.opengl.GLES20.glGetAttribLocation(mProgram, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == (-1)) {
            throw new java.lang.IllegalStateException("Could not get attrib location for aPosition");
        }
        maTextureHandle = android.opengl.GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == (-1)) {
            throw new java.lang.IllegalStateException("Could not get attrib location for aTextureCoord");
        }
        muMVPMatrixHandle = android.opengl.GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == (-1)) {
            throw new java.lang.IllegalStateException("Could not get attrib location for uMVPMatrix");
        }
        muSTMatrixHandle = android.opengl.GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
        checkGlError("glGetUniformLocation uSTMatrix");
        if (muSTMatrixHandle == (-1)) {
            throw new java.lang.IllegalStateException("Could not get attrib location for uSTMatrix");
        }
        int[] textures = new int[1];
        /* n */
        /* offset */
        android.opengl.GLES20.glGenTextures(1, textures, 0);
        mTextureID = textures[0];
        android.opengl.GLES20.glBindTexture(android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);
        checkGlError("glBindTexture mTextureID");
        android.opengl.GLES20.glTexParameterf(android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES, android.opengl.GLES20.GL_TEXTURE_MIN_FILTER, android.opengl.GLES20.GL_NEAREST);
        android.opengl.GLES20.glTexParameterf(android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES, android.opengl.GLES20.GL_TEXTURE_MAG_FILTER, android.opengl.GLES20.GL_LINEAR);
        android.opengl.GLES20.glTexParameteri(android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES, android.opengl.GLES20.GL_TEXTURE_WRAP_S, android.opengl.GLES20.GL_CLAMP_TO_EDGE);
        android.opengl.GLES20.glTexParameteri(android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES, android.opengl.GLES20.GL_TEXTURE_WRAP_T, android.opengl.GLES20.GL_CLAMP_TO_EDGE);
        checkGlError("glTexParameter");
    }

    private int getTextureId() {
        return mTextureID;
    }

    private void clearState() {
        mSurfaces.clear();
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : mConversionSurfaces) {
            try {
                android.hardware.camera2.legacy.LegacyCameraDevice.disconnectSurface(holder.surface);
            } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                android.util.Log.w(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Surface abandoned, skipping...", e);
            }
        }
        mConversionSurfaces.clear();
        mPBufferPixels = null;
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
        mSurfaceTexture = null;
    }

    private void configureEGLContext() {
        mEGLDisplay = android.opengl.EGL14.eglGetDisplay(android.opengl.EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == android.opengl.EGL14.EGL_NO_DISPLAY) {
            throw new java.lang.IllegalStateException("No EGL14 display");
        }
        int[] version = new int[2];
        if (!/* offset */
        /* offset */
        android.opengl.EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            throw new java.lang.IllegalStateException("Cannot initialize EGL14");
        }
        int[] attribList = new int[]{ android.opengl.EGL14.EGL_RED_SIZE, android.hardware.camera2.legacy.SurfaceTextureRenderer.EGL_COLOR_BITLENGTH, android.opengl.EGL14.EGL_GREEN_SIZE, android.hardware.camera2.legacy.SurfaceTextureRenderer.EGL_COLOR_BITLENGTH, android.opengl.EGL14.EGL_BLUE_SIZE, android.hardware.camera2.legacy.SurfaceTextureRenderer.EGL_COLOR_BITLENGTH, android.opengl.EGL14.EGL_RENDERABLE_TYPE, android.opengl.EGL14.EGL_OPENGL_ES2_BIT, android.hardware.camera2.legacy.SurfaceTextureRenderer.EGL_RECORDABLE_ANDROID, 1, android.opengl.EGL14.EGL_SURFACE_TYPE, android.opengl.EGL14.EGL_PBUFFER_BIT | android.opengl.EGL14.EGL_WINDOW_BIT, android.opengl.EGL14.EGL_NONE };
        android.opengl.EGLConfig[] configs = new android.opengl.EGLConfig[1];
        int[] numConfigs = new int[1];
        /* offset */
        /* offset */
        /* offset */
        android.opengl.EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.length, numConfigs, 0);
        checkEglError("eglCreateContext RGB888+recordable ES2");
        mConfigs = configs[0];
        int[] attrib_list = new int[]{ android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION, android.hardware.camera2.legacy.SurfaceTextureRenderer.GLES_VERSION, android.opengl.EGL14.EGL_NONE };
        mEGLContext = /* offset */
        android.opengl.EGL14.eglCreateContext(mEGLDisplay, configs[0], android.opengl.EGL14.EGL_NO_CONTEXT, attrib_list, 0);
        checkEglError("eglCreateContext");
        if (mEGLContext == android.opengl.EGL14.EGL_NO_CONTEXT) {
            throw new java.lang.IllegalStateException("No EGLContext could be made");
        }
    }

    private void configureEGLOutputSurfaces(java.util.Collection<android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder> surfaces) {
        if ((surfaces == null) || (surfaces.size() == 0)) {
            throw new java.lang.IllegalStateException("No Surfaces were provided to draw to");
        }
        int[] surfaceAttribs = new int[]{ android.opengl.EGL14.EGL_NONE };
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : surfaces) {
            holder.eglSurface = /* offset */
            android.opengl.EGL14.eglCreateWindowSurface(mEGLDisplay, mConfigs, holder.surface, surfaceAttribs, 0);
            checkEglError("eglCreateWindowSurface");
        }
    }

    private void configureEGLPbufferSurfaces(java.util.Collection<android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder> surfaces) {
        if ((surfaces == null) || (surfaces.size() == 0)) {
            throw new java.lang.IllegalStateException("No Surfaces were provided to draw to");
        }
        int maxLength = 0;
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : surfaces) {
            int length = holder.width * holder.height;
            // Find max surface size, ensure PBuffer can hold this many pixels
            maxLength = (length > maxLength) ? length : maxLength;
            int[] surfaceAttribs = new int[]{ android.opengl.EGL14.EGL_WIDTH, holder.width, android.opengl.EGL14.EGL_HEIGHT, holder.height, android.opengl.EGL14.EGL_NONE };
            holder.eglSurface = android.opengl.EGL14.eglCreatePbufferSurface(mEGLDisplay, mConfigs, surfaceAttribs, 0);
            checkEglError("eglCreatePbufferSurface");
        }
        mPBufferPixels = java.nio.ByteBuffer.allocateDirect(maxLength * android.hardware.camera2.legacy.SurfaceTextureRenderer.PBUFFER_PIXEL_BYTES).order(java.nio.ByteOrder.nativeOrder());
    }

    private void releaseEGLContext() {
        if (mEGLDisplay != android.opengl.EGL14.EGL_NO_DISPLAY) {
            android.opengl.EGL14.eglMakeCurrent(mEGLDisplay, android.opengl.EGL14.EGL_NO_SURFACE, android.opengl.EGL14.EGL_NO_SURFACE, android.opengl.EGL14.EGL_NO_CONTEXT);
            dumpGlTiming();
            if (mSurfaces != null) {
                for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : mSurfaces) {
                    if (holder.eglSurface != null) {
                        android.opengl.EGL14.eglDestroySurface(mEGLDisplay, holder.eglSurface);
                    }
                }
            }
            if (mConversionSurfaces != null) {
                for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : mConversionSurfaces) {
                    if (holder.eglSurface != null) {
                        android.opengl.EGL14.eglDestroySurface(mEGLDisplay, holder.eglSurface);
                    }
                }
            }
            android.opengl.EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
            android.opengl.EGL14.eglReleaseThread();
            android.opengl.EGL14.eglTerminate(mEGLDisplay);
        }
        mConfigs = null;
        mEGLDisplay = android.opengl.EGL14.EGL_NO_DISPLAY;
        mEGLContext = android.opengl.EGL14.EGL_NO_CONTEXT;
        clearState();
    }

    private void makeCurrent(android.opengl.EGLSurface surface) {
        android.opengl.EGL14.eglMakeCurrent(mEGLDisplay, surface, surface, mEGLContext);
        checkEglError("makeCurrent");
    }

    private boolean swapBuffers(android.opengl.EGLSurface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        boolean result = android.opengl.EGL14.eglSwapBuffers(mEGLDisplay, surface);
        int error = android.opengl.EGL14.eglGetError();
        if (error == android.opengl.EGL14.EGL_BAD_SURFACE) {
            throw new android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException();
        } else
            if (error != android.opengl.EGL14.EGL_SUCCESS) {
                throw new java.lang.IllegalStateException("swapBuffers: EGL error: 0x" + java.lang.Integer.toHexString(error));
            }

        return result;
    }

    private void checkEglError(java.lang.String msg) {
        int error;
        if ((error = android.opengl.EGL14.eglGetError()) != android.opengl.EGL14.EGL_SUCCESS) {
            throw new java.lang.IllegalStateException((msg + ": EGL error: 0x") + java.lang.Integer.toHexString(error));
        }
    }

    private void checkGlError(java.lang.String msg) {
        int error;
        while ((error = android.opengl.GLES20.glGetError()) != android.opengl.GLES20.GL_NO_ERROR) {
            throw new java.lang.IllegalStateException((msg + ": GLES20 error: 0x") + java.lang.Integer.toHexString(error));
        } 
    }

    /**
     * Save a measurement dump to disk, in
     * {@code /sdcard/CameraLegacy/durations_<time>_<width1>x<height1>_...txt}
     */
    private void dumpGlTiming() {
        if (mPerfMeasurer == null)
            return;

        java.io.File legacyStorageDir = new java.io.File(android.os.Environment.getExternalStorageDirectory(), "CameraLegacy");
        if (!legacyStorageDir.exists()) {
            if (!legacyStorageDir.mkdirs()) {
                android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Failed to create directory for data dump");
                return;
            }
        }
        java.lang.StringBuilder path = new java.lang.StringBuilder(legacyStorageDir.getPath());
        path.append(java.io.File.separator);
        path.append("durations_");
        android.text.format.Time now = new android.text.format.Time();
        now.setToNow();
        path.append(now.format2445());
        path.append("_S");
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder surface : mSurfaces) {
            path.append(java.lang.String.format("_%d_%d", surface.width, surface.height));
        }
        path.append("_C");
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder surface : mConversionSurfaces) {
            path.append(java.lang.String.format("_%d_%d", surface.width, surface.height));
        }
        path.append(".txt");
        mPerfMeasurer.dumpPerformanceData(path.toString());
    }

    private void setupGlTiming() {
        if (android.hardware.camera2.legacy.PerfMeasurement.isGlTimingSupported()) {
            android.util.Log.d(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Enabling GL performance measurement");
            mPerfMeasurer = new android.hardware.camera2.legacy.PerfMeasurement();
        } else {
            android.util.Log.d(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "GL performance measurement not supported on this device");
            mPerfMeasurer = null;
        }
    }

    private void beginGlTiming() {
        if (mPerfMeasurer == null)
            return;

        mPerfMeasurer.startTimer();
    }

    private void addGlTimestamp(long timestamp) {
        if (mPerfMeasurer == null)
            return;

        mPerfMeasurer.addTimestamp(timestamp);
    }

    private void endGlTiming() {
        if (mPerfMeasurer == null)
            return;

        mPerfMeasurer.stopTimer();
    }

    /**
     * Return the surface texture to draw to - this is the texture use to when producing output
     * surface buffers.
     *
     * @return a {@link SurfaceTexture}.
     */
    public android.graphics.SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    /**
     * Set a collection of output {@link Surface}s that can be drawn to.
     *
     * @param surfaces
     * 		a {@link Collection} of surfaces.
     */
    public void configureSurfaces(java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> surfaces) {
        releaseEGLContext();
        if ((surfaces == null) || (surfaces.size() == 0)) {
            android.util.Log.w(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "No output surfaces configured for GL drawing.");
            return;
        }
        for (android.util.Pair<android.view.Surface, android.util.Size> p : surfaces) {
            android.view.Surface s = p.first;
            android.util.Size surfaceSize = p.second;
            // If pixel conversions aren't handled by egl, use a pbuffer
            try {
                android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder = new android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder();
                holder.surface = s;
                holder.width = surfaceSize.getWidth();
                holder.height = surfaceSize.getHeight();
                if (android.hardware.camera2.legacy.LegacyCameraDevice.needsConversion(s)) {
                    mConversionSurfaces.add(holder);
                    // LegacyCameraDevice is the producer of surfaces if it's not handled by EGL,
                    // so LegacyCameraDevice needs to connect to the surfaces.
                    android.hardware.camera2.legacy.LegacyCameraDevice.connectSurface(s);
                } else {
                    mSurfaces.add(holder);
                }
            } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                android.util.Log.w(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Surface abandoned, skipping configuration... ", e);
            }
        }
        // Set up egl display
        configureEGLContext();
        // Set up regular egl surfaces if needed
        if (mSurfaces.size() > 0) {
            configureEGLOutputSurfaces(mSurfaces);
        }
        // Set up pbuffer surface if needed
        if (mConversionSurfaces.size() > 0) {
            configureEGLPbufferSurfaces(mConversionSurfaces);
        }
        makeCurrent(mSurfaces.size() > 0 ? mSurfaces.get(0).eglSurface : mConversionSurfaces.get(0).eglSurface);
        initializeGLState();
        mSurfaceTexture = new android.graphics.SurfaceTexture(getTextureId());
        // Set up performance tracking if enabled
        if (android.os.SystemProperties.getBoolean(android.hardware.camera2.legacy.SurfaceTextureRenderer.LEGACY_PERF_PROPERTY, false)) {
            setupGlTiming();
        }
    }

    /**
     * Draw the current buffer in the {@link SurfaceTexture} returned from
     * {@link #getSurfaceTexture()} into the set of target {@link Surface}s
     * in the next request from the given {@link CaptureCollector}, or drop
     * the frame if none is available.
     *
     * <p>
     * Any {@link Surface}s targeted must be a subset of the {@link Surface}s
     * set in the last {@link #configureSurfaces(java.util.Collection)} call.
     * </p>
     *
     * @param targetCollector
     * 		the surfaces to draw to.
     */
    public void drawIntoSurfaces(android.hardware.camera2.legacy.CaptureCollector targetCollector) {
        if (((mSurfaces == null) || (mSurfaces.size() == 0)) && ((mConversionSurfaces == null) || (mConversionSurfaces.size() == 0))) {
            return;
        }
        boolean doTiming = targetCollector.hasPendingPreviewCaptures();
        checkGlError("before updateTexImage");
        if (doTiming) {
            beginGlTiming();
        }
        mSurfaceTexture.updateTexImage();
        long timestamp = mSurfaceTexture.getTimestamp();
        android.util.Pair<android.hardware.camera2.legacy.RequestHolder, java.lang.Long> captureHolder = targetCollector.previewCaptured(timestamp);
        // No preview request queued, drop frame.
        if (captureHolder == null) {
            if (android.hardware.camera2.legacy.SurfaceTextureRenderer.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Dropping preview frame.");
            }
            if (doTiming) {
                endGlTiming();
            }
            return;
        }
        android.hardware.camera2.legacy.RequestHolder request = captureHolder.first;
        java.util.Collection<android.view.Surface> targetSurfaces = request.getHolderTargets();
        if (doTiming) {
            addGlTimestamp(timestamp);
        }
        java.util.List<java.lang.Long> targetSurfaceIds = new java.util.ArrayList();
        try {
            targetSurfaceIds = android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceIds(targetSurfaces);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            android.util.Log.w(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Surface abandoned, dropping frame. ", e);
            request.setOutputAbandoned();
        }
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : mSurfaces) {
            if (android.hardware.camera2.legacy.LegacyCameraDevice.containsSurfaceId(holder.surface, targetSurfaceIds)) {
                try {
                    android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceDimens(holder.surface, holder.width, holder.height);
                    makeCurrent(holder.eglSurface);
                    android.hardware.camera2.legacy.LegacyCameraDevice.setNextTimestamp(holder.surface, captureHolder.second);
                    drawFrame(mSurfaceTexture, holder.width, holder.height, mFacing == android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT ? android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_HORIZONTAL : android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_NONE);
                    swapBuffers(holder.eglSurface);
                } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    android.util.Log.w(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Surface abandoned, dropping frame. ", e);
                    request.setOutputAbandoned();
                }
            }
        }
        for (android.hardware.camera2.legacy.SurfaceTextureRenderer.EGLSurfaceHolder holder : mConversionSurfaces) {
            if (android.hardware.camera2.legacy.LegacyCameraDevice.containsSurfaceId(holder.surface, targetSurfaceIds)) {
                makeCurrent(holder.eglSurface);
                // glReadPixels reads from the bottom of the buffer, so add an extra vertical flip
                drawFrame(mSurfaceTexture, holder.width, holder.height, mFacing == android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT ? android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_BOTH : android.hardware.camera2.legacy.SurfaceTextureRenderer.FLIP_TYPE_VERTICAL);
                mPBufferPixels.clear();
                /* x */
                /* y */
                android.opengl.GLES20.glReadPixels(0, 0, holder.width, holder.height, android.opengl.GLES20.GL_RGBA, android.opengl.GLES20.GL_UNSIGNED_BYTE, mPBufferPixels);
                checkGlError("glReadPixels");
                try {
                    int format = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(holder.surface);
                    android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceDimens(holder.surface, holder.width, holder.height);
                    android.hardware.camera2.legacy.LegacyCameraDevice.setNextTimestamp(holder.surface, captureHolder.second);
                    android.hardware.camera2.legacy.LegacyCameraDevice.produceFrame(holder.surface, mPBufferPixels.array(), holder.width, holder.height, format);
                } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    android.util.Log.w(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Surface abandoned, dropping frame. ", e);
                    request.setOutputAbandoned();
                }
            }
        }
        targetCollector.previewProduced();
        if (doTiming) {
            endGlTiming();
        }
    }

    /**
     * Clean up the current GL context.
     */
    public void cleanupEGLContext() {
        releaseEGLContext();
    }

    /**
     * Drop all current GL operations on the floor.
     */
    public void flush() {
        // TODO: implement flush
        android.util.Log.e(android.hardware.camera2.legacy.SurfaceTextureRenderer.TAG, "Flush not yet implemented.");
    }
}

