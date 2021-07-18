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
package android.app;


/**
 * Base class for Dialogs.
 *
 * <p>Note: Activities provide a facility to manage the creation, saving and
 * restoring of dialogs. See {@link Activity#onCreateDialog(int)},
 * {@link Activity#onPrepareDialog(int, Dialog)},
 * {@link Activity#showDialog(int)}, and {@link Activity#dismissDialog(int)}. If
 * these methods are used, {@link #getOwnerActivity()} will return the Activity
 * that managed this dialog.
 *
 * <p>Often you will want to have a Dialog display on top of the current
 * input method, because there is no reason for it to accept text.  You can
 * do this by setting the {@link WindowManager.LayoutParams#FLAG_ALT_FOCUSABLE_IM
 * WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM} window flag (assuming
 * your Dialog takes input focus, as it the default) with the following code:
 *
 * <pre>
 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
 *         WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);</pre>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating dialogs, read the
 * <a href="{@docRoot }guide/topics/ui/dialogs.html">Dialogs</a> developer guide.</p>
 * </div>
 */
public class Dialog implements android.content.DialogInterface , android.view.KeyEvent.Callback , android.view.View.OnCreateContextMenuListener , android.view.Window.Callback , android.view.Window.OnWindowDismissedCallback {
    private static final java.lang.String TAG = "Dialog";

    private android.app.Activity mOwnerActivity;

    private final android.view.WindowManager mWindowManager;

    final android.content.Context mContext;

    final android.view.Window mWindow;

    android.view.View mDecor;

    private android.app.ActionBar mActionBar;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    protected boolean mCancelable = true;

    private java.lang.String mCancelAndDismissTaken;

    private android.os.Message mCancelMessage;

    private android.os.Message mDismissMessage;

    private android.os.Message mShowMessage;

    private android.content.DialogInterface.OnKeyListener mOnKeyListener;

    private boolean mCreated = false;

    private boolean mShowing = false;

    private boolean mCanceled = false;

    private final android.os.Handler mHandler = new android.os.Handler();

    private static final int DISMISS = 0x43;

    private static final int CANCEL = 0x44;

    private static final int SHOW = 0x45;

    private final android.os.Handler mListenersHandler;

    private android.view.SearchEvent mSearchEvent;

    private android.view.ActionMode mActionMode;

    private int mActionModeTypeStarting = android.view.ActionMode.TYPE_PRIMARY;

    private final java.lang.Runnable mDismissAction = this::dismissDialog;

    /**
     * Creates a dialog window that uses the default dialog theme.
     * <p>
     * The supplied {@code context} is used to obtain the window manager and
     * base theme used to present the dialog.
     *
     * @param context
     * 		the context in which the dialog should run
     * @see android.R.styleable#Theme_dialogTheme
     */
    public Dialog(@android.annotation.NonNull
    android.content.Context context) {
        this(context, 0, true);
    }

    /**
     * Creates a dialog window that uses a custom dialog style.
     * <p>
     * The supplied {@code context} is used to obtain the window manager and
     * base theme used to present the dialog.
     * <p>
     * The supplied {@code theme} is applied on top of the context's theme. See
     * <a href="{@docRoot }guide/topics/resources/available-resources.html#stylesandthemes">
     * Style and Theme Resources</a> for more information about defining and
     * using styles.
     *
     * @param context
     * 		the context in which the dialog should run
     * @param themeResId
     * 		a style resource describing the theme to use for the
     * 		window, or {@code 0} to use the default dialog theme
     */
    public Dialog(@android.annotation.NonNull
    android.content.Context context, @android.annotation.StyleRes
    int themeResId) {
        this(context, themeResId, true);
    }

    Dialog(@android.annotation.NonNull
    android.content.Context context, @android.annotation.StyleRes
    int themeResId, boolean createContextThemeWrapper) {
        if (createContextThemeWrapper) {
            if (themeResId == 0) {
                final android.util.TypedValue outValue = new android.util.TypedValue();
                context.getTheme().resolveAttribute(R.attr.dialogTheme, outValue, true);
                themeResId = outValue.resourceId;
            }
            mContext = new android.view.ContextThemeWrapper(context, themeResId);
        } else {
            mContext = context;
        }
        mWindowManager = ((android.view.WindowManager) (context.getSystemService(android.content.Context.WINDOW_SERVICE)));
        final android.view.Window w = new com.android.internal.policy.PhoneWindow(mContext);
        mWindow = w;
        w.setCallback(this);
        w.setOnWindowDismissedCallback(this);
        w.setWindowManager(mWindowManager, null, null);
        w.setGravity(android.view.Gravity.CENTER);
        mListenersHandler = new android.app.Dialog.ListenersHandler(this);
    }

    /**
     *
     *
     * @deprecated 
     * @unknown 
     */
    @java.lang.Deprecated
    protected Dialog(@android.annotation.NonNull
    android.content.Context context, boolean cancelable, @android.annotation.Nullable
    android.os.Message cancelCallback) {
        this(context);
        mCancelable = cancelable;
        mCancelMessage = cancelCallback;
    }

    protected Dialog(@android.annotation.NonNull
    android.content.Context context, boolean cancelable, @android.annotation.Nullable
    android.content.DialogInterface.OnCancelListener cancelListener) {
        this(context);
        mCancelable = cancelable;
        setOnCancelListener(cancelListener);
    }

    /**
     * Retrieve the Context this Dialog is running in.
     *
     * @return Context The Context used by the Dialog.
     */
    @android.annotation.NonNull
    public final android.content.Context getContext() {
        return mContext;
    }

    /**
     * Retrieve the {@link ActionBar} attached to this dialog, if present.
     *
     * @return The ActionBar attached to the dialog or null if no ActionBar is present.
     */
    @android.annotation.Nullable
    public android.app.ActionBar getActionBar() {
        return mActionBar;
    }

    /**
     * Sets the Activity that owns this dialog. An example use: This Dialog will
     * use the suggested volume control stream of the Activity.
     *
     * @param activity
     * 		The Activity that owns this dialog.
     */
    public final void setOwnerActivity(@android.annotation.NonNull
    android.app.Activity activity) {
        mOwnerActivity = activity;
        getWindow().setVolumeControlStream(mOwnerActivity.getVolumeControlStream());
    }

    /**
     * Returns the Activity that owns this Dialog. For example, if
     * {@link Activity#showDialog(int)} is used to show this Dialog, that
     * Activity will be the owner (by default). Depending on how this dialog was
     * created, this may return null.
     *
     * @return The Activity that owns this Dialog.
     */
    @android.annotation.Nullable
    public final android.app.Activity getOwnerActivity() {
        return mOwnerActivity;
    }

    /**
     *
     *
     * @return Whether the dialog is currently showing.
     */
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Forces immediate creation of the dialog.
     * <p>
     * Note that you should not override this method to perform dialog creation.
     * Rather, override {@link #onCreate(Bundle)}.
     */
    public void create() {
        if (!mCreated) {
            dispatchOnCreate(null);
        }
    }

    /**
     * Start the dialog and display it on screen.  The window is placed in the
     * application layer and opaque.  Note that you should not override this
     * method to do initialization when the dialog is shown, instead implement
     * that in {@link #onStart}.
     */
    public void show() {
        if (mShowing) {
            if (mDecor != null) {
                if (mWindow.hasFeature(android.view.Window.FEATURE_ACTION_BAR)) {
                    mWindow.invalidatePanelMenu(android.view.Window.FEATURE_ACTION_BAR);
                }
                mDecor.setVisibility(android.view.View.VISIBLE);
            }
            return;
        }
        mCanceled = false;
        if (!mCreated) {
            dispatchOnCreate(null);
        } else {
            // Fill the DecorView in on any configuration changes that
            // may have occured while it was removed from the WindowManager.
            final android.content.res.Configuration config = mContext.getResources().getConfiguration();
            mWindow.getDecorView().dispatchConfigurationChanged(config);
        }
        onStart();
        mDecor = mWindow.getDecorView();
        if ((mActionBar == null) && mWindow.hasFeature(android.view.Window.FEATURE_ACTION_BAR)) {
            final android.content.pm.ApplicationInfo info = mContext.getApplicationInfo();
            mWindow.setDefaultIcon(info.icon);
            mWindow.setDefaultLogo(info.logo);
            mActionBar = new com.android.internal.app.WindowDecorActionBar(this);
        }
        android.view.WindowManager.LayoutParams l = mWindow.getAttributes();
        if ((l.softInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION) == 0) {
            android.view.WindowManager.LayoutParams nl = new android.view.WindowManager.LayoutParams();
            nl.copyFrom(l);
            nl.softInputMode |= android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;
            l = nl;
        }
        mWindowManager.addView(mDecor, l);
        mShowing = true;
        sendShowMessage();
    }

    /**
     * Hide the dialog, but do not dismiss it.
     */
    public void hide() {
        if (mDecor != null) {
            mDecor.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Dismiss this dialog, removing it from the screen. This method can be
     * invoked safely from any thread.  Note that you should not override this
     * method to do cleanup when the dialog is dismissed, instead implement
     * that in {@link #onStop}.
     */
    @java.lang.Override
    public void dismiss() {
        if (android.os.Looper.myLooper() == mHandler.getLooper()) {
            dismissDialog();
        } else {
            mHandler.post(mDismissAction);
        }
    }

    void dismissDialog() {
        if ((mDecor == null) || (!mShowing)) {
            return;
        }
        if (mWindow.isDestroyed()) {
            android.util.Log.e(android.app.Dialog.TAG, "Tried to dismissDialog() but the Dialog's window was already destroyed!");
            return;
        }
        try {
            mWindowManager.removeViewImmediate(mDecor);
        } finally {
            if (mActionMode != null) {
                mActionMode.finish();
            }
            mDecor = null;
            mWindow.closeAllPanels();
            onStop();
            mShowing = false;
            sendDismissMessage();
        }
    }

    private void sendDismissMessage() {
        if (mDismissMessage != null) {
            // Obtain a new message so this dialog can be re-used
            android.os.Message.obtain(mDismissMessage).sendToTarget();
        }
    }

    private void sendShowMessage() {
        if (mShowMessage != null) {
            // Obtain a new message so this dialog can be re-used
            android.os.Message.obtain(mShowMessage).sendToTarget();
        }
    }

    // internal method to make sure mCreated is set properly without requiring
    // users to call through to super in onCreate
    void dispatchOnCreate(android.os.Bundle savedInstanceState) {
        if (!mCreated) {
            onCreate(savedInstanceState);
            mCreated = true;
        }
    }

    /**
     * Similar to {@link Activity#onCreate}, you should initialize your dialog
     * in this method, including calling {@link #setContentView}.
     *
     * @param savedInstanceState
     * 		If this dialog is being reinitialized after a
     * 		the hosting activity was previously shut down, holds the result from
     * 		the most recent call to {@link #onSaveInstanceState}, or null if this
     * 		is the first time.
     */
    protected void onCreate(android.os.Bundle savedInstanceState) {
    }

    /**
     * Called when the dialog is starting.
     */
    protected void onStart() {
        if (mActionBar != null)
            mActionBar.setShowHideAnimationEnabled(true);

    }

    /**
     * Called to tell you that you're stopping.
     */
    protected void onStop() {
        if (mActionBar != null)
            mActionBar.setShowHideAnimationEnabled(false);

    }

    private static final java.lang.String DIALOG_SHOWING_TAG = "android:dialogShowing";

    private static final java.lang.String DIALOG_HIERARCHY_TAG = "android:dialogHierarchy";

    /**
     * Saves the state of the dialog into a bundle.
     *
     * The default implementation saves the state of its view hierarchy, so you'll
     * likely want to call through to super if you override this to save additional
     * state.
     *
     * @return A bundle with the state of the dialog.
     */
    @android.annotation.NonNull
    public android.os.Bundle onSaveInstanceState() {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putBoolean(android.app.Dialog.DIALOG_SHOWING_TAG, mShowing);
        if (mCreated) {
            bundle.putBundle(android.app.Dialog.DIALOG_HIERARCHY_TAG, mWindow.saveHierarchyState());
        }
        return bundle;
    }

    /**
     * Restore the state of the dialog from a previously saved bundle.
     *
     * The default implementation restores the state of the dialog's view
     * hierarchy that was saved in the default implementation of {@link #onSaveInstanceState()},
     * so be sure to call through to super when overriding unless you want to
     * do all restoring of state yourself.
     *
     * @param savedInstanceState
     * 		The state of the dialog previously saved by
     * 		{@link #onSaveInstanceState()}.
     */
    public void onRestoreInstanceState(@android.annotation.NonNull
    android.os.Bundle savedInstanceState) {
        final android.os.Bundle dialogHierarchyState = savedInstanceState.getBundle(android.app.Dialog.DIALOG_HIERARCHY_TAG);
        if (dialogHierarchyState == null) {
            // dialog has never been shown, or onCreated, nothing to restore.
            return;
        }
        dispatchOnCreate(savedInstanceState);
        mWindow.restoreHierarchyState(dialogHierarchyState);
        if (savedInstanceState.getBoolean(android.app.Dialog.DIALOG_SHOWING_TAG)) {
            show();
        }
    }

    /**
     * Retrieve the current Window for the activity.  This can be used to
     * directly access parts of the Window API that are not available
     * through Activity/Screen.
     *
     * @return Window The current window, or null if the activity is not
    visual.
     */
    @android.annotation.Nullable
    public android.view.Window getWindow() {
        return mWindow;
    }

    /**
     * Call {@link android.view.Window#getCurrentFocus} on the
     * Window if this Activity to return the currently focused view.
     *
     * @return View The current View with focus or null.
     * @see #getWindow
     * @see android.view.Window#getCurrentFocus
     */
    @android.annotation.Nullable
    public android.view.View getCurrentFocus() {
        return mWindow != null ? mWindow.getCurrentFocus() : null;
    }

    /**
     * Finds a child view with the given identifier. Returns null if the
     * specified child view does not exist or the dialog has not yet been fully
     * created (for example, via {@link #show()} or {@link #create()}).
     *
     * @param id
     * 		the identifier of the view to find
     * @return The view with the given id or null.
     */
    @android.annotation.Nullable
    public android.view.View findViewById(@android.annotation.IdRes
    int id) {
        return mWindow.findViewById(id);
    }

    /**
     * Set the screen content from a layout resource.  The resource will be
     * inflated, adding all top-level views to the screen.
     *
     * @param layoutResID
     * 		Resource ID to be inflated.
     */
    public void setContentView(@android.annotation.LayoutRes
    int layoutResID) {
        mWindow.setContentView(layoutResID);
    }

    /**
     * Set the screen content to an explicit view.  This view is placed
     * directly into the screen's view hierarchy.  It can itself be a complex
     * view hierarchy.
     *
     * @param view
     * 		The desired content to display.
     */
    public void setContentView(@android.annotation.NonNull
    android.view.View view) {
        mWindow.setContentView(view);
    }

    /**
     * Set the screen content to an explicit view.  This view is placed
     * directly into the screen's view hierarchy.  It can itself be a complex
     * view hierarchy.
     *
     * @param view
     * 		The desired content to display.
     * @param params
     * 		Layout parameters for the view.
     */
    public void setContentView(@android.annotation.NonNull
    android.view.View view, @android.annotation.Nullable
    android.view.ViewGroup.LayoutParams params) {
        mWindow.setContentView(view, params);
    }

    /**
     * Add an additional content view to the screen.  Added after any existing
     * ones in the screen -- existing views are NOT removed.
     *
     * @param view
     * 		The desired content to display.
     * @param params
     * 		Layout parameters for the view.
     */
    public void addContentView(@android.annotation.NonNull
    android.view.View view, @android.annotation.Nullable
    android.view.ViewGroup.LayoutParams params) {
        mWindow.addContentView(view, params);
    }

    /**
     * Set the title text for this dialog's window.
     *
     * @param title
     * 		The new text to display in the title.
     */
    public void setTitle(@android.annotation.Nullable
    java.lang.CharSequence title) {
        mWindow.setTitle(title);
        mWindow.getAttributes().setTitle(title);
    }

    /**
     * Set the title text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param titleId
     * 		the title's text resource identifier
     */
    public void setTitle(@android.annotation.StringRes
    int titleId) {
        setTitle(mContext.getText(titleId));
    }

    /**
     * A key was pressed down.
     *
     * <p>If the focused view didn't want this event, this method is called.
     *
     * <p>The default implementation consumed the KEYCODE_BACK to later
     * handle it in {@link #onKeyUp}.
     *
     * @see #onKeyUp
     * @see android.view.KeyEvent
     */
    @java.lang.Override
    public boolean onKeyDown(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            event.startTracking();
            return true;
        }
        return false;
    }

    /**
     * Default implementation of {@link KeyEvent.Callback#onKeyLongPress(int, KeyEvent)
     * KeyEvent.Callback.onKeyLongPress()}: always returns false (doesn't handle
     * the event).
     */
    @java.lang.Override
    public boolean onKeyLongPress(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        return false;
    }

    /**
     * A key was released.
     *
     * <p>The default implementation handles KEYCODE_BACK to close the
     * dialog.
     *
     * @see #onKeyDown
     * @see KeyEvent
     */
    @java.lang.Override
    public boolean onKeyUp(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        if (((keyCode == android.view.KeyEvent.KEYCODE_BACK) && event.isTracking()) && (!event.isCanceled())) {
            onBackPressed();
            return true;
        }
        return false;
    }

    /**
     * Default implementation of {@link KeyEvent.Callback#onKeyMultiple(int, int, KeyEvent)
     * KeyEvent.Callback.onKeyMultiple()}: always returns false (doesn't handle
     * the event).
     */
    @java.lang.Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, @android.annotation.NonNull
    android.view.KeyEvent event) {
        return false;
    }

    /**
     * Called when the dialog has detected the user's press of the back
     * key.  The default implementation simply cancels the dialog (only if
     * it is cancelable), but you can override this to do whatever you want.
     */
    public void onBackPressed() {
        if (mCancelable) {
            cancel();
        }
    }

    /**
     * Called when a key shortcut event is not handled by any of the views in the Dialog.
     * Override this method to implement global key shortcuts for the Dialog.
     * Key shortcuts can also be implemented by setting the
     * {@link MenuItem#setShortcut(char, char) shortcut} property of menu items.
     *
     * @param keyCode
     * 		The value in event.getKeyCode().
     * @param event
     * 		Description of the key event.
     * @return True if the key shortcut was handled.
     */
    public boolean onKeyShortcut(int keyCode, @android.annotation.NonNull
    android.view.KeyEvent event) {
        return false;
    }

    /**
     * Called when a touch screen event was not handled by any of the views
     * under it. This is most useful to process touch events that happen outside
     * of your window bounds, where there is no view to receive it.
     *
     * @param event
     * 		The touch screen event being processed.
     * @return Return true if you have consumed the event, false if you haven't.
    The default implementation will cancel the dialog when a touch
    happens outside of the window bounds.
     */
    public boolean onTouchEvent(@android.annotation.NonNull
    android.view.MotionEvent event) {
        if ((mCancelable && mShowing) && mWindow.shouldCloseOnTouch(mContext, event)) {
            cancel();
            return true;
        }
        return false;
    }

    /**
     * Called when the trackball was moved and not handled by any of the
     * views inside of the activity.  So, for example, if the trackball moves
     * while focus is on a button, you will receive a call here because
     * buttons do not normally do anything with trackball events.  The call
     * here happens <em>before</em> trackball movements are converted to
     * DPAD key events, which then get sent back to the view hierarchy, and
     * will be processed at the point for things like focus navigation.
     *
     * @param event
     * 		The trackball event being processed.
     * @return Return true if you have consumed the event, false if you haven't.
    The default implementation always returns false.
     */
    public boolean onTrackballEvent(@android.annotation.NonNull
    android.view.MotionEvent event) {
        return false;
    }

    /**
     * Called when a generic motion event was not handled by any of the
     * views inside of the dialog.
     * <p>
     * Generic motion events describe joystick movements, mouse hovers, track pad
     * touches, scroll wheel movements and other input events.  The
     * {@link MotionEvent#getSource() source} of the motion event specifies
     * the class of input that was received.  Implementations of this method
     * must examine the bits in the source before processing the event.
     * The following code example shows how this is done.
     * </p><p>
     * Generic motion events with source class
     * {@link android.view.InputDevice#SOURCE_CLASS_POINTER}
     * are delivered to the view under the pointer.  All other generic motion events are
     * delivered to the focused view.
     * </p><p>
     * See {@link View#onGenericMotionEvent(MotionEvent)} for an example of how to
     * handle this event.
     * </p>
     *
     * @param event
     * 		The generic motion event being processed.
     * @return Return true if you have consumed the event, false if you haven't.
    The default implementation always returns false.
     */
    public boolean onGenericMotionEvent(@android.annotation.NonNull
    android.view.MotionEvent event) {
        return false;
    }

    @java.lang.Override
    public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
        if (mDecor != null) {
            mWindowManager.updateViewLayout(mDecor, params);
        }
    }

    @java.lang.Override
    public void onContentChanged() {
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }

    @java.lang.Override
    public void onAttachedToWindow() {
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onWindowDismissed(boolean finishTask) {
        dismiss();
    }

    /**
     * Called to process key events.  You can override this to intercept all
     * key events before they are dispatched to the window.  Be sure to call
     * this implementation for key events that should be handled normally.
     *
     * @param event
     * 		The key event.
     * @return boolean Return true if this event was consumed.
     */
    @java.lang.Override
    public boolean dispatchKeyEvent(@android.annotation.NonNull
    android.view.KeyEvent event) {
        if ((mOnKeyListener != null) && mOnKeyListener.onKey(this, event.getKeyCode(), event)) {
            return true;
        }
        if (mWindow.superDispatchKeyEvent(event)) {
            return true;
        }
        return event.dispatch(this, mDecor != null ? mDecor.getKeyDispatcherState() : null, this);
    }

    /**
     * Called to process a key shortcut event.
     * You can override this to intercept all key shortcut events before they are
     * dispatched to the window.  Be sure to call this implementation for key shortcut
     * events that should be handled normally.
     *
     * @param event
     * 		The key shortcut event.
     * @return True if this event was consumed.
     */
    @java.lang.Override
    public boolean dispatchKeyShortcutEvent(@android.annotation.NonNull
    android.view.KeyEvent event) {
        if (mWindow.superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    /**
     * Called to process touch screen events.  You can override this to
     * intercept all touch screen events before they are dispatched to the
     * window.  Be sure to call this implementation for touch screen events
     * that should be handled normally.
     *
     * @param ev
     * 		The touch screen event.
     * @return boolean Return true if this event was consumed.
     */
    @java.lang.Override
    public boolean dispatchTouchEvent(@android.annotation.NonNull
    android.view.MotionEvent ev) {
        if (mWindow.superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * Called to process trackball events.  You can override this to
     * intercept all trackball events before they are dispatched to the
     * window.  Be sure to call this implementation for trackball events
     * that should be handled normally.
     *
     * @param ev
     * 		The trackball event.
     * @return boolean Return true if this event was consumed.
     */
    @java.lang.Override
    public boolean dispatchTrackballEvent(@android.annotation.NonNull
    android.view.MotionEvent ev) {
        if (mWindow.superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    /**
     * Called to process generic motion events.  You can override this to
     * intercept all generic motion events before they are dispatched to the
     * window.  Be sure to call this implementation for generic motion events
     * that should be handled normally.
     *
     * @param ev
     * 		The generic motion event.
     * @return boolean Return true if this event was consumed.
     */
    @java.lang.Override
    public boolean dispatchGenericMotionEvent(@android.annotation.NonNull
    android.view.MotionEvent ev) {
        if (mWindow.superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEvent(@android.annotation.NonNull
    android.view.accessibility.AccessibilityEvent event) {
        event.setClassName(getClass().getName());
        event.setPackageName(mContext.getPackageName());
        android.view.ViewGroup.LayoutParams params = getWindow().getAttributes();
        boolean isFullScreen = (params.width == android.view.ViewGroup.LayoutParams.MATCH_PARENT) && (params.height == android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        event.setFullScreen(isFullScreen);
        return false;
    }

    /**
     *
     *
     * @see Activity#onCreatePanelView(int)
     */
    @java.lang.Override
    public android.view.View onCreatePanelView(int featureId) {
        return null;
    }

    /**
     *
     *
     * @see Activity#onCreatePanelMenu(int, Menu)
     */
    @java.lang.Override
    public boolean onCreatePanelMenu(int featureId, @android.annotation.NonNull
    android.view.Menu menu) {
        if (featureId == android.view.Window.FEATURE_OPTIONS_PANEL) {
            return onCreateOptionsMenu(menu);
        }
        return false;
    }

    /**
     *
     *
     * @see Activity#onPreparePanel(int, View, Menu)
     */
    @java.lang.Override
    public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
        if ((featureId == android.view.Window.FEATURE_OPTIONS_PANEL) && (menu != null)) {
            return onPrepareOptionsMenu(menu) && menu.hasVisibleItems();
        }
        return true;
    }

    /**
     *
     *
     * @see Activity#onMenuOpened(int, Menu)
     */
    @java.lang.Override
    public boolean onMenuOpened(int featureId, android.view.Menu menu) {
        if (featureId == android.view.Window.FEATURE_ACTION_BAR) {
            mActionBar.dispatchMenuVisibilityChanged(true);
        }
        return true;
    }

    /**
     *
     *
     * @see Activity#onMenuItemSelected(int, MenuItem)
     */
    @java.lang.Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        return false;
    }

    /**
     *
     *
     * @see Activity#onPanelClosed(int, Menu)
     */
    @java.lang.Override
    public void onPanelClosed(int featureId, android.view.Menu menu) {
        if (featureId == android.view.Window.FEATURE_ACTION_BAR) {
            mActionBar.dispatchMenuVisibilityChanged(false);
        }
    }

    /**
     * It is usually safe to proxy this call to the owner activity's
     * {@link Activity#onCreateOptionsMenu(Menu)} if the client desires the same
     * menu for this Dialog.
     *
     * @see Activity#onCreateOptionsMenu(Menu)
     * @see #getOwnerActivity()
     */
    public boolean onCreateOptionsMenu(@android.annotation.NonNull
    android.view.Menu menu) {
        return true;
    }

    /**
     * It is usually safe to proxy this call to the owner activity's
     * {@link Activity#onPrepareOptionsMenu(Menu)} if the client desires the
     * same menu for this Dialog.
     *
     * @see Activity#onPrepareOptionsMenu(Menu)
     * @see #getOwnerActivity()
     */
    public boolean onPrepareOptionsMenu(@android.annotation.NonNull
    android.view.Menu menu) {
        return true;
    }

    /**
     *
     *
     * @see Activity#onOptionsItemSelected(MenuItem)
     */
    public boolean onOptionsItemSelected(@android.annotation.NonNull
    android.view.MenuItem item) {
        return false;
    }

    /**
     *
     *
     * @see Activity#onOptionsMenuClosed(Menu)
     */
    public void onOptionsMenuClosed(@android.annotation.NonNull
    android.view.Menu menu) {
    }

    /**
     *
     *
     * @see Activity#openOptionsMenu()
     */
    public void openOptionsMenu() {
        if (mWindow.hasFeature(android.view.Window.FEATURE_OPTIONS_PANEL)) {
            mWindow.openPanel(android.view.Window.FEATURE_OPTIONS_PANEL, null);
        }
    }

    /**
     *
     *
     * @see Activity#closeOptionsMenu()
     */
    public void closeOptionsMenu() {
        if (mWindow.hasFeature(android.view.Window.FEATURE_OPTIONS_PANEL)) {
            mWindow.closePanel(android.view.Window.FEATURE_OPTIONS_PANEL);
        }
    }

    /**
     *
     *
     * @see Activity#invalidateOptionsMenu()
     */
    public void invalidateOptionsMenu() {
        if (mWindow.hasFeature(android.view.Window.FEATURE_OPTIONS_PANEL)) {
            mWindow.invalidatePanelMenu(android.view.Window.FEATURE_OPTIONS_PANEL);
        }
    }

    /**
     *
     *
     * @see Activity#onCreateContextMenu(ContextMenu, View, ContextMenuInfo)
     */
    @java.lang.Override
    public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    }

    /**
     *
     *
     * @see Activity#registerForContextMenu(View)
     */
    public void registerForContextMenu(@android.annotation.NonNull
    android.view.View view) {
        view.setOnCreateContextMenuListener(this);
    }

    /**
     *
     *
     * @see Activity#unregisterForContextMenu(View)
     */
    public void unregisterForContextMenu(@android.annotation.NonNull
    android.view.View view) {
        view.setOnCreateContextMenuListener(null);
    }

    /**
     *
     *
     * @see Activity#openContextMenu(View)
     */
    public void openContextMenu(@android.annotation.NonNull
    android.view.View view) {
        view.showContextMenu();
    }

    /**
     *
     *
     * @see Activity#onContextItemSelected(MenuItem)
     */
    public boolean onContextItemSelected(@android.annotation.NonNull
    android.view.MenuItem item) {
        return false;
    }

    /**
     *
     *
     * @see Activity#onContextMenuClosed(Menu)
     */
    public void onContextMenuClosed(@android.annotation.NonNull
    android.view.Menu menu) {
    }

    /**
     * This hook is called when the user signals the desire to start a search.
     */
    @java.lang.Override
    public boolean onSearchRequested(@android.annotation.NonNull
    android.view.SearchEvent searchEvent) {
        mSearchEvent = searchEvent;
        return onSearchRequested();
    }

    /**
     * This hook is called when the user signals the desire to start a search.
     */
    @java.lang.Override
    public boolean onSearchRequested() {
        final android.app.SearchManager searchManager = ((android.app.SearchManager) (mContext.getSystemService(android.content.Context.SEARCH_SERVICE)));
        // associate search with owner activity
        final android.content.ComponentName appName = getAssociatedActivity();
        if ((appName != null) && (searchManager.getSearchableInfo(appName) != null)) {
            searchManager.startSearch(null, false, appName, null, false);
            dismiss();
            return true;
        } else {
            return false;
        }
    }

    /**
     * During the onSearchRequested() callbacks, this function will return the
     * {@link SearchEvent} that triggered the callback, if it exists.
     *
     * @return SearchEvent The SearchEvent that triggered the {@link #onSearchRequested} callback.
     */
    @android.annotation.Nullable
    public final android.view.SearchEvent getSearchEvent() {
        return mSearchEvent;
    }

    @java.lang.Override
    public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
        if ((mActionBar != null) && (mActionModeTypeStarting == android.view.ActionMode.TYPE_PRIMARY)) {
            return mActionBar.startActionMode(callback);
        }
        return null;
    }

    @java.lang.Override
    public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int type) {
        try {
            mActionModeTypeStarting = type;
            return onWindowStartingActionMode(callback);
        } finally {
            mActionModeTypeStarting = android.view.ActionMode.TYPE_PRIMARY;
        }
    }

    /**
     * {@inheritDoc }
     *
     * Note that if you override this method you should always call through
     * to the superclass implementation by calling super.onActionModeStarted(mode).
     */
    @java.lang.Override
    @android.annotation.CallSuper
    public void onActionModeStarted(android.view.ActionMode mode) {
        mActionMode = mode;
    }

    /**
     * {@inheritDoc }
     *
     * Note that if you override this method you should always call through
     * to the superclass implementation by calling super.onActionModeFinished(mode).
     */
    @java.lang.Override
    @android.annotation.CallSuper
    public void onActionModeFinished(android.view.ActionMode mode) {
        if (mode == mActionMode) {
            mActionMode = null;
        }
    }

    /**
     *
     *
     * @return The activity associated with this dialog, or null if there is no associated activity.
     */
    private android.content.ComponentName getAssociatedActivity() {
        android.app.Activity activity = mOwnerActivity;
        android.content.Context context = getContext();
        while ((activity == null) && (context != null)) {
            if (context instanceof android.app.Activity) {
                activity = ((android.app.Activity) (context));// found it!

            } else {
                context = (context instanceof android.content.ContextWrapper) ? ((android.content.ContextWrapper) (context)).getBaseContext()// unwrap one level
                 : null;// done

            }
        } 
        return activity == null ? null : activity.getComponentName();
    }

    /**
     * Request that key events come to this dialog. Use this if your
     * dialog has no views with focus, but the dialog still wants
     * a chance to process key events.
     *
     * @param get
     * 		true if the dialog should receive key events, false otherwise
     * @see android.view.Window#takeKeyEvents
     */
    public void takeKeyEvents(boolean get) {
        mWindow.takeKeyEvents(get);
    }

    /**
     * Enable extended window features.  This is a convenience for calling
     * {@link android.view.Window#requestFeature getWindow().requestFeature()}.
     *
     * @param featureId
     * 		The desired feature as defined in
     * 		{@link android.view.Window}.
     * @return Returns true if the requested feature is supported and now
    enabled.
     * @see android.view.Window#requestFeature
     */
    public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
    }

    /**
     * Convenience for calling
     * {@link android.view.Window#setFeatureDrawableResource}.
     */
    public final void setFeatureDrawableResource(int featureId, @android.annotation.DrawableRes
    int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
    }

    /**
     * Convenience for calling
     * {@link android.view.Window#setFeatureDrawableUri}.
     */
    public final void setFeatureDrawableUri(int featureId, @android.annotation.Nullable
    android.net.Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
    }

    /**
     * Convenience for calling
     * {@link android.view.Window#setFeatureDrawable(int, Drawable)}.
     */
    public final void setFeatureDrawable(int featureId, @android.annotation.Nullable
    android.graphics.drawable.Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
    }

    /**
     * Convenience for calling
     * {@link android.view.Window#setFeatureDrawableAlpha}.
     */
    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
    }

    @android.annotation.NonNull
    public android.view.LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
    }

    /**
     * Sets whether this dialog is cancelable with the
     * {@link KeyEvent#KEYCODE_BACK BACK} key.
     */
    public void setCancelable(boolean flag) {
        mCancelable = flag;
    }

    /**
     * Sets whether this dialog is canceled when touched outside the window's
     * bounds. If setting to true, the dialog is set to be cancelable if not
     * already set.
     *
     * @param cancel
     * 		Whether the dialog should be canceled when touched outside
     * 		the window.
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && (!mCancelable)) {
            mCancelable = true;
        }
        mWindow.setCloseOnTouchOutside(cancel);
    }

    /**
     * Cancel the dialog.  This is essentially the same as calling {@link #dismiss()}, but it will
     * also call your {@link DialogInterface.OnCancelListener} (if registered).
     */
    @java.lang.Override
    public void cancel() {
        if ((!mCanceled) && (mCancelMessage != null)) {
            mCanceled = true;
            // Obtain a new message so this dialog can be re-used
            android.os.Message.obtain(mCancelMessage).sendToTarget();
        }
        dismiss();
    }

    /**
     * Set a listener to be invoked when the dialog is canceled.
     *
     * <p>This will only be invoked when the dialog is canceled.
     * Cancel events alone will not capture all ways that
     * the dialog might be dismissed. If the creator needs
     * to know when a dialog is dismissed in general, use
     * {@link #setOnDismissListener}.</p>
     *
     * @param listener
     * 		The {@link DialogInterface.OnCancelListener} to use.
     */
    public void setOnCancelListener(@android.annotation.Nullable
    android.content.DialogInterface.OnCancelListener listener) {
        if (mCancelAndDismissTaken != null) {
            throw new java.lang.IllegalStateException(("OnCancelListener is already taken by " + mCancelAndDismissTaken) + " and can not be replaced.");
        }
        if (listener != null) {
            mCancelMessage = mListenersHandler.obtainMessage(android.app.Dialog.CANCEL, listener);
        } else {
            mCancelMessage = null;
        }
    }

    /**
     * Set a message to be sent when the dialog is canceled.
     *
     * @param msg
     * 		The msg to send when the dialog is canceled.
     * @see #setOnCancelListener(android.content.DialogInterface.OnCancelListener)
     */
    public void setCancelMessage(@android.annotation.Nullable
    android.os.Message msg) {
        mCancelMessage = msg;
    }

    /**
     * Set a listener to be invoked when the dialog is dismissed.
     *
     * @param listener
     * 		The {@link DialogInterface.OnDismissListener} to use.
     */
    public void setOnDismissListener(@android.annotation.Nullable
    android.content.DialogInterface.OnDismissListener listener) {
        if (mCancelAndDismissTaken != null) {
            throw new java.lang.IllegalStateException(("OnDismissListener is already taken by " + mCancelAndDismissTaken) + " and can not be replaced.");
        }
        if (listener != null) {
            mDismissMessage = mListenersHandler.obtainMessage(android.app.Dialog.DISMISS, listener);
        } else {
            mDismissMessage = null;
        }
    }

    /**
     * Sets a listener to be invoked when the dialog is shown.
     *
     * @param listener
     * 		The {@link DialogInterface.OnShowListener} to use.
     */
    public void setOnShowListener(@android.annotation.Nullable
    android.content.DialogInterface.OnShowListener listener) {
        if (listener != null) {
            mShowMessage = mListenersHandler.obtainMessage(android.app.Dialog.SHOW, listener);
        } else {
            mShowMessage = null;
        }
    }

    /**
     * Set a message to be sent when the dialog is dismissed.
     *
     * @param msg
     * 		The msg to send when the dialog is dismissed.
     */
    public void setDismissMessage(@android.annotation.Nullable
    android.os.Message msg) {
        mDismissMessage = msg;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean takeCancelAndDismissListeners(@android.annotation.Nullable
    java.lang.String msg, @android.annotation.Nullable
    android.content.DialogInterface.OnCancelListener cancel, @android.annotation.Nullable
    android.content.DialogInterface.OnDismissListener dismiss) {
        if (mCancelAndDismissTaken != null) {
            mCancelAndDismissTaken = null;
        } else
            if ((mCancelMessage != null) || (mDismissMessage != null)) {
                return false;
            }

        setOnCancelListener(cancel);
        setOnDismissListener(dismiss);
        mCancelAndDismissTaken = msg;
        return true;
    }

    /**
     * By default, this will use the owner Activity's suggested stream type.
     *
     * @see Activity#setVolumeControlStream(int)
     * @see #setOwnerActivity(Activity)
     */
    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    /**
     *
     *
     * @see Activity#getVolumeControlStream()
     */
    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    /**
     * Sets the callback that will be called if a key is dispatched to the dialog.
     */
    public void setOnKeyListener(@android.annotation.Nullable
    android.content.DialogInterface.OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
    }

    private static final class ListenersHandler extends android.os.Handler {
        private final java.lang.ref.WeakReference<android.content.DialogInterface> mDialog;

        public ListenersHandler(android.app.Dialog dialog) {
            mDialog = new java.lang.ref.WeakReference<>(dialog);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.app.Dialog.DISMISS :
                    ((android.content.DialogInterface.OnDismissListener) (msg.obj)).onDismiss(mDialog.get());
                    break;
                case android.app.Dialog.CANCEL :
                    ((android.content.DialogInterface.OnCancelListener) (msg.obj)).onCancel(mDialog.get());
                    break;
                case android.app.Dialog.SHOW :
                    ((android.content.DialogInterface.OnShowListener) (msg.obj)).onShow(mDialog.get());
                    break;
            }
        }
    }
}

