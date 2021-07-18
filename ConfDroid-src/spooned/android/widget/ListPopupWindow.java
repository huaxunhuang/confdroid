/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * A ListPopupWindow anchors itself to a host view and displays a
 * list of choices.
 *
 * <p>ListPopupWindow contains a number of tricky behaviors surrounding
 * positioning, scrolling parents to fit the dropdown, interacting
 * sanely with the IME if present, and others.
 *
 * @see android.widget.AutoCompleteTextView
 * @see android.widget.Spinner
 */
public class ListPopupWindow implements com.android.internal.view.menu.ShowableListMenu {
    private static final java.lang.String TAG = "ListPopupWindow";

    private static final boolean DEBUG = false;

    /**
     * This value controls the length of time that the user
     * must leave a pointer down without scrolling to expand
     * the autocomplete dropdown list to cover the IME.
     */
    private static final int EXPAND_LIST_TIMEOUT = 250;

    private android.content.Context mContext;

    private android.widget.ListAdapter mAdapter;

    @android.annotation.UnsupportedAppUsage
    private android.widget.DropDownListView mDropDownList;

    private int mDropDownHeight = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

    private int mDropDownWidth = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

    private int mDropDownHorizontalOffset;

    private int mDropDownVerticalOffset;

    private int mDropDownWindowLayoutType = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;

    private boolean mDropDownVerticalOffsetSet;

    private boolean mIsAnimatedFromAnchor = true;

    private boolean mOverlapAnchor;

    private boolean mOverlapAnchorSet;

    private int mDropDownGravity = android.view.Gravity.NO_GRAVITY;

    private boolean mDropDownAlwaysVisible = false;

    private boolean mForceIgnoreOutsideTouch = false;

    int mListItemExpandMaximum = java.lang.Integer.MAX_VALUE;

    private android.view.View mPromptView;

    private int mPromptPosition = android.widget.ListPopupWindow.POSITION_PROMPT_ABOVE;

    private android.database.DataSetObserver mObserver;

    private android.view.View mDropDownAnchorView;

    private android.graphics.drawable.Drawable mDropDownListHighlight;

    private android.widget.AdapterView.OnItemClickListener mItemClickListener;

    private android.widget.AdapterView.OnItemSelectedListener mItemSelectedListener;

    private final android.widget.ListPopupWindow.ResizePopupRunnable mResizePopupRunnable = new android.widget.ListPopupWindow.ResizePopupRunnable();

    private final android.widget.ListPopupWindow.PopupTouchInterceptor mTouchInterceptor = new android.widget.ListPopupWindow.PopupTouchInterceptor();

    private final android.widget.ListPopupWindow.PopupScrollListener mScrollListener = new android.widget.ListPopupWindow.PopupScrollListener();

    private final android.widget.ListPopupWindow.ListSelectorHider mHideSelector = new android.widget.ListPopupWindow.ListSelectorHider();

    private java.lang.Runnable mShowDropDownRunnable;

    private final android.os.Handler mHandler;

    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    /**
     * Optional anchor-relative bounds to be used as the transition epicenter.
     * When {@code null}, the anchor bounds are used as the epicenter.
     */
    private android.graphics.Rect mEpicenterBounds;

    private boolean mModal;

    @android.annotation.UnsupportedAppUsage
    android.widget.PopupWindow mPopup;

    /**
     * The provided prompt view should appear above list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_ABOVE = 0;

    /**
     * The provided prompt view should appear below list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_BELOW = 1;

    /**
     * Alias for {@link ViewGroup.LayoutParams#MATCH_PARENT}.
     * If used to specify a popup width, the popup will match the width of the anchor view.
     * If used to specify a popup height, the popup will fill available space.
     */
    public static final int MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

    /**
     * Alias for {@link ViewGroup.LayoutParams#WRAP_CONTENT}.
     * If used to specify a popup width, the popup will use the width of its content.
     */
    public static final int WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * Mode for {@link #setInputMethodMode(int)}: the requirements for the
     * input method should be based on the focusability of the popup.  That is
     * if it is focusable than it needs to work with the input method, else
     * it doesn't.
     */
    public static final int INPUT_METHOD_FROM_FOCUSABLE = android.widget.PopupWindow.INPUT_METHOD_FROM_FOCUSABLE;

    /**
     * Mode for {@link #setInputMethodMode(int)}: this popup always needs to
     * work with an input method, regardless of whether it is focusable.  This
     * means that it will always be displayed so that the user can also operate
     * the input method while it is shown.
     */
    public static final int INPUT_METHOD_NEEDED = android.widget.PopupWindow.INPUT_METHOD_NEEDED;

    /**
     * Mode for {@link #setInputMethodMode(int)}: this popup never needs to
     * work with an input method, regardless of whether it is focusable.  This
     * means that it will always be displayed to use as much space on the
     * screen as needed, regardless of whether this covers the input method.
     */
    public static final int INPUT_METHOD_NOT_NEEDED = android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED;

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context
     * 		Context used for contained views.
     */
    public ListPopupWindow(@android.annotation.NonNull
    android.content.Context context) {
        this(context, null, com.android.internal.R.attr.listPopupWindowStyle, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context
     * 		Context used for contained views.
     * @param attrs
     * 		Attributes from inflating parent views used to style the popup.
     */
    public ListPopupWindow(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.listPopupWindowStyle, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context
     * 		Context used for contained views.
     * @param attrs
     * 		Attributes from inflating parent views used to style the popup.
     * @param defStyleAttr
     * 		Default style attribute to use for popup content.
     */
    public ListPopupWindow(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, @android.annotation.AttrRes
    int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context
     * 		Context used for contained views.
     * @param attrs
     * 		Attributes from inflating parent views used to style the popup.
     * @param defStyleAttr
     * 		Style attribute to read for default styling of popup content.
     * @param defStyleRes
     * 		Style resource ID to use for default styling of popup content.
     */
    public ListPopupWindow(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes) {
        mContext = context;
        mHandler = new android.os.Handler(context.getMainLooper());
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListPopupWindow, defStyleAttr, defStyleRes);
        mDropDownHorizontalOffset = a.getDimensionPixelOffset(R.styleable.ListPopupWindow_dropDownHorizontalOffset, 0);
        mDropDownVerticalOffset = a.getDimensionPixelOffset(R.styleable.ListPopupWindow_dropDownVerticalOffset, 0);
        if (mDropDownVerticalOffset != 0) {
            mDropDownVerticalOffsetSet = true;
        }
        a.recycle();
        mPopup = new android.widget.PopupWindow(context, attrs, defStyleAttr, defStyleRes);
        mPopup.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NEEDED);
    }

    /**
     * Sets the adapter that provides the data and the views to represent the data
     * in this popup window.
     *
     * @param adapter
     * 		The adapter to use to create this window's content.
     */
    public void setAdapter(@android.annotation.Nullable
    android.widget.ListAdapter adapter) {
        if (mObserver == null) {
            mObserver = new android.widget.ListPopupWindow.PopupDataSetObserver();
        } else
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(mObserver);
            }

        mAdapter = adapter;
        if (mAdapter != null) {
            adapter.registerDataSetObserver(mObserver);
        }
        if (mDropDownList != null) {
            mDropDownList.setAdapter(mAdapter);
        }
    }

    /**
     * Set where the optional prompt view should appear. The default is
     * {@link #POSITION_PROMPT_ABOVE}.
     *
     * @param position
     * 		A position constant declaring where the prompt should be displayed.
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public void setPromptPosition(int position) {
        mPromptPosition = position;
    }

    /**
     *
     *
     * @return Where the optional prompt view should appear.
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public int getPromptPosition() {
        return mPromptPosition;
    }

    /**
     * Set whether this window should be modal when shown.
     *
     * <p>If a popup window is modal, it will receive all touch and key input.
     * If the user touches outside the popup window's content area the popup window
     * will be dismissed.
     *
     * @param modal
     * 		{@code true} if the popup window should be modal, {@code false} otherwise.
     */
    public void setModal(boolean modal) {
        mModal = modal;
        mPopup.setFocusable(modal);
    }

    /**
     * Returns whether the popup window will be modal when shown.
     *
     * @return {@code true} if the popup window will be modal, {@code false} otherwise.
     */
    public boolean isModal() {
        return mModal;
    }

    /**
     * Forces outside touches to be ignored. Normally if {@link #isDropDownAlwaysVisible()} is
     * false, we allow outside touch to dismiss the dropdown. If this is set to true, then we
     * ignore outside touch even when the drop down is not set to always visible.
     *
     * @unknown Used only by AutoCompleteTextView to handle some internal special cases.
     */
    @android.annotation.UnsupportedAppUsage
    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    /**
     * Sets whether the drop-down should remain visible under certain conditions.
     *
     * The drop-down will occupy the entire screen below {@link #getAnchorView} regardless
     * of the size or content of the list.  {@link #getBackground()} will fill any space
     * that is not used by the list.
     *
     * @param dropDownAlwaysVisible
     * 		Whether to keep the drop-down visible.
     * @unknown Only used by AutoCompleteTextView under special conditions.
     */
    @android.annotation.UnsupportedAppUsage
    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    /**
     *
     *
     * @return Whether the drop-down is visible under special conditions.
     * @unknown Only used by AutoCompleteTextView under special conditions.
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isDropDownAlwaysVisible() {
        return mDropDownAlwaysVisible;
    }

    /**
     * Sets the operating mode for the soft input area.
     *
     * @param mode
     * 		The desired mode, see
     * 		{@link android.view.WindowManager.LayoutParams#softInputMode}
     * 		for the full list
     * @see android.view.WindowManager.LayoutParams#softInputMode
     * @see #getSoftInputMode()
     */
    public void setSoftInputMode(int mode) {
        mPopup.setSoftInputMode(mode);
    }

    /**
     * Returns the current value in {@link #setSoftInputMode(int)}.
     *
     * @see #setSoftInputMode(int)
     * @see android.view.WindowManager.LayoutParams#softInputMode
     */
    public int getSoftInputMode() {
        return mPopup.getSoftInputMode();
    }

    /**
     * Sets a drawable to use as the list item selector.
     *
     * @param selector
     * 		List selector drawable to use in the popup.
     */
    public void setListSelector(android.graphics.drawable.Drawable selector) {
        mDropDownListHighlight = selector;
    }

    /**
     *
     *
     * @return The background drawable for the popup window.
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getBackground() {
        return mPopup.getBackground();
    }

    /**
     * Sets a drawable to be the background for the popup window.
     *
     * @param d
     * 		A drawable to set as the background.
     */
    public void setBackgroundDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable d) {
        mPopup.setBackgroundDrawable(d);
    }

    /**
     * Set an animation style to use when the popup window is shown or dismissed.
     *
     * @param animationStyle
     * 		Animation style to use.
     */
    public void setAnimationStyle(@android.annotation.StyleRes
    int animationStyle) {
        mPopup.setAnimationStyle(animationStyle);
    }

    /**
     * Returns the animation style that will be used when the popup window is
     * shown or dismissed.
     *
     * @return Animation style that will be used.
     */
    @android.annotation.StyleRes
    public int getAnimationStyle() {
        return mPopup.getAnimationStyle();
    }

    /**
     * Returns the view that will be used to anchor this popup.
     *
     * @return The popup's anchor view
     */
    @android.annotation.Nullable
    public android.view.View getAnchorView() {
        return mDropDownAnchorView;
    }

    /**
     * Sets the popup's anchor view. This popup will always be positioned relative to
     * the anchor view when shown.
     *
     * @param anchor
     * 		The view to use as an anchor.
     */
    public void setAnchorView(@android.annotation.Nullable
    android.view.View anchor) {
        mDropDownAnchorView = anchor;
    }

    /**
     *
     *
     * @return The horizontal offset of the popup from its anchor in pixels.
     */
    public int getHorizontalOffset() {
        return mDropDownHorizontalOffset;
    }

    /**
     * Set the horizontal offset of this popup from its anchor view in pixels.
     *
     * @param offset
     * 		The horizontal offset of the popup from its anchor.
     */
    public void setHorizontalOffset(int offset) {
        mDropDownHorizontalOffset = offset;
    }

    /**
     *
     *
     * @return The vertical offset of the popup from its anchor in pixels.
     */
    public int getVerticalOffset() {
        if (!mDropDownVerticalOffsetSet) {
            return 0;
        }
        return mDropDownVerticalOffset;
    }

    /**
     * Set the vertical offset of this popup from its anchor view in pixels.
     *
     * @param offset
     * 		The vertical offset of the popup from its anchor.
     */
    public void setVerticalOffset(int offset) {
        mDropDownVerticalOffset = offset;
        mDropDownVerticalOffsetSet = true;
    }

    /**
     * Specifies the anchor-relative bounds of the popup's transition
     * epicenter.
     *
     * @param bounds
     * 		anchor-relative bounds, or {@code null} to use default epicenter
     * @see #getEpicenterBounds()
     */
    public void setEpicenterBounds(@android.annotation.Nullable
    android.graphics.Rect bounds) {
        mEpicenterBounds = (bounds != null) ? new android.graphics.Rect(bounds) : null;
    }

    /**
     * Returns bounds which are used as a popup's epicenter
     * of the enter and exit transitions.
     *
     * @return bounds relative to anchor view, or {@code null} if not set
     * @see #setEpicenterBounds(Rect)
     */
    @android.annotation.Nullable
    public android.graphics.Rect getEpicenterBounds() {
        return mEpicenterBounds != null ? new android.graphics.Rect(mEpicenterBounds) : null;
    }

    /**
     * Set the gravity of the dropdown list. This is commonly used to
     * set gravity to START or END for alignment with the anchor.
     *
     * @param gravity
     * 		Gravity value to use
     */
    public void setDropDownGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    /**
     *
     *
     * @return The width of the popup window in pixels.
     */
    public int getWidth() {
        return mDropDownWidth;
    }

    /**
     * Sets the width of the popup window in pixels. Can also be {@link #MATCH_PARENT}
     * or {@link #WRAP_CONTENT}.
     *
     * @param width
     * 		Width of the popup window.
     */
    public void setWidth(int width) {
        mDropDownWidth = width;
    }

    /**
     * Sets the width of the popup window by the size of its content. The final width may be
     * larger to accommodate styled window dressing.
     *
     * @param width
     * 		Desired width of content in pixels.
     */
    public void setContentWidth(int width) {
        android.graphics.drawable.Drawable popupBackground = mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(mTempRect);
            mDropDownWidth = (mTempRect.left + mTempRect.right) + width;
        } else {
            setWidth(width);
        }
    }

    /**
     *
     *
     * @return The height of the popup window in pixels.
     */
    public int getHeight() {
        return mDropDownHeight;
    }

    /**
     * Sets the height of the popup window in pixels. Can also be {@link #MATCH_PARENT}.
     *
     * @param height
     * 		Height of the popup window must be a positive value,
     * 		{@link #MATCH_PARENT}, or {@link #WRAP_CONTENT}.
     * @throws IllegalArgumentException
     * 		if height is set to negative value
     */
    public void setHeight(int height) {
        if (((height < 0) && (android.view.ViewGroup.LayoutParams.WRAP_CONTENT != height)) && (android.view.ViewGroup.LayoutParams.MATCH_PARENT != height)) {
            if (mContext.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.O) {
                android.util.Log.e(android.widget.ListPopupWindow.TAG, (("Negative value " + height) + " passed to ListPopupWindow#setHeight") + " produces undefined results");
            } else {
                throw new java.lang.IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
            }
        }
        mDropDownHeight = height;
    }

    /**
     * Set the layout type for this popup window.
     * <p>
     * See {@link WindowManager.LayoutParams#type} for possible values.
     *
     * @param layoutType
     * 		Layout type for this window.
     * @see WindowManager.LayoutParams#type
     */
    public void setWindowLayoutType(int layoutType) {
        mDropDownWindowLayoutType = layoutType;
    }

    /**
     * Sets a listener to receive events when a list item is clicked.
     *
     * @param clickListener
     * 		Listener to register
     * @see ListView#setOnItemClickListener(android.widget.AdapterView.OnItemClickListener)
     */
    public void setOnItemClickListener(@android.annotation.Nullable
    android.widget.AdapterView.OnItemClickListener clickListener) {
        mItemClickListener = clickListener;
    }

    /**
     * Sets a listener to receive events when a list item is selected.
     *
     * @param selectedListener
     * 		Listener to register.
     * @see ListView#setOnItemSelectedListener(OnItemSelectedListener)
     */
    public void setOnItemSelectedListener(@android.annotation.Nullable
    android.widget.AdapterView.OnItemSelectedListener selectedListener) {
        mItemSelectedListener = selectedListener;
    }

    /**
     * Set a view to act as a user prompt for this popup window. Where the prompt view will appear
     * is controlled by {@link #setPromptPosition(int)}.
     *
     * @param prompt
     * 		View to use as an informational prompt.
     */
    public void setPromptView(@android.annotation.Nullable
    android.view.View prompt) {
        boolean showing = isShowing();
        if (showing) {
            removePromptView();
        }
        mPromptView = prompt;
        if (showing) {
            show();
        }
    }

    /**
     * Post a {@link #show()} call to the UI thread.
     */
    public void postShow() {
        mHandler.post(mShowDropDownRunnable);
    }

    /**
     * Show the popup list. If the list is already showing, this method
     * will recalculate the popup's size and position.
     */
    @java.lang.Override
    public void show() {
        int height = buildDropDown();
        final boolean noInputMethod = isInputMethodNotNeeded();
        mPopup.setAllowScrollingAnchorParent(!noInputMethod);
        mPopup.setWindowLayoutType(mDropDownWindowLayoutType);
        if (mPopup.isShowing()) {
            if (!getAnchorView().isAttachedToWindow()) {
                // Don't update position if the anchor view is detached from window.
                return;
            }
            final int widthSpec;
            if (mDropDownWidth == android.view.ViewGroup.LayoutParams.MATCH_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                widthSpec = -1;
            } else
                if (mDropDownWidth == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
                    widthSpec = getAnchorView().getWidth();
                } else {
                    widthSpec = mDropDownWidth;
                }

            final int heightSpec;
            if (mDropDownHeight == android.view.ViewGroup.LayoutParams.MATCH_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                heightSpec = (noInputMethod) ? height : android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                if (noInputMethod) {
                    mPopup.setWidth(mDropDownWidth == android.view.ViewGroup.LayoutParams.MATCH_PARENT ? android.view.ViewGroup.LayoutParams.MATCH_PARENT : 0);
                    mPopup.setHeight(0);
                } else {
                    mPopup.setWidth(mDropDownWidth == android.view.ViewGroup.LayoutParams.MATCH_PARENT ? android.view.ViewGroup.LayoutParams.MATCH_PARENT : 0);
                    mPopup.setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                }
            } else
                if (mDropDownHeight == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
                    heightSpec = height;
                } else {
                    heightSpec = mDropDownHeight;
                }

            mPopup.setOutsideTouchable((!mForceIgnoreOutsideTouch) && (!mDropDownAlwaysVisible));
            mPopup.update(getAnchorView(), mDropDownHorizontalOffset, mDropDownVerticalOffset, widthSpec < 0 ? -1 : widthSpec, heightSpec < 0 ? -1 : heightSpec);
            mPopup.getContentView().restoreDefaultFocus();
        } else {
            final int widthSpec;
            if (mDropDownWidth == android.view.ViewGroup.LayoutParams.MATCH_PARENT) {
                widthSpec = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                if (mDropDownWidth == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
                    widthSpec = getAnchorView().getWidth();
                } else {
                    widthSpec = mDropDownWidth;
                }
            }
            final int heightSpec;
            if (mDropDownHeight == android.view.ViewGroup.LayoutParams.MATCH_PARENT) {
                heightSpec = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                if (mDropDownHeight == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
                    heightSpec = height;
                } else {
                    heightSpec = mDropDownHeight;
                }
            }
            mPopup.setWidth(widthSpec);
            mPopup.setHeight(heightSpec);
            mPopup.setIsClippedToScreen(true);
            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            mPopup.setOutsideTouchable((!mForceIgnoreOutsideTouch) && (!mDropDownAlwaysVisible));
            mPopup.setTouchInterceptor(mTouchInterceptor);
            mPopup.setEpicenterBounds(mEpicenterBounds);
            if (mOverlapAnchorSet) {
                mPopup.setOverlapAnchor(mOverlapAnchor);
            }
            mPopup.showAsDropDown(getAnchorView(), mDropDownHorizontalOffset, mDropDownVerticalOffset, mDropDownGravity);
            mDropDownList.setSelection(android.widget.ListView.INVALID_POSITION);
            mPopup.getContentView().restoreDefaultFocus();
            if ((!mModal) || mDropDownList.isInTouchMode()) {
                clearListSelection();
            }
            if (!mModal) {
                mHandler.post(mHideSelector);
            }
        }
    }

    /**
     * Dismiss the popup window.
     */
    @java.lang.Override
    public void dismiss() {
        mPopup.dismiss();
        removePromptView();
        mPopup.setContentView(null);
        mDropDownList = null;
        mHandler.removeCallbacks(mResizePopupRunnable);
    }

    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param listener
     * 		Listener that will be notified when the popup is dismissed.
     */
    public void setOnDismissListener(@android.annotation.Nullable
    android.widget.PopupWindow.OnDismissListener listener) {
        mPopup.setOnDismissListener(listener);
    }

    private void removePromptView() {
        if (mPromptView != null) {
            final android.view.ViewParent parent = mPromptView.getParent();
            if (parent instanceof android.view.ViewGroup) {
                final android.view.ViewGroup group = ((android.view.ViewGroup) (parent));
                group.removeView(mPromptView);
            }
        }
    }

    /**
     * Control how the popup operates with an input method: one of
     * {@link #INPUT_METHOD_FROM_FOCUSABLE}, {@link #INPUT_METHOD_NEEDED},
     * or {@link #INPUT_METHOD_NOT_NEEDED}.
     *
     * <p>If the popup is showing, calling this method will take effect only
     * the next time the popup is shown or through a manual call to the {@link #show()}
     * method.</p>
     *
     * @see #getInputMethodMode()
     * @see #show()
     */
    public void setInputMethodMode(int mode) {
        mPopup.setInputMethodMode(mode);
    }

    /**
     * Return the current value in {@link #setInputMethodMode(int)}.
     *
     * @see #setInputMethodMode(int)
     */
    public int getInputMethodMode() {
        return mPopup.getInputMethodMode();
    }

    /**
     * Set the selected position of the list.
     * Only valid when {@link #isShowing()} == {@code true}.
     *
     * @param position
     * 		List position to set as selected.
     */
    public void setSelection(int position) {
        android.widget.DropDownListView list = mDropDownList;
        if (isShowing() && (list != null)) {
            list.setListSelectionHidden(false);
            list.setSelection(position);
            if (list.getChoiceMode() != android.widget.ListView.CHOICE_MODE_NONE) {
                list.setItemChecked(position, true);
            }
        }
    }

    /**
     * Clear any current list selection.
     * Only valid when {@link #isShowing()} == {@code true}.
     */
    public void clearListSelection() {
        final android.widget.DropDownListView list = mDropDownList;
        if (list != null) {
            // WARNING: Please read the comment where mListSelectionHidden is declared
            list.setListSelectionHidden(true);
            list.hideSelector();
            list.requestLayout();
        }
    }

    /**
     *
     *
     * @return {@code true} if the popup is currently showing, {@code false} otherwise.
     */
    @java.lang.Override
    public boolean isShowing() {
        return mPopup.isShowing();
    }

    /**
     *
     *
     * @return {@code true} if this popup is configured to assume the user does not need
    to interact with the IME while it is showing, {@code false} otherwise.
     */
    public boolean isInputMethodNotNeeded() {
        return mPopup.getInputMethodMode() == android.widget.ListPopupWindow.INPUT_METHOD_NOT_NEEDED;
    }

    /**
     * Perform an item click operation on the specified list adapter position.
     *
     * @param position
     * 		Adapter position for performing the click
     * @return true if the click action could be performed, false if not.
    (e.g. if the popup was not showing, this method would return false.)
     */
    public boolean performItemClick(int position) {
        if (isShowing()) {
            if (mItemClickListener != null) {
                final android.widget.DropDownListView list = mDropDownList;
                final android.view.View child = list.getChildAt(position - list.getFirstVisiblePosition());
                final android.widget.ListAdapter adapter = list.getAdapter();
                mItemClickListener.onItemClick(list, child, position, adapter.getItemId(position));
            }
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @return The currently selected item or null if the popup is not showing.
     */
    @android.annotation.Nullable
    public java.lang.Object getSelectedItem() {
        if (!isShowing()) {
            return null;
        }
        return mDropDownList.getSelectedItem();
    }

    /**
     *
     *
     * @return The position of the currently selected item or {@link ListView#INVALID_POSITION}
    if {@link #isShowing()} == {@code false}.
     * @see ListView#getSelectedItemPosition()
     */
    public int getSelectedItemPosition() {
        if (!isShowing()) {
            return android.widget.ListView.INVALID_POSITION;
        }
        return mDropDownList.getSelectedItemPosition();
    }

    /**
     *
     *
     * @return The ID of the currently selected item or {@link ListView#INVALID_ROW_ID}
    if {@link #isShowing()} == {@code false}.
     * @see ListView#getSelectedItemId()
     */
    public long getSelectedItemId() {
        if (!isShowing()) {
            return android.widget.ListView.INVALID_ROW_ID;
        }
        return mDropDownList.getSelectedItemId();
    }

    /**
     *
     *
     * @return The View for the currently selected item or null if
    {@link #isShowing()} == {@code false}.
     * @see ListView#getSelectedView()
     */
    @android.annotation.Nullable
    public android.view.View getSelectedView() {
        if (!isShowing()) {
            return null;
        }
        return mDropDownList.getSelectedView();
    }

    /**
     *
     *
     * @return The {@link ListView} displayed within the popup window.
    Only valid when {@link #isShowing()} == {@code true}.
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.widget.ListView getListView() {
        return mDropDownList;
    }

    @android.annotation.NonNull
    android.widget.DropDownListView createDropDownListView(android.content.Context context, boolean hijackFocus) {
        return new android.widget.DropDownListView(context, hijackFocus);
    }

    /**
     * The maximum number of list items that can be visible and still have
     * the list expand when touched.
     *
     * @param max
     * 		Max number of items that can be visible and still allow the list to expand.
     */
    @android.annotation.UnsupportedAppUsage
    void setListItemExpandMax(int max) {
        mListItemExpandMaximum = max;
    }

    /**
     * Filter key down events. By forwarding key down events to this function,
     * views using non-modal ListPopupWindow can have it handle key selection of items.
     *
     * @param keyCode
     * 		keyCode param passed to the host view's onKeyDown
     * @param event
     * 		event param passed to the host view's onKeyDown
     * @return true if the event was handled, false if it was ignored.
     * @see #setModal(boolean)
     * @see #onKeyUp(int, KeyEvent)
     */
    public boolean onKeyDown(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        // when the drop down is shown, we drive it directly
        if (isShowing()) {
            // the key events are forwarded to the list in the drop down view
            // note that ListView handles space but we don't want that to happen
            // also if selection is not currently in the drop down, then don't
            // let center or enter presses go there since that would cause it
            // to select one of its items
            if ((keyCode != android.view.KeyEvent.KEYCODE_SPACE) && ((mDropDownList.getSelectedItemPosition() >= 0) || (!android.view.KeyEvent.isConfirmKey(keyCode)))) {
                int curIndex = mDropDownList.getSelectedItemPosition();
                boolean consumed;
                final boolean below = !mPopup.isAboveAnchor();
                final android.widget.ListAdapter adapter = mAdapter;
                boolean allEnabled;
                int firstItem = java.lang.Integer.MAX_VALUE;
                int lastItem = java.lang.Integer.MIN_VALUE;
                if (adapter != null) {
                    allEnabled = adapter.areAllItemsEnabled();
                    firstItem = (allEnabled) ? 0 : mDropDownList.lookForSelectablePosition(0, true);
                    lastItem = (allEnabled) ? adapter.getCount() - 1 : mDropDownList.lookForSelectablePosition(adapter.getCount() - 1, false);
                }
                if (((below && (keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP)) && (curIndex <= firstItem)) || (((!below) && (keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN)) && (curIndex >= lastItem))) {
                    // When the selection is at the top, we block the key
                    // event to prevent focus from moving.
                    clearListSelection();
                    mPopup.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NEEDED);
                    show();
                    return true;
                } else {
                    // WARNING: Please read the comment where mListSelectionHidden
                    // is declared
                    mDropDownList.setListSelectionHidden(false);
                }
                consumed = mDropDownList.onKeyDown(keyCode, event);
                if (android.widget.ListPopupWindow.DEBUG)
                    android.util.Log.v(android.widget.ListPopupWindow.TAG, (("Key down: code=" + keyCode) + " list consumed=") + consumed);

                if (consumed) {
                    // If it handled the key event, then the user is
                    // navigating in the list, so we should put it in front.
                    mPopup.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED);
                    // Here's a little trick we need to do to make sure that
                    // the list view is actually showing its focus indicator,
                    // by ensuring it has focus and getting its window out
                    // of touch mode.
                    mDropDownList.requestFocusFromTouch();
                    show();
                    switch (keyCode) {
                        // avoid passing the focus from the text view to the
                        // next component
                        case android.view.KeyEvent.KEYCODE_ENTER :
                        case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                        case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                        case android.view.KeyEvent.KEYCODE_DPAD_UP :
                            return true;
                    }
                } else {
                    if (below && (keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN)) {
                        // when the selection is at the bottom, we block the
                        // event to avoid going to the next focusable widget
                        if (curIndex == lastItem) {
                            return true;
                        }
                    } else
                        if (((!below) && (keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP)) && (curIndex == firstItem)) {
                            return true;
                        }

                }
            }
        }
        return false;
    }

    /**
     * Filter key up events. By forwarding key up events to this function,
     * views using non-modal ListPopupWindow can have it handle key selection of items.
     *
     * @param keyCode
     * 		keyCode param passed to the host view's onKeyUp
     * @param event
     * 		event param passed to the host view's onKeyUp
     * @return true if the event was handled, false if it was ignored.
     * @see #setModal(boolean)
     * @see #onKeyDown(int, KeyEvent)
     */
    public boolean onKeyUp(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        if (isShowing() && (mDropDownList.getSelectedItemPosition() >= 0)) {
            boolean consumed = mDropDownList.onKeyUp(keyCode, event);
            if (consumed && android.view.KeyEvent.isConfirmKey(keyCode)) {
                // if the list accepts the key events and the key event was a click, the text view
                // gets the selected item from the drop down as its content
                dismiss();
            }
            return consumed;
        }
        return false;
    }

    /**
     * Filter pre-IME key events. By forwarding {@link View#onKeyPreIme(int, KeyEvent)}
     * events to this function, views using ListPopupWindow can have it dismiss the popup
     * when the back key is pressed.
     *
     * @param keyCode
     * 		keyCode param passed to the host view's onKeyPreIme
     * @param event
     * 		event param passed to the host view's onKeyPreIme
     * @return true if the event was handled, false if it was ignored.
     * @see #setModal(boolean)
     */
    public boolean onKeyPreIme(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) && isShowing()) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            final android.view.View anchorView = mDropDownAnchorView;
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) {
                android.view.KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else
                if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                    android.view.KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                    if (state != null) {
                        state.handleUpEvent(event);
                    }
                    if (event.isTracking() && (!event.isCanceled())) {
                        dismiss();
                        return true;
                    }
                }

        }
        return false;
    }

    /**
     * Returns an {@link OnTouchListener} that can be added to the source view
     * to implement drag-to-open behavior. Generally, the source view should be
     * the same view that was passed to {@link #setAnchorView}.
     * <p>
     * When the listener is set on a view, touching that view and dragging
     * outside of its bounds will open the popup window. Lifting will select the
     * currently touched list item.
     * <p>
     * Example usage:
     * <pre>
     * ListPopupWindow myPopup = new ListPopupWindow(context);
     * myPopup.setAnchor(myAnchor);
     * OnTouchListener dragListener = myPopup.createDragToOpenListener(myAnchor);
     * myAnchor.setOnTouchListener(dragListener);
     * </pre>
     *
     * @param src
     * 		the view on which the resulting listener will be set
     * @return a touch listener that controls drag-to-open behavior
     */
    public android.view.View.OnTouchListener createDragToOpenListener(android.view.View src) {
        return new android.widget.ForwardingListener(src) {
            @java.lang.Override
            public com.android.internal.view.menu.ShowableListMenu getPopup() {
                return android.widget.ListPopupWindow.this;
            }
        };
    }

    /**
     * <p>Builds the popup window's content and returns the height the popup
     * should have. Returns -1 when the content already exists.</p>
     *
     * @return the content's height or -1 if content already exists
     */
    @android.annotation.UnsupportedAppUsage
    private int buildDropDown() {
        android.view.ViewGroup dropDownView;
        int otherHeights = 0;
        if (mDropDownList == null) {
            android.content.Context context = mContext;
            /**
             * This Runnable exists for the sole purpose of checking if the view layout has got
             * completed and if so call showDropDown to display the drop down. This is used to show
             * the drop down as soon as possible after user opens up the search dialog, without
             * waiting for the normal UI pipeline to do it's job which is slower than this method.
             */
            mShowDropDownRunnable = new java.lang.Runnable() {
                public void run() {
                    // View layout should be all done before displaying the drop down.
                    android.view.View view = getAnchorView();
                    if ((view != null) && (view.getWindowToken() != null)) {
                        show();
                    }
                }
            };
            mDropDownList = createDropDownListView(context, !mModal);
            if (mDropDownListHighlight != null) {
                mDropDownList.setSelector(mDropDownListHighlight);
            }
            mDropDownList.setAdapter(mAdapter);
            mDropDownList.setOnItemClickListener(mItemClickListener);
            mDropDownList.setFocusable(true);
            mDropDownList.setFocusableInTouchMode(true);
            mDropDownList.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                    if (position != (-1)) {
                        android.widget.DropDownListView dropDownList = mDropDownList;
                        if (dropDownList != null) {
                            dropDownList.setListSelectionHidden(false);
                        }
                    }
                }

                public void onNothingSelected(android.widget.AdapterView<?> parent) {
                }
            });
            mDropDownList.setOnScrollListener(mScrollListener);
            if (mItemSelectedListener != null) {
                mDropDownList.setOnItemSelectedListener(mItemSelectedListener);
            }
            dropDownView = mDropDownList;
            android.view.View hintView = mPromptView;
            if (hintView != null) {
                // if a hint has been specified, we accomodate more space for it and
                // add a text view in the drop down menu, at the bottom of the list
                android.widget.LinearLayout hintContainer = new android.widget.LinearLayout(context);
                hintContainer.setOrientation(android.widget.LinearLayout.VERTICAL);
                android.widget.LinearLayout.LayoutParams hintParams = new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0F);
                switch (mPromptPosition) {
                    case android.widget.ListPopupWindow.POSITION_PROMPT_BELOW :
                        hintContainer.addView(dropDownView, hintParams);
                        hintContainer.addView(hintView);
                        break;
                    case android.widget.ListPopupWindow.POSITION_PROMPT_ABOVE :
                        hintContainer.addView(hintView);
                        hintContainer.addView(dropDownView, hintParams);
                        break;
                    default :
                        android.util.Log.e(android.widget.ListPopupWindow.TAG, "Invalid hint position " + mPromptPosition);
                        break;
                }
                // Measure the hint's height to find how much more vertical
                // space we need to add to the drop down's height.
                final int widthSize;
                final int widthMode;
                if (mDropDownWidth >= 0) {
                    widthMode = android.view.View.MeasureSpec.AT_MOST;
                    widthSize = mDropDownWidth;
                } else {
                    widthMode = android.view.View.MeasureSpec.UNSPECIFIED;
                    widthSize = 0;
                }
                final int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                final int heightSpec = android.view.View.MeasureSpec.UNSPECIFIED;
                hintView.measure(widthSpec, heightSpec);
                hintParams = ((android.widget.LinearLayout.LayoutParams) (hintView.getLayoutParams()));
                otherHeights = (hintView.getMeasuredHeight() + hintParams.topMargin) + hintParams.bottomMargin;
                dropDownView = hintContainer;
            }
            mPopup.setContentView(dropDownView);
        } else {
            final android.view.View view = mPromptView;
            if (view != null) {
                android.widget.LinearLayout.LayoutParams hintParams = ((android.widget.LinearLayout.LayoutParams) (view.getLayoutParams()));
                otherHeights = (view.getMeasuredHeight() + hintParams.topMargin) + hintParams.bottomMargin;
            }
        }
        // getMaxAvailableHeight() subtracts the padding, so we put it back
        // to get the available height for the whole window.
        final int padding;
        final android.graphics.drawable.Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            padding = mTempRect.top + mTempRect.bottom;
            // If we don't have an explicit vertical offset, determine one from
            // the window background so that content will line up.
            if (!mDropDownVerticalOffsetSet) {
                mDropDownVerticalOffset = -mTempRect.top;
            }
        } else {
            mTempRect.setEmpty();
            padding = 0;
        }
        // Max height available on the screen for a popup.
        final boolean ignoreBottomDecorations = mPopup.getInputMethodMode() == android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED;
        final int maxHeight = mPopup.getMaxAvailableHeight(getAnchorView(), mDropDownVerticalOffset, ignoreBottomDecorations);
        if (mDropDownAlwaysVisible || (mDropDownHeight == android.view.ViewGroup.LayoutParams.MATCH_PARENT)) {
            return maxHeight + padding;
        }
        final int childWidthSpec;
        switch (mDropDownWidth) {
            case android.view.ViewGroup.LayoutParams.WRAP_CONTENT :
                childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(mContext.getResources().getDisplayMetrics().widthPixels - (mTempRect.left + mTempRect.right), android.view.View.MeasureSpec.AT_MOST);
                break;
            case android.view.ViewGroup.LayoutParams.MATCH_PARENT :
                childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(mContext.getResources().getDisplayMetrics().widthPixels - (mTempRect.left + mTempRect.right), android.view.View.MeasureSpec.EXACTLY);
                break;
            default :
                childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(mDropDownWidth, android.view.View.MeasureSpec.EXACTLY);
                break;
        }
        // Add padding only if the list has items in it, that way we don't show
        // the popup if it is not needed.
        final int listContent = mDropDownList.measureHeightOfChildren(childWidthSpec, 0, android.widget.DropDownListView.NO_POSITION, maxHeight - otherHeights, -1);
        if (listContent > 0) {
            final int listPadding = mDropDownList.getPaddingTop() + mDropDownList.getPaddingBottom();
            otherHeights += padding + listPadding;
        }
        return listContent + otherHeights;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setOverlapAnchor(boolean overlap) {
        mOverlapAnchorSet = true;
        mOverlapAnchor = overlap;
    }

    private class PopupDataSetObserver extends android.database.DataSetObserver {
        @java.lang.Override
        public void onChanged() {
            if (isShowing()) {
                // Resize the popup to fit new content
                show();
            }
        }

        @java.lang.Override
        public void onInvalidated() {
            dismiss();
        }
    }

    private class ListSelectorHider implements java.lang.Runnable {
        public void run() {
            clearListSelection();
        }
    }

    private class ResizePopupRunnable implements java.lang.Runnable {
        public void run() {
            if ((((mDropDownList != null) && mDropDownList.isAttachedToWindow()) && (mDropDownList.getCount() > mDropDownList.getChildCount())) && (mDropDownList.getChildCount() <= mListItemExpandMaximum)) {
                mPopup.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED);
                show();
            }
        }
    }

    private class PopupTouchInterceptor implements android.view.View.OnTouchListener {
        public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
            final int action = event.getAction();
            final int x = ((int) (event.getX()));
            final int y = ((int) (event.getY()));
            if ((((action == android.view.MotionEvent.ACTION_DOWN) && (mPopup != null)) && mPopup.isShowing()) && ((((x >= 0) && (x < mPopup.getWidth())) && (y >= 0)) && (y < mPopup.getHeight()))) {
                mHandler.postDelayed(mResizePopupRunnable, android.widget.ListPopupWindow.EXPAND_LIST_TIMEOUT);
            } else
                if (action == android.view.MotionEvent.ACTION_UP) {
                    mHandler.removeCallbacks(mResizePopupRunnable);
                }

            return false;
        }
    }

    private class PopupScrollListener implements android.widget.AbsListView.OnScrollListener {
        public void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }

        public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
            if (((scrollState == android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) && (!isInputMethodNotNeeded())) && (mPopup.getContentView() != null)) {
                mHandler.removeCallbacks(mResizePopupRunnable);
                mResizePopupRunnable.run();
            }
        }
    }
}

