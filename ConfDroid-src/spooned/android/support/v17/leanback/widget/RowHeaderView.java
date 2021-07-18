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
 * RowHeaderView is a header text view.
 */
public final class RowHeaderView extends android.widget.TextView {
    public RowHeaderView(android.content.Context context) {
        this(context, null);
    }

    public RowHeaderView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.rowHeaderStyle);
    }

    public RowHeaderView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}

