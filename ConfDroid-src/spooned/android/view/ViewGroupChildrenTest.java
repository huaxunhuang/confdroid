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
 * Exercises {@link android.view.ViewGroup}'s ability to add/remove children.
 */
public class ViewGroupChildrenTest extends android.test.ActivityInstrumentationTestCase<android.view.ViewGroupChildren> {
    private android.view.ViewGroup mGroup;

    public ViewGroupChildrenTest() {
        super("com.android.frameworks.coretests", android.view.ViewGroupChildren.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.ViewGroupChildren a = getActivity();
        mGroup = ((android.view.ViewGroup) (a.findViewById(R.id.group)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mGroup);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testStartsEmpty() throws java.lang.Exception {
        assertEquals("A ViewGroup should have no child by default", 0, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testAddChild() throws java.lang.Exception {
        android.view.View view = createView("1");
        mGroup.addView(view);
        assertEquals(1, mGroup.getChildCount());
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        android.test.ViewAsserts.assertGroupContains(mGroup, view);
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testAddChildAtFront() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        for (int i = 0; i < 24; i++) {
            android.view.View view = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(view);
        }
        android.view.View view = createView("X");
        mGroup.addView(view, 0);
        assertEquals(25, mGroup.getChildCount());
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        android.test.ViewAsserts.assertGroupContains(mGroup, view);
        assertSame(view, mGroup.getChildAt(0));
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testAddChildInMiddle() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        for (int i = 0; i < 24; i++) {
            android.view.View view = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(view);
        }
        android.view.View view = createView("X");
        mGroup.addView(view, 12);
        assertEquals(25, mGroup.getChildCount());
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        android.test.ViewAsserts.assertGroupContains(mGroup, view);
        assertSame(view, mGroup.getChildAt(12));
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testAddChildren() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        for (int i = 0; i < 24; i++) {
            android.view.View view = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(view);
            android.test.ViewAsserts.assertGroupContains(mGroup, view);
        }
        assertEquals(24, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChild() throws java.lang.Exception {
        android.view.View view = createView("1");
        mGroup.addView(view);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        mGroup.removeView(view);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        android.test.ViewAsserts.assertGroupNotContains(mGroup, view);
        assertEquals(0, mGroup.getChildCount());
        assertNull(view.getParent());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChildren() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        final android.view.View[] views = new android.view.View[24];
        for (int i = 0; i < views.length; i++) {
            views[i] = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(views[i]);
        }
        for (int i = views.length - 1; i >= 0; i--) {
            mGroup.removeViewAt(i);
            android.test.ViewAsserts.assertGroupIntegrity(mGroup);
            android.test.ViewAsserts.assertGroupNotContains(mGroup, views[i]);
            assertNull(views[i].getParent());
        }
        assertEquals(0, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChildrenBulk() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        final android.view.View[] views = new android.view.View[24];
        for (int i = 0; i < views.length; i++) {
            views[i] = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(views[i]);
        }
        mGroup.removeViews(6, 7);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        for (int i = 6; i < 13; i++) {
            android.test.ViewAsserts.assertGroupNotContains(mGroup, views[i]);
            assertNull(views[i].getParent());
        }
        assertEquals(17, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChildrenBulkAtFront() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        final android.view.View[] views = new android.view.View[24];
        for (int i = 0; i < views.length; i++) {
            views[i] = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(views[i]);
        }
        mGroup.removeViews(0, 7);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        for (int i = 0; i < 7; i++) {
            android.test.ViewAsserts.assertGroupNotContains(mGroup, views[i]);
            assertNull(views[i].getParent());
        }
        assertEquals("8", ((android.widget.TextView) (mGroup.getChildAt(0))).getText());
        assertEquals(17, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChildrenBulkAtEnd() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        final android.view.View[] views = new android.view.View[24];
        for (int i = 0; i < views.length; i++) {
            views[i] = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(views[i]);
        }
        mGroup.removeViews(17, 7);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        for (int i = 17; i < 24; i++) {
            android.test.ViewAsserts.assertGroupNotContains(mGroup, views[i]);
            assertNull(views[i].getParent());
        }
        assertEquals("17", ((android.widget.TextView) (mGroup.getChildAt(mGroup.getChildCount() - 1))).getText());
        assertEquals(17, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChildAtFront() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        final android.view.View[] views = new android.view.View[24];
        for (int i = 0; i < views.length; i++) {
            views[i] = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(views[i]);
        }
        mGroup.removeViewAt(0);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        android.test.ViewAsserts.assertGroupNotContains(mGroup, views[0]);
        assertNull(views[0].getParent());
        assertEquals(views.length - 1, mGroup.getChildCount());
    }

    @android.test.UiThreadTest
    @android.test.suitebuilder.annotation.MediumTest
    public void testRemoveChildInMiddle() throws java.lang.Exception {
        // 24 should be greater than ViewGroup.ARRAY_CAPACITY_INCREMENT
        final android.view.View[] views = new android.view.View[24];
        for (int i = 0; i < views.length; i++) {
            views[i] = createView(java.lang.String.valueOf(i + 1));
            mGroup.addView(views[i]);
        }
        mGroup.removeViewAt(12);
        android.test.ViewAsserts.assertGroupIntegrity(mGroup);
        android.test.ViewAsserts.assertGroupNotContains(mGroup, views[12]);
        assertNull(views[12].getParent());
        assertEquals(views.length - 1, mGroup.getChildCount());
    }

    private android.widget.TextView createView(java.lang.String text) {
        android.widget.TextView view = new android.widget.TextView(getActivity());
        view.setText(text);
        view.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
        return view;
    }
}

