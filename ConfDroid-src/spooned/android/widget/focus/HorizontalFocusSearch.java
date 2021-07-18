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
package android.widget.focus;


public class HorizontalFocusSearch extends android.app.Activity {
    private android.widget.LinearLayout mLayout;

    private android.widget.Button mLeftTall;

    private android.widget.Button mMidShort1Top;

    private android.widget.Button mMidShort2Bottom;

    private android.widget.Button mRightTall;

    public android.widget.LinearLayout getLayout() {
        return mLayout;
    }

    public android.widget.Button getLeftTall() {
        return mLeftTall;
    }

    public android.widget.Button getMidShort1Top() {
        return mMidShort1Top;
    }

    public android.widget.Button getMidShort2Bottom() {
        return mMidShort2Bottom;
    }

    public android.widget.Button getRightTall() {
        return mRightTall;
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mLayout = new android.widget.LinearLayout(this);
        mLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        mLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mLeftTall = makeTall("left tall");
        mLayout.addView(mLeftTall);
        mMidShort1Top = addShort(mLayout, "mid(1) top", false);
        mMidShort2Bottom = addShort(mLayout, "mid(2) bottom", true);
        mRightTall = makeTall("right tall");
        mLayout.addView(mRightTall);
        setContentView(mLayout);
    }

    // just to get toString non-sucky
    private static class MyButton extends android.widget.Button {
        public MyButton(android.content.Context context) {
            super(context);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return getText().toString();
        }
    }

    private android.widget.Button makeTall(java.lang.String label) {
        android.widget.Button button = new android.widget.focus.HorizontalFocusSearch.MyButton(this);
        button.setText(label);
        button.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        return button;
    }

    private android.widget.Button addShort(android.widget.LinearLayout root, java.lang.String label, boolean atBottom) {
        android.widget.Button button = new android.widget.focus.HorizontalFocusSearch.MyButton(this);
        button.setText(label);
        button.setLayoutParams(// height
        new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0, 490));
        android.widget.TextView filler = new android.widget.TextView(this);
        filler.setText("filler");
        filler.setLayoutParams(// height
        new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0, 510));
        android.widget.LinearLayout ll = new android.widget.LinearLayout(this);
        ll.setOrientation(android.widget.LinearLayout.VERTICAL);
        ll.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        if (atBottom) {
            ll.addView(filler);
            ll.addView(button);
            root.addView(ll);
        } else {
            ll.addView(button);
            ll.addView(filler);
            root.addView(ll);
        }
        return button;
    }
}

