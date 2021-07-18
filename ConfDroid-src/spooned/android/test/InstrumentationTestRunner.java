/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.test;


/**
 * An {@link Instrumentation} that runs various types of {@link junit.framework.TestCase}s against
 * an Android package (application).
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about application testing, read the
 * <a href="{@docRoot }guide/topics/testing/index.html">Testing</a> developer guide.</p>
 * </div>
 *
 * <h3>Typical Usage</h3>
 * <ol>
 * <li>Write {@link junit.framework.TestCase}s that perform unit, functional, or performance tests
 * against the classes in your package.  Typically these are subclassed from:
 *   <ul><li>{@link android.test.ActivityInstrumentationTestCase2}</li>
 *   <li>{@link android.test.ActivityUnitTestCase}</li>
 *   <li>{@link android.test.AndroidTestCase}</li>
 *   <li>{@link android.test.ApplicationTestCase}</li>
 *   <li>{@link android.test.InstrumentationTestCase}</li>
 *   <li>{@link android.test.ProviderTestCase}</li>
 *   <li>{@link android.test.ServiceTestCase}</li>
 *   <li>{@link android.test.SingleLaunchActivityTestCase}</li></ul>
 * <li>Set the <code>android:targetPackage</code> attribute of the <code>&lt;instrumentation&gt;</code>
 * element in the test package's manifest. You should set the attribute value
 * to the package name of the target application under test.
 * <li>Run the instrumentation using "adb shell am instrument -w",
 * with no optional arguments, to run all tests (except performance tests).
 * <li>Run the instrumentation using "adb shell am instrument -w",
 * with the argument '-e func true' to run all functional tests. These are tests that derive from
 * {@link android.test.InstrumentationTestCase}.
 * <li>Run the instrumentation using "adb shell am instrument -w",
 * with the argument '-e unit true' to run all unit tests. These are tests that <i>do not</i>derive
 * from {@link android.test.InstrumentationTestCase} (and are not performance tests).
 * <li>Run the instrumentation using "adb shell am instrument -w",
 * with the argument '-e class' set to run an individual {@link junit.framework.TestCase}.
 * </ol>
 * <p/>
 * <b>Running all tests:</b> adb shell am instrument -w
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running all small tests:</b> adb shell am instrument -w
 * -e size small
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running all medium tests:</b> adb shell am instrument -w
 * -e size medium
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running all large tests:</b> adb shell am instrument -w
 * -e size large
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Filter test run to tests with given annotation:</b> adb shell am instrument -w
 * -e annotation com.android.foo.MyAnnotation
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * If used with other options, the resulting test run will contain the union of the two options.
 * e.g. "-e size large -e annotation com.android.foo.MyAnnotation" will run only tests with both
 * the {@link LargeTest} and "com.android.foo.MyAnnotation" annotations.
 * <p/>
 * <b>Filter test run to tests <i>without</i> given annotation:</b> adb shell am instrument -w
 * -e notAnnotation com.android.foo.MyAnnotation
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running a single testcase:</b> adb shell am instrument -w
 * -e class com.android.foo.FooTest
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running a single test:</b> adb shell am instrument -w
 * -e class com.android.foo.FooTest#testFoo
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running multiple tests:</b> adb shell am instrument -w
 * -e class com.android.foo.FooTest,com.android.foo.TooTest
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Running all tests in a java package:</b> adb shell am instrument -w
 * -e package com.android.foo.subpkg
 *  com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>Including performance tests:</b> adb shell am instrument -w
 * -e perf true
 * com.android.foo/android.test.InstrumentationTestRunner
 * <p/>
 * <b>To debug your tests, set a break point in your code and pass:</b>
 * -e debug true
 * <p/>
 * <b>To run in 'log only' mode</b>
 * -e log true
 * This option will load and iterate through all test classes and methods, but will bypass actual
 * test execution. Useful for quickly obtaining info on the tests to be executed by an
 * instrumentation command.
 * <p/>
 * <b>To generate EMMA code coverage:</b>
 * -e coverage true
 * Note: this requires an emma instrumented build. By default, the code coverage results file
 * will be saved in a /data/<app>/coverage.ec file, unless overridden by coverageFile flag (see
 * below)
 * <p/>
 * <b> To specify EMMA code coverage results file path:</b>
 * -e coverageFile /sdcard/myFile.ec
 * <br/>
 * in addition to the other arguments.
 *
 * @deprecated Use
<a href="{@docRoot }reference/android/support/test/runner/AndroidJUnitRunner.html">
AndroidJUnitRunner</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
/* (not JavaDoc)
Although not necessary in most case, another way to use this class is to extend it and have the
derived class return the desired test suite from the {@link #getTestSuite()} method. The test
suite returned from this method will be used if no target class is defined in the meta-data or
command line argument parameters. If a derived class is used it needs to be added as an
instrumentation to the AndroidManifest.xml and the command to run it would look like:
<p/>
adb shell am instrument -w com.android.foo/<i>com.android.FooInstrumentationTestRunner</i>
<p/>
Where <i>com.android.FooInstrumentationTestRunner</i> is the derived class.

This model is used by many existing app tests, but can probably be deprecated.
 */
@java.lang.Deprecated
public class InstrumentationTestRunner extends android.app.Instrumentation implements android.test.TestSuiteProvider {
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ARGUMENT_TEST_CLASS = "class";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ARGUMENT_TEST_PACKAGE = "package";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ARGUMENT_TEST_SIZE_PREDICATE = "size";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ARGUMENT_DELAY_MSEC = "delay_msec";

    private static final java.lang.String SMALL_SUITE = "small";

    private static final java.lang.String MEDIUM_SUITE = "medium";

    private static final java.lang.String LARGE_SUITE = "large";

    private static final java.lang.String ARGUMENT_LOG_ONLY = "log";

    /**
     *
     *
     * @unknown 
     */
    static final java.lang.String ARGUMENT_ANNOTATION = "annotation";

    /**
     *
     *
     * @unknown 
     */
    static final java.lang.String ARGUMENT_NOT_ANNOTATION = "notAnnotation";

    /**
     * This constant defines the maximum allowed runtime (in ms) for a test included in the "small"
     * suite. It is used to make an educated guess at what suite an unlabeled test belongs.
     */
    private static final float SMALL_SUITE_MAX_RUNTIME = 100;

    /**
     * This constant defines the maximum allowed runtime (in ms) for a test included in the
     * "medium" suite. It is used to make an educated guess at what suite an unlabeled test belongs.
     */
    private static final float MEDIUM_SUITE_MAX_RUNTIME = 1000;

    /**
     * The following keys are used in the status bundle to provide structured reports to
     * an IInstrumentationWatcher.
     */
    /**
     * This value, if stored with key {@link android.app.Instrumentation#REPORT_KEY_IDENTIFIER},
     * identifies InstrumentationTestRunner as the source of the report.  This is sent with all
     * status messages.
     */
    public static final java.lang.String REPORT_VALUE_ID = "InstrumentationTestRunner";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the total number of tests that are being run.  This is sent with all status
     * messages.
     */
    public static final java.lang.String REPORT_KEY_NUM_TOTAL = "numtests";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the sequence number of the current test.  This is sent with any status message
     * describing a specific test being started or completed.
     */
    public static final java.lang.String REPORT_KEY_NUM_CURRENT = "current";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the name of the current test class.  This is sent with any status message
     * describing a specific test being started or completed.
     */
    public static final java.lang.String REPORT_KEY_NAME_CLASS = "class";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the name of the current test.  This is sent with any status message
     * describing a specific test being started or completed.
     */
    public static final java.lang.String REPORT_KEY_NAME_TEST = "test";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * reports the run time in seconds of the current test.
     */
    private static final java.lang.String REPORT_KEY_RUN_TIME = "runtime";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * reports the number of total iterations of the current test.
     */
    private static final java.lang.String REPORT_KEY_NUM_ITERATIONS = "numiterations";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * reports the guessed suite assignment for the current test.
     */
    private static final java.lang.String REPORT_KEY_SUITE_ASSIGNMENT = "suiteassignment";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the path to the generated code coverage file.
     */
    private static final java.lang.String REPORT_KEY_COVERAGE_PATH = "coverageFilePath";

    /**
     * The test is starting.
     */
    public static final int REPORT_VALUE_RESULT_START = 1;

    /**
     * The test completed successfully.
     */
    public static final int REPORT_VALUE_RESULT_OK = 0;

    /**
     * The test completed with an error.
     */
    public static final int REPORT_VALUE_RESULT_ERROR = -1;

    /**
     * The test completed with a failure.
     */
    public static final int REPORT_VALUE_RESULT_FAILURE = -2;

    /**
     * If included in the status bundle sent to an IInstrumentationWatcher, this key
     * identifies a stack trace describing an error or failure.  This is sent with any status
     * message describing a specific test being completed.
     */
    public static final java.lang.String REPORT_KEY_STACK = "stack";

    // Default file name for code coverage
    private static final java.lang.String DEFAULT_COVERAGE_FILE_NAME = "coverage.ec";

    private static final java.lang.String LOG_TAG = "InstrumentationTestRunner";

    private final android.os.Bundle mResults = new android.os.Bundle();

    private android.os.Bundle mArguments;

    private android.test.AndroidTestRunner mTestRunner;

    private boolean mDebug;

    private boolean mJustCount;

    private boolean mSuiteAssignmentMode;

    private int mTestCount;

    private java.lang.String mPackageOfTests;

    private boolean mCoverage;

    private java.lang.String mCoverageFilePath;

    private int mDelayMsec;

    @java.lang.Override
    public void onCreate(android.os.Bundle arguments) {
        super.onCreate(arguments);
        mArguments = arguments;
        // Apk paths used to search for test classes when using TestSuiteBuilders.
        java.lang.String[] apkPaths = new java.lang.String[]{ getTargetContext().getPackageCodePath(), getContext().getPackageCodePath() };
        android.test.ClassPathPackageInfoSource.setApkPaths(apkPaths);
        com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> testSizePredicate = null;
        com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> testAnnotationPredicate = null;
        com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> testNotAnnotationPredicate = null;
        java.lang.String testClassesArg = null;
        boolean logOnly = false;
        if (arguments != null) {
            // Test class name passed as an argument should override any meta-data declaration.
            testClassesArg = arguments.getString(android.test.InstrumentationTestRunner.ARGUMENT_TEST_CLASS);
            mDebug = getBooleanArgument(arguments, "debug");
            mJustCount = getBooleanArgument(arguments, "count");
            mSuiteAssignmentMode = getBooleanArgument(arguments, "suiteAssignment");
            mPackageOfTests = arguments.getString(android.test.InstrumentationTestRunner.ARGUMENT_TEST_PACKAGE);
            testSizePredicate = getSizePredicateFromArg(arguments.getString(android.test.InstrumentationTestRunner.ARGUMENT_TEST_SIZE_PREDICATE));
            testAnnotationPredicate = getAnnotationPredicate(arguments.getString(android.test.InstrumentationTestRunner.ARGUMENT_ANNOTATION));
            testNotAnnotationPredicate = getNotAnnotationPredicate(arguments.getString(android.test.InstrumentationTestRunner.ARGUMENT_NOT_ANNOTATION));
            logOnly = getBooleanArgument(arguments, android.test.InstrumentationTestRunner.ARGUMENT_LOG_ONLY);
            mCoverage = getBooleanArgument(arguments, "coverage");
            mCoverageFilePath = arguments.getString("coverageFile");
            try {
                java.lang.Object delay = arguments.get(android.test.InstrumentationTestRunner.ARGUMENT_DELAY_MSEC);// Accept either string or int

                if (delay != null)
                    mDelayMsec = java.lang.Integer.parseInt(delay.toString());

            } catch (java.lang.NumberFormatException e) {
                android.util.Log.e(android.test.InstrumentationTestRunner.LOG_TAG, "Invalid delay_msec parameter", e);
            }
        }
        android.test.suitebuilder.TestSuiteBuilder testSuiteBuilder = new android.test.suitebuilder.TestSuiteBuilder(getClass().getName(), getTargetContext().getClassLoader());
        if (testSizePredicate != null) {
            testSuiteBuilder.addRequirements(testSizePredicate);
        }
        if (testAnnotationPredicate != null) {
            testSuiteBuilder.addRequirements(testAnnotationPredicate);
        }
        if (testNotAnnotationPredicate != null) {
            testSuiteBuilder.addRequirements(testNotAnnotationPredicate);
        }
        if (testClassesArg == null) {
            if (mPackageOfTests != null) {
                testSuiteBuilder.includePackages(mPackageOfTests);
            } else {
                junit.framework.TestSuite testSuite = getTestSuite();
                if (testSuite != null) {
                    testSuiteBuilder.addTestSuite(testSuite);
                } else {
                    // no package or class bundle arguments were supplied, and no test suite
                    // provided so add all tests in application
                    testSuiteBuilder.includePackages("");
                }
            }
        } else {
            parseTestClasses(testClassesArg, testSuiteBuilder);
        }
        testSuiteBuilder.addRequirements(getBuilderRequirements());
        mTestRunner = getAndroidTestRunner();
        mTestRunner.setContext(getTargetContext());
        mTestRunner.setInstrumentation(this);
        mTestRunner.setSkipExecution(logOnly);
        mTestRunner.setTest(testSuiteBuilder.build());
        mTestCount = mTestRunner.getTestCases().size();
        if (mSuiteAssignmentMode) {
            mTestRunner.addTestListener(new android.test.InstrumentationTestRunner.SuiteAssignmentPrinter());
        } else {
            android.test.InstrumentationTestRunner.WatcherResultPrinter resultPrinter = new android.test.InstrumentationTestRunner.WatcherResultPrinter(mTestCount);
            mTestRunner.addTestListener(new android.test.TestPrinter("TestRunner", false));
            mTestRunner.addTestListener(resultPrinter);
            mTestRunner.setPerformanceResultsWriter(resultPrinter);
        }
        start();
    }

    /**
     * Get the arguments passed to this instrumentation.
     *
     * @return the Bundle object
     */
    public android.os.Bundle getArguments() {
        return mArguments;
    }

    /**
     * Add a {@link TestListener}
     *
     * @unknown 
     */
    protected void addTestListener(junit.framework.TestListener listener) {
        if ((mTestRunner != null) && (listener != null)) {
            mTestRunner.addTestListener(listener);
        }
    }

    java.util.List<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>> getBuilderRequirements() {
        return new java.util.ArrayList<com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod>>();
    }

    /**
     * Parses and loads the specified set of test classes
     *
     * @param testClassArg
     * 		- comma-separated list of test classes and methods
     * @param testSuiteBuilder
     * 		- builder to add tests to
     */
    private void parseTestClasses(java.lang.String testClassArg, android.test.suitebuilder.TestSuiteBuilder testSuiteBuilder) {
        java.lang.String[] testClasses = testClassArg.split(",");
        for (java.lang.String testClass : testClasses) {
            parseTestClass(testClass, testSuiteBuilder);
        }
    }

    /**
     * Parse and load the given test class and, optionally, method
     *
     * @param testClassName
     * 		- full package name of test class and optionally method to add.
     * 		Expected format: com.android.TestClass#testMethod
     * @param testSuiteBuilder
     * 		- builder to add tests to
     */
    private void parseTestClass(java.lang.String testClassName, android.test.suitebuilder.TestSuiteBuilder testSuiteBuilder) {
        int methodSeparatorIndex = testClassName.indexOf('#');
        java.lang.String testMethodName = null;
        if (methodSeparatorIndex > 0) {
            testMethodName = testClassName.substring(methodSeparatorIndex + 1);
            testClassName = testClassName.substring(0, methodSeparatorIndex);
        }
        testSuiteBuilder.addTestClassByName(testClassName, testMethodName, getTargetContext());
    }

    protected android.test.AndroidTestRunner getAndroidTestRunner() {
        return new android.test.AndroidTestRunner();
    }

    private boolean getBooleanArgument(android.os.Bundle arguments, java.lang.String tag) {
        java.lang.String tagString = arguments.getString(tag);
        return (tagString != null) && java.lang.Boolean.parseBoolean(tagString);
    }

    /* Returns the size predicate object, corresponding to the "size" argument value. */
    private com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> getSizePredicateFromArg(java.lang.String sizeArg) {
        if (android.test.InstrumentationTestRunner.SMALL_SUITE.equals(sizeArg)) {
            return android.test.suitebuilder.TestPredicates.SELECT_SMALL;
        } else
            if (android.test.InstrumentationTestRunner.MEDIUM_SUITE.equals(sizeArg)) {
                return android.test.suitebuilder.TestPredicates.SELECT_MEDIUM;
            } else
                if (android.test.InstrumentationTestRunner.LARGE_SUITE.equals(sizeArg)) {
                    return android.test.suitebuilder.TestPredicates.SELECT_LARGE;
                } else {
                    return null;
                }


    }

    /**
     * Returns the test predicate object, corresponding to the annotation class value provided via
     * the {@link ARGUMENT_ANNOTATION} argument.
     *
     * @return the predicate or <code>null</code>
     */
    private com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> getAnnotationPredicate(java.lang.String annotationClassName) {
        java.lang.Class<? extends java.lang.annotation.Annotation> annotationClass = getAnnotationClass(annotationClassName);
        if (annotationClass != null) {
            return new android.test.suitebuilder.annotation.HasAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * Returns the negative test predicate object, corresponding to the annotation class value
     * provided via the {@link ARGUMENT_NOT_ANNOTATION} argument.
     *
     * @return the predicate or <code>null</code>
     */
    private com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> getNotAnnotationPredicate(java.lang.String annotationClassName) {
        java.lang.Class<? extends java.lang.annotation.Annotation> annotationClass = getAnnotationClass(annotationClassName);
        if (annotationClass != null) {
            return com.android.internal.util.Predicates.not(new android.test.suitebuilder.annotation.HasAnnotation(annotationClass));
        }
        return null;
    }

    /**
     * Helper method to return the annotation class with specified name
     *
     * @param annotationClassName
     * 		the fully qualified name of the class
     * @return the annotation class or <code>null</code>
     */
    private java.lang.Class<? extends java.lang.annotation.Annotation> getAnnotationClass(java.lang.String annotationClassName) {
        if (annotationClassName == null) {
            return null;
        }
        try {
            java.lang.Class<?> annotationClass = java.lang.Class.forName(annotationClassName);
            if (annotationClass.isAnnotation()) {
                return ((java.lang.Class<? extends java.lang.annotation.Annotation>) (annotationClass));
            } else {
                android.util.Log.e(android.test.InstrumentationTestRunner.LOG_TAG, java.lang.String.format("Provided annotation value %s is not an Annotation", annotationClassName));
            }
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.e(android.test.InstrumentationTestRunner.LOG_TAG, java.lang.String.format("Could not find class for specified annotation %s", annotationClassName));
        }
        return null;
    }

    /**
     * Initialize the current thread as a looper.
     * <p/>
     * Exposed for unit testing.
     */
    void prepareLooper() {
        android.os.Looper.prepare();
    }

    @java.lang.Override
    public void onStart() {
        prepareLooper();
        if (mJustCount) {
            mResults.putString(android.app.Instrumentation.REPORT_KEY_IDENTIFIER, android.test.InstrumentationTestRunner.REPORT_VALUE_ID);
            mResults.putInt(android.test.InstrumentationTestRunner.REPORT_KEY_NUM_TOTAL, mTestCount);
            finish(android.app.Activity.RESULT_OK, mResults);
        } else {
            if (mDebug) {
                android.os.Debug.waitForDebugger();
            }
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            java.io.PrintStream writer = new java.io.PrintStream(byteArrayOutputStream);
            try {
                android.test.InstrumentationTestRunner.StringResultPrinter resultPrinter = new android.test.InstrumentationTestRunner.StringResultPrinter(writer);
                mTestRunner.addTestListener(resultPrinter);
                long startTime = java.lang.System.currentTimeMillis();
                mTestRunner.runTest();
                long runTime = java.lang.System.currentTimeMillis() - startTime;
                resultPrinter.printResult(mTestRunner.getTestResult(), runTime);
            } catch (java.lang.Throwable t) {
                // catch all exceptions so a more verbose error message can be outputted
                writer.println(java.lang.String.format("Test run aborted due to unexpected exception: %s", t.getMessage()));
                t.printStackTrace(writer);
            } finally {
                mResults.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, java.lang.String.format("\nTest results for %s=%s", mTestRunner.getTestClassName(), byteArrayOutputStream.toString()));
                if (mCoverage) {
                    generateCoverageReport();
                }
                writer.close();
                finish(android.app.Activity.RESULT_OK, mResults);
            }
        }
    }

    public junit.framework.TestSuite getTestSuite() {
        return getAllTests();
    }

    /**
     * Override this to define all of the tests to run in your package.
     */
    public junit.framework.TestSuite getAllTests() {
        return null;
    }

    /**
     * Override this to provide access to the class loader of your package.
     */
    public java.lang.ClassLoader getLoader() {
        return null;
    }

    private void generateCoverageReport() {
        // use reflection to call emma dump coverage method, to avoid
        // always statically compiling against emma jar
        java.lang.String coverageFilePath = getCoverageFilePath();
        java.io.File coverageFile = new java.io.File(coverageFilePath);
        try {
            java.lang.Class<?> emmaRTClass = java.lang.Class.forName("com.vladium.emma.rt.RT");
            java.lang.reflect.Method dumpCoverageMethod = emmaRTClass.getMethod("dumpCoverageData", coverageFile.getClass(), boolean.class, boolean.class);
            dumpCoverageMethod.invoke(null, coverageFile, false, false);
            // output path to generated coverage file so it can be parsed by a test harness if
            // needed
            mResults.putString(android.test.InstrumentationTestRunner.REPORT_KEY_COVERAGE_PATH, coverageFilePath);
            // also output a more user friendly msg
            final java.lang.String currentStream = mResults.getString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT);
            mResults.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, java.lang.String.format("%s\nGenerated code coverage data to %s", currentStream, coverageFilePath));
        } catch (java.lang.ClassNotFoundException e) {
            reportEmmaError("Is emma jar on classpath?", e);
        } catch (java.lang.SecurityException e) {
            reportEmmaError(e);
        } catch (java.lang.NoSuchMethodException e) {
            reportEmmaError(e);
        } catch (java.lang.IllegalArgumentException e) {
            reportEmmaError(e);
        } catch (java.lang.IllegalAccessException e) {
            reportEmmaError(e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            reportEmmaError(e);
        }
    }

    private java.lang.String getCoverageFilePath() {
        if (mCoverageFilePath == null) {
            return (getTargetContext().getFilesDir().getAbsolutePath() + java.io.File.separator) + android.test.InstrumentationTestRunner.DEFAULT_COVERAGE_FILE_NAME;
        } else {
            return mCoverageFilePath;
        }
    }

    private void reportEmmaError(java.lang.Exception e) {
        reportEmmaError("", e);
    }

    private void reportEmmaError(java.lang.String hint, java.lang.Exception e) {
        java.lang.String msg = "Failed to generate emma coverage. " + hint;
        android.util.Log.e(android.test.InstrumentationTestRunner.LOG_TAG, msg, e);
        mResults.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, "\nError: " + msg);
    }

    // TODO kill this, use status() and prettyprint model for better output
    private class StringResultPrinter extends junit.textui.ResultPrinter {
        public StringResultPrinter(java.io.PrintStream writer) {
            super(writer);
        }

        public synchronized void printResult(junit.framework.TestResult result, long runTime) {
            printHeader(runTime);
            printFooter(result);
        }
    }

    /**
     * This class sends status reports back to the IInstrumentationWatcher about
     * which suite each test belongs.
     */
    private class SuiteAssignmentPrinter implements junit.framework.TestListener {
        private android.os.Bundle mTestResult;

        private long mStartTime;

        private long mEndTime;

        private boolean mTimingValid;

        public SuiteAssignmentPrinter() {
        }

        /**
         * send a status for the start of a each test, so long tests can be seen as "running"
         */
        public void startTest(junit.framework.Test test) {
            mTimingValid = true;
            mStartTime = java.lang.System.currentTimeMillis();
        }

        /**
         *
         *
         * @see junit.framework.TestListener#addError(Test, Throwable)
         */
        public void addError(junit.framework.Test test, java.lang.Throwable t) {
            mTimingValid = false;
        }

        /**
         *
         *
         * @see junit.framework.TestListener#addFailure(Test, AssertionFailedError)
         */
        public void addFailure(junit.framework.Test test, junit.framework.AssertionFailedError t) {
            mTimingValid = false;
        }

        /**
         *
         *
         * @see junit.framework.TestListener#endTest(Test)
         */
        public void endTest(junit.framework.Test test) {
            float runTime;
            java.lang.String assignmentSuite;
            mEndTime = java.lang.System.currentTimeMillis();
            mTestResult = new android.os.Bundle();
            if ((!mTimingValid) || (mStartTime < 0)) {
                assignmentSuite = "NA";
                runTime = -1;
            } else {
                runTime = mEndTime - mStartTime;
                if ((runTime < android.test.InstrumentationTestRunner.SMALL_SUITE_MAX_RUNTIME) && (!android.test.InstrumentationTestCase.class.isAssignableFrom(test.getClass()))) {
                    assignmentSuite = android.test.InstrumentationTestRunner.SMALL_SUITE;
                } else
                    if (runTime < android.test.InstrumentationTestRunner.MEDIUM_SUITE_MAX_RUNTIME) {
                        assignmentSuite = android.test.InstrumentationTestRunner.MEDIUM_SUITE;
                    } else {
                        assignmentSuite = android.test.InstrumentationTestRunner.LARGE_SUITE;
                    }

            }
            // Clear mStartTime so that we can verify that it gets set next time.
            mStartTime = -1;
            mTestResult.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, ((((((test.getClass().getName() + "#") + ((junit.framework.TestCase) (test)).getName()) + "\nin ") + assignmentSuite) + " suite\nrunTime: ") + java.lang.String.valueOf(runTime)) + "\n");
            mTestResult.putFloat(android.test.InstrumentationTestRunner.REPORT_KEY_RUN_TIME, runTime);
            mTestResult.putString(android.test.InstrumentationTestRunner.REPORT_KEY_SUITE_ASSIGNMENT, assignmentSuite);
            sendStatus(0, mTestResult);
        }
    }

    /**
     * This class sends status reports back to the IInstrumentationWatcher
     */
    // TODO report the end of the cycle
    private class WatcherResultPrinter implements android.os.PerformanceCollector.PerformanceResultsWriter , junit.framework.TestListener {
        private final android.os.Bundle mResultTemplate;

        android.os.Bundle mTestResult;

        int mTestNum = 0;

        int mTestResultCode = 0;

        java.lang.String mTestClass = null;

        android.os.PerformanceCollector mPerfCollector = new android.os.PerformanceCollector();

        boolean mIsTimedTest = false;

        boolean mIncludeDetailedStats = false;

        public WatcherResultPrinter(int numTests) {
            mResultTemplate = new android.os.Bundle();
            mResultTemplate.putString(android.app.Instrumentation.REPORT_KEY_IDENTIFIER, android.test.InstrumentationTestRunner.REPORT_VALUE_ID);
            mResultTemplate.putInt(android.test.InstrumentationTestRunner.REPORT_KEY_NUM_TOTAL, numTests);
        }

        /**
         * send a status for the start of a each test, so long tests can be seen
         * as "running"
         */
        public void startTest(junit.framework.Test test) {
            java.lang.String testClass = test.getClass().getName();
            java.lang.String testName = ((junit.framework.TestCase) (test)).getName();
            mTestResult = new android.os.Bundle(mResultTemplate);
            mTestResult.putString(android.test.InstrumentationTestRunner.REPORT_KEY_NAME_CLASS, testClass);
            mTestResult.putString(android.test.InstrumentationTestRunner.REPORT_KEY_NAME_TEST, testName);
            mTestResult.putInt(android.test.InstrumentationTestRunner.REPORT_KEY_NUM_CURRENT, ++mTestNum);
            // pretty printing
            if ((testClass != null) && (!testClass.equals(mTestClass))) {
                mTestResult.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, java.lang.String.format("\n%s:", testClass));
                mTestClass = testClass;
            } else {
                mTestResult.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, "");
            }
            java.lang.reflect.Method testMethod = null;
            try {
                testMethod = test.getClass().getMethod(testName);
                // Report total number of iterations, if test is repetitive
                if (testMethod.isAnnotationPresent(android.test.RepetitiveTest.class)) {
                    int numIterations = testMethod.getAnnotation(android.test.RepetitiveTest.class).numIterations();
                    mTestResult.putInt(android.test.InstrumentationTestRunner.REPORT_KEY_NUM_ITERATIONS, numIterations);
                }
            } catch (java.lang.NoSuchMethodException e) {
                // ignore- the test with given name does not exist. Will be handled during test
                // execution
            }
            // The delay_msec parameter is normally used to provide buffers of idle time
            // for power measurement purposes. To make sure there is a delay before and after
            // every test in a suite, we delay *after* every test (see endTest below) and also
            // delay *before* the first test. So, delay test1 delay test2 delay.
            try {
                if (mTestNum == 1)
                    java.lang.Thread.sleep(mDelayMsec);

            } catch (java.lang.InterruptedException e) {
                throw new java.lang.IllegalStateException(e);
            }
            sendStatus(android.test.InstrumentationTestRunner.REPORT_VALUE_RESULT_START, mTestResult);
            mTestResultCode = 0;
            mIsTimedTest = false;
            mIncludeDetailedStats = false;
            try {
                // Look for TimedTest annotation on both test class and test method
                if ((testMethod != null) && testMethod.isAnnotationPresent(android.test.TimedTest.class)) {
                    mIsTimedTest = true;
                    mIncludeDetailedStats = testMethod.getAnnotation(android.test.TimedTest.class).includeDetailedStats();
                } else
                    if (test.getClass().isAnnotationPresent(android.test.TimedTest.class)) {
                        mIsTimedTest = true;
                        mIncludeDetailedStats = test.getClass().getAnnotation(android.test.TimedTest.class).includeDetailedStats();
                    }

            } catch (java.lang.SecurityException e) {
                // ignore - the test with given name cannot be accessed. Will be handled during
                // test execution
            }
            if (mIsTimedTest && mIncludeDetailedStats) {
                mPerfCollector.beginSnapshot("");
            } else
                if (mIsTimedTest) {
                    mPerfCollector.startTiming("");
                }

        }

        /**
         *
         *
         * @see junit.framework.TestListener#addError(Test, Throwable)
         */
        public void addError(junit.framework.Test test, java.lang.Throwable t) {
            mTestResult.putString(android.test.InstrumentationTestRunner.REPORT_KEY_STACK, junit.runner.BaseTestRunner.getFilteredTrace(t));
            mTestResultCode = android.test.InstrumentationTestRunner.REPORT_VALUE_RESULT_ERROR;
            // pretty printing
            mTestResult.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, java.lang.String.format("\nError in %s:\n%s", ((junit.framework.TestCase) (test)).getName(), junit.runner.BaseTestRunner.getFilteredTrace(t)));
        }

        /**
         *
         *
         * @see junit.framework.TestListener#addFailure(Test, AssertionFailedError)
         */
        public void addFailure(junit.framework.Test test, junit.framework.AssertionFailedError t) {
            mTestResult.putString(android.test.InstrumentationTestRunner.REPORT_KEY_STACK, junit.runner.BaseTestRunner.getFilteredTrace(t));
            mTestResultCode = android.test.InstrumentationTestRunner.REPORT_VALUE_RESULT_FAILURE;
            // pretty printing
            mTestResult.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, java.lang.String.format("\nFailure in %s:\n%s", ((junit.framework.TestCase) (test)).getName(), junit.runner.BaseTestRunner.getFilteredTrace(t)));
        }

        /**
         *
         *
         * @see junit.framework.TestListener#endTest(Test)
         */
        public void endTest(junit.framework.Test test) {
            if (mIsTimedTest && mIncludeDetailedStats) {
                mTestResult.putAll(mPerfCollector.endSnapshot());
            } else
                if (mIsTimedTest) {
                    writeStopTiming(mPerfCollector.stopTiming(""));
                }

            if (mTestResultCode == 0) {
                mTestResult.putString(android.app.Instrumentation.REPORT_KEY_STREAMRESULT, ".");
            }
            sendStatus(mTestResultCode, mTestResult);
            try {
                // Sleep after every test, if specified
                java.lang.Thread.sleep(mDelayMsec);
            } catch (java.lang.InterruptedException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }

        public void writeBeginSnapshot(java.lang.String label) {
            // Do nothing
        }

        public void writeEndSnapshot(android.os.Bundle results) {
            // Copy all snapshot data fields into mResults, which is outputted
            // via Instrumentation.finish
            mResults.putAll(results);
        }

        public void writeStartTiming(java.lang.String label) {
            // Do nothing
        }

        public void writeStopTiming(android.os.Bundle results) {
            // Copy results into mTestResult by flattening list of iterations,
            // which is outputted via WatcherResultPrinter.endTest
            int i = 0;
            for (android.os.Parcelable p : results.getParcelableArrayList(android.os.PerformanceCollector.METRIC_KEY_ITERATIONS)) {
                android.os.Bundle iteration = ((android.os.Bundle) (p));
                java.lang.String index = ("iteration" + i) + ".";
                mTestResult.putString(index + android.os.PerformanceCollector.METRIC_KEY_LABEL, iteration.getString(android.os.PerformanceCollector.METRIC_KEY_LABEL));
                mTestResult.putLong(index + android.os.PerformanceCollector.METRIC_KEY_CPU_TIME, iteration.getLong(android.os.PerformanceCollector.METRIC_KEY_CPU_TIME));
                mTestResult.putLong(index + android.os.PerformanceCollector.METRIC_KEY_EXECUTION_TIME, iteration.getLong(android.os.PerformanceCollector.METRIC_KEY_EXECUTION_TIME));
                i++;
            }
        }

        public void writeMeasurement(java.lang.String label, long value) {
            mTestResult.putLong(label, value);
        }

        public void writeMeasurement(java.lang.String label, float value) {
            mTestResult.putFloat(label, value);
        }

        public void writeMeasurement(java.lang.String label, java.lang.String value) {
            mTestResult.putString(label, value);
        }
    }
}

