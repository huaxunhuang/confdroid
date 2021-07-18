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
 * Build suites based on a combination of included packages, excluded packages,
 * and predicates that must be satisfied.
 */
public class TestSuiteBuilder {
    private android.content.Context context;

    private final android.test.suitebuilder.TestGrouping testGrouping = new android.test.suitebuilder.TestGrouping(android.test.suitebuilder.TestGrouping.SORT_BY_FULLY_QUALIFIED_NAME);

    private final java.util.Set<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>> predicates = new java.util.HashSet<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>>();

    private java.util.List<junit.framework.TestCase> testCases;

    private junit.framework.TestSuite rootSuite;

    private junit.framework.TestSuite suiteForCurrentClass;

    private java.lang.String currentClassname;

    private java.lang.String suiteName;

    /**
     * The given name is automatically prefixed with the package containing the tests to be run.
     * If more than one package is specified, the first is used.
     *
     * @param clazz
     * 		Use the class from your .apk. Use the class name for the test suite name.
     * 		Use the class' classloader in order to load classes for testing.
     * 		This is needed when running in the emulator.
     */
    public TestSuiteBuilder(java.lang.Class clazz) {
        this(clazz.getName(), clazz.getClassLoader());
    }

    public TestSuiteBuilder(java.lang.String name, java.lang.ClassLoader classLoader) {
        this.suiteName = name;
        this.testGrouping.setClassLoader(classLoader);
        this.testCases = com.google.android.collect.Lists.newArrayList();
        addRequirements(android.test.suitebuilder.TestPredicates.REJECT_SUPPRESSED);
    }

    /**
     *
     *
     * @unknown pending API Council approval
     */
    public android.test.suitebuilder.TestSuiteBuilder addTestClassByName(java.lang.String testClassName, java.lang.String testMethodName, android.content.Context context) {
        android.test.AndroidTestRunner atr = new android.test.AndroidTestRunner();
        atr.setContext(context);
        atr.setTestClassName(testClassName, testMethodName);
        this.testCases.addAll(atr.getTestCases());
        return this;
    }

    /**
     *
     *
     * @unknown pending API Council approval
     */
    public android.test.suitebuilder.TestSuiteBuilder addTestSuite(junit.framework.TestSuite testSuite) {
        for (junit.framework.TestCase testCase : ((java.util.List<junit.framework.TestCase>) (android.test.TestCaseUtil.getTests(testSuite, true)))) {
            this.testCases.add(testCase);
        }
        return this;
    }

    /**
     * Include all tests that satisfy the requirements in the given packages and all sub-packages,
     * unless otherwise specified.
     *
     * @param packageNames
     * 		Names of packages to add.
     * @return The builder for method chaining.
     */
    public android.test.suitebuilder.TestSuiteBuilder includePackages(java.lang.String... packageNames) {
        testGrouping.addPackagesRecursive(packageNames);
        return this;
    }

    /**
     * Exclude all tests in the given packages and all sub-packages, unless otherwise specified.
     *
     * @param packageNames
     * 		Names of packages to remove.
     * @return The builder for method chaining.
     */
    public android.test.suitebuilder.TestSuiteBuilder excludePackages(java.lang.String... packageNames) {
        testGrouping.removePackagesRecursive(packageNames);
        return this;
    }

    /**
     * Exclude tests that fail to satisfy all of the given predicates.
     *
     * @param predicates
     * 		Predicates to add to the list of requirements.
     * @return The builder for method chaining.
     */
    public android.test.suitebuilder.TestSuiteBuilder addRequirements(java.util.List<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>> predicates) {
        this.predicates.addAll(predicates);
        return this;
    }

    /**
     * Include all junit tests that satisfy the requirements in the calling class' package and all
     * sub-packages.
     *
     * @return The builder for method chaining.
     */
    public final android.test.suitebuilder.TestSuiteBuilder includeAllPackagesUnderHere() {
        java.lang.StackTraceElement[] stackTraceElements = java.lang.Thread.currentThread().getStackTrace();
        java.lang.String callingClassName = null;
        java.lang.String thisClassName = android.test.suitebuilder.TestSuiteBuilder.class.getName();
        // We want to get the package of this method's calling class. This method's calling class
        // should be one level below this class in the stack trace.
        for (int i = 0; i < stackTraceElements.length; i++) {
            java.lang.StackTraceElement element = stackTraceElements[i];
            if (thisClassName.equals(element.getClassName()) && "includeAllPackagesUnderHere".equals(element.getMethodName())) {
                // We've found this class in the call stack. The calling class must be the
                // next class in the stack.
                callingClassName = stackTraceElements[i + 1].getClassName();
                break;
            }
        }
        java.lang.String packageName = android.test.suitebuilder.TestSuiteBuilder.parsePackageNameFromClassName(callingClassName);
        return includePackages(packageName);
    }

    /**
     * Override the default name for the suite being built. This should generally be called if you
     * call {@link #addRequirements(com.android.internal.util.Predicate[])} to make it clear which
     * tests will be included. The name you specify is automatically prefixed with the package
     * containing the tests to be run. If more than one package is specified, the first is used.
     *
     * @param newSuiteName
     * 		Prefix of name to give the suite being built.
     * @return The builder for method chaining.
     */
    public android.test.suitebuilder.TestSuiteBuilder named(java.lang.String newSuiteName) {
        suiteName = newSuiteName;
        return this;
    }

    /**
     * Call this method once you've configured your builder as desired.
     *
     * @return The suite containing the requested tests.
     */
    public final junit.framework.TestSuite build() {
        rootSuite = new junit.framework.TestSuite(getSuiteName());
        // Keep track of current class so we know when to create a new sub-suite.
        currentClassname = null;
        try {
            for (android.test.suitebuilder.TestMethod test : testGrouping.getTests()) {
                if (satisfiesAllPredicates(test)) {
                    addTest(test);
                }
            }
            if (testCases.size() > 0) {
                for (junit.framework.TestCase testCase : testCases) {
                    if (satisfiesAllPredicates(new android.test.suitebuilder.TestMethod(testCase))) {
                        addTest(testCase);
                    }
                }
            }
        } catch (java.lang.Exception exception) {
            android.util.Log.i("TestSuiteBuilder", "Failed to create test.", exception);
            junit.framework.TestSuite suite = new junit.framework.TestSuite(getSuiteName());
            suite.addTest(new android.test.suitebuilder.TestSuiteBuilder.FailedToCreateTests(exception));
            return suite;
        }
        return rootSuite;
    }

    /**
     * Subclasses use this method to determine the name of the suite.
     *
     * @return The package and suite name combined.
     */
    protected java.lang.String getSuiteName() {
        return suiteName;
    }

    /**
     * Exclude tests that fail to satisfy all of the given predicates. If you call this method, you
     * probably also want to call {@link #named(String)} to override the default suite name.
     *
     * @param predicates
     * 		Predicates to add to the list of requirements.
     * @return The builder for method chaining.
     */
    public final android.test.suitebuilder.TestSuiteBuilder addRequirements(com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>... predicates) {
        java.util.ArrayList<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>> list = new java.util.ArrayList<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>>();
        java.util.Collections.addAll(list, predicates);
        return addRequirements(list);
    }

    /**
     * A special {@link junit.framework.TestCase} used to indicate a failure during the build()
     * step.
     */
    public static class FailedToCreateTests extends junit.framework.TestCase {
        private final java.lang.Exception exception;

        public FailedToCreateTests(java.lang.Exception exception) {
            super("testSuiteConstructionFailed");
            this.exception = exception;
        }

        public void testSuiteConstructionFailed() {
            throw new java.lang.RuntimeException("Exception during suite construction", exception);
        }
    }

    /**
     *
     *
     * @return the test package that represents the packages that were included for our test suite.

    {@hide } Not needed for 1.0 SDK.
     */
    protected android.test.suitebuilder.TestGrouping getTestGrouping() {
        return testGrouping;
    }

    private boolean satisfiesAllPredicates(android.test.suitebuilder.TestMethod test) {
        for (com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> predicate : predicates) {
            if (!predicate.apply(test)) {
                return false;
            }
        }
        return true;
    }

    private void addTest(android.test.suitebuilder.TestMethod testMethod) throws java.lang.Exception {
        addSuiteIfNecessary(testMethod.getEnclosingClassname());
        suiteForCurrentClass.addTest(testMethod.createTest());
    }

    private void addTest(junit.framework.Test test) {
        addSuiteIfNecessary(test.getClass().getName());
        suiteForCurrentClass.addTest(test);
    }

    private void addSuiteIfNecessary(java.lang.String parentClassname) {
        if (!parentClassname.equals(currentClassname)) {
            currentClassname = parentClassname;
            suiteForCurrentClass = new junit.framework.TestSuite(parentClassname);
            rootSuite.addTest(suiteForCurrentClass);
        }
    }

    private static java.lang.String parsePackageNameFromClassName(java.lang.String className) {
        return className.substring(0, className.lastIndexOf('.'));
    }
}

