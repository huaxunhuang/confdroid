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


public class MultiLayoutVerificationTest extends android.databinding.compilationTest.BaseCompilationTest {
    @org.junit.Test
    public void testMultipleLayoutFilesWithNameMismatch() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo("/layout/layout_with_class_name.xml", "/app/src/main/res/layout/with_class_name.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_CLASS_NAME, "AClassName"));
        copyResourceTo("/layout/layout_with_class_name.xml", "/app/src/main/res/layout-land/with_class_name.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_CLASS_NAME, "SomeOtherClassName"));
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(result.output, 0, result.resultCode);
        java.util.List<android.databinding.tool.processing.ScopedException> exceptions = result.getBindingExceptions();
        org.junit.Assert.assertEquals(result.error, 2, exceptions.size());
        boolean foundNormal = false;
        boolean foundLandscape = false;
        for (android.databinding.tool.processing.ScopedException exception : exceptions) {
            android.databinding.tool.processing.ScopedErrorReport report = exception.getScopedErrorReport();
            org.junit.Assert.assertNotNull(report);
            java.io.File file = new java.io.File(report.getFilePath());
            org.junit.Assert.assertTrue(file.exists());
            org.junit.Assert.assertEquals(1, report.getLocations().size());
            android.databinding.tool.store.Location location = report.getLocations().get(0);
            java.lang.String name = file.getParentFile().getName();
            if ("layout".equals(name)) {
                org.junit.Assert.assertEquals(new java.io.File(testFolder, "/app/src/main/res/layout/with_class_name.xml").getCanonicalFile(), file.getCanonicalFile());
                java.lang.String extract = extract("/app/src/main/res/layout/with_class_name.xml", location);
                org.junit.Assert.assertEquals(extract, "AClassName");
                org.junit.Assert.assertEquals(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_LAYOUT_CLASS_NAME_MISMATCH, android.databinding.compilationTest.BaseCompilationTest.DEFAULT_APP_PACKAGE + ".databinding.AClassName", "layout/with_class_name"), exception.getBareMessage());
                foundNormal = true;
            } else
                if ("layout-land".equals(name)) {
                    org.junit.Assert.assertEquals(new java.io.File(testFolder, "/app/src/main/res/layout-land/with_class_name.xml").getCanonicalFile(), file.getCanonicalFile());
                    java.lang.String extract = extract("/app/src/main/res/layout-land/with_class_name.xml", location);
                    org.junit.Assert.assertEquals("SomeOtherClassName", extract);
                    org.junit.Assert.assertEquals(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_LAYOUT_CLASS_NAME_MISMATCH, android.databinding.compilationTest.BaseCompilationTest.DEFAULT_APP_PACKAGE + ".databinding.SomeOtherClassName", "layout-land/with_class_name"), exception.getBareMessage());
                    foundLandscape = true;
                } else {
                    org.junit.Assert.fail("unexpected error file");
                }

        }
        org.junit.Assert.assertTrue("should find default config error\n" + result.error, foundNormal);
        org.junit.Assert.assertTrue("should find landscape error\n" + result.error, foundLandscape);
    }

    @org.junit.Test
    public void testMultipleLayoutFilesVariableMismatch() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo("/layout/layout_with_variable_type.xml", "/app/src/main/res/layout/layout_with_variable_type.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_CLASS_TYPE, "String"));
        copyResourceTo("/layout/layout_with_variable_type.xml", "/app/src/main/res/layout-land/layout_with_variable_type.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_CLASS_TYPE, "CharSequence"));
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(result.output, 0, result.resultCode);
        java.util.List<android.databinding.tool.processing.ScopedException> exceptions = result.getBindingExceptions();
        org.junit.Assert.assertEquals(result.error, 2, exceptions.size());
        boolean foundNormal = false;
        boolean foundLandscape = false;
        for (android.databinding.tool.processing.ScopedException exception : exceptions) {
            android.databinding.tool.processing.ScopedErrorReport report = exception.getScopedErrorReport();
            org.junit.Assert.assertNotNull(report);
            java.io.File file = new java.io.File(report.getFilePath());
            org.junit.Assert.assertTrue(file.exists());
            org.junit.Assert.assertEquals(result.error, 1, report.getLocations().size());
            android.databinding.tool.store.Location location = report.getLocations().get(0);
            // validated in switch
            java.lang.String name = file.getParentFile().getName();
            java.lang.String config = name;
            java.lang.String type = "???";
            if ("layout".equals(name)) {
                type = "String";
                foundNormal = true;
            } else
                if ("layout-land".equals(name)) {
                    type = "CharSequence";
                    foundLandscape = true;
                } else {
                    org.junit.Assert.fail("unexpected error file");
                }

            org.junit.Assert.assertEquals(new java.io.File(testFolder, ("/app/src/main/res/" + config) + "/layout_with_variable_type.xml").getCanonicalFile(), file.getCanonicalFile());
            java.lang.String extract = extract(("/app/src/main/res/" + config) + "/layout_with_variable_type.xml", location);
            org.junit.Assert.assertEquals(extract, ("<variable name=\"myVariable\" type=\"" + type) + "\"/>");
            org.junit.Assert.assertEquals(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_VARIABLE_TYPE_MISMATCH, "myVariable", type, config + "/layout_with_variable_type"), exception.getBareMessage());
        }
        org.junit.Assert.assertTrue(result.error, foundNormal);
        org.junit.Assert.assertTrue(result.error, foundLandscape);
    }

    @org.junit.Test
    public void testMultipleLayoutFilesImportMismatch() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        java.lang.String typeNormal = "java.util.List";
        java.lang.String typeLand = "java.util.Map";
        copyResourceTo("/layout/layout_with_import_type.xml", "/app/src/main/res/layout/layout_with_import_type.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_IMPORT_TYPE, typeNormal));
        copyResourceTo("/layout/layout_with_import_type.xml", "/app/src/main/res/layout-land/layout_with_import_type.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_IMPORT_TYPE, typeLand));
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(result.output, 0, result.resultCode);
        java.util.List<android.databinding.tool.processing.ScopedException> exceptions = result.getBindingExceptions();
        org.junit.Assert.assertEquals(result.error, 2, exceptions.size());
        boolean foundNormal = false;
        boolean foundLandscape = false;
        for (android.databinding.tool.processing.ScopedException exception : exceptions) {
            android.databinding.tool.processing.ScopedErrorReport report = exception.getScopedErrorReport();
            org.junit.Assert.assertNotNull(report);
            java.io.File file = new java.io.File(report.getFilePath());
            org.junit.Assert.assertTrue(file.exists());
            org.junit.Assert.assertEquals(result.error, 1, report.getLocations().size());
            android.databinding.tool.store.Location location = report.getLocations().get(0);
            // validated in switch
            java.lang.String name = file.getParentFile().getName();
            java.lang.String config = name;
            java.lang.String type = "???";
            if ("layout".equals(name)) {
                type = typeNormal;
                foundNormal = true;
            } else
                if ("layout-land".equals(name)) {
                    type = typeLand;
                    foundLandscape = true;
                } else {
                    org.junit.Assert.fail("unexpected error file");
                }

            org.junit.Assert.assertEquals(new java.io.File(testFolder, ("/app/src/main/res/" + config) + "/layout_with_import_type.xml").getCanonicalFile(), file.getCanonicalFile());
            java.lang.String extract = extract(("/app/src/main/res/" + config) + "/layout_with_import_type.xml", location);
            org.junit.Assert.assertEquals(extract, ("<import alias=\"Blah\" type=\"" + type) + "\"/>");
            org.junit.Assert.assertEquals(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_IMPORT_TYPE_MISMATCH, "Blah", type, config + "/layout_with_import_type"), exception.getBareMessage());
        }
        org.junit.Assert.assertTrue(result.error, foundNormal);
        org.junit.Assert.assertTrue(result.error, foundLandscape);
    }

    @org.junit.Test
    public void testSameIdInIncludeAndView() throws java.io.IOException, java.lang.InterruptedException, java.net.URISyntaxException {
        prepareProject();
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/basic_layout.xml");
        copyResourceTo("/layout/layout_with_include.xml", "/app/src/main/res/layout/foo.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_INCLUDE_ID, "sharedId"));
        copyResourceTo("/layout/layout_with_view_id.xml", "/app/src/main/res/layout-land/foo.xml", android.databinding.compilationTest.BaseCompilationTest.toMap(android.databinding.compilationTest.BaseCompilationTest.KEY_VIEW_ID, "sharedId"));
        android.databinding.compilationTest.CompilationResult result = runGradle("assembleDebug");
        org.junit.Assert.assertNotEquals(result.output, 0, result.resultCode);
        java.util.List<android.databinding.tool.processing.ScopedException> exceptions = result.getBindingExceptions();
        org.junit.Assert.assertEquals(result.error, 2, exceptions.size());
        boolean foundNormal = false;
        boolean foundLandscape = false;
        for (android.databinding.tool.processing.ScopedException exception : exceptions) {
            android.databinding.tool.processing.ScopedErrorReport report = exception.getScopedErrorReport();
            org.junit.Assert.assertNotNull(report);
            java.io.File file = new java.io.File(report.getFilePath());
            org.junit.Assert.assertTrue(file.exists());
            org.junit.Assert.assertEquals(result.error, 1, report.getLocations().size());
            android.databinding.tool.store.Location location = report.getLocations().get(0);
            // validated in switch
            java.lang.String config = file.getParentFile().getName();
            if ("layout".equals(config)) {
                java.lang.String extract = extract(("/app/src/main/res/" + config) + "/foo.xml", location);
                org.junit.Assert.assertEquals(extract, "<include layout=\"@layout/basic_layout\" " + "android:id=\"@+id/sharedId\" bind:myVariable=\"@{myVariable}\"/>");
                foundNormal = true;
            } else
                if ("layout-land".equals(config)) {
                    java.lang.String extract = extract(("/app/src/main/res/" + config) + "/foo.xml", location);
                    org.junit.Assert.assertEquals(extract, "<TextView android:layout_width=\"wrap_content\" " + ("android:layout_height=\"wrap_content\" android:id=\"@+id/sharedId\" " + "android:text=\"@{myVariable}\"/>"));
                    foundLandscape = true;
                } else {
                    org.junit.Assert.fail("unexpected error file");
                }

            org.junit.Assert.assertEquals(new java.io.File(testFolder, ("/app/src/main/res/" + config) + "/foo.xml").getCanonicalFile(), file.getCanonicalFile());
            org.junit.Assert.assertEquals(java.lang.String.format(android.databinding.tool.processing.ErrorMessages.MULTI_CONFIG_ID_USED_AS_IMPORT, "@+id/sharedId"), exception.getBareMessage());
        }
        org.junit.Assert.assertTrue(result.error, foundNormal);
        org.junit.Assert.assertTrue(result.error, foundLandscape);
    }
}

