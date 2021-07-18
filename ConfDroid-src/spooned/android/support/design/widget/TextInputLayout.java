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
package android.support.design.widget;


/**
 * Layout which wraps an {@link android.widget.EditText} (or descendant) to show a floating label
 * when the hint is hidden due to the user inputting text.
 *
 * <p>Also supports showing an error via {@link #setErrorEnabled(boolean)} and
 * {@link #setError(CharSequence)}, and a character counter via
 * {@link #setCounterEnabled(boolean)}.</p>
 *
 * <p>Password visibility toggling is also supported via the
 * {@link #setPasswordVisibilityToggleEnabled(boolean)} API and related attribute.
 * If enabled, a button is displayed to toggle between the password being displayed as plain-text
 * or disguised, when your EditText is set to display a password.</p>
 *
 * <p><strong>Note:</strong> When using the password toggle functionality, the 'end' compound
 * drawable of the EditText will be overridden while the toggle is enabled. To ensure that any
 * existing drawables are restored correctly, you should set those compound drawables relatively
 * (start/end), opposed to absolutely (left/right).</p>
 *
 * The {@link TextInputEditText} class is provided to be used as a child of this layout. Using
 * TextInputEditText allows TextInputLayout greater control over the visual aspects of any
 * text input. An example usage is as so:
 *
 * <pre>
 * &lt;android.support.design.widget.TextInputLayout
 *         android:layout_width=&quot;match_parent&quot;
 *         android:layout_height=&quot;wrap_content&quot;&gt;
 *
 *     &lt;android.support.design.widget.TextInputEditText
 *             android:layout_width=&quot;match_parent&quot;
 *             android:layout_height=&quot;wrap_content&quot;
 *             android:hint=&quot;@string/form_username&quot;/&gt;
 *
 * &lt;/android.support.design.widget.TextInputLayout&gt;
 * </pre>
 *
 * <p><strong>Note:</strong> The actual view hierarchy present under TextInputLayout is
 * <strong>NOT</strong> guaranteed to match the view hierarchy as written in XML. As a result,
 * calls to getParent() on children of the TextInputLayout -- such as an TextInputEditText --
 * may not return the TextInputLayout itself, but rather an intermediate View. If you need
 * to access a View directly, set an {@code android:id} and use {@link View#findViewById(int)}.
 */
public class TextInputLayout extends android.widget.LinearLayout {
    private static final int ANIMATION_DURATION = 200;

    private static final int INVALID_MAX_LENGTH = -1;

    private static final java.lang.String LOG_TAG = "TextInputLayout";

    private final android.widget.FrameLayout mInputFrame;

    android.widget.EditText mEditText;

    private boolean mHintEnabled;

    private java.lang.CharSequence mHint;

    private android.graphics.Paint mTmpPaint;

    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    private android.widget.LinearLayout mIndicatorArea;

    private int mIndicatorsAdded;

    private boolean mErrorEnabled;

    android.widget.TextView mErrorView;

    private int mErrorTextAppearance;

    private boolean mErrorShown;

    private java.lang.CharSequence mError;

    boolean mCounterEnabled;

    private android.widget.TextView mCounterView;

    private int mCounterMaxLength;

    private int mCounterTextAppearance;

    private int mCounterOverflowTextAppearance;

    private boolean mCounterOverflowed;

    private boolean mPasswordToggleEnabled;

    private android.graphics.drawable.Drawable mPasswordToggleDrawable;

    private java.lang.CharSequence mPasswordToggleContentDesc;

    private android.support.design.widget.CheckableImageButton mPasswordToggleView;

    private boolean mPasswordToggledVisible;

    private android.graphics.drawable.Drawable mPasswordToggleDummyDrawable;

    private android.graphics.drawable.Drawable mOriginalEditTextEndDrawable;

    private android.content.res.ColorStateList mPasswordToggleTintList;

    private boolean mHasPasswordToggleTintList;

    private android.graphics.PorterDuff.Mode mPasswordToggleTintMode;

    private boolean mHasPasswordToggleTintMode;

    private android.content.res.ColorStateList mDefaultTextColor;

    private android.content.res.ColorStateList mFocusedTextColor;

    // Only used for testing
    private boolean mHintExpanded;

    final android.support.design.widget.CollapsingTextHelper mCollapsingTextHelper = new android.support.design.widget.CollapsingTextHelper(this);

    private boolean mHintAnimationEnabled;

    private android.support.design.widget.ValueAnimatorCompat mAnimator;

    private boolean mHasReconstructedEditTextBackground;

    private boolean mInDrawableStateChanged;

    public TextInputLayout(android.content.Context context) {
        this(context, null);
    }

    public TextInputLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        // Can't call through to super(Context, AttributeSet, int) since it doesn't exist on API 10
        super(context, attrs);
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(context);
        setOrientation(android.widget.LinearLayout.VERTICAL);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        mInputFrame = new android.widget.FrameLayout(context);
        mInputFrame.setAddStatesFromChildren(true);
        addView(mInputFrame);
        mCollapsingTextHelper.setTextSizeInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        mCollapsingTextHelper.setPositionInterpolator(new android.view.animation.AccelerateInterpolator());
        mCollapsingTextHelper.setCollapsedTextGravity(android.view.Gravity.TOP | android.support.v4.view.GravityCompat.START);
        mHintExpanded = mCollapsingTextHelper.getExpansionFraction() == 1.0F;
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.TextInputLayout, defStyleAttr, R.style.Widget_Design_TextInputLayout);
        mHintEnabled = a.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
        setHint(a.getText(R.styleable.TextInputLayout_android_hint));
        mHintAnimationEnabled = a.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        if (a.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            mDefaultTextColor = mFocusedTextColor = a.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
        }
        final int hintAppearance = a.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1);
        if (hintAppearance != (-1)) {
            setHintTextAppearance(a.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        mErrorTextAppearance = a.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        final boolean errorEnabled = a.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        final boolean counterEnabled = a.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        setCounterMaxLength(a.getInt(R.styleable.TextInputLayout_counterMaxLength, android.support.design.widget.TextInputLayout.INVALID_MAX_LENGTH));
        mCounterTextAppearance = a.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        mCounterOverflowTextAppearance = a.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        mPasswordToggleEnabled = a.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
        mPasswordToggleDrawable = a.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
        mPasswordToggleContentDesc = a.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
        if (a.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
            mHasPasswordToggleTintList = true;
            mPasswordToggleTintList = a.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
        }
        if (a.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
            mHasPasswordToggleTintMode = true;
            mPasswordToggleTintMode = android.support.design.widget.ViewUtils.parseTintMode(a.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null);
        }
        a.recycle();
        setErrorEnabled(errorEnabled);
        setCounterEnabled(counterEnabled);
        applyPasswordToggleTint();
        if (android.support.v4.view.ViewCompat.getImportantForAccessibility(this) == android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            // Make sure we're important for accessibility if we haven't been explicitly not
            android.support.v4.view.ViewCompat.setImportantForAccessibility(this, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        android.support.v4.view.ViewCompat.setAccessibilityDelegate(this, new android.support.design.widget.TextInputLayout.TextInputAccessibilityDelegate());
    }

    @java.lang.Override
    public void addView(android.view.View child, int index, final android.view.ViewGroup.LayoutParams params) {
        if (child instanceof android.widget.EditText) {
            mInputFrame.addView(child, new android.widget.FrameLayout.LayoutParams(params));
            // Now use the EditText's LayoutParams as our own and update them to make enough space
            // for the label
            mInputFrame.setLayoutParams(params);
            updateInputLayoutMargins();
            setEditText(((android.widget.EditText) (child)));
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    /**
     * Set the typeface to use for both the expanded and floating hint.
     *
     * @param typeface
     * 		typeface to use, or {@code null} to use the default.
     */
    public void setTypeface(@android.support.annotation.Nullable
    android.graphics.Typeface typeface) {
        mCollapsingTextHelper.setTypefaces(typeface);
    }

    /**
     * Returns the typeface used for both the expanded and floating hint.
     */
    @android.support.annotation.NonNull
    public android.graphics.Typeface getTypeface() {
        // This could be either the collapsed or expanded
        return mCollapsingTextHelper.getCollapsedTypeface();
    }

    private void setEditText(android.widget.EditText editText) {
        // If we already have an EditText, throw an exception
        if (mEditText != null) {
            throw new java.lang.IllegalArgumentException("We already have an EditText, can only have one");
        }
        if (!(editText instanceof android.support.design.widget.TextInputEditText)) {
            android.util.Log.i(android.support.design.widget.TextInputLayout.LOG_TAG, "EditText added is not a TextInputEditText. Please switch to using that" + " class instead.");
        }
        mEditText = editText;
        final boolean hasPasswordTransformation = hasPasswordTransformation();
        // Use the EditText's typeface, and it's text size for our expanded text
        if (!hasPasswordTransformation) {
            // We don't want a monospace font just because we have a password field
            mCollapsingTextHelper.setTypefaces(mEditText.getTypeface());
        }
        mCollapsingTextHelper.setExpandedTextSize(mEditText.getTextSize());
        final int editTextGravity = mEditText.getGravity();
        mCollapsingTextHelper.setCollapsedTextGravity(android.view.Gravity.TOP | (editTextGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK));
        mCollapsingTextHelper.setExpandedTextGravity(editTextGravity);
        // Add a TextWatcher so that we know when the text input has changed
        mEditText.addTextChangedListener(new android.text.TextWatcher() {
            @java.lang.Override
            public void afterTextChanged(android.text.Editable s) {
                updateLabelState(true);
                if (mCounterEnabled) {
                    updateCounter(s.length());
                }
            }

            @java.lang.Override
            public void beforeTextChanged(java.lang.CharSequence s, int start, int count, int after) {
            }

            @java.lang.Override
            public void onTextChanged(java.lang.CharSequence s, int start, int before, int count) {
            }
        });
        // Use the EditText's hint colors if we don't have one set
        if (mDefaultTextColor == null) {
            mDefaultTextColor = mEditText.getHintTextColors();
        }
        // If we do not have a valid hint, try and retrieve it from the EditText, if enabled
        if (mHintEnabled && android.text.TextUtils.isEmpty(mHint)) {
            setHint(mEditText.getHint());
            // Clear the EditText's hint as we will display it ourselves
            mEditText.setHint(null);
        }
        if (mCounterView != null) {
            updateCounter(mEditText.getText().length());
        }
        if (mIndicatorArea != null) {
            adjustIndicatorPadding();
        }
        updatePasswordToggleView();
        // Update the label visibility with no animation
        updateLabelState(false);
    }

    private void updateInputLayoutMargins() {
        // Create/update the LayoutParams so that we can add enough top margin
        // to the EditText so make room for the label
        final android.widget.LinearLayout.LayoutParams lp = ((android.widget.LinearLayout.LayoutParams) (mInputFrame.getLayoutParams()));
        final int newTopMargin;
        if (mHintEnabled) {
            if (mTmpPaint == null) {
                mTmpPaint = new android.graphics.Paint();
            }
            mTmpPaint.setTypeface(mCollapsingTextHelper.getCollapsedTypeface());
            mTmpPaint.setTextSize(mCollapsingTextHelper.getCollapsedTextSize());
            newTopMargin = ((int) (-mTmpPaint.ascent()));
        } else {
            newTopMargin = 0;
        }
        if (newTopMargin != lp.topMargin) {
            lp.topMargin = newTopMargin;
            mInputFrame.requestLayout();
        }
    }

    void updateLabelState(boolean animate) {
        final boolean isEnabled = isEnabled();
        final boolean hasText = (mEditText != null) && (!android.text.TextUtils.isEmpty(mEditText.getText()));
        final boolean isFocused = android.support.design.widget.TextInputLayout.arrayContains(getDrawableState(), android.R.attr.state_focused);
        final boolean isErrorShowing = !android.text.TextUtils.isEmpty(getError());
        if (mDefaultTextColor != null) {
            mCollapsingTextHelper.setExpandedTextColor(mDefaultTextColor);
        }
        if ((isEnabled && mCounterOverflowed) && (mCounterView != null)) {
            mCollapsingTextHelper.setCollapsedTextColor(mCounterView.getTextColors());
        } else
            if ((isEnabled && isFocused) && (mFocusedTextColor != null)) {
                mCollapsingTextHelper.setCollapsedTextColor(mFocusedTextColor);
            } else
                if (mDefaultTextColor != null) {
                    mCollapsingTextHelper.setCollapsedTextColor(mDefaultTextColor);
                }


        if (hasText || (isEnabled() && (isFocused || isErrorShowing))) {
            // We should be showing the label so do so if it isn't already
            collapseHint(animate);
        } else {
            // We should not be showing the label so hide it
            expandHint(animate);
        }
    }

    /**
     * Returns the {@link android.widget.EditText} used for text input.
     */
    @android.support.annotation.Nullable
    public android.widget.EditText getEditText() {
        return mEditText;
    }

    /**
     * Set the hint to be displayed in the floating label, if enabled.
     *
     * @see #setHintEnabled(boolean)
     * @unknown ref android.support.design.R.styleable#TextInputLayout_android_hint
     */
    public void setHint(@android.support.annotation.Nullable
    java.lang.CharSequence hint) {
        if (mHintEnabled) {
            setHintInternal(hint);
            sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
        }
    }

    private void setHintInternal(java.lang.CharSequence hint) {
        mHint = hint;
        mCollapsingTextHelper.setText(hint);
    }

    /**
     * Returns the hint which is displayed in the floating label, if enabled.
     *
     * @return the hint, or null if there isn't one set, or the hint is not enabled.
     * @unknown ref android.support.design.R.styleable#TextInputLayout_android_hint
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getHint() {
        return mHintEnabled ? mHint : null;
    }

    /**
     * Sets whether the floating label functionality is enabled or not in this layout.
     *
     * <p>If enabled, any non-empty hint in the child EditText will be moved into the floating
     * hint, and its existing hint will be cleared. If disabled, then any non-empty floating hint
     * in this layout will be moved into the EditText, and this layout's hint will be cleared.</p>
     *
     * @see #setHint(CharSequence)
     * @see #isHintEnabled()
     * @unknown ref android.support.design.R.styleable#TextInputLayout_hintEnabled
     */
    public void setHintEnabled(boolean enabled) {
        if (enabled != mHintEnabled) {
            mHintEnabled = enabled;
            final java.lang.CharSequence editTextHint = mEditText.getHint();
            if (!mHintEnabled) {
                if ((!android.text.TextUtils.isEmpty(mHint)) && android.text.TextUtils.isEmpty(editTextHint)) {
                    // If the hint is disabled, but we have a hint set, and the EditText doesn't,
                    // pass it through...
                    mEditText.setHint(mHint);
                }
                // Now clear out any set hint
                setHintInternal(null);
            } else {
                if (!android.text.TextUtils.isEmpty(editTextHint)) {
                    // If the hint is now enabled and the EditText has one set, we'll use it if
                    // we don't already have one, and clear the EditText's
                    if (android.text.TextUtils.isEmpty(mHint)) {
                        setHint(editTextHint);
                    }
                    mEditText.setHint(null);
                }
            }
            // Now update the EditText top margin
            if (mEditText != null) {
                updateInputLayoutMargins();
            }
        }
    }

    /**
     * Returns whether the floating label functionality is enabled or not in this layout.
     *
     * @see #setHintEnabled(boolean)
     * @unknown ref android.support.design.R.styleable#TextInputLayout_hintEnabled
     */
    public boolean isHintEnabled() {
        return mHintEnabled;
    }

    /**
     * Sets the hint text color, size, style from the specified TextAppearance resource.
     *
     * @unknown ref android.support.design.R.styleable#TextInputLayout_hintTextAppearance
     */
    public void setHintTextAppearance(@android.support.annotation.StyleRes
    int resId) {
        mCollapsingTextHelper.setCollapsedTextAppearance(resId);
        mFocusedTextColor = mCollapsingTextHelper.getCollapsedTextColor();
        if (mEditText != null) {
            updateLabelState(false);
            // Text size might have changed so update the top margin
            updateInputLayoutMargins();
        }
    }

    private void addIndicator(android.widget.TextView indicator, int index) {
        if (mIndicatorArea == null) {
            mIndicatorArea = new android.widget.LinearLayout(getContext());
            mIndicatorArea.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            addView(mIndicatorArea, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            // Add a flexible spacer in the middle so that the left/right views stay pinned
            final android.support.v4.widget.Space spacer = new android.support.v4.widget.Space(getContext());
            final android.widget.LinearLayout.LayoutParams spacerLp = new android.widget.LinearLayout.LayoutParams(0, 0, 1.0F);
            mIndicatorArea.addView(spacer, spacerLp);
            if (mEditText != null) {
                adjustIndicatorPadding();
            }
        }
        mIndicatorArea.setVisibility(android.view.View.VISIBLE);
        mIndicatorArea.addView(indicator, index);
        mIndicatorsAdded++;
    }

    private void adjustIndicatorPadding() {
        // Add padding to the error and character counter so that they match the EditText
        android.support.v4.view.ViewCompat.setPaddingRelative(mIndicatorArea, android.support.v4.view.ViewCompat.getPaddingStart(mEditText), 0, android.support.v4.view.ViewCompat.getPaddingEnd(mEditText), mEditText.getPaddingBottom());
    }

    private void removeIndicator(android.widget.TextView indicator) {
        if (mIndicatorArea != null) {
            mIndicatorArea.removeView(indicator);
            if ((--mIndicatorsAdded) == 0) {
                mIndicatorArea.setVisibility(android.view.View.GONE);
            }
        }
    }

    /**
     * Whether the error functionality is enabled or not in this layout. Enabling this
     * functionality before setting an error message via {@link #setError(CharSequence)}, will mean
     * that this layout will not change size when an error is displayed.
     *
     * @unknown ref android.support.design.R.styleable#TextInputLayout_errorEnabled
     */
    public void setErrorEnabled(boolean enabled) {
        if (mErrorEnabled != enabled) {
            if (mErrorView != null) {
                android.support.v4.view.ViewCompat.animate(mErrorView).cancel();
            }
            if (enabled) {
                mErrorView = new android.widget.TextView(getContext());
                boolean useDefaultColor = false;
                try {
                    android.support.v4.widget.TextViewCompat.setTextAppearance(mErrorView, mErrorTextAppearance);
                    if ((android.os.Build.VERSION.SDK_INT >= 23) && (mErrorView.getTextColors().getDefaultColor() == android.graphics.Color.MAGENTA)) {
                        // Caused by our theme not extending from Theme.Design*. On API 23 and
                        // above, unresolved theme attrs result in MAGENTA rather than an exception.
                        // Flag so that we use a decent default
                        useDefaultColor = true;
                    }
                } catch (java.lang.Exception e) {
                    // Caused by our theme not extending from Theme.Design*. Flag so that we use
                    // a decent default
                    useDefaultColor = true;
                }
                if (useDefaultColor) {
                    // Probably caused by our theme not extending from Theme.Design*. Instead
                    // we manually set something appropriate
                    android.support.v4.widget.TextViewCompat.setTextAppearance(mErrorView, android.support.design.widget.android.support.v7.appcompat.R.style);
                    mErrorView.setTextColor(android.support.v4.content.ContextCompat.getColor(getContext(), R.color.design_textinput_error_color_light));
                }
                mErrorView.setVisibility(android.view.View.INVISIBLE);
                android.support.v4.view.ViewCompat.setAccessibilityLiveRegion(mErrorView, android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
                addIndicator(mErrorView, 0);
            } else {
                mErrorShown = false;
                updateEditTextBackground();
                removeIndicator(mErrorView);
                mErrorView = null;
            }
            mErrorEnabled = enabled;
        }
    }

    /**
     * Returns whether the error functionality is enabled or not in this layout.
     *
     * @unknown ref android.support.design.R.styleable#TextInputLayout_errorEnabled
     * @see #setErrorEnabled(boolean)
     */
    public boolean isErrorEnabled() {
        return mErrorEnabled;
    }

    /**
     * Sets an error message that will be displayed below our {@link EditText}. If the
     * {@code error} is {@code null}, the error message will be cleared.
     * <p>
     * If the error functionality has not been enabled via {@link #setErrorEnabled(boolean)}, then
     * it will be automatically enabled if {@code error} is not empty.
     *
     * @param error
     * 		Error message to display, or null to clear
     * @see #getError()
     */
    public void setError(@android.support.annotation.Nullable
    final java.lang.CharSequence error) {
        // Only animate if we're enabled, laid out, and we have a different error message
        setError(error, (android.support.v4.view.ViewCompat.isLaidOut(this) && isEnabled()) && ((mErrorView == null) || (!android.text.TextUtils.equals(mErrorView.getText(), error))));
    }

    private void setError(@android.support.annotation.Nullable
    final java.lang.CharSequence error, final boolean animate) {
        mError = error;
        if (!mErrorEnabled) {
            if (android.text.TextUtils.isEmpty(error)) {
                // If error isn't enabled, and the error is empty, just return
                return;
            }
            // Else, we'll assume that they want to enable the error functionality
            setErrorEnabled(true);
        }
        mErrorShown = !android.text.TextUtils.isEmpty(error);
        // Cancel any on-going animation
        android.support.v4.view.ViewCompat.animate(mErrorView).cancel();
        if (mErrorShown) {
            mErrorView.setText(error);
            mErrorView.setVisibility(android.view.View.VISIBLE);
            if (animate) {
                if (android.support.v4.view.ViewCompat.getAlpha(mErrorView) == 1.0F) {
                    // If it's currently 100% show, we'll animate it from 0
                    android.support.v4.view.ViewCompat.setAlpha(mErrorView, 0.0F);
                }
                android.support.v4.view.ViewCompat.animate(mErrorView).alpha(1.0F).setDuration(android.support.design.widget.TextInputLayout.ANIMATION_DURATION).setInterpolator(android.support.design.widget.AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationStart(android.view.View view) {
                        view.setVisibility(android.view.View.VISIBLE);
                    }
                }).start();
            } else {
                // Set alpha to 1f, just in case
                android.support.v4.view.ViewCompat.setAlpha(mErrorView, 1.0F);
            }
        } else {
            if (mErrorView.getVisibility() == android.view.View.VISIBLE) {
                if (animate) {
                    android.support.v4.view.ViewCompat.animate(mErrorView).alpha(0.0F).setDuration(android.support.design.widget.TextInputLayout.ANIMATION_DURATION).setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                        @java.lang.Override
                        public void onAnimationEnd(android.view.View view) {
                            mErrorView.setText(error);
                            view.setVisibility(android.view.View.INVISIBLE);
                        }
                    }).start();
                } else {
                    mErrorView.setText(error);
                    mErrorView.setVisibility(android.view.View.INVISIBLE);
                }
            }
        }
        updateEditTextBackground();
        updateLabelState(animate);
    }

    /**
     * Whether the character counter functionality is enabled or not in this layout.
     *
     * @unknown ref android.support.design.R.styleable#TextInputLayout_counterEnabled
     */
    public void setCounterEnabled(boolean enabled) {
        if (mCounterEnabled != enabled) {
            if (enabled) {
                mCounterView = new android.widget.TextView(getContext());
                mCounterView.setMaxLines(1);
                try {
                    android.support.v4.widget.TextViewCompat.setTextAppearance(mCounterView, mCounterTextAppearance);
                } catch (java.lang.Exception e) {
                    // Probably caused by our theme not extending from Theme.Design*. Instead
                    // we manually set something appropriate
                    android.support.v4.widget.TextViewCompat.setTextAppearance(mCounterView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
                    mCounterView.setTextColor(android.support.v4.content.ContextCompat.getColor(getContext(), R.color.design_textinput_error_color_light));
                }
                addIndicator(mCounterView, -1);
                if (mEditText == null) {
                    updateCounter(0);
                } else {
                    updateCounter(mEditText.getText().length());
                }
            } else {
                removeIndicator(mCounterView);
                mCounterView = null;
            }
            mCounterEnabled = enabled;
        }
    }

    /**
     * Returns whether the character counter functionality is enabled or not in this layout.
     *
     * @unknown ref android.support.design.R.styleable#TextInputLayout_counterEnabled
     * @see #setCounterEnabled(boolean)
     */
    public boolean isCounterEnabled() {
        return mCounterEnabled;
    }

    /**
     * Sets the max length to display at the character counter.
     *
     * @param maxLength
     * 		maxLength to display. Any value less than or equal to 0 will not be shown.
     * @unknown ref android.support.design.R.styleable#TextInputLayout_counterMaxLength
     */
    public void setCounterMaxLength(int maxLength) {
        if (mCounterMaxLength != maxLength) {
            if (maxLength > 0) {
                mCounterMaxLength = maxLength;
            } else {
                mCounterMaxLength = android.support.design.widget.TextInputLayout.INVALID_MAX_LENGTH;
            }
            if (mCounterEnabled) {
                updateCounter(mEditText == null ? 0 : mEditText.getText().length());
            }
        }
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        // Since we're set to addStatesFromChildren, we need to make sure that we set all
        // children to enabled/disabled otherwise any enabled children will wipe out our disabled
        // drawable state
        android.support.design.widget.TextInputLayout.recursiveSetEnabled(this, enabled);
        super.setEnabled(enabled);
    }

    private static void recursiveSetEnabled(final android.view.ViewGroup vg, final boolean enabled) {
        for (int i = 0, count = vg.getChildCount(); i < count; i++) {
            final android.view.View child = vg.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof android.view.ViewGroup) {
                android.support.design.widget.TextInputLayout.recursiveSetEnabled(((android.view.ViewGroup) (child)), enabled);
            }
        }
    }

    /**
     * Returns the max length shown at the character counter.
     *
     * @unknown ref android.support.design.R.styleable#TextInputLayout_counterMaxLength
     */
    public int getCounterMaxLength() {
        return mCounterMaxLength;
    }

    void updateCounter(int length) {
        boolean wasCounterOverflowed = mCounterOverflowed;
        if (mCounterMaxLength == android.support.design.widget.TextInputLayout.INVALID_MAX_LENGTH) {
            mCounterView.setText(java.lang.String.valueOf(length));
            mCounterOverflowed = false;
        } else {
            mCounterOverflowed = length > mCounterMaxLength;
            if (wasCounterOverflowed != mCounterOverflowed) {
                android.support.v4.widget.TextViewCompat.setTextAppearance(mCounterView, mCounterOverflowed ? mCounterOverflowTextAppearance : mCounterTextAppearance);
            }
            mCounterView.setText(getContext().getString(R.string.character_counter_pattern, length, mCounterMaxLength));
        }
        if ((mEditText != null) && (wasCounterOverflowed != mCounterOverflowed)) {
            updateLabelState(false);
            updateEditTextBackground();
        }
    }

    private void updateEditTextBackground() {
        if (mEditText == null) {
            return;
        }
        android.graphics.drawable.Drawable editTextBackground = mEditText.getBackground();
        if (editTextBackground == null) {
            return;
        }
        ensureBackgroundDrawableStateWorkaround();
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
            editTextBackground = editTextBackground.mutate();
        }
        if (mErrorShown && (mErrorView != null)) {
            // Set a color filter of the error color
            editTextBackground.setColorFilter(android.support.v7.widget.AppCompatDrawableManager.getPorterDuffColorFilter(mErrorView.getCurrentTextColor(), android.graphics.PorterDuff.Mode.SRC_IN));
        } else
            if (mCounterOverflowed && (mCounterView != null)) {
                // Set a color filter of the counter color
                editTextBackground.setColorFilter(android.support.v7.widget.AppCompatDrawableManager.getPorterDuffColorFilter(mCounterView.getCurrentTextColor(), android.graphics.PorterDuff.Mode.SRC_IN));
            } else {
                // Else reset the color filter and refresh the drawable state so that the
                // normal tint is used
                android.support.v4.graphics.drawable.DrawableCompat.clearColorFilter(editTextBackground);
                mEditText.refreshDrawableState();
            }

    }

    private void ensureBackgroundDrawableStateWorkaround() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if ((sdk != 21) && (sdk != 22)) {
            // The workaround is only required on API 21-22
            return;
        }
        final android.graphics.drawable.Drawable bg = mEditText.getBackground();
        if (bg == null) {
            return;
        }
        if (!mHasReconstructedEditTextBackground) {
            // This is gross. There is an issue in the platform which affects container Drawables
            // where the first drawable retrieved from resources will propagate any changes
            // (like color filter) to all instances from the cache. We'll try to workaround it...
            final android.graphics.drawable.Drawable newBg = bg.getConstantState().newDrawable();
            if (bg instanceof android.graphics.drawable.DrawableContainer) {
                // If we have a Drawable container, we can try and set it's constant state via
                // reflection from the new Drawable
                mHasReconstructedEditTextBackground = android.support.design.widget.DrawableUtils.setContainerConstantState(((android.graphics.drawable.DrawableContainer) (bg)), newBg.getConstantState());
            }
            if (!mHasReconstructedEditTextBackground) {
                // If we reach here then we just need to set a brand new instance of the Drawable
                // as the background. This has the unfortunate side-effect of wiping out any
                // user set padding, but I'd hope that use of custom padding on an EditText
                // is limited.
                android.support.v4.view.ViewCompat.setBackground(mEditText, newBg);
                mHasReconstructedEditTextBackground = true;
            }
        }
    }

    static class SavedState extends android.support.v4.view.AbsSavedState {
        java.lang.CharSequence error;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        public SavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
            super(source, loader);
            error = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            android.text.TextUtils.writeToParcel(error, dest, flags);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("TextInputLayout.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " error=") + error) + "}";
        }

        public static final android.os.Parcelable.Creator<android.support.design.widget.TextInputLayout.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.design.widget.TextInputLayout.SavedState>() {
            @java.lang.Override
            public android.support.design.widget.TextInputLayout.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.design.widget.TextInputLayout.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.design.widget.TextInputLayout.SavedState[] newArray(int size) {
                return new android.support.design.widget.TextInputLayout.SavedState[size];
            }
        });
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.support.design.widget.TextInputLayout.SavedState ss = new android.support.design.widget.TextInputLayout.SavedState(superState);
        if (mErrorShown) {
            ss.error = getError();
        }
        return ss;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.design.widget.TextInputLayout.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.design.widget.TextInputLayout.SavedState ss = ((android.support.design.widget.TextInputLayout.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        setError(ss.error);
        requestLayout();
    }

    /**
     * Returns the error message that was set to be displayed with
     * {@link #setError(CharSequence)}, or <code>null</code> if no error was set
     * or if error displaying is not enabled.
     *
     * @see #setError(CharSequence)
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getError() {
        return mErrorEnabled ? mError : null;
    }

    /**
     * Returns whether any hint state changes, due to being focused or non-empty text, are
     * animated.
     *
     * @see #setHintAnimationEnabled(boolean)
     * @unknown ref android.support.design.R.styleable#TextInputLayout_hintAnimationEnabled
     */
    public boolean isHintAnimationEnabled() {
        return mHintAnimationEnabled;
    }

    /**
     * Set whether any hint state changes, due to being focused or non-empty text, are
     * animated.
     *
     * @see #isHintAnimationEnabled()
     * @unknown ref android.support.design.R.styleable#TextInputLayout_hintAnimationEnabled
     */
    public void setHintAnimationEnabled(boolean enabled) {
        mHintAnimationEnabled = enabled;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        super.draw(canvas);
        if (mHintEnabled) {
            mCollapsingTextHelper.draw(canvas);
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updatePasswordToggleView();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void updatePasswordToggleView() {
        if (mEditText == null) {
            // If there is no EditText, there is nothing to update
            return;
        }
        if (shouldShowPasswordIcon()) {
            if (mPasswordToggleView == null) {
                mPasswordToggleView = ((android.support.design.widget.CheckableImageButton) (android.view.LayoutInflater.from(getContext()).inflate(R.layout.design_text_input_password_icon, mInputFrame, false)));
                mPasswordToggleView.setImageDrawable(mPasswordToggleDrawable);
                mPasswordToggleView.setContentDescription(mPasswordToggleContentDesc);
                mInputFrame.addView(mPasswordToggleView);
                mPasswordToggleView.setOnClickListener(new android.view.View.OnClickListener() {
                    @java.lang.Override
                    public void onClick(android.view.View view) {
                        passwordVisibilityToggleRequested();
                    }
                });
            }
            mPasswordToggleView.setVisibility(android.view.View.VISIBLE);
            // We need to add a dummy drawable as the end compound drawable so that the text is
            // indented and doesn't display below the toggle view
            if (mPasswordToggleDummyDrawable == null) {
                mPasswordToggleDummyDrawable = new android.graphics.drawable.ColorDrawable();
            }
            mPasswordToggleDummyDrawable.setBounds(0, 0, mPasswordToggleView.getMeasuredWidth(), 1);
            final android.graphics.drawable.Drawable[] compounds = android.support.v4.widget.TextViewCompat.getCompoundDrawablesRelative(mEditText);
            // Store the user defined end compound drawable so that we can restore it later
            if (compounds[2] != mPasswordToggleDummyDrawable) {
                mOriginalEditTextEndDrawable = compounds[2];
            }
            android.support.v4.widget.TextViewCompat.setCompoundDrawablesRelative(mEditText, compounds[0], compounds[1], mPasswordToggleDummyDrawable, compounds[3]);
            // Copy over the EditText's padding so that we match
            mPasswordToggleView.setPadding(mEditText.getPaddingLeft(), mEditText.getPaddingTop(), mEditText.getPaddingRight(), mEditText.getPaddingBottom());
        } else {
            if ((mPasswordToggleView != null) && (mPasswordToggleView.getVisibility() == android.view.View.VISIBLE)) {
                mPasswordToggleView.setVisibility(android.view.View.GONE);
            }
            if (mPasswordToggleDummyDrawable != null) {
                // Make sure that we remove the dummy end compound drawable if it exists, and then
                // clear it
                final android.graphics.drawable.Drawable[] compounds = android.support.v4.widget.TextViewCompat.getCompoundDrawablesRelative(mEditText);
                if (compounds[2] == mPasswordToggleDummyDrawable) {
                    android.support.v4.widget.TextViewCompat.setCompoundDrawablesRelative(mEditText, compounds[0], compounds[1], mOriginalEditTextEndDrawable, compounds[3]);
                    mPasswordToggleDummyDrawable = null;
                }
            }
        }
    }

    /**
     * Set the icon to use for the password visibility toggle button.
     *
     * <p>If you use an icon you should also set a description for its action
     * using {@link #setPasswordVisibilityToggleContentDescription(CharSequence)}.
     * This is used for accessibility.</p>
     *
     * @param resId
     * 		resource id of the drawable to set, or 0 to clear the icon
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleDrawable
     */
    public void setPasswordVisibilityToggleDrawable(@android.support.annotation.DrawableRes
    int resId) {
        setPasswordVisibilityToggleDrawable(resId != 0 ? android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId) : null);
    }

    /**
     * Set the icon to use for the password visibility toggle button.
     *
     * <p>If you use an icon you should also set a description for its action
     * using {@link #setPasswordVisibilityToggleContentDescription(CharSequence)}.
     * This is used for accessibility.</p>
     *
     * @param icon
     * 		Drawable to set, may be null to clear the icon
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleDrawable
     */
    public void setPasswordVisibilityToggleDrawable(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable icon) {
        mPasswordToggleDrawable = icon;
        if (mPasswordToggleView != null) {
            mPasswordToggleView.setImageDrawable(icon);
        }
    }

    /**
     * Set a content description for the navigation button if one is present.
     *
     * <p>The content description will be read via screen readers or other accessibility
     * systems to explain the action of the password visibility toggle.</p>
     *
     * @param resId
     * 		Resource ID of a content description string to set,
     * 		or 0 to clear the description
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleContentDescription
     */
    public void setPasswordVisibilityToggleContentDescription(@android.support.annotation.StringRes
    int resId) {
        setPasswordVisibilityToggleContentDescription(resId != 0 ? getResources().getText(resId) : null);
    }

    /**
     * Set a content description for the navigation button if one is present.
     *
     * <p>The content description will be read via screen readers or other accessibility
     * systems to explain the action of the password visibility toggle.</p>
     *
     * @param description
     * 		Content description to set, or null to clear the content description
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleContentDescription
     */
    public void setPasswordVisibilityToggleContentDescription(@android.support.annotation.Nullable
    java.lang.CharSequence description) {
        mPasswordToggleContentDesc = description;
        if (mPasswordToggleView != null) {
            mPasswordToggleView.setContentDescription(description);
        }
    }

    /**
     * Returns the icon currently used for the password visibility toggle button.
     *
     * @see #setPasswordVisibilityToggleDrawable(Drawable)
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleDrawable
     */
    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable getPasswordVisibilityToggleDrawable() {
        return mPasswordToggleDrawable;
    }

    /**
     * Returns the currently configured content description for the password visibility
     * toggle button.
     *
     * <p>This will be used to describe the navigation action to users through mechanisms
     * such as screen readers.</p>
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getPasswordVisibilityToggleContentDescription() {
        return mPasswordToggleContentDesc;
    }

    /**
     * Returns whether the password visibility toggle functionality is currently enabled.
     *
     * @see #setPasswordVisibilityToggleEnabled(boolean)
     */
    public boolean isPasswordVisibilityToggleEnabled() {
        return mPasswordToggleEnabled;
    }

    /**
     * Returns whether the password visibility toggle functionality is enabled or not.
     *
     * <p>When enabled, a button is placed at the end of the EditText which enables the user
     * to switch between the field's input being visibly disguised or not.</p>
     *
     * @param enabled
     * 		true to enable the functionality
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleEnabled
     */
    public void setPasswordVisibilityToggleEnabled(final boolean enabled) {
        if (mPasswordToggleEnabled != enabled) {
            mPasswordToggleEnabled = enabled;
            if (((!enabled) && mPasswordToggledVisible) && (mEditText != null)) {
                // If the toggle is no longer enabled, but we remove the PasswordTransformation
                // to make the password visible, add it back
                mEditText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            }
            // Reset the visibility tracking flag
            mPasswordToggledVisible = false;
            updatePasswordToggleView();
        }
    }

    /**
     * Applies a tint to the the password visibility toggle drawable. Does not modify the current
     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     *
     * <p>Subsequent calls to {@link #setPasswordVisibilityToggleDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint mode using
     * {@link DrawableCompat#setTintList(Drawable, ColorStateList)}.</p>
     *
     * @param tintList
     * 		the tint to apply, may be null to clear tint
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleTint
     */
    public void setPasswordVisibilityToggleTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tintList) {
        mPasswordToggleTintList = tintList;
        mHasPasswordToggleTintList = true;
        applyPasswordToggleTint();
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setPasswordVisibilityToggleTintList(ColorStateList)} to the password
     * visibility toggle drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.</p>
     *
     * @param mode
     * 		the blending mode used to apply the tint, may be null to clear tint
     * @unknown ref android.support.design.R.styleable#TextInputLayout_passwordToggleTintMode
     */
    public void setPasswordVisibilityToggleTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode mode) {
        mPasswordToggleTintMode = mode;
        mHasPasswordToggleTintMode = true;
        applyPasswordToggleTint();
    }

    void passwordVisibilityToggleRequested() {
        if (mPasswordToggleEnabled) {
            // Store the current cursor position
            final int selection = mEditText.getSelectionEnd();
            if (hasPasswordTransformation()) {
                mEditText.setTransformationMethod(null);
                mPasswordToggledVisible = true;
            } else {
                mEditText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                mPasswordToggledVisible = false;
            }
            mPasswordToggleView.setChecked(mPasswordToggledVisible);
            // And restore the cursor position
            mEditText.setSelection(selection);
        }
    }

    private boolean hasPasswordTransformation() {
        return (mEditText != null) && (mEditText.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod);
    }

    private boolean shouldShowPasswordIcon() {
        return mPasswordToggleEnabled && (hasPasswordTransformation() || mPasswordToggledVisible);
    }

    private void applyPasswordToggleTint() {
        if ((mPasswordToggleDrawable != null) && (mHasPasswordToggleTintList || mHasPasswordToggleTintMode)) {
            mPasswordToggleDrawable = android.support.v4.graphics.drawable.DrawableCompat.wrap(mPasswordToggleDrawable).mutate();
            if (mHasPasswordToggleTintList) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintList(mPasswordToggleDrawable, mPasswordToggleTintList);
            }
            if (mHasPasswordToggleTintMode) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mPasswordToggleDrawable, mPasswordToggleTintMode);
            }
            if ((mPasswordToggleView != null) && (mPasswordToggleView.getDrawable() != mPasswordToggleDrawable)) {
                mPasswordToggleView.setImageDrawable(mPasswordToggleDrawable);
            }
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHintEnabled && (mEditText != null)) {
            final android.graphics.Rect rect = mTmpRect;
            android.support.design.widget.ViewGroupUtils.getDescendantRect(this, mEditText, rect);
            final int l = rect.left + mEditText.getCompoundPaddingLeft();
            final int r = rect.right - mEditText.getCompoundPaddingRight();
            mCollapsingTextHelper.setExpandedBounds(l, rect.top + mEditText.getCompoundPaddingTop(), r, rect.bottom - mEditText.getCompoundPaddingBottom());
            // Set the collapsed bounds to be the the full height (minus padding) to match the
            // EditText's editable area
            mCollapsingTextHelper.setCollapsedBounds(l, getPaddingTop(), r, (bottom - top) - getPaddingBottom());
            mCollapsingTextHelper.recalculate();
        }
    }

    private void collapseHint(boolean animate) {
        if ((mAnimator != null) && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (animate && mHintAnimationEnabled) {
            animateToExpansionFraction(1.0F);
        } else {
            mCollapsingTextHelper.setExpansionFraction(1.0F);
        }
        mHintExpanded = false;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        if (mInDrawableStateChanged) {
            // Some of the calls below will update the drawable state of child views. Since we're
            // using addStatesFromChildren we can get into infinite recursion, hence we'll just
            // exit in this instance
            return;
        }
        mInDrawableStateChanged = true;
        super.drawableStateChanged();
        final int[] state = getDrawableState();
        boolean changed = false;
        // Drawable state has changed so see if we need to update the label
        updateLabelState(android.support.v4.view.ViewCompat.isLaidOut(this) && isEnabled());
        updateEditTextBackground();
        if (mCollapsingTextHelper != null) {
            changed |= mCollapsingTextHelper.setState(state);
        }
        if (changed) {
            invalidate();
        }
        mInDrawableStateChanged = false;
    }

    private void expandHint(boolean animate) {
        if ((mAnimator != null) && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (animate && mHintAnimationEnabled) {
            animateToExpansionFraction(0.0F);
        } else {
            mCollapsingTextHelper.setExpansionFraction(0.0F);
        }
        mHintExpanded = true;
    }

    private void animateToExpansionFraction(final float target) {
        if (mCollapsingTextHelper.getExpansionFraction() == target) {
            return;
        }
        if (mAnimator == null) {
            mAnimator = android.support.design.widget.ViewUtils.createAnimator();
            mAnimator.setInterpolator(android.support.design.widget.AnimationUtils.LINEAR_INTERPOLATOR);
            mAnimator.setDuration(android.support.design.widget.TextInputLayout.ANIMATION_DURATION);
            mAnimator.addUpdateListener(new android.support.design.widget.ValueAnimatorCompat.AnimatorUpdateListener() {
                @java.lang.Override
                public void onAnimationUpdate(android.support.design.widget.ValueAnimatorCompat animator) {
                    mCollapsingTextHelper.setExpansionFraction(animator.getAnimatedFloatValue());
                }
            });
        }
        mAnimator.setFloatValues(mCollapsingTextHelper.getExpansionFraction(), target);
        mAnimator.start();
    }

    @android.support.annotation.VisibleForTesting
    final boolean isHintExpanded() {
        return mHintExpanded;
    }

    private class TextInputAccessibilityDelegate extends android.support.v4.view.AccessibilityDelegateCompat {
        TextInputAccessibilityDelegate() {
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(android.support.design.widget.TextInputLayout.class.getSimpleName());
        }

        @java.lang.Override
        public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(host, event);
            final java.lang.CharSequence text = mCollapsingTextHelper.getText();
            if (!android.text.TextUtils.isEmpty(text)) {
                event.getText().add(text);
            }
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(android.support.design.widget.TextInputLayout.class.getSimpleName());
            final java.lang.CharSequence text = mCollapsingTextHelper.getText();
            if (!android.text.TextUtils.isEmpty(text)) {
                info.setText(text);
            }
            if (mEditText != null) {
                info.setLabelFor(mEditText);
            }
            final java.lang.CharSequence error = (mErrorView != null) ? mErrorView.getText() : null;
            if (!android.text.TextUtils.isEmpty(error)) {
                info.setContentInvalid(true);
                info.setError(error);
            }
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int v : array) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }
}

