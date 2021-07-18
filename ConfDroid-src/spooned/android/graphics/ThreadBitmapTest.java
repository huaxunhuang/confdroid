/**
 * Copyright (C) 2008 The Android Open Source Project
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


public class ThreadBitmapTest extends junit.framework.TestCase {
    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testCreation() {
        for (int i = 0; i < 200; i++) {
            new android.graphics.ThreadBitmapTest.MThread().start();
        }
    }

    class MThread extends java.lang.Thread {
        public android.graphics.Bitmap b;

        public MThread() {
            b = android.graphics.Bitmap.createBitmap(300, 300, android.graphics.Bitmap.Config.RGB_565);
        }

        public void run() {
        }
    }
}

