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
public class RenderNodePerfTest {
    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testMeasureRenderNodeJniOverhead() {
        final android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            node.setTranslationX(1.0F);
        } 
    }

    @org.junit.Test
    public void testCreateRenderNodeNoName() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            android.graphics.RenderNode.create(null, null);
        } 
    }

    @org.junit.Test
    public void testCreateRenderNode() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            android.graphics.RenderNode.create("LinearLayout", null);
        } 
    }

    @org.junit.Test
    public void testIsValid() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode node = android.graphics.RenderNode.create("LinearLayout", null);
        while (state.keepRunning()) {
            node.hasDisplayList();
        } 
    }

    @org.junit.Test
    public void testStartEnd() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode node = android.graphics.RenderNode.create("LinearLayout", null);
        while (state.keepRunning()) {
            node.beginRecording(100, 100);
            node.endRecording();
        } 
    }

    @org.junit.Test
    public void testStartEndDeepHierarchy() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode[] nodes = new android.graphics.RenderNode[30];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = android.graphics.RenderNode.create("LinearLayout", null);
        }
        while (state.keepRunning()) {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].beginRecording(100, 100);
            }
            for (int i = nodes.length - 1; i >= 0; i--) {
                nodes[i].endRecording();
            }
        } 
    }

    @org.junit.Test
    public void testHasIdentityMatrix() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode node = android.graphics.RenderNode.create("LinearLayout", null);
        while (state.keepRunning()) {
            node.hasIdentityMatrix();
        } 
    }

    @org.junit.Test
    public void testSetOutline() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode node = android.graphics.RenderNode.create("LinearLayout", null);
        android.graphics.Outline a = new android.graphics.Outline();
        a.setRoundRect(0, 0, 100, 100, 10);
        android.graphics.Outline b = new android.graphics.Outline();
        b.setRect(50, 50, 150, 150);
        b.setAlpha(0.5F);
        while (state.keepRunning()) {
            node.setOutline(a);
            node.setOutline(b);
        } 
    }
}

