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
public class VisibilityCallbackTest extends android.test.ActivityInstrumentationTestCase2<android.view.VisibilityCallback> {
    private android.widget.TextView mRefUp;

    private android.widget.TextView mRefDown;

    private android.view.VisibilityCallback.MonitoredTextView mVictim;

    private android.view.ViewGroup mParent;

    private android.widget.Button mVisible;

    private android.widget.Button mInvisible;

    private android.widget.Button mGone;

    public VisibilityCallbackTest() {
        super("com.android.frameworks.coretests", android.view.VisibilityCallback.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.VisibilityCallback a = getActivity();
        mRefUp = ((android.widget.TextView) (a.findViewById(R.id.refUp)));
        mRefDown = ((android.widget.TextView) (a.findViewById(R.id.refDown)));
        mVictim = ((android.view.VisibilityCallback.MonitoredTextView) (a.findViewById(R.id.victim)));
        mParent = ((android.view.ViewGroup) (a.findViewById(R.id.parent)));
        mVisible = ((android.widget.Button) (a.findViewById(R.id.vis)));
        mInvisible = ((android.widget.Button) (a.findViewById(R.id.invis)));
        mGone = ((android.widget.Button) (a.findViewById(R.id.gone)));
        mVictim.post(new java.lang.Runnable() {
            public void run() {
                mVictim.setVisibility(android.view.View.INVISIBLE);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mRefUp);
        assertNotNull(mRefDown);
        assertNotNull(mVictim);
        assertNotNull(mVisible);
        assertNotNull(mInvisible);
        assertNotNull(mGone);
        assertTrue(mVisible.hasFocus());
        assertEquals(android.view.View.INVISIBLE, mVictim.getVisibility());
        assertEquals(android.view.View.VISIBLE, mParent.getVisibility());
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testDirect() throws java.lang.Exception {
        mVictim.setVisibility(android.view.View.VISIBLE);
        assertEquals(android.view.View.VISIBLE, mVictim.getLastChangedVisibility());
        assertEquals(mVictim, mVictim.getLastVisChangedView());
        mVictim.setVisibility(android.view.View.INVISIBLE);
        assertEquals(android.view.View.INVISIBLE, mVictim.getLastChangedVisibility());
        assertEquals(mVictim, mVictim.getLastVisChangedView());
        mVictim.setVisibility(android.view.View.GONE);
        assertEquals(android.view.View.GONE, mVictim.getLastChangedVisibility());
        assertEquals(mVictim, mVictim.getLastVisChangedView());
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testChild() throws java.lang.Exception {
        mParent.setVisibility(android.view.View.INVISIBLE);
        assertEquals(android.view.View.INVISIBLE, mVictim.getLastChangedVisibility());
        assertEquals(mParent, mVictim.getLastVisChangedView());
        mParent.setVisibility(android.view.View.GONE);
        assertEquals(android.view.View.GONE, mVictim.getLastChangedVisibility());
        assertEquals(mParent, mVictim.getLastVisChangedView());
        mParent.setVisibility(android.view.View.VISIBLE);
        assertEquals(android.view.View.VISIBLE, mVictim.getLastChangedVisibility());
        assertEquals(mParent, mVictim.getLastVisChangedView());
    }
}

