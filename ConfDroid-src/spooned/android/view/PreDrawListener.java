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
package android.view;


/**
 * Tests views with popupWindows becoming invisible
 */
public class PreDrawListener extends android.app.Activity implements android.view.View.OnClickListener {
    private android.view.PreDrawListener.MyLinearLayout mFrame;

    public static class MyLinearLayout extends android.widget.LinearLayout implements android.view.ViewTreeObserver.OnPreDrawListener {
        public boolean mCancelNextDraw;

        public MyLinearLayout(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
        }

        public MyLinearLayout(android.content.Context context) {
            super(context);
        }

        @java.lang.Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            getViewTreeObserver().addOnPreDrawListener(this);
        }

        public boolean onPreDraw() {
            if (mCancelNextDraw) {
                android.widget.Button b = new android.widget.Button(this.getContext());
                b.setText("Hello");
                addView(b, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
                mCancelNextDraw = false;
                return false;
            }
            return true;
        }
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.pre_draw_listener);
        mFrame = ((android.view.PreDrawListener.MyLinearLayout) (findViewById(R.id.frame)));
        android.widget.Button mGoButton = ((android.widget.Button) (findViewById(R.id.go)));
        mGoButton.setOnClickListener(this);
    }

    public void onClick(android.view.View v) {
        mFrame.mCancelNextDraw = true;
        mFrame.invalidate();
    }
}

