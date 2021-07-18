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
package android.drm;


/**
 * An entity class that wraps the license information retrieved from the online DRM server.
 * <p>
 * A caller can instantiate a {@link DrmRights} object by first invoking the
 * {@link DrmManagerClient#processDrmInfo(DrmInfo)} method and then using the resulting
 * {@link ProcessedData} object to invoke the {@link DrmRights#DrmRights(ProcessedData, String)}
 * constructor.
 * <p>
 * A caller can also instantiate a {@link DrmRights} object by using the
 * {@link DrmRights#DrmRights(String, String)} constructor, which takes a path to a file
 * containing rights information instead of a <code>ProcessedData</code>.
 * <p>
 * Please note that the account id and subscription id is not mandatory by all DRM agents
 * or plugins. When account id or subscription id is not required by the specific DRM
 * agent or plugin, they can be either null, or an empty string, or any other don't-care
 * string value.
 */
public class DrmRights {
    private byte[] mData;

    private java.lang.String mMimeType;

    private java.lang.String mAccountId;

    private java.lang.String mSubscriptionId;

    /**
     * Creates a <code>DrmRights</code> object with the given parameters.
     *
     * @param rightsFilePath
     * 		Path to the file containing rights information.
     * @param mimeType
     * 		MIME type. Must not be null or an empty string.
     */
    public DrmRights(java.lang.String rightsFilePath, java.lang.String mimeType) {
        java.io.File file = new java.io.File(rightsFilePath);
        instantiate(file, mimeType);
    }

    /**
     * Creates a <code>DrmRights</code> object with the given parameters.
     *
     * @param rightsFilePath
     * 		Path to the file containing rights information.
     * @param mimeType
     * 		MIME type. Must not be null or an empty string.
     * @param accountId
     * 		Account ID of the user.
     */
    public DrmRights(java.lang.String rightsFilePath, java.lang.String mimeType, java.lang.String accountId) {
        this(rightsFilePath, mimeType);
        mAccountId = accountId;
    }

    /**
     * Creates a <code>DrmRights</code> object with the given parameters.
     *
     * @param rightsFilePath
     * 		Path to the file containing rights information.
     * @param mimeType
     * 		MIME type. Must not be null or an empty string.
     * @param accountId
     * 		Account ID of the user.
     * @param subscriptionId
     * 		Subscription ID of the user.
     */
    public DrmRights(java.lang.String rightsFilePath, java.lang.String mimeType, java.lang.String accountId, java.lang.String subscriptionId) {
        this(rightsFilePath, mimeType);
        mAccountId = accountId;
        mSubscriptionId = subscriptionId;
    }

    /**
     * Creates a <code>DrmRights</code> object with the given parameters.
     *
     * @param rightsFile
     * 		File containing rights information.
     * @param mimeType
     * 		MIME type. Must not be null or an empty string.
     */
    public DrmRights(java.io.File rightsFile, java.lang.String mimeType) {
        instantiate(rightsFile, mimeType);
    }

    private void instantiate(java.io.File rightsFile, java.lang.String mimeType) {
        try {
            mData = android.drm.DrmUtils.readBytes(rightsFile);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        mMimeType = mimeType;
        if (!isValid()) {
            final java.lang.String msg = ((("mimeType: " + mMimeType) + ",") + "data: ") + java.util.Arrays.toString(mData);
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    /**
     * Creates a <code>DrmRights</code> object with the given parameters.
     *
     * @param data
     * 		A {@link ProcessedData} object containing rights information.
     * 		Must not be null.
     * @param mimeType
     * 		The MIME type. It must not be null or an empty string.
     */
    public DrmRights(android.drm.ProcessedData data, java.lang.String mimeType) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("data is null");
        }
        mData = data.getData();
        mAccountId = data.getAccountId();
        mSubscriptionId = data.getSubscriptionId();
        mMimeType = mimeType;
        if (!isValid()) {
            final java.lang.String msg = ((("mimeType: " + mMimeType) + ",") + "data: ") + java.util.Arrays.toString(mData);
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    /**
     * Retrieves the rights data associated with this <code>DrmRights</code> object.
     *
     * @return A <code>byte</code> array representing the rights data.
     */
    public byte[] getData() {
        return mData;
    }

    /**
     * Retrieves the MIME type associated with this <code>DrmRights</code> object.
     *
     * @return The MIME type.
     */
    public java.lang.String getMimeType() {
        return mMimeType;
    }

    /**
     * Retrieves the account ID associated with this <code>DrmRights</code> object.
     *
     * @return The account ID.
     */
    public java.lang.String getAccountId() {
        return mAccountId;
    }

    /**
     * Retrieves the subscription ID associated with this <code>DrmRights</code> object.
     *
     * @return The subscription ID.
     */
    public java.lang.String getSubscriptionId() {
        return mSubscriptionId;
    }

    /**
     * Determines whether this instance is valid or not.
     *
     * @return True if valid; false if invalid.
     */
    /* package */
    boolean isValid() {
        return (((null != mMimeType) && (!mMimeType.equals(""))) && (null != mData)) && (mData.length > 0);
    }
}

