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
package android.view;


/**
 * Builds the drawing cache of two Views, one smaller than the maximum cache size,
 * one larger than the maximum cache size. The latter should always have a null
 * drawing cache.
 */
public class BigCacheTest extends android.test.ActivityInstrumentationTestCase<android.view.BigCache> {
    private android.view.View mTiny;

    private android.view.View mLarge;

    public BigCacheTest() {
        super("com.android.frameworks.coretests", android.view.BigCache.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.BigCache activity = getActivity();
        mTiny = activity.findViewById(R.id.a);
        mLarge = activity.findViewById(R.id.b);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mTiny);
        assertNotNull(mLarge);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawingCacheBelowMaximumSize() throws java.lang.Exception {
        final int max = android.view.ViewConfiguration.get(getActivity()).getScaledMaximumDrawingCacheSize();
        assertTrue(((mTiny.getWidth() * mTiny.getHeight()) * 2) < max);
        assertNotNull(createCacheForView(mTiny));
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @MediumTest
    public void testDrawingCacheAboveMaximumSize() throws java.lang.Exception {
        final int max = android.view.ViewConfiguration.get(getActivity()).getScaledMaximumDrawingCacheSize();
        assertTrue(((mLarge.getWidth() * mLarge.getHeight()) * 2) > max);
        assertNull(createCacheForView(mLarge));
    }

    private android.graphics.Bitmap createCacheForView(final android.view.View view) {
        final android.graphics.Bitmap[] cache = new android.graphics.Bitmap[1];
        getActivity().runOnUiThread(new java.lang.Runnable() {
            public void run() {
                view.setDrawingCacheEnabled(true);
                view.invalidate();
                view.buildDrawingCache();
                cache[0] = view.getDrawingCache();
            }
        });
        getInstrumentation().waitForIdleSync();
        return cache[0];
    }
}

