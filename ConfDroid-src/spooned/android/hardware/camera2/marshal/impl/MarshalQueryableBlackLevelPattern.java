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
 * Marshal {@link BlackLevelPattern} to/from {@link #TYPE_INT32} {@code x 4}
 */
public class MarshalQueryableBlackLevelPattern implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.BlackLevelPattern> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * android.hardware.camera2.params.BlackLevelPattern.COUNT;

    private class MarshalerBlackLevelPattern extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.BlackLevelPattern> {
        protected MarshalerBlackLevelPattern(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.BlackLevelPattern> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.BlackLevelPattern value, java.nio.ByteBuffer buffer) {
            for (int i = 0; i < (android.hardware.camera2.params.BlackLevelPattern.COUNT / 2); ++i) {
                for (int j = 0; j < (android.hardware.camera2.params.BlackLevelPattern.COUNT / 2); ++j) {
                    buffer.putInt(value.getOffsetForIndex(j, i));
                }
            }
        }

        @java.lang.Override
        public android.hardware.camera2.params.BlackLevelPattern unmarshal(java.nio.ByteBuffer buffer) {
            int[] channelOffsets = new int[android.hardware.camera2.params.BlackLevelPattern.COUNT];
            for (int i = 0; i < android.hardware.camera2.params.BlackLevelPattern.COUNT; ++i) {
                channelOffsets[i] = buffer.getInt();
            }
            return new android.hardware.camera2.params.BlackLevelPattern(channelOffsets);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.BlackLevelPattern> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.BlackLevelPattern> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern.MarshalerBlackLevelPattern(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.BlackLevelPattern> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && android.hardware.camera2.params.BlackLevelPattern.class.equals(managedType.getType());
    }
}

