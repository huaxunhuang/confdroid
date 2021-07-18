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
package android.support.v7.view;


/**
 * A simple decorator stub for Window.Callback that passes through any calls
 * to the wrapped instance as a base implementation. Call super.foo() to call into
 * the wrapped callback for any subclasses.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class WindowCallbackWrapper implements android.view.Window.Callback {
    final android.view.Window.Callback mWrapped;

    public WindowCallbackWrapper(android.view.Window.Callback wrapped) {
        if (wrapped == null) {
            throw new java.lang.IllegalArgumentException("Window callback may not be null");
        }
        mWrapped = wrapped;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        return mWrapped.dispatchKeyEvent(event);
    }

    @java.lang.Override
    public boolean dispatchKeyShortcutEvent(android.view.KeyEvent event) {
        return mWrapped.dispatchKeyShortcutEvent(event);
    }

    @java.lang.Override
    public boolean dispatchTouchEvent(android.view.MotionEvent event) {
        return mWrapped.dispatchTouchEvent(event);
    }

    @java.lang.Override
    public boolean dispatchTrackballEvent(android.view.MotionEvent event) {
        return mWrapped.dispatchTrackballEvent(event);
    }

    @java.lang.Override
    public boolean dispatchGenericMotionEvent(android.view.MotionEvent event) {
        return mWrapped.dispatchGenericMotionEvent(event);
    }

    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        return mWrapped.dispatchPopulateAccessibilityEvent(event);
    }

    @java.lang.Override
    public android.view.View onCreatePanelView(int featureId) {
        return mWrapped.onCreatePanelView(featureId);
    }

    @java.lang.Override
    public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
        return mWrapped.onCreatePanelMenu(featureId, menu);
    }

    @java.lang.Override
    public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
        return mWrapped.onPreparePanel(featureId, view, menu);
    }

    @java.lang.Override
    public boolean onMenuOpened(int featureId, android.view.Menu menu) {
        return mWrapped.onMenuOpened(featureId, menu);
    }

    @java.lang.Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        return mWrapped.onMenuItemSelected(featureId, item);
    }

    @java.lang.Override
    public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams attrs) {
        mWrapped.onWindowAttributesChanged(attrs);
    }

    @java.lang.Override
    public void onContentChanged() {
        mWrapped.onContentChanged();
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mWrapped.onWindowFocusChanged(hasFocus);
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        mWrapped.onAttachedToWindow();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        mWrapped.onDetachedFromWindow();
    }

    @java.lang.Override
    public void onPanelClosed(int featureId, android.view.Menu menu) {
        mWrapped.onPanelClosed(featureId, menu);
    }

    @java.lang.Override
    public boolean onSearchRequested(android.view.SearchEvent searchEvent) {
        return mWrapped.onSearchRequested(searchEvent);
    }

    @java.lang.Override
    public boolean onSearchRequested() {
        return mWrapped.onSearchRequested();
    }

    @java.lang.Override
    public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
        return mWrapped.onWindowStartingActionMode(callback);
    }

    @java.lang.Override
    public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int type) {
        return mWrapped.onWindowStartingActionMode(callback, type);
    }

    @java.lang.Override
    public void onActionModeStarted(android.view.ActionMode mode) {
        mWrapped.onActionModeStarted(mode);
    }

    @java.lang.Override
    public void onActionModeFinished(android.view.ActionMode mode) {
        mWrapped.onActionModeFinished(mode);
    }

    @java.lang.Override
    public void onProvideKeyboardShortcuts(java.util.List<android.view.KeyboardShortcutGroup> data, android.view.Menu menu, int deviceId) {
        mWrapped.onProvideKeyboardShortcuts(data, menu, deviceId);
    }
}

