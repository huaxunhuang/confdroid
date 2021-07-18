/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Integration points with the Fragment host.
 * <p>
 * Fragments may be hosted by any object; such as an {@link Activity}. In order to
 * host fragments, implement {@link FragmentHostCallback}, overriding the methods
 * applicable to the host.
 */
public abstract class FragmentHostCallback<E> extends android.support.v4.app.FragmentContainer {
    private final android.app.Activity mActivity;

    final android.content.Context mContext;

    private final android.os.Handler mHandler;

    final int mWindowAnimations;

    final android.support.v4.app.FragmentManagerImpl mFragmentManager = new android.support.v4.app.FragmentManagerImpl();

    /**
     * The loader managers for individual fragments [i.e. Fragment#getLoaderManager()]
     */
    private android.support.v4.util.SimpleArrayMap<java.lang.String, android.support.v4.app.LoaderManager> mAllLoaderManagers;

    /**
     * Whether or not fragment loaders should retain their state
     */
    private boolean mRetainLoaders;

    /**
     * The loader manger for the fragment host [i.e. Activity#getLoaderManager()]
     */
    private android.support.v4.app.LoaderManagerImpl mLoaderManager;

    private boolean mCheckedForLoaderManager;

    /**
     * Whether or not the fragment host loader manager was started
     */
    private boolean mLoadersStarted;

    public FragmentHostCallback(android.content.Context context, android.os.Handler handler, int windowAnimations) {
        /* activity */
        this(null, context, handler, windowAnimations);
    }

    FragmentHostCallback(android.support.v4.app.FragmentActivity activity) {
        /* context */
        /* windowAnimations */
        this(activity, activity, activity.mHandler, 0);
    }

    FragmentHostCallback(android.app.Activity activity, android.content.Context context, android.os.Handler handler, int windowAnimations) {
        mActivity = activity;
        mContext = context;
        mHandler = handler;
        mWindowAnimations = windowAnimations;
    }

    /**
     * Print internal state into the given stream.
     *
     * @param prefix
     * 		Desired prefix to prepend at each line of output.
     * @param fd
     * 		The raw file descriptor that the dump is being sent to.
     * @param writer
     * 		The PrintWriter to which you should dump your state. This will be closed
     * 		for you after you return.
     * @param args
     * 		additional arguments to the dump request.
     */
    public void onDump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
    }

    /**
     * Return {@code true} if the fragment's state needs to be saved.
     */
    public boolean onShouldSaveFragmentState(android.support.v4.app.Fragment fragment) {
        return true;
    }

    /**
     * Return a {@link LayoutInflater}.
     * See {@link Activity#getLayoutInflater()}.
     */
    public android.view.LayoutInflater onGetLayoutInflater() {
        return ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
    }

    /**
     * Return the object that's currently hosting the fragment. If a {@link Fragment}
     * is hosted by a {@link FragmentActivity}, the object returned here should be
     * the same object returned from {@link Fragment#getActivity()}.
     */
    @android.support.annotation.Nullable
    public abstract E onGetHost();

    /**
     * Invalidates the activity's options menu.
     * See {@link FragmentActivity#supportInvalidateOptionsMenu()}
     */
    public void onSupportInvalidateOptionsMenu() {
    }

    /**
     * Starts a new {@link Activity} from the given fragment.
     * See {@link FragmentActivity#startActivityForResult(Intent, int)}.
     */
    public void onStartActivityFromFragment(android.support.v4.app.Fragment fragment, android.content.Intent intent, int requestCode) {
        onStartActivityFromFragment(fragment, intent, requestCode, null);
    }

    /**
     * Starts a new {@link Activity} from the given fragment.
     * See {@link FragmentActivity#startActivityForResult(Intent, int, Bundle)}.
     */
    public void onStartActivityFromFragment(android.support.v4.app.Fragment fragment, android.content.Intent intent, int requestCode, @android.support.annotation.Nullable
    android.os.Bundle options) {
        if (requestCode != (-1)) {
            throw new java.lang.IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        }
        mContext.startActivity(intent);
    }

    /**
     * Starts a new {@link IntentSender} from the given fragment.
     * See {@link Activity#startIntentSender(IntentSender, Intent, int, int, int, Bundle)}.
     */
    public void onStartIntentSenderFromFragment(android.support.v4.app.Fragment fragment, android.content.IntentSender intent, int requestCode, @android.support.annotation.Nullable
    android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
        if (requestCode != (-1)) {
            throw new java.lang.IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
        }
        android.support.v4.app.ActivityCompat.startIntentSenderForResult(mActivity, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    /**
     * Requests permissions from the given fragment.
     * See {@link FragmentActivity#requestPermissions(String[], int)}
     */
    public void onRequestPermissionsFromFragment(@android.support.annotation.NonNull
    android.support.v4.app.Fragment fragment, @android.support.annotation.NonNull
    java.lang.String[] permissions, int requestCode) {
    }

    /**
     * Checks whether to show permission rationale UI from a fragment.
     * See {@link FragmentActivity#shouldShowRequestPermissionRationale(String)}
     */
    public boolean onShouldShowRequestPermissionRationale(@android.support.annotation.NonNull
    java.lang.String permission) {
        return false;
    }

    /**
     * Return {@code true} if there are window animations.
     */
    public boolean onHasWindowAnimations() {
        return true;
    }

    /**
     * Return the window animations.
     */
    public int onGetWindowAnimations() {
        return mWindowAnimations;
    }

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.view.View onFindViewById(int id) {
        return null;
    }

    @java.lang.Override
    public boolean onHasView() {
        return true;
    }

    android.app.Activity getActivity() {
        return mActivity;
    }

    android.content.Context getContext() {
        return mContext;
    }

    android.os.Handler getHandler() {
        return mHandler;
    }

    android.support.v4.app.FragmentManagerImpl getFragmentManagerImpl() {
        return mFragmentManager;
    }

    android.support.v4.app.LoaderManagerImpl getLoaderManagerImpl() {
        if (mLoaderManager != null) {
            return mLoaderManager;
        }
        mCheckedForLoaderManager = true;
        mLoaderManager = /* create */
        getLoaderManager("(root)", mLoadersStarted, true);
        return mLoaderManager;
    }

    void inactivateFragment(java.lang.String who) {
        // Log.v(TAG, "invalidateSupportFragment: who=" + who);
        if (mAllLoaderManagers != null) {
            android.support.v4.app.LoaderManagerImpl lm = ((android.support.v4.app.LoaderManagerImpl) (mAllLoaderManagers.get(who)));
            if ((lm != null) && (!lm.mRetaining)) {
                lm.doDestroy();
                mAllLoaderManagers.remove(who);
            }
        }
    }

    void onAttachFragment(android.support.v4.app.Fragment fragment) {
    }

    boolean getRetainLoaders() {
        return mRetainLoaders;
    }

    void doLoaderStart() {
        if (mLoadersStarted) {
            return;
        }
        mLoadersStarted = true;
        if (mLoaderManager != null) {
            mLoaderManager.doStart();
        } else
            if (!mCheckedForLoaderManager) {
                mLoaderManager = getLoaderManager("(root)", mLoadersStarted, false);
                // the returned loader manager may be a new one, so we have to start it
                if ((mLoaderManager != null) && (!mLoaderManager.mStarted)) {
                    mLoaderManager.doStart();
                }
            }

        mCheckedForLoaderManager = true;
    }

    // retain -- whether to stop the loader or retain it
    void doLoaderStop(boolean retain) {
        mRetainLoaders = retain;
        if (mLoaderManager == null) {
            return;
        }
        if (!mLoadersStarted) {
            return;
        }
        mLoadersStarted = false;
        if (retain) {
            mLoaderManager.doRetain();
        } else {
            mLoaderManager.doStop();
        }
    }

    void doLoaderRetain() {
        if (mLoaderManager == null) {
            return;
        }
        mLoaderManager.doRetain();
    }

    void doLoaderDestroy() {
        if (mLoaderManager == null) {
            return;
        }
        mLoaderManager.doDestroy();
    }

    void reportLoaderStart() {
        if (mAllLoaderManagers != null) {
            final int N = mAllLoaderManagers.size();
            android.support.v4.app.LoaderManagerImpl[] loaders = new android.support.v4.app.LoaderManagerImpl[N];
            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = ((android.support.v4.app.LoaderManagerImpl) (mAllLoaderManagers.valueAt(i)));
            }
            for (int i = 0; i < N; i++) {
                android.support.v4.app.LoaderManagerImpl lm = loaders[i];
                lm.finishRetain();
                lm.doReportStart();
            }
        }
    }

    android.support.v4.app.LoaderManagerImpl getLoaderManager(java.lang.String who, boolean started, boolean create) {
        if (mAllLoaderManagers == null) {
            mAllLoaderManagers = new android.support.v4.util.SimpleArrayMap<java.lang.String, android.support.v4.app.LoaderManager>();
        }
        android.support.v4.app.LoaderManagerImpl lm = ((android.support.v4.app.LoaderManagerImpl) (mAllLoaderManagers.get(who)));
        if (lm == null) {
            if (create) {
                lm = new android.support.v4.app.LoaderManagerImpl(who, this, started);
                mAllLoaderManagers.put(who, lm);
            }
        } else {
            lm.updateHostController(this);
        }
        return lm;
    }

    android.support.v4.util.SimpleArrayMap<java.lang.String, android.support.v4.app.LoaderManager> retainLoaderNonConfig() {
        boolean retainLoaders = false;
        if (mAllLoaderManagers != null) {
            // Restart any loader managers that were already stopped so that they
            // will be ready to retain
            final int N = mAllLoaderManagers.size();
            android.support.v4.app.LoaderManagerImpl[] loaders = new android.support.v4.app.LoaderManagerImpl[N];
            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = ((android.support.v4.app.LoaderManagerImpl) (mAllLoaderManagers.valueAt(i)));
            }
            final boolean doRetainLoaders = getRetainLoaders();
            for (int i = 0; i < N; i++) {
                android.support.v4.app.LoaderManagerImpl lm = loaders[i];
                if ((!lm.mRetaining) && doRetainLoaders) {
                    if (!lm.mStarted) {
                        lm.doStart();
                    }
                    lm.doRetain();
                }
                if (lm.mRetaining) {
                    retainLoaders = true;
                } else {
                    lm.doDestroy();
                    mAllLoaderManagers.remove(lm.mWho);
                }
            }
        }
        if (retainLoaders) {
            return mAllLoaderManagers;
        }
        return null;
    }

    void restoreLoaderNonConfig(android.support.v4.util.SimpleArrayMap<java.lang.String, android.support.v4.app.LoaderManager> loaderManagers) {
        mAllLoaderManagers = loaderManagers;
    }

    void dumpLoaders(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        writer.print(prefix);
        writer.print("mLoadersStarted=");
        writer.println(mLoadersStarted);
        if (mLoaderManager != null) {
            writer.print(prefix);
            writer.print("Loader Manager ");
            writer.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(mLoaderManager)));
            writer.println(":");
            mLoaderManager.dump(prefix + "  ", fd, writer, args);
        }
    }
}

