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


public class UnaryExpr extends android.databinding.tool.expr.Expr {
    final java.lang.String mOp;

    UnaryExpr(java.lang.String op, android.databinding.tool.expr.Expr expr) {
        super(expr);
        mOp = op;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return getExpr().getInvertibleError();
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return addTwoWay(android.databinding.tool.expr.Expr.join(getOpStr(), getExpr().getUniqueKey()));
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        return getExpr().toInverseCode(new android.databinding.tool.writer.KCode().app(mOp, value));
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app(getOp(), getExpr().toCode(expand));
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return getExpr().getResolvedType();
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return constructDynamicChildrenDependencies();
    }

    private java.lang.String getOpStr() {
        switch (mOp.charAt(0)) {
            case '~' :
                return "bitNot";
            case '!' :
                return "logicalNot";
            case '-' :
                return "unaryMinus";
            case '+' :
                return "unaryPlus";
        }
        return mOp;
    }

    public java.lang.String getOp() {
        return mOp;
    }

    public android.databinding.tool.expr.Expr getExpr() {
        return getChildren().get(0);
    }
}

