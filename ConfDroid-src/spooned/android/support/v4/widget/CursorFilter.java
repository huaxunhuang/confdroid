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
package android.support.v4.widget;


/**
 * The CursorFilter delegates most of the work to the
 * {@link android.widget.CursorAdapter}. Subclasses should override these
 * delegate methods to run the queries and convert the results into String
 * that can be used by auto-completion widgets.
 */
class CursorFilter extends android.widget.Filter {
    android.support.v4.widget.CursorFilter.CursorFilterClient mClient;

    interface CursorFilterClient {
        java.lang.CharSequence convertToString(android.database.Cursor cursor);

        android.database.Cursor runQueryOnBackgroundThread(java.lang.CharSequence constraint);

        android.database.Cursor getCursor();

        void changeCursor(android.database.Cursor cursor);
    }

    CursorFilter(android.support.v4.widget.CursorFilter.CursorFilterClient client) {
        mClient = client;
    }

    @java.lang.Override
    public java.lang.CharSequence convertResultToString(java.lang.Object resultValue) {
        return mClient.convertToString(((android.database.Cursor) (resultValue)));
    }

    @java.lang.Override
    protected android.widget.Filter.FilterResults performFiltering(java.lang.CharSequence constraint) {
        android.database.Cursor cursor = mClient.runQueryOnBackgroundThread(constraint);
        android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();
        if (cursor != null) {
            results.count = cursor.getCount();
            results.values = cursor;
        } else {
            results.count = 0;
            results.values = null;
        }
        return results;
    }

    @java.lang.Override
    protected void publishResults(java.lang.CharSequence constraint, android.widget.Filter.FilterResults results) {
        android.database.Cursor oldCursor = mClient.getCursor();
        if ((results.values != null) && (results.values != oldCursor)) {
            mClient.changeCursor(((android.database.Cursor) (results.values)));
        }
    }
}

