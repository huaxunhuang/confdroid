/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * Responsible for IME focus handling inside {@link ViewRootImpl}.
 *
 * @unknown 
 */
public final class ImeFocusController {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "ImeFocusController";

    private final android.view.ViewRootImpl mViewRootImpl;

    private boolean mHasImeFocus = false;

    private android.view.View mServedView;

    private android.view.View mNextServedView;

    private android.view.ImeFocusController.InputMethodManagerDelegate mDelegate;

    @android.annotation.UiThread
    ImeFocusController(@android.annotation.NonNull
    android.view.ViewRootImpl viewRootImpl) {
        mViewRootImpl = viewRootImpl;
    }

    @android.annotation.NonNull
    private android.view.ImeFocusController.InputMethodManagerDelegate getImmDelegate() {
        android.view.ImeFocusController.InputMethodManagerDelegate delegate = mDelegate;
        if (delegate != null) {
            return delegate;
        }
        delegate = mViewRootImpl.mContext.getSystemService(android.view.inputmethod.InputMethodManager.class).getDelegate();
        mDelegate = delegate;
        return delegate;
    }

    /**
     * Called when the view root is moved to a different display.
     */
    @android.annotation.UiThread
    void onMovedToDisplay() {
        // InputMethodManager managed its instances for different displays. So if the associated
        // display is changed, the delegate also needs to be refreshed (by getImmDelegate).
        // See the comment in {@link android.app.SystemServiceRegistry} for InputMethodManager
        // and {@link android.view.inputmethod.InputMethodManager#forContext}.
        mDelegate = null;
    }

    @android.annotation.UiThread
    void onTraversal(boolean hasWindowFocus, android.view.WindowManager.LayoutParams windowAttribute) {
        final boolean hasImeFocus = /* force */
        updateImeFocusable(windowAttribute, false);
        if ((!hasWindowFocus) || android.view.ImeFocusController.isInLocalFocusMode(windowAttribute)) {
            return;
        }
        if (hasImeFocus == mHasImeFocus) {
            return;
        }
        mHasImeFocus = hasImeFocus;
        if (mHasImeFocus) {
            /* hasWindowFocus */
            onPreWindowFocus(true, windowAttribute);
            /* hasWindowFocus */
            onPostWindowFocus(mViewRootImpl.mView.findFocus(), true, windowAttribute);
        }
    }

    @android.annotation.UiThread
    void onPreWindowFocus(boolean hasWindowFocus, android.view.WindowManager.LayoutParams windowAttribute) {
        if ((!mHasImeFocus) || android.view.ImeFocusController.isInLocalFocusMode(windowAttribute)) {
            return;
        }
        if (hasWindowFocus) {
            getImmDelegate().setCurrentRootView(mViewRootImpl);
        }
    }

    @android.annotation.UiThread
    boolean updateImeFocusable(android.view.WindowManager.LayoutParams windowAttribute, boolean force) {
        final boolean hasImeFocus = android.view.WindowManager.LayoutParams.mayUseInputMethod(windowAttribute.flags);
        if (force) {
            mHasImeFocus = hasImeFocus;
        }
        return hasImeFocus;
    }

    @android.annotation.UiThread
    void onPostWindowFocus(android.view.View focusedView, boolean hasWindowFocus, android.view.WindowManager.LayoutParams windowAttribute) {
        if (((!hasWindowFocus) || (!mHasImeFocus)) || android.view.ImeFocusController.isInLocalFocusMode(windowAttribute)) {
            return;
        }
        if (android.view.ImeFocusController.DEBUG) {
            android.util.Log.v(android.view.ImeFocusController.TAG, (("onWindowFocus: " + focusedView) + " softInputMode=") + com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(windowAttribute.softInputMode));
        }
        boolean forceFocus = false;
        final android.view.ImeFocusController.InputMethodManagerDelegate immDelegate = getImmDelegate();
        if (/* reset */
        immDelegate.isRestartOnNextWindowFocus(true)) {
            if (android.view.ImeFocusController.DEBUG)
                android.util.Log.v(android.view.ImeFocusController.TAG, "Restarting due to isRestartOnNextWindowFocus as true");

            forceFocus = true;
        }
        // Update mNextServedView when focusedView changed.
        final android.view.View viewForWindowFocus = (focusedView != null) ? focusedView : mViewRootImpl.mView;
        onViewFocusChanged(viewForWindowFocus, true);
        // Starting new input when the next focused view is same as served view but the currently
        // active connection (if any) is not associated with it.
        final boolean nextFocusIsServedView = mServedView == viewForWindowFocus;
        if (nextFocusIsServedView && (!immDelegate.hasActiveConnection(viewForWindowFocus))) {
            forceFocus = true;
        }
        immDelegate.startInputAsyncOnWindowFocusGain(viewForWindowFocus, windowAttribute.softInputMode, windowAttribute.flags, forceFocus);
    }

    public boolean checkFocus(boolean forceNewFocus, boolean startInput) {
        final android.view.ImeFocusController.InputMethodManagerDelegate immDelegate = getImmDelegate();
        if ((!immDelegate.isCurrentRootView(mViewRootImpl)) || ((mServedView == mNextServedView) && (!forceNewFocus))) {
            return false;
        }
        if (android.view.ImeFocusController.DEBUG)
            android.util.Log.v(android.view.ImeFocusController.TAG, (((((("checkFocus: view=" + mServedView) + " next=") + mNextServedView) + " force=") + forceNewFocus) + " package=") + (mServedView != null ? mServedView.getContext().getPackageName() : "<none>"));

        // Close the connection when no next served view coming.
        if (mNextServedView == null) {
            immDelegate.finishInput();
            immDelegate.closeCurrentIme();
            return false;
        }
        mServedView = mNextServedView;
        immDelegate.finishComposingText();
        if (startInput) {
            /* focusedView */
            /* startInputFlags */
            /* softInputMode */
            /* windowFlags */
            immDelegate.startInput(StartInputReason.CHECK_FOCUS, null, 0, 0, 0);
        }
        return true;
    }

    @android.annotation.UiThread
    void onViewFocusChanged(android.view.View view, boolean hasFocus) {
        if ((view == null) || view.isTemporarilyDetached()) {
            return;
        }
        if (!getImmDelegate().isCurrentRootView(view.getViewRootImpl())) {
            return;
        }
        if ((!view.hasImeFocus()) || (!view.hasWindowFocus())) {
            return;
        }
        if (android.view.ImeFocusController.DEBUG)
            android.util.Log.d(android.view.ImeFocusController.TAG, (("onViewFocusChanged, view=" + view) + ", mServedView=") + mServedView);

        // We don't need to track the next served view when the view lost focus here because:
        // 1) The current view focus may be cleared temporary when in touch mode, closing input
        // at this moment isn't the right way.
        // 2) We only care about the served view change when it focused, since changing input
        // connection when the focus target changed is reasonable.
        // 3) Setting the next served view as null when no more served view should be handled in
        // other special events (e.g. view detached from window or the window dismissed).
        if (hasFocus) {
            mNextServedView = view;
        }
        mViewRootImpl.dispatchCheckFocus();
    }

    @android.annotation.UiThread
    void onViewDetachedFromWindow(android.view.View view) {
        if (!getImmDelegate().isCurrentRootView(view.getViewRootImpl())) {
            return;
        }
        if (mServedView == view) {
            mNextServedView = null;
            mViewRootImpl.dispatchCheckFocus();
        }
    }

    @android.annotation.UiThread
    void onWindowDismissed() {
        final android.view.ImeFocusController.InputMethodManagerDelegate immDelegate = getImmDelegate();
        if (!immDelegate.isCurrentRootView(mViewRootImpl)) {
            return;
        }
        if (mServedView != null) {
            immDelegate.finishInput();
        }
        immDelegate.setCurrentRootView(null);
        mHasImeFocus = false;
    }

    /**
     *
     *
     * @param windowAttribute
     * 		{@link WindowManager.LayoutParams} to be checked.
     * @return Whether the window is in local focus mode or not.
     */
    @android.annotation.AnyThread
    private static boolean isInLocalFocusMode(android.view.WindowManager.LayoutParams windowAttribute) {
        return (windowAttribute.flags & android.view.WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE) != 0;
    }

    int onProcessImeInputStage(java.lang.Object token, android.view.InputEvent event, android.view.WindowManager.LayoutParams windowAttribute, android.view.inputmethod.InputMethodManager.FinishedInputEventCallback callback) {
        if ((!mHasImeFocus) || android.view.ImeFocusController.isInLocalFocusMode(windowAttribute)) {
            return android.view.inputmethod.InputMethodManager.DISPATCH_NOT_HANDLED;
        }
        final android.view.inputmethod.InputMethodManager imm = mViewRootImpl.mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
        if (imm == null) {
            return android.view.inputmethod.InputMethodManager.DISPATCH_NOT_HANDLED;
        }
        return imm.dispatchInputEvent(event, token, callback, mViewRootImpl.mHandler);
    }

    /**
     * A delegate implementing some basic {@link InputMethodManager} APIs.
     *
     * @unknown 
     */
    public interface InputMethodManagerDelegate {
        boolean startInput(@com.android.internal.inputmethod.StartInputReason
        int startInputReason, android.view.View focusedView, @com.android.internal.inputmethod.StartInputFlags
        int startInputFlags, @android.view.WindowManager.LayoutParams.SoftInputModeFlags
        int softInputMode, int windowFlags);

        void startInputAsyncOnWindowFocusGain(android.view.View rootView, @android.view.WindowManager.LayoutParams.SoftInputModeFlags
        int softInputMode, int windowFlags, boolean forceNewFocus);

        void finishInput();

        void closeCurrentIme();

        void finishComposingText();

        void setCurrentRootView(android.view.ViewRootImpl rootView);

        boolean isCurrentRootView(android.view.ViewRootImpl rootView);

        boolean isRestartOnNextWindowFocus(boolean reset);

        boolean hasActiveConnection(android.view.View view);
    }

    public android.view.View getServedView() {
        return mServedView;
    }

    public android.view.View getNextServedView() {
        return mNextServedView;
    }

    public void setServedView(android.view.View view) {
        mServedView = view;
    }

    public void setNextServedView(android.view.View view) {
        mNextServedView = view;
    }

    /**
     * Indicates whether the view's window has IME focused.
     */
    @android.annotation.UiThread
    boolean hasImeFocus() {
        return mHasImeFocus;
    }
}

