/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.view;


/**
 * Provides a mechanisms to issue pixel copy requests to allow for copy
 * operations from {@link Surface} to {@link Bitmap}
 */
public final class PixelCopy {
    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.PixelCopy.SUCCESS, android.view.PixelCopy.ERROR_UNKNOWN, android.view.PixelCopy.ERROR_TIMEOUT, android.view.PixelCopy.ERROR_SOURCE_NO_DATA, android.view.PixelCopy.ERROR_SOURCE_INVALID, android.view.PixelCopy.ERROR_DESTINATION_INVALID })
    public @interface CopyResultStatus {}

    /**
     * The pixel copy request succeeded
     */
    public static final int SUCCESS = 0;

    /**
     * The pixel copy request failed with an unknown error.
     */
    public static final int ERROR_UNKNOWN = 1;

    /**
     * A timeout occurred while trying to acquire a buffer from the source to
     * copy from.
     */
    public static final int ERROR_TIMEOUT = 2;

    /**
     * The source has nothing to copy from. When the source is a {@link Surface}
     * this means that no buffers have been queued yet. Wait for the source
     * to produce a frame and try again.
     */
    public static final int ERROR_SOURCE_NO_DATA = 3;

    /**
     * It is not possible to copy from the source. This can happen if the source
     * is hardware-protected or destroyed.
     */
    public static final int ERROR_SOURCE_INVALID = 4;

    /**
     * The destination isn't a valid copy target. If the destination is a bitmap
     * this can occur if the bitmap is too large for the hardware to copy to.
     * It can also occur if the destination has been destroyed.
     */
    public static final int ERROR_DESTINATION_INVALID = 5;

    /**
     * Listener for observing the completion of a PixelCopy request.
     */
    public interface OnPixelCopyFinishedListener {
        /**
         * Callback for when a pixel copy request has completed. This will be called
         * regardless of whether the copy succeeded or failed.
         *
         * @param copyResult
         * 		Contains the resulting status of the copy request.
         * 		This will either be {@link PixelCopy#SUCCESS} or one of the
         * 		<code>PixelCopy.ERROR_*</code> values.
         */
        void onPixelCopyFinished(@android.view.PixelCopy.CopyResultStatus
        int copyResult);
    }

    /**
     * Requests for the display content of a {@link SurfaceView} to be copied
     * into a provided {@link Bitmap}.
     *
     * The contents of the source will be scaled to fit exactly inside the bitmap.
     * The pixel format of the source buffer will be converted, as part of the copy,
     * to fit the the bitmap's {@link Bitmap.Config}. The most recently queued buffer
     * in the SurfaceView's Surface will be used as the source of the copy.
     *
     * @param source
     * 		The source from which to copy
     * @param dest
     * 		The destination of the copy. The source will be scaled to
     * 		match the width, height, and format of this bitmap.
     * @param listener
     * 		Callback for when the pixel copy request completes
     * @param listenerThread
     * 		The callback will be invoked on this Handler when
     * 		the copy is finished.
     */
    public static void request(@android.annotation.NonNull
    android.view.SurfaceView source, @android.annotation.NonNull
    android.graphics.Bitmap dest, @android.annotation.NonNull
    android.view.PixelCopy.OnPixelCopyFinishedListener listener, @android.annotation.NonNull
    android.os.Handler listenerThread) {
        android.view.PixelCopy.request(source.getHolder().getSurface(), dest, listener, listenerThread);
    }

    /**
     * Requests for the display content of a {@link SurfaceView} to be copied
     * into a provided {@link Bitmap}.
     *
     * The contents of the source will be scaled to fit exactly inside the bitmap.
     * The pixel format of the source buffer will be converted, as part of the copy,
     * to fit the the bitmap's {@link Bitmap.Config}. The most recently queued buffer
     * in the SurfaceView's Surface will be used as the source of the copy.
     *
     * @param source
     * 		The source from which to copy
     * @param srcRect
     * 		The area of the source to copy from. If this is null
     * 		the copy area will be the entire surface. The rect will be clamped to
     * 		the bounds of the Surface.
     * @param dest
     * 		The destination of the copy. The source will be scaled to
     * 		match the width, height, and format of this bitmap.
     * @param listener
     * 		Callback for when the pixel copy request completes
     * @param listenerThread
     * 		The callback will be invoked on this Handler when
     * 		the copy is finished.
     */
    public static void request(@android.annotation.NonNull
    android.view.SurfaceView source, @android.annotation.Nullable
    android.graphics.Rect srcRect, @android.annotation.NonNull
    android.graphics.Bitmap dest, @android.annotation.NonNull
    android.view.PixelCopy.OnPixelCopyFinishedListener listener, @android.annotation.NonNull
    android.os.Handler listenerThread) {
        android.view.PixelCopy.request(source.getHolder().getSurface(), srcRect, dest, listener, listenerThread);
    }

    /**
     * Requests a copy of the pixels from a {@link Surface} to be copied into
     * a provided {@link Bitmap}.
     *
     * The contents of the source will be scaled to fit exactly inside the bitmap.
     * The pixel format of the source buffer will be converted, as part of the copy,
     * to fit the the bitmap's {@link Bitmap.Config}. The most recently queued buffer
     * in the Surface will be used as the source of the copy.
     *
     * @param source
     * 		The source from which to copy
     * @param dest
     * 		The destination of the copy. The source will be scaled to
     * 		match the width, height, and format of this bitmap.
     * @param listener
     * 		Callback for when the pixel copy request completes
     * @param listenerThread
     * 		The callback will be invoked on this Handler when
     * 		the copy is finished.
     */
    public static void request(@android.annotation.NonNull
    android.view.Surface source, @android.annotation.NonNull
    android.graphics.Bitmap dest, @android.annotation.NonNull
    android.view.PixelCopy.OnPixelCopyFinishedListener listener, @android.annotation.NonNull
    android.os.Handler listenerThread) {
        android.view.PixelCopy.request(source, null, dest, listener, listenerThread);
    }

    /**
     * Requests a copy of the pixels at the provided {@link Rect} from
     * a {@link Surface} to be copied into a provided {@link Bitmap}.
     *
     * The contents of the source rect will be scaled to fit exactly inside the bitmap.
     * The pixel format of the source buffer will be converted, as part of the copy,
     * to fit the the bitmap's {@link Bitmap.Config}. The most recently queued buffer
     * in the Surface will be used as the source of the copy.
     *
     * @param source
     * 		The source from which to copy
     * @param srcRect
     * 		The area of the source to copy from. If this is null
     * 		the copy area will be the entire surface. The rect will be clamped to
     * 		the bounds of the Surface.
     * @param dest
     * 		The destination of the copy. The source will be scaled to
     * 		match the width, height, and format of this bitmap.
     * @param listener
     * 		Callback for when the pixel copy request completes
     * @param listenerThread
     * 		The callback will be invoked on this Handler when
     * 		the copy is finished.
     */
    public static void request(@android.annotation.NonNull
    android.view.Surface source, @android.annotation.Nullable
    android.graphics.Rect srcRect, @android.annotation.NonNull
    android.graphics.Bitmap dest, @android.annotation.NonNull
    android.view.PixelCopy.OnPixelCopyFinishedListener listener, @android.annotation.NonNull
    android.os.Handler listenerThread) {
        android.view.PixelCopy.validateBitmapDest(dest);
        if (!source.isValid()) {
            throw new java.lang.IllegalArgumentException("Surface isn't valid, source.isValid() == false");
        }
        if ((srcRect != null) && srcRect.isEmpty()) {
            throw new java.lang.IllegalArgumentException("sourceRect is empty");
        }
        // TODO: Make this actually async and fast and cool and stuff
        int result = android.view.ThreadedRenderer.copySurfaceInto(source, srcRect, dest);
        listenerThread.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                listener.onPixelCopyFinished(result);
            }
        });
    }

    /**
     * Requests a copy of the pixels from a {@link Window} to be copied into
     * a provided {@link Bitmap}.
     *
     * The contents of the source will be scaled to fit exactly inside the bitmap.
     * The pixel format of the source buffer will be converted, as part of the copy,
     * to fit the the bitmap's {@link Bitmap.Config}. The most recently queued buffer
     * in the Window's Surface will be used as the source of the copy.
     *
     * Note: This is limited to being able to copy from Window's with a non-null
     * DecorView. If {@link Window#peekDecorView()} is null this throws an
     * {@link IllegalArgumentException}. It will similarly throw an exception
     * if the DecorView has not yet acquired a backing surface. It is recommended
     * that {@link OnDrawListener} is used to ensure that at least one draw
     * has happened before trying to copy from the window, otherwise either
     * an {@link IllegalArgumentException} will be thrown or an error will
     * be returned to the {@link OnPixelCopyFinishedListener}.
     *
     * @param source
     * 		The source from which to copy
     * @param dest
     * 		The destination of the copy. The source will be scaled to
     * 		match the width, height, and format of this bitmap.
     * @param listener
     * 		Callback for when the pixel copy request completes
     * @param listenerThread
     * 		The callback will be invoked on this Handler when
     * 		the copy is finished.
     */
    public static void request(@android.annotation.NonNull
    android.view.Window source, @android.annotation.NonNull
    android.graphics.Bitmap dest, @android.annotation.NonNull
    android.view.PixelCopy.OnPixelCopyFinishedListener listener, @android.annotation.NonNull
    android.os.Handler listenerThread) {
        android.view.PixelCopy.request(source, null, dest, listener, listenerThread);
    }

    /**
     * Requests a copy of the pixels at the provided {@link Rect} from
     * a {@link Window} to be copied into a provided {@link Bitmap}.
     *
     * The contents of the source rect will be scaled to fit exactly inside the bitmap.
     * The pixel format of the source buffer will be converted, as part of the copy,
     * to fit the the bitmap's {@link Bitmap.Config}. The most recently queued buffer
     * in the Window's Surface will be used as the source of the copy.
     *
     * Note: This is limited to being able to copy from Window's with a non-null
     * DecorView. If {@link Window#peekDecorView()} is null this throws an
     * {@link IllegalArgumentException}. It will similarly throw an exception
     * if the DecorView has not yet acquired a backing surface. It is recommended
     * that {@link OnDrawListener} is used to ensure that at least one draw
     * has happened before trying to copy from the window, otherwise either
     * an {@link IllegalArgumentException} will be thrown or an error will
     * be returned to the {@link OnPixelCopyFinishedListener}.
     *
     * @param source
     * 		The source from which to copy
     * @param srcRect
     * 		The area of the source to copy from. If this is null
     * 		the copy area will be the entire surface. The rect will be clamped to
     * 		the bounds of the Surface.
     * @param dest
     * 		The destination of the copy. The source will be scaled to
     * 		match the width, height, and format of this bitmap.
     * @param listener
     * 		Callback for when the pixel copy request completes
     * @param listenerThread
     * 		The callback will be invoked on this Handler when
     * 		the copy is finished.
     */
    public static void request(@android.annotation.NonNull
    android.view.Window source, @android.annotation.Nullable
    android.graphics.Rect srcRect, @android.annotation.NonNull
    android.graphics.Bitmap dest, @android.annotation.NonNull
    android.view.PixelCopy.OnPixelCopyFinishedListener listener, @android.annotation.NonNull
    android.os.Handler listenerThread) {
        android.view.PixelCopy.validateBitmapDest(dest);
        if (source == null) {
            throw new java.lang.IllegalArgumentException("source is null");
        }
        if (source.peekDecorView() == null) {
            throw new java.lang.IllegalArgumentException("Only able to copy windows with decor views");
        }
        android.view.Surface surface = null;
        final android.view.ViewRootImpl root = source.peekDecorView().getViewRootImpl();
        if (root != null) {
            surface = root.mSurface;
            final android.graphics.Rect surfaceInsets = root.mWindowAttributes.surfaceInsets;
            if (srcRect == null) {
                srcRect = new android.graphics.Rect(surfaceInsets.left, surfaceInsets.top, root.mWidth + surfaceInsets.left, root.mHeight + surfaceInsets.top);
            } else {
                srcRect.offset(surfaceInsets.left, surfaceInsets.top);
            }
        }
        if ((surface == null) || (!surface.isValid())) {
            throw new java.lang.IllegalArgumentException("Window doesn't have a backing surface!");
        }
        android.view.PixelCopy.request(surface, srcRect, dest, listener, listenerThread);
    }

    private static void validateBitmapDest(android.graphics.Bitmap bitmap) {
        // TODO: Pre-check max texture dimens if we can
        if (bitmap == null) {
            throw new java.lang.IllegalArgumentException("Bitmap cannot be null");
        }
        if (bitmap.isRecycled()) {
            throw new java.lang.IllegalArgumentException("Bitmap is recycled");
        }
        if (!bitmap.isMutable()) {
            throw new java.lang.IllegalArgumentException("Bitmap is immutable");
        }
    }

    private PixelCopy() {
    }
}

