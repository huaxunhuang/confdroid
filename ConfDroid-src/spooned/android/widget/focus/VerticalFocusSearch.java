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


/**
 * Holds a few buttons of various sizes and horizontal placements in a
 * vertical layout to excercise some core focus searching.
 */
public class VerticalFocusSearch extends android.app.Activity {
    private android.widget.LinearLayout mLayout;

    private android.widget.Button mTopWide;

    private android.widget.Button mMidSkinny1Left;

    private android.widget.Button mBottomWide;

    private android.widget.Button mMidSkinny2Right;

    public android.widget.LinearLayout getLayout() {
        return mLayout;
    }

    public android.widget.Button getTopWide() {
        return mTopWide;
    }

    public android.widget.Button getMidSkinny1Left() {
        return mMidSkinny1Left;
    }

    public android.widget.Button getMidSkinny2Right() {
        return mMidSkinny2Right;
    }

    public android.widget.Button getBottomWide() {
        return mBottomWide;
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mLayout = new android.widget.LinearLayout(this);
        mLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
        mLayout.setHorizontalGravity(android.view.Gravity.START);
        mLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mTopWide = makeWide("top wide");
        mLayout.addView(mTopWide);
        mMidSkinny1Left = addSkinny(mLayout, "mid skinny 1(L)", false);
        mMidSkinny2Right = addSkinny(mLayout, "mid skinny 2(R)", true);
        mBottomWide = makeWide("bottom wide");
        mLayout.addView(mBottomWide);
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

    private android.widget.Button makeWide(java.lang.String label) {
        android.widget.Button button = new android.widget.focus.VerticalFocusSearch.MyButton(this);
        button.setText(label);
        button.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        return button;
    }

    /**
     * Add a skinny button that takes up just less than half of the screen
     * horizontally.
     *
     * @param root
     * 		The layout to add the button to.
     * @param label
     * 		The label of the button.
     * @param atRight
     * 		Which side to put the button on.
     * @return The newly created button.
     */
    private android.widget.Button addSkinny(android.widget.LinearLayout root, java.lang.String label, boolean atRight) {
        android.widget.Button button = new android.widget.focus.VerticalFocusSearch.MyButton(this);
        button.setText(label);
        button.setLayoutParams(// width
        new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 480));
        android.widget.TextView filler = new android.widget.TextView(this);
        filler.setText("filler");
        filler.setLayoutParams(// width
        new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 520));
        android.widget.LinearLayout ll = new android.widget.LinearLayout(this);
        ll.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        ll.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        if (atRight) {
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

