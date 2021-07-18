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
 * Marshal booleans: TYPE_BYTE <-> boolean/Boolean
 */
public class MarshalQueryableBoolean implements android.hardware.camera2.marshal.MarshalQueryable<java.lang.Boolean> {
    private class MarshalerBoolean extends android.hardware.camera2.marshal.Marshaler<java.lang.Boolean> {
        protected MarshalerBoolean(android.hardware.camera2.utils.TypeReference<java.lang.Boolean> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableBoolean.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(java.lang.Boolean value, java.nio.ByteBuffer buffer) {
            boolean unboxValue = value;
            buffer.put(((byte) (unboxValue ? 1 : 0)));
        }

        @java.lang.Override
        public java.lang.Boolean unmarshal(java.nio.ByteBuffer buffer) {
            return buffer.get() != 0;
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_BYTE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<java.lang.Boolean> createMarshaler(android.hardware.camera2.utils.TypeReference<java.lang.Boolean> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableBoolean.MarshalerBoolean(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<java.lang.Boolean> managedType, int nativeType) {
        return (java.lang.Boolean.class.equals(managedType.getType()) || boolean.class.equals(managedType.getType())) && (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE);
    }
}

