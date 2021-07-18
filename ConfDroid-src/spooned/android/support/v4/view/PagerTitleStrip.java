/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.view;


/**
 * PagerTitleStrip is a non-interactive indicator of the current, next,
 * and previous pages of a {@link ViewPager}. It is intended to be used as a
 * child view of a ViewPager widget in your XML layout.
 * Add it as a child of a ViewPager in your layout file and set its
 * android:layout_gravity to TOP or BOTTOM to pin it to the top or bottom
 * of the ViewPager. The title from each page is supplied by the method
 * {@link PagerAdapter#getPageTitle(int)} in the adapter supplied to
 * the ViewPager.
 *
 * <p>For an interactive indicator, see {@link PagerTabStrip}.</p>
 */
@android.support.v4.view.ViewPager.DecorView
public class PagerTitleStrip extends android.view.ViewGroup {
    private static final java.lang.String TAG = "PagerTitleStrip";

    android.support.v4.view.ViewPager mPager;

    android.widget.TextView mPrevText;

    android.widget.TextView mCurrText;

    android.widget.TextView mNextText;

    private int mLastKnownCurrentPage = -1;

    float mLastKnownPositionOffset = -1;

    private int mScaledTextSpacing;

    private int mGravity;

    private boolean mUpdatingText;

    private boolean mUpdatingPositions;

    private final android.support.v4.view.PagerTitleStrip.PageListener mPageListener = new android.support.v4.view.PagerTitleStrip.PageListener();

    private java.lang.ref.WeakReference<android.support.v4.view.PagerAdapter> mWatchingAdapter;

    private static final int[] ATTRS = new int[]{ android.R.attr.textAppearance, android.R.attr.textSize, android.R.attr.textColor, android.R.attr.gravity };

    private static final int[] TEXT_ATTRS = new int[]{ 0x101038c// android.R.attr.textAllCaps
     };

    private static final float SIDE_ALPHA = 0.6F;

    private static final int TEXT_SPACING = 16;// dip


    private int mNonPrimaryAlpha;

    int mTextColor;

    interface PagerTitleStripImpl {
        void setSingleLineAllCaps(android.widget.TextView text);
    }

    static class PagerTitleStripImplBase implements android.support.v4.view.PagerTitleStrip.PagerTitleStripImpl {
        @java.lang.Override
        public void setSingleLineAllCaps(android.widget.TextView text) {
            text.setSingleLine();
        }
    }

    static class PagerTitleStripImplIcs implements android.support.v4.view.PagerTitleStrip.PagerTitleStripImpl {
        @java.lang.Override
        public void setSingleLineAllCaps(android.widget.TextView text) {
            android.support.v4.view.PagerTitleStripIcs.setSingleLineAllCaps(text);
        }
    }

    private static final android.support.v4.view.PagerTitleStrip.PagerTitleStripImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            IMPL = new android.support.v4.view.PagerTitleStrip.PagerTitleStripImplIcs();
        } else {
            IMPL = new android.support.v4.view.PagerTitleStrip.PagerTitleStripImplBase();
        }
    }

    private static void setSingleLineAllCaps(android.widget.TextView text) {
        android.support.v4.view.PagerTitleStrip.IMPL.setSingleLineAllCaps(text);
    }

    public PagerTitleStrip(android.content.Context context) {
        this(context, null);
    }

    public PagerTitleStrip(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        addView(mPrevText = new android.widget.TextView(context));
        addView(mCurrText = new android.widget.TextView(context));
        addView(mNextText = new android.widget.TextView(context));
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.v4.view.PagerTitleStrip.ATTRS);
        final int textAppearance = a.getResourceId(0, 0);
        if (textAppearance != 0) {
            android.support.v4.widget.TextViewCompat.setTextAppearance(mPrevText, textAppearance);
            android.support.v4.widget.TextViewCompat.setTextAppearance(mCurrText, textAppearance);
            android.support.v4.widget.TextViewCompat.setTextAppearance(mNextText, textAppearance);
        }
        final int textSize = a.getDimensionPixelSize(1, 0);
        if (textSize != 0) {
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (a.hasValue(2)) {
            final int textColor = a.getColor(2, 0);
            mPrevText.setTextColor(textColor);
            mCurrText.setTextColor(textColor);
            mNextText.setTextColor(textColor);
        }
        mGravity = a.getInteger(3, android.view.Gravity.BOTTOM);
        a.recycle();
        mTextColor = mCurrText.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(android.support.v4.view.PagerTitleStrip.SIDE_ALPHA);
        mPrevText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        mCurrText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        mNextText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        boolean allCaps = false;
        if (textAppearance != 0) {
            final android.content.res.TypedArray ta = context.obtainStyledAttributes(textAppearance, android.support.v4.view.PagerTitleStrip.TEXT_ATTRS);
            allCaps = ta.getBoolean(0, false);
            ta.recycle();
        }
        if (allCaps) {
            android.support.v4.view.PagerTitleStrip.setSingleLineAllCaps(mPrevText);
            android.support.v4.view.PagerTitleStrip.setSingleLineAllCaps(mCurrText);
            android.support.v4.view.PagerTitleStrip.setSingleLineAllCaps(mNextText);
        } else {
            mPrevText.setSingleLine();
            mCurrText.setSingleLine();
            mNextText.setSingleLine();
        }
        final float density = context.getResources().getDisplayMetrics().density;
        mScaledTextSpacing = ((int) (android.support.v4.view.PagerTitleStrip.TEXT_SPACING * density));
    }

    /**
     * Set the required spacing between title segments.
     *
     * @param spacingPixels
     * 		Spacing between each title displayed in pixels
     */
    public void setTextSpacing(int spacingPixels) {
        mScaledTextSpacing = spacingPixels;
        requestLayout();
    }

    /**
     *
     *
     * @return The required spacing between title segments in pixels
     */
    public int getTextSpacing() {
        return mScaledTextSpacing;
    }

    /**
     * Set the alpha value used for non-primary page titles.
     *
     * @param alpha
     * 		Opacity value in the range 0-1f
     */
    public void setNonPrimaryAlpha(@android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    float alpha) {
        mNonPrimaryAlpha = ((int) (alpha * 255)) & 0xff;
        final int transparentColor = (mNonPrimaryAlpha << 24) | (mTextColor & 0xffffff);
        mPrevText.setTextColor(transparentColor);
        mNextText.setTextColor(transparentColor);
    }

    /**
     * Set the color value used as the base color for all displayed page titles.
     * Alpha will be ignored for non-primary page titles. See {@link #setNonPrimaryAlpha(float)}.
     *
     * @param color
     * 		Color hex code in 0xAARRGGBB format
     */
    public void setTextColor(@android.support.annotation.ColorInt
    int color) {
        mTextColor = color;
        mCurrText.setTextColor(color);
        final int transparentColor = (mNonPrimaryAlpha << 24) | (mTextColor & 0xffffff);
        mPrevText.setTextColor(transparentColor);
        mNextText.setTextColor(transparentColor);
    }

    /**
     * Set the default text size to a given unit and value.
     * See {@link TypedValue} for the possible dimension units.
     *
     * <p>Example: to set the text size to 14px, use
     * setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);</p>
     *
     * @param unit
     * 		The desired dimension unit
     * @param size
     * 		The desired size in the given units
     */
    public void setTextSize(int unit, float size) {
        mPrevText.setTextSize(unit, size);
        mCurrText.setTextSize(unit, size);
        mNextText.setTextSize(unit, size);
    }

    /**
     * Set the {@link Gravity} used to position text within the title strip.
     * Only the vertical gravity component is used.
     *
     * @param gravity
     * 		{@link Gravity} constant for positioning title text
     */
    public void setGravity(int gravity) {
        mGravity = gravity;
        requestLayout();
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final android.view.ViewParent parent = getParent();
        if (!(parent instanceof android.support.v4.view.ViewPager)) {
            throw new java.lang.IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
        }
        final android.support.v4.view.ViewPager pager = ((android.support.v4.view.ViewPager) (parent));
        final android.support.v4.view.PagerAdapter adapter = pager.getAdapter();
        pager.setInternalPageChangeListener(mPageListener);
        pager.addOnAdapterChangeListener(mPageListener);
        mPager = pager;
        updateAdapter(mWatchingAdapter != null ? mWatchingAdapter.get() : null, adapter);
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPager != null) {
            updateAdapter(mPager.getAdapter(), null);
            mPager.setInternalPageChangeListener(null);
            mPager.removeOnAdapterChangeListener(mPageListener);
            mPager = null;
        }
    }

    void updateText(int currentItem, android.support.v4.view.PagerAdapter adapter) {
        final int itemCount = (adapter != null) ? adapter.getCount() : 0;
        mUpdatingText = true;
        java.lang.CharSequence text = null;
        if ((currentItem >= 1) && (adapter != null)) {
            text = adapter.getPageTitle(currentItem - 1);
        }
        mPrevText.setText(text);
        mCurrText.setText((adapter != null) && (currentItem < itemCount) ? adapter.getPageTitle(currentItem) : null);
        text = null;
        if (((currentItem + 1) < itemCount) && (adapter != null)) {
            text = adapter.getPageTitle(currentItem + 1);
        }
        mNextText.setText(text);
        // Measure everything
        final int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        final int maxWidth = java.lang.Math.max(0, ((int) (width * 0.8F)));
        final int childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxWidth, android.view.View.MeasureSpec.AT_MOST);
        final int childHeight = (getHeight() - getPaddingTop()) - getPaddingBottom();
        final int maxHeight = java.lang.Math.max(0, childHeight);
        final int childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxHeight, android.view.View.MeasureSpec.AT_MOST);
        mPrevText.measure(childWidthSpec, childHeightSpec);
        mCurrText.measure(childWidthSpec, childHeightSpec);
        mNextText.measure(childWidthSpec, childHeightSpec);
        mLastKnownCurrentPage = currentItem;
        if (!mUpdatingPositions) {
            updateTextPositions(currentItem, mLastKnownPositionOffset, false);
        }
        mUpdatingText = false;
    }

    @java.lang.Override
    public void requestLayout() {
        if (!mUpdatingText) {
            super.requestLayout();
        }
    }

    void updateAdapter(android.support.v4.view.PagerAdapter oldAdapter, android.support.v4.view.PagerAdapter newAdapter) {
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(mPageListener);
            mWatchingAdapter = null;
        }
        if (newAdapter != null) {
            newAdapter.registerDataSetObserver(mPageListener);
            mWatchingAdapter = new java.lang.ref.WeakReference<android.support.v4.view.PagerAdapter>(newAdapter);
        }
        if (mPager != null) {
            mLastKnownCurrentPage = -1;
            mLastKnownPositionOffset = -1;
            updateText(mPager.getCurrentItem(), newAdapter);
            requestLayout();
        }
    }

    void updateTextPositions(int position, float positionOffset, boolean force) {
        if (position != mLastKnownCurrentPage) {
            updateText(position, mPager.getAdapter());
        } else
            if ((!force) && (positionOffset == mLastKnownPositionOffset)) {
                return;
            }

        mUpdatingPositions = true;
        final int prevWidth = mPrevText.getMeasuredWidth();
        final int currWidth = mCurrText.getMeasuredWidth();
        final int nextWidth = mNextText.getMeasuredWidth();
        final int halfCurrWidth = currWidth / 2;
        final int stripWidth = getWidth();
        final int stripHeight = getHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int textPaddedLeft = paddingLeft + halfCurrWidth;
        final int textPaddedRight = paddingRight + halfCurrWidth;
        final int contentWidth = (stripWidth - textPaddedLeft) - textPaddedRight;
        float currOffset = positionOffset + 0.5F;
        if (currOffset > 1.0F) {
            currOffset -= 1.0F;
        }
        final int currCenter = (stripWidth - textPaddedRight) - ((int) (contentWidth * currOffset));
        final int currLeft = currCenter - (currWidth / 2);
        final int currRight = currLeft + currWidth;
        final int prevBaseline = mPrevText.getBaseline();
        final int currBaseline = mCurrText.getBaseline();
        final int nextBaseline = mNextText.getBaseline();
        final int maxBaseline = java.lang.Math.max(java.lang.Math.max(prevBaseline, currBaseline), nextBaseline);
        final int prevTopOffset = maxBaseline - prevBaseline;
        final int currTopOffset = maxBaseline - currBaseline;
        final int nextTopOffset = maxBaseline - nextBaseline;
        final int alignedPrevHeight = prevTopOffset + mPrevText.getMeasuredHeight();
        final int alignedCurrHeight = currTopOffset + mCurrText.getMeasuredHeight();
        final int alignedNextHeight = nextTopOffset + mNextText.getMeasuredHeight();
        final int maxTextHeight = java.lang.Math.max(java.lang.Math.max(alignedPrevHeight, alignedCurrHeight), alignedNextHeight);
        final int vgrav = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        int prevTop;
        int currTop;
        int nextTop;
        switch (vgrav) {
            default :
            case android.view.Gravity.TOP :
                prevTop = paddingTop + prevTopOffset;
                currTop = paddingTop + currTopOffset;
                nextTop = paddingTop + nextTopOffset;
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                final int paddedHeight = (stripHeight - paddingTop) - paddingBottom;
                final int centeredTop = (paddedHeight - maxTextHeight) / 2;
                prevTop = centeredTop + prevTopOffset;
                currTop = centeredTop + currTopOffset;
                nextTop = centeredTop + nextTopOffset;
                break;
            case android.view.Gravity.BOTTOM :
                final int bottomGravTop = (stripHeight - paddingBottom) - maxTextHeight;
                prevTop = bottomGravTop + prevTopOffset;
                currTop = bottomGravTop + currTopOffset;
                nextTop = bottomGravTop + nextTopOffset;
                break;
        }
        mCurrText.layout(currLeft, currTop, currRight, currTop + mCurrText.getMeasuredHeight());
        final int prevLeft = java.lang.Math.min(paddingLeft, (currLeft - mScaledTextSpacing) - prevWidth);
        mPrevText.layout(prevLeft, prevTop, prevLeft + prevWidth, prevTop + mPrevText.getMeasuredHeight());
        final int nextLeft = java.lang.Math.max((stripWidth - paddingRight) - nextWidth, currRight + mScaledTextSpacing);
        mNextText.layout(nextLeft, nextTop, nextLeft + nextWidth, nextTop + mNextText.getMeasuredHeight());
        mLastKnownPositionOffset = positionOffset;
        mUpdatingPositions = false;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != android.view.View.MeasureSpec.EXACTLY) {
            throw new java.lang.IllegalStateException("Must measure with an exact width");
        }
        final int heightPadding = getPaddingTop() + getPaddingBottom();
        final int childHeightSpec = android.view.ViewGroup.getChildMeasureSpec(heightMeasureSpec, heightPadding, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int widthPadding = ((int) (widthSize * 0.2F));
        final int childWidthSpec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, widthPadding, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        mPrevText.measure(childWidthSpec, childHeightSpec);
        mCurrText.measure(childWidthSpec, childHeightSpec);
        mNextText.measure(childWidthSpec, childHeightSpec);
        final int height;
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == android.view.View.MeasureSpec.EXACTLY) {
            height = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        } else {
            final int textHeight = mCurrText.getMeasuredHeight();
            final int minHeight = getMinHeight();
            height = java.lang.Math.max(minHeight, textHeight + heightPadding);
        }
        final int childState = android.support.v4.view.ViewCompat.getMeasuredState(mCurrText);
        final int measuredHeight = android.support.v4.view.ViewCompat.resolveSizeAndState(height, heightMeasureSpec, childState << android.support.v4.view.ViewCompat.MEASURED_HEIGHT_STATE_SHIFT);
        setMeasuredDimension(widthSize, measuredHeight);
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mPager != null) {
            final float offset = (mLastKnownPositionOffset >= 0) ? mLastKnownPositionOffset : 0;
            updateTextPositions(mLastKnownCurrentPage, offset, true);
        }
    }

    int getMinHeight() {
        int minHeight = 0;
        final android.graphics.drawable.Drawable bg = getBackground();
        if (bg != null) {
            minHeight = bg.getIntrinsicHeight();
        }
        return minHeight;
    }

    private class PageListener extends android.database.DataSetObserver implements android.support.v4.view.ViewPager.OnAdapterChangeListener , android.support.v4.view.ViewPager.OnPageChangeListener {
        private int mScrollState;

        PageListener() {
        }

        @java.lang.Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0.5F) {
                // Consider ourselves to be on the next page when we're 50% of the way there.
                position++;
            }
            updateTextPositions(position, positionOffset, false);
        }

        @java.lang.Override
        public void onPageSelected(int position) {
            if (mScrollState == android.support.v4.view.ViewPager.SCROLL_STATE_IDLE) {
                // Only update the text here if we're not dragging or settling.
                updateText(mPager.getCurrentItem(), mPager.getAdapter());
                final float offset = (mLastKnownPositionOffset >= 0) ? mLastKnownPositionOffset : 0;
                updateTextPositions(mPager.getCurrentItem(), offset, true);
            }
        }

        @java.lang.Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @java.lang.Override
        public void onAdapterChanged(android.support.v4.view.ViewPager viewPager, android.support.v4.view.PagerAdapter oldAdapter, android.support.v4.view.PagerAdapter newAdapter) {
            updateAdapter(oldAdapter, newAdapter);
        }

        @java.lang.Override
        public void onChanged() {
            updateText(mPager.getCurrentItem(), mPager.getAdapter());
            final float offset = (mLastKnownPositionOffset >= 0) ? mLastKnownPositionOffset : 0;
            updateTextPositions(mPager.getCurrentItem(), offset, true);
        }
    }
}

