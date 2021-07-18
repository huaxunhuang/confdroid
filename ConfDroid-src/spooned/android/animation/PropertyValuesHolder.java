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
 * This class holds information about a property and the values that that property
 * should take on during an animation. PropertyValuesHolder objects can be used to create
 * animations with ValueAnimator or ObjectAnimator that operate on several different properties
 * in parallel.
 */
public class PropertyValuesHolder implements java.lang.Cloneable {
    /**
     * The name of the property associated with the values. This need not be a real property,
     * unless this object is being used with ObjectAnimator. But this is the name by which
     * aniamted values are looked up with getAnimatedValue(String) in ValueAnimator.
     */
    java.lang.String mPropertyName;

    /**
     *
     *
     * @unknown 
     */
    protected android.util.Property mProperty;

    /**
     * The setter function, if needed. ObjectAnimator hands off this functionality to
     * PropertyValuesHolder, since it holds all of the per-property information. This
     * property is automatically
     * derived when the animation starts in setupSetterAndGetter() if using ObjectAnimator.
     */
    java.lang.reflect.Method mSetter = null;

    /**
     * The getter function, if needed. ObjectAnimator hands off this functionality to
     * PropertyValuesHolder, since it holds all of the per-property information. This
     * property is automatically
     * derived when the animation starts in setupSetterAndGetter() if using ObjectAnimator.
     * The getter is only derived and used if one of the values is null.
     */
    private java.lang.reflect.Method mGetter = null;

    /**
     * The type of values supplied. This information is used both in deriving the setter/getter
     * functions and in deriving the type of TypeEvaluator.
     */
    java.lang.Class mValueType;

    /**
     * The set of keyframes (time/value pairs) that define this animation.
     */
    android.animation.Keyframes mKeyframes = null;

    // type evaluators for the primitive types handled by this implementation
    private static final android.animation.TypeEvaluator sIntEvaluator = new android.animation.IntEvaluator();

    private static final android.animation.TypeEvaluator sFloatEvaluator = new android.animation.FloatEvaluator();

    // We try several different types when searching for appropriate setter/getter functions.
    // The caller may have supplied values in a type that does not match the setter/getter
    // functions (such as the integers 0 and 1 to represent floating point values for alpha).
    // Also, the use of generics in constructors means that we end up with the Object versions
    // of primitive types (Float vs. float). But most likely, the setter/getter functions
    // will take primitive types instead.
    // So we supply an ordered array of other types to try before giving up.
    private static java.lang.Class[] FLOAT_VARIANTS = new java.lang.Class[]{ float.class, java.lang.Float.class, double.class, int.class, java.lang.Double.class, java.lang.Integer.class };

    private static java.lang.Class[] INTEGER_VARIANTS = new java.lang.Class[]{ int.class, java.lang.Integer.class, float.class, double.class, java.lang.Float.class, java.lang.Double.class };

    private static java.lang.Class[] DOUBLE_VARIANTS = new java.lang.Class[]{ double.class, java.lang.Double.class, float.class, int.class, java.lang.Float.class, java.lang.Integer.class };

    // These maps hold all property entries for a particular class. This map
    // is used to speed up property/setter/getter lookups for a given class/property
    // combination. No need to use reflection on the combination more than once.
    private static final java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.reflect.Method>> sSetterPropertyMap = new java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.reflect.Method>>();

    private static final java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.reflect.Method>> sGetterPropertyMap = new java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.reflect.Method>>();

    // Used to pass single value to varargs parameter in setter invocation
    final java.lang.Object[] mTmpValueArray = new java.lang.Object[1];

    /**
     * The type evaluator used to calculate the animated values. This evaluator is determined
     * automatically based on the type of the start/end objects passed into the constructor,
     * but the system only knows about the primitive types int and float. Any other
     * type will need to set the evaluator to a custom evaluator for that type.
     */
    private android.animation.TypeEvaluator mEvaluator;

    /**
     * The value most recently calculated by calculateValue(). This is set during
     * that function and might be retrieved later either by ValueAnimator.animatedValue() or
     * by the property-setting logic in ObjectAnimator.animatedValue().
     */
    private java.lang.Object mAnimatedValue;

    /**
     * Converts from the source Object type to the setter Object type.
     */
    private android.animation.TypeConverter mConverter;

    /**
     * Internal utility constructor, used by the factory methods to set the property name.
     *
     * @param propertyName
     * 		The name of the property for this holder.
     */
    private PropertyValuesHolder(java.lang.String propertyName) {
        mPropertyName = propertyName;
    }

    /**
     * Internal utility constructor, used by the factory methods to set the property.
     *
     * @param property
     * 		The property for this holder.
     */
    private PropertyValuesHolder(android.util.Property property) {
        mProperty = property;
        if (property != null) {
            mPropertyName = property.getName();
        }
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name and
     * set of int values.
     *
     * @param propertyName
     * 		The name of the property being animated.
     * @param values
     * 		The values that the named property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static android.animation.PropertyValuesHolder ofInt(java.lang.String propertyName, int... values) {
        return new android.animation.PropertyValuesHolder.IntPropertyValuesHolder(propertyName, values);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * set of int values.
     *
     * @param property
     * 		The property being animated. Should not be null.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static android.animation.PropertyValuesHolder ofInt(android.util.Property<?, java.lang.Integer> property, int... values) {
        return new android.animation.PropertyValuesHolder.IntPropertyValuesHolder(property, values);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name and
     * set of <code>int[]</code> values. At least two <code>int[]</code> values must be supplied,
     * a start and end value. If more values are supplied, the values will be animated from the
     * start, through all intermediate values to the end value. When used with ObjectAnimator,
     * the elements of the array represent the parameters of the setter function.
     *
     * @param propertyName
     * 		The name of the property being animated. Can also be the
     * 		case-sensitive name of the entire setter method. Should not be null.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see IntArrayEvaluator#IntArrayEvaluator(int[])
     * @see ObjectAnimator#ofMultiInt(Object, String, TypeConverter, TypeEvaluator, Object[])
     */
    public static android.animation.PropertyValuesHolder ofMultiInt(java.lang.String propertyName, int[][] values) {
        if (values.length < 2) {
            throw new java.lang.IllegalArgumentException("At least 2 values must be supplied");
        }
        int numParameters = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                throw new java.lang.IllegalArgumentException("values must not be null");
            }
            int length = values[i].length;
            if (i == 0) {
                numParameters = length;
            } else
                if (length != numParameters) {
                    throw new java.lang.IllegalArgumentException("Values must all have the same length");
                }

        }
        android.animation.IntArrayEvaluator evaluator = new android.animation.IntArrayEvaluator(new int[numParameters]);
        return new android.animation.PropertyValuesHolder.MultiIntValuesHolder(propertyName, null, evaluator, ((java.lang.Object[]) (values)));
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name to use
     * as a multi-int setter. The values are animated along the path, with the first
     * parameter of the setter set to the x coordinate and the second set to the y coordinate.
     *
     * @param propertyName
     * 		The name of the property being animated. Can also be the
     * 		case-sensitive name of the entire setter method. Should not be null.
     * 		The setter must take exactly two <code>int</code> parameters.
     * @param path
     * 		The Path along which the values should be animated.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see ObjectAnimator#ofPropertyValuesHolder(Object, PropertyValuesHolder...)
     */
    public static android.animation.PropertyValuesHolder ofMultiInt(java.lang.String propertyName, android.graphics.Path path) {
        android.animation.Keyframes keyframes = android.animation.KeyframeSet.ofPath(path);
        android.animation.PropertyValuesHolder.PointFToIntArray converter = new android.animation.PropertyValuesHolder.PointFToIntArray();
        return new android.animation.PropertyValuesHolder.MultiIntValuesHolder(propertyName, converter, null, keyframes);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * set of Object values for use with ObjectAnimator multi-value setters. The Object
     * values are converted to <code>int[]</code> using the converter.
     *
     * @param propertyName
     * 		The property being animated or complete name of the setter.
     * 		Should not be null.
     * @param converter
     * 		Used to convert the animated value to setter parameters.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see ObjectAnimator#ofMultiInt(Object, String, TypeConverter, TypeEvaluator, Object[])
     * @see ObjectAnimator#ofPropertyValuesHolder(Object, PropertyValuesHolder...)
     */
    @java.lang.SafeVarargs
    public static <V> android.animation.PropertyValuesHolder ofMultiInt(java.lang.String propertyName, android.animation.TypeConverter<V, int[]> converter, android.animation.TypeEvaluator<V> evaluator, V... values) {
        return new android.animation.PropertyValuesHolder.MultiIntValuesHolder(propertyName, converter, evaluator, values);
    }

    /**
     * Constructs and returns a PropertyValuesHolder object with the specified property name or
     * setter name for use in a multi-int setter function using ObjectAnimator. The values can be
     * of any type, but the type should be consistent so that the supplied
     * {@link android.animation.TypeEvaluator} can be used to to evaluate the animated value. The
     * <code>converter</code> converts the values to parameters in the setter function.
     *
     * <p>At least two values must be supplied, a start and an end value.</p>
     *
     * @param propertyName
     * 		The name of the property to associate with the set of values. This
     * 		may also be the complete name of a setter function.
     * @param converter
     * 		Converts <code>values</code> into int parameters for the setter.
     * 		Can be null if the Keyframes have int[] values.
     * @param evaluator
     * 		Used to interpolate between values.
     * @param values
     * 		The values at specific fractional times to evaluate between
     * @return A PropertyValuesHolder for a multi-int parameter setter.
     */
    public static <T> android.animation.PropertyValuesHolder ofMultiInt(java.lang.String propertyName, android.animation.TypeConverter<T, int[]> converter, android.animation.TypeEvaluator<T> evaluator, android.animation.Keyframe... values) {
        android.animation.KeyframeSet keyframeSet = android.animation.KeyframeSet.ofKeyframe(values);
        return new android.animation.PropertyValuesHolder.MultiIntValuesHolder(propertyName, converter, evaluator, keyframeSet);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name and
     * set of float values.
     *
     * @param propertyName
     * 		The name of the property being animated.
     * @param values
     * 		The values that the named property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static android.animation.PropertyValuesHolder ofFloat(java.lang.String propertyName, float... values) {
        return new android.animation.PropertyValuesHolder.FloatPropertyValuesHolder(propertyName, values);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * set of float values.
     *
     * @param property
     * 		The property being animated. Should not be null.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static android.animation.PropertyValuesHolder ofFloat(android.util.Property<?, java.lang.Float> property, float... values) {
        return new android.animation.PropertyValuesHolder.FloatPropertyValuesHolder(property, values);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name and
     * set of <code>float[]</code> values. At least two <code>float[]</code> values must be supplied,
     * a start and end value. If more values are supplied, the values will be animated from the
     * start, through all intermediate values to the end value. When used with ObjectAnimator,
     * the elements of the array represent the parameters of the setter function.
     *
     * @param propertyName
     * 		The name of the property being animated. Can also be the
     * 		case-sensitive name of the entire setter method. Should not be null.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see FloatArrayEvaluator#FloatArrayEvaluator(float[])
     * @see ObjectAnimator#ofMultiFloat(Object, String, TypeConverter, TypeEvaluator, Object[])
     */
    public static android.animation.PropertyValuesHolder ofMultiFloat(java.lang.String propertyName, float[][] values) {
        if (values.length < 2) {
            throw new java.lang.IllegalArgumentException("At least 2 values must be supplied");
        }
        int numParameters = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                throw new java.lang.IllegalArgumentException("values must not be null");
            }
            int length = values[i].length;
            if (i == 0) {
                numParameters = length;
            } else
                if (length != numParameters) {
                    throw new java.lang.IllegalArgumentException("Values must all have the same length");
                }

        }
        android.animation.FloatArrayEvaluator evaluator = new android.animation.FloatArrayEvaluator(new float[numParameters]);
        return new android.animation.PropertyValuesHolder.MultiFloatValuesHolder(propertyName, null, evaluator, ((java.lang.Object[]) (values)));
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name to use
     * as a multi-float setter. The values are animated along the path, with the first
     * parameter of the setter set to the x coordinate and the second set to the y coordinate.
     *
     * @param propertyName
     * 		The name of the property being animated. Can also be the
     * 		case-sensitive name of the entire setter method. Should not be null.
     * 		The setter must take exactly two <code>float</code> parameters.
     * @param path
     * 		The Path along which the values should be animated.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see ObjectAnimator#ofPropertyValuesHolder(Object, PropertyValuesHolder...)
     */
    public static android.animation.PropertyValuesHolder ofMultiFloat(java.lang.String propertyName, android.graphics.Path path) {
        android.animation.Keyframes keyframes = android.animation.KeyframeSet.ofPath(path);
        android.animation.PropertyValuesHolder.PointFToFloatArray converter = new android.animation.PropertyValuesHolder.PointFToFloatArray();
        return new android.animation.PropertyValuesHolder.MultiFloatValuesHolder(propertyName, converter, null, keyframes);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * set of Object values for use with ObjectAnimator multi-value setters. The Object
     * values are converted to <code>float[]</code> using the converter.
     *
     * @param propertyName
     * 		The property being animated or complete name of the setter.
     * 		Should not be null.
     * @param converter
     * 		Used to convert the animated value to setter parameters.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see ObjectAnimator#ofMultiFloat(Object, String, TypeConverter, TypeEvaluator, Object[])
     */
    @java.lang.SafeVarargs
    public static <V> android.animation.PropertyValuesHolder ofMultiFloat(java.lang.String propertyName, android.animation.TypeConverter<V, float[]> converter, android.animation.TypeEvaluator<V> evaluator, V... values) {
        return new android.animation.PropertyValuesHolder.MultiFloatValuesHolder(propertyName, converter, evaluator, values);
    }

    /**
     * Constructs and returns a PropertyValuesHolder object with the specified property name or
     * setter name for use in a multi-float setter function using ObjectAnimator. The values can be
     * of any type, but the type should be consistent so that the supplied
     * {@link android.animation.TypeEvaluator} can be used to to evaluate the animated value. The
     * <code>converter</code> converts the values to parameters in the setter function.
     *
     * <p>At least two values must be supplied, a start and an end value.</p>
     *
     * @param propertyName
     * 		The name of the property to associate with the set of values. This
     * 		may also be the complete name of a setter function.
     * @param converter
     * 		Converts <code>values</code> into float parameters for the setter.
     * 		Can be null if the Keyframes have float[] values.
     * @param evaluator
     * 		Used to interpolate between values.
     * @param values
     * 		The values at specific fractional times to evaluate between
     * @return A PropertyValuesHolder for a multi-float parameter setter.
     */
    public static <T> android.animation.PropertyValuesHolder ofMultiFloat(java.lang.String propertyName, android.animation.TypeConverter<T, float[]> converter, android.animation.TypeEvaluator<T> evaluator, android.animation.Keyframe... values) {
        android.animation.KeyframeSet keyframeSet = android.animation.KeyframeSet.ofKeyframe(values);
        return new android.animation.PropertyValuesHolder.MultiFloatValuesHolder(propertyName, converter, evaluator, keyframeSet);
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name and
     * set of Object values. This variant also takes a TypeEvaluator because the system
     * cannot automatically interpolate between objects of unknown type.
     *
     * <p><strong>Note:</strong> The Object values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the PropertyValuesHolder. If the objects will be mutated externally
     * after this method is called, callers should pass a copy of those objects instead.
     *
     * @param propertyName
     * 		The name of the property being animated.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		The values that the named property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static android.animation.PropertyValuesHolder ofObject(java.lang.String propertyName, android.animation.TypeEvaluator evaluator, java.lang.Object... values) {
        android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(propertyName);
        pvh.setObjectValues(values);
        pvh.setEvaluator(evaluator);
        return pvh;
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property name and
     * a Path along which the values should be animated. This variant supports a
     * <code>TypeConverter</code> to convert from <code>PointF</code> to the target
     * type.
     *
     * <p>The PointF passed to <code>converter</code> or <code>property</code>, if
     * <code>converter</code> is <code>null</code>, is reused on each animation frame and should
     * not be stored by the setter or TypeConverter.</p>
     *
     * @param propertyName
     * 		The name of the property being animated.
     * @param converter
     * 		Converts a PointF to the type associated with the setter. May be
     * 		null if conversion is unnecessary.
     * @param path
     * 		The Path along which the values should be animated.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static android.animation.PropertyValuesHolder ofObject(java.lang.String propertyName, android.animation.TypeConverter<android.graphics.PointF, ?> converter, android.graphics.Path path) {
        android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(propertyName);
        pvh.mKeyframes = android.animation.KeyframeSet.ofPath(path);
        pvh.mValueType = android.graphics.PointF.class;
        pvh.setConverter(converter);
        return pvh;
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * set of Object values. This variant also takes a TypeEvaluator because the system
     * cannot automatically interpolate between objects of unknown type.
     *
     * <p><strong>Note:</strong> The Object values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the PropertyValuesHolder. If the objects will be mutated externally
     * after this method is called, callers should pass a copy of those objects instead.
     *
     * @param property
     * 		The property being animated. Should not be null.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    @java.lang.SafeVarargs
    public static <V> android.animation.PropertyValuesHolder ofObject(android.util.Property property, android.animation.TypeEvaluator<V> evaluator, V... values) {
        android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(property);
        pvh.setObjectValues(values);
        pvh.setEvaluator(evaluator);
        return pvh;
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * set of Object values. This variant also takes a TypeEvaluator because the system
     * cannot automatically interpolate between objects of unknown type. This variant also
     * takes a <code>TypeConverter</code> to convert from animated values to the type
     * of the property. If only one value is supplied, the <code>TypeConverter</code>
     * must be a {@link android.animation.BidirectionalTypeConverter} to retrieve the current
     * value.
     *
     * <p><strong>Note:</strong> The Object values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the PropertyValuesHolder. If the objects will be mutated externally
     * after this method is called, callers should pass a copy of those objects instead.
     *
     * @param property
     * 		The property being animated. Should not be null.
     * @param converter
     * 		Converts the animated object to the Property type.
     * @param evaluator
     * 		A TypeEvaluator that will be called on each animation frame to
     * 		provide the necessary interpolation between the Object values to derive the animated
     * 		value.
     * @param values
     * 		The values that the property will animate between.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     * @see #setConverter(TypeConverter)
     * @see TypeConverter
     */
    @java.lang.SafeVarargs
    public static <T, V> android.animation.PropertyValuesHolder ofObject(android.util.Property<?, V> property, android.animation.TypeConverter<T, V> converter, android.animation.TypeEvaluator<T> evaluator, T... values) {
        android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(property);
        pvh.setConverter(converter);
        pvh.setObjectValues(values);
        pvh.setEvaluator(evaluator);
        return pvh;
    }

    /**
     * Constructs and returns a PropertyValuesHolder with a given property and
     * a Path along which the values should be animated. This variant supports a
     * <code>TypeConverter</code> to convert from <code>PointF</code> to the target
     * type.
     *
     * <p>The PointF passed to <code>converter</code> or <code>property</code>, if
     * <code>converter</code> is <code>null</code>, is reused on each animation frame and should
     * not be stored by the setter or TypeConverter.</p>
     *
     * @param property
     * 		The property being animated. Should not be null.
     * @param converter
     * 		Converts a PointF to the type associated with the setter. May be
     * 		null if conversion is unnecessary.
     * @param path
     * 		The Path along which the values should be animated.
     * @return PropertyValuesHolder The constructed PropertyValuesHolder object.
     */
    public static <V> android.animation.PropertyValuesHolder ofObject(android.util.Property<?, V> property, android.animation.TypeConverter<android.graphics.PointF, V> converter, android.graphics.Path path) {
        android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(property);
        pvh.mKeyframes = android.animation.KeyframeSet.ofPath(path);
        pvh.mValueType = android.graphics.PointF.class;
        pvh.setConverter(converter);
        return pvh;
    }

    /**
     * Constructs and returns a PropertyValuesHolder object with the specified property name and set
     * of values. These values can be of any type, but the type should be consistent so that
     * an appropriate {@link android.animation.TypeEvaluator} can be found that matches
     * the common type.
     * <p>If there is only one value, it is assumed to be the end value of an animation,
     * and an initial value will be derived, if possible, by calling a getter function
     * on the object. Also, if any value is null, the value will be filled in when the animation
     * starts in the same way. This mechanism of automatically getting null values only works
     * if the PropertyValuesHolder object is used in conjunction
     * {@link ObjectAnimator}, and with a getter function
     * derived automatically from <code>propertyName</code>, since otherwise PropertyValuesHolder has
     * no way of determining what the value should be.
     *
     * @param propertyName
     * 		The name of the property associated with this set of values. This
     * 		can be the actual property name to be used when using a ObjectAnimator object, or
     * 		just a name used to get animated values, such as if this object is used with an
     * 		ValueAnimator object.
     * @param values
     * 		The set of values to animate between.
     */
    public static android.animation.PropertyValuesHolder ofKeyframe(java.lang.String propertyName, android.animation.Keyframe... values) {
        android.animation.KeyframeSet keyframeSet = android.animation.KeyframeSet.ofKeyframe(values);
        return android.animation.PropertyValuesHolder.ofKeyframes(propertyName, keyframeSet);
    }

    /**
     * Constructs and returns a PropertyValuesHolder object with the specified property and set
     * of values. These values can be of any type, but the type should be consistent so that
     * an appropriate {@link android.animation.TypeEvaluator} can be found that matches
     * the common type.
     * <p>If there is only one value, it is assumed to be the end value of an animation,
     * and an initial value will be derived, if possible, by calling the property's
     * {@link android.util.Property#get(Object)} function.
     * Also, if any value is null, the value will be filled in when the animation
     * starts in the same way. This mechanism of automatically getting null values only works
     * if the PropertyValuesHolder object is used in conjunction with
     * {@link ObjectAnimator}, since otherwise PropertyValuesHolder has
     * no way of determining what the value should be.
     *
     * @param property
     * 		The property associated with this set of values. Should not be null.
     * @param values
     * 		The set of values to animate between.
     */
    public static android.animation.PropertyValuesHolder ofKeyframe(android.util.Property property, android.animation.Keyframe... values) {
        android.animation.KeyframeSet keyframeSet = android.animation.KeyframeSet.ofKeyframe(values);
        return android.animation.PropertyValuesHolder.ofKeyframes(property, keyframeSet);
    }

    static android.animation.PropertyValuesHolder ofKeyframes(java.lang.String propertyName, android.animation.Keyframes keyframes) {
        if (keyframes instanceof android.animation.Keyframes.IntKeyframes) {
            return new android.animation.PropertyValuesHolder.IntPropertyValuesHolder(propertyName, ((android.animation.Keyframes.IntKeyframes) (keyframes)));
        } else
            if (keyframes instanceof android.animation.Keyframes.FloatKeyframes) {
                return new android.animation.PropertyValuesHolder.FloatPropertyValuesHolder(propertyName, ((android.animation.Keyframes.FloatKeyframes) (keyframes)));
            } else {
                android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(propertyName);
                pvh.mKeyframes = keyframes;
                pvh.mValueType = keyframes.getType();
                return pvh;
            }

    }

    static android.animation.PropertyValuesHolder ofKeyframes(android.util.Property property, android.animation.Keyframes keyframes) {
        if (keyframes instanceof android.animation.Keyframes.IntKeyframes) {
            return new android.animation.PropertyValuesHolder.IntPropertyValuesHolder(property, ((android.animation.Keyframes.IntKeyframes) (keyframes)));
        } else
            if (keyframes instanceof android.animation.Keyframes.FloatKeyframes) {
                return new android.animation.PropertyValuesHolder.FloatPropertyValuesHolder(property, ((android.animation.Keyframes.FloatKeyframes) (keyframes)));
            } else {
                android.animation.PropertyValuesHolder pvh = new android.animation.PropertyValuesHolder(property);
                pvh.mKeyframes = keyframes;
                pvh.mValueType = keyframes.getType();
                return pvh;
            }

    }

    /**
     * Set the animated values for this object to this set of ints.
     * If there is only one value, it is assumed to be the end value of an animation,
     * and an initial value will be derived, if possible, by calling a getter function
     * on the object. Also, if any value is null, the value will be filled in when the animation
     * starts in the same way. This mechanism of automatically getting null values only works
     * if the PropertyValuesHolder object is used in conjunction
     * {@link ObjectAnimator}, and with a getter function
     * derived automatically from <code>propertyName</code>, since otherwise PropertyValuesHolder has
     * no way of determining what the value should be.
     *
     * @param values
     * 		One or more values that the animation will animate between.
     */
    public void setIntValues(int... values) {
        mValueType = int.class;
        mKeyframes = android.animation.KeyframeSet.ofInt(values);
    }

    /**
     * Set the animated values for this object to this set of floats.
     * If there is only one value, it is assumed to be the end value of an animation,
     * and an initial value will be derived, if possible, by calling a getter function
     * on the object. Also, if any value is null, the value will be filled in when the animation
     * starts in the same way. This mechanism of automatically getting null values only works
     * if the PropertyValuesHolder object is used in conjunction
     * {@link ObjectAnimator}, and with a getter function
     * derived automatically from <code>propertyName</code>, since otherwise PropertyValuesHolder has
     * no way of determining what the value should be.
     *
     * @param values
     * 		One or more values that the animation will animate between.
     */
    public void setFloatValues(float... values) {
        mValueType = float.class;
        mKeyframes = android.animation.KeyframeSet.ofFloat(values);
    }

    /**
     * Set the animated values for this object to this set of Keyframes.
     *
     * @param values
     * 		One or more values that the animation will animate between.
     */
    public void setKeyframes(android.animation.Keyframe... values) {
        int numKeyframes = values.length;
        android.animation.Keyframe[] keyframes = new android.animation.Keyframe[java.lang.Math.max(numKeyframes, 2)];
        mValueType = ((android.animation.Keyframe) (values[0])).getType();
        for (int i = 0; i < numKeyframes; ++i) {
            keyframes[i] = ((android.animation.Keyframe) (values[i]));
        }
        mKeyframes = new android.animation.KeyframeSet(keyframes);
    }

    /**
     * Set the animated values for this object to this set of Objects.
     * If there is only one value, it is assumed to be the end value of an animation,
     * and an initial value will be derived, if possible, by calling a getter function
     * on the object. Also, if any value is null, the value will be filled in when the animation
     * starts in the same way. This mechanism of automatically getting null values only works
     * if the PropertyValuesHolder object is used in conjunction
     * {@link ObjectAnimator}, and with a getter function
     * derived automatically from <code>propertyName</code>, since otherwise PropertyValuesHolder has
     * no way of determining what the value should be.
     *
     * <p><strong>Note:</strong> The Object values are stored as references to the original
     * objects, which means that changes to those objects after this method is called will
     * affect the values on the PropertyValuesHolder. If the objects will be mutated externally
     * after this method is called, callers should pass a copy of those objects instead.
     *
     * @param values
     * 		One or more values that the animation will animate between.
     */
    public void setObjectValues(java.lang.Object... values) {
        mValueType = values[0].getClass();
        mKeyframes = android.animation.KeyframeSet.ofObject(values);
        if (mEvaluator != null) {
            mKeyframes.setEvaluator(mEvaluator);
        }
    }

    /**
     * Sets the converter to convert from the values type to the setter's parameter type.
     * If only one value is supplied, <var>converter</var> must be a
     * {@link android.animation.BidirectionalTypeConverter}.
     *
     * @param converter
     * 		The converter to use to convert values.
     */
    public void setConverter(android.animation.TypeConverter converter) {
        mConverter = converter;
    }

    /**
     * Determine the setter or getter function using the JavaBeans convention of setFoo or
     * getFoo for a property named 'foo'. This function figures out what the name of the
     * function should be and uses reflection to find the Method with that name on the
     * target object.
     *
     * @param targetClass
     * 		The class to search for the method
     * @param prefix
     * 		"set" or "get", depending on whether we need a setter or getter.
     * @param valueType
     * 		The type of the parameter (in the case of a setter). This type
     * 		is derived from the values set on this PropertyValuesHolder. This type is used as
     * 		a first guess at the parameter type, but we check for methods with several different
     * 		types to avoid problems with slight mis-matches between supplied values and actual
     * 		value types used on the setter.
     * @return Method the method associated with mPropertyName.
     */
    private java.lang.reflect.Method getPropertyFunction(java.lang.Class targetClass, java.lang.String prefix, java.lang.Class valueType) {
        // TODO: faster implementation...
        java.lang.reflect.Method returnVal = null;
        java.lang.String methodName = android.animation.PropertyValuesHolder.getMethodName(prefix, mPropertyName);
        java.lang.Class[] args = null;
        if (valueType == null) {
            try {
                returnVal = targetClass.getMethod(methodName, args);
            } catch (java.lang.NoSuchMethodException e) {
                // Swallow the error, log it later
            }
        } else {
            args = new java.lang.Class[1];
            java.lang.Class[] typeVariants;
            if (valueType.equals(java.lang.Float.class)) {
                typeVariants = android.animation.PropertyValuesHolder.FLOAT_VARIANTS;
            } else
                if (valueType.equals(java.lang.Integer.class)) {
                    typeVariants = android.animation.PropertyValuesHolder.INTEGER_VARIANTS;
                } else
                    if (valueType.equals(java.lang.Double.class)) {
                        typeVariants = android.animation.PropertyValuesHolder.DOUBLE_VARIANTS;
                    } else {
                        typeVariants = new java.lang.Class[1];
                        typeVariants[0] = valueType;
                    }


            for (java.lang.Class typeVariant : typeVariants) {
                args[0] = typeVariant;
                try {
                    returnVal = targetClass.getMethod(methodName, args);
                    if (mConverter == null) {
                        // change the value type to suit
                        mValueType = typeVariant;
                    }
                    return returnVal;
                } catch (java.lang.NoSuchMethodException e) {
                    // Swallow the error and keep trying other variants
                }
            }
            // If we got here, then no appropriate function was found
        }
        if (returnVal == null) {
            android.util.Log.w("PropertyValuesHolder", (((("Method " + android.animation.PropertyValuesHolder.getMethodName(prefix, mPropertyName)) + "() with type ") + valueType) + " not found on target class ") + targetClass);
        }
        return returnVal;
    }

    /**
     * Returns the setter or getter requested. This utility function checks whether the
     * requested method exists in the propertyMapMap cache. If not, it calls another
     * utility function to request the Method from the targetClass directly.
     *
     * @param targetClass
     * 		The Class on which the requested method should exist.
     * @param propertyMapMap
     * 		The cache of setters/getters derived so far.
     * @param prefix
     * 		"set" or "get", for the setter or getter.
     * @param valueType
     * 		The type of parameter passed into the method (null for getter).
     * @return Method the method associated with mPropertyName.
     */
    private java.lang.reflect.Method setupSetterOrGetter(java.lang.Class targetClass, java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.reflect.Method>> propertyMapMap, java.lang.String prefix, java.lang.Class valueType) {
        java.lang.reflect.Method setterOrGetter = null;
        synchronized(propertyMapMap) {
            // Have to lock property map prior to reading it, to guard against
            // another thread putting something in there after we've checked it
            // but before we've added an entry to it
            java.util.HashMap<java.lang.String, java.lang.reflect.Method> propertyMap = propertyMapMap.get(targetClass);
            boolean wasInMap = false;
            if (propertyMap != null) {
                wasInMap = propertyMap.containsKey(mPropertyName);
                if (wasInMap) {
                    setterOrGetter = propertyMap.get(mPropertyName);
                }
            }
            if (!wasInMap) {
                setterOrGetter = getPropertyFunction(targetClass, prefix, valueType);
                if (propertyMap == null) {
                    propertyMap = new java.util.HashMap<java.lang.String, java.lang.reflect.Method>();
                    propertyMapMap.put(targetClass, propertyMap);
                }
                propertyMap.put(mPropertyName, setterOrGetter);
            }
        }
        return setterOrGetter;
    }

    /**
     * Utility function to get the setter from targetClass
     *
     * @param targetClass
     * 		The Class on which the requested method should exist.
     */
    void setupSetter(java.lang.Class targetClass) {
        java.lang.Class<?> propertyType = (mConverter == null) ? mValueType : mConverter.getTargetType();
        mSetter = setupSetterOrGetter(targetClass, android.animation.PropertyValuesHolder.sSetterPropertyMap, "set", propertyType);
    }

    /**
     * Utility function to get the getter from targetClass
     */
    private void setupGetter(java.lang.Class targetClass) {
        mGetter = setupSetterOrGetter(targetClass, android.animation.PropertyValuesHolder.sGetterPropertyMap, "get", null);
    }

    /**
     * Internal function (called from ObjectAnimator) to set up the setter and getter
     * prior to running the animation. If the setter has not been manually set for this
     * object, it will be derived automatically given the property name, target object, and
     * types of values supplied. If no getter has been set, it will be supplied iff any of the
     * supplied values was null. If there is a null value, then the getter (supplied or derived)
     * will be called to set those null values to the current value of the property
     * on the target object.
     *
     * @param target
     * 		The object on which the setter (and possibly getter) exist.
     */
    void setupSetterAndGetter(java.lang.Object target) {
        mKeyframes.invalidateCache();
        if (mProperty != null) {
            // check to make sure that mProperty is on the class of target
            try {
                java.lang.Object testValue = null;
                java.util.List<android.animation.Keyframe> keyframes = mKeyframes.getKeyframes();
                int keyframeCount = (keyframes == null) ? 0 : keyframes.size();
                for (int i = 0; i < keyframeCount; i++) {
                    android.animation.Keyframe kf = keyframes.get(i);
                    if ((!kf.hasValue()) || kf.valueWasSetOnStart()) {
                        if (testValue == null) {
                            testValue = convertBack(mProperty.get(target));
                        }
                        kf.setValue(testValue);
                        kf.setValueWasSetOnStart(true);
                    }
                }
                return;
            } catch (java.lang.ClassCastException e) {
                android.util.Log.w("PropertyValuesHolder", ((("No such property (" + mProperty.getName()) + ") on target object ") + target) + ". Trying reflection instead");
                mProperty = null;
            }
        }
        // We can't just say 'else' here because the catch statement sets mProperty to null.
        if (mProperty == null) {
            java.lang.Class targetClass = target.getClass();
            if (mSetter == null) {
                setupSetter(targetClass);
            }
            java.util.List<android.animation.Keyframe> keyframes = mKeyframes.getKeyframes();
            int keyframeCount = (keyframes == null) ? 0 : keyframes.size();
            for (int i = 0; i < keyframeCount; i++) {
                android.animation.Keyframe kf = keyframes.get(i);
                if ((!kf.hasValue()) || kf.valueWasSetOnStart()) {
                    if (mGetter == null) {
                        setupGetter(targetClass);
                        if (mGetter == null) {
                            // Already logged the error - just return to avoid NPE
                            return;
                        }
                    }
                    try {
                        java.lang.Object value = convertBack(mGetter.invoke(target));
                        kf.setValue(value);
                        kf.setValueWasSetOnStart(true);
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        android.util.Log.e("PropertyValuesHolder", e.toString());
                    } catch (java.lang.IllegalAccessException e) {
                        android.util.Log.e("PropertyValuesHolder", e.toString());
                    }
                }
            }
        }
    }

    private java.lang.Object convertBack(java.lang.Object value) {
        if (mConverter != null) {
            if (!(mConverter instanceof android.animation.BidirectionalTypeConverter)) {
                throw new java.lang.IllegalArgumentException(("Converter " + mConverter.getClass().getName()) + " must be a BidirectionalTypeConverter");
            }
            value = ((android.animation.BidirectionalTypeConverter) (mConverter)).convertBack(value);
        }
        return value;
    }

    /**
     * Utility function to set the value stored in a particular Keyframe. The value used is
     * whatever the value is for the property name specified in the keyframe on the target object.
     *
     * @param target
     * 		The target object from which the current value should be extracted.
     * @param kf
     * 		The keyframe which holds the property name and value.
     */
    private void setupValue(java.lang.Object target, android.animation.Keyframe kf) {
        if (mProperty != null) {
            java.lang.Object value = convertBack(mProperty.get(target));
            kf.setValue(value);
        } else {
            try {
                if (mGetter == null) {
                    java.lang.Class targetClass = target.getClass();
                    setupGetter(targetClass);
                    if (mGetter == null) {
                        // Already logged the error - just return to avoid NPE
                        return;
                    }
                }
                java.lang.Object value = convertBack(mGetter.invoke(target));
                kf.setValue(value);
            } catch (java.lang.reflect.InvocationTargetException e) {
                android.util.Log.e("PropertyValuesHolder", e.toString());
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e("PropertyValuesHolder", e.toString());
            }
        }
    }

    /**
     * This function is called by ObjectAnimator when setting the start values for an animation.
     * The start values are set according to the current values in the target object. The
     * property whose value is extracted is whatever is specified by the propertyName of this
     * PropertyValuesHolder object.
     *
     * @param target
     * 		The object which holds the start values that should be set.
     */
    void setupStartValue(java.lang.Object target) {
        java.util.List<android.animation.Keyframe> keyframes = mKeyframes.getKeyframes();
        if (!keyframes.isEmpty()) {
            setupValue(target, keyframes.get(0));
        }
    }

    /**
     * This function is called by ObjectAnimator when setting the end values for an animation.
     * The end values are set according to the current values in the target object. The
     * property whose value is extracted is whatever is specified by the propertyName of this
     * PropertyValuesHolder object.
     *
     * @param target
     * 		The object which holds the start values that should be set.
     */
    void setupEndValue(java.lang.Object target) {
        java.util.List<android.animation.Keyframe> keyframes = mKeyframes.getKeyframes();
        if (!keyframes.isEmpty()) {
            setupValue(target, keyframes.get(keyframes.size() - 1));
        }
    }

    @java.lang.Override
    public android.animation.PropertyValuesHolder clone() {
        try {
            android.animation.PropertyValuesHolder newPVH = ((android.animation.PropertyValuesHolder) (super.clone()));
            newPVH.mPropertyName = mPropertyName;
            newPVH.mProperty = mProperty;
            newPVH.mKeyframes = mKeyframes.clone();
            newPVH.mEvaluator = mEvaluator;
            return newPVH;
        } catch (java.lang.CloneNotSupportedException e) {
            // won't reach here
            return null;
        }
    }

    /**
     * Internal function to set the value on the target object, using the setter set up
     * earlier on this PropertyValuesHolder object. This function is called by ObjectAnimator
     * to handle turning the value calculated by ValueAnimator into a value set on the object
     * according to the name of the property.
     *
     * @param target
     * 		The target object on which the value is set
     */
    void setAnimatedValue(java.lang.Object target) {
        if (mProperty != null) {
            mProperty.set(target, getAnimatedValue());
        }
        if (mSetter != null) {
            try {
                mTmpValueArray[0] = getAnimatedValue();
                mSetter.invoke(target, mTmpValueArray);
            } catch (java.lang.reflect.InvocationTargetException e) {
                android.util.Log.e("PropertyValuesHolder", e.toString());
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e("PropertyValuesHolder", e.toString());
            }
        }
    }

    /**
     * Internal function, called by ValueAnimator, to set up the TypeEvaluator that will be used
     * to calculate animated values.
     */
    void init() {
        if (mEvaluator == null) {
            // We already handle int and float automatically, but not their Object
            // equivalents
            mEvaluator = (mValueType == java.lang.Integer.class) ? android.animation.PropertyValuesHolder.sIntEvaluator : mValueType == java.lang.Float.class ? android.animation.PropertyValuesHolder.sFloatEvaluator : null;
        }
        if (mEvaluator != null) {
            // KeyframeSet knows how to evaluate the common types - only give it a custom
            // evaluator if one has been set on this class
            mKeyframes.setEvaluator(mEvaluator);
        }
    }

    /**
     * The TypeEvaluator will be automatically determined based on the type of values
     * supplied to PropertyValuesHolder. The evaluator can be manually set, however, if so
     * desired. This may be important in cases where either the type of the values supplied
     * do not match the way that they should be interpolated between, or if the values
     * are of a custom type or one not currently understood by the animation system. Currently,
     * only values of type float and int (and their Object equivalents: Float
     * and Integer) are  correctly interpolated; all other types require setting a TypeEvaluator.
     *
     * @param evaluator
     * 		
     */
    public void setEvaluator(android.animation.TypeEvaluator evaluator) {
        mEvaluator = evaluator;
        mKeyframes.setEvaluator(evaluator);
    }

    /**
     * Function used to calculate the value according to the evaluator set up for
     * this PropertyValuesHolder object. This function is called by ValueAnimator.animateValue().
     *
     * @param fraction
     * 		The elapsed, interpolated fraction of the animation.
     */
    void calculateValue(float fraction) {
        java.lang.Object value = mKeyframes.getValue(fraction);
        mAnimatedValue = (mConverter == null) ? value : mConverter.convert(value);
    }

    /**
     * Sets the name of the property that will be animated. This name is used to derive
     * a setter function that will be called to set animated values.
     * For example, a property name of <code>foo</code> will result
     * in a call to the function <code>setFoo()</code> on the target object. If either
     * <code>valueFrom</code> or <code>valueTo</code> is null, then a getter function will
     * also be derived and called.
     *
     * <p>Note that the setter function derived from this property name
     * must take the same parameter type as the
     * <code>valueFrom</code> and <code>valueTo</code> properties, otherwise the call to
     * the setter function will fail.</p>
     *
     * @param propertyName
     * 		The name of the property being animated.
     */
    public void setPropertyName(java.lang.String propertyName) {
        mPropertyName = propertyName;
    }

    /**
     * Sets the property that will be animated.
     *
     * <p>Note that if this PropertyValuesHolder object is used with ObjectAnimator, the property
     * must exist on the target object specified in that ObjectAnimator.</p>
     *
     * @param property
     * 		The property being animated.
     */
    public void setProperty(android.util.Property property) {
        mProperty = property;
    }

    /**
     * Gets the name of the property that will be animated. This name will be used to derive
     * a setter function that will be called to set animated values.
     * For example, a property name of <code>foo</code> will result
     * in a call to the function <code>setFoo()</code> on the target object. If either
     * <code>valueFrom</code> or <code>valueTo</code> is null, then a getter function will
     * also be derived and called.
     */
    public java.lang.String getPropertyName() {
        return mPropertyName;
    }

    /**
     * Internal function, called by ValueAnimator and ObjectAnimator, to retrieve the value
     * most recently calculated in calculateValue().
     *
     * @return 
     */
    java.lang.Object getAnimatedValue() {
        return mAnimatedValue;
    }

    /**
     * PropertyValuesHolder is Animators use to hold internal animation related data.
     * Therefore, in order to replicate the animation behavior, we need to get data out of
     * PropertyValuesHolder.
     *
     * @unknown 
     */
    public void getPropertyValues(android.animation.PropertyValuesHolder.PropertyValues values) {
        init();
        values.propertyName = mPropertyName;
        values.type = mValueType;
        values.startValue = mKeyframes.getValue(0);
        if (values.startValue instanceof android.util.PathParser.PathData) {
            // PathData evaluator returns the same mutable PathData object when query fraction,
            // so we have to make a copy here.
            values.startValue = new android.util.PathParser.PathData(((android.util.PathParser.PathData) (values.startValue)));
        }
        values.endValue = mKeyframes.getValue(1);
        if (values.endValue instanceof android.util.PathParser.PathData) {
            // PathData evaluator returns the same mutable PathData object when query fraction,
            // so we have to make a copy here.
            values.endValue = new android.util.PathParser.PathData(((android.util.PathParser.PathData) (values.endValue)));
        }
        // TODO: We need a better way to get data out of keyframes.
        if (((mKeyframes instanceof android.animation.PathKeyframes.FloatKeyframesBase) || (mKeyframes instanceof android.animation.PathKeyframes.IntKeyframesBase)) || ((mKeyframes.getKeyframes() != null) && (mKeyframes.getKeyframes().size() > 2))) {
            // When a pvh has more than 2 keyframes, that means there are intermediate values in
            // addition to start/end values defined for animators. Another case where such
            // intermediate values are defined is when animator has a path to animate along. In
            // these cases, a data source is needed to capture these intermediate values.
            values.dataSource = new android.animation.PropertyValuesHolder.PropertyValues.DataSource() {
                @java.lang.Override
                public java.lang.Object getValueAtFraction(float fraction) {
                    return mKeyframes.getValue(fraction);
                }
            };
        } else {
            values.dataSource = null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.Class getValueType() {
        return mValueType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (mPropertyName + ": ") + mKeyframes.toString();
    }

    /**
     * Utility method to derive a setter/getter method name from a property name, where the
     * prefix is typically "set" or "get" and the first letter of the property name is
     * capitalized.
     *
     * @param prefix
     * 		The precursor to the method name, before the property name begins, typically
     * 		"set" or "get".
     * @param propertyName
     * 		The name of the property that represents the bulk of the method name
     * 		after the prefix. The first letter of this word will be capitalized in the resulting
     * 		method name.
     * @return String the property name converted to a method name according to the conventions
    specified above.
     */
    static java.lang.String getMethodName(java.lang.String prefix, java.lang.String propertyName) {
        if ((propertyName == null) || (propertyName.length() == 0)) {
            // shouldn't get here
            return prefix;
        }
        char firstLetter = java.lang.Character.toUpperCase(propertyName.charAt(0));
        java.lang.String theRest = propertyName.substring(1);
        return (prefix + firstLetter) + theRest;
    }

    static class IntPropertyValuesHolder extends android.animation.PropertyValuesHolder {
        // Cache JNI functions to avoid looking them up twice
        private static final java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>> sJNISetterPropertyMap = new java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>>();

        long mJniSetter;

        private android.util.IntProperty mIntProperty;

        android.animation.Keyframes.IntKeyframes mIntKeyframes;

        int mIntAnimatedValue;

        public IntPropertyValuesHolder(java.lang.String propertyName, android.animation.Keyframes.IntKeyframes keyframes) {
            super(propertyName);
            mValueType = int.class;
            mKeyframes = keyframes;
            mIntKeyframes = keyframes;
        }

        public IntPropertyValuesHolder(android.util.Property property, android.animation.Keyframes.IntKeyframes keyframes) {
            super(property);
            mValueType = int.class;
            mKeyframes = keyframes;
            mIntKeyframes = keyframes;
            if (property instanceof android.util.IntProperty) {
                mIntProperty = ((android.util.IntProperty) (mProperty));
            }
        }

        public IntPropertyValuesHolder(java.lang.String propertyName, int... values) {
            super(propertyName);
            setIntValues(values);
        }

        public IntPropertyValuesHolder(android.util.Property property, int... values) {
            super(property);
            setIntValues(values);
            if (property instanceof android.util.IntProperty) {
                mIntProperty = ((android.util.IntProperty) (mProperty));
            }
        }

        @java.lang.Override
        public void setProperty(android.util.Property property) {
            if (property instanceof android.util.IntProperty) {
                mIntProperty = ((android.util.IntProperty) (property));
            } else {
                super.setProperty(property);
            }
        }

        @java.lang.Override
        public void setIntValues(int... values) {
            super.setIntValues(values);
            mIntKeyframes = ((android.animation.Keyframes.IntKeyframes) (mKeyframes));
        }

        @java.lang.Override
        void calculateValue(float fraction) {
            mIntAnimatedValue = mIntKeyframes.getIntValue(fraction);
        }

        @java.lang.Override
        java.lang.Object getAnimatedValue() {
            return mIntAnimatedValue;
        }

        @java.lang.Override
        public android.animation.PropertyValuesHolder.IntPropertyValuesHolder clone() {
            android.animation.PropertyValuesHolder.IntPropertyValuesHolder newPVH = ((android.animation.PropertyValuesHolder.IntPropertyValuesHolder) (super.clone()));
            newPVH.mIntKeyframes = ((android.animation.Keyframes.IntKeyframes) (newPVH.mKeyframes));
            return newPVH;
        }

        /**
         * Internal function to set the value on the target object, using the setter set up
         * earlier on this PropertyValuesHolder object. This function is called by ObjectAnimator
         * to handle turning the value calculated by ValueAnimator into a value set on the object
         * according to the name of the property.
         *
         * @param target
         * 		The target object on which the value is set
         */
        @java.lang.Override
        void setAnimatedValue(java.lang.Object target) {
            if (mIntProperty != null) {
                mIntProperty.setValue(target, mIntAnimatedValue);
                return;
            }
            if (mProperty != null) {
                mProperty.set(target, mIntAnimatedValue);
                return;
            }
            if (mJniSetter != 0) {
                android.animation.PropertyValuesHolder.nCallIntMethod(target, mJniSetter, mIntAnimatedValue);
                return;
            }
            if (mSetter != null) {
                try {
                    mTmpValueArray[0] = mIntAnimatedValue;
                    mSetter.invoke(target, mTmpValueArray);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    android.util.Log.e("PropertyValuesHolder", e.toString());
                } catch (java.lang.IllegalAccessException e) {
                    android.util.Log.e("PropertyValuesHolder", e.toString());
                }
            }
        }

        @java.lang.Override
        void setupSetter(java.lang.Class targetClass) {
            if (mProperty != null) {
                return;
            }
            // Check new static hashmap<propName, int> for setter method
            synchronized(android.animation.PropertyValuesHolder.IntPropertyValuesHolder.sJNISetterPropertyMap) {
                java.util.HashMap<java.lang.String, java.lang.Long> propertyMap = android.animation.PropertyValuesHolder.IntPropertyValuesHolder.sJNISetterPropertyMap.get(targetClass);
                boolean wasInMap = false;
                if (propertyMap != null) {
                    wasInMap = propertyMap.containsKey(mPropertyName);
                    if (wasInMap) {
                        java.lang.Long jniSetter = propertyMap.get(mPropertyName);
                        if (jniSetter != null) {
                            mJniSetter = jniSetter;
                        }
                    }
                }
                if (!wasInMap) {
                    java.lang.String methodName = android.animation.PropertyValuesHolder.getMethodName("set", mPropertyName);
                    try {
                        mJniSetter = android.animation.PropertyValuesHolder.nGetIntMethod(targetClass, methodName);
                    } catch (java.lang.NoSuchMethodError e) {
                        // Couldn't find it via JNI - try reflection next. Probably means the method
                        // doesn't exist, or the type is wrong. An error will be logged later if
                        // reflection fails as well.
                    }
                    if (propertyMap == null) {
                        propertyMap = new java.util.HashMap<java.lang.String, java.lang.Long>();
                        android.animation.PropertyValuesHolder.IntPropertyValuesHolder.sJNISetterPropertyMap.put(targetClass, propertyMap);
                    }
                    propertyMap.put(mPropertyName, mJniSetter);
                }
            }
            if (mJniSetter == 0) {
                // Couldn't find method through fast JNI approach - just use reflection
                super.setupSetter(targetClass);
            }
        }
    }

    static class FloatPropertyValuesHolder extends android.animation.PropertyValuesHolder {
        // Cache JNI functions to avoid looking them up twice
        private static final java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>> sJNISetterPropertyMap = new java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>>();

        long mJniSetter;

        private android.util.FloatProperty mFloatProperty;

        android.animation.Keyframes.FloatKeyframes mFloatKeyframes;

        float mFloatAnimatedValue;

        public FloatPropertyValuesHolder(java.lang.String propertyName, android.animation.Keyframes.FloatKeyframes keyframes) {
            super(propertyName);
            mValueType = float.class;
            mKeyframes = keyframes;
            mFloatKeyframes = keyframes;
        }

        public FloatPropertyValuesHolder(android.util.Property property, android.animation.Keyframes.FloatKeyframes keyframes) {
            super(property);
            mValueType = float.class;
            mKeyframes = keyframes;
            mFloatKeyframes = keyframes;
            if (property instanceof android.util.FloatProperty) {
                mFloatProperty = ((android.util.FloatProperty) (mProperty));
            }
        }

        public FloatPropertyValuesHolder(java.lang.String propertyName, float... values) {
            super(propertyName);
            setFloatValues(values);
        }

        public FloatPropertyValuesHolder(android.util.Property property, float... values) {
            super(property);
            setFloatValues(values);
            if (property instanceof android.util.FloatProperty) {
                mFloatProperty = ((android.util.FloatProperty) (mProperty));
            }
        }

        @java.lang.Override
        public void setProperty(android.util.Property property) {
            if (property instanceof android.util.FloatProperty) {
                mFloatProperty = ((android.util.FloatProperty) (property));
            } else {
                super.setProperty(property);
            }
        }

        @java.lang.Override
        public void setFloatValues(float... values) {
            super.setFloatValues(values);
            mFloatKeyframes = ((android.animation.Keyframes.FloatKeyframes) (mKeyframes));
        }

        @java.lang.Override
        void calculateValue(float fraction) {
            mFloatAnimatedValue = mFloatKeyframes.getFloatValue(fraction);
        }

        @java.lang.Override
        java.lang.Object getAnimatedValue() {
            return mFloatAnimatedValue;
        }

        @java.lang.Override
        public android.animation.PropertyValuesHolder.FloatPropertyValuesHolder clone() {
            android.animation.PropertyValuesHolder.FloatPropertyValuesHolder newPVH = ((android.animation.PropertyValuesHolder.FloatPropertyValuesHolder) (super.clone()));
            newPVH.mFloatKeyframes = ((android.animation.Keyframes.FloatKeyframes) (newPVH.mKeyframes));
            return newPVH;
        }

        /**
         * Internal function to set the value on the target object, using the setter set up
         * earlier on this PropertyValuesHolder object. This function is called by ObjectAnimator
         * to handle turning the value calculated by ValueAnimator into a value set on the object
         * according to the name of the property.
         *
         * @param target
         * 		The target object on which the value is set
         */
        @java.lang.Override
        void setAnimatedValue(java.lang.Object target) {
            if (mFloatProperty != null) {
                mFloatProperty.setValue(target, mFloatAnimatedValue);
                return;
            }
            if (mProperty != null) {
                mProperty.set(target, mFloatAnimatedValue);
                return;
            }
            if (mJniSetter != 0) {
                android.animation.PropertyValuesHolder.nCallFloatMethod(target, mJniSetter, mFloatAnimatedValue);
                return;
            }
            if (mSetter != null) {
                try {
                    mTmpValueArray[0] = mFloatAnimatedValue;
                    mSetter.invoke(target, mTmpValueArray);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    android.util.Log.e("PropertyValuesHolder", e.toString());
                } catch (java.lang.IllegalAccessException e) {
                    android.util.Log.e("PropertyValuesHolder", e.toString());
                }
            }
        }

        @java.lang.Override
        void setupSetter(java.lang.Class targetClass) {
            if (mProperty != null) {
                return;
            }
            // Check new static hashmap<propName, int> for setter method
            synchronized(android.animation.PropertyValuesHolder.FloatPropertyValuesHolder.sJNISetterPropertyMap) {
                java.util.HashMap<java.lang.String, java.lang.Long> propertyMap = android.animation.PropertyValuesHolder.FloatPropertyValuesHolder.sJNISetterPropertyMap.get(targetClass);
                boolean wasInMap = false;
                if (propertyMap != null) {
                    wasInMap = propertyMap.containsKey(mPropertyName);
                    if (wasInMap) {
                        java.lang.Long jniSetter = propertyMap.get(mPropertyName);
                        if (jniSetter != null) {
                            mJniSetter = jniSetter;
                        }
                    }
                }
                if (!wasInMap) {
                    java.lang.String methodName = android.animation.PropertyValuesHolder.getMethodName("set", mPropertyName);
                    try {
                        mJniSetter = android.animation.PropertyValuesHolder.nGetFloatMethod(targetClass, methodName);
                    } catch (java.lang.NoSuchMethodError e) {
                        // Couldn't find it via JNI - try reflection next. Probably means the method
                        // doesn't exist, or the type is wrong. An error will be logged later if
                        // reflection fails as well.
                    }
                    if (propertyMap == null) {
                        propertyMap = new java.util.HashMap<java.lang.String, java.lang.Long>();
                        android.animation.PropertyValuesHolder.FloatPropertyValuesHolder.sJNISetterPropertyMap.put(targetClass, propertyMap);
                    }
                    propertyMap.put(mPropertyName, mJniSetter);
                }
            }
            if (mJniSetter == 0) {
                // Couldn't find method through fast JNI approach - just use reflection
                super.setupSetter(targetClass);
            }
        }
    }

    static class MultiFloatValuesHolder extends android.animation.PropertyValuesHolder {
        private long mJniSetter;

        private static final java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>> sJNISetterPropertyMap = new java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>>();

        public MultiFloatValuesHolder(java.lang.String propertyName, android.animation.TypeConverter converter, android.animation.TypeEvaluator evaluator, java.lang.Object... values) {
            super(propertyName);
            setConverter(converter);
            setObjectValues(values);
            setEvaluator(evaluator);
        }

        public MultiFloatValuesHolder(java.lang.String propertyName, android.animation.TypeConverter converter, android.animation.TypeEvaluator evaluator, android.animation.Keyframes keyframes) {
            super(propertyName);
            setConverter(converter);
            mKeyframes = keyframes;
            setEvaluator(evaluator);
        }

        /**
         * Internal function to set the value on the target object, using the setter set up
         * earlier on this PropertyValuesHolder object. This function is called by ObjectAnimator
         * to handle turning the value calculated by ValueAnimator into a value set on the object
         * according to the name of the property.
         *
         * @param target
         * 		The target object on which the value is set
         */
        @java.lang.Override
        void setAnimatedValue(java.lang.Object target) {
            float[] values = ((float[]) (getAnimatedValue()));
            int numParameters = values.length;
            if (mJniSetter != 0) {
                switch (numParameters) {
                    case 1 :
                        android.animation.PropertyValuesHolder.nCallFloatMethod(target, mJniSetter, values[0]);
                        break;
                    case 2 :
                        android.animation.PropertyValuesHolder.nCallTwoFloatMethod(target, mJniSetter, values[0], values[1]);
                        break;
                    case 4 :
                        android.animation.PropertyValuesHolder.nCallFourFloatMethod(target, mJniSetter, values[0], values[1], values[2], values[3]);
                        break;
                    default :
                        {
                            android.animation.PropertyValuesHolder.nCallMultipleFloatMethod(target, mJniSetter, values);
                            break;
                        }
                }
            }
        }

        /**
         * Internal function (called from ObjectAnimator) to set up the setter and getter
         * prior to running the animation. No getter can be used for multiple parameters.
         *
         * @param target
         * 		The object on which the setter exists.
         */
        @java.lang.Override
        void setupSetterAndGetter(java.lang.Object target) {
            setupSetter(target.getClass());
        }

        @java.lang.Override
        void setupSetter(java.lang.Class targetClass) {
            if (mJniSetter != 0) {
                return;
            }
            synchronized(android.animation.PropertyValuesHolder.MultiFloatValuesHolder.sJNISetterPropertyMap) {
                java.util.HashMap<java.lang.String, java.lang.Long> propertyMap = android.animation.PropertyValuesHolder.MultiFloatValuesHolder.sJNISetterPropertyMap.get(targetClass);
                boolean wasInMap = false;
                if (propertyMap != null) {
                    wasInMap = propertyMap.containsKey(mPropertyName);
                    if (wasInMap) {
                        java.lang.Long jniSetter = propertyMap.get(mPropertyName);
                        if (jniSetter != null) {
                            mJniSetter = jniSetter;
                        }
                    }
                }
                if (!wasInMap) {
                    java.lang.String methodName = android.animation.PropertyValuesHolder.getMethodName("set", mPropertyName);
                    calculateValue(0.0F);
                    float[] values = ((float[]) (getAnimatedValue()));
                    int numParams = values.length;
                    try {
                        mJniSetter = android.animation.PropertyValuesHolder.nGetMultipleFloatMethod(targetClass, methodName, numParams);
                    } catch (java.lang.NoSuchMethodError e) {
                        // try without the 'set' prefix
                        try {
                            mJniSetter = android.animation.PropertyValuesHolder.nGetMultipleFloatMethod(targetClass, mPropertyName, numParams);
                        } catch (java.lang.NoSuchMethodError e2) {
                            // just try reflection next
                        }
                    }
                    if (propertyMap == null) {
                        propertyMap = new java.util.HashMap<java.lang.String, java.lang.Long>();
                        android.animation.PropertyValuesHolder.MultiFloatValuesHolder.sJNISetterPropertyMap.put(targetClass, propertyMap);
                    }
                    propertyMap.put(mPropertyName, mJniSetter);
                }
            }
        }
    }

    static class MultiIntValuesHolder extends android.animation.PropertyValuesHolder {
        private long mJniSetter;

        private static final java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>> sJNISetterPropertyMap = new java.util.HashMap<java.lang.Class, java.util.HashMap<java.lang.String, java.lang.Long>>();

        public MultiIntValuesHolder(java.lang.String propertyName, android.animation.TypeConverter converter, android.animation.TypeEvaluator evaluator, java.lang.Object... values) {
            super(propertyName);
            setConverter(converter);
            setObjectValues(values);
            setEvaluator(evaluator);
        }

        public MultiIntValuesHolder(java.lang.String propertyName, android.animation.TypeConverter converter, android.animation.TypeEvaluator evaluator, android.animation.Keyframes keyframes) {
            super(propertyName);
            setConverter(converter);
            mKeyframes = keyframes;
            setEvaluator(evaluator);
        }

        /**
         * Internal function to set the value on the target object, using the setter set up
         * earlier on this PropertyValuesHolder object. This function is called by ObjectAnimator
         * to handle turning the value calculated by ValueAnimator into a value set on the object
         * according to the name of the property.
         *
         * @param target
         * 		The target object on which the value is set
         */
        @java.lang.Override
        void setAnimatedValue(java.lang.Object target) {
            int[] values = ((int[]) (getAnimatedValue()));
            int numParameters = values.length;
            if (mJniSetter != 0) {
                switch (numParameters) {
                    case 1 :
                        android.animation.PropertyValuesHolder.nCallIntMethod(target, mJniSetter, values[0]);
                        break;
                    case 2 :
                        android.animation.PropertyValuesHolder.nCallTwoIntMethod(target, mJniSetter, values[0], values[1]);
                        break;
                    case 4 :
                        android.animation.PropertyValuesHolder.nCallFourIntMethod(target, mJniSetter, values[0], values[1], values[2], values[3]);
                        break;
                    default :
                        {
                            android.animation.PropertyValuesHolder.nCallMultipleIntMethod(target, mJniSetter, values);
                            break;
                        }
                }
            }
        }

        /**
         * Internal function (called from ObjectAnimator) to set up the setter and getter
         * prior to running the animation. No getter can be used for multiple parameters.
         *
         * @param target
         * 		The object on which the setter exists.
         */
        @java.lang.Override
        void setupSetterAndGetter(java.lang.Object target) {
            setupSetter(target.getClass());
        }

        @java.lang.Override
        void setupSetter(java.lang.Class targetClass) {
            if (mJniSetter != 0) {
                return;
            }
            synchronized(android.animation.PropertyValuesHolder.MultiIntValuesHolder.sJNISetterPropertyMap) {
                java.util.HashMap<java.lang.String, java.lang.Long> propertyMap = android.animation.PropertyValuesHolder.MultiIntValuesHolder.sJNISetterPropertyMap.get(targetClass);
                boolean wasInMap = false;
                if (propertyMap != null) {
                    wasInMap = propertyMap.containsKey(mPropertyName);
                    if (wasInMap) {
                        java.lang.Long jniSetter = propertyMap.get(mPropertyName);
                        if (jniSetter != null) {
                            mJniSetter = jniSetter;
                        }
                    }
                }
                if (!wasInMap) {
                    java.lang.String methodName = android.animation.PropertyValuesHolder.getMethodName("set", mPropertyName);
                    calculateValue(0.0F);
                    int[] values = ((int[]) (getAnimatedValue()));
                    int numParams = values.length;
                    try {
                        mJniSetter = android.animation.PropertyValuesHolder.nGetMultipleIntMethod(targetClass, methodName, numParams);
                    } catch (java.lang.NoSuchMethodError e) {
                        // try without the 'set' prefix
                        try {
                            mJniSetter = android.animation.PropertyValuesHolder.nGetMultipleIntMethod(targetClass, mPropertyName, numParams);
                        } catch (java.lang.NoSuchMethodError e2) {
                            // couldn't find it.
                        }
                    }
                    if (propertyMap == null) {
                        propertyMap = new java.util.HashMap<java.lang.String, java.lang.Long>();
                        android.animation.PropertyValuesHolder.MultiIntValuesHolder.sJNISetterPropertyMap.put(targetClass, propertyMap);
                    }
                    propertyMap.put(mPropertyName, mJniSetter);
                }
            }
        }
    }

    /**
     * Convert from PointF to float[] for multi-float setters along a Path.
     */
    private static class PointFToFloatArray extends android.animation.TypeConverter<android.graphics.PointF, float[]> {
        private float[] mCoordinates = new float[2];

        public PointFToFloatArray() {
            super(android.graphics.PointF.class, float[].class);
        }

        @java.lang.Override
        public float[] convert(android.graphics.PointF value) {
            mCoordinates[0] = value.x;
            mCoordinates[1] = value.y;
            return mCoordinates;
        }
    }

    /**
     * Convert from PointF to int[] for multi-int setters along a Path.
     */
    private static class PointFToIntArray extends android.animation.TypeConverter<android.graphics.PointF, int[]> {
        private int[] mCoordinates = new int[2];

        public PointFToIntArray() {
            super(android.graphics.PointF.class, int[].class);
        }

        @java.lang.Override
        public int[] convert(android.graphics.PointF value) {
            mCoordinates[0] = java.lang.Math.round(value.x);
            mCoordinates[1] = java.lang.Math.round(value.y);
            return mCoordinates;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static class PropertyValues {
        public java.lang.String propertyName;

        public java.lang.Class type;

        public java.lang.Object startValue;

        public java.lang.Object endValue;

        public android.animation.PropertyValuesHolder.PropertyValues.DataSource dataSource = null;

        public interface DataSource {
            java.lang.Object getValueAtFraction(float fraction);
        }

        public java.lang.String toString() {
            return (((((("property name: " + propertyName) + ", type: ") + type) + ", startValue: ") + startValue.toString()) + ", endValue: ") + endValue.toString();
        }
    }

    private static native long nGetIntMethod(java.lang.Class targetClass, java.lang.String methodName);

    private static native long nGetFloatMethod(java.lang.Class targetClass, java.lang.String methodName);

    private static native long nGetMultipleIntMethod(java.lang.Class targetClass, java.lang.String methodName, int numParams);

    private static native long nGetMultipleFloatMethod(java.lang.Class targetClass, java.lang.String methodName, int numParams);

    private static native void nCallIntMethod(java.lang.Object target, long methodID, int arg);

    private static native void nCallFloatMethod(java.lang.Object target, long methodID, float arg);

    private static native void nCallTwoIntMethod(java.lang.Object target, long methodID, int arg1, int arg2);

    private static native void nCallFourIntMethod(java.lang.Object target, long methodID, int arg1, int arg2, int arg3, int arg4);

    private static native void nCallMultipleIntMethod(java.lang.Object target, long methodID, int[] args);

    private static native void nCallTwoFloatMethod(java.lang.Object target, long methodID, float arg1, float arg2);

    private static native void nCallFourFloatMethod(java.lang.Object target, long methodID, float arg1, float arg2, float arg3, float arg4);

    private static native void nCallMultipleFloatMethod(java.lang.Object target, long methodID, float[] args);
}

