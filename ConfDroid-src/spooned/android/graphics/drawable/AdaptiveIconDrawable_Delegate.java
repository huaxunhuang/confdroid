/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.graphics.drawable;


public class AdaptiveIconDrawable_Delegate {
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void constructor_after(android.graphics.drawable.AdaptiveIconDrawable icon) {
        java.lang.String pathString = android.content.res.Resources_Delegate.getLayoutlibCallback(android.content.res.Resources.getSystem()).getFlag(android.graphics.drawable.FLAG_KEY_ADAPTIVE_ICON_MASK_PATH);
        if (pathString != null) {
            AdaptiveIconDrawable.sMask = android.util.PathParser.createPathFromPathData(pathString);
        }
    }
}

