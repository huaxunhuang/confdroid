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
package android.widget;


/**
 * SlidingDrawer hides content out of the screen and allows the user to drag a handle
 * to bring the content on screen. SlidingDrawer can be used vertically or horizontally.
 *
 * A special widget composed of two children views: the handle, that the users drags,
 * and the content, attached to the handle and dragged with it.
 *
 * SlidingDrawer should be used as an overlay inside layouts. This means SlidingDrawer
 * should only be used inside of a FrameLayout or a RelativeLayout for instance. The
 * size of the SlidingDrawer defines how much space the content will occupy once slid
 * out so SlidingDrawer should usually use match_parent for both its dimensions.
 *
 * Inside an XML layout, SlidingDrawer must define the id of the handle and of the
 * content:
 *
 * <pre class="prettyprint">
 * &lt;SlidingDrawer
 *     android:id="@+id/drawer"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *
 *     android:handle="@+id/handle"
 *     android:content="@+id/content"&gt;
 *
 *     &lt;ImageView
 *         android:id="@id/handle"
 *         android:layout_width="88dip"
 *         android:layout_height="44dip" /&gt;
 *
 *     &lt;GridView
 *         android:id="@id/content"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" /&gt;
 *
 * &lt;/SlidingDrawer&gt;
 * </pre>
 *
 * @unknown ref android.R.styleable#SlidingDrawer_content
 * @unknown ref android.R.styleable#SlidingDrawer_handle
 * @unknown ref android.R.styleable#SlidingDrawer_topOffset
 * @unknown ref android.R.styleable#SlidingDrawer_bottomOffset
 * @unknown ref android.R.styleable#SlidingDrawer_orientation
 * @unknown ref android.R.styleable#SlidingDrawer_allowSingleTap
 * @unknown ref android.R.styleable#SlidingDrawer_animateOnClick
 * @deprecated This class is not supported anymore. It is recommended you
base your own implementation on the source code for the Android Open
Source Project if you must use it in your application.
 */
@java.lang.Deprecated
public class SlidingDrawer extends android.view.ViewGroup {
    public static final int ORIENTATION_HORIZONTAL = 0;

    public static final int ORIENTATION_VERTICAL = 1;

    private static final int TAP_THRESHOLD = 6;

    private static final float MAXIMUM_TAP_VELOCITY = 100.0F;

    private static final float MAXIMUM_MINOR_VELOCITY = 150.0F;

    private static final float MAXIMUM_MAJOR_VELOCITY = 200.0F;

    private static final float MAXIMUM_ACCELERATION = 2000.0F;

    private static final int VELOCITY_UNITS = 1000;

    private static final int ANIMATION_FRAME_DURATION = 1000 / 60;

    private static final int EXPANDED_FULL_OPEN = -10001;

    private static final int COLLAPSED_FULL_CLOSED = -10002;

    private final int mHandleId;

    private final int mContentId;

    private android.view.View mHandle;

    private android.view.View mContent;

    private final android.graphics.Rect mFrame = new android.graphics.Rect();

    private final android.graphics.Rect mInvalidate = new android.graphics.Rect();

    @android.annotation.UnsupportedAppUsage
    private boolean mTracking;

    private boolean mLocked;

    @android.annotation.UnsupportedAppUsage
    private android.view.VelocityTracker mVelocityTracker;

    private boolean mVertical;

    private boolean mExpanded;

    private int mBottomOffset;

    @android.annotation.UnsupportedAppUsage
    private int mTopOffset;

    private int mHandleHeight;

    private int mHandleWidth;

    private android.widget.SlidingDrawer.OnDrawerOpenListener mOnDrawerOpenListener;

    private android.widget.SlidingDrawer.OnDrawerCloseListener mOnDrawerCloseListener;

    private android.widget.SlidingDrawer.OnDrawerScrollListener mOnDrawerScrollListener;

    private float mAnimatedAcceleration;

    private float mAnimatedVelocity;

    private float mAnimationPosition;

    private long mAnimationLastTime;

    private long mCurrentAnimationTime;

    @android.annotation.UnsupportedAppUsage
    private int mTouchDelta;

    private boolean mAnimating;

    private boolean mAllowSingleTap;

    private boolean mAnimateOnClick;

    private final int mTapThreshold;

    private final int mMaximumTapVelocity;

    private final int mMaximumMinorVelocity;

    private final int mMaximumMajorVelocity;

    private final int mMaximumAcceleration;

    private final int mVelocityUnits;

    /**
     * Callback invoked when the drawer is opened.
     */
    public static interface OnDrawerOpenListener {
        /**
         * Invoked when the drawer becomes fully open.
         */
        public void onDrawerOpened();
    }

    /**
     * Callback invoked when the drawer is closed.
     */
    public static interface OnDrawerCloseListener {
        /**
         * Invoked when the drawer becomes fully closed.
         */
        public void onDrawerClosed();
    }

    /**
     * Callback invoked when the drawer is scrolled.
     */
    public static interface OnDrawerScrollListener {
        /**
         * Invoked when the user starts dragging/flinging the drawer's handle.
         */
        public void onScrollStarted();

        /**
         * Invoked when the user stops dragging/flinging the drawer's handle.
         */
        public void onScrollEnded();
    }

    /**
     * Creates a new SlidingDrawer from a specified set of attributes defined in XML.
     *
     * @param context
     * 		The application's environment.
     * @param attrs
     * 		The attributes defined in XML.
     */
    public SlidingDrawer(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Creates a new SlidingDrawer from a specified set of attributes defined in XML.
     *
     * @param context
     * 		The application's environment.
     * @param attrs
     * 		The attributes defined in XML.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public SlidingDrawer(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Creates a new SlidingDrawer from a specified set of attributes defined in XML.
     *
     * @param context
     * 		The application's environment.
     * @param attrs
     * 		The attributes defined in XML.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     */
    public SlidingDrawer(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingDrawer, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.SlidingDrawer, attrs, a, defStyleAttr, defStyleRes);
        int orientation = a.getInt(R.styleable.SlidingDrawer_orientation, android.widget.SlidingDrawer.ORIENTATION_VERTICAL);
        mVertical = orientation == android.widget.SlidingDrawer.ORIENTATION_VERTICAL;
        mBottomOffset = ((int) (a.getDimension(R.styleable.SlidingDrawer_bottomOffset, 0.0F)));
        mTopOffset = ((int) (a.getDimension(R.styleable.SlidingDrawer_topOffset, 0.0F)));
        mAllowSingleTap = a.getBoolean(R.styleable.SlidingDrawer_allowSingleTap, true);
        mAnimateOnClick = a.getBoolean(R.styleable.SlidingDrawer_animateOnClick, true);
        int handleId = a.getResourceId(R.styleable.SlidingDrawer_handle, 0);
        if (handleId == 0) {
            throw new java.lang.IllegalArgumentException("The handle attribute is required and must refer " + "to a valid child.");
        }
        int contentId = a.getResourceId(R.styleable.SlidingDrawer_content, 0);
        if (contentId == 0) {
            throw new java.lang.IllegalArgumentException("The content attribute is required and must refer " + "to a valid child.");
        }
        if (handleId == contentId) {
            throw new java.lang.IllegalArgumentException("The content and handle attributes must refer " + "to different children.");
        }
        mHandleId = handleId;
        mContentId = contentId;
        final float density = getResources().getDisplayMetrics().density;
        mTapThreshold = ((int) ((android.widget.SlidingDrawer.TAP_THRESHOLD * density) + 0.5F));
        mMaximumTapVelocity = ((int) ((android.widget.SlidingDrawer.MAXIMUM_TAP_VELOCITY * density) + 0.5F));
        mMaximumMinorVelocity = ((int) ((android.widget.SlidingDrawer.MAXIMUM_MINOR_VELOCITY * density) + 0.5F));
        mMaximumMajorVelocity = ((int) ((android.widget.SlidingDrawer.MAXIMUM_MAJOR_VELOCITY * density) + 0.5F));
        mMaximumAcceleration = ((int) ((android.widget.SlidingDrawer.MAXIMUM_ACCELERATION * density) + 0.5F));
        mVelocityUnits = ((int) ((android.widget.SlidingDrawer.VELOCITY_UNITS * density) + 0.5F));
        a.recycle();
        setAlwaysDrawnWithCacheEnabled(false);
    }

    @java.lang.Override
    protected void onFinishInflate() {
        mHandle = findViewById(mHandleId);
        if (mHandle == null) {
            throw new java.lang.IllegalArgumentException("The handle attribute is must refer to an" + " existing child.");
        }
        mHandle.setOnClickListener(new android.widget.SlidingDrawer.DrawerToggler());
        mContent = findViewById(mContentId);
        if (mContent == null) {
            throw new java.lang.IllegalArgumentException("The content attribute is must refer to an" + " existing child.");
        }
        mContent.setVisibility(android.view.View.GONE);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        if ((widthSpecMode == android.view.View.MeasureSpec.UNSPECIFIED) || (heightSpecMode == android.view.View.MeasureSpec.UNSPECIFIED)) {
            throw new java.lang.RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
        }
        final android.view.View handle = mHandle;
        measureChild(handle, widthMeasureSpec, heightMeasureSpec);
        if (mVertical) {
            int height = (heightSpecSize - handle.getMeasuredHeight()) - mTopOffset;
            mContent.measure(android.view.View.MeasureSpec.makeMeasureSpec(widthSpecSize, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY));
        } else {
            int width = (widthSpecSize - handle.getMeasuredWidth()) - mTopOffset;
            mContent.measure(android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(heightSpecSize, android.view.View.MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @java.lang.Override
    protected void dispatchDraw(android.graphics.Canvas canvas) {
        final long drawingTime = getDrawingTime();
        final android.view.View handle = mHandle;
        final boolean isVertical = mVertical;
        drawChild(canvas, handle, drawingTime);
        if (mTracking || mAnimating) {
            final android.graphics.Bitmap cache = mContent.getDrawingCache();
            if (cache != null) {
                if (isVertical) {
                    canvas.drawBitmap(cache, 0, handle.getBottom(), null);
                } else {
                    canvas.drawBitmap(cache, handle.getRight(), 0, null);
                }
            } else {
                canvas.save();
                canvas.translate(isVertical ? 0 : handle.getLeft() - mTopOffset, isVertical ? handle.getTop() - mTopOffset : 0);
                drawChild(canvas, mContent, drawingTime);
                canvas.restore();
            }
        } else
            if (mExpanded) {
                drawChild(canvas, mContent, drawingTime);
            }

    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTracking) {
            return;
        }
        final int width = r - l;
        final int height = b - t;
        final android.view.View handle = mHandle;
        int childWidth = handle.getMeasuredWidth();
        int childHeight = handle.getMeasuredHeight();
        int childLeft;
        int childTop;
        final android.view.View content = mContent;
        if (mVertical) {
            childLeft = (width - childWidth) / 2;
            childTop = (mExpanded) ? mTopOffset : (height - childHeight) + mBottomOffset;
            content.layout(0, mTopOffset + childHeight, content.getMeasuredWidth(), (mTopOffset + childHeight) + content.getMeasuredHeight());
        } else {
            childLeft = (mExpanded) ? mTopOffset : (width - childWidth) + mBottomOffset;
            childTop = (height - childHeight) / 2;
            content.layout(mTopOffset + childWidth, 0, (mTopOffset + childWidth) + content.getMeasuredWidth(), content.getMeasuredHeight());
        }
        handle.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        mHandleHeight = handle.getHeight();
        mHandleWidth = handle.getWidth();
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent event) {
        if (mLocked) {
            return false;
        }
        final int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        final android.graphics.Rect frame = mFrame;
        final android.view.View handle = mHandle;
        handle.getHitRect(frame);
        if ((!mTracking) && (!frame.contains(((int) (x)), ((int) (y))))) {
            return false;
        }
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            mTracking = true;
            handle.setPressed(true);
            // Must be called before prepareTracking()
            prepareContent();
            // Must be called after prepareContent()
            if (mOnDrawerScrollListener != null) {
                mOnDrawerScrollListener.onScrollStarted();
            }
            if (mVertical) {
                final int top = mHandle.getTop();
                mTouchDelta = ((int) (y)) - top;
                prepareTracking(top);
            } else {
                final int left = mHandle.getLeft();
                mTouchDelta = ((int) (x)) - left;
                prepareTracking(left);
            }
            mVelocityTracker.addMovement(event);
        }
        return true;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if (mLocked) {
            return true;
        }
        if (mTracking) {
            mVelocityTracker.addMovement(event);
            final int action = event.getAction();
            switch (action) {
                case android.view.MotionEvent.ACTION_MOVE :
                    moveHandle(((int) (mVertical ? event.getY() : event.getX())) - mTouchDelta);
                    break;
                case android.view.MotionEvent.ACTION_UP :
                case android.view.MotionEvent.ACTION_CANCEL :
                    {
                        final android.view.VelocityTracker velocityTracker = mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(mVelocityUnits);
                        float yVelocity = velocityTracker.getYVelocity();
                        float xVelocity = velocityTracker.getXVelocity();
                        boolean negative;
                        final boolean vertical = mVertical;
                        if (vertical) {
                            negative = yVelocity < 0;
                            if (xVelocity < 0) {
                                xVelocity = -xVelocity;
                            }
                            if (xVelocity > mMaximumMinorVelocity) {
                                xVelocity = mMaximumMinorVelocity;
                            }
                        } else {
                            negative = xVelocity < 0;
                            if (yVelocity < 0) {
                                yVelocity = -yVelocity;
                            }
                            if (yVelocity > mMaximumMinorVelocity) {
                                yVelocity = mMaximumMinorVelocity;
                            }
                        }
                        float velocity = ((float) (java.lang.Math.hypot(xVelocity, yVelocity)));
                        if (negative) {
                            velocity = -velocity;
                        }
                        final int top = mHandle.getTop();
                        final int left = mHandle.getLeft();
                        if (java.lang.Math.abs(velocity) < mMaximumTapVelocity) {
                            if (vertical ? (mExpanded && (top < (mTapThreshold + mTopOffset))) || ((!mExpanded) && (top > ((((mBottomOffset + mBottom) - mTop) - mHandleHeight) - mTapThreshold))) : (mExpanded && (left < (mTapThreshold + mTopOffset))) || ((!mExpanded) && (left > ((((mBottomOffset + mRight) - mLeft) - mHandleWidth) - mTapThreshold)))) {
                                if (mAllowSingleTap) {
                                    playSoundEffect(android.view.SoundEffectConstants.CLICK);
                                    if (mExpanded) {
                                        animateClose(vertical ? top : left, true);
                                    } else {
                                        animateOpen(vertical ? top : left, true);
                                    }
                                } else {
                                    performFling(vertical ? top : left, velocity, false, true);
                                }
                            } else {
                                performFling(vertical ? top : left, velocity, false, true);
                            }
                        } else {
                            performFling(vertical ? top : left, velocity, false, true);
                        }
                    }
                    break;
            }
        }
        return (mTracking || mAnimating) || super.onTouchEvent(event);
    }

    private void animateClose(int position, boolean notifyScrollListener) {
        prepareTracking(position);
        performFling(position, mMaximumAcceleration, true, notifyScrollListener);
    }

    private void animateOpen(int position, boolean notifyScrollListener) {
        prepareTracking(position);
        performFling(position, -mMaximumAcceleration, true, notifyScrollListener);
    }

    private void performFling(int position, float velocity, boolean always, boolean notifyScrollListener) {
        mAnimationPosition = position;
        mAnimatedVelocity = velocity;
        if (mExpanded) {
            if (always || ((velocity > mMaximumMajorVelocity) || ((position > (mTopOffset + (mVertical ? mHandleHeight : mHandleWidth))) && (velocity > (-mMaximumMajorVelocity))))) {
                // We are expanded, but they didn't move sufficiently to cause
                // us to retract.  Animate back to the expanded position.
                mAnimatedAcceleration = mMaximumAcceleration;
                if (velocity < 0) {
                    mAnimatedVelocity = 0;
                }
            } else {
                // We are expanded and are now going to animate away.
                mAnimatedAcceleration = -mMaximumAcceleration;
                if (velocity > 0) {
                    mAnimatedVelocity = 0;
                }
            }
        } else {
            if ((!always) && ((velocity > mMaximumMajorVelocity) || ((position > ((mVertical ? getHeight() : getWidth()) / 2)) && (velocity > (-mMaximumMajorVelocity))))) {
                // We are collapsed, and they moved enough to allow us to expand.
                mAnimatedAcceleration = mMaximumAcceleration;
                if (velocity < 0) {
                    mAnimatedVelocity = 0;
                }
            } else {
                // We are collapsed, but they didn't move sufficiently to cause
                // us to retract.  Animate back to the collapsed position.
                mAnimatedAcceleration = -mMaximumAcceleration;
                if (velocity > 0) {
                    mAnimatedVelocity = 0;
                }
            }
        }
        long now = android.os.SystemClock.uptimeMillis();
        mAnimationLastTime = now;
        mCurrentAnimationTime = now + android.widget.SlidingDrawer.ANIMATION_FRAME_DURATION;
        mAnimating = true;
        removeCallbacks(mSlidingRunnable);
        postDelayed(mSlidingRunnable, android.widget.SlidingDrawer.ANIMATION_FRAME_DURATION);
        stopTracking(notifyScrollListener);
    }

    @android.annotation.UnsupportedAppUsage
    private void prepareTracking(int position) {
        mTracking = true;
        mVelocityTracker = android.view.VelocityTracker.obtain();
        boolean opening = !mExpanded;
        if (opening) {
            mAnimatedAcceleration = mMaximumAcceleration;
            mAnimatedVelocity = mMaximumMajorVelocity;
            mAnimationPosition = mBottomOffset + (mVertical ? getHeight() - mHandleHeight : getWidth() - mHandleWidth);
            moveHandle(((int) (mAnimationPosition)));
            mAnimating = true;
            removeCallbacks(mSlidingRunnable);
            long now = android.os.SystemClock.uptimeMillis();
            mAnimationLastTime = now;
            mCurrentAnimationTime = now + android.widget.SlidingDrawer.ANIMATION_FRAME_DURATION;
            mAnimating = true;
        } else {
            if (mAnimating) {
                mAnimating = false;
                removeCallbacks(mSlidingRunnable);
            }
            moveHandle(position);
        }
    }

    private void moveHandle(int position) {
        final android.view.View handle = mHandle;
        if (mVertical) {
            if (position == android.widget.SlidingDrawer.EXPANDED_FULL_OPEN) {
                handle.offsetTopAndBottom(mTopOffset - handle.getTop());
                invalidate();
            } else
                if (position == android.widget.SlidingDrawer.COLLAPSED_FULL_CLOSED) {
                    handle.offsetTopAndBottom((((mBottomOffset + mBottom) - mTop) - mHandleHeight) - handle.getTop());
                    invalidate();
                } else {
                    final int top = handle.getTop();
                    int deltaY = position - top;
                    if (position < mTopOffset) {
                        deltaY = mTopOffset - top;
                    } else
                        if (deltaY > ((((mBottomOffset + mBottom) - mTop) - mHandleHeight) - top)) {
                            deltaY = (((mBottomOffset + mBottom) - mTop) - mHandleHeight) - top;
                        }

                    handle.offsetTopAndBottom(deltaY);
                    final android.graphics.Rect frame = mFrame;
                    final android.graphics.Rect region = mInvalidate;
                    handle.getHitRect(frame);
                    region.set(frame);
                    region.union(frame.left, frame.top - deltaY, frame.right, frame.bottom - deltaY);
                    region.union(0, frame.bottom - deltaY, getWidth(), (frame.bottom - deltaY) + mContent.getHeight());
                    invalidate(region);
                }

        } else {
            if (position == android.widget.SlidingDrawer.EXPANDED_FULL_OPEN) {
                handle.offsetLeftAndRight(mTopOffset - handle.getLeft());
                invalidate();
            } else
                if (position == android.widget.SlidingDrawer.COLLAPSED_FULL_CLOSED) {
                    handle.offsetLeftAndRight((((mBottomOffset + mRight) - mLeft) - mHandleWidth) - handle.getLeft());
                    invalidate();
                } else {
                    final int left = handle.getLeft();
                    int deltaX = position - left;
                    if (position < mTopOffset) {
                        deltaX = mTopOffset - left;
                    } else
                        if (deltaX > ((((mBottomOffset + mRight) - mLeft) - mHandleWidth) - left)) {
                            deltaX = (((mBottomOffset + mRight) - mLeft) - mHandleWidth) - left;
                        }

                    handle.offsetLeftAndRight(deltaX);
                    final android.graphics.Rect frame = mFrame;
                    final android.graphics.Rect region = mInvalidate;
                    handle.getHitRect(frame);
                    region.set(frame);
                    region.union(frame.left - deltaX, frame.top, frame.right - deltaX, frame.bottom);
                    region.union(frame.right - deltaX, 0, (frame.right - deltaX) + mContent.getWidth(), getHeight());
                    invalidate(region);
                }

        }
    }

    @android.annotation.UnsupportedAppUsage
    private void prepareContent() {
        if (mAnimating) {
            return;
        }
        // Something changed in the content, we need to honor the layout request
        // before creating the cached bitmap
        final android.view.View content = mContent;
        if (content.isLayoutRequested()) {
            if (mVertical) {
                final int childHeight = mHandleHeight;
                int height = ((mBottom - mTop) - childHeight) - mTopOffset;
                content.measure(android.view.View.MeasureSpec.makeMeasureSpec(mRight - mLeft, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY));
                content.layout(0, mTopOffset + childHeight, content.getMeasuredWidth(), (mTopOffset + childHeight) + content.getMeasuredHeight());
            } else {
                final int childWidth = mHandle.getWidth();
                int width = ((mRight - mLeft) - childWidth) - mTopOffset;
                content.measure(android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(mBottom - mTop, android.view.View.MeasureSpec.EXACTLY));
                content.layout(childWidth + mTopOffset, 0, (mTopOffset + childWidth) + content.getMeasuredWidth(), content.getMeasuredHeight());
            }
        }
        // Try only once... we should really loop but it's not a big deal
        // if the draw was cancelled, it will only be temporary anyway
        content.getViewTreeObserver().dispatchOnPreDraw();
        if (!content.isHardwareAccelerated())
            content.buildDrawingCache();

        content.setVisibility(android.view.View.GONE);
    }

    private void stopTracking(boolean notifyScrollListener) {
        mHandle.setPressed(false);
        mTracking = false;
        if (notifyScrollListener && (mOnDrawerScrollListener != null)) {
            mOnDrawerScrollListener.onScrollEnded();
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void doAnimation() {
        if (mAnimating) {
            incrementAnimation();
            if (mAnimationPosition >= ((mBottomOffset + (mVertical ? getHeight() : getWidth())) - 1)) {
                mAnimating = false;
                closeDrawer();
            } else
                if (mAnimationPosition < mTopOffset) {
                    mAnimating = false;
                    openDrawer();
                } else {
                    moveHandle(((int) (mAnimationPosition)));
                    mCurrentAnimationTime += android.widget.SlidingDrawer.ANIMATION_FRAME_DURATION;
                    postDelayed(mSlidingRunnable, android.widget.SlidingDrawer.ANIMATION_FRAME_DURATION);
                }

        }
    }

    private void incrementAnimation() {
        long now = android.os.SystemClock.uptimeMillis();
        float t = (now - mAnimationLastTime) / 1000.0F;
        // ms -> s
        final float position = mAnimationPosition;
        final float v = mAnimatedVelocity;
        // px/s
        final float a = mAnimatedAcceleration;
        // px/s/s
        mAnimationPosition = (position + (v * t)) + (((0.5F * a) * t) * t);// px

        mAnimatedVelocity = v + (a * t);
        // px/s
        mAnimationLastTime = now;// ms

    }

    /**
     * Toggles the drawer open and close. Takes effect immediately.
     *
     * @see #open()
     * @see #close()
     * @see #animateClose()
     * @see #animateOpen()
     * @see #animateToggle()
     */
    public void toggle() {
        if (!mExpanded) {
            openDrawer();
        } else {
            closeDrawer();
        }
        invalidate();
        requestLayout();
    }

    /**
     * Toggles the drawer open and close with an animation.
     *
     * @see #open()
     * @see #close()
     * @see #animateClose()
     * @see #animateOpen()
     * @see #toggle()
     */
    public void animateToggle() {
        if (!mExpanded) {
            animateOpen();
        } else {
            animateClose();
        }
    }

    /**
     * Opens the drawer immediately.
     *
     * @see #toggle()
     * @see #close()
     * @see #animateOpen()
     */
    public void open() {
        openDrawer();
        invalidate();
        requestLayout();
        sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    /**
     * Closes the drawer immediately.
     *
     * @see #toggle()
     * @see #open()
     * @see #animateClose()
     */
    public void close() {
        closeDrawer();
        invalidate();
        requestLayout();
    }

    /**
     * Closes the drawer with an animation.
     *
     * @see #close()
     * @see #open()
     * @see #animateOpen()
     * @see #animateToggle()
     * @see #toggle()
     */
    public void animateClose() {
        prepareContent();
        final android.widget.SlidingDrawer.OnDrawerScrollListener scrollListener = mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateClose(mVertical ? mHandle.getTop() : mHandle.getLeft(), false);
        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    /**
     * Opens the drawer with an animation.
     *
     * @see #close()
     * @see #open()
     * @see #animateClose()
     * @see #animateToggle()
     * @see #toggle()
     */
    public void animateOpen() {
        prepareContent();
        final android.widget.SlidingDrawer.OnDrawerScrollListener scrollListener = mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateOpen(mVertical ? mHandle.getTop() : mHandle.getLeft(), false);
        sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.SlidingDrawer.class.getName();
    }

    private void closeDrawer() {
        moveHandle(android.widget.SlidingDrawer.COLLAPSED_FULL_CLOSED);
        mContent.setVisibility(android.view.View.GONE);
        mContent.destroyDrawingCache();
        if (!mExpanded) {
            return;
        }
        mExpanded = false;
        if (mOnDrawerCloseListener != null) {
            mOnDrawerCloseListener.onDrawerClosed();
        }
    }

    private void openDrawer() {
        moveHandle(android.widget.SlidingDrawer.EXPANDED_FULL_OPEN);
        mContent.setVisibility(android.view.View.VISIBLE);
        if (mExpanded) {
            return;
        }
        mExpanded = true;
        if (mOnDrawerOpenListener != null) {
            mOnDrawerOpenListener.onDrawerOpened();
        }
    }

    /**
     * Sets the listener that receives a notification when the drawer becomes open.
     *
     * @param onDrawerOpenListener
     * 		The listener to be notified when the drawer is opened.
     */
    public void setOnDrawerOpenListener(android.widget.SlidingDrawer.OnDrawerOpenListener onDrawerOpenListener) {
        mOnDrawerOpenListener = onDrawerOpenListener;
    }

    /**
     * Sets the listener that receives a notification when the drawer becomes close.
     *
     * @param onDrawerCloseListener
     * 		The listener to be notified when the drawer is closed.
     */
    public void setOnDrawerCloseListener(android.widget.SlidingDrawer.OnDrawerCloseListener onDrawerCloseListener) {
        mOnDrawerCloseListener = onDrawerCloseListener;
    }

    /**
     * Sets the listener that receives a notification when the drawer starts or ends
     * a scroll. A fling is considered as a scroll. A fling will also trigger a
     * drawer opened or drawer closed event.
     *
     * @param onDrawerScrollListener
     * 		The listener to be notified when scrolling
     * 		starts or stops.
     */
    public void setOnDrawerScrollListener(android.widget.SlidingDrawer.OnDrawerScrollListener onDrawerScrollListener) {
        mOnDrawerScrollListener = onDrawerScrollListener;
    }

    /**
     * Returns the handle of the drawer.
     *
     * @return The View reprenseting the handle of the drawer, identified by
    the "handle" id in XML.
     */
    public android.view.View getHandle() {
        return mHandle;
    }

    /**
     * Returns the content of the drawer.
     *
     * @return The View reprenseting the content of the drawer, identified by
    the "content" id in XML.
     */
    public android.view.View getContent() {
        return mContent;
    }

    /**
     * Unlocks the SlidingDrawer so that touch events are processed.
     *
     * @see #lock()
     */
    public void unlock() {
        mLocked = false;
    }

    /**
     * Locks the SlidingDrawer so that touch events are ignores.
     *
     * @see #unlock()
     */
    public void lock() {
        mLocked = true;
    }

    /**
     * Indicates whether the drawer is currently fully opened.
     *
     * @return True if the drawer is opened, false otherwise.
     */
    public boolean isOpened() {
        return mExpanded;
    }

    /**
     * Indicates whether the drawer is scrolling or flinging.
     *
     * @return True if the drawer is scroller or flinging, false otherwise.
     */
    public boolean isMoving() {
        return mTracking || mAnimating;
    }

    private class DrawerToggler implements android.view.View.OnClickListener {
        public void onClick(android.view.View v) {
            if (mLocked) {
                return;
            }
            // mAllowSingleTap isn't relevant here; you're *always*
            // allowed to open/close the drawer by clicking with the
            // trackball.
            if (mAnimateOnClick) {
                animateToggle();
            } else {
                toggle();
            }
        }
    }

    private final java.lang.Runnable mSlidingRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            doAnimation();
        }
    };
}

