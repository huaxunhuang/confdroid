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
package android.databinding.annotationprocessor;


// binding app info and library info are necessary to trigger this.
public class ProcessBindable extends android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep implements android.databinding.tool.CompilerChef.BindableHolder {
    android.databinding.annotationprocessor.ProcessBindable.Intermediate mProperties;

    java.util.HashMap<java.lang.String, java.util.HashSet<java.lang.String>> mLayoutVariables = new java.util.HashMap<java.lang.String, java.util.HashSet<java.lang.String>>();

    @java.lang.Override
    public boolean onHandleStep(javax.annotation.processing.RoundEnvironment roundEnv, javax.annotation.processing.ProcessingEnvironment processingEnv, android.databinding.BindingBuildInfo buildInfo) {
        if (mProperties == null) {
            mProperties = new android.databinding.annotationprocessor.ProcessBindable.IntermediateV1(buildInfo.modulePackage());
            mergeLayoutVariables();
            mLayoutVariables.clear();
            javax.lang.model.element.TypeElement observableType = processingEnv.getElementUtils().getTypeElement("android.databinding.Observable");
            javax.lang.model.util.Types typeUtils = processingEnv.getTypeUtils();
            for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.Bindable.class)) {
                javax.lang.model.element.Element enclosingElement = element.getEnclosingElement();
                javax.lang.model.element.ElementKind kind = enclosingElement.getKind();
                if ((kind != javax.lang.model.element.ElementKind.CLASS) && (kind != javax.lang.model.element.ElementKind.INTERFACE)) {
                    android.databinding.tool.util.L.e("Bindable must be on a member field or method. The enclosing type is %s", enclosingElement.getKind());
                }
                javax.lang.model.element.TypeElement enclosing = ((javax.lang.model.element.TypeElement) (enclosingElement));
                if (!typeUtils.isAssignable(enclosing.asType(), observableType.asType())) {
                    android.databinding.tool.util.L.e("Bindable must be on a member in an Observable class. %s is not Observable", enclosingElement.getSimpleName());
                }
                java.lang.String name = getPropertyName(element);
                if (name != null) {
                    android.databinding.tool.util.Preconditions.checkNotNull(mProperties, "Must receive app / library info before " + "Bindable fields.");
                    mProperties.addProperty(enclosing.getQualifiedName().toString(), name);
                }
            }
            android.databinding.tool.util.GenerationalClassUtil.writeIntermediateFile(processingEnv, mProperties.getPackage(), createIntermediateFileName(mProperties.getPackage()), mProperties);
            generateBRClasses(!buildInfo.isLibrary(), mProperties.getPackage());
        }
        return false;
    }

    @java.lang.Override
    public void addVariable(java.lang.String variableName, java.lang.String containingClassName) {
        java.util.HashSet<java.lang.String> variableNames = mLayoutVariables.get(containingClassName);
        if (variableNames == null) {
            variableNames = new java.util.HashSet<java.lang.String>();
            mLayoutVariables.put(containingClassName, variableNames);
        }
        variableNames.add(variableName);
    }

    @java.lang.Override
    public void onProcessingOver(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) {
    }

    private java.lang.String createIntermediateFileName(java.lang.String appPkg) {
        return appPkg + android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.BR.getExtension();
    }

    private void generateBRClasses(boolean useFinalFields, java.lang.String pkg) {
        android.databinding.tool.util.L.d("************* Generating BR file %s. use final: %s", pkg, useFinalFields);
        java.util.HashSet<java.lang.String> properties = new java.util.HashSet<java.lang.String>();
        mProperties.captureProperties(properties);
        java.util.List<android.databinding.annotationprocessor.ProcessBindable.Intermediate> previousIntermediates = loadPreviousBRFiles();
        for (android.databinding.annotationprocessor.ProcessBindable.Intermediate intermediate : previousIntermediates) {
            intermediate.captureProperties(properties);
        }
        final android.databinding.tool.writer.JavaFileWriter writer = getWriter();
        android.databinding.tool.writer.BRWriter brWriter = new android.databinding.tool.writer.BRWriter(properties, useFinalFields);
        writer.writeToFile(pkg + ".BR", brWriter.write(pkg));
        // writeBRClass(useFinalFields, pkg, properties);
        if (useFinalFields) {
            // generate BR for all previous packages
            for (android.databinding.annotationprocessor.ProcessBindable.Intermediate intermediate : previousIntermediates) {
                writer.writeToFile(intermediate.getPackage() + ".BR", brWriter.write(intermediate.getPackage()));
            }
        }
        mCallback.onBrWriterReady(brWriter);
    }

    private java.lang.String getPropertyName(javax.lang.model.element.Element element) {
        switch (element.getKind()) {
            case FIELD :
                return android.databinding.annotationprocessor.ProcessBindable.stripPrefixFromField(((javax.lang.model.element.VariableElement) (element)));
            case METHOD :
                return stripPrefixFromMethod(((javax.lang.model.element.ExecutableElement) (element)));
            default :
                android.databinding.tool.util.L.e("@Bindable is not allowed on %s", element.getKind());
                return null;
        }
    }

    private static java.lang.String stripPrefixFromField(javax.lang.model.element.VariableElement element) {
        javax.lang.model.element.Name name = element.getSimpleName();
        if (name.length() >= 2) {
            char firstChar = name.charAt(0);
            char secondChar = name.charAt(1);
            if (((name.length() > 2) && (firstChar == 'm')) && (secondChar == '_')) {
                char thirdChar = name.charAt(2);
                if (java.lang.Character.isJavaIdentifierStart(thirdChar)) {
                    return ("" + java.lang.Character.toLowerCase(thirdChar)) + name.subSequence(3, name.length());
                }
            } else
                if (((firstChar == 'm') && java.lang.Character.isUpperCase(secondChar)) || ((firstChar == '_') && java.lang.Character.isJavaIdentifierStart(secondChar))) {
                    return ("" + java.lang.Character.toLowerCase(secondChar)) + name.subSequence(2, name.length());
                }

        }
        return name.toString();
    }

    private java.lang.String stripPrefixFromMethod(javax.lang.model.element.ExecutableElement element) {
        javax.lang.model.element.Name name = element.getSimpleName();
        java.lang.CharSequence propertyName;
        if (android.databinding.annotationprocessor.ProcessBindable.isGetter(element) || android.databinding.annotationprocessor.ProcessBindable.isSetter(element)) {
            propertyName = name.subSequence(3, name.length());
        } else
            if (android.databinding.annotationprocessor.ProcessBindable.isBooleanGetter(element)) {
                propertyName = name.subSequence(2, name.length());
            } else {
                android.databinding.tool.util.L.e("@Bindable associated with method must follow JavaBeans convention %s", element);
                return null;
            }

        char firstChar = propertyName.charAt(0);
        return ("" + java.lang.Character.toLowerCase(firstChar)) + propertyName.subSequence(1, propertyName.length());
    }

    private void mergeLayoutVariables() {
        for (java.lang.String containingClass : mLayoutVariables.keySet()) {
            for (java.lang.String variable : mLayoutVariables.get(containingClass)) {
                mProperties.addProperty(containingClass, variable);
            }
        }
    }

    private static boolean prefixes(java.lang.CharSequence sequence, java.lang.String prefix) {
        boolean prefixes = false;
        if (sequence.length() > prefix.length()) {
            int count = prefix.length();
            prefixes = true;
            for (int i = 0; i < count; i++) {
                if (sequence.charAt(i) != prefix.charAt(i)) {
                    prefixes = false;
                    break;
                }
            }
        }
        return prefixes;
    }

    private static boolean isGetter(javax.lang.model.element.ExecutableElement element) {
        javax.lang.model.element.Name name = element.getSimpleName();
        return ((android.databinding.annotationprocessor.ProcessBindable.prefixes(name, "get") && java.lang.Character.isJavaIdentifierStart(name.charAt(3))) && element.getParameters().isEmpty()) && (element.getReturnType().getKind() != javax.lang.model.type.TypeKind.VOID);
    }

    private static boolean isSetter(javax.lang.model.element.ExecutableElement element) {
        javax.lang.model.element.Name name = element.getSimpleName();
        return ((android.databinding.annotationprocessor.ProcessBindable.prefixes(name, "set") && java.lang.Character.isJavaIdentifierStart(name.charAt(3))) && (element.getParameters().size() == 1)) && (element.getReturnType().getKind() == javax.lang.model.type.TypeKind.VOID);
    }

    private static boolean isBooleanGetter(javax.lang.model.element.ExecutableElement element) {
        javax.lang.model.element.Name name = element.getSimpleName();
        return ((android.databinding.annotationprocessor.ProcessBindable.prefixes(name, "is") && java.lang.Character.isJavaIdentifierStart(name.charAt(2))) && element.getParameters().isEmpty()) && (element.getReturnType().getKind() == javax.lang.model.type.TypeKind.BOOLEAN);
    }

    private java.util.List<android.databinding.annotationprocessor.ProcessBindable.Intermediate> loadPreviousBRFiles() {
        return android.databinding.tool.util.GenerationalClassUtil.loadObjects(android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.BR);
    }

    private interface Intermediate extends java.io.Serializable {
        void captureProperties(java.util.Set<java.lang.String> properties);

        void addProperty(java.lang.String className, java.lang.String propertyName);

        boolean hasValues();

        java.lang.String getPackage();
    }

    private static class IntermediateV1 implements android.databinding.annotationprocessor.ProcessBindable.Intermediate , java.io.Serializable {
        private static final long serialVersionUID = 2L;

        private java.lang.String mPackage;

        private final java.util.HashMap<java.lang.String, java.util.HashSet<java.lang.String>> mProperties = new java.util.HashMap<java.lang.String, java.util.HashSet<java.lang.String>>();

        public IntermediateV1(java.lang.String aPackage) {
            mPackage = aPackage;
        }

        @java.lang.Override
        public void captureProperties(java.util.Set<java.lang.String> properties) {
            for (java.util.HashSet<java.lang.String> propertySet : mProperties.values()) {
                properties.addAll(propertySet);
            }
        }

        @java.lang.Override
        public void addProperty(java.lang.String className, java.lang.String propertyName) {
            java.util.HashSet<java.lang.String> properties = mProperties.get(className);
            if (properties == null) {
                properties = new java.util.HashSet<java.lang.String>();
                mProperties.put(className, properties);
            }
            properties.add(propertyName);
        }

        @java.lang.Override
        public boolean hasValues() {
            return !mProperties.isEmpty();
        }

        @java.lang.Override
        public java.lang.String getPackage() {
            return mPackage;
        }
    }
}

