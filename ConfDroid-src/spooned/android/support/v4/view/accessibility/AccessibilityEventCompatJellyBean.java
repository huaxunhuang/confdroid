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
package android.support.v4.view.accessibility;


class AccessibilityEventCompatJellyBean {
    public static void setMovementGranularity(android.view.accessibility.AccessibilityEvent event, int granularity) {
        event.setMovementGranularity(granularity);
    }

    public static int getMovementGranularity(android.view.accessibility.AccessibilityEvent event) {
        return event.getMovementGranularity();
    }

    public static void setAction(android.view.accessibility.AccessibilityEvent event, int action) {
        event.setAction(action);
    }

    public static int getAction(android.view.accessibility.AccessibilityEvent event) {
        return event.getAction();
    }
}

