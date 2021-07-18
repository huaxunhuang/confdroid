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
 * @deprecated This interface was inteded to be used by Gears. Since Gears was
deprecated, so is this class.
 */
@java.lang.Deprecated
public interface UrlInterceptHandler {
    /**
     * Given an URL, returns the CacheResult which contains the
     * surrogate response for the request, or null if the handler is
     * not interested.
     *
     * @param url
     * 		URL string.
     * @param headers
     * 		The headers associated with the request. May be null.
     * @return The CacheResult containing the surrogate response.
     * @unknown 
     * @deprecated Do not use, this interface is deprecated.
     */
    @java.lang.Deprecated
    public android.webkit.CacheManager.CacheResult service(java.lang.String url, java.util.Map<java.lang.String, java.lang.String> headers);

    /**
     * Given an URL, returns the PluginData which contains the
     * surrogate response for the request, or null if the handler is
     * not interested.
     *
     * @param url
     * 		URL string.
     * @param headers
     * 		The headers associated with the request. May be null.
     * @return The PluginData containing the surrogate response.
     * @unknown 
     * @deprecated Do not use, this interface is deprecated.
     */
    @java.lang.Deprecated
    public android.webkit.PluginData getPluginData(java.lang.String url, java.util.Map<java.lang.String, java.lang.String> headers);
}

