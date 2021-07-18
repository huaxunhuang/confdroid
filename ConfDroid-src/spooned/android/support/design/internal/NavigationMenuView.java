/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class NavigationMenuView extends android.support.v7.widget.RecyclerView implements android.support.v7.view.menu.MenuView {
    public NavigationMenuView(android.content.Context context) {
        this(context, null);
    }

    public NavigationMenuView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationMenuView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutManager(new android.support.v7.widget.LinearLayoutManager(context, android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
    }

    @java.lang.Override
    public void initialize(android.support.v7.view.menu.MenuBuilder menu) {
    }

    @java.lang.Override
    public int getWindowAnimations() {
        return 0;
    }
}

