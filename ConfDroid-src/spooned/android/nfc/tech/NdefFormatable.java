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
 * Provide access to NDEF format operations on a {@link Tag}.
 *
 * <p>Acquire a {@link NdefFormatable} object using {@link #get}.
 *
 * <p>Android devices with NFC must only enumerate and implement this
 * class for tags for which it can format to NDEF.
 *
 * <p>Unfortunately the procedures to convert unformated tags to NDEF formatted
 * tags are not specified by NFC Forum, and are not generally well-known. So
 * there is no mandatory set of tags for which all Android devices with NFC
 * must support {@link NdefFormatable}.
 *
 * <p class="note"><strong>Note:</strong> Methods that perform I/O operations
 * require the {@link android.Manifest.permission#NFC} permission.
 */
public final class NdefFormatable extends android.nfc.tech.BasicTagTechnology {
    private static final java.lang.String TAG = "NFC";

    /**
     * Get an instance of {@link NdefFormatable} for the given tag.
     * <p>Does not cause any RF activity and does not block.
     * <p>Returns null if {@link NdefFormatable} was not enumerated in {@link Tag#getTechList}.
     * This indicates the tag is not NDEF formatable by this Android device.
     *
     * @param tag
     * 		an NDEF formatable tag
     * @return NDEF formatable object
     */
    public static android.nfc.tech.NdefFormatable get(android.nfc.Tag tag) {
        if (!tag.hasTech(android.nfc.tech.TagTechnology.NDEF_FORMATABLE))
            return null;

        try {
            return new android.nfc.tech.NdefFormatable(tag);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    /**
     * Internal constructor, to be used by NfcAdapter
     *
     * @unknown 
     */
    public NdefFormatable(android.nfc.Tag tag) throws android.os.RemoteException {
        super(tag, android.nfc.tech.TagTechnology.NDEF_FORMATABLE);
    }

    /**
     * Format a tag as NDEF, and write a {@link NdefMessage}.
     *
     * <p>This is a multi-step process, an IOException is thrown
     * if any one step fails.
     * <p>The card is left in a read-write state after this operation.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param firstMessage
     * 		the NDEF message to write after formatting, can be null
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     * @throws FormatException
     * 		if the NDEF Message to write is malformed
     */
    public void format(android.nfc.NdefMessage firstMessage) throws android.nfc.FormatException, java.io.IOException {
        format(firstMessage, false);
    }

    /**
     * Formats a tag as NDEF, write a {@link NdefMessage}, and make read-only.
     *
     * <p>This is a multi-step process, an IOException is thrown
     * if any one step fails.
     * <p>The card is left in a read-only state if this method returns successfully.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param firstMessage
     * 		the NDEF message to write after formatting
     * @throws TagLostException
     * 		if the tag leaves the field
     * @throws IOException
     * 		if there is an I/O failure, or the operation is canceled
     * @throws FormatException
     * 		if the NDEF Message to write is malformed
     */
    public void formatReadOnly(android.nfc.NdefMessage firstMessage) throws android.nfc.FormatException, java.io.IOException {
        format(firstMessage, true);
    }

    /* package */
    void format(android.nfc.NdefMessage firstMessage, boolean makeReadOnly) throws android.nfc.FormatException, java.io.IOException {
        checkConnected();
        try {
            int serviceHandle = mTag.getServiceHandle();
            android.nfc.INfcTag tagService = mTag.getTagService();
            int errorCode = tagService.formatNdef(serviceHandle, android.nfc.tech.MifareClassic.KEY_DEFAULT);
            switch (errorCode) {
                case android.nfc.ErrorCodes.SUCCESS :
                    break;
                case android.nfc.ErrorCodes.ERROR_IO :
                    throw new java.io.IOException();
                case android.nfc.ErrorCodes.ERROR_INVALID_PARAM :
                    throw new android.nfc.FormatException();
                default :
                    // Should not happen
                    throw new java.io.IOException();
            }
            // Now check and see if the format worked
            if (!tagService.isNdef(serviceHandle)) {
                throw new java.io.IOException();
            }
            // Write a message, if one was provided
            if (firstMessage != null) {
                errorCode = tagService.ndefWrite(serviceHandle, firstMessage);
                switch (errorCode) {
                    case android.nfc.ErrorCodes.SUCCESS :
                        break;
                    case android.nfc.ErrorCodes.ERROR_IO :
                        throw new java.io.IOException();
                    case android.nfc.ErrorCodes.ERROR_INVALID_PARAM :
                        throw new android.nfc.FormatException();
                    default :
                        // Should not happen
                        throw new java.io.IOException();
                }
            }
            // optionally make read-only
            if (makeReadOnly) {
                errorCode = tagService.ndefMakeReadOnly(serviceHandle);
                switch (errorCode) {
                    case android.nfc.ErrorCodes.SUCCESS :
                        break;
                    case android.nfc.ErrorCodes.ERROR_IO :
                        throw new java.io.IOException();
                    case android.nfc.ErrorCodes.ERROR_INVALID_PARAM :
                        throw new java.io.IOException();
                    default :
                        // Should not happen
                        throw new java.io.IOException();
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.NdefFormatable.TAG, "NFC service dead", e);
        }
    }
}

