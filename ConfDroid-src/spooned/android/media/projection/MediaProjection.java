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
package android.media.projection;


/**
 * A token granting applications the ability to capture screen contents and/or
 * record system audio. The exact capabilities granted depend on the type of
 * MediaProjection.
 *
 * <p>
 * A screen capture session can be started through {@link MediaProjectionManager#createScreenCaptureIntent}. This grants the ability to
 * capture screen contents, but not system audio.
 * </p>
 */
public final class MediaProjection {
    private static final java.lang.String TAG = "MediaProjection";

    private final android.media.projection.IMediaProjection mImpl;

    private final android.content.Context mContext;

    private final java.util.Map<android.media.projection.MediaProjection.Callback, android.media.projection.MediaProjection.CallbackRecord> mCallbacks;

    /**
     *
     *
     * @unknown 
     */
    public MediaProjection(android.content.Context context, android.media.projection.IMediaProjection impl) {
        mCallbacks = new android.util.ArrayMap<android.media.projection.MediaProjection.Callback, android.media.projection.MediaProjection.CallbackRecord>();
        mContext = context;
        mImpl = impl;
        try {
            mImpl.start(new android.media.projection.MediaProjection.MediaProjectionCallback());
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failed to start media projection", e);
        }
    }

    /**
     * Register a listener to receive notifications about when the {@link MediaProjection} changes state.
     *
     * @param callback
     * 		The callback to call.
     * @param handler
     * 		The handler on which the callback should be invoked, or
     * 		null if the callback should be invoked on the calling thread's looper.
     * @see #unregisterCallback
     */
    public void registerCallback(android.media.projection.MediaProjection.Callback callback, android.os.Handler handler) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback should not be null");
        }
        if (handler == null) {
            handler = new android.os.Handler();
        }
        mCallbacks.put(callback, new android.media.projection.MediaProjection.CallbackRecord(callback, handler));
    }

    /**
     * Unregister a MediaProjection listener.
     *
     * @param callback
     * 		The callback to unregister.
     * @see #registerCallback
     */
    public void unregisterCallback(android.media.projection.MediaProjection.Callback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback should not be null");
        }
        mCallbacks.remove(callback);
    }

    /**
     *
     *
     * @unknown 
     */
    public android.hardware.display.VirtualDisplay createVirtualDisplay(@android.annotation.NonNull
    java.lang.String name, int width, int height, int dpi, boolean isSecure, @android.annotation.Nullable
    android.view.Surface surface, @android.annotation.Nullable
    android.hardware.display.VirtualDisplay.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        android.hardware.display.DisplayManager dm = ((android.hardware.display.DisplayManager) (mContext.getSystemService(android.content.Context.DISPLAY_SERVICE)));
        int flags = (isSecure) ? android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_SECURE : 0;
        return dm.createVirtualDisplay(this, name, width, height, dpi, surface, (flags | android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR) | android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION, callback, handler);
    }

    /**
     * Creates a {@link android.hardware.display.VirtualDisplay} to capture the
     * contents of the screen.
     *
     * @param name
     * 		The name of the virtual display, must be non-empty.
     * @param width
     * 		The width of the virtual display in pixels. Must be
     * 		greater than 0.
     * @param height
     * 		The height of the virtual display in pixels. Must be
     * 		greater than 0.
     * @param dpi
     * 		The density of the virtual display in dpi. Must be greater
     * 		than 0.
     * @param surface
     * 		The surface to which the content of the virtual display
     * 		should be rendered, or null if there is none initially.
     * @param flags
     * 		A combination of virtual display flags. See {@link DisplayManager} for the full
     * 		list of flags.
     * @param callback
     * 		Callback to call when the virtual display's state
     * 		changes, or null if none.
     * @param handler
     * 		The {@link android.os.Handler} on which the callback should be
     * 		invoked, or null if the callback should be invoked on the calling
     * 		thread's main {@link android.os.Looper}.
     * @see android.hardware.display.VirtualDisplay
     */
    public android.hardware.display.VirtualDisplay createVirtualDisplay(@android.annotation.NonNull
    java.lang.String name, int width, int height, int dpi, int flags, @android.annotation.Nullable
    android.view.Surface surface, @android.annotation.Nullable
    android.hardware.display.VirtualDisplay.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        android.hardware.display.DisplayManager dm = ((android.hardware.display.DisplayManager) (mContext.getSystemService(android.content.Context.DISPLAY_SERVICE)));
        return dm.createVirtualDisplay(this, name, width, height, dpi, surface, flags, callback, handler);
    }

    /**
     * Creates an AudioRecord to capture audio played back by the system.
     *
     * @unknown 
     */
    public android.media.AudioRecord createAudioRecord(int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes) {
        return null;
    }

    /**
     * Stops projection.
     */
    public void stop() {
        try {
            mImpl.stop();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.projection.MediaProjection.TAG, "Unable to stop projection", e);
        }
    }

    /**
     * Get the underlying IMediaProjection.
     *
     * @unknown 
     */
    public android.media.projection.IMediaProjection getProjection() {
        return mImpl;
    }

    /**
     * Callbacks for the projection session.
     */
    public static abstract class Callback {
        /**
         * Called when the MediaProjection session is no longer valid.
         * <p>
         * Once a MediaProjection has been stopped, it's up to the application to release any
         * resources it may be holding (e.g. {@link android.hardware.display.VirtualDisplay}s).
         * </p>
         */
        public void onStop() {
        }
    }

    private final class MediaProjectionCallback extends android.media.projection.IMediaProjectionCallback.Stub {
        @java.lang.Override
        public void onStop() {
            for (android.media.projection.MediaProjection.CallbackRecord cbr : mCallbacks.values()) {
                cbr.onStop();
            }
        }
    }

    private static final class CallbackRecord {
        private final android.media.projection.MediaProjection.Callback mCallback;

        private final android.os.Handler mHandler;

        public CallbackRecord(android.media.projection.MediaProjection.Callback callback, android.os.Handler handler) {
            mCallback = callback;
            mHandler = handler;
        }

        public void onStop() {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onStop();
                }
            });
        }
    }
}

