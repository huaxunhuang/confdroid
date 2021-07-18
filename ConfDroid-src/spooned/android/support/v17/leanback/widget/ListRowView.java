/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * ListRowView is a {@link android.view.ViewGroup} which always contains a
 * {@link HorizontalGridView}, and may optionally include a hover card.
 */
public final class ListRowView extends android.widget.LinearLayout {
    private android.support.v17.leanback.widget.HorizontalGridView mGridView;

    public ListRowView(android.content.Context context) {
        this(context, null);
    }

    public ListRowView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListRowView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        inflater.inflate(R.layout.lb_list_row, this);
        mGridView = ((android.support.v17.leanback.widget.HorizontalGridView) (findViewById(R.id.row_content)));
        // since we use WRAP_CONTENT for height in lb_list_row, we need set fixed size to false
        mGridView.setHasFixedSize(false);
        // Uncomment this to experiment with page-based scrolling.
        // mGridView.setFocusScrollStrategy(HorizontalGridView.FOCUS_SCROLL_PAGE);
        setOrientation(android.widget.LinearLayout.VERTICAL);
        setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    /**
     * Returns the HorizontalGridView.
     */
    public android.support.v17.leanback.widget.HorizontalGridView getGridView() {
        return mGridView;
    }
}

