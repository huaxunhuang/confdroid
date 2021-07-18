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
public class MaxColorsTest {
    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testMaxColorCount32() {
        testMaxColorCount(32);
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testMaxColorCount1() {
        testMaxColorCount(1);
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testMaxColorCount15() {
        testMaxColorCount(15);
    }

    private void testMaxColorCount(int colorCount) {
        android.support.v7.graphics.Palette newPalette = android.support.v7.graphics.Palette.from(android.support.v7.graphics.TestUtils.loadSampleBitmap()).maximumColorCount(colorCount).generate();
        org.junit.Assert.assertTrue(newPalette.getSwatches().size() <= colorCount);
    }
}

