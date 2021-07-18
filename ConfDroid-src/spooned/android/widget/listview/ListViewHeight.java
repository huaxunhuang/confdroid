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


public class ListViewHeight extends android.app.Activity {
    private android.view.View mButton1;

    private android.view.View mButton2;

    private android.view.View mButton3;

    private android.view.View mOuterLayout;

    private android.widget.ListView mInnerList;

    android.widget.ArrayAdapter<java.lang.String> mAdapter;

    private java.lang.String[] mStrings = new java.lang.String[]{ "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi" };

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout_listview_height);
        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mOuterLayout = findViewById(R.id.layout);
        mInnerList = ((android.widget.ListView) (findViewById(R.id.inner_list)));
        mAdapter = new android.widget.ArrayAdapter<java.lang.String>(this, android.R.layout.simple_dropdown_item_1line, mStrings);
        // Clicking this button will show the list view and set it to a fixed height
        // If you then hide the views, there is no problem.
        mButton1.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                // set listview to fixed height
                android.view.ViewGroup.MarginLayoutParams lp;
                lp = ((android.view.ViewGroup.MarginLayoutParams) (mInnerList.getLayoutParams()));
                lp.height = 200;
                mInnerList.setLayoutParams(lp);
                // enable list adapter
                mInnerList.setAdapter(mAdapter);
                // and show it
                mOuterLayout.setVisibility(android.view.View.VISIBLE);
            }
        });
        // Clicking this button will show the list view and set it match_parent height
        // If you then hide the views, there is an NPE when calculating the ListView height.
        mButton2.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                // set listview to fill screen
                android.view.ViewGroup.MarginLayoutParams lp;
                lp = ((android.view.ViewGroup.MarginLayoutParams) (mInnerList.getLayoutParams()));
                lp.height = lp.MATCH_PARENT;
                mInnerList.setLayoutParams(lp);
                // enable list adapter
                mInnerList.setAdapter(mAdapter);
                // and show it
                mOuterLayout.setVisibility(android.view.View.VISIBLE);
            }
        });
        // Clicking this button will remove the list adapter and hide the outer enclosing view.
        // We have to climb all the way to the top because the bug (not checking visibility)
        // only occurs at the very outer loop of ViewAncestor.performTraversals and in the case of
        // an Activity, this means you have to crawl all the way out to the root view.
        // In the search manager, it's sufficient to simply show/hide the outer search manager
        // view to trigger the same bug.
        mButton3.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mInnerList.setAdapter(null);
                // hide listview's owner
                // as it turns out, the owner doesn't take us high enough
                // because our activity includes a title bar, thus another layer
                android.view.View parent = ((android.view.View) (mOuterLayout.getParent()));// FrameLayout (app container)

                android.view.View grandpa = ((android.view.View) (parent.getParent()));// LinearLayout (title+app)

                android.view.View great = ((android.view.View) (grandpa.getParent()));// PhoneWindow.DecorView

                great.setVisibility(android.view.View.GONE);
            }
        });
    }
}

