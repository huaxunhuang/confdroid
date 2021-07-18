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
package android.view.menu;


/**
 * Utility base class for creating various Menu scenarios. Configurable by the
 * number of menu items. Used @link {@link ListScenario} as a reference.
 */
public class MenuScenario extends android.app.Activity implements android.view.MenuItem.OnMenuItemClickListener {
    private android.view.menu.MenuScenario.Params mParams = new android.view.menu.MenuScenario.Params();

    private android.view.Menu mMenu;

    private android.view.MenuItem[] mItems;

    private boolean[] mWasItemClicked;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        dispatchInitParams();
    }

    private void dispatchInitParams() {
        onInitParams(mParams);
        onParamsChanged();
    }

    public void setParams(android.view.menu.MenuScenario.Params params) {
        mParams = params;
        onParamsChanged();
    }

    public void onParamsChanged() {
        mItems = new android.view.MenuItem[mParams.numItems];
        mWasItemClicked = new boolean[mParams.numItems];
    }

    @java.lang.Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Safe to hold on to
        mMenu = menu;
        if (!mParams.shouldShowMenu)
            return false;

        android.view.MenuItem item;
        for (int i = 0; i < mParams.numItems; i++) {
            if ((item = onAddMenuItem(menu, i)) == null) {
                // Add a default item for this position if the subclasses
                // haven't
                java.lang.CharSequence givenTitle = mParams.itemTitles.get(i);
                item = menu.add(0, 0, 0, givenTitle != null ? givenTitle : "Item " + i);
            }
            if (item != null) {
                mItems[i] = item;
                if (mParams.listenForClicks) {
                    item.setOnMenuItemClickListener(this);
                }
            }
        }
        return true;
    }

    @java.lang.Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // Safe to hold on to
        mMenu = menu;
        return mParams.shouldShowMenu;
    }

    /**
     * Override this to add an item to the menu.
     *
     * @param itemPosition
     * 		The position of the item to add (only for your
     * 		reference).
     * @return The item that was added to the menu, or null if nothing was
    added.
     */
    protected android.view.MenuItem onAddMenuItem(android.view.Menu menu, int itemPosition) {
        return null;
    }

    /**
     * Override this to set the parameters for the scenario. Call through to super first.
     *
     * @param params
     * 		
     */
    protected void onInitParams(android.view.menu.MenuScenario.Params params) {
    }

    public android.view.Menu getMenu() {
        return mMenu;
    }

    public boolean onMenuItemClick(android.view.MenuItem item) {
        final int position = findItemPosition(item);
        if (position < 0)
            return false;

        mWasItemClicked[position] = true;
        return true;
    }

    public boolean wasItemClicked(int position) {
        return mWasItemClicked[position];
    }

    /**
     * Finds the position for a given Item.
     *
     * @param item
     * 		The item to find.
     * @return The position, or -1 if not found.
     */
    public int findItemPosition(android.view.MenuItem item) {
        // Could create reverse mapping, but optimizations aren't important (yet :P)
        for (int i = 0; i < mParams.numItems; i++) {
            if (mItems[i] == item)
                return i;

        }
        return -1;
    }

    public static class Params {
        // Using as data structure, so no m prefix
        private boolean shouldShowMenu = true;

        private int numItems = 10;

        private boolean listenForClicks = true;

        private android.util.SparseArray<java.lang.CharSequence> itemTitles = new android.util.SparseArray<java.lang.CharSequence>();

        public android.view.menu.MenuScenario.Params setShouldShowMenu(boolean shouldShowMenu) {
            this.shouldShowMenu = shouldShowMenu;
            return this;
        }

        public android.view.menu.MenuScenario.Params setNumItems(int numItems) {
            this.numItems = numItems;
            return this;
        }

        public android.view.menu.MenuScenario.Params setListenForClicks(boolean listenForClicks) {
            this.listenForClicks = listenForClicks;
            return this;
        }

        public android.view.menu.MenuScenario.Params setItemTitle(int itemPos, java.lang.CharSequence title) {
            itemTitles.put(itemPos, title);
            return this;
        }
    }
}

