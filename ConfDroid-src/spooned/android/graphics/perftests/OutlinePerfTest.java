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
package android.graphics.perftests;


@androidx.test.filters.LargeTest
public class OutlinePerfTest {
    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testSetEmpty() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Outline outline = new android.graphics.Outline();
        while (state.keepRunning()) {
            outline.setEmpty();
        } 
    }

    @org.junit.Test
    public void testSetRoundRect() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Outline outline = new android.graphics.Outline();
        while (state.keepRunning()) {
            outline.setRoundRect(50, 50, 150, 150, 5);
        } 
    }
}

