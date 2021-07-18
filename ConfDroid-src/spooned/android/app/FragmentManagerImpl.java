package android.app;


/**
 * Container for fragments associated with an activity.
 */
final class FragmentManagerImpl extends android.app.FragmentManager implements android.view.LayoutInflater.Factory2 {
    static boolean DEBUG = false;

    static final java.lang.String TAG = "FragmentManager";

    static final java.lang.String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";

    static final java.lang.String TARGET_STATE_TAG = "android:target_state";

    static final java.lang.String VIEW_STATE_TAG = "android:view_state";

    static final java.lang.String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";

    static class AnimateOnHWLayerIfNeededListener implements android.animation.Animator.AnimatorListener {
        private boolean mShouldRunOnHWLayer = false;

        private android.view.View mView;

        public AnimateOnHWLayerIfNeededListener(final android.view.View v) {
            if (v == null) {
                return;
            }
            mView = v;
        }

        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animation) {
            mShouldRunOnHWLayer = android.app.FragmentManagerImpl.shouldRunOnHWLayer(mView, animation);
            if (mShouldRunOnHWLayer) {
                mView.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
            }
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            if (mShouldRunOnHWLayer) {
                mView.setLayerType(android.view.View.LAYER_TYPE_NONE, null);
            }
            mView = null;
            animation.removeListener(this);
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationRepeat(android.animation.Animator animation) {
        }
    }

    java.util.ArrayList<java.lang.Runnable> mPendingActions;

    java.lang.Runnable[] mTmpActions;

    boolean mExecutingActions;

    java.util.ArrayList<android.app.Fragment> mActive;

    java.util.ArrayList<android.app.Fragment> mAdded;

    java.util.ArrayList<java.lang.Integer> mAvailIndices;

    java.util.ArrayList<android.app.BackStackRecord> mBackStack;

    java.util.ArrayList<android.app.Fragment> mCreatedMenus;

    // Must be accessed while locked.
    java.util.ArrayList<android.app.BackStackRecord> mBackStackIndices;

    java.util.ArrayList<java.lang.Integer> mAvailBackStackIndices;

    java.util.ArrayList<android.app.FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;

    int mCurState = android.app.Fragment.INITIALIZING;

    android.app.FragmentHostCallback<?> mHost;

    android.app.FragmentController mController;

    android.app.FragmentContainer mContainer;

    android.app.Fragment mParent;

    boolean mNeedMenuInvalidate;

    boolean mStateSaved;

    boolean mDestroyed;

    java.lang.String mNoTransactionsBecause;

    boolean mHavePendingDeferredStart;

    // Temporary vars for state save and restore.
    android.os.Bundle mStateBundle = null;

    android.util.SparseArray<android.os.Parcelable> mStateArray = null;

    java.lang.Runnable mExecCommit = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            execPendingActions();
        }
    };

    private void throwException(java.lang.RuntimeException ex) {
        android.util.Log.e(android.app.FragmentManagerImpl.TAG, ex.getMessage());
        android.util.LogWriter logw = new android.util.LogWriter(android.util.Log.ERROR, android.app.FragmentManagerImpl.TAG);
        java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(logw, false, 1024);
        if (mHost != null) {
            android.util.Log.e(android.app.FragmentManagerImpl.TAG, "Activity state:");
            try {
                mHost.onDump("  ", null, pw, new java.lang.String[]{  });
            } catch (java.lang.Exception e) {
                pw.flush();
                android.util.Log.e(android.app.FragmentManagerImpl.TAG, "Failed dumping state", e);
            }
        } else {
            android.util.Log.e(android.app.FragmentManagerImpl.TAG, "Fragment manager state:");
            try {
                dump("  ", null, pw, new java.lang.String[]{  });
            } catch (java.lang.Exception e) {
                pw.flush();
                android.util.Log.e(android.app.FragmentManagerImpl.TAG, "Failed dumping state", e);
            }
        }
        pw.flush();
        throw ex;
    }

    static boolean modifiesAlpha(android.animation.Animator anim) {
        if (anim == null) {
            return false;
        }
        if (anim instanceof android.animation.ValueAnimator) {
            android.animation.ValueAnimator valueAnim = ((android.animation.ValueAnimator) (anim));
            android.animation.PropertyValuesHolder[] values = valueAnim.getValues();
            for (int i = 0; i < values.length; i++) {
                if ("alpha".equals(values[i].getPropertyName())) {
                    return true;
                }
            }
        } else
            if (anim instanceof android.animation.AnimatorSet) {
                java.util.List<android.animation.Animator> animList = ((android.animation.AnimatorSet) (anim)).getChildAnimations();
                for (int i = 0; i < animList.size(); i++) {
                    if (android.app.FragmentManagerImpl.modifiesAlpha(animList.get(i))) {
                        return true;
                    }
                }
            }

        return false;
    }

    static boolean shouldRunOnHWLayer(android.view.View v, android.animation.Animator anim) {
        if ((v == null) || (anim == null)) {
            return false;
        }
        return ((v.getLayerType() == android.view.View.LAYER_TYPE_NONE) && v.hasOverlappingRendering()) && android.app.FragmentManagerImpl.modifiesAlpha(anim);
    }

    /**
     * Sets the to be animated view on hardware layer during the animation.
     */
    private void setHWLayerAnimListenerIfAlpha(final android.view.View v, android.animation.Animator anim) {
        if ((v == null) || (anim == null)) {
            return;
        }
        if (android.app.FragmentManagerImpl.shouldRunOnHWLayer(v, anim)) {
            anim.addListener(new android.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(v));
        }
    }

    @java.lang.Override
    public android.app.FragmentTransaction beginTransaction() {
        return new android.app.BackStackRecord(this);
    }

    @java.lang.Override
    public boolean executePendingTransactions() {
        return execPendingActions();
    }

    @java.lang.Override
    public void popBackStack() {
        enqueueAction(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                popBackStackState(mHost.getHandler(), null, -1, 0);
            }
        }, false);
    }

    @java.lang.Override
    public boolean popBackStackImmediate() {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(mHost.getHandler(), null, -1, 0);
    }

    @java.lang.Override
    public void popBackStack(final java.lang.String name, final int flags) {
        enqueueAction(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                popBackStackState(mHost.getHandler(), name, -1, flags);
            }
        }, false);
    }

    @java.lang.Override
    public boolean popBackStackImmediate(java.lang.String name, int flags) {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(mHost.getHandler(), name, -1, flags);
    }

    @java.lang.Override
    public void popBackStack(final int id, final int flags) {
        if (id < 0) {
            throw new java.lang.IllegalArgumentException("Bad id: " + id);
        }
        enqueueAction(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                popBackStackState(mHost.getHandler(), null, id, flags);
            }
        }, false);
    }

    @java.lang.Override
    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        executePendingTransactions();
        if (id < 0) {
            throw new java.lang.IllegalArgumentException("Bad id: " + id);
        }
        return popBackStackState(mHost.getHandler(), null, id, flags);
    }

    @java.lang.Override
    public int getBackStackEntryCount() {
        return mBackStack != null ? mBackStack.size() : 0;
    }

    @java.lang.Override
    public android.app.FragmentManager.BackStackEntry getBackStackEntryAt(int index) {
        return mBackStack.get(index);
    }

    @java.lang.Override
    public void addOnBackStackChangedListener(android.app.FragmentManager.OnBackStackChangedListener listener) {
        if (mBackStackChangeListeners == null) {
            mBackStackChangeListeners = new java.util.ArrayList<android.app.FragmentManager.OnBackStackChangedListener>();
        }
        mBackStackChangeListeners.add(listener);
    }

    @java.lang.Override
    public void removeOnBackStackChangedListener(android.app.FragmentManager.OnBackStackChangedListener listener) {
        if (mBackStackChangeListeners != null) {
            mBackStackChangeListeners.remove(listener);
        }
    }

    @java.lang.Override
    public void putFragment(android.os.Bundle bundle, java.lang.String key, android.app.Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new java.lang.IllegalStateException(("Fragment " + fragment) + " is not currently in the FragmentManager"));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    @java.lang.Override
    public android.app.Fragment getFragment(android.os.Bundle bundle, java.lang.String key) {
        int index = bundle.getInt(key, -1);
        if (index == (-1)) {
            return null;
        }
        if (index >= mActive.size()) {
            throwException(new java.lang.IllegalStateException((("Fragment no longer exists for key " + key) + ": index ") + index));
        }
        android.app.Fragment f = mActive.get(index);
        if (f == null) {
            throwException(new java.lang.IllegalStateException((("Fragment no longer exists for key " + key) + ": index ") + index));
        }
        return f;
    }

    @java.lang.Override
    public android.app.Fragment.SavedState saveFragmentInstanceState(android.app.Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new java.lang.IllegalStateException(("Fragment " + fragment) + " is not currently in the FragmentManager"));
        }
        if (fragment.mState > android.app.Fragment.INITIALIZING) {
            android.os.Bundle result = saveFragmentBasicState(fragment);
            return result != null ? new android.app.Fragment.SavedState(result) : null;
        }
        return null;
    }

    @java.lang.Override
    public boolean isDestroyed() {
        return mDestroyed;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" in ");
        if (mParent != null) {
            android.util.DebugUtils.buildShortClassTag(mParent, sb);
        } else {
            android.util.DebugUtils.buildShortClassTag(mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    @java.lang.Override
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        java.lang.String innerPrefix = prefix + "    ";
        int N;
        if (mActive != null) {
            N = mActive.size();
            if (N > 0) {
                writer.print(prefix);
                writer.print("Active Fragments in ");
                writer.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
                writer.println(":");
                for (int i = 0; i < N; i++) {
                    android.app.Fragment f = mActive.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f);
                    if (f != null) {
                        f.dump(innerPrefix, fd, writer, args);
                    }
                }
            }
        }
        if (mAdded != null) {
            N = mAdded.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Added Fragments:");
                for (int i = 0; i < N; i++) {
                    android.app.Fragment f = mAdded.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (mCreatedMenus != null) {
            N = mCreatedMenus.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                for (int i = 0; i < N; i++) {
                    android.app.Fragment f = mCreatedMenus.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (mBackStack != null) {
            N = mBackStack.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                for (int i = 0; i < N; i++) {
                    android.app.BackStackRecord bs = mBackStack.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        synchronized(this) {
            if (mBackStackIndices != null) {
                N = mBackStackIndices.size();
                if (N > 0) {
                    writer.print(prefix);
                    writer.println("Back Stack Indices:");
                    for (int i = 0; i < N; i++) {
                        android.app.BackStackRecord bs = mBackStackIndices.get(i);
                        writer.print(prefix);
                        writer.print("  #");
                        writer.print(i);
                        writer.print(": ");
                        writer.println(bs);
                    }
                }
            }
            if ((mAvailBackStackIndices != null) && (mAvailBackStackIndices.size() > 0)) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(java.util.Arrays.toString(mAvailBackStackIndices.toArray()));
            }
        }
        if (mPendingActions != null) {
            N = mPendingActions.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (int i = 0; i < N; i++) {
                    java.lang.Runnable r = mPendingActions.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(r);
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(mContainer);
        if (mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(mCurState);
        writer.print(" mStateSaved=");
        writer.print(mStateSaved);
        writer.print(" mDestroyed=");
        writer.println(mDestroyed);
        if (mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(mNeedMenuInvalidate);
        }
        if (mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(mNoTransactionsBecause);
        }
        if ((mAvailIndices != null) && (mAvailIndices.size() > 0)) {
            writer.print(prefix);
            writer.print("  mAvailIndices: ");
            writer.println(java.util.Arrays.toString(mAvailIndices.toArray()));
        }
    }

    android.animation.Animator loadAnimator(android.app.Fragment fragment, int transit, boolean enter, int transitionStyle) {
        android.animation.Animator animObj = fragment.onCreateAnimator(transit, enter, fragment.mNextAnim);
        if (animObj != null) {
            return animObj;
        }
        if (fragment.mNextAnim != 0) {
            android.animation.Animator anim = android.animation.AnimatorInflater.loadAnimator(mHost.getContext(), fragment.mNextAnim);
            if (anim != null) {
                return anim;
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = android.app.FragmentManagerImpl.transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        if ((transitionStyle == 0) && mHost.onHasWindowAnimations()) {
            transitionStyle = mHost.onGetWindowAnimations();
        }
        if (transitionStyle == 0) {
            return null;
        }
        android.content.res.TypedArray attrs = mHost.getContext().obtainStyledAttributes(transitionStyle, com.android.internal.R.styleable.FragmentAnimation);
        int anim = attrs.getResourceId(styleIndex, 0);
        attrs.recycle();
        if (anim == 0) {
            return null;
        }
        return android.animation.AnimatorInflater.loadAnimator(mHost.getContext(), anim);
    }

    public void performPendingDeferredStart(android.app.Fragment f) {
        if (f.mDeferStart) {
            if (mExecutingActions) {
                // Wait until we're done executing our pending transactions
                mHavePendingDeferredStart = true;
                return;
            }
            f.mDeferStart = false;
            moveToState(f, mCurState, 0, 0, false);
        }
    }

    boolean isStateAtLeast(int state) {
        return mCurState >= state;
    }

    void moveToState(android.app.Fragment f, int newState, int transit, int transitionStyle, boolean keepActive) {
        if (android.app.FragmentManagerImpl.DEBUG && false)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, (((((((("moveToState: " + f) + " oldState=") + f.mState) + " newState=") + newState) + " mRemoving=") + f.mRemoving) + " Callers=") + android.os.Debug.getCallers(5));

        // Fragments that are not currently added will sit in the onCreate() state.
        if (((!f.mAdded) || f.mDetached) && (newState > android.app.Fragment.CREATED)) {
            newState = android.app.Fragment.CREATED;
        }
        if (f.mRemoving && (newState > f.mState)) {
            // While removing a fragment, we can't change it to a higher state.
            newState = f.mState;
        }
        // Defer start if requested; don't allow it to move to STARTED or higher
        // if it's not already started.
        if ((f.mDeferStart && (f.mState < android.app.Fragment.STARTED)) && (newState > android.app.Fragment.STOPPED)) {
            newState = android.app.Fragment.STOPPED;
        }
        if (f.mState < newState) {
            // For fragments that are created from a layout, when restoring from
            // state we don't want to allow them to be created until they are
            // being reloaded from the layout.
            if (f.mFromLayout && (!f.mInLayout)) {
                return;
            }
            if (f.mAnimatingAway != null) {
                // The fragment is currently being animated...  but!  Now we
                // want to move our state back up.  Give up on waiting for the
                // animation, move to whatever the final state should be once
                // the animation is done, and then we can proceed from there.
                f.mAnimatingAway = null;
                moveToState(f, f.mStateAfterAnimating, 0, 0, true);
            }
            switch (f.mState) {
                case android.app.Fragment.INITIALIZING :
                    if (android.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.app.FragmentManagerImpl.TAG, "moveto CREATED: " + f);

                    if (f.mSavedFragmentState != null) {
                        f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(android.app.FragmentManagerImpl.VIEW_STATE_TAG);
                        f.mTarget = getFragment(f.mSavedFragmentState, android.app.FragmentManagerImpl.TARGET_STATE_TAG);
                        if (f.mTarget != null) {
                            f.mTargetRequestCode = f.mSavedFragmentState.getInt(android.app.FragmentManagerImpl.TARGET_REQUEST_CODE_STATE_TAG, 0);
                        }
                        f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(android.app.FragmentManagerImpl.USER_VISIBLE_HINT_TAG, true);
                        if (!f.mUserVisibleHint) {
                            f.mDeferStart = true;
                            if (newState > android.app.Fragment.STOPPED) {
                                newState = android.app.Fragment.STOPPED;
                            }
                        }
                    }
                    f.mHost = mHost;
                    f.mParentFragment = mParent;
                    f.mFragmentManager = (mParent != null) ? mParent.mChildFragmentManager : mHost.getFragmentManagerImpl();
                    f.mCalled = false;
                    f.onAttach(mHost.getContext());
                    if (!f.mCalled) {
                        throw new android.util.SuperNotCalledException(("Fragment " + f) + " did not call through to super.onAttach()");
                    }
                    if (f.mParentFragment == null) {
                        mHost.onAttachFragment(f);
                    } else {
                        f.mParentFragment.onAttachFragment(f);
                    }
                    if (!f.mRetaining) {
                        f.performCreate(f.mSavedFragmentState);
                    } else {
                        f.restoreChildFragmentState(f.mSavedFragmentState, true);
                        f.mState = android.app.Fragment.CREATED;
                    }
                    f.mRetaining = false;
                    if (f.mFromLayout) {
                        // For fragments that are part of the content view
                        // layout, we need to instantiate the view immediately
                        // and the inflater will take care of adding it.
                        f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
                        if (f.mView != null) {
                            f.mView.setSaveFromParentEnabled(false);
                            if (f.mHidden)
                                f.mView.setVisibility(android.view.View.GONE);

                            f.onViewCreated(f.mView, f.mSavedFragmentState);
                        }
                    }
                case android.app.Fragment.CREATED :
                    if (newState > android.app.Fragment.CREATED) {
                        if (android.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "moveto ACTIVITY_CREATED: " + f);

                        if (!f.mFromLayout) {
                            android.view.ViewGroup container = null;
                            if (f.mContainerId != 0) {
                                if (f.mContainerId == android.view.View.NO_ID) {
                                    throwException(new java.lang.IllegalArgumentException(("Cannot create fragment " + f) + " for a container view with no id"));
                                }
                                container = ((android.view.ViewGroup) (mContainer.onFindViewById(f.mContainerId)));
                                if ((container == null) && (!f.mRestored)) {
                                    java.lang.String resName;
                                    try {
                                        resName = f.getResources().getResourceName(f.mContainerId);
                                    } catch (android.content.res.Resources.NotFoundException e) {
                                        resName = "unknown";
                                    }
                                    throwException(new java.lang.IllegalArgumentException((((("No view found for id 0x" + java.lang.Integer.toHexString(f.mContainerId)) + " (") + resName) + ") for fragment ") + f));
                                }
                            }
                            f.mContainer = container;
                            f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), container, f.mSavedFragmentState);
                            if (f.mView != null) {
                                f.mView.setSaveFromParentEnabled(false);
                                if (container != null) {
                                    android.animation.Animator anim = loadAnimator(f, transit, true, transitionStyle);
                                    if (anim != null) {
                                        anim.setTarget(f.mView);
                                        setHWLayerAnimListenerIfAlpha(f.mView, anim);
                                        anim.start();
                                    }
                                    container.addView(f.mView);
                                }
                                if (f.mHidden)
                                    f.mView.setVisibility(android.view.View.GONE);

                                f.onViewCreated(f.mView, f.mSavedFragmentState);
                            }
                        }
                        f.performActivityCreated(f.mSavedFragmentState);
                        if (f.mView != null) {
                            f.restoreViewState(f.mSavedFragmentState);
                        }
                        f.mSavedFragmentState = null;
                    }
                case android.app.Fragment.ACTIVITY_CREATED :
                    if (newState > android.app.Fragment.ACTIVITY_CREATED) {
                        f.mState = android.app.Fragment.STOPPED;
                    }
                case android.app.Fragment.STOPPED :
                    if (newState > android.app.Fragment.STOPPED) {
                        if (android.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "moveto STARTED: " + f);

                        f.performStart();
                    }
                case android.app.Fragment.STARTED :
                    if (newState > android.app.Fragment.STARTED) {
                        if (android.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "moveto RESUMED: " + f);

                        f.performResume();
                        // Get rid of this in case we saved it and never needed it.
                        f.mSavedFragmentState = null;
                        f.mSavedViewState = null;
                    }
            }
        } else
            if (f.mState > newState) {
                switch (f.mState) {
                    case android.app.Fragment.RESUMED :
                        if (newState < android.app.Fragment.RESUMED) {
                            if (android.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.app.FragmentManagerImpl.TAG, "movefrom RESUMED: " + f);

                            f.performPause();
                        }
                    case android.app.Fragment.STARTED :
                        if (newState < android.app.Fragment.STARTED) {
                            if (android.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.app.FragmentManagerImpl.TAG, "movefrom STARTED: " + f);

                            f.performStop();
                        }
                    case android.app.Fragment.STOPPED :
                    case android.app.Fragment.ACTIVITY_CREATED :
                        if (newState < android.app.Fragment.ACTIVITY_CREATED) {
                            if (android.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.app.FragmentManagerImpl.TAG, "movefrom ACTIVITY_CREATED: " + f);

                            if (f.mView != null) {
                                // Need to save the current view state if not
                                // done already.
                                if (mHost.onShouldSaveFragmentState(f) && (f.mSavedViewState == null)) {
                                    saveFragmentViewState(f);
                                }
                            }
                            f.performDestroyView();
                            if ((f.mView != null) && (f.mContainer != null)) {
                                android.animation.Animator anim = null;
                                if ((mCurState > android.app.Fragment.INITIALIZING) && (!mDestroyed)) {
                                    anim = loadAnimator(f, transit, false, transitionStyle);
                                }
                                if (anim != null) {
                                    final android.view.ViewGroup container = f.mContainer;
                                    final android.view.View view = f.mView;
                                    final android.app.Fragment fragment = f;
                                    container.startViewTransition(view);
                                    f.mAnimatingAway = anim;
                                    f.mStateAfterAnimating = newState;
                                    anim.addListener(new android.animation.AnimatorListenerAdapter() {
                                        @java.lang.Override
                                        public void onAnimationEnd(android.animation.Animator anim) {
                                            container.endViewTransition(view);
                                            if (fragment.mAnimatingAway != null) {
                                                fragment.mAnimatingAway = null;
                                                moveToState(fragment, fragment.mStateAfterAnimating, 0, 0, false);
                                            }
                                        }
                                    });
                                    anim.setTarget(f.mView);
                                    setHWLayerAnimListenerIfAlpha(f.mView, anim);
                                    anim.start();
                                }
                                f.mContainer.removeView(f.mView);
                            }
                            f.mContainer = null;
                            f.mView = null;
                        }
                    case android.app.Fragment.CREATED :
                        if (newState < android.app.Fragment.CREATED) {
                            if (mDestroyed) {
                                if (f.mAnimatingAway != null) {
                                    // The fragment's containing activity is
                                    // being destroyed, but this fragment is
                                    // currently animating away.  Stop the
                                    // animation right now -- it is not needed,
                                    // and we can't wait any more on destroying
                                    // the fragment.
                                    android.animation.Animator anim = f.mAnimatingAway;
                                    f.mAnimatingAway = null;
                                    anim.cancel();
                                }
                            }
                            if (f.mAnimatingAway != null) {
                                // We are waiting for the fragment's view to finish
                                // animating away.  Just make a note of the state
                                // the fragment now should move to once the animation
                                // is done.
                                f.mStateAfterAnimating = newState;
                                newState = android.app.Fragment.CREATED;
                            } else {
                                if (android.app.FragmentManagerImpl.DEBUG)
                                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, "movefrom CREATED: " + f);

                                if (!f.mRetaining) {
                                    f.performDestroy();
                                } else {
                                    f.mState = android.app.Fragment.INITIALIZING;
                                }
                                f.performDetach();
                                if (!keepActive) {
                                    if (!f.mRetaining) {
                                        makeInactive(f);
                                    } else {
                                        f.mHost = null;
                                        f.mParentFragment = null;
                                        f.mFragmentManager = null;
                                    }
                                }
                            }
                        }
                }
            }

        if (f.mState != newState) {
            android.util.Log.w(android.app.FragmentManagerImpl.TAG, ((((("moveToState: Fragment state for " + f) + " not updated inline; ") + "expected state ") + newState) + " found ") + f.mState);
            f.mState = newState;
        }
    }

    void moveToState(android.app.Fragment f) {
        moveToState(f, mCurState, 0, 0, false);
    }

    void moveToState(int newState, boolean always) {
        moveToState(newState, 0, 0, always);
    }

    void moveToState(int newState, int transit, int transitStyle, boolean always) {
        if ((mHost == null) && (newState != android.app.Fragment.INITIALIZING)) {
            throw new java.lang.IllegalStateException("No activity");
        }
        if ((!always) && (mCurState == newState)) {
            return;
        }
        mCurState = newState;
        if (mActive != null) {
            boolean loadersRunning = false;
            for (int i = 0; i < mActive.size(); i++) {
                android.app.Fragment f = mActive.get(i);
                if (f != null) {
                    moveToState(f, newState, transit, transitStyle, false);
                    if (f.mLoaderManager != null) {
                        loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                    }
                }
            }
            if (!loadersRunning) {
                startPendingDeferredFragments();
            }
            if ((mNeedMenuInvalidate && (mHost != null)) && (mCurState == android.app.Fragment.RESUMED)) {
                mHost.onInvalidateOptionsMenu();
                mNeedMenuInvalidate = false;
            }
        }
    }

    void startPendingDeferredFragments() {
        if (mActive == null)
            return;

        for (int i = 0; i < mActive.size(); i++) {
            android.app.Fragment f = mActive.get(i);
            if (f != null) {
                performPendingDeferredStart(f);
            }
        }
    }

    void makeActive(android.app.Fragment f) {
        if (f.mIndex >= 0) {
            return;
        }
        if ((mAvailIndices == null) || (mAvailIndices.size() <= 0)) {
            if (mActive == null) {
                mActive = new java.util.ArrayList<android.app.Fragment>();
            }
            f.setIndex(mActive.size(), mParent);
            mActive.add(f);
        } else {
            f.setIndex(mAvailIndices.remove(mAvailIndices.size() - 1), mParent);
            mActive.set(f.mIndex, f);
        }
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "Allocated fragment index " + f);

    }

    void makeInactive(android.app.Fragment f) {
        if (f.mIndex < 0) {
            return;
        }
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "Freeing fragment index " + f);

        mActive.set(f.mIndex, null);
        if (mAvailIndices == null) {
            mAvailIndices = new java.util.ArrayList<java.lang.Integer>();
        }
        mAvailIndices.add(f.mIndex);
        mHost.inactivateFragment(f.mWho);
        f.initState();
    }

    public void addFragment(android.app.Fragment fragment, boolean moveToStateNow) {
        if (mAdded == null) {
            mAdded = new java.util.ArrayList<android.app.Fragment>();
        }
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "add: " + fragment);

        makeActive(fragment);
        if (!fragment.mDetached) {
            if (mAdded.contains(fragment)) {
                throw new java.lang.IllegalStateException("Fragment already added: " + fragment);
            }
            mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
            }
        }
    }

    public void removeFragment(android.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("remove: " + fragment) + " nesting=") + fragment.mBackStackNesting);

        final boolean inactive = !fragment.isInBackStack();
        if ((!fragment.mDetached) || inactive) {
            if (false) {
                // Would be nice to catch a bad remove here, but we need
                // time to test this to make sure we aren't crashes cases
                // where it is not a problem.
                if (!mAdded.contains(fragment)) {
                    throw new java.lang.IllegalStateException("Fragment not added: " + fragment);
                }
            }
            if (mAdded != null) {
                mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
            moveToState(fragment, inactive ? android.app.Fragment.INITIALIZING : android.app.Fragment.CREATED, transition, transitionStyle, false);
        }
    }

    public void hideFragment(android.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "hide: " + fragment);

        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mView != null) {
                android.animation.Animator anim = loadAnimator(fragment, transition, false, transitionStyle);
                if (anim != null) {
                    anim.setTarget(fragment.mView);
                    // Delay the actual hide operation until the animation finishes, otherwise
                    // the fragment will just immediately disappear
                    final android.app.Fragment finalFragment = fragment;
                    anim.addListener(new android.animation.AnimatorListenerAdapter() {
                        @java.lang.Override
                        public void onAnimationEnd(android.animation.Animator animation) {
                            if (finalFragment.mView != null) {
                                finalFragment.mView.setVisibility(android.view.View.GONE);
                            }
                        }
                    });
                    setHWLayerAnimListenerIfAlpha(finalFragment.mView, anim);
                    anim.start();
                } else {
                    fragment.mView.setVisibility(android.view.View.GONE);
                }
            }
            if ((fragment.mAdded && fragment.mHasMenu) && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(true);
        }
    }

    public void showFragment(android.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "show: " + fragment);

        if (fragment.mHidden) {
            fragment.mHidden = false;
            if (fragment.mView != null) {
                android.animation.Animator anim = loadAnimator(fragment, transition, true, transitionStyle);
                if (anim != null) {
                    anim.setTarget(fragment.mView);
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    anim.start();
                }
                fragment.mView.setVisibility(android.view.View.VISIBLE);
            }
            if ((fragment.mAdded && fragment.mHasMenu) && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(false);
        }
    }

    public void detachFragment(android.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "detach: " + fragment);

        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                // We are not already in back stack, so need to remove the fragment.
                if (mAdded != null) {
                    if (android.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.app.FragmentManagerImpl.TAG, "remove from detach: " + fragment);

                    mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
                moveToState(fragment, android.app.Fragment.CREATED, transition, transitionStyle, false);
            }
        }
    }

    public void attachFragment(android.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "attach: " + fragment);

        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                if (mAdded == null) {
                    mAdded = new java.util.ArrayList<android.app.Fragment>();
                }
                if (mAdded.contains(fragment)) {
                    throw new java.lang.IllegalStateException("Fragment already added: " + fragment);
                }
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, "add from attach: " + fragment);

                mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    mNeedMenuInvalidate = true;
                }
                moveToState(fragment, mCurState, transition, transitionStyle, false);
            }
        }
    }

    public android.app.Fragment findFragmentById(int id) {
        if (mAdded != null) {
            // First look through added fragments.
            for (int i = mAdded.size() - 1; i >= 0; i--) {
                android.app.Fragment f = mAdded.get(i);
                if ((f != null) && (f.mFragmentId == id)) {
                    return f;
                }
            }
        }
        if (mActive != null) {
            // Now for any known fragment.
            for (int i = mActive.size() - 1; i >= 0; i--) {
                android.app.Fragment f = mActive.get(i);
                if ((f != null) && (f.mFragmentId == id)) {
                    return f;
                }
            }
        }
        return null;
    }

    public android.app.Fragment findFragmentByTag(java.lang.String tag) {
        if ((mAdded != null) && (tag != null)) {
            // First look through added fragments.
            for (int i = mAdded.size() - 1; i >= 0; i--) {
                android.app.Fragment f = mAdded.get(i);
                if ((f != null) && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if ((mActive != null) && (tag != null)) {
            // Now for any known fragment.
            for (int i = mActive.size() - 1; i >= 0; i--) {
                android.app.Fragment f = mActive.get(i);
                if ((f != null) && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        return null;
    }

    public android.app.Fragment findFragmentByWho(java.lang.String who) {
        if ((mActive != null) && (who != null)) {
            for (int i = mActive.size() - 1; i >= 0; i--) {
                android.app.Fragment f = mActive.get(i);
                if ((f != null) && ((f = f.findFragmentByWho(who)) != null)) {
                    return f;
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (mStateSaved) {
            throw new java.lang.IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (mNoTransactionsBecause != null) {
            throw new java.lang.IllegalStateException("Can not perform this action inside of " + mNoTransactionsBecause);
        }
    }

    /**
     * Adds an action to the queue of pending actions.
     *
     * @param action
     * 		the action to add
     * @param allowStateLoss
     * 		whether to allow loss of state information
     * @throws IllegalStateException
     * 		if the activity has been destroyed
     */
    public void enqueueAction(java.lang.Runnable action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized(this) {
            if (mDestroyed || (mHost == null)) {
                throw new java.lang.IllegalStateException("Activity has been destroyed");
            }
            if (mPendingActions == null) {
                mPendingActions = new java.util.ArrayList<java.lang.Runnable>();
            }
            mPendingActions.add(action);
            if (mPendingActions.size() == 1) {
                mHost.getHandler().removeCallbacks(mExecCommit);
                mHost.getHandler().post(mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(android.app.BackStackRecord bse) {
        synchronized(this) {
            if ((mAvailBackStackIndices == null) || (mAvailBackStackIndices.size() <= 0)) {
                if (mBackStackIndices == null) {
                    mBackStackIndices = new java.util.ArrayList<android.app.BackStackRecord>();
                }
                int index = mBackStackIndices.size();
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("Setting back stack index " + index) + " to ") + bse);

                mBackStackIndices.add(bse);
                return index;
            } else {
                int index = mAvailBackStackIndices.remove(mAvailBackStackIndices.size() - 1);
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("Adding back stack index " + index) + " with ") + bse);

                mBackStackIndices.set(index, bse);
                return index;
            }
        }
    }

    public void setBackStackIndex(int index, android.app.BackStackRecord bse) {
        synchronized(this) {
            if (mBackStackIndices == null) {
                mBackStackIndices = new java.util.ArrayList<android.app.BackStackRecord>();
            }
            int N = mBackStackIndices.size();
            if (index < N) {
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("Setting back stack index " + index) + " to ") + bse);

                mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    mBackStackIndices.add(null);
                    if (mAvailBackStackIndices == null) {
                        mAvailBackStackIndices = new java.util.ArrayList<java.lang.Integer>();
                    }
                    if (android.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.app.FragmentManagerImpl.TAG, "Adding available back stack index " + N);

                    mAvailBackStackIndices.add(N);
                    N++;
                } 
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("Adding back stack index " + index) + " with ") + bse);

                mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized(this) {
            mBackStackIndices.set(index, null);
            if (mAvailBackStackIndices == null) {
                mAvailBackStackIndices = new java.util.ArrayList<java.lang.Integer>();
            }
            if (android.app.FragmentManagerImpl.DEBUG)
                android.util.Log.v(android.app.FragmentManagerImpl.TAG, "Freeing back stack index " + index);

            mAvailBackStackIndices.add(index);
        }
    }

    public void execSingleAction(java.lang.Runnable action, boolean allowStateLoss) {
        if (mExecutingActions) {
            throw new java.lang.IllegalStateException("FragmentManager is already executing transactions");
        }
        if (android.os.Looper.myLooper() != mHost.getHandler().getLooper()) {
            throw new java.lang.IllegalStateException("Must be called from main thread of fragment host");
        }
        if (!allowStateLoss) {
            checkStateLoss();
        }
        mExecutingActions = true;
        action.run();
        mExecutingActions = false;
        doPendingDeferredStart();
    }

    /**
     * Only call from main thread!
     */
    public boolean execPendingActions() {
        if (mExecutingActions) {
            throw new java.lang.IllegalStateException("Recursive entry to executePendingTransactions");
        }
        if (android.os.Looper.myLooper() != mHost.getHandler().getLooper()) {
            throw new java.lang.IllegalStateException("Must be called from main thread of process");
        }
        boolean didSomething = false;
        while (true) {
            int numActions;
            synchronized(this) {
                if ((mPendingActions == null) || (mPendingActions.size() == 0)) {
                    break;
                }
                numActions = mPendingActions.size();
                if ((mTmpActions == null) || (mTmpActions.length < numActions)) {
                    mTmpActions = new java.lang.Runnable[numActions];
                }
                mPendingActions.toArray(mTmpActions);
                mPendingActions.clear();
                mHost.getHandler().removeCallbacks(mExecCommit);
            }
            mExecutingActions = true;
            for (int i = 0; i < numActions; i++) {
                mTmpActions[i].run();
                mTmpActions[i] = null;
            }
            mExecutingActions = false;
            didSomething = true;
        } 
        doPendingDeferredStart();
        return didSomething;
    }

    void doPendingDeferredStart() {
        if (mHavePendingDeferredStart) {
            boolean loadersRunning = false;
            for (int i = 0; i < mActive.size(); i++) {
                android.app.Fragment f = mActive.get(i);
                if ((f != null) && (f.mLoaderManager != null)) {
                    loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                }
            }
            if (!loadersRunning) {
                mHavePendingDeferredStart = false;
                startPendingDeferredFragments();
            }
        }
    }

    void reportBackStackChanged() {
        if (mBackStackChangeListeners != null) {
            for (int i = 0; i < mBackStackChangeListeners.size(); i++) {
                mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    void addBackStackState(android.app.BackStackRecord state) {
        if (mBackStack == null) {
            mBackStack = new java.util.ArrayList<android.app.BackStackRecord>();
        }
        mBackStack.add(state);
        reportBackStackChanged();
    }

    boolean popBackStackState(android.os.Handler handler, java.lang.String name, int id, int flags) {
        if (mBackStack == null) {
            return false;
        }
        if (((name == null) && (id < 0)) && ((flags & android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE) == 0)) {
            int last = mBackStack.size() - 1;
            if (last < 0) {
                return false;
            }
            final android.app.BackStackRecord bss = mBackStack.remove(last);
            android.util.SparseArray<android.app.Fragment> firstOutFragments = new android.util.SparseArray<android.app.Fragment>();
            android.util.SparseArray<android.app.Fragment> lastInFragments = new android.util.SparseArray<android.app.Fragment>();
            if (mCurState >= android.app.Fragment.CREATED) {
                bss.calculateBackFragments(firstOutFragments, lastInFragments);
            }
            bss.popFromBackStack(true, null, firstOutFragments, lastInFragments);
            reportBackStackChanged();
        } else {
            int index = -1;
            if ((name != null) || (id >= 0)) {
                // If a name or ID is specified, look for that place in
                // the stack.
                index = mBackStack.size() - 1;
                while (index >= 0) {
                    android.app.BackStackRecord bss = mBackStack.get(index);
                    if ((name != null) && name.equals(bss.getName())) {
                        break;
                    }
                    if ((id >= 0) && (id == bss.mIndex)) {
                        break;
                    }
                    index--;
                } 
                if (index < 0) {
                    return false;
                }
                if ((flags & android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE) != 0) {
                    index--;
                    // Consume all following entries that match.
                    while (index >= 0) {
                        android.app.BackStackRecord bss = mBackStack.get(index);
                        if (((name != null) && name.equals(bss.getName())) || ((id >= 0) && (id == bss.mIndex))) {
                            index--;
                            continue;
                        }
                        break;
                    } 
                }
            }
            if (index == (mBackStack.size() - 1)) {
                return false;
            }
            final java.util.ArrayList<android.app.BackStackRecord> states = new java.util.ArrayList<android.app.BackStackRecord>();
            for (int i = mBackStack.size() - 1; i > index; i--) {
                states.add(mBackStack.remove(i));
            }
            final int LAST = states.size() - 1;
            android.util.SparseArray<android.app.Fragment> firstOutFragments = new android.util.SparseArray<android.app.Fragment>();
            android.util.SparseArray<android.app.Fragment> lastInFragments = new android.util.SparseArray<android.app.Fragment>();
            if (mCurState >= android.app.Fragment.CREATED) {
                for (int i = 0; i <= LAST; i++) {
                    states.get(i).calculateBackFragments(firstOutFragments, lastInFragments);
                }
            }
            android.app.BackStackRecord.TransitionState state = null;
            for (int i = 0; i <= LAST; i++) {
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, "Popping back stack state: " + states.get(i));

                state = states.get(i).popFromBackStack(i == LAST, state, firstOutFragments, lastInFragments);
            }
            reportBackStackChanged();
        }
        return true;
    }

    android.app.FragmentManagerNonConfig retainNonConfig() {
        java.util.ArrayList<android.app.Fragment> fragments = null;
        java.util.ArrayList<android.app.FragmentManagerNonConfig> childFragments = null;
        if (mActive != null) {
            for (int i = 0; i < mActive.size(); i++) {
                android.app.Fragment f = mActive.get(i);
                if (f != null) {
                    if (f.mRetainInstance) {
                        if (fragments == null) {
                            fragments = new java.util.ArrayList<>();
                        }
                        fragments.add(f);
                        f.mRetaining = true;
                        f.mTargetIndex = (f.mTarget != null) ? f.mTarget.mIndex : -1;
                        if (android.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.app.FragmentManagerImpl.TAG, "retainNonConfig: keeping retained " + f);

                    }
                    boolean addedChild = false;
                    if (f.mChildFragmentManager != null) {
                        android.app.FragmentManagerNonConfig child = f.mChildFragmentManager.retainNonConfig();
                        if (child != null) {
                            if (childFragments == null) {
                                childFragments = new java.util.ArrayList<>();
                                for (int j = 0; j < i; j++) {
                                    childFragments.add(null);
                                }
                            }
                            childFragments.add(child);
                            addedChild = true;
                        }
                    }
                    if ((childFragments != null) && (!addedChild)) {
                        childFragments.add(null);
                    }
                }
            }
        }
        if ((fragments == null) && (childFragments == null)) {
            return null;
        }
        return new android.app.FragmentManagerNonConfig(fragments, childFragments);
    }

    void saveFragmentViewState(android.app.Fragment f) {
        if (f.mView == null) {
            return;
        }
        if (mStateArray == null) {
            mStateArray = new android.util.SparseArray<android.os.Parcelable>();
        } else {
            mStateArray.clear();
        }
        f.mView.saveHierarchyState(mStateArray);
        if (mStateArray.size() > 0) {
            f.mSavedViewState = mStateArray;
            mStateArray = null;
        }
    }

    android.os.Bundle saveFragmentBasicState(android.app.Fragment f) {
        android.os.Bundle result = null;
        if (mStateBundle == null) {
            mStateBundle = new android.os.Bundle();
        }
        f.performSaveInstanceState(mStateBundle);
        if (!mStateBundle.isEmpty()) {
            result = mStateBundle;
            mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new android.os.Bundle();
            }
            result.putSparseParcelableArray(android.app.FragmentManagerImpl.VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new android.os.Bundle();
            }
            // Only add this if it's not the default value
            result.putBoolean(android.app.FragmentManagerImpl.USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    android.os.Parcelable saveAllState() {
        // Make sure all pending operations have now been executed to get
        // our state update-to-date.
        execPendingActions();
        mStateSaved = true;
        if ((mActive == null) || (mActive.size() <= 0)) {
            return null;
        }
        // First collect all active fragments.
        int N = mActive.size();
        android.app.FragmentState[] active = new android.app.FragmentState[N];
        boolean haveFragments = false;
        for (int i = 0; i < N; i++) {
            android.app.Fragment f = mActive.get(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    throwException(new java.lang.IllegalStateException((("Failure saving state: active " + f) + " has cleared index: ") + f.mIndex));
                }
                haveFragments = true;
                android.app.FragmentState fs = new android.app.FragmentState(f);
                active[i] = fs;
                if ((f.mState > android.app.Fragment.INITIALIZING) && (fs.mSavedFragmentState == null)) {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            throwException(new java.lang.IllegalStateException((("Failure saving state: " + f) + " has target not in fragment manager: ") + f.mTarget));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new android.os.Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, android.app.FragmentManagerImpl.TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(android.app.FragmentManagerImpl.TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                } else {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                }
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("Saved state of " + f) + ": ") + fs.mSavedFragmentState);

            }
        }
        if (!haveFragments) {
            if (android.app.FragmentManagerImpl.DEBUG)
                android.util.Log.v(android.app.FragmentManagerImpl.TAG, "saveAllState: no fragments!");

            return null;
        }
        int[] added = null;
        android.app.BackStackState[] backStack = null;
        // Build list of currently added fragments.
        if (mAdded != null) {
            N = mAdded.size();
            if (N > 0) {
                added = new int[N];
                for (int i = 0; i < N; i++) {
                    added[i] = mAdded.get(i).mIndex;
                    if (added[i] < 0) {
                        throwException(new java.lang.IllegalStateException((("Failure saving state: active " + mAdded.get(i)) + " has cleared index: ") + added[i]));
                    }
                    if (android.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("saveAllState: adding fragment #" + i) + ": ") + mAdded.get(i));

                }
            }
        }
        // Now save back stack.
        if (mBackStack != null) {
            N = mBackStack.size();
            if (N > 0) {
                backStack = new android.app.BackStackState[N];
                for (int i = 0; i < N; i++) {
                    backStack[i] = new android.app.BackStackState(this, mBackStack.get(i));
                    if (android.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("saveAllState: adding back stack #" + i) + ": ") + mBackStack.get(i));

                }
            }
        }
        android.app.FragmentManagerState fms = new android.app.FragmentManagerState();
        fms.mActive = active;
        fms.mAdded = added;
        fms.mBackStack = backStack;
        return fms;
    }

    void restoreAllState(android.os.Parcelable state, android.app.FragmentManagerNonConfig nonConfig) {
        // If there is no saved state at all, then there can not be
        // any nonConfig fragments either, so that is that.
        if (state == null)
            return;

        android.app.FragmentManagerState fms = ((android.app.FragmentManagerState) (state));
        if (fms.mActive == null)
            return;

        java.util.List<android.app.FragmentManagerNonConfig> childNonConfigs = null;
        // First re-attach any non-config instances we are retaining back
        // to their saved state, so we don't try to instantiate them again.
        if (nonConfig != null) {
            java.util.List<android.app.Fragment> nonConfigFragments = nonConfig.getFragments();
            childNonConfigs = nonConfig.getChildNonConfigs();
            final int count = (nonConfigFragments != null) ? nonConfigFragments.size() : 0;
            for (int i = 0; i < count; i++) {
                android.app.Fragment f = nonConfigFragments.get(i);
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, "restoreAllState: re-attaching retained " + f);

                android.app.FragmentState fs = fms.mActive[f.mIndex];
                fs.mInstance = f;
                f.mSavedViewState = null;
                f.mBackStackNesting = 0;
                f.mInLayout = false;
                f.mAdded = false;
                f.mTarget = null;
                if (fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
                    f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(android.app.FragmentManagerImpl.VIEW_STATE_TAG);
                    f.mSavedFragmentState = fs.mSavedFragmentState;
                }
            }
        }
        // Build the full list of active fragments, instantiating them from
        // their saved state.
        mActive = new java.util.ArrayList<>(fms.mActive.length);
        if (mAvailIndices != null) {
            mAvailIndices.clear();
        }
        for (int i = 0; i < fms.mActive.length; i++) {
            android.app.FragmentState fs = fms.mActive[i];
            if (fs != null) {
                android.app.FragmentManagerNonConfig childNonConfig = null;
                if ((childNonConfigs != null) && (i < childNonConfigs.size())) {
                    childNonConfig = childNonConfigs.get(i);
                }
                android.app.Fragment f = fs.instantiate(mHost, mParent, childNonConfig);
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("restoreAllState: active #" + i) + ": ") + f);

                mActive.add(f);
                // Now that the fragment is instantiated (or came from being
                // retained above), clear mInstance in case we end up re-restoring
                // from this FragmentState again.
                fs.mInstance = null;
            } else {
                mActive.add(null);
                if (mAvailIndices == null) {
                    mAvailIndices = new java.util.ArrayList<>();
                }
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, "restoreAllState: avail #" + i);

                mAvailIndices.add(i);
            }
        }
        // Update the target of all retained fragments.
        if (nonConfig != null) {
            java.util.List<android.app.Fragment> nonConfigFragments = nonConfig.getFragments();
            final int count = (nonConfigFragments != null) ? nonConfigFragments.size() : 0;
            for (int i = 0; i < count; i++) {
                android.app.Fragment f = nonConfigFragments.get(i);
                if (f.mTargetIndex >= 0) {
                    if (f.mTargetIndex < mActive.size()) {
                        f.mTarget = mActive.get(f.mTargetIndex);
                    } else {
                        android.util.Log.w(android.app.FragmentManagerImpl.TAG, (("Re-attaching retained fragment " + f) + " target no longer exists: ") + f.mTargetIndex);
                        f.mTarget = null;
                    }
                }
            }
        }
        // Build the list of currently added fragments.
        if (fms.mAdded != null) {
            mAdded = new java.util.ArrayList<android.app.Fragment>(fms.mAdded.length);
            for (int i = 0; i < fms.mAdded.length; i++) {
                android.app.Fragment f = mActive.get(fms.mAdded[i]);
                if (f == null) {
                    throwException(new java.lang.IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i]));
                }
                f.mAdded = true;
                if (android.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (("restoreAllState: added #" + i) + ": ") + f);

                if (mAdded.contains(f)) {
                    throw new java.lang.IllegalStateException("Already added!");
                }
                mAdded.add(f);
            }
        } else {
            mAdded = null;
        }
        // Build the back stack.
        if (fms.mBackStack != null) {
            mBackStack = new java.util.ArrayList<android.app.BackStackRecord>(fms.mBackStack.length);
            for (int i = 0; i < fms.mBackStack.length; i++) {
                android.app.BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                if (android.app.FragmentManagerImpl.DEBUG) {
                    android.util.Log.v(android.app.FragmentManagerImpl.TAG, (((("restoreAllState: back stack #" + i) + " (index ") + bse.mIndex) + "): ") + bse);
                    android.util.LogWriter logw = new android.util.LogWriter(android.util.Log.VERBOSE, android.app.FragmentManagerImpl.TAG);
                    java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(logw, false, 1024);
                    bse.dump("  ", pw, false);
                    pw.flush();
                }
                mBackStack.add(bse);
                if (bse.mIndex >= 0) {
                    setBackStackIndex(bse.mIndex, bse);
                }
            }
        } else {
            mBackStack = null;
        }
    }

    public void attachController(android.app.FragmentHostCallback<?> host, android.app.FragmentContainer container, android.app.Fragment parent) {
        if (mHost != null)
            throw new java.lang.IllegalStateException("Already attached");

        mHost = host;
        mContainer = container;
        mParent = parent;
    }

    public void noteStateNotSaved() {
        mStateSaved = false;
    }

    public void dispatchCreate() {
        mStateSaved = false;
        moveToState(android.app.Fragment.CREATED, false);
    }

    public void dispatchActivityCreated() {
        mStateSaved = false;
        moveToState(android.app.Fragment.ACTIVITY_CREATED, false);
    }

    public void dispatchStart() {
        mStateSaved = false;
        moveToState(android.app.Fragment.STARTED, false);
    }

    public void dispatchResume() {
        mStateSaved = false;
        moveToState(android.app.Fragment.RESUMED, false);
    }

    public void dispatchPause() {
        moveToState(android.app.Fragment.STARTED, false);
    }

    public void dispatchStop() {
        moveToState(android.app.Fragment.STOPPED, false);
    }

    public void dispatchDestroyView() {
        moveToState(android.app.Fragment.CREATED, false);
    }

    public void dispatchDestroy() {
        mDestroyed = true;
        execPendingActions();
        moveToState(android.app.Fragment.INITIALIZING, false);
        mHost = null;
        mContainer = null;
        mParent = null;
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        if (mAdded == null) {
            return;
        }
        for (int i = mAdded.size() - 1; i >= 0; --i) {
            final android.app.Fragment f = mAdded.get(i);
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        if (mAdded == null) {
            return;
        }
        for (int i = mAdded.size() - 1; i >= 0; --i) {
            final android.app.Fragment f = mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchConfigurationChanged(android.content.res.Configuration newConfig) {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performConfigurationChanged(newConfig);
                }
            }
        }
    }

    public void dispatchLowMemory() {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performLowMemory();
                }
            }
        }
    }

    public void dispatchTrimMemory(int level) {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performTrimMemory(level);
                }
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
        boolean show = false;
        java.util.ArrayList<android.app.Fragment> newMenus = null;
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    if (f.performCreateOptionsMenu(menu, inflater)) {
                        show = true;
                        if (newMenus == null) {
                            newMenus = new java.util.ArrayList<android.app.Fragment>();
                        }
                        newMenus.add(f);
                    }
                }
            }
        }
        if (mCreatedMenus != null) {
            for (int i = 0; i < mCreatedMenus.size(); i++) {
                android.app.Fragment f = mCreatedMenus.get(i);
                if ((newMenus == null) || (!newMenus.contains(f))) {
                    f.onDestroyOptionsMenu();
                }
            }
        }
        mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(android.view.Menu menu) {
        boolean show = false;
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    if (f.performPrepareOptionsMenu(menu)) {
                        show = true;
                    }
                }
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(android.view.MenuItem item) {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    if (f.performOptionsItemSelected(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(android.view.MenuItem item) {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    if (f.performContextItemSelected(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(android.view.Menu menu) {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    @java.lang.Override
    public void invalidateOptionsMenu() {
        if ((mHost != null) && (mCurState == android.app.Fragment.RESUMED)) {
            mHost.onInvalidateOptionsMenu();
        } else {
            mNeedMenuInvalidate = true;
        }
    }

    public static int reverseTransit(int transit) {
        int rev = 0;
        switch (transit) {
            case android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN :
                rev = android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
                break;
            case android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE :
                rev = android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
                break;
            case android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE :
                rev = android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;
                break;
        }
        return rev;
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        int animAttr = -1;
        switch (transit) {
            case android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN :
                animAttr = (enter) ? com.android.internal.R.styleable.FragmentAnimation_fragmentOpenEnterAnimation : com.android.internal.R.styleable.FragmentAnimation_fragmentOpenExitAnimation;
                break;
            case android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE :
                animAttr = (enter) ? com.android.internal.R.styleable.FragmentAnimation_fragmentCloseEnterAnimation : com.android.internal.R.styleable.FragmentAnimation_fragmentCloseExitAnimation;
                break;
            case android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE :
                animAttr = (enter) ? com.android.internal.R.styleable.FragmentAnimation_fragmentFadeEnterAnimation : com.android.internal.R.styleable.FragmentAnimation_fragmentFadeExitAnimation;
                break;
        }
        return animAttr;
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        if (!"fragment".equals(name)) {
            return null;
        }
        java.lang.String fname = attrs.getAttributeValue(null, "class");
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.Fragment);
        if (fname == null) {
            fname = a.getString(com.android.internal.R.styleable.Fragment_name);
        }
        int id = a.getResourceId(com.android.internal.R.styleable.Fragment_id, android.view.View.NO_ID);
        java.lang.String tag = a.getString(com.android.internal.R.styleable.Fragment_tag);
        a.recycle();
        int containerId = (parent != null) ? parent.getId() : 0;
        if (((containerId == android.view.View.NO_ID) && (id == android.view.View.NO_ID)) && (tag == null)) {
            throw new java.lang.IllegalArgumentException(((attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with") + " an id for ") + fname);
        }
        // If we restored from a previous state, we may already have
        // instantiated this fragment from the state and should use
        // that instance instead of making a new one.
        android.app.Fragment fragment = (id != android.view.View.NO_ID) ? findFragmentById(id) : null;
        if ((fragment == null) && (tag != null)) {
            fragment = findFragmentByTag(tag);
        }
        if ((fragment == null) && (containerId != android.view.View.NO_ID)) {
            fragment = findFragmentById(containerId);
        }
        if (android.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.app.FragmentManagerImpl.TAG, (((("onCreateView: id=0x" + java.lang.Integer.toHexString(id)) + " fname=") + fname) + " existing=") + fragment);

        if (fragment == null) {
            fragment = android.app.Fragment.instantiate(context, fname);
            fragment.mFromLayout = true;
            fragment.mFragmentId = (id != 0) ? id : containerId;
            fragment.mContainerId = containerId;
            fragment.mTag = tag;
            fragment.mInLayout = true;
            fragment.mFragmentManager = this;
            fragment.mHost = mHost;
            fragment.onInflate(mHost.getContext(), attrs, fragment.mSavedFragmentState);
            addFragment(fragment, true);
        } else
            if (fragment.mInLayout) {
                // A fragment already exists and it is not one we restored from
                // previous state.
                throw new java.lang.IllegalArgumentException((((((((attrs.getPositionDescription() + ": Duplicate id 0x") + java.lang.Integer.toHexString(id)) + ", tag ") + tag) + ", or parent id 0x") + java.lang.Integer.toHexString(containerId)) + " with another fragment for ") + fname);
            } else {
                // This fragment was retained from a previous instance; get it
                // going now.
                fragment.mInLayout = true;
                fragment.mHost = mHost;
                // If this fragment is newly instantiated (either right now, or
                // from last saved state), then give it the attributes to
                // initialize itself.
                if (!fragment.mRetaining) {
                    fragment.onInflate(mHost.getContext(), attrs, fragment.mSavedFragmentState);
                }
            }

        // If we haven't finished entering the CREATED state ourselves yet,
        // push the inflated child fragment along.
        if ((mCurState < android.app.Fragment.CREATED) && fragment.mFromLayout) {
            moveToState(fragment, android.app.Fragment.CREATED, 0, 0, false);
        } else {
            moveToState(fragment);
        }
        if (fragment.mView == null) {
            throw new java.lang.IllegalStateException(("Fragment " + fname) + " did not create a view.");
        }
        if (id != 0) {
            fragment.mView.setId(id);
        }
        if (fragment.mView.getTag() == null) {
            fragment.mView.setTag(tag);
        }
        return fragment.mView;
    }

    @java.lang.Override
    public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        return null;
    }

    android.view.LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this;
    }
}

