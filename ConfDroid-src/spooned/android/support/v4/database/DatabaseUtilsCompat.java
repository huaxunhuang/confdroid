/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.database;


/**
 * Helper for accessing features in {@link android.database.DatabaseUtils}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class DatabaseUtilsCompat {
    private DatabaseUtilsCompat() {
        /* Hide constructor */
    }

    /**
     * Concatenates two SQL WHERE clauses, handling empty or null values.
     */
    public static java.lang.String concatenateWhere(java.lang.String a, java.lang.String b) {
        if (android.text.TextUtils.isEmpty(a)) {
            return b;
        }
        if (android.text.TextUtils.isEmpty(b)) {
            return a;
        }
        return ((("(" + a) + ") AND (") + b) + ")";
    }

    /**
     * Appends one set of selection args to another. This is useful when adding a selection
     * argument to a user provided set.
     */
    public static java.lang.String[] appendSelectionArgs(java.lang.String[] originalValues, java.lang.String[] newValues) {
        if ((originalValues == null) || (originalValues.length == 0)) {
            return newValues;
        }
        java.lang.String[] result = new java.lang.String[originalValues.length + newValues.length];
        java.lang.System.arraycopy(originalValues, 0, result, 0, originalValues.length);
        java.lang.System.arraycopy(newValues, 0, result, originalValues.length, newValues.length);
        return result;
    }
}

