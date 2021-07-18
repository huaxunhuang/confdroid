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
 * An entity class that is used to pass information to an online DRM server. An instance of this
 * class is passed to the {@link DrmManagerClient#acquireDrmInfo acquireDrmInfo()} method to get an
 * instance of a {@link DrmInfo}.
 */
public class DrmInfoRequest {
    // Changes in following constants should be in sync with DrmInfoRequest.h
    /**
     * Acquires DRM server registration information.
     */
    public static final int TYPE_REGISTRATION_INFO = 1;

    /**
     * Acquires information for unregistering the DRM server.
     */
    public static final int TYPE_UNREGISTRATION_INFO = 2;

    /**
     * Acquires rights information.
     */
    public static final int TYPE_RIGHTS_ACQUISITION_INFO = 3;

    /**
     * Acquires the progress of the rights acquisition.
     */
    public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 4;

    /**
     * Key that is used to pass the unique session ID for the account or the user.
     */
    public static final java.lang.String ACCOUNT_ID = "account_id";

    /**
     * Key that is used to pass the unique session ID for the subscription.
     */
    public static final java.lang.String SUBSCRIPTION_ID = "subscription_id";

    private final int mInfoType;

    private final java.lang.String mMimeType;

    private final java.util.HashMap<java.lang.String, java.lang.Object> mRequestInformation = new java.util.HashMap<java.lang.String, java.lang.Object>();

    /**
     * Creates a <code>DrmInfoRequest</code> object with type and MIME type.
     *
     * @param infoType
     * 		Type of information.
     * @param mimeType
     * 		MIME type.
     */
    public DrmInfoRequest(int infoType, java.lang.String mimeType) {
        mInfoType = infoType;
        mMimeType = mimeType;
        if (!isValid()) {
            final java.lang.String msg = ((("infoType: " + infoType) + ",") + "mimeType: ") + mimeType;
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    /**
     * Retrieves the MIME type associated with this object.
     *
     * @return The MIME type.
     */
    public java.lang.String getMimeType() {
        return mMimeType;
    }

    /**
     * Retrieves the information type associated with this object.
     *
     * @return The information type.
     */
    public int getInfoType() {
        return mInfoType;
    }

    /**
     * Adds optional information as key-value pairs to this object.
     *
     * @param key
     * 		The key to add.
     * @param value
     * 		The value to add.
     */
    public void put(java.lang.String key, java.lang.Object value) {
        mRequestInformation.put(key, value);
    }

    /**
     * Retrieves the value of a given key.
     *
     * @param key
     * 		The key whose value is being retrieved.
     * @return The value of the key that is being retrieved. Returns null if the key cannot be
    found.
     */
    public java.lang.Object get(java.lang.String key) {
        return mRequestInformation.get(key);
    }

    /**
     * Retrieves an iterator object that you can use to iterate over the keys associated with
     * this <code>DrmInfoRequest</code> object.
     *
     * @return The iterator object.
     */
    public java.util.Iterator<java.lang.String> keyIterator() {
        return mRequestInformation.keySet().iterator();
    }

    /**
     * Retrieves an iterator object that you can use to iterate over the values associated with
     * this <code>DrmInfoRequest</code> object.
     *
     * @return The iterator object.
     */
    public java.util.Iterator<java.lang.Object> iterator() {
        return mRequestInformation.values().iterator();
    }

    /**
     * Returns whether this instance is valid or not
     *
     * @return true if valid
    false if invalid
     */
    boolean isValid() {
        return (((null != mMimeType) && (!mMimeType.equals(""))) && (null != mRequestInformation)) && android.drm.DrmInfoRequest.isValidType(mInfoType);
    }

    /* package */
    static boolean isValidType(int infoType) {
        boolean isValid = false;
        switch (infoType) {
            case android.drm.DrmInfoRequest.TYPE_REGISTRATION_INFO :
            case android.drm.DrmInfoRequest.TYPE_UNREGISTRATION_INFO :
            case android.drm.DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO :
            case android.drm.DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO :
                isValid = true;
                break;
        }
        return isValid;
    }
}

