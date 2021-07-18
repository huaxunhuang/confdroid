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
package android.widget.scroll.arrowscroll;


public class ButtonsWithTallTextViewInBetweenTest extends android.test.ActivityInstrumentationTestCase<android.widget.scroll.ButtonsWithTallTextViewInBetween> {
    private android.widget.ScrollView mScrollView;

    private android.widget.Button mTopButton;

    private android.widget.TextView mMiddleFiller;

    private android.widget.TextView mBottomButton;

    public ButtonsWithTallTextViewInBetweenTest() {
        super("com.android.frameworks.coretests", android.widget.scroll.ButtonsWithTallTextViewInBetween.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mScrollView = getActivity().getScrollView();
        mTopButton = getActivity().getTopButton();
        mMiddleFiller = getActivity().getMiddleFiller();
        mBottomButton = getActivity().getBottomButton();
    }

    private android.graphics.Rect mTempRect = new android.graphics.Rect();

    private int getTopWithinScrollView(android.view.View descendant) {
        descendant.getDrawingRect(mTempRect);
        mScrollView.offsetDescendantRectToMyCoords(descendant, mTempRect);
        return mTempRect.top;
    }

    private int getBottomWithinScrollView(android.view.View descendant) {
        descendant.getDrawingRect(mTempRect);
        mScrollView.offsetDescendantRectToMyCoords(descendant, mTempRect);
        return mTempRect.bottom;
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("top button should be shorter than max scroll amount", mTopButton.getHeight() < mScrollView.getMaxScrollAmount());
        assertTrue("bottom button should be further than max scroll amount off screen", (getTopWithinScrollView(mBottomButton) - mScrollView.getBottom()) > mScrollView.getMaxScrollAmount());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPanTopButtonOffScreenLosesFocus() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("scroll view should be scrolled by the max amount for one " + "arrow navigation", mScrollView.getMaxScrollAmount(), mScrollView.getScrollY());
        assertTrue("top button should be off screen", getBottomWithinScrollView(mTopButton) < mScrollView.getScrollY());
        assertFalse("top button should have lost focus", mTopButton.isFocused());
        assertTrue("scroll view should be focused", mScrollView.isFocused());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testScrollDownToBottomButton() throws java.lang.Exception {
        final int screenBottom = mScrollView.getScrollY() + mScrollView.getHeight();
        final int numDownsToButtonButton = ((getBottomWithinScrollView(mBottomButton) - screenBottom) / mScrollView.getMaxScrollAmount()) + 1;
        for (int i = 0; i < numDownsToButtonButton; i++) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        assertTrue("bottombutton.isFocused", mBottomButton.isFocused());
        assertEquals("should be fully scrolled to bottom", getActivity().getLinearLayout().getHeight() - mScrollView.getHeight(), mScrollView.getScrollY());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPanBottomButtonOffScreenLosesFocus() throws java.lang.Exception {
        mBottomButton.post(new java.lang.Runnable() {
            public void run() {
                mBottomButton.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue("bottombutton.isFocused", mBottomButton.isFocused());
        final int maxScroll = getActivity().getLinearLayout().getHeight() - mScrollView.getHeight();
        assertEquals("should be fully scrolled to bottom", maxScroll, mScrollView.getScrollY());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("scroll view should have scrolled by the max amount for one " + "arrow navigation", maxScroll - mScrollView.getMaxScrollAmount(), mScrollView.getScrollY());
        assertTrue("bottom button should be off screen", getTopWithinScrollView(mBottomButton) > (mScrollView.getScrollY() + mScrollView.getHeight()));
        assertFalse("bottom button should have lost focus", mBottomButton.isFocused());
        assertTrue("scroll view should be focused", mScrollView.isFocused());
    }
}

