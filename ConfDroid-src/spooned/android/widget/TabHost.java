/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.widget;


/**
 * Container for a tabbed window view. This object holds two children: a set of tab labels that the
 * user clicks to select a specific tab, and a FrameLayout object that displays the contents of that
 * page. The individual elements are typically controlled using this container object, rather than
 * setting values on the child elements themselves.
 */
public class TabHost extends android.widget.FrameLayout implements android.view.ViewTreeObserver.OnTouchModeChangeListener {
    private static final int TABWIDGET_LOCATION_LEFT = 0;

    private static final int TABWIDGET_LOCATION_TOP = 1;

    private static final int TABWIDGET_LOCATION_RIGHT = 2;

    private static final int TABWIDGET_LOCATION_BOTTOM = 3;

    private android.widget.TabWidget mTabWidget;

    private android.widget.FrameLayout mTabContent;

    @android.annotation.UnsupportedAppUsage
    private java.util.List<android.widget.TabHost.TabSpec> mTabSpecs = new java.util.ArrayList<android.widget.TabHost.TabSpec>(2);

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.annotation.UnsupportedAppUsage
    protected int mCurrentTab = -1;

    private android.view.View mCurrentView = null;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    protected android.app.LocalActivityManager mLocalActivityManager = null;

    @android.annotation.UnsupportedAppUsage
    private android.widget.TabHost.OnTabChangeListener mOnTabChangeListener;

    private android.view.View.OnKeyListener mTabKeyListener;

    private int mTabLayoutId;

    public TabHost(android.content.Context context) {
        super(context);
        initTabHost();
    }

    public TabHost(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.tabWidgetStyle);
    }

    public TabHost(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TabHost(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TabWidget, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.TabWidget, attrs, a, defStyleAttr, defStyleRes);
        mTabLayoutId = a.getResourceId(R.styleable.TabWidget_tabLayout, 0);
        a.recycle();
        if (mTabLayoutId == 0) {
            // In case the tabWidgetStyle does not inherit from Widget.TabWidget and tabLayout is
            // not defined.
            mTabLayoutId = R.layout.tab_indicator_holo;
        }
        initTabHost();
    }

    private void initTabHost() {
        setFocusableInTouchMode(true);
        setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mCurrentTab = -1;
        mCurrentView = null;
    }

    /**
     * Creates a new {@link TabSpec} associated with this tab host.
     *
     * @param tag
     * 		tag for the tab specification, must be non-null
     * @throws IllegalArgumentException
     * 		If the passed tag is null
     */
    @android.annotation.NonNull
    public android.widget.TabHost.TabSpec newTabSpec(@android.annotation.NonNull
    java.lang.String tag) {
        if (tag == null) {
            throw new java.lang.IllegalArgumentException("tag must be non-null");
        }
        return new android.widget.TabHost.TabSpec(tag);
    }

    /**
     * <p>Call setup() before adding tabs if loading TabHost using findViewById().
     * <i><b>However</i></b>: You do not need to call setup() after getTabHost()
     * in {@link android.app.TabActivity TabActivity}.
     * Example:</p>
     * <pre>mTabHost = (TabHost)findViewById(R.id.tabhost);
     * mTabHost.setup();
     * mTabHost.addTab(TAB_TAG_1, "Hello, world!", "Tab 1");
     */
    public void setup() {
        mTabWidget = findViewById(com.android.internal.R.id.tabs);
        if (mTabWidget == null) {
            throw new java.lang.RuntimeException("Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
        }
        // KeyListener to attach to all tabs. Detects non-navigation keys
        // and relays them to the tab content.
        mTabKeyListener = new android.view.View.OnKeyListener() {
            public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
                if (android.view.KeyEvent.isModifierKey(keyCode)) {
                    return false;
                }
                switch (keyCode) {
                    case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                    case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                    case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                    case android.view.KeyEvent.KEYCODE_DPAD_UP :
                    case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                    case android.view.KeyEvent.KEYCODE_TAB :
                    case android.view.KeyEvent.KEYCODE_SPACE :
                    case android.view.KeyEvent.KEYCODE_ENTER :
                        return false;
                }
                mTabContent.requestFocus(android.view.View.FOCUS_FORWARD);
                return mTabContent.dispatchKeyEvent(event);
            }
        };
        mTabWidget.setTabSelectionListener(new android.widget.TabWidget.OnTabSelectionChanged() {
            public void onTabSelectionChanged(int tabIndex, boolean clicked) {
                setCurrentTab(tabIndex);
                if (clicked) {
                    mTabContent.requestFocus(android.view.View.FOCUS_FORWARD);
                }
            }
        });
        mTabContent = findViewById(com.android.internal.R.id.tabcontent);
        if (mTabContent == null) {
            throw new java.lang.RuntimeException("Your TabHost must have a FrameLayout whose id attribute is " + "'android.R.id.tabcontent'");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendAccessibilityEventInternal(int eventType) {
        /* avoid super class behavior - TabWidget sends the right events */
    }

    /**
     * If you are using {@link TabSpec#setContent(android.content.Intent)}, this
     * must be called since the activityGroup is needed to launch the local activity.
     *
     * This is done for you if you extend {@link android.app.TabActivity}.
     *
     * @param activityGroup
     * 		Used to launch activities for tab content.
     */
    public void setup(android.app.LocalActivityManager activityGroup) {
        setup();
        mLocalActivityManager = activityGroup;
    }

    @java.lang.Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        // No longer used, but kept to maintain API compatibility.
    }

    /**
     * Add a tab.
     *
     * @param tabSpec
     * 		Specifies how to create the indicator and content.
     * @throws IllegalArgumentException
     * 		If the passed tab spec has null indicator strategy and / or
     * 		null content strategy.
     */
    public void addTab(android.widget.TabHost.TabSpec tabSpec) {
        if (tabSpec.mIndicatorStrategy == null) {
            throw new java.lang.IllegalArgumentException("you must specify a way to create the tab indicator.");
        }
        if (tabSpec.mContentStrategy == null) {
            throw new java.lang.IllegalArgumentException("you must specify a way to create the tab content");
        }
        android.view.View tabIndicator = tabSpec.mIndicatorStrategy.createIndicatorView();
        tabIndicator.setOnKeyListener(mTabKeyListener);
        // If this is a custom view, then do not draw the bottom strips for
        // the tab indicators.
        if (tabSpec.mIndicatorStrategy instanceof android.widget.TabHost.ViewIndicatorStrategy) {
            mTabWidget.setStripEnabled(false);
        }
        mTabWidget.addView(tabIndicator);
        mTabSpecs.add(tabSpec);
        if (mCurrentTab == (-1)) {
            setCurrentTab(0);
        }
    }

    /**
     * Removes all tabs from the tab widget associated with this tab host.
     */
    public void clearAllTabs() {
        mTabWidget.removeAllViews();
        initTabHost();
        mTabContent.removeAllViews();
        mTabSpecs.clear();
        requestLayout();
        invalidate();
    }

    public android.widget.TabWidget getTabWidget() {
        return mTabWidget;
    }

    /**
     * Returns the current tab.
     *
     * @return the current tab, may be {@code null} if no tab is set as current
     */
    @android.annotation.Nullable
    public int getCurrentTab() {
        return mCurrentTab;
    }

    /**
     * Returns the tag for the current tab.
     *
     * @return the tag for the current tab, may be {@code null} if no tab is
    set as current
     */
    @android.annotation.Nullable
    public java.lang.String getCurrentTabTag() {
        if ((mCurrentTab >= 0) && (mCurrentTab < mTabSpecs.size())) {
            return mTabSpecs.get(mCurrentTab).getTag();
        }
        return null;
    }

    /**
     * Returns the view for the current tab.
     *
     * @return the view for the current tab, may be {@code null} if no tab is
    set as current
     */
    @android.annotation.Nullable
    public android.view.View getCurrentTabView() {
        if ((mCurrentTab >= 0) && (mCurrentTab < mTabSpecs.size())) {
            return mTabWidget.getChildTabViewAt(mCurrentTab);
        }
        return null;
    }

    public android.view.View getCurrentView() {
        return mCurrentView;
    }

    /**
     * Sets the current tab based on its tag.
     *
     * @param tag
     * 		the tag for the tab to set as current
     */
    public void setCurrentTabByTag(java.lang.String tag) {
        for (int i = 0, count = mTabSpecs.size(); i < count; i++) {
            if (mTabSpecs.get(i).getTag().equals(tag)) {
                setCurrentTab(i);
                break;
            }
        }
    }

    /**
     * Get the FrameLayout which holds tab content
     */
    public android.widget.FrameLayout getTabContentView() {
        return mTabContent;
    }

    /**
     * Get the location of the TabWidget.
     *
     * @return The TabWidget location.
     */
    private int getTabWidgetLocation() {
        int location = android.widget.TabHost.TABWIDGET_LOCATION_TOP;
        switch (mTabWidget.getOrientation()) {
            case android.widget.LinearLayout.VERTICAL :
                location = (mTabContent.getLeft() < mTabWidget.getLeft()) ? android.widget.TabHost.TABWIDGET_LOCATION_RIGHT : android.widget.TabHost.TABWIDGET_LOCATION_LEFT;
                break;
            case android.widget.LinearLayout.HORIZONTAL :
            default :
                location = (mTabContent.getTop() < mTabWidget.getTop()) ? android.widget.TabHost.TABWIDGET_LOCATION_BOTTOM : android.widget.TabHost.TABWIDGET_LOCATION_TOP;
                break;
        }
        return location;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        final boolean handled = super.dispatchKeyEvent(event);
        // unhandled key events change focus to tab indicator for embedded
        // activities when there is nothing that will take focus from default
        // focus searching
        if (((((!handled) && (event.getAction() == android.view.KeyEvent.ACTION_DOWN)) && (mCurrentView != null)) && mCurrentView.isRootNamespace()) && mCurrentView.hasFocus()) {
            int keyCodeShouldChangeFocus = android.view.KeyEvent.KEYCODE_DPAD_UP;
            int directionShouldChangeFocus = android.view.View.FOCUS_UP;
            int soundEffect = android.view.SoundEffectConstants.NAVIGATION_UP;
            switch (getTabWidgetLocation()) {
                case android.widget.TabHost.TABWIDGET_LOCATION_LEFT :
                    keyCodeShouldChangeFocus = android.view.KeyEvent.KEYCODE_DPAD_LEFT;
                    directionShouldChangeFocus = android.view.View.FOCUS_LEFT;
                    soundEffect = android.view.SoundEffectConstants.NAVIGATION_LEFT;
                    break;
                case android.widget.TabHost.TABWIDGET_LOCATION_RIGHT :
                    keyCodeShouldChangeFocus = android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
                    directionShouldChangeFocus = android.view.View.FOCUS_RIGHT;
                    soundEffect = android.view.SoundEffectConstants.NAVIGATION_RIGHT;
                    break;
                case android.widget.TabHost.TABWIDGET_LOCATION_BOTTOM :
                    keyCodeShouldChangeFocus = android.view.KeyEvent.KEYCODE_DPAD_DOWN;
                    directionShouldChangeFocus = android.view.View.FOCUS_DOWN;
                    soundEffect = android.view.SoundEffectConstants.NAVIGATION_DOWN;
                    break;
                case android.widget.TabHost.TABWIDGET_LOCATION_TOP :
                default :
                    keyCodeShouldChangeFocus = android.view.KeyEvent.KEYCODE_DPAD_UP;
                    directionShouldChangeFocus = android.view.View.FOCUS_UP;
                    soundEffect = android.view.SoundEffectConstants.NAVIGATION_UP;
                    break;
            }
            if ((event.getKeyCode() == keyCodeShouldChangeFocus) && (mCurrentView.findFocus().focusSearch(directionShouldChangeFocus) == null)) {
                mTabWidget.getChildTabViewAt(mCurrentTab).requestFocus();
                playSoundEffect(soundEffect);
                return true;
            }
        }
        return handled;
    }

    @java.lang.Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        if (mCurrentView != null) {
            mCurrentView.dispatchWindowFocusChanged(hasFocus);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.TabHost.class.getName();
    }

    public void setCurrentTab(int index) {
        if ((index < 0) || (index >= mTabSpecs.size())) {
            return;
        }
        if (index == mCurrentTab) {
            return;
        }
        // notify old tab content
        if (mCurrentTab != (-1)) {
            mTabSpecs.get(mCurrentTab).mContentStrategy.tabClosed();
        }
        mCurrentTab = index;
        final android.widget.TabHost.TabSpec spec = mTabSpecs.get(index);
        // Call the tab widget's focusCurrentTab(), instead of just
        // selecting the tab.
        mTabWidget.focusCurrentTab(mCurrentTab);
        // tab content
        mCurrentView = spec.mContentStrategy.getContentView();
        if (mCurrentView.getParent() == null) {
            mTabContent.addView(mCurrentView, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (!mTabWidget.hasFocus()) {
            // if the tab widget didn't take focus (likely because we're in touch mode)
            // give the current tab content view a shot
            mCurrentView.requestFocus();
        }
        // mTabContent.requestFocus(View.FOCUS_FORWARD);
        invokeOnTabChangeListener();
    }

    /**
     * Register a callback to be invoked when the selected state of any of the items
     * in this list changes
     *
     * @param l
     * 		The callback that will run
     */
    public void setOnTabChangedListener(android.widget.TabHost.OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }

    private void invokeOnTabChangeListener() {
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(getCurrentTabTag());
        }
    }

    /**
     * Interface definition for a callback to be invoked when tab changed
     */
    public interface OnTabChangeListener {
        void onTabChanged(java.lang.String tabId);
    }

    /**
     * Makes the content of a tab when it is selected. Use this if your tab
     * content needs to be created on demand, i.e. you are not showing an
     * existing view or starting an activity.
     */
    public interface TabContentFactory {
        /**
         * Callback to make the tab contents
         *
         * @param tag
         * 		Which tab was selected.
         * @return The view to display the contents of the selected tab.
         */
        android.view.View createTabContent(java.lang.String tag);
    }

    /**
     * A tab has a tab indicator, content, and a tag that is used to keep
     * track of it.  This builder helps choose among these options.
     *
     * For the tab indicator, your choices are:
     * 1) set a label
     * 2) set a label and an icon
     *
     * For the tab content, your choices are:
     * 1) the id of a {@link View}
     * 2) a {@link TabContentFactory} that creates the {@link View} content.
     * 3) an {@link Intent} that launches an {@link android.app.Activity}.
     */
    public class TabSpec {
        @android.annotation.NonNull
        private final java.lang.String mTag;

        @android.annotation.UnsupportedAppUsage
        private android.widget.TabHost.IndicatorStrategy mIndicatorStrategy;

        @android.annotation.UnsupportedAppUsage
        private android.widget.TabHost.ContentStrategy mContentStrategy;

        /**
         * Constructs a new tab specification with the specified tag.
         *
         * @param tag
         * 		the tag for the tag specification, must be non-null
         */
        private TabSpec(@android.annotation.NonNull
        java.lang.String tag) {
            mTag = tag;
        }

        /**
         * Specify a label as the tab indicator.
         */
        public android.widget.TabHost.TabSpec setIndicator(java.lang.CharSequence label) {
            mIndicatorStrategy = new android.widget.TabHost.LabelIndicatorStrategy(label);
            return this;
        }

        /**
         * Specify a label and icon as the tab indicator.
         */
        public android.widget.TabHost.TabSpec setIndicator(java.lang.CharSequence label, android.graphics.drawable.Drawable icon) {
            mIndicatorStrategy = new android.widget.TabHost.LabelAndIconIndicatorStrategy(label, icon);
            return this;
        }

        /**
         * Specify a view as the tab indicator.
         */
        public android.widget.TabHost.TabSpec setIndicator(android.view.View view) {
            mIndicatorStrategy = new android.widget.TabHost.ViewIndicatorStrategy(view);
            return this;
        }

        /**
         * Specify the id of the view that should be used as the content
         * of the tab.
         */
        public android.widget.TabHost.TabSpec setContent(int viewId) {
            mContentStrategy = new android.widget.TabHost.ViewIdContentStrategy(viewId);
            return this;
        }

        /**
         * Specify a {@link android.widget.TabHost.TabContentFactory} to use to
         * create the content of the tab.
         */
        public android.widget.TabHost.TabSpec setContent(android.widget.TabHost.TabContentFactory contentFactory) {
            mContentStrategy = new android.widget.TabHost.FactoryContentStrategy(mTag, contentFactory);
            return this;
        }

        /**
         * Specify an intent to use to launch an activity as the tab content.
         */
        public android.widget.TabHost.TabSpec setContent(android.content.Intent intent) {
            mContentStrategy = new android.widget.TabHost.IntentContentStrategy(mTag, intent);
            return this;
        }

        /**
         * Returns the tag for this tab specification.
         *
         * @return the tag for this tab specification
         */
        @android.annotation.NonNull
        public java.lang.String getTag() {
            return mTag;
        }
    }

    /**
     * Specifies what you do to create a tab indicator.
     */
    private static interface IndicatorStrategy {
        /**
         * Return the view for the indicator.
         */
        android.view.View createIndicatorView();
    }

    /**
     * Specifies what you do to manage the tab content.
     */
    private static interface ContentStrategy {
        /**
         * Return the content view.  The view should may be cached locally.
         */
        android.view.View getContentView();

        /**
         * Perhaps do something when the tab associated with this content has
         * been closed (i.e make it invisible, or remove it).
         */
        void tabClosed();
    }

    /**
     * How to create a tab indicator that just has a label.
     */
    private class LabelIndicatorStrategy implements android.widget.TabHost.IndicatorStrategy {
        private final java.lang.CharSequence mLabel;

        private LabelIndicatorStrategy(java.lang.CharSequence label) {
            mLabel = label;
        }

        public android.view.View createIndicatorView() {
            final android.content.Context context = getContext();
            android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            android.view.View tabIndicator = // tab widget is the parent
            inflater.inflate(mTabLayoutId, mTabWidget, false);// no inflate params

            final android.widget.TextView tv = tabIndicator.findViewById(R.id.title);
            tv.setText(mLabel);
            if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
                // Donut apps get old color scheme
                tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getColorStateList(R.color.tab_indicator_text_v4));
            }
            return tabIndicator;
        }
    }

    /**
     * How we create a tab indicator that has a label and an icon
     */
    private class LabelAndIconIndicatorStrategy implements android.widget.TabHost.IndicatorStrategy {
        private final java.lang.CharSequence mLabel;

        private final android.graphics.drawable.Drawable mIcon;

        private LabelAndIconIndicatorStrategy(java.lang.CharSequence label, android.graphics.drawable.Drawable icon) {
            mLabel = label;
            mIcon = icon;
        }

        public android.view.View createIndicatorView() {
            final android.content.Context context = getContext();
            android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            android.view.View tabIndicator = // tab widget is the parent
            inflater.inflate(mTabLayoutId, mTabWidget, false);// no inflate params

            final android.widget.TextView tv = tabIndicator.findViewById(R.id.title);
            final android.widget.ImageView iconView = tabIndicator.findViewById(R.id.icon);
            // when icon is gone by default, we're in exclusive mode
            final boolean exclusive = iconView.getVisibility() == android.view.View.GONE;
            final boolean bindIcon = (!exclusive) || android.text.TextUtils.isEmpty(mLabel);
            tv.setText(mLabel);
            if (bindIcon && (mIcon != null)) {
                iconView.setImageDrawable(mIcon);
                iconView.setVisibility(android.view.View.VISIBLE);
            }
            if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
                // Donut apps get old color scheme
                tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getColorStateList(R.color.tab_indicator_text_v4));
            }
            return tabIndicator;
        }
    }

    /**
     * How to create a tab indicator by specifying a view.
     */
    private class ViewIndicatorStrategy implements android.widget.TabHost.IndicatorStrategy {
        private final android.view.View mView;

        private ViewIndicatorStrategy(android.view.View view) {
            mView = view;
        }

        public android.view.View createIndicatorView() {
            return mView;
        }
    }

    /**
     * How to create the tab content via a view id.
     */
    private class ViewIdContentStrategy implements android.widget.TabHost.ContentStrategy {
        private final android.view.View mView;

        private ViewIdContentStrategy(int viewId) {
            mView = mTabContent.findViewById(viewId);
            if (mView != null) {
                mView.setVisibility(android.view.View.GONE);
            } else {
                throw new java.lang.RuntimeException(("Could not create tab content because " + "could not find view with id ") + viewId);
            }
        }

        public android.view.View getContentView() {
            mView.setVisibility(android.view.View.VISIBLE);
            return mView;
        }

        public void tabClosed() {
            mView.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * How tab content is managed using {@link TabContentFactory}.
     */
    private class FactoryContentStrategy implements android.widget.TabHost.ContentStrategy {
        private android.view.View mTabContent;

        private final java.lang.CharSequence mTag;

        private android.widget.TabHost.TabContentFactory mFactory;

        public FactoryContentStrategy(java.lang.CharSequence tag, android.widget.TabHost.TabContentFactory factory) {
            mTag = tag;
            mFactory = factory;
        }

        public android.view.View getContentView() {
            if (mTabContent == null) {
                mTabContent = mFactory.createTabContent(mTag.toString());
            }
            mTabContent.setVisibility(android.view.View.VISIBLE);
            return mTabContent;
        }

        public void tabClosed() {
            mTabContent.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * How tab content is managed via an {@link Intent}: the content view is the
     * decorview of the launched activity.
     */
    private class IntentContentStrategy implements android.widget.TabHost.ContentStrategy {
        private final java.lang.String mTag;

        private final android.content.Intent mIntent;

        private android.view.View mLaunchedView;

        private IntentContentStrategy(java.lang.String tag, android.content.Intent intent) {
            mTag = tag;
            mIntent = intent;
        }

        @android.annotation.UnsupportedAppUsage
        public android.view.View getContentView() {
            if (mLocalActivityManager == null) {
                throw new java.lang.IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
            }
            final android.view.Window w = mLocalActivityManager.startActivity(mTag, mIntent);
            final android.view.View wd = (w != null) ? w.getDecorView() : null;
            if ((mLaunchedView != wd) && (mLaunchedView != null)) {
                if (mLaunchedView.getParent() != null) {
                    mTabContent.removeView(mLaunchedView);
                }
            }
            mLaunchedView = wd;
            // XXX Set FOCUS_AFTER_DESCENDANTS on embedded activities for now so they can get
            // focus if none of their children have it. They need focus to be able to
            // display menu items.
            // 
            // Replace this with something better when Bug 628886 is fixed...
            // 
            if (mLaunchedView != null) {
                mLaunchedView.setVisibility(android.view.View.VISIBLE);
                mLaunchedView.setFocusableInTouchMode(true);
                ((android.view.ViewGroup) (mLaunchedView)).setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
            }
            return mLaunchedView;
        }

        @android.annotation.UnsupportedAppUsage
        public void tabClosed() {
            if (mLaunchedView != null) {
                mLaunchedView.setVisibility(android.view.View.GONE);
            }
        }
    }
}

