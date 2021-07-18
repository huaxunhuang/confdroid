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


class AnnotationMethod extends android.databinding.tool.reflection.ModelMethod {
    final javax.lang.model.type.ExecutableType mMethod;

    final javax.lang.model.type.DeclaredType mDeclaringType;

    final javax.lang.model.element.ExecutableElement mExecutableElement;

    int mApiLevel = -1;// calculated on demand


    android.databinding.tool.reflection.ModelClass mReceiverType;

    public AnnotationMethod(javax.lang.model.type.DeclaredType declaringType, javax.lang.model.element.ExecutableElement executableElement) {
        mDeclaringType = declaringType;
        mExecutableElement = executableElement;
        javax.lang.model.util.Types typeUtils = android.databinding.tool.reflection.annotation.AnnotationAnalyzer.get().getTypeUtils();
        mMethod = ((javax.lang.model.type.ExecutableType) (typeUtils.asMemberOf(declaringType, executableElement)));
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getDeclaringClass() {
        if (mReceiverType == null) {
            mReceiverType = findReceiverType(mDeclaringType);
            if (mReceiverType == null) {
                mReceiverType = new android.databinding.tool.reflection.annotation.AnnotationClass(mDeclaringType);
            }
        }
        return mReceiverType;
    }

    // TODO: When going to Java 1.8, use mExecutableElement.getReceiverType()
    private android.databinding.tool.reflection.ModelClass findReceiverType(javax.lang.model.type.DeclaredType subType) {
        java.util.List<? extends javax.lang.model.type.TypeMirror> supers = android.databinding.tool.reflection.annotation.AnnotationMethod.getTypeUtils().directSupertypes(subType);
        for (javax.lang.model.type.TypeMirror superType : supers) {
            if (superType.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
                javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (superType));
                android.databinding.tool.reflection.ModelClass inSuper = findReceiverType(declaredType);
                if (inSuper != null) {
                    return inSuper;
                } else
                    if (hasExecutableMethod(declaredType)) {
                        return new android.databinding.tool.reflection.annotation.AnnotationClass(declaredType);
                    }

            }
        }
        return null;
    }

    private boolean hasExecutableMethod(javax.lang.model.type.DeclaredType declaredType) {
        javax.lang.model.util.Elements elementUtils = android.databinding.tool.reflection.annotation.AnnotationMethod.getElementUtils();
        javax.lang.model.element.TypeElement enclosing = ((javax.lang.model.element.TypeElement) (mExecutableElement.getEnclosingElement()));
        javax.lang.model.element.TypeElement typeElement = ((javax.lang.model.element.TypeElement) (declaredType.asElement()));
        for (javax.lang.model.element.Element element : typeElement.getEnclosedElements()) {
            if (element.getKind() == javax.lang.model.element.ElementKind.METHOD) {
                javax.lang.model.element.ExecutableElement executableElement = ((javax.lang.model.element.ExecutableElement) (element));
                if (executableElement.equals(mExecutableElement) || elementUtils.overrides(mExecutableElement, executableElement, enclosing)) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
        java.util.List<? extends javax.lang.model.type.TypeMirror> parameters = mMethod.getParameterTypes();
        android.databinding.tool.reflection.ModelClass[] parameterTypes = new android.databinding.tool.reflection.ModelClass[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            parameterTypes[i] = new android.databinding.tool.reflection.annotation.AnnotationClass(parameters.get(i));
        }
        return parameterTypes;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mExecutableElement.getSimpleName().toString();
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass getReturnType(java.util.List<android.databinding.tool.reflection.ModelClass> args) {
        javax.lang.model.type.TypeMirror returnType = mMethod.getReturnType();
        // TODO: support argument-supplied types
        // for example: public T[] toArray(T[] arr)
        return new android.databinding.tool.reflection.annotation.AnnotationClass(returnType);
    }

    @java.lang.Override
    public boolean isVoid() {
        return mMethod.getReturnType().getKind() == javax.lang.model.type.TypeKind.VOID;
    }

    @java.lang.Override
    public boolean isPublic() {
        return mExecutableElement.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC);
    }

    @java.lang.Override
    public boolean isStatic() {
        return mExecutableElement.getModifiers().contains(javax.lang.model.element.Modifier.STATIC);
    }

    @java.lang.Override
    public boolean isAbstract() {
        return mExecutableElement.getModifiers().contains(javax.lang.model.element.Modifier.ABSTRACT);
    }

    @java.lang.Override
    public boolean isBindable() {
        return mExecutableElement.getAnnotation(android.databinding.Bindable.class) != null;
    }

    @java.lang.Override
    public int getMinApi() {
        if (mApiLevel == (-1)) {
            mApiLevel = android.databinding.tool.reflection.SdkUtil.getMinApi(this);
        }
        return mApiLevel;
    }

    @java.lang.Override
    public java.lang.String getJniDescription() {
        return android.databinding.tool.reflection.TypeUtil.getInstance().getDescription(this);
    }

    @java.lang.Override
    public boolean isVarArgs() {
        return mExecutableElement.isVarArgs();
    }

    private static javax.lang.model.util.Types getTypeUtils() {
        return android.databinding.tool.reflection.annotation.AnnotationAnalyzer.get().mProcessingEnv.getTypeUtils();
    }

    private static javax.lang.model.util.Elements getElementUtils() {
        return android.databinding.tool.reflection.annotation.AnnotationAnalyzer.get().mProcessingEnv.getElementUtils();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((("AnnotationMethod{" + "mMethod=") + mMethod) + ", mDeclaringType=") + mDeclaringType) + ", mExecutableElement=") + mExecutableElement) + ", mApiLevel=") + mApiLevel) + '}';
    }
}

