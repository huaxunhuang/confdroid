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
package android.support.v4.view.accessibility;


/**
 * ICS specific AccessibilityEvent API implementation.
 */
class AccessibilityEventCompatIcs {
    public static int getRecordCount(android.view.accessibility.AccessibilityEvent event) {
        return event.getRecordCount();
    }

    public static void appendRecord(android.view.accessibility.AccessibilityEvent event, java.lang.Object record) {
        event.appendRecord(((android.view.accessibility.AccessibilityRecord) (record)));
    }

    public static java.lang.Object getRecord(android.view.accessibility.AccessibilityEvent event, int index) {
        return event.getRecord(index);
    }

    public static void setScrollable(android.view.accessibility.AccessibilityEvent event, boolean scrollable) {
        event.setScrollable(scrollable);
    }
}

