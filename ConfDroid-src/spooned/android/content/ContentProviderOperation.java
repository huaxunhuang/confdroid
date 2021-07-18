/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.content;


/**
 * Represents a single operation to be performed as part of a batch of operations.
 *
 * @see ContentProvider#applyBatch(ArrayList)
 */
public class ContentProviderOperation implements android.os.Parcelable {
    /**
     *
     *
     * @unknown exposed for unit tests
     */
    @android.annotation.UnsupportedAppUsage
    public static final int TYPE_INSERT = 1;

    /**
     *
     *
     * @unknown exposed for unit tests
     */
    @android.annotation.UnsupportedAppUsage
    public static final int TYPE_UPDATE = 2;

    /**
     *
     *
     * @unknown exposed for unit tests
     */
    @android.annotation.UnsupportedAppUsage
    public static final int TYPE_DELETE = 3;

    /**
     *
     *
     * @unknown exposed for unit tests
     */
    public static final int TYPE_ASSERT = 4;

    @android.annotation.UnsupportedAppUsage
    private final int mType;

    @android.annotation.UnsupportedAppUsage
    private final android.net.Uri mUri;

    @android.annotation.UnsupportedAppUsage
    private final java.lang.String mSelection;

    private final java.lang.String[] mSelectionArgs;

    private final android.content.ContentValues mValues;

    private final java.lang.Integer mExpectedCount;

    private final android.content.ContentValues mValuesBackReferences;

    private final java.util.Map<java.lang.Integer, java.lang.Integer> mSelectionArgsBackReferences;

    private final boolean mYieldAllowed;

    private final boolean mFailureAllowed;

    private static final java.lang.String TAG = "ContentProviderOperation";

    /**
     * Creates a {@link ContentProviderOperation} by copying the contents of a
     * {@link Builder}.
     */
    private ContentProviderOperation(android.content.ContentProviderOperation.Builder builder) {
        mType = builder.mType;
        mUri = builder.mUri;
        mValues = builder.mValues;
        mSelection = builder.mSelection;
        mSelectionArgs = builder.mSelectionArgs;
        mExpectedCount = builder.mExpectedCount;
        mSelectionArgsBackReferences = builder.mSelectionArgsBackReferences;
        mValuesBackReferences = builder.mValuesBackReferences;
        mYieldAllowed = builder.mYieldAllowed;
        mFailureAllowed = builder.mFailureAllowed;
    }

    private ContentProviderOperation(android.os.Parcel source) {
        mType = source.readInt();
        mUri = Uri.CREATOR.createFromParcel(source);
        mValues = (source.readInt() != 0) ? this.CREATOR.createFromParcel(source) : null;
        mSelection = (source.readInt() != 0) ? source.readString() : null;
        mSelectionArgs = (source.readInt() != 0) ? source.readStringArray() : null;
        mExpectedCount = (source.readInt() != 0) ? source.readInt() : null;
        mValuesBackReferences = (source.readInt() != 0) ? this.CREATOR.createFromParcel(source) : null;
        mSelectionArgsBackReferences = (source.readInt() != 0) ? new java.util.HashMap<java.lang.Integer, java.lang.Integer>() : null;
        if (mSelectionArgsBackReferences != null) {
            final int count = source.readInt();
            for (int i = 0; i < count; i++) {
                mSelectionArgsBackReferences.put(source.readInt(), source.readInt());
            }
        }
        mYieldAllowed = source.readInt() != 0;
        mFailureAllowed = source.readInt() != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public ContentProviderOperation(android.content.ContentProviderOperation cpo, android.net.Uri withUri) {
        mType = cpo.mType;
        mUri = withUri;
        mValues = cpo.mValues;
        mSelection = cpo.mSelection;
        mSelectionArgs = cpo.mSelectionArgs;
        mExpectedCount = cpo.mExpectedCount;
        mSelectionArgsBackReferences = cpo.mSelectionArgsBackReferences;
        mValuesBackReferences = cpo.mValuesBackReferences;
        mYieldAllowed = cpo.mYieldAllowed;
        mFailureAllowed = cpo.mFailureAllowed;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mType);
        android.net.Uri.writeToParcel(dest, mUri);
        if (mValues != null) {
            dest.writeInt(1);
            mValues.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        if (mSelection != null) {
            dest.writeInt(1);
            dest.writeString(mSelection);
        } else {
            dest.writeInt(0);
        }
        if (mSelectionArgs != null) {
            dest.writeInt(1);
            dest.writeStringArray(mSelectionArgs);
        } else {
            dest.writeInt(0);
        }
        if (mExpectedCount != null) {
            dest.writeInt(1);
            dest.writeInt(mExpectedCount);
        } else {
            dest.writeInt(0);
        }
        if (mValuesBackReferences != null) {
            dest.writeInt(1);
            mValuesBackReferences.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        if (mSelectionArgsBackReferences != null) {
            dest.writeInt(1);
            dest.writeInt(mSelectionArgsBackReferences.size());
            for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> entry : mSelectionArgsBackReferences.entrySet()) {
                dest.writeInt(entry.getKey());
                dest.writeInt(entry.getValue());
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mYieldAllowed ? 1 : 0);
        dest.writeInt(mFailureAllowed ? 1 : 0);
    }

    /**
     * Create a {@link Builder} suitable for building an insert {@link ContentProviderOperation}.
     *
     * @param uri
     * 		The {@link Uri} that is the target of the insert.
     * @return a {@link Builder}
     */
    public static android.content.ContentProviderOperation.Builder newInsert(android.net.Uri uri) {
        return new android.content.ContentProviderOperation.Builder(android.content.ContentProviderOperation.TYPE_INSERT, uri);
    }

    /**
     * Create a {@link Builder} suitable for building an update {@link ContentProviderOperation}.
     *
     * @param uri
     * 		The {@link Uri} that is the target of the update.
     * @return a {@link Builder}
     */
    public static android.content.ContentProviderOperation.Builder newUpdate(android.net.Uri uri) {
        return new android.content.ContentProviderOperation.Builder(android.content.ContentProviderOperation.TYPE_UPDATE, uri);
    }

    /**
     * Create a {@link Builder} suitable for building a delete {@link ContentProviderOperation}.
     *
     * @param uri
     * 		The {@link Uri} that is the target of the delete.
     * @return a {@link Builder}
     */
    public static android.content.ContentProviderOperation.Builder newDelete(android.net.Uri uri) {
        return new android.content.ContentProviderOperation.Builder(android.content.ContentProviderOperation.TYPE_DELETE, uri);
    }

    /**
     * Create a {@link Builder} suitable for building a
     * {@link ContentProviderOperation} to assert a set of values as provided
     * through {@link Builder#withValues(ContentValues)}.
     */
    public static android.content.ContentProviderOperation.Builder newAssertQuery(android.net.Uri uri) {
        return new android.content.ContentProviderOperation.Builder(android.content.ContentProviderOperation.TYPE_ASSERT, uri);
    }

    /**
     * Gets the Uri for the target of the operation.
     */
    public android.net.Uri getUri() {
        return mUri;
    }

    /**
     * Returns true if the operation allows yielding the database to other transactions
     * if the database is contended.
     *
     * @see android.database.sqlite.SQLiteDatabase#yieldIfContendedSafely()
     */
    public boolean isYieldAllowed() {
        return mYieldAllowed;
    }

    /**
     * {@hide }
     */
    public boolean isFailureAllowed() {
        return mFailureAllowed;
    }

    /**
     *
     *
     * @unknown exposed for unit tests
     */
    @android.annotation.UnsupportedAppUsage
    public int getType() {
        return mType;
    }

    /**
     * Returns true if the operation represents an insertion.
     *
     * @see #newInsert
     */
    public boolean isInsert() {
        return mType == android.content.ContentProviderOperation.TYPE_INSERT;
    }

    /**
     * Returns true if the operation represents a deletion.
     *
     * @see #newDelete
     */
    public boolean isDelete() {
        return mType == android.content.ContentProviderOperation.TYPE_DELETE;
    }

    /**
     * Returns true if the operation represents an update.
     *
     * @see #newUpdate
     */
    public boolean isUpdate() {
        return mType == android.content.ContentProviderOperation.TYPE_UPDATE;
    }

    /**
     * Returns true if the operation represents an assert query.
     *
     * @see #newAssertQuery
     */
    public boolean isAssertQuery() {
        return mType == android.content.ContentProviderOperation.TYPE_ASSERT;
    }

    /**
     * Returns true if the operation represents an insertion, deletion, or update.
     *
     * @see #isInsert
     * @see #isDelete
     * @see #isUpdate
     */
    public boolean isWriteOperation() {
        return ((mType == android.content.ContentProviderOperation.TYPE_DELETE) || (mType == android.content.ContentProviderOperation.TYPE_INSERT)) || (mType == android.content.ContentProviderOperation.TYPE_UPDATE);
    }

    /**
     * Returns true if the operation represents an assert query.
     *
     * @see #isAssertQuery
     */
    public boolean isReadOperation() {
        return mType == android.content.ContentProviderOperation.TYPE_ASSERT;
    }

    /**
     * Applies this operation using the given provider. The backRefs array is used to resolve any
     * back references that were requested using
     * {@link Builder#withValueBackReferences(ContentValues)} and
     * {@link Builder#withSelectionBackReference}.
     *
     * @param provider
     * 		the {@link ContentProvider} on which this batch is applied
     * @param backRefs
     * 		a {@link ContentProviderResult} array that will be consulted
     * 		to resolve any requested back references.
     * @param numBackRefs
     * 		the number of valid results on the backRefs array.
     * @return a {@link ContentProviderResult} that contains either the {@link Uri} of the inserted
    row if this was an insert otherwise the number of rows affected.
     * @throws OperationApplicationException
     * 		thrown if either the insert fails or
     * 		if the number of rows affected didn't match the expected count
     */
    public android.content.ContentProviderResult apply(android.content.ContentProvider provider, android.content.ContentProviderResult[] backRefs, int numBackRefs) throws android.content.OperationApplicationException {
        if (mFailureAllowed) {
            try {
                return applyInternal(provider, backRefs, numBackRefs);
            } catch (java.lang.Exception e) {
                return new android.content.ContentProviderResult(e.getMessage());
            }
        } else {
            return applyInternal(provider, backRefs, numBackRefs);
        }
    }

    private android.content.ContentProviderResult applyInternal(android.content.ContentProvider provider, android.content.ContentProviderResult[] backRefs, int numBackRefs) throws android.content.OperationApplicationException {
        android.content.ContentValues values = resolveValueBackReferences(backRefs, numBackRefs);
        java.lang.String[] selectionArgs = resolveSelectionArgsBackReferences(backRefs, numBackRefs);
        if (mType == android.content.ContentProviderOperation.TYPE_INSERT) {
            final android.net.Uri newUri = provider.insert(mUri, values);
            if (newUri != null) {
                return new android.content.ContentProviderResult(newUri);
            } else {
                throw new android.content.OperationApplicationException(("Insert into " + mUri) + " returned no result");
            }
        }
        final int numRows;
        if (mType == android.content.ContentProviderOperation.TYPE_DELETE) {
            numRows = provider.delete(mUri, mSelection, selectionArgs);
        } else
            if (mType == android.content.ContentProviderOperation.TYPE_UPDATE) {
                numRows = provider.update(mUri, values, mSelection, selectionArgs);
            } else
                if (mType == android.content.ContentProviderOperation.TYPE_ASSERT) {
                    // Assert that all rows match expected values
                    java.lang.String[] projection = null;
                    if (values != null) {
                        // Build projection map from expected values
                        final java.util.ArrayList<java.lang.String> projectionList = new java.util.ArrayList<java.lang.String>();
                        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : values.valueSet()) {
                            projectionList.add(entry.getKey());
                        }
                        projection = projectionList.toArray(new java.lang.String[projectionList.size()]);
                    }
                    final android.database.Cursor cursor = provider.query(mUri, projection, mSelection, selectionArgs, null);
                    try {
                        numRows = cursor.getCount();
                        if (projection != null) {
                            while (cursor.moveToNext()) {
                                for (int i = 0; i < projection.length; i++) {
                                    final java.lang.String cursorValue = cursor.getString(i);
                                    final java.lang.String expectedValue = values.getAsString(projection[i]);
                                    if (!android.text.TextUtils.equals(cursorValue, expectedValue)) {
                                        // Throw exception when expected values don't match
                                        throw new android.content.OperationApplicationException((((("Found value " + cursorValue) + " when expected ") + expectedValue) + " for column ") + projection[i]);
                                    }
                                }
                            } 
                        }
                    } finally {
                        cursor.close();
                    }
                } else {
                    throw new java.lang.IllegalStateException("bad type, " + mType);
                }


        if ((mExpectedCount != null) && (mExpectedCount != numRows)) {
            throw new android.content.OperationApplicationException((("Expected " + mExpectedCount) + " rows but actual ") + numRows);
        }
        return new android.content.ContentProviderResult(numRows);
    }

    /**
     * The ContentValues back references are represented as a ContentValues object where the
     * key refers to a column and the value is an index of the back reference whose
     * valued should be associated with the column.
     * <p>
     * This is intended to be a private method but it is exposed for
     * unit testing purposes
     *
     * @param backRefs
     * 		an array of previous results
     * @param numBackRefs
     * 		the number of valid previous results in backRefs
     * @return the ContentValues that should be used in this operation application after
    expansion of back references. This can be called if either mValues or mValuesBackReferences
    is null
     */
    public android.content.ContentValues resolveValueBackReferences(android.content.ContentProviderResult[] backRefs, int numBackRefs) {
        if (mValuesBackReferences == null) {
            return mValues;
        }
        final android.content.ContentValues values;
        if (mValues == null) {
            values = new android.content.ContentValues();
        } else {
            values = new android.content.ContentValues(mValues);
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : mValuesBackReferences.valueSet()) {
            java.lang.String key = entry.getKey();
            java.lang.Integer backRefIndex = mValuesBackReferences.getAsInteger(key);
            if (backRefIndex == null) {
                android.util.Log.e(android.content.ContentProviderOperation.TAG, this.toString());
                throw new java.lang.IllegalArgumentException(("values backref " + key) + " is not an integer");
            }
            values.put(key, backRefToValue(backRefs, numBackRefs, backRefIndex));
        }
        return values;
    }

    /**
     * The Selection Arguments back references are represented as a Map of Integer->Integer where
     * the key is an index into the selection argument array (see {@link Builder#withSelection})
     * and the value is the index of the previous result that should be used for that selection
     * argument array slot.
     * <p>
     * This is intended to be a private method but it is exposed for
     * unit testing purposes
     *
     * @param backRefs
     * 		an array of previous results
     * @param numBackRefs
     * 		the number of valid previous results in backRefs
     * @return the ContentValues that should be used in this operation application after
    expansion of back references. This can be called if either mValues or mValuesBackReferences
    is null
     */
    public java.lang.String[] resolveSelectionArgsBackReferences(android.content.ContentProviderResult[] backRefs, int numBackRefs) {
        if (mSelectionArgsBackReferences == null) {
            return mSelectionArgs;
        }
        java.lang.String[] newArgs = new java.lang.String[mSelectionArgs.length];
        java.lang.System.arraycopy(mSelectionArgs, 0, newArgs, 0, mSelectionArgs.length);
        for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> selectionArgBackRef : mSelectionArgsBackReferences.entrySet()) {
            final java.lang.Integer selectionArgIndex = selectionArgBackRef.getKey();
            final int backRefIndex = selectionArgBackRef.getValue();
            newArgs[selectionArgIndex] = java.lang.String.valueOf(backRefToValue(backRefs, numBackRefs, backRefIndex));
        }
        return newArgs;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((("mType: " + mType) + ", mUri: ") + mUri) + ", mSelection: ") + mSelection) + ", mExpectedCount: ") + mExpectedCount) + ", mYieldAllowed: ") + mYieldAllowed) + ", mValues: ") + mValues) + ", mValuesBackReferences: ") + mValuesBackReferences) + ", mSelectionArgsBackReferences: ") + mSelectionArgsBackReferences;
    }

    /**
     * Return the string representation of the requested back reference.
     *
     * @param backRefs
     * 		an array of results
     * @param numBackRefs
     * 		the number of items in the backRefs array that are valid
     * @param backRefIndex
     * 		which backRef to be used
     * @throws ArrayIndexOutOfBoundsException
     * 		thrown if the backRefIndex is larger than
     * 		the numBackRefs
     * @return the string representation of the requested back reference.
     */
    private long backRefToValue(android.content.ContentProviderResult[] backRefs, int numBackRefs, java.lang.Integer backRefIndex) {
        if (backRefIndex >= numBackRefs) {
            android.util.Log.e(android.content.ContentProviderOperation.TAG, this.toString());
            throw new java.lang.ArrayIndexOutOfBoundsException(((("asked for back ref " + backRefIndex) + " but there are only ") + numBackRefs) + " back refs");
        }
        android.content.ContentProviderResult backRef = backRefs[backRefIndex];
        long backRefValue;
        if (backRef.uri != null) {
            backRefValue = android.content.ContentUris.parseId(backRef.uri);
        } else {
            backRefValue = backRef.count;
        }
        return backRefValue;
    }

    public int describeContents() {
        return 0;
    }

    @android.annotation.NonNull
    public static final android.content.Creator<android.content.ContentProviderOperation> CREATOR = new android.content.Creator<android.content.ContentProviderOperation>() {
        public android.content.ContentProviderOperation createFromParcel(android.os.Parcel source) {
            return new android.content.ContentProviderOperation(source);
        }

        public android.content.ContentProviderOperation[] newArray(int size) {
            return new android.content.ContentProviderOperation[size];
        }
    };

    /**
     * Used to add parameters to a {@link ContentProviderOperation}. The {@link Builder} is
     * first created by calling {@link ContentProviderOperation#newInsert(android.net.Uri)},
     * {@link ContentProviderOperation#newUpdate(android.net.Uri)},
     * {@link ContentProviderOperation#newDelete(android.net.Uri)} or
     * {@link ContentProviderOperation#newAssertQuery(Uri)}. The withXXX methods
     * can then be used to add parameters to the builder. See the specific methods to find for
     * which {@link Builder} type each is allowed. Call {@link #build} to create the
     * {@link ContentProviderOperation} once all the parameters have been supplied.
     */
    public static class Builder {
        private final int mType;

        private final android.net.Uri mUri;

        private java.lang.String mSelection;

        private java.lang.String[] mSelectionArgs;

        private android.content.ContentValues mValues;

        private java.lang.Integer mExpectedCount;

        private android.content.ContentValues mValuesBackReferences;

        private java.util.Map<java.lang.Integer, java.lang.Integer> mSelectionArgsBackReferences;

        private boolean mYieldAllowed;

        private boolean mFailureAllowed;

        /**
         * Create a {@link Builder} of a given type. The uri must not be null.
         */
        private Builder(int type, android.net.Uri uri) {
            if (uri == null) {
                throw new java.lang.IllegalArgumentException("uri must not be null");
            }
            mType = type;
            mUri = uri;
        }

        /**
         * Create a ContentProviderOperation from this {@link Builder}.
         */
        public android.content.ContentProviderOperation build() {
            if (mType == android.content.ContentProviderOperation.TYPE_UPDATE) {
                if (((mValues == null) || mValues.isEmpty()) && ((mValuesBackReferences == null) || mValuesBackReferences.isEmpty())) {
                    throw new java.lang.IllegalArgumentException("Empty values");
                }
            }
            if (mType == android.content.ContentProviderOperation.TYPE_ASSERT) {
                if ((((mValues == null) || mValues.isEmpty()) && ((mValuesBackReferences == null) || mValuesBackReferences.isEmpty())) && (mExpectedCount == null)) {
                    throw new java.lang.IllegalArgumentException("Empty values");
                }
            }
            return new android.content.ContentProviderOperation(this);
        }

        /**
         * Add a {@link ContentValues} of back references. The key is the name of the column
         * and the value is an integer that is the index of the previous result whose
         * value should be used for the column. The value is added as a {@link String}.
         * A column value from the back references takes precedence over a value specified in
         * {@link #withValues}.
         * This can only be used with builders of type insert, update, or assert.
         *
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withValueBackReferences(android.content.ContentValues backReferences) {
            if (((mType != android.content.ContentProviderOperation.TYPE_INSERT) && (mType != android.content.ContentProviderOperation.TYPE_UPDATE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
            }
            mValuesBackReferences = backReferences;
            return this;
        }

        /**
         * Add a ContentValues back reference.
         * A column value from the back references takes precedence over a value specified in
         * {@link #withValues}.
         * This can only be used with builders of type insert, update, or assert.
         *
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withValueBackReference(java.lang.String key, int previousResult) {
            if (((mType != android.content.ContentProviderOperation.TYPE_INSERT) && (mType != android.content.ContentProviderOperation.TYPE_UPDATE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
            }
            if (mValuesBackReferences == null) {
                mValuesBackReferences = new android.content.ContentValues();
            }
            mValuesBackReferences.put(key, previousResult);
            return this;
        }

        /**
         * Add a back references as a selection arg. Any value at that index of the selection arg
         * that was specified by {@link #withSelection} will be overwritten.
         * This can only be used with builders of type update, delete, or assert.
         *
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withSelectionBackReference(int selectionArgIndex, int previousResult) {
            if (((mType != android.content.ContentProviderOperation.TYPE_UPDATE) && (mType != android.content.ContentProviderOperation.TYPE_DELETE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only updates, deletes, and asserts " + "can have selection back-references");
            }
            if (mSelectionArgsBackReferences == null) {
                mSelectionArgsBackReferences = new java.util.HashMap<java.lang.Integer, java.lang.Integer>();
            }
            mSelectionArgsBackReferences.put(selectionArgIndex, previousResult);
            return this;
        }

        /**
         * The ContentValues to use. This may be null. These values may be overwritten by
         * the corresponding value specified by {@link #withValueBackReference} or by
         * future calls to {@link #withValues} or {@link #withValue}.
         * This can only be used with builders of type insert, update, or assert.
         *
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withValues(android.content.ContentValues values) {
            if (((mType != android.content.ContentProviderOperation.TYPE_INSERT) && (mType != android.content.ContentProviderOperation.TYPE_UPDATE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only inserts, updates, and asserts can have values");
            }
            if (mValues == null) {
                mValues = new android.content.ContentValues();
            }
            mValues.putAll(values);
            return this;
        }

        /**
         * A value to insert or update. This value may be overwritten by
         * the corresponding value specified by {@link #withValueBackReference}.
         * This can only be used with builders of type insert, update, or assert.
         *
         * @param key
         * 		the name of this value
         * @param value
         * 		the value itself. the type must be acceptable for insertion by
         * 		{@link ContentValues#put}
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withValue(java.lang.String key, java.lang.Object value) {
            if (((mType != android.content.ContentProviderOperation.TYPE_INSERT) && (mType != android.content.ContentProviderOperation.TYPE_UPDATE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only inserts and updates can have values");
            }
            if (mValues == null) {
                mValues = new android.content.ContentValues();
            }
            if (value == null) {
                mValues.putNull(key);
            } else
                if (value instanceof java.lang.String) {
                    mValues.put(key, ((java.lang.String) (value)));
                } else
                    if (value instanceof java.lang.Byte) {
                        mValues.put(key, ((java.lang.Byte) (value)));
                    } else
                        if (value instanceof java.lang.Short) {
                            mValues.put(key, ((java.lang.Short) (value)));
                        } else
                            if (value instanceof java.lang.Integer) {
                                mValues.put(key, ((java.lang.Integer) (value)));
                            } else
                                if (value instanceof java.lang.Long) {
                                    mValues.put(key, ((java.lang.Long) (value)));
                                } else
                                    if (value instanceof java.lang.Float) {
                                        mValues.put(key, ((java.lang.Float) (value)));
                                    } else
                                        if (value instanceof java.lang.Double) {
                                            mValues.put(key, ((java.lang.Double) (value)));
                                        } else
                                            if (value instanceof java.lang.Boolean) {
                                                mValues.put(key, ((java.lang.Boolean) (value)));
                                            } else
                                                if (value instanceof byte[]) {
                                                    mValues.put(key, ((byte[]) (value)));
                                                } else {
                                                    throw new java.lang.IllegalArgumentException("bad value type: " + value.getClass().getName());
                                                }









            return this;
        }

        /**
         * The selection and arguments to use. An occurrence of '?' in the selection will be
         * replaced with the corresponding occurrence of the selection argument. Any of the
         * selection arguments may be overwritten by a selection argument back reference as
         * specified by {@link #withSelectionBackReference}.
         * This can only be used with builders of type update, delete, or assert.
         *
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withSelection(java.lang.String selection, java.lang.String[] selectionArgs) {
            if (((mType != android.content.ContentProviderOperation.TYPE_UPDATE) && (mType != android.content.ContentProviderOperation.TYPE_DELETE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only updates, deletes, and asserts can have selections");
            }
            mSelection = selection;
            if (selectionArgs == null) {
                mSelectionArgs = null;
            } else {
                mSelectionArgs = new java.lang.String[selectionArgs.length];
                java.lang.System.arraycopy(selectionArgs, 0, mSelectionArgs, 0, selectionArgs.length);
            }
            return this;
        }

        /**
         * If set then if the number of rows affected by this operation does not match
         * this count {@link OperationApplicationException} will be throw.
         * This can only be used with builders of type update, delete, or assert.
         *
         * @return this builder, to allow for chaining.
         */
        public android.content.ContentProviderOperation.Builder withExpectedCount(int count) {
            if (((mType != android.content.ContentProviderOperation.TYPE_UPDATE) && (mType != android.content.ContentProviderOperation.TYPE_DELETE)) && (mType != android.content.ContentProviderOperation.TYPE_ASSERT)) {
                throw new java.lang.IllegalArgumentException("only updates, deletes, and asserts can have expected counts");
            }
            mExpectedCount = count;
            return this;
        }

        /**
         * If set to true then the operation allows yielding the database to other transactions
         * if the database is contended.
         *
         * @return this builder, to allow for chaining.
         * @see android.database.sqlite.SQLiteDatabase#yieldIfContendedSafely()
         */
        public android.content.ContentProviderOperation.Builder withYieldAllowed(boolean yieldAllowed) {
            mYieldAllowed = yieldAllowed;
            return this;
        }

        /**
         * {@hide }
         */
        public android.content.ContentProviderOperation.Builder withFailureAllowed(boolean failureAllowed) {
            mFailureAllowed = failureAllowed;
            return this;
        }
    }
}

