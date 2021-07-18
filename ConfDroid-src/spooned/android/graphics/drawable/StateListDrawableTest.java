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
package android.graphics.drawable;


/**
 * Tests for StateListDrawable
 */
public class StateListDrawableTest extends junit.framework.TestCase {
    private android.graphics.drawable.StateListDrawable slDrawable;

    private android.graphics.drawable.StateListDrawableTest.MockDrawable mockFocusedDrawable;

    private android.graphics.drawable.StateListDrawableTest.MockDrawable mockCheckedDrawable;

    private android.view.MockView mockView;

    private android.graphics.drawable.StateListDrawableTest.MockDrawable mockDefaultDrawable;

    // Re-enable tests when we are running in the framework-test directory which allows
    // access to package private access for MockView
    public void broken_testFocusScenarioSetStringWildcardFirst() throws java.lang.Exception {
        int[] focusedStateSet = new int[]{ R.attr.state_focused };
        int[] checkedStateSet = new int[]{ R.attr.state_checked };
        slDrawable.addState(StateSet.WILD_CARD, mockDefaultDrawable);
        slDrawable.addState(checkedStateSet, mockCheckedDrawable);
        slDrawable.addState(focusedStateSet, mockFocusedDrawable);
        mockView.requestFocus();
        mockView.getBackground().draw(null);
        junit.framework.TestCase.assertTrue(mockDefaultDrawable.wasDrawn);
    }

    public void broken_testFocusScenarioStateSetWildcardLast() throws java.lang.Exception {
        int[] focusedStateSet = new int[]{ R.attr.state_focused };
        int[] checkedStateSet = new int[]{ R.attr.state_checked };
        slDrawable.addState(checkedStateSet, mockCheckedDrawable);
        slDrawable.addState(focusedStateSet, mockFocusedDrawable);
        slDrawable.addState(StateSet.WILD_CARD, mockDefaultDrawable);
        mockView.requestFocus();
        mockView.getBackground().draw(null);
        junit.framework.TestCase.assertTrue(mockFocusedDrawable.wasDrawn);
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        slDrawable = new android.graphics.drawable.StateListDrawable();
        mockFocusedDrawable = new android.graphics.drawable.StateListDrawableTest.MockDrawable();
        mockCheckedDrawable = new android.graphics.drawable.StateListDrawableTest.MockDrawable();
        mockDefaultDrawable = new android.graphics.drawable.StateListDrawableTest.MockDrawable();
        mockView = new android.view.MockView();
        mockView.setBackgroundDrawable(slDrawable);
    }

    static class MockDrawable extends android.graphics.drawable.Drawable {
        public boolean wasDrawn = false;

        public void draw(android.graphics.Canvas canvas) {
            wasDrawn = true;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(android.graphics.ColorFilter cf) {
        }

        public int getOpacity() {
            return android.graphics.PixelFormat.UNKNOWN;
        }
    }
}

