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
 * Vibrator implementation that does nothing.
 *
 * @unknown 
 */
public class NullVibrator extends android.os.Vibrator {
    private static final android.os.NullVibrator sInstance = new android.os.NullVibrator();

    private NullVibrator() {
    }

    public static android.os.NullVibrator getInstance() {
        return android.os.NullVibrator.sInstance;
    }

    @java.lang.Override
    public boolean hasVibrator() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void vibrate(int uid, java.lang.String opPkg, long milliseconds, android.media.AudioAttributes attributes) {
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void vibrate(int uid, java.lang.String opPkg, long[] pattern, int repeat, android.media.AudioAttributes attributes) {
        if (repeat >= pattern.length) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
    }

    @java.lang.Override
    public void cancel() {
    }
}

