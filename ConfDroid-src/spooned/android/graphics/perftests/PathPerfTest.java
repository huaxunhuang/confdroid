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
public class PathPerfTest {
    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testReset() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        while (state.keepRunning()) {
            path.reset();
        } 
    }

    @org.junit.Test
    public void testAddReset() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        while (state.keepRunning()) {
            path.addRect(0, 0, 100, 100, android.graphics.Path.Direction.CW);
            path.reset();
        } 
    }

    @org.junit.Test
    public void testRewind() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        while (state.keepRunning()) {
            path.rewind();
        } 
    }

    @org.junit.Test
    public void testAddRewind() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        while (state.keepRunning()) {
            path.addRect(0, 0, 100, 100, android.graphics.Path.Direction.CW);
            path.rewind();
        } 
    }

    @org.junit.Test
    public void testIsEmpty() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        path.addRect(0, 0, 100, 100, android.graphics.Path.Direction.CW);
        while (state.keepRunning()) {
            path.isEmpty();
        } 
    }

    @org.junit.Test
    public void testIsConvex() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        path.addRect(0, 0, 100, 100, android.graphics.Path.Direction.CW);
        while (state.keepRunning()) {
            path.isConvex();
        } 
    }

    @org.junit.Test
    public void testGetSetFillType() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        path.addRect(0, 0, 100, 100, android.graphics.Path.Direction.CW);
        while (state.keepRunning()) {
            path.setFillType(android.graphics.Path.FillType.EVEN_ODD);
            path.getFillType();
        } 
    }

    @org.junit.Test
    public void testIsRect() {
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.Path path = new android.graphics.Path();
        path.addRect(0, 0, 100, 100, android.graphics.Path.Direction.CW);
        final android.graphics.RectF outRect = new android.graphics.RectF();
        while (state.keepRunning()) {
            path.isRect(outRect);
        } 
    }
}

