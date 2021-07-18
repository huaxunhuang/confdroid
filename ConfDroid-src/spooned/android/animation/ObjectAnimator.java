/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.animation;


/**
 * This subclass of {@link ValueAnimator} provides support for animating properties on target objects.
 * The constructors of this class take parameters to define the target object that will be animated
 * as well as the name of the property that will be animated. Appropriate set/get functions
 * are then determined internally and the animation will call these functions as necessary to
 * animate the property.
 *
 * <p>Animators can be created from either code or resource files, as shown here:</p>
 *
 * {@sample development/samples/ApiDemos/res/anim/object_animator.xml ObjectAnimatorResources}
 *
 * <p>When using resource files, it is possible to use {@link PropertyValuesHolder} and
 * {@link Keyframe} to create more complex animations. Using PropertyValuesHolders
 * allows animators to animate several properties in parallel, as shown in this sample:</p>
 *
 * {@sample development/samples/ApiDemos/res/anim/object_animator_pvh.xml
 * PropertyValuesHolderResources}
 *
 * <p>Using Keyframes allows animations to follow more complex paths from the start
 * to the end values. Note that you can specify explicit fractional values (from 0 to 1) for
 * each keyframe to determine when, in the overall duration, the animation should arrive at that
 * value. Alternatively, you can leave the fractions off and the keyframes will be equally
 * distributed within the total duration. Also, a keyframe with no value will derive its value
 * from the target object when the animator starts, just like animators with only one
 * value specified. In addition, an optional interpolator can be specified. The interpolator will
 * be applied on the interval between the keyframe that the interpolator is set on and the previous
 * keyframe. When no interpolator is supplied, the default {@link AccelerateDecelerateInterpolator}
 * will be used. </p>
 *
 * {@sample development/samples/ApiDemos/res/anim/object_animator_pvh_kf_interpolated.xml KeyframeResources}
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about animating with {@code ObjectAnimator}, read the
 * <a href="{@docRoot }guide/topics/graphics/prop-animation.html#object-animator">Property
 * Animation</a> developer guide.</p>
 * </div>
 *
 * @see #setPropertyName(String)
 */
public final class ObjectAnimator extends android.animation.ValueAnimator {
    private static final java.lang.String LOG_TAG = "ObjectAnimator";

    private static final boolean DBG = false;

    /**
     * A weak reference to the target object on which the property exists, set
     * in the constructor. We'll cancel the animation if this goes away.
     */
    private java.lang.ref.WeakReference<java.lang.Object> mTarget;

    private java.lang.String mPropertyName;

    private android.util.Property mProperty;

    private boolean mAutoCancel = false;

    /**
     * Sets the name of the property that will be animated. This name is used to derive
     * a setter function that will be called to set animated values.
     * For example, a property name of <code>foo</code> will result
     * in a call to the function <code>setFoo()</code> on the target object. If either
     * <code>valueFrom</code> or <code>valueTo</code> is null, then a getter function will
     * also be derived and called.
     *
     * <p>For best performance of the mechanism that calls the setter function determined by the
     * name of the property being animated, use <code>float</code> or <code>int</code> typed values,
     * and make the setter function for those properties have a <code>void</code> return value. This
     * will cause the code to take an optimized path for these constrained circumstances. Other
     * property types and return types will work, but will have more overhead in processing
     * the requests due to normal reflection mechanisms.</p>
     *
     * <p>Note that the setter function derived from this property name
     * must take the same parameter type as the
     * <code>valueFrom</code> and <code>valueTo</code> properties, otherwise the call to
     * the setter function will fail.</p>
     *
     * <p>If this ObjectAnimator has been set up to animate several properties together,
     * using more than one PropertyValuesHolder objects, then setting the propertyName simply
     * sets the propertyName in the first of those PropertyValuesHolder objects.</p>
     *
     * @param propertyName
     * 		The name of the property being animated. Should not be null.
     */
    public void setPropertyName(@android.annotation.NonNull
    java.lang.String propertyName) {
        // mValues could be null if this is being constructed piecemeal. Just record the
        // propertyName to be used later when setValues() is called if so.
        if (mValues != null) {
            android.animation.PropertyValuesHolder valuesHolder = mValues[0];
            java.lang.String oldName = valuesHolder.getPropertyName();
            valuesHolder.setPropertyName(propertyName);
            mValuesMap.remove(oldName);
            mValuesMap.put(propertyName, valuesHolder);
        }
        mPropertyName = propertyName;
        // New property/values/target should cause re-initialization prior to starting
        mInitialized = false;
    }

    /**
     * Sets the property that will be animated. Property objects will take precedence over
     * properties specified by the {@link #setPropertyName(String)} method. Animations should
     * be set up to use one or the other, not both.
     *
     * @param property
     * 		The property being animated. Should not be null.
     */
    public void setProperty(@android.annotation.NonNull
    android.util.Property property) {
        // mValues could be null if this is being constructed piecemeal. Just record the
        // propertyName to be used later when setValues() is called if so.
        if (mValues != null) {
            android.animation.PropertyValuesHolder valuesHolder = mValues[0];
            java.lang.String oldName = valuesHolder.getPropertyName();
            valuesHolder.setProperty(property);
            mValuesMap.remove(oldName);
            mValuesMap.put(mPropertyName, valuesHolder);
        }
        if (mProperty != null) {
            mPropertyName = property.getName();
        }
        mProperty = property;
        // New property/values/target should cause re-initialization prior to starting
        mInitialized = false;
    }

    /**
     * Gets the name of the property that will be animated. This name will be used to derive
     * a setter function that will be called to set animated values.
     * For example, a property name of <code>foo</code> will result
     * in a call to the function <code>setFoo()</code> on the target object. If either
     * <code>valueFrom</code> or <code>valueTo</code> is null, then a getter function will
     * also be derived and called.
     *
     * <p>If this animator was created with a {@link Property} object instead of the
     * string name of a property, then this method will return the {@link Property#getName() name} of that Property object instead. If this animator was
     * created with one or more {@link PropertyValuesHolder} objects, then this method
     * will return the {@link PropertyValuesHolder#getPropertyName() name} of that
     * object (if there was just one) or a comma-separated list of all of the
     * names (if there are more than one).</p>
     */
    @android.annotation.Nullable
    public java.lang.String getPropertyName() {
        java.lang.String propertyName = null;
        if (mPropertyName != null) {
            propertyName = mPropertyName;
        } else
            if (mProperty != null) {
                propertyName = mProperty.getName();
            } else
                if ((mValues != null) && (mValues.length > 0)) {
                    for (int i = 0; i < mValues.length; ++i) {
                        if (i == 0) {
                            propertyName = "";
                        } else {
                            propertyName += ",";
                        }
                        propertyName += mValues[i].getPropertyName();
                    }
                }


        return propertyName;
    }

    @java.lang.Override
    java.lang.String getNameForTrace() {
        return "animator:" + getPropertyName();
    }

    /**
     * Creates a new ObjectAnimator object. This default constructor is primarily for
     * use internally; the other constructors which take parameters are more generally
     * useful.
     */
    public ObjectAnimator() {
    }

    /**
     * Private utility constructor that initializes the target object and name of the
     * property being animated.
     *
     * @param target
     * 		The object whose property is to be animated. This object should
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter.
     * @param propertyName
     * 		The name of the property being animated.
     */
    private ObjectAnimator(java.lang.Object target, java.lang.String propertyName) {
        setTarget(target);
        setPropertyName(propertyName);
    }

    /**
     * Private utility constructor that initializes the target object and property being animated.
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated.
     */
    private <T> ObjectAnimator(T target, android.util.Property<T, ?> property) {
        setTarget(target);
        setProperty(property);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between int values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated. This object should
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter.
     * @param propertyName
     * 		The name of the property being animated.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static android.animation.ObjectAnimator ofInt(java.lang.Object target, java.lang.String propertyName, int... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator(target, propertyName);
        anim.setIntValues(values);
        return anim;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a <code>Path</code>
     * using two properties. A <code>Path</code></> animation moves in two dimensions, animating
     * coordinates <code>(x, y)</code> together to follow the line. In this variation, the
     * coordinates are integers that are set to separate properties designated by
     * <code>xPropertyName</code> and <code>yPropertyName</code>.
     *
     * @param target
     * 		The object whose properties are to be animated. This object should
     * 		have public methods on it called <code>setNameX()</code> and
     * 		<code>setNameY</code>, where <code>nameX</code> and <code>nameY</code>
     * 		are the value of <code>xPropertyName</code> and <code>yPropertyName</code>
     * 		parameters, respectively.
     * @param xPropertyName
     * 		The name of the property for the x coordinate being animated.
     * @param yPropertyName
     * 		The name of the property for the y coordinate being animated.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    public static android.animation.ObjectAnimator ofInt(java.lang.Object target, java.lang.String xPropertyName, java.lang.String yPropertyName, android.graphics.Path path) {
        android.animation.PathKeyframes keyframes = android.animation.KeyframeSet.ofPath(path);
        android.animation.PropertyValuesHolder x = android.animation.PropertyValuesHolder.ofKeyframes(xPropertyName, keyframes.createXIntKeyframes());
        android.animation.PropertyValuesHolder y = android.animation.PropertyValuesHolder.ofKeyframes(yPropertyName, keyframes.createYIntKeyframes());
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, x, y);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between int values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static <T> android.animation.ObjectAnimator ofInt(T target, android.util.Property<T, java.lang.Integer> property, int... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator(target, property);
        anim.setIntValues(values);
        return anim;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a <code>Path</code>
     * using two properties.  A <code>Path</code></> animation moves in two dimensions, animating
     * coordinates <code>(x, y)</code> together to follow the line. In this variation, the
     * coordinates are integers that are set to separate properties, <code>xProperty</code> and
     * <code>yProperty</code>.
     *
     * @param target
     * 		The object whose properties are to be animated.
     * @param xProperty
     * 		The property for the x coordinate being animated.
     * @param yProperty
     * 		The property for the y coordinate being animated.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    public static <T> android.animation.ObjectAnimator ofInt(T target, android.util.Property<T, java.lang.Integer> xProperty, android.util.Property<T, java.lang.Integer> yProperty, android.graphics.Path path) {
        android.animation.PathKeyframes keyframes = android.animation.KeyframeSet.ofPath(path);
        android.animation.PropertyValuesHolder x = android.animation.PropertyValuesHolder.ofKeyframes(xProperty, keyframes.createXIntKeyframes());
        android.animation.PropertyValuesHolder y = android.animation.PropertyValuesHolder.ofKeyframes(yProperty, keyframes.createYIntKeyframes());
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, x, y);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates over int values for a multiple
     * parameters setter. Only public methods that take only int parameters are supported.
     * Each <code>int[]</code> contains a complete set of parameters to the setter method.
     * At least two <code>int[]</code> values must be provided, a start and end. More than two
     * values imply a starting value, values to animate through along the way, and an ending
     * value (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated. This object may
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter. <code>propertyName</code> may also
     * 		be the case-sensitive complete name of the public setter method.
     * @param propertyName
     * 		The name of the property being animated or the name of the setter method.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static android.animation.ObjectAnimator ofMultiInt(java.lang.Object target, java.lang.String propertyName, int[][] values) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofMultiInt(propertyName, values);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates the target using a multi-int setter
     * along the given <code>Path</code>. A <code>Path</code></> animation moves in two dimensions,
     * animating coordinates <code>(x, y)</code> together to follow the line. In this variation, the
     * coordinates are integer x and y coordinates used in the first and second parameter of the
     * setter, respectively.
     *
     * @param target
     * 		The object whose property is to be animated. This object may
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter. <code>propertyName</code> may also
     * 		be the case-sensitive complete name of the public setter method.
     * @param propertyName
     * 		The name of the property being animated or the name of the setter method.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    public static android.animation.ObjectAnimator ofMultiInt(java.lang.Object target, java.lang.String propertyName, android.graphics.Path path) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofMultiInt(propertyName, path);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates over values for a multiple int
     * parameters setter. Only public methods that take only int parameters are supported.
     * <p>At least two values must be provided, a start and end. More than two
     * values imply a starting value, values to animate through along the way, and an ending
     * value (these values will be distributed evenly across the duration of the animation).</p>
     *
     * @param target
     * 		The object whose property is to be animated. This object may
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter. <code>propertyName</code> may also
     * 		be the case-sensitive complete name of the public setter method.
     * @param propertyName
     * 		The name of the property being animated or the name of the setter method.
     * @param converter
     * 		Converts T objects into int parameters for the multi-value setter.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @java.lang.SafeVarargs
    public static <T> android.animation.ObjectAnimator ofMultiInt(java.lang.Object target, java.lang.String propertyName, android.animation.TypeConverter<T, int[]> converter, android.animation.TypeEvaluator<T> evaluator, T... values) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofMultiInt(propertyName, converter, evaluator, values);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between color values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated. This object should
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter.
     * @param propertyName
     * 		The name of the property being animated.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static android.animation.ObjectAnimator ofArgb(java.lang.Object target, java.lang.String propertyName, int... values) {
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofInt(target, propertyName, values);
        animator.setEvaluator(android.animation.ArgbEvaluator.getInstance());
        return animator;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between color values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static <T> android.animation.ObjectAnimator ofArgb(T target, android.util.Property<T, java.lang.Integer> property, int... values) {
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofInt(target, property, values);
        animator.setEvaluator(android.animation.ArgbEvaluator.getInstance());
        return animator;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between float values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated. This object should
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter.
     * @param propertyName
     * 		The name of the property being animated.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static android.animation.ObjectAnimator ofFloat(java.lang.Object target, java.lang.String propertyName, float... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator(target, propertyName);
        anim.setFloatValues(values);
        return anim;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a <code>Path</code>
     * using two properties. A <code>Path</code></> animation moves in two dimensions, animating
     * coordinates <code>(x, y)</code> together to follow the line. In this variation, the
     * coordinates are floats that are set to separate properties designated by
     * <code>xPropertyName</code> and <code>yPropertyName</code>.
     *
     * @param target
     * 		The object whose properties are to be animated. This object should
     * 		have public methods on it called <code>setNameX()</code> and
     * 		<code>setNameY</code>, where <code>nameX</code> and <code>nameY</code>
     * 		are the value of the <code>xPropertyName</code> and <code>yPropertyName</code>
     * 		parameters, respectively.
     * @param xPropertyName
     * 		The name of the property for the x coordinate being animated.
     * @param yPropertyName
     * 		The name of the property for the y coordinate being animated.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    public static android.animation.ObjectAnimator ofFloat(java.lang.Object target, java.lang.String xPropertyName, java.lang.String yPropertyName, android.graphics.Path path) {
        android.animation.PathKeyframes keyframes = android.animation.KeyframeSet.ofPath(path);
        android.animation.PropertyValuesHolder x = android.animation.PropertyValuesHolder.ofKeyframes(xPropertyName, keyframes.createXFloatKeyframes());
        android.animation.PropertyValuesHolder y = android.animation.PropertyValuesHolder.ofKeyframes(yPropertyName, keyframes.createYFloatKeyframes());
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, x, y);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between float values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static <T> android.animation.ObjectAnimator ofFloat(T target, android.util.Property<T, java.lang.Float> property, float... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator(target, property);
        anim.setFloatValues(values);
        return anim;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a <code>Path</code>
     * using two properties. A <code>Path</code></> animation moves in two dimensions, animating
     * coordinates <code>(x, y)</code> together to follow the line. In this variation, the
     * coordinates are floats that are set to separate properties, <code>xProperty</code> and
     * <code>yProperty</code>.
     *
     * @param target
     * 		The object whose properties are to be animated.
     * @param xProperty
     * 		The property for the x coordinate being animated.
     * @param yProperty
     * 		The property for the y coordinate being animated.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    public static <T> android.animation.ObjectAnimator ofFloat(T target, android.util.Property<T, java.lang.Float> xProperty, android.util.Property<T, java.lang.Float> yProperty, android.graphics.Path path) {
        android.animation.PathKeyframes keyframes = android.animation.KeyframeSet.ofPath(path);
        android.animation.PropertyValuesHolder x = android.animation.PropertyValuesHolder.ofKeyframes(xProperty, keyframes.createXFloatKeyframes());
        android.animation.PropertyValuesHolder y = android.animation.PropertyValuesHolder.ofKeyframes(yProperty, keyframes.createYFloatKeyframes());
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, x, y);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates over float values for a multiple
     * parameters setter. Only public methods that take only float parameters are supported.
     * Each <code>float[]</code> contains a complete set of parameters to the setter method.
     * At least two <code>float[]</code> values must be provided, a start and end. More than two
     * values imply a starting value, values to animate through along the way, and an ending
     * value (these values will be distributed evenly across the duration of the animation).
     *
     * @param target
     * 		The object whose property is to be animated. This object may
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter. <code>propertyName</code> may also
     * 		be the case-sensitive complete name of the public setter method.
     * @param propertyName
     * 		The name of the property being animated or the name of the setter method.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static android.animation.ObjectAnimator ofMultiFloat(java.lang.Object target, java.lang.String propertyName, float[][] values) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofMultiFloat(propertyName, values);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates the target using a multi-float setter
     * along the given <code>Path</code>. A <code>Path</code></> animation moves in two dimensions,
     * animating coordinates <code>(x, y)</code> together to follow the line. In this variation, the
     * coordinates are float x and y coordinates used in the first and second parameter of the
     * setter, respectively.
     *
     * @param target
     * 		The object whose property is to be animated. This object may
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter. <code>propertyName</code> may also
     * 		be the case-sensitive complete name of the public setter method.
     * @param propertyName
     * 		The name of the property being animated or the name of the setter method.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    public static android.animation.ObjectAnimator ofMultiFloat(java.lang.Object target, java.lang.String propertyName, android.graphics.Path path) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofMultiFloat(propertyName, path);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates over values for a multiple float
     * parameters setter. Only public methods that take only float parameters are supported.
     * <p>At least two values must be provided, a start and end. More than two
     * values imply a starting value, values to animate through along the way, and an ending
     * value (these values will be distributed evenly across the duration of the animation).</p>
     *
     * @param target
     * 		The object whose property is to be animated. This object may
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter. <code>propertyName</code> may also
     * 		be the case-sensitive complete name of the public setter method.
     * @param propertyName
     * 		The name of the property being animated or the name of the setter method.
     * @param converter
     * 		Converts T objects into float parameters for the multi-value setter.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @java.lang.SafeVarargs
    public static <T> android.animation.ObjectAnimator ofMultiFloat(java.lang.Object target, java.lang.String propertyName, android.animation.TypeConverter<T, float[]> converter, android.animation.TypeEvaluator<T> evaluator, T... values) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofMultiFloat(propertyName, converter, evaluator, values);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between Object values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * <p><strong>Note:</strong> The values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the animator. If the objects will be mutated externally after
     * this method is called, callers should pass a copy of those objects instead.
     *
     * @param target
     * 		The object whose property is to be animated. This object should
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter.
     * @param propertyName
     * 		The name of the property being animated.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    public static android.animation.ObjectAnimator ofObject(java.lang.Object target, java.lang.String propertyName, android.animation.TypeEvaluator evaluator, java.lang.Object... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator(target, propertyName);
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates a property along a <code>Path</code>.
     * A <code>Path</code></> animation moves in two dimensions, animating coordinates
     * <code>(x, y)</code> together to follow the line. This variant animates the coordinates
     * in a <code>PointF</code> to follow the <code>Path</code>. If the <code>Property</code>
     * associated with <code>propertyName</code> uses a type other than <code>PointF</code>,
     * <code>converter</code> can be used to change from <code>PointF</code> to the type
     * associated with the <code>Property</code>.
     *
     * @param target
     * 		The object whose property is to be animated. This object should
     * 		have a public method on it called <code>setName()</code>, where <code>name</code> is
     * 		the value of the <code>propertyName</code> parameter.
     * @param propertyName
     * 		The name of the property being animated.
     * @param converter
     * 		Converts a PointF to the type associated with the setter. May be
     * 		null if conversion is unnecessary.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    @android.annotation.NonNull
    public static android.animation.ObjectAnimator ofObject(java.lang.Object target, java.lang.String propertyName, @android.annotation.Nullable
    android.animation.TypeConverter<android.graphics.PointF, ?> converter, android.graphics.Path path) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofObject(propertyName, converter, path);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between Object values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     *
     * <p><strong>Note:</strong> The values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the animator. If the objects will be mutated externally after
     * this method is called, callers should pass a copy of those objects instead.
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @android.annotation.NonNull
    @java.lang.SafeVarargs
    public static <T, V> android.animation.ObjectAnimator ofObject(T target, android.util.Property<T, V> property, android.animation.TypeEvaluator<V> evaluator, V... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator(target, property);
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between Object values. A single
     * value implies that that value is the one being animated to, in which case the start value
     * will be derived from the property being animated and the target object when {@link #start()}
     * is called for the first time. Two values imply starting and ending values. More than two
     * values imply a starting value, values to animate through along the way, and an ending value
     * (these values will be distributed evenly across the duration of the animation).
     * This variant supplies a <code>TypeConverter</code> to convert from the animated values to the
     * type of the property. If only one value is supplied, the <code>TypeConverter</code> must be a
     * {@link android.animation.BidirectionalTypeConverter} to retrieve the current value.
     *
     * <p><strong>Note:</strong> The values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the animator. If the objects will be mutated externally after
     * this method is called, callers should pass a copy of those objects instead.
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated.
     * @param converter
     * 		Converts the animated object to the Property type.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @android.annotation.NonNull
    @java.lang.SafeVarargs
    public static <T, V, P> android.animation.ObjectAnimator ofObject(T target, android.util.Property<T, P> property, android.animation.TypeConverter<V, P> converter, android.animation.TypeEvaluator<V> evaluator, V... values) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofObject(property, converter, evaluator, values);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates a property along a <code>Path</code>.
     * A <code>Path</code></> animation moves in two dimensions, animating coordinates
     * <code>(x, y)</code> together to follow the line. This variant animates the coordinates
     * in a <code>PointF</code> to follow the <code>Path</code>. If <code>property</code>
     * uses a type other than <code>PointF</code>, <code>converter</code> can be used to change
     * from <code>PointF</code> to the type associated with the <code>Property</code>.
     *
     * <p>The PointF passed to <code>converter</code> or <code>property</code>, if
     * <code>converter</code> is <code>null</code>, is reused on each animation frame and should
     * not be stored by the setter or TypeConverter.</p>
     *
     * @param target
     * 		The object whose property is to be animated.
     * @param property
     * 		The property being animated. Should not be null.
     * @param converter
     * 		Converts a PointF to the type associated with the setter. May be
     * 		null if conversion is unnecessary.
     * @param path
     * 		The <code>Path</code> to animate values along.
     * @return An ObjectAnimator object that is set up to animate along <code>path</code>.
     */
    @android.annotation.NonNull
    public static <T, V> android.animation.ObjectAnimator ofObject(T target, @android.annotation.NonNull
    android.util.Property<T, V> property, @android.annotation.Nullable
    android.animation.TypeConverter<android.graphics.PointF, V> converter, android.graphics.Path path) {
        android.animation.PropertyValuesHolder pvh = android.animation.PropertyValuesHolder.ofObject(property, converter, path);
        return android.animation.ObjectAnimator.ofPropertyValuesHolder(target, pvh);
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between the sets of values specified
     * in <code>PropertyValueHolder</code> objects. This variant should be used when animating
     * several properties at once with the same ObjectAnimator, since PropertyValuesHolder allows
     * you to associate a set of animation values with a property name.
     *
     * @param target
     * 		The object whose property is to be animated. Depending on how the
     * 		PropertyValuesObjects were constructed, the target object should either have the {@link android.util.Property} objects used to construct the PropertyValuesHolder objects or (if the
     * 		PropertyValuesHOlder objects were created with property names) the target object should have
     * 		public methods on it called <code>setName()</code>, where <code>name</code> is the name of
     * 		the property passed in as the <code>propertyName</code> parameter for each of the
     * 		PropertyValuesHolder objects.
     * @param values
     * 		A set of PropertyValuesHolder objects whose values will be animated between
     * 		over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @android.annotation.NonNull
    public static android.animation.ObjectAnimator ofPropertyValuesHolder(java.lang.Object target, android.animation.PropertyValuesHolder... values) {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator();
        anim.setTarget(target);
        anim.setValues(values);
        return anim;
    }

    @java.lang.Override
    public void setIntValues(int... values) {
        if ((mValues == null) || (mValues.length == 0)) {
            // No values yet - this animator is being constructed piecemeal. Init the values with
            // whatever the current propertyName is
            if (mProperty != null) {
                setValues(android.animation.PropertyValuesHolder.ofInt(mProperty, values));
            } else {
                setValues(android.animation.PropertyValuesHolder.ofInt(mPropertyName, values));
            }
        } else {
            super.setIntValues(values);
        }
    }

    @java.lang.Override
    public void setFloatValues(float... values) {
        if ((mValues == null) || (mValues.length == 0)) {
            // No values yet - this animator is being constructed piecemeal. Init the values with
            // whatever the current propertyName is
            if (mProperty != null) {
                setValues(android.animation.PropertyValuesHolder.ofFloat(mProperty, values));
            } else {
                setValues(android.animation.PropertyValuesHolder.ofFloat(mPropertyName, values));
            }
        } else {
            super.setFloatValues(values);
        }
    }

    @java.lang.Override
    public void setObjectValues(java.lang.Object... values) {
        if ((mValues == null) || (mValues.length == 0)) {
            // No values yet - this animator is being constructed piecemeal. Init the values with
            // whatever the current propertyName is
            if (mProperty != null) {
                setValues(android.animation.PropertyValuesHolder.ofObject(mProperty, ((android.animation.TypeEvaluator) (null)), values));
            } else {
                setValues(android.animation.PropertyValuesHolder.ofObject(mPropertyName, ((android.animation.TypeEvaluator) (null)), values));
            }
        } else {
            super.setObjectValues(values);
        }
    }

    /**
     * autoCancel controls whether an ObjectAnimator will be canceled automatically
     * when any other ObjectAnimator with the same target and properties is started.
     * Setting this flag may make it easier to run different animators on the same target
     * object without having to keep track of whether there are conflicting animators that
     * need to be manually canceled. Canceling animators must have the same exact set of
     * target properties, in the same order.
     *
     * @param cancel
     * 		Whether future ObjectAnimators with the same target and properties
     * 		as this ObjectAnimator will cause this ObjectAnimator to be canceled.
     */
    public void setAutoCancel(boolean cancel) {
        mAutoCancel = cancel;
    }

    private boolean hasSameTargetAndProperties(@android.annotation.Nullable
    android.animation.Animator anim) {
        if (anim instanceof android.animation.ObjectAnimator) {
            android.animation.PropertyValuesHolder[] theirValues = ((android.animation.ObjectAnimator) (anim)).getValues();
            if ((((android.animation.ObjectAnimator) (anim)).getTarget() == getTarget()) && (mValues.length == theirValues.length)) {
                for (int i = 0; i < mValues.length; ++i) {
                    android.animation.PropertyValuesHolder pvhMine = mValues[i];
                    android.animation.PropertyValuesHolder pvhTheirs = theirValues[i];
                    if ((pvhMine.getPropertyName() == null) || (!pvhMine.getPropertyName().equals(pvhTheirs.getPropertyName()))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void start() {
        android.animation.AnimationHandler.getInstance().autoCancelBasedOn(this);
        if (android.animation.ObjectAnimator.DBG) {
            android.util.Log.d(android.animation.ObjectAnimator.LOG_TAG, (("Anim target, duration: " + getTarget()) + ", ") + getDuration());
            for (int i = 0; i < mValues.length; ++i) {
                android.animation.PropertyValuesHolder pvh = mValues[i];
                android.util.Log.d(android.animation.ObjectAnimator.LOG_TAG, (((((("   Values[" + i) + "]: ") + pvh.getPropertyName()) + ", ") + pvh.mKeyframes.getValue(0)) + ", ") + pvh.mKeyframes.getValue(1));
            }
        }
        super.start();
    }

    boolean shouldAutoCancel(android.animation.AnimationHandler.AnimationFrameCallback anim) {
        if (anim == null) {
            return false;
        }
        if (anim instanceof android.animation.ObjectAnimator) {
            android.animation.ObjectAnimator objAnim = ((android.animation.ObjectAnimator) (anim));
            if (objAnim.mAutoCancel && hasSameTargetAndProperties(objAnim)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function is called immediately before processing the first animation
     * frame of an animation. If there is a nonzero <code>startDelay</code>, the
     * function is called after that delay ends.
     * It takes care of the final initialization steps for the
     * animation. This includes setting mEvaluator, if the user has not yet
     * set it up, and the setter/getter methods, if the user did not supply
     * them.
     *
     *  <p>Overriders of this method should call the superclass method to cause
     *  internal mechanisms to be set up correctly.</p>
     */
    @android.annotation.CallSuper
    @java.lang.Override
    void initAnimation() {
        if (!mInitialized) {
            // mValueType may change due to setter/getter setup; do this before calling super.init(),
            // which uses mValueType to set up the default type evaluator.
            final java.lang.Object target = getTarget();
            if (target != null) {
                final int numValues = mValues.length;
                for (int i = 0; i < numValues; ++i) {
                    mValues[i].setupSetterAndGetter(target);
                }
            }
            super.initAnimation();
        }
    }

    /**
     * Sets the length of the animation. The default duration is 300 milliseconds.
     *
     * @param duration
     * 		The length of the animation, in milliseconds.
     * @return ObjectAnimator The object called with setDuration(). This return
    value makes it easier to compose statements together that construct and then set the
    duration, as in
    <code>ObjectAnimator.ofInt(target, propertyName, 0, 10).setDuration(500).start()</code>.
     */
    @java.lang.Override
    @android.annotation.NonNull
    public android.animation.ObjectAnimator setDuration(long duration) {
        super.setDuration(duration);
        return this;
    }

    /**
     * The target object whose property will be animated by this animation
     *
     * @return The object being animated
     */
    @android.annotation.Nullable
    public java.lang.Object getTarget() {
        return mTarget == null ? null : mTarget.get();
    }

    @java.lang.Override
    public void setTarget(@android.annotation.Nullable
    java.lang.Object target) {
        final java.lang.Object oldTarget = getTarget();
        if (oldTarget != target) {
            if (isStarted()) {
                cancel();
            }
            mTarget = (target == null) ? null : new java.lang.ref.WeakReference<java.lang.Object>(target);
            // New target should cause re-initialization prior to starting
            mInitialized = false;
        }
    }

    @java.lang.Override
    public void setupStartValues() {
        initAnimation();
        final java.lang.Object target = getTarget();
        if (target != null) {
            final int numValues = mValues.length;
            for (int i = 0; i < numValues; ++i) {
                mValues[i].setupStartValue(target);
            }
        }
    }

    @java.lang.Override
    public void setupEndValues() {
        initAnimation();
        final java.lang.Object target = getTarget();
        if (target != null) {
            final int numValues = mValues.length;
            for (int i = 0; i < numValues; ++i) {
                mValues[i].setupEndValue(target);
            }
        }
    }

    /**
     * This method is called with the elapsed fraction of the animation during every
     * animation frame. This function turns the elapsed fraction into an interpolated fraction
     * and then into an animated value (from the evaluator. The function is called mostly during
     * animation updates, but it is also called when the <code>end()</code>
     * function is called, to set the final value on the property.
     *
     * <p>Overrides of this method must call the superclass to perform the calculation
     * of the animated value.</p>
     *
     * @param fraction
     * 		The elapsed fraction of the animation.
     */
    @android.annotation.CallSuper
    @java.lang.Override
    void animateValue(float fraction) {
        final java.lang.Object target = getTarget();
        if ((mTarget != null) && (target == null)) {
            // We lost the target reference, cancel and clean up. Note: we allow null target if the
            // / target has never been set.
            cancel();
            return;
        }
        super.animateValue(fraction);
        int numValues = mValues.length;
        for (int i = 0; i < numValues; ++i) {
            mValues[i].setAnimatedValue(target);
        }
    }

    @java.lang.Override
    public android.animation.ObjectAnimator clone() {
        final android.animation.ObjectAnimator anim = ((android.animation.ObjectAnimator) (super.clone()));
        return anim;
    }

    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String toString() {
        java.lang.String returnVal = (("ObjectAnimator@" + java.lang.Integer.toHexString(hashCode())) + ", target ") + getTarget();
        if (mValues != null) {
            for (int i = 0; i < mValues.length; ++i) {
                returnVal += "\n    " + mValues[i].toString();
            }
        }
        return returnVal;
    }
}

