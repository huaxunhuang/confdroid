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
package android.support.v7.preference;


/**
 * A version of {@link ListPreference} that presents the options in a
 * drop down menu rather than a dialog.
 */
public class DropDownPreference extends android.support.v7.preference.ListPreference {
    private final android.content.Context mContext;

    private final android.widget.ArrayAdapter<java.lang.String> mAdapter;

    private android.widget.Spinner mSpinner;

    public DropDownPreference(android.content.Context context) {
        this(context, null);
    }

    public DropDownPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.dropdownPreferenceStyle);
    }

    public DropDownPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public DropDownPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mAdapter = createAdapter();
        updateEntries();
    }

    @java.lang.Override
    protected void onClick() {
        mSpinner.performClick();
    }

    @java.lang.Override
    public void setEntries(@android.support.annotation.NonNull
    java.lang.CharSequence[] entries) {
        super.setEntries(entries);
        updateEntries();
    }

    /**
     * By default, this class uses a simple {@link android.widget.ArrayAdapter}. But if you need
     * a more complicated {@link android.widget.ArrayAdapter}, this method can be overridden to
     * create a custom one.
     * <p> Note: This method is called from the constructor. So, overridden methods will get called
     * before any subclass initialization.
     *
     * @return The custom {@link android.widget.ArrayAdapter} that needs to be used with this class.
     */
    protected android.widget.ArrayAdapter createAdapter() {
        return new android.widget.ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item);
    }

    private void updateEntries() {
        mAdapter.clear();
        if (getEntries() != null) {
            for (java.lang.CharSequence c : getEntries()) {
                mAdapter.add(c.toString());
            }
        }
    }

    @java.lang.Override
    public void setValueIndex(int index) {
        setValue(getEntryValues()[index].toString());
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public int findSpinnerIndexOfValue(java.lang.String value) {
        java.lang.CharSequence[] entryValues = getEntryValues();
        if ((value != null) && (entryValues != null)) {
            for (int i = entryValues.length - 1; i >= 0; i--) {
                if (entryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return android.widget.Spinner.INVALID_POSITION;
    }

    @java.lang.Override
    protected void notifyChanged() {
        super.notifyChanged();
        mAdapter.notifyDataSetChanged();
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v7.preference.PreferenceViewHolder view) {
        mSpinner = ((android.widget.Spinner) (view.itemView.findViewById(R.id.spinner)));
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(mItemSelectedListener);
        mSpinner.setSelection(findSpinnerIndexOfValue(getValue()));
        super.onBindViewHolder(view);
    }

    private final android.widget.AdapterView.OnItemSelectedListener mItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {
        @java.lang.Override
        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
            if (position >= 0) {
                java.lang.String value = getEntryValues()[position].toString();
                if ((!value.equals(getValue())) && callChangeListener(value)) {
                    setValue(value);
                }
            }
        }

        @java.lang.Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            // noop
        }
    };
}

