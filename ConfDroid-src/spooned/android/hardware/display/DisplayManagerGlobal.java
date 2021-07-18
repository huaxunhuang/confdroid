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
package android.hardware.display;


/**
 * Manager communication with the display manager service on behalf of
 * an application process.  You're probably looking for {@link DisplayManager}.
 *
 * @unknown 
 */
public final class DisplayManagerGlobal {
    private static final java.lang.String TAG = "DisplayManager";

    private static final boolean DEBUG = false;

    // True if display info and display ids should be cached.
    // 
    // FIXME: The cache is currently disabled because it's unclear whether we have the
    // necessary guarantees that the caches will always be flushed before clients
    // attempt to observe their new state.  For example, depending on the order
    // in which the binder transactions take place, we might have a problem where
    // an application could start processing a configuration change due to a display
    // orientation change before the display info cache has actually been invalidated.
    private static final boolean USE_CACHE = false;

    public static final int EVENT_DISPLAY_ADDED = 1;

    public static final int EVENT_DISPLAY_CHANGED = 2;

    public static final int EVENT_DISPLAY_REMOVED = 3;

    private static android.hardware.display.DisplayManagerGlobal sInstance;

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.hardware.display.IDisplayManager mDm;

    private android.hardware.display.DisplayManagerGlobal.DisplayManagerCallback mCallback;

    private final java.util.ArrayList<android.hardware.display.DisplayManagerGlobal.DisplayListenerDelegate> mDisplayListeners = new java.util.ArrayList<android.hardware.display.DisplayManagerGlobal.DisplayListenerDelegate>();

    private final android.util.SparseArray<android.view.DisplayInfo> mDisplayInfoCache = new android.util.SparseArray<android.view.DisplayInfo>();

    private int[] mDisplayIdCache;

    private int mWifiDisplayScanNestCount;

    private DisplayManagerGlobal(android.hardware.display.IDisplayManager dm) {
        mDm = dm;
    }

    /**
     * Gets an instance of the display manager global singleton.
     *
     * @return The display manager instance, may be null early in system startup
    before the display manager has been fully initialized.
     */
    public static android.hardware.display.DisplayManagerGlobal getInstance() {
        synchronized(android.hardware.display.DisplayManagerGlobal.class) {
            if (android.hardware.display.DisplayManagerGlobal.sInstance == null) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.DISPLAY_SERVICE);
                if (b != null) {
                    android.hardware.display.DisplayManagerGlobal.sInstance = new android.hardware.display.DisplayManagerGlobal(IDisplayManager.Stub.asInterface(b));
                }
            }
            return android.hardware.display.DisplayManagerGlobal.sInstance;
        }
    }

    /**
     * Get information about a particular logical display.
     *
     * @param displayId
     * 		The logical display id.
     * @return Information about the specified display, or null if it does not exist.
    This object belongs to an internal cache and should be treated as if it were immutable.
     */
    public android.view.DisplayInfo getDisplayInfo(int displayId) {
        try {
            synchronized(mLock) {
                android.view.DisplayInfo info;
                if (android.hardware.display.DisplayManagerGlobal.USE_CACHE) {
                    info = mDisplayInfoCache.get(displayId);
                    if (info != null) {
                        return info;
                    }
                }
                info = mDm.getDisplayInfo(displayId);
                if (info == null) {
                    return null;
                }
                if (android.hardware.display.DisplayManagerGlobal.USE_CACHE) {
                    mDisplayInfoCache.put(displayId, info);
                }
                registerCallbackIfNeededLocked();
                if (android.hardware.display.DisplayManagerGlobal.DEBUG) {
                    android.util.Log.d(android.hardware.display.DisplayManagerGlobal.TAG, (("getDisplayInfo: displayId=" + displayId) + ", info=") + info);
                }
                return info;
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets all currently valid logical display ids.
     *
     * @return An array containing all display ids.
     */
    public int[] getDisplayIds() {
        try {
            synchronized(mLock) {
                if (android.hardware.display.DisplayManagerGlobal.USE_CACHE) {
                    if (mDisplayIdCache != null) {
                        return mDisplayIdCache;
                    }
                }
                int[] displayIds = mDm.getDisplayIds();
                if (android.hardware.display.DisplayManagerGlobal.USE_CACHE) {
                    mDisplayIdCache = displayIds;
                }
                registerCallbackIfNeededLocked();
                return displayIds;
            }
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /**
     * Gets information about a logical display.
     *
     * The display metrics may be adjusted to provide compatibility
     * for legacy applications or limited screen areas.
     *
     * @param displayId
     * 		The logical display id.
     * @param daj
     * 		The compatibility info and activityToken.
     * @return The display object, or null if there is no display with the given id.
     */
    public android.view.Display getCompatibleDisplay(int displayId, android.view.DisplayAdjustments daj) {
        android.view.DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new android.view.Display(this, displayId, displayInfo, daj);
    }

    /**
     * Gets information about a logical display without applying any compatibility metrics.
     *
     * @param displayId
     * 		The logical display id.
     * @return The display object, or null if there is no display with the given id.
     */
    public android.view.Display getRealDisplay(int displayId) {
        return getCompatibleDisplay(displayId, android.view.DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    public void registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener listener, android.os.Handler handler) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        synchronized(mLock) {
            int index = findDisplayListenerLocked(listener);
            if (index < 0) {
                mDisplayListeners.add(new android.hardware.display.DisplayManagerGlobal.DisplayListenerDelegate(listener, handler));
                registerCallbackIfNeededLocked();
            }
        }
    }

    public void unregisterDisplayListener(android.hardware.display.DisplayManager.DisplayListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener must not be null");
        }
        synchronized(mLock) {
            int index = findDisplayListenerLocked(listener);
            if (index >= 0) {
                android.hardware.display.DisplayManagerGlobal.DisplayListenerDelegate d = mDisplayListeners.get(index);
                d.clearEvents();
                mDisplayListeners.remove(index);
            }
        }
    }

    private int findDisplayListenerLocked(android.hardware.display.DisplayManager.DisplayListener listener) {
        final int numListeners = mDisplayListeners.size();
        for (int i = 0; i < numListeners; i++) {
            if (mDisplayListeners.get(i).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    private void registerCallbackIfNeededLocked() {
        if (mCallback == null) {
            mCallback = new android.hardware.display.DisplayManagerGlobal.DisplayManagerCallback();
            try {
                mDm.registerCallback(mCallback);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    private void handleDisplayEvent(int displayId, int event) {
        synchronized(mLock) {
            if (android.hardware.display.DisplayManagerGlobal.USE_CACHE) {
                mDisplayInfoCache.remove(displayId);
                if ((event == android.hardware.display.DisplayManagerGlobal.EVENT_DISPLAY_ADDED) || (event == android.hardware.display.DisplayManagerGlobal.EVENT_DISPLAY_REMOVED)) {
                    mDisplayIdCache = null;
                }
            }
            final int numListeners = mDisplayListeners.size();
            for (int i = 0; i < numListeners; i++) {
                mDisplayListeners.get(i).sendDisplayEvent(displayId, event);
            }
        }
    }

    public void startWifiDisplayScan() {
        synchronized(mLock) {
            if ((mWifiDisplayScanNestCount++) == 0) {
                registerCallbackIfNeededLocked();
                try {
                    mDm.startWifiDisplayScan();
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
        }
    }

    public void stopWifiDisplayScan() {
        synchronized(mLock) {
            if ((--mWifiDisplayScanNestCount) == 0) {
                try {
                    mDm.stopWifiDisplayScan();
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } else
                if (mWifiDisplayScanNestCount < 0) {
                    android.util.Log.wtf(android.hardware.display.DisplayManagerGlobal.TAG, "Wifi display scan nest count became negative: " + mWifiDisplayScanNestCount);
                    mWifiDisplayScanNestCount = 0;
                }

        }
    }

    public void connectWifiDisplay(java.lang.String deviceAddress) {
        if (deviceAddress == null) {
            throw new java.lang.IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            mDm.connectWifiDisplay(deviceAddress);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void pauseWifiDisplay() {
        try {
            mDm.pauseWifiDisplay();
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void resumeWifiDisplay() {
        try {
            mDm.resumeWifiDisplay();
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void disconnectWifiDisplay() {
        try {
            mDm.disconnectWifiDisplay();
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void renameWifiDisplay(java.lang.String deviceAddress, java.lang.String alias) {
        if (deviceAddress == null) {
            throw new java.lang.IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            mDm.renameWifiDisplay(deviceAddress, alias);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void forgetWifiDisplay(java.lang.String deviceAddress) {
        if (deviceAddress == null) {
            throw new java.lang.IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            mDm.forgetWifiDisplay(deviceAddress);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public android.hardware.display.WifiDisplayStatus getWifiDisplayStatus() {
        try {
            return mDm.getWifiDisplayStatus();
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void requestColorMode(int displayId, int colorMode) {
        try {
            mDm.requestColorMode(displayId, colorMode);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public android.hardware.display.VirtualDisplay createVirtualDisplay(android.content.Context context, android.media.projection.MediaProjection projection, java.lang.String name, int width, int height, int densityDpi, android.view.Surface surface, int flags, android.hardware.display.VirtualDisplay.Callback callback, android.os.Handler handler) {
        if (android.text.TextUtils.isEmpty(name)) {
            throw new java.lang.IllegalArgumentException("name must be non-null and non-empty");
        }
        if (((width <= 0) || (height <= 0)) || (densityDpi <= 0)) {
            throw new java.lang.IllegalArgumentException("width, height, and densityDpi must be " + "greater than 0");
        }
        android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallback callbackWrapper = new android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallback(callback, handler);
        android.media.projection.IMediaProjection projectionToken = (projection != null) ? projection.getProjection() : null;
        int displayId;
        try {
            displayId = mDm.createVirtualDisplay(callbackWrapper, projectionToken, context.getPackageName(), name, width, height, densityDpi, surface, flags);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
        if (displayId < 0) {
            android.util.Log.e(android.hardware.display.DisplayManagerGlobal.TAG, "Could not create virtual display: " + name);
            return null;
        }
        android.view.Display display = getRealDisplay(displayId);
        if (display == null) {
            android.util.Log.wtf(android.hardware.display.DisplayManagerGlobal.TAG, ("Could not obtain display info for newly created " + "virtual display: ") + name);
            try {
                mDm.releaseVirtualDisplay(callbackWrapper);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
            return null;
        }
        return new android.hardware.display.VirtualDisplay(this, display, callbackWrapper, surface);
    }

    public void setVirtualDisplaySurface(android.hardware.display.IVirtualDisplayCallback token, android.view.Surface surface) {
        try {
            mDm.setVirtualDisplaySurface(token, surface);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void resizeVirtualDisplay(android.hardware.display.IVirtualDisplayCallback token, int width, int height, int densityDpi) {
        try {
            mDm.resizeVirtualDisplay(token, width, height, densityDpi);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void releaseVirtualDisplay(android.hardware.display.IVirtualDisplayCallback token) {
        try {
            mDm.releaseVirtualDisplay(token);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private final class DisplayManagerCallback extends android.hardware.display.IDisplayManagerCallback.Stub {
        @java.lang.Override
        public void onDisplayEvent(int displayId, int event) {
            if (android.hardware.display.DisplayManagerGlobal.DEBUG) {
                android.util.Log.d(android.hardware.display.DisplayManagerGlobal.TAG, (("onDisplayEvent: displayId=" + displayId) + ", event=") + event);
            }
            handleDisplayEvent(displayId, event);
        }
    }

    private static final class DisplayListenerDelegate extends android.os.Handler {
        public final android.hardware.display.DisplayManager.DisplayListener mListener;

        public DisplayListenerDelegate(android.hardware.display.DisplayManager.DisplayListener listener, android.os.Handler handler) {
            /* async */
            super(handler != null ? handler.getLooper() : android.os.Looper.myLooper(), null, true);
            mListener = listener;
        }

        public void sendDisplayEvent(int displayId, int event) {
            android.os.Message msg = obtainMessage(event, displayId, 0);
            sendMessage(msg);
        }

        public void clearEvents() {
            removeCallbacksAndMessages(null);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.hardware.display.DisplayManagerGlobal.EVENT_DISPLAY_ADDED :
                    mListener.onDisplayAdded(msg.arg1);
                    break;
                case android.hardware.display.DisplayManagerGlobal.EVENT_DISPLAY_CHANGED :
                    mListener.onDisplayChanged(msg.arg1);
                    break;
                case android.hardware.display.DisplayManagerGlobal.EVENT_DISPLAY_REMOVED :
                    mListener.onDisplayRemoved(msg.arg1);
                    break;
            }
        }
    }

    private static final class VirtualDisplayCallback extends android.hardware.display.IVirtualDisplayCallback.Stub {
        private android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate mDelegate;

        public VirtualDisplayCallback(android.hardware.display.VirtualDisplay.Callback callback, android.os.Handler handler) {
            if (callback != null) {
                mDelegate = new android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate(callback, handler);
            }
        }

        // Binder call
        @java.lang.Override
        public void onPaused() {
            if (mDelegate != null) {
                mDelegate.sendEmptyMessage(android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate.MSG_DISPLAY_PAUSED);
            }
        }

        // Binder call
        @java.lang.Override
        public void onResumed() {
            if (mDelegate != null) {
                mDelegate.sendEmptyMessage(android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate.MSG_DISPLAY_RESUMED);
            }
        }

        // Binder call
        @java.lang.Override
        public void onStopped() {
            if (mDelegate != null) {
                mDelegate.sendEmptyMessage(android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate.MSG_DISPLAY_STOPPED);
            }
        }
    }

    private static final class VirtualDisplayCallbackDelegate extends android.os.Handler {
        public static final int MSG_DISPLAY_PAUSED = 0;

        public static final int MSG_DISPLAY_RESUMED = 1;

        public static final int MSG_DISPLAY_STOPPED = 2;

        private final android.hardware.display.VirtualDisplay.Callback mCallback;

        public VirtualDisplayCallbackDelegate(android.hardware.display.VirtualDisplay.Callback callback, android.os.Handler handler) {
            /* async */
            super(handler != null ? handler.getLooper() : android.os.Looper.myLooper(), null, true);
            mCallback = callback;
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate.MSG_DISPLAY_PAUSED :
                    mCallback.onPaused();
                    break;
                case android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate.MSG_DISPLAY_RESUMED :
                    mCallback.onResumed();
                    break;
                case android.hardware.display.DisplayManagerGlobal.VirtualDisplayCallbackDelegate.MSG_DISPLAY_STOPPED :
                    mCallback.onStopped();
                    break;
            }
        }
    }
}

