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
 * An animation that controls the position of an object. See the
 * {@link android.view.animation full package} description for details and
 * sample code.
 */
public class TranslateAnimation extends android.view.animation.Animation {
    private int mFromXType = android.view.animation.Animation.ABSOLUTE;

    private int mToXType = android.view.animation.Animation.ABSOLUTE;

    private int mFromYType = android.view.animation.Animation.ABSOLUTE;

    private int mToYType = android.view.animation.Animation.ABSOLUTE;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected float mFromXValue = 0.0F;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected float mToXValue = 0.0F;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected float mFromYValue = 0.0F;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected float mToYValue = 0.0F;

    /**
     *
     *
     * @unknown 
     */
    protected float mFromXDelta;

    /**
     *
     *
     * @unknown 
     */
    protected float mToXDelta;

    /**
     *
     *
     * @unknown 
     */
    protected float mFromYDelta;

    /**
     *
     *
     * @unknown 
     */
    protected float mToYDelta;

    /**
     * Constructor used when a TranslateAnimation is loaded from a resource.
     *
     * @param context
     * 		Application context to use
     * @param attrs
     * 		Attribute set from which to read values
     */
    public TranslateAnimation(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TranslateAnimation);
        android.view.animation.Animation.Description d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.TranslateAnimation_fromXDelta));
        mFromXType = d.type;
        mFromXValue = d.value;
        d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.TranslateAnimation_toXDelta));
        mToXType = d.type;
        mToXValue = d.value;
        d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.TranslateAnimation_fromYDelta));
        mFromYType = d.type;
        mFromYValue = d.value;
        d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.TranslateAnimation_toYDelta));
        mToYType = d.type;
        mToYValue = d.value;
        a.recycle();
    }

    /**
     * Constructor to use when building a TranslateAnimation from code
     *
     * @param fromXDelta
     * 		Change in X coordinate to apply at the start of the
     * 		animation
     * @param toXDelta
     * 		Change in X coordinate to apply at the end of the
     * 		animation
     * @param fromYDelta
     * 		Change in Y coordinate to apply at the start of the
     * 		animation
     * @param toYDelta
     * 		Change in Y coordinate to apply at the end of the
     * 		animation
     */
    public TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;
        mFromXType = android.view.animation.Animation.ABSOLUTE;
        mToXType = android.view.animation.Animation.ABSOLUTE;
        mFromYType = android.view.animation.Animation.ABSOLUTE;
        mToYType = android.view.animation.Animation.ABSOLUTE;
    }

    /**
     * Constructor to use when building a TranslateAnimation from code
     *
     * @param fromXType
     * 		Specifies how fromXValue should be interpreted. One of
     * 		Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     * 		Animation.RELATIVE_TO_PARENT.
     * @param fromXValue
     * 		Change in X coordinate to apply at the start of the
     * 		animation. This value can either be an absolute number if fromXType
     * 		is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toXType
     * 		Specifies how toXValue should be interpreted. One of
     * 		Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     * 		Animation.RELATIVE_TO_PARENT.
     * @param toXValue
     * 		Change in X coordinate to apply at the end of the
     * 		animation. This value can either be an absolute number if toXType
     * 		is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param fromYType
     * 		Specifies how fromYValue should be interpreted. One of
     * 		Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     * 		Animation.RELATIVE_TO_PARENT.
     * @param fromYValue
     * 		Change in Y coordinate to apply at the start of the
     * 		animation. This value can either be an absolute number if fromYType
     * 		is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toYType
     * 		Specifies how toYValue should be interpreted. One of
     * 		Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     * 		Animation.RELATIVE_TO_PARENT.
     * @param toYValue
     * 		Change in Y coordinate to apply at the end of the
     * 		animation. This value can either be an absolute number if toYType
     * 		is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType, float fromYValue, int toYType, float toYValue) {
        mFromXValue = fromXValue;
        mToXValue = toXValue;
        mFromYValue = fromYValue;
        mToYValue = toYValue;
        mFromXType = fromXType;
        mToXType = toXType;
        mFromYType = fromYType;
        mToYType = toYType;
    }

    @java.lang.Override
    protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
        float dx = mFromXDelta;
        float dy = mFromYDelta;
        if (mFromXDelta != mToXDelta) {
            dx = mFromXDelta + ((mToXDelta - mFromXDelta) * interpolatedTime);
        }
        if (mFromYDelta != mToYDelta) {
            dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
        }
        t.getMatrix().setTranslate(dx, dy);
    }

    @java.lang.Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
        mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
        mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
        mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);
    }
}

