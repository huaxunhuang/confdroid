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
 * Marshal {@link MeteringRectangle} to/from {@link #TYPE_INT32}
 */
public class MarshalQueryableMeteringRectangle implements android.hardware.camera2.marshal.MarshalQueryable<android.hardware.camera2.params.MeteringRectangle> {
    private static final int SIZE = android.hardware.camera2.marshal.MarshalHelpers.SIZEOF_INT32 * 5;

    /**
     * (xmin, ymin, xmax, ymax, weight)
     */
    private class MarshalerMeteringRectangle extends android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.MeteringRectangle> {
        protected MarshalerMeteringRectangle(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.MeteringRectangle> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle.this, typeReference, nativeType);
        }

        @java.lang.Override
        public void marshal(android.hardware.camera2.params.MeteringRectangle value, java.nio.ByteBuffer buffer) {
            int xMin = value.getX();
            int yMin = value.getY();
            int xMax = xMin + value.getWidth();
            int yMax = yMin + value.getHeight();
            int weight = value.getMeteringWeight();
            buffer.putInt(xMin);
            buffer.putInt(yMin);
            buffer.putInt(xMax);
            buffer.putInt(yMax);
            buffer.putInt(weight);
        }

        @java.lang.Override
        public android.hardware.camera2.params.MeteringRectangle unmarshal(java.nio.ByteBuffer buffer) {
            int xMin = buffer.getInt();
            int yMin = buffer.getInt();
            int xMax = buffer.getInt();
            int yMax = buffer.getInt();
            int weight = buffer.getInt();
            int width = xMax - xMin;
            int height = yMax - yMin;
            return new android.hardware.camera2.params.MeteringRectangle(xMin, yMin, width, height, weight);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle.SIZE;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.hardware.camera2.params.MeteringRectangle> createMarshaler(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.MeteringRectangle> managedType, int nativeType) {
        return new android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle.MarshalerMeteringRectangle(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.hardware.camera2.params.MeteringRectangle> managedType, int nativeType) {
        return (nativeType == android.hardware.camera2.impl.CameraMetadataNative.TYPE_INT32) && android.hardware.camera2.params.MeteringRectangle.class.equals(managedType.getType());
    }
}

