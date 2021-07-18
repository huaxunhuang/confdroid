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
package android.graphics.drawable;


/**
 * An extension of LayerDrawables that is intended to cross-fade between
 * the first and second layer. To start the transition, call {@link #startTransition(int)}. To
 * display just the first layer, call {@link #resetTransition()}.
 * <p>
 * It can be defined in an XML file with the <code>&lt;transition></code> element.
 * Each Drawable in the transition is defined in a nested <code>&lt;item></code>. For more
 * information, see the guide to <a
 * href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @unknown ref android.R.styleable#LayerDrawableItem_left
 * @unknown ref android.R.styleable#LayerDrawableItem_top
 * @unknown ref android.R.styleable#LayerDrawableItem_right
 * @unknown ref android.R.styleable#LayerDrawableItem_bottom
 * @unknown ref android.R.styleable#LayerDrawableItem_drawable
 * @unknown ref android.R.styleable#LayerDrawableItem_id
 */
public class TransitionDrawable extends android.graphics.drawable.LayerDrawable implements android.graphics.drawable.Drawable.Callback {
    /**
     * A transition is about to start.
     */
    private static final int TRANSITION_STARTING = 0;

    /**
     * The transition has started and the animation is in progress
     */
    private static final int TRANSITION_RUNNING = 1;

    /**
     * No transition will be applied
     */
    private static final int TRANSITION_NONE = 2;

    /**
     * The current state of the transition. One of {@link #TRANSITION_STARTING},
     * {@link #TRANSITION_RUNNING} and {@link #TRANSITION_NONE}
     */
    private int mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_NONE;

    private boolean mReverse;

    private long mStartTimeMillis;

    private int mFrom;

    @android.annotation.UnsupportedAppUsage
    private int mTo;

    private int mDuration;

    private int mOriginalDuration;

    @android.annotation.UnsupportedAppUsage
    private int mAlpha = 0;

    @android.annotation.UnsupportedAppUsage
    private boolean mCrossFade;

    /**
     * Create a new transition drawable with the specified list of layers. At least
     * 2 layers are required for this drawable to work properly.
     */
    public TransitionDrawable(android.graphics.drawable.Drawable[] layers) {
        this(new android.graphics.drawable.TransitionDrawable.TransitionState(null, null, null), layers);
    }

    /**
     * Create a new transition drawable with no layer. To work correctly, at least 2
     * layers must be added to this drawable.
     *
     * @see #TransitionDrawable(Drawable[])
     */
    TransitionDrawable() {
        this(new android.graphics.drawable.TransitionDrawable.TransitionState(null, null, null), ((android.content.res.Resources) (null)));
    }

    private TransitionDrawable(android.graphics.drawable.TransitionDrawable.TransitionState state, android.content.res.Resources res) {
        super(state, res);
    }

    private TransitionDrawable(android.graphics.drawable.TransitionDrawable.TransitionState state, android.graphics.drawable.Drawable[] layers) {
        super(layers, state);
    }

    @java.lang.Override
    android.graphics.drawable.LayerDrawable.LayerState createConstantState(android.graphics.drawable.LayerDrawable.LayerState state, android.content.res.Resources res) {
        return new android.graphics.drawable.TransitionDrawable.TransitionState(((android.graphics.drawable.TransitionDrawable.TransitionState) (state)), this, res);
    }

    /**
     * Begin the second layer on top of the first layer.
     *
     * @param durationMillis
     * 		The length of the transition in milliseconds
     */
    public void startTransition(int durationMillis) {
        mFrom = 0;
        mTo = 255;
        mAlpha = 0;
        mDuration = mOriginalDuration = durationMillis;
        mReverse = false;
        mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_STARTING;
        invalidateSelf();
    }

    /**
     * Show the second layer on top of the first layer immediately
     *
     * @unknown 
     */
    public void showSecondLayer() {
        mAlpha = 255;
        mReverse = false;
        mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_NONE;
        invalidateSelf();
    }

    /**
     * Show only the first layer.
     */
    public void resetTransition() {
        mAlpha = 0;
        mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_NONE;
        invalidateSelf();
    }

    /**
     * Reverses the transition, picking up where the transition currently is.
     * If the transition is not currently running, this will start the transition
     * with the specified duration. If the transition is already running, the last
     * known duration will be used.
     *
     * @param duration
     * 		The duration to use if no transition is running.
     */
    public void reverseTransition(int duration) {
        final long time = android.os.SystemClock.uptimeMillis();
        // Animation is over
        if ((time - mStartTimeMillis) > mDuration) {
            if (mTo == 0) {
                mFrom = 0;
                mTo = 255;
                mAlpha = 0;
                mReverse = false;
            } else {
                mFrom = 255;
                mTo = 0;
                mAlpha = 255;
                mReverse = true;
            }
            mDuration = mOriginalDuration = duration;
            mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_STARTING;
            invalidateSelf();
            return;
        }
        mReverse = !mReverse;
        mFrom = mAlpha;
        mTo = (mReverse) ? 0 : 255;
        mDuration = ((int) ((mReverse) ? time - mStartTimeMillis : mOriginalDuration - (time - mStartTimeMillis)));
        mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_STARTING;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        boolean done = true;
        switch (mTransitionState) {
            case android.graphics.drawable.TransitionDrawable.TRANSITION_STARTING :
                mStartTimeMillis = android.os.SystemClock.uptimeMillis();
                done = false;
                mTransitionState = android.graphics.drawable.TransitionDrawable.TRANSITION_RUNNING;
                break;
            case android.graphics.drawable.TransitionDrawable.TRANSITION_RUNNING :
                if (mStartTimeMillis >= 0) {
                    float normalized = ((float) (android.os.SystemClock.uptimeMillis() - mStartTimeMillis)) / mDuration;
                    done = normalized >= 1.0F;
                    normalized = java.lang.Math.min(normalized, 1.0F);
                    mAlpha = ((int) (mFrom + ((mTo - mFrom) * normalized)));
                }
                break;
        }
        final int alpha = mAlpha;
        final boolean crossFade = mCrossFade;
        final android.graphics.drawable.LayerDrawable.ChildDrawable[] array = mLayerState.mChildren;
        if (done) {
            // the setAlpha() calls below trigger invalidation and redraw. If we're done, just draw
            // the appropriate drawable[s] and return
            if ((!crossFade) || (alpha == 0)) {
                array[0].mDrawable.draw(canvas);
            }
            if (alpha == 0xff) {
                array[1].mDrawable.draw(canvas);
            }
            return;
        }
        android.graphics.drawable.Drawable d;
        d = array[0].mDrawable;
        if (crossFade) {
            d.setAlpha(255 - alpha);
        }
        d.draw(canvas);
        if (crossFade) {
            d.setAlpha(0xff);
        }
        if (alpha > 0) {
            d = array[1].mDrawable;
            d.setAlpha(alpha);
            d.draw(canvas);
            d.setAlpha(0xff);
        }
        if (!done) {
            invalidateSelf();
        }
    }

    /**
     * Enables or disables the cross fade of the drawables. When cross fade
     * is disabled, the first drawable is always drawn opaque. With cross
     * fade enabled, the first drawable is drawn with the opposite alpha of
     * the second drawable. Cross fade is disabled by default.
     *
     * @param enabled
     * 		True to enable cross fading, false otherwise.
     */
    public void setCrossFadeEnabled(boolean enabled) {
        mCrossFade = enabled;
    }

    /**
     * Indicates whether the cross fade is enabled for this transition.
     *
     * @return True if cross fading is enabled, false otherwise.
     */
    public boolean isCrossFadeEnabled() {
        return mCrossFade;
    }

    static class TransitionState extends android.graphics.drawable.LayerDrawable.LayerState {
        TransitionState(android.graphics.drawable.TransitionDrawable.TransitionState orig, android.graphics.drawable.TransitionDrawable owner, android.content.res.Resources res) {
            super(orig, owner, res);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.TransitionDrawable(this, ((android.content.res.Resources) (null)));
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.TransitionDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }
}

