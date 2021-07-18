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
 * Marshal any array {@code T}.
 *
 * <p>To marshal any {@code T} to/from a native type, the marshaler for T to/from that native type
 * also has to exist.</p>
 *
 * <p>{@code T} can be either a T2[] where T2 is an object type, or a P[] where P is a
 * built-in primitive (e.g. int[], float[], etc).</p>
 *
 * @param <T>
 * 		the type of the array (e.g. T = int[], or T = Rational[])
 */
public class MarshalQueryableArray<T> implements android.hardware.camera2.marshal.MarshalQueryable<T> {
    private static final java.lang.String TAG = android.hardware.camera2.marshal.impl.MarshalQueryableArray.class.getSimpleName();

    private static final boolean DEBUG = false;

    private class MarshalerArray extends android.hardware.camera2.marshal.Marshaler<T> {
        private final java.lang.Class<T> mClass;

        private final android.hardware.camera2.marshal.Marshaler<?> mComponentMarshaler;

        private final java.lang.Class<?> mComponentClass;

        @java.lang.SuppressWarnings("unchecked")
        protected MarshalerArray(android.hardware.camera2.utils.TypeReference<T> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableArray.this, typeReference, nativeType);
            mClass = ((java.lang.Class<T>) (typeReference.getRawType()));
            android.hardware.camera2.utils.TypeReference<?> componentToken = typeReference.getComponentType();
            mComponentMarshaler = android.hardware.camera2.marshal.MarshalRegistry.getMarshaler(componentToken, mNativeType);
            mComponentClass = componentToken.getRawType();
        }

        @java.lang.Override
        public void marshal(T value, java.nio.ByteBuffer buffer) {
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; ++i) {
                marshalArrayElement(mComponentMarshaler, buffer, value, i);
            }
        }

        @java.lang.Override
        public T unmarshal(java.nio.ByteBuffer buffer) {
            java.lang.Object array;
            int elementSize = mComponentMarshaler.getNativeSize();
            if (elementSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC) {
                int remaining = buffer.remaining();
                int arraySize = remaining / elementSize;
                if ((remaining % elementSize) != 0) {
                    throw new java.lang.UnsupportedOperationException(((((("Arrays for " + mTypeReference) + " must be packed tighly into a multiple of ") + elementSize) + "; but there are ") + (remaining % elementSize)) + " left over bytes");
                }
                if (android.hardware.camera2.marshal.impl.MarshalQueryableArray.DEBUG) {
                    android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableArray.TAG, java.lang.String.format("Attempting to unpack array (count = %d, element size = %d, bytes " + "remaining = %d) for type %s", arraySize, elementSize, remaining, mClass));
                }
                array = java.lang.reflect.Array.newInstance(mComponentClass, arraySize);
                for (int i = 0; i < arraySize; ++i) {
                    java.lang.Object elem = mComponentMarshaler.unmarshal(buffer);
                    java.lang.reflect.Array.set(array, i, elem);
                }
            } else {
                // Dynamic size, use an array list.
                java.util.ArrayList<java.lang.Object> arrayList = new java.util.ArrayList<java.lang.Object>();
                // Assumes array is packed tightly; no unused bytes allowed
                while (buffer.hasRemaining()) {
                    java.lang.Object elem = mComponentMarshaler.unmarshal(buffer);
                    arrayList.add(elem);
                } 
                int arraySize = arrayList.size();
                array = copyListToArray(arrayList, java.lang.reflect.Array.newInstance(mComponentClass, arraySize));
            }
            if (buffer.remaining() != 0) {
                android.util.Log.e(android.hardware.camera2.marshal.impl.MarshalQueryableArray.TAG, (("Trailing bytes (" + buffer.remaining()) + ") left over after unpacking ") + mClass);
            }
            return mClass.cast(array);
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC;
        }

        @java.lang.Override
        public int calculateMarshalSize(T value) {
            int elementSize = mComponentMarshaler.getNativeSize();
            int arrayLength = java.lang.reflect.Array.getLength(value);
            if (elementSize != android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC) {
                // The fast way. Every element size is uniform.
                return elementSize * arrayLength;
            } else {
                // The slow way. Accumulate size for each element.
                int size = 0;
                for (int i = 0; i < arrayLength; ++i) {
                    size += calculateElementMarshalSize(mComponentMarshaler, value, i);
                }
                return size;
            }
        }

        /* Helpers to avoid compiler errors regarding types with wildcards (?) */
        @java.lang.SuppressWarnings("unchecked")
        private <TElem> void marshalArrayElement(android.hardware.camera2.marshal.Marshaler<TElem> marshaler, java.nio.ByteBuffer buffer, java.lang.Object array, int index) {
            marshaler.marshal(((TElem) (java.lang.reflect.Array.get(array, index))), buffer);
        }

        @java.lang.SuppressWarnings("unchecked")
        private java.lang.Object copyListToArray(java.util.ArrayList<?> arrayList, java.lang.Object arrayDest) {
            return arrayList.toArray(((T[]) (arrayDest)));
        }

        @java.lang.SuppressWarnings("unchecked")
        private <TElem> int calculateElementMarshalSize(android.hardware.camera2.marshal.Marshaler<TElem> marshaler, java.lang.Object array, int index) {
            java.lang.Object elem = java.lang.reflect.Array.get(array, index);
            return marshaler.calculateMarshalSize(((TElem) (elem)));
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<T> createMarshaler(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        return new MarshalerArray(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        // support both ConcreteType[] and GenericType<ConcreteType>[]
        return managedType.getRawType().isArray();
        // TODO: Should this recurse deeper and check that there is
        // a valid marshaler for the ConcreteType as well?
    }
}

