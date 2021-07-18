/**
 * Copyright (C) 2010 The Android Open Source Project
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


public class BitmapFactoryTest extends junit.framework.TestCase {
    // tests that we can decode bitmaps from MemoryFiles
    @android.test.suitebuilder.annotation.SmallTest
    public void testBitmapParcelFileDescriptor() throws java.lang.Exception {
        android.graphics.Bitmap bitmap1 = android.graphics.Bitmap.createBitmap(new int[]{ android.graphics.Color.BLUE }, 1, 1, android.graphics.Bitmap.Config.RGB_565);
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        bitmap1.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
        android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.fromData(out.toByteArray(), null);
        java.io.FileDescriptor fd = pfd.getFileDescriptor();
        junit.framework.TestCase.assertNotNull("Got null FileDescriptor", fd);
        junit.framework.TestCase.assertTrue("Got invalid FileDescriptor", fd.valid());
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFileDescriptor(fd);
        junit.framework.TestCase.assertNotNull("BitmapFactory returned null", bitmap);
        junit.framework.TestCase.assertEquals("Bitmap width", 1, bitmap.getWidth());
        junit.framework.TestCase.assertEquals("Bitmap height", 1, bitmap.getHeight());
    }
}

