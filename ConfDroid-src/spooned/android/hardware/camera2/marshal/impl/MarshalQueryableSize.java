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
 * Marshal {@link Size} to/from {@code TYPE_INT32}
 */
public class MarshalQueryableSize implements android.hardware.camera2.marshal.MarshalQueryable<android.util.Size> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * 2;

    private class MarshalerSize extends android.hardware.camera2.marshal.Marshaler<android.util.Size> {
        protected MarshalerSize(android.hardware.camera2.utils.TypeReference<android.util.Size> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableSize.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.util.Size value, java.nio.ByteBuffer buffer) {
            buffer.putInt(value.getWidth());
            buffer.putInt(value.getHeight());
        }

        @java.lang.Override
        public android.util.Size unmarshal(java.nio.ByteBuffer buffer) {
            int width = buffer.getInt();
            int height = buffer.getInt();
            return new android.util.Size(width, height);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableSize.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.util.Size> createMarshaler(android.hardware.camera2.utils.TypeReference<android.util.Size> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableSize.MarshalerSize(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.util.Size> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && android.util.Size.class.equals(managedType.getType());
    }
}

