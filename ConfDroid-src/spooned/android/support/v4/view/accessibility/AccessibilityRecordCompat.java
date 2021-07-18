/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.view.accessibility;


/**
 * Helper for accessing {@link android.view.accessibility.AccessibilityRecord}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public class AccessibilityRecordCompat {
    static interface AccessibilityRecordImpl {
        public java.lang.Object obtain();

        public java.lang.Object obtain(java.lang.Object record);

        public void setSource(java.lang.Object record, android.view.View source);

        public void setSource(java.lang.Object record, android.view.View root, int virtualDescendantId);

        public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getSource(java.lang.Object record);

        public int getWindowId(java.lang.Object record);

        public boolean isChecked(java.lang.Object record);

        public void setChecked(java.lang.Object record, boolean isChecked);

        public boolean isEnabled(java.lang.Object record);

        public void setEnabled(java.lang.Object record, boolean isEnabled);

        public boolean isPassword(java.lang.Object record);

        public void setPassword(java.lang.Object record, boolean isPassword);

        public boolean isFullScreen(java.lang.Object record);

        public void setFullScreen(java.lang.Object record, boolean isFullScreen);

        public boolean isScrollable(java.lang.Object record);

        public void setScrollable(java.lang.Object record, boolean scrollable);

        public int getItemCount(java.lang.Object record);

        public void setItemCount(java.lang.Object record, int itemCount);

        public int getCurrentItemIndex(java.lang.Object record);

        public void setCurrentItemIndex(java.lang.Object record, int currentItemIndex);

        public int getFromIndex(java.lang.Object record);

        public void setFromIndex(java.lang.Object record, int fromIndex);

        public int getToIndex(java.lang.Object record);

        public void setToIndex(java.lang.Object record, int toIndex);

        public int getScrollX(java.lang.Object record);

        public void setScrollX(java.lang.Object record, int scrollX);

        public int getScrollY(java.lang.Object record);

        public void setScrollY(java.lang.Object record, int scrollY);

        public int getMaxScrollX(java.lang.Object record);

        public void setMaxScrollX(java.lang.Object record, int maxScrollX);

        public int getMaxScrollY(java.lang.Object record);

        public void setMaxScrollY(java.lang.Object record, int maxScrollY);

        public int getAddedCount(java.lang.Object record);

        public void setAddedCount(java.lang.Object record, int addedCount);

        public int getRemovedCount(java.lang.Object record);

        public void setRemovedCount(java.lang.Object record, int removedCount);

        public java.lang.CharSequence getClassName(java.lang.Object record);

        public void setClassName(java.lang.Object record, java.lang.CharSequence className);

        public java.util.List<java.lang.CharSequence> getText(java.lang.Object record);

        public java.lang.CharSequence getBeforeText(java.lang.Object record);

        public void setBeforeText(java.lang.Object record, java.lang.CharSequence beforeText);

        public java.lang.CharSequence getContentDescription(java.lang.Object record);

        public void setContentDescription(java.lang.Object record, java.lang.CharSequence contentDescription);

        public android.os.Parcelable getParcelableData(java.lang.Object record);

        public void setParcelableData(java.lang.Object record, android.os.Parcelable parcelableData);

        public void recycle(java.lang.Object record);
    }

    static class AccessibilityRecordStubImpl implements android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordImpl {
        @java.lang.Override
        public java.lang.Object obtain() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtain(java.lang.Object record) {
            return null;
        }

        @java.lang.Override
        public int getAddedCount(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public java.lang.CharSequence getBeforeText(java.lang.Object record) {
            return null;
        }

        @java.lang.Override
        public java.lang.CharSequence getClassName(java.lang.Object record) {
            return null;
        }

        @java.lang.Override
        public java.lang.CharSequence getContentDescription(java.lang.Object record) {
            return null;
        }

        @java.lang.Override
        public int getCurrentItemIndex(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getFromIndex(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getItemCount(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getMaxScrollX(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getMaxScrollY(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public android.os.Parcelable getParcelableData(java.lang.Object record) {
            return null;
        }

        @java.lang.Override
        public int getRemovedCount(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getScrollX(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getScrollY(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getSource(java.lang.Object record) {
            return null;
        }

        @java.lang.Override
        public java.util.List<java.lang.CharSequence> getText(java.lang.Object record) {
            return java.util.Collections.emptyList();
        }

        @java.lang.Override
        public int getToIndex(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public int getWindowId(java.lang.Object record) {
            return 0;
        }

        @java.lang.Override
        public boolean isChecked(java.lang.Object record) {
            return false;
        }

        @java.lang.Override
        public boolean isEnabled(java.lang.Object record) {
            return false;
        }

        @java.lang.Override
        public boolean isFullScreen(java.lang.Object record) {
            return false;
        }

        @java.lang.Override
        public boolean isPassword(java.lang.Object record) {
            return false;
        }

        @java.lang.Override
        public boolean isScrollable(java.lang.Object record) {
            return false;
        }

        @java.lang.Override
        public void recycle(java.lang.Object record) {
        }

        @java.lang.Override
        public void setAddedCount(java.lang.Object record, int addedCount) {
        }

        @java.lang.Override
        public void setBeforeText(java.lang.Object record, java.lang.CharSequence beforeText) {
        }

        @java.lang.Override
        public void setChecked(java.lang.Object record, boolean isChecked) {
        }

        @java.lang.Override
        public void setClassName(java.lang.Object record, java.lang.CharSequence className) {
        }

        @java.lang.Override
        public void setContentDescription(java.lang.Object record, java.lang.CharSequence contentDescription) {
        }

        @java.lang.Override
        public void setCurrentItemIndex(java.lang.Object record, int currentItemIndex) {
        }

        @java.lang.Override
        public void setEnabled(java.lang.Object record, boolean isEnabled) {
        }

        @java.lang.Override
        public void setFromIndex(java.lang.Object record, int fromIndex) {
        }

        @java.lang.Override
        public void setFullScreen(java.lang.Object record, boolean isFullScreen) {
        }

        @java.lang.Override
        public void setItemCount(java.lang.Object record, int itemCount) {
        }

        @java.lang.Override
        public void setMaxScrollX(java.lang.Object record, int maxScrollX) {
        }

        @java.lang.Override
        public void setMaxScrollY(java.lang.Object record, int maxScrollY) {
        }

        @java.lang.Override
        public void setParcelableData(java.lang.Object record, android.os.Parcelable parcelableData) {
        }

        @java.lang.Override
        public void setPassword(java.lang.Object record, boolean isPassword) {
        }

        @java.lang.Override
        public void setRemovedCount(java.lang.Object record, int removedCount) {
        }

        @java.lang.Override
        public void setScrollX(java.lang.Object record, int scrollX) {
        }

        @java.lang.Override
        public void setScrollY(java.lang.Object record, int scrollY) {
        }

        @java.lang.Override
        public void setScrollable(java.lang.Object record, boolean scrollable) {
        }

        @java.lang.Override
        public void setSource(java.lang.Object record, android.view.View source) {
        }

        @java.lang.Override
        public void setSource(java.lang.Object record, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public void setToIndex(java.lang.Object record, int toIndex) {
        }
    }

    static class AccessibilityRecordIcsImpl extends android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordStubImpl {
        @java.lang.Override
        public java.lang.Object obtain() {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.obtain();
        }

        @java.lang.Override
        public java.lang.Object obtain(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.obtain(record);
        }

        @java.lang.Override
        public int getAddedCount(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getAddedCount(record);
        }

        @java.lang.Override
        public java.lang.CharSequence getBeforeText(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getBeforeText(record);
        }

        @java.lang.Override
        public java.lang.CharSequence getClassName(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getClassName(record);
        }

        @java.lang.Override
        public java.lang.CharSequence getContentDescription(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getContentDescription(record);
        }

        @java.lang.Override
        public int getCurrentItemIndex(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getCurrentItemIndex(record);
        }

        @java.lang.Override
        public int getFromIndex(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getFromIndex(record);
        }

        @java.lang.Override
        public int getItemCount(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getItemCount(record);
        }

        @java.lang.Override
        public android.os.Parcelable getParcelableData(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getParcelableData(record);
        }

        @java.lang.Override
        public int getRemovedCount(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getRemovedCount(record);
        }

        @java.lang.Override
        public int getScrollX(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getScrollX(record);
        }

        @java.lang.Override
        public int getScrollY(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getScrollY(record);
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getSource(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getSource(record));
        }

        @java.lang.Override
        public java.util.List<java.lang.CharSequence> getText(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getText(record);
        }

        @java.lang.Override
        public int getToIndex(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getToIndex(record);
        }

        @java.lang.Override
        public int getWindowId(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.getWindowId(record);
        }

        @java.lang.Override
        public boolean isChecked(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.isChecked(record);
        }

        @java.lang.Override
        public boolean isEnabled(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.isEnabled(record);
        }

        @java.lang.Override
        public boolean isFullScreen(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.isFullScreen(record);
        }

        @java.lang.Override
        public boolean isPassword(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.isPassword(record);
        }

        @java.lang.Override
        public boolean isScrollable(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.isScrollable(record);
        }

        @java.lang.Override
        public void recycle(java.lang.Object record) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.recycle(record);
        }

        @java.lang.Override
        public void setAddedCount(java.lang.Object record, int addedCount) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setAddedCount(record, addedCount);
        }

        @java.lang.Override
        public void setBeforeText(java.lang.Object record, java.lang.CharSequence beforeText) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setBeforeText(record, beforeText);
        }

        @java.lang.Override
        public void setChecked(java.lang.Object record, boolean isChecked) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setChecked(record, isChecked);
        }

        @java.lang.Override
        public void setClassName(java.lang.Object record, java.lang.CharSequence className) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setClassName(record, className);
        }

        @java.lang.Override
        public void setContentDescription(java.lang.Object record, java.lang.CharSequence contentDescription) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setContentDescription(record, contentDescription);
        }

        @java.lang.Override
        public void setCurrentItemIndex(java.lang.Object record, int currentItemIndex) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setCurrentItemIndex(record, currentItemIndex);
        }

        @java.lang.Override
        public void setEnabled(java.lang.Object record, boolean isEnabled) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setEnabled(record, isEnabled);
        }

        @java.lang.Override
        public void setFromIndex(java.lang.Object record, int fromIndex) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setFromIndex(record, fromIndex);
        }

        @java.lang.Override
        public void setFullScreen(java.lang.Object record, boolean isFullScreen) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setFullScreen(record, isFullScreen);
        }

        @java.lang.Override
        public void setItemCount(java.lang.Object record, int itemCount) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setItemCount(record, itemCount);
        }

        @java.lang.Override
        public void setParcelableData(java.lang.Object record, android.os.Parcelable parcelableData) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setParcelableData(record, parcelableData);
        }

        @java.lang.Override
        public void setPassword(java.lang.Object record, boolean isPassword) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setPassword(record, isPassword);
        }

        @java.lang.Override
        public void setRemovedCount(java.lang.Object record, int removedCount) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setRemovedCount(record, removedCount);
        }

        @java.lang.Override
        public void setScrollX(java.lang.Object record, int scrollX) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setScrollX(record, scrollX);
        }

        @java.lang.Override
        public void setScrollY(java.lang.Object record, int scrollY) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setScrollY(record, scrollY);
        }

        @java.lang.Override
        public void setScrollable(java.lang.Object record, boolean scrollable) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setScrollable(record, scrollable);
        }

        @java.lang.Override
        public void setSource(java.lang.Object record, android.view.View source) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setSource(record, source);
        }

        @java.lang.Override
        public void setToIndex(java.lang.Object record, int toIndex) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcs.setToIndex(record, toIndex);
        }
    }

    static class AccessibilityRecordIcsMr1Impl extends android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordIcsImpl {
        @java.lang.Override
        public int getMaxScrollX(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcsMr1.getMaxScrollX(record);
        }

        @java.lang.Override
        public int getMaxScrollY(java.lang.Object record) {
            return android.support.v4.view.accessibility.AccessibilityRecordCompatIcsMr1.getMaxScrollY(record);
        }

        @java.lang.Override
        public void setMaxScrollX(java.lang.Object record, int maxScrollX) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcsMr1.setMaxScrollX(record, maxScrollX);
        }

        @java.lang.Override
        public void setMaxScrollY(java.lang.Object record, int maxScrollY) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatIcsMr1.setMaxScrollY(record, maxScrollY);
        }
    }

    static class AccessibilityRecordJellyBeanImpl extends android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordIcsMr1Impl {
        @java.lang.Override
        public void setSource(java.lang.Object record, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityRecordCompatJellyBean.setSource(record, root, virtualDescendantId);
        }
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            // JellyBean
            IMPL = new android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordJellyBeanImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 15) {
                // ICS MR1
                IMPL = new android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordIcsMr1Impl();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    // ICS
                    IMPL = new android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordIcsImpl();
                } else {
                    IMPL = new android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordStubImpl();
                }


    }

    private static final android.support.v4.view.accessibility.AccessibilityRecordCompat.AccessibilityRecordImpl IMPL;

    private final java.lang.Object mRecord;

    /**
     *
     *
     * @deprecated This is not type safe. If you want to modify an
    {@link AccessibilityEvent}'s properties defined in
    {@link android.view.accessibility.AccessibilityRecord} use
    {@link AccessibilityEventCompat#asRecord(AccessibilityEvent)}. This method will be removed
    in a subsequent release of the support library.
     */
    @java.lang.Deprecated
    public AccessibilityRecordCompat(java.lang.Object record) {
        mRecord = record;
    }

    /**
     *
     *
     * @return The wrapped implementation.
     * @deprecated This method will be removed in a subsequent release of
    the support library.
     */
    @java.lang.Deprecated
    public java.lang.Object getImpl() {
        return mRecord;
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * instantiated. The instance is initialized with data from the
     * given record.
     *
     * @return An instance.
     */
    public static android.support.v4.view.accessibility.AccessibilityRecordCompat obtain(android.support.v4.view.accessibility.AccessibilityRecordCompat record) {
        return new android.support.v4.view.accessibility.AccessibilityRecordCompat(android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.obtain(record.mRecord));
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * instantiated.
     *
     * @return An instance.
     */
    public static android.support.v4.view.accessibility.AccessibilityRecordCompat obtain() {
        return new android.support.v4.view.accessibility.AccessibilityRecordCompat(android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.obtain());
    }

    /**
     * Sets the event source.
     *
     * @param source
     * 		The source.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setSource(android.view.View source) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setSource(mRecord, source);
    }

    /**
     * Sets the source to be a virtual descendant of the given <code>root</code>.
     * If <code>virtualDescendantId</code> equals to {@link View#NO_ID} the root
     * is set as the source.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report them selves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setSource(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setSource(mRecord, root, virtualDescendantId);
    }

    /**
     * Gets the {@link android.view.accessibility.AccessibilityNodeInfo} of
     * the event source.
     * <p>
     * <strong>Note:</strong> It is a client responsibility to recycle the
     * received info by calling
     * {@link android.view.accessibility.AccessibilityNodeInfo#recycle()
     * AccessibilityNodeInfo#recycle()} to avoid creating of multiple instances.
     * </p>
     *
     * @return The info of the source.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getSource() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getSource(mRecord);
    }

    /**
     * Gets the id of the window from which the event comes from.
     *
     * @return The window id.
     */
    public int getWindowId() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getWindowId(mRecord);
    }

    /**
     * Gets if the source is checked.
     *
     * @return True if the view is checked, false otherwise.
     */
    public boolean isChecked() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.isChecked(mRecord);
    }

    /**
     * Sets if the source is checked.
     *
     * @param isChecked
     * 		True if the view is checked, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setChecked(boolean isChecked) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setChecked(mRecord, isChecked);
    }

    /**
     * Gets if the source is enabled.
     *
     * @return True if the view is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.isEnabled(mRecord);
    }

    /**
     * Sets if the source is enabled.
     *
     * @param isEnabled
     * 		True if the view is enabled, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setEnabled(boolean isEnabled) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setEnabled(mRecord, isEnabled);
    }

    /**
     * Gets if the source is a password field.
     *
     * @return True if the view is a password field, false otherwise.
     */
    public boolean isPassword() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.isPassword(mRecord);
    }

    /**
     * Sets if the source is a password field.
     *
     * @param isPassword
     * 		True if the view is a password field, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setPassword(boolean isPassword) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setPassword(mRecord, isPassword);
    }

    /**
     * Gets if the source is taking the entire screen.
     *
     * @return True if the source is full screen, false otherwise.
     */
    public boolean isFullScreen() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.isFullScreen(mRecord);
    }

    /**
     * Sets if the source is taking the entire screen.
     *
     * @param isFullScreen
     * 		True if the source is full screen, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setFullScreen(boolean isFullScreen) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setFullScreen(mRecord, isFullScreen);
    }

    /**
     * Gets if the source is scrollable.
     *
     * @return True if the source is scrollable, false otherwise.
     */
    public boolean isScrollable() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.isScrollable(mRecord);
    }

    /**
     * Sets if the source is scrollable.
     *
     * @param scrollable
     * 		True if the source is scrollable, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setScrollable(boolean scrollable) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setScrollable(mRecord, scrollable);
    }

    /**
     * Gets the number of items that can be visited.
     *
     * @return The number of items.
     */
    public int getItemCount() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getItemCount(mRecord);
    }

    /**
     * Sets the number of items that can be visited.
     *
     * @param itemCount
     * 		The number of items.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setItemCount(int itemCount) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setItemCount(mRecord, itemCount);
    }

    /**
     * Gets the index of the source in the list of items the can be visited.
     *
     * @return The current item index.
     */
    public int getCurrentItemIndex() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getCurrentItemIndex(mRecord);
    }

    /**
     * Sets the index of the source in the list of items that can be visited.
     *
     * @param currentItemIndex
     * 		The current item index.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setCurrentItemIndex(int currentItemIndex) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setCurrentItemIndex(mRecord, currentItemIndex);
    }

    /**
     * Gets the index of the first character of the changed sequence,
     * or the beginning of a text selection or the index of the first
     * visible item when scrolling.
     *
     * @return The index of the first character or selection
    start or the first visible item.
     */
    public int getFromIndex() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getFromIndex(mRecord);
    }

    /**
     * Sets the index of the first character of the changed sequence
     * or the beginning of a text selection or the index of the first
     * visible item when scrolling.
     *
     * @param fromIndex
     * 		The index of the first character or selection
     * 		start or the first visible item.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setFromIndex(int fromIndex) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setFromIndex(mRecord, fromIndex);
    }

    /**
     * Gets the index of text selection end or the index of the last
     * visible item when scrolling.
     *
     * @return The index of selection end or last item index.
     */
    public int getToIndex() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getToIndex(mRecord);
    }

    /**
     * Sets the index of text selection end or the index of the last
     * visible item when scrolling.
     *
     * @param toIndex
     * 		The index of selection end or last item index.
     */
    public void setToIndex(int toIndex) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setToIndex(mRecord, toIndex);
    }

    /**
     * Gets the scroll offset of the source left edge in pixels.
     *
     * @return The scroll.
     */
    public int getScrollX() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getScrollX(mRecord);
    }

    /**
     * Sets the scroll offset of the source left edge in pixels.
     *
     * @param scrollX
     * 		The scroll.
     */
    public void setScrollX(int scrollX) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setScrollX(mRecord, scrollX);
    }

    /**
     * Gets the scroll offset of the source top edge in pixels.
     *
     * @return The scroll.
     */
    public int getScrollY() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getScrollY(mRecord);
    }

    /**
     * Sets the scroll offset of the source top edge in pixels.
     *
     * @param scrollY
     * 		The scroll.
     */
    public void setScrollY(int scrollY) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setScrollY(mRecord, scrollY);
    }

    /**
     * Gets the max scroll offset of the source left edge in pixels.
     *
     * @return The max scroll.
     */
    public int getMaxScrollX() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getMaxScrollX(mRecord);
    }

    /**
     * Sets the max scroll offset of the source left edge in pixels.
     *
     * @param maxScrollX
     * 		The max scroll.
     */
    public void setMaxScrollX(int maxScrollX) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setMaxScrollX(mRecord, maxScrollX);
    }

    /**
     * Gets the max scroll offset of the source top edge in pixels.
     *
     * @return The max scroll.
     */
    public int getMaxScrollY() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getMaxScrollY(mRecord);
    }

    /**
     * Sets the max scroll offset of the source top edge in pixels.
     *
     * @param maxScrollY
     * 		The max scroll.
     */
    public void setMaxScrollY(int maxScrollY) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setMaxScrollY(mRecord, maxScrollY);
    }

    /**
     * Gets the number of added characters.
     *
     * @return The number of added characters.
     */
    public int getAddedCount() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getAddedCount(mRecord);
    }

    /**
     * Sets the number of added characters.
     *
     * @param addedCount
     * 		The number of added characters.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setAddedCount(int addedCount) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setAddedCount(mRecord, addedCount);
    }

    /**
     * Gets the number of removed characters.
     *
     * @return The number of removed characters.
     */
    public int getRemovedCount() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getRemovedCount(mRecord);
    }

    /**
     * Sets the number of removed characters.
     *
     * @param removedCount
     * 		The number of removed characters.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setRemovedCount(int removedCount) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setRemovedCount(mRecord, removedCount);
    }

    /**
     * Gets the class name of the source.
     *
     * @return The class name.
     */
    public java.lang.CharSequence getClassName() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getClassName(mRecord);
    }

    /**
     * Sets the class name of the source.
     *
     * @param className
     * 		The lass name.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setClassName(java.lang.CharSequence className) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setClassName(mRecord, className);
    }

    /**
     * Gets the text of the event. The index in the list represents the priority
     * of the text. Specifically, the lower the index the higher the priority.
     *
     * @return The text.
     */
    public java.util.List<java.lang.CharSequence> getText() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getText(mRecord);
    }

    /**
     * Sets the text before a change.
     *
     * @return The text before the change.
     */
    public java.lang.CharSequence getBeforeText() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getBeforeText(mRecord);
    }

    /**
     * Sets the text before a change.
     *
     * @param beforeText
     * 		The text before the change.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setBeforeText(java.lang.CharSequence beforeText) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setBeforeText(mRecord, beforeText);
    }

    /**
     * Gets the description of the source.
     *
     * @return The description.
     */
    public java.lang.CharSequence getContentDescription() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getContentDescription(mRecord);
    }

    /**
     * Sets the description of the source.
     *
     * @param contentDescription
     * 		The description.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setContentDescription(java.lang.CharSequence contentDescription) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setContentDescription(mRecord, contentDescription);
    }

    /**
     * Gets the {@link Parcelable} data.
     *
     * @return The parcelable data.
     */
    public android.os.Parcelable getParcelableData() {
        return android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.getParcelableData(mRecord);
    }

    /**
     * Sets the {@link Parcelable} data of the event.
     *
     * @param parcelableData
     * 		The parcelable data.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setParcelableData(android.os.Parcelable parcelableData) {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.setParcelableData(mRecord, parcelableData);
    }

    /**
     * Return an instance back to be reused.
     * <p>
     * <strong>Note:</strong> You must not touch the object after calling this
     * function.
     * </p>
     *
     * @throws IllegalStateException
     * 		If the record is already recycled.
     */
    public void recycle() {
        android.support.v4.view.accessibility.AccessibilityRecordCompat.IMPL.recycle(mRecord);
    }

    @java.lang.Override
    public int hashCode() {
        return mRecord == null ? 0 : mRecord.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.support.v4.view.accessibility.AccessibilityRecordCompat other = ((android.support.v4.view.accessibility.AccessibilityRecordCompat) (obj));
        if (mRecord == null) {
            if (other.mRecord != null) {
                return false;
            }
        } else
            if (!mRecord.equals(other.mRecord)) {
                return false;
            }

        return true;
    }
}

