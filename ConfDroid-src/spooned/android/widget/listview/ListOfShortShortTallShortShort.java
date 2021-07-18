/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget.listview;


/**
 * Exposes fading in and out multiple items.
 */
public class ListOfShortShortTallShortShort extends android.util.ListScenario {
    protected void init(android.widget.listview.Params params) {
        params.setNumItems(5).setItemScreenSizeFactor(0.1).setFadingEdgeScreenSizeFactor(0.22).setPositionScreenSizeFactorOverride(2, 1.1);
    }
}

