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
 * Base class for NAN session events callbacks. Should be extended by
 * applications wanting notifications. The callbacks are registered when a
 * publish or subscribe session is created using
 * {@link WifiNanManager#publish(PublishData, PublishSettings, WifiNanSessionListener, int)}
 * or
 * {@link WifiNanManager#subscribe(SubscribeData, SubscribeSettings, WifiNanSessionListener, int)}
 * . These are callbacks applying to a specific NAN session. Events
 * corresponding to the NAN link are delivered using {@link WifiNanEventListener}.
 * <p>
 * A single listener is registered at session creation - it cannot be replaced.
 * <p>
 * During registration specify which specific events are desired using a set of
 * {@code NanSessionListener.LISTEN_*} flags OR'd together. Only those events
 * will be delivered to the registered listener. Override those callbacks
 * {@code NanSessionListener.on*} for the registered events.
 *
 * @unknown PROPOSED_NAN_API
 */
public class WifiNanSessionListener {
    private static final java.lang.String TAG = "WifiNanSessionListener";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;// STOPSHIP if true


    /**
     * Publish fail callback event registration flag. Corresponding callback is
     * {@link WifiNanSessionListener#onPublishFail(int)}.
     *
     * @unknown 
     */
    public static final int LISTEN_PUBLISH_FAIL = 0x1 << 0;

    /**
     * Publish terminated callback event registration flag. Corresponding
     * callback is {@link WifiNanSessionListener#onPublishTerminated(int)}.
     */
    public static final int LISTEN_PUBLISH_TERMINATED = 0x1 << 1;

    /**
     * Subscribe fail callback event registration flag. Corresponding callback
     * is {@link WifiNanSessionListener#onSubscribeFail(int)}.
     *
     * @unknown 
     */
    public static final int LISTEN_SUBSCRIBE_FAIL = 0x1 << 2;

    /**
     * Subscribe terminated callback event registration flag. Corresponding
     * callback is {@link WifiNanSessionListener#onSubscribeTerminated(int)}.
     */
    public static final int LISTEN_SUBSCRIBE_TERMINATED = 0x1 << 3;

    /**
     * Match (discovery: publish or subscribe) callback event registration flag.
     * Corresponding callback is
     * {@link WifiNanSessionListener#onMatch(int, byte[], int, byte[], int)}.
     *
     * @unknown 
     */
    public static final int LISTEN_MATCH = 0x1 << 4;

    /**
     * Message sent successfully callback event registration flag. Corresponding
     * callback is {@link WifiNanSessionListener#onMessageSendSuccess()}.
     *
     * @unknown 
     */
    public static final int LISTEN_MESSAGE_SEND_SUCCESS = 0x1 << 5;

    /**
     * Message sending failure callback event registration flag. Corresponding
     * callback is {@link WifiNanSessionListener#onMessageSendFail(int)}.
     *
     * @unknown 
     */
    public static final int LISTEN_MESSAGE_SEND_FAIL = 0x1 << 6;

    /**
     * Message received callback event registration flag. Corresponding callback
     * is {@link WifiNanSessionListener#onMessageReceived(int, byte[], int)}.
     *
     * @unknown 
     */
    public static final int LISTEN_MESSAGE_RECEIVED = 0x1 << 7;

    /**
     * List of hidden events: which are mandatory - i.e. they will be added to
     * every request.
     *
     * @unknown 
     */
    public static final int LISTEN_HIDDEN_FLAGS = ((((android.net.wifi.nan.WifiNanSessionListener.LISTEN_PUBLISH_FAIL | android.net.wifi.nan.WifiNanSessionListener.LISTEN_SUBSCRIBE_FAIL) | android.net.wifi.nan.WifiNanSessionListener.LISTEN_MATCH) | android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_SEND_SUCCESS) | android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_SEND_FAIL) | android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_RECEIVED;

    /**
     * Failure reason flag for {@link WifiNanEventListener} and
     * {@link WifiNanSessionListener} callbacks. Indicates no resources to execute
     * the requested operation.
     */
    public static final int FAIL_REASON_NO_RESOURCES = 0;

    /**
     * Failure reason flag for {@link WifiNanEventListener} and
     * {@link WifiNanSessionListener} callbacks. Indicates invalid argument in the
     * requested operation.
     */
    public static final int FAIL_REASON_INVALID_ARGS = 1;

    /**
     * Failure reason flag for {@link WifiNanEventListener} and
     * {@link WifiNanSessionListener} callbacks. Indicates a message is transmitted
     * without a match (i.e. a discovery) occurring first.
     */
    public static final int FAIL_REASON_NO_MATCH_SESSION = 2;

    /**
     * Failure reason flag for {@link WifiNanEventListener} and
     * {@link WifiNanSessionListener} callbacks. Indicates an unspecified error
     * occurred during the operation.
     */
    public static final int FAIL_REASON_OTHER = 3;

    /**
     * Failure reason flag for
     * {@link WifiNanSessionListener#onPublishTerminated(int)} and
     * {@link WifiNanSessionListener#onSubscribeTerminated(int)} callbacks.
     * Indicates that publish or subscribe session is done - i.e. all the
     * requested operations (per {@link PublishSettings} or
     * {@link SubscribeSettings}) have been executed.
     */
    public static final int TERMINATE_REASON_DONE = 0;

    /**
     * Failure reason flag for
     * {@link WifiNanSessionListener#onPublishTerminated(int)} and
     * {@link WifiNanSessionListener#onSubscribeTerminated(int)} callbacks.
     * Indicates that publish or subscribe session is terminated due to a
     * failure.
     */
    public static final int TERMINATE_REASON_FAIL = 1;

    private static final java.lang.String MESSAGE_BUNDLE_KEY_PEER_ID = "peer_id";

    private static final java.lang.String MESSAGE_BUNDLE_KEY_MESSAGE = "message";

    private static final java.lang.String MESSAGE_BUNDLE_KEY_MESSAGE2 = "message2";

    private final android.os.Handler mHandler;

    /**
     * Constructs a {@link WifiNanSessionListener} using the looper of the current
     * thread. I.e. all callbacks will be delivered on the current thread.
     */
    public WifiNanSessionListener() {
        this(android.os.Looper.myLooper());
    }

    /**
     * Constructs a {@link WifiNanSessionListener} using the specified looper. I.e.
     * all callbacks will delivered on the thread of the specified looper.
     *
     * @param looper
     * 		The looper on which to execute the callbacks.
     */
    public WifiNanSessionListener(android.os.Looper looper) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "ctor: looper=" + looper);

        mHandler = new android.os.Handler(looper) {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (android.net.wifi.nan.WifiNanSessionListener.DBG)
                    android.util.Log.d(android.net.wifi.nan.WifiNanSessionListener.TAG, (("What=" + msg.what) + ", msg=") + msg);

                switch (msg.what) {
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_PUBLISH_FAIL :
                        android.net.wifi.nan.WifiNanSessionListener.this.onPublishFail(msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_PUBLISH_TERMINATED :
                        android.net.wifi.nan.WifiNanSessionListener.this.onPublishTerminated(msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_SUBSCRIBE_FAIL :
                        android.net.wifi.nan.WifiNanSessionListener.this.onSubscribeFail(msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_SUBSCRIBE_TERMINATED :
                        android.net.wifi.nan.WifiNanSessionListener.this.onSubscribeTerminated(msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_MATCH :
                        android.net.wifi.nan.WifiNanSessionListener.this.onMatch(msg.getData().getInt(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_PEER_ID), msg.getData().getByteArray(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_MESSAGE), msg.arg1, msg.getData().getByteArray(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_MESSAGE2), msg.arg2);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_SEND_SUCCESS :
                        android.net.wifi.nan.WifiNanSessionListener.this.onMessageSendSuccess(msg.arg1);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_SEND_FAIL :
                        android.net.wifi.nan.WifiNanSessionListener.this.onMessageSendFail(msg.arg1, msg.arg2);
                        break;
                    case android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_RECEIVED :
                        android.net.wifi.nan.WifiNanSessionListener.this.onMessageReceived(msg.arg2, msg.getData().getByteArray(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_MESSAGE), msg.arg1);
                        break;
                }
            }
        };
    }

    /**
     * Called when a publish operation fails. It is dummy method (empty
     * implementation printing out a log message). Override to implement your
     * custom response.
     *
     * @param reason
     * 		The failure reason using {@code NanSessionListener.FAIL_*}
     * 		codes.
     */
    public void onPublishFail(int reason) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onPublishFail: called in stub - override if interested");

    }

    /**
     * Called when a publish operation terminates. Event will only be delivered
     * if registered using {@link WifiNanSessionListener#LISTEN_PUBLISH_TERMINATED}.
     * A dummy (empty implementation printing out a warning). Make sure to
     * override if registered.
     *
     * @param reason
     * 		The termination reason using
     * 		{@code NanSessionListener.TERMINATE_*} codes.
     */
    public void onPublishTerminated(int reason) {
        android.util.Log.w(android.net.wifi.nan.WifiNanSessionListener.TAG, "onPublishTerminated: called in stub - override if interested or disable");
    }

    /**
     * Called when a subscribe operation fails. It is dummy method (empty
     * implementation printing out a log message). Override to implement your
     * custom response.
     *
     * @param reason
     * 		The failure reason using {@code NanSessionListener.FAIL_*}
     * 		codes.
     */
    public void onSubscribeFail(int reason) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onSubscribeFail: called in stub - override if interested");

    }

    /**
     * Called when a subscribe operation terminates. Event will only be
     * delivered if registered using
     * {@link WifiNanSessionListener#LISTEN_SUBSCRIBE_TERMINATED}. A dummy (empty
     * implementation printing out a warning). Make sure to override if
     * registered.
     *
     * @param reason
     * 		The termination reason using
     * 		{@code NanSessionListener.TERMINATE_*} codes.
     */
    public void onSubscribeTerminated(int reason) {
        android.util.Log.w(android.net.wifi.nan.WifiNanSessionListener.TAG, "onSubscribeTerminated: called in stub - override if interested or disable");
    }

    /**
     * Called when a discovery (publish or subscribe) operation results in a
     * match - i.e. when a peer is discovered. It is dummy method (empty
     * implementation printing out a log message). Override to implement your
     * custom response.
     *
     * @param peerId
     * 		The ID of the peer matching our discovery operation.
     * @param serviceSpecificInfo
     * 		The service specific information (arbitrary
     * 		byte array) provided by the peer as part of its discovery
     * 		packet.
     * @param serviceSpecificInfoLength
     * 		The length of the service specific
     * 		information array.
     * @param matchFilter
     * 		The filter (Tx on advertiser and Rx on listener) which
     * 		resulted in this match.
     * @param matchFilterLength
     * 		The length of the match filter array.
     */
    public void onMatch(int peerId, byte[] serviceSpecificInfo, int serviceSpecificInfoLength, byte[] matchFilter, int matchFilterLength) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMatch: called in stub - override if interested");

    }

    /**
     * Called when a message is transmitted successfully - i.e. when we know
     * that it was received successfully (corresponding to an ACK being
     * received). It is dummy method (empty implementation printing out a log
     * message). Override to implement your custom response.
     * <p>
     * Note that either this callback or
     * {@link WifiNanSessionListener#onMessageSendFail(int, int)} will be
     * received - never both.
     */
    public void onMessageSendSuccess(int messageId) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMessageSendSuccess: called in stub - override if interested");

    }

    /**
     * Called when a message transmission fails - i.e. when no ACK is received.
     * The hardware will usually attempt to re-transmit several times - this
     * event is received after all retries are exhausted. There is a possibility
     * that message was received by the destination successfully but the ACK was
     * lost. It is dummy method (empty implementation printing out a log
     * message). Override to implement your custom response.
     * <p>
     * Note that either this callback or
     * {@link WifiNanSessionListener#onMessageSendSuccess(int)} will be received
     * - never both
     *
     * @param reason
     * 		The failure reason using {@code NanSessionListener.FAIL_*}
     * 		codes.
     */
    public void onMessageSendFail(int messageId, int reason) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMessageSendFail: called in stub - override if interested");

    }

    /**
     * Called when a message is received from a discovery session peer. It is
     * dummy method (empty implementation printing out a log message). Override
     * to implement your custom response.
     *
     * @param peerId
     * 		The ID of the peer sending the message.
     * @param message
     * 		A byte array containing the message.
     * @param messageLength
     * 		The length of the byte array containing the relevant
     * 		message bytes.
     */
    public void onMessageReceived(int peerId, byte[] message, int messageLength) {
        if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
            android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMessageReceived: called in stub - override if interested");

    }

    /**
     * {@hide }
     */
    public android.net.wifi.nan.IWifiNanSessionListener callback = new android.net.wifi.nan.IWifiNanSessionListener.Stub() {
        @java.lang.Override
        public void onPublishFail(int reason) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onPublishFail: reason=" + reason);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_PUBLISH_FAIL);
            msg.arg1 = reason;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onPublishTerminated(int reason) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onPublishResponse: reason=" + reason);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_PUBLISH_TERMINATED);
            msg.arg1 = reason;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onSubscribeFail(int reason) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onSubscribeFail: reason=" + reason);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_SUBSCRIBE_FAIL);
            msg.arg1 = reason;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onSubscribeTerminated(int reason) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onSubscribeTerminated: reason=" + reason);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_SUBSCRIBE_TERMINATED);
            msg.arg1 = reason;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onMatch(int peerId, byte[] serviceSpecificInfo, int serviceSpecificInfoLength, byte[] matchFilter, int matchFilterLength) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMatch: peerId=" + peerId);

            android.os.Bundle data = new android.os.Bundle();
            data.putInt(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_PEER_ID, peerId);
            data.putByteArray(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_MESSAGE, serviceSpecificInfo);
            data.putByteArray(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_MESSAGE2, matchFilter);
            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_MATCH);
            msg.arg1 = serviceSpecificInfoLength;
            msg.arg2 = matchFilterLength;
            msg.setData(data);
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onMessageSendSuccess(int messageId) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMessageSendSuccess");

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_SEND_SUCCESS);
            msg.arg1 = messageId;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onMessageSendFail(int messageId, int reason) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG)
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, "onMessageSendFail: reason=" + reason);

            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_SEND_FAIL);
            msg.arg1 = messageId;
            msg.arg2 = reason;
            mHandler.sendMessage(msg);
        }

        @java.lang.Override
        public void onMessageReceived(int peerId, byte[] message, int messageLength) {
            if (android.net.wifi.nan.WifiNanSessionListener.VDBG) {
                android.util.Log.v(android.net.wifi.nan.WifiNanSessionListener.TAG, (("onMessageReceived: peerId='" + peerId) + "', messageLength=") + messageLength);
            }
            android.os.Bundle data = new android.os.Bundle();
            data.putByteArray(android.net.wifi.nan.WifiNanSessionListener.MESSAGE_BUNDLE_KEY_MESSAGE, message);
            android.os.Message msg = mHandler.obtainMessage(android.net.wifi.nan.WifiNanSessionListener.LISTEN_MESSAGE_RECEIVED);
            msg.arg1 = messageLength;
            msg.arg2 = peerId;
            msg.setData(data);
            mHandler.sendMessage(msg);
        }
    };
}

