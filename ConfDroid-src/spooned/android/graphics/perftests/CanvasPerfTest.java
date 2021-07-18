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
public class CanvasPerfTest {
    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testBasicViewGroupDraw() {
        // This test is a clone of BM_DisplayListCanvas_basicViewGroupDraw
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        android.graphics.RenderNode child = android.graphics.RenderNode.create("child", null);
        child.setLeftTopRightBottom(50, 50, 100, 100);
        android.graphics.RecordingCanvas canvas = node.start(100, 100);
        node.end(canvas);
        canvas = child.start(50, 50);
        canvas.drawColor(android.graphics.Color.WHITE);
        child.end(canvas);
        while (state.keepRunning()) {
            canvas = node.start(200, 200);
            int save = canvas.save();
            canvas.clipRect(1, 1, 199, 199);
            canvas.insertReorderBarrier();
            for (int i = 0; i < 5; i++) {
                canvas.drawRenderNode(child);
            }
            canvas.insertInorderBarrier();
            canvas.restoreToCount(save);
            node.end(canvas);
        } 
    }

    @org.junit.Test
    public void testRecordSimpleBitmapView() {
        // This test is a clone of BM_DisplayListCanvas_record_simpleBitmapView
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        android.graphics.RecordingCanvas canvas = node.start(100, 100);
        node.end(canvas);
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(80, 80, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColor(android.graphics.Color.BLACK);
        while (state.keepRunning()) {
            canvas = node.start(100, 100);
            {
                canvas.save();
                canvas.drawRect(0, 0, 100, 100, paint);
                canvas.restore();
            }
            {
                canvas.save();
                canvas.translate(10, 10);
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.restore();
            }
            node.end(canvas);
        } 
    }
}

