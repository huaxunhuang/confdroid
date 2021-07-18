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
package android.databinding.tool;


/**
 * Processes the layout XML, stripping the binding attributes and elements
 * and writes the information into an annotated class file for the annotation
 * processor to work with.
 */
public class LayoutXmlProcessor {
    // hardcoded in baseAdapters
    public static final java.lang.String RESOURCE_BUNDLE_PACKAGE = "android.databinding.layouts";

    public static final java.lang.String CLASS_NAME = "DataBindingInfo";

    private final android.databinding.tool.writer.JavaFileWriter mFileWriter;

    private final android.databinding.tool.store.ResourceBundle mResourceBundle;

    private final int mMinSdk;

    private boolean mProcessingComplete;

    private boolean mWritten;

    private final boolean mIsLibrary;

    private final java.lang.String mBuildId = java.util.UUID.randomUUID().toString();

    private final android.databinding.tool.LayoutXmlProcessor.OriginalFileLookup mOriginalFileLookup;

    public LayoutXmlProcessor(java.lang.String applicationPackage, android.databinding.tool.writer.JavaFileWriter fileWriter, int minSdk, boolean isLibrary, android.databinding.tool.LayoutXmlProcessor.OriginalFileLookup originalFileLookup) {
        mFileWriter = fileWriter;
        mResourceBundle = new android.databinding.tool.store.ResourceBundle(applicationPackage);
        mMinSdk = minSdk;
        mIsLibrary = isLibrary;
        mOriginalFileLookup = originalFileLookup;
    }

    private static void processIncrementalInputFiles(android.databinding.tool.LayoutXmlProcessor.ResourceInput input, android.databinding.tool.LayoutXmlProcessor.ProcessFileCallback callback) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
        android.databinding.tool.LayoutXmlProcessor.processExistingIncrementalFiles(input.getRootInputFolder(), input.getAdded(), callback);
        android.databinding.tool.LayoutXmlProcessor.processExistingIncrementalFiles(input.getRootInputFolder(), input.getChanged(), callback);
        android.databinding.tool.LayoutXmlProcessor.processRemovedIncrementalFiles(input.getRootInputFolder(), input.getRemoved(), callback);
    }

    private static void processExistingIncrementalFiles(java.io.File inputRoot, java.util.List<java.io.File> files, android.databinding.tool.LayoutXmlProcessor.ProcessFileCallback callback) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
        for (java.io.File file : files) {
            java.io.File parent = file.getParentFile();
            if (inputRoot.equals(parent)) {
                callback.processOtherRootFile(file);
            } else
                if (android.databinding.tool.LayoutXmlProcessor.layoutFolderFilter.accept(parent, parent.getName())) {
                    callback.processLayoutFile(file);
                } else {
                    callback.processOtherFile(parent, file);
                }

        }
    }

    private static void processRemovedIncrementalFiles(java.io.File inputRoot, java.util.List<java.io.File> files, android.databinding.tool.LayoutXmlProcessor.ProcessFileCallback callback) throws java.io.IOException {
        for (java.io.File file : files) {
            java.io.File parent = file.getParentFile();
            if (inputRoot.equals(parent)) {
                callback.processRemovedOtherRootFile(file);
            } else
                if (android.databinding.tool.LayoutXmlProcessor.layoutFolderFilter.accept(parent, parent.getName())) {
                    callback.processRemovedLayoutFile(file);
                } else {
                    callback.processRemovedOtherFile(parent, file);
                }

        }
    }

    private static void processAllInputFiles(android.databinding.tool.LayoutXmlProcessor.ResourceInput input, android.databinding.tool.LayoutXmlProcessor.ProcessFileCallback callback) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
        org.apache.commons.io.FileUtils.deleteDirectory(input.getRootOutputFolder());
        android.databinding.tool.util.Preconditions.check(input.getRootOutputFolder().mkdirs(), "out dir should be re-created");
        android.databinding.tool.util.Preconditions.check(input.getRootInputFolder().isDirectory(), "it must be a directory");
        for (java.io.File firstLevel : input.getRootInputFolder().listFiles()) {
            if (firstLevel.isDirectory()) {
                if (android.databinding.tool.LayoutXmlProcessor.layoutFolderFilter.accept(firstLevel, firstLevel.getName())) {
                    callback.processLayoutFolder(firstLevel);
                    for (java.io.File xmlFile : firstLevel.listFiles(android.databinding.tool.LayoutXmlProcessor.xmlFileFilter)) {
                        callback.processLayoutFile(xmlFile);
                    }
                } else {
                    callback.processOtherFolder(firstLevel);
                    for (java.io.File file : firstLevel.listFiles()) {
                        callback.processOtherFile(firstLevel, file);
                    }
                }
            } else {
                callback.processOtherRootFile(firstLevel);
            }
        }
    }

    /**
     * used by the studio plugin
     */
    public android.databinding.tool.store.ResourceBundle getResourceBundle() {
        return mResourceBundle;
    }

    public boolean processResources(final android.databinding.tool.LayoutXmlProcessor.ResourceInput input) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
        if (mProcessingComplete) {
            return false;
        }
        final android.databinding.tool.store.LayoutFileParser layoutFileParser = new android.databinding.tool.store.LayoutFileParser();
        final java.net.URI inputRootUri = input.getRootInputFolder().toURI();
        android.databinding.tool.LayoutXmlProcessor.ProcessFileCallback callback = new android.databinding.tool.LayoutXmlProcessor.ProcessFileCallback() {
            private java.io.File convertToOutFile(java.io.File file) {
                final java.lang.String subPath = android.databinding.tool.LayoutXmlProcessor.toSystemDependentPath(inputRootUri.relativize(file.toURI()).getPath());
                return new java.io.File(input.getRootOutputFolder(), subPath);
            }

            @java.lang.Override
            public void processLayoutFile(java.io.File file) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
                final java.io.File output = convertToOutFile(file);
                final android.databinding.tool.store.ResourceBundle.LayoutFileBundle bindingLayout = layoutFileParser.parseXml(file, output, mResourceBundle.getAppPackage(), mOriginalFileLookup);
                if ((bindingLayout != null) && (!bindingLayout.isEmpty())) {
                    mResourceBundle.addLayoutBundle(bindingLayout);
                }
            }

            @java.lang.Override
            public void processOtherFile(java.io.File parentFolder, java.io.File file) throws java.io.IOException {
                final java.io.File outParent = convertToOutFile(parentFolder);
                org.apache.commons.io.FileUtils.copyFile(file, new java.io.File(outParent, file.getName()));
            }

            @java.lang.Override
            public void processRemovedLayoutFile(java.io.File file) {
                mResourceBundle.addRemovedFile(file);
                final java.io.File out = convertToOutFile(file);
                org.apache.commons.io.FileUtils.deleteQuietly(out);
            }

            @java.lang.Override
            public void processRemovedOtherFile(java.io.File parentFolder, java.io.File file) throws java.io.IOException {
                final java.io.File outParent = convertToOutFile(parentFolder);
                org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(outParent, file.getName()));
            }

            @java.lang.Override
            public void processOtherFolder(java.io.File folder) {
                // noinspection ResultOfMethodCallIgnored
                convertToOutFile(folder).mkdirs();
            }

            @java.lang.Override
            public void processLayoutFolder(java.io.File folder) {
                // noinspection ResultOfMethodCallIgnored
                convertToOutFile(folder).mkdirs();
            }

            @java.lang.Override
            public void processOtherRootFile(java.io.File file) throws java.io.IOException {
                final java.io.File outFile = convertToOutFile(file);
                if (file.isDirectory()) {
                    org.apache.commons.io.FileUtils.copyDirectory(file, outFile);
                } else {
                    org.apache.commons.io.FileUtils.copyFile(file, outFile);
                }
            }

            @java.lang.Override
            public void processRemovedOtherRootFile(java.io.File file) throws java.io.IOException {
                final java.io.File outFile = convertToOutFile(file);
                org.apache.commons.io.FileUtils.deleteQuietly(outFile);
            }
        };
        if (input.isIncremental()) {
            android.databinding.tool.LayoutXmlProcessor.processIncrementalInputFiles(input, callback);
        } else {
            android.databinding.tool.LayoutXmlProcessor.processAllInputFiles(input, callback);
        }
        mProcessingComplete = true;
        return true;
    }

    public static java.lang.String toSystemDependentPath(java.lang.String path) {
        if (java.io.File.separatorChar != '/') {
            path = path.replace('/', java.io.File.separatorChar);
        }
        return path;
    }

    public void writeLayoutInfoFiles(java.io.File xmlOutDir) throws javax.xml.bind.JAXBException {
        if (mWritten) {
            return;
        }
        for (java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle> layouts : mResourceBundle.getLayoutBundles().values()) {
            for (android.databinding.tool.store.ResourceBundle.LayoutFileBundle layout : layouts) {
                writeXmlFile(xmlOutDir, layout);
            }
        }
        for (java.io.File file : mResourceBundle.getRemovedFiles()) {
            java.lang.String exportFileName = android.databinding.tool.LayoutXmlProcessor.generateExportFileName(file);
            org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(xmlOutDir, exportFileName));
        }
        mWritten = true;
    }

    private void writeXmlFile(java.io.File xmlOutDir, android.databinding.tool.store.ResourceBundle.LayoutFileBundle layout) throws javax.xml.bind.JAXBException {
        java.lang.String filename = android.databinding.tool.LayoutXmlProcessor.generateExportFileName(layout);
        mFileWriter.writeToFile(new java.io.File(xmlOutDir, filename), layout.toXML());
    }

    public java.lang.String getInfoClassFullName() {
        return (android.databinding.tool.LayoutXmlProcessor.RESOURCE_BUNDLE_PACKAGE + ".") + android.databinding.tool.LayoutXmlProcessor.CLASS_NAME;
    }

    /**
     * Generates a string identifier that can uniquely identify the given layout bundle.
     * This identifier can be used when we need to export data about this layout bundle.
     */
    private static java.lang.String generateExportFileName(android.databinding.tool.store.ResourceBundle.LayoutFileBundle layout) {
        return android.databinding.tool.LayoutXmlProcessor.generateExportFileName(layout.getFileName(), layout.getDirectory());
    }

    private static java.lang.String generateExportFileName(java.io.File file) {
        final java.lang.String fileName = file.getName();
        return android.databinding.tool.LayoutXmlProcessor.generateExportFileName(fileName.substring(0, fileName.lastIndexOf('.')), file.getParentFile().getName());
    }

    public static java.lang.String generateExportFileName(java.lang.String fileName, java.lang.String dirName) {
        return ((fileName + '-') + dirName) + ".xml";
    }

    public static java.lang.String exportLayoutNameFromInfoFileName(java.lang.String infoFileName) {
        return infoFileName.substring(0, infoFileName.indexOf('-'));
    }

    public void writeInfoClass(/* Nullable */
    java.io.File sdkDir, java.io.File xmlOutDir, /* Nullable */
    java.io.File exportClassListTo) {
        writeInfoClass(sdkDir, xmlOutDir, exportClassListTo, false, false);
    }

    public java.lang.String getPackage() {
        return mResourceBundle.getAppPackage();
    }

    public void writeInfoClass(/* Nullable */
    java.io.File sdkDir, java.io.File xmlOutDir, java.io.File exportClassListTo, boolean enableDebugLogs, boolean printEncodedErrorLogs) {
        com.google.common.escape.Escaper javaEscaper = android.databinding.tool.util.SourceCodeEscapers.javaCharEscaper();
        final java.lang.String sdkPath = (sdkDir == null) ? null : javaEscaper.escape(sdkDir.getAbsolutePath());
        final java.lang.Class annotation = android.databinding.BindingBuildInfo.class;
        final java.lang.String layoutInfoPath = javaEscaper.escape(xmlOutDir.getAbsolutePath());
        final java.lang.String exportClassListToPath = (exportClassListTo == null) ? "" : javaEscaper.escape(exportClassListTo.getAbsolutePath());
        java.lang.String classString = ((((((((((((((((((((((((((((((((((((("package " + android.databinding.tool.LayoutXmlProcessor.RESOURCE_BUNDLE_PACKAGE) + ";\n\n") + "import ") + annotation.getCanonicalName()) + ";\n\n") + "@") + annotation.getSimpleName()) + "(buildId=\"") + mBuildId) + "\", ") + "modulePackage=\"") + mResourceBundle.getAppPackage()) + "\", ") + "sdkRoot=") + "\"") + (sdkPath == null ? "" : sdkPath)) + "\",") + "layoutInfoDir=\"") + layoutInfoPath) + "\",") + "exportClassListTo=\"") + exportClassListToPath) + "\",") + "isLibrary=") + mIsLibrary) + ",") + "minSdk=") + mMinSdk) + ",") + "enableDebugLogs=") + enableDebugLogs) + ",") + "printEncodedError=") + printEncodedErrorLogs) + ")\n") + "public class ") + android.databinding.tool.LayoutXmlProcessor.CLASS_NAME) + " {}\n";
        mFileWriter.writeToFile((android.databinding.tool.LayoutXmlProcessor.RESOURCE_BUNDLE_PACKAGE + ".") + android.databinding.tool.LayoutXmlProcessor.CLASS_NAME, classString);
    }

    private static final java.io.FilenameFilter layoutFolderFilter = new java.io.FilenameFilter() {
        @java.lang.Override
        public boolean accept(java.io.File dir, java.lang.String name) {
            return name.startsWith("layout");
        }
    };

    private static final java.io.FilenameFilter xmlFileFilter = new java.io.FilenameFilter() {
        @java.lang.Override
        public boolean accept(java.io.File dir, java.lang.String name) {
            return name.toLowerCase().endsWith(".xml");
        }
    };

    /**
     * Helper interface that can find the original copy of a resource XML.
     */
    public interface OriginalFileLookup {
        /**
         *
         *
         * @param file
         * 		The intermediate build file
         * @return The original file or null if original File cannot be found.
         */
        java.io.File getOriginalFileFor(java.io.File file);
    }

    /**
     * API agnostic class to get resource changes incrementally.
     */
    public static class ResourceInput {
        private final boolean mIncremental;

        private final java.io.File mRootInputFolder;

        private final java.io.File mRootOutputFolder;

        private java.util.List<java.io.File> mAdded = new java.util.ArrayList<java.io.File>();

        private java.util.List<java.io.File> mRemoved = new java.util.ArrayList<java.io.File>();

        private java.util.List<java.io.File> mChanged = new java.util.ArrayList<java.io.File>();

        public ResourceInput(boolean incremental, java.io.File rootInputFolder, java.io.File rootOutputFolder) {
            mIncremental = incremental;
            mRootInputFolder = rootInputFolder;
            mRootOutputFolder = rootOutputFolder;
        }

        public void added(java.io.File file) {
            mAdded.add(file);
        }

        public void removed(java.io.File file) {
            mRemoved.add(file);
        }

        public void changed(java.io.File file) {
            mChanged.add(file);
        }

        public boolean shouldCopy() {
            return !mRootInputFolder.equals(mRootOutputFolder);
        }

        java.util.List<java.io.File> getAdded() {
            return mAdded;
        }

        java.util.List<java.io.File> getRemoved() {
            return mRemoved;
        }

        java.util.List<java.io.File> getChanged() {
            return mChanged;
        }

        java.io.File getRootInputFolder() {
            return mRootInputFolder;
        }

        java.io.File getRootOutputFolder() {
            return mRootOutputFolder;
        }

        public boolean isIncremental() {
            return mIncremental;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder out = new java.lang.StringBuilder();
            out.append("ResourceInput{").append("mIncremental=").append(mIncremental).append(", mRootInputFolder=").append(mRootInputFolder).append(", mRootOutputFolder=").append(mRootOutputFolder);
            android.databinding.tool.LayoutXmlProcessor.ResourceInput.logFiles(out, "added", mAdded);
            android.databinding.tool.LayoutXmlProcessor.ResourceInput.logFiles(out, "removed", mRemoved);
            android.databinding.tool.LayoutXmlProcessor.ResourceInput.logFiles(out, "changed", mChanged);
            return out.toString();
        }

        private static void logFiles(java.lang.StringBuilder out, java.lang.String name, java.util.List<java.io.File> files) {
            out.append("\n  ").append(name);
            for (java.io.File file : files) {
                out.append("\n   - ").append(file.getAbsolutePath());
            }
        }
    }

    private interface ProcessFileCallback {
        void processLayoutFile(java.io.File file) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException;

        void processOtherFile(java.io.File parentFolder, java.io.File file) throws java.io.IOException;

        void processRemovedLayoutFile(java.io.File file);

        void processRemovedOtherFile(java.io.File parentFolder, java.io.File file) throws java.io.IOException;

        void processOtherFolder(java.io.File folder);

        void processLayoutFolder(java.io.File folder);

        void processOtherRootFile(java.io.File file) throws java.io.IOException;

        void processRemovedOtherRootFile(java.io.File file) throws java.io.IOException;
    }
}

