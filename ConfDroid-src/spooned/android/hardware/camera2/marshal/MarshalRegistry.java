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
package android.hardware.camera2.marshal;


/**
 * Registry of supported marshalers; add new query-able marshalers or lookup existing ones.</p>
 */
public class MarshalRegistry {
    /**
     * Register a marshal queryable for the managed type {@code T}.
     *
     * <p>Multiple marshal queryables for the same managed type {@code T} may be registered;
     * this is desirable if they support different native types (e.g. marshaler 1 supports
     * {@code Integer <-> TYPE_INT32}, marshaler 2 supports {@code Integer <-> TYPE_BYTE}.</p>
     *
     * @param queryable
     * 		a non-{@code null} marshal queryable that supports marshaling {@code T}
     */
    public static <T> void registerMarshalQueryable(android.hardware.camera2.marshal.MarshalQueryable<T> queryable) {
        synchronized(android.hardware.camera2.marshal.MarshalRegistry.sMarshalLock) {
            android.hardware.camera2.marshal.MarshalRegistry.sRegisteredMarshalQueryables.add(queryable);
        }
    }

    /**
     * Lookup a marshaler between {@code T} and {@code nativeType}.
     *
     * <p>Marshalers are looked up in the order they were registered; earlier registered
     * marshal queriers get priority.</p>
     *
     * @param typeToken
     * 		The compile-time type reference for {@code T}
     * @param nativeType
     * 		The native type, e.g. {@link CameraMetadataNative#TYPE_BYTE TYPE_BYTE}
     * @return marshaler a non-{@code null} marshaler that supports marshaling the type combo
     * @throws UnsupportedOperationException
     * 		If no marshaler matching the args could be found
     */
    @java.lang.SuppressWarnings("unchecked")
    public static <T> android.hardware.camera2.marshal.Marshaler<T> getMarshaler(android.hardware.camera2.utils.TypeReference<T> typeToken, int nativeType) {
        synchronized(android.hardware.camera2.marshal.MarshalRegistry.sMarshalLock) {
            // TODO: can avoid making a new token each time by code-genning
            // the list of type tokens and native types from the keys (at the call sites)
            android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<T> marshalToken = new android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<T>(typeToken, nativeType);
            /* Marshalers are instantiated lazily once they are looked up; successive lookups
            will not instantiate new marshalers.
             */
            android.hardware.camera2.marshal.Marshaler<T> marshaler = ((android.hardware.camera2.marshal.Marshaler<T>) (android.hardware.camera2.marshal.MarshalRegistry.sMarshalerMap.get(marshalToken)));
            if (marshaler == null) {
                if (android.hardware.camera2.marshal.MarshalRegistry.sRegisteredMarshalQueryables.size() == 0) {
                    throw new java.lang.AssertionError("No available query marshalers registered");
                }
                // Query each marshaler to see if they support the native/managed type combination
                for (android.hardware.camera2.marshal.MarshalQueryable<?> potentialMarshaler : android.hardware.camera2.marshal.MarshalRegistry.sRegisteredMarshalQueryables) {
                    android.hardware.camera2.marshal.MarshalQueryable<T> castedPotential = ((android.hardware.camera2.marshal.MarshalQueryable<T>) (potentialMarshaler));
                    if (castedPotential.isTypeMappingSupported(typeToken, nativeType)) {
                        marshaler = castedPotential.createMarshaler(typeToken, nativeType);
                        break;
                    }
                }
                if (marshaler == null) {
                    throw new java.lang.UnsupportedOperationException(((("Could not find marshaler that matches the requested " + "combination of type reference ") + typeToken) + " and native type ") + android.hardware.camera2.marshal.MarshalHelpers.toStringNativeType(nativeType));
                }
                // Only put when no cached version exists to avoid +0.5ms lookup per call.
                android.hardware.camera2.marshal.MarshalRegistry.sMarshalerMap.put(marshalToken, marshaler);
            }
            return marshaler;
        }
    }

    private static class MarshalToken<T> {
        public MarshalToken(android.hardware.camera2.utils.TypeReference<T> typeReference, int nativeType) {
            this.typeReference = typeReference;
            this.nativeType = nativeType;
            this.hash = typeReference.hashCode() ^ nativeType;
        }

        final android.hardware.camera2.utils.TypeReference<T> typeReference;

        final int nativeType;

        private final int hash;

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            if (other instanceof android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<?>) {
                android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<?> otherToken = ((android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<?>) (other));
                return typeReference.equals(otherToken.typeReference) && (nativeType == otherToken.nativeType);
            }
            return false;
        }

        @java.lang.Override
        public int hashCode() {
            return hash;
        }
    }

    // Control access to the static data structures below
    private static final java.lang.Object sMarshalLock = new java.lang.Object();

    private static final java.util.List<android.hardware.camera2.marshal.MarshalQueryable<?>> sRegisteredMarshalQueryables = new java.util.ArrayList<android.hardware.camera2.marshal.MarshalQueryable<?>>();

    private static final java.util.HashMap<android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<?>, android.hardware.camera2.marshal.Marshaler<?>> sMarshalerMap = new java.util.HashMap<android.hardware.camera2.marshal.MarshalRegistry.MarshalToken<?>, android.hardware.camera2.marshal.Marshaler<?>>();

    private MarshalRegistry() {
        throw new java.lang.AssertionError();
    }
}

