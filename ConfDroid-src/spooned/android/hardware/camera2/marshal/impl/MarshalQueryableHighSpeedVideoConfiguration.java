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
 * Marshaler for {@code android.control.availableHighSpeedVideoConfigurations} custom class
 * {@link HighSpeedVideoConfiguration}
 *
 * <p>Data is stored as {@code (width, height, fpsMin, fpsMax)} tuples (int32).</p>
 */
public class MarshalQueryableHighSpeedVideoConfiguration implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.HighSpeedVideoConfiguration> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * 5;

    private class MarshalerHighSpeedVideoConfiguration extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.HighSpeedVideoConfiguration> {
        protected MarshalerHighSpeedVideoConfiguration(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.HighSpeedVideoConfiguration> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.HighSpeedVideoConfiguration value, java.nio.ByteBuffer buffer) {
            buffer.putInt(value.getWidth());
            buffer.putInt(value.getHeight());
            buffer.putInt(value.getFpsMin());
            buffer.putInt(value.getFpsMax());
            buffer.putInt(value.getBatchSizeMax());
        }

        @java.lang.Override
        public android.hardware.camera2.params.HighSpeedVideoConfiguration unmarshal(java.nio.ByteBuffer buffer) {
            int width = buffer.getInt();
            int height = buffer.getInt();
            int fpsMin = buffer.getInt();
            int fpsMax = buffer.getInt();
            int batchSizeMax = buffer.getInt();
            return new android.hardware.camera2.params.HighSpeedVideoConfiguration(width, height, fpsMin, fpsMax, batchSizeMax);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.HighSpeedVideoConfiguration> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.HighSpeedVideoConfiguration> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration.MarshalerHighSpeedVideoConfiguration(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.HighSpeedVideoConfiguration> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && managedType.getType().equals(android.hardware.camera2.params.HighSpeedVideoConfiguration.class);
    }
}

