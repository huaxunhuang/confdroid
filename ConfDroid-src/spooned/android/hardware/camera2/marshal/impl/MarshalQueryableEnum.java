/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.camera2.marshal.impl;


/**
 * Marshal any simple enum (0-arg constructors only) into/from either
 * {@code TYPE_BYTE} or {@code TYPE_INT32}.
 *
 * <p>Default values of the enum are mapped to its ordinal; this can be overridden
 * by providing a manual value with {@link #registerEnumValues}.</p>
 *
 * @param <T>
 * 		the type of {@code Enum}
 */
public class MarshalQueryableEnum<T extends java.lang.Enum<T>> implements android.hardware.camera2.marshal.MarshalQueryable<T> {
    private static final java.lang.String TAG = android.hardware.camera2.marshal.impl.MarshalQueryableEnum.class.getSimpleName();

    private static final boolean DEBUG = false;

    private static final int UINT8_MIN = 0x0;

    private static final int UINT8_MAX = (1 << java.lang.Byte.SIZE) - 1;

    private static final int UINT8_MASK = android.hardware.camera2.marshal.impl.MarshalQueryableEnum.UINT8_MAX;

    private class MarshalerEnum extends android.hardware.camera2.marshal.Marshaler<T> {
        private final java.lang.Class<T> mClass;

        @java.lang.SuppressWarnings("unchecked")
        protected MarshalerEnum(android.hardware.camera2.utils.TypeReference<T> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableEnum.this, typeReference, nativeType);
            mClass = ((java.lang.Class<T>) (typeReference.getRawType()));
        }

        @java.lang.Override
        public void marshal(T value, java.nio.ByteBuffer buffer) {
            int enumValue = android.hardware.camera2.marshal.impl.MarshalQueryableEnum.getEnumValue(value);
            if (mNativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) {
                buffer.putInt(enumValue);
            } else
                if (mNativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE) {
                    if ((enumValue < android.hardware.camera2.marshal.impl.MarshalQueryableEnum.UINT8_MIN) || (enumValue > android.hardware.camera2.marshal.impl.MarshalQueryableEnum.UINT8_MAX)) {
                        throw new java.lang.UnsupportedOperationException(java.lang.String.format("Enum value %x too large to fit into unsigned byte", enumValue));
                    }
                    buffer.put(((byte) (enumValue)));
                } else {
                    throw new java.lang.AssertionError();
                }

        }

        @java.lang.Override
        public T unmarshal(java.nio.ByteBuffer buffer) {
            int enumValue;
            switch (mNativeType) {
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32 :
                    enumValue = buffer.getInt();
                    break;
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE :
                    // get the unsigned byte value; avoid sign extension
                    enumValue = buffer.get() & android.hardware.camera2.marshal.impl.MarshalQueryableEnum.UINT8_MASK;
                    break;
                default :
                    throw new java.lang.AssertionError("Unexpected native type; impossible since its not supported");
            }
            return android.hardware.camera2.marshal.impl.MarshalQueryableEnum.getEnumFromValue(mClass, enumValue);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.MarshalHelpers.getPrimitiveTypeSize(mNativeType);
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<T> createMarshaler(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        return new MarshalerEnum(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        if ((nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) || (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE)) {
            if (managedType.getType() instanceof java.lang.Class<?>) {
                java.lang.Class<?> typeClass = ((java.lang.Class<?>) (managedType.getType()));
                if (typeClass.isEnum()) {
                    if (android.hardware.camera2.marshal.impl.MarshalQueryableEnum.DEBUG) {
                        android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableEnum.TAG, "possible enum detected for " + typeClass);
                    }
                    // The enum must not take extra arguments
                    try {
                        // match a class like: "public enum Fruits { Apple, Orange; }"
                        typeClass.getDeclaredConstructor(java.lang.String.class, int.class);
                        return true;
                    } catch (java.lang.NoSuchMethodException e) {
                        // Skip: custom enum with a special constructor e.g. Foo(T), but need Foo()
                        android.util.Log.e(android.hardware.camera2.marshal.impl.MarshalQueryableEnum.TAG, ("Can't marshal class " + typeClass) + "; no default constructor");
                    } catch (java.lang.SecurityException e) {
                        // Skip: wouldn't be able to touch the enum anyway
                        android.util.Log.e(android.hardware.camera2.marshal.impl.MarshalQueryableEnum.TAG, ("Can't marshal class " + typeClass) + "; not accessible");
                    }
                }
            }
        }
        return false;
    }

    @java.lang.SuppressWarnings("rawtypes")
    private static final java.util.HashMap<java.lang.Class<? extends java.lang.Enum>, int[]> sEnumValues = new java.util.HashMap<java.lang.Class<? extends java.lang.Enum>, int[]>();

    /**
     * Register a non-sequential set of values to be used with the marshal/unmarshal functions.
     *
     * <p>This enables get/set to correctly marshal the enum into a value that is C-compatible.</p>
     *
     * @param enumType
     * 		The class for an enum
     * @param values
     * 		A list of values mapping to the ordinals of the enum
     */
    public static <T extends java.lang.Enum<T>> void registerEnumValues(java.lang.Class<T> enumType, int[] values) {
        if (enumType.getEnumConstants().length != values.length) {
            throw new java.lang.IllegalArgumentException((("Expected values array to be the same size as the enumTypes values " + values.length) + " for type ") + enumType);
        }
        if (android.hardware.camera2.marshal.impl.MarshalQueryableEnum.DEBUG) {
            android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableEnum.TAG, ("Registered enum values for type " + enumType) + " values");
        }
        android.hardware.camera2.marshal.impl.MarshalQueryableEnum.sEnumValues.put(enumType, values);
    }

    /**
     * Get the numeric value from an enum.
     *
     * <p>This is usually the same as the ordinal value for
     * enums that have fully sequential values, although for C-style enums the range of values
     * may not map 1:1.</p>
     *
     * @param enumValue
     * 		Enum instance
     * @return Int guaranteed to be ABI-compatible with the C enum equivalent
     */
    private static <T extends java.lang.Enum<T>> int getEnumValue(T enumValue) {
        int[] values;
        values = android.hardware.camera2.marshal.impl.MarshalQueryableEnum.sEnumValues.get(enumValue.getClass());
        int ordinal = enumValue.ordinal();
        if (values != null) {
            return values[ordinal];
        }
        return ordinal;
    }

    /**
     * Finds the enum corresponding to it's numeric value. Opposite of {@link #getEnumValue} method.
     *
     * @param enumType
     * 		Class of the enum we want to find
     * @param value
     * 		The numeric value of the enum
     * @return An instance of the enum
     */
    private static <T extends java.lang.Enum<T>> T getEnumFromValue(java.lang.Class<T> enumType, int value) {
        int ordinal;
        int[] registeredValues = android.hardware.camera2.marshal.impl.MarshalQueryableEnum.sEnumValues.get(enumType);
        if (registeredValues != null) {
            ordinal = -1;
            for (int i = 0; i < registeredValues.length; ++i) {
                if (registeredValues[i] == value) {
                    ordinal = i;
                    break;
                }
            }
        } else {
            ordinal = value;
        }
        T[] values = enumType.getEnumConstants();
        if ((ordinal < 0) || (ordinal >= values.length)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Argument 'value' (%d) was not a valid enum value for type %s " + "(registered? %b)", value, enumType, registeredValues != null));
        }
        return values[ordinal];
    }
}

