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
package android.widget.focus;


/**
 * Tests focus searching between buttons within a grid that are touching, for example,
 * two buttons next two each other would have the left button's right equal to the
 * right button's left.  Same goes for top and bottom edges.
 *
 * This exercises some edge cases of {@link android.view.FocusFinder}.
 */
public class LinearLayoutGridTest extends android.test.SingleLaunchActivityTestCase<android.widget.focus.LinearLayoutGrid> {
    private android.view.ViewGroup mRootView;

    public LinearLayoutGridTest() {
        super("com.android.frameworks.coretests", android.widget.focus.LinearLayoutGrid.class);
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mRootView = getActivity().getRootView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoDownFromMiddle() {
        assertEquals(getActivity().getButtonAt(2, 1), android.view.FocusFinder.getInstance().findNextFocus(mRootView, getActivity().getButtonAt(1, 1), android.view.View.FOCUS_DOWN));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoUpFromMiddle() {
        assertEquals(getActivity().getButtonAt(0, 1), android.view.FocusFinder.getInstance().findNextFocus(mRootView, getActivity().getButtonAt(1, 1), android.view.View.FOCUS_UP));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoRightFromMiddle() {
        assertEquals(getActivity().getButtonAt(1, 2), android.view.FocusFinder.getInstance().findNextFocus(mRootView, getActivity().getButtonAt(1, 1), android.view.View.FOCUS_RIGHT));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoLeftFromMiddle() {
        assertEquals(getActivity().getButtonAt(1, 0), android.view.FocusFinder.getInstance().findNextFocus(mRootView, getActivity().getButtonAt(1, 1), android.view.View.FOCUS_LEFT));
    }
}

