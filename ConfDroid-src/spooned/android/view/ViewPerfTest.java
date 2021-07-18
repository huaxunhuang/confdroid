/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.view;


@androidx.test.filters.LargeTest
public class ViewPerfTest {
    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testSimpleViewInflate() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.content.Context context = androidx.test.InstrumentationRegistry.getInstrumentation().getTargetContext();
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        android.widget.FrameLayout root = new android.widget.FrameLayout(context);
        while (state.keepRunning()) {
            inflater.inflate(R.layout.test_simple_view, root, false);
        } 
    }

    @org.junit.Test
    public void testTwelveKeyInflate() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.content.Context context = androidx.test.InstrumentationRegistry.getInstrumentation().getTargetContext();
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        android.widget.FrameLayout root = new android.widget.FrameLayout(context);
        while (state.keepRunning()) {
            inflater.inflate(R.layout.twelve_key_entry, root, false);
        } 
    }
}

