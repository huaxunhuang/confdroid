/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * A {@link Spinner} which supports compatible features on older versions of the platform,
 * including:
 * <ul>
 * <li>Dynamic tinting of the background via the background tint methods in
 * {@link android.support.v4.view.ViewCompat}.</li>
 * <li>Configuring the background tint using {@link R.attr#backgroundTint} and
 * {@link R.attr#backgroundTintMode}.</li>
 * <li>Setting the popup theme using {@link R.attr#popupTheme}.</li>
 * </ul>
 *
 * <p>This will automatically be used when you use {@link Spinner} in your layouts.
 * You should only need to manually use this class when writing custom views.</p>
 */
public class AppCompatSpinner extends android.widget.Spinner implements android.support.v4.view.TintableBackgroundView {
    static final boolean IS_AT_LEAST_M = android.os.Build.VERSION.SDK_INT >= 23;

    private static final boolean IS_AT_LEAST_JB = android.os.Build.VERSION.SDK_INT >= 16;

    private static final int[] ATTRS_ANDROID_SPINNERMODE = new int[]{ android.R.attr.spinnerMode };

    private static final int MAX_ITEMS_MEASURED = 15;

    private static final java.lang.String TAG = "AppCompatSpinner";

    private static final int MODE_DIALOG = 0;

    private static final int MODE_DROPDOWN = 1;

    private static final int MODE_THEME = -1;

    private android.support.v7.widget.AppCompatBackgroundHelper mBackgroundTintHelper;

    /**
     * Context used to inflate the popup window or dialog.
     */
    private android.content.Context mPopupContext;

    /**
     * Forwarding listener used to implement drag-to-open.
     */
    private android.support.v7.widget.ForwardingListener mForwardingListener;

    /**
     * Temporary holder for setAdapter() calls from the super constructor.
     */
    private android.widget.SpinnerAdapter mTempAdapter;

    private boolean mPopupSet;

    android.support.v7.widget.AppCompatSpinner.DropdownPopup mPopup;

    int mDropDownWidth;

    final android.graphics.Rect mTempRect = new android.graphics.Rect();

    /**
     * Construct a new spinner with the given context's theme.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     */
    public AppCompatSpinner(android.content.Context context) {
        this(context, null);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied
     * mode of displaying choices. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN}.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param mode
     * 		Constant describing how the user will select choices from the spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public AppCompatSpinner(android.content.Context context, int mode) {
        this(context, null, R.attr.spinnerStyle, mode);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied attribute set.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     */
    public AppCompatSpinner(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.spinnerStyle);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style attribute.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public AppCompatSpinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, android.support.v7.widget.AppCompatSpinner.MODE_THEME);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style. <code>mode</code> may be one of {@link #MODE_DIALOG} or
     * {@link #MODE_DROPDOWN} and determines how the user will select choices from the spinner.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param mode
     * 		Constant describing how the user will select choices from the spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public AppCompatSpinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, mode, null);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, default styles, popup mode (one of {@link #MODE_DIALOG}
     * or {@link #MODE_DROPDOWN}), and the context against which the popup
     * should be inflated.
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
     * @param mode
     * 		Constant describing how the user will select choices from
     * 		the spinner.
     * @param popupTheme
     * 		The theme against which the dialog or dropdown popup
     * 		should be inflated. May be {@code null} to use the
     * 		view theme. If set, this will override any value
     * 		specified by
     * 		{@link R.styleable#Spinner_popupTheme}.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public AppCompatSpinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int mode, android.content.res.Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr);
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.Spinner, defStyleAttr, 0);
        mBackgroundTintHelper = new android.support.v7.widget.AppCompatBackgroundHelper(this);
        if (popupTheme != null) {
            mPopupContext = new android.support.v7.view.ContextThemeWrapper(context, popupTheme);
        } else {
            final int popupThemeResId = a.getResourceId(R.styleable.Spinner_popupTheme, 0);
            if (popupThemeResId != 0) {
                mPopupContext = new android.support.v7.view.ContextThemeWrapper(context, popupThemeResId);
            } else {
                // If we're running on a < M device, we'll use the current context and still handle
                // any dropdown popup
                mPopupContext = (!android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_M) ? context : null;
            }
        }
        if (mPopupContext != null) {
            if (mode == android.support.v7.widget.AppCompatSpinner.MODE_THEME) {
                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    // If we're running on API v11+ we will try and read android:spinnerMode
                    android.content.res.TypedArray aa = null;
                    try {
                        aa = context.obtainStyledAttributes(attrs, android.support.v7.widget.AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE, defStyleAttr, 0);
                        if (aa.hasValue(0)) {
                            mode = aa.getInt(0, android.support.v7.widget.AppCompatSpinner.MODE_DIALOG);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Log.i(android.support.v7.widget.AppCompatSpinner.TAG, "Could not read android:spinnerMode", e);
                    } finally {
                        if (aa != null) {
                            aa.recycle();
                        }
                    }
                } else {
                    // Else, we use a default mode of dropdown
                    mode = android.support.v7.widget.AppCompatSpinner.MODE_DROPDOWN;
                }
            }
            if (mode == android.support.v7.widget.AppCompatSpinner.MODE_DROPDOWN) {
                final android.support.v7.widget.AppCompatSpinner.DropdownPopup popup = new android.support.v7.widget.AppCompatSpinner.DropdownPopup(mPopupContext, attrs, defStyleAttr);
                final android.support.v7.widget.TintTypedArray pa = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mPopupContext, attrs, R.styleable.Spinner, defStyleAttr, 0);
                mDropDownWidth = pa.getLayoutDimension(R.styleable.Spinner_android_dropDownWidth, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                popup.setBackgroundDrawable(pa.getDrawable(R.styleable.Spinner_android_popupBackground));
                popup.setPromptText(a.getString(R.styleable.Spinner_android_prompt));
                pa.recycle();
                mPopup = popup;
                mForwardingListener = new android.support.v7.widget.ForwardingListener(this) {
                    @java.lang.Override
                    public android.support.v7.view.menu.ShowableListMenu getPopup() {
                        return popup;
                    }

                    @java.lang.Override
                    public boolean onForwardingStarted() {
                        if (!mPopup.isShowing()) {
                            mPopup.show();
                        }
                        return true;
                    }
                };
            }
        }
        final java.lang.CharSequence[] entries = a.getTextArray(R.styleable.Spinner_android_entries);
        if (entries != null) {
            final android.widget.ArrayAdapter<java.lang.CharSequence> adapter = new android.widget.ArrayAdapter(context, android.R.layout.simple_spinner_item, entries);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            setAdapter(adapter);
        }
        a.recycle();
        mPopupSet = true;
        // Base constructors can call setAdapter before we initialize mPopup.
        // Finish setting things up if this happened.
        if (mTempAdapter != null) {
            setAdapter(mTempAdapter);
            mTempAdapter = null;
        }
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    /**
     *
     *
     * @return the context used to inflate the Spinner's popup or dialog window
     */
    public android.content.Context getPopupContext() {
        if (mPopup != null) {
            return mPopupContext;
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_M) {
                return super.getPopupContext();
            }

        return null;
    }

    public void setPopupBackgroundDrawable(android.graphics.drawable.Drawable background) {
        if (mPopup != null) {
            mPopup.setBackgroundDrawable(background);
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                super.setPopupBackgroundDrawable(background);
            }

    }

    public void setPopupBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        setPopupBackgroundDrawable(android.support.v7.content.res.AppCompatResources.getDrawable(getPopupContext(), resId));
    }

    public android.graphics.drawable.Drawable getPopupBackground() {
        if (mPopup != null) {
            return mPopup.getBackground();
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                return super.getPopupBackground();
            }

        return null;
    }

    public void setDropDownVerticalOffset(int pixels) {
        if (mPopup != null) {
            mPopup.setVerticalOffset(pixels);
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                super.setDropDownVerticalOffset(pixels);
            }

    }

    public int getDropDownVerticalOffset() {
        if (mPopup != null) {
            return mPopup.getVerticalOffset();
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                return super.getDropDownVerticalOffset();
            }

        return 0;
    }

    public void setDropDownHorizontalOffset(int pixels) {
        if (mPopup != null) {
            mPopup.setHorizontalOffset(pixels);
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                super.setDropDownHorizontalOffset(pixels);
            }

    }

    /**
     * Get the configured horizontal offset in pixels for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; other modes will return 0.
     *
     * @return Horizontal offset in pixels
     */
    public int getDropDownHorizontalOffset() {
        if (mPopup != null) {
            return mPopup.getHorizontalOffset();
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                return super.getDropDownHorizontalOffset();
            }

        return 0;
    }

    public void setDropDownWidth(int pixels) {
        if (mPopup != null) {
            mDropDownWidth = pixels;
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                super.setDropDownWidth(pixels);
            }

    }

    public int getDropDownWidth() {
        if (mPopup != null) {
            return mDropDownWidth;
        } else
            if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_JB) {
                return super.getDropDownWidth();
            }

        return 0;
    }

    @java.lang.Override
    public void setAdapter(android.widget.SpinnerAdapter adapter) {
        // The super constructor may call setAdapter before we're prepared.
        // Postpone doing anything until we've finished construction.
        if (!mPopupSet) {
            mTempAdapter = adapter;
            return;
        }
        super.setAdapter(adapter);
        if (mPopup != null) {
            final android.content.Context popupContext = (mPopupContext == null) ? getContext() : mPopupContext;
            mPopup.setAdapter(new android.support.v7.widget.AppCompatSpinner.DropDownAdapter(adapter, popupContext.getTheme()));
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if ((mPopup != null) && mPopup.isShowing()) {
            mPopup.dismiss();
        }
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
            setMeasuredDimension(java.lang.Math.min(java.lang.Math.max(measuredWidth, compatMeasureContentWidth(getAdapter(), getBackground())), android.view.View.MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
        }
    }

    @java.lang.Override
    public boolean performClick() {
        if (mPopup != null) {
            // If we have a popup, show it if needed, or just consume the click...
            if (!mPopup.isShowing()) {
                mPopup.show();
            }
            return true;
        }
        // Else let the platform handle the click
        return super.performClick();
    }

    @java.lang.Override
    public void setPrompt(java.lang.CharSequence prompt) {
        if (mPopup != null) {
            mPopup.setPromptText(prompt);
        } else {
            super.setPrompt(prompt);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getPrompt() {
        return mPopup != null ? mPopup.getHintText() : super.getPrompt();
    }

    @java.lang.Override
    public void setBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
        super.setBackgroundDrawable(background);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundDrawable(background);
        }
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#setBackgroundTintList(android.view.View,
     * ColorStateList)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setSupportBackgroundTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintList(tint);
        }
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#getBackgroundTintList(android.view.View)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getSupportBackgroundTintList() {
        return mBackgroundTintHelper != null ? mBackgroundTintHelper.getSupportBackgroundTintList() : null;
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#setBackgroundTintMode(android.view.View,
     * PorterDuff.Mode)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void setSupportBackgroundTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintMode(tintMode);
        }
    }

    /**
     * This should be accessed via
     * {@link android.support.v4.view.ViewCompat#getBackgroundTintMode(android.view.View)}
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    @android.support.annotation.Nullable
    public android.graphics.PorterDuff.Mode getSupportBackgroundTintMode() {
        return mBackgroundTintHelper != null ? mBackgroundTintHelper.getSupportBackgroundTintMode() : null;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySupportBackgroundTint();
        }
    }

    int compatMeasureContentWidth(android.widget.SpinnerAdapter adapter, android.graphics.drawable.Drawable background) {
        if (adapter == null) {
            return 0;
        }
        int width = 0;
        android.view.View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), android.view.View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), android.view.View.MeasureSpec.UNSPECIFIED);
        // Make sure the number of items we'll measure is capped. If it's a huge data set
        // with wildly varying sizes, oh well.
        int start = java.lang.Math.max(0, getSelectedItemPosition());
        final int end = java.lang.Math.min(adapter.getCount(), start + android.support.v7.widget.AppCompatSpinner.MAX_ITEMS_MEASURED);
        final int count = end - start;
        start = java.lang.Math.max(0, start - (android.support.v7.widget.AppCompatSpinner.MAX_ITEMS_MEASURED - count));
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
        public DropDownAdapter(@android.support.annotation.Nullable
        android.widget.SpinnerAdapter adapter, @android.support.annotation.Nullable
        android.content.res.Resources.Theme dropDownTheme) {
            mAdapter = adapter;
            if (adapter instanceof android.widget.ListAdapter) {
                mListAdapter = ((android.widget.ListAdapter) (adapter));
            }
            if (dropDownTheme != null) {
                if (android.support.v7.widget.AppCompatSpinner.IS_AT_LEAST_M && (adapter instanceof android.widget.ThemedSpinnerAdapter)) {
                    final android.widget.ThemedSpinnerAdapter themedAdapter = ((android.widget.ThemedSpinnerAdapter) (adapter));
                    if (themedAdapter.getDropDownViewTheme() != dropDownTheme) {
                        themedAdapter.setDropDownViewTheme(dropDownTheme);
                    }
                } else
                    if (adapter instanceof android.support.v7.widget.ThemedSpinnerAdapter) {
                        final android.support.v7.widget.ThemedSpinnerAdapter themedAdapter = ((android.support.v7.widget.ThemedSpinnerAdapter) (adapter));
                        if (themedAdapter.getDropDownViewTheme() == null) {
                            themedAdapter.setDropDownViewTheme(dropDownTheme);
                        }
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

    private class DropdownPopup extends android.support.v7.widget.ListPopupWindow {
        private java.lang.CharSequence mHintText;

        android.widget.ListAdapter mAdapter;

        private final android.graphics.Rect mVisibleRect = new android.graphics.Rect();

        public DropdownPopup(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setAnchorView(android.support.v7.widget.AppCompatSpinner.this);
            setModal(true);
            setPromptPosition(android.support.v7.widget.ListPopupWindow.POSITION_PROMPT_ABOVE);
            setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @java.lang.Override
                public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                    android.support.v7.widget.AppCompatSpinner.this.setSelection(position);
                    if (getOnItemClickListener() != null) {
                        android.support.v7.widget.AppCompatSpinner.this.performItemClick(v, position, mAdapter.getItemId(position));
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
                background.getPadding(android.support.v7.widget.AppCompatSpinner.this.mTempRect);
                hOffset = (android.support.v7.widget.ViewUtils.isLayoutRtl(android.support.v7.widget.AppCompatSpinner.this)) ? android.support.v7.widget.AppCompatSpinner.this.mTempRect.right : -android.support.v7.widget.AppCompatSpinner.this.mTempRect.left;
            } else {
                android.support.v7.widget.AppCompatSpinner.this.mTempRect.left = android.support.v7.widget.AppCompatSpinner.this.mTempRect.right = 0;
            }
            final int spinnerPaddingLeft = android.support.v7.widget.AppCompatSpinner.this.getPaddingLeft();
            final int spinnerPaddingRight = android.support.v7.widget.AppCompatSpinner.this.getPaddingRight();
            final int spinnerWidth = android.support.v7.widget.AppCompatSpinner.this.getWidth();
            if (android.support.v7.widget.AppCompatSpinner.this.mDropDownWidth == android.support.v7.widget.ListPopupWindow.WRAP_CONTENT) {
                int contentWidth = compatMeasureContentWidth(((android.widget.SpinnerAdapter) (mAdapter)), getBackground());
                final int contentWidthLimit = (getContext().getResources().getDisplayMetrics().widthPixels - android.support.v7.widget.AppCompatSpinner.this.mTempRect.left) - android.support.v7.widget.AppCompatSpinner.this.mTempRect.right;
                if (contentWidth > contentWidthLimit) {
                    contentWidth = contentWidthLimit;
                }
                setContentWidth(java.lang.Math.max(contentWidth, (spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight));
            } else
                if (android.support.v7.widget.AppCompatSpinner.this.mDropDownWidth == android.support.v7.widget.ListPopupWindow.MATCH_PARENT) {
                    setContentWidth((spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight);
                } else {
                    setContentWidth(android.support.v7.widget.AppCompatSpinner.this.mDropDownWidth);
                }

            if (android.support.v7.widget.ViewUtils.isLayoutRtl(android.support.v7.widget.AppCompatSpinner.this)) {
                hOffset += (spinnerWidth - spinnerPaddingRight) - getWidth();
            } else {
                hOffset += spinnerPaddingLeft;
            }
            setHorizontalOffset(hOffset);
        }

        public void show() {
            final boolean wasShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(android.support.v7.widget.ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
            super.show();
            final android.widget.ListView listView = getListView();
            listView.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
            setSelection(android.support.v7.widget.AppCompatSpinner.this.getSelectedItemPosition());
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
                        if (!isVisibleToUser(android.support.v7.widget.AppCompatSpinner.this)) {
                            dismiss();
                        } else {
                            computeContentWidth();
                            // Use super.show here to update; we don't want to move the selected
                            // position or adjust other things that would be reset otherwise.
                            android.support.v7.widget.AppCompatSpinner.DropdownPopup.super.show();
                        }
                    }
                };
                vto.addOnGlobalLayoutListener(layoutListener);
                setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
                    @java.lang.Override
                    public void onDismiss() {
                        final android.view.ViewTreeObserver vto = getViewTreeObserver();
                        if (vto != null) {
                            vto.removeGlobalOnLayoutListener(layoutListener);
                        }
                    }
                });
            }
        }

        /**
         * Simplified version of the the hidden View.isVisibleToUser()
         */
        boolean isVisibleToUser(android.view.View view) {
            return android.support.v4.view.ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(mVisibleRect);
        }
    }
}

