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
 * Marshal {@link String} to/from {@link #TYPE_BYTE}.
 */
public class MarshalQueryableString implements android.hardware.camera2.marshal.MarshalQueryable<java.lang.String> {
    private static final java.lang.String TAG = android.hardware.camera2.marshal.impl.MarshalQueryableString.class.getSimpleName();

    private static final boolean DEBUG = false;

    private static final java.nio.charset.Charset UTF8_CHARSET = java.nio.charset.Charset.forName("UTF-8");

    private static final byte NUL = ((byte) ('\u0000'));// used as string terminator


    private class MarshalerString extends android.hardware.camera2.marshal.Marshaler<java.lang.String> {
        protected MarshalerString(android.hardware.camera2.utils.TypeReference<java.lang.String> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableString.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(java.lang.String value, java.nio.ByteBuffer buffer) {
            byte[] arr = value.getBytes(android.hardware.camera2.marshal.impl.MarshalQueryableString.UTF8_CHARSET);
            buffer.put(arr);
            buffer.put(android.hardware.camera2.marshal.impl.MarshalQueryableString.NUL);// metadata strings are NUL-terminated

        }

        @java.lang.Override
        public int calculateMarshalSize(java.lang.String value) {
            byte[] arr = value.getBytes(android.hardware.camera2.marshal.impl.MarshalQueryableString.UTF8_CHARSET);
            return arr.length + 1;// metadata strings are NUL-terminated

        }

        @java.lang.Override
        public java.lang.String unmarshal(java.nio.ByteBuffer buffer) {
            buffer.mark();// save the current position

            boolean foundNull = false;
            int stringLength = 0;
            while (buffer.hasRemaining()) {
                if (buffer.get() == android.hardware.camera2.marshal.impl.MarshalQueryableString.NUL) {
                    foundNull = true;
                    break;
                }
                stringLength++;
            } 
            if (android.hardware.camera2.marshal.impl.MarshalQueryableString.DEBUG) {
                android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableString.TAG, (("unmarshal - scanned " + stringLength) + " characters; found null? ") + foundNull);
            }
            if (!foundNull) {
                throw new java.lang.UnsupportedOperationException("Strings must be null-terminated");
            }
            buffer.reset();// go back to the previously marked position

            byte[] strBytes = new byte[stringLength + 1];
            /* dstOffset */
            buffer.get(strBytes, 0, stringLength + 1);// including null character

            // not including null character
            return /* offset */
            new java.lang.String(strBytes, 0, stringLength, android.hardware.camera2.marshal.impl.MarshalQueryableString.UTF8_CHARSET);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<java.lang.String> createMarshaler(android.hardware.camera2.utils.TypeReference<java.lang.String> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableString.MarshalerString(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<java.lang.String> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_BYTE) && java.lang.String.class.equals(managedType.getType());
    }
}

