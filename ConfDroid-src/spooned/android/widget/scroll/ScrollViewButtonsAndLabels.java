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
package android.widget.scroll;


/**
 * Basic scroll view example
 */
public class ScrollViewButtonsAndLabels extends android.app.Activity {
    private android.widget.ScrollView mScrollView;

    private android.widget.LinearLayout mLinearLayout;

    private int mNumGroups = 10;

    public android.widget.ScrollView getScrollView() {
        return mScrollView;
    }

    public android.widget.LinearLayout getLinearLayout() {
        return mLinearLayout;
    }

    public int getNumButtons() {
        return mNumGroups;
    }

    public android.widget.Button getButton(int groupNum) {
        if (groupNum > mNumGroups) {
            throw new java.lang.IllegalArgumentException("groupNum > " + mNumGroups);
        }
        return ((android.widget.Button) (mLinearLayout.getChildAt(2 * groupNum)));
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.scrollview_linear_layout);
        // estimated ratio to get enough buttons so a couple are off screen
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        mNumGroups = screenHeight / 30;
        mScrollView = ((android.widget.ScrollView) (findViewById(R.id.scrollView)));
        mLinearLayout = ((android.widget.LinearLayout) (findViewById(R.id.layout)));
        android.widget.LinearLayout.LayoutParams p = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mNumGroups; i++) {
            // want button to be first and last
            if (i > 0) {
                android.widget.TextView textView = new android.widget.TextView(this);
                textView.setText("Text View " + i);
                mLinearLayout.addView(textView, p);
            }
            android.widget.Button button = new android.widget.Button(this);
            button.setText("Button " + (i + 1));
            mLinearLayout.addView(button, p);
        }
    }
}

