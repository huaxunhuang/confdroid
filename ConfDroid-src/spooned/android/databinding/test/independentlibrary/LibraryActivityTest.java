/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.test.independentlibrary;


public class LibraryActivityTest extends android.test.ActivityInstrumentationTestCase2<android.databinding.test.independentlibrary.LibraryActivity> {
    public LibraryActivityTest() {
        super(android.databinding.test.independentlibrary.LibraryActivity.class);
    }

    public void testTextViewContents() throws java.lang.Throwable {
        final android.databinding.test.independentlibrary.LibraryActivity activity = getActivity();
        junit.framework.TestCase.assertNotNull("test sanity", activity);
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.widget.TextView textView = ((android.widget.TextView) (activity.findViewById(R.id.fooTextView)));
                final java.lang.String expected = (android.databinding.test.independentlibrary.LibraryActivity.FIELD_VALUE + " ") + android.databinding.test.independentlibrary.LibraryActivity.FIELD_VALUE;
                junit.framework.TestCase.assertEquals(expected, textView.getText().toString());
            }
        });
    }
}

