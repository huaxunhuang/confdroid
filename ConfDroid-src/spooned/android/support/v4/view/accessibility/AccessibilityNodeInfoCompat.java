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
 * Helper for accessing {@link android.view.accessibility.AccessibilityNodeInfo}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public class AccessibilityNodeInfoCompat {
    public static class AccessibilityActionCompat {
        /**
         * Action that gives input focus to the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_FOCUS = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_FOCUS, null);

        /**
         * Action that clears input focus of the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_FOCUS = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLEAR_FOCUS, null);

        /**
         * Action that selects the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SELECT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SELECT, null);

        /**
         * Action that deselects the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_SELECTION = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLEAR_SELECTION, null);

        /**
         * Action that clicks on the node info.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLICK = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLICK, null);

        /**
         * Action that long clicks on the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_LONG_CLICK = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_LONG_CLICK, null);

        /**
         * Action that gives accessibility focus to the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS, null);

        /**
         * Action that clears accessibility focus of the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS, null);

        /**
         * Action that requests to go to the next entity in this node's text
         * at a given movement granularity. For example, move to the next character,
         * word, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT},
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN}<br>
         * <strong>Example:</strong> Move to the previous character and do not extend selection.
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
         *           AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_CHARACTER);
         *   arguments.putBoolean(
         *           AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, false);
         *   info.performAction(
         *           AccessibilityActionCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY.getId(),
         *           arguments);
         * </code></pre></p>
         * </p>
         *
         * @see AccessibilityNodeInfoCompat#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         * @see AccessibilityNodeInfoCompat#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         * @see AccessibilityNodeInfoCompat#setMovementGranularities(int)
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         * @see AccessibilityNodeInfoCompat#getMovementGranularities()
        AccessibilityNodeInfoCompat.getMovementGranularities()
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_CHARACTER
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_CHARACTER
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_WORD
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_WORD
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_LINE
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_LINE
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_PARAGRAPH
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PARAGRAPH
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_PAGE
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PAGE
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, null);

        /**
         * Action that requests to go to the previous entity in this node's text
         * at a given movement granularity. For example, move to the next character,
         * word, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT},
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN}<br>
         * <strong>Example:</strong> Move to the next character and do not extend selection.
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
         *           AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_CHARACTER);
         *   arguments.putBoolean(
         *           AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, false);
         *   info.performAction(
         *           AccessibilityActionCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY.getId(),
         *           arguments);
         * </code></pre></p>
         * </p>
         *
         * @see AccessibilityNodeInfoCompat#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         * @see AccessibilityNodeInfoCompat#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         * @see AccessibilityNodeInfoCompat#setMovementGranularities(int)
        AccessibilityNodeInfoCompat.setMovementGranularities(int)
         * @see AccessibilityNodeInfoCompat#getMovementGranularities()
        AccessibilityNodeInfoCompat.getMovementGranularities()
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_CHARACTER
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_CHARACTER
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_WORD
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_WORD
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_LINE
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_LINE
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_PARAGRAPH
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PARAGRAPH
         * @see AccessibilityNodeInfoCompat#MOVEMENT_GRANULARITY_PAGE
        AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PAGE
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, null);

        /**
         * Action to move to the next HTML element of a given type. For example, move
         * to the BUTTON, INPUT, TABLE, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_HTML_ELEMENT_STRING
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_HTML_ELEMENT_STRING}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putString(
         *           AccessibilityNodeInfoCompat.ACTION_ARGUMENT_HTML_ELEMENT_STRING, "BUTTON");
         *   info.performAction(
         *           AccessibilityActionCompat.ACTION_NEXT_HTML_ELEMENT.getId(), arguments);
         * </code></pre></p>
         * </p>
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, null);

        /**
         * Action to move to the previous HTML element of a given type. For example, move
         * to the BUTTON, INPUT, TABLE, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_HTML_ELEMENT_STRING
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_HTML_ELEMENT_STRING}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putString(
         *           AccessibilityNodeInfoCompat.ACTION_ARGUMENT_HTML_ELEMENT_STRING, "BUTTON");
         *   info.performAction(
         *           AccessibilityActionCompat.ACTION_PREVIOUS_HTML_ELEMENT.getId(), arguments);
         * </code></pre></p>
         * </p>
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT, null);

        /**
         * Action to scroll the node content forward.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_FORWARD = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD, null);

        /**
         * Action to scroll the node content backward.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_BACKWARD = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD, null);

        /**
         * Action to copy the current selection to the clipboard.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_COPY = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_COPY, null);

        /**
         * Action to paste the current clipboard content.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PASTE = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_PASTE, null);

        /**
         * Action to cut the current selection and place it to the clipboard.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CUT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CUT, null);

        /**
         * Action to set the selection. Performing this action with no arguments
         * clears the selection.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_SELECTION_START_INT
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_START_INT},
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_SELECTION_END_INT
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_END_INT}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_START_INT, 1);
         *   arguments.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_END_INT, 2);
         *   info.performAction(AccessibilityActionCompat.ACTION_SET_SELECTION.getId(), arguments);
         * </code></pre></p>
         * </p>
         *
         * @see AccessibilityNodeInfoCompat#ACTION_ARGUMENT_SELECTION_START_INT
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_START_INT
         * @see AccessibilityNodeInfoCompat#ACTION_ARGUMENT_SELECTION_END_INT
        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SELECTION_END_INT
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_SELECTION = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SET_SELECTION, null);

        /**
         * Action to expand an expandable node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_EXPAND = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_EXPAND, null);

        /**
         * Action to collapse an expandable node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_COLLAPSE = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_COLLAPSE, null);

        /**
         * Action to dismiss a dismissable node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_DISMISS = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_DISMISS, null);

        /**
         * Action that sets the text of the node. Performing the action without argument,
         * using <code> null</code> or empty {@link CharSequence} will clear the text. This
         * action will also put the cursor at the end of text.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE
         *  AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
         *       "android");
         *   info.performAction(AccessibilityActionCompat.ACTION_SET_TEXT.getId(), arguments);
         * </code></pre></p>
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_TEXT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SET_TEXT, null);

        /**
         * Action that requests the node make its bounding rectangle visible
         * on the screen, scrolling if necessary just enough.
         *
         * @see View#requestRectangleOnScreen(Rect)
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SHOW_ON_SCREEN = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionShowOnScreen());

        /**
         * Action that scrolls the node to make the specified collection
         * position visible on screen.
         * <p>
         * <strong>Arguments:</strong>
         * <ul>
         *     <li>{@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_ROW_INT}</li>
         *     <li>{@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_COLUMN_INT}</li>
         * <ul>
         *
         * @see AccessibilityNodeInfoCompat#getCollectionInfo()
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_TO_POSITION = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionScrollToPosition());

        /**
         * Action to scroll the node content up.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_UP = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionScrollUp());

        /**
         * Action to scroll the node content left.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_LEFT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionScrollLeft());

        /**
         * Action to scroll the node content down.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_DOWN = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionScrollDown());

        /**
         * Action to scroll the node content right.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_RIGHT = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionScrollRight());

        /**
         * Action that context clicks the node.
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CONTEXT_CLICK = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionContextClick());

        /**
         * Action that sets progress between {@link RangeInfoCompat#getMin() RangeInfo.getMin()} and
         * {@link RangeInfoCompat#getMax() RangeInfo.getMax()}. It should use the same value type as
         * {@link RangeInfoCompat#getType() RangeInfo.getType()}
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfoCompat#ACTION_ARGUMENT_PROGRESS_VALUE}
         *
         * @see RangeInfoCompat
         */
        public static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_PROGRESS = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionSetProgress());

        final java.lang.Object mAction;

        /**
         * Creates a new instance.
         *
         * @param actionId
         * 		The action id.
         * @param label
         * 		The action label.
         */
        public AccessibilityActionCompat(int actionId, java.lang.CharSequence label) {
            this(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.newAccessibilityAction(actionId, label));
        }

        AccessibilityActionCompat(java.lang.Object action) {
            mAction = action;
        }

        /**
         * Gets the id for this action.
         *
         * @return The action id.
         */
        public int getId() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionId(mAction);
        }

        /**
         * Gets the label for this action. Its purpose is to describe the
         * action to user.
         *
         * @return The label.
         */
        public java.lang.CharSequence getLabel() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionLabel(mAction);
        }
    }

    /**
     * Class with information if a node is a collection.
     * <p>
     * A collection of items has rows and columns and may be hierarchical.
     * For example, a horizontal list is a collection with one column, as
     * many rows as the list items, and is not hierarchical; A table is a
     * collection with several rows, several columns, and is not hierarchical;
     * A vertical tree is a hierarchical collection with one column and
     * as many rows as the first level children.
     * </p>
     */
    public static class CollectionInfoCompat {
        /**
         * Selection mode where items are not selectable.
         */
        public static final int SELECTION_MODE_NONE = 0;

        /**
         * Selection mode where a single item may be selected.
         */
        public static final int SELECTION_MODE_SINGLE = 1;

        /**
         * Selection mode where multiple items may be selected.
         */
        public static final int SELECTION_MODE_MULTIPLE = 2;

        final java.lang.Object mInfo;

        /**
         * Returns a cached instance if such is available otherwise a new one.
         *
         * @param rowCount
         * 		The number of rows.
         * @param columnCount
         * 		The number of columns.
         * @param hierarchical
         * 		Whether the collection is hierarchical.
         * @param selectionMode
         * 		The collection's selection mode, one of:
         * 		<ul>
         * 		<li>{@link #SELECTION_MODE_NONE}
         * 		<li>{@link #SELECTION_MODE_SINGLE}
         * 		<li>{@link #SELECTION_MODE_MULTIPLE}
         * 		</ul>
         * @return An instance.
         */
        public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat obtain(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(rowCount, columnCount, hierarchical, selectionMode));
        }

        /**
         * Returns a cached instance if such is available otherwise a new one.
         *
         * @param rowCount
         * 		The number of rows.
         * @param columnCount
         * 		The number of columns.
         * @param hierarchical
         * 		Whether the collection is hierarchical.
         * @return An instance.
         */
        public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat obtain(int rowCount, int columnCount, boolean hierarchical) {
            return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(rowCount, columnCount, hierarchical));
        }

        CollectionInfoCompat(java.lang.Object info) {
            mInfo = info;
        }

        /**
         * Gets the number of columns.
         *
         * @return The column count.
         */
        public int getColumnCount() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionInfoColumnCount(mInfo);
        }

        /**
         * Gets the number of rows.
         *
         * @return The row count.
         */
        public int getRowCount() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionInfoRowCount(mInfo);
        }

        /**
         * Gets if the collection is a hierarchically ordered.
         *
         * @return Whether the collection is hierarchical.
         */
        public boolean isHierarchical() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isCollectionInfoHierarchical(mInfo);
        }

        /**
         * Gets the collection's selection mode.
         *
         * @return The collection's selection mode, one of:
        <ul>
        <li>{@link #SELECTION_MODE_NONE}
        <li>{@link #SELECTION_MODE_SINGLE}
        <li>{@link #SELECTION_MODE_MULTIPLE}
        </ul>
         */
        public int getSelectionMode() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionInfoSelectionMode(mInfo);
        }
    }

    /**
     * Class with information if a node is a collection item.
     * <p>
     * A collection item is contained in a collection, it starts at
     * a given row and column in the collection, and spans one or
     * more rows and columns. For example, a header of two related
     * table columns starts at the first row and the first column,
     * spans one row and two columns.
     * </p>
     */
    public static class CollectionItemInfoCompat {
        final java.lang.Object mInfo;

        /**
         * Returns a cached instance if such is available otherwise a new one.
         *
         * @param rowIndex
         * 		The row index at which the item is located.
         * @param rowSpan
         * 		The number of rows the item spans.
         * @param columnIndex
         * 		The column index at which the item is located.
         * @param columnSpan
         * 		The number of columns the item spans.
         * @param heading
         * 		Whether the item is a heading.
         * @param selected
         * 		Whether the item is selected.
         * @return An instance.
         */
        public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat obtain(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading, selected));
        }

        /**
         * Returns a cached instance if such is available otherwise a new one.
         *
         * @param rowIndex
         * 		The row index at which the item is located.
         * @param rowSpan
         * 		The number of rows the item spans.
         * @param columnIndex
         * 		The column index at which the item is located.
         * @param columnSpan
         * 		The number of columns the item spans.
         * @param heading
         * 		Whether the item is a heading.
         * @return An instance.
         */
        public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat obtain(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading) {
            return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading));
        }

        CollectionItemInfoCompat(java.lang.Object info) {
            mInfo = info;
        }

        /**
         * Gets the column index at which the item is located.
         *
         * @return The column index.
         */
        public int getColumnIndex() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnIndex(mInfo);
        }

        /**
         * Gets the number of columns the item spans.
         *
         * @return The column span.
         */
        public int getColumnSpan() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnSpan(mInfo);
        }

        /**
         * Gets the row index at which the item is located.
         *
         * @return The row index.
         */
        public int getRowIndex() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowIndex(mInfo);
        }

        /**
         * Gets the number of rows the item spans.
         *
         * @return The row span.
         */
        public int getRowSpan() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowSpan(mInfo);
        }

        /**
         * Gets if the collection item is a heading. For example, section
         * heading, table header, etc.
         *
         * @return If the item is a heading.
         */
        public boolean isHeading() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isCollectionItemHeading(mInfo);
        }

        /**
         * Gets if the collection item is selected.
         *
         * @return If the item is selected.
         */
        public boolean isSelected() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isCollectionItemSelected(mInfo);
        }
    }

    /**
     * Class with information if a node is a range.
     */
    public static class RangeInfoCompat {
        /**
         * Range type: integer.
         */
        public static final int RANGE_TYPE_INT = 0;

        /**
         * Range type: float.
         */
        public static final int RANGE_TYPE_FLOAT = 1;

        /**
         * Range type: percent with values from zero to one.
         */
        public static final int RANGE_TYPE_PERCENT = 2;

        /**
         * Obtains a cached instance if such is available otherwise a new one.
         *
         * @param type
         * 		The type of the range.
         * @param min
         * 		The min value.
         * @param max
         * 		The max value.
         * @param current
         * 		The current value.
         * @return The instance
         */
        public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.RangeInfoCompat obtain(int type, float min, float max, float current) {
            return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.RangeInfoCompat(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtainRangeInfo(type, min, max, current));
        }

        final java.lang.Object mInfo;

        RangeInfoCompat(java.lang.Object info) {
            mInfo = info;
        }

        /**
         * Gets the current value.
         *
         * @return The current value.
         */
        public float getCurrent() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.RangeInfo.getCurrent(mInfo);
        }

        /**
         * Gets the max value.
         *
         * @return The max value.
         */
        public float getMax() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.RangeInfo.getMax(mInfo);
        }

        /**
         * Gets the min value.
         *
         * @return The min value.
         */
        public float getMin() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.RangeInfo.getMin(mInfo);
        }

        /**
         * Gets the range type.
         *
         * @return The range type.
         * @see #RANGE_TYPE_INT
         * @see #RANGE_TYPE_FLOAT
         * @see #RANGE_TYPE_PERCENT
         */
        public int getType() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.RangeInfo.getType(mInfo);
        }
    }

    interface AccessibilityNodeInfoImpl {
        java.lang.Object newAccessibilityAction(int actionId, java.lang.CharSequence label);

        java.lang.Object obtain();

        java.lang.Object obtain(android.view.View source);

        java.lang.Object obtain(java.lang.Object info);

        java.lang.Object obtain(android.view.View root, int virtualDescendantId);

        void setSource(java.lang.Object info, android.view.View source);

        void setSource(java.lang.Object info, android.view.View root, int virtualDescendantId);

        java.lang.Object findFocus(java.lang.Object info, int focus);

        java.lang.Object focusSearch(java.lang.Object info, int direction);

        int getWindowId(java.lang.Object info);

        int getChildCount(java.lang.Object info);

        java.lang.Object getChild(java.lang.Object info, int index);

        void addChild(java.lang.Object info, android.view.View child);

        void addChild(java.lang.Object info, android.view.View child, int virtualDescendantId);

        boolean removeChild(java.lang.Object info, android.view.View child);

        boolean removeChild(java.lang.Object info, android.view.View root, int virtualDescendantId);

        int getActions(java.lang.Object info);

        void addAction(java.lang.Object info, int action);

        void addAction(java.lang.Object info, java.lang.Object action);

        boolean removeAction(java.lang.Object info, java.lang.Object action);

        int getAccessibilityActionId(java.lang.Object action);

        java.lang.CharSequence getAccessibilityActionLabel(java.lang.Object action);

        boolean performAction(java.lang.Object info, int action);

        boolean performAction(java.lang.Object info, int action, android.os.Bundle arguments);

        void setMovementGranularities(java.lang.Object info, int granularities);

        int getMovementGranularities(java.lang.Object info);

        java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.Object info, java.lang.String text);

        java.lang.Object getParent(java.lang.Object info);

        void setParent(java.lang.Object info, android.view.View root, int virtualDescendantId);

        void setParent(java.lang.Object info, android.view.View parent);

        void getBoundsInParent(java.lang.Object info, android.graphics.Rect outBounds);

        void setBoundsInParent(java.lang.Object info, android.graphics.Rect bounds);

        void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds);

        void setBoundsInScreen(java.lang.Object info, android.graphics.Rect bounds);

        boolean isCheckable(java.lang.Object info);

        void setCheckable(java.lang.Object info, boolean checkable);

        boolean isChecked(java.lang.Object info);

        void setChecked(java.lang.Object info, boolean checked);

        boolean isFocusable(java.lang.Object info);

        void setFocusable(java.lang.Object info, boolean focusable);

        boolean isFocused(java.lang.Object info);

        void setFocused(java.lang.Object info, boolean focused);

        boolean isVisibleToUser(java.lang.Object info);

        void setVisibleToUser(java.lang.Object info, boolean visibleToUser);

        boolean isAccessibilityFocused(java.lang.Object info);

        void setAccessibilityFocused(java.lang.Object info, boolean focused);

        boolean isSelected(java.lang.Object info);

        void setSelected(java.lang.Object info, boolean selected);

        boolean isClickable(java.lang.Object info);

        void setClickable(java.lang.Object info, boolean clickable);

        boolean isLongClickable(java.lang.Object info);

        void setLongClickable(java.lang.Object info, boolean longClickable);

        boolean isEnabled(java.lang.Object info);

        void setEnabled(java.lang.Object info, boolean enabled);

        boolean isPassword(java.lang.Object info);

        void setPassword(java.lang.Object info, boolean password);

        boolean isScrollable(java.lang.Object info);

        void setScrollable(java.lang.Object info, boolean scrollable);

        java.lang.CharSequence getPackageName(java.lang.Object info);

        void setPackageName(java.lang.Object info, java.lang.CharSequence packageName);

        java.lang.CharSequence getClassName(java.lang.Object info);

        void setClassName(java.lang.Object info, java.lang.CharSequence className);

        java.lang.CharSequence getText(java.lang.Object info);

        void setText(java.lang.Object info, java.lang.CharSequence text);

        java.lang.CharSequence getContentDescription(java.lang.Object info);

        void setContentDescription(java.lang.Object info, java.lang.CharSequence contentDescription);

        void recycle(java.lang.Object info);

        java.lang.String getViewIdResourceName(java.lang.Object info);

        void setViewIdResourceName(java.lang.Object info, java.lang.String viewId);

        int getLiveRegion(java.lang.Object info);

        void setLiveRegion(java.lang.Object info, int mode);

        java.lang.Object getCollectionInfo(java.lang.Object info);

        void setCollectionInfo(java.lang.Object info, java.lang.Object collectionInfo);

        java.lang.Object getCollectionItemInfo(java.lang.Object info);

        void setCollectionItemInfo(java.lang.Object info, java.lang.Object collectionItemInfo);

        java.lang.Object getRangeInfo(java.lang.Object info);

        void setRangeInfo(java.lang.Object info, java.lang.Object rangeInfo);

        java.util.List<java.lang.Object> getActionList(java.lang.Object info);

        java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode);

        java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical);

        int getCollectionInfoColumnCount(java.lang.Object info);

        int getCollectionInfoRowCount(java.lang.Object info);

        boolean isCollectionInfoHierarchical(java.lang.Object info);

        int getCollectionInfoSelectionMode(java.lang.Object info);

        java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected);

        java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading);

        int getCollectionItemColumnIndex(java.lang.Object info);

        int getCollectionItemColumnSpan(java.lang.Object info);

        int getCollectionItemRowIndex(java.lang.Object info);

        int getCollectionItemRowSpan(java.lang.Object info);

        boolean isCollectionItemHeading(java.lang.Object info);

        boolean isCollectionItemSelected(java.lang.Object info);

        java.lang.Object obtainRangeInfo(int type, float min, float max, float current);

        java.lang.Object getTraversalBefore(java.lang.Object info);

        void setTraversalBefore(java.lang.Object info, android.view.View view);

        void setTraversalBefore(java.lang.Object info, android.view.View root, int virtualDescendantId);

        java.lang.Object getTraversalAfter(java.lang.Object info);

        void setTraversalAfter(java.lang.Object info, android.view.View view);

        void setTraversalAfter(java.lang.Object info, android.view.View root, int virtualDescendantId);

        void setContentInvalid(java.lang.Object info, boolean contentInvalid);

        boolean isContentInvalid(java.lang.Object info);

        void setError(java.lang.Object info, java.lang.CharSequence error);

        java.lang.CharSequence getError(java.lang.Object info);

        void setLabelFor(java.lang.Object info, android.view.View labeled);

        void setLabelFor(java.lang.Object info, android.view.View root, int virtualDescendantId);

        java.lang.Object getLabelFor(java.lang.Object info);

        void setLabeledBy(java.lang.Object info, android.view.View labeled);

        void setLabeledBy(java.lang.Object info, android.view.View root, int virtualDescendantId);

        java.lang.Object getLabeledBy(java.lang.Object info);

        boolean canOpenPopup(java.lang.Object info);

        void setCanOpenPopup(java.lang.Object info, boolean opensPopup);

        java.util.List<java.lang.Object> findAccessibilityNodeInfosByViewId(java.lang.Object info, java.lang.String viewId);

        android.os.Bundle getExtras(java.lang.Object info);

        int getInputType(java.lang.Object info);

        void setInputType(java.lang.Object info, int inputType);

        void setMaxTextLength(java.lang.Object info, int max);

        int getMaxTextLength(java.lang.Object info);

        void setTextSelection(java.lang.Object info, int start, int end);

        int getTextSelectionStart(java.lang.Object info);

        int getTextSelectionEnd(java.lang.Object info);

        java.lang.Object getWindow(java.lang.Object info);

        boolean isDismissable(java.lang.Object info);

        void setDismissable(java.lang.Object info, boolean dismissable);

        boolean isEditable(java.lang.Object info);

        void setEditable(java.lang.Object info, boolean editable);

        int getDrawingOrder(java.lang.Object info);

        void setDrawingOrder(java.lang.Object info, int drawingOrderInParent);

        boolean isImportantForAccessibility(java.lang.Object info);

        void setImportantForAccessibility(java.lang.Object info, boolean importantForAccessibility);

        boolean isMultiLine(java.lang.Object info);

        void setMultiLine(java.lang.Object info, boolean multiLine);

        boolean refresh(java.lang.Object info);

        java.lang.CharSequence getRoleDescription(java.lang.Object info);

        void setRoleDescription(java.lang.Object info, java.lang.CharSequence roleDescription);

        java.lang.Object getActionScrollToPosition();

        java.lang.Object getActionSetProgress();

        boolean isContextClickable(java.lang.Object info);

        void setContextClickable(java.lang.Object info, boolean contextClickable);

        java.lang.Object getActionShowOnScreen();

        java.lang.Object getActionScrollUp();

        java.lang.Object getActionScrollDown();

        java.lang.Object getActionScrollLeft();

        java.lang.Object getActionScrollRight();

        java.lang.Object getActionContextClick();
    }

    static class AccessibilityNodeInfoStubImpl implements android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoImpl {
        @java.lang.Override
        public java.lang.Object newAccessibilityAction(int actionId, java.lang.CharSequence label) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtain() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtain(android.view.View source) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtain(android.view.View root, int virtualDescendantId) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtain(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void addAction(java.lang.Object info, int action) {
        }

        @java.lang.Override
        public void addAction(java.lang.Object info, java.lang.Object action) {
        }

        @java.lang.Override
        public boolean removeAction(java.lang.Object info, java.lang.Object action) {
            return false;
        }

        @java.lang.Override
        public int getAccessibilityActionId(java.lang.Object action) {
            return 0;
        }

        @java.lang.Override
        public java.lang.CharSequence getAccessibilityActionLabel(java.lang.Object action) {
            return null;
        }

        @java.lang.Override
        public void addChild(java.lang.Object info, android.view.View child) {
        }

        @java.lang.Override
        public void addChild(java.lang.Object info, android.view.View child, int virtualDescendantId) {
        }

        @java.lang.Override
        public boolean removeChild(java.lang.Object info, android.view.View child) {
            return false;
        }

        @java.lang.Override
        public boolean removeChild(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            return false;
        }

        @java.lang.Override
        public java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.Object info, java.lang.String text) {
            return java.util.Collections.emptyList();
        }

        @java.lang.Override
        public int getActions(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public void getBoundsInParent(java.lang.Object info, android.graphics.Rect outBounds) {
        }

        @java.lang.Override
        public void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds) {
        }

        @java.lang.Override
        public java.lang.Object getChild(java.lang.Object info, int index) {
            return null;
        }

        @java.lang.Override
        public int getChildCount(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public java.lang.CharSequence getClassName(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.CharSequence getContentDescription(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.CharSequence getPackageName(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getParent(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.CharSequence getText(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public int getWindowId(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public boolean isCheckable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isChecked(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isClickable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isEnabled(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isFocusable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isFocused(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isVisibleToUser(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isAccessibilityFocused(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isLongClickable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isPassword(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isScrollable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isSelected(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean performAction(java.lang.Object info, int action) {
            return false;
        }

        @java.lang.Override
        public boolean performAction(java.lang.Object info, int action, android.os.Bundle arguments) {
            return false;
        }

        @java.lang.Override
        public void setMovementGranularities(java.lang.Object info, int granularities) {
        }

        @java.lang.Override
        public int getMovementGranularities(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public void setBoundsInParent(java.lang.Object info, android.graphics.Rect bounds) {
        }

        @java.lang.Override
        public void setBoundsInScreen(java.lang.Object info, android.graphics.Rect bounds) {
        }

        @java.lang.Override
        public void setCheckable(java.lang.Object info, boolean checkable) {
        }

        @java.lang.Override
        public void setChecked(java.lang.Object info, boolean checked) {
        }

        @java.lang.Override
        public void setClassName(java.lang.Object info, java.lang.CharSequence className) {
        }

        @java.lang.Override
        public void setClickable(java.lang.Object info, boolean clickable) {
        }

        @java.lang.Override
        public void setContentDescription(java.lang.Object info, java.lang.CharSequence contentDescription) {
        }

        @java.lang.Override
        public void setEnabled(java.lang.Object info, boolean enabled) {
        }

        @java.lang.Override
        public void setFocusable(java.lang.Object info, boolean focusable) {
        }

        @java.lang.Override
        public void setFocused(java.lang.Object info, boolean focused) {
        }

        @java.lang.Override
        public void setVisibleToUser(java.lang.Object info, boolean visibleToUser) {
        }

        @java.lang.Override
        public void setAccessibilityFocused(java.lang.Object info, boolean focused) {
        }

        @java.lang.Override
        public void setLongClickable(java.lang.Object info, boolean longClickable) {
        }

        @java.lang.Override
        public void setPackageName(java.lang.Object info, java.lang.CharSequence packageName) {
        }

        @java.lang.Override
        public void setParent(java.lang.Object info, android.view.View parent) {
        }

        @java.lang.Override
        public void setPassword(java.lang.Object info, boolean password) {
        }

        @java.lang.Override
        public void setScrollable(java.lang.Object info, boolean scrollable) {
        }

        @java.lang.Override
        public void setSelected(java.lang.Object info, boolean selected) {
        }

        @java.lang.Override
        public void setSource(java.lang.Object info, android.view.View source) {
        }

        @java.lang.Override
        public void setSource(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public java.lang.Object findFocus(java.lang.Object info, int focus) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object focusSearch(java.lang.Object info, int direction) {
            return null;
        }

        @java.lang.Override
        public void setText(java.lang.Object info, java.lang.CharSequence text) {
        }

        @java.lang.Override
        public void recycle(java.lang.Object info) {
        }

        @java.lang.Override
        public void setParent(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public java.lang.String getViewIdResourceName(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setViewIdResourceName(java.lang.Object info, java.lang.String viewId) {
        }

        @java.lang.Override
        public int getLiveRegion(java.lang.Object info) {
            return android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_NONE;
        }

        @java.lang.Override
        public void setLiveRegion(java.lang.Object info, int mode) {
            // No-op
        }

        @java.lang.Override
        public java.lang.Object getCollectionInfo(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setCollectionInfo(java.lang.Object info, java.lang.Object collectionInfo) {
        }

        @java.lang.Override
        public java.lang.Object getCollectionItemInfo(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setCollectionItemInfo(java.lang.Object info, java.lang.Object collectionItemInfo) {
        }

        @java.lang.Override
        public java.lang.Object getRangeInfo(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setRangeInfo(java.lang.Object info, java.lang.Object rangeInfo) {
        }

        @java.lang.Override
        public java.util.List<java.lang.Object> getActionList(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical) {
            return null;
        }

        @java.lang.Override
        public int getCollectionInfoColumnCount(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public int getCollectionInfoRowCount(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public boolean isCollectionInfoHierarchical(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading) {
            return null;
        }

        @java.lang.Override
        public int getCollectionItemColumnIndex(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public int getCollectionItemColumnSpan(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public int getCollectionItemRowIndex(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public int getCollectionItemRowSpan(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public boolean isCollectionItemHeading(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public boolean isCollectionItemSelected(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public java.lang.Object obtainRangeInfo(int type, float min, float max, float current) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getTraversalBefore(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setTraversalBefore(java.lang.Object info, android.view.View view) {
        }

        @java.lang.Override
        public void setTraversalBefore(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public java.lang.Object getTraversalAfter(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setTraversalAfter(java.lang.Object info, android.view.View view) {
        }

        @java.lang.Override
        public void setTraversalAfter(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public void setContentInvalid(java.lang.Object info, boolean contentInvalid) {
        }

        @java.lang.Override
        public boolean isContentInvalid(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public void setError(java.lang.Object info, java.lang.CharSequence error) {
        }

        @java.lang.Override
        public java.lang.CharSequence getError(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setLabelFor(java.lang.Object info, android.view.View labeled) {
        }

        @java.lang.Override
        public void setLabelFor(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public java.lang.Object getLabelFor(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setLabeledBy(java.lang.Object info, android.view.View labeled) {
        }

        @java.lang.Override
        public void setLabeledBy(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        }

        @java.lang.Override
        public java.lang.Object getLabeledBy(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public boolean canOpenPopup(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public void setCanOpenPopup(java.lang.Object info, boolean opensPopup) {
        }

        @java.lang.Override
        public java.util.List<java.lang.Object> findAccessibilityNodeInfosByViewId(java.lang.Object info, java.lang.String viewId) {
            return java.util.Collections.emptyList();
        }

        @java.lang.Override
        public android.os.Bundle getExtras(java.lang.Object info) {
            return new android.os.Bundle();
        }

        @java.lang.Override
        public int getInputType(java.lang.Object info) {
            return android.text.InputType.TYPE_NULL;
        }

        @java.lang.Override
        public void setInputType(java.lang.Object info, int inputType) {
        }

        @java.lang.Override
        public void setMaxTextLength(java.lang.Object info, int max) {
        }

        @java.lang.Override
        public int getMaxTextLength(java.lang.Object info) {
            return -1;
        }

        @java.lang.Override
        public void setTextSelection(java.lang.Object info, int start, int end) {
        }

        @java.lang.Override
        public int getTextSelectionStart(java.lang.Object info) {
            return -1;
        }

        @java.lang.Override
        public int getTextSelectionEnd(java.lang.Object info) {
            return -1;
        }

        @java.lang.Override
        public java.lang.Object getWindow(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public boolean isDismissable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public void setDismissable(java.lang.Object info, boolean dismissable) {
        }

        @java.lang.Override
        public boolean isEditable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public void setEditable(java.lang.Object info, boolean editable) {
        }

        @java.lang.Override
        public boolean isMultiLine(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public void setMultiLine(java.lang.Object info, boolean multiLine) {
        }

        @java.lang.Override
        public boolean refresh(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public java.lang.CharSequence getRoleDescription(java.lang.Object info) {
            return null;
        }

        @java.lang.Override
        public void setRoleDescription(java.lang.Object info, java.lang.CharSequence roleDescription) {
        }

        @java.lang.Override
        public java.lang.Object getActionScrollToPosition() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getActionSetProgress() {
            return null;
        }

        @java.lang.Override
        public boolean isContextClickable(java.lang.Object info) {
            return false;
        }

        @java.lang.Override
        public void setContextClickable(java.lang.Object info, boolean contextClickable) {
            // Do nothing.
        }

        @java.lang.Override
        public java.lang.Object getActionShowOnScreen() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getActionScrollUp() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getActionScrollDown() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getActionScrollLeft() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getActionScrollRight() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getActionContextClick() {
            return null;
        }

        @java.lang.Override
        public int getCollectionInfoSelectionMode(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public int getDrawingOrder(java.lang.Object info) {
            return 0;
        }

        @java.lang.Override
        public void setDrawingOrder(java.lang.Object info, int drawingOrderInParent) {
        }

        @java.lang.Override
        public boolean isImportantForAccessibility(java.lang.Object info) {
            return true;
        }

        @java.lang.Override
        public void setImportantForAccessibility(java.lang.Object info, boolean importantForAccessibility) {
        }
    }

    static class AccessibilityNodeInfoIcsImpl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoStubImpl {
        @java.lang.Override
        public java.lang.Object obtain() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.obtain();
        }

        @java.lang.Override
        public java.lang.Object obtain(android.view.View source) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.obtain(source);
        }

        @java.lang.Override
        public java.lang.Object obtain(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.obtain(info);
        }

        @java.lang.Override
        public void addAction(java.lang.Object info, int action) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.addAction(info, action);
        }

        @java.lang.Override
        public void addChild(java.lang.Object info, android.view.View child) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.addChild(info, child);
        }

        @java.lang.Override
        public java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.Object info, java.lang.String text) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.findAccessibilityNodeInfosByText(info, text);
        }

        @java.lang.Override
        public int getActions(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getActions(info);
        }

        @java.lang.Override
        public void getBoundsInParent(java.lang.Object info, android.graphics.Rect outBounds) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getBoundsInParent(info, outBounds);
        }

        @java.lang.Override
        public void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getBoundsInScreen(info, outBounds);
        }

        @java.lang.Override
        public java.lang.Object getChild(java.lang.Object info, int index) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getChild(info, index);
        }

        @java.lang.Override
        public int getChildCount(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getChildCount(info);
        }

        @java.lang.Override
        public java.lang.CharSequence getClassName(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getClassName(info);
        }

        @java.lang.Override
        public java.lang.CharSequence getContentDescription(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getContentDescription(info);
        }

        @java.lang.Override
        public java.lang.CharSequence getPackageName(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getPackageName(info);
        }

        @java.lang.Override
        public java.lang.Object getParent(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getParent(info);
        }

        @java.lang.Override
        public java.lang.CharSequence getText(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getText(info);
        }

        @java.lang.Override
        public int getWindowId(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.getWindowId(info);
        }

        @java.lang.Override
        public boolean isCheckable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isCheckable(info);
        }

        @java.lang.Override
        public boolean isChecked(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isChecked(info);
        }

        @java.lang.Override
        public boolean isClickable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isClickable(info);
        }

        @java.lang.Override
        public boolean isEnabled(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isEnabled(info);
        }

        @java.lang.Override
        public boolean isFocusable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isFocusable(info);
        }

        @java.lang.Override
        public boolean isFocused(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isFocused(info);
        }

        @java.lang.Override
        public boolean isLongClickable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isLongClickable(info);
        }

        @java.lang.Override
        public boolean isPassword(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isPassword(info);
        }

        @java.lang.Override
        public boolean isScrollable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isScrollable(info);
        }

        @java.lang.Override
        public boolean isSelected(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.isSelected(info);
        }

        @java.lang.Override
        public boolean performAction(java.lang.Object info, int action) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.performAction(info, action);
        }

        @java.lang.Override
        public void setBoundsInParent(java.lang.Object info, android.graphics.Rect bounds) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setBoundsInParent(info, bounds);
        }

        @java.lang.Override
        public void setBoundsInScreen(java.lang.Object info, android.graphics.Rect bounds) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setBoundsInScreen(info, bounds);
        }

        @java.lang.Override
        public void setCheckable(java.lang.Object info, boolean checkable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setCheckable(info, checkable);
        }

        @java.lang.Override
        public void setChecked(java.lang.Object info, boolean checked) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setChecked(info, checked);
        }

        @java.lang.Override
        public void setClassName(java.lang.Object info, java.lang.CharSequence className) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setClassName(info, className);
        }

        @java.lang.Override
        public void setClickable(java.lang.Object info, boolean clickable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setClickable(info, clickable);
        }

        @java.lang.Override
        public void setContentDescription(java.lang.Object info, java.lang.CharSequence contentDescription) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setContentDescription(info, contentDescription);
        }

        @java.lang.Override
        public void setEnabled(java.lang.Object info, boolean enabled) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setEnabled(info, enabled);
        }

        @java.lang.Override
        public void setFocusable(java.lang.Object info, boolean focusable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setFocusable(info, focusable);
        }

        @java.lang.Override
        public void setFocused(java.lang.Object info, boolean focused) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setFocused(info, focused);
        }

        @java.lang.Override
        public void setLongClickable(java.lang.Object info, boolean longClickable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setLongClickable(info, longClickable);
        }

        @java.lang.Override
        public void setPackageName(java.lang.Object info, java.lang.CharSequence packageName) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setPackageName(info, packageName);
        }

        @java.lang.Override
        public void setParent(java.lang.Object info, android.view.View parent) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setParent(info, parent);
        }

        @java.lang.Override
        public void setPassword(java.lang.Object info, boolean password) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setPassword(info, password);
        }

        @java.lang.Override
        public void setScrollable(java.lang.Object info, boolean scrollable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setScrollable(info, scrollable);
        }

        @java.lang.Override
        public void setSelected(java.lang.Object info, boolean selected) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setSelected(info, selected);
        }

        @java.lang.Override
        public void setSource(java.lang.Object info, android.view.View source) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setSource(info, source);
        }

        @java.lang.Override
        public void setText(java.lang.Object info, java.lang.CharSequence text) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.setText(info, text);
        }

        @java.lang.Override
        public void recycle(java.lang.Object info) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatIcs.recycle(info);
        }
    }

    static class AccessibilityNodeInfoJellybeanImpl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoIcsImpl {
        @java.lang.Override
        public java.lang.Object obtain(android.view.View root, int virtualDescendantId) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.obtain(root, virtualDescendantId);
        }

        @java.lang.Override
        public java.lang.Object findFocus(java.lang.Object info, int focus) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.findFocus(info, focus);
        }

        @java.lang.Override
        public java.lang.Object focusSearch(java.lang.Object info, int direction) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.focusSearch(info, direction);
        }

        @java.lang.Override
        public void addChild(java.lang.Object info, android.view.View child, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.addChild(info, child, virtualDescendantId);
        }

        @java.lang.Override
        public void setSource(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.setSource(info, root, virtualDescendantId);
        }

        @java.lang.Override
        public boolean isVisibleToUser(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.isVisibleToUser(info);
        }

        @java.lang.Override
        public void setVisibleToUser(java.lang.Object info, boolean visibleToUser) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.setVisibleToUser(info, visibleToUser);
        }

        @java.lang.Override
        public boolean isAccessibilityFocused(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.isAccessibilityFocused(info);
        }

        @java.lang.Override
        public void setAccessibilityFocused(java.lang.Object info, boolean focused) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.setAccesibilityFocused(info, focused);
        }

        @java.lang.Override
        public boolean performAction(java.lang.Object info, int action, android.os.Bundle arguments) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.performAction(info, action, arguments);
        }

        @java.lang.Override
        public void setMovementGranularities(java.lang.Object info, int granularities) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.setMovementGranularities(info, granularities);
        }

        @java.lang.Override
        public int getMovementGranularities(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.getMovementGranularities(info);
        }

        @java.lang.Override
        public void setParent(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellyBean.setParent(info, root, virtualDescendantId);
        }
    }

    static class AccessibilityNodeInfoJellybeanMr1Impl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanImpl {
        @java.lang.Override
        public void setLabelFor(java.lang.Object info, android.view.View labeled) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr1.setLabelFor(info, labeled);
        }

        @java.lang.Override
        public void setLabelFor(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr1.setLabelFor(info, root, virtualDescendantId);
        }

        @java.lang.Override
        public java.lang.Object getLabelFor(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr1.getLabelFor(info);
        }

        @java.lang.Override
        public void setLabeledBy(java.lang.Object info, android.view.View labeled) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr1.setLabeledBy(info, labeled);
        }

        @java.lang.Override
        public void setLabeledBy(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr1.setLabeledBy(info, root, virtualDescendantId);
        }

        @java.lang.Override
        public java.lang.Object getLabeledBy(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr1.getLabeledBy(info);
        }
    }

    static class AccessibilityNodeInfoJellybeanMr2Impl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanMr1Impl {
        @java.lang.Override
        public java.lang.String getViewIdResourceName(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.getViewIdResourceName(info);
        }

        @java.lang.Override
        public void setViewIdResourceName(java.lang.Object info, java.lang.String viewId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.setViewIdResourceName(info, viewId);
        }

        @java.lang.Override
        public java.util.List<java.lang.Object> findAccessibilityNodeInfosByViewId(java.lang.Object info, java.lang.String viewId) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.findAccessibilityNodeInfosByViewId(info, viewId);
        }

        @java.lang.Override
        public void setTextSelection(java.lang.Object info, int start, int end) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.setTextSelection(info, start, end);
        }

        @java.lang.Override
        public int getTextSelectionStart(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.getTextSelectionStart(info);
        }

        @java.lang.Override
        public int getTextSelectionEnd(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.getTextSelectionEnd(info);
        }

        @java.lang.Override
        public boolean isEditable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.isEditable(info);
        }

        @java.lang.Override
        public void setEditable(java.lang.Object info, boolean editable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.setEditable(info, editable);
        }

        @java.lang.Override
        public boolean refresh(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatJellybeanMr2.refresh(info);
        }
    }

    static class AccessibilityNodeInfoKitKatImpl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanMr2Impl {
        @java.lang.Override
        public int getLiveRegion(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getLiveRegion(info);
        }

        @java.lang.Override
        public void setLiveRegion(java.lang.Object info, int mode) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setLiveRegion(info, mode);
        }

        @java.lang.Override
        public java.lang.Object getCollectionInfo(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getCollectionInfo(info);
        }

        @java.lang.Override
        public void setCollectionInfo(java.lang.Object info, java.lang.Object collectionInfo) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setCollectionInfo(info, collectionInfo);
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.obtainCollectionInfo(rowCount, columnCount, hierarchical, selectionMode);
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.obtainCollectionInfo(rowCount, columnCount, hierarchical);
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.obtainCollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading);
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.obtainCollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading);
        }

        @java.lang.Override
        public int getCollectionInfoColumnCount(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionInfo.getColumnCount(info);
        }

        @java.lang.Override
        public int getCollectionInfoRowCount(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionInfo.getRowCount(info);
        }

        @java.lang.Override
        public boolean isCollectionInfoHierarchical(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionInfo.isHierarchical(info);
        }

        @java.lang.Override
        public java.lang.Object getCollectionItemInfo(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getCollectionItemInfo(info);
        }

        @java.lang.Override
        public java.lang.Object getRangeInfo(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getRangeInfo(info);
        }

        @java.lang.Override
        public void setRangeInfo(java.lang.Object info, java.lang.Object rangeInfo) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setRangeInfo(info, rangeInfo);
        }

        @java.lang.Override
        public int getCollectionItemColumnIndex(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getColumnIndex(info);
        }

        @java.lang.Override
        public int getCollectionItemColumnSpan(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getColumnSpan(info);
        }

        @java.lang.Override
        public int getCollectionItemRowIndex(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getRowIndex(info);
        }

        @java.lang.Override
        public int getCollectionItemRowSpan(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.getRowSpan(info);
        }

        @java.lang.Override
        public boolean isCollectionItemHeading(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.CollectionItemInfo.isHeading(info);
        }

        @java.lang.Override
        public void setCollectionItemInfo(java.lang.Object info, java.lang.Object collectionItemInfo) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setCollectionItemInfo(info, collectionItemInfo);
        }

        @java.lang.Override
        public java.lang.Object obtainRangeInfo(int type, float min, float max, float current) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.obtainRangeInfo(type, min, max, current);
        }

        @java.lang.Override
        public void setContentInvalid(java.lang.Object info, boolean contentInvalid) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setContentInvalid(info, contentInvalid);
        }

        @java.lang.Override
        public boolean isContentInvalid(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.isContentInvalid(info);
        }

        @java.lang.Override
        public boolean canOpenPopup(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.canOpenPopup(info);
        }

        @java.lang.Override
        public void setCanOpenPopup(java.lang.Object info, boolean opensPopup) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setCanOpenPopup(info, opensPopup);
        }

        @java.lang.Override
        public android.os.Bundle getExtras(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getExtras(info);
        }

        @java.lang.Override
        public int getInputType(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getInputType(info);
        }

        @java.lang.Override
        public void setInputType(java.lang.Object info, int inputType) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setInputType(info, inputType);
        }

        @java.lang.Override
        public boolean isDismissable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.isDismissable(info);
        }

        @java.lang.Override
        public void setDismissable(java.lang.Object info, boolean dismissable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setDismissable(info, dismissable);
        }

        @java.lang.Override
        public boolean isMultiLine(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.isMultiLine(info);
        }

        @java.lang.Override
        public void setMultiLine(java.lang.Object info, boolean multiLine) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setMultiLine(info, multiLine);
        }

        @java.lang.Override
        public java.lang.CharSequence getRoleDescription(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.getRoleDescription(info);
        }

        @java.lang.Override
        public void setRoleDescription(java.lang.Object info, java.lang.CharSequence roleDescription) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat.setRoleDescription(info, roleDescription);
        }
    }

    static class AccessibilityNodeInfoApi21Impl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoKitKatImpl {
        @java.lang.Override
        public java.lang.Object newAccessibilityAction(int actionId, java.lang.CharSequence label) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.newAccessibilityAction(actionId, label);
        }

        @java.lang.Override
        public java.util.List<java.lang.Object> getActionList(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.getActionList(info);
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.obtainCollectionInfo(rowCount, columnCount, hierarchical, selectionMode);
        }

        @java.lang.Override
        public void addAction(java.lang.Object info, java.lang.Object action) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.addAction(info, action);
        }

        @java.lang.Override
        public boolean removeAction(java.lang.Object info, java.lang.Object action) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.removeAction(info, action);
        }

        @java.lang.Override
        public int getAccessibilityActionId(java.lang.Object action) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.getAccessibilityActionId(action);
        }

        @java.lang.Override
        public java.lang.CharSequence getAccessibilityActionLabel(java.lang.Object action) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.getAccessibilityActionLabel(action);
        }

        @java.lang.Override
        public java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.obtainCollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading, selected);
        }

        @java.lang.Override
        public boolean isCollectionItemSelected(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.CollectionItemInfo.isSelected(info);
        }

        @java.lang.Override
        public java.lang.CharSequence getError(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.getError(info);
        }

        @java.lang.Override
        public void setError(java.lang.Object info, java.lang.CharSequence error) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.setError(info, error);
        }

        @java.lang.Override
        public void setMaxTextLength(java.lang.Object info, int max) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.setMaxTextLength(info, max);
        }

        @java.lang.Override
        public int getMaxTextLength(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.getMaxTextLength(info);
        }

        @java.lang.Override
        public java.lang.Object getWindow(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.getWindow(info);
        }

        @java.lang.Override
        public boolean removeChild(java.lang.Object info, android.view.View child) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.removeChild(info, child);
        }

        @java.lang.Override
        public boolean removeChild(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.removeChild(info, root, virtualDescendantId);
        }

        @java.lang.Override
        public int getCollectionInfoSelectionMode(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi21.CollectionInfo.getSelectionMode(info);
        }
    }

    static class AccessibilityNodeInfoApi22Impl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi21Impl {
        @java.lang.Override
        public java.lang.Object getTraversalBefore(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi22.getTraversalBefore(info);
        }

        @java.lang.Override
        public void setTraversalBefore(java.lang.Object info, android.view.View view) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi22.setTraversalBefore(info, view);
        }

        @java.lang.Override
        public void setTraversalBefore(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi22.setTraversalBefore(info, root, virtualDescendantId);
        }

        @java.lang.Override
        public java.lang.Object getTraversalAfter(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi22.getTraversalAfter(info);
        }

        @java.lang.Override
        public void setTraversalAfter(java.lang.Object info, android.view.View view) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi22.setTraversalAfter(info, view);
        }

        @java.lang.Override
        public void setTraversalAfter(java.lang.Object info, android.view.View root, int virtualDescendantId) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi22.setTraversalAfter(info, root, virtualDescendantId);
        }
    }

    static class AccessibilityNodeInfoApi23Impl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi22Impl {
        @java.lang.Override
        public java.lang.Object getActionScrollToPosition() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionScrollToPosition();
        }

        @java.lang.Override
        public java.lang.Object getActionShowOnScreen() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionShowOnScreen();
        }

        @java.lang.Override
        public java.lang.Object getActionScrollUp() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionScrollUp();
        }

        @java.lang.Override
        public java.lang.Object getActionScrollDown() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionScrollDown();
        }

        @java.lang.Override
        public java.lang.Object getActionScrollLeft() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionScrollLeft();
        }

        @java.lang.Override
        public java.lang.Object getActionScrollRight() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionScrollRight();
        }

        @java.lang.Override
        public java.lang.Object getActionContextClick() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.getActionContextClick();
        }

        @java.lang.Override
        public boolean isContextClickable(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.isContextClickable(info);
        }

        @java.lang.Override
        public void setContextClickable(java.lang.Object info, boolean contextClickable) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi23.setContextClickable(info, contextClickable);
        }
    }

    static class AccessibilityNodeInfoApi24Impl extends android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi23Impl {
        @java.lang.Override
        public java.lang.Object getActionSetProgress() {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi24.getActionSetProgress();
        }

        @java.lang.Override
        public int getDrawingOrder(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi24.getDrawingOrder(info);
        }

        @java.lang.Override
        public void setDrawingOrder(java.lang.Object info, int drawingOrderInParent) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi24.setDrawingOrder(info, drawingOrderInParent);
        }

        @java.lang.Override
        public boolean isImportantForAccessibility(java.lang.Object info) {
            return android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi24.isImportantForAccessibility(info);
        }

        @java.lang.Override
        public void setImportantForAccessibility(java.lang.Object info, boolean importantForAccessibility) {
            android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi24.setImportantForAccessibility(info, importantForAccessibility);
        }
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi24Impl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi23Impl();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 22) {
                    IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi22Impl();
                } else
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi21Impl();
                    } else
                        if (android.os.Build.VERSION.SDK_INT >= 19) {
                            // KitKat
                            IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoKitKatImpl();
                        } else
                            if (android.os.Build.VERSION.SDK_INT >= 18) {
                                // JellyBean MR2
                                IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanMr2Impl();
                            } else
                                if (android.os.Build.VERSION.SDK_INT >= 17) {
                                    // JellyBean MR1
                                    IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanMr1Impl();
                                } else
                                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                                        // JellyBean
                                        IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanImpl();
                                    } else
                                        if (android.os.Build.VERSION.SDK_INT >= 14) {
                                            // ICS
                                            IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoIcsImpl();
                                        } else {
                                            IMPL = new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoStubImpl();
                                        }








    }

    static final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityNodeInfoImpl IMPL;

    private final java.lang.Object mInfo;

    // Actions introduced in IceCreamSandwich
    /**
     * Action that focuses the node.
     */
    public static final int ACTION_FOCUS = 0x1;

    /**
     * Action that unfocuses the node.
     */
    public static final int ACTION_CLEAR_FOCUS = 0x2;

    /**
     * Action that selects the node.
     */
    public static final int ACTION_SELECT = 0x4;

    /**
     * Action that unselects the node.
     */
    public static final int ACTION_CLEAR_SELECTION = 0x8;

    /**
     * Action that clicks on the node info.
     */
    public static final int ACTION_CLICK = 0x10;

    /**
     * Action that long clicks on the node.
     */
    public static final int ACTION_LONG_CLICK = 0x20;

    // Actions introduced in JellyBean
    /**
     * Action that gives accessibility focus to the node.
     */
    public static final int ACTION_ACCESSIBILITY_FOCUS = 0x40;

    /**
     * Action that clears accessibility focus of the node.
     */
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 0x80;

    /**
     * Action that requests to go to the next entity in this node's text
     * at a given movement granularity. For example, move to the next character,
     * word, etc.
     * <p>
     * <strong>Arguments:</strong> {@link #ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT}<,
     * {@link #ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN}<br>
     * <strong>Example:</strong> Move to the previous character and do not extend selection.
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
     *           AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER);
     *   arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
     *           false);
     *   info.performAction(AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, arguments);
     * </code></pre></p>
     * </p>
     *
     * @see #ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
     * @see #ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
     * @see #setMovementGranularities(int)
     * @see #getMovementGranularities()
     * @see #MOVEMENT_GRANULARITY_CHARACTER
     * @see #MOVEMENT_GRANULARITY_WORD
     * @see #MOVEMENT_GRANULARITY_LINE
     * @see #MOVEMENT_GRANULARITY_PARAGRAPH
     * @see #MOVEMENT_GRANULARITY_PAGE
     */
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 0x100;

    /**
     * Action that requests to go to the previous entity in this node's text
     * at a given movement granularity. For example, move to the next character,
     * word, etc.
     * <p>
     * <strong>Arguments:</strong> {@link #ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT}<,
     * {@link #ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN}<br>
     * <strong>Example:</strong> Move to the next character and do not extend selection.
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
     *           AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER);
     *   arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
     *           false);
     *   info.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY,
     *           arguments);
     * </code></pre></p>
     * </p>
     *
     * @see #ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
     * @see #ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
     * @see #setMovementGranularities(int)
     * @see #getMovementGranularities()
     * @see #MOVEMENT_GRANULARITY_CHARACTER
     * @see #MOVEMENT_GRANULARITY_WORD
     * @see #MOVEMENT_GRANULARITY_LINE
     * @see #MOVEMENT_GRANULARITY_PARAGRAPH
     * @see #MOVEMENT_GRANULARITY_PAGE
     */
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 0x200;

    /**
     * Action to move to the next HTML element of a given type. For example, move
     * to the BUTTON, INPUT, TABLE, etc.
     * <p>
     * <strong>Arguments:</strong> {@link #ACTION_ARGUMENT_HTML_ELEMENT_STRING}<br>
     * <strong>Example:</strong>
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING, "BUTTON");
     *   info.performAction(AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT, arguments);
     * </code></pre></p>
     * </p>
     */
    public static final int ACTION_NEXT_HTML_ELEMENT = 0x400;

    /**
     * Action to move to the previous HTML element of a given type. For example, move
     * to the BUTTON, INPUT, TABLE, etc.
     * <p>
     * <strong>Arguments:</strong> {@link #ACTION_ARGUMENT_HTML_ELEMENT_STRING}<br>
     * <strong>Example:</strong>
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING, "BUTTON");
     *   info.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT, arguments);
     * </code></pre></p>
     * </p>
     */
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 0x800;

    /**
     * Action to scroll the node content forward.
     */
    public static final int ACTION_SCROLL_FORWARD = 0x1000;

    /**
     * Action to scroll the node content backward.
     */
    public static final int ACTION_SCROLL_BACKWARD = 0x2000;

    // Actions introduced in JellyBeanMr2
    /**
     * Action to copy the current selection to the clipboard.
     */
    public static final int ACTION_COPY = 0x4000;

    /**
     * Action to paste the current clipboard content.
     */
    public static final int ACTION_PASTE = 0x8000;

    /**
     * Action to cut the current selection and place it to the clipboard.
     */
    public static final int ACTION_CUT = 0x10000;

    /**
     * Action to set the selection. Performing this action with no arguments
     * clears the selection.
     * <p>
     * <strong>Arguments:</strong> {@link #ACTION_ARGUMENT_SELECTION_START_INT},
     * {@link #ACTION_ARGUMENT_SELECTION_END_INT}<br>
     * <strong>Example:</strong>
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 1);
     *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, 2);
     *   info.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
     * </code></pre></p>
     * </p>
     *
     * @see #ACTION_ARGUMENT_SELECTION_START_INT
     * @see #ACTION_ARGUMENT_SELECTION_END_INT
     */
    public static final int ACTION_SET_SELECTION = 0x20000;

    /**
     * Action to expand an expandable node.
     */
    public static final int ACTION_EXPAND = 0x40000;

    /**
     * Action to collapse an expandable node.
     */
    public static final int ACTION_COLLAPSE = 0x80000;

    /**
     * Action to dismiss a dismissable node.
     */
    public static final int ACTION_DISMISS = 0x100000;

    /**
     * Action that sets the text of the node. Performing the action without argument, using <code>
     * null</code> or empty {@link CharSequence} will clear the text. This action will also put the
     * cursor at the end of text.
     * <p>
     * <strong>Arguments:</strong> {@link #ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE}<br>
     * <strong>Example:</strong>
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
     *       "android");
     *   info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
     * </code></pre></p>
     */
    public static final int ACTION_SET_TEXT = 0x200000;

    // Action arguments
    /**
     * Argument for which movement granularity to be used when traversing the node text.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong> {@link #ACTION_NEXT_AT_MOVEMENT_GRANULARITY},
     * {@link #ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY}
     * </p>
     */
    public static final java.lang.String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";

    /**
     * Argument for which HTML element to get moving to the next/previous HTML element.
     * <p>
     * <strong>Type:</strong> String<br>
     * <strong>Actions:</strong> {@link #ACTION_NEXT_HTML_ELEMENT},
     *         {@link #ACTION_PREVIOUS_HTML_ELEMENT}
     * </p>
     */
    public static final java.lang.String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";

    /**
     * Argument for whether when moving at granularity to extend the selection
     * or to move it otherwise.
     * <p>
     * <strong>Type:</strong> boolean<br>
     * <strong>Actions:</strong> {@link #ACTION_NEXT_AT_MOVEMENT_GRANULARITY},
     * {@link #ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY}
     * </p>
     *
     * @see #ACTION_NEXT_AT_MOVEMENT_GRANULARITY
     * @see #ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY
     */
    public static final java.lang.String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";

    /**
     * Argument for specifying the selection start.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong> {@link #ACTION_SET_SELECTION}
     * </p>
     *
     * @see #ACTION_SET_SELECTION
     */
    public static final java.lang.String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";

    /**
     * Argument for specifying the selection end.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong> {@link #ACTION_SET_SELECTION}
     * </p>
     *
     * @see #ACTION_SET_SELECTION
     */
    public static final java.lang.String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";

    /**
     * Argument for specifying the text content to set
     * <p>
     * <strong>Type:</strong> CharSequence<br>
     * <strong>Actions:</strong> {@link #ACTION_SET_TEXT}
     * </p>
     *
     * @see #ACTION_SET_TEXT
     */
    public static final java.lang.String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";

    /**
     * Argument for specifying the collection row to make visible on screen.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityActionCompat#ACTION_SCROLL_TO_POSITION}</li>
     * </ul>
     *
     * @see AccessibilityActionCompat#ACTION_SCROLL_TO_POSITION
     */
    public static final java.lang.String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";

    /**
     * Argument for specifying the collection column to make visible on screen.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityActionCompat#ACTION_SCROLL_TO_POSITION}</li>
     * </ul>
     *
     * @see AccessibilityActionCompat#ACTION_SCROLL_TO_POSITION
     */
    public static final java.lang.String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";

    /**
     * Argument for specifying the progress value to set.
     * <p>
     * <strong>Type:</strong> float<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityActionCompat#ACTION_SET_PROGRESS}</li>
     * </ul>
     *
     * @see AccessibilityActionCompat#ACTION_SET_PROGRESS
     */
    public static final java.lang.String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";

    // Focus types
    /**
     * The input focus.
     */
    public static final int FOCUS_INPUT = 1;

    /**
     * The accessibility focus.
     */
    public static final int FOCUS_ACCESSIBILITY = 2;

    // Movement granularities
    /**
     * Movement granularity bit for traversing the text of a node by character.
     */
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 0x1;

    /**
     * Movement granularity bit for traversing the text of a node by word.
     */
    public static final int MOVEMENT_GRANULARITY_WORD = 0x2;

    /**
     * Movement granularity bit for traversing the text of a node by line.
     */
    public static final int MOVEMENT_GRANULARITY_LINE = 0x4;

    /**
     * Movement granularity bit for traversing the text of a node by paragraph.
     */
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 0x8;

    /**
     * Movement granularity bit for traversing the text of a node by page.
     */
    public static final int MOVEMENT_GRANULARITY_PAGE = 0x10;

    /**
     * Creates a wrapper for info implementation.
     *
     * @param object
     * 		The info to wrap.
     * @return A wrapper for if the object is not null, null otherwise.
     */
    static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat wrapNonNullInstance(java.lang.Object object) {
        if (object != null) {
            return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat(object);
        }
        return null;
    }

    /**
     * Creates a new instance wrapping an
     * {@link android.view.accessibility.AccessibilityNodeInfo}.
     *
     * @param info
     * 		The info.
     */
    public AccessibilityNodeInfoCompat(java.lang.Object info) {
        mInfo = info;
    }

    /**
     *
     *
     * @return The wrapped {@link android.view.accessibility.AccessibilityNodeInfo}.
     */
    public java.lang.Object getInfo() {
        return mInfo;
    }

    /**
     * Returns a cached instance if such is available otherwise a new one and
     * sets the source.
     *
     * @return An instance.
     * @see #setSource(View)
     */
    public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat obtain(android.view.View source) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtain(source));
    }

    /**
     * Returns a cached instance if such is available otherwise a new one
     * and sets the source.
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     * @return An instance.
     * @see #setSource(View, int)
     */
    public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat obtain(android.view.View root, int virtualDescendantId) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtain(root, virtualDescendantId));
    }

    /**
     * Returns a cached instance if such is available otherwise a new one.
     *
     * @return An instance.
     */
    public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat obtain() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtain());
    }

    /**
     * Returns a cached instance if such is available or a new one is create.
     * The returned instance is initialized from the given <code>info</code>.
     *
     * @param info
     * 		The other info.
     * @return An instance.
     */
    public static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat obtain(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.obtain(info.mInfo));
    }

    /**
     * Sets the source.
     *
     * @param source
     * 		The info source.
     */
    public void setSource(android.view.View source) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setSource(mInfo, source);
    }

    /**
     * Sets the source to be a virtual descendant of the given <code>root</code>.
     * If <code>virtualDescendantId</code> is {@link View#NO_ID} the root
     * is set as the source.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report themselves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setSource(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setSource(mInfo, root, virtualDescendantId);
    }

    /**
     * Find the view that has the specified focus type. The search starts from
     * the view represented by this node info.
     *
     * @param focus
     * 		The focus to find. One of {@link #FOCUS_INPUT} or
     * 		{@link #FOCUS_ACCESSIBILITY}.
     * @return The node info of the focused view or null.
     * @see #FOCUS_INPUT
     * @see #FOCUS_ACCESSIBILITY
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat findFocus(int focus) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.findFocus(mInfo, focus));
    }

    /**
     * Searches for the nearest view in the specified direction that can take
     * input focus.
     *
     * @param direction
     * 		The direction. Can be one of:
     * 		{@link View#FOCUS_DOWN},
     * 		{@link View#FOCUS_UP},
     * 		{@link View#FOCUS_LEFT},
     * 		{@link View#FOCUS_RIGHT},
     * 		{@link View#FOCUS_FORWARD},
     * 		{@link View#FOCUS_BACKWARD}.
     * @return The node info for the view that can take accessibility focus.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat focusSearch(int direction) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.focusSearch(mInfo, direction));
    }

    /**
     * Gets the id of the window from which the info comes from.
     *
     * @return The window id.
     */
    public int getWindowId() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getWindowId(mInfo);
    }

    /**
     * Gets the number of children.
     *
     * @return The child count.
     */
    public int getChildCount() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getChildCount(mInfo);
    }

    /**
     * Get the child at given index.
     * <p>
     * <strong>Note:</strong> It is a client responsibility to recycle the
     * received info by calling {@link AccessibilityNodeInfoCompat#recycle()} to
     * avoid creating of multiple instances.
     * </p>
     *
     * @param index
     * 		The child index.
     * @return The child node.
     * @throws IllegalStateException
     * 		If called outside of an
     * 		AccessibilityService.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getChild(int index) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getChild(mInfo, index));
    }

    /**
     * Adds a child.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param child
     * 		The child.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void addChild(android.view.View child) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.addChild(mInfo, child);
    }

    /**
     * Adds a virtual child which is a descendant of the given <code>root</code>.
     * If <code>virtualDescendantId</code> is {@link View#NO_ID} the root
     * is added as a child.
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
     * 		The id of the virtual child.
     */
    public void addChild(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.addChild(mInfo, root, virtualDescendantId);
    }

    /**
     * Removes a child. If the child was not previously added to the node,
     * calling this method has no effect.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}.
     * This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param child
     * 		The child.
     * @return true if the child was present
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public boolean removeChild(android.view.View child) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.removeChild(mInfo, child);
    }

    /**
     * Removes a virtual child which is a descendant of the given
     * <code>root</code>. If the child was not previously added to the node,
     * calling this method has no effect.
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual child.
     * @return true if the child was present
     * @see #addChild(View, int)
     */
    public boolean removeChild(android.view.View root, int virtualDescendantId) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.removeChild(mInfo, root, virtualDescendantId);
    }

    /**
     * Gets the actions that can be performed on the node.
     *
     * @return The bit mask of with actions.
     * @see android.view.accessibility.AccessibilityNodeInfo#ACTION_FOCUS
     * @see android.view.accessibility.AccessibilityNodeInfo#ACTION_CLEAR_FOCUS
     * @see android.view.accessibility.AccessibilityNodeInfo#ACTION_SELECT
     * @see android.view.accessibility.AccessibilityNodeInfo#ACTION_CLEAR_SELECTION
     */
    public int getActions() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActions(mInfo);
    }

    /**
     * Adds an action that can be performed on the node.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param action
     * 		The action.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void addAction(int action) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.addAction(mInfo, action);
    }

    /**
     * Adds an action that can be performed on the node.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param action
     * 		The action.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void addAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat action) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.addAction(mInfo, action.mAction);
    }

    /**
     * Removes an action that can be performed on the node. If the action was
     * not already added to the node, calling this method has no effect.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param action
     * 		The action to be removed.
     * @return The action removed from the list of actions.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public boolean removeAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat action) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.removeAction(mInfo, action.mAction);
    }

    /**
     * Performs an action on the node.
     * <p>
     * <strong>Note:</strong> An action can be performed only if the request is
     * made from an {@link android.accessibilityservice.AccessibilityService}.
     * </p>
     *
     * @param action
     * 		The action to perform.
     * @return True if the action was performed.
     * @throws IllegalStateException
     * 		If called outside of an
     * 		AccessibilityService.
     */
    public boolean performAction(int action) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.performAction(mInfo, action);
    }

    /**
     * Performs an action on the node.
     * <p>
     *   <strong>Note:</strong> An action can be performed only if the request is made
     *   from an {@link android.accessibilityservice.AccessibilityService}.
     * </p>
     *
     * @param action
     * 		The action to perform.
     * @param arguments
     * 		A bundle with additional arguments.
     * @return True if the action was performed.
     * @throws IllegalStateException
     * 		If called outside of an AccessibilityService.
     */
    public boolean performAction(int action, android.os.Bundle arguments) {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.performAction(mInfo, action, arguments);
    }

    /**
     * Sets the movement granularities for traversing the text of this node.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param granularities
     * 		The bit mask with granularities.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setMovementGranularities(int granularities) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setMovementGranularities(mInfo, granularities);
    }

    /**
     * Gets the movement granularities for traversing the text of this node.
     *
     * @return The bit mask with granularities.
     */
    public int getMovementGranularities() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getMovementGranularities(mInfo);
    }

    /**
     * Finds {@link android.view.accessibility.AccessibilityNodeInfo}s by text. The match
     * is case insensitive containment. The search is relative to this info i.e. this
     * info is the root of the traversed tree.
     * <p>
     * <strong>Note:</strong> It is a client responsibility to recycle the
     * received info by calling {@link android.view.accessibility.AccessibilityNodeInfo#recycle()}
     * to avoid creating of multiple instances.
     * </p>
     *
     * @param text
     * 		The searched text.
     * @return A list of node info.
     */
    public java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(java.lang.String text) {
        java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> result = new java.util.ArrayList<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat>();
        java.util.List<java.lang.Object> infos = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.findAccessibilityNodeInfosByText(mInfo, text);
        final int infoCount = infos.size();
        for (int i = 0; i < infoCount; i++) {
            java.lang.Object info = infos.get(i);
            result.add(new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat(info));
        }
        return result;
    }

    /**
     * Gets the parent.
     * <p>
     * <strong>Note:</strong> It is a client responsibility to recycle the
     * received info by calling {@link android.view.accessibility.AccessibilityNodeInfo#recycle()}
     * to avoid creating of multiple instances.
     * </p>
     *
     * @return The parent.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getParent() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getParent(mInfo));
    }

    /**
     * Sets the parent.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param parent
     * 		The parent.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setParent(android.view.View parent) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setParent(mInfo, parent);
    }

    /**
     * Sets the parent to be a virtual descendant of the given <code>root</code>.
     * If <code>virtualDescendantId</code> equals to {@link View#NO_ID} the root
     * is set as the parent.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report them selves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setParent(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setParent(mInfo, root, virtualDescendantId);
    }

    /**
     * Gets the node bounds in parent coordinates.
     *
     * @param outBounds
     * 		The output node bounds.
     */
    public void getBoundsInParent(android.graphics.Rect outBounds) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getBoundsInParent(mInfo, outBounds);
    }

    /**
     * Sets the node bounds in parent coordinates.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param bounds
     * 		The node bounds.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setBoundsInParent(android.graphics.Rect bounds) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setBoundsInParent(mInfo, bounds);
    }

    /**
     * Gets the node bounds in screen coordinates.
     *
     * @param outBounds
     * 		The output node bounds.
     */
    public void getBoundsInScreen(android.graphics.Rect outBounds) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getBoundsInScreen(mInfo, outBounds);
    }

    /**
     * Sets the node bounds in screen coordinates.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param bounds
     * 		The node bounds.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setBoundsInScreen(android.graphics.Rect bounds) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setBoundsInScreen(mInfo, bounds);
    }

    /**
     * Gets whether this node is checkable.
     *
     * @return True if the node is checkable.
     */
    public boolean isCheckable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isCheckable(mInfo);
    }

    /**
     * Sets whether this node is checkable.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param checkable
     * 		True if the node is checkable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setCheckable(boolean checkable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setCheckable(mInfo, checkable);
    }

    /**
     * Gets whether this node is checked.
     *
     * @return True if the node is checked.
     */
    public boolean isChecked() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isChecked(mInfo);
    }

    /**
     * Sets whether this node is checked.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param checked
     * 		True if the node is checked.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setChecked(boolean checked) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setChecked(mInfo, checked);
    }

    /**
     * Gets whether this node is focusable.
     *
     * @return True if the node is focusable.
     */
    public boolean isFocusable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isFocusable(mInfo);
    }

    /**
     * Sets whether this node is focusable.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param focusable
     * 		True if the node is focusable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setFocusable(boolean focusable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setFocusable(mInfo, focusable);
    }

    /**
     * Gets whether this node is focused.
     *
     * @return True if the node is focused.
     */
    public boolean isFocused() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isFocused(mInfo);
    }

    /**
     * Sets whether this node is focused.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param focused
     * 		True if the node is focused.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setFocused(boolean focused) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setFocused(mInfo, focused);
    }

    /**
     * Sets whether this node is visible to the user.
     *
     * @return Whether the node is visible to the user.
     */
    public boolean isVisibleToUser() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isVisibleToUser(mInfo);
    }

    /**
     * Sets whether this node is visible to the user.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param visibleToUser
     * 		Whether the node is visible to the user.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setVisibleToUser(boolean visibleToUser) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setVisibleToUser(mInfo, visibleToUser);
    }

    /**
     * Gets whether this node is accessibility focused.
     *
     * @return True if the node is accessibility focused.
     */
    public boolean isAccessibilityFocused() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isAccessibilityFocused(mInfo);
    }

    /**
     * Sets whether this node is accessibility focused.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param focused
     * 		True if the node is accessibility focused.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setAccessibilityFocused(boolean focused) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setAccessibilityFocused(mInfo, focused);
    }

    /**
     * Gets whether this node is selected.
     *
     * @return True if the node is selected.
     */
    public boolean isSelected() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isSelected(mInfo);
    }

    /**
     * Sets whether this node is selected.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param selected
     * 		True if the node is selected.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setSelected(boolean selected) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setSelected(mInfo, selected);
    }

    /**
     * Gets whether this node is clickable.
     *
     * @return True if the node is clickable.
     */
    public boolean isClickable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isClickable(mInfo);
    }

    /**
     * Sets whether this node is clickable.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param clickable
     * 		True if the node is clickable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setClickable(boolean clickable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setClickable(mInfo, clickable);
    }

    /**
     * Gets whether this node is long clickable.
     *
     * @return True if the node is long clickable.
     */
    public boolean isLongClickable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isLongClickable(mInfo);
    }

    /**
     * Sets whether this node is long clickable.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param longClickable
     * 		True if the node is long clickable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setLongClickable(boolean longClickable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setLongClickable(mInfo, longClickable);
    }

    /**
     * Gets whether this node is enabled.
     *
     * @return True if the node is enabled.
     */
    public boolean isEnabled() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isEnabled(mInfo);
    }

    /**
     * Sets whether this node is enabled.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param enabled
     * 		True if the node is enabled.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setEnabled(boolean enabled) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setEnabled(mInfo, enabled);
    }

    /**
     * Gets whether this node is a password.
     *
     * @return True if the node is a password.
     */
    public boolean isPassword() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isPassword(mInfo);
    }

    /**
     * Sets whether this node is a password.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param password
     * 		True if the node is a password.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setPassword(boolean password) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setPassword(mInfo, password);
    }

    /**
     * Gets if the node is scrollable.
     *
     * @return True if the node is scrollable, false otherwise.
     */
    public boolean isScrollable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isScrollable(mInfo);
    }

    /**
     * Sets if the node is scrollable.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param scrollable
     * 		True if the node is scrollable, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setScrollable(boolean scrollable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setScrollable(mInfo, scrollable);
    }

    /**
     * Returns whether the node originates from a view considered important for accessibility.
     *
     * @return {@code true} if the node originates from a view considered important for
    accessibility, {@code false} otherwise
     * @see View#isImportantForAccessibility()
     */
    public boolean isImportantForAccessibility() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isImportantForAccessibility(mInfo);
    }

    /**
     * Sets whether the node is considered important for accessibility.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param important
     * 		{@code true} if the node is considered important for accessibility,
     * 		{@code false} otherwise
     */
    public void setImportantForAccessibility(boolean important) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setImportantForAccessibility(mInfo, important);
    }

    /**
     * Gets the package this node comes from.
     *
     * @return The package name.
     */
    public java.lang.CharSequence getPackageName() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getPackageName(mInfo);
    }

    /**
     * Sets the package this node comes from.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param packageName
     * 		The package name.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setPackageName(java.lang.CharSequence packageName) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setPackageName(mInfo, packageName);
    }

    /**
     * Gets the class this node comes from.
     *
     * @return The class name.
     */
    public java.lang.CharSequence getClassName() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getClassName(mInfo);
    }

    /**
     * Sets the class this node comes from.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param className
     * 		The class name.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setClassName(java.lang.CharSequence className) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setClassName(mInfo, className);
    }

    /**
     * Gets the text of this node.
     *
     * @return The text.
     */
    public java.lang.CharSequence getText() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getText(mInfo);
    }

    /**
     * Sets the text of this node.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param text
     * 		The text.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setText(java.lang.CharSequence text) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setText(mInfo, text);
    }

    /**
     * Gets the content description of this node.
     *
     * @return The content description.
     */
    public java.lang.CharSequence getContentDescription() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getContentDescription(mInfo);
    }

    /**
     * Sets the content description of this node.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param contentDescription
     * 		The content description.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setContentDescription(java.lang.CharSequence contentDescription) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setContentDescription(mInfo, contentDescription);
    }

    /**
     * Return an instance back to be reused.
     * <p>
     * <strong>Note:</strong> You must not touch the object after calling this function.
     *
     * @throws IllegalStateException
     * 		If the info is already recycled.
     */
    public void recycle() {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.recycle(mInfo);
    }

    /**
     * Sets the fully qualified resource name of the source view's id.
     *
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param viewId
     * 		The id resource name.
     */
    public void setViewIdResourceName(java.lang.String viewId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setViewIdResourceName(mInfo, viewId);
    }

    /**
     * Gets the fully qualified resource name of the source view's id.
     *
     * <p>
     *   <strong>Note:</strong> The primary usage of this API is for UI test automation
     *   and in order to report the source view id of an {@link AccessibilityNodeInfoCompat}
     *   the client has to set the {@link AccessibilityServiceInfoCompat#FLAG_REPORT_VIEW_IDS}
     *   flag when configuring his {@link android.accessibilityservice.AccessibilityService}.
     * </p>
     *
     * @return The id resource name.
     */
    public java.lang.String getViewIdResourceName() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getViewIdResourceName(mInfo);
    }

    /**
     * Gets the node's live region mode.
     * <p>
     * A live region is a node that contains information that is important for
     * the user and when it changes the user should be notified. For example,
     * in a login screen with a TextView that displays an "incorrect password"
     * notification, that view should be marked as a live region with mode
     * {@link ViewCompat#ACCESSIBILITY_LIVE_REGION_POLITE}.
     * <p>
     * It is the responsibility of the accessibility service to monitor
     * {@link AccessibilityEventCompat#TYPE_WINDOW_CONTENT_CHANGED} events
     * indicating changes to live region nodes and their children.
     *
     * @return The live region mode, or
    {@link ViewCompat#ACCESSIBILITY_LIVE_REGION_NONE} if the view is
    not a live region.
     * @see ViewCompat#getAccessibilityLiveRegion(View)
     */
    public int getLiveRegion() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getLiveRegion(mInfo);
    }

    /**
     * Sets the node's live region mode.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is
     * made immutable before being delivered to an AccessibilityService.
     *
     * @param mode
     * 		The live region mode, or
     * 		{@link ViewCompat#ACCESSIBILITY_LIVE_REGION_NONE} if the view is
     * 		not a live region.
     * @see ViewCompat#setAccessibilityLiveRegion(View, int)
     */
    public void setLiveRegion(int mode) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setLiveRegion(mInfo, mode);
    }

    /**
     * Get the drawing order of the view corresponding it this node.
     * <p>
     * Drawing order is determined only within the node's parent, so this index is only relative
     * to its siblings.
     * <p>
     * In some cases, the drawing order is essentially simultaneous, so it is possible for two
     * siblings to return the same value. It is also possible that values will be skipped.
     *
     * @return The drawing position of the view corresponding to this node relative to its siblings.
     */
    public int getDrawingOrder() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getDrawingOrder(mInfo);
    }

    /**
     * Set the drawing order of the view corresponding it this node.
     *
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param drawingOrderInParent
     * 		
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setDrawingOrder(int drawingOrderInParent) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setDrawingOrder(mInfo, drawingOrderInParent);
    }

    /**
     * Gets the collection info if the node is a collection. A collection
     * child is always a collection item.
     *
     * @return The collection info.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat getCollectionInfo() {
        java.lang.Object info = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionInfo(mInfo);
        if (info == null)
            return null;

        return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat(info);
    }

    public void setCollectionInfo(java.lang.Object collectionInfo) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setCollectionInfo(mInfo, ((android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat) (collectionInfo)).mInfo);
    }

    public void setCollectionItemInfo(java.lang.Object collectionItemInfo) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setCollectionItemInfo(mInfo, ((android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat) (collectionItemInfo)).mInfo);
    }

    /**
     * Gets the collection item info if the node is a collection item. A collection
     * item is always a child of a collection.
     *
     * @return The collection item info.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat getCollectionItemInfo() {
        java.lang.Object info = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getCollectionItemInfo(mInfo);
        if (info == null)
            return null;

        return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat(info);
    }

    /**
     * Gets the range info if this node is a range.
     *
     * @return The range.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.RangeInfoCompat getRangeInfo() {
        java.lang.Object info = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getRangeInfo(mInfo);
        if (info == null)
            return null;

        return new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.RangeInfoCompat(info);
    }

    /**
     * Sets the range info if this node is a range.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param rangeInfo
     * 		The range info.
     */
    public void setRangeInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.RangeInfoCompat rangeInfo) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setRangeInfo(mInfo, rangeInfo.mInfo);
    }

    /**
     * Gets the actions that can be performed on the node.
     *
     * @return A list of AccessibilityActions.
     */
    public java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList() {
        java.util.List<java.lang.Object> actions = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getActionList(mInfo);
        if (actions != null) {
            java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat> result = new java.util.ArrayList<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat>();
            final int actionCount = actions.size();
            for (int i = 0; i < actionCount; i++) {
                java.lang.Object action = actions.get(i);
                result.add(new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat(action));
            }
            return result;
        } else {
            return java.util.Collections.<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat>emptyList();
        }
    }

    /**
     * Sets if the content of this node is invalid. For example,
     * a date is not well-formed.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param contentInvalid
     * 		If the node content is invalid.
     */
    public void setContentInvalid(boolean contentInvalid) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setContentInvalid(mInfo, contentInvalid);
    }

    /**
     * Gets if the content of this node is invalid. For example,
     * a date is not well-formed.
     *
     * @return If the node content is invalid.
     */
    public boolean isContentInvalid() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isContentInvalid(mInfo);
    }

    /**
     * Gets whether this node is context clickable.
     *
     * @return True if the node is context clickable.
     */
    public boolean isContextClickable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isContextClickable(mInfo);
    }

    /**
     * Sets whether this node is context clickable.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}. This class is made immutable
     * before being delivered to an AccessibilityService.
     * </p>
     *
     * @param contextClickable
     * 		True if the node is context clickable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setContextClickable(boolean contextClickable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setContextClickable(mInfo, contextClickable);
    }

    /**
     * Sets the error text of this node.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param error
     * 		The error text.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setError(java.lang.CharSequence error) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setError(mInfo, error);
    }

    /**
     * Gets the error text of this node.
     *
     * @return The error text.
     */
    public java.lang.CharSequence getError() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getError(mInfo);
    }

    /**
     * Sets the view for which the view represented by this info serves as a
     * label for accessibility purposes.
     *
     * @param labeled
     * 		The view for which this info serves as a label.
     */
    public void setLabelFor(android.view.View labeled) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setLabelFor(mInfo, labeled);
    }

    /**
     * Sets the view for which the view represented by this info serves as a
     * label for accessibility purposes. If <code>virtualDescendantId</code>
     * is {@link View#NO_ID} the root is set as the labeled.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report themselves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     *
     * @param root
     * 		The root whose virtual descendant serves as a label.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setLabelFor(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setLabelFor(mInfo, root, virtualDescendantId);
    }

    /**
     * Gets the node info for which the view represented by this info serves as
     * a label for accessibility purposes.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfoCompat#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @return The labeled info.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getLabelFor() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getLabelFor(mInfo));
    }

    /**
     * Sets the view which serves as the label of the view represented by
     * this info for accessibility purposes.
     *
     * @param label
     * 		The view that labels this node's source.
     */
    public void setLabeledBy(android.view.View label) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setLabeledBy(mInfo, label);
    }

    /**
     * Sets the view which serves as the label of the view represented by
     * this info for accessibility purposes. If <code>virtualDescendantId</code>
     * is {@link View#NO_ID} the root is set as the label.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report themselves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param root
     * 		The root whose virtual descendant labels this node's source.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setLabeledBy(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setLabeledBy(mInfo, root, virtualDescendantId);
    }

    /**
     * Gets the node info which serves as the label of the view represented by
     * this info for accessibility purposes.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfoCompat#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @return The label.
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getLabeledBy() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getLabeledBy(mInfo));
    }

    /**
     * Gets if this node opens a popup or a dialog.
     *
     * @return If the the node opens a popup.
     */
    public boolean canOpenPopup() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.canOpenPopup(mInfo);
    }

    /**
     * Sets if this node opens a popup or a dialog.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param opensPopup
     * 		If the the node opens a popup.
     */
    public void setCanOpenPopup(boolean opensPopup) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setCanOpenPopup(mInfo, opensPopup);
    }

    /**
     * Finds {@link AccessibilityNodeInfoCompat}s by the fully qualified view id's resource
     * name where a fully qualified id is of the from "package:id/id_resource_name".
     * For example, if the target application's package is "foo.bar" and the id
     * resource name is "baz", the fully qualified resource id is "foo.bar:id/baz".
     *
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfoCompat#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     * <p>
     *   <strong>Note:</strong> The primary usage of this API is for UI test automation
     *   and in order to report the fully qualified view id if an
     *   {@link AccessibilityNodeInfoCompat} the client has to set the
     *   {@link android.accessibilityservice.AccessibilityServiceInfo#FLAG_REPORT_VIEW_IDS}
     *   flag when configuring his {@link android.accessibilityservice.AccessibilityService}.
     * </p>
     *
     * @param viewId
     * 		The fully qualified resource name of the view id to find.
     * @return A list of node info.
     */
    public java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId(java.lang.String viewId) {
        java.util.List<java.lang.Object> nodes = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.findAccessibilityNodeInfosByViewId(mInfo, viewId);
        if (nodes != null) {
            java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> result = new java.util.ArrayList<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat>();
            for (java.lang.Object node : nodes) {
                result.add(new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat(node));
            }
            return result;
        } else {
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Gets an optional bundle with extra data. The bundle
     * is lazily created and never <code>null</code>.
     * <p>
     * <strong>Note:</strong> It is recommended to use the package
     * name of your application as a prefix for the keys to avoid
     * collisions which may confuse an accessibility service if the
     * same key has different meaning when emitted from different
     * applications.
     * </p>
     *
     * @return The bundle.
     */
    public android.os.Bundle getExtras() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getExtras(mInfo);
    }

    /**
     * Gets the input type of the source as defined by {@link InputType}.
     *
     * @return The input type.
     */
    public int getInputType() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getInputType(mInfo);
    }

    /**
     * Sets the input type of the source as defined by {@link InputType}.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an
     *   AccessibilityService.
     * </p>
     *
     * @param inputType
     * 		The input type.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setInputType(int inputType) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setInputType(mInfo, inputType);
    }

    /**
     * Sets the maximum text length, or -1 for no limit.
     * <p>
     * Typically used to indicate that an editable text field has a limit on
     * the number of characters entered.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}.
     * This class is made immutable before being delivered to an AccessibilityService.
     *
     * @param max
     * 		The maximum text length.
     * @see #getMaxTextLength()
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setMaxTextLength(int max) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setMaxTextLength(mInfo, max);
    }

    /**
     * Returns the maximum text length for this node.
     *
     * @return The maximum text length, or -1 for no limit.
     * @see #setMaxTextLength(int)
     */
    public int getMaxTextLength() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getMaxTextLength(mInfo);
    }

    /**
     * Sets the text selection start and end.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param start
     * 		The text selection start.
     * @param end
     * 		The text selection end.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setTextSelection(int start, int end) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setTextSelection(mInfo, start, end);
    }

    /**
     * Gets the text selection start.
     *
     * @return The text selection start if there is selection or -1.
     */
    public int getTextSelectionStart() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getTextSelectionStart(mInfo);
    }

    /**
     * Gets the text selection end.
     *
     * @return The text selection end if there is selection or -1.
     */
    public int getTextSelectionEnd() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getTextSelectionEnd(mInfo);
    }

    /**
     * Gets the node before which this one is visited during traversal. A screen-reader
     * must visit the content of this node before the content of the one it precedes.
     *
     * @return The succeeding node if such or <code>null</code>.
     * @see #setTraversalBefore(android.view.View)
     * @see #setTraversalBefore(android.view.View, int)
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getTraversalBefore() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getTraversalBefore(mInfo));
    }

    /**
     * Sets the view before whose node this one should be visited during traversal. A
     * screen-reader must visit the content of this node before the content of the one
     * it precedes.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param view
     * 		The view providing the preceding node.
     * @see #getTraversalBefore()
     */
    public void setTraversalBefore(android.view.View view) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setTraversalBefore(mInfo, view);
    }

    /**
     * Sets the node before which this one is visited during traversal. A screen-reader
     * must visit the content of this node before the content of the one it precedes.
     * The successor is a virtual descendant of the given <code>root</code>. If
     * <code>virtualDescendantId</code> equals to {@link View#NO_ID} the root is set
     * as the successor.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report them selves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setTraversalBefore(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setTraversalBefore(mInfo, root, virtualDescendantId);
    }

    /**
     * Gets the node after which this one is visited in accessibility traversal.
     * A screen-reader must visit the content of the other node before the content
     * of this one.
     *
     * @return The succeeding node if such or <code>null</code>.
     * @see #setTraversalAfter(android.view.View)
     * @see #setTraversalAfter(android.view.View, int)
     */
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat getTraversalAfter() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getTraversalAfter(mInfo));
    }

    /**
     * Sets the view whose node is visited after this one in accessibility traversal.
     * A screen-reader must visit the content of the other node before the content
     * of this one.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param view
     * 		The previous view.
     * @see #getTraversalAfter()
     */
    public void setTraversalAfter(android.view.View view) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setTraversalAfter(mInfo, view);
    }

    /**
     * Sets the node after which this one is visited in accessibility traversal.
     * A screen-reader must visit the content of the other node before the content
     * of this one. If <code>virtualDescendantId</code> equals to {@link View#NO_ID}
     * the root is set as the predecessor.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report them selves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setTraversalAfter(android.view.View root, int virtualDescendantId) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setTraversalAfter(mInfo, root, virtualDescendantId);
    }

    /**
     * Gets the window to which this node belongs.
     *
     * @return The window.
     * @see android.accessibilityservice.AccessibilityService#getWindows()
     */
    public android.support.v4.view.accessibility.AccessibilityWindowInfoCompat getWindow() {
        return android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.wrapNonNullInstance(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getWindow(mInfo));
    }

    /**
     * Gets if the node can be dismissed.
     *
     * @return If the node can be dismissed.
     */
    public boolean isDismissable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isDismissable(mInfo);
    }

    /**
     * Sets if the node can be dismissed.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param dismissable
     * 		If the node can be dismissed.
     */
    public void setDismissable(boolean dismissable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setDismissable(mInfo, dismissable);
    }

    /**
     * Gets if the node is editable.
     *
     * @return True if the node is editable, false otherwise.
     */
    public boolean isEditable() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isEditable(mInfo);
    }

    /**
     * Sets whether this node is editable.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param editable
     * 		True if the node is editable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setEditable(boolean editable) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setEditable(mInfo, editable);
    }

    /**
     * Gets if the node is a multi line editable text.
     *
     * @return True if the node is multi line.
     */
    public boolean isMultiLine() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.isMultiLine(mInfo);
    }

    /**
     * Sets if the node is a multi line editable text.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param multiLine
     * 		True if the node is multi line.
     */
    public void setMultiLine(boolean multiLine) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setMultiLine(mInfo, multiLine);
    }

    /**
     * Refreshes this info with the latest state of the view it represents.
     * <p>
     * <strong>Note:</strong> If this method returns false this info is obsolete
     * since it represents a view that is no longer in the view tree and should
     * be recycled.
     * </p>
     *
     * @return Whether the refresh succeeded.
     */
    public boolean refresh() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.refresh(mInfo);
    }

    /**
     * Gets the custom role description.
     *
     * @return The role description.
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getRoleDescription() {
        return android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.getRoleDescription(mInfo);
    }

    /**
     * Sets the custom role description.
     *
     * <p>
     *   The role description allows you to customize the name for the view's semantic
     *   role. For example, if you create a custom subclass of {@link android.view.View}
     *   to display a menu bar, you could assign it the role description of "menu bar".
     * </p>
     * <p>
     *   <strong>Warning:</strong> For consistency with other applications, you should
     *   not use the role description to force accessibility services to describe
     *   standard views (such as buttons or checkboxes) using specific wording. For
     *   example, you should not set a role description of "check box" or "tick box" for
     *   a standard {@link android.widget.CheckBox}. Instead let accessibility services
     *   decide what feedback to provide.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param roleDescription
     * 		The role description.
     */
    public void setRoleDescription(@android.support.annotation.Nullable
    java.lang.CharSequence roleDescription) {
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.IMPL.setRoleDescription(mInfo, roleDescription);
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
        android.support.v4.view.accessibility.AccessibilityNodeInfoCompat other = ((android.support.v4.view.accessibility.AccessibilityNodeInfoCompat) (obj));
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
        builder.append(super.toString());
        android.graphics.Rect bounds = new android.graphics.Rect();
        getBoundsInParent(bounds);
        builder.append("; boundsInParent: " + bounds);
        getBoundsInScreen(bounds);
        builder.append("; boundsInScreen: " + bounds);
        builder.append("; packageName: ").append(getPackageName());
        builder.append("; className: ").append(getClassName());
        builder.append("; text: ").append(getText());
        builder.append("; contentDescription: ").append(getContentDescription());
        builder.append("; viewId: ").append(getViewIdResourceName());
        builder.append("; checkable: ").append(isCheckable());
        builder.append("; checked: ").append(isChecked());
        builder.append("; focusable: ").append(isFocusable());
        builder.append("; focused: ").append(isFocused());
        builder.append("; selected: ").append(isSelected());
        builder.append("; clickable: ").append(isClickable());
        builder.append("; longClickable: ").append(isLongClickable());
        builder.append("; enabled: ").append(isEnabled());
        builder.append("; password: ").append(isPassword());
        builder.append("; scrollable: " + isScrollable());
        builder.append("; [");
        for (int actionBits = getActions(); actionBits != 0;) {
            final int action = 1 << java.lang.Integer.numberOfTrailingZeros(actionBits);
            actionBits &= ~action;
            builder.append(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.getActionSymbolicName(action));
            if (actionBits != 0) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private static java.lang.String getActionSymbolicName(int action) {
        switch (action) {
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_FOCUS :
                return "ACTION_FOCUS";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLEAR_FOCUS :
                return "ACTION_CLEAR_FOCUS";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SELECT :
                return "ACTION_SELECT";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLEAR_SELECTION :
                return "ACTION_CLEAR_SELECTION";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLICK :
                return "ACTION_CLICK";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_LONG_CLICK :
                return "ACTION_LONG_CLICK";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS :
                return "ACTION_ACCESSIBILITY_FOCUS";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS :
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY :
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY :
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT :
                return "ACTION_NEXT_HTML_ELEMENT";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT :
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD :
                return "ACTION_SCROLL_FORWARD";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD :
                return "ACTION_SCROLL_BACKWARD";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CUT :
                return "ACTION_CUT";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_COPY :
                return "ACTION_COPY";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_PASTE :
                return "ACTION_PASTE";
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SET_SELECTION :
                return "ACTION_SET_SELECTION";
            default :
                return "ACTION_UNKNOWN";
        }
    }
}

