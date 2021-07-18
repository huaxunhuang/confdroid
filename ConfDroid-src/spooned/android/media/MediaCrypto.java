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
 * MediaCrypto class can be used in conjunction with {@link android.media.MediaCodec}
 * to decode encrypted media data.
 *
 * Crypto schemes are assigned 16 byte UUIDs,
 * the method {@link #isCryptoSchemeSupported} can be used to query if a given
 * scheme is supported on the device.
 */
public final class MediaCrypto {
    /**
     * Query if the given scheme identified by its UUID is supported on
     * this device.
     *
     * @param uuid
     * 		The UUID of the crypto scheme.
     */
    public static final boolean isCryptoSchemeSupported(@android.annotation.NonNull
    java.util.UUID uuid) {
        return android.media.MediaCrypto.isCryptoSchemeSupportedNative(android.media.MediaCrypto.getByteArrayFromUUID(uuid));
    }

    @android.annotation.NonNull
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
    byte[] uuid);

    /**
     * Instantiate a MediaCrypto object using opaque, crypto scheme specific
     * data.
     *
     * @param uuid
     * 		The UUID of the crypto scheme.
     * @param initData
     * 		Opaque initialization data specific to the crypto scheme.
     */
    public MediaCrypto(@android.annotation.NonNull
    java.util.UUID uuid, @android.annotation.NonNull
    byte[] initData) throws android.media.MediaCryptoException {
        native_setup(android.media.MediaCrypto.getByteArrayFromUUID(uuid), initData);
    }

    /**
     * Query if the crypto scheme requires the use of a secure decoder
     * to decode data of the given mime type.
     *
     * @param mime
     * 		The mime type of the media data
     */
    public final native boolean requiresSecureDecoderComponent(@android.annotation.NonNull
    java.lang.String mime);

    /**
     * Associate a MediaDrm session with this MediaCrypto instance.  The
     * MediaDrm session is used to securely load decryption keys for a
     * crypto scheme.  The crypto keys loaded through the MediaDrm session
     * may be selected for use during the decryption operation performed
     * by {@link android.media.MediaCodec#queueSecureInputBuffer} by specifying
     * their key ids in the {@link android.media.MediaCodec.CryptoInfo#key} field.
     *
     * @param sessionId
     * 		the MediaDrm sessionId to associate with this
     * 		MediaCrypto instance
     * @throws MediaCryptoException
     * 		on failure to set the sessionId
     */
    public final native void setMediaDrmSession(@android.annotation.NonNull
    byte[] sessionId) throws android.media.MediaCryptoException;

    @java.lang.Override
    protected void finalize() {
        native_finalize();
    }

    public final native void release();

    private static final native void native_init();

    private final native void native_setup(@android.annotation.NonNull
    byte[] uuid, @android.annotation.NonNull
    byte[] initData) throws android.media.MediaCryptoException;

    private final native void native_finalize();

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaCrypto.native_init();
    }

    private long mNativeContext;
}

