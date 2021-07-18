/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.test.suitebuilder;


/**
 * Represents a collection of test classes present on the classpath. You can add individual classes
 * or entire packages. By default sub-packages are included recursively, but methods are
 * provided to allow for arbitrary inclusion or exclusion of sub-packages. Typically a
 * {@link TestGrouping} will have only one root package, but this is not a requirement.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
public class TestGrouping {
    private static final java.lang.String LOG_TAG = "TestGrouping";

    java.util.SortedSet<java.lang.Class<? extends junit.framework.TestCase>> testCaseClasses;

    public static final java.util.Comparator<java.lang.Class<? extends junit.framework.TestCase>> SORT_BY_SIMPLE_NAME = new android.test.suitebuilder.TestGrouping.SortBySimpleName();

    public static final java.util.Comparator<java.lang.Class<? extends junit.framework.TestCase>> SORT_BY_FULLY_QUALIFIED_NAME = new android.test.suitebuilder.TestGrouping.SortByFullyQualifiedName();

    protected java.lang.String firstIncludedPackage = null;

    private java.lang.ClassLoader classLoader;

    public TestGrouping(java.util.Comparator<java.lang.Class<? extends junit.framework.TestCase>> comparator) {
        testCaseClasses = new java.util.TreeSet<java.lang.Class<? extends junit.framework.TestCase>>(comparator);
    }

    /**
     *
     *
     * @return A list of all tests in the package, including small, medium, large,
    flaky, and suppressed tests. Includes sub-packages recursively.
     */
    public java.util.List<android.test.suitebuilder.TestMethod> getTests() {
        java.util.List<android.test.suitebuilder.TestMethod> testMethods = new java.util.ArrayList<android.test.suitebuilder.TestMethod>();
        for (java.lang.Class<? extends junit.framework.TestCase> testCase : testCaseClasses) {
            for (java.lang.reflect.Method testMethod : getTestMethods(testCase)) {
                testMethods.add(new android.test.suitebuilder.TestMethod(testMethod, testCase));
            }
        }
        return testMethods;
    }

    protected java.util.List<java.lang.reflect.Method> getTestMethods(java.lang.Class<? extends junit.framework.TestCase> testCaseClass) {
        java.util.List<java.lang.reflect.Method> methods = java.util.Arrays.asList(testCaseClass.getMethods());
        return select(methods, new android.test.suitebuilder.TestGrouping.TestMethodPredicate());
    }

    java.util.SortedSet<java.lang.Class<? extends junit.framework.TestCase>> getTestCaseClasses() {
        return testCaseClasses;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.test.suitebuilder.TestGrouping other = ((android.test.suitebuilder.TestGrouping) (o));
        if (!this.testCaseClasses.equals(other.testCaseClasses)) {
            return false;
        }
        return this.testCaseClasses.comparator().equals(other.testCaseClasses.comparator());
    }

    public int hashCode() {
        return testCaseClasses.hashCode();
    }

    /**
     * Include all tests in the given packages and all their sub-packages, unless otherwise
     * specified. Each of the given packages must contain at least one test class, either directly
     * or in a sub-package.
     *
     * @param packageNames
     * 		Names of packages to add.
     * @return The {@link TestGrouping} for method chaining.
     */
    public android.test.suitebuilder.TestGrouping addPackagesRecursive(java.lang.String... packageNames) {
        for (java.lang.String packageName : packageNames) {
            java.util.List<java.lang.Class<? extends junit.framework.TestCase>> addedClasses = testCaseClassesInPackage(packageName);
            if (addedClasses.isEmpty()) {
                android.util.Log.w(android.test.suitebuilder.TestGrouping.LOG_TAG, ("Invalid Package: '" + packageName) + "' could not be found or has no tests");
            }
            testCaseClasses.addAll(addedClasses);
            if (firstIncludedPackage == null) {
                firstIncludedPackage = packageName;
            }
        }
        return this;
    }

    /**
     * Exclude all tests in the given packages and all their sub-packages, unless otherwise
     * specified.
     *
     * @param packageNames
     * 		Names of packages to remove.
     * @return The {@link TestGrouping} for method chaining.
     */
    public android.test.suitebuilder.TestGrouping removePackagesRecursive(java.lang.String... packageNames) {
        for (java.lang.String packageName : packageNames) {
            testCaseClasses.removeAll(testCaseClassesInPackage(packageName));
        }
        return this;
    }

    /**
     *
     *
     * @return The first package name passed to {@link #addPackagesRecursive(String[])}, or null
    if that method was never called.
     */
    public java.lang.String getFirstIncludedPackage() {
        return firstIncludedPackage;
    }

    private java.util.List<java.lang.Class<? extends junit.framework.TestCase>> testCaseClassesInPackage(java.lang.String packageName) {
        android.test.ClassPathPackageInfoSource source = android.test.PackageInfoSources.forClassPath(classLoader);
        android.test.ClassPathPackageInfo packageInfo = source.getPackageInfo(packageName);
        return selectTestClasses(packageInfo.getTopLevelClassesRecursive());
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.List<java.lang.Class<? extends junit.framework.TestCase>> selectTestClasses(java.util.Set<java.lang.Class<?>> allClasses) {
        java.util.List<java.lang.Class<? extends junit.framework.TestCase>> testClasses = new java.util.ArrayList<java.lang.Class<? extends junit.framework.TestCase>>();
        for (java.lang.Class<?> testClass : select(allClasses, new android.test.suitebuilder.TestGrouping.TestCasePredicate())) {
            testClasses.add(((java.lang.Class<? extends junit.framework.TestCase>) (testClass)));
        }
        return testClasses;
    }

    private <T> java.util.List<T> select(java.util.Collection<T> items, com.android.internal.util.Predicate<T> predicate) {
        java.util.ArrayList<T> selectedItems = new java.util.ArrayList<T>();
        for (T item : items) {
            if (predicate.apply(item)) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public void setClassLoader(java.lang.ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Sort classes by their simple names (i.e. without the package prefix), using
     * their packages to sort classes with the same name.
     */
    private static class SortBySimpleName implements java.io.Serializable , java.util.Comparator<java.lang.Class<? extends junit.framework.TestCase>> {
        public int compare(java.lang.Class<? extends junit.framework.TestCase> class1, java.lang.Class<? extends junit.framework.TestCase> class2) {
            int result = class1.getSimpleName().compareTo(class2.getSimpleName());
            if (result != 0) {
                return result;
            }
            return class1.getName().compareTo(class2.getName());
        }
    }

    /**
     * Sort classes by their fully qualified names (i.e. with the package
     * prefix).
     */
    private static class SortByFullyQualifiedName implements java.io.Serializable , java.util.Comparator<java.lang.Class<? extends junit.framework.TestCase>> {
        public int compare(java.lang.Class<? extends junit.framework.TestCase> class1, java.lang.Class<? extends junit.framework.TestCase> class2) {
            return class1.getName().compareTo(class2.getName());
        }
    }

    private static class TestCasePredicate implements com.android.internal.util.Predicate<java.lang.Class<?>> {
        public boolean apply(java.lang.Class aClass) {
            int modifiers = ((java.lang.Class<?>) (aClass)).getModifiers();
            return ((junit.framework.TestCase.class.isAssignableFrom(((java.lang.Class<?>) (aClass))) && java.lang.reflect.Modifier.isPublic(modifiers)) && (!java.lang.reflect.Modifier.isAbstract(modifiers))) && hasValidConstructor(((java.lang.Class<?>) (aClass)));
        }

        @java.lang.SuppressWarnings("unchecked")
        private boolean hasValidConstructor(java.lang.Class<?> aClass) {
            // The cast below is not necessary with the Java 5 compiler, but necessary with the Java 6 compiler,
            // where the return type of Class.getDeclaredConstructors() was changed
            // from Constructor<T>[] to Constructor<?>[]
            java.lang.reflect.Constructor<? extends junit.framework.TestCase>[] constructors = ((java.lang.reflect.Constructor<? extends junit.framework.TestCase>[]) (aClass.getConstructors()));
            for (java.lang.reflect.Constructor<? extends junit.framework.TestCase> constructor : constructors) {
                if (java.lang.reflect.Modifier.isPublic(constructor.getModifiers())) {
                    java.lang.Class[] parameterTypes = constructor.getParameterTypes();
                    if ((parameterTypes.length == 0) || ((parameterTypes.length == 1) && (parameterTypes[0] == java.lang.String.class))) {
                        return true;
                    }
                }
            }
            android.util.Log.i(android.test.suitebuilder.TestGrouping.LOG_TAG, java.lang.String.format("TestCase class %s is missing a public constructor with no parameters " + "or a single String parameter - skipping", aClass.getName()));
            return false;
        }
    }

    private static class TestMethodPredicate implements com.android.internal.util.Predicate<java.lang.reflect.Method> {
        public boolean apply(java.lang.reflect.Method method) {
            return ((method.getParameterTypes().length == 0) && method.getName().startsWith("test")) && method.getReturnType().getSimpleName().equals("void");
        }
    }
}

