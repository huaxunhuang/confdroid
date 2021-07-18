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
package android.graphics;


/**
 *
 *
 * @unknown 
 */
public final class BLASTBufferQueue {
    // Note: This field is accessed by native code.
    private long mNativeObject;// BLASTBufferQueue*


    private static native long nativeCreate(long surfaceControl, long width, long height, boolean tripleBufferingEnabled);

    private static native void nativeDestroy(long ptr);

    private static native android.view.Surface nativeGetSurface(long ptr);

    private static native void nativeSetNextTransaction(long ptr, long transactionPtr);

    private static native void nativeUpdate(long ptr, long surfaceControl, long width, long height);

    /**
     * Create a new connection with the surface flinger.
     */
    public BLASTBufferQueue(android.view.SurfaceControl sc, int width, int height, boolean tripleBufferingEnabled) {
        mNativeObject = android.graphics.BLASTBufferQueue.nativeCreate(sc.mNativeObject, width, height, tripleBufferingEnabled);
    }

    public void destroy() {
        android.graphics.BLASTBufferQueue.nativeDestroy(mNativeObject);
    }

    public android.view.Surface getSurface() {
        return android.graphics.BLASTBufferQueue.nativeGetSurface(mNativeObject);
    }

    public void setNextTransaction(android.view.SurfaceControl.Transaction t) {
        android.graphics.BLASTBufferQueue.nativeSetNextTransaction(mNativeObject, t.mNativeObject);
    }

    public void update(android.view.SurfaceControl sc, int width, int height) {
        android.graphics.BLASTBufferQueue.nativeUpdate(mNativeObject, sc.mNativeObject, width, height);
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (mNativeObject != 0) {
                android.graphics.BLASTBufferQueue.nativeDestroy(mNativeObject);
            }
        } finally {
            super.finalize();
        }
    }
}

