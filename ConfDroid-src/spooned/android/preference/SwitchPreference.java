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
package android.preference;


/**
 * A {@link Preference} that provides a two-state toggleable option.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 *
 * @unknown ref android.R.styleable#SwitchPreference_summaryOff
 * @unknown ref android.R.styleable#SwitchPreference_summaryOn
 * @unknown ref android.R.styleable#SwitchPreference_switchTextOff
 * @unknown ref android.R.styleable#SwitchPreference_switchTextOn
 * @unknown ref android.R.styleable#SwitchPreference_disableDependentsState
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class SwitchPreference extends android.preference.TwoStatePreference {
    @android.annotation.UnsupportedAppUsage
    private final android.preference.SwitchPreference.Listener mListener = new android.preference.SwitchPreference.Listener();

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
            android.preference.SwitchPreference.this.setChecked(isChecked);
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
    public SwitchPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.SwitchPreference, defStyleAttr, defStyleRes);
        setSummaryOn(a.getString(com.android.internal.R.styleable.SwitchPreference_summaryOn));
        setSummaryOff(a.getString(com.android.internal.R.styleable.SwitchPreference_summaryOff));
        setSwitchTextOn(a.getString(com.android.internal.R.styleable.SwitchPreference_switchTextOn));
        setSwitchTextOff(a.getString(com.android.internal.R.styleable.SwitchPreference_switchTextOff));
        setDisableDependentsState(a.getBoolean(com.android.internal.R.styleable.SwitchPreference_disableDependentsState, false));
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
    public SwitchPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
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
    public SwitchPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.switchPreferenceStyle);
    }

    /**
     * Construct a new SwitchPreference with default style options.
     *
     * @param context
     * 		The Context that will style this preference
     */
    public SwitchPreference(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    protected void onBindView(android.view.View view) {
        super.onBindView(view);
        android.view.View checkableView = view.findViewById(com.android.internal.R.id.switch_widget);
        if ((checkableView != null) && (checkableView instanceof android.widget.Checkable)) {
            if (checkableView instanceof android.widget.Switch) {
                final android.widget.Switch switchView = ((android.widget.Switch) (checkableView));
                switchView.setOnCheckedChangeListener(null);
            }
            ((android.widget.Checkable) (checkableView)).setChecked(mChecked);
            if (checkableView instanceof android.widget.Switch) {
                final android.widget.Switch switchView = ((android.widget.Switch) (checkableView));
                switchView.setTextOn(mSwitchOn);
                switchView.setTextOff(mSwitchOff);
                switchView.setOnCheckedChangeListener(mListener);
            }
        }
        syncSummaryView(view);
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
    public void setSwitchTextOn(@android.annotation.StringRes
    int resId) {
        setSwitchTextOn(getContext().getString(resId));
    }

    /**
     * Set the text displayed on the switch widget in the off state.
     * This should be a very short string; one word if possible.
     *
     * @param resId
     * 		The text as a string resource ID
     */
    public void setSwitchTextOff(@android.annotation.StringRes
    int resId) {
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
}

