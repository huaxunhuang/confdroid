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
 * @unknown - This is part of a framework that is under development and should not be used for
active development.
 */
@java.lang.Deprecated
public class TestCaseUtil {
    private TestCaseUtil() {
    }

    @java.lang.SuppressWarnings("unchecked")
    public static java.util.List<java.lang.String> getTestCaseNames(junit.framework.Test test, boolean flatten) {
        java.util.List<junit.framework.Test> tests = ((java.util.List<junit.framework.Test>) (android.test.TestCaseUtil.getTests(test, flatten)));
        java.util.List<java.lang.String> testCaseNames = com.google.android.collect.Lists.newArrayList();
        for (junit.framework.Test aTest : tests) {
            testCaseNames.add(android.test.TestCaseUtil.getTestName(aTest));
        }
        return testCaseNames;
    }

    public static java.util.List<? extends junit.framework.Test> getTests(junit.framework.Test test, boolean flatten) {
        return android.test.TestCaseUtil.getTests(test, flatten, new java.util.HashSet<java.lang.Class<?>>());
    }

    private static java.util.List<? extends junit.framework.Test> getTests(junit.framework.Test test, boolean flatten, java.util.Set<java.lang.Class<?>> seen) {
        java.util.List<junit.framework.Test> testCases = com.google.android.collect.Lists.newArrayList();
        if (test != null) {
            junit.framework.Test workingTest = null;
            /* If we want to run a single TestCase method only, we must not
            invoke the suite() method, because we will run all test methods
            of the class then.
             */
            if ((test instanceof junit.framework.TestCase) && (((junit.framework.TestCase) (test)).getName() == null)) {
                workingTest = android.test.TestCaseUtil.invokeSuiteMethodIfPossible(test.getClass(), seen);
            }
            if (workingTest == null) {
                workingTest = test;
            }
            if (workingTest instanceof junit.framework.TestSuite) {
                junit.framework.TestSuite testSuite = ((junit.framework.TestSuite) (workingTest));
                java.util.Enumeration enumeration = testSuite.tests();
                while (enumeration.hasMoreElements()) {
                    junit.framework.Test childTest = ((junit.framework.Test) (enumeration.nextElement()));
                    if (flatten) {
                        testCases.addAll(android.test.TestCaseUtil.getTests(childTest, flatten, seen));
                    } else {
                        testCases.add(childTest);
                    }
                } 
            } else {
                testCases.add(workingTest);
            }
        }
        return testCases;
    }

    private static junit.framework.Test invokeSuiteMethodIfPossible(java.lang.Class testClass, java.util.Set<java.lang.Class<?>> seen) {
        try {
            java.lang.reflect.Method suiteMethod = testClass.getMethod(junit.runner.BaseTestRunner.SUITE_METHODNAME, new java.lang.Class[0]);
            /* Additional check necessary: If a TestCase contains a suite()
            method that returns a TestSuite including the TestCase itself,
            we need to stop the recursion. We use a set of classes to
            remember which classes' suite() methods were already invoked.
             */
            if (java.lang.reflect.Modifier.isStatic(suiteMethod.getModifiers()) && (!seen.contains(testClass))) {
                seen.add(testClass);
                try {
                    return ((junit.framework.Test) (suiteMethod.invoke(null, ((java.lang.Object[]) (null)))));
                } catch (java.lang.reflect.InvocationTargetException e) {
                    // do nothing
                } catch (java.lang.IllegalAccessException e) {
                    // do nothing
                }
            }
        } catch (java.lang.NoSuchMethodException e) {
            // do nothing
        }
        return null;
    }

    public static java.lang.String getTestName(junit.framework.Test test) {
        if (test instanceof junit.framework.TestCase) {
            junit.framework.TestCase testCase = ((junit.framework.TestCase) (test));
            return testCase.getName();
        } else
            if (test instanceof junit.framework.TestSuite) {
                junit.framework.TestSuite testSuite = ((junit.framework.TestSuite) (test));
                java.lang.String name = testSuite.getName();
                if (name != null) {
                    int index = name.lastIndexOf(".");
                    if (index > (-1)) {
                        return name.substring(index + 1);
                    } else {
                        return name;
                    }
                }
            }

        return "";
    }

    public static junit.framework.Test getTestAtIndex(junit.framework.TestSuite testSuite, int position) {
        int index = 0;
        java.util.Enumeration enumeration = testSuite.tests();
        while (enumeration.hasMoreElements()) {
            junit.framework.Test test = ((junit.framework.Test) (enumeration.nextElement()));
            if (index == position) {
                return test;
            }
            index++;
        } 
        return null;
    }

    public static junit.framework.TestSuite createTestSuite(java.lang.Class<? extends junit.framework.Test> testClass) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        junit.framework.Test test = android.test.TestCaseUtil.invokeSuiteMethodIfPossible(testClass, new java.util.HashSet<java.lang.Class<?>>());
        if (test == null) {
            return new junit.framework.TestSuite(testClass);
        } else
            if (junit.framework.TestCase.class.isAssignableFrom(test.getClass())) {
                junit.framework.TestSuite testSuite = new junit.framework.TestSuite(test.getClass().getName());
                testSuite.addTest(test);
                return testSuite;
            }

        return ((junit.framework.TestSuite) (test));
    }
}

