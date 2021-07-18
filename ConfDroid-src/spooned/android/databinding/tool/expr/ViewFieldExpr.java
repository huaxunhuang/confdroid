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


public class ViewFieldExpr extends android.databinding.tool.expr.BuiltInVariableExpr {
    private final android.databinding.tool.BindingTarget mBindingTarget;

    ViewFieldExpr(android.databinding.tool.BindingTarget bindingTarget) {
        super(android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(bindingTarget), android.databinding.tool.expr.ViewFieldExpr.initialType(bindingTarget), android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(bindingTarget));
        mBindingTarget = bindingTarget;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "View fields may not be the target of two-way binding";
    }

    private static java.lang.String initialType(android.databinding.tool.BindingTarget bindingTarget) {
        return bindingTarget.isBinder() ? "android.databinding.ViewDataBinding" : bindingTarget.getInterfaceType();
    }

    public android.databinding.tool.BindingTarget getBindingTarget() {
        return mBindingTarget;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        final android.databinding.tool.reflection.ModelClass type = modelAnalyzer.findClass(mBindingTarget.getInterfaceType(), null);
        if (type == null) {
            return modelAnalyzer.findClass("android.databinding.ViewDataBinding", null);
        }
        return type;
    }
}

