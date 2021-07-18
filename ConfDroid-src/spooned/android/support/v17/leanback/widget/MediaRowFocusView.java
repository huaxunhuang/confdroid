/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * Creates a view for a media item row in a playlist
 */
class MediaRowFocusView extends android.view.View {
    private final android.graphics.Paint mPaint;

    private final android.graphics.RectF mRoundRectF = new android.graphics.RectF();

    private int mRoundRectRadius;

    public MediaRowFocusView(android.content.Context context) {
        super(context);
        mPaint = createPaint(context);
    }

    public MediaRowFocusView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mPaint = createPaint(context);
    }

    public MediaRowFocusView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = createPaint(context);
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        mRoundRectRadius = getHeight() / 2;
        int drawHeight = 2 * mRoundRectRadius;
        int drawOffset = (drawHeight - getHeight()) / 2;
        mRoundRectF.set(0, -drawOffset, getWidth(), getHeight() + drawOffset);
        canvas.drawRoundRect(mRoundRectF, mRoundRectRadius, mRoundRectRadius, mPaint);
    }

    private android.graphics.Paint createPaint(android.content.Context context) {
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColor(context.getResources().getColor(R.color.lb_playback_media_row_highlight_color));
        return paint;
    }

    public int getRoundRectRadius() {
        return mRoundRectRadius;
    }
}

