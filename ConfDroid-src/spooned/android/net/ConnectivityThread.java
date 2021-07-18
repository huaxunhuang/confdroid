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
package android.net;


/**
 * Shared singleton connectivity thread for the system.  This is a thread for
 * connectivity operations such as AsyncChannel connections to system services.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @unknown 
 */
public final class ConnectivityThread extends android.os.HandlerThread {
    private static android.net.ConnectivityThread sInstance;

    private ConnectivityThread() {
        super("ConnectivityThread");
    }

    private static synchronized android.net.ConnectivityThread getInstance() {
        if (android.net.ConnectivityThread.sInstance == null) {
            android.net.ConnectivityThread.sInstance = new android.net.ConnectivityThread();
            android.net.ConnectivityThread.sInstance.start();
        }
        return android.net.ConnectivityThread.sInstance;
    }

    public static android.net.ConnectivityThread get() {
        return android.net.ConnectivityThread.getInstance();
    }

    public static android.os.Looper getInstanceLooper() {
        return android.net.ConnectivityThread.getInstance().getLooper();
    }
}

