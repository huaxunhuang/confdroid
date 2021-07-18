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
package android.databinding.compilationTest;


@java.lang.SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class SimpleCompilationTest extends android.databinding.compilationTest.BaseCompilationTest {
    @org.junit.Test
    public void listTasks() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        android.databinding.compilationTest.CompilationResult result = runGradle("tasks");
        org.junit.Assert.assertEquals(0, result.resultCode);
        org.junit.Assert.assertTrue("there should not be any errors", org.apache.commons.lang3.StringUtils.isEmpty(result.error));
        org.junit.Assert.assertTrue("Test sanity, empty project tasks", result.resultContainsText("All tasks runnable from root project"));
    }

    @org.junit.Test
    public void testEmptyCompilation() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertEquals(result.error, 0, result.resultCode);
        org.junit.Assert.assertTrue("there should not be any errors " + result.error, org.apache.commons.lang3.StringUtils.isEmpty(result.error));
        org.junit.Assert.assertTrue("Test sanity, should compile fine", result.resultContainsText("BUILD SUCCESSFUL"));
    }

    @org.junit.Test
    public void testMultipleConfigs() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/main.xml");
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout-sw100dp/main.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertEquals(result.error, 0, result.resultCode);
        java.io.File debugOut = new java.io.File(testFolder, "app/build/intermediates/data-binding-layout-out/debug");
        java.util.Collection<java.io.File> layoutFiles = org.apache.commons.io.FileUtils.listFiles(debugOut, new org.apache.commons.io.filefilter.SuffixFileFilter(".xml"), new org.apache.commons.io.filefilter.PrefixFileFilter("layout"));
        org.junit.Assert.assertTrue("test sanity", layoutFiles.size() > 1);
        for (java.io.File layout : layoutFiles) {
            final java.lang.String contents = org.apache.commons.io.FileUtils.readFileToString(layout);
            if (layout.getParent().contains("sw100")) {
                org.junit.Assert.assertTrue("File has wrong tag:" + layout.getPath(), contents.indexOf("android:tag=\"layout-sw100dp/main_0\"") > 0);
            } else {
                org.junit.Assert.assertTrue((("File has wrong tag:" + layout.getPath()) + "\n") + contents, contents.indexOf("android:tag=\"layout/main_0\"") > 0);
            }
        }
    }

    private android.databinding.tool.processing.ScopedException singleFileErrorTest(java.lang.String resource, java.lang.String targetFile, java.lang.String expectedExtract, java.lang.String errorMessage) throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo(resource, targetFile);
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(0, result.resultCode);
        android.databinding.tool.processing.ScopedException scopedException = result.getBindingException();
        org.junit.Assert.assertNotNull(result.error, scopedException);
        android.databinding.tool.processing.ScopedErrorReport report = scopedException.getScopedErrorReport();
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(1, report.getLocations().size());
        android.databinding.tool.store.Location loc = report.getLocations().get(0);
        if (expectedExtract != null) {
            java.lang.String extract = extract(targetFile, loc);
            org.junit.Assert.assertEquals(expectedExtract, extract);
        }
        final java.io.File errorFile = new java.io.File(report.getFilePath());
        org.junit.Assert.assertTrue(errorFile.exists());
        org.junit.Assert.assertEquals(new java.io.File(testFolder, targetFile).getCanonicalFile(), errorFile.getCanonicalFile());
        if (errorMessage != null) {
            org.junit.Assert.assertEquals(errorMessage, scopedException.getBareMessage());
        }
        return scopedException;
    }

    @org.junit.Test
    public void testMultipleExceptionsInDifferentFiles() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo("/layout/undefined_variable_binding.xml", "/app/src/main/res/layout/broken.xml");
        copyResourceTo("/layout/invalid_setter_binding.xml", "/app/src/main/res/layout/invalid_setter.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(result.output, 0, result.resultCode);
        java.util.List<android.databinding.tool.processing.ScopedException> bindingExceptions = result.getBindingExceptions();
        org.junit.Assert.assertEquals(result.error, 2, bindingExceptions.size());
        java.io.File broken = new java.io.File(testFolder, "/app/src/main/res/layout/broken.xml");
        java.io.File invalidSetter = new java.io.File(testFolder, "/app/src/main/res/layout/invalid_setter.xml");
        for (android.databinding.tool.processing.ScopedException exception : bindingExceptions) {
            android.databinding.tool.processing.ScopedErrorReport report = exception.getScopedErrorReport();
            final java.io.File errorFile = new java.io.File(report.getFilePath());
            java.lang.String message = null;
            java.lang.String expectedErrorFile = null;
            if (errorFile.getCanonicalPath().equals(broken.getCanonicalPath())) {
                message = java.lang.String.format(android.databinding.tool.processing.ErrorMessages.UNDEFINED_VARIABLE, "myVariable");
                expectedErrorFile = "/app/src/main/res/layout/broken.xml";
            } else
                if (errorFile.getCanonicalPath().equals(invalidSetter.getCanonicalPath())) {
                    message = java.lang.String.format(android.databinding.tool.processing.ErrorMessages.CANNOT_FIND_SETTER_CALL, "android:textx", java.lang.String.class.getCanonicalName());
                    expectedErrorFile = "/app/src/main/res/layout/invalid_setter.xml";
                } else {
                    org.junit.Assert.fail("unexpected exception " + exception.getBareMessage());
                }

            org.junit.Assert.assertEquals(1, report.getLocations().size());
            android.databinding.tool.store.Location loc = report.getLocations().get(0);
            java.lang.String extract = extract(expectedErrorFile, loc);
            org.junit.Assert.assertEquals("myVariable", extract);
            org.junit.Assert.assertEquals(message, exception.getBareMessage());
        }
    }

    @org.junit.Test
    public void testBadSyntax() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        singleFileErrorTest("/layout/layout_with_bad_syntax.xml", "/app/src/main/res/layout/broken.xml", "myVar.length())", java.lang.String.format(android.databinding.tool.processing.ErrorMessages.SYNTAX_ERROR, "extraneous input ')' expecting {<EOF>, ',', '.', '[', '+', '-', '*', '/', " + ("'%', '<<', '>>>', '>>', '<=', '>=', '>', '<', 'instanceof', " + "'==', '!=', '&', '^', '|', '&&', '||', '?', '??'}")));
    }

    @org.junit.Test
    public void testBrokenSyntax() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        singleFileErrorTest("/layout/layout_with_completely_broken_syntax.xml", "/app/src/main/res/layout/broken.xml", "new String()", java.lang.String.format(android.databinding.tool.processing.ErrorMessages.SYNTAX_ERROR, "mismatched input 'String' expecting {<EOF>, ',', '.', '[', '+', '-', '*', " + ("'/', '%', '<<', '>>>', '>>', '<=', '>=', '>', '<', 'instanceof'," + " '==', '!=', '&', '^', '|', '&&', '||', '?', '??'}")));
    }

    @org.junit.Test
    public void testUndefinedVariable() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        android.databinding.tool.processing.ScopedException ex = singleFileErrorTest("/layout/undefined_variable_binding.xml", "/app/src/main/res/layout/broken.xml", "myVariable", java.lang.String.format(android.databinding.tool.processing.ErrorMessages.UNDEFINED_VARIABLE, "myVariable"));
    }

    @org.junit.Test
    public void testInvalidSetterBinding() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        android.databinding.tool.processing.ScopedException ex = singleFileErrorTest("/layout/invalid_setter_binding.xml", "/app/src/main/res/layout/invalid_setter.xml", "myVariable", java.lang.String.format(android.databinding.tool.processing.ErrorMessages.CANNOT_FIND_SETTER_CALL, "android:textx", java.lang.String.class.getCanonicalName()));
    }

    @org.junit.Test
    public void testRootTag() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo("/layout/root_tag.xml", "/app/src/main/res/layout/root_tag.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(0, result.resultCode);
        org.junit.Assert.assertNotNull(result.error);
        final java.lang.String expected = java.lang.String.format(android.databinding.tool.processing.ErrorMessages.ROOT_TAG_NOT_SUPPORTED, "hello");
        org.junit.Assert.assertTrue(result.error.contains(expected));
    }

    @org.junit.Test
    public void testInvalidVariableType() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        android.databinding.tool.processing.ScopedException ex = singleFileErrorTest("/layout/invalid_variable_type.xml", "/app/src/main/res/layout/invalid_variable.xml", "myVariable", java.lang.String.format(android.databinding.tool.processing.ErrorMessages.CANNOT_RESOLVE_TYPE, "myVariable~"));
    }

    @org.junit.Test
    public void testSingleModule() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareApp(android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_DEPENDENCIES, "compile project(':module1')", android.databinding.compilationTest.BaseCompilationTest.KEY_SETTINGS_INCLUDES, "include \':app\'\ninclude \':module1\'"));
        prepareModule("module1", "com.example.module1", android.databinding.compilationTest.BaseCompilationTest.toMap());
        copyResourceTo("/layout/basic_layout.xml", "/module1/src/main/res/layout/module_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/app_layout.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertEquals(result.error, 0, result.resultCode);
    }

    @org.junit.Test
    public void testModuleDependencyChange() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareApp(android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_DEPENDENCIES, "compile project(':module1')", android.databinding.compilationTest.BaseCompilationTest.KEY_SETTINGS_INCLUDES, "include \':app\'\ninclude \':module1\'"));
        prepareModule("module1", "com.example.module1", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_DEPENDENCIES, "compile 'com.android.support:appcompat-v7:23.1.1'"));
        copyResourceTo("/layout/basic_layout.xml", "/module1/src/main/res/layout/module_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/app_layout.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertEquals(result.error, 0, result.resultCode);
        java.io.File moduleFolder = new java.io.File(testFolder, "module1");
        copyResourceTo("/module_build.gradle", new java.io.File(moduleFolder, "build.gradle"), android.databinding.compilationTest.BaseCompilationTest.toMap());
        result = runGradle("assembleDebug");
        org.junit.Assert.assertEquals(result.error, 0, result.resultCode);
    }

    @org.junit.Test
    public void testTwoLevelDependency() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareApp(android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_DEPENDENCIES, "compile project(':module1')", android.databinding.compilationTest.BaseCompilationTest.KEY_SETTINGS_INCLUDES, "include \':app\'\ninclude \':module1\'\n" + "include ':module2'"));
        prepareModule("module1", "com.example.module1", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_DEPENDENCIES, "compile project(':module2')"));
        prepareModule("module2", "com.example.module2", android.databinding.compilationTest.BaseCompilationTest.toMap());
        copyResourceTo("/layout/basic_layout.xml", "/module2/src/main/res/layout/module2_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/module1/src/main/res/layout/module1_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/app_layout.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertEquals(result.error, 0, result.resultCode);
    }

    @org.junit.Test
    public void testIncludeInMerge() throws java.lang.Throwable {
        prepareProject();
        copyResourceTo("/layout/merge_include.xml", "/app/src/main/res/layout/merge_include.xml");
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(0, result.resultCode);
        java.util.List<android.databinding.tool.processing.ScopedException> errors = android.databinding.tool.processing.ScopedException.extractErrors(result.error);
        org.junit.Assert.assertEquals(result.error, 1, errors.size());
        final android.databinding.tool.processing.ScopedException ex = errors.get(0);
        final android.databinding.tool.processing.ScopedErrorReport report = ex.getScopedErrorReport();
        final java.io.File errorFile = new java.io.File(report.getFilePath());
        org.junit.Assert.assertTrue(errorFile.exists());
        org.junit.Assert.assertEquals(new java.io.File(testFolder, "/app/src/main/res/layout/merge_include.xml").getCanonicalFile(), errorFile.getCanonicalFile());
        org.junit.Assert.assertEquals("Merge shouldn't support includes as root. Error message was '" + result.error, android.databinding.tool.processing.ErrorMessages.INCLUDE_INSIDE_MERGE, ex.getBareMessage());
    }
}

