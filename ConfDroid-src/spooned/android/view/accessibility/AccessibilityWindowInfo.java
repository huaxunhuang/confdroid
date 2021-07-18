/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * This class represents a state snapshot of a window for accessibility
 * purposes. The screen content contains one or more windows where some
 * windows can be descendants of other windows, which is the windows are
 * hierarchically ordered. Note that there is no root window. Hence, the
 * screen content can be seen as a collection of window trees.
 */
public final class AccessibilityWindowInfo implements android.os.Parcelable {
    private static final boolean DEBUG = false;

    /**
     * Window type: This is an application window. Such a window shows UI for
     * interacting with an application.
     */
    public static final int TYPE_APPLICATION = 1;

    /**
     * Window type: This is an input method window. Such a window shows UI for
     * inputting text such as keyboard, suggestions, etc.
     */
    public static final int TYPE_INPUT_METHOD = 2;

    /**
     * Window type: This is an system window. Such a window shows UI for
     * interacting with the system.
     */
    public static final int TYPE_SYSTEM = 3;

    /**
     * Window type: Windows that are overlaid <em>only</em> by an {@link android.accessibilityservice.AccessibilityService} for interception of
     * user interactions without changing the windows an accessibility service
     * can introspect. In particular, an accessibility service can introspect
     * only windows that a sighted user can interact with which they can touch
     * these windows or can type into these windows. For example, if there
     * is a full screen accessibility overlay that is touchable, the windows
     * below it will be introspectable by an accessibility service regardless
     * they are covered by a touchable window.
     */
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;

    /**
     * Window type: A system window used to divide the screen in split-screen mode.
     * This type of window is present only in split-screen mode.
     */
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;

    /* Special values for window IDs */
    /**
     *
     *
     * @unknown 
     */
    public static final int ACTIVE_WINDOW_ID = java.lang.Integer.MAX_VALUE;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNDEFINED_WINDOW_ID = -1;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANY_WINDOW_ID = -2;

    /**
     *
     *
     * @unknown 
     */
    public static final int PICTURE_IN_PICTURE_ACTION_REPLACER_WINDOW_ID = -3;

    private static final int BOOLEAN_PROPERTY_ACTIVE = 1 << 0;

    private static final int BOOLEAN_PROPERTY_FOCUSED = 1 << 1;

    private static final int BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED = 1 << 2;

    private static final int BOOLEAN_PROPERTY_PICTURE_IN_PICTURE = 1 << 3;

    // Housekeeping.
    private static final int MAX_POOL_SIZE = 10;

    private static final android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityWindowInfo> sPool = new android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityWindowInfo>(android.view.accessibility.AccessibilityWindowInfo.MAX_POOL_SIZE);

    // TODO(b/129300068): Remove sNumInstancesInUse.
    private static java.util.concurrent.atomic.AtomicInteger sNumInstancesInUse;

    // Data.
    private int mType = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    private int mLayer = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    private int mBooleanProperties;

    private int mId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    private int mParentId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    private final android.graphics.Rect mBoundsInScreen = new android.graphics.Rect();

    private android.util.LongArray mChildIds;

    private java.lang.CharSequence mTitle;

    private long mAnchorId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private int mConnectionId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    private AccessibilityWindowInfo() {
        /* do nothing - hide constructor */
    }

    /**
     *
     *
     * @unknown 
     */
    AccessibilityWindowInfo(android.view.accessibility.AccessibilityWindowInfo info) {
        init(info);
    }

    /**
     * Gets the title of the window.
     *
     * @return The title of the window, or {@code null} if none is available.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Sets the title of the window.
     *
     * @param title
     * 		The title.
     * @unknown 
     */
    public void setTitle(java.lang.CharSequence title) {
        mTitle = title;
    }

    /**
     * Gets the type of the window.
     *
     * @return The type.
     * @see #TYPE_APPLICATION
     * @see #TYPE_INPUT_METHOD
     * @see #TYPE_SYSTEM
     * @see #TYPE_ACCESSIBILITY_OVERLAY
     */
    public int getType() {
        return mType;
    }

    /**
     * Sets the type of the window.
     *
     * @param type
     * 		The type
     * @unknown 
     */
    public void setType(int type) {
        mType = type;
    }

    /**
     * Gets the layer which determines the Z-order of the window. Windows
     * with greater layer appear on top of windows with lesser layer.
     *
     * @return The window layer.
     */
    public int getLayer() {
        return mLayer;
    }

    /**
     * Sets the layer which determines the Z-order of the window. Windows
     * with greater layer appear on top of windows with lesser layer.
     *
     * @param layer
     * 		The window layer.
     * @unknown 
     */
    public void setLayer(int layer) {
        mLayer = layer;
    }

    /**
     * Gets the root node in the window's hierarchy.
     *
     * @return The root node.
     */
    public android.view.accessibility.AccessibilityNodeInfo getRoot() {
        if (mConnectionId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID) {
            return null;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mId, android.view.accessibility.AccessibilityNodeInfo.ROOT_NODE_ID, true, android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS, null);
    }

    /**
     * Sets the anchor node's ID.
     *
     * @param anchorId
     * 		The anchor's accessibility id in its window.
     * @unknown 
     */
    public void setAnchorId(long anchorId) {
        mAnchorId = anchorId;
    }

    /**
     * Gets the node that anchors this window to another.
     *
     * @return The anchor node, or {@code null} if none exists.
     */
    public android.view.accessibility.AccessibilityNodeInfo getAnchor() {
        if (((mConnectionId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID) || (mAnchorId == android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID)) || (mParentId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID)) {
            return null;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mParentId, mAnchorId, true, 0, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setPictureInPicture(boolean pictureInPicture) {
        setBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_PICTURE_IN_PICTURE, pictureInPicture);
    }

    /**
     * Check if the window is in picture-in-picture mode.
     *
     * @return {@code true} if the window is in picture-in-picture mode, {@code false} otherwise.
     */
    public boolean isInPictureInPictureMode() {
        return getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_PICTURE_IN_PICTURE);
    }

    /**
     * Gets the parent window.
     *
     * @return The parent window, or {@code null} if none exists.
     */
    public android.view.accessibility.AccessibilityWindowInfo getParent() {
        if ((mConnectionId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID) || (mParentId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID)) {
            return null;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.getWindow(mConnectionId, mParentId);
    }

    /**
     * Sets the parent window id.
     *
     * @param parentId
     * 		The parent id.
     * @unknown 
     */
    public void setParentId(int parentId) {
        mParentId = parentId;
    }

    /**
     * Gets the unique window id.
     *
     * @return windowId The window id.
     */
    public int getId() {
        return mId;
    }

    /**
     * Sets the unique window id.
     *
     * @param id
     * 		The window id.
     * @unknown 
     */
    public void setId(int id) {
        mId = id;
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
        mConnectionId = connectionId;
    }

    /**
     * Gets the bounds of this window in the screen.
     *
     * @param outBounds
     * 		The out window bounds.
     */
    public void getBoundsInScreen(android.graphics.Rect outBounds) {
        outBounds.set(mBoundsInScreen);
    }

    /**
     * Sets the bounds of this window in the screen.
     *
     * @param bounds
     * 		The out window bounds.
     * @unknown 
     */
    public void setBoundsInScreen(android.graphics.Rect bounds) {
        mBoundsInScreen.set(bounds);
    }

    /**
     * Gets if this window is active. An active window is the one
     * the user is currently touching or the window has input focus
     * and the user is not touching any window.
     *
     * @return Whether this is the active window.
     */
    public boolean isActive() {
        return getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACTIVE);
    }

    /**
     * Sets if this window is active, which is this is the window
     * the user is currently touching or the window has input focus
     * and the user is not touching any window.
     *
     * @param active
     * 		Whether this is the active window.
     * @unknown 
     */
    public void setActive(boolean active) {
        setBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACTIVE, active);
    }

    /**
     * Gets if this window has input focus.
     *
     * @return Whether has input focus.
     */
    public boolean isFocused() {
        return getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_FOCUSED);
    }

    /**
     * Sets if this window has input focus.
     *
     * @param focused
     * 		Whether has input focus.
     * @unknown 
     */
    public void setFocused(boolean focused) {
        setBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_FOCUSED, focused);
    }

    /**
     * Gets if this window has accessibility focus.
     *
     * @return Whether has accessibility focus.
     */
    public boolean isAccessibilityFocused() {
        return getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED);
    }

    /**
     * Sets if this window has accessibility focus.
     *
     * @param focused
     * 		Whether has accessibility focus.
     * @unknown 
     */
    public void setAccessibilityFocused(boolean focused) {
        setBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED, focused);
    }

    /**
     * Gets the number of child windows.
     *
     * @return The child count.
     */
    public int getChildCount() {
        return mChildIds != null ? mChildIds.size() : 0;
    }

    /**
     * Gets the child window at a given index.
     *
     * @param index
     * 		The index.
     * @return The child.
     */
    public android.view.accessibility.AccessibilityWindowInfo getChild(int index) {
        if (mChildIds == null) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (mConnectionId == android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID) {
            return null;
        }
        final int childId = ((int) (mChildIds.get(index)));
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.getWindow(mConnectionId, childId);
    }

    /**
     * Adds a child window.
     *
     * @param childId
     * 		The child window id.
     * @unknown 
     */
    public void addChild(int childId) {
        if (mChildIds == null) {
            mChildIds = new android.util.LongArray();
        }
        mChildIds.add(childId);
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * created.
     *
     * @return An instance.
     */
    public static android.view.accessibility.AccessibilityWindowInfo obtain() {
        android.view.accessibility.AccessibilityWindowInfo info = android.view.accessibility.AccessibilityWindowInfo.sPool.acquire();
        if (info == null) {
            info = new android.view.accessibility.AccessibilityWindowInfo();
        }
        if (android.view.accessibility.AccessibilityWindowInfo.sNumInstancesInUse != null) {
            android.view.accessibility.AccessibilityWindowInfo.sNumInstancesInUse.incrementAndGet();
        }
        return info;
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * created. The returned instance is initialized from the given
     * <code>info</code>.
     *
     * @param info
     * 		The other info.
     * @return An instance.
     */
    public static android.view.accessibility.AccessibilityWindowInfo obtain(android.view.accessibility.AccessibilityWindowInfo info) {
        android.view.accessibility.AccessibilityWindowInfo infoClone = android.view.accessibility.AccessibilityWindowInfo.obtain();
        infoClone.init(info);
        return infoClone;
    }

    /**
     * Specify a counter that will be incremented on obtain() and decremented on recycle()
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static void setNumInstancesInUseCounter(java.util.concurrent.atomic.AtomicInteger counter) {
        if (android.view.accessibility.AccessibilityWindowInfo.sNumInstancesInUse != null) {
            android.view.accessibility.AccessibilityWindowInfo.sNumInstancesInUse = counter;
        }
    }

    /**
     * Return an instance back to be reused.
     * <p>
     * <strong>Note:</strong> You must not touch the object after calling this function.
     * </p>
     *
     * @throws IllegalStateException
     * 		If the info is already recycled.
     */
    public void recycle() {
        clear();
        android.view.accessibility.AccessibilityWindowInfo.sPool.release(this);
        if (android.view.accessibility.AccessibilityWindowInfo.sNumInstancesInUse != null) {
            android.view.accessibility.AccessibilityWindowInfo.sNumInstancesInUse.decrementAndGet();
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mType);
        parcel.writeInt(mLayer);
        parcel.writeInt(mBooleanProperties);
        parcel.writeInt(mId);
        parcel.writeInt(mParentId);
        mBoundsInScreen.writeToParcel(parcel, flags);
        parcel.writeCharSequence(mTitle);
        parcel.writeLong(mAnchorId);
        final android.util.LongArray childIds = mChildIds;
        if (childIds == null) {
            parcel.writeInt(0);
        } else {
            final int childCount = childIds.size();
            parcel.writeInt(childCount);
            for (int i = 0; i < childCount; i++) {
                parcel.writeInt(((int) (childIds.get(i))));
            }
        }
        parcel.writeInt(mConnectionId);
    }

    /**
     * Initializes this instance from another one.
     *
     * @param other
     * 		The other instance.
     */
    private void init(android.view.accessibility.AccessibilityWindowInfo other) {
        mType = other.mType;
        mLayer = other.mLayer;
        mBooleanProperties = other.mBooleanProperties;
        mId = other.mId;
        mParentId = other.mParentId;
        mBoundsInScreen.set(other.mBoundsInScreen);
        mTitle = other.mTitle;
        mAnchorId = other.mAnchorId;
        if ((other.mChildIds != null) && (other.mChildIds.size() > 0)) {
            if (mChildIds == null) {
                mChildIds = other.mChildIds.clone();
            } else {
                mChildIds.addAll(other.mChildIds);
            }
        }
        mConnectionId = other.mConnectionId;
    }

    private void initFromParcel(android.os.Parcel parcel) {
        mType = parcel.readInt();
        mLayer = parcel.readInt();
        mBooleanProperties = parcel.readInt();
        mId = parcel.readInt();
        mParentId = parcel.readInt();
        mBoundsInScreen.readFromParcel(parcel);
        mTitle = parcel.readCharSequence();
        mAnchorId = parcel.readLong();
        final int childCount = parcel.readInt();
        if (childCount > 0) {
            if (mChildIds == null) {
                mChildIds = new android.util.LongArray(childCount);
            }
            for (int i = 0; i < childCount; i++) {
                final int childId = parcel.readInt();
                mChildIds.add(childId);
            }
        }
        mConnectionId = parcel.readInt();
    }

    @java.lang.Override
    public int hashCode() {
        return mId;
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
        android.view.accessibility.AccessibilityWindowInfo other = ((android.view.accessibility.AccessibilityWindowInfo) (obj));
        return mId == other.mId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("AccessibilityWindowInfo[");
        builder.append("title=").append(mTitle);
        builder.append(", id=").append(mId);
        builder.append(", type=").append(android.view.accessibility.AccessibilityWindowInfo.typeToString(mType));
        builder.append(", layer=").append(mLayer);
        builder.append(", bounds=").append(mBoundsInScreen);
        builder.append(", focused=").append(isFocused());
        builder.append(", active=").append(isActive());
        builder.append(", pictureInPicture=").append(isInPictureInPictureMode());
        if (android.view.accessibility.AccessibilityWindowInfo.DEBUG) {
            builder.append(", parent=").append(mParentId);
            builder.append(", children=[");
            if (mChildIds != null) {
                final int childCount = mChildIds.size();
                for (int i = 0; i < childCount; i++) {
                    builder.append(mChildIds.get(i));
                    if (i < (childCount - 1)) {
                        builder.append(',');
                    }
                }
            } else {
                builder.append("null");
            }
            builder.append(']');
        } else {
            builder.append(", hasParent=").append(mParentId != android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID);
            builder.append(", isAnchored=").append(mAnchorId != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID);
            builder.append(", hasChildren=").append((mChildIds != null) && (mChildIds.size() > 0));
        }
        builder.append(']');
        return builder.toString();
    }

    /**
     * Clears the internal state.
     */
    private void clear() {
        mType = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        mLayer = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        mBooleanProperties = 0;
        mId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        mParentId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        mBoundsInScreen.setEmpty();
        if (mChildIds != null) {
            mChildIds.clear();
        }
        mConnectionId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;
        mAnchorId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;
        mTitle = null;
    }

    /**
     * Gets the value of a boolean property.
     *
     * @param property
     * 		The property.
     * @return The value.
     */
    private boolean getBooleanProperty(int property) {
        return (mBooleanProperties & property) != 0;
    }

    /**
     * Sets a boolean property.
     *
     * @param property
     * 		The property.
     * @param value
     * 		The value.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    private void setBooleanProperty(int property, boolean value) {
        if (value) {
            mBooleanProperties |= property;
        } else {
            mBooleanProperties &= ~property;
        }
    }

    private static java.lang.String typeToString(int type) {
        switch (type) {
            case android.view.accessibility.AccessibilityWindowInfo.TYPE_APPLICATION :
                {
                    return "TYPE_APPLICATION";
                }
            case android.view.accessibility.AccessibilityWindowInfo.TYPE_INPUT_METHOD :
                {
                    return "TYPE_INPUT_METHOD";
                }
            case android.view.accessibility.AccessibilityWindowInfo.TYPE_SYSTEM :
                {
                    return "TYPE_SYSTEM";
                }
            case android.view.accessibility.AccessibilityWindowInfo.TYPE_ACCESSIBILITY_OVERLAY :
                {
                    return "TYPE_ACCESSIBILITY_OVERLAY";
                }
            case android.view.accessibility.AccessibilityWindowInfo.TYPE_SPLIT_SCREEN_DIVIDER :
                {
                    return "TYPE_SPLIT_SCREEN_DIVIDER";
                }
            default :
                return "<UNKNOWN>";
        }
    }

    /**
     * Checks whether this window changed. The argument should be
     * another state of the same window, which is have the same id
     * and type as they never change.
     *
     * @param other
     * 		The new state.
     * @return Whether something changed.
     * @unknown 
     */
    public boolean changed(android.view.accessibility.AccessibilityWindowInfo other) {
        if (other.mId != mId) {
            throw new java.lang.IllegalArgumentException("Not same window.");
        }
        if (other.mType != mType) {
            throw new java.lang.IllegalArgumentException("Not same type.");
        }
        if (!mBoundsInScreen.equals(other.mBoundsInScreen)) {
            return true;
        }
        if (mLayer != other.mLayer) {
            return true;
        }
        if (mBooleanProperties != other.mBooleanProperties) {
            return true;
        }
        if (mParentId != other.mParentId) {
            return true;
        }
        if (mChildIds == null) {
            if (other.mChildIds != null) {
                return true;
            }
        } else
            if (!mChildIds.equals(other.mChildIds)) {
                return true;
            }

        return false;
    }

    /**
     * Reports how this window differs from a possibly different state of the same window. The
     * argument must have the same id and type as neither of those properties may change.
     *
     * @param other
     * 		The new state.
     * @return A set of flags showing how the window has changes, or 0 if the two states are the
    same.
     * @unknown 
     */
    @android.view.accessibility.AccessibilityEvent.WindowsChangeTypes
    public int differenceFrom(android.view.accessibility.AccessibilityWindowInfo other) {
        if (other.mId != mId) {
            throw new java.lang.IllegalArgumentException("Not same window.");
        }
        if (other.mType != mType) {
            throw new java.lang.IllegalArgumentException("Not same type.");
        }
        int changes = 0;
        if (!android.text.TextUtils.equals(mTitle, other.mTitle)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_TITLE;
        }
        if (!mBoundsInScreen.equals(other.mBoundsInScreen)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_BOUNDS;
        }
        if (mLayer != other.mLayer) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_LAYER;
        }
        if (getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACTIVE) != other.getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACTIVE)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_ACTIVE;
        }
        if (getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_FOCUSED) != other.getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_FOCUSED)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_FOCUSED;
        }
        if (getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED) != other.getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_ACCESSIBILITY_FOCUSED;
        }
        if (getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_PICTURE_IN_PICTURE) != other.getBooleanProperty(android.view.accessibility.AccessibilityWindowInfo.BOOLEAN_PROPERTY_PICTURE_IN_PICTURE)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_PIP;
        }
        if (mParentId != other.mParentId) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_PARENT;
        }
        if (!java.util.Objects.equals(mChildIds, other.mChildIds)) {
            changes |= android.view.accessibility.AccessibilityEvent.WINDOWS_CHANGE_CHILDREN;
        }
        return changes;
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.accessibility.AccessibilityWindowInfo> CREATOR = new android.view.accessibility.Creator<android.view.accessibility.AccessibilityWindowInfo>() {
        @java.lang.Override
        public android.view.accessibility.AccessibilityWindowInfo createFromParcel(android.os.Parcel parcel) {
            android.view.accessibility.AccessibilityWindowInfo info = android.view.accessibility.AccessibilityWindowInfo.obtain();
            info.initFromParcel(parcel);
            return info;
        }

        @java.lang.Override
        public android.view.accessibility.AccessibilityWindowInfo[] newArray(int size) {
            return new android.view.accessibility.AccessibilityWindowInfo[size];
        }
    };
}

