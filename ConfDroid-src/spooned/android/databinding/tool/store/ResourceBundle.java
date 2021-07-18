/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool.store;


/**
 * This is a serializable class that can keep the result of parsing layout files.
 */
public class ResourceBundle implements java.io.Serializable {
    private static final java.lang.String[] ANDROID_VIEW_PACKAGE_VIEWS = new java.lang.String[]{ "View", "ViewGroup", "ViewStub", "TextureView", "SurfaceView" };

    private java.lang.String mAppPackage;

    private java.util.HashMap<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> mLayoutBundles = new java.util.HashMap<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>>();

    private java.util.List<java.io.File> mRemovedFiles = new java.util.ArrayList<java.io.File>();

    public ResourceBundle(java.lang.String appPackage) {
        mAppPackage = appPackage;
    }

    public void addLayoutBundle(android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle) {
        if (bundle.mFileName == null) {
            android.databinding.tool.util.L.e("File bundle must have a name. %s does not have one.", bundle);
            return;
        }
        if (!mLayoutBundles.containsKey(bundle.mFileName)) {
            mLayoutBundles.put(bundle.mFileName, new java.util.ArrayList<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>());
        }
        final java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> bundles = mLayoutBundles.get(bundle.mFileName);
        for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle existing : bundles) {
            if (existing.equals(bundle)) {
                android.databinding.tool.util.L.d("skipping layout bundle %s because it already exists.", bundle);
                return;
            }
        }
        android.databinding.tool.util.L.d("adding bundle %s", bundle);
        bundles.add(bundle);
    }

    public java.util.HashMap<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> getLayoutBundles() {
        return mLayoutBundles;
    }

    public java.lang.String getAppPackage() {
        return mAppPackage;
    }

    public void validateMultiResLayouts() {
        for (java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> layoutFileBundles : mLayoutBundles.values()) {
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle layoutFileBundle : layoutFileBundles) {
                java.util.List<android.databinding.tool.store.ResourceBundle.BindingTargetBundle> unboundIncludes = new java.util.ArrayList<android.databinding.tool.store.ResourceBundle.BindingTargetBundle>();
                for (android.databinding.tool.store.ResourceBundle.BindingTargetBundle target : layoutFileBundle.getBindingTargetBundles()) {
                    if (target.isBinder()) {
                        java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> boundTo = mLayoutBundles.get(target.getIncludedLayout());
                        if ((boundTo == null) || boundTo.isEmpty()) {
                            android.databinding.tool.util.L.d("There is no binding for %s, reverting to plain layout", target.getIncludedLayout());
                            if (target.getId() == null) {
                                unboundIncludes.add(target);
                            } else {
                                target.setIncludedLayout(null);
                                target.setInterfaceType("android.view.View");
                                target.mViewName = "android.view.View";
                            }
                        } else {
                            java.lang.String binding = boundTo.get(0).getFullBindingClass();
                            target.setInterfaceType(binding);
                        }
                    }
                }
                layoutFileBundle.getBindingTargetBundles().removeAll(unboundIncludes);
            }
        }
        for (java.util.Map.Entry<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> bundles : mLayoutBundles.entrySet()) {
            if (bundles.getValue().size() < 2) {
                continue;
            }
            // validate all ids are in correct view types
            // and all variables have the same name
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles.getValue()) {
                bundle.mHasVariations = true;
            }
            java.lang.String bindingClass = validateAndGetSharedClassName(bundles.getValue());
            java.util.Map<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation> variableTypes = validateAndMergeNameTypeLocations(bundles.getValue(), android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_VARIABLE_TYPE_MISMATCH, new android.databinding.tool.store.ResourceBundle.ValidateAndFilterCallback() {
                @java.lang.Override
                public java.util.List<? extends android.databinding.tool.store.ResourceBundle.NameTypeLocation> get(android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle) {
                    return bundle.mVariables;
                }
            });
            java.util.Map<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation> importTypes = validateAndMergeNameTypeLocations(bundles.getValue(), android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_IMPORT_TYPE_MISMATCH, new android.databinding.tool.store.ResourceBundle.ValidateAndFilterCallback() {
                @java.lang.Override
                public java.util.List<android.databinding.tool.store.ResourceBundle.NameTypeLocation> get(android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle) {
                    return bundle.mImports;
                }
            });
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles.getValue()) {
                // now add missing ones to each to ensure they can be referenced
                android.databinding.tool.util.L.d("checking for missing variables in %s / %s", bundle.mFileName, bundle.mConfigName);
                for (java.util.Map.Entry<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation> variable : variableTypes.entrySet()) {
                    if (!android.databinding.tool.store.ResourceBundle.NameTypeLocation.contains(bundle.mVariables, variable.getKey())) {
                        android.databinding.tool.store.ResourceBundle.NameTypeLocation orig = variable.getValue();
                        bundle.addVariable(orig.name, orig.type, orig.location, false);
                        android.databinding.tool.util.L.d("adding missing variable %s to %s / %s", variable.getKey(), bundle.mFileName, bundle.mConfigName);
                    }
                }
                for (java.util.Map.Entry<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation> userImport : importTypes.entrySet()) {
                    if (!android.databinding.tool.store.ResourceBundle.NameTypeLocation.contains(bundle.mImports, userImport.getKey())) {
                        bundle.mImports.add(userImport.getValue());
                        android.databinding.tool.util.L.d("adding missing import %s to %s / %s", userImport.getKey(), bundle.mFileName, bundle.mConfigName);
                    }
                }
            }
            java.util.Set<java.lang.String> includeBindingIds = new java.util.HashSet<java.lang.String>();
            java.util.Set<java.lang.String> viewBindingIds = new java.util.HashSet<java.lang.String>();
            java.util.Map<java.lang.String, java.lang.String> viewTypes = new java.util.HashMap<java.lang.String, java.lang.String>();
            java.util.Map<java.lang.String, java.lang.String> includes = new java.util.HashMap<java.lang.String, java.lang.String>();
            android.databinding.tool.util.L.d("validating ids for %s", bundles.getKey());
            java.util.Set<java.lang.String> conflictingIds = new java.util.HashSet<java.lang.String>();
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles.getValue()) {
                try {
                    android.databinding.tool.processing.Scope.enter(bundle);
                    for (android.databinding.tool.store.ResourceBundle.BindingTargetBundle target : bundle.mBindingTargetBundles) {
                        try {
                            android.databinding.tool.processing.Scope.enter(target);
                            android.databinding.tool.util.L.d("checking %s %s %s", target.getId(), target.getFullClassName(), target.isBinder());
                            if (target.mId != null) {
                                if (target.isBinder()) {
                                    if (viewBindingIds.contains(target.mId)) {
                                        android.databinding.tool.util.L.d("%s is conflicting", target.mId);
                                        conflictingIds.add(target.mId);
                                        continue;
                                    }
                                    includeBindingIds.add(target.mId);
                                } else {
                                    if (includeBindingIds.contains(target.mId)) {
                                        android.databinding.tool.util.L.d("%s is conflicting", target.mId);
                                        conflictingIds.add(target.mId);
                                        continue;
                                    }
                                    viewBindingIds.add(target.mId);
                                }
                                java.lang.String existingType = viewTypes.get(target.mId);
                                if (existingType == null) {
                                    android.databinding.tool.util.L.d("assigning %s as %s", target.getId(), target.getFullClassName());
                                    viewTypes.put(target.mId, target.getFullClassName());
                                    if (target.isBinder()) {
                                        includes.put(target.mId, target.getIncludedLayout());
                                    }
                                } else
                                    if (!existingType.equals(target.getFullClassName())) {
                                        if (target.isBinder()) {
                                            android.databinding.tool.util.L.d("overriding %s as base binder", target.getId());
                                            viewTypes.put(target.mId, "android.databinding.ViewDataBinding");
                                            includes.put(target.mId, target.getIncludedLayout());
                                        } else {
                                            android.databinding.tool.util.L.d("overriding %s as base view", target.getId());
                                            viewTypes.put(target.mId, "android.view.View");
                                        }
                                    }

                            }
                        } catch (android.databinding.tool.processing.ScopedException ex) {
                            android.databinding.tool.processing.Scope.defer(ex);
                        } finally {
                            android.databinding.tool.processing.Scope.exit();
                        }
                    }
                } finally {
                    android.databinding.tool.processing.Scope.exit();
                }
            }
            if (!conflictingIds.isEmpty()) {
                for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles.getValue()) {
                    for (android.databinding.tool.store.ResourceBundle.BindingTargetBundle target : bundle.mBindingTargetBundles) {
                        if (conflictingIds.contains(target.mId)) {
                            android.databinding.tool.processing.Scope.registerError(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_ID_USED_AS_IMPORT, target.mId), bundle, target);
                        }
                    }
                }
            }
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles.getValue()) {
                try {
                    android.databinding.tool.processing.Scope.enter(bundle);
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> viewType : viewTypes.entrySet()) {
                        android.databinding.tool.store.ResourceBundle.BindingTargetBundle target = bundle.getBindingTargetById(viewType.getKey());
                        if (target == null) {
                            java.lang.String include = includes.get(viewType.getKey());
                            if (include == null) {
                                bundle.createBindingTarget(viewType.getKey(), viewType.getValue(), false, null, null, null);
                            } else {
                                android.databinding.tool.store.ResourceBundle.BindingTargetBundle bindingTargetBundle = bundle.createBindingTarget(viewType.getKey(), null, false, null, null, null);
                                bindingTargetBundle.setIncludedLayout(includes.get(viewType.getKey()));
                                bindingTargetBundle.setInterfaceType(viewType.getValue());
                            }
                        } else {
                            android.databinding.tool.util.L.d("setting interface type on %s (%s) as %s", target.mId, target.getFullClassName(), viewType.getValue());
                            target.setInterfaceType(viewType.getValue());
                        }
                    }
                } catch (android.databinding.tool.processing.ScopedException ex) {
                    android.databinding.tool.processing.Scope.defer(ex);
                } finally {
                    android.databinding.tool.processing.Scope.exit();
                }
            }
        }
        // assign class names to each
        for (java.util.Map.Entry<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> entry : mLayoutBundles.entrySet()) {
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : entry.getValue()) {
                final java.lang.String configName;
                if (bundle.hasVariations()) {
                    // append configuration specifiers.
                    final java.lang.String parentFileName = bundle.mDirectory;
                    android.databinding.tool.util.L.d("parent file for %s is %s", bundle.getFileName(), parentFileName);
                    if ("layout".equals(parentFileName)) {
                        configName = "";
                    } else {
                        configName = android.databinding.tool.util.ParserHelper.toClassName(parentFileName.substring("layout-".length()));
                    }
                } else {
                    configName = "";
                }
                bundle.mConfigName = configName;
            }
        }
    }

    /**
     * Receives a list of bundles which are representations of the same layout file in different
     * configurations.
     *
     * @param bundles
     * 		
     * @return The map for variables and their types
     */
    private java.util.Map<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation> validateAndMergeNameTypeLocations(java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> bundles, java.lang.String errorMessage, android.databinding.tool.store.ResourceBundle.ValidateAndFilterCallback callback) {
        java.util.Map<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation> result = new java.util.HashMap<java.lang.String, android.databinding.tool.store.ResourceBundle.NameTypeLocation>();
        java.util.Set<java.lang.String> mismatched = new java.util.HashSet<java.lang.String>();
        for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles) {
            for (android.databinding.tool.store.ResourceBundle.NameTypeLocation item : callback.get(bundle)) {
                android.databinding.tool.store.ResourceBundle.NameTypeLocation existing = result.get(item.name);
                if ((existing != null) && (!existing.type.equals(item.type))) {
                    mismatched.add(item.name);
                    continue;
                }
                result.put(item.name, item);
            }
        }
        if (mismatched.isEmpty()) {
            return result;
        }
        // create exceptions. We could get more clever and find the outlier but for now, listing
        // each file w/ locations seems enough
        for (java.lang.String mismatch : mismatched) {
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles) {
                android.databinding.tool.store.ResourceBundle.NameTypeLocation found = null;
                for (android.databinding.tool.store.ResourceBundle.NameTypeLocation item : callback.get(bundle)) {
                    if (mismatch.equals(item.name)) {
                        found = item;
                        break;
                    }
                }
                if (found == null) {
                    // variable is not defined in this layout, continue
                    continue;
                }
                android.databinding.tool.processing.Scope.registerError(java.lang.String.format(errorMessage, found.name, found.type, (bundle.mDirectory + "/") + bundle.getFileName()), bundle, found.location.createScope());
            }
        }
        return result;
    }

    /**
     * Receives a list of bundles which are representations of the same layout file in different
     * configurations.
     *
     * @param bundles
     * 		
     * @return The shared class name for these bundles
     */
    private java.lang.String validateAndGetSharedClassName(java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> bundles) {
        java.lang.String sharedClassName = null;
        boolean hasMismatch = false;
        for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles) {
            bundle.mHasVariations = true;
            java.lang.String fullBindingClass = bundle.getFullBindingClass();
            if (sharedClassName == null) {
                sharedClassName = fullBindingClass;
            } else
                if (!sharedClassName.equals(fullBindingClass)) {
                    hasMismatch = true;
                    break;
                }

        }
        if (!hasMismatch) {
            return sharedClassName;
        }
        // generate proper exceptions for each
        for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle : bundles) {
            android.databinding.tool.processing.Scope.registerError(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_LAYOUT_CLASS_NAME_MISMATCH, bundle.getFullBindingClass(), (bundle.mDirectory + "/") + bundle.getFileName()), bundle, bundle.getClassNameLocationProvider());
        }
        return sharedClassName;
    }

    public void addRemovedFile(java.io.File file) {
        mRemovedFiles.add(file);
    }

    public java.util.List<java.io.File> getRemovedFiles() {
        return mRemovedFiles;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
    @javax.xml.bind.annotation.XmlRootElement(name = "Layout")
    public static class LayoutFileBundle implements android.databinding.tool.processing.scopes.FileScopeProvider , java.io.Serializable {
        @javax.xml.bind.annotation.XmlAttribute(name = "layout", required = true)
        public java.lang.String mFileName;

        @javax.xml.bind.annotation.XmlAttribute(name = "modulePackage", required = true)
        public java.lang.String mModulePackage;

        @javax.xml.bind.annotation.XmlAttribute(name = "absoluteFilePath", required = true)
        public java.lang.String mAbsoluteFilePath;

        private java.lang.String mConfigName;

        // The binding class as given by the user
        @javax.xml.bind.annotation.XmlAttribute(name = "bindingClass", required = false)
        public java.lang.String mBindingClass;

        // The location of the name of the generated class, optional
        @javax.xml.bind.annotation.XmlElement(name = "ClassNameLocation", required = false)
        private android.databinding.tool.store.Location mClassNameLocation;

        // The full package and class name as determined from mBindingClass and mModulePackage
        private java.lang.String mFullBindingClass;

        // The simple binding class name as determined from mBindingClass and mModulePackage
        private java.lang.String mBindingClassName;

        // The package of the binding class as determined from mBindingClass and mModulePackage
        private java.lang.String mBindingPackage;

        @javax.xml.bind.annotation.XmlAttribute(name = "directory", required = true)
        public java.lang.String mDirectory;

        public boolean mHasVariations;

        @javax.xml.bind.annotation.XmlElement(name = "Variables")
        public java.util.List<android.databinding.tool.store.ResourceBundle.VariableDeclaration> mVariables = new java.util.ArrayList<android.databinding.tool.store.ResourceBundle.VariableDeclaration>();

        @javax.xml.bind.annotation.XmlElement(name = "Imports")
        public java.util.List<android.databinding.tool.store.ResourceBundle.NameTypeLocation> mImports = new java.util.ArrayList<android.databinding.tool.store.ResourceBundle.NameTypeLocation>();

        @javax.xml.bind.annotation.XmlElementWrapper(name = "Targets")
        @javax.xml.bind.annotation.XmlElement(name = "Target")
        public java.util.List<android.databinding.tool.store.ResourceBundle.BindingTargetBundle> mBindingTargetBundles = new java.util.ArrayList<android.databinding.tool.store.ResourceBundle.BindingTargetBundle>();

        @javax.xml.bind.annotation.XmlAttribute(name = "isMerge", required = true)
        private boolean mIsMerge;

        private android.databinding.tool.processing.scopes.LocationScopeProvider mClassNameLocationProvider;

        // for XML binding
        public LayoutFileBundle() {
        }

        /**
         * Updates configuration fields from the given bundle but does not change variables,
         * binding expressions etc.
         */
        public void inheritConfigurationFrom(android.databinding.tool.store.ResourceBundle.LayoutFileBundle other) {
            mFileName = other.mFileName;
            mModulePackage = other.mModulePackage;
            mBindingClass = other.mBindingClass;
            mFullBindingClass = other.mFullBindingClass;
            mBindingClassName = other.mBindingClassName;
            mBindingPackage = other.mBindingPackage;
            mHasVariations = other.mHasVariations;
            mIsMerge = other.mIsMerge;
        }

        public LayoutFileBundle(java.io.File file, java.lang.String fileName, java.lang.String directory, java.lang.String modulePackage, boolean isMerge) {
            mFileName = fileName;
            mDirectory = directory;
            mModulePackage = modulePackage;
            mIsMerge = isMerge;
            mAbsoluteFilePath = file.getAbsolutePath();
        }

        public android.databinding.tool.processing.scopes.LocationScopeProvider getClassNameLocationProvider() {
            if (((mClassNameLocationProvider == null) && (mClassNameLocation != null)) && mClassNameLocation.isValid()) {
                mClassNameLocationProvider = mClassNameLocation.createScope();
            }
            return mClassNameLocationProvider;
        }

        public void addVariable(java.lang.String name, java.lang.String type, android.databinding.tool.store.Location location, boolean declared) {
            android.databinding.tool.util.Preconditions.check(!android.databinding.tool.store.ResourceBundle.NameTypeLocation.contains(mVariables, name), "Cannot use same variable name twice. %s in %s", name, location);
            mVariables.add(new android.databinding.tool.store.ResourceBundle.VariableDeclaration(name, type, location, declared));
        }

        public void addImport(java.lang.String alias, java.lang.String type, android.databinding.tool.store.Location location) {
            android.databinding.tool.util.Preconditions.check(!android.databinding.tool.store.ResourceBundle.NameTypeLocation.contains(mImports, alias), "Cannot import same alias twice. %s in %s", alias, location);
            mImports.add(new android.databinding.tool.store.ResourceBundle.NameTypeLocation(alias, type, location));
        }

        public android.databinding.tool.store.ResourceBundle.BindingTargetBundle createBindingTarget(java.lang.String id, java.lang.String viewName, boolean used, java.lang.String tag, java.lang.String originalTag, android.databinding.tool.store.Location location) {
            android.databinding.tool.store.ResourceBundle.BindingTargetBundle target = new android.databinding.tool.store.ResourceBundle.BindingTargetBundle(id, viewName, used, tag, originalTag, location);
            mBindingTargetBundles.add(target);
            return target;
        }

        public boolean isEmpty() {
            return (mVariables.isEmpty() && mImports.isEmpty()) && mBindingTargetBundles.isEmpty();
        }

        public android.databinding.tool.store.ResourceBundle.BindingTargetBundle getBindingTargetById(java.lang.String key) {
            for (android.databinding.tool.store.ResourceBundle.BindingTargetBundle target : mBindingTargetBundles) {
                if (key.equals(target.mId)) {
                    return target;
                }
            }
            return null;
        }

        public java.lang.String getFileName() {
            return mFileName;
        }

        public java.lang.String getConfigName() {
            return mConfigName;
        }

        public java.lang.String getDirectory() {
            return mDirectory;
        }

        public boolean hasVariations() {
            return mHasVariations;
        }

        public java.util.List<android.databinding.tool.store.ResourceBundle.VariableDeclaration> getVariables() {
            return mVariables;
        }

        public java.util.List<android.databinding.tool.store.ResourceBundle.NameTypeLocation> getImports() {
            return mImports;
        }

        public boolean isMerge() {
            return mIsMerge;
        }

        public java.lang.String getBindingClassName() {
            if (mBindingClassName == null) {
                java.lang.String fullClass = getFullBindingClass();
                int dotIndex = fullClass.lastIndexOf('.');
                mBindingClassName = fullClass.substring(dotIndex + 1);
            }
            return mBindingClassName;
        }

        public void setBindingClass(java.lang.String bindingClass, android.databinding.tool.store.Location location) {
            mBindingClass = bindingClass;
            mClassNameLocation = location;
        }

        public java.lang.String getBindingClassPackage() {
            if (mBindingPackage == null) {
                java.lang.String fullClass = getFullBindingClass();
                int dotIndex = fullClass.lastIndexOf('.');
                mBindingPackage = fullClass.substring(0, dotIndex);
            }
            return mBindingPackage;
        }

        private java.lang.String getFullBindingClass() {
            if (mFullBindingClass == null) {
                if (mBindingClass == null) {
                    mFullBindingClass = ((getModulePackage() + ".databinding.") + android.databinding.tool.util.ParserHelper.toClassName(getFileName())) + "Binding";
                } else
                    if (mBindingClass.startsWith(".")) {
                        mFullBindingClass = getModulePackage() + mBindingClass;
                    } else
                        if (mBindingClass.indexOf('.') < 0) {
                            mFullBindingClass = (getModulePackage() + ".databinding.") + mBindingClass;
                        } else {
                            mFullBindingClass = mBindingClass;
                        }


            }
            return mFullBindingClass;
        }

        public java.util.List<android.databinding.tool.store.ResourceBundle.BindingTargetBundle> getBindingTargetBundles() {
            return mBindingTargetBundles;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle = ((android.databinding.tool.store.ResourceBundle.LayoutFileBundle) (o));
            if (mConfigName != null ? !mConfigName.equals(bundle.mConfigName) : bundle.mConfigName != null) {
                return false;
            }
            if (mDirectory != null ? !mDirectory.equals(bundle.mDirectory) : bundle.mDirectory != null) {
                return false;
            }
            return !(mFileName != null ? !mFileName.equals(bundle.mFileName) : bundle.mFileName != null);
        }

        @java.lang.Override
        public int hashCode() {
            int result = (mFileName != null) ? mFileName.hashCode() : 0;
            result = (31 * result) + (mConfigName != null ? mConfigName.hashCode() : 0);
            result = (31 * result) + (mDirectory != null ? mDirectory.hashCode() : 0);
            return result;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((((("LayoutFileBundle{" + "mHasVariations=") + mHasVariations) + ", mDirectory='") + mDirectory) + '\'') + ", mConfigName='") + mConfigName) + '\'') + ", mModulePackage='") + mModulePackage) + '\'') + ", mFileName='") + mFileName) + '\'') + '}';
        }

        public java.lang.String getModulePackage() {
            return mModulePackage;
        }

        public java.lang.String getAbsoluteFilePath() {
            return mAbsoluteFilePath;
        }

        @java.lang.Override
        public java.lang.String provideScopeFilePath() {
            return mAbsoluteFilePath;
        }

        private static javax.xml.bind.Marshaller sMarshaller;

        private static javax.xml.bind.Unmarshaller sUmarshaller;

        public java.lang.String toXML() throws javax.xml.bind.JAXBException {
            java.io.StringWriter writer = new java.io.StringWriter();
            android.databinding.tool.store.ResourceBundle.LayoutFileBundle.getMarshaller().marshal(this, writer);
            return writer.getBuffer().toString();
        }

        public static android.databinding.tool.store.ResourceBundle.LayoutFileBundle fromXML(java.io.InputStream inputStream) throws javax.xml.bind.JAXBException {
            return ((android.databinding.tool.store.ResourceBundle.LayoutFileBundle) (android.databinding.tool.store.ResourceBundle.LayoutFileBundle.getUnmarshaller().unmarshal(inputStream)));
        }

        private static javax.xml.bind.Marshaller getMarshaller() throws javax.xml.bind.JAXBException {
            if (android.databinding.tool.store.ResourceBundle.LayoutFileBundle.sMarshaller == null) {
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(android.databinding.tool.store.ResourceBundle.LayoutFileBundle.class);
                android.databinding.tool.store.ResourceBundle.LayoutFileBundle.sMarshaller = context.createMarshaller();
            }
            return android.databinding.tool.store.ResourceBundle.LayoutFileBundle.sMarshaller;
        }

        private static javax.xml.bind.Unmarshaller getUnmarshaller() throws javax.xml.bind.JAXBException {
            if (android.databinding.tool.store.ResourceBundle.LayoutFileBundle.sUmarshaller == null) {
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(android.databinding.tool.store.ResourceBundle.LayoutFileBundle.class);
                android.databinding.tool.store.ResourceBundle.LayoutFileBundle.sUmarshaller = context.createUnmarshaller();
            }
            return android.databinding.tool.store.ResourceBundle.LayoutFileBundle.sUmarshaller;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
    public static class NameTypeLocation {
        @javax.xml.bind.annotation.XmlAttribute(name = "type", required = true)
        public java.lang.String type;

        @javax.xml.bind.annotation.XmlAttribute(name = "name", required = true)
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlElement(name = "location", required = false)
        public android.databinding.tool.store.Location location;

        public NameTypeLocation() {
        }

        public NameTypeLocation(java.lang.String name, java.lang.String type, android.databinding.tool.store.Location location) {
            this.type = type;
            this.name = name;
            this.location = location;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((("{" + "type='") + type) + '\'') + ", name='") + name) + '\'') + ", location=") + location) + '}';
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.databinding.tool.store.ResourceBundle.NameTypeLocation that = ((android.databinding.tool.store.ResourceBundle.NameTypeLocation) (o));
            if (location != null ? !location.equals(that.location) : that.location != null) {
                return false;
            }
            if (!name.equals(that.name)) {
                return false;
            }
            return type.equals(that.type);
        }

        @java.lang.Override
        public int hashCode() {
            int result = type.hashCode();
            result = (31 * result) + name.hashCode();
            result = (31 * result) + (location != null ? location.hashCode() : 0);
            return result;
        }

        public static boolean contains(java.util.List<? extends android.databinding.tool.store.ResourceBundle.NameTypeLocation> list, java.lang.String name) {
            for (android.databinding.tool.store.ResourceBundle.NameTypeLocation ntl : list) {
                if (name.equals(ntl.name)) {
                    return true;
                }
            }
            return false;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
    public static class VariableDeclaration extends android.databinding.tool.store.ResourceBundle.NameTypeLocation {
        @javax.xml.bind.annotation.XmlAttribute(name = "declared", required = false)
        public boolean declared;

        public VariableDeclaration() {
        }

        public VariableDeclaration(java.lang.String name, java.lang.String type, android.databinding.tool.store.Location location, boolean declared) {
            super(name, type, location);
            this.declared = declared;
        }
    }

    public static class MarshalledMapType {
        public java.util.List<android.databinding.tool.store.ResourceBundle.NameTypeLocation> entries;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
    public static class BindingTargetBundle implements android.databinding.tool.processing.scopes.LocationScopeProvider , java.io.Serializable {
        // public for XML serialization
        @javax.xml.bind.annotation.XmlAttribute(name = "id")
        public java.lang.String mId;

        @javax.xml.bind.annotation.XmlAttribute(name = "tag", required = true)
        public java.lang.String mTag;

        @javax.xml.bind.annotation.XmlAttribute(name = "originalTag")
        public java.lang.String mOriginalTag;

        @javax.xml.bind.annotation.XmlAttribute(name = "view", required = false)
        public java.lang.String mViewName;

        private java.lang.String mFullClassName;

        public boolean mUsed = true;

        @javax.xml.bind.annotation.XmlElementWrapper(name = "Expressions")
        @javax.xml.bind.annotation.XmlElement(name = "Expression")
        public java.util.List<android.databinding.tool.store.ResourceBundle.BindingTargetBundle.BindingBundle> mBindingBundleList = new java.util.ArrayList<android.databinding.tool.store.ResourceBundle.BindingTargetBundle.BindingBundle>();

        @javax.xml.bind.annotation.XmlAttribute(name = "include")
        public java.lang.String mIncludedLayout;

        @javax.xml.bind.annotation.XmlElement(name = "location")
        public android.databinding.tool.store.Location mLocation;

        private java.lang.String mInterfaceType;

        // For XML serialization
        public BindingTargetBundle() {
        }

        public BindingTargetBundle(java.lang.String id, java.lang.String viewName, boolean used, java.lang.String tag, java.lang.String originalTag, android.databinding.tool.store.Location location) {
            mId = id;
            mViewName = viewName;
            mUsed = used;
            mTag = tag;
            mOriginalTag = originalTag;
            mLocation = location;
        }

        public void addBinding(java.lang.String name, java.lang.String expr, boolean isTwoWay, android.databinding.tool.store.Location location, android.databinding.tool.store.Location valueLocation) {
            mBindingBundleList.add(new android.databinding.tool.store.ResourceBundle.BindingTargetBundle.BindingBundle(name, expr, isTwoWay, location, valueLocation));
        }

        public void setIncludedLayout(java.lang.String includedLayout) {
            mIncludedLayout = includedLayout;
        }

        public java.lang.String getIncludedLayout() {
            return mIncludedLayout;
        }

        public boolean isBinder() {
            return mIncludedLayout != null;
        }

        public void setInterfaceType(java.lang.String interfaceType) {
            mInterfaceType = interfaceType;
        }

        public void setLocation(android.databinding.tool.store.Location location) {
            mLocation = location;
        }

        public android.databinding.tool.store.Location getLocation() {
            return mLocation;
        }

        public java.lang.String getId() {
            return mId;
        }

        public java.lang.String getTag() {
            return mTag;
        }

        public java.lang.String getOriginalTag() {
            return mOriginalTag;
        }

        public java.lang.String getFullClassName() {
            if (mFullClassName == null) {
                if (isBinder()) {
                    mFullClassName = mInterfaceType;
                } else
                    if (mViewName.indexOf('.') == (-1)) {
                        if (java.util.Arrays.asList(android.databinding.tool.store.ResourceBundle.ANDROID_VIEW_PACKAGE_VIEWS).contains(mViewName)) {
                            mFullClassName = "android.view." + mViewName;
                        } else
                            if ("WebView".equals(mViewName)) {
                                mFullClassName = "android.webkit." + mViewName;
                            } else {
                                mFullClassName = "android.widget." + mViewName;
                            }

                    } else {
                        mFullClassName = mViewName;
                    }

            }
            if (mFullClassName == null) {
                android.databinding.tool.util.L.e("Unexpected full class name = null. view = %s, interface = %s, layout = %s", mViewName, mInterfaceType, mIncludedLayout);
            }
            return mFullClassName;
        }

        public boolean isUsed() {
            return mUsed;
        }

        public java.util.List<android.databinding.tool.store.ResourceBundle.BindingTargetBundle.BindingBundle> getBindingBundleList() {
            return mBindingBundleList;
        }

        public java.lang.String getInterfaceType() {
            return mInterfaceType;
        }

        @java.lang.Override
        public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
            return mLocation == null ? null : java.util.Arrays.asList(mLocation);
        }

        @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
        public static class BindingBundle implements java.io.Serializable {
            private java.lang.String mName;

            private java.lang.String mExpr;

            private android.databinding.tool.store.Location mLocation;

            private android.databinding.tool.store.Location mValueLocation;

            private boolean mIsTwoWay;

            public BindingBundle() {
            }

            public BindingBundle(java.lang.String name, java.lang.String expr, boolean isTwoWay, android.databinding.tool.store.Location location, android.databinding.tool.store.Location valueLocation) {
                mName = name;
                mExpr = expr;
                mLocation = location;
                mIsTwoWay = isTwoWay;
                mValueLocation = valueLocation;
            }

            @javax.xml.bind.annotation.XmlAttribute(name = "attribute", required = true)
            public java.lang.String getName() {
                return mName;
            }

            @javax.xml.bind.annotation.XmlAttribute(name = "text", required = true)
            public java.lang.String getExpr() {
                return mExpr;
            }

            public void setName(java.lang.String name) {
                mName = name;
            }

            public void setExpr(java.lang.String expr) {
                mExpr = expr;
            }

            public void setTwoWay(boolean isTwoWay) {
                mIsTwoWay = isTwoWay;
            }

            @javax.xml.bind.annotation.XmlElement(name = "Location")
            public android.databinding.tool.store.Location getLocation() {
                return mLocation;
            }

            public void setLocation(android.databinding.tool.store.Location location) {
                mLocation = location;
            }

            @javax.xml.bind.annotation.XmlElement(name = "ValueLocation")
            public android.databinding.tool.store.Location getValueLocation() {
                return mValueLocation;
            }

            @javax.xml.bind.annotation.XmlElement(name = "TwoWay")
            public boolean isTwoWay() {
                return mIsTwoWay;
            }

            public void setValueLocation(android.databinding.tool.store.Location valueLocation) {
                mValueLocation = valueLocation;
            }
        }
    }

    /**
     * Just an inner callback class to process imports and variables w/ the same code.
     */
    private interface ValidateAndFilterCallback {
        java.util.List<? extends android.databinding.tool.store.ResourceBundle.NameTypeLocation> get(android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle);
    }
}

