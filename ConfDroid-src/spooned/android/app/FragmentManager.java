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
 * Interface for interacting with {@link Fragment} objects inside of an
 * {@link Activity}
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using fragments, read the
 * <a href="{@docRoot }guide/components/fragments.html">Fragments</a> developer guide.</p>
 * </div>
 *
 * While the FragmentManager API was introduced in
 * {@link android.os.Build.VERSION_CODES#HONEYCOMB}, a version of the API
 * at is also available for use on older platforms through
 * {@link android.support.v4.app.FragmentActivity}.  See the blog post
 * <a href="http://android-developers.blogspot.com/2011/03/fragments-for-all.html">
 * Fragments For All</a> for more details.
 */
public abstract class FragmentManager {
    /**
     * Representation of an entry on the fragment back stack, as created
     * with {@link FragmentTransaction#addToBackStack(String)
     * FragmentTransaction.addToBackStack()}.  Entries can later be
     * retrieved with {@link FragmentManager#getBackStackEntryAt(int)
     * FragmentManager.getBackStackEntryAt()}.
     *
     * <p>Note that you should never hold on to a BackStackEntry object;
     * the identifier as returned by {@link #getId} is the only thing that
     * will be persisted across activity instances.
     */
    public interface BackStackEntry {
        /**
         * Return the unique identifier for the entry.  This is the only
         * representation of the entry that will persist across activity
         * instances.
         */
        public int getId();

        /**
         * Get the name that was supplied to
         * {@link FragmentTransaction#addToBackStack(String)
         * FragmentTransaction.addToBackStack(String)} when creating this entry.
         */
        public java.lang.String getName();

        /**
         * Return the full bread crumb title resource identifier for the entry,
         * or 0 if it does not have one.
         */
        public int getBreadCrumbTitleRes();

        /**
         * Return the short bread crumb title resource identifier for the entry,
         * or 0 if it does not have one.
         */
        public int getBreadCrumbShortTitleRes();

        /**
         * Return the full bread crumb title for the entry, or null if it
         * does not have one.
         */
        public java.lang.CharSequence getBreadCrumbTitle();

        /**
         * Return the short bread crumb title for the entry, or null if it
         * does not have one.
         */
        public java.lang.CharSequence getBreadCrumbShortTitle();
    }

    /**
     * Interface to watch for changes to the back stack.
     */
    public interface OnBackStackChangedListener {
        /**
         * Called whenever the contents of the back stack change.
         */
        public void onBackStackChanged();
    }

    /**
     * Start a series of edit operations on the Fragments associated with
     * this FragmentManager.
     *
     * <p>Note: A fragment transaction can only be created/committed prior
     * to an activity saving its state.  If you try to commit a transaction
     * after {@link Activity#onSaveInstanceState Activity.onSaveInstanceState()}
     * (and prior to a following {@link Activity#onStart Activity.onStart}
     * or {@link Activity#onResume Activity.onResume()}, you will get an error.
     * This is because the framework takes care of saving your current fragments
     * in the state, and if changes are made after the state is saved then they
     * will be lost.</p>
     */
    public abstract android.app.FragmentTransaction beginTransaction();

    /**
     *
     *
     * @unknown -- remove once prebuilts are in.
     */
    @java.lang.Deprecated
    public android.app.FragmentTransaction openTransaction() {
        return beginTransaction();
    }

    /**
     * After a {@link FragmentTransaction} is committed with
     * {@link FragmentTransaction#commit FragmentTransaction.commit()}, it
     * is scheduled to be executed asynchronously on the process's main thread.
     * If you want to immediately executing any such pending operations, you
     * can call this function (only from the main thread) to do so.  Note that
     * all callbacks and other related behavior will be done from within this
     * call, so be careful about where this is called from.
     *
     * @return Returns true if there were any pending transactions to be
    executed.
     */
    public abstract boolean executePendingTransactions();

    /**
     * Finds a fragment that was identified by the given id either when inflated
     * from XML or as the container ID when added in a transaction.  This first
     * searches through fragments that are currently added to the manager's
     * activity; if no such fragment is found, then all fragments currently
     * on the back stack associated with this ID are searched.
     *
     * @return The fragment if found or null otherwise.
     */
    public abstract android.app.Fragment findFragmentById(int id);

    /**
     * Finds a fragment that was identified by the given tag either when inflated
     * from XML or as supplied when added in a transaction.  This first
     * searches through fragments that are currently added to the manager's
     * activity; if no such fragment is found, then all fragments currently
     * on the back stack are searched.
     *
     * @return The fragment if found or null otherwise.
     */
    public abstract android.app.Fragment findFragmentByTag(java.lang.String tag);

    /**
     * Flag for {@link #popBackStack(String, int)}
     * and {@link #popBackStack(int, int)}: If set, and the name or ID of
     * a back stack entry has been supplied, then all matching entries will
     * be consumed until one that doesn't match is found or the bottom of
     * the stack is reached.  Otherwise, all entries up to but not including that entry
     * will be removed.
     */
    public static final int POP_BACK_STACK_INCLUSIVE = 1 << 0;

    /**
     * Pop the top state off the back stack.  This function is asynchronous -- it
     * enqueues the request to pop, but the action will not be performed until the
     * application returns to its event loop.
     */
    public abstract void popBackStack();

    /**
     * Like {@link #popBackStack()}, but performs the operation immediately
     * inside of the call.  This is like calling {@link #executePendingTransactions()}
     * afterwards.
     *
     * @return Returns true if there was something popped, else false.
     */
    public abstract boolean popBackStackImmediate();

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.  If there is nothing to pop, false is returned.
     * This function is asynchronous -- it enqueues the
     * request to pop, but the action will not be performed until the application
     * returns to its event loop.
     *
     * @param name
     * 		If non-null, this is the name of a previous back state
     * 		to look for; if found, all states up to that state will be popped.  The
     * 		{@link #POP_BACK_STACK_INCLUSIVE} flag can be used to control whether
     * 		the named state itself is popped. If null, only the top state is popped.
     * @param flags
     * 		Either 0 or {@link #POP_BACK_STACK_INCLUSIVE}.
     */
    public abstract void popBackStack(java.lang.String name, int flags);

    /**
     * Like {@link #popBackStack(String, int)}, but performs the operation immediately
     * inside of the call.  This is like calling {@link #executePendingTransactions()}
     * afterwards.
     *
     * @return Returns true if there was something popped, else false.
     */
    public abstract boolean popBackStackImmediate(java.lang.String name, int flags);

    /**
     * Pop all back stack states up to the one with the given identifier.
     * This function is asynchronous -- it enqueues the
     * request to pop, but the action will not be performed until the application
     * returns to its event loop.
     *
     * @param id
     * 		Identifier of the stated to be popped. If no identifier exists,
     * 		false is returned.
     * 		The identifier is the number returned by
     * 		{@link FragmentTransaction#commit() FragmentTransaction.commit()}.  The
     * 		{@link #POP_BACK_STACK_INCLUSIVE} flag can be used to control whether
     * 		the named state itself is popped.
     * @param flags
     * 		Either 0 or {@link #POP_BACK_STACK_INCLUSIVE}.
     */
    public abstract void popBackStack(int id, int flags);

    /**
     * Like {@link #popBackStack(int, int)}, but performs the operation immediately
     * inside of the call.  This is like calling {@link #executePendingTransactions()}
     * afterwards.
     *
     * @return Returns true if there was something popped, else false.
     */
    public abstract boolean popBackStackImmediate(int id, int flags);

    /**
     * Return the number of entries currently in the back stack.
     */
    public abstract int getBackStackEntryCount();

    /**
     * Return the BackStackEntry at index <var>index</var> in the back stack;
     * where the item on the bottom of the stack has index 0.
     */
    public abstract android.app.FragmentManager.BackStackEntry getBackStackEntryAt(int index);

    /**
     * Add a new listener for changes to the fragment back stack.
     */
    public abstract void addOnBackStackChangedListener(android.app.FragmentManager.OnBackStackChangedListener listener);

    /**
     * Remove a listener that was previously added with
     * {@link #addOnBackStackChangedListener(OnBackStackChangedListener)}.
     */
    public abstract void removeOnBackStackChangedListener(android.app.FragmentManager.OnBackStackChangedListener listener);

    /**
     * Put a reference to a fragment in a Bundle.  This Bundle can be
     * persisted as saved state, and when later restoring
     * {@link #getFragment(Bundle, String)} will return the current
     * instance of the same fragment.
     *
     * @param bundle
     * 		The bundle in which to put the fragment reference.
     * @param key
     * 		The name of the entry in the bundle.
     * @param fragment
     * 		The Fragment whose reference is to be stored.
     */
    public abstract void putFragment(android.os.Bundle bundle, java.lang.String key, android.app.Fragment fragment);

    /**
     * Retrieve the current Fragment instance for a reference previously
     * placed with {@link #putFragment(Bundle, String, Fragment)}.
     *
     * @param bundle
     * 		The bundle from which to retrieve the fragment reference.
     * @param key
     * 		The name of the entry in the bundle.
     * @return Returns the current Fragment instance that is associated with
    the given reference.
     */
    public abstract android.app.Fragment getFragment(android.os.Bundle bundle, java.lang.String key);

    /**
     * Save the current instance state of the given Fragment.  This can be
     * used later when creating a new instance of the Fragment and adding
     * it to the fragment manager, to have it create itself to match the
     * current state returned here.  Note that there are limits on how
     * this can be used:
     *
     * <ul>
     * <li>The Fragment must currently be attached to the FragmentManager.
     * <li>A new Fragment created using this saved state must be the same class
     * type as the Fragment it was created from.
     * <li>The saved state can not contain dependencies on other fragments --
     * that is it can't use {@link #putFragment(Bundle, String, Fragment)} to
     * store a fragment reference because that reference may not be valid when
     * this saved state is later used.  Likewise the Fragment's target and
     * result code are not included in this state.
     * </ul>
     *
     * @param f
     * 		The Fragment whose state is to be saved.
     * @return The generated state.  This will be null if there was no
    interesting state created by the fragment.
     */
    public abstract android.app.Fragment.SavedState saveFragmentInstanceState(android.app.Fragment f);

    /**
     * Returns true if the final {@link Activity#onDestroy() Activity.onDestroy()}
     * call has been made on the FragmentManager's Activity, so this instance is now dead.
     */
    public abstract boolean isDestroyed();

    /**
     * Print the FragmentManager's state into the given stream.
     *
     * @param prefix
     * 		Text to print at the front of each line.
     * @param fd
     * 		The raw file descriptor that the dump is being sent to.
     * @param writer
     * 		A PrintWriter to which the dump is to be set.
     * @param args
     * 		Additional arguments to the dump request.
     */
    public abstract void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args);

    /**
     * Control whether the framework's internal fragment manager debugging
     * logs are turned on.  If enabled, you will see output in logcat as
     * the framework performs fragment operations.
     */
    public static void enableDebugLogging(boolean enabled) {
        android.app.FragmentManagerImpl.DEBUG = enabled;
    }

    /**
     * Invalidate the attached activity's options menu as necessary.
     * This may end up being deferred until we move to the resumed state.
     */
    public void invalidateOptionsMenu() {
    }
}

