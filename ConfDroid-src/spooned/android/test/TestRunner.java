/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * Support class that actually runs a test. Android uses this class,
 * and you probably will not need to instantiate, extend, or call this
 * class yourself. See the full {@link android.test} package description
 * to learn more about testing Android applications.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
@java.lang.Deprecated
public class TestRunner implements android.test.PerformanceTestCase.Intermediates {
    public static final int REGRESSION = 0;

    public static final int PERFORMANCE = 1;

    public static final int PROFILING = 2;

    public static final int CLEARSCREEN = 0;

    private static final java.lang.String TAG = "TestHarness";

    private android.content.Context mContext;

    private int mMode = android.test.TestRunner.REGRESSION;

    private java.util.List<android.test.TestRunner.Listener> mListeners = com.google.android.collect.Lists.newArrayList();

    private int mPassed;

    private int mFailed;

    private int mInternalIterations;

    private long mStartTime;

    private long mEndTime;

    private java.lang.String mClassName;

    java.util.List<android.test.TestRunner.IntermediateTime> mIntermediates = null;

    private static java.lang.Class mRunnableClass;

    private static java.lang.Class mJUnitClass;

    static {
        try {
            android.test.TestRunner.mRunnableClass = java.lang.Class.forName("java.lang.Runnable", false, null);
            android.test.TestRunner.mJUnitClass = java.lang.Class.forName("junit.framework.TestCase", false, null);
        } catch (java.lang.ClassNotFoundException ex) {
            throw new java.lang.RuntimeException("shouldn't happen", ex);
        }
    }

    public class JunitTestSuite extends junit.framework.TestSuite implements junit.framework.TestListener {
        boolean mError = false;

        public JunitTestSuite() {
            super();
        }

        @java.lang.Override
        public void run(junit.framework.TestResult result) {
            result.addListener(this);
            super.run(result);
            result.removeListener(this);
        }

        /**
         * Implemented method of the interface TestListener which will listen for the
         * start of a test.
         *
         * @param test
         * 		
         */
        public void startTest(junit.framework.Test test) {
            started(test.toString());
        }

        /**
         * Implemented method of the interface TestListener which will listen for the
         * end of the test.
         *
         * @param test
         * 		
         */
        public void endTest(junit.framework.Test test) {
            finished(test.toString());
            if (!mError) {
                passed(test.toString());
            }
        }

        /**
         * Implemented method of the interface TestListener which will listen for an
         * mError while running the test.
         *
         * @param test
         * 		
         */
        public void addError(junit.framework.Test test, java.lang.Throwable t) {
            mError = true;
            failed(test.toString(), t);
        }

        public void addFailure(junit.framework.Test test, junit.framework.AssertionFailedError t) {
            mError = true;
            failed(test.toString(), t);
        }
    }

    /**
     * Listener.performance() 'intermediates' argument is a list of these.
     */
    public static class IntermediateTime {
        public IntermediateTime(java.lang.String name, long timeInNS) {
            this.name = name;
            this.timeInNS = timeInNS;
        }

        public java.lang.String name;

        public long timeInNS;
    }

    /**
     * Support class that receives status on test progress. You should not need to
     * extend this interface yourself.
     */
    public interface Listener {
        void started(java.lang.String className);

        void finished(java.lang.String className);

        void performance(java.lang.String className, long itemTimeNS, int iterations, java.util.List<android.test.TestRunner.IntermediateTime> itermediates);

        void passed(java.lang.String className);

        void failed(java.lang.String className, java.lang.Throwable execption);
    }

    public TestRunner(android.content.Context context) {
        mContext = context;
    }

    public void addListener(android.test.TestRunner.Listener listener) {
        mListeners.add(listener);
    }

    public void startProfiling() {
        java.io.File file = new java.io.File("/tmp/trace");
        file.mkdir();
        java.lang.String base = ("/tmp/trace/" + mClassName) + ".dmtrace";
        android.os.Debug.startMethodTracing(base, (8 * 1024) * 1024);
    }

    public void finishProfiling() {
        android.os.Debug.stopMethodTracing();
    }

    private void started(java.lang.String className) {
        int count = mListeners.size();
        for (int i = 0; i < count; i++) {
            mListeners.get(i).started(className);
        }
    }

    private void finished(java.lang.String className) {
        int count = mListeners.size();
        for (int i = 0; i < count; i++) {
            mListeners.get(i).finished(className);
        }
    }

    private void performance(java.lang.String className, long itemTimeNS, int iterations, java.util.List<android.test.TestRunner.IntermediateTime> intermediates) {
        int count = mListeners.size();
        for (int i = 0; i < count; i++) {
            mListeners.get(i).performance(className, itemTimeNS, iterations, intermediates);
        }
    }

    public void passed(java.lang.String className) {
        mPassed++;
        int count = mListeners.size();
        for (int i = 0; i < count; i++) {
            mListeners.get(i).passed(className);
        }
    }

    public void failed(java.lang.String className, java.lang.Throwable exception) {
        mFailed++;
        int count = mListeners.size();
        for (int i = 0; i < count; i++) {
            mListeners.get(i).failed(className, exception);
        }
    }

    public int passedCount() {
        return mPassed;
    }

    public int failedCount() {
        return mFailed;
    }

    public void run(java.lang.String[] classes) {
        for (java.lang.String cl : classes) {
            run(cl);
        }
    }

    public void setInternalIterations(int count) {
        mInternalIterations = count;
    }

    public void startTiming(boolean realTime) {
        if (realTime) {
            mStartTime = java.lang.System.currentTimeMillis();
        } else {
            mStartTime = android.os.SystemClock.currentThreadTimeMillis();
        }
    }

    public void addIntermediate(java.lang.String name) {
        addIntermediate(name, (java.lang.System.currentTimeMillis() - mStartTime) * 1000000);
    }

    public void addIntermediate(java.lang.String name, long timeInNS) {
        mIntermediates.add(new android.test.TestRunner.IntermediateTime(name, timeInNS));
    }

    public void finishTiming(boolean realTime) {
        if (realTime) {
            mEndTime = java.lang.System.currentTimeMillis();
        } else {
            mEndTime = android.os.SystemClock.currentThreadTimeMillis();
        }
    }

    public void setPerformanceMode(int mode) {
        mMode = mode;
    }

    private void missingTest(java.lang.String className, java.lang.Throwable e) {
        started(className);
        finished(className);
        failed(className, e);
    }

    /* This class determines if more suites are added to this class then adds all individual
    test classes to a test suite for run
     */
    public void run(java.lang.String className) {
        try {
            mClassName = className;
            java.lang.Class clazz = mContext.getClassLoader().loadClass(className);
            java.lang.reflect.Method method = android.test.TestRunner.getChildrenMethod(clazz);
            if (method != null) {
                java.lang.String[] children = android.test.TestRunner.getChildren(method);
                run(children);
            } else
                if (android.test.TestRunner.mRunnableClass.isAssignableFrom(clazz)) {
                    java.lang.Runnable test = ((java.lang.Runnable) (clazz.newInstance()));
                    android.test.TestCase testcase = null;
                    if (test instanceof android.test.TestCase) {
                        testcase = ((android.test.TestCase) (test));
                    }
                    java.lang.Throwable e = null;
                    boolean didSetup = false;
                    started(className);
                    try {
                        if (testcase != null) {
                            testcase.setUp(mContext);
                            didSetup = true;
                        }
                        if (mMode == android.test.TestRunner.PERFORMANCE) {
                            runInPerformanceMode(test, className, false, className);
                        } else
                            if (mMode == android.test.TestRunner.PROFILING) {
                                // Need a way to mark a test to be run in profiling mode or not.
                                startProfiling();
                                test.run();
                                finishProfiling();
                            } else {
                                test.run();
                            }

                    } catch (java.lang.Throwable ex) {
                        e = ex;
                    }
                    if ((testcase != null) && didSetup) {
                        try {
                            testcase.tearDown();
                        } catch (java.lang.Throwable ex) {
                            e = ex;
                        }
                    }
                    finished(className);
                    if (e == null) {
                        passed(className);
                    } else {
                        failed(className, e);
                    }
                } else
                    if (android.test.TestRunner.mJUnitClass.isAssignableFrom(clazz)) {
                        java.lang.Throwable e = null;
                        // Create a Junit Suite.
                        android.test.TestRunner.JunitTestSuite suite = new android.test.TestRunner.JunitTestSuite();
                        java.lang.reflect.Method[] methods = android.test.TestRunner.getAllTestMethods(clazz);
                        for (java.lang.reflect.Method m : methods) {
                            junit.framework.TestCase test = ((junit.framework.TestCase) (clazz.newInstance()));
                            test.setName(m.getName());
                            if (test instanceof android.test.AndroidTestCase) {
                                android.test.AndroidTestCase testcase = ((android.test.AndroidTestCase) (test));
                                try {
                                    testcase.setContext(mContext);
                                    testcase.setTestContext(mContext);
                                } catch (java.lang.Exception ex) {
                                    android.util.Log.i("TestHarness", ex.toString());
                                }
                            }
                            suite.addTest(test);
                        }
                        if (mMode == android.test.TestRunner.PERFORMANCE) {
                            final int testCount = suite.testCount();
                            for (int j = 0; j < testCount; j++) {
                                junit.framework.Test test = suite.testAt(j);
                                started(test.toString());
                                try {
                                    runInPerformanceMode(test, className, true, test.toString());
                                } catch (java.lang.Throwable ex) {
                                    e = ex;
                                }
                                finished(test.toString());
                                if (e == null) {
                                    passed(test.toString());
                                } else {
                                    failed(test.toString(), e);
                                }
                            }
                        } else
                            if (mMode == android.test.TestRunner.PROFILING) {
                                // Need a way to mark a test to be run in profiling mode or not.
                                startProfiling();
                                junit.textui.TestRunner.run(suite);
                                finishProfiling();
                            } else {
                                junit.textui.TestRunner.run(suite);
                            }

                    } else {
                        java.lang.System.out.println(("Test wasn't Runnable and didn't have a" + " children method: ") + className);
                    }


        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.e("ClassNotFoundException for " + className, e.toString());
            if (isJunitTest(className)) {
                runSingleJunitTest(className);
            } else {
                missingTest(className, e);
            }
        } catch (java.lang.InstantiationException e) {
            java.lang.System.out.println("InstantiationException for " + className);
            missingTest(className, e);
        } catch (java.lang.IllegalAccessException e) {
            java.lang.System.out.println("IllegalAccessException for " + className);
            missingTest(className, e);
        }
    }

    public void runInPerformanceMode(java.lang.Object testCase, java.lang.String className, boolean junitTest, java.lang.String testNameInDb) throws java.lang.Exception {
        boolean increaseIterations = true;
        int iterations = 1;
        long duration = 0;
        mIntermediates = null;
        mInternalIterations = 1;
        java.lang.Class clazz = mContext.getClassLoader().loadClass(className);
        java.lang.Object perftest = clazz.newInstance();
        android.test.PerformanceTestCase perftestcase = null;
        if (perftest instanceof android.test.PerformanceTestCase) {
            perftestcase = ((android.test.PerformanceTestCase) (perftest));
            // only run the test if it is not marked as a performance only test
            if ((mMode == android.test.TestRunner.REGRESSION) && perftestcase.isPerformanceOnly())
                return;

        }
        // First force GCs, to avoid GCs happening during out
        // test and skewing its time.
        java.lang.Runtime.getRuntime().runFinalization();
        java.lang.Runtime.getRuntime().gc();
        if (perftestcase != null) {
            mIntermediates = new java.util.ArrayList<android.test.TestRunner.IntermediateTime>();
            iterations = perftestcase.startPerformance(this);
            if (iterations > 0) {
                increaseIterations = false;
            } else {
                iterations = 1;
            }
        }
        // Pause briefly to let things settle down...
        java.lang.Thread.sleep(1000);
        do {
            mEndTime = 0;
            if (increaseIterations) {
                // Test case does not implement
                // PerformanceTestCase or returned 0 iterations,
                // so we take care of measure the whole test time.
                mStartTime = android.os.SystemClock.currentThreadTimeMillis();
            } else {
                // Try to make it obvious if the test case
                // doesn't call startTiming().
                mStartTime = 0;
            }
            if (junitTest) {
                for (int i = 0; i < iterations; i++) {
                    junit.textui.TestRunner.run(((junit.framework.Test) (testCase)));
                }
            } else {
                java.lang.Runnable test = ((java.lang.Runnable) (testCase));
                for (int i = 0; i < iterations; i++) {
                    test.run();
                }
            }
            long endTime = mEndTime;
            if (endTime == 0) {
                endTime = android.os.SystemClock.currentThreadTimeMillis();
            }
            duration = endTime - mStartTime;
            if (!increaseIterations) {
                break;
            }
            if (duration <= 1) {
                iterations *= 1000;
            } else
                if (duration <= 10) {
                    iterations *= 100;
                } else
                    if (duration < 100) {
                        iterations *= 10;
                    } else
                        if (duration < 1000) {
                            iterations *= ((int) ((1000 / duration) + 2));
                        } else {
                            break;
                        }



        } while (true );
        if (duration != 0) {
            iterations *= mInternalIterations;
            performance(testNameInDb, (duration * 1000000) / iterations, iterations, mIntermediates);
        }
    }

    public void runSingleJunitTest(java.lang.String className) {
        java.lang.Throwable excep = null;
        int index = className.lastIndexOf('$');
        java.lang.String testName = "";
        java.lang.String originalClassName = className;
        if (index >= 0) {
            className = className.substring(0, index);
            testName = originalClassName.substring(index + 1);
        }
        try {
            java.lang.Class clazz = mContext.getClassLoader().loadClass(className);
            if (android.test.TestRunner.mJUnitClass.isAssignableFrom(clazz)) {
                junit.framework.TestCase test = ((junit.framework.TestCase) (clazz.newInstance()));
                android.test.TestRunner.JunitTestSuite newSuite = new android.test.TestRunner.JunitTestSuite();
                test.setName(testName);
                if (test instanceof android.test.AndroidTestCase) {
                    android.test.AndroidTestCase testcase = ((android.test.AndroidTestCase) (test));
                    try {
                        testcase.setContext(mContext);
                    } catch (java.lang.Exception ex) {
                        android.util.Log.w(android.test.TestRunner.TAG, "Exception encountered while trying to set the context.", ex);
                    }
                }
                newSuite.addTest(test);
                if (mMode == android.test.TestRunner.PERFORMANCE) {
                    try {
                        started(test.toString());
                        runInPerformanceMode(test, className, true, test.toString());
                        finished(test.toString());
                        if (excep == null) {
                            passed(test.toString());
                        } else {
                            failed(test.toString(), excep);
                        }
                    } catch (java.lang.Throwable ex) {
                        excep = ex;
                    }
                } else
                    if (mMode == android.test.TestRunner.PROFILING) {
                        startProfiling();
                        junit.textui.TestRunner.run(newSuite);
                        finishProfiling();
                    } else {
                        junit.textui.TestRunner.run(newSuite);
                    }

            }
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.e("TestHarness", "No test case to run", e);
        } catch (java.lang.IllegalAccessException e) {
            android.util.Log.e("TestHarness", "Illegal Access Exception", e);
        } catch (java.lang.InstantiationException e) {
            android.util.Log.e("TestHarness", "Instantiation Exception", e);
        }
    }

    public static java.lang.reflect.Method getChildrenMethod(java.lang.Class clazz) {
        try {
            return clazz.getMethod("children", ((java.lang.Class[]) (null)));
        } catch (java.lang.NoSuchMethodException e) {
        }
        return null;
    }

    public static java.lang.reflect.Method getChildrenMethod(android.content.Context c, java.lang.String className) {
        try {
            return android.test.TestRunner.getChildrenMethod(c.getClassLoader().loadClass(className));
        } catch (java.lang.ClassNotFoundException e) {
        }
        return null;
    }

    public static java.lang.String[] getChildren(android.content.Context c, java.lang.String className) {
        java.lang.reflect.Method m = android.test.TestRunner.getChildrenMethod(c, className);
        java.lang.String[] testChildren = android.test.TestRunner.getTestChildren(c, className);
        if ((m == null) & (testChildren == null)) {
            throw new java.lang.RuntimeException("couldn't get children method for " + className);
        }
        if (m != null) {
            java.lang.String[] children = android.test.TestRunner.getChildren(m);
            if (testChildren != null) {
                java.lang.String[] allChildren = new java.lang.String[testChildren.length + children.length];
                java.lang.System.arraycopy(children, 0, allChildren, 0, children.length);
                java.lang.System.arraycopy(testChildren, 0, allChildren, children.length, testChildren.length);
                return allChildren;
            } else {
                return children;
            }
        } else {
            if (testChildren != null) {
                return testChildren;
            }
        }
        return null;
    }

    public static java.lang.String[] getChildren(java.lang.reflect.Method m) {
        try {
            if (!java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
                throw new java.lang.RuntimeException("children method is not static");
            }
            return ((java.lang.String[]) (m.invoke(null, ((java.lang.Object[]) (null)))));
        } catch (java.lang.IllegalAccessException e) {
        } catch (java.lang.reflect.InvocationTargetException e) {
        }
        return new java.lang.String[0];
    }

    public static java.lang.String[] getTestChildren(android.content.Context c, java.lang.String className) {
        try {
            java.lang.Class clazz = c.getClassLoader().loadClass(className);
            if (android.test.TestRunner.mJUnitClass.isAssignableFrom(clazz)) {
                return android.test.TestRunner.getTestChildren(clazz);
            }
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.e("TestHarness", "No class found", e);
        }
        return null;
    }

    public static java.lang.String[] getTestChildren(java.lang.Class clazz) {
        java.lang.reflect.Method[] methods = android.test.TestRunner.getAllTestMethods(clazz);
        java.lang.String[] onScreenTestNames = new java.lang.String[methods.length];
        int index = 0;
        for (java.lang.reflect.Method m : methods) {
            onScreenTestNames[index] = (clazz.getName() + "$") + m.getName();
            index++;
        }
        return onScreenTestNames;
    }

    public static java.lang.reflect.Method[] getAllTestMethods(java.lang.Class clazz) {
        java.lang.reflect.Method[] allMethods = clazz.getDeclaredMethods();
        int numOfMethods = 0;
        for (java.lang.reflect.Method m : allMethods) {
            boolean mTrue = android.test.TestRunner.isTestMethod(m);
            if (mTrue) {
                numOfMethods++;
            }
        }
        int index = 0;
        java.lang.reflect.Method[] testMethods = new java.lang.reflect.Method[numOfMethods];
        for (java.lang.reflect.Method m : allMethods) {
            boolean mTrue = android.test.TestRunner.isTestMethod(m);
            if (mTrue) {
                testMethods[index] = m;
                index++;
            }
        }
        return testMethods;
    }

    private static boolean isTestMethod(java.lang.reflect.Method m) {
        return (m.getName().startsWith("test") && (m.getReturnType() == void.class)) && (m.getParameterTypes().length == 0);
    }

    public static int countJunitTests(java.lang.Class clazz) {
        java.lang.reflect.Method[] allTestMethods = android.test.TestRunner.getAllTestMethods(clazz);
        int numberofMethods = allTestMethods.length;
        return numberofMethods;
    }

    public static boolean isTestSuite(android.content.Context c, java.lang.String className) {
        boolean childrenMethods = android.test.TestRunner.getChildrenMethod(c, className) != null;
        try {
            java.lang.Class clazz = c.getClassLoader().loadClass(className);
            if (android.test.TestRunner.mJUnitClass.isAssignableFrom(clazz)) {
                int numTests = android.test.TestRunner.countJunitTests(clazz);
                if (numTests > 0)
                    childrenMethods = true;

            }
        } catch (java.lang.ClassNotFoundException e) {
        }
        return childrenMethods;
    }

    public boolean isJunitTest(java.lang.String className) {
        int index = className.lastIndexOf('$');
        if (index >= 0) {
            className = className.substring(0, index);
        }
        try {
            java.lang.Class clazz = mContext.getClassLoader().loadClass(className);
            if (android.test.TestRunner.mJUnitClass.isAssignableFrom(clazz)) {
                return true;
            }
        } catch (java.lang.ClassNotFoundException e) {
        }
        return false;
    }

    /**
     * Returns the number of tests that will be run if you try to do this.
     */
    public static int countTests(android.content.Context c, java.lang.String className) {
        try {
            java.lang.Class clazz = c.getClassLoader().loadClass(className);
            java.lang.reflect.Method method = android.test.TestRunner.getChildrenMethod(clazz);
            if (method != null) {
                java.lang.String[] children = android.test.TestRunner.getChildren(method);
                int rv = 0;
                for (java.lang.String child : children) {
                    rv += android.test.TestRunner.countTests(c, child);
                }
                return rv;
            } else
                if (android.test.TestRunner.mRunnableClass.isAssignableFrom(clazz)) {
                    return 1;
                } else
                    if (android.test.TestRunner.mJUnitClass.isAssignableFrom(clazz)) {
                        return android.test.TestRunner.countJunitTests(clazz);
                    }


        } catch (java.lang.ClassNotFoundException e) {
            return 1;// this gets the count right, because either this test

            // is missing, and it will fail when run or it is a single Junit test to be run.
        }
        return 0;
    }

    /**
     * Returns a title to display given the className of a test.
     * <p/>
     * <p>Currently this function just returns the portion of the
     * class name after the last '.'
     */
    public static java.lang.String getTitle(java.lang.String className) {
        int indexDot = className.lastIndexOf('.');
        int indexDollar = className.lastIndexOf('$');
        int index = (indexDot > indexDollar) ? indexDot : indexDollar;
        if (index >= 0) {
            className = className.substring(index + 1);
        }
        return className;
    }
}

