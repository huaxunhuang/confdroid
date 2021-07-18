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
package android.databinding.tool.writer;


public class ComponentWriter {
    private static final java.lang.String INDENT = "    ";

    public ComponentWriter() {
    }

    public java.lang.String createComponent() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("package android.databinding;\n\n");
        builder.append("public interface DataBindingComponent {\n");
        final android.databinding.tool.store.SetterStore setterStore = android.databinding.tool.store.SetterStore.get(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> bindingAdapters = setterStore.getComponentBindingAdapters();
        for (final java.lang.String simpleName : bindingAdapters.keySet()) {
            final java.util.List<java.lang.String> classes = bindingAdapters.get(simpleName);
            if (classes.size() > 1) {
                int index = 1;
                for (java.lang.String className : classes) {
                    android.databinding.tool.writer.ComponentWriter.addGetter(builder, simpleName, className, index++);
                }
            } else {
                android.databinding.tool.writer.ComponentWriter.addGetter(builder, simpleName, classes.iterator().next(), 0);
            }
        }
        builder.append("}\n");
        return builder.toString();
    }

    private static void addGetter(java.lang.StringBuilder builder, java.lang.String simpleName, java.lang.String className, int index) {
        builder.append(android.databinding.tool.writer.ComponentWriter.INDENT).append(className).append(" get").append(simpleName);
        if (index > 0) {
            builder.append(index);
        }
        builder.append("();\n");
    }
}

