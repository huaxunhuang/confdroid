/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v13.app;


/**
 * Version of {@link android.support.v4.app.FragmentTabHost} that can be
 * used with the platform {@link android.app.Fragment} APIs.  You will not
 * normally use this, instead using action bar tabs.
 */
public class FragmentTabHost extends android.widget.TabHost implements android.widget.TabHost.OnTabChangeListener {
    private final java.util.ArrayList<android.support.v13.app.FragmentTabHost.TabInfo> mTabs = new java.util.ArrayList<android.support.v13.app.FragmentTabHost.TabInfo>();

    private android.widget.FrameLayout mRealTabContent;

    private android.content.Context mContext;

    private android.app.FragmentManager mFragmentManager;

    private int mContainerId;

    private android.widget.TabHost.OnTabChangeListener mOnTabChangeListener;

    private android.support.v13.app.FragmentTabHost.TabInfo mLastTab;

    private boolean mAttached;

    static final class TabInfo {
        final java.lang.String tag;

        final java.lang.Class<?> clss;

        final android.os.Bundle args;

        android.app.Fragment fragment;

        TabInfo(java.lang.String _tag, java.lang.Class<?> _class, android.os.Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    static class DummyTabFactory implements android.widget.TabHost.TabContentFactory {
        private final android.content.Context mContext;

        public DummyTabFactory(android.content.Context context) {
            mContext = context;
        }

        @java.lang.Override
        public android.view.View createTabContent(java.lang.String tag) {
            android.view.View v = new android.view.View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    static class SavedState extends android.view.View.BaseSavedState {
        java.lang.String curTab;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        SavedState(android.os.Parcel in) {
            super(in);
            curTab = in.readString();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(curTab);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("FragmentTabHost.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " curTab=") + curTab) + "}";
        }

        public static final android.os.Parcelable.Creator<android.support.v13.app.FragmentTabHost.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v13.app.FragmentTabHost.SavedState>() {
            @java.lang.Override
            public android.support.v13.app.FragmentTabHost.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v13.app.FragmentTabHost.SavedState(in);
            }

            @java.lang.Override
            public android.support.v13.app.FragmentTabHost.SavedState[] newArray(int size) {
                return new android.support.v13.app.FragmentTabHost.SavedState[size];
            }
        };
    }

    public FragmentTabHost(android.content.Context context) {
        // Note that we call through to the version that takes an AttributeSet,
        // because the simple Context construct can result in a broken object!
        super(context, null);
        initFragmentTabHost(context, null);
    }

    public FragmentTabHost(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        initFragmentTabHost(context, attrs);
    }

    private void initFragmentTabHost(android.content.Context context, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, new int[]{ android.R.attr.inflatedId }, 0, 0);
        mContainerId = a.getResourceId(0, 0);
        a.recycle();
        super.setOnTabChangedListener(this);
    }

    private void ensureHierarchy(android.content.Context context) {
        // If owner hasn't made its own view hierarchy, then as a convenience
        // we will construct a standard one here.
        if (findViewById(android.R.id.tabs) == null) {
            android.widget.LinearLayout ll = new android.widget.LinearLayout(context);
            ll.setOrientation(android.widget.LinearLayout.VERTICAL);
            addView(ll, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            android.widget.TabWidget tw = new android.widget.TabWidget(context);
            tw.setId(android.R.id.tabs);
            tw.setOrientation(android.widget.TabWidget.HORIZONTAL);
            ll.addView(tw, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
            android.widget.FrameLayout fl = new android.widget.FrameLayout(context);
            fl.setId(android.R.id.tabcontent);
            ll.addView(fl, new android.widget.LinearLayout.LayoutParams(0, 0, 0));
            mRealTabContent = fl = new android.widget.FrameLayout(context);
            mRealTabContent.setId(mContainerId);
            ll.addView(fl, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        }
    }

    /**
     *
     *
     * @deprecated Don't call the original TabHost setup, you must instead
    call {@link #setup(Context, FragmentManager)} or
    {@link #setup(Context, FragmentManager, int)}.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public void setup() {
        throw new java.lang.IllegalStateException("Must call setup() that takes a Context and FragmentManager");
    }

    public void setup(android.content.Context context, android.app.FragmentManager manager) {
        ensureHierarchy(context);// Ensure views required by super.setup()

        super.setup();
        mContext = context;
        mFragmentManager = manager;
        ensureContent();
    }

    public void setup(android.content.Context context, android.app.FragmentManager manager, int containerId) {
        ensureHierarchy(context);// Ensure views required by super.setup()

        super.setup();
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
        ensureContent();
        mRealTabContent.setId(containerId);
        // We must have an ID to be able to save/restore our state.  If
        // the owner hasn't set one at this point, we will set it ourselves.
        if (getId() == android.view.View.NO_ID) {
            setId(android.R.id.tabhost);
        }
    }

    private void ensureContent() {
        if (mRealTabContent == null) {
            mRealTabContent = ((android.widget.FrameLayout) (findViewById(mContainerId)));
            if (mRealTabContent == null) {
                throw new java.lang.IllegalStateException("No tab content FrameLayout found for id " + mContainerId);
            }
        }
    }

    @java.lang.Override
    public void setOnTabChangedListener(android.widget.TabHost.OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }

    public void addTab(android.widget.TabHost.TabSpec tabSpec, java.lang.Class<?> clss, android.os.Bundle args) {
        tabSpec.setContent(new android.support.v13.app.FragmentTabHost.DummyTabFactory(mContext));
        java.lang.String tag = tabSpec.getTag();
        android.support.v13.app.FragmentTabHost.TabInfo info = new android.support.v13.app.FragmentTabHost.TabInfo(tag, clss, args);
        if (mAttached) {
            // If we are already attached to the window, then check to make
            // sure this tab's fragment is inactive if it exists.  This shouldn't
            // normally happen.
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if ((info.fragment != null) && (!info.fragment.isDetached())) {
                android.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
        }
        mTabs.add(info);
        addTab(tabSpec);
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        java.lang.String currentTab = getCurrentTabTag();
        // Go through all tabs and make sure their fragments match
        // the correct state.
        android.app.FragmentTransaction ft = null;
        for (int i = 0; i < mTabs.size(); i++) {
            android.support.v13.app.FragmentTabHost.TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if ((tab.fragment != null) && (!tab.fragment.isDetached())) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab.  Nothing to do.
                    mLastTab = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab.  Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.detach(tab.fragment);
                }
            }
        }
        // We are now ready to go.  Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.support.v13.app.FragmentTabHost.SavedState ss = new android.support.v13.app.FragmentTabHost.SavedState(superState);
        ss.curTab = getCurrentTabTag();
        return ss;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v13.app.FragmentTabHost.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v13.app.FragmentTabHost.SavedState ss = ((android.support.v13.app.FragmentTabHost.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentTabByTag(ss.curTab);
    }

    @java.lang.Override
    public void onTabChanged(java.lang.String tabId) {
        if (mAttached) {
            android.app.FragmentTransaction ft = doTabChanged(tabId, null);
            if (ft != null) {
                ft.commit();
            }
        }
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(tabId);
        }
    }

    private android.app.FragmentTransaction doTabChanged(java.lang.String tabId, android.app.FragmentTransaction ft) {
        android.support.v13.app.FragmentTabHost.TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            android.support.v13.app.FragmentTabHost.TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new java.lang.IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = android.app.Fragment.instantiate(mContext, newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }
            mLastTab = newTab;
        }
        return ft;
    }
}

