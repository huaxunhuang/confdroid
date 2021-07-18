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
public class ConsistencyTest {
    private static final int NUMBER_TRIALS = 10;

    @org.junit.Test
    @android.test.suitebuilder.annotation.MediumTest
    public void testConsistency() {
        android.support.v7.graphics.Palette lastPalette = null;
        final android.graphics.Bitmap bitmap = android.support.v7.graphics.TestUtils.loadSampleBitmap();
        for (int i = 0; i < android.support.v7.graphics.ConsistencyTest.NUMBER_TRIALS; i++) {
            android.support.v7.graphics.Palette newPalette = android.support.v7.graphics.Palette.from(bitmap).generate();
            if (lastPalette != null) {
                android.support.v7.graphics.ConsistencyTest.assetPalettesEqual(lastPalette, newPalette);
            }
            lastPalette = newPalette;
        }
    }

    private static void assetPalettesEqual(android.support.v7.graphics.Palette p1, android.support.v7.graphics.Palette p2) {
        org.junit.Assert.assertEquals(p1.getVibrantSwatch(), p2.getVibrantSwatch());
        org.junit.Assert.assertEquals(p1.getLightVibrantSwatch(), p2.getLightVibrantSwatch());
        org.junit.Assert.assertEquals(p1.getDarkVibrantSwatch(), p2.getDarkVibrantSwatch());
        org.junit.Assert.assertEquals(p1.getMutedSwatch(), p2.getMutedSwatch());
        org.junit.Assert.assertEquals(p1.getLightMutedSwatch(), p2.getLightMutedSwatch());
        org.junit.Assert.assertEquals(p1.getDarkMutedSwatch(), p2.getDarkMutedSwatch());
    }
}

