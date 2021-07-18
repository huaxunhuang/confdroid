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


public class JavaField extends android.databinding.tool.reflection.ModelField {
    public final java.lang.reflect.Field mField;

    public JavaField(java.lang.reflect.Field field) {
        mField = field;
    }

    @java.lang.Override
    public boolean isBindable() {
        return mField.getAnnotation(android.databinding.Bindable.class) != null;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mField.getName();
    }

    @java.lang.Override
    public boolean isPublic() {
        return java.lang.reflect.Modifier.isPublic(mField.getModifiers());
    }

    @java.lang.Override
    public boolean isStatic() {
        return java.lang.reflect.Modifier.isStatic(mField.getModifiers());
    }

    @java.lang.Override
    public boolean isFinal() {
        return java.lang.reflect.Modifier.isFinal(mField.getModifiers());
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getFieldType() {
        return new android.databinding.tool.reflection.java.JavaClass(mField.getType());
    }
}

