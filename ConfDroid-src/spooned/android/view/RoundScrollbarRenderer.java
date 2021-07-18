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
package android.view;


/**
 * Helper class for drawing round scroll bars on round Wear devices.
 */
class RoundScrollbarRenderer {
    // The range of the scrollbar position represented as an angle in degrees.
    private static final int SCROLLBAR_ANGLE_RANGE = 90;

    private static final int MAX_SCROLLBAR_ANGLE_SWIPE = 16;

    private static final int MIN_SCROLLBAR_ANGLE_SWIPE = 6;

    private static final float WIDTH_PERCENTAGE = 0.02F;

    private static final int DEFAULT_THUMB_COLOR = 0xffe8eaed;

    private static final int DEFAULT_TRACK_COLOR = 0x4cffffff;

    private final android.graphics.Paint mThumbPaint = new android.graphics.Paint();

    private final android.graphics.Paint mTrackPaint = new android.graphics.Paint();

    private final android.graphics.RectF mRect = new android.graphics.RectF();

    private final android.view.View mParent;

    private final int mMaskThickness;

    public RoundScrollbarRenderer(android.view.View parent) {
        // Paints for the round scrollbar.
        // Set up the thumb paint
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        mThumbPaint.setStyle(android.graphics.Paint.Style.STROKE);
        // Set up the track paint
        mTrackPaint.setAntiAlias(true);
        mTrackPaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        mTrackPaint.setStyle(android.graphics.Paint.Style.STROKE);
        mParent = parent;
        // Fetch the resource indicating the thickness of CircularDisplayMask, rounding in the same
        // way WindowManagerService.showCircularMask does. The scroll bar is inset by this amount so
        // that it doesn't get clipped.
        mMaskThickness = parent.getContext().getResources().getDimensionPixelSize(com.android.internal.R.dimen.circular_display_mask_thickness);
    }

    public void drawRoundScrollbars(android.graphics.Canvas canvas, float alpha, android.graphics.Rect bounds) {
        if (alpha == 0) {
            return;
        }
        // Get information about the current scroll state of the parent view.
        float maxScroll = mParent.computeVerticalScrollRange();
        float scrollExtent = mParent.computeVerticalScrollExtent();
        if ((scrollExtent <= 0) || (maxScroll <= scrollExtent)) {
            return;
        }
        float currentScroll = java.lang.Math.max(0, mParent.computeVerticalScrollOffset());
        float linearThumbLength = mParent.computeVerticalScrollExtent();
        float thumbWidth = mParent.getWidth() * android.view.RoundScrollbarRenderer.WIDTH_PERCENTAGE;
        mThumbPaint.setStrokeWidth(thumbWidth);
        mTrackPaint.setStrokeWidth(thumbWidth);
        setThumbColor(android.view.RoundScrollbarRenderer.applyAlpha(android.view.RoundScrollbarRenderer.DEFAULT_THUMB_COLOR, alpha));
        setTrackColor(android.view.RoundScrollbarRenderer.applyAlpha(android.view.RoundScrollbarRenderer.DEFAULT_TRACK_COLOR, alpha));
        // Normalize the sweep angle for the scroll bar.
        float sweepAngle = (linearThumbLength / maxScroll) * android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE;
        sweepAngle = android.view.RoundScrollbarRenderer.clamp(sweepAngle, android.view.RoundScrollbarRenderer.MIN_SCROLLBAR_ANGLE_SWIPE, android.view.RoundScrollbarRenderer.MAX_SCROLLBAR_ANGLE_SWIPE);
        // Normalize the start angle so that it falls on the track.
        float startAngle = ((currentScroll * (android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE - sweepAngle)) / (maxScroll - linearThumbLength)) - (android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE / 2);
        startAngle = android.view.RoundScrollbarRenderer.clamp(startAngle, (-android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE) / 2, (android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE / 2) - sweepAngle);
        // Draw the track and the thumb.
        float inset = (thumbWidth / 2) + mMaskThickness;
        mRect.set(bounds.left + inset, bounds.top + inset, bounds.right - inset, bounds.bottom - inset);
        canvas.drawArc(mRect, (-android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE) / 2, android.view.RoundScrollbarRenderer.SCROLLBAR_ANGLE_RANGE, false, mTrackPaint);
        canvas.drawArc(mRect, startAngle, sweepAngle, false, mThumbPaint);
    }

    private static float clamp(float val, float min, float max) {
        if (val < min) {
            return min;
        } else
            if (val > max) {
                return max;
            } else {
                return val;
            }

    }

    private static int applyAlpha(int color, float alpha) {
        int alphaByte = ((int) (android.graphics.Color.alpha(color) * alpha));
        return android.graphics.Color.argb(alphaByte, android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color));
    }

    private void setThumbColor(int thumbColor) {
        if (mThumbPaint.getColor() != thumbColor) {
            mThumbPaint.setColor(thumbColor);
        }
    }

    private void setTrackColor(int trackColor) {
        if (mTrackPaint.getColor() != trackColor) {
            mTrackPaint.setColor(trackColor);
        }
    }
}

