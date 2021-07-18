package android.widget;


/**
 * An interface notified when a menu item is hovered. Useful for cases when hover should trigger
 * some behavior at a higher level, like managing the opening and closing of submenus.
 *
 * @unknown 
 */
public interface MenuItemHoverListener {
    /**
     * Called when hover exits a menu item.
     * <p>
     * If hover is moving to another item, this method will be called before
     * {@link #onItemHoverEnter(MenuBuilder, MenuItem)} for the newly-hovered item.
     *
     * @param menu
     * 		the item's parent menu
     * @param item
     * 		the hovered menu item
     */
    void onItemHoverExit(@android.annotation.NonNull
    com.android.internal.view.menu.MenuBuilder menu, @android.annotation.NonNull
    android.view.MenuItem item);

    /**
     * Called when hover enters a menu item.
     *
     * @param menu
     * 		the item's parent menu
     * @param item
     * 		the hovered menu item
     */
    void onItemHoverEnter(@android.annotation.NonNull
    com.android.internal.view.menu.MenuBuilder menu, @android.annotation.NonNull
    android.view.MenuItem item);
}

