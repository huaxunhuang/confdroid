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
package android.view.accessibility;


/**
 * Represents a record in an {@link AccessibilityEvent} and contains information
 * about state change of its source {@link android.view.View}. When a view fires
 * an accessibility event it requests from its parent to dispatch the
 * constructed event. The parent may optionally append a record for itself
 * for providing more context to
 * {@link android.accessibilityservice.AccessibilityService}s. Hence,
 * accessibility services can facilitate additional accessibility records
 * to enhance feedback.
 * </p>
 * <p>
 * Once the accessibility event containing a record is dispatched the record is
 * made immutable and calling a state mutation method generates an error.
 * </p>
 * <p>
 * <strong>Note:</strong> Not all properties are applicable to all accessibility
 * event types. For detailed information please refer to {@link AccessibilityEvent}.
 * </p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating and processing AccessibilityRecords, read the
 * <a href="{@docRoot }guide/topics/ui/accessibility/index.html">Accessibility</a>
 * developer guide.</p>
 * </div>
 *
 * @see AccessibilityEvent
 * @see AccessibilityManager
 * @see android.accessibilityservice.AccessibilityService
 * @see AccessibilityNodeInfo
 */
public class AccessibilityRecord {
    /**
     *
     *
     * @unknown 
     */
    protected static final boolean DEBUG_CONCISE_TOSTRING = false;

    private static final int UNDEFINED = -1;

    private static final int PROPERTY_CHECKED = 0x1;

    private static final int PROPERTY_ENABLED = 0x2;

    private static final int PROPERTY_PASSWORD = 0x4;

    private static final int PROPERTY_FULL_SCREEN = 0x80;

    private static final int PROPERTY_SCROLLABLE = 0x100;

    private static final int PROPERTY_IMPORTANT_FOR_ACCESSIBILITY = 0x200;

    private static final int GET_SOURCE_PREFETCH_FLAGS = (android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_PREDECESSORS | android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_SIBLINGS) | android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS;

    // Housekeeping
    private static final int MAX_POOL_SIZE = 10;

    private static final java.lang.Object sPoolLock = new java.lang.Object();

    private static android.view.accessibility.AccessibilityRecord sPool;

    private static int sPoolSize;

    private android.view.accessibility.AccessibilityRecord mNext;

    private boolean mIsInPool;

    @android.annotation.UnsupportedAppUsage
    boolean mSealed;

    int mBooleanProperties = 0;

    int mCurrentItemIndex = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mItemCount = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mFromIndex = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mToIndex = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mScrollX = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mScrollY = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mScrollDeltaX = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mScrollDeltaY = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mMaxScrollX = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mMaxScrollY = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mAddedCount = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    int mRemovedCount = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    @android.annotation.UnsupportedAppUsage
    long mSourceNodeId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    int mSourceWindowId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    java.lang.CharSequence mClassName;

    java.lang.CharSequence mContentDescription;

    java.lang.CharSequence mBeforeText;

    android.os.Parcelable mParcelableData;

    final java.util.List<java.lang.CharSequence> mText = new java.util.ArrayList<java.lang.CharSequence>();

    int mConnectionId = android.view.accessibility.AccessibilityRecord.UNDEFINED;

    /* Hide constructor. */
    AccessibilityRecord() {
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
        setSource(source, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
    public void setSource(@android.annotation.Nullable
    android.view.View root, int virtualDescendantId) {
        enforceNotSealed();
        boolean important = true;
        int rootViewId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mSourceWindowId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        if (root != null) {
            important = root.isImportantForAccessibility();
            rootViewId = root.getAccessibilityViewId();
            mSourceWindowId = root.getAccessibilityWindowId();
        }
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_IMPORTANT_FOR_ACCESSIBILITY, important);
        mSourceNodeId = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootViewId, virtualDescendantId);
    }

    /**
     * Set the source node ID directly
     *
     * @param sourceNodeId
     * 		The source node Id
     * @unknown 
     */
    public void setSourceNodeId(long sourceNodeId) {
        mSourceNodeId = sourceNodeId;
    }

    /**
     * Gets the {@link AccessibilityNodeInfo} of the event source.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the received info
     *   by calling {@link AccessibilityNodeInfo#recycle() AccessibilityNodeInfo#recycle()}
     *   to avoid creating of multiple instances.
     * </p>
     *
     * @return The info of the source.
     */
    public android.view.accessibility.AccessibilityNodeInfo getSource() {
        enforceSealed();
        if (((mConnectionId == android.view.accessibility.AccessibilityRecord.UNDEFINED) || (mSourceWindowId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID)) || (android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(mSourceNodeId) == android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID)) {
            return null;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mSourceWindowId, mSourceNodeId, false, android.view.accessibility.AccessibilityRecord.GET_SOURCE_PREFETCH_FLAGS, null);
    }

    /**
     * Sets the window id.
     *
     * @param windowId
     * 		The window id.
     * @unknown 
     */
    public void setWindowId(int windowId) {
        mSourceWindowId = windowId;
    }

    /**
     * Gets the id of the window from which the event comes from.
     *
     * @return The window id.
     */
    public int getWindowId() {
        return mSourceWindowId;
    }

    /**
     * Gets if the source is checked.
     *
     * @return True if the view is checked, false otherwise.
     */
    public boolean isChecked() {
        return getBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_CHECKED);
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
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_CHECKED, isChecked);
    }

    /**
     * Gets if the source is enabled.
     *
     * @return True if the view is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return getBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_ENABLED);
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
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_ENABLED, isEnabled);
    }

    /**
     * Gets if the source is a password field.
     *
     * @return True if the view is a password field, false otherwise.
     */
    public boolean isPassword() {
        return getBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_PASSWORD);
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
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_PASSWORD, isPassword);
    }

    /**
     * Gets if the source is taking the entire screen.
     *
     * @return True if the source is full screen, false otherwise.
     */
    public boolean isFullScreen() {
        return getBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_FULL_SCREEN);
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
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_FULL_SCREEN, isFullScreen);
    }

    /**
     * Gets if the source is scrollable.
     *
     * @return True if the source is scrollable, false otherwise.
     */
    public boolean isScrollable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_SCROLLABLE);
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
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_SCROLLABLE, scrollable);
    }

    /**
     * Gets if the source is important for accessibility.
     *
     * <strong>Note:</strong> Used only internally to determine whether
     * to deliver the event to a given accessibility service since some
     * services may want to regard all views for accessibility while others
     * may want to regard only the important views for accessibility.
     *
     * @return True if the source is important for accessibility,
    false otherwise.
     * @unknown 
     */
    public boolean isImportantForAccessibility() {
        return getBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_IMPORTANT_FOR_ACCESSIBILITY);
    }

    /**
     * Sets if the source is important for accessibility.
     *
     * @param importantForAccessibility
     * 		True if the source is important for accessibility,
     * 		false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     * @unknown 
     */
    public void setImportantForAccessibility(boolean importantForAccessibility) {
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityRecord.PROPERTY_IMPORTANT_FOR_ACCESSIBILITY, importantForAccessibility);
    }

    /**
     * Gets the number of items that can be visited.
     *
     * @return The number of items.
     */
    public int getItemCount() {
        return mItemCount;
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
        enforceNotSealed();
        mItemCount = itemCount;
    }

    /**
     * Gets the index of the source in the list of items the can be visited.
     *
     * @return The current item index.
     */
    public int getCurrentItemIndex() {
        return mCurrentItemIndex;
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
        enforceNotSealed();
        mCurrentItemIndex = currentItemIndex;
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
        return mFromIndex;
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
        enforceNotSealed();
        mFromIndex = fromIndex;
    }

    /**
     * Gets the index of text selection end or the index of the last
     * visible item when scrolling.
     *
     * @return The index of selection end or last item index.
     */
    public int getToIndex() {
        return mToIndex;
    }

    /**
     * Sets the index of text selection end or the index of the last
     * visible item when scrolling.
     *
     * @param toIndex
     * 		The index of selection end or last item index.
     */
    public void setToIndex(int toIndex) {
        enforceNotSealed();
        mToIndex = toIndex;
    }

    /**
     * Gets the scroll offset of the source left edge in pixels.
     *
     * @return The scroll.
     */
    public int getScrollX() {
        return mScrollX;
    }

    /**
     * Sets the scroll offset of the source left edge in pixels.
     *
     * @param scrollX
     * 		The scroll.
     */
    public void setScrollX(int scrollX) {
        enforceNotSealed();
        mScrollX = scrollX;
    }

    /**
     * Gets the scroll offset of the source top edge in pixels.
     *
     * @return The scroll.
     */
    public int getScrollY() {
        return mScrollY;
    }

    /**
     * Sets the scroll offset of the source top edge in pixels.
     *
     * @param scrollY
     * 		The scroll.
     */
    public void setScrollY(int scrollY) {
        enforceNotSealed();
        mScrollY = scrollY;
    }

    /**
     * Gets the difference in pixels between the horizontal position before the scroll and the
     * current horizontal position
     *
     * @return the scroll delta x
     */
    public int getScrollDeltaX() {
        return mScrollDeltaX;
    }

    /**
     * Sets the difference in pixels between the horizontal position before the scroll and the
     * current horizontal position
     *
     * @param scrollDeltaX
     * 		the scroll delta x
     */
    public void setScrollDeltaX(int scrollDeltaX) {
        enforceNotSealed();
        mScrollDeltaX = scrollDeltaX;
    }

    /**
     * Gets the difference in pixels between the vertical position before the scroll and the
     * current vertical position
     *
     * @return the scroll delta y
     */
    public int getScrollDeltaY() {
        return mScrollDeltaY;
    }

    /**
     * Sets the difference in pixels between the vertical position before the scroll and the
     * current vertical position
     *
     * @param scrollDeltaY
     * 		the scroll delta y
     */
    public void setScrollDeltaY(int scrollDeltaY) {
        enforceNotSealed();
        mScrollDeltaY = scrollDeltaY;
    }

    /**
     * Gets the max scroll offset of the source left edge in pixels.
     *
     * @return The max scroll.
     */
    public int getMaxScrollX() {
        return mMaxScrollX;
    }

    /**
     * Sets the max scroll offset of the source left edge in pixels.
     *
     * @param maxScrollX
     * 		The max scroll.
     */
    public void setMaxScrollX(int maxScrollX) {
        enforceNotSealed();
        mMaxScrollX = maxScrollX;
    }

    /**
     * Gets the max scroll offset of the source top edge in pixels.
     *
     * @return The max scroll.
     */
    public int getMaxScrollY() {
        return mMaxScrollY;
    }

    /**
     * Sets the max scroll offset of the source top edge in pixels.
     *
     * @param maxScrollY
     * 		The max scroll.
     */
    public void setMaxScrollY(int maxScrollY) {
        enforceNotSealed();
        mMaxScrollY = maxScrollY;
    }

    /**
     * Gets the number of added characters.
     *
     * @return The number of added characters.
     */
    public int getAddedCount() {
        return mAddedCount;
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
        enforceNotSealed();
        mAddedCount = addedCount;
    }

    /**
     * Gets the number of removed characters.
     *
     * @return The number of removed characters.
     */
    public int getRemovedCount() {
        return mRemovedCount;
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
        enforceNotSealed();
        mRemovedCount = removedCount;
    }

    /**
     * Gets the class name of the source.
     *
     * @return The class name.
     */
    public java.lang.CharSequence getClassName() {
        return mClassName;
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
        enforceNotSealed();
        mClassName = className;
    }

    /**
     * Gets the text of the event. The index in the list represents the priority
     * of the text. Specifically, the lower the index the higher the priority.
     *
     * @return The text.
     */
    public java.util.List<java.lang.CharSequence> getText() {
        return mText;
    }

    /**
     * Gets the text before a change.
     *
     * @return The text before the change.
     */
    public java.lang.CharSequence getBeforeText() {
        return mBeforeText;
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
        enforceNotSealed();
        mBeforeText = (beforeText == null) ? null : beforeText.subSequence(0, beforeText.length());
    }

    /**
     * Gets the description of the source.
     *
     * @return The description.
     */
    public java.lang.CharSequence getContentDescription() {
        return mContentDescription;
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
        enforceNotSealed();
        mContentDescription = (contentDescription == null) ? null : contentDescription.subSequence(0, contentDescription.length());
    }

    /**
     * Gets the {@link Parcelable} data.
     *
     * @return The parcelable data.
     */
    public android.os.Parcelable getParcelableData() {
        return mParcelableData;
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
        enforceNotSealed();
        mParcelableData = parcelableData;
    }

    /**
     * Gets the id of the source node.
     *
     * @return The id.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public long getSourceNodeId() {
        return mSourceNodeId;
    }

    /**
     * Sets the unique id of the IAccessibilityServiceConnection over which
     * this instance can send requests to the system.
     *
     * @param connectionId
     * 		The connection id.
     * @unknown 
     */
    public void setConnectionId(int connectionId) {
        enforceNotSealed();
        mConnectionId = connectionId;
    }

    /**
     * Sets if this instance is sealed.
     *
     * @param sealed
     * 		Whether is sealed.
     * @unknown 
     */
    public void setSealed(boolean sealed) {
        mSealed = sealed;
    }

    /**
     * Gets if this instance is sealed.
     *
     * @return Whether is sealed.
     */
    boolean isSealed() {
        return mSealed;
    }

    /**
     * Enforces that this instance is sealed.
     *
     * @throws IllegalStateException
     * 		If this instance is not sealed.
     */
    void enforceSealed() {
        if (!isSealed()) {
            throw new java.lang.IllegalStateException("Cannot perform this " + "action on a not sealed instance.");
        }
    }

    /**
     * Enforces that this instance is not sealed.
     *
     * @throws IllegalStateException
     * 		If this instance is sealed.
     */
    void enforceNotSealed() {
        if (isSealed()) {
            throw new java.lang.IllegalStateException("Cannot perform this " + "action on a sealed instance.");
        }
    }

    /**
     * Gets the value of a boolean property.
     *
     * @param property
     * 		The property.
     * @return The value.
     */
    private boolean getBooleanProperty(int property) {
        return (mBooleanProperties & property) == property;
    }

    /**
     * Sets a boolean property.
     *
     * @param property
     * 		The property.
     * @param value
     * 		The value.
     */
    private void setBooleanProperty(int property, boolean value) {
        if (value) {
            mBooleanProperties |= property;
        } else {
            mBooleanProperties &= ~property;
        }
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * instantiated. The instance is initialized with data from the
     * given record.
     *
     * @return An instance.
     */
    public static android.view.accessibility.AccessibilityRecord obtain(android.view.accessibility.AccessibilityRecord record) {
        android.view.accessibility.AccessibilityRecord clone = android.view.accessibility.AccessibilityRecord.obtain();
        clone.init(record);
        return clone;
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * instantiated.
     *
     * @return An instance.
     */
    public static android.view.accessibility.AccessibilityRecord obtain() {
        synchronized(android.view.accessibility.AccessibilityRecord.sPoolLock) {
            if (android.view.accessibility.AccessibilityRecord.sPool != null) {
                android.view.accessibility.AccessibilityRecord record = android.view.accessibility.AccessibilityRecord.sPool;
                android.view.accessibility.AccessibilityRecord.sPool = android.view.accessibility.AccessibilityRecord.sPool.mNext;
                android.view.accessibility.AccessibilityRecord.sPoolSize--;
                record.mNext = null;
                record.mIsInPool = false;
                return record;
            }
            return new android.view.accessibility.AccessibilityRecord();
        }
    }

    /**
     * Return an instance back to be reused.
     * <p>
     * <strong>Note:</strong> You must not touch the object after calling this function.
     *
     * @throws IllegalStateException
     * 		If the record is already recycled.
     */
    public void recycle() {
        if (mIsInPool) {
            throw new java.lang.IllegalStateException("Record already recycled!");
        }
        clear();
        synchronized(android.view.accessibility.AccessibilityRecord.sPoolLock) {
            if (android.view.accessibility.AccessibilityRecord.sPoolSize <= android.view.accessibility.AccessibilityRecord.MAX_POOL_SIZE) {
                mNext = android.view.accessibility.AccessibilityRecord.sPool;
                android.view.accessibility.AccessibilityRecord.sPool = this;
                mIsInPool = true;
                android.view.accessibility.AccessibilityRecord.sPoolSize++;
            }
        }
    }

    /**
     * Initialize this record from another one.
     *
     * @param record
     * 		The to initialize from.
     */
    void init(android.view.accessibility.AccessibilityRecord record) {
        mSealed = record.mSealed;
        mBooleanProperties = record.mBooleanProperties;
        mCurrentItemIndex = record.mCurrentItemIndex;
        mItemCount = record.mItemCount;
        mFromIndex = record.mFromIndex;
        mToIndex = record.mToIndex;
        mScrollX = record.mScrollX;
        mScrollY = record.mScrollY;
        mMaxScrollX = record.mMaxScrollX;
        mMaxScrollY = record.mMaxScrollY;
        mAddedCount = record.mAddedCount;
        mRemovedCount = record.mRemovedCount;
        mClassName = record.mClassName;
        mContentDescription = record.mContentDescription;
        mBeforeText = record.mBeforeText;
        mParcelableData = record.mParcelableData;
        mText.addAll(record.mText);
        mSourceWindowId = record.mSourceWindowId;
        mSourceNodeId = record.mSourceNodeId;
        mConnectionId = record.mConnectionId;
    }

    /**
     * Clears the state of this instance.
     */
    void clear() {
        mSealed = false;
        mBooleanProperties = 0;
        mCurrentItemIndex = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mItemCount = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mFromIndex = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mToIndex = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mScrollX = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mScrollY = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mMaxScrollX = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mMaxScrollY = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mAddedCount = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mRemovedCount = android.view.accessibility.AccessibilityRecord.UNDEFINED;
        mClassName = null;
        mContentDescription = null;
        mBeforeText = null;
        mParcelableData = null;
        mText.clear();
        mSourceNodeId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mSourceWindowId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        mConnectionId = android.view.accessibility.AccessibilityRecord.UNDEFINED;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return appendTo(new java.lang.StringBuilder()).toString();
    }

    java.lang.StringBuilder appendTo(java.lang.StringBuilder builder) {
        builder.append(" [ ClassName: ").append(mClassName);
        if ((!android.view.accessibility.AccessibilityRecord.DEBUG_CONCISE_TOSTRING) || (!com.android.internal.util.CollectionUtils.isEmpty(mText))) {
            appendPropName(builder, "Text").append(mText);
        }
        append(builder, "ContentDescription", mContentDescription);
        append(builder, "ItemCount", mItemCount);
        append(builder, "CurrentItemIndex", mCurrentItemIndex);
        appendUnless(true, android.view.accessibility.AccessibilityRecord.PROPERTY_ENABLED, builder);
        appendUnless(false, android.view.accessibility.AccessibilityRecord.PROPERTY_PASSWORD, builder);
        appendUnless(false, android.view.accessibility.AccessibilityRecord.PROPERTY_CHECKED, builder);
        appendUnless(false, android.view.accessibility.AccessibilityRecord.PROPERTY_FULL_SCREEN, builder);
        appendUnless(false, android.view.accessibility.AccessibilityRecord.PROPERTY_SCROLLABLE, builder);
        append(builder, "BeforeText", mBeforeText);
        append(builder, "FromIndex", mFromIndex);
        append(builder, "ToIndex", mToIndex);
        append(builder, "ScrollX", mScrollX);
        append(builder, "ScrollY", mScrollY);
        append(builder, "MaxScrollX", mMaxScrollX);
        append(builder, "MaxScrollY", mMaxScrollY);
        append(builder, "AddedCount", mAddedCount);
        append(builder, "RemovedCount", mRemovedCount);
        append(builder, "ParcelableData", mParcelableData);
        builder.append(" ]");
        return builder;
    }

    private void appendUnless(boolean defValue, int prop, java.lang.StringBuilder builder) {
        boolean value = getBooleanProperty(prop);
        if (android.view.accessibility.AccessibilityRecord.DEBUG_CONCISE_TOSTRING && (value == defValue))
            return;

        appendPropName(builder, android.view.accessibility.AccessibilityRecord.singleBooleanPropertyToString(prop)).append(value);
    }

    private static java.lang.String singleBooleanPropertyToString(int prop) {
        switch (prop) {
            case android.view.accessibility.AccessibilityRecord.PROPERTY_CHECKED :
                return "Checked";
            case android.view.accessibility.AccessibilityRecord.PROPERTY_ENABLED :
                return "Enabled";
            case android.view.accessibility.AccessibilityRecord.PROPERTY_PASSWORD :
                return "Password";
            case android.view.accessibility.AccessibilityRecord.PROPERTY_FULL_SCREEN :
                return "FullScreen";
            case android.view.accessibility.AccessibilityRecord.PROPERTY_SCROLLABLE :
                return "Scrollable";
            case android.view.accessibility.AccessibilityRecord.PROPERTY_IMPORTANT_FOR_ACCESSIBILITY :
                return "ImportantForAccessibility";
            default :
                return java.lang.Integer.toHexString(prop);
        }
    }

    private void append(java.lang.StringBuilder builder, java.lang.String propName, int propValue) {
        if (android.view.accessibility.AccessibilityRecord.DEBUG_CONCISE_TOSTRING && (propValue == android.view.accessibility.AccessibilityRecord.UNDEFINED))
            return;

        appendPropName(builder, propName).append(propValue);
    }

    private void append(java.lang.StringBuilder builder, java.lang.String propName, java.lang.Object propValue) {
        if (android.view.accessibility.AccessibilityRecord.DEBUG_CONCISE_TOSTRING && (propValue == null))
            return;

        appendPropName(builder, propName).append(propValue);
    }

    private java.lang.StringBuilder appendPropName(java.lang.StringBuilder builder, java.lang.String propName) {
        return builder.append("; ").append(propName).append(": ");
    }
}

