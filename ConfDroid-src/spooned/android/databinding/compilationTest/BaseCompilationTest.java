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


public class BaseCompilationTest {
    private static final java.lang.String PRINT_ENCODED_ERRORS_PROPERTY = "android.injected.invoked.from.ide";

    @org.junit.Rule
    public org.junit.rules.TestName name = new org.junit.rules.TestName();

    static java.util.regex.Pattern VARIABLES = java.util.regex.Pattern.compile("!@\\{([A-Za-z0-9_-]*)}");

    public static final java.lang.String KEY_MANIFEST_PACKAGE = "PACKAGE";

    public static final java.lang.String KEY_DEPENDENCIES = "DEPENDENCIES";

    public static final java.lang.String KEY_SETTINGS_INCLUDES = "SETTINGS_INCLUDES";

    public static final java.lang.String DEFAULT_APP_PACKAGE = "com.android.databinding.compilationTest.test";

    public static final java.lang.String KEY_CLASS_NAME = "CLASSNAME";

    public static final java.lang.String KEY_CLASS_TYPE = "CLASSTYPE";

    public static final java.lang.String KEY_IMPORT_TYPE = "IMPORTTYPE";

    public static final java.lang.String KEY_INCLUDE_ID = "INCLUDEID";

    public static final java.lang.String KEY_VIEW_ID = "VIEWID";

    protected final java.io.File testFolder = new java.io.File("./build/build-test");

    protected void copyResourceTo(java.lang.String name, java.lang.String path) throws java.io.IOException {
        copyResourceTo(name, new java.io.File(testFolder, path));
    }

    protected void copyResourceTo(java.lang.String name, java.lang.String path, java.util.Map<java.lang.String, java.lang.String> replacements) throws java.io.IOException {
        copyResourceTo(name, new java.io.File(testFolder, path), replacements);
    }

    protected void copyResourceDirectory(java.lang.String name, java.lang.String targetPath) throws java.io.IOException, java.net.URISyntaxException {
        java.net.URL dir = getClass().getResource(name);
        org.junit.Assert.assertNotNull(dir);
        org.junit.Assert.assertEquals("file", dir.getProtocol());
        java.io.File folder = new java.io.File(dir.toURI());
        org.junit.Assert.assertTrue(folder.isDirectory());
        java.io.File target = new java.io.File(testFolder, targetPath);
        int len = folder.getAbsolutePath().length() + 1;
        for (java.io.File item : org.apache.commons.io.FileUtils.listFiles(folder, null, true)) {
            if (item.getAbsolutePath().equals(folder.getAbsolutePath())) {
                continue;
            }
            java.lang.String resourcePath = item.getAbsolutePath().substring(len);
            copyResourceTo((name + "/") + resourcePath, new java.io.File(target, resourcePath));
        }
    }

    @org.junit.Before
    public void clear() throws java.io.IOException {
        if (testFolder.exists()) {
            org.apache.commons.io.FileUtils.forceDelete(testFolder);
        }
    }

    /**
     * Extracts the text in the given location from the the at the given application path.
     *
     * @param pathInApp
     * 		The path, relative to the root of the application under test
     * @param location
     * 		The location to extract
     * @return The string that is contained in the given location
     * @throws IOException
     * 		If file is invalid.
     */
    protected java.lang.String extract(java.lang.String pathInApp, android.databinding.tool.store.Location location) throws java.io.IOException {
        java.io.File file = new java.io.File(testFolder, pathInApp);
        org.junit.Assert.assertTrue(file.exists());
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        java.util.List<java.lang.String> lines = org.apache.commons.io.FileUtils.readLines(file);
        for (int i = location.startLine; i <= location.endLine; i++) {
            if (i > location.startLine) {
                result.append("\n");
            }
            java.lang.String line = lines.get(i);
            int start = 0;
            if (i == location.startLine) {
                start = location.startOffset;
            }
            int end = line.length() - 1;// inclusive

            if (i == location.endLine) {
                end = location.endOffset;
            }
            result.append(line.substring(start, end + 1));
        }
        return result.toString();
    }

    protected void copyResourceTo(java.lang.String name, java.io.File targetFile) throws java.io.IOException {
        java.io.File directory = targetFile.getParentFile();
        org.apache.commons.io.FileUtils.forceMkdir(directory);
        java.io.InputStream contents = getClass().getResourceAsStream(name);
        java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFile);
        org.apache.commons.io.IOUtils.copy(contents, fos);
        org.apache.commons.io.IOUtils.closeQuietly(fos);
        org.apache.commons.io.IOUtils.closeQuietly(contents);
    }

    protected static java.util.Map<java.lang.String, java.lang.String> toMap(java.lang.String... keysAndValues) {
        org.junit.Assert.assertEquals(0, keysAndValues.length % 2);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<java.lang.String, java.lang.String>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }

    protected void copyResourceTo(java.lang.String name, java.io.File targetFile, java.util.Map<java.lang.String, java.lang.String> replacements) throws java.io.IOException {
        if (replacements.isEmpty()) {
            copyResourceTo(name, targetFile);
        }
        java.io.InputStream inputStream = getClass().getResourceAsStream(name);
        final java.lang.String contents = org.apache.commons.io.IOUtils.toString(inputStream);
        org.apache.commons.io.IOUtils.closeQuietly(inputStream);
        java.lang.StringBuilder out = new java.lang.StringBuilder(contents.length());
        final java.util.regex.Matcher matcher = android.databinding.compilationTest.BaseCompilationTest.VARIABLES.matcher(contents);
        int location = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (start > location) {
                out.append(contents, location, start);
            }
            final java.lang.String key = matcher.group(1);
            final java.lang.String replacement = replacements.get(key);
            if (replacement != null) {
                out.append(replacement);
            }
            location = matcher.end();
        } 
        if (location < contents.length()) {
            out.append(contents, location, contents.length());
        }
        org.apache.commons.io.FileUtils.writeStringToFile(targetFile, out.toString());
    }

    protected void prepareProject() throws java.io.IOException, java.net.URISyntaxException {
        prepareApp(null);
    }

    private java.util.Map<java.lang.String, java.lang.String> addDefaults(java.util.Map<java.lang.String, java.lang.String> map) {
        if (map == null) {
            map = new java.util.HashMap<java.lang.String, java.lang.String>();
        }
        if (!map.containsKey(android.databinding.compilationTest.BaseCompilationTest.KEY_MANIFEST_PACKAGE)) {
            map.put(android.databinding.compilationTest.BaseCompilationTest.KEY_MANIFEST_PACKAGE, android.databinding.compilationTest.BaseCompilationTest.DEFAULT_APP_PACKAGE);
        }
        if (!map.containsKey(android.databinding.compilationTest.BaseCompilationTest.KEY_SETTINGS_INCLUDES)) {
            map.put(android.databinding.compilationTest.BaseCompilationTest.KEY_SETTINGS_INCLUDES, "include ':app'");
        }
        return map;
    }

    protected void prepareApp(java.util.Map<java.lang.String, java.lang.String> replacements) throws java.io.IOException, java.net.URISyntaxException {
        replacements = addDefaults(replacements);
        // how to get build folder, pass from gradle somehow ?
        org.apache.commons.io.FileUtils.forceMkdir(testFolder);
        copyResourceTo("/AndroidManifest.xml", new java.io.File(testFolder, "app/src/main/AndroidManifest.xml"), replacements);
        copyResourceTo("/project_build.gradle", new java.io.File(testFolder, "build.gradle"), replacements);
        copyResourceTo("/app_build.gradle", new java.io.File(testFolder, "app/build.gradle"), replacements);
        copyResourceTo("/settings.gradle", new java.io.File(testFolder, "settings.gradle"), replacements);
        java.io.File localProperties = new java.io.File("../local.properties");
        if (localProperties.exists()) {
            org.apache.commons.io.FileUtils.copyFile(localProperties, new java.io.File(testFolder, "local.properties"));
        }
        org.apache.commons.io.FileUtils.copyFile(new java.io.File("../propLoader.gradle"), new java.io.File(testFolder, "propLoaderClone.gradle"));
        org.apache.commons.io.FileUtils.copyFile(new java.io.File("../gradlew"), new java.io.File(testFolder, "gradlew"));
        org.apache.commons.io.FileUtils.copyDirectory(new java.io.File("../gradle"), new java.io.File(testFolder, "gradle"));
    }

    protected void prepareModule(java.lang.String moduleName, java.lang.String packageName, java.util.Map<java.lang.String, java.lang.String> replacements) throws java.io.IOException, java.net.URISyntaxException {
        replacements = addDefaults(replacements);
        replacements.put(android.databinding.compilationTest.BaseCompilationTest.KEY_MANIFEST_PACKAGE, packageName);
        java.io.File moduleFolder = new java.io.File(testFolder, moduleName);
        if (moduleFolder.exists()) {
            org.apache.commons.io.FileUtils.forceDelete(moduleFolder);
        }
        org.apache.commons.io.FileUtils.forceMkdir(moduleFolder);
        copyResourceTo("/AndroidManifest.xml", new java.io.File(moduleFolder, "src/main/AndroidManifest.xml"), replacements);
        copyResourceTo("/module_build.gradle", new java.io.File(moduleFolder, "build.gradle"), replacements);
    }

    protected android.databinding.compilationTest.CompilationResult runGradle(java.lang.String... params) throws java.io.IOException, java.lang.InterruptedException {
        setExecutable();
        java.io.File pathToExecutable = new java.io.File(testFolder, "gradlew");
        java.util.List<java.lang.String> args = new java.util.ArrayList<java.lang.String>();
        args.add(pathToExecutable.getAbsolutePath());
        args.add(("-P" + android.databinding.compilationTest.BaseCompilationTest.PRINT_ENCODED_ERRORS_PROPERTY) + "=true");
        if ("true".equals(java.lang.System.getProperties().getProperty("useReleaseVersion", "false"))) {
            args.add("-PuseReleaseVersion=true");
        }
        if ("true".equals(java.lang.System.getProperties().getProperty("addRemoteRepos", "false"))) {
            args.add("-PaddRemoteRepos=true");
        }
        args.add("--project-cache-dir");
        args.add(new java.io.File("../.caches/", name.getMethodName()).getAbsolutePath());
        java.util.Collections.addAll(args, params);
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(args);
        builder.environment().putAll(java.lang.System.getenv());
        java.lang.String javaHome = java.lang.System.getProperty("java.home");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(javaHome)) {
            builder.environment().put("JAVA_HOME", javaHome);
        }
        builder.directory(testFolder);
        java.lang.Process process = builder.start();
        java.lang.String output = android.databinding.compilationTest.BaseCompilationTest.collect(process.getInputStream());
        java.lang.String error = android.databinding.compilationTest.BaseCompilationTest.collect(process.getErrorStream());
        int result = process.waitFor();
        return new android.databinding.compilationTest.CompilationResult(result, output, error);
    }

    private void setExecutable() throws java.io.IOException {
        java.io.File gw = new java.io.File(testFolder, "gradlew");
        gw.setExecutable(true);
    }

    /**
     * Use this instead of IO utils so that we can easily log the output when necessary
     */
    private static java.lang.String collect(java.io.InputStream stream) throws java.io.IOException {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String line;
        final java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(stream));
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        } 
        return sb.toString();
    }
}

