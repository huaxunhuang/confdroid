/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Base class for activities that want to use the support-based
 * {@link android.support.v4.app.Fragment} and
 * {@link android.support.v4.content.Loader} APIs.
 *
 * <p>When using this class as opposed to new platform's built-in fragment
 * and loader support, you must use the {@link #getSupportFragmentManager()}
 * and {@link #getSupportLoaderManager()} methods respectively to access
 * those features.
 *
 * <p>Known limitations:</p>
 * <ul>
 * <li> <p>When using the <code>&lt;fragment></code> tag, this implementation can not
 * use the parent view's ID as the new fragment's ID.  You must explicitly
 * specify an ID (or tag) in the <code>&lt;fragment></code>.</p>
 * <li> <p>Prior to Honeycomb (3.0), an activity's state was saved before pausing.
 * Fragments are a significant amount of new state, and dynamic enough that one
 * often wants them to change between pausing and stopping.  These classes
 * throw an exception if you try to change the fragment state after it has been
 * saved, to avoid accidental loss of UI state.  However this is too restrictive
 * prior to Honeycomb, where the state is saved before pausing.  To address this,
 * when running on platforms prior to Honeycomb an exception will not be thrown
 * if you change fragments between the state save and the activity being stopped.
 * This means that in some cases if the activity is restored from its last saved
 * state, this may be a snapshot slightly before what the user last saw.</p>
 * </ul>
 */
public class FragmentActivity extends android.support.v4.app.BaseFragmentActivityJB implements android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback , android.support.v4.app.ActivityCompatApi23.RequestPermissionsRequestCodeValidator {
    private static final java.lang.String TAG = "FragmentActivity";

    static final java.lang.String FRAGMENTS_TAG = "android:support:fragments";

    static final java.lang.String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";

    static final java.lang.String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";

    static final java.lang.String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";

    static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 0xffff - 1;

    // This is the SDK API version of Honeycomb (3.0).
    private static final int HONEYCOMB = 11;

    static final int MSG_REALLY_STOPPED = 1;

    static final int MSG_RESUME_PENDING = 2;

    final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.support.v4.app.FragmentActivity.MSG_REALLY_STOPPED :
                    if (mStopped) {
                        doReallyStop(false);
                    }
                    break;
                case android.support.v4.app.FragmentActivity.MSG_RESUME_PENDING :
                    onResumeFragments();
                    mFragments.execPendingActions();
                    break;
                default :
                    super.handleMessage(msg);
            }
        }
    };

    final android.support.v4.app.FragmentController mFragments = android.support.v4.app.FragmentController.createController(new android.support.v4.app.FragmentActivity.HostCallbacks());

    boolean mCreated;

    boolean mResumed;

    boolean mStopped;

    boolean mReallyStopped;

    boolean mRetaining;

    boolean mOptionsMenuInvalidated;

    boolean mRequestedPermissionsFromFragment;

    // A hint for the next candidate request index. Request indicies are ints between 0 and 2^16-1
    // which are encoded into the upper 16 bits of the requestCode for
    // Fragment.startActivityForResult(...) calls. This allows us to dispatch onActivityResult(...)
    // to the appropriate Fragment. Request indicies are allocated by allocateRequestIndex(...).
    int mNextCandidateRequestIndex;

    // A map from request index to Fragment "who" (i.e. a Fragment's unique identifier). Used to
    // keep track of the originating Fragment for Fragment.startActivityForResult(...) calls, so we
    // can dispatch the onActivityResult(...) to the appropriate Fragment. Will only contain entries
    // for startActivityForResult calls where a result has not yet been delivered.
    android.support.v4.util.SparseArrayCompat<java.lang.String> mPendingFragmentActivityResults;

    static final class NonConfigurationInstances {
        java.lang.Object custom;

        android.support.v4.app.FragmentManagerNonConfig fragments;

        android.support.v4.util.SimpleArrayMap<java.lang.String, android.support.v4.app.LoaderManager> loaders;
    }

    android.support.v4.media.session.MediaControllerCompat mMediaController;

    // ------------------------------------------------------------------------
    // HOOKS INTO ACTIVITY
    // ------------------------------------------------------------------------
    /**
     * Dispatch incoming result to the correct fragment.
     */
    @java.lang.Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        mFragments.noteStateNotSaved();
        int requestIndex = requestCode >> 16;
        if (requestIndex != 0) {
            requestIndex--;
            java.lang.String who = mPendingFragmentActivityResults.get(requestIndex);
            mPendingFragmentActivityResults.remove(requestIndex);
            if (who == null) {
                android.util.Log.w(android.support.v4.app.FragmentActivity.TAG, "Activity result delivered for unknown Fragment.");
                return;
            }
            android.support.v4.app.Fragment targetFragment = mFragments.findFragmentByWho(who);
            if (targetFragment == null) {
                android.util.Log.w(android.support.v4.app.FragmentActivity.TAG, "Activity result no fragment exists for who: " + who);
            } else {
                targetFragment.onActivityResult(requestCode & 0xffff, resultCode, data);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @java.lang.Override
    public void onBackPressed() {
        if (!mFragments.getSupportFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    /**
     * Sets a {@link MediaControllerCompat} for later retrieval via
     * {@link #getSupportMediaController()}.
     *
     * <p>On API 21 and later, this controller will be tied to the window of the activity and
     * media key and volume events which are received while the Activity is in the foreground
     * will be forwarded to the controller and used to invoke transport controls or adjust the
     * volume. Prior to API 21, the global handling of media key and volume events through an
     * active {@link android.support.v4.media.session.MediaSessionCompat} and media button receiver
     * will still be respected.</p>
     *
     * @param mediaController
     * 		The controller for the session which should receive
     * 		media keys and volume changes on API 21 and later.
     * @see #getSupportMediaController()
     * @see #setMediaController(android.media.session.MediaController)
     */
    public final void setSupportMediaController(android.support.v4.media.session.MediaControllerCompat mediaController) {
        mMediaController = mediaController;
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v4.app.ActivityCompatApi21.setMediaController(this, mediaController.getMediaController());
        }
    }

    /**
     * Retrieves the current {@link MediaControllerCompat} for sending media key and volume events.
     *
     * @return The controller which should receive events.
     * @see #setSupportMediaController(MediaControllerCompat)
     * @see #getMediaController()
     */
    public final android.support.v4.media.session.MediaControllerCompat getSupportMediaController() {
        return mMediaController;
    }

    /**
     * Reverses the Activity Scene entry Transition and triggers the calling Activity
     * to reverse its exit Transition. When the exit Transition completes,
     * {@link #finish()} is called. If no entry Transition was used, finish() is called
     * immediately and the Activity exit Transition is run.
     *
     * <p>On Android 4.4 or lower, this method only finishes the Activity with no
     * special exit transition.</p>
     */
    public void supportFinishAfterTransition() {
        android.support.v4.app.ActivityCompat.finishAfterTransition(this);
    }

    /**
     * When {@link android.app.ActivityOptions#makeSceneTransitionAnimation(Activity,
     * android.view.View, String)} was used to start an Activity, <var>callback</var>
     * will be called to handle shared elements on the <i>launched</i> Activity. This requires
     * {@link Window#FEATURE_CONTENT_TRANSITIONS}.
     *
     * @param callback
     * 		Used to manipulate shared element transitions on the launched Activity.
     */
    public void setEnterSharedElementCallback(android.support.v4.app.SharedElementCallback callback) {
        android.support.v4.app.ActivityCompat.setEnterSharedElementCallback(this, callback);
    }

    /**
     * When {@link android.app.ActivityOptions#makeSceneTransitionAnimation(Activity,
     * android.view.View, String)} was used to start an Activity, <var>listener</var>
     * will be called to handle shared elements on the <i>launching</i> Activity. Most
     * calls will only come when returning from the started Activity.
     * This requires {@link Window#FEATURE_CONTENT_TRANSITIONS}.
     *
     * @param listener
     * 		Used to manipulate shared element transitions on the launching Activity.
     */
    public void setExitSharedElementCallback(android.support.v4.app.SharedElementCallback listener) {
        android.support.v4.app.ActivityCompat.setExitSharedElementCallback(this, listener);
    }

    /**
     * Support library version of {@link android.app.Activity#postponeEnterTransition()} that works
     * only on API 21 and later.
     */
    public void supportPostponeEnterTransition() {
        android.support.v4.app.ActivityCompat.postponeEnterTransition(this);
    }

    /**
     * Support library version of {@link android.app.Activity#startPostponedEnterTransition()}
     * that only works with API 21 and later.
     */
    public void supportStartPostponedEnterTransition() {
        android.support.v4.app.ActivityCompat.startPostponedEnterTransition(this);
    }

    /**
     * {@inheritDoc }
     *
     * <p><strong>Note:</strong> If you override this method you must call
     * <code>super.onMultiWindowModeChanged</code> to correctly dispatch the event
     * to support fragments attached to this activity.</p>
     *
     * @param isInMultiWindowMode
     * 		True if the activity is in multi-window mode.
     */
    @android.support.annotation.CallSuper
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        mFragments.dispatchMultiWindowModeChanged(isInMultiWindowMode);
    }

    /**
     * {@inheritDoc }
     *
     * <p><strong>Note:</strong> If you override this method you must call
     * <code>super.onPictureInPictureModeChanged</code> to correctly dispatch the event
     * to support fragments attached to this activity.</p>
     *
     * @param isInPictureInPictureMode
     * 		True if the activity is in picture-in-picture mode.
     */
    @android.support.annotation.CallSuper
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        mFragments.dispatchPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    /**
     * Dispatch configuration change to all fragments.
     */
    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mFragments.dispatchConfigurationChanged(newConfig);
    }

    /**
     * Perform initialization of all fragments and loaders.
     */
    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    protected void onCreate(@android.support.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        /* parent */
        mFragments.attachHost(null);
        super.onCreate(savedInstanceState);
        android.support.v4.app.FragmentActivity.NonConfigurationInstances nc = ((android.support.v4.app.FragmentActivity.NonConfigurationInstances) (getLastNonConfigurationInstance()));
        if (nc != null) {
            mFragments.restoreLoaderNonConfig(nc.loaders);
        }
        if (savedInstanceState != null) {
            android.os.Parcelable p = savedInstanceState.getParcelable(android.support.v4.app.FragmentActivity.FRAGMENTS_TAG);
            mFragments.restoreAllState(p, nc != null ? nc.fragments : null);
            // Check if there are any pending onActivityResult calls to descendent Fragments.
            if (savedInstanceState.containsKey(android.support.v4.app.FragmentActivity.NEXT_CANDIDATE_REQUEST_INDEX_TAG)) {
                mNextCandidateRequestIndex = savedInstanceState.getInt(android.support.v4.app.FragmentActivity.NEXT_CANDIDATE_REQUEST_INDEX_TAG);
                int[] requestCodes = savedInstanceState.getIntArray(android.support.v4.app.FragmentActivity.ALLOCATED_REQUEST_INDICIES_TAG);
                java.lang.String[] fragmentWhos = savedInstanceState.getStringArray(android.support.v4.app.FragmentActivity.REQUEST_FRAGMENT_WHO_TAG);
                if (((requestCodes == null) || (fragmentWhos == null)) || (requestCodes.length != fragmentWhos.length)) {
                    android.util.Log.w(android.support.v4.app.FragmentActivity.TAG, "Invalid requestCode mapping in savedInstanceState.");
                } else {
                    mPendingFragmentActivityResults = new android.support.v4.util.SparseArrayCompat<>(requestCodes.length);
                    for (int i = 0; i < requestCodes.length; i++) {
                        mPendingFragmentActivityResults.put(requestCodes[i], fragmentWhos[i]);
                    }
                }
            }
        }
        if (mPendingFragmentActivityResults == null) {
            mPendingFragmentActivityResults = new android.support.v4.util.SparseArrayCompat<>();
            mNextCandidateRequestIndex = 0;
        }
        mFragments.dispatchCreate();
    }

    /**
     * Dispatch to Fragment.onCreateOptionsMenu().
     */
    @java.lang.Override
    public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
        if (featureId == android.view.Window.FEATURE_OPTIONS_PANEL) {
            boolean show = super.onCreatePanelMenu(featureId, menu);
            show |= mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
            if (android.os.Build.VERSION.SDK_INT >= android.support.v4.app.FragmentActivity.HONEYCOMB) {
                return show;
            }
            // Prior to Honeycomb, the framework can't invalidate the options
            // menu, so we must always say we have one in case the app later
            // invalidates it and needs to have it shown.
            return true;
        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    @java.lang.Override
    final android.view.View dispatchFragmentsOnCreateView(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        return mFragments.onCreateView(parent, name, context, attrs);
    }

    /**
     * Destroy all fragments and loaders.
     */
    @java.lang.Override
    protected void onDestroy() {
        super.onDestroy();
        doReallyStop(false);
        mFragments.dispatchDestroy();
        mFragments.doLoaderDestroy();
    }

    /**
     * Dispatch onLowMemory() to all fragments.
     */
    @java.lang.Override
    public void onLowMemory() {
        super.onLowMemory();
        mFragments.dispatchLowMemory();
    }

    /**
     * Dispatch context and options menu to fragments.
     */
    @java.lang.Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        if (super.onMenuItemSelected(featureId, item)) {
            return true;
        }
        switch (featureId) {
            case android.view.Window.FEATURE_OPTIONS_PANEL :
                return mFragments.dispatchOptionsItemSelected(item);
            case android.view.Window.FEATURE_CONTEXT_MENU :
                return mFragments.dispatchContextItemSelected(item);
            default :
                return false;
        }
    }

    /**
     * Call onOptionsMenuClosed() on fragments.
     */
    @java.lang.Override
    public void onPanelClosed(int featureId, android.view.Menu menu) {
        switch (featureId) {
            case android.view.Window.FEATURE_OPTIONS_PANEL :
                mFragments.dispatchOptionsMenuClosed(menu);
                break;
        }
        super.onPanelClosed(featureId, menu);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @java.lang.Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        if (mHandler.hasMessages(android.support.v4.app.FragmentActivity.MSG_RESUME_PENDING)) {
            mHandler.removeMessages(android.support.v4.app.FragmentActivity.MSG_RESUME_PENDING);
            onResumeFragments();
        }
        mFragments.dispatchPause();
    }

    /**
     * Handle onNewIntent() to inform the fragment manager that the
     * state is not saved.  If you are handling new intents and may be
     * making changes to the fragment state, you want to be sure to call
     * through to the super-class here first.  Otherwise, if your state
     * is saved but the activity is not stopped, you could get an
     * onNewIntent() call which happens before onResume() and trying to
     * perform fragment operations at that point will throw IllegalStateException
     * because the fragment manager thinks the state is still saved.
     */
    @java.lang.Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
        mFragments.noteStateNotSaved();
    }

    /**
     * Hook in to note that fragment state is no longer saved.
     */
    public void onStateNotSaved() {
        mFragments.noteStateNotSaved();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @java.lang.Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(android.support.v4.app.FragmentActivity.MSG_RESUME_PENDING);
        mResumed = true;
        mFragments.execPendingActions();
    }

    /**
     * Dispatch onResume() to fragments.
     */
    @java.lang.Override
    protected void onPostResume() {
        super.onPostResume();
        mHandler.removeMessages(android.support.v4.app.FragmentActivity.MSG_RESUME_PENDING);
        onResumeFragments();
        mFragments.execPendingActions();
    }

    /**
     * This is the fragment-orientated version of {@link #onResume()} that you
     * can override to perform operations in the Activity at the same point
     * where its fragments are resumed.  Be sure to always call through to
     * the super-class.
     */
    protected void onResumeFragments() {
        mFragments.dispatchResume();
    }

    /**
     * Dispatch onPrepareOptionsMenu() to fragments.
     */
    @java.lang.Override
    public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
        if ((featureId == android.view.Window.FEATURE_OPTIONS_PANEL) && (menu != null)) {
            if (mOptionsMenuInvalidated) {
                mOptionsMenuInvalidated = false;
                menu.clear();
                onCreatePanelMenu(featureId, menu);
            }
            boolean goforit = onPrepareOptionsPanel(view, menu);
            goforit |= mFragments.dispatchPrepareOptionsMenu(menu);
            return goforit;
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected boolean onPrepareOptionsPanel(android.view.View view, android.view.Menu menu) {
        return super.onPreparePanel(android.view.Window.FEATURE_OPTIONS_PANEL, view, menu);
    }

    /**
     * Retain all appropriate fragment and loader state.  You can NOT
     * override this yourself!  Use {@link #onRetainCustomNonConfigurationInstance()}
     * if you want to retain your own state.
     */
    @java.lang.Override
    public final java.lang.Object onRetainNonConfigurationInstance() {
        if (mStopped) {
            doReallyStop(true);
        }
        java.lang.Object custom = onRetainCustomNonConfigurationInstance();
        android.support.v4.app.FragmentManagerNonConfig fragments = mFragments.retainNestedNonConfig();
        android.support.v4.util.SimpleArrayMap<java.lang.String, android.support.v4.app.LoaderManager> loaders = mFragments.retainLoaderNonConfig();
        if (((fragments == null) && (loaders == null)) && (custom == null)) {
            return null;
        }
        android.support.v4.app.FragmentActivity.NonConfigurationInstances nci = new android.support.v4.app.FragmentActivity.NonConfigurationInstances();
        nci.custom = custom;
        nci.fragments = fragments;
        nci.loaders = loaders;
        return nci;
    }

    /**
     * Save all appropriate fragment state.
     */
    @java.lang.Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        android.os.Parcelable p = mFragments.saveAllState();
        if (p != null) {
            outState.putParcelable(android.support.v4.app.FragmentActivity.FRAGMENTS_TAG, p);
        }
        if (mPendingFragmentActivityResults.size() > 0) {
            outState.putInt(android.support.v4.app.FragmentActivity.NEXT_CANDIDATE_REQUEST_INDEX_TAG, mNextCandidateRequestIndex);
            int[] requestCodes = new int[mPendingFragmentActivityResults.size()];
            java.lang.String[] fragmentWhos = new java.lang.String[mPendingFragmentActivityResults.size()];
            for (int i = 0; i < mPendingFragmentActivityResults.size(); i++) {
                requestCodes[i] = mPendingFragmentActivityResults.keyAt(i);
                fragmentWhos[i] = mPendingFragmentActivityResults.valueAt(i);
            }
            outState.putIntArray(android.support.v4.app.FragmentActivity.ALLOCATED_REQUEST_INDICIES_TAG, requestCodes);
            outState.putStringArray(android.support.v4.app.FragmentActivity.REQUEST_FRAGMENT_WHO_TAG, fragmentWhos);
        }
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @java.lang.Override
    protected void onStart() {
        super.onStart();
        mStopped = false;
        mReallyStopped = false;
        mHandler.removeMessages(android.support.v4.app.FragmentActivity.MSG_REALLY_STOPPED);
        if (!mCreated) {
            mCreated = true;
            mFragments.dispatchActivityCreated();
        }
        mFragments.noteStateNotSaved();
        mFragments.execPendingActions();
        mFragments.doLoaderStart();
        // NOTE: HC onStart goes here.
        mFragments.dispatchStart();
        mFragments.reportLoaderStart();
    }

    /**
     * Dispatch onStop() to all fragments.  Ensure all loaders are stopped.
     */
    @java.lang.Override
    protected void onStop() {
        super.onStop();
        mStopped = true;
        mHandler.sendEmptyMessage(android.support.v4.app.FragmentActivity.MSG_REALLY_STOPPED);
        mFragments.dispatchStop();
    }

    // ------------------------------------------------------------------------
    // NEW METHODS
    // ------------------------------------------------------------------------
    /**
     * Use this instead of {@link #onRetainNonConfigurationInstance()}.
     * Retrieve later with {@link #getLastCustomNonConfigurationInstance()}.
     */
    public java.lang.Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    /**
     * Return the value previously returned from
     * {@link #onRetainCustomNonConfigurationInstance()}.
     */
    @java.lang.SuppressWarnings("deprecation")
    public java.lang.Object getLastCustomNonConfigurationInstance() {
        android.support.v4.app.FragmentActivity.NonConfigurationInstances nc = ((android.support.v4.app.FragmentActivity.NonConfigurationInstances) (getLastNonConfigurationInstance()));
        return nc != null ? nc.custom : null;
    }

    /**
     * Support library version of {@link Activity#invalidateOptionsMenu}.
     *
     * <p>Invalidate the activity's options menu. This will cause relevant presentations
     * of the menu to fully update via calls to onCreateOptionsMenu and
     * onPrepareOptionsMenu the next time the menu is requested.
     */
    public void supportInvalidateOptionsMenu() {
        if (android.os.Build.VERSION.SDK_INT >= android.support.v4.app.FragmentActivity.HONEYCOMB) {
            // If we are running on HC or greater, we can use the framework
            // API to invalidate the options menu.
            android.support.v4.app.ActivityCompatHoneycomb.invalidateOptionsMenu(this);
            return;
        }
        // Whoops, older platform...  we'll use a hack, to manually rebuild
        // the options menu the next time it is prepared.
        mOptionsMenuInvalidated = true;
    }

    /**
     * Print the Activity's state into the given stream.  This gets invoked if
     * you run "adb shell dumpsys activity <activity_component_name>".
     *
     * @param prefix
     * 		Desired prefix to prepend at each line of output.
     * @param fd
     * 		The raw file descriptor that the dump is being sent to.
     * @param writer
     * 		The PrintWriter to which you should dump your state.  This will be
     * 		closed for you after you return.
     * @param args
     * 		additional arguments to the dump request.
     */
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (android.os.Build.VERSION.SDK_INT >= android.support.v4.app.FragmentActivity.HONEYCOMB) {
            // XXX This can only work if we can call the super-class impl. :/
            // ActivityCompatHoneycomb.dump(this, prefix, fd, writer, args);
        }
        writer.print(prefix);
        writer.print("Local FragmentActivity ");
        writer.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        writer.println(" State:");
        java.lang.String innerPrefix = prefix + "  ";
        writer.print(innerPrefix);
        writer.print("mCreated=");
        writer.print(mCreated);
        writer.print("mResumed=");
        writer.print(mResumed);
        writer.print(" mStopped=");
        writer.print(mStopped);
        writer.print(" mReallyStopped=");
        writer.println(mReallyStopped);
        mFragments.dumpLoaders(innerPrefix, fd, writer, args);
        mFragments.getSupportFragmentManager().dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.println("View Hierarchy:");
        dumpViewHierarchy(prefix + "  ", writer, getWindow().getDecorView());
    }

    private static java.lang.String viewToString(android.view.View view) {
        java.lang.StringBuilder out = new java.lang.StringBuilder(128);
        out.append(view.getClass().getName());
        out.append('{');
        out.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(view)));
        out.append(' ');
        switch (view.getVisibility()) {
            case android.view.View.VISIBLE :
                out.append('V');
                break;
            case android.view.View.INVISIBLE :
                out.append('I');
                break;
            case android.view.View.GONE :
                out.append('G');
                break;
            default :
                out.append('.');
                break;
        }
        out.append(view.isFocusable() ? 'F' : '.');
        out.append(view.isEnabled() ? 'E' : '.');
        out.append(view.willNotDraw() ? '.' : 'D');
        out.append(view.isHorizontalScrollBarEnabled() ? 'H' : '.');
        out.append(view.isVerticalScrollBarEnabled() ? 'V' : '.');
        out.append(view.isClickable() ? 'C' : '.');
        out.append(view.isLongClickable() ? 'L' : '.');
        out.append(' ');
        out.append(view.isFocused() ? 'F' : '.');
        out.append(view.isSelected() ? 'S' : '.');
        out.append(view.isPressed() ? 'P' : '.');
        out.append(' ');
        out.append(view.getLeft());
        out.append(',');
        out.append(view.getTop());
        out.append('-');
        out.append(view.getRight());
        out.append(',');
        out.append(view.getBottom());
        final int id = view.getId();
        if (id != android.view.View.NO_ID) {
            out.append(" #");
            out.append(java.lang.Integer.toHexString(id));
            final android.content.res.Resources r = view.getResources();
            if ((id != 0) && (r != null)) {
                try {
                    java.lang.String pkgname;
                    switch (id & 0xff000000) {
                        case 0x7f000000 :
                            pkgname = "app";
                            break;
                        case 0x1000000 :
                            pkgname = "android";
                            break;
                        default :
                            pkgname = r.getResourcePackageName(id);
                            break;
                    }
                    java.lang.String typename = r.getResourceTypeName(id);
                    java.lang.String entryname = r.getResourceEntryName(id);
                    out.append(" ");
                    out.append(pkgname);
                    out.append(":");
                    out.append(typename);
                    out.append("/");
                    out.append(entryname);
                } catch (android.content.res.Resources.NotFoundException e) {
                }
            }
        }
        out.append("}");
        return out.toString();
    }

    private void dumpViewHierarchy(java.lang.String prefix, java.io.PrintWriter writer, android.view.View view) {
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
            return;
        }
        writer.println(android.support.v4.app.FragmentActivity.viewToString(view));
        if (!(view instanceof android.view.ViewGroup)) {
            return;
        }
        android.view.ViewGroup grp = ((android.view.ViewGroup) (view));
        final int N = grp.getChildCount();
        if (N <= 0) {
            return;
        }
        prefix = prefix + "  ";
        for (int i = 0; i < N; i++) {
            dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
        }
    }

    void doReallyStop(boolean retaining) {
        if (!mReallyStopped) {
            mReallyStopped = true;
            mRetaining = retaining;
            mHandler.removeMessages(android.support.v4.app.FragmentActivity.MSG_REALLY_STOPPED);
            onReallyStop();
        } else
            if (retaining) {
                // We're already really stopped, but we've been asked to retain.
                // Our fragments are taken care of but we need to mark the loaders for retention.
                // In order to do this correctly we need to restart the loaders first before
                // handing them off to the next activity.
                mFragments.doLoaderStart();
                mFragments.doLoaderStop(true);
            }

    }

    /**
     * Pre-HC, we didn't have a way to determine whether an activity was
     * being stopped for a config change or not until we saw
     * onRetainNonConfigurationInstance() called after onStop().  However
     * we need to know this, to know whether to retain fragments.  This will
     * tell us what we need to know.
     */
    void onReallyStop() {
        mFragments.doLoaderStop(mRetaining);
        mFragments.dispatchReallyStop();
    }

    // ------------------------------------------------------------------------
    // FRAGMENT SUPPORT
    // ------------------------------------------------------------------------
    /**
     * Called when a fragment is attached to the activity.
     *
     * <p>This is called after the attached fragment's <code>onAttach</code> and before
     * the attached fragment's <code>onCreate</code> if the fragment has not yet had a previous
     * call to <code>onCreate</code>.</p>
     */
    @java.lang.SuppressWarnings("unused")
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
    }

    /**
     * Return the FragmentManager for interacting with fragments associated
     * with this activity.
     */
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return mFragments.getSupportFragmentManager();
    }

    public android.support.v4.app.LoaderManager getSupportLoaderManager() {
        return mFragments.getSupportLoaderManager();
    }

    /**
     * Modifies the standard behavior to allow results to be delivered to fragments.
     * This imposes a restriction that requestCode be <= 0xffff.
     */
    @java.lang.Override
    public void startActivityForResult(android.content.Intent intent, int requestCode) {
        // If this was started from a Fragment we've already checked the upper 16 bits were not in
        // use, and then repurposed them for the Fragment's index.
        if (!mStartedActivityFromFragment) {
            if (requestCode != (-1)) {
                android.support.v4.app.BaseFragmentActivityGingerbread.checkForValidRequestCode(requestCode);
            }
        }
        super.startActivityForResult(intent, requestCode);
    }

    @java.lang.Override
    public final void validateRequestPermissionsRequestCode(int requestCode) {
        // We use 16 bits of the request code to encode the fragment id when
        // requesting permissions from a fragment. Hence, requestPermissions()
        // should validate the code against that but we cannot override it as
        // we can not then call super and also the ActivityCompat would call
        // back to this override. To handle this we use dependency inversion
        // where we are the validator of request codes when requesting
        // permissions in ActivityCompat.
        if ((!mRequestedPermissionsFromFragment) && (requestCode != (-1))) {
            android.support.v4.app.BaseFragmentActivityGingerbread.checkForValidRequestCode(requestCode);
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode
     * 		The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions
     * 		The requested permissions. Never null.
     * @param grantResults
     * 		The grant results for the corresponding permissions
     * 		which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     * 		or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @java.lang.Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull
    java.lang.String[] permissions, @android.support.annotation.NonNull
    int[] grantResults) {
        int index = (requestCode >> 16) & 0xffff;
        if (index != 0) {
            index--;
            java.lang.String who = mPendingFragmentActivityResults.get(index);
            mPendingFragmentActivityResults.remove(index);
            if (who == null) {
                android.util.Log.w(android.support.v4.app.FragmentActivity.TAG, "Activity result delivered for unknown Fragment.");
                return;
            }
            android.support.v4.app.Fragment frag = mFragments.findFragmentByWho(who);
            if (frag == null) {
                android.util.Log.w(android.support.v4.app.FragmentActivity.TAG, "Activity result no fragment exists for who: " + who);
            } else {
                frag.onRequestPermissionsResult(requestCode & 0xffff, permissions, grantResults);
            }
        }
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior.
     */
    public void startActivityFromFragment(android.support.v4.app.Fragment fragment, android.content.Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, null);
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior.
     */
    public void startActivityFromFragment(android.support.v4.app.Fragment fragment, android.content.Intent intent, int requestCode, @android.support.annotation.Nullable
    android.os.Bundle options) {
        mStartedActivityFromFragment = true;
        try {
            if (requestCode == (-1)) {
                android.support.v4.app.ActivityCompat.startActivityForResult(this, intent, -1, options);
                return;
            }
            android.support.v4.app.BaseFragmentActivityGingerbread.checkForValidRequestCode(requestCode);
            int requestIndex = allocateRequestIndex(fragment);
            android.support.v4.app.ActivityCompat.startActivityForResult(this, intent, ((requestIndex + 1) << 16) + (requestCode & 0xffff), options);
        } finally {
            mStartedActivityFromFragment = false;
        }
    }

    /**
     * Called by Fragment.startIntentSenderForResult() to implement its behavior.
     */
    public void startIntentSenderFromFragment(android.support.v4.app.Fragment fragment, android.content.IntentSender intent, int requestCode, @android.support.annotation.Nullable
    android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
        mStartedIntentSenderFromFragment = true;
        try {
            if (requestCode == (-1)) {
                android.support.v4.app.ActivityCompat.startIntentSenderForResult(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
                return;
            }
            android.support.v4.app.BaseFragmentActivityGingerbread.checkForValidRequestCode(requestCode);
            int requestIndex = allocateRequestIndex(fragment);
            android.support.v4.app.ActivityCompat.startIntentSenderForResult(this, intent, ((requestIndex + 1) << 16) + (requestCode & 0xffff), fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } finally {
            mStartedIntentSenderFromFragment = false;
        }
    }

    // Allocates the next available startActivityForResult request index.
    private int allocateRequestIndex(android.support.v4.app.Fragment fragment) {
        // Sanity check that we havn't exhaused the request index space.
        if (mPendingFragmentActivityResults.size() >= android.support.v4.app.FragmentActivity.MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS) {
            throw new java.lang.IllegalStateException("Too many pending Fragment activity results.");
        }
        // Find an unallocated request index in the mPendingFragmentActivityResults map.
        while (mPendingFragmentActivityResults.indexOfKey(mNextCandidateRequestIndex) >= 0) {
            mNextCandidateRequestIndex = (mNextCandidateRequestIndex + 1) % android.support.v4.app.FragmentActivity.MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS;
        } 
        int requestIndex = mNextCandidateRequestIndex;
        mPendingFragmentActivityResults.put(requestIndex, fragment.mWho);
        mNextCandidateRequestIndex = (mNextCandidateRequestIndex + 1) % android.support.v4.app.FragmentActivity.MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS;
        return requestIndex;
    }

    /**
     * Called by Fragment.requestPermissions() to implement its behavior.
     */
    void requestPermissionsFromFragment(android.support.v4.app.Fragment fragment, java.lang.String[] permissions, int requestCode) {
        if (requestCode == (-1)) {
            android.support.v4.app.ActivityCompat.requestPermissions(this, permissions, requestCode);
            return;
        }
        android.support.v4.app.BaseFragmentActivityGingerbread.checkForValidRequestCode(requestCode);
        try {
            mRequestedPermissionsFromFragment = true;
            int requestIndex = allocateRequestIndex(fragment);
            android.support.v4.app.ActivityCompat.requestPermissions(this, permissions, ((requestIndex + 1) << 16) + (requestCode & 0xffff));
        } finally {
            mRequestedPermissionsFromFragment = false;
        }
    }

    class HostCallbacks extends android.support.v4.app.FragmentHostCallback<android.support.v4.app.FragmentActivity> {
        public HostCallbacks() {
            /* fragmentActivity */
            super(android.support.v4.app.FragmentActivity.this);
        }

        @java.lang.Override
        public void onDump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            android.support.v4.app.FragmentActivity.this.dump(prefix, fd, writer, args);
        }

        @java.lang.Override
        public boolean onShouldSaveFragmentState(android.support.v4.app.Fragment fragment) {
            return !isFinishing();
        }

        @java.lang.Override
        public android.view.LayoutInflater onGetLayoutInflater() {
            return android.support.v4.app.FragmentActivity.this.getLayoutInflater().cloneInContext(android.support.v4.app.FragmentActivity.this);
        }

        @java.lang.Override
        public android.support.v4.app.FragmentActivity onGetHost() {
            return android.support.v4.app.FragmentActivity.this;
        }

        @java.lang.Override
        public void onSupportInvalidateOptionsMenu() {
            android.support.v4.app.FragmentActivity.this.supportInvalidateOptionsMenu();
        }

        @java.lang.Override
        public void onStartActivityFromFragment(android.support.v4.app.Fragment fragment, android.content.Intent intent, int requestCode) {
            android.support.v4.app.FragmentActivity.this.startActivityFromFragment(fragment, intent, requestCode);
        }

        @java.lang.Override
        public void onStartActivityFromFragment(android.support.v4.app.Fragment fragment, android.content.Intent intent, int requestCode, @android.support.annotation.Nullable
        android.os.Bundle options) {
            android.support.v4.app.FragmentActivity.this.startActivityFromFragment(fragment, intent, requestCode, options);
        }

        @java.lang.Override
        public void onStartIntentSenderFromFragment(android.support.v4.app.Fragment fragment, android.content.IntentSender intent, int requestCode, @android.support.annotation.Nullable
        android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
            android.support.v4.app.FragmentActivity.this.startIntentSenderFromFragment(fragment, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        }

        @java.lang.Override
        public void onRequestPermissionsFromFragment(@android.support.annotation.NonNull
        android.support.v4.app.Fragment fragment, @android.support.annotation.NonNull
        java.lang.String[] permissions, int requestCode) {
            android.support.v4.app.FragmentActivity.this.requestPermissionsFromFragment(fragment, permissions, requestCode);
        }

        @java.lang.Override
        public boolean onShouldShowRequestPermissionRationale(@android.support.annotation.NonNull
        java.lang.String permission) {
            return android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(android.support.v4.app.FragmentActivity.this, permission);
        }

        @java.lang.Override
        public boolean onHasWindowAnimations() {
            return getWindow() != null;
        }

        @java.lang.Override
        public int onGetWindowAnimations() {
            final android.view.Window w = getWindow();
            return w == null ? 0 : w.getAttributes().windowAnimations;
        }

        @java.lang.Override
        public void onAttachFragment(android.support.v4.app.Fragment fragment) {
            android.support.v4.app.FragmentActivity.this.onAttachFragment(fragment);
        }

        @android.support.annotation.Nullable
        @java.lang.Override
        public android.view.View onFindViewById(int id) {
            return android.support.v4.app.FragmentActivity.this.findViewById(id);
        }

        @java.lang.Override
        public boolean onHasView() {
            final android.view.Window w = getWindow();
            return (w != null) && (w.peekDecorView() != null);
        }
    }
}

