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
package android.widget.gridview;


/**
 * A grid with vertical spacing between rows
 */
public class GridSingleColumn extends android.util.GridScenario {
    @java.lang.Override
    protected void init(android.widget.gridview.Params params) {
        params.setStartingSelectionPosition(-1).setMustFillScreen(false).setNumItems(101).setNumColumns(1).setColumnWidth(60).setItemScreenSizeFactor(0.2).setVerticalSpacing(20).setStretchMode(android.widget.GridView.STRETCH_SPACING);
    }
}

