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


/**
 * This is a special expression that is created when we have an adapter that has multiple
 * parameters.
 * <p>
 * When it is detected, we create a new binding with this argument list expression and merge N
 * bindings into a new one so that rest of the code generation logic works as expected.
 */
public class ArgListExpr extends android.databinding.tool.expr.Expr {
    private int mId;

    public ArgListExpr(int id, java.lang.Iterable<android.databinding.tool.expr.Expr> children) {
        super(children);
        mId = id;
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return (("ArgList[" + mId) + "]") + super.computeUniqueKey();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        throw new java.lang.IllegalStateException("should never try to convert an argument expressions" + " into code");
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return modelAnalyzer.findClass(java.lang.Void.class);
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return super.constructDynamicChildrenDependencies();
    }

    @java.lang.Override
    public boolean canBeEvaluatedToAVariable() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Merged bindings are not invertible.";
    }
}

