/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A layout animation controller is used to animated a grid layout's children.
 *
 * While {@link LayoutAnimationController} relies only on the index of the child
 * in the view group to compute the animation delay, this class uses both the
 * X and Y coordinates of the child within a grid.
 *
 * In addition, the animation direction can be controlled. The default direction
 * is <code>DIRECTION_LEFT_TO_RIGHT | DIRECTION_TOP_TO_BOTTOM</code>. You can
 * also set the animation priority to columns or rows. The default priority is
 * none.
 *
 * Information used to compute the animation delay of each child are stored
 * in an instance of
 * {@link android.view.animation.GridLayoutAnimationController.AnimationParameters},
 * itself stored in the {@link android.view.ViewGroup.LayoutParams} of the view.
 *
 * @see LayoutAnimationController
 * @see android.widget.GridView
 * @unknown ref android.R.styleable#GridLayoutAnimation_columnDelay
 * @unknown ref android.R.styleable#GridLayoutAnimation_rowDelay
 * @unknown ref android.R.styleable#GridLayoutAnimation_direction
 * @unknown ref android.R.styleable#GridLayoutAnimation_directionPriority
 */
public class GridLayoutAnimationController extends android.view.animation.LayoutAnimationController {
    /**
     * Animates the children starting from the left of the grid to the right.
     */
    public static final int DIRECTION_LEFT_TO_RIGHT = 0x0;

    /**
     * Animates the children starting from the right of the grid to the left.
     */
    public static final int DIRECTION_RIGHT_TO_LEFT = 0x1;

    /**
     * Animates the children starting from the top of the grid to the bottom.
     */
    public static final int DIRECTION_TOP_TO_BOTTOM = 0x0;

    /**
     * Animates the children starting from the bottom of the grid to the top.
     */
    public static final int DIRECTION_BOTTOM_TO_TOP = 0x2;

    /**
     * Bitmask used to retrieve the horizontal component of the direction.
     */
    public static final int DIRECTION_HORIZONTAL_MASK = 0x1;

    /**
     * Bitmask used to retrieve the vertical component of the direction.
     */
    public static final int DIRECTION_VERTICAL_MASK = 0x2;

    /**
     * Rows and columns are animated at the same time.
     */
    public static final int PRIORITY_NONE = 0;

    /**
     * Columns are animated first.
     */
    public static final int PRIORITY_COLUMN = 1;

    /**
     * Rows are animated first.
     */
    public static final int PRIORITY_ROW = 2;

    private float mColumnDelay;

    private float mRowDelay;

    private int mDirection;

    private int mDirectionPriority;

    /**
     * Creates a new grid layout animation controller from external resources.
     *
     * @param context
     * 		the Context the view  group is running in, through which
     * 		it can access the resources
     * @param attrs
     * 		the attributes of the XML tag that is inflating the
     * 		layout animation controller
     */
    public GridLayoutAnimationController(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.GridLayoutAnimation);
        android.view.animation.Animation.Description d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.GridLayoutAnimation_columnDelay));
        mColumnDelay = d.value;
        d = android.view.animation.Animation.Description.parseValue(a.peekValue(com.android.internal.R.styleable.GridLayoutAnimation_rowDelay));
        mRowDelay = d.value;
        // noinspection PointlessBitwiseExpression
        mDirection = a.getInt(com.android.internal.R.styleable.GridLayoutAnimation_direction, android.view.animation.GridLayoutAnimationController.DIRECTION_LEFT_TO_RIGHT | android.view.animation.GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM);
        mDirectionPriority = a.getInt(com.android.internal.R.styleable.GridLayoutAnimation_directionPriority, android.view.animation.GridLayoutAnimationController.PRIORITY_NONE);
        a.recycle();
    }

    /**
     * Creates a new layout animation controller with a delay of 50%
     * for both rows and columns and the specified animation.
     *
     * @param animation
     * 		the animation to use on each child of the view group
     */
    public GridLayoutAnimationController(android.view.animation.Animation animation) {
        this(animation, 0.5F, 0.5F);
    }

    /**
     * Creates a new layout animation controller with the specified delays
     * and the specified animation.
     *
     * @param animation
     * 		the animation to use on each child of the view group
     * @param columnDelay
     * 		the delay by which each column animation must be offset
     * @param rowDelay
     * 		the delay by which each row animation must be offset
     */
    public GridLayoutAnimationController(android.view.animation.Animation animation, float columnDelay, float rowDelay) {
        super(animation);
        mColumnDelay = columnDelay;
        mRowDelay = rowDelay;
    }

    /**
     * Returns the delay by which the children's animation are offset from one
     * column to the other. The delay is expressed as a fraction of the
     * animation duration.
     *
     * @return a fraction of the animation duration
     * @see #setColumnDelay(float)
     * @see #getRowDelay()
     * @see #setRowDelay(float)
     */
    public float getColumnDelay() {
        return mColumnDelay;
    }

    /**
     * Sets the delay, as a fraction of the animation duration, by which the
     * children's animations are offset from one column to the other.
     *
     * @param columnDelay
     * 		a fraction of the animation duration
     * @see #getColumnDelay()
     * @see #getRowDelay()
     * @see #setRowDelay(float)
     */
    public void setColumnDelay(float columnDelay) {
        mColumnDelay = columnDelay;
    }

    /**
     * Returns the delay by which the children's animation are offset from one
     * row to the other. The delay is expressed as a fraction of the
     * animation duration.
     *
     * @return a fraction of the animation duration
     * @see #setRowDelay(float)
     * @see #getColumnDelay()
     * @see #setColumnDelay(float)
     */
    public float getRowDelay() {
        return mRowDelay;
    }

    /**
     * Sets the delay, as a fraction of the animation duration, by which the
     * children's animations are offset from one row to the other.
     *
     * @param rowDelay
     * 		a fraction of the animation duration
     * @see #getRowDelay()
     * @see #getColumnDelay()
     * @see #setColumnDelay(float)
     */
    public void setRowDelay(float rowDelay) {
        mRowDelay = rowDelay;
    }

    /**
     * Returns the direction of the animation. {@link #DIRECTION_HORIZONTAL_MASK}
     * and {@link #DIRECTION_VERTICAL_MASK} can be used to retrieve the
     * horizontal and vertical components of the direction.
     *
     * @return the direction of the animation
     * @see #setDirection(int)
     * @see #DIRECTION_BOTTOM_TO_TOP
     * @see #DIRECTION_TOP_TO_BOTTOM
     * @see #DIRECTION_LEFT_TO_RIGHT
     * @see #DIRECTION_RIGHT_TO_LEFT
     * @see #DIRECTION_HORIZONTAL_MASK
     * @see #DIRECTION_VERTICAL_MASK
     */
    public int getDirection() {
        return mDirection;
    }

    /**
     * Sets the direction of the animation. The direction is expressed as an
     * integer containing a horizontal and vertical component. For instance,
     * <code>DIRECTION_BOTTOM_TO_TOP | DIRECTION_RIGHT_TO_LEFT</code>.
     *
     * @param direction
     * 		the direction of the animation
     * @see #getDirection()
     * @see #DIRECTION_BOTTOM_TO_TOP
     * @see #DIRECTION_TOP_TO_BOTTOM
     * @see #DIRECTION_LEFT_TO_RIGHT
     * @see #DIRECTION_RIGHT_TO_LEFT
     * @see #DIRECTION_HORIZONTAL_MASK
     * @see #DIRECTION_VERTICAL_MASK
     */
    public void setDirection(int direction) {
        mDirection = direction;
    }

    /**
     * Returns the direction priority for the animation. The priority can
     * be either {@link #PRIORITY_NONE}, {@link #PRIORITY_COLUMN} or
     * {@link #PRIORITY_ROW}.
     *
     * @return the priority of the animation direction
     * @see #setDirectionPriority(int)
     * @see #PRIORITY_COLUMN
     * @see #PRIORITY_NONE
     * @see #PRIORITY_ROW
     */
    public int getDirectionPriority() {
        return mDirectionPriority;
    }

    /**
     * Specifies the direction priority of the animation. For instance,
     * {@link #PRIORITY_COLUMN} will give priority to columns: the animation
     * will first play on the column, then on the rows.Z
     *
     * @param directionPriority
     * 		the direction priority of the animation
     * @see #getDirectionPriority()
     * @see #PRIORITY_COLUMN
     * @see #PRIORITY_NONE
     * @see #PRIORITY_ROW
     */
    public void setDirectionPriority(int directionPriority) {
        mDirectionPriority = directionPriority;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean willOverlap() {
        return (mColumnDelay < 1.0F) || (mRowDelay < 1.0F);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected long getDelayForView(android.view.View view) {
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        android.view.animation.GridLayoutAnimationController.AnimationParameters params = ((android.view.animation.GridLayoutAnimationController.AnimationParameters) (lp.layoutAnimationParameters));
        if (params == null) {
            return 0;
        }
        final int column = getTransformedColumnIndex(params);
        final int row = getTransformedRowIndex(params);
        final int rowsCount = params.rowsCount;
        final int columnsCount = params.columnsCount;
        final long duration = mAnimation.getDuration();
        final float columnDelay = mColumnDelay * duration;
        final float rowDelay = mRowDelay * duration;
        float totalDelay;
        long viewDelay;
        if (mInterpolator == null) {
            mInterpolator = new android.view.animation.LinearInterpolator();
        }
        switch (mDirectionPriority) {
            case android.view.animation.GridLayoutAnimationController.PRIORITY_COLUMN :
                viewDelay = ((long) ((row * rowDelay) + ((column * rowsCount) * rowDelay)));
                totalDelay = (rowsCount * rowDelay) + ((columnsCount * rowsCount) * rowDelay);
                break;
            case android.view.animation.GridLayoutAnimationController.PRIORITY_ROW :
                viewDelay = ((long) ((column * columnDelay) + ((row * columnsCount) * columnDelay)));
                totalDelay = (columnsCount * columnDelay) + ((rowsCount * columnsCount) * columnDelay);
                break;
            case android.view.animation.GridLayoutAnimationController.PRIORITY_NONE :
            default :
                viewDelay = ((long) ((column * columnDelay) + (row * rowDelay)));
                totalDelay = (columnsCount * columnDelay) + (rowsCount * rowDelay);
                break;
        }
        float normalizedDelay = viewDelay / totalDelay;
        normalizedDelay = mInterpolator.getInterpolation(normalizedDelay);
        return ((long) (normalizedDelay * totalDelay));
    }

    private int getTransformedColumnIndex(android.view.animation.GridLayoutAnimationController.AnimationParameters params) {
        int index;
        switch (getOrder()) {
            case android.view.animation.LayoutAnimationController.ORDER_REVERSE :
                index = (params.columnsCount - 1) - params.column;
                break;
            case android.view.animation.LayoutAnimationController.ORDER_RANDOM :
                if (mRandomizer == null) {
                    mRandomizer = new java.util.Random();
                }
                index = ((int) (params.columnsCount * mRandomizer.nextFloat()));
                break;
            case android.view.animation.LayoutAnimationController.ORDER_NORMAL :
            default :
                index = params.column;
                break;
        }
        int direction = mDirection & android.view.animation.GridLayoutAnimationController.DIRECTION_HORIZONTAL_MASK;
        if (direction == android.view.animation.GridLayoutAnimationController.DIRECTION_RIGHT_TO_LEFT) {
            index = (params.columnsCount - 1) - index;
        }
        return index;
    }

    private int getTransformedRowIndex(android.view.animation.GridLayoutAnimationController.AnimationParameters params) {
        int index;
        switch (getOrder()) {
            case android.view.animation.LayoutAnimationController.ORDER_REVERSE :
                index = (params.rowsCount - 1) - params.row;
                break;
            case android.view.animation.LayoutAnimationController.ORDER_RANDOM :
                if (mRandomizer == null) {
                    mRandomizer = new java.util.Random();
                }
                index = ((int) (params.rowsCount * mRandomizer.nextFloat()));
                break;
            case android.view.animation.LayoutAnimationController.ORDER_NORMAL :
            default :
                index = params.row;
                break;
        }
        int direction = mDirection & android.view.animation.GridLayoutAnimationController.DIRECTION_VERTICAL_MASK;
        if (direction == android.view.animation.GridLayoutAnimationController.DIRECTION_BOTTOM_TO_TOP) {
            index = (params.rowsCount - 1) - index;
        }
        return index;
    }

    /**
     * The set of parameters that has to be attached to each view contained in
     * the view group animated by the grid layout animation controller. These
     * parameters are used to compute the start time of each individual view's
     * animation.
     */
    public static class AnimationParameters extends android.view.animation.LayoutAnimationController.AnimationParameters {
        /**
         * The view group's column to which the view belongs.
         */
        public int column;

        /**
         * The view group's row to which the view belongs.
         */
        public int row;

        /**
         * The number of columns in the view's enclosing grid layout.
         */
        public int columnsCount;

        /**
         * The number of rows in the view's enclosing grid layout.
         */
        public int rowsCount;
    }
}

