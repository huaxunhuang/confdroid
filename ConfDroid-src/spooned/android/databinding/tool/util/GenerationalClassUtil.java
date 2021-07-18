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
package android.databinding.tool.util;


/**
 * A utility class that helps adding build specific objects to the jar file
 * and their extraction later on.
 */
public class GenerationalClassUtil {
    private static java.util.List[] sCache = null;

    public static <T extends java.io.Serializable> java.util.List<T> loadObjects(android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter filter) {
        if (android.databinding.tool.util.GenerationalClassUtil.sCache == null) {
            android.databinding.tool.util.GenerationalClassUtil.buildCache();
        }
        // noinspection unchecked
        return android.databinding.tool.util.GenerationalClassUtil.sCache[filter.ordinal()];
    }

    private static void buildCache() {
        android.databinding.tool.util.L.d("building generational class cache");
        java.lang.ClassLoader classLoader = android.databinding.tool.util.GenerationalClassUtil.class.getClassLoader();
        android.databinding.tool.util.Preconditions.check(classLoader instanceof java.net.URLClassLoader, "Class loader must be an" + "instance of URLClassLoader. %s", classLoader);
        // noinspection ConstantConditions
        final java.net.URLClassLoader urlClassLoader = ((java.net.URLClassLoader) (classLoader));
        android.databinding.tool.util.GenerationalClassUtil.sCache = new java.util.List[android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.values().length];
        for (android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter filter : android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.values()) {
            android.databinding.tool.util.GenerationalClassUtil.sCache[filter.ordinal()] = new java.util.ArrayList();
        }
        for (java.net.URL url : urlClassLoader.getURLs()) {
            android.databinding.tool.util.L.d("checking url %s for intermediate data", url);
            try {
                final java.io.File file = new java.io.File(url.toURI());
                if (!file.exists()) {
                    android.databinding.tool.util.L.d("cannot load file for %s", url);
                    continue;
                }
                if (file.isDirectory()) {
                    // probably exported classes dir.
                    android.databinding.tool.util.GenerationalClassUtil.loadFromDirectory(file);
                } else {
                    // assume it is a zip file
                    android.databinding.tool.util.GenerationalClassUtil.loadFomZipFile(file);
                }
            } catch (java.io.IOException e) {
                android.databinding.tool.util.L.d("cannot open zip file from %s", url);
            } catch (java.net.URISyntaxException e) {
                android.databinding.tool.util.L.d("cannot open zip file from %s", url);
            }
        }
    }

    private static void loadFromDirectory(java.io.File directory) {
        for (java.io.File file : org.apache.commons.io.FileUtils.listFiles(directory, org.apache.commons.io.filefilter.TrueFileFilter.INSTANCE, org.apache.commons.io.filefilter.TrueFileFilter.INSTANCE)) {
            for (android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter filter : android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.values()) {
                if (filter.accept(file.getName())) {
                    java.io.InputStream inputStream = null;
                    try {
                        inputStream = org.apache.commons.io.FileUtils.openInputStream(file);
                        java.io.Serializable item = android.databinding.tool.util.GenerationalClassUtil.fromInputStream(inputStream);
                        if (item != null) {
                            // noinspection unchecked
                            android.databinding.tool.util.GenerationalClassUtil.sCache[filter.ordinal()].add(item);
                            android.databinding.tool.util.L.d("loaded item %s from file", item);
                        }
                    } catch (java.io.IOException e) {
                        android.databinding.tool.util.L.e(e, "Could not merge in Bindables from %s", file.getAbsolutePath());
                    } catch (java.lang.ClassNotFoundException e) {
                        android.databinding.tool.util.L.e(e, "Could not read Binding properties intermediate file. %s", file.getAbsolutePath());
                    } finally {
                        org.apache.commons.io.IOUtils.closeQuietly(inputStream);
                    }
                }
            }
        }
    }

    private static void loadFomZipFile(java.io.File file) throws java.io.IOException {
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);
        java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            java.util.zip.ZipEntry entry = entries.nextElement();
            for (android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter filter : android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.values()) {
                if (!filter.accept(entry.getName())) {
                    continue;
                }
                java.io.InputStream inputStream = null;
                try {
                    inputStream = zipFile.getInputStream(entry);
                    java.io.Serializable item = android.databinding.tool.util.GenerationalClassUtil.fromInputStream(inputStream);
                    android.databinding.tool.util.L.d("loaded item %s from zip file", item);
                    if (item != null) {
                        // noinspection unchecked
                        android.databinding.tool.util.GenerationalClassUtil.sCache[filter.ordinal()].add(item);
                    }
                } catch (java.io.IOException e) {
                    android.databinding.tool.util.L.e(e, "Could not merge in Bindables from %s", file.getAbsolutePath());
                } catch (java.lang.ClassNotFoundException e) {
                    android.databinding.tool.util.L.e(e, "Could not read Binding properties intermediate file. %s", file.getAbsolutePath());
                } finally {
                    org.apache.commons.io.IOUtils.closeQuietly(inputStream);
                }
            }
        } 
    }

    private static java.io.Serializable fromInputStream(java.io.InputStream inputStream) throws java.io.IOException, java.lang.ClassNotFoundException {
        java.io.ObjectInputStream in = new java.io.ObjectInputStream(inputStream);
        return ((java.io.Serializable) (in.readObject()));
    }

    public static void writeIntermediateFile(javax.annotation.processing.ProcessingEnvironment processingEnv, java.lang.String packageName, java.lang.String fileName, java.io.Serializable object) {
        java.io.ObjectOutputStream oos = null;
        try {
            javax.tools.FileObject intermediate = processingEnv.getFiler().createResource(javax.tools.StandardLocation.CLASS_OUTPUT, packageName, fileName);
            java.io.OutputStream ios = intermediate.openOutputStream();
            oos = new java.io.ObjectOutputStream(ios);
            oos.writeObject(object);
            oos.close();
            android.databinding.tool.util.L.d("wrote intermediate bindable file %s %s", packageName, fileName);
        } catch (java.io.IOException e) {
            android.databinding.tool.util.L.e(e, "Could not write to intermediate file: %s", fileName);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(oos);
        }
    }

    public enum ExtensionFilter {

        BR("-br.bin"),
        LAYOUT("-layoutinfo.bin"),
        SETTER_STORE("-setter_store.bin");
        private final java.lang.String mExtension;

        ExtensionFilter(java.lang.String extension) {
            mExtension = extension;
        }

        public boolean accept(java.lang.String entryName) {
            return entryName.endsWith(mExtension);
        }

        public java.lang.String getExtension() {
            return mExtension;
        }
    }
}

