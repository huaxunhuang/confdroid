/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.hardware.display;


final class DisplayManagerJellybeanMr1 {
    public static java.lang.Object getDisplayManager(android.content.Context context) {
        return context.getSystemService(android.content.Context.DISPLAY_SERVICE);
    }

    public static android.view.Display getDisplay(java.lang.Object displayManagerObj, int displayId) {
        return ((android.hardware.display.DisplayManager) (displayManagerObj)).getDisplay(displayId);
    }

    public static android.view.Display[] getDisplays(java.lang.Object displayManagerObj) {
        return ((android.hardware.display.DisplayManager) (displayManagerObj)).getDisplays();
    }

    public static android.view.Display[] getDisplays(java.lang.Object displayManagerObj, java.lang.String category) {
        return ((android.hardware.display.DisplayManager) (displayManagerObj)).getDisplays(category);
    }
}

