package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class GLFrame extends android.filterfw.core.Frame {
    // GL-related binding types
    public static final int EXISTING_TEXTURE_BINDING = 100;

    public static final int EXISTING_FBO_BINDING = 101;

    public static final int NEW_TEXTURE_BINDING = 102;// TODO: REMOVE THIS


    public static final int NEW_FBO_BINDING = 103;// TODO: REMOVE THIS


    public static final int EXTERNAL_TEXTURE = 104;

    private int glFrameId = -1;

    /**
     * Flag whether we own the texture or not. If we do not, we must be careful when caching or
     * storing the frame, as the user may delete, and regenerate it.
     */
    private boolean mOwnsTexture = true;

    /**
     * Keep a reference to the GL environment, so that it does not get deallocated while there
     * are still frames living in it.
     */
    private android.filterfw.core.GLEnvironment mGLEnvironment;

    GLFrame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager) {
        super(format, frameManager);
    }

    GLFrame(android.filterfw.core.FrameFormat format, android.filterfw.core.FrameManager frameManager, int bindingType, long bindingId) {
        super(format, frameManager, bindingType, bindingId);
    }

    void init(android.filterfw.core.GLEnvironment glEnv) {
        android.filterfw.core.FrameFormat format = getFormat();
        mGLEnvironment = glEnv;
        // Check that we have a valid format
        if (format.getBytesPerSample() != 4) {
            throw new java.lang.IllegalArgumentException("GL frames must have 4 bytes per sample!");
        } else
            if (format.getDimensionCount() != 2) {
                throw new java.lang.IllegalArgumentException("GL frames must be 2-dimensional!");
            } else
                if (getFormat().getSize() < 0) {
                    throw new java.lang.IllegalArgumentException("Initializing GL frame with zero size!");
                }


        // Create correct frame
        int bindingType = getBindingType();
        boolean reusable = true;
        if (bindingType == android.filterfw.core.Frame.NO_BINDING) {
            initNew(false);
        } else
            if (bindingType == android.filterfw.core.GLFrame.EXTERNAL_TEXTURE) {
                initNew(true);
                reusable = false;
            } else
                if (bindingType == android.filterfw.core.GLFrame.EXISTING_TEXTURE_BINDING) {
                    initWithTexture(((int) (getBindingId())));
                } else
                    if (bindingType == android.filterfw.core.GLFrame.EXISTING_FBO_BINDING) {
                        initWithFbo(((int) (getBindingId())));
                    } else
                        if (bindingType == android.filterfw.core.GLFrame.NEW_TEXTURE_BINDING) {
                            initWithTexture(((int) (getBindingId())));
                        } else
                            if (bindingType == android.filterfw.core.GLFrame.NEW_FBO_BINDING) {
                                initWithFbo(((int) (getBindingId())));
                            } else {
                                throw new java.lang.RuntimeException(("Attempting to create GL frame with unknown binding type " + bindingType) + "!");
                            }





        setReusable(reusable);
    }

    private void initNew(boolean isExternal) {
        if (isExternal) {
            if (!nativeAllocateExternal(mGLEnvironment)) {
                throw new java.lang.RuntimeException("Could not allocate external GL frame!");
            }
        } else {
            if (!nativeAllocate(mGLEnvironment, getFormat().getWidth(), getFormat().getHeight())) {
                throw new java.lang.RuntimeException("Could not allocate GL frame!");
            }
        }
    }

    private void initWithTexture(int texId) {
        int width = getFormat().getWidth();
        int height = getFormat().getHeight();
        if (!nativeAllocateWithTexture(mGLEnvironment, texId, width, height)) {
            throw new java.lang.RuntimeException("Could not allocate texture backed GL frame!");
        }
        mOwnsTexture = false;
        markReadOnly();
    }

    private void initWithFbo(int fboId) {
        int width = getFormat().getWidth();
        int height = getFormat().getHeight();
        if (!nativeAllocateWithFbo(mGLEnvironment, fboId, width, height)) {
            throw new java.lang.RuntimeException("Could not allocate FBO backed GL frame!");
        }
    }

    void flushGPU(java.lang.String message) {
        android.filterfw.core.StopWatchMap timer = android.filterfw.core.GLFrameTimer.get();
        if (timer.LOG_MFF_RUNNING_TIMES) {
            timer.start("glFinish " + message);
            android.opengl.GLES20.glFinish();
            timer.stop("glFinish " + message);
        }
    }

    @java.lang.Override
    protected synchronized boolean hasNativeAllocation() {
        return glFrameId != (-1);
    }

    @java.lang.Override
    protected synchronized void releaseNativeAllocation() {
        nativeDeallocate();
        glFrameId = -1;
    }

    public android.filterfw.core.GLEnvironment getGLEnvironment() {
        return mGLEnvironment;
    }

    @java.lang.Override
    public java.lang.Object getObjectValue() {
        assertGLEnvValid();
        return java.nio.ByteBuffer.wrap(getNativeData());
    }

    @java.lang.Override
    public void setInts(int[] ints) {
        assertFrameMutable();
        assertGLEnvValid();
        if (!setNativeInts(ints)) {
            throw new java.lang.RuntimeException("Could not set int values for GL frame!");
        }
    }

    @java.lang.Override
    public int[] getInts() {
        assertGLEnvValid();
        flushGPU("getInts");
        return getNativeInts();
    }

    @java.lang.Override
    public void setFloats(float[] floats) {
        assertFrameMutable();
        assertGLEnvValid();
        if (!setNativeFloats(floats)) {
            throw new java.lang.RuntimeException("Could not set int values for GL frame!");
        }
    }

    @java.lang.Override
    public float[] getFloats() {
        assertGLEnvValid();
        flushGPU("getFloats");
        return getNativeFloats();
    }

    @java.lang.Override
    public void setData(java.nio.ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        assertGLEnvValid();
        byte[] bytes = buffer.array();
        if (getFormat().getSize() != bytes.length) {
            throw new java.lang.RuntimeException("Data size in setData does not match GL frame size!");
        } else
            if (!setNativeData(bytes, offset, length)) {
                throw new java.lang.RuntimeException("Could not set GL frame data!");
            }

    }

    @java.lang.Override
    public java.nio.ByteBuffer getData() {
        assertGLEnvValid();
        flushGPU("getData");
        return java.nio.ByteBuffer.wrap(getNativeData());
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        assertFrameMutable();
        assertGLEnvValid();
        if ((getFormat().getWidth() != bitmap.getWidth()) || (getFormat().getHeight() != bitmap.getHeight())) {
            throw new java.lang.RuntimeException("Bitmap dimensions do not match GL frame dimensions!");
        } else {
            android.graphics.Bitmap rgbaBitmap = android.filterfw.core.Frame.convertBitmapToRGBA(bitmap);
            if (!setNativeBitmap(rgbaBitmap, rgbaBitmap.getByteCount())) {
                throw new java.lang.RuntimeException("Could not set GL frame bitmap data!");
            }
        }
    }

    @java.lang.Override
    public android.graphics.Bitmap getBitmap() {
        assertGLEnvValid();
        flushGPU("getBitmap");
        android.graphics.Bitmap result = android.graphics.Bitmap.createBitmap(getFormat().getWidth(), getFormat().getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        if (!getNativeBitmap(result)) {
            throw new java.lang.RuntimeException("Could not get bitmap data from GL frame!");
        }
        return result;
    }

    @java.lang.Override
    public void setDataFromFrame(android.filterfw.core.Frame frame) {
        assertGLEnvValid();
        // Make sure frame fits
        if (getFormat().getSize() < frame.getFormat().getSize()) {
            throw new java.lang.RuntimeException((((("Attempting to assign frame of size " + frame.getFormat().getSize()) + " to ") + "smaller GL frame of size ") + getFormat().getSize()) + "!");
        }
        // Invoke optimized implementations if possible
        if (frame instanceof android.filterfw.core.NativeFrame) {
            nativeCopyFromNative(((android.filterfw.core.NativeFrame) (frame)));
        } else
            if (frame instanceof android.filterfw.core.GLFrame) {
                nativeCopyFromGL(((android.filterfw.core.GLFrame) (frame)));
            } else
                if (frame instanceof android.filterfw.core.SimpleFrame) {
                    setObjectValue(frame.getObjectValue());
                } else {
                    super.setDataFromFrame(frame);
                }


    }

    public void setViewport(int x, int y, int width, int height) {
        assertFrameMutable();
        setNativeViewport(x, y, width, height);
    }

    public void setViewport(android.graphics.Rect rect) {
        assertFrameMutable();
        setNativeViewport(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
    }

    public void generateMipMap() {
        assertFrameMutable();
        assertGLEnvValid();
        if (!generateNativeMipMap()) {
            throw new java.lang.RuntimeException("Could not generate mip-map for GL frame!");
        }
    }

    public void setTextureParameter(int param, int value) {
        assertFrameMutable();
        assertGLEnvValid();
        if (!setNativeTextureParam(param, value)) {
            throw new java.lang.RuntimeException((((("Could not set texture value " + param) + " = ") + value) + " ") + "for GLFrame!");
        }
    }

    public int getTextureId() {
        return getNativeTextureId();
    }

    public int getFboId() {
        return getNativeFboId();
    }

    public void focus() {
        if (!nativeFocus()) {
            throw new java.lang.RuntimeException("Could not focus on GLFrame for drawing!");
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("GLFrame id: " + glFrameId) + " (") + getFormat()) + ") with texture ID ") + getTextureId()) + ", FBO ID ") + getFboId();
    }

    @java.lang.Override
    protected void reset(android.filterfw.core.FrameFormat newFormat) {
        if (!nativeResetParams()) {
            throw new java.lang.RuntimeException("Could not reset GLFrame texture parameters!");
        }
        super.reset(newFormat);
    }

    @java.lang.Override
    protected void onFrameStore() {
        if (!mOwnsTexture) {
            // Detach texture from FBO in case user manipulates it.
            nativeDetachTexFromFbo();
        }
    }

    @java.lang.Override
    protected void onFrameFetch() {
        if (!mOwnsTexture) {
            // Reattach texture to FBO when using frame again. This may reallocate the texture
            // in case it has become invalid.
            nativeReattachTexToFbo();
        }
    }

    private void assertGLEnvValid() {
        if (!mGLEnvironment.isContextActive()) {
            if (android.filterfw.core.GLEnvironment.isAnyContextActive()) {
                throw new java.lang.RuntimeException((("Attempting to access " + this) + " with foreign GL ") + "context active!");
            } else {
                throw new java.lang.RuntimeException((("Attempting to access " + this) + " with no GL context ") + " active!");
            }
        }
    }

    static {
        java.lang.System.loadLibrary("filterfw");
    }

    private native boolean nativeAllocate(android.filterfw.core.GLEnvironment env, int width, int height);

    private native boolean nativeAllocateWithTexture(android.filterfw.core.GLEnvironment env, int textureId, int width, int height);

    private native boolean nativeAllocateWithFbo(android.filterfw.core.GLEnvironment env, int fboId, int width, int height);

    private native boolean nativeAllocateExternal(android.filterfw.core.GLEnvironment env);

    private native boolean nativeDeallocate();

    private native boolean setNativeData(byte[] data, int offset, int length);

    private native byte[] getNativeData();

    private native boolean setNativeInts(int[] ints);

    private native boolean setNativeFloats(float[] floats);

    private native int[] getNativeInts();

    private native float[] getNativeFloats();

    private native boolean setNativeBitmap(android.graphics.Bitmap bitmap, int size);

    private native boolean getNativeBitmap(android.graphics.Bitmap bitmap);

    private native boolean setNativeViewport(int x, int y, int width, int height);

    private native int getNativeTextureId();

    private native int getNativeFboId();

    private native boolean generateNativeMipMap();

    private native boolean setNativeTextureParam(int param, int value);

    private native boolean nativeResetParams();

    private native boolean nativeCopyFromNative(android.filterfw.core.NativeFrame frame);

    private native boolean nativeCopyFromGL(android.filterfw.core.GLFrame frame);

    private native boolean nativeFocus();

    private native boolean nativeReattachTexToFbo();

    private native boolean nativeDetachTexFromFbo();
}

