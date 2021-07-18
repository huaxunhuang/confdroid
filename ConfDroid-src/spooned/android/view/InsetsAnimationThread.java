/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.view;


/**
 * Thread to be used for inset animations to be running off the main thread.
 *
 * @unknown 
 */
public class InsetsAnimationThread extends android.os.HandlerThread {
    private static android.view.InsetsAnimationThread sInstance;

    private static android.os.Handler sHandler;

    private InsetsAnimationThread() {
        // TODO: Should this use higher priority?
        super("InsetsAnimations");
    }

    private static void ensureThreadLocked() {
        if (android.view.InsetsAnimationThread.sInstance == null) {
            android.view.InsetsAnimationThread.sInstance = new android.view.InsetsAnimationThread();
            android.view.InsetsAnimationThread.sInstance.start();
            android.view.InsetsAnimationThread.sInstance.getLooper().setTraceTag(Trace.TRACE_TAG_VIEW);
            android.view.InsetsAnimationThread.sHandler = new android.os.Handler(android.view.InsetsAnimationThread.sInstance.getLooper());
        }
    }

    public static void release() {
        synchronized(android.view.InsetsAnimationThread.class) {
            if (android.view.InsetsAnimationThread.sInstance == null) {
                return;
            }
            android.view.InsetsAnimationThread.sInstance.getLooper().quitSafely();
            android.view.InsetsAnimationThread.sInstance = null;
            android.view.InsetsAnimationThread.sHandler = null;
        }
    }

    public static android.view.InsetsAnimationThread get() {
        synchronized(android.view.InsetsAnimationThread.class) {
            android.view.InsetsAnimationThread.ensureThreadLocked();
            return android.view.InsetsAnimationThread.sInstance;
        }
    }

    public static android.os.Handler getHandler() {
        synchronized(android.view.InsetsAnimationThread.class) {
            android.view.InsetsAnimationThread.ensureThreadLocked();
            return android.view.InsetsAnimationThread.sHandler;
        }
    }
}

