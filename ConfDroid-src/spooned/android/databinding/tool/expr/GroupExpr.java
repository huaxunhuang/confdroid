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


public class GroupExpr extends android.databinding.tool.expr.Expr {
    public GroupExpr(android.databinding.tool.expr.Expr wrapped) {
        super(wrapped);
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return getWrapped().getResolvedType();
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return getWrapped().constructDependencies();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode().app("(", getWrapped().toCode(expand)).app(")");
    }

    public android.databinding.tool.expr.Expr getWrapped() {
        return getChildren().get(0);
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        // Nothing to do here. Other expressions should automatically take care of grouping.
        return getWrapped().toInverseCode(value);
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return getWrapped().getInvertibleError();
    }
}

