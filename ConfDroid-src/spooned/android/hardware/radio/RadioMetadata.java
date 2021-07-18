/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.hardware.radio;


/**
 * Contains meta data about a radio program such as station name, song title, artist etc...
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class RadioMetadata implements android.os.Parcelable {
    private static final java.lang.String TAG = "RadioMetadata";

    /**
     * The RDS Program Information.
     */
    public static final java.lang.String METADATA_KEY_RDS_PI = "android.hardware.radio.metadata.RDS_PI";

    /**
     * The RDS Program Service.
     */
    public static final java.lang.String METADATA_KEY_RDS_PS = "android.hardware.radio.metadata.RDS_PS";

    /**
     * The RDS PTY.
     */
    public static final java.lang.String METADATA_KEY_RDS_PTY = "android.hardware.radio.metadata.RDS_PTY";

    /**
     * The RBDS PTY.
     */
    public static final java.lang.String METADATA_KEY_RBDS_PTY = "android.hardware.radio.metadata.RBDS_PTY";

    /**
     * The RBDS Radio Text.
     */
    public static final java.lang.String METADATA_KEY_RDS_RT = "android.hardware.radio.metadata.RDS_RT";

    /**
     * The song title.
     */
    public static final java.lang.String METADATA_KEY_TITLE = "android.hardware.radio.metadata.TITLE";

    /**
     * The artist name.
     */
    public static final java.lang.String METADATA_KEY_ARTIST = "android.hardware.radio.metadata.ARTIST";

    /**
     * The album name.
     */
    public static final java.lang.String METADATA_KEY_ALBUM = "android.hardware.radio.metadata.ALBUM";

    /**
     * The music genre.
     */
    public static final java.lang.String METADATA_KEY_GENRE = "android.hardware.radio.metadata.GENRE";

    /**
     * The radio station icon {@link Bitmap}.
     */
    public static final java.lang.String METADATA_KEY_ICON = "android.hardware.radio.metadata.ICON";

    /**
     * The artwork for the song/album {@link Bitmap}.
     */
    public static final java.lang.String METADATA_KEY_ART = "android.hardware.radio.metadata.ART";

    /**
     * The clock.
     */
    public static final java.lang.String METADATA_KEY_CLOCK = "android.hardware.radio.metadata.CLOCK";

    private static final int METADATA_TYPE_INVALID = -1;

    private static final int METADATA_TYPE_INT = 0;

    private static final int METADATA_TYPE_TEXT = 1;

    private static final int METADATA_TYPE_BITMAP = 2;

    private static final int METADATA_TYPE_CLOCK = 3;

    private static final android.util.ArrayMap<java.lang.String, java.lang.Integer> METADATA_KEYS_TYPE;

    static {
        METADATA_KEYS_TYPE = new android.util.ArrayMap<java.lang.String, java.lang.Integer>();
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_PI, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_PS, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_PTY, android.hardware.radio.RadioMetadata.METADATA_TYPE_INT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_RBDS_PTY, android.hardware.radio.RadioMetadata.METADATA_TYPE_INT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_RT, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_TITLE, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_ARTIST, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_ALBUM, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_GENRE, android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_ICON, android.hardware.radio.RadioMetadata.METADATA_TYPE_BITMAP);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_ART, android.hardware.radio.RadioMetadata.METADATA_TYPE_BITMAP);
        android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.put(android.hardware.radio.RadioMetadata.METADATA_KEY_CLOCK, android.hardware.radio.RadioMetadata.METADATA_TYPE_CLOCK);
    }

    // keep in sync with: system/media/radio/include/system/radio_metadata.h
    private static final int NATIVE_KEY_INVALID = -1;

    private static final int NATIVE_KEY_RDS_PI = 0;

    private static final int NATIVE_KEY_RDS_PS = 1;

    private static final int NATIVE_KEY_RDS_PTY = 2;

    private static final int NATIVE_KEY_RBDS_PTY = 3;

    private static final int NATIVE_KEY_RDS_RT = 4;

    private static final int NATIVE_KEY_TITLE = 5;

    private static final int NATIVE_KEY_ARTIST = 6;

    private static final int NATIVE_KEY_ALBUM = 7;

    private static final int NATIVE_KEY_GENRE = 8;

    private static final int NATIVE_KEY_ICON = 9;

    private static final int NATIVE_KEY_ART = 10;

    private static final int NATIVE_KEY_CLOCK = 11;

    private static final android.util.SparseArray<java.lang.String> NATIVE_KEY_MAPPING;

    static {
        NATIVE_KEY_MAPPING = new android.util.SparseArray<java.lang.String>();
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_RDS_PI, android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_PI);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_RDS_PS, android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_PS);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_RDS_PTY, android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_PTY);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_RBDS_PTY, android.hardware.radio.RadioMetadata.METADATA_KEY_RBDS_PTY);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_RDS_RT, android.hardware.radio.RadioMetadata.METADATA_KEY_RDS_RT);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_TITLE, android.hardware.radio.RadioMetadata.METADATA_KEY_TITLE);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_ARTIST, android.hardware.radio.RadioMetadata.METADATA_KEY_ARTIST);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_ALBUM, android.hardware.radio.RadioMetadata.METADATA_KEY_ALBUM);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_GENRE, android.hardware.radio.RadioMetadata.METADATA_KEY_GENRE);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_ICON, android.hardware.radio.RadioMetadata.METADATA_KEY_ICON);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_ART, android.hardware.radio.RadioMetadata.METADATA_KEY_ART);
        android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.put(android.hardware.radio.RadioMetadata.NATIVE_KEY_CLOCK, android.hardware.radio.RadioMetadata.METADATA_KEY_CLOCK);
    }

    /**
     * Provides a Clock that can be used to describe time as provided by the Radio.
     *
     * The clock is defined by the seconds since epoch at the UTC + 0 timezone
     * and timezone offset from UTC + 0 represented in number of minutes.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class Clock implements android.os.Parcelable {
        private final long mUtcEpochSeconds;

        private final int mTimezoneOffsetMinutes;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeLong(mUtcEpochSeconds);
            out.writeInt(mTimezoneOffsetMinutes);
        }

        public static final android.os.Parcelable.Creator<android.hardware.radio.RadioMetadata.Clock> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioMetadata.Clock>() {
            public android.hardware.radio.RadioMetadata.Clock createFromParcel(android.os.Parcel in) {
                return new android.hardware.radio.RadioMetadata.Clock(in);
            }

            public android.hardware.radio.RadioMetadata.Clock[] newArray(int size) {
                return new android.hardware.radio.RadioMetadata.Clock[size];
            }
        };

        public Clock(long utcEpochSeconds, int timezoneOffsetMinutes) {
            mUtcEpochSeconds = utcEpochSeconds;
            mTimezoneOffsetMinutes = timezoneOffsetMinutes;
        }

        private Clock(android.os.Parcel in) {
            mUtcEpochSeconds = in.readLong();
            mTimezoneOffsetMinutes = in.readInt();
        }

        public long getUtcEpochSeconds() {
            return mUtcEpochSeconds;
        }

        public int getTimezoneOffsetMinutes() {
            return mTimezoneOffsetMinutes;
        }
    }

    private final android.os.Bundle mBundle;

    RadioMetadata() {
        mBundle = new android.os.Bundle();
    }

    private RadioMetadata(android.os.Bundle bundle) {
        mBundle = new android.os.Bundle(bundle);
    }

    private RadioMetadata(android.os.Parcel in) {
        mBundle = in.readBundle();
    }

    /**
     * Returns {@code true} if the given key is contained in the meta data
     *
     * @param key
     * 		a String key
     * @return {@code true} if the key exists in this meta data, {@code false} otherwise
     */
    public boolean containsKey(java.lang.String key) {
        return mBundle.containsKey(key);
    }

    /**
     * Returns the text value associated with the given key as a String, or null
     * if the key is not found in the meta data.
     *
     * @param key
     * 		The key the value is stored under
     * @return a String value, or null
     */
    public java.lang.String getString(java.lang.String key) {
        return mBundle.getString(key);
    }

    /**
     * Returns the value associated with the given key,
     * or 0 if the key is not found in the meta data.
     *
     * @param key
     * 		The key the value is stored under
     * @return an int value
     */
    public int getInt(java.lang.String key) {
        return mBundle.getInt(key, 0);
    }

    /**
     * Returns a {@link Bitmap} for the given key or null if the key is not found in the meta data.
     *
     * @param key
     * 		The key the value is stored under
     * @return a {@link Bitmap} or null
     */
    public android.graphics.Bitmap getBitmap(java.lang.String key) {
        android.graphics.Bitmap bmp = null;
        try {
            bmp = mBundle.getParcelable(key);
        } catch (java.lang.Exception e) {
            // ignore, value was not a bitmap
            android.util.Log.w(android.hardware.radio.RadioMetadata.TAG, "Failed to retrieve a key as Bitmap.", e);
        }
        return bmp;
    }

    public android.hardware.radio.RadioMetadata.Clock getClock(java.lang.String key) {
        android.hardware.radio.RadioMetadata.Clock clock = null;
        try {
            clock = mBundle.getParcelable(key);
        } catch (java.lang.Exception e) {
            // ignore, value was not a clock.
            android.util.Log.w(android.hardware.radio.RadioMetadata.TAG, "Failed to retrieve a key as Clock.", e);
        }
        return clock;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeBundle(mBundle);
    }

    /**
     * Returns the number of fields in this meta data.
     *
     * @return the number of fields in the meta data.
     */
    public int size() {
        return mBundle.size();
    }

    /**
     * Returns a Set containing the Strings used as keys in this meta data.
     *
     * @return a Set of String keys
     */
    public java.util.Set<java.lang.String> keySet() {
        return mBundle.keySet();
    }

    /**
     * Helper for getting the String key used by {@link RadioMetadata} from the
     * corrsponding native integer key.
     *
     * @param editorKey
     * 		The key used by the editor
     * @return the key used by this class or null if no mapping exists
     * @unknown 
     */
    public static java.lang.String getKeyFromNativeKey(int nativeKey) {
        return android.hardware.radio.RadioMetadata.NATIVE_KEY_MAPPING.get(nativeKey, null);
    }

    public static final android.os.Parcelable.Creator<android.hardware.radio.RadioMetadata> CREATOR = new android.os.Parcelable.Creator<android.hardware.radio.RadioMetadata>() {
        @java.lang.Override
        public android.hardware.radio.RadioMetadata createFromParcel(android.os.Parcel in) {
            return new android.hardware.radio.RadioMetadata(in);
        }

        @java.lang.Override
        public android.hardware.radio.RadioMetadata[] newArray(int size) {
            return new android.hardware.radio.RadioMetadata[size];
        }
    };

    /**
     * Use to build RadioMetadata objects.
     */
    public static final class Builder {
        private final android.os.Bundle mBundle;

        /**
         * Create an empty Builder. Any field that should be included in the
         * {@link RadioMetadata} must be added.
         */
        public Builder() {
            mBundle = new android.os.Bundle();
        }

        /**
         * Create a Builder using a {@link RadioMetadata} instance to set the
         * initial values. All fields in the source meta data will be included in
         * the new meta data. Fields can be overwritten by adding the same key.
         *
         * @param source
         * 		
         */
        public Builder(android.hardware.radio.RadioMetadata source) {
            mBundle = new android.os.Bundle(source.mBundle);
        }

        /**
         * Create a Builder using a {@link RadioMetadata} instance to set
         * initial values, but replace bitmaps with a scaled down copy if they
         * are larger than maxBitmapSize.
         *
         * @param source
         * 		The original meta data to copy.
         * @param maxBitmapSize
         * 		The maximum height/width for bitmaps contained
         * 		in the meta data.
         * @unknown 
         */
        public Builder(android.hardware.radio.RadioMetadata source, int maxBitmapSize) {
            this(source);
            for (java.lang.String key : mBundle.keySet()) {
                java.lang.Object value = mBundle.get(key);
                if ((value != null) && (value instanceof android.graphics.Bitmap)) {
                    android.graphics.Bitmap bmp = ((android.graphics.Bitmap) (value));
                    if ((bmp.getHeight() > maxBitmapSize) || (bmp.getWidth() > maxBitmapSize)) {
                        putBitmap(key, scaleBitmap(bmp, maxBitmapSize));
                    }
                }
            }
        }

        /**
         * Put a String value into the meta data. Custom keys may be used, but if
         * the METADATA_KEYs defined in this class are used they may only be one
         * of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_RDS_PI}</li>
         * <li>{@link #METADATA_KEY_RDS_PS}</li>
         * <li>{@link #METADATA_KEY_RDS_RT}</li>
         * <li>{@link #METADATA_KEY_TITLE}</li>
         * <li>{@link #METADATA_KEY_ARTIST}</li>
         * <li>{@link #METADATA_KEY_ALBUM}</li>
         * <li>{@link #METADATA_KEY_GENRE}</li>
         * </ul>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The String value to store
         * @return the same Builder instance
         */
        public android.hardware.radio.RadioMetadata.Builder putString(java.lang.String key, java.lang.String value) {
            if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT)) {
                throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a String");
            }
            mBundle.putString(key, value);
            return this;
        }

        /**
         * Put an int value into the meta data. Custom keys may be used, but if
         * the METADATA_KEYs defined in this class are used they may only be one
         * of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_RDS_PTY}</li>
         * <li>{@link #METADATA_KEY_RBDS_PTY}</li>
         * </ul>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The int value to store
         * @return the same Builder instance
         */
        public android.hardware.radio.RadioMetadata.Builder putInt(java.lang.String key, int value) {
            if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_INT)) {
                throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a long");
            }
            mBundle.putInt(key, value);
            return this;
        }

        /**
         * Put a {@link Bitmap} into the meta data. Custom keys may be used, but
         * if the METADATA_KEYs defined in this class are used they may only be
         * one of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_ICON}</li>
         * <li>{@link #METADATA_KEY_ART}</li>
         * </ul>
         * <p>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The Bitmap to store
         * @return the same Builder instance
         */
        public android.hardware.radio.RadioMetadata.Builder putBitmap(java.lang.String key, android.graphics.Bitmap value) {
            if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_BITMAP)) {
                throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a Bitmap");
            }
            mBundle.putParcelable(key, value);
            return this;
        }

        /**
         * Put a {@link RadioMetadata.Clock} into the meta data. Custom keys may be used, but if the
         * METADATA_KEYs defined in this class are used they may only be one of the following:
         * <ul>
         * <li>{@link #MEADATA_KEY_CLOCK}</li>
         * </ul>
         *
         * @param utcSecondsSinceEpoch
         * 		Number of seconds since epoch for UTC + 0 timezone.
         * @param timezoneOffsetInMinutes
         * 		Offset of timezone from UTC + 0 in minutes.
         * @return the same Builder instance.
         */
        public android.hardware.radio.RadioMetadata.Builder putClock(java.lang.String key, long utcSecondsSinceEpoch, int timezoneOffsetMinutes) {
            if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_CLOCK)) {
                throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a RadioMetadata.Clock.");
            }
            mBundle.putParcelable(key, new android.hardware.radio.RadioMetadata.Clock(utcSecondsSinceEpoch, timezoneOffsetMinutes));
            return this;
        }

        /**
         * Creates a {@link RadioMetadata} instance with the specified fields.
         *
         * @return a new {@link RadioMetadata} object
         */
        public android.hardware.radio.RadioMetadata build() {
            return new android.hardware.radio.RadioMetadata(mBundle);
        }

        private android.graphics.Bitmap scaleBitmap(android.graphics.Bitmap bmp, int maxSize) {
            float maxSizeF = maxSize;
            float widthScale = maxSizeF / bmp.getWidth();
            float heightScale = maxSizeF / bmp.getHeight();
            float scale = java.lang.Math.min(widthScale, heightScale);
            int height = ((int) (bmp.getHeight() * scale));
            int width = ((int) (bmp.getWidth() * scale));
            return android.graphics.Bitmap.createScaledBitmap(bmp, width, height, true);
        }
    }

    int putIntFromNative(int nativeKey, int value) {
        java.lang.String key = android.hardware.radio.RadioMetadata.getKeyFromNativeKey(nativeKey);
        if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_INT)) {
            return -1;
        }
        mBundle.putInt(key, value);
        return 0;
    }

    int putStringFromNative(int nativeKey, java.lang.String value) {
        java.lang.String key = android.hardware.radio.RadioMetadata.getKeyFromNativeKey(nativeKey);
        if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_TEXT)) {
            return -1;
        }
        mBundle.putString(key, value);
        return 0;
    }

    int putBitmapFromNative(int nativeKey, byte[] value) {
        java.lang.String key = android.hardware.radio.RadioMetadata.getKeyFromNativeKey(nativeKey);
        if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_BITMAP)) {
            return -1;
        }
        android.graphics.Bitmap bmp = null;
        try {
            bmp = android.graphics.BitmapFactory.decodeByteArray(value, 0, value.length);
        } catch (java.lang.Exception e) {
        } finally {
            if (bmp == null) {
                return -1;
            }
            mBundle.putParcelable(key, bmp);
            return 0;
        }
    }

    int putClockFromNative(int nativeKey, long utcEpochSeconds, int timezoneOffsetInMinutes) {
        android.util.Log.d(android.hardware.radio.RadioMetadata.TAG, "putClockFromNative()");
        java.lang.String key = android.hardware.radio.RadioMetadata.getKeyFromNativeKey(nativeKey);
        if ((!android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.containsKey(key)) || (android.hardware.radio.RadioMetadata.METADATA_KEYS_TYPE.get(key) != android.hardware.radio.RadioMetadata.METADATA_TYPE_CLOCK)) {
            return -1;
        }
        mBundle.putParcelable(key, new android.hardware.radio.RadioMetadata.Clock(utcEpochSeconds, timezoneOffsetInMinutes));
        return 0;
    }
}

