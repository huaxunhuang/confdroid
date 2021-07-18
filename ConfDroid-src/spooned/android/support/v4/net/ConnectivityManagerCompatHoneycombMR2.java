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
package android.support.v4.net;


/**
 * Implementation of ConnectivityManagerCompat that can use Honeycomb MR2 APIs.
 */
class ConnectivityManagerCompatHoneycombMR2 {
    public static boolean isActiveNetworkMetered(android.net.ConnectivityManager cm) {
        final android.net.NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            // err on side of caution
            return true;
        }
        final int type = info.getType();
        switch (type) {
            case android.net.ConnectivityManager.TYPE_MOBILE :
            case android.net.ConnectivityManager.TYPE_MOBILE_DUN :
            case android.net.ConnectivityManager.TYPE_MOBILE_HIPRI :
            case android.net.ConnectivityManager.TYPE_MOBILE_MMS :
            case android.net.ConnectivityManager.TYPE_MOBILE_SUPL :
            case android.net.ConnectivityManager.TYPE_WIMAX :
                return true;
            case android.net.ConnectivityManager.TYPE_WIFI :
            case android.net.ConnectivityManager.TYPE_BLUETOOTH :
            case android.net.ConnectivityManager.TYPE_ETHERNET :
                return false;
            default :
                // err on side of caution
                return true;
        }
    }
}

