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
 * Task to exclude generated classes from the Jar task of a library project
 */
public class DataBindingExcludeGeneratedTask extends org.gradle.api.DefaultTask {
    private java.lang.String appPackage;

    private java.lang.String infoClassQualifiedName;

    @org.gradle.api.tasks.Input
    private java.io.File generatedClassListFile;

    private boolean isLibrary;

    private org.gradle.api.tasks.bundling.Jar packageTask;

    private final java.lang.String EXCLUDE_PATTERN = "android/databinding/layouts/*.*";

    public void setAppPackage(java.lang.String appPackage) {
        this.appPackage = appPackage;
    }

    public void setInfoClassQualifiedName(java.lang.String infoClassQualifiedName) {
        this.infoClassQualifiedName = infoClassQualifiedName;
    }

    public void setLibrary(boolean isLibrary) {
        this.isLibrary = isLibrary;
    }

    public void setPackageTask(org.gradle.api.tasks.bundling.Jar packageTask) {
        this.packageTask = packageTask;
    }

    public void setGeneratedClassListFile(java.io.File generatedClassListFile) {
        this.generatedClassListFile = generatedClassListFile;
    }

    public java.lang.String getAppPackage() {
        return appPackage;
    }

    public java.lang.String getInfoClassQualifiedName() {
        return infoClassQualifiedName;
    }

    public java.io.File getGeneratedClassListFile() {
        return generatedClassListFile;
    }

    @org.gradle.api.tasks.TaskAction
    public void excludeGenerated() {
        android.databinding.tool.util.L.d("Excluding generated classes from jar. Is library ? %s", isLibrary);
        java.lang.String appPkgAsClass = appPackage.replace('.', '/');
        java.lang.String infoClassAsClass = infoClassQualifiedName.replace('.', '/');
        exclude(infoClassAsClass + ".class");
        exclude(EXCLUDE_PATTERN);
        if (isLibrary) {
            exclude(appPkgAsClass + "/BR.*");
            exclude("android/databinding/DynamicUtil.class");
            java.util.List<java.lang.String> generatedClasses = readGeneratedClasses();
            for (java.lang.String klass : generatedClasses) {
                exclude(klass.replace('.', '/') + ".class");
            }
        }
        android.databinding.tool.processing.Scope.assertNoError();
        android.databinding.tool.util.L.d("Excluding generated classes from library jar is done.");
    }

    private void exclude(java.lang.String pattern) {
        android.databinding.tool.util.L.d("exclude %s", pattern);
        packageTask.exclude(pattern);
    }

    private java.util.List<java.lang.String> readGeneratedClasses() {
        com.google.common.base.Preconditions.checkNotNull(generatedClassListFile, "Data binding exclude generated task" + " is not configured properly");
        com.google.common.base.Preconditions.checkArgument(generatedClassListFile.exists(), "Generated class list does not exist %s", generatedClassListFile.getAbsolutePath());
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
        com.google.common.base.Preconditions.checkState(false, "Could not read data binding generated class list");
        return null;
    }
}

