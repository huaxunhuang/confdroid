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


public class AnnotationAnalyzer extends android.databinding.tool.reflection.ModelAnalyzer {
    public static final java.util.Map<java.lang.String, javax.lang.model.type.TypeKind> PRIMITIVE_TYPES;

    static {
        PRIMITIVE_TYPES = new java.util.HashMap<java.lang.String, javax.lang.model.type.TypeKind>();
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("boolean", javax.lang.model.type.TypeKind.BOOLEAN);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("byte", javax.lang.model.type.TypeKind.BYTE);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("short", javax.lang.model.type.TypeKind.SHORT);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("char", javax.lang.model.type.TypeKind.CHAR);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("int", javax.lang.model.type.TypeKind.INT);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("long", javax.lang.model.type.TypeKind.LONG);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("float", javax.lang.model.type.TypeKind.FLOAT);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.put("double", javax.lang.model.type.TypeKind.DOUBLE);
    }

    public final javax.annotation.processing.ProcessingEnvironment mProcessingEnv;

    public AnnotationAnalyzer(javax.annotation.processing.ProcessingEnvironment processingEnvironment) {
        mProcessingEnv = processingEnvironment;
        setInstance(this);
        android.databinding.tool.util.L.setClient(new android.databinding.tool.util.L.Client() {
            @java.lang.Override
            public void printMessage(javax.tools.Diagnostic.Kind kind, java.lang.String message, javax.lang.model.element.Element element) {
                javax.annotation.processing.Messager messager = mProcessingEnv.getMessager();
                if (element != null) {
                    messager.printMessage(kind, message, element);
                } else {
                    messager.printMessage(kind, message);
                }
            }
        });
    }

    public static android.databinding.tool.reflection.annotation.AnnotationAnalyzer get() {
        return ((android.databinding.tool.reflection.annotation.AnnotationAnalyzer) (android.databinding.tool.reflection.ModelAnalyzer.getInstance()));
    }

    @java.lang.Override
    public android.databinding.tool.reflection.annotation.AnnotationClass loadPrimitive(java.lang.String className) {
        javax.lang.model.type.TypeKind typeKind = android.databinding.tool.reflection.annotation.AnnotationAnalyzer.PRIMITIVE_TYPES.get(className);
        if (typeKind == null) {
            return null;
        } else {
            javax.lang.model.util.Types typeUtils = getTypeUtils();
            return new android.databinding.tool.reflection.annotation.AnnotationClass(typeUtils.getPrimitiveType(typeKind));
        }
    }

    @java.lang.Override
    public android.databinding.tool.reflection.annotation.AnnotationClass findClass(java.lang.String className, java.util.Map<java.lang.String, java.lang.String> imports) {
        className = className.trim();
        int numDimensions = 0;
        while (className.endsWith("[]")) {
            numDimensions++;
            className = className.substring(0, className.length() - 2);
        } 
        android.databinding.tool.reflection.annotation.AnnotationClass primitive = loadPrimitive(className);
        if (primitive != null) {
            return addDimension(primitive.mTypeMirror, numDimensions);
        }
        int templateOpenIndex = className.indexOf('<');
        javax.lang.model.type.DeclaredType declaredType;
        if (templateOpenIndex < 0) {
            javax.lang.model.element.TypeElement typeElement = getTypeElement(className, imports);
            if (typeElement == null) {
                return null;
            }
            declaredType = ((javax.lang.model.type.DeclaredType) (typeElement.asType()));
        } else {
            int templateCloseIndex = className.lastIndexOf('>');
            java.lang.String paramStr = className.substring(templateOpenIndex + 1, templateCloseIndex);
            java.lang.String baseClassName = className.substring(0, templateOpenIndex);
            javax.lang.model.element.TypeElement typeElement = getTypeElement(baseClassName, imports);
            if (typeElement == null) {
                android.databinding.tool.util.L.e("cannot find type element for %s", baseClassName);
                return null;
            }
            java.util.ArrayList<java.lang.String> templateParameters = splitTemplateParameters(paramStr);
            javax.lang.model.type.TypeMirror[] typeArgs = new javax.lang.model.type.TypeMirror[templateParameters.size()];
            for (int i = 0; i < typeArgs.length; i++) {
                final android.databinding.tool.reflection.annotation.AnnotationClass clazz = findClass(templateParameters.get(i), imports);
                if (clazz == null) {
                    android.databinding.tool.util.L.e("cannot find type argument for %s in %s", templateParameters.get(i), baseClassName);
                    return null;
                }
                typeArgs[i] = clazz.mTypeMirror;
            }
            javax.lang.model.util.Types typeUtils = getTypeUtils();
            declaredType = typeUtils.getDeclaredType(typeElement, typeArgs);
        }
        return addDimension(declaredType, numDimensions);
    }

    private android.databinding.tool.reflection.annotation.AnnotationClass addDimension(javax.lang.model.type.TypeMirror type, int numDimensions) {
        while (numDimensions > 0) {
            type = getTypeUtils().getArrayType(type);
            numDimensions--;
        } 
        return new android.databinding.tool.reflection.annotation.AnnotationClass(type);
    }

    private javax.lang.model.element.TypeElement getTypeElement(java.lang.String className, java.util.Map<java.lang.String, java.lang.String> imports) {
        javax.lang.model.util.Elements elementUtils = getElementUtils();
        final boolean hasDot = className.indexOf('.') >= 0;
        if ((!hasDot) && (imports != null)) {
            // try the imports
            java.lang.String importedClass = imports.get(className);
            if (importedClass != null) {
                className = importedClass;
            }
        }
        if (className.indexOf('.') < 0) {
            // try java.lang.
            java.lang.String javaLangClass = "java.lang." + className;
            try {
                javax.lang.model.element.TypeElement javaLang = elementUtils.getTypeElement(javaLangClass);
                if (javaLang != null) {
                    return javaLang;
                }
            } catch (java.lang.Exception e) {
                // try the normal way
            }
        }
        try {
            javax.lang.model.element.TypeElement typeElement = elementUtils.getTypeElement(className);
            if (((typeElement == null) && hasDot) && (imports != null)) {
                int lastDot = className.lastIndexOf('.');
                javax.lang.model.element.TypeElement parent = getTypeElement(className.substring(0, lastDot), imports);
                if (parent == null) {
                    return null;
                }
                java.lang.String name = (parent.getQualifiedName() + ".") + className.substring(lastDot + 1);
                return getTypeElement(name, null);
            }
            return typeElement;
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    private java.util.ArrayList<java.lang.String> splitTemplateParameters(java.lang.String templateParameters) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<java.lang.String>();
        int index = 0;
        int openCount = 0;
        java.lang.StringBuilder arg = new java.lang.StringBuilder();
        while (index < templateParameters.length()) {
            char c = templateParameters.charAt(index);
            if ((c == ',') && (openCount == 0)) {
                list.add(arg.toString());
                arg.delete(0, arg.length());
            } else
                if (!java.lang.Character.isWhitespace(c)) {
                    arg.append(c);
                    if (c == '<') {
                        openCount++;
                    } else
                        if (c == '>') {
                            openCount--;
                        }

                }

            index++;
        } 
        list.add(arg.toString());
        return list;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass findClass(java.lang.Class classType) {
        return findClass(classType.getCanonicalName(), null);
    }

    public javax.lang.model.util.Types getTypeUtils() {
        return mProcessingEnv.getTypeUtils();
    }

    public javax.lang.model.util.Elements getElementUtils() {
        return mProcessingEnv.getElementUtils();
    }

    public javax.annotation.processing.ProcessingEnvironment getProcessingEnv() {
        return mProcessingEnv;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.TypeUtil createTypeUtil() {
        return new android.databinding.tool.reflection.annotation.AnnotationTypeUtil(this);
    }
}

