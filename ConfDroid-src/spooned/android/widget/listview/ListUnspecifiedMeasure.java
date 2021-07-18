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


public class ListUnspecifiedMeasure<T extends android.app.Activity> extends android.test.ActivityInstrumentationTestCase<T> {
    private T mActivity;

    private android.widget.ListView mListView;

    protected ListUnspecifiedMeasure(java.lang.Class<T> klass) {
        super("com.android.frameworks.coretests", klass);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = android.widget.listview.ListUnspecifiedMeasure.getActivity();
        mListView = ((android.widget.ListView) (mActivity.findViewById(R.id.list)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        android.widget.listview.ListUnspecifiedMeasure.assertNotNull(mActivity);
        android.widget.listview.ListUnspecifiedMeasure.assertNotNull(mListView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testWasMeasured() {
        android.widget.listview.ListUnspecifiedMeasure.assertTrue(mListView.getMeasuredWidth() > 0);
        android.widget.listview.ListUnspecifiedMeasure.assertTrue(mListView.getWidth() > 0);
        android.widget.listview.ListUnspecifiedMeasure.assertTrue(mListView.getMeasuredHeight() > 0);
        android.widget.listview.ListUnspecifiedMeasure.assertTrue(mListView.getHeight() > 0);
    }
}

