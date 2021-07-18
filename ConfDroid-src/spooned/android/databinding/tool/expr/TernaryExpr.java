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


public class TernaryExpr extends android.databinding.tool.expr.Expr {
    TernaryExpr(android.databinding.tool.expr.Expr pred, android.databinding.tool.expr.Expr ifTrue, android.databinding.tool.expr.Expr ifFalse) {
        super(pred, ifTrue, ifFalse);
    }

    public android.databinding.tool.expr.Expr getPred() {
        return getChildren().get(0);
    }

    public android.databinding.tool.expr.Expr getIfTrue() {
        return getChildren().get(1);
    }

    public android.databinding.tool.expr.Expr getIfFalse() {
        return getChildren().get(2);
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return "?:" + super.computeUniqueKey();
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        if (getPred().isDynamic()) {
            return "The condition of a ternary operator must be constant: " + getPred().toFullCode();
        }
        final java.lang.String trueInvertible = getIfTrue().getInvertibleError();
        if (trueInvertible != null) {
            return trueInvertible;
        } else {
            return getIfFalse().getInvertibleError();
        }
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        final android.databinding.tool.expr.Expr ifTrue = getIfTrue();
        final android.databinding.tool.expr.Expr ifFalse = getIfFalse();
        if (android.databinding.tool.expr.TernaryExpr.isNullLiteral(ifTrue)) {
            return ifFalse.getResolvedType();
        } else
            if (android.databinding.tool.expr.TernaryExpr.isNullLiteral(ifFalse)) {
                return ifTrue.getResolvedType();
            }

        return modelAnalyzer.findCommonParentOf(getIfTrue().getResolvedType(), getIfFalse().getResolvedType());
    }

    private static boolean isNullLiteral(android.databinding.tool.expr.Expr expr) {
        final android.databinding.tool.reflection.ModelClass type = expr.getResolvedType();
        return (type.isObject() && (expr instanceof android.databinding.tool.expr.SymbolExpr)) && "null".equals(((android.databinding.tool.expr.SymbolExpr) (expr)).getText());
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        java.util.List<android.databinding.tool.expr.Dependency> deps = new java.util.ArrayList<android.databinding.tool.expr.Dependency>();
        android.databinding.tool.expr.Expr predExpr = getPred();
        final android.databinding.tool.expr.Dependency pred = new android.databinding.tool.expr.Dependency(this, predExpr);
        pred.setMandatory(true);
        deps.add(pred);
        android.databinding.tool.expr.Expr ifTrueExpr = getIfTrue();
        if (ifTrueExpr.isDynamic()) {
            deps.add(new android.databinding.tool.expr.Dependency(this, ifTrueExpr, predExpr, true));
        }
        android.databinding.tool.expr.Expr ifFalseExpr = getIfFalse();
        if (ifFalseExpr.isDynamic()) {
            deps.add(new android.databinding.tool.expr.Dependency(this, ifFalseExpr, predExpr, false));
        }
        return deps;
    }

    @java.lang.Override
    protected java.util.BitSet getPredicateInvalidFlags() {
        return getPred().getInvalidFlags();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app("", getPred().toCode(expand)).app(" ? ", getIfTrue().toCode(expand)).app(" : ", getIfFalse().toCode(expand));
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode variable) {
        return new android.databinding.tool.writer.KCode().app("if (", getPred().toCode(true)).app(") {").tab(getIfTrue().toInverseCode(variable)).nl(new android.databinding.tool.writer.KCode("} else {")).tab(getIfFalse().toInverseCode(variable)).nl(new android.databinding.tool.writer.KCode("}"));
    }

    @java.lang.Override
    public boolean isConditional() {
        return true;
    }
}

