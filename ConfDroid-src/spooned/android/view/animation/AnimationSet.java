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
package android.view.animation;


/**
 * Represents a group of Animations that should be played together.
 * The transformation of each individual animation are composed
 * together into a single transform.
 * If AnimationSet sets any properties that its children also set
 * (for example, duration or fillBefore), the values of AnimationSet
 * override the child values.
 *
 * <p>The way that AnimationSet inherits behavior from Animation is important to
 * understand. Some of the Animation attributes applied to AnimationSet affect the
 * AnimationSet itself, some are pushed down to the children, and some are ignored,
 * as follows:
 * <ul>
 *     <li>duration, repeatMode, fillBefore, fillAfter: These properties, when set
 *     on an AnimationSet object, will be pushed down to all child animations.</li>
 *     <li>repeatCount, fillEnabled: These properties are ignored for AnimationSet.</li>
 *     <li>startOffset, shareInterpolator: These properties apply to the AnimationSet itself.</li>
 * </ul>
 * Starting with {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH},
 * the behavior of these properties is the same in XML resources and at runtime (prior to that
 * release, the values set in XML were ignored for AnimationSet). That is, calling
 * <code>setDuration(500)</code> on an AnimationSet has the same effect as declaring
 * <code>android:duration="500"</code> in an XML resource for an AnimationSet object.</p>
 */
public class AnimationSet extends android.view.animation.Animation {
    private static final int PROPERTY_FILL_AFTER_MASK = 0x1;

    private static final int PROPERTY_FILL_BEFORE_MASK = 0x2;

    private static final int PROPERTY_REPEAT_MODE_MASK = 0x4;

    private static final int PROPERTY_START_OFFSET_MASK = 0x8;

    private static final int PROPERTY_SHARE_INTERPOLATOR_MASK = 0x10;

    private static final int PROPERTY_DURATION_MASK = 0x20;

    private static final int PROPERTY_MORPH_MATRIX_MASK = 0x40;

    private static final int PROPERTY_CHANGE_BOUNDS_MASK = 0x80;

    private int mFlags = 0;

    private boolean mDirty;

    private boolean mHasAlpha;

    private java.util.ArrayList<android.view.animation.Animation> mAnimations = new java.util.ArrayList<android.view.animation.Animation>();

    private android.view.animation.Transformation mTempTransformation = new android.view.animation.Transformation();

    private long mLastEnd;

    private long[] mStoredOffsets;

    /**
     * Constructor used when an AnimationSet is loaded from a resource.
     *
     * @param context
     * 		Application context to use
     * @param attrs
     * 		Attribute set from which to read values
     */
    public AnimationSet(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.AnimationSet);
        setFlag(android.view.animation.AnimationSet.PROPERTY_SHARE_INTERPOLATOR_MASK, a.getBoolean(com.android.internal.R.styleable.AnimationSet_shareInterpolator, true));
        init();
        if (context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_duration)) {
                mFlags |= android.view.animation.AnimationSet.PROPERTY_DURATION_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_fillBefore)) {
                mFlags |= android.view.animation.AnimationSet.PROPERTY_FILL_BEFORE_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_fillAfter)) {
                mFlags |= android.view.animation.AnimationSet.PROPERTY_FILL_AFTER_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_repeatMode)) {
                mFlags |= android.view.animation.AnimationSet.PROPERTY_REPEAT_MODE_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_startOffset)) {
                mFlags |= android.view.animation.AnimationSet.PROPERTY_START_OFFSET_MASK;
            }
        }
        a.recycle();
    }

    /**
     * Constructor to use when building an AnimationSet from code
     *
     * @param shareInterpolator
     * 		Pass true if all of the animations in this set
     * 		should use the interpolator associated with this AnimationSet.
     * 		Pass false if each animation should use its own interpolator.
     */
    public AnimationSet(boolean shareInterpolator) {
        setFlag(android.view.animation.AnimationSet.PROPERTY_SHARE_INTERPOLATOR_MASK, shareInterpolator);
        init();
    }

    @java.lang.Override
    protected android.view.animation.AnimationSet clone() throws java.lang.CloneNotSupportedException {
        final android.view.animation.AnimationSet animation = ((android.view.animation.AnimationSet) (super.clone()));
        animation.mTempTransformation = new android.view.animation.Transformation();
        animation.mAnimations = new java.util.ArrayList<android.view.animation.Animation>();
        final int count = mAnimations.size();
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        for (int i = 0; i < count; i++) {
            animation.mAnimations.add(animations.get(i).clone());
        }
        return animation;
    }

    private void setFlag(int mask, boolean value) {
        if (value) {
            mFlags |= mask;
        } else {
            mFlags &= ~mask;
        }
    }

    private void init() {
        mStartTime = 0;
    }

    @java.lang.Override
    public void setFillAfter(boolean fillAfter) {
        mFlags |= android.view.animation.AnimationSet.PROPERTY_FILL_AFTER_MASK;
        super.setFillAfter(fillAfter);
    }

    @java.lang.Override
    public void setFillBefore(boolean fillBefore) {
        mFlags |= android.view.animation.AnimationSet.PROPERTY_FILL_BEFORE_MASK;
        super.setFillBefore(fillBefore);
    }

    @java.lang.Override
    public void setRepeatMode(int repeatMode) {
        mFlags |= android.view.animation.AnimationSet.PROPERTY_REPEAT_MODE_MASK;
        super.setRepeatMode(repeatMode);
    }

    @java.lang.Override
    public void setStartOffset(long startOffset) {
        mFlags |= android.view.animation.AnimationSet.PROPERTY_START_OFFSET_MASK;
        super.setStartOffset(startOffset);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasAlpha() {
        if (mDirty) {
            mDirty = mHasAlpha = false;
            final int count = mAnimations.size();
            final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
            for (int i = 0; i < count; i++) {
                if (animations.get(i).hasAlpha()) {
                    mHasAlpha = true;
                    break;
                }
            }
        }
        return mHasAlpha;
    }

    /**
     * <p>Sets the duration of every child animation.</p>
     *
     * @param durationMillis
     * 		the duration of the animation, in milliseconds, for
     * 		every child in this set
     */
    @java.lang.Override
    public void setDuration(long durationMillis) {
        mFlags |= android.view.animation.AnimationSet.PROPERTY_DURATION_MASK;
        super.setDuration(durationMillis);
        mLastEnd = mStartOffset + mDuration;
    }

    /**
     * Add a child animation to this animation set.
     * The transforms of the child animations are applied in the order
     * that they were added
     *
     * @param a
     * 		Animation to add.
     */
    public void addAnimation(android.view.animation.Animation a) {
        mAnimations.add(a);
        boolean noMatrix = (mFlags & android.view.animation.AnimationSet.PROPERTY_MORPH_MATRIX_MASK) == 0;
        if (noMatrix && a.willChangeTransformationMatrix()) {
            mFlags |= android.view.animation.AnimationSet.PROPERTY_MORPH_MATRIX_MASK;
        }
        boolean changeBounds = (mFlags & android.view.animation.AnimationSet.PROPERTY_CHANGE_BOUNDS_MASK) == 0;
        if (changeBounds && a.willChangeBounds()) {
            mFlags |= android.view.animation.AnimationSet.PROPERTY_CHANGE_BOUNDS_MASK;
        }
        if ((mFlags & android.view.animation.AnimationSet.PROPERTY_DURATION_MASK) == android.view.animation.AnimationSet.PROPERTY_DURATION_MASK) {
            mLastEnd = mStartOffset + mDuration;
        } else {
            if (mAnimations.size() == 1) {
                mDuration = a.getStartOffset() + a.getDuration();
                mLastEnd = mStartOffset + mDuration;
            } else {
                mLastEnd = java.lang.Math.max(mLastEnd, (mStartOffset + a.getStartOffset()) + a.getDuration());
                mDuration = mLastEnd - mStartOffset;
            }
        }
        mDirty = true;
    }

    /**
     * Sets the start time of this animation and all child animations
     *
     * @see android.view.animation.Animation#setStartTime(long)
     */
    @java.lang.Override
    public void setStartTime(long startTimeMillis) {
        super.setStartTime(startTimeMillis);
        final int count = mAnimations.size();
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        for (int i = 0; i < count; i++) {
            android.view.animation.Animation a = animations.get(i);
            a.setStartTime(startTimeMillis);
        }
    }

    @java.lang.Override
    public long getStartTime() {
        long startTime = java.lang.Long.MAX_VALUE;
        final int count = mAnimations.size();
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        for (int i = 0; i < count; i++) {
            android.view.animation.Animation a = animations.get(i);
            startTime = java.lang.Math.min(startTime, a.getStartTime());
        }
        return startTime;
    }

    @java.lang.Override
    public void restrictDuration(long durationMillis) {
        super.restrictDuration(durationMillis);
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        int count = animations.size();
        for (int i = 0; i < count; i++) {
            animations.get(i).restrictDuration(durationMillis);
        }
    }

    /**
     * The duration of an AnimationSet is defined to be the
     * duration of the longest child animation.
     *
     * @see android.view.animation.Animation#getDuration()
     */
    @java.lang.Override
    public long getDuration() {
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        final int count = animations.size();
        long duration = 0;
        boolean durationSet = (mFlags & android.view.animation.AnimationSet.PROPERTY_DURATION_MASK) == android.view.animation.AnimationSet.PROPERTY_DURATION_MASK;
        if (durationSet) {
            duration = mDuration;
        } else {
            for (int i = 0; i < count; i++) {
                duration = java.lang.Math.max(duration, animations.get(i).getDuration());
            }
        }
        return duration;
    }

    /**
     * The duration hint of an animation set is the maximum of the duration
     * hints of all of its component animations.
     *
     * @see android.view.animation.Animation#computeDurationHint
     */
    public long computeDurationHint() {
        long duration = 0;
        final int count = mAnimations.size();
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        for (int i = count - 1; i >= 0; --i) {
            final long d = animations.get(i).computeDurationHint();
            if (d > duration)
                duration = d;

        }
        return duration;
    }

    /**
     *
     *
     * @unknown 
     */
    public void initializeInvalidateRegion(int left, int top, int right, int bottom) {
        final android.graphics.RectF region = mPreviousRegion;
        region.set(left, top, right, bottom);
        region.inset(-1.0F, -1.0F);
        if (mFillBefore) {
            final int count = mAnimations.size();
            final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
            final android.view.animation.Transformation temp = mTempTransformation;
            final android.view.animation.Transformation previousTransformation = mPreviousTransformation;
            for (int i = count - 1; i >= 0; --i) {
                final android.view.animation.Animation a = animations.get(i);
                if (((!a.isFillEnabled()) || a.getFillBefore()) || (a.getStartOffset() == 0)) {
                    temp.clear();
                    final android.view.animation.Interpolator interpolator = a.mInterpolator;
                    a.applyTransformation(interpolator != null ? interpolator.getInterpolation(0.0F) : 0.0F, temp);
                    previousTransformation.compose(temp);
                }
            }
        }
    }

    /**
     * The transformation of an animation set is the concatenation of all of its
     * component animations.
     *
     * @see android.view.animation.Animation#getTransformation
     */
    @java.lang.Override
    public boolean getTransformation(long currentTime, android.view.animation.Transformation t) {
        final int count = mAnimations.size();
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        final android.view.animation.Transformation temp = mTempTransformation;
        boolean more = false;
        boolean started = false;
        boolean ended = true;
        t.clear();
        for (int i = count - 1; i >= 0; --i) {
            final android.view.animation.Animation a = animations.get(i);
            temp.clear();
            more = a.getTransformation(currentTime, temp, getScaleFactor()) || more;
            t.compose(temp);
            started = started || a.hasStarted();
            ended = a.hasEnded() && ended;
        }
        if (started && (!mStarted)) {
            dispatchAnimationStart();
            mStarted = true;
        }
        if (ended != mEnded) {
            dispatchAnimationEnd();
            mEnded = ended;
        }
        return more;
    }

    /**
     *
     *
     * @see android.view.animation.Animation#scaleCurrentDuration(float)
     */
    @java.lang.Override
    public void scaleCurrentDuration(float scale) {
        final java.util.ArrayList<android.view.animation.Animation> animations = mAnimations;
        int count = animations.size();
        for (int i = 0; i < count; i++) {
            animations.get(i).scaleCurrentDuration(scale);
        }
    }

    /**
     *
     *
     * @see android.view.animation.Animation#initialize(int, int, int, int)
     */
    @java.lang.Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        boolean durationSet = (mFlags & android.view.animation.AnimationSet.PROPERTY_DURATION_MASK) == android.view.animation.AnimationSet.PROPERTY_DURATION_MASK;
        boolean fillAfterSet = (mFlags & android.view.animation.AnimationSet.PROPERTY_FILL_AFTER_MASK) == android.view.animation.AnimationSet.PROPERTY_FILL_AFTER_MASK;
        boolean fillBeforeSet = (mFlags & android.view.animation.AnimationSet.PROPERTY_FILL_BEFORE_MASK) == android.view.animation.AnimationSet.PROPERTY_FILL_BEFORE_MASK;
        boolean repeatModeSet = (mFlags & android.view.animation.AnimationSet.PROPERTY_REPEAT_MODE_MASK) == android.view.animation.AnimationSet.PROPERTY_REPEAT_MODE_MASK;
        boolean shareInterpolator = (mFlags & android.view.animation.AnimationSet.PROPERTY_SHARE_INTERPOLATOR_MASK) == android.view.animation.AnimationSet.PROPERTY_SHARE_INTERPOLATOR_MASK;
        boolean startOffsetSet = (mFlags & android.view.animation.AnimationSet.PROPERTY_START_OFFSET_MASK) == android.view.animation.AnimationSet.PROPERTY_START_OFFSET_MASK;
        if (shareInterpolator) {
            ensureInterpolator();
        }
        final java.util.ArrayList<android.view.animation.Animation> children = mAnimations;
        final int count = children.size();
        final long duration = mDuration;
        final boolean fillAfter = mFillAfter;
        final boolean fillBefore = mFillBefore;
        final int repeatMode = mRepeatMode;
        final android.view.animation.Interpolator interpolator = mInterpolator;
        final long startOffset = mStartOffset;
        long[] storedOffsets = mStoredOffsets;
        if (startOffsetSet) {
            if ((storedOffsets == null) || (storedOffsets.length != count)) {
                storedOffsets = mStoredOffsets = new long[count];
            }
        } else
            if (storedOffsets != null) {
                storedOffsets = mStoredOffsets = null;
            }

        for (int i = 0; i < count; i++) {
            android.view.animation.Animation a = children.get(i);
            if (durationSet) {
                a.setDuration(duration);
            }
            if (fillAfterSet) {
                a.setFillAfter(fillAfter);
            }
            if (fillBeforeSet) {
                a.setFillBefore(fillBefore);
            }
            if (repeatModeSet) {
                a.setRepeatMode(repeatMode);
            }
            if (shareInterpolator) {
                a.setInterpolator(interpolator);
            }
            if (startOffsetSet) {
                long offset = a.getStartOffset();
                a.setStartOffset(offset + startOffset);
                storedOffsets[i] = offset;
            }
            a.initialize(width, height, parentWidth, parentHeight);
        }
    }

    @java.lang.Override
    public void reset() {
        super.reset();
        restoreChildrenStartOffset();
    }

    /**
     *
     *
     * @unknown 
     */
    void restoreChildrenStartOffset() {
        final long[] offsets = mStoredOffsets;
        if (offsets == null)
            return;

        final java.util.ArrayList<android.view.animation.Animation> children = mAnimations;
        final int count = children.size();
        for (int i = 0; i < count; i++) {
            children.get(i).setStartOffset(offsets[i]);
        }
    }

    /**
     *
     *
     * @return All the child animations in this AnimationSet. Note that
    this may include other AnimationSets, which are not expanded.
     */
    public java.util.List<android.view.animation.Animation> getAnimations() {
        return mAnimations;
    }

    @java.lang.Override
    public boolean willChangeTransformationMatrix() {
        return (mFlags & android.view.animation.AnimationSet.PROPERTY_MORPH_MATRIX_MASK) == android.view.animation.AnimationSet.PROPERTY_MORPH_MATRIX_MASK;
    }

    @java.lang.Override
    public boolean willChangeBounds() {
        return (mFlags & android.view.animation.AnimationSet.PROPERTY_CHANGE_BOUNDS_MASK) == android.view.animation.AnimationSet.PROPERTY_CHANGE_BOUNDS_MASK;
    }
}

