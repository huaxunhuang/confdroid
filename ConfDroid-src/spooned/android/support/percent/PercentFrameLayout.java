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
 * limitations under the License.
 */
package android.support.percent;


/**
 * Subclass of {@link android.widget.FrameLayout} that supports percentage based dimensions and
 * margins.
 *
 * You can specify dimension or a margin of child by using attributes with "Percent" suffix. Follow
 * this example:
 *
 * <pre class="prettyprint">
 * &lt;android.support.percent.PercentFrameLayout
 *         xmlns:android="http://schemas.android.com/apk/res/android"
 *         xmlns:app="http://schemas.android.com/apk/res-auto"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent"&gt
 *     &lt;ImageView
 *         app:layout_widthPercent="50%"
 *         app:layout_heightPercent="50%"
 *         app:layout_marginTopPercent="25%"
 *         app:layout_marginLeftPercent="25%"/&gt
 * &lt;/android.support.percent.PercentFrameLayout&gt
 * </pre>
 *
 * The attributes that you can use are:
 * <ul>
 *     <li>{@code layout_widthPercent}
 *     <li>{@code layout_heightPercent}
 *     <li>{@code layout_marginPercent}
 *     <li>{@code layout_marginLeftPercent}
 *     <li>{@code layout_marginTopPercent}
 *     <li>{@code layout_marginRightPercent}
 *     <li>{@code layout_marginBottomPercent}
 *     <li>{@code layout_marginStartPercent}
 *     <li>{@code layout_marginEndPercent}
 *     <li>{@code layout_aspectRatio}
 * </ul>
 *
 * It is not necessary to specify {@code layout_width/height} if you specify {@code layout_widthPercent.} However, if you want the view to be able to take up more space than what
 * percentage value permits, you can add {@code layout_width/height="wrap_content"}. In that case
 * if the percentage size is too small for the View's content, it will be resized using
 * {@code wrap_content} rule.
 *
 * <p>
 * You can also make one dimension be a fraction of the other by setting only width or height and
 * using {@code layout_aspectRatio} for the second one to be calculated automatically. For
 * example, if you would like to achieve 16:9 aspect ratio, you can write:
 * <pre class="prettyprint">
 *     android:layout_width="300dp"
 *     app:layout_aspectRatio="178%"
 * </pre>
 * This will make the aspect ratio 16:9 (1.78:1) with the width fixed at 300dp and height adjusted
 * accordingly.
 */
public class PercentFrameLayout extends android.widget.FrameLayout {
    private final android.support.percent.PercentLayoutHelper mHelper = new android.support.percent.PercentLayoutHelper(this);

    public PercentFrameLayout(android.content.Context context) {
        super(context);
    }

    public PercentFrameLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentFrameLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @java.lang.Override
    protected android.support.percent.PercentFrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.support.percent.PercentFrameLayout.LayoutParams(android.support.percent.PercentFrameLayout.LayoutParams.MATCH_PARENT, android.support.percent.PercentFrameLayout.LayoutParams.MATCH_PARENT);
    }

    @java.lang.Override
    public android.support.percent.PercentFrameLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.percent.PercentFrameLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHelper.restoreOriginalParams();
    }

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams implements android.support.percent.PercentLayoutHelper.PercentLayoutParams {
        private android.support.percent.PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            mPercentLayoutInfo = android.support.percent.PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.FrameLayout.LayoutParams source) {
            super(((android.view.ViewGroup.MarginLayoutParams) (source)));
            gravity = source.gravity;
        }

        public LayoutParams(android.support.percent.PercentFrameLayout.LayoutParams source) {
            this(((android.widget.FrameLayout.LayoutParams) (source)));
            mPercentLayoutInfo = source.mPercentLayoutInfo;
        }

        @java.lang.Override
        public android.support.percent.PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            if (mPercentLayoutInfo == null) {
                mPercentLayoutInfo = new android.support.percent.PercentLayoutHelper.PercentLayoutInfo();
            }
            return mPercentLayoutInfo;
        }

        @java.lang.Override
        protected void setBaseAttributes(android.content.res.TypedArray a, int widthAttr, int heightAttr) {
            android.support.percent.PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }
    }
}

