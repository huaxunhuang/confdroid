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
package android.databinding.tool.reflection.java;


public class JavaAnalyzer extends android.databinding.tool.reflection.ModelAnalyzer {
    public static final java.util.Map<java.lang.String, java.lang.Class> PRIMITIVE_TYPES;

    static {
        PRIMITIVE_TYPES = new java.util.HashMap<java.lang.String, java.lang.Class>();
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("boolean", boolean.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("byte", byte.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("short", short.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("char", char.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("int", int.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("long", long.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("float", float.class);
        android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.put("double", double.class);
    }

    private java.util.HashMap<java.lang.String, android.databinding.tool.reflection.java.JavaClass> mClassCache = new java.util.HashMap<java.lang.String, android.databinding.tool.reflection.java.JavaClass>();

    private final java.lang.ClassLoader mClassLoader;

    public JavaAnalyzer(java.lang.ClassLoader classLoader) {
        setInstance(this);
        mClassLoader = classLoader;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.java.JavaClass loadPrimitive(java.lang.String className) {
        java.lang.Class clazz = android.databinding.tool.reflection.java.JavaAnalyzer.PRIMITIVE_TYPES.get(className);
        if (clazz == null) {
            return null;
        } else {
            return new android.databinding.tool.reflection.java.JavaClass(clazz);
        }
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass[] getObservableFieldTypes() {
        return new android.databinding.tool.reflection.ModelClass[0];
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass findClass(java.lang.String className, java.util.Map<java.lang.String, java.lang.String> imports) {
        // TODO handle imports
        android.databinding.tool.reflection.java.JavaClass loaded = mClassCache.get(className);
        if (loaded != null) {
            return loaded;
        }
        android.databinding.tool.util.L.d("trying to load class %s from %s", className, mClassLoader.toString());
        loaded = loadPrimitive(className);
        if (loaded == null) {
            try {
                if (className.startsWith("[") && className.contains("L")) {
                    int indexOfL = className.indexOf('L');
                    android.databinding.tool.reflection.java.JavaClass baseClass = ((android.databinding.tool.reflection.java.JavaClass) (findClass(className.substring(indexOfL + 1, className.length() - 1), null)));
                    java.lang.String realClassName = (className.substring(0, indexOfL + 1) + baseClass.mClass.getCanonicalName()) + ';';
                    loaded = new android.databinding.tool.reflection.java.JavaClass(java.lang.Class.forName(realClassName, false, mClassLoader));
                    mClassCache.put(className, loaded);
                } else {
                    loaded = loadRecursively(className);
                    mClassCache.put(className, loaded);
                }
            } catch (java.lang.Throwable t) {
                // L.e(t, "cannot load class " + className);
            }
        }
        // expr visitor may call this to resolve statics. Sometimes, it is OK not to find a class.
        if (loaded == null) {
            return null;
        }
        android.databinding.tool.util.L.d("loaded class %s", loaded.mClass.getCanonicalName());
        return loaded;
    }

    @java.lang.Override
    public android.databinding.tool.reflection.ModelClass findClass(java.lang.Class classType) {
        return new android.databinding.tool.reflection.java.JavaClass(classType);
    }

    @java.lang.Override
    public android.databinding.tool.reflection.TypeUtil createTypeUtil() {
        return new android.databinding.tool.reflection.java.JavaTypeUtil();
    }

    private android.databinding.tool.reflection.java.JavaClass loadRecursively(java.lang.String className) throws java.lang.ClassNotFoundException {
        try {
            android.databinding.tool.util.L.d("recursively checking %s", className);
            return new android.databinding.tool.reflection.java.JavaClass(mClassLoader.loadClass(className));
        } catch (java.lang.ClassNotFoundException ex) {
            int lastIndexOfDot = className.lastIndexOf(".");
            if (lastIndexOfDot == (-1)) {
                throw ex;
            }
            return loadRecursively((className.substring(0, lastIndexOfDot) + "$") + className.substring(lastIndexOfDot + 1));
        }
    }

    private static java.lang.String loadAndroidHome() {
        java.util.Map<java.lang.String, java.lang.String> env = java.lang.System.getenv();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : env.entrySet()) {
            android.databinding.tool.util.L.d("%s %s", entry.getKey(), entry.getValue());
        }
        if (env.containsKey("ANDROID_HOME")) {
            return env.get("ANDROID_HOME");
        }
        // check for local.properties file
        java.io.File folder = new java.io.File(".").getAbsoluteFile();
        while ((folder != null) && folder.exists()) {
            java.io.File f = new java.io.File(folder, "local.properties");
            if (f.exists() && f.canRead()) {
                try {
                    for (java.lang.String line : org.apache.commons.io.FileUtils.readLines(f)) {
                        java.util.List<java.lang.String> keyValue = com.google.common.base.Splitter.on('=').splitToList(line);
                        if (keyValue.size() == 2) {
                            java.lang.String key = keyValue.get(0).trim();
                            if (key.equals("sdk.dir")) {
                                return keyValue.get(1).trim();
                            }
                        }
                    }
                } catch (java.io.IOException ignored) {
                }
            }
            folder = folder.getParentFile();
        } 
        return null;
    }

    public static void initForTests() {
        java.lang.String androidHome = android.databinding.tool.reflection.java.JavaAnalyzer.loadAndroidHome();
        if (com.google.common.base.Strings.isNullOrEmpty(androidHome) || (!new java.io.File(androidHome).exists())) {
            throw new java.lang.IllegalStateException("you need to have ANDROID_HOME set in your environment" + " to run compiler tests");
        }
        java.io.File androidJar = new java.io.File(androidHome + "/platforms/android-21/android.jar");
        if ((!androidJar.exists()) || (!androidJar.canRead())) {
            throw new java.lang.IllegalStateException("cannot find android jar at " + androidJar.getAbsolutePath());
        }
        // now load android data binding library as well
        try {
            java.lang.ClassLoader classLoader = new java.net.URLClassLoader(new java.net.URL[]{ androidJar.toURI().toURL() }, android.databinding.tool.reflection.ModelAnalyzer.class.getClassLoader());
            new android.databinding.tool.reflection.java.JavaAnalyzer(classLoader);
        } catch (java.net.MalformedURLException e) {
            throw new java.lang.RuntimeException("cannot create class loader", e);
        }
        android.databinding.tool.reflection.SdkUtil.initialize(8, new java.io.File(androidHome));
    }
}

