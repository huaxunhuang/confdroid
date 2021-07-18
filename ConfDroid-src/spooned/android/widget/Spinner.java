/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A view that displays one child at a time and lets the user pick among them.
 * The items in the Spinner come from the {@link Adapter} associated with
 * this view.
 *
 * <p>See the <a href="{@docRoot }guide/topics/ui/controls/spinner.html">Spinners</a> guide.</p>
 *
 * @unknown ref android.R.styleable#Spinner_dropDownSelector
 * @unknown ref android.R.styleable#Spinner_dropDownWidth
 * @unknown ref android.R.styleable#Spinner_gravity
 * @unknown ref android.R.styleable#Spinner_popupBackground
 * @unknown ref android.R.styleable#Spinner_prompt
 * @unknown ref android.R.styleable#Spinner_spinnerMode
 * @unknown ref android.R.styleable#ListPopupWindow_dropDownVerticalOffset
 * @unknown ref android.R.styleable#ListPopupWindow_dropDownHorizontalOffset
 */
@android.annotation.Widget
public class Spinner extends android.widget.AbsSpinner implements android.content.DialogInterface.OnClickListener {
    private static final java.lang.String TAG = "Spinner";

    // Only measure this many items to get a decent max width.
    private static final int MAX_ITEMS_MEASURED = 15;

    /**
     * Use a dialog window for selecting spinner options.
     */
    public static final int MODE_DIALOG = 0;

    /**
     * Use a dropdown anchored to the Spinner for selecting spinner options.
     */
    public static final int MODE_DROPDOWN = 1;

    /**
     * Use the theme-supplied value to select the dropdown mode.
     */
    private static final int MODE_THEME = -1;

    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    /**
     * Context used to inflate the popup window or dialog.
     */
    private final android.content.Context mPopupContext;

    /**
     * Forwarding listener used to implement drag-to-open.
     */
    @android.annotation.UnsupportedAppUsage
    private android.widget.ForwardingListener mForwardingListener;

    /**
     * Temporary holder for setAdapter() calls from the super constructor.
     */
    private android.widget.SpinnerAdapter mTempAdapter;

    @android.annotation.UnsupportedAppUsage
    private android.widget.Spinner.SpinnerPopup mPopup;

    int mDropDownWidth;

    private int mGravity;

    private boolean mDisableChildrenWhenDisabled;

    /**
     * Constructs a new spinner with the given context's theme.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     */
    public Spinner(android.content.Context context) {
        this(context, null);
    }

    /**
     * Constructs a new spinner with the given context's theme and the supplied
     * mode of displaying choices. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN}.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param mode
     * 		Constant describing how the user will select choices from
     * 		the spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public Spinner(android.content.Context context, int mode) {
        this(context, null, com.android.internal.R.attr.spinnerStyle, mode);
    }

    /**
     * Constructs a new spinner with the given context's theme and the supplied
     * attribute set.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     */
    public Spinner(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.spinnerStyle);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, and default style attribute.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default
     * 		values for the view. Can be 0 to not look for
     * 		defaults.
     */
    public Spinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0, android.widget.Spinner.MODE_THEME);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, and default style attribute. <code>mode</code> may be one
     * of {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN} and determines how the
     * user will select choices from the spinner.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default
     * 		values for the view. Can be 0 to not look for defaults.
     * @param mode
     * 		Constant describing how the user will select choices from the
     * 		spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public Spinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, 0, mode);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, and default styles. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN} and determines how the
     * user will select choices from the spinner.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default
     * 		values for the view. Can be 0 to not look for
     * 		defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme.
     * 		Can be 0 to not look for defaults.
     * @param mode
     * 		Constant describing how the user will select choices from
     * 		the spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public Spinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        this(context, attrs, defStyleAttr, defStyleRes, mode, null);
    }

    /**
     * Constructs a new spinner with the given context, the supplied attribute
     * set, default styles, popup mode (one of {@link #MODE_DIALOG} or
     * {@link #MODE_DROPDOWN}), and the theme against which the popup should be
     * inflated.
     *
     * @param context
     * 		The context against which the view is inflated, which
     * 		provides access to the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default
     * 		values for the view. Can be 0 to not look for
     * 		defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme.
     * 		Can be 0 to not look for defaults.
     * @param mode
     * 		Constant describing how the user will select choices from
     * 		the spinner.
     * @param popupTheme
     * 		The theme against which the dialog or dropdown popup
     * 		should be inflated. May be {@code null} to use the
     * 		view theme. If set, this will override any value
     * 		specified by
     * 		{@link android.R.styleable#Spinner_popupTheme}.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public Spinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, android.content.res.Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Spinner, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.Spinner, attrs, a, defStyleAttr, defStyleRes);
        if (popupTheme != null) {
            mPopupContext = new android.view.ContextThemeWrapper(context, popupTheme);
        } else {
            final int popupThemeResId = a.getResourceId(R.styleable.Spinner_popupTheme, 0);
            if (popupThemeResId != 0) {
                mPopupContext = new android.view.ContextThemeWrapper(context, popupThemeResId);
            } else {
                mPopupContext = context;
            }
        }
        if (mode == android.widget.Spinner.MODE_THEME) {
            mode = a.getInt(R.styleable.Spinner_spinnerMode, android.widget.Spinner.MODE_DIALOG);
        }
        switch (mode) {
            case android.widget.Spinner.MODE_DIALOG :
                {
                    mPopup = new android.widget.Spinner.DialogPopup();
                    mPopup.setPromptText(a.getString(R.styleable.Spinner_prompt));
                    break;
                }
            case android.widget.Spinner.MODE_DROPDOWN :
                {
                    final android.widget.Spinner.DropdownPopup popup = new android.widget.Spinner.DropdownPopup(mPopupContext, attrs, defStyleAttr, defStyleRes);
                    final android.content.res.TypedArray pa = mPopupContext.obtainStyledAttributes(attrs, R.styleable.Spinner, defStyleAttr, defStyleRes);
                    mDropDownWidth = pa.getLayoutDimension(R.styleable.Spinner_dropDownWidth, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (pa.hasValueOrEmpty(R.styleable.Spinner_dropDownSelector)) {
                        popup.setListSelector(pa.getDrawable(R.styleable.Spinner_dropDownSelector));
                    }
                    popup.setBackgroundDrawable(pa.getDrawable(R.styleable.Spinner_popupBackground));
                    popup.setPromptText(a.getString(R.styleable.Spinner_prompt));
                    pa.recycle();
                    mPopup = popup;
                    mForwardingListener = new android.widget.ForwardingListener(this) {
                        @java.lang.Override
                        public com.android.internal.view.menu.ShowableListMenu getPopup() {
                            return popup;
                        }

                        @java.lang.Override
                        public boolean onForwardingStarted() {
                            if (!mPopup.isShowing()) {
                                mPopup.show(getTextDirection(), getTextAlignment());
                            }
                            return true;
                        }
                    };
                    break;
                }
        }
        mGravity = a.getInt(R.styleable.Spinner_gravity, android.view.Gravity.CENTER);
        mDisableChildrenWhenDisabled = a.getBoolean(R.styleable.Spinner_disableChildrenWhenDisabled, false);
        a.recycle();
        // Base constructor can call setAdapter before we initialize mPopup.
        // Finish setting things up if this happened.
        if (mTempAdapter != null) {
            setAdapter(mTempAdapter);
            mTempAdapter = null;
        }
    }

    /**
     *
     *
     * @return the context used to inflate the Spinner's popup or dialog window
     */
    public android.content.Context getPopupContext() {
        return mPopupContext;
    }

    /**
     * Set the background drawable for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other modes.
     *
     * @param background
     * 		Background drawable
     * @unknown ref android.R.styleable#Spinner_popupBackground
     */
    public void setPopupBackgroundDrawable(android.graphics.drawable.Drawable background) {
        if (!(mPopup instanceof android.widget.Spinner.DropdownPopup)) {
            android.util.Log.e(android.widget.Spinner.TAG, "setPopupBackgroundDrawable: incompatible spinner mode; ignoring...");
            return;
        }
        mPopup.setBackgroundDrawable(background);
    }

    /**
     * Set the background drawable for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other modes.
     *
     * @param resId
     * 		Resource ID of a background drawable
     * @unknown ref android.R.styleable#Spinner_popupBackground
     */
    public void setPopupBackgroundResource(@android.annotation.DrawableRes
    int resId) {
        setPopupBackgroundDrawable(getPopupContext().getDrawable(resId));
    }

    /**
     * Get the background drawable for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; other modes will return null.
     *
     * @return background Background drawable
     * @unknown ref android.R.styleable#Spinner_popupBackground
     */
    @android.view.inspector.InspectableProperty
    public android.graphics.drawable.Drawable getPopupBackground() {
        return mPopup.getBackground();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public boolean isPopupShowing() {
        return (mPopup != null) && mPopup.isShowing();
    }

    /**
     * Set a vertical offset in pixels for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other modes.
     *
     * @param pixels
     * 		Vertical offset in pixels
     * @unknown ref android.R.styleable#ListPopupWindow_dropDownVerticalOffset
     */
    public void setDropDownVerticalOffset(int pixels) {
        mPopup.setVerticalOffset(pixels);
    }

    /**
     * Get the configured vertical offset in pixels for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; other modes will return 0.
     *
     * @return Vertical offset in pixels
     * @unknown ref android.R.styleable#ListPopupWindow_dropDownVerticalOffset
     */
    @android.view.inspector.InspectableProperty
    public int getDropDownVerticalOffset() {
        return mPopup.getVerticalOffset();
    }

    /**
     * Set a horizontal offset in pixels for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other modes.
     *
     * @param pixels
     * 		Horizontal offset in pixels
     * @unknown ref android.R.styleable#ListPopupWindow_dropDownHorizontalOffset
     */
    public void setDropDownHorizontalOffset(int pixels) {
        mPopup.setHorizontalOffset(pixels);
    }

    /**
     * Get the configured horizontal offset in pixels for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; other modes will return 0.
     *
     * @return Horizontal offset in pixels
     * @unknown ref android.R.styleable#ListPopupWindow_dropDownHorizontalOffset
     */
    @android.view.inspector.InspectableProperty
    public int getDropDownHorizontalOffset() {
        return mPopup.getHorizontalOffset();
    }

    /**
     * Set the width of the spinner's popup window of choices in pixels. This value
     * may also be set to {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
     * to match the width of the Spinner itself, or
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} to wrap to the measured size
     * of contained dropdown list items.
     *
     * <p>Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other modes.</p>
     *
     * @param pixels
     * 		Width in pixels, WRAP_CONTENT, or MATCH_PARENT
     * @unknown ref android.R.styleable#Spinner_dropDownWidth
     */
    public void setDropDownWidth(int pixels) {
        if (!(mPopup instanceof android.widget.Spinner.DropdownPopup)) {
            android.util.Log.e(android.widget.Spinner.TAG, "Cannot set dropdown width for MODE_DIALOG, ignoring");
            return;
        }
        mDropDownWidth = pixels;
    }

    /**
     * Get the configured width of the spinner's popup window of choices in pixels.
     * The returned value may also be {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
     * meaning the popup window will match the width of the Spinner itself, or
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} to wrap to the measured size
     * of contained dropdown list items.
     *
     * @return Width in pixels, WRAP_CONTENT, or MATCH_PARENT
     * @unknown ref android.R.styleable#Spinner_dropDownWidth
     */
    @android.view.inspector.InspectableProperty
    public int getDropDownWidth() {
        return mDropDownWidth;
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mDisableChildrenWhenDisabled) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setEnabled(enabled);
            }
        }
    }

    /**
     * Describes how the selected item view is positioned. Currently only the horizontal component
     * is used. The default is determined by the current theme.
     *
     * @param gravity
     * 		See {@link android.view.Gravity}
     * @unknown ref android.R.styleable#Spinner_gravity
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= android.view.Gravity.START;
            }
            mGravity = gravity;
            requestLayout();
        }
    }

    /**
     * Describes how the selected item view is positioned. The default is determined by the
     * current theme.
     *
     * @return A {@link android.view.Gravity Gravity} value
     */
    @android.view.inspector.InspectableProperty(valueType = android.view.inspector.InspectableProperty.ValueType.GRAVITY)
    public int getGravity() {
        return mGravity;
    }

    /**
     * Sets the {@link SpinnerAdapter} used to provide the data which backs
     * this Spinner.
     * <p>
     * If this Spinner has a popup theme set in XML via the
     * {@link android.R.styleable#Spinner_popupTheme popupTheme} attribute, the
     * adapter should inflate drop-down views using the same theme. The easiest
     * way to achieve this is by using {@link #getPopupContext()} to obtain a
     * layout inflater for use in
     * {@link SpinnerAdapter#getDropDownView(int, View, ViewGroup)}.
     * <p>
     * Spinner overrides {@link Adapter#getViewTypeCount()} on the
     * Adapter associated with this view. Calling
     * {@link Adapter#getItemViewType(int) getItemViewType(int)} on the object
     * returned from {@link #getAdapter()} will always return 0. Calling
     * {@link Adapter#getViewTypeCount() getViewTypeCount()} will always return
     * 1. On API {@link Build.VERSION_CODES#LOLLIPOP} and above, attempting to set an
     * adapter with more than one view type will throw an
     * {@link IllegalArgumentException}.
     *
     * @param adapter
     * 		the adapter to set
     * @see AbsSpinner#setAdapter(SpinnerAdapter)
     * @throws IllegalArgumentException
     * 		if the adapter has more than one view
     * 		type
     */
    @java.lang.Override
    public void setAdapter(android.widget.SpinnerAdapter adapter) {
        // The super constructor may call setAdapter before we're prepared.
        // Postpone doing anything until we've finished construction.
        if (mPopup == null) {
            mTempAdapter = adapter;
            return;
        }
        super.setAdapter(adapter);
        mRecycler.clear();
        final int targetSdkVersion = mContext.getApplicationInfo().targetSdkVersion;
        if (((targetSdkVersion >= Build.VERSION_CODES.LOLLIPOP) && (adapter != null)) && (adapter.getViewTypeCount() != 1)) {
            throw new java.lang.IllegalArgumentException("Spinner adapter view type count must be 1");
        }
        final android.content.Context popupContext = (mPopupContext == null) ? mContext : mPopupContext;
        mPopup.setAdapter(new android.widget.Spinner.DropDownAdapter(adapter, popupContext.getTheme()));
    }

    @java.lang.Override
    public int getBaseline() {
        android.view.View child = null;
        if (getChildCount() > 0) {
            child = getChildAt(0);
        } else
            if ((mAdapter != null) && (mAdapter.getCount() > 0)) {
                child = makeView(0, false);
                mRecycler.put(0, child);
            }

        if (child != null) {
            final int childBaseline = child.getBaseline();
            return childBaseline >= 0 ? child.getTop() + childBaseline : -1;
        } else {
            return -1;
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if ((mPopup != null) && mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    /**
     * <p>A spinner does not support item click events. Calling this method
     * will raise an exception.</p>
     * <p>Instead use {@link AdapterView#setOnItemSelectedListener}.
     *
     * @param l
     * 		this listener will be ignored
     */
    @java.lang.Override
    public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener l) {
        throw new java.lang.RuntimeException("setOnItemClickListener cannot be used with a spinner.");
    }

    /**
     *
     *
     * @unknown internal use only
     */
    @android.annotation.UnsupportedAppUsage
    public void setOnItemClickListenerInt(android.widget.AdapterView.OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if ((mForwardingListener != null) && mForwardingListener.onTouch(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ((mPopup != null) && (android.view.View.MeasureSpec.getMode(widthMeasureSpec) == android.view.View.MeasureSpec.AT_MOST)) {
            final int measuredWidth = getMeasuredWidth();
            setMeasuredDimension(java.lang.Math.min(java.lang.Math.max(measuredWidth, measureContentWidth(getAdapter(), getBackground())), android.view.View.MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
        }
    }

    /**
     *
     *
     * @see android.view.View#onLayout(boolean,int,int,int,int)

    Creates and positions all views
     */
    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mInLayout = true;
        layout(0, false);
        mInLayout = false;
    }

    /**
     * Creates and positions all views for this Spinner.
     *
     * @param delta
     * 		Change in the selected position. +1 means selection is moving to the right,
     * 		so views are scrolling to the left. -1 means selection is moving to the left.
     */
    @java.lang.Override
    void layout(int delta, boolean animate) {
        int childrenLeft = mSpinnerPadding.left;
        int childrenWidth = ((mRight - mLeft) - mSpinnerPadding.left) - mSpinnerPadding.right;
        if (mDataChanged) {
            handleDataChanged();
        }
        // Handle the empty set by removing all views
        if (mItemCount == 0) {
            resetList();
            return;
        }
        if (mNextSelectedPosition >= 0) {
            setSelectedPositionInt(mNextSelectedPosition);
        }
        recycleAllViews();
        // Clear out old views
        removeAllViewsInLayout();
        // Make selected view and position it
        mFirstPosition = mSelectedPosition;
        if (mAdapter != null) {
            android.view.View sel = makeView(mSelectedPosition, true);
            int width = sel.getMeasuredWidth();
            int selectedOffset = childrenLeft;
            final int layoutDirection = getLayoutDirection();
            final int absoluteGravity = android.view.Gravity.getAbsoluteGravity(mGravity, layoutDirection);
            switch (absoluteGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
                case android.view.Gravity.CENTER_HORIZONTAL :
                    selectedOffset = (childrenLeft + (childrenWidth / 2)) - (width / 2);
                    break;
                case android.view.Gravity.RIGHT :
                    selectedOffset = (childrenLeft + childrenWidth) - width;
                    break;
            }
            sel.offsetLeftAndRight(selectedOffset);
        }
        // Flush any cached views that did not get reused above
        mRecycler.clear();
        invalidate();
        checkSelectionChanged();
        mDataChanged = false;
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
    }

    /**
     * Obtain a view, either by pulling an existing view from the recycler or
     * by getting a new one from the adapter. If we are animating, make sure
     * there is enough information in the view's layout parameters to animate
     * from the old to new positions.
     *
     * @param position
     * 		Position in the spinner for the view to obtain
     * @param addChild
     * 		true to add the child to the spinner, false to obtain and configure only.
     * @return A view for the given position
     */
    private android.view.View makeView(int position, boolean addChild) {
        android.view.View child;
        if (!mDataChanged) {
            child = mRecycler.get(position);
            if (child != null) {
                // Position the view
                setUpChild(child, addChild);
                return child;
            }
        }
        // Nothing found in the recycler -- ask the adapter for a view
        child = mAdapter.getView(position, null, this);
        // Position the view
        setUpChild(child, addChild);
        return child;
    }

    /**
     * Helper for makeAndAddView to set the position of a view
     * and fill out its layout paramters.
     *
     * @param child
     * 		The view to position
     * @param addChild
     * 		true if the child should be added to the Spinner during setup
     */
    private void setUpChild(android.view.View child, boolean addChild) {
        // Respect layout params that are already in the view. Otherwise
        // make some up...
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = generateDefaultLayoutParams();
        }
        addViewInLayout(child, 0, lp);
        child.setSelected(hasFocus());
        if (mDisableChildrenWhenDisabled) {
            child.setEnabled(isEnabled());
        }
        // Get measure specs
        int childHeightSpec = android.view.ViewGroup.getChildMeasureSpec(mHeightMeasureSpec, mSpinnerPadding.top + mSpinnerPadding.bottom, lp.height);
        int childWidthSpec = android.view.ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mSpinnerPadding.left + mSpinnerPadding.right, lp.width);
        // Measure child
        child.measure(childWidthSpec, childHeightSpec);
        int childLeft;
        int childRight;
        // Position vertically based on gravity setting
        int childTop = mSpinnerPadding.top + ((((getMeasuredHeight() - mSpinnerPadding.bottom) - mSpinnerPadding.top) - child.getMeasuredHeight()) / 2);
        int childBottom = childTop + child.getMeasuredHeight();
        int width = child.getMeasuredWidth();
        childLeft = 0;
        childRight = childLeft + width;
        child.layout(childLeft, childTop, childRight, childBottom);
        if (!addChild) {
            removeViewInLayout(child);
        }
    }

    @java.lang.Override
    public boolean performClick() {
        boolean handled = super.performClick();
        if (!handled) {
            handled = true;
            if (!mPopup.isShowing()) {
                mPopup.show(getTextDirection(), getTextAlignment());
            }
        }
        return handled;
    }

    @java.lang.Override
    public void onClick(android.content.DialogInterface dialog, int which) {
        setSelection(which);
        dialog.dismiss();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.Spinner.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (mAdapter != null) {
            info.setCanOpenPopup(true);
        }
    }

    /**
     * Sets the prompt to display when the dialog is shown.
     *
     * @param prompt
     * 		the prompt to set
     */
    public void setPrompt(java.lang.CharSequence prompt) {
        mPopup.setPromptText(prompt);
    }

    /**
     * Sets the prompt to display when the dialog is shown.
     *
     * @param promptId
     * 		the resource ID of the prompt to display when the dialog is shown
     */
    public void setPromptId(int promptId) {
        setPrompt(getContext().getText(promptId));
    }

    /**
     *
     *
     * @return The prompt to display when the dialog is shown
     */
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getPrompt() {
        return mPopup.getHintText();
    }

    int measureContentWidth(android.widget.SpinnerAdapter adapter, android.graphics.drawable.Drawable background) {
        if (adapter == null) {
            return 0;
        }
        int width = 0;
        android.view.View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec = android.view.View.MeasureSpec.makeSafeMeasureSpec(getMeasuredWidth(), android.view.View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = android.view.View.MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), android.view.View.MeasureSpec.UNSPECIFIED);
        // Make sure the number of items we'll measure is capped. If it's a huge data set
        // with wildly varying sizes, oh well.
        int start = java.lang.Math.max(0, getSelectedItemPosition());
        final int end = java.lang.Math.min(adapter.getCount(), start + android.widget.Spinner.MAX_ITEMS_MEASURED);
        final int count = end - start;
        start = java.lang.Math.max(0, start - (android.widget.Spinner.MAX_ITEMS_MEASURED - count));
        for (int i = start; i < end; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            itemView = adapter.getView(i, itemView, this);
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = java.lang.Math.max(width, itemView.getMeasuredWidth());
        }
        // Add background padding to measured width
        if (background != null) {
            background.getPadding(mTempRect);
            width += mTempRect.left + mTempRect.right;
        }
        return width;
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        final android.widget.Spinner.SavedState ss = new android.widget.Spinner.SavedState(super.onSaveInstanceState());
        ss.showDropdown = (mPopup != null) && mPopup.isShowing();
        return ss;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.Spinner.SavedState ss = ((android.widget.Spinner.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.showDropdown) {
            android.view.ViewTreeObserver vto = getViewTreeObserver();
            if (vto != null) {
                final android.view.ViewTreeObserver.OnGlobalLayoutListener listener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                    @java.lang.Override
                    public void onGlobalLayout() {
                        if (!mPopup.isShowing()) {
                            mPopup.show(getTextDirection(), getTextAlignment());
                        }
                        final android.view.ViewTreeObserver vto = getViewTreeObserver();
                        if (vto != null) {
                            vto.removeOnGlobalLayoutListener(this);
                        }
                    }
                };
                vto.addOnGlobalLayoutListener(listener);
            }
        }
    }

    @java.lang.Override
    public android.view.PointerIcon onResolvePointerIcon(android.view.MotionEvent event, int pointerIndex) {
        if (((getPointerIcon() == null) && isClickable()) && isEnabled()) {
            return android.view.PointerIcon.getSystemIcon(getContext(), android.view.PointerIcon.TYPE_HAND);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    static class SavedState extends android.widget.AbsSpinner.SavedState {
        boolean showDropdown;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        private SavedState(android.os.Parcel in) {
            super(in);
            showDropdown = in.readByte() != 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(((byte) (showDropdown ? 1 : 0)));
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.Spinner.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.Spinner.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    /**
     * <p>Wrapper class for an Adapter. Transforms the embedded Adapter instance
     * into a ListAdapter.</p>
     */
    private static class DropDownAdapter implements android.widget.ListAdapter , android.widget.SpinnerAdapter {
        private android.widget.SpinnerAdapter mAdapter;

        private android.widget.ListAdapter mListAdapter;

        /**
         * Creates a new ListAdapter wrapper for the specified adapter.
         *
         * @param adapter
         * 		the SpinnerAdapter to transform into a ListAdapter
         * @param dropDownTheme
         * 		the theme against which to inflate drop-down
         * 		views, may be {@null } to use default theme
         */
        public DropDownAdapter(@android.annotation.Nullable
        android.widget.SpinnerAdapter adapter, @android.annotation.Nullable
        android.content.res.Resources.Theme dropDownTheme) {
            mAdapter = adapter;
            if (adapter instanceof android.widget.ListAdapter) {
                mListAdapter = ((android.widget.ListAdapter) (adapter));
            }
            if ((dropDownTheme != null) && (adapter instanceof android.widget.ThemedSpinnerAdapter)) {
                final android.widget.ThemedSpinnerAdapter themedAdapter = ((android.widget.ThemedSpinnerAdapter) (adapter));
                if (themedAdapter.getDropDownViewTheme() == null) {
                    themedAdapter.setDropDownViewTheme(dropDownTheme);
                }
            }
        }

        public int getCount() {
            return mAdapter == null ? 0 : mAdapter.getCount();
        }

        public java.lang.Object getItem(int position) {
            return mAdapter == null ? null : mAdapter.getItem(position);
        }

        public long getItemId(int position) {
            return mAdapter == null ? -1 : mAdapter.getItemId(position);
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        public android.view.View getDropDownView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            return mAdapter == null ? null : mAdapter.getDropDownView(position, convertView, parent);
        }

        public boolean hasStableIds() {
            return (mAdapter != null) && mAdapter.hasStableIds();
        }

        public void registerDataSetObserver(android.database.DataSetObserver observer) {
            if (mAdapter != null) {
                mAdapter.registerDataSetObserver(observer);
            }
        }

        public void unregisterDataSetObserver(android.database.DataSetObserver observer) {
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(observer);
            }
        }

        /**
         * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this call.
         * Otherwise, return true.
         */
        public boolean areAllItemsEnabled() {
            final android.widget.ListAdapter adapter = mListAdapter;
            if (adapter != null) {
                return adapter.areAllItemsEnabled();
            } else {
                return true;
            }
        }

        /**
         * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this call.
         * Otherwise, return true.
         */
        public boolean isEnabled(int position) {
            final android.widget.ListAdapter adapter = mListAdapter;
            if (adapter != null) {
                return adapter.isEnabled(position);
            } else {
                return true;
            }
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }
    }

    /**
     * Implements some sort of popup selection interface for selecting a spinner option.
     * Allows for different spinner modes.
     */
    private interface SpinnerPopup {
        public void setAdapter(android.widget.ListAdapter adapter);

        /**
         * Show the popup
         */
        public void show(int textDirection, int textAlignment);

        /**
         * Dismiss the popup
         */
        public void dismiss();

        /**
         *
         *
         * @return true if the popup is showing, false otherwise.
         */
        @android.annotation.UnsupportedAppUsage
        public boolean isShowing();

        /**
         * Set hint text to be displayed to the user. This should provide
         * a description of the choice being made.
         *
         * @param hintText
         * 		Hint text to set.
         */
        public void setPromptText(java.lang.CharSequence hintText);

        public java.lang.CharSequence getHintText();

        public void setBackgroundDrawable(android.graphics.drawable.Drawable bg);

        public void setVerticalOffset(int px);

        public void setHorizontalOffset(int px);

        public android.graphics.drawable.Drawable getBackground();

        public int getVerticalOffset();

        public int getHorizontalOffset();
    }

    private class DialogPopup implements android.content.DialogInterface.OnClickListener , android.widget.Spinner.SpinnerPopup {
        private android.app.AlertDialog mPopup;

        private android.widget.ListAdapter mListAdapter;

        private java.lang.CharSequence mPrompt;

        public void dismiss() {
            if (mPopup != null) {
                mPopup.dismiss();
                mPopup = null;
            }
        }

        @android.annotation.UnsupportedAppUsage
        public boolean isShowing() {
            return mPopup != null ? mPopup.isShowing() : false;
        }

        public void setAdapter(android.widget.ListAdapter adapter) {
            mListAdapter = adapter;
        }

        public void setPromptText(java.lang.CharSequence hintText) {
            mPrompt = hintText;
        }

        public java.lang.CharSequence getHintText() {
            return mPrompt;
        }

        public void show(int textDirection, int textAlignment) {
            if (mListAdapter == null) {
                return;
            }
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getPopupContext());
            if (mPrompt != null) {
                builder.setTitle(mPrompt);
            }
            mPopup = builder.setSingleChoiceItems(mListAdapter, getSelectedItemPosition(), this).create();
            final android.widget.ListView listView = mPopup.getListView();
            listView.setTextDirection(textDirection);
            listView.setTextAlignment(textAlignment);
            mPopup.show();
        }

        public void onClick(android.content.DialogInterface dialog, int which) {
            setSelection(which);
            if (mOnItemClickListener != null) {
                performItemClick(null, which, mListAdapter.getItemId(which));
            }
            dismiss();
        }

        @java.lang.Override
        public void setBackgroundDrawable(android.graphics.drawable.Drawable bg) {
            android.util.Log.e(android.widget.Spinner.TAG, "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        @java.lang.Override
        public void setVerticalOffset(int px) {
            android.util.Log.e(android.widget.Spinner.TAG, "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        @java.lang.Override
        public void setHorizontalOffset(int px) {
            android.util.Log.e(android.widget.Spinner.TAG, "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getBackground() {
            return null;
        }

        @java.lang.Override
        public int getVerticalOffset() {
            return 0;
        }

        @java.lang.Override
        public int getHorizontalOffset() {
            return 0;
        }
    }

    private class DropdownPopup extends android.widget.ListPopupWindow implements android.widget.Spinner.SpinnerPopup {
        private java.lang.CharSequence mHintText;

        private android.widget.ListAdapter mAdapter;

        public DropdownPopup(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            setAnchorView(android.widget.Spinner.this);
            setModal(true);
            setPromptPosition(android.widget.ListPopupWindow.POSITION_PROMPT_ABOVE);
            setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                public void onItemClick(android.widget.AdapterView parent, android.view.View v, int position, long id) {
                    android.widget.Spinner.this.setSelection(position);
                    if (mOnItemClickListener != null) {
                        android.widget.Spinner.this.performItemClick(v, position, mAdapter.getItemId(position));
                    }
                    dismiss();
                }
            });
        }

        @java.lang.Override
        public void setAdapter(android.widget.ListAdapter adapter) {
            super.setAdapter(adapter);
            mAdapter = adapter;
        }

        public java.lang.CharSequence getHintText() {
            return mHintText;
        }

        public void setPromptText(java.lang.CharSequence hintText) {
            // Hint text is ignored for dropdowns, but maintain it here.
            mHintText = hintText;
        }

        void computeContentWidth() {
            final android.graphics.drawable.Drawable background = getBackground();
            int hOffset = 0;
            if (background != null) {
                background.getPadding(android.widget.Spinner.this.mTempRect);
                hOffset = (isLayoutRtl()) ? android.widget.Spinner.this.mTempRect.right : -android.widget.Spinner.this.mTempRect.left;
            } else {
                android.widget.Spinner.this.mTempRect.left = android.widget.Spinner.this.mTempRect.right = 0;
            }
            final int spinnerPaddingLeft = android.widget.Spinner.this.getPaddingLeft();
            final int spinnerPaddingRight = android.widget.Spinner.this.getPaddingRight();
            final int spinnerWidth = android.widget.Spinner.this.getWidth();
            if (android.widget.Spinner.this.mDropDownWidth == android.widget.ListPopupWindow.WRAP_CONTENT) {
                int contentWidth = measureContentWidth(((android.widget.SpinnerAdapter) (mAdapter)), getBackground());
                final int contentWidthLimit = (android.widget.Spinner.this.mContext.getResources().getDisplayMetrics().widthPixels - android.widget.Spinner.this.mTempRect.left) - android.widget.Spinner.this.mTempRect.right;
                if (contentWidth > contentWidthLimit) {
                    contentWidth = contentWidthLimit;
                }
                setContentWidth(java.lang.Math.max(contentWidth, (spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight));
            } else
                if (android.widget.Spinner.this.mDropDownWidth == android.widget.ListPopupWindow.MATCH_PARENT) {
                    setContentWidth((spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight);
                } else {
                    setContentWidth(android.widget.Spinner.this.mDropDownWidth);
                }

            if (isLayoutRtl()) {
                hOffset += (spinnerWidth - spinnerPaddingRight) - getWidth();
            } else {
                hOffset += spinnerPaddingLeft;
            }
            setHorizontalOffset(hOffset);
        }

        public void show(int textDirection, int textAlignment) {
            final boolean wasShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(android.widget.ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
            super.show();
            final android.widget.ListView listView = getListView();
            listView.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
            listView.setTextDirection(textDirection);
            listView.setTextAlignment(textAlignment);
            setSelection(android.widget.Spinner.this.getSelectedItemPosition());
            if (wasShowing) {
                // Skip setting up the layout/dismiss listener below. If we were previously
                // showing it will still stick around.
                return;
            }
            // Make sure we hide if our anchor goes away.
            // TODO: This might be appropriate to push all the way down to PopupWindow,
            // but it may have other side effects to investigate first. (Text editing handles, etc.)
            final android.view.ViewTreeObserver vto = getViewTreeObserver();
            if (vto != null) {
                final android.view.ViewTreeObserver.OnGlobalLayoutListener layoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                    @java.lang.Override
                    public void onGlobalLayout() {
                        if (!android.widget.Spinner.this.isVisibleToUser()) {
                            dismiss();
                        } else {
                            computeContentWidth();
                            // Use super.show here to update; we don't want to move the selected
                            // position or adjust other things that would be reset otherwise.
                            android.widget.Spinner.DropdownPopup.super.show();
                        }
                    }
                };
                vto.addOnGlobalLayoutListener(layoutListener);
                setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
                    @java.lang.Override
                    public void onDismiss() {
                        final android.view.ViewTreeObserver vto = getViewTreeObserver();
                        if (vto != null) {
                            vto.removeOnGlobalLayoutListener(layoutListener);
                        }
                    }
                });
            }
        }
    }
}

