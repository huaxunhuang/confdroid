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


public class ShortButtonsTest extends android.test.ActivityInstrumentationTestCase<android.widget.scroll.ShortButtons> {
    private android.widget.ScrollView mScrollView;

    public ShortButtonsTest() {
        super("com.android.frameworks.coretests", android.widget.scroll.ShortButtons.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mScrollView = getActivity().getScrollView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("buttons should be shorter than screen", getActivity().getButtonAt(0).getHeight() < mScrollView.getHeight());
        assertTrue("should be enough buttons to have some scrolled off screen", getActivity().getLinearLayout().getHeight() > getActivity().getScrollView().getHeight());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testScrollDownToBottomThroughButtons() throws java.lang.Exception {
        final int numButtons = getActivity().getNumButtons();
        for (int i = 0; i < numButtons; i++) {
            java.lang.String prefix = (("after " + i) + " downs expected button ") + i;
            final android.widget.Button button = getActivity().getButtonAt(i);
            assertTrue(prefix + "  to have focus", button.isFocused());
            assertTrue(prefix + " to be on screen", isButtonOnScreen(button));
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        assertEquals("should be fully scrolled to bottom", getActivity().getLinearLayout().getHeight() - mScrollView.getHeight(), mScrollView.getScrollY());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testScrollFromBottomToTopThroughButtons() throws java.lang.Exception {
        final int numButtons = getActivity().getNumButtons();
        final android.widget.Button lastButton = getActivity().getButtonAt(numButtons - 1);
        lastButton.post(new java.lang.Runnable() {
            public void run() {
                lastButton.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue("lastButton.isFocused()", lastButton.isFocused());
        for (int i = numButtons - 1; i >= 0; i--) {
            java.lang.String prefix = (("after " + i) + " ups expected button ") + i;
            final android.widget.Button button = getActivity().getButtonAt(i);
            assertTrue(prefix + "  to have focus", button.isFocused());
            assertTrue(prefix + " to be on screen", isButtonOnScreen(button));
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        }
        assertEquals("should be fully scrolled to top", 0, mScrollView.getScrollY());
    }

    private android.graphics.Rect mTempRect = new android.graphics.Rect();

    protected boolean isButtonOnScreen(android.widget.Button b) {
        b.getDrawingRect(mTempRect);
        mScrollView.offsetDescendantRectToMyCoords(b, mTempRect);
        return (mTempRect.bottom >= mScrollView.getScrollY()) && (mTempRect.top <= (mScrollView.getScrollY() + mScrollView.getHeight()));
    }
}

