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
 * Builds the drawing cache of Views of various dimension. The assumption is that
 * a View with a 0-sized dimension (width or height) will always have a null
 * drawing cache.
 */
public class ZeroSizedTest extends android.test.ActivityInstrumentationTestCase<android.view.ZeroSized> {
    private android.view.View mWithDimension;

    private android.view.View mWithNoWdith;

    private android.view.View mWithNoHeight;

    private android.view.View mWithNoDimension;

    public ZeroSizedTest() {
        super("com.android.frameworks.coretests", android.view.ZeroSized.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.ZeroSized activity = getActivity();
        mWithDimension = activity.findViewById(R.id.dimension);
        mWithNoWdith = activity.findViewById(R.id.noWidth);
        mWithNoHeight = activity.findViewById(R.id.noHeight);
        mWithNoDimension = activity.findViewById(R.id.noDimension);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mWithDimension);
        assertNotNull(mWithNoWdith);
        assertNotNull(mWithNoHeight);
        assertNotNull(mWithNoDimension);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawingCacheWithDimension() throws java.lang.Exception {
        assertTrue(mWithDimension.getWidth() > 0);
        assertTrue(mWithDimension.getHeight() > 0);
        assertNotNull(createCacheForView(mWithDimension));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawingCacheWithNoWidth() throws java.lang.Exception {
        assertTrue(mWithNoWdith.getWidth() == 0);
        assertTrue(mWithNoWdith.getHeight() > 0);
        assertNull(createCacheForView(mWithNoWdith));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawingCacheWithNoHeight() throws java.lang.Exception {
        assertTrue(mWithNoHeight.getWidth() > 0);
        assertTrue(mWithNoHeight.getHeight() == 0);
        assertNull(createCacheForView(mWithNoHeight));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDrawingCacheWithNoDimension() throws java.lang.Exception {
        assertTrue(mWithNoDimension.getWidth() == 0);
        assertTrue(mWithNoDimension.getHeight() == 0);
        assertNull(createCacheForView(mWithNoDimension));
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

