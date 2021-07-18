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
package android.databinding.tool.store;


public class SetterStore {
    private static android.databinding.tool.store.SetterStore sStore;

    private final android.databinding.tool.store.SetterStore.IntermediateV2 mStore;

    private final android.databinding.tool.reflection.ModelAnalyzer mClassAnalyzer;

    private java.util.HashMap<java.lang.String, java.util.List<java.lang.String>> mInstanceAdapters;

    private final java.util.HashSet<java.lang.String> mInverseEventAttributes = new java.util.HashSet<java.lang.String>();

    private java.util.Comparator<android.databinding.tool.store.SetterStore.MultiAttributeSetter> COMPARE_MULTI_ATTRIBUTE_SETTERS = new java.util.Comparator<android.databinding.tool.store.SetterStore.MultiAttributeSetter>() {
        @java.lang.Override
        public int compare(android.databinding.tool.store.SetterStore.MultiAttributeSetter o1, android.databinding.tool.store.SetterStore.MultiAttributeSetter o2) {
            if (o1.attributes.length != o2.attributes.length) {
                return o2.attributes.length - o1.attributes.length;
            }
            android.databinding.tool.reflection.ModelClass view1 = mClassAnalyzer.findClass(o1.mKey.viewType, null).erasure();
            android.databinding.tool.reflection.ModelClass view2 = mClassAnalyzer.findClass(o2.mKey.viewType, null).erasure();
            if (!view1.equals(view2)) {
                if (view1.isAssignableFrom(view2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
            if (!o1.mKey.attributeIndices.keySet().equals(o2.mKey.attributeIndices.keySet())) {
                // order by attribute name
                java.util.Iterator<java.lang.String> o1Keys = o1.mKey.attributeIndices.keySet().iterator();
                java.util.Iterator<java.lang.String> o2Keys = o2.mKey.attributeIndices.keySet().iterator();
                while (o1Keys.hasNext()) {
                    java.lang.String key1 = o1Keys.next();
                    java.lang.String key2 = o2Keys.next();
                    int compare = key1.compareTo(key2);
                    if (compare != 0) {
                        return compare;
                    }
                } 
                android.databinding.tool.util.Preconditions.check(false, "The sets don't match! That means the keys shouldn't match also");
            }
            // Same view type. Same attributes
            for (java.lang.String attribute : o1.mKey.attributeIndices.keySet()) {
                final int index1 = o1.mKey.attributeIndices.get(attribute);
                final int index2 = o2.mKey.attributeIndices.get(attribute);
                android.databinding.tool.reflection.ModelClass type1 = mClassAnalyzer.findClass(o1.mKey.parameterTypes[index1], null);
                android.databinding.tool.reflection.ModelClass type2 = mClassAnalyzer.findClass(o2.mKey.parameterTypes[index2], null);
                if (type1.equals(type2)) {
                    continue;
                }
                if (o1.mCasts[index1] != null) {
                    if (o2.mCasts[index2] == null) {
                        return 1;// o2 is better

                    } else {
                        continue;// both are casts

                    }
                } else
                    if (o2.mCasts[index2] != null) {
                        return -1;// o1 is better

                    }

                if (o1.mConverters[index1] != null) {
                    if (o2.mConverters[index2] == null) {
                        return 1;// o2 is better

                    } else {
                        continue;// both are conversions

                    }
                } else
                    if (o2.mConverters[index2] != null) {
                        return -1;// o1 is better

                    }

                if (type1.isPrimitive()) {
                    if (type2.isPrimitive()) {
                        int type1ConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(type1);
                        int type2ConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(type2);
                        return type2ConversionLevel - type1ConversionLevel;
                    } else {
                        // type1 is primitive and has higher priority
                        return -1;
                    }
                } else
                    if (type2.isPrimitive()) {
                        return 1;
                    }

                if (type1.isAssignableFrom(type2)) {
                    return 1;
                } else
                    if (type2.isAssignableFrom(type1)) {
                        return -1;
                    }

            }
            // hmmm... same view type, same attributes, same parameter types... ?
            return 0;
        }
    };

    private SetterStore(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer, android.databinding.tool.store.SetterStore.IntermediateV2 store) {
        mClassAnalyzer = modelAnalyzer;
        mStore = store;
        for (java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription> adapter : mStore.inverseAdapters.values()) {
            for (android.databinding.tool.store.SetterStore.InverseDescription inverseDescription : adapter.values()) {
                mInverseEventAttributes.add(inverseDescription.event);
            }
        }
        for (java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.InverseDescription> method : mStore.inverseMethods.values()) {
            for (android.databinding.tool.store.SetterStore.InverseDescription inverseDescription : method.values()) {
                mInverseEventAttributes.add(inverseDescription.event);
            }
        }
    }

    public static android.databinding.tool.store.SetterStore get(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        if (android.databinding.tool.store.SetterStore.sStore == null) {
            android.databinding.tool.store.SetterStore.sStore = android.databinding.tool.store.SetterStore.load(modelAnalyzer);
        }
        return android.databinding.tool.store.SetterStore.sStore;
    }

    private static android.databinding.tool.store.SetterStore load(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        android.databinding.tool.store.SetterStore.IntermediateV2 store = new android.databinding.tool.store.SetterStore.IntermediateV2();
        java.util.List<android.databinding.tool.store.SetterStore.Intermediate> previousStores = android.databinding.tool.util.GenerationalClassUtil.loadObjects(android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.SETTER_STORE);
        for (android.databinding.tool.store.SetterStore.Intermediate intermediate : previousStores) {
            android.databinding.tool.store.SetterStore.merge(store, intermediate);
        }
        return new android.databinding.tool.store.SetterStore(modelAnalyzer, store);
    }

    public void addRenamedMethod(java.lang.String attribute, java.lang.String declaringClass, java.lang.String method, javax.lang.model.element.TypeElement declaredOn) {
        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription> renamed = mStore.renamedMethods.get(attribute);
        if (renamed == null) {
            renamed = new java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription>();
            mStore.renamedMethods.put(attribute, renamed);
        }
        android.databinding.tool.store.SetterStore.MethodDescription methodDescription = new android.databinding.tool.store.SetterStore.MethodDescription(declaredOn.getQualifiedName().toString(), method);
        android.databinding.tool.util.L.d("STORE addmethod desc %s", methodDescription);
        renamed.put(declaringClass, methodDescription);
    }

    public void addInverseMethod(java.lang.String attribute, java.lang.String event, java.lang.String declaringClass, java.lang.String method, javax.lang.model.element.TypeElement declaredOn) {
        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        event = android.databinding.tool.store.SetterStore.stripNamespace(event);
        java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.InverseDescription> inverseMethods = mStore.inverseMethods.get(attribute);
        if (inverseMethods == null) {
            inverseMethods = new java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.InverseDescription>();
            mStore.inverseMethods.put(attribute, inverseMethods);
        }
        android.databinding.tool.store.SetterStore.InverseDescription methodDescription = new android.databinding.tool.store.SetterStore.InverseDescription(declaredOn.getQualifiedName().toString(), method, event);
        android.databinding.tool.util.L.d("STORE addInverseMethod desc %s", methodDescription);
        inverseMethods.put(declaringClass, methodDescription);
    }

    public void addBindingAdapter(javax.annotation.processing.ProcessingEnvironment processingEnv, java.lang.String attribute, javax.lang.model.element.ExecutableElement bindingMethod, boolean takesComponent) {
        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        android.databinding.tool.util.L.d("STORE addBindingAdapter %s %s", attribute, bindingMethod);
        java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription> adapters = mStore.adapterMethods.get(attribute);
        if (adapters == null) {
            adapters = new java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription>();
            mStore.adapterMethods.put(attribute, adapters);
        }
        java.util.List<? extends javax.lang.model.element.VariableElement> parameters = bindingMethod.getParameters();
        final int viewIndex = (takesComponent) ? 1 : 0;
        javax.lang.model.type.TypeMirror viewType = android.databinding.tool.store.SetterStore.eraseType(processingEnv, parameters.get(viewIndex).asType());
        java.lang.String view = android.databinding.tool.store.SetterStore.getQualifiedName(viewType);
        javax.lang.model.type.TypeMirror parameterType = android.databinding.tool.store.SetterStore.eraseType(processingEnv, parameters.get(viewIndex + 1).asType());
        java.lang.String value = android.databinding.tool.store.SetterStore.getQualifiedName(parameterType);
        android.databinding.tool.store.SetterStore.AccessorKey key = new android.databinding.tool.store.SetterStore.AccessorKey(view, value);
        if (adapters.containsKey(key)) {
            throw new java.lang.IllegalArgumentException("Already exists!");
        }
        adapters.put(key, new android.databinding.tool.store.SetterStore.MethodDescription(bindingMethod, 1, takesComponent));
    }

    public void addInverseAdapter(javax.annotation.processing.ProcessingEnvironment processingEnv, java.lang.String attribute, java.lang.String event, javax.lang.model.element.ExecutableElement bindingMethod, boolean takesComponent) {
        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        event = android.databinding.tool.store.SetterStore.stripNamespace(event);
        android.databinding.tool.util.L.d("STORE addInverseAdapter %s %s", attribute, bindingMethod);
        java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription> adapters = mStore.inverseAdapters.get(attribute);
        if (adapters == null) {
            adapters = new java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription>();
            mStore.inverseAdapters.put(attribute, adapters);
        }
        java.util.List<? extends javax.lang.model.element.VariableElement> parameters = bindingMethod.getParameters();
        final int viewIndex = (takesComponent) ? 1 : 0;
        javax.lang.model.type.TypeMirror viewType = android.databinding.tool.store.SetterStore.eraseType(processingEnv, parameters.get(viewIndex).asType());
        java.lang.String view = android.databinding.tool.store.SetterStore.getQualifiedName(viewType);
        javax.lang.model.type.TypeMirror returnType = android.databinding.tool.store.SetterStore.eraseType(processingEnv, bindingMethod.getReturnType());
        java.lang.String value = android.databinding.tool.store.SetterStore.getQualifiedName(returnType);
        android.databinding.tool.store.SetterStore.AccessorKey key = new android.databinding.tool.store.SetterStore.AccessorKey(view, value);
        if (adapters.containsKey(key)) {
            throw new java.lang.IllegalArgumentException("Already exists!");
        }
        adapters.put(key, new android.databinding.tool.store.SetterStore.InverseDescription(bindingMethod, event, takesComponent));
    }

    private static javax.lang.model.type.TypeMirror eraseType(javax.annotation.processing.ProcessingEnvironment processingEnv, javax.lang.model.type.TypeMirror typeMirror) {
        if (android.databinding.tool.store.SetterStore.hasTypeVar(typeMirror)) {
            return processingEnv.getTypeUtils().erasure(typeMirror);
        } else {
            return typeMirror;
        }
    }

    private static android.databinding.tool.reflection.ModelClass eraseType(android.databinding.tool.reflection.ModelClass modelClass) {
        if (android.databinding.tool.store.SetterStore.hasTypeVar(modelClass)) {
            return modelClass.erasure();
        } else {
            return modelClass;
        }
    }

    private static boolean hasTypeVar(javax.lang.model.type.TypeMirror typeMirror) {
        javax.lang.model.type.TypeKind kind = typeMirror.getKind();
        if (kind == javax.lang.model.type.TypeKind.TYPEVAR) {
            return true;
        } else
            if (kind == javax.lang.model.type.TypeKind.ARRAY) {
                return android.databinding.tool.store.SetterStore.hasTypeVar(((javax.lang.model.type.ArrayType) (typeMirror)).getComponentType());
            } else
                if (kind == javax.lang.model.type.TypeKind.DECLARED) {
                    javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (typeMirror));
                    java.util.List<? extends javax.lang.model.type.TypeMirror> typeArguments = declaredType.getTypeArguments();
                    if ((typeArguments == null) || typeArguments.isEmpty()) {
                        return false;
                    }
                    for (javax.lang.model.type.TypeMirror arg : typeArguments) {
                        if (android.databinding.tool.store.SetterStore.hasTypeVar(arg)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    return false;
                }


    }

    private static boolean hasTypeVar(android.databinding.tool.reflection.ModelClass type) {
        if (type.isTypeVar()) {
            return true;
        } else
            if (type.isArray()) {
                return android.databinding.tool.store.SetterStore.hasTypeVar(type.getComponentType());
            } else {
                java.util.List<android.databinding.tool.reflection.ModelClass> typeArguments = type.getTypeArguments();
                if (typeArguments == null) {
                    return false;
                }
                for (android.databinding.tool.reflection.ModelClass arg : typeArguments) {
                    if (android.databinding.tool.store.SetterStore.hasTypeVar(arg)) {
                        return true;
                    }
                }
                return false;
            }

    }

    public void addBindingAdapter(javax.annotation.processing.ProcessingEnvironment processingEnv, java.lang.String[] attributes, javax.lang.model.element.ExecutableElement bindingMethod, boolean takesComponent, boolean requireAll) {
        android.databinding.tool.util.L.d("STORE add multi-value BindingAdapter %d %s", attributes.length, bindingMethod);
        android.databinding.tool.store.SetterStore.MultiValueAdapterKey key = new android.databinding.tool.store.SetterStore.MultiValueAdapterKey(processingEnv, bindingMethod, attributes, takesComponent, requireAll);
        android.databinding.tool.store.SetterStore.MethodDescription methodDescription = new android.databinding.tool.store.SetterStore.MethodDescription(bindingMethod, attributes.length, takesComponent);
        mStore.multiValueAdapters.put(key, methodDescription);
    }

    private static java.lang.String[] stripAttributes(java.lang.String[] attributes) {
        java.lang.String[] strippedAttributes = new java.lang.String[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != null) {
                strippedAttributes[i] = android.databinding.tool.store.SetterStore.stripNamespace(attributes[i]);
            }
        }
        return strippedAttributes;
    }

    public void addUntaggableTypes(java.lang.String[] typeNames, javax.lang.model.element.TypeElement declaredOn) {
        android.databinding.tool.util.L.d("STORE addUntaggableTypes %s %s", java.util.Arrays.toString(typeNames), declaredOn);
        java.lang.String declaredType = declaredOn.getQualifiedName().toString();
        for (java.lang.String type : typeNames) {
            mStore.untaggableTypes.put(type, declaredType);
        }
    }

    private static java.lang.String getQualifiedName(javax.lang.model.type.TypeMirror type) {
        final javax.lang.model.type.TypeKind kind = type.getKind();
        if (kind == javax.lang.model.type.TypeKind.ARRAY) {
            return android.databinding.tool.store.SetterStore.getQualifiedName(((javax.lang.model.type.ArrayType) (type)).getComponentType()) + "[]";
        } else
            if ((kind == javax.lang.model.type.TypeKind.DECLARED) && android.databinding.tool.store.SetterStore.isIncompleteType(type)) {
                javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (type));
                return declaredType.asElement().toString();
            } else {
                return type.toString();
            }

    }

    private static boolean isIncompleteType(javax.lang.model.type.TypeMirror type) {
        final javax.lang.model.type.TypeKind kind = type.getKind();
        if ((kind == javax.lang.model.type.TypeKind.TYPEVAR) || (kind == javax.lang.model.type.TypeKind.WILDCARD)) {
            return true;
        } else
            if (kind == javax.lang.model.type.TypeKind.DECLARED) {
                javax.lang.model.type.DeclaredType declaredType = ((javax.lang.model.type.DeclaredType) (type));
                java.util.List<? extends javax.lang.model.type.TypeMirror> typeArgs = declaredType.getTypeArguments();
                if (typeArgs == null) {
                    return false;
                }
                for (javax.lang.model.type.TypeMirror arg : typeArgs) {
                    if (android.databinding.tool.store.SetterStore.isIncompleteType(arg)) {
                        return true;
                    }
                }
            }

        return false;
    }

    public void addConversionMethod(javax.lang.model.element.ExecutableElement conversionMethod) {
        android.databinding.tool.util.L.d("STORE addConversionMethod %s", conversionMethod);
        java.util.List<? extends javax.lang.model.element.VariableElement> parameters = conversionMethod.getParameters();
        java.lang.String fromType = android.databinding.tool.store.SetterStore.getQualifiedName(parameters.get(0).asType());
        java.lang.String toType = android.databinding.tool.store.SetterStore.getQualifiedName(conversionMethod.getReturnType());
        android.databinding.tool.store.SetterStore.MethodDescription methodDescription = new android.databinding.tool.store.SetterStore.MethodDescription(conversionMethod, 1, false);
        java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription> convertTo = mStore.conversionMethods.get(fromType);
        if (convertTo == null) {
            convertTo = new java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription>();
            mStore.conversionMethods.put(fromType, convertTo);
        }
        convertTo.put(toType, methodDescription);
    }

    public void clear(java.util.Set<java.lang.String> classes) {
        java.util.ArrayList<android.databinding.tool.store.SetterStore.AccessorKey> removedAccessorKeys = new java.util.ArrayList<android.databinding.tool.store.SetterStore.AccessorKey>();
        for (java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription> adapters : mStore.adapterMethods.values()) {
            for (android.databinding.tool.store.SetterStore.AccessorKey key : adapters.keySet()) {
                android.databinding.tool.store.SetterStore.MethodDescription description = adapters.get(key);
                if (classes.contains(description.type)) {
                    removedAccessorKeys.add(key);
                }
            }
            android.databinding.tool.store.SetterStore.removeFromMap(adapters, removedAccessorKeys);
        }
        java.util.ArrayList<java.lang.String> removedRenamed = new java.util.ArrayList<java.lang.String>();
        for (java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription> renamed : mStore.renamedMethods.values()) {
            for (java.lang.String key : renamed.keySet()) {
                if (classes.contains(renamed.get(key).type)) {
                    removedRenamed.add(key);
                }
            }
            android.databinding.tool.store.SetterStore.removeFromMap(renamed, removedRenamed);
        }
        java.util.ArrayList<java.lang.String> removedConversions = new java.util.ArrayList<java.lang.String>();
        for (java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription> convertTos : mStore.conversionMethods.values()) {
            for (java.lang.String toType : convertTos.keySet()) {
                android.databinding.tool.store.SetterStore.MethodDescription methodDescription = convertTos.get(toType);
                if (classes.contains(methodDescription.type)) {
                    removedConversions.add(toType);
                }
            }
            android.databinding.tool.store.SetterStore.removeFromMap(convertTos, removedConversions);
        }
        java.util.ArrayList<java.lang.String> removedUntaggable = new java.util.ArrayList<java.lang.String>();
        for (java.lang.String typeName : mStore.untaggableTypes.keySet()) {
            if (classes.contains(mStore.untaggableTypes.get(typeName))) {
                removedUntaggable.add(typeName);
            }
        }
        android.databinding.tool.store.SetterStore.removeFromMap(mStore.untaggableTypes, removedUntaggable);
    }

    private static <K, V> void removeFromMap(java.util.Map<K, V> map, java.util.List<K> keys) {
        for (K key : keys) {
            map.remove(key);
        }
        keys.clear();
    }

    public void write(java.lang.String projectPackage, javax.annotation.processing.ProcessingEnvironment processingEnvironment) throws java.io.IOException {
        android.databinding.tool.util.GenerationalClassUtil.writeIntermediateFile(processingEnvironment, projectPackage, projectPackage + android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.SETTER_STORE.getExtension(), mStore);
    }

    private static java.lang.String stripNamespace(java.lang.String attribute) {
        if (!attribute.startsWith("android:")) {
            int colon = attribute.indexOf(':');
            if (colon >= 0) {
                attribute = attribute.substring(colon + 1);
            }
        }
        return attribute;
    }

    public boolean isTwoWayEventAttribute(java.lang.String attribute) {
        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        return mInverseEventAttributes.contains(attribute);
    }

    public java.util.List<android.databinding.tool.store.SetterStore.MultiAttributeSetter> getMultiAttributeSetterCalls(java.lang.String[] attributes, android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.reflection.ModelClass[] valueType) {
        attributes = android.databinding.tool.store.SetterStore.stripAttributes(attributes);
        final java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter> calls = new java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter>();
        if ((viewType != null) && viewType.isGeneric()) {
            java.util.List<android.databinding.tool.reflection.ModelClass> viewGenerics = viewType.getTypeArguments();
            for (int i = 0; i < valueType.length; i++) {
                valueType[i] = android.databinding.tool.store.SetterStore.eraseType(valueType[i], viewGenerics);
            }
            viewType = viewType.erasure();
        }
        java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter> matching = getMatchingMultiAttributeSetters(attributes, viewType, valueType);
        java.util.Collections.sort(matching, COMPARE_MULTI_ATTRIBUTE_SETTERS);
        while (!matching.isEmpty()) {
            android.databinding.tool.store.SetterStore.MultiAttributeSetter bestMatch = matching.get(0);
            calls.add(bestMatch);
            android.databinding.tool.store.SetterStore.removeConsumedAttributes(matching, bestMatch.attributes);
        } 
        return calls;
    }

    private static java.lang.String simpleName(java.lang.String className) {
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex < 0) {
            return className;
        } else {
            return className.substring(dotIndex + 1);
        }
    }

    public java.util.Map<java.lang.String, java.util.List<java.lang.String>> getComponentBindingAdapters() {
        ensureInstanceAdapters();
        return mInstanceAdapters;
    }

    private java.lang.String getBindingAdapterCall(java.lang.String className) {
        ensureInstanceAdapters();
        final java.lang.String simpleName = android.databinding.tool.store.SetterStore.simpleName(className);
        java.util.List<java.lang.String> adapters = mInstanceAdapters.get(simpleName);
        if (adapters.size() == 1) {
            return ("get" + simpleName) + "()";
        } else {
            int index = adapters.indexOf(className) + 1;
            return (("get" + simpleName) + index) + "()";
        }
    }

    private void ensureInstanceAdapters() {
        if (mInstanceAdapters == null) {
            java.util.HashSet<java.lang.String> adapters = new java.util.HashSet<java.lang.String>();
            for (java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription> methods : mStore.adapterMethods.values()) {
                for (android.databinding.tool.store.SetterStore.MethodDescription method : methods.values()) {
                    if (!method.isStatic) {
                        adapters.add(method.type);
                    }
                }
            }
            for (android.databinding.tool.store.SetterStore.MethodDescription method : mStore.multiValueAdapters.values()) {
                if (!method.isStatic) {
                    adapters.add(method.type);
                }
            }
            for (java.util.Map<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription> methods : mStore.inverseAdapters.values()) {
                for (android.databinding.tool.store.SetterStore.InverseDescription method : methods.values()) {
                    if (!method.isStatic) {
                        adapters.add(method.type);
                    }
                }
            }
            mInstanceAdapters = new java.util.HashMap<java.lang.String, java.util.List<java.lang.String>>();
            for (java.lang.String adapter : adapters) {
                final java.lang.String simpleName = android.databinding.tool.store.SetterStore.simpleName(adapter);
                java.util.List<java.lang.String> list = mInstanceAdapters.get(simpleName);
                if (list == null) {
                    list = new java.util.ArrayList<java.lang.String>();
                    mInstanceAdapters.put(simpleName, list);
                }
                list.add(adapter);
            }
            for (java.util.List<java.lang.String> list : mInstanceAdapters.values()) {
                if (list.size() > 1) {
                    java.util.Collections.sort(list);
                }
            }
        }
    }

    // Removes all MultiAttributeSetters that require any of the values in attributes
    private static void removeConsumedAttributes(java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter> matching, java.lang.String[] attributes) {
        for (int i = matching.size() - 1; i >= 0; i--) {
            final android.databinding.tool.store.SetterStore.MultiAttributeSetter setter = matching.get(i);
            boolean found = false;
            for (java.lang.String attribute : attributes) {
                if (android.databinding.tool.store.SetterStore.isInArray(attribute, setter.attributes)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                matching.remove(i);
            }
        }
    }

    // Linear search through the String array for a specific value.
    private static boolean isInArray(java.lang.String str, java.lang.String[] array) {
        for (java.lang.String value : array) {
            if (value.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter> getMatchingMultiAttributeSetters(java.lang.String[] attributes, android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.reflection.ModelClass[] valueType) {
        final java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter> setters = new java.util.ArrayList<android.databinding.tool.store.SetterStore.MultiAttributeSetter>();
        for (android.databinding.tool.store.SetterStore.MultiValueAdapterKey adapter : mStore.multiValueAdapters.keySet()) {
            if (adapter.requireAll && (adapter.attributes.length > attributes.length)) {
                continue;
            }
            android.databinding.tool.reflection.ModelClass viewClass = mClassAnalyzer.findClass(adapter.viewType, null);
            if (viewClass.isGeneric()) {
                viewClass = viewClass.erasure();
            }
            if (!viewClass.isAssignableFrom(viewType)) {
                continue;
            }
            final android.databinding.tool.store.SetterStore.MethodDescription method = mStore.multiValueAdapters.get(adapter);
            final android.databinding.tool.store.SetterStore.MultiAttributeSetter setter = createMultiAttributeSetter(method, attributes, valueType, adapter);
            if (setter != null) {
                setters.add(setter);
            }
        }
        return setters;
    }

    private android.databinding.tool.store.SetterStore.MultiAttributeSetter createMultiAttributeSetter(android.databinding.tool.store.SetterStore.MethodDescription method, java.lang.String[] allAttributes, android.databinding.tool.reflection.ModelClass[] attributeValues, android.databinding.tool.store.SetterStore.MultiValueAdapterKey adapter) {
        int matchingAttributes = 0;
        java.lang.String[] casts = new java.lang.String[adapter.attributes.length];
        android.databinding.tool.store.SetterStore.MethodDescription[] conversions = new android.databinding.tool.store.SetterStore.MethodDescription[adapter.attributes.length];
        boolean[] supplied = new boolean[adapter.attributes.length];
        for (int i = 0; i < allAttributes.length; i++) {
            java.lang.Integer index = adapter.attributeIndices.get(allAttributes[i]);
            if (index != null) {
                supplied[index] = true;
                matchingAttributes++;
                final java.lang.String parameterTypeStr = adapter.parameterTypes[index];
                final android.databinding.tool.reflection.ModelClass parameterType = android.databinding.tool.store.SetterStore.eraseType(mClassAnalyzer.findClass(parameterTypeStr, null));
                final android.databinding.tool.reflection.ModelClass attributeType = attributeValues[i];
                if (!parameterType.isAssignableFrom(attributeType)) {
                    if (android.databinding.tool.reflection.ModelMethod.isBoxingConversion(parameterType, attributeType)) {
                        // automatic boxing is ok
                        continue;
                    } else
                        if (android.databinding.tool.reflection.ModelMethod.isImplicitConversion(attributeType, parameterType)) {
                            // implicit conversion is ok
                            continue;
                        }

                    // Look for a converter
                    conversions[index] = getConversionMethod(attributeType, parameterType, null);
                    if (conversions[index] == null) {
                        if (attributeType.isObject()) {
                            // Cast is allowed also
                            casts[index] = parameterTypeStr;
                        } else {
                            // Parameter type mismatch
                            return null;
                        }
                    }
                }
            }
        }
        if ((adapter.requireAll && (matchingAttributes != adapter.attributes.length)) || (matchingAttributes == 0)) {
            return null;
        } else {
            return new android.databinding.tool.store.SetterStore.MultiAttributeSetter(adapter, supplied, method, conversions, casts);
        }
    }

    public android.databinding.tool.store.SetterStore.SetterCall getSetterCall(java.lang.String attribute, android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.reflection.ModelClass valueType, java.util.Map<java.lang.String, java.lang.String> imports) {
        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        android.databinding.tool.store.SetterStore.SetterCall setterCall = null;
        android.databinding.tool.store.SetterStore.MethodDescription conversionMethod = null;
        if (viewType != null) {
            viewType = viewType.erasure();
            java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription> adapters = mStore.adapterMethods.get(attribute);
            android.databinding.tool.reflection.ModelMethod bestSetterMethod = getBestSetter(viewType, valueType, attribute, imports);
            android.databinding.tool.reflection.ModelClass bestViewType = null;
            android.databinding.tool.reflection.ModelClass bestValueType = null;
            if (bestSetterMethod != null) {
                bestViewType = bestSetterMethod.getDeclaringClass();
                bestValueType = bestSetterMethod.getParameterTypes()[0];
                setterCall = new android.databinding.tool.store.SetterStore.ModelMethodSetter(bestSetterMethod);
            }
            if (adapters != null) {
                for (android.databinding.tool.store.SetterStore.AccessorKey key : adapters.keySet()) {
                    try {
                        android.databinding.tool.reflection.ModelClass adapterViewType = mClassAnalyzer.findClass(key.viewType, imports).erasure();
                        if ((adapterViewType != null) && adapterViewType.isAssignableFrom(viewType)) {
                            try {
                                android.databinding.tool.util.L.d("setter parameter type is %s", key.valueType);
                                final android.databinding.tool.reflection.ModelClass adapterValueType = android.databinding.tool.store.SetterStore.eraseType(mClassAnalyzer.findClass(key.valueType, imports));
                                android.databinding.tool.util.L.d("setter %s takes type %s, compared to %s", adapters.get(key).method, adapterValueType.toJavaCode(), valueType.toJavaCode());
                                boolean isBetterView = (bestViewType == null) || bestViewType.isAssignableFrom(adapterViewType);
                                if (isBetterParameter(valueType, adapterValueType, bestValueType, isBetterView, imports)) {
                                    bestViewType = adapterViewType;
                                    bestValueType = adapterValueType;
                                    android.databinding.tool.store.SetterStore.MethodDescription adapter = adapters.get(key);
                                    setterCall = new android.databinding.tool.store.SetterStore.AdapterSetter(adapter, adapterValueType);
                                }
                            } catch (java.lang.Exception e) {
                                android.databinding.tool.util.L.e(e, "Unknown class: %s", key.valueType);
                            }
                        }
                    } catch (java.lang.Exception e) {
                        android.databinding.tool.util.L.e(e, "Unknown class: %s", key.viewType);
                    }
                }
            }
            conversionMethod = getConversionMethod(valueType, bestValueType, imports);
            if ((valueType.isObject() && (setterCall != null)) && bestValueType.isNullable()) {
                setterCall.setCast(bestValueType);
            }
        }
        if (setterCall == null) {
            if ((viewType != null) && (!viewType.isViewDataBinding())) {
                return null;// no setter found!!

            }
            setterCall = new android.databinding.tool.store.SetterStore.DummySetter(android.databinding.tool.store.SetterStore.getDefaultSetter(attribute));
        }
        setterCall.setConverter(conversionMethod);
        return setterCall;
    }

    public android.databinding.tool.store.SetterStore.BindingGetterCall getGetterCall(java.lang.String attribute, android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.reflection.ModelClass valueType, java.util.Map<java.lang.String, java.lang.String> imports) {
        if (viewType == null) {
            return null;
        } else
            if (viewType.isViewDataBinding()) {
                return new android.databinding.tool.store.SetterStore.ViewDataBindingGetterCall(attribute);
            }

        attribute = android.databinding.tool.store.SetterStore.stripNamespace(attribute);
        viewType = viewType.erasure();
        android.databinding.tool.store.SetterStore.InverseMethod bestMethod = getBestGetter(viewType, valueType, attribute, imports);
        java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription> adapters = mStore.inverseAdapters.get(attribute);
        if (adapters != null) {
            for (android.databinding.tool.store.SetterStore.AccessorKey key : adapters.keySet()) {
                try {
                    android.databinding.tool.reflection.ModelClass adapterViewType = mClassAnalyzer.findClass(key.viewType, imports).erasure();
                    if ((adapterViewType != null) && adapterViewType.isAssignableFrom(viewType)) {
                        try {
                            android.databinding.tool.util.L.d("getter return type is %s", key.valueType);
                            final android.databinding.tool.reflection.ModelClass adapterValueType = android.databinding.tool.store.SetterStore.eraseType(mClassAnalyzer.findClass(key.valueType, imports));
                            android.databinding.tool.util.L.d("getter %s returns type %s, compared to %s", adapters.get(key).method, adapterValueType.toJavaCode(), valueType);
                            boolean isBetterView = (bestMethod.viewType == null) || bestMethod.viewType.isAssignableFrom(adapterViewType);
                            if ((valueType == null) || isBetterParameter(adapterValueType, valueType, bestMethod.returnType, isBetterView, imports)) {
                                bestMethod.viewType = adapterViewType;
                                bestMethod.returnType = adapterValueType;
                                android.databinding.tool.store.SetterStore.InverseDescription inverseDescription = adapters.get(key);
                                android.databinding.tool.reflection.ModelClass listenerType = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass(android.databinding.InverseBindingListener.class);
                                android.databinding.tool.store.SetterStore.BindingSetterCall eventCall = getSetterCall(inverseDescription.event, viewType, listenerType, imports);
                                if (eventCall == null) {
                                    java.util.List<android.databinding.tool.store.SetterStore.MultiAttributeSetter> setters = getMultiAttributeSetterCalls(new java.lang.String[]{ inverseDescription.event }, viewType, new android.databinding.tool.reflection.ModelClass[]{ listenerType });
                                    if (setters.size() != 1) {
                                        android.databinding.tool.util.L.e("Could not find event '%s' on View type '%s'", inverseDescription.event, viewType.getCanonicalName());
                                    } else {
                                        bestMethod.call = new android.databinding.tool.store.SetterStore.AdapterGetter(inverseDescription, setters.get(0));
                                    }
                                } else {
                                    bestMethod.call = new android.databinding.tool.store.SetterStore.AdapterGetter(inverseDescription, eventCall);
                                }
                            }
                        } catch (java.lang.Exception e) {
                            android.databinding.tool.util.L.e(e, "Unknown class: %s", key.valueType);
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.databinding.tool.util.L.e(e, "Unknown class: %s", key.viewType);
                }
            }
        }
        return bestMethod.call;
    }

    public boolean isUntaggable(java.lang.String viewType) {
        return mStore.untaggableTypes.containsKey(viewType);
    }

    private android.databinding.tool.reflection.ModelMethod getBestSetter(android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.reflection.ModelClass argumentType, java.lang.String attribute, java.util.Map<java.lang.String, java.lang.String> imports) {
        if (viewType.isGeneric()) {
            argumentType = android.databinding.tool.store.SetterStore.eraseType(argumentType, viewType.getTypeArguments());
            viewType = viewType.erasure();
        }
        java.util.List<java.lang.String> setterCandidates = new java.util.ArrayList<java.lang.String>();
        java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription> renamed = mStore.renamedMethods.get(attribute);
        if (renamed != null) {
            for (java.lang.String className : renamed.keySet()) {
                try {
                    android.databinding.tool.reflection.ModelClass renamedViewType = mClassAnalyzer.findClass(className, imports);
                    if (renamedViewType.erasure().isAssignableFrom(viewType)) {
                        setterCandidates.add(renamed.get(className).method);
                        break;
                    }
                } catch (java.lang.Exception e) {
                    // printMessage(Diagnostic.Kind.NOTE, "Unknown class: " + className);
                }
            }
        }
        setterCandidates.add(android.databinding.tool.store.SetterStore.getDefaultSetter(attribute));
        setterCandidates.add(android.databinding.tool.store.SetterStore.trimAttributeNamespace(attribute));
        android.databinding.tool.reflection.ModelMethod bestMethod = null;
        android.databinding.tool.reflection.ModelClass bestParameterType = null;
        java.util.List<android.databinding.tool.reflection.ModelClass> args = new java.util.ArrayList<android.databinding.tool.reflection.ModelClass>();
        args.add(argumentType);
        for (java.lang.String name : setterCandidates) {
            android.databinding.tool.reflection.ModelMethod[] methods = viewType.getMethods(name, 1);
            for (android.databinding.tool.reflection.ModelMethod method : methods) {
                android.databinding.tool.reflection.ModelClass[] parameterTypes = method.getParameterTypes();
                android.databinding.tool.reflection.ModelClass param = parameterTypes[0];
                if (method.isVoid() && isBetterParameter(argumentType, param, bestParameterType, true, imports)) {
                    bestParameterType = param;
                    bestMethod = method;
                }
            }
        }
        return bestMethod;
    }

    private android.databinding.tool.store.SetterStore.InverseMethod getBestGetter(android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.reflection.ModelClass valueType, java.lang.String attribute, java.util.Map<java.lang.String, java.lang.String> imports) {
        if (viewType.isGeneric()) {
            if (valueType != null) {
                valueType = android.databinding.tool.store.SetterStore.eraseType(valueType, viewType.getTypeArguments());
            }
            viewType = viewType.erasure();
        }
        android.databinding.tool.reflection.ModelClass bestReturnType = null;
        android.databinding.tool.store.SetterStore.InverseDescription bestDescription = null;
        android.databinding.tool.reflection.ModelClass bestViewType = null;
        android.databinding.tool.reflection.ModelMethod bestMethod = null;
        java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.InverseDescription> inverseMethods = mStore.inverseMethods.get(attribute);
        if (inverseMethods != null) {
            for (java.lang.String className : inverseMethods.keySet()) {
                try {
                    android.databinding.tool.reflection.ModelClass methodViewType = mClassAnalyzer.findClass(className, imports);
                    if (methodViewType.erasure().isAssignableFrom(viewType)) {
                        boolean isBetterViewType = (bestViewType == null) || bestViewType.isAssignableFrom(methodViewType);
                        final android.databinding.tool.store.SetterStore.InverseDescription inverseDescription = inverseMethods.get(className);
                        final java.lang.String name = (inverseDescription.method.isEmpty()) ? android.databinding.tool.store.SetterStore.trimAttributeNamespace(attribute) : inverseDescription.method;
                        android.databinding.tool.reflection.ModelMethod method = methodViewType.findInstanceGetter(name);
                        android.databinding.tool.reflection.ModelClass returnType = method.getReturnType(null);// no parameters

                        if (((valueType == null) || (bestReturnType == null)) || isBetterParameter(returnType, valueType, bestReturnType, isBetterViewType, imports)) {
                            bestDescription = inverseDescription;
                            bestReturnType = returnType;
                            bestViewType = methodViewType;
                            bestMethod = method;
                        }
                    }
                } catch (java.lang.Exception e) {
                    // printMessage(Diagnostic.Kind.NOTE, "Unknown class: " + className);
                }
            }
        }
        android.databinding.tool.store.SetterStore.BindingGetterCall call = null;
        if (bestDescription != null) {
            final android.databinding.tool.reflection.ModelClass listenerType = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass(android.databinding.InverseBindingListener.class);
            android.databinding.tool.store.SetterStore.SetterCall eventSetter = getSetterCall(bestDescription.event, viewType, listenerType, imports);
            if (eventSetter == null) {
                java.util.List<android.databinding.tool.store.SetterStore.MultiAttributeSetter> setters = getMultiAttributeSetterCalls(new java.lang.String[]{ bestDescription.event }, viewType, new android.databinding.tool.reflection.ModelClass[]{ listenerType });
                if (setters.size() != 1) {
                    android.databinding.tool.util.L.e("Could not find event '%s' on View type '%s'", bestDescription.event, viewType.getCanonicalName());
                    bestViewType = null;
                    bestReturnType = null;
                } else {
                    call = new android.databinding.tool.store.SetterStore.ViewGetterCall(bestDescription, bestMethod, setters.get(0));
                }
            } else {
                call = new android.databinding.tool.store.SetterStore.ViewGetterCall(bestDescription, bestMethod, eventSetter);
            }
        }
        return new android.databinding.tool.store.SetterStore.InverseMethod(call, bestReturnType, bestViewType);
    }

    private static android.databinding.tool.reflection.ModelClass eraseType(android.databinding.tool.reflection.ModelClass type, java.util.List<android.databinding.tool.reflection.ModelClass> typeParameters) {
        java.util.List<android.databinding.tool.reflection.ModelClass> typeArguments = type.getTypeArguments();
        if ((typeArguments == null) || (typeParameters == null)) {
            return type;
        }
        for (android.databinding.tool.reflection.ModelClass arg : typeArguments) {
            if (typeParameters.contains(arg)) {
                return type.erasure();
            }
        }
        return type;
    }

    private static java.lang.String trimAttributeNamespace(java.lang.String attribute) {
        final int colonIndex = attribute.indexOf(':');
        return colonIndex == (-1) ? attribute : attribute.substring(colonIndex + 1);
    }

    private static java.lang.String getDefaultSetter(java.lang.String attribute) {
        return "set" + android.databinding.tool.util.StringUtils.capitalize(android.databinding.tool.store.SetterStore.trimAttributeNamespace(attribute));
    }

    private boolean isBetterParameter(android.databinding.tool.reflection.ModelClass argument, android.databinding.tool.reflection.ModelClass parameter, android.databinding.tool.reflection.ModelClass oldParameter, boolean isBetterViewTypeMatch, java.util.Map<java.lang.String, java.lang.String> imports) {
        // Right view type. Check the value
        if ((!isBetterViewTypeMatch) && oldParameter.equals(argument)) {
            return false;
        } else
            if (argument.equals(parameter)) {
                // Exact match
                return true;
            } else
                if ((!isBetterViewTypeMatch) && android.databinding.tool.reflection.ModelMethod.isBoxingConversion(oldParameter, argument)) {
                    return false;
                } else
                    if (android.databinding.tool.reflection.ModelMethod.isBoxingConversion(parameter, argument)) {
                        // Boxing/unboxing is second best
                        return true;
                    } else {
                        int oldConversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(oldParameter);
                        if (android.databinding.tool.reflection.ModelMethod.isImplicitConversion(argument, parameter)) {
                            // Better implicit conversion
                            int conversionLevel = android.databinding.tool.reflection.ModelMethod.getImplicitConversionLevel(parameter);
                            return (oldConversionLevel < 0) || (conversionLevel < oldConversionLevel);
                        } else
                            if (oldConversionLevel >= 0) {
                                return false;
                            } else
                                if (parameter.isAssignableFrom(argument)) {
                                    // Right type, see if it is better than the current best match.
                                    if (oldParameter == null) {
                                        return true;
                                    } else {
                                        return oldParameter.isAssignableFrom(parameter);
                                    }
                                } else {
                                    android.databinding.tool.store.SetterStore.MethodDescription conversionMethod = getConversionMethod(argument, parameter, imports);
                                    if (conversionMethod != null) {
                                        return true;
                                    }
                                    if (getConversionMethod(argument, oldParameter, imports) != null) {
                                        return false;
                                    }
                                    return argument.isObject() && (!parameter.isPrimitive());
                                }


                    }



    }

    private android.databinding.tool.store.SetterStore.MethodDescription getConversionMethod(android.databinding.tool.reflection.ModelClass from, android.databinding.tool.reflection.ModelClass to, java.util.Map<java.lang.String, java.lang.String> imports) {
        if ((from != null) && (to != null)) {
            if (to.isObject()) {
                return null;
            }
            for (java.lang.String fromClassName : mStore.conversionMethods.keySet()) {
                try {
                    android.databinding.tool.reflection.ModelClass convertFrom = mClassAnalyzer.findClass(fromClassName, imports);
                    if (canUseForConversion(from, convertFrom)) {
                        java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription> conversion = mStore.conversionMethods.get(fromClassName);
                        for (java.lang.String toClassName : conversion.keySet()) {
                            try {
                                android.databinding.tool.reflection.ModelClass convertTo = mClassAnalyzer.findClass(toClassName, imports);
                                if (canUseForConversion(convertTo, to)) {
                                    return conversion.get(toClassName);
                                }
                            } catch (java.lang.Exception e) {
                                android.databinding.tool.util.L.d(e, "Unknown class: %s", toClassName);
                            }
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.databinding.tool.util.L.d(e, "Unknown class: %s", fromClassName);
                }
            }
        }
        return null;
    }

    private boolean canUseForConversion(android.databinding.tool.reflection.ModelClass from, android.databinding.tool.reflection.ModelClass to) {
        if (from.isIncomplete() || to.isIncomplete()) {
            from = from.erasure();
            to = to.erasure();
        }
        return (from.equals(to) || android.databinding.tool.reflection.ModelMethod.isBoxingConversion(from, to)) || to.isAssignableFrom(from);
    }

    private static void merge(android.databinding.tool.store.SetterStore.IntermediateV2 store, android.databinding.tool.store.SetterStore.Intermediate dumpStore) {
        android.databinding.tool.store.SetterStore.IntermediateV2 intermediateV2 = ((android.databinding.tool.store.SetterStore.IntermediateV2) (dumpStore.upgrade()));
        android.databinding.tool.store.SetterStore.merge(store.adapterMethods, intermediateV2.adapterMethods);
        android.databinding.tool.store.SetterStore.merge(store.renamedMethods, intermediateV2.renamedMethods);
        android.databinding.tool.store.SetterStore.merge(store.conversionMethods, intermediateV2.conversionMethods);
        store.multiValueAdapters.putAll(intermediateV2.multiValueAdapters);
        store.untaggableTypes.putAll(intermediateV2.untaggableTypes);
        android.databinding.tool.store.SetterStore.merge(store.inverseAdapters, intermediateV2.inverseAdapters);
        android.databinding.tool.store.SetterStore.merge(store.inverseMethods, intermediateV2.inverseMethods);
    }

    private static <K, V, D> void merge(java.util.HashMap<K, java.util.HashMap<V, D>> first, java.util.HashMap<K, java.util.HashMap<V, D>> second) {
        for (K key : second.keySet()) {
            java.util.HashMap<V, D> firstVals = first.get(key);
            java.util.HashMap<V, D> secondVals = second.get(key);
            if (firstVals == null) {
                first.put(key, secondVals);
            } else {
                for (V key2 : secondVals.keySet()) {
                    if (!firstVals.containsKey(key2)) {
                        firstVals.put(key2, secondVals.get(key2));
                    }
                }
            }
        }
    }

    private static java.lang.String createAdapterCall(android.databinding.tool.store.SetterStore.MethodDescription adapter, java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String... args) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (adapter.isStatic) {
            sb.append(adapter.type);
        } else {
            final android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
            final java.lang.String binderCall = setterStore.getBindingAdapterCall(adapter.type);
            sb.append(componentExpression).append('.').append(binderCall);
        }
        sb.append('.').append(adapter.method).append('(');
        if (adapter.componentClass != null) {
            if (!"DataBindingComponent".equals(adapter.componentClass)) {
                sb.append('(').append(adapter.componentClass).append(") ");
            }
            sb.append(componentExpression).append(", ");
        }
        sb.append(viewExpression);
        for (java.lang.String arg : args) {
            sb.append(", ").append(arg);
        }
        sb.append(')');
        return sb.toString();
    }

    private static class MultiValueAdapterKey implements java.io.Serializable {
        private static final long serialVersionUID = 1;

        public final java.lang.String viewType;

        public final java.lang.String[] attributes;

        public final java.lang.String[] parameterTypes;

        public final boolean requireAll;

        public final java.util.TreeMap<java.lang.String, java.lang.Integer> attributeIndices = new java.util.TreeMap<java.lang.String, java.lang.Integer>();

        public MultiValueAdapterKey(javax.annotation.processing.ProcessingEnvironment processingEnv, javax.lang.model.element.ExecutableElement method, java.lang.String[] attributes, boolean takesComponent, boolean requireAll) {
            this.attributes = android.databinding.tool.store.SetterStore.stripAttributes(attributes);
            this.requireAll = requireAll;
            java.util.List<? extends javax.lang.model.element.VariableElement> parameters = method.getParameters();
            final int argStart = 1 + (takesComponent ? 1 : 0);
            this.viewType = android.databinding.tool.store.SetterStore.getQualifiedName(android.databinding.tool.store.SetterStore.eraseType(processingEnv, parameters.get(argStart - 1).asType()));
            this.parameterTypes = new java.lang.String[parameters.size() - argStart];
            for (int i = 0; i < attributes.length; i++) {
                javax.lang.model.type.TypeMirror typeMirror = android.databinding.tool.store.SetterStore.eraseType(processingEnv, parameters.get(i + argStart).asType());
                this.parameterTypes[i] = android.databinding.tool.store.SetterStore.getQualifiedName(typeMirror);
                attributeIndices.put(this.attributes[i], i);
            }
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof android.databinding.tool.store.SetterStore.MultiValueAdapterKey)) {
                return false;
            }
            final android.databinding.tool.store.SetterStore.MultiValueAdapterKey that = ((android.databinding.tool.store.SetterStore.MultiValueAdapterKey) (obj));
            if (((!this.viewType.equals(that.viewType)) || (this.attributes.length != that.attributes.length)) || (!this.attributeIndices.keySet().equals(that.attributeIndices.keySet()))) {
                return false;
            }
            for (int i = 0; i < this.attributes.length; i++) {
                final int thatIndex = that.attributeIndices.get(this.attributes[i]);
                final java.lang.String thisParameter = parameterTypes[i];
                final java.lang.String thatParameter = that.parameterTypes[thatIndex];
                if (!thisParameter.equals(thatParameter)) {
                    return false;
                }
            }
            return true;
        }

        @java.lang.Override
        public int hashCode() {
            return android.databinding.tool.store.SetterStore.mergedHashCode(viewType, attributeIndices.keySet());
        }
    }

    private static int mergedHashCode(java.lang.Object... objects) {
        return java.util.Arrays.hashCode(objects);
    }

    private static class MethodDescription implements java.io.Serializable {
        private static final long serialVersionUID = 1;

        public final java.lang.String type;

        public final java.lang.String method;

        public final boolean requiresOldValue;

        public final boolean isStatic;

        public final java.lang.String componentClass;

        public MethodDescription(java.lang.String type, java.lang.String method) {
            this.type = type;
            this.method = method;
            this.requiresOldValue = false;
            this.isStatic = true;
            this.componentClass = null;
            android.databinding.tool.util.L.d("BINARY created method desc 1 %s %s", type, method);
        }

        public MethodDescription(javax.lang.model.element.ExecutableElement method, int numAttributes, boolean takesComponent) {
            javax.lang.model.element.TypeElement enclosingClass = ((javax.lang.model.element.TypeElement) (method.getEnclosingElement()));
            this.type = enclosingClass.getQualifiedName().toString();
            this.method = method.getSimpleName().toString();
            final int argStart = 1 + (takesComponent ? 1 : 0);
            this.requiresOldValue = (method.getParameters().size() - argStart) == (numAttributes * 2);
            this.isStatic = method.getModifiers().contains(javax.lang.model.element.Modifier.STATIC);
            this.componentClass = (takesComponent) ? android.databinding.tool.store.SetterStore.getQualifiedName(method.getParameters().get(0).asType()) : null;
            android.databinding.tool.util.L.d("BINARY created method desc 2 %s %s, %s", type, this.method, method);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (obj instanceof android.databinding.tool.store.SetterStore.MethodDescription) {
                android.databinding.tool.store.SetterStore.MethodDescription that = ((android.databinding.tool.store.SetterStore.MethodDescription) (obj));
                return that.type.equals(this.type) && that.method.equals(this.method);
            } else {
                return false;
            }
        }

        @java.lang.Override
        public int hashCode() {
            return android.databinding.tool.store.SetterStore.mergedHashCode(type, method);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((type + ".") + method) + "()";
        }
    }

    private static class InverseDescription extends android.databinding.tool.store.SetterStore.MethodDescription {
        private static final long serialVersionUID = 1;

        public final java.lang.String event;

        public InverseDescription(java.lang.String type, java.lang.String method, java.lang.String event) {
            super(type, method);
            this.event = event;
        }

        public InverseDescription(javax.lang.model.element.ExecutableElement method, java.lang.String event, boolean takesComponent) {
            super(method, 1, takesComponent);
            this.event = event;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if ((!super.equals(obj)) || (!(obj instanceof android.databinding.tool.store.SetterStore.InverseDescription))) {
                return false;
            }
            return event.equals(((android.databinding.tool.store.SetterStore.InverseDescription) (obj)).event);
        }

        @java.lang.Override
        public int hashCode() {
            return android.databinding.tool.store.SetterStore.mergedHashCode(type, method, event);
        }
    }

    private static class AccessorKey implements java.io.Serializable {
        private static final long serialVersionUID = 1;

        public final java.lang.String viewType;

        public final java.lang.String valueType;

        public AccessorKey(java.lang.String viewType, java.lang.String valueType) {
            this.viewType = viewType;
            this.valueType = valueType;
        }

        @java.lang.Override
        public int hashCode() {
            return android.databinding.tool.store.SetterStore.mergedHashCode(viewType, valueType);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (obj instanceof android.databinding.tool.store.SetterStore.AccessorKey) {
                android.databinding.tool.store.SetterStore.AccessorKey that = ((android.databinding.tool.store.SetterStore.AccessorKey) (obj));
                return viewType.equals(that.valueType) && valueType.equals(that.valueType);
            } else {
                return false;
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("AK(" + viewType) + ", ") + valueType) + ")";
        }
    }

    private interface Intermediate extends java.io.Serializable {
        android.databinding.tool.store.SetterStore.Intermediate upgrade();
    }

    private static class IntermediateV1 implements android.databinding.tool.store.SetterStore.Intermediate , java.io.Serializable {
        private static final long serialVersionUID = 1;

        public final java.util.HashMap<java.lang.String, java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription>> adapterMethods = new java.util.HashMap<java.lang.String, java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.MethodDescription>>();

        public final java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription>> renamedMethods = new java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription>>();

        public final java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription>> conversionMethods = new java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.MethodDescription>>();

        public final java.util.HashMap<java.lang.String, java.lang.String> untaggableTypes = new java.util.HashMap<java.lang.String, java.lang.String>();

        public final java.util.HashMap<android.databinding.tool.store.SetterStore.MultiValueAdapterKey, android.databinding.tool.store.SetterStore.MethodDescription> multiValueAdapters = new java.util.HashMap<android.databinding.tool.store.SetterStore.MultiValueAdapterKey, android.databinding.tool.store.SetterStore.MethodDescription>();

        public IntermediateV1() {
        }

        @java.lang.Override
        public android.databinding.tool.store.SetterStore.Intermediate upgrade() {
            android.databinding.tool.store.SetterStore.IntermediateV2 v2 = new android.databinding.tool.store.SetterStore.IntermediateV2();
            v2.adapterMethods.putAll(adapterMethods);
            v2.renamedMethods.putAll(renamedMethods);
            v2.conversionMethods.putAll(conversionMethods);
            v2.untaggableTypes.putAll(untaggableTypes);
            v2.multiValueAdapters.putAll(multiValueAdapters);
            return v2;
        }
    }

    private static class IntermediateV2 extends android.databinding.tool.store.SetterStore.IntermediateV1 {
        public final java.util.HashMap<java.lang.String, java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription>> inverseAdapters = new java.util.HashMap<java.lang.String, java.util.HashMap<android.databinding.tool.store.SetterStore.AccessorKey, android.databinding.tool.store.SetterStore.InverseDescription>>();

        public final java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.InverseDescription>> inverseMethods = new java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.databinding.tool.store.SetterStore.InverseDescription>>();

        @java.lang.Override
        public android.databinding.tool.store.SetterStore.Intermediate upgrade() {
            return this;
        }
    }

    public static class DummySetter extends android.databinding.tool.store.SetterStore.SetterCall {
        private java.lang.String mMethodName;

        public DummySetter(java.lang.String methodName) {
            mMethodName = methodName;
        }

        @java.lang.Override
        public java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String valueExpression) {
            return ((((viewExpression + ".") + mMethodName) + "(") + valueExpression) + ")";
        }

        @java.lang.Override
        public java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String oldValue, java.lang.String valueExpression) {
            return ((((viewExpression + ".") + mMethodName) + "(") + valueExpression) + ")";
        }

        @java.lang.Override
        public int getMinApi() {
            return 1;
        }

        @java.lang.Override
        public boolean requiresOldValue() {
            return false;
        }

        @java.lang.Override
        public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
            return new android.databinding.tool.reflection.ModelClass[]{ android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass(java.lang.Object.class) };
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return null;
        }
    }

    public static class AdapterSetter extends android.databinding.tool.store.SetterStore.SetterCall {
        final android.databinding.tool.store.SetterStore.MethodDescription mAdapter;

        final android.databinding.tool.reflection.ModelClass mParameterType;

        public AdapterSetter(android.databinding.tool.store.SetterStore.MethodDescription adapter, android.databinding.tool.reflection.ModelClass parameterType) {
            mAdapter = adapter;
            mParameterType = parameterType;
        }

        @java.lang.Override
        public java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String valueExpression) {
            return android.databinding.tool.store.SetterStore.createAdapterCall(mAdapter, componentExpression, viewExpression, mCastString + valueExpression);
        }

        @java.lang.Override
        protected java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String oldValue, java.lang.String valueExpression) {
            return android.databinding.tool.store.SetterStore.createAdapterCall(mAdapter, componentExpression, viewExpression, mCastString + oldValue, mCastString + valueExpression);
        }

        @java.lang.Override
        public int getMinApi() {
            return 1;
        }

        @java.lang.Override
        public boolean requiresOldValue() {
            return mAdapter.requiresOldValue;
        }

        @java.lang.Override
        public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
            return new android.databinding.tool.reflection.ModelClass[]{ mParameterType };
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return mAdapter.isStatic ? null : mAdapter.type;
        }
    }

    public static class ModelMethodSetter extends android.databinding.tool.store.SetterStore.SetterCall {
        final android.databinding.tool.reflection.ModelMethod mModelMethod;

        public ModelMethodSetter(android.databinding.tool.reflection.ModelMethod modelMethod) {
            mModelMethod = modelMethod;
        }

        @java.lang.Override
        public java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String valueExpression) {
            return (((((viewExpression + ".") + mModelMethod.getName()) + "(") + mCastString) + valueExpression) + ")";
        }

        @java.lang.Override
        protected java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String oldValue, java.lang.String valueExpression) {
            return ((((((((viewExpression + ".") + mModelMethod.getName()) + "(") + mCastString) + oldValue) + ", ") + mCastString) + valueExpression) + ")";
        }

        @java.lang.Override
        public int getMinApi() {
            return mModelMethod.getMinApi();
        }

        @java.lang.Override
        public boolean requiresOldValue() {
            return mModelMethod.getParameterTypes().length == 3;
        }

        @java.lang.Override
        public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
            return new android.databinding.tool.reflection.ModelClass[]{ mModelMethod.getParameterTypes()[0] };
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return null;
        }
    }

    public interface BindingSetterCall {
        java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String... valueExpressions);

        int getMinApi();

        boolean requiresOldValue();

        android.databinding.tool.reflection.ModelClass[] getParameterTypes();

        java.lang.String getBindingAdapterInstanceClass();
    }

    public static abstract class SetterCall implements android.databinding.tool.store.SetterStore.BindingSetterCall {
        private android.databinding.tool.store.SetterStore.MethodDescription mConverter;

        protected java.lang.String mCastString = "";

        public SetterCall() {
        }

        public void setConverter(android.databinding.tool.store.SetterStore.MethodDescription converter) {
            mConverter = converter;
        }

        protected abstract java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String converted);

        protected abstract java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String oldValue, java.lang.String converted);

        @java.lang.Override
        public final java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String... valueExpression) {
            android.databinding.tool.util.Preconditions.check(valueExpression.length == 2, "value expressions size must be 2");
            if (requiresOldValue()) {
                return toJavaInternal(componentExpression, viewExpression, convertValue(valueExpression[0]), convertValue(valueExpression[1]));
            } else {
                return toJavaInternal(componentExpression, viewExpression, convertValue(valueExpression[1]));
            }
        }

        protected java.lang.String convertValue(java.lang.String valueExpression) {
            return mConverter == null ? valueExpression : ((((mConverter.type + ".") + mConverter.method) + "(") + valueExpression) + ")";
        }

        public abstract int getMinApi();

        public void setCast(android.databinding.tool.reflection.ModelClass castTo) {
            mCastString = ("(" + castTo.toJavaCode()) + ") ";
        }
    }

    public static class MultiAttributeSetter implements android.databinding.tool.store.SetterStore.BindingSetterCall {
        public final java.lang.String[] attributes;

        private final android.databinding.tool.store.SetterStore.MethodDescription mAdapter;

        private final android.databinding.tool.store.SetterStore.MethodDescription[] mConverters;

        private final java.lang.String[] mCasts;

        private final android.databinding.tool.store.SetterStore.MultiValueAdapterKey mKey;

        private final boolean[] mSupplied;

        public MultiAttributeSetter(android.databinding.tool.store.SetterStore.MultiValueAdapterKey key, boolean[] supplied, android.databinding.tool.store.SetterStore.MethodDescription adapter, android.databinding.tool.store.SetterStore.MethodDescription[] converters, java.lang.String[] casts) {
            android.databinding.tool.util.Preconditions.check(((((converters != null) && (converters.length == key.attributes.length)) && (casts != null)) && (casts.length == key.attributes.length)) && (supplied.length == key.attributes.length), "invalid arguments to create multi attr setter");
            this.mAdapter = adapter;
            this.mConverters = converters;
            this.mCasts = casts;
            this.mKey = key;
            this.mSupplied = supplied;
            if (key.requireAll) {
                this.attributes = key.attributes;
            } else {
                int numSupplied = 0;
                for (int i = 0; i < mKey.attributes.length; i++) {
                    if (supplied[i]) {
                        numSupplied++;
                    }
                }
                if (numSupplied == key.attributes.length) {
                    this.attributes = key.attributes;
                } else {
                    this.attributes = new java.lang.String[numSupplied];
                    int attrIndex = 0;
                    for (int i = 0; i < key.attributes.length; i++) {
                        if (supplied[i]) {
                            attributes[attrIndex++] = key.attributes[i];
                        }
                    }
                }
            }
        }

        @java.lang.Override
        public final java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String[] valueExpressions) {
            android.databinding.tool.util.Preconditions.check(valueExpressions.length == (attributes.length * 2), "MultiAttributeSetter needs %s items, received %s", java.util.Arrays.toString(attributes), java.util.Arrays.toString(valueExpressions));
            final int numAttrs = mKey.attributes.length;
            java.lang.String[] args = new java.lang.String[numAttrs + (requiresOldValue() ? numAttrs : 0)];
            final int startIndex = (mAdapter.requiresOldValue) ? 0 : numAttrs;
            int attrIndex = (mAdapter.requiresOldValue) ? 0 : attributes.length;
            final android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
            java.lang.StringBuilder argBuilder = new java.lang.StringBuilder();
            final int endIndex = numAttrs * 2;
            for (int i = startIndex; i < endIndex; i++) {
                argBuilder.setLength(0);
                if (!mSupplied[i % numAttrs]) {
                    final java.lang.String paramType = mKey.parameterTypes[i % numAttrs];
                    final java.lang.String defaultValue = modelAnalyzer.getDefaultValue(paramType);
                    argBuilder.append('(').append(paramType).append(')').append(defaultValue);
                } else {
                    if (mConverters[i % numAttrs] != null) {
                        final android.databinding.tool.store.SetterStore.MethodDescription converter = mConverters[i % numAttrs];
                        argBuilder.append(converter.type).append('.').append(converter.method).append('(').append(valueExpressions[attrIndex]).append(')');
                    } else {
                        if (mCasts[i % numAttrs] != null) {
                            argBuilder.append('(').append(mCasts[i % numAttrs]).append(')');
                        }
                        argBuilder.append(valueExpressions[attrIndex]);
                    }
                    attrIndex++;
                }
                args[i - startIndex] = argBuilder.toString();
            }
            return android.databinding.tool.store.SetterStore.createAdapterCall(mAdapter, componentExpression, viewExpression, args);
        }

        @java.lang.Override
        public int getMinApi() {
            return 1;
        }

        @java.lang.Override
        public boolean requiresOldValue() {
            return mAdapter.requiresOldValue;
        }

        @java.lang.Override
        public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
            android.databinding.tool.reflection.ModelClass[] parameters = new android.databinding.tool.reflection.ModelClass[attributes.length];
            java.lang.String[] paramTypeStrings = mKey.parameterTypes;
            android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
            int attrIndex = 0;
            for (int i = 0; i < mKey.attributes.length; i++) {
                if (mSupplied[i]) {
                    parameters[attrIndex++] = modelAnalyzer.findClass(paramTypeStrings[i], null);
                }
            }
            return parameters;
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return mAdapter.isStatic ? null : mAdapter.type;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((("MultiAttributeSetter{" + "attributes=") + java.util.Arrays.toString(attributes)) + ", mAdapter=") + mAdapter) + ", mConverters=") + java.util.Arrays.toString(mConverters)) + ", mCasts=") + java.util.Arrays.toString(mCasts)) + ", mKey=") + mKey) + '}';
        }
    }

    public static class ViewDataBindingEventSetter implements android.databinding.tool.store.SetterStore.BindingSetterCall {
        public ViewDataBindingEventSetter() {
        }

        @java.lang.Override
        public java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String... valueExpressions) {
            return ((((("setBindingInverseListener(" + viewExpression) + ", ") + valueExpressions[0]) + ", ") + valueExpressions[1]) + ")";
        }

        @java.lang.Override
        public int getMinApi() {
            return 0;
        }

        @java.lang.Override
        public boolean requiresOldValue() {
            return true;
        }

        @java.lang.Override
        public android.databinding.tool.reflection.ModelClass[] getParameterTypes() {
            android.databinding.tool.reflection.ModelClass[] parameterTypes = new android.databinding.tool.reflection.ModelClass[1];
            parameterTypes[0] = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass("android.databinding.ViewDataBinder.PropertyChangedInverseListener", null);
            return parameterTypes;
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return null;
        }
    }

    public interface BindingGetterCall {
        java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression);

        int getMinApi();

        java.lang.String getBindingAdapterInstanceClass();

        void setBindingAdapterCall(java.lang.String method);

        android.databinding.tool.store.SetterStore.BindingSetterCall getEvent();

        java.lang.String getEventAttribute();
    }

    public static class ViewDataBindingGetterCall implements android.databinding.tool.store.SetterStore.BindingGetterCall {
        private final java.lang.String mGetter;

        private final android.databinding.tool.store.SetterStore.BindingSetterCall mEventSetter;

        private final java.lang.String mAttribute;

        public ViewDataBindingGetterCall(java.lang.String attribute) {
            final int colonIndex = attribute.indexOf(':');
            mAttribute = attribute.substring(colonIndex + 1);
            mGetter = "get" + android.databinding.tool.util.StringUtils.capitalize(mAttribute);
            mEventSetter = new android.databinding.tool.store.SetterStore.ViewDataBindingEventSetter();
        }

        @java.lang.Override
        public java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression) {
            return ((viewExpression + ".") + mGetter) + "()";
        }

        @java.lang.Override
        public int getMinApi() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return null;
        }

        @java.lang.Override
        public void setBindingAdapterCall(java.lang.String method) {
        }

        @java.lang.Override
        public android.databinding.tool.store.SetterStore.BindingSetterCall getEvent() {
            return mEventSetter;
        }

        @java.lang.Override
        public java.lang.String getEventAttribute() {
            return mAttribute;
        }
    }

    public static class ViewGetterCall implements android.databinding.tool.store.SetterStore.BindingGetterCall {
        private final android.databinding.tool.store.SetterStore.InverseDescription mInverseDescription;

        private final android.databinding.tool.store.SetterStore.BindingSetterCall mEventCall;

        private final android.databinding.tool.reflection.ModelMethod mMethod;

        public ViewGetterCall(android.databinding.tool.store.SetterStore.InverseDescription inverseDescription, android.databinding.tool.reflection.ModelMethod method, android.databinding.tool.store.SetterStore.BindingSetterCall eventCall) {
            mInverseDescription = inverseDescription;
            mEventCall = eventCall;
            mMethod = method;
        }

        @java.lang.Override
        public android.databinding.tool.store.SetterStore.BindingSetterCall getEvent() {
            return mEventCall;
        }

        @java.lang.Override
        public java.lang.String getEventAttribute() {
            return mInverseDescription.event;
        }

        @java.lang.Override
        public java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression) {
            return ((viewExpression + ".") + mMethod.getName()) + "()";
        }

        @java.lang.Override
        public int getMinApi() {
            return mMethod.getMinApi();
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return null;
        }

        @java.lang.Override
        public void setBindingAdapterCall(java.lang.String method) {
        }
    }

    public static class AdapterGetter implements android.databinding.tool.store.SetterStore.BindingGetterCall {
        private final android.databinding.tool.store.SetterStore.InverseDescription mInverseDescription;

        private java.lang.String mBindingAdapterCall;

        private final android.databinding.tool.store.SetterStore.BindingSetterCall mEventCall;

        public AdapterGetter(android.databinding.tool.store.SetterStore.InverseDescription description, android.databinding.tool.store.SetterStore.BindingSetterCall eventCall) {
            mInverseDescription = description;
            mEventCall = eventCall;
        }

        @java.lang.Override
        public java.lang.String toJava(java.lang.String componentExpression, java.lang.String viewExpression) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            if (mInverseDescription.isStatic) {
                sb.append(mInverseDescription.type);
            } else {
                sb.append(componentExpression).append('.').append(mBindingAdapterCall);
            }
            sb.append('.').append(mInverseDescription.method).append('(');
            if (mInverseDescription.componentClass != null) {
                if (!"DataBindingComponent".equals(mInverseDescription.componentClass)) {
                    sb.append('(').append(mInverseDescription.componentClass).append(") ");
                }
                sb.append(componentExpression).append(", ");
            }
            sb.append(viewExpression).append(')');
            return sb.toString();
        }

        @java.lang.Override
        public int getMinApi() {
            return 1;
        }

        @java.lang.Override
        public java.lang.String getBindingAdapterInstanceClass() {
            return mInverseDescription.isStatic ? null : mInverseDescription.type;
        }

        @java.lang.Override
        public void setBindingAdapterCall(java.lang.String method) {
            mBindingAdapterCall = method;
        }

        @java.lang.Override
        public android.databinding.tool.store.SetterStore.BindingSetterCall getEvent() {
            return mEventCall;
        }

        @java.lang.Override
        public java.lang.String getEventAttribute() {
            return mInverseDescription.event;
        }
    }

    private static class InverseMethod {
        public android.databinding.tool.store.SetterStore.BindingGetterCall call;

        public android.databinding.tool.reflection.ModelClass returnType;

        public android.databinding.tool.reflection.ModelClass viewType;

        public InverseMethod(android.databinding.tool.store.SetterStore.BindingGetterCall call, android.databinding.tool.reflection.ModelClass returnType, android.databinding.tool.reflection.ModelClass viewType) {
            this.call = call;
            this.returnType = returnType;
            this.viewType = viewType;
        }
    }
}

