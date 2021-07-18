/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v17.leanback.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class Util {
    /**
     * Returns true if child == parent or is descendant of the parent.
     */
    public static boolean isDescendant(android.view.ViewGroup parent, android.view.View child) {
        while (child != null) {
            if (child == parent) {
                return true;
            }
            android.view.ViewParent p = child.getParent();
            if (!(p instanceof android.view.View)) {
                return false;
            }
            child = ((android.view.View) (p));
        } 
        return false;
    }
}

