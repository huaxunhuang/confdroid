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
package android.graphics.drawable;


public class IconTest extends android.test.AndroidTestCase {
    public static final java.lang.String TAG = android.graphics.drawable.IconTest.class.getSimpleName();

    public static void L(java.lang.String s, java.lang.Object... parts) {
        android.util.Log.d(android.graphics.drawable.IconTest.TAG, parts.length == 0 ? s : java.lang.String.format(s, parts));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testWithBitmap() throws java.lang.Exception {
        final android.graphics.Bitmap bm1 = android.graphics.Bitmap.createBitmap(100, 200, android.graphics.Bitmap.Config.ARGB_8888);
        final android.graphics.Bitmap bm2 = android.graphics.Bitmap.createBitmap(100, 200, android.graphics.Bitmap.Config.RGB_565);
        final android.graphics.Bitmap bm3 = ((android.graphics.drawable.BitmapDrawable) (getContext().getDrawable(R.drawable.landscape))).getBitmap();
        final android.graphics.Canvas can1 = new android.graphics.Canvas(bm1);
        can1.drawColor(0xffff0000);
        final android.graphics.Canvas can2 = new android.graphics.Canvas(bm2);
        can2.drawColor(0xff00ff00);
        final android.graphics.drawable.Icon im1 = android.graphics.drawable.Icon.createWithBitmap(bm1);
        final android.graphics.drawable.Icon im2 = android.graphics.drawable.Icon.createWithBitmap(bm2);
        final android.graphics.drawable.Icon im3 = android.graphics.drawable.Icon.createWithBitmap(bm3);
        final android.graphics.drawable.Drawable draw1 = im1.loadDrawable(mContext);
        final android.graphics.drawable.Drawable draw2 = im2.loadDrawable(mContext);
        final android.graphics.drawable.Drawable draw3 = im3.loadDrawable(mContext);
        final android.graphics.Bitmap test1 = android.graphics.Bitmap.createBitmap(draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        final android.graphics.Bitmap test2 = android.graphics.Bitmap.createBitmap(draw2.getIntrinsicWidth(), draw2.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        final android.graphics.Bitmap test3 = android.graphics.Bitmap.createBitmap(draw3.getIntrinsicWidth(), draw3.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        draw1.setBounds(0, 0, draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight());
        draw1.draw(new android.graphics.Canvas(test1));
        draw2.setBounds(0, 0, draw2.getIntrinsicWidth(), draw2.getIntrinsicHeight());
        draw2.draw(new android.graphics.Canvas(test2));
        draw3.setBounds(0, 0, draw3.getIntrinsicWidth(), draw3.getIntrinsicHeight());
        draw3.draw(new android.graphics.Canvas(test3));
        final java.io.File dir = getContext().getExternalFilesDir(null);
        android.graphics.drawable.IconTest.L("writing temp bitmaps to %s...", dir);
        bm1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "bitmap1-original.png")));
        test1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "bitmap1-test.png")));
        if (!equalBitmaps(bm1, test1)) {
            findBitmapDifferences(bm1, test1);
            fail("bitmap1 differs, check " + dir);
        }
        bm2.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "bitmap2-original.png")));
        test2.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "bitmap2-test.png")));
        if (!equalBitmaps(bm2, test2)) {
            findBitmapDifferences(bm2, test2);
            fail("bitmap2 differs, check " + dir);
        }
        bm3.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "bitmap3-original.png")));
        test3.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "bitmap3-test.png")));
        if (!equalBitmaps(bm3, test3)) {
            findBitmapDifferences(bm3, test3);
            fail("bitmap3 differs, check " + dir);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testWithBitmapResource() throws java.lang.Exception {
        final android.graphics.Bitmap res1 = ((android.graphics.drawable.BitmapDrawable) (getContext().getDrawable(R.drawable.landscape))).getBitmap();
        final android.graphics.drawable.Icon im1 = android.graphics.drawable.Icon.createWithResource(getContext(), R.drawable.landscape);
        final android.graphics.drawable.Drawable draw1 = im1.loadDrawable(mContext);
        final android.graphics.Bitmap test1 = android.graphics.Bitmap.createBitmap(draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        draw1.setBounds(0, 0, test1.getWidth(), test1.getHeight());
        draw1.draw(new android.graphics.Canvas(test1));
        final java.io.File dir = getContext().getExternalFilesDir(null);
        res1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "res1-original.png")));
        test1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "res1-test.png")));
        if (!equalBitmaps(res1, test1)) {
            findBitmapDifferences(res1, test1);
            fail("res1 differs, check " + dir);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testWithFile() throws java.lang.Exception {
        final android.graphics.Bitmap bit1 = ((android.graphics.drawable.BitmapDrawable) (getContext().getDrawable(R.drawable.landscape))).getBitmap();
        final java.io.File dir = getContext().getExternalFilesDir(null);
        final java.io.File file1 = new java.io.File(dir, "file1-original.png");
        bit1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(file1));
        final android.graphics.drawable.Icon im1 = android.graphics.drawable.Icon.createWithFilePath(file1.toString());
        final android.graphics.drawable.Drawable draw1 = im1.loadDrawable(mContext);
        final android.graphics.Bitmap test1 = android.graphics.Bitmap.createBitmap(draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        draw1.setBounds(0, 0, test1.getWidth(), test1.getHeight());
        draw1.draw(new android.graphics.Canvas(test1));
        test1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "file1-test.png")));
        if (!equalBitmaps(bit1, test1)) {
            findBitmapDifferences(bit1, test1);
            fail("testWithFile: file1 differs, check " + dir);
        }
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testAsync() throws java.lang.Exception {
        final android.graphics.Bitmap bit1 = ((android.graphics.drawable.BitmapDrawable) (getContext().getDrawable(R.drawable.landscape))).getBitmap();
        final java.io.File dir = getContext().getExternalFilesDir(null);
        final java.io.File file1 = new java.io.File(dir, "async-original.png");
        bit1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(file1));
        final android.graphics.drawable.Icon im1 = android.graphics.drawable.Icon.createWithFilePath(file1.toString());
        final android.os.HandlerThread thd = new android.os.HandlerThread("testAsync");
        thd.start();
        final android.os.Handler h = new android.os.Handler(thd.getLooper());
        android.graphics.drawable.IconTest.L(android.graphics.drawable.IconTest.TAG, "asyncTest: dispatching load to thread: " + thd);
        im1.loadDrawableAsync(mContext, new android.graphics.drawable.Icon.OnDrawableLoadedListener() {
            @java.lang.Override
            public void onDrawableLoaded(android.graphics.drawable.Drawable draw1) {
                android.graphics.drawable.IconTest.L(android.graphics.drawable.IconTest.TAG, "asyncTest: thread: loading drawable");
                android.graphics.drawable.IconTest.L(android.graphics.drawable.IconTest.TAG, "asyncTest: thread: loaded: %dx%d", draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight());
                final android.graphics.Bitmap test1 = android.graphics.Bitmap.createBitmap(draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
                draw1.setBounds(0, 0, test1.getWidth(), test1.getHeight());
                draw1.draw(new android.graphics.Canvas(test1));
                try {
                    test1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(new java.io.File(dir, "async-test.png")));
                } catch (java.io.FileNotFoundException ex) {
                    fail("couldn't create test file: " + ex);
                }
                if (!equalBitmaps(bit1, test1)) {
                    findBitmapDifferences(bit1, test1);
                    fail("testAsync: file1 differs, check " + dir);
                }
            }
        }, h);
        android.graphics.drawable.IconTest.L(android.graphics.drawable.IconTest.TAG, "asyncTest: awaiting result");
        java.lang.Thread.sleep(500);// ;_;

        assertTrue("async-test.png does not exist!", new java.io.File(dir, "async-test.png").exists());
        android.graphics.drawable.IconTest.L(android.graphics.drawable.IconTest.TAG, "asyncTest: done");
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testParcel() throws java.lang.Exception {
        final android.graphics.Bitmap originalbits = ((android.graphics.drawable.BitmapDrawable) (getContext().getDrawable(R.drawable.landscape))).getBitmap();
        final java.io.ByteArrayOutputStream ostream = new java.io.ByteArrayOutputStream((originalbits.getWidth() * originalbits.getHeight()) * 2);// guess 50% compression

        originalbits.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, ostream);
        final byte[] pngdata = ostream.toByteArray();
        android.graphics.drawable.IconTest.L("starting testParcel; bitmap: %d bytes, PNG: %d bytes", originalbits.getByteCount(), pngdata.length);
        final java.io.File dir = getContext().getExternalFilesDir(null);
        final java.io.File originalfile = new java.io.File(dir, "parcel-original.png");
        new java.io.FileOutputStream(originalfile).write(pngdata);
        java.util.ArrayList<android.graphics.drawable.Icon> imgs = new java.util.ArrayList<>();
        final android.graphics.drawable.Icon file1 = android.graphics.drawable.Icon.createWithFilePath(originalfile.getAbsolutePath());
        imgs.add(file1);
        final android.graphics.drawable.Icon bit1 = android.graphics.drawable.Icon.createWithBitmap(originalbits);
        imgs.add(bit1);
        final android.graphics.drawable.Icon data1 = android.graphics.drawable.Icon.createWithData(pngdata, 0, pngdata.length);
        imgs.add(data1);
        final android.graphics.drawable.Icon res1 = android.graphics.drawable.Icon.createWithResource(getContext(), R.drawable.landscape);
        imgs.add(res1);
        java.util.ArrayList<android.graphics.drawable.Icon> test = new java.util.ArrayList<>();
        final android.os.Parcel parcel = android.os.Parcel.obtain();
        int pos = 0;
        parcel.writeInt(imgs.size());
        for (android.graphics.drawable.Icon img : imgs) {
            img.writeToParcel(parcel, 0);
            android.graphics.drawable.IconTest.L("used %d bytes parceling: %s", parcel.dataPosition() - pos, img);
            pos = parcel.dataPosition();
        }
        parcel.setDataPosition(0);// rewind

        final int N = parcel.readInt();
        for (int i = 0; i < N; i++) {
            android.graphics.drawable.Icon img = this.CREATOR.createFromParcel(parcel);
            android.graphics.drawable.IconTest.L("test %d: read from parcel: %s", i, img);
            final java.io.File testfile = new java.io.File(dir, java.lang.String.format("parcel-test%02d.png", i));
            final android.graphics.drawable.Drawable draw1 = img.loadDrawable(mContext);
            if (draw1 == null) {
                fail("null drawable from img: " + img);
            }
            final android.graphics.Bitmap test1 = android.graphics.Bitmap.createBitmap(draw1.getIntrinsicWidth(), draw1.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
            draw1.setBounds(0, 0, test1.getWidth(), test1.getHeight());
            draw1.draw(new android.graphics.Canvas(test1));
            try {
                test1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(testfile));
            } catch (java.io.FileNotFoundException ex) {
                fail((("couldn't create test file " + testfile) + ": ") + ex);
            }
            if (!equalBitmaps(originalbits, test1)) {
                findBitmapDifferences(originalbits, test1);
                fail((testfile + " differs from original: ") + originalfile);
            }
        }
    }

    // ======== utils ========
    static final char[] GRADIENT = " .:;+=xX$#".toCharArray();

    static float[] hsv = new float[3];

    static char colorToChar(int color) {
        int sum = (((color >> 16) & 0xff) + ((color >> 8) & 0xff)) + (color & 0xff);
        return android.graphics.drawable.IconTest.GRADIENT[(sum * (android.graphics.drawable.IconTest.GRADIENT.length - 1)) / (3 * 0xff)];
    }

    static void printBits(int[] a, int w, int h) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                sb.append(android.graphics.drawable.IconTest.colorToChar(a[i + (w * j)]));
            }
            sb.append('\n');
        }
        android.graphics.drawable.IconTest.L(sb.toString());
    }

    static void printBits(android.graphics.Bitmap a) {
        final int w = a.getWidth();
        final int h = a.getHeight();
        int[] aPix = new int[w * h];
        android.graphics.drawable.IconTest.printBits(aPix, w, h);
    }

    boolean equalBitmaps(android.graphics.Bitmap a, android.graphics.Bitmap b) {
        if ((a.getWidth() != b.getWidth()) || (a.getHeight() != b.getHeight()))
            return false;

        final int w = a.getWidth();
        final int h = a.getHeight();
        int[] aPix = new int[w * h];
        int[] bPix = new int[w * h];
        a.getPixels(aPix, 0, w, 0, 0, w, h);
        b.getPixels(bPix, 0, w, 0, 0, w, h);
        return java.util.Arrays.equals(aPix, bPix);
    }

    void findBitmapDifferences(android.graphics.Bitmap a, android.graphics.Bitmap b) {
        if ((a.getWidth() != b.getWidth()) || (a.getHeight() != b.getHeight())) {
            android.graphics.drawable.IconTest.L("different sizes: %dx%d vs %dx%d", a.getWidth(), a.getHeight(), b.getWidth(), b.getHeight());
            return;
        }
        final int w = a.getWidth();
        final int h = a.getHeight();
        int[] aPix = new int[w * h];
        int[] bPix = new int[w * h];
        a.getPixels(aPix, 0, w, 0, 0, w, h);
        b.getPixels(bPix, 0, w, 0, 0, w, h);
        android.graphics.drawable.IconTest.L("bitmap a (%dx%d)", w, h);
        android.graphics.drawable.IconTest.printBits(aPix, w, h);
        android.graphics.drawable.IconTest.L("bitmap b (%dx%d)", w, h);
        android.graphics.drawable.IconTest.printBits(bPix, w, h);
        java.lang.StringBuffer sb = new java.lang.StringBuffer("Different pixels: ");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (aPix[i + (w * j)] != bPix[i + (w * j)]) {
                    sb.append(" ").append(i).append(",").append(j);
                }
            }
        }
        android.graphics.drawable.IconTest.L(sb.toString());
    }
}

