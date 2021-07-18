/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Helper for accessing {@link android.view.accessibility.AccessibilityWindowInfo}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public class AccessibilityWindowInfoCompat {
    private interface AccessibilityWindowInfoImpl {
        java.lang.Object obtain();

        java.lang.Object obtain(java.lang.Object info);

        int getType(java.lang.Object info);

        int getLayer(java.lang.Object info);

        java.lang.Object getRoot(java.lang.Object info);

        java.lang.Object getParent(java.lang.Object info);

        int getId(java.lang.Object info);

        void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds);

        boolean isActive(java.lang.Object info);

        boolean isFocused(java.lang.Object info);

        boolean isAccessibilityFocused(java.lang.Object info);

        int getChildCount(java.lang.Object info);

        java.lang.Object getChild(java.lang.Object info, int index);

        java.lang.CharSequence getTitle(java.lang.Object info);

        java.lang.Object getAnchor(java.lang.Object info);

        void recycle(java.lang.Object info);
    }

    private static class AccessibilityWindowInfoStubImpl implements android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl {
        AccessibilityWindowInfoStubImpl() {
        }

        @java.lang.Override
        public java.lang.Object obtain() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtain(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public int getType(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.UNDEFINED;
        }

        @java.lang.Override
        public int getLayer(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.UNDEFINED;
        }

        @java.lang.Override
        public java.lang.Object getRoot(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getParent(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public int getId(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.UNDEFINED;
        }

        @java.lang.Override
        public void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds) {
        }

        @java.lang.Override
        public boolean isActive(java.lang.Object info) {
            return true;
        }

        @java.lang.Override
        public boolean isFocused(java.lang.Object info) {
            return true;
        }

        @java.lang.Override
        public boolean isAccessibilityFocused(java.lang.Object info) {
            return true;
        }

        @java.lang.Override
        public int getChildCount(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public java.lang.Object getChild(java.lang.Object info, int index) {
            return null;
        }

        @java.lang.Override
        public void recycle(java.lang.Object info) {
        }

        @java.lang.Override
        public java.lang.CharSequence getTitle(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getAnchor(java.lang.Object info) {
            return null;
        }
    }

    private static class AccessibilityWindowInfoApi21Impl extends android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl {
        AccessibilityWindowInfoApi21Impl() {
        }

        @java.lang.Override
        public java.lang.Object obtain() {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.obtain();
        }

        @java.lang.Override
        public java.lang.Object obtain(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.obtain(info);
        }

        @java.lang.Override
        public int getType(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getType(info);
        }

        @java.lang.Override
        public int getLayer(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getLayer(info);
        }

        @java.lang.Override
        public java.lang.Object getRoot(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getRoot(info);
        }

        @java.lang.Override
        public java.lang.Object getParent(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getParent(info);
        }

        @java.lang.Override
        public int getId(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getId(info);
        }

        @java.lang.Override
        public void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds) {
            android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getBoundsInScreen(info, outBounds);
        }

        @java.lang.Override
        public boolean isActive(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.isActive(info);
        }

        @java.lang.Override
        public boolean isFocused(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.isFocused(info);
        }

        @java.lang.Override
        public boolean isAccessibilityFocused(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.isAccessibilityFocused(info);
        }

        @java.lang.Override
        public int getChildCount(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getChildCount(info);
        }

        @java.lang.Override
        public java.lang.Object getChild(java.lang.Object info, int index) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.getChild(info, index);
        }

        @java.lang.Override
        public void recycle(java.lang.Object info) {
            android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21.recycle(info);
        }
    }

    private static class AccessibilityWindowInfoApi24Impl extends android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi21Impl {
        AccessibilityWindowInfoApi24Impl() {
        }

        @java.lang.Override
        public java.lang.CharSequence getTitle(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi24.getTitle(info);
        }

        @java.lang.Override
        public java.lang.Object getAnchor(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi24.getAnchor(info);
        }
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            IMPL = new android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi24Impl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                IMPL = new android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi21Impl();
            } else {
                IMPL = new android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl();
            }

    }

    private static final android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl IMPL;

    private java.lang.Object mInfo;

    private static final int UNDEFINED = -1;

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

    /**
     * Creates a wrapper for info implementation.
     *
     * @param object
     * 		The info to wrap.
     * @return A wrapper for if the object is not null, null otherwise.
     */
    static android.support.v4.view.accessibility.AccessibilityWindowInfoCompat wrapNonNullInstance(java.lang.Object object) {
        if (object != null) {
            return new android.support.v4.view.accessibility.AccessibilityWindowInfoCompat(object);
        }
        return null;
    }

    private AccessibilityWindowInfoCompat(java.lang.Object info) {
        mInfo = info;
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
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getType(mInfo);
    }

    /**
     * Gets the layer which determines the Z-order of the window. Windows
     * with greater layer appear on top of windows with lesser layer.
     *
     * @return The window layer.
     */
    public int getLayer() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getLayer(mInfo);
    }

    /**
     * Gets the root node in the window's hierarchy.
     *
     * @return The root node.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getRoot() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getRoot(mInfo));
    }

    /**
     * Gets the parent window if such.
     *
     * @return The parent window.
     */
    public android.support.v4.view.accessibility.AccessibilityWindowInfoCompat getParent() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getParent(mInfo));
    }

    /**
     * Gets the unique window id.
     *
     * @return windowId The window id.
     */
    public int getId() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getId(mInfo);
    }

    /**
     * Gets the bounds of this window in the screen.
     *
     * @param outBounds
     * 		The out window bounds.
     */
    public void getBoundsInScreen(android.graphics.Rect outBounds) {
        android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getBoundsInScreen(mInfo, outBounds);
    }

    /**
     * Gets if this window is active. An active window is the one
     * the user is currently touching or the window has input focus
     * and the user is not touching any window.
     *
     * @return Whether this is the active window.
     */
    public boolean isActive() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.isActive(mInfo);
    }

    /**
     * Gets if this window has input focus.
     *
     * @return Whether has input focus.
     */
    public boolean isFocused() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.isFocused(mInfo);
    }

    /**
     * Gets if this window has accessibility focus.
     *
     * @return Whether has accessibility focus.
     */
    public boolean isAccessibilityFocused() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.isAccessibilityFocused(mInfo);
    }

    /**
     * Gets the number of child windows.
     *
     * @return The child count.
     */
    public int getChildCount() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getChildCount(mInfo);
    }

    /**
     * Gets the child window at a given index.
     *
     * @param index
     * 		The index.
     * @return The child.
     */
    public android.support.v4.view.accessibility.AccessibilityWindowInfoCompat getChild(int index) {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getChild(mInfo, index));
    }

    /**
     * Gets the title of the window.
     *
     * @return The title of the window, or the application label for the window if no title was
    explicitly set, or {@code null} if neither is available.
     */
    public java.lang.CharSequence getTitle() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getTitle(mInfo);
    }

    /**
     * Gets the node that anchors this window to another.
     *
     * @return The anchor node, or {@code null} if none exists.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getAnchor() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.getAnchor(mInfo));
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * created.
     *
     * @return An instance.
     */
    public static android.support.v4.view.accessibility.AccessibilityWindowInfoCompat obtain() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.obtain());
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
    public static android.support.v4.view.accessibility.AccessibilityWindowInfoCompat obtain(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat info) {
        return info == null ? null : android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.obtain(info.mInfo));
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
        android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.IMPL.recycle(mInfo);
    }

    @java.lang.Override
    public int hashCode() {
        return mInfo == null ? 0 : mInfo.hashCode();
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
        android.support.v4.view.accessibility.AccessibilityWindowInfoCompat other = ((android.support.v4.view.accessibility.AccessibilityWindowInfoCompat) (obj));
        if (mInfo == null) {
            if (other.mInfo != null) {
                return false;
            }
        } else
            if (!mInfo.equals(other.mInfo)) {
                return false;
            }

        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        android.graphics.Rect bounds = new android.graphics.Rect();
        getBoundsInScreen(bounds);
        builder.append("AccessibilityWindowInfo[");
        builder.append("id=").append(getId());
        builder.append(", type=").append(android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.typeToString(getType()));
        builder.append(", layer=").append(getLayer());
        builder.append(", bounds=").append(bounds);
        builder.append(", focused=").append(isFocused());
        builder.append(", active=").append(isActive());
        builder.append(", hasParent=").append(getParent() != null);
        builder.append(", hasChildren=").append(getChildCount() > 0);
        builder.append(']');
        return builder.toString();
    }

    private static java.lang.String typeToString(int type) {
        switch (type) {
            case android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.TYPE_APPLICATION :
                {
                    return "TYPE_APPLICATION";
                }
            case android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.TYPE_INPUT_METHOD :
                {
                    return "TYPE_INPUT_METHOD";
                }
            case android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.TYPE_SYSTEM :
                {
                    return "TYPE_SYSTEM";
                }
            case android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.TYPE_ACCESSIBILITY_OVERLAY :
                {
                    return "TYPE_ACCESSIBILITY_OVERLAY";
                }
            default :
                return "<UNKNOWN>";
        }
    }
}

