/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.design.widget;


/**
 * TabItem is a special 'view' which allows you to declare tab items for a {@link TabLayout}
 * within a layout. This view is not actually added to TabLayout, it is just a dummy which allows
 * setting of a tab items's text, icon and custom layout. See TabLayout for more information on how
 * to use it.
 *
 * @unknown ref android.support.design.R.styleable#TabItem_android_icon
 * @unknown ref android.support.design.R.styleable#TabItem_android_text
 * @unknown ref android.support.design.R.styleable#TabItem_android_layout
 * @see TabLayout
 */
public final class TabItem extends android.view.View {
    final java.lang.CharSequence mText;

    final android.graphics.drawable.Drawable mIcon;

    final int mCustomLayout;

    public TabItem(android.content.Context context) {
        this(context, null);
    }

    public TabItem(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.TabItem);
        mText = a.getText(R.styleable.TabItem_android_text);
        mIcon = a.getDrawable(R.styleable.TabItem_android_icon);
        mCustomLayout = a.getResourceId(R.styleable.TabItem_android_layout, 0);
        a.recycle();
    }
}

