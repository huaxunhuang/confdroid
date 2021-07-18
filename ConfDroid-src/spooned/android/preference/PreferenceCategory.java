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
 * Used to group {@link Preference} objects
 * and provide a disabled title above the group.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class PreferenceCategory extends android.preference.PreferenceGroup {
    private static final java.lang.String TAG = "PreferenceCategory";

    public PreferenceCategory(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceCategory(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCategory(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.preferenceCategoryStyle);
    }

    public PreferenceCategory(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    protected boolean onPrepareAddPreference(android.preference.Preference preference) {
        if (preference instanceof android.preference.PreferenceCategory) {
            throw new java.lang.IllegalArgumentException((("Cannot add a " + android.preference.PreferenceCategory.TAG) + " directly to a ") + android.preference.PreferenceCategory.TAG);
        }
        return super.onPrepareAddPreference(preference);
    }

    @java.lang.Override
    public boolean isEnabled() {
        return false;
    }

    @java.lang.Override
    public boolean shouldDisableDependents() {
        return !super.isEnabled();
    }
}

