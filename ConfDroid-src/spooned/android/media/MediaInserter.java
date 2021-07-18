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
package android.media;


/**
 * A MediaScanner helper class which enables us to do lazy insertion on the
 * given provider. This class manages buffers internally and flushes when they
 * are full. Note that you should call flushAll() after using this class.
 * {@hide }
 */
public class MediaInserter {
    private final java.util.HashMap<android.net.Uri, java.util.List<android.content.ContentValues>> mRowMap = new java.util.HashMap<android.net.Uri, java.util.List<android.content.ContentValues>>();

    private final java.util.HashMap<android.net.Uri, java.util.List<android.content.ContentValues>> mPriorityRowMap = new java.util.HashMap<android.net.Uri, java.util.List<android.content.ContentValues>>();

    private final android.content.ContentProviderClient mProvider;

    private final int mBufferSizePerUri;

    public MediaInserter(android.content.ContentProviderClient provider, int bufferSizePerUri) {
        mProvider = provider;
        mBufferSizePerUri = bufferSizePerUri;
    }

    public void insert(android.net.Uri tableUri, android.content.ContentValues values) throws android.os.RemoteException {
        insert(tableUri, values, false);
    }

    public void insertwithPriority(android.net.Uri tableUri, android.content.ContentValues values) throws android.os.RemoteException {
        insert(tableUri, values, true);
    }

    private void insert(android.net.Uri tableUri, android.content.ContentValues values, boolean priority) throws android.os.RemoteException {
        java.util.HashMap<android.net.Uri, java.util.List<android.content.ContentValues>> rowmap = (priority) ? mPriorityRowMap : mRowMap;
        java.util.List<android.content.ContentValues> list = rowmap.get(tableUri);
        if (list == null) {
            list = new java.util.ArrayList<android.content.ContentValues>();
            rowmap.put(tableUri, list);
        }
        list.add(new android.content.ContentValues(values));
        if (list.size() >= mBufferSizePerUri) {
            flushAllPriority();
            flush(tableUri, list);
        }
    }

    public void flushAll() throws android.os.RemoteException {
        flushAllPriority();
        for (android.net.Uri tableUri : mRowMap.keySet()) {
            java.util.List<android.content.ContentValues> list = mRowMap.get(tableUri);
            flush(tableUri, list);
        }
        mRowMap.clear();
    }

    private void flushAllPriority() throws android.os.RemoteException {
        for (android.net.Uri tableUri : mPriorityRowMap.keySet()) {
            java.util.List<android.content.ContentValues> list = mPriorityRowMap.get(tableUri);
            flush(tableUri, list);
        }
        mPriorityRowMap.clear();
    }

    private void flush(android.net.Uri tableUri, java.util.List<android.content.ContentValues> list) throws android.os.RemoteException {
        if (!list.isEmpty()) {
            android.content.ContentValues[] valuesArray = new android.content.ContentValues[list.size()];
            valuesArray = list.toArray(valuesArray);
            mProvider.bulkInsert(tableUri, valuesArray);
            list.clear();
        }
    }
}

