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
 * Views should obey their background {@link Drawable}'s minimum size
 * requirements ({@link Drawable#getMinimumHeight()} and
 * {@link Drawable#getMinimumWidth()}) when possible.
 * <p>
 * This Activity exercises a few Views with background {@link Drawable}s.
 */
public class DrawableBgMinSize extends android.app.Activity implements android.view.View.OnClickListener {
    private boolean mUsingBigBg = false;

    private android.graphics.drawable.Drawable mBackgroundDrawable;

    private android.graphics.drawable.Drawable mBigBackgroundDrawable;

    private android.widget.Button mChangeBackgroundsButton;

    private android.widget.TextView mTextView;

    private android.widget.LinearLayout mLinearLayout;

    private android.widget.RelativeLayout mRelativeLayout;

    private android.widget.FrameLayout mFrameLayout;

    private android.widget.AbsoluteLayout mAbsoluteLayout;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.drawable_background_minimum_size);
        mBackgroundDrawable = getResources().getDrawable(R.drawable.drawable_background);
        mBigBackgroundDrawable = getResources().getDrawable(R.drawable.big_drawable_background);
        mChangeBackgroundsButton = ((android.widget.Button) (findViewById(R.id.change_backgrounds)));
        mChangeBackgroundsButton.setOnClickListener(this);
        mTextView = ((android.widget.TextView) (findViewById(R.id.text_view)));
        mLinearLayout = ((android.widget.LinearLayout) (findViewById(R.id.linear_layout)));
        mRelativeLayout = ((android.widget.RelativeLayout) (findViewById(R.id.relative_layout)));
        mFrameLayout = ((android.widget.FrameLayout) (findViewById(R.id.frame_layout)));
        mAbsoluteLayout = ((android.widget.AbsoluteLayout) (findViewById(R.id.absolute_layout)));
        changeBackgrounds(mBackgroundDrawable);
    }

    private void changeBackgrounds(android.graphics.drawable.Drawable newBg) {
        mTextView.setBackgroundDrawable(newBg);
        mLinearLayout.setBackgroundDrawable(newBg);
        mRelativeLayout.setBackgroundDrawable(newBg);
        mFrameLayout.setBackgroundDrawable(newBg);
        mAbsoluteLayout.setBackgroundDrawable(newBg);
    }

    public void onClick(android.view.View v) {
        if (mUsingBigBg) {
            changeBackgrounds(mBackgroundDrawable);
        } else {
            changeBackgrounds(mBigBackgroundDrawable);
        }
        mUsingBigBg = !mUsingBigBg;
    }
}

