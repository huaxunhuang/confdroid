/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A custom EditText that satisfies the IME key monitoring requirements of GuidedStepFragment.
 */
public class GuidedActionEditText extends android.widget.EditText implements android.support.v17.leanback.widget.ImeKeyMonitor {
    /**
     * Workaround for b/26990627 forcing recompute the padding for the View when we turn on/off
     * the default background of EditText
     */
    static final class NoPaddingDrawable extends android.graphics.drawable.Drawable {
        @java.lang.Override
        public boolean getPadding(android.graphics.Rect padding) {
            padding.set(0, 0, 0, 0);
            return true;
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas) {
        }

        @java.lang.Override
        public void setAlpha(int alpha) {
        }

        @java.lang.Override
        public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        }

        @java.lang.Override
        public int getOpacity() {
            return android.graphics.PixelFormat.TRANSPARENT;
        }
    }

    private android.support.v17.leanback.widget.ImeKeyMonitor.ImeKeyListener mKeyListener;

    private final android.graphics.drawable.Drawable mSavedBackground;

    private final android.graphics.drawable.Drawable mNoPaddingDrawable;

    public GuidedActionEditText(android.content.Context ctx) {
        this(ctx, null);
    }

    public GuidedActionEditText(android.content.Context ctx, android.util.AttributeSet attrs) {
        this(ctx, attrs, android.R.attr.editTextStyle);
    }

    public GuidedActionEditText(android.content.Context ctx, android.util.AttributeSet attrs, int defStyleAttr) {
        super(ctx, attrs, defStyleAttr);
        mSavedBackground = getBackground();
        mNoPaddingDrawable = new android.support.v17.leanback.widget.GuidedActionEditText.NoPaddingDrawable();
        setBackground(mNoPaddingDrawable);
    }

    @java.lang.Override
    public void setImeKeyListener(android.support.v17.leanback.widget.ImeKeyMonitor.ImeKeyListener listener) {
        mKeyListener = listener;
    }

    @java.lang.Override
    public boolean onKeyPreIme(int keyCode, android.view.KeyEvent event) {
        boolean result = false;
        if (mKeyListener != null) {
            result = mKeyListener.onKeyPreIme(this, keyCode, event);
        }
        if (!result) {
            result = super.onKeyPreIme(keyCode, event);
        }
        return result;
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(isFocused() ? android.widget.EditText.class.getName() : android.widget.TextView.class.getName());
    }

    @java.lang.Override
    protected void onFocusChanged(boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setBackground(mSavedBackground);
        } else {
            setBackground(mNoPaddingDrawable);
        }
        // Make the TextView focusable during editing, avoid the TextView gets accessibility focus
        // before editing started. see also GuidedActionAdapterGroup where setFocusable(true).
        if (!focused) {
            setFocusable(false);
        }
    }
}

