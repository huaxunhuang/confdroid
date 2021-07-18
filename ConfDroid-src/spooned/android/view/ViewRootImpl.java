/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * The top of a view hierarchy, implementing the needed protocol between View
 * and the WindowManager.  This is for the most part an internal implementation
 * detail of {@link WindowManagerGlobal}.
 *
 * {@hide }
 */
@java.lang.SuppressWarnings({ "EmptyCatchBlock", "PointlessBooleanExpression" })
public final class ViewRootImpl implements android.view.ThreadedRenderer.DrawCallbacks , android.view.View.AttachInfo.Callbacks , android.view.ViewParent {
    private static final java.lang.String TAG = "ViewRootImpl";

    private static final boolean DBG = false;

    private static final boolean LOCAL_LOGV = false;

    /**
     *
     *
     * @unknown PointlessBooleanExpression
     */
    private static final boolean DEBUG_DRAW = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_LAYOUT = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_DIALOG = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_INPUT_RESIZE = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_ORIENTATION = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_TRACKBALL = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_IMF = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_CONFIGURATION = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_FPS = false;

    private static final boolean DEBUG_INPUT_STAGES = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_KEEP_SCREEN_ON = false || android.view.ViewRootImpl.LOCAL_LOGV;

    private static final boolean DEBUG_CONTENT_CAPTURE = false || android.view.ViewRootImpl.LOCAL_LOGV;

    /**
     * Set to false if we do not want to use the multi threaded renderer even though
     * threaded renderer (aka hardware renderering) is used. Note that by disabling
     * this, WindowCallbacks will not fire.
     */
    private static final boolean MT_RENDERER_AVAILABLE = true;

    /**
     * If set to 2, the view system will switch from using rectangles retrieved from window to
     * dispatch to the view hierarchy to using {@link InsetsController}, that derives the insets
     * directly from the full configuration, enabling richer information about the insets state, as
     * well as new APIs to control it frame-by-frame, and synchronize animations with it.
     * <p>
     * Only set this to 2 once the new insets system is productionized and the old APIs are
     * fully migrated over.
     * <p>
     * If set to 1, this will switch to a mode where we only use the new approach for IME, but not
     * for the status/navigation bar.
     */
    private static final java.lang.String USE_NEW_INSETS_PROPERTY = "persist.wm.new_insets";

    /**
     *
     *
     * @see #USE_NEW_INSETS_PROPERTY
     * @unknown 
     */
    public static int sNewInsetsMode = android.os.SystemProperties.getInt(android.view.ViewRootImpl.USE_NEW_INSETS_PROPERTY, 0);

    /**
     *
     *
     * @see #USE_NEW_INSETS_PROPERTY
     * @unknown 
     */
    public static final int NEW_INSETS_MODE_NONE = 0;

    /**
     *
     *
     * @see #USE_NEW_INSETS_PROPERTY
     * @unknown 
     */
    public static final int NEW_INSETS_MODE_IME = 1;

    /**
     *
     *
     * @see #USE_NEW_INSETS_PROPERTY
     * @unknown 
     */
    public static final int NEW_INSETS_MODE_FULL = 2;

    /**
     * Set this system property to true to force the view hierarchy to render
     * at 60 Hz. This can be used to measure the potential framerate.
     */
    private static final java.lang.String PROPERTY_PROFILE_RENDERING = "viewroot.profile_rendering";

    // properties used by emulator to determine display shape
    public static final java.lang.String PROPERTY_EMULATOR_WIN_OUTSET_BOTTOM_PX = "ro.emu.win_outset_bottom_px";

    /**
     * Maximum time we allow the user to roll the trackball enough to generate
     * a key event, before resetting the counters.
     */
    static final int MAX_TRACKBALL_DELAY = 250;

    @android.annotation.UnsupportedAppUsage
    static final java.lang.ThreadLocal<android.view.HandlerActionQueue> sRunQueues = new java.lang.ThreadLocal<android.view.HandlerActionQueue>();

    static final java.util.ArrayList<java.lang.Runnable> sFirstDrawHandlers = new java.util.ArrayList<>();

    static boolean sFirstDrawComplete = false;

    /**
     * Callback for notifying about global configuration changes.
     */
    public interface ConfigChangedCallback {
        /**
         * Notifies about global config change.
         */
        void onConfigurationChanged(android.content.res.Configuration globalConfig);
    }

    private static final java.util.ArrayList<android.view.ViewRootImpl.ConfigChangedCallback> sConfigCallbacks = new java.util.ArrayList<>();

    /**
     * Callback for notifying activities about override configuration changes.
     */
    public interface ActivityConfigCallback {
        /**
         * Notifies about override config change and/or move to different display.
         *
         * @param overrideConfig
         * 		New override config to apply to activity.
         * @param newDisplayId
         * 		New display id, {@link Display#INVALID_DISPLAY} if not changed.
         */
        void onConfigurationChanged(android.content.res.Configuration overrideConfig, int newDisplayId);
    }

    /**
     * Callback used to notify corresponding activity about override configuration change and make
     * sure that all resources are set correctly before updating the ViewRootImpl's internal state.
     */
    private android.view.ViewRootImpl.ActivityConfigCallback mActivityConfigCallback;

    /**
     * Used when configuration change first updates the config of corresponding activity.
     * In that case we receive a call back from {@link ActivityThread} and this flag is used to
     * preserve the initial value.
     *
     * @see #performConfigurationChange(Configuration, Configuration, boolean, int)
     */
    private boolean mForceNextConfigUpdate;

    /**
     * Signals that compatibility booleans have been initialized according to
     * target SDK versions.
     */
    private static boolean sCompatibilityDone = false;

    /**
     * Always assign focus if a focusable View is available.
     */
    private static boolean sAlwaysAssignFocus;

    /**
     * This list must only be modified by the main thread, so a lock is only needed when changing
     * the list or when accessing the list from a non-main thread.
     */
    @com.android.internal.annotations.GuardedBy("mWindowCallbacks")
    final java.util.ArrayList<android.view.WindowCallbacks> mWindowCallbacks = new java.util.ArrayList<>();

    @android.annotation.UnsupportedAppUsage
    public final android.content.Context mContext;

    @android.annotation.UnsupportedAppUsage
    final android.view.IWindowSession mWindowSession;

    @android.annotation.NonNull
    android.view.Display mDisplay;

    final android.hardware.display.DisplayManager mDisplayManager;

    final java.lang.String mBasePackageName;

    final int[] mTmpLocation = new int[2];

    final android.util.TypedValue mTmpValue = new android.util.TypedValue();

    final java.lang.Thread mThread;

    final android.view.WindowLeaked mLocation;

    public final android.view.WindowManager.LayoutParams mWindowAttributes = new android.view.WindowManager.LayoutParams();

    final android.view.ViewRootImpl.W mWindow;

    final int mTargetSdkVersion;

    int mSeq;

    @android.annotation.UnsupportedAppUsage
    android.view.View mView;

    android.view.View mAccessibilityFocusedHost;

    android.view.accessibility.AccessibilityNodeInfo mAccessibilityFocusedVirtualView;

    // True if the window currently has pointer capture enabled.
    boolean mPointerCapture;

    int mViewVisibility;

    boolean mAppVisible = true;

    // For recents to freeform transition we need to keep drawing after the app receives information
    // that it became invisible. This will ignore that information and depend on the decor view
    // visibility to control drawing. The decor view visibility will get adjusted when the app get
    // stopped and that's when the app will stop drawing further frames.
    private boolean mForceDecorViewVisibility = false;

    // Used for tracking app visibility updates separately in case we get double change. This will
    // make sure that we always call relayout for the corresponding window.
    private boolean mAppVisibilityChanged;

    int mOrigWindowType = -1;

    /**
     * Whether the window had focus during the most recent traversal.
     */
    boolean mHadWindowFocus;

    /**
     * Whether the window lost focus during a previous traversal and has not
     * yet gained it back. Used to determine whether a WINDOW_STATE_CHANGE
     * accessibility events should be sent during traversal.
     */
    boolean mLostWindowFocus;

    // Set to true if the owner of this window is in the stopped state,
    // so the window should no longer be active.
    @android.annotation.UnsupportedAppUsage
    boolean mStopped = false;

    // Set to true if the owner of this window is in ambient mode,
    // which means it won't receive input events.
    boolean mIsAmbientMode = false;

    // Set to true to stop input during an Activity Transition.
    boolean mPausedForTransition = false;

    boolean mLastInCompatMode = false;

    android.view.SurfaceHolder.Callback2 mSurfaceHolderCallback;

    com.android.internal.view.BaseSurfaceHolder mSurfaceHolder;

    boolean mIsCreating;

    boolean mDrawingAllowed;

    final android.graphics.Region mTransparentRegion;

    final android.graphics.Region mPreviousTransparentRegion;

    @android.annotation.UnsupportedAppUsage
    int mWidth;

    @android.annotation.UnsupportedAppUsage
    int mHeight;

    @android.annotation.UnsupportedAppUsage
    android.graphics.Rect mDirty;

    public boolean mIsAnimating;

    private boolean mUseMTRenderer;

    private boolean mDragResizing;

    private boolean mInvalidateRootRequested;

    private int mResizeMode;

    private int mCanvasOffsetX;

    private int mCanvasOffsetY;

    private boolean mActivityRelaunched;

    android.content.res.CompatibilityInfo.Translator mTranslator;

    @android.annotation.UnsupportedAppUsage
    final android.view.View.AttachInfo mAttachInfo;

    android.view.InputChannel mInputChannel;

    android.view.InputQueue.Callback mInputQueueCallback;

    android.view.InputQueue mInputQueue;

    @android.annotation.UnsupportedAppUsage
    android.view.FallbackEventHandler mFallbackEventHandler;

    android.view.Choreographer mChoreographer;

    final android.graphics.Rect mTempRect;// used in the transaction to not thrash the heap.


    final android.graphics.Rect mVisRect;// used to retrieve visible rect of focused view.


    private final android.graphics.Rect mTempBoundsRect = new android.graphics.Rect();// used to set the size of the bounds surface.


    // This is used to reduce the race between window focus changes being dispatched from
    // the window manager and input events coming through the input system.
    @com.android.internal.annotations.GuardedBy("this")
    boolean mWindowFocusChanged;

    @com.android.internal.annotations.GuardedBy("this")
    boolean mUpcomingWindowFocus;

    @com.android.internal.annotations.GuardedBy("this")
    boolean mUpcomingInTouchMode;

    public boolean mTraversalScheduled;

    int mTraversalBarrier;

    boolean mWillDrawSoon;

    /**
     * Set to true while in performTraversals for detecting when die(true) is called from internal
     * callbacks such as onMeasure, onPreDraw, onDraw and deferring doDie() until later.
     */
    boolean mIsInTraversal;

    boolean mApplyInsetsRequested;

    boolean mLayoutRequested;

    boolean mFirst;

    boolean mReportNextDraw;

    boolean mFullRedrawNeeded;

    boolean mNewSurfaceNeeded;

    boolean mHasHadWindowFocus;

    boolean mLastWasImTarget;

    boolean mForceNextWindowRelayout;

    java.util.concurrent.CountDownLatch mWindowDrawCountDown;

    boolean mIsDrawing;

    int mLastSystemUiVisibility;

    int mClientWindowLayoutFlags;

    boolean mLastOverscanRequested;

    // Pool of queued input events.
    private static final int MAX_QUEUED_INPUT_EVENT_POOL_SIZE = 10;

    private android.view.ViewRootImpl.QueuedInputEvent mQueuedInputEventPool;

    private int mQueuedInputEventPoolSize;

    /* Input event queue.
    Pending input events are input events waiting to be delivered to the input stages
    and handled by the application.
     */
    android.view.ViewRootImpl.QueuedInputEvent mPendingInputEventHead;

    android.view.ViewRootImpl.QueuedInputEvent mPendingInputEventTail;

    int mPendingInputEventCount;

    boolean mProcessInputEventsScheduled;

    boolean mUnbufferedInputDispatch;

    java.lang.String mPendingInputEventQueueLengthCounterName = "pq";

    android.view.ViewRootImpl.InputStage mFirstInputStage;

    android.view.ViewRootImpl.InputStage mFirstPostImeInputStage;

    android.view.ViewRootImpl.InputStage mSyntheticInputStage;

    private final android.view.ViewRootImpl.UnhandledKeyManager mUnhandledKeyManager = new android.view.ViewRootImpl.UnhandledKeyManager();

    boolean mWindowAttributesChanged = false;

    int mWindowAttributesChangesFlag = 0;

    // These can be accessed by any thread, must be protected with a lock.
    // Surface can never be reassigned or cleared (use Surface.clear()).
    @android.annotation.UnsupportedAppUsage
    public final android.view.Surface mSurface = new android.view.Surface();

    private final android.view.SurfaceControl mSurfaceControl = new android.view.SurfaceControl();

    /**
     * Child surface of {@code mSurface} with the same bounds as its parent, and crop bounds
     * are set to the parent's bounds adjusted for surface insets. This surface is created when
     * {@link ViewRootImpl#createBoundsSurface(int)} is called.
     * By parenting to this bounds surface, child surfaces can ensure they do not draw into the
     * surface inset regions set by the parent window.
     */
    public final android.view.Surface mBoundsSurface = new android.view.Surface();

    private android.view.SurfaceSession mSurfaceSession;

    private android.view.SurfaceControl mBoundsSurfaceControl;

    private final android.view.SurfaceControl.Transaction mTransaction = new android.view.SurfaceControl.Transaction();

    @android.annotation.UnsupportedAppUsage
    boolean mAdded;

    boolean mAddedTouchMode;

    final android.graphics.Rect mTmpFrame = new android.graphics.Rect();

    // These are accessed by multiple threads.
    final android.graphics.Rect mWinFrame;// frame given by window manager.


    final android.graphics.Rect mPendingOverscanInsets = new android.graphics.Rect();

    final android.graphics.Rect mPendingVisibleInsets = new android.graphics.Rect();

    final android.graphics.Rect mPendingStableInsets = new android.graphics.Rect();

    final android.graphics.Rect mPendingContentInsets = new android.graphics.Rect();

    final android.graphics.Rect mPendingOutsets = new android.graphics.Rect();

    final android.graphics.Rect mPendingBackDropFrame = new android.graphics.Rect();

    final android.view.DisplayCutout.ParcelableWrapper mPendingDisplayCutout = new android.view.DisplayCutout.ParcelableWrapper(android.view.DisplayCutout.NO_CUTOUT);

    boolean mPendingAlwaysConsumeSystemBars;

    private android.view.InsetsState mTempInsets = new android.view.InsetsState();

    final android.view.ViewTreeObserver.InternalInsetsInfo mLastGivenInsets = new android.view.ViewTreeObserver.InternalInsetsInfo();

    final android.graphics.Rect mDispatchContentInsets = new android.graphics.Rect();

    final android.graphics.Rect mDispatchStableInsets = new android.graphics.Rect();

    android.view.DisplayCutout mDispatchDisplayCutout = android.view.DisplayCutout.NO_CUTOUT;

    private android.view.WindowInsets mLastWindowInsets;

    /**
     * Last applied configuration obtained from resources.
     */
    private final android.content.res.Configuration mLastConfigurationFromResources = new android.content.res.Configuration();

    /**
     * Last configuration reported from WM or via {@link #MSG_UPDATE_CONFIGURATION}.
     */
    private final android.util.MergedConfiguration mLastReportedMergedConfiguration = new android.util.MergedConfiguration();

    /**
     * Configurations waiting to be applied.
     */
    private final android.util.MergedConfiguration mPendingMergedConfiguration = new android.util.MergedConfiguration();

    boolean mScrollMayChange;

    @android.view.WindowManager.LayoutParams.SoftInputModeFlags
    int mSoftInputMode;

    @android.annotation.UnsupportedAppUsage
    java.lang.ref.WeakReference<android.view.View> mLastScrolledFocus;

    int mScrollY;

    int mCurScrollY;

    android.widget.Scroller mScroller;

    static final android.view.animation.Interpolator mResizeInterpolator = new android.view.animation.AccelerateDecelerateInterpolator();

    private java.util.ArrayList<android.animation.LayoutTransition> mPendingTransitions;

    final android.view.ViewConfiguration mViewConfiguration;

    /* Drag/drop */
    android.content.ClipDescription mDragDescription;

    android.view.View mCurrentDragView;

    volatile java.lang.Object mLocalDragState;

    final android.graphics.PointF mDragPoint = new android.graphics.PointF();

    final android.graphics.PointF mLastTouchPoint = new android.graphics.PointF();

    int mLastTouchSource;

    private boolean mProfileRendering;

    private android.view.Choreographer.FrameCallback mRenderProfiler;

    private boolean mRenderProfilingEnabled;

    // Variables to track frames per second, enabled via DEBUG_FPS flag
    private long mFpsStartTime = -1;

    private long mFpsPrevTime = -1;

    private int mFpsNumFrames;

    private int mPointerIconType = android.view.PointerIcon.TYPE_NOT_SPECIFIED;

    private android.view.PointerIcon mCustomPointerIcon = null;

    /**
     * see {@link #playSoundEffect(int)}
     */
    android.media.AudioManager mAudioManager;

    final android.view.accessibility.AccessibilityManager mAccessibilityManager;

    android.view.AccessibilityInteractionController mAccessibilityInteractionController;

    final android.view.ViewRootImpl.AccessibilityInteractionConnectionManager mAccessibilityInteractionConnectionManager = new android.view.ViewRootImpl.AccessibilityInteractionConnectionManager();

    final android.view.ViewRootImpl.HighContrastTextManager mHighContrastTextManager;

    android.view.ViewRootImpl.SendWindowContentChangedAccessibilityEvent mSendWindowContentChangedAccessibilityEvent;

    java.util.HashSet<android.view.View> mTempHashSet;

    private final int mDensity;

    private final int mNoncompatDensity;

    private boolean mInLayout = false;

    java.util.ArrayList<android.view.View> mLayoutRequesters = new java.util.ArrayList<android.view.View>();

    boolean mHandlingLayoutInLayoutRequest = false;

    private int mViewLayoutDirectionInitial;

    /**
     * Set to true once doDie() has been called.
     */
    private boolean mRemoved;

    private boolean mNeedsRendererSetup;

    private final android.view.InputEventCompatProcessor mInputCompatProcessor;

    /**
     * Consistency verifier for debugging purposes.
     */
    protected final android.view.InputEventConsistencyVerifier mInputEventConsistencyVerifier = (android.view.InputEventConsistencyVerifier.isInstrumentationEnabled()) ? new android.view.InputEventConsistencyVerifier(this, 0) : null;

    private final android.view.InsetsController mInsetsController = new android.view.InsetsController(this);

    private final android.view.GestureExclusionTracker mGestureExclusionTracker = new android.view.GestureExclusionTracker();

    static final class SystemUiVisibilityInfo {
        int seq;

        int globalVisibility;

        int localValue;

        int localChanges;
    }

    private java.lang.String mTag = android.view.ViewRootImpl.TAG;

    public ViewRootImpl(android.content.Context context, android.view.Display display) {
        mContext = context;
        mWindowSession = android.view.WindowManagerGlobal.getWindowSession();
        mDisplay = display;
        mBasePackageName = context.getBasePackageName();
        mThread = java.lang.Thread.currentThread();
        mLocation = new android.view.WindowLeaked(null);
        mLocation.fillInStackTrace();
        mWidth = -1;
        mHeight = -1;
        mDirty = new android.graphics.Rect();
        mTempRect = new android.graphics.Rect();
        mVisRect = new android.graphics.Rect();
        mWinFrame = new android.graphics.Rect();
        mWindow = new android.view.ViewRootImpl.W(this);
        mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        mViewVisibility = android.view.View.GONE;
        mTransparentRegion = new android.graphics.Region();
        mPreviousTransparentRegion = new android.graphics.Region();
        mFirst = true;// true for the first time the view is added

        mAdded = false;
        mAttachInfo = new android.view.View.AttachInfo(mWindowSession, mWindow, display, this, mHandler, this, context);
        mAccessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(context);
        mAccessibilityManager.addAccessibilityStateChangeListener(mAccessibilityInteractionConnectionManager, mHandler);
        mHighContrastTextManager = new android.view.ViewRootImpl.HighContrastTextManager();
        mAccessibilityManager.addHighTextContrastStateChangeListener(mHighContrastTextManager, mHandler);
        mViewConfiguration = android.view.ViewConfiguration.get(context);
        mDensity = context.getResources().getDisplayMetrics().densityDpi;
        mNoncompatDensity = context.getResources().getDisplayMetrics().noncompatDensityDpi;
        mFallbackEventHandler = new com.android.internal.policy.PhoneFallbackEventHandler(context);
        mChoreographer = android.view.Choreographer.getInstance();
        mDisplayManager = ((android.hardware.display.DisplayManager) (context.getSystemService(android.content.Context.DISPLAY_SERVICE)));
        java.lang.String processorOverrideName = context.getResources().getString(R.string.config_inputEventCompatProcessorOverrideClassName);
        if (processorOverrideName.isEmpty()) {
            // No compatibility processor override, using default.
            mInputCompatProcessor = new android.view.InputEventCompatProcessor(context);
        } else {
            android.view.InputEventCompatProcessor compatProcessor = null;
            try {
                final java.lang.Class<? extends android.view.InputEventCompatProcessor> klass = ((java.lang.Class<? extends android.view.InputEventCompatProcessor>) (java.lang.Class.forName(processorOverrideName)));
                compatProcessor = klass.getConstructor(android.content.Context.class).newInstance(context);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.view.ViewRootImpl.TAG, "Unable to create the InputEventCompatProcessor. ", e);
            } finally {
                mInputCompatProcessor = compatProcessor;
            }
        }
        if (!android.view.ViewRootImpl.sCompatibilityDone) {
            android.view.ViewRootImpl.sAlwaysAssignFocus = mTargetSdkVersion < Build.VERSION_CODES.P;
            android.view.ViewRootImpl.sCompatibilityDone = true;
        }
        loadSystemProperties();
    }

    public static void addFirstDrawHandler(java.lang.Runnable callback) {
        synchronized(android.view.ViewRootImpl.sFirstDrawHandlers) {
            if (!android.view.ViewRootImpl.sFirstDrawComplete) {
                android.view.ViewRootImpl.sFirstDrawHandlers.add(callback);
            }
        }
    }

    /**
     * Add static config callback to be notified about global config changes.
     */
    @android.annotation.UnsupportedAppUsage
    public static void addConfigCallback(android.view.ViewRootImpl.ConfigChangedCallback callback) {
        synchronized(android.view.ViewRootImpl.sConfigCallbacks) {
            android.view.ViewRootImpl.sConfigCallbacks.add(callback);
        }
    }

    /**
     * Add activity config callback to be notified about override config changes.
     */
    public void setActivityConfigCallback(android.view.ViewRootImpl.ActivityConfigCallback callback) {
        mActivityConfigCallback = callback;
    }

    public void addWindowCallbacks(android.view.WindowCallbacks callback) {
        synchronized(mWindowCallbacks) {
            mWindowCallbacks.add(callback);
        }
    }

    public void removeWindowCallbacks(android.view.WindowCallbacks callback) {
        synchronized(mWindowCallbacks) {
            mWindowCallbacks.remove(callback);
        }
    }

    public void reportDrawFinish() {
        if (mWindowDrawCountDown != null) {
            mWindowDrawCountDown.countDown();
        }
    }

    // FIXME for perf testing only
    private boolean mProfile = false;

    /**
     * Call this to profile the next traversal call.
     * FIXME for perf testing only. Remove eventually
     */
    public void profile() {
        mProfile = true;
    }

    /**
     * Indicates whether we are in touch mode. Calling this method triggers an IPC
     * call and should be avoided whenever possible.
     *
     * @return True, if the device is in touch mode, false otherwise.
     * @unknown 
     */
    static boolean isInTouchMode() {
        android.view.IWindowSession windowSession = android.view.WindowManagerGlobal.peekWindowSession();
        if (windowSession != null) {
            try {
                return windowSession.getInTouchMode();
            } catch (android.os.RemoteException e) {
            }
        }
        return false;
    }

    /**
     * Notifies us that our child has been rebuilt, following
     * a window preservation operation. In these cases we
     * keep the same DecorView, but the activity controlling it
     * is a different instance, and we need to update our
     * callbacks.
     *
     * @unknown 
     */
    public void notifyChildRebuilt() {
        if (mView instanceof com.android.internal.view.RootViewSurfaceTaker) {
            if (mSurfaceHolderCallback != null) {
                mSurfaceHolder.removeCallback(mSurfaceHolderCallback);
            }
            mSurfaceHolderCallback = ((com.android.internal.view.RootViewSurfaceTaker) (mView)).willYouTakeTheSurface();
            if (mSurfaceHolderCallback != null) {
                mSurfaceHolder = new android.view.ViewRootImpl.TakenSurfaceHolder();
                mSurfaceHolder.setFormat(android.graphics.PixelFormat.UNKNOWN);
                mSurfaceHolder.addCallback(mSurfaceHolderCallback);
            } else {
                mSurfaceHolder = null;
            }
            mInputQueueCallback = ((com.android.internal.view.RootViewSurfaceTaker) (mView)).willYouTakeTheInputQueue();
            if (mInputQueueCallback != null) {
                mInputQueueCallback.onInputQueueCreated(mInputQueue);
            }
        }
    }

    /**
     * We have one child
     */
    public void setView(android.view.View view, android.view.WindowManager.LayoutParams attrs, android.view.View panelParentView) {
        synchronized(this) {
            if (mView == null) {
                mView = view;
                mAttachInfo.mDisplayState = mDisplay.getState();
                mDisplayManager.registerDisplayListener(mDisplayListener, mHandler);
                mViewLayoutDirectionInitial = mView.getRawLayoutDirection();
                mFallbackEventHandler.setView(view);
                mWindowAttributes.copyFrom(attrs);
                if (mWindowAttributes.packageName == null) {
                    mWindowAttributes.packageName = mBasePackageName;
                }
                attrs = mWindowAttributes;
                setTag();
                if ((android.view.ViewRootImpl.DEBUG_KEEP_SCREEN_ON && ((mClientWindowLayoutFlags & android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0)) && ((attrs.flags & android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0)) {
                    android.util.Slog.d(mTag, "setView: FLAG_KEEP_SCREEN_ON changed from true to false!");
                }
                // Keep track of the actual window flags supplied by the client.
                mClientWindowLayoutFlags = attrs.flags;
                setAccessibilityFocus(null, null);
                if (view instanceof com.android.internal.view.RootViewSurfaceTaker) {
                    mSurfaceHolderCallback = ((com.android.internal.view.RootViewSurfaceTaker) (view)).willYouTakeTheSurface();
                    if (mSurfaceHolderCallback != null) {
                        mSurfaceHolder = new android.view.ViewRootImpl.TakenSurfaceHolder();
                        mSurfaceHolder.setFormat(android.graphics.PixelFormat.UNKNOWN);
                        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
                    }
                }
                // Compute surface insets required to draw at specified Z value.
                // TODO: Use real shadow insets for a constant max Z.
                if (!attrs.hasManualSurfaceInsets) {
                    /* manual */
                    /* preservePrevious */
                    attrs.setSurfaceInsets(view, false, true);
                }
                android.content.res.CompatibilityInfo compatibilityInfo = mDisplay.getDisplayAdjustments().getCompatibilityInfo();
                mTranslator = compatibilityInfo.getTranslator();
                // If the application owns the surface, don't enable hardware acceleration
                if (mSurfaceHolder == null) {
                    // While this is supposed to enable only, it can effectively disable
                    // the acceleration too.
                    enableHardwareAcceleration(attrs);
                    final boolean useMTRenderer = android.view.ViewRootImpl.MT_RENDERER_AVAILABLE && (mAttachInfo.mThreadedRenderer != null);
                    if (mUseMTRenderer != useMTRenderer) {
                        // Shouldn't be resizing, as it's done only in window setup,
                        // but end just in case.
                        endDragResizing();
                        mUseMTRenderer = useMTRenderer;
                    }
                }
                boolean restore = false;
                if (mTranslator != null) {
                    mSurface.setCompatibilityTranslator(mTranslator);
                    restore = true;
                    attrs.backup();
                    mTranslator.translateWindowLayout(attrs);
                }
                if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                    android.util.Log.d(mTag, "WindowLayout in setView:" + attrs);

                if (!compatibilityInfo.supportsScreen()) {
                    attrs.privateFlags |= android.view.WindowManager.LayoutParams.PRIVATE_FLAG_COMPATIBLE_WINDOW;
                    mLastInCompatMode = true;
                }
                mSoftInputMode = attrs.softInputMode;
                mWindowAttributesChanged = true;
                mWindowAttributesChangesFlag = android.view.WindowManager.LayoutParams.EVERYTHING_CHANGED;
                mAttachInfo.mRootView = view;
                mAttachInfo.mScalingRequired = mTranslator != null;
                mAttachInfo.mApplicationScale = (mTranslator == null) ? 1.0F : mTranslator.applicationScale;
                if (panelParentView != null) {
                    mAttachInfo.mPanelParentWindowToken = panelParentView.getApplicationWindowToken();
                }
                mAdded = true;
                int res;/* = WindowManagerImpl.ADD_OKAY; */

                // Schedule the first layout -before- adding to the window
                // manager, to make sure we do the relayout before receiving
                // any other events from the system.
                requestLayout();
                if ((mWindowAttributes.inputFeatures & android.view.WindowManager.LayoutParams.INPUT_FEATURE_NO_INPUT_CHANNEL) == 0) {
                    mInputChannel = new android.view.InputChannel();
                }
                mForceDecorViewVisibility = (mWindowAttributes.privateFlags & android.view.WindowManager.LayoutParams.PRIVATE_FLAG_FORCE_DECOR_VIEW_VISIBILITY) != 0;
                try {
                    mOrigWindowType = mWindowAttributes.type;
                    mAttachInfo.mRecomputeGlobalAttributes = true;
                    collectViewAttributes();
                    res = mWindowSession.addToDisplay(mWindow, mSeq, mWindowAttributes, getHostVisibility(), mDisplay.getDisplayId(), mTmpFrame, mAttachInfo.mContentInsets, mAttachInfo.mStableInsets, mAttachInfo.mOutsets, mAttachInfo.mDisplayCutout, mInputChannel, mTempInsets);
                    setFrame(mTmpFrame);
                } catch (android.os.RemoteException e) {
                    mAdded = false;
                    mView = null;
                    mAttachInfo.mRootView = null;
                    mInputChannel = null;
                    mFallbackEventHandler.setView(null);
                    unscheduleTraversals();
                    setAccessibilityFocus(null, null);
                    throw new java.lang.RuntimeException("Adding window failed", e);
                } finally {
                    if (restore) {
                        attrs.restore();
                    }
                }
                if (mTranslator != null) {
                    mTranslator.translateRectInScreenToAppWindow(mAttachInfo.mContentInsets);
                }
                mPendingOverscanInsets.set(0, 0, 0, 0);
                mPendingContentInsets.set(mAttachInfo.mContentInsets);
                mPendingStableInsets.set(mAttachInfo.mStableInsets);
                mPendingDisplayCutout.set(mAttachInfo.mDisplayCutout);
                mPendingVisibleInsets.set(0, 0, 0, 0);
                mAttachInfo.mAlwaysConsumeSystemBars = (res & android.view.WindowManagerGlobal.ADD_FLAG_ALWAYS_CONSUME_SYSTEM_BARS) != 0;
                mPendingAlwaysConsumeSystemBars = mAttachInfo.mAlwaysConsumeSystemBars;
                mInsetsController.onStateChanged(mTempInsets);
                if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                    android.util.Log.v(mTag, "Added window " + mWindow);

                if (res < android.view.WindowManagerGlobal.ADD_OKAY) {
                    mAttachInfo.mRootView = null;
                    mAdded = false;
                    mFallbackEventHandler.setView(null);
                    unscheduleTraversals();
                    setAccessibilityFocus(null, null);
                    switch (res) {
                        case android.view.WindowManagerGlobal.ADD_BAD_APP_TOKEN :
                        case android.view.WindowManagerGlobal.ADD_BAD_SUBWINDOW_TOKEN :
                            throw new android.view.WindowManager.BadTokenException(("Unable to add window -- token " + attrs.token) + " is not valid; is your activity running?");
                        case android.view.WindowManagerGlobal.ADD_NOT_APP_TOKEN :
                            throw new android.view.WindowManager.BadTokenException(("Unable to add window -- token " + attrs.token) + " is not for an application");
                        case android.view.WindowManagerGlobal.ADD_APP_EXITING :
                            throw new android.view.WindowManager.BadTokenException(("Unable to add window -- app for token " + attrs.token) + " is exiting");
                        case android.view.WindowManagerGlobal.ADD_DUPLICATE_ADD :
                            throw new android.view.WindowManager.BadTokenException(("Unable to add window -- window " + mWindow) + " has already been added");
                        case android.view.WindowManagerGlobal.ADD_STARTING_NOT_NEEDED :
                            // Silently ignore -- we would have just removed it
                            // right away, anyway.
                            return;
                        case android.view.WindowManagerGlobal.ADD_MULTIPLE_SINGLETON :
                            throw new android.view.WindowManager.BadTokenException(((("Unable to add window " + mWindow) + " -- another window of type ") + mWindowAttributes.type) + " already exists");
                        case android.view.WindowManagerGlobal.ADD_PERMISSION_DENIED :
                            throw new android.view.WindowManager.BadTokenException((("Unable to add window " + mWindow) + " -- permission denied for window type ") + mWindowAttributes.type);
                        case android.view.WindowManagerGlobal.ADD_INVALID_DISPLAY :
                            throw new android.view.WindowManager.InvalidDisplayException(("Unable to add window " + mWindow) + " -- the specified display can not be found");
                        case android.view.WindowManagerGlobal.ADD_INVALID_TYPE :
                            throw new android.view.WindowManager.InvalidDisplayException(((("Unable to add window " + mWindow) + " -- the specified window type ") + mWindowAttributes.type) + " is not valid");
                    }
                    throw new java.lang.RuntimeException("Unable to add window -- unknown error code " + res);
                }
                if (view instanceof com.android.internal.view.RootViewSurfaceTaker) {
                    mInputQueueCallback = ((com.android.internal.view.RootViewSurfaceTaker) (view)).willYouTakeTheInputQueue();
                }
                if (mInputChannel != null) {
                    if (mInputQueueCallback != null) {
                        mInputQueue = new android.view.InputQueue();
                        mInputQueueCallback.onInputQueueCreated(mInputQueue);
                    }
                    mInputEventReceiver = new android.view.ViewRootImpl.WindowInputEventReceiver(mInputChannel, android.os.Looper.myLooper());
                }
                view.assignParent(this);
                mAddedTouchMode = (res & android.view.WindowManagerGlobal.ADD_FLAG_IN_TOUCH_MODE) != 0;
                mAppVisible = (res & android.view.WindowManagerGlobal.ADD_FLAG_APP_VISIBLE) != 0;
                if (mAccessibilityManager.isEnabled()) {
                    mAccessibilityInteractionConnectionManager.ensureConnection();
                }
                if (view.getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
                    view.setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                }
                // Set up the input pipeline.
                java.lang.CharSequence counterSuffix = attrs.getTitle();
                mSyntheticInputStage = new android.view.ViewRootImpl.SyntheticInputStage();
                android.view.ViewRootImpl.InputStage viewPostImeStage = new android.view.ViewRootImpl.ViewPostImeInputStage(mSyntheticInputStage);
                android.view.ViewRootImpl.InputStage nativePostImeStage = new android.view.ViewRootImpl.NativePostImeInputStage(viewPostImeStage, "aq:native-post-ime:" + counterSuffix);
                android.view.ViewRootImpl.InputStage earlyPostImeStage = new android.view.ViewRootImpl.EarlyPostImeInputStage(nativePostImeStage);
                android.view.ViewRootImpl.InputStage imeStage = new android.view.ViewRootImpl.ImeInputStage(earlyPostImeStage, "aq:ime:" + counterSuffix);
                android.view.ViewRootImpl.InputStage viewPreImeStage = new android.view.ViewRootImpl.ViewPreImeInputStage(imeStage);
                android.view.ViewRootImpl.InputStage nativePreImeStage = new android.view.ViewRootImpl.NativePreImeInputStage(viewPreImeStage, "aq:native-pre-ime:" + counterSuffix);
                mFirstInputStage = nativePreImeStage;
                mFirstPostImeInputStage = earlyPostImeStage;
                mPendingInputEventQueueLengthCounterName = "aq:pending:" + counterSuffix;
            }
        }
    }

    private void setTag() {
        final java.lang.String[] split = mWindowAttributes.getTitle().toString().split("\\.");
        if (split.length > 0) {
            mTag = ((android.view.ViewRootImpl.TAG + "[") + split[split.length - 1]) + "]";
        }
    }

    /**
     * Whether the window is in local focus mode or not
     */
    private boolean isInLocalFocusMode() {
        return (mWindowAttributes.flags & android.view.WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE) != 0;
    }

    @android.annotation.UnsupportedAppUsage
    public int getWindowFlags() {
        return mWindowAttributes.flags;
    }

    public int getDisplayId() {
        return mDisplay.getDisplayId();
    }

    public java.lang.CharSequence getTitle() {
        return mWindowAttributes.getTitle();
    }

    /**
     *
     *
     * @return the width of the root view. Note that this will return {@code -1} until the first
    layout traversal, when the width is set.
     * @unknown 
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     *
     *
     * @return the height of the root view. Note that this will return {@code -1} until the first
    layout traversal, when the height is set.
     * @unknown 
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Destroys hardware rendering resources for this ViewRootImpl
     *
     * May be called on any thread
     */
    @android.annotation.AnyThread
    void destroyHardwareResources() {
        final android.view.ThreadedRenderer renderer = mAttachInfo.mThreadedRenderer;
        if (renderer != null) {
            // This is called by WindowManagerGlobal which may or may not be on the right thread
            if (android.os.Looper.myLooper() != mAttachInfo.mHandler.getLooper()) {
                mAttachInfo.mHandler.postAtFrontOfQueue(this::destroyHardwareResources);
                return;
            }
            renderer.destroyHardwareResources(mView);
            renderer.destroy();
        }
    }

    @android.annotation.UnsupportedAppUsage
    public void detachFunctor(long functor) {
        if (mAttachInfo.mThreadedRenderer != null) {
            // Fence so that any pending invokeFunctor() messages will be processed
            // before we return from detachFunctor.
            mAttachInfo.mThreadedRenderer.stopDrawing();
        }
    }

    /**
     * Schedules the functor for execution in either kModeProcess or
     * kModeProcessNoContext, depending on whether or not there is an EGLContext.
     *
     * @param functor
     * 		The native functor to invoke
     * @param waitForCompletion
     * 		If true, this will not return until the functor
     * 		has invoked. If false, the functor may be invoked
     * 		asynchronously.
     */
    @android.annotation.UnsupportedAppUsage
    public static void invokeFunctor(long functor, boolean waitForCompletion) {
        android.view.ThreadedRenderer.invokeFunctor(functor, waitForCompletion);
    }

    /**
     *
     *
     * @param animator
     * 		animator to register with the hardware renderer
     */
    public void registerAnimatingRenderNode(android.graphics.RenderNode animator) {
        if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.registerAnimatingRenderNode(animator);
        } else {
            if (mAttachInfo.mPendingAnimatingRenderNodes == null) {
                mAttachInfo.mPendingAnimatingRenderNodes = new java.util.ArrayList<android.graphics.RenderNode>();
            }
            mAttachInfo.mPendingAnimatingRenderNodes.add(animator);
        }
    }

    /**
     *
     *
     * @param animator
     * 		animator to register with the hardware renderer
     */
    public void registerVectorDrawableAnimator(android.view.NativeVectorDrawableAnimator animator) {
        if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.registerVectorDrawableAnimator(animator);
        }
    }

    /**
     * Registers a callback to be executed when the next frame is being drawn on RenderThread. This
     * callback will be executed on a RenderThread worker thread, and only used for the next frame
     * and thus it will only fire once.
     *
     * @param callback
     * 		The callback to register.
     */
    public void registerRtFrameCallback(android.graphics.HardwareRenderer.FrameDrawingCallback callback) {
        if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.registerRtFrameCallback(( frame) -> {
                try {
                    callback.onFrameDraw(frame);
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.view.ViewRootImpl.TAG, "Exception while executing onFrameDraw", e);
                }
            });
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void enableHardwareAcceleration(android.view.WindowManager.LayoutParams attrs) {
        mAttachInfo.mHardwareAccelerated = false;
        mAttachInfo.mHardwareAccelerationRequested = false;
        // Don't enable hardware acceleration when the application is in compatibility mode
        if (mTranslator != null)
            return;

        // Try to enable hardware acceleration if requested
        final boolean hardwareAccelerated = (attrs.flags & android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) != 0;
        if (hardwareAccelerated) {
            if (!android.view.ThreadedRenderer.isAvailable()) {
                return;
            }
            // Persistent processes (including the system) should not do
            // accelerated rendering on low-end devices.  In that case,
            // sRendererDisabled will be set.  In addition, the system process
            // itself should never do accelerated rendering.  In that case, both
            // sRendererDisabled and sSystemRendererDisabled are set.  When
            // sSystemRendererDisabled is set, PRIVATE_FLAG_FORCE_HARDWARE_ACCELERATED
            // can be used by code on the system process to escape that and enable
            // HW accelerated drawing.  (This is basically for the lock screen.)
            final boolean fakeHwAccelerated = (attrs.privateFlags & android.view.WindowManager.LayoutParams.PRIVATE_FLAG_FAKE_HARDWARE_ACCELERATED) != 0;
            final boolean forceHwAccelerated = (attrs.privateFlags & android.view.WindowManager.LayoutParams.PRIVATE_FLAG_FORCE_HARDWARE_ACCELERATED) != 0;
            if (fakeHwAccelerated) {
                // This is exclusively for the preview windows the window manager
                // shows for launching applications, so they will look more like
                // the app being launched.
                mAttachInfo.mHardwareAccelerationRequested = true;
            } else
                if ((!android.view.ThreadedRenderer.sRendererDisabled) || (android.view.ThreadedRenderer.sSystemRendererDisabled && forceHwAccelerated)) {
                    if (mAttachInfo.mThreadedRenderer != null) {
                        mAttachInfo.mThreadedRenderer.destroy();
                    }
                    final android.graphics.Rect insets = attrs.surfaceInsets;
                    final boolean hasSurfaceInsets = (((insets.left != 0) || (insets.right != 0)) || (insets.top != 0)) || (insets.bottom != 0);
                    final boolean translucent = (attrs.format != android.graphics.PixelFormat.OPAQUE) || hasSurfaceInsets;
                    final boolean wideGamut = mContext.getResources().getConfiguration().isScreenWideColorGamut() && (attrs.getColorMode() == android.content.pm.ActivityInfo.COLOR_MODE_WIDE_COLOR_GAMUT);
                    mAttachInfo.mThreadedRenderer = android.view.ThreadedRenderer.create(mContext, translucent, attrs.getTitle().toString());
                    mAttachInfo.mThreadedRenderer.setWideGamut(wideGamut);
                    updateForceDarkMode();
                    if (mAttachInfo.mThreadedRenderer != null) {
                        mAttachInfo.mHardwareAccelerated = mAttachInfo.mHardwareAccelerationRequested = true;
                    }
                }

        }
    }

    private int getNightMode() {
        return mContext.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
    }

    private void updateForceDarkMode() {
        if (mAttachInfo.mThreadedRenderer == null)
            return;

        boolean useAutoDark = getNightMode() == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        if (useAutoDark) {
            boolean forceDarkAllowedDefault = android.os.SystemProperties.getBoolean(android.view.ThreadedRenderer.DEBUG_FORCE_DARK, false);
            android.content.res.TypedArray a = mContext.obtainStyledAttributes(R.styleable.Theme);
            useAutoDark = a.getBoolean(R.styleable.Theme_isLightTheme, true) && a.getBoolean(R.styleable.Theme_forceDarkAllowed, forceDarkAllowedDefault);
            a.recycle();
        }
        if (mAttachInfo.mThreadedRenderer.setForceDark(useAutoDark)) {
            // TODO: Don't require regenerating all display lists to apply this setting
            invalidateWorld(mView);
        }
    }

    @android.annotation.UnsupportedAppUsage
    public android.view.View getView() {
        return mView;
    }

    final android.view.WindowLeaked getLocation() {
        return mLocation;
    }

    void setLayoutParams(android.view.WindowManager.LayoutParams attrs, boolean newView) {
        synchronized(this) {
            final int oldInsetLeft = mWindowAttributes.surfaceInsets.left;
            final int oldInsetTop = mWindowAttributes.surfaceInsets.top;
            final int oldInsetRight = mWindowAttributes.surfaceInsets.right;
            final int oldInsetBottom = mWindowAttributes.surfaceInsets.bottom;
            final int oldSoftInputMode = mWindowAttributes.softInputMode;
            final boolean oldHasManualSurfaceInsets = mWindowAttributes.hasManualSurfaceInsets;
            if ((android.view.ViewRootImpl.DEBUG_KEEP_SCREEN_ON && ((mClientWindowLayoutFlags & android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0)) && ((attrs.flags & android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0)) {
                android.util.Slog.d(mTag, "setLayoutParams: FLAG_KEEP_SCREEN_ON from true to false!");
            }
            // Keep track of the actual window flags supplied by the client.
            mClientWindowLayoutFlags = attrs.flags;
            // Preserve compatible window flag if exists.
            final int compatibleWindowFlag = mWindowAttributes.privateFlags & android.view.WindowManager.LayoutParams.PRIVATE_FLAG_COMPATIBLE_WINDOW;
            // Transfer over system UI visibility values as they carry current state.
            attrs.systemUiVisibility = mWindowAttributes.systemUiVisibility;
            attrs.subtreeSystemUiVisibility = mWindowAttributes.subtreeSystemUiVisibility;
            mWindowAttributesChangesFlag = mWindowAttributes.copyFrom(attrs);
            if ((mWindowAttributesChangesFlag & android.view.WindowManager.LayoutParams.TRANSLUCENT_FLAGS_CHANGED) != 0) {
                // Recompute system ui visibility.
                mAttachInfo.mRecomputeGlobalAttributes = true;
            }
            if ((mWindowAttributesChangesFlag & android.view.WindowManager.LayoutParams.LAYOUT_CHANGED) != 0) {
                // Request to update light center.
                mAttachInfo.mNeedsUpdateLightCenter = true;
            }
            if (mWindowAttributes.packageName == null) {
                mWindowAttributes.packageName = mBasePackageName;
            }
            mWindowAttributes.privateFlags |= compatibleWindowFlag;
            if (mWindowAttributes.preservePreviousSurfaceInsets) {
                // Restore old surface insets.
                mWindowAttributes.surfaceInsets.set(oldInsetLeft, oldInsetTop, oldInsetRight, oldInsetBottom);
                mWindowAttributes.hasManualSurfaceInsets = oldHasManualSurfaceInsets;
            } else
                if ((((mWindowAttributes.surfaceInsets.left != oldInsetLeft) || (mWindowAttributes.surfaceInsets.top != oldInsetTop)) || (mWindowAttributes.surfaceInsets.right != oldInsetRight)) || (mWindowAttributes.surfaceInsets.bottom != oldInsetBottom)) {
                    mNeedsRendererSetup = true;
                }

            applyKeepScreenOnFlag(mWindowAttributes);
            if (newView) {
                mSoftInputMode = attrs.softInputMode;
                requestLayout();
            }
            // Don't lose the mode we last auto-computed.
            if ((attrs.softInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST) == android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED) {
                mWindowAttributes.softInputMode = (mWindowAttributes.softInputMode & (~android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST)) | (oldSoftInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
            }
            mWindowAttributesChanged = true;
            scheduleTraversals();
        }
    }

    void handleAppVisibility(boolean visible) {
        if (mAppVisible != visible) {
            mAppVisible = visible;
            mAppVisibilityChanged = true;
            scheduleTraversals();
            if (!mAppVisible) {
                android.view.WindowManagerGlobal.trimForeground();
            }
        }
    }

    void handleGetNewSurface() {
        mNewSurfaceNeeded = true;
        mFullRedrawNeeded = true;
        scheduleTraversals();
    }

    private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() {
        @java.lang.Override
        public void onDisplayChanged(int displayId) {
            if ((mView != null) && (mDisplay.getDisplayId() == displayId)) {
                final int oldDisplayState = mAttachInfo.mDisplayState;
                final int newDisplayState = mDisplay.getState();
                if (oldDisplayState != newDisplayState) {
                    mAttachInfo.mDisplayState = newDisplayState;
                    pokeDrawLockIfNeeded();
                    if (oldDisplayState != android.view.Display.STATE_UNKNOWN) {
                        final int oldScreenState = toViewScreenState(oldDisplayState);
                        final int newScreenState = toViewScreenState(newDisplayState);
                        if (oldScreenState != newScreenState) {
                            mView.dispatchScreenStateChanged(newScreenState);
                        }
                        if (oldDisplayState == android.view.Display.STATE_OFF) {
                            // Draw was suppressed so we need to for it to happen here.
                            mFullRedrawNeeded = true;
                            scheduleTraversals();
                        }
                    }
                }
            }
        }

        @java.lang.Override
        public void onDisplayRemoved(int displayId) {
        }

        @java.lang.Override
        public void onDisplayAdded(int displayId) {
        }

        private int toViewScreenState(int displayState) {
            return displayState == android.view.Display.STATE_OFF ? android.view.View.SCREEN_STATE_OFF : android.view.View.SCREEN_STATE_ON;
        }
    };

    /**
     * Notify about move to a different display.
     *
     * @param displayId
     * 		The id of the display where this view root is moved to.
     * @param config
     * 		Configuration of the resources on new display after move.
     * @unknown 
     */
    public void onMovedToDisplay(int displayId, android.content.res.Configuration config) {
        if (mDisplay.getDisplayId() == displayId) {
            return;
        }
        // Get new instance of display based on current display adjustments. It may be updated later
        // if moving between the displays also involved a configuration change.
        updateInternalDisplay(displayId, mView.getResources());
        mAttachInfo.mDisplayState = mDisplay.getState();
        // Internal state updated, now notify the view hierarchy.
        mView.dispatchMovedToDisplay(mDisplay, config);
    }

    /**
     * Updates {@link #mDisplay} to the display object corresponding to {@param displayId}.
     * Uses DEFAULT_DISPLAY if there isn't a display object in the system corresponding
     * to {@param displayId}.
     */
    private void updateInternalDisplay(int displayId, android.content.res.Resources resources) {
        final android.view.Display preferredDisplay = android.app.ResourcesManager.getInstance().getAdjustedDisplay(displayId, resources);
        if (preferredDisplay == null) {
            // Fallback to use default display.
            android.util.Slog.w(android.view.ViewRootImpl.TAG, "Cannot get desired display with Id: " + displayId);
            mDisplay = android.app.ResourcesManager.getInstance().getAdjustedDisplay(android.view.Display.DEFAULT_DISPLAY, resources);
        } else {
            mDisplay = preferredDisplay;
        }
        mContext.updateDisplay(mDisplay.getDisplayId());
    }

    void pokeDrawLockIfNeeded() {
        final int displayState = mAttachInfo.mDisplayState;
        if ((((mView != null) && mAdded) && mTraversalScheduled) && ((displayState == android.view.Display.STATE_DOZE) || (displayState == android.view.Display.STATE_DOZE_SUSPEND))) {
            try {
                mWindowSession.pokeDrawLock(mWindow);
            } catch (android.os.RemoteException ex) {
                // System server died, oh well.
            }
        }
    }

    @java.lang.Override
    public void requestFitSystemWindows() {
        checkThread();
        mApplyInsetsRequested = true;
        scheduleTraversals();
    }

    void notifyInsetsChanged() {
        if (android.view.ViewRootImpl.sNewInsetsMode == android.view.ViewRootImpl.NEW_INSETS_MODE_NONE) {
            return;
        }
        mApplyInsetsRequested = true;
        // If this changes during traversal, no need to schedule another one as it will dispatch it
        // during the current traversal.
        if (!mIsInTraversal) {
            scheduleTraversals();
        }
    }

    @java.lang.Override
    public void requestLayout() {
        if (!mHandlingLayoutInLayoutRequest) {
            checkThread();
            mLayoutRequested = true;
            scheduleTraversals();
        }
    }

    @java.lang.Override
    public boolean isLayoutRequested() {
        return mLayoutRequested;
    }

    @java.lang.Override
    public void onDescendantInvalidated(@android.annotation.NonNull
    android.view.View child, @android.annotation.NonNull
    android.view.View descendant) {
        // TODO: Re-enable after camera is fixed or consider targetSdk checking this
        // checkThread();
        if ((descendant.mPrivateFlags & android.view.View.PFLAG_DRAW_ANIMATION) != 0) {
            mIsAnimating = true;
        }
        invalidate();
    }

    @android.annotation.UnsupportedAppUsage
    void invalidate() {
        mDirty.set(0, 0, mWidth, mHeight);
        if (!mWillDrawSoon) {
            scheduleTraversals();
        }
    }

    void invalidateWorld(android.view.View view) {
        view.invalidate();
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup parent = ((android.view.ViewGroup) (view));
            for (int i = 0; i < parent.getChildCount(); i++) {
                invalidateWorld(parent.getChildAt(i));
            }
        }
    }

    @java.lang.Override
    public void invalidateChild(android.view.View child, android.graphics.Rect dirty) {
        invalidateChildInParent(null, dirty);
    }

    @java.lang.Override
    public android.view.ViewParent invalidateChildInParent(int[] location, android.graphics.Rect dirty) {
        checkThread();
        if (android.view.ViewRootImpl.DEBUG_DRAW)
            android.util.Log.v(mTag, "Invalidate child: " + dirty);

        if (dirty == null) {
            invalidate();
            return null;
        } else
            if (dirty.isEmpty() && (!mIsAnimating)) {
                return null;
            }

        if ((mCurScrollY != 0) || (mTranslator != null)) {
            mTempRect.set(dirty);
            dirty = mTempRect;
            if (mCurScrollY != 0) {
                dirty.offset(0, -mCurScrollY);
            }
            if (mTranslator != null) {
                mTranslator.translateRectInAppWindowToScreen(dirty);
            }
            if (mAttachInfo.mScalingRequired) {
                dirty.inset(-1, -1);
            }
        }
        invalidateRectOnScreen(dirty);
        return null;
    }

    private void invalidateRectOnScreen(android.graphics.Rect dirty) {
        final android.graphics.Rect localDirty = mDirty;
        // Add the new dirty rect to the current one
        localDirty.union(dirty.left, dirty.top, dirty.right, dirty.bottom);
        // Intersect with the bounds of the window to skip
        // updates that lie outside of the visible region
        final float appScale = mAttachInfo.mApplicationScale;
        final boolean intersected = localDirty.intersect(0, 0, ((int) ((mWidth * appScale) + 0.5F)), ((int) ((mHeight * appScale) + 0.5F)));
        if (!intersected) {
            localDirty.setEmpty();
        }
        if ((!mWillDrawSoon) && (intersected || mIsAnimating)) {
            scheduleTraversals();
        }
    }

    public void setIsAmbientMode(boolean ambient) {
        mIsAmbientMode = ambient;
    }

    interface WindowStoppedCallback {
        public void windowStopped(boolean stopped);
    }

    private final java.util.ArrayList<android.view.ViewRootImpl.WindowStoppedCallback> mWindowStoppedCallbacks = new java.util.ArrayList<>();

    void addWindowStoppedCallback(android.view.ViewRootImpl.WindowStoppedCallback c) {
        mWindowStoppedCallbacks.add(c);
    }

    void removeWindowStoppedCallback(android.view.ViewRootImpl.WindowStoppedCallback c) {
        mWindowStoppedCallbacks.remove(c);
    }

    void setWindowStopped(boolean stopped) {
        checkThread();
        if (mStopped != stopped) {
            mStopped = stopped;
            final android.view.ThreadedRenderer renderer = mAttachInfo.mThreadedRenderer;
            if (renderer != null) {
                if (android.view.ViewRootImpl.DEBUG_DRAW)
                    android.util.Log.d(mTag, (("WindowStopped on " + getTitle()) + " set to ") + mStopped);

                renderer.setStopped(mStopped);
            }
            if (!mStopped) {
                mNewSurfaceNeeded = true;
                scheduleTraversals();
            } else {
                if (renderer != null) {
                    renderer.destroyHardwareResources(mView);
                }
            }
            for (int i = 0; i < mWindowStoppedCallbacks.size(); i++) {
                mWindowStoppedCallbacks.get(i).windowStopped(stopped);
            }
            if (mStopped) {
                if ((mSurfaceHolder != null) && mSurface.isValid()) {
                    notifySurfaceDestroyed();
                }
                destroySurface();
            }
        }
    }

    /**
     * Creates a surface as a child of {@code mSurface} with the same bounds as its parent and
     * crop bounds set to the parent's bounds adjusted for surface insets.
     *
     * @param zOrderLayer
     * 		Z order relative to the parent surface.
     */
    public void createBoundsSurface(int zOrderLayer) {
        if (mSurfaceSession == null) {
            mSurfaceSession = new android.view.SurfaceSession();
        }
        if ((mBoundsSurfaceControl != null) && mBoundsSurface.isValid()) {
            return;// surface control for bounds surface already exists.

        }
        mBoundsSurfaceControl = new android.view.SurfaceControl.Builder(mSurfaceSession).setName("Bounds for - " + getTitle().toString()).setParent(mSurfaceControl).build();
        setBoundsSurfaceCrop();
        mTransaction.setLayer(mBoundsSurfaceControl, zOrderLayer).show(mBoundsSurfaceControl).apply();
        mBoundsSurface.copyFrom(mBoundsSurfaceControl);
    }

    private void setBoundsSurfaceCrop() {
        // mWinFrame is already adjusted for surface insets. So offset it and use it as
        // the cropping bounds.
        mTempBoundsRect.set(mWinFrame);
        mTempBoundsRect.offsetTo(mWindowAttributes.surfaceInsets.left, mWindowAttributes.surfaceInsets.top);
        mTransaction.setWindowCrop(mBoundsSurfaceControl, mTempBoundsRect);
    }

    /**
     * Called after window layout to update the bounds surface. If the surface insets have
     * changed or the surface has resized, update the bounds surface.
     */
    private void updateBoundsSurface() {
        if ((mBoundsSurfaceControl != null) && mSurface.isValid()) {
            setBoundsSurfaceCrop();
            mTransaction.deferTransactionUntilSurface(mBoundsSurfaceControl, mSurface, mSurface.getNextFrameNumber()).apply();
        }
    }

    private void destroySurface() {
        mSurface.release();
        mSurfaceControl.release();
        mSurfaceSession = null;
        if (mBoundsSurfaceControl != null) {
            mBoundsSurfaceControl.remove();
            mBoundsSurface.release();
            mBoundsSurfaceControl = null;
        }
    }

    /**
     * Block the input events during an Activity Transition. The KEYCODE_BACK event is allowed
     * through to allow quick reversal of the Activity Transition.
     *
     * @param paused
     * 		true to pause, false to resume.
     */
    public void setPausedForTransition(boolean paused) {
        mPausedForTransition = paused;
    }

    @java.lang.Override
    public android.view.ViewParent getParent() {
        return null;
    }

    @java.lang.Override
    public boolean getChildVisibleRect(android.view.View child, android.graphics.Rect r, android.graphics.Point offset) {
        if (child != mView) {
            throw new java.lang.RuntimeException("child is not mine, honest!");
        }
        // Note: don't apply scroll offset, because we want to know its
        // visibility in the virtual canvas being given to the view hierarchy.
        return r.intersect(0, 0, mWidth, mHeight);
    }

    @java.lang.Override
    public void bringChildToFront(android.view.View child) {
    }

    int getHostVisibility() {
        return mAppVisible || mForceDecorViewVisibility ? mView.getVisibility() : android.view.View.GONE;
    }

    /**
     * Add LayoutTransition to the list of transitions to be started in the next traversal.
     * This list will be cleared after the transitions on the list are start()'ed. These
     * transitionsa re added by LayoutTransition itself when it sets up animations. The setup
     * happens during the layout phase of traversal, which we want to complete before any of the
     * animations are started (because those animations may side-effect properties that layout
     * depends upon, like the bounding rectangles of the affected views). So we add the transition
     * to the list and it is started just prior to starting the drawing phase of traversal.
     *
     * @param transition
     * 		The LayoutTransition to be started on the next traversal.
     * @unknown 
     */
    public void requestTransitionStart(android.animation.LayoutTransition transition) {
        if ((mPendingTransitions == null) || (!mPendingTransitions.contains(transition))) {
            if (mPendingTransitions == null) {
                mPendingTransitions = new java.util.ArrayList<android.animation.LayoutTransition>();
            }
            mPendingTransitions.add(transition);
        }
    }

    /**
     * Notifies the HardwareRenderer that a new frame will be coming soon.
     * Currently only {@link ThreadedRenderer} cares about this, and uses
     * this knowledge to adjust the scheduling of off-thread animations
     */
    void notifyRendererOfFramePending() {
        if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.notifyFramePending();
        }
    }

    @android.annotation.UnsupportedAppUsage
    void scheduleTraversals() {
        if (!mTraversalScheduled) {
            mTraversalScheduled = true;
            mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();
            mChoreographer.postCallback(android.view.Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
            if (!mUnbufferedInputDispatch) {
                scheduleConsumeBatchedInput();
            }
            notifyRendererOfFramePending();
            pokeDrawLockIfNeeded();
        }
    }

    void unscheduleTraversals() {
        if (mTraversalScheduled) {
            mTraversalScheduled = false;
            mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);
            mChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
        }
    }

    void doTraversal() {
        if (mTraversalScheduled) {
            mTraversalScheduled = false;
            mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);
            if (mProfile) {
                android.os.Debug.startMethodTracing("ViewAncestor");
            }
            performTraversals();
            if (mProfile) {
                android.os.Debug.stopMethodTracing();
                mProfile = false;
            }
        }
    }

    private void applyKeepScreenOnFlag(android.view.WindowManager.LayoutParams params) {
        // Update window's global keep screen on flag: if a view has requested
        // that the screen be kept on, then it is always set; otherwise, it is
        // set to whatever the client last requested for the global state.
        if (mAttachInfo.mKeepScreenOn) {
            params.flags |= android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        } else {
            params.flags = (params.flags & (~android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)) | (mClientWindowLayoutFlags & android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private boolean collectViewAttributes() {
        if (mAttachInfo.mRecomputeGlobalAttributes) {
            // Log.i(mTag, "Computing view hierarchy attributes!");
            mAttachInfo.mRecomputeGlobalAttributes = false;
            boolean oldScreenOn = mAttachInfo.mKeepScreenOn;
            mAttachInfo.mKeepScreenOn = false;
            mAttachInfo.mSystemUiVisibility = 0;
            mAttachInfo.mHasSystemUiListeners = false;
            mView.dispatchCollectViewAttributes(mAttachInfo, 0);
            mAttachInfo.mSystemUiVisibility &= ~mAttachInfo.mDisabledSystemUiVisibility;
            android.view.WindowManager.LayoutParams params = mWindowAttributes;
            mAttachInfo.mSystemUiVisibility |= getImpliedSystemUiVisibility(params);
            if (((mAttachInfo.mKeepScreenOn != oldScreenOn) || (mAttachInfo.mSystemUiVisibility != params.subtreeSystemUiVisibility)) || (mAttachInfo.mHasSystemUiListeners != params.hasSystemUiListeners)) {
                applyKeepScreenOnFlag(params);
                params.subtreeSystemUiVisibility = mAttachInfo.mSystemUiVisibility;
                params.hasSystemUiListeners = mAttachInfo.mHasSystemUiListeners;
                mView.dispatchWindowSystemUiVisiblityChanged(mAttachInfo.mSystemUiVisibility);
                return true;
            }
        }
        return false;
    }

    private int getImpliedSystemUiVisibility(android.view.WindowManager.LayoutParams params) {
        int vis = 0;
        // Translucent decor window flags imply stable system ui visibility.
        if ((params.flags & android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0) {
            vis |= android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        if ((params.flags & android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) != 0) {
            vis |= android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        return vis;
    }

    private boolean measureHierarchy(final android.view.View host, final android.view.WindowManager.LayoutParams lp, final android.content.res.Resources res, final int desiredWindowWidth, final int desiredWindowHeight) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        boolean windowSizeMayChange = false;
        if (android.view.ViewRootImpl.DEBUG_ORIENTATION || android.view.ViewRootImpl.DEBUG_LAYOUT)
            android.util.Log.v(mTag, ((((("Measuring " + host) + " in display ") + desiredWindowWidth) + "x") + desiredWindowHeight) + "...");

        boolean goodMeasure = false;
        if (lp.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
            // On large screens, we don't want to allow dialogs to just
            // stretch to fill the entire width of the screen to display
            // one line of text.  First try doing the layout at a smaller
            // size to see if it will fit.
            final android.util.DisplayMetrics packageMetrics = res.getDisplayMetrics();
            res.getValue(com.android.internal.R.dimen.config_prefDialogWidth, mTmpValue, true);
            int baseSize = 0;
            if (mTmpValue.type == android.util.TypedValue.TYPE_DIMENSION) {
                baseSize = ((int) (mTmpValue.getDimension(packageMetrics)));
            }
            if (android.view.ViewRootImpl.DEBUG_DIALOG)
                android.util.Log.v(mTag, (((("Window " + mView) + ": baseSize=") + baseSize) + ", desiredWindowWidth=") + desiredWindowWidth);

            if ((baseSize != 0) && (desiredWindowWidth > baseSize)) {
                childWidthMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(baseSize, lp.width);
                childHeightMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(desiredWindowHeight, lp.height);
                performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                if (android.view.ViewRootImpl.DEBUG_DIALOG)
                    android.util.Log.v(mTag, (((((((("Window " + mView) + ": measured (") + host.getMeasuredWidth()) + ",") + host.getMeasuredHeight()) + ") from width spec: ") + android.view.View.MeasureSpec.toString(childWidthMeasureSpec)) + " and height spec: ") + android.view.View.MeasureSpec.toString(childHeightMeasureSpec));

                if ((host.getMeasuredWidthAndState() & android.view.View.MEASURED_STATE_TOO_SMALL) == 0) {
                    goodMeasure = true;
                } else {
                    // Didn't fit in that size... try expanding a bit.
                    baseSize = (baseSize + desiredWindowWidth) / 2;
                    if (android.view.ViewRootImpl.DEBUG_DIALOG)
                        android.util.Log.v(mTag, (("Window " + mView) + ": next baseSize=") + baseSize);

                    childWidthMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(baseSize, lp.width);
                    performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                    if (android.view.ViewRootImpl.DEBUG_DIALOG)
                        android.util.Log.v(mTag, ((((("Window " + mView) + ": measured (") + host.getMeasuredWidth()) + ",") + host.getMeasuredHeight()) + ")");

                    if ((host.getMeasuredWidthAndState() & android.view.View.MEASURED_STATE_TOO_SMALL) == 0) {
                        if (android.view.ViewRootImpl.DEBUG_DIALOG)
                            android.util.Log.v(mTag, "Good!");

                        goodMeasure = true;
                    }
                }
            }
        }
        if (!goodMeasure) {
            childWidthMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(desiredWindowWidth, lp.width);
            childHeightMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(desiredWindowHeight, lp.height);
            performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
            if ((mWidth != host.getMeasuredWidth()) || (mHeight != host.getMeasuredHeight())) {
                windowSizeMayChange = true;
            }
        }
        if (android.view.ViewRootImpl.DBG) {
            java.lang.System.out.println("======================================");
            java.lang.System.out.println("performTraversals -- after measure");
            host.debug();
        }
        return windowSizeMayChange;
    }

    /**
     * Modifies the input matrix such that it maps view-local coordinates to
     * on-screen coordinates.
     *
     * @param m
     * 		input matrix to modify
     */
    void transformMatrixToGlobal(android.graphics.Matrix m) {
        m.preTranslate(mAttachInfo.mWindowLeft, mAttachInfo.mWindowTop);
    }

    /**
     * Modifies the input matrix such that it maps on-screen coordinates to
     * view-local coordinates.
     *
     * @param m
     * 		input matrix to modify
     */
    void transformMatrixToLocal(android.graphics.Matrix m) {
        m.postTranslate(-mAttachInfo.mWindowLeft, -mAttachInfo.mWindowTop);
    }

    /* package */
    android.view.WindowInsets getWindowInsets(boolean forceConstruct) {
        if ((mLastWindowInsets == null) || forceConstruct) {
            mDispatchContentInsets.set(mAttachInfo.mContentInsets);
            mDispatchStableInsets.set(mAttachInfo.mStableInsets);
            mDispatchDisplayCutout = mAttachInfo.mDisplayCutout.get();
            android.graphics.Rect contentInsets = mDispatchContentInsets;
            android.graphics.Rect stableInsets = mDispatchStableInsets;
            android.view.DisplayCutout displayCutout = mDispatchDisplayCutout;
            // For dispatch we preserve old logic, but for direct requests from Views we allow to
            // immediately use pending insets. This is such that getRootWindowInsets returns the
            // result from the layout hint before we ran a traversal shortly after adding a window.
            if ((!forceConstruct) && (((!mPendingContentInsets.equals(contentInsets)) || (!mPendingStableInsets.equals(stableInsets))) || (!mPendingDisplayCutout.get().equals(displayCutout)))) {
                contentInsets = mPendingContentInsets;
                stableInsets = mPendingStableInsets;
                displayCutout = mPendingDisplayCutout.get();
            }
            android.graphics.Rect outsets = mAttachInfo.mOutsets;
            if ((((outsets.left > 0) || (outsets.top > 0)) || (outsets.right > 0)) || (outsets.bottom > 0)) {
                contentInsets = new android.graphics.Rect(contentInsets.left + outsets.left, contentInsets.top + outsets.top, contentInsets.right + outsets.right, contentInsets.bottom + outsets.bottom);
            }
            contentInsets = ensureInsetsNonNegative(contentInsets, "content");
            stableInsets = ensureInsetsNonNegative(stableInsets, "stable");
            mLastWindowInsets = mInsetsController.calculateInsets(mContext.getResources().getConfiguration().isScreenRound(), mAttachInfo.mAlwaysConsumeSystemBars, displayCutout, contentInsets, stableInsets, mWindowAttributes.softInputMode);
        }
        return mLastWindowInsets;
    }

    private android.graphics.Rect ensureInsetsNonNegative(android.graphics.Rect insets, java.lang.String kind) {
        if ((((insets.left < 0) || (insets.top < 0)) || (insets.right < 0)) || (insets.bottom < 0)) {
            android.util.Log.wtf(mTag, (((("Negative " + kind) + "Insets: ") + insets) + ", mFirst=") + mFirst);
            return new android.graphics.Rect(java.lang.Math.max(0, insets.left), java.lang.Math.max(0, insets.top), java.lang.Math.max(0, insets.right), java.lang.Math.max(0, insets.bottom));
        }
        return insets;
    }

    void dispatchApplyInsets(android.view.View host) {
        android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "dispatchApplyInsets");
        android.view.WindowInsets insets = /* forceConstruct */
        getWindowInsets(true);
        final boolean dispatchCutout = mWindowAttributes.layoutInDisplayCutoutMode == android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        if (!dispatchCutout) {
            // Window is either not laid out in cutout or the status bar inset takes care of
            // clearing the cutout, so we don't need to dispatch the cutout to the hierarchy.
            insets = insets.consumeDisplayCutout();
        }
        host.dispatchApplyWindowInsets(insets);
        android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
    }

    android.view.InsetsController getInsetsController() {
        return mInsetsController;
    }

    private static boolean shouldUseDisplaySize(final android.view.WindowManager.LayoutParams lp) {
        return ((lp.type == android.view.WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL) || (lp.type == android.view.WindowManager.LayoutParams.TYPE_INPUT_METHOD)) || (lp.type == android.view.WindowManager.LayoutParams.TYPE_VOLUME_OVERLAY);
    }

    private int dipToPx(int dip) {
        final android.util.DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return ((int) ((displayMetrics.density * dip) + 0.5F));
    }

    private void performTraversals() {
        // cache mView since it is used so much below...
        final android.view.View host = mView;
        if (android.view.ViewRootImpl.DBG) {
            java.lang.System.out.println("======================================");
            java.lang.System.out.println("performTraversals");
            host.debug();
        }
        if ((host == null) || (!mAdded))
            return;

        mIsInTraversal = true;
        mWillDrawSoon = true;
        boolean windowSizeMayChange = false;
        boolean surfaceChanged = false;
        android.view.WindowManager.LayoutParams lp = mWindowAttributes;
        int desiredWindowWidth;
        int desiredWindowHeight;
        final int viewVisibility = getHostVisibility();
        final boolean viewVisibilityChanged = (!mFirst) && (((mViewVisibility != viewVisibility) || mNewSurfaceNeeded) || // Also check for possible double visibility update, which will make current
        // viewVisibility value equal to mViewVisibility and we may miss it.
        mAppVisibilityChanged);
        mAppVisibilityChanged = false;
        final boolean viewUserVisibilityChanged = (!mFirst) && ((mViewVisibility == android.view.View.VISIBLE) != (viewVisibility == android.view.View.VISIBLE));
        android.view.WindowManager.LayoutParams params = null;
        if (mWindowAttributesChanged) {
            mWindowAttributesChanged = false;
            surfaceChanged = true;
            params = lp;
        }
        android.content.res.CompatibilityInfo compatibilityInfo = mDisplay.getDisplayAdjustments().getCompatibilityInfo();
        if (compatibilityInfo.supportsScreen() == mLastInCompatMode) {
            params = lp;
            mFullRedrawNeeded = true;
            mLayoutRequested = true;
            if (mLastInCompatMode) {
                params.privateFlags &= ~android.view.WindowManager.LayoutParams.PRIVATE_FLAG_COMPATIBLE_WINDOW;
                mLastInCompatMode = false;
            } else {
                params.privateFlags |= android.view.WindowManager.LayoutParams.PRIVATE_FLAG_COMPATIBLE_WINDOW;
                mLastInCompatMode = true;
            }
        }
        mWindowAttributesChangesFlag = 0;
        android.graphics.Rect frame = mWinFrame;
        if (mFirst) {
            mFullRedrawNeeded = true;
            mLayoutRequested = true;
            final android.content.res.Configuration config = mContext.getResources().getConfiguration();
            if (android.view.ViewRootImpl.shouldUseDisplaySize(lp)) {
                // NOTE -- system code, won't try to do compat mode.
                android.graphics.Point size = new android.graphics.Point();
                mDisplay.getRealSize(size);
                desiredWindowWidth = size.x;
                desiredWindowHeight = size.y;
            } else {
                desiredWindowWidth = mWinFrame.width();
                desiredWindowHeight = mWinFrame.height();
            }
            // We used to use the following condition to choose 32 bits drawing caches:
            // PixelFormat.hasAlpha(lp.format) || lp.format == PixelFormat.RGBX_8888
            // However, windows are now always 32 bits by default, so choose 32 bits
            mAttachInfo.mUse32BitDrawingCache = true;
            mAttachInfo.mWindowVisibility = viewVisibility;
            mAttachInfo.mRecomputeGlobalAttributes = false;
            mLastConfigurationFromResources.setTo(config);
            mLastSystemUiVisibility = mAttachInfo.mSystemUiVisibility;
            // Set the layout direction if it has not been set before (inherit is the default)
            if (mViewLayoutDirectionInitial == android.view.View.LAYOUT_DIRECTION_INHERIT) {
                host.setLayoutDirection(config.getLayoutDirection());
            }
            host.dispatchAttachedToWindow(mAttachInfo, 0);
            mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(true);
            dispatchApplyInsets(host);
        } else {
            desiredWindowWidth = frame.width();
            desiredWindowHeight = frame.height();
            if ((desiredWindowWidth != mWidth) || (desiredWindowHeight != mHeight)) {
                if (android.view.ViewRootImpl.DEBUG_ORIENTATION)
                    android.util.Log.v(mTag, (("View " + host) + " resized to: ") + frame);

                mFullRedrawNeeded = true;
                mLayoutRequested = true;
                windowSizeMayChange = true;
            }
        }
        if (viewVisibilityChanged) {
            mAttachInfo.mWindowVisibility = viewVisibility;
            host.dispatchWindowVisibilityChanged(viewVisibility);
            if (viewUserVisibilityChanged) {
                host.dispatchVisibilityAggregated(viewVisibility == android.view.View.VISIBLE);
            }
            if ((viewVisibility != android.view.View.VISIBLE) || mNewSurfaceNeeded) {
                endDragResizing();
                destroyHardwareResources();
            }
            if (viewVisibility == android.view.View.GONE) {
                // After making a window gone, we will count it as being
                // shown for the first time the next time it gets focus.
                mHasHadWindowFocus = false;
            }
        }
        // Non-visible windows can't hold accessibility focus.
        if (mAttachInfo.mWindowVisibility != android.view.View.VISIBLE) {
            host.clearAccessibilityFocus();
        }
        // Execute enqueued actions on every traversal in case a detached view enqueued an action
        android.view.ViewRootImpl.getRunQueue().executeActions(mAttachInfo.mHandler);
        boolean insetsChanged = false;
        boolean layoutRequested = mLayoutRequested && ((!mStopped) || mReportNextDraw);
        if (layoutRequested) {
            final android.content.res.Resources res = mView.getContext().getResources();
            if (mFirst) {
                // make sure touch mode code executes by setting cached value
                // to opposite of the added touch mode.
                mAttachInfo.mInTouchMode = !mAddedTouchMode;
                ensureTouchModeLocally(mAddedTouchMode);
            } else {
                if (!mPendingOverscanInsets.equals(mAttachInfo.mOverscanInsets)) {
                    insetsChanged = true;
                }
                if (!mPendingContentInsets.equals(mAttachInfo.mContentInsets)) {
                    insetsChanged = true;
                }
                if (!mPendingStableInsets.equals(mAttachInfo.mStableInsets)) {
                    insetsChanged = true;
                }
                if (!mPendingDisplayCutout.equals(mAttachInfo.mDisplayCutout)) {
                    insetsChanged = true;
                }
                if (!mPendingVisibleInsets.equals(mAttachInfo.mVisibleInsets)) {
                    mAttachInfo.mVisibleInsets.set(mPendingVisibleInsets);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                        android.util.Log.v(mTag, "Visible insets changing to: " + mAttachInfo.mVisibleInsets);

                }
                if (!mPendingOutsets.equals(mAttachInfo.mOutsets)) {
                    insetsChanged = true;
                }
                if (mPendingAlwaysConsumeSystemBars != mAttachInfo.mAlwaysConsumeSystemBars) {
                    insetsChanged = true;
                }
                if ((lp.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) || (lp.height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT)) {
                    windowSizeMayChange = true;
                    if (android.view.ViewRootImpl.shouldUseDisplaySize(lp)) {
                        // NOTE -- system code, won't try to do compat mode.
                        android.graphics.Point size = new android.graphics.Point();
                        mDisplay.getRealSize(size);
                        desiredWindowWidth = size.x;
                        desiredWindowHeight = size.y;
                    } else {
                        android.content.res.Configuration config = res.getConfiguration();
                        desiredWindowWidth = dipToPx(config.screenWidthDp);
                        desiredWindowHeight = dipToPx(config.screenHeightDp);
                    }
                }
            }
            // Ask host how big it wants to be
            windowSizeMayChange |= measureHierarchy(host, lp, res, desiredWindowWidth, desiredWindowHeight);
        }
        if (collectViewAttributes()) {
            params = lp;
        }
        if (mAttachInfo.mForceReportNewAttributes) {
            mAttachInfo.mForceReportNewAttributes = false;
            params = lp;
        }
        if (mFirst || mAttachInfo.mViewVisibilityChanged) {
            mAttachInfo.mViewVisibilityChanged = false;
            int resizeMode = mSoftInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST;
            // If we are in auto resize mode, then we need to determine
            // what mode to use now.
            if (resizeMode == android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED) {
                final int N = mAttachInfo.mScrollContainers.size();
                for (int i = 0; i < N; i++) {
                    if (mAttachInfo.mScrollContainers.get(i).isShown()) {
                        resizeMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
                    }
                }
                if (resizeMode == 0) {
                    resizeMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
                }
                if ((lp.softInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST) != resizeMode) {
                    lp.softInputMode = (lp.softInputMode & (~android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST)) | resizeMode;
                    params = lp;
                }
            }
        }
        if (params != null) {
            if ((host.mPrivateFlags & android.view.View.PFLAG_REQUEST_TRANSPARENT_REGIONS) != 0) {
                if (!android.graphics.PixelFormat.formatHasAlpha(params.format)) {
                    params.format = android.graphics.PixelFormat.TRANSLUCENT;
                }
            }
            mAttachInfo.mOverscanRequested = (params.flags & android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN) != 0;
        }
        if (mApplyInsetsRequested) {
            mApplyInsetsRequested = false;
            mLastOverscanRequested = mAttachInfo.mOverscanRequested;
            dispatchApplyInsets(host);
            if (mLayoutRequested) {
                // Short-circuit catching a new layout request here, so
                // we don't need to go through two layout passes when things
                // change due to fitting system windows, which can happen a lot.
                windowSizeMayChange |= measureHierarchy(host, lp, mView.getContext().getResources(), desiredWindowWidth, desiredWindowHeight);
            }
        }
        if (layoutRequested) {
            // Clear this now, so that if anything requests a layout in the
            // rest of this function we will catch it and re-run a full
            // layout pass.
            mLayoutRequested = false;
        }
        boolean windowShouldResize = (layoutRequested && windowSizeMayChange) && ((((mWidth != host.getMeasuredWidth()) || (mHeight != host.getMeasuredHeight())) || (((lp.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) && (frame.width() < desiredWindowWidth)) && (frame.width() != mWidth))) || (((lp.height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) && (frame.height() < desiredWindowHeight)) && (frame.height() != mHeight)));
        windowShouldResize |= mDragResizing && (mResizeMode == android.view.WindowCallbacks.RESIZE_MODE_FREEFORM);
        // If the activity was just relaunched, it might have unfrozen the task bounds (while
        // relaunching), so we need to force a call into window manager to pick up the latest
        // bounds.
        windowShouldResize |= mActivityRelaunched;
        // Determine whether to compute insets.
        // If there are no inset listeners remaining then we may still need to compute
        // insets in case the old insets were non-empty and must be reset.
        final boolean computesInternalInsets = mAttachInfo.mTreeObserver.hasComputeInternalInsetsListeners() || mAttachInfo.mHasNonEmptyGivenInternalInsets;
        boolean insetsPending = false;
        int relayoutResult = 0;
        boolean updatedConfiguration = false;
        final int surfaceGenerationId = mSurface.getGenerationId();
        final boolean isViewVisible = viewVisibility == android.view.View.VISIBLE;
        final boolean windowRelayoutWasForced = mForceNextWindowRelayout;
        boolean surfaceSizeChanged = false;
        if (((((mFirst || windowShouldResize) || insetsChanged) || viewVisibilityChanged) || (params != null)) || mForceNextWindowRelayout) {
            mForceNextWindowRelayout = false;
            if (isViewVisible) {
                // If this window is giving internal insets to the window
                // manager, and it is being added or changing its visibility,
                // then we want to first give the window manager "fake"
                // insets to cause it to effectively ignore the content of
                // the window during layout.  This avoids it briefly causing
                // other windows to resize/move based on the raw frame of the
                // window, waiting until we can finish laying out this window
                // and get back to the window manager with the ultimately
                // computed insets.
                insetsPending = computesInternalInsets && (mFirst || viewVisibilityChanged);
            }
            if (mSurfaceHolder != null) {
                mSurfaceHolder.mSurfaceLock.lock();
                mDrawingAllowed = true;
            }
            boolean hwInitialized = false;
            boolean contentInsetsChanged = false;
            boolean hadSurface = mSurface.isValid();
            try {
                if (android.view.ViewRootImpl.DEBUG_LAYOUT) {
                    android.util.Log.i(mTag, (((("host=w:" + host.getMeasuredWidth()) + ", h:") + host.getMeasuredHeight()) + ", params=") + params);
                }
                if (mAttachInfo.mThreadedRenderer != null) {
                    // relayoutWindow may decide to destroy mSurface. As that decision
                    // happens in WindowManager service, we need to be defensive here
                    // and stop using the surface in case it gets destroyed.
                    if (mAttachInfo.mThreadedRenderer.pause()) {
                        // Animations were running so we need to push a frame
                        // to resume them
                        mDirty.set(0, 0, mWidth, mHeight);
                    }
                    mChoreographer.mFrameInfo.addFlags(android.graphics.FrameInfo.FLAG_WINDOW_LAYOUT_CHANGED);
                }
                relayoutResult = relayoutWindow(params, viewVisibility, insetsPending);
                if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                    android.util.Log.v(mTag, (((((((((((((("relayout: frame=" + frame.toShortString()) + " overscan=") + mPendingOverscanInsets.toShortString()) + " content=") + mPendingContentInsets.toShortString()) + " visible=") + mPendingVisibleInsets.toShortString()) + " stable=") + mPendingStableInsets.toShortString()) + " cutout=") + mPendingDisplayCutout.get().toString()) + " outsets=") + mPendingOutsets.toShortString()) + " surface=") + mSurface);

                // If the pending {@link MergedConfiguration} handed back from
                // {@link #relayoutWindow} does not match the one last reported,
                // WindowManagerService has reported back a frame from a configuration not yet
                // handled by the client. In this case, we need to accept the configuration so we
                // do not lay out and draw with the wrong configuration.
                if (!mPendingMergedConfiguration.equals(mLastReportedMergedConfiguration)) {
                    if (android.view.ViewRootImpl.DEBUG_CONFIGURATION)
                        android.util.Log.v(mTag, "Visible with new config: " + mPendingMergedConfiguration.getMergedConfiguration());

                    /* same display */
                    performConfigurationChange(mPendingMergedConfiguration, !mFirst, android.view.Display.INVALID_DISPLAY);
                    updatedConfiguration = true;
                }
                final boolean overscanInsetsChanged = !mPendingOverscanInsets.equals(mAttachInfo.mOverscanInsets);
                contentInsetsChanged = !mPendingContentInsets.equals(mAttachInfo.mContentInsets);
                final boolean visibleInsetsChanged = !mPendingVisibleInsets.equals(mAttachInfo.mVisibleInsets);
                final boolean stableInsetsChanged = !mPendingStableInsets.equals(mAttachInfo.mStableInsets);
                final boolean cutoutChanged = !mPendingDisplayCutout.equals(mAttachInfo.mDisplayCutout);
                final boolean outsetsChanged = !mPendingOutsets.equals(mAttachInfo.mOutsets);
                surfaceSizeChanged = (relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_SURFACE_RESIZED) != 0;
                surfaceChanged |= surfaceSizeChanged;
                final boolean alwaysConsumeSystemBarsChanged = mPendingAlwaysConsumeSystemBars != mAttachInfo.mAlwaysConsumeSystemBars;
                final boolean colorModeChanged = hasColorModeChanged(lp.getColorMode());
                if (contentInsetsChanged) {
                    mAttachInfo.mContentInsets.set(mPendingContentInsets);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                        android.util.Log.v(mTag, "Content insets changing to: " + mAttachInfo.mContentInsets);

                }
                if (overscanInsetsChanged) {
                    mAttachInfo.mOverscanInsets.set(mPendingOverscanInsets);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                        android.util.Log.v(mTag, "Overscan insets changing to: " + mAttachInfo.mOverscanInsets);

                    // Need to relayout with content insets.
                    contentInsetsChanged = true;
                }
                if (stableInsetsChanged) {
                    mAttachInfo.mStableInsets.set(mPendingStableInsets);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                        android.util.Log.v(mTag, "Decor insets changing to: " + mAttachInfo.mStableInsets);

                    // Need to relayout with content insets.
                    contentInsetsChanged = true;
                }
                if (cutoutChanged) {
                    mAttachInfo.mDisplayCutout.set(mPendingDisplayCutout);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT) {
                        android.util.Log.v(mTag, "DisplayCutout changing to: " + mAttachInfo.mDisplayCutout);
                    }
                    // Need to relayout with content insets.
                    contentInsetsChanged = true;
                }
                if (alwaysConsumeSystemBarsChanged) {
                    mAttachInfo.mAlwaysConsumeSystemBars = mPendingAlwaysConsumeSystemBars;
                    contentInsetsChanged = true;
                }
                if ((((contentInsetsChanged || (mLastSystemUiVisibility != mAttachInfo.mSystemUiVisibility)) || mApplyInsetsRequested) || (mLastOverscanRequested != mAttachInfo.mOverscanRequested)) || outsetsChanged) {
                    mLastSystemUiVisibility = mAttachInfo.mSystemUiVisibility;
                    mLastOverscanRequested = mAttachInfo.mOverscanRequested;
                    mAttachInfo.mOutsets.set(mPendingOutsets);
                    mApplyInsetsRequested = false;
                    dispatchApplyInsets(host);
                    // We applied insets so force contentInsetsChanged to ensure the
                    // hierarchy is measured below.
                    contentInsetsChanged = true;
                }
                if (visibleInsetsChanged) {
                    mAttachInfo.mVisibleInsets.set(mPendingVisibleInsets);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                        android.util.Log.v(mTag, "Visible insets changing to: " + mAttachInfo.mVisibleInsets);

                }
                if (colorModeChanged && (mAttachInfo.mThreadedRenderer != null)) {
                    mAttachInfo.mThreadedRenderer.setWideGamut(lp.getColorMode() == android.content.pm.ActivityInfo.COLOR_MODE_WIDE_COLOR_GAMUT);
                }
                if (!hadSurface) {
                    if (mSurface.isValid()) {
                        // If we are creating a new surface, then we need to
                        // completely redraw it.
                        mFullRedrawNeeded = true;
                        mPreviousTransparentRegion.setEmpty();
                        // Only initialize up-front if transparent regions are not
                        // requested, otherwise defer to see if the entire window
                        // will be transparent
                        if (mAttachInfo.mThreadedRenderer != null) {
                            try {
                                hwInitialized = mAttachInfo.mThreadedRenderer.initialize(mSurface);
                                if (hwInitialized && ((host.mPrivateFlags & android.view.View.PFLAG_REQUEST_TRANSPARENT_REGIONS) == 0)) {
                                    // Don't pre-allocate if transparent regions
                                    // are requested as they may not be needed
                                    mAttachInfo.mThreadedRenderer.allocateBuffers();
                                }
                            } catch (android.view.Surface.OutOfResourcesException e) {
                                handleOutOfResourcesException(e);
                                return;
                            }
                        }
                    }
                } else
                    if (!mSurface.isValid()) {
                        // If the surface has been removed, then reset the scroll
                        // positions.
                        if (mLastScrolledFocus != null) {
                            mLastScrolledFocus.clear();
                        }
                        mScrollY = mCurScrollY = 0;
                        if (mView instanceof com.android.internal.view.RootViewSurfaceTaker) {
                            ((com.android.internal.view.RootViewSurfaceTaker) (mView)).onRootViewScrollYChanged(mCurScrollY);
                        }
                        if (mScroller != null) {
                            mScroller.abortAnimation();
                        }
                        // Our surface is gone
                        if ((mAttachInfo.mThreadedRenderer != null) && mAttachInfo.mThreadedRenderer.isEnabled()) {
                            mAttachInfo.mThreadedRenderer.destroy();
                        }
                    } else
                        if ((((((surfaceGenerationId != mSurface.getGenerationId()) || surfaceSizeChanged) || windowRelayoutWasForced) || colorModeChanged) && (mSurfaceHolder == null)) && (mAttachInfo.mThreadedRenderer != null)) {
                            mFullRedrawNeeded = true;
                            try {
                                // Need to do updateSurface (which leads to CanvasContext::setSurface and
                                // re-create the EGLSurface) if either the Surface changed (as indicated by
                                // generation id), or WindowManager changed the surface size. The latter is
                                // because on some chips, changing the consumer side's BufferQueue size may
                                // not take effect immediately unless we create a new EGLSurface.
                                // Note that frame size change doesn't always imply surface size change (eg.
                                // drag resizing uses fullscreen surface), need to check surfaceSizeChanged
                                // flag from WindowManager.
                                mAttachInfo.mThreadedRenderer.updateSurface(mSurface);
                            } catch (android.view.Surface.OutOfResourcesException e) {
                                handleOutOfResourcesException(e);
                                return;
                            }
                        }


                final boolean freeformResizing = (relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_DRAG_RESIZING_FREEFORM) != 0;
                final boolean dockedResizing = (relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_DRAG_RESIZING_DOCKED) != 0;
                final boolean dragResizing = freeformResizing || dockedResizing;
                if (mDragResizing != dragResizing) {
                    if (dragResizing) {
                        mResizeMode = (freeformResizing) ? android.view.WindowCallbacks.RESIZE_MODE_FREEFORM : android.view.WindowCallbacks.RESIZE_MODE_DOCKED_DIVIDER;
                        final boolean backdropSizeMatchesFrame = (mWinFrame.width() == mPendingBackDropFrame.width()) && (mWinFrame.height() == mPendingBackDropFrame.height());
                        // TODO: Need cutout?
                        startDragResizing(mPendingBackDropFrame, !backdropSizeMatchesFrame, mPendingVisibleInsets, mPendingStableInsets, mResizeMode);
                    } else {
                        // We shouldn't come here, but if we come we should end the resize.
                        endDragResizing();
                    }
                }
                if (!mUseMTRenderer) {
                    if (dragResizing) {
                        mCanvasOffsetX = mWinFrame.left;
                        mCanvasOffsetY = mWinFrame.top;
                    } else {
                        mCanvasOffsetX = mCanvasOffsetY = 0;
                    }
                }
            } catch (android.os.RemoteException e) {
            }
            if (android.view.ViewRootImpl.DEBUG_ORIENTATION)
                android.util.Log.v(android.view.ViewRootImpl.TAG, (("Relayout returned: frame=" + frame) + ", surface=") + mSurface);

            mAttachInfo.mWindowLeft = frame.left;
            mAttachInfo.mWindowTop = frame.top;
            // !!FIXME!! This next section handles the case where we did not get the
            // window size we asked for. We should avoid this by getting a maximum size from
            // the window session beforehand.
            if ((mWidth != frame.width()) || (mHeight != frame.height())) {
                mWidth = frame.width();
                mHeight = frame.height();
            }
            if (mSurfaceHolder != null) {
                // The app owns the surface; tell it about what is going on.
                if (mSurface.isValid()) {
                    // XXX .copyFrom() doesn't work!
                    // mSurfaceHolder.mSurface.copyFrom(mSurface);
                    mSurfaceHolder.mSurface = mSurface;
                }
                mSurfaceHolder.setSurfaceFrameSize(mWidth, mHeight);
                mSurfaceHolder.mSurfaceLock.unlock();
                if (mSurface.isValid()) {
                    if (!hadSurface) {
                        mSurfaceHolder.ungetCallbacks();
                        mIsCreating = true;
                        android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                        if (callbacks != null) {
                            for (android.view.SurfaceHolder.Callback c : callbacks) {
                                c.surfaceCreated(mSurfaceHolder);
                            }
                        }
                        surfaceChanged = true;
                    }
                    if (surfaceChanged || (surfaceGenerationId != mSurface.getGenerationId())) {
                        android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                        if (callbacks != null) {
                            for (android.view.SurfaceHolder.Callback c : callbacks) {
                                c.surfaceChanged(mSurfaceHolder, lp.format, mWidth, mHeight);
                            }
                        }
                    }
                    mIsCreating = false;
                } else
                    if (hadSurface) {
                        notifySurfaceDestroyed();
                        mSurfaceHolder.mSurfaceLock.lock();
                        try {
                            mSurfaceHolder.mSurface = new android.view.Surface();
                        } finally {
                            mSurfaceHolder.mSurfaceLock.unlock();
                        }
                    }

            }
            final android.view.ThreadedRenderer threadedRenderer = mAttachInfo.mThreadedRenderer;
            if ((threadedRenderer != null) && threadedRenderer.isEnabled()) {
                if (((hwInitialized || (mWidth != threadedRenderer.getWidth())) || (mHeight != threadedRenderer.getHeight())) || mNeedsRendererSetup) {
                    threadedRenderer.setup(mWidth, mHeight, mAttachInfo, mWindowAttributes.surfaceInsets);
                    mNeedsRendererSetup = false;
                }
            }
            if ((!mStopped) || mReportNextDraw) {
                boolean focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_IN_TOUCH_MODE) != 0);
                if ((((focusChangedDueToTouchMode || (mWidth != host.getMeasuredWidth())) || (mHeight != host.getMeasuredHeight())) || contentInsetsChanged) || updatedConfiguration) {
                    int childWidthMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(mWidth, lp.width);
                    int childHeightMeasureSpec = android.view.ViewRootImpl.getRootMeasureSpec(mHeight, lp.height);
                    if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                        android.util.Log.v(mTag, (((((((("Ooops, something changed!  mWidth=" + mWidth) + " measuredWidth=") + host.getMeasuredWidth()) + " mHeight=") + mHeight) + " measuredHeight=") + host.getMeasuredHeight()) + " coveredInsetsChanged=") + contentInsetsChanged);

                    // Ask host how big it wants to be
                    performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                    // Implementation of weights from WindowManager.LayoutParams
                    // We just grow the dimensions as needed and re-measure if
                    // needs be
                    int width = host.getMeasuredWidth();
                    int height = host.getMeasuredHeight();
                    boolean measureAgain = false;
                    if (lp.horizontalWeight > 0.0F) {
                        width += ((int) ((mWidth - width) * lp.horizontalWeight));
                        childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
                        measureAgain = true;
                    }
                    if (lp.verticalWeight > 0.0F) {
                        height += ((int) ((mHeight - height) * lp.verticalWeight));
                        childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY);
                        measureAgain = true;
                    }
                    if (measureAgain) {
                        if (android.view.ViewRootImpl.DEBUG_LAYOUT)
                            android.util.Log.v(mTag, (("And hey let's measure once more: width=" + width) + " height=") + height);

                        performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                    }
                    layoutRequested = true;
                }
            }
        } else {
            // Not the first pass and no window/insets/visibility change but the window
            // may have moved and we need check that and if so to update the left and right
            // in the attach info. We translate only the window frame since on window move
            // the window manager tells us only for the new frame but the insets are the
            // same and we do not want to translate them more than once.
            maybeHandleWindowMove(frame);
        }
        if (surfaceSizeChanged) {
            updateBoundsSurface();
        }
        final boolean didLayout = layoutRequested && ((!mStopped) || mReportNextDraw);
        boolean triggerGlobalLayoutListener = didLayout || mAttachInfo.mRecomputeGlobalAttributes;
        if (didLayout) {
            performLayout(lp, mWidth, mHeight);
            // By this point all views have been sized and positioned
            // We can compute the transparent area
            if ((host.mPrivateFlags & android.view.View.PFLAG_REQUEST_TRANSPARENT_REGIONS) != 0) {
                // start out transparent
                // TODO: AVOID THAT CALL BY CACHING THE RESULT?
                host.getLocationInWindow(mTmpLocation);
                mTransparentRegion.set(mTmpLocation[0], mTmpLocation[1], (mTmpLocation[0] + host.mRight) - host.mLeft, (mTmpLocation[1] + host.mBottom) - host.mTop);
                host.gatherTransparentRegion(mTransparentRegion);
                if (mTranslator != null) {
                    mTranslator.translateRegionInWindowToScreen(mTransparentRegion);
                }
                if (!mTransparentRegion.equals(mPreviousTransparentRegion)) {
                    mPreviousTransparentRegion.set(mTransparentRegion);
                    mFullRedrawNeeded = true;
                    // reconfigure window manager
                    try {
                        mWindowSession.setTransparentRegion(mWindow, mTransparentRegion);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
            if (android.view.ViewRootImpl.DBG) {
                java.lang.System.out.println("======================================");
                java.lang.System.out.println("performTraversals -- after setFrame");
                host.debug();
            }
        }
        if (triggerGlobalLayoutListener) {
            mAttachInfo.mRecomputeGlobalAttributes = false;
            mAttachInfo.mTreeObserver.dispatchOnGlobalLayout();
        }
        if (computesInternalInsets) {
            // Clear the original insets.
            final android.view.ViewTreeObserver.InternalInsetsInfo insets = mAttachInfo.mGivenInternalInsets;
            insets.reset();
            // Compute new insets in place.
            mAttachInfo.mTreeObserver.dispatchOnComputeInternalInsets(insets);
            mAttachInfo.mHasNonEmptyGivenInternalInsets = !insets.isEmpty();
            // Tell the window manager.
            if (insetsPending || (!mLastGivenInsets.equals(insets))) {
                mLastGivenInsets.set(insets);
                // Translate insets to screen coordinates if needed.
                final android.graphics.Rect contentInsets;
                final android.graphics.Rect visibleInsets;
                final android.graphics.Region touchableRegion;
                if (mTranslator != null) {
                    contentInsets = mTranslator.getTranslatedContentInsets(insets.contentInsets);
                    visibleInsets = mTranslator.getTranslatedVisibleInsets(insets.visibleInsets);
                    touchableRegion = mTranslator.getTranslatedTouchableArea(insets.touchableRegion);
                } else {
                    contentInsets = insets.contentInsets;
                    visibleInsets = insets.visibleInsets;
                    touchableRegion = insets.touchableRegion;
                }
                try {
                    mWindowSession.setInsets(mWindow, insets.mTouchableInsets, contentInsets, visibleInsets, touchableRegion);
                } catch (android.os.RemoteException e) {
                }
            }
        }
        if (mFirst) {
            if (android.view.ViewRootImpl.sAlwaysAssignFocus || (!android.view.ViewRootImpl.isInTouchMode())) {
                // handle first focus request
                if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE) {
                    android.util.Log.v(mTag, "First: mView.hasFocus()=" + mView.hasFocus());
                }
                if (mView != null) {
                    if (!mView.hasFocus()) {
                        mView.restoreDefaultFocus();
                        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE) {
                            android.util.Log.v(mTag, "First: requested focused view=" + mView.findFocus());
                        }
                    } else {
                        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE) {
                            android.util.Log.v(mTag, "First: existing focused view=" + mView.findFocus());
                        }
                    }
                }
            } else {
                // Some views (like ScrollView) won't hand focus to descendants that aren't within
                // their viewport. Before layout, there's a good change these views are size 0
                // which means no children can get focus. After layout, this view now has size, but
                // is not guaranteed to hand-off focus to a focusable child (specifically, the edge-
                // case where the child has a size prior to layout and thus won't trigger
                // focusableViewAvailable).
                android.view.View focused = mView.findFocus();
                if ((focused instanceof android.view.ViewGroup) && (((android.view.ViewGroup) (focused)).getDescendantFocusability() == android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS)) {
                    focused.restoreDefaultFocus();
                }
            }
        }
        final boolean changedVisibility = (viewVisibilityChanged || mFirst) && isViewVisible;
        final boolean hasWindowFocus = mAttachInfo.mHasWindowFocus && isViewVisible;
        final boolean regainedFocus = hasWindowFocus && mLostWindowFocus;
        if (regainedFocus) {
            mLostWindowFocus = false;
        } else
            if ((!hasWindowFocus) && mHadWindowFocus) {
                mLostWindowFocus = true;
            }

        if (changedVisibility || regainedFocus) {
            // Toasts are presented as notifications - don't present them as windows as well
            boolean isToast = (mWindowAttributes == null) ? false : mWindowAttributes.type == android.view.WindowManager.LayoutParams.TYPE_TOAST;
            if (!isToast) {
                host.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            }
        }
        mFirst = false;
        mWillDrawSoon = false;
        mNewSurfaceNeeded = false;
        mActivityRelaunched = false;
        mViewVisibility = viewVisibility;
        mHadWindowFocus = hasWindowFocus;
        if (hasWindowFocus && (!isInLocalFocusMode())) {
            final boolean imTarget = android.view.WindowManager.LayoutParams.mayUseInputMethod(mWindowAttributes.flags);
            if (imTarget != mLastWasImTarget) {
                mLastWasImTarget = imTarget;
                android.view.inputmethod.InputMethodManager imm = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
                if ((imm != null) && imTarget) {
                    imm.onPreWindowFocus(mView, hasWindowFocus);
                    imm.onPostWindowFocus(mView, mView.findFocus(), mWindowAttributes.softInputMode, !mHasHadWindowFocus, mWindowAttributes.flags);
                }
            }
        }
        // Remember if we must report the next draw.
        if ((relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_FIRST_TIME) != 0) {
            reportNextDraw();
        }
        boolean cancelDraw = mAttachInfo.mTreeObserver.dispatchOnPreDraw() || (!isViewVisible);
        if (!cancelDraw) {
            if ((mPendingTransitions != null) && (mPendingTransitions.size() > 0)) {
                for (int i = 0; i < mPendingTransitions.size(); ++i) {
                    mPendingTransitions.get(i).startChangingAnimations();
                }
                mPendingTransitions.clear();
            }
            performDraw();
        } else {
            if (isViewVisible) {
                // Try again
                scheduleTraversals();
            } else
                if ((mPendingTransitions != null) && (mPendingTransitions.size() > 0)) {
                    for (int i = 0; i < mPendingTransitions.size(); ++i) {
                        mPendingTransitions.get(i).endChangingAnimations();
                    }
                    mPendingTransitions.clear();
                }

        }
        mIsInTraversal = false;
    }

    private void notifySurfaceDestroyed() {
        mSurfaceHolder.ungetCallbacks();
        android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
        if (callbacks != null) {
            for (android.view.SurfaceHolder.Callback c : callbacks) {
                c.surfaceDestroyed(mSurfaceHolder);
            }
        }
    }

    private void maybeHandleWindowMove(android.graphics.Rect frame) {
        // TODO: Well, we are checking whether the frame has changed similarly
        // to how this is done for the insets. This is however incorrect since
        // the insets and the frame are translated. For example, the old frame
        // was (1, 1 - 1, 1) and was translated to say (2, 2 - 2, 2), now the new
        // reported frame is (2, 2 - 2, 2) which implies no change but this is not
        // true since we are comparing a not translated value to a translated one.
        // This scenario is rare but we may want to fix that.
        final boolean windowMoved = (mAttachInfo.mWindowLeft != frame.left) || (mAttachInfo.mWindowTop != frame.top);
        if (windowMoved) {
            if (mTranslator != null) {
                mTranslator.translateRectInScreenToAppWinFrame(frame);
            }
            mAttachInfo.mWindowLeft = frame.left;
            mAttachInfo.mWindowTop = frame.top;
        }
        if (windowMoved || mAttachInfo.mNeedsUpdateLightCenter) {
            // Update the light position for the new offsets.
            if (mAttachInfo.mThreadedRenderer != null) {
                mAttachInfo.mThreadedRenderer.setLightCenter(mAttachInfo);
            }
            mAttachInfo.mNeedsUpdateLightCenter = false;
        }
    }

    private void handleWindowFocusChanged() {
        final boolean hasWindowFocus;
        final boolean inTouchMode;
        synchronized(this) {
            if (!mWindowFocusChanged) {
                return;
            }
            mWindowFocusChanged = false;
            hasWindowFocus = mUpcomingWindowFocus;
            inTouchMode = mUpcomingInTouchMode;
        }
        if (android.view.ViewRootImpl.sNewInsetsMode != android.view.ViewRootImpl.NEW_INSETS_MODE_NONE) {
            // TODO (b/131181940): Make sure this doesn't leak Activity with mActivityConfigCallback
            // config changes.
            if (hasWindowFocus) {
                mInsetsController.onWindowFocusGained();
            } else {
                mInsetsController.onWindowFocusLost();
            }
        }
        if (mAdded) {
            profileRendering(hasWindowFocus);
            if (hasWindowFocus) {
                ensureTouchModeLocally(inTouchMode);
                if ((mAttachInfo.mThreadedRenderer != null) && mSurface.isValid()) {
                    mFullRedrawNeeded = true;
                    try {
                        final android.view.WindowManager.LayoutParams lp = mWindowAttributes;
                        final android.graphics.Rect surfaceInsets = (lp != null) ? lp.surfaceInsets : null;
                        mAttachInfo.mThreadedRenderer.initializeIfNeeded(mWidth, mHeight, mAttachInfo, mSurface, surfaceInsets);
                    } catch (android.view.Surface.OutOfResourcesException e) {
                        android.util.Log.e(mTag, "OutOfResourcesException locking surface", e);
                        try {
                            if (!mWindowSession.outOfMemory(mWindow)) {
                                android.util.Slog.w(mTag, "No processes killed for memory;" + " killing self");
                                java.lang.Process.killProcess(java.lang.Process.myPid());
                            }
                        } catch (android.os.RemoteException ex) {
                        }
                        // Retry in a bit.
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(android.view.ViewRootImpl.MSG_WINDOW_FOCUS_CHANGED), 500);
                        return;
                    }
                }
            }
            mAttachInfo.mHasWindowFocus = hasWindowFocus;
            mLastWasImTarget = android.view.WindowManager.LayoutParams.mayUseInputMethod(mWindowAttributes.flags);
            android.view.inputmethod.InputMethodManager imm = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
            if (((imm != null) && mLastWasImTarget) && (!isInLocalFocusMode())) {
                imm.onPreWindowFocus(mView, hasWindowFocus);
            }
            if (mView != null) {
                mAttachInfo.mKeyDispatchState.reset();
                mView.dispatchWindowFocusChanged(hasWindowFocus);
                mAttachInfo.mTreeObserver.dispatchOnWindowFocusChange(hasWindowFocus);
                if (mAttachInfo.mTooltipHost != null) {
                    mAttachInfo.mTooltipHost.hideTooltip();
                }
            }
            // Note: must be done after the focus change callbacks,
            // so all of the view state is set up correctly.
            if (hasWindowFocus) {
                if (((imm != null) && mLastWasImTarget) && (!isInLocalFocusMode())) {
                    imm.onPostWindowFocus(mView, mView.findFocus(), mWindowAttributes.softInputMode, !mHasHadWindowFocus, mWindowAttributes.flags);
                }
                // Clear the forward bit.  We can just do this directly, since
                // the window manager doesn't care about it.
                mWindowAttributes.softInputMode &= ~android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;
                ((android.view.WindowManager.LayoutParams) (mView.getLayoutParams())).softInputMode &= ~android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;
                mHasHadWindowFocus = true;
                // Refocusing a window that has a focused view should fire a
                // focus event for the view since the global focused view changed.
                fireAccessibilityFocusEventIfHasFocusedNode();
            } else {
                if (mPointerCapture) {
                    handlePointerCaptureChanged(false);
                }
            }
        }
        mFirstInputStage.onWindowFocusChanged(hasWindowFocus);
    }

    private void fireAccessibilityFocusEventIfHasFocusedNode() {
        if (!android.view.accessibility.AccessibilityManager.getInstance(mContext).isEnabled()) {
            return;
        }
        final android.view.View focusedView = mView.findFocus();
        if (focusedView == null) {
            return;
        }
        final android.view.accessibility.AccessibilityNodeProvider provider = focusedView.getAccessibilityNodeProvider();
        if (provider == null) {
            focusedView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED);
        } else {
            final android.view.accessibility.AccessibilityNodeInfo focusedNode = findFocusedVirtualNode(provider);
            if (focusedNode != null) {
                final int virtualId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(focusedNode.getSourceNodeId());
                // This is a best effort since clearing and setting the focus via the
                // provider APIs could have side effects. We don't have a provider API
                // similar to that on View to ask a given event to be fired.
                final android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED);
                event.setSource(focusedView, virtualId);
                event.setPackageName(focusedNode.getPackageName());
                event.setChecked(focusedNode.isChecked());
                event.setContentDescription(focusedNode.getContentDescription());
                event.setPassword(focusedNode.isPassword());
                event.getText().add(focusedNode.getText());
                event.setEnabled(focusedNode.isEnabled());
                focusedView.getParent().requestSendAccessibilityEvent(focusedView, event);
                focusedNode.recycle();
            }
        }
    }

    private android.view.accessibility.AccessibilityNodeInfo findFocusedVirtualNode(android.view.accessibility.AccessibilityNodeProvider provider) {
        android.view.accessibility.AccessibilityNodeInfo focusedNode = provider.findFocus(android.view.accessibility.AccessibilityNodeInfo.FOCUS_INPUT);
        if (focusedNode != null) {
            return focusedNode;
        }
        if (!mContext.isAutofillCompatibilityEnabled()) {
            return null;
        }
        // Unfortunately some provider implementations don't properly
        // implement AccessibilityNodeProvider#findFocus
        android.view.accessibility.AccessibilityNodeInfo current = provider.createAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
        if (current.isFocused()) {
            return current;
        }
        final java.util.Queue<android.view.accessibility.AccessibilityNodeInfo> fringe = new java.util.LinkedList<>();
        fringe.offer(current);
        while (!fringe.isEmpty()) {
            current = fringe.poll();
            final android.util.LongArray childNodeIds = current.getChildNodeIds();
            if ((childNodeIds == null) || (childNodeIds.size() <= 0)) {
                continue;
            }
            final int childCount = childNodeIds.size();
            for (int i = 0; i < childCount; i++) {
                final int virtualId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(childNodeIds.get(i));
                final android.view.accessibility.AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(virtualId);
                if (child != null) {
                    if (child.isFocused()) {
                        return child;
                    }
                    fringe.offer(child);
                }
            }
            current.recycle();
        } 
        return null;
    }

    private void handleOutOfResourcesException(android.view.Surface.OutOfResourcesException e) {
        android.util.Log.e(mTag, "OutOfResourcesException initializing HW surface", e);
        try {
            if ((!mWindowSession.outOfMemory(mWindow)) && (java.lang.Process.myUid() != SYSTEM_UID)) {
                android.util.Slog.w(mTag, "No processes killed for memory; killing self");
                java.lang.Process.killProcess(java.lang.Process.myPid());
            }
        } catch (android.os.RemoteException ex) {
        }
        mLayoutRequested = true;// ask wm for a new surface next time.

    }

    private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
        if (mView == null) {
            return;
        }
        android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "measure");
        try {
            mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
    }

    /**
     * Called by {@link android.view.View#isInLayout()} to determine whether the view hierarchy
     * is currently undergoing a layout pass.
     *
     * @return whether the view hierarchy is currently undergoing a layout pass
     */
    boolean isInLayout() {
        return mInLayout;
    }

    /**
     * Called by {@link android.view.View#requestLayout()} if the view hierarchy is currently
     * undergoing a layout pass. requestLayout() should not generally be called during layout,
     * unless the container hierarchy knows what it is doing (i.e., it is fine as long as
     * all children in that container hierarchy are measured and laid out at the end of the layout
     * pass for that container). If requestLayout() is called anyway, we handle it correctly
     * by registering all requesters during a frame as it proceeds. At the end of the frame,
     * we check all of those views to see if any still have pending layout requests, which
     * indicates that they were not correctly handled by their container hierarchy. If that is
     * the case, we clear all such flags in the tree, to remove the buggy flag state that leads
     * to blank containers, and force a second request/measure/layout pass in this frame. If
     * more requestLayout() calls are received during that second layout pass, we post those
     * requests to the next frame to avoid possible infinite loops.
     *
     * <p>The return value from this method indicates whether the request should proceed
     * (if it is a request during the first layout pass) or should be skipped and posted to the
     * next frame (if it is a request during the second layout pass).</p>
     *
     * @param view
     * 		the view that requested the layout.
     * @return true if request should proceed, false otherwise.
     */
    boolean requestLayoutDuringLayout(final android.view.View view) {
        if ((view.mParent == null) || (view.mAttachInfo == null)) {
            // Would not normally trigger another layout, so just let it pass through as usual
            return true;
        }
        if (!mLayoutRequesters.contains(view)) {
            mLayoutRequesters.add(view);
        }
        if (!mHandlingLayoutInLayoutRequest) {
            // Let the request proceed normally; it will be processed in a second layout pass
            // if necessary
            return true;
        } else {
            // Don't let the request proceed during the second layout pass.
            // It will post to the next frame instead.
            return false;
        }
    }

    private void performLayout(android.view.WindowManager.LayoutParams lp, int desiredWindowWidth, int desiredWindowHeight) {
        mLayoutRequested = false;
        mScrollMayChange = true;
        mInLayout = true;
        final android.view.View host = mView;
        if (host == null) {
            return;
        }
        if (android.view.ViewRootImpl.DEBUG_ORIENTATION || android.view.ViewRootImpl.DEBUG_LAYOUT) {
            android.util.Log.v(mTag, ((((("Laying out " + host) + " to (") + host.getMeasuredWidth()) + ", ") + host.getMeasuredHeight()) + ")");
        }
        android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "layout");
        try {
            host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
            mInLayout = false;
            int numViewsRequestingLayout = mLayoutRequesters.size();
            if (numViewsRequestingLayout > 0) {
                // requestLayout() was called during layout.
                // If no layout-request flags are set on the requesting views, there is no problem.
                // If some requests are still pending, then we need to clear those flags and do
                // a full request/measure/layout pass to handle this situation.
                java.util.ArrayList<android.view.View> validLayoutRequesters = getValidLayoutRequesters(mLayoutRequesters, false);
                if (validLayoutRequesters != null) {
                    // Set this flag to indicate that any further requests are happening during
                    // the second pass, which may result in posting those requests to the next
                    // frame instead
                    mHandlingLayoutInLayoutRequest = true;
                    // Process fresh layout requests, then measure and layout
                    int numValidRequests = validLayoutRequesters.size();
                    for (int i = 0; i < numValidRequests; ++i) {
                        final android.view.View view = validLayoutRequesters.get(i);
                        android.util.Log.w("View", ("requestLayout() improperly called by " + view) + " during layout: running second layout pass");
                        view.requestLayout();
                    }
                    measureHierarchy(host, lp, mView.getContext().getResources(), desiredWindowWidth, desiredWindowHeight);
                    mInLayout = true;
                    host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                    mHandlingLayoutInLayoutRequest = false;
                    // Check the valid requests again, this time without checking/clearing the
                    // layout flags, since requests happening during the second pass get noop'd
                    validLayoutRequesters = getValidLayoutRequesters(mLayoutRequesters, true);
                    if (validLayoutRequesters != null) {
                        final java.util.ArrayList<android.view.View> finalRequesters = validLayoutRequesters;
                        // Post second-pass requests to the next frame
                        android.view.ViewRootImpl.getRunQueue().post(new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                int numValidRequests = finalRequesters.size();
                                for (int i = 0; i < numValidRequests; ++i) {
                                    final android.view.View view = finalRequesters.get(i);
                                    android.util.Log.w("View", ("requestLayout() improperly called by " + view) + " during second layout pass: posting in next frame");
                                    view.requestLayout();
                                }
                            }
                        });
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
        mInLayout = false;
    }

    /**
     * This method is called during layout when there have been calls to requestLayout() during
     * layout. It walks through the list of views that requested layout to determine which ones
     * still need it, based on visibility in the hierarchy and whether they have already been
     * handled (as is usually the case with ListView children).
     *
     * @param layoutRequesters
     * 		The list of views that requested layout during layout
     * @param secondLayoutRequests
     * 		Whether the requests were issued during the second layout pass.
     * 		If so, the FORCE_LAYOUT flag was not set on requesters.
     * @return A list of the actual views that still need to be laid out.
     */
    private java.util.ArrayList<android.view.View> getValidLayoutRequesters(java.util.ArrayList<android.view.View> layoutRequesters, boolean secondLayoutRequests) {
        int numViewsRequestingLayout = layoutRequesters.size();
        java.util.ArrayList<android.view.View> validLayoutRequesters = null;
        for (int i = 0; i < numViewsRequestingLayout; ++i) {
            android.view.View view = layoutRequesters.get(i);
            if ((((view != null) && (view.mAttachInfo != null)) && (view.mParent != null)) && (secondLayoutRequests || ((view.mPrivateFlags & android.view.View.PFLAG_FORCE_LAYOUT) == android.view.View.PFLAG_FORCE_LAYOUT))) {
                boolean gone = false;
                android.view.View parent = view;
                // Only trigger new requests for views in a non-GONE hierarchy
                while (parent != null) {
                    if ((parent.mViewFlags & android.view.View.VISIBILITY_MASK) == android.view.View.GONE) {
                        gone = true;
                        break;
                    }
                    if (parent.mParent instanceof android.view.View) {
                        parent = ((android.view.View) (parent.mParent));
                    } else {
                        parent = null;
                    }
                } 
                if (!gone) {
                    if (validLayoutRequesters == null) {
                        validLayoutRequesters = new java.util.ArrayList<android.view.View>();
                    }
                    validLayoutRequesters.add(view);
                }
            }
        }
        if (!secondLayoutRequests) {
            // If we're checking the layout flags, then we need to clean them up also
            for (int i = 0; i < numViewsRequestingLayout; ++i) {
                android.view.View view = layoutRequesters.get(i);
                while ((view != null) && ((view.mPrivateFlags & android.view.View.PFLAG_FORCE_LAYOUT) != 0)) {
                    view.mPrivateFlags &= ~android.view.View.PFLAG_FORCE_LAYOUT;
                    if (view.mParent instanceof android.view.View) {
                        view = ((android.view.View) (view.mParent));
                    } else {
                        view = null;
                    }
                } 
            }
        }
        layoutRequesters.clear();
        return validLayoutRequesters;
    }

    @java.lang.Override
    public void requestTransparentRegion(android.view.View child) {
        // the test below should not fail unless someone is messing with us
        checkThread();
        if (mView == child) {
            mView.mPrivateFlags |= android.view.View.PFLAG_REQUEST_TRANSPARENT_REGIONS;
            // Need to make sure we re-evaluate the window attributes next
            // time around, to ensure the window has the correct format.
            mWindowAttributesChanged = true;
            mWindowAttributesChangesFlag = 0;
            requestLayout();
        }
    }

    /**
     * Figures out the measure spec for the root view in a window based on it's
     * layout params.
     *
     * @param windowSize
     * 		The available width or height of the window
     * @param rootDimension
     * 		The layout params for one dimension (width or height) of the
     * 		window.
     * @return The measure spec to use to measure the root view.
     */
    private static int getRootMeasureSpec(int windowSize, int rootDimension) {
        int measureSpec;
        switch (rootDimension) {
            case android.view.ViewGroup.LayoutParams.MATCH_PARENT :
                // Window can't resize. Force root view to be windowSize.
                measureSpec = android.view.View.MeasureSpec.makeMeasureSpec(windowSize, android.view.View.MeasureSpec.EXACTLY);
                break;
            case android.view.ViewGroup.LayoutParams.WRAP_CONTENT :
                // Window can resize. Set max size for root view.
                measureSpec = android.view.View.MeasureSpec.makeMeasureSpec(windowSize, android.view.View.MeasureSpec.AT_MOST);
                break;
            default :
                // Window wants to be an exact size. Force root view to be that size.
                measureSpec = android.view.View.MeasureSpec.makeMeasureSpec(rootDimension, android.view.View.MeasureSpec.EXACTLY);
                break;
        }
        return measureSpec;
    }

    int mHardwareXOffset;

    int mHardwareYOffset;

    @java.lang.Override
    public void onPreDraw(android.graphics.RecordingCanvas canvas) {
        // If mCurScrollY is not 0 then this influences the hardwareYOffset. The end result is we
        // can apply offsets that are not handled by anything else, resulting in underdraw as
        // the View is shifted (thus shifting the window background) exposing unpainted
        // content. To handle this with minimal glitches we just clear to BLACK if the window
        // is opaque. If it's not opaque then HWUI already internally does a glClear to
        // transparent, so there's no risk of underdraw on non-opaque surfaces.
        if (((mCurScrollY != 0) && (mHardwareYOffset != 0)) && mAttachInfo.mThreadedRenderer.isOpaque()) {
            canvas.drawColor(android.graphics.Color.BLACK);
        }
        canvas.translate(-mHardwareXOffset, -mHardwareYOffset);
    }

    @java.lang.Override
    public void onPostDraw(android.graphics.RecordingCanvas canvas) {
        drawAccessibilityFocusedDrawableIfNeeded(canvas);
        if (mUseMTRenderer) {
            for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
                mWindowCallbacks.get(i).onPostDraw(canvas);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void outputDisplayList(android.view.View view) {
        view.mRenderNode.output();
    }

    /**
     *
     *
     * @see #PROPERTY_PROFILE_RENDERING
     */
    private void profileRendering(boolean enabled) {
        if (mProfileRendering) {
            mRenderProfilingEnabled = enabled;
            if (mRenderProfiler != null) {
                mChoreographer.removeFrameCallback(mRenderProfiler);
            }
            if (mRenderProfilingEnabled) {
                if (mRenderProfiler == null) {
                    mRenderProfiler = new android.view.Choreographer.FrameCallback() {
                        @java.lang.Override
                        public void doFrame(long frameTimeNanos) {
                            mDirty.set(0, 0, mWidth, mHeight);
                            scheduleTraversals();
                            if (mRenderProfilingEnabled) {
                                mChoreographer.postFrameCallback(mRenderProfiler);
                            }
                        }
                    };
                }
                mChoreographer.postFrameCallback(mRenderProfiler);
            } else {
                mRenderProfiler = null;
            }
        }
    }

    /**
     * Called from draw() when DEBUG_FPS is enabled
     */
    private void trackFPS() {
        // Tracks frames per second drawn. First value in a series of draws may be bogus
        // because it down not account for the intervening idle time
        long nowTime = java.lang.System.currentTimeMillis();
        if (mFpsStartTime < 0) {
            mFpsStartTime = mFpsPrevTime = nowTime;
            mFpsNumFrames = 0;
        } else {
            ++mFpsNumFrames;
            java.lang.String thisHash = java.lang.Integer.toHexString(java.lang.System.identityHashCode(this));
            long frameTime = nowTime - mFpsPrevTime;
            long totalTime = nowTime - mFpsStartTime;
            android.util.Log.v(mTag, (("0x" + thisHash) + "\tFrame time:\t") + frameTime);
            mFpsPrevTime = nowTime;
            if (totalTime > 1000) {
                float fps = (((float) (mFpsNumFrames)) * 1000) / totalTime;
                android.util.Log.v(mTag, (("0x" + thisHash) + "\tFPS:\t") + fps);
                mFpsStartTime = nowTime;
                mFpsNumFrames = 0;
            }
        }
    }

    /**
     * A count of the number of calls to pendingDrawFinished we
     * require to notify the WM drawing is complete.
     */
    int mDrawsNeededToReport = 0;

    /**
     * Delay notifying WM of draw finished until
     * a balanced call to pendingDrawFinished.
     */
    void drawPending() {
        mDrawsNeededToReport++;
    }

    void pendingDrawFinished() {
        if (mDrawsNeededToReport == 0) {
            throw new java.lang.RuntimeException("Unbalanced drawPending/pendingDrawFinished calls");
        }
        mDrawsNeededToReport--;
        if (mDrawsNeededToReport == 0) {
            reportDrawFinished();
        }
    }

    private void postDrawFinished() {
        mHandler.sendEmptyMessage(android.view.ViewRootImpl.MSG_DRAW_FINISHED);
    }

    private void reportDrawFinished() {
        try {
            mDrawsNeededToReport = 0;
            mWindowSession.finishDrawing(mWindow);
        } catch (android.os.RemoteException e) {
            // Have fun!
        }
    }

    private void performDraw() {
        if ((mAttachInfo.mDisplayState == android.view.Display.STATE_OFF) && (!mReportNextDraw)) {
            return;
        } else
            if (mView == null) {
                return;
            }

        final boolean fullRedrawNeeded = mFullRedrawNeeded || mReportNextDraw;
        mFullRedrawNeeded = false;
        mIsDrawing = true;
        android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "draw");
        boolean usingAsyncReport = false;
        if ((mAttachInfo.mThreadedRenderer != null) && mAttachInfo.mThreadedRenderer.isEnabled()) {
            java.util.ArrayList<java.lang.Runnable> commitCallbacks = mAttachInfo.mTreeObserver.captureFrameCommitCallbacks();
            if (mReportNextDraw) {
                usingAsyncReport = true;
                final android.os.Handler handler = mAttachInfo.mHandler;
                mAttachInfo.mThreadedRenderer.setFrameCompleteCallback((long frameNr) -> handler.postAtFrontOfQueue(() -> {
                    // TODO: Use the frame number
                    pendingDrawFinished();
                    if (commitCallbacks != null) {
                        for (int i = 0; i < commitCallbacks.size(); i++) {
                            run();
                        }
                    }
                }));
            } else
                if ((commitCallbacks != null) && (commitCallbacks.size() > 0)) {
                    final android.os.Handler handler = mAttachInfo.mHandler;
                    mAttachInfo.mThreadedRenderer.setFrameCompleteCallback((long frameNr) -> handler.postAtFrontOfQueue(() -> {
                        for (int i = 0; i < commitCallbacks.size(); i++) {
                            run();
                        }
                    }));
                }

        }
        try {
            boolean canUseAsync = draw(fullRedrawNeeded);
            if (usingAsyncReport && (!canUseAsync)) {
                mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(null);
                usingAsyncReport = false;
            }
        } finally {
            mIsDrawing = false;
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
        // For whatever reason we didn't create a HardwareRenderer, end any
        // hardware animations that are now dangling
        if (mAttachInfo.mPendingAnimatingRenderNodes != null) {
            final int count = mAttachInfo.mPendingAnimatingRenderNodes.size();
            for (int i = 0; i < count; i++) {
                mAttachInfo.mPendingAnimatingRenderNodes.get(i).endAllAnimators();
            }
            mAttachInfo.mPendingAnimatingRenderNodes.clear();
        }
        if (mReportNextDraw) {
            mReportNextDraw = false;
            // if we're using multi-thread renderer, wait for the window frame draws
            if (mWindowDrawCountDown != null) {
                try {
                    mWindowDrawCountDown.await();
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.e(mTag, "Window redraw count down interrupted!");
                }
                mWindowDrawCountDown = null;
            }
            if (mAttachInfo.mThreadedRenderer != null) {
                mAttachInfo.mThreadedRenderer.setStopped(mStopped);
            }
            if (android.view.ViewRootImpl.LOCAL_LOGV) {
                android.util.Log.v(mTag, "FINISHED DRAWING: " + mWindowAttributes.getTitle());
            }
            if ((mSurfaceHolder != null) && mSurface.isValid()) {
                com.android.internal.view.SurfaceCallbackHelper sch = new com.android.internal.view.SurfaceCallbackHelper(this::postDrawFinished);
                android.view.SurfaceHolder.Callback[] callbacks = mSurfaceHolder.getCallbacks();
                sch.dispatchSurfaceRedrawNeededAsync(mSurfaceHolder, callbacks);
            } else
                if (!usingAsyncReport) {
                    if (mAttachInfo.mThreadedRenderer != null) {
                        mAttachInfo.mThreadedRenderer.fence();
                    }
                    pendingDrawFinished();
                }

        }
    }

    private boolean draw(boolean fullRedrawNeeded) {
        android.view.Surface surface = mSurface;
        if (!surface.isValid()) {
            return false;
        }
        if (android.view.ViewRootImpl.DEBUG_FPS) {
            trackFPS();
        }
        if (!android.view.ViewRootImpl.sFirstDrawComplete) {
            synchronized(android.view.ViewRootImpl.sFirstDrawHandlers) {
                android.view.ViewRootImpl.sFirstDrawComplete = true;
                final int count = android.view.ViewRootImpl.sFirstDrawHandlers.size();
                for (int i = 0; i < count; i++) {
                    mHandler.post(android.view.ViewRootImpl.sFirstDrawHandlers.get(i));
                }
            }
        }
        scrollToRectOrFocus(null, false);
        if (mAttachInfo.mViewScrollChanged) {
            mAttachInfo.mViewScrollChanged = false;
            mAttachInfo.mTreeObserver.dispatchOnScrollChanged();
        }
        boolean animating = (mScroller != null) && mScroller.computeScrollOffset();
        final int curScrollY;
        if (animating) {
            curScrollY = mScroller.getCurrY();
        } else {
            curScrollY = mScrollY;
        }
        if (mCurScrollY != curScrollY) {
            mCurScrollY = curScrollY;
            fullRedrawNeeded = true;
            if (mView instanceof com.android.internal.view.RootViewSurfaceTaker) {
                ((com.android.internal.view.RootViewSurfaceTaker) (mView)).onRootViewScrollYChanged(mCurScrollY);
            }
        }
        final float appScale = mAttachInfo.mApplicationScale;
        final boolean scalingRequired = mAttachInfo.mScalingRequired;
        final android.graphics.Rect dirty = mDirty;
        if (mSurfaceHolder != null) {
            // The app owns the surface, we won't draw.
            dirty.setEmpty();
            if (animating && (mScroller != null)) {
                mScroller.abortAnimation();
            }
            return false;
        }
        if (fullRedrawNeeded) {
            dirty.set(0, 0, ((int) ((mWidth * appScale) + 0.5F)), ((int) ((mHeight * appScale) + 0.5F)));
        }
        if (android.view.ViewRootImpl.DEBUG_ORIENTATION || android.view.ViewRootImpl.DEBUG_DRAW) {
            android.util.Log.v(mTag, (((((((((((((((((((("Draw " + mView) + "/") + mWindowAttributes.getTitle()) + ": dirty={") + dirty.left) + ",") + dirty.top) + ",") + dirty.right) + ",") + dirty.bottom) + "} surface=") + surface) + " surface.isValid()=") + surface.isValid()) + ", appScale:") + appScale) + ", width=") + mWidth) + ", height=") + mHeight);
        }
        mAttachInfo.mTreeObserver.dispatchOnDraw();
        int xOffset = -mCanvasOffsetX;
        int yOffset = (-mCanvasOffsetY) + curScrollY;
        final android.view.WindowManager.LayoutParams params = mWindowAttributes;
        final android.graphics.Rect surfaceInsets = (params != null) ? params.surfaceInsets : null;
        if (surfaceInsets != null) {
            xOffset -= surfaceInsets.left;
            yOffset -= surfaceInsets.top;
            // Offset dirty rect for surface insets.
            dirty.offset(surfaceInsets.left, surfaceInsets.right);
        }
        boolean accessibilityFocusDirty = false;
        final android.graphics.drawable.Drawable drawable = mAttachInfo.mAccessibilityFocusDrawable;
        if (drawable != null) {
            final android.graphics.Rect bounds = mAttachInfo.mTmpInvalRect;
            final boolean hasFocus = getAccessibilityFocusedRect(bounds);
            if (!hasFocus) {
                bounds.setEmpty();
            }
            if (!bounds.equals(drawable.getBounds())) {
                accessibilityFocusDirty = true;
            }
        }
        mAttachInfo.mDrawingTime = mChoreographer.getFrameTimeNanos() / android.util.TimeUtils.NANOS_PER_MS;
        boolean useAsyncReport = false;
        if (((!dirty.isEmpty()) || mIsAnimating) || accessibilityFocusDirty) {
            if ((mAttachInfo.mThreadedRenderer != null) && mAttachInfo.mThreadedRenderer.isEnabled()) {
                // If accessibility focus moved, always invalidate the root.
                boolean invalidateRoot = accessibilityFocusDirty || mInvalidateRootRequested;
                mInvalidateRootRequested = false;
                // Draw with hardware renderer.
                mIsAnimating = false;
                if ((mHardwareYOffset != yOffset) || (mHardwareXOffset != xOffset)) {
                    mHardwareYOffset = yOffset;
                    mHardwareXOffset = xOffset;
                    invalidateRoot = true;
                }
                if (invalidateRoot) {
                    mAttachInfo.mThreadedRenderer.invalidateRoot();
                }
                dirty.setEmpty();
                // Stage the content drawn size now. It will be transferred to the renderer
                // shortly before the draw commands get send to the renderer.
                final boolean updated = updateContentDrawBounds();
                if (mReportNextDraw) {
                    // report next draw overrides setStopped()
                    // This value is re-sync'd to the value of mStopped
                    // in the handling of mReportNextDraw post-draw.
                    mAttachInfo.mThreadedRenderer.setStopped(false);
                }
                if (updated) {
                    requestDrawWindow();
                }
                useAsyncReport = true;
                mAttachInfo.mThreadedRenderer.draw(mView, mAttachInfo, this);
            } else {
                // If we get here with a disabled & requested hardware renderer, something went
                // wrong (an invalidate posted right before we destroyed the hardware surface
                // for instance) so we should just bail out. Locking the surface with software
                // rendering at this point would lock it forever and prevent hardware renderer
                // from doing its job when it comes back.
                // Before we request a new frame we must however attempt to reinitiliaze the
                // hardware renderer if it's in requested state. This would happen after an
                // eglTerminate() for instance.
                if ((((mAttachInfo.mThreadedRenderer != null) && (!mAttachInfo.mThreadedRenderer.isEnabled())) && mAttachInfo.mThreadedRenderer.isRequested()) && mSurface.isValid()) {
                    try {
                        mAttachInfo.mThreadedRenderer.initializeIfNeeded(mWidth, mHeight, mAttachInfo, mSurface, surfaceInsets);
                    } catch (android.view.Surface.OutOfResourcesException e) {
                        handleOutOfResourcesException(e);
                        return false;
                    }
                    mFullRedrawNeeded = true;
                    scheduleTraversals();
                    return false;
                }
                if (!drawSoftware(surface, mAttachInfo, xOffset, yOffset, scalingRequired, dirty, surfaceInsets)) {
                    return false;
                }
            }
        }
        if (animating) {
            mFullRedrawNeeded = true;
            scheduleTraversals();
        }
        return useAsyncReport;
    }

    /**
     *
     *
     * @return true if drawing was successful, false if an error occurred
     */
    private boolean drawSoftware(android.view.Surface surface, android.view.View.AttachInfo attachInfo, int xoff, int yoff, boolean scalingRequired, android.graphics.Rect dirty, android.graphics.Rect surfaceInsets) {
        // Draw with software renderer.
        final android.graphics.Canvas canvas;
        // We already have the offset of surfaceInsets in xoff, yoff and dirty region,
        // therefore we need to add it back when moving the dirty region.
        int dirtyXOffset = xoff;
        int dirtyYOffset = yoff;
        if (surfaceInsets != null) {
            dirtyXOffset += surfaceInsets.left;
            dirtyYOffset += surfaceInsets.top;
        }
        try {
            dirty.offset(-dirtyXOffset, -dirtyYOffset);
            final int left = dirty.left;
            final int top = dirty.top;
            final int right = dirty.right;
            final int bottom = dirty.bottom;
            canvas = mSurface.lockCanvas(dirty);
            // TODO: Do this in native
            canvas.setDensity(mDensity);
        } catch (android.view.Surface.OutOfResourcesException e) {
            handleOutOfResourcesException(e);
            return false;
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(mTag, "Could not lock surface", e);
            // Don't assume this is due to out of memory, it could be
            // something else, and if it is something else then we could
            // kill stuff (or ourself) for no reason.
            mLayoutRequested = true;// ask wm for a new surface next time.

            return false;
        } finally {
            dirty.offset(dirtyXOffset, dirtyYOffset);// Reset to the original value.

        }
        try {
            if (android.view.ViewRootImpl.DEBUG_ORIENTATION || android.view.ViewRootImpl.DEBUG_DRAW) {
                android.util.Log.v(mTag, (((("Surface " + surface) + " drawing to bitmap w=") + canvas.getWidth()) + ", h=") + canvas.getHeight());
                // canvas.drawARGB(255, 255, 0, 0);
            }
            // If this bitmap's format includes an alpha channel, we
            // need to clear it before drawing so that the child will
            // properly re-composite its drawing on a transparent
            // background. This automatically respects the clip/dirty region
            // or
            // If we are applying an offset, we need to clear the area
            // where the offset doesn't appear to avoid having garbage
            // left in the blank areas.
            if (((!canvas.isOpaque()) || (yoff != 0)) || (xoff != 0)) {
                canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            }
            dirty.setEmpty();
            mIsAnimating = false;
            mView.mPrivateFlags |= android.view.View.PFLAG_DRAWN;
            if (android.view.ViewRootImpl.DEBUG_DRAW) {
                android.content.Context cxt = mView.getContext();
                android.util.Log.i(mTag, (((("Drawing: package:" + cxt.getPackageName()) + ", metrics=") + cxt.getResources().getDisplayMetrics()) + ", compatibilityInfo=") + cxt.getResources().getCompatibilityInfo());
            }
            canvas.translate(-xoff, -yoff);
            if (mTranslator != null) {
                mTranslator.translateCanvas(canvas);
            }
            canvas.setScreenDensity(scalingRequired ? mNoncompatDensity : 0);
            mView.draw(canvas);
            drawAccessibilityFocusedDrawableIfNeeded(canvas);
        } finally {
            try {
                surface.unlockCanvasAndPost(canvas);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(mTag, "Could not unlock surface", e);
                mLayoutRequested = true;// ask wm for a new surface next time.

                // noinspection ReturnInsideFinallyBlock
                return false;
            }
            if (android.view.ViewRootImpl.LOCAL_LOGV) {
                android.util.Log.v(mTag, ("Surface " + surface) + " unlockCanvasAndPost");
            }
        }
        return true;
    }

    /**
     * We want to draw a highlight around the current accessibility focused.
     * Since adding a style for all possible view is not a viable option we
     * have this specialized drawing method.
     *
     * Note: We are doing this here to be able to draw the highlight for
     *       virtual views in addition to real ones.
     *
     * @param canvas
     * 		The canvas on which to draw.
     */
    private void drawAccessibilityFocusedDrawableIfNeeded(android.graphics.Canvas canvas) {
        final android.graphics.Rect bounds = mAttachInfo.mTmpInvalRect;
        if (getAccessibilityFocusedRect(bounds)) {
            final android.graphics.drawable.Drawable drawable = getAccessibilityFocusedDrawable();
            if (drawable != null) {
                drawable.setBounds(bounds);
                drawable.draw(canvas);
            }
        } else
            if (mAttachInfo.mAccessibilityFocusDrawable != null) {
                mAttachInfo.mAccessibilityFocusDrawable.setBounds(0, 0, 0, 0);
            }

    }

    private boolean getAccessibilityFocusedRect(android.graphics.Rect bounds) {
        final android.view.accessibility.AccessibilityManager manager = android.view.accessibility.AccessibilityManager.getInstance(mView.mContext);
        if ((!manager.isEnabled()) || (!manager.isTouchExplorationEnabled())) {
            return false;
        }
        final android.view.View host = mAccessibilityFocusedHost;
        if ((host == null) || (host.mAttachInfo == null)) {
            return false;
        }
        final android.view.accessibility.AccessibilityNodeProvider provider = host.getAccessibilityNodeProvider();
        if (provider == null) {
            host.getBoundsOnScreen(bounds, true);
        } else
            if (mAccessibilityFocusedVirtualView != null) {
                mAccessibilityFocusedVirtualView.getBoundsInScreen(bounds);
            } else {
                return false;
            }

        // Transform the rect into window-relative coordinates.
        final android.view.View.AttachInfo attachInfo = mAttachInfo;
        bounds.offset(0, attachInfo.mViewRootImpl.mScrollY);
        bounds.offset(-attachInfo.mWindowLeft, -attachInfo.mWindowTop);
        if (!bounds.intersect(0, 0, attachInfo.mViewRootImpl.mWidth, attachInfo.mViewRootImpl.mHeight)) {
            // If no intersection, set bounds to empty.
            bounds.setEmpty();
        }
        return !bounds.isEmpty();
    }

    private android.graphics.drawable.Drawable getAccessibilityFocusedDrawable() {
        // Lazily load the accessibility focus drawable.
        if (mAttachInfo.mAccessibilityFocusDrawable == null) {
            final android.util.TypedValue value = new android.util.TypedValue();
            final boolean resolved = mView.mContext.getTheme().resolveAttribute(R.attr.accessibilityFocusedDrawable, value, true);
            if (resolved) {
                mAttachInfo.mAccessibilityFocusDrawable = mView.mContext.getDrawable(value.resourceId);
            }
        }
        return mAttachInfo.mAccessibilityFocusDrawable;
    }

    void updateSystemGestureExclusionRectsForView(android.view.View view) {
        mGestureExclusionTracker.updateRectsForView(view);
        mHandler.sendEmptyMessage(android.view.ViewRootImpl.MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED);
    }

    void systemGestureExclusionChanged() {
        final java.util.List<android.graphics.Rect> rectsForWindowManager = mGestureExclusionTracker.computeChangedRects();
        if ((rectsForWindowManager != null) && (mView != null)) {
            try {
                mWindowSession.reportSystemGestureExclusionChanged(mWindow, rectsForWindowManager);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
            mAttachInfo.mTreeObserver.dispatchOnSystemGestureExclusionRectsChanged(rectsForWindowManager);
        }
    }

    /**
     * Set the root-level system gesture exclusion rects. These are added to those provided by
     * the root's view hierarchy.
     */
    public void setRootSystemGestureExclusionRects(@android.annotation.NonNull
    java.util.List<android.graphics.Rect> rects) {
        mGestureExclusionTracker.setRootSystemGestureExclusionRects(rects);
        mHandler.sendEmptyMessage(android.view.ViewRootImpl.MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED);
    }

    /**
     * Returns the root-level system gesture exclusion rects. These do not include those provided by
     * the root's view hierarchy.
     */
    @android.annotation.NonNull
    public java.util.List<android.graphics.Rect> getRootSystemGestureExclusionRects() {
        return mGestureExclusionTracker.getRootSystemGestureExclusionRects();
    }

    /**
     * Requests that the root render node is invalidated next time we perform a draw, such that
     * {@link WindowCallbacks#onPostDraw} gets called.
     */
    public void requestInvalidateRootRenderNode() {
        mInvalidateRootRequested = true;
    }

    boolean scrollToRectOrFocus(android.graphics.Rect rectangle, boolean immediate) {
        final android.graphics.Rect ci = mAttachInfo.mContentInsets;
        final android.graphics.Rect vi = mAttachInfo.mVisibleInsets;
        int scrollY = 0;
        boolean handled = false;
        if ((((vi.left > ci.left) || (vi.top > ci.top)) || (vi.right > ci.right)) || (vi.bottom > ci.bottom)) {
            // We'll assume that we aren't going to change the scroll
            // offset, since we want to avoid that unless it is actually
            // going to make the focus visible...  otherwise we scroll
            // all over the place.
            scrollY = mScrollY;
            // We can be called for two different situations: during a draw,
            // to update the scroll position if the focus has changed (in which
            // case 'rectangle' is null), or in response to a
            // requestChildRectangleOnScreen() call (in which case 'rectangle'
            // is non-null and we just want to scroll to whatever that
            // rectangle is).
            final android.view.View focus = mView.findFocus();
            if (focus == null) {
                return false;
            }
            android.view.View lastScrolledFocus = (mLastScrolledFocus != null) ? mLastScrolledFocus.get() : null;
            if (focus != lastScrolledFocus) {
                // If the focus has changed, then ignore any requests to scroll
                // to a rectangle; first we want to make sure the entire focus
                // view is visible.
                rectangle = null;
            }
            if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                android.util.Log.v(mTag, (((((("Eval scroll: focus=" + focus) + " rectangle=") + rectangle) + " ci=") + ci) + " vi=") + vi);

            if (((focus == lastScrolledFocus) && (!mScrollMayChange)) && (rectangle == null)) {
                // Optimization: if the focus hasn't changed since last
                // time, and no layout has happened, then just leave things
                // as they are.
                if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                    android.util.Log.v(mTag, (("Keeping scroll y=" + mScrollY) + " vi=") + vi.toShortString());

            } else {
                // We need to determine if the currently focused view is
                // within the visible part of the window and, if not, apply
                // a pan so it can be seen.
                mLastScrolledFocus = new java.lang.ref.WeakReference<android.view.View>(focus);
                mScrollMayChange = false;
                if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                    android.util.Log.v(mTag, "Need to scroll?");

                // Try to find the rectangle from the focus view.
                if (focus.getGlobalVisibleRect(mVisRect, null)) {
                    if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                        android.util.Log.v(mTag, (((((("Root w=" + mView.getWidth()) + " h=") + mView.getHeight()) + " ci=") + ci.toShortString()) + " vi=") + vi.toShortString());

                    if (rectangle == null) {
                        focus.getFocusedRect(mTempRect);
                        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                            android.util.Log.v(mTag, (("Focus " + focus) + ": focusRect=") + mTempRect.toShortString());

                        if (mView instanceof android.view.ViewGroup) {
                            ((android.view.ViewGroup) (mView)).offsetDescendantRectToMyCoords(focus, mTempRect);
                        }
                        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                            android.util.Log.v(mTag, (("Focus in window: focusRect=" + mTempRect.toShortString()) + " visRect=") + mVisRect.toShortString());

                    } else {
                        mTempRect.set(rectangle);
                        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                            android.util.Log.v(mTag, (("Request scroll to rect: " + mTempRect.toShortString()) + " visRect=") + mVisRect.toShortString());

                    }
                    if (mTempRect.intersect(mVisRect)) {
                        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                            android.util.Log.v(mTag, "Focus window visible rect: " + mTempRect.toShortString());

                        if (mTempRect.height() > ((mView.getHeight() - vi.top) - vi.bottom)) {
                            // If the focus simply is not going to fit, then
                            // best is probably just to leave things as-is.
                            if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                                android.util.Log.v(mTag, "Too tall; leaving scrollY=" + scrollY);

                        } else// Next, check whether top or bottom is covered based on the non-scrolled
                        // position, and calculate new scrollY (or set it to 0).
                        // We can't keep using mScrollY here. For example mScrollY is non-zero
                        // due to IME, then IME goes away. The current value of mScrollY leaves top
                        // and bottom both visible, but we still need to scroll it back to 0.

                            if (mTempRect.top < vi.top) {
                                scrollY = mTempRect.top - vi.top;
                                if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                                    android.util.Log.v(mTag, "Top covered; scrollY=" + scrollY);

                            } else
                                if (mTempRect.bottom > (mView.getHeight() - vi.bottom)) {
                                    scrollY = mTempRect.bottom - (mView.getHeight() - vi.bottom);
                                    if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                                        android.util.Log.v(mTag, "Bottom covered; scrollY=" + scrollY);

                                } else {
                                    scrollY = 0;
                                }


                        handled = true;
                    }
                }
            }
        }
        if (scrollY != mScrollY) {
            if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE)
                android.util.Log.v(mTag, (("Pan scroll changed: old=" + mScrollY) + " , new=") + scrollY);

            if (!immediate) {
                if (mScroller == null) {
                    mScroller = new android.widget.Scroller(mView.getContext());
                }
                mScroller.startScroll(0, mScrollY, 0, scrollY - mScrollY);
            } else
                if (mScroller != null) {
                    mScroller.abortAnimation();
                }

            mScrollY = scrollY;
        }
        return handled;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.view.View getAccessibilityFocusedHost() {
        return mAccessibilityFocusedHost;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.view.accessibility.AccessibilityNodeInfo getAccessibilityFocusedVirtualView() {
        return mAccessibilityFocusedVirtualView;
    }

    void setAccessibilityFocus(android.view.View view, android.view.accessibility.AccessibilityNodeInfo node) {
        // If we have a virtual view with accessibility focus we need
        // to clear the focus and invalidate the virtual view bounds.
        if (mAccessibilityFocusedVirtualView != null) {
            android.view.accessibility.AccessibilityNodeInfo focusNode = mAccessibilityFocusedVirtualView;
            android.view.View focusHost = mAccessibilityFocusedHost;
            // Wipe the state of the current accessibility focus since
            // the call into the provider to clear accessibility focus
            // will fire an accessibility event which will end up calling
            // this method and we want to have clean state when this
            // invocation happens.
            mAccessibilityFocusedHost = null;
            mAccessibilityFocusedVirtualView = null;
            // Clear accessibility focus on the host after clearing state since
            // this method may be reentrant.
            focusHost.clearAccessibilityFocusNoCallbacks(android.view.accessibility.AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
            android.view.accessibility.AccessibilityNodeProvider provider = focusHost.getAccessibilityNodeProvider();
            if (provider != null) {
                // Invalidate the area of the cleared accessibility focus.
                focusNode.getBoundsInParent(mTempRect);
                focusHost.invalidate(mTempRect);
                // Clear accessibility focus in the virtual node.
                final int virtualNodeId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(focusNode.getSourceNodeId());
                provider.performAction(virtualNodeId, android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS, null);
            }
            focusNode.recycle();
        }
        if ((mAccessibilityFocusedHost != null) && (mAccessibilityFocusedHost != view)) {
            // Clear accessibility focus in the view.
            mAccessibilityFocusedHost.clearAccessibilityFocusNoCallbacks(android.view.accessibility.AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
        }
        // Set the new focus host and node.
        mAccessibilityFocusedHost = view;
        mAccessibilityFocusedVirtualView = node;
        if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.invalidateRoot();
        }
    }

    boolean hasPointerCapture() {
        return mPointerCapture;
    }

    void requestPointerCapture(boolean enabled) {
        if (mPointerCapture == enabled) {
            return;
        }
        requestPointerCapture(mAttachInfo.mWindowToken, enabled);
    }

    private void handlePointerCaptureChanged(boolean hasCapture) {
        if (mPointerCapture == hasCapture) {
            return;
        }
        mPointerCapture = hasCapture;
        if (mView != null) {
            mView.dispatchPointerCaptureChanged(hasCapture);
        }
    }

    private boolean hasColorModeChanged(int colorMode) {
        if (mAttachInfo.mThreadedRenderer == null) {
            return false;
        }
        final boolean isWideGamut = colorMode == android.content.pm.ActivityInfo.COLOR_MODE_WIDE_COLOR_GAMUT;
        if (mAttachInfo.mThreadedRenderer.isWideGamut() == isWideGamut) {
            return false;
        }
        if (isWideGamut && (!mContext.getResources().getConfiguration().isScreenWideColorGamut())) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE) {
            android.util.Log.v(mTag, "Request child focus: focus now " + focused);
        }
        checkThread();
        scheduleTraversals();
    }

    @java.lang.Override
    public void clearChildFocus(android.view.View child) {
        if (android.view.ViewRootImpl.DEBUG_INPUT_RESIZE) {
            android.util.Log.v(mTag, "Clearing child focus");
        }
        checkThread();
        scheduleTraversals();
    }

    @java.lang.Override
    public android.view.ViewParent getParentForAccessibility() {
        return null;
    }

    @java.lang.Override
    public void focusableViewAvailable(android.view.View v) {
        checkThread();
        if (mView != null) {
            if (!mView.hasFocus()) {
                if (android.view.ViewRootImpl.sAlwaysAssignFocus || (!mAttachInfo.mInTouchMode)) {
                    v.requestFocus();
                }
            } else {
                // the one case where will transfer focus away from the current one
                // is if the current view is a view group that prefers to give focus
                // to its children first AND the view is a descendant of it.
                android.view.View focused = mView.findFocus();
                if (focused instanceof android.view.ViewGroup) {
                    android.view.ViewGroup group = ((android.view.ViewGroup) (focused));
                    if ((group.getDescendantFocusability() == android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS) && android.view.ViewRootImpl.isViewDescendantOf(v, focused)) {
                        v.requestFocus();
                    }
                }
            }
        }
    }

    @java.lang.Override
    public void recomputeViewAttributes(android.view.View child) {
        checkThread();
        if (mView == child) {
            mAttachInfo.mRecomputeGlobalAttributes = true;
            if (!mWillDrawSoon) {
                scheduleTraversals();
            }
        }
    }

    void dispatchDetachedFromWindow() {
        mFirstInputStage.onDetachedFromWindow();
        if ((mView != null) && (mView.mAttachInfo != null)) {
            mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(false);
            mView.dispatchDetachedFromWindow();
        }
        mAccessibilityInteractionConnectionManager.ensureNoConnection();
        mAccessibilityManager.removeAccessibilityStateChangeListener(mAccessibilityInteractionConnectionManager);
        mAccessibilityManager.removeHighTextContrastStateChangeListener(mHighContrastTextManager);
        removeSendWindowContentChangedCallback();
        destroyHardwareRenderer();
        setAccessibilityFocus(null, null);
        mView.assignParent(null);
        mView = null;
        mAttachInfo.mRootView = null;
        destroySurface();
        if ((mInputQueueCallback != null) && (mInputQueue != null)) {
            mInputQueueCallback.onInputQueueDestroyed(mInputQueue);
            mInputQueue.dispose();
            mInputQueueCallback = null;
            mInputQueue = null;
        }
        if (mInputEventReceiver != null) {
            mInputEventReceiver.dispose();
            mInputEventReceiver = null;
        }
        try {
            mWindowSession.remove(mWindow);
        } catch (android.os.RemoteException e) {
        }
        // Dispose the input channel after removing the window so the Window Manager
        // doesn't interpret the input channel being closed as an abnormal termination.
        if (mInputChannel != null) {
            mInputChannel.dispose();
            mInputChannel = null;
        }
        mDisplayManager.unregisterDisplayListener(mDisplayListener);
        unscheduleTraversals();
    }

    /**
     * Notifies all callbacks that configuration and/or display has changed and updates internal
     * state.
     *
     * @param mergedConfiguration
     * 		New global and override config in {@link MergedConfiguration}
     * 		container.
     * @param force
     * 		Flag indicating if we should force apply the config.
     * @param newDisplayId
     * 		Id of new display if moved, {@link Display#INVALID_DISPLAY} if not
     * 		changed.
     */
    private void performConfigurationChange(android.util.MergedConfiguration mergedConfiguration, boolean force, int newDisplayId) {
        if (mergedConfiguration == null) {
            throw new java.lang.IllegalArgumentException("No merged config provided.");
        }
        android.content.res.Configuration globalConfig = mergedConfiguration.getGlobalConfiguration();
        final android.content.res.Configuration overrideConfig = mergedConfiguration.getOverrideConfiguration();
        if (android.view.ViewRootImpl.DEBUG_CONFIGURATION)
            android.util.Log.v(mTag, (((("Applying new config to window " + mWindowAttributes.getTitle()) + ", globalConfig: ") + globalConfig) + ", overrideConfig: ") + overrideConfig);

        final android.content.res.CompatibilityInfo ci = mDisplay.getDisplayAdjustments().getCompatibilityInfo();
        if (!ci.equals(android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
            globalConfig = new android.content.res.Configuration(globalConfig);
            ci.applyToConfiguration(mNoncompatDensity, globalConfig);
        }
        synchronized(android.view.ViewRootImpl.sConfigCallbacks) {
            for (int i = android.view.ViewRootImpl.sConfigCallbacks.size() - 1; i >= 0; i--) {
                android.view.ViewRootImpl.sConfigCallbacks.get(i).onConfigurationChanged(globalConfig);
            }
        }
        mLastReportedMergedConfiguration.setConfiguration(globalConfig, overrideConfig);
        mForceNextConfigUpdate = force;
        if (mActivityConfigCallback != null) {
            // An activity callback is set - notify it about override configuration update.
            // This basically initiates a round trip to ActivityThread and back, which will ensure
            // that corresponding activity and resources are updated before updating inner state of
            // ViewRootImpl. Eventually it will call #updateConfiguration().
            mActivityConfigCallback.onConfigurationChanged(overrideConfig, newDisplayId);
        } else {
            // There is no activity callback - update the configuration right away.
            updateConfiguration(newDisplayId);
        }
        mForceNextConfigUpdate = false;
    }

    /**
     * Update display and views if last applied merged configuration changed.
     *
     * @param newDisplayId
     * 		Id of new display if moved, {@link Display#INVALID_DISPLAY} otherwise.
     */
    public void updateConfiguration(int newDisplayId) {
        if (mView == null) {
            return;
        }
        // At this point the resources have been updated to
        // have the most recent config, whatever that is.  Use
        // the one in them which may be newer.
        final android.content.res.Resources localResources = mView.getResources();
        final android.content.res.Configuration config = localResources.getConfiguration();
        // Handle move to display.
        if (newDisplayId != android.view.Display.INVALID_DISPLAY) {
            onMovedToDisplay(newDisplayId, config);
        }
        // Handle configuration change.
        if (mForceNextConfigUpdate || (mLastConfigurationFromResources.diff(config) != 0)) {
            // Update the display with new DisplayAdjustments.
            updateInternalDisplay(mDisplay.getDisplayId(), localResources);
            final int lastLayoutDirection = mLastConfigurationFromResources.getLayoutDirection();
            final int currentLayoutDirection = config.getLayoutDirection();
            mLastConfigurationFromResources.setTo(config);
            if ((lastLayoutDirection != currentLayoutDirection) && (mViewLayoutDirectionInitial == android.view.View.LAYOUT_DIRECTION_INHERIT)) {
                mView.setLayoutDirection(currentLayoutDirection);
            }
            mView.dispatchConfigurationChanged(config);
            // We could have gotten this {@link Configuration} update after we called
            // {@link #performTraversals} with an older {@link Configuration}. As a result, our
            // window frame may be stale. We must ensure the next pass of {@link #performTraversals}
            // catches this.
            mForceNextWindowRelayout = true;
            requestLayout();
        }
        updateForceDarkMode();
    }

    /**
     * Return true if child is an ancestor of parent, (or equal to the parent).
     */
    public static boolean isViewDescendantOf(android.view.View child, android.view.View parent) {
        if (child == parent) {
            return true;
        }
        final android.view.ViewParent theParent = child.getParent();
        return (theParent instanceof android.view.ViewGroup) && android.view.ViewRootImpl.isViewDescendantOf(((android.view.View) (theParent)), parent);
    }

    private static void forceLayout(android.view.View view) {
        view.forceLayout();
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = ((android.view.ViewGroup) (view));
            final int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.ViewRootImpl.forceLayout(group.getChildAt(i));
            }
        }
    }

    private static final int MSG_INVALIDATE = 1;

    private static final int MSG_INVALIDATE_RECT = 2;

    private static final int MSG_DIE = 3;

    private static final int MSG_RESIZED = 4;

    private static final int MSG_RESIZED_REPORT = 5;

    private static final int MSG_WINDOW_FOCUS_CHANGED = 6;

    private static final int MSG_DISPATCH_INPUT_EVENT = 7;

    private static final int MSG_DISPATCH_APP_VISIBILITY = 8;

    private static final int MSG_DISPATCH_GET_NEW_SURFACE = 9;

    private static final int MSG_DISPATCH_KEY_FROM_IME = 11;

    private static final int MSG_DISPATCH_KEY_FROM_AUTOFILL = 12;

    private static final int MSG_CHECK_FOCUS = 13;

    private static final int MSG_CLOSE_SYSTEM_DIALOGS = 14;

    private static final int MSG_DISPATCH_DRAG_EVENT = 15;

    private static final int MSG_DISPATCH_DRAG_LOCATION_EVENT = 16;

    private static final int MSG_DISPATCH_SYSTEM_UI_VISIBILITY = 17;

    private static final int MSG_UPDATE_CONFIGURATION = 18;

    private static final int MSG_PROCESS_INPUT_EVENTS = 19;

    private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST = 21;

    private static final int MSG_INVALIDATE_WORLD = 22;

    private static final int MSG_WINDOW_MOVED = 23;

    private static final int MSG_SYNTHESIZE_INPUT_EVENT = 24;

    private static final int MSG_DISPATCH_WINDOW_SHOWN = 25;

    private static final int MSG_REQUEST_KEYBOARD_SHORTCUTS = 26;

    private static final int MSG_UPDATE_POINTER_ICON = 27;

    private static final int MSG_POINTER_CAPTURE_CHANGED = 28;

    private static final int MSG_DRAW_FINISHED = 29;

    private static final int MSG_INSETS_CHANGED = 30;

    private static final int MSG_INSETS_CONTROL_CHANGED = 31;

    private static final int MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED = 32;

    final class ViewRootHandler extends android.os.Handler {
        @java.lang.Override
        public java.lang.String getMessageName(android.os.Message message) {
            switch (message.what) {
                case android.view.ViewRootImpl.MSG_INVALIDATE :
                    return "MSG_INVALIDATE";
                case android.view.ViewRootImpl.MSG_INVALIDATE_RECT :
                    return "MSG_INVALIDATE_RECT";
                case android.view.ViewRootImpl.MSG_DIE :
                    return "MSG_DIE";
                case android.view.ViewRootImpl.MSG_RESIZED :
                    return "MSG_RESIZED";
                case android.view.ViewRootImpl.MSG_RESIZED_REPORT :
                    return "MSG_RESIZED_REPORT";
                case android.view.ViewRootImpl.MSG_WINDOW_FOCUS_CHANGED :
                    return "MSG_WINDOW_FOCUS_CHANGED";
                case android.view.ViewRootImpl.MSG_DISPATCH_INPUT_EVENT :
                    return "MSG_DISPATCH_INPUT_EVENT";
                case android.view.ViewRootImpl.MSG_DISPATCH_APP_VISIBILITY :
                    return "MSG_DISPATCH_APP_VISIBILITY";
                case android.view.ViewRootImpl.MSG_DISPATCH_GET_NEW_SURFACE :
                    return "MSG_DISPATCH_GET_NEW_SURFACE";
                case android.view.ViewRootImpl.MSG_DISPATCH_KEY_FROM_IME :
                    return "MSG_DISPATCH_KEY_FROM_IME";
                case android.view.ViewRootImpl.MSG_DISPATCH_KEY_FROM_AUTOFILL :
                    return "MSG_DISPATCH_KEY_FROM_AUTOFILL";
                case android.view.ViewRootImpl.MSG_CHECK_FOCUS :
                    return "MSG_CHECK_FOCUS";
                case android.view.ViewRootImpl.MSG_CLOSE_SYSTEM_DIALOGS :
                    return "MSG_CLOSE_SYSTEM_DIALOGS";
                case android.view.ViewRootImpl.MSG_DISPATCH_DRAG_EVENT :
                    return "MSG_DISPATCH_DRAG_EVENT";
                case android.view.ViewRootImpl.MSG_DISPATCH_DRAG_LOCATION_EVENT :
                    return "MSG_DISPATCH_DRAG_LOCATION_EVENT";
                case android.view.ViewRootImpl.MSG_DISPATCH_SYSTEM_UI_VISIBILITY :
                    return "MSG_DISPATCH_SYSTEM_UI_VISIBILITY";
                case android.view.ViewRootImpl.MSG_UPDATE_CONFIGURATION :
                    return "MSG_UPDATE_CONFIGURATION";
                case android.view.ViewRootImpl.MSG_PROCESS_INPUT_EVENTS :
                    return "MSG_PROCESS_INPUT_EVENTS";
                case android.view.ViewRootImpl.MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST :
                    return "MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST";
                case android.view.ViewRootImpl.MSG_WINDOW_MOVED :
                    return "MSG_WINDOW_MOVED";
                case android.view.ViewRootImpl.MSG_SYNTHESIZE_INPUT_EVENT :
                    return "MSG_SYNTHESIZE_INPUT_EVENT";
                case android.view.ViewRootImpl.MSG_DISPATCH_WINDOW_SHOWN :
                    return "MSG_DISPATCH_WINDOW_SHOWN";
                case android.view.ViewRootImpl.MSG_UPDATE_POINTER_ICON :
                    return "MSG_UPDATE_POINTER_ICON";
                case android.view.ViewRootImpl.MSG_POINTER_CAPTURE_CHANGED :
                    return "MSG_POINTER_CAPTURE_CHANGED";
                case android.view.ViewRootImpl.MSG_DRAW_FINISHED :
                    return "MSG_DRAW_FINISHED";
                case android.view.ViewRootImpl.MSG_INSETS_CHANGED :
                    return "MSG_INSETS_CHANGED";
                case android.view.ViewRootImpl.MSG_INSETS_CONTROL_CHANGED :
                    return "MSG_INSETS_CONTROL_CHANGED";
                case android.view.ViewRootImpl.MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED :
                    return "MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED";
            }
            return super.getMessageName(message);
        }

        @java.lang.Override
        public boolean sendMessageAtTime(android.os.Message msg, long uptimeMillis) {
            if ((msg.what == android.view.ViewRootImpl.MSG_REQUEST_KEYBOARD_SHORTCUTS) && (msg.obj == null)) {
                // Debugging for b/27963013
                throw new java.lang.NullPointerException("Attempted to call MSG_REQUEST_KEYBOARD_SHORTCUTS with null receiver:");
            }
            return super.sendMessageAtTime(msg, uptimeMillis);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.view.ViewRootImpl.MSG_INVALIDATE :
                    ((android.view.View) (msg.obj)).invalidate();
                    break;
                case android.view.ViewRootImpl.MSG_INVALIDATE_RECT :
                    final android.view.View.AttachInfo.InvalidateInfo info = ((android.view.View.AttachInfo.InvalidateInfo) (msg.obj));
                    info.target.invalidate(info.left, info.top, info.right, info.bottom);
                    info.recycle();
                    break;
                case android.view.ViewRootImpl.MSG_PROCESS_INPUT_EVENTS :
                    mProcessInputEventsScheduled = false;
                    doProcessInputEvents();
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_APP_VISIBILITY :
                    handleAppVisibility(msg.arg1 != 0);
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_GET_NEW_SURFACE :
                    handleGetNewSurface();
                    break;
                case android.view.ViewRootImpl.MSG_RESIZED :
                    {
                        // Recycled in the fall through...
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        if ((((((((((mWinFrame.equals(args.arg1) && mPendingOverscanInsets.equals(args.arg5)) && mPendingContentInsets.equals(args.arg2)) && mPendingStableInsets.equals(args.arg6)) && mPendingDisplayCutout.get().equals(args.arg9)) && mPendingVisibleInsets.equals(args.arg3)) && mPendingOutsets.equals(args.arg7)) && mPendingBackDropFrame.equals(args.arg8)) && (args.arg4 == null)) && (args.argi1 == 0)) && (mDisplay.getDisplayId() == args.argi3)) {
                            break;
                        }
                    }// fall through...

                case android.view.ViewRootImpl.MSG_RESIZED_REPORT :
                    if (mAdded) {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        final int displayId = args.argi3;
                        android.util.MergedConfiguration mergedConfiguration = ((android.util.MergedConfiguration) (args.arg4));
                        final boolean displayChanged = mDisplay.getDisplayId() != displayId;
                        boolean configChanged = false;
                        if (!mLastReportedMergedConfiguration.equals(mergedConfiguration)) {
                            // If configuration changed - notify about that and, maybe,
                            // about move to display.
                            /* force */
                            /* same display */
                            performConfigurationChange(mergedConfiguration, false, displayChanged ? displayId : android.view.Display.INVALID_DISPLAY);
                            configChanged = true;
                        } else
                            if (displayChanged) {
                                // Moved to display without config change - report last applied one.
                                onMovedToDisplay(displayId, mLastConfigurationFromResources);
                            }

                        final boolean framesChanged = ((((((!mWinFrame.equals(args.arg1)) || (!mPendingOverscanInsets.equals(args.arg5))) || (!mPendingContentInsets.equals(args.arg2))) || (!mPendingStableInsets.equals(args.arg6))) || (!mPendingDisplayCutout.get().equals(args.arg9))) || (!mPendingVisibleInsets.equals(args.arg3))) || (!mPendingOutsets.equals(args.arg7));
                        setFrame(((android.graphics.Rect) (args.arg1)));
                        mPendingOverscanInsets.set(((android.graphics.Rect) (args.arg5)));
                        mPendingContentInsets.set(((android.graphics.Rect) (args.arg2)));
                        mPendingStableInsets.set(((android.graphics.Rect) (args.arg6)));
                        mPendingDisplayCutout.set(((android.view.DisplayCutout) (args.arg9)));
                        mPendingVisibleInsets.set(((android.graphics.Rect) (args.arg3)));
                        mPendingOutsets.set(((android.graphics.Rect) (args.arg7)));
                        mPendingBackDropFrame.set(((android.graphics.Rect) (args.arg8)));
                        mForceNextWindowRelayout = args.argi1 != 0;
                        mPendingAlwaysConsumeSystemBars = args.argi2 != 0;
                        args.recycle();
                        if (msg.what == android.view.ViewRootImpl.MSG_RESIZED_REPORT) {
                            reportNextDraw();
                        }
                        if ((mView != null) && (framesChanged || configChanged)) {
                            android.view.ViewRootImpl.forceLayout(mView);
                        }
                        requestLayout();
                    }
                    break;
                case android.view.ViewRootImpl.MSG_INSETS_CHANGED :
                    mInsetsController.onStateChanged(((android.view.InsetsState) (msg.obj)));
                    break;
                case android.view.ViewRootImpl.MSG_INSETS_CONTROL_CHANGED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        mInsetsController.onControlsChanged(((android.view.InsetsSourceControl[]) (args.arg2)));
                        mInsetsController.onStateChanged(((android.view.InsetsState) (args.arg1)));
                        break;
                    }
                case android.view.ViewRootImpl.MSG_WINDOW_MOVED :
                    if (mAdded) {
                        final int w = mWinFrame.width();
                        final int h = mWinFrame.height();
                        final int l = msg.arg1;
                        final int t = msg.arg2;
                        mTmpFrame.left = l;
                        mTmpFrame.right = l + w;
                        mTmpFrame.top = t;
                        mTmpFrame.bottom = t + h;
                        setFrame(mTmpFrame);
                        mPendingBackDropFrame.set(mWinFrame);
                        maybeHandleWindowMove(mWinFrame);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_WINDOW_FOCUS_CHANGED :
                    {
                        handleWindowFocusChanged();
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DIE :
                    doDie();
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_INPUT_EVENT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        android.view.InputEvent event = ((android.view.InputEvent) (args.arg1));
                        android.view.InputEventReceiver receiver = ((android.view.InputEventReceiver) (args.arg2));
                        enqueueInputEvent(event, receiver, 0, true);
                        args.recycle();
                    }
                    break;
                case android.view.ViewRootImpl.MSG_SYNTHESIZE_INPUT_EVENT :
                    {
                        android.view.InputEvent event = ((android.view.InputEvent) (msg.obj));
                        enqueueInputEvent(event, null, android.view.ViewRootImpl.QueuedInputEvent.FLAG_UNHANDLED, true);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_KEY_FROM_IME :
                    {
                        if (android.view.ViewRootImpl.LOCAL_LOGV) {
                            android.util.Log.v(android.view.ViewRootImpl.TAG, (("Dispatching key " + msg.obj) + " from IME to ") + mView);
                        }
                        android.view.KeyEvent event = ((android.view.KeyEvent) (msg.obj));
                        if ((event.getFlags() & android.view.KeyEvent.FLAG_FROM_SYSTEM) != 0) {
                            // The IME is trying to say this event is from the
                            // system!  Bad bad bad!
                            // noinspection UnusedAssignment
                            event = android.view.KeyEvent.changeFlags(event, event.getFlags() & (~android.view.KeyEvent.FLAG_FROM_SYSTEM));
                        }
                        enqueueInputEvent(event, null, android.view.ViewRootImpl.QueuedInputEvent.FLAG_DELIVER_POST_IME, true);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_KEY_FROM_AUTOFILL :
                    {
                        if (android.view.ViewRootImpl.LOCAL_LOGV) {
                            android.util.Log.v(android.view.ViewRootImpl.TAG, (("Dispatching key " + msg.obj) + " from Autofill to ") + mView);
                        }
                        android.view.KeyEvent event = ((android.view.KeyEvent) (msg.obj));
                        enqueueInputEvent(event, null, 0, true);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_CHECK_FOCUS :
                    {
                        android.view.inputmethod.InputMethodManager imm = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
                        if (imm != null) {
                            imm.checkFocus();
                        }
                    }
                    break;
                case android.view.ViewRootImpl.MSG_CLOSE_SYSTEM_DIALOGS :
                    {
                        if (mView != null) {
                            mView.onCloseSystemDialogs(((java.lang.String) (msg.obj)));
                        }
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_DRAG_EVENT :
                    {
                    }// fall through

                case android.view.ViewRootImpl.MSG_DISPATCH_DRAG_LOCATION_EVENT :
                    {
                        android.view.DragEvent event = ((android.view.DragEvent) (msg.obj));
                        // only present when this app called startDrag()
                        event.mLocalState = mLocalDragState;
                        handleDragEvent(event);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_SYSTEM_UI_VISIBILITY :
                    {
                        handleDispatchSystemUiVisibilityChanged(((android.view.ViewRootImpl.SystemUiVisibilityInfo) (msg.obj)));
                    }
                    break;
                case android.view.ViewRootImpl.MSG_UPDATE_CONFIGURATION :
                    {
                        android.content.res.Configuration config = ((android.content.res.Configuration) (msg.obj));
                        if (config.isOtherSeqNewer(mLastReportedMergedConfiguration.getMergedConfiguration())) {
                            // If we already have a newer merged config applied - use its global part.
                            config = mLastReportedMergedConfiguration.getGlobalConfiguration();
                        }
                        // Use the newer global config and last reported override config.
                        mPendingMergedConfiguration.setConfiguration(config, mLastReportedMergedConfiguration.getOverrideConfiguration());
                        /* force */
                        /* same display */
                        performConfigurationChange(mPendingMergedConfiguration, false, android.view.Display.INVALID_DISPLAY);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST :
                    {
                        setAccessibilityFocus(null, null);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_INVALIDATE_WORLD :
                    {
                        if (mView != null) {
                            invalidateWorld(mView);
                        }
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DISPATCH_WINDOW_SHOWN :
                    {
                        handleDispatchWindowShown();
                    }
                    break;
                case android.view.ViewRootImpl.MSG_REQUEST_KEYBOARD_SHORTCUTS :
                    {
                        final com.android.internal.os.IResultReceiver receiver = ((com.android.internal.os.IResultReceiver) (msg.obj));
                        final int deviceId = msg.arg1;
                        handleRequestKeyboardShortcuts(receiver, deviceId);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_UPDATE_POINTER_ICON :
                    {
                        android.view.MotionEvent event = ((android.view.MotionEvent) (msg.obj));
                        resetPointerIcon(event);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_POINTER_CAPTURE_CHANGED :
                    {
                        final boolean hasCapture = msg.arg1 != 0;
                        handlePointerCaptureChanged(hasCapture);
                    }
                    break;
                case android.view.ViewRootImpl.MSG_DRAW_FINISHED :
                    {
                        pendingDrawFinished();
                    }
                    break;
                case android.view.ViewRootImpl.MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED :
                    {
                        systemGestureExclusionChanged();
                    }
                    break;
            }
        }
    }

    final android.view.ViewRootImpl.ViewRootHandler mHandler = new android.view.ViewRootImpl.ViewRootHandler();

    /**
     * Something in the current window tells us we need to change the touch mode.  For
     * example, we are not in touch mode, and the user touches the screen.
     *
     * If the touch mode has changed, tell the window manager, and handle it locally.
     *
     * @param inTouchMode
     * 		Whether we want to be in touch mode.
     * @return True if the touch mode changed and focus changed was changed as a result
     */
    @android.annotation.UnsupportedAppUsage
    boolean ensureTouchMode(boolean inTouchMode) {
        if (android.view.ViewRootImpl.DBG)
            android.util.Log.d("touchmode", ((("ensureTouchMode(" + inTouchMode) + "), current ") + "touch mode is ") + mAttachInfo.mInTouchMode);

        if (mAttachInfo.mInTouchMode == inTouchMode)
            return false;

        // tell the window manager
        try {
            mWindowSession.setInTouchMode(inTouchMode);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
        // handle the change
        return ensureTouchModeLocally(inTouchMode);
    }

    /**
     * Ensure that the touch mode for this window is set, and if it is changing,
     * take the appropriate action.
     *
     * @param inTouchMode
     * 		Whether we want to be in touch mode.
     * @return True if the touch mode changed and focus changed was changed as a result
     */
    private boolean ensureTouchModeLocally(boolean inTouchMode) {
        if (android.view.ViewRootImpl.DBG)
            android.util.Log.d("touchmode", ((("ensureTouchModeLocally(" + inTouchMode) + "), current ") + "touch mode is ") + mAttachInfo.mInTouchMode);

        if (mAttachInfo.mInTouchMode == inTouchMode)
            return false;

        mAttachInfo.mInTouchMode = inTouchMode;
        mAttachInfo.mTreeObserver.dispatchOnTouchModeChanged(inTouchMode);
        return inTouchMode ? enterTouchMode() : leaveTouchMode();
    }

    private boolean enterTouchMode() {
        if ((mView != null) && mView.hasFocus()) {
            // note: not relying on mFocusedView here because this could
            // be when the window is first being added, and mFocused isn't
            // set yet.
            final android.view.View focused = mView.findFocus();
            if ((focused != null) && (!focused.isFocusableInTouchMode())) {
                final android.view.ViewGroup ancestorToTakeFocus = android.view.ViewRootImpl.findAncestorToTakeFocusInTouchMode(focused);
                if (ancestorToTakeFocus != null) {
                    // there is an ancestor that wants focus after its
                    // descendants that is focusable in touch mode.. give it
                    // focus
                    return ancestorToTakeFocus.requestFocus();
                } else {
                    // There's nothing to focus. Clear and propagate through the
                    // hierarchy, but don't attempt to place new focus.
                    focused.clearFocusInternal(null, true, false);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Find an ancestor of focused that wants focus after its descendants and is
     * focusable in touch mode.
     *
     * @param focused
     * 		The currently focused view.
     * @return An appropriate view, or null if no such view exists.
     */
    private static android.view.ViewGroup findAncestorToTakeFocusInTouchMode(android.view.View focused) {
        android.view.ViewParent parent = focused.getParent();
        while (parent instanceof android.view.ViewGroup) {
            final android.view.ViewGroup vgParent = ((android.view.ViewGroup) (parent));
            if ((vgParent.getDescendantFocusability() == android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS) && vgParent.isFocusableInTouchMode()) {
                return vgParent;
            }
            if (vgParent.isRootNamespace()) {
                return null;
            } else {
                parent = vgParent.getParent();
            }
        } 
        return null;
    }

    private boolean leaveTouchMode() {
        if (mView != null) {
            if (mView.hasFocus()) {
                android.view.View focusedView = mView.findFocus();
                if (!(focusedView instanceof android.view.ViewGroup)) {
                    // some view has focus, let it keep it
                    return false;
                } else
                    if (((android.view.ViewGroup) (focusedView)).getDescendantFocusability() != android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS) {
                        // some view group has focus, and doesn't prefer its children
                        // over itself for focus, so let them keep it.
                        return false;
                    }

            }
            // find the best view to give focus to in this brave new non-touch-mode
            // world
            return mView.restoreDefaultFocus();
        }
        return false;
    }

    /**
     * Base class for implementing a stage in the chain of responsibility
     * for processing input events.
     * <p>
     * Events are delivered to the stage by the {@link #deliver} method.  The stage
     * then has the choice of finishing the event or forwarding it to the next stage.
     * </p>
     */
    abstract class InputStage {
        private final android.view.ViewRootImpl.InputStage mNext;

        protected static final int FORWARD = 0;

        protected static final int FINISH_HANDLED = 1;

        protected static final int FINISH_NOT_HANDLED = 2;

        /**
         * Creates an input stage.
         *
         * @param next
         * 		The next stage to which events should be forwarded.
         */
        public InputStage(android.view.ViewRootImpl.InputStage next) {
            mNext = next;
        }

        /**
         * Delivers an event to be processed.
         */
        public final void deliver(android.view.ViewRootImpl.QueuedInputEvent q) {
            if ((q.mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_FINISHED) != 0) {
                forward(q);
            } else
                if (shouldDropInputEvent(q)) {
                    finish(q, false);
                } else {
                    apply(q, onProcess(q));
                }

        }

        /**
         * Marks the the input event as finished then forwards it to the next stage.
         */
        protected void finish(android.view.ViewRootImpl.QueuedInputEvent q, boolean handled) {
            q.mFlags |= android.view.ViewRootImpl.QueuedInputEvent.FLAG_FINISHED;
            if (handled) {
                q.mFlags |= android.view.ViewRootImpl.QueuedInputEvent.FLAG_FINISHED_HANDLED;
            }
            forward(q);
        }

        /**
         * Forwards the event to the next stage.
         */
        protected void forward(android.view.ViewRootImpl.QueuedInputEvent q) {
            onDeliverToNext(q);
        }

        /**
         * Applies a result code from {@link #onProcess} to the specified event.
         */
        protected void apply(android.view.ViewRootImpl.QueuedInputEvent q, int result) {
            if (result == android.view.ViewRootImpl.InputStage.FORWARD) {
                forward(q);
            } else
                if (result == android.view.ViewRootImpl.InputStage.FINISH_HANDLED) {
                    finish(q, true);
                } else
                    if (result == android.view.ViewRootImpl.InputStage.FINISH_NOT_HANDLED) {
                        finish(q, false);
                    } else {
                        throw new java.lang.IllegalArgumentException("Invalid result: " + result);
                    }


        }

        /**
         * Called when an event is ready to be processed.
         *
         * @return A result code indicating how the event was handled.
         */
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        /**
         * Called when an event is being delivered to the next stage.
         */
        protected void onDeliverToNext(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (android.view.ViewRootImpl.DEBUG_INPUT_STAGES) {
                android.util.Log.v(mTag, (("Done with " + getClass().getSimpleName()) + ". ") + q);
            }
            if (mNext != null) {
                mNext.deliver(q);
            } else {
                finishInputEvent(q);
            }
        }

        protected void onWindowFocusChanged(boolean hasWindowFocus) {
            if (mNext != null) {
                mNext.onWindowFocusChanged(hasWindowFocus);
            }
        }

        protected void onDetachedFromWindow() {
            if (mNext != null) {
                mNext.onDetachedFromWindow();
            }
        }

        protected boolean shouldDropInputEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            if ((mView == null) || (!mAdded)) {
                android.util.Slog.w(mTag, "Dropping event due to root view being removed: " + q.mEvent);
                return true;
            } else
                if ((((((!mAttachInfo.mHasWindowFocus) && (!q.mEvent.isFromSource(android.view.InputDevice.SOURCE_CLASS_POINTER))) && (!isAutofillUiShowing())) || mStopped) || (mIsAmbientMode && (!q.mEvent.isFromSource(android.view.InputDevice.SOURCE_CLASS_BUTTON)))) || (mPausedForTransition && (!isBack(q.mEvent)))) {
                    // This is a focus event and the window doesn't currently have input focus or
                    // has stopped. This could be an event that came back from the previous stage
                    // but the window has lost focus or stopped in the meantime.
                    if (android.view.ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                        // Don't drop terminal input events, however mark them as canceled.
                        q.mEvent.cancel();
                        android.util.Slog.w(mTag, "Cancelling event due to no window focus: " + q.mEvent);
                        return false;
                    }
                    // Drop non-terminal input events.
                    android.util.Slog.w(mTag, "Dropping event due to no window focus: " + q.mEvent);
                    return true;
                }

            return false;
        }

        void dump(java.lang.String prefix, java.io.PrintWriter writer) {
            if (mNext != null) {
                mNext.dump(prefix, writer);
            }
        }

        private boolean isBack(android.view.InputEvent event) {
            if (event instanceof android.view.KeyEvent) {
                return ((android.view.KeyEvent) (event)).getKeyCode() == android.view.KeyEvent.KEYCODE_BACK;
            } else {
                return false;
            }
        }
    }

    /**
     * Base class for implementing an input pipeline stage that supports
     * asynchronous and out-of-order processing of input events.
     * <p>
     * In addition to what a normal input stage can do, an asynchronous
     * input stage may also defer an input event that has been delivered to it
     * and finish or forward it later.
     * </p>
     */
    abstract class AsyncInputStage extends android.view.ViewRootImpl.InputStage {
        private final java.lang.String mTraceCounter;

        private android.view.ViewRootImpl.QueuedInputEvent mQueueHead;

        private android.view.ViewRootImpl.QueuedInputEvent mQueueTail;

        private int mQueueLength;

        protected static final int DEFER = 3;

        /**
         * Creates an asynchronous input stage.
         *
         * @param next
         * 		The next stage to which events should be forwarded.
         * @param traceCounter
         * 		The name of a counter to record the size of
         * 		the queue of pending events.
         */
        public AsyncInputStage(android.view.ViewRootImpl.InputStage next, java.lang.String traceCounter) {
            super(next);
            mTraceCounter = traceCounter;
        }

        /**
         * Marks the event as deferred, which is to say that it will be handled
         * asynchronously.  The caller is responsible for calling {@link #forward}
         * or {@link #finish} later when it is done handling the event.
         */
        protected void defer(android.view.ViewRootImpl.QueuedInputEvent q) {
            q.mFlags |= android.view.ViewRootImpl.QueuedInputEvent.FLAG_DEFERRED;
            enqueue(q);
        }

        @java.lang.Override
        protected void forward(android.view.ViewRootImpl.QueuedInputEvent q) {
            // Clear the deferred flag.
            q.mFlags &= ~android.view.ViewRootImpl.QueuedInputEvent.FLAG_DEFERRED;
            // Fast path if the queue is empty.
            android.view.ViewRootImpl.QueuedInputEvent curr = mQueueHead;
            if (curr == null) {
                super.forward(q);
                return;
            }
            // Determine whether the event must be serialized behind any others
            // before it can be delivered to the next stage.  This is done because
            // deferred events might be handled out of order by the stage.
            final int deviceId = q.mEvent.getDeviceId();
            android.view.ViewRootImpl.QueuedInputEvent prev = null;
            boolean blocked = false;
            while ((curr != null) && (curr != q)) {
                if ((!blocked) && (deviceId == curr.mEvent.getDeviceId())) {
                    blocked = true;
                }
                prev = curr;
                curr = curr.mNext;
            } 
            // If the event is blocked, then leave it in the queue to be delivered later.
            // Note that the event might not yet be in the queue if it was not previously
            // deferred so we will enqueue it if needed.
            if (blocked) {
                if (curr == null) {
                    enqueue(q);
                }
                return;
            }
            // The event is not blocked.  Deliver it immediately.
            if (curr != null) {
                curr = curr.mNext;
                dequeue(q, prev);
            }
            super.forward(q);
            // Dequeuing this event may have unblocked successors.  Deliver them.
            while (curr != null) {
                if (deviceId == curr.mEvent.getDeviceId()) {
                    if ((curr.mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_DEFERRED) != 0) {
                        break;
                    }
                    android.view.ViewRootImpl.QueuedInputEvent next = curr.mNext;
                    dequeue(curr, prev);
                    super.forward(curr);
                    curr = next;
                } else {
                    prev = curr;
                    curr = curr.mNext;
                }
            } 
        }

        @java.lang.Override
        protected void apply(android.view.ViewRootImpl.QueuedInputEvent q, int result) {
            if (result == android.view.ViewRootImpl.AsyncInputStage.DEFER) {
                defer(q);
            } else {
                super.apply(q, result);
            }
        }

        private void enqueue(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (mQueueTail == null) {
                mQueueHead = q;
                mQueueTail = q;
            } else {
                mQueueTail.mNext = q;
                mQueueTail = q;
            }
            mQueueLength += 1;
            android.os.Trace.traceCounter(Trace.TRACE_TAG_INPUT, mTraceCounter, mQueueLength);
        }

        private void dequeue(android.view.ViewRootImpl.QueuedInputEvent q, android.view.ViewRootImpl.QueuedInputEvent prev) {
            if (prev == null) {
                mQueueHead = q.mNext;
            } else {
                prev.mNext = q.mNext;
            }
            if (mQueueTail == q) {
                mQueueTail = prev;
            }
            q.mNext = null;
            mQueueLength -= 1;
            android.os.Trace.traceCounter(Trace.TRACE_TAG_INPUT, mTraceCounter, mQueueLength);
        }

        @java.lang.Override
        void dump(java.lang.String prefix, java.io.PrintWriter writer) {
            writer.print(prefix);
            writer.print(getClass().getName());
            writer.print(": mQueueLength=");
            writer.println(mQueueLength);
            super.dump(prefix, writer);
        }
    }

    /**
     * Delivers pre-ime input events to a native activity.
     * Does not support pointer events.
     */
    final class NativePreImeInputStage extends android.view.ViewRootImpl.AsyncInputStage implements android.view.InputQueue.FinishedInputEventCallback {
        public NativePreImeInputStage(android.view.ViewRootImpl.InputStage next, java.lang.String traceCounter) {
            super(next, traceCounter);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            if ((mInputQueue != null) && (q.mEvent instanceof android.view.KeyEvent)) {
                mInputQueue.sendInputEvent(q.mEvent, q, true, this);
                return android.view.ViewRootImpl.AsyncInputStage.DEFER;
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        @java.lang.Override
        public void onFinishedInputEvent(java.lang.Object token, boolean handled) {
            android.view.ViewRootImpl.QueuedInputEvent q = ((android.view.ViewRootImpl.QueuedInputEvent) (token));
            if (handled) {
                finish(q, true);
                return;
            }
            forward(q);
        }
    }

    /**
     * Delivers pre-ime input events to the view hierarchy.
     * Does not support pointer events.
     */
    final class ViewPreImeInputStage extends android.view.ViewRootImpl.InputStage {
        public ViewPreImeInputStage(android.view.ViewRootImpl.InputStage next) {
            super(next);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (q.mEvent instanceof android.view.KeyEvent) {
                return processKeyEvent(q);
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private int processKeyEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.KeyEvent event = ((android.view.KeyEvent) (q.mEvent));
            if (mView.dispatchKeyEventPreIme(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }
    }

    /**
     * Delivers input events to the ime.
     * Does not support pointer events.
     */
    final class ImeInputStage extends android.view.ViewRootImpl.AsyncInputStage implements android.view.inputmethod.InputMethodManager.FinishedInputEventCallback {
        public ImeInputStage(android.view.ViewRootImpl.InputStage next, java.lang.String traceCounter) {
            super(next, traceCounter);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (mLastWasImTarget && (!isInLocalFocusMode())) {
                android.view.inputmethod.InputMethodManager imm = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
                if (imm != null) {
                    final android.view.InputEvent event = q.mEvent;
                    if (android.view.ViewRootImpl.DEBUG_IMF)
                        android.util.Log.v(mTag, "Sending input event to IME: " + event);

                    int result = imm.dispatchInputEvent(event, q, this, mHandler);
                    if (result == android.view.inputmethod.InputMethodManager.DISPATCH_HANDLED) {
                        return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                    } else
                        if (result == android.view.inputmethod.InputMethodManager.DISPATCH_NOT_HANDLED) {
                            // The IME could not handle it, so skip along to the next InputStage
                            return android.view.ViewRootImpl.InputStage.FORWARD;
                        } else {
                            return android.view.ViewRootImpl.AsyncInputStage.DEFER;// callback will be invoked later

                        }

                }
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        @java.lang.Override
        public void onFinishedInputEvent(java.lang.Object token, boolean handled) {
            android.view.ViewRootImpl.QueuedInputEvent q = ((android.view.ViewRootImpl.QueuedInputEvent) (token));
            if (handled) {
                finish(q, true);
                return;
            }
            forward(q);
        }
    }

    /**
     * Performs early processing of post-ime input events.
     */
    final class EarlyPostImeInputStage extends android.view.ViewRootImpl.InputStage {
        public EarlyPostImeInputStage(android.view.ViewRootImpl.InputStage next) {
            super(next);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (q.mEvent instanceof android.view.KeyEvent) {
                return processKeyEvent(q);
            } else
                if (q.mEvent instanceof android.view.MotionEvent) {
                    return processMotionEvent(q);
                }

            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private int processKeyEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.KeyEvent event = ((android.view.KeyEvent) (q.mEvent));
            if (mAttachInfo.mTooltipHost != null) {
                mAttachInfo.mTooltipHost.handleTooltipKey(event);
            }
            // If the key's purpose is to exit touch mode then we consume it
            // and consider it handled.
            if (checkForLeavingTouchModeAndConsume(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            // Make sure the fallback event policy sees all keys that will be
            // delivered to the view hierarchy.
            mFallbackEventHandler.preDispatchKeyEvent(event);
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private int processMotionEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
            if (event.isFromSource(android.view.InputDevice.SOURCE_CLASS_POINTER)) {
                return processPointerEvent(q);
            }
            // If the motion event is from an absolute position device, exit touch mode
            final int action = event.getActionMasked();
            if ((action == android.view.MotionEvent.ACTION_DOWN) || (action == android.view.MotionEvent.ACTION_SCROLL)) {
                if (event.isFromSource(android.view.InputDevice.SOURCE_CLASS_POSITION)) {
                    ensureTouchMode(false);
                }
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private int processPointerEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
            // Translate the pointer event for compatibility, if needed.
            if (mTranslator != null) {
                mTranslator.translateEventInScreenToAppWindow(event);
            }
            // Enter touch mode on down or scroll, if it is coming from a touch screen device,
            // exit otherwise.
            final int action = event.getAction();
            if ((action == android.view.MotionEvent.ACTION_DOWN) || (action == android.view.MotionEvent.ACTION_SCROLL)) {
                ensureTouchMode(event.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN));
            }
            if (action == android.view.MotionEvent.ACTION_DOWN) {
                // Upon motion event within app window, close autofill ui.
                android.view.autofill.AutofillManager afm = getAutofillManager();
                if (afm != null) {
                    afm.requestHideFillUi();
                }
            }
            if ((action == android.view.MotionEvent.ACTION_DOWN) && (mAttachInfo.mTooltipHost != null)) {
                mAttachInfo.mTooltipHost.hideTooltip();
            }
            // Offset the scroll position.
            if (mCurScrollY != 0) {
                event.offsetLocation(0, mCurScrollY);
            }
            // Remember the touch position for possible drag-initiation.
            if (event.isTouchEvent()) {
                mLastTouchPoint.x = event.getRawX();
                mLastTouchPoint.y = event.getRawY();
                mLastTouchSource = event.getSource();
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }
    }

    /**
     * Delivers post-ime input events to a native activity.
     */
    final class NativePostImeInputStage extends android.view.ViewRootImpl.AsyncInputStage implements android.view.InputQueue.FinishedInputEventCallback {
        public NativePostImeInputStage(android.view.ViewRootImpl.InputStage next, java.lang.String traceCounter) {
            super(next, traceCounter);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (mInputQueue != null) {
                mInputQueue.sendInputEvent(q.mEvent, q, false, this);
                return android.view.ViewRootImpl.AsyncInputStage.DEFER;
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        @java.lang.Override
        public void onFinishedInputEvent(java.lang.Object token, boolean handled) {
            android.view.ViewRootImpl.QueuedInputEvent q = ((android.view.ViewRootImpl.QueuedInputEvent) (token));
            if (handled) {
                finish(q, true);
                return;
            }
            forward(q);
        }
    }

    /**
     * Delivers post-ime input events to the view hierarchy.
     */
    final class ViewPostImeInputStage extends android.view.ViewRootImpl.InputStage {
        public ViewPostImeInputStage(android.view.ViewRootImpl.InputStage next) {
            super(next);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (q.mEvent instanceof android.view.KeyEvent) {
                return processKeyEvent(q);
            } else {
                final int source = q.mEvent.getSource();
                if ((source & android.view.InputDevice.SOURCE_CLASS_POINTER) != 0) {
                    return processPointerEvent(q);
                } else
                    if ((source & android.view.InputDevice.SOURCE_CLASS_TRACKBALL) != 0) {
                        return processTrackballEvent(q);
                    } else {
                        return processGenericMotionEvent(q);
                    }

            }
        }

        @java.lang.Override
        protected void onDeliverToNext(android.view.ViewRootImpl.QueuedInputEvent q) {
            if (((mUnbufferedInputDispatch && (q.mEvent instanceof android.view.MotionEvent)) && ((android.view.MotionEvent) (q.mEvent)).isTouchEvent()) && android.view.ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                mUnbufferedInputDispatch = false;
                scheduleConsumeBatchedInput();
            }
            super.onDeliverToNext(q);
        }

        private boolean performFocusNavigation(android.view.KeyEvent event) {
            int direction = 0;
            switch (event.getKeyCode()) {
                case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                    if (event.hasNoModifiers()) {
                        direction = android.view.View.FOCUS_LEFT;
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                    if (event.hasNoModifiers()) {
                        direction = android.view.View.FOCUS_RIGHT;
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_DPAD_UP :
                    if (event.hasNoModifiers()) {
                        direction = android.view.View.FOCUS_UP;
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                    if (event.hasNoModifiers()) {
                        direction = android.view.View.FOCUS_DOWN;
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_TAB :
                    if (event.hasNoModifiers()) {
                        direction = android.view.View.FOCUS_FORWARD;
                    } else
                        if (event.hasModifiers(android.view.KeyEvent.META_SHIFT_ON)) {
                            direction = android.view.View.FOCUS_BACKWARD;
                        }

                    break;
            }
            if (direction != 0) {
                android.view.View focused = mView.findFocus();
                if (focused != null) {
                    android.view.View v = focused.focusSearch(direction);
                    if ((v != null) && (v != focused)) {
                        // do the math the get the interesting rect
                        // of previous focused into the coord system of
                        // newly focused view
                        focused.getFocusedRect(mTempRect);
                        if (mView instanceof android.view.ViewGroup) {
                            ((android.view.ViewGroup) (mView)).offsetDescendantRectToMyCoords(focused, mTempRect);
                            ((android.view.ViewGroup) (mView)).offsetRectIntoDescendantCoords(v, mTempRect);
                        }
                        if (v.requestFocus(direction, mTempRect)) {
                            playSoundEffect(android.view.SoundEffectConstants.getContantForFocusDirection(direction));
                            return true;
                        }
                    }
                    // Give the focused view a last chance to handle the dpad key.
                    if (mView.dispatchUnhandledMove(focused, direction)) {
                        return true;
                    }
                } else {
                    if (mView.restoreDefaultFocus()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean performKeyboardGroupNavigation(int direction) {
            final android.view.View focused = mView.findFocus();
            if ((focused == null) && mView.restoreDefaultFocus()) {
                return true;
            }
            android.view.View cluster = (focused == null) ? keyboardNavigationClusterSearch(null, direction) : focused.keyboardNavigationClusterSearch(null, direction);
            // Since requestFocus only takes "real" focus directions (and therefore also
            // restoreFocusInCluster), convert forward/backward focus into FOCUS_DOWN.
            int realDirection = direction;
            if ((direction == android.view.View.FOCUS_FORWARD) || (direction == android.view.View.FOCUS_BACKWARD)) {
                realDirection = android.view.View.FOCUS_DOWN;
            }
            if ((cluster != null) && cluster.isRootNamespace()) {
                // the default cluster. Try to find a non-clustered view to focus.
                if (cluster.restoreFocusNotInCluster()) {
                    playSoundEffect(android.view.SoundEffectConstants.getContantForFocusDirection(direction));
                    return true;
                }
                // otherwise skip to next actual cluster
                cluster = keyboardNavigationClusterSearch(null, direction);
            }
            if ((cluster != null) && cluster.restoreFocusInCluster(realDirection)) {
                playSoundEffect(android.view.SoundEffectConstants.getContantForFocusDirection(direction));
                return true;
            }
            return false;
        }

        private int processKeyEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.KeyEvent event = ((android.view.KeyEvent) (q.mEvent));
            if (mUnhandledKeyManager.preViewDispatch(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            // Deliver the key to the view hierarchy.
            if (mView.dispatchKeyEvent(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            if (shouldDropInputEvent(q)) {
                return android.view.ViewRootImpl.InputStage.FINISH_NOT_HANDLED;
            }
            // This dispatch is for windows that don't have a Window.Callback. Otherwise,
            // the Window.Callback usually will have already called this (see
            // DecorView.superDispatchKeyEvent) leaving this call a no-op.
            if (mUnhandledKeyManager.dispatch(mView, event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            int groupNavigationDirection = 0;
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getKeyCode() == android.view.KeyEvent.KEYCODE_TAB)) {
                if (android.view.KeyEvent.metaStateHasModifiers(event.getMetaState(), android.view.KeyEvent.META_META_ON)) {
                    groupNavigationDirection = android.view.View.FOCUS_FORWARD;
                } else
                    if (android.view.KeyEvent.metaStateHasModifiers(event.getMetaState(), android.view.KeyEvent.META_META_ON | android.view.KeyEvent.META_SHIFT_ON)) {
                        groupNavigationDirection = android.view.View.FOCUS_BACKWARD;
                    }

            }
            // If a modifier is held, try to interpret the key as a shortcut.
            if (((((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (!android.view.KeyEvent.metaStateHasNoModifiers(event.getMetaState()))) && (event.getRepeatCount() == 0)) && (!android.view.KeyEvent.isModifierKey(event.getKeyCode()))) && (groupNavigationDirection == 0)) {
                if (mView.dispatchKeyShortcutEvent(event)) {
                    return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                }
                if (shouldDropInputEvent(q)) {
                    return android.view.ViewRootImpl.InputStage.FINISH_NOT_HANDLED;
                }
            }
            // Apply the fallback event policy.
            if (mFallbackEventHandler.dispatchKeyEvent(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            if (shouldDropInputEvent(q)) {
                return android.view.ViewRootImpl.InputStage.FINISH_NOT_HANDLED;
            }
            // Handle automatic focus changes.
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                if (groupNavigationDirection != 0) {
                    if (performKeyboardGroupNavigation(groupNavigationDirection)) {
                        return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                    }
                } else {
                    if (performFocusNavigation(event)) {
                        return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                    }
                }
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private int processPointerEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
            mAttachInfo.mUnbufferedDispatchRequested = false;
            mAttachInfo.mHandlingPointerEvent = true;
            boolean handled = mView.dispatchPointerEvent(event);
            maybeUpdatePointerIcon(event);
            maybeUpdateTooltip(event);
            mAttachInfo.mHandlingPointerEvent = false;
            if (mAttachInfo.mUnbufferedDispatchRequested && (!mUnbufferedInputDispatch)) {
                mUnbufferedInputDispatch = true;
                if (mConsumeBatchedInputScheduled) {
                    scheduleConsumeBatchedInputImmediately();
                }
            }
            return handled ? android.view.ViewRootImpl.InputStage.FINISH_HANDLED : android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private void maybeUpdatePointerIcon(android.view.MotionEvent event) {
            if ((event.getPointerCount() == 1) && event.isFromSource(android.view.InputDevice.SOURCE_MOUSE)) {
                if ((event.getActionMasked() == android.view.MotionEvent.ACTION_HOVER_ENTER) || (event.getActionMasked() == android.view.MotionEvent.ACTION_HOVER_EXIT)) {
                    // Other apps or the window manager may change the icon type outside of
                    // this app, therefore the icon type has to be reset on enter/exit event.
                    mPointerIconType = android.view.PointerIcon.TYPE_NOT_SPECIFIED;
                }
                if (event.getActionMasked() != android.view.MotionEvent.ACTION_HOVER_EXIT) {
                    if ((!updatePointerIcon(event)) && (event.getActionMasked() == android.view.MotionEvent.ACTION_HOVER_MOVE)) {
                        mPointerIconType = android.view.PointerIcon.TYPE_NOT_SPECIFIED;
                    }
                }
            }
        }

        private int processTrackballEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
            if (event.isFromSource(android.view.InputDevice.SOURCE_MOUSE_RELATIVE)) {
                if ((!hasPointerCapture()) || mView.dispatchCapturedPointerEvent(event)) {
                    return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                }
            }
            if (mView.dispatchTrackballEvent(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        private int processGenericMotionEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
            final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
            if (event.isFromSource(android.view.InputDevice.SOURCE_TOUCHPAD)) {
                if (hasPointerCapture() && mView.dispatchCapturedPointerEvent(event)) {
                    return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                }
            }
            // Deliver the event to the view.
            if (mView.dispatchGenericMotionEvent(event)) {
                return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
            }
            return android.view.ViewRootImpl.InputStage.FORWARD;
        }
    }

    private void resetPointerIcon(android.view.MotionEvent event) {
        mPointerIconType = android.view.PointerIcon.TYPE_NOT_SPECIFIED;
        updatePointerIcon(event);
    }

    private boolean updatePointerIcon(android.view.MotionEvent event) {
        final int pointerIndex = 0;
        final float x = event.getX(pointerIndex);
        final float y = event.getY(pointerIndex);
        if (mView == null) {
            // E.g. click outside a popup to dismiss it
            android.util.Slog.d(mTag, "updatePointerIcon called after view was removed");
            return false;
        }
        if ((((x < 0) || (x >= mView.getWidth())) || (y < 0)) || (y >= mView.getHeight())) {
            // E.g. when moving window divider with mouse
            android.util.Slog.d(mTag, "updatePointerIcon called with position out of bounds");
            return false;
        }
        final android.view.PointerIcon pointerIcon = mView.onResolvePointerIcon(event, pointerIndex);
        final int pointerType = (pointerIcon != null) ? pointerIcon.getType() : android.view.PointerIcon.TYPE_DEFAULT;
        if (mPointerIconType != pointerType) {
            mPointerIconType = pointerType;
            mCustomPointerIcon = null;
            if (mPointerIconType != android.view.PointerIcon.TYPE_CUSTOM) {
                android.hardware.input.InputManager.getInstance().setPointerIconType(pointerType);
                return true;
            }
        }
        if ((mPointerIconType == android.view.PointerIcon.TYPE_CUSTOM) && (!pointerIcon.equals(mCustomPointerIcon))) {
            mCustomPointerIcon = pointerIcon;
            android.hardware.input.InputManager.getInstance().setCustomPointerIcon(mCustomPointerIcon);
        }
        return true;
    }

    private void maybeUpdateTooltip(android.view.MotionEvent event) {
        if (event.getPointerCount() != 1) {
            return;
        }
        final int action = event.getActionMasked();
        if (((action != android.view.MotionEvent.ACTION_HOVER_ENTER) && (action != android.view.MotionEvent.ACTION_HOVER_MOVE)) && (action != android.view.MotionEvent.ACTION_HOVER_EXIT)) {
            return;
        }
        android.view.accessibility.AccessibilityManager manager = android.view.accessibility.AccessibilityManager.getInstance(mContext);
        if (manager.isEnabled() && manager.isTouchExplorationEnabled()) {
            return;
        }
        if (mView == null) {
            android.util.Slog.d(mTag, "maybeUpdateTooltip called after view was removed");
            return;
        }
        mView.dispatchTooltipHoverEvent(event);
    }

    /**
     * Performs synthesis of new input events from unhandled input events.
     */
    final class SyntheticInputStage extends android.view.ViewRootImpl.InputStage {
        private final android.view.ViewRootImpl.SyntheticTrackballHandler mTrackball = new android.view.ViewRootImpl.SyntheticTrackballHandler();

        private final android.view.ViewRootImpl.SyntheticJoystickHandler mJoystick = new android.view.ViewRootImpl.SyntheticJoystickHandler();

        private final android.view.ViewRootImpl.SyntheticTouchNavigationHandler mTouchNavigation = new android.view.ViewRootImpl.SyntheticTouchNavigationHandler();

        private final android.view.ViewRootImpl.SyntheticKeyboardHandler mKeyboard = new android.view.ViewRootImpl.SyntheticKeyboardHandler();

        public SyntheticInputStage() {
            super(null);
        }

        @java.lang.Override
        protected int onProcess(android.view.ViewRootImpl.QueuedInputEvent q) {
            q.mFlags |= android.view.ViewRootImpl.QueuedInputEvent.FLAG_RESYNTHESIZED;
            if (q.mEvent instanceof android.view.MotionEvent) {
                final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
                final int source = event.getSource();
                if ((source & android.view.InputDevice.SOURCE_CLASS_TRACKBALL) != 0) {
                    mTrackball.process(event);
                    return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                } else
                    if ((source & android.view.InputDevice.SOURCE_CLASS_JOYSTICK) != 0) {
                        mJoystick.process(event);
                        return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                    } else
                        if ((source & android.view.InputDevice.SOURCE_TOUCH_NAVIGATION) == android.view.InputDevice.SOURCE_TOUCH_NAVIGATION) {
                            mTouchNavigation.process(event);
                            return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                        }


            } else
                if ((q.mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_UNHANDLED) != 0) {
                    mKeyboard.process(((android.view.KeyEvent) (q.mEvent)));
                    return android.view.ViewRootImpl.InputStage.FINISH_HANDLED;
                }

            return android.view.ViewRootImpl.InputStage.FORWARD;
        }

        @java.lang.Override
        protected void onDeliverToNext(android.view.ViewRootImpl.QueuedInputEvent q) {
            if ((q.mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_RESYNTHESIZED) == 0) {
                // Cancel related synthetic events if any prior stage has handled the event.
                if (q.mEvent instanceof android.view.MotionEvent) {
                    final android.view.MotionEvent event = ((android.view.MotionEvent) (q.mEvent));
                    final int source = event.getSource();
                    if ((source & android.view.InputDevice.SOURCE_CLASS_TRACKBALL) != 0) {
                        mTrackball.cancel();
                    } else
                        if ((source & android.view.InputDevice.SOURCE_CLASS_JOYSTICK) != 0) {
                            mJoystick.cancel();
                        } else
                            if ((source & android.view.InputDevice.SOURCE_TOUCH_NAVIGATION) == android.view.InputDevice.SOURCE_TOUCH_NAVIGATION) {
                                mTouchNavigation.cancel(event);
                            }


                }
            }
            super.onDeliverToNext(q);
        }

        @java.lang.Override
        protected void onWindowFocusChanged(boolean hasWindowFocus) {
            if (!hasWindowFocus) {
                mJoystick.cancel();
            }
        }

        @java.lang.Override
        protected void onDetachedFromWindow() {
            mJoystick.cancel();
        }
    }

    /**
     * Creates dpad events from unhandled trackball movements.
     */
    final class SyntheticTrackballHandler {
        private final android.view.ViewRootImpl.TrackballAxis mX = new android.view.ViewRootImpl.TrackballAxis();

        private final android.view.ViewRootImpl.TrackballAxis mY = new android.view.ViewRootImpl.TrackballAxis();

        private long mLastTime;

        public void process(android.view.MotionEvent event) {
            // Translate the trackball event into DPAD keys and try to deliver those.
            long curTime = android.os.SystemClock.uptimeMillis();
            if ((mLastTime + android.view.ViewRootImpl.MAX_TRACKBALL_DELAY) < curTime) {
                // It has been too long since the last movement,
                // so restart at the beginning.
                mX.reset(0);
                mY.reset(0);
                mLastTime = curTime;
            }
            final int action = event.getAction();
            final int metaState = event.getMetaState();
            switch (action) {
                case android.view.MotionEvent.ACTION_DOWN :
                    mX.reset(2);
                    mY.reset(2);
                    enqueueInputEvent(new android.view.KeyEvent(curTime, curTime, android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_CENTER, 0, metaState, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_FALLBACK, android.view.InputDevice.SOURCE_KEYBOARD));
                    break;
                case android.view.MotionEvent.ACTION_UP :
                    mX.reset(2);
                    mY.reset(2);
                    enqueueInputEvent(new android.view.KeyEvent(curTime, curTime, android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_DPAD_CENTER, 0, metaState, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_FALLBACK, android.view.InputDevice.SOURCE_KEYBOARD));
                    break;
            }
            if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                android.util.Log.v(mTag, (((((((((((((((((("TB X=" + mX.position) + " step=") + mX.step) + " dir=") + mX.dir) + " acc=") + mX.acceleration) + " move=") + event.getX()) + " / Y=") + mY.position) + " step=") + mY.step) + " dir=") + mY.dir) + " acc=") + mY.acceleration) + " move=") + event.getY());

            final float xOff = mX.collect(event.getX(), event.getEventTime(), "X");
            final float yOff = mY.collect(event.getY(), event.getEventTime(), "Y");
            // Generate DPAD events based on the trackball movement.
            // We pick the axis that has moved the most as the direction of
            // the DPAD.  When we generate DPAD events for one axis, then the
            // other axis is reset -- we don't want to perform DPAD jumps due
            // to slight movements in the trackball when making major movements
            // along the other axis.
            int keycode = 0;
            int movement = 0;
            float accel = 1;
            if (xOff > yOff) {
                movement = mX.generate();
                if (movement != 0) {
                    keycode = (movement > 0) ? android.view.KeyEvent.KEYCODE_DPAD_RIGHT : android.view.KeyEvent.KEYCODE_DPAD_LEFT;
                    accel = mX.acceleration;
                    mY.reset(2);
                }
            } else
                if (yOff > 0) {
                    movement = mY.generate();
                    if (movement != 0) {
                        keycode = (movement > 0) ? android.view.KeyEvent.KEYCODE_DPAD_DOWN : android.view.KeyEvent.KEYCODE_DPAD_UP;
                        accel = mY.acceleration;
                        mX.reset(2);
                    }
                }

            if (keycode != 0) {
                if (movement < 0)
                    movement = -movement;

                int accelMovement = ((int) (movement * accel));
                if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                    android.util.Log.v(mTag, (((("Move: movement=" + movement) + " accelMovement=") + accelMovement) + " accel=") + accel);

                if (accelMovement > movement) {
                    if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                        android.util.Log.v(mTag, "Delivering fake DPAD: " + keycode);

                    movement--;
                    int repeatCount = accelMovement - movement;
                    enqueueInputEvent(new android.view.KeyEvent(curTime, curTime, android.view.KeyEvent.ACTION_MULTIPLE, keycode, repeatCount, metaState, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_FALLBACK, android.view.InputDevice.SOURCE_KEYBOARD));
                }
                while (movement > 0) {
                    if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                        android.util.Log.v(mTag, "Delivering fake DPAD: " + keycode);

                    movement--;
                    curTime = android.os.SystemClock.uptimeMillis();
                    enqueueInputEvent(new android.view.KeyEvent(curTime, curTime, android.view.KeyEvent.ACTION_DOWN, keycode, 0, metaState, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_FALLBACK, android.view.InputDevice.SOURCE_KEYBOARD));
                    enqueueInputEvent(new android.view.KeyEvent(curTime, curTime, android.view.KeyEvent.ACTION_UP, keycode, 0, metaState, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_FALLBACK, android.view.InputDevice.SOURCE_KEYBOARD));
                } 
                mLastTime = curTime;
            }
        }

        public void cancel() {
            mLastTime = java.lang.Integer.MIN_VALUE;
            // If we reach this, we consumed a trackball event.
            // Because we will not translate the trackball event into a key event,
            // touch mode will not exit, so we exit touch mode here.
            if ((mView != null) && mAdded) {
                ensureTouchMode(false);
            }
        }
    }

    /**
     * Maintains state information for a single trackball axis, generating
     * discrete (DPAD) movements based on raw trackball motion.
     */
    static final class TrackballAxis {
        /**
         * The maximum amount of acceleration we will apply.
         */
        static final float MAX_ACCELERATION = 20;

        /**
         * The maximum amount of time (in milliseconds) between events in order
         * for us to consider the user to be doing fast trackball movements,
         * and thus apply an acceleration.
         */
        static final long FAST_MOVE_TIME = 150;

        /**
         * Scaling factor to the time (in milliseconds) between events to how
         * much to multiple/divide the current acceleration.  When movement
         * is < FAST_MOVE_TIME this multiplies the acceleration; when >
         * FAST_MOVE_TIME it divides it.
         */
        static final float ACCEL_MOVE_SCALING_FACTOR = 1.0F / 40;

        static final float FIRST_MOVEMENT_THRESHOLD = 0.5F;

        static final float SECOND_CUMULATIVE_MOVEMENT_THRESHOLD = 2.0F;

        static final float SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD = 1.0F;

        float position;

        float acceleration = 1;

        long lastMoveTime = 0;

        int step;

        int dir;

        int nonAccelMovement;

        void reset(int _step) {
            position = 0;
            acceleration = 1;
            lastMoveTime = 0;
            step = _step;
            dir = 0;
        }

        /**
         * Add trackball movement into the state.  If the direction of movement
         * has been reversed, the state is reset before adding the
         * movement (so that you don't have to compensate for any previously
         * collected movement before see the result of the movement in the
         * new direction).
         *
         * @return Returns the absolute value of the amount of movement
        collected so far.
         */
        float collect(float off, long time, java.lang.String axis) {
            long normTime;
            if (off > 0) {
                normTime = ((long) (off * android.view.ViewRootImpl.TrackballAxis.FAST_MOVE_TIME));
                if (dir < 0) {
                    if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                        android.util.Log.v(android.view.ViewRootImpl.TAG, axis + " reversed to positive!");

                    position = 0;
                    step = 0;
                    acceleration = 1;
                    lastMoveTime = 0;
                }
                dir = 1;
            } else
                if (off < 0) {
                    normTime = ((long) ((-off) * android.view.ViewRootImpl.TrackballAxis.FAST_MOVE_TIME));
                    if (dir > 0) {
                        if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                            android.util.Log.v(android.view.ViewRootImpl.TAG, axis + " reversed to negative!");

                        position = 0;
                        step = 0;
                        acceleration = 1;
                        lastMoveTime = 0;
                    }
                    dir = -1;
                } else {
                    normTime = 0;
                }

            // The number of milliseconds between each movement that is
            // considered "normal" and will not result in any acceleration
            // or deceleration, scaled by the offset we have here.
            if (normTime > 0) {
                long delta = time - lastMoveTime;
                lastMoveTime = time;
                float acc = acceleration;
                if (delta < normTime) {
                    // The user is scrolling rapidly, so increase acceleration.
                    float scale = (normTime - delta) * android.view.ViewRootImpl.TrackballAxis.ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1)
                        acc *= scale;

                    if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                        android.util.Log.v(android.view.ViewRootImpl.TAG, (((((((((axis + " accelerate: off=") + off) + " normTime=") + normTime) + " delta=") + delta) + " scale=") + scale) + " acc=") + acc);

                    acceleration = (acc < android.view.ViewRootImpl.TrackballAxis.MAX_ACCELERATION) ? acc : android.view.ViewRootImpl.TrackballAxis.MAX_ACCELERATION;
                } else {
                    // The user is scrolling slowly, so decrease acceleration.
                    float scale = (delta - normTime) * android.view.ViewRootImpl.TrackballAxis.ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1)
                        acc /= scale;

                    if (android.view.ViewRootImpl.DEBUG_TRACKBALL)
                        android.util.Log.v(android.view.ViewRootImpl.TAG, (((((((((axis + " deccelerate: off=") + off) + " normTime=") + normTime) + " delta=") + delta) + " scale=") + scale) + " acc=") + acc);

                    acceleration = (acc > 1) ? acc : 1;
                }
            }
            position += off;
            return java.lang.Math.abs(position);
        }

        /**
         * Generate the number of discrete movement events appropriate for
         * the currently collected trackball movement.
         *
         * @return Returns the number of discrete movements, either positive
        or negative, or 0 if there is not enough trackball movement yet
        for a discrete movement.
         */
        int generate() {
            int movement = 0;
            nonAccelMovement = 0;
            do {
                final int dir = (position >= 0) ? 1 : -1;
                switch (step) {
                    // If we are going to execute the first step, then we want
                    // to do this as soon as possible instead of waiting for
                    // a full movement, in order to make things look responsive.
                    case 0 :
                        if (java.lang.Math.abs(position) < android.view.ViewRootImpl.TrackballAxis.FIRST_MOVEMENT_THRESHOLD) {
                            return movement;
                        }
                        movement += dir;
                        nonAccelMovement += dir;
                        step = 1;
                        break;
                        // If we have generated the first movement, then we need
                        // to wait for the second complete trackball motion before
                        // generating the second discrete movement.
                    case 1 :
                        if (java.lang.Math.abs(position) < android.view.ViewRootImpl.TrackballAxis.SECOND_CUMULATIVE_MOVEMENT_THRESHOLD) {
                            return movement;
                        }
                        movement += dir;
                        nonAccelMovement += dir;
                        position -= android.view.ViewRootImpl.TrackballAxis.SECOND_CUMULATIVE_MOVEMENT_THRESHOLD * dir;
                        step = 2;
                        break;
                        // After the first two, we generate discrete movements
                        // consistently with the trackball, applying an acceleration
                        // if the trackball is moving quickly.  This is a simple
                        // acceleration on top of what we already compute based
                        // on how quickly the wheel is being turned, to apply
                        // a longer increasing acceleration to continuous movement
                        // in one direction.
                    default :
                        if (java.lang.Math.abs(position) < android.view.ViewRootImpl.TrackballAxis.SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD) {
                            return movement;
                        }
                        movement += dir;
                        position -= dir * android.view.ViewRootImpl.TrackballAxis.SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD;
                        float acc = acceleration;
                        acc *= 1.1F;
                        acceleration = (acc < android.view.ViewRootImpl.TrackballAxis.MAX_ACCELERATION) ? acc : acceleration;
                        break;
                }
            } while (true );
        }
    }

    /**
     * Creates dpad events from unhandled joystick movements.
     */
    final class SyntheticJoystickHandler extends android.os.Handler {
        private static final int MSG_ENQUEUE_X_AXIS_KEY_REPEAT = 1;

        private static final int MSG_ENQUEUE_Y_AXIS_KEY_REPEAT = 2;

        private final android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState mJoystickAxesState = new android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState();

        private final android.util.SparseArray<android.view.KeyEvent> mDeviceKeyEvents = new android.util.SparseArray();

        public SyntheticJoystickHandler() {
            super(true);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.view.ViewRootImpl.SyntheticJoystickHandler.MSG_ENQUEUE_X_AXIS_KEY_REPEAT :
                case android.view.ViewRootImpl.SyntheticJoystickHandler.MSG_ENQUEUE_Y_AXIS_KEY_REPEAT :
                    {
                        if (mAttachInfo.mHasWindowFocus) {
                            android.view.KeyEvent oldEvent = ((android.view.KeyEvent) (msg.obj));
                            android.view.KeyEvent e = android.view.KeyEvent.changeTimeRepeat(oldEvent, android.os.SystemClock.uptimeMillis(), oldEvent.getRepeatCount() + 1);
                            enqueueInputEvent(e);
                            android.os.Message m = obtainMessage(msg.what, e);
                            m.setAsynchronous(true);
                            sendMessageDelayed(m, android.view.ViewConfiguration.getKeyRepeatDelay());
                        }
                    }
                    break;
            }
        }

        public void process(android.view.MotionEvent event) {
            switch (event.getActionMasked()) {
                case android.view.MotionEvent.ACTION_CANCEL :
                    cancel();
                    break;
                case android.view.MotionEvent.ACTION_MOVE :
                    update(event);
                    break;
                default :
                    android.util.Log.w(mTag, "Unexpected action: " + event.getActionMasked());
            }
        }

        private void cancel() {
            removeMessages(android.view.ViewRootImpl.SyntheticJoystickHandler.MSG_ENQUEUE_X_AXIS_KEY_REPEAT);
            removeMessages(android.view.ViewRootImpl.SyntheticJoystickHandler.MSG_ENQUEUE_Y_AXIS_KEY_REPEAT);
            for (int i = 0; i < mDeviceKeyEvents.size(); i++) {
                final android.view.KeyEvent keyEvent = mDeviceKeyEvents.valueAt(i);
                if (keyEvent != null) {
                    enqueueInputEvent(android.view.KeyEvent.changeTimeRepeat(keyEvent, android.os.SystemClock.uptimeMillis(), 0));
                }
            }
            mDeviceKeyEvents.clear();
            mJoystickAxesState.resetState();
        }

        private void update(android.view.MotionEvent event) {
            final int historySize = event.getHistorySize();
            for (int h = 0; h < historySize; h++) {
                final long time = event.getHistoricalEventTime(h);
                mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_X, event.getHistoricalAxisValue(android.view.MotionEvent.AXIS_X, 0, h));
                mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_Y, event.getHistoricalAxisValue(android.view.MotionEvent.AXIS_Y, 0, h));
                mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_HAT_X, event.getHistoricalAxisValue(android.view.MotionEvent.AXIS_HAT_X, 0, h));
                mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_HAT_Y, event.getHistoricalAxisValue(android.view.MotionEvent.AXIS_HAT_Y, 0, h));
            }
            final long time = event.getEventTime();
            mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_X, event.getAxisValue(android.view.MotionEvent.AXIS_X));
            mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_Y, event.getAxisValue(android.view.MotionEvent.AXIS_Y));
            mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_HAT_X, event.getAxisValue(android.view.MotionEvent.AXIS_HAT_X));
            mJoystickAxesState.updateStateForAxis(event, time, android.view.MotionEvent.AXIS_HAT_Y, event.getAxisValue(android.view.MotionEvent.AXIS_HAT_Y));
        }

        final class JoystickAxesState {
            // State machine: from neutral state (no button press) can go into
            // button STATE_UP_OR_LEFT or STATE_DOWN_OR_RIGHT state, emitting an ACTION_DOWN event.
            // From STATE_UP_OR_LEFT or STATE_DOWN_OR_RIGHT state can go into neutral state,
            // emitting an ACTION_UP event.
            private static final int STATE_UP_OR_LEFT = -1;

            private static final int STATE_NEUTRAL = 0;

            private static final int STATE_DOWN_OR_RIGHT = 1;

            final int[] mAxisStatesHat = new int[]{ android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL, android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL };// {AXIS_HAT_X, AXIS_HAT_Y}


            final int[] mAxisStatesStick = new int[]{ android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL, android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL };// {AXIS_X, AXIS_Y}


            void resetState() {
                mAxisStatesHat[0] = android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL;
                mAxisStatesHat[1] = android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL;
                mAxisStatesStick[0] = android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL;
                mAxisStatesStick[1] = android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL;
            }

            void updateStateForAxis(android.view.MotionEvent event, long time, int axis, float value) {
                // Emit KeyEvent if necessary
                // axis can be AXIS_X, AXIS_Y, AXIS_HAT_X, AXIS_HAT_Y
                final int axisStateIndex;
                final int repeatMessage;
                if (isXAxis(axis)) {
                    axisStateIndex = 0;
                    repeatMessage = android.view.ViewRootImpl.SyntheticJoystickHandler.MSG_ENQUEUE_X_AXIS_KEY_REPEAT;
                } else
                    if (isYAxis(axis)) {
                        axisStateIndex = 1;
                        repeatMessage = android.view.ViewRootImpl.SyntheticJoystickHandler.MSG_ENQUEUE_Y_AXIS_KEY_REPEAT;
                    } else {
                        android.util.Log.e(mTag, ("Unexpected axis " + axis) + " in updateStateForAxis!");
                        return;
                    }

                final int newState = joystickAxisValueToState(value);
                final int currentState;
                if ((axis == android.view.MotionEvent.AXIS_X) || (axis == android.view.MotionEvent.AXIS_Y)) {
                    currentState = mAxisStatesStick[axisStateIndex];
                } else {
                    currentState = mAxisStatesHat[axisStateIndex];
                }
                if (currentState == newState) {
                    return;
                }
                final int metaState = event.getMetaState();
                final int deviceId = event.getDeviceId();
                final int source = event.getSource();
                if ((currentState == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_DOWN_OR_RIGHT) || (currentState == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_UP_OR_LEFT)) {
                    // send a button release event
                    final int keyCode = joystickAxisAndStateToKeycode(axis, currentState);
                    if (keyCode != android.view.KeyEvent.KEYCODE_UNKNOWN) {
                        enqueueInputEvent(new android.view.KeyEvent(time, time, android.view.KeyEvent.ACTION_UP, keyCode, 0, metaState, deviceId, 0, android.view.KeyEvent.FLAG_FALLBACK, source));
                        // remove the corresponding pending UP event if focus lost/view detached
                        mDeviceKeyEvents.put(deviceId, null);
                    }
                    removeMessages(repeatMessage);
                }
                if ((newState == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_DOWN_OR_RIGHT) || (newState == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_UP_OR_LEFT)) {
                    // send a button down event
                    final int keyCode = joystickAxisAndStateToKeycode(axis, newState);
                    if (keyCode != android.view.KeyEvent.KEYCODE_UNKNOWN) {
                        android.view.KeyEvent keyEvent = new android.view.KeyEvent(time, time, android.view.KeyEvent.ACTION_DOWN, keyCode, 0, metaState, deviceId, 0, android.view.KeyEvent.FLAG_FALLBACK, source);
                        enqueueInputEvent(keyEvent);
                        android.os.Message m = obtainMessage(repeatMessage, keyEvent);
                        m.setAsynchronous(true);
                        sendMessageDelayed(m, android.view.ViewConfiguration.getKeyRepeatTimeout());
                        // store the corresponding ACTION_UP event so that it can be sent
                        // if focus is lost or root view is removed
                        mDeviceKeyEvents.put(deviceId, new android.view.KeyEvent(time, time, android.view.KeyEvent.ACTION_UP, keyCode, 0, metaState, deviceId, 0, android.view.KeyEvent.FLAG_FALLBACK | android.view.KeyEvent.FLAG_CANCELED, source));
                    }
                }
                if ((axis == android.view.MotionEvent.AXIS_X) || (axis == android.view.MotionEvent.AXIS_Y)) {
                    mAxisStatesStick[axisStateIndex] = newState;
                } else {
                    mAxisStatesHat[axisStateIndex] = newState;
                }
            }

            private boolean isXAxis(int axis) {
                return (axis == android.view.MotionEvent.AXIS_X) || (axis == android.view.MotionEvent.AXIS_HAT_X);
            }

            private boolean isYAxis(int axis) {
                return (axis == android.view.MotionEvent.AXIS_Y) || (axis == android.view.MotionEvent.AXIS_HAT_Y);
            }

            private int joystickAxisAndStateToKeycode(int axis, int state) {
                if (isXAxis(axis) && (state == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_UP_OR_LEFT)) {
                    return android.view.KeyEvent.KEYCODE_DPAD_LEFT;
                }
                if (isXAxis(axis) && (state == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_DOWN_OR_RIGHT)) {
                    return android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
                }
                if (isYAxis(axis) && (state == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_UP_OR_LEFT)) {
                    return android.view.KeyEvent.KEYCODE_DPAD_UP;
                }
                if (isYAxis(axis) && (state == android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_DOWN_OR_RIGHT)) {
                    return android.view.KeyEvent.KEYCODE_DPAD_DOWN;
                }
                android.util.Log.e(mTag, (("Unknown axis " + axis) + " or direction ") + state);
                return android.view.KeyEvent.KEYCODE_UNKNOWN;// should never happen

            }

            private int joystickAxisValueToState(float value) {
                if (value >= 0.5F) {
                    return android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_DOWN_OR_RIGHT;
                } else
                    if (value <= (-0.5F)) {
                        return android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_UP_OR_LEFT;
                    } else {
                        return android.view.ViewRootImpl.SyntheticJoystickHandler.JoystickAxesState.STATE_NEUTRAL;
                    }

            }
        }
    }

    /**
     * Creates dpad events from unhandled touch navigation movements.
     */
    final class SyntheticTouchNavigationHandler extends android.os.Handler {
        private static final java.lang.String LOCAL_TAG = "SyntheticTouchNavigationHandler";

        private static final boolean LOCAL_DEBUG = false;

        // Assumed nominal width and height in millimeters of a touch navigation pad,
        // if no resolution information is available from the input system.
        private static final float DEFAULT_WIDTH_MILLIMETERS = 48;

        private static final float DEFAULT_HEIGHT_MILLIMETERS = 48;

        /* TODO: These constants should eventually be moved to ViewConfiguration. */
        // The nominal distance traveled to move by one unit.
        private static final int TICK_DISTANCE_MILLIMETERS = 12;

        // Minimum and maximum fling velocity in ticks per second.
        // The minimum velocity should be set such that we perform enough ticks per
        // second that the fling appears to be fluid.  For example, if we set the minimum
        // to 2 ticks per second, then there may be up to half a second delay between the next
        // to last and last ticks which is noticeably discrete and jerky.  This value should
        // probably not be set to anything less than about 4.
        // If fling accuracy is a problem then consider tuning the tick distance instead.
        private static final float MIN_FLING_VELOCITY_TICKS_PER_SECOND = 6.0F;

        private static final float MAX_FLING_VELOCITY_TICKS_PER_SECOND = 20.0F;

        // Fling velocity decay factor applied after each new key is emitted.
        // This parameter controls the deceleration and overall duration of the fling.
        // The fling stops automatically when its velocity drops below the minimum
        // fling velocity defined above.
        private static final float FLING_TICK_DECAY = 0.8F;

        /* The input device that we are tracking. */
        private int mCurrentDeviceId = -1;

        private int mCurrentSource;

        private boolean mCurrentDeviceSupported;

        /* Configuration for the current input device. */
        // The scaled tick distance.  A movement of this amount should generally translate
        // into a single dpad event in a given direction.
        private float mConfigTickDistance;

        // The minimum and maximum scaled fling velocity.
        private float mConfigMinFlingVelocity;

        private float mConfigMaxFlingVelocity;

        /* Tracking state. */
        // The velocity tracker for detecting flings.
        private android.view.VelocityTracker mVelocityTracker;

        // The active pointer id, or -1 if none.
        private int mActivePointerId = -1;

        // Location where tracking started.
        private float mStartX;

        private float mStartY;

        // Most recently observed position.
        private float mLastX;

        private float mLastY;

        // Accumulated movement delta since the last direction key was sent.
        private float mAccumulatedX;

        private float mAccumulatedY;

        // Set to true if any movement was delivered to the app.
        // Implies that tap slop was exceeded.
        private boolean mConsumedMovement;

        // The most recently sent key down event.
        // The keycode remains set until the direction changes or a fling ends
        // so that repeated key events may be generated as required.
        private long mPendingKeyDownTime;

        private int mPendingKeyCode = android.view.KeyEvent.KEYCODE_UNKNOWN;

        private int mPendingKeyRepeatCount;

        private int mPendingKeyMetaState;

        // The current fling velocity while a fling is in progress.
        private boolean mFlinging;

        private float mFlingVelocity;

        public SyntheticTouchNavigationHandler() {
            super(true);
        }

        public void process(android.view.MotionEvent event) {
            // Update the current device information.
            final long time = event.getEventTime();
            final int deviceId = event.getDeviceId();
            final int source = event.getSource();
            if ((mCurrentDeviceId != deviceId) || (mCurrentSource != source)) {
                finishKeys(time);
                finishTracking(time);
                mCurrentDeviceId = deviceId;
                mCurrentSource = source;
                mCurrentDeviceSupported = false;
                android.view.InputDevice device = event.getDevice();
                if (device != null) {
                    // In order to support an input device, we must know certain
                    // characteristics about it, such as its size and resolution.
                    android.view.InputDevice.MotionRange xRange = device.getMotionRange(android.view.MotionEvent.AXIS_X);
                    android.view.InputDevice.MotionRange yRange = device.getMotionRange(android.view.MotionEvent.AXIS_Y);
                    if ((xRange != null) && (yRange != null)) {
                        mCurrentDeviceSupported = true;
                        // Infer the resolution if it not actually known.
                        float xRes = xRange.getResolution();
                        if (xRes <= 0) {
                            xRes = xRange.getRange() / android.view.ViewRootImpl.SyntheticTouchNavigationHandler.DEFAULT_WIDTH_MILLIMETERS;
                        }
                        float yRes = yRange.getResolution();
                        if (yRes <= 0) {
                            yRes = yRange.getRange() / android.view.ViewRootImpl.SyntheticTouchNavigationHandler.DEFAULT_HEIGHT_MILLIMETERS;
                        }
                        float nominalRes = (xRes + yRes) * 0.5F;
                        // Precompute all of the configuration thresholds we will need.
                        mConfigTickDistance = android.view.ViewRootImpl.SyntheticTouchNavigationHandler.TICK_DISTANCE_MILLIMETERS * nominalRes;
                        mConfigMinFlingVelocity = android.view.ViewRootImpl.SyntheticTouchNavigationHandler.MIN_FLING_VELOCITY_TICKS_PER_SECOND * mConfigTickDistance;
                        mConfigMaxFlingVelocity = android.view.ViewRootImpl.SyntheticTouchNavigationHandler.MAX_FLING_VELOCITY_TICKS_PER_SECOND * mConfigTickDistance;
                        if (android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_DEBUG) {
                            android.util.Log.d(android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_TAG, ((((((((("Configured device " + mCurrentDeviceId) + " (") + java.lang.Integer.toHexString(mCurrentSource)) + "): ") + ", mConfigTickDistance=") + mConfigTickDistance) + ", mConfigMinFlingVelocity=") + mConfigMinFlingVelocity) + ", mConfigMaxFlingVelocity=") + mConfigMaxFlingVelocity);
                        }
                    }
                }
            }
            if (!mCurrentDeviceSupported) {
                return;
            }
            // Handle the event.
            final int action = event.getActionMasked();
            switch (action) {
                case android.view.MotionEvent.ACTION_DOWN :
                    {
                        boolean caughtFling = mFlinging;
                        finishKeys(time);
                        finishTracking(time);
                        mActivePointerId = event.getPointerId(0);
                        mVelocityTracker = android.view.VelocityTracker.obtain();
                        mVelocityTracker.addMovement(event);
                        mStartX = event.getX();
                        mStartY = event.getY();
                        mLastX = mStartX;
                        mLastY = mStartY;
                        mAccumulatedX = 0;
                        mAccumulatedY = 0;
                        // If we caught a fling, then pretend that the tap slop has already
                        // been exceeded to suppress taps whose only purpose is to stop the fling.
                        mConsumedMovement = caughtFling;
                        break;
                    }
                case android.view.MotionEvent.ACTION_MOVE :
                case android.view.MotionEvent.ACTION_UP :
                    {
                        if (mActivePointerId < 0) {
                            break;
                        }
                        final int index = event.findPointerIndex(mActivePointerId);
                        if (index < 0) {
                            finishKeys(time);
                            finishTracking(time);
                            break;
                        }
                        mVelocityTracker.addMovement(event);
                        final float x = event.getX(index);
                        final float y = event.getY(index);
                        mAccumulatedX += x - mLastX;
                        mAccumulatedY += y - mLastY;
                        mLastX = x;
                        mLastY = y;
                        // Consume any accumulated movement so far.
                        final int metaState = event.getMetaState();
                        consumeAccumulatedMovement(time, metaState);
                        // Detect taps and flings.
                        if (action == android.view.MotionEvent.ACTION_UP) {
                            if (mConsumedMovement && (mPendingKeyCode != android.view.KeyEvent.KEYCODE_UNKNOWN)) {
                                // It might be a fling.
                                mVelocityTracker.computeCurrentVelocity(1000, mConfigMaxFlingVelocity);
                                final float vx = mVelocityTracker.getXVelocity(mActivePointerId);
                                final float vy = mVelocityTracker.getYVelocity(mActivePointerId);
                                if (!startFling(time, vx, vy)) {
                                    finishKeys(time);
                                }
                            }
                            finishTracking(time);
                        }
                        break;
                    }
                case android.view.MotionEvent.ACTION_CANCEL :
                    {
                        finishKeys(time);
                        finishTracking(time);
                        break;
                    }
            }
        }

        public void cancel(android.view.MotionEvent event) {
            if ((mCurrentDeviceId == event.getDeviceId()) && (mCurrentSource == event.getSource())) {
                final long time = event.getEventTime();
                finishKeys(time);
                finishTracking(time);
            }
        }

        private void finishKeys(long time) {
            cancelFling();
            sendKeyUp(time);
        }

        private void finishTracking(long time) {
            if (mActivePointerId >= 0) {
                mActivePointerId = -1;
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

        private void consumeAccumulatedMovement(long time, int metaState) {
            final float absX = java.lang.Math.abs(mAccumulatedX);
            final float absY = java.lang.Math.abs(mAccumulatedY);
            if (absX >= absY) {
                if (absX >= mConfigTickDistance) {
                    mAccumulatedX = consumeAccumulatedMovement(time, metaState, mAccumulatedX, android.view.KeyEvent.KEYCODE_DPAD_LEFT, android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
                    mAccumulatedY = 0;
                    mConsumedMovement = true;
                }
            } else {
                if (absY >= mConfigTickDistance) {
                    mAccumulatedY = consumeAccumulatedMovement(time, metaState, mAccumulatedY, android.view.KeyEvent.KEYCODE_DPAD_UP, android.view.KeyEvent.KEYCODE_DPAD_DOWN);
                    mAccumulatedX = 0;
                    mConsumedMovement = true;
                }
            }
        }

        private float consumeAccumulatedMovement(long time, int metaState, float accumulator, int negativeKeyCode, int positiveKeyCode) {
            while (accumulator <= (-mConfigTickDistance)) {
                sendKeyDownOrRepeat(time, negativeKeyCode, metaState);
                accumulator += mConfigTickDistance;
            } 
            while (accumulator >= mConfigTickDistance) {
                sendKeyDownOrRepeat(time, positiveKeyCode, metaState);
                accumulator -= mConfigTickDistance;
            } 
            return accumulator;
        }

        private void sendKeyDownOrRepeat(long time, int keyCode, int metaState) {
            if (mPendingKeyCode != keyCode) {
                sendKeyUp(time);
                mPendingKeyDownTime = time;
                mPendingKeyCode = keyCode;
                mPendingKeyRepeatCount = 0;
            } else {
                mPendingKeyRepeatCount += 1;
            }
            mPendingKeyMetaState = metaState;
            // Note: Normally we would pass FLAG_LONG_PRESS when the repeat count is 1
            // but it doesn't quite make sense when simulating the events in this way.
            if (android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_DEBUG) {
                android.util.Log.d(android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_TAG, (((("Sending key down: keyCode=" + mPendingKeyCode) + ", repeatCount=") + mPendingKeyRepeatCount) + ", metaState=") + java.lang.Integer.toHexString(mPendingKeyMetaState));
            }
            enqueueInputEvent(new android.view.KeyEvent(mPendingKeyDownTime, time, android.view.KeyEvent.ACTION_DOWN, mPendingKeyCode, mPendingKeyRepeatCount, mPendingKeyMetaState, mCurrentDeviceId, android.view.KeyEvent.FLAG_FALLBACK, mCurrentSource));
        }

        private void sendKeyUp(long time) {
            if (mPendingKeyCode != android.view.KeyEvent.KEYCODE_UNKNOWN) {
                if (android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_DEBUG) {
                    android.util.Log.d(android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_TAG, (("Sending key up: keyCode=" + mPendingKeyCode) + ", metaState=") + java.lang.Integer.toHexString(mPendingKeyMetaState));
                }
                enqueueInputEvent(new android.view.KeyEvent(mPendingKeyDownTime, time, android.view.KeyEvent.ACTION_UP, mPendingKeyCode, 0, mPendingKeyMetaState, mCurrentDeviceId, 0, android.view.KeyEvent.FLAG_FALLBACK, mCurrentSource));
                mPendingKeyCode = android.view.KeyEvent.KEYCODE_UNKNOWN;
            }
        }

        private boolean startFling(long time, float vx, float vy) {
            if (android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_DEBUG) {
                android.util.Log.d(android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_TAG, (((("Considering fling: vx=" + vx) + ", vy=") + vy) + ", min=") + mConfigMinFlingVelocity);
            }
            // Flings must be oriented in the same direction as the preceding movements.
            switch (mPendingKeyCode) {
                case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                    if (((-vx) >= mConfigMinFlingVelocity) && (java.lang.Math.abs(vy) < mConfigMinFlingVelocity)) {
                        mFlingVelocity = -vx;
                        break;
                    }
                    return false;
                case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                    if ((vx >= mConfigMinFlingVelocity) && (java.lang.Math.abs(vy) < mConfigMinFlingVelocity)) {
                        mFlingVelocity = vx;
                        break;
                    }
                    return false;
                case android.view.KeyEvent.KEYCODE_DPAD_UP :
                    if (((-vy) >= mConfigMinFlingVelocity) && (java.lang.Math.abs(vx) < mConfigMinFlingVelocity)) {
                        mFlingVelocity = -vy;
                        break;
                    }
                    return false;
                case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                    if ((vy >= mConfigMinFlingVelocity) && (java.lang.Math.abs(vx) < mConfigMinFlingVelocity)) {
                        mFlingVelocity = vy;
                        break;
                    }
                    return false;
            }
            // Post the first fling event.
            mFlinging = postFling(time);
            return mFlinging;
        }

        private boolean postFling(long time) {
            // The idea here is to estimate the time when the pointer would have
            // traveled one tick distance unit given the current fling velocity.
            // This effect creates continuity of motion.
            if (mFlingVelocity >= mConfigMinFlingVelocity) {
                long delay = ((long) ((mConfigTickDistance / mFlingVelocity) * 1000));
                postAtTime(mFlingRunnable, time + delay);
                if (android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_DEBUG) {
                    android.util.Log.d(android.view.ViewRootImpl.SyntheticTouchNavigationHandler.LOCAL_TAG, (((("Posted fling: velocity=" + mFlingVelocity) + ", delay=") + delay) + ", keyCode=") + mPendingKeyCode);
                }
                return true;
            }
            return false;
        }

        private void cancelFling() {
            if (mFlinging) {
                removeCallbacks(mFlingRunnable);
                mFlinging = false;
            }
        }

        private final java.lang.Runnable mFlingRunnable = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                final long time = android.os.SystemClock.uptimeMillis();
                sendKeyDownOrRepeat(time, mPendingKeyCode, mPendingKeyMetaState);
                mFlingVelocity *= android.view.ViewRootImpl.SyntheticTouchNavigationHandler.FLING_TICK_DECAY;
                if (!postFling(time)) {
                    mFlinging = false;
                    finishKeys(time);
                }
            }
        };
    }

    final class SyntheticKeyboardHandler {
        public void process(android.view.KeyEvent event) {
            if ((event.getFlags() & android.view.KeyEvent.FLAG_FALLBACK) != 0) {
                return;
            }
            final android.view.KeyCharacterMap kcm = event.getKeyCharacterMap();
            final int keyCode = event.getKeyCode();
            final int metaState = event.getMetaState();
            // Check for fallback actions specified by the key character map.
            android.view.KeyCharacterMap.FallbackAction fallbackAction = kcm.getFallbackAction(keyCode, metaState);
            if (fallbackAction != null) {
                final int flags = event.getFlags() | android.view.KeyEvent.FLAG_FALLBACK;
                android.view.KeyEvent fallbackEvent = android.view.KeyEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), fallbackAction.keyCode, event.getRepeatCount(), fallbackAction.metaState, event.getDeviceId(), event.getScanCode(), flags, event.getSource(), null);
                fallbackAction.recycle();
                enqueueInputEvent(fallbackEvent);
            }
        }
    }

    /**
     * Returns true if the key is used for keyboard navigation.
     *
     * @param keyEvent
     * 		The key event.
     * @return True if the key is used for keyboard navigation.
     */
    private static boolean isNavigationKey(android.view.KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
            case android.view.KeyEvent.KEYCODE_PAGE_UP :
            case android.view.KeyEvent.KEYCODE_PAGE_DOWN :
            case android.view.KeyEvent.KEYCODE_MOVE_HOME :
            case android.view.KeyEvent.KEYCODE_MOVE_END :
            case android.view.KeyEvent.KEYCODE_TAB :
            case android.view.KeyEvent.KEYCODE_SPACE :
            case android.view.KeyEvent.KEYCODE_ENTER :
                return true;
        }
        return false;
    }

    /**
     * Returns true if the key is used for typing.
     *
     * @param keyEvent
     * 		The key event.
     * @return True if the key is used for typing.
     */
    private static boolean isTypingKey(android.view.KeyEvent keyEvent) {
        return keyEvent.getUnicodeChar() > 0;
    }

    /**
     * See if the key event means we should leave touch mode (and leave touch mode if so).
     *
     * @param event
     * 		The key event.
     * @return Whether this key event should be consumed (meaning the act of
    leaving touch mode alone is considered the event).
     */
    private boolean checkForLeavingTouchModeAndConsume(android.view.KeyEvent event) {
        // Only relevant in touch mode.
        if (!mAttachInfo.mInTouchMode) {
            return false;
        }
        // Only consider leaving touch mode on DOWN or MULTIPLE actions, never on UP.
        final int action = event.getAction();
        if ((action != android.view.KeyEvent.ACTION_DOWN) && (action != android.view.KeyEvent.ACTION_MULTIPLE)) {
            return false;
        }
        // Don't leave touch mode if the IME told us not to.
        if ((event.getFlags() & android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE) != 0) {
            return false;
        }
        // If the key can be used for keyboard navigation then leave touch mode
        // and select a focused view if needed (in ensureTouchMode).
        // When a new focused view is selected, we consume the navigation key because
        // navigation doesn't make much sense unless a view already has focus so
        // the key's purpose is to set focus.
        if (android.view.ViewRootImpl.isNavigationKey(event)) {
            return ensureTouchMode(false);
        }
        // If the key can be used for typing then leave touch mode
        // and select a focused view if needed (in ensureTouchMode).
        // Always allow the view to process the typing key.
        if (android.view.ViewRootImpl.isTypingKey(event)) {
            ensureTouchMode(false);
            return false;
        }
        return false;
    }

    /* drag/drop */
    @android.annotation.UnsupportedAppUsage
    void setLocalDragState(java.lang.Object obj) {
        mLocalDragState = obj;
    }

    private void handleDragEvent(android.view.DragEvent event) {
        // From the root, only drag start/end/location are dispatched.  entered/exited
        // are determined and dispatched by the viewgroup hierarchy, who then report
        // that back here for ultimate reporting back to the framework.
        if ((mView != null) && mAdded) {
            final int what = event.mAction;
            // Cache the drag description when the operation starts, then fill it in
            // on subsequent calls as a convenience
            if (what == android.view.DragEvent.ACTION_DRAG_STARTED) {
                mCurrentDragView = null;// Start the current-recipient tracking

                mDragDescription = event.mClipDescription;
            } else {
                if (what == android.view.DragEvent.ACTION_DRAG_ENDED) {
                    mDragDescription = null;
                }
                event.mClipDescription = mDragDescription;
            }
            if (what == android.view.DragEvent.ACTION_DRAG_EXITED) {
                // A direct EXITED event means that the window manager knows we've just crossed
                // a window boundary, so the current drag target within this one must have
                // just been exited. Send the EXITED notification to the current drag view, if any.
                if (android.view.View.sCascadedDragDrop) {
                    mView.dispatchDragEnterExitInPreN(event);
                }
                setDragFocus(null, event);
            } else {
                // For events with a [screen] location, translate into window coordinates
                if ((what == android.view.DragEvent.ACTION_DRAG_LOCATION) || (what == android.view.DragEvent.ACTION_DROP)) {
                    mDragPoint.set(event.mX, event.mY);
                    if (mTranslator != null) {
                        mTranslator.translatePointInScreenToAppWindow(mDragPoint);
                    }
                    if (mCurScrollY != 0) {
                        mDragPoint.offset(0, mCurScrollY);
                    }
                    event.mX = mDragPoint.x;
                    event.mY = mDragPoint.y;
                }
                // Remember who the current drag target is pre-dispatch
                final android.view.View prevDragView = mCurrentDragView;
                if ((what == android.view.DragEvent.ACTION_DROP) && (event.mClipData != null)) {
                    event.mClipData.prepareToEnterProcess();
                }
                // Now dispatch the drag/drop event
                boolean result = mView.dispatchDragEvent(event);
                if ((what == android.view.DragEvent.ACTION_DRAG_LOCATION) && (!event.mEventHandlerWasCalled)) {
                    // If the LOCATION event wasn't delivered to any handler, no view now has a drag
                    // focus.
                    setDragFocus(null, event);
                }
                // If we changed apparent drag target, tell the OS about it
                if (prevDragView != mCurrentDragView) {
                    try {
                        if (prevDragView != null) {
                            mWindowSession.dragRecipientExited(mWindow);
                        }
                        if (mCurrentDragView != null) {
                            mWindowSession.dragRecipientEntered(mWindow);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(mTag, "Unable to note drag target change");
                    }
                }
                // Report the drop result when we're done
                if (what == android.view.DragEvent.ACTION_DROP) {
                    try {
                        android.util.Log.i(mTag, "Reporting drop result: " + result);
                        mWindowSession.reportDropResult(mWindow, result);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(mTag, "Unable to report drop result");
                    }
                }
                // When the drag operation ends, reset drag-related state
                if (what == android.view.DragEvent.ACTION_DRAG_ENDED) {
                    mCurrentDragView = null;
                    setLocalDragState(null);
                    mAttachInfo.mDragToken = null;
                    if (mAttachInfo.mDragSurface != null) {
                        mAttachInfo.mDragSurface.release();
                        mAttachInfo.mDragSurface = null;
                    }
                }
            }
        }
        event.recycle();
    }

    public void handleDispatchSystemUiVisibilityChanged(android.view.ViewRootImpl.SystemUiVisibilityInfo args) {
        if (mSeq != args.seq) {
            // The sequence has changed, so we need to update our value and make
            // sure to do a traversal afterward so the window manager is given our
            // most recent data.
            mSeq = args.seq;
            mAttachInfo.mForceReportNewAttributes = true;
            scheduleTraversals();
        }
        if (mView == null)
            return;

        if (args.localChanges != 0) {
            mView.updateLocalSystemUiVisibility(args.localValue, args.localChanges);
        }
        int visibility = args.globalVisibility & android.view.View.SYSTEM_UI_CLEARABLE_FLAGS;
        if (visibility != mAttachInfo.mGlobalSystemUiVisibility) {
            mAttachInfo.mGlobalSystemUiVisibility = visibility;
            mView.dispatchSystemUiVisibilityChanged(visibility);
        }
    }

    /**
     * Notify that the window title changed
     */
    public void onWindowTitleChanged() {
        mAttachInfo.mForceReportNewAttributes = true;
    }

    public void handleDispatchWindowShown() {
        mAttachInfo.mTreeObserver.dispatchOnWindowShown();
    }

    public void handleRequestKeyboardShortcuts(com.android.internal.os.IResultReceiver receiver, int deviceId) {
        android.os.Bundle data = new android.os.Bundle();
        java.util.ArrayList<android.view.KeyboardShortcutGroup> list = new java.util.ArrayList<>();
        if (mView != null) {
            mView.requestKeyboardShortcuts(list, deviceId);
        }
        data.putParcelableArrayList(android.view.WindowManager.PARCEL_KEY_SHORTCUTS_ARRAY, list);
        try {
            receiver.send(0, data);
        } catch (android.os.RemoteException e) {
        }
    }

    @android.annotation.UnsupportedAppUsage
    public void getLastTouchPoint(android.graphics.Point outLocation) {
        outLocation.x = ((int) (mLastTouchPoint.x));
        outLocation.y = ((int) (mLastTouchPoint.y));
    }

    public int getLastTouchSource() {
        return mLastTouchSource;
    }

    public void setDragFocus(android.view.View newDragTarget, android.view.DragEvent event) {
        if ((mCurrentDragView != newDragTarget) && (!android.view.View.sCascadedDragDrop)) {
            // Send EXITED and ENTERED notifications to the old and new drag focus views.
            final float tx = event.mX;
            final float ty = event.mY;
            final int action = event.mAction;
            final android.content.ClipData td = event.mClipData;
            // Position should not be available for ACTION_DRAG_ENTERED and ACTION_DRAG_EXITED.
            event.mX = 0;
            event.mY = 0;
            event.mClipData = null;
            if (mCurrentDragView != null) {
                event.mAction = android.view.DragEvent.ACTION_DRAG_EXITED;
                mCurrentDragView.callDragEventHandler(event);
            }
            if (newDragTarget != null) {
                event.mAction = android.view.DragEvent.ACTION_DRAG_ENTERED;
                newDragTarget.callDragEventHandler(event);
            }
            event.mAction = action;
            event.mX = tx;
            event.mY = ty;
            event.mClipData = td;
        }
        mCurrentDragView = newDragTarget;
    }

    private android.media.AudioManager getAudioManager() {
        if (mView == null) {
            throw new java.lang.IllegalStateException("getAudioManager called when there is no mView");
        }
        if (mAudioManager == null) {
            mAudioManager = ((android.media.AudioManager) (mView.getContext().getSystemService(android.content.Context.AUDIO_SERVICE)));
        }
        return mAudioManager;
    }

    @android.annotation.Nullable
    private android.view.autofill.AutofillManager getAutofillManager() {
        if (mView instanceof android.view.ViewGroup) {
            android.view.ViewGroup decorView = ((android.view.ViewGroup) (mView));
            if (decorView.getChildCount() > 0) {
                // We cannot use decorView's Context for querying AutofillManager: DecorView's
                // context is based on Application Context, it would allocate a different
                // AutofillManager instance.
                return decorView.getChildAt(0).getContext().getSystemService(android.view.autofill.AutofillManager.class);
            }
        }
        return null;
    }

    private boolean isAutofillUiShowing() {
        android.view.autofill.AutofillManager afm = getAutofillManager();
        if (afm == null) {
            return false;
        }
        return afm.isAutofillUiShowing();
    }

    public android.view.AccessibilityInteractionController getAccessibilityInteractionController() {
        if (mView == null) {
            throw new java.lang.IllegalStateException("getAccessibilityInteractionController" + " called when there is no mView");
        }
        if (mAccessibilityInteractionController == null) {
            mAccessibilityInteractionController = new android.view.AccessibilityInteractionController(this);
        }
        return mAccessibilityInteractionController;
    }

    private int relayoutWindow(android.view.WindowManager.LayoutParams params, int viewVisibility, boolean insetsPending) throws android.os.RemoteException {
        float appScale = mAttachInfo.mApplicationScale;
        boolean restore = false;
        if ((params != null) && (mTranslator != null)) {
            restore = true;
            params.backup();
            mTranslator.translateWindowLayout(params);
        }
        if (params != null) {
            if (android.view.ViewRootImpl.DBG)
                android.util.Log.d(mTag, "WindowLayout in layoutWindow:" + params);

            if (mOrigWindowType != params.type) {
                // For compatibility with old apps, don't crash here.
                if (mTargetSdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    android.util.Slog.w(mTag, ("Window type can not be changed after " + "the window is added; ignoring change of ") + mView);
                    params.type = mOrigWindowType;
                }
            }
        }
        long frameNumber = -1;
        if (mSurface.isValid()) {
            frameNumber = mSurface.getNextFrameNumber();
        }
        int relayoutResult = mWindowSession.relayout(mWindow, mSeq, params, ((int) ((mView.getMeasuredWidth() * appScale) + 0.5F)), ((int) ((mView.getMeasuredHeight() * appScale) + 0.5F)), viewVisibility, insetsPending ? android.view.WindowManagerGlobal.RELAYOUT_INSETS_PENDING : 0, frameNumber, mTmpFrame, mPendingOverscanInsets, mPendingContentInsets, mPendingVisibleInsets, mPendingStableInsets, mPendingOutsets, mPendingBackDropFrame, mPendingDisplayCutout, mPendingMergedConfiguration, mSurfaceControl, mTempInsets);
        if (mSurfaceControl.isValid()) {
            mSurface.copyFrom(mSurfaceControl);
        } else {
            destroySurface();
        }
        mPendingAlwaysConsumeSystemBars = (relayoutResult & android.view.WindowManagerGlobal.RELAYOUT_RES_CONSUME_ALWAYS_SYSTEM_BARS) != 0;
        if (restore) {
            params.restore();
        }
        if (mTranslator != null) {
            mTranslator.translateRectInScreenToAppWinFrame(mTmpFrame);
            mTranslator.translateRectInScreenToAppWindow(mPendingOverscanInsets);
            mTranslator.translateRectInScreenToAppWindow(mPendingContentInsets);
            mTranslator.translateRectInScreenToAppWindow(mPendingVisibleInsets);
            mTranslator.translateRectInScreenToAppWindow(mPendingStableInsets);
        }
        setFrame(mTmpFrame);
        mInsetsController.onStateChanged(mTempInsets);
        return relayoutResult;
    }

    private void setFrame(android.graphics.Rect frame) {
        mWinFrame.set(frame);
        mInsetsController.onFrameChanged(frame);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void playSoundEffect(int effectId) {
        checkThread();
        try {
            final android.media.AudioManager audioManager = getAudioManager();
            switch (effectId) {
                case android.view.SoundEffectConstants.CLICK :
                    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                    return;
                case android.view.SoundEffectConstants.NAVIGATION_DOWN :
                    audioManager.playSoundEffect(AudioManager.FX_FOCUS_NAVIGATION_DOWN);
                    return;
                case android.view.SoundEffectConstants.NAVIGATION_LEFT :
                    audioManager.playSoundEffect(AudioManager.FX_FOCUS_NAVIGATION_LEFT);
                    return;
                case android.view.SoundEffectConstants.NAVIGATION_RIGHT :
                    audioManager.playSoundEffect(AudioManager.FX_FOCUS_NAVIGATION_RIGHT);
                    return;
                case android.view.SoundEffectConstants.NAVIGATION_UP :
                    audioManager.playSoundEffect(AudioManager.FX_FOCUS_NAVIGATION_UP);
                    return;
                default :
                    throw new java.lang.IllegalArgumentException((("unknown effect id " + effectId) + " not defined in ") + android.view.SoundEffectConstants.class.getCanonicalName());
            }
        } catch (java.lang.IllegalStateException e) {
            // Exception thrown by getAudioManager() when mView is null
            android.util.Log.e(mTag, "FATAL EXCEPTION when attempting to play sound effect: " + e);
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean performHapticFeedback(int effectId, boolean always) {
        try {
            return mWindowSession.performHapticFeedback(effectId, always);
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.view.View focusSearch(android.view.View focused, int direction) {
        checkThread();
        if (!(mView instanceof android.view.ViewGroup)) {
            return null;
        }
        return android.view.FocusFinder.getInstance().findNextFocus(((android.view.ViewGroup) (mView)), focused, direction);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.view.View keyboardNavigationClusterSearch(android.view.View currentCluster, @android.view.View.FocusDirection
    int direction) {
        checkThread();
        return android.view.FocusFinder.getInstance().findNextKeyboardNavigationCluster(mView, currentCluster, direction);
    }

    public void debug() {
        mView.debug();
    }

    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        java.lang.String innerPrefix = prefix + "  ";
        writer.print(prefix);
        writer.println("ViewRoot:");
        writer.print(innerPrefix);
        writer.print("mAdded=");
        writer.print(mAdded);
        writer.print(" mRemoved=");
        writer.println(mRemoved);
        writer.print(innerPrefix);
        writer.print("mConsumeBatchedInputScheduled=");
        writer.println(mConsumeBatchedInputScheduled);
        writer.print(innerPrefix);
        writer.print("mConsumeBatchedInputImmediatelyScheduled=");
        writer.println(mConsumeBatchedInputImmediatelyScheduled);
        writer.print(innerPrefix);
        writer.print("mPendingInputEventCount=");
        writer.println(mPendingInputEventCount);
        writer.print(innerPrefix);
        writer.print("mProcessInputEventsScheduled=");
        writer.println(mProcessInputEventsScheduled);
        writer.print(innerPrefix);
        writer.print("mTraversalScheduled=");
        writer.print(mTraversalScheduled);
        writer.print(innerPrefix);
        writer.print("mIsAmbientMode=");
        writer.print(mIsAmbientMode);
        if (mTraversalScheduled) {
            writer.print(" (barrier=");
            writer.print(mTraversalBarrier);
            writer.println(")");
        } else {
            writer.println();
        }
        mFirstInputStage.dump(innerPrefix, writer);
        mChoreographer.dump(prefix, writer);
        mInsetsController.dump(prefix, writer);
        writer.print(prefix);
        writer.println("View Hierarchy:");
        dumpViewHierarchy(innerPrefix, writer, mView);
    }

    private void dumpViewHierarchy(java.lang.String prefix, java.io.PrintWriter writer, android.view.View view) {
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
            return;
        }
        writer.println(view.toString());
        if (!(view instanceof android.view.ViewGroup)) {
            return;
        }
        android.view.ViewGroup grp = ((android.view.ViewGroup) (view));
        final int N = grp.getChildCount();
        if (N <= 0) {
            return;
        }
        prefix = prefix + "  ";
        for (int i = 0; i < N; i++) {
            dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
        }
    }

    public void dumpGfxInfo(int[] info) {
        info[0] = info[1] = 0;
        if (mView != null) {
            android.view.ViewRootImpl.getGfxInfo(mView, info);
        }
    }

    private static void getGfxInfo(android.view.View view, int[] info) {
        android.graphics.RenderNode renderNode = view.mRenderNode;
        info[0]++;
        if (renderNode != null) {
            info[1] += ((int) (renderNode.computeApproximateMemoryUsage()));
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = ((android.view.ViewGroup) (view));
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.ViewRootImpl.getGfxInfo(group.getChildAt(i), info);
            }
        }
    }

    /**
     *
     *
     * @param immediate
     * 		True, do now if not in traversal. False, put on queue and do later.
     * @return True, request has been queued. False, request has been completed.
     */
    boolean die(boolean immediate) {
        // Make sure we do execute immediately if we are in the middle of a traversal or the damage
        // done by dispatchDetachedFromWindow will cause havoc on return.
        if (immediate && (!mIsInTraversal)) {
            doDie();
            return false;
        }
        if (!mIsDrawing) {
            destroyHardwareRenderer();
        } else {
            android.util.Log.e(mTag, ((("Attempting to destroy the window while drawing!\n" + "  window=") + this) + ", title=") + mWindowAttributes.getTitle());
        }
        mHandler.sendEmptyMessage(android.view.ViewRootImpl.MSG_DIE);
        return true;
    }

    void doDie() {
        checkThread();
        if (android.view.ViewRootImpl.LOCAL_LOGV)
            android.util.Log.v(mTag, (("DIE in " + this) + " of ") + mSurface);

        synchronized(this) {
            if (mRemoved) {
                return;
            }
            mRemoved = true;
            if (mAdded) {
                dispatchDetachedFromWindow();
            }
            if (mAdded && (!mFirst)) {
                destroyHardwareRenderer();
                if (mView != null) {
                    int viewVisibility = mView.getVisibility();
                    boolean viewVisibilityChanged = mViewVisibility != viewVisibility;
                    if (mWindowAttributesChanged || viewVisibilityChanged) {
                        // If layout params have been changed, first give them
                        // to the window manager to make sure it has the correct
                        // animation info.
                        try {
                            if ((relayoutWindow(mWindowAttributes, viewVisibility, false) & android.view.WindowManagerGlobal.RELAYOUT_RES_FIRST_TIME) != 0) {
                                mWindowSession.finishDrawing(mWindow);
                            }
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    destroySurface();
                }
            }
            mAdded = false;
        }
        android.view.WindowManagerGlobal.getInstance().doRemoveView(this);
    }

    public void requestUpdateConfiguration(android.content.res.Configuration config) {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_UPDATE_CONFIGURATION, config);
        mHandler.sendMessage(msg);
    }

    public void loadSystemProperties() {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Profiling
                mProfileRendering = android.os.SystemProperties.getBoolean(android.view.ViewRootImpl.PROPERTY_PROFILE_RENDERING, false);
                profileRendering(mAttachInfo.mHasWindowFocus);
                // Hardware rendering
                if (mAttachInfo.mThreadedRenderer != null) {
                    if (mAttachInfo.mThreadedRenderer.loadSystemProperties()) {
                        invalidate();
                    }
                }
                // Layout debugging
                boolean layout = android.sysprop.DisplayProperties.debug_layout().orElse(false);
                if (layout != mAttachInfo.mDebugLayout) {
                    mAttachInfo.mDebugLayout = layout;
                    if (!mHandler.hasMessages(android.view.ViewRootImpl.MSG_INVALIDATE_WORLD)) {
                        mHandler.sendEmptyMessageDelayed(android.view.ViewRootImpl.MSG_INVALIDATE_WORLD, 200);
                    }
                }
            }
        });
    }

    private void destroyHardwareRenderer() {
        android.view.ThreadedRenderer hardwareRenderer = mAttachInfo.mThreadedRenderer;
        if (hardwareRenderer != null) {
            if (mView != null) {
                hardwareRenderer.destroyHardwareResources(mView);
            }
            hardwareRenderer.destroy();
            hardwareRenderer.setRequested(false);
            mAttachInfo.mThreadedRenderer = null;
            mAttachInfo.mHardwareAccelerated = false;
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void dispatchResized(android.graphics.Rect frame, android.graphics.Rect overscanInsets, android.graphics.Rect contentInsets, android.graphics.Rect visibleInsets, android.graphics.Rect stableInsets, android.graphics.Rect outsets, boolean reportDraw, android.util.MergedConfiguration mergedConfiguration, android.graphics.Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, android.view.DisplayCutout.ParcelableWrapper displayCutout) {
        if (android.view.ViewRootImpl.DEBUG_LAYOUT)
            android.util.Log.v(mTag, (((((((((("Resizing " + this) + ": frame=") + frame.toShortString()) + " contentInsets=") + contentInsets.toShortString()) + " visibleInsets=") + visibleInsets.toShortString()) + " reportDraw=") + reportDraw) + " backDropFrame=") + backDropFrame);

        // Tell all listeners that we are resizing the window so that the chrome can get
        // updated as fast as possible on a separate thread,
        if (mDragResizing && mUseMTRenderer) {
            boolean fullscreen = frame.equals(backDropFrame);
            synchronized(mWindowCallbacks) {
                for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
                    mWindowCallbacks.get(i).onWindowSizeIsChanging(backDropFrame, fullscreen, visibleInsets, stableInsets);
                }
            }
        }
        android.os.Message msg = mHandler.obtainMessage(reportDraw ? android.view.ViewRootImpl.MSG_RESIZED_REPORT : android.view.ViewRootImpl.MSG_RESIZED);
        if (mTranslator != null) {
            mTranslator.translateRectInScreenToAppWindow(frame);
            mTranslator.translateRectInScreenToAppWindow(overscanInsets);
            mTranslator.translateRectInScreenToAppWindow(contentInsets);
            mTranslator.translateRectInScreenToAppWindow(visibleInsets);
        }
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        final boolean sameProcessCall = android.os.Binder.getCallingPid() == android.os.android.os.Process.myPid();
        args.arg1 = (sameProcessCall) ? new android.graphics.Rect(frame) : frame;
        args.arg2 = (sameProcessCall) ? new android.graphics.Rect(contentInsets) : contentInsets;
        args.arg3 = (sameProcessCall) ? new android.graphics.Rect(visibleInsets) : visibleInsets;
        args.arg4 = (sameProcessCall && (mergedConfiguration != null)) ? new android.util.MergedConfiguration(mergedConfiguration) : mergedConfiguration;
        args.arg5 = (sameProcessCall) ? new android.graphics.Rect(overscanInsets) : overscanInsets;
        args.arg6 = (sameProcessCall) ? new android.graphics.Rect(stableInsets) : stableInsets;
        args.arg7 = (sameProcessCall) ? new android.graphics.Rect(outsets) : outsets;
        args.arg8 = (sameProcessCall) ? new android.graphics.Rect(backDropFrame) : backDropFrame;
        args.arg9 = displayCutout.get();// DisplayCutout is immutable.

        args.argi1 = (forceLayout) ? 1 : 0;
        args.argi2 = (alwaysConsumeSystemBars) ? 1 : 0;
        args.argi3 = displayId;
        msg.obj = args;
        mHandler.sendMessage(msg);
    }

    private void dispatchInsetsChanged(android.view.InsetsState insetsState) {
        mHandler.obtainMessage(android.view.ViewRootImpl.MSG_INSETS_CHANGED, insetsState).sendToTarget();
    }

    private void dispatchInsetsControlChanged(android.view.InsetsState insetsState, android.view.InsetsSourceControl[] activeControls) {
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = insetsState;
        args.arg2 = activeControls;
        mHandler.obtainMessage(android.view.ViewRootImpl.MSG_INSETS_CONTROL_CHANGED, args).sendToTarget();
    }

    public void dispatchMoved(int newX, int newY) {
        if (android.view.ViewRootImpl.DEBUG_LAYOUT)
            android.util.Log.v(mTag, (((("Window moved " + this) + ": newX=") + newX) + " newY=") + newY);

        if (mTranslator != null) {
            android.graphics.PointF point = new android.graphics.PointF(newX, newY);
            mTranslator.translatePointInScreenToAppWindow(point);
            newX = ((int) (point.x + 0.5));
            newY = ((int) (point.y + 0.5));
        }
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_WINDOW_MOVED, newX, newY);
        mHandler.sendMessage(msg);
    }

    /**
     * Represents a pending input event that is waiting in a queue.
     *
     * Input events are processed in serial order by the timestamp specified by
     * {@link InputEvent#getEventTimeNano()}.  In general, the input dispatcher delivers
     * one input event to the application at a time and waits for the application
     * to finish handling it before delivering the next one.
     *
     * However, because the application or IME can synthesize and inject multiple
     * key events at a time without going through the input dispatcher, we end up
     * needing a queue on the application's side.
     */
    private static final class QueuedInputEvent {
        public static final int FLAG_DELIVER_POST_IME = 1 << 0;

        public static final int FLAG_DEFERRED = 1 << 1;

        public static final int FLAG_FINISHED = 1 << 2;

        public static final int FLAG_FINISHED_HANDLED = 1 << 3;

        public static final int FLAG_RESYNTHESIZED = 1 << 4;

        public static final int FLAG_UNHANDLED = 1 << 5;

        public static final int FLAG_MODIFIED_FOR_COMPATIBILITY = 1 << 6;

        public android.view.ViewRootImpl.QueuedInputEvent mNext;

        public android.view.InputEvent mEvent;

        public android.view.InputEventReceiver mReceiver;

        public int mFlags;

        public boolean shouldSkipIme() {
            if ((mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_DELIVER_POST_IME) != 0) {
                return true;
            }
            return (mEvent instanceof android.view.MotionEvent) && (mEvent.isFromSource(android.view.InputDevice.SOURCE_CLASS_POINTER) || mEvent.isFromSource(android.view.InputDevice.SOURCE_ROTARY_ENCODER));
        }

        public boolean shouldSendToSynthesizer() {
            if ((mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_UNHANDLED) != 0) {
                return true;
            }
            return false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("QueuedInputEvent{flags=");
            boolean hasPrevious = false;
            hasPrevious = flagToString("DELIVER_POST_IME", android.view.ViewRootImpl.QueuedInputEvent.FLAG_DELIVER_POST_IME, hasPrevious, sb);
            hasPrevious = flagToString("DEFERRED", android.view.ViewRootImpl.QueuedInputEvent.FLAG_DEFERRED, hasPrevious, sb);
            hasPrevious = flagToString("FINISHED", android.view.ViewRootImpl.QueuedInputEvent.FLAG_FINISHED, hasPrevious, sb);
            hasPrevious = flagToString("FINISHED_HANDLED", android.view.ViewRootImpl.QueuedInputEvent.FLAG_FINISHED_HANDLED, hasPrevious, sb);
            hasPrevious = flagToString("RESYNTHESIZED", android.view.ViewRootImpl.QueuedInputEvent.FLAG_RESYNTHESIZED, hasPrevious, sb);
            hasPrevious = flagToString("UNHANDLED", android.view.ViewRootImpl.QueuedInputEvent.FLAG_UNHANDLED, hasPrevious, sb);
            if (!hasPrevious) {
                sb.append("0");
            }
            sb.append(", hasNextQueuedEvent=" + (mEvent != null ? "true" : "false"));
            sb.append(", hasInputEventReceiver=" + (mReceiver != null ? "true" : "false"));
            sb.append((", mEvent=" + mEvent) + "}");
            return sb.toString();
        }

        private boolean flagToString(java.lang.String name, int flag, boolean hasPrevious, java.lang.StringBuilder sb) {
            if ((mFlags & flag) != 0) {
                if (hasPrevious) {
                    sb.append("|");
                }
                sb.append(name);
                return true;
            }
            return hasPrevious;
        }
    }

    private android.view.ViewRootImpl.QueuedInputEvent obtainQueuedInputEvent(android.view.InputEvent event, android.view.InputEventReceiver receiver, int flags) {
        android.view.ViewRootImpl.QueuedInputEvent q = mQueuedInputEventPool;
        if (q != null) {
            mQueuedInputEventPoolSize -= 1;
            mQueuedInputEventPool = q.mNext;
            q.mNext = null;
        } else {
            q = new android.view.ViewRootImpl.QueuedInputEvent();
        }
        q.mEvent = event;
        q.mReceiver = receiver;
        q.mFlags = flags;
        return q;
    }

    private void recycleQueuedInputEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
        q.mEvent = null;
        q.mReceiver = null;
        if (mQueuedInputEventPoolSize < android.view.ViewRootImpl.MAX_QUEUED_INPUT_EVENT_POOL_SIZE) {
            mQueuedInputEventPoolSize += 1;
            q.mNext = mQueuedInputEventPool;
            mQueuedInputEventPool = q;
        }
    }

    @android.annotation.UnsupportedAppUsage
    void enqueueInputEvent(android.view.InputEvent event) {
        enqueueInputEvent(event, null, 0, false);
    }

    @android.annotation.UnsupportedAppUsage
    void enqueueInputEvent(android.view.InputEvent event, android.view.InputEventReceiver receiver, int flags, boolean processImmediately) {
        android.view.ViewRootImpl.QueuedInputEvent q = obtainQueuedInputEvent(event, receiver, flags);
        // Always enqueue the input event in order, regardless of its time stamp.
        // We do this because the application or the IME may inject key events
        // in response to touch events and we want to ensure that the injected keys
        // are processed in the order they were received and we cannot trust that
        // the time stamp of injected events are monotonic.
        android.view.ViewRootImpl.QueuedInputEvent last = mPendingInputEventTail;
        if (last == null) {
            mPendingInputEventHead = q;
            mPendingInputEventTail = q;
        } else {
            last.mNext = q;
            mPendingInputEventTail = q;
        }
        mPendingInputEventCount += 1;
        android.os.Trace.traceCounter(Trace.TRACE_TAG_INPUT, mPendingInputEventQueueLengthCounterName, mPendingInputEventCount);
        if (processImmediately) {
            doProcessInputEvents();
        } else {
            scheduleProcessInputEvents();
        }
    }

    private void scheduleProcessInputEvents() {
        if (!mProcessInputEventsScheduled) {
            mProcessInputEventsScheduled = true;
            android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_PROCESS_INPUT_EVENTS);
            msg.setAsynchronous(true);
            mHandler.sendMessage(msg);
        }
    }

    void doProcessInputEvents() {
        // Deliver all pending input events in the queue.
        while (mPendingInputEventHead != null) {
            android.view.ViewRootImpl.QueuedInputEvent q = mPendingInputEventHead;
            mPendingInputEventHead = q.mNext;
            if (mPendingInputEventHead == null) {
                mPendingInputEventTail = null;
            }
            q.mNext = null;
            mPendingInputEventCount -= 1;
            android.os.Trace.traceCounter(Trace.TRACE_TAG_INPUT, mPendingInputEventQueueLengthCounterName, mPendingInputEventCount);
            long eventTime = q.mEvent.getEventTimeNano();
            long oldestEventTime = eventTime;
            if (q.mEvent instanceof android.view.MotionEvent) {
                android.view.MotionEvent me = ((android.view.MotionEvent) (q.mEvent));
                if (me.getHistorySize() > 0) {
                    oldestEventTime = me.getHistoricalEventTimeNano(0);
                }
            }
            mChoreographer.mFrameInfo.updateInputEventTime(eventTime, oldestEventTime);
            deliverInputEvent(q);
        } 
        // We are done processing all input events that we can process right now
        // so we can clear the pending flag immediately.
        if (mProcessInputEventsScheduled) {
            mProcessInputEventsScheduled = false;
            mHandler.removeMessages(android.view.ViewRootImpl.MSG_PROCESS_INPUT_EVENTS);
        }
    }

    private void deliverInputEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
        android.os.Trace.asyncTraceBegin(Trace.TRACE_TAG_VIEW, "deliverInputEvent", q.mEvent.getSequenceNumber());
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onInputEvent(q.mEvent, 0);
        }
        android.view.ViewRootImpl.InputStage stage;
        if (q.shouldSendToSynthesizer()) {
            stage = mSyntheticInputStage;
        } else {
            stage = (q.shouldSkipIme()) ? mFirstPostImeInputStage : mFirstInputStage;
        }
        if (q.mEvent instanceof android.view.KeyEvent) {
            mUnhandledKeyManager.preDispatch(((android.view.KeyEvent) (q.mEvent)));
        }
        if (stage != null) {
            handleWindowFocusChanged();
            stage.deliver(q);
        } else {
            finishInputEvent(q);
        }
    }

    private void finishInputEvent(android.view.ViewRootImpl.QueuedInputEvent q) {
        android.os.Trace.asyncTraceEnd(Trace.TRACE_TAG_VIEW, "deliverInputEvent", q.mEvent.getSequenceNumber());
        if (q.mReceiver != null) {
            boolean handled = (q.mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_FINISHED_HANDLED) != 0;
            boolean modified = (q.mFlags & android.view.ViewRootImpl.QueuedInputEvent.FLAG_MODIFIED_FOR_COMPATIBILITY) != 0;
            if (modified) {
                android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "processInputEventBeforeFinish");
                android.view.InputEvent processedEvent;
                try {
                    processedEvent = mInputCompatProcessor.processInputEventBeforeFinish(q.mEvent);
                } finally {
                    android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
                }
                if (processedEvent != null) {
                    q.mReceiver.finishInputEvent(processedEvent, handled);
                }
            } else {
                q.mReceiver.finishInputEvent(q.mEvent, handled);
            }
        } else {
            q.mEvent.recycleIfNeededAfterDispatch();
        }
        recycleQueuedInputEvent(q);
    }

    static boolean isTerminalInputEvent(android.view.InputEvent event) {
        if (event instanceof android.view.KeyEvent) {
            final android.view.KeyEvent keyEvent = ((android.view.KeyEvent) (event));
            return keyEvent.getAction() == android.view.KeyEvent.ACTION_UP;
        } else {
            final android.view.MotionEvent motionEvent = ((android.view.MotionEvent) (event));
            final int action = motionEvent.getAction();
            return ((action == android.view.MotionEvent.ACTION_UP) || (action == android.view.MotionEvent.ACTION_CANCEL)) || (action == android.view.MotionEvent.ACTION_HOVER_EXIT);
        }
    }

    void scheduleConsumeBatchedInput() {
        if (!mConsumeBatchedInputScheduled) {
            mConsumeBatchedInputScheduled = true;
            mChoreographer.postCallback(android.view.Choreographer.CALLBACK_INPUT, mConsumedBatchedInputRunnable, null);
        }
    }

    void unscheduleConsumeBatchedInput() {
        if (mConsumeBatchedInputScheduled) {
            mConsumeBatchedInputScheduled = false;
            mChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_INPUT, mConsumedBatchedInputRunnable, null);
        }
    }

    void scheduleConsumeBatchedInputImmediately() {
        if (!mConsumeBatchedInputImmediatelyScheduled) {
            unscheduleConsumeBatchedInput();
            mConsumeBatchedInputImmediatelyScheduled = true;
            mHandler.post(mConsumeBatchedInputImmediatelyRunnable);
        }
    }

    void doConsumeBatchedInput(long frameTimeNanos) {
        if (mConsumeBatchedInputScheduled) {
            mConsumeBatchedInputScheduled = false;
            if (mInputEventReceiver != null) {
                if (mInputEventReceiver.consumeBatchedInputEvents(frameTimeNanos) && (frameTimeNanos != (-1))) {
                    // If we consumed a batch here, we want to go ahead and schedule the
                    // consumption of batched input events on the next frame. Otherwise, we would
                    // wait until we have more input events pending and might get starved by other
                    // things occurring in the process. If the frame time is -1, however, then
                    // we're in a non-batching mode, so there's no need to schedule this.
                    scheduleConsumeBatchedInput();
                }
            }
            doProcessInputEvents();
        }
    }

    final class TraversalRunnable implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            doTraversal();
        }
    }

    final android.view.ViewRootImpl.TraversalRunnable mTraversalRunnable = new android.view.ViewRootImpl.TraversalRunnable();

    final class WindowInputEventReceiver extends android.view.InputEventReceiver {
        public WindowInputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper) {
            super(inputChannel, looper);
        }

        @java.lang.Override
        public void onInputEvent(android.view.InputEvent event) {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "processInputEventForCompatibility");
            java.util.List<android.view.InputEvent> processedEvents;
            try {
                processedEvents = mInputCompatProcessor.processInputEventForCompatibility(event);
            } finally {
                android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
            }
            if (processedEvents != null) {
                if (processedEvents.isEmpty()) {
                    // InputEvent consumed by mInputCompatProcessor
                    finishInputEvent(event, true);
                } else {
                    for (int i = 0; i < processedEvents.size(); i++) {
                        enqueueInputEvent(processedEvents.get(i), this, android.view.ViewRootImpl.QueuedInputEvent.FLAG_MODIFIED_FOR_COMPATIBILITY, true);
                    }
                }
            } else {
                enqueueInputEvent(event, this, 0, true);
            }
        }

        @java.lang.Override
        public void onBatchedInputEventPending() {
            if (mUnbufferedInputDispatch) {
                super.onBatchedInputEventPending();
            } else {
                scheduleConsumeBatchedInput();
            }
        }

        @java.lang.Override
        public void dispose() {
            unscheduleConsumeBatchedInput();
            super.dispose();
        }
    }

    android.view.ViewRootImpl.WindowInputEventReceiver mInputEventReceiver;

    final class ConsumeBatchedInputRunnable implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            doConsumeBatchedInput(mChoreographer.getFrameTimeNanos());
        }
    }

    final android.view.ViewRootImpl.ConsumeBatchedInputRunnable mConsumedBatchedInputRunnable = new android.view.ViewRootImpl.ConsumeBatchedInputRunnable();

    boolean mConsumeBatchedInputScheduled;

    final class ConsumeBatchedInputImmediatelyRunnable implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            doConsumeBatchedInput(-1);
        }
    }

    final android.view.ViewRootImpl.ConsumeBatchedInputImmediatelyRunnable mConsumeBatchedInputImmediatelyRunnable = new android.view.ViewRootImpl.ConsumeBatchedInputImmediatelyRunnable();

    boolean mConsumeBatchedInputImmediatelyScheduled;

    final class InvalidateOnAnimationRunnable implements java.lang.Runnable {
        private boolean mPosted;

        private final java.util.ArrayList<android.view.View> mViews = new java.util.ArrayList<android.view.View>();

        private final java.util.ArrayList<android.view.View.AttachInfo.InvalidateInfo> mViewRects = new java.util.ArrayList<android.view.View.AttachInfo.InvalidateInfo>();

        private android.view.View[] mTempViews;

        private android.view.View.AttachInfo.InvalidateInfo[] mTempViewRects;

        public void addView(android.view.View view) {
            synchronized(this) {
                mViews.add(view);
                postIfNeededLocked();
            }
        }

        public void addViewRect(android.view.View.AttachInfo.InvalidateInfo info) {
            synchronized(this) {
                mViewRects.add(info);
                postIfNeededLocked();
            }
        }

        public void removeView(android.view.View view) {
            synchronized(this) {
                mViews.remove(view);
                for (int i = mViewRects.size(); (i--) > 0;) {
                    android.view.View.AttachInfo.InvalidateInfo info = mViewRects.get(i);
                    if (info.target == view) {
                        mViewRects.remove(i);
                        info.recycle();
                    }
                }
                if ((mPosted && mViews.isEmpty()) && mViewRects.isEmpty()) {
                    mChoreographer.removeCallbacks(android.view.Choreographer.CALLBACK_ANIMATION, this, null);
                    mPosted = false;
                }
            }
        }

        @java.lang.Override
        public void run() {
            final int viewCount;
            final int viewRectCount;
            synchronized(this) {
                mPosted = false;
                viewCount = mViews.size();
                if (viewCount != 0) {
                    mTempViews = mViews.toArray(mTempViews != null ? mTempViews : new android.view.View[viewCount]);
                    mViews.clear();
                }
                viewRectCount = mViewRects.size();
                if (viewRectCount != 0) {
                    mTempViewRects = mViewRects.toArray(mTempViewRects != null ? mTempViewRects : new android.view.View.AttachInfo.InvalidateInfo[viewRectCount]);
                    mViewRects.clear();
                }
            }
            for (int i = 0; i < viewCount; i++) {
                mTempViews[i].invalidate();
                mTempViews[i] = null;
            }
            for (int i = 0; i < viewRectCount; i++) {
                final android.view.View.AttachInfo.InvalidateInfo info = mTempViewRects[i];
                info.target.invalidate(info.left, info.top, info.right, info.bottom);
                info.recycle();
            }
        }

        private void postIfNeededLocked() {
            if (!mPosted) {
                mChoreographer.postCallback(android.view.Choreographer.CALLBACK_ANIMATION, this, null);
                mPosted = true;
            }
        }
    }

    final android.view.ViewRootImpl.InvalidateOnAnimationRunnable mInvalidateOnAnimationRunnable = new android.view.ViewRootImpl.InvalidateOnAnimationRunnable();

    public void dispatchInvalidateDelayed(android.view.View view, long delayMilliseconds) {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_INVALIDATE, view);
        mHandler.sendMessageDelayed(msg, delayMilliseconds);
    }

    public void dispatchInvalidateRectDelayed(android.view.View.AttachInfo.InvalidateInfo info, long delayMilliseconds) {
        final android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_INVALIDATE_RECT, info);
        mHandler.sendMessageDelayed(msg, delayMilliseconds);
    }

    public void dispatchInvalidateOnAnimation(android.view.View view) {
        mInvalidateOnAnimationRunnable.addView(view);
    }

    public void dispatchInvalidateRectOnAnimation(android.view.View.AttachInfo.InvalidateInfo info) {
        mInvalidateOnAnimationRunnable.addViewRect(info);
    }

    @android.annotation.UnsupportedAppUsage
    public void cancelInvalidate(android.view.View view) {
        mHandler.removeMessages(android.view.ViewRootImpl.MSG_INVALIDATE, view);
        // fixme: might leak the AttachInfo.InvalidateInfo objects instead of returning
        // them to the pool
        mHandler.removeMessages(android.view.ViewRootImpl.MSG_INVALIDATE_RECT, view);
        mInvalidateOnAnimationRunnable.removeView(view);
    }

    @android.annotation.UnsupportedAppUsage
    public void dispatchInputEvent(android.view.InputEvent event) {
        dispatchInputEvent(event, null);
    }

    @android.annotation.UnsupportedAppUsage
    public void dispatchInputEvent(android.view.InputEvent event, android.view.InputEventReceiver receiver) {
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = event;
        args.arg2 = receiver;
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_DISPATCH_INPUT_EVENT, args);
        msg.setAsynchronous(true);
        mHandler.sendMessage(msg);
    }

    public void synthesizeInputEvent(android.view.InputEvent event) {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_SYNTHESIZE_INPUT_EVENT, event);
        msg.setAsynchronous(true);
        mHandler.sendMessage(msg);
    }

    @android.annotation.UnsupportedAppUsage
    public void dispatchKeyFromIme(android.view.KeyEvent event) {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_DISPATCH_KEY_FROM_IME, event);
        msg.setAsynchronous(true);
        mHandler.sendMessage(msg);
    }

    public void dispatchKeyFromAutofill(android.view.KeyEvent event) {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_DISPATCH_KEY_FROM_AUTOFILL, event);
        msg.setAsynchronous(true);
        mHandler.sendMessage(msg);
    }

    /**
     * Reinject unhandled {@link InputEvent}s in order to synthesize fallbacks events.
     *
     * Note that it is the responsibility of the caller of this API to recycle the InputEvent it
     * passes in.
     */
    @android.annotation.UnsupportedAppUsage
    public void dispatchUnhandledInputEvent(android.view.InputEvent event) {
        if (event instanceof android.view.MotionEvent) {
            event = android.view.MotionEvent.obtain(((android.view.MotionEvent) (event)));
        }
        synthesizeInputEvent(event);
    }

    public void dispatchAppVisibility(boolean visible) {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_DISPATCH_APP_VISIBILITY);
        msg.arg1 = (visible) ? 1 : 0;
        mHandler.sendMessage(msg);
    }

    public void dispatchGetNewSurface() {
        android.os.Message msg = mHandler.obtainMessage(android.view.ViewRootImpl.MSG_DISPATCH_GET_NEW_SURFACE);
        mHandler.sendMessage(msg);
    }

    public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
        synchronized(this) {
            mWindowFocusChanged = true;
            mUpcomingWindowFocus = hasFocus;
            mUpcomingInTouchMode = inTouchMode;
        }
        android.os.Message msg = android.os.Message.obtain();
        msg.what = android.view.ViewRootImpl.MSG_WINDOW_FOCUS_CHANGED;
        mHandler.sendMessage(msg);
    }

    public void dispatchWindowShown() {
        mHandler.sendEmptyMessage(android.view.ViewRootImpl.MSG_DISPATCH_WINDOW_SHOWN);
    }

    public void dispatchCloseSystemDialogs(java.lang.String reason) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = android.view.ViewRootImpl.MSG_CLOSE_SYSTEM_DIALOGS;
        msg.obj = reason;
        mHandler.sendMessage(msg);
    }

    public void dispatchDragEvent(android.view.DragEvent event) {
        final int what;
        if (event.getAction() == android.view.DragEvent.ACTION_DRAG_LOCATION) {
            what = android.view.ViewRootImpl.MSG_DISPATCH_DRAG_LOCATION_EVENT;
            mHandler.removeMessages(what);
        } else {
            what = android.view.ViewRootImpl.MSG_DISPATCH_DRAG_EVENT;
        }
        android.os.Message msg = mHandler.obtainMessage(what, event);
        mHandler.sendMessage(msg);
    }

    public void updatePointerIcon(float x, float y) {
        final int what = android.view.ViewRootImpl.MSG_UPDATE_POINTER_ICON;
        mHandler.removeMessages(what);
        final long now = android.os.SystemClock.uptimeMillis();
        final android.view.MotionEvent event = android.view.MotionEvent.obtain(0, now, android.view.MotionEvent.ACTION_HOVER_MOVE, x, y, 0);
        android.os.Message msg = mHandler.obtainMessage(what, event);
        mHandler.sendMessage(msg);
    }

    public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) {
        android.view.ViewRootImpl.SystemUiVisibilityInfo args = new android.view.ViewRootImpl.SystemUiVisibilityInfo();
        args.seq = seq;
        args.globalVisibility = globalVisibility;
        args.localValue = localValue;
        args.localChanges = localChanges;
        mHandler.sendMessage(mHandler.obtainMessage(android.view.ViewRootImpl.MSG_DISPATCH_SYSTEM_UI_VISIBILITY, args));
    }

    public void dispatchCheckFocus() {
        if (!mHandler.hasMessages(android.view.ViewRootImpl.MSG_CHECK_FOCUS)) {
            // This will result in a call to checkFocus() below.
            mHandler.sendEmptyMessage(android.view.ViewRootImpl.MSG_CHECK_FOCUS);
        }
    }

    public void dispatchRequestKeyboardShortcuts(com.android.internal.os.IResultReceiver receiver, int deviceId) {
        mHandler.obtainMessage(android.view.ViewRootImpl.MSG_REQUEST_KEYBOARD_SHORTCUTS, deviceId, 0, receiver).sendToTarget();
    }

    public void dispatchPointerCaptureChanged(boolean on) {
        final int what = android.view.ViewRootImpl.MSG_POINTER_CAPTURE_CHANGED;
        mHandler.removeMessages(what);
        android.os.Message msg = mHandler.obtainMessage(what);
        msg.arg1 = (on) ? 1 : 0;
        mHandler.sendMessage(msg);
    }

    /**
     * Post a callback to send a
     * {@link AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED} event.
     * This event is send at most once every
     * {@link ViewConfiguration#getSendRecurringAccessibilityEventsInterval()}.
     */
    private void postSendWindowContentChangedCallback(android.view.View source, int changeType) {
        if (mSendWindowContentChangedAccessibilityEvent == null) {
            mSendWindowContentChangedAccessibilityEvent = new android.view.ViewRootImpl.SendWindowContentChangedAccessibilityEvent();
        }
        mSendWindowContentChangedAccessibilityEvent.runOrPost(source, changeType);
    }

    /**
     * Remove a posted callback to send a
     * {@link AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED} event.
     */
    private void removeSendWindowContentChangedCallback() {
        if (mSendWindowContentChangedAccessibilityEvent != null) {
            mHandler.removeCallbacks(mSendWindowContentChangedAccessibilityEvent);
        }
    }

    @java.lang.Override
    public boolean showContextMenuForChild(android.view.View originalView) {
        return false;
    }

    @java.lang.Override
    public boolean showContextMenuForChild(android.view.View originalView, float x, float y) {
        return false;
    }

    @java.lang.Override
    public android.view.ActionMode startActionModeForChild(android.view.View originalView, android.view.ActionMode.Callback callback) {
        return null;
    }

    @java.lang.Override
    public android.view.ActionMode startActionModeForChild(android.view.View originalView, android.view.ActionMode.Callback callback, int type) {
        return null;
    }

    @java.lang.Override
    public void createContextMenu(android.view.ContextMenu menu) {
    }

    @java.lang.Override
    public void childDrawableStateChanged(android.view.View child) {
    }

    @java.lang.Override
    public boolean requestSendAccessibilityEvent(android.view.View child, android.view.accessibility.AccessibilityEvent event) {
        if (((mView == null) || mStopped) || mPausedForTransition) {
            return false;
        }
        // Immediately flush pending content changed event (if any) to preserve event order
        if (((event.getEventType() != android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) && (mSendWindowContentChangedAccessibilityEvent != null)) && (mSendWindowContentChangedAccessibilityEvent.mSource != null)) {
            mSendWindowContentChangedAccessibilityEvent.removeCallbacksAndRun();
        }
        // Intercept accessibility focus events fired by virtual nodes to keep
        // track of accessibility focus position in such nodes.
        final int eventType = event.getEventType();
        final android.view.View source = getSourceForAccessibilityEvent(event);
        switch (eventType) {
            case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED :
                {
                    if (source != null) {
                        android.view.accessibility.AccessibilityNodeProvider provider = source.getAccessibilityNodeProvider();
                        if (provider != null) {
                            final int virtualNodeId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(event.getSourceNodeId());
                            final android.view.accessibility.AccessibilityNodeInfo node;
                            node = provider.createAccessibilityNodeInfo(virtualNodeId);
                            setAccessibilityFocus(source, node);
                        }
                    }
                }
                break;
            case android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED :
                {
                    if ((source != null) && (source.getAccessibilityNodeProvider() != null)) {
                        setAccessibilityFocus(null, null);
                    }
                }
                break;
            case android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED :
                {
                    handleWindowContentChangedEvent(event);
                }
                break;
        }
        mAccessibilityManager.sendAccessibilityEvent(event);
        return true;
    }

    private android.view.View getSourceForAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        final long sourceNodeId = event.getSourceNodeId();
        final int accessibilityViewId = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(sourceNodeId);
        return android.view.accessibility.AccessibilityNodeIdManager.getInstance().findView(accessibilityViewId);
    }

    /**
     * Updates the focused virtual view, when necessary, in response to a
     * content changed event.
     * <p>
     * This is necessary to get updated bounds after a position change.
     *
     * @param event
     * 		an accessibility event of type
     * 		{@link AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED}
     */
    private void handleWindowContentChangedEvent(android.view.accessibility.AccessibilityEvent event) {
        final android.view.View focusedHost = mAccessibilityFocusedHost;
        if ((focusedHost == null) || (mAccessibilityFocusedVirtualView == null)) {
            // No virtual view focused, nothing to do here.
            return;
        }
        final android.view.accessibility.AccessibilityNodeProvider provider = focusedHost.getAccessibilityNodeProvider();
        if (provider == null) {
            // Error state: virtual view with no provider. Clear focus.
            mAccessibilityFocusedHost = null;
            mAccessibilityFocusedVirtualView = null;
            focusedHost.clearAccessibilityFocusNoCallbacks(0);
            return;
        }
        // We only care about change types that may affect the bounds of the
        // focused virtual view.
        final int changes = event.getContentChangeTypes();
        if (((changes & android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) == 0) && (changes != android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED)) {
            return;
        }
        final long eventSourceNodeId = event.getSourceNodeId();
        final int changedViewId = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(eventSourceNodeId);
        // Search up the tree for subtree containment.
        boolean hostInSubtree = false;
        android.view.View root = mAccessibilityFocusedHost;
        while ((root != null) && (!hostInSubtree)) {
            if (changedViewId == root.getAccessibilityViewId()) {
                hostInSubtree = true;
            } else {
                final android.view.ViewParent parent = root.getParent();
                if (parent instanceof android.view.View) {
                    root = ((android.view.View) (parent));
                } else {
                    root = null;
                }
            }
        } 
        // We care only about changes in subtrees containing the host view.
        if (!hostInSubtree) {
            return;
        }
        final long focusedSourceNodeId = mAccessibilityFocusedVirtualView.getSourceNodeId();
        int focusedChildId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(focusedSourceNodeId);
        // Refresh the node for the focused virtual view.
        final android.graphics.Rect oldBounds = mTempRect;
        mAccessibilityFocusedVirtualView.getBoundsInScreen(oldBounds);
        mAccessibilityFocusedVirtualView = provider.createAccessibilityNodeInfo(focusedChildId);
        if (mAccessibilityFocusedVirtualView == null) {
            // Error state: The node no longer exists. Clear focus.
            mAccessibilityFocusedHost = null;
            focusedHost.clearAccessibilityFocusNoCallbacks(0);
            // This will probably fail, but try to keep the provider's internal
            // state consistent by clearing focus.
            provider.performAction(focusedChildId, android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
            invalidateRectOnScreen(oldBounds);
        } else {
            // The node was refreshed, invalidate bounds if necessary.
            final android.graphics.Rect newBounds = mAccessibilityFocusedVirtualView.getBoundsInScreen();
            if (!oldBounds.equals(newBounds)) {
                oldBounds.union(newBounds);
                invalidateRectOnScreen(oldBounds);
            }
        }
    }

    @java.lang.Override
    public void notifySubtreeAccessibilityStateChanged(android.view.View child, android.view.View source, int changeType) {
        postSendWindowContentChangedCallback(com.android.internal.util.Preconditions.checkNotNull(source), changeType);
    }

    @java.lang.Override
    public boolean canResolveLayoutDirection() {
        return true;
    }

    @java.lang.Override
    public boolean isLayoutDirectionResolved() {
        return true;
    }

    @java.lang.Override
    public int getLayoutDirection() {
        return android.view.View.LAYOUT_DIRECTION_RESOLVED_DEFAULT;
    }

    @java.lang.Override
    public boolean canResolveTextDirection() {
        return true;
    }

    @java.lang.Override
    public boolean isTextDirectionResolved() {
        return true;
    }

    @java.lang.Override
    public int getTextDirection() {
        return android.view.View.TEXT_DIRECTION_RESOLVED_DEFAULT;
    }

    @java.lang.Override
    public boolean canResolveTextAlignment() {
        return true;
    }

    @java.lang.Override
    public boolean isTextAlignmentResolved() {
        return true;
    }

    @java.lang.Override
    public int getTextAlignment() {
        return android.view.View.TEXT_ALIGNMENT_RESOLVED_DEFAULT;
    }

    private android.view.View getCommonPredecessor(android.view.View first, android.view.View second) {
        if (mTempHashSet == null) {
            mTempHashSet = new java.util.HashSet<android.view.View>();
        }
        java.util.HashSet<android.view.View> seen = mTempHashSet;
        seen.clear();
        android.view.View firstCurrent = first;
        while (firstCurrent != null) {
            seen.add(firstCurrent);
            android.view.ViewParent firstCurrentParent = firstCurrent.mParent;
            if (firstCurrentParent instanceof android.view.View) {
                firstCurrent = ((android.view.View) (firstCurrentParent));
            } else {
                firstCurrent = null;
            }
        } 
        android.view.View secondCurrent = second;
        while (secondCurrent != null) {
            if (seen.contains(secondCurrent)) {
                seen.clear();
                return secondCurrent;
            }
            android.view.ViewParent secondCurrentParent = secondCurrent.mParent;
            if (secondCurrentParent instanceof android.view.View) {
                secondCurrent = ((android.view.View) (secondCurrentParent));
            } else {
                secondCurrent = null;
            }
        } 
        seen.clear();
        return null;
    }

    void checkThread() {
        if (mThread != java.lang.Thread.currentThread()) {
            throw new android.view.ViewRootImpl.CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views.");
        }
    }

    @java.lang.Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // ViewAncestor never intercepts touch event, so this can be a no-op
    }

    @java.lang.Override
    public boolean requestChildRectangleOnScreen(android.view.View child, android.graphics.Rect rectangle, boolean immediate) {
        if (rectangle == null) {
            return scrollToRectOrFocus(null, immediate);
        }
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        final boolean scrolled = scrollToRectOrFocus(rectangle, immediate);
        mTempRect.set(rectangle);
        mTempRect.offset(0, -mCurScrollY);
        mTempRect.offset(mAttachInfo.mWindowLeft, mAttachInfo.mWindowTop);
        try {
            mWindowSession.onRectangleOnScreenRequested(mWindow, mTempRect);
        } catch (android.os.RemoteException re) {
            /* ignore */
        }
        return scrolled;
    }

    @java.lang.Override
    public void childHasTransientStateChanged(android.view.View child, boolean hasTransientState) {
        // Do nothing.
    }

    @java.lang.Override
    public boolean onStartNestedScroll(android.view.View child, android.view.View target, int nestedScrollAxes) {
        return false;
    }

    @java.lang.Override
    public void onStopNestedScroll(android.view.View target) {
    }

    @java.lang.Override
    public void onNestedScrollAccepted(android.view.View child, android.view.View target, int nestedScrollAxes) {
    }

    @java.lang.Override
    public void onNestedScroll(android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @java.lang.Override
    public void onNestedPreScroll(android.view.View target, int dx, int dy, int[] consumed) {
    }

    @java.lang.Override
    public boolean onNestedFling(android.view.View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @java.lang.Override
    public boolean onNestedPreFling(android.view.View target, float velocityX, float velocityY) {
        return false;
    }

    @java.lang.Override
    public boolean onNestedPrePerformAccessibilityAction(android.view.View target, int action, android.os.Bundle args) {
        return false;
    }

    private void reportNextDraw() {
        if (mReportNextDraw == false) {
            drawPending();
        }
        mReportNextDraw = true;
    }

    /**
     * Force the window to report its next draw.
     * <p>
     * This method is only supposed to be used to speed up the interaction from SystemUI and window
     * manager when waiting for the first frame to be drawn when turning on the screen. DO NOT USE
     * unless you fully understand this interaction.
     *
     * @unknown 
     */
    public void setReportNextDraw() {
        reportNextDraw();
        invalidate();
    }

    void changeCanvasOpacity(boolean opaque) {
        android.util.Log.d(mTag, "changeCanvasOpacity: opaque=" + opaque);
        opaque = opaque & ((mView.mPrivateFlags & android.view.View.PFLAG_REQUEST_TRANSPARENT_REGIONS) == 0);
        if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.setOpaque(opaque);
        }
    }

    /**
     * Dispatches a KeyEvent to all registered key fallback handlers.
     *
     * @param event
     * 		
     * @return {@code true} if the event was handled, {@code false} otherwise.
     */
    public boolean dispatchUnhandledKeyEvent(android.view.KeyEvent event) {
        return mUnhandledKeyManager.dispatch(mView, event);
    }

    class TakenSurfaceHolder extends com.android.internal.view.BaseSurfaceHolder {
        @java.lang.Override
        public boolean onAllowLockCanvas() {
            return mDrawingAllowed;
        }

        @java.lang.Override
        public void onRelayoutContainer() {
            // Not currently interesting -- from changing between fixed and layout size.
        }

        @java.lang.Override
        public void setFormat(int format) {
            ((com.android.internal.view.RootViewSurfaceTaker) (mView)).setSurfaceFormat(format);
        }

        @java.lang.Override
        public void setType(int type) {
            ((com.android.internal.view.RootViewSurfaceTaker) (mView)).setSurfaceType(type);
        }

        @java.lang.Override
        public void onUpdateSurface() {
            // We take care of format and type changes on our own.
            throw new java.lang.IllegalStateException("Shouldn't be here");
        }

        @java.lang.Override
        public boolean isCreating() {
            return mIsCreating;
        }

        @java.lang.Override
        public void setFixedSize(int width, int height) {
            throw new java.lang.UnsupportedOperationException("Currently only support sizing from layout");
        }

        @java.lang.Override
        public void setKeepScreenOn(boolean screenOn) {
            ((com.android.internal.view.RootViewSurfaceTaker) (mView)).setSurfaceKeepScreenOn(screenOn);
        }
    }

    static class W extends android.view.IWindow.Stub {
        private final java.lang.ref.WeakReference<android.view.ViewRootImpl> mViewAncestor;

        private final android.view.IWindowSession mWindowSession;

        W(android.view.ViewRootImpl viewAncestor) {
            mViewAncestor = new java.lang.ref.WeakReference<android.view.ViewRootImpl>(viewAncestor);
            mWindowSession = viewAncestor.mWindowSession;
        }

        @java.lang.Override
        public void resized(android.graphics.Rect frame, android.graphics.Rect overscanInsets, android.graphics.Rect contentInsets, android.graphics.Rect visibleInsets, android.graphics.Rect stableInsets, android.graphics.Rect outsets, boolean reportDraw, android.util.MergedConfiguration mergedConfiguration, android.graphics.Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, android.view.DisplayCutout.ParcelableWrapper displayCutout) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchResized(frame, overscanInsets, contentInsets, visibleInsets, stableInsets, outsets, reportDraw, mergedConfiguration, backDropFrame, forceLayout, alwaysConsumeSystemBars, displayId, displayCutout);
            }
        }

        @java.lang.Override
        public void insetsChanged(android.view.InsetsState insetsState) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchInsetsChanged(insetsState);
            }
        }

        @java.lang.Override
        public void insetsControlChanged(android.view.InsetsState insetsState, android.view.InsetsSourceControl[] activeControls) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchInsetsControlChanged(insetsState, activeControls);
            }
        }

        @java.lang.Override
        public void moved(int newX, int newY) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchMoved(newX, newY);
            }
        }

        @java.lang.Override
        public void dispatchAppVisibility(boolean visible) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchAppVisibility(visible);
            }
        }

        @java.lang.Override
        public void dispatchGetNewSurface() {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchGetNewSurface();
            }
        }

        @java.lang.Override
        public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.windowFocusChanged(hasFocus, inTouchMode);
            }
        }

        private static int checkCallingPermission(java.lang.String permission) {
            try {
                return android.app.ActivityManager.getService().checkPermission(permission, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
            } catch (android.os.RemoteException e) {
                return android.content.pm.PackageManager.PERMISSION_DENIED;
            }
        }

        @java.lang.Override
        public void executeCommand(java.lang.String command, java.lang.String parameters, android.os.ParcelFileDescriptor out) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                final android.view.View view = viewAncestor.mView;
                if (view != null) {
                    if (android.view.ViewRootImpl.W.checkCallingPermission(Manifest.permission.DUMP) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        throw new java.lang.SecurityException(((("Insufficient permissions to invoke" + " executeCommand() from pid=") + android.os.Binder.getCallingPid()) + ", uid=") + android.os.Binder.getCallingUid());
                    }
                    java.io.OutputStream clientStream = null;
                    try {
                        clientStream = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(out);
                        android.view.ViewDebug.dispatchCommand(view, command, parameters, clientStream);
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (clientStream != null) {
                            try {
                                clientStream.close();
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        @java.lang.Override
        public void closeSystemDialogs(java.lang.String reason) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchCloseSystemDialogs(reason);
            }
        }

        @java.lang.Override
        public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
            if (sync) {
                try {
                    mWindowSession.wallpaperOffsetsComplete(asBinder());
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @java.lang.Override
        public void dispatchWallpaperCommand(java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) {
            if (sync) {
                try {
                    mWindowSession.wallpaperCommandComplete(asBinder(), null);
                } catch (android.os.RemoteException e) {
                }
            }
        }

        /* Drag/drop */
        @java.lang.Override
        public void dispatchDragEvent(android.view.DragEvent event) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchDragEvent(event);
            }
        }

        @java.lang.Override
        public void updatePointerIcon(float x, float y) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.updatePointerIcon(x, y);
            }
        }

        @java.lang.Override
        public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchSystemUiVisibilityChanged(seq, globalVisibility, localValue, localChanges);
            }
        }

        @java.lang.Override
        public void dispatchWindowShown() {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchWindowShown();
            }
        }

        @java.lang.Override
        public void requestAppKeyboardShortcuts(com.android.internal.os.IResultReceiver receiver, int deviceId) {
            android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchRequestKeyboardShortcuts(receiver, deviceId);
            }
        }

        @java.lang.Override
        public void dispatchPointerCaptureChanged(boolean hasCapture) {
            final android.view.ViewRootImpl viewAncestor = mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchPointerCaptureChanged(hasCapture);
            }
        }
    }

    public static final class CalledFromWrongThreadException extends android.util.AndroidRuntimeException {
        @android.annotation.UnsupportedAppUsage
        public CalledFromWrongThreadException(java.lang.String msg) {
            super(msg);
        }
    }

    static android.view.HandlerActionQueue getRunQueue() {
        android.view.HandlerActionQueue rq = android.view.ViewRootImpl.sRunQueues.get();
        if (rq != null) {
            return rq;
        }
        rq = new android.view.HandlerActionQueue();
        android.view.ViewRootImpl.sRunQueues.set(rq);
        return rq;
    }

    /**
     * Start a drag resizing which will inform all listeners that a window resize is taking place.
     */
    private void startDragResizing(android.graphics.Rect initialBounds, boolean fullscreen, android.graphics.Rect systemInsets, android.graphics.Rect stableInsets, int resizeMode) {
        if (!mDragResizing) {
            mDragResizing = true;
            if (mUseMTRenderer) {
                for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
                    mWindowCallbacks.get(i).onWindowDragResizeStart(initialBounds, fullscreen, systemInsets, stableInsets, resizeMode);
                }
            }
            mFullRedrawNeeded = true;
        }
    }

    /**
     * End a drag resize which will inform all listeners that a window resize has ended.
     */
    private void endDragResizing() {
        if (mDragResizing) {
            mDragResizing = false;
            if (mUseMTRenderer) {
                for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
                    mWindowCallbacks.get(i).onWindowDragResizeEnd();
                }
            }
            mFullRedrawNeeded = true;
        }
    }

    private boolean updateContentDrawBounds() {
        boolean updated = false;
        if (mUseMTRenderer) {
            for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
                updated |= mWindowCallbacks.get(i).onContentDrawn(mWindowAttributes.surfaceInsets.left, mWindowAttributes.surfaceInsets.top, mWidth, mHeight);
            }
        }
        return updated | (mDragResizing && mReportNextDraw);
    }

    private void requestDrawWindow() {
        if (!mUseMTRenderer) {
            return;
        }
        mWindowDrawCountDown = new java.util.concurrent.CountDownLatch(mWindowCallbacks.size());
        for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
            mWindowCallbacks.get(i).onRequestDraw(mReportNextDraw);
        }
    }

    /**
     * Tells this instance that its corresponding activity has just relaunched. In this case, we
     * need to force a relayout of the window to make sure we get the correct bounds from window
     * manager.
     */
    public void reportActivityRelaunched() {
        mActivityRelaunched = true;
    }

    public android.view.SurfaceControl getSurfaceControl() {
        return mSurfaceControl;
    }

    /**
     * Class for managing the accessibility interaction connection
     * based on the global accessibility state.
     */
    final class AccessibilityInteractionConnectionManager implements android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener {
        @java.lang.Override
        public void onAccessibilityStateChanged(boolean enabled) {
            if (enabled) {
                ensureConnection();
                if (mAttachInfo.mHasWindowFocus && (mView != null)) {
                    mView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                    android.view.View focusedView = mView.findFocus();
                    if ((focusedView != null) && (focusedView != mView)) {
                        focusedView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    }
                }
            } else {
                ensureNoConnection();
                mHandler.obtainMessage(android.view.ViewRootImpl.MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST).sendToTarget();
            }
        }

        public void ensureConnection() {
            final boolean registered = mAttachInfo.mAccessibilityWindowId != android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
            if (!registered) {
                mAttachInfo.mAccessibilityWindowId = mAccessibilityManager.addAccessibilityInteractionConnection(mWindow, mContext.getPackageName(), new android.view.ViewRootImpl.AccessibilityInteractionConnection(android.view.ViewRootImpl.this));
            }
        }

        public void ensureNoConnection() {
            final boolean registered = mAttachInfo.mAccessibilityWindowId != android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
            if (registered) {
                mAttachInfo.mAccessibilityWindowId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
                mAccessibilityManager.removeAccessibilityInteractionConnection(mWindow);
            }
        }
    }

    final class HighContrastTextManager implements android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener {
        HighContrastTextManager() {
            android.view.ThreadedRenderer.setHighContrastText(mAccessibilityManager.isHighTextContrastEnabled());
        }

        @java.lang.Override
        public void onHighTextContrastStateChanged(boolean enabled) {
            android.view.ThreadedRenderer.setHighContrastText(enabled);
            // Destroy Displaylists so they can be recreated with high contrast recordings
            destroyHardwareResources();
            // Schedule redraw, which will rerecord + redraw all text
            invalidate();
        }
    }

    /**
     * This class is an interface this ViewAncestor provides to the
     * AccessibilityManagerService to the latter can interact with
     * the view hierarchy in this ViewAncestor.
     */
    static final class AccessibilityInteractionConnection extends android.view.accessibility.IAccessibilityInteractionConnection.Stub {
        private final java.lang.ref.WeakReference<android.view.ViewRootImpl> mViewRootImpl;

        AccessibilityInteractionConnection(android.view.ViewRootImpl viewRootImpl) {
            mViewRootImpl = new java.lang.ref.WeakReference<android.view.ViewRootImpl>(viewRootImpl);
        }

        @java.lang.Override
        public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec, android.os.Bundle args) {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfoByAccessibilityIdClientThread(accessibilityNodeId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec, args);
            } else {
                // We cannot make the call and notify the caller so it does not wait.
                try {
                    callback.setFindAccessibilityNodeInfosResult(null, interactionId);
                } catch (android.os.RemoteException re) {
                    /* best effort - ignore */
                }
            }
        }

        @java.lang.Override
        public void performAccessibilityAction(long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().performAccessibilityActionClientThread(accessibilityNodeId, action, arguments, interactionId, callback, flags, interrogatingPid, interrogatingTid);
            } else {
                // We cannot make the call and notify the caller so it does not wait.
                try {
                    callback.setPerformAccessibilityActionResult(false, interactionId);
                } catch (android.os.RemoteException re) {
                    /* best effort - ignore */
                }
            }
        }

        @java.lang.Override
        public void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, java.lang.String viewId, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByViewIdClientThread(accessibilityNodeId, viewId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            } else {
                // We cannot make the call and notify the caller so it does not wait.
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                } catch (android.os.RemoteException re) {
                    /* best effort - ignore */
                }
            }
        }

        @java.lang.Override
        public void findAccessibilityNodeInfosByText(long accessibilityNodeId, java.lang.String text, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByTextClientThread(accessibilityNodeId, text, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            } else {
                // We cannot make the call and notify the caller so it does not wait.
                try {
                    callback.setFindAccessibilityNodeInfosResult(null, interactionId);
                } catch (android.os.RemoteException re) {
                    /* best effort - ignore */
                }
            }
        }

        @java.lang.Override
        public void findFocus(long accessibilityNodeId, int focusType, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().findFocusClientThread(accessibilityNodeId, focusType, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            } else {
                // We cannot make the call and notify the caller so it does not wait.
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                } catch (android.os.RemoteException re) {
                    /* best effort - ignore */
                }
            }
        }

        @java.lang.Override
        public void focusSearch(long accessibilityNodeId, int direction, android.graphics.Region interactiveRegion, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, android.view.MagnificationSpec spec) {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().focusSearchClientThread(accessibilityNodeId, direction, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            } else {
                // We cannot make the call and notify the caller so it does not wait.
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                } catch (android.os.RemoteException re) {
                    /* best effort - ignore */
                }
            }
        }

        @java.lang.Override
        public void clearAccessibilityFocus() {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().clearAccessibilityFocusClientThread();
            }
        }

        @java.lang.Override
        public void notifyOutsideTouch() {
            android.view.ViewRootImpl viewRootImpl = mViewRootImpl.get();
            if ((viewRootImpl != null) && (viewRootImpl.mView != null)) {
                viewRootImpl.getAccessibilityInteractionController().notifyOutsideTouchClientThread();
            }
        }
    }

    private class SendWindowContentChangedAccessibilityEvent implements java.lang.Runnable {
        private int mChangeTypes = 0;

        public android.view.View mSource;

        public long mLastEventTimeMillis;

        /**
         * Override for {@link AccessibilityEvent#originStackTrace} to provide the stack trace
         * of the original {@link #runOrPost} call instead of one for sending the delayed event
         * from a looper.
         */
        public java.lang.StackTraceElement[] mOrigin;

        @java.lang.Override
        public void run() {
            // Protect against re-entrant code and attempt to do the right thing in the case that
            // we're multithreaded.
            android.view.View source = mSource;
            mSource = null;
            if (source == null) {
                android.util.Log.e(android.view.ViewRootImpl.TAG, "Accessibility content change has no source");
                return;
            }
            // The accessibility may be turned off while we were waiting so check again.
            if (android.view.accessibility.AccessibilityManager.getInstance(mContext).isEnabled()) {
                mLastEventTimeMillis = android.os.SystemClock.uptimeMillis();
                android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain();
                event.setEventType(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
                event.setContentChangeTypes(mChangeTypes);
                if (android.view.accessibility.AccessibilityEvent.DEBUG_ORIGIN)
                    event.originStackTrace = mOrigin;

                source.sendAccessibilityEventUnchecked(event);
            } else {
                mLastEventTimeMillis = 0;
            }
            // In any case reset to initial state.
            source.resetSubtreeAccessibilityStateChanged();
            mChangeTypes = 0;
            if (android.view.accessibility.AccessibilityEvent.DEBUG_ORIGIN)
                mOrigin = null;

        }

        public void runOrPost(android.view.View source, int changeType) {
            if (mHandler.getLooper() != android.os.Looper.myLooper()) {
                android.view.ViewRootImpl.CalledFromWrongThreadException e = new android.view.ViewRootImpl.CalledFromWrongThreadException("Only the " + "original thread that created a view hierarchy can touch its views.");
                // TODO: Throw the exception
                android.util.Log.e(android.view.ViewRootImpl.TAG, "Accessibility content change on non-UI thread. Future Android " + "versions will throw an exception.", e);
                // Attempt to recover. This code does not eliminate the thread safety issue, but
                // it should force any issues to happen near the above log.
                mHandler.removeCallbacks(this);
                if (mSource != null) {
                    // Dispatch whatever was pending. It's still possible that the runnable started
                    // just before we removed the callbacks, and bad things will happen, but at
                    // least they should happen very close to the logged error.
                    run();
                }
            }
            if (mSource != null) {
                // If there is no common predecessor, then mSource points to
                // a removed view, hence in this case always prefer the source.
                android.view.View predecessor = getCommonPredecessor(mSource, source);
                if (predecessor != null) {
                    predecessor = predecessor.getSelfOrParentImportantForA11y();
                }
                mSource = (predecessor != null) ? predecessor : source;
                mChangeTypes |= changeType;
                return;
            }
            mSource = source;
            mChangeTypes = changeType;
            if (android.view.accessibility.AccessibilityEvent.DEBUG_ORIGIN) {
                mOrigin = java.lang.Thread.currentThread().getStackTrace();
            }
            final long timeSinceLastMillis = android.os.SystemClock.uptimeMillis() - mLastEventTimeMillis;
            final long minEventIntevalMillis = android.view.ViewConfiguration.getSendRecurringAccessibilityEventsInterval();
            if (timeSinceLastMillis >= minEventIntevalMillis) {
                removeCallbacksAndRun();
            } else {
                mHandler.postDelayed(this, minEventIntevalMillis - timeSinceLastMillis);
            }
        }

        public void removeCallbacksAndRun() {
            mHandler.removeCallbacks(this);
            run();
        }
    }

    private static class UnhandledKeyManager {
        // This is used to ensure that unhandled events are only dispatched once. We attempt
        // to dispatch more than once in order to achieve a certain order. Specifically, if we
        // are in an Activity or Dialog (and have a Window.Callback), the unhandled events should
        // be dispatched after the view hierarchy, but before the Callback. However, if we aren't
        // in an activity, we still want unhandled keys to be dispatched.
        private boolean mDispatched = true;

        // Keeps track of which Views have unhandled key focus for which keys. This doesn't
        // include modifiers.
        private final android.util.SparseArray<java.lang.ref.WeakReference<android.view.View>> mCapturedKeys = new android.util.SparseArray();

        // The current receiver. This value is transient and used between the pre-dispatch and
        // pre-view phase to ensure that other input-stages don't interfere with tracking.
        private java.lang.ref.WeakReference<android.view.View> mCurrentReceiver = null;

        boolean dispatch(android.view.View root, android.view.KeyEvent event) {
            if (mDispatched) {
                return false;
            }
            android.view.View consumer;
            try {
                android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "UnhandledKeyEvent dispatch");
                mDispatched = true;
                consumer = root.dispatchUnhandledKeyEvent(event);
                // If an unhandled listener handles one, then keep track of it so that the
                // consuming view is first to receive its repeats and release as well.
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    int keycode = event.getKeyCode();
                    if ((consumer != null) && (!android.view.KeyEvent.isModifierKey(keycode))) {
                        mCapturedKeys.put(keycode, new java.lang.ref.WeakReference(consumer));
                    }
                }
            } finally {
                android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
            }
            return consumer != null;
        }

        /**
         * Called before the event gets dispatched to anything
         */
        void preDispatch(android.view.KeyEvent event) {
            // Always clean-up 'up' events since it's possible for earlier dispatch stages to
            // consume them without consuming the corresponding 'down' event.
            mCurrentReceiver = null;
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                int idx = mCapturedKeys.indexOfKey(event.getKeyCode());
                if (idx >= 0) {
                    mCurrentReceiver = mCapturedKeys.valueAt(idx);
                    mCapturedKeys.removeAt(idx);
                }
            }
        }

        /**
         * Called before the event gets dispatched to the view hierarchy
         *
         * @return {@code true} if an unhandled handler has focus and consumed the event
         */
        boolean preViewDispatch(android.view.KeyEvent event) {
            mDispatched = false;
            if (mCurrentReceiver == null) {
                mCurrentReceiver = mCapturedKeys.get(event.getKeyCode());
            }
            if (mCurrentReceiver != null) {
                android.view.View target = mCurrentReceiver.get();
                if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                    mCurrentReceiver = null;
                }
                if ((target != null) && target.isAttachedToWindow()) {
                    target.onUnhandledKeyEvent(event);
                }
                // consume anyways so that we don't feed uncaptured key events to other views
                return true;
            }
            return false;
        }
    }
}

