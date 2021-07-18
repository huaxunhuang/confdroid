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
package android.databinding.tool;


/**
 * Keeps all information about the bindings per layout file
 */
public class LayoutBinder implements android.databinding.tool.processing.scopes.FileScopeProvider {
    private static final java.util.Comparator<android.databinding.tool.BindingTarget> COMPARE_FIELD_NAME = new java.util.Comparator<android.databinding.tool.BindingTarget>() {
        @java.lang.Override
        public int compare(android.databinding.tool.BindingTarget first, android.databinding.tool.BindingTarget second) {
            final java.lang.String fieldName1 = android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(first);
            final java.lang.String fieldName2 = android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(second);
            return fieldName1.compareTo(fieldName2);
        }
    };

    /* val pkg: String, val projectPackage: String, val baseClassName: String,
    val layoutName:String, val lb: LayoutExprBinding
     */
    private final android.databinding.tool.expr.ExprModel mExprModel;

    private final android.databinding.tool.ExpressionParser mExpressionParser;

    private final java.util.List<android.databinding.tool.BindingTarget> mBindingTargets;

    private final java.util.List<android.databinding.tool.BindingTarget> mSortedBindingTargets;

    private java.lang.String mModulePackage;

    private final java.util.HashMap<java.lang.String, java.lang.String> mUserDefinedVariables = new java.util.HashMap<java.lang.String, java.lang.String>();

    private android.databinding.tool.writer.LayoutBinderWriter mWriter;

    private android.databinding.tool.store.ResourceBundle.LayoutFileBundle mBundle;

    private static final java.lang.String[] sJavaLangClasses = new java.lang.String[]{ "Deprecated", "Override", "SafeVarargs", "SuppressWarnings", "Appendable", "AutoCloseable", "CharSequence", "Cloneable", "Comparable", "Iterable", "Readable", "Runnable", "Thread.UncaughtExceptionHandler", "Boolean", "Byte", "Character", "Character.Subset", "Character.UnicodeBlock", "Class", "ClassLoader", "Compiler", "Double", "Enum", "Float", "InheritableThreadLocal", "Integer", "Long", "Math", "Number", "Object", "Package", "Process", "ProcessBuilder", "Runtime", "RuntimePermission", "SecurityManager", "Short", "StackTraceElement", "StrictMath", "String", "StringBuffer", "StringBuilder", "System", "Thread", "ThreadGroup", "ThreadLocal", "Throwable", "Void", "Thread.State", "ArithmeticException", "ArrayIndexOutOfBoundsException", "ArrayStoreException", "ClassCastException", "ClassNotFoundException", "CloneNotSupportedException", "EnumConstantNotPresentException", "Exception", "IllegalAccessException", "IllegalArgumentException", "IllegalMonitorStateException", "IllegalStateException", "IllegalThreadStateException", "IndexOutOfBoundsException", "InstantiationException", "InterruptedException", "NegativeArraySizeException", "NoSuchFieldException", "NoSuchMethodException", "NullPointerException", "NumberFormatException", "ReflectiveOperationException", "RuntimeException", "SecurityException", "StringIndexOutOfBoundsException", "TypeNotPresentException", "UnsupportedOperationException", "AbstractMethodError", "AssertionError", "ClassCircularityError", "ClassFormatError", "Error", "ExceptionInInitializerError", "IllegalAccessError", "IncompatibleClassChangeError", "InstantiationError", "InternalError", "LinkageError", "NoClassDefFoundError", "NoSuchFieldError", "NoSuchMethodError", "OutOfMemoryError", "StackOverflowError", "ThreadDeath", "UnknownError", "UnsatisfiedLinkError", "UnsupportedClassVersionError", "VerifyError", "VirtualMachineError" };

    public LayoutBinder(android.databinding.tool.store.ResourceBundle.LayoutFileBundle layoutBundle) {
        try {
            android.databinding.tool.processing.Scope.enter(this);
            mExprModel = new android.databinding.tool.expr.ExprModel();
            mExpressionParser = new android.databinding.tool.ExpressionParser(mExprModel);
            mBindingTargets = new java.util.ArrayList<android.databinding.tool.BindingTarget>();
            mBundle = layoutBundle;
            mModulePackage = layoutBundle.getModulePackage();
            java.util.HashSet<java.lang.String> names = new java.util.HashSet<java.lang.String>();
            // copy over data.
            for (android.databinding.tool.store.ResourceBundle.VariableDeclaration variable : mBundle.getVariables()) {
                addVariable(variable.name, variable.type, variable.location, variable.declared);
                names.add(variable.name);
            }
            for (android.databinding.tool.store.ResourceBundle.NameTypeLocation userImport : mBundle.getImports()) {
                mExprModel.addImport(userImport.name, userImport.type, userImport.location);
                names.add(userImport.name);
            }
            if (!names.contains("context")) {
                mExprModel.builtInVariable("context", "android.content.Context", "getRoot().getContext()");
                names.add("context");
            }
            for (java.lang.String javaLangClass : android.databinding.tool.LayoutBinder.sJavaLangClasses) {
                mExprModel.addImport(javaLangClass, "java.lang." + javaLangClass, null);
            }
            // First resolve all the View fields
            // Ensure there are no conflicts with variable names
            for (android.databinding.tool.store.ResourceBundle.BindingTargetBundle targetBundle : mBundle.getBindingTargetBundles()) {
                try {
                    android.databinding.tool.processing.Scope.enter(targetBundle);
                    final android.databinding.tool.BindingTarget bindingTarget = createBindingTarget(targetBundle);
                    if (bindingTarget.getId() != null) {
                        final java.lang.String fieldName = android.databinding.tool.writer.LayoutBinderWriterKt.getReadableName(bindingTarget);
                        if (names.contains(fieldName)) {
                            android.databinding.tool.util.L.w("View field %s collides with a variable or import", fieldName);
                        } else {
                            names.add(fieldName);
                            mExprModel.viewFieldExpr(bindingTarget);
                        }
                    }
                } finally {
                    android.databinding.tool.processing.Scope.exit();
                }
            }
            for (android.databinding.tool.BindingTarget bindingTarget : mBindingTargets) {
                try {
                    android.databinding.tool.processing.Scope.enter(bindingTarget.mBundle);
                    for (android.databinding.tool.store.ResourceBundle.BindingTargetBundle.BindingBundle bindingBundle : bindingTarget.mBundle.getBindingBundleList()) {
                        try {
                            android.databinding.tool.processing.Scope.enter(bindingBundle.getValueLocation());
                            bindingTarget.addBinding(bindingBundle.getName(), parse(bindingBundle.getExpr(), bindingBundle.isTwoWay(), bindingBundle.getValueLocation()));
                        } finally {
                            android.databinding.tool.processing.Scope.exit();
                        }
                    }
                    bindingTarget.resolveTwoWayExpressions();
                    bindingTarget.resolveMultiSetters();
                    bindingTarget.resolveListeners();
                } finally {
                    android.databinding.tool.processing.Scope.exit();
                }
            }
            mSortedBindingTargets = new java.util.ArrayList<android.databinding.tool.BindingTarget>(mBindingTargets);
            java.util.Collections.sort(mSortedBindingTargets, android.databinding.tool.LayoutBinder.COMPARE_FIELD_NAME);
        } finally {
            android.databinding.tool.processing.Scope.exit();
        }
    }

    public void resolveWhichExpressionsAreUsed() {
        java.util.List<android.databinding.tool.expr.Expr> used = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.BindingTarget target : mBindingTargets) {
            for (android.databinding.tool.Binding binding : target.getBindings()) {
                binding.getExpr().setIsUsed(true);
                used.add(binding.getExpr());
            }
        }
        while (!used.isEmpty()) {
            android.databinding.tool.expr.Expr e = used.remove(used.size() - 1);
            for (android.databinding.tool.expr.Dependency dep : e.getDependencies()) {
                if (!dep.getOther().isUsed()) {
                    used.add(dep.getOther());
                    dep.getOther().setIsUsed(true);
                }
            }
        } 
    }

    public android.databinding.tool.expr.IdentifierExpr addVariable(java.lang.String name, java.lang.String type, android.databinding.tool.store.Location location, boolean declared) {
        android.databinding.tool.util.Preconditions.check(!mUserDefinedVariables.containsKey(name), "%s has already been defined as %s", name, type);
        final android.databinding.tool.expr.IdentifierExpr id = mExprModel.identifier(name);
        id.setUserDefinedType(type);
        id.enableDirectInvalidation();
        if (location != null) {
            id.addLocation(location);
        }
        mUserDefinedVariables.put(name, type);
        if (declared) {
            id.setDeclared();
        }
        return id;
    }

    public java.util.HashMap<java.lang.String, java.lang.String> getUserDefinedVariables() {
        return mUserDefinedVariables;
    }

    public android.databinding.tool.BindingTarget createBindingTarget(android.databinding.tool.store.ResourceBundle.BindingTargetBundle targetBundle) {
        final android.databinding.tool.BindingTarget target = new android.databinding.tool.BindingTarget(targetBundle);
        mBindingTargets.add(target);
        target.setModel(mExprModel);
        return target;
    }

    public android.databinding.tool.expr.Expr parse(java.lang.String input, boolean isTwoWay, @org.antlr.v4.runtime.misc.Nullable
    android.databinding.tool.store.Location locationInFile) {
        final android.databinding.tool.expr.Expr parsed = mExpressionParser.parse(input, locationInFile);
        parsed.setBindingExpression(true);
        parsed.setTwoWay(isTwoWay);
        return parsed;
    }

    public java.util.List<android.databinding.tool.BindingTarget> getBindingTargets() {
        return mBindingTargets;
    }

    public java.util.List<android.databinding.tool.BindingTarget> getSortedTargets() {
        return mSortedBindingTargets;
    }

    public boolean isEmpty() {
        return mExprModel.size() == 0;
    }

    public android.databinding.tool.expr.ExprModel getModel() {
        return mExprModel;
    }

    private void ensureWriter() {
        if (mWriter == null) {
            mWriter = new android.databinding.tool.writer.LayoutBinderWriter(this);
        }
    }

    public void sealModel() {
        mExprModel.seal();
    }

    public java.lang.String writeViewBinderBaseClass(boolean forLibrary) {
        ensureWriter();
        return mWriter.writeBaseClass(forLibrary);
    }

    public java.lang.String writeViewBinder(int minSdk) {
        ensureWriter();
        android.databinding.tool.util.Preconditions.checkNotNull(getPackage(), "package cannot be null");
        android.databinding.tool.util.Preconditions.checkNotNull(getClassName(), "base class name cannot be null");
        return mWriter.write(minSdk);
    }

    public java.lang.String getPackage() {
        return mBundle.getBindingClassPackage();
    }

    public boolean isMerge() {
        return mBundle.isMerge();
    }

    public java.lang.String getModulePackage() {
        return mModulePackage;
    }

    public java.lang.String getLayoutname() {
        return mBundle.getFileName();
    }

    public java.lang.String getImplementationName() {
        if (hasVariations()) {
            return (mBundle.getBindingClassName() + mBundle.getConfigName()) + "Impl";
        } else {
            return mBundle.getBindingClassName();
        }
    }

    public java.lang.String getClassName() {
        return mBundle.getBindingClassName();
    }

    public java.lang.String getTag() {
        return (mBundle.getDirectory() + "/") + mBundle.getFileName();
    }

    public boolean hasVariations() {
        return mBundle.hasVariations();
    }

    @java.lang.Override
    public java.lang.String provideScopeFilePath() {
        return mBundle.getAbsoluteFilePath();
    }
}

