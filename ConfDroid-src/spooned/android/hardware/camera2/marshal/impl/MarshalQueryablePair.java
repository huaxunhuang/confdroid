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
 * Marshal {@link Pair} to/from any native type
 */
public class MarshalQueryablePair<T1, T2> implements android.hardware.camera2.marshal.MarshalQueryable<android.util.Pair<T1, T2>> {
    private class MarshalerPair extends android.hardware.camera2.marshal.Marshaler<android.util.Pair<T1, T2>> {
        private final java.lang.Class<? super android.util.Pair<T1, T2>> mClass;

        private final java.lang.reflect.Constructor<android.util.Pair<T1, T2>> mConstructor;

        /**
         * Marshal the {@code T1} inside of {@code Pair<T1, T2>}
         */
        private final android.hardware.camera2.marshal.Marshaler<T1> mNestedTypeMarshalerFirst;

        /**
         * Marshal the {@code T1} inside of {@code Pair<T1, T2>}
         */
        private final android.hardware.camera2.marshal.Marshaler<T2> mNestedTypeMarshalerSecond;

        @java.lang.SuppressWarnings("unchecked")
        protected MarshalerPair(android.hardware.camera2.utils.TypeReference<android.util.Pair<T1, T2>> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryablePair.this, typeReference, nativeType);
            mClass = typeReference.getRawType();
            /* Lookup the actual type arguments, e.g. Pair<Integer, Float> --> [Integer, Float]
            and then get the marshalers for that managed type.
             */
            java.lang.reflect.ParameterizedType paramType;
            try {
                paramType = ((java.lang.reflect.ParameterizedType) (typeReference.getType()));
            } catch (java.lang.ClassCastException e) {
                throw new java.lang.AssertionError("Raw use of Pair is not supported", e);
            }
            // Get type marshaler for T1
            {
                java.lang.reflect.Type actualTypeArgument = paramType.getActualTypeArguments()[0];
                android.hardware.camera2.utils.TypeReference<?> actualTypeArgToken = android.hardware.camera2.utils.TypeReference.createSpecializedTypeReference(actualTypeArgument);
                mNestedTypeMarshalerFirst = ((android.hardware.camera2.marshal.Marshaler<T1>) (android.hardware.camera2.marshal.MarshalRegistry.getMarshaler(actualTypeArgToken, mNativeType)));
            }
            // Get type marshaler for T2
            {
                java.lang.reflect.Type actualTypeArgument = paramType.getActualTypeArguments()[1];
                android.hardware.camera2.utils.TypeReference<?> actualTypeArgToken = android.hardware.camera2.utils.TypeReference.createSpecializedTypeReference(actualTypeArgument);
                mNestedTypeMarshalerSecond = ((android.hardware.camera2.marshal.Marshaler<T2>) (android.hardware.camera2.marshal.MarshalRegistry.getMarshaler(actualTypeArgToken, mNativeType)));
            }
            try {
                mConstructor = ((java.lang.reflect.Constructor<android.util.Pair<T1, T2>>) (mClass.getConstructor(java.lang.Object.class, java.lang.Object.class)));
            } catch (java.lang.NoSuchMethodException e) {
                throw new java.lang.AssertionError(e);
            }
        }

        @java.lang.Override
        public void marshal(android.util.Pair<T1, T2> value, java.nio.ByteBuffer buffer) {
            if (value.first == null) {
                throw new java.lang.UnsupportedOperationException("Pair#first must not be null");
            } else
                if (value.second == null) {
                    throw new java.lang.UnsupportedOperationException("Pair#second must not be null");
                }

            mNestedTypeMarshalerFirst.marshal(value.first, buffer);
            mNestedTypeMarshalerSecond.marshal(value.second, buffer);
        }

        @java.lang.Override
        public android.util.Pair<T1, T2> unmarshal(java.nio.ByteBuffer buffer) {
            T1 first = mNestedTypeMarshalerFirst.unmarshal(buffer);
            T2 second = mNestedTypeMarshalerSecond.unmarshal(buffer);
            try {
                return mConstructor.newInstance(first, second);
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
            int firstSize = mNestedTypeMarshalerFirst.getNativeSize();
            int secondSize = mNestedTypeMarshalerSecond.getNativeSize();
            if ((firstSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC) && (secondSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC)) {
                return firstSize + secondSize;
            } else {
                return android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC;
            }
        }

        @java.lang.Override
        public int calculateMarshalSize(android.util.Pair<T1, T2> value) {
            int nativeSize = getNativeSize();
            if (nativeSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC) {
                return nativeSize;
            } else {
                int firstSize = mNestedTypeMarshalerFirst.calculateMarshalSize(value.first);
                int secondSize = mNestedTypeMarshalerSecond.calculateMarshalSize(value.second);
                return firstSize + secondSize;
            }
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<android.util.Pair<T1, T2>> createMarshaler(android.hardware.camera2.utils.TypeReference<android.util.Pair<T1, T2>> managedType, int nativeType) {
        return new MarshalerPair(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<android.util.Pair<T1, T2>> managedType, int nativeType) {
        return android.util.Pair.class.equals(managedType.getRawType());
    }
}

