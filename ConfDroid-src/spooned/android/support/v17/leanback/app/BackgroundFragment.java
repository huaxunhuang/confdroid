/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.app;


/**
 * Fragment used by the background manager.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class BackgroundFragment extends android.app.Fragment implements android.support.v17.leanback.app.BackgroundManager.FragmentStateQueriable {
    private android.support.v17.leanback.app.BackgroundManager mBackgroundManager;

    void setBackgroundManager(android.support.v17.leanback.app.BackgroundManager backgroundManager) {
        mBackgroundManager = backgroundManager;
    }

    android.support.v17.leanback.app.BackgroundManager getBackgroundManager() {
        return mBackgroundManager;
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        // mBackgroundManager might be null:
        // if BackgroundFragment is just restored by FragmentManager,
        // and user does not call BackgroundManager.getInstance() yet.
        if (mBackgroundManager != null) {
            mBackgroundManager.onActivityStart();
        }
    }

    @java.lang.Override
    public void onResume() {
        super.onResume();
        // mBackgroundManager might be null:
        // if BackgroundFragment is just restored by FragmentManager,
        // and user does not call BackgroundManager.getInstance() yet.
        if (mBackgroundManager != null) {
            mBackgroundManager.onResume();
        }
    }

    @java.lang.Override
    public void onDestroy() {
        super.onDestroy();
        // mBackgroundManager might be null:
        // if BackgroundFragment is just restored by FragmentManager,
        // and user does not call BackgroundManager.getInstance() yet.
        if (mBackgroundManager != null) {
            mBackgroundManager.detach();
        }
    }
}

