package android.app;


/**
 * API for performing a set of Fragment operations.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using fragments, read the
 * <a href="{@docRoot }guide/components/fragments.html">Fragments</a> developer
 * guide.</p>
 * </div>
 */
public abstract class FragmentTransaction {
    /**
     * Calls {@link #add(int, Fragment, String)} with a 0 containerViewId.
     */
    public abstract android.app.FragmentTransaction add(android.app.Fragment fragment, java.lang.String tag);

    /**
     * Calls {@link #add(int, Fragment, String)} with a null tag.
     */
    public abstract android.app.FragmentTransaction add(@android.annotation.IdRes
    int containerViewId, android.app.Fragment fragment);

    /**
     * Add a fragment to the activity state.  This fragment may optionally
     * also have its view (if {@link Fragment#onCreateView Fragment.onCreateView}
     * returns non-null) inserted into a container view of the activity.
     *
     * @param containerViewId
     * 		Optional identifier of the container this fragment is
     * 		to be placed in.  If 0, it will not be placed in a container.
     * @param fragment
     * 		The fragment to be added.  This fragment must not already
     * 		be added to the activity.
     * @param tag
     * 		Optional tag name for the fragment, to later retrieve the
     * 		fragment with {@link FragmentManager#findFragmentByTag(String)
     * 		FragmentManager.findFragmentByTag(String)}.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction add(@android.annotation.IdRes
    int containerViewId, android.app.Fragment fragment, java.lang.String tag);

    /**
     * Calls {@link #replace(int, Fragment, String)} with a null tag.
     */
    public abstract android.app.FragmentTransaction replace(@android.annotation.IdRes
    int containerViewId, android.app.Fragment fragment);

    /**
     * Replace an existing fragment that was added to a container.  This is
     * essentially the same as calling {@link #remove(Fragment)} for all
     * currently added fragments that were added with the same containerViewId
     * and then {@link #add(int, Fragment, String)} with the same arguments
     * given here.
     *
     * @param containerViewId
     * 		Identifier of the container whose fragment(s) are
     * 		to be replaced.
     * @param fragment
     * 		The new fragment to place in the container.
     * @param tag
     * 		Optional tag name for the fragment, to later retrieve the
     * 		fragment with {@link FragmentManager#findFragmentByTag(String)
     * 		FragmentManager.findFragmentByTag(String)}.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction replace(@android.annotation.IdRes
    int containerViewId, android.app.Fragment fragment, java.lang.String tag);

    /**
     * Remove an existing fragment.  If it was added to a container, its view
     * is also removed from that container.
     *
     * @param fragment
     * 		The fragment to be removed.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction remove(android.app.Fragment fragment);

    /**
     * Hides an existing fragment.  This is only relevant for fragments whose
     * views have been added to a container, as this will cause the view to
     * be hidden.
     *
     * @param fragment
     * 		The fragment to be hidden.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction hide(android.app.Fragment fragment);

    /**
     * Shows a previously hidden fragment.  This is only relevant for fragments whose
     * views have been added to a container, as this will cause the view to
     * be shown.
     *
     * @param fragment
     * 		The fragment to be shown.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction show(android.app.Fragment fragment);

    /**
     * Detach the given fragment from the UI.  This is the same state as
     * when it is put on the back stack: the fragment is removed from
     * the UI, however its state is still being actively managed by the
     * fragment manager.  When going into this state its view hierarchy
     * is destroyed.
     *
     * @param fragment
     * 		The fragment to be detached.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction detach(android.app.Fragment fragment);

    /**
     * Re-attach a fragment after it had previously been detached from
     * the UI with {@link #detach(Fragment)}.  This
     * causes its view hierarchy to be re-created, attached to the UI,
     * and displayed.
     *
     * @param fragment
     * 		The fragment to be attached.
     * @return Returns the same FragmentTransaction instance.
     */
    public abstract android.app.FragmentTransaction attach(android.app.Fragment fragment);

    /**
     *
     *
     * @return <code>true</code> if this transaction contains no operations,
    <code>false</code> otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * Bit mask that is set for all enter transitions.
     */
    public static final int TRANSIT_ENTER_MASK = 0x1000;

    /**
     * Bit mask that is set for all exit transitions.
     */
    public static final int TRANSIT_EXIT_MASK = 0x2000;

    /**
     * Not set up for a transition.
     */
    public static final int TRANSIT_UNSET = -1;

    /**
     * No animation for transition.
     */
    public static final int TRANSIT_NONE = 0;

    /**
     * Fragment is being added onto the stack
     */
    public static final int TRANSIT_FRAGMENT_OPEN = 1 | android.app.FragmentTransaction.TRANSIT_ENTER_MASK;

    /**
     * Fragment is being removed from the stack
     */
    public static final int TRANSIT_FRAGMENT_CLOSE = 2 | android.app.FragmentTransaction.TRANSIT_EXIT_MASK;

    /**
     * Fragment should simply fade in or out; that is, no strong navigation associated
     * with it except that it is appearing or disappearing for some reason.
     */
    public static final int TRANSIT_FRAGMENT_FADE = 3 | android.app.FragmentTransaction.TRANSIT_ENTER_MASK;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.app.FragmentTransaction.TRANSIT_NONE, android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN, android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Transit {}

    /**
     * Set specific animation resources to run for the fragments that are
     * entering and exiting in this transaction. These animations will not be
     * played when popping the back stack.
     */
    public abstract android.app.FragmentTransaction setCustomAnimations(@android.annotation.AnimatorRes
    int enter, @android.annotation.AnimatorRes
    int exit);

    /**
     * Set specific animation resources to run for the fragments that are
     * entering and exiting in this transaction. The <code>popEnter</code>
     * and <code>popExit</code> animations will be played for enter/exit
     * operations specifically when popping the back stack.
     */
    public abstract android.app.FragmentTransaction setCustomAnimations(@android.annotation.AnimatorRes
    int enter, @android.annotation.AnimatorRes
    int exit, @android.annotation.AnimatorRes
    int popEnter, @android.annotation.AnimatorRes
    int popExit);

    /**
     * Select a standard transition animation for this transaction.  May be
     * one of {@link #TRANSIT_NONE}, {@link #TRANSIT_FRAGMENT_OPEN},
     * {@link #TRANSIT_FRAGMENT_CLOSE}, or {@link #TRANSIT_FRAGMENT_FADE}.
     */
    public abstract android.app.FragmentTransaction setTransition(@android.app.FragmentTransaction.Transit
    int transit);

    /**
     * Used with to map a View from a removed or hidden Fragment to a View from a shown
     * or added Fragment.
     *
     * @param sharedElement
     * 		A View in a disappearing Fragment to match with a View in an
     * 		appearing Fragment.
     * @param name
     * 		The transitionName for a View in an appearing Fragment to match to the shared
     * 		element.
     */
    public abstract android.app.FragmentTransaction addSharedElement(android.view.View sharedElement, java.lang.String name);

    /**
     * Set a custom style resource that will be used for resolving transit
     * animations.
     */
    public abstract android.app.FragmentTransaction setTransitionStyle(@android.annotation.StyleRes
    int styleRes);

    /**
     * Add this transaction to the back stack.  This means that the transaction
     * will be remembered after it is committed, and will reverse its operation
     * when later popped off the stack.
     *
     * @param name
     * 		An optional name for this back stack state, or null.
     */
    public abstract android.app.FragmentTransaction addToBackStack(@android.annotation.Nullable
    java.lang.String name);

    /**
     * Returns true if this FragmentTransaction is allowed to be added to the back
     * stack. If this method would return false, {@link #addToBackStack(String)}
     * will throw {@link IllegalStateException}.
     *
     * @return True if {@link #addToBackStack(String)} is permitted on this transaction.
     */
    public abstract boolean isAddToBackStackAllowed();

    /**
     * Disallow calls to {@link #addToBackStack(String)}. Any future calls to
     * addToBackStack will throw {@link IllegalStateException}. If addToBackStack
     * has already been called, this method will throw IllegalStateException.
     */
    public abstract android.app.FragmentTransaction disallowAddToBackStack();

    /**
     * Set the full title to show as a bread crumb when this transaction
     * is on the back stack, as used by {@link FragmentBreadCrumbs}.
     *
     * @param res
     * 		A string resource containing the title.
     */
    public abstract android.app.FragmentTransaction setBreadCrumbTitle(@android.annotation.StringRes
    int res);

    /**
     * Like {@link #setBreadCrumbTitle(int)} but taking a raw string; this
     * method is <em>not</em> recommended, as the string can not be changed
     * later if the locale changes.
     */
    public abstract android.app.FragmentTransaction setBreadCrumbTitle(java.lang.CharSequence text);

    /**
     * Set the short title to show as a bread crumb when this transaction
     * is on the back stack, as used by {@link FragmentBreadCrumbs}.
     *
     * @param res
     * 		A string resource containing the title.
     */
    public abstract android.app.FragmentTransaction setBreadCrumbShortTitle(@android.annotation.StringRes
    int res);

    /**
     * Like {@link #setBreadCrumbShortTitle(int)} but taking a raw string; this
     * method is <em>not</em> recommended, as the string can not be changed
     * later if the locale changes.
     */
    public abstract android.app.FragmentTransaction setBreadCrumbShortTitle(java.lang.CharSequence text);

    /**
     * Schedules a commit of this transaction.  The commit does
     * not happen immediately; it will be scheduled as work on the main thread
     * to be done the next time that thread is ready.
     *
     * <p class="note">A transaction can only be committed with this method
     * prior to its containing activity saving its state.  If the commit is
     * attempted after that point, an exception will be thrown.  This is
     * because the state after the commit can be lost if the activity needs to
     * be restored from its state.  See {@link #commitAllowingStateLoss()} for
     * situations where it may be okay to lose the commit.</p>
     *
     * @return Returns the identifier of this transaction's back stack entry,
    if {@link #addToBackStack(String)} had been called.  Otherwise, returns
    a negative number.
     */
    public abstract int commit();

    /**
     * Like {@link #commit} but allows the commit to be executed after an
     * activity's state is saved.  This is dangerous because the commit can
     * be lost if the activity needs to later be restored from its state, so
     * this should only be used for cases where it is okay for the UI state
     * to change unexpectedly on the user.
     */
    public abstract int commitAllowingStateLoss();

    /**
     * Commits this transaction synchronously. Any added fragments will be
     * initialized and brought completely to the lifecycle state of their host
     * and any removed fragments will be torn down accordingly before this
     * call returns. Committing a transaction in this way allows fragments
     * to be added as dedicated, encapsulated components that monitor the
     * lifecycle state of their host while providing firmer ordering guarantees
     * around when those fragments are fully initialized and ready. Fragments
     * that manage views will have those views created and attached.
     *
     * <p>Calling <code>commitNow</code> is preferable to calling
     * {@link #commit()} followed by {@link FragmentManager#executePendingTransactions()}
     * as the latter will have the side effect of attempting to commit <em>all</em>
     * currently pending transactions whether that is the desired behavior
     * or not.</p>
     *
     * <p>Transactions committed in this way may not be added to the
     * FragmentManager's back stack, as doing so would break other expected
     * ordering guarantees for other asynchronously committed transactions.
     * This method will throw {@link IllegalStateException} if the transaction
     * previously requested to be added to the back stack with
     * {@link #addToBackStack(String)}.</p>
     *
     * <p class="note">A transaction can only be committed with this method
     * prior to its containing activity saving its state.  If the commit is
     * attempted after that point, an exception will be thrown.  This is
     * because the state after the commit can be lost if the activity needs to
     * be restored from its state.  See {@link #commitAllowingStateLoss()} for
     * situations where it may be okay to lose the commit.</p>
     */
    public abstract void commitNow();

    /**
     * Like {@link #commitNow} but allows the commit to be executed after an
     * activity's state is saved.  This is dangerous because the commit can
     * be lost if the activity needs to later be restored from its state, so
     * this should only be used for cases where it is okay for the UI state
     * to change unexpectedly on the user.
     */
    public abstract void commitNowAllowingStateLoss();
}

