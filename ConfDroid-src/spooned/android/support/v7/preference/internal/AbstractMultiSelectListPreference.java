/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v7.preference.internal;


/**
 * Stub superclass for {@link android.support.v14.preference.MultiSelectListPreference} so that we
 * can reference it from
 * {@link android.support.v7.preference.MultiSelectListPreferenceDialogFragmentCompat}
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public abstract class AbstractMultiSelectListPreference extends android.support.v7.preference.DialogPreference {
    public AbstractMultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AbstractMultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbstractMultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractMultiSelectListPreference(android.content.Context context) {
        super(context);
    }

    public abstract java.lang.CharSequence[] getEntries();

    public abstract java.util.Set<java.lang.String> getValues();

    public abstract java.lang.CharSequence[] getEntryValues();

    public abstract void setValues(java.util.Set<java.lang.String> values);
}

