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
 * This test case class provides a framework for testing a single
 * {@link ContentProvider} and for testing your app code with an
 * isolated content provider. Instead of using the system map of
 * providers that is based on the manifests of other applications, the test
 * case creates its own internal map. It then uses this map to resolve providers
 * given an authority. This allows you to inject test providers and to null out
 * providers that you do not want to use.
 * <p>
 *      This test case also sets up the following mock objects:
 * </p>
 * <ul>
 *      <li>
 *          An {@link android.test.IsolatedContext} that stubs out Context methods that might
 *          affect the rest of the running system, while allowing tests to do real file and
 *          database work.
 *      </li>
 *      <li>
 *          A {@link android.test.mock.MockContentResolver} that provides the functionality of a
 *          regular content resolver, but uses {@link IsolatedContext}. It stubs out
 *          {@link ContentResolver#notifyChange(Uri, ContentObserver, boolean)} to
 *          prevent the test from affecting the running system.
 *      </li>
 *      <li>
 *          An instance of the provider under test, running in an {@link IsolatedContext}.
 *      </li>
 * </ul>
 * <p>
 *      This framework is set up automatically by the base class' {@link #setUp()} method. If you
 *      override this method, you must call the super method as the first statement in
 *      your override.
 * </p>
 * <p>
 *     In order for their tests to be run, concrete subclasses must provide their own
 *     constructor with no arguments. This constructor must call
 *     {@link #ProviderTestCase2(Class, String)} as  its first operation.
 * </p>
 * For more information on content provider testing, please see
 * <a href="{@docRoot }tools/testing/contentprovider_testing.html">Content Provider Testing</a>.
 */
public abstract class ProviderTestCase2<T extends android.content.ContentProvider> extends android.test.AndroidTestCase {
    java.lang.Class<T> mProviderClass;

    java.lang.String mProviderAuthority;

    private android.test.IsolatedContext mProviderContext;

    private android.test.mock.MockContentResolver mResolver;

    private class MockContext2 extends android.test.mock.MockContext {
        @java.lang.Override
        public android.content.res.Resources getResources() {
            return getContext().getResources();
        }

        @java.lang.Override
        public java.io.File getDir(java.lang.String name, int mode) {
            // name the directory so the directory will be separated from
            // one created through the regular Context
            return getContext().getDir("mockcontext2_" + name, mode);
        }

        @java.lang.Override
        public android.content.Context getApplicationContext() {
            return this;
        }
    }

    /**
     * Constructor.
     *
     * @param providerClass
     * 		The class name of the provider under test
     * @param providerAuthority
     * 		The provider's authority string
     */
    public ProviderTestCase2(java.lang.Class<T> providerClass, java.lang.String providerAuthority) {
        mProviderClass = providerClass;
        mProviderAuthority = providerAuthority;
    }

    private T mProvider;

    /**
     * Returns the content provider created by this class in the {@link #setUp()} method.
     *
     * @return T An instance of the provider class given as a parameter to the test case class.
     */
    public T getProvider() {
        return mProvider;
    }

    /**
     * Sets up the environment for the test fixture.
     * <p>
     * Creates a new
     * {@link android.test.mock.MockContentResolver}, a new IsolatedContext
     * that isolates the provider's file operations, and a new instance of
     * the provider under test within the isolated environment.
     * </p>
     *
     * @throws Exception
     * 		
     */
    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mResolver = new android.test.mock.MockContentResolver();
        final java.lang.String filenamePrefix = "test.";
        android.test.RenamingDelegatingContext targetContextWrapper = // The context that most methods are
        // delegated to
        // The context that file methods are delegated to
        new android.test.RenamingDelegatingContext(new MockContext2(), getContext(), filenamePrefix);
        mProviderContext = new android.test.IsolatedContext(mResolver, targetContextWrapper);
        mProvider = android.test.ProviderTestCase2.createProviderForTest(mProviderContext, mProviderClass, mProviderAuthority);
        mResolver.addProvider(mProviderAuthority, getProvider());
    }

    /**
     * Creates and sets up a new instance of the provider.
     */
    static <T extends android.content.ContentProvider> T createProviderForTest(android.content.Context context, java.lang.Class<T> providerClass, java.lang.String authority) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        T instance = providerClass.newInstance();
        android.content.pm.ProviderInfo providerInfo = new android.content.pm.ProviderInfo();
        providerInfo.authority = authority;
        instance.attachInfoForTesting(context, providerInfo);
        return instance;
    }

    /**
     * Tears down the environment for the test fixture.
     * <p>
     * Calls {@link android.content.ContentProvider#shutdown()} on the
     * {@link android.content.ContentProvider} represented by mProvider.
     */
    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        mProvider.shutdown();
        super.tearDown();
    }

    /**
     * Gets the {@link MockContentResolver} created by this class during initialization. You
     * must use the methods of this resolver to access the provider under test.
     *
     * @return A {@link MockContentResolver} instance.
     */
    public android.test.mock.MockContentResolver getMockContentResolver() {
        return mResolver;
    }

    /**
     * Gets the {@link IsolatedContext} created by this class during initialization.
     *
     * @return The {@link IsolatedContext} instance
     */
    public android.test.IsolatedContext getMockContext() {
        return mProviderContext;
    }

    /**
     * <p>
     *      Creates a new content provider of the same type as that passed to the test case class,
     *      with an authority name set to the authority parameter, and using an SQLite database as
     *      the underlying data source. The SQL statement parameter is used to create the database.
     *      This method also creates a new {@link MockContentResolver} and adds the provider to it.
     * </p>
     * <p>
     *      Both the new provider and the new resolver are put into an {@link IsolatedContext}
     *      that uses the targetContext parameter for file operations and a {@link MockContext}
     *      for everything else. The IsolatedContext prepends the filenamePrefix parameter to
     *      file, database, and directory names.
     * </p>
     * <p>
     *      This is a convenience method for creating a "mock" provider that can contain test data.
     * </p>
     *
     * @param targetContext
     * 		The context to use as the basis of the IsolatedContext
     * @param filenamePrefix
     * 		A string that is prepended to file, database, and directory names
     * @param providerClass
     * 		The type of the provider being tested
     * @param authority
     * 		The authority string to associated with the test provider
     * @param databaseName
     * 		The name assigned to the database
     * @param databaseVersion
     * 		The version assigned to the database
     * @param sql
     * 		A string containing the SQL statements that are needed to create the desired
     * 		database and its tables. The format is the same as that generated by the
     * 		<a href="http://www.sqlite.org/sqlite.html">sqlite3</a> tool's <code>.dump</code> command.
     * @return ContentResolver A new {@link MockContentResolver} linked to the provider
     * @throws IllegalAccessException
     * 		
     * @throws InstantiationException
     * 		
     */
    public static <T extends android.content.ContentProvider> android.content.ContentResolver newResolverWithContentProviderFromSql(android.content.Context targetContext, java.lang.String filenamePrefix, java.lang.Class<T> providerClass, java.lang.String authority, java.lang.String databaseName, int databaseVersion, java.lang.String sql) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        android.test.mock.MockContentResolver resolver = new android.test.mock.MockContentResolver();
        android.test.RenamingDelegatingContext targetContextWrapper = // The context that most methods are delegated to
        // The context that file methods are delegated to
        new android.test.RenamingDelegatingContext(new android.test.mock.MockContext(), targetContext, filenamePrefix);
        android.content.Context context = new android.test.IsolatedContext(resolver, targetContextWrapper);
        android.database.DatabaseUtils.createDbFromSqlStatements(context, databaseName, databaseVersion, sql);
        T provider = android.test.ProviderTestCase2.createProviderForTest(context, providerClass, authority);
        resolver.addProvider(authority, provider);
        return resolver;
    }
}

