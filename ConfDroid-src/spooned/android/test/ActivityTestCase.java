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
 * This is common code used to support Activity test cases.  For more useful classes, please see
 * {@link android.test.ActivityUnitTestCase} and
 * {@link android.test.ActivityInstrumentationTestCase}.
 *
 * @deprecated New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public abstract class ActivityTestCase extends android.test.InstrumentationTestCase {
    /**
     * The activity that will be set up for use in each test method.
     */
    private android.app.Activity mActivity;

    /**
     *
     *
     * @return Returns the activity under test.
     */
    protected android.app.Activity getActivity() {
        return mActivity;
    }

    /**
     * Set the activity under test.
     *
     * @param testActivity
     * 		The activity under test
     */
    protected void setActivity(android.app.Activity testActivity) {
        mActivity = testActivity;
    }

    /**
     * This function is called by various TestCase implementations, at tearDown() time, in order
     * to scrub out any class variables.  This protects against memory leaks in the case where a
     * test case creates a non-static inner class (thus referencing the test case) and gives it to
     * someone else to hold onto.
     *
     * @param testCaseClass
     * 		The class of the derived TestCase implementation.
     * @throws IllegalAccessException
     * 		
     */
    protected void scrubClass(final java.lang.Class<?> testCaseClass) throws java.lang.IllegalAccessException {
        final java.lang.reflect.Field[] fields = getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            final java.lang.Class<?> fieldClass = field.getDeclaringClass();
            if ((testCaseClass.isAssignableFrom(fieldClass) && (!field.getType().isPrimitive())) && ((field.getModifiers() & java.lang.reflect.Modifier.FINAL) == 0)) {
                try {
                    field.setAccessible(true);
                    field.set(this, null);
                } catch (java.lang.Exception e) {
                    android.util.Log.d("TestCase", "Error: Could not nullify field!");
                }
                if (field.get(this) != null) {
                    android.util.Log.d("TestCase", "Error: Could not nullify field!");
                }
            }
        }
    }
}

