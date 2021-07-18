/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.textclassifier;


/**
 * Logging for android.view.textclassifier package.
 * <p>
 * To enable full log:
 * 1. adb shell setprop log.tag.androidtc VERBOSE
 * 2. adb shell stop && adb shell start
 *
 * @unknown 
 */
public final class Log {
    /**
     * true: Enables full logging.
     * false: Limits logging to debug level.
     */
    static final boolean ENABLE_FULL_LOGGING = android.view.textclassifier.android.util.Log.isLoggable(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, android.util.Log.VERBOSE);

    private Log() {
    }

    public static void v(java.lang.String tag, java.lang.String msg) {
        if (android.view.textclassifier.Log.ENABLE_FULL_LOGGING) {
            android.view.textclassifier.android.util.Log.v(tag, msg);
        }
    }

    public static void d(java.lang.String tag, java.lang.String msg) {
        android.view.textclassifier.android.util.Log.d(tag, msg);
    }

    public static void w(java.lang.String tag, java.lang.String msg) {
        android.view.textclassifier.android.util.Log.w(tag, msg);
    }

    public static void e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        if (android.view.textclassifier.Log.ENABLE_FULL_LOGGING) {
            android.view.textclassifier.android.util.Log.e(tag, msg, tr);
        } else {
            final java.lang.String trString = (tr != null) ? tr.getClass().getSimpleName() : "??";
            android.view.textclassifier.android.util.Log.d(tag, java.lang.String.format("%s (%s)", msg, trString));
        }
    }
}

