/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.widget;


/**
 * This widget provides a simple utility for turning a continued long-press event
 * into a series of clicks at some set frequency. There is no actual 'zoom' functionality
 * handled by this widget directly. Instead, clients of this API should set up an
 * {@link View#setOnClickListener(OnClickListener) onClickListener} to handle
 * zoom functionality. That click listener is called on a frequency
 * determined by {@link #setZoomSpeed(long)} whenever the user long-presses
 * on the ZoomButton.
 *
 * @deprecated Use other means to handle this functionality. This widget is merely a
simple wrapper around a long-press handler.
 */
@java.lang.Deprecated
public class ZoomButton extends android.widget.ImageButton implements android.view.View.OnLongClickListener {
    private final java.lang.Runnable mRunnable = new java.lang.Runnable() {
        public void run() {
            if ((hasOnClickListeners() && mIsInLongpress) && isEnabled()) {
                callOnClick();
                postDelayed(this, mZoomSpeed);
            }
        }
    };

    private long mZoomSpeed = 1000;

    private boolean mIsInLongpress;

    public ZoomButton(android.content.Context context) {
        this(context, null);
    }

    public ZoomButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ZoomButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnLongClickListener(this);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if ((event.getAction() == android.view.MotionEvent.ACTION_CANCEL) || (event.getAction() == android.view.MotionEvent.ACTION_UP)) {
            mIsInLongpress = false;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Sets the delay between calls to the widget's {@link View#setOnClickListener(OnClickListener)
     * onClickListener}.
     *
     * @param speed
     * 		The delay between calls to the click listener, in milliseconds
     */
    public void setZoomSpeed(long speed) {
        mZoomSpeed = speed;
    }

    @java.lang.Override
    public boolean onLongClick(android.view.View v) {
        mIsInLongpress = true;
        post(mRunnable);
        return true;
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        mIsInLongpress = false;
        return super.onKeyUp(keyCode, event);
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            /* If we're being disabled reset the state back to unpressed
            as disabled views don't get events and therefore we won't
            get the up event to reset the state.
             */
            setPressed(false);
        }
        super.setEnabled(enabled);
    }

    @java.lang.Override
    public boolean dispatchUnhandledMove(android.view.View focused, int direction) {
        clearFocus();
        return super.dispatchUnhandledMove(focused, direction);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ZoomButton.class.getName();
    }
}

