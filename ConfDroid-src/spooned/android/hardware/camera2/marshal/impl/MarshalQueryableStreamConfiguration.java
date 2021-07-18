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
 * Marshaler for {@code android.scaler.availableStreamConfigurations} custom class
 * {@link StreamConfiguration}
 *
 * <p>Data is stored as {@code (format, width, height, input?)} tuples (int32).</p>
 */
public class MarshalQueryableStreamConfiguration implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.StreamConfiguration> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * 4;

    private class MarshalerStreamConfiguration extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.StreamConfiguration> {
        protected MarshalerStreamConfiguration(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.StreamConfiguration> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.StreamConfiguration value, java.nio.ByteBuffer buffer) {
            buffer.putInt(value.getFormat());
            buffer.putInt(value.getWidth());
            buffer.putInt(value.getHeight());
            buffer.putInt(value.isInput() ? 1 : 0);
        }

        @java.lang.Override
        public android.hardware.camera2.params.StreamConfiguration unmarshal(java.nio.ByteBuffer buffer) {
            int format = buffer.getInt();
            int width = buffer.getInt();
            int height = buffer.getInt();
            boolean input = buffer.getInt() != 0;
            return new android.hardware.camera2.params.StreamConfiguration(format, width, height, input);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.StreamConfiguration> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.StreamConfiguration> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration.MarshalerStreamConfiguration(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.StreamConfiguration> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && managedType.getType().equals(android.hardware.camera2.params.StreamConfiguration.class);
    }
}

