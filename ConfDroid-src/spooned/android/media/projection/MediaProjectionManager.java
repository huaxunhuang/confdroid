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
 * Manages the retrieval of certain types of {@link MediaProjection} tokens.
 *
 * <p>
 * Get an instance of this class by calling {@link android.content.Context#getSystemService(java.lang.String)
 * Context.getSystemService()} with the argument {@link android.content.Context#MEDIA_PROJECTION_SERVICE}.
 * </p>
 */
public final class MediaProjectionManager {
    private static final java.lang.String TAG = "MediaProjectionManager";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_APP_TOKEN = "android.media.projection.extra.EXTRA_APP_TOKEN";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_MEDIA_PROJECTION = "android.media.projection.extra.EXTRA_MEDIA_PROJECTION";

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_SCREEN_CAPTURE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_MIRRORING = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_PRESENTATION = 2;

    private android.content.Context mContext;

    private java.util.Map<android.media.projection.MediaProjectionManager.Callback, android.media.projection.MediaProjectionManager.CallbackDelegate> mCallbacks;

    private android.media.projection.IMediaProjectionManager mService;

    /**
     *
     *
     * @unknown 
     */
    public MediaProjectionManager(android.content.Context context) {
        mContext = context;
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.MEDIA_PROJECTION_SERVICE);
        mService = IMediaProjectionManager.Stub.asInterface(b);
        mCallbacks = new android.util.ArrayMap<>();
    }

    /**
     * Returns an Intent that <b>must</b> passed to startActivityForResult()
     * in order to start screen capture. The activity will prompt
     * the user whether to allow screen capture.  The result of this
     * activity should be passed to getMediaProjection.
     */
    public android.content.Intent createScreenCaptureIntent() {
        android.content.Intent i = new android.content.Intent();
        i.setClassName("com.android.systemui", "com.android.systemui.media.MediaProjectionPermissionActivity");
        return i;
    }

    /**
     * Retrieve the MediaProjection obtained from a succesful screen
     * capture request. Will be null if the result from the
     * startActivityForResult() is anything other than RESULT_OK.
     *
     * @param resultCode
     * 		The result code from {@link android.app.Activity#onActivityResult(int,
     * 		int, android.content.Intent)}
     * @param resultData
     * 		The resulting data from {@link android.app.Activity#onActivityResult(int,
     * 		int, android.content.Intent)}
     */
    public android.media.projection.MediaProjection getMediaProjection(int resultCode, @android.annotation.NonNull
    android.content.Intent resultData) {
        if ((resultCode != android.app.Activity.RESULT_OK) || (resultData == null)) {
            return null;
        }
        android.os.IBinder projection = resultData.getIBinderExtra(android.media.projection.MediaProjectionManager.EXTRA_MEDIA_PROJECTION);
        if (projection == null) {
            return null;
        }
        return new android.media.projection.MediaProjection(mContext, IMediaProjection.Stub.asInterface(projection));
    }

    /**
     * Get the {@link MediaProjectionInfo} for the active {@link MediaProjection}.
     *
     * @unknown 
     */
    public android.media.projection.MediaProjectionInfo getActiveProjectionInfo() {
        try {
            return mService.getActiveProjectionInfo();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.projection.MediaProjectionManager.TAG, "Unable to get the active projection info", e);
        }
        return null;
    }

    /**
     * Stop the current projection if there is one.
     *
     * @unknown 
     */
    public void stopActiveProjection() {
        try {
            mService.stopActiveProjection();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.projection.MediaProjectionManager.TAG, "Unable to stop the currently active media projection", e);
        }
    }

    /**
     * Add a callback to monitor all of the {@link MediaProjection}s activity.
     * Not for use by regular applications, must have the MANAGE_MEDIA_PROJECTION permission.
     *
     * @unknown 
     */
    public void addCallback(@android.annotation.NonNull
    android.media.projection.MediaProjectionManager.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        android.media.projection.MediaProjectionManager.CallbackDelegate delegate = new android.media.projection.MediaProjectionManager.CallbackDelegate(callback, handler);
        mCallbacks.put(callback, delegate);
        try {
            mService.addCallback(delegate);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.projection.MediaProjectionManager.TAG, "Unable to add callbacks to MediaProjection service", e);
        }
    }

    /**
     * Remove a MediaProjection monitoring callback.
     *
     * @unknown 
     */
    public void removeCallback(@android.annotation.NonNull
    android.media.projection.MediaProjectionManager.Callback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        android.media.projection.MediaProjectionManager.CallbackDelegate delegate = mCallbacks.remove(callback);
        try {
            if (delegate != null) {
                mService.removeCallback(delegate);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.projection.MediaProjectionManager.TAG, "Unable to add callbacks to MediaProjection service", e);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static abstract class Callback {
        public abstract void onStart(android.media.projection.MediaProjectionInfo info);

        public abstract void onStop(android.media.projection.MediaProjectionInfo info);
    }

    /**
     *
     *
     * @unknown 
     */
    private static final class CallbackDelegate extends android.media.projection.IMediaProjectionWatcherCallback.Stub {
        private android.media.projection.MediaProjectionManager.Callback mCallback;

        private android.os.Handler mHandler;

        public CallbackDelegate(android.media.projection.MediaProjectionManager.Callback callback, android.os.Handler handler) {
            mCallback = callback;
            if (handler == null) {
                handler = new android.os.Handler();
            }
            mHandler = handler;
        }

        @java.lang.Override
        public void onStart(final android.media.projection.MediaProjectionInfo info) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onStart(info);
                }
            });
        }

        @java.lang.Override
        public void onStop(final android.media.projection.MediaProjectionInfo info) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onStop(info);
                }
            });
        }
    }
}

