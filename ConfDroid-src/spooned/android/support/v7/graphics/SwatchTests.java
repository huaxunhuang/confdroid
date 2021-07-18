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
public class SwatchTests {
    private static final float MIN_CONTRAST_TITLE_TEXT = 3.0F;

    private static final float MIN_CONTRAST_BODY_TEXT = 4.5F;

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testTextColorContrasts() {
        final android.support.v7.graphics.Palette p = android.support.v7.graphics.Palette.from(android.support.v7.graphics.TestUtils.loadSampleBitmap()).generate();
        for (android.support.v7.graphics.Palette.Swatch swatch : p.getSwatches()) {
            testSwatchTextColorContrasts(swatch);
        }
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testHslNotNull() {
        final android.support.v7.graphics.Palette p = android.support.v7.graphics.Palette.from(android.support.v7.graphics.TestUtils.loadSampleBitmap()).generate();
        for (android.support.v7.graphics.Palette.Swatch swatch : p.getSwatches()) {
            org.junit.Assert.assertNotNull(swatch.getHsl());
        }
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testHslIsRgb() {
        final android.support.v7.graphics.Palette p = android.support.v7.graphics.Palette.from(android.support.v7.graphics.TestUtils.loadSampleBitmap()).generate();
        for (android.support.v7.graphics.Palette.Swatch swatch : p.getSwatches()) {
            org.junit.Assert.assertEquals(android.support.v4.graphics.ColorUtils.HSLToColor(swatch.getHsl()), swatch.getRgb());
        }
    }

    private void testSwatchTextColorContrasts(android.support.v7.graphics.Palette.Swatch swatch) {
        final int bodyTextColor = swatch.getBodyTextColor();
        org.junit.Assert.assertTrue(android.support.v4.graphics.ColorUtils.calculateContrast(bodyTextColor, swatch.getRgb()) >= android.support.v7.graphics.SwatchTests.MIN_CONTRAST_BODY_TEXT);
        final int titleTextColor = swatch.getTitleTextColor();
        org.junit.Assert.assertTrue(android.support.v4.graphics.ColorUtils.calculateContrast(titleTextColor, swatch.getRgb()) >= android.support.v7.graphics.SwatchTests.MIN_CONTRAST_TITLE_TEXT);
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testEqualsWhenSame() {
        android.support.v7.graphics.Palette.Swatch swatch1 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.WHITE, 50);
        android.support.v7.graphics.Palette.Swatch swatch2 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.WHITE, 50);
        org.junit.Assert.assertEquals(swatch1, swatch2);
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testEqualsWhenColorDifferent() {
        android.support.v7.graphics.Palette.Swatch swatch1 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLACK, 50);
        android.support.v7.graphics.Palette.Swatch swatch2 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.WHITE, 50);
        org.junit.Assert.assertFalse(swatch1.equals(swatch2));
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testEqualsWhenPopulationDifferent() {
        android.support.v7.graphics.Palette.Swatch swatch1 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLACK, 50);
        android.support.v7.graphics.Palette.Swatch swatch2 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLACK, 100);
        org.junit.Assert.assertFalse(swatch1.equals(swatch2));
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.SmallTest
    public void testEqualsWhenDifferent() {
        android.support.v7.graphics.Palette.Swatch swatch1 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLUE, 50);
        android.support.v7.graphics.Palette.Swatch swatch2 = new android.support.v7.graphics.Palette.Swatch(android.graphics.Color.BLACK, 100);
        org.junit.Assert.assertFalse(swatch1.equals(swatch2));
    }
}

