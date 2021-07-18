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
 * Marshal fake native enums (ints): TYPE_BYTE <-> int/Integer
 */
public class MarshalQueryableNativeByteToInteger implements android.hardware.camera2.marshal.MarshalQueryable<java.lang.Integer> {
    private static final int UINT8_MASK = (1 << java.lang.Byte.SIZE) - 1;

    private class MarshalerNativeByteToInteger extends android.hardware.camera2.marshal.Marshaler<java.lang.Integer> {
        protected MarshalerNativeByteToInteger(android.hardware.camera2.utils.TypeReference<java.lang.Integer> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(java.lang.Integer value, java.nio.ByteBuffer buffer) {
            buffer.put(((byte) ((int) (value))));// truncate down to byte

        }

        @java.lang.Override
        public java.lang.Integer unmarshal(java.nio.ByteBuffer buffer) {
            // expand unsigned byte to int; avoid sign extension
            return buffer.get() & android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger.UINT8_MASK;
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_BYTE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<java.lang.Integer> createMarshaler(android.hardware.camera2.utils.TypeReference<java.lang.Integer> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger.MarshalerNativeByteToInteger(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<java.lang.Integer> managedType, int nativeType) {
        return (java.lang.Integer.class.equals(managedType.getType()) || int.class.equals(managedType.getType())) && (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE);
    }
}

