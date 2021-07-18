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
 * This class provides isolated testing of a single activity.  The activity under test will
 * be created with minimal connection to the system infrastructure, and you can inject mocked or
 * wrappered versions of many of Activity's dependencies.  Most of the work is handled
 * automatically here by {@link #setUp} and {@link #tearDown}.
 *
 * <p>If you prefer a functional test, see {@link android.test.ActivityInstrumentationTestCase}.
 *
 * <p>It must be noted that, as a true unit test, your Activity will not be running in the
 * normal system and will not participate in the normal interactions with other Activities.
 * The following methods should not be called in this configuration - most of them will throw
 * exceptions:
 * <ul>
 * <li>{@link android.app.Activity#createPendingResult(int, Intent, int)}</li>
 * <li>{@link android.app.Activity#startActivityIfNeeded(Intent, int)}</li>
 * <li>{@link android.app.Activity#startActivityFromChild(Activity, Intent, int)}</li>
 * <li>{@link android.app.Activity#startNextMatchingActivity(Intent)}</li>
 * <li>{@link android.app.Activity#getCallingActivity()}</li>
 * <li>{@link android.app.Activity#getCallingPackage()}</li>
 * <li>{@link android.app.Activity#createPendingResult(int, Intent, int)}</li>
 * <li>{@link android.app.Activity#getTaskId()}</li>
 * <li>{@link android.app.Activity#isTaskRoot()}</li>
 * <li>{@link android.app.Activity#moveTaskToBack(boolean)}</li>
 * </ul>
 *
 * <p>The following methods may be called but will not do anything.  For test purposes, you can use
 * the methods {@link #getStartedActivityIntent()} and {@link #getStartedActivityRequest()} to
 * inspect the parameters that they were called with.
 * <ul>
 * <li>{@link android.app.Activity#startActivity(Intent)}</li>
 * <li>{@link android.app.Activity#startActivityForResult(Intent, int)}</li>
 * </ul>
 *
 * <p>The following methods may be called but will not do anything.  For test purposes, you can use
 * the methods {@link #isFinishCalled()} and {@link #getFinishedActivityRequest()} to inspect the
 * parameters that they were called with.
 * <ul>
 * <li>{@link android.app.Activity#finish()}</li>
 * <li>{@link android.app.Activity#finishFromChild(Activity child)}</li>
 * <li>{@link android.app.Activity#finishActivity(int requestCode)}</li>
 * </ul>
 *
 * @deprecated Write
<a href="{@docRoot }training/testing/unit-testing/local-unit-tests.html">Local Unit Tests</a>
instead.
 */
@java.lang.Deprecated
public abstract class ActivityUnitTestCase<T extends android.app.Activity> extends android.test.ActivityTestCase {
    private static final java.lang.String TAG = "ActivityUnitTestCase";

    private java.lang.Class<T> mActivityClass;

    private android.content.Context mActivityContext;

    private android.app.Application mApplication;

    private android.test.ActivityUnitTestCase.MockParent mMockParent;

    private boolean mAttached = false;

    private boolean mCreated = false;

    public ActivityUnitTestCase(java.lang.Class<T> activityClass) {
        mActivityClass = activityClass;
    }

    @java.lang.Override
    public T getActivity() {
        return ((T) (super.getActivity()));
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        // default value for target context, as a default
        mActivityContext = getInstrumentation().getTargetContext();
    }

    /**
     * Start the activity under test, in the same way as if it was started by
     * {@link android.content.Context#startActivity Context.startActivity()}, providing the
     * arguments it supplied.  When you use this method to start the activity, it will automatically
     * be stopped by {@link #tearDown}.
     *
     * <p>This method will call onCreate(), but if you wish to further exercise Activity life
     * cycle methods, you must call them yourself from your test case.
     *
     * <p><i>Do not call from your setUp() method.  You must call this method from each of your
     * test methods.</i>
     *
     * @param intent
     * 		The Intent as if supplied to {@link android.content.Context#startActivity}.
     * @param savedInstanceState
     * 		The instance state, if you are simulating this part of the life
     * 		cycle.  Typically null.
     * @param lastNonConfigurationInstance
     * 		This Object will be available to the
     * 		Activity if it calls {@link android.app.Activity#getLastNonConfigurationInstance()}.
     * 		Typically null.
     * @return Returns the Activity that was created
     */
    protected T startActivity(android.content.Intent intent, android.os.Bundle savedInstanceState, java.lang.Object lastNonConfigurationInstance) {
        junit.framework.TestCase.assertFalse("Activity already created", mCreated);
        if (!mAttached) {
            junit.framework.TestCase.assertNotNull(mActivityClass);
            setActivity(null);
            T newActivity = null;
            try {
                android.os.IBinder token = null;
                if (mApplication == null) {
                    setApplication(new android.test.mock.MockApplication());
                }
                android.content.ComponentName cn = new android.content.ComponentName(mActivityClass.getPackage().getName(), mActivityClass.getName());
                intent.setComponent(cn);
                android.content.pm.ActivityInfo info = new android.content.pm.ActivityInfo();
                java.lang.CharSequence title = mActivityClass.getName();
                mMockParent = new android.test.ActivityUnitTestCase.MockParent();
                java.lang.String id = null;
                newActivity = ((T) (getInstrumentation().newActivity(mActivityClass, mActivityContext, token, mApplication, intent, info, title, mMockParent, id, lastNonConfigurationInstance)));
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.test.ActivityUnitTestCase.TAG, "Catching exception", e);
                junit.framework.TestCase.assertNotNull(newActivity);
            }
            junit.framework.TestCase.assertNotNull(newActivity);
            setActivity(newActivity);
            mAttached = true;
        }
        T result = getActivity();
        if (result != null) {
            getInstrumentation().callActivityOnCreate(getActivity(), savedInstanceState);
            mCreated = true;
        }
        return result;
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        setActivity(null);
        // Scrub out members - protects against memory leaks in the case where someone
        // creates a non-static inner class (thus referencing the test case) and gives it to
        // someone else to hold onto
        scrubClass(android.test.ActivityInstrumentationTestCase.class);
        super.tearDown();
    }

    /**
     * Set the application for use during the test.  You must call this function before calling
     * {@link #startActivity}.  If your test does not call this method,
     *
     * @param application
     * 		The Application object that will be injected into the Activity under test.
     */
    public void setApplication(android.app.Application application) {
        mApplication = application;
    }

    /**
     * If you wish to inject a Mock, Isolated, or otherwise altered context, you can do so
     * here.  You must call this function before calling {@link #startActivity}.  If you wish to
     * obtain a real Context, as a building block, use getInstrumentation().getTargetContext().
     */
    public void setActivityContext(android.content.Context activityContext) {
        mActivityContext = activityContext;
    }

    /**
     * This method will return the value if your Activity under test calls
     * {@link android.app.Activity#setRequestedOrientation}.
     */
    public int getRequestedOrientation() {
        if (mMockParent != null) {
            return mMockParent.mRequestedOrientation;
        }
        return 0;
    }

    /**
     * This method will return the launch intent if your Activity under test calls
     * {@link android.app.Activity#startActivity(Intent)} or
     * {@link android.app.Activity#startActivityForResult(Intent, int)}.
     *
     * @return The Intent provided in the start call, or null if no start call was made.
     */
    public android.content.Intent getStartedActivityIntent() {
        if (mMockParent != null) {
            return mMockParent.mStartedActivityIntent;
        }
        return null;
    }

    /**
     * This method will return the launch request code if your Activity under test calls
     * {@link android.app.Activity#startActivityForResult(Intent, int)}.
     *
     * @return The request code provided in the start call, or -1 if no start call was made.
     */
    public int getStartedActivityRequest() {
        if (mMockParent != null) {
            return mMockParent.mStartedActivityRequest;
        }
        return 0;
    }

    /**
     * This method will notify you if the Activity under test called
     * {@link android.app.Activity#finish()},
     * {@link android.app.Activity#finishFromChild(Activity)}, or
     * {@link android.app.Activity#finishActivity(int)}.
     *
     * @return Returns true if one of the listed finish methods was called.
     */
    public boolean isFinishCalled() {
        if (mMockParent != null) {
            return mMockParent.mFinished;
        }
        return false;
    }

    /**
     * This method will return the request code if the Activity under test called
     * {@link android.app.Activity#finishActivity(int)}.
     *
     * @return The request code provided in the start call, or -1 if no finish call was made.
     */
    public int getFinishedActivityRequest() {
        if (mMockParent != null) {
            return mMockParent.mFinishedActivityRequest;
        }
        return 0;
    }

    /**
     * This mock Activity represents the "parent" activity.  By injecting this, we allow the user
     * to call a few more Activity methods, including:
     * <ul>
     * <li>{@link android.app.Activity#getRequestedOrientation()}</li>
     * <li>{@link android.app.Activity#setRequestedOrientation(int)}</li>
     * <li>{@link android.app.Activity#finish()}</li>
     * <li>{@link android.app.Activity#finishActivity(int requestCode)}</li>
     * <li>{@link android.app.Activity#finishFromChild(Activity child)}</li>
     * </ul>
     *
     * TODO: Make this overrideable, and the unit test can look for calls to other methods
     */
    private static class MockParent extends android.app.Activity {
        public int mRequestedOrientation = 0;

        public android.content.Intent mStartedActivityIntent = null;

        public int mStartedActivityRequest = -1;

        public boolean mFinished = false;

        public int mFinishedActivityRequest = -1;

        /**
         * Implementing in the parent allows the user to call this function on the tested activity.
         */
        @java.lang.Override
        public void setRequestedOrientation(int requestedOrientation) {
            mRequestedOrientation = requestedOrientation;
        }

        /**
         * Implementing in the parent allows the user to call this function on the tested activity.
         */
        @java.lang.Override
        public int getRequestedOrientation() {
            return mRequestedOrientation;
        }

        /**
         * By returning null here, we inhibit the creation of any "container" for the window.
         */
        @java.lang.Override
        public android.view.Window getWindow() {
            return null;
        }

        /**
         * By defining this in the parent, we allow the tested activity to call
         * <ul>
         * <li>{@link android.app.Activity#startActivity(Intent)}</li>
         * <li>{@link android.app.Activity#startActivityForResult(Intent, int)}</li>
         * </ul>
         */
        @java.lang.Override
        public void startActivityFromChild(android.app.Activity child, android.content.Intent intent, int requestCode) {
            mStartedActivityIntent = intent;
            mStartedActivityRequest = requestCode;
        }

        /**
         * By defining this in the parent, we allow the tested activity to call
         * <ul>
         * <li>{@link android.app.Activity#finish()}</li>
         * <li>{@link android.app.Activity#finishFromChild(Activity child)}</li>
         * </ul>
         */
        @java.lang.Override
        public void finishFromChild(android.app.Activity child) {
            mFinished = true;
        }

        /**
         * By defining this in the parent, we allow the tested activity to call
         * <ul>
         * <li>{@link android.app.Activity#finishActivity(int requestCode)}</li>
         * </ul>
         */
        @java.lang.Override
        public void finishActivityFromChild(android.app.Activity child, int requestCode) {
            mFinished = true;
            mFinishedActivityRequest = requestCode;
        }
    }
}

