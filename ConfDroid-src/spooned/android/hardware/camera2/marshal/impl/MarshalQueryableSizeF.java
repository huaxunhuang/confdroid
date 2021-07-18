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
 * Marshal {@link SizeF} to/from {@code TYPE_FLOAT}
 */
public class MarshalQueryableSizeF implements android.hardware.camera2.marshal.MarshalQueryable<android.util.SizeF> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_FLOAT * 2;

    private class MarshalerSizeF extends android.hardware.camera2.marshal.Marshaler<android.util.SizeF> {
        protected MarshalerSizeF(android.hardware.camera2.utils.TypeReference<android.util.SizeF> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableSizeF.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.util.SizeF value, java.nio.ByteBuffer buffer) {
            buffer.putFloat(value.getWidth());
            buffer.putFloat(value.getHeight());
        }

        @java.lang.Override
        public android.util.SizeF unmarshal(java.nio.ByteBuffer buffer) {
            float width = buffer.getFloat();
            float height = buffer.getFloat();
            return new android.util.SizeF(width, height);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableSizeF.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.util.SizeF> createMarshaler(android.hardware.camera2.utils.TypeReference<android.util.SizeF> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableSizeF.MarshalerSizeF(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.util.SizeF> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_FLOAT) && android.util.SizeF.class.equals(managedType.getType());
    }
}

