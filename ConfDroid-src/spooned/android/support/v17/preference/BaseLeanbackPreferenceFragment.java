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
package android.support.v17.preference;


/**
 * This fragment provides a preference fragment with leanback-style behavior, suitable for
 * embedding into broader UI elements.
 */
public abstract class BaseLeanbackPreferenceFragment extends android.support.v14.preference.PreferenceFragment {
    @java.lang.Override
    public android.support.v7.widget.RecyclerView onCreateRecyclerView(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.os.Bundle savedInstanceState) {
        android.support.v17.leanback.widget.VerticalGridView verticalGridView = ((android.support.v17.leanback.widget.VerticalGridView) (inflater.inflate(R.layout.leanback_preferences_list, parent, false)));
        verticalGridView.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_BOTH_EDGE);
        verticalGridView.setFocusScrollStrategy(android.support.v17.leanback.widget.VerticalGridView.FOCUS_SCROLL_ALIGNED);
        verticalGridView.setAccessibilityDelegateCompat(new android.support.v7.preference.PreferenceRecyclerViewAccessibilityDelegate(verticalGridView));
        return verticalGridView;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public android.app.Fragment getCallbackFragment() {
        return getParentFragment();
    }
}

