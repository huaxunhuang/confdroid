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


public class InstanceOfExpr extends android.databinding.tool.expr.Expr {
    final java.lang.String mTypeStr;

    android.databinding.tool.reflection.ModelClass mType;

    InstanceOfExpr(android.databinding.tool.expr.Expr left, java.lang.String type) {
        super(left);
        mTypeStr = type;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return android.databinding.tool.expr.Expr.join("instanceof", super.computeUniqueKey(), mTypeStr);
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app("", getExpr().toCode(expand)).app(" instanceof ").app(getType().toJavaCode());
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        mType = modelAnalyzer.findClass(mTypeStr, getModel().getImports());
        return modelAnalyzer.loadPrimitive("boolean");
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return constructDynamicChildrenDependencies();
    }

    public android.databinding.tool.expr.Expr getExpr() {
        return getChildren().get(0);
    }

    public android.databinding.tool.reflection.ModelClass getType() {
        return mType;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "two-way binding can't target a value with the 'instanceof' operator";
    }
}

