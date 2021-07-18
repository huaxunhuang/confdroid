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
package android.graphics.drawable;


/**
 * Drawable containing a set of Drawable keyframes where the currently displayed
 * keyframe is chosen based on the current state set. Animations between
 * keyframes may optionally be defined using transition elements.
 * <p>
 * This drawable can be defined in an XML file with the <code>
 * &lt;animated-selector></code> element. Each keyframe Drawable is defined in a
 * nested <code>&lt;item></code> element. Transitions are defined in a nested
 * <code>&lt;transition></code> element.
 *
 * @unknown ref android.R.styleable#DrawableStates_state_focused
 * @unknown ref android.R.styleable#DrawableStates_state_window_focused
 * @unknown ref android.R.styleable#DrawableStates_state_enabled
 * @unknown ref android.R.styleable#DrawableStates_state_checkable
 * @unknown ref android.R.styleable#DrawableStates_state_checked
 * @unknown ref android.R.styleable#DrawableStates_state_selected
 * @unknown ref android.R.styleable#DrawableStates_state_activated
 * @unknown ref android.R.styleable#DrawableStates_state_active
 * @unknown ref android.R.styleable#DrawableStates_state_single
 * @unknown ref android.R.styleable#DrawableStates_state_first
 * @unknown ref android.R.styleable#DrawableStates_state_middle
 * @unknown ref android.R.styleable#DrawableStates_state_last
 * @unknown ref android.R.styleable#DrawableStates_state_pressed
 */
public class AnimatedStateListDrawable extends android.graphics.drawable.StateListDrawable {
    private static final java.lang.String LOGTAG = android.graphics.drawable.AnimatedStateListDrawable.class.getSimpleName();

    private static final java.lang.String ELEMENT_TRANSITION = "transition";

    private static final java.lang.String ELEMENT_ITEM = "item";

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState mState;

    /**
     * The currently running transition, if any.
     */
    private android.graphics.drawable.AnimatedStateListDrawable.Transition mTransition;

    /**
     * Index to be set after the transition ends.
     */
    private int mTransitionToIndex = -1;

    /**
     * Index away from which we are transitioning.
     */
    private int mTransitionFromIndex = -1;

    private boolean mMutated;

    public AnimatedStateListDrawable() {
        this(null, null);
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        final boolean changed = super.setVisible(visible, restart);
        if ((mTransition != null) && (changed || restart)) {
            if (visible) {
                mTransition.start();
            } else {
                // Ensure we're showing the correct state when visible.
                jumpToCurrentState();
            }
        }
        return changed;
    }

    /**
     * Add a new drawable to the set of keyframes.
     *
     * @param stateSet
     * 		An array of resource IDs to associate with the keyframe
     * @param drawable
     * 		The drawable to show when in the specified state, may not be null
     * @param id
     * 		The unique identifier for the keyframe
     */
    public void addState(@android.annotation.NonNull
    int[] stateSet, @android.annotation.NonNull
    android.graphics.drawable.Drawable drawable, int id) {
        if (drawable == null) {
            throw new java.lang.IllegalArgumentException("Drawable must not be null");
        }
        mState.addStateSet(stateSet, drawable, id);
        onStateChange(getState());
    }

    /**
     * Adds a new transition between keyframes.
     *
     * @param fromId
     * 		Unique identifier of the starting keyframe
     * @param toId
     * 		Unique identifier of the ending keyframe
     * @param transition
     * 		An {@link Animatable} drawable to use as a transition, may not be null
     * @param reversible
     * 		Whether the transition can be reversed
     */
    public <T extends android.graphics.drawable.Drawable & android.graphics.drawable.Animatable> void addTransition(int fromId, int toId, @android.annotation.NonNull
    T transition, boolean reversible) {
        if (transition == null) {
            throw new java.lang.IllegalArgumentException("Transition drawable must not be null");
        }
        mState.addTransition(fromId, toId, transition, reversible);
    }

    @java.lang.Override
    public boolean isStateful() {
        return true;
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        // If we're not already at the target index, either attempt to find a
        // valid transition to it or jump directly there.
        final int targetIndex = mState.indexOfKeyframe(stateSet);
        boolean changed = (targetIndex != getCurrentIndex()) && (selectTransition(targetIndex) || selectDrawable(targetIndex));
        // We need to propagate the state change to the current drawable, but
        // we can't call StateListDrawable.onStateChange() without changing the
        // current drawable.
        final android.graphics.drawable.Drawable current = getCurrent();
        if (current != null) {
            changed |= current.setState(stateSet);
        }
        return changed;
    }

    private boolean selectTransition(int toIndex) {
        final int fromIndex;
        final android.graphics.drawable.AnimatedStateListDrawable.Transition currentTransition = mTransition;
        if (currentTransition != null) {
            if (toIndex == mTransitionToIndex) {
                // Already animating to that keyframe.
                return true;
            } else
                if ((toIndex == mTransitionFromIndex) && currentTransition.canReverse()) {
                    // Reverse the current animation.
                    currentTransition.reverse();
                    mTransitionToIndex = mTransitionFromIndex;
                    mTransitionFromIndex = toIndex;
                    return true;
                }

            // Start the next transition from the end of the current one.
            fromIndex = mTransitionToIndex;
            // Changing animation, end the current animation.
            currentTransition.stop();
        } else {
            fromIndex = getCurrentIndex();
        }
        // Reset state.
        mTransition = null;
        mTransitionFromIndex = -1;
        mTransitionToIndex = -1;
        final android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState state = mState;
        final int fromId = state.getKeyframeIdAt(fromIndex);
        final int toId = state.getKeyframeIdAt(toIndex);
        if ((toId == 0) || (fromId == 0)) {
            // Missing a keyframe ID.
            return false;
        }
        final int transitionIndex = state.indexOfTransition(fromId, toId);
        if (transitionIndex < 0) {
            // Couldn't select a transition.
            return false;
        }
        boolean hasReversibleFlag = state.transitionHasReversibleFlag(fromId, toId);
        // This may fail if we're already on the transition, but that's okay!
        selectDrawable(transitionIndex);
        final android.graphics.drawable.AnimatedStateListDrawable.Transition transition;
        final android.graphics.drawable.Drawable d = getCurrent();
        if (d instanceof android.graphics.drawable.AnimationDrawable) {
            final boolean reversed = state.isTransitionReversed(fromId, toId);
            transition = new android.graphics.drawable.AnimatedStateListDrawable.AnimationDrawableTransition(((android.graphics.drawable.AnimationDrawable) (d)), reversed, hasReversibleFlag);
        } else
            if (d instanceof android.graphics.drawable.AnimatedVectorDrawable) {
                final boolean reversed = state.isTransitionReversed(fromId, toId);
                transition = new android.graphics.drawable.AnimatedStateListDrawable.AnimatedVectorDrawableTransition(((android.graphics.drawable.AnimatedVectorDrawable) (d)), reversed, hasReversibleFlag);
            } else
                if (d instanceof android.graphics.drawable.Animatable) {
                    transition = new android.graphics.drawable.AnimatedStateListDrawable.AnimatableTransition(((android.graphics.drawable.Animatable) (d)));
                } else {
                    // We don't know how to animate this transition.
                    return false;
                }


        transition.start();
        mTransition = transition;
        mTransitionFromIndex = fromIndex;
        mTransitionToIndex = toIndex;
        return true;
    }

    private static abstract class Transition {
        public abstract void start();

        public abstract void stop();

        public void reverse() {
            // Not supported by default.
        }

        public boolean canReverse() {
            return false;
        }
    }

    private static class AnimatableTransition extends android.graphics.drawable.AnimatedStateListDrawable.Transition {
        private final android.graphics.drawable.Animatable mA;

        public AnimatableTransition(android.graphics.drawable.Animatable a) {
            mA = a;
        }

        @java.lang.Override
        public void start() {
            mA.start();
        }

        @java.lang.Override
        public void stop() {
            mA.stop();
        }
    }

    private static class AnimationDrawableTransition extends android.graphics.drawable.AnimatedStateListDrawable.Transition {
        private final android.animation.ObjectAnimator mAnim;

        // Even AnimationDrawable is always reversible technically, but
        // we should obey the XML's android:reversible flag.
        private final boolean mHasReversibleFlag;

        public AnimationDrawableTransition(android.graphics.drawable.AnimationDrawable ad, boolean reversed, boolean hasReversibleFlag) {
            final int frameCount = ad.getNumberOfFrames();
            final int fromFrame = (reversed) ? frameCount - 1 : 0;
            final int toFrame = (reversed) ? 0 : frameCount - 1;
            final android.graphics.drawable.AnimatedStateListDrawable.FrameInterpolator interp = new android.graphics.drawable.AnimatedStateListDrawable.FrameInterpolator(ad, reversed);
            final android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofInt(ad, "currentIndex", fromFrame, toFrame);
            anim.setAutoCancel(true);
            anim.setDuration(interp.getTotalDuration());
            anim.setInterpolator(interp);
            mHasReversibleFlag = hasReversibleFlag;
            mAnim = anim;
        }

        @java.lang.Override
        public boolean canReverse() {
            return mHasReversibleFlag;
        }

        @java.lang.Override
        public void start() {
            mAnim.start();
        }

        @java.lang.Override
        public void reverse() {
            mAnim.reverse();
        }

        @java.lang.Override
        public void stop() {
            mAnim.cancel();
        }
    }

    private static class AnimatedVectorDrawableTransition extends android.graphics.drawable.AnimatedStateListDrawable.Transition {
        private final android.graphics.drawable.AnimatedVectorDrawable mAvd;

        // mReversed is indicating the current transition's direction.
        private final boolean mReversed;

        // mHasReversibleFlag is indicating whether the whole transition has
        // reversible flag set to true.
        // If mHasReversibleFlag is false, then mReversed is always false.
        private final boolean mHasReversibleFlag;

        public AnimatedVectorDrawableTransition(android.graphics.drawable.AnimatedVectorDrawable avd, boolean reversed, boolean hasReversibleFlag) {
            mAvd = avd;
            mReversed = reversed;
            mHasReversibleFlag = hasReversibleFlag;
        }

        @java.lang.Override
        public boolean canReverse() {
            // When the transition's XML says it is not reversible, then we obey
            // it, even if the AVD itself is reversible.
            // This will help the single direction transition.
            return mAvd.canReverse() && mHasReversibleFlag;
        }

        @java.lang.Override
        public void start() {
            if (mReversed) {
                reverse();
            } else {
                mAvd.start();
            }
        }

        @java.lang.Override
        public void reverse() {
            if (canReverse()) {
                mAvd.reverse();
            } else {
                android.util.Log.w(android.graphics.drawable.AnimatedStateListDrawable.LOGTAG, "Can't reverse, either the reversible is set to false," + " or the AnimatedVectorDrawable can't reverse");
            }
        }

        @java.lang.Override
        public void stop() {
            mAvd.stop();
        }
    }

    @java.lang.Override
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        if (mTransition != null) {
            mTransition.stop();
            mTransition = null;
            selectDrawable(mTransitionToIndex);
            mTransitionToIndex = -1;
            mTransitionFromIndex = -1;
        }
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.AnimatedStateListDrawable);
        super.inflateWithAttributes(r, parser, a, R.styleable.AnimatedStateListDrawable_visible);
        updateStateFromTypedArray(a);
        updateDensity(r);
        a.recycle();
        inflateChildElements(r, parser, attrs, theme);
        init();
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.Nullable
    android.content.res.Resources.Theme theme) {
        super.applyTheme(theme);
        final android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState state = mState;
        if ((state == null) || (state.mAnimThemeAttrs == null)) {
            return;
        }
        final android.content.res.TypedArray a = theme.resolveAttributes(state.mAnimThemeAttrs, R.styleable.AnimatedRotateDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        init();
    }

    private void updateStateFromTypedArray(android.content.res.TypedArray a) {
        final android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState state = mState;
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mAnimThemeAttrs = a.extractThemeAttrs();
        state.setVariablePadding(a.getBoolean(R.styleable.AnimatedStateListDrawable_variablePadding, state.mVariablePadding));
        state.setConstantSize(a.getBoolean(R.styleable.AnimatedStateListDrawable_constantSize, state.mConstantSize));
        state.setEnterFadeDuration(a.getInt(R.styleable.AnimatedStateListDrawable_enterFadeDuration, state.mEnterFadeDuration));
        state.setExitFadeDuration(a.getInt(R.styleable.AnimatedStateListDrawable_exitFadeDuration, state.mExitFadeDuration));
        setDither(a.getBoolean(R.styleable.AnimatedStateListDrawable_dither, state.mDither));
        setAutoMirrored(a.getBoolean(R.styleable.AnimatedStateListDrawable_autoMirrored, state.mAutoMirrored));
    }

    private void init() {
        onStateChange(getState());
    }

    private void inflateChildElements(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        final int innerDepth = parser.getDepth() + 1;
        int depth;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (((depth = parser.getDepth()) >= innerDepth) || (type != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            if (depth > innerDepth) {
                continue;
            }
            if (parser.getName().equals(android.graphics.drawable.AnimatedStateListDrawable.ELEMENT_ITEM)) {
                parseItem(r, parser, attrs, theme);
            } else
                if (parser.getName().equals(android.graphics.drawable.AnimatedStateListDrawable.ELEMENT_TRANSITION)) {
                    parseTransition(r, parser, attrs, theme);
                }

        } 
    }

    private int parseTransition(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // This allows state list drawable item elements to be themed at
        // inflation time but does NOT make them work for Zygote preload.
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.AnimatedStateListDrawableTransition);
        final int fromId = a.getResourceId(R.styleable.AnimatedStateListDrawableTransition_fromId, 0);
        final int toId = a.getResourceId(R.styleable.AnimatedStateListDrawableTransition_toId, 0);
        final boolean reversible = a.getBoolean(R.styleable.AnimatedStateListDrawableTransition_reversible, false);
        android.graphics.drawable.Drawable dr = a.getDrawable(R.styleable.AnimatedStateListDrawableTransition_drawable);
        a.recycle();
        // Loading child elements modifies the state of the AttributeSet's
        // underlying parser, so it needs to happen after obtaining
        // attributes and extracting states.
        if (dr == null) {
            int type;
            while ((type = parser.next()) == org.xmlpull.v1.XmlPullParser.TEXT) {
            } 
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": <transition> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
            }
            dr = android.graphics.drawable.Drawable.createFromXmlInner(r, parser, attrs, theme);
        }
        return mState.addTransition(fromId, toId, dr, reversible);
    }

    private int parseItem(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // This allows state list drawable item elements to be themed at
        // inflation time but does NOT make them work for Zygote preload.
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.AnimatedStateListDrawableItem);
        final int keyframeId = a.getResourceId(R.styleable.AnimatedStateListDrawableItem_id, 0);
        android.graphics.drawable.Drawable dr = a.getDrawable(R.styleable.AnimatedStateListDrawableItem_drawable);
        a.recycle();
        final int[] states = extractStateSet(attrs);
        // Loading child elements modifies the state of the AttributeSet's
        // underlying parser, so it needs to happen after obtaining
        // attributes and extracting states.
        if (dr == null) {
            int type;
            while ((type = parser.next()) == org.xmlpull.v1.XmlPullParser.TEXT) {
            } 
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
            }
            dr = android.graphics.drawable.Drawable.createFromXmlInner(r, parser, attrs, theme);
        }
        return mState.addStateSet(states, dr, keyframeId);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mState.mutate();
            mMutated = true;
        }
        return this;
    }

    @java.lang.Override
    android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState cloneConstantState() {
        return new android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState(mState, this, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearMutated() {
        super.clearMutated();
        mMutated = false;
    }

    static class AnimatedStateListState extends android.graphics.drawable.StateListDrawable.StateListState {
        // REVERSED_BIT is indicating the current transition's direction.
        private static final long REVERSED_BIT = 0x100000000L;

        // REVERSIBLE_FLAG_BIT is indicating whether the whole transition has
        // reversible flag set to true.
        private static final long REVERSIBLE_FLAG_BIT = 0x200000000L;

        int[] mAnimThemeAttrs;

        @android.annotation.UnsupportedAppUsage
        android.util.LongSparseLongArray mTransitions;

        @android.annotation.UnsupportedAppUsage
        android.util.SparseIntArray mStateIds;

        AnimatedStateListState(@android.annotation.Nullable
        android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState orig, @android.annotation.NonNull
        android.graphics.drawable.AnimatedStateListDrawable owner, @android.annotation.Nullable
        android.content.res.Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                // Perform a shallow copy and rely on mutate() to deep-copy.
                mAnimThemeAttrs = orig.mAnimThemeAttrs;
                mTransitions = orig.mTransitions;
                mStateIds = orig.mStateIds;
            } else {
                mTransitions = new android.util.LongSparseLongArray();
                mStateIds = new android.util.SparseIntArray();
            }
        }

        void mutate() {
            mTransitions = mTransitions.clone();
            mStateIds = mStateIds.clone();
        }

        int addTransition(int fromId, int toId, @android.annotation.NonNull
        android.graphics.drawable.Drawable anim, boolean reversible) {
            final int pos = super.addChild(anim);
            final long keyFromTo = android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.generateTransitionKey(fromId, toId);
            long reversibleBit = 0;
            if (reversible) {
                reversibleBit = android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.REVERSIBLE_FLAG_BIT;
            }
            mTransitions.append(keyFromTo, pos | reversibleBit);
            if (reversible) {
                final long keyToFrom = android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.generateTransitionKey(toId, fromId);
                mTransitions.append(keyToFrom, (pos | android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.REVERSED_BIT) | reversibleBit);
            }
            return pos;
        }

        int addStateSet(@android.annotation.NonNull
        int[] stateSet, @android.annotation.NonNull
        android.graphics.drawable.Drawable drawable, int id) {
            final int index = super.addStateSet(stateSet, drawable);
            mStateIds.put(index, id);
            return index;
        }

        int indexOfKeyframe(@android.annotation.NonNull
        int[] stateSet) {
            final int index = super.indexOfStateSet(stateSet);
            if (index >= 0) {
                return index;
            }
            return super.indexOfStateSet(StateSet.WILD_CARD);
        }

        int getKeyframeIdAt(int index) {
            return index < 0 ? 0 : mStateIds.get(index, 0);
        }

        int indexOfTransition(int fromId, int toId) {
            final long keyFromTo = android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.generateTransitionKey(fromId, toId);
            return ((int) (mTransitions.get(keyFromTo, -1)));
        }

        boolean isTransitionReversed(int fromId, int toId) {
            final long keyFromTo = android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.generateTransitionKey(fromId, toId);
            return (mTransitions.get(keyFromTo, -1) & android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.REVERSED_BIT) != 0;
        }

        boolean transitionHasReversibleFlag(int fromId, int toId) {
            final long keyFromTo = android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.generateTransitionKey(fromId, toId);
            return (mTransitions.get(keyFromTo, -1) & android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState.REVERSIBLE_FLAG_BIT) != 0;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return (mAnimThemeAttrs != null) || super.canApplyTheme();
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.AnimatedStateListDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.AnimatedStateListDrawable(this, res);
        }

        private static long generateTransitionKey(int fromId, int toId) {
            return (((long) (fromId)) << 32) | toId;
        }
    }

    @java.lang.Override
    protected void setConstantState(@android.annotation.NonNull
    android.graphics.drawable.DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState) {
            mState = ((android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState) (state));
        }
    }

    private AnimatedStateListDrawable(@android.annotation.Nullable
    android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState state, @android.annotation.Nullable
    android.content.res.Resources res) {
        super(null);
        // Every animated state list drawable has its own constant state.
        final android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState newState = new android.graphics.drawable.AnimatedStateListDrawable.AnimatedStateListState(state, this, res);
        setConstantState(newState);
        onStateChange(getState());
        jumpToCurrentState();
    }

    /**
     * Interpolates between frames with respect to their individual durations.
     */
    private static class FrameInterpolator implements android.animation.TimeInterpolator {
        private int[] mFrameTimes;

        private int mFrames;

        private int mTotalDuration;

        public FrameInterpolator(android.graphics.drawable.AnimationDrawable d, boolean reversed) {
            updateFrames(d, reversed);
        }

        public int updateFrames(android.graphics.drawable.AnimationDrawable d, boolean reversed) {
            final int N = d.getNumberOfFrames();
            mFrames = N;
            if ((mFrameTimes == null) || (mFrameTimes.length < N)) {
                mFrameTimes = new int[N];
            }
            final int[] frameTimes = mFrameTimes;
            int totalDuration = 0;
            for (int i = 0; i < N; i++) {
                final int duration = d.getDuration(reversed ? (N - i) - 1 : i);
                frameTimes[i] = duration;
                totalDuration += duration;
            }
            mTotalDuration = totalDuration;
            return totalDuration;
        }

        public int getTotalDuration() {
            return mTotalDuration;
        }

        @java.lang.Override
        public float getInterpolation(float input) {
            final int elapsed = ((int) ((input * mTotalDuration) + 0.5F));
            final int N = mFrames;
            final int[] frameTimes = mFrameTimes;
            // Find the current frame and remaining time within that frame.
            int remaining = elapsed;
            int i = 0;
            while ((i < N) && (remaining >= frameTimes[i])) {
                remaining -= frameTimes[i];
                i++;
            } 
            // Remaining time is relative of total duration.
            final float frameElapsed;
            if (i < N) {
                frameElapsed = remaining / ((float) (mTotalDuration));
            } else {
                frameElapsed = 0;
            }
            return (i / ((float) (N))) + frameElapsed;
        }
    }
}

