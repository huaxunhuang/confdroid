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
 * This class provides functional testing of a single activity.  The activity under test will
 * be created using the system infrastructure (by calling InstrumentationTestCase.launchActivity())
 * and you will then be able to manipulate your Activity directly.
 *
 * <p>Other options supported by this test case include:
 * <ul>
 * <li>You can run any test method on the UI thread (see {@link android.test.UiThreadTest}).</li>
 * <li>You can inject custom Intents into your Activity (see
 * {@link #setActivityIntent(Intent)}).</li>
 * </ul>
 *
 * <p>This class replaces {@link android.test.ActivityInstrumentationTestCase}, which is deprecated.
 * New tests should be written using this base class.
 *
 * <p>If you prefer an isolated unit test, see {@link android.test.ActivityUnitTestCase}.
 *
 * @deprecated Use
<a href="{@docRoot }reference/android/support/test/rule/ActivityTestRule.html">
ActivityTestRule</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public abstract class ActivityInstrumentationTestCase2<T extends android.app.Activity> extends android.test.ActivityTestCase {
    java.lang.Class<T> mActivityClass;

    boolean mInitialTouchMode = false;

    android.content.Intent mActivityIntent = null;

    /**
     * Creates an {@link ActivityInstrumentationTestCase2}.
     *
     * @param pkg
     * 		ignored - no longer in use.
     * @param activityClass
     * 		The activity to test. This must be a class in the instrumentation
     * 		targetPackage specified in the AndroidManifest.xml
     * @deprecated use {@link #ActivityInstrumentationTestCase2(Class)} instead
     */
    @java.lang.Deprecated
    public ActivityInstrumentationTestCase2(java.lang.String pkg, java.lang.Class<T> activityClass) {
        this(activityClass);
    }

    /**
     * Creates an {@link ActivityInstrumentationTestCase2}.
     *
     * @param activityClass
     * 		The activity to test. This must be a class in the instrumentation
     * 		targetPackage specified in the AndroidManifest.xml
     */
    public ActivityInstrumentationTestCase2(java.lang.Class<T> activityClass) {
        mActivityClass = activityClass;
    }

    /**
     * Get the Activity under test, starting it if necessary.
     *
     * For each test method invocation, the Activity will not actually be created until the first
     * time this method is called.
     *
     * <p>If you wish to provide custom setup values to your Activity, you may call
     * {@link #setActivityIntent(Intent)} and/or {@link #setActivityInitialTouchMode(boolean)}
     * before your first call to getActivity().  Calling them after your Activity has
     * started will have no effect.
     *
     * <p><b>NOTE:</b> Activities under test may not be started from within the UI thread.
     * If your test method is annotated with {@link android.test.UiThreadTest}, then your Activity
     * will be started automatically just before your test method is run.  You still call this
     * method in order to get the Activity under test.
     *
     * @return the Activity under test
     */
    @java.lang.Override
    public T getActivity() {
        android.app.Activity a = super.getActivity();
        if (a == null) {
            // set initial touch mode
            getInstrumentation().setInTouchMode(mInitialTouchMode);
            final java.lang.String targetPackage = getInstrumentation().getTargetContext().getPackageName();
            // inject custom intent, if provided
            if (mActivityIntent == null) {
                a = launchActivity(targetPackage, mActivityClass, null);
            } else {
                a = launchActivityWithIntent(targetPackage, mActivityClass, mActivityIntent);
            }
            setActivity(a);
        }
        return ((T) (a));
    }

    /**
     * Call this method before the first call to {@link #getActivity} to inject a customized Intent
     * into the Activity under test.
     *
     * <p>If you do not call this, the default intent will be provided.  If you call this after
     * your Activity has been started, it will have no effect.
     *
     * <p><b>NOTE:</b> Activities under test may not be started from within the UI thread.
     * If your test method is annotated with {@link android.test.UiThreadTest}, then you must call
     * {@link #setActivityIntent(Intent)} from {@link #setUp()}.
     *
     * <p>The default Intent (if this method is not called) is:
     *  action = {@link Intent#ACTION_MAIN}
     *  flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
     * All other fields are null or empty.
     *
     * @param i
     * 		The Intent to start the Activity with, or null to reset to the default Intent.
     */
    public void setActivityIntent(android.content.Intent i) {
        mActivityIntent = i;
    }

    /**
     * Call this method before the first call to {@link #getActivity} to set the initial touch
     * mode for the Activity under test.
     *
     * <p>If you do not call this, the touch mode will be false.  If you call this after
     * your Activity has been started, it will have no effect.
     *
     * <p><b>NOTE:</b> Activities under test may not be started from within the UI thread.
     * If your test method is annotated with {@link android.test.UiThreadTest}, then you must call
     * {@link #setActivityInitialTouchMode(boolean)} from {@link #setUp()}.
     *
     * @param initialTouchMode
     * 		true if the Activity should be placed into "touch mode" when started
     */
    public void setActivityInitialTouchMode(boolean initialTouchMode) {
        mInitialTouchMode = initialTouchMode;
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mInitialTouchMode = false;
        mActivityIntent = null;
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        // Finish the Activity off (unless was never launched anyway)
        android.app.Activity a = super.getActivity();
        if (a != null) {
            a.finish();
            setActivity(null);
        }
        // Scrub out members - protects against memory leaks in the case where someone
        // creates a non-static inner class (thus referencing the test case) and gives it to
        // someone else to hold onto
        scrubClass(android.test.ActivityInstrumentationTestCase2.class);
        super.tearDown();
    }

    /**
     * Runs the current unit test. If the unit test is annotated with
     * {@link android.test.UiThreadTest}, force the Activity to be created before switching to
     * the UI thread.
     */
    @java.lang.Override
    protected void runTest() throws java.lang.Throwable {
        try {
            java.lang.reflect.Method method = getClass().getMethod(getName(), ((java.lang.Class[]) (null)));
            if (method.isAnnotationPresent(android.test.UiThreadTest.class)) {
                getActivity();
            }
        } catch (java.lang.Exception e) {
            // eat the exception here; super.runTest() will catch it again and handle it properly
        }
        super.runTest();
    }
}

