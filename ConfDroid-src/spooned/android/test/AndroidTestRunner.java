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
 *
 *
 * @deprecated Use
<a href="{@docRoot }reference/android/support/test/runner/AndroidJUnitRunner.html">
AndroidJUnitRunner</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class AndroidTestRunner extends junit.runner.BaseTestRunner {
    private junit.framework.TestResult mTestResult;

    private java.lang.String mTestClassName;

    private java.util.List<junit.framework.TestCase> mTestCases;

    private android.content.Context mContext;

    private boolean mSkipExecution = false;

    private java.util.List<junit.framework.TestListener> mTestListeners = com.google.android.collect.Lists.newArrayList();

    private android.app.Instrumentation mInstrumentation;

    private android.os.PerformanceCollector.PerformanceResultsWriter mPerfWriter;

    @java.lang.SuppressWarnings("unchecked")
    public void setTestClassName(java.lang.String testClassName, java.lang.String testMethodName) {
        java.lang.Class testClass = loadTestClass(testClassName);
        if (shouldRunSingleTestMethod(testMethodName, testClass)) {
            junit.framework.TestCase testCase = buildSingleTestMethod(testClass, testMethodName);
            mTestCases = com.google.android.collect.Lists.newArrayList(testCase);
            mTestClassName = testClass.getSimpleName();
        } else {
            setTest(getTest(testClass), testClass);
        }
    }

    public void setTest(junit.framework.Test test) {
        setTest(test, test.getClass());
    }

    private void setTest(junit.framework.Test test, java.lang.Class<? extends junit.framework.Test> testClass) {
        mTestCases = ((java.util.List<junit.framework.TestCase>) (android.test.TestCaseUtil.getTests(test, true)));
        if (junit.framework.TestSuite.class.isAssignableFrom(testClass)) {
            mTestClassName = android.test.TestCaseUtil.getTestName(test);
        } else {
            mTestClassName = testClass.getSimpleName();
        }
    }

    public void clearTestListeners() {
        mTestListeners.clear();
    }

    public void addTestListener(junit.framework.TestListener testListener) {
        if (testListener != null) {
            mTestListeners.add(testListener);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.lang.Class<? extends junit.framework.Test> loadTestClass(java.lang.String testClassName) {
        try {
            return ((java.lang.Class<? extends junit.framework.Test>) (mContext.getClassLoader().loadClass(testClassName)));
        } catch (java.lang.ClassNotFoundException e) {
            runFailed("Could not find test class. Class: " + testClassName);
        }
        return null;
    }

    private junit.framework.TestCase buildSingleTestMethod(java.lang.Class testClass, java.lang.String testMethodName) {
        try {
            java.lang.reflect.Constructor c = testClass.getConstructor();
            return newSingleTestMethod(testClass, testMethodName, c);
        } catch (java.lang.NoSuchMethodException e) {
        }
        try {
            java.lang.reflect.Constructor c = testClass.getConstructor(java.lang.String.class);
            return newSingleTestMethod(testClass, testMethodName, c, testMethodName);
        } catch (java.lang.NoSuchMethodException e) {
        }
        return null;
    }

    private junit.framework.TestCase newSingleTestMethod(java.lang.Class testClass, java.lang.String testMethodName, java.lang.reflect.Constructor constructor, java.lang.Object... args) {
        try {
            junit.framework.TestCase testCase = ((junit.framework.TestCase) (constructor.newInstance(args)));
            testCase.setName(testMethodName);
            return testCase;
        } catch (java.lang.IllegalAccessException e) {
            runFailed("Could not access test class. Class: " + testClass.getName());
        } catch (java.lang.InstantiationException e) {
            runFailed("Could not instantiate test class. Class: " + testClass.getName());
        } catch (java.lang.IllegalArgumentException e) {
            runFailed("Illegal argument passed to constructor. Class: " + testClass.getName());
        } catch (java.lang.reflect.InvocationTargetException e) {
            runFailed("Constructor thew an exception. Class: " + testClass.getName());
        }
        return null;
    }

    private boolean shouldRunSingleTestMethod(java.lang.String testMethodName, java.lang.Class<? extends junit.framework.Test> testClass) {
        return (testMethodName != null) && junit.framework.TestCase.class.isAssignableFrom(testClass);
    }

    private junit.framework.Test getTest(java.lang.Class clazz) {
        if (android.test.TestSuiteProvider.class.isAssignableFrom(clazz)) {
            try {
                android.test.TestSuiteProvider testSuiteProvider = ((android.test.TestSuiteProvider) (clazz.getConstructor().newInstance()));
                return testSuiteProvider.getTestSuite();
            } catch (java.lang.InstantiationException e) {
                runFailed("Could not instantiate test suite provider. Class: " + clazz.getName());
            } catch (java.lang.IllegalAccessException e) {
                runFailed("Illegal access of test suite provider. Class: " + clazz.getName());
            } catch (java.lang.reflect.InvocationTargetException e) {
                runFailed("Invocation exception test suite provider. Class: " + clazz.getName());
            } catch (java.lang.NoSuchMethodException e) {
                runFailed("No such method on test suite provider. Class: " + clazz.getName());
            }
        }
        return getTest(clazz.getName());
    }

    protected junit.framework.TestResult createTestResult() {
        if (mSkipExecution) {
            return new android.test.NoExecTestResult();
        }
        return new junit.framework.TestResult();
    }

    void setSkipExecution(boolean skip) {
        mSkipExecution = skip;
    }

    public java.util.List<junit.framework.TestCase> getTestCases() {
        return mTestCases;
    }

    public java.lang.String getTestClassName() {
        return mTestClassName;
    }

    public junit.framework.TestResult getTestResult() {
        return mTestResult;
    }

    public void runTest() {
        runTest(createTestResult());
    }

    public void runTest(junit.framework.TestResult testResult) {
        mTestResult = testResult;
        for (junit.framework.TestListener testListener : mTestListeners) {
            mTestResult.addListener(testListener);
        }
        android.content.Context testContext = (mInstrumentation == null) ? mContext : mInstrumentation.getContext();
        for (junit.framework.TestCase testCase : mTestCases) {
            setContextIfAndroidTestCase(testCase, mContext, testContext);
            setInstrumentationIfInstrumentationTestCase(testCase, mInstrumentation);
            setPerformanceWriterIfPerformanceCollectorTestCase(testCase, mPerfWriter);
            testCase.run(mTestResult);
        }
    }

    private void setContextIfAndroidTestCase(junit.framework.Test test, android.content.Context context, android.content.Context testContext) {
        if (android.test.AndroidTestCase.class.isAssignableFrom(test.getClass())) {
            ((android.test.AndroidTestCase) (test)).setContext(context);
            ((android.test.AndroidTestCase) (test)).setTestContext(testContext);
        }
    }

    public void setContext(android.content.Context context) {
        mContext = context;
    }

    private void setInstrumentationIfInstrumentationTestCase(junit.framework.Test test, android.app.Instrumentation instrumentation) {
        if (android.test.InstrumentationTestCase.class.isAssignableFrom(test.getClass())) {
            ((android.test.InstrumentationTestCase) (test)).injectInstrumentation(instrumentation);
        }
    }

    private void setPerformanceWriterIfPerformanceCollectorTestCase(junit.framework.Test test, android.os.PerformanceCollector.PerformanceResultsWriter writer) {
        if (android.test.PerformanceCollectorTestCase.class.isAssignableFrom(test.getClass())) {
            ((android.test.PerformanceCollectorTestCase) (test)).setPerformanceResultsWriter(writer);
        }
    }

    public void setInstrumentation(android.app.Instrumentation instrumentation) {
        mInstrumentation = instrumentation;
    }

    /**
     *
     *
     * @deprecated Incorrect spelling,
    use {@link #setInstrumentation(android.app.Instrumentation)} instead.
     */
    @java.lang.Deprecated
    public void setInstrumentaiton(android.app.Instrumentation instrumentation) {
        setInstrumentation(instrumentation);
    }

    /**
     * {@hide } Pending approval for public API.
     */
    public void setPerformanceResultsWriter(android.os.PerformanceCollector.PerformanceResultsWriter writer) {
        mPerfWriter = writer;
    }

    @java.lang.Override
    protected java.lang.Class loadSuiteClass(java.lang.String suiteClassName) throws java.lang.ClassNotFoundException {
        return mContext.getClassLoader().loadClass(suiteClassName);
    }

    public void testStarted(java.lang.String testName) {
    }

    public void testEnded(java.lang.String testName) {
    }

    public void testFailed(int status, junit.framework.Test test, java.lang.Throwable t) {
    }

    protected void runFailed(java.lang.String message) {
        throw new java.lang.RuntimeException(message);
    }
}

