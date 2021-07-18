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


public class SymbolExpr extends android.databinding.tool.expr.Expr {
    java.lang.String mText;

    java.lang.Class mType;

    SymbolExpr(java.lang.String text, java.lang.Class type) {
        super();
        mText = text;
        mType = type;
    }

    public java.lang.String getText() {
        return mText;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return modelAnalyzer.findClass(mType);
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return mType.getSimpleName() + mText;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return ("Symbol '" + mText) + "' cannot be the target of a two-way binding expression";
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode(getText());
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return new java.util.ArrayList<android.databinding.tool.expr.Dependency>();
    }
}

