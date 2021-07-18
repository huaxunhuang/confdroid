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
package android.media;


/**
 * Listens for Wifi remote display connections managed by the media server.
 *
 * @unknown 
 */
public final class RemoteDisplay {
    /* these constants must be kept in sync with IRemoteDisplayClient.h */
    public static final int DISPLAY_FLAG_SECURE = 1 << 0;

    public static final int DISPLAY_ERROR_UNKOWN = 1;

    public static final int DISPLAY_ERROR_CONNECTION_DROPPED = 2;

    private final dalvik.system.CloseGuard mGuard = dalvik.system.CloseGuard.get();

    private final android.media.RemoteDisplay.Listener mListener;

    private final android.os.Handler mHandler;

    private final java.lang.String mOpPackageName;

    private long mPtr;

    private native long nativeListen(java.lang.String iface, java.lang.String opPackageName);

    private native void nativeDispose(long ptr);

    private native void nativePause(long ptr);

    private native void nativeResume(long ptr);

    private RemoteDisplay(android.media.RemoteDisplay.Listener listener, android.os.Handler handler, java.lang.String opPackageName) {
        mListener = listener;
        mHandler = handler;
        mOpPackageName = opPackageName;
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    /**
     * Starts listening for displays to be connected on the specified interface.
     *
     * @param iface
     * 		The interface address and port in the form "x.x.x.x:y".
     * @param listener
     * 		The listener to invoke when displays are connected or disconnected.
     * @param handler
     * 		The handler on which to invoke the listener.
     */
    public static android.media.RemoteDisplay listen(java.lang.String iface, android.media.RemoteDisplay.Listener listener, android.os.Handler handler, java.lang.String opPackageName) {
        if (iface == null) {
            throw new java.lang.IllegalArgumentException("iface must not be null");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        if (handler == null) {
            throw new java.lang.IllegalArgumentException("handler must not be null");
        }
        android.media.RemoteDisplay display = new android.media.RemoteDisplay(listener, handler, opPackageName);
        display.startListening(iface);
        return display;
    }

    /**
     * Disconnects the remote display and stops listening for new connections.
     */
    public void dispose() {
        dispose(false);
    }

    public void pause() {
        nativePause(mPtr);
    }

    public void resume() {
        nativeResume(mPtr);
    }

    private void dispose(boolean finalized) {
        if (mPtr != 0) {
            if (mGuard != null) {
                if (finalized) {
                    mGuard.warnIfOpen();
                } else {
                    mGuard.close();
                }
            }
            nativeDispose(mPtr);
            mPtr = 0;
        }
    }

    private void startListening(java.lang.String iface) {
        mPtr = nativeListen(iface, mOpPackageName);
        if (mPtr == 0) {
            throw new java.lang.IllegalStateException((("Could not start listening for " + "remote display connection on \"") + iface) + "\"");
        }
        mGuard.open("dispose");
    }

    // Called from native.
    private void notifyDisplayConnected(final android.view.Surface surface, final int width, final int height, final int flags, final int session) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mListener.onDisplayConnected(surface, width, height, flags, session);
            }
        });
    }

    // Called from native.
    private void notifyDisplayDisconnected() {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mListener.onDisplayDisconnected();
            }
        });
    }

    // Called from native.
    private void notifyDisplayError(final int error) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mListener.onDisplayError(error);
            }
        });
    }

    /**
     * Listener invoked when the remote display connection changes state.
     */
    public interface Listener {
        void onDisplayConnected(android.view.Surface surface, int width, int height, int flags, int session);

        void onDisplayDisconnected();

        void onDisplayError(int error);
    }
}

