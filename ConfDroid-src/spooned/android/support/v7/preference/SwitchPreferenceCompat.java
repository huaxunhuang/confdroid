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
package android.support.v7.preference;


/**
 * A {@link Preference} that provides a two-state toggleable option.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 *
 * @unknown name android:summaryOff
 * @unknown name android:summaryOn
 * @unknown name android:switchTextOff
 * @unknown name android:switchTextOn
 * @unknown name android:disableDependentsState
 */
public class SwitchPreferenceCompat extends android.support.v7.preference.TwoStatePreference {
    private final android.support.v7.preference.SwitchPreferenceCompat.Listener mListener = new android.support.v7.preference.SwitchPreferenceCompat.Listener();

    // Switch text for on and off states
    private java.lang.CharSequence mSwitchOn;

    private java.lang.CharSequence mSwitchOff;

    private class Listener implements android.widget.CompoundButton.OnCheckedChangeListener {
        @java.lang.Override
        public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                return;
            }
            android.support.v7.preference.SwitchPreferenceCompat.this.setChecked(isChecked);
        }
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context
     * 		The Context that will style this preference
     * @param attrs
     * 		Style attributes that differ from the default
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     */
    public SwitchPreferenceCompat(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchPreferenceCompat, defStyleAttr, defStyleRes);
        setSummaryOn(android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.SwitchPreferenceCompat_summaryOn, R.styleable.SwitchPreferenceCompat_android_summaryOn));
        setSummaryOff(android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.SwitchPreferenceCompat_summaryOff, R.styleable.SwitchPreferenceCompat_android_summaryOff));
        setSwitchTextOn(android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.SwitchPreferenceCompat_switchTextOn, R.styleable.SwitchPreferenceCompat_android_switchTextOn));
        setSwitchTextOff(android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.SwitchPreferenceCompat_switchTextOff, R.styleable.SwitchPreferenceCompat_android_switchTextOff));
        setDisableDependentsState(android.support.v4.content.res.TypedArrayUtils.getBoolean(a, R.styleable.SwitchPreferenceCompat_disableDependentsState, R.styleable.SwitchPreferenceCompat_android_disableDependentsState, false));
        a.recycle();
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context
     * 		The Context that will style this preference
     * @param attrs
     * 		Style attributes that differ from the default
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public SwitchPreferenceCompat(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context
     * 		The Context that will style this preference
     * @param attrs
     * 		Style attributes that differ from the default
     */
    public SwitchPreferenceCompat(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.switchPreferenceCompatStyle);
    }

    /**
     * Construct a new SwitchPreference with default style options.
     *
     * @param context
     * 		The Context that will style this preference
     */
    public SwitchPreferenceCompat(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v7.preference.PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        android.view.View switchView = holder.findViewById(R.id.switchWidget);
        syncSwitchView(switchView);
        syncSummaryView(holder);
    }

    /**
     * Set the text displayed on the switch widget in the on state.
     * This should be a very short string; one word if possible.
     *
     * @param onText
     * 		Text to display in the on state
     */
    public void setSwitchTextOn(java.lang.CharSequence onText) {
        mSwitchOn = onText;
        notifyChanged();
    }

    /**
     * Set the text displayed on the switch widget in the off state.
     * This should be a very short string; one word if possible.
     *
     * @param offText
     * 		Text to display in the off state
     */
    public void setSwitchTextOff(java.lang.CharSequence offText) {
        mSwitchOff = offText;
        notifyChanged();
    }

    /**
     * Set the text displayed on the switch widget in the on state.
     * This should be a very short string; one word if possible.
     *
     * @param resId
     * 		The text as a string resource ID
     */
    public void setSwitchTextOn(int resId) {
        setSwitchTextOn(getContext().getString(resId));
    }

    /**
     * Set the text displayed on the switch widget in the off state.
     * This should be a very short string; one word if possible.
     *
     * @param resId
     * 		The text as a string resource ID
     */
    public void setSwitchTextOff(int resId) {
        setSwitchTextOff(getContext().getString(resId));
    }

    /**
     *
     *
     * @return The text that will be displayed on the switch widget in the on state
     */
    public java.lang.CharSequence getSwitchTextOn() {
        return mSwitchOn;
    }

    /**
     *
     *
     * @return The text that will be displayed on the switch widget in the off state
     */
    public java.lang.CharSequence getSwitchTextOff() {
        return mSwitchOff;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    protected void performClick(android.view.View view) {
        super.performClick(view);
        syncViewIfAccessibilityEnabled(view);
    }

    private void syncViewIfAccessibilityEnabled(android.view.View view) {
        android.view.accessibility.AccessibilityManager accessibilityManager = ((android.view.accessibility.AccessibilityManager) (getContext().getSystemService(android.content.Context.ACCESSIBILITY_SERVICE)));
        if (!accessibilityManager.isEnabled()) {
            return;
        }
        android.view.View switchView = view.findViewById(R.id.switchWidget);
        syncSwitchView(switchView);
        android.view.View summaryView = view.findViewById(android.R.id.summary);
        syncSummaryView(summaryView);
    }

    private void syncSwitchView(android.view.View view) {
        if (view instanceof android.support.v7.widget.SwitchCompat) {
            final android.support.v7.widget.SwitchCompat switchView = ((android.support.v7.widget.SwitchCompat) (view));
            switchView.setOnCheckedChangeListener(null);
        }
        if (view instanceof android.widget.Checkable) {
            ((android.widget.Checkable) (view)).setChecked(mChecked);
        }
        if (view instanceof android.support.v7.widget.SwitchCompat) {
            final android.support.v7.widget.SwitchCompat switchView = ((android.support.v7.widget.SwitchCompat) (view));
            switchView.setTextOn(mSwitchOn);
            switchView.setTextOff(mSwitchOff);
            switchView.setOnCheckedChangeListener(mListener);
        }
    }
}

