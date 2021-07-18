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
 * Multiple binding expressions can be evaluated using a single adapter. In those cases,
 * we replace the Binding with a MergedBinding.
 */
public class MergedBinding extends android.databinding.tool.Binding {
    private final android.databinding.tool.store.SetterStore.MultiAttributeSetter mMultiAttributeSetter;

    public MergedBinding(android.databinding.tool.expr.ExprModel model, android.databinding.tool.store.SetterStore.MultiAttributeSetter multiAttributeSetter, android.databinding.tool.BindingTarget target, java.lang.Iterable<android.databinding.tool.Binding> bindings) {
        super(target, android.databinding.tool.MergedBinding.createMergedName(bindings), android.databinding.tool.MergedBinding.createArgListExpr(model, bindings));
        mMultiAttributeSetter = multiAttributeSetter;
    }

    @java.lang.Override
    public void resolveListeners() {
        android.databinding.tool.reflection.ModelClass[] parameters = mMultiAttributeSetter.getParameterTypes();
        java.util.List<android.databinding.tool.expr.Expr> children = getExpr().getChildren();
        final android.databinding.tool.expr.Expr expr = getExpr();
        for (int i = 0; i < children.size(); i++) {
            final android.databinding.tool.expr.Expr child = children.get(i);
            child.resolveListeners(parameters[i], expr);
        }
    }

    private static android.databinding.tool.expr.Expr createArgListExpr(android.databinding.tool.expr.ExprModel model, final java.lang.Iterable<android.databinding.tool.Binding> bindings) {
        java.util.List<android.databinding.tool.expr.Expr> args = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.Binding binding : bindings) {
            args.add(binding.getExpr());
        }
        android.databinding.tool.expr.Expr expr = model.argListExpr(args);
        expr.setBindingExpression(true);
        return expr;
    }

    private static java.lang.String createMergedName(java.lang.Iterable<android.databinding.tool.Binding> bindings) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.databinding.tool.Binding binding : bindings) {
            sb.append(binding.getName());
        }
        return sb.toString();
    }

    public android.databinding.tool.expr.Expr[] getComponentExpressions() {
        android.databinding.tool.expr.ArgListExpr args = ((android.databinding.tool.expr.ArgListExpr) (getExpr()));
        return args.getChildren().toArray(new android.databinding.tool.expr.Expr[args.getChildren().size()]);
    }

    public java.lang.String[] getAttributes() {
        return mMultiAttributeSetter.attributes;
    }

    @java.lang.Override
    public java.lang.String getBindingAdapterInstanceClass() {
        return mMultiAttributeSetter.getBindingAdapterInstanceClass();
    }

    @java.lang.Override
    public boolean requiresOldValue() {
        return mMultiAttributeSetter.requiresOldValue();
    }

    @java.lang.Override
    public int getMinApi() {
        return 1;
    }

    @java.lang.Override
    public java.lang.String toJavaCode(java.lang.String targetViewName, java.lang.String bindingComponent) {
        final android.databinding.tool.expr.ArgListExpr args = ((android.databinding.tool.expr.ArgListExpr) (getExpr()));
        final java.util.List<java.lang.String> newValues = new java.util.ArrayList<java.lang.String>();
        for (android.databinding.tool.expr.Expr expr : args.getChildren()) {
            newValues.add(expr.toCode().generate());
        }
        final java.util.List<java.lang.String> oldValues;
        if (requiresOldValue()) {
            oldValues = new java.util.ArrayList<java.lang.String>();
            for (android.databinding.tool.expr.Expr expr : args.getChildren()) {
                oldValues.add("this." + android.databinding.tool.writer.LayoutBinderWriterKt.getOldValueName(expr));
            }
        } else {
            oldValues = java.util.Arrays.asList(new java.lang.String[args.getChildren().size()]);
        }
        final java.lang.String[] expressions = android.databinding.tool.MergedBinding.concat(oldValues, newValues, java.lang.String.class);
        android.databinding.tool.util.L.d("merged binding arg: %s", args.getUniqueKey());
        return mMultiAttributeSetter.toJava(bindingComponent, targetViewName, expressions);
    }

    private static <T> T[] concat(java.util.List<T> l1, java.util.List<T> l2, java.lang.Class<T> klass) {
        java.util.List<T> result = new java.util.ArrayList<T>();
        result.addAll(l1);
        result.addAll(l2);
        return result.toArray(((T[]) (java.lang.reflect.Array.newInstance(klass, result.size()))));
    }
}

