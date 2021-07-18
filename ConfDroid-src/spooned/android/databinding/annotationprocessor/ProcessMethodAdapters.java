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


public class ProcessMethodAdapters extends android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep {
    private static final java.lang.String INVERSE_BINDING_EVENT_ATTR_SUFFIX = "AttrChanged";

    public ProcessMethodAdapters() {
    }

    @java.lang.Override
    public boolean onHandleStep(javax.annotation.processing.RoundEnvironment roundEnv, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) {
        android.databinding.tool.util.L.d("processing adapters");
        final android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
        android.databinding.tool.util.Preconditions.checkNotNull(modelAnalyzer, "Model analyzer should be" + " initialized first");
        android.databinding.tool.store.SetterStore store = android.databinding.tool.store.SetterStore.get(modelAnalyzer);
        clearIncrementalClasses(roundEnv, store);
        addBindingAdapters(roundEnv, processingEnvironment, store);
        addRenamed(roundEnv, store);
        addConversions(roundEnv, store);
        addUntaggable(roundEnv, store);
        addInverseAdapters(roundEnv, processingEnvironment, store);
        addInverseMethods(roundEnv, store);
        try {
            store.write(buildInfo.modulePackage(), processingEnvironment);
        } catch (java.io.IOException e) {
            android.databinding.tool.util.L.e(e, "Could not write BindingAdapter intermediate file.");
        }
        return true;
    }

    @java.lang.Override
    public void onProcessingOver(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) {
    }

    private void addBindingAdapters(javax.annotation.processing.RoundEnvironment roundEnv, javax.annotation.processing.ProcessingEnvironment processingEnv, android.databinding.tool.store.SetterStore store) {
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.BindingAdapter.class)) {
            if ((element.getKind() != javax.lang.model.element.ElementKind.METHOD) || (!element.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC))) {
                android.databinding.tool.util.L.e(element, "@BindingAdapter on invalid element: %s", element);
                continue;
            }
            android.databinding.BindingAdapter bindingAdapter = element.getAnnotation(android.databinding.BindingAdapter.class);
            javax.lang.model.element.ExecutableElement executableElement = ((javax.lang.model.element.ExecutableElement) (element));
            java.util.List<? extends javax.lang.model.element.VariableElement> parameters = executableElement.getParameters();
            if (bindingAdapter.value().length == 0) {
                android.databinding.tool.util.L.e(element, "@BindingAdapter requires at least one attribute. %s", element);
                continue;
            }
            final boolean takesComponent = android.databinding.annotationprocessor.ProcessMethodAdapters.takesComponent(executableElement, processingEnv);
            final int startIndex = 1 + (takesComponent ? 1 : 0);
            final int numAttributes = bindingAdapter.value().length;
            final int numAdditionalArgs = parameters.size() - startIndex;
            if (numAdditionalArgs == (2 * numAttributes)) {
                // This BindingAdapter takes old and new values. Make sure they are properly ordered
                javax.lang.model.util.Types typeUtils = processingEnv.getTypeUtils();
                boolean hasParameterError = false;
                for (int i = startIndex; i < (numAttributes + startIndex); i++) {
                    if (!typeUtils.isSameType(parameters.get(i).asType(), parameters.get(i + numAttributes).asType())) {
                        android.databinding.tool.util.L.e(executableElement, "BindingAdapter %s: old values should be followed " + ("by new values. Parameter %d must be the same type as parameter " + "%d."), executableElement, i + 1, (i + numAttributes) + 1);
                        hasParameterError = true;
                        break;
                    }
                }
                if (hasParameterError) {
                    continue;
                }
            } else
                if (numAdditionalArgs != numAttributes) {
                    android.databinding.tool.util.L.e(element, "@BindingAdapter %s has %d attributes and %d value " + "parameters. There should be %d or %d value parameters.", executableElement, numAttributes, numAdditionalArgs, numAttributes, numAttributes * 2);
                    continue;
                }

            android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespaces(element, bindingAdapter.value());
            try {
                if (numAttributes == 1) {
                    final java.lang.String attribute = bindingAdapter.value()[0];
                    store.addBindingAdapter(processingEnv, attribute, executableElement, takesComponent);
                } else {
                    store.addBindingAdapter(processingEnv, bindingAdapter.value(), executableElement, takesComponent, bindingAdapter.requireAll());
                }
            } catch (java.lang.IllegalArgumentException e) {
                android.databinding.tool.util.L.e(element, "@BindingAdapter for duplicate View and parameter type: %s", element);
            }
        }
    }

    private static boolean takesComponent(javax.lang.model.element.ExecutableElement executableElement, javax.annotation.processing.ProcessingEnvironment processingEnvironment) {
        java.util.List<? extends javax.lang.model.element.VariableElement> parameters = executableElement.getParameters();
        javax.lang.model.util.Elements elementUtils = processingEnvironment.getElementUtils();
        javax.lang.model.type.TypeMirror viewElement = elementUtils.getTypeElement("android.view.View").asType();
        if (parameters.size() < 2) {
            return false;// Validation will fail in the caller

        }
        javax.lang.model.type.TypeMirror parameter1 = parameters.get(0).asType();
        javax.lang.model.util.Types typeUtils = processingEnvironment.getTypeUtils();
        if ((parameter1.getKind() == javax.lang.model.type.TypeKind.DECLARED) && typeUtils.isAssignable(parameter1, viewElement)) {
            return false;// first parameter is a View

        }
        if (parameters.size() < 3) {
            javax.lang.model.type.TypeMirror viewStubProxy = elementUtils.getTypeElement("android.databinding.ViewStubProxy").asType();
            if (!typeUtils.isAssignable(parameter1, viewStubProxy)) {
                android.databinding.tool.util.L.e(executableElement, "@BindingAdapter %s is applied to a method that has two " + "parameters, the first must be a View type", executableElement);
            }
            return false;
        }
        javax.lang.model.type.TypeMirror parameter2 = parameters.get(1).asType();
        if (typeUtils.isAssignable(parameter2, viewElement)) {
            return true;// second parameter is a View

        }
        android.databinding.tool.util.L.e(executableElement, "@BindingAdapter %s is applied to a method that doesn't take a " + (("View subclass as the first or second parameter. When a BindingAdapter uses a " + "DataBindingComponent, the component parameter is first and the View ") + "parameter is second, otherwise the View parameter is first."), executableElement);
        return false;
    }

    private static void warnAttributeNamespace(javax.lang.model.element.Element element, java.lang.String attribute) {
        if (attribute.contains(":") && (!attribute.startsWith("android:"))) {
            android.databinding.tool.util.L.w(element, "Application namespace for attribute %s will be ignored.", attribute);
        }
    }

    private static void warnAttributeNamespaces(javax.lang.model.element.Element element, java.lang.String[] attributes) {
        for (java.lang.String attribute : attributes) {
            android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespace(element, attribute);
        }
    }

    private void addRenamed(javax.annotation.processing.RoundEnvironment roundEnv, android.databinding.tool.store.SetterStore store) {
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.BindingMethods.class)) {
            android.databinding.BindingMethods bindingMethods = element.getAnnotation(android.databinding.BindingMethods.class);
            for (android.databinding.BindingMethod bindingMethod : bindingMethods.value()) {
                final java.lang.String attribute = bindingMethod.attribute();
                final java.lang.String method = bindingMethod.method();
                android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespace(element, attribute);
                java.lang.String type;
                try {
                    type = bindingMethod.type().getCanonicalName();
                } catch (javax.lang.model.type.MirroredTypeException e) {
                    type = e.getTypeMirror().toString();
                }
                store.addRenamedMethod(attribute, type, method, ((javax.lang.model.element.TypeElement) (element)));
            }
        }
    }

    private void addConversions(javax.annotation.processing.RoundEnvironment roundEnv, android.databinding.tool.store.SetterStore store) {
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.BindingConversion.class)) {
            if (((element.getKind() != javax.lang.model.element.ElementKind.METHOD) || (!element.getModifiers().contains(javax.lang.model.element.Modifier.STATIC))) || (!element.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC))) {
                android.databinding.tool.util.L.e(element, "@BindingConversion is only allowed on public static methods %s", element);
                continue;
            }
            javax.lang.model.element.ExecutableElement executableElement = ((javax.lang.model.element.ExecutableElement) (element));
            if (executableElement.getParameters().size() != 1) {
                android.databinding.tool.util.L.e(element, "@BindingConversion method should have one parameter %s", element);
                continue;
            }
            if (executableElement.getReturnType().getKind() == javax.lang.model.type.TypeKind.VOID) {
                android.databinding.tool.util.L.e(element, "@BindingConversion method must return a value %s", element);
                continue;
            }
            store.addConversionMethod(executableElement);
        }
    }

    private void addInverseAdapters(javax.annotation.processing.RoundEnvironment roundEnv, javax.annotation.processing.ProcessingEnvironment processingEnv, android.databinding.tool.store.SetterStore store) {
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.InverseBindingAdapter.class)) {
            if (!element.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)) {
                android.databinding.tool.util.L.e(element, "@InverseBindingAdapter must be associated with a public method");
                continue;
            }
            javax.lang.model.element.ExecutableElement executableElement = ((javax.lang.model.element.ExecutableElement) (element));
            if (executableElement.getReturnType().getKind() == javax.lang.model.type.TypeKind.VOID) {
                android.databinding.tool.util.L.e(element, "@InverseBindingAdapter must have a non-void return type");
                continue;
            }
            final android.databinding.InverseBindingAdapter inverseBindingAdapter = executableElement.getAnnotation(android.databinding.InverseBindingAdapter.class);
            final java.lang.String attribute = inverseBindingAdapter.attribute();
            android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespace(element, attribute);
            final java.lang.String event = (inverseBindingAdapter.event().isEmpty()) ? inverseBindingAdapter.attribute() + android.databinding.annotationprocessor.ProcessMethodAdapters.INVERSE_BINDING_EVENT_ATTR_SUFFIX : inverseBindingAdapter.event();
            android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespace(element, event);
            final boolean takesComponent = android.databinding.annotationprocessor.ProcessMethodAdapters.takesComponent(executableElement, processingEnv);
            final int expectedArgs = (takesComponent) ? 2 : 1;
            final int numParameters = executableElement.getParameters().size();
            if (numParameters != expectedArgs) {
                android.databinding.tool.util.L.e(element, "@InverseBindingAdapter %s takes %s parameters, but %s parameters " + "were expected", element, numParameters, expectedArgs);
                continue;
            }
            try {
                store.addInverseAdapter(processingEnv, attribute, event, executableElement, takesComponent);
            } catch (java.lang.IllegalArgumentException e) {
                android.databinding.tool.util.L.e(element, "@InverseBindingAdapter for duplicate View and parameter type: %s", element);
            }
        }
    }

    private void addInverseMethods(javax.annotation.processing.RoundEnvironment roundEnv, android.databinding.tool.store.SetterStore store) {
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.InverseBindingMethods.class)) {
            android.databinding.InverseBindingMethods bindingMethods = element.getAnnotation(android.databinding.InverseBindingMethods.class);
            for (android.databinding.InverseBindingMethod bindingMethod : bindingMethods.value()) {
                final java.lang.String attribute = bindingMethod.attribute();
                final java.lang.String method = bindingMethod.method();
                final java.lang.String event = (bindingMethod.event().isEmpty()) ? bindingMethod.attribute() + android.databinding.annotationprocessor.ProcessMethodAdapters.INVERSE_BINDING_EVENT_ATTR_SUFFIX : bindingMethod.event();
                android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespace(element, attribute);
                android.databinding.annotationprocessor.ProcessMethodAdapters.warnAttributeNamespace(element, event);
                java.lang.String type;
                try {
                    type = bindingMethod.type().getCanonicalName();
                } catch (javax.lang.model.type.MirroredTypeException e) {
                    type = e.getTypeMirror().toString();
                }
                store.addInverseMethod(attribute, event, type, method, ((javax.lang.model.element.TypeElement) (element)));
            }
        }
    }

    private void addUntaggable(javax.annotation.processing.RoundEnvironment roundEnv, android.databinding.tool.store.SetterStore store) {
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.Untaggable.class)) {
            android.databinding.Untaggable untaggable = element.getAnnotation(android.databinding.Untaggable.class);
            store.addUntaggableTypes(untaggable.value(), ((javax.lang.model.element.TypeElement) (element)));
        }
    }

    private void clearIncrementalClasses(javax.annotation.processing.RoundEnvironment roundEnv, android.databinding.tool.store.SetterStore store) {
        java.util.HashSet<java.lang.String> classes = new java.util.HashSet<java.lang.String>();
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.BindingAdapter.class)) {
            javax.lang.model.element.TypeElement containingClass = ((javax.lang.model.element.TypeElement) (element.getEnclosingElement()));
            classes.add(containingClass.getQualifiedName().toString());
        }
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.BindingMethods.class)) {
            classes.add(((javax.lang.model.element.TypeElement) (element)).getQualifiedName().toString());
        }
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.BindingConversion.class)) {
            classes.add(((javax.lang.model.element.TypeElement) (element.getEnclosingElement())).getQualifiedName().toString());
        }
        for (javax.lang.model.element.Element element : android.databinding.annotationprocessor.AnnotationUtil.getElementsAnnotatedWith(roundEnv, android.databinding.Untaggable.class)) {
            classes.add(((javax.lang.model.element.TypeElement) (element)).getQualifiedName().toString());
        }
        store.clear(classes);
    }
}

