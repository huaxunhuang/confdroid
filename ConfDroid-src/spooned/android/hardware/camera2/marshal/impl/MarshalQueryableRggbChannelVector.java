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
 * Marshal {@link RggbChannelVector} to/from {@link #TYPE_FLOAT} {@code x 4}
 */
public class MarshalQueryableRggbChannelVector implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.RggbChannelVector> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_FLOAT * android.hardware.camera2.params.RggbChannelVector.COUNT;

    private class MarshalerRggbChannelVector extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.RggbChannelVector> {
        protected MarshalerRggbChannelVector(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.RggbChannelVector> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.RggbChannelVector value, java.nio.ByteBuffer buffer) {
            for (int i = 0; i < android.hardware.camera2.params.RggbChannelVector.COUNT; ++i) {
                buffer.putFloat(value.getComponent(i));
            }
        }

        @java.lang.Override
        public android.hardware.camera2.params.RggbChannelVector unmarshal(java.nio.ByteBuffer buffer) {
            float red = buffer.getFloat();
            float gEven = buffer.getFloat();
            float gOdd = buffer.getFloat();
            float blue = buffer.getFloat();
            return new android.hardware.camera2.params.RggbChannelVector(red, gEven, gOdd, blue);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.RggbChannelVector> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.RggbChannelVector> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector.MarshalerRggbChannelVector(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.RggbChannelVector> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_FLOAT) && android.hardware.camera2.params.RggbChannelVector.class.equals(managedType.getType());
    }
}

