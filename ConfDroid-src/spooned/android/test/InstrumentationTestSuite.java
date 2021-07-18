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
 * A {@link junit.framework.TestSuite} that injects {@link android.app.Instrumentation} into
 * {@link InstrumentationTestCase} before running them.
 *
 * @deprecated Use
<a href="{@docRoot }reference/android/support/test/InstrumentationRegistry.html">
InstrumentationRegistry</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class InstrumentationTestSuite extends junit.framework.TestSuite {
    private final android.app.Instrumentation mInstrumentation;

    /**
     *
     *
     * @param instr
     * 		The instrumentation that will be injected into each
     * 		test before running it.
     */
    public InstrumentationTestSuite(android.app.Instrumentation instr) {
        mInstrumentation = instr;
    }

    public InstrumentationTestSuite(java.lang.String name, android.app.Instrumentation instr) {
        super(name);
        mInstrumentation = instr;
    }

    /**
     *
     *
     * @param theClass
     * 		Inspected for methods starting with 'test'
     * @param instr
     * 		The instrumentation to inject into each test before
     * 		running.
     */
    public InstrumentationTestSuite(final java.lang.Class theClass, android.app.Instrumentation instr) {
        super(theClass);
        mInstrumentation = instr;
    }

    @java.lang.Override
    public void addTestSuite(java.lang.Class testClass) {
        addTest(new android.test.InstrumentationTestSuite(testClass, mInstrumentation));
    }

    @java.lang.Override
    public void runTest(junit.framework.Test test, junit.framework.TestResult result) {
        if (test instanceof android.test.InstrumentationTestCase) {
            ((android.test.InstrumentationTestCase) (test)).injectInstrumentation(mInstrumentation);
        }
        // run the test as usual
        super.runTest(test, result);
    }
}

