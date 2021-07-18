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
 * This class is used by Android Gradle plugin.
 */
@java.lang.SuppressWarnings("unused")
public class DataBindingBuilder {
    android.databinding.tool.DataBindingBuilder.Versions mVersions;

    private static final java.lang.String EXCLUDE_PATTERN = "android/databinding/layouts/*.*";

    public java.lang.String getCompilerVersion() {
        return getVersions().compiler;
    }

    public java.lang.String getBaseLibraryVersion(java.lang.String compilerVersion) {
        return getVersions().baseLibrary;
    }

    public java.lang.String getLibraryVersion(java.lang.String compilerVersion) {
        return getVersions().extensions;
    }

    public java.lang.String getBaseAdaptersVersion(java.lang.String compilerVersion) {
        return getVersions().extensions;
    }

    public void setPrintMachineReadableOutput(boolean machineReadableOutput) {
        android.databinding.tool.processing.ScopedException.encodeOutput(machineReadableOutput);
    }

    public boolean getPrintMachineReadableOutput() {
        return android.databinding.tool.processing.ScopedException.issEncodeOutput();
    }

    public void setDebugLogEnabled(boolean enableDebugLogs) {
        android.databinding.tool.util.L.setDebugLog(enableDebugLogs);
    }

    public boolean isDebugLogEnabled() {
        return android.databinding.tool.util.L.isDebugEnabled();
    }

    private android.databinding.tool.DataBindingBuilder.Versions getVersions() {
        if (mVersions != null) {
            return mVersions;
        }
        try {
            java.util.Properties props = new java.util.Properties();
            java.io.InputStream stream = getClass().getResourceAsStream("/data_binding_version_info.properties");
            try {
                props.load(stream);
                mVersions = new android.databinding.tool.DataBindingBuilder.Versions(props);
            } finally {
                org.apache.commons.io.IOUtils.closeQuietly(stream);
            }
        } catch (java.io.IOException exception) {
            android.databinding.tool.util.L.e(exception, "Cannot read data binding version");
        }
        return mVersions;
    }

    /**
     * Returns the list of classes that should be excluded from package task
     *
     * @param layoutXmlProcessor
     * 		The LayoutXmlProcessor for this variant
     * @param generatedClassListFile
     * 		The location of the File into which data binding compiler wrote
     * 		list of generated classes
     * @return The list of classes to exclude. They are already in JNI format.
     */
    public java.util.List<java.lang.String> getJarExcludeList(android.databinding.tool.LayoutXmlProcessor layoutXmlProcessor, java.io.File generatedClassListFile) {
        java.util.List<java.lang.String> excludes = new java.util.ArrayList<java.lang.String>();
        java.lang.String appPkgAsClass = layoutXmlProcessor.getPackage().replace('.', '/');
        java.lang.String infoClassAsClass = layoutXmlProcessor.getInfoClassFullName().replace('.', '/');
        excludes.add(infoClassAsClass + ".class");
        excludes.add(android.databinding.tool.DataBindingBuilder.EXCLUDE_PATTERN);
        excludes.add(appPkgAsClass + "/BR.*");
        excludes.add("android/databinding/DynamicUtil.class");
        if (generatedClassListFile != null) {
            java.util.List<java.lang.String> generatedClasses = readGeneratedClasses(generatedClassListFile);
            for (java.lang.String klass : generatedClasses) {
                excludes.add(klass.replace('.', '/') + ".class");
            }
        }
        android.databinding.tool.processing.Scope.assertNoError();
        return excludes;
    }

    private java.util.List<java.lang.String> readGeneratedClasses(java.io.File generatedClassListFile) {
        android.databinding.tool.util.Preconditions.checkNotNull(generatedClassListFile, "Data binding exclude generated task" + " is not configured properly");
        android.databinding.tool.util.Preconditions.check(generatedClassListFile.exists(), "Generated class list does not exist %s", generatedClassListFile.getAbsolutePath());
        java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream(generatedClassListFile);
            return org.apache.commons.io.IOUtils.readLines(fis);
        } catch (java.io.FileNotFoundException e) {
            android.databinding.tool.util.L.e(e, "Unable to read generated class list from %s", generatedClassListFile.getAbsoluteFile());
        } catch (java.io.IOException e) {
            android.databinding.tool.util.L.e(e, "Unexpected exception while reading %s", generatedClassListFile.getAbsoluteFile());
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(fis);
        }
        android.databinding.tool.util.L.e("Could not read data binding generated class list");
        return null;
    }

    public android.databinding.tool.writer.JavaFileWriter createJavaFileWriter(java.io.File outFolder) {
        return new android.databinding.tool.DataBindingBuilder.GradleFileWriter(outFolder.getAbsolutePath());
    }

    static class GradleFileWriter extends android.databinding.tool.writer.JavaFileWriter {
        private final java.lang.String outputBase;

        public GradleFileWriter(java.lang.String outputBase) {
            this.outputBase = outputBase;
        }

        @java.lang.Override
        public void writeToFile(java.lang.String canonicalName, java.lang.String contents) {
            java.lang.String asPath = canonicalName.replace('.', '/');
            java.io.File f = new java.io.File(((outputBase + "/") + asPath) + ".java");
            // noinspection ResultOfMethodCallIgnored
            f.getParentFile().mkdirs();
            java.io.FileOutputStream fos = null;
            try {
                fos = new java.io.FileOutputStream(f);
                org.apache.commons.io.IOUtils.write(contents, fos);
            } catch (java.io.IOException e) {
                android.databinding.tool.util.L.e(e, "cannot write file " + f.getAbsolutePath());
            } finally {
                org.apache.commons.io.IOUtils.closeQuietly(fos);
            }
        }
    }

    private static class Versions {
        final java.lang.String compilerCommon;

        final java.lang.String compiler;

        final java.lang.String baseLibrary;

        final java.lang.String extensions;

        public Versions(java.util.Properties properties) {
            compilerCommon = properties.getProperty("compilerCommon");
            compiler = properties.getProperty("compiler");
            baseLibrary = properties.getProperty("baseLibrary");
            extensions = properties.getProperty("extensions");
            android.databinding.tool.util.Preconditions.checkNotNull(compilerCommon, "cannot read compiler common version");
            android.databinding.tool.util.Preconditions.checkNotNull(compiler, "cannot read compiler version");
            android.databinding.tool.util.Preconditions.checkNotNull(baseLibrary, "cannot read baseLibrary version");
            android.databinding.tool.util.Preconditions.checkNotNull(extensions, "cannot read extensions version");
        }
    }
}

