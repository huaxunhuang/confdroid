/**
 * Copyright (C) 2007 The Android Open Source Project
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


/**
 * Graphics Performance Tests
 */
// We don't want to run these perf tests in the continuous build.
@android.test.suitebuilder.annotation.Suppress
public class GraphicsPerformanceTests {
    private static final java.lang.String TAG = "GfxPerf";

    public static java.lang.String[] children() {
        return new java.lang.String[]{ // test decoding bitmaps of various sizes
        android.graphics.GraphicsPerformanceTests.DecodeBitmapTest.class.getName(), // odd-sized bitmap drawing tests
        android.graphics.GraphicsPerformanceTests.DrawBitmap7x7.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap15x15.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap31x31.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap63x63.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap127x127.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap319x239.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap319x479.class.getName(), // even-sized bitmap drawing tests
        android.graphics.GraphicsPerformanceTests.DrawBitmap8x8.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap16x16.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap32x32.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap64x64.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap128x128.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap320x240.class.getName(), android.graphics.GraphicsPerformanceTests.DrawBitmap320x480.class.getName() };
    }

    /**
     * Base class for all graphics tests
     */
    public static abstract class GraphicsTestBase extends android.test.AndroidTestCase implements android.test.PerformanceTestCase {
        /**
         * Target "screen" (bitmap) width and height
         */
        private static final int DEFAULT_ITERATIONS = 1;

        private static final int SCREEN_WIDTH = 320;

        private static final int SCREEN_HEIGHT = 480;

        /**
         * Number of iterations to pass back to harness. Subclass should override
         */
        protected int mIterations = 1;

        /**
         * Bitmap we allocate and draw to
         */
        protected android.graphics.Bitmap mDestBitmap;

        /**
         * Canvas of drawing routines
         */
        protected android.graphics.Canvas mCanvas;

        /**
         * Style and color information (uses defaults)
         */
        protected android.graphics.Paint mPaint;

        @java.lang.Override
        public void setUp() throws java.lang.Exception {
            setUp();
            // Create drawable bitmap for rendering into
            mDestBitmap = android.graphics.Bitmap.createBitmap(android.graphics.GraphicsPerformanceTests.GraphicsTestBase.SCREEN_WIDTH, android.graphics.GraphicsPerformanceTests.GraphicsTestBase.SCREEN_HEIGHT, android.graphics.Bitmap.Config.RGB_565);
            // Set of drawing routines
            mCanvas = new android.graphics.Canvas(mDestBitmap);
            // Styles
            mPaint = new android.graphics.Paint();
            // Ask subclass for number of iterations
            mIterations = getIterations();
        }

        // A reasonable default
        public int getIterations() {
            return android.graphics.GraphicsPerformanceTests.GraphicsTestBase.DEFAULT_ITERATIONS;
        }

        public boolean isPerformanceOnly() {
            return true;
        }

        public int startPerformance(android.graphics.Intermediates intermediates) {
            intermediates.setInternalIterations(mIterations * 10);
            return 0;
        }
    }

    /**
     * Tests time to decode a number of sizes of images.
     */
    public static class DecodeBitmapTest extends android.graphics.GraphicsPerformanceTests.GraphicsTestBase {
        /**
         * Number of times to run this test
         */
        private static final int DECODE_ITERATIONS = 10;

        /**
         * Used to access package bitmap images
         */
        private android.content.res.Resources mResources;

        @java.lang.Override
        public void setUp() throws java.lang.Exception {
            super.setUp();
            // For bitmap resources
            android.content.Context context = getContext();
            junit.framework.Assert.assertNotNull(context);
            mResources = context.getResources();
            junit.framework.Assert.assertNotNull(mResources);
        }

        @java.lang.Override
        public int getIterations() {
            return android.graphics.GraphicsPerformanceTests.DecodeBitmapTest.DECODE_ITERATIONS;
        }

        public void testDecodeBitmap() {
            for (int i = 0; i < android.graphics.GraphicsPerformanceTests.DecodeBitmapTest.DECODE_ITERATIONS; i++) {
                android.graphics.BitmapFactory.decodeResource(mResources, R.drawable.test16x12);
                android.graphics.BitmapFactory.decodeResource(mResources, R.drawable.test32x24);
                android.graphics.BitmapFactory.decodeResource(mResources, R.drawable.test64x48);
                android.graphics.BitmapFactory.decodeResource(mResources, R.drawable.test128x96);
                android.graphics.BitmapFactory.decodeResource(mResources, R.drawable.test256x192);
                android.graphics.BitmapFactory.decodeResource(mResources, R.drawable.test320x240);
            }
        }
    }

    /**
     * Base class for bitmap drawing tests
     */
    public static abstract class DrawBitmapTest extends android.graphics.GraphicsPerformanceTests.GraphicsTestBase {
        /**
         * Number of times to run each draw test
         */
        private static final int ITERATIONS = 1000;

        /**
         * Bitmap to draw. Allocated by subclass's createBitmap() function.
         */
        private android.graphics.Bitmap mBitmap;

        @java.lang.Override
        public void setUp() throws java.lang.Exception {
            super.setUp();
            // Invoke subclass's method to create the bitmap
            mBitmap = createBitmap();
        }

        public int getIterations() {
            return android.graphics.GraphicsPerformanceTests.DrawBitmapTest.ITERATIONS;
        }

        // Generic abstract function to create bitmap for any given subclass
        public abstract android.graphics.Bitmap createBitmap();

        // Provide convenience test code for all subsequent classes.
        // Note: Though it would be convenient to declare all of the test*() methods here
        // and just inherit them, our test harness doesn't support it. So we replicate
        // a bit of code in each derived test case.
        public void drawBitmapEven() {
            for (int i = 0; i < android.graphics.GraphicsPerformanceTests.DrawBitmapTest.ITERATIONS; i++) {
                mCanvas.drawBitmap(mBitmap, 0.0F, 0.0F, mPaint);
            }
        }

        public void drawBitmapOdd() {
            for (int i = 0; i < android.graphics.GraphicsPerformanceTests.DrawBitmapTest.ITERATIONS; i++) {
                mCanvas.drawBitmap(mBitmap, 1.0F, 0.0F, mPaint);
            }
        }
    }

    /**
     * Test drawing of 7x7 image
     */
    public static class DrawBitmap7x7 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(7, 7, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 15x15 image
     */
    public static class DrawBitmap15x15 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(15, 15, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 31x31 image
     */
    public static class DrawBitmap31x31 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(31, 31, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 63x63 image
     */
    public static class DrawBitmap63x63 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(63, 63, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 127x127 image
     */
    public static class DrawBitmap127x127 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(127, 127, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 319x239 image
     */
    public static class DrawBitmap319x239 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(319, 239, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 319x479 image
     */
    public static class DrawBitmap319x479 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(319, 479, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 8x8 image
     */
    public static class DrawBitmap8x8 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(8, 8, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 16x16 image
     */
    public static class DrawBitmap16x16 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(16, 16, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 32x32 image
     */
    public static class DrawBitmap32x32 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(32, 32, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 64x64 image
     */
    public static class DrawBitmap64x64 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(64, 64, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 128x128 image
     */
    public static class DrawBitmap128x128 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(128, 128, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 320x240 image
     */
    public static class DrawBitmap320x240 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(320, 240, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 320x480 image
     */
    public static class DrawBitmap320x480 extends android.graphics.GraphicsPerformanceTests.DrawBitmapTest {
        public android.graphics.Bitmap createBitmap() {
            return android.graphics.Bitmap.createBitmap(320, 480, android.graphics.Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }
}

