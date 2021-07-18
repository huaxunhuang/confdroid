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
 * {@link ViewAnimator} that switches between two views, and has a factory
 * from which these views are created.  You can either use the factory to
 * create the views, or add them yourself.  A ViewSwitcher can only have two
 * child views, of which only one is shown at a time.
 */
public class ViewSwitcher extends android.widget.ViewAnimator {
    /**
     * The factory used to create the two children.
     */
    android.widget.ViewSwitcher.ViewFactory mFactory;

    /**
     * Creates a new empty ViewSwitcher.
     *
     * @param context
     * 		the application's environment
     */
    public ViewSwitcher(android.content.Context context) {
        super(context);
    }

    /**
     * Creates a new empty ViewSwitcher for the given context and with the
     * specified set attributes.
     *
     * @param context
     * 		the application environment
     * @param attrs
     * 		a collection of attributes
     */
    public ViewSwitcher(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc }
     *
     * @throws IllegalStateException
     * 		if this switcher already contains two children
     */
    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new java.lang.IllegalStateException("Can't add more than 2 views to a ViewSwitcher");
        }
        super.addView(child, index, params);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ViewSwitcher.class.getName();
    }

    /**
     * Returns the next view to be displayed.
     *
     * @return the view that will be displayed after the next views flip.
     */
    public android.view.View getNextView() {
        int which = (mWhichChild == 0) ? 1 : 0;
        return getChildAt(which);
    }

    private android.view.View obtainView() {
        android.view.View child = mFactory.makeView();
        android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (child.getLayoutParams()));
        if (lp == null) {
            lp = new android.widget.FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        addView(child, lp);
        return child;
    }

    /**
     * Sets the factory used to create the two views between which the
     * ViewSwitcher will flip. Instead of using a factory, you can call
     * {@link #addView(android.view.View, int, android.view.ViewGroup.LayoutParams)}
     * twice.
     *
     * @param factory
     * 		the view factory used to generate the switcher's content
     */
    public void setFactory(android.widget.ViewSwitcher.ViewFactory factory) {
        mFactory = factory;
        obtainView();
        obtainView();
    }

    /**
     * Reset the ViewSwitcher to hide all of the existing views and to make it
     * think that the first time animation has not yet played.
     */
    public void reset() {
        mFirstTime = true;
        android.view.View v;
        v = getChildAt(0);
        if (v != null) {
            v.setVisibility(android.view.View.GONE);
        }
        v = getChildAt(1);
        if (v != null) {
            v.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Creates views in a ViewSwitcher.
     */
    public interface ViewFactory {
        /**
         * Creates a new {@link android.view.View} to be added in a
         * {@link android.widget.ViewSwitcher}.
         *
         * @return a {@link android.view.View}
         */
        android.view.View makeView();
    }
}

