package android.support.v4.app;


/**
 * Container for fragments associated with an activity.
 */
final class FragmentManagerImpl extends android.support.v4.app.FragmentManager implements android.support.v4.view.LayoutInflaterFactory {
    static boolean DEBUG = false;

    static final java.lang.String TAG = "FragmentManager";

    static final boolean HONEYCOMB = android.os.Build.VERSION.SDK_INT >= 11;

    static final java.lang.String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";

    static final java.lang.String TARGET_STATE_TAG = "android:target_state";

    static final java.lang.String VIEW_STATE_TAG = "android:view_state";

    static final java.lang.String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";

    static class AnimateOnHWLayerIfNeededListener implements android.view.animation.Animation.AnimationListener {
        private android.view.animation.Animation.AnimationListener mOriginalListener;

        private boolean mShouldRunOnHWLayer;

        android.view.View mView;

        public AnimateOnHWLayerIfNeededListener(final android.view.View v, android.view.animation.Animation anim) {
            if ((v == null) || (anim == null)) {
                return;
            }
            mView = v;
        }

        public AnimateOnHWLayerIfNeededListener(final android.view.View v, android.view.animation.Animation anim, android.view.animation.Animation.AnimationListener listener) {
            if ((v == null) || (anim == null)) {
                return;
            }
            mOriginalListener = listener;
            mView = v;
            mShouldRunOnHWLayer = true;
        }

        @java.lang.Override
        @android.support.annotation.CallSuper
        public void onAnimationStart(android.view.animation.Animation animation) {
            if (mOriginalListener != null) {
                mOriginalListener.onAnimationStart(animation);
            }
        }

        @java.lang.Override
        @android.support.annotation.CallSuper
        public void onAnimationEnd(android.view.animation.Animation animation) {
            if ((mView != null) && mShouldRunOnHWLayer) {
                // If we're attached to a window, assume we're in the normal performTraversals
                // drawing path for Animations running. It's not safe to change the layer type
                // during drawing, so post it to the View to run later. If we're not attached
                // or we're running on N and above, post it to the view. If we're not on N and
                // not attached, do it right now since existing platform versions don't run the
                // hwui renderer for detached views off the UI thread making changing layer type
                // safe, but posting may not be.
                // Prior to N posting to a detached view from a non-Looper thread could cause
                // leaks, since the thread-local run queue on a non-Looper thread would never
                // be flushed.
                if (android.support.v4.view.ViewCompat.isAttachedToWindow(mView) || android.support.v4.os.BuildCompat.isAtLeastN()) {
                    mView.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            android.support.v4.view.ViewCompat.setLayerType(mView, android.support.v4.view.ViewCompat.LAYER_TYPE_NONE, null);
                        }
                    });
                } else {
                    android.support.v4.view.ViewCompat.setLayerType(mView, android.support.v4.view.ViewCompat.LAYER_TYPE_NONE, null);
                }
            }
            if (mOriginalListener != null) {
                mOriginalListener.onAnimationEnd(animation);
            }
        }

        @java.lang.Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {
            if (mOriginalListener != null) {
                mOriginalListener.onAnimationRepeat(animation);
            }
        }
    }

    java.util.ArrayList<java.lang.Runnable> mPendingActions;

    java.lang.Runnable[] mTmpActions;

    boolean mExecutingActions;

    java.util.ArrayList<android.support.v4.app.Fragment> mActive;

    java.util.ArrayList<android.support.v4.app.Fragment> mAdded;

    java.util.ArrayList<java.lang.Integer> mAvailIndices;

    java.util.ArrayList<android.support.v4.app.BackStackRecord> mBackStack;

    java.util.ArrayList<android.support.v4.app.Fragment> mCreatedMenus;

    // Must be accessed while locked.
    java.util.ArrayList<android.support.v4.app.BackStackRecord> mBackStackIndices;

    java.util.ArrayList<java.lang.Integer> mAvailBackStackIndices;

    java.util.ArrayList<android.support.v4.app.FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;

    int mCurState = android.support.v4.app.Fragment.INITIALIZING;

    android.support.v4.app.FragmentHostCallback mHost;

    android.support.v4.app.FragmentController mController;

    android.support.v4.app.FragmentContainer mContainer;

    android.support.v4.app.Fragment mParent;

    static java.lang.reflect.Field sAnimationListenerField = null;

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

    static boolean modifiesAlpha(android.view.animation.Animation anim) {
        if (anim instanceof android.view.animation.AlphaAnimation) {
            return true;
        } else
            if (anim instanceof android.view.animation.AnimationSet) {
                java.util.List<android.view.animation.Animation> anims = ((android.view.animation.AnimationSet) (anim)).getAnimations();
                for (int i = 0; i < anims.size(); i++) {
                    if (anims.get(i) instanceof android.view.animation.AlphaAnimation) {
                        return true;
                    }
                }
            }

        return false;
    }

    static boolean shouldRunOnHWLayer(android.view.View v, android.view.animation.Animation anim) {
        return (((android.os.Build.VERSION.SDK_INT >= 19) && (android.support.v4.view.ViewCompat.getLayerType(v) == android.support.v4.view.ViewCompat.LAYER_TYPE_NONE)) && android.support.v4.view.ViewCompat.hasOverlappingRendering(v)) && android.support.v4.app.FragmentManagerImpl.modifiesAlpha(anim);
    }

    private void throwException(java.lang.RuntimeException ex) {
        android.util.Log.e(android.support.v4.app.FragmentManagerImpl.TAG, ex.getMessage());
        android.util.Log.e(android.support.v4.app.FragmentManagerImpl.TAG, "Activity state:");
        android.support.v4.util.LogWriter logw = new android.support.v4.util.LogWriter(android.support.v4.app.FragmentManagerImpl.TAG);
        java.io.PrintWriter pw = new java.io.PrintWriter(logw);
        if (mHost != null) {
            try {
                mHost.onDump("  ", null, pw, new java.lang.String[]{  });
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.support.v4.app.FragmentManagerImpl.TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, pw, new java.lang.String[]{  });
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.support.v4.app.FragmentManagerImpl.TAG, "Failed dumping state", e);
            }
        }
        throw ex;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction beginTransaction() {
        return new android.support.v4.app.BackStackRecord(this);
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
    public android.support.v4.app.FragmentManager.BackStackEntry getBackStackEntryAt(int index) {
        return mBackStack.get(index);
    }

    @java.lang.Override
    public void addOnBackStackChangedListener(android.support.v4.app.FragmentManager.OnBackStackChangedListener listener) {
        if (mBackStackChangeListeners == null) {
            mBackStackChangeListeners = new java.util.ArrayList<android.support.v4.app.FragmentManager.OnBackStackChangedListener>();
        }
        mBackStackChangeListeners.add(listener);
    }

    @java.lang.Override
    public void removeOnBackStackChangedListener(android.support.v4.app.FragmentManager.OnBackStackChangedListener listener) {
        if (mBackStackChangeListeners != null) {
            mBackStackChangeListeners.remove(listener);
        }
    }

    @java.lang.Override
    public void putFragment(android.os.Bundle bundle, java.lang.String key, android.support.v4.app.Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new java.lang.IllegalStateException(("Fragment " + fragment) + " is not currently in the FragmentManager"));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    @java.lang.Override
    public android.support.v4.app.Fragment getFragment(android.os.Bundle bundle, java.lang.String key) {
        int index = bundle.getInt(key, -1);
        if (index == (-1)) {
            return null;
        }
        if (index >= mActive.size()) {
            throwException(new java.lang.IllegalStateException((("Fragment no longer exists for key " + key) + ": index ") + index));
        }
        android.support.v4.app.Fragment f = mActive.get(index);
        if (f == null) {
            throwException(new java.lang.IllegalStateException((("Fragment no longer exists for key " + key) + ": index ") + index));
        }
        return f;
    }

    @java.lang.Override
    public java.util.List<android.support.v4.app.Fragment> getFragments() {
        return mActive;
    }

    @java.lang.Override
    public android.support.v4.app.Fragment.SavedState saveFragmentInstanceState(android.support.v4.app.Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new java.lang.IllegalStateException(("Fragment " + fragment) + " is not currently in the FragmentManager"));
        }
        if (fragment.mState > android.support.v4.app.Fragment.INITIALIZING) {
            android.os.Bundle result = saveFragmentBasicState(fragment);
            return result != null ? new android.support.v4.app.Fragment.SavedState(result) : null;
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
            android.support.v4.util.DebugUtils.buildShortClassTag(mParent, sb);
        } else {
            android.support.v4.util.DebugUtils.buildShortClassTag(mHost, sb);
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
                    android.support.v4.app.Fragment f = mActive.get(i);
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
                    android.support.v4.app.Fragment f = mAdded.get(i);
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
                    android.support.v4.app.Fragment f = mCreatedMenus.get(i);
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
                    android.support.v4.app.BackStackRecord bs = mBackStack.get(i);
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
                        android.support.v4.app.BackStackRecord bs = mBackStackIndices.get(i);
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

    static final android.view.animation.Interpolator DECELERATE_QUINT = new android.view.animation.DecelerateInterpolator(2.5F);

    static final android.view.animation.Interpolator DECELERATE_CUBIC = new android.view.animation.DecelerateInterpolator(1.5F);

    static final android.view.animation.Interpolator ACCELERATE_QUINT = new android.view.animation.AccelerateInterpolator(2.5F);

    static final android.view.animation.Interpolator ACCELERATE_CUBIC = new android.view.animation.AccelerateInterpolator(1.5F);

    static final int ANIM_DUR = 220;

    static android.view.animation.Animation makeOpenCloseAnimation(android.content.Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
        android.view.animation.AnimationSet set = new android.view.animation.AnimationSet(false);
        android.view.animation.ScaleAnimation scale = new android.view.animation.ScaleAnimation(startScale, endScale, startScale, endScale, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F);
        scale.setInterpolator(android.support.v4.app.FragmentManagerImpl.DECELERATE_QUINT);
        scale.setDuration(android.support.v4.app.FragmentManagerImpl.ANIM_DUR);
        set.addAnimation(scale);
        android.view.animation.AlphaAnimation alpha = new android.view.animation.AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(android.support.v4.app.FragmentManagerImpl.DECELERATE_CUBIC);
        alpha.setDuration(android.support.v4.app.FragmentManagerImpl.ANIM_DUR);
        set.addAnimation(alpha);
        return set;
    }

    static android.view.animation.Animation makeFadeAnimation(android.content.Context context, float start, float end) {
        android.view.animation.AlphaAnimation anim = new android.view.animation.AlphaAnimation(start, end);
        anim.setInterpolator(android.support.v4.app.FragmentManagerImpl.DECELERATE_CUBIC);
        anim.setDuration(android.support.v4.app.FragmentManagerImpl.ANIM_DUR);
        return anim;
    }

    android.view.animation.Animation loadAnimation(android.support.v4.app.Fragment fragment, int transit, boolean enter, int transitionStyle) {
        android.view.animation.Animation animObj = fragment.onCreateAnimation(transit, enter, fragment.mNextAnim);
        if (animObj != null) {
            return animObj;
        }
        if (fragment.mNextAnim != 0) {
            android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mHost.getContext(), fragment.mNextAnim);
            if (anim != null) {
                return anim;
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = android.support.v4.app.FragmentManagerImpl.transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        switch (styleIndex) {
            case android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_OPEN_ENTER :
                return android.support.v4.app.FragmentManagerImpl.makeOpenCloseAnimation(mHost.getContext(), 1.125F, 1.0F, 0, 1);
            case android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_OPEN_EXIT :
                return android.support.v4.app.FragmentManagerImpl.makeOpenCloseAnimation(mHost.getContext(), 1.0F, 0.975F, 1, 0);
            case android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_CLOSE_ENTER :
                return android.support.v4.app.FragmentManagerImpl.makeOpenCloseAnimation(mHost.getContext(), 0.975F, 1.0F, 0, 1);
            case android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_CLOSE_EXIT :
                return android.support.v4.app.FragmentManagerImpl.makeOpenCloseAnimation(mHost.getContext(), 1.0F, 1.075F, 1, 0);
            case android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_FADE_ENTER :
                return android.support.v4.app.FragmentManagerImpl.makeFadeAnimation(mHost.getContext(), 0, 1);
            case android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_FADE_EXIT :
                return android.support.v4.app.FragmentManagerImpl.makeFadeAnimation(mHost.getContext(), 1, 0);
        }
        if ((transitionStyle == 0) && mHost.onHasWindowAnimations()) {
            transitionStyle = mHost.onGetWindowAnimations();
        }
        if (transitionStyle == 0) {
            return null;
        }
        // TypedArray attrs = mActivity.obtainStyledAttributes(transitionStyle,
        // com.android.internal.R.styleable.FragmentAnimation);
        // int anim = attrs.getResourceId(styleIndex, 0);
        // attrs.recycle();
        // if (anim == 0) {
        // return null;
        // }
        // return AnimatorInflater.loadAnimator(mActivity, anim);
        return null;
    }

    public void performPendingDeferredStart(android.support.v4.app.Fragment f) {
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

    /**
     * Sets the to be animated view on hardware layer during the animation. Note
     * that calling this will replace any existing animation listener on the animation
     * with a new one, as animations do not support more than one listeners. Therefore,
     * animations that already have listeners should do the layer change operations
     * in their existing listeners, rather than calling this function.
     */
    private void setHWLayerAnimListenerIfAlpha(final android.view.View v, android.view.animation.Animation anim) {
        if ((v == null) || (anim == null)) {
            return;
        }
        if (android.support.v4.app.FragmentManagerImpl.shouldRunOnHWLayer(v, anim)) {
            android.view.animation.Animation.AnimationListener originalListener = null;
            try {
                if (android.support.v4.app.FragmentManagerImpl.sAnimationListenerField == null) {
                    android.support.v4.app.FragmentManagerImpl.sAnimationListenerField = android.view.animation.Animation.class.getDeclaredField("mListener");
                    android.support.v4.app.FragmentManagerImpl.sAnimationListenerField.setAccessible(true);
                }
                originalListener = ((android.view.animation.Animation.AnimationListener) (android.support.v4.app.FragmentManagerImpl.sAnimationListenerField.get(anim)));
            } catch (java.lang.NoSuchFieldException e) {
                android.util.Log.e(android.support.v4.app.FragmentManagerImpl.TAG, "No field with the name mListener is found in Animation class", e);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e(android.support.v4.app.FragmentManagerImpl.TAG, "Cannot access Animation's mListener field", e);
            }
            // If there's already a listener set on the animation, we need wrap the new listener
            // around the existing listener, so that they will both get animation listener
            // callbacks.
            android.support.v4.view.ViewCompat.setLayerType(v, android.support.v4.view.ViewCompat.LAYER_TYPE_HARDWARE, null);
            anim.setAnimationListener(new android.support.v4.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(v, anim, originalListener));
        }
    }

    boolean isStateAtLeast(int state) {
        return mCurState >= state;
    }

    void moveToState(android.support.v4.app.Fragment f, int newState, int transit, int transitionStyle, boolean keepActive) {
        // Fragments that are not currently added will sit in the onCreate() state.
        if (((!f.mAdded) || f.mDetached) && (newState > android.support.v4.app.Fragment.CREATED)) {
            newState = android.support.v4.app.Fragment.CREATED;
        }
        if (f.mRemoving && (newState > f.mState)) {
            // While removing a fragment, we can't change it to a higher state.
            newState = f.mState;
        }
        // Defer start if requested; don't allow it to move to STARTED or higher
        // if it's not already started.
        if ((f.mDeferStart && (f.mState < android.support.v4.app.Fragment.STARTED)) && (newState > android.support.v4.app.Fragment.STOPPED)) {
            newState = android.support.v4.app.Fragment.STOPPED;
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
                case android.support.v4.app.Fragment.INITIALIZING :
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "moveto CREATED: " + f);

                    if (f.mSavedFragmentState != null) {
                        f.mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
                        f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(android.support.v4.app.FragmentManagerImpl.VIEW_STATE_TAG);
                        f.mTarget = getFragment(f.mSavedFragmentState, android.support.v4.app.FragmentManagerImpl.TARGET_STATE_TAG);
                        if (f.mTarget != null) {
                            f.mTargetRequestCode = f.mSavedFragmentState.getInt(android.support.v4.app.FragmentManagerImpl.TARGET_REQUEST_CODE_STATE_TAG, 0);
                        }
                        f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(android.support.v4.app.FragmentManagerImpl.USER_VISIBLE_HINT_TAG, true);
                        if (!f.mUserVisibleHint) {
                            f.mDeferStart = true;
                            if (newState > android.support.v4.app.Fragment.STOPPED) {
                                newState = android.support.v4.app.Fragment.STOPPED;
                            }
                        }
                    }
                    f.mHost = mHost;
                    f.mParentFragment = mParent;
                    f.mFragmentManager = (mParent != null) ? mParent.mChildFragmentManager : mHost.getFragmentManagerImpl();
                    f.mCalled = false;
                    f.onAttach(mHost.getContext());
                    if (!f.mCalled) {
                        throw new android.support.v4.app.SuperNotCalledException(("Fragment " + f) + " did not call through to super.onAttach()");
                    }
                    if (f.mParentFragment == null) {
                        mHost.onAttachFragment(f);
                    } else {
                        f.mParentFragment.onAttachFragment(f);
                    }
                    if (!f.mRetaining) {
                        f.performCreate(f.mSavedFragmentState);
                    } else {
                        f.restoreChildFragmentState(f.mSavedFragmentState);
                        f.mState = android.support.v4.app.Fragment.CREATED;
                    }
                    f.mRetaining = false;
                    if (f.mFromLayout) {
                        // For fragments that are part of the content view
                        // layout, we need to instantiate the view immediately
                        // and the inflater will take care of adding it.
                        f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
                        if (f.mView != null) {
                            f.mInnerView = f.mView;
                            if (android.os.Build.VERSION.SDK_INT >= 11) {
                                android.support.v4.view.ViewCompat.setSaveFromParentEnabled(f.mView, false);
                            } else {
                                f.mView = android.support.v4.app.NoSaveStateFrameLayout.wrap(f.mView);
                            }
                            if (f.mHidden)
                                f.mView.setVisibility(android.view.View.GONE);

                            f.onViewCreated(f.mView, f.mSavedFragmentState);
                        } else {
                            f.mInnerView = null;
                        }
                    }
                case android.support.v4.app.Fragment.CREATED :
                    if (newState > android.support.v4.app.Fragment.CREATED) {
                        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "moveto ACTIVITY_CREATED: " + f);

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
                                f.mInnerView = f.mView;
                                if (android.os.Build.VERSION.SDK_INT >= 11) {
                                    android.support.v4.view.ViewCompat.setSaveFromParentEnabled(f.mView, false);
                                } else {
                                    f.mView = android.support.v4.app.NoSaveStateFrameLayout.wrap(f.mView);
                                }
                                if (container != null) {
                                    android.view.animation.Animation anim = loadAnimation(f, transit, true, transitionStyle);
                                    if (anim != null) {
                                        setHWLayerAnimListenerIfAlpha(f.mView, anim);
                                        f.mView.startAnimation(anim);
                                    }
                                    container.addView(f.mView);
                                }
                                if (f.mHidden)
                                    f.mView.setVisibility(android.view.View.GONE);

                                f.onViewCreated(f.mView, f.mSavedFragmentState);
                            } else {
                                f.mInnerView = null;
                            }
                        }
                        f.performActivityCreated(f.mSavedFragmentState);
                        if (f.mView != null) {
                            f.restoreViewState(f.mSavedFragmentState);
                        }
                        f.mSavedFragmentState = null;
                    }
                case android.support.v4.app.Fragment.ACTIVITY_CREATED :
                    if (newState > android.support.v4.app.Fragment.ACTIVITY_CREATED) {
                        f.mState = android.support.v4.app.Fragment.STOPPED;
                    }
                case android.support.v4.app.Fragment.STOPPED :
                    if (newState > android.support.v4.app.Fragment.STOPPED) {
                        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "moveto STARTED: " + f);

                        f.performStart();
                    }
                case android.support.v4.app.Fragment.STARTED :
                    if (newState > android.support.v4.app.Fragment.STARTED) {
                        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "moveto RESUMED: " + f);

                        f.performResume();
                        f.mSavedFragmentState = null;
                        f.mSavedViewState = null;
                    }
            }
        } else
            if (f.mState > newState) {
                switch (f.mState) {
                    case android.support.v4.app.Fragment.RESUMED :
                        if (newState < android.support.v4.app.Fragment.RESUMED) {
                            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "movefrom RESUMED: " + f);

                            f.performPause();
                        }
                    case android.support.v4.app.Fragment.STARTED :
                        if (newState < android.support.v4.app.Fragment.STARTED) {
                            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "movefrom STARTED: " + f);

                            f.performStop();
                        }
                    case android.support.v4.app.Fragment.STOPPED :
                        if (newState < android.support.v4.app.Fragment.STOPPED) {
                            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "movefrom STOPPED: " + f);

                            f.performReallyStop();
                        }
                    case android.support.v4.app.Fragment.ACTIVITY_CREATED :
                        if (newState < android.support.v4.app.Fragment.ACTIVITY_CREATED) {
                            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "movefrom ACTIVITY_CREATED: " + f);

                            if (f.mView != null) {
                                // Need to save the current view state if not
                                // done already.
                                if (mHost.onShouldSaveFragmentState(f) && (f.mSavedViewState == null)) {
                                    saveFragmentViewState(f);
                                }
                            }
                            f.performDestroyView();
                            if ((f.mView != null) && (f.mContainer != null)) {
                                android.view.animation.Animation anim = null;
                                if ((mCurState > android.support.v4.app.Fragment.INITIALIZING) && (!mDestroyed)) {
                                    anim = loadAnimation(f, transit, false, transitionStyle);
                                }
                                if (anim != null) {
                                    final android.support.v4.app.Fragment fragment = f;
                                    f.mAnimatingAway = f.mView;
                                    f.mStateAfterAnimating = newState;
                                    final android.view.View viewToAnimate = f.mView;
                                    anim.setAnimationListener(new android.support.v4.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(viewToAnimate, anim) {
                                        @java.lang.Override
                                        public void onAnimationEnd(android.view.animation.Animation animation) {
                                            super.onAnimationEnd(animation);
                                            if (fragment.mAnimatingAway != null) {
                                                fragment.mAnimatingAway = null;
                                                moveToState(fragment, fragment.mStateAfterAnimating, 0, 0, false);
                                            }
                                        }
                                    });
                                    f.mView.startAnimation(anim);
                                }
                                f.mContainer.removeView(f.mView);
                            }
                            f.mContainer = null;
                            f.mView = null;
                            f.mInnerView = null;
                        }
                    case android.support.v4.app.Fragment.CREATED :
                        if (newState < android.support.v4.app.Fragment.CREATED) {
                            if (mDestroyed) {
                                if (f.mAnimatingAway != null) {
                                    // The fragment's containing activity is
                                    // being destroyed, but this fragment is
                                    // currently animating away.  Stop the
                                    // animation right now -- it is not needed,
                                    // and we can't wait any more on destroying
                                    // the fragment.
                                    android.view.View v = f.mAnimatingAway;
                                    f.mAnimatingAway = null;
                                    v.clearAnimation();
                                }
                            }
                            if (f.mAnimatingAway != null) {
                                // We are waiting for the fragment's view to finish
                                // animating away.  Just make a note of the state
                                // the fragment now should move to once the animation
                                // is done.
                                f.mStateAfterAnimating = newState;
                                newState = android.support.v4.app.Fragment.CREATED;
                            } else {
                                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "movefrom CREATED: " + f);

                                if (!f.mRetaining) {
                                    f.performDestroy();
                                } else {
                                    f.mState = android.support.v4.app.Fragment.INITIALIZING;
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
            android.util.Log.w(android.support.v4.app.FragmentManagerImpl.TAG, ((((("moveToState: Fragment state for " + f) + " not updated inline; ") + "expected state ") + newState) + " found ") + f.mState);
            f.mState = newState;
        }
    }

    void moveToState(android.support.v4.app.Fragment f) {
        moveToState(f, mCurState, 0, 0, false);
    }

    void moveToState(int newState, boolean always) {
        moveToState(newState, 0, 0, always);
    }

    void moveToState(int newState, int transit, int transitStyle, boolean always) {
        if ((mHost == null) && (newState != android.support.v4.app.Fragment.INITIALIZING)) {
            throw new java.lang.IllegalStateException("No host");
        }
        if ((!always) && (mCurState == newState)) {
            return;
        }
        mCurState = newState;
        if (mActive != null) {
            boolean loadersRunning = false;
            for (int i = 0; i < mActive.size(); i++) {
                android.support.v4.app.Fragment f = mActive.get(i);
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
            if ((mNeedMenuInvalidate && (mHost != null)) && (mCurState == android.support.v4.app.Fragment.RESUMED)) {
                mHost.onSupportInvalidateOptionsMenu();
                mNeedMenuInvalidate = false;
            }
        }
    }

    void startPendingDeferredFragments() {
        if (mActive == null)
            return;

        for (int i = 0; i < mActive.size(); i++) {
            android.support.v4.app.Fragment f = mActive.get(i);
            if (f != null) {
                performPendingDeferredStart(f);
            }
        }
    }

    void makeActive(android.support.v4.app.Fragment f) {
        if (f.mIndex >= 0) {
            return;
        }
        if ((mAvailIndices == null) || (mAvailIndices.size() <= 0)) {
            if (mActive == null) {
                mActive = new java.util.ArrayList<android.support.v4.app.Fragment>();
            }
            f.setIndex(mActive.size(), mParent);
            mActive.add(f);
        } else {
            f.setIndex(mAvailIndices.remove(mAvailIndices.size() - 1), mParent);
            mActive.set(f.mIndex, f);
        }
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "Allocated fragment index " + f);

    }

    void makeInactive(android.support.v4.app.Fragment f) {
        if (f.mIndex < 0) {
            return;
        }
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "Freeing fragment index " + f);

        mActive.set(f.mIndex, null);
        if (mAvailIndices == null) {
            mAvailIndices = new java.util.ArrayList<java.lang.Integer>();
        }
        mAvailIndices.add(f.mIndex);
        mHost.inactivateFragment(f.mWho);
        f.initState();
    }

    public void addFragment(android.support.v4.app.Fragment fragment, boolean moveToStateNow) {
        if (mAdded == null) {
            mAdded = new java.util.ArrayList<android.support.v4.app.Fragment>();
        }
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "add: " + fragment);

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

    public void removeFragment(android.support.v4.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("remove: " + fragment) + " nesting=") + fragment.mBackStackNesting);

        final boolean inactive = !fragment.isInBackStack();
        if ((!fragment.mDetached) || inactive) {
            if (mAdded != null) {
                mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
            moveToState(fragment, inactive ? android.support.v4.app.Fragment.INITIALIZING : android.support.v4.app.Fragment.CREATED, transition, transitionStyle, false);
        }
    }

    public void hideFragment(android.support.v4.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "hide: " + fragment);

        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mView != null) {
                android.view.animation.Animation anim = loadAnimation(fragment, transition, false, transitionStyle);
                if (anim != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    fragment.mView.startAnimation(anim);
                }
                fragment.mView.setVisibility(android.view.View.GONE);
            }
            if ((fragment.mAdded && fragment.mHasMenu) && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(true);
        }
    }

    public void showFragment(android.support.v4.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "show: " + fragment);

        if (fragment.mHidden) {
            fragment.mHidden = false;
            if (fragment.mView != null) {
                android.view.animation.Animation anim = loadAnimation(fragment, transition, true, transitionStyle);
                if (anim != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    fragment.mView.startAnimation(anim);
                }
                fragment.mView.setVisibility(android.view.View.VISIBLE);
            }
            if ((fragment.mAdded && fragment.mHasMenu) && fragment.mMenuVisible) {
                mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(false);
        }
    }

    public void detachFragment(android.support.v4.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "detach: " + fragment);

        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                // We are not already in back stack, so need to remove the fragment.
                if (mAdded != null) {
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "remove from detach: " + fragment);

                    mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
                moveToState(fragment, android.support.v4.app.Fragment.CREATED, transition, transitionStyle, false);
            }
        }
    }

    public void attachFragment(android.support.v4.app.Fragment fragment, int transition, int transitionStyle) {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "attach: " + fragment);

        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                if (mAdded == null) {
                    mAdded = new java.util.ArrayList<android.support.v4.app.Fragment>();
                }
                if (mAdded.contains(fragment)) {
                    throw new java.lang.IllegalStateException("Fragment already added: " + fragment);
                }
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "add from attach: " + fragment);

                mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    mNeedMenuInvalidate = true;
                }
                moveToState(fragment, mCurState, transition, transitionStyle, false);
            }
        }
    }

    @java.lang.Override
    public android.support.v4.app.Fragment findFragmentById(int id) {
        if (mAdded != null) {
            // First look through added fragments.
            for (int i = mAdded.size() - 1; i >= 0; i--) {
                android.support.v4.app.Fragment f = mAdded.get(i);
                if ((f != null) && (f.mFragmentId == id)) {
                    return f;
                }
            }
        }
        if (mActive != null) {
            // Now for any known fragment.
            for (int i = mActive.size() - 1; i >= 0; i--) {
                android.support.v4.app.Fragment f = mActive.get(i);
                if ((f != null) && (f.mFragmentId == id)) {
                    return f;
                }
            }
        }
        return null;
    }

    @java.lang.Override
    public android.support.v4.app.Fragment findFragmentByTag(java.lang.String tag) {
        if ((mAdded != null) && (tag != null)) {
            // First look through added fragments.
            for (int i = mAdded.size() - 1; i >= 0; i--) {
                android.support.v4.app.Fragment f = mAdded.get(i);
                if ((f != null) && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if ((mActive != null) && (tag != null)) {
            // Now for any known fragment.
            for (int i = mActive.size() - 1; i >= 0; i--) {
                android.support.v4.app.Fragment f = mActive.get(i);
                if ((f != null) && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        return null;
    }

    public android.support.v4.app.Fragment findFragmentByWho(java.lang.String who) {
        if ((mActive != null) && (who != null)) {
            for (int i = mActive.size() - 1; i >= 0; i--) {
                android.support.v4.app.Fragment f = mActive.get(i);
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

    public int allocBackStackIndex(android.support.v4.app.BackStackRecord bse) {
        synchronized(this) {
            if ((mAvailBackStackIndices == null) || (mAvailBackStackIndices.size() <= 0)) {
                if (mBackStackIndices == null) {
                    mBackStackIndices = new java.util.ArrayList<android.support.v4.app.BackStackRecord>();
                }
                int index = mBackStackIndices.size();
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("Setting back stack index " + index) + " to ") + bse);

                mBackStackIndices.add(bse);
                return index;
            } else {
                int index = mAvailBackStackIndices.remove(mAvailBackStackIndices.size() - 1);
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("Adding back stack index " + index) + " with ") + bse);

                mBackStackIndices.set(index, bse);
                return index;
            }
        }
    }

    public void setBackStackIndex(int index, android.support.v4.app.BackStackRecord bse) {
        synchronized(this) {
            if (mBackStackIndices == null) {
                mBackStackIndices = new java.util.ArrayList<android.support.v4.app.BackStackRecord>();
            }
            int N = mBackStackIndices.size();
            if (index < N) {
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("Setting back stack index " + index) + " to ") + bse);

                mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    mBackStackIndices.add(null);
                    if (mAvailBackStackIndices == null) {
                        mAvailBackStackIndices = new java.util.ArrayList<java.lang.Integer>();
                    }
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "Adding available back stack index " + N);

                    mAvailBackStackIndices.add(N);
                    N++;
                } 
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("Adding back stack index " + index) + " with ") + bse);

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
            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "Freeing back stack index " + index);

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
            throw new java.lang.IllegalStateException("FragmentManager is already executing transactions");
        }
        if (android.os.Looper.myLooper() != mHost.getHandler().getLooper()) {
            throw new java.lang.IllegalStateException("Must be called from main thread of fragment host");
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
                android.support.v4.app.Fragment f = mActive.get(i);
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

    void addBackStackState(android.support.v4.app.BackStackRecord state) {
        if (mBackStack == null) {
            mBackStack = new java.util.ArrayList<android.support.v4.app.BackStackRecord>();
        }
        mBackStack.add(state);
        reportBackStackChanged();
    }

    @java.lang.SuppressWarnings("unused")
    boolean popBackStackState(android.os.Handler handler, java.lang.String name, int id, int flags) {
        if (mBackStack == null) {
            return false;
        }
        if (((name == null) && (id < 0)) && ((flags & android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE) == 0)) {
            int last = mBackStack.size() - 1;
            if (last < 0) {
                return false;
            }
            final android.support.v4.app.BackStackRecord bss = mBackStack.remove(last);
            android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments = new android.util.SparseArray<android.support.v4.app.Fragment>();
            android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments = new android.util.SparseArray<android.support.v4.app.Fragment>();
            if (mCurState >= android.support.v4.app.Fragment.CREATED) {
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
                    android.support.v4.app.BackStackRecord bss = mBackStack.get(index);
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
                if ((flags & android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE) != 0) {
                    index--;
                    // Consume all following entries that match.
                    while (index >= 0) {
                        android.support.v4.app.BackStackRecord bss = mBackStack.get(index);
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
            final java.util.ArrayList<android.support.v4.app.BackStackRecord> states = new java.util.ArrayList<android.support.v4.app.BackStackRecord>();
            for (int i = mBackStack.size() - 1; i > index; i--) {
                states.add(mBackStack.remove(i));
            }
            final int LAST = states.size() - 1;
            android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments = new android.util.SparseArray<android.support.v4.app.Fragment>();
            android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments = new android.util.SparseArray<android.support.v4.app.Fragment>();
            if (mCurState >= android.support.v4.app.Fragment.CREATED) {
                for (int i = 0; i <= LAST; i++) {
                    states.get(i).calculateBackFragments(firstOutFragments, lastInFragments);
                }
            }
            android.support.v4.app.BackStackRecord.TransitionState state = null;
            for (int i = 0; i <= LAST; i++) {
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "Popping back stack state: " + states.get(i));

                state = states.get(i).popFromBackStack(i == LAST, state, firstOutFragments, lastInFragments);
            }
            reportBackStackChanged();
        }
        return true;
    }

    android.support.v4.app.FragmentManagerNonConfig retainNonConfig() {
        java.util.ArrayList<android.support.v4.app.Fragment> fragments = null;
        java.util.ArrayList<android.support.v4.app.FragmentManagerNonConfig> childFragments = null;
        if (mActive != null) {
            for (int i = 0; i < mActive.size(); i++) {
                android.support.v4.app.Fragment f = mActive.get(i);
                if (f != null) {
                    if (f.mRetainInstance) {
                        if (fragments == null) {
                            fragments = new java.util.ArrayList<android.support.v4.app.Fragment>();
                        }
                        fragments.add(f);
                        f.mRetaining = true;
                        f.mTargetIndex = (f.mTarget != null) ? f.mTarget.mIndex : -1;
                        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "retainNonConfig: keeping retained " + f);

                    }
                    boolean addedChild = false;
                    if (f.mChildFragmentManager != null) {
                        android.support.v4.app.FragmentManagerNonConfig child = f.mChildFragmentManager.retainNonConfig();
                        if (child != null) {
                            if (childFragments == null) {
                                childFragments = new java.util.ArrayList<android.support.v4.app.FragmentManagerNonConfig>();
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
        return new android.support.v4.app.FragmentManagerNonConfig(fragments, childFragments);
    }

    void saveFragmentViewState(android.support.v4.app.Fragment f) {
        if (f.mInnerView == null) {
            return;
        }
        if (mStateArray == null) {
            mStateArray = new android.util.SparseArray<android.os.Parcelable>();
        } else {
            mStateArray.clear();
        }
        f.mInnerView.saveHierarchyState(mStateArray);
        if (mStateArray.size() > 0) {
            f.mSavedViewState = mStateArray;
            mStateArray = null;
        }
    }

    android.os.Bundle saveFragmentBasicState(android.support.v4.app.Fragment f) {
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
            result.putSparseParcelableArray(android.support.v4.app.FragmentManagerImpl.VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new android.os.Bundle();
            }
            // Only add this if it's not the default value
            result.putBoolean(android.support.v4.app.FragmentManagerImpl.USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    android.os.Parcelable saveAllState() {
        // Make sure all pending operations have now been executed to get
        // our state update-to-date.
        execPendingActions();
        if (android.support.v4.app.FragmentManagerImpl.HONEYCOMB) {
            // As of Honeycomb, we save state after pausing.  Prior to that
            // it is before pausing.  With fragments this is an issue, since
            // there are many things you may do after pausing but before
            // stopping that change the fragment state.  For those older
            // devices, we will not at this point say that we have saved
            // the state, so we will allow them to continue doing fragment
            // transactions.  This retains the same semantics as Honeycomb,
            // though you do have the risk of losing the very most recent state
            // if the process is killed...  we'll live with that.
            mStateSaved = true;
        }
        if ((mActive == null) || (mActive.size() <= 0)) {
            return null;
        }
        // First collect all active fragments.
        int N = mActive.size();
        android.support.v4.app.FragmentState[] active = new android.support.v4.app.FragmentState[N];
        boolean haveFragments = false;
        for (int i = 0; i < N; i++) {
            android.support.v4.app.Fragment f = mActive.get(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    throwException(new java.lang.IllegalStateException((("Failure saving state: active " + f) + " has cleared index: ") + f.mIndex));
                }
                haveFragments = true;
                android.support.v4.app.FragmentState fs = new android.support.v4.app.FragmentState(f);
                active[i] = fs;
                if ((f.mState > android.support.v4.app.Fragment.INITIALIZING) && (fs.mSavedFragmentState == null)) {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            throwException(new java.lang.IllegalStateException((("Failure saving state: " + f) + " has target not in fragment manager: ") + f.mTarget));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new android.os.Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, android.support.v4.app.FragmentManagerImpl.TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(android.support.v4.app.FragmentManagerImpl.TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                } else {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                }
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("Saved state of " + f) + ": ") + fs.mSavedFragmentState);

            }
        }
        if (!haveFragments) {
            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "saveAllState: no fragments!");

            return null;
        }
        int[] added = null;
        android.support.v4.app.BackStackState[] backStack = null;
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
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("saveAllState: adding fragment #" + i) + ": ") + mAdded.get(i));

                }
            }
        }
        // Now save back stack.
        if (mBackStack != null) {
            N = mBackStack.size();
            if (N > 0) {
                backStack = new android.support.v4.app.BackStackState[N];
                for (int i = 0; i < N; i++) {
                    backStack[i] = new android.support.v4.app.BackStackState(mBackStack.get(i));
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("saveAllState: adding back stack #" + i) + ": ") + mBackStack.get(i));

                }
            }
        }
        android.support.v4.app.FragmentManagerState fms = new android.support.v4.app.FragmentManagerState();
        fms.mActive = active;
        fms.mAdded = added;
        fms.mBackStack = backStack;
        return fms;
    }

    void restoreAllState(android.os.Parcelable state, android.support.v4.app.FragmentManagerNonConfig nonConfig) {
        // If there is no saved state at all, then there can not be
        // any nonConfig fragments either, so that is that.
        if (state == null)
            return;

        android.support.v4.app.FragmentManagerState fms = ((android.support.v4.app.FragmentManagerState) (state));
        if (fms.mActive == null)
            return;

        java.util.List<android.support.v4.app.FragmentManagerNonConfig> childNonConfigs = null;
        // First re-attach any non-config instances we are retaining back
        // to their saved state, so we don't try to instantiate them again.
        if (nonConfig != null) {
            java.util.List<android.support.v4.app.Fragment> nonConfigFragments = nonConfig.getFragments();
            childNonConfigs = nonConfig.getChildNonConfigs();
            final int count = (nonConfigFragments != null) ? nonConfigFragments.size() : 0;
            for (int i = 0; i < count; i++) {
                android.support.v4.app.Fragment f = nonConfigFragments.get(i);
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "restoreAllState: re-attaching retained " + f);

                android.support.v4.app.FragmentState fs = fms.mActive[f.mIndex];
                fs.mInstance = f;
                f.mSavedViewState = null;
                f.mBackStackNesting = 0;
                f.mInLayout = false;
                f.mAdded = false;
                f.mTarget = null;
                if (fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
                    f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(android.support.v4.app.FragmentManagerImpl.VIEW_STATE_TAG);
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
            android.support.v4.app.FragmentState fs = fms.mActive[i];
            if (fs != null) {
                android.support.v4.app.FragmentManagerNonConfig childNonConfig = null;
                if ((childNonConfigs != null) && (i < childNonConfigs.size())) {
                    childNonConfig = childNonConfigs.get(i);
                }
                android.support.v4.app.Fragment f = fs.instantiate(mHost, mParent, childNonConfig);
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("restoreAllState: active #" + i) + ": ") + f);

                mActive.add(f);
                // Now that the fragment is instantiated (or came from being
                // retained above), clear mInstance in case we end up re-restoring
                // from this FragmentState again.
                fs.mInstance = null;
            } else {
                mActive.add(null);
                if (mAvailIndices == null) {
                    mAvailIndices = new java.util.ArrayList<java.lang.Integer>();
                }
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, "restoreAllState: avail #" + i);

                mAvailIndices.add(i);
            }
        }
        // Update the target of all retained fragments.
        if (nonConfig != null) {
            java.util.List<android.support.v4.app.Fragment> nonConfigFragments = nonConfig.getFragments();
            final int count = (nonConfigFragments != null) ? nonConfigFragments.size() : 0;
            for (int i = 0; i < count; i++) {
                android.support.v4.app.Fragment f = nonConfigFragments.get(i);
                if (f.mTargetIndex >= 0) {
                    if (f.mTargetIndex < mActive.size()) {
                        f.mTarget = mActive.get(f.mTargetIndex);
                    } else {
                        android.util.Log.w(android.support.v4.app.FragmentManagerImpl.TAG, (("Re-attaching retained fragment " + f) + " target no longer exists: ") + f.mTargetIndex);
                        f.mTarget = null;
                    }
                }
            }
        }
        // Build the list of currently added fragments.
        if (fms.mAdded != null) {
            mAdded = new java.util.ArrayList<android.support.v4.app.Fragment>(fms.mAdded.length);
            for (int i = 0; i < fms.mAdded.length; i++) {
                android.support.v4.app.Fragment f = mActive.get(fms.mAdded[i]);
                if (f == null) {
                    throwException(new java.lang.IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i]));
                }
                f.mAdded = true;
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("restoreAllState: added #" + i) + ": ") + f);

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
            mBackStack = new java.util.ArrayList<android.support.v4.app.BackStackRecord>(fms.mBackStack.length);
            for (int i = 0; i < fms.mBackStack.length; i++) {
                android.support.v4.app.BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                if (android.support.v4.app.FragmentManagerImpl.DEBUG) {
                    android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (((("restoreAllState: back stack #" + i) + " (index ") + bse.mIndex) + "): ") + bse);
                    android.support.v4.util.LogWriter logw = new android.support.v4.util.LogWriter(android.support.v4.app.FragmentManagerImpl.TAG);
                    java.io.PrintWriter pw = new java.io.PrintWriter(logw);
                    bse.dump("  ", pw, false);
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

    public void attachController(android.support.v4.app.FragmentHostCallback host, android.support.v4.app.FragmentContainer container, android.support.v4.app.Fragment parent) {
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
        moveToState(android.support.v4.app.Fragment.CREATED, false);
    }

    public void dispatchActivityCreated() {
        mStateSaved = false;
        moveToState(android.support.v4.app.Fragment.ACTIVITY_CREATED, false);
    }

    public void dispatchStart() {
        mStateSaved = false;
        moveToState(android.support.v4.app.Fragment.STARTED, false);
    }

    public void dispatchResume() {
        mStateSaved = false;
        moveToState(android.support.v4.app.Fragment.RESUMED, false);
    }

    public void dispatchPause() {
        moveToState(android.support.v4.app.Fragment.STARTED, false);
    }

    public void dispatchStop() {
        // See saveAllState() for the explanation of this.  We do this for
        // all platform versions, to keep our behavior more consistent between
        // them.
        mStateSaved = true;
        moveToState(android.support.v4.app.Fragment.STOPPED, false);
    }

    public void dispatchReallyStop() {
        moveToState(android.support.v4.app.Fragment.ACTIVITY_CREATED, false);
    }

    public void dispatchDestroyView() {
        moveToState(android.support.v4.app.Fragment.CREATED, false);
    }

    public void dispatchDestroy() {
        mDestroyed = true;
        execPendingActions();
        moveToState(android.support.v4.app.Fragment.INITIALIZING, false);
        mHost = null;
        mContainer = null;
        mParent = null;
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        if (mAdded == null) {
            return;
        }
        for (int i = mAdded.size() - 1; i >= 0; --i) {
            final android.support.v4.app.Fragment f = mAdded.get(i);
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
            final android.support.v4.app.Fragment f = mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchConfigurationChanged(android.content.res.Configuration newConfig) {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.support.v4.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performConfigurationChanged(newConfig);
                }
            }
        }
    }

    public void dispatchLowMemory() {
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.support.v4.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performLowMemory();
                }
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
        boolean show = false;
        java.util.ArrayList<android.support.v4.app.Fragment> newMenus = null;
        if (mAdded != null) {
            for (int i = 0; i < mAdded.size(); i++) {
                android.support.v4.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    if (f.performCreateOptionsMenu(menu, inflater)) {
                        show = true;
                        if (newMenus == null) {
                            newMenus = new java.util.ArrayList<android.support.v4.app.Fragment>();
                        }
                        newMenus.add(f);
                    }
                }
            }
        }
        if (mCreatedMenus != null) {
            for (int i = 0; i < mCreatedMenus.size(); i++) {
                android.support.v4.app.Fragment f = mCreatedMenus.get(i);
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
                android.support.v4.app.Fragment f = mAdded.get(i);
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
                android.support.v4.app.Fragment f = mAdded.get(i);
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
                android.support.v4.app.Fragment f = mAdded.get(i);
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
                android.support.v4.app.Fragment f = mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public static int reverseTransit(int transit) {
        int rev = 0;
        switch (transit) {
            case android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN :
                rev = android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
                break;
            case android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE :
                rev = android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
                break;
            case android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE :
                rev = android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;
                break;
        }
        return rev;
    }

    public static final int ANIM_STYLE_OPEN_ENTER = 1;

    public static final int ANIM_STYLE_OPEN_EXIT = 2;

    public static final int ANIM_STYLE_CLOSE_ENTER = 3;

    public static final int ANIM_STYLE_CLOSE_EXIT = 4;

    public static final int ANIM_STYLE_FADE_ENTER = 5;

    public static final int ANIM_STYLE_FADE_EXIT = 6;

    public static int transitToStyleIndex(int transit, boolean enter) {
        int animAttr = -1;
        switch (transit) {
            case android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN :
                animAttr = (enter) ? android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_OPEN_ENTER : android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_OPEN_EXIT;
                break;
            case android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE :
                animAttr = (enter) ? android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_CLOSE_ENTER : android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_CLOSE_EXIT;
                break;
            case android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE :
                animAttr = (enter) ? android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_FADE_ENTER : android.support.v4.app.FragmentManagerImpl.ANIM_STYLE_FADE_EXIT;
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
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.v4.app.FragmentManagerImpl.FragmentTag.Fragment);
        if (fname == null) {
            fname = a.getString(android.support.v4.app.FragmentManagerImpl.FragmentTag.Fragment_name);
        }
        int id = a.getResourceId(android.support.v4.app.FragmentManagerImpl.FragmentTag.Fragment_id, android.view.View.NO_ID);
        java.lang.String tag = a.getString(android.support.v4.app.FragmentManagerImpl.FragmentTag.Fragment_tag);
        a.recycle();
        if (!android.support.v4.app.Fragment.isSupportFragmentClass(mHost.getContext(), fname)) {
            // Invalid support lib fragment; let the device's framework handle it.
            // This will allow android.app.Fragments to do the right thing.
            return null;
        }
        int containerId = (parent != null) ? parent.getId() : 0;
        if (((containerId == android.view.View.NO_ID) && (id == android.view.View.NO_ID)) && (tag == null)) {
            throw new java.lang.IllegalArgumentException((attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for ") + fname);
        }
        // If we restored from a previous state, we may already have
        // instantiated this fragment from the state and should use
        // that instance instead of making a new one.
        android.support.v4.app.Fragment fragment = (id != android.view.View.NO_ID) ? findFragmentById(id) : null;
        if ((fragment == null) && (tag != null)) {
            fragment = findFragmentByTag(tag);
        }
        if ((fragment == null) && (containerId != android.view.View.NO_ID)) {
            fragment = findFragmentById(containerId);
        }
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (((("onCreateView: id=0x" + java.lang.Integer.toHexString(id)) + " fname=") + fname) + " existing=") + fragment);

        if (fragment == null) {
            fragment = android.support.v4.app.Fragment.instantiate(context, fname);
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
        if ((mCurState < android.support.v4.app.Fragment.CREATED) && fragment.mFromLayout) {
            moveToState(fragment, android.support.v4.app.Fragment.CREATED, 0, 0, false);
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

    android.support.v4.view.LayoutInflaterFactory getLayoutInflaterFactory() {
        return this;
    }

    static class FragmentTag {
        public static final int[] Fragment = new int[]{ 0x1010003, 0x10100d0, 0x10100d1 };

        public static final int Fragment_id = 1;

        public static final int Fragment_name = 0;

        public static final int Fragment_tag = 2;
    }
}

