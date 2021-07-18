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
package android.databinding.tool.reflection;


/**
 * This is the base class for several implementations of something that
 * acts like a ClassLoader. Different implementations work with the Annotation
 * Processor, ClassLoader, and an Android Studio plugin.
 */
public abstract class ModelAnalyzer {
    public static final java.lang.String[] LIST_CLASS_NAMES = new java.lang.String[]{ "java.util.List", "android.util.SparseArray", "android.util.SparseBooleanArray", "android.util.SparseIntArray", "android.util.SparseLongArray", "android.util.LongSparseArray", "android.support.v4.util.LongSparseArray" };

    public static final java.lang.String MAP_CLASS_NAME = "java.util.Map";

    public static final java.lang.String STRING_CLASS_NAME = "java.lang.String";

    public static final java.lang.String OBJECT_CLASS_NAME = "java.lang.Object";

    public static final java.lang.String OBSERVABLE_CLASS_NAME = "android.databinding.Observable";

    public static final java.lang.String OBSERVABLE_LIST_CLASS_NAME = "android.databinding.ObservableList";

    public static final java.lang.String OBSERVABLE_MAP_CLASS_NAME = "android.databinding.ObservableMap";

    public static final java.lang.String[] OBSERVABLE_FIELDS = new java.lang.String[]{ "android.databinding.ObservableBoolean", "android.databinding.ObservableByte", "android.databinding.ObservableChar", "android.databinding.ObservableShort", "android.databinding.ObservableInt", "android.databinding.ObservableLong", "android.databinding.ObservableFloat", "android.databinding.ObservableDouble", "android.databinding.ObservableField", "android.databinding.ObservableParcelable" };

    public static final java.lang.String VIEW_DATA_BINDING = "android.databinding.ViewDataBinding";

    public static final java.lang.String VIEW_STUB_CLASS_NAME = "android.view.ViewStub";

    private android.databinding.tool.reflection.ModelClass[] mListTypes;

    private android.databinding.tool.reflection.ModelClass mMapType;

    private android.databinding.tool.reflection.ModelClass mStringType;

    private android.databinding.tool.reflection.ModelClass mObjectType;

    private android.databinding.tool.reflection.ModelClass mObservableType;

    private android.databinding.tool.reflection.ModelClass mObservableListType;

    private android.databinding.tool.reflection.ModelClass mObservableMapType;

    private android.databinding.tool.reflection.ModelClass[] mObservableFieldTypes;

    private android.databinding.tool.reflection.ModelClass mViewBindingType;

    private android.databinding.tool.reflection.ModelClass mViewStubType;

    private static android.databinding.tool.reflection.ModelAnalyzer sAnalyzer;

    protected void setInstance(android.databinding.tool.reflection.ModelAnalyzer analyzer) {
        android.databinding.tool.reflection.ModelAnalyzer.sAnalyzer = analyzer;
    }

    public android.databinding.tool.reflection.ModelClass findCommonParentOf(android.databinding.tool.reflection.ModelClass modelClass1, android.databinding.tool.reflection.ModelClass modelClass2) {
        android.databinding.tool.reflection.ModelClass curr = modelClass1;
        while ((curr != null) && (!curr.isAssignableFrom(modelClass2))) {
            curr = curr.getSuperclass();
        } 
        if (curr == null) {
            if (modelClass1.isObject() && modelClass2.isInterface()) {
                return modelClass1;
            } else
                if (modelClass2.isObject() && modelClass1.isInterface()) {
                    return modelClass2;
                }

            android.databinding.tool.reflection.ModelClass primitive1 = modelClass1.unbox();
            android.databinding.tool.reflection.ModelClass primitive2 = modelClass2.unbox();
            if ((!modelClass1.equals(primitive1)) || (!modelClass2.equals(primitive2))) {
                return findCommonParentOf(primitive1, primitive2);
            }
        }
        android.databinding.tool.util.Preconditions.checkNotNull(curr, (("must be able to find a common parent for " + modelClass1) + " and ") + modelClass2);
        return curr;
    }

    public abstract android.databinding.tool.reflection.ModelClass loadPrimitive(java.lang.String className);

    public static android.databinding.tool.reflection.ModelAnalyzer getInstance() {
        return android.databinding.tool.reflection.ModelAnalyzer.sAnalyzer;
    }

    public static void setProcessingEnvironment(javax.annotation.processing.ProcessingEnvironment processingEnvironment) {
        if (android.databinding.tool.reflection.ModelAnalyzer.sAnalyzer != null) {
            throw new java.lang.IllegalStateException("processing env is already created, you cannot " + "change class loader after that");
        }
        android.databinding.tool.util.L.d("setting processing env to %s", processingEnvironment);
        android.databinding.tool.reflection.annotation.AnnotationAnalyzer annotationAnalyzer = new android.databinding.tool.reflection.annotation.AnnotationAnalyzer(processingEnvironment);
        android.databinding.tool.reflection.ModelAnalyzer.sAnalyzer = annotationAnalyzer;
    }

    /**
     * Takes a raw className (potentially w/ generics and arrays) and expands definitions using
     * the import statements.
     * <p>
     * For instance, this allows user to define variables
     * <variable type="User" name="user"/>
     * if they previously imported User.
     * <import name="com.example.User"/>
     */
    public java.lang.String applyImports(java.lang.String className, java.util.Map<java.lang.String, java.lang.String> imports) {
        className = className.trim();
        int numDimensions = 0;
        java.lang.String generic = null;
        // handle array
        while (className.endsWith("[]")) {
            numDimensions++;
            className = className.substring(0, className.length() - 2);
        } 
        // handle generics
        final int lastCharIndex = className.length() - 1;
        if ('>' == className.charAt(lastCharIndex)) {
            // has generic.
            int open = className.indexOf('<');
            if (open == (-1)) {
                android.databinding.tool.util.L.e("un-matching generic syntax for %s", className);
                return className;
            }
            generic = applyImports(className.substring(open + 1, lastCharIndex), imports);
            className = className.substring(0, open);
        }
        int dotIndex = className.indexOf('.');
        final java.lang.String qualifier;
        final java.lang.String rest;
        if (dotIndex == (-1)) {
            qualifier = className;
            rest = null;
        } else {
            qualifier = className.substring(0, dotIndex);
            rest = className.substring(dotIndex);// includes dot

        }
        final java.lang.String expandedQualifier = imports.get(qualifier);
        java.lang.String result;
        if (expandedQualifier != null) {
            result = (rest == null) ? expandedQualifier : expandedQualifier + rest;
        } else {
            result = className;// no change

        }
        // now append back dimension and generics
        if (generic != null) {
            result = ((result + "<") + applyImports(generic, imports)) + ">";
        }
        while ((numDimensions--) > 0) {
            result = result + "[]";
        } 
        return result;
    }

    public java.lang.String getDefaultValue(java.lang.String className) {
        if ("int".equals(className)) {
            return "0";
        }
        if ("short".equals(className)) {
            return "0";
        }
        if ("long".equals(className)) {
            return "0L";
        }
        if ("float".equals(className)) {
            return "0f";
        }
        if ("double".equals(className)) {
            return "0.0";
        }
        if ("boolean".equals(className)) {
            return "false";
        }
        if ("char".equals(className)) {
            return "\'\\u0000\'";
        }
        if ("byte".equals(className)) {
            return "0";
        }
        return "null";
    }

    public abstract android.databinding.tool.reflection.ModelClass findClass(java.lang.String className, java.util.Map<java.lang.String, java.lang.String> imports);

    public abstract android.databinding.tool.reflection.ModelClass findClass(java.lang.Class classType);

    public abstract android.databinding.tool.reflection.TypeUtil createTypeUtil();

    android.databinding.tool.reflection.ModelClass[] getListTypes() {
        if (mListTypes == null) {
            mListTypes = new android.databinding.tool.reflection.ModelClass[android.databinding.tool.reflection.ModelAnalyzer.LIST_CLASS_NAMES.length];
            for (int i = 0; i < mListTypes.length; i++) {
                final android.databinding.tool.reflection.ModelClass modelClass = findClass(android.databinding.tool.reflection.ModelAnalyzer.LIST_CLASS_NAMES[i], null);
                if (modelClass != null) {
                    mListTypes[i] = modelClass.erasure();
                }
            }
        }
        return mListTypes;
    }

    public android.databinding.tool.reflection.ModelClass getMapType() {
        if (mMapType == null) {
            mMapType = loadClassErasure(android.databinding.tool.reflection.ModelAnalyzer.MAP_CLASS_NAME);
        }
        return mMapType;
    }

    android.databinding.tool.reflection.ModelClass getStringType() {
        if (mStringType == null) {
            mStringType = findClass(android.databinding.tool.reflection.ModelAnalyzer.STRING_CLASS_NAME, null);
        }
        return mStringType;
    }

    android.databinding.tool.reflection.ModelClass getObjectType() {
        if (mObjectType == null) {
            mObjectType = findClass(android.databinding.tool.reflection.ModelAnalyzer.OBJECT_CLASS_NAME, null);
        }
        return mObjectType;
    }

    android.databinding.tool.reflection.ModelClass getObservableType() {
        if (mObservableType == null) {
            mObservableType = findClass(android.databinding.tool.reflection.ModelAnalyzer.OBSERVABLE_CLASS_NAME, null);
        }
        return mObservableType;
    }

    android.databinding.tool.reflection.ModelClass getObservableListType() {
        if (mObservableListType == null) {
            mObservableListType = loadClassErasure(android.databinding.tool.reflection.ModelAnalyzer.OBSERVABLE_LIST_CLASS_NAME);
        }
        return mObservableListType;
    }

    android.databinding.tool.reflection.ModelClass getObservableMapType() {
        if (mObservableMapType == null) {
            mObservableMapType = loadClassErasure(android.databinding.tool.reflection.ModelAnalyzer.OBSERVABLE_MAP_CLASS_NAME);
        }
        return mObservableMapType;
    }

    android.databinding.tool.reflection.ModelClass getViewDataBindingType() {
        if (mViewBindingType == null) {
            mViewBindingType = findClass(android.databinding.tool.reflection.ModelAnalyzer.VIEW_DATA_BINDING, null);
        }
        return mViewBindingType;
    }

    protected android.databinding.tool.reflection.ModelClass[] getObservableFieldTypes() {
        if (mObservableFieldTypes == null) {
            mObservableFieldTypes = new android.databinding.tool.reflection.ModelClass[android.databinding.tool.reflection.ModelAnalyzer.OBSERVABLE_FIELDS.length];
            for (int i = 0; i < android.databinding.tool.reflection.ModelAnalyzer.OBSERVABLE_FIELDS.length; i++) {
                mObservableFieldTypes[i] = loadClassErasure(android.databinding.tool.reflection.ModelAnalyzer.OBSERVABLE_FIELDS[i]);
            }
        }
        return mObservableFieldTypes;
    }

    android.databinding.tool.reflection.ModelClass getViewStubType() {
        if (mViewStubType == null) {
            mViewStubType = findClass(android.databinding.tool.reflection.ModelAnalyzer.VIEW_STUB_CLASS_NAME, null);
        }
        return mViewStubType;
    }

    private android.databinding.tool.reflection.ModelClass loadClassErasure(java.lang.String className) {
        return findClass(className, null).erasure();
    }
}

