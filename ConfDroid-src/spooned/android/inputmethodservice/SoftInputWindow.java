/**
 * Copyright (C) 2007-2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.inputmethodservice;


/**
 * A SoftInputWindow is a Dialog that is intended to be used for a top-level input
 * method window.  It will be displayed along the edge of the screen, moving
 * the application user interface away from it so that the focused item is
 * always visible.
 *
 * @unknown 
 */
public class SoftInputWindow extends android.app.Dialog {
    final java.lang.String mName;

    final android.inputmethodservice.SoftInputWindow.Callback mCallback;

    final android.view.KeyEvent.Callback mKeyEventCallback;

    final android.view.KeyEvent.DispatcherState mDispatcherState;

    final int mWindowType;

    final int mGravity;

    final boolean mTakesFocus;

    private final android.graphics.Rect mBounds = new android.graphics.Rect();

    public interface Callback {
        public void onBackPressed();
    }

    public void setToken(android.os.IBinder token) {
        android.view.WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.token = token;
        getWindow().setAttributes(lp);
    }

    /**
     * Create a SoftInputWindow that uses a custom style.
     *
     * @param context
     * 		The Context in which the DockWindow should run. In
     * 		particular, it uses the window manager and theme from this context
     * 		to present its UI.
     * @param theme
     * 		A style resource describing the theme to use for the window.
     * 		See <a href="{@docRoot }reference/available-resources.html#stylesandthemes">Style
     * 		and Theme Resources</a> for more information about defining and
     * 		using styles. This theme is applied on top of the current theme in
     * 		<var>context</var>. If 0, the default dialog theme will be used.
     */
    public SoftInputWindow(android.content.Context context, java.lang.String name, int theme, android.inputmethodservice.SoftInputWindow.Callback callback, android.view.KeyEvent.Callback keyEventCallback, android.view.KeyEvent.DispatcherState dispatcherState, int windowType, int gravity, boolean takesFocus) {
        super(context, theme);
        mName = name;
        mCallback = callback;
        mKeyEventCallback = keyEventCallback;
        mDispatcherState = dispatcherState;
        mWindowType = windowType;
        mGravity = gravity;
        mTakesFocus = takesFocus;
        initDockWindow();
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mDispatcherState.reset();
    }

    @java.lang.Override
    public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
        getWindow().getDecorView().getHitRect(mBounds);
        if (ev.isWithinBoundsNoHistory(mBounds.left, mBounds.top, mBounds.right - 1, mBounds.bottom - 1)) {
            return super.dispatchTouchEvent(ev);
        } else {
            android.view.MotionEvent temp = ev.clampNoHistory(mBounds.left, mBounds.top, mBounds.right - 1, mBounds.bottom - 1);
            boolean handled = super.dispatchTouchEvent(temp);
            temp.recycle();
            return handled;
        }
    }

    /**
     * Set which boundary of the screen the DockWindow sticks to.
     *
     * @param gravity
     * 		The boundary of the screen to stick. See {#link
     * 		android.view.Gravity.LEFT}, {#link android.view.Gravity.TOP},
     * 		{#link android.view.Gravity.BOTTOM}, {#link
     * 		android.view.Gravity.RIGHT}.
     */
    public void setGravity(int gravity) {
        android.view.WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = gravity;
        updateWidthHeight(lp);
        getWindow().setAttributes(lp);
    }

    public int getGravity() {
        return getWindow().getAttributes().gravity;
    }

    private void updateWidthHeight(android.view.WindowManager.LayoutParams lp) {
        if ((lp.gravity == android.view.Gravity.TOP) || (lp.gravity == android.view.Gravity.BOTTOM)) {
            lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        }
    }

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if ((mKeyEventCallback != null) && mKeyEventCallback.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
        if ((mKeyEventCallback != null) && mKeyEventCallback.onKeyLongPress(keyCode, event)) {
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if ((mKeyEventCallback != null) && mKeyEventCallback.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyMultiple(int keyCode, int count, android.view.KeyEvent event) {
        if ((mKeyEventCallback != null) && mKeyEventCallback.onKeyMultiple(keyCode, count, event)) {
            return true;
        }
        return super.onKeyMultiple(keyCode, count, event);
    }

    public void onBackPressed() {
        if (mCallback != null) {
            mCallback.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void initDockWindow() {
        android.view.WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.type = mWindowType;
        lp.setTitle(mName);
        lp.gravity = mGravity;
        updateWidthHeight(lp);
        getWindow().setAttributes(lp);
        int windowSetFlags = android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int windowModFlags = (android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) | android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        if (!mTakesFocus) {
            windowSetFlags |= android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            windowSetFlags |= android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            windowModFlags |= android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }
        getWindow().setFlags(windowSetFlags, windowModFlags);
    }
}

