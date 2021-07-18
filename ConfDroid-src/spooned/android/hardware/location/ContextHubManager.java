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
package android.hardware.location;


/**
 * A class that exposes the Context hubs on a device to applications.
 *
 * Please note that this class is not expected to be used by unbundled applications. Also, calling
 * applications are expected to have LOCATION_HARDWARE permissions to use this class.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class ContextHubManager {
    private static final java.lang.String TAG = "ContextHubManager";

    private final android.os.Looper mMainLooper;

    private android.hardware.location.IContextHubService mContextHubService;

    private android.hardware.location.ContextHubManager.Callback mCallback;

    private android.os.Handler mCallbackHandler;

    /**
     *
     *
     * @deprecated Use {@code mCallback} instead.
     */
    @java.lang.Deprecated
    private android.hardware.location.ContextHubManager.ICallback mLocalCallback;

    /**
     * An interface to receive asynchronous communication from the context hub.
     */
    public static abstract class Callback {
        protected Callback() {
        }

        /**
         * Callback function called on message receipt from context hub.
         *
         * @param hubHandle
         * 		Handle (system-wide unique identifier) of the hub of the message.
         * @param nanoAppHandle
         * 		Handle (unique identifier) for app instance that sent the message.
         * @param message
         * 		The context hub message.
         * @see ContextHubMessage
         */
        public abstract void onMessageReceipt(int hubHandle, int nanoAppHandle, android.hardware.location.ContextHubMessage message);
    }

    /**
     *
     *
     * @deprecated Use {@link Callback} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public interface ICallback {
        /**
         * Callback function called on message receipt from context hub.
         *
         * @param hubHandle
         * 		Handle (system-wide unique identifier) of the hub of the message.
         * @param nanoAppHandle
         * 		Handle (unique identifier) for app instance that sent the message.
         * @param message
         * 		The context hub message.
         * @see ContextHubMessage
         */
        void onMessageReceipt(int hubHandle, int nanoAppHandle, android.hardware.location.ContextHubMessage message);
    }

    /**
     * Get a handle to all the context hubs in the system
     *
     * @return array of context hub handles
     */
    public int[] getContextHubHandles() {
        int[] retVal = null;
        try {
            retVal = getBinder().getContextHubHandles();
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not fetch context hub handles : " + e);
        }
        return retVal;
    }

    /**
     * Get more information about a specific hub.
     *
     * @param hubHandle
     * 		Handle (system-wide unique identifier) of a context hub.
     * @return ContextHubInfo Information about the requested context hub.
     * @see ContextHubInfo
     */
    public android.hardware.location.ContextHubInfo getContextHubInfo(int hubHandle) {
        android.hardware.location.ContextHubInfo retVal = null;
        try {
            retVal = getBinder().getContextHubInfo(hubHandle);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not fetch context hub info :" + e);
        }
        return retVal;
    }

    /**
     * Load a nano app on a specified context hub.
     *
     * Note that loading is asynchronous.  When we return from this method,
     * the nano app (probably) hasn't loaded yet.  Assuming a return of 0
     * from this method, then the final success/failure for the load, along
     * with the "handle" for the nanoapp, is all delivered in a byte
     * string via a call to Callback.onMessageReceipt.
     *
     * TODO(b/30784270): Provide a better success/failure and "handle" delivery.
     *
     * @param hubHandle
     * 		handle of context hub to load the app on.
     * @param app
     * 		the nanoApp to load on the hub
     * @return 0 if the command for loading was sent to the context hub;
    -1 otherwise
     * @see NanoApp
     */
    public int loadNanoApp(int hubHandle, android.hardware.location.NanoApp app) {
        int retVal = -1;
        if (app == null) {
            return retVal;
        }
        try {
            retVal = getBinder().loadNanoApp(hubHandle, app);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not load nanoApp :" + e);
        }
        return retVal;
    }

    /**
     * Unload a specified nanoApp
     *
     * Note that unloading is asynchronous.  When we return from this method,
     * the nano app (probably) hasn't unloaded yet.  Assuming a return of 0
     * from this method, then the final success/failure for the unload is
     * delivered in a byte string via a call to Callback.onMessageReceipt.
     *
     * TODO(b/30784270): Provide a better success/failure delivery.
     *
     * @param nanoAppHandle
     * 		handle of the nanoApp to unload
     * @return 0 if the command for unloading was sent to the context hub;
    -1 otherwise
     */
    public int unloadNanoApp(int nanoAppHandle) {
        int retVal = -1;
        try {
            retVal = getBinder().unloadNanoApp(nanoAppHandle);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not fetch unload nanoApp :" + e);
        }
        return retVal;
    }

    /**
     * get information about the nano app instance
     *
     * NOTE: The returned NanoAppInstanceInfo does _not_ contain correct
     * information for several fields, specifically:
     * - getName()
     * - getPublisher()
     * - getNeededExecMemBytes()
     * - getNeededReadMemBytes()
     * - getNeededWriteMemBytes()
     *
     * For example, say you call loadNanoApp() with a NanoApp that has
     * getName() returning "My Name".  Later, if you call getNanoAppInstanceInfo
     * for that nanoapp, the returned NanoAppInstanceInfo's getName()
     * method will claim "Preloaded app, unknown", even though you would
     * have expected "My Name".  For now, as the user, you'll need to
     * separately track the above fields if they are of interest to you.
     *
     * TODO(b/30943489): Have the returned NanoAppInstanceInfo contain the
     *     correct information.
     *
     * @param nanoAppHandle
     * 		handle of the nanoAppInstance
     * @return NanoAppInstanceInfo Information about the nano app instance.
     * @see NanoAppInstanceInfo
     */
    public android.hardware.location.NanoAppInstanceInfo getNanoAppInstanceInfo(int nanoAppHandle) {
        android.hardware.location.NanoAppInstanceInfo retVal = null;
        try {
            retVal = getBinder().getNanoAppInstanceInfo(nanoAppHandle);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not fetch nanoApp info :" + e);
        }
        return retVal;
    }

    /**
     * Find a specified nano app on the system
     *
     * @param hubHandle
     * 		handle of hub to search for nano app
     * @param filter
     * 		filter specifying the search criteria for app
     * @see NanoAppFilter
     * @return int[] Array of handles to any found nano apps
     */
    public int[] findNanoAppOnHub(int hubHandle, android.hardware.location.NanoAppFilter filter) {
        int[] retVal = null;
        try {
            retVal = getBinder().findNanoAppOnHub(hubHandle, filter);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not query nanoApp instance :" + e);
        }
        return retVal;
    }

    /**
     * Send a message to a specific nano app instance on a context hub.
     *
     * Note that the return value of this method only speaks of success
     * up to the point of sending this to the Context Hub.  It is not
     * an assurance that the Context Hub successfully sent this message
     * on to the nanoapp.  If assurance is desired, a protocol should be
     * established between your code and the nanoapp, with the nanoapp
     * sending a confirmation message (which will be reported via
     * Callback.onMessageReceipt).
     *
     * @param hubHandle
     * 		handle of the hub to send the message to
     * @param nanoAppHandle
     * 		handle of the nano app to send to
     * @param message
     * 		Message to be sent
     * @see ContextHubMessage
     * @return int 0 on success, -1 otherwise
     */
    public int sendMessage(int hubHandle, int nanoAppHandle, android.hardware.location.ContextHubMessage message) {
        int retVal = -1;
        if ((message == null) || (message.getData() == null)) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "null ptr");
            return retVal;
        }
        try {
            retVal = getBinder().sendMessage(hubHandle, nanoAppHandle, message);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not send message :" + e.toString());
        }
        return retVal;
    }

    /**
     * Set a callback to receive messages from the context hub
     *
     * @param callback
     * 		Callback object
     * @see Callback
     * @return int 0 on success, -1 otherwise
     */
    public int registerCallback(android.hardware.location.ContextHubManager.Callback callback) {
        return registerCallback(callback, null);
    }

    /**
     *
     *
     * @deprecated Use {@link #registerCallback(Callback)} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public int registerCallback(android.hardware.location.ContextHubManager.ICallback callback) {
        if (mLocalCallback != null) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Max number of local callbacks reached!");
            return -1;
        }
        mLocalCallback = callback;
        return 0;
    }

    /**
     * Set a callback to receive messages from the context hub
     *
     * @param callback
     * 		Callback object
     * @param handler
     * 		Handler object
     * @see Callback
     * @return int 0 on success, -1 otherwise
     */
    public int registerCallback(android.hardware.location.ContextHubManager.Callback callback, android.os.Handler handler) {
        synchronized(this) {
            if (mCallback != null) {
                android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Max number of callbacks reached!");
                return -1;
            }
            mCallback = callback;
            mCallbackHandler = handler;
        }
        return 0;
    }

    /**
     * Unregister a callback for receive messages from the context hub.
     *
     * @see Callback
     * @param callback
     * 		method to deregister
     * @return int 0 on success, -1 otherwise
     */
    public int unregisterCallback(android.hardware.location.ContextHubManager.Callback callback) {
        synchronized(this) {
            if (callback != mCallback) {
                android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Cannot recognize callback!");
                return -1;
            }
            mCallback = null;
            mCallbackHandler = null;
        }
        return 0;
    }

    /**
     *
     *
     * @deprecated Use {@link #unregisterCallback(Callback)} instead.
     * @unknown 
     */
    public synchronized int unregisterCallback(android.hardware.location.ContextHubManager.ICallback callback) {
        if (callback != mLocalCallback) {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Cannot recognize local callback!");
            return -1;
        }
        mLocalCallback = null;
        return 0;
    }

    private IContextHubCallback.Stub mClientCallback = new android.hardware.location.IContextHubCallback.Stub() {
        @java.lang.Override
        public void onMessageReceipt(final int hubId, final int nanoAppId, final android.hardware.location.ContextHubMessage message) {
            if (mCallback != null) {
                synchronized(this) {
                    final android.hardware.location.ContextHubManager.Callback callback = mCallback;
                    android.os.Handler handler = (mCallbackHandler == null) ? new android.os.Handler(mMainLooper) : mCallbackHandler;
                    handler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            callback.onMessageReceipt(hubId, nanoAppId, message);
                        }
                    });
                }
            } else
                if (mLocalCallback != null) {
                    // we always ensure that mCallback takes precedence, because mLocalCallback is only
                    // for internal compatibility
                    synchronized(this) {
                        mLocalCallback.onMessageReceipt(hubId, nanoAppId, message);
                    }
                } else {
                    android.util.Log.d(android.hardware.location.ContextHubManager.TAG, "Context hub manager client callback is NULL");
                }

        }
    };

    /**
     *
     *
     * @unknown 
     */
    public ContextHubManager(android.content.Context context, android.os.Looper mainLooper) {
        mMainLooper = mainLooper;
        android.os.IBinder b = android.os.ServiceManager.getService(android.hardware.location.ContextHubService.CONTEXTHUB_SERVICE);
        if (b != null) {
            mContextHubService = IContextHubService.Stub.asInterface(b);
            try {
                getBinder().registerCallback(mClientCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "Could not register callback:" + e);
            }
        } else {
            android.util.Log.w(android.hardware.location.ContextHubManager.TAG, "failed to getService");
        }
    }

    private android.hardware.location.IContextHubService getBinder() throws android.os.RemoteException {
        if (mContextHubService == null) {
            throw new android.os.RemoteException("Service not connected.");
        }
        return mContextHubService;
    }
}

