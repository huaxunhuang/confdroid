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
package android.support.transition;


/**
 * Backport of WindowId.
 *
 * <p>Since the use of WindowId in Transition API is limited to identifying windows, we can just
 * wrap a window token and use it as an identifier.</p>
 */
class WindowIdPort {
    private final android.os.IBinder mToken;

    private WindowIdPort(android.os.IBinder token) {
        mToken = token;
    }

    static android.support.transition.WindowIdPort getWindowId(@android.support.annotation.NonNull
    android.view.View view) {
        return new android.support.transition.WindowIdPort(view.getWindowToken());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        return (obj instanceof android.support.transition.WindowIdPort) && ((android.support.transition.WindowIdPort) (obj)).mToken.equals(this.mToken);
    }
}

