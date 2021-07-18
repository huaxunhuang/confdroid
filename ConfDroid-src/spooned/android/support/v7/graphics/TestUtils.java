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
package android.support.v7.graphics;


class TestUtils {
    static android.graphics.Bitmap loadSampleBitmap() {
        return android.graphics.BitmapFactory.decodeResource(android.support.test.InstrumentationRegistry.getContext().getResources(), R.drawable.photo);
    }

    static void assertCloseColors(int expected, int actual) {
        org.junit.Assert.assertEquals(android.graphics.Color.red(expected), android.graphics.Color.red(actual), 2);
        org.junit.Assert.assertEquals(android.graphics.Color.green(expected), android.graphics.Color.green(actual), 2);
        org.junit.Assert.assertEquals(android.graphics.Color.blue(expected), android.graphics.Color.blue(actual), 2);
    }
}

