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
package android.test;


/**
 * This test runner extends the default InstrumentationTestRunner. It overrides
 * the {@code onCreate(Bundle)} method and sets the system properties necessary
 * for many core tests to run. This is needed because there are some core tests
 * that need writing access to the file system. We also need to set the harness
 * Thread's context ClassLoader. Otherwise some classes and resources will not
 * be found. Finally, we add a means to free memory allocated by a TestCase
 * after its execution.
 *
 * @unknown 
 */
@java.lang.Deprecated
public class InstrumentationCoreTestRunner extends android.test.InstrumentationTestRunner {
    /**
     * Convenience definition of our log tag.
     */
    private static final java.lang.String TAG = "InstrumentationCoreTestRunner";

    /**
     * True if (and only if) we are running in single-test mode (as opposed to
     * batch mode).
     */
    private boolean singleTest = false;

    @java.lang.Override
    public void onCreate(android.os.Bundle arguments) {
        // We might want to move this to /sdcard, if is is mounted/writable.
        java.io.File cacheDir = getTargetContext().getCacheDir();
        // Set some properties that the core tests absolutely need.
        java.lang.System.setProperty("user.language", "en");
        java.lang.System.setProperty("user.region", "US");
        java.lang.System.setProperty("java.home", cacheDir.getAbsolutePath());
        java.lang.System.setProperty("user.home", cacheDir.getAbsolutePath());
        java.lang.System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
        if (arguments != null) {
            java.lang.String classArg = arguments.getString(android.test.InstrumentationTestRunner.ARGUMENT_TEST_CLASS);
            singleTest = (classArg != null) && classArg.contains("#");
        }
        super.onCreate(arguments);
    }

    @java.lang.Override
    protected android.test.AndroidTestRunner getAndroidTestRunner() {
        android.test.AndroidTestRunner runner = super.getAndroidTestRunner();
        runner.addTestListener(new junit.framework.TestListener() {
            /**
             * The last test class we executed code from.
             */
            private java.lang.Class<?> lastClass;

            /**
             * The minimum time we expect a test to take.
             */
            private static final int MINIMUM_TIME = 100;

            /**
             * The start time of our current test in System.currentTimeMillis().
             */
            private long startTime;

            public void startTest(junit.framework.Test test) {
                if (test.getClass() != lastClass) {
                    lastClass = test.getClass();
                    printMemory(test.getClass());
                }
                java.lang.Thread.currentThread().setContextClassLoader(test.getClass().getClassLoader());
                startTime = java.lang.System.currentTimeMillis();
            }

            public void endTest(junit.framework.Test test) {
                if (test instanceof junit.framework.TestCase) {
                    cleanup(((junit.framework.TestCase) (test)));
                    /* Make sure all tests take at least MINIMUM_TIME to
                    complete. If they don't, we wait a bit. The Cupcake
                    Binder can't handle too many operations in a very
                    short time, which causes headache for the CTS.
                     */
                    long timeTaken = java.lang.System.currentTimeMillis() - startTime;
                    if (timeTaken < MINIMUM_TIME) {
                        try {
                            java.lang.Thread.sleep(MINIMUM_TIME - timeTaken);
                        } catch (java.lang.InterruptedException ignored) {
                            // We don't care.
                        }
                    }
                }
            }

            public void addError(junit.framework.Test test, java.lang.Throwable t) {
                // This space intentionally left blank.
            }

            public void addFailure(junit.framework.Test test, junit.framework.AssertionFailedError t) {
                // This space intentionally left blank.
            }

            /**
             * Dumps some memory info.
             */
            private void printMemory(java.lang.Class<? extends junit.framework.Test> testClass) {
                java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
                long total = runtime.totalMemory();
                long free = runtime.freeMemory();
                long used = total - free;
                android.util.Log.d(android.test.InstrumentationCoreTestRunner.TAG, "Total memory  : " + total);
                android.util.Log.d(android.test.InstrumentationCoreTestRunner.TAG, "Used memory   : " + used);
                android.util.Log.d(android.test.InstrumentationCoreTestRunner.TAG, "Free memory   : " + free);
                android.util.Log.d(android.test.InstrumentationCoreTestRunner.TAG, "Now executing : " + testClass.getName());
            }

            /**
             * Nulls all non-static reference fields in the given test class.
             * This method helps us with those test classes that don't have an
             * explicit tearDown() method. Normally the garbage collector should
             * take care of everything, but since JUnit keeps references to all
             * test cases, a little help might be a good idea.
             */
            private void cleanup(junit.framework.TestCase test) {
                java.lang.Class<?> clazz = test.getClass();
                while (clazz != junit.framework.TestCase.class) {
                    java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        java.lang.reflect.Field f = fields[i];
                        if ((!f.getType().isPrimitive()) && (!java.lang.reflect.Modifier.isStatic(f.getModifiers()))) {
                            try {
                                f.setAccessible(true);
                                f.set(test, null);
                            } catch (java.lang.Exception ignored) {
                                // Nothing we can do about it.
                            }
                        }
                    }
                    clazz = clazz.getSuperclass();
                } 
            }
        });
        return runner;
    }
}

