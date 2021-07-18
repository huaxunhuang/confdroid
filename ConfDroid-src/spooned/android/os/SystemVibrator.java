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
package android.os;


/**
 * Vibrator implementation that controls the main system vibrator.
 *
 * @unknown 
 */
public class SystemVibrator extends android.os.Vibrator {
    private static final java.lang.String TAG = "Vibrator";

    private final android.os.IVibratorService mService;

    private final android.os.Binder mToken = new android.os.Binder();

    public SystemVibrator() {
        mService = IVibratorService.Stub.asInterface(android.os.ServiceManager.getService("vibrator"));
    }

    public SystemVibrator(android.content.Context context) {
        super(context);
        mService = IVibratorService.Stub.asInterface(android.os.ServiceManager.getService("vibrator"));
    }

    @java.lang.Override
    public boolean hasVibrator() {
        if (mService == null) {
            android.util.Log.w(android.os.SystemVibrator.TAG, "Failed to vibrate; no vibrator service.");
            return false;
        }
        try {
            return mService.hasVibrator();
        } catch (android.os.RemoteException e) {
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void vibrate(int uid, java.lang.String opPkg, long milliseconds, android.media.AudioAttributes attributes) {
        if (mService == null) {
            android.util.Log.w(android.os.SystemVibrator.TAG, "Failed to vibrate; no vibrator service.");
            return;
        }
        try {
            mService.vibrate(uid, opPkg, milliseconds, android.os.SystemVibrator.usageForAttributes(attributes), mToken);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.os.SystemVibrator.TAG, "Failed to vibrate.", e);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void vibrate(int uid, java.lang.String opPkg, long[] pattern, int repeat, android.media.AudioAttributes attributes) {
        if (mService == null) {
            android.util.Log.w(android.os.SystemVibrator.TAG, "Failed to vibrate; no vibrator service.");
            return;
        }
        // catch this here because the server will do nothing.  pattern may
        // not be null, let that be checked, because the server will drop it
        // anyway
        if (repeat < pattern.length) {
            try {
                mService.vibratePattern(uid, opPkg, pattern, repeat, android.os.SystemVibrator.usageForAttributes(attributes), mToken);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.os.SystemVibrator.TAG, "Failed to vibrate.", e);
            }
        } else {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
    }

    private static int usageForAttributes(android.media.AudioAttributes attributes) {
        return attributes != null ? attributes.getUsage() : android.media.AudioAttributes.USAGE_UNKNOWN;
    }

    @java.lang.Override
    public void cancel() {
        if (mService == null) {
            return;
        }
        try {
            mService.cancelVibrate(mToken);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.os.SystemVibrator.TAG, "Failed to cancel vibration.", e);
        }
    }
}

