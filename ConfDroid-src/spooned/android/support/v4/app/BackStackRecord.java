package android.support.v4.app;


/**
 * Entry of an operation on the fragment back stack.
 */
final class BackStackRecord extends android.support.v4.app.FragmentTransaction implements android.support.v4.app.FragmentManager.BackStackEntry , java.lang.Runnable {
    static final java.lang.String TAG = android.support.v4.app.FragmentManagerImpl.TAG;

    static final boolean SUPPORTS_TRANSITIONS = android.os.Build.VERSION.SDK_INT >= 21;

    final android.support.v4.app.FragmentManagerImpl mManager;

    static final int OP_NULL = 0;

    static final int OP_ADD = 1;

    static final int OP_REPLACE = 2;

    static final int OP_REMOVE = 3;

    static final int OP_HIDE = 4;

    static final int OP_SHOW = 5;

    static final int OP_DETACH = 6;

    static final int OP_ATTACH = 7;

    static final class Op {
        android.support.v4.app.BackStackRecord.Op next;

        android.support.v4.app.BackStackRecord.Op prev;

        int cmd;

        android.support.v4.app.Fragment fragment;

        int enterAnim;

        int exitAnim;

        int popEnterAnim;

        int popExitAnim;

        java.util.ArrayList<android.support.v4.app.Fragment> removed;
    }

    android.support.v4.app.BackStackRecord.Op mHead;

    android.support.v4.app.BackStackRecord.Op mTail;

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

    public void dump(java.lang.String prefix, java.io.PrintWriter writer, boolean full) {
        if (full) {
            writer.print(prefix);
            writer.print("mName=");
            writer.print(mName);
            writer.print(" mIndex=");
            writer.print(mIndex);
            writer.print(" mCommitted=");
            writer.println(mCommitted);
            if (mTransition != android.support.v4.app.FragmentTransaction.TRANSIT_NONE) {
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
            android.support.v4.app.BackStackRecord.Op op = mHead;
            int num = 0;
            while (op != null) {
                java.lang.String cmdStr;
                switch (op.cmd) {
                    case android.support.v4.app.BackStackRecord.OP_NULL :
                        cmdStr = "NULL";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_ADD :
                        cmdStr = "ADD";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_REPLACE :
                        cmdStr = "REPLACE";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_REMOVE :
                        cmdStr = "REMOVE";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_HIDE :
                        cmdStr = "HIDE";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_SHOW :
                        cmdStr = "SHOW";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_DETACH :
                        cmdStr = "DETACH";
                        break;
                    case android.support.v4.app.BackStackRecord.OP_ATTACH :
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
                        writer.print(prefix);
                        writer.print("enterAnim=#");
                        writer.print(java.lang.Integer.toHexString(op.enterAnim));
                        writer.print(" exitAnim=#");
                        writer.println(java.lang.Integer.toHexString(op.exitAnim));
                    }
                    if ((op.popEnterAnim != 0) || (op.popExitAnim != 0)) {
                        writer.print(prefix);
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

    public BackStackRecord(android.support.v4.app.FragmentManagerImpl manager) {
        mManager = manager;
    }

    @java.lang.Override
    public int getId() {
        return mIndex;
    }

    @java.lang.Override
    public int getBreadCrumbTitleRes() {
        return mBreadCrumbTitleRes;
    }

    @java.lang.Override
    public int getBreadCrumbShortTitleRes() {
        return mBreadCrumbShortTitleRes;
    }

    @java.lang.Override
    public java.lang.CharSequence getBreadCrumbTitle() {
        if (mBreadCrumbTitleRes != 0) {
            return mManager.mHost.getContext().getText(mBreadCrumbTitleRes);
        }
        return mBreadCrumbTitleText;
    }

    @java.lang.Override
    public java.lang.CharSequence getBreadCrumbShortTitle() {
        if (mBreadCrumbShortTitleRes != 0) {
            return mManager.mHost.getContext().getText(mBreadCrumbShortTitleRes);
        }
        return mBreadCrumbShortTitleText;
    }

    void addOp(android.support.v4.app.BackStackRecord.Op op) {
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

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction add(android.support.v4.app.Fragment fragment, java.lang.String tag) {
        doAddOp(0, fragment, tag, android.support.v4.app.BackStackRecord.OP_ADD);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction add(int containerViewId, android.support.v4.app.Fragment fragment) {
        doAddOp(containerViewId, fragment, null, android.support.v4.app.BackStackRecord.OP_ADD);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction add(int containerViewId, android.support.v4.app.Fragment fragment, java.lang.String tag) {
        doAddOp(containerViewId, fragment, tag, android.support.v4.app.BackStackRecord.OP_ADD);
        return this;
    }

    private void doAddOp(int containerViewId, android.support.v4.app.Fragment fragment, java.lang.String tag, int opcmd) {
        final java.lang.Class fragmentClass = fragment.getClass();
        final int modifiers = fragmentClass.getModifiers();
        if ((fragmentClass.isAnonymousClass() || (!java.lang.reflect.Modifier.isPublic(modifiers))) || (fragmentClass.isMemberClass() && (!java.lang.reflect.Modifier.isStatic(modifiers)))) {
            throw new java.lang.IllegalStateException((("Fragment " + fragmentClass.getCanonicalName()) + " must be a public static class to be  properly recreated from") + " instance state.");
        }
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
        android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
        op.cmd = opcmd;
        op.fragment = fragment;
        addOp(op);
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction replace(int containerViewId, android.support.v4.app.Fragment fragment) {
        return replace(containerViewId, fragment, null);
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction replace(int containerViewId, android.support.v4.app.Fragment fragment, java.lang.String tag) {
        if (containerViewId == 0) {
            throw new java.lang.IllegalArgumentException("Must use non-zero containerViewId");
        }
        doAddOp(containerViewId, fragment, tag, android.support.v4.app.BackStackRecord.OP_REPLACE);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction remove(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
        op.cmd = android.support.v4.app.BackStackRecord.OP_REMOVE;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction hide(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
        op.cmd = android.support.v4.app.BackStackRecord.OP_HIDE;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction show(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
        op.cmd = android.support.v4.app.BackStackRecord.OP_SHOW;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction detach(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
        op.cmd = android.support.v4.app.BackStackRecord.OP_DETACH;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction attach(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
        op.cmd = android.support.v4.app.BackStackRecord.OP_ATTACH;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setCustomAnimations(int enter, int exit) {
        return setCustomAnimations(enter, exit, 0, 0);
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        mEnterAnim = enter;
        mExitAnim = exit;
        mPopEnterAnim = popEnter;
        mPopExitAnim = popExit;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setTransition(int transition) {
        mTransition = transition;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction addSharedElement(android.view.View sharedElement, java.lang.String name) {
        if (android.support.v4.app.BackStackRecord.SUPPORTS_TRANSITIONS) {
            java.lang.String transitionName = android.support.v4.app.FragmentTransitionCompat21.getTransitionName(sharedElement);
            if (transitionName == null) {
                throw new java.lang.IllegalArgumentException("Unique transitionNames are required for all" + " sharedElements");
            }
            if (mSharedElementSourceNames == null) {
                mSharedElementSourceNames = new java.util.ArrayList<java.lang.String>();
                mSharedElementTargetNames = new java.util.ArrayList<java.lang.String>();
            }
            mSharedElementSourceNames.add(transitionName);
            mSharedElementTargetNames.add(name);
        }
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setTransitionStyle(int styleRes) {
        mTransitionStyle = styleRes;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction addToBackStack(java.lang.String name) {
        if (!mAllowAddToBackStack) {
            throw new java.lang.IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        mAddToBackStack = true;
        mName = name;
        return this;
    }

    @java.lang.Override
    public boolean isAddToBackStackAllowed() {
        return mAllowAddToBackStack;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction disallowAddToBackStack() {
        if (mAddToBackStack) {
            throw new java.lang.IllegalStateException("This transaction is already being added to the back stack");
        }
        mAllowAddToBackStack = false;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setBreadCrumbTitle(int res) {
        mBreadCrumbTitleRes = res;
        mBreadCrumbTitleText = null;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setBreadCrumbTitle(java.lang.CharSequence text) {
        mBreadCrumbTitleRes = 0;
        mBreadCrumbTitleText = text;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setBreadCrumbShortTitle(int res) {
        mBreadCrumbShortTitleRes = res;
        mBreadCrumbShortTitleText = null;
        return this;
    }

    @java.lang.Override
    public android.support.v4.app.FragmentTransaction setBreadCrumbShortTitle(java.lang.CharSequence text) {
        mBreadCrumbShortTitleRes = 0;
        mBreadCrumbShortTitleText = text;
        return this;
    }

    void bumpBackStackNesting(int amt) {
        if (!mAddToBackStack) {
            return;
        }
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, (("Bump nesting in " + this) + " by ") + amt);

        android.support.v4.app.BackStackRecord.Op op = mHead;
        while (op != null) {
            if (op.fragment != null) {
                op.fragment.mBackStackNesting += amt;
                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                    android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, (("Bump nesting of " + op.fragment) + " to ") + op.fragment.mBackStackNesting);

            }
            if (op.removed != null) {
                for (int i = op.removed.size() - 1; i >= 0; i--) {
                    android.support.v4.app.Fragment r = op.removed.get(i);
                    r.mBackStackNesting += amt;
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, (("Bump nesting of " + r) + " to ") + r.mBackStackNesting);

                }
            }
            op = op.next;
        } 
    }

    @java.lang.Override
    public int commit() {
        return commitInternal(false);
    }

    @java.lang.Override
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
        if (mCommitted)
            throw new java.lang.IllegalStateException("commit already called");

        if (android.support.v4.app.FragmentManagerImpl.DEBUG) {
            android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, "Commit: " + this);
            android.support.v4.util.LogWriter logw = new android.support.v4.util.LogWriter(android.support.v4.app.BackStackRecord.TAG);
            java.io.PrintWriter pw = new java.io.PrintWriter(logw);
            dump("  ", null, pw, null);
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

    @java.lang.Override
    public void run() {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG)
            android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, "Run: " + this);

        if (mAddToBackStack) {
            if (mIndex < 0) {
                throw new java.lang.IllegalStateException("addToBackStack() called after commit()");
            }
        }
        bumpBackStackNesting(1);
        android.support.v4.app.BackStackRecord.TransitionState state = null;
        android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments = null;
        android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments = null;
        if (android.support.v4.app.BackStackRecord.SUPPORTS_TRANSITIONS && (mManager.mCurState >= android.support.v4.app.Fragment.CREATED)) {
            firstOutFragments = new android.util.SparseArray<android.support.v4.app.Fragment>();
            lastInFragments = new android.util.SparseArray<android.support.v4.app.Fragment>();
            calculateFragments(firstOutFragments, lastInFragments);
            state = beginTransition(firstOutFragments, lastInFragments, false);
        }
        int transitionStyle = (state != null) ? 0 : mTransitionStyle;
        int transition = (state != null) ? 0 : mTransition;
        android.support.v4.app.BackStackRecord.Op op = mHead;
        while (op != null) {
            int enterAnim = (state != null) ? 0 : op.enterAnim;
            int exitAnim = (state != null) ? 0 : op.exitAnim;
            switch (op.cmd) {
                case android.support.v4.app.BackStackRecord.OP_ADD :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = enterAnim;
                        mManager.addFragment(f, false);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_REPLACE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        int containerId = f.mContainerId;
                        if (mManager.mAdded != null) {
                            for (int i = mManager.mAdded.size() - 1; i >= 0; i--) {
                                android.support.v4.app.Fragment old = mManager.mAdded.get(i);
                                if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                    android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, (("OP_REPLACE: adding=" + f) + " old=") + old);

                                if (old.mContainerId == containerId) {
                                    if (old == f) {
                                        op.fragment = f = null;
                                    } else {
                                        if (op.removed == null) {
                                            op.removed = new java.util.ArrayList<android.support.v4.app.Fragment>();
                                        }
                                        op.removed.add(old);
                                        old.mNextAnim = exitAnim;
                                        if (mAddToBackStack) {
                                            old.mBackStackNesting += 1;
                                            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                                                android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, (("Bump nesting of " + old) + " to ") + old.mBackStackNesting);

                                        }
                                        mManager.removeFragment(old, transition, transitionStyle);
                                    }
                                }
                            }
                        }
                        if (f != null) {
                            f.mNextAnim = enterAnim;
                            mManager.addFragment(f, false);
                        }
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_REMOVE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = exitAnim;
                        mManager.removeFragment(f, transition, transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_HIDE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = exitAnim;
                        mManager.hideFragment(f, transition, transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_SHOW :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = enterAnim;
                        mManager.showFragment(f, transition, transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_DETACH :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = exitAnim;
                        mManager.detachFragment(f, transition, transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_ATTACH :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = enterAnim;
                        mManager.attachFragment(f, transition, transitionStyle);
                    }
                    break;
                default :
                    {
                        throw new java.lang.IllegalArgumentException("Unknown cmd: " + op.cmd);
                    }
            }
            op = op.next;
        } 
        mManager.moveToState(mManager.mCurState, transition, transitionStyle, true);
        if (mAddToBackStack) {
            mManager.addBackStackState(this);
        }
    }

    private static void setFirstOut(android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments, android.support.v4.app.Fragment fragment) {
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

    private void setLastIn(android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments, android.support.v4.app.Fragment fragment) {
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
            if ((fragment.mState < android.support.v4.app.Fragment.CREATED) && (mManager.mCurState >= android.support.v4.app.Fragment.CREATED)) {
                mManager.makeActive(fragment);
                mManager.moveToState(fragment, android.support.v4.app.Fragment.CREATED, 0, 0, false);
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
    private void calculateFragments(android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments) {
        if (!mManager.mContainer.onHasView()) {
            return;// nothing to see, so no transitions

        }
        android.support.v4.app.BackStackRecord.Op op = mHead;
        while (op != null) {
            switch (op.cmd) {
                case android.support.v4.app.BackStackRecord.OP_ADD :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_REPLACE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        if (mManager.mAdded != null) {
                            for (int i = 0; i < mManager.mAdded.size(); i++) {
                                android.support.v4.app.Fragment old = mManager.mAdded.get(i);
                                if ((f == null) || (old.mContainerId == f.mContainerId)) {
                                    if (old == f) {
                                        f = null;
                                        lastInFragments.remove(old.mContainerId);
                                    } else {
                                        android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, old);
                                    }
                                }
                            }
                        }
                        setLastIn(firstOutFragments, lastInFragments, op.fragment);
                        break;
                    }
                case android.support.v4.app.BackStackRecord.OP_REMOVE :
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_HIDE :
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_SHOW :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_DETACH :
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_ATTACH :
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
    public void calculateBackFragments(android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments) {
        if (!mManager.mContainer.onHasView()) {
            return;// nothing to see, so no transitions

        }
        android.support.v4.app.BackStackRecord.Op op = mTail;
        while (op != null) {
            switch (op.cmd) {
                case android.support.v4.app.BackStackRecord.OP_ADD :
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_REPLACE :
                    if (op.removed != null) {
                        for (int i = op.removed.size() - 1; i >= 0; i--) {
                            setLastIn(firstOutFragments, lastInFragments, op.removed.get(i));
                        }
                    }
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_REMOVE :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_HIDE :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_SHOW :
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_DETACH :
                    setLastIn(firstOutFragments, lastInFragments, op.fragment);
                    break;
                case android.support.v4.app.BackStackRecord.OP_ATTACH :
                    android.support.v4.app.BackStackRecord.setFirstOut(firstOutFragments, lastInFragments, op.fragment);
                    break;
            }
            op = op.prev;
        } 
    }

    public android.support.v4.app.BackStackRecord.TransitionState popFromBackStack(boolean doStateMove, android.support.v4.app.BackStackRecord.TransitionState state, android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments) {
        if (android.support.v4.app.FragmentManagerImpl.DEBUG) {
            android.util.Log.v(android.support.v4.app.BackStackRecord.TAG, "popFromBackStack: " + this);
            android.support.v4.util.LogWriter logw = new android.support.v4.util.LogWriter(android.support.v4.app.BackStackRecord.TAG);
            java.io.PrintWriter pw = new java.io.PrintWriter(logw);
            dump("  ", null, pw, null);
        }
        if (android.support.v4.app.BackStackRecord.SUPPORTS_TRANSITIONS && (mManager.mCurState >= android.support.v4.app.Fragment.CREATED)) {
            if (state == null) {
                if ((firstOutFragments.size() != 0) || (lastInFragments.size() != 0)) {
                    state = beginTransition(firstOutFragments, lastInFragments, true);
                }
            } else
                if (!doStateMove) {
                    android.support.v4.app.BackStackRecord.setNameOverrides(state, mSharedElementTargetNames, mSharedElementSourceNames);
                }

        }
        bumpBackStackNesting(-1);
        int transitionStyle = (state != null) ? 0 : mTransitionStyle;
        int transition = (state != null) ? 0 : mTransition;
        android.support.v4.app.BackStackRecord.Op op = mTail;
        while (op != null) {
            int popEnterAnim = (state != null) ? 0 : op.popEnterAnim;
            int popExitAnim = (state != null) ? 0 : op.popExitAnim;
            switch (op.cmd) {
                case android.support.v4.app.BackStackRecord.OP_ADD :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = popExitAnim;
                        mManager.removeFragment(f, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_REPLACE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        if (f != null) {
                            f.mNextAnim = popExitAnim;
                            mManager.removeFragment(f, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle);
                        }
                        if (op.removed != null) {
                            for (int i = 0; i < op.removed.size(); i++) {
                                android.support.v4.app.Fragment old = op.removed.get(i);
                                old.mNextAnim = popEnterAnim;
                                mManager.addFragment(old, false);
                            }
                        }
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_REMOVE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = popEnterAnim;
                        mManager.addFragment(f, false);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_HIDE :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = popEnterAnim;
                        mManager.showFragment(f, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_SHOW :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = popExitAnim;
                        mManager.hideFragment(f, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_DETACH :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = popEnterAnim;
                        mManager.attachFragment(f, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle);
                    }
                    break;
                case android.support.v4.app.BackStackRecord.OP_ATTACH :
                    {
                        android.support.v4.app.Fragment f = op.fragment;
                        f.mNextAnim = popEnterAnim;
                        mManager.detachFragment(f, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle);
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
            mManager.moveToState(mManager.mCurState, android.support.v4.app.FragmentManagerImpl.reverseTransit(transition), transitionStyle, true);
            state = null;
        }
        if (mIndex >= 0) {
            mManager.freeBackStackIndex(mIndex);
            mIndex = -1;
        }
        return state;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mName;
    }

    public int getTransition() {
        return mTransition;
    }

    public int getTransitionStyle() {
        return mTransitionStyle;
    }

    @java.lang.Override
    public boolean isEmpty() {
        return mNumOp == 0;
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
    in {@link #setNameOverrides(BackStackRecord.TransitionState, java.util.ArrayList,
    java.util.ArrayList)}.
     */
    private android.support.v4.app.BackStackRecord.TransitionState beginTransition(android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments, boolean isBack) {
        android.support.v4.app.BackStackRecord.TransitionState state = new android.support.v4.app.BackStackRecord.TransitionState();
        // Adding a non-existent target view makes sure that the transitions don't target
        // any views by default. They'll only target the views we tell add. If we don't
        // add any, then no views will be targeted.
        state.nonExistentView = new android.view.View(mManager.mHost.getContext());
        boolean anyTransitionStarted = false;
        // Go over all leaving fragments.
        for (int i = 0; i < firstOutFragments.size(); i++) {
            int containerId = firstOutFragments.keyAt(i);
            if (configureTransitions(containerId, state, isBack, firstOutFragments, lastInFragments)) {
                anyTransitionStarted = true;
            }
        }
        // Now go over all entering fragments that didn't have a leaving fragment.
        for (int i = 0; i < lastInFragments.size(); i++) {
            int containerId = lastInFragments.keyAt(i);
            if ((firstOutFragments.get(containerId) == null) && configureTransitions(containerId, state, isBack, firstOutFragments, lastInFragments)) {
                anyTransitionStarted = true;
            }
        }
        if (!anyTransitionStarted) {
            state = null;
        }
        return state;
    }

    private static java.lang.Object getEnterTransition(android.support.v4.app.Fragment inFragment, boolean isBack) {
        if (inFragment == null) {
            return null;
        }
        return android.support.v4.app.FragmentTransitionCompat21.cloneTransition(isBack ? inFragment.getReenterTransition() : inFragment.getEnterTransition());
    }

    private static java.lang.Object getExitTransition(android.support.v4.app.Fragment outFragment, boolean isBack) {
        if (outFragment == null) {
            return null;
        }
        return android.support.v4.app.FragmentTransitionCompat21.cloneTransition(isBack ? outFragment.getReturnTransition() : outFragment.getExitTransition());
    }

    private static java.lang.Object getSharedElementTransition(android.support.v4.app.Fragment inFragment, android.support.v4.app.Fragment outFragment, boolean isBack) {
        if ((inFragment == null) || (outFragment == null)) {
            return null;
        }
        return android.support.v4.app.FragmentTransitionCompat21.wrapSharedElementTransition(isBack ? outFragment.getSharedElementReturnTransition() : inFragment.getSharedElementEnterTransition());
    }

    private static java.lang.Object captureExitingViews(java.lang.Object exitTransition, android.support.v4.app.Fragment outFragment, java.util.ArrayList<android.view.View> exitingViews, android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews, android.view.View nonExistentView) {
        if (exitTransition != null) {
            exitTransition = android.support.v4.app.FragmentTransitionCompat21.captureExitingViews(exitTransition, outFragment.getView(), exitingViews, namedViews, nonExistentView);
        }
        return exitTransition;
    }

    private android.support.v4.util.ArrayMap<java.lang.String, android.view.View> remapSharedElements(android.support.v4.app.BackStackRecord.TransitionState state, android.support.v4.app.Fragment outFragment, boolean isBack) {
        android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews = new android.support.v4.util.ArrayMap<java.lang.String, android.view.View>();
        if (mSharedElementSourceNames != null) {
            android.support.v4.app.FragmentTransitionCompat21.findNamedViews(namedViews, outFragment.getView());
            if (isBack) {
                namedViews.retainAll(mSharedElementTargetNames);
            } else {
                namedViews = android.support.v4.app.BackStackRecord.remapNames(mSharedElementSourceNames, mSharedElementTargetNames, namedViews);
            }
        }
        if (isBack) {
            if (outFragment.mEnterTransitionCallback != null) {
                outFragment.mEnterTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            }
            setBackNameOverrides(state, namedViews, false);
        } else {
            if (outFragment.mExitTransitionCallback != null) {
                outFragment.mExitTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            }
            setNameOverrides(state, namedViews, false);
        }
        return namedViews;
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
    private boolean configureTransitions(int containerId, android.support.v4.app.BackStackRecord.TransitionState state, boolean isBack, android.util.SparseArray<android.support.v4.app.Fragment> firstOutFragments, android.util.SparseArray<android.support.v4.app.Fragment> lastInFragments) {
        android.view.ViewGroup sceneRoot = ((android.view.ViewGroup) (mManager.mContainer.onFindViewById(containerId)));
        if (sceneRoot == null) {
            return false;
        }
        final android.support.v4.app.Fragment inFragment = lastInFragments.get(containerId);
        android.support.v4.app.Fragment outFragment = firstOutFragments.get(containerId);
        java.lang.Object enterTransition = android.support.v4.app.BackStackRecord.getEnterTransition(inFragment, isBack);
        java.lang.Object sharedElementTransition = android.support.v4.app.BackStackRecord.getSharedElementTransition(inFragment, outFragment, isBack);
        java.lang.Object exitTransition = android.support.v4.app.BackStackRecord.getExitTransition(outFragment, isBack);
        android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews = null;
        java.util.ArrayList<android.view.View> sharedElementTargets = new java.util.ArrayList<android.view.View>();
        if (sharedElementTransition != null) {
            namedViews = remapSharedElements(state, outFragment, isBack);
            if (namedViews.isEmpty()) {
                sharedElementTransition = null;
                namedViews = null;
            } else {
                // Notify the start of the transition.
                android.support.v4.app.SharedElementCallback callback = (isBack) ? outFragment.mEnterTransitionCallback : inFragment.mEnterTransitionCallback;
                if (callback != null) {
                    java.util.ArrayList<java.lang.String> names = new java.util.ArrayList<java.lang.String>(namedViews.keySet());
                    java.util.ArrayList<android.view.View> views = new java.util.ArrayList<android.view.View>(namedViews.values());
                    callback.onSharedElementStart(names, views, null);
                }
                prepareSharedElementTransition(state, sceneRoot, sharedElementTransition, inFragment, outFragment, isBack, sharedElementTargets, enterTransition, exitTransition);
            }
        }
        if (((enterTransition == null) && (sharedElementTransition == null)) && (exitTransition == null)) {
            return false;// no transitions!

        }
        java.util.ArrayList<android.view.View> exitingViews = new java.util.ArrayList<android.view.View>();
        exitTransition = android.support.v4.app.BackStackRecord.captureExitingViews(exitTransition, outFragment, exitingViews, namedViews, state.nonExistentView);
        // Set the epicenter of the exit transition
        if ((mSharedElementTargetNames != null) && (namedViews != null)) {
            android.view.View epicenterView = namedViews.get(mSharedElementTargetNames.get(0));
            if (epicenterView != null) {
                if (exitTransition != null) {
                    android.support.v4.app.FragmentTransitionCompat21.setEpicenter(exitTransition, epicenterView);
                }
                if (sharedElementTransition != null) {
                    android.support.v4.app.FragmentTransitionCompat21.setEpicenter(sharedElementTransition, epicenterView);
                }
            }
        }
        android.support.v4.app.FragmentTransitionCompat21.ViewRetriever viewRetriever = new android.support.v4.app.FragmentTransitionCompat21.ViewRetriever() {
            @java.lang.Override
            public android.view.View getView() {
                return inFragment.getView();
            }
        };
        java.util.ArrayList<android.view.View> enteringViews = new java.util.ArrayList<android.view.View>();
        android.support.v4.util.ArrayMap<java.lang.String, android.view.View> renamedViews = new android.support.v4.util.ArrayMap<java.lang.String, android.view.View>();
        boolean allowOverlap = true;
        if (inFragment != null) {
            allowOverlap = (isBack) ? inFragment.getAllowReturnTransitionOverlap() : inFragment.getAllowEnterTransitionOverlap();
        }
        java.lang.Object transition = android.support.v4.app.FragmentTransitionCompat21.mergeTransitions(enterTransition, exitTransition, sharedElementTransition, allowOverlap);
        if (transition != null) {
            android.support.v4.app.FragmentTransitionCompat21.addTransitionTargets(enterTransition, sharedElementTransition, exitTransition, sceneRoot, viewRetriever, state.nonExistentView, state.enteringEpicenterView, state.nameOverrides, enteringViews, exitingViews, namedViews, renamedViews, sharedElementTargets);
            excludeHiddenFragmentsAfterEnter(sceneRoot, state, containerId, transition);
            // We want to exclude hidden views later, so we need a non-null list in the
            // transition now.
            android.support.v4.app.FragmentTransitionCompat21.excludeTarget(transition, state.nonExistentView, true);
            // Now exclude all currently hidden fragments.
            excludeHiddenFragments(state, containerId, transition);
            android.support.v4.app.FragmentTransitionCompat21.beginDelayedTransition(sceneRoot, transition);
            android.support.v4.app.FragmentTransitionCompat21.cleanupTransitions(sceneRoot, state.nonExistentView, enterTransition, enteringViews, exitTransition, exitingViews, sharedElementTransition, sharedElementTargets, transition, state.hiddenFragmentViews, renamedViews);
        }
        return transition != null;
    }

    private void prepareSharedElementTransition(final android.support.v4.app.BackStackRecord.TransitionState state, final android.view.View sceneRoot, final java.lang.Object sharedElementTransition, final android.support.v4.app.Fragment inFragment, final android.support.v4.app.Fragment outFragment, final boolean isBack, final java.util.ArrayList<android.view.View> sharedElementTargets, final java.lang.Object enterTransition, final java.lang.Object exitTransition) {
        if (sharedElementTransition != null) {
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    // Remove the exclude for the shared elements from the exiting fragment.
                    android.support.v4.app.FragmentTransitionCompat21.removeTargets(sharedElementTransition, sharedElementTargets);
                    // keep the nonExistentView as excluded so the list doesn't get emptied
                    sharedElementTargets.remove(state.nonExistentView);
                    android.support.v4.app.FragmentTransitionCompat21.excludeSharedElementViews(enterTransition, exitTransition, sharedElementTransition, sharedElementTargets, false);
                    sharedElementTargets.clear();
                    android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews = mapSharedElementsIn(state, isBack, inFragment);
                    android.support.v4.app.FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, state.nonExistentView, namedViews, sharedElementTargets);
                    setEpicenterIn(namedViews, state);
                    callSharedElementEnd(state, inFragment, outFragment, isBack, namedViews);
                    // Exclude the shared elements from the entering fragment.
                    android.support.v4.app.FragmentTransitionCompat21.excludeSharedElementViews(enterTransition, exitTransition, sharedElementTransition, sharedElementTargets, true);
                    return true;
                }
            });
        }
    }

    void callSharedElementEnd(android.support.v4.app.BackStackRecord.TransitionState state, android.support.v4.app.Fragment inFragment, android.support.v4.app.Fragment outFragment, boolean isBack, android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews) {
        android.support.v4.app.SharedElementCallback sharedElementCallback = (isBack) ? outFragment.mEnterTransitionCallback : inFragment.mEnterTransitionCallback;
        if (sharedElementCallback != null) {
            java.util.ArrayList<java.lang.String> names = new java.util.ArrayList<java.lang.String>(namedViews.keySet());
            java.util.ArrayList<android.view.View> views = new java.util.ArrayList<android.view.View>(namedViews.values());
            sharedElementCallback.onSharedElementEnd(names, views, null);
        }
    }

    void setEpicenterIn(android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews, android.support.v4.app.BackStackRecord.TransitionState state) {
        if ((mSharedElementTargetNames != null) && (!namedViews.isEmpty())) {
            // now we know the epicenter of the entering transition.
            android.view.View epicenter = namedViews.get(mSharedElementTargetNames.get(0));
            if (epicenter != null) {
                state.enteringEpicenterView.epicenter = epicenter;
            }
        }
    }

    android.support.v4.util.ArrayMap<java.lang.String, android.view.View> mapSharedElementsIn(android.support.v4.app.BackStackRecord.TransitionState state, boolean isBack, android.support.v4.app.Fragment inFragment) {
        // Now map the shared elements in the incoming fragment
        android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews = mapEnteringSharedElements(state, inFragment, isBack);
        // remap shared elements and set the name mapping used
        // in the shared element transition.
        if (isBack) {
            if (inFragment.mExitTransitionCallback != null) {
                inFragment.mExitTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            }
            setBackNameOverrides(state, namedViews, true);
        } else {
            if (inFragment.mEnterTransitionCallback != null) {
                inFragment.mEnterTransitionCallback.onMapSharedElements(mSharedElementTargetNames, namedViews);
            }
            setNameOverrides(state, namedViews, true);
        }
        return namedViews;
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
     * @return A copy of namedViews with the keys coming from toGoInMap.
     */
    private static android.support.v4.util.ArrayMap<java.lang.String, android.view.View> remapNames(java.util.ArrayList<java.lang.String> inMap, java.util.ArrayList<java.lang.String> toGoInMap, android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews) {
        if (namedViews.isEmpty()) {
            return namedViews;
        }
        android.support.v4.util.ArrayMap<java.lang.String, android.view.View> remappedViews = new android.support.v4.util.ArrayMap<java.lang.String, android.view.View>();
        int numKeys = inMap.size();
        for (int i = 0; i < numKeys; i++) {
            android.view.View view = namedViews.get(inMap.get(i));
            if (view != null) {
                remappedViews.put(toGoInMap.get(i), view);
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
    private android.support.v4.util.ArrayMap<java.lang.String, android.view.View> mapEnteringSharedElements(android.support.v4.app.BackStackRecord.TransitionState state, android.support.v4.app.Fragment inFragment, boolean isBack) {
        android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews = new android.support.v4.util.ArrayMap<java.lang.String, android.view.View>();
        android.view.View root = inFragment.getView();
        if (root != null) {
            if (mSharedElementSourceNames != null) {
                android.support.v4.app.FragmentTransitionCompat21.findNamedViews(namedViews, root);
                if (isBack) {
                    namedViews = android.support.v4.app.BackStackRecord.remapNames(mSharedElementSourceNames, mSharedElementTargetNames, namedViews);
                } else {
                    namedViews.retainAll(mSharedElementTargetNames);
                }
            }
        }
        return namedViews;
    }

    private void excludeHiddenFragmentsAfterEnter(final android.view.View sceneRoot, final android.support.v4.app.BackStackRecord.TransitionState state, final int containerId, final java.lang.Object transition) {
        sceneRoot.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
            @java.lang.Override
            public boolean onPreDraw() {
                sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                excludeHiddenFragments(state, containerId, transition);
                return true;
            }
        });
    }

    void excludeHiddenFragments(android.support.v4.app.BackStackRecord.TransitionState state, int containerId, java.lang.Object transition) {
        if (mManager.mAdded != null) {
            for (int i = 0; i < mManager.mAdded.size(); i++) {
                android.support.v4.app.Fragment fragment = mManager.mAdded.get(i);
                if (((fragment.mView != null) && (fragment.mContainer != null)) && (fragment.mContainerId == containerId)) {
                    if (fragment.mHidden) {
                        if (!state.hiddenFragmentViews.contains(fragment.mView)) {
                            android.support.v4.app.FragmentTransitionCompat21.excludeTarget(transition, fragment.mView, true);
                            state.hiddenFragmentViews.add(fragment.mView);
                        }
                    } else {
                        android.support.v4.app.FragmentTransitionCompat21.excludeTarget(transition, fragment.mView, false);
                        state.hiddenFragmentViews.remove(fragment.mView);
                    }
                }
            }
        }
    }

    private static void setNameOverride(android.support.v4.util.ArrayMap<java.lang.String, java.lang.String> overrides, java.lang.String source, java.lang.String target) {
        if ((source != null) && (target != null)) {
            for (int index = 0; index < overrides.size(); index++) {
                if (source.equals(overrides.valueAt(index))) {
                    overrides.setValueAt(index, target);
                    return;
                }
            }
            overrides.put(source, target);
        }
    }

    private static void setNameOverrides(android.support.v4.app.BackStackRecord.TransitionState state, java.util.ArrayList<java.lang.String> sourceNames, java.util.ArrayList<java.lang.String> targetNames) {
        if (sourceNames != null) {
            for (int i = 0; i < sourceNames.size(); i++) {
                java.lang.String source = sourceNames.get(i);
                java.lang.String target = targetNames.get(i);
                android.support.v4.app.BackStackRecord.setNameOverride(state.nameOverrides, source, target);
            }
        }
    }

    private void setBackNameOverrides(android.support.v4.app.BackStackRecord.TransitionState state, android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews, boolean isEnd) {
        int count = (mSharedElementTargetNames == null) ? 0 : mSharedElementTargetNames.size();
        for (int i = 0; i < count; i++) {
            java.lang.String source = mSharedElementSourceNames.get(i);
            java.lang.String originalTarget = mSharedElementTargetNames.get(i);
            android.view.View view = namedViews.get(originalTarget);
            if (view != null) {
                java.lang.String target = android.support.v4.app.FragmentTransitionCompat21.getTransitionName(view);
                if (isEnd) {
                    android.support.v4.app.BackStackRecord.setNameOverride(state.nameOverrides, source, target);
                } else {
                    android.support.v4.app.BackStackRecord.setNameOverride(state.nameOverrides, target, source);
                }
            }
        }
    }

    private void setNameOverrides(android.support.v4.app.BackStackRecord.TransitionState state, android.support.v4.util.ArrayMap<java.lang.String, android.view.View> namedViews, boolean isEnd) {
        int count = namedViews.size();
        for (int i = 0; i < count; i++) {
            java.lang.String source = namedViews.keyAt(i);
            java.lang.String target = android.support.v4.app.FragmentTransitionCompat21.getTransitionName(namedViews.valueAt(i));
            if (isEnd) {
                android.support.v4.app.BackStackRecord.setNameOverride(state.nameOverrides, source, target);
            } else {
                android.support.v4.app.BackStackRecord.setNameOverride(state.nameOverrides, target, source);
            }
        }
    }

    public class TransitionState {
        public android.support.v4.util.ArrayMap<java.lang.String, java.lang.String> nameOverrides = new android.support.v4.util.ArrayMap<java.lang.String, java.lang.String>();

        public java.util.ArrayList<android.view.View> hiddenFragmentViews = new java.util.ArrayList<android.view.View>();

        public android.support.v4.app.FragmentTransitionCompat21.EpicenterView enteringEpicenterView = new android.support.v4.app.FragmentTransitionCompat21.EpicenterView();

        public android.view.View nonExistentView;
    }
}

