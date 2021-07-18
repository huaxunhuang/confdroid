/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.app;


/**
 * A primary toolbar within the activity that may display the activity title, application-level
 * navigation affordances, and other interactive items.
 *
 * <p>Beginning with Android 3.0 (API level 11), the action bar appears at the top of an
 * activity's window when the activity uses the system's {@link android.R.style#Theme_Holo Holo} theme (or one of its descendant themes), which is the default.
 * You may otherwise add the action bar by calling {@link android.view.Window#requestFeature requestFeature(FEATURE_ACTION_BAR)} or by declaring it in a
 * custom theme with the {@link android.R.styleable#Theme_windowActionBar windowActionBar} property.
 * </p>
 *
 * <p>Beginning with Android L (API level 21), the action bar may be represented by any
 * Toolbar widget within the application layout. The application may signal to the Activity
 * which Toolbar should be treated as the Activity's action bar. Activities that use this
 * feature should use one of the supplied <code>.NoActionBar</code> themes, set the
 * {@link android.R.styleable#Theme_windowActionBar windowActionBar} attribute to <code>false</code>
 * or otherwise not request the window feature.</p>
 *
 * <p>By adjusting the window features requested by the theme and the layouts used for
 * an Activity's content view, an app can use the standard system action bar on older platform
 * releases and the newer inline toolbars on newer platform releases. The <code>ActionBar</code>
 * object obtained from the Activity can be used to control either configuration transparently.</p>
 *
 * <p>When using the Holo themes the action bar shows the application icon on
 * the left, followed by the activity title. If your activity has an options menu, you can make
 * select items accessible directly from the action bar as "action items". You can also
 * modify various characteristics of the action bar or remove it completely.</p>
 *
 * <p>When using the Material themes (default in API 21 or newer) the navigation button
 * (formerly "Home") takes over the space previously occupied by the application icon.
 * Apps wishing to express a stronger branding should use their brand colors heavily
 * in the action bar and other application chrome or use a {@link #setLogo(int) logo}
 * in place of their standard title text.</p>
 *
 * <p>From your activity, you can retrieve an instance of {@link ActionBar} by calling {@link android.app.Activity#getActionBar getActionBar()}.</p>
 *
 * <p>In some cases, the action bar may be overlayed by another bar that enables contextual actions,
 * using an {@link android.view.ActionMode}. For example, when the user selects one or more items in
 * your activity, you can enable an action mode that offers actions specific to the selected
 * items, with a UI that temporarily replaces the action bar. Although the UI may occupy the
 * same space, the {@link android.view.ActionMode} APIs are distinct and independent from those for
 * {@link ActionBar}.</p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about how to use the action bar, including how to add action items, navigation
 * modes and more, read the <a href="{@docRoot }guide/topics/ui/actionbar.html">Action
 * Bar</a> developer guide.</p>
 * </div>
 */
public abstract class ActionBar {
    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.app.ActionBar.NAVIGATION_MODE_STANDARD, android.app.ActionBar.NAVIGATION_MODE_LIST, android.app.ActionBar.NAVIGATION_MODE_TABS })
    public @interface NavigationMode {}

    /**
     * Standard navigation mode. Consists of either a logo or icon
     * and title text with an optional subtitle. Clicking any of these elements
     * will dispatch onOptionsItemSelected to the host Activity with
     * a MenuItem with item ID android.R.id.home.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public static final int NAVIGATION_MODE_STANDARD = 0;

    /**
     * List navigation mode. Instead of static title text this mode
     * presents a list menu for navigation within the activity.
     * e.g. this might be presented to the user as a dropdown list.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public static final int NAVIGATION_MODE_LIST = 1;

    /**
     * Tab navigation mode. Instead of static title text this mode
     * presents a series of tabs for navigation within the activity.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public static final int NAVIGATION_MODE_TABS = 2;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.app.ActionBar.DISPLAY_USE_LOGO, android.app.ActionBar.DISPLAY_SHOW_HOME, android.app.ActionBar.DISPLAY_HOME_AS_UP, android.app.ActionBar.DISPLAY_SHOW_TITLE, android.app.ActionBar.DISPLAY_SHOW_CUSTOM, android.app.ActionBar.DISPLAY_TITLE_MULTIPLE_LINES })
    public @interface DisplayOptions {}

    /**
     * Use logo instead of icon if available. This flag will cause appropriate
     * navigation modes to use a wider logo in place of the standard icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_USE_LOGO = 0x1;

    /**
     * Show 'home' elements in this action bar, leaving more space for other
     * navigation elements. This includes logo and icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_HOME = 0x2;

    /**
     * Display the 'home' element such that it appears as an 'up' affordance.
     * e.g. show an arrow to the left indicating the action that will be taken.
     *
     * Set this flag if selecting the 'home' button in the action bar to return
     * up by a single level in your UI rather than back to the top level or front page.
     *
     * <p>Setting this option will implicitly enable interaction with the home/up
     * button. See {@link #setHomeButtonEnabled(boolean)}.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_HOME_AS_UP = 0x4;

    /**
     * Show the activity title and subtitle, if present.
     *
     * @see #setTitle(CharSequence)
     * @see #setTitle(int)
     * @see #setSubtitle(CharSequence)
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_TITLE = 0x8;

    /**
     * Show the custom view if one has been set.
     *
     * @see #setCustomView(View)
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_CUSTOM = 0x10;

    /**
     * Allow the title to wrap onto multiple lines if space is available
     *
     * @unknown pending API approval
     */
    public static final int DISPLAY_TITLE_MULTIPLE_LINES = 0x20;

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.
     *
     * @param view
     * 		Custom navigation view to place in the ActionBar.
     */
    public abstract void setCustomView(android.view.View view);

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * <p>Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.</p>
     *
     * <p>The display option {@link #DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param view
     * 		Custom navigation view to place in the ActionBar.
     * @param layoutParams
     * 		How this custom view should layout in the bar.
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setCustomView(android.view.View view, android.app.ActionBar.LayoutParams layoutParams);

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * <p>Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.</p>
     *
     * <p>The display option {@link #DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param resId
     * 		Resource ID of a layout to inflate into the ActionBar.
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setCustomView(@android.annotation.LayoutRes
    int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param resId
     * 		Resource ID of a drawable to show as an icon.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setIcon(@android.annotation.DrawableRes
    int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param icon
     * 		Drawable to show as an icon.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setIcon(android.graphics.drawable.Drawable icon);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param resId
     * 		Resource ID of a drawable to show as a logo.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setLogo(@android.annotation.DrawableRes
    int resId);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param logo
     * 		Drawable to show as a logo.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setLogo(android.graphics.drawable.Drawable logo);

    /**
     * Set the adapter and navigation callback for list navigation mode.
     *
     * The supplied adapter will provide views for the expanded list as well as
     * the currently selected item. (These may be displayed differently.)
     *
     * The supplied OnNavigationListener will alert the application when the user
     * changes the current list selection.
     *
     * @param adapter
     * 		An adapter that will provide views both to display
     * 		the current navigation selection and populate views
     * 		within the dropdown navigation menu.
     * @param callback
     * 		An OnNavigationListener that will receive events when the user
     * 		selects a navigation item.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void setListNavigationCallbacks(android.widget.SpinnerAdapter adapter, android.app.ActionBar.OnNavigationListener callback);

    /**
     * Set the selected navigation item in list or tabbed navigation modes.
     *
     * @param position
     * 		Position of the item to select.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void setSelectedNavigationItem(int position);

    /**
     * Get the position of the selected navigation item in list or tabbed navigation modes.
     *
     * @return Position of the selected item.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract int getSelectedNavigationIndex();

    /**
     * Get the number of navigation items present in the current navigation mode.
     *
     * @return Number of navigation items.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract int getNavigationItemCount();

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param title
     * 		Title to set
     * @see #setTitle(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setTitle(java.lang.CharSequence title);

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId
     * 		Resource ID of title string to set
     * @see #setTitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setTitle(@android.annotation.StringRes
    int resId);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set. Set to null to disable the
     * subtitle entirely.
     *
     * @param subtitle
     * 		Subtitle to set
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setSubtitle(java.lang.CharSequence subtitle);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId
     * 		Resource ID of subtitle string to set
     * @see #setSubtitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setSubtitle(@android.annotation.StringRes
    int resId);

    /**
     * Set display options. This changes all display option bits at once. To change
     * a limited subset of display options, see {@link #setDisplayOptions(int, int)}.
     *
     * @param options
     * 		A combination of the bits defined by the DISPLAY_ constants
     * 		defined in ActionBar.
     */
    public abstract void setDisplayOptions(@android.app.ActionBar.DisplayOptions
    int options);

    /**
     * Set selected display options. Only the options specified by mask will be changed.
     * To change all display option bits at once, see {@link #setDisplayOptions(int)}.
     *
     * <p>Example: setDisplayOptions(0, DISPLAY_SHOW_HOME) will disable the
     * {@link #DISPLAY_SHOW_HOME} option.
     * setDisplayOptions(DISPLAY_SHOW_HOME, DISPLAY_SHOW_HOME | DISPLAY_USE_LOGO)
     * will enable {@link #DISPLAY_SHOW_HOME} and disable {@link #DISPLAY_USE_LOGO}.
     *
     * @param options
     * 		A combination of the bits defined by the DISPLAY_ constants
     * 		defined in ActionBar.
     * @param mask
     * 		A bit mask declaring which display options should be changed.
     */
    public abstract void setDisplayOptions(@android.app.ActionBar.DisplayOptions
    int options, @android.app.ActionBar.DisplayOptions
    int mask);

    /**
     * Set whether to display the activity logo rather than the activity icon.
     * A logo is often a wider, more detailed image.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param useLogo
     * 		true to use the activity logo, false to use the activity icon.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayUseLogoEnabled(boolean useLogo);

    /**
     * Set whether to include the application home affordance in the action bar.
     * Home is presented as either an activity icon or logo.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHome
     * 		true to show home, false otherwise.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowHomeEnabled(boolean showHome);

    /**
     * Set whether home should be displayed as an "up" affordance.
     * Set this to true if selecting "home" returns up by a single level in your UI
     * rather than back to the top level or front page.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHomeAsUp
     * 		true to show the user that selecting home will return one
     * 		level up rather than to the top level of the app.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

    /**
     * Set whether an activity title/subtitle should be displayed.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showTitle
     * 		true to display a title/subtitle if present.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowTitleEnabled(boolean showTitle);

    /**
     * Set whether a custom view should be displayed, if set.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showCustom
     * 		true if the currently set custom view should be displayed, false otherwise.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowCustomEnabled(boolean showCustom);

    /**
     * Set the ActionBar's background. This will be used for the primary
     * action bar.
     *
     * @param d
     * 		Background drawable
     * @see #setStackedBackgroundDrawable(Drawable)
     * @see #setSplitBackgroundDrawable(Drawable)
     */
    public abstract void setBackgroundDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable d);

    /**
     * Set the ActionBar's stacked background. This will appear
     * in the second row/stacked bar on some devices and configurations.
     *
     * @param d
     * 		Background drawable for the stacked row
     */
    public void setStackedBackgroundDrawable(android.graphics.drawable.Drawable d) {
    }

    /**
     * Set the ActionBar's split background. This will appear in
     * the split action bar containing menu-provided action buttons
     * on some devices and configurations.
     * <p>You can enable split action bar with {@link android.R.attr#uiOptions}
     *
     * @param d
     * 		Background drawable for the split bar
     */
    public void setSplitBackgroundDrawable(android.graphics.drawable.Drawable d) {
    }

    /**
     *
     *
     * @return The current custom view.
     */
    public abstract android.view.View getCustomView();

    /**
     * Returns the current ActionBar title in standard mode.
     * Returns null if {@link #getNavigationMode()} would not return
     * {@link #NAVIGATION_MODE_STANDARD}.
     *
     * @return The current ActionBar title or null.
     */
    public abstract java.lang.CharSequence getTitle();

    /**
     * Returns the current ActionBar subtitle in standard mode.
     * Returns null if {@link #getNavigationMode()} would not return
     * {@link #NAVIGATION_MODE_STANDARD}.
     *
     * @return The current ActionBar subtitle or null.
     */
    public abstract java.lang.CharSequence getSubtitle();

    /**
     * Returns the current navigation mode. The result will be one of:
     * <ul>
     * <li>{@link #NAVIGATION_MODE_STANDARD}</li>
     * <li>{@link #NAVIGATION_MODE_LIST}</li>
     * <li>{@link #NAVIGATION_MODE_TABS}</li>
     * </ul>
     *
     * @return The current navigation mode.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    @android.app.ActionBar.NavigationMode
    public abstract int getNavigationMode();

    /**
     * Set the current navigation mode.
     *
     * @param mode
     * 		The new mode to set.
     * @see #NAVIGATION_MODE_STANDARD
     * @see #NAVIGATION_MODE_LIST
     * @see #NAVIGATION_MODE_TABS
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void setNavigationMode(@android.app.ActionBar.NavigationMode
    int mode);

    /**
     *
     *
     * @return The current set of display options.
     */
    public abstract int getDisplayOptions();

    /**
     * Create and return a new {@link Tab}.
     * This tab will not be included in the action bar until it is added.
     *
     * <p>Very often tabs will be used to switch between {@link Fragment}
     * objects.  Here is a typical implementation of such tabs:</p>
     *
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentTabs.java
     *      complete}
     *
     * @return A new Tab
     * @see #addTab(Tab)
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract android.app.ActionBar.Tab newTab();

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be added at the end of the list.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab
     * 		Tab to add
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void addTab(android.app.ActionBar.Tab tab);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be added at the end of the list.
     *
     * @param tab
     * 		Tab to add
     * @param setSelected
     * 		True if the added tab should become the selected tab.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void addTab(android.app.ActionBar.Tab tab, boolean setSelected);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be inserted at
     * <code>position</code>. If this is the first tab to be added it will become
     * the selected tab.
     *
     * @param tab
     * 		The tab to add
     * @param position
     * 		The new position of the tab
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void addTab(android.app.ActionBar.Tab tab, int position);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be insterted at
     * <code>position</code>.
     *
     * @param tab
     * 		The tab to add
     * @param position
     * 		The new position of the tab
     * @param setSelected
     * 		True if the added tab should become the selected tab.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void addTab(android.app.ActionBar.Tab tab, int position, boolean setSelected);

    /**
     * Remove a tab from the action bar. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param tab
     * 		The tab to remove
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void removeTab(android.app.ActionBar.Tab tab);

    /**
     * Remove a tab from the action bar. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param position
     * 		Position of the tab to remove
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void removeTabAt(int position);

    /**
     * Remove all tabs from the action bar and deselect the current tab.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void removeAllTabs();

    /**
     * Select the specified tab. If it is not a child of this action bar it will be added.
     *
     * <p>Note: If you want to select by index, use {@link #setSelectedNavigationItem(int)}.</p>
     *
     * @param tab
     * 		Tab to select
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract void selectTab(android.app.ActionBar.Tab tab);

    /**
     * Returns the currently selected tab if in tabbed navigation mode and there is at least
     * one tab present.
     *
     * @return The currently selected tab or null
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract android.app.ActionBar.Tab getSelectedTab();

    /**
     * Returns the tab at the specified index.
     *
     * @param index
     * 		Index value in the range 0-get
     * @return 
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract android.app.ActionBar.Tab getTabAt(int index);

    /**
     * Returns the number of tabs currently registered with the action bar.
     *
     * @return Tab count
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public abstract int getTabCount();

    /**
     * Retrieve the current height of the ActionBar.
     *
     * @return The ActionBar's height
     */
    public abstract int getHeight();

    /**
     * Show the ActionBar if it is not currently showing.
     * If the window hosting the ActionBar does not have the feature
     * {@link Window#FEATURE_ACTION_BAR_OVERLAY} it will resize application
     * content to fit the new space available.
     *
     * <p>If you are hiding the ActionBar through
     * {@link View#SYSTEM_UI_FLAG_FULLSCREEN View.SYSTEM_UI_FLAG_FULLSCREEN},
     * you should not call this function directly.
     */
    public abstract void show();

    /**
     * Hide the ActionBar if it is currently showing.
     * If the window hosting the ActionBar does not have the feature
     * {@link Window#FEATURE_ACTION_BAR_OVERLAY} it will resize application
     * content to fit the new space available.
     *
     * <p>Instead of calling this function directly, you can also cause an
     * ActionBar using the overlay feature to hide through
     * {@link View#SYSTEM_UI_FLAG_FULLSCREEN View.SYSTEM_UI_FLAG_FULLSCREEN}.
     * Hiding the ActionBar through this system UI flag allows you to more
     * seamlessly hide it in conjunction with other screen decorations.
     */
    public abstract void hide();

    /**
     *
     *
     * @return <code>true</code> if the ActionBar is showing, <code>false</code> otherwise.
     */
    public abstract boolean isShowing();

    /**
     * Add a listener that will respond to menu visibility change events.
     *
     * @param listener
     * 		The new listener to add
     */
    public abstract void addOnMenuVisibilityListener(android.app.ActionBar.OnMenuVisibilityListener listener);

    /**
     * Remove a menu visibility listener. This listener will no longer receive menu
     * visibility change events.
     *
     * @param listener
     * 		A listener to remove that was previously added
     */
    public abstract void removeOnMenuVisibilityListener(android.app.ActionBar.OnMenuVisibilityListener listener);

    /**
     * Enable or disable the "home" button in the corner of the action bar. (Note that this
     * is the application home/up affordance on the action bar, not the systemwide home
     * button.)
     *
     * <p>This defaults to true for packages targeting &lt; API 14. For packages targeting
     * API 14 or greater, the application should call this method to enable interaction
     * with the home/up affordance.
     *
     * <p>Setting the {@link #DISPLAY_HOME_AS_UP} display option will automatically enable
     * the home button.
     *
     * @param enabled
     * 		true to enable the home button, false to disable the home button.
     */
    public void setHomeButtonEnabled(boolean enabled) {
    }

    /**
     * Returns a {@link Context} with an appropriate theme for creating views that
     * will appear in the action bar. If you are inflating or instantiating custom views
     * that will appear in an action bar, you should use the Context returned by this method.
     * (This includes adapters used for list navigation mode.)
     * This will ensure that views contrast properly against the action bar.
     *
     * @return A themed Context for creating views
     */
    public android.content.Context getThemedContext() {
        return null;
    }

    /**
     * Returns true if the Title field has been truncated during layout for lack
     * of available space.
     *
     * @return true if the Title field has been truncated
     * @unknown pending API approval
     */
    public boolean isTitleTruncated() {
        return false;
    }

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when {@link #DISPLAY_HOME_AS_UP} is enabled. This can be useful if you are using
     * this mode to display an alternate selection for up navigation, such as a sliding drawer.
     *
     * <p>If you pass <code>null</code> to this method, the default drawable from the theme
     * will be used.</p>
     *
     * <p>If you implement alternate or intermediate behavior around Up, you should also
     * call {@link #setHomeActionContentDescription(int) setHomeActionContentDescription()}
     * to provide a correct description of the action for accessibility support.</p>
     *
     * @param indicator
     * 		A drawable to use for the up indicator, or null to use the theme's default
     * @see #setDisplayOptions(int, int)
     * @see #setDisplayHomeAsUpEnabled(boolean)
     * @see #setHomeActionContentDescription(int)
     */
    public void setHomeAsUpIndicator(android.graphics.drawable.Drawable indicator) {
    }

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when {@link #DISPLAY_HOME_AS_UP} is enabled. This can be useful if you are using
     * this mode to display an alternate selection for up navigation, such as a sliding drawer.
     *
     * <p>If you pass <code>0</code> to this method, the default drawable from the theme
     * will be used.</p>
     *
     * <p>If you implement alternate or intermediate behavior around Up, you should also
     * call {@link #setHomeActionContentDescription(int) setHomeActionContentDescription()}
     * to provide a correct description of the action for accessibility support.</p>
     *
     * @param resId
     * 		Resource ID of a drawable to use for the up indicator, or null
     * 		to use the theme's default
     * @see #setDisplayOptions(int, int)
     * @see #setDisplayHomeAsUpEnabled(boolean)
     * @see #setHomeActionContentDescription(int)
     */
    public void setHomeAsUpIndicator(@android.annotation.DrawableRes
    int resId) {
    }

    /**
     * Set an alternate description for the Home/Up action, when enabled.
     *
     * <p>This description is commonly used for accessibility/screen readers when
     * the Home action is enabled. (See {@link #setDisplayHomeAsUpEnabled(boolean)}.)
     * Examples of this are, "Navigate Home" or "Navigate Up" depending on the
     * {@link #DISPLAY_HOME_AS_UP} display option. If you have changed the home-as-up
     * indicator using {@link #setHomeAsUpIndicator(int)} to indicate more specific
     * functionality such as a sliding drawer, you should also set this to accurately
     * describe the action.</p>
     *
     * <p>Setting this to <code>null</code> will use the system default description.</p>
     *
     * @param description
     * 		New description for the Home action when enabled
     * @see #setHomeAsUpIndicator(int)
     * @see #setHomeAsUpIndicator(android.graphics.drawable.Drawable)
     */
    public void setHomeActionContentDescription(java.lang.CharSequence description) {
    }

    /**
     * Set an alternate description for the Home/Up action, when enabled.
     *
     * <p>This description is commonly used for accessibility/screen readers when
     * the Home action is enabled. (See {@link #setDisplayHomeAsUpEnabled(boolean)}.)
     * Examples of this are, "Navigate Home" or "Navigate Up" depending on the
     * {@link #DISPLAY_HOME_AS_UP} display option. If you have changed the home-as-up
     * indicator using {@link #setHomeAsUpIndicator(int)} to indicate more specific
     * functionality such as a sliding drawer, you should also set this to accurately
     * describe the action.</p>
     *
     * <p>Setting this to <code>0</code> will use the system default description.</p>
     *
     * @param resId
     * 		Resource ID of a string to use as the new description
     * 		for the Home action when enabled
     * @see #setHomeAsUpIndicator(int)
     * @see #setHomeAsUpIndicator(android.graphics.drawable.Drawable)
     */
    public void setHomeActionContentDescription(@android.annotation.StringRes
    int resId) {
    }

    /**
     * Enable hiding the action bar on content scroll.
     *
     * <p>If enabled, the action bar will scroll out of sight along with a
     * {@link View#setNestedScrollingEnabled(boolean) nested scrolling child} view's content.
     * The action bar must be in {@link Window#FEATURE_ACTION_BAR_OVERLAY overlay mode}
     * to enable hiding on content scroll.</p>
     *
     * <p>When partially scrolled off screen the action bar is considered
     * {@link #hide() hidden}. A call to {@link #show() show} will cause it to return to full view.
     * </p>
     *
     * @param hideOnContentScroll
     * 		true to enable hiding on content scroll.
     */
    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        if (hideOnContentScroll) {
            throw new java.lang.UnsupportedOperationException("Hide on content scroll is not supported in " + "this action bar configuration.");
        }
    }

    /**
     * Return whether the action bar is configured to scroll out of sight along with
     * a {@link View#setNestedScrollingEnabled(boolean) nested scrolling child}.
     *
     * @return true if hide-on-content-scroll is enabled
     * @see #setHideOnContentScrollEnabled(boolean)
     */
    public boolean isHideOnContentScrollEnabled() {
        return false;
    }

    /**
     * Return the current vertical offset of the action bar.
     *
     * <p>The action bar's current hide offset is the distance that the action bar is currently
     * scrolled offscreen in pixels. The valid range is 0 (fully visible) to the action bar's
     * current measured {@link #getHeight() height} (fully invisible).</p>
     *
     * @return The action bar's offset toward its fully hidden state in pixels
     */
    public int getHideOffset() {
        return 0;
    }

    /**
     * Set the current hide offset of the action bar.
     *
     * <p>The action bar's current hide offset is the distance that the action bar is currently
     * scrolled offscreen in pixels. The valid range is 0 (fully visible) to the action bar's
     * current measured {@link #getHeight() height} (fully invisible).</p>
     *
     * @param offset
     * 		The action bar's offset toward its fully hidden state in pixels.
     */
    public void setHideOffset(int offset) {
        if (offset != 0) {
            throw new java.lang.UnsupportedOperationException("Setting an explicit action bar hide offset " + "is not supported in this action bar configuration.");
        }
    }

    /**
     * Set the Z-axis elevation of the action bar in pixels.
     *
     * <p>The action bar's elevation is the distance it is placed from its parent surface. Higher
     * values are closer to the user.</p>
     *
     * @param elevation
     * 		Elevation value in pixels
     */
    public void setElevation(float elevation) {
        if (elevation != 0) {
            throw new java.lang.UnsupportedOperationException("Setting a non-zero elevation is " + "not supported in this action bar configuration.");
        }
    }

    /**
     * Get the Z-axis elevation of the action bar in pixels.
     *
     * <p>The action bar's elevation is the distance it is placed from its parent surface. Higher
     * values are closer to the user.</p>
     *
     * @return Elevation value in pixels
     */
    public float getElevation() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDefaultDisplayHomeAsUpEnabled(boolean enabled) {
    }

    /**
     *
     *
     * @unknown 
     */
    public void setShowHideAnimationEnabled(boolean enabled) {
    }

    /**
     *
     *
     * @unknown 
     */
    public void onConfigurationChanged(android.content.res.Configuration config) {
    }

    /**
     *
     *
     * @unknown 
     */
    public void dispatchMenuVisibilityChanged(boolean visible) {
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.ActionMode startActionMode(android.view.ActionMode.Callback callback) {
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean openOptionsMenu() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean invalidateOptionsMenu() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean onMenuKeyEvent(android.view.KeyEvent event) {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean onKeyShortcut(int keyCode, android.view.KeyEvent event) {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean collapseActionView() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setWindowTitle(java.lang.CharSequence title) {
    }

    /**
     * Attempts to move focus to the ActionBar if it does not already contain the focus.
     *
     * @return {@code true} if focus changes or {@code false} if focus doesn't change.
     * @unknown 
     */
    public boolean requestFocus() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public void onDestroy() {
    }

    /**
     * Common implementation for requestFocus that takes in the Toolbar and moves focus
     * to the contents. This makes the ViewGroups containing the toolbar allow focus while it stays
     * in the ActionBar and then prevents it again once it leaves.
     *
     * @param viewGroup
     * 		The toolbar ViewGroup
     * @return {@code true} if focus changes or {@code false} if focus doesn't change.
     * @unknown 
     */
    protected boolean requestFocus(android.view.ViewGroup viewGroup) {
        if ((viewGroup != null) && (!viewGroup.hasFocus())) {
            final android.view.ViewGroup toolbar = (viewGroup.getTouchscreenBlocksFocus()) ? viewGroup : null;
            android.view.ViewParent parent = viewGroup.getParent();
            android.view.ViewGroup container = null;
            while ((parent != null) && (parent instanceof android.view.ViewGroup)) {
                final android.view.ViewGroup vgParent = ((android.view.ViewGroup) (parent));
                if (vgParent.getTouchscreenBlocksFocus()) {
                    container = vgParent;
                    break;
                }
                parent = vgParent.getParent();
            } 
            if (container != null) {
                container.setTouchscreenBlocksFocus(false);
            }
            if (toolbar != null) {
                toolbar.setTouchscreenBlocksFocus(false);
            }
            viewGroup.requestFocus();
            final android.view.View focused = viewGroup.findFocus();
            if (focused != null) {
                focused.setOnFocusChangeListener(new android.app.ActionBar.FollowOutOfActionBar(viewGroup, container, toolbar));
            } else {
                if (container != null) {
                    container.setTouchscreenBlocksFocus(true);
                }
                if (toolbar != null) {
                    toolbar.setTouchscreenBlocksFocus(true);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Listener interface for ActionBar navigation events.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public interface OnNavigationListener {
        /**
         * This method is called whenever a navigation item in your action bar
         * is selected.
         *
         * @param itemPosition
         * 		Position of the item clicked.
         * @param itemId
         * 		ID of the item clicked.
         * @return True if the event was handled, false otherwise.
         */
        public boolean onNavigationItemSelected(int itemPosition, long itemId);
    }

    /**
     * Listener for receiving events when action bar menus are shown or hidden.
     */
    public interface OnMenuVisibilityListener {
        /**
         * Called when an action bar menu is shown or hidden. Applications may want to use
         * this to tune auto-hiding behavior for the action bar or pause/resume video playback,
         * gameplay, or other activity within the main content area.
         *
         * @param isVisible
         * 		True if an action bar menu is now visible, false if no action bar
         * 		menus are visible.
         */
        public void onMenuVisibilityChanged(boolean isVisible);
    }

    /**
     * A tab in the action bar.
     *
     * <p>Tabs manage the hiding and showing of {@link Fragment}s.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public static abstract class Tab {
        /**
         * An invalid position for a tab.
         *
         * @see #getPosition()
         */
        public static final int INVALID_POSITION = -1;

        /**
         * Return the current position of this tab in the action bar.
         *
         * @return Current position, or {@link #INVALID_POSITION} if this tab is not currently in
        the action bar.
         */
        public abstract int getPosition();

        /**
         * Return the icon associated with this tab.
         *
         * @return The tab's icon
         */
        public abstract android.graphics.drawable.Drawable getIcon();

        /**
         * Return the text of this tab.
         *
         * @return The tab's text
         */
        public abstract java.lang.CharSequence getText();

        /**
         * Set the icon displayed on this tab.
         *
         * @param icon
         * 		The drawable to use as an icon
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setIcon(android.graphics.drawable.Drawable icon);

        /**
         * Set the icon displayed on this tab.
         *
         * @param resId
         * 		Resource ID referring to the drawable to use as an icon
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setIcon(@android.annotation.DrawableRes
        int resId);

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not
         * room to display the entire string.
         *
         * @param text
         * 		The text to display
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setText(java.lang.CharSequence text);

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not
         * room to display the entire string.
         *
         * @param resId
         * 		A resource ID referring to the text that should be displayed
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setText(@android.annotation.StringRes
        int resId);

        /**
         * Set a custom view to be used for this tab. This overrides values set by
         * {@link #setText(CharSequence)} and {@link #setIcon(Drawable)}.
         *
         * @param view
         * 		Custom view to be used as a tab.
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setCustomView(android.view.View view);

        /**
         * Set a custom view to be used for this tab. This overrides values set by
         * {@link #setText(CharSequence)} and {@link #setIcon(Drawable)}.
         *
         * @param layoutResId
         * 		A layout resource to inflate and use as a custom tab view
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setCustomView(@android.annotation.LayoutRes
        int layoutResId);

        /**
         * Retrieve a previously set custom view for this tab.
         *
         * @return The custom view set by {@link #setCustomView(View)}.
         */
        public abstract android.view.View getCustomView();

        /**
         * Give this Tab an arbitrary object to hold for later use.
         *
         * @param obj
         * 		Object to store
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setTag(java.lang.Object obj);

        /**
         *
         *
         * @return This Tab's tag object.
         */
        public abstract java.lang.Object getTag();

        /**
         * Set the {@link TabListener} that will handle switching to and from this tab.
         * All tabs must have a TabListener set before being added to the ActionBar.
         *
         * @param listener
         * 		Listener to handle tab selection events
         * @return The current instance for call chaining
         */
        public abstract android.app.ActionBar.Tab setTabListener(android.app.ActionBar.TabListener listener);

        /**
         * Select this tab. Only valid if the tab has been added to the action bar.
         */
        public abstract void select();

        /**
         * Set a description of this tab's content for use in accessibility support.
         * If no content description is provided the title will be used.
         *
         * @param resId
         * 		A resource ID referring to the description text
         * @return The current instance for call chaining
         * @see #setContentDescription(CharSequence)
         * @see #getContentDescription()
         */
        public abstract android.app.ActionBar.Tab setContentDescription(@android.annotation.StringRes
        int resId);

        /**
         * Set a description of this tab's content for use in accessibility support.
         * If no content description is provided the title will be used.
         *
         * @param contentDesc
         * 		Description of this tab's content
         * @return The current instance for call chaining
         * @see #setContentDescription(int)
         * @see #getContentDescription()
         */
        public abstract android.app.ActionBar.Tab setContentDescription(java.lang.CharSequence contentDesc);

        /**
         * Gets a brief description of this tab's content for use in accessibility support.
         *
         * @return Description of this tab's content
         * @see #setContentDescription(CharSequence)
         * @see #setContentDescription(int)
         */
        public abstract java.lang.CharSequence getContentDescription();
    }

    /**
     * Callback interface invoked when a tab is focused, unfocused, added, or removed.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
    toolbar action bars. Consider using other
    <a href="http://developer.android.com/design/patterns/navigation.html">common
    navigation patterns</a> instead.
     */
    public interface TabListener {
        /**
         * Called when a tab enters the selected state.
         *
         * @param tab
         * 		The tab that was selected
         * @param ft
         * 		A {@link FragmentTransaction} for queuing fragment operations to execute
         * 		during a tab switch. The previous tab's unselect and this tab's select will be
         * 		executed in a single transaction. This FragmentTransaction does not support
         * 		being added to the back stack.
         */
        public void onTabSelected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft);

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab
         * 		The tab that was unselected
         * @param ft
         * 		A {@link FragmentTransaction} for queuing fragment operations to execute
         * 		during a tab switch. This tab's unselect and the newly selected tab's select
         * 		will be executed in a single transaction. This FragmentTransaction does not
         * 		support being added to the back stack.
         */
        public void onTabUnselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft);

        /**
         * Called when a tab that is already selected is chosen again by the user.
         * Some applications may use this action to return to the top level of a category.
         *
         * @param tab
         * 		The tab that was reselected.
         * @param ft
         * 		A {@link FragmentTransaction} for queuing fragment operations to execute
         * 		once this method returns. This FragmentTransaction does not support
         * 		being added to the back stack.
         */
        public void onTabReselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft);
    }

    /**
     * Per-child layout information associated with action bar custom views.
     *
     * @unknown ref android.R.styleable#ActionBar_LayoutParams_layout_gravity
     */
    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        /**
         * Gravity for the view associated with these LayoutParams.
         *
         * @see android.view.Gravity
         */
        @android.view.ViewDebug.ExportedProperty(category = "layout", mapping = { @android.view.ViewDebug.IntToString(from = -1, to = "NONE"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.NO_GRAVITY, to = "NONE"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.TOP, to = "TOP"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.BOTTOM, to = "BOTTOM"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.LEFT, to = "LEFT"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.RIGHT, to = "RIGHT"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.START, to = "START"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.END, to = "END"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.CENTER_VERTICAL, to = "CENTER_VERTICAL"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.FILL_VERTICAL, to = "FILL_VERTICAL"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.CENTER_HORIZONTAL, to = "CENTER_HORIZONTAL"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.FILL_HORIZONTAL, to = "FILL_HORIZONTAL"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.CENTER, to = "CENTER"), @android.view.ViewDebug.IntToString(from = android.view.Gravity.FILL, to = "FILL") })
        public int gravity = android.view.Gravity.NO_GRAVITY;

        public LayoutParams(@android.annotation.NonNull
        android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ActionBar_LayoutParams);
            gravity = a.getInt(com.android.internal.R.styleable.ActionBar_LayoutParams_layout_gravity, android.view.Gravity.NO_GRAVITY);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = android.view.Gravity.CENTER_VERTICAL | android.view.Gravity.START;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, gravity);
        }

        public LayoutParams(android.app.ActionBar.LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        /* Note for framework developers:

        You might notice that ActionBar.LayoutParams is missing a constructor overload
        for MarginLayoutParams. While it may seem like a good idea to add one, at this
        point it's dangerous for source compatibility. Upon building against a new
        version of the SDK an app can end up statically linking to the new MarginLayoutParams
        overload, causing a crash when running on older platform versions with no other changes.
         */
        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        protected void encodeProperties(@android.annotation.NonNull
        android.view.ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("gravity", gravity);
        }
    }

    /**
     * Tracks the focused View until it leaves the ActionBar, then it resets the
     * touchscreenBlocksFocus value.
     */
    private static class FollowOutOfActionBar implements android.view.View.OnFocusChangeListener , java.lang.Runnable {
        private final android.view.ViewGroup mFocusRoot;

        private final android.view.ViewGroup mContainer;

        private final android.view.ViewGroup mToolbar;

        public FollowOutOfActionBar(android.view.ViewGroup focusRoot, android.view.ViewGroup container, android.view.ViewGroup toolbar) {
            mContainer = container;
            mToolbar = toolbar;
            mFocusRoot = focusRoot;
        }

        @java.lang.Override
        public void onFocusChange(android.view.View v, boolean hasFocus) {
            if (!hasFocus) {
                v.setOnFocusChangeListener(null);
                final android.view.View focused = mFocusRoot.findFocus();
                if (focused != null) {
                    focused.setOnFocusChangeListener(this);
                } else {
                    mFocusRoot.post(this);
                }
            }
        }

        @java.lang.Override
        public void run() {
            if (mContainer != null) {
                mContainer.setTouchscreenBlocksFocus(true);
            }
            if (mToolbar != null) {
                mToolbar.setTouchscreenBlocksFocus(true);
            }
        }
    }
}

