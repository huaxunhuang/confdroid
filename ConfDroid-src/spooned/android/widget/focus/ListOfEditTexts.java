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


public class ListOfEditTexts extends android.app.Activity {
    private int mLinesPerEditText = 12;

    private android.widget.ListView mListView;

    private android.widget.LinearLayout mLinearLayout;

    public android.widget.ListView getListView() {
        return mListView;
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        // create linear layout
        mLinearLayout = new android.widget.LinearLayout(this);
        mLinearLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
        mLinearLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        // add a button above
        android.widget.Button buttonAbove = new android.widget.Button(this);
        buttonAbove.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonAbove.setText("button above list");
        mLinearLayout.addView(buttonAbove);
        // add a list view to it
        mListView = new android.widget.ListView(this);
        mListView.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mListView.setDrawSelectorOnTop(false);
        mListView.setItemsCanFocus(true);
        mListView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0F));
        java.util.List<java.lang.String> bodies = com.google.android.collect.Lists.newArrayList(getBody("zero hello, my name is android"), getBody("one i'm a paranoid android"), getBody("two i robot.  huh huh."), getBody("three not the g-phone!"));
        mListView.setAdapter(new android.widget.focus.ListOfEditTexts.MyAdapter(this, bodies));
        mLinearLayout.addView(mListView);
        // add button below
        android.widget.Button buttonBelow = new android.widget.Button(this);
        buttonBelow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonBelow.setText("button below list");
        mLinearLayout.addView(buttonBelow);
        setContentView(mLinearLayout);
    }

    java.lang.String getBody(java.lang.String line) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder((line.length() + 5) * mLinesPerEditText);
        for (int i = 0; i < mLinesPerEditText; i++) {
            sb.append(i + 1).append(' ').append(line);
            if (i < (mLinesPerEditText - 1)) {
                sb.append('\n');// all but last line

            }
        }
        return sb.toString();
    }

    private static class MyAdapter extends android.widget.ArrayAdapter<java.lang.String> {
        public MyAdapter(android.content.Context context, java.util.List<java.lang.String> bodies) {
            super(context, 0, bodies);
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            java.lang.String body = getItem(position);
            if (convertView != null) {
                ((android.widget.EditText) (convertView)).setText(body);
                return convertView;
            }
            android.widget.EditText editText = new android.widget.EditText(getContext());
            editText.setText(body);
            return editText;
        }
    }
}

