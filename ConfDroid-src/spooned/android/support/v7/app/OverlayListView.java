/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v7.app;


/**
 * A ListView which has an additional overlay layer. {@link BitmapDrawable}
 * can be added to the layer and can be animated.
 */
final class OverlayListView extends android.widget.ListView {
    private final java.util.List<android.support.v7.app.OverlayListView.OverlayObject> mOverlayObjects = new java.util.ArrayList<>();

    public OverlayListView(android.content.Context context) {
        super(context);
    }

    public OverlayListView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Adds an object to the overlay layer.
     *
     * @param object
     * 		An object to be added.
     */
    public void addOverlayObject(android.support.v7.app.OverlayListView.OverlayObject object) {
        mOverlayObjects.add(object);
    }

    /**
     * Starts all animations of objects in the overlay layer.
     */
    public void startAnimationAll() {
        for (android.support.v7.app.OverlayListView.OverlayObject object : mOverlayObjects) {
            if (!object.isAnimationStarted()) {
                object.startAnimation(getDrawingTime());
            }
        }
    }

    /**
     * Stops all animations of objects in the overlay layer.
     */
    public void stopAnimationAll() {
        for (android.support.v7.app.OverlayListView.OverlayObject object : mOverlayObjects) {
            object.stopAnimation();
        }
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if (mOverlayObjects.size() > 0) {
            java.util.Iterator<android.support.v7.app.OverlayListView.OverlayObject> it = mOverlayObjects.iterator();
            while (it.hasNext()) {
                android.support.v7.app.OverlayListView.OverlayObject object = it.next();
                android.graphics.drawable.BitmapDrawable bitmap = object.getBitmapDrawable();
                if (bitmap != null) {
                    bitmap.draw(canvas);
                }
                if (!object.update(getDrawingTime())) {
                    it.remove();
                }
            } 
        }
    }

    /**
     * A class that represents an object to be shown in the overlay layer.
     */
    public static class OverlayObject {
        private android.graphics.drawable.BitmapDrawable mBitmap;

        private float mCurrentAlpha = 1.0F;

        private android.graphics.Rect mCurrentBounds;

        private android.view.animation.Interpolator mInterpolator;

        private long mDuration;

        private android.graphics.Rect mStartRect;

        private int mDeltaY;

        private float mStartAlpha = 1.0F;

        private float mEndAlpha = 1.0F;

        private long mStartTime;

        private boolean mIsAnimationStarted;

        private boolean mIsAnimationEnded;

        private android.support.v7.app.OverlayListView.OverlayObject.OnAnimationEndListener mListener;

        public OverlayObject(android.graphics.drawable.BitmapDrawable bitmap, android.graphics.Rect startRect) {
            mBitmap = bitmap;
            mStartRect = startRect;
            mCurrentBounds = new android.graphics.Rect(startRect);
            if ((mBitmap != null) && (mCurrentBounds != null)) {
                mBitmap.setAlpha(((int) (mCurrentAlpha * 255)));
                mBitmap.setBounds(mCurrentBounds);
            }
        }

        /**
         * Returns the bitmap that this object represents.
         *
         * @return BitmapDrawable that this object has.
         */
        public android.graphics.drawable.BitmapDrawable getBitmapDrawable() {
            return mBitmap;
        }

        /**
         * Returns the started status of the animation.
         *
         * @return True if the animation has started, false otherwise.
         */
        public boolean isAnimationStarted() {
            return mIsAnimationStarted;
        }

        /**
         * Sets animation for varying alpha.
         *
         * @param startAlpha
         * 		Starting alpha value for the animation, where 1.0 means
         * 		fully opaque and 0.0 means fully transparent.
         * @param endAlpha
         * 		Ending alpha value for the animation.
         * @return This OverlayObject to allow for chaining of calls.
         */
        public android.support.v7.app.OverlayListView.OverlayObject setAlphaAnimation(float startAlpha, float endAlpha) {
            mStartAlpha = startAlpha;
            mEndAlpha = endAlpha;
            return this;
        }

        /**
         * Sets animation for moving objects vertically.
         *
         * @param deltaY
         * 		Distance to move in pixels.
         * @return This OverlayObject to allow for chaining of calls.
         */
        public android.support.v7.app.OverlayListView.OverlayObject setTranslateYAnimation(int deltaY) {
            mDeltaY = deltaY;
            return this;
        }

        /**
         * Sets how long the animation will last.
         *
         * @param duration
         * 		Duration in milliseconds
         * @return This OverlayObject to allow for chaining of calls.
         */
        public android.support.v7.app.OverlayListView.OverlayObject setDuration(long duration) {
            mDuration = duration;
            return this;
        }

        /**
         * Sets the acceleration curve for this animation.
         *
         * @param interpolator
         * 		The interpolator which defines the acceleration curve
         * @return This OverlayObject to allow for chaining of calls.
         */
        public android.support.v7.app.OverlayListView.OverlayObject setInterpolator(android.view.animation.Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }

        /**
         * Binds an animation end listener to the animation.
         *
         * @param listener
         * 		the animation end listener to be notified.
         * @return This OverlayObject to allow for chaining of calls.
         */
        public android.support.v7.app.OverlayListView.OverlayObject setAnimationEndListener(android.support.v7.app.OverlayListView.OverlayObject.OnAnimationEndListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * Starts the animation and sets the start time.
         *
         * @param startTime
         * 		Start time to be set in Millis
         */
        public void startAnimation(long startTime) {
            mStartTime = startTime;
            mIsAnimationStarted = true;
        }

        /**
         * Stops the animation.
         */
        public void stopAnimation() {
            mIsAnimationStarted = true;
            mIsAnimationEnded = true;
            if (mListener != null) {
                mListener.onAnimationEnd();
            }
        }

        /**
         * Calculates and updates current bounds and alpha value.
         *
         * @param currentTime
         * 		Current time.in millis
         */
        public boolean update(long currentTime) {
            if (mIsAnimationEnded) {
                return false;
            }
            float normalizedTime = (currentTime - mStartTime) / ((float) (mDuration));
            normalizedTime = java.lang.Math.max(0.0F, java.lang.Math.min(1.0F, normalizedTime));
            if (!mIsAnimationStarted) {
                normalizedTime = 0.0F;
            }
            float interpolatedTime = (mInterpolator == null) ? normalizedTime : mInterpolator.getInterpolation(normalizedTime);
            int deltaY = ((int) (mDeltaY * interpolatedTime));
            mCurrentBounds.top = mStartRect.top + deltaY;
            mCurrentBounds.bottom = mStartRect.bottom + deltaY;
            mCurrentAlpha = mStartAlpha + ((mEndAlpha - mStartAlpha) * interpolatedTime);
            if ((mBitmap != null) && (mCurrentBounds != null)) {
                mBitmap.setAlpha(((int) (mCurrentAlpha * 255)));
                mBitmap.setBounds(mCurrentBounds);
            }
            if (mIsAnimationStarted && (normalizedTime >= 1.0F)) {
                mIsAnimationEnded = true;
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }
            return !mIsAnimationEnded;
        }

        /**
         * An animation listener that receives notifications when the animation ends.
         */
        public interface OnAnimationEndListener {
            /**
             * Notifies the end of the animation.
             */
            public void onAnimationEnd();
        }
    }
}

