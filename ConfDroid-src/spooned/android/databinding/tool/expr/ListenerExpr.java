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
 * This wraps an expression, but makes it unique for a particular event listener type.
 * This is used to differentiate listener methods. For example:
 * <pre>
 *     public void onFoo(String str) {...}
 *     public void onFoo(int i) {...}
 * </pre>
 */
public class ListenerExpr extends android.databinding.tool.expr.Expr {
    private final java.lang.String mName;

    private final android.databinding.tool.reflection.ModelClass mListenerType;

    private final android.databinding.tool.reflection.ModelMethod mMethod;

    ListenerExpr(android.databinding.tool.expr.Expr expr, java.lang.String name, android.databinding.tool.reflection.ModelClass listenerType, android.databinding.tool.reflection.ModelMethod method) {
        super(expr);
        mName = name;
        mListenerType = listenerType;
        mMethod = method;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return mListenerType;
    }

    public android.databinding.tool.reflection.ModelMethod getMethod() {
        return mMethod;
    }

    public android.databinding.tool.expr.Expr getChild() {
        return getChildren().get(0);
    }

    public java.lang.String getName() {
        return mName;
    }

    @java.lang.Override
    public boolean isDynamic() {
        return getChild().isDynamic();
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        final java.util.List<android.databinding.tool.expr.Dependency> dependencies = new java.util.ArrayList<android.databinding.tool.expr.Dependency>();
        android.databinding.tool.expr.Dependency dependency = new android.databinding.tool.expr.Dependency(this, getChild());
        dependency.setMandatory(true);
        dependencies.add(dependency);
        return dependencies;
    }

    protected java.lang.String computeUniqueKey() {
        return android.databinding.tool.expr.Expr.join(getResolvedType().getCanonicalName(), getChild().computeUniqueKey(), mName);
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode generateCode(boolean expand) {
        android.databinding.tool.writer.KCode code = new android.databinding.tool.writer.KCode("(");
        final int minApi = java.lang.Math.max(mListenerType.getMinApi(), mMethod.getMinApi());
        if (minApi > 1) {
            code.app(("(getBuildSdkInt() < " + minApi) + ") ? null : ");
        }
        final java.lang.String fieldName = android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(this);
        final java.lang.String listenerClassName = android.databinding.tool.writer.LayoutBinderWriterKt.getListenerClassName(this);
        final android.databinding.tool.writer.KCode value = getChild().toCode();
        code.app("((").app(fieldName).app(" == null) ? (").app(fieldName).app(" = new ").app(listenerClassName).app("()) : ").app(fieldName).app(")");
        if (getChild().isDynamic()) {
            code.app(".setValue(", value).app(")");
        }
        code.app(")");
        return code;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Listeners cannot be the target of a two-way binding";
    }
}

