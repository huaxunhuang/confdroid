/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.service.carrier;


/**
 * A service that receives calls from the system when new SMS and MMS are
 * sent or received.
 * <p>To extend this class, you must declare the service in your manifest file with
 * the {@link android.Manifest.permission#BIND_CARRIER_SERVICES} permission
 * and include an intent filter with the {@link #SERVICE_INTERFACE} action. For example:</p>
 * <pre>
 * &lt;service android:name=".MyMessagingService"
 *          android:label="&#64;string/service_name"
 *          android:permission="android.permission.BIND_CARRIER_SERVICES">
 *     &lt;intent-filter>
 *         &lt;action android:name="android.service.carrier.CarrierMessagingService" />
 *     &lt;/intent-filter>
 * &lt;/service></pre>
 */
public abstract class CarrierMessagingService extends android.app.Service {
    /**
     * The {@link android.content.Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.service.carrier.CarrierMessagingService";

    /**
     * The default bitmask value passed to the callback of {@link #onReceiveTextSms} with all
     * {@code RECEIVE_OPTIONS_x} flags cleared to indicate that the message should be kept and a
     * new message notification should be shown.
     *
     * @see #RECEIVE_OPTIONS_DROP
     * @see #RECEIVE_OPTIONS_SKIP_NOTIFY_WHEN_CREDENTIAL_PROTECTED_STORAGE_UNAVAILABLE
     */
    public static final int RECEIVE_OPTIONS_DEFAULT = 0;

    /**
     * Used to set the flag in the bitmask passed to the callback of {@link #onReceiveTextSms} to
     * indicate that the inbound SMS should be dropped.
     */
    public static final int RECEIVE_OPTIONS_DROP = 0x1;

    /**
     * Used to set the flag in the bitmask passed to the callback of {@link #onReceiveTextSms} to
     * indicate that a new message notification should not be shown to the user when the
     * credential-encrypted storage of the device is not available before the user unlocks the
     * phone. It is only applicable to devices that support file-based encryption.
     */
    public static final int RECEIVE_OPTIONS_SKIP_NOTIFY_WHEN_CREDENTIAL_PROTECTED_STORAGE_UNAVAILABLE = 0x2;

    /**
     * Indicates that an SMS or MMS message was successfully sent.
     */
    public static final int SEND_STATUS_OK = 0;

    /**
     * SMS/MMS sending failed. We should retry via the carrier network.
     */
    public static final int SEND_STATUS_RETRY_ON_CARRIER_NETWORK = 1;

    /**
     * SMS/MMS sending failed. We should not retry via the carrier network.
     */
    public static final int SEND_STATUS_ERROR = 2;

    /**
     * Successfully downloaded an MMS message.
     */
    public static final int DOWNLOAD_STATUS_OK = 0;

    /**
     * MMS downloading failed. We should retry via the carrier network.
     */
    public static final int DOWNLOAD_STATUS_RETRY_ON_CARRIER_NETWORK = 1;

    /**
     * MMS downloading failed. We should not retry via the carrier network.
     */
    public static final int DOWNLOAD_STATUS_ERROR = 2;

    /**
     * Flag to request SMS delivery status report.
     */
    public static final int SEND_FLAG_REQUEST_DELIVERY_STATUS = 1;

    private final android.service.carrier.CarrierMessagingService.ICarrierMessagingWrapper mWrapper = new android.service.carrier.CarrierMessagingService.ICarrierMessagingWrapper();

    /**
     * Override this method to filter inbound SMS messages.
     *
     * @param pdu
     * 		the PDUs of the message
     * @param format
     * 		the format of the PDUs, typically "3gpp" or "3gpp2"
     * @param destPort
     * 		the destination port of a binary SMS, this will be -1 for text SMS
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param callback
     * 		result callback. Call with {@code true} to keep an inbound SMS message and
     * 		deliver to SMS apps, and {@code false} to drop the message.
     * @deprecated Use {@link #onReceiveTextSms} instead.
     */
    @java.lang.Deprecated
    public void onFilterSms(@android.annotation.NonNull
    android.service.carrier.MessagePdu pdu, @android.annotation.NonNull
    java.lang.String format, int destPort, int subId, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<java.lang.Boolean> callback) {
        // optional
        try {
            callback.onReceiveResult(true);
        } catch (android.os.RemoteException ex) {
        }
    }

    /**
     * Override this method to filter inbound SMS messages.
     *
     * <p>This method will be called once for every incoming text SMS. You can invoke the callback
     * with a bitmask to tell the platform how to handle the SMS. For a SMS received on a
     * file-based encryption capable device while the credential-encrypted storage is not available,
     * this method will be called for the second time when the credential-encrypted storage becomes
     * available after the user unlocks the phone, if the bit {@link #RECEIVE_OPTIONS_DROP} is not
     * set when invoking the callback.
     *
     * @param pdu
     * 		the PDUs of the message
     * @param format
     * 		the format of the PDUs, typically "3gpp" or "3gpp2"
     * @param destPort
     * 		the destination port of a binary SMS, this will be -1 for text SMS
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param callback
     * 		result callback. Call with a bitmask integer to indicate how the incoming
     * 		text SMS should be handled by the platform. Use {@link #RECEIVE_OPTIONS_DROP} and
     * 		{@link #RECEIVE_OPTIONS_SKIP_NOTIFY_WHEN_CREDENTIAL_PROTECTED_STORAGE_UNAVAILABLE}
     * 		to set the flags in the bitmask.
     */
    public void onReceiveTextSms(@android.annotation.NonNull
    android.service.carrier.MessagePdu pdu, @android.annotation.NonNull
    java.lang.String format, int destPort, int subId, @android.annotation.NonNull
    final android.service.carrier.CarrierMessagingService.ResultCallback<java.lang.Integer> callback) {
        onFilterSms(pdu, format, destPort, subId, new android.service.carrier.CarrierMessagingService.ResultCallback<java.lang.Boolean>() {
            @java.lang.Override
            public void onReceiveResult(java.lang.Boolean result) throws android.os.RemoteException {
                callback.onReceiveResult(result ? android.service.carrier.CarrierMessagingService.RECEIVE_OPTIONS_DEFAULT : android.service.carrier.CarrierMessagingService.RECEIVE_OPTIONS_DROP | android.service.carrier.CarrierMessagingService.RECEIVE_OPTIONS_SKIP_NOTIFY_WHEN_CREDENTIAL_PROTECTED_STORAGE_UNAVAILABLE);
            }
        });
    }

    /**
     * Override this method to intercept text SMSs sent from the device.
     *
     * @deprecated Override {@link #onSendTextSms} below instead.
     * @param text
     * 		the text to send
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param destAddress
     * 		phone number of the recipient of the message
     * @param callback
     * 		result callback. Call with a {@link SendSmsResult}.
     */
    @java.lang.Deprecated
    public void onSendTextSms(@android.annotation.NonNull
    java.lang.String text, int subId, @android.annotation.NonNull
    java.lang.String destAddress, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendSmsResult> callback) {
        // optional
        try {
            callback.onReceiveResult(new android.service.carrier.CarrierMessagingService.SendSmsResult(android.service.carrier.CarrierMessagingService.SEND_STATUS_RETRY_ON_CARRIER_NETWORK, 0));
        } catch (android.os.RemoteException ex) {
        }
    }

    /**
     * Override this method to intercept text SMSs sent from the device.
     *
     * @param text
     * 		the text to send
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param destAddress
     * 		phone number of the recipient of the message
     * @param sendSmsFlag
     * 		Flag for sending SMS. Acceptable values are 0 and
     * 		{@link #SEND_FLAG_REQUEST_DELIVERY_STATUS}.
     * @param callback
     * 		result callback. Call with a {@link SendSmsResult}.
     */
    public void onSendTextSms(@android.annotation.NonNull
    java.lang.String text, int subId, @android.annotation.NonNull
    java.lang.String destAddress, int sendSmsFlag, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendSmsResult> callback) {
        // optional
        onSendTextSms(text, subId, destAddress, callback);
    }

    /**
     * Override this method to intercept binary SMSs sent from the device.
     *
     * @deprecated Override {@link #onSendDataSms} below instead.
     * @param data
     * 		the binary content
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param destAddress
     * 		phone number of the recipient of the message
     * @param destPort
     * 		the destination port
     * @param callback
     * 		result callback. Call with a {@link SendSmsResult}.
     */
    @java.lang.Deprecated
    public void onSendDataSms(@android.annotation.NonNull
    byte[] data, int subId, @android.annotation.NonNull
    java.lang.String destAddress, int destPort, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendSmsResult> callback) {
        // optional
        try {
            callback.onReceiveResult(new android.service.carrier.CarrierMessagingService.SendSmsResult(android.service.carrier.CarrierMessagingService.SEND_STATUS_RETRY_ON_CARRIER_NETWORK, 0));
        } catch (android.os.RemoteException ex) {
        }
    }

    /**
     * Override this method to intercept binary SMSs sent from the device.
     *
     * @param data
     * 		the binary content
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param destAddress
     * 		phone number of the recipient of the message
     * @param destPort
     * 		the destination port
     * @param sendSmsFlag
     * 		Flag for sending SMS. Acceptable values are 0 and
     * 		{@link #SEND_FLAG_REQUEST_DELIVERY_STATUS}.
     * @param callback
     * 		result callback. Call with a {@link SendSmsResult}.
     */
    public void onSendDataSms(@android.annotation.NonNull
    byte[] data, int subId, @android.annotation.NonNull
    java.lang.String destAddress, int destPort, int sendSmsFlag, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendSmsResult> callback) {
        // optional
        onSendDataSms(data, subId, destAddress, destPort, callback);
    }

    /**
     * Override this method to intercept long SMSs sent from the device.
     *
     * @deprecated Override {@link #onSendMultipartTextSms} below instead.
     * @param parts
     * 		a {@link List} of the message parts
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param destAddress
     * 		phone number of the recipient of the message
     * @param callback
     * 		result callback. Call with a {@link SendMultipartSmsResult}.
     */
    @java.lang.Deprecated
    public void onSendMultipartTextSms(@android.annotation.NonNull
    java.util.List<java.lang.String> parts, int subId, @android.annotation.NonNull
    java.lang.String destAddress, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendMultipartSmsResult> callback) {
        // optional
        try {
            callback.onReceiveResult(new android.service.carrier.CarrierMessagingService.SendMultipartSmsResult(android.service.carrier.CarrierMessagingService.SEND_STATUS_RETRY_ON_CARRIER_NETWORK, null));
        } catch (android.os.RemoteException ex) {
        }
    }

    /**
     * Override this method to intercept long SMSs sent from the device.
     *
     * @param parts
     * 		a {@link List} of the message parts
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param destAddress
     * 		phone number of the recipient of the message
     * @param sendSmsFlag
     * 		Flag for sending SMS. Acceptable values are 0 and
     * 		{@link #SEND_FLAG_REQUEST_DELIVERY_STATUS}.
     * @param callback
     * 		result callback. Call with a {@link SendMultipartSmsResult}.
     */
    public void onSendMultipartTextSms(@android.annotation.NonNull
    java.util.List<java.lang.String> parts, int subId, @android.annotation.NonNull
    java.lang.String destAddress, int sendSmsFlag, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendMultipartSmsResult> callback) {
        // optional
        onSendMultipartTextSms(parts, subId, destAddress, callback);
    }

    /**
     * Override this method to intercept MMSs sent from the device.
     *
     * @param pduUri
     * 		the content provider URI of the PDU to send
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param location
     * 		the optional URI to send this MMS PDU. If this is {code null},
     * 		the PDU should be sent to the default MMSC URL.
     * @param callback
     * 		result callback. Call with a {@link SendMmsResult}.
     */
    public void onSendMms(@android.annotation.NonNull
    android.net.Uri pduUri, int subId, @android.annotation.Nullable
    android.net.Uri location, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendMmsResult> callback) {
        // optional
        try {
            callback.onReceiveResult(new android.service.carrier.CarrierMessagingService.SendMmsResult(android.service.carrier.CarrierMessagingService.SEND_STATUS_RETRY_ON_CARRIER_NETWORK, null));
        } catch (android.os.RemoteException ex) {
        }
    }

    /**
     * Override this method to download MMSs received.
     *
     * @param contentUri
     * 		the content provider URI of the PDU to be downloaded.
     * @param subId
     * 		SMS subscription ID of the SIM
     * @param location
     * 		the URI of the message to be downloaded.
     * @param callback
     * 		result callback. Call with a status code which is one of
     * 		{@link #DOWNLOAD_STATUS_OK},
     * 		{@link #DOWNLOAD_STATUS_RETRY_ON_CARRIER_NETWORK}, or {@link #DOWNLOAD_STATUS_ERROR}.
     */
    public void onDownloadMms(@android.annotation.NonNull
    android.net.Uri contentUri, int subId, @android.annotation.NonNull
    android.net.Uri location, @android.annotation.NonNull
    android.service.carrier.CarrierMessagingService.ResultCallback<java.lang.Integer> callback) {
        // optional
        try {
            callback.onReceiveResult(android.service.carrier.CarrierMessagingService.DOWNLOAD_STATUS_RETRY_ON_CARRIER_NETWORK);
        } catch (android.os.RemoteException ex) {
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.os.IBinder onBind(@android.annotation.NonNull
    android.content.Intent intent) {
        if (!android.service.carrier.CarrierMessagingService.SERVICE_INTERFACE.equals(intent.getAction())) {
            return null;
        }
        return mWrapper;
    }

    /**
     * The result of sending an MMS.
     */
    public static final class SendMmsResult {
        private int mSendStatus;

        private byte[] mSendConfPdu;

        /**
         * Constructs a SendMmsResult with the MMS send result, and the SendConf PDU.
         *
         * @param sendStatus
         * 		send status, one of {@link #SEND_STATUS_OK},
         * 		{@link #SEND_STATUS_RETRY_ON_CARRIER_NETWORK}, and
         * 		{@link #SEND_STATUS_ERROR}
         * @param sendConfPdu
         * 		a possibly {code null} SendConf PDU, which confirms that the message
         * 		was sent. sendConfPdu is ignored if the {@code result} is not
         * 		{@link #SEND_STATUS_OK}.
         */
        public SendMmsResult(int sendStatus, @android.annotation.Nullable
        byte[] sendConfPdu) {
            mSendStatus = sendStatus;
            mSendConfPdu = sendConfPdu;
        }

        /**
         * Returns the send status of the just-sent MMS.
         *
         * @return the send status which is one of {@link #SEND_STATUS_OK},
        {@link #SEND_STATUS_RETRY_ON_CARRIER_NETWORK}, and {@link #SEND_STATUS_ERROR}
         */
        public int getSendStatus() {
            return mSendStatus;
        }

        /**
         * Returns the SendConf PDU, which confirms that the message was sent.
         *
         * @return the SendConf PDU
         */
        @android.annotation.Nullable
        public byte[] getSendConfPdu() {
            return mSendConfPdu;
        }
    }

    /**
     * The result of sending an SMS.
     */
    public static final class SendSmsResult {
        private final int mSendStatus;

        private final int mMessageRef;

        /**
         * Constructs a SendSmsResult with the send status and message reference for the
         * just-sent SMS.
         *
         * @param sendStatus
         * 		send status, one of {@link #SEND_STATUS_OK},
         * 		{@link #SEND_STATUS_RETRY_ON_CARRIER_NETWORK}, and {@link #SEND_STATUS_ERROR}.
         * @param messageRef
         * 		message reference of the just-sent SMS. This field is applicable only
         * 		if send status is {@link #SEND_STATUS_OK}.
         */
        public SendSmsResult(int sendStatus, int messageRef) {
            mSendStatus = sendStatus;
            mMessageRef = messageRef;
        }

        /**
         * Returns the message reference of the just-sent SMS.
         *
         * @return the message reference
         */
        public int getMessageRef() {
            return mMessageRef;
        }

        /**
         * Returns the send status of the just-sent SMS.
         *
         * @return the send status
         */
        public int getSendStatus() {
            return mSendStatus;
        }
    }

    /**
     * The result of sending a multipart SMS.
     */
    public static final class SendMultipartSmsResult {
        private final int mSendStatus;

        private final int[] mMessageRefs;

        /**
         * Constructs a SendMultipartSmsResult with the send status and message references for the
         * just-sent multipart SMS.
         *
         * @param sendStatus
         * 		send status, one of {@link #SEND_STATUS_OK},
         * 		{@link #SEND_STATUS_RETRY_ON_CARRIER_NETWORK}, and {@link #SEND_STATUS_ERROR}.
         * @param messageRefs
         * 		an array of message references, one for each part of the
         * 		multipart SMS. This field is applicable only if send status is
         * 		{@link #SEND_STATUS_OK}.
         */
        public SendMultipartSmsResult(int sendStatus, @android.annotation.Nullable
        int[] messageRefs) {
            mSendStatus = sendStatus;
            mMessageRefs = messageRefs;
        }

        /**
         * Returns the message references of the just-sent multipart SMS.
         *
         * @return the message references, one for each part of the multipart SMS
         */
        @android.annotation.Nullable
        public int[] getMessageRefs() {
            return mMessageRefs;
        }

        /**
         * Returns the send status of the just-sent SMS.
         *
         * @return the send status
         */
        public int getSendStatus() {
            return mSendStatus;
        }
    }

    /**
     * A callback interface used to provide results asynchronously.
     */
    public interface ResultCallback<T> {
        /**
         * Invoked when the result is available.
         *
         * @param result
         * 		the result
         */
        public void onReceiveResult(@android.annotation.NonNull
        T result) throws android.os.RemoteException;
    }

    /**
     * A wrapper around ICarrierMessagingService to enable the carrier messaging app to implement
     * methods it cares about in the {@link ICarrierMessagingService} interface.
     */
    private class ICarrierMessagingWrapper extends android.service.carrier.ICarrierMessagingService.Stub {
        @java.lang.Override
        public void filterSms(android.service.carrier.MessagePdu pdu, java.lang.String format, int destPort, int subId, final android.service.carrier.ICarrierMessagingCallback callback) {
            onReceiveTextSms(pdu, format, destPort, subId, new android.service.carrier.CarrierMessagingService.ResultCallback<java.lang.Integer>() {
                @java.lang.Override
                public void onReceiveResult(java.lang.Integer options) throws android.os.RemoteException {
                    callback.onFilterComplete(options);
                }
            });
        }

        @java.lang.Override
        public void sendTextSms(java.lang.String text, int subId, java.lang.String destAddress, int sendSmsFlag, final android.service.carrier.ICarrierMessagingCallback callback) {
            onSendTextSms(text, subId, destAddress, sendSmsFlag, new android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendSmsResult>() {
                @java.lang.Override
                public void onReceiveResult(final android.service.carrier.CarrierMessagingService.SendSmsResult result) throws android.os.RemoteException {
                    callback.onSendSmsComplete(result.getSendStatus(), result.getMessageRef());
                }
            });
        }

        @java.lang.Override
        public void sendDataSms(byte[] data, int subId, java.lang.String destAddress, int destPort, int sendSmsFlag, final android.service.carrier.ICarrierMessagingCallback callback) {
            onSendDataSms(data, subId, destAddress, destPort, sendSmsFlag, new android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendSmsResult>() {
                @java.lang.Override
                public void onReceiveResult(final android.service.carrier.CarrierMessagingService.SendSmsResult result) throws android.os.RemoteException {
                    callback.onSendSmsComplete(result.getSendStatus(), result.getMessageRef());
                }
            });
        }

        @java.lang.Override
        public void sendMultipartTextSms(java.util.List<java.lang.String> parts, int subId, java.lang.String destAddress, int sendSmsFlag, final android.service.carrier.ICarrierMessagingCallback callback) {
            onSendMultipartTextSms(parts, subId, destAddress, sendSmsFlag, new android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendMultipartSmsResult>() {
                @java.lang.Override
                public void onReceiveResult(final android.service.carrier.CarrierMessagingService.SendMultipartSmsResult result) throws android.os.RemoteException {
                    callback.onSendMultipartSmsComplete(result.getSendStatus(), result.getMessageRefs());
                }
            });
        }

        @java.lang.Override
        public void sendMms(android.net.Uri pduUri, int subId, android.net.Uri location, final android.service.carrier.ICarrierMessagingCallback callback) {
            onSendMms(pduUri, subId, location, new android.service.carrier.CarrierMessagingService.ResultCallback<android.service.carrier.CarrierMessagingService.SendMmsResult>() {
                @java.lang.Override
                public void onReceiveResult(final android.service.carrier.CarrierMessagingService.SendMmsResult result) throws android.os.RemoteException {
                    callback.onSendMmsComplete(result.getSendStatus(), result.getSendConfPdu());
                }
            });
        }

        @java.lang.Override
        public void downloadMms(android.net.Uri pduUri, int subId, android.net.Uri location, final android.service.carrier.ICarrierMessagingCallback callback) {
            onDownloadMms(pduUri, subId, location, new android.service.carrier.CarrierMessagingService.ResultCallback<java.lang.Integer>() {
                @java.lang.Override
                public void onReceiveResult(java.lang.Integer result) throws android.os.RemoteException {
                    callback.onDownloadMmsComplete(result);
                }
            });
        }
    }
}

