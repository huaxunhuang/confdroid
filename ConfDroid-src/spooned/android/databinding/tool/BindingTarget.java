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


public class BindingTarget implements android.databinding.tool.processing.scopes.LocationScopeProvider {
    java.util.List<android.databinding.tool.Binding> mBindings = new java.util.ArrayList<android.databinding.tool.Binding>();

    java.util.List<android.databinding.tool.InverseBinding> mInverseBindings = new java.util.ArrayList<android.databinding.tool.InverseBinding>();

    android.databinding.tool.expr.ExprModel mModel;

    android.databinding.tool.reflection.ModelClass mResolvedClass;

    // if this target presents itself in multiple layout files with different view types,
    // it receives an interface type and should use it in the getter instead.
    android.databinding.tool.store.ResourceBundle.BindingTargetBundle mBundle;

    public BindingTarget(android.databinding.tool.store.ResourceBundle.BindingTargetBundle bundle) {
        mBundle = bundle;
    }

    public boolean isUsed() {
        return mBundle.isUsed();
    }

    public void addBinding(java.lang.String name, android.databinding.tool.expr.Expr expr) {
        if (android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance()).isTwoWayEventAttribute(name)) {
            android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.TWO_WAY_EVENT_ATTRIBUTE, name);
        }
        mBindings.add(new android.databinding.tool.Binding(this, name, expr));
        if (expr.isTwoWay()) {
            try {
                android.databinding.tool.processing.Scope.enter(expr);
                expr.assertIsInvertible();
                final android.databinding.tool.InverseBinding inverseBinding = new android.databinding.tool.InverseBinding(this, name, expr);
                mInverseBindings.add(inverseBinding);
                mBindings.add(new android.databinding.tool.Binding(this, inverseBinding.getEventAttribute(), mModel.twoWayListenerExpr(inverseBinding), inverseBinding.getEventSetter()));
            } finally {
                android.databinding.tool.processing.Scope.exit();
            }
        }
    }

    public java.lang.String getInterfaceType() {
        return mBundle.getInterfaceType() == null ? mBundle.getFullClassName() : mBundle.getInterfaceType();
    }

    public android.databinding.tool.InverseBinding addInverseBinding(java.lang.String name, android.databinding.tool.store.SetterStore.BindingGetterCall call) {
        final android.databinding.tool.InverseBinding inverseBinding = new android.databinding.tool.InverseBinding(this, name, null);
        inverseBinding.setGetterCall(call);
        mInverseBindings.add(inverseBinding);
        mBindings.add(new android.databinding.tool.Binding(this, inverseBinding.getEventAttribute(), mModel.twoWayListenerExpr(inverseBinding)));
        return inverseBinding;
    }

    @java.lang.Override
    public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
        return mBundle.provideScopeLocation();
    }

    public java.lang.String getId() {
        return mBundle.getId();
    }

    public java.lang.String getTag() {
        return mBundle.getTag();
    }

    public java.lang.String getOriginalTag() {
        return mBundle.getOriginalTag();
    }

    public java.lang.String getViewClass() {
        return mBundle.getFullClassName();
    }

    public android.databinding.tool.reflection.ModelClass getResolvedType() {
        if (mResolvedClass == null) {
            if (mBundle.isBinder()) {
                mResolvedClass = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass(android.databinding.tool.reflection.ModelAnalyzer.VIEW_DATA_BINDING, mModel.getImports());
            } else {
                mResolvedClass = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass(mBundle.getFullClassName(), mModel.getImports());
            }
        }
        return mResolvedClass;
    }

    public java.lang.String getIncludedLayout() {
        return mBundle.getIncludedLayout();
    }

    public boolean isBinder() {
        return getIncludedLayout() != null;
    }

    public boolean supportsTag() {
        return !android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance()).isUntaggable(mBundle.getFullClassName());
    }

    public java.util.List<android.databinding.tool.Binding> getBindings() {
        return mBindings;
    }

    public java.util.List<android.databinding.tool.InverseBinding> getInverseBindings() {
        return mInverseBindings;
    }

    public android.databinding.tool.expr.ExprModel getModel() {
        return mModel;
    }

    public void setModel(android.databinding.tool.expr.ExprModel model) {
        mModel = model;
    }

    public void resolveListeners() {
        for (android.databinding.tool.Binding binding : mBindings) {
            binding.resolveListeners();
        }
    }

    public void resolveTwoWayExpressions() {
        for (android.databinding.tool.Binding binding : mBindings) {
            binding.resolveTwoWayExpressions();
        }
    }

    /**
     * Called after BindingTarget is finalized.
     * <p>
     * We traverse all bindings and ask SetterStore to figure out if any can be combined.
     * When N bindings are combined, they are demoted from being a binding expression and a new
     * ArgList expression is added as the new binding expression that depends on others.
     */
    public void resolveMultiSetters() {
        android.databinding.tool.util.L.d("resolving multi setters for %s", getId());
        final android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
        final java.lang.String[] attributes = new java.lang.String[mBindings.size()];
        final android.databinding.tool.reflection.ModelClass[] types = new android.databinding.tool.reflection.ModelClass[mBindings.size()];
        for (int i = 0; i < mBindings.size(); i++) {
            android.databinding.tool.Binding binding = mBindings.get(i);
            try {
                android.databinding.tool.processing.Scope.enter(binding);
                attributes[i] = binding.getName();
                types[i] = binding.getExpr().getResolvedType();
            } finally {
                android.databinding.tool.processing.Scope.exit();
            }
        }
        final java.util.List<android.databinding.tool.store.SetterStore.MultiAttributeSetter> multiAttributeSetterCalls = setterStore.getMultiAttributeSetterCalls(attributes, getResolvedType(), types);
        if (multiAttributeSetterCalls.isEmpty()) {
            return;
        }
        final java.util.Map<java.lang.String, android.databinding.tool.Binding> lookup = new java.util.HashMap<java.lang.String, android.databinding.tool.Binding>();
        for (android.databinding.tool.Binding binding : mBindings) {
            java.lang.String name = binding.getName();
            if (name.startsWith("android:")) {
                lookup.put(name, binding);
            } else {
                int ind = name.indexOf(":");
                if (ind == (-1)) {
                    lookup.put(name, binding);
                } else {
                    lookup.put(name.substring(ind + 1), binding);
                }
            }
        }
        java.util.List<android.databinding.tool.MergedBinding> mergeBindings = new java.util.ArrayList<android.databinding.tool.MergedBinding>();
        for (final android.databinding.tool.store.SetterStore.MultiAttributeSetter setter : multiAttributeSetterCalls) {
            android.databinding.tool.util.L.d("resolved %s", setter);
            final java.util.List<android.databinding.tool.Binding> mergedBindings = new java.util.ArrayList<android.databinding.tool.Binding>();
            for (java.lang.String attribute : setter.attributes) {
                android.databinding.tool.Binding binding = lookup.get(attribute);
                android.databinding.tool.util.Preconditions.checkNotNull(binding, "cannot find binding for %s", attribute);
                mergedBindings.add(binding);
            }
            for (android.databinding.tool.Binding binding : mergedBindings) {
                binding.getExpr().setBindingExpression(false);
                mBindings.remove(binding);
            }
            android.databinding.tool.MergedBinding mergedBinding = new android.databinding.tool.MergedBinding(getModel(), setter, this, mergedBindings);
            mergeBindings.add(mergedBinding);
        }
        for (android.databinding.tool.MergedBinding binding : mergeBindings) {
            mBindings.add(binding);
        }
    }
}

