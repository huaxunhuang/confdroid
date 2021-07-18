/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.provider;


/**
 * Base class for a search indexable provider. Such provider offers data to be indexed either
 * as a reference to an XML file (like a {@link android.preference.PreferenceScreen}) or either
 * as some raw data.
 *
 * @see SearchIndexableResource
 * @see SearchIndexableData
 * @see SearchIndexablesContract

To create a search indexables provider, extend this class, then implement the abstract methods,
and add it to your manifest like this:

<pre class="prettyprint">&lt;manifest&gt;
...
&lt;application&gt;
...
&lt;provider
android:name="com.example.MyIndexablesProvider"
android:authorities="com.example.myindexablesprovider"
android:exported="true"
android:grantUriPermissions="true"
android:permission="android.permission.READ_SEARCH_INDEXABLES"
&lt;intent-filter&gt;
&lt;action android:name="android.content.action.SEARCH_INDEXABLES_PROVIDER" /&gt;
&lt;/intent-filter&gt;
&lt;/provider&gt;
...
&lt;/application&gt;
&lt;/manifest&gt;</pre>
<p>
When defining your provider, you must protect it with
{@link android.Manifest.permission#READ_SEARCH_INDEXABLES}, which is a permission only the system
can obtain.
</p>
 * @unknown 
 */
@android.annotation.SystemApi
public abstract class SearchIndexablesProvider extends android.content.ContentProvider {
    private static final java.lang.String TAG = "IndexablesProvider";

    private java.lang.String mAuthority;

    private android.content.UriMatcher mMatcher;

    private static final int MATCH_RES_CODE = 1;

    private static final int MATCH_RAW_CODE = 2;

    private static final int MATCH_NON_INDEXABLE_KEYS_CODE = 3;

    /**
     * Implementation is provided by the parent class.
     */
    @java.lang.Override
    public void attachInfo(android.content.Context context, android.content.pm.ProviderInfo info) {
        mAuthority = info.authority;
        mMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);
        mMatcher.addURI(mAuthority, android.provider.SearchIndexablesContract.INDEXABLES_XML_RES_PATH, android.provider.SearchIndexablesProvider.MATCH_RES_CODE);
        mMatcher.addURI(mAuthority, android.provider.SearchIndexablesContract.INDEXABLES_RAW_PATH, android.provider.SearchIndexablesProvider.MATCH_RAW_CODE);
        mMatcher.addURI(mAuthority, android.provider.SearchIndexablesContract.NON_INDEXABLES_KEYS_PATH, android.provider.SearchIndexablesProvider.MATCH_NON_INDEXABLE_KEYS_CODE);
        // Sanity check our setup
        if (!info.exported) {
            throw new java.lang.SecurityException("Provider must be exported");
        }
        if (!info.grantUriPermissions) {
            throw new java.lang.SecurityException("Provider must grantUriPermissions");
        }
        if (!android.provider.android.Manifest.permission.READ_SEARCH_INDEXABLES.equals(info.readPermission)) {
            throw new java.lang.SecurityException("Provider must be protected by READ_SEARCH_INDEXABLES");
        }
        super.attachInfo(context, info);
    }

    @java.lang.Override
    public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        switch (mMatcher.match(uri)) {
            case android.provider.SearchIndexablesProvider.MATCH_RES_CODE :
                return queryXmlResources(null);
            case android.provider.SearchIndexablesProvider.MATCH_RAW_CODE :
                return queryRawData(null);
            case android.provider.SearchIndexablesProvider.MATCH_NON_INDEXABLE_KEYS_CODE :
                return queryNonIndexableKeys(null);
            default :
                throw new java.lang.UnsupportedOperationException("Unknown Uri " + uri);
        }
    }

    /**
     * Returns all {@link android.provider.SearchIndexablesContract.XmlResource}.
     *
     * Those are Xml resource IDs to some {@link android.preference.PreferenceScreen}.
     *
     * @param projection
     * 		list of {@link android.provider.SearchIndexablesContract.XmlResource}
     * 		columns to put into the cursor. If {@code null} all supported columns
     * 		should be included.
     */
    public abstract android.database.Cursor queryXmlResources(java.lang.String[] projection);

    /**
     * Returns all {@link android.provider.SearchIndexablesContract.RawData}.
     *
     * Those are the raw indexable data.
     *
     * @param projection
     * 		list of {@link android.provider.SearchIndexablesContract.RawData} columns
     * 		to put into the cursor. If {@code null} all supported columns should be
     * 		included.
     */
    public abstract android.database.Cursor queryRawData(java.lang.String[] projection);

    /**
     * Returns all {@link android.provider.SearchIndexablesContract.NonIndexableKey}.
     *
     * Those are the non indexable data keys.
     *
     * @param projection
     * 		list of {@link android.provider.SearchIndexablesContract.NonIndexableKey}
     * 		columns to put into the cursor. If {@code null} all supported columns
     * 		should be included.
     */
    public abstract android.database.Cursor queryNonIndexableKeys(java.lang.String[] projection);

    @java.lang.Override
    public java.lang.String getType(android.net.Uri uri) {
        switch (mMatcher.match(uri)) {
            case android.provider.SearchIndexablesProvider.MATCH_RES_CODE :
                return android.provider.SearchIndexablesContract.XmlResource.MIME_TYPE;
            case android.provider.SearchIndexablesProvider.MATCH_RAW_CODE :
                return android.provider.SearchIndexablesContract.RawData.MIME_TYPE;
            case android.provider.SearchIndexablesProvider.MATCH_NON_INDEXABLE_KEYS_CODE :
                return android.provider.SearchIndexablesContract.NonIndexableKey.MIME_TYPE;
            default :
                throw new java.lang.IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * Implementation is provided by the parent class. Throws by default, and cannot be overriden.
     */
    @java.lang.Override
    public final android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
        throw new java.lang.UnsupportedOperationException("Insert not supported");
    }

    /**
     * Implementation is provided by the parent class. Throws by default, and cannot be overriden.
     */
    @java.lang.Override
    public final int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("Delete not supported");
    }

    /**
     * Implementation is provided by the parent class. Throws by default, and cannot be overriden.
     */
    @java.lang.Override
    public final int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("Update not supported");
    }
}

