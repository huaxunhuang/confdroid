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
 * See 1080989. You need some contacts for this adapter.
 */
public class ListWithDisappearingItemBug extends android.app.ListActivity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.widget.Toast.makeText(this, "Make sure you rotate screen to see bug", android.widget.Toast.LENGTH_LONG).show();
        // Get a cursor with all people
        android.database.Cursor c = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        android.widget.ListAdapter adapter = // Use a template that displays a text view
        // Give the cursor to the list adatper
        // Map the NAME column in the people database to...
        // The "text1" view defined in the XML template
        new android.widget.SimpleCursorAdapter(this, R.layout.list_with_disappearing_item_bug_item, c, new java.lang.String[]{ android.provider.Contacts.People.NAME }, new int[]{ R.id.text1 });
        setListAdapter(adapter);
        android.view.animation.AnimationSet set = new android.view.animation.AnimationSet(true);
        android.view.animation.Animation animation = new android.view.animation.AlphaAnimation(0.0F, 1.0F);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new android.view.animation.TranslateAnimation(android.view.animation.Animation.RELATIVE_TO_SELF, 0.0F, android.view.animation.Animation.RELATIVE_TO_SELF, 0.0F, android.view.animation.Animation.RELATIVE_TO_SELF, -1.0F, android.view.animation.Animation.RELATIVE_TO_SELF, 0.0F);
        animation.setDuration(100);
        set.addAnimation(animation);
        android.view.animation.LayoutAnimationController controller = new android.view.animation.LayoutAnimationController(set, 0.5F);
        android.widget.ListView listView = getListView();
        listView.setLayoutAnimation(controller);
    }
}

