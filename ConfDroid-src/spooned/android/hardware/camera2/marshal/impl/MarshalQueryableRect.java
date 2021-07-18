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
 * Marshal {@link Rect} to/from {@link #TYPE_INT32}
 */
public class MarshalQueryableRect implements android.hardware.camera2.marshal.MarshalQueryable<android.graphics.Rect> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * 4;

    private class MarshalerRect extends android.hardware.camera2.marshal.Marshaler<android.graphics.Rect> {
        protected MarshalerRect(android.hardware.camera2.utils.TypeReference<android.graphics.Rect> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableRect.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.graphics.Rect value, java.nio.ByteBuffer buffer) {
            buffer.putInt(value.left);
            buffer.putInt(value.top);
            buffer.putInt(value.width());
            buffer.putInt(value.height());
        }

        @java.lang.Override
        public android.graphics.Rect unmarshal(java.nio.ByteBuffer buffer) {
            int left = buffer.getInt();
            int top = buffer.getInt();
            int width = buffer.getInt();
            int height = buffer.getInt();
            int right = left + width;
            int bottom = top + height;
            return new android.graphics.Rect(left, top, right, bottom);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableRect.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.graphics.Rect> createMarshaler(android.hardware.camera2.utils.TypeReference<android.graphics.Rect> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableRect.MarshalerRect(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.graphics.Rect> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && android.graphics.Rect.class.equals(managedType.getType());
    }
}

