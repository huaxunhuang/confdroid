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
 * Helper for accessing features in {@link AccessibilityEvent}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class AccessibilityEventCompat {
    static interface AccessibilityEventVersionImpl {
        int getRecordCount(android.view.accessibility.AccessibilityEvent event);

        void appendRecord(android.view.accessibility.AccessibilityEvent event, java.lang.Object record);

        java.lang.Object getRecord(android.view.accessibility.AccessibilityEvent event, int index);

        void setContentChangeTypes(android.view.accessibility.AccessibilityEvent event, int types);

        int getContentChangeTypes(android.view.accessibility.AccessibilityEvent event);

        public void setMovementGranularity(android.view.accessibility.AccessibilityEvent event, int granularity);

        public int getMovementGranularity(android.view.accessibility.AccessibilityEvent event);

        public void setAction(android.view.accessibility.AccessibilityEvent event, int action);

        public int getAction(android.view.accessibility.AccessibilityEvent event);
    }

    static class AccessibilityEventStubImpl implements android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventVersionImpl {
        @java.lang.Override
        public void appendRecord(android.view.accessibility.AccessibilityEvent event, java.lang.Object record) {
        }

        @java.lang.Override
        public java.lang.Object getRecord(android.view.accessibility.AccessibilityEvent event, int index) {
            return null;
        }

        @java.lang.Override
        public void setContentChangeTypes(android.view.accessibility.AccessibilityEvent event, int types) {
        }

        @java.lang.Override
        public int getRecordCount(android.view.accessibility.AccessibilityEvent event) {
            return 0;
        }

        @java.lang.Override
        public int getContentChangeTypes(android.view.accessibility.AccessibilityEvent event) {
            return 0;
        }

        @java.lang.Override
        public void setMovementGranularity(android.view.accessibility.AccessibilityEvent event, int granularity) {
        }

        @java.lang.Override
        public int getMovementGranularity(android.view.accessibility.AccessibilityEvent event) {
            return 0;
        }

        @java.lang.Override
        public void setAction(android.view.accessibility.AccessibilityEvent event, int action) {
        }

        @java.lang.Override
        public int getAction(android.view.accessibility.AccessibilityEvent event) {
            return 0;
        }
    }

    static class AccessibilityEventIcsImpl extends android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventStubImpl {
        @java.lang.Override
        public void appendRecord(android.view.accessibility.AccessibilityEvent event, java.lang.Object record) {
            android.support.v4.view.accessibility.AccessibilityEventCompatIcs.appendRecord(event, record);
        }

        @java.lang.Override
        public java.lang.Object getRecord(android.view.accessibility.AccessibilityEvent event, int index) {
            return android.support.v4.view.accessibility.AccessibilityEventCompatIcs.getRecord(event, index);
        }

        @java.lang.Override
        public int getRecordCount(android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.accessibility.AccessibilityEventCompatIcs.getRecordCount(event);
        }
    }

    static class AccessibilityEventJellyBeanImpl extends android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventIcsImpl {
        @java.lang.Override
        public void setMovementGranularity(android.view.accessibility.AccessibilityEvent event, int granularity) {
            android.support.v4.view.accessibility.AccessibilityEventCompatJellyBean.setMovementGranularity(event, granularity);
        }

        @java.lang.Override
        public int getMovementGranularity(android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.accessibility.AccessibilityEventCompatJellyBean.getMovementGranularity(event);
        }

        @java.lang.Override
        public void setAction(android.view.accessibility.AccessibilityEvent event, int action) {
            android.support.v4.view.accessibility.AccessibilityEventCompatJellyBean.setAction(event, action);
        }

        @java.lang.Override
        public int getAction(android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.accessibility.AccessibilityEventCompatJellyBean.getAction(event);
        }
    }

    static class AccessibilityEventKitKatImpl extends android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventJellyBeanImpl {
        @java.lang.Override
        public void setContentChangeTypes(android.view.accessibility.AccessibilityEvent event, int types) {
            android.support.v4.view.accessibility.AccessibilityEventCompatKitKat.setContentChangeTypes(event, types);
        }

        @java.lang.Override
        public int getContentChangeTypes(android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.accessibility.AccessibilityEventCompatKitKat.getContentChangeTypes(event);
        }
    }

    private static final android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventVersionImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            // KitKat
            IMPL = new android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventKitKatImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                // Jellybean
                IMPL = new android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventJellyBeanImpl();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    // ICS
                    IMPL = new android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventIcsImpl();
                } else {
                    IMPL = new android.support.v4.view.accessibility.AccessibilityEventCompat.AccessibilityEventStubImpl();
                }


    }

    /**
     * Represents the event of a hover enter over a {@link android.view.View}.
     */
    public static final int TYPE_VIEW_HOVER_ENTER = 0x80;

    /**
     * Represents the event of a hover exit over a {@link android.view.View}.
     */
    public static final int TYPE_VIEW_HOVER_EXIT = 0x100;

    /**
     * Represents the event of starting a touch exploration gesture.
     */
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 0x200;

    /**
     * Represents the event of ending a touch exploration gesture.
     */
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 0x400;

    /**
     * Represents the event of changing the content of a window.
     */
    public static final int TYPE_WINDOW_CONTENT_CHANGED = 0x800;

    /**
     * Represents the event of scrolling a view.
     */
    public static final int TYPE_VIEW_SCROLLED = 0x1000;

    /**
     * Represents the event of changing the selection in an {@link android.widget.EditText}.
     */
    public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 0x2000;

    /**
     * Represents the event of an application making an announcement.
     */
    public static final int TYPE_ANNOUNCEMENT = 0x4000;

    /**
     * Represents the event of gaining accessibility focus.
     */
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 0x8000;

    /**
     * Represents the event of clearing accessibility focus.
     */
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 0x10000;

    /**
     * Represents the event of traversing the text of a view at a given movement granularity.
     */
    public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 0x20000;

    /**
     * Represents the event of beginning gesture detection.
     */
    public static final int TYPE_GESTURE_DETECTION_START = 0x40000;

    /**
     * Represents the event of ending gesture detection.
     */
    public static final int TYPE_GESTURE_DETECTION_END = 0x80000;

    /**
     * Represents the event of the user starting to touch the screen.
     */
    public static final int TYPE_TOUCH_INTERACTION_START = 0x100000;

    /**
     * Represents the event of the user ending to touch the screen.
     */
    public static final int TYPE_TOUCH_INTERACTION_END = 0x200000;

    /**
     * Represents the event change in the windows shown on the screen.
     */
    public static final int TYPE_WINDOWS_CHANGED = 0x400000;

    /**
     * Represents the event of a context click on a {@link android.view.View}.
     */
    public static final int TYPE_VIEW_CONTEXT_CLICKED = 0x800000;

    /**
     * Represents the event of the assistant currently reading the users screen context.
     */
    public static final int TYPE_ASSIST_READING_CONTEXT = 0x1000000;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The type of change is not defined.
     */
    public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0x0;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * A node in the subtree rooted at the source node was added or removed.
     */
    public static final int CONTENT_CHANGE_TYPE_SUBTREE = 0x1;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The node's text changed.
     */
    public static final int CONTENT_CHANGE_TYPE_TEXT = 0x2;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The node's content description changed.
     */
    public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 0x4;

    /**
     * Mask for {@link AccessibilityEvent} all types.
     *
     * @see AccessibilityEvent#TYPE_VIEW_CLICKED
     * @see AccessibilityEvent#TYPE_VIEW_LONG_CLICKED
     * @see AccessibilityEvent#TYPE_VIEW_SELECTED
     * @see AccessibilityEvent#TYPE_VIEW_FOCUSED
     * @see AccessibilityEvent#TYPE_VIEW_TEXT_CHANGED
     * @see AccessibilityEvent#TYPE_WINDOW_STATE_CHANGED
     * @see AccessibilityEvent#TYPE_NOTIFICATION_STATE_CHANGED
     * @see #TYPE_VIEW_HOVER_ENTER
     * @see #TYPE_VIEW_HOVER_EXIT
     * @see #TYPE_TOUCH_EXPLORATION_GESTURE_START
     * @see #TYPE_TOUCH_EXPLORATION_GESTURE_END
     * @see #TYPE_WINDOW_CONTENT_CHANGED
     * @see #TYPE_VIEW_SCROLLED
     * @see #TYPE_VIEW_TEXT_SELECTION_CHANGED
     * @see #TYPE_ANNOUNCEMENT
     * @see #TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY
     * @see #TYPE_GESTURE_DETECTION_START
     * @see #TYPE_GESTURE_DETECTION_END
     * @see #TYPE_TOUCH_INTERACTION_START
     * @see #TYPE_TOUCH_INTERACTION_END
     */
    public static final int TYPES_ALL_MASK = 0xffffffff;

    /* Hide constructor from clients. */
    private AccessibilityEventCompat() {
    }

    /**
     * Gets the number of records contained in the event.
     *
     * @return The number of records.
     */
    public static int getRecordCount(android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.getRecordCount(event);
    }

    /**
     * Appends an {@link android.view.accessibility.AccessibilityRecord} to the end of
     * event records.
     *
     * @param record
     * 		The record to append.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public static void appendRecord(android.view.accessibility.AccessibilityEvent event, android.support.v4.view.accessibility.AccessibilityRecordCompat record) {
        android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.appendRecord(event, record.getImpl());
    }

    /**
     * Gets the record at a given index.
     *
     * @param index
     * 		The index.
     * @return The record at the specified index.
     */
    public static android.support.v4.view.accessibility.AccessibilityRecordCompat getRecord(android.view.accessibility.AccessibilityEvent event, int index) {
        return new android.support.v4.view.accessibility.AccessibilityRecordCompat(android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.getRecord(event, index));
    }

    /**
     * Creates an {@link AccessibilityRecordCompat} from an {@link AccessibilityEvent}
     * that can be used to manipulate the event properties defined in
     * {@link android.view.accessibility.AccessibilityRecord}.
     * <p>
     * <strong>Note:</strong> Do not call {@link AccessibilityRecordCompat#recycle()} on the
     * returned {@link AccessibilityRecordCompat}. Call {@link AccessibilityEvent#recycle()}
     * in case you want to recycle the event.
     * </p>
     *
     * @param event
     * 		The from which to create a record.
     * @return An {@link AccessibilityRecordCompat}.
     */
    public static android.support.v4.view.accessibility.AccessibilityRecordCompat asRecord(android.view.accessibility.AccessibilityEvent event) {
        return new android.support.v4.view.accessibility.AccessibilityRecordCompat(event);
    }

    /**
     * Sets the bit mask of node tree changes signaled by an
     * {@link #TYPE_WINDOW_CONTENT_CHANGED} event.
     *
     * @param changeTypes
     * 		The bit mask of change types.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     * @see #getContentChangeTypes(AccessibilityEvent)
     */
    public static void setContentChangeTypes(android.view.accessibility.AccessibilityEvent event, int changeTypes) {
        android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.setContentChangeTypes(event, changeTypes);
    }

    /**
     * Gets the bit mask of change types signaled by an
     * {@link #TYPE_WINDOW_CONTENT_CHANGED} event. A single event may represent
     * multiple change types.
     *
     * @return The bit mask of change types. One or more of:
    <ul>
    <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION}
    <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_SUBTREE}
    <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_TEXT}
    <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_UNDEFINED}
    </ul>
     */
    public static int getContentChangeTypes(android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.getContentChangeTypes(event);
    }

    /**
     * Sets the movement granularity that was traversed.
     *
     * @param granularity
     * 		The granularity.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setMovementGranularity(android.view.accessibility.AccessibilityEvent event, int granularity) {
        android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.setMovementGranularity(event, granularity);
    }

    /**
     * Gets the movement granularity that was traversed.
     *
     * @return The granularity.
     */
    public int getMovementGranularity(android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.getMovementGranularity(event);
    }

    /**
     * Sets the performed action that triggered this event.
     * <p>
     * Valid actions are defined in {@link AccessibilityNodeInfoCompat}:
     * <ul>
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_ACCESSIBILITY_FOCUS}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLEAR_ACCESSIBILITY_FOCUS}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLEAR_FOCUS}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLEAR_SELECTION}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLICK}
     * <li>etc.
     * </ul>
     *
     * @param action
     * 		The action.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     * @see AccessibilityNodeInfoCompat#performAction(int)
     */
    public void setAction(android.view.accessibility.AccessibilityEvent event, int action) {
        android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.setAction(event, action);
    }

    /**
     * Gets the performed action that triggered this event.
     *
     * @return The action.
     */
    public int getAction(android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.accessibility.AccessibilityEventCompat.IMPL.getAction(event);
    }
}

