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
 * Marshal/unmarshal built-in primitive types to and from a {@link ByteBuffer}.
 *
 * <p>The following list of type marshaling is supported:
 * <ul>
 * <li>byte <-> TYPE_BYTE
 * <li>int <-> TYPE_INT32
 * <li>long <-> TYPE_INT64
 * <li>float <-> TYPE_FLOAT
 * <li>double <-> TYPE_DOUBLE
 * <li>Rational <-> TYPE_RATIONAL
 * </ul>
 * </p>
 *
 * <p>Due to the nature of generics, values are always boxed; this also means that both
 * the boxed and unboxed types are supported (i.e. both {@code int} and {@code Integer}).</p>
 *
 * <p>Each managed type <!--(other than boolean)--> must correspond 1:1 to the native type
 * (e.g. a byte will not map to a {@link CameraMetadataNative#TYPE_INT32 TYPE_INT32} or vice versa)
 * for marshaling.</p>
 */
public final class MarshalQueryablePrimitive<T> implements android.hardware.camera2.marshal.MarshalQueryable<T> {
    private class MarshalerPrimitive extends android.hardware.camera2.marshal.Marshaler<T> {
        /**
         * Always the wrapped class variant of the primitive class for {@code T}
         */
        private final java.lang.Class<T> mClass;

        @java.lang.SuppressWarnings("unchecked")
        protected MarshalerPrimitive(android.hardware.camera2.utils.TypeReference<T> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryablePrimitive.this, typeReference, nativeType);
            // Turn primitives into wrappers, otherwise int.class.cast(Integer) will fail
            mClass = android.hardware.camera2.marshal.MarshalHelpers.wrapClassIfPrimitive(((java.lang.Class<T>) (typeReference.getRawType())));
        }

        @java.lang.Override
        public T unmarshal(java.nio.ByteBuffer buffer) {
            return mClass.cast(unmarshalObject(buffer));
        }

        @java.lang.Override
        public int calculateMarshalSize(T value) {
            return android.hardware.camera2.marshal.MarshalHelpers.getPrimitiveTypeSize(mNativeType);
        }

        @java.lang.Override
        public void marshal(T value, java.nio.ByteBuffer buffer) {
            if (value instanceof java.lang.Integer) {
                android.hardware.camera2.marshal.MarshalHelpers.checkNativeTypeEquals(android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32, mNativeType);
                final int val = ((java.lang.Integer) (value));
                marshalPrimitive(val, buffer);
            } else
                if (value instanceof java.lang.Float) {
                    android.hardware.camera2.marshal.MarshalHelpers.checkNativeTypeEquals(android.hardware.camera2.impl.CameraMetadataNative.TYPE_FLOAT, mNativeType);
                    final float val = ((java.lang.Float) (value));
                    marshalPrimitive(val, buffer);
                } else
                    if (value instanceof java.lang.Long) {
                        android.hardware.camera2.marshal.MarshalHelpers.checkNativeTypeEquals(android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT64, mNativeType);
                        final long val = ((java.lang.Long) (value));
                        marshalPrimitive(val, buffer);
                    } else
                        if (value instanceof android.util.Rational) {
                            android.hardware.camera2.marshal.MarshalHelpers.checkNativeTypeEquals(android.hardware.camera2.impl.CameraMetadataNative.TYPE_RATIONAL, mNativeType);
                            marshalPrimitive(((android.util.Rational) (value)), buffer);
                        } else
                            if (value instanceof java.lang.Double) {
                                android.hardware.camera2.marshal.MarshalHelpers.checkNativeTypeEquals(android.hardware.camera2.impl.CameraMetadataNative.TYPE_DOUBLE, mNativeType);
                                final double val = ((java.lang.Double) (value));
                                marshalPrimitive(val, buffer);
                            } else
                                if (value instanceof java.lang.Byte) {
                                    android.hardware.camera2.marshal.MarshalHelpers.checkNativeTypeEquals(android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE, mNativeType);
                                    final byte val = ((java.lang.Byte) (value));
                                    marshalPrimitive(val, buffer);
                                } else {
                                    throw new java.lang.UnsupportedOperationException("Can't marshal managed type " + mTypeReference);
                                }





        }

        private void marshalPrimitive(int value, java.nio.ByteBuffer buffer) {
            buffer.putInt(value);
        }

        private void marshalPrimitive(float value, java.nio.ByteBuffer buffer) {
            buffer.putFloat(value);
        }

        private void marshalPrimitive(double value, java.nio.ByteBuffer buffer) {
            buffer.putDouble(value);
        }

        private void marshalPrimitive(long value, java.nio.ByteBuffer buffer) {
            buffer.putLong(value);
        }

        private void marshalPrimitive(android.util.Rational value, java.nio.ByteBuffer buffer) {
            buffer.putInt(value.getNumerator());
            buffer.putInt(value.getDenominator());
        }

        private void marshalPrimitive(byte value, java.nio.ByteBuffer buffer) {
            buffer.put(value);
        }

        private java.lang.Object unmarshalObject(java.nio.ByteBuffer buffer) {
            switch (mNativeType) {
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32 :
                    return buffer.getInt();
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_FLOAT :
                    return buffer.getFloat();
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT64 :
                    return buffer.getLong();
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_RATIONAL :
                    int numerator = buffer.getInt();
                    int denominator = buffer.getInt();
                    return new android.util.Rational(numerator, denominator);
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_DOUBLE :
                    return buffer.getDouble();
                case android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE :
                    return buffer.get();// getByte

                default :
                    throw new java.lang.UnsupportedOperationException("Can't unmarshal native type " + mNativeType);
            }
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.MarshalHelpers.getPrimitiveTypeSize(mNativeType);
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<T> createMarshaler(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        return new MarshalerPrimitive(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        if (managedType.getType() instanceof java.lang.Class<?>) {
            java.lang.Class<?> klass = ((java.lang.Class<?>) (managedType.getType()));
            if ((klass == byte.class) || (klass == java.lang.Byte.class)) {
                return nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE;
            } else
                if ((klass == int.class) || (klass == java.lang.Integer.class)) {
                    return nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32;
                } else
                    if ((klass == float.class) || (klass == java.lang.Float.class)) {
                        return nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_FLOAT;
                    } else
                        if ((klass == long.class) || (klass == java.lang.Long.class)) {
                            return nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT64;
                        } else
                            if ((klass == double.class) || (klass == java.lang.Double.class)) {
                                return nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_DOUBLE;
                            } else
                                if (klass == android.util.Rational.class) {
                                    return nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_RATIONAL;
                                }





        }
        return false;
    }
}

