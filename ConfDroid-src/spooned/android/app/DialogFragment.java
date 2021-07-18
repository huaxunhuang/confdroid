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
 * A fragment that displays a dialog window, floating on top of its
 * activity's window.  This fragment contains a Dialog object, which it
 * displays as appropriate based on the fragment's state.  Control of
 * the dialog (deciding when to show, hide, dismiss it) should be done through
 * the API here, not with direct calls on the dialog.
 *
 * <p>Implementations should override this class and implement
 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} to supply the
 * content of the dialog.  Alternatively, they can override
 * {@link #onCreateDialog(Bundle)} to create an entirely custom dialog, such
 * as an AlertDialog, with its own content.
 *
 * <p>Topics covered here:
 * <ol>
 * <li><a href="#Lifecycle">Lifecycle</a>
 * <li><a href="#BasicDialog">Basic Dialog</a>
 * <li><a href="#AlertDialog">Alert Dialog</a>
 * <li><a href="#DialogOrEmbed">Selecting Between Dialog or Embedding</a>
 * </ol>
 *
 * <a name="Lifecycle"></a>
 * <h3>Lifecycle</h3>
 *
 * <p>DialogFragment does various things to keep the fragment's lifecycle
 * driving it, instead of the Dialog.  Note that dialogs are generally
 * autonomous entities -- they are their own window, receiving their own
 * input events, and often deciding on their own when to disappear (by
 * receiving a back key event or the user clicking on a button).
 *
 * <p>DialogFragment needs to ensure that what is happening with the Fragment
 * and Dialog states remains consistent.  To do this, it watches for dismiss
 * events from the dialog and takes care of removing its own state when they
 * happen.  This means you should use {@link #show(FragmentManager, String)}
 * or {@link #show(FragmentTransaction, String)} to add an instance of
 * DialogFragment to your UI, as these keep track of how DialogFragment should
 * remove itself when the dialog is dismissed.
 *
 * <a name="BasicDialog"></a>
 * <h3>Basic Dialog</h3>
 *
 * <p>The simplest use of DialogFragment is as a floating container for the
 * fragment's view hierarchy.  A simple implementation may look like this:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentDialog.java
 *      dialog}
 *
 * <p>An example showDialog() method on the Activity could be:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentDialog.java
 *      add_dialog}
 *
 * <p>This removes any currently shown dialog, creates a new DialogFragment
 * with an argument, and shows it as a new state on the back stack.  When the
 * transaction is popped, the current DialogFragment and its Dialog will be
 * destroyed, and the previous one (if any) re-shown.  Note that in this case
 * DialogFragment will take care of popping the transaction of the Dialog
 * is dismissed separately from it.
 *
 * <a name="AlertDialog"></a>
 * <h3>Alert Dialog</h3>
 *
 * <p>Instead of (or in addition to) implementing {@link #onCreateView} to
 * generate the view hierarchy inside of a dialog, you may implement
 * {@link #onCreateDialog(Bundle)} to create your own custom Dialog object.
 *
 * <p>This is most useful for creating an {@link AlertDialog}, allowing you
 * to display standard alerts to the user that are managed by a fragment.
 * A simple example implementation of this is:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentAlertDialog.java
 *      dialog}
 *
 * <p>The activity creating this fragment may have the following methods to
 * show the dialog and receive results from it:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentAlertDialog.java
 *      activity}
 *
 * <p>Note that in this case the fragment is not placed on the back stack, it
 * is just added as an indefinitely running fragment.  Because dialogs normally
 * are modal, this will still operate as a back stack, since the dialog will
 * capture user input until it is dismissed.  When it is dismissed, DialogFragment
 * will take care of removing itself from its fragment manager.
 *
 * <a name="DialogOrEmbed"></a>
 * <h3>Selecting Between Dialog or Embedding</h3>
 *
 * <p>A DialogFragment can still optionally be used as a normal fragment, if
 * desired.  This is useful if you have a fragment that in some cases should
 * be shown as a dialog and others embedded in a larger UI.  This behavior
 * will normally be automatically selected for you based on how you are using
 * the fragment, but can be customized with {@link #setShowsDialog(boolean)}.
 *
 * <p>For example, here is a simple dialog fragment:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentDialogOrActivity.java
 *      dialog}
 *
 * <p>An instance of this fragment can be created and shown as a dialog:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentDialogOrActivity.java
 *      show_dialog}
 *
 * <p>It can also be added as content in a view hierarchy:
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentDialogOrActivity.java
 *      embed}
 */
public class DialogFragment extends android.app.Fragment implements android.content.DialogInterface.OnCancelListener , android.content.DialogInterface.OnDismissListener {
    /**
     * Style for {@link #setStyle(int, int)}: a basic,
     * normal dialog.
     */
    public static final int STYLE_NORMAL = 0;

    /**
     * Style for {@link #setStyle(int, int)}: don't include
     * a title area.
     */
    public static final int STYLE_NO_TITLE = 1;

    /**
     * Style for {@link #setStyle(int, int)}: don't draw
     * any frame at all; the view hierarchy returned by {@link #onCreateView}
     * is entirely responsible for drawing the dialog.
     */
    public static final int STYLE_NO_FRAME = 2;

    /**
     * Style for {@link #setStyle(int, int)}: like
     * {@link #STYLE_NO_FRAME}, but also disables all input to the dialog.
     * The user can not touch it, and its window will not receive input focus.
     */
    public static final int STYLE_NO_INPUT = 3;

    private static final java.lang.String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";

    private static final java.lang.String SAVED_STYLE = "android:style";

    private static final java.lang.String SAVED_THEME = "android:theme";

    private static final java.lang.String SAVED_CANCELABLE = "android:cancelable";

    private static final java.lang.String SAVED_SHOWS_DIALOG = "android:showsDialog";

    private static final java.lang.String SAVED_BACK_STACK_ID = "android:backStackId";

    int mStyle = android.app.DialogFragment.STYLE_NORMAL;

    int mTheme = 0;

    boolean mCancelable = true;

    boolean mShowsDialog = true;

    int mBackStackId = -1;

    android.app.Dialog mDialog;

    boolean mViewDestroyed;

    boolean mDismissed;

    boolean mShownByMe;

    public DialogFragment() {
    }

    /**
     * Call to customize the basic appearance and behavior of the
     * fragment's dialog.  This can be used for some common dialog behaviors,
     * taking care of selecting flags, theme, and other options for you.  The
     * same effect can be achieve by manually setting Dialog and Window
     * attributes yourself.  Calling this after the fragment's Dialog is
     * created will have no effect.
     *
     * @param style
     * 		Selects a standard style: may be {@link #STYLE_NORMAL},
     * 		{@link #STYLE_NO_TITLE}, {@link #STYLE_NO_FRAME}, or
     * 		{@link #STYLE_NO_INPUT}.
     * @param theme
     * 		Optional custom theme.  If 0, an appropriate theme (based
     * 		on the style) will be selected for you.
     */
    public void setStyle(int style, int theme) {
        mStyle = style;
        if ((mStyle == android.app.DialogFragment.STYLE_NO_FRAME) || (mStyle == android.app.DialogFragment.STYLE_NO_INPUT)) {
            mTheme = com.android.internal.R.style.Theme_DeviceDefault_Dialog_NoFrame;
        }
        if (theme != 0) {
            mTheme = theme;
        }
    }

    /**
     * Display the dialog, adding the fragment to the given FragmentManager.  This
     * is a convenience for explicitly creating a transaction, adding the
     * fragment to it with the given tag, and committing it.  This does
     * <em>not</em> add the transaction to the back stack.  When the fragment
     * is dismissed, a new transaction will be executed to remove it from
     * the activity.
     *
     * @param manager
     * 		The FragmentManager this fragment will be added to.
     * @param tag
     * 		The tag for this fragment, as per
     * 		{@link FragmentTransaction#add(Fragment, String) FragmentTransaction.add}.
     */
    public void show(android.app.FragmentManager manager, java.lang.String tag) {
        mDismissed = false;
        mShownByMe = true;
        android.app.FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    /**
     * {@hide }
     */
    public void showAllowingStateLoss(android.app.FragmentManager manager, java.lang.String tag) {
        mDismissed = false;
        mShownByMe = true;
        android.app.FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * Display the dialog, adding the fragment using an existing transaction
     * and then committing the transaction.
     *
     * @param transaction
     * 		An existing transaction in which to add the fragment.
     * @param tag
     * 		The tag for this fragment, as per
     * 		{@link FragmentTransaction#add(Fragment, String) FragmentTransaction.add}.
     * @return Returns the identifier of the committed transaction, as per
    {@link FragmentTransaction#commit() FragmentTransaction.commit()}.
     */
    public int show(android.app.FragmentTransaction transaction, java.lang.String tag) {
        mDismissed = false;
        mShownByMe = true;
        transaction.add(this, tag);
        mViewDestroyed = false;
        mBackStackId = transaction.commit();
        return mBackStackId;
    }

    /**
     * Dismiss the fragment and its dialog.  If the fragment was added to the
     * back stack, all back stack state up to and including this entry will
     * be popped.  Otherwise, a new transaction will be committed to remove
     * the fragment.
     */
    public void dismiss() {
        dismissInternal(false);
    }

    /**
     * Version of {@link #dismiss()} that uses
     * {@link FragmentTransaction#commitAllowingStateLoss()
     * FragmentTransaction.commitAllowingStateLoss()}.  See linked
     * documentation for further details.
     */
    public void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    void dismissInternal(boolean allowStateLoss) {
        if (mDismissed) {
            return;
        }
        mDismissed = true;
        mShownByMe = false;
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mViewDestroyed = true;
        if (mBackStackId >= 0) {
            getFragmentManager().popBackStack(mBackStackId, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mBackStackId = -1;
        } else {
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            if (allowStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
        }
    }

    public android.app.Dialog getDialog() {
        return mDialog;
    }

    public int getTheme() {
        return mTheme;
    }

    /**
     * Control whether the shown Dialog is cancelable.  Use this instead of
     * directly calling {@link Dialog#setCancelable(boolean)
     * Dialog.setCancelable(boolean)}, because DialogFragment needs to change
     * its behavior based on this.
     *
     * @param cancelable
     * 		If true, the dialog is cancelable.  The default
     * 		is true.
     */
    public void setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        if (mDialog != null)
            mDialog.setCancelable(cancelable);

    }

    /**
     * Return the current value of {@link #setCancelable(boolean)}.
     */
    public boolean isCancelable() {
        return mCancelable;
    }

    /**
     * Controls whether this fragment should be shown in a dialog.  If not
     * set, no Dialog will be created in {@link #onActivityCreated(Bundle)},
     * and the fragment's view hierarchy will thus not be added to it.  This
     * allows you to instead use it as a normal fragment (embedded inside of
     * its activity).
     *
     * <p>This is normally set for you based on whether the fragment is
     * associated with a container view ID passed to
     * {@link FragmentTransaction#add(int, Fragment) FragmentTransaction.add(int, Fragment)}.
     * If the fragment was added with a container, setShowsDialog will be
     * initialized to false; otherwise, it will be true.
     *
     * @param showsDialog
     * 		If true, the fragment will be displayed in a Dialog.
     * 		If false, no Dialog will be created and the fragment's view hierarchly
     * 		left undisturbed.
     */
    public void setShowsDialog(boolean showsDialog) {
        mShowsDialog = showsDialog;
    }

    /**
     * Return the current value of {@link #setShowsDialog(boolean)}.
     */
    public boolean getShowsDialog() {
        return mShowsDialog;
    }

    @java.lang.Override
    public void onAttach(android.content.Context context) {
        super.onAttach(context);
        if (!mShownByMe) {
            // If not explicitly shown through our API, take this as an
            // indication that the dialog is no longer dismissed.
            mDismissed = false;
        }
    }

    @java.lang.Override
    public void onDetach() {
        super.onDetach();
        if ((!mShownByMe) && (!mDismissed)) {
            // The fragment was not shown by a direct call here, it is not
            // dismissed, and now it is being detached...  well, okay, thou
            // art now dismissed.  Have fun.
            mDismissed = true;
        }
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShowsDialog = mContainerId == 0;
        if (savedInstanceState != null) {
            mStyle = savedInstanceState.getInt(android.app.DialogFragment.SAVED_STYLE, android.app.DialogFragment.STYLE_NORMAL);
            mTheme = savedInstanceState.getInt(android.app.DialogFragment.SAVED_THEME, 0);
            mCancelable = savedInstanceState.getBoolean(android.app.DialogFragment.SAVED_CANCELABLE, true);
            mShowsDialog = savedInstanceState.getBoolean(android.app.DialogFragment.SAVED_SHOWS_DIALOG, mShowsDialog);
            mBackStackId = savedInstanceState.getInt(android.app.DialogFragment.SAVED_BACK_STACK_ID, -1);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.LayoutInflater getLayoutInflater(android.os.Bundle savedInstanceState) {
        if (!mShowsDialog) {
            return super.getLayoutInflater(savedInstanceState);
        }
        mDialog = onCreateDialog(savedInstanceState);
        switch (mStyle) {
            case android.app.DialogFragment.STYLE_NO_INPUT :
                mDialog.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                // fall through...
            case android.app.DialogFragment.STYLE_NO_FRAME :
            case android.app.DialogFragment.STYLE_NO_TITLE :
                mDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (mDialog != null) {
            return ((android.view.LayoutInflater) (mDialog.getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        }
        return ((android.view.LayoutInflater) (mHost.getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
    }

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} does not need
     * to be implemented since the AlertDialog takes care of its own content.
     *
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     *
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override {@link #onCancel(DialogInterface)}
     * and {@link #onDismiss(DialogInterface)}.</p>
     *
     * @param savedInstanceState
     * 		The last saved instance state of the Fragment,
     * 		or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    public android.app.Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
        return new android.app.Dialog(getActivity(), getTheme());
    }

    public void onCancel(android.content.DialogInterface dialog) {
    }

    public void onDismiss(android.content.DialogInterface dialog) {
        if (!mViewDestroyed) {
            // Note: we need to use allowStateLoss, because the dialog
            // dispatches this asynchronously so we can receive the call
            // after the activity is paused.  Worst case, when the user comes
            // back to the activity they see the dialog again.
            dismissInternal(true);
        }
    }

    @java.lang.Override
    public void onActivityCreated(android.os.Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mShowsDialog) {
            return;
        }
        android.view.View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new java.lang.IllegalStateException("DialogFragment can not be attached to a container view");
            }
            mDialog.setContentView(view);
        }
        final android.app.Activity activity = getActivity();
        if (activity != null) {
            mDialog.setOwnerActivity(activity);
        }
        mDialog.setCancelable(mCancelable);
        if (!mDialog.takeCancelAndDismissListeners("DialogFragment", this, this)) {
            throw new java.lang.IllegalStateException("You can not set Dialog's OnCancelListener or OnDismissListener");
        }
        if (savedInstanceState != null) {
            android.os.Bundle dialogState = savedInstanceState.getBundle(android.app.DialogFragment.SAVED_DIALOG_STATE_TAG);
            if (dialogState != null) {
                mDialog.onRestoreInstanceState(dialogState);
            }
        }
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        if (mDialog != null) {
            mViewDestroyed = false;
            mDialog.show();
        }
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDialog != null) {
            android.os.Bundle dialogState = mDialog.onSaveInstanceState();
            if (dialogState != null) {
                outState.putBundle(android.app.DialogFragment.SAVED_DIALOG_STATE_TAG, dialogState);
            }
        }
        if (mStyle != android.app.DialogFragment.STYLE_NORMAL) {
            outState.putInt(android.app.DialogFragment.SAVED_STYLE, mStyle);
        }
        if (mTheme != 0) {
            outState.putInt(android.app.DialogFragment.SAVED_THEME, mTheme);
        }
        if (!mCancelable) {
            outState.putBoolean(android.app.DialogFragment.SAVED_CANCELABLE, mCancelable);
        }
        if (!mShowsDialog) {
            outState.putBoolean(android.app.DialogFragment.SAVED_SHOWS_DIALOG, mShowsDialog);
        }
        if (mBackStackId != (-1)) {
            outState.putInt(android.app.DialogFragment.SAVED_BACK_STACK_ID, mBackStackId);
        }
    }

    @java.lang.Override
    public void onStop() {
        super.onStop();
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    /**
     * Remove dialog.
     */
    @java.lang.Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDialog != null) {
            // Set removed here because this dismissal is just to hide
            // the dialog -- we don't want this to cause the fragment to
            // actually be removed.
            mViewDestroyed = true;
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @java.lang.Override
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.println("DialogFragment:");
        writer.print(prefix);
        writer.print("  mStyle=");
        writer.print(mStyle);
        writer.print(" mTheme=0x");
        writer.println(java.lang.Integer.toHexString(mTheme));
        writer.print(prefix);
        writer.print("  mCancelable=");
        writer.print(mCancelable);
        writer.print(" mShowsDialog=");
        writer.print(mShowsDialog);
        writer.print(" mBackStackId=");
        writer.println(mBackStackId);
        writer.print(prefix);
        writer.print("  mDialog=");
        writer.println(mDialog);
        writer.print(prefix);
        writer.print("  mViewDestroyed=");
        writer.print(mViewDestroyed);
        writer.print(" mDismissed=");
        writer.print(mDismissed);
        writer.print(" mShownByMe=");
        writer.println(mShownByMe);
    }
}

