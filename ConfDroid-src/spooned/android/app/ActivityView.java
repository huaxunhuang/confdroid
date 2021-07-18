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
package android.app;


/**
 *
 *
 * @unknown 
 */
public class ActivityView extends android.view.ViewGroup {
    private static final java.lang.String TAG = "ActivityView";

    private static final boolean DEBUG = false;

    private static final int MSG_SET_SURFACE = 1;

    private static final int CPU_COUNT = java.lang.Runtime.getRuntime().availableProcessors();

    private static final int MINIMUM_POOL_SIZE = 1;

    private static final int MAXIMUM_POOL_SIZE = (android.app.ActivityView.CPU_COUNT * 2) + 1;

    private static final int KEEP_ALIVE = 1;

    private static final java.util.concurrent.ThreadFactory sThreadFactory = new java.util.concurrent.ThreadFactory() {
        private final java.util.concurrent.atomic.AtomicInteger mCount = new java.util.concurrent.atomic.AtomicInteger(1);

        public java.lang.Thread newThread(java.lang.Runnable r) {
            return new java.lang.Thread(r, "ActivityView #" + mCount.getAndIncrement());
        }
    };

    private static final java.util.concurrent.BlockingQueue<java.lang.Runnable> sPoolWorkQueue = new java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    private static final java.util.concurrent.Executor sExecutor = new java.util.concurrent.ThreadPoolExecutor(android.app.ActivityView.MINIMUM_POOL_SIZE, android.app.ActivityView.MAXIMUM_POOL_SIZE, android.app.ActivityView.KEEP_ALIVE, java.util.concurrent.TimeUnit.SECONDS, android.app.ActivityView.sPoolWorkQueue, android.app.ActivityView.sThreadFactory);

    private static class SerialExecutor implements java.util.concurrent.Executor {
        private final java.util.ArrayDeque<java.lang.Runnable> mTasks = new java.util.ArrayDeque<java.lang.Runnable>();

        private java.lang.Runnable mActive;

        public synchronized void execute(final java.lang.Runnable r) {
            mTasks.offer(new java.lang.Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                android.app.ActivityView.sExecutor.execute(mActive);
            }
        }
    }

    private final android.app.ActivityView.SerialExecutor mExecutor = new android.app.ActivityView.SerialExecutor();

    private final int mDensityDpi;

    private final android.view.TextureView mTextureView;

    @com.android.internal.annotations.GuardedBy("mActivityContainerLock")
    private android.app.ActivityView.ActivityContainerWrapper mActivityContainer;

    private java.lang.Object mActivityContainerLock = new java.lang.Object();

    private android.app.Activity mActivity;

    private int mWidth;

    private int mHeight;

    private android.view.Surface mSurface;

    private int mLastVisibility;

    private android.app.ActivityView.ActivityViewCallback mActivityViewCallback;

    public ActivityView(android.content.Context context) {
        this(context, null);
    }

    public ActivityView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        while (context instanceof android.content.ContextWrapper) {
            if (context instanceof android.app.Activity) {
                mActivity = ((android.app.Activity) (context));
                break;
            }
            context = ((android.content.ContextWrapper) (context)).getBaseContext();
        } 
        if (mActivity == null) {
            throw new java.lang.IllegalStateException("The ActivityView's Context is not an Activity.");
        }
        try {
            mActivityContainer = new android.app.ActivityView.ActivityContainerWrapper(android.app.ActivityManagerNative.getDefault().createVirtualActivityContainer(mActivity.getActivityToken(), new android.app.ActivityView.ActivityContainerCallback(this)));
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("ActivityView: Unable to create ActivityContainer. " + e);
        }
        mTextureView = new android.view.TextureView(context);
        mTextureView.setSurfaceTextureListener(new android.app.ActivityView.ActivityViewSurfaceTextureListener());
        addView(mTextureView);
        android.view.WindowManager wm = ((android.view.WindowManager) (mActivity.getSystemService(android.content.Context.WINDOW_SERVICE)));
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mDensityDpi = metrics.densityDpi;
        mLastVisibility = getVisibility();
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, "ctor()");

    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mTextureView.layout(0, 0, r - l, b - t);
    }

    @java.lang.Override
    protected void onVisibilityChanged(android.view.View changedView, final int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if ((mSurface != null) && ((visibility == android.view.View.GONE) || (mLastVisibility == android.view.View.GONE))) {
            if (android.app.ActivityView.DEBUG)
                android.util.Log.v(android.app.ActivityView.TAG, "visibility changed; enqueing runnable");

            final android.view.Surface surface = (visibility == android.view.View.GONE) ? null : mSurface;
            setSurfaceAsync(surface, mWidth, mHeight, mDensityDpi, false);
        }
        mLastVisibility = visibility;
    }

    private boolean injectInputEvent(android.view.InputEvent event) {
        return (mActivityContainer != null) && mActivityContainer.injectEvent(event);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        return injectInputEvent(event) || super.onTouchEvent(event);
    }

    @java.lang.Override
    public boolean onGenericMotionEvent(android.view.MotionEvent event) {
        if (event.isFromSource(android.view.InputDevice.SOURCE_CLASS_POINTER)) {
            if (injectInputEvent(event)) {
                return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, (("onAttachedToWindow(): mActivityContainer=" + mActivityContainer) + " mSurface=") + mSurface);

    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, (("onDetachedFromWindow(): mActivityContainer=" + mActivityContainer) + " mSurface=") + mSurface);

    }

    public boolean isAttachedToDisplay() {
        return mSurface != null;
    }

    public void startActivity(android.content.Intent intent) {
        if (mActivityContainer == null) {
            throw new java.lang.IllegalStateException("Attempt to call startActivity after release");
        }
        if (mSurface == null) {
            throw new java.lang.IllegalStateException("Surface not yet created.");
        }
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, ((("startActivity(): intent=" + intent) + " ") + (isAttachedToDisplay() ? "" : "not")) + " attached");

        if (mActivityContainer.startActivity(intent) == android.app.ActivityManager.START_CANCELED) {
            throw new android.os.OperationCanceledException();
        }
    }

    public void startActivity(android.content.IntentSender intentSender) {
        if (mActivityContainer == null) {
            throw new java.lang.IllegalStateException("Attempt to call startActivity after release");
        }
        if (mSurface == null) {
            throw new java.lang.IllegalStateException("Surface not yet created.");
        }
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, ((("startActivityIntentSender(): intentSender=" + intentSender) + " ") + (isAttachedToDisplay() ? "" : "not")) + " attached");

        final android.content.IIntentSender iIntentSender = intentSender.getTarget();
        if (mActivityContainer.startActivityIntentSender(iIntentSender) == android.app.ActivityManager.START_CANCELED) {
            throw new android.os.OperationCanceledException();
        }
    }

    public void startActivity(android.app.PendingIntent pendingIntent) {
        if (mActivityContainer == null) {
            throw new java.lang.IllegalStateException("Attempt to call startActivity after release");
        }
        if (mSurface == null) {
            throw new java.lang.IllegalStateException("Surface not yet created.");
        }
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, ((("startActivityPendingIntent(): PendingIntent=" + pendingIntent) + " ") + (isAttachedToDisplay() ? "" : "not")) + " attached");

        final android.content.IIntentSender iIntentSender = pendingIntent.getTarget();
        if (mActivityContainer.startActivityIntentSender(iIntentSender) == android.app.ActivityManager.START_CANCELED) {
            throw new android.os.OperationCanceledException();
        }
    }

    public void release() {
        if (android.app.ActivityView.DEBUG)
            android.util.Log.v(android.app.ActivityView.TAG, (("release() mActivityContainer=" + mActivityContainer) + " mSurface=") + mSurface);

        if (mActivityContainer == null) {
            android.util.Log.e(android.app.ActivityView.TAG, "Duplicate call to release");
            return;
        }
        synchronized(mActivityContainerLock) {
            mActivityContainer.release();
            mActivityContainer = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        mTextureView.setSurfaceTextureListener(null);
    }

    private void setSurfaceAsync(final android.view.Surface surface, final int width, final int height, final int densityDpi, final boolean callback) {
        mExecutor.execute(new java.lang.Runnable() {
            public void run() {
                try {
                    synchronized(mActivityContainerLock) {
                        if (mActivityContainer != null) {
                            mActivityContainer.setSurface(surface, width, height, densityDpi);
                        }
                    }
                } catch (android.os.RemoteException e) {
                    throw new java.lang.RuntimeException("ActivityView: Unable to set surface of ActivityContainer. ", e);
                }
                if (callback) {
                    post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            if (mActivityViewCallback != null) {
                                if (surface != null) {
                                    mActivityViewCallback.onSurfaceAvailable(android.app.ActivityView.this);
                                } else {
                                    mActivityViewCallback.onSurfaceDestroyed(android.app.ActivityView.this);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Set the callback to use to report certain state changes.
     *
     * Note: If the surface has been created prior to this call being made, then
     * ActivityViewCallback.onSurfaceAvailable will be called from within setCallback.
     *
     * @param callback
     * 		The callback to report events to.
     * @see ActivityViewCallback
     */
    public void setCallback(android.app.ActivityView.ActivityViewCallback callback) {
        mActivityViewCallback = callback;
        if (mSurface != null) {
            mActivityViewCallback.onSurfaceAvailable(this);
        }
    }

    public static abstract class ActivityViewCallback {
        /**
         * Called when all activities in the ActivityView have completed and been removed. Register
         * using {@link ActivityView#setCallback(ActivityViewCallback)}. Each ActivityView may
         * have at most one callback registered.
         */
        public abstract void onAllActivitiesComplete(android.app.ActivityView view);

        /**
         * Called when the surface is ready to be drawn to. Calling startActivity prior to this
         * callback will result in an IllegalStateException.
         */
        public abstract void onSurfaceAvailable(android.app.ActivityView view);

        /**
         * Called when the surface has been removed. Calling startActivity after this callback
         * will result in an IllegalStateException.
         */
        public abstract void onSurfaceDestroyed(android.app.ActivityView view);
    }

    private class ActivityViewSurfaceTextureListener implements android.view.TextureView.SurfaceTextureListener {
        @java.lang.Override
        public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture surfaceTexture, int width, int height) {
            if (mActivityContainer == null) {
                return;
            }
            if (android.app.ActivityView.DEBUG)
                android.util.Log.d(android.app.ActivityView.TAG, (("onSurfaceTextureAvailable: width=" + width) + " height=") + height);

            mWidth = width;
            mHeight = height;
            mSurface = new android.view.Surface(surfaceTexture);
            setSurfaceAsync(mSurface, mWidth, mHeight, mDensityDpi, true);
        }

        @java.lang.Override
        public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture surfaceTexture, int width, int height) {
            if (mActivityContainer == null) {
                return;
            }
            if (android.app.ActivityView.DEBUG)
                android.util.Log.d(android.app.ActivityView.TAG, (("onSurfaceTextureSizeChanged: w=" + width) + " h=") + height);

        }

        @java.lang.Override
        public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture surfaceTexture) {
            if (mActivityContainer == null) {
                return true;
            }
            if (android.app.ActivityView.DEBUG)
                android.util.Log.d(android.app.ActivityView.TAG, "onSurfaceTextureDestroyed");

            mSurface.release();
            mSurface = null;
            setSurfaceAsync(null, mWidth, mHeight, mDensityDpi, true);
            return true;
        }

        @java.lang.Override
        public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surfaceTexture) {
            // Log.d(TAG, "onSurfaceTextureUpdated");
        }
    }

    private static class ActivityContainerCallback extends android.app.IActivityContainerCallback.Stub {
        private final java.lang.ref.WeakReference<android.app.ActivityView> mActivityViewWeakReference;

        ActivityContainerCallback(android.app.ActivityView activityView) {
            mActivityViewWeakReference = new java.lang.ref.WeakReference<>(activityView);
        }

        @java.lang.Override
        public void setVisible(android.os.IBinder container, boolean visible) {
            if (android.app.ActivityView.DEBUG)
                android.util.Log.v(android.app.ActivityView.TAG, (((("setVisible(): container=" + container) + " visible=") + visible) + " ActivityView=") + mActivityViewWeakReference.get());

        }

        @java.lang.Override
        public void onAllActivitiesComplete(android.os.IBinder container) {
            final android.app.ActivityView activityView = mActivityViewWeakReference.get();
            if (activityView != null) {
                final android.app.ActivityView.ActivityViewCallback callback = activityView.mActivityViewCallback;
                if (callback != null) {
                    final java.lang.ref.WeakReference<android.app.ActivityView.ActivityViewCallback> callbackRef = new java.lang.ref.WeakReference<>(callback);
                    activityView.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            android.app.ActivityView.ActivityViewCallback callback = callbackRef.get();
                            if (callback != null) {
                                callback.onAllActivitiesComplete(activityView);
                            }
                        }
                    });
                }
            }
        }
    }

    private static class ActivityContainerWrapper {
        private final android.app.IActivityContainer mIActivityContainer;

        private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

        boolean mOpened;// Protected by mGuard.


        ActivityContainerWrapper(android.app.IActivityContainer container) {
            mIActivityContainer = container;
            mOpened = true;
            mGuard.open("release");
        }

        void attachToDisplay(int displayId) {
            try {
                mIActivityContainer.attachToDisplay(displayId);
            } catch (android.os.RemoteException e) {
            }
        }

        void setSurface(android.view.Surface surface, int width, int height, int density) throws android.os.RemoteException {
            mIActivityContainer.setSurface(surface, width, height, density);
        }

        int startActivity(android.content.Intent intent) {
            try {
                return mIActivityContainer.startActivity(intent);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("ActivityView: Unable to startActivity. " + e);
            }
        }

        int startActivityIntentSender(android.content.IIntentSender intentSender) {
            try {
                return mIActivityContainer.startActivityIntentSender(intentSender);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("ActivityView: Unable to startActivity from IntentSender. " + e);
            }
        }

        int getDisplayId() {
            try {
                return mIActivityContainer.getDisplayId();
            } catch (android.os.RemoteException e) {
                return -1;
            }
        }

        boolean injectEvent(android.view.InputEvent event) {
            try {
                return mIActivityContainer.injectEvent(event);
            } catch (android.os.RemoteException e) {
                return false;
            }
        }

        void release() {
            synchronized(mGuard) {
                if (mOpened) {
                    if (android.app.ActivityView.DEBUG)
                        android.util.Log.v(android.app.ActivityView.TAG, "ActivityContainerWrapper: release called");

                    try {
                        mIActivityContainer.release();
                        mGuard.close();
                    } catch (android.os.RemoteException e) {
                    }
                    mOpened = false;
                }
            }
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            if (android.app.ActivityView.DEBUG)
                android.util.Log.v(android.app.ActivityView.TAG, "ActivityContainerWrapper: finalize called");

            try {
                if (mGuard != null) {
                    mGuard.warnIfOpen();
                    release();
                }
            } finally {
                super.finalize();
            }
        }
    }
}

