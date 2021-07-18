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
package android.net.wifi.nan;


/**
 * This class provides the primary API for managing Wi-Fi NAN operation:
 * including discovery and data-links. Get an instance of this class by calling
 * {@link android.content.Context#getSystemService(String)
 * Context.getSystemService(Context.WIFI_NAN_SERVICE)}.
 * <p>
 * The class provides access to:
 * <ul>
 * <li>Configure a NAN connection and register for events.
 * <li>Create publish and subscribe sessions.
 * <li>Create NAN network specifier to be used to create a NAN network.
 * </ul>
 *
 * @unknown PROPOSED_NAN_API
 */
public class WifiNanManager {
    private static final java.lang.String TAG = "WifiNanManager";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;// STOPSHIP if true


    private android.os.IBinder mBinder;

    private android.net.wifi.nan.IWifiNanManager mService;

    /**
     * {@hide }
     */
    public WifiNanManager(android.net.wifi.nan.IWifiNanManager service) {
        mService = service;
    }

    /**
     * Re-connect to the Wi-Fi NAN service - enabling the application to execute
     * {@link WifiNanManager} APIs. Application don't normally need to call this
     * API since it is executed in the constructor. However, applications which
     * have explicitly {@link WifiNanManager#disconnect()} need to call this
     * function to re-connect.
     *
     * @param listener
     * 		A listener extended from {@link WifiNanEventListener}.
     * @param events
     * 		The set of events to be delivered to the {@code listener}.
     * 		OR'd event flags from {@link WifiNanEventListener
     * 		NanEventListener.LISTEN*}.
     */
    public void connect(android.net.wifi.nan.WifiNanEventListener listener, int events) {
        try {
            if (android.net.wifi.nan.WifiNanManager.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, "connect()");

            if (listener == null) {
                throw new java.lang.IllegalArgumentException("Invalid listener - must not be null");
            }
            if (mBinder == null) {
                mBinder = new android.os.Binder();
            }
            mService.connect(mBinder, listener.callback, events);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Disconnect from the Wi-Fi NAN service and destroy all outstanding
     * operations - i.e. all publish and subscribes are terminated, any
     * outstanding data-link is shut-down, and all requested NAN configurations
     * are cancelled.
     * <p>
     * An application may then re-connect using
     * {@link WifiNanManager#connect(WifiNanEventListener, int)} .
     */
    public void disconnect() {
        try {
            if (android.net.wifi.nan.WifiNanManager.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, "disconnect()");

            mService.disconnect(mBinder);
            mBinder = null;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Requests a NAN configuration, specified by {@link ConfigRequest}. Note
     * that NAN is a shared resource and the device can only be a member of a
     * single cluster. Thus the service may merge configuration requests from
     * multiple applications and configure NAN differently from individual
     * requests.
     * <p>
     * The {@link WifiNanEventListener#onConfigCompleted(ConfigRequest)} will be
     * called when configuration is completed (if a listener is registered for
     * this specific event).
     *
     * @param configRequest
     * 		The requested NAN configuration.
     */
    public void requestConfig(android.net.wifi.nan.ConfigRequest configRequest) {
        if (android.net.wifi.nan.WifiNanManager.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, "requestConfig(): configRequest=" + configRequest);

        try {
            mService.requestConfig(configRequest);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Request a NAN publish session. The results of the publish session
     * operation will result in callbacks to the indicated listener:
     * {@link WifiNanSessionListener NanSessionListener.on*}.
     *
     * @param publishData
     * 		The {@link PublishData} specifying the contents of the
     * 		publish session.
     * @param publishSettings
     * 		The {@link PublishSettings} specifying the
     * 		settings for the publish session.
     * @param listener
     * 		The {@link WifiNanSessionListener} derived objects to be used
     * 		for the event callbacks specified by {@code events}.
     * @param events
     * 		The list of events to be delivered to the {@code listener}
     * 		object. An OR'd value of {@link WifiNanSessionListener
     * 		NanSessionListener.LISTEN_*}.
     * @return The {@link WifiNanPublishSession} which can be used to further
    control the publish session.
     */
    public android.net.wifi.nan.WifiNanPublishSession publish(android.net.wifi.nan.PublishData publishData, android.net.wifi.nan.PublishSettings publishSettings, android.net.wifi.nan.WifiNanSessionListener listener, int events) {
        return publishRaw(publishData, publishSettings, listener, events | android.net.wifi.nan.WifiNanSessionListener.LISTEN_HIDDEN_FLAGS);
    }

    /**
     * Same as publish(*) but does not modify the event flag
     *
     * @unknown 
     */
    public android.net.wifi.nan.WifiNanPublishSession publishRaw(android.net.wifi.nan.PublishData publishData, android.net.wifi.nan.PublishSettings publishSettings, android.net.wifi.nan.WifiNanSessionListener listener, int events) {
        if (android.net.wifi.nan.WifiNanManager.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, (("publish(): data='" + publishData) + "', settings=") + publishSettings);

        if ((publishSettings.mPublishType == android.net.wifi.nan.PublishSettings.PUBLISH_TYPE_UNSOLICITED) && (publishData.mRxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid publish data & settings: UNSOLICITED " + "publishes (active) can't have an Rx filter");
        }
        if ((publishSettings.mPublishType == android.net.wifi.nan.PublishSettings.PUBLISH_TYPE_SOLICITED) && (publishData.mTxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid publish data & settings: SOLICITED " + "publishes (passive) can't have a Tx filter");
        }
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("Invalid listener - must not be null");
        }
        int sessionId;
        try {
            sessionId = mService.createSession(listener.callback, events);
            if (android.net.wifi.nan.WifiNanManager.DBG)
                android.util.Log.d(android.net.wifi.nan.WifiNanManager.TAG, "publish: session created - sessionId=" + sessionId);

            mService.publish(sessionId, publishData, publishSettings);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        return new android.net.wifi.nan.WifiNanPublishSession(this, sessionId);
    }

    /**
     * {@hide }
     */
    public void publish(int sessionId, android.net.wifi.nan.PublishData publishData, android.net.wifi.nan.PublishSettings publishSettings) {
        if (android.net.wifi.nan.WifiNanManager.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, (("publish(): data='" + publishData) + "', settings=") + publishSettings);

        if ((publishSettings.mPublishType == android.net.wifi.nan.PublishSettings.PUBLISH_TYPE_UNSOLICITED) && (publishData.mRxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid publish data & settings: UNSOLICITED " + "publishes (active) can't have an Rx filter");
        }
        if ((publishSettings.mPublishType == android.net.wifi.nan.PublishSettings.PUBLISH_TYPE_SOLICITED) && (publishData.mTxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid publish data & settings: SOLICITED " + "publishes (passive) can't have a Tx filter");
        }
        try {
            mService.publish(sessionId, publishData, publishSettings);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Request a NAN subscribe session. The results of the subscribe session
     * operation will result in callbacks to the indicated listener:
     * {@link WifiNanSessionListener NanSessionListener.on*}.
     *
     * @param subscribeData
     * 		The {@link SubscribeData} specifying the contents of
     * 		the subscribe session.
     * @param subscribeSettings
     * 		The {@link SubscribeSettings} specifying the
     * 		settings for the subscribe session.
     * @param listener
     * 		The {@link WifiNanSessionListener} derived objects to be used
     * 		for the event callbacks specified by {@code events}.
     * @param events
     * 		The list of events to be delivered to the {@code listener}
     * 		object. An OR'd value of {@link WifiNanSessionListener
     * 		NanSessionListener.LISTEN_*}.
     * @return The {@link WifiNanSubscribeSession} which can be used to further
    control the subscribe session.
     */
    public android.net.wifi.nan.WifiNanSubscribeSession subscribe(android.net.wifi.nan.SubscribeData subscribeData, android.net.wifi.nan.SubscribeSettings subscribeSettings, android.net.wifi.nan.WifiNanSessionListener listener, int events) {
        return subscribeRaw(subscribeData, subscribeSettings, listener, events | android.net.wifi.nan.WifiNanSessionListener.LISTEN_HIDDEN_FLAGS);
    }

    /**
     * Same as subscribe(*) but does not modify the event flag
     *
     * @unknown 
     */
    public android.net.wifi.nan.WifiNanSubscribeSession subscribeRaw(android.net.wifi.nan.SubscribeData subscribeData, android.net.wifi.nan.SubscribeSettings subscribeSettings, android.net.wifi.nan.WifiNanSessionListener listener, int events) {
        if (android.net.wifi.nan.WifiNanManager.VDBG) {
            android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, (("subscribe(): data='" + subscribeData) + "', settings=") + subscribeSettings);
        }
        if ((subscribeSettings.mSubscribeType == android.net.wifi.nan.SubscribeSettings.SUBSCRIBE_TYPE_ACTIVE) && (subscribeData.mRxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid subscribe data & settings: ACTIVE subscribes can't have an Rx filter");
        }
        if ((subscribeSettings.mSubscribeType == android.net.wifi.nan.SubscribeSettings.SUBSCRIBE_TYPE_PASSIVE) && (subscribeData.mTxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid subscribe data & settings: PASSIVE subscribes can't have a Tx filter");
        }
        int sessionId;
        try {
            sessionId = mService.createSession(listener.callback, events);
            if (android.net.wifi.nan.WifiNanManager.DBG)
                android.util.Log.d(android.net.wifi.nan.WifiNanManager.TAG, "subscribe: session created - sessionId=" + sessionId);

            mService.subscribe(sessionId, subscribeData, subscribeSettings);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        return new android.net.wifi.nan.WifiNanSubscribeSession(this, sessionId);
    }

    /**
     * {@hide }
     */
    public void subscribe(int sessionId, android.net.wifi.nan.SubscribeData subscribeData, android.net.wifi.nan.SubscribeSettings subscribeSettings) {
        if (android.net.wifi.nan.WifiNanManager.VDBG) {
            android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, (("subscribe(): data='" + subscribeData) + "', settings=") + subscribeSettings);
        }
        if ((subscribeSettings.mSubscribeType == android.net.wifi.nan.SubscribeSettings.SUBSCRIBE_TYPE_ACTIVE) && (subscribeData.mRxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid subscribe data & settings: ACTIVE subscribes can't have an Rx filter");
        }
        if ((subscribeSettings.mSubscribeType == android.net.wifi.nan.SubscribeSettings.SUBSCRIBE_TYPE_PASSIVE) && (subscribeData.mTxFilterLength != 0)) {
            throw new java.lang.IllegalArgumentException("Invalid subscribe data & settings: PASSIVE subscribes can't have a Tx filter");
        }
        try {
            mService.subscribe(sessionId, subscribeData, subscribeSettings);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void stopSession(int sessionId) {
        if (android.net.wifi.nan.WifiNanManager.DBG)
            android.util.Log.d(android.net.wifi.nan.WifiNanManager.TAG, "Stop NAN session #" + sessionId);

        try {
            mService.stopSession(sessionId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void destroySession(int sessionId) {
        if (android.net.wifi.nan.WifiNanManager.DBG)
            android.util.Log.d(android.net.wifi.nan.WifiNanManager.TAG, "Destroy NAN session #" + sessionId);

        try {
            mService.destroySession(sessionId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void sendMessage(int sessionId, int peerId, byte[] message, int messageLength, int messageId) {
        try {
            if (android.net.wifi.nan.WifiNanManager.VDBG) {
                android.util.Log.v(android.net.wifi.nan.WifiNanManager.TAG, (((((("sendMessage(): sessionId=" + sessionId) + ", peerId=") + peerId) + ", messageLength=") + messageLength) + ", messageId=") + messageId);
            }
            mService.sendMessage(sessionId, peerId, message, messageLength, messageId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}

