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
 * A base class for tag technologies that are built on top of transceive().
 */
abstract class BasicTagTechnology implements android.nfc.tech.TagTechnology {
    private static final java.lang.String TAG = "NFC";

    final android.nfc.Tag mTag;

    boolean mIsConnected;

    int mSelectedTechnology;

    BasicTagTechnology(android.nfc.Tag tag, int tech) throws android.os.RemoteException {
        mTag = tag;
        mSelectedTechnology = tech;
    }

    @java.lang.Override
    public android.nfc.Tag getTag() {
        return mTag;
    }

    /**
     * Internal helper to throw IllegalStateException if the technology isn't connected
     */
    void checkConnected() {
        if ((mTag.getConnectedTechnology() != mSelectedTechnology) || (mTag.getConnectedTechnology() == (-1))) {
            throw new java.lang.IllegalStateException("Call connect() first!");
        }
    }

    @java.lang.Override
    public boolean isConnected() {
        if (!mIsConnected) {
            return false;
        }
        try {
            return mTag.getTagService().isPresent(mTag.getServiceHandle());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.BasicTagTechnology.TAG, "NFC service dead", e);
            return false;
        }
    }

    @java.lang.Override
    public void connect() throws java.io.IOException {
        try {
            int errorCode = mTag.getTagService().connect(mTag.getServiceHandle(), mSelectedTechnology);
            if (errorCode == android.nfc.ErrorCodes.SUCCESS) {
                // Store this in the tag object
                mTag.setConnectedTechnology(mSelectedTechnology);
                mIsConnected = true;
            } else
                if (errorCode == android.nfc.ErrorCodes.ERROR_NOT_SUPPORTED) {
                    throw new java.lang.UnsupportedOperationException("Connecting to " + ("this technology is not supported by the NFC " + "adapter."));
                } else {
                    throw new java.io.IOException();
                }

        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.BasicTagTechnology.TAG, "NFC service dead", e);
            throw new java.io.IOException("NFC service died");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void reconnect() throws java.io.IOException {
        if (!mIsConnected) {
            throw new java.lang.IllegalStateException("Technology not connected yet");
        }
        try {
            int errorCode = mTag.getTagService().reconnect(mTag.getServiceHandle());
            if (errorCode != android.nfc.ErrorCodes.SUCCESS) {
                mIsConnected = false;
                mTag.setTechnologyDisconnected();
                throw new java.io.IOException();
            }
        } catch (android.os.RemoteException e) {
            mIsConnected = false;
            mTag.setTechnologyDisconnected();
            android.util.Log.e(android.nfc.tech.BasicTagTechnology.TAG, "NFC service dead", e);
            throw new java.io.IOException("NFC service died");
        }
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        try {
            /* Note that we don't want to physically disconnect the tag,
            but just reconnect to it to reset its state
             */
            mTag.getTagService().resetTimeouts();
            mTag.getTagService().reconnect(mTag.getServiceHandle());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.BasicTagTechnology.TAG, "NFC service dead", e);
        } finally {
            mIsConnected = false;
            mTag.setTechnologyDisconnected();
        }
    }

    /**
     * Internal getMaxTransceiveLength()
     */
    int getMaxTransceiveLengthInternal() {
        try {
            return mTag.getTagService().getMaxTransceiveLength(mSelectedTechnology);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.BasicTagTechnology.TAG, "NFC service dead", e);
            return 0;
        }
    }

    /**
     * Internal transceive
     */
    byte[] transceive(byte[] data, boolean raw) throws java.io.IOException {
        checkConnected();
        try {
            android.nfc.TransceiveResult result = mTag.getTagService().transceive(mTag.getServiceHandle(), data, raw);
            if (result == null) {
                throw new java.io.IOException("transceive failed");
            } else {
                return result.getResponseOrThrow();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.nfc.tech.BasicTagTechnology.TAG, "NFC service dead", e);
            throw new java.io.IOException("NFC service died");
        }
    }
}

