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
 * A {@link Preference} that provides checkbox widget
 * functionality.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 *
 * @unknown name android:summaryOff
 * @unknown name android:summaryOn
 * @unknown name android:disableDependentsState
 */
public class CheckBoxPreference extends android.support.v7.preference.TwoStatePreference {
    private final android.support.v7.preference.CheckBoxPreference.Listener mListener = new android.support.v7.preference.CheckBoxPreference.Listener();

    private class Listener implements android.widget.CompoundButton.OnCheckedChangeListener {
        @java.lang.Override
        public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                return;
            }
            android.support.v7.preference.CheckBoxPreference.this.setChecked(isChecked);
        }
    }

    public CheckBoxPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckBoxPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxPreference, defStyleAttr, defStyleRes);
        setSummaryOn(android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.CheckBoxPreference_summaryOn, R.styleable.CheckBoxPreference_android_summaryOn));
        setSummaryOff(android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.CheckBoxPreference_summaryOff, R.styleable.CheckBoxPreference_android_summaryOff));
        setDisableDependentsState(android.support.v4.content.res.TypedArrayUtils.getBoolean(a, R.styleable.CheckBoxPreference_disableDependentsState, R.styleable.CheckBoxPreference_android_disableDependentsState, false));
        a.recycle();
    }

    public CheckBoxPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v4.content.res.TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle, android.R.attr.checkBoxPreferenceStyle));
    }

    public CheckBoxPreference(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v7.preference.PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        syncCheckboxView(holder.findViewById(android.R.id.checkbox));
        syncSummaryView(holder);
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
        android.view.View checkboxView = view.findViewById(android.R.id.checkbox);
        syncCheckboxView(checkboxView);
        android.view.View summaryView = view.findViewById(android.R.id.summary);
        syncSummaryView(summaryView);
    }

    private void syncCheckboxView(android.view.View view) {
        if (view instanceof android.widget.CompoundButton) {
            ((android.widget.CompoundButton) (view)).setOnCheckedChangeListener(null);
        }
        if (view instanceof android.widget.Checkable) {
            ((android.widget.Checkable) (view)).setChecked(mChecked);
        }
        if (view instanceof android.widget.CompoundButton) {
            ((android.widget.CompoundButton) (view)).setOnCheckedChangeListener(mListener);
        }
    }
}

