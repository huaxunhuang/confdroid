/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.view;


/**
 * Basic implementation of {@link IWindowManager} so that {@link Display} (and
 * {@link Display_Delegate}) can return a valid instance.
 */
public class IWindowManagerImpl extends android.view.IWindowManager.Default {
    private final android.content.res.Configuration mConfig;

    private final android.util.DisplayMetrics mMetrics;

    private final int mRotation;

    private final boolean mHasNavigationBar;

    public IWindowManagerImpl(android.content.res.Configuration config, android.util.DisplayMetrics metrics, int rotation, boolean hasNavigationBar) {
        mConfig = config;
        mMetrics = metrics;
        mRotation = rotation;
        mHasNavigationBar = hasNavigationBar;
    }

    // custom API.
    public android.util.DisplayMetrics getMetrics() {
        return mMetrics;
    }

    // ---- implementation of IWindowManager that we care about ----
    @java.lang.Override
    public int getDefaultDisplayRotation() throws android.os.RemoteException {
        return mRotation;
    }

    @java.lang.Override
    public boolean hasNavigationBar(int displayId) {
        // TODO(multi-display): Change it once we need it per display.
        return mHasNavigationBar;
    }
}

