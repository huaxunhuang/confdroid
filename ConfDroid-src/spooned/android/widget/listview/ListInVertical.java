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
package android.widget.listview;


/**
 * Exercises a list in a vertical linear layout
 */
public class ListInVertical extends android.app.Activity {
    private android.widget.ListView mListView;

    @java.lang.Override
    public void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_in_vertical);
        java.lang.String[] values = new java.lang.String[1000];
        for (int i = 0; i < 1000; i++) {
            values[i] = ((java.lang.Integer) (i)).toString();
        }
        mListView = ((android.widget.ListView) (findViewById(R.id.list)));
        mListView.setAdapter(new android.widget.ArrayAdapter<java.lang.String>(this, android.R.layout.simple_list_item_1, values));
    }

    public android.widget.ListView getListView() {
        return mListView;
    }
}

