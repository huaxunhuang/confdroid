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
 * Extend this if you need to access Resources or other things that depend on Activity Context.
 *
 * @deprecated Use
<a href="{@docRoot }reference/android/support/test/InstrumentationRegistry.html">
InstrumentationRegistry</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class AndroidTestCase extends junit.framework.TestCase {
    protected android.content.Context mContext;

    private android.content.Context mTestContext;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
    }

    @android.test.suitebuilder.annotation.Suppress
    public void testAndroidTestCaseSetupProperly() {
        junit.framework.TestCase.assertNotNull("Context is null. setContext should be called before tests are run", mContext);
    }

    public void setContext(android.content.Context context) {
        mContext = context;
    }

    public android.content.Context getContext() {
        return mContext;
    }

    /**
     * Test context can be used to access resources from the test's own package
     * as opposed to the resources from the test target package. Access to the
     * latter is provided by the context set with the {@link #setContext}
     * method.
     *
     * @unknown 
     */
    public void setTestContext(android.content.Context context) {
        mTestContext = context;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.Context getTestContext() {
        return mTestContext;
    }

    /**
     * Asserts that launching a given activity is protected by a particular permission by
     * attempting to start the activity and validating that a {@link SecurityException}
     * is thrown that mentions the permission in its error message.
     *
     * Note that an instrumentation isn't needed because all we are looking for is a security error
     * and we don't need to wait for the activity to launch and get a handle to the activity.
     *
     * @param packageName
     * 		The package name of the activity to launch.
     * @param className
     * 		The class of the activity to launch.
     * @param permission
     * 		The name of the permission.
     */
    public void assertActivityRequiresPermission(java.lang.String packageName, java.lang.String className, java.lang.String permission) {
        final android.content.Intent intent = new android.content.Intent();
        intent.setClassName(packageName, className);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            getContext().startActivity(intent);
            junit.framework.TestCase.fail("expected security exception for " + permission);
        } catch (java.lang.SecurityException expected) {
            junit.framework.TestCase.assertNotNull("security exception's error message.", expected.getMessage());
            junit.framework.TestCase.assertTrue(("error message should contain " + permission) + ".", expected.getMessage().contains(permission));
        }
    }

    /**
     * Asserts that reading from the content uri requires a particular permission by querying the
     * uri and ensuring a {@link SecurityException} is thrown mentioning the particular permission.
     *
     * @param uri
     * 		The uri that requires a permission to query.
     * @param permission
     * 		The permission that should be required.
     */
    public void assertReadingContentUriRequiresPermission(android.net.Uri uri, java.lang.String permission) {
        try {
            getContext().getContentResolver().query(uri, null, null, null, null);
            junit.framework.TestCase.fail("expected SecurityException requiring " + permission);
        } catch (java.lang.SecurityException expected) {
            junit.framework.TestCase.assertNotNull("security exception's error message.", expected.getMessage());
            junit.framework.TestCase.assertTrue(("error message should contain " + permission) + ".", expected.getMessage().contains(permission));
        }
    }

    /**
     * Asserts that writing to the content uri requires a particular permission by inserting into
     * the uri and ensuring a {@link SecurityException} is thrown mentioning the particular
     * permission.
     *
     * @param uri
     * 		The uri that requires a permission to query.
     * @param permission
     * 		The permission that should be required.
     */
    public void assertWritingContentUriRequiresPermission(android.net.Uri uri, java.lang.String permission) {
        try {
            getContext().getContentResolver().insert(uri, new android.content.ContentValues());
            junit.framework.TestCase.fail("expected SecurityException requiring " + permission);
        } catch (java.lang.SecurityException expected) {
            junit.framework.TestCase.assertNotNull("security exception's error message.", expected.getMessage());
            junit.framework.TestCase.assertTrue(((("error message should contain \"" + permission) + "\". Got: \"") + expected.getMessage()) + "\".", expected.getMessage().contains(permission));
        }
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
            if ((!field.getType().isPrimitive()) && (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))) {
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

