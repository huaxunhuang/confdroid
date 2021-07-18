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
 * TwoWayListenerExpr is used to set the event listener for a two-way binding expression.
 */
public class TwoWayListenerExpr extends android.databinding.tool.expr.Expr {
    final android.databinding.tool.InverseBinding mInverseBinding;

    public TwoWayListenerExpr(android.databinding.tool.InverseBinding inverseBinding) {
        mInverseBinding = inverseBinding;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        return modelAnalyzer.findClass(android.databinding.InverseBindingListener.class);
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return constructDynamicChildrenDependencies();
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        final java.lang.String fieldName = android.databinding.tool.writer.LayoutBinderWriterKt.getFieldName(mInverseBinding);
        return new android.databinding.tool.writer.KCode(fieldName);
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        return ((("event(" + mInverseBinding.getEventAttribute()) + ", ") + java.lang.System.identityHashCode(mInverseBinding)) + ")";
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Inverted expressions are already inverted!";
    }
}

