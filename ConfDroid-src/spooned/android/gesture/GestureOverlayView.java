/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.gesture;


/**
 * A transparent overlay for gesture input that can be placed on top of other
 * widgets or contain other widgets.
 *
 * @unknown ref android.R.styleable#GestureOverlayView_eventsInterceptionEnabled
 * @unknown ref android.R.styleable#GestureOverlayView_fadeDuration
 * @unknown ref android.R.styleable#GestureOverlayView_fadeOffset
 * @unknown ref android.R.styleable#GestureOverlayView_fadeEnabled
 * @unknown ref android.R.styleable#GestureOverlayView_gestureStrokeWidth
 * @unknown ref android.R.styleable#GestureOverlayView_gestureStrokeAngleThreshold
 * @unknown ref android.R.styleable#GestureOverlayView_gestureStrokeLengthThreshold
 * @unknown ref android.R.styleable#GestureOverlayView_gestureStrokeSquarenessThreshold
 * @unknown ref android.R.styleable#GestureOverlayView_gestureStrokeType
 * @unknown ref android.R.styleable#GestureOverlayView_gestureColor
 * @unknown ref android.R.styleable#GestureOverlayView_orientation
 * @unknown ref android.R.styleable#GestureOverlayView_uncertainGestureColor
 */
@android.annotation.Widget
public class GestureOverlayView extends android.widget.FrameLayout {
    public static final int GESTURE_STROKE_TYPE_SINGLE = 0;

    public static final int GESTURE_STROKE_TYPE_MULTIPLE = 1;

    public static final int ORIENTATION_HORIZONTAL = 0;

    public static final int ORIENTATION_VERTICAL = 1;

    private static final int FADE_ANIMATION_RATE = 16;

    private static final boolean GESTURE_RENDERING_ANTIALIAS = true;

    private static final boolean DITHER_FLAG = true;

    private final android.graphics.Paint mGesturePaint = new android.graphics.Paint();

    private long mFadeDuration = 150;

    private long mFadeOffset = 420;

    private long mFadingStart;

    private boolean mFadingHasStarted;

    private boolean mFadeEnabled = true;

    private int mCurrentColor;

    private int mCertainGestureColor = 0xffffff00;

    private int mUncertainGestureColor = 0x48ffff00;

    private float mGestureStrokeWidth = 12.0F;

    private int mInvalidateExtraBorder = 10;

    private int mGestureStrokeType = android.gesture.GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE;

    private float mGestureStrokeLengthThreshold = 50.0F;

    private float mGestureStrokeSquarenessTreshold = 0.275F;

    private float mGestureStrokeAngleThreshold = 40.0F;

    private int mOrientation = android.gesture.GestureOverlayView.ORIENTATION_VERTICAL;

    private final android.graphics.Rect mInvalidRect = new android.graphics.Rect();

    private final android.graphics.Path mPath = new android.graphics.Path();

    private boolean mGestureVisible = true;

    private float mX;

    private float mY;

    private float mCurveEndX;

    private float mCurveEndY;

    private float mTotalLength;

    private boolean mIsGesturing = false;

    private boolean mPreviousWasGesturing = false;

    private boolean mInterceptEvents = true;

    private boolean mIsListeningForGestures;

    private boolean mResetGesture;

    // current gesture
    private android.gesture.Gesture mCurrentGesture;

    private final java.util.ArrayList<android.gesture.GesturePoint> mStrokeBuffer = new java.util.ArrayList<android.gesture.GesturePoint>(100);

    // TODO: Make this a list of WeakReferences
    private final java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener> mOnGestureListeners = new java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener>();

    // TODO: Make this a list of WeakReferences
    private final java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturePerformedListener> mOnGesturePerformedListeners = new java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturePerformedListener>();

    // TODO: Make this a list of WeakReferences
    private final java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturingListener> mOnGesturingListeners = new java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturingListener>();

    private boolean mHandleGestureActions;

    // fading out effect
    private boolean mIsFadingOut = false;

    private float mFadingAlpha = 1.0F;

    private final android.view.animation.AccelerateDecelerateInterpolator mInterpolator = new android.view.animation.AccelerateDecelerateInterpolator();

    private final android.gesture.GestureOverlayView.FadeOutRunnable mFadingOut = new android.gesture.GestureOverlayView.FadeOutRunnable();

    public GestureOverlayView(android.content.Context context) {
        super(context);
        init();
    }

    public GestureOverlayView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.gestureOverlayViewStyle);
    }

    public GestureOverlayView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GestureOverlayView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureOverlayView, defStyleAttr, defStyleRes);
        mGestureStrokeWidth = a.getFloat(R.styleable.GestureOverlayView_gestureStrokeWidth, mGestureStrokeWidth);
        mInvalidateExtraBorder = java.lang.Math.max(1, ((int) (mGestureStrokeWidth)) - 1);
        mCertainGestureColor = a.getColor(R.styleable.GestureOverlayView_gestureColor, mCertainGestureColor);
        mUncertainGestureColor = a.getColor(R.styleable.GestureOverlayView_uncertainGestureColor, mUncertainGestureColor);
        mFadeDuration = a.getInt(R.styleable.GestureOverlayView_fadeDuration, ((int) (mFadeDuration)));
        mFadeOffset = a.getInt(R.styleable.GestureOverlayView_fadeOffset, ((int) (mFadeOffset)));
        mGestureStrokeType = a.getInt(R.styleable.GestureOverlayView_gestureStrokeType, mGestureStrokeType);
        mGestureStrokeLengthThreshold = a.getFloat(R.styleable.GestureOverlayView_gestureStrokeLengthThreshold, mGestureStrokeLengthThreshold);
        mGestureStrokeAngleThreshold = a.getFloat(R.styleable.GestureOverlayView_gestureStrokeAngleThreshold, mGestureStrokeAngleThreshold);
        mGestureStrokeSquarenessTreshold = a.getFloat(R.styleable.GestureOverlayView_gestureStrokeSquarenessThreshold, mGestureStrokeSquarenessTreshold);
        mInterceptEvents = a.getBoolean(R.styleable.GestureOverlayView_eventsInterceptionEnabled, mInterceptEvents);
        mFadeEnabled = a.getBoolean(R.styleable.GestureOverlayView_fadeEnabled, mFadeEnabled);
        mOrientation = a.getInt(R.styleable.GestureOverlayView_orientation, mOrientation);
        a.recycle();
        init();
    }

    private void init() {
        setWillNotDraw(false);
        final android.graphics.Paint gesturePaint = mGesturePaint;
        gesturePaint.setAntiAlias(android.gesture.GestureOverlayView.GESTURE_RENDERING_ANTIALIAS);
        gesturePaint.setColor(mCertainGestureColor);
        gesturePaint.setStyle(android.graphics.Paint.Style.STROKE);
        gesturePaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        gesturePaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        gesturePaint.setStrokeWidth(mGestureStrokeWidth);
        gesturePaint.setDither(android.gesture.GestureOverlayView.DITHER_FLAG);
        mCurrentColor = mCertainGestureColor;
        setPaintAlpha(255);
    }

    public java.util.ArrayList<android.gesture.GesturePoint> getCurrentStroke() {
        return mStrokeBuffer;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setGestureColor(@android.annotation.ColorInt
    int color) {
        mCertainGestureColor = color;
    }

    public void setUncertainGestureColor(@android.annotation.ColorInt
    int color) {
        mUncertainGestureColor = color;
    }

    @android.annotation.ColorInt
    public int getUncertainGestureColor() {
        return mUncertainGestureColor;
    }

    @android.annotation.ColorInt
    public int getGestureColor() {
        return mCertainGestureColor;
    }

    public float getGestureStrokeWidth() {
        return mGestureStrokeWidth;
    }

    public void setGestureStrokeWidth(float gestureStrokeWidth) {
        mGestureStrokeWidth = gestureStrokeWidth;
        mInvalidateExtraBorder = java.lang.Math.max(1, ((int) (gestureStrokeWidth)) - 1);
        mGesturePaint.setStrokeWidth(gestureStrokeWidth);
    }

    public int getGestureStrokeType() {
        return mGestureStrokeType;
    }

    public void setGestureStrokeType(int gestureStrokeType) {
        mGestureStrokeType = gestureStrokeType;
    }

    public float getGestureStrokeLengthThreshold() {
        return mGestureStrokeLengthThreshold;
    }

    public void setGestureStrokeLengthThreshold(float gestureStrokeLengthThreshold) {
        mGestureStrokeLengthThreshold = gestureStrokeLengthThreshold;
    }

    public float getGestureStrokeSquarenessTreshold() {
        return mGestureStrokeSquarenessTreshold;
    }

    public void setGestureStrokeSquarenessTreshold(float gestureStrokeSquarenessTreshold) {
        mGestureStrokeSquarenessTreshold = gestureStrokeSquarenessTreshold;
    }

    public float getGestureStrokeAngleThreshold() {
        return mGestureStrokeAngleThreshold;
    }

    public void setGestureStrokeAngleThreshold(float gestureStrokeAngleThreshold) {
        mGestureStrokeAngleThreshold = gestureStrokeAngleThreshold;
    }

    public boolean isEventsInterceptionEnabled() {
        return mInterceptEvents;
    }

    public void setEventsInterceptionEnabled(boolean enabled) {
        mInterceptEvents = enabled;
    }

    public boolean isFadeEnabled() {
        return mFadeEnabled;
    }

    public void setFadeEnabled(boolean fadeEnabled) {
        mFadeEnabled = fadeEnabled;
    }

    public android.gesture.Gesture getGesture() {
        return mCurrentGesture;
    }

    public void setGesture(android.gesture.Gesture gesture) {
        if (mCurrentGesture != null) {
            clear(false);
        }
        setCurrentColor(mCertainGestureColor);
        mCurrentGesture = gesture;
        final android.graphics.Path path = mCurrentGesture.toPath();
        final android.graphics.RectF bounds = new android.graphics.RectF();
        path.computeBounds(bounds, true);
        // TODO: The path should also be scaled to fit inside this view
        mPath.rewind();
        mPath.addPath(path, (-bounds.left) + ((getWidth() - bounds.width()) / 2.0F), (-bounds.top) + ((getHeight() - bounds.height()) / 2.0F));
        mResetGesture = true;
        invalidate();
    }

    public android.graphics.Path getGesturePath() {
        return mPath;
    }

    public android.graphics.Path getGesturePath(android.graphics.Path path) {
        path.set(mPath);
        return path;
    }

    public boolean isGestureVisible() {
        return mGestureVisible;
    }

    public void setGestureVisible(boolean visible) {
        mGestureVisible = visible;
    }

    public long getFadeOffset() {
        return mFadeOffset;
    }

    public void setFadeOffset(long fadeOffset) {
        mFadeOffset = fadeOffset;
    }

    public void addOnGestureListener(android.gesture.GestureOverlayView.OnGestureListener listener) {
        mOnGestureListeners.add(listener);
    }

    public void removeOnGestureListener(android.gesture.GestureOverlayView.OnGestureListener listener) {
        mOnGestureListeners.remove(listener);
    }

    public void removeAllOnGestureListeners() {
        mOnGestureListeners.clear();
    }

    public void addOnGesturePerformedListener(android.gesture.GestureOverlayView.OnGesturePerformedListener listener) {
        mOnGesturePerformedListeners.add(listener);
        if (mOnGesturePerformedListeners.size() > 0) {
            mHandleGestureActions = true;
        }
    }

    public void removeOnGesturePerformedListener(android.gesture.GestureOverlayView.OnGesturePerformedListener listener) {
        mOnGesturePerformedListeners.remove(listener);
        if (mOnGesturePerformedListeners.size() <= 0) {
            mHandleGestureActions = false;
        }
    }

    public void removeAllOnGesturePerformedListeners() {
        mOnGesturePerformedListeners.clear();
        mHandleGestureActions = false;
    }

    public void addOnGesturingListener(android.gesture.GestureOverlayView.OnGesturingListener listener) {
        mOnGesturingListeners.add(listener);
    }

    public void removeOnGesturingListener(android.gesture.GestureOverlayView.OnGesturingListener listener) {
        mOnGesturingListeners.remove(listener);
    }

    public void removeAllOnGesturingListeners() {
        mOnGesturingListeners.clear();
    }

    public boolean isGesturing() {
        return mIsGesturing;
    }

    private void setCurrentColor(int color) {
        mCurrentColor = color;
        if (mFadingHasStarted) {
            setPaintAlpha(((int) (255 * mFadingAlpha)));
        } else {
            setPaintAlpha(255);
        }
        invalidate();
    }

    /**
     *
     *
     * @unknown 
     */
    public android.graphics.Paint getGesturePaint() {
        return mGesturePaint;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        super.draw(canvas);
        if ((mCurrentGesture != null) && mGestureVisible) {
            canvas.drawPath(mPath, mGesturePaint);
        }
    }

    private void setPaintAlpha(int alpha) {
        alpha += alpha >> 7;
        final int baseAlpha = mCurrentColor >>> 24;
        final int useAlpha = (baseAlpha * alpha) >> 8;
        mGesturePaint.setColor(((mCurrentColor << 8) >>> 8) | (useAlpha << 24));
    }

    public void clear(boolean animated) {
        clear(animated, false, true);
    }

    private void clear(boolean animated, boolean fireActionPerformed, boolean immediate) {
        setPaintAlpha(255);
        removeCallbacks(mFadingOut);
        mResetGesture = false;
        mFadingOut.fireActionPerformed = fireActionPerformed;
        mFadingOut.resetMultipleStrokes = false;
        if (animated && (mCurrentGesture != null)) {
            mFadingAlpha = 1.0F;
            mIsFadingOut = true;
            mFadingHasStarted = false;
            mFadingStart = android.view.animation.AnimationUtils.currentAnimationTimeMillis() + mFadeOffset;
            postDelayed(mFadingOut, mFadeOffset);
        } else {
            mFadingAlpha = 1.0F;
            mIsFadingOut = false;
            mFadingHasStarted = false;
            if (immediate) {
                mCurrentGesture = null;
                mPath.rewind();
                invalidate();
            } else
                if (fireActionPerformed) {
                    postDelayed(mFadingOut, mFadeOffset);
                } else
                    if (mGestureStrokeType == android.gesture.GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE) {
                        mFadingOut.resetMultipleStrokes = true;
                        postDelayed(mFadingOut, mFadeOffset);
                    } else {
                        mCurrentGesture = null;
                        mPath.rewind();
                        invalidate();
                    }


        }
    }

    public void cancelClearAnimation() {
        setPaintAlpha(255);
        mIsFadingOut = false;
        mFadingHasStarted = false;
        removeCallbacks(mFadingOut);
        mPath.rewind();
        mCurrentGesture = null;
    }

    public void cancelGesture() {
        mIsListeningForGestures = false;
        // add the stroke to the current gesture
        mCurrentGesture.addStroke(new android.gesture.GestureStroke(mStrokeBuffer));
        // pass the event to handlers
        final long now = android.os.SystemClock.uptimeMillis();
        final android.view.MotionEvent event = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_CANCEL, 0.0F, 0.0F, 0);
        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener> listeners = mOnGestureListeners;
        int count = listeners.size();
        for (int i = 0; i < count; i++) {
            listeners.get(i).onGestureCancelled(this, event);
        }
        event.recycle();
        clear(false);
        mIsGesturing = false;
        mPreviousWasGesturing = false;
        mStrokeBuffer.clear();
        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturingListener> otherListeners = mOnGesturingListeners;
        count = otherListeners.size();
        for (int i = 0; i < count; i++) {
            otherListeners.get(i).onGesturingEnded(this);
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelClearAnimation();
    }

    @java.lang.Override
    public boolean dispatchTouchEvent(android.view.MotionEvent event) {
        if (isEnabled()) {
            final boolean cancelDispatch = (mIsGesturing || (((mCurrentGesture != null) && (mCurrentGesture.getStrokesCount() > 0)) && mPreviousWasGesturing)) && mInterceptEvents;
            processEvent(event);
            if (cancelDispatch) {
                event.setAction(android.view.MotionEvent.ACTION_CANCEL);
            }
            super.dispatchTouchEvent(event);
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean processEvent(android.view.MotionEvent event) {
        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN :
                touchDown(event);
                invalidate();
                return true;
            case android.view.MotionEvent.ACTION_MOVE :
                if (mIsListeningForGestures) {
                    android.graphics.Rect rect = touchMove(event);
                    if (rect != null) {
                        invalidate(rect);
                    }
                    return true;
                }
                break;
            case android.view.MotionEvent.ACTION_UP :
                if (mIsListeningForGestures) {
                    touchUp(event, false);
                    invalidate();
                    return true;
                }
                break;
            case android.view.MotionEvent.ACTION_CANCEL :
                if (mIsListeningForGestures) {
                    touchUp(event, true);
                    invalidate();
                    return true;
                }
        }
        return false;
    }

    private void touchDown(android.view.MotionEvent event) {
        mIsListeningForGestures = true;
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        mTotalLength = 0;
        mIsGesturing = false;
        if ((mGestureStrokeType == android.gesture.GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE) || mResetGesture) {
            if (mHandleGestureActions)
                setCurrentColor(mUncertainGestureColor);

            mResetGesture = false;
            mCurrentGesture = null;
            mPath.rewind();
        } else
            if ((mCurrentGesture == null) || (mCurrentGesture.getStrokesCount() == 0)) {
                if (mHandleGestureActions)
                    setCurrentColor(mUncertainGestureColor);

            }

        // if there is fading out going on, stop it.
        if (mFadingHasStarted) {
            cancelClearAnimation();
        } else
            if (mIsFadingOut) {
                setPaintAlpha(255);
                mIsFadingOut = false;
                mFadingHasStarted = false;
                removeCallbacks(mFadingOut);
            }

        if (mCurrentGesture == null) {
            mCurrentGesture = new android.gesture.Gesture();
        }
        mStrokeBuffer.add(new android.gesture.GesturePoint(x, y, event.getEventTime()));
        mPath.moveTo(x, y);
        final int border = mInvalidateExtraBorder;
        mInvalidRect.set(((int) (x)) - border, ((int) (y)) - border, ((int) (x)) + border, ((int) (y)) + border);
        mCurveEndX = x;
        mCurveEndY = y;
        // pass the event to handlers
        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener> listeners = mOnGestureListeners;
        final int count = listeners.size();
        for (int i = 0; i < count; i++) {
            listeners.get(i).onGestureStarted(this, event);
        }
    }

    private android.graphics.Rect touchMove(android.view.MotionEvent event) {
        android.graphics.Rect areaToRefresh = null;
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = java.lang.Math.abs(x - previousX);
        final float dy = java.lang.Math.abs(y - previousY);
        if ((dx >= android.gesture.GestureStroke.TOUCH_TOLERANCE) || (dy >= android.gesture.GestureStroke.TOUCH_TOLERANCE)) {
            areaToRefresh = mInvalidRect;
            // start with the curve end
            final int border = mInvalidateExtraBorder;
            areaToRefresh.set(((int) (mCurveEndX)) - border, ((int) (mCurveEndY)) - border, ((int) (mCurveEndX)) + border, ((int) (mCurveEndY)) + border);
            float cX = mCurveEndX = (x + previousX) / 2;
            float cY = mCurveEndY = (y + previousY) / 2;
            mPath.quadTo(previousX, previousY, cX, cY);
            // union with the control point of the new curve
            areaToRefresh.union(((int) (previousX)) - border, ((int) (previousY)) - border, ((int) (previousX)) + border, ((int) (previousY)) + border);
            // union with the end point of the new curve
            areaToRefresh.union(((int) (cX)) - border, ((int) (cY)) - border, ((int) (cX)) + border, ((int) (cY)) + border);
            mX = x;
            mY = y;
            mStrokeBuffer.add(new android.gesture.GesturePoint(x, y, event.getEventTime()));
            if (mHandleGestureActions && (!mIsGesturing)) {
                mTotalLength += ((float) (java.lang.Math.hypot(dx, dy)));
                if (mTotalLength > mGestureStrokeLengthThreshold) {
                    final android.gesture.OrientedBoundingBox box = android.gesture.GestureUtils.computeOrientedBoundingBox(mStrokeBuffer);
                    float angle = java.lang.Math.abs(box.orientation);
                    if (angle > 90) {
                        angle = 180 - angle;
                    }
                    if ((box.squareness > mGestureStrokeSquarenessTreshold) || (mOrientation == android.gesture.GestureOverlayView.ORIENTATION_VERTICAL ? angle < mGestureStrokeAngleThreshold : angle > mGestureStrokeAngleThreshold)) {
                        mIsGesturing = true;
                        setCurrentColor(mCertainGestureColor);
                        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturingListener> listeners = mOnGesturingListeners;
                        int count = listeners.size();
                        for (int i = 0; i < count; i++) {
                            listeners.get(i).onGesturingStarted(this);
                        }
                    }
                }
            }
            // pass the event to handlers
            final java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener> listeners = mOnGestureListeners;
            final int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.get(i).onGesture(this, event);
            }
        }
        return areaToRefresh;
    }

    private void touchUp(android.view.MotionEvent event, boolean cancel) {
        mIsListeningForGestures = false;
        // A gesture wasn't started or was cancelled
        if (mCurrentGesture != null) {
            // add the stroke to the current gesture
            mCurrentGesture.addStroke(new android.gesture.GestureStroke(mStrokeBuffer));
            if (!cancel) {
                // pass the event to handlers
                final java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener> listeners = mOnGestureListeners;
                int count = listeners.size();
                for (int i = 0; i < count; i++) {
                    listeners.get(i).onGestureEnded(this, event);
                }
                clear(mHandleGestureActions && mFadeEnabled, mHandleGestureActions && mIsGesturing, false);
            } else {
                cancelGesture(event);
            }
        } else {
            cancelGesture(event);
        }
        mStrokeBuffer.clear();
        mPreviousWasGesturing = mIsGesturing;
        mIsGesturing = false;
        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturingListener> listeners = mOnGesturingListeners;
        int count = listeners.size();
        for (int i = 0; i < count; i++) {
            listeners.get(i).onGesturingEnded(this);
        }
    }

    private void cancelGesture(android.view.MotionEvent event) {
        // pass the event to handlers
        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGestureListener> listeners = mOnGestureListeners;
        final int count = listeners.size();
        for (int i = 0; i < count; i++) {
            listeners.get(i).onGestureCancelled(this, event);
        }
        clear(false);
    }

    private void fireOnGesturePerformed() {
        final java.util.ArrayList<android.gesture.GestureOverlayView.OnGesturePerformedListener> actionListeners = mOnGesturePerformedListeners;
        final int count = actionListeners.size();
        for (int i = 0; i < count; i++) {
            actionListeners.get(i).onGesturePerformed(this, mCurrentGesture);
        }
    }

    private class FadeOutRunnable implements java.lang.Runnable {
        boolean fireActionPerformed;

        boolean resetMultipleStrokes;

        public void run() {
            if (mIsFadingOut) {
                final long now = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
                final long duration = now - mFadingStart;
                if (duration > mFadeDuration) {
                    if (fireActionPerformed) {
                        fireOnGesturePerformed();
                    }
                    mPreviousWasGesturing = false;
                    mIsFadingOut = false;
                    mFadingHasStarted = false;
                    mPath.rewind();
                    mCurrentGesture = null;
                    setPaintAlpha(255);
                } else {
                    mFadingHasStarted = true;
                    float interpolatedTime = java.lang.Math.max(0.0F, java.lang.Math.min(1.0F, duration / ((float) (mFadeDuration))));
                    mFadingAlpha = 1.0F - mInterpolator.getInterpolation(interpolatedTime);
                    setPaintAlpha(((int) (255 * mFadingAlpha)));
                    postDelayed(this, android.gesture.GestureOverlayView.FADE_ANIMATION_RATE);
                }
            } else
                if (resetMultipleStrokes) {
                    mResetGesture = true;
                } else {
                    fireOnGesturePerformed();
                    mFadingHasStarted = false;
                    mPath.rewind();
                    mCurrentGesture = null;
                    mPreviousWasGesturing = false;
                    setPaintAlpha(255);
                }

            invalidate();
        }
    }

    public static interface OnGesturingListener {
        void onGesturingStarted(android.gesture.GestureOverlayView overlay);

        void onGesturingEnded(android.gesture.GestureOverlayView overlay);
    }

    public static interface OnGestureListener {
        void onGestureStarted(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event);

        void onGesture(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event);

        void onGestureEnded(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event);

        void onGestureCancelled(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event);
    }

    public static interface OnGesturePerformedListener {
        void onGesturePerformed(android.gesture.GestureOverlayView overlay, android.gesture.Gesture gesture);
    }
}

