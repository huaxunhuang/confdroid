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
 * Tests of {@link android.graphics.ColorStateList}
 */
public class ColorStateListTest extends android.test.AndroidTestCase {
    private android.content.res.Resources mResources;

    private int mFailureColor;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mResources = mContext.getResources();
        mFailureColor = mResources.getColor(R.color.failColor);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testStateIsInList() throws java.lang.Exception {
        android.content.res.ColorStateList colorStateList = mResources.getColorStateList(R.color.color1);
        int[] focusedState = new int[]{ android.graphics.android.R.attr };
        int focusColor = colorStateList.getColorForState(focusedState, R.color.failColor);
        assertEquals(mResources.getColor(R.color.testcolor1), focusColor);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testEmptyState() throws java.lang.Exception {
        android.content.res.ColorStateList colorStateList = mResources.getColorStateList(R.color.color1);
        int[] emptyState = new int[]{  };
        int defaultColor = colorStateList.getColorForState(emptyState, mFailureColor);
        assertEquals(mResources.getColor(R.color.testcolor2), defaultColor);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetColor() throws java.lang.Exception {
        int defaultColor = mResources.getColor(R.color.color1);
        assertEquals(mResources.getColor(R.color.testcolor2), defaultColor);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGetColorWhenListHasNoDefault() throws java.lang.Exception {
        int defaultColor = mResources.getColor(R.color.color_no_default);
        assertEquals(mResources.getColor(R.color.testcolor1), defaultColor);
    }
}

