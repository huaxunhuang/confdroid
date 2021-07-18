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


public class InverseBinding implements android.databinding.tool.processing.scopes.LocationScopeProvider {
    private final java.lang.String mName;

    private final android.databinding.tool.expr.Expr mExpr;

    private final android.databinding.tool.BindingTarget mTarget;

    private android.databinding.tool.store.SetterStore.BindingGetterCall mGetterCall;

    private final java.util.ArrayList<android.databinding.tool.expr.FieldAccessExpr> mChainedExpressions = new java.util.ArrayList<android.databinding.tool.expr.FieldAccessExpr>();

    public InverseBinding(android.databinding.tool.BindingTarget target, java.lang.String name, android.databinding.tool.expr.Expr expr) {
        mTarget = target;
        mName = name;
        mExpr = expr;
    }

    @java.lang.Override
    public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
        if (mExpr != null) {
            return mExpr.getLocations();
        } else {
            return mChainedExpressions.get(0).getLocations();
        }
    }

    void setGetterCall(android.databinding.tool.store.SetterStore.BindingGetterCall getterCall) {
        mGetterCall = getterCall;
    }

    public void addChainedExpression(android.databinding.tool.expr.FieldAccessExpr expr) {
        mChainedExpressions.add(expr);
    }

    public boolean isOnBinder() {
        return mTarget.getResolvedType().isViewDataBinding();
    }

    private android.databinding.tool.store.SetterStore.BindingGetterCall getGetterCall() {
        if (mGetterCall == null) {
            if (mExpr != null) {
                mExpr.getResolvedType();// force resolve of ObservableFields

            }
            try {
                android.databinding.tool.processing.Scope.enter(mTarget);
                android.databinding.tool.processing.Scope.enter(this);
                resolveGetterCall();
                if (mGetterCall == null) {
                    android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.CANNOT_FIND_GETTER_CALL, mName, mExpr == null ? "Unknown" : mExpr.getResolvedType(), mTarget.getResolvedType());
                }
            } finally {
                android.databinding.tool.processing.Scope.exit();
                android.databinding.tool.processing.Scope.exit();
            }
        }
        return mGetterCall;
    }

    private void resolveGetterCall() {
        android.databinding.tool.reflection.ModelClass viewType = mTarget.getResolvedType();
        final android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
        final android.databinding.tool.reflection.ModelClass resolvedType = (mExpr == null) ? null : mExpr.getResolvedType();
        mGetterCall = setterStore.getGetterCall(mName, viewType, resolvedType, getModel().getImports());
    }

    public android.databinding.tool.BindingTarget getTarget() {
        return mTarget;
    }

    public android.databinding.tool.writer.KCode toJavaCode(java.lang.String bindingComponent, final android.databinding.tool.writer.FlagSet flagField) {
        final java.lang.String targetViewName = android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(getTarget());
        android.databinding.tool.writer.KCode code = new android.databinding.tool.writer.KCode();
        // A chained expression will have substituted its chained value for the expression
        // unless the attribute has no expression. Therefore, chaining and expressions are
        // mutually exclusive.
        android.databinding.tool.util.Preconditions.check((mExpr == null) != mChainedExpressions.isEmpty(), "Chained expressions are only against unbound attributes.");
        if (mExpr != null) {
            code.app("", mExpr.toInverseCode(new android.databinding.tool.writer.KCode(getGetterCall().toJava(bindingComponent, targetViewName))));
        } else {
            // !mChainedExpressions.isEmpty())
            final java.lang.String fieldName = flagField.getLocalName();
            android.databinding.tool.writer.FlagSet flagSet = new android.databinding.tool.writer.FlagSet();
            for (android.databinding.tool.expr.FieldAccessExpr expr : mChainedExpressions) {
                flagSet = flagSet.or(new android.databinding.tool.writer.FlagSet(expr.getId()));
            }
            final android.databinding.tool.writer.FlagSet allFlags = flagSet;
            code.nl(new android.databinding.tool.writer.KCode("synchronized(this) {"));
            code.tab(android.databinding.tool.writer.LayoutBinderWriterKt.mapOr(flagField, flagSet, new kotlin.jvm.functions.Function2<java.lang.String, java.lang.Integer, android.databinding.tool.writer.KCode>() {
                @java.lang.Override
                public android.databinding.tool.writer.KCode invoke(java.lang.String suffix, java.lang.Integer index) {
                    return new android.databinding.tool.writer.KCode(fieldName).app(suffix).app(" |= ").app(android.databinding.tool.writer.LayoutBinderWriterKt.binaryCode(allFlags, index)).app(";");
                }
            }));
            code.nl(new android.databinding.tool.writer.KCode("}"));
            code.nl(new android.databinding.tool.writer.KCode("requestRebind()"));
        }
        return code;
    }

    public java.lang.String getBindingAdapterInstanceClass() {
        return getGetterCall().getBindingAdapterInstanceClass();
    }

    /**
     * The min api level in which this binding should be executed.
     * <p>
     * This should be the minimum value among the dependencies of this binding.
     */
    public int getMinApi() {
        final android.databinding.tool.store.SetterStore.BindingGetterCall getterCall = getGetterCall();
        return java.lang.Math.max(getterCall.getMinApi(), getterCall.getEvent().getMinApi());
    }

    public android.databinding.tool.store.SetterStore.BindingSetterCall getEventSetter() {
        final android.databinding.tool.store.SetterStore.BindingGetterCall getterCall = getGetterCall();
        return getterCall.getEvent();
    }

    public java.lang.String getName() {
        return mName;
    }

    public java.lang.String getEventAttribute() {
        return getGetterCall().getEventAttribute();
    }

    public android.databinding.tool.expr.ExprModel getModel() {
        if (mExpr != null) {
            return mExpr.getModel();
        }
        return mChainedExpressions.get(0).getModel();
    }
}

