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
package android.view;


/**
 * A header of a notification view
 *
 * @unknown 
 */
@android.widget.RemoteViews.RemoteView
public class NotificationHeaderView extends android.view.ViewGroup {
    public static final int NO_COLOR = android.app.Notification.COLOR_INVALID;

    private final int mChildMinWidth;

    private final int mContentEndMargin;

    private final int mGravity;

    private android.view.View mAppName;

    private android.view.View mHeaderText;

    private android.view.View mSecondaryHeaderText;

    private android.view.View.OnClickListener mExpandClickListener;

    private android.view.View.OnClickListener mAppOpsListener;

    private android.view.NotificationHeaderView.HeaderTouchListener mTouchListener = new android.view.NotificationHeaderView.HeaderTouchListener();

    private android.widget.ImageView mExpandButton;

    private com.android.internal.widget.CachingIconView mIcon;

    private android.view.View mProfileBadge;

    private android.view.View mOverlayIcon;

    private android.view.View mCameraIcon;

    private android.view.View mMicIcon;

    private android.view.View mAppOps;

    private android.view.View mAudiblyAlertedIcon;

    private int mIconColor;

    private int mOriginalNotificationColor;

    private boolean mExpanded;

    private boolean mShowExpandButtonAtEnd;

    private boolean mShowWorkBadgeAtEnd;

    private int mHeaderTextMarginEnd;

    private android.graphics.drawable.Drawable mBackground;

    private boolean mEntireHeaderClickable;

    private boolean mExpandOnlyOnButton;

    private boolean mAcceptAllTouches;

    private int mTotalWidth;

    android.view.ViewOutlineProvider mProvider = new android.view.ViewOutlineProvider() {
        @java.lang.Override
        public void getOutline(android.view.View view, android.graphics.Outline outline) {
            if (mBackground != null) {
                outline.setRect(0, 0, getWidth(), getHeight());
                outline.setAlpha(1.0F);
            }
        }
    };

    public NotificationHeaderView(android.content.Context context) {
        this(context, null);
    }

    @android.annotation.UnsupportedAppUsage
    public NotificationHeaderView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationHeaderView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NotificationHeaderView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.Resources res = getResources();
        mChildMinWidth = res.getDimensionPixelSize(R.dimen.notification_header_shrink_min_width);
        mContentEndMargin = res.getDimensionPixelSize(R.dimen.notification_content_margin_end);
        mEntireHeaderClickable = res.getBoolean(R.bool.config_notificationHeaderClickableForExpand);
        int[] attrIds = new int[]{ android.R.attr.gravity };
        android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, attrIds, defStyleAttr, defStyleRes);
        mGravity = ta.getInt(0, 0);
        ta.recycle();
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAppName = findViewById(com.android.internal.R.id.app_name_text);
        mHeaderText = findViewById(com.android.internal.R.id.header_text);
        mSecondaryHeaderText = findViewById(com.android.internal.R.id.header_text_secondary);
        mExpandButton = findViewById(com.android.internal.R.id.expand_button);
        mIcon = findViewById(com.android.internal.R.id.icon);
        mProfileBadge = findViewById(com.android.internal.R.id.profile_badge);
        mCameraIcon = findViewById(com.android.internal.R.id.camera);
        mMicIcon = findViewById(com.android.internal.R.id.mic);
        mOverlayIcon = findViewById(com.android.internal.R.id.overlay);
        mAppOps = findViewById(com.android.internal.R.id.app_ops);
        mAudiblyAlertedIcon = findViewById(com.android.internal.R.id.alerted_icon);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int givenWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int givenHeight = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        int wrapContentWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(givenWidth, android.view.View.MeasureSpec.AT_MOST);
        int wrapContentHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(givenHeight, android.view.View.MeasureSpec.AT_MOST);
        int totalWidth = getPaddingStart();
        int iconWidth = getPaddingEnd();
        for (int i = 0; i < getChildCount(); i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                // We'll give it the rest of the space in the end
                continue;
            }
            final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
            int childWidthSpec = android.view.ViewGroup.getChildMeasureSpec(wrapContentWidthSpec, lp.leftMargin + lp.rightMargin, lp.width);
            int childHeightSpec = android.view.ViewGroup.getChildMeasureSpec(wrapContentHeightSpec, lp.topMargin + lp.bottomMargin, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
            if ((((child == mExpandButton) && mShowExpandButtonAtEnd) || (child == mProfileBadge)) || (child == mAppOps)) {
                iconWidth += (lp.leftMargin + lp.rightMargin) + child.getMeasuredWidth();
            } else {
                totalWidth += (lp.leftMargin + lp.rightMargin) + child.getMeasuredWidth();
            }
        }
        // Ensure that there is at least enough space for the icons
        int endMargin = java.lang.Math.max(mHeaderTextMarginEnd, iconWidth);
        if (totalWidth > (givenWidth - endMargin)) {
            int overFlow = (totalWidth - givenWidth) + endMargin;
            // We are overflowing, lets shrink the app name first
            overFlow = shrinkViewForOverflow(wrapContentHeightSpec, overFlow, mAppName, mChildMinWidth);
            // still overflowing, we shrink the header text
            overFlow = shrinkViewForOverflow(wrapContentHeightSpec, overFlow, mHeaderText, 0);
            // still overflowing, finally we shrink the secondary header text
            shrinkViewForOverflow(wrapContentHeightSpec, overFlow, mSecondaryHeaderText, 0);
        }
        totalWidth += getPaddingEnd();
        mTotalWidth = java.lang.Math.min(totalWidth, givenWidth);
        setMeasuredDimension(givenWidth, givenHeight);
    }

    private int shrinkViewForOverflow(int heightSpec, int overFlow, android.view.View targetView, int minimumWidth) {
        final int oldWidth = targetView.getMeasuredWidth();
        if (((overFlow > 0) && (targetView.getVisibility() != android.view.View.GONE)) && (oldWidth > minimumWidth)) {
            // we're still too big
            int newSize = java.lang.Math.max(minimumWidth, oldWidth - overFlow);
            int childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(newSize, android.view.View.MeasureSpec.AT_MOST);
            targetView.measure(childWidthSpec, heightSpec);
            overFlow -= oldWidth - newSize;
        }
        return overFlow;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingStart();
        int end = getMeasuredWidth();
        final boolean centerAligned = (mGravity & android.view.Gravity.CENTER_HORIZONTAL) != 0;
        if (centerAligned) {
            left += (getMeasuredWidth() / 2) - (mTotalWidth / 2);
        }
        int childCount = getChildCount();
        int ownHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        for (int i = 0; i < childCount; i++) {
            android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            int childHeight = child.getMeasuredHeight();
            android.view.ViewGroup.MarginLayoutParams params = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
            int layoutLeft;
            int layoutRight;
            int top = ((int) (getPaddingTop() + ((ownHeight - childHeight) / 2.0F)));
            int bottom = top + childHeight;
            if ((((child == mExpandButton) && mShowExpandButtonAtEnd) || (child == mProfileBadge)) || (child == mAppOps)) {
                if (end == getMeasuredWidth()) {
                    layoutRight = end - mContentEndMargin;
                } else {
                    layoutRight = end - params.getMarginEnd();
                }
                layoutLeft = layoutRight - child.getMeasuredWidth();
                end = layoutLeft - params.getMarginStart();
            } else {
                left += params.getMarginStart();
                int right = left + child.getMeasuredWidth();
                layoutLeft = left;
                layoutRight = right;
                left = right + params.getMarginEnd();
            }
            if (getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) {
                int ltrLeft = layoutLeft;
                layoutLeft = getWidth() - layoutRight;
                layoutRight = getWidth() - ltrLeft;
            }
            child.layout(layoutLeft, top, layoutRight, bottom);
        }
        updateTouchListener();
    }

    @java.lang.Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.view.ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    /**
     * Set a {@link Drawable} to be displayed as a background on the header.
     */
    public void setHeaderBackgroundDrawable(android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            setWillNotDraw(false);
            mBackground = drawable;
            mBackground.setCallback(this);
            setOutlineProvider(mProvider);
        } else {
            setWillNotDraw(true);
            mBackground = null;
            setOutlineProvider(null);
        }
        invalidate();
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        if (mBackground != null) {
            mBackground.setBounds(0, 0, getWidth(), getHeight());
            mBackground.draw(canvas);
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(android.graphics.drawable.Drawable who) {
        return super.verifyDrawable(who) || (who == mBackground);
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        if ((mBackground != null) && mBackground.isStateful()) {
            mBackground.setState(getDrawableState());
        }
    }

    private void updateTouchListener() {
        if ((mExpandClickListener == null) && (mAppOpsListener == null)) {
            setOnTouchListener(null);
            return;
        }
        setOnTouchListener(mTouchListener);
        mTouchListener.bindTouchRects();
    }

    /**
     * Sets onclick listener for app ops icons.
     */
    public void setAppOpsOnClickListener(android.view.View.OnClickListener l) {
        mAppOpsListener = l;
        mAppOps.setOnClickListener(mAppOpsListener);
        mCameraIcon.setOnClickListener(mAppOpsListener);
        mMicIcon.setOnClickListener(mAppOpsListener);
        mOverlayIcon.setOnClickListener(mAppOpsListener);
        updateTouchListener();
    }

    @java.lang.Override
    public void setOnClickListener(@android.annotation.Nullable
    android.view.View.OnClickListener l) {
        mExpandClickListener = l;
        mExpandButton.setOnClickListener(mExpandClickListener);
        updateTouchListener();
    }

    @android.view.RemotableViewMethod
    public void setOriginalIconColor(int color) {
        mIconColor = color;
    }

    public int getOriginalIconColor() {
        return mIconColor;
    }

    @android.view.RemotableViewMethod
    public void setOriginalNotificationColor(int color) {
        mOriginalNotificationColor = color;
    }

    public int getOriginalNotificationColor() {
        return mOriginalNotificationColor;
    }

    @android.view.RemotableViewMethod
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
        updateExpandButton();
    }

    /**
     * Shows or hides 'app op in use' icons based on app usage.
     */
    public void showAppOpsIcons(android.util.ArraySet<java.lang.Integer> appOps) {
        if ((((mOverlayIcon == null) || (mCameraIcon == null)) || (mMicIcon == null)) || (appOps == null)) {
            return;
        }
        mOverlayIcon.setVisibility(appOps.contains(AppOpsManager.OP_SYSTEM_ALERT_WINDOW) ? android.view.View.VISIBLE : android.view.View.GONE);
        mCameraIcon.setVisibility(appOps.contains(AppOpsManager.OP_CAMERA) ? android.view.View.VISIBLE : android.view.View.GONE);
        mMicIcon.setVisibility(appOps.contains(AppOpsManager.OP_RECORD_AUDIO) ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    /**
     * Updates icon visibility based on the noisiness of the notification.
     */
    public void setRecentlyAudiblyAlerted(boolean audiblyAlerted) {
        mAudiblyAlertedIcon.setVisibility(audiblyAlerted ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void updateExpandButton() {
        int drawableId;
        int contentDescriptionId;
        if (mExpanded) {
            drawableId = R.drawable.ic_collapse_notification;
            contentDescriptionId = R.string.expand_button_content_description_expanded;
        } else {
            drawableId = R.drawable.ic_expand_notification;
            contentDescriptionId = R.string.expand_button_content_description_collapsed;
        }
        mExpandButton.setImageDrawable(getContext().getDrawable(drawableId));
        mExpandButton.setColorFilter(mOriginalNotificationColor);
        mExpandButton.setContentDescription(mContext.getText(contentDescriptionId));
    }

    public void setShowWorkBadgeAtEnd(boolean showWorkBadgeAtEnd) {
        if (showWorkBadgeAtEnd != mShowWorkBadgeAtEnd) {
            setClipToPadding(!showWorkBadgeAtEnd);
            mShowWorkBadgeAtEnd = showWorkBadgeAtEnd;
        }
    }

    /**
     * Sets whether or not the expand button appears at the end of the NotificationHeaderView. If
     * both this and {@link #setShowWorkBadgeAtEnd(boolean)} have been set to true, then the
     * expand button will appear closer to the end than the work badge.
     */
    public void setShowExpandButtonAtEnd(boolean showExpandButtonAtEnd) {
        if (showExpandButtonAtEnd != mShowExpandButtonAtEnd) {
            setClipToPadding(!showExpandButtonAtEnd);
            mShowExpandButtonAtEnd = showExpandButtonAtEnd;
        }
    }

    public android.view.View getWorkProfileIcon() {
        return mProfileBadge;
    }

    public com.android.internal.widget.CachingIconView getIcon() {
        return mIcon;
    }

    /**
     * Sets the margin end for the text portion of the header, excluding right-aligned elements
     *
     * @param headerTextMarginEnd
     * 		margin size
     */
    @android.view.RemotableViewMethod
    public void setHeaderTextMarginEnd(int headerTextMarginEnd) {
        if (mHeaderTextMarginEnd != headerTextMarginEnd) {
            mHeaderTextMarginEnd = headerTextMarginEnd;
            requestLayout();
        }
    }

    /**
     * Get the current margin end value for the header text
     *
     * @return margin size
     */
    public int getHeaderTextMarginEnd() {
        return mHeaderTextMarginEnd;
    }

    public class HeaderTouchListener implements android.view.View.OnTouchListener {
        private final java.util.ArrayList<android.graphics.Rect> mTouchRects = new java.util.ArrayList<>();

        private android.graphics.Rect mExpandButtonRect;

        private android.graphics.Rect mAppOpsRect;

        private int mTouchSlop;

        private boolean mTrackGesture;

        private float mDownX;

        private float mDownY;

        public HeaderTouchListener() {
        }

        public void bindTouchRects() {
            mTouchRects.clear();
            addRectAroundView(mIcon);
            mExpandButtonRect = addRectAroundView(mExpandButton);
            mAppOpsRect = addRectAroundView(mAppOps);
            addWidthRect();
            mTouchSlop = android.view.ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }

        private void addWidthRect() {
            android.graphics.Rect r = new android.graphics.Rect();
            r.top = 0;
            r.bottom = ((int) (32 * getResources().getDisplayMetrics().density));
            r.left = 0;
            r.right = getWidth();
            mTouchRects.add(r);
        }

        private android.graphics.Rect addRectAroundView(android.view.View view) {
            final android.graphics.Rect r = getRectAroundView(view);
            mTouchRects.add(r);
            return r;
        }

        private android.graphics.Rect getRectAroundView(android.view.View view) {
            float size = 48 * getResources().getDisplayMetrics().density;
            float width = java.lang.Math.max(size, view.getWidth());
            float height = java.lang.Math.max(size, view.getHeight());
            final android.graphics.Rect r = new android.graphics.Rect();
            if (view.getVisibility() == android.view.View.GONE) {
                view = getFirstChildNotGone();
                r.left = ((int) (view.getLeft() - (width / 2.0F)));
            } else {
                r.left = ((int) (((view.getLeft() + view.getRight()) / 2.0F) - (width / 2.0F)));
            }
            r.top = ((int) (((view.getTop() + view.getBottom()) / 2.0F) - (height / 2.0F)));
            r.bottom = ((int) (r.top + height));
            r.right = ((int) (r.left + width));
            return r;
        }

        @java.lang.Override
        public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getActionMasked() & android.view.MotionEvent.ACTION_MASK) {
                case android.view.MotionEvent.ACTION_DOWN :
                    mTrackGesture = false;
                    if (isInside(x, y)) {
                        mDownX = x;
                        mDownY = y;
                        mTrackGesture = true;
                        return true;
                    }
                    break;
                case android.view.MotionEvent.ACTION_MOVE :
                    if (mTrackGesture) {
                        if ((java.lang.Math.abs(mDownX - x) > mTouchSlop) || (java.lang.Math.abs(mDownY - y) > mTouchSlop)) {
                            mTrackGesture = false;
                        }
                    }
                    break;
                case android.view.MotionEvent.ACTION_UP :
                    if (mTrackGesture) {
                        if (mAppOps.isVisibleToUser() && (mAppOpsRect.contains(((int) (x)), ((int) (y))) || mAppOpsRect.contains(((int) (mDownX)), ((int) (mDownY))))) {
                            mAppOps.performClick();
                            return true;
                        }
                        mExpandButton.performClick();
                    }
                    break;
            }
            return mTrackGesture;
        }

        private boolean isInside(float x, float y) {
            if (mAcceptAllTouches) {
                return true;
            }
            if (mExpandOnlyOnButton) {
                return mExpandButtonRect.contains(((int) (x)), ((int) (y)));
            }
            for (int i = 0; i < mTouchRects.size(); i++) {
                android.graphics.Rect r = mTouchRects.get(i);
                if (r.contains(((int) (x)), ((int) (y)))) {
                    return true;
                }
            }
            return false;
        }
    }

    private android.view.View getFirstChildNotGone() {
        for (int i = 0; i < getChildCount(); i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                return child;
            }
        }
        return this;
    }

    public android.widget.ImageView getExpandButton() {
        return mExpandButton;
    }

    @java.lang.Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean isInTouchRect(float x, float y) {
        if (mExpandClickListener == null) {
            return false;
        }
        return mTouchListener.isInside(x, y);
    }

    /**
     * Sets whether or not all touches to this header view will register as a click. Note that
     * if the config value for {@code config_notificationHeaderClickableForExpand} is {@code true},
     * then calling this method with {@code false} will not override that configuration.
     */
    @android.view.RemotableViewMethod
    public void setAcceptAllTouches(boolean acceptAllTouches) {
        mAcceptAllTouches = mEntireHeaderClickable || acceptAllTouches;
    }

    /**
     * Sets whether only the expand icon itself should serve as the expand target.
     */
    @android.view.RemotableViewMethod
    public void setExpandOnlyOnButton(boolean expandOnlyOnButton) {
        mExpandOnlyOnButton = expandOnlyOnButton;
    }
}

