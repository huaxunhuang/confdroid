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
 * limitations under the License.
 */
package android.view;


public class PointerIcon_Delegate {
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void loadResource(android.view.PointerIcon icon, android.content.Context context, android.content.res.Resources resources, int resourceId) {
        // HACK: This bypasses the problem of having an enum resolved as a resourceId.
        // PointerIcon would not be displayed by layoutlib anyway, so we always return the null
        // icon.
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void registerDisplayListener(android.content.Context context) {
        // Ignore this as we do not have a DisplayManager
    }
}

