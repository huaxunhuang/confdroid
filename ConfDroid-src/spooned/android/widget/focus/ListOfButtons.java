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
package android.widget.focus;


/**
 * A layout with a ListView containing buttons.
 */
public class ListOfButtons extends android.app.ListActivity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_with_button_above);
        getListView().setItemsCanFocus(true);
        setListAdapter(new android.widget.focus.ListOfButtons.MyAdapter(this, mLabels));
    }

    java.lang.String[] mLabels = new java.lang.String[]{ "Alabama", "Alaska", "Arizona", "apple sauce!", "California", "Colorado", "Connecticut", "Delaware" };

    public java.lang.String[] getLabels() {
        return mLabels;
    }

    public static class MyAdapter extends android.widget.ArrayAdapter<java.lang.String> {
        public MyAdapter(android.content.Context context, java.lang.String[] labels) {
            super(context, 0, labels);
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            java.lang.String label = getItem(position);
            android.widget.Button button = new android.widget.Button(parent.getContext());
            button.setText(label);
            return button;
        }

        @java.lang.Override
        public boolean areAllItemsEnabled() {
            return false;
        }
    }
}

