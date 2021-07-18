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


public class CastExpr extends android.databinding.tool.expr.Expr {
    final java.lang.String mType;

    CastExpr(java.lang.String type, android.databinding.tool.expr.Expr expr) {
        super(expr);
        mType = type;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return modelAnalyzer.findClass(mType, getModel().getImports());
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        final java.util.List<android.databinding.tool.expr.Dependency> dependencies = constructDynamicChildrenDependencies();
        for (android.databinding.tool.expr.Dependency dependency : dependencies) {
            dependency.setMandatory(true);
        }
        return dependencies;
    }

    protected java.lang.String computeUniqueKey() {
        return addTwoWay(android.databinding.tool.expr.Expr.join(mType, getCastExpr().computeUniqueKey()));
    }

    public android.databinding.tool.expr.Expr getCastExpr() {
        return getChildren().get(0);
    }

    public java.lang.String getCastType() {
        return getResolvedType().toJavaCode();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app("(").app(getCastType()).app(") ", getCastExpr().toCode(expand));
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return getCastExpr().getInvertibleError();
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        // assume no need to cast in reverse
        return getCastExpr().toInverseCode(value);
    }
}

