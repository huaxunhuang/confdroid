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
 * Marshaler for {@code android.scaler.availableInputOutputFormatsMap} custom class
 * {@link ReprocessFormatsMap}
 */
public class MarshalQueryableReprocessFormatsMap implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.ReprocessFormatsMap> {
    private class MarshalerReprocessFormatsMap extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.ReprocessFormatsMap> {
        protected MarshalerReprocessFormatsMap(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.ReprocessFormatsMap> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableReprocessFormatsMap.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.ReprocessFormatsMap value, java.nio.ByteBuffer buffer) {
            /* // writing (static example, DNG+ZSL)
            int32_t[] contents = {
              RAW_OPAQUE, 3, RAW16, YUV_420_888, BLOB,
              RAW16, 2, YUV_420_888, BLOB,
              ...,
              INPUT_FORMAT, OUTPUT_FORMAT_COUNT, [OUTPUT_0, OUTPUT_1, ..., OUTPUT_FORMAT_COUNT-1]
            };
             */
            int[] inputs = android.hardware.camera2.params.StreamConfigurationMap.imageFormatToInternal(value.getInputs());
            for (int input : inputs) {
                // INPUT_FORMAT
                buffer.putInt(input);
                int[] outputs = android.hardware.camera2.params.StreamConfigurationMap.imageFormatToInternal(value.getOutputs(input));
                // OUTPUT_FORMAT_COUNT
                buffer.putInt(outputs.length);
                // [OUTPUT_0, OUTPUT_1, ..., OUTPUT_FORMAT_COUNT-1]
                for (int output : outputs) {
                    buffer.putInt(output);
                }
            }
        }

        @java.lang.Override
        public android.hardware.camera2.params.ReprocessFormatsMap unmarshal(java.nio.ByteBuffer buffer) {
            int len = buffer.remaining() / android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32;
            if ((buffer.remaining() % android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32) != 0) {
                throw new java.lang.AssertionError("ReprocessFormatsMap was not TYPE_INT32");
            }
            int[] entries = new int[len];
            java.nio.IntBuffer intBuffer = buffer.asIntBuffer();
            intBuffer.get(entries);
            // TODO: consider moving rest of parsing code from ReprocessFormatsMap to here
            return new android.hardware.camera2.params.ReprocessFormatsMap(entries);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC;
        }

        @java.lang.Override
        public int calculateMarshalSize(android.hardware.camera2.params.ReprocessFormatsMap value) {
            /* // writing (static example, DNG+ZSL)
            int32_t[] contents = {
              RAW_OPAQUE, 3, RAW16, YUV_420_888, BLOB,
              RAW16, 2, YUV_420_888, BLOB,
              ...,
              INPUT_FORMAT, OUTPUT_FORMAT_COUNT, [OUTPUT_0, OUTPUT_1, ..., OUTPUT_FORMAT_COUNT-1]
            };
             */
            int length = 0;
            int[] inputs = value.getInputs();
            for (int input : inputs) {
                length += 1;// INPUT_FORMAT

                length += 1;// OUTPUT_FORMAT_COUNT

                int[] outputs = value.getOutputs(input);
                length += outputs.length;// [OUTPUT_0, OUTPUT_1, ..., OUTPUT_FORMAT_COUNT-1]

            }
            return length * android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.ReprocessFormatsMap> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.ReprocessFormatsMap> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableReprocessFormatsMap.MarshalerReprocessFormatsMap(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.ReprocessFormatsMap> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && managedType.getType().equals(android.hardware.camera2.params.ReprocessFormatsMap.class);
    }
}

