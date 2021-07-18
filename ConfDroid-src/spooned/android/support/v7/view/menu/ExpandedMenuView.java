/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.view.menu;


/**
 * The expanded menu view is a list-like menu with all of the available menu items.  It is opened
 * by the user clicking no the 'More' button on the icon menu view.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class ExpandedMenuView extends android.widget.ListView implements android.support.v7.view.menu.MenuBuilder.ItemInvoker , android.support.v7.view.menu.MenuView , android.widget.AdapterView.OnItemClickListener {
    private static final int[] TINT_ATTRS = new int[]{ android.R.attr.background, android.R.attr.divider };

    private android.support.v7.view.menu.MenuBuilder mMenu;

    /**
     * Default animations for this menu
     */
    private int mAnimations;

    public ExpandedMenuView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public ExpandedMenuView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        setOnItemClickListener(this);
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, android.support.v7.view.menu.ExpandedMenuView.TINT_ATTRS, defStyleAttr, 0);
        if (a.hasValue(0)) {
            setBackgroundDrawable(a.getDrawable(0));
        }
        if (a.hasValue(1)) {
            setDivider(a.getDrawable(1));
        }
        a.recycle();
    }

    @java.lang.Override
    public void initialize(android.support.v7.view.menu.MenuBuilder menu) {
        mMenu = menu;
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Clear the cached bitmaps of children
        setChildrenDrawingCacheEnabled(false);
    }

    @java.lang.Override
    public boolean invokeItem(android.support.v7.view.menu.MenuItemImpl item) {
        return mMenu.performItemAction(item, 0);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("rawtypes")
    public void onItemClick(android.widget.AdapterView parent, android.view.View v, int position, long id) {
        invokeItem(((android.support.v7.view.menu.MenuItemImpl) (getAdapter().getItem(position))));
    }

    @java.lang.Override
    public int getWindowAnimations() {
        return mAnimations;
    }
}

