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
 * Marshaler for custom class {@link StreamConfigurationDuration} for min-frame and stall durations.
 *
 * <p>
 * Data is stored as {@code (format, width, height, durationNs)} tuples (int64).
 * </p>
 */
public class MarshalQueryableStreamConfigurationDuration implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.StreamConfigurationDuration> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT64 * 4;

    /**
     * Values and-ed with this will do an unsigned int to signed long conversion;
     * in other words the sign bit from the int will not be extended.
     */
    private static final long MASK_UNSIGNED_INT = 0xffffffffL;

    private class MarshalerStreamConfigurationDuration extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.StreamConfigurationDuration> {
        protected MarshalerStreamConfigurationDuration(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.StreamConfigurationDuration> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.StreamConfigurationDuration value, java.nio.ByteBuffer buffer) {
            buffer.putLong(value.getFormat() & android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration.MASK_UNSIGNED_INT);// unsigned int -> long

            buffer.putLong(value.getWidth());
            buffer.putLong(value.getHeight());
            buffer.putLong(value.getDuration());
        }

        @java.lang.Override
        public android.hardware.camera2.params.StreamConfigurationDuration unmarshal(java.nio.ByteBuffer buffer) {
            int format = ((int) (buffer.getLong()));
            int width = ((int) (buffer.getLong()));
            int height = ((int) (buffer.getLong()));
            long durationNs = buffer.getLong();
            return new android.hardware.camera2.params.StreamConfigurationDuration(format, width, height, durationNs);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.StreamConfigurationDuration> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.StreamConfigurationDuration> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration.MarshalerStreamConfigurationDuration(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.StreamConfigurationDuration> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT64) && android.hardware.camera2.params.StreamConfigurationDuration.class.equals(managedType.getType());
    }
}

