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


/**
 * This is the implementation of ModelClass for the annotation
 * processor. It relies on AnnotationAnalyzer.
 */
class AnnotationClass extends android.databinding.tool.reflection.ModelClass {
    final javax.lang.model.type.TypeMirror mTypeMirror;

    public AnnotationClass(javax.lang.model.type.TypeMirror typeMirror) {
        mTypeMirror = typeMirror;
    }

    @java.lang.Override
    public java.lang.String toJavaCode() {
        if (isIncomplete()) {
            return getCanonicalName();
        }
        return mTypeMirror.toString();
    }

    @java.lang.Override
    public boolean isArray() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.ARRAY;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.annotation.AnnotationClass getComponentType() {
        javax.lang.model.type.TypeMirror component = null;
        if (isArray()) {
            component = ((javax.lang.model.type.ArrayType) (mTypeMirror)).getComponentType();
        } else
            if (isList()) {
                for (android.databinding.tool.reflection.ModelMethod method : getMethods("get", 1)) {
                    android.databinding.tool.reflection.ModelClass parameter = method.getParameterTypes()[0];
                    if (parameter.isInt() || parameter.isLong()) {
                        java.util.ArrayList<android.databinding.tool.reflection.ModelClass> parameters = new java.util.ArrayList<android.databinding.tool.reflection.ModelClass>(1);
                        parameters.add(parameter);
                        return ((android.databinding.tool.reflection.annotation.AnnotationClass) (method.getReturnType(parameters)));
                    }
                }
                // no "get" call found!
                return null;
            } else {
                android.databinding.tool.reflection.annotation.AnnotationClass mapClass = ((android.databinding.tool.reflection.annotation.AnnotationClass) (android.databinding.tool.reflection.ModelAnalyzer.getInstance().getMapType()));
                javax.lang.model.type.DeclaredType mapType = findInterface(mapClass.mTypeMirror);
                if (mapType == null) {
                    return null;
                }
                component = mapType.getTypeArguments().get(1);
            }

        return new android.databinding.tool.reflection.annotation.AnnotationClass(component);
    }

    private javax.lang.model.type.DeclaredType findInterface(javax.lang.model.type.TypeMirror interfaceType) {
        javax.lang.model.util.Types typeUtil = android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils();
        javax.lang.model.type.TypeMirror foundInterface = null;
        if (typeUtil.isSameType(interfaceType, typeUtil.erasure(mTypeMirror))) {
            foundInterface = mTypeMirror;
        } else {
            java.util.ArrayList<javax.lang.model.type.TypeMirror> toCheck = new java.util.ArrayList<javax.lang.model.type.TypeMirror>();
            toCheck.add(mTypeMirror);
            while (!toCheck.isEmpty()) {
                javax.lang.model.type.TypeMirror typeMirror = toCheck.remove(0);
                if (typeUtil.isSameType(interfaceType, typeUtil.erasure(typeMirror))) {
                    foundInterface = typeMirror;
                    break;
                } else {
                    toCheck.addAll(typeUtil.directSupertypes(typeMirror));
                }
            } 
            if (foundInterface == null) {
                android.databinding.tool.util.L.e(((("Detected " + interfaceType) + " type for ") + mTypeMirror) + ", but not able to find the implemented interface.");
                return null;
            }
        }
        if (foundInterface.getKind() != javax.lang.model.type.TypeKind.DECLARED) {
            android.databinding.tool.util.L.e((((("Found " + interfaceType) + " type for ") + mTypeMirror) + ", but it isn't a declared type: ") + foundInterface);
            return null;
        }
        return ((javax.lang.model.type.DeclaredType) (foundInterface));
    }

    @java.lang.Override
    public boolean isNullable() {
        switch (mTypeMirror.getKind()) {
            case ARRAY :
            case DECLARED :
            case NULL :
                return true;
            default :
                return false;
        }
    }

    @java.lang.Override
    public boolean isPrimitive() {
        switch (mTypeMirror.getKind()) {
            case BOOLEAN :
            case BYTE :
            case SHORT :
            case INT :
            case LONG :
            case CHAR :
            case FLOAT :
            case DOUBLE :
                return true;
            default :
                return false;
        }
    }

    @java.lang.Override
    public boolean isBoolean() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.BOOLEAN;
    }

    @java.lang.Override
    public boolean isChar() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.CHAR;
    }

    @java.lang.Override
    public boolean isByte() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.BYTE;
    }

    @java.lang.Override
    public boolean isShort() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.SHORT;
    }

    @java.lang.Override
    public boolean isInt() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.INT;
    }

    @java.lang.Override
    public boolean isLong() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.LONG;
    }

    @java.lang.Override
    public boolean isFloat() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.FLOAT;
    }

    @java.lang.Override
    public boolean isDouble() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DOUBLE;
    }

    @java.lang.Override
    public boolean isGeneric() {
        boolean isGeneric = false;
        if (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
            javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (mTypeMirror));
            java.util.List<? extends javax.lang.model.type.TypeMirror> typeArguments = declaredType.getTypeArguments();
            isGeneric = (typeArguments != null) && (!typeArguments.isEmpty());
        }
        return isGeneric;
    }

    @java.lang.Override
    public int getMinApi() {
        if (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
            javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (mTypeMirror));
            java.util.List<? extends javax.lang.model.element.AnnotationMirror> annotations = android.databinding.tool.reflection.annotation.AnnotationClass.getElementUtils().getAllAnnotationMirrors(declaredType.asElement());
            javax.lang.model.element.TypeElement targetApi = android.databinding.tool.reflection.annotation.AnnotationClass.getElementUtils().getTypeElement("android.annotation.TargetApi");
            javax.lang.model.type.TypeMirror targetApiType = targetApi.asType();
            javax.lang.model.util.Types typeUtils = android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils();
            for (javax.lang.model.element.AnnotationMirror annotation : annotations) {
                if (typeUtils.isAssignable(annotation.getAnnotationType(), targetApiType)) {
                    for (javax.lang.model.element.AnnotationValue value : annotation.getElementValues().values()) {
                        return ((java.lang.Integer) (value.getValue()));
                    }
                }
            }
        }
        return super.getMinApi();
    }

    @java.lang.Override
    public java.util.List<android.databinding.tool.reflection.ModelClass> getTypeArguments() {
        java.util.List<android.databinding.tool.reflection.ModelClass> types = null;
        if (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
            javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (mTypeMirror));
            java.util.List<? extends javax.lang.model.type.TypeMirror> typeArguments = declaredType.getTypeArguments();
            if ((typeArguments != null) && (!typeArguments.isEmpty())) {
                types = new java.util.ArrayList<android.databinding.tool.reflection.ModelClass>();
                for (javax.lang.model.type.TypeMirror typeMirror : typeArguments) {
                    types.add(new android.databinding.tool.reflection.annotation.AnnotationClass(typeMirror));
                }
            }
        }
        return types;
    }

    @java.lang.Override
    public boolean isTypeVar() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.TYPEVAR;
    }

    @java.lang.Override
    public boolean isWildcard() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.WILDCARD;
    }

    @java.lang.Override
    public boolean isInterface() {
        return (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) && (((javax.lang.model.type.DeclaredType) (mTypeMirror)).asElement().getKind() == javax.lang.model.element.ElementKind.INTERFACE);
    }

    @java.lang.Override
    public boolean isVoid() {
        return mTypeMirror.getKind() == javax.lang.model.type.TypeKind.VOID;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.annotation.AnnotationClass unbox() {
        if (!isNullable()) {
            return this;
        }
        try {
            return new android.databinding.tool.reflection.annotation.AnnotationClass(android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils().unboxedType(mTypeMirror));
        } catch (java.lang.IllegalArgumentException e) {
            // I'm being lazy. This is much easier than checking every type.
            return this;
        }
    }

    @java.lang.Override
    public android.databinding.tool.reflection.annotation.AnnotationClass box() {
        if (!isPrimitive()) {
            return this;
        }
        return new android.databinding.tool.reflection.annotation.AnnotationClass(android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils().boxedClass(((javax.lang.model.type.PrimitiveType) (mTypeMirror))).asType());
    }

    @java.lang.Override
    public boolean isAssignableFrom(android.databinding.tool.reflection.ModelClass that) {
        if (that == null) {
            return false;
        }
        android.databinding.tool.reflection.annotation.AnnotationClass thatAnnotationClass = ((android.databinding.tool.reflection.annotation.AnnotationClass) (that));
        return android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils().isAssignable(thatAnnotationClass.mTypeMirror, this.mTypeMirror);
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelMethod[] getDeclaredMethods() {
        final android.databinding.tool.reflection.ModelMethod[] declaredMethods;
        if (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
            javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (mTypeMirror));
            javax.lang.model.util.Elements elementUtils = android.databinding.tool.reflection.annotation.AnnotationClass.getElementUtils();
            javax.lang.model.element.TypeElement typeElement = ((javax.lang.model.element.TypeElement) (declaredType.asElement()));
            java.util.List<? extends javax.lang.model.element.Element> members = elementUtils.getAllMembers(typeElement);
            java.util.List<javax.lang.model.element.ExecutableElement> methods = javax.lang.model.util.ElementFilter.methodsIn(members);
            declaredMethods = new android.databinding.tool.reflection.ModelMethod[methods.size()];
            for (int i = 0; i < declaredMethods.length; i++) {
                declaredMethods[i] = new android.databinding.tool.reflection.annotation.AnnotationMethod(declaredType, methods.get(i));
            }
        } else {
            declaredMethods = new android.databinding.tool.reflection.ModelMethod[0];
        }
        return declaredMethods;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.annotation.AnnotationClass getSuperclass() {
        if (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
            javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (mTypeMirror));
            javax.lang.model.element.TypeElement typeElement = ((javax.lang.model.element.TypeElement) (declaredType.asElement()));
            javax.lang.model.type.TypeMirror superClass = typeElement.getSuperclass();
            if (superClass.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
                return new android.databinding.tool.reflection.annotation.AnnotationClass(superClass);
            }
        }
        return null;
    }

    @java.lang.Override
    public java.lang.String getCanonicalName() {
        return android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils().erasure(mTypeMirror).toString();
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass erasure() {
        final javax.lang.model.type.TypeMirror erasure = android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils().erasure(mTypeMirror);
        if (erasure == mTypeMirror) {
            return this;
        } else {
            return new android.databinding.tool.reflection.annotation.AnnotationClass(erasure);
        }
    }

    @java.lang.Override
    public java.lang.String getJniDescription() {
        return android.databinding.tool.reflection.TypeUtil.getInstance().getDescription(this);
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelField[] getDeclaredFields() {
        final android.databinding.tool.reflection.ModelField[] declaredFields;
        if (mTypeMirror.getKind() == javax.lang.model.type.TypeKind.DECLARED) {
            javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (mTypeMirror));
            javax.lang.model.util.Elements elementUtils = android.databinding.tool.reflection.annotation.AnnotationClass.getElementUtils();
            javax.lang.model.element.TypeElement typeElement = ((javax.lang.model.element.TypeElement) (declaredType.asElement()));
            java.util.List<? extends javax.lang.model.element.Element> members = elementUtils.getAllMembers(typeElement);
            java.util.List<javax.lang.model.element.VariableElement> fields = javax.lang.model.util.ElementFilter.fieldsIn(members);
            declaredFields = new android.databinding.tool.reflection.ModelField[fields.size()];
            for (int i = 0; i < declaredFields.length; i++) {
                declaredFields[i] = new android.databinding.tool.reflection.annotation.AnnotationField(typeElement, fields.get(i));
            }
        } else {
            declaredFields = new android.databinding.tool.reflection.ModelField[0];
        }
        return declaredFields;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.databinding.tool.reflection.annotation.AnnotationClass) {
            return android.databinding.tool.reflection.annotation.AnnotationClass.getTypeUtils().isSameType(mTypeMirror, ((android.databinding.tool.reflection.annotation.AnnotationClass) (obj)).mTypeMirror);
        } else {
            return false;
        }
    }

    @java.lang.Override
    public int hashCode() {
        return mTypeMirror.toString().hashCode();
    }

    private static javax.lang.model.util.Types getTypeUtils() {
        return android.databinding.tool.reflection.annotation.AnnotationAnalyzer.get().mProcessingEnv.getTypeUtils();
    }

    private static javax.lang.model.util.Elements getElementUtils() {
        return android.databinding.tool.reflection.annotation.AnnotationAnalyzer.get().mProcessingEnv.getElementUtils();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mTypeMirror.toString();
    }
}

