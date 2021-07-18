/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.webkit;


/**
 * The CookieSyncManager is used to synchronize the browser cookie store
 * between RAM and permanent storage. To get the best performance, browser cookies are
 * saved in RAM. A separate thread saves the cookies between, driven by a timer.
 * <p>
 *
 * To use the CookieSyncManager, the host application has to call the following
 * when the application starts:
 * <p>
 *
 * <pre class="prettyprint">CookieSyncManager.createInstance(context)</pre><p>
 *
 * To set up for sync, the host application has to call<p>
 * <pre class="prettyprint">CookieSyncManager.getInstance().startSync()</pre><p>
 *
 * in Activity.onResume(), and call
 * <p>
 *
 * <pre class="prettyprint">
 * CookieSyncManager.getInstance().stopSync()
 * </pre><p>
 *
 * in Activity.onPause().<p>
 *
 * To get instant sync instead of waiting for the timer to trigger, the host can
 * call
 * <p>
 * <pre class="prettyprint">CookieSyncManager.getInstance().sync()</pre><p>
 *
 * The sync interval is 5 minutes, so you will want to force syncs
 * manually anyway, for instance in {@link WebViewClient#onPageFinished}. Note that even sync() happens
 * asynchronously, so don't do it just as your activity is shutting
 * down.
 *
 * @deprecated The WebView now automatically syncs cookies as necessary.
You no longer need to create or use the CookieSyncManager.
To manually force a sync you can use the CookieManager
method {@link CookieManager#flush} which is a synchronous
replacement for {@link #sync}.
 */
@java.lang.Deprecated
public final class CookieSyncManager extends android.webkit.WebSyncManager {
    private static android.webkit.CookieSyncManager sRef;

    private static boolean sGetInstanceAllowed = false;

    private CookieSyncManager() {
        super(null, null);
    }

    /**
     * Singleton access to a {@link CookieSyncManager}. An
     * IllegalStateException will be thrown if
     * {@link CookieSyncManager#createInstance(Context)} is not called before.
     *
     * @return CookieSyncManager
     */
    public static synchronized android.webkit.CookieSyncManager getInstance() {
        android.webkit.CookieSyncManager.checkInstanceIsAllowed();
        if (android.webkit.CookieSyncManager.sRef == null) {
            android.webkit.CookieSyncManager.sRef = new android.webkit.CookieSyncManager();
        }
        return android.webkit.CookieSyncManager.sRef;
    }

    /**
     * Create a singleton CookieSyncManager within a context
     *
     * @param context
     * 		
     * @return CookieSyncManager
     */
    public static synchronized android.webkit.CookieSyncManager createInstance(android.content.Context context) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("Invalid context argument");
        }
        android.webkit.CookieSyncManager.setGetInstanceIsAllowed();
        return android.webkit.CookieSyncManager.getInstance();
    }

    /**
     * sync() forces sync manager to sync now
     *
     * @deprecated Use {@link CookieManager#flush} instead.
     */
    @java.lang.Deprecated
    public void sync() {
        android.webkit.CookieManager.getInstance().flush();
    }

    /**
     *
     *
     * @deprecated Use {@link CookieManager#flush} instead.
     */
    @java.lang.Deprecated
    protected void syncFromRamToFlash() {
        android.webkit.CookieManager.getInstance().flush();
    }

    /**
     * resetSync() resets sync manager's timer.
     *
     * @deprecated Calling resetSync is no longer necessary as the WebView automatically
    syncs cookies.
     */
    @java.lang.Deprecated
    public void resetSync() {
    }

    /**
     * startSync() requests sync manager to start sync.
     *
     * @deprecated Calling startSync is no longer necessary as the WebView automatically
    syncs cookies.
     */
    @java.lang.Deprecated
    public void startSync() {
    }

    /**
     * stopSync() requests sync manager to stop sync. remove any SYNC_MESSAGE in
     * the queue to break the sync loop
     *
     * @deprecated Calling stopSync is no longer useful as the WebView
    automatically syncs cookies.
     */
    @java.lang.Deprecated
    public void stopSync() {
    }

    static void setGetInstanceIsAllowed() {
        android.webkit.CookieSyncManager.sGetInstanceAllowed = true;
    }

    private static void checkInstanceIsAllowed() {
        // Prior to Android KK, calling createInstance() or constructing a WebView is
        // a hard pre-condition for calling getInstance(). We retain that contract to aid
        // developers targeting a range of SDK levels.
        if (!android.webkit.CookieSyncManager.sGetInstanceAllowed) {
            throw new java.lang.IllegalStateException("CookieSyncManager::createInstance() needs to be called " + "before CookieSyncManager::getInstance()");
        }
    }
}

