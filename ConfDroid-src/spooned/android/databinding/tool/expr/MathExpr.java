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


public class MathExpr extends android.databinding.tool.expr.Expr {
    final java.lang.String mOp;

    MathExpr(android.databinding.tool.expr.Expr left, java.lang.String op, android.databinding.tool.expr.Expr right) {
        super(left, right);
        mOp = op;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return addTwoWay(android.databinding.tool.expr.Expr.join(getLeft().getUniqueKey(), mOp, getRight().getUniqueKey()));
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        if ("+".equals(mOp)) {
            // TODO we need upper casting etc.
            if (getLeft().getResolvedType().isString() || getRight().getResolvedType().isString()) {
                return modelAnalyzer.findClass(java.lang.String.class);
            }
        }
        return modelAnalyzer.findCommonParentOf(getLeft().getResolvedType(), getRight().getResolvedType());
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
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app("", getLeft().toCode(expand)).app(mOp, getRight().toCode(expand));
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        if (mOp.equals("%")) {
            return "The modulus operator (%) is not supported in two-way binding.";
        } else
            if (getResolvedType().isString()) {
                return "String concatenation operator (+) is not supported in two-way binding.";
            }

        if (!getLeft().isDynamic()) {
            return getRight().getInvertibleError();
        } else
            if (!getRight().isDynamic()) {
                return getLeft().getInvertibleError();
            } else {
                return ("Arithmetic operator " + mOp) + " is not supported with two dynamic expressions.";
            }

    }

    private java.lang.String inverseCast() {
        if (!getLeft().isDynamic()) {
            return inverseCast(getRight());
        } else {
            return inverseCast(getLeft());
        }
    }

    private java.lang.String inverseCast(android.databinding.tool.expr.Expr expr) {
        if (!expr.getResolvedType().isAssignableFrom(getResolvedType())) {
            return ("(" + getResolvedType()) + ")";
        }
        return null;
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        if (!isDynamic()) {
            return toCode();
        }
        final android.databinding.tool.expr.Expr left = getLeft();
        final android.databinding.tool.expr.Expr right = getRight();
        final android.databinding.tool.expr.Expr constExpr = (left.isDynamic()) ? right : left;
        final android.databinding.tool.expr.Expr varExpr = (left.isDynamic()) ? left : right;
        final java.lang.String cast = inverseCast();
        if (cast != null) {
            value = new android.databinding.tool.writer.KCode(cast).app("(", value).app(")");
        }
        switch (mOp.charAt(0)) {
            case '+' :
                // const + x = value  => x = value - const
                return varExpr.toInverseCode(value.app(" - (", constExpr.toCode()).app(")"));
            case '*' :
                // const * x = value => x = value / const
                return varExpr.toInverseCode(value.app(" / (", constExpr.toCode()).app(")"));
            case '-' :
                if (!left.isDynamic()) {
                    // const - x = value => x = const - value)
                    return varExpr.toInverseCode(new android.databinding.tool.writer.KCode().app("(", constExpr.toCode()).app(") - (", value).app(")"));
                } else {
                    // x - const = value => x = value + const)
                    return varExpr.toInverseCode(value.app(" + ", constExpr.toCode()));
                }
            case '/' :
                if (!left.isDynamic()) {
                    // const / x = value => x = const / value
                    return varExpr.toInverseCode(new android.databinding.tool.writer.KCode("(").app("", constExpr.toCode()).app(") / (", value).app(")"));
                } else {
                    // x / const = value => x = value * const
                    return varExpr.toInverseCode(new android.databinding.tool.writer.KCode("(").app("", value).app(") * (", constExpr.toCode()).app(")"));
                }
        }
        throw new java.lang.IllegalStateException("Invalid math operation is not invertible: " + mOp);
    }
}

