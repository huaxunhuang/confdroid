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
 * This class is used to instantiate animator XML files into Animator objects.
 * <p>
 * For performance reasons, inflation relies heavily on pre-processing of
 * XML files that is done at build time. Therefore, it is not currently possible
 * to use this inflater with an XmlPullParser over a plain XML file at runtime;
 * it only works with an XmlPullParser returned from a compiled resource (R.
 * <em>something</em> file.)
 */
public class AnimatorInflater {
    private static final java.lang.String TAG = "AnimatorInflater";

    /**
     * These flags are used when parsing AnimatorSet objects
     */
    private static final int TOGETHER = 0;

    private static final int SEQUENTIALLY = 1;

    /**
     * Enum values used in XML attributes to indicate the value for mValueType
     */
    private static final int VALUE_TYPE_FLOAT = 0;

    private static final int VALUE_TYPE_INT = 1;

    private static final int VALUE_TYPE_PATH = 2;

    private static final int VALUE_TYPE_COLOR = 3;

    private static final int VALUE_TYPE_UNDEFINED = 4;

    private static final boolean DBG_ANIMATOR_INFLATER = false;

    // used to calculate changing configs for resource references
    private static final android.util.TypedValue sTmpTypedValue = new android.util.TypedValue();

    /**
     * Loads an {@link Animator} object from a resource
     *
     * @param context
     * 		Application context used to access resources
     * @param id
     * 		The resource id of the animation to load
     * @return The animator object reference by the specified id
     * @throws android.content.res.Resources.NotFoundException
     * 		when the animation cannot be loaded
     */
    public static android.animation.Animator loadAnimator(android.content.Context context, @android.annotation.AnimatorRes
    int id) throws android.content.res.Resources.NotFoundException {
        return android.animation.AnimatorInflater.loadAnimator(context.getResources(), context.getTheme(), id);
    }

    /**
     * Loads an {@link Animator} object from a resource
     *
     * @param resources
     * 		The resources
     * @param theme
     * 		The theme
     * @param id
     * 		The resource id of the animation to load
     * @return The animator object reference by the specified id
     * @throws android.content.res.Resources.NotFoundException
     * 		when the animation cannot be loaded
     * @unknown 
     */
    public static android.animation.Animator loadAnimator(android.content.res.Resources resources, android.content.res.Resources.Theme theme, int id) throws android.content.res.Resources.NotFoundException {
        return android.animation.AnimatorInflater.loadAnimator(resources, theme, id, 1);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.animation.Animator loadAnimator(android.content.res.Resources resources, android.content.res.Resources.Theme theme, int id, float pathErrorScale) throws android.content.res.Resources.NotFoundException {
        final android.content.res.ConfigurationBoundResourceCache<android.animation.Animator> animatorCache = resources.getAnimatorCache();
        android.animation.Animator animator = animatorCache.getInstance(id, resources, theme);
        if (animator != null) {
            if (android.animation.AnimatorInflater.DBG_ANIMATOR_INFLATER) {
                android.util.Log.d(android.animation.AnimatorInflater.TAG, "loaded animator from cache, " + resources.getResourceName(id));
            }
            return animator;
        } else
            if (android.animation.AnimatorInflater.DBG_ANIMATOR_INFLATER) {
                android.util.Log.d(android.animation.AnimatorInflater.TAG, "cache miss for animator " + resources.getResourceName(id));
            }

        android.content.res.XmlResourceParser parser = null;
        try {
            parser = resources.getAnimation(id);
            animator = android.animation.AnimatorInflater.createAnimatorFromXml(resources, theme, parser, pathErrorScale);
            if (animator != null) {
                animator.appendChangingConfigurations(android.animation.AnimatorInflater.getChangingConfigs(resources, id));
                final android.content.res.ConstantState<android.animation.Animator> constantState = animator.createConstantState();
                if (constantState != null) {
                    if (android.animation.AnimatorInflater.DBG_ANIMATOR_INFLATER) {
                        android.util.Log.d(android.animation.AnimatorInflater.TAG, "caching animator for res " + resources.getResourceName(id));
                    }
                    animatorCache.put(id, theme, constantState);
                    // create a new animator so that cached version is never used by the user
                    animator = constantState.newInstance(resources, theme);
                }
            }
            return animator;
        } catch (org.xmlpull.v1.XmlPullParserException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (java.io.IOException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load animation resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    public static android.animation.StateListAnimator loadStateListAnimator(android.content.Context context, int id) throws android.content.res.Resources.NotFoundException {
        final android.content.res.Resources resources = context.getResources();
        final android.content.res.ConfigurationBoundResourceCache<android.animation.StateListAnimator> cache = resources.getStateListAnimatorCache();
        final android.content.res.Resources.Theme theme = context.getTheme();
        android.animation.StateListAnimator animator = cache.getInstance(id, resources, theme);
        if (animator != null) {
            return animator;
        }
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = resources.getAnimation(id);
            animator = android.animation.AnimatorInflater.createStateListAnimatorFromXml(context, parser, android.util.Xml.asAttributeSet(parser));
            if (animator != null) {
                animator.appendChangingConfigurations(android.animation.AnimatorInflater.getChangingConfigs(resources, id));
                final android.content.res.ConstantState<android.animation.StateListAnimator> constantState = animator.createConstantState();
                if (constantState != null) {
                    cache.put(id, theme, constantState);
                    // return a clone so that the animator in constant state is never used.
                    animator = constantState.newInstance(resources, theme);
                }
            }
            return animator;
        } catch (org.xmlpull.v1.XmlPullParserException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load state list animator resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (java.io.IOException ex) {
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException("Can't load state list animator resource ID #0x" + java.lang.Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static android.animation.StateListAnimator createStateListAnimatorFromXml(android.content.Context context, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attributeSet) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        android.animation.StateListAnimator stateListAnimator = new android.animation.StateListAnimator();
        while (true) {
            type = parser.next();
            switch (type) {
                case org.xmlpull.v1.XmlPullParser.END_DOCUMENT :
                case org.xmlpull.v1.XmlPullParser.END_TAG :
                    return stateListAnimator;
                case org.xmlpull.v1.XmlPullParser.START_TAG :
                    // parse item
                    android.animation.Animator animator = null;
                    if ("item".equals(parser.getName())) {
                        int attributeCount = parser.getAttributeCount();
                        int[] states = new int[attributeCount];
                        int stateIndex = 0;
                        for (int i = 0; i < attributeCount; i++) {
                            int attrName = attributeSet.getAttributeNameResource(i);
                            if (attrName == R.attr.animation) {
                                final int animId = attributeSet.getAttributeResourceValue(i, 0);
                                animator = android.animation.AnimatorInflater.loadAnimator(context, animId);
                            } else {
                                states[stateIndex++] = (attributeSet.getAttributeBooleanValue(i, false)) ? attrName : -attrName;
                            }
                        }
                        if (animator == null) {
                            animator = android.animation.AnimatorInflater.createAnimatorFromXml(context.getResources(), context.getTheme(), parser, 1.0F);
                        }
                        if (animator == null) {
                            throw new android.content.res.Resources.NotFoundException("animation state item must have a valid animation");
                        }
                        stateListAnimator.addState(android.util.StateSet.trimStateSet(states, stateIndex), animator);
                    }
                    break;
            }
        } 
    }

    /**
     * PathDataEvaluator is used to interpolate between two paths which are
     * represented in the same format but different control points' values.
     * The path is represented as verbs and points for each of the verbs.
     */
    private static class PathDataEvaluator implements android.animation.TypeEvaluator<android.util.PathParser.PathData> {
        private final android.util.PathParser.PathData mPathData = new android.util.PathParser.PathData();

        @java.lang.Override
        public android.util.PathParser.PathData evaluate(float fraction, android.util.PathParser.PathData startPathData, android.util.PathParser.PathData endPathData) {
            if (!android.util.PathParser.interpolatePathData(mPathData, startPathData, endPathData, fraction)) {
                throw new java.lang.IllegalArgumentException("Can't interpolate between" + " two incompatible pathData");
            }
            return mPathData;
        }
    }

    private static android.animation.PropertyValuesHolder getPVH(android.content.res.TypedArray styledAttributes, int valueType, int valueFromId, int valueToId, java.lang.String propertyName) {
        android.util.TypedValue tvFrom = styledAttributes.peekValue(valueFromId);
        boolean hasFrom = tvFrom != null;
        int fromType = (hasFrom) ? tvFrom.type : 0;
        android.util.TypedValue tvTo = styledAttributes.peekValue(valueToId);
        boolean hasTo = tvTo != null;
        int toType = (hasTo) ? tvTo.type : 0;
        if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_UNDEFINED) {
            // Check whether it's color type. If not, fall back to default type (i.e. float type)
            if ((hasFrom && android.animation.AnimatorInflater.isColorType(fromType)) || (hasTo && android.animation.AnimatorInflater.isColorType(toType))) {
                valueType = android.animation.AnimatorInflater.VALUE_TYPE_COLOR;
            } else {
                valueType = android.animation.AnimatorInflater.VALUE_TYPE_FLOAT;
            }
        }
        boolean getFloats = valueType == android.animation.AnimatorInflater.VALUE_TYPE_FLOAT;
        android.animation.PropertyValuesHolder returnValue = null;
        if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_PATH) {
            java.lang.String fromString = styledAttributes.getString(valueFromId);
            java.lang.String toString = styledAttributes.getString(valueToId);
            android.util.PathParser.PathData nodesFrom = (fromString == null) ? null : new android.util.PathParser.PathData(fromString);
            android.util.PathParser.PathData nodesTo = (toString == null) ? null : new android.util.PathParser.PathData(toString);
            if ((nodesFrom != null) || (nodesTo != null)) {
                if (nodesFrom != null) {
                    android.animation.TypeEvaluator evaluator = new android.animation.AnimatorInflater.PathDataEvaluator();
                    if (nodesTo != null) {
                        if (!android.util.PathParser.canMorph(nodesFrom, nodesTo)) {
                            throw new android.view.InflateException(((" Can't morph from " + fromString) + " to ") + toString);
                        }
                        returnValue = android.animation.PropertyValuesHolder.ofObject(propertyName, evaluator, nodesFrom, nodesTo);
                    } else {
                        returnValue = android.animation.PropertyValuesHolder.ofObject(propertyName, evaluator, ((java.lang.Object) (nodesFrom)));
                    }
                } else
                    if (nodesTo != null) {
                        android.animation.TypeEvaluator evaluator = new android.animation.AnimatorInflater.PathDataEvaluator();
                        returnValue = android.animation.PropertyValuesHolder.ofObject(propertyName, evaluator, ((java.lang.Object) (nodesTo)));
                    }

            }
        } else {
            android.animation.TypeEvaluator evaluator = null;
            // Integer and float value types are handled here.
            if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_COLOR) {
                // special case for colors: ignore valueType and get ints
                evaluator = android.animation.ArgbEvaluator.getInstance();
            }
            if (getFloats) {
                float valueFrom;
                float valueTo;
                if (hasFrom) {
                    if (fromType == android.util.TypedValue.TYPE_DIMENSION) {
                        valueFrom = styledAttributes.getDimension(valueFromId, 0.0F);
                    } else {
                        valueFrom = styledAttributes.getFloat(valueFromId, 0.0F);
                    }
                    if (hasTo) {
                        if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                            valueTo = styledAttributes.getDimension(valueToId, 0.0F);
                        } else {
                            valueTo = styledAttributes.getFloat(valueToId, 0.0F);
                        }
                        returnValue = android.animation.PropertyValuesHolder.ofFloat(propertyName, valueFrom, valueTo);
                    } else {
                        returnValue = android.animation.PropertyValuesHolder.ofFloat(propertyName, valueFrom);
                    }
                } else {
                    if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                        valueTo = styledAttributes.getDimension(valueToId, 0.0F);
                    } else {
                        valueTo = styledAttributes.getFloat(valueToId, 0.0F);
                    }
                    returnValue = android.animation.PropertyValuesHolder.ofFloat(propertyName, valueTo);
                }
            } else {
                int valueFrom;
                int valueTo;
                if (hasFrom) {
                    if (fromType == android.util.TypedValue.TYPE_DIMENSION) {
                        valueFrom = ((int) (styledAttributes.getDimension(valueFromId, 0.0F)));
                    } else
                        if (android.animation.AnimatorInflater.isColorType(fromType)) {
                            valueFrom = styledAttributes.getColor(valueFromId, 0);
                        } else {
                            valueFrom = styledAttributes.getInt(valueFromId, 0);
                        }

                    if (hasTo) {
                        if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                            valueTo = ((int) (styledAttributes.getDimension(valueToId, 0.0F)));
                        } else
                            if (android.animation.AnimatorInflater.isColorType(toType)) {
                                valueTo = styledAttributes.getColor(valueToId, 0);
                            } else {
                                valueTo = styledAttributes.getInt(valueToId, 0);
                            }

                        returnValue = android.animation.PropertyValuesHolder.ofInt(propertyName, valueFrom, valueTo);
                    } else {
                        returnValue = android.animation.PropertyValuesHolder.ofInt(propertyName, valueFrom);
                    }
                } else {
                    if (hasTo) {
                        if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                            valueTo = ((int) (styledAttributes.getDimension(valueToId, 0.0F)));
                        } else
                            if (android.animation.AnimatorInflater.isColorType(toType)) {
                                valueTo = styledAttributes.getColor(valueToId, 0);
                            } else {
                                valueTo = styledAttributes.getInt(valueToId, 0);
                            }

                        returnValue = android.animation.PropertyValuesHolder.ofInt(propertyName, valueTo);
                    }
                }
            }
            if ((returnValue != null) && (evaluator != null)) {
                returnValue.setEvaluator(evaluator);
            }
        }
        return returnValue;
    }

    /**
     *
     *
     * @param anim
     * 		The animator, must not be null
     * @param arrayAnimator
     * 		Incoming typed array for Animator's attributes.
     * @param arrayObjectAnimator
     * 		Incoming typed array for Object Animator's
     * 		attributes.
     * @param pixelSize
     * 		The relative pixel size, used to calculate the
     * 		maximum error for path animations.
     */
    private static void parseAnimatorFromTypeArray(android.animation.ValueAnimator anim, android.content.res.TypedArray arrayAnimator, android.content.res.TypedArray arrayObjectAnimator, float pixelSize) {
        long duration = arrayAnimator.getInt(R.styleable.Animator_duration, 300);
        long startDelay = arrayAnimator.getInt(R.styleable.Animator_startOffset, 0);
        int valueType = arrayAnimator.getInt(R.styleable.Animator_valueType, android.animation.AnimatorInflater.VALUE_TYPE_UNDEFINED);
        if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_UNDEFINED) {
            valueType = android.animation.AnimatorInflater.inferValueTypeFromValues(arrayAnimator, R.styleable.Animator_valueFrom, R.styleable.Animator_valueTo);
        }
        android.animation.PropertyValuesHolder pvh = android.animation.AnimatorInflater.getPVH(arrayAnimator, valueType, R.styleable.Animator_valueFrom, R.styleable.Animator_valueTo, "");
        if (pvh != null) {
            anim.setValues(pvh);
        }
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        if (arrayAnimator.hasValue(R.styleable.Animator_repeatCount)) {
            anim.setRepeatCount(arrayAnimator.getInt(R.styleable.Animator_repeatCount, 0));
        }
        if (arrayAnimator.hasValue(R.styleable.Animator_repeatMode)) {
            anim.setRepeatMode(arrayAnimator.getInt(R.styleable.Animator_repeatMode, android.animation.ValueAnimator.RESTART));
        }
        if (arrayObjectAnimator != null) {
            android.animation.AnimatorInflater.setupObjectAnimator(anim, arrayObjectAnimator, valueType == android.animation.AnimatorInflater.VALUE_TYPE_FLOAT, pixelSize);
        }
    }

    /**
     * Setup the Animator to achieve path morphing.
     *
     * @param anim
     * 		The target Animator which will be updated.
     * @param arrayAnimator
     * 		TypedArray for the ValueAnimator.
     * @return the PathDataEvaluator.
     */
    private static android.animation.TypeEvaluator setupAnimatorForPath(android.animation.ValueAnimator anim, android.content.res.TypedArray arrayAnimator) {
        android.animation.TypeEvaluator evaluator = null;
        java.lang.String fromString = arrayAnimator.getString(R.styleable.Animator_valueFrom);
        java.lang.String toString = arrayAnimator.getString(R.styleable.Animator_valueTo);
        android.util.PathParser.PathData pathDataFrom = (fromString == null) ? null : new android.util.PathParser.PathData(fromString);
        android.util.PathParser.PathData pathDataTo = (toString == null) ? null : new android.util.PathParser.PathData(toString);
        if (pathDataFrom != null) {
            if (pathDataTo != null) {
                anim.setObjectValues(pathDataFrom, pathDataTo);
                if (!android.util.PathParser.canMorph(pathDataFrom, pathDataTo)) {
                    throw new android.view.InflateException((((arrayAnimator.getPositionDescription() + " Can't morph from ") + fromString) + " to ") + toString);
                }
            } else {
                anim.setObjectValues(((java.lang.Object) (pathDataFrom)));
            }
            evaluator = new android.animation.AnimatorInflater.PathDataEvaluator();
        } else
            if (pathDataTo != null) {
                anim.setObjectValues(((java.lang.Object) (pathDataTo)));
                evaluator = new android.animation.AnimatorInflater.PathDataEvaluator();
            }

        if (android.animation.AnimatorInflater.DBG_ANIMATOR_INFLATER && (evaluator != null)) {
            android.util.Log.v(android.animation.AnimatorInflater.TAG, "create a new PathDataEvaluator here");
        }
        return evaluator;
    }

    /**
     * Setup ObjectAnimator's property or values from pathData.
     *
     * @param anim
     * 		The target Animator which will be updated.
     * @param arrayObjectAnimator
     * 		TypedArray for the ObjectAnimator.
     * @param getFloats
     * 		True if the value type is float.
     * @param pixelSize
     * 		The relative pixel size, used to calculate the
     * 		maximum error for path animations.
     */
    private static void setupObjectAnimator(android.animation.ValueAnimator anim, android.content.res.TypedArray arrayObjectAnimator, boolean getFloats, float pixelSize) {
        android.animation.ObjectAnimator oa = ((android.animation.ObjectAnimator) (anim));
        java.lang.String pathData = arrayObjectAnimator.getString(R.styleable.PropertyAnimator_pathData);
        // Path can be involved in an ObjectAnimator in the following 3 ways:
        // 1) Path morphing: the property to be animated is pathData, and valueFrom and valueTo
        // are both of pathType. valueType = pathType needs to be explicitly defined.
        // 2) A property in X or Y dimension can be animated along a path: the property needs to be
        // defined in propertyXName or propertyYName attribute, the path will be defined in the
        // pathData attribute. valueFrom and valueTo will not be necessary for this animation.
        // 3) PathInterpolator can also define a path (in pathData) for its interpolation curve.
        // Here we are dealing with case 2:
        if (pathData != null) {
            java.lang.String propertyXName = arrayObjectAnimator.getString(R.styleable.PropertyAnimator_propertyXName);
            java.lang.String propertyYName = arrayObjectAnimator.getString(R.styleable.PropertyAnimator_propertyYName);
            if ((propertyXName == null) && (propertyYName == null)) {
                throw new android.view.InflateException(arrayObjectAnimator.getPositionDescription() + " propertyXName or propertyYName is needed for PathData");
            } else {
                android.graphics.Path path = android.util.PathParser.createPathFromPathData(pathData);
                float error = 0.5F * pixelSize;// max half a pixel error

                android.animation.PathKeyframes keyframeSet = android.animation.KeyframeSet.ofPath(path, error);
                android.animation.Keyframes xKeyframes;
                android.animation.Keyframes yKeyframes;
                if (getFloats) {
                    xKeyframes = keyframeSet.createXFloatKeyframes();
                    yKeyframes = keyframeSet.createYFloatKeyframes();
                } else {
                    xKeyframes = keyframeSet.createXIntKeyframes();
                    yKeyframes = keyframeSet.createYIntKeyframes();
                }
                android.animation.PropertyValuesHolder x = null;
                android.animation.PropertyValuesHolder y = null;
                if (propertyXName != null) {
                    x = android.animation.PropertyValuesHolder.ofKeyframes(propertyXName, xKeyframes);
                }
                if (propertyYName != null) {
                    y = android.animation.PropertyValuesHolder.ofKeyframes(propertyYName, yKeyframes);
                }
                if (x == null) {
                    oa.setValues(y);
                } else
                    if (y == null) {
                        oa.setValues(x);
                    } else {
                        oa.setValues(x, y);
                    }

            }
        } else {
            java.lang.String propertyName = arrayObjectAnimator.getString(R.styleable.PropertyAnimator_propertyName);
            oa.setPropertyName(propertyName);
        }
    }

    /**
     * Setup ValueAnimator's values.
     * This will handle all of the integer, float and color types.
     *
     * @param anim
     * 		The target Animator which will be updated.
     * @param arrayAnimator
     * 		TypedArray for the ValueAnimator.
     * @param getFloats
     * 		True if the value type is float.
     * @param hasFrom
     * 		True if "valueFrom" exists.
     * @param fromType
     * 		The type of "valueFrom".
     * @param hasTo
     * 		True if "valueTo" exists.
     * @param toType
     * 		The type of "valueTo".
     */
    private static void setupValues(android.animation.ValueAnimator anim, android.content.res.TypedArray arrayAnimator, boolean getFloats, boolean hasFrom, int fromType, boolean hasTo, int toType) {
        int valueFromIndex = R.styleable.Animator_valueFrom;
        int valueToIndex = R.styleable.Animator_valueTo;
        if (getFloats) {
            float valueFrom;
            float valueTo;
            if (hasFrom) {
                if (fromType == android.util.TypedValue.TYPE_DIMENSION) {
                    valueFrom = arrayAnimator.getDimension(valueFromIndex, 0.0F);
                } else {
                    valueFrom = arrayAnimator.getFloat(valueFromIndex, 0.0F);
                }
                if (hasTo) {
                    if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                        valueTo = arrayAnimator.getDimension(valueToIndex, 0.0F);
                    } else {
                        valueTo = arrayAnimator.getFloat(valueToIndex, 0.0F);
                    }
                    anim.setFloatValues(valueFrom, valueTo);
                } else {
                    anim.setFloatValues(valueFrom);
                }
            } else {
                if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                    valueTo = arrayAnimator.getDimension(valueToIndex, 0.0F);
                } else {
                    valueTo = arrayAnimator.getFloat(valueToIndex, 0.0F);
                }
                anim.setFloatValues(valueTo);
            }
        } else {
            int valueFrom;
            int valueTo;
            if (hasFrom) {
                if (fromType == android.util.TypedValue.TYPE_DIMENSION) {
                    valueFrom = ((int) (arrayAnimator.getDimension(valueFromIndex, 0.0F)));
                } else
                    if (android.animation.AnimatorInflater.isColorType(fromType)) {
                        valueFrom = arrayAnimator.getColor(valueFromIndex, 0);
                    } else {
                        valueFrom = arrayAnimator.getInt(valueFromIndex, 0);
                    }

                if (hasTo) {
                    if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                        valueTo = ((int) (arrayAnimator.getDimension(valueToIndex, 0.0F)));
                    } else
                        if (android.animation.AnimatorInflater.isColorType(toType)) {
                            valueTo = arrayAnimator.getColor(valueToIndex, 0);
                        } else {
                            valueTo = arrayAnimator.getInt(valueToIndex, 0);
                        }

                    anim.setIntValues(valueFrom, valueTo);
                } else {
                    anim.setIntValues(valueFrom);
                }
            } else {
                if (hasTo) {
                    if (toType == android.util.TypedValue.TYPE_DIMENSION) {
                        valueTo = ((int) (arrayAnimator.getDimension(valueToIndex, 0.0F)));
                    } else
                        if (android.animation.AnimatorInflater.isColorType(toType)) {
                            valueTo = arrayAnimator.getColor(valueToIndex, 0);
                        } else {
                            valueTo = arrayAnimator.getInt(valueToIndex, 0);
                        }

                    anim.setIntValues(valueTo);
                }
            }
        }
    }

    private static android.animation.Animator createAnimatorFromXml(android.content.res.Resources res, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser, float pixelSize) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return android.animation.AnimatorInflater.createAnimatorFromXml(res, theme, parser, android.util.Xml.asAttributeSet(parser), null, 0, pixelSize);
    }

    private static android.animation.Animator createAnimatorFromXml(android.content.res.Resources res, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.animation.AnimatorSet parent, int sequenceOrdering, float pixelSize) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.animation.Animator anim = null;
        java.util.ArrayList<android.animation.Animator> childAnims = null;
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            boolean gotValues = false;
            if (name.equals("objectAnimator")) {
                anim = android.animation.AnimatorInflater.loadObjectAnimator(res, theme, attrs, pixelSize);
            } else
                if (name.equals("animator")) {
                    anim = android.animation.AnimatorInflater.loadAnimator(res, theme, attrs, null, pixelSize);
                } else
                    if (name.equals("set")) {
                        anim = new android.animation.AnimatorSet();
                        android.content.res.TypedArray a;
                        if (theme != null) {
                            a = theme.obtainStyledAttributes(attrs, R.styleable.AnimatorSet, 0, 0);
                        } else {
                            a = res.obtainAttributes(attrs, R.styleable.AnimatorSet);
                        }
                        anim.appendChangingConfigurations(a.getChangingConfigurations());
                        int ordering = a.getInt(R.styleable.AnimatorSet_ordering, android.animation.AnimatorInflater.TOGETHER);
                        android.animation.AnimatorInflater.createAnimatorFromXml(res, theme, parser, attrs, ((android.animation.AnimatorSet) (anim)), ordering, pixelSize);
                        a.recycle();
                    } else
                        if (name.equals("propertyValuesHolder")) {
                            android.animation.PropertyValuesHolder[] values = android.animation.AnimatorInflater.loadValues(res, theme, parser, android.util.Xml.asAttributeSet(parser));
                            if (((values != null) && (anim != null)) && (anim instanceof android.animation.ValueAnimator)) {
                                ((android.animation.ValueAnimator) (anim)).setValues(values);
                            }
                            gotValues = true;
                        } else {
                            throw new java.lang.RuntimeException("Unknown animator name: " + parser.getName());
                        }



            if ((parent != null) && (!gotValues)) {
                if (childAnims == null) {
                    childAnims = new java.util.ArrayList<android.animation.Animator>();
                }
                childAnims.add(anim);
            }
        } 
        if ((parent != null) && (childAnims != null)) {
            android.animation.Animator[] animsArray = new android.animation.Animator[childAnims.size()];
            int index = 0;
            for (android.animation.Animator a : childAnims) {
                animsArray[index++] = a;
            }
            if (sequenceOrdering == android.animation.AnimatorInflater.TOGETHER) {
                parent.playTogether(animsArray);
            } else {
                parent.playSequentially(animsArray);
            }
        }
        return anim;
    }

    private static android.animation.PropertyValuesHolder[] loadValues(android.content.res.Resources res, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.util.ArrayList<android.animation.PropertyValuesHolder> values = null;
        int type;
        while (((type = parser.getEventType()) != org.xmlpull.v1.XmlPullParser.END_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            java.lang.String name = parser.getName();
            if (name.equals("propertyValuesHolder")) {
                android.content.res.TypedArray a;
                if (theme != null) {
                    a = theme.obtainStyledAttributes(attrs, R.styleable.PropertyValuesHolder, 0, 0);
                } else {
                    a = res.obtainAttributes(attrs, R.styleable.PropertyValuesHolder);
                }
                java.lang.String propertyName = a.getString(R.styleable.PropertyValuesHolder_propertyName);
                int valueType = a.getInt(R.styleable.PropertyValuesHolder_valueType, android.animation.AnimatorInflater.VALUE_TYPE_UNDEFINED);
                android.animation.PropertyValuesHolder pvh = android.animation.AnimatorInflater.loadPvh(res, theme, parser, propertyName, valueType);
                if (pvh == null) {
                    pvh = android.animation.AnimatorInflater.getPVH(a, valueType, R.styleable.PropertyValuesHolder_valueFrom, R.styleable.PropertyValuesHolder_valueTo, propertyName);
                }
                if (pvh != null) {
                    if (values == null) {
                        values = new java.util.ArrayList<android.animation.PropertyValuesHolder>();
                    }
                    values.add(pvh);
                }
                a.recycle();
            }
            parser.next();
        } 
        android.animation.PropertyValuesHolder[] valuesArray = null;
        if (values != null) {
            int count = values.size();
            valuesArray = new android.animation.PropertyValuesHolder[count];
            for (int i = 0; i < count; ++i) {
                valuesArray[i] = values.get(i);
            }
        }
        return valuesArray;
    }

    // When no value type is provided in keyframe, we need to infer the type from the value. i.e.
    // if value is defined in the style of a color value, then the color type is returned.
    // Otherwise, default float type is returned.
    private static int inferValueTypeOfKeyframe(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs) {
        int valueType;
        android.content.res.TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.Keyframe, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.Keyframe);
        }
        android.util.TypedValue keyframeValue = a.peekValue(R.styleable.Keyframe_value);
        boolean hasValue = keyframeValue != null;
        // When no value type is provided, check whether it's a color type first.
        // If not, fall back to default value type (i.e. float type).
        if (hasValue && android.animation.AnimatorInflater.isColorType(keyframeValue.type)) {
            valueType = android.animation.AnimatorInflater.VALUE_TYPE_COLOR;
        } else {
            valueType = android.animation.AnimatorInflater.VALUE_TYPE_FLOAT;
        }
        a.recycle();
        return valueType;
    }

    private static int inferValueTypeFromValues(android.content.res.TypedArray styledAttributes, int valueFromId, int valueToId) {
        android.util.TypedValue tvFrom = styledAttributes.peekValue(valueFromId);
        boolean hasFrom = tvFrom != null;
        int fromType = (hasFrom) ? tvFrom.type : 0;
        android.util.TypedValue tvTo = styledAttributes.peekValue(valueToId);
        boolean hasTo = tvTo != null;
        int toType = (hasTo) ? tvTo.type : 0;
        int valueType;
        // Check whether it's color type. If not, fall back to default type (i.e. float type)
        if ((hasFrom && android.animation.AnimatorInflater.isColorType(fromType)) || (hasTo && android.animation.AnimatorInflater.isColorType(toType))) {
            valueType = android.animation.AnimatorInflater.VALUE_TYPE_COLOR;
        } else {
            valueType = android.animation.AnimatorInflater.VALUE_TYPE_FLOAT;
        }
        return valueType;
    }

    private static void dumpKeyframes(java.lang.Object[] keyframes, java.lang.String header) {
        if ((keyframes == null) || (keyframes.length == 0)) {
            return;
        }
        android.util.Log.d(android.animation.AnimatorInflater.TAG, header);
        int count = keyframes.length;
        for (int i = 0; i < count; ++i) {
            android.animation.Keyframe keyframe = ((android.animation.Keyframe) (keyframes[i]));
            android.util.Log.d(android.animation.AnimatorInflater.TAG, ((((("Keyframe " + i) + ": fraction ") + (keyframe.getFraction() < 0 ? "null" : keyframe.getFraction())) + ", ") + ", value : ") + (keyframe.hasValue() ? keyframe.getValue() : "null"));
        }
    }

    // Load property values holder if there are keyframes defined in it. Otherwise return null.
    private static android.animation.PropertyValuesHolder loadPvh(android.content.res.Resources res, android.content.res.Resources.Theme theme, org.xmlpull.v1.XmlPullParser parser, java.lang.String propertyName, int valueType) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.animation.PropertyValuesHolder value = null;
        java.util.ArrayList<android.animation.Keyframe> keyframes = null;
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            java.lang.String name = parser.getName();
            if (name.equals("keyframe")) {
                if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_UNDEFINED) {
                    valueType = android.animation.AnimatorInflater.inferValueTypeOfKeyframe(res, theme, android.util.Xml.asAttributeSet(parser));
                }
                android.animation.Keyframe keyframe = android.animation.AnimatorInflater.loadKeyframe(res, theme, android.util.Xml.asAttributeSet(parser), valueType);
                if (keyframe != null) {
                    if (keyframes == null) {
                        keyframes = new java.util.ArrayList<android.animation.Keyframe>();
                    }
                    keyframes.add(keyframe);
                }
                parser.next();
            }
        } 
        int count;
        if ((keyframes != null) && ((count = keyframes.size()) > 0)) {
            // make sure we have keyframes at 0 and 1
            // If we have keyframes with set fractions, add keyframes at start/end
            // appropriately. If start/end have no set fractions:
            // if there's only one keyframe, set its fraction to 1 and add one at 0
            // if >1 keyframe, set the last fraction to 1, the first fraction to 0
            android.animation.Keyframe firstKeyframe = keyframes.get(0);
            android.animation.Keyframe lastKeyframe = keyframes.get(count - 1);
            float endFraction = lastKeyframe.getFraction();
            if (endFraction < 1) {
                if (endFraction < 0) {
                    lastKeyframe.setFraction(1);
                } else {
                    keyframes.add(keyframes.size(), android.animation.AnimatorInflater.createNewKeyframe(lastKeyframe, 1));
                    ++count;
                }
            }
            float startFraction = firstKeyframe.getFraction();
            if (startFraction != 0) {
                if (startFraction < 0) {
                    firstKeyframe.setFraction(0);
                } else {
                    keyframes.add(0, android.animation.AnimatorInflater.createNewKeyframe(firstKeyframe, 0));
                    ++count;
                }
            }
            android.animation.Keyframe[] keyframeArray = new android.animation.Keyframe[count];
            keyframes.toArray(keyframeArray);
            for (int i = 0; i < count; ++i) {
                android.animation.Keyframe keyframe = keyframeArray[i];
                if (keyframe.getFraction() < 0) {
                    if (i == 0) {
                        keyframe.setFraction(0);
                    } else
                        if (i == (count - 1)) {
                            keyframe.setFraction(1);
                        } else {
                            // figure out the start/end parameters of the current gap
                            // in fractions and distribute the gap among those keyframes
                            int startIndex = i;
                            int endIndex = i;
                            for (int j = startIndex + 1; j < (count - 1); ++j) {
                                if (keyframeArray[j].getFraction() >= 0) {
                                    break;
                                }
                                endIndex = j;
                            }
                            float gap = keyframeArray[endIndex + 1].getFraction() - keyframeArray[startIndex - 1].getFraction();
                            android.animation.AnimatorInflater.distributeKeyframes(keyframeArray, gap, startIndex, endIndex);
                        }

                }
            }
            value = android.animation.PropertyValuesHolder.ofKeyframe(propertyName, keyframeArray);
            if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_COLOR) {
                value.setEvaluator(android.animation.ArgbEvaluator.getInstance());
            }
        }
        return value;
    }

    private static android.animation.Keyframe createNewKeyframe(android.animation.Keyframe sampleKeyframe, float fraction) {
        return sampleKeyframe.getType() == float.class ? android.animation.Keyframe.ofFloat(fraction) : sampleKeyframe.getType() == int.class ? android.animation.Keyframe.ofInt(fraction) : android.animation.Keyframe.ofObject(fraction);
    }

    /**
     * Utility function to set fractions on keyframes to cover a gap in which the
     * fractions are not currently set. Keyframe fractions will be distributed evenly
     * in this gap. For example, a gap of 1 keyframe in the range 0-1 will be at .5, a gap
     * of .6 spread between two keyframes will be at .2 and .4 beyond the fraction at the
     * keyframe before startIndex.
     * Assumptions:
     * - First and last keyframe fractions (bounding this spread) are already set. So,
     * for example, if no fractions are set, we will already set first and last keyframe
     * fraction values to 0 and 1.
     * - startIndex must be >0 (which follows from first assumption).
     * - endIndex must be >= startIndex.
     *
     * @param keyframes
     * 		the array of keyframes
     * @param gap
     * 		The total gap we need to distribute
     * @param startIndex
     * 		The index of the first keyframe whose fraction must be set
     * @param endIndex
     * 		The index of the last keyframe whose fraction must be set
     */
    private static void distributeKeyframes(android.animation.Keyframe[] keyframes, float gap, int startIndex, int endIndex) {
        int count = (endIndex - startIndex) + 2;
        float increment = gap / count;
        for (int i = startIndex; i <= endIndex; ++i) {
            keyframes[i].setFraction(keyframes[i - 1].getFraction() + increment);
        }
    }

    private static android.animation.Keyframe loadKeyframe(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs, int valueType) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.Keyframe, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.Keyframe);
        }
        android.animation.Keyframe keyframe = null;
        float fraction = a.getFloat(R.styleable.Keyframe_fraction, -1);
        android.util.TypedValue keyframeValue = a.peekValue(R.styleable.Keyframe_value);
        boolean hasValue = keyframeValue != null;
        if (valueType == android.animation.AnimatorInflater.VALUE_TYPE_UNDEFINED) {
            // When no value type is provided, check whether it's a color type first.
            // If not, fall back to default value type (i.e. float type).
            if (hasValue && android.animation.AnimatorInflater.isColorType(keyframeValue.type)) {
                valueType = android.animation.AnimatorInflater.VALUE_TYPE_COLOR;
            } else {
                valueType = android.animation.AnimatorInflater.VALUE_TYPE_FLOAT;
            }
        }
        if (hasValue) {
            switch (valueType) {
                case android.animation.AnimatorInflater.VALUE_TYPE_FLOAT :
                    float value = a.getFloat(R.styleable.Keyframe_value, 0);
                    keyframe = android.animation.Keyframe.ofFloat(fraction, value);
                    break;
                case android.animation.AnimatorInflater.VALUE_TYPE_COLOR :
                case android.animation.AnimatorInflater.VALUE_TYPE_INT :
                    int intValue = a.getInt(R.styleable.Keyframe_value, 0);
                    keyframe = android.animation.Keyframe.ofInt(fraction, intValue);
                    break;
            }
        } else {
            keyframe = (valueType == android.animation.AnimatorInflater.VALUE_TYPE_FLOAT) ? android.animation.Keyframe.ofFloat(fraction) : android.animation.Keyframe.ofInt(fraction);
        }
        final int resID = a.getResourceId(R.styleable.Keyframe_interpolator, 0);
        if (resID > 0) {
            final android.view.animation.Interpolator interpolator = android.view.animation.AnimationUtils.loadInterpolator(res, theme, resID);
            keyframe.setInterpolator(interpolator);
        }
        a.recycle();
        return keyframe;
    }

    private static android.animation.ObjectAnimator loadObjectAnimator(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs, float pathErrorScale) throws android.content.res.Resources.NotFoundException {
        android.animation.ObjectAnimator anim = new android.animation.ObjectAnimator();
        android.animation.AnimatorInflater.loadAnimator(res, theme, attrs, anim, pathErrorScale);
        return anim;
    }

    /**
     * Creates a new animation whose parameters come from the specified context
     * and attributes set.
     *
     * @param res
     * 		The resources
     * @param attrs
     * 		The set of attributes holding the animation parameters
     * @param anim
     * 		Null if this is a ValueAnimator, otherwise this is an
     * 		ObjectAnimator
     */
    private static android.animation.ValueAnimator loadAnimator(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet attrs, android.animation.ValueAnimator anim, float pathErrorScale) throws android.content.res.Resources.NotFoundException {
        android.content.res.TypedArray arrayAnimator = null;
        android.content.res.TypedArray arrayObjectAnimator = null;
        if (theme != null) {
            arrayAnimator = theme.obtainStyledAttributes(attrs, R.styleable.Animator, 0, 0);
        } else {
            arrayAnimator = res.obtainAttributes(attrs, R.styleable.Animator);
        }
        // If anim is not null, then it is an object animator.
        if (anim != null) {
            if (theme != null) {
                arrayObjectAnimator = theme.obtainStyledAttributes(attrs, R.styleable.PropertyAnimator, 0, 0);
            } else {
                arrayObjectAnimator = res.obtainAttributes(attrs, R.styleable.PropertyAnimator);
            }
            anim.appendChangingConfigurations(arrayObjectAnimator.getChangingConfigurations());
        }
        if (anim == null) {
            anim = new android.animation.ValueAnimator();
        }
        anim.appendChangingConfigurations(arrayAnimator.getChangingConfigurations());
        android.animation.AnimatorInflater.parseAnimatorFromTypeArray(anim, arrayAnimator, arrayObjectAnimator, pathErrorScale);
        final int resID = arrayAnimator.getResourceId(R.styleable.Animator_interpolator, 0);
        if (resID > 0) {
            final android.view.animation.Interpolator interpolator = android.view.animation.AnimationUtils.loadInterpolator(res, theme, resID);
            if (interpolator instanceof android.view.animation.BaseInterpolator) {
                anim.appendChangingConfigurations(((android.view.animation.BaseInterpolator) (interpolator)).getChangingConfiguration());
            }
            anim.setInterpolator(interpolator);
        }
        arrayAnimator.recycle();
        if (arrayObjectAnimator != null) {
            arrayObjectAnimator.recycle();
        }
        return anim;
    }

    @android.content.pm.ActivityInfo.Config
    private static int getChangingConfigs(@android.annotation.NonNull
    android.content.res.Resources resources, @android.annotation.AnyRes
    int id) {
        synchronized(android.animation.AnimatorInflater.sTmpTypedValue) {
            resources.getValue(id, android.animation.AnimatorInflater.sTmpTypedValue, true);
            return android.animation.AnimatorInflater.sTmpTypedValue.changingConfigurations;
        }
    }

    private static boolean isColorType(int type) {
        return (type >= android.util.TypedValue.TYPE_FIRST_COLOR_INT) && (type <= android.util.TypedValue.TYPE_LAST_COLOR_INT);
    }
}

