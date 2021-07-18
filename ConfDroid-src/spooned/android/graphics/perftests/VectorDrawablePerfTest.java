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


@org.junit.runner.RunWith(androidx.test.runner.AndroidJUnit4.class)
@android.test.suitebuilder.annotation.LargeTest
public class VectorDrawablePerfTest {
    private static final boolean DUMP_BITMAP = false;

    private int[] mTestWidths = new int[]{ 1024, 512 };

    private int[] mTestHeights = new int[]{ 512, 1024 };

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testBitmapDrawPerf() {
        int resId = R.drawable.vector_drawable01;
        android.app.Activity activity = mActivityRule.getActivity();
        android.graphics.drawable.VectorDrawable vd = ((android.graphics.drawable.VectorDrawable) (activity.getDrawable(resId)));
        int w = 1024;
        int h = 1024;
        android.graphics.Bitmap.Config conf = android.graphics.Bitmap.Config.ARGB_8888;
        android.graphics.Bitmap bmp = android.graphics.Bitmap.createBitmap(w, h, conf);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bmp);
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int i = 0;
        while (state.keepRunning()) {
            // Use different width / height each to force the vectorDrawable abandon the cache.
            vd.setBounds(0, 0, mTestWidths[i % 2], mTestHeights[i % 2]);
            i++;
            vd.draw(canvas);
        } 
        // Double check the bitmap pixels to make sure we draw correct content.
        int backgroundColor = bmp.getPixel(w / 4, h / 2);
        int objColor = bmp.getPixel(w / 8, (h / 2) + 1);
        int emptyColor = bmp.getPixel((w * 3) / 4, (h * 3) / 4);
        junit.framework.Assert.assertTrue("The background should be white", backgroundColor == android.graphics.Color.WHITE);
        junit.framework.Assert.assertTrue("The object should be black", objColor == android.graphics.Color.BLACK);
        junit.framework.Assert.assertTrue("The right bottom part should be empty", emptyColor == android.graphics.Color.TRANSPARENT);
        if (android.graphics.perftests.VectorDrawablePerfTest.DUMP_BITMAP) {
            android.perftests.utils.BitmapUtils.saveBitmapIntoPNG(activity, bmp, resId);
        }
    }
}

