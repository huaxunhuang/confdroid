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
 * Displays checked/unchecked states as a button
 * with a "light" indicator and by default accompanied with the text "ON" or "OFF".
 *
 * <p>See the <a href="{@docRoot }guide/topics/ui/controls/togglebutton.html">Toggle Buttons</a>
 * guide.</p>
 *
 * @unknown ref android.R.styleable#ToggleButton_textOn
 * @unknown ref android.R.styleable#ToggleButton_textOff
 * @unknown ref android.R.styleable#ToggleButton_disabledAlpha
 */
public class ToggleButton extends android.widget.CompoundButton {
    private java.lang.CharSequence mTextOn;

    private java.lang.CharSequence mTextOff;

    private android.graphics.drawable.Drawable mIndicatorDrawable;

    private static final int NO_ALPHA = 0xff;

    private float mDisabledAlpha;

    public ToggleButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ToggleButton, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.ToggleButton, attrs, a, defStyleAttr, defStyleRes);
        mTextOn = a.getText(com.android.internal.R.styleable.ToggleButton_textOn);
        mTextOff = a.getText(com.android.internal.R.styleable.ToggleButton_textOff);
        mDisabledAlpha = a.getFloat(com.android.internal.R.styleable.ToggleButton_disabledAlpha, 0.5F);
        syncTextState();
        a.recycle();
    }

    public ToggleButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ToggleButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.buttonStyleToggle);
    }

    public ToggleButton(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        syncTextState();
    }

    private void syncTextState() {
        boolean checked = isChecked();
        if (checked && (mTextOn != null)) {
            setText(mTextOn);
        } else
            if ((!checked) && (mTextOff != null)) {
                setText(mTextOff);
            }

    }

    /**
     * Returns the text for when the button is in the checked state.
     *
     * @return The text.
     */
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getTextOn() {
        return mTextOn;
    }

    /**
     * Sets the text for when the button is in the checked state.
     *
     * @param textOn
     * 		The text.
     */
    public void setTextOn(java.lang.CharSequence textOn) {
        mTextOn = textOn;
    }

    /**
     * Returns the text for when the button is not in the checked state.
     *
     * @return The text.
     */
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getTextOff() {
        return mTextOff;
    }

    /**
     * Sets the text for when the button is not in the checked state.
     *
     * @param textOff
     * 		The text.
     */
    public void setTextOff(java.lang.CharSequence textOff) {
        mTextOff = textOff;
    }

    /**
     * Returns the alpha value of the button when it is disabled
     *
     * @return the alpha value, 0.0-1.0
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.FloatRange(from = 0.0, to = 1.0)
    public float getDisabledAlpha() {
        return mDisabledAlpha;
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateReferenceToIndicatorDrawable(getBackground());
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable d) {
        super.setBackgroundDrawable(d);
        updateReferenceToIndicatorDrawable(d);
    }

    private void updateReferenceToIndicatorDrawable(android.graphics.drawable.Drawable backgroundDrawable) {
        if (backgroundDrawable instanceof android.graphics.drawable.LayerDrawable) {
            android.graphics.drawable.LayerDrawable layerDrawable = ((android.graphics.drawable.LayerDrawable) (backgroundDrawable));
            mIndicatorDrawable = layerDrawable.findDrawableByLayerId(com.android.internal.R.id.toggle);
        } else {
            mIndicatorDrawable = null;
        }
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mIndicatorDrawable != null) {
            mIndicatorDrawable.setAlpha(isEnabled() ? android.widget.ToggleButton.NO_ALPHA : ((int) (android.widget.ToggleButton.NO_ALPHA * mDisabledAlpha)));
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ToggleButton.class.getName();
    }
}

