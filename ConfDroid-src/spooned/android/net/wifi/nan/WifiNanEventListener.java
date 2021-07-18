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
 * Base class for NAN events callbacks. Should be extended by applications
 * wanting notifications. These are callbacks applying to the NAN connection as
 * a whole - not to specific publish or subscribe sessions - for that see
 * {@link WifiNanSessionListener}.
 * <p>
 * During registration specify which specific events are desired using a set of
 * {@code NanEventListener.LISTEN_*} flags OR'd together. Only those events will
 * be delivered to the registered listener. Override those callbacks
 * {@code NanEventListener.on*} for the registered events.
 *
 * @unknown PROPOSED_NAN_API
 */
public class WifiNanEventListener {
    private static final java.lang.String TAG = "WifiNanEventListener";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;// STOPSHIP if true


    /**
     * Configuration completion callback event registration flag. Corresponding
     * callback is {@link WifiNanEventListener#onConfigCompleted(ConfigRequest)}.
     */
    public static final int LISTEN_CONFIG_COMPLETED = 0x1 << 0;

    /**
     * Configuration failed callback event registration flag. Corresponding
     * callback is
     * {@link WifiNanEventListener#onConfigFailed(ConfigRequest, int)}.
     */
    public static final int LISTEN_CONFIG_FAILED = 0x1 << 1;

    /**
     * NAN cluster is down callback event registration flag. Corresponding
     * callback is {@link WifiNanEventListener#onNanDown(int)}.
     */
    public static final int LISTEN_NAN_DOWN = 0x1 << 2;

    /**
     * NAN identity has changed event registration flag. This may be due to
     * joining a cluster, starting a cluster, or discovery interface change. The
     * implication is that peers you've been communicating with may no longer
     * recognize you and you need to re-establish your identity. Corresponding
     * callback is {@link WifiNanEventListener#onIdentityChanged()}.
     */
    public static final int LISTEN_IDENTITY_CHANGED = 0x1 << 3;

    private final android.os.Handler mHandler;

    /**
     * Constructs a {@link WifiNanEventListener} using the looper of the current
     * thread. I.e. all callbacks will be delivered on the current thread.
     */
    public WifiNanEventListener() {
        this(android.os.Looper.myLooper());
    }

    /**
     * Constructs a {@link WifiNanEventListener} using the specified looper. I.e.
     * all callbacks will delivered on the thread of the specified looper.
     *
     * @param looper
     * 		The looper on which to execute the callbacks.
     */
    public WifiNanEventListener(android.os.Looper looper) {
        if (android.net.wifi.nan.WifiNanEventListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanEventListener.TAG, "ctor: looper=" + looper);

        mHandler = new android.os.Handler(looper) {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (android.net.wifi.nan.WifiNanEventListener.DBG)
                    android.util.Log.d(android.net.wifi.nan.WifiNanEventListener.TAG, (("What=" + msg.what) + ", msg=") + msg);

                switch (msg.what) {
                    case android.net.wifi.nan.WifiNanEventListener.LISTEN_CONFIG_COMPLETED :
                        android.net.wifi.nan.WifiNanEventListener.this.onConfigCompleted(((android.net.wifi.nan.ConfigRequest) (msg.obj)));
                        break;
                    case android.net.wifi.nan.WifiNanEventListener.LISTEN_CONFIG_FAILED :
                        android.net.wifi.nan.WifiNanEventListener.this.onConfigFailed(((android.net.wifi.nan.ConfigRequest) (msg.obj)), msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanEventListener.LISTEN_NAN_DOWN :
                        android.net.wifi.nan.WifiNanEventListener.this.onNanDown(msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanEventListener.LISTEN_IDENTITY_CHANGED :
                        android.net.wifi.nan.WifiNanEventListener.this.onIdentityChanged();
                        break;
                }
            }
        };
    }

    /**
     * Called when NAN configuration is completed. Event will only be delivered
     * if registered using {@link WifiNanEventListener#LISTEN_CONFIG_COMPLETED}. A
     * dummy (empty implementation printing out a warning). Make sure to
     * override if registered.
     *
     * @param completedConfig
     * 		The actual configuration request which was
     * 		completed. Note that it may be different from that requested
     * 		by the application. The service combines configuration
     * 		requests from all applications.
     */
    public void onConfigCompleted(android.net.wifi.nan.ConfigRequest completedConfig) {
        android.util.Log.w(android.net.wifi.nan.WifiNanEventListener.TAG, "onConfigCompleted: called in stub - override if interested or disable");
    }

    /**
     * Called when NAN configuration failed. Event will only be delivered if
     * registered using {@link WifiNanEventListener#LISTEN_CONFIG_FAILED}. A dummy
     * (empty implementation printing out a warning). Make sure to override if
     * registered.
     *
     * @param reason
     * 		Failure reason code, see {@code NanSessionListener.FAIL_*}.
     */
    public void onConfigFailed(android.net.wifi.nan.ConfigRequest failedConfig, int reason) {
        android.util.Log.w(android.net.wifi.nan.WifiNanEventListener.TAG, "onConfigFailed: called in stub - override if interested or disable");
    }

    /**
     * Called when NAN cluster is down. Event will only be delivered if
     * registered using {@link WifiNanEventListener#LISTEN_NAN_DOWN}. A dummy (empty
     * implementation printing out a warning). Make sure to override if
     * registered.
     *
     * @param reason
     * 		Reason code for event, see {@code NanSessionListener.FAIL_*}.
     */
    public void onNanDown(int reason) {
        android.util.Log.w(android.net.wifi.nan.WifiNanEventListener.TAG, "onNanDown: called in stub - override if interested or disable");
    }

    /**
     * Called when NAN identity has changed. This may be due to joining a
     * cluster, starting a cluster, or discovery interface change. The
     * implication is that peers you've been communicating with may no longer
     * recognize you and you need to re-establish your identity. Event will only
     * be delivered if registered using
     * {@link WifiNanEventListener#LISTEN_IDENTITY_CHANGED}. A dummy (empty
     * implementation printing out a warning). Make sure to override if
     * registered.
     */
    public void onIdentityChanged() {
        if (android.net.wifi.nan.WifiNanEventListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanEventListener.TAG, "onIdentityChanged: called in stub - override if interested");

    }

    /**
     * {@hide }
     */
    public android.net.wifi.nan.IWifiNanEventListener callback = new android.net.wifi.nan.IWifiNanEventListener.Stub() {
        @java.lang.Override
        public void onConfigCompleted(android.net.wifi.nan.ConfigRequest completedConfig) {
            if (android.net.wifi.nan.WifiNanEventListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanEventListener.TAG, "onConfigCompleted: configRequest=" + completedConfig);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanEventListener.LISTEN_CONFIG_COMPLETED);
            msg.obj = completedConfig;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onConfigFailed(android.net.wifi.nan.ConfigRequest failedConfig, int reason) {
            if (android.net.wifi.nan.WifiNanEventListener.VDBG) {
                android.util.Log.v(android.net.wifi.nan.WifiNanEventListener.TAG, (("onConfigFailed: failedConfig=" + failedConfig) + ", reason=") + reason);
            }
            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanEventListener.LISTEN_CONFIG_FAILED);
            msg.arg1 = reason;
            msg.obj = failedConfig;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onNanDown(int reason) {
            if (android.net.wifi.nan.WifiNanEventListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanEventListener.TAG, "onNanDown: reason=" + reason);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanEventListener.LISTEN_NAN_DOWN);
            msg.arg1 = reason;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onIdentityChanged() {
            if (android.net.wifi.nan.WifiNanEventListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanEventListener.TAG, "onIdentityChanged");

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanEventListener.LISTEN_IDENTITY_CHANGED);
            mHandler.sendMessage(msg);
        }
    };
}

