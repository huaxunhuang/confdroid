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


public class BracketExpr extends android.databinding.tool.expr.Expr {
    public enum BracketAccessor {

        ARRAY,
        LIST,
        MAP;}

    private android.databinding.tool.expr.BracketExpr.BracketAccessor mAccessor;

    BracketExpr(android.databinding.tool.expr.Expr target, android.databinding.tool.expr.Expr arg) {
        super(target, arg);
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        android.databinding.tool.reflection.ModelClass targetType = getTarget().getResolvedType();
        if (targetType.isArray()) {
            mAccessor = android.databinding.tool.expr.BracketExpr.BracketAccessor.ARRAY;
        } else
            if (targetType.isList()) {
                mAccessor = android.databinding.tool.expr.BracketExpr.BracketAccessor.LIST;
            } else
                if (targetType.isMap()) {
                    mAccessor = android.databinding.tool.expr.BracketExpr.BracketAccessor.MAP;
                } else {
                    throw new java.lang.IllegalArgumentException(("Cannot determine variable type used in [] " + ("expression. Cast the value to List, Map, " + "or array. Type detected: ")) + targetType.toJavaCode());
                }


        return targetType.getComponentType();
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        final java.util.List<android.databinding.tool.expr.Dependency> dependencies = constructDynamicChildrenDependencies();
        for (android.databinding.tool.expr.Dependency dependency : dependencies) {
            if (dependency.getOther() == getTarget()) {
                dependency.setMandatory(true);
            }
        }
        return dependencies;
    }

    protected java.lang.String computeUniqueKey() {
        final java.lang.String targetKey = getTarget().computeUniqueKey();
        return addTwoWay(android.databinding.tool.expr.Expr.join(targetKey, "$", getArg().computeUniqueKey(), "$"));
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return null;
    }

    public android.databinding.tool.expr.Expr getTarget() {
        return getChildren().get(0);
    }

    public android.databinding.tool.expr.Expr getArg() {
        return getChildren().get(1);
    }

    public android.databinding.tool.expr.BracketExpr.BracketAccessor getAccessor() {
        return mAccessor;
    }

    public boolean argCastsInteger() {
        return (mAccessor != android.databinding.tool.expr.BracketExpr.BracketAccessor.MAP) && getArg().getResolvedType().isObject();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        java.lang.String cast = (argCastsInteger()) ? "(Integer) " : "";
        switch (getAccessor()) {
            case ARRAY :
                {
                    return new android.databinding.tool.writer.KCode().app("getFromArray(", getTarget().toCode()).app(", ").app(cast, getArg().toCode()).app(")");
                }
            case LIST :
                {
                    android.databinding.tool.reflection.ModelClass listType = android.databinding.tool.reflection.ModelAnalyzer.getInstance().findClass(java.util.List.class).erasure();
                    android.databinding.tool.reflection.ModelClass targetType = getTarget().getResolvedType().erasure();
                    if (listType.isAssignableFrom(targetType)) {
                        return new android.databinding.tool.writer.KCode().app("getFromList(", getTarget().toCode()).app(", ").app(cast, getArg().toCode()).app(")");
                    } else {
                        return new android.databinding.tool.writer.KCode().app("", getTarget().toCode()).app(".get(").app(cast, getArg().toCode()).app(")");
                    }
                }
            case MAP :
                return new android.databinding.tool.writer.KCode().app("", getTarget().toCode()).app(".get(", getArg().toCode()).app(")");
        }
        throw new java.lang.IllegalStateException("Invalid BracketAccessor type");
    }

    @java.lang.Override
    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        java.lang.String cast = (argCastsInteger()) ? "(Integer) " : "";
        return new android.databinding.tool.writer.KCode().app("setTo(", getTarget().toCode(true)).app(", ").app(cast, getArg().toCode(true)).app(", ", value).app(");");
    }
}

