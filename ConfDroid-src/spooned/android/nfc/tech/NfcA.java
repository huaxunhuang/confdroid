/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.nfc.tech;


/**
 * Provides access to NFC-A (ISO 14443-3A) properties and I/O operations on a {@link Tag}.
 *
 * <p>Acquire a {@link NfcA} object using {@link #get}.
 * <p>The primary NFC-A I/O operation is {@link #transceive}. Applications must
 * implement their own protocol stack on top of {@link #transceive}.
 *
 * <p class="note"><strong>Note:</strong> Methods that perform I/O operations
 * require the {@link android.Manifest.permission#NFC} permission.
 */
public final class NfcA extends android.nfc.tech.BasicTagTechnology {
    private static final java.lang.String TAG = "NFC";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_SAK = "sak";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_ATQA = "atqa";

    private short mSak;

    private byte[] mAtqa;

    /**
     * Get an instance of {@link NfcA} for the given tag.
     * <p>Returns null if {@link NfcA} was not enumerated in {@link Tag#getTechList}.
     * This indicates the tag does not support NFC-A.
     * <p>Does not cause any RF activity and does not block.
     *
     * @param tag
     * 		an NFC-A compatible tag
     * @return NFC-A object
     */
    public static android.nfc.tech.NfcA get(android.nfc.Tag tag) {
        if (!tag.hasTech(android.nfc.tech.TagTechnology.NFC_A))
            return null;

        try {
            return new android.nfc.tech.NfcA(tag);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public NfcA(android.nfc.Tag tag) throws android.os.RemoteException {
        super(tag, android.nfc.tech.TagTechnology.NFC_A);
        android.os.Bundle extras = tag.getTechExtras(android.nfc.tech.TagTechnology.NFC_A);
        mSak = extras.getShort(android.nfc.tech.NfcA.EXTRA_SAK);
        mAtqa = extras.getByteArray(android.nfc.tech.NfcA.EXTRA_ATQA);
    }

    /**
     * Return the ATQA/SENS_RES bytes from tag discovery.
     *
     * <p>Does not cause any RF activity and does not block.
     *
     * @return ATQA/SENS_RES bytes
     */
    public byte[] getAtqa() {
        return mAtqa;
    }

    /**
     * Return the SAK/SEL_RES bytes from tag discovery.
     *
     * <p>Does not cause any RF activity and does not block.
     *
     * @return SAK bytes
     */
    public short getSak() {
        return mSak;
    }

    /**
     * Send raw NFC-A commands to the tag and receive the response.
     *
     * <p>Applications must not append the EoD (CRC) to the payload,
     * it will be automatically calculated.
     * <p>Applications must only send commands that are complete bytes,
     * for example a SENS_REQ is not possible (these are used to
     * manage tag polling and initialization).
     *
     * <p>Use {@link #getMaxTransceiveLength} to retrieve the maximum number of bytes
     * that can be sent with {@link #transceive}.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param data
     * 		bytes to send
     * @return bytes received in response
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or this operation is canceled
     */
    public byte[] transceive(byte[] data) throws java.io.IOException {
        return transceive(data, true);
    }

    /**
     * Return the maximum number of bytes that can be sent with {@link #transceive}.
     *
     * @return the maximum number of bytes that can be sent with {@link #transceive}.
     */
    public int getMaxTransceiveLength() {
        return getMaxTransceiveLengthInternal();
    }

    /**
     * Set the {@link #transceive} timeout in milliseconds.
     *
     * <p>The timeout only applies to {@link #transceive} on this object,
     * and is reset to a default value when {@link #close} is called.
     *
     * <p>Setting a longer timeout may be useful when performing
     * transactions that require a long processing time on the tag
     * such as key generation.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param timeout
     * 		timeout value in milliseconds
     */
    public void setTimeout(int timeout) {
        try {
            int err = mTag.getTagService().setTimeout(android.nfc.tech.TagTechnology.NFC_A, timeout);
            if (err != android.nfc.ErrorCodes.SUCCESS) {
                throw new java.lang.IllegalArgumentException("The supplied timeout is not valid");
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.NfcA.TAG, "NFC service dead", e);
        }
    }

    /**
     * Get the current {@link #transceive} timeout in milliseconds.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @return timeout value in milliseconds
     */
    public int getTimeout() {
        try {
            return mTag.getTagService().getTimeout(android.nfc.tech.TagTechnology.NFC_A);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.NfcA.TAG, "NFC service dead", e);
            return 0;
        }
    }
}

