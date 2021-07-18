/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.graphics;


public class BitmapTest extends junit.framework.TestCase {
    @android.test.suitebuilder.annotation.SmallTest
    public void testBasic() throws java.lang.Exception {
        android.graphics.Bitmap bm1 = android.graphics.Bitmap.createBitmap(100, 200, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Bitmap bm2 = android.graphics.Bitmap.createBitmap(100, 200, android.graphics.Bitmap.Config.RGB_565);
        android.graphics.Bitmap bm3 = android.graphics.Bitmap.createBitmap(100, 200, android.graphics.Bitmap.Config.ARGB_4444);
        junit.framework.TestCase.assertTrue("mutability", bm1.isMutable());
        junit.framework.TestCase.assertTrue("mutability", bm2.isMutable());
        junit.framework.TestCase.assertTrue("mutability", bm3.isMutable());
        junit.framework.TestCase.assertEquals("width", 100, bm1.getWidth());
        junit.framework.TestCase.assertEquals("width", 100, bm2.getWidth());
        junit.framework.TestCase.assertEquals("width", 100, bm3.getWidth());
        junit.framework.TestCase.assertEquals("rowbytes", 400, bm1.getRowBytes());
        junit.framework.TestCase.assertEquals("rowbytes", 200, bm2.getRowBytes());
        junit.framework.TestCase.assertEquals("rowbytes", 200, bm3.getRowBytes());
        junit.framework.TestCase.assertEquals("byteCount", 80000, bm1.getByteCount());
        junit.framework.TestCase.assertEquals("byteCount", 40000, bm2.getByteCount());
        junit.framework.TestCase.assertEquals("byteCount", 40000, bm3.getByteCount());
        junit.framework.TestCase.assertEquals("height", 200, bm1.getHeight());
        junit.framework.TestCase.assertEquals("height", 200, bm2.getHeight());
        junit.framework.TestCase.assertEquals("height", 200, bm3.getHeight());
        junit.framework.TestCase.assertTrue("hasAlpha", bm1.hasAlpha());
        junit.framework.TestCase.assertFalse("hasAlpha", bm2.hasAlpha());
        junit.framework.TestCase.assertTrue("hasAlpha", bm3.hasAlpha());
        junit.framework.TestCase.assertTrue("getConfig", bm1.getConfig() == android.graphics.Bitmap.Config.ARGB_8888);
        junit.framework.TestCase.assertTrue("getConfig", bm2.getConfig() == android.graphics.Bitmap.Config.RGB_565);
        junit.framework.TestCase.assertTrue("getConfig", bm3.getConfig() == android.graphics.Bitmap.Config.ARGB_4444);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testMutability() throws java.lang.Exception {
        android.graphics.Bitmap bm1 = android.graphics.Bitmap.createBitmap(100, 200, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Bitmap bm2 = android.graphics.Bitmap.createBitmap(new int[100 * 200], 100, 200, android.graphics.Bitmap.Config.ARGB_8888);
        junit.framework.TestCase.assertTrue("mutability", bm1.isMutable());
        junit.framework.TestCase.assertFalse("mutability", bm2.isMutable());
        bm1.eraseColor(0);
        try {
            bm2.eraseColor(0);
            junit.framework.TestCase.fail("eraseColor should throw exception");
        } catch (java.lang.IllegalStateException ex) {
            // safe to catch and ignore this
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetPixelsWithAlpha() throws java.lang.Exception {
        int[] colors = new int[100];
        for (int i = 0; i < 100; i++) {
            colors[i] = (((0xff << 24) | (i << 16)) | (i << 8)) | i;
        }
        android.graphics.Bitmap bm = android.graphics.Bitmap.createBitmap(colors, 10, 10, android.graphics.Bitmap.Config.ARGB_8888);
        int[] pixels = new int[100];
        bm.getPixels(pixels, 0, 10, 0, 0, 10, 10);
        for (int i = 0; i < 100; i++) {
            int p = bm.getPixel(i % 10, i / 10);
            junit.framework.TestCase.assertEquals("getPixels", p, pixels[i]);
        }
        for (int i = 0; i < 100; i++) {
            int p = bm.getPixel(i % 10, i / 10);
            junit.framework.TestCase.assertEquals("getPixel", p, colors[i]);
            junit.framework.TestCase.assertEquals("pixel value", p, (((0xff << 24) | (i << 16)) | (i << 8)) | i);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetPixelsWithoutAlpha() throws java.lang.Exception {
        int[] colors = new int[100];
        for (int i = 0; i < 100; i++) {
            colors[i] = (((0xff << 24) | (i << 16)) | (i << 8)) | i;
        }
        android.graphics.Bitmap bm = android.graphics.Bitmap.createBitmap(colors, 10, 10, android.graphics.Bitmap.Config.RGB_565);
        int[] pixels = new int[100];
        bm.getPixels(pixels, 0, 10, 0, 0, 10, 10);
        for (int i = 0; i < 100; i++) {
            int p = bm.getPixel(i % 10, i / 10);
            junit.framework.TestCase.assertEquals("getPixels", p, pixels[i]);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSetPixelsWithAlpha() throws java.lang.Exception {
        int[] colors = new int[100];
        for (int i = 0; i < 100; i++) {
            colors[i] = (((0xff << 24) | (i << 16)) | (i << 8)) | i;
        }
        android.graphics.Bitmap.Config config = android.graphics.Bitmap.Config.ARGB_8888;
        android.graphics.Bitmap bm1 = android.graphics.Bitmap.createBitmap(colors, 10, 10, config);
        android.graphics.Bitmap bm2 = android.graphics.Bitmap.createBitmap(10, 10, config);
        for (int i = 0; i < 100; i++) {
            bm2.setPixel(i % 10, i / 10, colors[i]);
        }
        for (int i = 0; i < 100; i++) {
            junit.framework.TestCase.assertEquals("setPixel", bm1.getPixel(i % 10, i / 10), bm2.getPixel(i % 10, i / 10));
        }
        for (int i = 0; i < 100; i++) {
            junit.framework.TestCase.assertEquals("setPixel value", bm1.getPixel(i % 10, i / 10), colors[i]);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSetPixelsWithoutAlpha() throws java.lang.Exception {
        int[] colors = new int[100];
        for (int i = 0; i < 100; i++) {
            colors[i] = (((0xff << 24) | (i << 16)) | (i << 8)) | i;
        }
        android.graphics.Bitmap.Config config = android.graphics.Bitmap.Config.RGB_565;
        android.graphics.Bitmap bm1 = android.graphics.Bitmap.createBitmap(colors, 10, 10, config);
        android.graphics.Bitmap bm2 = android.graphics.Bitmap.createBitmap(10, 10, config);
        for (int i = 0; i < 100; i++) {
            bm2.setPixel(i % 10, i / 10, colors[i]);
        }
        for (int i = 0; i < 100; i++) {
            junit.framework.TestCase.assertEquals("setPixel", bm1.getPixel(i % 10, i / 10), bm2.getPixel(i % 10, i / 10));
        }
    }

    private static int computePrePostMul(int alpha, int comp) {
        if (alpha == 0) {
            return 0;
        }
        int premul = java.lang.Math.round((alpha * comp) / 255.0F);
        int unpre = java.lang.Math.round((255.0F * premul) / alpha);
        return unpre;
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSetPixelsWithNonOpaqueAlpha() throws java.lang.Exception {
        int[] colors = new int[256];
        for (int i = 0; i < 256; i++) {
            colors[i] = (((i << 24) | (0xff << 16)) | (0x80 << 8)) | 0;
        }
        android.graphics.Bitmap.Config config = android.graphics.Bitmap.Config.ARGB_8888;
        // create a bitmap with the color array specified
        android.graphics.Bitmap bm1 = android.graphics.Bitmap.createBitmap(colors, 16, 16, config);
        // create a bitmap with no colors, but then call setPixels
        android.graphics.Bitmap bm2 = android.graphics.Bitmap.createBitmap(16, 16, config);
        bm2.setPixels(colors, 0, 16, 0, 0, 16, 16);
        // now check that we did a good job returning the unpremultiplied alpha
        final int tolerance = 1;
        for (int i = 0; i < 256; i++) {
            int c0 = colors[i];
            int c1 = bm1.getPixel(i % 16, i / 16);
            int c2 = bm2.getPixel(i % 16, i / 16);
            // these two should always be identical
            junit.framework.TestCase.assertEquals("getPixel", c1, c2);
            // comparing the original (c0) with the returned color is tricky,
            // since it gets premultiplied during the set(), and unpremultiplied
            // by the get().
            int a0 = android.graphics.Color.alpha(c0);
            int a1 = android.graphics.Color.alpha(c1);
            junit.framework.TestCase.assertEquals("alpha", a0, a1);
            int r0 = android.graphics.Color.red(c0);
            int r1 = android.graphics.Color.red(c1);
            int rr = android.graphics.BitmapTest.computePrePostMul(a0, r0);
            junit.framework.TestCase.assertTrue("red", java.lang.Math.abs(rr - r1) <= tolerance);
            int g0 = android.graphics.Color.green(c0);
            int g1 = android.graphics.Color.green(c1);
            int gg = android.graphics.BitmapTest.computePrePostMul(a0, g0);
            junit.framework.TestCase.assertTrue("green", java.lang.Math.abs(gg - g1) <= tolerance);
            int b0 = android.graphics.Color.blue(c0);
            int b1 = android.graphics.Color.blue(c1);
            int bb = android.graphics.BitmapTest.computePrePostMul(a0, b0);
            junit.framework.TestCase.assertTrue("blue", java.lang.Math.abs(bb - b1) <= tolerance);
            if (false) {
                int cc = android.graphics.Color.argb(a0, rr, gg, bb);
                android.graphics.android.util.Log.d("skia", (((("original " + java.lang.Integer.toHexString(c0)) + " set+get ") + java.lang.Integer.toHexString(c1)) + " local ") + java.lang.Integer.toHexString(cc));
            }
        }
    }
}

