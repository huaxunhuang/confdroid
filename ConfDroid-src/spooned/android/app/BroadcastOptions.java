/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Helper class for building an options Bundle that can be used with
 * {@link android.content.Context#sendBroadcast(android.content.Intent)
 * Context.sendBroadcast(Intent)} and related methods.
 * {@hide }
 */
@android.annotation.SystemApi
public class BroadcastOptions {
    private long mTemporaryAppWhitelistDuration;

    private int mMinManifestReceiverApiLevel = 0;

    private int mMaxManifestReceiverApiLevel = android.os.Build.VERSION_CODES.CUR_DEVELOPMENT;

    /**
     * How long to temporarily put an app on the power whitelist when executing this broadcast
     * to it.
     */
    static final java.lang.String KEY_TEMPORARY_APP_WHITELIST_DURATION = "android:broadcast.temporaryAppWhitelistDuration";

    /**
     * Corresponds to {@link #setMinManifestReceiverApiLevel}.
     */
    static final java.lang.String KEY_MIN_MANIFEST_RECEIVER_API_LEVEL = "android:broadcast.minManifestReceiverApiLevel";

    /**
     * Corresponds to {@link #setMaxManifestReceiverApiLevel}.
     */
    static final java.lang.String KEY_MAX_MANIFEST_RECEIVER_API_LEVEL = "android:broadcast.maxManifestReceiverApiLevel";

    public static android.app.BroadcastOptions makeBasic() {
        android.app.BroadcastOptions opts = new android.app.BroadcastOptions();
        return opts;
    }

    private BroadcastOptions() {
    }

    /**
     *
     *
     * @unknown 
     */
    public BroadcastOptions(android.os.Bundle opts) {
        mTemporaryAppWhitelistDuration = opts.getLong(android.app.BroadcastOptions.KEY_TEMPORARY_APP_WHITELIST_DURATION);
        mMinManifestReceiverApiLevel = opts.getInt(android.app.BroadcastOptions.KEY_MIN_MANIFEST_RECEIVER_API_LEVEL, 0);
        mMaxManifestReceiverApiLevel = opts.getInt(android.app.BroadcastOptions.KEY_MAX_MANIFEST_RECEIVER_API_LEVEL, android.os.Build.VERSION_CODES.CUR_DEVELOPMENT);
    }

    /**
     * Set a duration for which the system should temporary place an application on the
     * power whitelist when this broadcast is being delivered to it.
     *
     * @param duration
     * 		The duration in milliseconds; 0 means to not place on whitelist.
     */
    public void setTemporaryAppWhitelistDuration(long duration) {
        mTemporaryAppWhitelistDuration = duration;
    }

    /**
     * Return {@link #setTemporaryAppWhitelistDuration}.
     *
     * @unknown 
     */
    public long getTemporaryAppWhitelistDuration() {
        return mTemporaryAppWhitelistDuration;
    }

    /**
     * Set the minimum target API level of receivers of the broadcast.  If an application
     * is targeting an API level less than this, the broadcast will not be delivered to
     * them.  This only applies to receivers declared in the app's AndroidManifest.xml.
     *
     * @unknown 
     */
    public void setMinManifestReceiverApiLevel(int apiLevel) {
        mMinManifestReceiverApiLevel = apiLevel;
    }

    /**
     * Return {@link #setMinManifestReceiverApiLevel}.
     *
     * @unknown 
     */
    public int getMinManifestReceiverApiLevel() {
        return mMinManifestReceiverApiLevel;
    }

    /**
     * Set the maximum target API level of receivers of the broadcast.  If an application
     * is targeting an API level greater than this, the broadcast will not be delivered to
     * them.  This only applies to receivers declared in the app's AndroidManifest.xml.
     *
     * @unknown 
     */
    public void setMaxManifestReceiverApiLevel(int apiLevel) {
        mMaxManifestReceiverApiLevel = apiLevel;
    }

    /**
     * Return {@link #setMaxManifestReceiverApiLevel}.
     *
     * @unknown 
     */
    public int getMaxManifestReceiverApiLevel() {
        return mMaxManifestReceiverApiLevel;
    }

    /**
     * Returns the created options as a Bundle, which can be passed to
     * {@link android.content.Context#sendBroadcast(android.content.Intent)
     * Context.sendBroadcast(Intent)} and related methods.
     * Note that the returned Bundle is still owned by the BroadcastOptions
     * object; you must not modify it, but can supply it to the sendBroadcast
     * methods that take an options Bundle.
     */
    public android.os.Bundle toBundle() {
        android.os.Bundle b = new android.os.Bundle();
        if (mTemporaryAppWhitelistDuration > 0) {
            b.putLong(android.app.BroadcastOptions.KEY_TEMPORARY_APP_WHITELIST_DURATION, mTemporaryAppWhitelistDuration);
        }
        if (mMinManifestReceiverApiLevel != 0) {
            b.putInt(android.app.BroadcastOptions.KEY_MIN_MANIFEST_RECEIVER_API_LEVEL, mMinManifestReceiverApiLevel);
        }
        if (mMaxManifestReceiverApiLevel != android.os.Build.VERSION_CODES.CUR_DEVELOPMENT) {
            b.putInt(android.app.BroadcastOptions.KEY_MAX_MANIFEST_RECEIVER_API_LEVEL, mMaxManifestReceiverApiLevel);
        }
        return b.isEmpty() ? null : b;
    }
}

