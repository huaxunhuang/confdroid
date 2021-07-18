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


public class TallTextAboveButtonTest extends android.test.ActivityInstrumentationTestCase<android.widget.scroll.TallTextAboveButton> {
    private android.widget.ScrollView mScrollView;

    private android.widget.TextView mTopText;

    private android.widget.TextView mBottomButton;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mScrollView = getActivity().getScrollView();
        mTopText = getActivity().getContentChildAt(0);
        mBottomButton = getActivity().getContentChildAt(1);
    }

    public TallTextAboveButtonTest() {
        super("com.android.frameworks.coretests", android.widget.scroll.TallTextAboveButton.class);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("top text should be larger than screen", mTopText.getHeight() > mScrollView.getHeight());
        assertTrue((("scroll view should have focus (because nothing else focusable " + "is on screen), but ") + getActivity().getScrollView().findFocus()) + " does instead", getActivity().getScrollView().isFocused());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGainFocusAsScrolledOntoScreen() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue("button should have scrolled onto screen", mBottomButton.getBottom() >= mScrollView.getBottom());
        assertTrue("button should have gained focus as it was scrolled completely " + "into view", mBottomButton.isFocused());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue(("scroll view should have focus, but " + getActivity().getScrollView().findFocus()) + " does instead", getActivity().getScrollView().isFocused());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testScrollingButtonOffScreenLosesFocus() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue("button should have focus", mBottomButton.isFocused());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue(("scroll view should have focus, but " + getActivity().getScrollView().findFocus()) + " does instead", getActivity().getScrollView().isFocused());
    }
}

