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


public class ComparisonExpr extends android.databinding.tool.expr.Expr {
    final java.lang.String mOp;

    ComparisonExpr(java.lang.String op, android.databinding.tool.expr.Expr left, android.databinding.tool.expr.Expr right) {
        super(left, right);
        mOp = op;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return android.databinding.tool.expr.Expr.join(mOp, super.computeUniqueKey());
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return modelAnalyzer.loadPrimitive("boolean");
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return constructDynamicChildrenDependencies();
    }

    public java.lang.String getOp() {
        return mOp;
    }

    public android.databinding.tool.expr.Expr getLeft() {
        return getChildren().get(0);
    }

    public android.databinding.tool.expr.Expr getRight() {
        return getChildren().get(1);
    }

    @java.lang.Override
    public boolean isEqualityCheck() {
        return "==".equals(mOp.trim());
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app("", getLeft().toCode(expand)).app(" ").app(getOp()).app(" ").app("", getRight().toCode(expand));
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Comparison operators are not valid as targets of two-way binding";
    }
}

