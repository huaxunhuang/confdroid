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
package android.support.v4.app;


/**
 *
 *
 * @deprecated Please use ActionBarDrawerToggle in support-v7-appcompat.

<p>
This class provides a handy way to tie together the functionality of
{@link DrawerLayout} and the framework <code>ActionBar</code> to implement the recommended
design for navigation drawers.

<p>To use <code>ActionBarDrawerToggle</code>, create one in your Activity and call through
to the following methods corresponding to your Activity callbacks:</p>

<ul>
<li>{@link Activity#onConfigurationChanged(android.content.res.Configuration) onConfigurationChanged}</li>
<li>{@link Activity#onOptionsItemSelected(android.view.MenuItem) onOptionsItemSelected}</li>
</ul>

<p>Call {@link #syncState()} from your <code>Activity</code>'s
{@link Activity#onPostCreate(android.os.Bundle) onPostCreate} to synchronize the indicator
with the state of the linked DrawerLayout after <code>onRestoreInstanceState</code>
has occurred.</p>

<p><code>ActionBarDrawerToggle</code> can be used directly as a
{@link DrawerLayout.DrawerListener}, or if you are already providing your own listener,
call through to each of the listener methods from your own.</p>
 */
@java.lang.Deprecated
public class ActionBarDrawerToggle implements android.support.v4.widget.DrawerLayout.DrawerListener {
    /**
     * Allows an implementing Activity to return an {@link ActionBarDrawerToggle.Delegate} to use
     * with ActionBarDrawerToggle.
     */
    public interface DelegateProvider {
        /**
         *
         *
         * @return Delegate to use for ActionBarDrawableToggles, or null if the Activity
        does not wish to override the default behavior.
         */
        @android.support.annotation.Nullable
        android.support.v4.app.ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
    }

    public interface Delegate {
        /**
         *
         *
         * @return Up indicator drawable as defined in the Activity's theme, or null if one is not
        defined.
         */
        @android.support.annotation.Nullable
        android.graphics.drawable.Drawable getThemeUpIndicator();

        /**
         * Set the Action Bar's up indicator drawable and content description.
         *
         * @param upDrawable
         * 		- Drawable to set as up indicator
         * @param contentDescRes
         * 		- Content description to set
         */
        void setActionBarUpIndicator(android.graphics.drawable.Drawable upDrawable, @android.support.annotation.StringRes
        int contentDescRes);

        /**
         * Set the Action Bar's up indicator content description.
         *
         * @param contentDescRes
         * 		- Content description to set
         */
        void setActionBarDescription(@android.support.annotation.StringRes
        int contentDescRes);
    }

    private interface ActionBarDrawerToggleImpl {
        android.graphics.drawable.Drawable getThemeUpIndicator(android.app.Activity activity);

        java.lang.Object setActionBarUpIndicator(java.lang.Object info, android.app.Activity activity, android.graphics.drawable.Drawable themeImage, int contentDescRes);

        java.lang.Object setActionBarDescription(java.lang.Object info, android.app.Activity activity, int contentDescRes);
    }

    private static class ActionBarDrawerToggleImplBase implements android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
        ActionBarDrawerToggleImplBase() {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getThemeUpIndicator(android.app.Activity activity) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object setActionBarUpIndicator(java.lang.Object info, android.app.Activity activity, android.graphics.drawable.Drawable themeImage, int contentDescRes) {
            // No action bar to set.
            return info;
        }

        @java.lang.Override
        public java.lang.Object setActionBarDescription(java.lang.Object info, android.app.Activity activity, int contentDescRes) {
            // No action bar to set
            return info;
        }
    }

    private static class ActionBarDrawerToggleImplHC implements android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
        ActionBarDrawerToggleImplHC() {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getThemeUpIndicator(android.app.Activity activity) {
            return android.support.v4.app.ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(activity);
        }

        @java.lang.Override
        public java.lang.Object setActionBarUpIndicator(java.lang.Object info, android.app.Activity activity, android.graphics.drawable.Drawable themeImage, int contentDescRes) {
            return android.support.v4.app.ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(info, activity, themeImage, contentDescRes);
        }

        @java.lang.Override
        public java.lang.Object setActionBarDescription(java.lang.Object info, android.app.Activity activity, int contentDescRes) {
            return android.support.v4.app.ActionBarDrawerToggleHoneycomb.setActionBarDescription(info, activity, contentDescRes);
        }
    }

    private static class ActionBarDrawerToggleImplJellybeanMR2 implements android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl {
        ActionBarDrawerToggleImplJellybeanMR2() {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getThemeUpIndicator(android.app.Activity activity) {
            return android.support.v4.app.ActionBarDrawerToggleJellybeanMR2.getThemeUpIndicator(activity);
        }

        @java.lang.Override
        public java.lang.Object setActionBarUpIndicator(java.lang.Object info, android.app.Activity activity, android.graphics.drawable.Drawable themeImage, int contentDescRes) {
            return android.support.v4.app.ActionBarDrawerToggleJellybeanMR2.setActionBarUpIndicator(info, activity, themeImage, contentDescRes);
        }

        @java.lang.Override
        public java.lang.Object setActionBarDescription(java.lang.Object info, android.app.Activity activity, int contentDescRes) {
            return android.support.v4.app.ActionBarDrawerToggleJellybeanMR2.setActionBarDescription(info, activity, contentDescRes);
        }
    }

    private static final android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 18) {
            IMPL = new android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImplJellybeanMR2();
        } else
            if (version >= 11) {
                IMPL = new android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImplHC();
            } else {
                IMPL = new android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImplBase();
            }

    }

    /**
     * Fraction of its total width by which to offset the toggle drawable.
     */
    private static final float TOGGLE_DRAWABLE_OFFSET = 1 / 3.0F;

    // android.R.id.home as defined by public API in v11
    private static final int ID_HOME = 0x102002c;

    final android.app.Activity mActivity;

    private final android.support.v4.app.ActionBarDrawerToggle.Delegate mActivityImpl;

    private final android.support.v4.widget.DrawerLayout mDrawerLayout;

    private boolean mDrawerIndicatorEnabled = true;

    private boolean mHasCustomUpIndicator;

    private android.graphics.drawable.Drawable mHomeAsUpIndicator;

    private android.graphics.drawable.Drawable mDrawerImage;

    private android.support.v4.app.ActionBarDrawerToggle.SlideDrawable mSlider;

    private final int mDrawerImageResource;

    private final int mOpenDrawerContentDescRes;

    private final int mCloseDrawerContentDescRes;

    private java.lang.Object mSetIndicatorInfo;

    /**
     * Construct a new ActionBarDrawerToggle.
     *
     * <p>The given {@link Activity} will be linked to the specified {@link DrawerLayout}.
     * The provided drawer indicator drawable will animate slightly off-screen as the drawer
     * is opened, indicating that in the open state the drawer will move off-screen when pressed
     * and in the closed state the drawer will move on-screen when pressed.</p>
     *
     * <p>String resources must be provided to describe the open/close drawer actions for
     * accessibility services.</p>
     *
     * @param activity
     * 		The Activity hosting the drawer
     * @param drawerLayout
     * 		The DrawerLayout to link to the given Activity's ActionBar
     * @param drawerImageRes
     * 		A Drawable resource to use as the drawer indicator
     * @param openDrawerContentDescRes
     * 		A String resource to describe the "open drawer" action
     * 		for accessibility
     * @param closeDrawerContentDescRes
     * 		A String resource to describe the "close drawer" action
     * 		for accessibility
     */
    public ActionBarDrawerToggle(android.app.Activity activity, android.support.v4.widget.DrawerLayout drawerLayout, @android.support.annotation.DrawableRes
    int drawerImageRes, @android.support.annotation.StringRes
    int openDrawerContentDescRes, @android.support.annotation.StringRes
    int closeDrawerContentDescRes) {
        this(activity, drawerLayout, !android.support.v4.app.ActionBarDrawerToggle.assumeMaterial(activity), drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    private static boolean assumeMaterial(android.content.Context context) {
        return (context.getApplicationInfo().targetSdkVersion >= 21) && (android.os.Build.VERSION.SDK_INT >= 21);
    }

    /**
     * Construct a new ActionBarDrawerToggle.
     *
     * <p>The given {@link Activity} will be linked to the specified {@link DrawerLayout}.
     * The provided drawer indicator drawable will animate slightly off-screen as the drawer
     * is opened, indicating that in the open state the drawer will move off-screen when pressed
     * and in the closed state the drawer will move on-screen when pressed.</p>
     *
     * <p>String resources must be provided to describe the open/close drawer actions for
     * accessibility services.</p>
     *
     * @param activity
     * 		The Activity hosting the drawer
     * @param drawerLayout
     * 		The DrawerLayout to link to the given Activity's ActionBar
     * @param animate
     * 		True to animate the drawer indicator along with the drawer's position.
     * 		Material apps should set this to false.
     * @param drawerImageRes
     * 		A Drawable resource to use as the drawer indicator
     * @param openDrawerContentDescRes
     * 		A String resource to describe the "open drawer" action
     * 		for accessibility
     * @param closeDrawerContentDescRes
     * 		A String resource to describe the "close drawer" action
     * 		for accessibility
     */
    public ActionBarDrawerToggle(android.app.Activity activity, android.support.v4.widget.DrawerLayout drawerLayout, boolean animate, @android.support.annotation.DrawableRes
    int drawerImageRes, @android.support.annotation.StringRes
    int openDrawerContentDescRes, @android.support.annotation.StringRes
    int closeDrawerContentDescRes) {
        mActivity = activity;
        // Allow the Activity to provide an impl
        if (activity instanceof android.support.v4.app.ActionBarDrawerToggle.DelegateProvider) {
            mActivityImpl = ((android.support.v4.app.ActionBarDrawerToggle.DelegateProvider) (activity)).getDrawerToggleDelegate();
        } else {
            mActivityImpl = null;
        }
        mDrawerLayout = drawerLayout;
        mDrawerImageResource = drawerImageRes;
        mOpenDrawerContentDescRes = openDrawerContentDescRes;
        mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        mHomeAsUpIndicator = getThemeUpIndicator();
        mDrawerImage = android.support.v4.content.ContextCompat.getDrawable(activity, drawerImageRes);
        mSlider = new android.support.v4.app.ActionBarDrawerToggle.SlideDrawable(mDrawerImage);
        mSlider.setOffset(animate ? android.support.v4.app.ActionBarDrawerToggle.TOGGLE_DRAWABLE_OFFSET : 0);
    }

    /**
     * Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.
     *
     * <p>This should be called from your <code>Activity</code>'s
     * {@link Activity#onPostCreate(android.os.Bundle) onPostCreate} method to synchronize after
     * the DrawerLayout's instance state has been restored, and any other time when the state
     * may have diverged in such a way that the ActionBarDrawerToggle was not notified.
     * (For example, if you stop forwarding appropriate drawer events for a period of time.)</p>
     */
    public void syncState() {
        if (mDrawerLayout.isDrawerOpen(android.support.v4.view.GravityCompat.START)) {
            mSlider.setPosition(1);
        } else {
            mSlider.setPosition(0);
        }
        if (mDrawerIndicatorEnabled) {
            setActionBarUpIndicator(mSlider, mDrawerLayout.isDrawerOpen(android.support.v4.view.GravityCompat.START) ? mCloseDrawerContentDescRes : mOpenDrawerContentDescRes);
        }
    }

    /**
     * Set the up indicator to display when the drawer indicator is not
     * enabled.
     * <p>
     * If you pass <code>null</code> to this method, the default drawable from
     * the theme will be used.
     *
     * @param indicator
     * 		A drawable to use for the up indicator, or null to use
     * 		the theme's default
     * @see #setDrawerIndicatorEnabled(boolean)
     */
    public void setHomeAsUpIndicator(android.graphics.drawable.Drawable indicator) {
        if (indicator == null) {
            mHomeAsUpIndicator = getThemeUpIndicator();
            mHasCustomUpIndicator = false;
        } else {
            mHomeAsUpIndicator = indicator;
            mHasCustomUpIndicator = true;
        }
        if (!mDrawerIndicatorEnabled) {
            setActionBarUpIndicator(mHomeAsUpIndicator, 0);
        }
    }

    /**
     * Set the up indicator to display when the drawer indicator is not
     * enabled.
     * <p>
     * If you pass 0 to this method, the default drawable from the theme will
     * be used.
     *
     * @param resId
     * 		Resource ID of a drawable to use for the up indicator, or 0
     * 		to use the theme's default
     * @see #setDrawerIndicatorEnabled(boolean)
     */
    public void setHomeAsUpIndicator(int resId) {
        android.graphics.drawable.Drawable indicator = null;
        if (resId != 0) {
            indicator = android.support.v4.content.ContextCompat.getDrawable(mActivity, resId);
        }
        setHomeAsUpIndicator(indicator);
    }

    /**
     * Enable or disable the drawer indicator. The indicator defaults to enabled.
     *
     * <p>When the indicator is disabled, the <code>ActionBar</code> will revert to displaying
     * the home-as-up indicator provided by the <code>Activity</code>'s theme in the
     * <code>android.R.attr.homeAsUpIndicator</code> attribute instead of the animated
     * drawer glyph.</p>
     *
     * @param enable
     * 		true to enable, false to disable
     */
    public void setDrawerIndicatorEnabled(boolean enable) {
        if (enable != mDrawerIndicatorEnabled) {
            if (enable) {
                setActionBarUpIndicator(mSlider, mDrawerLayout.isDrawerOpen(android.support.v4.view.GravityCompat.START) ? mCloseDrawerContentDescRes : mOpenDrawerContentDescRes);
            } else {
                setActionBarUpIndicator(mHomeAsUpIndicator, 0);
            }
            mDrawerIndicatorEnabled = enable;
        }
    }

    /**
     *
     *
     * @return true if the enhanced drawer indicator is enabled, false otherwise
     * @see #setDrawerIndicatorEnabled(boolean)
     */
    public boolean isDrawerIndicatorEnabled() {
        return mDrawerIndicatorEnabled;
    }

    /**
     * This method should always be called by your <code>Activity</code>'s
     * {@link Activity#onConfigurationChanged(android.content.res.Configuration) onConfigurationChanged}
     * method.
     *
     * @param newConfig
     * 		The new configuration
     */
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        // Reload drawables that can change with configuration
        if (!mHasCustomUpIndicator) {
            mHomeAsUpIndicator = getThemeUpIndicator();
        }
        mDrawerImage = android.support.v4.content.ContextCompat.getDrawable(mActivity, mDrawerImageResource);
        syncState();
    }

    /**
     * This method should be called by your <code>Activity</code>'s
     * {@link Activity#onOptionsItemSelected(android.view.MenuItem) onOptionsItemSelected} method.
     * If it returns true, your <code>onOptionsItemSelected</code> method should return true and
     * skip further processing.
     *
     * @param item
     * 		the MenuItem instance representing the selected menu item
     * @return true if the event was handled and further processing should not occur
     */
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (((item != null) && (item.getItemId() == android.support.v4.app.ActionBarDrawerToggle.ID_HOME)) && mDrawerIndicatorEnabled) {
            if (mDrawerLayout.isDrawerVisible(android.support.v4.view.GravityCompat.START)) {
                mDrawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(android.support.v4.view.GravityCompat.START);
            }
            return true;
        }
        return false;
    }

    /**
     * {@link DrawerLayout.DrawerListener} callback method. If you do not use your
     * ActionBarDrawerToggle instance directly as your DrawerLayout's listener, you should call
     * through to this method from your own listener object.
     *
     * @param drawerView
     * 		The child view that was moved
     * @param slideOffset
     * 		The new offset of this drawer within its range, from 0-1
     */
    @java.lang.Override
    public void onDrawerSlide(android.view.View drawerView, float slideOffset) {
        float glyphOffset = mSlider.getPosition();
        if (slideOffset > 0.5F) {
            glyphOffset = java.lang.Math.max(glyphOffset, java.lang.Math.max(0.0F, slideOffset - 0.5F) * 2);
        } else {
            glyphOffset = java.lang.Math.min(glyphOffset, slideOffset * 2);
        }
        mSlider.setPosition(glyphOffset);
    }

    /**
     * {@link DrawerLayout.DrawerListener} callback method. If you do not use your
     * ActionBarDrawerToggle instance directly as your DrawerLayout's listener, you should call
     * through to this method from your own listener object.
     *
     * @param drawerView
     * 		Drawer view that is now open
     */
    @java.lang.Override
    public void onDrawerOpened(android.view.View drawerView) {
        mSlider.setPosition(1);
        if (mDrawerIndicatorEnabled) {
            setActionBarDescription(mCloseDrawerContentDescRes);
        }
    }

    /**
     * {@link DrawerLayout.DrawerListener} callback method. If you do not use your
     * ActionBarDrawerToggle instance directly as your DrawerLayout's listener, you should call
     * through to this method from your own listener object.
     *
     * @param drawerView
     * 		Drawer view that is now closed
     */
    @java.lang.Override
    public void onDrawerClosed(android.view.View drawerView) {
        mSlider.setPosition(0);
        if (mDrawerIndicatorEnabled) {
            setActionBarDescription(mOpenDrawerContentDescRes);
        }
    }

    /**
     * {@link DrawerLayout.DrawerListener} callback method. If you do not use your
     * ActionBarDrawerToggle instance directly as your DrawerLayout's listener, you should call
     * through to this method from your own listener object.
     *
     * @param newState
     * 		The new drawer motion state
     */
    @java.lang.Override
    public void onDrawerStateChanged(int newState) {
    }

    android.graphics.drawable.Drawable getThemeUpIndicator() {
        if (mActivityImpl != null) {
            return mActivityImpl.getThemeUpIndicator();
        }
        return android.support.v4.app.ActionBarDrawerToggle.IMPL.getThemeUpIndicator(mActivity);
    }

    void setActionBarUpIndicator(android.graphics.drawable.Drawable upDrawable, int contentDescRes) {
        if (mActivityImpl != null) {
            mActivityImpl.setActionBarUpIndicator(upDrawable, contentDescRes);
            return;
        }
        mSetIndicatorInfo = android.support.v4.app.ActionBarDrawerToggle.IMPL.setActionBarUpIndicator(mSetIndicatorInfo, mActivity, upDrawable, contentDescRes);
    }

    void setActionBarDescription(int contentDescRes) {
        if (mActivityImpl != null) {
            mActivityImpl.setActionBarDescription(contentDescRes);
            return;
        }
        mSetIndicatorInfo = android.support.v4.app.ActionBarDrawerToggle.IMPL.setActionBarDescription(mSetIndicatorInfo, mActivity, contentDescRes);
    }

    private class SlideDrawable extends android.graphics.drawable.InsetDrawable implements android.graphics.drawable.Drawable.Callback {
        private final boolean mHasMirroring = android.os.Build.VERSION.SDK_INT > 18;

        private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

        private float mPosition;

        private float mOffset;

        SlideDrawable(android.graphics.drawable.Drawable wrapped) {
            super(wrapped, 0);
        }

        /**
         * Sets the current position along the offset.
         *
         * @param position
         * 		a value between 0 and 1
         */
        public void setPosition(float position) {
            mPosition = position;
            invalidateSelf();
        }

        public float getPosition() {
            return mPosition;
        }

        /**
         * Specifies the maximum offset when the position is at 1.
         *
         * @param offset
         * 		maximum offset as a fraction of the drawable width,
         * 		positive to shift left or negative to shift right.
         * @see #setPosition(float)
         */
        public void setOffset(float offset) {
            mOffset = offset;
            invalidateSelf();
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas) {
            copyBounds(mTmpRect);
            canvas.save();
            // Layout direction must be obtained from the activity.
            final boolean isLayoutRTL = android.support.v4.view.ViewCompat.getLayoutDirection(mActivity.getWindow().getDecorView()) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
            final int flipRtl = (isLayoutRTL) ? -1 : 1;
            final int width = mTmpRect.width();
            canvas.translate((((-mOffset) * width) * mPosition) * flipRtl, 0);
            // Force auto-mirroring if it's not supported by the platform.
            if (isLayoutRTL && (!mHasMirroring)) {
                canvas.translate(width, 0);
                canvas.scale(-1, 1);
            }
            super.draw(canvas);
            canvas.restore();
        }
    }
}

