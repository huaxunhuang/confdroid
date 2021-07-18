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


public class BuiltInVariableExpr extends android.databinding.tool.expr.IdentifierExpr {
    private final java.lang.String mAccessCode;

    BuiltInVariableExpr(java.lang.String name, java.lang.String type, java.lang.String accessCode) {
        super(name);
        super.setUserDefinedType(type);
        this.mAccessCode = accessCode;
    }

    @java.lang.Override
    public boolean isDynamic() {
        return false;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        android.databinding.tool.reflection.ModelClass modelClass = super.resolveType(modelAnalyzer);
        return modelClass;
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return new java.util.ArrayList<android.databinding.tool.expr.Dependency>();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        if (mAccessCode == null) {
            return new android.databinding.tool.writer.KCode().app(mName);
        } else {
            return new android.databinding.tool.writer.KCode().app(mAccessCode);
        }
    }

    public boolean isDeclared() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Built-in variables may not be the target of two-way binding";
    }
}

