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
 * An extension to {@link TextView} that supports the {@link Checkable}
 * interface and displays.
 * <p>
 * This is useful when used in a {@link android.widget.ListView ListView} where
 * the {@link android.widget.ListView#setChoiceMode(int) setChoiceMode} has
 * been set to something other than
 * {@link android.widget.ListView#CHOICE_MODE_NONE CHOICE_MODE_NONE}.
 *
 * @unknown ref android.R.styleable#CheckedTextView_checked
 * @unknown ref android.R.styleable#CheckedTextView_checkMark
 */
public class CheckedTextView extends android.widget.TextView implements android.widget.Checkable {
    private boolean mChecked;

    private int mCheckMarkResource;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mCheckMarkDrawable;

    private android.content.res.ColorStateList mCheckMarkTintList = null;

    private android.graphics.BlendMode mCheckMarkBlendMode = null;

    private boolean mHasCheckMarkTint = false;

    private boolean mHasCheckMarkTintMode = false;

    private int mBasePadding;

    private int mCheckMarkWidth;

    @android.annotation.UnsupportedAppUsage
    private int mCheckMarkGravity = android.view.Gravity.END;

    private boolean mNeedRequestlayout;

    private static final int[] CHECKED_STATE_SET = new int[]{ R.attr.state_checked };

    public CheckedTextView(android.content.Context context) {
        this(context, null);
    }

    public CheckedTextView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.checkedTextViewStyle);
    }

    public CheckedTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckedTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckedTextView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.CheckedTextView, attrs, a, defStyleAttr, defStyleRes);
        final android.graphics.drawable.Drawable d = a.getDrawable(R.styleable.CheckedTextView_checkMark);
        if (d != null) {
            setCheckMarkDrawable(d);
        }
        if (a.hasValue(R.styleable.CheckedTextView_checkMarkTintMode)) {
            mCheckMarkBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.CheckedTextView_checkMarkTintMode, -1), mCheckMarkBlendMode);
            mHasCheckMarkTintMode = true;
        }
        if (a.hasValue(R.styleable.CheckedTextView_checkMarkTint)) {
            mCheckMarkTintList = a.getColorStateList(R.styleable.CheckedTextView_checkMarkTint);
            mHasCheckMarkTint = true;
        }
        mCheckMarkGravity = a.getInt(R.styleable.CheckedTextView_checkMarkGravity, android.view.Gravity.END);
        final boolean checked = a.getBoolean(R.styleable.CheckedTextView_checked, false);
        setChecked(checked);
        a.recycle();
        applyCheckMarkTint();
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @android.view.ViewDebug.ExportedProperty
    @android.view.inspector.InspectableProperty
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * Sets the checked state of this view.
     *
     * @param checked
     * 		{@code true} set the state to checked, {@code false} to
     * 		uncheck
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            notifyViewAccessibilityStateChangedIfNeeded(android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);
        }
    }

    /**
     * Sets the check mark to the drawable with the specified resource ID.
     * <p>
     * When this view is checked, the drawable's state set will include
     * {@link android.R.attr#state_checked}.
     *
     * @param resId
     * 		the resource identifier of drawable to use as the check
     * 		mark
     * @unknown ref android.R.styleable#CheckedTextView_checkMark
     * @see #setCheckMarkDrawable(Drawable)
     * @see #getCheckMarkDrawable()
     */
    public void setCheckMarkDrawable(@android.annotation.DrawableRes
    int resId) {
        if ((resId != 0) && (resId == mCheckMarkResource)) {
            return;
        }
        final android.graphics.drawable.Drawable d = (resId != 0) ? getContext().getDrawable(resId) : null;
        setCheckMarkDrawableInternal(d, resId);
    }

    /**
     * Set the check mark to the specified drawable.
     * <p>
     * When this view is checked, the drawable's state set will include
     * {@link android.R.attr#state_checked}.
     *
     * @param d
     * 		the drawable to use for the check mark
     * @unknown ref android.R.styleable#CheckedTextView_checkMark
     * @see #setCheckMarkDrawable(int)
     * @see #getCheckMarkDrawable()
     */
    public void setCheckMarkDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable d) {
        setCheckMarkDrawableInternal(d, 0);
    }

    private void setCheckMarkDrawableInternal(@android.annotation.Nullable
    android.graphics.drawable.Drawable d, @android.annotation.DrawableRes
    int resId) {
        if (mCheckMarkDrawable != null) {
            mCheckMarkDrawable.setCallback(null);
            unscheduleDrawable(mCheckMarkDrawable);
        }
        mNeedRequestlayout = d != mCheckMarkDrawable;
        if (d != null) {
            d.setCallback(this);
            d.setVisible(getVisibility() == android.view.View.VISIBLE, false);
            d.setState(android.widget.CheckedTextView.CHECKED_STATE_SET);
            // Record the intrinsic dimensions when in "checked" state.
            setMinHeight(d.getIntrinsicHeight());
            mCheckMarkWidth = d.getIntrinsicWidth();
            d.setState(getDrawableState());
        } else {
            mCheckMarkWidth = 0;
        }
        mCheckMarkDrawable = d;
        mCheckMarkResource = resId;
        applyCheckMarkTint();
        // Do padding resolution. This will call internalSetPadding() and do a
        // requestLayout() if needed.
        resolvePadding();
    }

    /**
     * Applies a tint to the check mark drawable. Does not modify the
     * current tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setCheckMarkDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and
     * tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#CheckedTextView_checkMarkTint
     * @see #getCheckMarkTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setCheckMarkTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mCheckMarkTintList = tint;
        mHasCheckMarkTint = true;
        applyCheckMarkTint();
    }

    /**
     * Returns the tint applied to the check mark drawable, if specified.
     *
     * @return the tint applied to the check mark drawable
     * @unknown ref android.R.styleable#CheckedTextView_checkMarkTint
     * @see #setCheckMarkTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "checkMarkTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getCheckMarkTintList() {
        return mCheckMarkTintList;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setCheckMarkTintList(ColorStateList)} to the check mark
     * drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#CheckedTextView_checkMarkTintMode
     * @see #setCheckMarkTintList(ColorStateList)
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setCheckMarkTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setCheckMarkTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setCheckMarkTintList(ColorStateList)} to the check mark
     * drawable. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#CheckedTextView_checkMarkTintMode
     * @see #setCheckMarkTintList(ColorStateList)
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setCheckMarkTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode tintMode) {
        mCheckMarkBlendMode = tintMode;
        mHasCheckMarkTintMode = true;
        applyCheckMarkTint();
    }

    /**
     * Returns the blending mode used to apply the tint to the check mark
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the check mark
    drawable
     * @unknown ref android.R.styleable#CheckedTextView_checkMarkTintMode
     * @see #setCheckMarkTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getCheckMarkTintMode() {
        return mCheckMarkBlendMode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mCheckMarkBlendMode) : null;
    }

    /**
     * Returns the blending mode used to apply the tint to the check mark
     * drawable, if specified.
     *
     * @return the blending mode used to apply the tint to the check mark
    drawable
     * @unknown ref android.R.styleable#CheckedTextView_checkMarkTintMode
     * @see #setCheckMarkTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty(attributeId = android.widget.android.R.styleable.class)
    @android.annotation.Nullable
    public android.graphics.BlendMode getCheckMarkTintBlendMode() {
        return mCheckMarkBlendMode;
    }

    private void applyCheckMarkTint() {
        if ((mCheckMarkDrawable != null) && (mHasCheckMarkTint || mHasCheckMarkTintMode)) {
            mCheckMarkDrawable = mCheckMarkDrawable.mutate();
            if (mHasCheckMarkTint) {
                mCheckMarkDrawable.setTintList(mCheckMarkTintList);
            }
            if (mHasCheckMarkTintMode) {
                mCheckMarkDrawable.setTintBlendMode(mCheckMarkBlendMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mCheckMarkDrawable.isStateful()) {
                mCheckMarkDrawable.setState(getDrawableState());
            }
        }
    }

    @android.view.RemotableViewMethod
    @java.lang.Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (mCheckMarkDrawable != null) {
            mCheckMarkDrawable.setVisible(visibility == android.view.View.VISIBLE, false);
        }
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mCheckMarkDrawable != null) {
            mCheckMarkDrawable.jumpToCurrentState();
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        return (who == mCheckMarkDrawable) || super.verifyDrawable(who);
    }

    /**
     * Gets the checkmark drawable
     *
     * @return The drawable use to represent the checkmark, if any.
     * @see #setCheckMarkDrawable(Drawable)
     * @see #setCheckMarkDrawable(int)
     * @unknown ref android.R.styleable#CheckedTextView_checkMark
     */
    @android.view.inspector.InspectableProperty(name = "checkMark")
    public android.graphics.drawable.Drawable getCheckMarkDrawable() {
        return mCheckMarkDrawable;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        setBasePadding(isCheckMarkAtStart());
    }

    @java.lang.Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        updatePadding();
    }

    private void updatePadding() {
        resetPaddingToInitialValues();
        int newPadding = (mCheckMarkDrawable != null) ? mCheckMarkWidth + mBasePadding : mBasePadding;
        if (isCheckMarkAtStart()) {
            mNeedRequestlayout |= mPaddingLeft != newPadding;
            mPaddingLeft = newPadding;
        } else {
            mNeedRequestlayout |= mPaddingRight != newPadding;
            mPaddingRight = newPadding;
        }
        if (mNeedRequestlayout) {
            requestLayout();
            mNeedRequestlayout = false;
        }
    }

    private void setBasePadding(boolean checkmarkAtStart) {
        if (checkmarkAtStart) {
            mBasePadding = mPaddingLeft;
        } else {
            mBasePadding = mPaddingRight;
        }
    }

    private boolean isCheckMarkAtStart() {
        final int gravity = android.view.Gravity.getAbsoluteGravity(mCheckMarkGravity, getLayoutDirection());
        final int hgrav = gravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        return hgrav == android.view.Gravity.LEFT;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        final android.graphics.drawable.Drawable checkMarkDrawable = mCheckMarkDrawable;
        if (checkMarkDrawable != null) {
            final int verticalGravity = getGravity() & android.view.Gravity.VERTICAL_GRAVITY_MASK;
            final int height = checkMarkDrawable.getIntrinsicHeight();
            int y = 0;
            switch (verticalGravity) {
                case android.view.Gravity.BOTTOM :
                    y = getHeight() - height;
                    break;
                case android.view.Gravity.CENTER_VERTICAL :
                    y = (getHeight() - height) / 2;
                    break;
            }
            final boolean checkMarkAtStart = isCheckMarkAtStart();
            final int width = getWidth();
            final int top = y;
            final int bottom = top + height;
            final int left;
            final int right;
            if (checkMarkAtStart) {
                left = mBasePadding;
                right = left + mCheckMarkWidth;
            } else {
                right = width - mBasePadding;
                left = right - mCheckMarkWidth;
            }
            checkMarkDrawable.setBounds(mScrollX + left, top, mScrollX + right, bottom);
            checkMarkDrawable.draw(canvas);
            final android.graphics.drawable.Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(mScrollX + left, top, mScrollX + right, bottom);
            }
        }
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            android.view.View.mergeDrawableStates(drawableState, android.widget.CheckedTextView.CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final android.graphics.drawable.Drawable checkMarkDrawable = mCheckMarkDrawable;
        if (((checkMarkDrawable != null) && checkMarkDrawable.isStateful()) && checkMarkDrawable.setState(getDrawableState())) {
            invalidateDrawable(checkMarkDrawable);
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mCheckMarkDrawable != null) {
            mCheckMarkDrawable.setHotspot(x, y);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.CheckedTextView.class.getName();
    }

    static class SavedState extends android.view.View.BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CheckedTextView#onSaveInstanceState()}
         */
        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(android.os.Parcel in) {
            super(in);
            checked = ((java.lang.Boolean) (in.readValue(null)));
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("CheckedTextView.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " checked=") + checked) + "}";
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.CheckedTextView.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.CheckedTextView.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.widget.CheckedTextView.SavedState ss = new android.widget.CheckedTextView.SavedState(superState);
        ss.checked = isChecked();
        return ss;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.CheckedTextView.SavedState ss = ((android.widget.CheckedTextView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setChecked(mChecked);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setCheckable(true);
        info.setChecked(mChecked);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void encodeProperties(@android.annotation.NonNull
    android.view.ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("text:checked", isChecked());
    }
}

