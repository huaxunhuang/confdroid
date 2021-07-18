/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * This class enables automatic and optimized animation of select properties on View objects.
 * If only one or two properties on a View object are being animated, then using an
 * {@link android.animation.ObjectAnimator} is fine; the property setters called by ObjectAnimator
 * are well equipped to do the right thing to set the property and invalidate the view
 * appropriately. But if several properties are animated simultaneously, or if you just want a
 * more convenient syntax to animate a specific property, then ViewPropertyAnimator might be
 * more well-suited to the task.
 *
 * <p>This class may provide better performance for several simultaneous animations, because
 * it will optimize invalidate calls to take place only once for several properties instead of each
 * animated property independently causing its own invalidation. Also, the syntax of using this
 * class could be easier to use because the caller need only tell the View object which
 * property to animate, and the value to animate either to or by, and this class handles the
 * details of configuring the underlying Animator class and starting it.</p>
 *
 * <p>This class is not constructed by the caller, but rather by the View whose properties
 * it will animate. Calls to {@link android.view.View#animate()} will return a reference
 * to the appropriate ViewPropertyAnimator object for that View.</p>
 */
public class ViewPropertyAnimator {
    /**
     * The View whose properties are being animated by this class. This is set at
     * construction time.
     */
    final android.view.View mView;

    /**
     * The duration of the underlying Animator object. By default, we don't set the duration
     * on the Animator and just use its default duration. If the duration is ever set on this
     * Animator, then we use the duration that it was set to.
     */
    private long mDuration;

    /**
     * A flag indicating whether the duration has been set on this object. If not, we don't set
     * the duration on the underlying Animator, but instead just use its default duration.
     */
    private boolean mDurationSet = false;

    /**
     * The startDelay of the underlying Animator object. By default, we don't set the startDelay
     * on the Animator and just use its default startDelay. If the startDelay is ever set on this
     * Animator, then we use the startDelay that it was set to.
     */
    private long mStartDelay = 0;

    /**
     * A flag indicating whether the startDelay has been set on this object. If not, we don't set
     * the startDelay on the underlying Animator, but instead just use its default startDelay.
     */
    private boolean mStartDelaySet = false;

    /**
     * The interpolator of the underlying Animator object. By default, we don't set the interpolator
     * on the Animator and just use its default interpolator. If the interpolator is ever set on
     * this Animator, then we use the interpolator that it was set to.
     */
    private android.animation.TimeInterpolator mInterpolator;

    /**
     * A flag indicating whether the interpolator has been set on this object. If not, we don't set
     * the interpolator on the underlying Animator, but instead just use its default interpolator.
     */
    private boolean mInterpolatorSet = false;

    /**
     * Listener for the lifecycle events of the underlying ValueAnimator object.
     */
    private Animator.AnimatorListener mListener = null;

    /**
     * Listener for the update events of the underlying ValueAnimator object.
     */
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = null;

    /**
     * A lazily-created ValueAnimator used in order to get some default animator properties
     * (duration, start delay, interpolator, etc.).
     */
    private android.animation.ValueAnimator mTempValueAnimator;

    /**
     * This listener is the mechanism by which the underlying Animator causes changes to the
     * properties currently being animated, as well as the cleanup after an animation is
     * complete.
     */
    private android.view.ViewPropertyAnimator.AnimatorEventListener mAnimatorEventListener = new android.view.ViewPropertyAnimator.AnimatorEventListener();

    /**
     * This list holds the properties that have been asked to animate. We allow the caller to
     * request several animations prior to actually starting the underlying animator. This
     * enables us to run one single animator to handle several properties in parallel. Each
     * property is tossed onto the pending list until the animation actually starts (which is
     * done by posting it onto mView), at which time the pending list is cleared and the properties
     * on that list are added to the list of properties associated with that animator.
     */
    java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder> mPendingAnimations = new java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder>();

    private java.lang.Runnable mPendingSetupAction;

    private java.lang.Runnable mPendingCleanupAction;

    private java.lang.Runnable mPendingOnStartAction;

    private java.lang.Runnable mPendingOnEndAction;

    /**
     * Constants used to associate a property being requested and the mechanism used to set
     * the property (this class calls directly into View to set the properties in question).
     */
    static final int NONE = 0x0;

    static final int TRANSLATION_X = 0x1;

    static final int TRANSLATION_Y = 0x2;

    static final int TRANSLATION_Z = 0x4;

    static final int SCALE_X = 0x8;

    static final int SCALE_Y = 0x10;

    static final int ROTATION = 0x20;

    static final int ROTATION_X = 0x40;

    static final int ROTATION_Y = 0x80;

    static final int X = 0x100;

    static final int Y = 0x200;

    static final int Z = 0x400;

    static final int ALPHA = 0x800;

    private static final int TRANSFORM_MASK = (((((((((android.view.ViewPropertyAnimator.TRANSLATION_X | android.view.ViewPropertyAnimator.TRANSLATION_Y) | android.view.ViewPropertyAnimator.TRANSLATION_Z) | android.view.ViewPropertyAnimator.SCALE_X) | android.view.ViewPropertyAnimator.SCALE_Y) | android.view.ViewPropertyAnimator.ROTATION) | android.view.ViewPropertyAnimator.ROTATION_X) | android.view.ViewPropertyAnimator.ROTATION_Y) | android.view.ViewPropertyAnimator.X) | android.view.ViewPropertyAnimator.Y) | android.view.ViewPropertyAnimator.Z;

    /**
     * The mechanism by which the user can request several properties that are then animated
     * together works by posting this Runnable to start the underlying Animator. Every time
     * a property animation is requested, we cancel any previous postings of the Runnable
     * and re-post it. This means that we will only ever run the Runnable (and thus start the
     * underlying animator) after the caller is done setting the properties that should be
     * animated together.
     */
    private java.lang.Runnable mAnimationStarter = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            startAnimation();
        }
    };

    /**
     * This class holds information about the overall animation being run on the set of
     * properties. The mask describes which properties are being animated and the
     * values holder is the list of all property/value objects.
     */
    private static class PropertyBundle {
        int mPropertyMask;

        java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder> mNameValuesHolder;

        PropertyBundle(int propertyMask, java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder> nameValuesHolder) {
            mPropertyMask = propertyMask;
            mNameValuesHolder = nameValuesHolder;
        }

        /**
         * Removes the given property from being animated as a part of this
         * PropertyBundle. If the property was a part of this bundle, it returns
         * true to indicate that it was, in fact, canceled. This is an indication
         * to the caller that a cancellation actually occurred.
         *
         * @param propertyConstant
         * 		The property whose cancellation is requested.
         * @return true if the given property is a part of this bundle and if it
        has therefore been canceled.
         */
        boolean cancel(int propertyConstant) {
            if (((mPropertyMask & propertyConstant) != 0) && (mNameValuesHolder != null)) {
                int count = mNameValuesHolder.size();
                for (int i = 0; i < count; ++i) {
                    android.view.ViewPropertyAnimator.NameValuesHolder nameValuesHolder = mNameValuesHolder.get(i);
                    if (nameValuesHolder.mNameConstant == propertyConstant) {
                        mNameValuesHolder.remove(i);
                        mPropertyMask &= ~propertyConstant;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * This list tracks the list of properties being animated by any particular animator.
     * In most situations, there would only ever be one animator running at a time. But it is
     * possible to request some properties to animate together, then while those properties
     * are animating, to request some other properties to animate together. The way that
     * works is by having this map associate the group of properties being animated with the
     * animator handling the animation. On every update event for an Animator, we ask the
     * map for the associated properties and set them accordingly.
     */
    private java.util.HashMap<android.animation.Animator, android.view.ViewPropertyAnimator.PropertyBundle> mAnimatorMap = new java.util.HashMap<android.animation.Animator, android.view.ViewPropertyAnimator.PropertyBundle>();

    private java.util.HashMap<android.animation.Animator, java.lang.Runnable> mAnimatorSetupMap;

    private java.util.HashMap<android.animation.Animator, java.lang.Runnable> mAnimatorCleanupMap;

    private java.util.HashMap<android.animation.Animator, java.lang.Runnable> mAnimatorOnStartMap;

    private java.util.HashMap<android.animation.Animator, java.lang.Runnable> mAnimatorOnEndMap;

    /**
     * This is the information we need to set each property during the animation.
     * mNameConstant is used to set the appropriate field in View, and the from/delta
     * values are used to calculate the animated value for a given animation fraction
     * during the animation.
     */
    static class NameValuesHolder {
        int mNameConstant;

        float mFromValue;

        float mDeltaValue;

        NameValuesHolder(int nameConstant, float fromValue, float deltaValue) {
            mNameConstant = nameConstant;
            mFromValue = fromValue;
            mDeltaValue = deltaValue;
        }
    }

    /**
     * Constructor, called by View. This is private by design, as the user should only
     * get a ViewPropertyAnimator by calling View.animate().
     *
     * @param view
     * 		The View associated with this ViewPropertyAnimator
     */
    ViewPropertyAnimator(android.view.View view) {
        mView = view;
        view.ensureTransformationInfo();
    }

    /**
     * Sets the duration for the underlying animator that animates the requested properties.
     * By default, the animator uses the default value for ValueAnimator. Calling this method
     * will cause the declared value to be used instead.
     *
     * @param duration
     * 		The length of ensuing property animations, in milliseconds. The value
     * 		cannot be negative.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator setDuration(long duration) {
        if (duration < 0) {
            throw new java.lang.IllegalArgumentException("Animators cannot have negative duration: " + duration);
        }
        mDurationSet = true;
        mDuration = duration;
        return this;
    }

    /**
     * Returns the current duration of property animations. If the duration was set on this
     * object, that value is returned. Otherwise, the default value of the underlying Animator
     * is returned.
     *
     * @see #setDuration(long)
     * @return The duration of animations, in milliseconds.
     */
    public long getDuration() {
        if (mDurationSet) {
            return mDuration;
        } else {
            // Just return the default from ValueAnimator, since that's what we'd get if
            // the value has not been set otherwise
            if (mTempValueAnimator == null) {
                mTempValueAnimator = new android.animation.ValueAnimator();
            }
            return mTempValueAnimator.getDuration();
        }
    }

    /**
     * Returns the current startDelay of property animations. If the startDelay was set on this
     * object, that value is returned. Otherwise, the default value of the underlying Animator
     * is returned.
     *
     * @see #setStartDelay(long)
     * @return The startDelay of animations, in milliseconds.
     */
    public long getStartDelay() {
        if (mStartDelaySet) {
            return mStartDelay;
        } else {
            // Just return the default from ValueAnimator (0), since that's what we'd get if
            // the value has not been set otherwise
            return 0;
        }
    }

    /**
     * Sets the startDelay for the underlying animator that animates the requested properties.
     * By default, the animator uses the default value for ValueAnimator. Calling this method
     * will cause the declared value to be used instead.
     *
     * @param startDelay
     * 		The delay of ensuing property animations, in milliseconds. The value
     * 		cannot be negative.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator setStartDelay(long startDelay) {
        if (startDelay < 0) {
            throw new java.lang.IllegalArgumentException(("Animators cannot have negative start " + "delay: ") + startDelay);
        }
        mStartDelaySet = true;
        mStartDelay = startDelay;
        return this;
    }

    /**
     * Sets the interpolator for the underlying animator that animates the requested properties.
     * By default, the animator uses the default interpolator for ValueAnimator. Calling this method
     * will cause the declared object to be used instead.
     *
     * @param interpolator
     * 		The TimeInterpolator to be used for ensuing property animations. A value
     * 		of <code>null</code> will result in linear interpolation.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator setInterpolator(android.animation.TimeInterpolator interpolator) {
        mInterpolatorSet = true;
        mInterpolator = interpolator;
        return this;
    }

    /**
     * Returns the timing interpolator that this animation uses.
     *
     * @return The timing interpolator for this animation.
     */
    public android.animation.TimeInterpolator getInterpolator() {
        if (mInterpolatorSet) {
            return mInterpolator;
        } else {
            // Just return the default from ValueAnimator, since that's what we'd get if
            // the value has not been set otherwise
            if (mTempValueAnimator == null) {
                mTempValueAnimator = new android.animation.ValueAnimator();
            }
            return mTempValueAnimator.getInterpolator();
        }
    }

    /**
     * Sets a listener for events in the underlying Animators that run the property
     * animations.
     *
     * @see Animator.AnimatorListener
     * @param listener
     * 		The listener to be called with AnimatorListener events. A value of
     * 		<code>null</code> removes any existing listener.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator setListener(android.animation.Animator.AnimatorListener listener) {
        mListener = listener;
        return this;
    }

    Animator.AnimatorListener getListener() {
        return mListener;
    }

    /**
     * Sets a listener for update events in the underlying ValueAnimator that runs
     * the property animations. Note that the underlying animator is animating between
     * 0 and 1 (these values are then turned into the actual property values internally
     * by ViewPropertyAnimator). So the animator cannot give information on the current
     * values of the properties being animated by this ViewPropertyAnimator, although
     * the view object itself can be queried to get the current values.
     *
     * @see android.animation.ValueAnimator.AnimatorUpdateListener
     * @param listener
     * 		The listener to be called with update events. A value of
     * 		<code>null</code> removes any existing listener.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator setUpdateListener(android.animation.ValueAnimator.AnimatorUpdateListener listener) {
        mUpdateListener = listener;
        return this;
    }

    ValueAnimator.AnimatorUpdateListener getUpdateListener() {
        return mUpdateListener;
    }

    /**
     * Starts the currently pending property animations immediately. Calling <code>start()</code>
     * is optional because all animations start automatically at the next opportunity. However,
     * if the animations are needed to start immediately and synchronously (not at the time when
     * the next event is processed by the hierarchy, which is when the animations would begin
     * otherwise), then this method can be used.
     */
    public void start() {
        mView.removeCallbacks(mAnimationStarter);
        startAnimation();
    }

    /**
     * Cancels all property animations that are currently running or pending.
     */
    public void cancel() {
        if (mAnimatorMap.size() > 0) {
            java.util.HashMap<android.animation.Animator, android.view.ViewPropertyAnimator.PropertyBundle> mAnimatorMapCopy = ((java.util.HashMap<android.animation.Animator, android.view.ViewPropertyAnimator.PropertyBundle>) (mAnimatorMap.clone()));
            java.util.Set<android.animation.Animator> animatorSet = mAnimatorMapCopy.keySet();
            for (android.animation.Animator runningAnim : animatorSet) {
                runningAnim.cancel();
            }
        }
        mPendingAnimations.clear();
        mPendingSetupAction = null;
        mPendingCleanupAction = null;
        mPendingOnStartAction = null;
        mPendingOnEndAction = null;
        mView.removeCallbacks(mAnimationStarter);
    }

    /**
     * This method will cause the View's <code>x</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator x(float value) {
        animateProperty(android.view.ViewPropertyAnimator.X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>x</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator xBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>y</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator y(float value) {
        animateProperty(android.view.ViewPropertyAnimator.Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>y</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator yBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>z</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setZ(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator z(float value) {
        animateProperty(android.view.ViewPropertyAnimator.Z, value);
        return this;
    }

    /**
     * This method will cause the View's <code>z</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setZ(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator zBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.Z, value);
        return this;
    }

    /**
     * This method will cause the View's <code>rotation</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setRotation(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator rotation(float value) {
        animateProperty(android.view.ViewPropertyAnimator.ROTATION, value);
        return this;
    }

    /**
     * This method will cause the View's <code>rotation</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setRotation(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator rotationBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.ROTATION, value);
        return this;
    }

    /**
     * This method will cause the View's <code>rotationX</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setRotationX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator rotationX(float value) {
        animateProperty(android.view.ViewPropertyAnimator.ROTATION_X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>rotationX</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setRotationX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator rotationXBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.ROTATION_X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>rotationY</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setRotationY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator rotationY(float value) {
        animateProperty(android.view.ViewPropertyAnimator.ROTATION_Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>rotationY</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setRotationY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator rotationYBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.ROTATION_Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>translationX</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setTranslationX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator translationX(float value) {
        animateProperty(android.view.ViewPropertyAnimator.TRANSLATION_X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>translationX</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setTranslationX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator translationXBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.TRANSLATION_X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>translationY</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setTranslationY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator translationY(float value) {
        animateProperty(android.view.ViewPropertyAnimator.TRANSLATION_Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>translationY</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setTranslationY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator translationYBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.TRANSLATION_Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>translationZ</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setTranslationZ(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator translationZ(float value) {
        animateProperty(android.view.ViewPropertyAnimator.TRANSLATION_Z, value);
        return this;
    }

    /**
     * This method will cause the View's <code>translationZ</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setTranslationZ(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator translationZBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.TRANSLATION_Z, value);
        return this;
    }

    /**
     * This method will cause the View's <code>scaleX</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setScaleX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator scaleX(float value) {
        animateProperty(android.view.ViewPropertyAnimator.SCALE_X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>scaleX</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setScaleX(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator scaleXBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.SCALE_X, value);
        return this;
    }

    /**
     * This method will cause the View's <code>scaleY</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setScaleY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator scaleY(float value) {
        animateProperty(android.view.ViewPropertyAnimator.SCALE_Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>scaleY</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setScaleY(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator scaleYBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.SCALE_Y, value);
        return this;
    }

    /**
     * This method will cause the View's <code>alpha</code> property to be animated to the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The value to be animated to.
     * @see View#setAlpha(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator alpha(float value) {
        animateProperty(android.view.ViewPropertyAnimator.ALPHA, value);
        return this;
    }

    /**
     * This method will cause the View's <code>alpha</code> property to be animated by the
     * specified value. Animations already running on the property will be canceled.
     *
     * @param value
     * 		The amount to be animated by, as an offset from the current value.
     * @see View#setAlpha(float)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator alphaBy(float value) {
        animatePropertyBy(android.view.ViewPropertyAnimator.ALPHA, value);
        return this;
    }

    /**
     * The View associated with this ViewPropertyAnimator will have its
     * {@link View#setLayerType(int, android.graphics.Paint) layer type} set to
     * {@link View#LAYER_TYPE_HARDWARE} for the duration of the next animation.
     * As stated in the documentation for {@link View#LAYER_TYPE_HARDWARE},
     * the actual type of layer used internally depends on the runtime situation of the
     * view. If the activity and this view are hardware-accelerated, then the layer will be
     * accelerated as well. If the activity or the view is not accelerated, then the layer will
     * effectively be the same as {@link View#LAYER_TYPE_SOFTWARE}.
     *
     * <p>This state is not persistent, either on the View or on this ViewPropertyAnimator: the
     * layer type of the View will be restored when the animation ends to what it was when this
     * method was called, and this setting on ViewPropertyAnimator is only valid for the next
     * animation. Note that calling this method and then independently setting the layer type of
     * the View (by a direct call to {@link View#setLayerType(int, android.graphics.Paint)}) will
     * result in some inconsistency, including having the layer type restored to its pre-withLayer()
     * value when the animation ends.</p>
     *
     * @see View#setLayerType(int, android.graphics.Paint)
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator withLayer() {
        mPendingSetupAction = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mView.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
                if (mView.isAttachedToWindow()) {
                    mView.buildLayer();
                }
            }
        };
        final int currentLayerType = mView.getLayerType();
        mPendingCleanupAction = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mView.setLayerType(currentLayerType, null);
            }
        };
        if (mAnimatorSetupMap == null) {
            mAnimatorSetupMap = new java.util.HashMap<android.animation.Animator, java.lang.Runnable>();
        }
        if (mAnimatorCleanupMap == null) {
            mAnimatorCleanupMap = new java.util.HashMap<android.animation.Animator, java.lang.Runnable>();
        }
        return this;
    }

    /**
     * Specifies an action to take place when the next animation runs. If there is a
     * {@link #setStartDelay(long) startDelay} set on this ViewPropertyAnimator, then the
     * action will run after that startDelay expires, when the actual animation begins.
     * This method, along with {@link #withEndAction(Runnable)}, is intended to help facilitate
     * choreographing ViewPropertyAnimator animations with other animations or actions
     * in the application.
     *
     * @param runnable
     * 		The action to run when the next animation starts.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator withStartAction(java.lang.Runnable runnable) {
        mPendingOnStartAction = runnable;
        if ((runnable != null) && (mAnimatorOnStartMap == null)) {
            mAnimatorOnStartMap = new java.util.HashMap<android.animation.Animator, java.lang.Runnable>();
        }
        return this;
    }

    /**
     * Specifies an action to take place when the next animation ends. The action is only
     * run if the animation ends normally; if the ViewPropertyAnimator is canceled during
     * that animation, the runnable will not run.
     * This method, along with {@link #withStartAction(Runnable)}, is intended to help facilitate
     * choreographing ViewPropertyAnimator animations with other animations or actions
     * in the application.
     *
     * <p>For example, the following code animates a view to x=200 and then back to 0:</p>
     * <pre>
     *     Runnable endAction = new Runnable() {
     *         public void run() {
     *             view.animate().x(0);
     *         }
     *     };
     *     view.animate().x(200).withEndAction(endAction);
     * </pre>
     *
     * @param runnable
     * 		The action to run when the next animation ends.
     * @return This object, allowing calls to methods in this class to be chained.
     */
    public android.view.ViewPropertyAnimator withEndAction(java.lang.Runnable runnable) {
        mPendingOnEndAction = runnable;
        if ((runnable != null) && (mAnimatorOnEndMap == null)) {
            mAnimatorOnEndMap = new java.util.HashMap<android.animation.Animator, java.lang.Runnable>();
        }
        return this;
    }

    boolean hasActions() {
        return (((mPendingSetupAction != null) || (mPendingCleanupAction != null)) || (mPendingOnStartAction != null)) || (mPendingOnEndAction != null);
    }

    /**
     * Starts the underlying Animator for a set of properties. We use a single animator that
     * simply runs from 0 to 1, and then use that fractional value to set each property
     * value accordingly.
     */
    private void startAnimation() {
        mView.setHasTransientState(true);
        android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofFloat(1.0F);
        java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder> nameValueList = ((java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder>) (mPendingAnimations.clone()));
        mPendingAnimations.clear();
        int propertyMask = 0;
        int propertyCount = nameValueList.size();
        for (int i = 0; i < propertyCount; ++i) {
            android.view.ViewPropertyAnimator.NameValuesHolder nameValuesHolder = nameValueList.get(i);
            propertyMask |= nameValuesHolder.mNameConstant;
        }
        mAnimatorMap.put(animator, new android.view.ViewPropertyAnimator.PropertyBundle(propertyMask, nameValueList));
        if (mPendingSetupAction != null) {
            mAnimatorSetupMap.put(animator, mPendingSetupAction);
            mPendingSetupAction = null;
        }
        if (mPendingCleanupAction != null) {
            mAnimatorCleanupMap.put(animator, mPendingCleanupAction);
            mPendingCleanupAction = null;
        }
        if (mPendingOnStartAction != null) {
            mAnimatorOnStartMap.put(animator, mPendingOnStartAction);
            mPendingOnStartAction = null;
        }
        if (mPendingOnEndAction != null) {
            mAnimatorOnEndMap.put(animator, mPendingOnEndAction);
            mPendingOnEndAction = null;
        }
        animator.addUpdateListener(mAnimatorEventListener);
        animator.addListener(mAnimatorEventListener);
        if (mStartDelaySet) {
            animator.setStartDelay(mStartDelay);
        }
        if (mDurationSet) {
            animator.setDuration(mDuration);
        }
        if (mInterpolatorSet) {
            animator.setInterpolator(mInterpolator);
        }
        animator.start();
    }

    /**
     * Utility function, called by the various x(), y(), etc. methods. This stores the
     * constant name for the property along with the from/delta values that will be used to
     * calculate and set the property during the animation. This structure is added to the
     * pending animations, awaiting the eventual start() of the underlying animator. A
     * Runnable is posted to start the animation, and any pending such Runnable is canceled
     * (which enables us to end up starting just one animator for all of the properties
     * specified at one time).
     *
     * @param constantName
     * 		The specifier for the property being animated
     * @param toValue
     * 		The value to which the property will animate
     */
    private void animateProperty(int constantName, float toValue) {
        float fromValue = getValue(constantName);
        float deltaValue = toValue - fromValue;
        animatePropertyBy(constantName, fromValue, deltaValue);
    }

    /**
     * Utility function, called by the various xBy(), yBy(), etc. methods. This method is
     * just like animateProperty(), except the value is an offset from the property's
     * current value, instead of an absolute "to" value.
     *
     * @param constantName
     * 		The specifier for the property being animated
     * @param byValue
     * 		The amount by which the property will change
     */
    private void animatePropertyBy(int constantName, float byValue) {
        float fromValue = getValue(constantName);
        animatePropertyBy(constantName, fromValue, byValue);
    }

    /**
     * Utility function, called by animateProperty() and animatePropertyBy(), which handles the
     * details of adding a pending animation and posting the request to start the animation.
     *
     * @param constantName
     * 		The specifier for the property being animated
     * @param startValue
     * 		The starting value of the property
     * @param byValue
     * 		The amount by which the property will change
     */
    private void animatePropertyBy(int constantName, float startValue, float byValue) {
        // First, cancel any existing animations on this property
        if (mAnimatorMap.size() > 0) {
            android.animation.Animator animatorToCancel = null;
            java.util.Set<android.animation.Animator> animatorSet = mAnimatorMap.keySet();
            for (android.animation.Animator runningAnim : animatorSet) {
                android.view.ViewPropertyAnimator.PropertyBundle bundle = mAnimatorMap.get(runningAnim);
                if (bundle.cancel(constantName)) {
                    // property was canceled - cancel the animation if it's now empty
                    // Note that it's safe to break out here because every new animation
                    // on a property will cancel a previous animation on that property, so
                    // there can only ever be one such animation running.
                    if (bundle.mPropertyMask == android.view.ViewPropertyAnimator.NONE) {
                        // the animation is no longer changing anything - cancel it
                        animatorToCancel = runningAnim;
                        break;
                    }
                }
            }
            if (animatorToCancel != null) {
                animatorToCancel.cancel();
            }
        }
        android.view.ViewPropertyAnimator.NameValuesHolder nameValuePair = new android.view.ViewPropertyAnimator.NameValuesHolder(constantName, startValue, byValue);
        mPendingAnimations.add(nameValuePair);
        mView.removeCallbacks(mAnimationStarter);
        mView.postOnAnimation(mAnimationStarter);
    }

    /**
     * This method handles setting the property values directly in the View object's fields.
     * propertyConstant tells it which property should be set, value is the value to set
     * the property to.
     *
     * @param propertyConstant
     * 		The property to be set
     * @param value
     * 		The value to set the property to
     */
    private void setValue(int propertyConstant, float value) {
        final android.graphics.RenderNode renderNode = mView.mRenderNode;
        switch (propertyConstant) {
            case android.view.ViewPropertyAnimator.TRANSLATION_X :
                renderNode.setTranslationX(value);
                break;
            case android.view.ViewPropertyAnimator.TRANSLATION_Y :
                renderNode.setTranslationY(value);
                break;
            case android.view.ViewPropertyAnimator.TRANSLATION_Z :
                renderNode.setTranslationZ(value);
                break;
            case android.view.ViewPropertyAnimator.ROTATION :
                renderNode.setRotationZ(value);
                break;
            case android.view.ViewPropertyAnimator.ROTATION_X :
                renderNode.setRotationX(value);
                break;
            case android.view.ViewPropertyAnimator.ROTATION_Y :
                renderNode.setRotationY(value);
                break;
            case android.view.ViewPropertyAnimator.SCALE_X :
                renderNode.setScaleX(value);
                break;
            case android.view.ViewPropertyAnimator.SCALE_Y :
                renderNode.setScaleY(value);
                break;
            case android.view.ViewPropertyAnimator.X :
                renderNode.setTranslationX(value - mView.mLeft);
                break;
            case android.view.ViewPropertyAnimator.Y :
                renderNode.setTranslationY(value - mView.mTop);
                break;
            case android.view.ViewPropertyAnimator.Z :
                renderNode.setTranslationZ(value - renderNode.getElevation());
                break;
            case android.view.ViewPropertyAnimator.ALPHA :
                mView.setAlphaInternal(value);
                renderNode.setAlpha(value);
                break;
        }
    }

    /**
     * This method gets the value of the named property from the View object.
     *
     * @param propertyConstant
     * 		The property whose value should be returned
     * @return float The value of the named property
     */
    private float getValue(int propertyConstant) {
        final android.graphics.RenderNode node = mView.mRenderNode;
        switch (propertyConstant) {
            case android.view.ViewPropertyAnimator.TRANSLATION_X :
                return node.getTranslationX();
            case android.view.ViewPropertyAnimator.TRANSLATION_Y :
                return node.getTranslationY();
            case android.view.ViewPropertyAnimator.TRANSLATION_Z :
                return node.getTranslationZ();
            case android.view.ViewPropertyAnimator.ROTATION :
                return node.getRotationZ();
            case android.view.ViewPropertyAnimator.ROTATION_X :
                return node.getRotationX();
            case android.view.ViewPropertyAnimator.ROTATION_Y :
                return node.getRotationY();
            case android.view.ViewPropertyAnimator.SCALE_X :
                return node.getScaleX();
            case android.view.ViewPropertyAnimator.SCALE_Y :
                return node.getScaleY();
            case android.view.ViewPropertyAnimator.X :
                return mView.mLeft + node.getTranslationX();
            case android.view.ViewPropertyAnimator.Y :
                return mView.mTop + node.getTranslationY();
            case android.view.ViewPropertyAnimator.Z :
                return node.getElevation() + node.getTranslationZ();
            case android.view.ViewPropertyAnimator.ALPHA :
                return mView.getAlpha();
        }
        return 0;
    }

    /**
     * Utility class that handles the various Animator events. The only ones we care
     * about are the end event (which we use to clean up the animator map when an animator
     * finishes) and the update event (which we use to calculate the current value of each
     * property and then set it on the view object).
     */
    private class AnimatorEventListener implements android.animation.Animator.AnimatorListener , android.animation.ValueAnimator.AnimatorUpdateListener {
        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animation) {
            if (mAnimatorSetupMap != null) {
                java.lang.Runnable r = mAnimatorSetupMap.get(animation);
                if (r != null) {
                    r.run();
                }
                mAnimatorSetupMap.remove(animation);
            }
            if (mAnimatorOnStartMap != null) {
                java.lang.Runnable r = mAnimatorOnStartMap.get(animation);
                if (r != null) {
                    r.run();
                }
                mAnimatorOnStartMap.remove(animation);
            }
            if (mListener != null) {
                mListener.onAnimationStart(animation);
            }
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
            if (mListener != null) {
                mListener.onAnimationCancel(animation);
            }
            if (mAnimatorOnEndMap != null) {
                mAnimatorOnEndMap.remove(animation);
            }
        }

        @java.lang.Override
        public void onAnimationRepeat(android.animation.Animator animation) {
            if (mListener != null) {
                mListener.onAnimationRepeat(animation);
            }
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            mView.setHasTransientState(false);
            if (mAnimatorCleanupMap != null) {
                java.lang.Runnable r = mAnimatorCleanupMap.get(animation);
                if (r != null) {
                    r.run();
                }
                mAnimatorCleanupMap.remove(animation);
            }
            if (mListener != null) {
                mListener.onAnimationEnd(animation);
            }
            if (mAnimatorOnEndMap != null) {
                java.lang.Runnable r = mAnimatorOnEndMap.get(animation);
                if (r != null) {
                    r.run();
                }
                mAnimatorOnEndMap.remove(animation);
            }
            mAnimatorMap.remove(animation);
        }

        /**
         * Calculate the current value for each property and set it on the view. Invalidate
         * the view object appropriately, depending on which properties are being animated.
         *
         * @param animation
         * 		The animator associated with the properties that need to be
         * 		set. This animator holds the animation fraction which we will use to calculate
         * 		the current value of each property.
         */
        @java.lang.Override
        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
            android.view.ViewPropertyAnimator.PropertyBundle propertyBundle = mAnimatorMap.get(animation);
            if (propertyBundle == null) {
                // Shouldn't happen, but just to play it safe
                return;
            }
            boolean hardwareAccelerated = mView.isHardwareAccelerated();
            if (!hardwareAccelerated) {
                mView.invalidateParentCaches();
            }
            float fraction = animation.getAnimatedFraction();
            int propertyMask = propertyBundle.mPropertyMask;
            if ((propertyMask & android.view.ViewPropertyAnimator.TRANSFORM_MASK) != 0) {
                mView.invalidateViewProperty(hardwareAccelerated, false);
            }
            java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder> valueList = propertyBundle.mNameValuesHolder;
            if (valueList != null) {
                int count = valueList.size();
                for (int i = 0; i < count; ++i) {
                    android.view.ViewPropertyAnimator.NameValuesHolder values = valueList.get(i);
                    float value = values.mFromValue + (fraction * values.mDeltaValue);
                    setValue(values.mNameConstant, value);
                }
            }
            if ((propertyMask & android.view.ViewPropertyAnimator.TRANSFORM_MASK) != 0) {
                if (!hardwareAccelerated) {
                    mView.mPrivateFlags |= android.view.View.PFLAG_DRAWN;// force another invalidation

                }
            }
            mView.invalidateViewProperty(false, false);
            if (mUpdateListener != null) {
                mUpdateListener.onAnimationUpdate(animation);
            }
        }
    }
}

