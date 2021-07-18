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
 * Marshal {@link ColorSpaceTransform} to/from {@link #TYPE_RATIONAL}
 */
public class MarshalQueryableColorSpaceTransform implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.ColorSpaceTransform> {
    private static final int ELEMENTS_INT32 = (3 * 3) * (android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_RATIONAL / android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32);

    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.ELEMENTS_INT32;

    /**
     * rational x 3 x 3
     */
    private class MarshalerColorSpaceTransform extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.ColorSpaceTransform> {
        protected MarshalerColorSpaceTransform(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.ColorSpaceTransform> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.ColorSpaceTransform value, java.nio.ByteBuffer buffer) {
            int[] transformAsArray = new int[android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.ELEMENTS_INT32];
            /* offset */
            value.copyElements(transformAsArray, 0);
            for (int i = 0; i < android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.ELEMENTS_INT32; ++i) {
                buffer.putInt(transformAsArray[i]);
            }
        }

        @java.lang.Override
        public android.hardware.camera2.params.ColorSpaceTransform unmarshal(java.nio.ByteBuffer buffer) {
            int[] transformAsArray = new int[android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.ELEMENTS_INT32];
            for (int i = 0; i < android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.ELEMENTS_INT32; ++i) {
                transformAsArray[i] = buffer.getInt();
            }
            return new android.hardware.camera2.params.ColorSpaceTransform(transformAsArray);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.ColorSpaceTransform> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.ColorSpaceTransform> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform.MarshalerColorSpaceTransform(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.ColorSpaceTransform> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_RATIONAL) && android.hardware.camera2.params.ColorSpaceTransform.class.equals(managedType.getType());
    }
}

