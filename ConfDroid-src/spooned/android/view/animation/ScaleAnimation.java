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
 * An animation that controls the scale of an object. You can specify the point
 * to use for the center of scaling.
 */
public class ScaleAnimation extends android.view.animation.Animation {
    private final android.content.res.Resources mResources;

    private float mFromX;

    private float mToX;

    private float mFromY;

    private float mToY;

    private int mFromXType = android.util.TypedValue.TYPE_NULL;

    private int mToXType = android.util.TypedValue.TYPE_NULL;

    private int mFromYType = android.util.TypedValue.TYPE_NULL;

    private int mToYType = android.util.TypedValue.TYPE_NULL;

    private int mFromXData = 0;

    private int mToXData = 0;

    private int mFromYData = 0;

    private int mToYData = 0;

    private int mPivotXType = android.view.animation.Animation.ABSOLUTE;

    private int mPivotYType = android.view.animation.Animation.ABSOLUTE;

    private float mPivotXValue = 0.0F;

    private float mPivotYValue = 0.0F;

    private float mPivotX;

    private float mPivotY;

    /**
     * Constructor used when a ScaleAnimation is loaded from a resource.
     *
     * @param context
     * 		Application context to use
     * @param attrs
     * 		Attribute set from which to read values
     */
    public ScaleAnimation(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mResources = context.getResources();
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ScaleAnimation);
        android.util.TypedValue tv = a.peekValue(com.android.internal.R.styleable.ScaleAnimation_fromXScale);
        mFromX = 0.0F;
        if (tv != null) {
            if (tv.type == android.util.TypedValue.TYPE_FLOAT) {
                // This is a scaling factor.
                mFromX = tv.getFloat();
            } else {
                mFromXType = tv.type;
                mFromXData = tv.data;
            }
        }
        tv = a.peekValue(com.android.internal.R.styleable.ScaleAnimation_toXScale);
        mToX = 0.0F;
        if (tv != null) {
            if (tv.type == android.util.TypedValue.TYPE_FLOAT) {
                // This is a scaling factor.
                mToX = tv.getFloat();
            } else {
                mToXType = tv.type;
                mToXData = tv.data;
            }
        }
        tv = a.peekValue(com.android.internal.R.styleable.ScaleAnimation_fromYScale);
        mFromY = 0.0F;
        if (tv != null) {
            if (tv.type == android.util.TypedValue.TYPE_FLOAT) {
                // This is a scaling factor.
                mFromY = tv.getFloat();
            } else {
                mFromYType = tv.type;
                mFromYData = tv.data;
            }
        }
        tv = a.peekValue(com.android.internal.R.styleable.ScaleAnimation_toYScale);
        mToY = 0.0F;
        if (tv != null) {
            if (tv.type == android.util.TypedValue.TYPE_FLOAT) {
                // This is a scaling factor.
                mToY = tv.getFloat();
            } else {
                mToYType = tv.type;
                mToYData = tv.data;
            }
        }
        android.view.animation.Animation.Description d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.ScaleAnimation_pivotX));
        mPivotXType = d.type;
        mPivotXValue = d.value;
        d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.ScaleAnimation_pivotY));
        mPivotYType = d.type;
        mPivotYValue = d.value;
        a.recycle();
        initializePivotPoint();
    }

    /**
     * Constructor to use when building a ScaleAnimation from code
     *
     * @param fromX
     * 		Horizontal scaling factor to apply at the start of the
     * 		animation
     * @param toX
     * 		Horizontal scaling factor to apply at the end of the animation
     * @param fromY
     * 		Vertical scaling factor to apply at the start of the
     * 		animation
     * @param toY
     * 		Vertical scaling factor to apply at the end of the animation
     */
    public ScaleAnimation(float fromX, float toX, float fromY, float toY) {
        mResources = null;
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;
        mPivotX = 0;
        mPivotY = 0;
    }

    /**
     * Constructor to use when building a ScaleAnimation from code
     *
     * @param fromX
     * 		Horizontal scaling factor to apply at the start of the
     * 		animation
     * @param toX
     * 		Horizontal scaling factor to apply at the end of the animation
     * @param fromY
     * 		Vertical scaling factor to apply at the start of the
     * 		animation
     * @param toY
     * 		Vertical scaling factor to apply at the end of the animation
     * @param pivotX
     * 		The X coordinate of the point about which the object is
     * 		being scaled, specified as an absolute number where 0 is the left
     * 		edge. (This point remains fixed while the object changes size.)
     * @param pivotY
     * 		The Y coordinate of the point about which the object is
     * 		being scaled, specified as an absolute number where 0 is the top
     * 		edge. (This point remains fixed while the object changes size.)
     */
    public ScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotX, float pivotY) {
        mResources = null;
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;
        mPivotXType = android.view.animation.Animation.ABSOLUTE;
        mPivotYType = android.view.animation.Animation.ABSOLUTE;
        mPivotXValue = pivotX;
        mPivotYValue = pivotY;
        initializePivotPoint();
    }

    /**
     * Constructor to use when building a ScaleAnimation from code
     *
     * @param fromX
     * 		Horizontal scaling factor to apply at the start of the
     * 		animation
     * @param toX
     * 		Horizontal scaling factor to apply at the end of the animation
     * @param fromY
     * 		Vertical scaling factor to apply at the start of the
     * 		animation
     * @param toY
     * 		Vertical scaling factor to apply at the end of the animation
     * @param pivotXType
     * 		Specifies how pivotXValue should be interpreted. One of
     * 		Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     * 		Animation.RELATIVE_TO_PARENT.
     * @param pivotXValue
     * 		The X coordinate of the point about which the object
     * 		is being scaled, specified as an absolute number where 0 is the
     * 		left edge. (This point remains fixed while the object changes
     * 		size.) This value can either be an absolute number if pivotXType
     * 		is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param pivotYType
     * 		Specifies how pivotYValue should be interpreted. One of
     * 		Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     * 		Animation.RELATIVE_TO_PARENT.
     * @param pivotYValue
     * 		The Y coordinate of the point about which the object
     * 		is being scaled, specified as an absolute number where 0 is the
     * 		top edge. (This point remains fixed while the object changes
     * 		size.) This value can either be an absolute number if pivotYType
     * 		is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        mResources = null;
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;
        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;
        initializePivotPoint();
    }

    /**
     * Called at the end of constructor methods to initialize, if possible, values for
     * the pivot point. This is only possible for ABSOLUTE pivot values.
     */
    private void initializePivotPoint() {
        if (mPivotXType == android.view.animation.Animation.ABSOLUTE) {
            mPivotX = mPivotXValue;
        }
        if (mPivotYType == android.view.animation.Animation.ABSOLUTE) {
            mPivotY = mPivotYValue;
        }
    }

    @java.lang.Override
    protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
        float sx = 1.0F;
        float sy = 1.0F;
        float scale = getScaleFactor();
        if ((mFromX != 1.0F) || (mToX != 1.0F)) {
            sx = mFromX + ((mToX - mFromX) * interpolatedTime);
        }
        if ((mFromY != 1.0F) || (mToY != 1.0F)) {
            sy = mFromY + ((mToY - mFromY) * interpolatedTime);
        }
        if ((mPivotX == 0) && (mPivotY == 0)) {
            t.getMatrix().setScale(sx, sy);
        } else {
            t.getMatrix().setScale(sx, sy, scale * mPivotX, scale * mPivotY);
        }
    }

    float resolveScale(float scale, int type, int data, int size, int psize) {
        float targetSize;
        if (type == android.util.TypedValue.TYPE_FRACTION) {
            targetSize = android.util.TypedValue.complexToFraction(data, size, psize);
        } else
            if (type == android.util.TypedValue.TYPE_DIMENSION) {
                targetSize = android.util.TypedValue.complexToDimension(data, mResources.getDisplayMetrics());
            } else {
                return scale;
            }

        if (size == 0) {
            return 1;
        }
        return targetSize / ((float) (size));
    }

    @java.lang.Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mFromX = resolveScale(mFromX, mFromXType, mFromXData, width, parentWidth);
        mToX = resolveScale(mToX, mToXType, mToXData, width, parentWidth);
        mFromY = resolveScale(mFromY, mFromYType, mFromYData, height, parentHeight);
        mToY = resolveScale(mToY, mToYType, mToYData, height, parentHeight);
        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }
}

