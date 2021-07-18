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
package android.widget.focus;


public class DescendantFocusabilityTest extends android.test.ActivityInstrumentationTestCase<android.widget.focus.DescendantFocusability> {
    private android.widget.focus.DescendantFocusability a;

    public DescendantFocusabilityTest() {
        super("com.android.frameworks.coretests", android.widget.focus.DescendantFocusability.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        a = getActivity();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertEquals(android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS, a.beforeDescendants.getDescendantFocusability());
        assertEquals(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS, a.afterDescendants.getDescendantFocusability());
        assertEquals(android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS, a.blocksDescendants.getDescendantFocusability());
        assertTrue(a.beforeDescendantsChild.isFocusable());
        assertTrue(a.afterDescendantsChild.isFocusable());
        assertTrue(a.blocksDescendantsChild.isFocusable());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testBeforeDescendants() {
        a.beforeDescendants.setFocusable(true);
        assertTrue(a.beforeDescendants.requestFocus());
        assertTrue(a.beforeDescendants.isFocused());
        a.beforeDescendants.setFocusable(false);
        a.beforeDescendants.requestFocus();
        assertTrue(a.beforeDescendantsChild.isFocused());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testAfterDescendants() {
        a.afterDescendants.setFocusable(true);
        assertTrue(a.afterDescendants.requestFocus());
        assertTrue(a.afterDescendantsChild.isFocused());
        a.afterDescendants.setFocusable(false);
        assertTrue(a.afterDescendants.requestFocus());
        assertTrue(a.afterDescendantsChild.isFocused());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testBlocksDescendants() {
        a.blocksDescendants.setFocusable(true);
        assertTrue(a.blocksDescendants.requestFocus());
        assertTrue(a.blocksDescendants.isFocused());
        assertFalse(a.blocksDescendantsChild.isFocused());
        a.blocksDescendants.setFocusable(false);
        assertFalse(a.blocksDescendants.requestFocus());
        assertFalse(a.blocksDescendants.isFocused());
        assertFalse(a.blocksDescendantsChild.isFocused());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testChildOfDescendantBlockerRequestFocusFails() {
        assertFalse(a.blocksDescendantsChild.requestFocus());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testBeforeDescendantsEnterTouchMode() {
        a.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                a.beforeDescendants.setFocusableInTouchMode(true);
                a.beforeDescendantsChild.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(a.beforeDescendantsChild.isFocused());
        assertFalse(a.beforeDescendantsChild.isInTouchMode());
        android.test.TouchUtils.clickView(this, a.beforeDescendantsChild);
        assertTrue(a.beforeDescendantsChild.isInTouchMode());
        assertFalse(a.beforeDescendants.isFocused());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testAfterDescendantsEnterTouchMode() {
        a.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                a.afterDescendants.setFocusableInTouchMode(true);
                a.afterDescendantsChild.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(a.afterDescendantsChild.isFocused());
        assertFalse(a.afterDescendantsChild.isInTouchMode());
        android.test.TouchUtils.clickView(this, a.afterDescendantsChild);
        assertTrue(a.afterDescendantsChild.isInTouchMode());
        assertTrue(a.afterDescendants.isFocused());
    }
}

