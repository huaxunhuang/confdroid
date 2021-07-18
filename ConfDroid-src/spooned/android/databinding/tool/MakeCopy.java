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
 * This class is used by make to copy resources to an intermediate directory and start processing
 * them. When aapt takes over, this can be easily extracted to a short script.
 */
public class MakeCopy {
    private static final int MANIFEST_INDEX = 0;

    private static final int SRC_INDEX = 1;

    private static final int XML_INDEX = 2;

    private static final int RES_OUT_INDEX = 3;

    private static final int RES_IN_INDEX = 4;

    private static final java.lang.String APP_SUBPATH = android.databinding.tool.LayoutXmlProcessor.RESOURCE_BUNDLE_PACKAGE.replace('.', java.io.File.separatorChar);

    private static final java.io.FilenameFilter LAYOUT_DIR_FILTER = new java.io.FilenameFilter() {
        @java.lang.Override
        public boolean accept(java.io.File dir, java.lang.String name) {
            return name.toLowerCase().startsWith("layout");
        }
    };

    private static final java.io.FilenameFilter XML_FILENAME_FILTER = new java.io.FilenameFilter() {
        @java.lang.Override
        public boolean accept(java.io.File dir, java.lang.String name) {
            return name.toLowerCase().endsWith(".xml");
        }
    };

    public static void main(java.lang.String[] args) {
        if (args.length < 5) {
            java.lang.System.out.println("required parameters: [-l] manifest adk-dir src-out-dir xml-out-dir " + "res-out-dir res-in-dir...");
            java.lang.System.out.println("Creates an android data binding class and copies resources from");
            java.lang.System.out.println("res-source to res-target and modifies binding layout files");
            java.lang.System.out.println("in res-target. Binding data is extracted into XML files");
            java.lang.System.out.println("and placed in xml-out-dir.");
            java.lang.System.out.println("  -l          indicates that this is a library");
            java.lang.System.out.println("  manifest    path to AndroidManifest.xml file");
            java.lang.System.out.println("  src-out-dir path to where generated source goes");
            java.lang.System.out.println("  xml-out-dir path to where generated binding XML goes");
            java.lang.System.out.println("  res-out-dir path to the where modified resources should go");
            java.lang.System.out.println("  res-in-dir  path to source resources \"res\" directory. One" + " or more are allowed.");
            java.lang.System.exit(1);
        }
        final boolean isLibrary = args[0].equals("-l");
        final int indexOffset = (isLibrary) ? 1 : 0;
        final java.lang.String applicationPackage;
        final int minSdk;
        final org.w3c.dom.Document androidManifest = android.databinding.tool.MakeCopy.readAndroidManifest(new java.io.File(args[android.databinding.tool.MakeCopy.MANIFEST_INDEX + indexOffset]));
        try {
            final javax.xml.xpath.XPathFactory xPathFactory = javax.xml.xpath.XPathFactory.newInstance();
            final javax.xml.xpath.XPath xPath = xPathFactory.newXPath();
            applicationPackage = xPath.evaluate("string(/manifest/@package)", androidManifest);
            final java.lang.Double minSdkNumber = ((java.lang.Double) (xPath.evaluate("number(/manifest/uses-sdk/@android:minSdkVersion)", androidManifest, javax.xml.xpath.XPathConstants.NUMBER)));
            minSdk = (minSdkNumber == null) ? 1 : minSdkNumber.intValue();
        } catch (javax.xml.xpath.XPathExpressionException e) {
            e.printStackTrace();
            java.lang.System.exit(6);
            return;
        }
        final java.io.File srcDir = new java.io.File(args[android.databinding.tool.MakeCopy.SRC_INDEX + indexOffset], android.databinding.tool.MakeCopy.APP_SUBPATH);
        if (!android.databinding.tool.MakeCopy.makeTargetDir(srcDir)) {
            java.lang.System.err.println("Could not create source directory " + srcDir);
            java.lang.System.exit(2);
        }
        final java.io.File resTarget = new java.io.File(args[android.databinding.tool.MakeCopy.RES_OUT_INDEX + indexOffset]);
        if (!android.databinding.tool.MakeCopy.makeTargetDir(resTarget)) {
            java.lang.System.err.println("Could not create resource directory: " + resTarget);
            java.lang.System.exit(4);
        }
        final java.io.File xmlDir = new java.io.File(args[android.databinding.tool.MakeCopy.XML_INDEX + indexOffset]);
        if (!android.databinding.tool.MakeCopy.makeTargetDir(xmlDir)) {
            java.lang.System.err.println("Could not create xml output directory: " + xmlDir);
            java.lang.System.exit(5);
        }
        java.lang.System.out.println("Application Package: " + applicationPackage);
        java.lang.System.out.println("Minimum SDK: " + minSdk);
        java.lang.System.out.println("Target Resources: " + resTarget.getAbsolutePath());
        java.lang.System.out.println("Target Source Dir: " + srcDir.getAbsolutePath());
        java.lang.System.out.println("Target XML Dir: " + xmlDir.getAbsolutePath());
        java.lang.System.out.println("Library? " + isLibrary);
        boolean foundSomeResources = false;
        for (int i = android.databinding.tool.MakeCopy.RES_IN_INDEX + indexOffset; i < args.length; i++) {
            final java.io.File resDir = new java.io.File(args[i]);
            if (!resDir.exists()) {
                java.lang.System.out.println("Could not find resource directory: " + resDir);
            } else {
                java.lang.System.out.println("Source Resources: " + resDir.getAbsolutePath());
                try {
                    org.apache.commons.io.FileUtils.copyDirectory(resDir, resTarget);
                    android.databinding.tool.MakeCopy.addFromFile(resDir, resTarget);
                    foundSomeResources = true;
                } catch (java.io.IOException e) {
                    java.lang.System.err.println((((("Could not copy resources from " + resDir) + " to ") + resTarget) + ": ") + e.getLocalizedMessage());
                    java.lang.System.exit(3);
                }
            }
        }
        if (!foundSomeResources) {
            java.lang.System.err.println("No resource directories were found.");
            java.lang.System.exit(7);
        }
        android.databinding.tool.MakeCopy.processLayoutFiles(applicationPackage, resTarget, srcDir, xmlDir, minSdk, isLibrary);
    }

    private static org.w3c.dom.Document readAndroidManifest(java.io.File manifest) {
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            return documentBuilder.parse(manifest);
        } catch (java.lang.Exception e) {
            java.lang.System.err.println((("Could not load Android Manifest from " + manifest.getAbsolutePath()) + ": ") + e.getLocalizedMessage());
            java.lang.System.exit(8);
            return null;
        }
    }

    private static void processLayoutFiles(java.lang.String applicationPackage, java.io.File resTarget, java.io.File srcDir, java.io.File xmlDir, int minSdk, boolean isLibrary) {
        android.databinding.tool.MakeCopy.MakeFileWriter makeFileWriter = new android.databinding.tool.MakeCopy.MakeFileWriter(srcDir);
        android.databinding.tool.LayoutXmlProcessor xmlProcessor = new android.databinding.tool.LayoutXmlProcessor(applicationPackage, makeFileWriter, minSdk, isLibrary, new android.databinding.tool.LayoutXmlProcessor.OriginalFileLookup() {
            @java.lang.Override
            public java.io.File getOriginalFileFor(java.io.File file) {
                return file;
            }
        });
        try {
            android.databinding.tool.LayoutXmlProcessor.ResourceInput input = new android.databinding.tool.LayoutXmlProcessor.ResourceInput(false, resTarget, resTarget);
            xmlProcessor.processResources(input);
            xmlProcessor.writeLayoutInfoFiles(xmlDir);
            // TODO Looks like make does not support excluding from libs ?
            xmlProcessor.writeInfoClass(null, xmlDir, null);
            java.util.Map<java.lang.String, java.util.List<android.databinding.tool.store.ResourceBundle.LayoutFileBundle>> bundles = xmlProcessor.getResourceBundle().getLayoutBundles();
            if (isLibrary) {
                for (java.lang.String name : bundles.keySet()) {
                    android.databinding.tool.store.ResourceBundle.LayoutFileBundle layoutFileBundle = bundles.get(name).get(0);
                    java.lang.String pkgName = layoutFileBundle.getBindingClassPackage().replace('.', '/');
                    java.lang.System.err.println(((pkgName + '/') + layoutFileBundle.getBindingClassName()) + ".class");
                }
            }
            if (makeFileWriter.getErrorCount() > 0) {
                java.lang.System.exit(9);
            }
        } catch (java.lang.Exception e) {
            java.lang.System.err.println("Error processing layout files: " + e.getLocalizedMessage());
            java.lang.System.exit(10);
        }
    }

    private static void addFromFile(java.io.File resDir, java.io.File resTarget) {
        for (java.io.File layoutDir : resDir.listFiles(android.databinding.tool.MakeCopy.LAYOUT_DIR_FILTER)) {
            if (layoutDir.isDirectory()) {
                java.io.File targetDir = new java.io.File(resTarget, layoutDir.getName());
                for (java.io.File layoutFile : layoutDir.listFiles(android.databinding.tool.MakeCopy.XML_FILENAME_FILTER)) {
                    java.io.File targetFile = new java.io.File(targetDir, layoutFile.getName());
                    java.io.FileWriter appender = null;
                    try {
                        appender = new java.io.FileWriter(targetFile, true);
                        appender.write(("<!-- From: " + layoutFile.toURI().toString()) + " -->\n");
                    } catch (java.io.IOException e) {
                        java.lang.System.err.println((("Could not update " + layoutFile) + ": ") + e.getLocalizedMessage());
                    } finally {
                        org.apache.commons.io.IOUtils.closeQuietly(appender);
                    }
                }
            }
        }
    }

    private static boolean makeTargetDir(java.io.File dir) {
        if (dir.exists()) {
            return dir.isDirectory();
        }
        return dir.mkdirs();
    }

    private static class MakeFileWriter extends android.databinding.tool.writer.JavaFileWriter {
        private final java.io.File mSourceRoot;

        private int mErrorCount;

        public MakeFileWriter(java.io.File sourceRoot) {
            mSourceRoot = sourceRoot;
        }

        @java.lang.Override
        public void writeToFile(java.lang.String canonicalName, java.lang.String contents) {
            java.lang.String fileName = canonicalName.replace('.', java.io.File.separatorChar) + ".java";
            java.io.File sourceFile = new java.io.File(mSourceRoot, fileName);
            java.io.FileWriter writer = null;
            try {
                sourceFile.getParentFile().mkdirs();
                writer = new java.io.FileWriter(sourceFile);
                writer.write(contents);
            } catch (java.io.IOException e) {
                java.lang.System.err.println((("Could not write to " + sourceFile) + ": ") + e.getLocalizedMessage());
                mErrorCount++;
            } finally {
                org.apache.commons.io.IOUtils.closeQuietly(writer);
            }
        }

        public int getErrorCount() {
            return mErrorCount;
        }
    }
}

