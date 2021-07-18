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


public class JavaMethod extends android.databinding.tool.reflection.ModelMethod {
    public final java.lang.reflect.Method mMethod;

    public JavaMethod(java.lang.reflect.Method method) {
        mMethod = method;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getDeclaringClass() {
        return new android.databinding.tool.reflection.java.JavaClass(mMethod.getDeclaringClass());
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
        java.lang.Class[] parameterTypes = mMethod.getParameterTypes();
        android.databinding.tool.reflection.ModelClass[] parameterClasses = new android.databinding.tool.reflection.ModelClass[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterClasses[i] = new android.databinding.tool.reflection.java.JavaClass(parameterTypes[i]);
        }
        return parameterClasses;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mMethod.getName();
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getReturnType(java.util.List<android.databinding.tool.reflection.ModelClass> args) {
        return new android.databinding.tool.reflection.java.JavaClass(mMethod.getReturnType());
    }

    @java.lang.Override
    public boolean isVoid() {
        return void.class.equals(mMethod.getReturnType());
    }

    @java.lang.Override
    public boolean isPublic() {
        return java.lang.reflect.Modifier.isPublic(mMethod.getModifiers());
    }

    @java.lang.Override
    public boolean isStatic() {
        return java.lang.reflect.Modifier.isStatic(mMethod.getModifiers());
    }

    @java.lang.Override
    public boolean isAbstract() {
        return java.lang.reflect.Modifier.isAbstract(mMethod.getModifiers());
    }

    @java.lang.Override
    public boolean isBindable() {
        return mMethod.getAnnotation(android.databinding.Bindable.class) != null;
    }

    @java.lang.Override
    public int getMinApi() {
        return android.databinding.tool.reflection.SdkUtil.getMinApi(this);
    }

    @java.lang.Override
    public java.lang.String getJniDescription() {
        return android.databinding.tool.reflection.TypeUtil.getInstance().getDescription(this);
    }

    @java.lang.Override
    public boolean isVarArgs() {
        return false;
    }
}

