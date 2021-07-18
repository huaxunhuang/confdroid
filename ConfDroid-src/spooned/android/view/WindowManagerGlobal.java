/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Provides low-level communication with the system window manager for
 * operations that are not associated with any particular context.
 *
 * This class is only used internally to implement global functions where
 * the caller already knows the display and relevant compatibility information
 * for the operation.  For most purposes, you should use {@link WindowManager} instead
 * since it is bound to a context.
 *
 * @see WindowManagerImpl
 * @unknown 
 */
public final class WindowManagerGlobal {
    private static final java.lang.String TAG = "WindowManager";

    /**
     * The user is navigating with keys (not the touch screen), so
     * navigational focus should be shown.
     */
    public static final int RELAYOUT_RES_IN_TOUCH_MODE = 0x1;

    /**
     * This is the first time the window is being drawn,
     * so the client must call drawingFinished() when done
     */
    public static final int RELAYOUT_RES_FIRST_TIME = 0x2;

    /**
     * The window manager has changed the surface from the last call.
     */
    public static final int RELAYOUT_RES_SURFACE_CHANGED = 0x4;

    /**
     * The window is being resized by dragging on the docked divider. The client should render
     * at (0, 0) and extend its background to the background frame passed into
     * {@link IWindow#resized}.
     */
    public static final int RELAYOUT_RES_DRAG_RESIZING_DOCKED = 0x8;

    /**
     * The window is being resized by dragging one of the window corners,
     * in this case the surface would be fullscreen-sized. The client should
     * render to the actual frame location (instead of (0,curScrollY)).
     */
    public static final int RELAYOUT_RES_DRAG_RESIZING_FREEFORM = 0x10;

    /**
     * The window manager has changed the size of the surface from the last call.
     */
    public static final int RELAYOUT_RES_SURFACE_RESIZED = 0x20;

    /**
     * In multi-window we force show the system bars. Because we don't want that the surface size
     * changes in this mode, we instead have a flag whether the system bar sizes should always be
     * consumed, so the app is treated like there is no virtual system bars at all.
     */
    public static final int RELAYOUT_RES_CONSUME_ALWAYS_SYSTEM_BARS = 0x40;

    /**
     * Flag for relayout: the client will be later giving
     * internal insets; as a result, the window will not impact other window
     * layouts until the insets are given.
     */
    public static final int RELAYOUT_INSETS_PENDING = 0x1;

    /**
     * Flag for relayout: the client may be currently using the current surface,
     * so if it is to be destroyed as a part of the relayout the destroy must
     * be deferred until later.  The client will call performDeferredDestroy()
     * when it is okay.
     */
    public static final int RELAYOUT_DEFER_SURFACE_DESTROY = 0x2;

    public static final int ADD_FLAG_APP_VISIBLE = 0x2;

    public static final int ADD_FLAG_IN_TOUCH_MODE = android.view.WindowManagerGlobal.RELAYOUT_RES_IN_TOUCH_MODE;

    /**
     * Like {@link #RELAYOUT_RES_CONSUME_ALWAYS_SYSTEM_BARS}, but as a "hint" when adding the
     * window.
     */
    public static final int ADD_FLAG_ALWAYS_CONSUME_SYSTEM_BARS = 0x4;

    public static final int ADD_OKAY = 0;

    public static final int ADD_BAD_APP_TOKEN = -1;

    public static final int ADD_BAD_SUBWINDOW_TOKEN = -2;

    public static final int ADD_NOT_APP_TOKEN = -3;

    public static final int ADD_APP_EXITING = -4;

    public static final int ADD_DUPLICATE_ADD = -5;

    public static final int ADD_STARTING_NOT_NEEDED = -6;

    public static final int ADD_MULTIPLE_SINGLETON = -7;

    public static final int ADD_PERMISSION_DENIED = -8;

    public static final int ADD_INVALID_DISPLAY = -9;

    public static final int ADD_INVALID_TYPE = -10;

    @android.annotation.UnsupportedAppUsage
    private static android.view.WindowManagerGlobal sDefaultWindowManager;

    @android.annotation.UnsupportedAppUsage
    private static android.view.IWindowManager sWindowManagerService;

    @android.annotation.UnsupportedAppUsage
    private static android.view.IWindowSession sWindowSession;

    @android.annotation.UnsupportedAppUsage
    private final java.lang.Object mLock = new java.lang.Object();

    @android.annotation.UnsupportedAppUsage
    private final java.util.ArrayList<android.view.View> mViews = new java.util.ArrayList<android.view.View>();

    @android.annotation.UnsupportedAppUsage
    private final java.util.ArrayList<android.view.ViewRootImpl> mRoots = new java.util.ArrayList<android.view.ViewRootImpl>();

    @android.annotation.UnsupportedAppUsage
    private final java.util.ArrayList<android.view.WindowManager.LayoutParams> mParams = new java.util.ArrayList<android.view.WindowManager.LayoutParams>();

    private final android.util.ArraySet<android.view.View> mDyingViews = new android.util.ArraySet<android.view.View>();

    private java.lang.Runnable mSystemPropertyUpdater;

    private WindowManagerGlobal() {
    }

    @android.annotation.UnsupportedAppUsage
    public static void initialize() {
        android.view.WindowManagerGlobal.getWindowManagerService();
    }

    @android.annotation.UnsupportedAppUsage
    public static android.view.WindowManagerGlobal getInstance() {
        synchronized(android.view.WindowManagerGlobal.class) {
            if (android.view.WindowManagerGlobal.sDefaultWindowManager == null) {
                android.view.WindowManagerGlobal.sDefaultWindowManager = new android.view.WindowManagerGlobal();
            }
            return android.view.WindowManagerGlobal.sDefaultWindowManager;
        }
    }

    @android.annotation.UnsupportedAppUsage
    public static android.view.IWindowManager getWindowManagerService() {
        synchronized(android.view.WindowManagerGlobal.class) {
            if (android.view.WindowManagerGlobal.sWindowManagerService == null) {
                android.view.WindowManagerGlobal.sWindowManagerService = IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window"));
                try {
                    if (android.view.WindowManagerGlobal.sWindowManagerService != null) {
                        android.animation.ValueAnimator.setDurationScale(android.view.WindowManagerGlobal.sWindowManagerService.getCurrentAnimatorScale());
                    }
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return android.view.WindowManagerGlobal.sWindowManagerService;
        }
    }

    @android.annotation.UnsupportedAppUsage
    public static android.view.IWindowSession getWindowSession() {
        synchronized(android.view.WindowManagerGlobal.class) {
            if (android.view.WindowManagerGlobal.sWindowSession == null) {
                try {
                    // Emulate the legacy behavior.  The global instance of InputMethodManager
                    // was instantiated here.
                    // TODO(b/116157766): Remove this hack after cleaning up @UnsupportedAppUsage
                    android.view.inputmethod.InputMethodManager.ensureDefaultInstanceForDefaultDisplayIfNecessary();
                    android.view.IWindowManager windowManager = android.view.WindowManagerGlobal.getWindowManagerService();
                    android.view.WindowManagerGlobal.sWindowSession = windowManager.openSession(new android.view.IWindowSessionCallback.Stub() {
                        @java.lang.Override
                        public void onAnimatorScaleChanged(float scale) {
                            android.animation.ValueAnimator.setDurationScale(scale);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return android.view.WindowManagerGlobal.sWindowSession;
        }
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public static android.view.IWindowSession peekWindowSession() {
        synchronized(android.view.WindowManagerGlobal.class) {
            return android.view.WindowManagerGlobal.sWindowSession;
        }
    }

    @android.annotation.UnsupportedAppUsage
    public java.lang.String[] getViewRootNames() {
        synchronized(mLock) {
            final int numRoots = mRoots.size();
            java.lang.String[] mViewRoots = new java.lang.String[numRoots];
            for (int i = 0; i < numRoots; ++i) {
                mViewRoots[i] = android.view.WindowManagerGlobal.getWindowName(mRoots.get(i));
            }
            return mViewRoots;
        }
    }

    @android.annotation.UnsupportedAppUsage
    public java.util.ArrayList<android.view.ViewRootImpl> getRootViews(android.os.IBinder token) {
        java.util.ArrayList<android.view.ViewRootImpl> views = new java.util.ArrayList<>();
        synchronized(mLock) {
            final int numRoots = mRoots.size();
            for (int i = 0; i < numRoots; ++i) {
                android.view.WindowManager.LayoutParams params = mParams.get(i);
                if (params.token == null) {
                    continue;
                }
                if (params.token != token) {
                    boolean isChild = false;
                    if ((params.type >= android.view.WindowManager.LayoutParams.FIRST_SUB_WINDOW) && (params.type <= android.view.WindowManager.LayoutParams.LAST_SUB_WINDOW)) {
                        for (int j = 0; j < numRoots; ++j) {
                            android.view.View viewj = mViews.get(j);
                            android.view.WindowManager.LayoutParams paramsj = mParams.get(j);
                            if ((params.token == viewj.getWindowToken()) && (paramsj.token == token)) {
                                isChild = true;
                                break;
                            }
                        }
                    }
                    if (!isChild) {
                        continue;
                    }
                }
                views.add(mRoots.get(i));
            }
        }
        return views;
    }

    /**
     *
     *
     * @return the list of all views attached to the global window manager
     */
    @android.annotation.NonNull
    public java.util.ArrayList<android.view.View> getWindowViews() {
        synchronized(mLock) {
            return new java.util.ArrayList<>(mViews);
        }
    }

    public android.view.View getWindowView(android.os.IBinder windowToken) {
        synchronized(mLock) {
            final int numViews = mViews.size();
            for (int i = 0; i < numViews; ++i) {
                final android.view.View view = mViews.get(i);
                if (view.getWindowToken() == windowToken) {
                    return view;
                }
            }
        }
        return null;
    }

    @android.annotation.UnsupportedAppUsage
    public android.view.View getRootView(java.lang.String name) {
        synchronized(mLock) {
            for (int i = mRoots.size() - 1; i >= 0; --i) {
                final android.view.ViewRootImpl root = mRoots.get(i);
                if (name.equals(android.view.WindowManagerGlobal.getWindowName(root)))
                    return root.getView();

            }
        }
        return null;
    }

    public void addView(android.view.View view, android.view.ViewGroup.LayoutParams params, android.view.Display display, android.view.Window parentWindow) {
        if (view == null) {
            throw new java.lang.IllegalArgumentException("view must not be null");
        }
        if (display == null) {
            throw new java.lang.IllegalArgumentException("display must not be null");
        }
        if (!(params instanceof android.view.WindowManager.LayoutParams)) {
            throw new java.lang.IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
        final android.view.WindowManager.LayoutParams wparams = ((android.view.WindowManager.LayoutParams) (params));
        if (parentWindow != null) {
            parentWindow.adjustLayoutParamsForSubWindow(wparams);
        } else {
            // If there's no parent, then hardware acceleration for this view is
            // set from the application's hardware acceleration setting.
            final android.content.Context context = view.getContext();
            if ((context != null) && ((context.getApplicationInfo().flags & android.content.pm.ApplicationInfo.FLAG_HARDWARE_ACCELERATED) != 0)) {
                wparams.flags |= android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
            }
        }
        android.view.ViewRootImpl root;
        android.view.View panelParentView = null;
        synchronized(mLock) {
            // Start watching for system property changes.
            if (mSystemPropertyUpdater == null) {
                mSystemPropertyUpdater = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        synchronized(mLock) {
                            for (int i = mRoots.size() - 1; i >= 0; --i) {
                                mRoots.get(i).loadSystemProperties();
                            }
                        }
                    }
                };
                android.os.SystemProperties.addChangeCallback(mSystemPropertyUpdater);
            }
            int index = findViewLocked(view, false);
            if (index >= 0) {
                if (mDyingViews.contains(view)) {
                    // Don't wait for MSG_DIE to make it's way through root's queue.
                    mRoots.get(index).doDie();
                } else {
                    throw new java.lang.IllegalStateException(("View " + view) + " has already been added to the window manager.");
                }
                // The previous removeView() had not completed executing. Now it has.
            }
            // If this is a panel window, then find the window it is being
            // attached to for future reference.
            if ((wparams.type >= android.view.WindowManager.LayoutParams.FIRST_SUB_WINDOW) && (wparams.type <= android.view.WindowManager.LayoutParams.LAST_SUB_WINDOW)) {
                final int count = mViews.size();
                for (int i = 0; i < count; i++) {
                    if (mRoots.get(i).mWindow.asBinder() == wparams.token) {
                        panelParentView = mViews.get(i);
                    }
                }
            }
            root = new android.view.ViewRootImpl(view.getContext(), display);
            view.setLayoutParams(wparams);
            mViews.add(view);
            mRoots.add(root);
            mParams.add(wparams);
            // do this last because it fires off messages to start doing things
            try {
                root.setView(view, wparams, panelParentView);
            } catch (java.lang.RuntimeException e) {
                // BadTokenException or InvalidDisplayException, clean up.
                if (index >= 0) {
                    removeViewLocked(index, true);
                }
                throw e;
            }
        }
    }

    public void updateViewLayout(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        if (view == null) {
            throw new java.lang.IllegalArgumentException("view must not be null");
        }
        if (!(params instanceof android.view.WindowManager.LayoutParams)) {
            throw new java.lang.IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
        final android.view.WindowManager.LayoutParams wparams = ((android.view.WindowManager.LayoutParams) (params));
        view.setLayoutParams(wparams);
        synchronized(mLock) {
            int index = findViewLocked(view, true);
            android.view.ViewRootImpl root = mRoots.get(index);
            mParams.remove(index);
            mParams.add(index, wparams);
            root.setLayoutParams(wparams, false);
        }
    }

    @android.annotation.UnsupportedAppUsage
    public void removeView(android.view.View view, boolean immediate) {
        if (view == null) {
            throw new java.lang.IllegalArgumentException("view must not be null");
        }
        synchronized(mLock) {
            int index = findViewLocked(view, true);
            android.view.View curView = mRoots.get(index).getView();
            removeViewLocked(index, immediate);
            if (curView == view) {
                return;
            }
            throw new java.lang.IllegalStateException((("Calling with view " + view) + " but the ViewAncestor is attached to ") + curView);
        }
    }

    /**
     * Remove all roots with specified token.
     *
     * @param token
     * 		app or window token.
     * @param who
     * 		name of caller, used in logs.
     * @param what
     * 		type of caller, used in logs.
     */
    public void closeAll(android.os.IBinder token, java.lang.String who, java.lang.String what) {
        /* view */
        closeAllExceptView(token, null, who, what);
    }

    /**
     * Remove all roots with specified token, except maybe one view.
     *
     * @param token
     * 		app or window token.
     * @param view
     * 		view that should be should be preserved along with it's root.
     * 		Pass null if everything should be removed.
     * @param who
     * 		name of caller, used in logs.
     * @param what
     * 		type of caller, used in logs.
     */
    public void closeAllExceptView(android.os.IBinder token, android.view.View view, java.lang.String who, java.lang.String what) {
        synchronized(mLock) {
            int count = mViews.size();
            for (int i = 0; i < count; i++) {
                if (((view == null) || (mViews.get(i) != view)) && ((token == null) || (mParams.get(i).token == token))) {
                    android.view.ViewRootImpl root = mRoots.get(i);
                    if (who != null) {
                        android.view.WindowLeaked leak = new android.view.WindowLeaked(((((what + " ") + who) + " has leaked window ") + root.getView()) + " that was originally added here");
                        leak.setStackTrace(root.getLocation().getStackTrace());
                        android.util.Log.e(android.view.WindowManagerGlobal.TAG, "", leak);
                    }
                    removeViewLocked(i, false);
                }
            }
        }
    }

    private void removeViewLocked(int index, boolean immediate) {
        android.view.ViewRootImpl root = mRoots.get(index);
        android.view.View view = root.getView();
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm = view.getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
            if (imm != null) {
                imm.windowDismissed(mViews.get(index).getWindowToken());
            }
        }
        boolean deferred = root.die(immediate);
        if (view != null) {
            view.assignParent(null);
            if (deferred) {
                mDyingViews.add(view);
            }
        }
    }

    void doRemoveView(android.view.ViewRootImpl root) {
        synchronized(mLock) {
            final int index = mRoots.indexOf(root);
            if (index >= 0) {
                mRoots.remove(index);
                mParams.remove(index);
                final android.view.View view = mViews.remove(index);
                mDyingViews.remove(view);
            }
        }
        if (android.view.ThreadedRenderer.sTrimForeground && android.view.ThreadedRenderer.isAvailable()) {
            doTrimForeground();
        }
    }

    private int findViewLocked(android.view.View view, boolean required) {
        final int index = mViews.indexOf(view);
        if (required && (index < 0)) {
            throw new java.lang.IllegalArgumentException(("View=" + view) + " not attached to window manager");
        }
        return index;
    }

    public static boolean shouldDestroyEglContext(int trimLevel) {
        // On low-end gfx devices we trim when memory is moderate;
        // on high-end devices we do this when low.
        if (trimLevel >= android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            return true;
        }
        if ((trimLevel >= android.content.ComponentCallbacks2.TRIM_MEMORY_MODERATE) && (!android.app.ActivityManager.isHighEndGfx())) {
            return true;
        }
        return false;
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    public void trimMemory(int level) {
        if (android.view.ThreadedRenderer.isAvailable()) {
            if (android.view.WindowManagerGlobal.shouldDestroyEglContext(level)) {
                // Destroy all hardware surfaces and resources associated to
                // known windows
                synchronized(mLock) {
                    for (int i = mRoots.size() - 1; i >= 0; --i) {
                        mRoots.get(i).destroyHardwareResources();
                    }
                }
                // Force a full memory flush
                level = android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE;
            }
            android.view.ThreadedRenderer.trimMemory(level);
            if (android.view.ThreadedRenderer.sTrimForeground) {
                doTrimForeground();
            }
        }
    }

    public static void trimForeground() {
        if (android.view.ThreadedRenderer.sTrimForeground && android.view.ThreadedRenderer.isAvailable()) {
            android.view.WindowManagerGlobal wm = android.view.WindowManagerGlobal.getInstance();
            wm.doTrimForeground();
        }
    }

    private void doTrimForeground() {
        boolean hasVisibleWindows = false;
        synchronized(mLock) {
            for (int i = mRoots.size() - 1; i >= 0; --i) {
                final android.view.ViewRootImpl root = mRoots.get(i);
                if (((root.mView != null) && (root.getHostVisibility() == android.view.View.VISIBLE)) && (root.mAttachInfo.mThreadedRenderer != null)) {
                    hasVisibleWindows = true;
                } else {
                    root.destroyHardwareResources();
                }
            }
        }
        if (!hasVisibleWindows) {
            android.view.ThreadedRenderer.trimMemory(android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE);
        }
    }

    public void dumpGfxInfo(java.io.FileDescriptor fd, java.lang.String[] args) {
        java.io.FileOutputStream fout = new java.io.FileOutputStream(fd);
        java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(fout);
        try {
            synchronized(mLock) {
                final int count = mViews.size();
                pw.println("Profile data in ms:");
                for (int i = 0; i < count; i++) {
                    android.view.ViewRootImpl root = mRoots.get(i);
                    java.lang.String name = android.view.WindowManagerGlobal.getWindowName(root);
                    pw.printf("\n\t%s (visibility=%d)", name, root.getHostVisibility());
                    android.view.ThreadedRenderer renderer = root.getView().mAttachInfo.mThreadedRenderer;
                    if (renderer != null) {
                        renderer.dumpGfxInfo(pw, fd, args);
                    }
                }
                pw.println("\nView hierarchy:\n");
                int viewsCount = 0;
                int displayListsSize = 0;
                int[] info = new int[2];
                for (int i = 0; i < count; i++) {
                    android.view.ViewRootImpl root = mRoots.get(i);
                    root.dumpGfxInfo(info);
                    java.lang.String name = android.view.WindowManagerGlobal.getWindowName(root);
                    pw.printf("  %s\n  %d views, %.2f kB of display lists", name, info[0], info[1] / 1024.0F);
                    pw.printf("\n\n");
                    viewsCount += info[0];
                    displayListsSize += info[1];
                }
                pw.printf("\nTotal ViewRootImpl: %d\n", count);
                pw.printf("Total Views:        %d\n", viewsCount);
                pw.printf("Total DisplayList:  %.2f kB\n\n", displayListsSize / 1024.0F);
            }
        } finally {
            pw.flush();
        }
    }

    private static java.lang.String getWindowName(android.view.ViewRootImpl root) {
        return (((root.mWindowAttributes.getTitle() + "/") + root.getClass().getName()) + '@') + java.lang.Integer.toHexString(root.hashCode());
    }

    public void setStoppedState(android.os.IBinder token, boolean stopped) {
        java.util.ArrayList<android.view.ViewRootImpl> nonCurrentThreadRoots = null;
        synchronized(mLock) {
            int count = mViews.size();
            for (int i = count - 1; i >= 0; i--) {
                if ((token == null) || (mParams.get(i).token == token)) {
                    android.view.ViewRootImpl root = mRoots.get(i);
                    // Client might remove the view by "stopped" event.
                    if (root.mThread == java.lang.Thread.currentThread()) {
                        root.setWindowStopped(stopped);
                    } else {
                        if (nonCurrentThreadRoots == null) {
                            nonCurrentThreadRoots = new java.util.ArrayList<>();
                        }
                        nonCurrentThreadRoots.add(root);
                    }
                    // Recursively forward stopped state to View's attached
                    // to this Window rather than the root application token,
                    // e.g. PopupWindow's.
                    setStoppedState(root.mAttachInfo.mWindowToken, stopped);
                }
            }
        }
        // Update the stopped state synchronously to ensure the surface won't be used after server
        // side has destroyed it. This operation should be outside the lock to avoid any potential
        // paths from setWindowStopped to WindowManagerGlobal which may cause deadlocks.
        if (nonCurrentThreadRoots != null) {
            for (int i = nonCurrentThreadRoots.size() - 1; i >= 0; i--) {
                android.view.ViewRootImpl root = nonCurrentThreadRoots.get(i);
                root.mHandler.runWithScissors(() -> root.setWindowStopped(stopped), 0);
            }
        }
    }

    public void reportNewConfiguration(android.content.res.Configuration config) {
        synchronized(mLock) {
            int count = mViews.size();
            config = new android.content.res.Configuration(config);
            for (int i = 0; i < count; i++) {
                android.view.ViewRootImpl root = mRoots.get(i);
                root.requestUpdateConfiguration(config);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void changeCanvasOpacity(android.os.IBinder token, boolean opaque) {
        if (token == null) {
            return;
        }
        synchronized(mLock) {
            for (int i = mParams.size() - 1; i >= 0; --i) {
                if (mParams.get(i).token == token) {
                    mRoots.get(i).changeCanvasOpacity(opaque);
                    return;
                }
            }
        }
    }
}

