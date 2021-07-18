/**
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.view.accessibility;


/**
 * This class exercises the caching and recycling of {@link AccessibilityEvent}s.
 */
public class RecycleAccessibilityEventTest extends junit.framework.TestCase {
    private static final java.lang.String CLASS_NAME = "foo.bar.baz.Test";

    private static final java.lang.String PACKAGE_NAME = "foo.bar.baz";

    private static final java.lang.String TEXT = "Some stuff";

    private static final java.lang.String CONTENT_DESCRIPTION = "Content description";

    private static final int ITEM_COUNT = 10;

    private static final int CURRENT_ITEM_INDEX = 1;

    private static final int FROM_INDEX = 1;

    private static final int ADDED_COUNT = 2;

    private static final int REMOVED_COUNT = 1;

    /**
     * If an {@link AccessibilityEvent} is marshaled/unmarshaled correctly
     */
    @android.test.suitebuilder.annotation.SmallTest
    public void testAccessibilityEventViewTextChangedType() {
        android.view.accessibility.AccessibilityEvent first = android.view.accessibility.AccessibilityEvent.obtain(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        junit.framework.TestCase.assertNotNull(first);
        first.setClassName(android.view.accessibility.RecycleAccessibilityEventTest.CLASS_NAME);
        first.setPackageName(android.view.accessibility.RecycleAccessibilityEventTest.PACKAGE_NAME);
        first.getText().add(android.view.accessibility.RecycleAccessibilityEventTest.TEXT);
        first.setFromIndex(android.view.accessibility.RecycleAccessibilityEventTest.FROM_INDEX);
        first.setAddedCount(android.view.accessibility.RecycleAccessibilityEventTest.ADDED_COUNT);
        first.setRemovedCount(android.view.accessibility.RecycleAccessibilityEventTest.REMOVED_COUNT);
        first.setChecked(true);
        first.setContentDescription(android.view.accessibility.RecycleAccessibilityEventTest.CONTENT_DESCRIPTION);
        first.setItemCount(android.view.accessibility.RecycleAccessibilityEventTest.ITEM_COUNT);
        first.setCurrentItemIndex(android.view.accessibility.RecycleAccessibilityEventTest.CURRENT_ITEM_INDEX);
        first.setEnabled(true);
        first.setPassword(true);
        first.recycle();
        junit.framework.TestCase.assertNotNull(first);
        junit.framework.TestCase.assertNull(first.getClassName());
        junit.framework.TestCase.assertNull(first.getPackageName());
        junit.framework.TestCase.assertEquals(0, first.getText().size());
        junit.framework.TestCase.assertFalse(first.isChecked());
        junit.framework.TestCase.assertNull(first.getContentDescription());
        junit.framework.TestCase.assertEquals(-1, first.getItemCount());
        junit.framework.TestCase.assertEquals(android.view.accessibility.AccessibilityEvent.INVALID_POSITION, first.getCurrentItemIndex());
        junit.framework.TestCase.assertFalse(first.isEnabled());
        junit.framework.TestCase.assertFalse(first.isPassword());
        junit.framework.TestCase.assertEquals(-1, first.getFromIndex());
        junit.framework.TestCase.assertEquals(-1, first.getAddedCount());
        junit.framework.TestCase.assertEquals(-1, first.getRemovedCount());
        // get another event from the pool (this must be the recycled first)
        android.view.accessibility.AccessibilityEvent second = android.view.accessibility.AccessibilityEvent.obtain();
        junit.framework.TestCase.assertEquals(first, second);
    }
}

