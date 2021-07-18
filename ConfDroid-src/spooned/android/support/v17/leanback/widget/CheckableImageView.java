/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * ImageView that supports Checkable states.
 */
class CheckableImageView extends android.widget.ImageView implements android.widget.Checkable {
    private boolean mChecked;

    private static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    public CheckableImageView(android.content.Context context) {
        this(context, null);
    }

    public CheckableImageView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableImageView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @java.lang.Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            android.view.View.mergeDrawableStates(drawableState, android.support.v17.leanback.widget.CheckableImageView.CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @java.lang.Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @java.lang.Override
    public boolean isChecked() {
        return mChecked;
    }

    @java.lang.Override
    public void setChecked(final boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }
}

