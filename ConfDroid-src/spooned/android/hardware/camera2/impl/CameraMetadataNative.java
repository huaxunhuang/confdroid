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
package android.hardware.camera2.impl;


/**
 * Implementation of camera metadata marshal/unmarshal across Binder to
 * the camera service
 */
public class CameraMetadataNative implements android.os.Parcelable {
    public static class Key<T> {
        private boolean mHasTag;

        private int mTag;

        private final java.lang.Class<T> mType;

        private final android.hardware.camera2.utils.TypeReference<T> mTypeReference;

        private final java.lang.String mName;

        private final int mHash;

        /**
         * Visible for testing only.
         *
         * <p>Use the CameraCharacteristics.Key, CaptureResult.Key, or CaptureRequest.Key
         * for application code or vendor-extended keys.</p>
         */
        public Key(java.lang.String name, java.lang.Class<T> type) {
            if (name == null) {
                throw new java.lang.NullPointerException("Key needs a valid name");
            } else
                if (type == null) {
                    throw new java.lang.NullPointerException("Type needs to be non-null");
                }

            mName = name;
            mType = type;
            mTypeReference = android.hardware.camera2.utils.TypeReference.createSpecializedTypeReference(type);
            mHash = mName.hashCode() ^ mTypeReference.hashCode();
        }

        /**
         * Visible for testing only.
         *
         * <p>Use the CameraCharacteristics.Key, CaptureResult.Key, or CaptureRequest.Key
         * for application code or vendor-extended keys.</p>
         */
        @java.lang.SuppressWarnings("unchecked")
        public Key(java.lang.String name, android.hardware.camera2.utils.TypeReference<T> typeReference) {
            if (name == null) {
                throw new java.lang.NullPointerException("Key needs a valid name");
            } else
                if (typeReference == null) {
                    throw new java.lang.NullPointerException("TypeReference needs to be non-null");
                }

            mName = name;
            mType = ((java.lang.Class<T>) (typeReference.getRawType()));
            mTypeReference = typeReference;
            mHash = mName.hashCode() ^ mTypeReference.hashCode();
        }

        /**
         * Return a camelCase, period separated name formatted like:
         * {@code "root.section[.subsections].name"}.
         *
         * <p>Built-in keys exposed by the Android SDK are always prefixed with {@code "android."};
         * keys that are device/platform-specific are prefixed with {@code "com."}.</p>
         *
         * <p>For example, {@code CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP} would
         * have a name of {@code "android.scaler.streamConfigurationMap"}; whereas a device
         * specific key might look like {@code "com.google.nexus.data.private"}.</p>
         *
         * @return String representation of the key name
         */
        public final java.lang.String getName() {
            return mName;
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public final int hashCode() {
            return mHash;
        }

        /**
         * Compare this key against other native keys, request keys, result keys, and
         * characteristics keys.
         *
         * <p>Two keys are considered equal if their name and type reference are equal.</p>
         *
         * <p>Note that the equality against non-native keys is one-way. A native key may be equal
         * to a result key; but that same result key will not be equal to a native key.</p>
         */
        @java.lang.SuppressWarnings("rawtypes")
        @java.lang.Override
        public final boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (this.hashCode() != o.hashCode())) {
                return false;
            }
            android.hardware.camera2.impl.CameraMetadataNative.Key<?> lhs;
            if (o instanceof android.hardware.camera2.CaptureResult.Key) {
                lhs = ((android.hardware.camera2.CaptureResult.Key) (o)).getNativeKey();
            } else
                if (o instanceof android.hardware.camera2.CaptureRequest.Key) {
                    lhs = ((android.hardware.camera2.CaptureRequest.Key) (o)).getNativeKey();
                } else
                    if (o instanceof android.hardware.camera2.CameraCharacteristics.Key) {
                        lhs = ((android.hardware.camera2.CameraCharacteristics.Key) (o)).getNativeKey();
                    } else
                        if (o instanceof android.hardware.camera2.impl.CameraMetadataNative.Key) {
                            lhs = ((android.hardware.camera2.impl.CameraMetadataNative.Key<?>) (o));
                        } else {
                            return false;
                        }



            return mName.equals(lhs.mName) && mTypeReference.equals(lhs.mTypeReference);
        }

        /**
         * <p>
         * Get the tag corresponding to this key. This enables insertion into the
         * native metadata.
         * </p>
         *
         * <p>This value is looked up the first time, and cached subsequently.</p>
         *
         * @return The tag numeric value corresponding to the string
         */
        public final int getTag() {
            if (!mHasTag) {
                mTag = android.hardware.camera2.impl.CameraMetadataNative.getTag(mName);
                mHasTag = true;
            }
            return mTag;
        }

        /**
         * Get the raw class backing the type {@code T} for this key.
         *
         * <p>The distinction is only important if {@code T} is a generic, e.g.
         * {@code Range<Integer>} since the nested type will be erased.</p>
         */
        public final java.lang.Class<T> getType() {
            // TODO: remove this; other places should use #getTypeReference() instead
            return mType;
        }

        /**
         * Get the type reference backing the type {@code T} for this key.
         *
         * <p>The distinction is only important if {@code T} is a generic, e.g.
         * {@code Range<Integer>} since the nested type will be retained.</p>
         */
        public final android.hardware.camera2.utils.TypeReference<T> getTypeReference() {
            return mTypeReference;
        }
    }

    private static final java.lang.String TAG = "CameraMetadataJV";

    private static final boolean DEBUG = false;

    // this should be in sync with HAL_PIXEL_FORMAT_BLOB defined in graphics.h
    public static final int NATIVE_JPEG_FORMAT = 0x21;

    private static final java.lang.String CELLID_PROCESS = "CELLID";

    private static final java.lang.String GPS_PROCESS = "GPS";

    private static final int FACE_LANDMARK_SIZE = 6;

    private static java.lang.String translateLocationProviderToProcess(final java.lang.String provider) {
        if (provider == null) {
            return null;
        }
        switch (provider) {
            case android.location.LocationManager.GPS_PROVIDER :
                return android.hardware.camera2.impl.CameraMetadataNative.GPS_PROCESS;
            case android.location.LocationManager.NETWORK_PROVIDER :
                return android.hardware.camera2.impl.CameraMetadataNative.CELLID_PROCESS;
            default :
                return null;
        }
    }

    private static java.lang.String translateProcessToLocationProvider(final java.lang.String process) {
        if (process == null) {
            return null;
        }
        switch (process) {
            case android.hardware.camera2.impl.CameraMetadataNative.GPS_PROCESS :
                return android.location.LocationManager.GPS_PROVIDER;
            case android.hardware.camera2.impl.CameraMetadataNative.CELLID_PROCESS :
                return android.location.LocationManager.NETWORK_PROVIDER;
            default :
                return null;
        }
    }

    public CameraMetadataNative() {
        super();
        mMetadataPtr = nativeAllocate();
        if (mMetadataPtr == 0) {
            throw new java.lang.OutOfMemoryError("Failed to allocate native CameraMetadata");
        }
    }

    /**
     * Copy constructor - clone metadata
     */
    public CameraMetadataNative(android.hardware.camera2.impl.CameraMetadataNative other) {
        super();
        mMetadataPtr = nativeAllocateCopy(other);
        if (mMetadataPtr == 0) {
            throw new java.lang.OutOfMemoryError("Failed to allocate native CameraMetadata");
        }
    }

    /**
     * Move the contents from {@code other} into a new camera metadata instance.</p>
     *
     * <p>After this call, {@code other} will become empty.</p>
     *
     * @param other
     * 		the previous metadata instance which will get pilfered
     * @return a new metadata instance with the values from {@code other} moved into it
     */
    public static android.hardware.camera2.impl.CameraMetadataNative move(android.hardware.camera2.impl.CameraMetadataNative other) {
        android.hardware.camera2.impl.CameraMetadataNative newObject = new android.hardware.camera2.impl.CameraMetadataNative();
        newObject.swap(other);
        return newObject;
    }

    public static final android.os.Parcelable.Creator<android.hardware.camera2.impl.CameraMetadataNative> CREATOR = new android.os.Parcelable.Creator<android.hardware.camera2.impl.CameraMetadataNative>() {
        @java.lang.Override
        public android.hardware.camera2.impl.CameraMetadataNative createFromParcel(android.os.Parcel in) {
            android.hardware.camera2.impl.CameraMetadataNative metadata = new android.hardware.camera2.impl.CameraMetadataNative();
            metadata.readFromParcel(in);
            return metadata;
        }

        @java.lang.Override
        public android.hardware.camera2.impl.CameraMetadataNative[] newArray(int size) {
            return new android.hardware.camera2.impl.CameraMetadataNative[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        nativeWriteToParcel(dest);
    }

    /**
     *
     *
     * @unknown 
     */
    public <T> T get(android.hardware.camera2.CameraCharacteristics.Key<T> key) {
        return get(key.getNativeKey());
    }

    /**
     *
     *
     * @unknown 
     */
    public <T> T get(android.hardware.camera2.CaptureResult.Key<T> key) {
        return get(key.getNativeKey());
    }

    /**
     *
     *
     * @unknown 
     */
    public <T> T get(android.hardware.camera2.CaptureRequest.Key<T> key) {
        return get(key.getNativeKey());
    }

    /**
     * Look-up a metadata field value by its key.
     *
     * @param key
     * 		a non-{@code null} key instance
     * @return the field corresponding to the {@code key}, or {@code null} if no value was set
     */
    public <T> T get(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
        com.android.internal.util.Preconditions.checkNotNull(key, "key must not be null");
        // Check if key has been overridden to use a wrapper class on the java side.
        android.hardware.camera2.impl.GetCommand g = android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.get(key);
        if (g != null) {
            return g.getValue(this, key);
        }
        return getBase(key);
    }

    public void readFromParcel(android.os.Parcel in) {
        nativeReadFromParcel(in);
    }

    /**
     * Set the global client-side vendor tag descriptor to allow use of vendor
     * tags in camera applications.
     *
     * @throws ServiceSpecificException
     * 		
     * @unknown 
     */
    public static void setupGlobalVendorTagDescriptor() throws android.os.ServiceSpecificException {
        int err = android.hardware.camera2.impl.CameraMetadataNative.nativeSetupGlobalVendorTagDescriptor();
        if (err != 0) {
            throw new android.os.ServiceSpecificException(err, "Failure to set up global vendor tags");
        }
    }

    /**
     * Set the global client-side vendor tag descriptor to allow use of vendor
     * tags in camera applications.
     *
     * @return int An error code corresponding to one of the
    {@link ICameraService} error constants, or 0 on success.
     */
    private static native int nativeSetupGlobalVendorTagDescriptor();

    /**
     * Set a camera metadata field to a value. The field definitions can be
     * found in {@link CameraCharacteristics}, {@link CaptureResult}, and
     * {@link CaptureRequest}.
     *
     * @param key
     * 		The metadata field to write.
     * @param value
     * 		The value to set the field to, which must be of a matching
     * 		type to the key.
     */
    public <T> void set(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key, T value) {
        android.hardware.camera2.impl.SetCommand s = android.hardware.camera2.impl.CameraMetadataNative.sSetCommandMap.get(key);
        if (s != null) {
            s.setValue(this, value);
            return;
        }
        setBase(key, value);
    }

    public <T> void set(android.hardware.camera2.CaptureRequest.Key<T> key, T value) {
        set(key.getNativeKey(), value);
    }

    public <T> void set(android.hardware.camera2.CaptureResult.Key<T> key, T value) {
        set(key.getNativeKey(), value);
    }

    public <T> void set(android.hardware.camera2.CameraCharacteristics.Key<T> key, T value) {
        set(key.getNativeKey(), value);
    }

    // Keep up-to-date with camera_metadata.h
    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_BYTE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT32 = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_FLOAT = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_INT64 = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_DOUBLE = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_RATIONAL = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int NUM_TYPES = 6;

    private void close() {
        // this sets mMetadataPtr to 0
        nativeClose();
        mMetadataPtr = 0;// set it to 0 again to prevent eclipse from making this field final

    }

    private <T> T getBase(android.hardware.camera2.CameraCharacteristics.Key<T> key) {
        return getBase(key.getNativeKey());
    }

    private <T> T getBase(android.hardware.camera2.CaptureResult.Key<T> key) {
        return getBase(key.getNativeKey());
    }

    private <T> T getBase(android.hardware.camera2.CaptureRequest.Key<T> key) {
        return getBase(key.getNativeKey());
    }

    private <T> T getBase(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
        int tag = key.getTag();
        byte[] values = readValues(tag);
        if (values == null) {
            return null;
        }
        android.hardware.camera2.marshal.Marshaler<T> marshaler = android.hardware.camera2.impl.CameraMetadataNative.getMarshalerForKey(key);
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(values).order(java.nio.ByteOrder.nativeOrder());
        return marshaler.unmarshal(buffer);
    }

    // Use Command pattern here to avoid lots of expensive if/equals checks in get for overridden
    // metadata.
    private static final java.util.HashMap<android.hardware.camera2.impl.CameraMetadataNative.Key<?>, android.hardware.camera2.impl.GetCommand> sGetCommandMap = new java.util.HashMap<android.hardware.camera2.impl.CameraMetadataNative.Key<?>, android.hardware.camera2.impl.GetCommand>();

    static {
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getAvailableFormats()));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CaptureResult.STATISTICS_FACES.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getFaces()));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getFaceRectangles()));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getStreamConfigurationMap()));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS_AE.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getMaxRegions(key)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS_AWB.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getMaxRegions(key)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS_AF.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getMaxRegions(key)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getMaxNumOutputs(key)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getMaxNumOutputs(key)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getMaxNumOutputs(key)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getTonemapCurve()));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getGpsLocation()));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sGetCommandMap.put(android.hardware.camera2.CaptureResult.STATISTICS_LENS_SHADING_CORRECTION_MAP.getNativeKey(), new android.hardware.camera2.impl.GetCommand() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public <T> T getValue(android.hardware.camera2.impl.CameraMetadataNative metadata, android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
                return ((T) (metadata.getLensShadingMap()));
            }
        });
    }

    private int[] getAvailableFormats() {
        int[] availableFormats = getBase(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_FORMATS);
        if (availableFormats != null) {
            for (int i = 0; i < availableFormats.length; i++) {
                // JPEG has different value between native and managed side, need override.
                if (availableFormats[i] == android.hardware.camera2.impl.CameraMetadataNative.NATIVE_JPEG_FORMAT) {
                    availableFormats[i] = android.graphics.ImageFormat.JPEG;
                }
            }
        }
        return availableFormats;
    }

    private boolean setFaces(android.hardware.camera2.params.Face[] faces) {
        if (faces == null) {
            return false;
        }
        int numFaces = faces.length;
        // Detect if all faces are SIMPLE or not; count # of valid faces
        boolean fullMode = true;
        for (android.hardware.camera2.params.Face face : faces) {
            if (face == null) {
                numFaces--;
                android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "setFaces - null face detected, skipping");
                continue;
            }
            if (face.getId() == android.hardware.camera2.params.Face.ID_UNSUPPORTED) {
                fullMode = false;
            }
        }
        android.graphics.Rect[] faceRectangles = new android.graphics.Rect[numFaces];
        byte[] faceScores = new byte[numFaces];
        int[] faceIds = null;
        int[] faceLandmarks = null;
        if (fullMode) {
            faceIds = new int[numFaces];
            faceLandmarks = new int[numFaces * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE];
        }
        int i = 0;
        for (android.hardware.camera2.params.Face face : faces) {
            if (face == null) {
                continue;
            }
            faceRectangles[i] = face.getBounds();
            faceScores[i] = ((byte) (face.getScore()));
            if (fullMode) {
                faceIds[i] = face.getId();
                int j = 0;
                faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + (j++)] = face.getLeftEyePosition().x;
                faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + (j++)] = face.getLeftEyePosition().y;
                faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + (j++)] = face.getRightEyePosition().x;
                faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + (j++)] = face.getRightEyePosition().y;
                faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + (j++)] = face.getMouthPosition().x;
                faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + (j++)] = face.getMouthPosition().y;
            }
            i++;
        }
        set(android.hardware.camera2.CaptureResult.STATISTICS_FACE_RECTANGLES, faceRectangles);
        set(android.hardware.camera2.CaptureResult.STATISTICS_FACE_IDS, faceIds);
        set(android.hardware.camera2.CaptureResult.STATISTICS_FACE_LANDMARKS, faceLandmarks);
        set(android.hardware.camera2.CaptureResult.STATISTICS_FACE_SCORES, faceScores);
        return true;
    }

    private android.hardware.camera2.params.Face[] getFaces() {
        java.lang.Integer faceDetectMode = get(android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE);
        byte[] faceScores = get(android.hardware.camera2.CaptureResult.STATISTICS_FACE_SCORES);
        android.graphics.Rect[] faceRectangles = get(android.hardware.camera2.CaptureResult.STATISTICS_FACE_RECTANGLES);
        int[] faceIds = get(android.hardware.camera2.CaptureResult.STATISTICS_FACE_IDS);
        int[] faceLandmarks = get(android.hardware.camera2.CaptureResult.STATISTICS_FACE_LANDMARKS);
        if (android.hardware.camera2.impl.CameraMetadataNative.areValuesAllNull(faceDetectMode, faceScores, faceRectangles, faceIds, faceLandmarks)) {
            return null;
        }
        if (faceDetectMode == null) {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Face detect mode metadata is null, assuming the mode is SIMPLE");
            faceDetectMode = android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_SIMPLE;
        } else {
            if (faceDetectMode == android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_OFF) {
                return new android.hardware.camera2.params.Face[0];
            }
            if ((faceDetectMode != android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_SIMPLE) && (faceDetectMode != android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_FULL)) {
                android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Unknown face detect mode: " + faceDetectMode);
                return new android.hardware.camera2.params.Face[0];
            }
        }
        // Face scores and rectangles are required by SIMPLE and FULL mode.
        if ((faceScores == null) || (faceRectangles == null)) {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Expect face scores and rectangles to be non-null");
            return new android.hardware.camera2.params.Face[0];
        } else
            if (faceScores.length != faceRectangles.length) {
                android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, java.lang.String.format("Face score size(%d) doesn match face rectangle size(%d)!", faceScores.length, faceRectangles.length));
            }

        // To be safe, make number of faces is the minimal of all face info metadata length.
        int numFaces = java.lang.Math.min(faceScores.length, faceRectangles.length);
        // Face id and landmarks are only required by FULL mode.
        if (faceDetectMode == android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_FULL) {
            if ((faceIds == null) || (faceLandmarks == null)) {
                android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Expect face ids and landmarks to be non-null for FULL mode," + "fallback to SIMPLE mode");
                faceDetectMode = android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_SIMPLE;
            } else {
                if ((faceIds.length != numFaces) || (faceLandmarks.length != (numFaces * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE))) {
                    android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, java.lang.String.format("Face id size(%d), or face landmark size(%d) don't" + "match face number(%d)!", faceIds.length, faceLandmarks.length * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE, numFaces));
                }
                // To be safe, make number of faces is the minimal of all face info metadata length.
                numFaces = java.lang.Math.min(numFaces, faceIds.length);
                numFaces = java.lang.Math.min(numFaces, faceLandmarks.length / android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE);
            }
        }
        java.util.ArrayList<android.hardware.camera2.params.Face> faceList = new java.util.ArrayList<android.hardware.camera2.params.Face>();
        if (faceDetectMode == android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE_SIMPLE) {
            for (int i = 0; i < numFaces; i++) {
                if ((faceScores[i] <= android.hardware.camera2.params.Face.SCORE_MAX) && (faceScores[i] >= android.hardware.camera2.params.Face.SCORE_MIN)) {
                    faceList.add(new android.hardware.camera2.params.Face(faceRectangles[i], faceScores[i]));
                }
            }
        } else {
            // CaptureResult.STATISTICS_FACE_DETECT_MODE_FULL
            for (int i = 0; i < numFaces; i++) {
                if (((faceScores[i] <= android.hardware.camera2.params.Face.SCORE_MAX) && (faceScores[i] >= android.hardware.camera2.params.Face.SCORE_MIN)) && (faceIds[i] >= 0)) {
                    android.graphics.Point leftEye = new android.graphics.Point(faceLandmarks[i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE], faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + 1]);
                    android.graphics.Point rightEye = new android.graphics.Point(faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + 2], faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + 3]);
                    android.graphics.Point mouth = new android.graphics.Point(faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + 4], faceLandmarks[(i * android.hardware.camera2.impl.CameraMetadataNative.FACE_LANDMARK_SIZE) + 5]);
                    android.hardware.camera2.params.Face face = new android.hardware.camera2.params.Face(faceRectangles[i], faceScores[i], faceIds[i], leftEye, rightEye, mouth);
                    faceList.add(face);
                }
            }
        }
        android.hardware.camera2.params.Face[] faces = new android.hardware.camera2.params.Face[faceList.size()];
        faceList.toArray(faces);
        return faces;
    }

    // Face rectangles are defined as (left, top, right, bottom) instead of
    // (left, top, width, height) at the native level, so the normal Rect
    // conversion that does (l, t, w, h) -> (l, t, r, b) is unnecessary. Undo
    // that conversion here for just the faces.
    private android.graphics.Rect[] getFaceRectangles() {
        android.graphics.Rect[] faceRectangles = getBase(android.hardware.camera2.CaptureResult.STATISTICS_FACE_RECTANGLES);
        if (faceRectangles == null)
            return null;

        android.graphics.Rect[] fixedFaceRectangles = new android.graphics.Rect[faceRectangles.length];
        for (int i = 0; i < faceRectangles.length; i++) {
            fixedFaceRectangles[i] = new android.graphics.Rect(faceRectangles[i].left, faceRectangles[i].top, faceRectangles[i].right - faceRectangles[i].left, faceRectangles[i].bottom - faceRectangles[i].top);
        }
        return fixedFaceRectangles;
    }

    private android.hardware.camera2.params.LensShadingMap getLensShadingMap() {
        float[] lsmArray = getBase(android.hardware.camera2.CaptureResult.STATISTICS_LENS_SHADING_MAP);
        android.util.Size s = get(android.hardware.camera2.CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE);
        // Do not warn if lsmArray is null while s is not. This is valid.
        if (lsmArray == null) {
            return null;
        }
        if (s == null) {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "getLensShadingMap - Lens shading map size was null.");
            return null;
        }
        android.hardware.camera2.params.LensShadingMap map = new android.hardware.camera2.params.LensShadingMap(lsmArray, s.getHeight(), s.getWidth());
        return map;
    }

    private android.location.Location getGpsLocation() {
        java.lang.String processingMethod = get(android.hardware.camera2.CaptureResult.JPEG_GPS_PROCESSING_METHOD);
        double[] coords = get(android.hardware.camera2.CaptureResult.JPEG_GPS_COORDINATES);
        java.lang.Long timeStamp = get(android.hardware.camera2.CaptureResult.JPEG_GPS_TIMESTAMP);
        if (android.hardware.camera2.impl.CameraMetadataNative.areValuesAllNull(processingMethod, coords, timeStamp)) {
            return null;
        }
        android.location.Location l = new android.location.Location(android.hardware.camera2.impl.CameraMetadataNative.translateProcessToLocationProvider(processingMethod));
        if (timeStamp != null) {
            l.setTime(timeStamp);
        } else {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "getGpsLocation - No timestamp for GPS location.");
        }
        if (coords != null) {
            l.setLatitude(coords[0]);
            l.setLongitude(coords[1]);
            l.setAltitude(coords[2]);
        } else {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "getGpsLocation - No coordinates for GPS location");
        }
        return l;
    }

    private boolean setGpsLocation(android.location.Location l) {
        if (l == null) {
            return false;
        }
        double[] coords = new double[]{ l.getLatitude(), l.getLongitude(), l.getAltitude() };
        java.lang.String processMethod = android.hardware.camera2.impl.CameraMetadataNative.translateLocationProviderToProcess(l.getProvider());
        long timestamp = l.getTime();
        set(android.hardware.camera2.CaptureRequest.JPEG_GPS_TIMESTAMP, timestamp);
        set(android.hardware.camera2.CaptureRequest.JPEG_GPS_COORDINATES, coords);
        if (processMethod == null) {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "setGpsLocation - No process method, Location is not from a GPS or NETWORK" + "provider");
        } else {
            setBase(android.hardware.camera2.CaptureRequest.JPEG_GPS_PROCESSING_METHOD, processMethod);
        }
        return true;
    }

    private android.hardware.camera2.params.StreamConfigurationMap getStreamConfigurationMap() {
        android.hardware.camera2.params.StreamConfiguration[] configurations = getBase(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS);
        android.hardware.camera2.params.StreamConfigurationDuration[] minFrameDurations = getBase(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS);
        android.hardware.camera2.params.StreamConfigurationDuration[] stallDurations = getBase(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS);
        android.hardware.camera2.params.StreamConfiguration[] depthConfigurations = getBase(android.hardware.camera2.CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS);
        android.hardware.camera2.params.StreamConfigurationDuration[] depthMinFrameDurations = getBase(android.hardware.camera2.CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS);
        android.hardware.camera2.params.StreamConfigurationDuration[] depthStallDurations = getBase(android.hardware.camera2.CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS);
        android.hardware.camera2.params.HighSpeedVideoConfiguration[] highSpeedVideoConfigurations = getBase(android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS);
        android.hardware.camera2.params.ReprocessFormatsMap inputOutputFormatsMap = getBase(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP);
        int[] capabilities = getBase(android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        boolean listHighResolution = false;
        for (int capability : capabilities) {
            if (capability == android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BURST_CAPTURE) {
                listHighResolution = true;
                break;
            }
        }
        return new android.hardware.camera2.params.StreamConfigurationMap(configurations, minFrameDurations, stallDurations, depthConfigurations, depthMinFrameDurations, depthStallDurations, highSpeedVideoConfigurations, inputOutputFormatsMap, listHighResolution);
    }

    private <T> java.lang.Integer getMaxRegions(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
        final int AE = 0;
        final int AWB = 1;
        final int AF = 2;
        // The order of the elements is: (AE, AWB, AF)
        int[] maxRegions = getBase(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS);
        if (maxRegions == null) {
            return null;
        }
        if (key.equals(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS_AE)) {
            return maxRegions[AE];
        } else
            if (key.equals(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS_AWB)) {
                return maxRegions[AWB];
            } else
                if (key.equals(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS_AF)) {
                    return maxRegions[AF];
                } else {
                    throw new java.lang.AssertionError("Invalid key " + key);
                }


    }

    private <T> java.lang.Integer getMaxNumOutputs(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
        final int RAW = 0;
        final int PROC = 1;
        final int PROC_STALLING = 2;
        // The order of the elements is: (raw, proc+nonstalling, proc+stalling)
        int[] maxNumOutputs = getBase(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS);
        if (maxNumOutputs == null) {
            return null;
        }
        if (key.equals(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW)) {
            return maxNumOutputs[RAW];
        } else
            if (key.equals(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC)) {
                return maxNumOutputs[PROC];
            } else
                if (key.equals(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING)) {
                    return maxNumOutputs[PROC_STALLING];
                } else {
                    throw new java.lang.AssertionError("Invalid key " + key);
                }


    }

    private <T> android.hardware.camera2.params.TonemapCurve getTonemapCurve() {
        float[] red = getBase(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE_RED);
        float[] green = getBase(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE_GREEN);
        float[] blue = getBase(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE_BLUE);
        if (android.hardware.camera2.impl.CameraMetadataNative.areValuesAllNull(red, green, blue)) {
            return null;
        }
        if (((red == null) || (green == null)) || (blue == null)) {
            android.util.Log.w(android.hardware.camera2.impl.CameraMetadataNative.TAG, "getTonemapCurve - missing tone curve components");
            return null;
        }
        android.hardware.camera2.params.TonemapCurve tc = new android.hardware.camera2.params.TonemapCurve(red, green, blue);
        return tc;
    }

    private <T> void setBase(android.hardware.camera2.CameraCharacteristics.Key<T> key, T value) {
        setBase(key.getNativeKey(), value);
    }

    private <T> void setBase(android.hardware.camera2.CaptureResult.Key<T> key, T value) {
        setBase(key.getNativeKey(), value);
    }

    private <T> void setBase(android.hardware.camera2.CaptureRequest.Key<T> key, T value) {
        setBase(key.getNativeKey(), value);
    }

    private <T> void setBase(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key, T value) {
        int tag = key.getTag();
        if (value == null) {
            // Erase the entry
            /* src */
            writeValues(tag, null);
            return;
        }// else update the entry to a new value

        android.hardware.camera2.marshal.Marshaler<T> marshaler = android.hardware.camera2.impl.CameraMetadataNative.getMarshalerForKey(key);
        int size = marshaler.calculateMarshalSize(value);
        // TODO: Optimization. Cache the byte[] and reuse if the size is big enough.
        byte[] values = new byte[size];
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(values).order(java.nio.ByteOrder.nativeOrder());
        marshaler.marshal(value, buffer);
        writeValues(tag, values);
    }

    // Use Command pattern here to avoid lots of expensive if/equals checks in get for overridden
    // metadata.
    private static final java.util.HashMap<android.hardware.camera2.impl.CameraMetadataNative.Key<?>, android.hardware.camera2.impl.SetCommand> sSetCommandMap = new java.util.HashMap<android.hardware.camera2.impl.CameraMetadataNative.Key<?>, android.hardware.camera2.impl.SetCommand>();

    static {
        android.hardware.camera2.impl.CameraMetadataNative.sSetCommandMap.put(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new android.hardware.camera2.impl.SetCommand() {
            @java.lang.Override
            public <T> void setValue(android.hardware.camera2.impl.CameraMetadataNative metadata, T value) {
                metadata.setAvailableFormats(((int[]) (value)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sSetCommandMap.put(android.hardware.camera2.CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new android.hardware.camera2.impl.SetCommand() {
            @java.lang.Override
            public <T> void setValue(android.hardware.camera2.impl.CameraMetadataNative metadata, T value) {
                metadata.setFaceRectangles(((android.graphics.Rect[]) (value)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sSetCommandMap.put(android.hardware.camera2.CaptureResult.STATISTICS_FACES.getNativeKey(), new android.hardware.camera2.impl.SetCommand() {
            @java.lang.Override
            public <T> void setValue(android.hardware.camera2.impl.CameraMetadataNative metadata, T value) {
                metadata.setFaces(((android.hardware.camera2.params.Face[]) (value)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sSetCommandMap.put(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE.getNativeKey(), new android.hardware.camera2.impl.SetCommand() {
            @java.lang.Override
            public <T> void setValue(android.hardware.camera2.impl.CameraMetadataNative metadata, T value) {
                metadata.setTonemapCurve(((android.hardware.camera2.params.TonemapCurve) (value)));
            }
        });
        android.hardware.camera2.impl.CameraMetadataNative.sSetCommandMap.put(android.hardware.camera2.CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new android.hardware.camera2.impl.SetCommand() {
            @java.lang.Override
            public <T> void setValue(android.hardware.camera2.impl.CameraMetadataNative metadata, T value) {
                metadata.setGpsLocation(((android.location.Location) (value)));
            }
        });
    }

    private boolean setAvailableFormats(int[] value) {
        int[] availableFormat = value;
        if (value == null) {
            // Let setBase() to handle the null value case.
            return false;
        }
        int[] newValues = new int[availableFormat.length];
        for (int i = 0; i < availableFormat.length; i++) {
            newValues[i] = availableFormat[i];
            if (availableFormat[i] == android.graphics.ImageFormat.JPEG) {
                newValues[i] = android.hardware.camera2.impl.CameraMetadataNative.NATIVE_JPEG_FORMAT;
            }
        }
        setBase(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_FORMATS, newValues);
        return true;
    }

    /**
     * Convert Face Rectangles from managed side to native side as they have different definitions.
     * <p>
     * Managed side face rectangles are defined as: left, top, width, height.
     * Native side face rectangles are defined as: left, top, right, bottom.
     * The input face rectangle need to be converted to native side definition when set is called.
     * </p>
     *
     * @param faceRects
     * 		Input face rectangles.
     * @return true if face rectangles can be set successfully. Otherwise, Let the caller
    (setBase) to handle it appropriately.
     */
    private boolean setFaceRectangles(android.graphics.Rect[] faceRects) {
        if (faceRects == null) {
            return false;
        }
        android.graphics.Rect[] newFaceRects = new android.graphics.Rect[faceRects.length];
        for (int i = 0; i < newFaceRects.length; i++) {
            newFaceRects[i] = new android.graphics.Rect(faceRects[i].left, faceRects[i].top, faceRects[i].right + faceRects[i].left, faceRects[i].bottom + faceRects[i].top);
        }
        setBase(android.hardware.camera2.CaptureResult.STATISTICS_FACE_RECTANGLES, newFaceRects);
        return true;
    }

    private <T> boolean setTonemapCurve(android.hardware.camera2.params.TonemapCurve tc) {
        if (tc == null) {
            return false;
        }
        float[][] curve = new float[3][];
        for (int i = android.hardware.camera2.params.TonemapCurve.CHANNEL_RED; i <= android.hardware.camera2.params.TonemapCurve.CHANNEL_BLUE; i++) {
            int pointCount = tc.getPointCount(i);
            curve[i] = new float[pointCount * android.hardware.camera2.params.TonemapCurve.POINT_SIZE];
            tc.copyColorCurve(i, curve[i], 0);
        }
        setBase(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE_RED, curve[0]);
        setBase(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE_GREEN, curve[1]);
        setBase(android.hardware.camera2.CaptureRequest.TONEMAP_CURVE_BLUE, curve[2]);
        return true;
    }

    private long mMetadataPtr;// native CameraMetadata*


    private native long nativeAllocate();

    private native long nativeAllocateCopy(android.hardware.camera2.impl.CameraMetadataNative other) throws java.lang.NullPointerException;

    private native synchronized void nativeWriteToParcel(android.os.Parcel dest);

    private native synchronized void nativeReadFromParcel(android.os.Parcel source);

    private native synchronized void nativeSwap(android.hardware.camera2.impl.CameraMetadataNative other) throws java.lang.NullPointerException;

    private native synchronized void nativeClose();

    private native synchronized boolean nativeIsEmpty();

    private native synchronized int nativeGetEntryCount();

    private native synchronized byte[] nativeReadValues(int tag);

    private native synchronized void nativeWriteValues(int tag, byte[] src);

    private native synchronized void nativeDump() throws java.io.IOException;// dump to ALOGD


    private static native java.util.ArrayList nativeGetAllVendorKeys(java.lang.Class keyClass);

    private static native int nativeGetTagFromKey(java.lang.String keyName) throws java.lang.IllegalArgumentException;

    private static native int nativeGetTypeFromTag(int tag) throws java.lang.IllegalArgumentException;

    private static native void nativeClassInit();

    /**
     * <p>Perform a 0-copy swap of the internal metadata with another object.</p>
     *
     * <p>Useful to convert a CameraMetadata into e.g. a CaptureRequest.</p>
     *
     * @param other
     * 		Metadata to swap with
     * @throws NullPointerException
     * 		if other was null
     * @unknown 
     */
    public void swap(android.hardware.camera2.impl.CameraMetadataNative other) {
        nativeSwap(other);
    }

    /**
     *
     *
     * @unknown 
     */
    public int getEntryCount() {
        return nativeGetEntryCount();
    }

    /**
     * Does this metadata contain at least 1 entry?
     *
     * @unknown 
     */
    public boolean isEmpty() {
        return nativeIsEmpty();
    }

    /**
     * Return a list containing keys of the given key class for all defined vendor tags.
     *
     * @unknown 
     */
    public static <K> java.util.ArrayList<K> getAllVendorKeys(java.lang.Class<K> keyClass) {
        if (keyClass == null) {
            throw new java.lang.NullPointerException();
        }
        return ((java.util.ArrayList<K>) (android.hardware.camera2.impl.CameraMetadataNative.nativeGetAllVendorKeys(keyClass)));
    }

    /**
     * Convert a key string into the equivalent native tag.
     *
     * @throws IllegalArgumentException
     * 		if the key was not recognized
     * @throws NullPointerException
     * 		if the key was null
     * @unknown 
     */
    public static int getTag(java.lang.String key) {
        return android.hardware.camera2.impl.CameraMetadataNative.nativeGetTagFromKey(key);
    }

    /**
     * Get the underlying native type for a tag.
     *
     * @param tag
     * 		An integer tag, see e.g. {@link #getTag}
     * @return An int enum for the metadata type, see e.g. {@link #TYPE_BYTE}
     * @unknown 
     */
    public static int getNativeType(int tag) {
        return android.hardware.camera2.impl.CameraMetadataNative.nativeGetTypeFromTag(tag);
    }

    /**
     * <p>Updates the existing entry for tag with the new bytes pointed by src, erasing
     * the entry if src was null.</p>
     *
     * <p>An empty array can be passed in to update the entry to 0 elements.</p>
     *
     * @param tag
     * 		An integer tag, see e.g. {@link #getTag}
     * @param src
     * 		An array of bytes, or null to erase the entry
     * @unknown 
     */
    public void writeValues(int tag, byte[] src) {
        nativeWriteValues(tag, src);
    }

    /**
     * <p>Returns a byte[] of data corresponding to this tag. Use a wrapped bytebuffer to unserialize
     * the data properly.</p>
     *
     * <p>An empty array can be returned to denote an existing entry with 0 elements.</p>
     *
     * @param tag
     * 		An integer tag, see e.g. {@link #getTag}
     * @return {@code null} if there were 0 entries for this tag, a byte[] otherwise.
     * @unknown 
     */
    public byte[] readValues(int tag) {
        // TODO: Optimization. Native code returns a ByteBuffer instead.
        return nativeReadValues(tag);
    }

    /**
     * Dumps the native metadata contents to logcat.
     *
     * <p>Visibility for testing/debugging only. The results will not
     * include any synthesized keys, as they are invisible to the native layer.</p>
     *
     * @unknown 
     */
    public void dumpToLog() {
        try {
            nativeDump();
        } catch (java.io.IOException e) {
            android.util.Log.wtf(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Dump logging failed", e);
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * Get the marshaler compatible with the {@code key} and type {@code T}.
     *
     * @throws UnsupportedOperationException
     * 		if the native/managed type combination for {@code key} is not supported
     */
    private static <T> android.hardware.camera2.marshal.Marshaler<T> getMarshalerForKey(android.hardware.camera2.impl.CameraMetadataNative.Key<T> key) {
        return android.hardware.camera2.marshal.MarshalRegistry.getMarshaler(key.getTypeReference(), android.hardware.camera2.impl.CameraMetadataNative.getNativeType(key.getTag()));
    }

    @java.lang.SuppressWarnings({ "unchecked", "rawtypes" })
    private static void registerAllMarshalers() {
        if (android.hardware.camera2.impl.CameraMetadataNative.DEBUG) {
            android.util.Log.v(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Shall register metadata marshalers");
        }
        android.hardware.camera2.marshal.MarshalQueryable[] queryList = new android.hardware.camera2.marshal.MarshalQueryable[]{ // marshalers for standard types
        new android.hardware.camera2.marshal.impl.MarshalQueryablePrimitive(), new android.hardware.camera2.marshal.impl.MarshalQueryableEnum(), new android.hardware.camera2.marshal.impl.MarshalQueryableArray(), // pseudo standard types, that expand/narrow the native type into a managed type
        new android.hardware.camera2.marshal.impl.MarshalQueryableBoolean(), new android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger(), // marshalers for custom types
        new android.hardware.camera2.marshal.impl.MarshalQueryableRect(), new android.hardware.camera2.marshal.impl.MarshalQueryableSize(), new android.hardware.camera2.marshal.impl.MarshalQueryableSizeF(), new android.hardware.camera2.marshal.impl.MarshalQueryableString(), new android.hardware.camera2.marshal.impl.MarshalQueryableReprocessFormatsMap(), new android.hardware.camera2.marshal.impl.MarshalQueryableRange(), new android.hardware.camera2.marshal.impl.MarshalQueryablePair(), new android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle(), new android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform(), new android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration(), new android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration(), new android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector(), new android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern(), new android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration(), // generic parcelable marshaler (MUST BE LAST since it has lowest priority)
        new android.hardware.camera2.marshal.impl.MarshalQueryableParcelable() };
        for (android.hardware.camera2.marshal.MarshalQueryable query : queryList) {
            android.hardware.camera2.marshal.MarshalRegistry.registerMarshalQueryable(query);
        }
        if (android.hardware.camera2.impl.CameraMetadataNative.DEBUG) {
            android.util.Log.v(android.hardware.camera2.impl.CameraMetadataNative.TAG, "Registered metadata marshalers");
        }
    }

    /**
     * Check if input arguments are all {@code null}.
     *
     * @param objs
     * 		Input arguments for null check
     * @return {@code true} if input arguments are all {@code null}, otherwise {@code false}
     */
    private static boolean areValuesAllNull(java.lang.Object... objs) {
        for (java.lang.Object o : objs) {
            if (o != null)
                return false;

        }
        return true;
    }

    static {
        /* We use a class initializer to allow the native code to cache some field offsets */
        android.hardware.camera2.impl.CameraMetadataNative.nativeClassInit();
        android.hardware.camera2.impl.CameraMetadataNative.registerAllMarshalers();
    }
}

