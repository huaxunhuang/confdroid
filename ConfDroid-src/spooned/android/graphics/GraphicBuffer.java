/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.graphics;


/**
 * Simple wrapper for the native GraphicBuffer class.
 *
 * @unknown 
 */
@java.lang.SuppressWarnings("UnusedDeclaration")
public class GraphicBuffer implements android.os.Parcelable {
    // Note: keep usage flags in sync with GraphicBuffer.h and gralloc.h
    public static final int USAGE_SW_READ_NEVER = 0x0;

    public static final int USAGE_SW_READ_RARELY = 0x2;

    public static final int USAGE_SW_READ_OFTEN = 0x3;

    public static final int USAGE_SW_READ_MASK = 0xf;

    public static final int USAGE_SW_WRITE_NEVER = 0x0;

    public static final int USAGE_SW_WRITE_RARELY = 0x20;

    public static final int USAGE_SW_WRITE_OFTEN = 0x30;

    public static final int USAGE_SW_WRITE_MASK = 0xf0;

    public static final int USAGE_SOFTWARE_MASK = android.graphics.GraphicBuffer.USAGE_SW_READ_MASK | android.graphics.GraphicBuffer.USAGE_SW_WRITE_MASK;

    public static final int USAGE_PROTECTED = 0x4000;

    public static final int USAGE_HW_TEXTURE = 0x100;

    public static final int USAGE_HW_RENDER = 0x200;

    public static final int USAGE_HW_2D = 0x400;

    public static final int USAGE_HW_COMPOSER = 0x800;

    public static final int USAGE_HW_VIDEO_ENCODER = 0x10000;

    public static final int USAGE_HW_MASK = 0x71f00;

    private final int mWidth;

    private final int mHeight;

    private final int mFormat;

    private final int mUsage;

    // Note: do not rename, this field is used by native code
    @android.annotation.UnsupportedAppUsage
    private final long mNativeObject;

    // These two fields are only used by lock/unlockCanvas()
    private android.graphics.Canvas mCanvas;

    private int mSaveCount;

    // If set to true, this GraphicBuffer instance cannot be used anymore
    private boolean mDestroyed;

    /**
     * Creates new <code>GraphicBuffer</code> instance. This method will return null
     * if the buffer cannot be created.
     *
     * @param width
     * 		The width in pixels of the buffer
     * @param height
     * 		The height in pixels of the buffer
     * @param format
     * 		The format of each pixel as specified in {@link PixelFormat}
     * @param usage
     * 		Hint indicating how the buffer will be used
     * @return A <code>GraphicBuffer</code> instance or null
     */
    public static android.graphics.GraphicBuffer create(int width, int height, int format, int usage) {
        long nativeObject = android.graphics.GraphicBuffer.nCreateGraphicBuffer(width, height, format, usage);
        if (nativeObject != 0) {
            return new android.graphics.GraphicBuffer(width, height, format, usage, nativeObject);
        }
        return null;
    }

    /**
     * Private use only. See {@link #create(int, int, int, int)}.
     */
    @android.annotation.UnsupportedAppUsage
    private GraphicBuffer(int width, int height, int format, int usage, long nativeObject) {
        mWidth = width;
        mHeight = height;
        mFormat = format;
        mUsage = usage;
        mNativeObject = nativeObject;
    }

    /**
     * For SurfaceControl JNI.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.graphics.GraphicBuffer createFromExisting(int width, int height, int format, int usage, long unwrappedNativeObject) {
        long nativeObject = android.graphics.GraphicBuffer.nWrapGraphicBuffer(unwrappedNativeObject);
        if (nativeObject != 0) {
            return new android.graphics.GraphicBuffer(width, height, format, usage, nativeObject);
        }
        return null;
    }

    /**
     * Returns the width of this buffer in pixels.
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Returns the height of this buffer in pixels.
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Returns the pixel format of this buffer. The pixel format must be one of
     * the formats defined in {@link PixelFormat}.
     */
    public int getFormat() {
        return mFormat;
    }

    /**
     * Returns the usage hint set on this buffer.
     */
    public int getUsage() {
        return mUsage;
    }

    /**
     * <p>Start editing the pixels in the buffer. A null is returned if the buffer
     * cannot be locked for editing.</p>
     *
     * <p>The content of the buffer is preserved between unlockCanvas()
     * and lockCanvas().</p>
     *
     * <p>If this method is called after {@link #destroy()}, the return value will
     * always be null.</p>
     *
     * @return A Canvas used to draw into the buffer, or null.
     * @see #lockCanvas(android.graphics.Rect)
     * @see #unlockCanvasAndPost(android.graphics.Canvas)
     * @see #isDestroyed()
     */
    public android.graphics.Canvas lockCanvas() {
        return lockCanvas(null);
    }

    /**
     * Just like {@link #lockCanvas()} but allows specification of a dirty
     * rectangle.
     *
     * <p>If this method is called after {@link #destroy()}, the return value will
     * always be null.</p>
     *
     * @param dirty
     * 		Area of the buffer that may be modified.
     * @return A Canvas used to draw into the surface, or null.
     * @see #lockCanvas()
     * @see #unlockCanvasAndPost(android.graphics.Canvas)
     * @see #isDestroyed()
     */
    public android.graphics.Canvas lockCanvas(android.graphics.Rect dirty) {
        if (mDestroyed) {
            return null;
        }
        if (mCanvas == null) {
            mCanvas = new android.graphics.Canvas();
        }
        if (android.graphics.GraphicBuffer.nLockCanvas(mNativeObject, mCanvas, dirty)) {
            mSaveCount = mCanvas.save();
            return mCanvas;
        }
        return null;
    }

    /**
     * Finish editing pixels in the buffer.
     *
     * <p>This method doesn't do anything if {@link #destroy()} was
     * previously called.</p>
     *
     * @param canvas
     * 		The Canvas previously returned by lockCanvas()
     * @see #lockCanvas()
     * @see #lockCanvas(android.graphics.Rect)
     * @see #isDestroyed()
     */
    public void unlockCanvasAndPost(android.graphics.Canvas canvas) {
        if (((!mDestroyed) && (mCanvas != null)) && (canvas == mCanvas)) {
            canvas.restoreToCount(mSaveCount);
            mSaveCount = 0;
            android.graphics.GraphicBuffer.nUnlockCanvasAndPost(mNativeObject, mCanvas);
        }
    }

    /**
     * Destroyes this buffer immediately. Calling this method frees up any
     * underlying native resources. After calling this method, this buffer
     * must not be used in any way ({@link #lockCanvas()} must not be called,
     * etc.)
     *
     * @see #isDestroyed()
     */
    public void destroy() {
        if (!mDestroyed) {
            mDestroyed = true;
            android.graphics.GraphicBuffer.nDestroyGraphicBuffer(mNativeObject);
        }
    }

    /**
     * Indicates whether this buffer has been destroyed. A destroyed buffer
     * cannot be used in any way: locking a Canvas will return null, the buffer
     * cannot be written to a parcel, etc.
     *
     * @return True if this <code>GraphicBuffer</code> is in a destroyed state,
    false otherwise.
     * @see #destroy()
     */
    public boolean isDestroyed() {
        return mDestroyed;
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (!mDestroyed)
                android.graphics.GraphicBuffer.nDestroyGraphicBuffer(mNativeObject);

        } finally {
            super.finalize();
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * <p>Calling this method will throw an <code>IllegalStateException</code> if
     * {@link #destroy()} has been previously called.</p>
     *
     * @param dest
     * 		The Parcel in which the object should be written.
     * @param flags
     * 		Additional flags about how the object should be written.
     * 		May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (mDestroyed) {
            throw new java.lang.IllegalStateException("This GraphicBuffer has been destroyed and cannot be " + "written to a parcel.");
        }
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        dest.writeInt(mFormat);
        dest.writeInt(mUsage);
        android.graphics.GraphicBuffer.nWriteGraphicBufferToParcel(mNativeObject, dest);
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.graphics.GraphicBuffer> CREATOR = new android.os.Parcelable.Creator<android.graphics.GraphicBuffer>() {
        public android.graphics.GraphicBuffer createFromParcel(android.os.Parcel in) {
            int width = in.readInt();
            int height = in.readInt();
            int format = in.readInt();
            int usage = in.readInt();
            long nativeObject = nReadGraphicBufferFromParcel(in);
            if (nativeObject != 0) {
                return new android.graphics.GraphicBuffer(width, height, format, usage, nativeObject);
            }
            return null;
        }

        public android.graphics.GraphicBuffer[] newArray(int size) {
            return new android.graphics.GraphicBuffer[size];
        }
    };

    private static native long nCreateGraphicBuffer(int width, int height, int format, int usage);

    private static native void nDestroyGraphicBuffer(long nativeObject);

    private static native void nWriteGraphicBufferToParcel(long nativeObject, android.os.Parcel dest);

    private static native long nReadGraphicBufferFromParcel(android.os.Parcel in);

    private static native boolean nLockCanvas(long nativeObject, android.graphics.Canvas canvas, android.graphics.Rect dirty);

    private static native boolean nUnlockCanvasAndPost(long nativeObject, android.graphics.Canvas canvas);

    private static native long nWrapGraphicBuffer(long nativeObject);
}

