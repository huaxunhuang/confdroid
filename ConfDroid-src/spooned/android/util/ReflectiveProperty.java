/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.util;


/**
 * Internal class to automatically generate a Property for a given class/name pair, given the
 * specification of {@link Property#of(java.lang.Class, java.lang.Class, java.lang.String)}
 */
class ReflectiveProperty<T, V> extends android.util.Property<T, V> {
    private static final java.lang.String PREFIX_GET = "get";

    private static final java.lang.String PREFIX_IS = "is";

    private static final java.lang.String PREFIX_SET = "set";

    private java.lang.reflect.Method mSetter;

    private java.lang.reflect.Method mGetter;

    private java.lang.reflect.Field mField;

    /**
     * For given property name 'name', look for getName/isName method or 'name' field.
     * Also look for setName method (optional - could be readonly). Failing method getters and
     * field results in throwing NoSuchPropertyException.
     *
     * @param propertyHolder
     * 		The class on which the methods or field are found
     * @param name
     * 		The name of the property, where this name is capitalized and appended to
     * 		"get" and "is to search for the appropriate methods. If the get/is methods are not found,
     * 		the constructor will search for a field with that exact name.
     */
    public ReflectiveProperty(java.lang.Class<T> propertyHolder, java.lang.Class<V> valueType, java.lang.String name) {
        // TODO: cache reflection info for each new class/name pair
        super(valueType, name);
        char firstLetter = java.lang.Character.toUpperCase(name.charAt(0));
        java.lang.String theRest = name.substring(1);
        java.lang.String capitalizedName = firstLetter + theRest;
        java.lang.String getterName = android.util.ReflectiveProperty.PREFIX_GET + capitalizedName;
        try {
            mGetter = propertyHolder.getMethod(getterName, ((java.lang.Class<?>[]) (null)));
        } catch (java.lang.NoSuchMethodException e) {
            // getName() not available - try isName() instead
            getterName = android.util.ReflectiveProperty.PREFIX_IS + capitalizedName;
            try {
                mGetter = propertyHolder.getMethod(getterName, ((java.lang.Class<?>[]) (null)));
            } catch (java.lang.NoSuchMethodException e1) {
                // Try public field instead
                try {
                    mField = propertyHolder.getField(name);
                    java.lang.Class fieldType = mField.getType();
                    if (!typesMatch(valueType, fieldType)) {
                        throw new android.util.NoSuchPropertyException((((("Underlying type (" + fieldType) + ") ") + "does not match Property type (") + valueType) + ")");
                    }
                    return;
                } catch (java.lang.NoSuchFieldException e2) {
                    // no way to access property - throw appropriate exception
                    throw new android.util.NoSuchPropertyException(("No accessor method or field found for" + " property with name ") + name);
                }
            }
        }
        java.lang.Class getterType = mGetter.getReturnType();
        // Check to make sure our getter type matches our valueType
        if (!typesMatch(valueType, getterType)) {
            throw new android.util.NoSuchPropertyException((((("Underlying type (" + getterType) + ") ") + "does not match Property type (") + valueType) + ")");
        }
        java.lang.String setterName = android.util.ReflectiveProperty.PREFIX_SET + capitalizedName;
        try {
            mSetter = propertyHolder.getMethod(setterName, getterType);
        } catch (java.lang.NoSuchMethodException ignored) {
            // Okay to not have a setter - just a readonly property
        }
    }

    /**
     * Utility method to check whether the type of the underlying field/method on the target
     * object matches the type of the Property. The extra checks for primitive types are because
     * generics will force the Property type to be a class, whereas the type of the underlying
     * method/field will probably be a primitive type instead. Accept float as matching Float,
     * etc.
     */
    private boolean typesMatch(java.lang.Class<V> valueType, java.lang.Class getterType) {
        if (getterType != valueType) {
            if (getterType.isPrimitive()) {
                return ((((((((getterType == float.class) && (valueType == java.lang.Float.class)) || ((getterType == int.class) && (valueType == java.lang.Integer.class))) || ((getterType == boolean.class) && (valueType == java.lang.Boolean.class))) || ((getterType == long.class) && (valueType == java.lang.Long.class))) || ((getterType == double.class) && (valueType == java.lang.Double.class))) || ((getterType == short.class) && (valueType == java.lang.Short.class))) || ((getterType == byte.class) && (valueType == java.lang.Byte.class))) || ((getterType == char.class) && (valueType == java.lang.Character.class));
            }
            return false;
        }
        return true;
    }

    @java.lang.Override
    public void set(T object, V value) {
        if (mSetter != null) {
            try {
                mSetter.invoke(object, value);
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.AssertionError();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new java.lang.RuntimeException(e.getCause());
            }
        } else
            if (mField != null) {
                try {
                    mField.set(object, value);
                } catch (java.lang.IllegalAccessException e) {
                    throw new java.lang.AssertionError();
                }
            } else {
                throw new java.lang.UnsupportedOperationException(("Property " + getName()) + " is read-only");
            }

    }

    @java.lang.Override
    public V get(T object) {
        if (mGetter != null) {
            try {
                return ((V) (mGetter.invoke(object, ((java.lang.Object[]) (null)))));
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.AssertionError();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new java.lang.RuntimeException(e.getCause());
            }
        } else
            if (mField != null) {
                try {
                    return ((V) (mField.get(object)));
                } catch (java.lang.IllegalAccessException e) {
                    throw new java.lang.AssertionError();
                }
            }

        // Should not get here: there should always be a non-null getter or field
        throw new java.lang.AssertionError();
    }

    /**
     * Returns false if there is no setter or public field underlying this Property.
     */
    @java.lang.Override
    public boolean isReadOnly() {
        return (mSetter == null) && (mField == null);
    }
}

