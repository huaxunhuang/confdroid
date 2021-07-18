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


@android.test.suitebuilder.annotation.SmallTest
public class ContentProviderOperationTest extends junit.framework.TestCase {
    private static final android.net.Uri sTestUri1 = android.net.Uri.parse("content://authority/blah");

    private static final android.content.ContentValues sTestValues1;

    private static final java.lang.Class<android.content.ContentProviderOperation.Builder> CLASS_BUILDER = android.content.ContentProviderOperation.Builder.class;

    private static final java.lang.Class<android.content.ContentProviderOperation> CLASS_OPERATION = android.content.ContentProviderOperation.class;

    static {
        sTestValues1 = new android.content.ContentValues();
        android.content.ContentProviderOperationTest.sTestValues1.put("a", 1);
        android.content.ContentProviderOperationTest.sTestValues1.put("b", "two");
    }

    public void testInsert() throws android.content.OperationApplicationException {
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newInsert(android.content.ContentProviderOperationTest.sTestUri1).withValues(android.content.ContentProviderOperationTest.sTestValues1).build();
        android.content.ContentProviderResult result = op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
            public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
                junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.toString(), uri.toString());
                junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestValues1.toString(), values.toString());
                return uri.buildUpon().appendPath("19").build();
            }
        }, null, 0);
        junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.buildUpon().appendPath("19").toString(), result.uri.toString());
    }

    public void testInsertNoValues() throws android.content.OperationApplicationException {
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newInsert(android.content.ContentProviderOperationTest.sTestUri1).build();
        android.content.ContentProviderResult result = op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
            public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
                junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.toString(), uri.toString());
                junit.framework.TestCase.assertNull(values);
                return uri.buildUpon().appendPath("19").build();
            }
        }, null, 0);
        junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.buildUpon().appendPath("19").toString(), result.uri.toString());
    }

    public void testInsertFailed() {
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newInsert(android.content.ContentProviderOperationTest.sTestUri1).withValues(android.content.ContentProviderOperationTest.sTestValues1).build();
        try {
            op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
                public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
                    junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.toString(), uri.toString());
                    junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestValues1.toString(), values.toString());
                    return null;
                }
            }, null, 0);
            junit.framework.TestCase.fail("the apply should have thrown an OperationApplicationException");
        } catch (android.content.OperationApplicationException e) {
            // this is the expected case
        }
    }

    public void testInsertWithBackRefs() throws android.content.OperationApplicationException {
        android.content.ContentProviderResult[] previousResults = new android.content.ContentProviderResult[4];
        previousResults[0] = new android.content.ContentProviderResult(100);
        previousResults[1] = new android.content.ContentProviderResult(101);
        previousResults[2] = new android.content.ContentProviderResult(102);
        previousResults[3] = new android.content.ContentProviderResult(103);
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newInsert(android.content.ContentProviderOperationTest.sTestUri1).withValues(android.content.ContentProviderOperationTest.sTestValues1).withValueBackReference("a1", 3).withValueBackReference("a2", 1).build();
        android.content.ContentProviderResult result = op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
            public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
                junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.toString(), uri.toString());
                android.content.ContentValues expected = new android.content.ContentValues(android.content.ContentProviderOperationTest.sTestValues1);
                expected.put("a1", 103);
                expected.put("a2", 101);
                junit.framework.TestCase.assertEquals(expected.toString(), values.toString());
                return uri.buildUpon().appendPath("19").build();
            }
        }, previousResults, previousResults.length);
        junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.buildUpon().appendPath("19").toString(), result.uri.toString());
    }

    public void testUpdate() throws android.content.OperationApplicationException {
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newInsert(android.content.ContentProviderOperationTest.sTestUri1).withValues(android.content.ContentProviderOperationTest.sTestValues1).build();
        android.content.ContentProviderResult[] backRefs = new android.content.ContentProviderResult[2];
        android.content.ContentProviderResult result = op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
            public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
                junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.toString(), uri.toString());
                junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestValues1.toString(), values.toString());
                return uri.buildUpon().appendPath("19").build();
            }
        }, backRefs, 1);
        junit.framework.TestCase.assertEquals(android.content.ContentProviderOperationTest.sTestUri1.buildUpon().appendPath("19").toString(), result.uri.toString());
    }

    public void testAssert() {
        // Build an operation to assert values match provider
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newAssertQuery(android.content.ContentProviderOperationTest.sTestUri1).withValues(android.content.ContentProviderOperationTest.sTestValues1).build();
        try {
            // Assert that values match from cursor
            android.content.ContentProviderResult result = op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
                public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
                    // Return cursor over specific set of values
                    return getCursor(android.content.ContentProviderOperationTest.sTestValues1, 1);
                }
            }, null, 0);
        } catch (android.content.OperationApplicationException e) {
            junit.framework.TestCase.fail("newAssert() failed");
        }
    }

    public void testAssertNoValues() {
        // Build an operation to assert values match provider
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newAssertQuery(android.content.ContentProviderOperationTest.sTestUri1).withExpectedCount(1).build();
        try {
            // Assert that values match from cursor
            android.content.ContentProviderResult result = op1.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
                public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
                    // Return cursor over specific set of values
                    return getCursor(android.content.ContentProviderOperationTest.sTestValues1, 1);
                }
            }, null, 0);
        } catch (android.content.OperationApplicationException e) {
            junit.framework.TestCase.fail("newAssert() failed");
        }
        android.content.ContentProviderOperation op2 = android.content.ContentProviderOperation.newAssertQuery(android.content.ContentProviderOperationTest.sTestUri1).withExpectedCount(0).build();
        try {
            // Assert that values match from cursor
            android.content.ContentProviderResult result = op2.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
                public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
                    // Return cursor over specific set of values
                    return getCursor(android.content.ContentProviderOperationTest.sTestValues1, 0);
                }
            }, null, 0);
        } catch (android.content.OperationApplicationException e) {
            junit.framework.TestCase.fail("newAssert() failed");
        }
        android.content.ContentProviderOperation op3 = android.content.ContentProviderOperation.newAssertQuery(android.content.ContentProviderOperationTest.sTestUri1).withExpectedCount(2).build();
        try {
            // Assert that values match from cursor
            android.content.ContentProviderResult result = op3.apply(new android.content.ContentProviderOperationTest.TestContentProvider() {
                public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
                    // Return cursor over specific set of values
                    return getCursor(android.content.ContentProviderOperationTest.sTestValues1, 5);
                }
            }, null, 0);
            junit.framework.TestCase.fail("we expect the exception to be thrown");
        } catch (android.content.OperationApplicationException e) {
        }
    }

    /**
     * Build a {@link Cursor} with a single row that contains all values
     * provided through the given {@link ContentValues}.
     */
    private android.database.Cursor getCursor(android.content.ContentValues contentValues, int numRows) {
        final java.util.Set<java.util.Map.Entry<java.lang.String, java.lang.Object>> valueSet = contentValues.valueSet();
        final java.lang.String[] keys = new java.lang.String[valueSet.size()];
        final java.lang.Object[] values = new java.lang.Object[valueSet.size()];
        int i = 0;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : valueSet) {
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
            i++;
        }
        final android.database.MatrixCursor cursor = new android.database.MatrixCursor(keys);
        for (i = 0; i < numRows; i++) {
            cursor.addRow(values);
        }
        return cursor;
    }

    public void testValueBackRefs() {
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("a", "in1");
        values.put("a2", "in2");
        values.put("b", "in3");
        values.put("c", "in4");
        android.content.ContentProviderResult[] previousResults = new android.content.ContentProviderResult[4];
        previousResults[0] = new android.content.ContentProviderResult(100);
        previousResults[1] = new android.content.ContentProviderResult(101);
        previousResults[2] = new android.content.ContentProviderResult(102);
        previousResults[3] = new android.content.ContentProviderResult(103);
        android.content.ContentValues expectedValues = new android.content.ContentValues(values);
        expectedValues.put("a1", ((long) (103)));
        expectedValues.put("a2", ((long) (101)));
        expectedValues.put("a3", ((long) (102)));
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newInsert(android.content.ContentProviderOperationTest.sTestUri1).withValues(values).withValueBackReference("a1", 3).withValueBackReference("a2", 1).withValueBackReference("a3", 2).build();
        android.content.ContentValues v2 = op1.resolveValueBackReferences(previousResults, previousResults.length);
        junit.framework.TestCase.assertEquals(expectedValues, v2);
    }

    public void testSelectionBackRefs() {
        android.content.ContentProviderResult[] previousResults = new android.content.ContentProviderResult[4];
        previousResults[0] = new android.content.ContentProviderResult(100);
        previousResults[1] = new android.content.ContentProviderResult(101);
        previousResults[2] = new android.content.ContentProviderResult(102);
        previousResults[3] = new android.content.ContentProviderResult(103);
        java.lang.String[] selectionArgs = new java.lang.String[]{ "a", null, null, "b", null };
        final android.content.ContentValues values = new android.content.ContentValues();
        values.put("unused", "unused");
        android.content.ContentProviderOperation op1 = android.content.ContentProviderOperation.newUpdate(android.content.ContentProviderOperationTest.sTestUri1).withSelectionBackReference(1, 3).withSelectionBackReference(2, 1).withSelectionBackReference(4, 2).withSelection("unused", selectionArgs).withValues(values).build();
        java.lang.String[] s2 = op1.resolveSelectionArgsBackReferences(previousResults, previousResults.length);
        junit.framework.TestCase.assertEquals("a,103,101,b,102", android.text.TextUtils.join(",", s2));
    }

    public void testParcelingOperation() throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.NoSuchFieldException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        android.content.ContentProviderOperation op1;
        android.content.ContentProviderOperation op2;
        java.util.HashMap<java.lang.Integer, java.lang.Integer> selArgsBackRef = new java.util.HashMap<java.lang.Integer, java.lang.Integer>();
        selArgsBackRef.put(1, 2);
        selArgsBackRef.put(3, 4);
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("v1", "val1");
        values.put("v2", "43");
        android.content.ContentValues valuesBackRef = new android.content.ContentValues();
        values.put("v3", "val3");
        values.put("v4", "44");
        try {
            android.content.ContentProviderOperation.Builder builder = android.content.ContentProviderOperation.newInsert(android.net.Uri.parse("content://goo/bar"));
            builderSetExpectedCount(builder, 42);
            builderSetSelection(builder, "selection");
            builderSetSelectionArgs(builder, new java.lang.String[]{ "a", "b" });
            builderSetSelectionArgsBackReferences(builder, selArgsBackRef);
            builderSetValues(builder, values);
            builderSetValuesBackReferences(builder, valuesBackRef);
            op1 = android.content.ContentProviderOperationTest.newOperationFromBuilder(builder);
            op1.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            op2 = android.content.ContentProviderOperation.CREATOR.createFromParcel(parcel);
            junit.framework.TestCase.assertEquals(android.content.ContentProviderOperation.TYPE_INSERT, operationGetType(op2));
            junit.framework.TestCase.assertEquals("content://goo/bar", operationGetUri(op2).toString());
            junit.framework.TestCase.assertEquals(java.lang.Integer.valueOf(42), operationGetExpectedCount(op2));
            junit.framework.TestCase.assertEquals("selection", operationGetSelection(op2));
            junit.framework.TestCase.assertEquals(2, operationGetSelectionArgs(op2).length);
            junit.framework.TestCase.assertEquals("a", operationGetSelectionArgs(op2)[0]);
            junit.framework.TestCase.assertEquals("b", operationGetSelectionArgs(op2)[1]);
            junit.framework.TestCase.assertEquals(values, operationGetValues(op2));
            junit.framework.TestCase.assertEquals(valuesBackRef, operationGetValuesBackReferences(op2));
            junit.framework.TestCase.assertEquals(2, operationGetSelectionArgsBackReferences(op2).size());
            junit.framework.TestCase.assertEquals(java.lang.Integer.valueOf(2), operationGetSelectionArgsBackReferences(op2).get(1));
            junit.framework.TestCase.assertEquals(java.lang.Integer.valueOf(4), operationGetSelectionArgsBackReferences(op2).get(3));
        } finally {
            parcel.recycle();
        }
        try {
            android.content.ContentProviderOperation.Builder builder = android.content.ContentProviderOperation.newUpdate(android.net.Uri.parse("content://goo/bar"));
            builderSetSelectionArgsBackReferences(builder, selArgsBackRef);
            op1 = android.content.ContentProviderOperationTest.newOperationFromBuilder(builder);
            op1.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            op2 = android.content.ContentProviderOperation.CREATOR.createFromParcel(parcel);
            junit.framework.TestCase.assertEquals(android.content.ContentProviderOperation.TYPE_UPDATE, operationGetType(op2));
            junit.framework.TestCase.assertEquals("content://goo/bar", operationGetUri(op2).toString());
            junit.framework.TestCase.assertNull(operationGetExpectedCount(op2));
            junit.framework.TestCase.assertNull(operationGetSelection(op2));
            junit.framework.TestCase.assertNull(operationGetSelectionArgs(op2));
            junit.framework.TestCase.assertNull(operationGetValues(op2));
            junit.framework.TestCase.assertNull(operationGetValuesBackReferences(op2));
            junit.framework.TestCase.assertEquals(2, operationGetSelectionArgsBackReferences(op2).size());
            junit.framework.TestCase.assertEquals(java.lang.Integer.valueOf(2), operationGetSelectionArgsBackReferences(op2).get(1));
            junit.framework.TestCase.assertEquals(java.lang.Integer.valueOf(4), operationGetSelectionArgsBackReferences(op2).get(3));
        } finally {
            parcel.recycle();
        }
        try {
            android.content.ContentProviderOperation.Builder builder = android.content.ContentProviderOperation.newDelete(android.net.Uri.parse("content://goo/bar"));
            op1 = android.content.ContentProviderOperationTest.newOperationFromBuilder(builder);
            op1.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            op2 = android.content.ContentProviderOperation.CREATOR.createFromParcel(parcel);
            junit.framework.TestCase.assertEquals(android.content.ContentProviderOperation.TYPE_DELETE, operationGetType(op2));
            junit.framework.TestCase.assertEquals("content://goo/bar", operationGetUri(op2).toString());
            junit.framework.TestCase.assertNull(operationGetExpectedCount(op2));
            junit.framework.TestCase.assertNull(operationGetSelection(op2));
            junit.framework.TestCase.assertNull(operationGetSelectionArgs(op2));
            junit.framework.TestCase.assertNull(operationGetValues(op2));
            junit.framework.TestCase.assertNull(operationGetValuesBackReferences(op2));
            junit.framework.TestCase.assertNull(operationGetSelectionArgsBackReferences(op2));
        } finally {
            parcel.recycle();
        }
    }

    private static android.content.ContentProviderOperation newOperationFromBuilder(android.content.ContentProviderOperation.Builder builder) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        final java.lang.reflect.Constructor constructor = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredConstructor(android.content.ContentProviderOperationTest.CLASS_BUILDER);
        constructor.setAccessible(true);
        return ((android.content.ContentProviderOperation) (constructor.newInstance(builder)));
    }

    private void builderSetSelectionArgsBackReferences(android.content.ContentProviderOperation.Builder builder, java.util.HashMap<java.lang.Integer, java.lang.Integer> selArgsBackRef) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field field;
        field = android.content.ContentProviderOperationTest.CLASS_BUILDER.getDeclaredField("mSelectionArgsBackReferences");
        field.setAccessible(true);
        field.set(builder, selArgsBackRef);
    }

    private void builderSetValuesBackReferences(android.content.ContentProviderOperation.Builder builder, android.content.ContentValues valuesBackReferences) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field field;
        field = android.content.ContentProviderOperationTest.CLASS_BUILDER.getDeclaredField("mValuesBackReferences");
        field.setAccessible(true);
        field.set(builder, valuesBackReferences);
    }

    private void builderSetSelection(android.content.ContentProviderOperation.Builder builder, java.lang.String selection) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field field;
        field = android.content.ContentProviderOperationTest.CLASS_BUILDER.getDeclaredField("mSelection");
        field.setAccessible(true);
        field.set(builder, selection);
    }

    private void builderSetSelectionArgs(android.content.ContentProviderOperation.Builder builder, java.lang.String[] selArgs) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field field;
        field = android.content.ContentProviderOperationTest.CLASS_BUILDER.getDeclaredField("mSelectionArgs");
        field.setAccessible(true);
        field.set(builder, selArgs);
    }

    private void builderSetValues(android.content.ContentProviderOperation.Builder builder, android.content.ContentValues values) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field field;
        field = android.content.ContentProviderOperationTest.CLASS_BUILDER.getDeclaredField("mValues");
        field.setAccessible(true);
        field.set(builder, values);
    }

    private void builderSetExpectedCount(android.content.ContentProviderOperation.Builder builder, java.lang.Integer expectedCount) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        java.lang.reflect.Field field;
        field = android.content.ContentProviderOperationTest.CLASS_BUILDER.getDeclaredField("mExpectedCount");
        field.setAccessible(true);
        field.set(builder, expectedCount);
    }

    private int operationGetType(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mType");
        field.setAccessible(true);
        return field.getInt(operation);
    }

    private android.net.Uri operationGetUri(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mUri");
        field.setAccessible(true);
        return ((android.net.Uri) (field.get(operation)));
    }

    private java.lang.String operationGetSelection(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mSelection");
        field.setAccessible(true);
        return ((java.lang.String) (field.get(operation)));
    }

    private java.lang.String[] operationGetSelectionArgs(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mSelectionArgs");
        field.setAccessible(true);
        return ((java.lang.String[]) (field.get(operation)));
    }

    private android.content.ContentValues operationGetValues(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mValues");
        field.setAccessible(true);
        return ((android.content.ContentValues) (field.get(operation)));
    }

    private java.lang.Integer operationGetExpectedCount(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mExpectedCount");
        field.setAccessible(true);
        return ((java.lang.Integer) (field.get(operation)));
    }

    private android.content.ContentValues operationGetValuesBackReferences(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mValuesBackReferences");
        field.setAccessible(true);
        return ((android.content.ContentValues) (field.get(operation)));
    }

    private java.util.Map<java.lang.Integer, java.lang.Integer> operationGetSelectionArgsBackReferences(android.content.ContentProviderOperation operation) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final java.lang.reflect.Field field = android.content.ContentProviderOperationTest.CLASS_OPERATION.getDeclaredField("mSelectionArgsBackReferences");
        field.setAccessible(true);
        return ((java.util.Map<java.lang.Integer, java.lang.Integer>) (field.get(operation)));
    }

    public void testParcelingResult() {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        android.content.ContentProviderResult result1;
        android.content.ContentProviderResult result2;
        try {
            result1 = new android.content.ContentProviderResult(android.net.Uri.parse("content://goo/bar"));
            result1.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            result2 = android.content.ContentProviderResult.CREATOR.createFromParcel(parcel);
            junit.framework.TestCase.assertEquals("content://goo/bar", result2.uri.toString());
            junit.framework.TestCase.assertNull(result2.count);
        } finally {
            parcel.recycle();
        }
        parcel = android.os.Parcel.obtain();
        try {
            result1 = new android.content.ContentProviderResult(42);
            result1.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            result2 = android.content.ContentProviderResult.CREATOR.createFromParcel(parcel);
            junit.framework.TestCase.assertEquals(java.lang.Integer.valueOf(42), result2.count);
            junit.framework.TestCase.assertNull(result2.uri);
        } finally {
            parcel.recycle();
        }
    }

    static class TestContentProvider extends android.content.ContentProvider {
        public boolean onCreate() {
            throw new java.lang.UnsupportedOperationException();
        }

        public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
            throw new java.lang.UnsupportedOperationException();
        }

        public java.lang.String getType(android.net.Uri uri) {
            throw new java.lang.UnsupportedOperationException();
        }

        public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
            throw new java.lang.UnsupportedOperationException();
        }

        public int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
            throw new java.lang.UnsupportedOperationException();
        }

        public int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

