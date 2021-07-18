/**
 * Copyright (C) 2011 Sony Ericsson Mobile Communications AB.
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
package android.widget.scroll.arrowscroll;


public class MultiPageTextWithPaddingTest extends android.test.ActivityInstrumentationTestCase<android.widget.scroll.arrowscroll.MultiPageTextWithPadding> {
    private android.widget.ScrollView mScrollView;

    private android.widget.TextView mTextView;

    public MultiPageTextWithPaddingTest() {
        super("com.android.frameworks.coretests", android.widget.scroll.arrowscroll.MultiPageTextWithPadding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mScrollView = getActivity().getScrollView();
        mTextView = getActivity().getContentChildAt(0);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("text should not fit on screen", mTextView.getHeight() > mScrollView.getHeight());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testScrollDownToBottom() throws java.lang.Exception {
        // Calculate the number of arrow scrolls needed to reach the bottom
        int scrollsNeeded = ((int) (java.lang.Math.ceil(java.lang.Math.max(0.0F, mTextView.getHeight() - mScrollView.getHeight()) / mScrollView.getMaxScrollAmount())));
        for (int i = 0; i < scrollsNeeded; i++) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        assertEquals("should be fully scrolled to bottom", getActivity().getLinearLayout().getHeight() - ((mScrollView.getHeight() - mScrollView.getPaddingTop()) - mScrollView.getPaddingBottom()), mScrollView.getScrollY());
    }
}

