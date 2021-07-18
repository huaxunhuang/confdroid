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
package android.widget.listview;


public class ListManagedCursor extends android.app.ListActivity implements android.widget.AdapterView.OnItemClickListener {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get a cursor with all people
        android.database.Cursor c = getContentResolver().query(Settings.System.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        android.widget.ListAdapter adapter = // Use a template that displays a text view
        // Give the cursor to the list adatper
        // Map the NAME column in the people database to...
        // The "text1" view defined in the XML template
        new android.widget.SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new java.lang.String[]{ android.provider.Contacts.People.NAME }, new int[]{ android.R.id.text1 });
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    public void onItemClick(android.widget.AdapterView parent, android.view.View view, int position, long id) {
        android.content.Intent dummyIntent = new android.content.Intent(this, android.widget.listview.ListSimple.class);
        startActivity(dummyIntent);
    }
}

