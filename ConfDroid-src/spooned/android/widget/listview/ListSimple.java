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


public class ListSimple extends android.util.ListScenario {
    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setStackFromBottom(false).setStartingSelectionPosition(-1).setNumItems(1000).setItemScreenSizeFactor(0.14);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        getListView().setVerticalScrollBarEnabled(true);
        getListView().setFadingEdgeLength(12);
        getListView().setVerticalFadingEdgeEnabled(true);
    }

    @java.lang.Override
    protected android.view.View createView(int position, android.view.ViewGroup parent, int desiredHeight) {
        android.view.View view = createView(position, parent, desiredHeight);
        view.setBackgroundColor(0xff191919);
        ((android.widget.TextView) (view)).setTextSize(16.0F);
        return view;
    }
}

