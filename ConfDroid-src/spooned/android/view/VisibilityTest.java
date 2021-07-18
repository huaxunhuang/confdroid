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
 * Exercises {@link android.view.View}'s ability to change visibility between
 * GONE, VISIBLE and INVISIBLE.
 */
public class VisibilityTest extends android.test.ActivityInstrumentationTestCase<android.view.Visibility> {
    private android.widget.TextView mRefUp;

    private android.widget.TextView mRefDown;

    private android.widget.TextView mVictim;

    private android.widget.Button mVisible;

    private android.widget.Button mInvisible;

    private android.widget.Button mGone;

    public VisibilityTest() {
        super("com.android.frameworks.coretests", android.view.Visibility.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.Visibility a = getActivity();
        mRefUp = ((android.widget.TextView) (a.findViewById(R.id.refUp)));
        mRefDown = ((android.widget.TextView) (a.findViewById(R.id.refDown)));
        mVictim = ((android.widget.TextView) (a.findViewById(R.id.victim)));
        mVisible = ((android.widget.Button) (a.findViewById(R.id.vis)));
        mInvisible = ((android.widget.Button) (a.findViewById(R.id.invis)));
        mGone = ((android.widget.Button) (a.findViewById(R.id.gone)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mRefUp);
        assertNotNull(mRefDown);
        assertNotNull(mVictim);
        assertNotNull(mVisible);
        assertNotNull(mInvisible);
        assertNotNull(mGone);
        assertTrue(mVisible.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testVisibleToInvisible() throws java.lang.Exception {
        sendKeys("DPAD_RIGHT");
        assertTrue(mInvisible.hasFocus());
        int oldTop = mVictim.getTop();
        sendKeys("DPAD_CENTER");
        assertEquals(android.view.View.INVISIBLE, mVictim.getVisibility());
        int newTop = mVictim.getTop();
        assertEquals(oldTop, newTop);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testVisibleToGone() throws java.lang.Exception {
        // sendKeys("2*DPAD_RIGHT");
        sendRepeatedKeys(2, android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertTrue(mGone.hasFocus());
        int oldTop = mVictim.getTop();
        sendKeys("DPAD_CENTER");
        assertEquals(android.view.View.GONE, mVictim.getVisibility());
        int refDownTop = mRefDown.getTop();
        assertEquals(oldTop, refDownTop);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testGoneToVisible() throws java.lang.Exception {
        sendKeys("2*DPAD_RIGHT");
        assertTrue(mGone.hasFocus());
        int oldTop = mVictim.getTop();
        sendKeys("DPAD_CENTER");
        assertEquals(android.view.View.GONE, mVictim.getVisibility());
        int refDownTop = mRefDown.getTop();
        assertEquals(oldTop, refDownTop);
        sendKeys("2*DPAD_LEFT DPAD_CENTER");
        assertEquals(android.view.View.VISIBLE, mVictim.getVisibility());
        int newTop = mVictim.getTop();
        assertEquals(oldTop, newTop);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoneToInvisible() throws java.lang.Exception {
        sendKeys("2*DPAD_RIGHT");
        assertTrue(mGone.hasFocus());
        int oldTop = mVictim.getTop();
        sendKeys("DPAD_CENTER");
        assertEquals(android.view.View.GONE, mVictim.getVisibility());
        int refDownTop = mRefDown.getTop();
        assertEquals(oldTop, refDownTop);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_LEFT, android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        assertEquals(android.view.View.INVISIBLE, mVictim.getVisibility());
        int newTop = mVictim.getTop();
        assertEquals(oldTop, newTop);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testInvisibleToVisible() throws java.lang.Exception {
        sendKeys("DPAD_RIGHT");
        assertTrue(mInvisible.hasFocus());
        int oldTop = mVictim.getTop();
        sendKeys("DPAD_CENTER");
        assertEquals(android.view.View.INVISIBLE, mVictim.getVisibility());
        int newTop = mVictim.getTop();
        assertEquals(oldTop, newTop);
        sendKeys("DPAD_LEFT DPAD_CENTER");
        assertEquals(android.view.View.VISIBLE, mVictim.getVisibility());
        newTop = mVictim.getTop();
        assertEquals(oldTop, newTop);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testInvisibleToGone() throws java.lang.Exception {
        sendKeys("DPAD_RIGHT");
        assertTrue(mInvisible.hasFocus());
        int oldTop = mVictim.getTop();
        sendKeys("DPAD_CENTER");
        assertEquals(android.view.View.INVISIBLE, mVictim.getVisibility());
        int newTop = mVictim.getTop();
        assertEquals(oldTop, newTop);
        sendKeys("DPAD_RIGHT DPAD_CENTER");
        assertEquals(android.view.View.GONE, mVictim.getVisibility());
        int refDownTop = mRefDown.getTop();
        assertEquals(oldTop, refDownTop);
    }
}

