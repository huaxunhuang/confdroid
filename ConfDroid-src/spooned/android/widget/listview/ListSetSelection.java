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
 * List of 1,000 items used to test calls to setSelection() in touch mode.
 * Pressing the S key will call setSelection(0) on the list.
 */
public class ListSetSelection extends android.util.ListScenario {
    private android.widget.Button mButton;

    @java.lang.Override
    protected void init(android.widget.listview.Params params) {
        params.setStackFromBottom(false).setStartingSelectionPosition(-1).setNumItems(1000).setItemScreenSizeFactor(0.22);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mButton = new android.widget.Button(this);
        mButton.setText("setSelection(0)");
        mButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                getListView().setSelection(0);
            }
        });
        getListViewContainer().addView(mButton, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public android.widget.Button getButton() {
        return mButton;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (event.getKeyCode() == android.view.KeyEvent.KEYCODE_S) {
            getListView().setSelection(0);
            return true;
        }
        return dispatchKeyEvent(event);
    }
}

