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
 * Marshal {@link Range} to/from any native type
 */
public class MarshalQueryableRange<T extends java.lang.Comparable<? super T>> implements android.hardware.camera2.marshal.MarshalQueryable<android.util.Range<T>> {
    private static final int RANGE_COUNT = 2;

    private class MarshalerRange extends android.hardware.camera2.marshal.Marshaler<android.util.Range<T>> {
        private final java.lang.Class<? super android.util.Range<T>> mClass;

        private final java.lang.reflect.Constructor<android.util.Range<T>> mConstructor;

        /**
         * Marshal the {@code T} inside of {@code Range<T>}
         */
        private final android.hardware.camera2.marshal.Marshaler<T> mNestedTypeMarshaler;

        @java.lang.SuppressWarnings("unchecked")
        protected MarshalerRange(android.hardware.camera2.utils.TypeReference<android.util.Range<T>> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableRange.this, typeReference, nativeType);
            mClass = typeReference.getRawType();
            /* Lookup the actual type argument, e.g. Range<Integer> --> Integer
            and then get the marshaler for that managed type.
             */
            java.lang.reflect.ParameterizedType paramType;
            try {
                paramType = ((java.lang.reflect.ParameterizedType) (typeReference.getType()));
            } catch (java.lang.ClassCastException e) {
                throw new java.lang.AssertionError("Raw use of Range is not supported", e);
            }
            java.lang.reflect.Type actualTypeArgument = paramType.getActualTypeArguments()[0];
            android.hardware.camera2.utils.TypeReference<?> actualTypeArgToken = android.hardware.camera2.utils.TypeReference.createSpecializedTypeReference(actualTypeArgument);
            mNestedTypeMarshaler = ((android.hardware.camera2.marshal.Marshaler<T>) (android.hardware.camera2.marshal.MarshalRegistry.getMarshaler(actualTypeArgToken, mNativeType)));
            try {
                mConstructor = ((java.lang.reflect.Constructor<android.util.Range<T>>) (mClass.getConstructor(java.lang.Comparable.class, java.lang.Comparable.class)));
            } catch (java.lang.NoSuchMethodException e) {
                throw new java.lang.AssertionError(e);
            }
        }

        @java.lang.Override
        public void marshal(android.util.Range<T> value, java.nio.ByteBuffer buffer) {
            mNestedTypeMarshaler.marshal(value.getLower(), buffer);
            mNestedTypeMarshaler.marshal(value.getUpper(), buffer);
        }

        @java.lang.Override
        public android.util.Range<T> unmarshal(java.nio.ByteBuffer buffer) {
            T lower = mNestedTypeMarshaler.unmarshal(buffer);
            T upper = mNestedTypeMarshaler.unmarshal(buffer);
            try {
                return mConstructor.newInstance(lower, upper);
            } catch (java.lang.InstantiationException e) {
                throw new java.lang.AssertionError(e);
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.AssertionError(e);
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.AssertionError(e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new java.lang.AssertionError(e);
            }
        }

        @java.lang.Override
        public int getNativeSize() {
            int nestedSize = mNestedTypeMarshaler.getNativeSize();
            if (nestedSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC) {
                return nestedSize * android.hardware.camera2.marshal.impl.MarshalQueryableRange.RANGE_COUNT;
            } else {
                return android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC;
            }
        }

        @java.lang.Override
        public int calculateMarshalSize(android.util.Range<T> value) {
            int nativeSize = getNativeSize();
            if (nativeSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC) {
                return nativeSize;
            } else {
                int lowerSize = mNestedTypeMarshaler.calculateMarshalSize(value.getLower());
                int upperSize = mNestedTypeMarshaler.calculateMarshalSize(value.getUpper());
                return lowerSize + upperSize;
            }
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.util.Range<T>> createMarshaler(android.hardware.camera2.utils.TypeReference<android.util.Range<T>> managedType, int nativeType) {
        return new MarshalerRange(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.util.Range<T>> managedType, int nativeType) {
        return android.util.Range.class.equals(managedType.getRawType());
    }
}

