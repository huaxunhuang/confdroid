/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.view;


/**
 * Helper for accessing features in {@link android.view.MenuItem}
 * introduced after API level 4 in a backwards compatible fashion.
 * <p class="note"><strong>Note:</strong> You cannot get an instance of this class. Instead,
 * it provides <em>static</em> methods that correspond to the methods in {@link android.view.MenuItem}, but take a {@link android.view.MenuItem} object as an additional
 * argument.</p>
 */
public final class MenuItemCompat {
    private static final java.lang.String TAG = "MenuItemCompat";

    /**
     * Never show this item as a button in an Action Bar.
     */
    public static final int SHOW_AS_ACTION_NEVER = 0;

    /**
     * Show this item as a button in an Action Bar if the system
     * decides there is room for it.
     */
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;

    /**
     * Always show this item as a button in an Action Bar. Use sparingly!
     * If too many items are set to always show in the Action Bar it can
     * crowd the Action Bar and degrade the user experience on devices with
     * smaller screens. A good rule of thumb is to have no more than 2
     * items set to always show at a time.
     */
    public static final int SHOW_AS_ACTION_ALWAYS = 2;

    /**
     * When this item is in the action bar, always show it with a
     * text label even if it also has an icon specified.
     */
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;

    /**
     * This item's action view collapses to a normal menu item.
     * When expanded, the action view temporarily takes over
     * a larger segment of its container.
     */
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;

    /**
     * Interface for the full API.
     */
    interface MenuVersionImpl {
        void setShowAsAction(android.view.MenuItem item, int actionEnum);

        android.view.MenuItem setActionView(android.view.MenuItem item, android.view.View view);

        android.view.MenuItem setActionView(android.view.MenuItem item, int resId);

        android.view.View getActionView(android.view.MenuItem item);

        boolean expandActionView(android.view.MenuItem item);

        boolean collapseActionView(android.view.MenuItem item);

        boolean isActionViewExpanded(android.view.MenuItem item);

        android.view.MenuItem setOnActionExpandListener(android.view.MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener);
    }

    /**
     * Interface definition for a callback to be invoked when a menu item marked with {@link #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW} is expanded or collapsed.
     *
     * @see #expandActionView(android.view.MenuItem)
     * @see #collapseActionView(android.view.MenuItem)
     * @see #setShowAsAction(android.view.MenuItem, int)
     */
    public interface OnActionExpandListener {
        /**
         * Called when a menu item with {@link #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW}
         * is expanded.
         *
         * @param item
         * 		Item that was expanded
         * @return true if the item should expand, false if expansion should be suppressed.
         */
        public boolean onMenuItemActionExpand(android.view.MenuItem item);

        /**
         * Called when a menu item with {@link #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW}
         * is collapsed.
         *
         * @param item
         * 		Item that was collapsed
         * @return true if the item should collapse, false if collapsing should be suppressed.
         */
        public boolean onMenuItemActionCollapse(android.view.MenuItem item);
    }

    /**
     * Interface implementation that doesn't use anything about v4 APIs.
     */
    static class BaseMenuVersionImpl implements android.support.v4.view.MenuItemCompat.MenuVersionImpl {
        @java.lang.Override
        public void setShowAsAction(android.view.MenuItem item, int actionEnum) {
        }

        @java.lang.Override
        public android.view.MenuItem setActionView(android.view.MenuItem item, android.view.View view) {
            return item;
        }

        @java.lang.Override
        public android.view.MenuItem setActionView(android.view.MenuItem item, int resId) {
            return item;
        }

        @java.lang.Override
        public android.view.View getActionView(android.view.MenuItem item) {
            return null;
        }

        @java.lang.Override
        public boolean expandActionView(android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public boolean collapseActionView(android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public boolean isActionViewExpanded(android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public android.view.MenuItem setOnActionExpandListener(android.view.MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
            return item;
        }
    }

    /**
     * Interface implementation for devices with at least v11 APIs.
     */
    static class HoneycombMenuVersionImpl implements android.support.v4.view.MenuItemCompat.MenuVersionImpl {
        @java.lang.Override
        public void setShowAsAction(android.view.MenuItem item, int actionEnum) {
            android.support.v4.view.MenuItemCompatHoneycomb.setShowAsAction(item, actionEnum);
        }

        @java.lang.Override
        public android.view.MenuItem setActionView(android.view.MenuItem item, android.view.View view) {
            return android.support.v4.view.MenuItemCompatHoneycomb.setActionView(item, view);
        }

        @java.lang.Override
        public android.view.MenuItem setActionView(android.view.MenuItem item, int resId) {
            return android.support.v4.view.MenuItemCompatHoneycomb.setActionView(item, resId);
        }

        @java.lang.Override
        public android.view.View getActionView(android.view.MenuItem item) {
            return android.support.v4.view.MenuItemCompatHoneycomb.getActionView(item);
        }

        @java.lang.Override
        public boolean expandActionView(android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public boolean collapseActionView(android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public boolean isActionViewExpanded(android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public android.view.MenuItem setOnActionExpandListener(android.view.MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
            return item;
        }
    }

    static class IcsMenuVersionImpl extends android.support.v4.view.MenuItemCompat.HoneycombMenuVersionImpl {
        @java.lang.Override
        public boolean expandActionView(android.view.MenuItem item) {
            return android.support.v4.view.MenuItemCompatIcs.expandActionView(item);
        }

        @java.lang.Override
        public boolean collapseActionView(android.view.MenuItem item) {
            return android.support.v4.view.MenuItemCompatIcs.collapseActionView(item);
        }

        @java.lang.Override
        public boolean isActionViewExpanded(android.view.MenuItem item) {
            return android.support.v4.view.MenuItemCompatIcs.isActionViewExpanded(item);
        }

        @java.lang.Override
        public android.view.MenuItem setOnActionExpandListener(android.view.MenuItem item, final android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
            if (listener == null) {
                return android.support.v4.view.MenuItemCompatIcs.setOnActionExpandListener(item, null);
            }
            /* MenuItemCompatIcs is a dependency of this segment of the support lib
            but not the other way around, so we need to take an extra step here to proxy
            to the right types.
             */
            return android.support.v4.view.MenuItemCompatIcs.setOnActionExpandListener(item, new android.support.v4.view.MenuItemCompatIcs.SupportActionExpandProxy() {
                @java.lang.Override
                public boolean onMenuItemActionExpand(android.view.MenuItem item) {
                    return listener.onMenuItemActionExpand(item);
                }

                @java.lang.Override
                public boolean onMenuItemActionCollapse(android.view.MenuItem item) {
                    return listener.onMenuItemActionCollapse(item);
                }
            });
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.view.MenuItemCompat.MenuVersionImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 14) {
            IMPL = new android.support.v4.view.MenuItemCompat.IcsMenuVersionImpl();
        } else
            if (version >= 11) {
                IMPL = new android.support.v4.view.MenuItemCompat.HoneycombMenuVersionImpl();
            } else {
                IMPL = new android.support.v4.view.MenuItemCompat.BaseMenuVersionImpl();
            }

    }

    // -------------------------------------------------------------------
    /**
     * Sets how this item should display in the presence of a compatible Action Bar. If the given
     * item is compatible, this will call the item's supported implementation of
     * {@link MenuItem#setShowAsAction(int)}.
     *
     * @param item
     * 		- the item to change
     * @param actionEnum
     * 		- How the item should display.
     */
    public static void setShowAsAction(android.view.MenuItem item, int actionEnum) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            ((android.support.v4.internal.view.SupportMenuItem) (item)).setShowAsAction(actionEnum);
        } else {
            android.support.v4.view.MenuItemCompat.IMPL.setShowAsAction(item, actionEnum);
        }
    }

    /**
     * Set an action view for this menu item. An action view will be displayed in place
     * of an automatically generated menu item element in the UI when this item is shown
     * as an action within a parent.
     *
     * @param item
     * 		the item to change
     * @param view
     * 		View to use for presenting this item to the user.
     * @return This Item so additional setters can be called.
     * @see #setShowAsAction(MenuItem, int)
     */
    public static android.view.MenuItem setActionView(android.view.MenuItem item, android.view.View view) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).setActionView(view);
        }
        return android.support.v4.view.MenuItemCompat.IMPL.setActionView(item, view);
    }

    /**
     * Set an action view for this menu item. An action view will be displayed in place
     * of an automatically generated menu item element in the UI when this item is shown
     * as an action within a parent.
     * <p>
     *   <strong>Note:</strong> Setting an action view overrides the action provider
     *           set via {@link #setActionProvider(MenuItem, ActionProvider)}.
     * </p>
     *
     * @param item
     * 		the item to change
     * @param resId
     * 		Layout resource to use for presenting this item to the user.
     * @return This Item so additional setters can be called.
     * @see #setShowAsAction(MenuItem, int)
     */
    public static android.view.MenuItem setActionView(android.view.MenuItem item, int resId) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).setActionView(resId);
        }
        return android.support.v4.view.MenuItemCompat.IMPL.setActionView(item, resId);
    }

    /**
     * Returns the currently set action view for this menu item.
     *
     * @param item
     * 		the item to query
     * @return This item's action view
     */
    public static android.view.View getActionView(android.view.MenuItem item) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).getActionView();
        }
        return android.support.v4.view.MenuItemCompat.IMPL.getActionView(item);
    }

    /**
     * Sets the {@link ActionProvider} responsible for creating an action view if
     * the item is placed on the action bar. The provider also provides a default
     * action invoked if the item is placed in the overflow menu.
     * <p>
     *   <strong>Note:</strong> Setting an action provider overrides the action view
     *           set via {@link #setActionView(MenuItem, View)}.
     * </p>
     *
     * @param item
     * 		item to change
     * @param provider
     * 		The action provider.
     * @return This Item so additional setters can be called.
     * @see ActionProvider
     */
    public static android.view.MenuItem setActionProvider(android.view.MenuItem item, android.support.v4.view.ActionProvider provider) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).setSupportActionProvider(provider);
        }
        // TODO Wrap the support ActionProvider and assign it
        android.util.Log.w(android.support.v4.view.MenuItemCompat.TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return item;
    }

    /**
     * Gets the {@link ActionProvider}.
     *
     * @return The action provider.
     * @see ActionProvider
     * @see #setActionProvider(MenuItem, ActionProvider)
     */
    public static android.support.v4.view.ActionProvider getActionProvider(android.view.MenuItem item) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).getSupportActionProvider();
        }
        // TODO Wrap the framework ActionProvider and return it
        android.util.Log.w(android.support.v4.view.MenuItemCompat.TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }

    /**
     * Expand the action view associated with this menu item.
     * The menu item must have an action view set, as well as
     * the showAsAction flag {@link #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW}.
     * If a listener has been set using
     * {@link #setOnActionExpandListener(MenuItem, OnActionExpandListener)}
     * it will have its {@link OnActionExpandListener#onMenuItemActionExpand(MenuItem)}
     * method invoked. The listener may return false from this method to prevent expanding
     * the action view.
     *
     * @return true if the action view was expanded, false otherwise.
     */
    public static boolean expandActionView(android.view.MenuItem item) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).expandActionView();
        }
        return android.support.v4.view.MenuItemCompat.IMPL.expandActionView(item);
    }

    /**
     * Collapse the action view associated with this menu item. The menu item must have an action
     * view set, as well as the showAsAction flag {@link #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW}. If a
     * listener has been set using {@link #setOnActionExpandListener(MenuItem,
     * android.support.v4.view.MenuItemCompat.OnActionExpandListener)}
     * it will have its {@link android.support.v4.view.MenuItemCompat.OnActionExpandListener#onMenuItemActionCollapse(MenuItem)}
     * method invoked. The listener may return false from this method to prevent collapsing
     * the action view.
     *
     * @return true if the action view was collapsed, false otherwise.
     */
    public static boolean collapseActionView(android.view.MenuItem item) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).collapseActionView();
        }
        return android.support.v4.view.MenuItemCompat.IMPL.collapseActionView(item);
    }

    /**
     * Returns true if this menu item's action view has been expanded.
     *
     * @return true if the item's action view is expanded, false otherwise.
     * @see #expandActionView(MenuItem)
     * @see #collapseActionView(MenuItem)
     * @see #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
     * @see android.support.v4.view.MenuItemCompat.OnActionExpandListener
     */
    public static boolean isActionViewExpanded(android.view.MenuItem item) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).isActionViewExpanded();
        }
        return android.support.v4.view.MenuItemCompat.IMPL.isActionViewExpanded(item);
    }

    /**
     * Set an {@link OnActionExpandListener} on this menu
     * item to be notified when the associated action view is expanded or collapsed.
     * The menu item must be configured to expand or collapse its action view using the flag
     * {@link #SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW}.
     *
     * @param listener
     * 		Listener that will respond to expand/collapse events
     * @return This menu item instance for call chaining
     */
    public static android.view.MenuItem setOnActionExpandListener(android.view.MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
        if (item instanceof android.support.v4.internal.view.SupportMenuItem) {
            return ((android.support.v4.internal.view.SupportMenuItem) (item)).setSupportOnActionExpandListener(listener);
        }
        return android.support.v4.view.MenuItemCompat.IMPL.setOnActionExpandListener(item, listener);
    }

    private MenuItemCompat() {
    }
}

