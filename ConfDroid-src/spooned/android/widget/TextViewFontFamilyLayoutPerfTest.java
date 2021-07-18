/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * limitations under the License
 */
package android.widget;


@androidx.test.filters.LargeTest
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class TextViewFontFamilyLayoutPerfTest {
    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection layouts() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "String fontFamily attribute", R.layout.test_textview_font_family_string }, new java.lang.Object[]{ "File fontFamily attribute", R.layout.test_textview_font_family_file }, new java.lang.Object[]{ "XML fontFamily attribute", R.layout.test_textview_font_family_xml } });
    }

    private int mLayoutId;

    public TextViewFontFamilyLayoutPerfTest(java.lang.String key, int layoutId) {
        mLayoutId = layoutId;
    }

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testConstruction() throws java.lang.Throwable {
        final android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.view.LayoutInflater inflator = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        while (state.keepRunning()) {
            inflator.inflate(mLayoutId, null, false);
        } 
    }
}

