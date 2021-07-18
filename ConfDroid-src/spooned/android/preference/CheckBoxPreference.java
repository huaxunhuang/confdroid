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
package android.preference;


/**
 * A {@link Preference} that provides checkbox widget
 * functionality.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 *
 * @unknown ref android.R.styleable#CheckBoxPreference_summaryOff
 * @unknown ref android.R.styleable#CheckBoxPreference_summaryOn
 * @unknown ref android.R.styleable#CheckBoxPreference_disableDependentsState
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class CheckBoxPreference extends android.preference.TwoStatePreference {
    public CheckBoxPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckBoxPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.CheckBoxPreference, defStyleAttr, defStyleRes);
        setSummaryOn(a.getString(com.android.internal.R.styleable.CheckBoxPreference_summaryOn));
        setSummaryOff(a.getString(com.android.internal.R.styleable.CheckBoxPreference_summaryOff));
        setDisableDependentsState(a.getBoolean(com.android.internal.R.styleable.CheckBoxPreference_disableDependentsState, false));
        a.recycle();
    }

    public CheckBoxPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.checkBoxPreferenceStyle);
    }

    public CheckBoxPreference(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    protected void onBindView(android.view.View view) {
        super.onBindView(view);
        android.view.View checkboxView = view.findViewById(com.android.internal.R.id.checkbox);
        if ((checkboxView != null) && (checkboxView instanceof android.widget.Checkable)) {
            ((android.widget.Checkable) (checkboxView)).setChecked(mChecked);
        }
        syncSummaryView(view);
    }
}

