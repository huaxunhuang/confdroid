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
 * Used to group {@link Preference} objects and provide a disabled title above
 * the group.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 */
public class PreferenceCategory extends android.support.v7.preference.PreferenceGroup {
    private static final java.lang.String TAG = "PreferenceCategory";

    public PreferenceCategory(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceCategory(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCategory(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v4.content.res.TypedArrayUtils.getAttr(context, R.attr.preferenceCategoryStyle, android.R.attr.preferenceCategoryStyle));
    }

    public PreferenceCategory(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    protected boolean onPrepareAddPreference(android.support.v7.preference.Preference preference) {
        if (preference instanceof android.support.v7.preference.PreferenceCategory) {
            throw new java.lang.IllegalArgumentException((("Cannot add a " + android.support.v7.preference.PreferenceCategory.TAG) + " directly to a ") + android.support.v7.preference.PreferenceCategory.TAG);
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

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(info);
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat existingItemInfo = info.getCollectionItemInfo();
        if (existingItemInfo == null) {
            return;
        }
        final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat newItemInfo = /* heading */
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(existingItemInfo.getRowIndex(), existingItemInfo.getRowSpan(), existingItemInfo.getColumnIndex(), existingItemInfo.getColumnSpan(), true, existingItemInfo.isSelected());
        info.setCollectionItemInfo(newItemInfo);
    }
}

