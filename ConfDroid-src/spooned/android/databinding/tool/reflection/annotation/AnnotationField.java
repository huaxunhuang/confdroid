/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.tool.reflection.annotation;


class AnnotationField extends android.databinding.tool.reflection.ModelField {
    final javax.lang.model.element.VariableElement mField;

    final javax.lang.model.element.TypeElement mDeclaredClass;

    public AnnotationField(javax.lang.model.element.TypeElement declaredClass, javax.lang.model.element.VariableElement field) {
        mDeclaredClass = declaredClass;
        mField = field;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mField.toString();
    }

    @java.lang.Override
    public boolean isBindable() {
        return mField.getAnnotation(android.databinding.Bindable.class) != null;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mField.getSimpleName().toString();
    }

    @java.lang.Override
    public boolean isPublic() {
        return mField.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC);
    }

    @java.lang.Override
    public boolean isStatic() {
        return mField.getModifiers().contains(javax.lang.model.element.Modifier.STATIC);
    }

    @java.lang.Override
    public boolean isFinal() {
        return mField.getModifiers().contains(javax.lang.model.element.Modifier.FINAL);
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getFieldType() {
        return new android.databinding.tool.reflection.annotation.AnnotationClass(mField.asType());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.databinding.tool.reflection.annotation.AnnotationField) {
            android.databinding.tool.reflection.annotation.AnnotationField that = ((android.databinding.tool.reflection.annotation.AnnotationField) (obj));
            return mDeclaredClass.equals(that.mDeclaredClass) && android.databinding.tool.reflection.annotation.AnnotationAnalyzer.get().getTypeUtils().isSameType(mField.asType(), that.mField.asType());
        } else {
            return false;
        }
    }
}

