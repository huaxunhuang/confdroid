/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.service.wallpaper;


/**
 * A wallpaper service is responsible for showing a live wallpaper behind
 * applications that would like to sit on top of it.  This service object
 * itself does very little -- its only purpose is to generate instances of
 * {@link Engine} as needed.  Implementing a wallpaper thus
 * involves subclassing from this, subclassing an Engine implementation,
 * and implementing {@link #onCreateEngine()} to return a new instance of
 * your engine.
 */
public abstract class WallpaperService extends android.app.Service {
    /**
     * The {@link Intent} that must be declared as handled by the service.
     * To be supported, the service must also require the
     * {@link android.Manifest.permission#BIND_WALLPAPER} permission so
     * that other applications can not abuse it.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.service.wallpaper.WallpaperService";

    /**
     * Name under which a WallpaperService component publishes information
     * about itself.  This meta-data must reference an XML resource containing
     * a <code>&lt;{@link android.R.styleable#Wallpaper wallpaper}&gt;</code>
     * tag.
     */
    public static final java.lang.String SERVICE_META_DATA = "android.service.wallpaper";

    static final java.lang.String TAG = "WallpaperService";

    static final boolean DEBUG = false;

    private static final int DO_ATTACH = 10;

    private static final int DO_DETACH = 20;

    private static final int DO_SET_DESIRED_SIZE = 30;

    private static final int DO_SET_DISPLAY_PADDING = 40;

    private static final int MSG_UPDATE_SURFACE = 10000;

    private static final int MSG_VISIBILITY_CHANGED = 10010;

    private static final int MSG_WALLPAPER_OFFSETS = 10020;

    private static final int MSG_WALLPAPER_COMMAND = 10025;

    private static final int MSG_WINDOW_RESIZED = 10030;

    private static final int MSG_WINDOW_MOVED = 10035;

    private static final int MSG_TOUCH_EVENT = 10040;

    private final java.util.ArrayList<android.service.wallpaper.WallpaperService.Engine> mActiveEngines = new java.util.ArrayList<android.service.wallpaper.WallpaperService.Engine>();

    static final class WallpaperCommand {
        java.lang.String action;

        int x;

        int y;

        int z;

        android.os.Bundle extras;

        boolean sync;
    }

    /**
     * The actual implementation of a wallpaper.  A wallpaper service may
     * have multiple instances running (for example as a real wallpaper
     * and as a preview), each of which is represented by its own Engine
     * instance.  You must implement {@link WallpaperService#onCreateEngine()}
     * to return your concrete Engine implementation.
     */
    public class Engine {
        android.service.wallpaper.WallpaperService.IWallpaperEngineWrapper mIWallpaperEngine;

        // Copies from mIWallpaperEngine.
        com.android.internal.os.HandlerCaller mCaller;

        android.service.wallpaper.IWallpaperConnection mConnection;

        android.os.IBinder mWindowToken;

        boolean mInitializing = true;

        boolean mVisible;

        boolean mReportedVisible;

        boolean mDestroyed;

        // Current window state.
        boolean mCreated;

        boolean mSurfaceCreated;

        boolean mIsCreating;

        boolean mDrawingAllowed;

        boolean mOffsetsChanged;

        boolean mFixedSizeAllowed;

        int mWidth;

        int mHeight;

        int mFormat;

        int mType;

        int mCurWidth;

        int mCurHeight;

        int mWindowFlags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        int mWindowPrivateFlags = android.view.WindowManager.LayoutParams.PRIVATE_FLAG_WANTS_OFFSET_NOTIFICATIONS;

        int mCurWindowFlags = mWindowFlags;

        int mCurWindowPrivateFlags = mWindowPrivateFlags;

        final android.graphics.Rect mVisibleInsets = new android.graphics.Rect();

        final android.graphics.Rect mWinFrame = new android.graphics.Rect();

        final android.graphics.Rect mOverscanInsets = new android.graphics.Rect();

        final android.graphics.Rect mContentInsets = new android.graphics.Rect();

        final android.graphics.Rect mStableInsets = new android.graphics.Rect();

        final android.graphics.Rect mOutsets = new android.graphics.Rect();

        final android.graphics.Rect mDispatchedOverscanInsets = new android.graphics.Rect();

        final android.graphics.Rect mDispatchedContentInsets = new android.graphics.Rect();

        final android.graphics.Rect mDispatchedStableInsets = new android.graphics.Rect();

        final android.graphics.Rect mDispatchedOutsets = new android.graphics.Rect();

        final android.graphics.Rect mFinalSystemInsets = new android.graphics.Rect();

        final android.graphics.Rect mFinalStableInsets = new android.graphics.Rect();

        final android.graphics.Rect mBackdropFrame = new android.graphics.Rect();

        final android.content.res.Configuration mConfiguration = new android.content.res.Configuration();

        final android.view.WindowManager.LayoutParams mLayout = new android.view.WindowManager.LayoutParams();

        android.view.IWindowSession mSession;

        android.view.InputChannel mInputChannel;

        final java.lang.Object mLock = new java.lang.Object();

        boolean mOffsetMessageEnqueued;

        float mPendingXOffset;

        float mPendingYOffset;

        float mPendingXOffsetStep;

        float mPendingYOffsetStep;

        boolean mPendingSync;

        android.view.MotionEvent mPendingMove;

        android.hardware.display.DisplayManager mDisplayManager;

        android.view.Display mDisplay;

        private int mDisplayState;

        final com.android.internal.view.BaseSurfaceHolder mSurfaceHolder = new com.android.internal.view.BaseSurfaceHolder() {
            {
                mRequestedFormat = android.graphics.PixelFormat.RGBX_8888;
            }

            @java.lang.Override
            public boolean onAllowLockCanvas() {
                return mDrawingAllowed;
            }

            @java.lang.Override
            public void onRelayoutContainer() {
                android.os.Message msg = mCaller.obtainMessage(android.service.wallpaper.WallpaperService.MSG_UPDATE_SURFACE);
                mCaller.sendMessage(msg);
            }

            @java.lang.Override
            public void onUpdateSurface() {
                android.os.Message msg = mCaller.obtainMessage(android.service.wallpaper.WallpaperService.MSG_UPDATE_SURFACE);
                mCaller.sendMessage(msg);
            }

            public boolean isCreating() {
                return mIsCreating;
            }

            @java.lang.Override
            public void setFixedSize(int width, int height) {
                if (!mFixedSizeAllowed) {
                    // Regular apps can't do this.  It can only work for
                    // certain designs of window animations, so you can't
                    // rely on it.
                    throw new java.lang.UnsupportedOperationException("Wallpapers currently only support sizing from layout");
                }
                setFixedSize(width, height);
            }

            public void setKeepScreenOn(boolean screenOn) {
                throw new java.lang.UnsupportedOperationException("Wallpapers do not support keep screen on");
            }

            @java.lang.Override
            public android.graphics.Canvas lockCanvas() {
                if ((mDisplayState == android.view.Display.STATE_DOZE) || (mDisplayState == android.view.Display.STATE_DOZE_SUSPEND)) {
                    try {
                        mSession.pokeDrawLock(mWindow);
                    } catch (android.os.RemoteException e) {
                        // System server died, can be ignored.
                    }
                }
                return lockCanvas();
            }
        };

        final class WallpaperInputEventReceiver extends android.view.InputEventReceiver {
            public WallpaperInputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper) {
                super(inputChannel, looper);
            }

            @java.lang.Override
            public void onInputEvent(android.view.InputEvent event) {
                boolean handled = false;
                try {
                    if ((event instanceof android.view.MotionEvent) && ((event.getSource() & android.view.InputDevice.SOURCE_CLASS_POINTER) != 0)) {
                        android.view.MotionEvent dup = android.view.MotionEvent.obtainNoHistory(((android.view.MotionEvent) (event)));
                        dispatchPointer(dup);
                        handled = true;
                    }
                } finally {
                    finishInputEvent(event, handled);
                }
            }
        }

        android.service.wallpaper.WallpaperService.Engine.WallpaperInputEventReceiver mInputEventReceiver;

        final com.android.internal.view.BaseIWindow mWindow = new com.android.internal.view.BaseIWindow() {
            @java.lang.Override
            public void resized(android.graphics.Rect frame, android.graphics.Rect overscanInsets, android.graphics.Rect contentInsets, android.graphics.Rect visibleInsets, android.graphics.Rect stableInsets, android.graphics.Rect outsets, boolean reportDraw, android.content.res.Configuration newConfig, android.graphics.Rect backDropRect, boolean forceLayout, boolean alwaysConsumeNavBar) {
                android.os.Message msg = mCaller.obtainMessageIO(android.service.wallpaper.WallpaperService.MSG_WINDOW_RESIZED, reportDraw ? 1 : 0, outsets);
                mCaller.sendMessage(msg);
            }

            @java.lang.Override
            public void moved(int newX, int newY) {
                android.os.Message msg = mCaller.obtainMessageII(android.service.wallpaper.WallpaperService.MSG_WINDOW_MOVED, newX, newY);
                mCaller.sendMessage(msg);
            }

            @java.lang.Override
            public void dispatchAppVisibility(boolean visible) {
                // We don't do this in preview mode; we'll let the preview
                // activity tell us when to run.
                if (!mIWallpaperEngine.mIsPreview) {
                    android.os.Message msg = mCaller.obtainMessageI(android.service.wallpaper.WallpaperService.MSG_VISIBILITY_CHANGED, visible ? 1 : 0);
                    mCaller.sendMessage(msg);
                }
            }

            @java.lang.Override
            public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
                synchronized(mLock) {
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("Dispatch wallpaper offsets: " + x) + ", ") + y);

                    mPendingXOffset = x;
                    mPendingYOffset = y;
                    mPendingXOffsetStep = xStep;
                    mPendingYOffsetStep = yStep;
                    if (sync) {
                        mPendingSync = true;
                    }
                    if (!mOffsetMessageEnqueued) {
                        mOffsetMessageEnqueued = true;
                        android.os.Message msg = mCaller.obtainMessage(android.service.wallpaper.WallpaperService.MSG_WALLPAPER_OFFSETS);
                        mCaller.sendMessage(msg);
                    }
                }
            }

            @java.lang.Override
            public void dispatchWallpaperCommand(java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) {
                synchronized(mLock) {
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("Dispatch wallpaper command: " + x) + ", ") + y);

                    android.service.wallpaper.WallpaperService.WallpaperCommand cmd = new android.service.wallpaper.WallpaperService.WallpaperCommand();
                    cmd.action = action;
                    cmd.x = x;
                    cmd.y = y;
                    cmd.z = z;
                    cmd.extras = extras;
                    cmd.sync = sync;
                    android.os.Message msg = mCaller.obtainMessage(android.service.wallpaper.WallpaperService.MSG_WALLPAPER_COMMAND);
                    msg.obj = cmd;
                    mCaller.sendMessage(msg);
                }
            }
        };

        /**
         * Provides access to the surface in which this wallpaper is drawn.
         */
        public android.view.SurfaceHolder getSurfaceHolder() {
            return mSurfaceHolder;
        }

        /**
         * Convenience for {@link WallpaperManager#getDesiredMinimumWidth()
         * WallpaperManager.getDesiredMinimumWidth()}, returning the width
         * that the system would like this wallpaper to run in.
         */
        public int getDesiredMinimumWidth() {
            return mIWallpaperEngine.mReqWidth;
        }

        /**
         * Convenience for {@link WallpaperManager#getDesiredMinimumHeight()
         * WallpaperManager.getDesiredMinimumHeight()}, returning the height
         * that the system would like this wallpaper to run in.
         */
        public int getDesiredMinimumHeight() {
            return mIWallpaperEngine.mReqHeight;
        }

        /**
         * Return whether the wallpaper is currently visible to the user,
         * this is the last value supplied to
         * {@link #onVisibilityChanged(boolean)}.
         */
        public boolean isVisible() {
            return mReportedVisible;
        }

        /**
         * Returns true if this engine is running in preview mode -- that is,
         * it is being shown to the user before they select it as the actual
         * wallpaper.
         */
        public boolean isPreview() {
            return mIWallpaperEngine.mIsPreview;
        }

        /**
         * Control whether this wallpaper will receive raw touch events
         * from the window manager as the user interacts with the window
         * that is currently displaying the wallpaper.  By default they
         * are turned off.  If enabled, the events will be received in
         * {@link #onTouchEvent(MotionEvent)}.
         */
        public void setTouchEventsEnabled(boolean enabled) {
            mWindowFlags = (enabled) ? mWindowFlags & (~android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) : mWindowFlags | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            if (mCreated) {
                updateSurface(false, false, false);
            }
        }

        /**
         * Control whether this wallpaper will receive notifications when the wallpaper
         * has been scrolled. By default, wallpapers will receive notifications, although
         * the default static image wallpapers do not. It is a performance optimization to
         * set this to false.
         *
         * @param enabled
         * 		whether the wallpaper wants to receive offset notifications
         */
        public void setOffsetNotificationsEnabled(boolean enabled) {
            mWindowPrivateFlags = (enabled) ? mWindowPrivateFlags | android.view.WindowManager.LayoutParams.PRIVATE_FLAG_WANTS_OFFSET_NOTIFICATIONS : mWindowPrivateFlags & (~android.view.WindowManager.LayoutParams.PRIVATE_FLAG_WANTS_OFFSET_NOTIFICATIONS);
            if (mCreated) {
                updateSurface(false, false, false);
            }
        }

        /**
         * {@hide }
         */
        public void setFixedSizeAllowed(boolean allowed) {
            mFixedSizeAllowed = allowed;
        }

        /**
         * Called once to initialize the engine.  After returning, the
         * engine's surface will be created by the framework.
         */
        public void onCreate(android.view.SurfaceHolder surfaceHolder) {
        }

        /**
         * Called right before the engine is going away.  After this the
         * surface will be destroyed and this Engine object is no longer
         * valid.
         */
        public void onDestroy() {
        }

        /**
         * Called to inform you of the wallpaper becoming visible or
         * hidden.  <em>It is very important that a wallpaper only use
         * CPU while it is visible.</em>.
         */
        public void onVisibilityChanged(boolean visible) {
        }

        /**
         * Called with the current insets that are in effect for the wallpaper.
         * This gives you the part of the overall wallpaper surface that will
         * generally be visible to the user (ignoring position offsets applied to it).
         *
         * @param insets
         * 		Insets to apply.
         */
        public void onApplyWindowInsets(android.view.WindowInsets insets) {
        }

        /**
         * Called as the user performs touch-screen interaction with the
         * window that is currently showing this wallpaper.  Note that the
         * events you receive here are driven by the actual application the
         * user is interacting with, so if it is slow you will get fewer
         * move events.
         */
        public void onTouchEvent(android.view.MotionEvent event) {
        }

        /**
         * Called to inform you of the wallpaper's offsets changing
         * within its contain, corresponding to the container's
         * call to {@link WallpaperManager#setWallpaperOffsets(IBinder, float, float)
         * WallpaperManager.setWallpaperOffsets()}.
         */
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        }

        /**
         * Process a command that was sent to the wallpaper with
         * {@link WallpaperManager#sendWallpaperCommand}.
         * The default implementation does nothing, and always returns null
         * as the result.
         *
         * @param action
         * 		The name of the command to perform.  This tells you
         * 		what to do and how to interpret the rest of the arguments.
         * @param x
         * 		Generic integer parameter.
         * @param y
         * 		Generic integer parameter.
         * @param z
         * 		Generic integer parameter.
         * @param extras
         * 		Any additional parameters.
         * @param resultRequested
         * 		If true, the caller is requesting that
         * 		a result, appropriate for the command, be returned back.
         * @return If returning a result, create a Bundle and place the
        result data in to it.  Otherwise return null.
         */
        public android.os.Bundle onCommand(java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean resultRequested) {
            return null;
        }

        /**
         * Called when an application has changed the desired virtual size of
         * the wallpaper.
         */
        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
        }

        /**
         * Convenience for {@link SurfaceHolder.Callback#surfaceChanged
         * SurfaceHolder.Callback.surfaceChanged()}.
         */
        public void onSurfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
        }

        /**
         * Convenience for {@link SurfaceHolder.Callback2#surfaceRedrawNeeded
         * SurfaceHolder.Callback.surfaceRedrawNeeded()}.
         */
        public void onSurfaceRedrawNeeded(android.view.SurfaceHolder holder) {
        }

        /**
         * Convenience for {@link SurfaceHolder.Callback#surfaceCreated
         * SurfaceHolder.Callback.surfaceCreated()}.
         */
        public void onSurfaceCreated(android.view.SurfaceHolder holder) {
        }

        /**
         * Convenience for {@link SurfaceHolder.Callback#surfaceDestroyed
         * SurfaceHolder.Callback.surfaceDestroyed()}.
         */
        public void onSurfaceDestroyed(android.view.SurfaceHolder holder) {
        }

        protected void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter out, java.lang.String[] args) {
            out.print(prefix);
            out.print("mInitializing=");
            out.print(mInitializing);
            out.print(" mDestroyed=");
            out.println(mDestroyed);
            out.print(prefix);
            out.print("mVisible=");
            out.print(mVisible);
            out.print(" mReportedVisible=");
            out.println(mReportedVisible);
            out.print(prefix);
            out.print("mDisplay=");
            out.println(mDisplay);
            out.print(prefix);
            out.print("mCreated=");
            out.print(mCreated);
            out.print(" mSurfaceCreated=");
            out.print(mSurfaceCreated);
            out.print(" mIsCreating=");
            out.print(mIsCreating);
            out.print(" mDrawingAllowed=");
            out.println(mDrawingAllowed);
            out.print(prefix);
            out.print("mWidth=");
            out.print(mWidth);
            out.print(" mCurWidth=");
            out.print(mCurWidth);
            out.print(" mHeight=");
            out.print(mHeight);
            out.print(" mCurHeight=");
            out.println(mCurHeight);
            out.print(prefix);
            out.print("mType=");
            out.print(mType);
            out.print(" mWindowFlags=");
            out.print(mWindowFlags);
            out.print(" mCurWindowFlags=");
            out.println(mCurWindowFlags);
            out.print(prefix);
            out.print("mWindowPrivateFlags=");
            out.print(mWindowPrivateFlags);
            out.print(" mCurWindowPrivateFlags=");
            out.println(mCurWindowPrivateFlags);
            out.print(prefix);
            out.print("mVisibleInsets=");
            out.print(mVisibleInsets.toShortString());
            out.print(" mWinFrame=");
            out.print(mWinFrame.toShortString());
            out.print(" mContentInsets=");
            out.println(mContentInsets.toShortString());
            out.print(prefix);
            out.print("mConfiguration=");
            out.println(mConfiguration);
            out.print(prefix);
            out.print("mLayout=");
            out.println(mLayout);
            synchronized(mLock) {
                out.print(prefix);
                out.print("mPendingXOffset=");
                out.print(mPendingXOffset);
                out.print(" mPendingXOffset=");
                out.println(mPendingXOffset);
                out.print(prefix);
                out.print("mPendingXOffsetStep=");
                out.print(mPendingXOffsetStep);
                out.print(" mPendingXOffsetStep=");
                out.println(mPendingXOffsetStep);
                out.print(prefix);
                out.print("mOffsetMessageEnqueued=");
                out.print(mOffsetMessageEnqueued);
                out.print(" mPendingSync=");
                out.println(mPendingSync);
                if (mPendingMove != null) {
                    out.print(prefix);
                    out.print("mPendingMove=");
                    out.println(mPendingMove);
                }
            }
        }

        private void dispatchPointer(android.view.MotionEvent event) {
            if (event.isTouchEvent()) {
                synchronized(mLock) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_MOVE) {
                        mPendingMove = event;
                    } else {
                        mPendingMove = null;
                    }
                }
                android.os.Message msg = mCaller.obtainMessageO(android.service.wallpaper.WallpaperService.MSG_TOUCH_EVENT, event);
                mCaller.sendMessage(msg);
            } else {
                event.recycle();
            }
        }

        void updateSurface(boolean forceRelayout, boolean forceReport, boolean redrawNeeded) {
            if (mDestroyed) {
                android.util.Log.w(android.service.wallpaper.WallpaperService.TAG, "Ignoring updateSurface: destroyed");
            }
            boolean fixedSize = false;
            int myWidth = mSurfaceHolder.getRequestedWidth();
            if (myWidth <= 0)
                myWidth = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            else
                fixedSize = true;

            int myHeight = mSurfaceHolder.getRequestedHeight();
            if (myHeight <= 0)
                myHeight = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            else
                fixedSize = true;

            final boolean creating = !mCreated;
            final boolean surfaceCreating = !mSurfaceCreated;
            final boolean formatChanged = mFormat != mSurfaceHolder.getRequestedFormat();
            boolean sizeChanged = (mWidth != myWidth) || (mHeight != myHeight);
            boolean insetsChanged = !mCreated;
            final boolean typeChanged = mType != mSurfaceHolder.getRequestedType();
            final boolean flagsChanged = (mCurWindowFlags != mWindowFlags) || (mCurWindowPrivateFlags != mWindowPrivateFlags);
            if ((((((((forceRelayout || creating) || surfaceCreating) || formatChanged) || sizeChanged) || typeChanged) || flagsChanged) || redrawNeeded) || (!mIWallpaperEngine.mShownReported)) {
                if (android.service.wallpaper.WallpaperService.DEBUG)
                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (((("Changes: creating=" + creating) + " format=") + formatChanged) + " size=") + sizeChanged);

                try {
                    mWidth = myWidth;
                    mHeight = myHeight;
                    mFormat = mSurfaceHolder.getRequestedFormat();
                    mType = mSurfaceHolder.getRequestedType();
                    mLayout.x = 0;
                    mLayout.y = 0;
                    mLayout.width = myWidth;
                    mLayout.height = myHeight;
                    mLayout.format = mFormat;
                    mCurWindowFlags = mWindowFlags;
                    mLayout.flags = (((mWindowFlags | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR) | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN) | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    mCurWindowPrivateFlags = mWindowPrivateFlags;
                    mLayout.privateFlags = mWindowPrivateFlags;
                    mLayout.memoryType = mType;
                    mLayout.token = mWindowToken;
                    if (!mCreated) {
                        // Retrieve watch round info
                        android.content.res.TypedArray windowStyle = obtainStyledAttributes(com.android.internal.R.styleable.Window);
                        windowStyle.recycle();
                        // Add window
                        mLayout.type = mIWallpaperEngine.mWindowType;
                        mLayout.gravity = android.view.Gravity.START | android.view.Gravity.TOP;
                        mLayout.setTitle(android.service.wallpaper.WallpaperService.this.getClass().getName());
                        mLayout.windowAnimations = com.android.internal.R.style.Animation_Wallpaper;
                        mInputChannel = new android.view.InputChannel();
                        if (mSession.addToDisplay(mWindow, mWindow.mSeq, mLayout, android.view.View.VISIBLE, android.view.Display.DEFAULT_DISPLAY, mContentInsets, mStableInsets, mOutsets, mInputChannel) < 0) {
                            android.util.Log.w(android.service.wallpaper.WallpaperService.TAG, "Failed to add window while updating wallpaper surface.");
                            return;
                        }
                        mCreated = true;
                        mInputEventReceiver = new android.service.wallpaper.WallpaperService.Engine.WallpaperInputEventReceiver(mInputChannel, android.os.Looper.myLooper());
                    }
                    mSurfaceHolder.mSurfaceLock.lock();
                    mDrawingAllowed = true;
                    if (!fixedSize) {
                        mLayout.surfaceInsets.set(mIWallpaperEngine.mDisplayPadding);
                        mLayout.surfaceInsets.left += mOutsets.left;
                        mLayout.surfaceInsets.top += mOutsets.top;
                        mLayout.surfaceInsets.right += mOutsets.right;
                        mLayout.surfaceInsets.bottom += mOutsets.bottom;
                    } else {
                        mLayout.surfaceInsets.set(0, 0, 0, 0);
                    }
                    final int relayoutResult = mSession.relayout(mWindow, mWindow.mSeq, mLayout, mWidth, mHeight, android.view.View.VISIBLE, 0, mWinFrame, mOverscanInsets, mContentInsets, mVisibleInsets, mStableInsets, mOutsets, mBackdropFrame, mConfiguration, mSurfaceHolder.mSurface);
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("New surface: " + mSurfaceHolder.mSurface) + ", frame=") + mWinFrame);

                    int w = mWinFrame.width();
                    int h = mWinFrame.height();
                    if (!fixedSize) {
                        final android.graphics.Rect padding = mIWallpaperEngine.mDisplayPadding;
                        w += ((padding.left + padding.right) + mOutsets.left) + mOutsets.right;
                        h += ((padding.top + padding.bottom) + mOutsets.top) + mOutsets.bottom;
                        mOverscanInsets.left += padding.left;
                        mOverscanInsets.top += padding.top;
                        mOverscanInsets.right += padding.right;
                        mOverscanInsets.bottom += padding.bottom;
                        mContentInsets.left += padding.left;
                        mContentInsets.top += padding.top;
                        mContentInsets.right += padding.right;
                        mContentInsets.bottom += padding.bottom;
                        mStableInsets.left += padding.left;
                        mStableInsets.top += padding.top;
                        mStableInsets.right += padding.right;
                        mStableInsets.bottom += padding.bottom;
                    }
                    if (mCurWidth != w) {
                        sizeChanged = true;
                        mCurWidth = w;
                    }
                    if (mCurHeight != h) {
                        sizeChanged = true;
                        mCurHeight = h;
                    }
                    if (android.service.wallpaper.WallpaperService.DEBUG) {
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("Wallpaper size has changed: (" + mCurWidth) + ", ") + mCurHeight);
                    }
                    insetsChanged |= !mDispatchedOverscanInsets.equals(mOverscanInsets);
                    insetsChanged |= !mDispatchedContentInsets.equals(mContentInsets);
                    insetsChanged |= !mDispatchedStableInsets.equals(mStableInsets);
                    insetsChanged |= !mDispatchedOutsets.equals(mOutsets);
                    mSurfaceHolder.setSurfaceFrameSize(w, h);
                    mSurfaceHolder.mSurfaceLock.unlock();
                    if (!mSurfaceHolder.mSurface.isValid()) {
                        reportSurfaceDestroyed();
                        if (android.service.wallpaper.WallpaperService.DEBUG)
                            android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "Layout: Surface destroyed");

                        return;
                    }
                    boolean didSurface = false;
                    try {
                        mSurfaceHolder.ungetCallbacks();
                        if (surfaceCreating) {
                            mIsCreating = true;
                            didSurface = true;
                            if (android.service.wallpaper.WallpaperService.DEBUG)
                                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("onSurfaceCreated(" + mSurfaceHolder) + "): ") + this);

                            onSurfaceCreated(mSurfaceHolder);
                            android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                            if (callbacks != null) {
                                for (android.view.SurfaceHolder.Callback c : callbacks) {
                                    c.surfaceCreated(mSurfaceHolder);
                                }
                            }
                        }
                        redrawNeeded |= creating || ((relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_FIRST_TIME) != 0);
                        if ((((forceReport || creating) || surfaceCreating) || formatChanged) || sizeChanged) {
                            if (android.service.wallpaper.WallpaperService.DEBUG) {
                                java.lang.RuntimeException e = new java.lang.RuntimeException();
                                e.fillInStackTrace();
                                android.util.Log.w(android.service.wallpaper.WallpaperService.TAG, (((((("forceReport=" + forceReport) + " creating=") + creating) + " formatChanged=") + formatChanged) + " sizeChanged=") + sizeChanged, e);
                            }
                            if (android.service.wallpaper.WallpaperService.DEBUG)
                                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (((((((("onSurfaceChanged(" + mSurfaceHolder) + ", ") + mFormat) + ", ") + mCurWidth) + ", ") + mCurHeight) + "): ") + this);

                            didSurface = true;
                            onSurfaceChanged(mSurfaceHolder, mFormat, mCurWidth, mCurHeight);
                            android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                            if (callbacks != null) {
                                for (android.view.SurfaceHolder.Callback c : callbacks) {
                                    c.surfaceChanged(mSurfaceHolder, mFormat, mCurWidth, mCurHeight);
                                }
                            }
                        }
                        if (insetsChanged) {
                            mDispatchedOverscanInsets.set(mOverscanInsets);
                            mDispatchedOverscanInsets.left += mOutsets.left;
                            mDispatchedOverscanInsets.top += mOutsets.top;
                            mDispatchedOverscanInsets.right += mOutsets.right;
                            mDispatchedOverscanInsets.bottom += mOutsets.bottom;
                            mDispatchedContentInsets.set(mContentInsets);
                            mDispatchedStableInsets.set(mStableInsets);
                            mDispatchedOutsets.set(mOutsets);
                            mFinalSystemInsets.set(mDispatchedOverscanInsets);
                            mFinalStableInsets.set(mDispatchedStableInsets);
                            android.view.WindowInsets insets = new android.view.WindowInsets(mFinalSystemInsets, null, mFinalStableInsets, getResources().getConfiguration().isScreenRound(), false);
                            if (android.service.wallpaper.WallpaperService.DEBUG) {
                                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "dispatching insets=" + insets);
                            }
                            onApplyWindowInsets(insets);
                        }
                        if (redrawNeeded) {
                            onSurfaceRedrawNeeded(mSurfaceHolder);
                            android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                            if (callbacks != null) {
                                for (android.view.SurfaceHolder.Callback c : callbacks) {
                                    if (c instanceof android.view.SurfaceHolder.Callback2) {
                                        ((android.view.SurfaceHolder.Callback2) (c)).surfaceRedrawNeeded(mSurfaceHolder);
                                    }
                                }
                            }
                        }
                        if (didSurface && (!mReportedVisible)) {
                            // This wallpaper is currently invisible, but its
                            // surface has changed.  At this point let's tell it
                            // again that it is invisible in case the report about
                            // the surface caused it to start running.  We really
                            // don't want wallpapers running when not visible.
                            if (mIsCreating) {
                                // Some wallpapers will ignore this call if they
                                // had previously been told they were invisble,
                                // so if we are creating a new surface then toggle
                                // the state to get them to notice.
                                if (android.service.wallpaper.WallpaperService.DEBUG)
                                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "onVisibilityChanged(true) at surface: " + this);

                                onVisibilityChanged(true);
                            }
                            if (android.service.wallpaper.WallpaperService.DEBUG)
                                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "onVisibilityChanged(false) at surface: " + this);

                            onVisibilityChanged(false);
                        }
                    } finally {
                        mIsCreating = false;
                        mSurfaceCreated = true;
                        if (redrawNeeded) {
                            mSession.finishDrawing(mWindow);
                        }
                        mIWallpaperEngine.reportShown();
                    }
                } catch (android.os.RemoteException ex) {
                }
                if (android.service.wallpaper.WallpaperService.DEBUG)
                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (((((("Layout: x=" + mLayout.x) + " y=") + mLayout.y) + " w=") + mLayout.width) + " h=") + mLayout.height);

            }
        }

        void attach(android.service.wallpaper.WallpaperService.IWallpaperEngineWrapper wrapper) {
            if (android.service.wallpaper.WallpaperService.DEBUG)
                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("attach: " + this) + " wrapper=") + wrapper);

            if (mDestroyed) {
                return;
            }
            mIWallpaperEngine = wrapper;
            mCaller = wrapper.mCaller;
            mConnection = wrapper.mConnection;
            mWindowToken = wrapper.mWindowToken;
            mSurfaceHolder.setSizeFromLayout();
            mInitializing = true;
            mSession = android.view.WindowManagerGlobal.getWindowSession();
            mWindow.setSession(mSession);
            mLayout.packageName = getPackageName();
            mDisplayManager = ((android.hardware.display.DisplayManager) (getSystemService(android.content.Context.DISPLAY_SERVICE)));
            mDisplayManager.registerDisplayListener(mDisplayListener, mCaller.getHandler());
            mDisplay = mDisplayManager.getDisplay(android.view.Display.DEFAULT_DISPLAY);
            mDisplayState = mDisplay.getState();
            if (android.service.wallpaper.WallpaperService.DEBUG)
                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "onCreate(): " + this);

            onCreate(mSurfaceHolder);
            mInitializing = false;
            mReportedVisible = false;
            updateSurface(false, false, false);
        }

        void doDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            if (!mDestroyed) {
                if (android.service.wallpaper.WallpaperService.DEBUG)
                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (((("onDesiredSizeChanged(" + desiredWidth) + ",") + desiredHeight) + "): ") + this);

                mIWallpaperEngine.mReqWidth = desiredWidth;
                mIWallpaperEngine.mReqHeight = desiredHeight;
                onDesiredSizeChanged(desiredWidth, desiredHeight);
                doOffsetsChanged(true);
            }
        }

        void doDisplayPaddingChanged(android.graphics.Rect padding) {
            if (!mDestroyed) {
                if (android.service.wallpaper.WallpaperService.DEBUG)
                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("onDisplayPaddingChanged(" + padding) + "): ") + this);

                if (!mIWallpaperEngine.mDisplayPadding.equals(padding)) {
                    mIWallpaperEngine.mDisplayPadding.set(padding);
                    updateSurface(true, false, false);
                }
            }
        }

        void doVisibilityChanged(boolean visible) {
            if (!mDestroyed) {
                mVisible = visible;
                reportVisibility();
            }
        }

        void reportVisibility() {
            if (!mDestroyed) {
                mDisplayState = (mDisplay == null) ? android.view.Display.STATE_UNKNOWN : mDisplay.getState();
                boolean visible = mVisible && (mDisplayState != android.view.Display.STATE_OFF);
                if (mReportedVisible != visible) {
                    mReportedVisible = visible;
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("onVisibilityChanged(" + visible) + "): ") + this);

                    if (visible) {
                        // If becoming visible, in preview mode the surface
                        // may have been destroyed so now we need to make
                        // sure it is re-created.
                        doOffsetsChanged(false);
                        updateSurface(false, false, false);
                    }
                    onVisibilityChanged(visible);
                }
            }
        }

        void doOffsetsChanged(boolean always) {
            if (mDestroyed) {
                return;
            }
            if ((!always) && (!mOffsetsChanged)) {
                return;
            }
            float xOffset;
            float yOffset;
            float xOffsetStep;
            float yOffsetStep;
            boolean sync;
            synchronized(mLock) {
                xOffset = mPendingXOffset;
                yOffset = mPendingYOffset;
                xOffsetStep = mPendingXOffsetStep;
                yOffsetStep = mPendingYOffsetStep;
                sync = mPendingSync;
                mPendingSync = false;
                mOffsetMessageEnqueued = false;
            }
            if (mSurfaceCreated) {
                if (mReportedVisible) {
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (((("Offsets change in " + this) + ": ") + xOffset) + ",") + yOffset);

                    final int availw = mIWallpaperEngine.mReqWidth - mCurWidth;
                    final int xPixels = (availw > 0) ? -((int) ((availw * xOffset) + 0.5F)) : 0;
                    final int availh = mIWallpaperEngine.mReqHeight - mCurHeight;
                    final int yPixels = (availh > 0) ? -((int) ((availh * yOffset) + 0.5F)) : 0;
                    onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixels, yPixels);
                } else {
                    mOffsetsChanged = true;
                }
            }
            if (sync) {
                try {
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "Reporting offsets change complete");

                    mSession.wallpaperOffsetsComplete(mWindow.asBinder());
                } catch (android.os.RemoteException e) {
                }
            }
        }

        void doCommand(android.service.wallpaper.WallpaperService.WallpaperCommand cmd) {
            android.os.Bundle result;
            if (!mDestroyed) {
                result = onCommand(cmd.action, cmd.x, cmd.y, cmd.z, cmd.extras, cmd.sync);
            } else {
                result = null;
            }
            if (cmd.sync) {
                try {
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "Reporting command complete");

                    mSession.wallpaperCommandComplete(mWindow.asBinder(), result);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        void reportSurfaceDestroyed() {
            if (mSurfaceCreated) {
                mSurfaceCreated = false;
                mSurfaceHolder.ungetCallbacks();
                android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                if (callbacks != null) {
                    for (android.view.SurfaceHolder.Callback c : callbacks) {
                        c.surfaceDestroyed(mSurfaceHolder);
                    }
                }
                if (android.service.wallpaper.WallpaperService.DEBUG)
                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("onSurfaceDestroyed(" + mSurfaceHolder) + "): ") + this);

                onSurfaceDestroyed(mSurfaceHolder);
            }
        }

        void detach() {
            if (mDestroyed) {
                return;
            }
            mDestroyed = true;
            if (mDisplayManager != null) {
                mDisplayManager.unregisterDisplayListener(mDisplayListener);
            }
            if (mVisible) {
                mVisible = false;
                if (android.service.wallpaper.WallpaperService.DEBUG)
                    android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "onVisibilityChanged(false): " + this);

                onVisibilityChanged(false);
            }
            reportSurfaceDestroyed();
            if (android.service.wallpaper.WallpaperService.DEBUG)
                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "onDestroy(): " + this);

            onDestroy();
            if (mCreated) {
                try {
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("Removing window and destroying surface " + mSurfaceHolder.getSurface()) + " of: ") + this);

                    if (mInputEventReceiver != null) {
                        mInputEventReceiver.dispose();
                        mInputEventReceiver = null;
                    }
                    mSession.remove(mWindow);
                } catch (android.os.RemoteException e) {
                }
                mSurfaceHolder.mSurface.release();
                mCreated = false;
                // Dispose the input channel after removing the window so the Window Manager
                // doesn't interpret the input channel being closed as an abnormal termination.
                if (mInputChannel != null) {
                    mInputChannel.dispose();
                    mInputChannel = null;
                }
            }
        }

        private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() {
            @java.lang.Override
            public void onDisplayChanged(int displayId) {
                if (mDisplay.getDisplayId() == displayId) {
                    reportVisibility();
                }
            }

            @java.lang.Override
            public void onDisplayRemoved(int displayId) {
            }

            @java.lang.Override
            public void onDisplayAdded(int displayId) {
            }
        };
    }

    class IWallpaperEngineWrapper extends android.service.wallpaper.IWallpaperEngine.Stub implements com.android.internal.os.HandlerCaller.Callback {
        private final com.android.internal.os.HandlerCaller mCaller;

        final android.service.wallpaper.IWallpaperConnection mConnection;

        final android.os.IBinder mWindowToken;

        final int mWindowType;

        final boolean mIsPreview;

        boolean mShownReported;

        int mReqWidth;

        int mReqHeight;

        final android.graphics.Rect mDisplayPadding = new android.graphics.Rect();

        android.service.wallpaper.WallpaperService.Engine mEngine;

        IWallpaperEngineWrapper(android.service.wallpaper.WallpaperService context, android.service.wallpaper.IWallpaperConnection conn, android.os.IBinder windowToken, int windowType, boolean isPreview, int reqWidth, int reqHeight, android.graphics.Rect padding) {
            mCaller = new com.android.internal.os.HandlerCaller(context, context.getMainLooper(), this, true);
            mConnection = conn;
            mWindowToken = windowToken;
            mWindowType = windowType;
            mIsPreview = isPreview;
            mReqWidth = reqWidth;
            mReqHeight = reqHeight;
            mDisplayPadding.set(padding);
            android.os.Message msg = mCaller.obtainMessage(android.service.wallpaper.WallpaperService.DO_ATTACH);
            mCaller.sendMessage(msg);
        }

        public void setDesiredSize(int width, int height) {
            android.os.Message msg = mCaller.obtainMessageII(android.service.wallpaper.WallpaperService.DO_SET_DESIRED_SIZE, width, height);
            mCaller.sendMessage(msg);
        }

        public void setDisplayPadding(android.graphics.Rect padding) {
            android.os.Message msg = mCaller.obtainMessageO(android.service.wallpaper.WallpaperService.DO_SET_DISPLAY_PADDING, padding);
            mCaller.sendMessage(msg);
        }

        public void setVisibility(boolean visible) {
            android.os.Message msg = mCaller.obtainMessageI(android.service.wallpaper.WallpaperService.MSG_VISIBILITY_CHANGED, visible ? 1 : 0);
            mCaller.sendMessage(msg);
        }

        public void dispatchPointer(android.view.MotionEvent event) {
            if (mEngine != null) {
                mEngine.dispatchPointer(event);
            } else {
                event.recycle();
            }
        }

        public void dispatchWallpaperCommand(java.lang.String action, int x, int y, int z, android.os.Bundle extras) {
            if (mEngine != null) {
                mEngine.mWindow.dispatchWallpaperCommand(action, x, y, z, extras, false);
            }
        }

        public void reportShown() {
            if (!mShownReported) {
                mShownReported = true;
                try {
                    mConnection.engineShown(this);
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(android.service.wallpaper.WallpaperService.TAG, "Wallpaper host disappeared", e);
                    return;
                }
            }
        }

        public void destroy() {
            android.os.Message msg = mCaller.obtainMessage(android.service.wallpaper.WallpaperService.DO_DETACH);
            mCaller.sendMessage(msg);
        }

        public void executeMessage(android.os.Message message) {
            switch (message.what) {
                case android.service.wallpaper.WallpaperService.DO_ATTACH :
                    {
                        try {
                            mConnection.attachEngine(this);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.w(android.service.wallpaper.WallpaperService.TAG, "Wallpaper host disappeared", e);
                            return;
                        }
                        android.service.wallpaper.WallpaperService.Engine engine = onCreateEngine();
                        mEngine = engine;
                        mActiveEngines.add(engine);
                        engine.attach(this);
                        return;
                    }
                case android.service.wallpaper.WallpaperService.DO_DETACH :
                    {
                        mActiveEngines.remove(mEngine);
                        mEngine.detach();
                        return;
                    }
                case android.service.wallpaper.WallpaperService.DO_SET_DESIRED_SIZE :
                    {
                        mEngine.doDesiredSizeChanged(message.arg1, message.arg2);
                        return;
                    }
                case android.service.wallpaper.WallpaperService.DO_SET_DISPLAY_PADDING :
                    {
                        mEngine.doDisplayPaddingChanged(((android.graphics.Rect) (message.obj)));
                    }
                case android.service.wallpaper.WallpaperService.MSG_UPDATE_SURFACE :
                    mEngine.updateSurface(true, false, false);
                    break;
                case android.service.wallpaper.WallpaperService.MSG_VISIBILITY_CHANGED :
                    if (android.service.wallpaper.WallpaperService.DEBUG)
                        android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, (("Visibility change in " + mEngine) + ": ") + message.arg1);

                    mEngine.doVisibilityChanged(message.arg1 != 0);
                    break;
                case android.service.wallpaper.WallpaperService.MSG_WALLPAPER_OFFSETS :
                    {
                        mEngine.doOffsetsChanged(true);
                    }
                    break;
                case android.service.wallpaper.WallpaperService.MSG_WALLPAPER_COMMAND :
                    {
                        android.service.wallpaper.WallpaperService.WallpaperCommand cmd = ((android.service.wallpaper.WallpaperService.WallpaperCommand) (message.obj));
                        mEngine.doCommand(cmd);
                    }
                    break;
                case android.service.wallpaper.WallpaperService.MSG_WINDOW_RESIZED :
                    {
                        final boolean reportDraw = message.arg1 != 0;
                        mEngine.mOutsets.set(((android.graphics.Rect) (message.obj)));
                        mEngine.updateSurface(true, false, reportDraw);
                        mEngine.doOffsetsChanged(true);
                    }
                    break;
                case android.service.wallpaper.WallpaperService.MSG_WINDOW_MOVED :
                    {
                        // Do nothing. What does it mean for a Wallpaper to move?
                    }
                    break;
                case android.service.wallpaper.WallpaperService.MSG_TOUCH_EVENT :
                    {
                        boolean skip = false;
                        android.view.MotionEvent ev = ((android.view.MotionEvent) (message.obj));
                        if (ev.getAction() == android.view.MotionEvent.ACTION_MOVE) {
                            synchronized(mEngine.mLock) {
                                if (mEngine.mPendingMove == ev) {
                                    mEngine.mPendingMove = null;
                                } else {
                                    // this is not the motion event we are looking for....
                                    skip = true;
                                }
                            }
                        }
                        if (!skip) {
                            if (android.service.wallpaper.WallpaperService.DEBUG)
                                android.util.Log.v(android.service.wallpaper.WallpaperService.TAG, "Delivering touch event: " + ev);

                            mEngine.onTouchEvent(ev);
                        }
                        ev.recycle();
                    }
                    break;
                default :
                    android.util.Log.w(android.service.wallpaper.WallpaperService.TAG, "Unknown message type " + message.what);
            }
        }
    }

    /**
     * Implements the internal {@link IWallpaperService} interface to convert
     * incoming calls to it back to calls on an {@link WallpaperService}.
     */
    class IWallpaperServiceWrapper extends android.service.wallpaper.IWallpaperService.Stub {
        private final android.service.wallpaper.WallpaperService mTarget;

        public IWallpaperServiceWrapper(android.service.wallpaper.WallpaperService context) {
            mTarget = context;
        }

        @java.lang.Override
        public void attach(android.service.wallpaper.IWallpaperConnection conn, android.os.IBinder windowToken, int windowType, boolean isPreview, int reqWidth, int reqHeight, android.graphics.Rect padding) {
            new android.service.wallpaper.WallpaperService.IWallpaperEngineWrapper(mTarget, conn, windowToken, windowType, isPreview, reqWidth, reqHeight, padding);
        }
    }

    @java.lang.Override
    public void onCreate() {
        super.onCreate();
    }

    @java.lang.Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mActiveEngines.size(); i++) {
            mActiveEngines.get(i).detach();
        }
        mActiveEngines.clear();
    }

    /**
     * Implement to return the implementation of the internal accessibility
     * service interface.  Subclasses should not override.
     */
    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return new android.service.wallpaper.WallpaperService.IWallpaperServiceWrapper(this);
    }

    /**
     * Must be implemented to return a new instance of the wallpaper's engine.
     * Note that multiple instances may be active at the same time, such as
     * when the wallpaper is currently set as the active wallpaper and the user
     * is in the wallpaper picker viewing a preview of it as well.
     */
    public abstract android.service.wallpaper.WallpaperService.Engine onCreateEngine();

    @java.lang.Override
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter out, java.lang.String[] args) {
        out.print("State of wallpaper ");
        out.print(this);
        out.println(":");
        for (int i = 0; i < mActiveEngines.size(); i++) {
            android.service.wallpaper.WallpaperService.Engine engine = mActiveEngines.get(i);
            out.print("  Engine ");
            out.print(engine);
            out.println(":");
            engine.dump("    ", fd, out, args);
        }
    }
}

