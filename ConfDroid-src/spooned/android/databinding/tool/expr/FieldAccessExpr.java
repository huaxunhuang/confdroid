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
package android.databinding.tool.expr;


public class FieldAccessExpr extends android.databinding.tool.expr.Expr {
    java.lang.String mName;

    // notification name for the field. Important when we map this to a method w/ different name
    java.lang.String mBrName;

    android.databinding.tool.reflection.Callable mGetter;

    final boolean mIsObservableField;

    boolean mIsListener;

    boolean mIsViewAttributeAccess;

    FieldAccessExpr(android.databinding.tool.expr.Expr parent, java.lang.String name) {
        super(parent);
        mName = name;
        mIsObservableField = false;
    }

    FieldAccessExpr(android.databinding.tool.expr.Expr parent, java.lang.String name, boolean isObservableField) {
        super(parent);
        mName = name;
        mIsObservableField = isObservableField;
    }

    public android.databinding.tool.expr.Expr getChild() {
        return getChildren().get(0);
    }

    public android.databinding.tool.reflection.Callable getGetter() {
        if (mGetter == null) {
            getResolvedType();
        }
        return mGetter;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        if (getGetter().setterName == null) {
            return ((("Two-way binding cannot resolve a setter for " + getResolvedType().toJavaCode()) + " property '") + mName) + "'";
        }
        if (!mGetter.isDynamic()) {
            return (("Cannot change a final field in " + getResolvedType().toJavaCode()) + " property ") + mName;
        }
        return null;
    }

    public int getMinApi() {
        return mGetter.getMinApi();
    }

    @java.lang.Override
    public boolean isDynamic() {
        if (mGetter == null) {
            getResolvedType();
        }
        if ((mGetter == null) || (mGetter.type == android.databinding.tool.reflection.Callable.Type.METHOD)) {
            return true;
        }
        // if it is static final, gone
        if (getChild().isDynamic()) {
            // if owner is dynamic, then we can be dynamic unless we are static final
            return (!mGetter.isStatic()) || mGetter.isDynamic();
        }
        if (mIsViewAttributeAccess) {
            return true;// must be able to invalidate this

        }
        // if owner is NOT dynamic, we can be dynamic if an only if getter is dynamic
        return mGetter.isDynamic();
    }

    public boolean hasBindableAnnotations() {
        return mGetter.canBeInvalidated();
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr resolveListeners(android.databinding.tool.reflection.ModelClass listener, android.databinding.tool.expr.Expr parent) {
        if ((mName == null) || mName.isEmpty()) {
            return this;// ObservableFields aren't listeners

        }
        final android.databinding.tool.reflection.ModelClass childType = getChild().getResolvedType();
        if (getGetter() == null) {
            if ((listener == null) || (!mIsListener)) {
                android.databinding.tool.util.L.e("Could not resolve %s.%s as an accessor or listener on the attribute.", childType.getCanonicalName(), mName);
                return this;
            }
            getChild().getParents().remove(this);
        } else
            if (listener == null) {
                return this;// Not a listener, but we have a getter.

            }

        java.util.List<android.databinding.tool.reflection.ModelMethod> abstractMethods = listener.getAbstractMethods();
        int numberOfAbstractMethods = (abstractMethods == null) ? 0 : abstractMethods.size();
        if (numberOfAbstractMethods != 1) {
            if (mGetter == null) {
                android.databinding.tool.util.L.e("Could not find accessor %s.%s and %s has %d abstract methods, so is" + " not resolved as a listener", childType.getCanonicalName(), mName, listener.getCanonicalName(), numberOfAbstractMethods);
            }
            return this;
        }
        // Look for a signature matching the abstract method
        final android.databinding.tool.reflection.ModelMethod listenerMethod = abstractMethods.get(0);
        final android.databinding.tool.reflection.ModelClass[] listenerParameters = listenerMethod.getParameterTypes();
        boolean isStatic = getChild() instanceof android.databinding.tool.expr.StaticIdentifierExpr;
        java.util.List<android.databinding.tool.reflection.ModelMethod> methods = childType.findMethods(mName, isStatic);
        if (methods == null) {
            return this;
        }
        for (android.databinding.tool.reflection.ModelMethod method : methods) {
            if (acceptsParameters(method, listenerParameters) && method.getReturnType(null).equals(listenerMethod.getReturnType(null))) {
                resetResolvedType();
                // replace this with ListenerExpr in parent
                android.databinding.tool.expr.Expr listenerExpr = getModel().listenerExpr(getChild(), mName, listener, listenerMethod);
                if (parent != null) {
                    int index;
                    while ((index = parent.getChildren().indexOf(this)) != (-1)) {
                        parent.getChildren().set(index, listenerExpr);
                    } 
                }
                if (getModel().mBindingExpressions.contains(this)) {
                    getModel().bindingExpr(listenerExpr);
                }
                getParents().remove(parent);
                if (getParents().isEmpty()) {
                    getModel().removeExpr(this);
                }
                return listenerExpr;
            }
        }
        if (mGetter == null) {
            android.databinding.tool.util.L.e("Listener class %s with method %s did not match signature of any method %s.%s", listener.getCanonicalName(), listenerMethod.getName(), childType.getCanonicalName(), mName);
        }
        return this;
    }

    private boolean acceptsParameters(android.databinding.tool.reflection.ModelMethod method, android.databinding.tool.reflection.ModelClass[] listenerParameters) {
        android.databinding.tool.reflection.ModelClass[] parameters = method.getParameterTypes();
        if (parameters.length != listenerParameters.length) {
            return false;
        }
        for (int i = 0; i < parameters.length; i++) {
            if (!parameters[i].isAssignableFrom(listenerParameters[i])) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        final java.util.List<android.databinding.tool.expr.Dependency> dependencies = constructDynamicChildrenDependencies();
        for (android.databinding.tool.expr.Dependency dependency : dependencies) {
            if (dependency.getOther() == getChild()) {
                dependency.setMandatory(true);
            }
        }
        return dependencies;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        if (mIsObservableField) {
            return addTwoWay(android.databinding.tool.expr.Expr.join(mName, "..", super.computeUniqueKey()));
        }
        return addTwoWay(android.databinding.tool.expr.Expr.join(mName, ".", super.computeUniqueKey()));
    }

    public java.lang.String getName() {
        return mName;
    }

    public java.lang.String getBrName() {
        if (mIsListener) {
            return null;
        }
        try {
            android.databinding.tool.processing.Scope.enter(this);
            android.databinding.tool.util.Preconditions.checkNotNull(mGetter, "cannot get br name before resolving the getter");
            return mBrName;
        } finally {
            android.databinding.tool.processing.Scope.exit();
        }
    }

    @java.lang.Override
    public void updateExpr(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        try {
            android.databinding.tool.processing.Scope.enter(this);
            resolveType(modelAnalyzer);
            super.updateExpr(modelAnalyzer);
        } finally {
            android.databinding.tool.processing.Scope.exit();
        }
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        if (mIsListener) {
            return modelAnalyzer.findClass(java.lang.Object.class);
        }
        if (mGetter == null) {
            android.databinding.tool.expr.Expr child = getChild();
            child.getResolvedType();
            boolean isStatic = child instanceof android.databinding.tool.expr.StaticIdentifierExpr;
            android.databinding.tool.reflection.ModelClass resolvedType = child.getResolvedType();
            android.databinding.tool.util.L.d("resolving %s. Resolved class type: %s", this, resolvedType);
            mGetter = resolvedType.findGetterOrField(mName, isStatic);
            if (mGetter == null) {
                mIsListener = resolvedType.findMethods(mName, isStatic) != null;
                if (!mIsListener) {
                    android.databinding.tool.util.L.e("Could not find accessor %s.%s", resolvedType.getCanonicalName(), mName);
                }
                return modelAnalyzer.findClass(java.lang.Object.class);
            }
            if (mGetter.isStatic() && (!isStatic)) {
                // found a static method on an instance. register a new one
                child.getParents().remove(this);
                getChildren().remove(child);
                android.databinding.tool.expr.StaticIdentifierExpr staticId = getModel().staticIdentifierFor(resolvedType);
                getChildren().add(staticId);
                staticId.getParents().add(this);
                child = getChild();// replace the child for the next if stmt

            }
            if (mGetter.resolvedType.isObservableField()) {
                // Make this the ".get()" and add an extra field access for the observable field
                child.getParents().remove(this);
                getChildren().remove(child);
                android.databinding.tool.expr.FieldAccessExpr observableField = getModel().observableField(child, mName);
                observableField.mGetter = mGetter;
                getChildren().add(observableField);
                observableField.getParents().add(this);
                mGetter = mGetter.resolvedType.findGetterOrField("", false);
                mName = "";
                mBrName = android.databinding.tool.ext.ExtKt.br(mName);
            } else
                if (hasBindableAnnotations()) {
                    mBrName = android.databinding.tool.ext.ExtKt.br(android.databinding.tool.util.BrNameUtil.brKey(mGetter));
                }

        }
        return mGetter.resolvedType;
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr resolveTwoWayExpressions(android.databinding.tool.expr.Expr parent) {
        final android.databinding.tool.expr.Expr child = getChild();
        if (!(child instanceof android.databinding.tool.expr.ViewFieldExpr)) {
            return this;
        }
        final android.databinding.tool.expr.ViewFieldExpr expr = ((android.databinding.tool.expr.ViewFieldExpr) (child));
        final android.databinding.tool.BindingTarget bindingTarget = expr.getBindingTarget();
        // This is a binding to a View's attribute, so look for matching attribute
        // on that View's BindingTarget. If there is an expression, we simply replace
        // the binding with that binding expression.
        for (android.databinding.tool.Binding binding : bindingTarget.getBindings()) {
            if (android.databinding.tool.expr.FieldAccessExpr.attributeMatchesName(binding.getName(), mName)) {
                final android.databinding.tool.expr.Expr replacement = binding.getExpr();
                replaceExpression(parent, replacement);
                return replacement;
            }
        }
        // There was no binding expression to bind to. This should be a two-way binding.
        // This is a synthesized two-way binding because we must capture the events from
        // the View and change the value when the target View's attribute changes.
        final android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
        final android.databinding.tool.reflection.ModelClass targetClass = expr.getResolvedType();
        android.databinding.tool.store.SetterStore.BindingGetterCall getter = setterStore.getGetterCall(mName, targetClass, null, null);
        if (getter == null) {
            getter = setterStore.getGetterCall("android:" + mName, targetClass, null, null);
            if (getter == null) {
                android.databinding.tool.util.L.e("Could not resolve the two-way binding attribute '%s' on type '%s'", mName, targetClass);
            }
        }
        android.databinding.tool.InverseBinding inverseBinding = null;
        for (android.databinding.tool.Binding binding : bindingTarget.getBindings()) {
            final android.databinding.tool.expr.Expr testExpr = binding.getExpr();
            if ((testExpr instanceof android.databinding.tool.expr.TwoWayListenerExpr) && getter.getEventAttribute().equals(binding.getName())) {
                inverseBinding = ((android.databinding.tool.expr.TwoWayListenerExpr) (testExpr)).mInverseBinding;
                break;
            }
        }
        if (inverseBinding == null) {
            inverseBinding = bindingTarget.addInverseBinding(mName, getter);
        }
        inverseBinding.addChainedExpression(this);
        mIsViewAttributeAccess = true;
        enableDirectInvalidation();
        return this;
    }

    private static boolean attributeMatchesName(java.lang.String attribute, java.lang.String field) {
        int colonIndex = attribute.indexOf(':');
        return attribute.substring(colonIndex + 1).equals(field);
    }

    private void replaceExpression(android.databinding.tool.expr.Expr parent, android.databinding.tool.expr.Expr replacement) {
        if (parent != null) {
            java.util.List<android.databinding.tool.expr.Expr> children = parent.getChildren();
            int index;
            while ((index = children.indexOf(this)) >= 0) {
                children.set(index, replacement);
                replacement.getParents().add(parent);
            } 
            while (getParents().remove(parent)) {
                // just remove all copies of parent.
            } 
        }
        if (getParents().isEmpty()) {
            getModel().removeExpr(this);
        }
    }

    @java.lang.Override
    protected java.lang.String asPackage() {
        java.lang.String parentPackage = getChild().asPackage();
        return parentPackage == null ? null : (parentPackage + ".") + mName;
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        android.databinding.tool.writer.KCode code = new android.databinding.tool.writer.KCode();
        if (expand) {
            java.lang.String defaultValue = android.databinding.tool.reflection.ModelAnalyzer.getInstance().getDefaultValue(getResolvedType().toJavaCode());
            code.app("(", getChild().toCode(true)).app(" == null) ? ").app(defaultValue).app(" : ");
        }
        code.app("", getChild().toCode(expand)).app(".");
        if (getGetter().type == android.databinding.tool.reflection.Callable.Type.FIELD) {
            return code.app(getGetter().name);
        } else {
            return code.app(getGetter().name).app("()");
        }
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        if (mGetter.setterName == null) {
            throw new java.lang.IllegalStateException("There is no inverse for " + toCode().generate());
        }
        android.databinding.tool.writer.KCode castValue = new android.databinding.tool.writer.KCode("(").app(getResolvedType().toJavaCode() + ")(", value).app(")");
        java.lang.String type = getChild().getResolvedType().toJavaCode();
        android.databinding.tool.writer.KCode code = new android.databinding.tool.writer.KCode("targetObj_.");
        if (getGetter().type == android.databinding.tool.reflection.Callable.Type.FIELD) {
            code.app(getGetter().setterName).app(" = ", castValue).app(";");
        } else {
            code.app(getGetter().setterName).app("(", castValue).app(")").app(";");
        }
        return new android.databinding.tool.writer.KCode().app("final ").app(type).app(" targetObj_ = ", getChild().toCode(true)).app(";").nl(new android.databinding.tool.writer.KCode("if (targetObj_ != null) {")).tab(code).nl(new android.databinding.tool.writer.KCode("}"));
    }
}

