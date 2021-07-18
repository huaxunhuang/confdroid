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
 * Represents a test to be run. Can be constructed without instantiating the TestCase or even
 * loading the class.
 */
public class TestMethod {
    private final java.lang.String enclosingClassname;

    private final java.lang.String testMethodName;

    private final java.lang.Class<? extends junit.framework.TestCase> enclosingClass;

    public TestMethod(java.lang.reflect.Method method, java.lang.Class<? extends junit.framework.TestCase> enclosingClass) {
        this(method.getName(), enclosingClass);
    }

    public TestMethod(java.lang.String methodName, java.lang.Class<? extends junit.framework.TestCase> enclosingClass) {
        this.enclosingClass = enclosingClass;
        this.enclosingClassname = enclosingClass.getName();
        this.testMethodName = methodName;
    }

    public TestMethod(junit.framework.TestCase testCase) {
        this(testCase.getName(), testCase.getClass());
    }

    public java.lang.String getName() {
        return testMethodName;
    }

    public java.lang.String getEnclosingClassname() {
        return enclosingClassname;
    }

    public <T extends java.lang.annotation.Annotation> T getAnnotation(java.lang.Class<T> annotationClass) {
        try {
            return getEnclosingClass().getMethod(getName()).getAnnotation(annotationClass);
        } catch (java.lang.NoSuchMethodException e) {
            return null;
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    public java.lang.Class<? extends junit.framework.TestCase> getEnclosingClass() {
        return enclosingClass;
    }

    public junit.framework.TestCase createTest() throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
        return instantiateTest(enclosingClass, testMethodName);
    }

    @java.lang.SuppressWarnings("unchecked")
    private junit.framework.TestCase instantiateTest(java.lang.Class testCaseClass, java.lang.String testName) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
        java.lang.reflect.Constructor[] constructors = testCaseClass.getConstructors();
        if (constructors.length == 0) {
            return instantiateTest(testCaseClass.getSuperclass(), testName);
        } else {
            for (java.lang.reflect.Constructor constructor : constructors) {
                java.lang.Class[] params = constructor.getParameterTypes();
                if (noargsConstructor(params)) {
                    junit.framework.TestCase test = ((java.lang.reflect.Constructor<? extends junit.framework.TestCase>) (constructor)).newInstance();
                    // JUnit will run just the one test if you call
                    // {@link TestCase#setName(String)}
                    test.setName(testName);
                    return test;
                } else
                    if (singleStringConstructor(params)) {
                        return ((java.lang.reflect.Constructor<? extends junit.framework.TestCase>) (constructor)).newInstance(testName);
                    }

            }
        }
        throw new java.lang.RuntimeException("Unable to locate a constructor for " + testCaseClass.getName());
    }

    private boolean singleStringConstructor(java.lang.Class[] params) {
        return (params.length == 1) && params[0].equals(java.lang.String.class);
    }

    private boolean noargsConstructor(java.lang.Class[] params) {
        return params.length == 0;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.test.suitebuilder.TestMethod that = ((android.test.suitebuilder.TestMethod) (o));
        if (enclosingClassname != null ? !enclosingClassname.equals(that.enclosingClassname) : that.enclosingClassname != null) {
            return false;
        }
        if (testMethodName != null ? !testMethodName.equals(that.testMethodName) : that.testMethodName != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = (enclosingClassname != null) ? enclosingClassname.hashCode() : 0;
        result = (31 * result) + (testMethodName != null ? testMethodName.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (enclosingClassname + ".") + testMethodName;
    }
}

