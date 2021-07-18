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


public class AnnotationTypeUtil extends android.databinding.tool.reflection.TypeUtil {
    javax.lang.model.util.Types mTypes;

    public AnnotationTypeUtil(android.databinding.tool.reflection.annotation.AnnotationAnalyzer annotationAnalyzer) {
        mTypes = annotationAnalyzer.getTypeUtils();
    }

    @java.lang.Override
    public java.lang.String getDescription(android.databinding.tool.reflection.ModelClass modelClass) {
        // TODO use interface
        return modelClass.getCanonicalName().replace('.', '/');
    }

    @java.lang.Override
    public java.lang.String getDescription(android.databinding.tool.reflection.ModelMethod modelMethod) {
        // TODO use interface
        return modelMethod.getName() + getDescription(((android.databinding.tool.reflection.annotation.AnnotationMethod) (modelMethod)).mExecutableElement.asType());
    }

    private java.lang.String getDescription(javax.lang.model.type.TypeMirror typeMirror) {
        if (typeMirror == null) {
            throw new java.lang.UnsupportedOperationException();
        }
        switch (typeMirror.getKind()) {
            case BOOLEAN :
                return android.databinding.tool.reflection.TypeUtil.BOOLEAN;
            case BYTE :
                return android.databinding.tool.reflection.TypeUtil.BYTE;
            case SHORT :
                return android.databinding.tool.reflection.TypeUtil.SHORT;
            case INT :
                return android.databinding.tool.reflection.TypeUtil.INT;
            case LONG :
                return android.databinding.tool.reflection.TypeUtil.LONG;
            case CHAR :
                return android.databinding.tool.reflection.TypeUtil.CHAR;
            case FLOAT :
                return android.databinding.tool.reflection.TypeUtil.FLOAT;
            case DOUBLE :
                return android.databinding.tool.reflection.TypeUtil.DOUBLE;
            case DECLARED :
                return (android.databinding.tool.reflection.TypeUtil.CLASS_PREFIX + mTypes.erasure(typeMirror).toString().replace('.', '/')) + android.databinding.tool.reflection.TypeUtil.CLASS_SUFFIX;
            case VOID :
                return android.databinding.tool.reflection.TypeUtil.VOID;
            case ARRAY :
                final javax.lang.model.type.ArrayType arrayType = ((javax.lang.model.type.ArrayType) (typeMirror));
                final java.lang.String componentType = getDescription(arrayType.getComponentType());
                return android.databinding.tool.reflection.TypeUtil.ARRAY + componentType;
            case TYPEVAR :
                final javax.lang.model.type.TypeVariable typeVariable = ((javax.lang.model.type.TypeVariable) (typeMirror));
                final java.lang.String name = typeVariable.toString();
                return (android.databinding.tool.reflection.TypeUtil.CLASS_PREFIX + name.replace('.', '/')) + android.databinding.tool.reflection.TypeUtil.CLASS_SUFFIX;
            case EXECUTABLE :
                final javax.lang.model.type.ExecutableType executableType = ((javax.lang.model.type.ExecutableType) (typeMirror));
                final int argStart = mTypes.erasure(executableType).toString().indexOf('(');
                final java.lang.String methodName = executableType.toString().substring(0, argStart);
                final java.lang.String args = joinArgs(executableType.getParameterTypes());
                // TODO detect constructor?
                return (((methodName + "(") + args) + ")") + getDescription(executableType.getReturnType());
            default :
                throw new java.lang.UnsupportedOperationException((("cannot understand type " + typeMirror.toString()) + ", kind:") + typeMirror.getKind().name());
        }
    }

    private java.lang.String joinArgs(java.util.List<? extends javax.lang.model.type.TypeMirror> mirrorList) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        for (javax.lang.model.type.TypeMirror mirror : mirrorList) {
            result.append(getDescription(mirror));
        }
        return result.toString();
    }
}

