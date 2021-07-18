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
package android.databinding.annotationprocessor;


public class ProcessExpressions extends android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep {
    public ProcessExpressions() {
    }

    @java.lang.Override
    public boolean onHandleStep(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) throws javax.xml.bind.JAXBException {
        android.databinding.tool.store.ResourceBundle resourceBundle;
        android.databinding.tool.reflection.SdkUtil.initialize(buildInfo.minSdk(), new java.io.File(buildInfo.sdkRoot()));
        resourceBundle = new android.databinding.tool.store.ResourceBundle(buildInfo.modulePackage());
        java.util.List<android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2> intermediateList = loadDependencyIntermediates();
        for (android.databinding.annotationprocessor.ProcessExpressions.Intermediate intermediate : intermediateList) {
            try {
                intermediate.appendTo(resourceBundle);
            } catch (java.lang.Throwable throwable) {
                android.databinding.tool.util.L.e(throwable, "unable to prepare resource bundle");
            }
        }
        android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2 mine = createIntermediateFromLayouts(buildInfo.layoutInfoDir(), intermediateList);
        if (mine != null) {
            mine.updateOverridden(resourceBundle);
            intermediateList.add(mine);
            saveIntermediate(processingEnvironment, buildInfo, mine);
            mine.appendTo(resourceBundle);
        }
        // generate them here so that bindable parser can read
        try {
            writeResourceBundle(resourceBundle, buildInfo.isLibrary(), buildInfo.minSdk(), buildInfo.exportClassListTo());
        } catch (java.lang.Throwable t) {
            android.databinding.tool.util.L.e(t, "cannot generate view binders");
        }
        return true;
    }

    private java.util.List<android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2> loadDependencyIntermediates() {
        final java.util.List<android.databinding.annotationprocessor.ProcessExpressions.Intermediate> original = android.databinding.tool.util.GenerationalClassUtil.loadObjects(android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.LAYOUT);
        final java.util.List<android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2> upgraded = new java.util.ArrayList<android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2>(original.size());
        for (android.databinding.annotationprocessor.ProcessExpressions.Intermediate intermediate : original) {
            final android.databinding.annotationprocessor.ProcessExpressions.Intermediate updatedIntermediate = intermediate.upgrade();
            android.databinding.tool.util.Preconditions.check(updatedIntermediate instanceof android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2, "Incompatible data" + (" binding dependency. Please update your dependencies or recompile them with" + " application module's data binding version."));
            // noinspection ConstantConditions
            upgraded.add(((android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2) (updatedIntermediate)));
        }
        return upgraded;
    }

    private void saveIntermediate(javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo, android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2 intermediate) {
        android.databinding.tool.util.GenerationalClassUtil.writeIntermediateFile(processingEnvironment, buildInfo.modulePackage(), buildInfo.modulePackage() + android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.LAYOUT.getExtension(), intermediate);
    }

    @java.lang.Override
    public void onProcessingOver(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) {
    }

    private android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2 createIntermediateFromLayouts(java.lang.String layoutInfoFolderPath, java.util.List<android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2> intermediateList) {
        final java.util.Set<java.lang.String> excludeList = new java.util.HashSet<java.lang.String>();
        for (android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2 lib : intermediateList) {
            excludeList.addAll(lib.mLayoutInfoMap.keySet());
        }
        final java.io.File layoutInfoFolder = new java.io.File(layoutInfoFolderPath);
        if (!layoutInfoFolder.isDirectory()) {
            android.databinding.tool.util.L.d("layout info folder does not exist, skipping for %s", layoutInfoFolderPath);
            return null;
        }
        android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2 result = new android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2();
        for (java.io.File layoutFile : layoutInfoFolder.listFiles(new java.io.FilenameFilter() {
            @java.lang.Override
            public boolean accept(java.io.File dir, java.lang.String name) {
                return name.endsWith(".xml") && (!excludeList.contains(name));
            }
        })) {
            try {
                result.addEntry(layoutFile.getName(), org.apache.commons.io.FileUtils.readFileToString(layoutFile));
            } catch (java.io.IOException e) {
                android.databinding.tool.util.L.e(e, "cannot load layout file information. Try a clean build");
            }
        }
        return result;
    }

    private void writeResourceBundle(android.databinding.tool.store.ResourceBundle resourceBundle, boolean forLibraryModule, final int minSdk, java.lang.String exportClassNamesTo) throws javax.xml.bind.JAXBException {
        final android.databinding.tool.CompilerChef compilerChef = android.databinding.tool.CompilerChef.createChef(resourceBundle, getWriter());
        compilerChef.sealModels();
        compilerChef.writeComponent();
        if (compilerChef.hasAnythingToGenerate()) {
            compilerChef.writeViewBinderInterfaces(forLibraryModule);
            if (!forLibraryModule) {
                compilerChef.writeViewBinders(minSdk);
            }
        }
        if (forLibraryModule && (exportClassNamesTo == null)) {
            android.databinding.tool.util.L.e("When compiling a library module, build info must include exportClassListTo path");
        }
        if (forLibraryModule) {
            java.util.Set<java.lang.String> classNames = compilerChef.getWrittenClassNames();
            java.lang.String out = com.google.common.base.Joiner.on(android.databinding.tool.util.StringUtils.LINE_SEPARATOR).join(classNames);
            android.databinding.tool.util.L.d("Writing list of classes to %s . \nList:%s", exportClassNamesTo, out);
            try {
                // noinspection ConstantConditions
                org.apache.commons.io.FileUtils.write(new java.io.File(exportClassNamesTo), out);
            } catch (java.io.IOException e) {
                android.databinding.tool.util.L.e(e, "Cannot create list of written classes");
            }
        }
        mCallback.onChefReady(compilerChef, forLibraryModule, minSdk);
    }

    public interface Intermediate extends java.io.Serializable {
        android.databinding.annotationprocessor.ProcessExpressions.Intermediate upgrade();

        void appendTo(android.databinding.tool.store.ResourceBundle resourceBundle) throws java.lang.Throwable;
    }

    public static class IntermediateV1 implements android.databinding.annotationprocessor.ProcessExpressions.Intermediate {
        transient javax.xml.bind.Unmarshaller mUnmarshaller;

        // name to xml content map
        java.util.Map<java.lang.String, java.lang.String> mLayoutInfoMap = new java.util.HashMap<java.lang.String, java.lang.String>();

        @java.lang.Override
        public android.databinding.annotationprocessor.ProcessExpressions.Intermediate upgrade() {
            final android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2 updated = new android.databinding.annotationprocessor.ProcessExpressions.IntermediateV2();
            updated.mLayoutInfoMap = mLayoutInfoMap;
            updated.mUnmarshaller = mUnmarshaller;
            return updated;
        }

        @java.lang.Override
        public void appendTo(android.databinding.tool.store.ResourceBundle resourceBundle) throws javax.xml.bind.JAXBException {
            if (mUnmarshaller == null) {
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(android.databinding.tool.store.ResourceBundle.LayoutFileBundle.class);
                mUnmarshaller = context.createUnmarshaller();
            }
            for (java.lang.String content : mLayoutInfoMap.values()) {
                final java.io.InputStream is = org.apache.commons.io.IOUtils.toInputStream(content);
                try {
                    final android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle = ((android.databinding.tool.store.ResourceBundle.LayoutFileBundle) (mUnmarshaller.unmarshal(is)));
                    resourceBundle.addLayoutBundle(bundle);
                    android.databinding.tool.util.L.d("loaded layout info file %s", bundle);
                } finally {
                    org.apache.commons.io.IOUtils.closeQuietly(is);
                }
            }
        }

        public void addEntry(java.lang.String name, java.lang.String contents) {
            mLayoutInfoMap.put(name, contents);
        }

        // keeping the method to match deserialized structure
        @java.lang.SuppressWarnings("unused")
        public void removeOverridden(java.util.List<android.databinding.annotationprocessor.ProcessExpressions.Intermediate> existing) {
        }
    }

    public static class IntermediateV2 extends android.databinding.annotationprocessor.ProcessExpressions.IntermediateV1 {
        // specify so that we can define updates ourselves.
        private static final long serialVersionUID = 2L;

        @java.lang.Override
        public void appendTo(android.databinding.tool.store.ResourceBundle resourceBundle) throws javax.xml.bind.JAXBException {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : mLayoutInfoMap.entrySet()) {
                final java.io.InputStream is = org.apache.commons.io.IOUtils.toInputStream(entry.getValue());
                try {
                    final android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle = android.databinding.tool.store.ResourceBundle.LayoutFileBundle.fromXML(is);
                    resourceBundle.addLayoutBundle(bundle);
                    android.databinding.tool.util.L.d("loaded layout info file %s", bundle);
                } finally {
                    org.apache.commons.io.IOUtils.closeQuietly(is);
                }
            }
        }

        /**
         * if a layout is overridden from a module (which happens when layout is auto-generated),
         * we need to update its contents from the class that overrides it.
         * This must be done before this bundle is saved, otherwise, it will not be recognized
         * when it is used in another project.
         */
        public void updateOverridden(android.databinding.tool.store.ResourceBundle bundle) throws javax.xml.bind.JAXBException {
            // When a layout is copied from inherited module, it is eleminated while reading
            // info files. (createIntermediateFromLayouts).
            // Build process may also duplicate some files at compile time. This is where
            // we detect those copies and force inherit their module and classname information.
            final java.util.HashMap<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> bundles = bundle.getLayoutBundles();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> info : mLayoutInfoMap.entrySet()) {
                java.lang.String key = android.databinding.tool.LayoutXmlProcessor.exportLayoutNameFromInfoFileName(info.getKey());
                final java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> existingList = bundles.get(key);
                if ((existingList != null) && (!existingList.isEmpty())) {
                    android.databinding.tool.store.ResourceBundle.LayoutFileBundle myBundle = android.databinding.tool.store.ResourceBundle.LayoutFileBundle.fromXML(org.apache.commons.io.IOUtils.toInputStream(info.getValue()));
                    final android.databinding.tool.store.ResourceBundle.LayoutFileBundle inheritFrom = existingList.get(0);
                    myBundle.inheritConfigurationFrom(inheritFrom);
                    android.databinding.tool.util.L.d("inheriting data for %s (%s) from %s", info.getKey(), key, inheritFrom);
                    mLayoutInfoMap.put(info.getKey(), myBundle.toXML());
                }
            }
        }
    }
}

