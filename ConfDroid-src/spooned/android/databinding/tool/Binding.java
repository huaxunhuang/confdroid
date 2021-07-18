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


public class Binding implements android.databinding.tool.processing.scopes.LocationScopeProvider {
    private final java.lang.String mName;

    private android.databinding.tool.expr.Expr mExpr;

    private final android.databinding.tool.BindingTarget mTarget;

    private android.databinding.tool.store.SetterStore.BindingSetterCall mSetterCall;

    public Binding(android.databinding.tool.BindingTarget target, java.lang.String name, android.databinding.tool.expr.Expr expr) {
        this(target, name, expr, null);
    }

    public Binding(android.databinding.tool.BindingTarget target, java.lang.String name, android.databinding.tool.expr.Expr expr, android.databinding.tool.store.SetterStore.BindingSetterCall setterCall) {
        mTarget = target;
        mName = name;
        mExpr = expr;
        mSetterCall = setterCall;
    }

    @java.lang.Override
    public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
        return mExpr.getLocations();
    }

    public void resolveListeners() {
        final android.databinding.tool.reflection.ModelClass listenerParameter = android.databinding.tool.Binding.getListenerParameter(mTarget, mName, mExpr);
        android.databinding.tool.expr.Expr listenerExpr = mExpr.resolveListeners(listenerParameter, null);
        if (listenerExpr != mExpr) {
            listenerExpr.setBindingExpression(true);
            mExpr = listenerExpr;
        }
    }

    public void resolveTwoWayExpressions() {
        android.databinding.tool.expr.Expr expr = mExpr.resolveTwoWayExpressions(null);
        if (expr != mExpr) {
            mExpr = expr;
        }
    }

    private android.databinding.tool.store.SetterStore.BindingSetterCall getSetterCall() {
        if (mSetterCall == null) {
            try {
                android.databinding.tool.processing.Scope.enter(getTarget());
                android.databinding.tool.processing.Scope.enter(this);
                resolveSetterCall();
                if (mSetterCall == null) {
                    android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.CANNOT_FIND_SETTER_CALL, mName, mExpr.getResolvedType());
                }
            } finally {
                android.databinding.tool.processing.Scope.exit();
                android.databinding.tool.processing.Scope.exit();
            }
        }
        return mSetterCall;
    }

    private void resolveSetterCall() {
        android.databinding.tool.reflection.ModelClass viewType = mTarget.getResolvedType();
        if ((viewType != null) && viewType.extendsViewStub()) {
            if (android.databinding.tool.Binding.isListenerAttribute(mName)) {
                android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
                android.databinding.tool.reflection.ModelClass viewStubProxy = modelAnalyzer.findClass("android.databinding.ViewStubProxy", null);
                mSetterCall = android.databinding.tool.store.SetterStore.get(modelAnalyzer).getSetterCall(mName, viewStubProxy, mExpr.getResolvedType(), mExpr.getModel().getImports());
            } else
                if (android.databinding.tool.Binding.isViewStubAttribute(mName)) {
                    mSetterCall = new android.databinding.tool.Binding.ViewStubDirectCall(mName, viewType, mExpr);
                } else {
                    mSetterCall = new android.databinding.tool.Binding.ViewStubSetterCall(mName);
                }

        } else {
            final android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
            mSetterCall = setterStore.getSetterCall(mName, viewType, mExpr.getResolvedType(), mExpr.getModel().getImports());
        }
    }

    /**
     * Similar to getSetterCall, but assumes an Object parameter to find the best matching listener.
     */
    private static android.databinding.tool.reflection.ModelClass getListenerParameter(android.databinding.tool.BindingTarget target, java.lang.String name, android.databinding.tool.expr.Expr expr) {
        android.databinding.tool.reflection.ModelClass viewType = target.getResolvedType();
        android.databinding.tool.store.SetterStore.SetterCall setterCall;
        android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
        android.databinding.tool.reflection.ModelClass objectParameter = modelAnalyzer.findClass(java.lang.Object.class);
        android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(modelAnalyzer);
        if ((viewType != null) && viewType.extendsViewStub()) {
            if (android.databinding.tool.Binding.isListenerAttribute(name)) {
                android.databinding.tool.reflection.ModelClass viewStubProxy = modelAnalyzer.findClass("android.databinding.ViewStubProxy", null);
                setterCall = android.databinding.tool.store.SetterStore.get(modelAnalyzer).getSetterCall(name, viewStubProxy, objectParameter, expr.getModel().getImports());
            } else
                if (android.databinding.tool.Binding.isViewStubAttribute(name)) {
                    setterCall = new android.databinding.tool.Binding.ViewStubDirectCall(name, viewType, expr);
                } else {
                    setterCall = new android.databinding.tool.Binding.ViewStubSetterCall(name);
                }

        } else {
            setterCall = setterStore.getSetterCall(name, viewType, objectParameter, expr.getModel().getImports());
        }
        if (setterCall != null) {
            return setterCall.getParameterTypes()[0];
        }
        java.util.List<android.databinding.tool.store.SetterStore.MultiAttributeSetter> setters = setterStore.getMultiAttributeSetterCalls(new java.lang.String[]{ name }, viewType, new android.databinding.tool.reflection.ModelClass[]{ modelAnalyzer.findClass(java.lang.Object.class) });
        if (setters.isEmpty()) {
            return null;
        } else {
            return setters.get(0).getParameterTypes()[0];
        }
    }

    public android.databinding.tool.BindingTarget getTarget() {
        return mTarget;
    }

    public java.lang.String toJavaCode(java.lang.String targetViewName, java.lang.String bindingComponent) {
        final java.lang.String currentValue = (requiresOldValue()) ? "this." + android.databinding.tool.writer.LayoutBinderWriterKt.getOldValueName(mExpr) : null;
        final java.lang.String argCode = getExpr().toCode().generate();
        return getSetterCall().toJava(bindingComponent, targetViewName, currentValue, argCode);
    }

    public java.lang.String getBindingAdapterInstanceClass() {
        return getSetterCall().getBindingAdapterInstanceClass();
    }

    public android.databinding.tool.expr.Expr[] getComponentExpressions() {
        return new android.databinding.tool.expr.Expr[]{ mExpr };
    }

    public boolean requiresOldValue() {
        return getSetterCall().requiresOldValue();
    }

    /**
     * The min api level in which this binding should be executed.
     * <p>
     * This should be the minimum value among the dependencies of this binding. For now, we only
     * check the setter.
     */
    public int getMinApi() {
        return getSetterCall().getMinApi();
    }

    public java.lang.String getName() {
        return mName;
    }

    public android.databinding.tool.expr.Expr getExpr() {
        return mExpr;
    }

    private static boolean isViewStubAttribute(java.lang.String name) {
        return (("android:inflatedId".equals(name) || "android:layout".equals(name)) || "android:visibility".equals(name)) || "android:layoutInflater".equals(name);
    }

    private static boolean isListenerAttribute(java.lang.String name) {
        return "android:onInflate".equals(name) || "android:onInflateListener".equals(name);
    }

    private static class ViewStubSetterCall extends android.databinding.tool.store.SetterStore.SetterCall {
        private final java.lang.String mName;

        public ViewStubSetterCall(java.lang.String name) {
            mName = name.substring(name.lastIndexOf(':') + 1);
        }

        @java.lang.Override
        protected java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String converted) {
            return ((((((("if (" + viewExpression) + ".isInflated()) ") + viewExpression) + ".getBinding().setVariable(BR.") + mName) + ", ") + converted) + ")";
        }

        @java.lang.Override
        protected java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String oldValue, java.lang.String converted) {
            return null;
        }

        @java.lang.Override
        public int getMinApi() {
            return 0;
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

    private static class ViewStubDirectCall extends android.databinding.tool.store.SetterStore.SetterCall {
        private final android.databinding.tool.store.SetterStore.SetterCall mWrappedCall;

        public ViewStubDirectCall(java.lang.String name, android.databinding.tool.reflection.ModelClass viewType, android.databinding.tool.expr.Expr expr) {
            mWrappedCall = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance()).getSetterCall(name, viewType, expr.getResolvedType(), expr.getModel().getImports());
            if (mWrappedCall == null) {
                android.databinding.tool.util.L.e("Cannot find the setter for attribute '%s' on %s with parameter type %s.", name, viewType, expr.getResolvedType());
            }
        }

        @java.lang.Override
        protected java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String converted) {
            return (("if (!" + viewExpression) + ".isInflated()) ") + mWrappedCall.toJava(componentExpression, viewExpression + ".getViewStub()", null, converted);
        }

        @java.lang.Override
        protected java.lang.String toJavaInternal(java.lang.String componentExpression, java.lang.String viewExpression, java.lang.String oldValue, java.lang.String converted) {
            return null;
        }

        @java.lang.Override
        public int getMinApi() {
            return 0;
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
            return mWrappedCall.getBindingAdapterInstanceClass();
        }
    }
}

