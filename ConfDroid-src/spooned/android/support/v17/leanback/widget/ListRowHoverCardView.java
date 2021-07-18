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
 * ListRowHoverCardView contains a title and description.
 */
public final class ListRowHoverCardView extends android.widget.LinearLayout {
    private final android.widget.TextView mTitleView;

    private final android.widget.TextView mDescriptionView;

    public ListRowHoverCardView(android.content.Context context) {
        this(context, null);
    }

    public ListRowHoverCardView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListRowHoverCardView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        inflater.inflate(R.layout.lb_list_row_hovercard, this);
        mTitleView = ((android.widget.TextView) (findViewById(R.id.title)));
        mDescriptionView = ((android.widget.TextView) (findViewById(R.id.description)));
    }

    /**
     * Returns the title text.
     */
    public final java.lang.CharSequence getTitle() {
        return mTitleView.getText();
    }

    /**
     * Sets the title text.
     */
    public final void setTitle(java.lang.CharSequence text) {
        if (!android.text.TextUtils.isEmpty(text)) {
            mTitleView.setText(text);
            mTitleView.setVisibility(android.view.View.VISIBLE);
        } else {
            mTitleView.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Returns the description text.
     */
    public final java.lang.CharSequence getDescription() {
        return mDescriptionView.getText();
    }

    /**
     * Sets the description text.
     */
    public final void setDescription(java.lang.CharSequence text) {
        if (!android.text.TextUtils.isEmpty(text)) {
            mDescriptionView.setText(text);
            mDescriptionView.setVisibility(android.view.View.VISIBLE);
        } else {
            mDescriptionView.setVisibility(android.view.View.GONE);
        }
    }
}

