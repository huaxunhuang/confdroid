/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.app;


/**
 * Wrapper to adapt the ActionBar.OnNavigationListener in an AdapterView.OnItemSelectedListener
 * for use in Spinner widgets. Used by action bar implementations.
 */
class NavItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
    private final android.support.v7.app.ActionBar.OnNavigationListener mListener;

    public NavItemSelectedListener(android.support.v7.app.ActionBar.OnNavigationListener listener) {
        mListener = listener;
    }

    @java.lang.Override
    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        if (mListener != null) {
            mListener.onNavigationItemSelected(position, id);
        }
    }

    @java.lang.Override
    public void onNothingSelected(android.widget.AdapterView<?> parent) {
        // Do nothing
    }
}

