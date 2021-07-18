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
package android.view;


/**
 * An empty implementation of {@link Window.Callback} that always returns null/false.
 */
public class WindowCallback implements android.view.Window.Callback {
    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean dispatchKeyShortcutEvent(android.view.KeyEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean dispatchTouchEvent(android.view.MotionEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean dispatchTrackballEvent(android.view.MotionEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean dispatchGenericMotionEvent(android.view.MotionEvent event) {
        return false;
    }

    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        return false;
    }

    @java.lang.Override
    public android.view.View onCreatePanelView(int featureId) {
        return null;
    }

    @java.lang.Override
    public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
        return false;
    }

    @java.lang.Override
    public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
        return false;
    }

    @java.lang.Override
    public boolean onMenuOpened(int featureId, android.view.Menu menu) {
        return false;
    }

    @java.lang.Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        return false;
    }

    @java.lang.Override
    public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams attrs) {
    }

    @java.lang.Override
    public void onContentChanged() {
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }

    @java.lang.Override
    public void onAttachedToWindow() {
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
    }

    @java.lang.Override
    public void onPanelClosed(int featureId, android.view.Menu menu) {
    }

    @java.lang.Override
    public boolean onSearchRequested(android.view.SearchEvent searchEvent) {
        return onSearchRequested();
    }

    @java.lang.Override
    public boolean onSearchRequested() {
        return false;
    }

    @java.lang.Override
    public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
        return null;
    }

    @java.lang.Override
    public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int type) {
        return null;
    }

    @java.lang.Override
    public void onActionModeStarted(android.view.ActionMode mode) {
    }

    @java.lang.Override
    public void onActionModeFinished(android.view.ActionMode mode) {
    }
}

