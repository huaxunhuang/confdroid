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
package android.support.v7.graphics;


@org.junit.runner.RunWith(android.support.test.runner.AndroidJUnit4.class)
public class BucketTests {
    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testSourceBitmapNotRecycled() {
        final android.graphics.Bitmap sample = android.support.v7.graphics.TestUtils.loadSampleBitmap();
        android.support.v7.graphics.Palette.from(sample).generate();
        org.junit.Assert.assertFalse(sample.isRecycled());
    }

    @org.junit.Test(expected = java.lang.UnsupportedOperationException.class)
    @android.test.suitebuilder.annotation.SmallTest
    public void testSwatchesUnmodifiable() {
        android.support.v7.graphics.Palette p = android.support.v7.graphics.Palette.from(android.support.v7.graphics.TestUtils.loadSampleBitmap()).generate();
        p.getSwatches().remove(0);
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testSwatchesBuilder() {
        java.util.ArrayList<android.support.v7.graphics.Palette.Swatch> swatches = new java.util.ArrayList<>();
        swatches.add(new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLACK, 40));
        swatches.add(new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.GREEN, 60));
        swatches.add(new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLUE, 10));
        android.support.v7.graphics.Palette p = android.support.v7.graphics.Palette.from(swatches);
        org.junit.Assert.assertEquals(swatches, p.getSwatches());
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testRegionWhole() {
        final android.graphics.Bitmap sample = android.support.v7.graphics.TestUtils.loadSampleBitmap();
        android.support.v7.graphics.Palette.Builder b = new android.support.v7.graphics.Palette.Builder(sample);
        b.setRegion(0, 0, sample.getWidth(), sample.getHeight());
        b.generate();
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testRegionUpperLeft() {
        final android.graphics.Bitmap sample = android.support.v7.graphics.TestUtils.loadSampleBitmap();
        android.support.v7.graphics.Palette.Builder b = new android.support.v7.graphics.Palette.Builder(sample);
        b.setRegion(0, 0, sample.getWidth() / 2, sample.getHeight() / 2);
        b.generate();
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testRegionBottomRight() {
        final android.graphics.Bitmap sample = android.support.v7.graphics.TestUtils.loadSampleBitmap();
        android.support.v7.graphics.Palette.Builder b = new android.support.v7.graphics.Palette.Builder(sample);
        b.setRegion(sample.getWidth() / 2, sample.getHeight() / 2, sample.getWidth(), sample.getHeight());
        b.generate();
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testOnePixelTallBitmap() {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(1000, 1, android.graphics.Bitmap.Config.ARGB_8888);
        android.support.v7.graphics.Palette.Builder b = new android.support.v7.graphics.Palette.Builder(bitmap);
        b.generate();
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testOnePixelWideBitmap() {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(1, 1000, android.graphics.Bitmap.Config.ARGB_8888);
        android.support.v7.graphics.Palette.Builder b = new android.support.v7.graphics.Palette.Builder(bitmap);
        b.generate();
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testBlueBitmapReturnsBlueSwatch() {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(300, 300, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        canvas.drawColor(android.graphics.Color.BLUE);
        final android.support.v7.graphics.Palette palette = android.support.v7.graphics.Palette.from(bitmap).generate();
        org.junit.Assert.assertEquals(1, palette.getSwatches().size());
        final android.support.v7.graphics.Palette.Swatch swatch = palette.getSwatches().get(0);
        android.support.v7.graphics.TestUtils.assertCloseColors(android.graphics.Color.BLUE, swatch.getRgb());
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testBlueBitmapWithRegionReturnsBlueSwatch() {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(300, 300, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        canvas.drawColor(android.graphics.Color.BLUE);
        final android.support.v7.graphics.Palette palette = android.support.v7.graphics.Palette.from(bitmap).setRegion(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight()).generate();
        org.junit.Assert.assertEquals(1, palette.getSwatches().size());
        final android.support.v7.graphics.Palette.Swatch swatch = palette.getSwatches().get(0);
        android.support.v7.graphics.TestUtils.assertCloseColors(android.graphics.Color.BLUE, swatch.getRgb());
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testDominantSwatch() {
        final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888);
        // First fill the canvas with blue
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        canvas.drawColor(android.graphics.Color.BLUE);
        final android.graphics.Paint paint = new android.graphics.Paint();
        // Now we'll draw the top 10px tall rect with green
        paint.setColor(android.graphics.Color.GREEN);
        canvas.drawRect(0, 0, 100, 10, paint);
        // Now we'll draw the next 20px tall rect with red
        paint.setColor(android.graphics.Color.RED);
        canvas.drawRect(0, 11, 100, 30, paint);
        // Now generate a palette from the bitmap
        final android.support.v7.graphics.Palette palette = android.support.v7.graphics.Palette.from(bitmap).generate();
        // First assert that there are 3 swatches
        org.junit.Assert.assertEquals(3, palette.getSwatches().size());
        // Now assert that the dominant swatch is blue
        final android.support.v7.graphics.Palette.Swatch swatch = palette.getDominantSwatch();
        org.junit.Assert.assertNotNull(swatch);
        android.support.v7.graphics.TestUtils.assertCloseColors(android.graphics.Color.BLUE, swatch.getRgb());
    }
}

