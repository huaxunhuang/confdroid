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
package android.bluetooth.client.map;


public class BluetoothMasClient {
    private static final java.lang.String TAG = "BluetoothMasClient";

    private static final int SOCKET_CONNECTED = 10;

    private static final int SOCKET_ERROR = 11;

    /**
     * Callback message sent when connection state changes
     * <p>
     * <code>arg1</code> is set to {@link #STATUS_OK} when connection is
     * established successfully and {@link #STATUS_FAILED} when connection
     * either failed or was disconnected (depends on request from application)
     *
     * @see #connect()
     * @see #disconnect()
     */
    public static final int EVENT_CONNECT = 1;

    /**
     * Callback message sent when MSE accepted update inbox request
     *
     * @see #updateInbox()
     */
    public static final int EVENT_UPDATE_INBOX = 2;

    /**
     * Callback message sent when path is changed
     * <p>
     * <code>obj</code> is set to path currently set on MSE
     *
     * @see #setFolderRoot()
     * @see #setFolderUp()
     * @see #setFolderDown(String)
     */
    public static final int EVENT_SET_PATH = 3;

    /**
     * Callback message sent when folder listing is received
     * <p>
     * <code>obj</code> contains ArrayList of sub-folder names
     *
     * @see #getFolderListing()
     * @see #getFolderListing(int, int)
     */
    public static final int EVENT_GET_FOLDER_LISTING = 4;

    /**
     * Callback message sent when folder listing size is received
     * <p>
     * <code>obj</code> contains number of items in folder listing
     *
     * @see #getFolderListingSize()
     */
    public static final int EVENT_GET_FOLDER_LISTING_SIZE = 5;

    /**
     * Callback message sent when messages listing is received
     * <p>
     * <code>obj</code> contains ArrayList of {@link BluetoothMapBmessage}
     *
     * @see #getMessagesListing(String, int)
     * @see #getMessagesListing(String, int, MessagesFilter, int)
     * @see #getMessagesListing(String, int, MessagesFilter, int, int, int)
     */
    public static final int EVENT_GET_MESSAGES_LISTING = 6;

    /**
     * Callback message sent when message is received
     * <p>
     * <code>obj</code> contains {@link BluetoothMapBmessage}
     *
     * @see #getMessage(String, CharsetType, boolean)
     */
    public static final int EVENT_GET_MESSAGE = 7;

    /**
     * Callback message sent when message status is changed
     *
     * @see #setMessageDeletedStatus(String, boolean)
     * @see #setMessageReadStatus(String, boolean)
     */
    public static final int EVENT_SET_MESSAGE_STATUS = 8;

    /**
     * Callback message sent when message is pushed to MSE
     * <p>
     * <code>obj</code> contains handle of message as allocated by MSE
     *
     * @see #pushMessage(String, BluetoothMapBmessage, CharsetType)
     * @see #pushMessage(String, BluetoothMapBmessage, CharsetType, boolean,
    boolean)
     */
    public static final int EVENT_PUSH_MESSAGE = 9;

    /**
     * Callback message sent when notification status is changed
     * <p>
     * <code>obj</code> contains <code>1</code> if notifications are enabled and
     * <code>0</code> otherwise
     *
     * @see #setNotificationRegistration(boolean)
     */
    public static final int EVENT_SET_NOTIFICATION_REGISTRATION = 10;

    /**
     * Callback message sent when event report is received from MSE to MNS
     * <p>
     * <code>obj</code> contains {@link BluetoothMapEventReport}
     *
     * @see #setNotificationRegistration(boolean)
     */
    public static final int EVENT_EVENT_REPORT = 11;

    /**
     * Callback message sent when messages listing size is received
     * <p>
     * <code>obj</code> contains number of items in messages listing
     *
     * @see #getMessagesListingSize()
     */
    public static final int EVENT_GET_MESSAGES_LISTING_SIZE = 12;

    /**
     * Status for callback message when request is successful
     */
    public static final int STATUS_OK = 0;

    /**
     * Status for callback message when request is not successful
     */
    public static final int STATUS_FAILED = 1;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_DEFAULT = 0x0;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_SUBJECT = 0x1;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_DATETIME = 0x2;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_SENDER_NAME = 0x4;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_SENDER_ADDRESSING = 0x8;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_RECIPIENT_NAME = 0x10;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_RECIPIENT_ADDRESSING = 0x20;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_TYPE = 0x40;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_SIZE = 0x80;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_RECEPTION_STATUS = 0x100;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_TEXT = 0x200;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_ATTACHMENT_SIZE = 0x400;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_PRIORITY = 0x800;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_READ = 0x1000;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_SENT = 0x2000;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_PROTECTED = 0x4000;

    /**
     * Constant corresponding to <code>ParameterMask</code> application
     * parameter value in MAP specification
     */
    public static final int PARAMETER_REPLYTO_ADDRESSING = 0x8000;

    public enum ConnectionState {

        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING;}

    public enum CharsetType {

        NATIVE,
        UTF_8;}

    /**
     * device associated with client
     */
    private final android.bluetooth.BluetoothDevice mDevice;

    /**
     * MAS instance associated with client
     */
    private final android.bluetooth.SdpMasRecord mMas;

    /**
     * callback handler to application
     */
    private final android.os.Handler mCallback;

    private android.bluetooth.client.map.BluetoothMasClient.ConnectionState mConnectionState = android.bluetooth.client.map.BluetoothMasClient.ConnectionState.DISCONNECTED;

    private boolean mNotificationEnabled = false;

    private android.bluetooth.client.map.BluetoothMasClient.SocketConnectThread mConnectThread = null;

    private javax.obex.ObexTransport mObexTransport = null;

    private android.bluetooth.client.map.BluetoothMasObexClientSession mObexSession = null;

    private android.bluetooth.client.map.BluetoothMasClient.SessionHandler mSessionHandler = null;

    private android.bluetooth.client.map.BluetoothMnsService mMnsService = null;

    private java.util.ArrayDeque<java.lang.String> mPath = null;

    private static class SessionHandler extends android.os.Handler {
        private final java.lang.ref.WeakReference<android.bluetooth.client.map.BluetoothMasClient> mClient;

        public SessionHandler(android.bluetooth.client.map.BluetoothMasClient client) {
            super();
            mClient = new java.lang.ref.WeakReference<android.bluetooth.client.map.BluetoothMasClient>(client);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.bluetooth.client.map.BluetoothMasClient client = mClient.get();
            if (client == null) {
                return;
            }
            android.util.Log.v(android.bluetooth.client.map.BluetoothMasClient.TAG, "handleMessage  " + msg.what);
            switch (msg.what) {
                case android.bluetooth.client.map.BluetoothMasClient.SOCKET_ERROR :
                    client.mConnectThread = null;
                    client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_CONNECT, false);
                    break;
                case android.bluetooth.client.map.BluetoothMasClient.SOCKET_CONNECTED :
                    client.mConnectThread = null;
                    client.mObexTransport = ((javax.obex.ObexTransport) (msg.obj));
                    client.mObexSession = new android.bluetooth.client.map.BluetoothMasObexClientSession(client.mObexTransport, client.mSessionHandler);
                    client.mObexSession.start();
                    break;
                case android.bluetooth.client.map.BluetoothMasObexClientSession.MSG_OBEX_CONNECTED :
                    client.mPath.clear();// we're in root after connected

                    client.mConnectionState = android.bluetooth.client.map.BluetoothMasClient.ConnectionState.CONNECTED;
                    client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_CONNECT, true);
                    break;
                case android.bluetooth.client.map.BluetoothMasObexClientSession.MSG_OBEX_DISCONNECTED :
                    client.mConnectionState = android.bluetooth.client.map.BluetoothMasClient.ConnectionState.DISCONNECTED;
                    client.mNotificationEnabled = false;
                    client.mObexSession = null;
                    client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_CONNECT, false);
                    break;
                case android.bluetooth.client.map.BluetoothMasObexClientSession.MSG_REQUEST_COMPLETED :
                    android.bluetooth.client.map.BluetoothMasRequest request = ((android.bluetooth.client.map.BluetoothMasRequest) (msg.obj));
                    int status = (request.isSuccess()) ? android.bluetooth.client.map.BluetoothMasClient.STATUS_OK : android.bluetooth.client.map.BluetoothMasClient.STATUS_FAILED;
                    android.util.Log.v(android.bluetooth.client.map.BluetoothMasClient.TAG, (("MSG_REQUEST_COMPLETED (" + status) + ") for ") + request.getClass().getName());
                    if (request instanceof android.bluetooth.client.map.BluetoothMasRequestUpdateInbox) {
                        client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_UPDATE_INBOX, request.isSuccess());
                    } else
                        if (request instanceof android.bluetooth.client.map.BluetoothMasRequestSetPath) {
                            if (request.isSuccess()) {
                                android.bluetooth.client.map.BluetoothMasRequestSetPath req = ((android.bluetooth.client.map.BluetoothMasRequestSetPath) (request));
                                switch (req.mDir) {
                                    case UP :
                                        if (client.mPath.size() > 0) {
                                            client.mPath.removeLast();
                                        }
                                        break;
                                    case ROOT :
                                        client.mPath.clear();
                                        break;
                                    case DOWN :
                                        client.mPath.addLast(req.mName);
                                        break;
                                }
                            }
                            client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_SET_PATH, request.isSuccess(), client.getCurrentPath());
                        } else
                            if (request instanceof android.bluetooth.client.map.BluetoothMasRequestGetFolderListing) {
                                android.bluetooth.client.map.BluetoothMasRequestGetFolderListing req = ((android.bluetooth.client.map.BluetoothMasRequestGetFolderListing) (request));
                                java.util.ArrayList<java.lang.String> folders = req.getList();
                                client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_GET_FOLDER_LISTING, request.isSuccess(), folders);
                            } else
                                if (request instanceof android.bluetooth.client.map.BluetoothMasRequestGetFolderListingSize) {
                                    int size = ((android.bluetooth.client.map.BluetoothMasRequestGetFolderListingSize) (request)).getSize();
                                    client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_GET_FOLDER_LISTING_SIZE, request.isSuccess(), size);
                                } else
                                    if (request instanceof android.bluetooth.client.map.BluetoothMasRequestGetMessagesListing) {
                                        android.bluetooth.client.map.BluetoothMasRequestGetMessagesListing req = ((android.bluetooth.client.map.BluetoothMasRequestGetMessagesListing) (request));
                                        java.util.ArrayList<android.bluetooth.client.map.BluetoothMapMessage> msgs = req.getList();
                                        client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_GET_MESSAGES_LISTING, request.isSuccess(), msgs);
                                    } else
                                        if (request instanceof android.bluetooth.client.map.BluetoothMasRequestGetMessage) {
                                            android.bluetooth.client.map.BluetoothMasRequestGetMessage req = ((android.bluetooth.client.map.BluetoothMasRequestGetMessage) (request));
                                            android.bluetooth.client.map.BluetoothMapBmessage bmsg = req.getMessage();
                                            client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_GET_MESSAGE, request.isSuccess(), bmsg);
                                        } else
                                            if (request instanceof android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus) {
                                                client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_SET_MESSAGE_STATUS, request.isSuccess());
                                            } else
                                                if (request instanceof android.bluetooth.client.map.BluetoothMasRequestPushMessage) {
                                                    android.bluetooth.client.map.BluetoothMasRequestPushMessage req = ((android.bluetooth.client.map.BluetoothMasRequestPushMessage) (request));
                                                    java.lang.String handle = req.getMsgHandle();
                                                    client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_PUSH_MESSAGE, request.isSuccess(), handle);
                                                } else
                                                    if (request instanceof android.bluetooth.client.map.BluetoothMasRequestSetNotificationRegistration) {
                                                        android.bluetooth.client.map.BluetoothMasRequestSetNotificationRegistration req = ((android.bluetooth.client.map.BluetoothMasRequestSetNotificationRegistration) (request));
                                                        client.mNotificationEnabled = (req.isSuccess()) ? req.getStatus() : client.mNotificationEnabled;
                                                        client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_SET_NOTIFICATION_REGISTRATION, request.isSuccess(), client.mNotificationEnabled ? 1 : 0);
                                                    } else
                                                        if (request instanceof android.bluetooth.client.map.BluetoothMasRequestGetMessagesListingSize) {
                                                            int size = ((android.bluetooth.client.map.BluetoothMasRequestGetMessagesListingSize) (request)).getSize();
                                                            client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_GET_MESSAGES_LISTING_SIZE, request.isSuccess(), size);
                                                        }









                    break;
                case android.bluetooth.client.map.BluetoothMnsService.EVENT_REPORT :
                    /* pass event report directly to app */
                    client.sendToClient(android.bluetooth.client.map.BluetoothMasClient.EVENT_EVENT_REPORT, true, msg.obj);
                    break;
            }
        }
    }

    private void sendToClient(int event, boolean success) {
        sendToClient(event, success, null);
    }

    private void sendToClient(int event, boolean success, int param) {
        sendToClient(event, success, java.lang.Integer.valueOf(param));
    }

    private void sendToClient(int event, boolean success, java.lang.Object param) {
        // Send  event, status and notification state for both sucess and failure case.
        mCallback.obtainMessage(event, success ? android.bluetooth.client.map.BluetoothMasClient.STATUS_OK : android.bluetooth.client.map.BluetoothMasClient.STATUS_FAILED, mMas.getMasInstanceId(), param).sendToTarget();
    }

    private class SocketConnectThread extends java.lang.Thread {
        private android.bluetooth.BluetoothSocket socket = null;

        public SocketConnectThread() {
            super("SocketConnectThread");
        }

        @java.lang.Override
        public void run() {
            try {
                socket = mDevice.createRfcommSocket(mMas.getRfcommCannelNumber());
                socket.connect();
                android.bluetooth.client.map.BluetoothMapRfcommTransport transport;
                transport = new android.bluetooth.client.map.BluetoothMapRfcommTransport(socket);
                mSessionHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasClient.SOCKET_CONNECTED, transport).sendToTarget();
            } catch (java.io.IOException e) {
                android.util.Log.e(android.bluetooth.client.map.BluetoothMasClient.TAG, "Error when creating/connecting socket", e);
                closeSocket();
                mSessionHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasClient.SOCKET_ERROR).sendToTarget();
            }
        }

        @java.lang.Override
        public void interrupt() {
            closeSocket();
        }

        private void closeSocket() {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (java.io.IOException e) {
                android.util.Log.e(android.bluetooth.client.map.BluetoothMasClient.TAG, "Error when closing socket", e);
            }
        }
    }

    /**
     * Object representation of filters to be applied on message listing
     *
     * @see #getMessagesListing(String, int, MessagesFilter, int)
     * @see #getMessagesListing(String, int, MessagesFilter, int, int, int)
     */
    public static final class MessagesFilter {
        public static final byte MESSAGE_TYPE_ALL = 0x0;

        public static final byte MESSAGE_TYPE_SMS_GSM = 0x1;

        public static final byte MESSAGE_TYPE_SMS_CDMA = 0x2;

        public static final byte MESSAGE_TYPE_EMAIL = 0x4;

        public static final byte MESSAGE_TYPE_MMS = 0x8;

        public static final byte READ_STATUS_ANY = 0x0;

        public static final byte READ_STATUS_UNREAD = 0x1;

        public static final byte READ_STATUS_READ = 0x2;

        public static final byte PRIORITY_ANY = 0x0;

        public static final byte PRIORITY_HIGH = 0x1;

        public static final byte PRIORITY_NON_HIGH = 0x2;

        byte messageType = android.bluetooth.client.map.BluetoothMasClient.MessagesFilter.MESSAGE_TYPE_ALL;

        java.lang.String periodBegin = null;

        java.lang.String periodEnd = null;

        byte readStatus = android.bluetooth.client.map.BluetoothMasClient.MessagesFilter.READ_STATUS_ANY;

        java.lang.String recipient = null;

        java.lang.String originator = null;

        byte priority = android.bluetooth.client.map.BluetoothMasClient.MessagesFilter.PRIORITY_ANY;

        public MessagesFilter() {
        }

        public void setMessageType(byte filter) {
            messageType = filter;
        }

        public void setPeriod(java.util.Date filterBegin, java.util.Date filterEnd) {
            // Handle possible NPE for obexTime constructor utility
            if (filterBegin != null)
                periodBegin = new android.bluetooth.client.map.utils.ObexTime(filterBegin).toString();

            if (filterEnd != null)
                periodEnd = new android.bluetooth.client.map.utils.ObexTime(filterEnd).toString();

        }

        public void setReadStatus(byte readfilter) {
            readStatus = readfilter;
        }

        public void setRecipient(java.lang.String filter) {
            if ("".equals(filter)) {
                recipient = null;
            } else {
                recipient = filter;
            }
        }

        public void setOriginator(java.lang.String filter) {
            if ("".equals(filter)) {
                originator = null;
            } else {
                originator = filter;
            }
        }

        public void setPriority(byte filter) {
            priority = filter;
        }
    }

    /**
     * Constructs client object to communicate with single MAS instance on MSE
     *
     * @param device
     * 		{@link BluetoothDevice} corresponding to remote device
     * 		acting as MSE
     * @param mas
     * 		{@link BluetoothMasInstance} object describing MAS instance on
     * 		remote device
     * @param callback
     * 		{@link Handler} object to which callback messages will be
     * 		sent Each message will have <code>arg1</code> set to either
     * 		{@link #STATUS_OK} or {@link #STATUS_FAILED} and
     * 		<code>arg2</code> to MAS instance ID. <code>obj</code> in
     * 		message is event specific.
     */
    public BluetoothMasClient(android.bluetooth.BluetoothDevice device, android.bluetooth.SdpMasRecord mas, android.os.Handler callback) {
        mDevice = device;
        mMas = mas;
        mCallback = callback;
        mPath = new java.util.ArrayDeque<java.lang.String>();
    }

    /**
     * Retrieves MAS instance data associated with client
     *
     * @return instance data object
     */
    public android.bluetooth.SdpMasRecord getInstanceData() {
        return mMas;
    }

    /**
     * Connects to MAS instance
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_CONNECT}
     */
    public void connect() {
        if (mSessionHandler == null) {
            mSessionHandler = new android.bluetooth.client.map.BluetoothMasClient.SessionHandler(this);
        }
        if ((mConnectThread == null) && (mObexSession == null)) {
            mConnectionState = android.bluetooth.client.map.BluetoothMasClient.ConnectionState.CONNECTING;
            mConnectThread = new android.bluetooth.client.map.BluetoothMasClient.SocketConnectThread();
            mConnectThread.start();
        }
    }

    /**
     * Disconnects from MAS instance
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_CONNECT}
     */
    public void disconnect() {
        if ((mConnectThread == null) && (mObexSession == null)) {
            return;
        }
        mConnectionState = android.bluetooth.client.map.BluetoothMasClient.ConnectionState.DISCONNECTING;
        if (mConnectThread != null) {
            mConnectThread.interrupt();
        }
        if (mObexSession != null) {
            mObexSession.stop();
        }
    }

    @java.lang.Override
    public void finalize() {
        disconnect();
    }

    /**
     * Gets current connection state
     *
     * @return current connection state
     * @see ConnectionState
     */
    public android.bluetooth.client.map.BluetoothMasClient.ConnectionState getState() {
        return mConnectionState;
    }

    private boolean enableNotifications() {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMasClient.TAG, "enableNotifications()");
        if (mMnsService == null) {
            mMnsService = new android.bluetooth.client.map.BluetoothMnsService();
        }
        mMnsService.registerCallback(mMas.getMasInstanceId(), mSessionHandler);
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetNotificationRegistration(true);
        return mObexSession.makeRequest(request);
    }

    private boolean disableNotifications() {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMasClient.TAG, "enableNotifications()");
        if (mMnsService != null) {
            mMnsService.unregisterCallback(mMas.getMasInstanceId());
        }
        mMnsService = null;
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetNotificationRegistration(false);
        return mObexSession.makeRequest(request);
    }

    /**
     * Sets state of notifications for MAS instance
     * <p>
     * Once notifications are enabled, callback handler will receive
     * {@link #EVENT_EVENT_REPORT} when new notification is received
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_SET_NOTIFICATION_REGISTRATION}
     *
     * @param status
     * 		<code>true</code> if notifications shall be enabled,
     * 		<code>false</code> otherwise
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean setNotificationRegistration(boolean status) {
        if (mObexSession == null) {
            return false;
        }
        if (status) {
            return enableNotifications();
        } else {
            return disableNotifications();
        }
    }

    /**
     * Gets current state of notifications for MAS instance
     *
     * @return <code>true</code> if notifications are enabled,
    <code>false</code> otherwise
     */
    public boolean getNotificationRegistration() {
        return mNotificationEnabled;
    }

    /**
     * Goes back to root of folder hierarchy
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_SET_PATH}
     *
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean setFolderRoot() {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetPath(true);
        return mObexSession.makeRequest(request);
    }

    /**
     * Goes back to parent folder in folder hierarchy
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_SET_PATH}
     *
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean setFolderUp() {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetPath(false);
        return mObexSession.makeRequest(request);
    }

    /**
     * Goes down to specified sub-folder in folder hierarchy
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_SET_PATH}
     *
     * @param name
     * 		name of sub-folder
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean setFolderDown(java.lang.String name) {
        if (mObexSession == null) {
            return false;
        }
        if (((name == null) || name.isEmpty()) || name.contains("/")) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetPath(name);
        return mObexSession.makeRequest(request);
    }

    /**
     * Gets current path in folder hierarchy
     *
     * @return current path
     */
    public java.lang.String getCurrentPath() {
        if (mPath.size() == 0) {
            return "";
        }
        java.util.Iterator<java.lang.String> iter = mPath.iterator();
        java.lang.StringBuilder sb = new java.lang.StringBuilder(iter.next());
        while (iter.hasNext()) {
            sb.append("/").append(iter.next());
        } 
        return sb.toString();
    }

    /**
     * Gets list of sub-folders in current folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_FOLDER_LISTING}
     *
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean getFolderListing() {
        return getFolderListing(((short) (0)), ((short) (0)));
    }

    /**
     * Gets list of sub-folders in current folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_FOLDER_LISTING}
     *
     * @param maxListCount
     * 		maximum number of items returned or <code>0</code>
     * 		for default value
     * @param listStartOffset
     * 		index of first item returned or <code>0</code> for
     * 		default value
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     * @throws IllegalArgumentException
     * 		if either maxListCount or
     * 		listStartOffset are outside allowed range [0..65535]
     */
    public boolean getFolderListing(int maxListCount, int listStartOffset) {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestGetFolderListing(maxListCount, listStartOffset);
        return mObexSession.makeRequest(request);
    }

    /**
     * Gets number of sub-folders in current folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_FOLDER_LISTING_SIZE}
     *
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean getFolderListingSize() {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestGetFolderListingSize();
        return mObexSession.makeRequest(request);
    }

    /**
     * Gets list of messages in specified sub-folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_MESSAGES_LISTING}
     *
     * @param folder
     * 		name of sub-folder or <code>null</code> for current folder
     * @param parameters
     * 		bit-mask specifying requested parameters in listing or
     * 		<code>0</code> for default value
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean getMessagesListing(java.lang.String folder, int parameters) {
        return getMessagesListing(folder, parameters, null, ((byte) (0)), 0, 0);
    }

    /**
     * Gets list of messages in specified sub-folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_MESSAGES_LISTING}
     *
     * @param folder
     * 		name of sub-folder or <code>null</code> for current folder
     * @param parameters
     * 		corresponds to <code>ParameterMask</code> application
     * 		parameter in MAP specification
     * @param filter
     * 		{@link MessagesFilter} object describing filters to be
     * 		applied on listing by MSE
     * @param subjectLength
     * 		maximum length of message subject in returned
     * 		listing or <code>0</code> for default value
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     * @throws IllegalArgumentException
     * 		if subjectLength is outside allowed
     * 		range [0..255]
     */
    public boolean getMessagesListing(java.lang.String folder, int parameters, android.bluetooth.client.map.BluetoothMasClient.MessagesFilter filter, int subjectLength) {
        return getMessagesListing(folder, parameters, filter, subjectLength, 0, 0);
    }

    /**
     * Gets list of messages in specified sub-folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_MESSAGES_LISTING}
     *
     * @param folder
     * 		name of sub-folder or <code>null</code> for current folder
     * @param parameters
     * 		corresponds to <code>ParameterMask</code> application
     * 		parameter in MAP specification
     * @param filter
     * 		{@link MessagesFilter} object describing filters to be
     * 		applied on listing by MSE
     * @param subjectLength
     * 		maximum length of message subject in returned
     * 		listing or <code>0</code> for default value
     * @param maxListCount
     * 		maximum number of items returned or <code>0</code>
     * 		for default value
     * @param listStartOffset
     * 		index of first item returned or <code>0</code> for
     * 		default value
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     * @throws IllegalArgumentException
     * 		if subjectLength is outside allowed
     * 		range [0..255] or either maxListCount or listStartOffset are
     * 		outside allowed range [0..65535]
     */
    public boolean getMessagesListing(java.lang.String folder, int parameters, android.bluetooth.client.map.BluetoothMasClient.MessagesFilter filter, int subjectLength, int maxListCount, int listStartOffset) {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestGetMessagesListing(folder, parameters, filter, subjectLength, maxListCount, listStartOffset);
        return mObexSession.makeRequest(request);
    }

    /**
     * Gets number of messages in current folder
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_GET_MESSAGES_LISTING_SIZE}
     *
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean getMessagesListingSize() {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestGetMessagesListingSize();
        return mObexSession.makeRequest(request);
    }

    /**
     * Retrieves message from MSE
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_GET_MESSAGE}
     *
     * @param handle
     * 		handle of message to retrieve
     * @param charset
     * 		{@link CharsetType} object corresponding to
     * 		<code>Charset</code> application parameter in MAP
     * 		specification
     * @param attachment
     * 		corresponds to <code>Attachment</code> application
     * 		parameter in MAP specification
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean getMessage(java.lang.String handle, boolean attachment) {
        if (mObexSession == null) {
            return false;
        }
        try {
            /* just to validate */
            new java.math.BigInteger(handle, 16);
        } catch (java.lang.NumberFormatException e) {
            return false;
        }
        // Since we support only text messaging via Bluetooth, it is OK to restrict the requests to
        // force conversion to UTF-8.
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestGetMessage(handle, android.bluetooth.client.map.BluetoothMasClient.CharsetType.UTF_8, attachment);
        return mObexSession.makeRequest(request);
    }

    /**
     * Sets read status of message on MSE
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_SET_MESSAGE_STATUS}
     *
     * @param handle
     * 		handle of message
     * @param read
     * 		<code>true</code> for "read", <code>false</code> for "unread"
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean setMessageReadStatus(java.lang.String handle, boolean read) {
        if (mObexSession == null) {
            return false;
        }
        try {
            /* just to validate */
            new java.math.BigInteger(handle, 16);
        } catch (java.lang.NumberFormatException e) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus(handle, android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus.StatusIndicator.READ, read);
        return mObexSession.makeRequest(request);
    }

    /**
     * Sets deleted status of message on MSE
     * <p>
     * Upon completion callback handler will receive
     * {@link #EVENT_SET_MESSAGE_STATUS}
     *
     * @param handle
     * 		handle of message
     * @param deleted
     * 		<code>true</code> for "deleted", <code>false</code> for
     * 		"undeleted"
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean setMessageDeletedStatus(java.lang.String handle, boolean deleted) {
        if (mObexSession == null) {
            return false;
        }
        try {
            /* just to validate */
            new java.math.BigInteger(handle, 16);
        } catch (java.lang.NumberFormatException e) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus(handle, android.bluetooth.client.map.BluetoothMasRequestSetMessageStatus.StatusIndicator.DELETED, deleted);
        return mObexSession.makeRequest(request);
    }

    /**
     * Pushes new message to MSE
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_PUSH_MESSAGE}
     *
     * @param folder
     * 		name of sub-folder to push to or <code>null</code> for
     * 		current folder
     * @param charset
     * 		{@link CharsetType} object corresponding to
     * 		<code>Charset</code> application parameter in MAP
     * 		specification
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean pushMessage(java.lang.String folder, android.bluetooth.client.map.BluetoothMapBmessage bmsg, android.bluetooth.client.map.BluetoothMasClient.CharsetType charset) {
        return pushMessage(folder, bmsg, charset, false, false);
    }

    /**
     * Pushes new message to MSE
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_PUSH_MESSAGE}
     *
     * @param folder
     * 		name of sub-folder to push to or <code>null</code> for
     * 		current folder
     * @param bmsg
     * 		{@link BluetoothMapBmessage} object representing message to
     * 		be pushed
     * @param charset
     * 		{@link CharsetType} object corresponding to
     * 		<code>Charset</code> application parameter in MAP
     * 		specification
     * @param transparent
     * 		corresponds to <code>Transparent</code> application
     * 		parameter in MAP specification
     * @param retry
     * 		corresponds to <code>Transparent</code> application
     * 		parameter in MAP specification
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean pushMessage(java.lang.String folder, android.bluetooth.client.map.BluetoothMapBmessage bmsg, android.bluetooth.client.map.BluetoothMasClient.CharsetType charset, boolean transparent, boolean retry) {
        if (mObexSession == null) {
            return false;
        }
        java.lang.String bmsgString = android.bluetooth.client.map.BluetoothMapBmessageBuilder.createBmessage(bmsg);
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestPushMessage(folder, bmsgString, charset, transparent, retry);
        return mObexSession.makeRequest(request);
    }

    /**
     * Requests MSE to initiate ubdate of inbox
     * <p>
     * Upon completion callback handler will receive {@link #EVENT_UPDATE_INBOX}
     *
     * @return <code>true</code> if request has been sent, <code>false</code>
    otherwise
     */
    public boolean updateInbox() {
        if (mObexSession == null) {
            return false;
        }
        android.bluetooth.client.map.BluetoothMasRequest request = new android.bluetooth.client.map.BluetoothMasRequestUpdateInbox();
        return mObexSession.makeRequest(request);
    }
}

