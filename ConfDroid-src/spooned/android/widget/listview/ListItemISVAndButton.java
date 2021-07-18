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
 * Each item is an internal selection view, a button, and some filler
 */
public class ListItemISVAndButton extends android.util.ListScenario {
    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setItemScreenSizeFactor(2.0).setNumItems(3).setItemsFocusable(true);
    }

    @java.lang.Override
    protected android.view.View createView(int position, android.view.ViewGroup parent, int desiredHeight) {
        android.content.Context context = parent.getContext();
        final android.widget.LinearLayout ll = new android.widget.LinearLayout(context);
        ll.setOrientation(android.widget.LinearLayout.VERTICAL);
        final android.util.InternalSelectionView isv = new android.util.InternalSelectionView(context, 8, "ISV postion " + position);
        isv.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, desiredHeight - 240));
        ll.addView(isv);
        final android.widget.LinearLayout.LayoutParams buttonLp = new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 40);
        final android.widget.Button topButton = new android.widget.Button(context);
        topButton.setLayoutParams(buttonLp);
        topButton.setText(("button " + position) + ")");
        ll.addView(topButton);
        final android.widget.TextView filler = new android.widget.TextView(context);
        filler.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 200));
        filler.setText("filler");
        ll.addView(filler);
        return ll;
    }
}

