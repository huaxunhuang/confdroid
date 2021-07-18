/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * This is the main entry-point into the WebView back end implementations, which the WebView
 * proxy class uses to instantiate all the other objects as needed. The backend must provide an
 * implementation of this interface, and make it available to the WebView via mechanism TBD.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public interface WebViewFactoryProvider {
    /**
     * This Interface provides glue for implementing the backend of WebView static methods which
     * cannot be implemented in-situ in the proxy class.
     */
    interface Statics {
        /**
         * Implements the API method:
         * {@link android.webkit.WebView#findAddress(String)}
         */
        java.lang.String findAddress(java.lang.String addr);

        /**
         * Implements the API method:
         * {@link android.webkit.WebSettings#getDefaultUserAgent(Context)}
         */
        java.lang.String getDefaultUserAgent(android.content.Context context);

        /**
         * Used for tests only.
         */
        void freeMemoryForTests();

        /**
         * Implements the API method:
         * {@link android.webkit.WebView#setWebContentsDebuggingEnabled(boolean)}
         */
        void setWebContentsDebuggingEnabled(boolean enable);

        /**
         * Implements the API method:
         * {@link android.webkit.WebView#clearClientCertPreferences(Runnable)}
         */
        void clearClientCertPreferences(java.lang.Runnable onCleared);

        /**
         * Implements the API method:
         * {@link android.webkit.WebView#setSlowWholeDocumentDrawEnabled(boolean)}
         */
        void enableSlowWholeDocumentDraw();

        /**
         * Implement the API method
         * {@link android.webkit.WebChromeClient.FileChooserParams#parseResult(int, Intent)}
         */
        android.net.Uri[] parseFileChooserResult(int resultCode, android.content.Intent intent);
    }

    android.webkit.WebViewFactoryProvider.Statics getStatics();

    /**
     * Construct a new WebViewProvider.
     *
     * @param webView
     * 		the WebView instance bound to this implementation instance. Note it will not
     * 		necessarily be fully constructed at the point of this call: defer real initialization to
     * 		WebViewProvider.init().
     * @param privateAccess
     * 		provides access into WebView internal methods.
     */
    android.webkit.WebViewProvider createWebView(android.webkit.WebView webView, android.webkit.WebView.PrivateAccess privateAccess);

    /**
     * Gets the singleton GeolocationPermissions instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the single GeolocationPermissions instance.
     */
    android.webkit.GeolocationPermissions getGeolocationPermissions();

    /**
     * Gets the singleton CookieManager instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the singleton CookieManager instance
     */
    android.webkit.CookieManager getCookieManager();

    /**
     * Gets the TokenBindingService instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the TokenBindingService instance
     */
    android.webkit.TokenBindingService getTokenBindingService();

    /**
     * Gets the ServiceWorkerController instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the ServiceWorkerController instance
     */
    android.webkit.ServiceWorkerController getServiceWorkerController();

    /**
     * Gets the singleton WebIconDatabase instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the singleton WebIconDatabase instance
     */
    android.webkit.WebIconDatabase getWebIconDatabase();

    /**
     * Gets the singleton WebStorage instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the singleton WebStorage instance
     */
    android.webkit.WebStorage getWebStorage();

    /**
     * Gets the singleton WebViewDatabase instance for this WebView implementation. The
     * implementation must return the same instance on subsequent calls.
     *
     * @return the singleton WebViewDatabase instance
     */
    android.webkit.WebViewDatabase getWebViewDatabase(android.content.Context context);
}

