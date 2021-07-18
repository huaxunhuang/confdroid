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
package android.widget;


/**
 * Simple {@link ViewAnimator} that will animate between two or more views
 * that have been added to it.  Only one child is shown at a time.  If
 * requested, can automatically flip between each child at a regular interval.
 *
 * @unknown ref android.R.styleable#ViewFlipper_flipInterval
 * @unknown ref android.R.styleable#ViewFlipper_autoStart
 */
@android.widget.RemoteViews.RemoteView
public class ViewFlipper extends android.widget.ViewAnimator {
    private static final java.lang.String TAG = "ViewFlipper";

    private static final boolean LOGD = false;

    private static final int DEFAULT_INTERVAL = 3000;

    private int mFlipInterval = android.widget.ViewFlipper.DEFAULT_INTERVAL;

    private boolean mAutoStart = false;

    private boolean mRunning = false;

    private boolean mStarted = false;

    private boolean mVisible = false;

    @android.annotation.UnsupportedAppUsage
    private boolean mUserPresent = true;

    public ViewFlipper(android.content.Context context) {
        super(context);
    }

    public ViewFlipper(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ViewFlipper);
        mFlipInterval = a.getInt(com.android.internal.R.styleable.ViewFlipper_flipInterval, android.widget.ViewFlipper.DEFAULT_INTERVAL);
        mAutoStart = a.getBoolean(com.android.internal.R.styleable.ViewFlipper_autoStart, false);
        a.recycle();
    }

    private final android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            final java.lang.String action = intent.getAction();
            if (android.content.Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                updateRunning();
            } else
                if (android.content.Intent.ACTION_USER_PRESENT.equals(action)) {
                    mUserPresent = true;
                    updateRunning(false);
                }

        }
    };

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Listen for broadcasts related to user-presence
        final android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(android.content.Intent.ACTION_SCREEN_OFF);
        filter.addAction(android.content.Intent.ACTION_USER_PRESENT);
        // OK, this is gross but needed. This class is supported by the
        // remote views machanism and as a part of that the remote views
        // can be inflated by a context for another user without the app
        // having interact users permission - just for loading resources.
        // For exmaple, when adding widgets from a user profile to the
        // home screen. Therefore, we register the receiver as the current
        // user not the one the context is for.
        getContext().registerReceiverAsUser(mReceiver, android.widget.android.os.Process.myUserHandle(), filter, null, getHandler());
        if (mAutoStart) {
            // Automatically start when requested
            startFlipping();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        getContext().unregisterReceiver(mReceiver);
        updateRunning();
    }

    @java.lang.Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == android.view.View.VISIBLE;
        updateRunning(false);
    }

    /**
     * How long to wait before flipping to the next view
     *
     * @param milliseconds
     * 		time in milliseconds
     */
    @android.view.RemotableViewMethod
    public void setFlipInterval(@android.annotation.IntRange(from = 0)
    int milliseconds) {
        mFlipInterval = milliseconds;
    }

    /**
     * Get the delay before flipping to the next view.
     *
     * @return delay time in milliseconds
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.IntRange(from = 0)
    public int getFlipInterval() {
        return mFlipInterval;
    }

    /**
     * Start a timer to cycle through child views
     */
    public void startFlipping() {
        mStarted = true;
        updateRunning();
    }

    /**
     * No more flips
     */
    public void stopFlipping() {
        mStarted = false;
        updateRunning();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ViewFlipper.class.getName();
    }

    /**
     * Internal method to start or stop dispatching flip {@link Message} based
     * on {@link #mRunning} and {@link #mVisible} state.
     */
    private void updateRunning() {
        updateRunning(true);
    }

    /**
     * Internal method to start or stop dispatching flip {@link Message} based
     * on {@link #mRunning} and {@link #mVisible} state.
     *
     * @param flipNow
     * 		Determines whether or not to execute the animation now, in
     * 		addition to queuing future flips. If omitted, defaults to
     * 		true.
     */
    @android.annotation.UnsupportedAppUsage
    private void updateRunning(boolean flipNow) {
        boolean running = (mVisible && mStarted) && mUserPresent;
        if (running != mRunning) {
            if (running) {
                showOnly(mWhichChild, flipNow);
                postDelayed(mFlipRunnable, mFlipInterval);
            } else {
                removeCallbacks(mFlipRunnable);
            }
            mRunning = running;
        }
        if (android.widget.ViewFlipper.LOGD) {
            android.util.Log.d(android.widget.ViewFlipper.TAG, (((((("updateRunning() mVisible=" + mVisible) + ", mStarted=") + mStarted) + ", mUserPresent=") + mUserPresent) + ", mRunning=") + mRunning);
        }
    }

    /**
     * Returns true if the child views are flipping.
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public boolean isFlipping() {
        return mStarted;
    }

    /**
     * Set if this view automatically calls {@link #startFlipping()} when it
     * becomes attached to a window.
     */
    public void setAutoStart(boolean autoStart) {
        mAutoStart = autoStart;
    }

    /**
     * Returns true if this view automatically calls {@link #startFlipping()}
     * when it becomes attached to a window.
     */
    @android.view.inspector.InspectableProperty
    public boolean isAutoStart() {
        return mAutoStart;
    }

    private final java.lang.Runnable mFlipRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if (mRunning) {
                showNext();
                postDelayed(mFlipRunnable, mFlipInterval);
            }
        }
    };
}

