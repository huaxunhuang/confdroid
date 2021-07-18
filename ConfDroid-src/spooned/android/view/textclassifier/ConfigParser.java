/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.textclassifier;


/**
 * Retrieves settings from {@link DeviceConfig} and {@link android.provider.Settings}.
 * It will try DeviceConfig first and then Settings.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
public final class ConfigParser {
    private static final java.lang.String TAG = "ConfigParser";

    static final boolean ENABLE_DEVICE_CONFIG = true;

    private static final java.lang.String STRING_LIST_DELIMITER = ":";

    private final java.util.function.Supplier<java.lang.String> mLegacySettingsSupplier;

    private final java.lang.Object mLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mLock")
    private final java.util.Map<java.lang.String, java.lang.Object> mCache = new android.util.ArrayMap();

    @com.android.internal.annotations.GuardedBy("mLock")
    @android.annotation.Nullable
    private android.util.KeyValueListParser mSettingsParser;// Call getLegacySettings() instead.


    public ConfigParser(java.util.function.Supplier<java.lang.String> legacySettingsSupplier) {
        mLegacySettingsSupplier = com.android.internal.util.Preconditions.checkNotNull(legacySettingsSupplier);
    }

    private android.util.KeyValueListParser getLegacySettings() {
        synchronized(mLock) {
            if (mSettingsParser == null) {
                final java.lang.String legacySettings = mLegacySettingsSupplier.get();
                try {
                    mSettingsParser = new android.util.KeyValueListParser(',');
                    mSettingsParser.setString(legacySettings);
                } catch (java.lang.IllegalArgumentException e) {
                    // Failed to parse the settings string, log this and move on with defaults.
                    android.view.textclassifier.Log.w(android.view.textclassifier.ConfigParser.TAG, "Bad text_classifier_constants: " + legacySettings);
                }
            }
            return mSettingsParser;
        }
    }

    /**
     * Reads a boolean setting through the cache.
     */
    public boolean getBoolean(java.lang.String key, boolean defaultValue) {
        synchronized(mLock) {
            final java.lang.Object cached = mCache.get(key);
            if (cached instanceof java.lang.Boolean) {
                return ((boolean) (cached));
            }
            final boolean value;
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                value = android.provider.DeviceConfig.getBoolean(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getBoolean(key, defaultValue));
            } else {
                value = getLegacySettings().getBoolean(key, defaultValue);
            }
            mCache.put(key, value);
            return value;
        }
    }

    /**
     * Reads an integer setting through the cache.
     */
    public int getInt(java.lang.String key, int defaultValue) {
        synchronized(mLock) {
            final java.lang.Object cached = mCache.get(key);
            if (cached instanceof java.lang.Integer) {
                return ((int) (cached));
            }
            final int value;
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                value = android.provider.DeviceConfig.getInt(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getInt(key, defaultValue));
            } else {
                value = getLegacySettings().getInt(key, defaultValue);
            }
            mCache.put(key, value);
            return value;
        }
    }

    /**
     * Reads a float setting through the cache.
     */
    public float getFloat(java.lang.String key, float defaultValue) {
        synchronized(mLock) {
            final java.lang.Object cached = mCache.get(key);
            if (cached instanceof java.lang.Float) {
                return ((float) (cached));
            }
            final float value;
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                value = android.provider.DeviceConfig.getFloat(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getFloat(key, defaultValue));
            } else {
                value = getLegacySettings().getFloat(key, defaultValue);
            }
            mCache.put(key, value);
            return value;
        }
    }

    /**
     * Reads a string setting through the cache.
     */
    public java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
        synchronized(mLock) {
            final java.lang.Object cached = mCache.get(key);
            if (cached instanceof java.lang.String) {
                return ((java.lang.String) (cached));
            }
            final java.lang.String value;
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                value = android.provider.DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, getLegacySettings().getString(key, defaultValue));
            } else {
                value = getLegacySettings().getString(key, defaultValue);
            }
            mCache.put(key, value);
            return value;
        }
    }

    /**
     * Reads a string list setting through the cache.
     */
    public java.util.List<java.lang.String> getStringList(java.lang.String key, java.util.List<java.lang.String> defaultValue) {
        synchronized(mLock) {
            final java.lang.Object cached = mCache.get(key);
            if (cached instanceof java.util.List) {
                final java.util.List asList = ((java.util.List) (cached));
                if (asList.isEmpty()) {
                    return java.util.Collections.emptyList();
                } else
                    if (asList.get(0) instanceof java.lang.String) {
                        return ((java.util.List<java.lang.String>) (cached));
                    }

            }
            final java.util.List<java.lang.String> value;
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                value = android.view.textclassifier.ConfigParser.getDeviceConfigStringList(key, getSettingsStringList(key, defaultValue));
            } else {
                value = getSettingsStringList(key, defaultValue);
            }
            mCache.put(key, value);
            return value;
        }
    }

    /**
     * Reads a float array through the cache. The returned array should be expected to be of the
     * same length as that of the defaultValue.
     */
    public float[] getFloatArray(java.lang.String key, float[] defaultValue) {
        synchronized(mLock) {
            final java.lang.Object cached = mCache.get(key);
            if (cached instanceof float[]) {
                return ((float[]) (cached));
            }
            final float[] value;
            if (android.view.textclassifier.ConfigParser.ENABLE_DEVICE_CONFIG) {
                value = android.view.textclassifier.ConfigParser.getDeviceConfigFloatArray(key, getSettingsFloatArray(key, defaultValue));
            } else {
                value = getSettingsFloatArray(key, defaultValue);
            }
            mCache.put(key, value);
            return value;
        }
    }

    private java.util.List<java.lang.String> getSettingsStringList(java.lang.String key, java.util.List<java.lang.String> defaultValue) {
        return android.view.textclassifier.ConfigParser.parse(mSettingsParser.getString(key, null), defaultValue);
    }

    private static java.util.List<java.lang.String> getDeviceConfigStringList(java.lang.String key, java.util.List<java.lang.String> defaultValue) {
        return android.view.textclassifier.ConfigParser.parse(android.provider.DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, null), defaultValue);
    }

    private static float[] getDeviceConfigFloatArray(java.lang.String key, float[] defaultValue) {
        return android.view.textclassifier.ConfigParser.parse(android.provider.DeviceConfig.getString(DeviceConfig.NAMESPACE_TEXTCLASSIFIER, key, null), defaultValue);
    }

    private float[] getSettingsFloatArray(java.lang.String key, float[] defaultValue) {
        return android.view.textclassifier.ConfigParser.parse(mSettingsParser.getString(key, null), defaultValue);
    }

    private static java.util.List<java.lang.String> parse(@android.annotation.Nullable
    java.lang.String listStr, java.util.List<java.lang.String> defaultValue) {
        if (listStr != null) {
            return java.util.Collections.unmodifiableList(java.util.Arrays.asList(listStr.split(android.view.textclassifier.ConfigParser.STRING_LIST_DELIMITER)));
        }
        return defaultValue;
    }

    private static float[] parse(@android.annotation.Nullable
    java.lang.String arrayStr, float[] defaultValue) {
        if (arrayStr != null) {
            final java.lang.String[] split = arrayStr.split(android.view.textclassifier.ConfigParser.STRING_LIST_DELIMITER);
            if (split.length != defaultValue.length) {
                return defaultValue;
            }
            final float[] result = new float[split.length];
            for (int i = 0; i < split.length; i++) {
                try {
                    result[i] = java.lang.Float.parseFloat(split[i]);
                } catch (java.lang.NumberFormatException e) {
                    return defaultValue;
                }
            }
            return result;
        } else {
            return defaultValue;
        }
    }
}

