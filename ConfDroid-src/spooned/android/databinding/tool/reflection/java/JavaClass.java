/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool.reflection.java;


public class JavaClass extends android.databinding.tool.reflection.ModelClass {
    public final java.lang.Class mClass;

    public JavaClass(java.lang.Class clazz) {
        mClass = clazz;
    }

    @java.lang.Override
    public java.lang.String toJavaCode() {
        return android.databinding.tool.reflection.java.JavaClass.toJavaCode(mClass);
    }

    private static java.lang.String toJavaCode(java.lang.Class aClass) {
        if (aClass.isArray()) {
            java.lang.Class component = aClass.getComponentType();
            return android.databinding.tool.reflection.java.JavaClass.toJavaCode(component) + "[]";
        } else {
            return aClass.getCanonicalName().replace('$', '.');
        }
    }

    @java.lang.Override
    public boolean isArray() {
        return mClass.isArray();
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getComponentType() {
        if (mClass.isArray()) {
            return new android.databinding.tool.reflection.java.JavaClass(mClass.getComponentType());
        } else
            if (isList() || isMap()) {
                return new android.databinding.tool.reflection.java.JavaClass(java.lang.Object.class);
            } else {
                return null;
            }

    }

    @java.lang.Override
    public boolean isNullable() {
        return java.lang.Object.class.isAssignableFrom(mClass);
    }

    @java.lang.Override
    public boolean isPrimitive() {
        return mClass.isPrimitive();
    }

    @java.lang.Override
    public boolean isBoolean() {
        return boolean.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isChar() {
        return char.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isByte() {
        return byte.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isShort() {
        return short.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isInt() {
        return int.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isLong() {
        return long.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isFloat() {
        return float.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isDouble() {
        return double.class.equals(mClass);
    }

    @java.lang.Override
    public boolean isGeneric() {
        return false;
    }

    @java.lang.Override
    public java.util.List<android.databinding.tool.reflection.ModelClass> getTypeArguments() {
        return null;
    }

    @java.lang.Override
    public boolean isTypeVar() {
        return false;
    }

    @java.lang.Override
    public boolean isWildcard() {
        return false;
    }

    @java.lang.Override
    public boolean isInterface() {
        return mClass.isInterface();
    }

    @java.lang.Override
    public boolean isVoid() {
        return void.class.equals(mClass);
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass unbox() {
        if (mClass.isPrimitive()) {
            return this;
        }
        if (java.lang.Integer.class.equals(mClass)) {
            return new android.databinding.tool.reflection.java.JavaClass(int.class);
        } else
            if (java.lang.Long.class.equals(mClass)) {
                return new android.databinding.tool.reflection.java.JavaClass(long.class);
            } else
                if (java.lang.Short.class.equals(mClass)) {
                    return new android.databinding.tool.reflection.java.JavaClass(short.class);
                } else
                    if (java.lang.Byte.class.equals(mClass)) {
                        return new android.databinding.tool.reflection.java.JavaClass(byte.class);
                    } else
                        if (java.lang.Character.class.equals(mClass)) {
                            return new android.databinding.tool.reflection.java.JavaClass(char.class);
                        } else
                            if (java.lang.Double.class.equals(mClass)) {
                                return new android.databinding.tool.reflection.java.JavaClass(double.class);
                            } else
                                if (java.lang.Float.class.equals(mClass)) {
                                    return new android.databinding.tool.reflection.java.JavaClass(float.class);
                                } else
                                    if (java.lang.Boolean.class.equals(mClass)) {
                                        return new android.databinding.tool.reflection.java.JavaClass(boolean.class);
                                    } else {
                                        // not a boxed type
                                        return this;
                                    }







    }

    @java.lang.Override
    public android.databinding.tool.reflection.java.JavaClass box() {
        if (!mClass.isPrimitive()) {
            return this;
        }
        if (int.class.equals(mClass)) {
            return new android.databinding.tool.reflection.java.JavaClass(java.lang.Integer.class);
        } else
            if (long.class.equals(mClass)) {
                return new android.databinding.tool.reflection.java.JavaClass(java.lang.Long.class);
            } else
                if (short.class.equals(mClass)) {
                    return new android.databinding.tool.reflection.java.JavaClass(java.lang.Short.class);
                } else
                    if (byte.class.equals(mClass)) {
                        return new android.databinding.tool.reflection.java.JavaClass(java.lang.Byte.class);
                    } else
                        if (char.class.equals(mClass)) {
                            return new android.databinding.tool.reflection.java.JavaClass(java.lang.Character.class);
                        } else
                            if (double.class.equals(mClass)) {
                                return new android.databinding.tool.reflection.java.JavaClass(java.lang.Double.class);
                            } else
                                if (float.class.equals(mClass)) {
                                    return new android.databinding.tool.reflection.java.JavaClass(java.lang.Float.class);
                                } else
                                    if (boolean.class.equals(mClass)) {
                                        return new android.databinding.tool.reflection.java.JavaClass(java.lang.Boolean.class);
                                    } else {
                                        // not a valid type?
                                        return this;
                                    }







    }

    @java.lang.Override
    public boolean isAssignableFrom(android.databinding.tool.reflection.ModelClass that) {
        java.lang.Class thatClass = ((android.databinding.tool.reflection.java.JavaClass) (that)).mClass;
        return mClass.isAssignableFrom(thatClass);
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getSuperclass() {
        if (mClass.getSuperclass() == null) {
            return null;
        }
        return new android.databinding.tool.reflection.java.JavaClass(mClass.getSuperclass());
    }

    @java.lang.Override
    public java.lang.String getCanonicalName() {
        return mClass.getCanonicalName();
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass erasure() {
        return this;
    }

    @java.lang.Override
    public java.lang.String getJniDescription() {
        return android.databinding.tool.reflection.TypeUtil.getInstance().getDescription(this);
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelField[] getDeclaredFields() {
        java.lang.reflect.Field[] fields = mClass.getDeclaredFields();
        android.databinding.tool.reflection.ModelField[] modelFields;
        if (fields == null) {
            modelFields = new android.databinding.tool.reflection.ModelField[0];
        } else {
            modelFields = new android.databinding.tool.reflection.ModelField[fields.length];
            for (int i = 0; i < fields.length; i++) {
                modelFields[i] = new android.databinding.tool.reflection.java.JavaField(fields[i]);
            }
        }
        return modelFields;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelMethod[] getDeclaredMethods() {
        java.lang.reflect.Method[] methods = mClass.getDeclaredMethods();
        if (methods == null) {
            return new android.databinding.tool.reflection.ModelMethod[0];
        } else {
            android.databinding.tool.reflection.ModelMethod[] classMethods = new android.databinding.tool.reflection.ModelMethod[methods.length];
            for (int i = 0; i < methods.length; i++) {
                classMethods[i] = new android.databinding.tool.reflection.java.JavaMethod(methods[i]);
            }
            return classMethods;
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.databinding.tool.reflection.java.JavaClass) {
            return mClass.equals(((android.databinding.tool.reflection.java.JavaClass) (obj)).mClass);
        } else {
            return false;
        }
    }

    @java.lang.Override
    public int hashCode() {
        return mClass.hashCode();
    }
}

