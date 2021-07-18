/**
 * Copyright (C) 2006 The Android Open Source Project
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


public class MenuTest extends android.test.AndroidTestCase {
    private com.android.internal.view.menu.MenuBuilder mMenu;

    public void setUp() throws java.lang.Exception {
        setUp();
        mMenu = new com.android.internal.view.menu.MenuBuilder(super.getContext());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testItemId() {
        final int id = 512;
        final android.view.MenuItem item = mMenu.add(0, id, 0, "test");
        junit.framework.Assert.assertEquals(id, item.getItemId());
        junit.framework.Assert.assertEquals(item, mMenu.findItem(id));
        junit.framework.Assert.assertEquals(0, mMenu.findItemIndex(id));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGroupId() {
        final int groupId = 541;
        final int item1Index = 1;
        final int item2Index = 3;
        mMenu.add(0, 0, item1Index - 1, "ignore");
        final android.view.MenuItem item = mMenu.add(groupId, 0, item1Index, "test");
        mMenu.add(0, 0, item2Index - 1, "ignore");
        final android.view.MenuItem item2 = mMenu.add(groupId, 0, item2Index, "test2");
        junit.framework.Assert.assertEquals(groupId, item.getGroupId());
        junit.framework.Assert.assertEquals(groupId, item2.getGroupId());
        junit.framework.Assert.assertEquals(item1Index, mMenu.findGroupIndex(groupId));
        junit.framework.Assert.assertEquals(item2Index, mMenu.findGroupIndex(groupId, item1Index + 1));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testGroup() {
        // This test does the following
        // 1. Create a grouped item in the menu
        // 2. Check that findGroupIndex() finds the grouped item.
        // 3. Check that findGroupIndex() doesn't find a non-existent group.
        final int GROUP_ONE = android.view.Menu.FIRST;
        final int GROUP_TWO = android.view.Menu.FIRST + 1;
        mMenu.add(GROUP_ONE, 0, 0, "Menu text");
        junit.framework.Assert.assertEquals(mMenu.findGroupIndex(GROUP_ONE), 0);
        junit.framework.Assert.assertEquals(mMenu.findGroupIndex(GROUP_TWO), -1);
        // TODO: expand this test case to do multiple groups,
        // adding and removing, hiding and showing, etc.
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsShortcutWithAlpha() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', 'a');
        junit.framework.Assert.assertTrue(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, 0)));
        junit.framework.Assert.assertFalse(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_B, makeKeyEvent(android.view.KeyEvent.KEYCODE_B, 0)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsShortcutWithNumeric() throws java.lang.Exception {
        mMenu.setQwertyMode(false);
        mMenu.add(0, 0, 0, "test").setShortcut('2', 'a');
        junit.framework.Assert.assertTrue(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_2, makeKeyEvent(android.view.KeyEvent.KEYCODE_2, 0)));
        junit.framework.Assert.assertFalse(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, 0)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsShortcutWithAlt() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', 'a');
        junit.framework.Assert.assertTrue(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, android.view.KeyEvent.META_ALT_ON)));
        junit.framework.Assert.assertFalse(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, android.view.KeyEvent.META_SYM_ON)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsNotShortcutWithShift() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', 'a');
        junit.framework.Assert.assertFalse(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, android.view.KeyEvent.META_SHIFT_ON)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsNotShortcutWithSym() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', 'a');
        junit.framework.Assert.assertFalse(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, android.view.KeyEvent.META_SYM_ON)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsShortcutWithUpperCaseAlpha() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', 'A');
        junit.framework.Assert.assertTrue(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_A, makeKeyEvent(android.view.KeyEvent.KEYCODE_A, 0)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsShortcutWithBackspace() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', '\b');
        junit.framework.Assert.assertTrue(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_DEL, makeKeyEvent(android.view.KeyEvent.KEYCODE_DEL, 0)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testIsShortcutWithNewline() throws java.lang.Exception {
        mMenu.setQwertyMode(true);
        mMenu.add(0, 0, 0, "test").setShortcut('2', '\n');
        junit.framework.Assert.assertTrue(mMenu.isShortcutKey(android.view.KeyEvent.KEYCODE_ENTER, makeKeyEvent(android.view.KeyEvent.KEYCODE_ENTER, 0)));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testOrder() {
        final java.lang.String a = "a";
        final java.lang.String b = "b";
        final java.lang.String c = "c";
        final int firstOrder = 7;
        final int midOrder = 8;
        final int lastOrder = 9;
        mMenu.add(0, 0, lastOrder, c);
        mMenu.add(0, 0, firstOrder, a);
        mMenu.add(0, 0, midOrder, b);
        junit.framework.Assert.assertEquals(firstOrder, mMenu.getItem(0).getOrder());
        junit.framework.Assert.assertEquals(a, mMenu.getItem(0).getTitle());
        junit.framework.Assert.assertEquals(midOrder, mMenu.getItem(1).getOrder());
        junit.framework.Assert.assertEquals(b, mMenu.getItem(1).getTitle());
        junit.framework.Assert.assertEquals(lastOrder, mMenu.getItem(2).getOrder());
        junit.framework.Assert.assertEquals(c, mMenu.getItem(2).getTitle());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testTitle() {
        final java.lang.String title = "test";
        final android.view.MenuItem stringItem = mMenu.add(title);
        final android.view.MenuItem resItem = mMenu.add(R.string.menu_test);
        junit.framework.Assert.assertEquals(title, stringItem.getTitle());
        junit.framework.Assert.assertEquals(getContext().getResources().getString(R.string.menu_test), resItem.getTitle());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCheckable() {
        final int groupId = 1;
        final android.view.MenuItem item1 = mMenu.add(groupId, 1, 0, "item1");
        final android.view.MenuItem item2 = mMenu.add(groupId, 2, 0, "item2");
        // Set to exclusive
        mMenu.setGroupCheckable(groupId, true, true);
        junit.framework.Assert.assertTrue("Item was not set to checkable", item1.isCheckable());
        item1.setChecked(true);
        junit.framework.Assert.assertTrue("Item did not get checked", item1.isChecked());
        junit.framework.Assert.assertFalse("Item was not unchecked due to exclusive checkable", item2.isChecked());
        mMenu.findItem(2).setChecked(true);
        junit.framework.Assert.assertTrue("Item did not get checked", item2.isChecked());
        junit.framework.Assert.assertFalse("Item was not unchecked due to exclusive checkable", item1.isChecked());
        // Multiple non-exlusive checkable items
        mMenu.setGroupCheckable(groupId, true, false);
        junit.framework.Assert.assertTrue("Item was not set to checkable", item1.isCheckable());
        item1.setChecked(false);
        junit.framework.Assert.assertFalse("Item did not get unchecked", item1.isChecked());
        item1.setChecked(true);
        junit.framework.Assert.assertTrue("Item did not get checked", item1.isChecked());
        mMenu.findItem(2).setChecked(true);
        junit.framework.Assert.assertTrue("Item did not get checked", item2.isChecked());
        junit.framework.Assert.assertTrue("Item was unchecked when it shouldnt have been", item1.isChecked());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testVisibility() {
        final android.view.MenuItem item1 = mMenu.add(0, 1, 0, "item1");
        final android.view.MenuItem item2 = mMenu.add(0, 2, 0, "item2");
        // Should start as visible
        junit.framework.Assert.assertTrue("Item did not start as visible", item1.isVisible());
        junit.framework.Assert.assertTrue("Item did not start as visible", item2.isVisible());
        // Hide
        item1.setVisible(false);
        junit.framework.Assert.assertFalse("Item did not become invisible", item1.isVisible());
        mMenu.findItem(2).setVisible(false);
        junit.framework.Assert.assertFalse("Item did not become invisible", item2.isVisible());
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testSubMenu() {
        final android.view.SubMenu subMenu = mMenu.addSubMenu(0, 0, 0, "submenu");
        final android.view.MenuItem subMenuItem = subMenu.getItem();
        final android.view.MenuItem item1 = subMenu.add(0, 1, 0, "item1");
        final android.view.MenuItem item2 = subMenu.add(0, 2, 0, "item2");
        // findItem should recurse into submenus
        junit.framework.Assert.assertEquals(item1, mMenu.findItem(1));
        junit.framework.Assert.assertEquals(item2, mMenu.findItem(2));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testRemove() {
        final int groupId = 1;
        final android.view.MenuItem item1 = mMenu.add(groupId, 1, 0, "item1");
        final android.view.MenuItem item2 = mMenu.add(groupId, 2, 0, "item2");
        final android.view.MenuItem item3 = mMenu.add(groupId, 3, 0, "item3");
        final android.view.MenuItem item4 = mMenu.add(groupId, 4, 0, "item4");
        final android.view.MenuItem item5 = mMenu.add(groupId, 5, 0, "item5");
        final android.view.MenuItem item6 = mMenu.add(0, 6, 0, "item6");
        junit.framework.Assert.assertEquals(item1, mMenu.findItem(1));
        mMenu.removeItemAt(0);
        junit.framework.Assert.assertNull(mMenu.findItem(1));
        junit.framework.Assert.assertEquals(item2, mMenu.findItem(2));
        mMenu.removeItem(2);
        junit.framework.Assert.assertNull(mMenu.findItem(2));
        junit.framework.Assert.assertEquals(item3, mMenu.findItem(3));
        junit.framework.Assert.assertEquals(item4, mMenu.findItem(4));
        junit.framework.Assert.assertEquals(item5, mMenu.findItem(5));
        mMenu.removeGroup(groupId);
        junit.framework.Assert.assertNull(mMenu.findItem(3));
        junit.framework.Assert.assertNull(mMenu.findItem(4));
        junit.framework.Assert.assertNull(mMenu.findItem(5));
        junit.framework.Assert.assertEquals(item6, mMenu.findItem(6));
        mMenu.clear();
        junit.framework.Assert.assertNull(mMenu.findItem(6));
    }

    private android.view.KeyEvent makeKeyEvent(int keyCode, int metaState) {
        return new android.view.KeyEvent(0L, 0L, android.view.KeyEvent.ACTION_DOWN, keyCode, 0, metaState);
    }
}

