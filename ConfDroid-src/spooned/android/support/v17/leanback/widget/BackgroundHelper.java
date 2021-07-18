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
package android.support.v17.leanback.widget;


/**
 * Helper for view backgrounds.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class BackgroundHelper {
    static final android.support.v17.leanback.widget.BackgroundHelper.BackgroundHelperVersionImpl sImpl;

    interface BackgroundHelperVersionImpl {
        void setBackgroundPreservingAlpha(android.view.View view, android.graphics.drawable.Drawable drawable);
    }

    private static final class BackgroundHelperStubImpl implements android.support.v17.leanback.widget.BackgroundHelper.BackgroundHelperVersionImpl {
        BackgroundHelperStubImpl() {
        }

        @java.lang.Override
        public void setBackgroundPreservingAlpha(android.view.View view, android.graphics.drawable.Drawable drawable) {
            // Cannot query drawable alpha
            view.setBackground(drawable);
        }
    }

    private static final class BackgroundHelperKitkatImpl implements android.support.v17.leanback.widget.BackgroundHelper.BackgroundHelperVersionImpl {
        BackgroundHelperKitkatImpl() {
        }

        @java.lang.Override
        public void setBackgroundPreservingAlpha(android.view.View view, android.graphics.drawable.Drawable drawable) {
            android.support.v17.leanback.widget.BackgroundHelperKitkat.setBackgroundPreservingAlpha(view, drawable);
        }
    }

    private BackgroundHelper() {
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            sImpl = new android.support.v17.leanback.widget.BackgroundHelper.BackgroundHelperKitkatImpl();
        } else {
            sImpl = new android.support.v17.leanback.widget.BackgroundHelper.BackgroundHelperStubImpl();
        }
    }

    public static void setBackgroundPreservingAlpha(android.view.View view, android.graphics.drawable.Drawable drawable) {
        android.support.v17.leanback.widget.BackgroundHelper.sImpl.setBackgroundPreservingAlpha(view, drawable);
    }
}

