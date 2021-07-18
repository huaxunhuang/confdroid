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
 * Base class for AppCompat themed {@link android.app.Dialog}s.
 */
public class AppCompatDialog extends android.app.Dialog implements android.support.v7.app.AppCompatCallback {
    private android.support.v7.app.AppCompatDelegate mDelegate;

    public AppCompatDialog(android.content.Context context) {
        this(context, 0);
    }

    public AppCompatDialog(android.content.Context context, int theme) {
        super(context, android.support.v7.app.AppCompatDialog.getThemeResId(context, theme));
        // This is a bit weird, but Dialog's are typically created and setup before being shown,
        // which means that we can't rely on onCreate() being called before a content view is set.
        // To workaround this, we call onCreate(null) in the ctor, and then again as usual in
        // onCreate().
        getDelegate().onCreate(null);
        // Apply AppCompat's DayNight resources if needed
        getDelegate().applyDayNight();
    }

    protected AppCompatDialog(android.content.Context context, boolean cancelable, android.content.DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        super.onCreate(savedInstanceState);
        getDelegate().onCreate(savedInstanceState);
    }

    /**
     * Support library version of {@link android.app.Dialog#getActionBar}.
     *
     * <p>Retrieve a reference to this dialog's ActionBar.
     *
     * @return The Dialog's ActionBar, or null if it does not have one.
     */
    public android.support.v7.app.ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
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

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.view.View findViewById(@android.support.annotation.IdRes
    int id) {
        return getDelegate().findViewById(id);
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        super.setTitle(title);
        getDelegate().setTitle(title);
    }

    @java.lang.Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        getDelegate().setTitle(getContext().getString(titleId));
    }

    @java.lang.Override
    public void addContentView(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @java.lang.Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    /**
     * Enable extended support library window features.
     * <p>
     * This is a convenience for calling
     * {@link android.view.Window#requestFeature getWindow().requestFeature()}.
     * </p>
     *
     * @param featureId
     * 		The desired feature as defined in {@link android.view.Window} or
     * 		{@link android.support.v4.view.WindowCompat}.
     * @return Returns true if the requested feature is supported and now enabled.
     * @see android.app.Dialog#requestWindowFeature
     * @see android.view.Window#requestFeature
     */
    public boolean supportRequestWindowFeature(int featureId) {
        return getDelegate().requestWindowFeature(featureId);
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
     *
     *
     * @return The {@link AppCompatDelegate} being used by this Dialog.
     */
    public android.support.v7.app.AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = android.support.v7.app.AppCompatDelegate.create(this, this);
        }
        return mDelegate;
    }

    private static int getThemeResId(android.content.Context context, int themeId) {
        if (themeId == 0) {
            // If the provided theme is 0, then retrieve the dialogTheme from our theme
            android.util.TypedValue outValue = new android.util.TypedValue();
            context.getTheme().resolveAttribute(R.attr.dialogTheme, outValue, true);
            themeId = outValue.resourceId;
        }
        return themeId;
    }

    @java.lang.Override
    public void onSupportActionModeStarted(android.support.v7.view.ActionMode mode) {
    }

    @java.lang.Override
    public void onSupportActionModeFinished(android.support.v7.view.ActionMode mode) {
    }

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.support.v7.view.ActionMode onWindowStartingSupportActionMode(android.support.v7.view.ActionMode.Callback callback) {
        return null;
    }
}

