/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class GLEnvironment {
    private int glEnvId;

    private boolean mManageContext = true;

    public GLEnvironment() {
        nativeAllocate();
    }

    private GLEnvironment(android.filterfw.core.NativeAllocatorTag tag) {
    }

    public synchronized void tearDown() {
        if (glEnvId != (-1)) {
            nativeDeallocate();
            glEnvId = -1;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        tearDown();
    }

    public void initWithNewContext() {
        mManageContext = true;
        if (!nativeInitWithNewContext()) {
            throw new java.lang.RuntimeException("Could not initialize GLEnvironment with new context!");
        }
    }

    public void initWithCurrentContext() {
        mManageContext = false;
        if (!nativeInitWithCurrentContext()) {
            throw new java.lang.RuntimeException("Could not initialize GLEnvironment with current context!");
        }
    }

    public boolean isActive() {
        return nativeIsActive();
    }

    public boolean isContextActive() {
        return nativeIsContextActive();
    }

    public static boolean isAnyContextActive() {
        return android.filterfw.core.GLEnvironment.nativeIsAnyContextActive();
    }

    public void activate() {
        if ((android.os.Looper.myLooper() != null) && android.os.Looper.myLooper().equals(android.os.Looper.getMainLooper())) {
            android.util.Log.e("FilterFramework", "Activating GL context in UI thread!");
        }
        if (mManageContext && (!nativeActivate())) {
            throw new java.lang.RuntimeException("Could not activate GLEnvironment!");
        }
    }

    public void deactivate() {
        if (mManageContext && (!nativeDeactivate())) {
            throw new java.lang.RuntimeException("Could not deactivate GLEnvironment!");
        }
    }

    public void swapBuffers() {
        if (!nativeSwapBuffers()) {
            throw new java.lang.RuntimeException("Error swapping EGL buffers!");
        }
    }

    public int registerSurface(android.view.Surface surface) {
        int result = nativeAddSurface(surface);
        if (result < 0) {
            throw new java.lang.RuntimeException(("Error registering surface " + surface) + "!");
        }
        return result;
    }

    public int registerSurfaceTexture(android.graphics.SurfaceTexture surfaceTexture, int width, int height) {
        android.view.Surface surface = new android.view.Surface(surfaceTexture);
        int result = nativeAddSurfaceWidthHeight(surface, width, height);
        surface.release();
        if (result < 0) {
            throw new java.lang.RuntimeException(("Error registering surfaceTexture " + surfaceTexture) + "!");
        }
        return result;
    }

    public int registerSurfaceFromMediaRecorder(android.media.MediaRecorder mediaRecorder) {
        int result = nativeAddSurfaceFromMediaRecorder(mediaRecorder);
        if (result < 0) {
            throw new java.lang.RuntimeException((("Error registering surface from " + "MediaRecorder") + mediaRecorder) + "!");
        }
        return result;
    }

    public void activateSurfaceWithId(int surfaceId) {
        if (!nativeActivateSurfaceId(surfaceId)) {
            throw new java.lang.RuntimeException(("Could not activate surface " + surfaceId) + "!");
        }
    }

    public void unregisterSurfaceId(int surfaceId) {
        if (!nativeRemoveSurfaceId(surfaceId)) {
            throw new java.lang.RuntimeException(("Could not unregister surface " + surfaceId) + "!");
        }
    }

    public void setSurfaceTimestamp(long timestamp) {
        if (!nativeSetSurfaceTimestamp(timestamp)) {
            throw new java.lang.RuntimeException("Could not set timestamp for current surface!");
        }
    }

    static {
        java.lang.System.loadLibrary("filterfw");
    }

    private native boolean nativeInitWithNewContext();

    private native boolean nativeInitWithCurrentContext();

    private native boolean nativeIsActive();

    private native boolean nativeIsContextActive();

    private static native boolean nativeIsAnyContextActive();

    private native boolean nativeActivate();

    private native boolean nativeDeactivate();

    private native boolean nativeSwapBuffers();

    private native boolean nativeAllocate();

    private native boolean nativeDeallocate();

    private native int nativeAddSurface(android.view.Surface surface);

    private native int nativeAddSurfaceWidthHeight(android.view.Surface surface, int width, int height);

    private native int nativeAddSurfaceFromMediaRecorder(android.media.MediaRecorder mediaRecorder);

    private native boolean nativeDisconnectSurfaceMediaSource(android.media.MediaRecorder mediaRecorder);

    private native boolean nativeActivateSurfaceId(int surfaceId);

    private native boolean nativeRemoveSurfaceId(int surfaceId);

    private native boolean nativeSetSurfaceTimestamp(long timestamp);
}

