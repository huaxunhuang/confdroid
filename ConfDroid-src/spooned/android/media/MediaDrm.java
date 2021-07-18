/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * MediaDrm can be used to obtain keys for decrypting protected media streams, in
 * conjunction with {@link android.media.MediaCrypto}.  The MediaDrm APIs
 * are designed to support the ISO/IEC 23001-7: Common Encryption standard, but
 * may also be used to implement other encryption schemes.
 * <p>
 * Encrypted content is prepared using an encryption server and stored in a content
 * library. The encrypted content is streamed or downloaded from the content library to
 * client devices via content servers.  Licenses to view the content are obtained from
 * a License Server.
 * <p>
 * <p><img src="../../../images/mediadrm_overview.png"
 *      alt="MediaDrm Overview diagram"
 *      border="0" /></p>
 * <p>
 * Keys are requested from the license server using a key request. The key
 * response is delivered to the client app, which provides the response to the
 * MediaDrm API.
 * <p>
 * A Provisioning server may be required to distribute device-unique credentials to
 * the devices.
 * <p>
 * Enforcing requirements related to the number of devices that may play content
 * simultaneously can be performed either through key renewal or using the secure
 * stop methods.
 * <p>
 * The following sequence diagram shows the interactions between the objects
 * involved while playing back encrypted content:
 * <p>
 * <p><img src="../../../images/mediadrm_decryption_sequence.png"
 *         alt="MediaDrm Overview diagram"
 *         border="0" /></p>
 * <p>
 * The app first constructs {@link android.media.MediaExtractor} and
 * {@link android.media.MediaCodec} objects. It accesses the DRM-scheme-identifying UUID,
 * typically from metadata in the content, and uses this UUID to construct an instance
 * of a MediaDrm object that is able to support the DRM scheme required by the content.
 * Crypto schemes are assigned 16 byte UUIDs.  The method {@link #isCryptoSchemeSupported}
 * can be used to query if a given scheme is supported on the device.
 * <p>
 * The app calls {@link #openSession} to generate a sessionId that will uniquely identify
 * the session in subsequent interactions. The app next uses the MediaDrm object to
 * obtain a key request message and send it to the license server, then provide
 * the server's response to the MediaDrm object.
 * <p>
 * Once the app has a sessionId, it can construct a MediaCrypto object from the UUID and
 * sessionId.  The MediaCrypto object is registered with the MediaCodec in the
 * {@link MediaCodec.#configure} method to enable the codec to decrypt content.
 * <p>
 * When the app has constructed {@link android.media.MediaExtractor},
 * {@link android.media.MediaCodec} and {@link android.media.MediaCrypto} objects,
 * it proceeds to pull samples from the extractor and queue them into the decoder.  For
 * encrypted content, the samples returned from the extractor remain encrypted, they
 * are only decrypted when the samples are delivered to the decoder.
 * <p>
 * MediaDrm methods throw {@link android.media.MediaDrm.MediaDrmStateException}
 * when a method is called on a MediaDrm object that has had an unrecoverable failure
 * in the DRM plugin or security hardware.
 * {@link android.media.MediaDrm.MediaDrmStateException} extends
 * {@link java.lang.IllegalStateException} with the addition of a developer-readable
 * diagnostic information string associated with the exception.
 * <p>
 * In the event of a mediaserver process crash or restart while a MediaDrm object
 * is active, MediaDrm methods may throw {@link android.media.MediaDrmResetException}.
 * To recover, the app must release the MediaDrm object, then create and initialize
 * a new one.
 * <p>
 * As {@link android.media.MediaDrmResetException} and
 * {@link android.media.MediaDrm.MediaDrmStateException} both extend
 * {@link java.lang.IllegalStateException}, they should be in an earlier catch()
 * block than {@link java.lang.IllegalStateException} if handled separately.
 * <p>
 * <a name="Callbacks"></a>
 * <h3>Callbacks</h3>
 * <p>Applications should register for informational events in order
 * to be informed of key state updates during playback or streaming.
 * Registration for these events is done via a call to
 * {@link #setOnEventListener}. In order to receive the respective
 * callback associated with this listener, applications are required to create
 * MediaDrm objects on a thread with its own Looper running (main UI
 * thread by default has a Looper running).
 */
public final class MediaDrm {
    private static final java.lang.String TAG = "MediaDrm";

    private static final java.lang.String PERMISSION = android.Manifest.permission.ACCESS_DRM_CERTIFICATES;

    private android.media.MediaDrm.EventHandler mEventHandler;

    private android.media.MediaDrm.EventHandler mOnKeyStatusChangeEventHandler;

    private android.media.MediaDrm.EventHandler mOnExpirationUpdateEventHandler;

    private android.media.MediaDrm.OnEventListener mOnEventListener;

    private android.media.MediaDrm.OnKeyStatusChangeListener mOnKeyStatusChangeListener;

    private android.media.MediaDrm.OnExpirationUpdateListener mOnExpirationUpdateListener;

    private long mNativeContext;

    /**
     * Specify no certificate type
     *
     * @unknown - not part of the public API at this time
     */
    public static final int CERTIFICATE_TYPE_NONE = 0;

    /**
     * Specify X.509 certificate type
     *
     * @unknown - not part of the public API at this time
     */
    public static final int CERTIFICATE_TYPE_X509 = 1;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.MediaDrm.CERTIFICATE_TYPE_NONE, android.media.MediaDrm.CERTIFICATE_TYPE_X509 })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface CertificateType {}

    /**
     * Query if the given scheme identified by its UUID is supported on
     * this device.
     *
     * @param uuid
     * 		The UUID of the crypto scheme.
     */
    public static final boolean isCryptoSchemeSupported(@android.annotation.NonNull
    java.util.UUID uuid) {
        return android.media.MediaDrm.isCryptoSchemeSupportedNative(android.media.MediaDrm.getByteArrayFromUUID(uuid), null);
    }

    /**
     * Query if the given scheme identified by its UUID is supported on
     * this device, and whether the drm plugin is able to handle the
     * media container format specified by mimeType.
     *
     * @param uuid
     * 		The UUID of the crypto scheme.
     * @param mimeType
     * 		The MIME type of the media container, e.g. "video/mp4"
     * 		or "video/webm"
     */
    public static final boolean isCryptoSchemeSupported(@android.annotation.NonNull
    java.util.UUID uuid, @android.annotation.NonNull
    java.lang.String mimeType) {
        return android.media.MediaDrm.isCryptoSchemeSupportedNative(android.media.MediaDrm.getByteArrayFromUUID(uuid), mimeType);
    }

    private static final byte[] getByteArrayFromUUID(@android.annotation.NonNull
    java.util.UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] uuidBytes = new byte[16];
        for (int i = 0; i < 8; ++i) {
            uuidBytes[i] = ((byte) (msb >>> (8 * (7 - i))));
            uuidBytes[8 + i] = ((byte) (lsb >>> (8 * (7 - i))));
        }
        return uuidBytes;
    }

    private static final native boolean isCryptoSchemeSupportedNative(@android.annotation.NonNull
    byte[] uuid, @android.annotation.Nullable
    java.lang.String mimeType);

    /**
     * Instantiate a MediaDrm object
     *
     * @param uuid
     * 		The UUID of the crypto scheme.
     * @throws UnsupportedSchemeException
     * 		if the device does not support the
     * 		specified scheme UUID
     */
    public MediaDrm(@android.annotation.NonNull
    java.util.UUID uuid) throws android.media.UnsupportedSchemeException {
        android.os.Looper looper;
        if ((looper = android.os.Looper.myLooper()) != null) {
            mEventHandler = new android.media.MediaDrm.EventHandler(this, looper);
        } else
            if ((looper = android.os.Looper.getMainLooper()) != null) {
                mEventHandler = new android.media.MediaDrm.EventHandler(this, looper);
            } else {
                mEventHandler = null;
            }

        /* Native setup requires a weak reference to our object.
        It's easier to create it here than in C++.
         */
        native_setup(new java.lang.ref.WeakReference<android.media.MediaDrm>(this), android.media.MediaDrm.getByteArrayFromUUID(uuid));
    }

    /**
     * Thrown when an unrecoverable failure occurs during a MediaDrm operation.
     * Extends java.lang.IllegalStateException with the addition of an error
     * code that may be useful in diagnosing the failure.
     */
    public static final class MediaDrmStateException extends java.lang.IllegalStateException {
        private final int mErrorCode;

        private final java.lang.String mDiagnosticInfo;

        /**
         *
         *
         * @unknown 
         */
        public MediaDrmStateException(int errorCode, @android.annotation.Nullable
        java.lang.String detailMessage) {
            super(detailMessage);
            mErrorCode = errorCode;
            // TODO get this from DRM session
            final java.lang.String sign = (errorCode < 0) ? "neg_" : "";
            mDiagnosticInfo = ("android.media.MediaDrm.error_" + sign) + java.lang.Math.abs(errorCode);
        }

        /**
         * Retrieve the associated error code
         *
         * @unknown 
         */
        public int getErrorCode() {
            return mErrorCode;
        }

        /**
         * Retrieve a developer-readable diagnostic information string
         * associated with the exception. Do not show this to end-users,
         * since this string will not be localized or generally comprehensible
         * to end-users.
         */
        @android.annotation.NonNull
        public java.lang.String getDiagnosticInfo() {
            return mDiagnosticInfo;
        }
    }

    /**
     * Register a callback to be invoked when a session expiration update
     * occurs.  The app's OnExpirationUpdateListener will be notified
     * when the expiration time of the keys in the session have changed.
     *
     * @param listener
     * 		the callback that will be run, or {@code null} to unregister the
     * 		previously registered callback.
     * @param handler
     * 		the handler on which the listener should be invoked, or
     * 		{@code null} if the listener should be invoked on the calling thread's looper.
     */
    public void setOnExpirationUpdateListener(@android.annotation.Nullable
    android.media.MediaDrm.OnExpirationUpdateListener listener, @android.annotation.Nullable
    android.os.Handler handler) {
        if (listener != null) {
            android.os.Looper looper = (handler != null) ? handler.getLooper() : android.os.Looper.myLooper();
            if (looper != null) {
                if ((mEventHandler == null) || (mEventHandler.getLooper() != looper)) {
                    mEventHandler = new android.media.MediaDrm.EventHandler(this, looper);
                }
            }
        }
        mOnExpirationUpdateListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when a drm session
     * expiration update occurs
     */
    public interface OnExpirationUpdateListener {
        /**
         * Called when a session expiration update occurs, to inform the app
         * about the change in expiration time
         *
         * @param md
         * 		the MediaDrm object on which the event occurred
         * @param sessionId
         * 		the DRM session ID on which the event occurred
         * @param expirationTime
         * 		the new expiration time for the keys in the session.
         * 		The time is in milliseconds, relative to the Unix epoch.  A time of
         * 		0 indicates that the keys never expire.
         */
        void onExpirationUpdate(@android.annotation.NonNull
        android.media.MediaDrm md, @android.annotation.NonNull
        byte[] sessionId, long expirationTime);
    }

    /**
     * Register a callback to be invoked when the state of keys in a session
     * change, e.g. when a license update occurs or when a license expires.
     *
     * @param listener
     * 		the callback that will be run when key status changes, or
     * 		{@code null} to unregister the previously registered callback.
     * @param handler
     * 		the handler on which the listener should be invoked, or
     * 		null if the listener should be invoked on the calling thread's looper.
     */
    public void setOnKeyStatusChangeListener(@android.annotation.Nullable
    android.media.MediaDrm.OnKeyStatusChangeListener listener, @android.annotation.Nullable
    android.os.Handler handler) {
        if (listener != null) {
            android.os.Looper looper = (handler != null) ? handler.getLooper() : android.os.Looper.myLooper();
            if (looper != null) {
                if ((mEventHandler == null) || (mEventHandler.getLooper() != looper)) {
                    mEventHandler = new android.media.MediaDrm.EventHandler(this, looper);
                }
            }
        }
        mOnKeyStatusChangeListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the keys in a drm
     * session change states.
     */
    public interface OnKeyStatusChangeListener {
        /**
         * Called when the keys in a session change status, such as when the license
         * is renewed or expires.
         *
         * @param md
         * 		the MediaDrm object on which the event occurred
         * @param sessionId
         * 		the DRM session ID on which the event occurred
         * @param keyInformation
         * 		a list of {@link MediaDrm.KeyStatus}
         * 		instances indicating the status for each key in the session
         * @param hasNewUsableKey
         * 		indicates if a key has been added that is usable,
         * 		which may trigger an attempt to resume playback on the media stream
         * 		if it is currently blocked waiting for a key.
         */
        void onKeyStatusChange(@android.annotation.NonNull
        android.media.MediaDrm md, @android.annotation.NonNull
        byte[] sessionId, @android.annotation.NonNull
        java.util.List<android.media.MediaDrm.KeyStatus> keyInformation, boolean hasNewUsableKey);
    }

    /**
     * Defines the status of a key.
     * A KeyStatus for each key in a session is provided to the
     * {@link OnKeyStatusChangeListener#onKeyStatusChange}
     * listener.
     */
    public static final class KeyStatus {
        private final byte[] mKeyId;

        private final int mStatusCode;

        /**
         * The key is currently usable to decrypt media data
         */
        public static final int STATUS_USABLE = 0;

        /**
         * The key is no longer usable to decrypt media data because its
         * expiration time has passed.
         */
        public static final int STATUS_EXPIRED = 1;

        /**
         * The key is not currently usable to decrypt media data because its
         * output requirements cannot currently be met.
         */
        public static final int STATUS_OUTPUT_NOT_ALLOWED = 2;

        /**
         * The status of the key is not yet known and is being determined.
         * The status will be updated with the actual status when it has
         * been determined.
         */
        public static final int STATUS_PENDING = 3;

        /**
         * The key is not currently usable to decrypt media data because of an
         * internal error in processing unrelated to input parameters.  This error
         * is not actionable by an app.
         */
        public static final int STATUS_INTERNAL_ERROR = 4;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.IntDef({ android.media.MediaDrm.KeyStatus.STATUS_USABLE, android.media.MediaDrm.KeyStatus.STATUS_EXPIRED, android.media.MediaDrm.KeyStatus.STATUS_OUTPUT_NOT_ALLOWED, android.media.MediaDrm.KeyStatus.STATUS_PENDING, android.media.MediaDrm.KeyStatus.STATUS_INTERNAL_ERROR })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface KeyStatusCode {}

        KeyStatus(@android.annotation.NonNull
        byte[] keyId, @android.media.MediaDrm.KeyStatus.KeyStatusCode
        int statusCode) {
            mKeyId = keyId;
            mStatusCode = statusCode;
        }

        /**
         * Returns the status code for the key
         *
         * @return one of {@link #STATUS_USABLE}, {@link #STATUS_EXPIRED},
        {@link #STATUS_OUTPUT_NOT_ALLOWED}, {@link #STATUS_PENDING}
        or {@link #STATUS_INTERNAL_ERROR}.
         */
        @android.media.MediaDrm.KeyStatus.KeyStatusCode
        public int getStatusCode() {
            return mStatusCode;
        }

        /**
         * Returns the id for the key
         */
        @android.annotation.NonNull
        public byte[] getKeyId() {
            return mKeyId;
        }
    }

    /**
     * Register a callback to be invoked when an event occurs
     *
     * @param listener
     * 		the callback that will be run.  Use {@code null} to
     * 		stop receiving event callbacks.
     */
    public void setOnEventListener(@android.annotation.Nullable
    android.media.MediaDrm.OnEventListener listener) {
        mOnEventListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when a drm event
     * occurs
     */
    public interface OnEventListener {
        /**
         * Called when an event occurs that requires the app to be notified
         *
         * @param md
         * 		the MediaDrm object on which the event occurred
         * @param sessionId
         * 		the DRM session ID on which the event occurred,
         * 		or {@code null} if there is no session ID associated with the event.
         * @param event
         * 		indicates the event type
         * @param extra
         * 		an secondary error code
         * @param data
         * 		optional byte array of data that may be associated with the event
         */
        void onEvent(@android.annotation.NonNull
        android.media.MediaDrm md, @android.annotation.Nullable
        byte[] sessionId, @android.media.MediaDrm.DrmEvent
        int event, int extra, @android.annotation.Nullable
        byte[] data);
    }

    /**
     * This event type indicates that the app needs to request a certificate from
     * the provisioning server.  The request message data is obtained using
     * {@link #getProvisionRequest}
     *
     * @deprecated Handle provisioning via {@link android.media.NotProvisionedException}
    instead.
     */
    public static final int EVENT_PROVISION_REQUIRED = 1;

    /**
     * This event type indicates that the app needs to request keys from a license
     * server.  The request message data is obtained using {@link #getKeyRequest}.
     */
    public static final int EVENT_KEY_REQUIRED = 2;

    /**
     * This event type indicates that the licensed usage duration for keys in a session
     * has expired.  The keys are no longer valid.
     */
    public static final int EVENT_KEY_EXPIRED = 3;

    /**
     * This event may indicate some specific vendor-defined condition, see your
     * DRM provider documentation for details
     */
    public static final int EVENT_VENDOR_DEFINED = 4;

    /**
     * This event indicates that a session opened by the app has been reclaimed by the resource
     * manager.
     */
    public static final int EVENT_SESSION_RECLAIMED = 5;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.MediaDrm.EVENT_PROVISION_REQUIRED, android.media.MediaDrm.EVENT_KEY_REQUIRED, android.media.MediaDrm.EVENT_KEY_EXPIRED, android.media.MediaDrm.EVENT_VENDOR_DEFINED, android.media.MediaDrm.EVENT_SESSION_RECLAIMED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DrmEvent {}

    private static final int DRM_EVENT = 200;

    private static final int EXPIRATION_UPDATE = 201;

    private static final int KEY_STATUS_CHANGE = 202;

    private class EventHandler extends android.os.Handler {
        private android.media.MediaDrm mMediaDrm;

        public EventHandler(@android.annotation.NonNull
        android.media.MediaDrm md, @android.annotation.NonNull
        android.os.Looper looper) {
            super(looper);
            mMediaDrm = md;
        }

        @java.lang.Override
        public void handleMessage(@android.annotation.NonNull
        android.os.Message msg) {
            if (mMediaDrm.mNativeContext == 0) {
                android.util.Log.w(android.media.MediaDrm.TAG, "MediaDrm went away with unhandled events");
                return;
            }
            switch (msg.what) {
                case android.media.MediaDrm.DRM_EVENT :
                    if (mOnEventListener != null) {
                        if ((msg.obj != null) && (msg.obj instanceof android.os.Parcel)) {
                            android.os.Parcel parcel = ((android.os.Parcel) (msg.obj));
                            byte[] sessionId = parcel.createByteArray();
                            if (sessionId.length == 0) {
                                sessionId = null;
                            }
                            byte[] data = parcel.createByteArray();
                            if (data.length == 0) {
                                data = null;
                            }
                            android.util.Log.i(android.media.MediaDrm.TAG, ((("Drm event (" + msg.arg1) + ",") + msg.arg2) + ")");
                            mOnEventListener.onEvent(mMediaDrm, sessionId, msg.arg1, msg.arg2, data);
                        }
                    }
                    return;
                case android.media.MediaDrm.KEY_STATUS_CHANGE :
                    if (mOnKeyStatusChangeListener != null) {
                        if ((msg.obj != null) && (msg.obj instanceof android.os.Parcel)) {
                            android.os.Parcel parcel = ((android.os.Parcel) (msg.obj));
                            byte[] sessionId = parcel.createByteArray();
                            if (sessionId.length > 0) {
                                java.util.List<android.media.MediaDrm.KeyStatus> keyStatusList = keyStatusListFromParcel(parcel);
                                boolean hasNewUsableKey = parcel.readInt() != 0;
                                android.util.Log.i(android.media.MediaDrm.TAG, "Drm key status changed");
                                mOnKeyStatusChangeListener.onKeyStatusChange(mMediaDrm, sessionId, keyStatusList, hasNewUsableKey);
                            }
                        }
                    }
                    return;
                case android.media.MediaDrm.EXPIRATION_UPDATE :
                    if (mOnExpirationUpdateListener != null) {
                        if ((msg.obj != null) && (msg.obj instanceof android.os.Parcel)) {
                            android.os.Parcel parcel = ((android.os.Parcel) (msg.obj));
                            byte[] sessionId = parcel.createByteArray();
                            if (sessionId.length > 0) {
                                long expirationTime = parcel.readLong();
                                android.util.Log.i(android.media.MediaDrm.TAG, "Drm key expiration update: " + expirationTime);
                                mOnExpirationUpdateListener.onExpirationUpdate(mMediaDrm, sessionId, expirationTime);
                            }
                        }
                    }
                    return;
                default :
                    android.util.Log.e(android.media.MediaDrm.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    /**
     * Parse a list of KeyStatus objects from an event parcel
     */
    @android.annotation.NonNull
    private java.util.List<android.media.MediaDrm.KeyStatus> keyStatusListFromParcel(@android.annotation.NonNull
    android.os.Parcel parcel) {
        int nelems = parcel.readInt();
        java.util.List<android.media.MediaDrm.KeyStatus> keyStatusList = new java.util.ArrayList(nelems);
        while ((nelems--) > 0) {
            byte[] keyId = parcel.createByteArray();
            int keyStatusCode = parcel.readInt();
            keyStatusList.add(new android.media.MediaDrm.KeyStatus(keyId, keyStatusCode));
        } 
        return keyStatusList;
    }

    /**
     * This method is called from native code when an event occurs.  This method
     * just uses the EventHandler system to post the event back to the main app thread.
     * We use a weak reference to the original MediaPlayer object so that the native
     * code is safe from the object disappearing from underneath it.  (This is
     * the cookie passed to native_setup().)
     */
    private static void postEventFromNative(@android.annotation.NonNull
    java.lang.Object mediadrm_ref, int what, int eventType, int extra, @android.annotation.Nullable
    java.lang.Object obj) {
        android.media.MediaDrm md = ((android.media.MediaDrm) (((java.lang.ref.WeakReference<android.media.MediaDrm>) (mediadrm_ref)).get()));
        if (md == null) {
            return;
        }
        if (md.mEventHandler != null) {
            android.os.Message m = md.mEventHandler.obtainMessage(what, eventType, extra, obj);
            md.mEventHandler.sendMessage(m);
        }
    }

    /**
     * Open a new session with the MediaDrm object.  A session ID is returned.
     *
     * @throws NotProvisionedException
     * 		if provisioning is needed
     * @throws ResourceBusyException
     * 		if required resources are in use
     */
    @android.annotation.NonNull
    public native byte[] openSession() throws android.media.NotProvisionedException, android.media.ResourceBusyException;

    /**
     * Close a session on the MediaDrm object that was previously opened
     * with {@link #openSession}.
     */
    public native void closeSession(@android.annotation.NonNull
    byte[] sessionId);

    /**
     * This key request type species that the keys will be for online use, they will
     * not be saved to the device for subsequent use when the device is not connected
     * to a network.
     */
    public static final int KEY_TYPE_STREAMING = 1;

    /**
     * This key request type specifies that the keys will be for offline use, they
     * will be saved to the device for use when the device is not connected to a network.
     */
    public static final int KEY_TYPE_OFFLINE = 2;

    /**
     * This key request type specifies that previously saved offline keys should be released.
     */
    public static final int KEY_TYPE_RELEASE = 3;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.MediaDrm.KEY_TYPE_STREAMING, android.media.MediaDrm.KEY_TYPE_OFFLINE, android.media.MediaDrm.KEY_TYPE_RELEASE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface KeyType {}

    /**
     * Contains the opaque data an app uses to request keys from a license server
     */
    public static final class KeyRequest {
        private byte[] mData;

        private java.lang.String mDefaultUrl;

        private int mRequestType;

        /**
         * Key request type is initial license request
         */
        public static final int REQUEST_TYPE_INITIAL = 0;

        /**
         * Key request type is license renewal
         */
        public static final int REQUEST_TYPE_RENEWAL = 1;

        /**
         * Key request type is license release
         */
        public static final int REQUEST_TYPE_RELEASE = 2;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.IntDef({ android.media.MediaDrm.KeyRequest.REQUEST_TYPE_INITIAL, android.media.MediaDrm.KeyRequest.REQUEST_TYPE_RENEWAL, android.media.MediaDrm.KeyRequest.REQUEST_TYPE_RELEASE })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface RequestType {}

        KeyRequest() {
        }

        /**
         * Get the opaque message data
         */
        @android.annotation.NonNull
        public byte[] getData() {
            if (mData == null) {
                // this should never happen as mData is initialized in
                // JNI after construction of the KeyRequest object. The check
                // is needed here to guarantee @NonNull annotation.
                throw new java.lang.RuntimeException("KeyRequest is not initialized");
            }
            return mData;
        }

        /**
         * Get the default URL to use when sending the key request message to a
         * server, if known.  The app may prefer to use a different license
         * server URL from other sources.
         * This method returns an empty string if the default URL is not known.
         */
        @android.annotation.NonNull
        public java.lang.String getDefaultUrl() {
            if (mDefaultUrl == null) {
                // this should never happen as mDefaultUrl is initialized in
                // JNI after construction of the KeyRequest object. The check
                // is needed here to guarantee @NonNull annotation.
                throw new java.lang.RuntimeException("KeyRequest is not initialized");
            }
            return mDefaultUrl;
        }

        /**
         * Get the type of the request
         *
         * @return one of {@link #REQUEST_TYPE_INITIAL},
        {@link #REQUEST_TYPE_RENEWAL} or {@link #REQUEST_TYPE_RELEASE}
         */
        @android.media.MediaDrm.KeyRequest.RequestType
        public int getRequestType() {
            return mRequestType;
        }
    }

    /**
     * A key request/response exchange occurs between the app and a license server
     * to obtain or release keys used to decrypt encrypted content.
     * <p>
     * getKeyRequest() is used to obtain an opaque key request byte array that is
     * delivered to the license server.  The opaque key request byte array is returned
     * in KeyRequest.data.  The recommended URL to deliver the key request to is
     * returned in KeyRequest.defaultUrl.
     * <p>
     * After the app has received the key request response from the server,
     * it should deliver to the response to the DRM engine plugin using the method
     * {@link #provideKeyResponse}.
     *
     * @param scope
     * 		may be a sessionId or a keySetId, depending on the specified keyType.
     * 		When the keyType is KEY_TYPE_STREAMING or KEY_TYPE_OFFLINE,
     * 		scope should be set to the sessionId the keys will be provided to.  When the keyType
     * 		is KEY_TYPE_RELEASE, scope should be set to the keySetId of the keys
     * 		being released. Releasing keys from a device invalidates them for all sessions.
     * @param init
     * 		container-specific data, its meaning is interpreted based on the
     * 		mime type provided in the mimeType parameter.  It could contain, for example,
     * 		the content ID, key ID or other data obtained from the content metadata that is
     * 		required in generating the key request. init may be null when keyType is
     * 		KEY_TYPE_RELEASE.
     * @param mimeType
     * 		identifies the mime type of the content
     * @param keyType
     * 		specifes the type of the request. The request may be to acquire
     * 		keys for streaming or offline content, or to release previously acquired
     * 		keys, which are identified by a keySetId.
     * @param optionalParameters
     * 		are included in the key request message to
     * 		allow a client application to provide additional message parameters to the server.
     * 		This may be {@code null} if no additional parameters are to be sent.
     * @throws NotProvisionedException
     * 		if reprovisioning is needed, due to a
     * 		problem with the certifcate
     */
    @android.annotation.NonNull
    public native android.media.MediaDrm.KeyRequest getKeyRequest(@android.annotation.NonNull
    byte[] scope, @android.annotation.Nullable
    byte[] init, @android.annotation.Nullable
    java.lang.String mimeType, @android.media.MediaDrm.KeyType
    int keyType, @android.annotation.Nullable
    java.util.HashMap<java.lang.String, java.lang.String> optionalParameters) throws android.media.NotProvisionedException;

    /**
     * A key response is received from the license server by the app, then it is
     * provided to the DRM engine plugin using provideKeyResponse.  When the
     * response is for an offline key request, a keySetId is returned that can be
     * used to later restore the keys to a new session with the method
     * {@link #restoreKeys}.
     * When the response is for a streaming or release request, null is returned.
     *
     * @param scope
     * 		may be a sessionId or keySetId depending on the type of the
     * 		response.  Scope should be set to the sessionId when the response is for either
     * 		streaming or offline key requests.  Scope should be set to the keySetId when
     * 		the response is for a release request.
     * @param response
     * 		the byte array response from the server
     * @throws NotProvisionedException
     * 		if the response indicates that
     * 		reprovisioning is required
     * @throws DeniedByServerException
     * 		if the response indicates that the
     * 		server rejected the request
     */
    @android.annotation.Nullable
    public native byte[] provideKeyResponse(@android.annotation.NonNull
    byte[] scope, @android.annotation.NonNull
    byte[] response) throws android.media.DeniedByServerException, android.media.NotProvisionedException;

    /**
     * Restore persisted offline keys into a new session.  keySetId identifies the
     * keys to load, obtained from a prior call to {@link #provideKeyResponse}.
     *
     * @param sessionId
     * 		the session ID for the DRM session
     * @param keySetId
     * 		identifies the saved key set to restore
     */
    public native void restoreKeys(@android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    byte[] keySetId);

    /**
     * Remove the current keys from a session.
     *
     * @param sessionId
     * 		the session ID for the DRM session
     */
    public native void removeKeys(@android.annotation.NonNull
    byte[] sessionId);

    /**
     * Request an informative description of the key status for the session.  The status is
     * in the form of {name, value} pairs.  Since DRM license policies vary by vendor,
     * the specific status field names are determined by each DRM vendor.  Refer to your
     * DRM provider documentation for definitions of the field names for a particular
     * DRM engine plugin.
     *
     * @param sessionId
     * 		the session ID for the DRM session
     */
    @android.annotation.NonNull
    public native java.util.HashMap<java.lang.String, java.lang.String> queryKeyStatus(@android.annotation.NonNull
    byte[] sessionId);

    /**
     * Contains the opaque data an app uses to request a certificate from a provisioning
     * server
     */
    public static final class ProvisionRequest {
        ProvisionRequest() {
        }

        /**
         * Get the opaque message data
         */
        @android.annotation.NonNull
        public byte[] getData() {
            if (mData == null) {
                // this should never happen as mData is initialized in
                // JNI after construction of the KeyRequest object. The check
                // is needed here to guarantee @NonNull annotation.
                throw new java.lang.RuntimeException("ProvisionRequest is not initialized");
            }
            return mData;
        }

        /**
         * Get the default URL to use when sending the provision request
         * message to a server, if known. The app may prefer to use a different
         * provisioning server URL obtained from other sources.
         * This method returns an empty string if the default URL is not known.
         */
        @android.annotation.NonNull
        public java.lang.String getDefaultUrl() {
            if (mDefaultUrl == null) {
                // this should never happen as mDefaultUrl is initialized in
                // JNI after construction of the ProvisionRequest object. The check
                // is needed here to guarantee @NonNull annotation.
                throw new java.lang.RuntimeException("ProvisionRequest is not initialized");
            }
            return mDefaultUrl;
        }

        private byte[] mData;

        private java.lang.String mDefaultUrl;
    }

    /**
     * A provision request/response exchange occurs between the app and a provisioning
     * server to retrieve a device certificate.  If provisionining is required, the
     * EVENT_PROVISION_REQUIRED event will be sent to the event handler.
     * getProvisionRequest is used to obtain the opaque provision request byte array that
     * should be delivered to the provisioning server. The provision request byte array
     * is returned in ProvisionRequest.data. The recommended URL to deliver the provision
     * request to is returned in ProvisionRequest.defaultUrl.
     */
    @android.annotation.NonNull
    public android.media.MediaDrm.ProvisionRequest getProvisionRequest() {
        return getProvisionRequestNative(android.media.MediaDrm.CERTIFICATE_TYPE_NONE, "");
    }

    @android.annotation.NonNull
    private native android.media.MediaDrm.ProvisionRequest getProvisionRequestNative(int certType, @android.annotation.NonNull
    java.lang.String certAuthority);

    /**
     * After a provision response is received by the app, it is provided to the DRM
     * engine plugin using this method.
     *
     * @param response
     * 		the opaque provisioning response byte array to provide to the
     * 		DRM engine plugin.
     * @throws DeniedByServerException
     * 		if the response indicates that the
     * 		server rejected the request
     */
    public void provideProvisionResponse(@android.annotation.NonNull
    byte[] response) throws android.media.DeniedByServerException {
        provideProvisionResponseNative(response);
    }

    /* could there be a valid response with 0-sized certificate or key? */
    @android.annotation.NonNull
    private native android.media.MediaDrm.Certificate provideProvisionResponseNative(@android.annotation.NonNull
    byte[] response) throws android.media.DeniedByServerException;

    /**
     * A means of enforcing limits on the number of concurrent streams per subscriber
     * across devices is provided via SecureStop. This is achieved by securely
     * monitoring the lifetime of sessions.
     * <p>
     * Information from the server related to the current playback session is written
     * to persistent storage on the device when each MediaCrypto object is created.
     * <p>
     * In the normal case, playback will be completed, the session destroyed and the
     * Secure Stops will be queried. The app queries secure stops and forwards the
     * secure stop message to the server which verifies the signature and notifies the
     * server side database that the session destruction has been confirmed. The persisted
     * record on the client is only removed after positive confirmation that the server
     * received the message using releaseSecureStops().
     */
    @android.annotation.NonNull
    public native java.util.List<byte[]> getSecureStops();

    /**
     * Access secure stop by secure stop ID.
     *
     * @param ssid
     * 		- The secure stop ID provided by the license server.
     */
    @android.annotation.NonNull
    public native byte[] getSecureStop(@android.annotation.NonNull
    byte[] ssid);

    /**
     * Process the SecureStop server response message ssRelease.  After authenticating
     * the message, remove the SecureStops identified in the response.
     *
     * @param ssRelease
     * 		the server response indicating which secure stops to release
     */
    public native void releaseSecureStops(@android.annotation.NonNull
    byte[] ssRelease);

    /**
     * Remove all secure stops without requiring interaction with the server.
     */
    public native void releaseAllSecureStops();

    /**
     * String property name: identifies the maker of the DRM engine plugin
     */
    public static final java.lang.String PROPERTY_VENDOR = "vendor";

    /**
     * String property name: identifies the version of the DRM engine plugin
     */
    public static final java.lang.String PROPERTY_VERSION = "version";

    /**
     * String property name: describes the DRM engine plugin
     */
    public static final java.lang.String PROPERTY_DESCRIPTION = "description";

    /**
     * String property name: a comma-separated list of cipher and mac algorithms
     * supported by CryptoSession.  The list may be empty if the DRM engine
     * plugin does not support CryptoSession operations.
     */
    public static final java.lang.String PROPERTY_ALGORITHMS = "algorithms";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.StringDef({ android.media.MediaDrm.PROPERTY_VENDOR, android.media.MediaDrm.PROPERTY_VERSION, android.media.MediaDrm.PROPERTY_DESCRIPTION, android.media.MediaDrm.PROPERTY_ALGORITHMS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface StringProperty {}

    /**
     * Read a DRM engine plugin String property value, given the property name string.
     * <p>
     * Standard fields names are:
     * {@link #PROPERTY_VENDOR}, {@link #PROPERTY_VERSION},
     * {@link #PROPERTY_DESCRIPTION}, {@link #PROPERTY_ALGORITHMS}
     */
    /* FIXME this throws IllegalStateException for invalid property names */
    @android.annotation.NonNull
    public native java.lang.String getPropertyString(@android.annotation.NonNull
    @android.media.MediaDrm.StringProperty
    java.lang.String propertyName);

    /**
     * Byte array property name: the device unique identifier is established during
     * device provisioning and provides a means of uniquely identifying each device.
     */
    /* FIXME this throws IllegalStateException for invalid property names */
    public static final java.lang.String PROPERTY_DEVICE_UNIQUE_ID = "deviceUniqueId";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.StringDef({ android.media.MediaDrm.PROPERTY_DEVICE_UNIQUE_ID })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ArrayProperty {}

    /**
     * Read a DRM engine plugin byte array property value, given the property name string.
     * <p>
     * Standard fields names are {@link #PROPERTY_DEVICE_UNIQUE_ID}
     */
    @android.annotation.NonNull
    public native byte[] getPropertyByteArray(@android.media.MediaDrm.ArrayProperty
    java.lang.String propertyName);

    /**
     * Set a DRM engine plugin String property value.
     */
    public native void setPropertyString(@android.media.MediaDrm.StringProperty
    java.lang.String propertyName, @android.annotation.NonNull
    java.lang.String value);

    /**
     * Set a DRM engine plugin byte array property value.
     */
    public native void setPropertyByteArray(@android.media.MediaDrm.ArrayProperty
    java.lang.String propertyName, @android.annotation.NonNull
    byte[] value);

    private static final native void setCipherAlgorithmNative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    java.lang.String algorithm);

    private static final native void setMacAlgorithmNative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    java.lang.String algorithm);

    @android.annotation.NonNull
    private static final native byte[] encryptNative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    byte[] keyId, @android.annotation.NonNull
    byte[] input, @android.annotation.NonNull
    byte[] iv);

    @android.annotation.NonNull
    private static final native byte[] decryptNative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    byte[] keyId, @android.annotation.NonNull
    byte[] input, @android.annotation.NonNull
    byte[] iv);

    @android.annotation.NonNull
    private static final native byte[] signNative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    byte[] keyId, @android.annotation.NonNull
    byte[] message);

    private static final native boolean verifyNative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    byte[] keyId, @android.annotation.NonNull
    byte[] message, @android.annotation.NonNull
    byte[] signature);

    /**
     * In addition to supporting decryption of DASH Common Encrypted Media, the
     * MediaDrm APIs provide the ability to securely deliver session keys from
     * an operator's session key server to a client device, based on the factory-installed
     * root of trust, and then perform encrypt, decrypt, sign and verify operations
     * with the session key on arbitrary user data.
     * <p>
     * The CryptoSession class implements generic encrypt/decrypt/sign/verify methods
     * based on the established session keys.  These keys are exchanged using the
     * getKeyRequest/provideKeyResponse methods.
     * <p>
     * Applications of this capability could include securing various types of
     * purchased or private content, such as applications, books and other media,
     * photos or media delivery protocols.
     * <p>
     * Operators can create session key servers that are functionally similar to a
     * license key server, except that instead of receiving license key requests and
     * providing encrypted content keys which are used specifically to decrypt A/V media
     * content, the session key server receives session key requests and provides
     * encrypted session keys which can be used for general purpose crypto operations.
     * <p>
     * A CryptoSession is obtained using {@link #getCryptoSession}
     */
    public final class CryptoSession {
        private byte[] mSessionId;

        CryptoSession(@android.annotation.NonNull
        byte[] sessionId, @android.annotation.NonNull
        java.lang.String cipherAlgorithm, @android.annotation.NonNull
        java.lang.String macAlgorithm) {
            mSessionId = sessionId;
            android.media.MediaDrm.setCipherAlgorithmNative(android.media.MediaDrm.this, sessionId, cipherAlgorithm);
            android.media.MediaDrm.setMacAlgorithmNative(android.media.MediaDrm.this, sessionId, macAlgorithm);
        }

        /**
         * Encrypt data using the CryptoSession's cipher algorithm
         *
         * @param keyid
         * 		specifies which key to use
         * @param input
         * 		the data to encrypt
         * @param iv
         * 		the initialization vector to use for the cipher
         */
        @android.annotation.NonNull
        public byte[] encrypt(@android.annotation.NonNull
        byte[] keyid, @android.annotation.NonNull
        byte[] input, @android.annotation.NonNull
        byte[] iv) {
            return android.media.MediaDrm.encryptNative(android.media.MediaDrm.this, mSessionId, keyid, input, iv);
        }

        /**
         * Decrypt data using the CryptoSessions's cipher algorithm
         *
         * @param keyid
         * 		specifies which key to use
         * @param input
         * 		the data to encrypt
         * @param iv
         * 		the initialization vector to use for the cipher
         */
        @android.annotation.NonNull
        public byte[] decrypt(@android.annotation.NonNull
        byte[] keyid, @android.annotation.NonNull
        byte[] input, @android.annotation.NonNull
        byte[] iv) {
            return android.media.MediaDrm.decryptNative(android.media.MediaDrm.this, mSessionId, keyid, input, iv);
        }

        /**
         * Sign data using the CryptoSessions's mac algorithm.
         *
         * @param keyid
         * 		specifies which key to use
         * @param message
         * 		the data for which a signature is to be computed
         */
        @android.annotation.NonNull
        public byte[] sign(@android.annotation.NonNull
        byte[] keyid, @android.annotation.NonNull
        byte[] message) {
            return android.media.MediaDrm.signNative(android.media.MediaDrm.this, mSessionId, keyid, message);
        }

        /**
         * Verify a signature using the CryptoSessions's mac algorithm. Return true
         * if the signatures match, false if they do no.
         *
         * @param keyid
         * 		specifies which key to use
         * @param message
         * 		the data to verify
         * @param signature
         * 		the reference signature which will be compared with the
         * 		computed signature
         */
        public boolean verify(@android.annotation.NonNull
        byte[] keyid, @android.annotation.NonNull
        byte[] message, @android.annotation.NonNull
        byte[] signature) {
            return android.media.MediaDrm.verifyNative(android.media.MediaDrm.this, mSessionId, keyid, message, signature);
        }
    }

    /**
     * Obtain a CryptoSession object which can be used to encrypt, decrypt,
     * sign and verify messages or data using the session keys established
     * for the session using methods {@link #getKeyRequest} and
     * {@link #provideKeyResponse} using a session key server.
     *
     * @param sessionId
     * 		the session ID for the session containing keys
     * 		to be used for encrypt, decrypt, sign and/or verify
     * @param cipherAlgorithm
     * 		the algorithm to use for encryption and
     * 		decryption ciphers. The algorithm string conforms to JCA Standard
     * 		Names for Cipher Transforms and is case insensitive.  For example
     * 		"AES/CBC/NoPadding".
     * @param macAlgorithm
     * 		the algorithm to use for sign and verify
     * 		The algorithm string conforms to JCA Standard Names for Mac
     * 		Algorithms and is case insensitive.  For example "HmacSHA256".
     * 		<p>
     * 		The list of supported algorithms for a DRM engine plugin can be obtained
     * 		using the method {@link #getPropertyString} with the property name
     * 		"algorithms".
     */
    public android.media.MediaDrm.CryptoSession getCryptoSession(@android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    java.lang.String cipherAlgorithm, @android.annotation.NonNull
    java.lang.String macAlgorithm) {
        return new android.media.MediaDrm.CryptoSession(sessionId, cipherAlgorithm, macAlgorithm);
    }

    /**
     * Contains the opaque data an app uses to request a certificate from a provisioning
     * server
     *
     * @unknown - not part of the public API at this time
     */
    public static final class CertificateRequest {
        private byte[] mData;

        private java.lang.String mDefaultUrl;

        CertificateRequest(@android.annotation.NonNull
        byte[] data, @android.annotation.NonNull
        java.lang.String defaultUrl) {
            mData = data;
            mDefaultUrl = defaultUrl;
        }

        /**
         * Get the opaque message data
         */
        @android.annotation.NonNull
        public byte[] getData() {
            return mData;
        }

        /**
         * Get the default URL to use when sending the certificate request
         * message to a server, if known. The app may prefer to use a different
         * certificate server URL obtained from other sources.
         */
        @android.annotation.NonNull
        public java.lang.String getDefaultUrl() {
            return mDefaultUrl;
        }
    }

    /**
     * Generate a certificate request, specifying the certificate type
     * and authority. The response received should be passed to
     * provideCertificateResponse.
     *
     * @param certType
     * 		Specifies the certificate type.
     * @param certAuthority
     * 		is passed to the certificate server to specify
     * 		the chain of authority.
     * @unknown - not part of the public API at this time
     */
    @android.annotation.NonNull
    public android.media.MediaDrm.CertificateRequest getCertificateRequest(@android.media.MediaDrm.CertificateType
    int certType, @android.annotation.NonNull
    java.lang.String certAuthority) {
        android.media.MediaDrm.ProvisionRequest provisionRequest = getProvisionRequestNative(certType, certAuthority);
        return new android.media.MediaDrm.CertificateRequest(provisionRequest.getData(), provisionRequest.getDefaultUrl());
    }

    /**
     * Contains the wrapped private key and public certificate data associated
     * with a certificate.
     *
     * @unknown - not part of the public API at this time
     */
    public static final class Certificate {
        Certificate() {
        }

        /**
         * Get the wrapped private key data
         */
        @android.annotation.NonNull
        public byte[] getWrappedPrivateKey() {
            if (mWrappedKey == null) {
                // this should never happen as mWrappedKey is initialized in
                // JNI after construction of the KeyRequest object. The check
                // is needed here to guarantee @NonNull annotation.
                throw new java.lang.RuntimeException("Cerfificate is not initialized");
            }
            return mWrappedKey;
        }

        /**
         * Get the PEM-encoded certificate chain
         */
        @android.annotation.NonNull
        public byte[] getContent() {
            if (mCertificateData == null) {
                // this should never happen as mCertificateData is initialized in
                // JNI after construction of the KeyRequest object. The check
                // is needed here to guarantee @NonNull annotation.
                throw new java.lang.RuntimeException("Cerfificate is not initialized");
            }
            return mCertificateData;
        }

        private byte[] mWrappedKey;

        private byte[] mCertificateData;
    }

    /**
     * Process a response from the certificate server.  The response
     * is obtained from an HTTP Post to the url provided by getCertificateRequest.
     * <p>
     * The public X509 certificate chain and wrapped private key are returned
     * in the returned Certificate objec.  The certificate chain is in PEM format.
     * The wrapped private key should be stored in application private
     * storage, and used when invoking the signRSA method.
     *
     * @param response
     * 		the opaque certificate response byte array to provide to the
     * 		DRM engine plugin.
     * @throws DeniedByServerException
     * 		if the response indicates that the
     * 		server rejected the request
     * @unknown - not part of the public API at this time
     */
    @android.annotation.NonNull
    public android.media.MediaDrm.Certificate provideCertificateResponse(@android.annotation.NonNull
    byte[] response) throws android.media.DeniedByServerException {
        return provideProvisionResponseNative(response);
    }

    @android.annotation.NonNull
    private static final native byte[] signRSANative(@android.annotation.NonNull
    android.media.MediaDrm drm, @android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    java.lang.String algorithm, @android.annotation.NonNull
    byte[] wrappedKey, @android.annotation.NonNull
    byte[] message);

    /**
     * Sign data using an RSA key
     *
     * @param sessionId
     * 		a sessionId obtained from openSession on the MediaDrm object
     * @param algorithm
     * 		the signing algorithm to use, e.g. "PKCS1-BlockType1"
     * @param wrappedKey
     * 		- the wrapped (encrypted) RSA private key obtained
     * 		from provideCertificateResponse
     * @param message
     * 		the data for which a signature is to be computed
     * @unknown - not part of the public API at this time
     */
    @android.annotation.NonNull
    public byte[] signRSA(@android.annotation.NonNull
    byte[] sessionId, @android.annotation.NonNull
    java.lang.String algorithm, @android.annotation.NonNull
    byte[] wrappedKey, @android.annotation.NonNull
    byte[] message) {
        return android.media.MediaDrm.signRSANative(this, sessionId, algorithm, wrappedKey, message);
    }

    @java.lang.Override
    protected void finalize() {
        native_finalize();
    }

    public final native void release();

    private static final native void native_init();

    private final native void native_setup(java.lang.Object mediadrm_this, byte[] uuid);

    private final native void native_finalize();

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaDrm.native_init();
    }
}

