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
package android.databinding.tool;


/**
 * The main class that handles parsing files and generating classes.
 */
public class DataBinder {
    java.util.List<android.databinding.tool.LayoutBinder> mLayoutBinders = new java.util.ArrayList<android.databinding.tool.LayoutBinder>();

    private static final java.lang.String COMPONENT_CLASS = "android.databinding.DataBindingComponent";

    private android.databinding.tool.writer.JavaFileWriter mFileWriter;

    java.util.Set<java.lang.String> writtenClasses = new java.util.HashSet<java.lang.String>();

    public DataBinder(android.databinding.tool.store.ResourceBundle resourceBundle) {
        android.databinding.tool.util.L.d("reading resource bundle into data binder");
        for (java.util.Map.Entry<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> entry : resourceBundle.getLayoutBundles().entrySet()) {
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : entry.getValue()) {
                try {
                    mLayoutBinders.add(new android.databinding.tool.LayoutBinder(bundle));
                } catch (android.databinding.tool.processing.ScopedException ex) {
                    android.databinding.tool.processing.Scope.defer(ex);
                }
            }
        }
    }

    public java.util.List<android.databinding.tool.LayoutBinder> getLayoutBinders() {
        return mLayoutBinders;
    }

    public void sealModels() {
        for (android.databinding.tool.LayoutBinder layoutBinder : mLayoutBinders) {
            layoutBinder.sealModel();
        }
    }

    public void writerBaseClasses(boolean isLibrary) {
        for (android.databinding.tool.LayoutBinder layoutBinder : mLayoutBinders) {
            try {
                android.databinding.tool.processing.Scope.enter(layoutBinder);
                if (isLibrary || layoutBinder.hasVariations()) {
                    java.lang.String className = layoutBinder.getClassName();
                    java.lang.String canonicalName = (layoutBinder.getPackage() + ".") + className;
                    if (writtenClasses.contains(canonicalName)) {
                        continue;
                    }
                    android.databinding.tool.util.L.d("writing data binder base %s", canonicalName);
                    mFileWriter.writeToFile(canonicalName, layoutBinder.writeViewBinderBaseClass(isLibrary));
                    writtenClasses.add(canonicalName);
                }
            } catch (android.databinding.tool.processing.ScopedException ex) {
                android.databinding.tool.processing.Scope.defer(ex);
            } finally {
                android.databinding.tool.processing.Scope.exit();
            }
        }
    }

    public void writeBinders(int minSdk) {
        for (android.databinding.tool.LayoutBinder layoutBinder : mLayoutBinders) {
            try {
                android.databinding.tool.processing.Scope.enter(layoutBinder);
                java.lang.String className = layoutBinder.getImplementationName();
                java.lang.String canonicalName = (layoutBinder.getPackage() + ".") + className;
                android.databinding.tool.util.L.d("writing data binder %s", canonicalName);
                writtenClasses.add(canonicalName);
                mFileWriter.writeToFile(canonicalName, layoutBinder.writeViewBinder(minSdk));
            } catch (android.databinding.tool.processing.ScopedException ex) {
                android.databinding.tool.processing.Scope.defer(ex);
            } finally {
                android.databinding.tool.processing.Scope.exit();
            }
        }
    }

    public void writeComponent() {
        android.databinding.tool.writer.ComponentWriter componentWriter = new android.databinding.tool.writer.ComponentWriter();
        writtenClasses.add(android.databinding.tool.DataBinder.COMPONENT_CLASS);
        mFileWriter.writeToFile(android.databinding.tool.DataBinder.COMPONENT_CLASS, componentWriter.createComponent());
    }

    public java.util.Set<java.lang.String> getWrittenClassNames() {
        return writtenClasses;
    }

    public void setFileWriter(android.databinding.tool.writer.JavaFileWriter fileWriter) {
        mFileWriter = fileWriter;
    }

    public android.databinding.tool.writer.JavaFileWriter getFileWriter() {
        return mFileWriter;
    }
}

