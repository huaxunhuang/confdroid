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
 * If you would like to test a single content provider with an
 * {@link InstrumentationTestCase}, this provides some of the boiler plate in {@link #setUp} and
 * {@link #tearDown}.
 *
 * @deprecated this class extends InstrumentationTestCase but should extend AndroidTestCase. Use
ProviderTestCase2, which corrects this problem, instead.
 */
@java.lang.Deprecated
public abstract class ProviderTestCase<T extends android.content.ContentProvider> extends android.test.InstrumentationTestCase {
    java.lang.Class<T> mProviderClass;

    java.lang.String mProviderAuthority;

    private android.test.IsolatedContext mProviderContext;

    private android.test.mock.MockContentResolver mResolver;

    public ProviderTestCase(java.lang.Class<T> providerClass, java.lang.String providerAuthority) {
        mProviderClass = providerClass;
        mProviderAuthority = providerAuthority;
    }

    /**
     * The content provider that will be set up for use in each test method.
     */
    private T mProvider;

    public T getProvider() {
        return mProvider;
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mResolver = new android.test.mock.MockContentResolver();
        final java.lang.String filenamePrefix = "test.";
        android.test.RenamingDelegatingContext targetContextWrapper = // The context that most methods are delegated to
        // The context that file methods are delegated to
        new android.test.RenamingDelegatingContext(new android.test.mock.MockContext(), getInstrumentation().getTargetContext(), filenamePrefix);
        mProviderContext = new android.test.IsolatedContext(mResolver, targetContextWrapper);
        mProvider = android.test.ProviderTestCase2.createProviderForTest(mProviderContext, mProviderClass, mProviderAuthority);
        mResolver.addProvider(mProviderAuthority, getProvider());
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

    public android.test.mock.MockContentResolver getMockContentResolver() {
        return mResolver;
    }

    public android.test.IsolatedContext getMockContext() {
        return mProviderContext;
    }

    public static <T extends android.content.ContentProvider> android.content.ContentResolver newResolverWithContentProviderFromSql(android.content.Context targetContext, java.lang.Class<T> providerClass, java.lang.String authority, java.lang.String databaseName, int databaseVersion, java.lang.String sql) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        final java.lang.String filenamePrefix = "test.";
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

