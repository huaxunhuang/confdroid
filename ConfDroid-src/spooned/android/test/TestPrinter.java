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
 * Prints the test progress to stdout. Android includes a default
 * implementation and calls these methods to print out test progress; you
 * probably will not need to create or extend this class or call its methods manually.
 * See the full {@link android.test} package description for information about
 * getting test results.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
@java.lang.Deprecated
public class TestPrinter implements android.test.TestRunner.Listener , junit.framework.TestListener {
    private java.lang.String mTag;

    private boolean mOnlyFailures;

    private java.util.Set<java.lang.String> mFailedTests = new java.util.HashSet<java.lang.String>();

    public TestPrinter(java.lang.String tag, boolean onlyFailures) {
        mTag = tag;
        mOnlyFailures = onlyFailures;
    }

    public void started(java.lang.String className) {
        if (!mOnlyFailures) {
            android.util.Log.i(mTag, "started: " + className);
        }
    }

    public void finished(java.lang.String className) {
        if (!mOnlyFailures) {
            android.util.Log.i(mTag, "finished: " + className);
        }
    }

    public void performance(java.lang.String className, long itemTimeNS, int iterations, java.util.List<android.test.TestRunner.IntermediateTime> intermediates) {
        android.util.Log.i(mTag, ((((("perf: " + className) + " = ") + itemTimeNS) + "ns/op (done ") + iterations) + " times)");
        if ((intermediates != null) && (intermediates.size() > 0)) {
            int N = intermediates.size();
            for (int i = 0; i < N; i++) {
                android.test.TestRunner.IntermediateTime time = intermediates.get(i);
                android.util.Log.i(mTag, ((("  intermediate: " + time.name) + " = ") + time.timeInNS) + "ns");
            }
        }
    }

    public void passed(java.lang.String className) {
        if (!mOnlyFailures) {
            android.util.Log.i(mTag, "passed: " + className);
        }
    }

    public void failed(java.lang.String className, java.lang.Throwable exception) {
        android.util.Log.i(mTag, "failed: " + className);
        android.util.Log.i(mTag, "----- begin exception -----");
        android.util.Log.i(mTag, "", exception);
        android.util.Log.i(mTag, "----- end exception -----");
    }

    private void failed(junit.framework.Test test, java.lang.Throwable t) {
        mFailedTests.add(test.toString());
        failed(test.toString(), t);
    }

    public void addError(junit.framework.Test test, java.lang.Throwable t) {
        failed(test, t);
    }

    public void addFailure(junit.framework.Test test, junit.framework.AssertionFailedError t) {
        failed(test, t);
    }

    public void endTest(junit.framework.Test test) {
        finished(test.toString());
        if (!mFailedTests.contains(test.toString())) {
            passed(test.toString());
        }
        mFailedTests.remove(test.toString());
    }

    public void startTest(junit.framework.Test test) {
        started(test.toString());
    }
}

