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
 * Base class for maintaining global application state. You can provide your own
 * implementation by creating a subclass and specifying the fully-qualified name
 * of this subclass as the <code>"android:name"</code> attribute in your
 * AndroidManifest.xml's <code>&lt;application&gt;</code> tag. The Application
 * class, or your subclass of the Application class, is instantiated before any
 * other class when the process for your application/package is created.
 *
 * <p class="note"><strong>Note: </strong>There is normally no need to subclass
 * Application.  In most situations, static singletons can provide the same
 * functionality in a more modular way.  If your singleton needs a global
 * context (for example to register broadcast receivers), include
 * {@link android.content.Context#getApplicationContext() Context.getApplicationContext()}
 * as a {@link android.content.Context} argument when invoking your singleton's
 * <code>getInstance()</code> method.
 * </p>
 */
public class Application extends android.content.ContextWrapper implements android.content.ComponentCallbacks2 {
    private java.util.ArrayList<android.content.ComponentCallbacks> mComponentCallbacks = new java.util.ArrayList<android.content.ComponentCallbacks>();

    private java.util.ArrayList<android.app.Application.ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new java.util.ArrayList<android.app.Application.ActivityLifecycleCallbacks>();

    private java.util.ArrayList<android.app.Application.OnProvideAssistDataListener> mAssistCallbacks = null;

    /**
     *
     *
     * @unknown 
     */
    public android.app.LoadedApk mLoadedApk;

    public interface ActivityLifecycleCallbacks {
        void onActivityCreated(android.app.Activity activity, android.os.Bundle savedInstanceState);

        void onActivityStarted(android.app.Activity activity);

        void onActivityResumed(android.app.Activity activity);

        void onActivityPaused(android.app.Activity activity);

        void onActivityStopped(android.app.Activity activity);

        void onActivitySaveInstanceState(android.app.Activity activity, android.os.Bundle outState);

        void onActivityDestroyed(android.app.Activity activity);
    }

    /**
     * Callback interface for use with {@link Application#registerOnProvideAssistDataListener}
     * and {@link Application#unregisterOnProvideAssistDataListener}.
     */
    public interface OnProvideAssistDataListener {
        /**
         * This is called when the user is requesting an assist, to build a full
         * {@link Intent#ACTION_ASSIST} Intent with all of the context of the current
         * application.  You can override this method to place into the bundle anything
         * you would like to appear in the {@link Intent#EXTRA_ASSIST_CONTEXT} part
         * of the assist Intent.
         */
        public void onProvideAssistData(android.app.Activity activity, android.os.Bundle data);
    }

    public Application() {
        super(null);
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @android.annotation.CallSuper
    public void onCreate() {
    }

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    @android.annotation.CallSuper
    public void onTerminate() {
    }

    @android.annotation.CallSuper
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        java.lang.Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.content.ComponentCallbacks) (callbacks[i])).onConfigurationChanged(newConfig);
            }
        }
    }

    @android.annotation.CallSuper
    public void onLowMemory() {
        java.lang.Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.content.ComponentCallbacks) (callbacks[i])).onLowMemory();
            }
        }
    }

    @android.annotation.CallSuper
    public void onTrimMemory(int level) {
        java.lang.Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                java.lang.Object c = callbacks[i];
                if (c instanceof android.content.ComponentCallbacks2) {
                    ((android.content.ComponentCallbacks2) (c)).onTrimMemory(level);
                }
            }
        }
    }

    public void registerComponentCallbacks(android.content.ComponentCallbacks callback) {
        synchronized(mComponentCallbacks) {
            mComponentCallbacks.add(callback);
        }
    }

    public void unregisterComponentCallbacks(android.content.ComponentCallbacks callback) {
        synchronized(mComponentCallbacks) {
            mComponentCallbacks.remove(callback);
        }
    }

    public void registerActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks callback) {
        synchronized(mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks callback) {
        synchronized(mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.remove(callback);
        }
    }

    public void registerOnProvideAssistDataListener(android.app.Application.OnProvideAssistDataListener callback) {
        synchronized(this) {
            if (mAssistCallbacks == null) {
                mAssistCallbacks = new java.util.ArrayList<android.app.Application.OnProvideAssistDataListener>();
            }
            mAssistCallbacks.add(callback);
        }
    }

    public void unregisterOnProvideAssistDataListener(android.app.Application.OnProvideAssistDataListener callback) {
        synchronized(this) {
            if (mAssistCallbacks != null) {
                mAssistCallbacks.remove(callback);
            }
        }
    }

    // ------------------ Internal API ------------------
    /**
     *
     *
     * @unknown 
     */
    /* package */
    final void attach(android.content.Context context) {
        attachBaseContext(context);
        mLoadedApk = android.app.ContextImpl.getImpl(context).mPackageInfo;
    }

    /* package */
    void dispatchActivityCreated(android.app.Activity activity, android.os.Bundle savedInstanceState) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivityCreated(activity, savedInstanceState);
            }
        }
    }

    /* package */
    void dispatchActivityStarted(android.app.Activity activity) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivityStarted(activity);
            }
        }
    }

    /* package */
    void dispatchActivityResumed(android.app.Activity activity) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivityResumed(activity);
            }
        }
    }

    /* package */
    void dispatchActivityPaused(android.app.Activity activity) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivityPaused(activity);
            }
        }
    }

    /* package */
    void dispatchActivityStopped(android.app.Activity activity) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivityStopped(activity);
            }
        }
    }

    /* package */
    void dispatchActivitySaveInstanceState(android.app.Activity activity, android.os.Bundle outState) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivitySaveInstanceState(activity, outState);
            }
        }
    }

    /* package */
    void dispatchActivityDestroyed(android.app.Activity activity) {
        java.lang.Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.ActivityLifecycleCallbacks) (callbacks[i])).onActivityDestroyed(activity);
            }
        }
    }

    private java.lang.Object[] collectComponentCallbacks() {
        java.lang.Object[] callbacks = null;
        synchronized(mComponentCallbacks) {
            if (mComponentCallbacks.size() > 0) {
                callbacks = mComponentCallbacks.toArray();
            }
        }
        return callbacks;
    }

    private java.lang.Object[] collectActivityLifecycleCallbacks() {
        java.lang.Object[] callbacks = null;
        synchronized(mActivityLifecycleCallbacks) {
            if (mActivityLifecycleCallbacks.size() > 0) {
                callbacks = mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    /* package */
    void dispatchOnProvideAssistData(android.app.Activity activity, android.os.Bundle data) {
        java.lang.Object[] callbacks;
        synchronized(this) {
            if (mAssistCallbacks == null) {
                return;
            }
            callbacks = mAssistCallbacks.toArray();
        }
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((android.app.Application.OnProvideAssistDataListener) (callbacks[i])).onProvideAssistData(activity, data);
            }
        }
    }
}

