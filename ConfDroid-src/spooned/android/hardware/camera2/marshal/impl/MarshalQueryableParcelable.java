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
 * Marshal any {@code T extends Parcelable} to/from any native type
 *
 * <p>Use with extreme caution! File descriptors and binders will not be marshaled across.</p>
 */
public class MarshalQueryableParcelable<T extends android.os.Parcelable> implements android.hardware.camera2.marshal.MarshalQueryable<T> {
    private static final java.lang.String TAG = "MarshalParcelable";

    private static final boolean DEBUG = false;

    private static final java.lang.String FIELD_CREATOR = "CREATOR";

    private class MarshalerParcelable extends android.hardware.camera2.marshal.Marshaler<T> {
        private final java.lang.Class<T> mClass;

        private final android.os.Parcelable.Creator<T> mCreator;

        @java.lang.SuppressWarnings("unchecked")
        protected MarshalerParcelable(android.hardware.camera2.utils.TypeReference<T> typeReference, int nativeType) {
            super(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.this, typeReference, nativeType);
            mClass = ((java.lang.Class<T>) (typeReference.getRawType()));
            java.lang.reflect.Field creatorField;
            try {
                creatorField = mClass.getDeclaredField(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.FIELD_CREATOR);
            } catch (java.lang.NoSuchFieldException e) {
                // Impossible. All Parcelable implementations must have a 'CREATOR' static field
                throw new java.lang.AssertionError(e);
            }
            try {
                mCreator = ((android.os.Parcelable.Creator<T>) (creatorField.get(null)));
            } catch (java.lang.IllegalAccessException e) {
                // Impossible: All 'CREATOR' static fields must be public
                throw new java.lang.AssertionError(e);
            } catch (java.lang.IllegalArgumentException e) {
                // Impossible: This is a static field, so null must be ok
                throw new java.lang.AssertionError(e);
            }
        }

        @java.lang.Override
        public void marshal(T value, java.nio.ByteBuffer buffer) {
            if (android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.DEBUG) {
                android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.TAG, "marshal " + value);
            }
            android.os.Parcel parcel = android.os.Parcel.obtain();
            byte[] parcelContents;
            try {
                /* flags */
                value.writeToParcel(parcel, 0);
                if (parcel.hasFileDescriptors()) {
                    throw new java.lang.UnsupportedOperationException(("Parcelable " + value) + " must not have file descriptors");
                }
                parcelContents = parcel.marshall();
            } finally {
                parcel.recycle();
            }
            if (parcelContents.length == 0) {
                throw new java.lang.AssertionError("No data marshaled for " + value);
            }
            buffer.put(parcelContents);
        }

        @java.lang.Override
        public T unmarshal(java.nio.ByteBuffer buffer) {
            if (android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.DEBUG) {
                android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.TAG, "unmarshal, buffer remaining " + buffer.remaining());
            }
            /* Quadratically slow when marshaling an array of parcelables.

            Read out the entire byte buffer as an array, then copy it into the parcel.

            Once we unparcel the entire object, advance the byte buffer by only how many
            bytes the parcel actually used up.

            Future: If we ever do need to use parcelable arrays, we can do this a little smarter
            by reading out a chunk like 4,8,16,24 each time, but not sure how to detect
            parcels being too short in this case.

            Future: Alternatively use Parcel#obtain(long) directly into the native
            pointer of a ByteBuffer, which would not copy if the ByteBuffer was direct.
             */
            buffer.mark();
            android.os.Parcel parcel = android.os.Parcel.obtain();
            try {
                int maxLength = buffer.remaining();
                byte[] remaining = new byte[maxLength];
                buffer.get(remaining);
                /* offset */
                parcel.unmarshall(remaining, 0, maxLength);
                /* pos */
                parcel.setDataPosition(0);
                T value = mCreator.createFromParcel(parcel);
                int actualLength = parcel.dataPosition();
                if (actualLength == 0) {
                    throw new java.lang.AssertionError("No data marshaled for " + value);
                }
                // set the position past the bytes the parcelable actually used
                buffer.reset();
                buffer.position(buffer.position() + actualLength);
                if (android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.DEBUG) {
                    android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.TAG, "unmarshal, parcel length was " + actualLength);
                    android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.TAG, "unmarshal, value is " + value);
                }
                return mClass.cast(value);
            } finally {
                parcel.recycle();
            }
        }

        @java.lang.Override
        public int getNativeSize() {
            return android.hardware.camera2.marshal.Marshaler.NATIVE_SIZE_DYNAMIC;
        }

        @java.lang.Override
        public int calculateMarshalSize(T value) {
            android.os.Parcel parcel = android.os.Parcel.obtain();
            try {
                /* flags */
                value.writeToParcel(parcel, 0);
                int length = parcel.marshall().length;
                if (android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.DEBUG) {
                    android.util.Log.v(android.hardware.camera2.marshal.impl.MarshalQueryableParcelable.TAG, (("calculateMarshalSize, length when parceling " + value) + " is ") + length);
                }
                return length;
            } finally {
                parcel.recycle();
            }
        }
    }

    @java.lang.Override
    public android.hardware.camera2.marshal.Marshaler<T> createMarshaler(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        return new MarshalerParcelable(managedType, nativeType);
    }

    @java.lang.Override
    public boolean isTypeMappingSupported(android.hardware.camera2.utils.TypeReference<T> managedType, int nativeType) {
        return android.os.Parcelable.class.isAssignableFrom(managedType.getRawType());
    }
}

