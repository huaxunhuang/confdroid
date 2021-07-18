/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */
package android.support.v7.internal.widget;


/**
 * Extension of ImageView that correctly applies maxWidth and maxHeight.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class PreferenceImageView extends android.widget.ImageView {
    private int mMaxWidth = java.lang.Integer.MAX_VALUE;

    private int mMaxHeight = java.lang.Integer.MAX_VALUE;

    public PreferenceImageView(android.content.Context context) {
        this(context, null);
    }

    public PreferenceImageView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreferenceImageView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreferenceImageView, defStyleAttr, 0);
        setMaxWidth(a.getDimensionPixelSize(R.styleable.PreferenceImageView_maxWidth, java.lang.Integer.MAX_VALUE));
        setMaxHeight(a.getDimensionPixelSize(R.styleable.PreferenceImageView_maxHeight, java.lang.Integer.MAX_VALUE));
        a.recycle();
    }

    // public PreferenceImageView(Context context, AttributeSet attrs, int defStyleAttr,
    // int defStyleRes) {
    // super(context, attrs, defStyleAttr, defStyleRes);
    // }
    @java.lang.Override
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        super.setMaxWidth(maxWidth);
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }

    @java.lang.Override
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        super.setMaxHeight(maxHeight);
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        if ((widthMode == android.view.View.MeasureSpec.AT_MOST) || (widthMode == android.view.View.MeasureSpec.UNSPECIFIED)) {
            final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
            final int maxWidth = getMaxWidth();
            if ((maxWidth != java.lang.Integer.MAX_VALUE) && ((maxWidth < widthSize) || (widthMode == android.view.View.MeasureSpec.UNSPECIFIED))) {
                widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxWidth, android.view.View.MeasureSpec.AT_MOST);
            }
        }
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        if ((heightMode == android.view.View.MeasureSpec.AT_MOST) || (heightMode == android.view.View.MeasureSpec.UNSPECIFIED)) {
            final int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
            final int maxHeight = getMaxHeight();
            if ((maxHeight != java.lang.Integer.MAX_VALUE) && ((maxHeight < heightSize) || (heightMode == android.view.View.MeasureSpec.UNSPECIFIED))) {
                heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxHeight, android.view.View.MeasureSpec.AT_MOST);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

