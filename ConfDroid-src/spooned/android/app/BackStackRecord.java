package android.app;


/**
 *
 *
 * @unknown Entry of an operation on the fragment back stack.
 */
final class BackStackRecord extends android.app.FragmentTransaction implements android.app.FragmentManager.BackStackEntry , java.lang.Runnable {
    static final java.lang.String TAG = android.app.FragmentManagerImpl.TAG;

    final android.app.FragmentManagerImpl mManager;

    static final int OP_NULL = 0;

    static final int OP_ADD = 1;

    static final int OP_REPLACE = 2;

    static final int OP_REMOVE = 3;

    static final int OP_HIDE = 4;

    static final int OP_SHOW = 5;

    static final int OP_DETACH = 6;

    static final int OP_ATTACH = 7;

    static final class Op {
        android.app.BackStackRecord.Op next;

        android.app.BackStackRecord.Op prev;

        int cmd;

        android.app.Fragment fragment;

        int enterAnim;

        int exitAnim;

        int popEnterAnim;

        int popExitAnim;

        java.util.ArrayList<android.app.Fragment> removed;
    }

    android.app.BackStackRecord.Op mHead;

    android.app.BackStackRecord.Op mTail;

    int mNumOp;

    int mEnterAnim;

    int mExitAnim;

    int mPopEnterAnim;

    int mPopExitAnim;

    int mTransition;

    int mTransitionStyle;

    boolean mAddToBackStack;

    boolean mAllowAddToBackStack = true;

    java.lang.String mName;

    boolean mCommitted;

    int mIndex = -1;

    int mBreadCrumbTitleRes;

    java.lang.CharSequence mBreadCrumbTitleText;

    int mBreadCrumbShortTitleRes;

    java.lang.CharSequence mBreadCrumbShortTitleText;

    java.util.ArrayList<java.lang.String> mSharedElementSourceNames;

    java.util.ArrayList<java.lang.String> mSharedElementTargetNames;

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        if (mIndex >= 0) {
            sb.append(" #");
            sb.append(mIndex);
        }
        if (mName != null) {
            sb.append(" ");
            sb.append(mName);
        }
        sb.append("}");
        return sb.toString();
    }

    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        dump(prefix, writer, true);
    }

    void dump(java.lang.String prefix, java.io.PrintWriter writer, boolean full) {
        if (full) {
            writer.print(prefix);
            writer.print("mName=");
            writer.print(mName);
            writer.print(" mIndex=");
            writer.print(mIndex);
            writer.print(" mCommitted=");
            writer.println(mCommitted);
            if (mTransition != android.app.FragmentTransaction.TRANSIT_NONE) {
                writer.print(prefix);
                writer.print("mTransition=#");
                writer.print(java.lang.Integer.toHexString(mTransition));
                writer.print(" mTransitionStyle=#");
                writer.println(java.lang.Integer.toHexString(mTransitionStyle));
            }
            if ((mEnterAnim != 0) || (mExitAnim != 0)) {
                writer.print(prefix);
                writer.print("mEnterAnim=#");
                writer.print(java.lang.Integer.toHexString(mEnterAnim));
                writer.print(" mExitAnim=#");
                writer.println(java.lang.Integer.toHexString(mExitAnim));
            }
            if ((mPopEnterAnim != 0) || (mPopExitAnim != 0)) {
                writer.print(prefix);
                writer.print("mPopEnterAnim=#");
                writer.print(java.lang.Integer.toHexString(mPopEnterAnim));
                writer.print(" mPopExitAnim=#");
                writer.println(java.lang.Integer.toHexString(mPopExitAnim));
            }
            if ((mBreadCrumbTitleRes != 0) || (mBreadCrumbTitleText != null)) {
                writer.print(prefix);
                writer.print("mBreadCrumbTitleRes=#");
                writer.print(java.lang.Integer.toHexString(mBreadCrumbTitleRes));
                writer.print(" mBreadCrumbTitleText=");
                writer.println(mBreadCrumbTitleText);
            }
            if ((mBreadCrumbShortTitleRes != 0) || (mBreadCrumbShortTitleText != null)) {
                writer.print(prefix);
                writer.print("mBreadCrumbShortTitleRes=#");
                writer.print(java.lang.Integer.toHexString(mBreadCrumbShortTitleRes));
                writer.print(" mBreadCrumbShortTitleText=");
                writer.println(mBreadCrumbShortTitleText);
            }
        }
        if (mHead != null) {
            writer.print(prefix);
            writer.println("Operations:");
            java.lang.String innerPrefix = prefix + "    ";
            android.app.BackStackRecord.Op op = mHead;
            int num = 0;
            while (op != null) {
                java.lang.String cmdStr;
                switch (op.cmd) {
                    case android.app.BackStackRecord.OP_NULL :
                        cmdStr = "NULL";
                        break;
                    case android.app.BackStackRecord.OP_ADD :
                        cmdStr = "ADD";
                        break;
                    case android.app.BackStackRecord.OP_REPLACE :
                        cmdStr = "REPLACE";
                        break;
                    case android.app.BackStackRecord.OP_REMOVE :
                        cmdStr = "REMOVE";
                        break;
                    case android.app.BackStackRecord.OP_HIDE :
                        cmdStr = "HIDE";
                        break;
                    case android.app.BackStackRecord.OP_SHOW :
                        cmdStr = "SHOW";
                        break;
                    case android.app.BackStackRecord.OP_DETACH :
                        cmdStr = "DETACH";
                        break;
                    case android.app.BackStackRecord.OP_ATTACH :
                        cmdStr = "ATTACH";
                        break;
                    default :
                        cmdStr = "cmd=" + op.cmd;
                        break;
                }
                writer.print(prefix);
                writer.print("  Op #");
                writer.print(num);
                writer.print(": ");
                writer.print(cmdStr);
                writer.print(" ");
                writer.println(op.fragment);
                if (full) {
                    if ((op.enterAnim != 0) || (op.exitAnim != 0)) {
                        writer.print(innerPrefix);
                        writer.print("enterAnim=#");
                        writer.print(java.lang.Integer.toHexString(op.enterAnim));
                        writer.print(" exitAnim=#");
                        writer.println(java.lang.Integer.toHexString(op.exitAnim));
                    }
                    if ((op.popEnterAnim != 0) || (op.popExitAnim != 0)) {
                        writer.print(innerPrefix);
                        writer.print("popEnterAnim=#");
                        writer.print(java.lang.Integer.toHexString(op.popEnterAnim));
                        writer.print(" popExitAnim=#");
                        writer.println(java.lang.Integer.toHexString(op.popExitAnim));
                    }
                }
                if ((op.removed != null) && (op.removed.size() > 0)) {
                    for (int i = 0; i < op.removed.size(); i++) {
                        writer.print(innerPrefix);
                        if (op.removed.size() == 1) {
                            writer.print("Removed: ");
                        } else {
                            if (i == 0) {
                                writer.println("Removed:");
                            }
                            writer.print(innerPrefix);
                            writer.print("  #");
                            writer.print(i);
                            writer.print(": ");
                        }
                        writer.println(op.removed.get(i));
                    }
                }
                op = op.next;
                num++;
            } 
        }
    }

    public BackStackRecord(android.app.FragmentManagerImpl manager) {
        mManager = manager;
    }

    public int getId() {
        return mIndex;
    }

    public int getBreadCrumbTitleRes() {
        return mBreadCrumbTitleRes;
    }

    public int getBreadCrumbShortTitleRes() {
        return mBreadCrumbShortTitleRes;
    }

    public java.lang.CharSequence getBreadCrumbTitle() {
        if (mBreadCrumbTitleRes != 0) {
            return mManager.mHost.getContext().getText(mBreadCrumbTitleRes);
        }
        return mBreadCrumbTitleText;
    }

    public java.lang.CharSequence getBreadCrumbShortTitle() {
        if (mBreadCrumbShortTitleRes != 0) {
            return mManager.mHost.getContext().getText(mBreadCrumbShortTitleRes);
        }
        return mBreadCrumbShortTitleText;
    }

    void addOp(android.app.BackStackRecord.Op op) {
        if (mHead == null) {
            mHead = mTail = op;
        } else {
            op.prev = mTail;
            mTail.next = op;
            mTail = op;
        }
        op.enterAnim = mEnterAnim;
        op.exitAnim = mExitAnim;
        op.popEnterAnim = mPopEnterAnim;
        op.popExitAnim = mPopExitAnim;
        mNumOp++;
    }

    public android.app.FragmentTransaction add(android.app.Fragment fragment, java.lang.String tag) {
        doAddOp(0, fragment, tag, android.app.BackStackRecord.OP_ADD);
        return this;
    }

    public android.app.FragmentTransaction add(int containerViewId, android.app.Fragment fragment) {
        doAddOp(containerViewId, fragment, null, android.app.BackStackRecord.OP_ADD);
        return this;
    }

    public android.app.FragmentTransaction add(int containerViewId, android.app.Fragment fragment, java.lang.String tag) {
        doAddOp(containerViewId, fragment, tag, android.app.BackStackRecord.OP_ADD);
        return this;
    }

    private void doAddOp(int containerViewId, android.app.Fragment fragment, java.lang.String tag, int opcmd) {
        fragment.mFragmentManager = mManager;
        if (tag != null) {
            if ((fragment.mTag != null) && (!tag.equals(fragment.mTag))) {
                throw new java.lang.IllegalStateException((((("Can't change tag of fragment " + fragment) + ": was ") + fragment.mTag) + " now ") + tag);
            }
            fragment.mTag = tag;
        }
        if (containerViewId != 0) {
            if (containerViewId == android.view.View.NO_ID) {
                throw new java.lang.IllegalArgumentException(((("Can't add fragment " + fragment) + " with tag ") + tag) + " to container view with no id");
            }
            if ((fragment.mFragmentId != 0) && (fragment.mFragmentId != containerViewId)) {
                throw new java.lang.IllegalStateException((((("Can't change container ID of fragment " + fragment) + ": was ") + fragment.mFragmentId) + " now ") + containerViewId);
            }
            fragment.mContainerId = fragment.mFragmentId = containerViewId;
        }
        android.app.BackStackRecord.Op op = new android.app.BackStackRecord.Op();
        op.cmd = opcmd;
        op.fragment = fragment;
        addOp(op);
    }

    public android.app.FragmentTransaction replace(int containerViewId, android.app.Fragment fragment) {
        return replace(containerViewId, fragment, null);
    }

    public android.app.FragmentTransaction replace(int containerViewId, android.app.Fragment fragment, java.lang.String tag) {
        if (containerViewId == 0) {
            throw new java.lang.IllegalArgumentException("Must use non-zero containerViewId");
        }
        doAddOp(containerViewId, fragment, tag, android.app.BackStackRecord.OP_REPLACE);
        return this;
    }

    public android.app.FragmentTransaction remove(android.app.Fragment fragment) {
        android.app.BackStackRecord.Op op = new android.app.BackStackRecord.Op();
        op.cmd = android.app.BackStackRecord.OP_REMOVE;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public android.app.FragmentTransaction hide(android.app.Fragment fragment) {
        android.app.BackStackRecord.Op op = new android.app.BackStackRecord.Op();
        op.cmd = android.app.BackStackRecord.OP_HIDE;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public android.app.FragmentTransaction show(android.app.Fragment fragment) {
        android.app.BackStackRecord.Op op = new android.app.BackStackRecord.Op();
        op.cmd = android.app.BackStackRecord.OP_SHOW;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public android.app.FragmentTransaction detach(android.app.Fragment fragment) {
        android.app.BackStackRecord.Op op = new android.app.BackStackRecord.Op();
        op.cmd = android.app.BackStackRecord.OP_DETACH;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public android.app.FragmentTransaction attach(android.app.Fragment fragment) {
        android.app.BackStackRecord.Op op = new android.app.BackStackRecord.Op();
        op.cmd = android.app.BackStackRecord.OP_ATTACH;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public android.app.FragmentTransaction setCustomAnimations(int enter, int exit) {
        return setCustomAnimations(enter, exit, 0, 0);
    }

    public android.app.FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        mEnterAnim = enter;
        mExitAnim = exit;
        mPopEnterAnim = popEnter;
        mPopExitAnim = popExit;
        return this;
    }

    public android.app.FragmentTransaction setTransition(int transition) {
        mTransition = transition;
        return this;
    }

    @java.lang.Override
    public android.app.FragmentTransaction addSharedElement(android.view.View sharedElement, java.lang.String name) {
        java.lang.String transitionName = sharedElement.getTransitionName();
        if (transitionName == null) {
            throw new java.lang.IllegalArgumentException("Unique transitionNames are required for all" + " sharedElements");
        }
        if (mSharedElementSourceNames == null) {
            mSharedElementSourceNames = new java.util.ArrayList<java.lang.String>();
            mSharedElementTargetNames = new java.util.ArrayList<java.lang.String>();
        }
        mSharedElementSourceNames.add(transitionName);
        mSharedElementTargetNames.add(name);
        return this;
    }

    public android.app.FragmentTransaction setTransitionStyle(int styleRes) {
        mTransitionStyle = styleRes;
        return this;
    }

    public android.app.FragmentTransaction addToBackStack(java.lang.String name) {
        if (!mAllowAddToBackStack) {
            throw new java.lang.IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        mAddToBackStack = true;
        mName = name;
        return this;
    }

    public boolean isAddToBackStackAllowed() {
        return mAllowAddToBackStack;
    }

    public android.app.FragmentTransaction disallowAddToBackStack() {
        if (mAddToBackStack) {
            throw new java.lang.IllegalStateException("This transaction is already being added to the back stack");
        }
        mAllowAddToBackStack = false;
        return this;
    }

    public android.app.FragmentTransaction setBreadCrumbTitle(int res) {
        mBreadCrumbTitleRes = res;
        mBreadCrumbTitleText = null;
        return this;
    }

    public android.app.FragmentTransaction setBreadCrumbTitle(java.lang.CharSequence text) {
        mBreadCrumbTitleRes = 0;
        mBreadCrumbTitleText = text;
        return this;
    }

    public android.app.FragmentTransaction setBreadCrumbShortTitle(int res) {
        mBreadCrumbShortTitleRes = res;
        mBreadCrumbShortTitleText = null;
        return this;
    }

    public android.app.FragmentTransaction setBreadCrumbShortTitle(java.lang.CharSequence text) {
        mBreadCrumbShortTitleRes = 0;
        mBreadCrumbShortTitleText = text;
        return this;
    }

    void bumpBackStackNesting(int amt) {
        if (!mAddToBackStack) {
            return;
        }
        if (android.app.FragmentManagerImpl.DEBUG) {
            android.util.Log.v(android.app.BackStackRecord.TAG, (("Bump nesting in " + this) + " by ") + amt);
        }
        android.app.BackStackRecord.Op op = mHead;
        while (op != null) {
            if (op.fragment != null) {
                op.fragment.mBackStackNesting += amt;
                if (android.app.FragmentManagerImpl.DEBUG) {
                    android.util.Log.v(android.app.BackStackRecord.TAG, (("Bump nesting of " + op.fragment) + " to ") + op.fragment.mBackStackNesting);
                }
            }
            if (op.removed != null) {
                for (int i = op.removed.size() - 1; i >= 0; i--) {
                    android.app.Fragment r = op.removed.get(i);
                    r.mBackStackNesting += amt;
                    if (android.app.FragmentManagerImpl.DEBUG) {
                        android.util.Log.v(android.app.BackStackRecord.TAG, (("Bump nesting of " + r) + " to ") + r.mBackStackNesting);
                    }
                }
            }
            op = op.next;
        } 
    }

    public int commit() {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    @java.lang.Override
    public void commitNow() {
        disallowAddToBackStack();
        mManager.execSingleAction(this, false);
    }

    @java.lang.Override
    public void commitNowAllowingStateLoss() {
        disallowAddToBackStack();
        mManager.execSingleAction(this, true);
    }

    int commitInternal(boolean allowStateLoss) {
        if (mCommitted) {
            throw new java.lang.IllegalStateException("commit already called");
        }
        if (android.app.FragmentManagerImpl.DEBUG) {
            android.util.Log.v(android.app.BackStackRecord.TAG, "Commit: " + this);
            android.util.LogWriter logw = new android.util.LogWriter(android.util.Log.VERBOSE, android.app.BackStackRecord.TAG);
            java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(logw, false, 1024);
            dump("  ", null, pw, null);
            pw.flush();
        }
        mCommitted = true;
        if (mAddToBackStack) {
            mIndex = mManager.allocBackStackIndex(this);
        } else {
            mIndex = -1;
        }
        mManager.enqueueAction(this, allowStateLoss);
        return mIndex;
    }

    public void run() {
        if (android.app.FragmentManagerImpl.DEBUG) {
            android.util.Log.v(android.app.BackStackRecord.TAG, "Run: " + this);
        }
        if (mAddToBackStack) {
            if (mIndex < 0) {
                throw new java.lang.IllegalStateException("addToBackStack() called after commit()");
            }
        }
        bumpBackStackNesting(1);
        if (mManager.mCurState >= android.app.Fragment.CREATED) {
            android.util.SparseArray<android.app.Fragment> firstOutFragments = new android.util.SparseArray<android.app.Fragment>();
            android.util.SparseArray<android.app.Fragment> lastInFragments = new android.util.SparseArray<android.app.Fragment>();
            calculateFragments(firstOutFragments, lastInFragments);
            beginTransition(firstOutFragments, lastInFragments, false);
        }
        android.app.BackStackRecord.Op op = mHead;
        while (op != null) {
            switch (op.cmd) {
                case android.app.BackStackRecord.OP_ADD :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        mManager.addFragment(f, false);
                    }
                    break;
                case android.app.BackStackRecord.OP_REPLACE :
                    {
                        android.app.Fragment f = op.fragment;
                        int containerId = f.mContainerId;
                        if (mManager.mAdded != null) {
                            for (int i = mManager.mAdded.size() - 1; i >= 0; i--) {
                                android.app.Fragment old = mManager.mAdded.get(i);
                                if (android.app.FragmentManagerImpl.DEBUG) {
                                    android.util.Log.v(android.app.BackStackRecord.TAG, (("OP_REPLACE: adding=" + f) + " old=") + old);
                                }
                                if (old.mContainerId == containerId) {
                                    if (old == f) {
                                        op.fragment = f = null;
                                    } else {
                                        if (op.removed == null) {
                                            op.removed = new java.util.ArrayList<android.app.Fragment>();
                                        }
                                        op.removed.add(old);
                                        old.mNextAnim = op.exitAnim;
                                        if (mAddToBackStack) {
                                            old.mBackStackNesting += 1;
                                            if (android.app.FragmentManagerImpl.DEBUG) {
                                                android.util.Log.v(android.app.BackStackRecord.TAG, (("Bump nesting of " + old) + " to ") + old.mBackStackNesting);
                                            }
                                        }
                                        mManager.removeFragment(old, mTransition, mTransitionStyle);
                                    }
                                }
                            }
                        }
                        if (f != null) {
                            f.mNextAnim = op.enterAnim;
                            mManager.addFragment(f, false);
                        }
                    }
                    break;
                case android.app.BackStackRecord.OP_REMOVE :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        mManager.removeFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_HIDE :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        mManager.hideFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_SHOW :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        mManager.showFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_DETACH :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.exitAnim;
                        mManager.detachFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_ATTACH :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        mManager.attachFragment(f, mTransition, mTransitionStyle);
                    }
                    break;
                default :
                    {
                        throw new java.lang.IllegalArgumentException("Unknown cmd: " + op.cmd);
                    }
            }
            op = op.next;
        } 
        mManager.moveToState(mManager.mCurState, mTransition, mTransitionStyle, true);
        if (mAddToBackStack) {
            mManager.addBackStackState(this);
        }
    }

    private static void setFirstOut(android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments, android.app.Fragment fragment) {
        if (fragment != null) {
            int containerId = fragment.mContainerId;
            if ((containerId != 0) && (!fragment.isHidden())) {
                if ((fragment.isAdded() && (fragment.getView() != null)) && (firstOutFragments.get(containerId) == null)) {
                    firstOutFragments.put(containerId, fragment);
                }
                if (lastInFragments.get(containerId) == fragment) {
                    lastInFragments.remove(containerId);
                }
            }
        }
    }

    private void setLastIn(android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments, android.app.Fragment fragment) {
        if (fragment != null) {
            int containerId = fragment.mContainerId;
            if (containerId != 0) {
                if (!fragment.isAdded()) {
                    lastInFragments.put(containerId, fragment);
                }
                if (firstOutFragments.get(containerId) == fragment) {
                    firstOutFragments.remove(containerId);
                }
            }
            /**
             * Ensure that fragments that are entering are at least at the CREATED state
             * so that they may load Transitions using TransitionInflater.
             */
            if (((fragment.mState < android.app.Fragment.CREATED) && (mManager.mCurState >= android.app.Fragment.CREATED)) && (mManager.mHost.getContext().getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.N)) {
                mManager.makeActive(fragment);
                mManager.moveToState(fragment, android.app.Fragment.CREATED, 0, 0, false);
            }
        }
    }

    /**
     * Finds the first removed fragment and last added fragments when going forward.
     * If none of the fragments have transitions, then both lists will be empty.
     *
     * @param firstOutFragments
     * 		The list of first fragments to be removed, keyed on the
     * 		container ID. This list will be modified by the method.
     * @param lastInFragments
     * 		The list of last fragments to be added, keyed on the
     * 		container ID. This list will be modified by the method.
     */
    private void calculateFragments(android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments) {
        if (!mManager.mContainer.onHasView()) {
            return;// nothing to see, so no transitions

        }
        android.app.BackStackRecord.Op op = mHead;
        while (op != null) {
            switch (op.cmd) {
                case android.app.BackStackRecord.OP_ADD :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_REPLACE :
                    {
                        android.app.Fragment f = op.fragment;
                        if (mManager.mAdded != null) {
                            for (int i = 0; i < mManager.mAdded.size(); i++) {
                                android.app.Fragment old = mManager.mAdded.get(i);
                                if ((f == null) || (old.mContainerId == f.mContainerId)) {
                                    if (old == f) {
                                        f = null;
                                        lastInFragments.remove(old.mContainerId);
                                    } else {
                                        android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, old);
                                    }
                                }
                            }
                        }
                        setLastIn(firstOutFragments, lastInFragments, op.fragment);
                        break;
                    }
                case android.app.BackStackRecord.OP_REMOVE :
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_HIDE :
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_SHOW :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_DETACH :
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_ATTACH :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
            }
            op = op.next;
        } 
    }

    /**
     * Finds the first removed fragment and last added fragments when popping the back stack.
     * If none of the fragments have transitions, then both lists will be empty.
     *
     * @param firstOutFragments
     * 		The list of first fragments to be removed, keyed on the
     * 		container ID. This list will be modified by the method.
     * @param lastInFragments
     * 		The list of last fragments to be added, keyed on the
     * 		container ID. This list will be modified by the method.
     */
    public void calculateBackFragments(android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments) {
        if (!mManager.mContainer.onHasView()) {
            return;// nothing to see, so no transitions

        }
        android.app.BackStackRecord.Op op = mTail;
        while (op != null) {
            switch (op.cmd) {
                case android.app.BackStackRecord.OP_ADD :
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_REPLACE :
                    if (op.removed != null) {
                        for (int i = op.removed.size() - 1; i >= 0; i--) {
                            setLastIn(firstOutFragments, lastInFragments, op.removed.get(i));
                        }
                    }
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_REMOVE :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_HIDE :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_SHOW :
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_DETACH :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.app.BackStackRecord.OP_ATTACH :
                    android.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
            }
            op = op.prev;
        } 
    }

    /**
     * When custom fragment transitions are used, this sets up the state for each transition
     * and begins the transition. A different transition is started for each fragment container
     * and consists of up to 3 different transitions: the exit transition, a shared element
     * transition and an enter transition.
     *
     * <p>The exit transition operates against the leaf nodes of the first fragment
     * with a view that was removed. If no such fragment was removed, then no exit
     * transition is executed. The exit transition comes from the outgoing fragment.</p>
     *
     * <p>The enter transition operates against the last fragment that was added. If
     * that fragment does not have a view or no fragment was added, then no enter
     * transition is executed. The enter transition comes from the incoming fragment.</p>
     *
     * <p>The shared element transition operates against all views and comes either
     * from the outgoing fragment or the incoming fragment, depending on whether this
     * is going forward or popping the back stack. When going forward, the incoming
     * fragment's enter shared element transition is used, but when going back, the
     * outgoing fragment's return shared element transition is used. Shared element
     * transitions only operate if there is both an incoming and outgoing fragment.</p>
     *
     * @param firstOutFragments
     * 		The list of first fragments to be removed, keyed on the
     * 		container ID.
     * @param lastInFragments
     * 		The list of last fragments to be added, keyed on the
     * 		container ID.
     * @param isBack
     * 		true if this is popping the back stack or false if this is a
     * 		forward operation.
     * @return The TransitionState used to complete the operation of the transition
    in {@link #setNameOverrides(android.app.BackStackRecord.TransitionState, java.util.ArrayList,
    java.util.ArrayList)}.
     */
    private android.app.BackStackRecord.TransitionState beginTransition(android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments, boolean isBack) {
        android.app.BackStackRecord.TransitionState state = new android.app.BackStackRecord.TransitionState();
        // Adding a non-existent target view makes sure that the transitions don't target
        // any views by default. They'll only target the views we tell add. If we don't
        // add any, then no views will be targeted.
        state.nonExistentView = new android.view.View(mManager.mHost.getContext());
        // Go over all leaving fragments.
        for (int i = 0; i < firstOutFragments.size(); i++) {
            int containerId = firstOutFragments.keyAt(i);
            configureTransitions(containerId, state, isBack, firstOutFragments, lastInFragments);
        }
        // Now go over all entering fragments that didn't have a leaving fragment.
        for (int i = 0; i < lastInFragments.size(); i++) {
            int containerId = lastInFragments.keyAt(i);
            if (firstOutFragments.get(containerId) == null) {
                configureTransitions(containerId, state, isBack, firstOutFragments, lastInFragments);
            }
        }
        return state;
    }

    private static android.transition.Transition cloneTransition(android.transition.Transition transition) {
        if (transition != null) {
            transition = transition.clone();
        }
        return transition;
    }

    private static android.transition.Transition getEnterTransition(android.app.Fragment inFragment, boolean isBack) {
        if (inFragment == null) {
            return null;
        }
        return android.app.BackStackRecord.cloneTransition(isBack ? inFragment.getReenterTransition() : inFragment.getEnterTransition());
    }

    private static android.transition.Transition getExitTransition(android.app.Fragment outFragment, boolean isBack) {
        if (outFragment == null) {
            return null;
        }
        return android.app.BackStackRecord.cloneTransition(isBack ? outFragment.getReturnTransition() : outFragment.getExitTransition());
    }

    private static android.transition.TransitionSet getSharedElementTransition(android.app.Fragment inFragment, android.app.Fragment outFragment, boolean isBack) {
        if ((inFragment == null) || (outFragment == null)) {
            return null;
        }
        android.transition.Transition transition = android.app.BackStackRecord.cloneTransition(isBack ? outFragment.getSharedElementReturnTransition() : inFragment.getSharedElementEnterTransition());
        if (transition == null) {
            return null;
        }
        android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
        transitionSet.addTransition(transition);
        return transitionSet;
    }

    private static java.util.ArrayList<android.view.View> captureExitingViews(android.transition.Transition exitTransition, android.app.Fragment outFragment, android.util.ArrayMap<java.lang.String, android.view.View> namedViews, android.view.View nonExistentView) {
        java.util.ArrayList<android.view.View> viewList = null;
        if (exitTransition != null) {
            viewList = new java.util.ArrayList<android.view.View>();
            android.view.View root = outFragment.getView();
            root.captureTransitioningViews(viewList);
            if (namedViews != null) {
                viewList.removeAll(namedViews.values());
            }
            if (!viewList.isEmpty()) {
                viewList.add(nonExistentView);
                android.app.BackStackRecord.addTargets(exitTransition, viewList);
            }
        }
        return viewList;
    }

    private android.util.ArrayMap<java.lang.String, android.view.View> remapSharedElements(android.app.BackStackRecord.TransitionState state, android.app.Fragment outFragment, boolean isBack) {
        android.util.ArrayMap<java.lang.String, android.view.View> namedViews = new android.util.ArrayMap<java.lang.String, android.view.View>();
        if (mSharedElementSourceNames != null) {
            outFragment.getView().findNamedViews(namedViews);
            if (isBack) {
                namedViews.retainAll(mSharedElementTargetNames);
            } else {
                namedViews = android.app.BackStackRecord.remapNames(mSharedElementSourceNames, mSharedElementTargetNames, namedViews);
            }
        }
        if (isBack) {
            outFragment.mEnterTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            setBackNameOverrides(state, namedViews, false);
        } else {
            outFragment.mExitTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            setNameOverrides(state, namedViews, false);
        }
        return namedViews;
    }

    /**
     * Prepares the enter transition by adding a non-existent view to the transition's target list
     * and setting it epicenter callback. By adding a non-existent view to the target list,
     * we can prevent any view from being targeted at the beginning of the transition.
     * We will add to the views before the end state of the transition is captured so that the
     * views will appear. At the start of the transition, we clear the list of targets so that
     * we can restore the state of the transition and use it again.
     *
     * <p>The shared element transition maps its shared elements immediately prior to
     * capturing the final state of the Transition.</p>
     */
    private java.util.ArrayList<android.view.View> addTransitionTargets(final android.app.BackStackRecord.TransitionState state, final android.transition.Transition enterTransition, final android.transition.TransitionSet sharedElementTransition, final android.transition.Transition exitTransition, final android.transition.Transition overallTransition, final android.view.View container, final android.app.Fragment inFragment, final android.app.Fragment outFragment, final java.util.ArrayList<android.view.View> hiddenFragmentViews, final boolean isBack, final java.util.ArrayList<android.view.View> sharedElementTargets) {
        if (((enterTransition == null) && (sharedElementTransition == null)) && (overallTransition == null)) {
            return null;
        }
        final java.util.ArrayList<android.view.View> enteringViews = new java.util.ArrayList<android.view.View>();
        container.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
            @java.lang.Override
            public boolean onPreDraw() {
                container.getViewTreeObserver().removeOnPreDrawListener(this);
                // Don't include any newly-hidden fragments in the transition.
                if (inFragment != null) {
                    excludeHiddenFragments(hiddenFragmentViews, inFragment.mContainerId, overallTransition);
                }
                android.util.ArrayMap<java.lang.String, android.view.View> namedViews = null;
                if (sharedElementTransition != null) {
                    namedViews = mapSharedElementsIn(state, isBack, inFragment);
                    android.app.BackStackRecord.removeTargets(sharedElementTransition, sharedElementTargets);
                    // keep the nonExistentView as excluded so the list doesn't get emptied
                    sharedElementTargets.remove(state.nonExistentView);
                    android.app.BackStackRecord.excludeViews(exitTransition, sharedElementTransition, sharedElementTargets, false);
                    android.app.BackStackRecord.excludeViews(enterTransition, sharedElementTransition, sharedElementTargets, false);
                    android.app.BackStackRecord.setSharedElementTargets(sharedElementTransition, state.nonExistentView, namedViews, sharedElementTargets);
                    setEpicenterIn(namedViews, state);
                    callSharedElementEnd(state, inFragment, outFragment, isBack, namedViews);
                }
                if (enterTransition != null) {
                    enterTransition.removeTarget(state.nonExistentView);
                    android.view.View view = inFragment.getView();
                    if (view != null) {
                        view.captureTransitioningViews(enteringViews);
                        if (namedViews != null) {
                            enteringViews.removeAll(namedViews.values());
                        }
                        enteringViews.add(state.nonExistentView);
                        // We added this earlier to prevent any views being targeted.
                        android.app.BackStackRecord.addTargets(enterTransition, enteringViews);
                    }
                    setSharedElementEpicenter(enterTransition, state);
                }
                android.app.BackStackRecord.excludeViews(exitTransition, enterTransition, enteringViews, true);
                android.app.BackStackRecord.excludeViews(exitTransition, sharedElementTransition, sharedElementTargets, true);
                android.app.BackStackRecord.excludeViews(enterTransition, sharedElementTransition, sharedElementTargets, true);
                return true;
            }
        });
        return enteringViews;
    }

    private void callSharedElementEnd(android.app.BackStackRecord.TransitionState state, android.app.Fragment inFragment, android.app.Fragment outFragment, boolean isBack, android.util.ArrayMap<java.lang.String, android.view.View> namedViews) {
        android.app.SharedElementCallback sharedElementCallback = (isBack) ? outFragment.mEnterTransitionCallback : inFragment.mEnterTransitionCallback;
        java.util.ArrayList<java.lang.String> names = new java.util.ArrayList<java.lang.String>(namedViews.keySet());
        java.util.ArrayList<android.view.View> views = new java.util.ArrayList<android.view.View>(namedViews.values());
        sharedElementCallback.onSharedElementEnd(names, views, null);
    }

    private void setEpicenterIn(android.util.ArrayMap<java.lang.String, android.view.View> namedViews, android.app.BackStackRecord.TransitionState state) {
        if ((mSharedElementTargetNames != null) && (!namedViews.isEmpty())) {
            // now we know the epicenter of the entering transition.
            android.view.View epicenter = namedViews.get(mSharedElementTargetNames.get(0));
            if (epicenter != null) {
                state.enteringEpicenterView = epicenter;
            }
        }
    }

    private android.util.ArrayMap<java.lang.String, android.view.View> mapSharedElementsIn(android.app.BackStackRecord.TransitionState state, boolean isBack, android.app.Fragment inFragment) {
        // Now map the shared elements in the incoming fragment
        android.util.ArrayMap<java.lang.String, android.view.View> namedViews = mapEnteringSharedElements(state, inFragment, isBack);
        // remap shared elements and set the name mapping used
        // in the shared element transition.
        if (isBack) {
            inFragment.mExitTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            setBackNameOverrides(state, namedViews, true);
        } else {
            inFragment.mEnterTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            setNameOverrides(state, namedViews, true);
        }
        return namedViews;
    }

    private static android.transition.Transition mergeTransitions(android.transition.Transition enterTransition, android.transition.Transition exitTransition, android.transition.Transition sharedElementTransition, android.app.Fragment inFragment, boolean isBack) {
        boolean overlap = true;
        if (((enterTransition != null) && (exitTransition != null)) && (inFragment != null)) {
            overlap = (isBack) ? inFragment.getAllowReturnTransitionOverlap() : inFragment.getAllowEnterTransitionOverlap();
        }
        // Wrap the transitions. Explicit targets like in enter and exit will cause the
        // views to be targeted regardless of excluded views. If that happens, then the
        // excluded fragments views (hidden fragments) will still be in the transition.
        android.transition.Transition transition;
        if (overlap) {
            // Regular transition -- do it all together
            android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
            if (enterTransition != null) {
                transitionSet.addTransition(enterTransition);
            }
            if (exitTransition != null) {
                transitionSet.addTransition(exitTransition);
            }
            if (sharedElementTransition != null) {
                transitionSet.addTransition(sharedElementTransition);
            }
            transition = transitionSet;
        } else {
            // First do exit, then enter, but allow shared element transition to happen
            // during both.
            android.transition.Transition staggered = null;
            if ((exitTransition != null) && (enterTransition != null)) {
                staggered = new android.transition.TransitionSet().addTransition(exitTransition).addTransition(enterTransition).setOrdering(android.transition.TransitionSet.ORDERING_SEQUENTIAL);
            } else
                if (exitTransition != null) {
                    staggered = exitTransition;
                } else
                    if (enterTransition != null) {
                        staggered = enterTransition;
                    }


            if (sharedElementTransition != null) {
                android.transition.TransitionSet together = new android.transition.TransitionSet();
                if (staggered != null) {
                    together.addTransition(staggered);
                }
                together.addTransition(sharedElementTransition);
                transition = together;
            } else {
                transition = staggered;
            }
        }
        return transition;
    }

    /**
     * Configures custom transitions for a specific fragment container.
     *
     * @param containerId
     * 		The container ID of the fragments to configure the transition for.
     * @param state
     * 		The Transition State keeping track of the executing transitions.
     * @param firstOutFragments
     * 		The list of first fragments to be removed, keyed on the
     * 		container ID.
     * @param lastInFragments
     * 		The list of last fragments to be added, keyed on the
     * 		container ID.
     * @param isBack
     * 		true if this is popping the back stack or false if this is a
     * 		forward operation.
     */
    private void configureTransitions(int containerId, android.app.BackStackRecord.TransitionState state, boolean isBack, android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments) {
        android.view.ViewGroup sceneRoot = ((android.view.ViewGroup) (mManager.mContainer.onFindViewById(containerId)));
        if (sceneRoot != null) {
            android.app.Fragment inFragment = lastInFragments.get(containerId);
            android.app.Fragment outFragment = firstOutFragments.get(containerId);
            android.transition.Transition enterTransition = android.app.BackStackRecord.getEnterTransition(inFragment, isBack);
            android.transition.TransitionSet sharedElementTransition = android.app.BackStackRecord.getSharedElementTransition(inFragment, outFragment, isBack);
            android.transition.Transition exitTransition = android.app.BackStackRecord.getExitTransition(outFragment, isBack);
            if (((enterTransition == null) && (sharedElementTransition == null)) && (exitTransition == null)) {
                return;// no transitions!

            }
            if (enterTransition != null) {
                enterTransition.addTarget(state.nonExistentView);
            }
            android.util.ArrayMap<java.lang.String, android.view.View> namedViews = null;
            java.util.ArrayList<android.view.View> sharedElementTargets = new java.util.ArrayList<android.view.View>();
            if (sharedElementTransition != null) {
                namedViews = remapSharedElements(state, outFragment, isBack);
                android.app.BackStackRecord.setSharedElementTargets(sharedElementTransition, state.nonExistentView, namedViews, sharedElementTargets);
                // Notify the start of the transition.
                android.app.SharedElementCallback callback = (isBack) ? outFragment.mEnterTransitionCallback : inFragment.mEnterTransitionCallback;
                java.util.ArrayList<java.lang.String> names = new java.util.ArrayList<java.lang.String>(namedViews.keySet());
                java.util.ArrayList<android.view.View> views = new java.util.ArrayList<android.view.View>(namedViews.values());
                callback.onSharedElementStart(names, views, null);
            }
            java.util.ArrayList<android.view.View> exitingViews = android.app.BackStackRecord.captureExitingViews(exitTransition, outFragment, namedViews, state.nonExistentView);
            if ((exitingViews == null) || exitingViews.isEmpty()) {
                exitTransition = null;
            }
            android.app.BackStackRecord.excludeViews(enterTransition, exitTransition, exitingViews, true);
            android.app.BackStackRecord.excludeViews(enterTransition, sharedElementTransition, sharedElementTargets, true);
            android.app.BackStackRecord.excludeViews(exitTransition, sharedElementTransition, sharedElementTargets, true);
            // Set the epicenter of the exit transition
            if ((mSharedElementTargetNames != null) && (namedViews != null)) {
                android.view.View epicenterView = namedViews.get(mSharedElementTargetNames.get(0));
                if (epicenterView != null) {
                    if (exitTransition != null) {
                        android.app.BackStackRecord.setEpicenter(exitTransition, epicenterView);
                    }
                    if (sharedElementTransition != null) {
                        android.app.BackStackRecord.setEpicenter(sharedElementTransition, epicenterView);
                    }
                }
            }
            android.transition.Transition transition = android.app.BackStackRecord.mergeTransitions(enterTransition, exitTransition, sharedElementTransition, inFragment, isBack);
            if (transition != null) {
                java.util.ArrayList<android.view.View> hiddenFragments = new java.util.ArrayList<android.view.View>();
                java.util.ArrayList<android.view.View> enteringViews = addTransitionTargets(state, enterTransition, sharedElementTransition, exitTransition, transition, sceneRoot, inFragment, outFragment, hiddenFragments, isBack, sharedElementTargets);
                transition.setNameOverrides(state.nameOverrides);
                // We want to exclude hidden views later, so we need a non-null list in the
                // transition now.
                transition.excludeTarget(state.nonExistentView, true);
                // Now exclude all currently hidden fragments.
                excludeHiddenFragments(hiddenFragments, containerId, transition);
                android.transition.TransitionManager.beginDelayedTransition(sceneRoot, transition);
                // Remove the view targeting after the transition starts
                removeTargetedViewsFromTransitions(sceneRoot, state.nonExistentView, enterTransition, enteringViews, exitTransition, exitingViews, sharedElementTransition, sharedElementTargets, transition, hiddenFragments);
            }
        }
    }

    /**
     * Finds all children of the shared elements and sets the wrapping TransitionSet
     * targets to point to those. It also limits transitions that have no targets to the
     * specific shared elements. This allows developers to target child views of the
     * shared elements specifically, but this doesn't happen by default.
     */
    private static void setSharedElementTargets(android.transition.TransitionSet transition, android.view.View nonExistentView, android.util.ArrayMap<java.lang.String, android.view.View> namedViews, java.util.ArrayList<android.view.View> sharedElementTargets) {
        sharedElementTargets.clear();
        sharedElementTargets.addAll(namedViews.values());
        final java.util.List<android.view.View> views = transition.getTargets();
        views.clear();
        final int count = sharedElementTargets.size();
        for (int i = 0; i < count; i++) {
            final android.view.View view = sharedElementTargets.get(i);
            android.app.BackStackRecord.bfsAddViewChildren(views, view);
        }
        sharedElementTargets.add(nonExistentView);
        android.app.BackStackRecord.addTargets(transition, sharedElementTargets);
    }

    /**
     * Uses a breadth-first scheme to add startView and all of its children to views.
     * It won't add a child if it is already in views.
     */
    private static void bfsAddViewChildren(final java.util.List<android.view.View> views, final android.view.View startView) {
        final int startIndex = views.size();
        if (android.app.BackStackRecord.containedBeforeIndex(views, startView, startIndex)) {
            return;// This child is already in the list, so all its children are also.

        }
        views.add(startView);
        for (int index = startIndex; index < views.size(); index++) {
            final android.view.View view = views.get(index);
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (view));
                final int childCount = viewGroup.getChildCount();
                for (int childIndex = 0; childIndex < childCount; childIndex++) {
                    final android.view.View child = viewGroup.getChildAt(childIndex);
                    if (!android.app.BackStackRecord.containedBeforeIndex(views, child, startIndex)) {
                        views.add(child);
                    }
                }
            }
        }
    }

    /**
     * Does a linear search through views for view, limited to maxIndex.
     */
    private static boolean containedBeforeIndex(final java.util.List<android.view.View> views, final android.view.View view, final int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            if (views.get(i) == view) {
                return true;
            }
        }
        return false;
    }

    private static void excludeViews(android.transition.Transition transition, android.transition.Transition fromTransition, java.util.ArrayList<android.view.View> views, boolean exclude) {
        if (transition != null) {
            final int viewCount = (fromTransition == null) ? 0 : views.size();
            for (int i = 0; i < viewCount; i++) {
                transition.excludeTarget(views.get(i), exclude);
            }
        }
    }

    /**
     * After the transition has started, remove all targets that we added to the transitions
     * so that the transitions are left in a clean state.
     */
    private void removeTargetedViewsFromTransitions(final android.view.ViewGroup sceneRoot, final android.view.View nonExistingView, final android.transition.Transition enterTransition, final java.util.ArrayList<android.view.View> enteringViews, final android.transition.Transition exitTransition, final java.util.ArrayList<android.view.View> exitingViews, final android.transition.Transition sharedElementTransition, final java.util.ArrayList<android.view.View> sharedElementTargets, final android.transition.Transition overallTransition, final java.util.ArrayList<android.view.View> hiddenViews) {
        if (overallTransition != null) {
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (enterTransition != null) {
                        android.app.BackStackRecord.removeTargets(enterTransition, enteringViews);
                        android.app.BackStackRecord.excludeViews(enterTransition, exitTransition, exitingViews, false);
                        android.app.BackStackRecord.excludeViews(enterTransition, sharedElementTransition, sharedElementTargets, false);
                    }
                    if (exitTransition != null) {
                        android.app.BackStackRecord.removeTargets(exitTransition, exitingViews);
                        android.app.BackStackRecord.excludeViews(exitTransition, enterTransition, enteringViews, false);
                        android.app.BackStackRecord.excludeViews(exitTransition, sharedElementTransition, sharedElementTargets, false);
                    }
                    if (sharedElementTransition != null) {
                        android.app.BackStackRecord.removeTargets(sharedElementTransition, sharedElementTargets);
                    }
                    int numViews = hiddenViews.size();
                    for (int i = 0; i < numViews; i++) {
                        overallTransition.excludeTarget(hiddenViews.get(i), false);
                    }
                    overallTransition.excludeTarget(nonExistingView, false);
                    return true;
                }
            });
        }
    }

    /**
     * This method removes the views from transitions that target ONLY those views.
     * The views list should match those added in addTargets and should contain
     * one view that is not in the view hierarchy (state.nonExistentView).
     */
    public static void removeTargets(android.transition.Transition transition, java.util.ArrayList<android.view.View> views) {
        if (transition instanceof android.transition.TransitionSet) {
            android.transition.TransitionSet set = ((android.transition.TransitionSet) (transition));
            int numTransitions = set.getTransitionCount();
            for (int i = 0; i < numTransitions; i++) {
                android.transition.Transition child = set.getTransitionAt(i);
                android.app.BackStackRecord.removeTargets(child, views);
            }
        } else
            if (!android.app.BackStackRecord.hasSimpleTarget(transition)) {
                java.util.List<android.view.View> targets = transition.getTargets();
                if (((targets != null) && (targets.size() == views.size())) && targets.containsAll(views)) {
                    // We have an exact match. We must have added these earlier in addTargets
                    for (int i = views.size() - 1; i >= 0; i--) {
                        transition.removeTarget(views.get(i));
                    }
                }
            }

    }

    /**
     * This method adds views as targets to the transition, but only if the transition
     * doesn't already have a target. It is best for views to contain one View object
     * that does not exist in the view hierarchy (state.nonExistentView) so that
     * when they are removed later, a list match will suffice to remove the targets.
     * Otherwise, if you happened to have targeted the exact views for the transition,
     * the removeTargets call will remove them unexpectedly.
     */
    public static void addTargets(android.transition.Transition transition, java.util.ArrayList<android.view.View> views) {
        if (transition instanceof android.transition.TransitionSet) {
            android.transition.TransitionSet set = ((android.transition.TransitionSet) (transition));
            int numTransitions = set.getTransitionCount();
            for (int i = 0; i < numTransitions; i++) {
                android.transition.Transition child = set.getTransitionAt(i);
                android.app.BackStackRecord.addTargets(child, views);
            }
        } else
            if (!android.app.BackStackRecord.hasSimpleTarget(transition)) {
                java.util.List<android.view.View> targets = transition.getTargets();
                if (android.app.BackStackRecord.isNullOrEmpty(targets)) {
                    // We can just add the target views
                    int numViews = views.size();
                    for (int i = 0; i < numViews; i++) {
                        transition.addTarget(views.get(i));
                    }
                }
            }

    }

    private static boolean hasSimpleTarget(android.transition.Transition transition) {
        return ((!android.app.BackStackRecord.isNullOrEmpty(transition.getTargetIds())) || (!android.app.BackStackRecord.isNullOrEmpty(transition.getTargetNames()))) || (!android.app.BackStackRecord.isNullOrEmpty(transition.getTargetTypes()));
    }

    private static boolean isNullOrEmpty(java.util.List list) {
        return (list == null) || list.isEmpty();
    }

    /**
     * Remaps a name-to-View map, substituting different names for keys.
     *
     * @param inMap
     * 		A list of keys found in the map, in the order in toGoInMap
     * @param toGoInMap
     * 		A list of keys to use for the new map, in the order of inMap
     * @param namedViews
     * 		The current mapping
     * @return a new Map after it has been mapped with the new names as keys.
     */
    private static android.util.ArrayMap<java.lang.String, android.view.View> remapNames(java.util.ArrayList<java.lang.String> inMap, java.util.ArrayList<java.lang.String> toGoInMap, android.util.ArrayMap<java.lang.String, android.view.View> namedViews) {
        android.util.ArrayMap<java.lang.String, android.view.View> remappedViews = new android.util.ArrayMap<java.lang.String, android.view.View>();
        if (!namedViews.isEmpty()) {
            int numKeys = inMap.size();
            for (int i = 0; i < numKeys; i++) {
                android.view.View view = namedViews.get(inMap.get(i));
                if (view != null) {
                    remappedViews.put(toGoInMap.get(i), view);
                }
            }
        }
        return remappedViews;
    }

    /**
     * Maps shared elements to views in the entering fragment.
     *
     * @param state
     * 		The transition State as returned from {@link #beginTransition(
     * 		android.util.SparseArray, android.util.SparseArray, boolean)}.
     * @param inFragment
     * 		The last fragment to be added.
     * @param isBack
     * 		true if this is popping the back stack or false if this is a
     * 		forward operation.
     */
    private android.util.ArrayMap<java.lang.String, android.view.View> mapEnteringSharedElements(android.app.BackStackRecord.TransitionState state, android.app.Fragment inFragment, boolean isBack) {
        android.util.ArrayMap<java.lang.String, android.view.View> namedViews = new android.util.ArrayMap<java.lang.String, android.view.View>();
        android.view.View root = inFragment.getView();
        if (root != null) {
            if (mSharedElementSourceNames != null) {
                root.findNamedViews(namedViews);
                if (isBack) {
                    namedViews = android.app.BackStackRecord.remapNames(mSharedElementSourceNames, mSharedElementTargetNames, namedViews);
                } else {
                    namedViews.retainAll(mSharedElementTargetNames);
                }
            }
        }
        return namedViews;
    }

    private void excludeHiddenFragments(final java.util.ArrayList<android.view.View> hiddenFragmentViews, int containerId, android.transition.Transition transition) {
        if (mManager.mAdded != null) {
            for (int i = 0; i < mManager.mAdded.size(); i++) {
                android.app.Fragment fragment = mManager.mAdded.get(i);
                if (((fragment.mView != null) && (fragment.mContainer != null)) && (fragment.mContainerId == containerId)) {
                    if (fragment.mHidden) {
                        if (!hiddenFragmentViews.contains(fragment.mView)) {
                            transition.excludeTarget(fragment.mView, true);
                            hiddenFragmentViews.add(fragment.mView);
                        }
                    } else {
                        transition.excludeTarget(fragment.mView, false);
                        hiddenFragmentViews.remove(fragment.mView);
                    }
                }
            }
        }
    }

    private static void setEpicenter(android.transition.Transition transition, android.view.View view) {
        final android.graphics.Rect epicenter = new android.graphics.Rect();
        view.getBoundsOnScreen(epicenter);
        transition.setEpicenterCallback(new android.transition.Transition.EpicenterCallback() {
            @java.lang.Override
            public android.graphics.Rect onGetEpicenter(android.transition.Transition transition) {
                return epicenter;
            }
        });
    }

    private void setSharedElementEpicenter(android.transition.Transition transition, final android.app.BackStackRecord.TransitionState state) {
        transition.setEpicenterCallback(new android.transition.Transition.EpicenterCallback() {
            private android.graphics.Rect mEpicenter;

            @java.lang.Override
            public android.graphics.Rect onGetEpicenter(android.transition.Transition transition) {
                if ((mEpicenter == null) && (state.enteringEpicenterView != null)) {
                    mEpicenter = new android.graphics.Rect();
                    state.enteringEpicenterView.getBoundsOnScreen(mEpicenter);
                }
                return mEpicenter;
            }
        });
    }

    public android.app.BackStackRecord.TransitionState popFromBackStack(boolean doStateMove, android.app.BackStackRecord.TransitionState state, android.util.SparseArray<android.app.Fragment> firstOutFragments, android.util.SparseArray<android.app.Fragment> lastInFragments) {
        if (android.app.FragmentManagerImpl.DEBUG) {
            android.util.Log.v(android.app.BackStackRecord.TAG, "popFromBackStack: " + this);
            android.util.LogWriter logw = new android.util.LogWriter(android.util.Log.VERBOSE, android.app.BackStackRecord.TAG);
            java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(logw, false, 1024);
            dump("  ", null, pw, null);
            pw.flush();
        }
        if (mManager.mCurState >= android.app.Fragment.CREATED) {
            if (state == null) {
                if ((firstOutFragments.size() != 0) || (lastInFragments.size() != 0)) {
                    state = beginTransition(firstOutFragments, lastInFragments, true);
                }
            } else
                if (!doStateMove) {
                    android.app.BackStackRecord.setNameOverrides(state, mSharedElementTargetNames, mSharedElementSourceNames);
                }

        }
        bumpBackStackNesting(-1);
        android.app.BackStackRecord.Op op = mTail;
        while (op != null) {
            switch (op.cmd) {
                case android.app.BackStackRecord.OP_ADD :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.popExitAnim;
                        mManager.removeFragment(f, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_REPLACE :
                    {
                        android.app.Fragment f = op.fragment;
                        if (f != null) {
                            f.mNextAnim = op.popExitAnim;
                            mManager.removeFragment(f, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
                        }
                        if (op.removed != null) {
                            for (int i = 0; i < op.removed.size(); i++) {
                                android.app.Fragment old = op.removed.get(i);
                                old.mNextAnim = op.popEnterAnim;
                                mManager.addFragment(old, false);
                            }
                        }
                    }
                    break;
                case android.app.BackStackRecord.OP_REMOVE :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.popEnterAnim;
                        mManager.addFragment(f, false);
                    }
                    break;
                case android.app.BackStackRecord.OP_HIDE :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.popEnterAnim;
                        mManager.showFragment(f, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_SHOW :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.popExitAnim;
                        mManager.hideFragment(f, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_DETACH :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.popEnterAnim;
                        mManager.attachFragment(f, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
                    }
                    break;
                case android.app.BackStackRecord.OP_ATTACH :
                    {
                        android.app.Fragment f = op.fragment;
                        f.mNextAnim = op.popExitAnim;
                        mManager.detachFragment(f, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
                    }
                    break;
                default :
                    {
                        throw new java.lang.IllegalArgumentException("Unknown cmd: " + op.cmd);
                    }
            }
            op = op.prev;
        } 
        if (doStateMove) {
            mManager.moveToState(mManager.mCurState, android.app.FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle, true);
            state = null;
        }
        if (mIndex >= 0) {
            mManager.freeBackStackIndex(mIndex);
            mIndex = -1;
        }
        return state;
    }

    private static void setNameOverride(android.util.ArrayMap<java.lang.String, java.lang.String> overrides, java.lang.String source, java.lang.String target) {
        if (((source != null) && (target != null)) && (!source.equals(target))) {
            for (int index = 0; index < overrides.size(); index++) {
                if (source.equals(overrides.valueAt(index))) {
                    overrides.setValueAt(index, target);
                    return;
                }
            }
            overrides.put(source, target);
        }
    }

    private static void setNameOverrides(android.app.BackStackRecord.TransitionState state, java.util.ArrayList<java.lang.String> sourceNames, java.util.ArrayList<java.lang.String> targetNames) {
        if ((sourceNames != null) && (targetNames != null)) {
            for (int i = 0; i < sourceNames.size(); i++) {
                java.lang.String source = sourceNames.get(i);
                java.lang.String target = targetNames.get(i);
                android.app.BackStackRecord.setNameOverride(state.nameOverrides, source, target);
            }
        }
    }

    private void setBackNameOverrides(android.app.BackStackRecord.TransitionState state, android.util.ArrayMap<java.lang.String, android.view.View> namedViews, boolean isEnd) {
        int targetCount = (mSharedElementTargetNames == null) ? 0 : mSharedElementTargetNames.size();
        int sourceCount = (mSharedElementSourceNames == null) ? 0 : mSharedElementSourceNames.size();
        final int count = java.lang.Math.min(targetCount, sourceCount);
        for (int i = 0; i < count; i++) {
            java.lang.String source = mSharedElementSourceNames.get(i);
            java.lang.String originalTarget = mSharedElementTargetNames.get(i);
            android.view.View view = namedViews.get(originalTarget);
            if (view != null) {
                java.lang.String target = view.getTransitionName();
                if (isEnd) {
                    android.app.BackStackRecord.setNameOverride(state.nameOverrides, source, target);
                } else {
                    android.app.BackStackRecord.setNameOverride(state.nameOverrides, target, source);
                }
            }
        }
    }

    private void setNameOverrides(android.app.BackStackRecord.TransitionState state, android.util.ArrayMap<java.lang.String, android.view.View> namedViews, boolean isEnd) {
        int count = (namedViews == null) ? 0 : namedViews.size();
        for (int i = 0; i < count; i++) {
            java.lang.String source = namedViews.keyAt(i);
            java.lang.String target = namedViews.valueAt(i).getTransitionName();
            if (isEnd) {
                android.app.BackStackRecord.setNameOverride(state.nameOverrides, source, target);
            } else {
                android.app.BackStackRecord.setNameOverride(state.nameOverrides, target, source);
            }
        }
    }

    public java.lang.String getName() {
        return mName;
    }

    public int getTransition() {
        return mTransition;
    }

    public int getTransitionStyle() {
        return mTransitionStyle;
    }

    public boolean isEmpty() {
        return mNumOp == 0;
    }

    public class TransitionState {
        public android.util.ArrayMap<java.lang.String, java.lang.String> nameOverrides = new android.util.ArrayMap<java.lang.String, java.lang.String>();

        public android.view.View enteringEpicenterView;

        public android.view.View nonExistentView;
    }
}

