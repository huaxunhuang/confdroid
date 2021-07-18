/**
 * Copyright (C) 2016 The Android Open Source Project
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
 */
@android.annotation.SystemApi
public final class WebViewUpdateService {
    private WebViewUpdateService() {
    }

    /**
     * Fetch all packages that could potentially implement WebView.
     */
    public static android.webkit.WebViewProviderInfo[] getAllWebViewPackages() {
        try {
            return android.webkit.WebViewUpdateService.getUpdateService().getAllWebViewPackages();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /**
     * Fetch all packages that could potentially implement WebView and are currently valid.
     */
    public static android.webkit.WebViewProviderInfo[] getValidWebViewPackages() {
        try {
            return android.webkit.WebViewUpdateService.getUpdateService().getValidWebViewPackages();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /**
     * Used by DevelopmentSetting to get the name of the WebView provider currently in use.
     */
    public static java.lang.String getCurrentWebViewPackageName() {
        try {
            return android.webkit.WebViewUpdateService.getUpdateService().getCurrentWebViewPackageName();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private static android.webkit.IWebViewUpdateService getUpdateService() {
        return android.webkit.WebViewFactory.getUpdateService();
    }
}

