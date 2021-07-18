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
package android.hardware.camera2.utils;


/**
 * Super type token; allows capturing generic types at runtime by forcing them to be reified.
 *
 * <p>Usage example: <pre>{@code // using anonymous classes (preferred)
 *      TypeReference&lt;Integer> intToken = new TypeReference&lt;Integer>() {{}};
 *
 *      // using named classes
 *      class IntTypeReference extends TypeReference&lt;Integer> {...}
 *      TypeReference&lt;Integer> intToken = new IntTypeReference();
 * }</p></pre>
 *
 * <p>Unlike the reference implementation, this bans nested TypeVariables; that is all
 * dynamic types must equal to the static types.</p>
 *
 * <p>See <a href="http://gafter.blogspot.com/2007/05/limitation-of-super-type-tokens.html">
 * http://gafter.blogspot.com/2007/05/limitation-of-super-type-tokens.html</a>
 * for more details.</p>
 */
public abstract class TypeReference<T> {
    private final java.lang.reflect.Type mType;

    private final int mHash;

    /**
     * Create a new type reference for {@code T}.
     *
     * @throws IllegalArgumentException
     * 		if {@code T}'s actual type contains a type variable
     * @see TypeReference
     */
    protected TypeReference() {
        java.lang.reflect.ParameterizedType thisType = ((java.lang.reflect.ParameterizedType) (getClass().getGenericSuperclass()));
        // extract the "T" from TypeReference<T>
        mType = thisType.getActualTypeArguments()[0];
        /* Prohibit type references with type variables such as

           class GenericListToken<T> extends TypeReference<List<T>>

        Since the "T" there is not known without an instance of T, type equality would
        consider *all* Lists equal regardless of T. Allowing this would defeat
        some of the type safety of a type reference.
         */
        if (android.hardware.camera2.utils.TypeReference.containsTypeVariable(mType)) {
            throw new java.lang.IllegalArgumentException("Including a type variable in a type reference is not allowed");
        }
        mHash = mType.hashCode();
    }

    /**
     * Return the dynamic {@link Type} corresponding to the captured type {@code T}.
     */
    public java.lang.reflect.Type getType() {
        return mType;
    }

    private TypeReference(java.lang.reflect.Type type) {
        mType = type;
        if (android.hardware.camera2.utils.TypeReference.containsTypeVariable(mType)) {
            throw new java.lang.IllegalArgumentException("Including a type variable in a type reference is not allowed");
        }
        mHash = mType.hashCode();
    }

    private static class SpecializedTypeReference<T> extends android.hardware.camera2.utils.TypeReference<T> {
        public SpecializedTypeReference(java.lang.Class<T> klass) {
            super(klass);
        }
    }

    @java.lang.SuppressWarnings("rawtypes")
    private static class SpecializedBaseTypeReference extends android.hardware.camera2.utils.TypeReference {
        public SpecializedBaseTypeReference(java.lang.reflect.Type type) {
            super(type);
        }
    }

    /**
     * Create a specialized type reference from a dynamic class instance,
     * bypassing the standard compile-time checks.
     *
     * <p>As with a regular type reference, the {@code klass} must not contain
     * any type variables.</p>
     *
     * @param klass
     * 		a non-{@code null} {@link Class} instance
     * @return a type reference which captures {@code T} at runtime
     * @throws IllegalArgumentException
     * 		if {@code T} had any type variables
     */
    public static <T> android.hardware.camera2.utils.TypeReference<T> createSpecializedTypeReference(java.lang.Class<T> klass) {
        return new android.hardware.camera2.utils.TypeReference.SpecializedTypeReference<T>(klass);
    }

    /**
     * Create a specialized type reference from a dynamic {@link Type} instance,
     * bypassing the standard compile-time checks.
     *
     * <p>As with a regular type reference, the {@code type} must not contain
     * any type variables.</p>
     *
     * @param type
     * 		a non-{@code null} {@link Type} instance
     * @return a type reference which captures {@code T} at runtime
     * @throws IllegalArgumentException
     * 		if {@code type} had any type variables
     */
    public static android.hardware.camera2.utils.TypeReference<?> createSpecializedTypeReference(java.lang.reflect.Type type) {
        return new android.hardware.camera2.utils.TypeReference.SpecializedBaseTypeReference(type);
    }

    /**
     * Returns the raw type of T.
     *
     * <p><ul>
     * <li>If T is a Class itself, T itself is returned.
     * <li>If T is a ParameterizedType, the raw type of the parameterized type is returned.
     * <li>If T is a GenericArrayType, the returned type is the corresponding array class.
     * For example: {@code List<Integer>[]} => {@code List[]}.
     * <li>If T is a type variable or a wildcard type, the raw type of the first upper bound is
     * returned. For example: {@code <X extends Foo>} => {@code Foo}.
     * </ul>
     *
     * @return the raw type of {@code T}
     */
    @java.lang.SuppressWarnings("unchecked")
    public final java.lang.Class<? super T> getRawType() {
        return ((java.lang.Class<? super T>) (android.hardware.camera2.utils.TypeReference.getRawType(mType)));
    }

    private static final java.lang.Class<?> getRawType(java.lang.reflect.Type type) {
        if (type == null) {
            throw new java.lang.NullPointerException("type must not be null");
        }
        if (type instanceof java.lang.Class<?>) {
            return ((java.lang.Class<?>) (type));
        } else
            if (type instanceof java.lang.reflect.ParameterizedType) {
                return ((java.lang.Class<?>) (((java.lang.reflect.ParameterizedType) (type)).getRawType()));
            } else
                if (type instanceof java.lang.reflect.GenericArrayType) {
                    return android.hardware.camera2.utils.TypeReference.getArrayClass(android.hardware.camera2.utils.TypeReference.getRawType(((java.lang.reflect.GenericArrayType) (type)).getGenericComponentType()));
                } else
                    if (type instanceof java.lang.reflect.WildcardType) {
                        // Should be at most 1 upper bound, but treat it like an array for simplicity
                        return android.hardware.camera2.utils.TypeReference.getRawType(((java.lang.reflect.WildcardType) (type)).getUpperBounds());
                    } else
                        if (type instanceof java.lang.reflect.TypeVariable) {
                            throw new java.lang.AssertionError("Type variables are not allowed in type references");
                        } else {
                            // Impossible
                            throw new java.lang.AssertionError("Unhandled branch to get raw type for type " + type);
                        }




    }

    private static final java.lang.Class<?> getRawType(java.lang.reflect.Type[] types) {
        if (types == null) {
            return null;
        }
        for (java.lang.reflect.Type type : types) {
            java.lang.Class<?> klass = android.hardware.camera2.utils.TypeReference.getRawType(type);
            if (klass != null) {
                return klass;
            }
        }
        return null;
    }

    private static final java.lang.Class<?> getArrayClass(java.lang.Class<?> componentType) {
        return java.lang.reflect.Array.newInstance(componentType, 0).getClass();
    }

    /**
     * Get the component type, e.g. {@code T} from {@code T[]}.
     *
     * @return component type, or {@code null} if {@code T} is not an array
     */
    public android.hardware.camera2.utils.TypeReference<?> getComponentType() {
        java.lang.reflect.Type componentType = android.hardware.camera2.utils.TypeReference.getComponentType(mType);
        return componentType != null ? android.hardware.camera2.utils.TypeReference.createSpecializedTypeReference(componentType) : null;
    }

    private static java.lang.reflect.Type getComponentType(java.lang.reflect.Type type) {
        android.hardware.camera2.utils.TypeReference.checkNotNull(type, "type must not be null");
        if (type instanceof java.lang.Class<?>) {
            return ((java.lang.Class<?>) (type)).getComponentType();
        } else
            if (type instanceof java.lang.reflect.ParameterizedType) {
                return null;
            } else
                if (type instanceof java.lang.reflect.GenericArrayType) {
                    return ((java.lang.reflect.GenericArrayType) (type)).getGenericComponentType();
                } else
                    if (type instanceof java.lang.reflect.WildcardType) {
                        // Should be at most 1 upper bound, but treat it like an array for simplicity
                        throw new java.lang.UnsupportedOperationException("TODO: support wild card components");
                    } else
                        if (type instanceof java.lang.reflect.TypeVariable) {
                            throw new java.lang.AssertionError("Type variables are not allowed in type references");
                        } else {
                            // Impossible
                            throw new java.lang.AssertionError("Unhandled branch to get component type for type " + type);
                        }




    }

    /**
     * Compare two objects for equality.
     *
     * <p>A TypeReference is only equal to another TypeReference if their captured type {@code T}
     * is also equal.</p>
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        // Note that this comparison could inaccurately return true when comparing types
        // with nested type variables; therefore we ban type variables in the constructor.
        return (o instanceof android.hardware.camera2.utils.TypeReference<?>) && mType.equals(((android.hardware.camera2.utils.TypeReference<?>) (o)).mType);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int hashCode() {
        return mHash;
    }

    /**
     * Check if the {@code type} contains a {@link TypeVariable} recursively.
     *
     * <p>Intuitively, a type variable is a type in a type expression that refers to a generic
     * type which is not known at the definition of the expression (commonly seen when
     * type parameters are used, e.g. {@code class Foo<T>}).</p>
     *
     * <p>See <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.4">
     * http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.4</a>
     * for a more formal definition of a type variable</p>.
     *
     * @param type
     * 		a type object ({@code null} is allowed)
     * @return {@code true} if there were nested type variables; {@code false} otherwise
     */
    public static boolean containsTypeVariable(java.lang.reflect.Type type) {
        if (type == null) {
            // Trivially false
            return false;
        } else
            if (type instanceof java.lang.reflect.TypeVariable<?>) {
                /* T -> trivially true */
                return true;
            } else
                if (type instanceof java.lang.Class<?>) {
                    /* class Foo -> no type variable
                    class Foo<T> - has a type variable

                    This also covers the case of class Foo<T> extends ... / implements ...
                    since everything on the right hand side would either include a type variable T
                    or have no type variables.
                     */
                    java.lang.Class<?> klass = ((java.lang.Class<?>) (type));
                    // Empty array => class is not generic
                    if (klass.getTypeParameters().length != 0) {
                        return true;
                    } else {
                        // Does the outer class(es) contain any type variables?
                        /* class Outer<T> {
                          class Inner {
                             T field;
                          }
                        }

                        In this case 'Inner' has no type parameters itself, but it still has a type
                        variable as part of the type definition.
                         */
                        return android.hardware.camera2.utils.TypeReference.containsTypeVariable(klass.getDeclaringClass());
                    }
                } else
                    if (type instanceof java.lang.reflect.ParameterizedType) {
                        /* This is the "Foo<T1, T2, T3, ... Tn>" in the scope of a

                             // no type variables here, T1-Tn are known at this definition
                             class X extends Foo<T1, T2, T3, ... Tn>

                             // T1 is a type variable, T2-Tn are known at this definition
                             class X<T1> extends Foo<T1, T2, T3, ... Tn>
                         */
                        java.lang.reflect.ParameterizedType p = ((java.lang.reflect.ParameterizedType) (type));
                        // This needs to be recursively checked
                        for (java.lang.reflect.Type arg : p.getActualTypeArguments()) {
                            if (android.hardware.camera2.utils.TypeReference.containsTypeVariable(arg)) {
                                return true;
                            }
                        }
                        return false;
                    } else
                        if (type instanceof java.lang.reflect.WildcardType) {
                            java.lang.reflect.WildcardType wild = ((java.lang.reflect.WildcardType) (type));
                            /* This is is the "?" inside of a

                                  Foo<?> --> unbounded; trivially no type variables
                                  Foo<? super T> --> lower bound; does T have a type variable?
                                  Foo<? extends T> --> upper bound; does T have a type variable?
                             */
                            /* According to JLS 4.5.1
                             (http://java.sun.com/docs/books/jls/third_edition/html/typesValues.html#4.5.1):

                             - More than 1 lower/upper bound is illegal
                             - Both a lower and upper bound is illegal

                             However, we use this 'array OR array' approach for readability
                             */
                            return android.hardware.camera2.utils.TypeReference.containsTypeVariable(wild.getLowerBounds()) || android.hardware.camera2.utils.TypeReference.containsTypeVariable(wild.getUpperBounds());
                        }




        return false;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("TypeReference<");
        android.hardware.camera2.utils.TypeReference.toString(getType(), builder);
        builder.append(">");
        return builder.toString();
    }

    private static void toString(java.lang.reflect.Type type, java.lang.StringBuilder out) {
        if (type == null) {
            return;
        } else
            if (type instanceof java.lang.reflect.TypeVariable<?>) {
                // T
                out.append(((java.lang.reflect.TypeVariable<?>) (type)).getName());
            } else
                if (type instanceof java.lang.Class<?>) {
                    java.lang.Class<?> klass = ((java.lang.Class<?>) (type));
                    out.append(klass.getName());
                    android.hardware.camera2.utils.TypeReference.toString(klass.getTypeParameters(), out);
                } else
                    if (type instanceof java.lang.reflect.ParameterizedType) {
                        // "Foo<T1, T2, T3, ... Tn>"
                        java.lang.reflect.ParameterizedType p = ((java.lang.reflect.ParameterizedType) (type));
                        out.append(((java.lang.Class<?>) (p.getRawType())).getName());
                        android.hardware.camera2.utils.TypeReference.toString(p.getActualTypeArguments(), out);
                    } else
                        if (type instanceof java.lang.reflect.GenericArrayType) {
                            java.lang.reflect.GenericArrayType gat = ((java.lang.reflect.GenericArrayType) (type));
                            android.hardware.camera2.utils.TypeReference.toString(gat.getGenericComponentType(), out);
                            out.append("[]");
                        } else {
                            // WildcardType, BoundedType
                            // TODO:
                            out.append(type.toString());
                        }




    }

    private static void toString(java.lang.reflect.Type[] types, java.lang.StringBuilder out) {
        if (types == null) {
            return;
        } else
            if (types.length == 0) {
                return;
            }

        out.append("<");
        for (int i = 0; i < types.length; ++i) {
            android.hardware.camera2.utils.TypeReference.toString(types[i], out);
            if (i != (types.length - 1)) {
                out.append(", ");
            }
        }
        out.append(">");
    }

    /**
     * Check if any of the elements in this array contained a type variable.
     *
     * <p>Empty and null arrays trivially have no type variables.</p>
     *
     * @param typeArray
     * 		an array ({@code null} is ok) of types
     * @return true if any elements contained a type variable; false otherwise
     */
    private static boolean containsTypeVariable(java.lang.reflect.Type[] typeArray) {
        if (typeArray == null) {
            return false;
        }
        for (java.lang.reflect.Type type : typeArray) {
            if (android.hardware.camera2.utils.TypeReference.containsTypeVariable(type)) {
                return true;
            }
        }
        return false;
    }
}

