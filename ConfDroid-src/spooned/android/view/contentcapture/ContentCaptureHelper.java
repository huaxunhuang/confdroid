/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.contentcapture;


/**
 * Helper class for this package and server's.
 *
 * @unknown 
 */
public final class ContentCaptureHelper {
    private static final java.lang.String TAG = android.view.contentcapture.ContentCaptureHelper.class.getSimpleName();

    public static boolean sVerbose = false;

    public static boolean sDebug = true;

    /**
     * Used to log text that could contain PII.
     */
    @android.annotation.Nullable
    public static java.lang.String getSanitizedString(@android.annotation.Nullable
    java.lang.CharSequence text) {
        return text == null ? null : text.length() + "_chars";
    }

    /**
     * Gets the default logging level for the device.
     */
    @android.view.contentcapture.ContentCaptureManager.LoggingLevel
    public static int getDefaultLoggingLevel() {
        return android.os.Build.IS_DEBUGGABLE ? android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_DEBUG : android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_OFF;
    }

    /**
     * Sets the value of the static logging level constants based on device config.
     */
    public static void setLoggingLevel() {
        final int defaultLevel = android.view.contentcapture.ContentCaptureHelper.getDefaultLoggingLevel();
        final int level = android.provider.DeviceConfig.getInt(DeviceConfig.NAMESPACE_CONTENT_CAPTURE, android.view.contentcapture.ContentCaptureManager.DEVICE_CONFIG_PROPERTY_LOGGING_LEVEL, defaultLevel);
        android.view.contentcapture.ContentCaptureHelper.setLoggingLevel(level);
    }

    /**
     * Sets the value of the static logging level constants based the given level.
     */
    public static void setLoggingLevel(@android.view.contentcapture.ContentCaptureManager.LoggingLevel
    int level) {
        android.util.Log.i(android.view.contentcapture.ContentCaptureHelper.TAG, "Setting logging level to " + android.view.contentcapture.ContentCaptureHelper.getLoggingLevelAsString(level));
        android.view.contentcapture.ContentCaptureHelper.sVerbose = android.view.contentcapture.ContentCaptureHelper.sDebug = false;
        switch (level) {
            case android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_VERBOSE :
                android.view.contentcapture.ContentCaptureHelper.sVerbose = true;
                // fall through
            case android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_DEBUG :
                android.view.contentcapture.ContentCaptureHelper.sDebug = true;
                return;
            case android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_OFF :
                // You log nothing, Jon Snow!
                return;
            default :
                android.util.Log.w(android.view.contentcapture.ContentCaptureHelper.TAG, "setLoggingLevel(): invalud level: " + level);
        }
    }

    /**
     * Gets a user-friendly value for a content capture logging level.
     */
    public static java.lang.String getLoggingLevelAsString(@android.view.contentcapture.ContentCaptureManager.LoggingLevel
    int level) {
        switch (level) {
            case android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_OFF :
                return "OFF";
            case android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_DEBUG :
                return "DEBUG";
            case android.view.contentcapture.ContentCaptureManager.LOGGING_LEVEL_VERBOSE :
                return "VERBOSE";
            default :
                return "UNKNOWN-" + level;
        }
    }

    /**
     * Converts a set to a list.
     */
    @android.annotation.Nullable
    public static <T> java.util.ArrayList<T> toList(@android.annotation.Nullable
    java.util.Set<T> set) {
        return set == null ? null : new java.util.ArrayList<T>(set);
    }

    /**
     * Converts a list to a set.
     */
    @android.annotation.Nullable
    public static <T> android.util.ArraySet<T> toSet(@android.annotation.Nullable
    java.util.List<T> list) {
        return list == null ? null : new android.util.ArraySet<T>(list);
    }

    private ContentCaptureHelper() {
        throw new java.lang.UnsupportedOperationException("contains only static methods");
    }
}

