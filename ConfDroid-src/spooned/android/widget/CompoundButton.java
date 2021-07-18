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
 * <p>
 * A button with two states, checked and unchecked. When the button is pressed
 * or clicked, the state changes automatically.
 * </p>
 *
 * <p><strong>XML attributes</strong></p>
 * <p>
 * See {@link android.R.styleable#CompoundButton
 * CompoundButton Attributes}, {@link android.R.styleable#Button Button
 * Attributes}, {@link android.R.styleable#TextView TextView Attributes}, {@link android.R.styleable#View View Attributes}
 * </p>
 */
public abstract class CompoundButton extends android.widget.Button implements android.widget.Checkable {
    private static final java.lang.String LOG_TAG = android.widget.CompoundButton.class.getSimpleName();

    private boolean mChecked;

    @android.annotation.UnsupportedAppUsage
    private boolean mBroadcasting;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mButtonDrawable;

    private android.content.res.ColorStateList mButtonTintList = null;

    private android.graphics.BlendMode mButtonBlendMode = null;

    private boolean mHasButtonTint = false;

    private boolean mHasButtonBlendMode = false;

    @android.annotation.UnsupportedAppUsage
    private android.widget.CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    private android.widget.CompoundButton.OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    // Indicates whether the toggle state was set from resources or dynamically, so it can be used
    // to sanitize autofill requests.
    private boolean mCheckedFromResource = false;

    private static final int[] CHECKED_STATE_SET = new int[]{ R.attr.state_checked };

    public CompoundButton(android.content.Context context) {
        this(context, null);
    }

    public CompoundButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompoundButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.CompoundButton, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.CompoundButton, attrs, a, defStyleAttr, defStyleRes);
        final android.graphics.drawable.Drawable d = a.getDrawable(com.android.internal.R.styleable.CompoundButton_button);
        if (d != null) {
            setButtonDrawable(d);
        }
        if (a.hasValue(R.styleable.CompoundButton_buttonTintMode)) {
            mButtonBlendMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(R.styleable.CompoundButton_buttonTintMode, -1), mButtonBlendMode);
            mHasButtonBlendMode = true;
        }
        if (a.hasValue(R.styleable.CompoundButton_buttonTint)) {
            mButtonTintList = a.getColorStateList(R.styleable.CompoundButton_buttonTint);
            mHasButtonTint = true;
        }
        final boolean checked = a.getBoolean(com.android.internal.R.styleable.CompoundButton_checked, false);
        setChecked(checked);
        mCheckedFromResource = true;
        a.recycle();
        applyButtonTint();
    }

    @java.lang.Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @java.lang.Override
    public boolean performClick() {
        toggle();
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(android.view.SoundEffectConstants.CLICK);
        }
        return handled;
    }

    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty
    @java.lang.Override
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked
     * 		true to check the button, false to uncheck it
     */
    @java.lang.Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mCheckedFromResource = false;
            mChecked = checked;
            refreshDrawableState();
            notifyViewAccessibilityStateChangedIfNeeded(android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);
            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }
            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
            final android.view.autofill.AutofillManager afm = mContext.getSystemService(android.view.autofill.AutofillManager.class);
            if (afm != null) {
                afm.notifyValueChanged(this);
            }
            mBroadcasting = false;
        }
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener
     * 		the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(@android.annotation.Nullable
    android.widget.CompoundButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener
     * 		the callback to call on checked state change
     * @unknown 
     */
    void setOnCheckedChangeWidgetListener(android.widget.CompoundButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView
         * 		The compound button view whose state has changed.
         * @param isChecked
         * 		The new checked state of buttonView.
         */
        void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked);
    }

    /**
     * Sets a drawable as the compound button image given its resource
     * identifier.
     *
     * @param resId
     * 		the resource identifier of the drawable
     * @unknown ref android.R.styleable#CompoundButton_button
     */
    public void setButtonDrawable(@android.annotation.DrawableRes
    int resId) {
        final android.graphics.drawable.Drawable d;
        if (resId != 0) {
            d = getContext().getDrawable(resId);
        } else {
            d = null;
        }
        setButtonDrawable(d);
    }

    /**
     * Sets a drawable as the compound button image.
     *
     * @param drawable
     * 		the drawable to set
     * @unknown ref android.R.styleable#CompoundButton_button
     */
    public void setButtonDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable drawable) {
        if (mButtonDrawable != drawable) {
            if (mButtonDrawable != null) {
                mButtonDrawable.setCallback(null);
                unscheduleDrawable(mButtonDrawable);
            }
            mButtonDrawable = drawable;
            if (drawable != null) {
                drawable.setCallback(this);
                drawable.setLayoutDirection(getLayoutDirection());
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                drawable.setVisible(getVisibility() == android.view.View.VISIBLE, false);
                setMinHeight(drawable.getIntrinsicHeight());
                applyButtonTint();
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onResolveDrawables(@android.view.View.ResolvedLayoutDir
    int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        if (mButtonDrawable != null) {
            mButtonDrawable.setLayoutDirection(layoutDirection);
        }
    }

    /**
     *
     *
     * @return the drawable used as the compound button image
     * @see #setButtonDrawable(Drawable)
     * @see #setButtonDrawable(int)
     */
    @android.view.inspector.InspectableProperty(name = "button")
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getButtonDrawable() {
        return mButtonDrawable;
    }

    /**
     * Applies a tint to the button drawable. Does not modify the current tint
     * mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setButtonDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#CompoundButton_buttonTint
     * @see #setButtonTintList(ColorStateList)
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setButtonTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mButtonTintList = tint;
        mHasButtonTint = true;
        applyButtonTint();
    }

    /**
     *
     *
     * @return the tint applied to the button drawable
     * @unknown ref android.R.styleable#CompoundButton_buttonTint
     * @see #setButtonTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "buttonTint")
    @android.annotation.Nullable
    public android.content.res.ColorStateList getButtonTintList() {
        return mButtonTintList;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setButtonTintList(ColorStateList)}} to the button drawable. The
     * default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#CompoundButton_buttonTintMode
     * @see #getButtonTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setButtonTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setButtonTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setButtonTintList(ColorStateList)}} to the button drawable. The
     * default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#CompoundButton_buttonTintMode
     * @see #getButtonTintMode()
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setButtonTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode tintMode) {
        mButtonBlendMode = tintMode;
        mHasButtonBlendMode = true;
        applyButtonTint();
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the button drawable
     * @unknown ref android.R.styleable#CompoundButton_buttonTintMode
     * @see #setButtonTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty(name = "buttonTintMode")
    @android.annotation.Nullable
    public android.graphics.PorterDuff.Mode getButtonTintMode() {
        return mButtonBlendMode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mButtonBlendMode) : null;
    }

    /**
     *
     *
     * @return the blending mode used to apply the tint to the button drawable
     * @unknown ref android.R.styleable#CompoundButton_buttonTintMode
     * @see #setButtonTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(name = "buttonBlendMode", attributeId = R.styleable.CompoundButton_buttonTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getButtonTintBlendMode() {
        return mButtonBlendMode;
    }

    private void applyButtonTint() {
        if ((mButtonDrawable != null) && (mHasButtonTint || mHasButtonBlendMode)) {
            mButtonDrawable = mButtonDrawable.mutate();
            if (mHasButtonTint) {
                mButtonDrawable.setTintList(mButtonTintList);
            }
            if (mHasButtonBlendMode) {
                mButtonDrawable.setTintBlendMode(mButtonBlendMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mButtonDrawable.isStateful()) {
                mButtonDrawable.setState(getDrawableState());
            }
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.CompoundButton.class.getName();
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

    @java.lang.Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (!isLayoutRtl()) {
            final android.graphics.drawable.Drawable buttonDrawable = mButtonDrawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth();
            }
        }
        return padding;
    }

    @java.lang.Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight();
        if (isLayoutRtl()) {
            final android.graphics.drawable.Drawable buttonDrawable = mButtonDrawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth();
            }
        }
        return padding;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getHorizontalOffsetForDrawables() {
        final android.graphics.drawable.Drawable buttonDrawable = mButtonDrawable;
        return buttonDrawable != null ? buttonDrawable.getIntrinsicWidth() : 0;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        final android.graphics.drawable.Drawable buttonDrawable = mButtonDrawable;
        if (buttonDrawable != null) {
            final int verticalGravity = getGravity() & android.view.Gravity.VERTICAL_GRAVITY_MASK;
            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth();
            final int top;
            switch (verticalGravity) {
                case android.view.Gravity.BOTTOM :
                    top = getHeight() - drawableHeight;
                    break;
                case android.view.Gravity.CENTER_VERTICAL :
                    top = (getHeight() - drawableHeight) / 2;
                    break;
                default :
                    top = 0;
            }
            final int bottom = top + drawableHeight;
            final int left = (isLayoutRtl()) ? getWidth() - drawableWidth : 0;
            final int right = (isLayoutRtl()) ? getWidth() : drawableWidth;
            buttonDrawable.setBounds(left, top, right, bottom);
            final android.graphics.drawable.Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(left, top, right, bottom);
            }
        }
        super.onDraw(canvas);
        if (buttonDrawable != null) {
            final int scrollX = mScrollX;
            final int scrollY = mScrollY;
            if ((scrollX == 0) && (scrollY == 0)) {
                buttonDrawable.draw(canvas);
            } else {
                canvas.translate(scrollX, scrollY);
                buttonDrawable.draw(canvas);
                canvas.translate(-scrollX, -scrollY);
            }
        }
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            android.view.View.mergeDrawableStates(drawableState, android.widget.CompoundButton.CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final android.graphics.drawable.Drawable buttonDrawable = mButtonDrawable;
        if (((buttonDrawable != null) && buttonDrawable.isStateful()) && buttonDrawable.setState(getDrawableState())) {
            invalidateDrawable(buttonDrawable);
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mButtonDrawable != null) {
            mButtonDrawable.setHotspot(x, y);
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        return super.verifyDrawable(who) || (who == mButtonDrawable);
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mButtonDrawable != null)
            mButtonDrawable.jumpToCurrentState();

    }

    static class SavedState extends android.view.View.BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CompoundButton#onSaveInstanceState()}
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
            return ((("CompoundButton.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " checked=") + checked) + "}";
        }

        @java.lang.SuppressWarnings("hiding")
        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.CompoundButton.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.CompoundButton.SavedState>() {
            @java.lang.Override
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            @java.lang.Override
            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.widget.CompoundButton.SavedState ss = new android.widget.CompoundButton.SavedState(superState);
        ss.checked = isChecked();
        return ss;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.CompoundButton.SavedState ss = ((android.widget.CompoundButton.SavedState) (state));
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
    protected void encodeProperties(@android.annotation.NonNull
    android.view.ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("checked", isChecked());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void onProvideStructure(@android.annotation.NonNull
    android.view.ViewStructure structure, @android.view.View.ViewStructureType
    int viewFor, int flags) {
        super.onProvideStructure(structure, viewFor, flags);
        if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
            structure.setDataIsSensitive(!mCheckedFromResource);
        }
    }

    @java.lang.Override
    public void autofill(android.view.autofill.AutofillValue value) {
        if (!isEnabled())
            return;

        if (!value.isToggle()) {
            android.util.Log.w(android.widget.CompoundButton.LOG_TAG, (value + " could not be autofilled into ") + this);
            return;
        }
        setChecked(value.getToggleValue());
    }

    @java.lang.Override
    @android.view.View.AutofillType
    public int getAutofillType() {
        return isEnabled() ? android.view.View.AUTOFILL_TYPE_TOGGLE : android.view.View.AUTOFILL_TYPE_NONE;
    }

    @java.lang.Override
    public android.view.autofill.AutofillValue getAutofillValue() {
        return isEnabled() ? android.view.autofill.AutofillValue.forToggle(isChecked()) : null;
    }
}

