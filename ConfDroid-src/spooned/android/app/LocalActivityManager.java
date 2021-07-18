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
 * <p>Helper class for managing multiple running embedded activities in the same
 * process. This class is not normally used directly, but rather created for
 * you as part of the {@link android.app.ActivityGroup} implementation.
 *
 * @see ActivityGroup
 * @deprecated Use the new {@link Fragment} and {@link FragmentManager} APIs
instead; these are also
available on older platforms through the Android compatibility package.
 */
@java.lang.Deprecated
public class LocalActivityManager {
    private static final java.lang.String TAG = "LocalActivityManager";

    private static final boolean localLOGV = false;

    // Internal token for an Activity being managed by LocalActivityManager.
    private static class LocalActivityRecord extends android.os.Binder {
        LocalActivityRecord(java.lang.String _id, android.content.Intent _intent) {
            id = _id;
            intent = _intent;
        }

        final java.lang.String id;// Unique name of this record.


        android.content.Intent intent;// Which activity to run here.


        android.content.pm.ActivityInfo activityInfo;// Package manager info about activity.


        android.app.Activity activity;// Currently instantiated activity.


        android.view.Window window;// Activity's top-level window.


        android.os.Bundle instanceState;// Last retrieved freeze state.


        int curState = android.app.LocalActivityManager.RESTORED;// Current state the activity is in.

    }

    static final int RESTORED = 0;// State restored, but no startActivity().


    static final int INITIALIZING = 1;// Ready to launch (after startActivity()).


    static final int CREATED = 2;// Created, not started or resumed.


    static final int STARTED = 3;// Created and started, not resumed.


    static final int RESUMED = 4;// Created started and resumed.


    static final int DESTROYED = 5;// No longer with us.


    /**
     * Thread our activities are running in.
     */
    private final android.app.ActivityThread mActivityThread;

    /**
     * The containing activity that owns the activities we create.
     */
    private final android.app.Activity mParent;

    /**
     * The activity that is currently resumed.
     */
    private android.app.LocalActivityManager.LocalActivityRecord mResumed;

    /**
     * id -> record of all known activities.
     */
    private final java.util.Map<java.lang.String, android.app.LocalActivityManager.LocalActivityRecord> mActivities = new java.util.HashMap<java.lang.String, android.app.LocalActivityManager.LocalActivityRecord>();

    /**
     * array of all known activities for easy iterating.
     */
    private final java.util.ArrayList<android.app.LocalActivityManager.LocalActivityRecord> mActivityArray = new java.util.ArrayList<android.app.LocalActivityManager.LocalActivityRecord>();

    /**
     * True if only one activity can be resumed at a time
     */
    private boolean mSingleMode;

    /**
     * Set to true once we find out the container is finishing.
     */
    private boolean mFinishing;

    /**
     * Current state the owner (ActivityGroup) is in
     */
    private int mCurState = android.app.LocalActivityManager.INITIALIZING;

    /**
     * String ids of running activities starting with least recently used.
     */
    // TODO: put back in stopping of activities.
    // private List<LocalActivityRecord>  mLRU = new ArrayList();
    /**
     * Create a new LocalActivityManager for holding activities running within
     * the given <var>parent</var>.
     *
     * @param parent
     * 		the host of the embedded activities
     * @param singleMode
     * 		True if the LocalActivityManger should keep a maximum
     * 		of one activity resumed
     */
    public LocalActivityManager(android.app.Activity parent, boolean singleMode) {
        mActivityThread = android.app.ActivityThread.currentActivityThread();
        mParent = parent;
        mSingleMode = singleMode;
    }

    private void moveToState(android.app.LocalActivityManager.LocalActivityRecord r, int desiredState) {
        if ((r.curState == android.app.LocalActivityManager.RESTORED) || (r.curState == android.app.LocalActivityManager.DESTROYED)) {
            // startActivity() has not yet been called, so nothing to do.
            return;
        }
        if (r.curState == android.app.LocalActivityManager.INITIALIZING) {
            // Get the lastNonConfigurationInstance for the activity
            java.util.HashMap<java.lang.String, java.lang.Object> lastNonConfigurationInstances = mParent.getLastNonConfigurationChildInstances();
            java.lang.Object instanceObj = null;
            if (lastNonConfigurationInstances != null) {
                instanceObj = lastNonConfigurationInstances.get(r.id);
            }
            android.app.Activity.NonConfigurationInstances instance = null;
            if (instanceObj != null) {
                instance = new android.app.Activity.NonConfigurationInstances();
                instance.activity = instanceObj;
            }
            // We need to have always created the activity.
            if (android.app.LocalActivityManager.localLOGV)
                android.util.Log.v(android.app.LocalActivityManager.TAG, (r.id + ": starting ") + r.intent);

            if (r.activityInfo == null) {
                r.activityInfo = mActivityThread.resolveActivityInfo(r.intent);
            }
            r.activity = mActivityThread.startActivityNow(mParent, r.id, r.intent, r.activityInfo, r, r.instanceState, instance);
            if (r.activity == null) {
                return;
            }
            r.window = r.activity.getWindow();
            r.instanceState = null;
            r.curState = android.app.LocalActivityManager.STARTED;
            if (desiredState == android.app.LocalActivityManager.RESUMED) {
                if (android.app.LocalActivityManager.localLOGV)
                    android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": resuming");

                mActivityThread.performResumeActivity(r, true, "moveToState-INITIALIZING");
                r.curState = android.app.LocalActivityManager.RESUMED;
            }
            // Don't do anything more here.  There is an important case:
            // if this is being done as part of onCreate() of the group, then
            // the launching of the activity gets its state a little ahead
            // of our own (it is now STARTED, while we are only CREATED).
            // If we just leave things as-is, we'll deal with it as the
            // group's state catches up.
            return;
        }
        switch (r.curState) {
            case android.app.LocalActivityManager.CREATED :
                if (desiredState == android.app.LocalActivityManager.STARTED) {
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": restarting");

                    mActivityThread.performRestartActivity(r);
                    r.curState = android.app.LocalActivityManager.STARTED;
                }
                if (desiredState == android.app.LocalActivityManager.RESUMED) {
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": restarting and resuming");

                    mActivityThread.performRestartActivity(r);
                    mActivityThread.performResumeActivity(r, true, "moveToState-CREATED");
                    r.curState = android.app.LocalActivityManager.RESUMED;
                }
                return;
            case android.app.LocalActivityManager.STARTED :
                if (desiredState == android.app.LocalActivityManager.RESUMED) {
                    // Need to resume it...
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": resuming");

                    mActivityThread.performResumeActivity(r, true, "moveToState-STARTED");
                    r.instanceState = null;
                    r.curState = android.app.LocalActivityManager.RESUMED;
                }
                if (desiredState == android.app.LocalActivityManager.CREATED) {
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": stopping");

                    mActivityThread.performStopActivity(r, false, "moveToState-STARTED");
                    r.curState = android.app.LocalActivityManager.CREATED;
                }
                return;
            case android.app.LocalActivityManager.RESUMED :
                if (desiredState == android.app.LocalActivityManager.STARTED) {
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": pausing");

                    performPause(r, mFinishing);
                    r.curState = android.app.LocalActivityManager.STARTED;
                }
                if (desiredState == android.app.LocalActivityManager.CREATED) {
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": pausing");

                    performPause(r, mFinishing);
                    if (android.app.LocalActivityManager.localLOGV)
                        android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": stopping");

                    mActivityThread.performStopActivity(r, false, "moveToState-RESUMED");
                    r.curState = android.app.LocalActivityManager.CREATED;
                }
                return;
        }
    }

    private void performPause(android.app.LocalActivityManager.LocalActivityRecord r, boolean finishing) {
        final boolean needState = r.instanceState == null;
        final android.os.Bundle instanceState = mActivityThread.performPauseActivity(r, finishing, needState, "performPause");
        if (needState) {
            r.instanceState = instanceState;
        }
    }

    /**
     * Start a new activity running in the group.  Every activity you start
     * must have a unique string ID associated with it -- this is used to keep
     * track of the activity, so that if you later call startActivity() again
     * on it the same activity object will be retained.
     *
     * <p>When there had previously been an activity started under this id,
     * it may either be destroyed and a new one started, or the current
     * one re-used, based on these conditions, in order:</p>
     *
     * <ul>
     * <li> If the Intent maps to a different activity component than is
     * currently running, the current activity is finished and a new one
     * started.
     * <li> If the current activity uses a non-multiple launch mode (such
     * as singleTop), or the Intent has the
     * {@link Intent#FLAG_ACTIVITY_SINGLE_TOP} flag set, then the current
     * activity will remain running and its
     * {@link Activity#onNewIntent(Intent) Activity.onNewIntent()} method
     * called.
     * <li> If the new Intent is the same (excluding extras) as the previous
     * one, and the new Intent does not have the
     * {@link Intent#FLAG_ACTIVITY_CLEAR_TOP} set, then the current activity
     * will remain running as-is.
     * <li> Otherwise, the current activity will be finished and a new
     * one started.
     * </ul>
     *
     * <p>If the given Intent can not be resolved to an available Activity,
     * this method throws {@link android.content.ActivityNotFoundException}.
     *
     * <p>Warning: There is an issue where, if the Intent does not
     * include an explicit component, we can restore the state for a different
     * activity class than was previously running when the state was saved (if
     * the set of available activities changes between those points).
     *
     * @param id
     * 		Unique identifier of the activity to be started
     * @param intent
     * 		The Intent describing the activity to be started
     * @return Returns the window of the activity.  The caller needs to take
    care of adding this window to a view hierarchy, and likewise dealing
    with removing the old window if the activity has changed.
     * @throws android.content.ActivityNotFoundException
     * 		
     */
    public android.view.Window startActivity(java.lang.String id, android.content.Intent intent) {
        if (mCurState == android.app.LocalActivityManager.INITIALIZING) {
            throw new java.lang.IllegalStateException("Activities can't be added until the containing group has been created.");
        }
        boolean adding = false;
        boolean sameIntent = false;
        android.content.pm.ActivityInfo aInfo = null;
        // Already have information about the new activity id?
        android.app.LocalActivityManager.LocalActivityRecord r = mActivities.get(id);
        if (r == null) {
            // Need to create it...
            r = new android.app.LocalActivityManager.LocalActivityRecord(id, intent);
            adding = true;
        } else
            if (r.intent != null) {
                sameIntent = r.intent.filterEquals(intent);
                if (sameIntent) {
                    // We are starting the same activity.
                    aInfo = r.activityInfo;
                }
            }

        if (aInfo == null) {
            aInfo = mActivityThread.resolveActivityInfo(intent);
        }
        // Pause the currently running activity if there is one and only a single
        // activity is allowed to be running at a time.
        if (mSingleMode) {
            android.app.LocalActivityManager.LocalActivityRecord old = mResumed;
            // If there was a previous activity, and it is not the current
            // activity, we need to stop it.
            if (((old != null) && (old != r)) && (mCurState == android.app.LocalActivityManager.RESUMED)) {
                moveToState(old, android.app.LocalActivityManager.STARTED);
            }
        }
        if (adding) {
            // It's a brand new world.
            mActivities.put(id, r);
            mActivityArray.add(r);
        } else
            if (r.activityInfo != null) {
                // If the new activity is the same as the current one, then
                // we may be able to reuse it.
                if ((aInfo == r.activityInfo) || (aInfo.name.equals(r.activityInfo.name) && aInfo.packageName.equals(r.activityInfo.packageName))) {
                    if ((aInfo.launchMode != android.content.pm.ActivityInfo.LAUNCH_MULTIPLE) || ((intent.getFlags() & android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP) != 0)) {
                        // The activity wants onNewIntent() called.
                        java.util.ArrayList<com.android.internal.content.ReferrerIntent> intents = new java.util.ArrayList<>(1);
                        intents.add(new com.android.internal.content.ReferrerIntent(intent, mParent.getPackageName()));
                        if (android.app.LocalActivityManager.localLOGV)
                            android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": new intent");

                        /* andPause */
                        mActivityThread.performNewIntents(r, intents, false);
                        r.intent = intent;
                        moveToState(r, mCurState);
                        if (mSingleMode) {
                            mResumed = r;
                        }
                        return r.window;
                    }
                    if (sameIntent && ((intent.getFlags() & android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP) == 0)) {
                        // We are showing the same thing, so this activity is
                        // just resumed and stays as-is.
                        r.intent = intent;
                        moveToState(r, mCurState);
                        if (mSingleMode) {
                            mResumed = r;
                        }
                        return r.window;
                    }
                }
                // The new activity is different than the current one, or it
                // is a multiple launch activity, so we need to destroy what
                // is currently there.
                performDestroy(r, true);
            }

        r.intent = intent;
        r.curState = android.app.LocalActivityManager.INITIALIZING;
        r.activityInfo = aInfo;
        moveToState(r, mCurState);
        // When in single mode keep track of the current activity
        if (mSingleMode) {
            mResumed = r;
        }
        return r.window;
    }

    private android.view.Window performDestroy(android.app.LocalActivityManager.LocalActivityRecord r, boolean finish) {
        android.view.Window win;
        win = r.window;
        if ((r.curState == android.app.LocalActivityManager.RESUMED) && (!finish)) {
            performPause(r, finish);
        }
        if (android.app.LocalActivityManager.localLOGV)
            android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": destroying");

        mActivityThread.performDestroyActivity(r, finish);
        r.activity = null;
        r.window = null;
        if (finish) {
            r.instanceState = null;
        }
        r.curState = android.app.LocalActivityManager.DESTROYED;
        return win;
    }

    /**
     * Destroy the activity associated with a particular id.  This activity
     * will go through the normal lifecycle events and fine onDestroy(), and
     * then the id removed from the group.
     *
     * @param id
     * 		Unique identifier of the activity to be destroyed
     * @param finish
     * 		If true, this activity will be finished, so its id and
     * 		all state are removed from the group.
     * @return Returns the window that was used to display the activity, or
    null if there was none.
     */
    public android.view.Window destroyActivity(java.lang.String id, boolean finish) {
        android.app.LocalActivityManager.LocalActivityRecord r = mActivities.get(id);
        android.view.Window win = null;
        if (r != null) {
            win = performDestroy(r, finish);
            if (finish) {
                mActivities.remove(id);
                mActivityArray.remove(r);
            }
        }
        return win;
    }

    /**
     * Retrieve the Activity that is currently running.
     *
     * @return the currently running (resumed) Activity, or null if there is
    not one
     * @see #startActivity
     * @see #getCurrentId
     */
    public android.app.Activity getCurrentActivity() {
        return mResumed != null ? mResumed.activity : null;
    }

    /**
     * Retrieve the ID of the activity that is currently running.
     *
     * @return the ID of the currently running (resumed) Activity, or null if
    there is not one
     * @see #startActivity
     * @see #getCurrentActivity
     */
    public java.lang.String getCurrentId() {
        return mResumed != null ? mResumed.id : null;
    }

    /**
     * Return the Activity object associated with a string ID.
     *
     * @see #startActivity
     * @return the associated Activity object, or null if the id is unknown or
    its activity is not currently instantiated
     */
    public android.app.Activity getActivity(java.lang.String id) {
        android.app.LocalActivityManager.LocalActivityRecord r = mActivities.get(id);
        return r != null ? r.activity : null;
    }

    /**
     * Restore a state that was previously returned by {@link #saveInstanceState}.  This
     * adds to the activity group information about all activity IDs that had
     * previously been saved, even if they have not been started yet, so if the
     * user later navigates to them the correct state will be restored.
     *
     * <p>Note: This does <b>not</b> change the current running activity, or
     * start whatever activity was previously running when the state was saved.
     * That is up to the client to do, in whatever way it thinks is best.
     *
     * @param state
     * 		a previously saved state; does nothing if this is null
     * @see #saveInstanceState
     */
    public void dispatchCreate(android.os.Bundle state) {
        if (state != null) {
            for (java.lang.String id : state.keySet()) {
                try {
                    final android.os.Bundle astate = state.getBundle(id);
                    android.app.LocalActivityManager.LocalActivityRecord r = mActivities.get(id);
                    if (r != null) {
                        r.instanceState = astate;
                    } else {
                        r = new android.app.LocalActivityManager.LocalActivityRecord(id, null);
                        r.instanceState = astate;
                        mActivities.put(id, r);
                        mActivityArray.add(r);
                    }
                } catch (java.lang.Exception e) {
                    // Recover from -all- app errors.
                    android.util.Log.e(android.app.LocalActivityManager.TAG, "Exception thrown when restoring LocalActivityManager state", e);
                }
            }
        }
        mCurState = android.app.LocalActivityManager.CREATED;
    }

    /**
     * Retrieve the state of all activities known by the group.  For
     * activities that have previously run and are now stopped or finished, the
     * last saved state is used.  For the current running activity, its
     * {@link Activity#onSaveInstanceState} is called to retrieve its current state.
     *
     * @return a Bundle holding the newly created state of all known activities
     * @see #dispatchCreate
     */
    public android.os.Bundle saveInstanceState() {
        android.os.Bundle state = null;
        // FIXME: child activities will freeze as part of onPaused. Do we
        // need to do this here?
        final int N = mActivityArray.size();
        for (int i = 0; i < N; i++) {
            final android.app.LocalActivityManager.LocalActivityRecord r = mActivityArray.get(i);
            if (state == null) {
                state = new android.os.Bundle();
            }
            if (((r.instanceState != null) || (r.curState == android.app.LocalActivityManager.RESUMED)) && (r.activity != null)) {
                // We need to save the state now, if we don't currently
                // already have it or the activity is currently resumed.
                final android.os.Bundle childState = new android.os.Bundle();
                r.activity.performSaveInstanceState(childState);
                r.instanceState = childState;
            }
            if (r.instanceState != null) {
                state.putBundle(r.id, r.instanceState);
            }
        }
        return state;
    }

    /**
     * Called by the container activity in its {@link Activity#onResume} so
     * that LocalActivityManager can perform the corresponding action on the
     * activities it holds.
     *
     * @see Activity#onResume
     */
    public void dispatchResume() {
        mCurState = android.app.LocalActivityManager.RESUMED;
        if (mSingleMode) {
            if (mResumed != null) {
                moveToState(mResumed, android.app.LocalActivityManager.RESUMED);
            }
        } else {
            final int N = mActivityArray.size();
            for (int i = 0; i < N; i++) {
                moveToState(mActivityArray.get(i), android.app.LocalActivityManager.RESUMED);
            }
        }
    }

    /**
     * Called by the container activity in its {@link Activity#onPause} so
     * that LocalActivityManager can perform the corresponding action on the
     * activities it holds.
     *
     * @param finishing
     * 		set to true if the parent activity has been finished;
     * 		this can be determined by calling
     * 		Activity.isFinishing()
     * @see Activity#onPause
     * @see Activity#isFinishing
     */
    public void dispatchPause(boolean finishing) {
        if (finishing) {
            mFinishing = true;
        }
        mCurState = android.app.LocalActivityManager.STARTED;
        if (mSingleMode) {
            if (mResumed != null) {
                moveToState(mResumed, android.app.LocalActivityManager.STARTED);
            }
        } else {
            final int N = mActivityArray.size();
            for (int i = 0; i < N; i++) {
                android.app.LocalActivityManager.LocalActivityRecord r = mActivityArray.get(i);
                if (r.curState == android.app.LocalActivityManager.RESUMED) {
                    moveToState(r, android.app.LocalActivityManager.STARTED);
                }
            }
        }
    }

    /**
     * Called by the container activity in its {@link Activity#onStop} so
     * that LocalActivityManager can perform the corresponding action on the
     * activities it holds.
     *
     * @see Activity#onStop
     */
    public void dispatchStop() {
        mCurState = android.app.LocalActivityManager.CREATED;
        final int N = mActivityArray.size();
        for (int i = 0; i < N; i++) {
            android.app.LocalActivityManager.LocalActivityRecord r = mActivityArray.get(i);
            moveToState(r, android.app.LocalActivityManager.CREATED);
        }
    }

    /**
     * Call onRetainNonConfigurationInstance on each child activity and store the
     * results in a HashMap by id.  Only construct the HashMap if there is a non-null
     * object to store.  Note that this does not support nested ActivityGroups.
     *
     * {@hide }
     */
    public java.util.HashMap<java.lang.String, java.lang.Object> dispatchRetainNonConfigurationInstance() {
        java.util.HashMap<java.lang.String, java.lang.Object> instanceMap = null;
        final int N = mActivityArray.size();
        for (int i = 0; i < N; i++) {
            android.app.LocalActivityManager.LocalActivityRecord r = mActivityArray.get(i);
            if ((r != null) && (r.activity != null)) {
                java.lang.Object instance = r.activity.onRetainNonConfigurationInstance();
                if (instance != null) {
                    if (instanceMap == null) {
                        instanceMap = new java.util.HashMap<java.lang.String, java.lang.Object>();
                    }
                    instanceMap.put(r.id, instance);
                }
            }
        }
        return instanceMap;
    }

    /**
     * Remove all activities from this LocalActivityManager, performing an
     * {@link Activity#onDestroy} on any that are currently instantiated.
     */
    public void removeAllActivities() {
        dispatchDestroy(true);
    }

    /**
     * Called by the container activity in its {@link Activity#onDestroy} so
     * that LocalActivityManager can perform the corresponding action on the
     * activities it holds.
     *
     * @see Activity#onDestroy
     */
    public void dispatchDestroy(boolean finishing) {
        final int N = mActivityArray.size();
        for (int i = 0; i < N; i++) {
            android.app.LocalActivityManager.LocalActivityRecord r = mActivityArray.get(i);
            if (android.app.LocalActivityManager.localLOGV)
                android.util.Log.v(android.app.LocalActivityManager.TAG, r.id + ": destroying");

            mActivityThread.performDestroyActivity(r, finishing);
        }
        mActivities.clear();
        mActivityArray.clear();
    }
}

