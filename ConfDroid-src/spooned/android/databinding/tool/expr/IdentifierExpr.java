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


public class IdentifierExpr extends android.databinding.tool.expr.Expr {
    java.lang.String mName;

    java.lang.String mUserDefinedType;

    private boolean mIsDeclared;

    IdentifierExpr(java.lang.String name) {
        mName = name;
    }

    public java.lang.String getName() {
        return mName;
    }

    /**
     * If this is root, its type should be set while parsing the XML document
     *
     * @param userDefinedType
     * 		The type of this identifier
     */
    public void setUserDefinedType(java.lang.String userDefinedType) {
        mUserDefinedType = userDefinedType;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return android.databinding.tool.expr.Expr.join(mName, super.computeUniqueKey());
    }

    public java.lang.String getUserDefinedType() {
        return mUserDefinedType;
    }

    @java.lang.Override
    public boolean isDynamic() {
        return true;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(final android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        android.databinding.tool.util.Preconditions.checkNotNull(mUserDefinedType, android.databinding.tool.processing.ErrorMessages.UNDEFINED_VARIABLE, mName);
        return modelAnalyzer.findClass(mUserDefinedType, getModel().getImports());
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return new java.util.ArrayList<android.databinding.tool.expr.Dependency>();
    }

    @java.lang.Override
    protected java.lang.String asPackage() {
        return mUserDefinedType == null ? mName : null;
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        if (expand) {
            return new android.databinding.tool.writer.KCode(android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(this));
        } else {
            return new android.databinding.tool.writer.KCode(android.databinding.tool.writer.LayoutBinderWriterKt.getExecutePendingLocalName(this));
        }
    }

    public void setDeclared() {
        mIsDeclared = true;
    }

    public boolean isDeclared() {
        return mIsDeclared;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return null;
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        return new android.databinding.tool.writer.KCode().app(android.databinding.tool.writer.LayoutBinderWriterKt.getSetterName(this)).app("(", value).app(");");
    }
}

