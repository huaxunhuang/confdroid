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


public class MethodCallExpr extends android.databinding.tool.expr.Expr {
    final java.lang.String mName;

    android.databinding.tool.reflection.Callable mGetter;

    static java.util.List<android.databinding.tool.expr.Expr> concat(android.databinding.tool.expr.Expr e, java.util.List<android.databinding.tool.expr.Expr> list) {
        java.util.List<android.databinding.tool.expr.Expr> merged = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        merged.add(e);
        merged.addAll(list);
        return merged;
    }

    MethodCallExpr(android.databinding.tool.expr.Expr target, java.lang.String name, java.util.List<android.databinding.tool.expr.Expr> args) {
        super(android.databinding.tool.expr.MethodCallExpr.concat(target, args));
        mName = name;
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
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        android.databinding.tool.writer.KCode code = new android.databinding.tool.writer.KCode().app("", getTarget().toCode(expand)).app(".").app(getGetter().name).app("(");
        boolean first = true;
        for (android.databinding.tool.expr.Expr arg : getArgs()) {
            if (first) {
                first = false;
            } else {
                code.app(", ");
            }
            code.app("", arg.toCode(expand));
        }
        code.app(")");
        return code;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        if (mGetter == null) {
            java.util.List<android.databinding.tool.reflection.ModelClass> args = new java.util.ArrayList<android.databinding.tool.reflection.ModelClass>();
            for (android.databinding.tool.expr.Expr expr : getArgs()) {
                args.add(expr.getResolvedType());
            }
            android.databinding.tool.expr.Expr target = getTarget();
            boolean isStatic = target instanceof android.databinding.tool.expr.StaticIdentifierExpr;
            android.databinding.tool.reflection.ModelMethod method = target.getResolvedType().getMethod(mName, args, isStatic);
            if (method == null) {
                java.lang.String message = (("cannot find method '" + mName) + "' in class ") + target.getResolvedType().toJavaCode();
                java.lang.IllegalArgumentException e = new java.lang.IllegalArgumentException(message);
                android.databinding.tool.util.L.e(e, "cannot find method %s in class %s", mName, target.getResolvedType().toJavaCode());
                throw e;
            }
            if ((!isStatic) && method.isStatic()) {
                // found a static method on an instance. Use class instead
                target.getParents().remove(this);
                getChildren().remove(target);
                android.databinding.tool.expr.StaticIdentifierExpr staticId = getModel().staticIdentifierFor(target.getResolvedType());
                getChildren().add(staticId);
                staticId.getParents().add(this);
                // make sure we update this in case we access it below
                target = getTarget();
            }
            int flags = android.databinding.tool.reflection.Callable.DYNAMIC;
            if (method.isStatic()) {
                flags |= android.databinding.tool.reflection.Callable.STATIC;
            }
            mGetter = new android.databinding.tool.reflection.Callable(android.databinding.tool.reflection.Callable.Type.METHOD, method.getName(), null, method.getReturnType(args), method.getParameterTypes().length, flags);
        }
        return mGetter.resolvedType;
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        final java.util.List<android.databinding.tool.expr.Dependency> dependencies = constructDynamicChildrenDependencies();
        for (android.databinding.tool.expr.Dependency dependency : dependencies) {
            if (dependency.getOther() == getTarget()) {
                dependency.setMandatory(true);
            }
        }
        return dependencies;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return android.databinding.tool.expr.Expr.join(getTarget().computeUniqueKey(), mName, super.computeUniqueKey());
    }

    public android.databinding.tool.expr.Expr getTarget() {
        return getChildren().get(0);
    }

    public java.lang.String getName() {
        return mName;
    }

    public java.util.List<android.databinding.tool.expr.Expr> getArgs() {
        return getChildren().subList(1, getChildren().size());
    }

    public android.databinding.tool.reflection.Callable getGetter() {
        return mGetter;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Method calls may not be used in two-way expressions";
    }
}

