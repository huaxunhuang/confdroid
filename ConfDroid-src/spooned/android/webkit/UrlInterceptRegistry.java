/**
 * Copyright (C) 2008 The Android Open Source Project
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
 *
 *
 * @unknown 
 * @deprecated This class was intended to be used by Gears. Since Gears was
deprecated, so is this class.
 */
@java.lang.Deprecated
public final class UrlInterceptRegistry {
    private static final java.lang.String LOGTAG = "intercept";

    private static boolean mDisabled = false;

    private static java.util.LinkedList mHandlerList;

    private static synchronized java.util.LinkedList getHandlers() {
        if (android.webkit.UrlInterceptRegistry.mHandlerList == null)
            android.webkit.UrlInterceptRegistry.mHandlerList = new java.util.LinkedList<android.webkit.UrlInterceptHandler>();

        return android.webkit.UrlInterceptRegistry.mHandlerList;
    }

    /**
     * set the flag to control whether url intercept is enabled or disabled
     *
     * @param disabled
     * 		true to disable the cache
     * @unknown 
     * @deprecated This class was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public static synchronized void setUrlInterceptDisabled(boolean disabled) {
        android.webkit.UrlInterceptRegistry.mDisabled = disabled;
    }

    /**
     * get the state of the url intercept, enabled or disabled
     *
     * @return return if it is disabled
     * @unknown 
     * @deprecated This class was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public static synchronized boolean urlInterceptDisabled() {
        return android.webkit.UrlInterceptRegistry.mDisabled;
    }

    /**
     * Register a new UrlInterceptHandler. This handler will be called
     * before any that were previously registered.
     *
     * @param handler
     * 		The new UrlInterceptHandler object
     * @return true if the handler was not previously registered.
     * @unknown 
     * @deprecated This class was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public static synchronized boolean registerHandler(android.webkit.UrlInterceptHandler handler) {
        if (!android.webkit.UrlInterceptRegistry.getHandlers().contains(handler)) {
            android.webkit.UrlInterceptRegistry.getHandlers().addFirst(handler);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Unregister a previously registered UrlInterceptHandler.
     *
     * @param handler
     * 		A previously registered UrlInterceptHandler.
     * @return true if the handler was found and removed from the list.
     * @unknown 
     * @deprecated This class was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public static synchronized boolean unregisterHandler(android.webkit.UrlInterceptHandler handler) {
        return android.webkit.UrlInterceptRegistry.getHandlers().remove(handler);
    }

    /**
     * Given an url, returns the CacheResult of the first
     * UrlInterceptHandler interested, or null if none are.
     *
     * @return A CacheResult containing surrogate content.
     * @unknown 
     * @deprecated This class was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public static synchronized android.webkit.CacheManager.CacheResult getSurrogate(java.lang.String url, java.util.Map<java.lang.String, java.lang.String> headers) {
        if (android.webkit.UrlInterceptRegistry.urlInterceptDisabled()) {
            return null;
        }
        java.util.Iterator iter = android.webkit.UrlInterceptRegistry.getHandlers().listIterator();
        while (iter.hasNext()) {
            android.webkit.UrlInterceptHandler handler = ((android.webkit.UrlInterceptHandler) (iter.next()));
            android.webkit.CacheManager.CacheResult result = handler.service(url, headers);
            if (result != null) {
                return result;
            }
        } 
        return null;
    }

    /**
     * Given an url, returns the PluginData of the first
     * UrlInterceptHandler interested, or null if none are or if
     * intercepts are disabled.
     *
     * @return A PluginData instance containing surrogate content.
     * @unknown 
     * @deprecated This class was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public static synchronized android.webkit.PluginData getPluginData(java.lang.String url, java.util.Map<java.lang.String, java.lang.String> headers) {
        if (android.webkit.UrlInterceptRegistry.urlInterceptDisabled()) {
            return null;
        }
        java.util.Iterator iter = android.webkit.UrlInterceptRegistry.getHandlers().listIterator();
        while (iter.hasNext()) {
            android.webkit.UrlInterceptHandler handler = ((android.webkit.UrlInterceptHandler) (iter.next()));
            android.webkit.PluginData data = handler.getPluginData(url, headers);
            if (data != null) {
                return data;
            }
        } 
        return null;
    }
}

