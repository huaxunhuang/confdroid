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
package android.support.v7.app;


/**
 * Base class for activities that use the
 * <a href="{@docRoot }tools/extras/support-library.html">support library</a> action bar features.
 *
 * <p>You can add an {@link android.support.v7.app.ActionBar} to your activity when running on API level 7 or higher
 * by extending this class for your activity and setting the activity theme to
 * {@link android.support.v7.appcompat.R.style#Theme_AppCompat Theme.AppCompat} or a similar theme.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 *
 * <p>For information about how to use the action bar, including how to add action items, navigation
 * modes and more, read the <a href="{@docRoot }guide/topics/ui/actionbar.html">Action
 * Bar</a> API guide.</p>
 * </div>
 */
public class AppCompatActivity extends android.support.v4.app.FragmentActivity implements android.support.v4.app.TaskStackBuilder.SupportParentable , android.support.v7.app.ActionBarDrawerToggle.DelegateProvider , android.support.v7.app.AppCompatCallback {
    private android.support.v7.app.AppCompatDelegate mDelegate;

    private int mThemeId = 0;

    private boolean mEatKeyUpEvent;

    private android.content.res.Resources mResources;

    @java.lang.Override
    protected void onCreate(@android.support.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        final android.support.v7.app.AppCompatDelegate delegate = getDelegate();
        delegate.installViewFactory();
        delegate.onCreate(savedInstanceState);
        if (delegate.applyDayNight() && (mThemeId != 0)) {
            // If DayNight has been applied, we need to re-apply the theme for
            // the changes to take effect. On API 23+, we should bypass
            // setTheme(), which will no-op if the theme ID is identical to the
            // current theme ID.
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                onApplyThemeResource(getTheme(), mThemeId, false);
            } else {
                setTheme(mThemeId);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @java.lang.Override
    public void setTheme(@android.support.annotation.StyleRes
    final int resid) {
        super.setTheme(resid);
        // Keep hold of the theme id so that we can re-set it later if needed
        mThemeId = resid;
    }

    @java.lang.Override
    protected void onPostCreate(@android.support.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    /**
     * Support library version of {@link android.app.Activity#getActionBar}.
     *
     * <p>Retrieve a reference to this activity's ActionBar.
     *
     * @return The Activity's ActionBar, or null if it does not have one.
     */
    @android.support.annotation.Nullable
    public android.support.v7.app.ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    /**
     * Set a {@link android.widget.Toolbar Toolbar} to act as the
     * {@link android.support.v7.app.ActionBar} for this Activity window.
     *
     * <p>When set to a non-null value the {@link #getActionBar()} method will return
     * an {@link android.support.v7.app.ActionBar} object that can be used to control the given
     * toolbar as if it were a traditional window decor action bar. The toolbar's menu will be
     * populated with the Activity's options menu and the navigation button will be wired through
     * the standard {@link android.R.id#home home} menu select action.</p>
     *
     * <p>In order to use a Toolbar within the Activity's window content the application
     * must not request the window feature
     * {@link android.view.Window#FEATURE_ACTION_BAR FEATURE_SUPPORT_ACTION_BAR}.</p>
     *
     * @param toolbar
     * 		Toolbar to set as the Activity's action bar, or {@code null} to clear it
     */
    public void setSupportActionBar(@android.support.annotation.Nullable
    android.support.v7.widget.Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @java.lang.Override
    public android.view.MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @java.lang.Override
    public void setContentView(@android.support.annotation.LayoutRes
    int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @java.lang.Override
    public void setContentView(android.view.View view) {
        getDelegate().setContentView(view);
    }

    @java.lang.Override
    public void setContentView(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @java.lang.Override
    public void addContentView(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
        if (mResources != null) {
            // The real (and thus managed) resources object was already updated
            // by ResourcesManager, so pull the current metrics from there.
            final android.util.DisplayMetrics newMetrics = super.getResources().getDisplayMetrics();
            mResources.updateConfiguration(newConfig, newMetrics);
        }
    }

    @java.lang.Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @java.lang.Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();
    }

    @java.lang.Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @java.lang.Override
    public android.view.View findViewById(@android.support.annotation.IdRes
    int id) {
        return getDelegate().findViewById(id);
    }

    @java.lang.Override
    public final boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        if (super.onMenuItemSelected(featureId, item)) {
            return true;
        }
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (((item.getItemId() == android.R.id.home) && (ab != null)) && ((ab.getDisplayOptions() & android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP) != 0)) {
            return onSupportNavigateUp();
        }
        return false;
    }

    @java.lang.Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    @java.lang.Override
    protected void onTitleChanged(java.lang.CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    /**
     * Enable extended support library window features.
     * <p>
     * This is a convenience for calling
     * {@link android.view.Window#requestFeature getWindow().requestFeature()}.
     * </p>
     *
     * @param featureId
     * 		The desired feature as defined in
     * 		{@link android.view.Window} or {@link android.support.v4.view.WindowCompat}.
     * @return Returns true if the requested feature is supported and now enabled.
     * @see android.app.Activity#requestWindowFeature
     * @see android.view.Window#requestFeature
     */
    public boolean supportRequestWindowFeature(int featureId) {
        return getDelegate().requestWindowFeature(featureId);
    }

    @java.lang.Override
    public void supportInvalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    /**
     * Notifies the Activity that a support action mode has been started.
     * Activity subclasses overriding this method should call the superclass implementation.
     *
     * @param mode
     * 		The new action mode.
     */
    @java.lang.Override
    @android.support.annotation.CallSuper
    public void onSupportActionModeStarted(@android.support.annotation.NonNull
    android.support.v7.view.ActionMode mode) {
    }

    /**
     * Notifies the activity that a support action mode has finished.
     * Activity subclasses overriding this method should call the superclass implementation.
     *
     * @param mode
     * 		The action mode that just finished.
     */
    @java.lang.Override
    @android.support.annotation.CallSuper
    public void onSupportActionModeFinished(@android.support.annotation.NonNull
    android.support.v7.view.ActionMode mode) {
    }

    /**
     * Called when a support action mode is being started for this window. Gives the
     * callback an opportunity to handle the action mode in its own unique and
     * beautiful way. If this method returns null the system can choose a way
     * to present the mode or choose not to start the mode at all.
     *
     * @param callback
     * 		Callback to control the lifecycle of this action mode
     * @return The ActionMode that was started, or null if the system should present it
     */
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.support.v7.view.ActionMode onWindowStartingSupportActionMode(@android.support.annotation.NonNull
    android.support.v7.view.ActionMode.Callback callback) {
        return null;
    }

    /**
     * Start an action mode.
     *
     * @param callback
     * 		Callback that will manage lifecycle events for this context mode
     * @return The ContextMode that was started, or null if it was canceled
     */
    @android.support.annotation.Nullable
    public android.support.v7.view.ActionMode startSupportActionMode(@android.support.annotation.NonNull
    android.support.v7.view.ActionMode.Callback callback) {
        return getDelegate().startSupportActionMode(callback);
    }

    /**
     *
     *
     * @deprecated Progress bars are no longer provided in AppCompat.
     */
    @java.lang.Deprecated
    public void setSupportProgressBarVisibility(boolean visible) {
    }

    /**
     *
     *
     * @deprecated Progress bars are no longer provided in AppCompat.
     */
    @java.lang.Deprecated
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
    }

    /**
     *
     *
     * @deprecated Progress bars are no longer provided in AppCompat.
     */
    @java.lang.Deprecated
    public void setSupportProgressBarIndeterminate(boolean indeterminate) {
    }

    /**
     *
     *
     * @deprecated Progress bars are no longer provided in AppCompat.
     */
    @java.lang.Deprecated
    public void setSupportProgress(int progress) {
    }

    /**
     * Support version of {@link #onCreateNavigateUpTaskStack(android.app.TaskStackBuilder)}.
     * This method will be called on all platform versions.
     *
     * Define the synthetic task stack that will be generated during Up navigation from
     * a different task.
     *
     * <p>The default implementation of this method adds the parent chain of this activity
     * as specified in the manifest to the supplied {@link android.support.v4.app.TaskStackBuilder}. Applications
     * may choose to override this method to construct the desired task stack in a different
     * way.</p>
     *
     * <p>This method will be invoked by the default implementation of {@link #onNavigateUp()}
     * if {@link #shouldUpRecreateTask(android.content.Intent)} returns true when supplied with the intent
     * returned by {@link #getParentActivityIntent()}.</p>
     *
     * <p>Applications that wish to supply extra Intent parameters to the parent stack defined
     * by the manifest should override
     * {@link #onPrepareSupportNavigateUpTaskStack(android.support.v4.app.TaskStackBuilder)}.</p>
     *
     * @param builder
     * 		An empty TaskStackBuilder - the application should add intents representing
     * 		the desired task stack
     */
    public void onCreateSupportNavigateUpTaskStack(@android.support.annotation.NonNull
    android.support.v4.app.TaskStackBuilder builder) {
        builder.addParentStack(this);
    }

    /**
     * Support version of {@link #onPrepareNavigateUpTaskStack(android.app.TaskStackBuilder)}.
     * This method will be called on all platform versions.
     *
     * Prepare the synthetic task stack that will be generated during Up navigation
     * from a different task.
     *
     * <p>This method receives the {@link android.support.v4.app.TaskStackBuilder} with the constructed series of
     * Intents as generated by {@link #onCreateSupportNavigateUpTaskStack(android.support.v4.app.TaskStackBuilder)}.
     * If any extra data should be added to these intents before launching the new task,
     * the application should override this method and add that data here.</p>
     *
     * @param builder
     * 		A TaskStackBuilder that has been populated with Intents by
     * 		onCreateNavigateUpTaskStack.
     */
    public void onPrepareSupportNavigateUpTaskStack(@android.support.annotation.NonNull
    android.support.v4.app.TaskStackBuilder builder) {
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar.
     *
     * <p>If a parent was specified in the manifest for this activity or an activity-alias to it,
     * default Up navigation will be handled automatically. See
     * {@link #getSupportParentActivityIntent()} for how to specify the parent. If any activity
     * along the parent chain requires extra Intent arguments, the Activity subclass
     * should override the method {@link #onPrepareSupportNavigateUpTaskStack(android.support.v4.app.TaskStackBuilder)}
     * to supply those arguments.</p>
     *
     * <p>See <a href="{@docRoot }guide/topics/fundamentals/tasks-and-back-stack.html">Tasks and
     * Back Stack</a> from the developer guide and
     * <a href="{@docRoot }design/patterns/navigation.html">Navigation</a> from the design guide
     * for more information about navigating within your app.</p>
     *
     * <p>See the {@link android.support.v4.app.TaskStackBuilder} class and the Activity methods
     * {@link #getSupportParentActivityIntent()}, {@link #supportShouldUpRecreateTask(android.content.Intent)}, and
     * {@link #supportNavigateUpTo(android.content.Intent)} for help implementing custom Up navigation.</p>
     *
     * @return true if Up navigation completed successfully and this Activity was finished,
    false otherwise.
     */
    public boolean onSupportNavigateUp() {
        android.content.Intent upIntent = getSupportParentActivityIntent();
        if (upIntent != null) {
            if (supportShouldUpRecreateTask(upIntent)) {
                android.support.v4.app.TaskStackBuilder b = android.support.v4.app.TaskStackBuilder.create(this);
                onCreateSupportNavigateUpTaskStack(b);
                onPrepareSupportNavigateUpTaskStack(b);
                b.startActivities();
                try {
                    android.support.v4.app.ActivityCompat.finishAffinity(this);
                } catch (java.lang.IllegalStateException e) {
                    // This can only happen on 4.1+, when we don't have a parent or a result set.
                    // In that case we should just finish().
                    finish();
                }
            } else {
                // This activity is part of the application's task, so simply
                // navigate up to the hierarchical parent activity.
                supportNavigateUpTo(upIntent);
            }
            return true;
        }
        return false;
    }

    /**
     * Obtain an {@link android.content.Intent} that will launch an explicit target activity
     * specified by sourceActivity's {@link android.support.v4.app.NavUtils#PARENT_ACTIVITY} &lt;meta-data&gt;
     * element in the application's manifest. If the device is running
     * Jellybean or newer, the android:parentActivityName attribute will be preferred
     * if it is present.
     *
     * @return a new Intent targeting the defined parent activity of sourceActivity
     */
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.content.Intent getSupportParentActivityIntent() {
        return android.support.v4.app.NavUtils.getParentActivityIntent(this);
    }

    /**
     * Returns true if sourceActivity should recreate the task when navigating 'up'
     * by using targetIntent.
     *
     * <p>If this method returns false the app can trivially call
     * {@link #supportNavigateUpTo(android.content.Intent)} using the same parameters to correctly perform
     * up navigation. If this method returns false, the app should synthesize a new task stack
     * by using {@link android.support.v4.app.TaskStackBuilder} or another similar mechanism to perform up navigation.</p>
     *
     * @param targetIntent
     * 		An intent representing the target destination for up navigation
     * @return true if navigating up should recreate a new task stack, false if the same task
    should be used for the destination
     */
    public boolean supportShouldUpRecreateTask(@android.support.annotation.NonNull
    android.content.Intent targetIntent) {
        return android.support.v4.app.NavUtils.shouldUpRecreateTask(this, targetIntent);
    }

    /**
     * Navigate from sourceActivity to the activity specified by upIntent, finishing sourceActivity
     * in the process. upIntent will have the flag {@link android.content.Intent#FLAG_ACTIVITY_CLEAR_TOP} set
     * by this method, along with any others required for proper up navigation as outlined
     * in the Android Design Guide.
     *
     * <p>This method should be used when performing up navigation from within the same task
     * as the destination. If up navigation should cross tasks in some cases, see
     * {@link #supportShouldUpRecreateTask(android.content.Intent)}.</p>
     *
     * @param upIntent
     * 		An intent representing the target destination for up navigation
     */
    public void supportNavigateUpTo(@android.support.annotation.NonNull
    android.content.Intent upIntent) {
        android.support.v4.app.NavUtils.navigateUpTo(this, upIntent);
    }

    @java.lang.Override
    public void onContentChanged() {
        // Call onSupportContentChanged() for legacy reasons
        onSupportContentChanged();
    }

    /**
     *
     *
     * @deprecated Use {@link #onContentChanged()} instead.
     */
    @java.lang.Deprecated
    public void onSupportContentChanged() {
    }

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.support.v7.app.ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return getDelegate().getDrawerToggleDelegate();
    }

    /**
     * {@inheritDoc }
     *
     * <p>Please note: AppCompat uses it's own feature id for the action bar:
     * {@link AppCompatDelegate#FEATURE_SUPPORT_ACTION_BAR FEATURE_SUPPORT_ACTION_BAR}.</p>
     */
    @java.lang.Override
    public boolean onMenuOpened(int featureId, android.view.Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * {@inheritDoc }
     *
     * <p>Please note: AppCompat uses it's own feature id for the action bar:
     * {@link AppCompatDelegate#FEATURE_SUPPORT_ACTION_BAR FEATURE_SUPPORT_ACTION_BAR}.</p>
     */
    @java.lang.Override
    public void onPanelClosed(int featureId, android.view.Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @java.lang.Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        getDelegate().onSaveInstanceState(outState);
    }

    /**
     *
     *
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @android.support.annotation.NonNull
    public android.support.v7.app.AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = android.support.v7.app.AppCompatDelegate.create(this, this);
        }
        return mDelegate;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (android.support.v4.view.KeyEventCompat.isCtrlPressed(event) && (event.getUnicodeChar(event.getMetaState() & (~android.view.KeyEvent.META_CTRL_MASK)) == '<')) {
            // Capture the Control-< and send focus to the ActionBar
            final int action = event.getAction();
            if (action == android.view.KeyEvent.ACTION_DOWN) {
                final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                if (((actionBar != null) && actionBar.isShowing()) && actionBar.requestFocus()) {
                    mEatKeyUpEvent = true;
                    return true;
                }
            } else
                if ((action == android.view.KeyEvent.ACTION_UP) && mEatKeyUpEvent) {
                    mEatKeyUpEvent = false;
                    return true;
                }

        }
        return super.dispatchKeyEvent(event);
    }

    @java.lang.Override
    public android.content.res.Resources getResources() {
        if ((mResources == null) && android.support.v7.widget.VectorEnabledTintResources.shouldBeUsed()) {
            mResources = new android.support.v7.widget.VectorEnabledTintResources(this, super.getResources());
        }
        return mResources == null ? super.getResources() : mResources;
    }
}

