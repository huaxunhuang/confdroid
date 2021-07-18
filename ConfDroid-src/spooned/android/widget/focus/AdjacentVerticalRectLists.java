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
 * {@link android.view.FocusFinder#findNextFocus(android.view.ViewGroup, android.view.View, int)}
 * and
 * {@link android.view.View#requestFocus(int, android.graphics.Rect)}
 * work together to give a newly focused item a hint about the most interesting
 * rectangle of the previously focused view.  The view taking focus can use this
 * to set an internal selection more appropriate using this rect.
 *
 * This Activity excercises that behavior using three adjacent {@link android.util.InternalSelectionView}
 * that report interesting rects when giving up focus, and use interesting rects
 * when taking focus to best select the internal row to show as selected.
 */
public class AdjacentVerticalRectLists extends android.app.Activity {
    private android.widget.LinearLayout mLayout;

    private android.util.InternalSelectionView mLeftColumn;

    private android.util.InternalSelectionView mMiddleColumn;

    private android.util.InternalSelectionView mRightColumn;

    public android.widget.LinearLayout getLayout() {
        return mLayout;
    }

    public android.util.InternalSelectionView getLeftColumn() {
        return mLeftColumn;
    }

    public android.util.InternalSelectionView getMiddleColumn() {
        return mMiddleColumn;
    }

    public android.util.InternalSelectionView getRightColumn() {
        return mRightColumn;
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        mLayout = new android.widget.LinearLayout(this);
        mLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        mLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mLeftColumn = new android.util.InternalSelectionView(this, 5, "left column");
        mLeftColumn.setLayoutParams(params);
        mLeftColumn.setPadding(10, 10, 10, 10);
        mLayout.addView(mLeftColumn);
        mMiddleColumn = new android.util.InternalSelectionView(this, 5, "middle column");
        mMiddleColumn.setLayoutParams(params);
        mMiddleColumn.setPadding(10, 10, 10, 10);
        mLayout.addView(mMiddleColumn);
        mRightColumn = new android.util.InternalSelectionView(this, 5, "right column");
        mRightColumn.setLayoutParams(params);
        mRightColumn.setPadding(10, 10, 10, 10);
        mLayout.addView(mRightColumn);
        setContentView(mLayout);
    }
}

