/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */
package android.support.v17.leanback.widget;


/**
 * A LinearLayout that preserves the focused child view.
 */
class PlaybackControlsRowView extends android.widget.LinearLayout {
    public interface OnUnhandledKeyListener {
        /**
         * Returns true if the key event should be consumed.
         */
        public boolean onUnhandledKey(android.view.KeyEvent event);
    }

    private android.support.v17.leanback.widget.PlaybackControlsRowView.OnUnhandledKeyListener mOnUnhandledKeyListener;

    public PlaybackControlsRowView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaybackControlsRowView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnUnhandledKeyListener(android.support.v17.leanback.widget.PlaybackControlsRowView.OnUnhandledKeyListener listener) {
        mOnUnhandledKeyListener = listener;
    }

    public android.support.v17.leanback.widget.PlaybackControlsRowView.OnUnhandledKeyListener getOnUnhandledKeyListener() {
        return mOnUnhandledKeyListener;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        return (mOnUnhandledKeyListener != null) && mOnUnhandledKeyListener.onUnhandledKey(event);
    }

    @java.lang.Override
    protected boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect) {
        final android.view.View focused = findFocus();
        if ((focused != null) && focused.requestFocus(direction, previouslyFocusedRect)) {
            return true;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }
}

