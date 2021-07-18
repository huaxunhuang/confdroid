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
package android.view;


public class RunQueueTest extends android.test.ActivityInstrumentationTestCase<android.view.RunQueue> {
    public RunQueueTest() {
        super("com.android.frameworks.coretests", android.view.RunQueue.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRunnableRan() throws java.lang.Exception {
        final android.view.RunQueue activity = getActivity();
        getInstrumentation().waitForIdleSync();
        assertTrue("The runnable did not run", activity.runnableRan);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRunnableCancelled() throws java.lang.Exception {
        final android.view.RunQueue activity = getActivity();
        getInstrumentation().waitForIdleSync();
        assertTrue("The runnable was not cancelled", activity.runnableCancelled);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testListenerFired() throws java.lang.Exception {
        final android.view.RunQueue activity = getActivity();
        getInstrumentation().waitForIdleSync();
        assertTrue("The global layout listener did not fire", activity.globalLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTreeObserverKilled() throws java.lang.Exception {
        final android.view.RunQueue activity = getActivity();
        getInstrumentation().waitForIdleSync();
        assertFalse("The view tree observer is still alive", activity.viewTreeObserver.isAlive());
    }
}

