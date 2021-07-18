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
 * This class represents a node of the window content as well as actions that
 * can be requested from its source. From the point of view of an
 * {@link android.accessibilityservice.AccessibilityService} a window's content is
 * presented as a tree of accessibility node infos, which may or may not map one-to-one
 * to the view hierarchy. In other words, a custom view is free to report itself as
 * a tree of accessibility node info.
 * </p>
 * <p>
 * Once an accessibility node info is delivered to an accessibility service it is
 * made immutable and calling a state mutation method generates an error.
 * </p>
 * <p>
 * Please refer to {@link android.accessibilityservice.AccessibilityService} for
 * details about how to obtain a handle to window content as a tree of accessibility
 * node info as well as details about the security model.
 * </p>
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about making applications accessible, read the
 * <a href="{@docRoot }guide/topics/ui/accessibility/index.html">Accessibility</a>
 * developer guide.</p>
 * </div>
 *
 * @see android.accessibilityservice.AccessibilityService
 * @see AccessibilityEvent
 * @see AccessibilityManager
 */
public class AccessibilityNodeInfo implements android.os.Parcelable {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "AccessibilityNodeInfo";

    /**
     *
     *
     * @unknown 
     */
    public static final int UNDEFINED_CONNECTION_ID = -1;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNDEFINED_SELECTION_INDEX = -1;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNDEFINED_ITEM_ID = java.lang.Integer.MAX_VALUE;

    /**
     *
     *
     * @unknown 
     */
    public static final int ROOT_ITEM_ID = java.lang.Integer.MAX_VALUE - 1;

    /**
     *
     *
     * @unknown 
     */
    public static final long UNDEFINED_NODE_ID = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID, android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID);

    /**
     *
     *
     * @unknown 
     */
    public static final long ROOT_NODE_ID = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(android.view.accessibility.AccessibilityNodeInfo.ROOT_ITEM_ID, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_PREFETCH_PREDECESSORS = 0x1;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_PREFETCH_SIBLINGS = 0x2;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_PREFETCH_DESCENDANTS = 0x4;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 0x8;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLAG_REPORT_VIEW_IDS = 0x10;

    // Actions.
    /**
     * Action that gives input focus to the node.
     */
    public static final int ACTION_FOCUS = 0x1;

    /**
     * Action that clears input focus of the node.
     */
    public static final int ACTION_CLEAR_FOCUS = 0x2;

    /**
     * Action that selects the node.
     */
    public static final int ACTION_SELECT = 0x4;

    /**
     * Action that deselects the node.
     */
    public static final int ACTION_CLEAR_SELECTION = 0x8;

    /**
     * Action that clicks on the node info.
     *
     * See {@link AccessibilityAction#ACTION_CLICK}
     */
    public static final int ACTION_CLICK = 0x10;

    /**
     * Action that long clicks on the node.
     */
    public static final int ACTION_LONG_CLICK = 0x20;

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
     * <strong>Arguments:</strong>
     * {@link #ACTION_ARGUMENT_SELECTION_START_INT},
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
     * <strong>Arguments:</strong>
     * {@link #ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE}<br>
     * <strong>Example:</strong>
     * <code><pre><p>
     *   Bundle arguments = new Bundle();
     *   arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
     *       "android");
     *   info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
     * </code></pre></p>
     */
    public static final int ACTION_SET_TEXT = 0x200000;

    /**
     *
     *
     * @unknown 
     */
    public static final int LAST_LEGACY_STANDARD_ACTION = android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_TEXT;

    /**
     * Mask to see if the value is larger than the largest ACTION_ constant
     */
    private static final int ACTION_TYPE_MASK = 0xff000000;

    // Action arguments
    /**
     * Argument for which movement granularity to be used when traversing the node text.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_NEXT_AT_MOVEMENT_GRANULARITY}</li>
     *     <li>{@link AccessibilityAction#ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY}</li>
     * </ul>
     * </p>
     *
     * @see AccessibilityAction#ACTION_NEXT_AT_MOVEMENT_GRANULARITY
     * @see AccessibilityAction#ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY
     */
    public static final java.lang.String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";

    /**
     * Argument for which HTML element to get moving to the next/previous HTML element.
     * <p>
     * <strong>Type:</strong> String<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_NEXT_HTML_ELEMENT}</li>
     *     <li>{@link AccessibilityAction#ACTION_PREVIOUS_HTML_ELEMENT}</li>
     * </ul>
     * </p>
     *
     * @see AccessibilityAction#ACTION_NEXT_HTML_ELEMENT
     * @see AccessibilityAction#ACTION_PREVIOUS_HTML_ELEMENT
     */
    public static final java.lang.String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";

    /**
     * Argument for whether when moving at granularity to extend the selection
     * or to move it otherwise.
     * <p>
     * <strong>Type:</strong> boolean<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_NEXT_AT_MOVEMENT_GRANULARITY}</li>
     *     <li>{@link AccessibilityAction#ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_NEXT_AT_MOVEMENT_GRANULARITY
     * @see AccessibilityAction#ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY
     */
    public static final java.lang.String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";

    /**
     * Argument for specifying the selection start.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_SET_SELECTION}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_SET_SELECTION
     */
    public static final java.lang.String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";

    /**
     * Argument for specifying the selection end.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_SET_SELECTION}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_SET_SELECTION
     */
    public static final java.lang.String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";

    /**
     * Argument for specifying the text content to set.
     * <p>
     * <strong>Type:</strong> CharSequence<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_SET_TEXT}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_SET_TEXT
     */
    public static final java.lang.String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";

    /**
     * Argument for specifying the collection row to make visible on screen.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_SCROLL_TO_POSITION}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_SCROLL_TO_POSITION
     */
    public static final java.lang.String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";

    /**
     * Argument for specifying the collection column to make visible on screen.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_SCROLL_TO_POSITION}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_SCROLL_TO_POSITION
     */
    public static final java.lang.String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";

    /**
     * Argument for specifying the progress value to set.
     * <p>
     * <strong>Type:</strong> float<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_SET_PROGRESS}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_SET_PROGRESS
     */
    public static final java.lang.String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";

    /**
     * Argument for specifying the x coordinate to which to move a window.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_MOVE_WINDOW}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_MOVE_WINDOW
     */
    public static final java.lang.String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";

    /**
     * Argument for specifying the y coordinate to which to move a window.
     * <p>
     * <strong>Type:</strong> int<br>
     * <strong>Actions:</strong>
     * <ul>
     *     <li>{@link AccessibilityAction#ACTION_MOVE_WINDOW}</li>
     * </ul>
     *
     * @see AccessibilityAction#ACTION_MOVE_WINDOW
     */
    public static final java.lang.String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";

    /**
     * Argument to pass the {@link AccessibilityClickableSpan}.
     * For use with R.id.accessibilityActionClickOnClickableSpan
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN = "android.view.accessibility.action.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN";

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
     * Key used to request and locate extra data for text character location. This key requests that
     * an array of {@link android.graphics.RectF}s be added to the extras. This request is made with
     * {@link #refreshWithExtraData(String, Bundle)}. The arguments taken by this request are two
     * integers: {@link #EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX} and
     * {@link #EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH}. The starting index must be valid
     * inside the CharSequence returned by {@link #getText()}, and the length must be positive.
     * <p>
     * The data can be retrieved from the {@code Bundle} returned by {@link #getExtras()} using this
     * string as a key for {@link Bundle#getParcelableArray(String)}. The
     * {@link android.graphics.RectF} will be null for characters that either do not exist or are
     * off the screen.
     *
     * {@see #refreshWithExtraData(String, Bundle)}
     */
    public static final java.lang.String EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_KEY";

    /**
     * Integer argument specifying the start index of the requested text location data. Must be
     * valid inside the CharSequence returned by {@link #getText()}.
     *
     * @see #EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY
     */
    public static final java.lang.String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX";

    /**
     * Integer argument specifying the end index of the requested text location data. Must be
     * positive.
     *
     * @see #EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY
     */
    public static final java.lang.String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_DATA_REQUESTED_KEY = "android.view.accessibility.AccessibilityNodeInfo.extra_data_requested";

    // Boolean attributes.
    private static final int BOOLEAN_PROPERTY_CHECKABLE = 0x1;

    private static final int BOOLEAN_PROPERTY_CHECKED = 0x2;

    private static final int BOOLEAN_PROPERTY_FOCUSABLE = 0x4;

    private static final int BOOLEAN_PROPERTY_FOCUSED = 0x8;

    private static final int BOOLEAN_PROPERTY_SELECTED = 0x10;

    private static final int BOOLEAN_PROPERTY_CLICKABLE = 0x20;

    private static final int BOOLEAN_PROPERTY_LONG_CLICKABLE = 0x40;

    private static final int BOOLEAN_PROPERTY_ENABLED = 0x80;

    private static final int BOOLEAN_PROPERTY_PASSWORD = 0x100;

    private static final int BOOLEAN_PROPERTY_SCROLLABLE = 0x200;

    private static final int BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED = 0x400;

    private static final int BOOLEAN_PROPERTY_VISIBLE_TO_USER = 0x800;

    private static final int BOOLEAN_PROPERTY_EDITABLE = 0x1000;

    private static final int BOOLEAN_PROPERTY_OPENS_POPUP = 0x2000;

    private static final int BOOLEAN_PROPERTY_DISMISSABLE = 0x4000;

    private static final int BOOLEAN_PROPERTY_MULTI_LINE = 0x8000;

    private static final int BOOLEAN_PROPERTY_CONTENT_INVALID = 0x10000;

    private static final int BOOLEAN_PROPERTY_CONTEXT_CLICKABLE = 0x20000;

    private static final int BOOLEAN_PROPERTY_IMPORTANCE = 0x40000;

    private static final int BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE = 0x80000;

    private static final int BOOLEAN_PROPERTY_IS_SHOWING_HINT = 0x100000;

    private static final int BOOLEAN_PROPERTY_IS_HEADING = 0x200000;

    private static final int BOOLEAN_PROPERTY_IS_TEXT_ENTRY_KEY = 0x400000;

    /**
     * Bits that provide the id of a virtual descendant of a view.
     */
    private static final long VIRTUAL_DESCENDANT_ID_MASK = 0xffffffff00000000L;

    /**
     * Bit shift of {@link #VIRTUAL_DESCENDANT_ID_MASK} to get to the id for a
     * virtual descendant of a view. Such a descendant does not exist in the view
     * hierarchy and is only reported via the accessibility APIs.
     */
    private static final int VIRTUAL_DESCENDANT_ID_SHIFT = 32;

    // TODO(b/129300068): Remove sNumInstancesInUse.
    private static java.util.concurrent.atomic.AtomicInteger sNumInstancesInUse;

    /**
     * Gets the accessibility view id which identifies a View in the view three.
     *
     * @param accessibilityNodeId
     * 		The id of an {@link AccessibilityNodeInfo}.
     * @return The accessibility view id part of the node id.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static int getAccessibilityViewId(long accessibilityNodeId) {
        return ((int) (accessibilityNodeId));
    }

    /**
     * Gets the virtual descendant id which identifies an imaginary view in a
     * containing View.
     *
     * @param accessibilityNodeId
     * 		The id of an {@link AccessibilityNodeInfo}.
     * @return The virtual view id part of the node id.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static int getVirtualDescendantId(long accessibilityNodeId) {
        return ((int) ((accessibilityNodeId & android.view.accessibility.AccessibilityNodeInfo.VIRTUAL_DESCENDANT_ID_MASK) >> android.view.accessibility.AccessibilityNodeInfo.VIRTUAL_DESCENDANT_ID_SHIFT));
    }

    /**
     * Makes a node id by shifting the <code>virtualDescendantId</code>
     * by {@link #VIRTUAL_DESCENDANT_ID_SHIFT} and taking
     * the bitwise or with the <code>accessibilityViewId</code>.
     *
     * @param accessibilityViewId
     * 		A View accessibility id.
     * @param virtualDescendantId
     * 		A virtual descendant id.
     * @return The node id.
     * @unknown 
     */
    public static long makeNodeId(int accessibilityViewId, int virtualDescendantId) {
        return (((long) (virtualDescendantId)) << android.view.accessibility.AccessibilityNodeInfo.VIRTUAL_DESCENDANT_ID_SHIFT) | accessibilityViewId;
    }

    // Housekeeping.
    private static final int MAX_POOL_SIZE = 50;

    private static final android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityNodeInfo> sPool = new android.util.Pools.SynchronizedPool(android.view.accessibility.AccessibilityNodeInfo.MAX_POOL_SIZE);

    private static final android.view.accessibility.AccessibilityNodeInfo DEFAULT = new android.view.accessibility.AccessibilityNodeInfo();

    @android.annotation.UnsupportedAppUsage
    private boolean mSealed;

    // Data.
    private int mWindowId = android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID;

    @android.annotation.UnsupportedAppUsage
    private long mSourceNodeId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private long mParentNodeId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private long mLabelForId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private long mLabeledById = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private long mTraversalBefore = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private long mTraversalAfter = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    private int mBooleanProperties;

    private final android.graphics.Rect mBoundsInParent = new android.graphics.Rect();

    private final android.graphics.Rect mBoundsInScreen = new android.graphics.Rect();

    private int mDrawingOrderInParent;

    private java.lang.CharSequence mPackageName;

    private java.lang.CharSequence mClassName;

    // Hidden, unparceled value used to hold the original value passed to setText
    private java.lang.CharSequence mOriginalText;

    private java.lang.CharSequence mText;

    private java.lang.CharSequence mHintText;

    private java.lang.CharSequence mError;

    private java.lang.CharSequence mPaneTitle;

    private java.lang.CharSequence mContentDescription;

    private java.lang.CharSequence mTooltipText;

    private java.lang.String mViewIdResourceName;

    private java.util.ArrayList<java.lang.String> mExtraDataKeys;

    @android.annotation.UnsupportedAppUsage
    private android.util.LongArray mChildNodeIds;

    private java.util.ArrayList<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> mActions;

    private int mMaxTextLength = -1;

    private int mMovementGranularities;

    private int mTextSelectionStart = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_SELECTION_INDEX;

    private int mTextSelectionEnd = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_SELECTION_INDEX;

    private int mInputType = android.text.InputType.TYPE_NULL;

    private int mLiveRegion = android.view.View.ACCESSIBILITY_LIVE_REGION_NONE;

    private android.os.Bundle mExtras;

    private int mConnectionId = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_CONNECTION_ID;

    private android.view.accessibility.AccessibilityNodeInfo.RangeInfo mRangeInfo;

    private android.view.accessibility.AccessibilityNodeInfo.CollectionInfo mCollectionInfo;

    private android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo mCollectionItemInfo;

    private android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo mTouchDelegateInfo;

    /**
     * Hide constructor from clients.
     */
    private AccessibilityNodeInfo() {
        /* do nothing */
    }

    /**
     *
     *
     * @unknown 
     */
    AccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info) {
        init(info);
    }

    /**
     * Sets the source.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param source
     * 		The info source.
     */
    public void setSource(android.view.View source) {
        setSource(source, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
        enforceNotSealed();
        mWindowId = (root != null) ? root.getAccessibilityWindowId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mSourceNodeId = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
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
    public android.view.accessibility.AccessibilityNodeInfo findFocus(int focus) {
        enforceSealed();
        enforceValidFocusType(focus);
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return null;
        }
        return android.view.accessibility.AccessibilityInteractionClient.getInstance().findFocus(mConnectionId, mWindowId, mSourceNodeId, focus);
    }

    /**
     * Searches for the nearest view in the specified direction that can take
     * the input focus.
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
    public android.view.accessibility.AccessibilityNodeInfo focusSearch(int direction) {
        enforceSealed();
        enforceValidFocusDirection(direction);
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return null;
        }
        return android.view.accessibility.AccessibilityInteractionClient.getInstance().focusSearch(mConnectionId, mWindowId, mSourceNodeId, direction);
    }

    /**
     * Gets the id of the window from which the info comes from.
     *
     * @return The window id.
     */
    public int getWindowId() {
        return mWindowId;
    }

    /**
     * Refreshes this info with the latest state of the view it represents.
     * <p>
     * <strong>Note:</strong> If this method returns false this info is obsolete
     * since it represents a view that is no longer in the view tree and should
     * be recycled.
     * </p>
     *
     * @param bypassCache
     * 		Whether to bypass the cache.
     * @return Whether the refresh succeeded.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean refresh(android.os.Bundle arguments, boolean bypassCache) {
        enforceSealed();
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return false;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        android.view.accessibility.AccessibilityNodeInfo refreshedInfo = client.findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mWindowId, mSourceNodeId, bypassCache, 0, arguments);
        if (refreshedInfo == null) {
            return false;
        }
        // Hard-to-reproduce bugs seem to be due to some tools recycling a node on another
        // thread. If that happens, the init will re-seal the node, which then is in a bad state
        // when it is obtained. Enforce sealing again before we init to fail when a node has been
        // recycled during a refresh to catch such errors earlier.
        enforceSealed();
        init(refreshedInfo);
        refreshedInfo.recycle();
        return true;
    }

    /**
     * Refreshes this info with the latest state of the view it represents.
     *
     * @return {@code true} if the refresh succeeded. {@code false} if the {@link View} represented
    by this node is no longer in the view tree (and thus this node is obsolete and should be
    recycled).
     */
    public boolean refresh() {
        return refresh(null, true);
    }

    /**
     * Refreshes this info with the latest state of the view it represents, and request new
     * data be added by the View.
     *
     * @param extraDataKey
     * 		The extra data requested. Data that must be requested
     * 		with this mechanism is generally expensive to retrieve, so should only be
     * 		requested when needed. See
     * 		{@link #EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY} and
     * 		{@link #getAvailableExtraData()}.
     * @param args
     * 		A bundle of arguments for the request. These depend on the particular request.
     * @return {@code true} if the refresh succeeded. {@code false} if the {@link View} represented
    by this node is no longer in the view tree (and thus this node is obsolete and should be
    recycled).
     */
    public boolean refreshWithExtraData(java.lang.String extraDataKey, android.os.Bundle args) {
        args.putString(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_REQUESTED_KEY, extraDataKey);
        return refresh(args, true);
    }

    /**
     * Returns the array containing the IDs of this node's children.
     *
     * @unknown 
     */
    public android.util.LongArray getChildNodeIds() {
        return mChildNodeIds;
    }

    /**
     * Returns the id of the child at the specified index.
     *
     * @throws IndexOutOfBoundsException
     * 		when index &lt; 0 || index &gt;=
     * 		getChildCount()
     * @unknown 
     */
    public long getChildId(int index) {
        if (mChildNodeIds == null) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return mChildNodeIds.get(index);
    }

    /**
     * Gets the number of children.
     *
     * @return The child count.
     */
    public int getChildCount() {
        return mChildNodeIds == null ? 0 : mChildNodeIds.size();
    }

    /**
     * Get the child at given index.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfo#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @param index
     * 		The child index.
     * @return The child node.
     * @throws IllegalStateException
     * 		If called outside of an AccessibilityService.
     */
    public android.view.accessibility.AccessibilityNodeInfo getChild(int index) {
        enforceSealed();
        if (mChildNodeIds == null) {
            return null;
        }
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return null;
        }
        final long childId = mChildNodeIds.get(index);
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mWindowId, childId, false, android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS, null);
    }

    /**
     * Adds a child.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}.
     * This class is made immutable before being delivered to an AccessibilityService.
     * Note that a view cannot be made its own child.
     * </p>
     *
     * @param child
     * 		The child.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void addChild(android.view.View child) {
        addChildInternal(child, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID, true);
    }

    /**
     * Unchecked version of {@link #addChild(View)} that does not verify
     * uniqueness. For framework use only.
     *
     * @unknown 
     */
    public void addChildUnchecked(android.view.View child) {
        addChildInternal(child, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID, false);
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
        return removeChild(child, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
     * Note that a view cannot be made its own child.
     * </p>
     *
     * @param root
     * 		The root of the virtual subtree.
     * @param virtualDescendantId
     * 		The id of the virtual child.
     */
    public void addChild(android.view.View root, int virtualDescendantId) {
        addChildInternal(root, virtualDescendantId, true);
    }

    private void addChildInternal(android.view.View root, int virtualDescendantId, boolean checked) {
        enforceNotSealed();
        if (mChildNodeIds == null) {
            mChildNodeIds = new android.util.LongArray();
        }
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        final long childNodeId = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
        if (childNodeId == mSourceNodeId) {
            android.util.Log.e(android.view.accessibility.AccessibilityNodeInfo.TAG, "Rejecting attempt to make a View its own child");
            return;
        }
        // If we're checking uniqueness and the ID already exists, abort.
        if (checked && (mChildNodeIds.indexOf(childNodeId) >= 0)) {
            return;
        }
        mChildNodeIds.add(childNodeId);
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
        enforceNotSealed();
        final android.util.LongArray childIds = mChildNodeIds;
        if (childIds == null) {
            return false;
        }
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        final long childNodeId = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
        final int index = childIds.indexOf(childNodeId);
        if (index < 0) {
            return false;
        }
        childIds.remove(index);
        return true;
    }

    /**
     * Gets the actions that can be performed on the node.
     */
    public java.util.List<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> getActionList() {
        return com.android.internal.util.CollectionUtils.emptyIfNull(mActions);
    }

    /**
     * Gets the actions that can be performed on the node.
     *
     * @return The bit mask of with actions.
     * @see AccessibilityNodeInfo#ACTION_FOCUS
     * @see AccessibilityNodeInfo#ACTION_CLEAR_FOCUS
     * @see AccessibilityNodeInfo#ACTION_SELECT
     * @see AccessibilityNodeInfo#ACTION_CLEAR_SELECTION
     * @see AccessibilityNodeInfo#ACTION_ACCESSIBILITY_FOCUS
     * @see AccessibilityNodeInfo#ACTION_CLEAR_ACCESSIBILITY_FOCUS
     * @see AccessibilityNodeInfo#ACTION_CLICK
     * @see AccessibilityNodeInfo#ACTION_LONG_CLICK
     * @see AccessibilityNodeInfo#ACTION_NEXT_AT_MOVEMENT_GRANULARITY
     * @see AccessibilityNodeInfo#ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY
     * @see AccessibilityNodeInfo#ACTION_NEXT_HTML_ELEMENT
     * @see AccessibilityNodeInfo#ACTION_PREVIOUS_HTML_ELEMENT
     * @see AccessibilityNodeInfo#ACTION_SCROLL_FORWARD
     * @see AccessibilityNodeInfo#ACTION_SCROLL_BACKWARD
     * @deprecated Use {@link #getActionList()}.
     */
    @java.lang.Deprecated
    public int getActions() {
        int returnValue = 0;
        if (mActions == null) {
            return returnValue;
        }
        final int actionSize = mActions.size();
        for (int i = 0; i < actionSize; i++) {
            int actionId = mActions.get(i).getId();
            if (actionId <= android.view.accessibility.AccessibilityNodeInfo.LAST_LEGACY_STANDARD_ACTION) {
                returnValue |= actionId;
            }
        }
        return returnValue;
    }

    /**
     * Adds an action that can be performed on the node.
     * <p>
     * To add a standard action use the static constants on {@link AccessibilityAction}.
     * To add a custom action create a new {@link AccessibilityAction} by passing in a
     * resource id from your application as the action id and an optional label that
     * describes the action. To override one of the standard actions use as the action
     * id of a standard action id such as {@link #ACTION_CLICK} and an optional label that
     * describes the action.
     * </p>
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param action
     * 		The action.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
        enforceNotSealed();
        addActionUnchecked(action);
    }

    private void addActionUnchecked(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
        if (action == null) {
            return;
        }
        if (mActions == null) {
            mActions = new java.util.ArrayList<>();
        }
        mActions.remove(action);
        mActions.add(action);
    }

    /**
     * Adds an action that can be performed on the node.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param action
     * 		The action.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     * @throws IllegalArgumentException
     * 		If the argument is not one of the standard actions.
     * @deprecated This has been deprecated for {@link #addAction(AccessibilityAction)}
     */
    @java.lang.Deprecated
    public void addAction(int action) {
        enforceNotSealed();
        if ((action & android.view.accessibility.AccessibilityNodeInfo.ACTION_TYPE_MASK) != 0) {
            throw new java.lang.IllegalArgumentException(("Action is not a combination of the standard " + "actions: ") + action);
        }
        addStandardActions(action);
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
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     * @deprecated Use {@link #removeAction(AccessibilityAction)}
     */
    @java.lang.Deprecated
    public void removeAction(int action) {
        enforceNotSealed();
        removeAction(android.view.accessibility.AccessibilityNodeInfo.getActionSingleton(action));
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
    public boolean removeAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
        enforceNotSealed();
        if ((mActions == null) || (action == null)) {
            return false;
        }
        return mActions.remove(action);
    }

    /**
     * Removes all actions.
     *
     * @unknown 
     */
    public void removeAllActions() {
        if (mActions != null) {
            mActions.clear();
        }
    }

    /**
     * Gets the node before which this one is visited during traversal. A screen-reader
     * must visit the content of this node before the content of the one it precedes.
     *
     * @return The succeeding node if such or <code>null</code>.
     * @see #setTraversalBefore(android.view.View)
     * @see #setTraversalBefore(android.view.View, int)
     */
    public android.view.accessibility.AccessibilityNodeInfo getTraversalBefore() {
        enforceSealed();
        return android.view.accessibility.AccessibilityNodeInfo.getNodeForAccessibilityId(mConnectionId, mWindowId, mTraversalBefore);
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
        setTraversalBefore(view, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
        enforceNotSealed();
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mTraversalBefore = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
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
    public android.view.accessibility.AccessibilityNodeInfo getTraversalAfter() {
        enforceSealed();
        return android.view.accessibility.AccessibilityNodeInfo.getNodeForAccessibilityId(mConnectionId, mWindowId, mTraversalAfter);
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
        setTraversalAfter(view, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
        enforceNotSealed();
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mTraversalAfter = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
    }

    /**
     * Get the extra data available for this node.
     * <p>
     * Some data that is useful for some accessibility services is expensive to compute, and would
     * place undue overhead on apps to compute all the time. That data can be requested with
     * {@link #refreshWithExtraData(String, Bundle)}.
     *
     * @return An unmodifiable list of keys corresponding to extra data that can be requested.
     * @see #EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY
     */
    public java.util.List<java.lang.String> getAvailableExtraData() {
        if (mExtraDataKeys != null) {
            return java.util.Collections.unmodifiableList(mExtraDataKeys);
        } else {
            return java.util.Collections.EMPTY_LIST;
        }
    }

    /**
     * Set the extra data available for this node.
     * <p>
     * <strong>Note:</strong> When a {@code View} passes in a non-empty list, it promises that
     * it will populate the node's extras with corresponding pieces of information in
     * {@link View#addExtraDataToAccessibilityNodeInfo(AccessibilityNodeInfo, String, Bundle)}.
     * <p>
     * <strong>Note:</strong> Cannot be called from an
     * {@link android.accessibilityservice.AccessibilityService}.
     * This class is made immutable before being delivered to an AccessibilityService.
     *
     * @param extraDataKeys
     * 		A list of types of extra data that are available.
     * @see #getAvailableExtraData()
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setAvailableExtraData(java.util.List<java.lang.String> extraDataKeys) {
        enforceNotSealed();
        mExtraDataKeys = new java.util.ArrayList<>(extraDataKeys);
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
        enforceNotSealed();
        mMaxTextLength = max;
    }

    /**
     * Returns the maximum text length for this node.
     *
     * @return The maximum text length, or -1 for no limit.
     * @see #setMaxTextLength(int)
     */
    public int getMaxTextLength() {
        return mMaxTextLength;
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
        enforceNotSealed();
        mMovementGranularities = granularities;
    }

    /**
     * Gets the movement granularities for traversing the text of this node.
     *
     * @return The bit mask with granularities.
     */
    public int getMovementGranularities() {
        return mMovementGranularities;
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
     * @return True if the action was performed.
     * @throws IllegalStateException
     * 		If called outside of an AccessibilityService.
     */
    public boolean performAction(int action) {
        enforceSealed();
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return false;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.performAccessibilityAction(mConnectionId, mWindowId, mSourceNodeId, action, null);
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
        enforceSealed();
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return false;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.performAccessibilityAction(mConnectionId, mWindowId, mSourceNodeId, action, arguments);
    }

    /**
     * Finds {@link AccessibilityNodeInfo}s by text. The match is case
     * insensitive containment. The search is relative to this info i.e.
     * this info is the root of the traversed tree.
     *
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfo#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @param text
     * 		The searched text.
     * @return A list of node info.
     */
    public java.util.List<android.view.accessibility.AccessibilityNodeInfo> findAccessibilityNodeInfosByText(java.lang.String text) {
        enforceSealed();
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return java.util.Collections.emptyList();
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfosByText(mConnectionId, mWindowId, mSourceNodeId, text);
    }

    /**
     * Finds {@link AccessibilityNodeInfo}s by the fully qualified view id's resource
     * name where a fully qualified id is of the from "package:id/id_resource_name".
     * For example, if the target application's package is "foo.bar" and the id
     * resource name is "baz", the fully qualified resource id is "foo.bar:id/baz".
     *
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfo#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     * <p>
     *   <strong>Note:</strong> The primary usage of this API is for UI test automation
     *   and in order to report the fully qualified view id if an {@link AccessibilityNodeInfo}
     *   the client has to set the {@link AccessibilityServiceInfo#FLAG_REPORT_VIEW_IDS}
     *   flag when configuring his {@link android.accessibilityservice.AccessibilityService}.
     * </p>
     *
     * @param viewId
     * 		The fully qualified resource name of the view id to find.
     * @return A list of node info.
     */
    public java.util.List<android.view.accessibility.AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(java.lang.String viewId) {
        enforceSealed();
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return java.util.Collections.emptyList();
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfosByViewId(mConnectionId, mWindowId, mSourceNodeId, viewId);
    }

    /**
     * Gets the window to which this node belongs.
     *
     * @return The window.
     * @see android.accessibilityservice.AccessibilityService#getWindows()
     */
    public android.view.accessibility.AccessibilityWindowInfo getWindow() {
        enforceSealed();
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(mConnectionId, mWindowId, mSourceNodeId)) {
            return null;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.getWindow(mConnectionId, mWindowId);
    }

    /**
     * Gets the parent.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfo#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @return The parent.
     */
    public android.view.accessibility.AccessibilityNodeInfo getParent() {
        enforceSealed();
        return android.view.accessibility.AccessibilityNodeInfo.getNodeForAccessibilityId(mConnectionId, mWindowId, mParentNodeId);
    }

    /**
     *
     *
     * @return The parent node id.
     * @unknown 
     */
    public long getParentNodeId() {
        return mParentNodeId;
    }

    /**
     * Sets the parent.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param parent
     * 		The parent.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setParent(android.view.View parent) {
        setParent(parent, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
        enforceNotSealed();
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mParentNodeId = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
    }

    /**
     * Gets the node bounds in the viewParent's coordinates.
     * {@link #getParent()} does not represent the source's viewParent.
     * Instead it represents the result of {@link View#getParentForAccessibility()},
     * which returns the closest ancestor where {@link View#isImportantForAccessibility()} is true.
     * So this method is not reliable.
     *
     * @param outBounds
     * 		The output node bounds.
     * @deprecated Use {@link #getBoundsInScreen(Rect)} instead.
     */
    @java.lang.Deprecated
    public void getBoundsInParent(android.graphics.Rect outBounds) {
        outBounds.set(mBoundsInParent.left, mBoundsInParent.top, mBoundsInParent.right, mBoundsInParent.bottom);
    }

    /**
     * Sets the node bounds in the viewParent's coordinates.
     * {@link #getParent()} does not represent the source's viewParent.
     * Instead it represents the result of {@link View#getParentForAccessibility()},
     * which returns the closest ancestor where {@link View#isImportantForAccessibility()} is true.
     * So this method is not reliable.
     *
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param bounds
     * 		The node bounds.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     * @deprecated Accessibility services should not care about these bounds.
     */
    @java.lang.Deprecated
    public void setBoundsInParent(android.graphics.Rect bounds) {
        enforceNotSealed();
        mBoundsInParent.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    /**
     * Gets the node bounds in screen coordinates.
     *
     * @param outBounds
     * 		The output node bounds.
     */
    public void getBoundsInScreen(android.graphics.Rect outBounds) {
        outBounds.set(mBoundsInScreen.left, mBoundsInScreen.top, mBoundsInScreen.right, mBoundsInScreen.bottom);
    }

    /**
     * Returns the actual rect containing the node bounds in screen coordinates.
     *
     * @unknown Not safe to expose outside the framework.
     */
    public android.graphics.Rect getBoundsInScreen() {
        return mBoundsInScreen;
    }

    /**
     * Sets the node bounds in screen coordinates.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param bounds
     * 		The node bounds.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setBoundsInScreen(android.graphics.Rect bounds) {
        enforceNotSealed();
        mBoundsInScreen.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    /**
     * Gets whether this node is checkable.
     *
     * @return True if the node is checkable.
     */
    public boolean isCheckable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CHECKABLE);
    }

    /**
     * Sets whether this node is checkable.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param checkable
     * 		True if the node is checkable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setCheckable(boolean checkable) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CHECKABLE, checkable);
    }

    /**
     * Gets whether this node is checked.
     *
     * @return True if the node is checked.
     */
    public boolean isChecked() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CHECKED);
    }

    /**
     * Sets whether this node is checked.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param checked
     * 		True if the node is checked.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setChecked(boolean checked) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CHECKED, checked);
    }

    /**
     * Gets whether this node is focusable.
     *
     * @return True if the node is focusable.
     */
    public boolean isFocusable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_FOCUSABLE);
    }

    /**
     * Sets whether this node is focusable.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param focusable
     * 		True if the node is focusable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setFocusable(boolean focusable) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_FOCUSABLE, focusable);
    }

    /**
     * Gets whether this node is focused.
     *
     * @return True if the node is focused.
     */
    public boolean isFocused() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_FOCUSED);
    }

    /**
     * Sets whether this node is focused.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param focused
     * 		True if the node is focused.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setFocused(boolean focused) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_FOCUSED, focused);
    }

    /**
     * Gets whether this node is visible to the user.
     *
     * @return Whether the node is visible to the user.
     */
    public boolean isVisibleToUser() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_VISIBLE_TO_USER);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_VISIBLE_TO_USER, visibleToUser);
    }

    /**
     * Gets whether this node is accessibility focused.
     *
     * @return True if the node is accessibility focused.
     */
    public boolean isAccessibilityFocused() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED, focused);
    }

    /**
     * Gets whether this node is selected.
     *
     * @return True if the node is selected.
     */
    public boolean isSelected() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_SELECTED);
    }

    /**
     * Sets whether this node is selected.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param selected
     * 		True if the node is selected.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setSelected(boolean selected) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_SELECTED, selected);
    }

    /**
     * Gets whether this node is clickable.
     *
     * @return True if the node is clickable.
     */
    public boolean isClickable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CLICKABLE);
    }

    /**
     * Sets whether this node is clickable.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param clickable
     * 		True if the node is clickable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setClickable(boolean clickable) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CLICKABLE, clickable);
    }

    /**
     * Gets whether this node is long clickable.
     *
     * @return True if the node is long clickable.
     */
    public boolean isLongClickable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_LONG_CLICKABLE);
    }

    /**
     * Sets whether this node is long clickable.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param longClickable
     * 		True if the node is long clickable.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setLongClickable(boolean longClickable) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_LONG_CLICKABLE, longClickable);
    }

    /**
     * Gets whether this node is enabled.
     *
     * @return True if the node is enabled.
     */
    public boolean isEnabled() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_ENABLED);
    }

    /**
     * Sets whether this node is enabled.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param enabled
     * 		True if the node is enabled.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setEnabled(boolean enabled) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_ENABLED, enabled);
    }

    /**
     * Gets whether this node is a password.
     *
     * @return True if the node is a password.
     */
    public boolean isPassword() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_PASSWORD);
    }

    /**
     * Sets whether this node is a password.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param password
     * 		True if the node is a password.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setPassword(boolean password) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_PASSWORD, password);
    }

    /**
     * Gets if the node is scrollable.
     *
     * @return True if the node is scrollable, false otherwise.
     */
    public boolean isScrollable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_SCROLLABLE);
    }

    /**
     * Sets if the node is scrollable.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param scrollable
     * 		True if the node is scrollable, false otherwise.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setScrollable(boolean scrollable) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_SCROLLABLE, scrollable);
    }

    /**
     * Gets if the node is editable.
     *
     * @return True if the node is editable, false otherwise.
     */
    public boolean isEditable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_EDITABLE);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_EDITABLE, editable);
    }

    /**
     * If this node represents a visually distinct region of the screen that may update separately
     * from the rest of the window, it is considered a pane. Set the pane title to indicate that
     * the node is a pane, and to provide a title for it.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param paneTitle
     * 		The title of the pane represented by this node.
     */
    public void setPaneTitle(@android.annotation.Nullable
    java.lang.CharSequence paneTitle) {
        enforceNotSealed();
        mPaneTitle = (paneTitle == null) ? null : paneTitle.subSequence(0, paneTitle.length());
    }

    /**
     * Get the title of the pane represented by this node.
     *
     * @return The title of the pane represented by this node, or {@code null} if this node does
    not represent a pane.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getPaneTitle() {
        return mPaneTitle;
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
        return mDrawingOrderInParent;
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
        enforceNotSealed();
        mDrawingOrderInParent = drawingOrderInParent;
    }

    /**
     * Gets the collection info if the node is a collection. A collection
     * child is always a collection item.
     *
     * @return The collection info.
     */
    public android.view.accessibility.AccessibilityNodeInfo.CollectionInfo getCollectionInfo() {
        return mCollectionInfo;
    }

    /**
     * Sets the collection info if the node is a collection. A collection
     * child is always a collection item.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param collectionInfo
     * 		The collection info.
     */
    public void setCollectionInfo(android.view.accessibility.AccessibilityNodeInfo.CollectionInfo collectionInfo) {
        enforceNotSealed();
        mCollectionInfo = collectionInfo;
    }

    /**
     * Gets the collection item info if the node is a collection item. A collection
     * item is always a child of a collection.
     *
     * @return The collection item info.
     */
    public android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo getCollectionItemInfo() {
        return mCollectionItemInfo;
    }

    /**
     * Sets the collection item info if the node is a collection item. A collection
     * item is always a child of a collection.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     */
    public void setCollectionItemInfo(android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo collectionItemInfo) {
        enforceNotSealed();
        mCollectionItemInfo = collectionItemInfo;
    }

    /**
     * Gets the range info if this node is a range.
     *
     * @return The range.
     */
    public android.view.accessibility.AccessibilityNodeInfo.RangeInfo getRangeInfo() {
        return mRangeInfo;
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
    public void setRangeInfo(android.view.accessibility.AccessibilityNodeInfo.RangeInfo rangeInfo) {
        enforceNotSealed();
        mRangeInfo = rangeInfo;
    }

    /**
     * Gets if the content of this node is invalid. For example,
     * a date is not well-formed.
     *
     * @return If the node content is invalid.
     */
    public boolean isContentInvalid() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CONTENT_INVALID);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CONTENT_INVALID, contentInvalid);
    }

    /**
     * Gets whether this node is context clickable.
     *
     * @return True if the node is context clickable.
     */
    public boolean isContextClickable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CONTEXT_CLICKABLE);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_CONTEXT_CLICKABLE, contextClickable);
    }

    /**
     * Gets the node's live region mode.
     * <p>
     * A live region is a node that contains information that is important for
     * the user and when it changes the user should be notified. For example,
     * in a login screen with a TextView that displays an "incorrect password"
     * notification, that view should be marked as a live region with mode
     * {@link View#ACCESSIBILITY_LIVE_REGION_POLITE}.
     * <p>
     * It is the responsibility of the accessibility service to monitor
     * {@link AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED} events indicating
     * changes to live region nodes and their children.
     *
     * @return The live region mode, or
    {@link View#ACCESSIBILITY_LIVE_REGION_NONE} if the view is not a
    live region.
     * @see android.view.View#getAccessibilityLiveRegion()
     */
    public int getLiveRegion() {
        return mLiveRegion;
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
     * 		{@link View#ACCESSIBILITY_LIVE_REGION_NONE} if the view is not a
     * 		live region.
     * @see android.view.View#setAccessibilityLiveRegion(int)
     */
    public void setLiveRegion(int mode) {
        enforceNotSealed();
        mLiveRegion = mode;
    }

    /**
     * Gets if the node is a multi line editable text.
     *
     * @return True if the node is multi line.
     */
    public boolean isMultiLine() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_MULTI_LINE);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_MULTI_LINE, multiLine);
    }

    /**
     * Gets if this node opens a popup or a dialog.
     *
     * @return If the the node opens a popup.
     */
    public boolean canOpenPopup() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_OPENS_POPUP);
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
        enforceNotSealed();
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_OPENS_POPUP, opensPopup);
    }

    /**
     * Gets if the node can be dismissed.
     *
     * @return If the node can be dismissed.
     */
    public boolean isDismissable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_DISMISSABLE);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_DISMISSABLE, dismissable);
    }

    /**
     * Returns whether the node originates from a view considered important for accessibility.
     *
     * @return {@code true} if the node originates from a view considered important for
    accessibility, {@code false} otherwise
     * @see View#isImportantForAccessibility()
     */
    public boolean isImportantForAccessibility() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IMPORTANCE);
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
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IMPORTANCE, important);
    }

    /**
     * Returns whether the node is explicitly marked as a focusable unit by a screen reader. Note
     * that {@code false} indicates that it is not explicitly marked, not that the node is not
     * a focusable unit. Screen readers should generally use other signals, such as
     * {@link #isFocusable()}, or the presence of text in a node, to determine what should receive
     * focus.
     *
     * @return {@code true} if the node is specifically marked as a focusable unit for screen
    readers, {@code false} otherwise.
     * @see View#isScreenReaderFocusable()
     */
    public boolean isScreenReaderFocusable() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE);
    }

    /**
     * Sets whether the node should be considered a focusable unit by a screen reader.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param screenReaderFocusable
     * 		{@code true} if the node is a focusable unit for screen readers,
     * 		{@code false} otherwise.
     */
    public void setScreenReaderFocusable(boolean screenReaderFocusable) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE, screenReaderFocusable);
    }

    /**
     * Returns whether the node's text represents a hint for the user to enter text. It should only
     * be {@code true} if the node has editable text.
     *
     * @return {@code true} if the text in the node represents a hint to the user, {@code false}
    otherwise.
     */
    public boolean isShowingHintText() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IS_SHOWING_HINT);
    }

    /**
     * Sets whether the node's text represents a hint for the user to enter text. It should only
     * be {@code true} if the node has editable text.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param showingHintText
     * 		{@code true} if the text in the node represents a hint to the user,
     * 		{@code false} otherwise.
     */
    public void setShowingHintText(boolean showingHintText) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IS_SHOWING_HINT, showingHintText);
    }

    /**
     * Returns whether node represents a heading.
     * <p><strong>Note:</strong> Returns {@code true} if either {@link #setHeading(boolean)}
     * marks this node as a heading or if the node has a {@link CollectionItemInfo} that marks
     * it as such, to accomodate apps that use the now-deprecated API.</p>
     *
     * @return {@code true} if the node is a heading, {@code false} otherwise.
     */
    public boolean isHeading() {
        if (getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IS_HEADING))
            return true;

        android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo itemInfo = getCollectionItemInfo();
        return (itemInfo != null) && itemInfo.mHeading;
    }

    /**
     * Sets whether the node represents a heading.
     *
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param isHeading
     * 		{@code true} if the node is a heading, {@code false} otherwise.
     */
    public void setHeading(boolean isHeading) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IS_HEADING, isHeading);
    }

    /**
     * Returns whether node represents a text entry key that is part of a keyboard or keypad.
     *
     * @return {@code true} if the node is a text entry key., {@code false} otherwise.
     */
    public boolean isTextEntryKey() {
        return getBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IS_TEXT_ENTRY_KEY);
    }

    /**
     * Sets whether the node represents a text entry key that is part of a keyboard or keypad.
     *
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param isTextEntryKey
     * 		{@code true} if the node is a text entry key, {@code false} otherwise.
     */
    public void setTextEntryKey(boolean isTextEntryKey) {
        setBooleanProperty(android.view.accessibility.AccessibilityNodeInfo.BOOLEAN_PROPERTY_IS_TEXT_ENTRY_KEY, isTextEntryKey);
    }

    /**
     * Gets the package this node comes from.
     *
     * @return The package name.
     */
    public java.lang.CharSequence getPackageName() {
        return mPackageName;
    }

    /**
     * Sets the package this node comes from.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param packageName
     * 		The package name.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setPackageName(java.lang.CharSequence packageName) {
        enforceNotSealed();
        mPackageName = packageName;
    }

    /**
     * Gets the class this node comes from.
     *
     * @return The class name.
     */
    public java.lang.CharSequence getClassName() {
        return mClassName;
    }

    /**
     * Sets the class this node comes from.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param className
     * 		The class name.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setClassName(java.lang.CharSequence className) {
        enforceNotSealed();
        mClassName = className;
    }

    /**
     * Gets the text of this node.
     * <p>
     *   <strong>Note:</strong> If the text contains {@link ClickableSpan}s or {@link URLSpan}s,
     *   these spans will have been replaced with ones whose {@link ClickableSpan#onClick(View)}
     *   can be called from an {@link AccessibilityService}. When called from a service, the
     *   {@link View} argument is ignored and the corresponding span will be found on the view that
     *   this {@code AccessibilityNodeInfo} represents and called with that view as its argument.
     *   <p>
     *   This treatment of {@link ClickableSpan}s means that the text returned from this method may
     *   different slightly one passed to {@link #setText(CharSequence)}, although they will be
     *   equivalent according to {@link TextUtils#equals(CharSequence, CharSequence)}. The
     *   {@link ClickableSpan#onClick(View)} of any spans, however, will generally not work outside
     *   of an accessibility service.
     * </p>
     *
     * @return The text.
     */
    public java.lang.CharSequence getText() {
        // Attach this node to any spans that need it
        if (mText instanceof android.text.Spanned) {
            android.text.Spanned spanned = ((android.text.Spanned) (mText));
            android.text.style.AccessibilityClickableSpan[] clickableSpans = spanned.getSpans(0, mText.length(), android.text.style.AccessibilityClickableSpan.class);
            for (int i = 0; i < clickableSpans.length; i++) {
                clickableSpans[i].copyConnectionDataFrom(this);
            }
            android.text.style.AccessibilityURLSpan[] urlSpans = spanned.getSpans(0, mText.length(), android.text.style.AccessibilityURLSpan.class);
            for (int i = 0; i < urlSpans.length; i++) {
                urlSpans[i].copyConnectionDataFrom(this);
            }
        }
        return mText;
    }

    /**
     * Get the text passed to setText before any changes to the spans.
     *
     * @unknown 
     */
    public java.lang.CharSequence getOriginalText() {
        return mOriginalText;
    }

    /**
     * Sets the text of this node.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param text
     * 		The text.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setText(java.lang.CharSequence text) {
        enforceNotSealed();
        mOriginalText = text;
        // Replace any ClickableSpans in mText with placeholders
        if (text instanceof android.text.Spanned) {
            android.text.style.ClickableSpan[] spans = ((android.text.Spanned) (text)).getSpans(0, text.length(), android.text.style.ClickableSpan.class);
            if (spans.length > 0) {
                android.text.Spannable spannable = new android.text.SpannableStringBuilder(text);
                for (int i = 0; i < spans.length; i++) {
                    android.text.style.ClickableSpan span = spans[i];
                    if ((span instanceof android.text.style.AccessibilityClickableSpan) || (span instanceof android.text.style.AccessibilityURLSpan)) {
                        // We've already done enough
                        break;
                    }
                    int spanToReplaceStart = spannable.getSpanStart(span);
                    int spanToReplaceEnd = spannable.getSpanEnd(span);
                    int spanToReplaceFlags = spannable.getSpanFlags(span);
                    spannable.removeSpan(span);
                    android.text.style.ClickableSpan replacementSpan = (span instanceof android.text.style.URLSpan) ? new android.text.style.AccessibilityURLSpan(((android.text.style.URLSpan) (span))) : new android.text.style.AccessibilityClickableSpan(span.getId());
                    spannable.setSpan(replacementSpan, spanToReplaceStart, spanToReplaceEnd, spanToReplaceFlags);
                }
                mText = spannable;
                return;
            }
        }
        mText = (text == null) ? null : text.subSequence(0, text.length());
    }

    /**
     * Gets the hint text of this node. Only applies to nodes where text can be entered.
     *
     * @return The hint text.
     */
    public java.lang.CharSequence getHintText() {
        return mHintText;
    }

    /**
     * Sets the hint text of this node. Only applies to nodes where text can be entered.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param hintText
     * 		The hint text for this mode.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setHintText(java.lang.CharSequence hintText) {
        enforceNotSealed();
        mHintText = (hintText == null) ? null : hintText.subSequence(0, hintText.length());
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
        enforceNotSealed();
        mError = (error == null) ? null : error.subSequence(0, error.length());
    }

    /**
     * Gets the error text of this node.
     *
     * @return The error text.
     */
    public java.lang.CharSequence getError() {
        return mError;
    }

    /**
     * Gets the content description of this node.
     *
     * @return The content description.
     */
    public java.lang.CharSequence getContentDescription() {
        return mContentDescription;
    }

    /**
     * Sets the content description of this node.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param contentDescription
     * 		The content description.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setContentDescription(java.lang.CharSequence contentDescription) {
        enforceNotSealed();
        mContentDescription = (contentDescription == null) ? null : contentDescription.subSequence(0, contentDescription.length());
    }

    /**
     * Gets the tooltip text of this node.
     *
     * @return The tooltip text.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getTooltipText() {
        return mTooltipText;
    }

    /**
     * Sets the tooltip text of this node.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param tooltipText
     * 		The tooltip text.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setTooltipText(@android.annotation.Nullable
    java.lang.CharSequence tooltipText) {
        enforceNotSealed();
        mTooltipText = (tooltipText == null) ? null : tooltipText.subSequence(0, tooltipText.length());
    }

    /**
     * Sets the view for which the view represented by this info serves as a
     * label for accessibility purposes.
     *
     * @param labeled
     * 		The view for which this info serves as a label.
     */
    public void setLabelFor(android.view.View labeled) {
        setLabelFor(labeled, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an AccessibilityService.
     * </p>
     *
     * @param root
     * 		The root whose virtual descendant serves as a label.
     * @param virtualDescendantId
     * 		The id of the virtual descendant.
     */
    public void setLabelFor(android.view.View root, int virtualDescendantId) {
        enforceNotSealed();
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mLabelForId = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
    }

    /**
     * Gets the node info for which the view represented by this info serves as
     * a label for accessibility purposes.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfo#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @return The labeled info.
     */
    public android.view.accessibility.AccessibilityNodeInfo getLabelFor() {
        enforceSealed();
        return android.view.accessibility.AccessibilityNodeInfo.getNodeForAccessibilityId(mConnectionId, mWindowId, mLabelForId);
    }

    /**
     * Sets the view which serves as the label of the view represented by
     * this info for accessibility purposes.
     *
     * @param label
     * 		The view that labels this node's source.
     */
    public void setLabeledBy(android.view.View label) {
        setLabeledBy(label, android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID);
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
        enforceNotSealed();
        final int rootAccessibilityViewId = (root != null) ? root.getAccessibilityViewId() : android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID;
        mLabeledById = android.view.accessibility.AccessibilityNodeInfo.makeNodeId(rootAccessibilityViewId, virtualDescendantId);
    }

    /**
     * Gets the node info which serves as the label of the view represented by
     * this info for accessibility purposes.
     * <p>
     *   <strong>Note:</strong> It is a client responsibility to recycle the
     *     received info by calling {@link AccessibilityNodeInfo#recycle()}
     *     to avoid creating of multiple instances.
     * </p>
     *
     * @return The label.
     */
    public android.view.accessibility.AccessibilityNodeInfo getLabeledBy() {
        enforceSealed();
        return android.view.accessibility.AccessibilityNodeInfo.getNodeForAccessibilityId(mConnectionId, mWindowId, mLabeledById);
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
     * @param viewIdResName
     * 		The id resource name.
     */
    public void setViewIdResourceName(java.lang.String viewIdResName) {
        enforceNotSealed();
        mViewIdResourceName = viewIdResName;
    }

    /**
     * Gets the fully qualified resource name of the source view's id.
     *
     * <p>
     *   <strong>Note:</strong> The primary usage of this API is for UI test automation
     *   and in order to report the source view id of an {@link AccessibilityNodeInfo} the
     *   client has to set the {@link AccessibilityServiceInfo#FLAG_REPORT_VIEW_IDS}
     *   flag when configuring his {@link android.accessibilityservice.AccessibilityService}.
     * </p>
     *
     * @return The id resource name.
     */
    public java.lang.String getViewIdResourceName() {
        return mViewIdResourceName;
    }

    /**
     * Gets the text selection start or the cursor position.
     * <p>
     * If no text is selected, both this method and
     * {@link AccessibilityNodeInfo#getTextSelectionEnd()} return the same value:
     * the current location of the cursor.
     * </p>
     *
     * @return The text selection start, the cursor location if there is no selection, or -1 if
    there is no text selection and no cursor.
     */
    public int getTextSelectionStart() {
        return mTextSelectionStart;
    }

    /**
     * Gets the text selection end if text is selected.
     * <p>
     * If no text is selected, both this method and
     * {@link AccessibilityNodeInfo#getTextSelectionStart()} return the same value:
     * the current location of the cursor.
     * </p>
     *
     * @return The text selection end, the cursor location if there is no selection, or -1 if
    there is no text selection and no cursor.
     */
    public int getTextSelectionEnd() {
        return mTextSelectionEnd;
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
        enforceNotSealed();
        mTextSelectionStart = start;
        mTextSelectionEnd = end;
    }

    /**
     * Gets the input type of the source as defined by {@link InputType}.
     *
     * @return The input type.
     */
    public int getInputType() {
        return mInputType;
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
        enforceNotSealed();
        mInputType = inputType;
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
        if (mExtras == null) {
            mExtras = new android.os.Bundle();
        }
        return mExtras;
    }

    /**
     * Check if a node has an extras bundle
     *
     * @unknown 
     */
    public boolean hasExtras() {
        return mExtras != null;
    }

    /**
     * Get the {@link TouchDelegateInfo} for touch delegate behavior with the represented view.
     * It is possible for the same node to be pointed to by several regions. Use
     * {@link TouchDelegateInfo#getRegionAt(int)} to get touch delegate target {@link Region}, and
     * {@link TouchDelegateInfo#getTargetForRegion(Region)} for {@link AccessibilityNodeInfo} from
     * the given region.
     *
     * @return {@link TouchDelegateInfo} or {@code null} if there are no touch delegates.
     */
    @android.annotation.Nullable
    public android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo getTouchDelegateInfo() {
        if (mTouchDelegateInfo != null) {
            mTouchDelegateInfo.setConnectionId(mConnectionId);
            mTouchDelegateInfo.setWindowId(mWindowId);
        }
        return mTouchDelegateInfo;
    }

    /**
     * Set touch delegate info if the represented view has a {@link TouchDelegate}.
     * <p>
     *   <strong>Note:</strong> Cannot be called from an
     *   {@link android.accessibilityservice.AccessibilityService}.
     *   This class is made immutable before being delivered to an
     *   AccessibilityService.
     * </p>
     *
     * @param delegatedInfo
     * 		{@link TouchDelegateInfo} returned from
     * 		{@link TouchDelegate#getTouchDelegateInfo()}.
     * @throws IllegalStateException
     * 		If called from an AccessibilityService.
     */
    public void setTouchDelegateInfo(@android.annotation.NonNull
    android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo delegatedInfo) {
        enforceNotSealed();
        mTouchDelegateInfo = delegatedInfo;
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
        enforceNotSealed();
        if (value) {
            mBooleanProperties |= property;
        } else {
            mBooleanProperties &= ~property;
        }
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
     * Get the connection ID.
     *
     * @return The connection id
     * @unknown 
     */
    public int getConnectionId() {
        return mConnectionId;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Sets the id of the source node.
     *
     * @param sourceId
     * 		The id.
     * @param windowId
     * 		The window id.
     * @unknown 
     */
    public void setSourceNodeId(long sourceId, int windowId) {
        enforceNotSealed();
        mSourceNodeId = sourceId;
        mWindowId = windowId;
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
     * Sets if this instance is sealed.
     *
     * @param sealed
     * 		Whether is sealed.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setSealed(boolean sealed) {
        mSealed = sealed;
    }

    /**
     * Gets if this instance is sealed.
     *
     * @return Whether is sealed.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isSealed() {
        return mSealed;
    }

    /**
     * Enforces that this instance is sealed.
     *
     * @throws IllegalStateException
     * 		If this instance is not sealed.
     * @unknown 
     */
    protected void enforceSealed() {
        if (!isSealed()) {
            throw new java.lang.IllegalStateException("Cannot perform this " + "action on a not sealed instance.");
        }
    }

    private void enforceValidFocusDirection(int direction) {
        switch (direction) {
            case android.view.View.FOCUS_DOWN :
            case android.view.View.FOCUS_UP :
            case android.view.View.FOCUS_LEFT :
            case android.view.View.FOCUS_RIGHT :
            case android.view.View.FOCUS_FORWARD :
            case android.view.View.FOCUS_BACKWARD :
                return;
            default :
                throw new java.lang.IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private void enforceValidFocusType(int focusType) {
        switch (focusType) {
            case android.view.accessibility.AccessibilityNodeInfo.FOCUS_INPUT :
            case android.view.accessibility.AccessibilityNodeInfo.FOCUS_ACCESSIBILITY :
                return;
            default :
                throw new java.lang.IllegalArgumentException("Unknown focus type: " + focusType);
        }
    }

    /**
     * Enforces that this instance is not sealed.
     *
     * @throws IllegalStateException
     * 		If this instance is sealed.
     * @unknown 
     */
    protected void enforceNotSealed() {
        if (isSealed()) {
            throw new java.lang.IllegalStateException("Cannot perform this " + "action on a sealed instance.");
        }
    }

    /**
     * Returns a cached instance if such is available otherwise a new one
     * and sets the source.
     *
     * @param source
     * 		The source view.
     * @return An instance.
     * @see #setSource(View)
     */
    public static android.view.accessibility.AccessibilityNodeInfo obtain(android.view.View source) {
        android.view.accessibility.AccessibilityNodeInfo info = android.view.accessibility.AccessibilityNodeInfo.obtain();
        info.setSource(source);
        return info;
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
    public static android.view.accessibility.AccessibilityNodeInfo obtain(android.view.View root, int virtualDescendantId) {
        android.view.accessibility.AccessibilityNodeInfo info = android.view.accessibility.AccessibilityNodeInfo.obtain();
        info.setSource(root, virtualDescendantId);
        return info;
    }

    /**
     * Returns a cached instance if such is available otherwise a new one.
     *
     * @return An instance.
     */
    public static android.view.accessibility.AccessibilityNodeInfo obtain() {
        android.view.accessibility.AccessibilityNodeInfo info = android.view.accessibility.AccessibilityNodeInfo.sPool.acquire();
        if (android.view.accessibility.AccessibilityNodeInfo.sNumInstancesInUse != null) {
            android.view.accessibility.AccessibilityNodeInfo.sNumInstancesInUse.incrementAndGet();
        }
        return info != null ? info : new android.view.accessibility.AccessibilityNodeInfo();
    }

    /**
     * Returns a cached instance if such is available or a new one is
     * create. The returned instance is initialized from the given
     * <code>info</code>.
     *
     * @param info
     * 		The other info.
     * @return An instance.
     */
    public static android.view.accessibility.AccessibilityNodeInfo obtain(android.view.accessibility.AccessibilityNodeInfo info) {
        android.view.accessibility.AccessibilityNodeInfo infoClone = android.view.accessibility.AccessibilityNodeInfo.obtain();
        infoClone.init(info);
        return infoClone;
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
        clear();
        android.view.accessibility.AccessibilityNodeInfo.sPool.release(this);
        if (android.view.accessibility.AccessibilityNodeInfo.sNumInstancesInUse != null) {
            android.view.accessibility.AccessibilityNodeInfo.sNumInstancesInUse.decrementAndGet();
        }
    }

    /**
     * Specify a counter that will be incremented on obtain() and decremented on recycle()
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static void setNumInstancesInUseCounter(java.util.concurrent.atomic.AtomicInteger counter) {
        android.view.accessibility.AccessibilityNodeInfo.sNumInstancesInUse = counter;
    }

    /**
     * {@inheritDoc }
     * <p>
     *   <strong>Note:</strong> After the instance is written to a parcel it
     *      is recycled. You must not touch the object after calling this function.
     * </p>
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        writeToParcelNoRecycle(parcel, flags);
        // Since instances of this class are fetched via synchronous i.e. blocking
        // calls in IPCs we always recycle as soon as the instance is marshaled.
        recycle();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void writeToParcelNoRecycle(android.os.Parcel parcel, int flags) {
        // Write bit set of indices of fields with values differing from default
        long nonDefaultFields = 0;
        int fieldIndex = 0;// index of the current field

        if (isSealed() != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.isSealed())
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mSourceNodeId != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mSourceNodeId)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mWindowId != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mWindowId)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mParentNodeId != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mParentNodeId)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mLabelForId != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mLabelForId)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mLabeledById != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mLabeledById)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mTraversalBefore != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mTraversalBefore)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mTraversalAfter != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mTraversalAfter)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mConnectionId != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mConnectionId)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!android.util.LongArray.elementsEqual(mChildNodeIds, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mChildNodeIds)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mBoundsInParent, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mBoundsInParent)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mBoundsInScreen, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mBoundsInScreen)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mActions, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mActions))
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mMaxTextLength != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mMaxTextLength)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mMovementGranularities != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mMovementGranularities) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (mBooleanProperties != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mBooleanProperties)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!java.util.Objects.equals(mPackageName, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mPackageName)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mClassName, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mClassName))
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!java.util.Objects.equals(mText, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mText))
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!java.util.Objects.equals(mHintText, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mHintText)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mError, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mError))
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!java.util.Objects.equals(mContentDescription, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mContentDescription)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mPaneTitle, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mPaneTitle)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mTooltipText, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mTooltipText)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mViewIdResourceName, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mViewIdResourceName)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (mTextSelectionStart != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mTextSelectionStart) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (mTextSelectionEnd != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mTextSelectionEnd) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (mInputType != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mInputType)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mLiveRegion != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mLiveRegion)
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (mDrawingOrderInParent != android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mDrawingOrderInParent) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mExtraDataKeys, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mExtraDataKeys)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mExtras, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mExtras))
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!java.util.Objects.equals(mRangeInfo, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mRangeInfo))
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);

        fieldIndex++;
        if (!java.util.Objects.equals(mCollectionInfo, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mCollectionInfo)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mCollectionItemInfo, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mCollectionItemInfo)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        fieldIndex++;
        if (!java.util.Objects.equals(mTouchDelegateInfo, android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mTouchDelegateInfo)) {
            nonDefaultFields |= com.android.internal.util.BitUtils.bitAt(fieldIndex);
        }
        int totalFields = fieldIndex;
        parcel.writeLong(nonDefaultFields);
        fieldIndex = 0;
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(isSealed() ? 1 : 0);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeLong(mSourceNodeId);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mWindowId);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeLong(mParentNodeId);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeLong(mLabelForId);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeLong(mLabeledById);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeLong(mTraversalBefore);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeLong(mTraversalAfter);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mConnectionId);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            final android.util.LongArray childIds = mChildNodeIds;
            if (childIds == null) {
                parcel.writeInt(0);
            } else {
                final int childIdsSize = childIds.size();
                parcel.writeInt(childIdsSize);
                for (int i = 0; i < childIdsSize; i++) {
                    parcel.writeLong(childIds.get(i));
                }
            }
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            parcel.writeInt(mBoundsInParent.top);
            parcel.writeInt(mBoundsInParent.bottom);
            parcel.writeInt(mBoundsInParent.left);
            parcel.writeInt(mBoundsInParent.right);
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            parcel.writeInt(mBoundsInScreen.top);
            parcel.writeInt(mBoundsInScreen.bottom);
            parcel.writeInt(mBoundsInScreen.left);
            parcel.writeInt(mBoundsInScreen.right);
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            if ((mActions != null) && (!mActions.isEmpty())) {
                final int actionCount = mActions.size();
                int nonStandardActionCount = 0;
                long defaultStandardActions = 0;
                for (int i = 0; i < actionCount; i++) {
                    android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action = mActions.get(i);
                    if (android.view.accessibility.AccessibilityNodeInfo.isDefaultStandardAction(action)) {
                        defaultStandardActions |= action.mSerializationFlag;
                    } else {
                        nonStandardActionCount++;
                    }
                }
                parcel.writeLong(defaultStandardActions);
                parcel.writeInt(nonStandardActionCount);
                for (int i = 0; i < actionCount; i++) {
                    android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action = mActions.get(i);
                    if (!android.view.accessibility.AccessibilityNodeInfo.isDefaultStandardAction(action)) {
                        parcel.writeInt(action.getId());
                        parcel.writeCharSequence(action.getLabel());
                    }
                }
            } else {
                parcel.writeLong(0);
                parcel.writeInt(0);
            }
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mMaxTextLength);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mMovementGranularities);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mBooleanProperties);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mPackageName);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mClassName);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mText);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mHintText);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mError);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            parcel.writeCharSequence(mContentDescription);
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mPaneTitle);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeCharSequence(mTooltipText);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeString(mViewIdResourceName);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mTextSelectionStart);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mTextSelectionEnd);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mInputType);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mLiveRegion);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeInt(mDrawingOrderInParent);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeStringList(mExtraDataKeys);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            parcel.writeBundle(mExtras);

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            parcel.writeInt(mRangeInfo.getType());
            parcel.writeFloat(mRangeInfo.getMin());
            parcel.writeFloat(mRangeInfo.getMax());
            parcel.writeFloat(mRangeInfo.getCurrent());
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            parcel.writeInt(mCollectionInfo.getRowCount());
            parcel.writeInt(mCollectionInfo.getColumnCount());
            parcel.writeInt(mCollectionInfo.isHierarchical() ? 1 : 0);
            parcel.writeInt(mCollectionInfo.getSelectionMode());
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            parcel.writeInt(mCollectionItemInfo.getRowIndex());
            parcel.writeInt(mCollectionItemInfo.getRowSpan());
            parcel.writeInt(mCollectionItemInfo.getColumnIndex());
            parcel.writeInt(mCollectionItemInfo.getColumnSpan());
            parcel.writeInt(mCollectionItemInfo.isHeading() ? 1 : 0);
            parcel.writeInt(mCollectionItemInfo.isSelected() ? 1 : 0);
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            mTouchDelegateInfo.writeToParcel(parcel, flags);
        }
        if (android.view.accessibility.AccessibilityNodeInfo.DEBUG) {
            fieldIndex--;
            if (totalFields != fieldIndex) {
                throw new java.lang.IllegalStateException((("Number of fields mismatch: " + totalFields) + " vs ") + fieldIndex);
            }
        }
    }

    /**
     * Initializes this instance from another one.
     *
     * @param other
     * 		The other instance.
     */
    private void init(android.view.accessibility.AccessibilityNodeInfo other) {
        mSealed = other.mSealed;
        mSourceNodeId = other.mSourceNodeId;
        mParentNodeId = other.mParentNodeId;
        mLabelForId = other.mLabelForId;
        mLabeledById = other.mLabeledById;
        mTraversalBefore = other.mTraversalBefore;
        mTraversalAfter = other.mTraversalAfter;
        mWindowId = other.mWindowId;
        mConnectionId = other.mConnectionId;
        mBoundsInParent.set(other.mBoundsInParent);
        mBoundsInScreen.set(other.mBoundsInScreen);
        mPackageName = other.mPackageName;
        mClassName = other.mClassName;
        mText = other.mText;
        mOriginalText = other.mOriginalText;
        mHintText = other.mHintText;
        mError = other.mError;
        mContentDescription = other.mContentDescription;
        mPaneTitle = other.mPaneTitle;
        mTooltipText = other.mTooltipText;
        mViewIdResourceName = other.mViewIdResourceName;
        if (mActions != null)
            mActions.clear();

        final java.util.ArrayList<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> otherActions = other.mActions;
        if ((otherActions != null) && (otherActions.size() > 0)) {
            if (mActions == null) {
                mActions = new java.util.ArrayList(otherActions);
            } else {
                mActions.addAll(other.mActions);
            }
        }
        mBooleanProperties = other.mBooleanProperties;
        mMaxTextLength = other.mMaxTextLength;
        mMovementGranularities = other.mMovementGranularities;
        if (mChildNodeIds != null)
            mChildNodeIds.clear();

        final android.util.LongArray otherChildNodeIds = other.mChildNodeIds;
        if ((otherChildNodeIds != null) && (otherChildNodeIds.size() > 0)) {
            if (mChildNodeIds == null) {
                mChildNodeIds = otherChildNodeIds.clone();
            } else {
                mChildNodeIds.addAll(otherChildNodeIds);
            }
        }
        mTextSelectionStart = other.mTextSelectionStart;
        mTextSelectionEnd = other.mTextSelectionEnd;
        mInputType = other.mInputType;
        mLiveRegion = other.mLiveRegion;
        mDrawingOrderInParent = other.mDrawingOrderInParent;
        mExtraDataKeys = other.mExtraDataKeys;
        mExtras = (other.mExtras != null) ? new android.os.Bundle(other.mExtras) : null;
        if (mRangeInfo != null)
            mRangeInfo.recycle();

        mRangeInfo = (other.mRangeInfo != null) ? android.view.accessibility.AccessibilityNodeInfo.RangeInfo.obtain(other.mRangeInfo) : null;
        if (mCollectionInfo != null)
            mCollectionInfo.recycle();

        mCollectionInfo = (other.mCollectionInfo != null) ? android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(other.mCollectionInfo) : null;
        if (mCollectionItemInfo != null)
            mCollectionItemInfo.recycle();

        mCollectionItemInfo = (other.mCollectionItemInfo != null) ? android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(other.mCollectionItemInfo) : null;
        final android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo otherInfo = other.mTouchDelegateInfo;
        mTouchDelegateInfo = (otherInfo != null) ? new android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo(otherInfo.mTargetMap, true) : null;
    }

    /**
     * Creates a new instance from a {@link Parcel}.
     *
     * @param parcel
     * 		A parcel containing the state of a {@link AccessibilityNodeInfo}.
     */
    private void initFromParcel(android.os.Parcel parcel) {
        // Bit mask of non-default-valued field indices
        long nonDefaultFields = parcel.readLong();
        int fieldIndex = 0;
        final boolean sealed = (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) ? parcel.readInt() == 1 : android.view.accessibility.AccessibilityNodeInfo.DEFAULT.mSealed;
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mSourceNodeId = parcel.readLong();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mWindowId = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mParentNodeId = parcel.readLong();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mLabelForId = parcel.readLong();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mLabeledById = parcel.readLong();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mTraversalBefore = parcel.readLong();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mTraversalAfter = parcel.readLong();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mConnectionId = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            final int childrenSize = parcel.readInt();
            if (childrenSize <= 0) {
                mChildNodeIds = null;
            } else {
                mChildNodeIds = new android.util.LongArray(childrenSize);
                for (int i = 0; i < childrenSize; i++) {
                    final long childId = parcel.readLong();
                    mChildNodeIds.add(childId);
                }
            }
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            mBoundsInParent.top = parcel.readInt();
            mBoundsInParent.bottom = parcel.readInt();
            mBoundsInParent.left = parcel.readInt();
            mBoundsInParent.right = parcel.readInt();
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            mBoundsInScreen.top = parcel.readInt();
            mBoundsInScreen.bottom = parcel.readInt();
            mBoundsInScreen.left = parcel.readInt();
            mBoundsInScreen.right = parcel.readInt();
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            final long standardActions = parcel.readLong();
            addStandardActions(standardActions);
            final int nonStandardActionCount = parcel.readInt();
            for (int i = 0; i < nonStandardActionCount; i++) {
                final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(parcel.readInt(), parcel.readCharSequence());
                addActionUnchecked(action);
            }
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mMaxTextLength = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mMovementGranularities = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mBooleanProperties = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mPackageName = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mClassName = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mText = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mHintText = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mError = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            mContentDescription = parcel.readCharSequence();
        }
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mPaneTitle = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mTooltipText = parcel.readCharSequence();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mViewIdResourceName = parcel.readString();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mTextSelectionStart = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mTextSelectionEnd = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mInputType = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mLiveRegion = parcel.readInt();

        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++))
            mDrawingOrderInParent = parcel.readInt();

        mExtraDataKeys = (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) ? parcel.createStringArrayList() : null;
        mExtras = (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) ? parcel.readBundle() : null;
        if (mRangeInfo != null)
            mRangeInfo.recycle();

        mRangeInfo = (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) ? android.view.accessibility.AccessibilityNodeInfo.RangeInfo.obtain(parcel.readInt(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat()) : null;
        if (mCollectionInfo != null)
            mCollectionInfo.recycle();

        mCollectionInfo = (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) ? android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(parcel.readInt(), parcel.readInt(), parcel.readInt() == 1, parcel.readInt()) : null;
        if (mCollectionItemInfo != null)
            mCollectionItemInfo.recycle();

        mCollectionItemInfo = (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) ? android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() == 1, parcel.readInt() == 1) : null;
        if (com.android.internal.util.BitUtils.isBitSet(nonDefaultFields, fieldIndex++)) {
            mTouchDelegateInfo = this.CREATOR.createFromParcel(parcel);
        }
        mSealed = sealed;
    }

    /**
     * Clears the state of this instance.
     */
    private void clear() {
        init(android.view.accessibility.AccessibilityNodeInfo.DEFAULT);
    }

    private static boolean isDefaultStandardAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
        return (action.mSerializationFlag != (-1L)) && android.text.TextUtils.isEmpty(action.getLabel());
    }

    private static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction getActionSingleton(int actionId) {
        final int actions = android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.sStandardActions.size();
        for (int i = 0; i < actions; i++) {
            android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction currentAction = android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.sStandardActions.valueAt(i);
            if (actionId == currentAction.getId()) {
                return currentAction;
            }
        }
        return null;
    }

    private static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction getActionSingletonBySerializationFlag(long flag) {
        final int actions = android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.sStandardActions.size();
        for (int i = 0; i < actions; i++) {
            android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction currentAction = android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.sStandardActions.valueAt(i);
            if (flag == currentAction.mSerializationFlag) {
                return currentAction;
            }
        }
        return null;
    }

    private void addStandardActions(long serializationIdMask) {
        long remainingIds = serializationIdMask;
        while (remainingIds > 0) {
            final long id = 1L << java.lang.Long.numberOfTrailingZeros(remainingIds);
            remainingIds &= ~id;
            android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action = android.view.accessibility.AccessibilityNodeInfo.getActionSingletonBySerializationFlag(id);
            addAction(action);
        } 
    }

    /**
     * Gets the human readable action symbolic name.
     *
     * @param action
     * 		The action.
     * @return The symbolic name.
     */
    private static java.lang.String getActionSymbolicName(int action) {
        switch (action) {
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_FOCUS :
                return "ACTION_FOCUS";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_FOCUS :
                return "ACTION_CLEAR_FOCUS";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SELECT :
                return "ACTION_SELECT";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_SELECTION :
                return "ACTION_CLEAR_SELECTION";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK :
                return "ACTION_CLICK";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_LONG_CLICK :
                return "ACTION_LONG_CLICK";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS :
                return "ACTION_ACCESSIBILITY_FOCUS";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS :
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY :
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY :
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT :
                return "ACTION_NEXT_HTML_ELEMENT";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT :
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD :
                return "ACTION_SCROLL_FORWARD";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD :
                return "ACTION_SCROLL_BACKWARD";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CUT :
                return "ACTION_CUT";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_COPY :
                return "ACTION_COPY";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_PASTE :
                return "ACTION_PASTE";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_SELECTION :
                return "ACTION_SET_SELECTION";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_EXPAND :
                return "ACTION_EXPAND";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_COLLAPSE :
                return "ACTION_COLLAPSE";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_DISMISS :
                return "ACTION_DISMISS";
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_TEXT :
                return "ACTION_SET_TEXT";
            case R.id.accessibilityActionShowOnScreen :
                return "ACTION_SHOW_ON_SCREEN";
            case R.id.accessibilityActionScrollToPosition :
                return "ACTION_SCROLL_TO_POSITION";
            case R.id.accessibilityActionScrollUp :
                return "ACTION_SCROLL_UP";
            case R.id.accessibilityActionScrollLeft :
                return "ACTION_SCROLL_LEFT";
            case R.id.accessibilityActionScrollDown :
                return "ACTION_SCROLL_DOWN";
            case R.id.accessibilityActionScrollRight :
                return "ACTION_SCROLL_RIGHT";
            case R.id.accessibilityActionPageDown :
                return "ACTION_PAGE_DOWN";
            case R.id.accessibilityActionPageUp :
                return "ACTION_PAGE_UP";
            case R.id.accessibilityActionPageLeft :
                return "ACTION_PAGE_LEFT";
            case R.id.accessibilityActionPageRight :
                return "ACTION_PAGE_RIGHT";
            case R.id.accessibilityActionSetProgress :
                return "ACTION_SET_PROGRESS";
            case R.id.accessibilityActionContextClick :
                return "ACTION_CONTEXT_CLICK";
            case R.id.accessibilityActionShowTooltip :
                return "ACTION_SHOW_TOOLTIP";
            case R.id.accessibilityActionHideTooltip :
                return "ACTION_HIDE_TOOLTIP";
            default :
                return "ACTION_UNKNOWN";
        }
    }

    /**
     * Gets the human readable movement granularity symbolic name.
     *
     * @param granularity
     * 		The granularity.
     * @return The symbolic name.
     */
    private static java.lang.String getMovementGranularitySymbolicName(int granularity) {
        switch (granularity) {
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER :
                return "MOVEMENT_GRANULARITY_CHARACTER";
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD :
                return "MOVEMENT_GRANULARITY_WORD";
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE :
                return "MOVEMENT_GRANULARITY_LINE";
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH :
                return "MOVEMENT_GRANULARITY_PARAGRAPH";
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE :
                return "MOVEMENT_GRANULARITY_PAGE";
            default :
                throw new java.lang.IllegalArgumentException("Unknown movement granularity: " + granularity);
        }
    }

    private static boolean canPerformRequestOverConnection(int connectionId, int windowId, long accessibilityNodeId) {
        return ((windowId != android.view.accessibility.AccessibilityWindowInfo.UNDEFINED_WINDOW_ID) && (android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId) != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID)) && (connectionId != android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_CONNECTION_ID);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        android.view.accessibility.AccessibilityNodeInfo other = ((android.view.accessibility.AccessibilityNodeInfo) (object));
        if (mSourceNodeId != other.mSourceNodeId) {
            return false;
        }
        if (mWindowId != other.mWindowId) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(mSourceNodeId);
        result = (prime * result) + android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(mSourceNodeId);
        result = (prime * result) + mWindowId;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append(super.toString());
        if (android.view.accessibility.AccessibilityNodeInfo.DEBUG) {
            builder.append("; sourceNodeId: " + mSourceNodeId);
            builder.append("; windowId: " + mWindowId);
            builder.append("; accessibilityViewId: ").append(android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(mSourceNodeId));
            builder.append("; virtualDescendantId: ").append(android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(mSourceNodeId));
            builder.append("; mParentNodeId: " + mParentNodeId);
            builder.append("; traversalBefore: ").append(mTraversalBefore);
            builder.append("; traversalAfter: ").append(mTraversalAfter);
            int granularities = mMovementGranularities;
            builder.append("; MovementGranularities: [");
            while (granularities != 0) {
                final int granularity = 1 << java.lang.Integer.numberOfTrailingZeros(granularities);
                granularities &= ~granularity;
                builder.append(android.view.accessibility.AccessibilityNodeInfo.getMovementGranularitySymbolicName(granularity));
                if (granularities != 0) {
                    builder.append(", ");
                }
            } 
            builder.append("]");
            builder.append("; childAccessibilityIds: [");
            final android.util.LongArray childIds = mChildNodeIds;
            if (childIds != null) {
                for (int i = 0, count = childIds.size(); i < count; i++) {
                    builder.append(childIds.get(i));
                    if (i < (count - 1)) {
                        builder.append(", ");
                    }
                }
            }
            builder.append("]");
        }
        builder.append("; boundsInParent: ").append(mBoundsInParent);
        builder.append("; boundsInScreen: ").append(mBoundsInScreen);
        builder.append("; packageName: ").append(mPackageName);
        builder.append("; className: ").append(mClassName);
        builder.append("; text: ").append(mText);
        builder.append("; error: ").append(mError);
        builder.append("; maxTextLength: ").append(mMaxTextLength);
        builder.append("; contentDescription: ").append(mContentDescription);
        builder.append("; tooltipText: ").append(mTooltipText);
        builder.append("; viewIdResName: ").append(mViewIdResourceName);
        builder.append("; checkable: ").append(isCheckable());
        builder.append("; checked: ").append(isChecked());
        builder.append("; focusable: ").append(isFocusable());
        builder.append("; focused: ").append(isFocused());
        builder.append("; selected: ").append(isSelected());
        builder.append("; clickable: ").append(isClickable());
        builder.append("; longClickable: ").append(isLongClickable());
        builder.append("; contextClickable: ").append(isContextClickable());
        builder.append("; enabled: ").append(isEnabled());
        builder.append("; password: ").append(isPassword());
        builder.append("; scrollable: ").append(isScrollable());
        builder.append("; importantForAccessibility: ").append(isImportantForAccessibility());
        builder.append("; visible: ").append(isVisibleToUser());
        builder.append("; actions: ").append(mActions);
        return builder.toString();
    }

    private static android.view.accessibility.AccessibilityNodeInfo getNodeForAccessibilityId(int connectionId, int windowId, long accessibilityId) {
        if (!android.view.accessibility.AccessibilityNodeInfo.canPerformRequestOverConnection(connectionId, windowId, accessibilityId)) {
            return null;
        }
        android.view.accessibility.AccessibilityInteractionClient client = android.view.accessibility.AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfoByAccessibilityId(connectionId, windowId, accessibilityId, false, (android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_PREDECESSORS | android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS) | android.view.accessibility.AccessibilityNodeInfo.FLAG_PREFETCH_SIBLINGS, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String idToString(long accessibilityId) {
        int accessibilityViewId = android.view.accessibility.AccessibilityNodeInfo.getAccessibilityViewId(accessibilityId);
        int virtualDescendantId = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(accessibilityId);
        return virtualDescendantId == android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID ? android.view.accessibility.AccessibilityNodeInfo.idItemToString(accessibilityViewId) : (android.view.accessibility.AccessibilityNodeInfo.idItemToString(accessibilityViewId) + ":") + android.view.accessibility.AccessibilityNodeInfo.idItemToString(virtualDescendantId);
    }

    private static java.lang.String idItemToString(int item) {
        switch (item) {
            case android.view.accessibility.AccessibilityNodeInfo.ROOT_ITEM_ID :
                return "ROOT";
            case android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_ITEM_ID :
                return "UNDEFINED";
            case android.view.accessibility.AccessibilityNodeProvider.HOST_VIEW_ID :
                return "HOST";
            default :
                return "" + item;
        }
    }

    /**
     * A class defining an action that can be performed on an {@link AccessibilityNodeInfo}.
     * Each action has a unique id that is mandatory and optional data.
     * <p>
     * There are three categories of actions:
     * <ul>
     * <li><strong>Standard actions</strong> - These are actions that are reported and
     * handled by the standard UI widgets in the platform. For each standard action
     * there is a static constant defined in this class, e.g. {@link #ACTION_FOCUS}.
     * These actions will have {@code null} labels.
     * </li>
     * <li><strong>Custom actions action</strong> - These are actions that are reported
     * and handled by custom widgets. i.e. ones that are not part of the UI toolkit. For
     * example, an application may define a custom action for clearing the user history.
     * </li>
     * <li><strong>Overriden standard actions</strong> - These are actions that override
     * standard actions to customize them. For example, an app may add a label to the
     * standard {@link #ACTION_CLICK} action to indicate to the user that this action clears
     * browsing history.
     * </ul>
     * </p>
     * <p>
     * Actions are typically added to an {@link AccessibilityNodeInfo} by using
     * {@link AccessibilityNodeInfo#addAction(AccessibilityAction)} within
     * {@link View#onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo)} and are performed
     * within {@link View#performAccessibilityAction(int, Bundle)}.
     * </p>
     * <p class="note">
     * <strong>Note:</strong> Views which support these actions should invoke
     * {@link View#setImportantForAccessibility(int)} with
     * {@link View#IMPORTANT_FOR_ACCESSIBILITY_YES} to ensure an {@link AccessibilityService}
     * can discover the set of supported actions.
     * </p>
     */
    public static final class AccessibilityAction {
        /**
         *
         *
         * @unknown 
         */
        public static final android.util.ArraySet<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> sStandardActions = new android.util.ArraySet();

        /**
         * Action that gives input focus to the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_FOCUS = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_FOCUS);

        /**
         * Action that clears input focus of the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CLEAR_FOCUS = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_FOCUS);

        /**
         * Action that selects the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SELECT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SELECT);

        /**
         * Action that deselects the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CLEAR_SELECTION = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_SELECTION);

        /**
         * Action that clicks on the node info.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CLICK = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK);

        /**
         * Action that long clicks on the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_LONG_CLICK = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_LONG_CLICK);

        /**
         * Action that gives accessibility focus to the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_ACCESSIBILITY_FOCUS = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);

        /**
         * Action that clears accessibility focus of the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CLEAR_ACCESSIBILITY_FOCUS = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS);

        /**
         * Action that requests to go to the next entity in this node's text
         * at a given movement granularity. For example, move to the next character,
         * word, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT},
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN}<br>
         * <strong>Example:</strong> Move to the previous character and do not extend selection.
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
         *           AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER);
         *   arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
         *           false);
         *   info.performAction(AccessibilityAction.ACTION_NEXT_AT_MOVEMENT_GRANULARITY.getId(),
         *           arguments);
         * </code></pre></p>
         * </p>
         *
         * @see AccessibilityNodeInfo#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
        AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         * @see AccessibilityNodeInfo#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
        AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         * @see AccessibilityNodeInfo#setMovementGranularities(int)
        AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         * @see AccessibilityNodeInfo#getMovementGranularities()
        AccessibilityNodeInfo.getMovementGranularities()
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_CHARACTER
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_WORD
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_LINE
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_PARAGRAPH
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_PAGE
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);

        /**
         * Action that requests to go to the previous entity in this node's text
         * at a given movement granularity. For example, move to the next character,
         * word, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT},
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN}<br>
         * <strong>Example:</strong> Move to the next character and do not extend selection.
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
         *           AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER);
         *   arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
         *           false);
         *   info.performAction(AccessibilityAction.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY.getId(),
         *           arguments);
         * </code></pre></p>
         * </p>
         *
         * @see AccessibilityNodeInfo#ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
        AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT
         * @see AccessibilityNodeInfo#ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
        AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN
         * @see AccessibilityNodeInfo#setMovementGranularities(int)
        AccessibilityNodeInfo.setMovementGranularities(int)
         * @see AccessibilityNodeInfo#getMovementGranularities()
        AccessibilityNodeInfo.getMovementGranularities()
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_CHARACTER
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_WORD
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_LINE
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_PARAGRAPH
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH
         * @see AccessibilityNodeInfo#MOVEMENT_GRANULARITY_PAGE
        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);

        /**
         * Action to move to the next HTML element of a given type. For example, move
         * to the BUTTON, INPUT, TABLE, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_HTML_ELEMENT_STRING
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING, "BUTTON");
         *   info.performAction(AccessibilityAction.ACTION_NEXT_HTML_ELEMENT.getId(), arguments);
         * </code></pre></p>
         * </p>
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_NEXT_HTML_ELEMENT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT);

        /**
         * Action to move to the previous HTML element of a given type. For example, move
         * to the BUTTON, INPUT, TABLE, etc.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_HTML_ELEMENT_STRING
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING, "BUTTON");
         *   info.performAction(AccessibilityAction.ACTION_PREVIOUS_HTML_ELEMENT.getId(), arguments);
         * </code></pre></p>
         * </p>
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PREVIOUS_HTML_ELEMENT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT);

        /**
         * Action to scroll the node content forward.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_FORWARD = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

        /**
         * Action to scroll the node content backward.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_BACKWARD = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);

        /**
         * Action to copy the current selection to the clipboard.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_COPY = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_COPY);

        /**
         * Action to paste the current clipboard content.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PASTE = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_PASTE);

        /**
         * Action to cut the current selection and place it to the clipboard.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CUT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CUT);

        /**
         * Action to set the selection. Performing this action with no arguments
         * clears the selection.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_SELECTION_START_INT
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT},
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_SELECTION_END_INT
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 1);
         *   arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, 2);
         *   info.performAction(AccessibilityAction.ACTION_SET_SELECTION.getId(), arguments);
         * </code></pre></p>
         * </p>
         *
         * @see AccessibilityNodeInfo#ACTION_ARGUMENT_SELECTION_START_INT
        AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT
         * @see AccessibilityNodeInfo#ACTION_ARGUMENT_SELECTION_END_INT
        AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SET_SELECTION = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_SELECTION);

        /**
         * Action to expand an expandable node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_EXPAND = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_EXPAND);

        /**
         * Action to collapse an expandable node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_COLLAPSE = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_COLLAPSE);

        /**
         * Action to dismiss a dismissable node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_DISMISS = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_DISMISS);

        /**
         * Action that sets the text of the node. Performing the action without argument,
         * using <code> null</code> or empty {@link CharSequence} will clear the text. This
         * action will also put the cursor at the end of text.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE
         *  AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE}<br>
         * <strong>Example:</strong>
         * <code><pre><p>
         *   Bundle arguments = new Bundle();
         *   arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
         *       "android");
         *   info.performAction(AccessibilityAction.ACTION_SET_TEXT.getId(), arguments);
         * </code></pre></p>
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SET_TEXT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_TEXT);

        /**
         * Action that requests the node make its bounding rectangle visible
         * on the screen, scrolling if necessary just enough.
         *
         * @see View#requestRectangleOnScreen(Rect)
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SHOW_ON_SCREEN = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionShowOnScreen);

        /**
         * Action that scrolls the node to make the specified collection
         * position visible on screen.
         * <p>
         * <strong>Arguments:</strong>
         * <ul>
         *     <li>{@link AccessibilityNodeInfo#ACTION_ARGUMENT_ROW_INT}</li>
         *     <li>{@link AccessibilityNodeInfo#ACTION_ARGUMENT_COLUMN_INT}</li>
         * <ul>
         *
         * @see AccessibilityNodeInfo#getCollectionInfo()
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_TO_POSITION = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionScrollToPosition);

        /**
         * Action to scroll the node content up.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_UP = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionScrollUp);

        /**
         * Action to scroll the node content left.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_LEFT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionScrollLeft);

        /**
         * Action to scroll the node content down.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_DOWN = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionScrollDown);

        /**
         * Action to scroll the node content right.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_RIGHT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionScrollRight);

        /**
         * Action to move to the page above.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_UP = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionPageUp);

        /**
         * Action to move to the page below.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_DOWN = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionPageDown);

        /**
         * Action to move to the page left.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_LEFT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionPageLeft);

        /**
         * Action to move to the page right.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_RIGHT = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionPageRight);

        /**
         * Action that context clicks the node.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CONTEXT_CLICK = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionContextClick);

        /**
         * Action that sets progress between {@link RangeInfo#getMin() RangeInfo.getMin()} and
         * {@link RangeInfo#getMax() RangeInfo.getMax()}. It should use the same value type as
         * {@link RangeInfo#getType() RangeInfo.getType()}
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_PROGRESS_VALUE}
         *
         * @see RangeInfo
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SET_PROGRESS = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionSetProgress);

        /**
         * Action to move a window to a new location.
         * <p>
         * <strong>Arguments:</strong>
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_MOVE_WINDOW_X}
         * {@link AccessibilityNodeInfo#ACTION_ARGUMENT_MOVE_WINDOW_Y}
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_MOVE_WINDOW = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionMoveWindow);

        /**
         * Action to show a tooltip. A node should expose this action only for views with tooltip
         * text that but are not currently showing a tooltip.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SHOW_TOOLTIP = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionShowTooltip);

        /**
         * Action to hide a tooltip. A node should expose this action only for views that are
         * currently showing a tooltip.
         */
        public static final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_HIDE_TOOLTIP = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(R.id.accessibilityActionHideTooltip);

        private final int mActionId;

        private final java.lang.CharSequence mLabel;

        /**
         *
         *
         * @unknown 
         */
        public long mSerializationFlag = -1L;

        /**
         * Creates a new AccessibilityAction. For adding a standard action without a specific label,
         * use the static constants.
         *
         * You can also override the description for one the standard actions. Below is an example
         * how to override the standard click action by adding a custom label:
         * <pre>
         *   AccessibilityAction action = new AccessibilityAction(
         *           AccessibilityAction.ACTION_CLICK.getId(), getLocalizedLabel());
         *   node.addAction(action);
         * </pre>
         *
         * @param actionId
         * 		The id for this action. This should either be one of the
         * 		standard actions or a specific action for your app. In that case it is
         * 		required to use a resource identifier.
         * @param label
         * 		The label for the new AccessibilityAction.
         */
        public AccessibilityAction(int actionId, @android.annotation.Nullable
        java.lang.CharSequence label) {
            if (((actionId & android.view.accessibility.AccessibilityNodeInfo.ACTION_TYPE_MASK) == 0) && (java.lang.Integer.bitCount(actionId) != 1)) {
                throw new java.lang.IllegalArgumentException("Invalid standard action id");
            }
            mActionId = actionId;
            mLabel = label;
        }

        /**
         * Constructor for a {@link #sStandardActions standard} action
         */
        private AccessibilityAction(int standardActionId) {
            this(standardActionId, null);
            mSerializationFlag = com.android.internal.util.BitUtils.bitAt(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.sStandardActions.size());
            android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.sStandardActions.add(this);
        }

        /**
         * Gets the id for this action.
         *
         * @return The action id.
         */
        public int getId() {
            return mActionId;
        }

        /**
         * Gets the label for this action. Its purpose is to describe the
         * action to user.
         *
         * @return The label.
         */
        public java.lang.CharSequence getLabel() {
            return mLabel;
        }

        @java.lang.Override
        public int hashCode() {
            return mActionId;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            return mActionId == ((android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction) (other)).mActionId;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (("AccessibilityAction: " + android.view.accessibility.AccessibilityNodeInfo.getActionSymbolicName(mActionId)) + " - ") + mLabel;
        }
    }

    /**
     * Class with information if a node is a range. Use
     * {@link RangeInfo#obtain(int, float, float, float)} to get an instance. Recycling is
     * handled by the {@link AccessibilityNodeInfo} to which this object is attached.
     */
    public static final class RangeInfo {
        private static final int MAX_POOL_SIZE = 10;

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

        private static final android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityNodeInfo.RangeInfo> sPool = new android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityNodeInfo.RangeInfo>(android.view.accessibility.AccessibilityNodeInfo.RangeInfo.MAX_POOL_SIZE);

        private int mType;

        private float mMin;

        private float mMax;

        private float mCurrent;

        /**
         * Obtains a pooled instance that is a clone of another one.
         *
         * @param other
         * 		The instance to clone.
         * @unknown 
         */
        public static android.view.accessibility.AccessibilityNodeInfo.RangeInfo obtain(android.view.accessibility.AccessibilityNodeInfo.RangeInfo other) {
            return android.view.accessibility.AccessibilityNodeInfo.RangeInfo.obtain(other.mType, other.mMin, other.mMax, other.mCurrent);
        }

        /**
         * Obtains a pooled instance.
         *
         * @param type
         * 		The type of the range.
         * @param min
         * 		The minimum value. Use {@code Float.NEGATIVE_INFINITY} if the range has no
         * 		minimum.
         * @param max
         * 		The maximum value. Use {@code Float.POSITIVE_INFINITY} if the range has no
         * 		maximum.
         * @param current
         * 		The current value.
         */
        public static android.view.accessibility.AccessibilityNodeInfo.RangeInfo obtain(int type, float min, float max, float current) {
            android.view.accessibility.AccessibilityNodeInfo.RangeInfo info = android.view.accessibility.AccessibilityNodeInfo.RangeInfo.sPool.acquire();
            if (info == null) {
                return new android.view.accessibility.AccessibilityNodeInfo.RangeInfo(type, min, max, current);
            }
            info.mType = type;
            info.mMin = min;
            info.mMax = max;
            info.mCurrent = current;
            return info;
        }

        /**
         * Creates a new range.
         *
         * @param type
         * 		The type of the range.
         * @param min
         * 		The minimum value. Use {@code Float.NEGATIVE_INFINITY} if the range has no
         * 		minimum.
         * @param max
         * 		The maximum value. Use {@code Float.POSITIVE_INFINITY} if the range has no
         * 		maximum.
         * @param current
         * 		The current value.
         */
        private RangeInfo(int type, float min, float max, float current) {
            mType = type;
            mMin = min;
            mMax = max;
            mCurrent = current;
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
            return mType;
        }

        /**
         * Gets the minimum value.
         *
         * @return The minimum value, or {@code Float.NEGATIVE_INFINITY} if no minimum exists.
         */
        public float getMin() {
            return mMin;
        }

        /**
         * Gets the maximum value.
         *
         * @return The maximum value, or {@code Float.POSITIVE_INFINITY} if no maximum exists.
         */
        public float getMax() {
            return mMax;
        }

        /**
         * Gets the current value.
         *
         * @return The current value.
         */
        public float getCurrent() {
            return mCurrent;
        }

        /**
         * Recycles this instance.
         */
        void recycle() {
            clear();
            android.view.accessibility.AccessibilityNodeInfo.RangeInfo.sPool.release(this);
        }

        private void clear() {
            mType = 0;
            mMin = 0;
            mMax = 0;
            mCurrent = 0;
        }
    }

    /**
     * Class with information if a node is a collection. Use
     * {@link CollectionInfo#obtain(int, int, boolean)} to get an instance. Recycling is
     * handled by the {@link AccessibilityNodeInfo} to which this object is attached.
     * <p>
     * A collection of items has rows and columns and may be hierarchical.
     * For example, a horizontal list is a collection with one column, as
     * many rows as the list items, and is not hierarchical; A table is a
     * collection with several rows, several columns, and is not hierarchical;
     * A vertical tree is a hierarchical collection with one column and
     * as many rows as the first level children.
     * </p>
     */
    public static final class CollectionInfo {
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

        private static final int MAX_POOL_SIZE = 20;

        private static final android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityNodeInfo.CollectionInfo> sPool = new android.util.Pools.SynchronizedPool(android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.MAX_POOL_SIZE);

        private int mRowCount;

        private int mColumnCount;

        private boolean mHierarchical;

        private int mSelectionMode;

        /**
         * Obtains a pooled instance that is a clone of another one.
         *
         * @param other
         * 		The instance to clone.
         * @unknown 
         */
        public static android.view.accessibility.AccessibilityNodeInfo.CollectionInfo obtain(android.view.accessibility.AccessibilityNodeInfo.CollectionInfo other) {
            return android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(other.mRowCount, other.mColumnCount, other.mHierarchical, other.mSelectionMode);
        }

        /**
         * Obtains a pooled instance.
         *
         * @param rowCount
         * 		The number of rows.
         * @param columnCount
         * 		The number of columns.
         * @param hierarchical
         * 		Whether the collection is hierarchical.
         */
        public static android.view.accessibility.AccessibilityNodeInfo.CollectionInfo obtain(int rowCount, int columnCount, boolean hierarchical) {
            return android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(rowCount, columnCount, hierarchical, android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.SELECTION_MODE_NONE);
        }

        /**
         * Obtains a pooled instance.
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
         */
        public static android.view.accessibility.AccessibilityNodeInfo.CollectionInfo obtain(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            final android.view.accessibility.AccessibilityNodeInfo.CollectionInfo info = android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.sPool.acquire();
            if (info == null) {
                return new android.view.accessibility.AccessibilityNodeInfo.CollectionInfo(rowCount, columnCount, hierarchical, selectionMode);
            }
            info.mRowCount = rowCount;
            info.mColumnCount = columnCount;
            info.mHierarchical = hierarchical;
            info.mSelectionMode = selectionMode;
            return info;
        }

        /**
         * Creates a new instance.
         *
         * @param rowCount
         * 		The number of rows.
         * @param columnCount
         * 		The number of columns.
         * @param hierarchical
         * 		Whether the collection is hierarchical.
         * @param selectionMode
         * 		The collection's selection mode.
         */
        private CollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            mRowCount = rowCount;
            mColumnCount = columnCount;
            mHierarchical = hierarchical;
            mSelectionMode = selectionMode;
        }

        /**
         * Gets the number of rows.
         *
         * @return The row count.
         */
        public int getRowCount() {
            return mRowCount;
        }

        /**
         * Gets the number of columns.
         *
         * @return The column count.
         */
        public int getColumnCount() {
            return mColumnCount;
        }

        /**
         * Gets if the collection is a hierarchically ordered.
         *
         * @return Whether the collection is hierarchical.
         */
        public boolean isHierarchical() {
            return mHierarchical;
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
            return mSelectionMode;
        }

        /**
         * Recycles this instance.
         */
        void recycle() {
            clear();
            android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.sPool.release(this);
        }

        private void clear() {
            mRowCount = 0;
            mColumnCount = 0;
            mHierarchical = false;
            mSelectionMode = android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.SELECTION_MODE_NONE;
        }
    }

    /**
     * Class with information if a node is a collection item. Use
     * {@link CollectionItemInfo#obtain(int, int, int, int, boolean)}
     * to get an instance. Recycling is handled by the {@link AccessibilityNodeInfo} to which this
     * object is attached.
     * <p>
     * A collection item is contained in a collection, it starts at
     * a given row and column in the collection, and spans one or
     * more rows and columns. For example, a header of two related
     * table columns starts at the first row and the first column,
     * spans one row and two columns.
     * </p>
     */
    public static final class CollectionItemInfo {
        private static final int MAX_POOL_SIZE = 20;

        private static final android.util.Pools.SynchronizedPool<android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo> sPool = new android.util.Pools.SynchronizedPool(android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.MAX_POOL_SIZE);

        /**
         * Obtains a pooled instance that is a clone of another one.
         *
         * @param other
         * 		The instance to clone.
         * @unknown 
         */
        public static android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo obtain(android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo other) {
            return android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(other.mRowIndex, other.mRowSpan, other.mColumnIndex, other.mColumnSpan, other.mHeading, other.mSelected);
        }

        /**
         * Obtains a pooled instance.
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
         * 		Whether the item is a heading. (Prefer
         * 		{@link AccessibilityNodeInfo#setHeading(boolean)}).
         */
        public static android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo obtain(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading) {
            return android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(rowIndex, rowSpan, columnIndex, columnSpan, heading, false);
        }

        /**
         * Obtains a pooled instance.
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
         * 		Whether the item is a heading. (Prefer
         * 		{@link AccessibilityNodeInfo#setHeading(boolean)})
         * @param selected
         * 		Whether the item is selected.
         */
        public static android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo obtain(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            final android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo info = android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.sPool.acquire();
            if (info == null) {
                return new android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading, selected);
            }
            info.mRowIndex = rowIndex;
            info.mRowSpan = rowSpan;
            info.mColumnIndex = columnIndex;
            info.mColumnSpan = columnSpan;
            info.mHeading = heading;
            info.mSelected = selected;
            return info;
        }

        private boolean mHeading;

        private int mColumnIndex;

        private int mRowIndex;

        private int mColumnSpan;

        private int mRowSpan;

        private boolean mSelected;

        /**
         * Creates a new instance.
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
         */
        private CollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            mRowIndex = rowIndex;
            mRowSpan = rowSpan;
            mColumnIndex = columnIndex;
            mColumnSpan = columnSpan;
            mHeading = heading;
            mSelected = selected;
        }

        /**
         * Gets the column index at which the item is located.
         *
         * @return The column index.
         */
        public int getColumnIndex() {
            return mColumnIndex;
        }

        /**
         * Gets the row index at which the item is located.
         *
         * @return The row index.
         */
        public int getRowIndex() {
            return mRowIndex;
        }

        /**
         * Gets the number of columns the item spans.
         *
         * @return The column span.
         */
        public int getColumnSpan() {
            return mColumnSpan;
        }

        /**
         * Gets the number of rows the item spans.
         *
         * @return The row span.
         */
        public int getRowSpan() {
            return mRowSpan;
        }

        /**
         * Gets if the collection item is a heading. For example, section
         * heading, table header, etc.
         *
         * @return If the item is a heading.
         * @deprecated Use {@link AccessibilityNodeInfo#isHeading()}
         */
        public boolean isHeading() {
            return mHeading;
        }

        /**
         * Gets if the collection item is selected.
         *
         * @return If the item is selected.
         */
        public boolean isSelected() {
            return mSelected;
        }

        /**
         * Recycles this instance.
         */
        void recycle() {
            clear();
            android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.sPool.release(this);
        }

        private void clear() {
            mColumnIndex = 0;
            mColumnSpan = 0;
            mRowIndex = 0;
            mRowSpan = 0;
            mHeading = false;
            mSelected = false;
        }
    }

    /**
     * Class with information of touch delegated views and regions from {@link TouchDelegate} for
     * the {@link AccessibilityNodeInfo}.
     *
     * @see AccessibilityNodeInfo#setTouchDelegateInfo(TouchDelegateInfo)
     */
    public static final class TouchDelegateInfo implements android.os.Parcelable {
        private android.util.ArrayMap<android.graphics.Region, java.lang.Long> mTargetMap;

        // Two ids are initialized lazily in AccessibilityNodeInfo#getTouchDelegateInfo
        private int mConnectionId;

        private int mWindowId;

        /**
         * Create a new instance of {@link TouchDelegateInfo}.
         *
         * @param targetMap
         * 		A map from regions (in view coordinates) to delegated views.
         * @throws IllegalArgumentException
         * 		if targetMap is empty or {@code null} in
         * 		Regions or Views.
         */
        public TouchDelegateInfo(@android.annotation.NonNull
        java.util.Map<android.graphics.Region, android.view.View> targetMap) {
            com.android.internal.util.Preconditions.checkArgument(((!targetMap.isEmpty()) && (!targetMap.containsKey(null))) && (!targetMap.containsValue(null)));
            mTargetMap = new android.util.ArrayMap(targetMap.size());
            for (final android.graphics.Region region : targetMap.keySet()) {
                final android.view.View view = targetMap.get(region);
                mTargetMap.put(region, ((long) (view.getAccessibilityViewId())));
            }
        }

        /**
         * Create a new instance from target map.
         *
         * @param targetMap
         * 		A map from regions (in view coordinates) to delegated views'
         * 		accessibility id.
         * @param doCopy
         * 		True if shallow copy targetMap.
         * @throws IllegalArgumentException
         * 		if targetMap is empty or {@code null} in
         * 		Regions or Views.
         */
        TouchDelegateInfo(@android.annotation.NonNull
        android.util.ArrayMap<android.graphics.Region, java.lang.Long> targetMap, boolean doCopy) {
            com.android.internal.util.Preconditions.checkArgument(((!targetMap.isEmpty()) && (!targetMap.containsKey(null))) && (!targetMap.containsValue(null)));
            if (doCopy) {
                mTargetMap = new android.util.ArrayMap(targetMap.size());
                mTargetMap.putAll(targetMap);
            } else {
                mTargetMap = targetMap;
            }
        }

        /**
         * Set the connection ID.
         *
         * @param connectionId
         * 		The connection id.
         */
        private void setConnectionId(int connectionId) {
            mConnectionId = connectionId;
        }

        /**
         * Set the window ID.
         *
         * @param windowId
         * 		The window id.
         */
        private void setWindowId(int windowId) {
            mWindowId = windowId;
        }

        /**
         * Returns the number of touch delegate target region.
         *
         * @return Number of touch delegate target region.
         */
        public int getRegionCount() {
            return mTargetMap.size();
        }

        /**
         * Return the {@link Region} at the given index in the {@link TouchDelegateInfo}.
         *
         * @param index
         * 		The desired index, must be between 0 and {@link #getRegionCount()}-1.
         * @return Returns the {@link Region} stored at the given index.
         */
        @android.annotation.NonNull
        public android.graphics.Region getRegionAt(int index) {
            return mTargetMap.keyAt(index);
        }

        /**
         * Return the target {@link AccessibilityNodeInfo} for the given {@link Region}.
         * <p>
         *   <strong>Note:</strong> This api can only be called from {@link AccessibilityService}.
         * </p>
         * <p>
         *   <strong>Note:</strong> It is a client responsibility to recycle the
         *     received info by calling {@link AccessibilityNodeInfo#recycle()}
         *     to avoid creating of multiple instances.
         * </p>
         *
         * @param region
         * 		The region retrieved from {@link #getRegionAt(int)}.
         * @return The target node associates with the given region.
         */
        @android.annotation.Nullable
        public android.view.accessibility.AccessibilityNodeInfo getTargetForRegion(@android.annotation.NonNull
        android.graphics.Region region) {
            return android.view.accessibility.AccessibilityNodeInfo.getNodeForAccessibilityId(mConnectionId, mWindowId, mTargetMap.get(region));
        }

        /**
         * Return the accessibility id of target node.
         *
         * @param region
         * 		The region retrieved from {@link #getRegionAt(int)}.
         * @return The accessibility id of target node.
         * @unknown 
         */
        @android.annotation.TestApi
        public long getAccessibilityIdForRegion(@android.annotation.NonNull
        android.graphics.Region region) {
            return mTargetMap.get(region);
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mTargetMap.size());
            for (int i = 0; i < mTargetMap.size(); i++) {
                final android.graphics.Region region = mTargetMap.keyAt(i);
                final java.lang.Long accessibilityId = mTargetMap.valueAt(i);
                region.writeToParcel(dest, flags);
                dest.writeLong(accessibilityId);
            }
        }

        /**
         *
         *
         * @see android.os.Parcelable.Creator
         */
        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo> CREATOR = new android.os.Parcelable.Creator<android.view.accessibility.AccessibilityNodeInfo.TouchDelegateInfo>() {
            @java.lang.Override
            public android.view.accessibility.TouchDelegateInfo createFromParcel(android.os.Parcel parcel) {
                final int size = parcel.readInt();
                if (size == 0) {
                    return null;
                }
                final ArrayMap<android.graphics.Region, java.lang.Long> targetMap = new android.view.accessibility.ArrayMap<>(size);
                for (int i = 0; i < size; i++) {
                    final android.graphics.Region region = Region.CREATOR.createFromParcel(parcel);
                    final long accessibilityId = parcel.readLong();
                    targetMap.put(region, accessibilityId);
                }
                final android.view.accessibility.TouchDelegateInfo touchDelegateInfo = new android.view.accessibility.TouchDelegateInfo(targetMap, false);
                return touchDelegateInfo;
            }

            @java.lang.Override
            public android.view.accessibility.TouchDelegateInfo[] newArray(int size) {
                return new android.view.accessibility.TouchDelegateInfo[size];
            }
        };
    }

    /**
     *
     *
     * @see android.os.Parcelable.Creator
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.accessibility.AccessibilityNodeInfo> CREATOR = new android.os.Parcelable.Creator<android.view.accessibility.AccessibilityNodeInfo>() {
        @java.lang.Override
        public android.view.accessibility.AccessibilityNodeInfo createFromParcel(android.os.Parcel parcel) {
            android.view.accessibility.AccessibilityNodeInfo info = android.view.accessibility.AccessibilityNodeInfo.obtain();
            info.initFromParcel(parcel);
            return info;
        }

        @java.lang.Override
        public android.view.accessibility.AccessibilityNodeInfo[] newArray(int size) {
            return new android.view.accessibility.AccessibilityNodeInfo[size];
        }
    };
}

