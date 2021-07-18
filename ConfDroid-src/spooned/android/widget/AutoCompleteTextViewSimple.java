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
package android.widget;


public class AutoCompleteTextViewSimple extends android.app.Activity implements android.widget.AdapterView.OnItemClickListener , android.widget.AdapterView.OnItemSelectedListener {
    private final java.lang.String LOG_TAG = "AutoCompleteTextViewSimple";

    private android.widget.AutoCompleteTextView mTextView;

    /**
     * These are cleared by resetItemListeners(), and set by the callback listeners
     */
    public boolean mItemClickCalled;

    public int mItemClickPosition;

    public boolean mItemSelectedCalled;

    public int mItemSelectedPosition;

    public boolean mNothingSelectedCalled;

    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        // Be sure to call the super class.
        super.onCreate(icicle);
        // setup layout & views
        setContentView(R.layout.autocompletetextview_simple);
        mTextView = ((android.widget.AutoCompleteTextView) (findViewById(R.id.autocompletetextview1)));
        // configure callbacks used for monitoring
        mTextView.setOnItemClickListener(this);
        mTextView.setOnItemSelectedListener(this);
        resetItemListeners();
        setStringAdapter(5, "a");
    }

    /**
     *
     *
     * @return The AutoCompleteTextView used in this test activity.
     */
    public android.widget.AutoCompleteTextView getTextView() {
        return mTextView;
    }

    /**
     * Set the autocomplete data to an adapter containing 0..n strings with a consistent prefix.
     */
    public void setStringAdapter(int numSuggestions, java.lang.String prefix) {
        // generate the string array
        java.lang.String[] strings = new java.lang.String[numSuggestions];
        for (int i = 0; i < numSuggestions; ++i) {
            strings[i] = prefix + java.lang.String.valueOf(i);
        }
        // install it with an adapter
        android.widget.ArrayAdapter<java.lang.String> adapter = new android.widget.ArrayAdapter<java.lang.String>(this, android.R.layout.simple_dropdown_item_1line, strings);
        mTextView.setAdapter(adapter);
    }

    /**
     * For monitoring OnItemClickListener & OnItemSelectedListener
     *
     * An alternative here would be to provide a set of pass-through callbacks
     */
    public void resetItemListeners() {
        mItemClickCalled = false;
        mItemClickPosition = -1;
        mItemSelectedCalled = false;
        mItemSelectedPosition = -1;
        mNothingSelectedCalled = false;
    }

    /**
     * Implements OnItemClickListener
     */
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        android.util.Log.d(LOG_TAG, "onItemClick() position " + position);
        mItemClickCalled = true;
        mItemClickPosition = position;
    }

    /**
     * Implements OnItemSelectedListener
     */
    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        android.util.Log.d(LOG_TAG, "onItemSelected() position " + position);
        mItemSelectedCalled = true;
        mItemSelectedPosition = position;
    }

    /**
     * Implements OnItemSelectedListener
     */
    public void onNothingSelected(android.widget.AdapterView<?> parent) {
        android.util.Log.d(LOG_TAG, "onNothingSelected()");
        mNothingSelectedCalled = true;
    }
}

