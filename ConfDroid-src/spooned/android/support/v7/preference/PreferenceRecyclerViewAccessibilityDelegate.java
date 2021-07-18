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
package android.support.v7.preference;


/**
 * The AccessibilityDelegate used by the RecyclerView that displays Views for Preferences.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class PreferenceRecyclerViewAccessibilityDelegate extends android.support.v7.widget.RecyclerViewAccessibilityDelegate {
    final android.support.v7.widget.RecyclerView mRecyclerView;

    final android.support.v4.view.AccessibilityDelegateCompat mDefaultItemDelegate = super.getItemDelegate();

    public PreferenceRecyclerViewAccessibilityDelegate(android.support.v7.widget.RecyclerView recyclerView) {
        super(recyclerView);
        mRecyclerView = recyclerView;
    }

    @java.lang.Override
    public android.support.v4.view.AccessibilityDelegateCompat getItemDelegate() {
        return mItemDelegate;
    }

    final android.support.v4.view.AccessibilityDelegateCompat mItemDelegate = new android.support.v4.view.AccessibilityDelegateCompat() {
        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            mDefaultItemDelegate.onInitializeAccessibilityNodeInfo(host, info);
            int position = mRecyclerView.getChildAdapterPosition(host);
            android.support.v7.widget.RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            if (!(adapter instanceof android.support.v7.preference.PreferenceGroupAdapter)) {
                return;
            }
            android.support.v7.preference.PreferenceGroupAdapter preferenceGroupAdapter = ((android.support.v7.preference.PreferenceGroupAdapter) (adapter));
            android.support.v7.preference.Preference preference = preferenceGroupAdapter.getItem(position);
            if (preference == null) {
                return;
            }
            preference.onInitializeAccessibilityNodeInfo(info);
        }

        @java.lang.Override
        public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args) {
            // Must forward actions since the default delegate will handle actions.
            return mDefaultItemDelegate.performAccessibilityAction(host, action, args);
        }
    };
}

