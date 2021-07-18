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
package android.widget;


/**
 * A view that shows items in a vertically scrolling two-level list. This
 * differs from the {@link ListView} by allowing two levels: groups which can
 * individually be expanded to show its children. The items come from the
 * {@link ExpandableListAdapter} associated with this view.
 * <p>
 * Expandable lists are able to show an indicator beside each item to display
 * the item's current state (the states are usually one of expanded group,
 * collapsed group, child, or last child). Use
 * {@link #setChildIndicator(Drawable)} or {@link #setGroupIndicator(Drawable)}
 * (or the corresponding XML attributes) to set these indicators (see the docs
 * for each method to see additional state that each Drawable can have). The
 * default style for an {@link ExpandableListView} provides indicators which
 * will be shown next to Views given to the {@link ExpandableListView}. The
 * layouts android.R.layout.simple_expandable_list_item_1 and
 * android.R.layout.simple_expandable_list_item_2 (which should be used with
 * {@link SimpleCursorTreeAdapter}) contain the preferred position information
 * for indicators.
 * <p>
 * The context menu information set by an {@link ExpandableListView} will be a
 * {@link ExpandableListContextMenuInfo} object with
 * {@link ExpandableListContextMenuInfo#packedPosition} being a packed position
 * that can be used with {@link #getPackedPositionType(long)} and the other
 * similar methods.
 * <p>
 * <em><b>Note:</b></em> You cannot use the value <code>wrap_content</code>
 * for the <code>android:layout_height</code> attribute of a
 * ExpandableListView in XML if the parent's size is also not strictly specified
 * (for example, if the parent were ScrollView you could not specify
 * wrap_content since it also can be any length. However, you can use
 * wrap_content if the ExpandableListView parent has a specific size, such as
 * 100 pixels.
 *
 * @unknown ref android.R.styleable#ExpandableListView_groupIndicator
 * @unknown ref android.R.styleable#ExpandableListView_indicatorLeft
 * @unknown ref android.R.styleable#ExpandableListView_indicatorRight
 * @unknown ref android.R.styleable#ExpandableListView_childIndicator
 * @unknown ref android.R.styleable#ExpandableListView_childIndicatorLeft
 * @unknown ref android.R.styleable#ExpandableListView_childIndicatorRight
 * @unknown ref android.R.styleable#ExpandableListView_childDivider
 * @unknown ref android.R.styleable#ExpandableListView_indicatorStart
 * @unknown ref android.R.styleable#ExpandableListView_indicatorEnd
 * @unknown ref android.R.styleable#ExpandableListView_childIndicatorStart
 * @unknown ref android.R.styleable#ExpandableListView_childIndicatorEnd
 */
public class ExpandableListView extends android.widget.ListView {
    /**
     * The packed position represents a group.
     */
    public static final int PACKED_POSITION_TYPE_GROUP = 0;

    /**
     * The packed position represents a child.
     */
    public static final int PACKED_POSITION_TYPE_CHILD = 1;

    /**
     * The packed position represents a neither/null/no preference.
     */
    public static final int PACKED_POSITION_TYPE_NULL = 2;

    /**
     * The value for a packed position that represents neither/null/no
     * preference. This value is not otherwise possible since a group type
     * (first bit 0) should not have a child position filled.
     */
    public static final long PACKED_POSITION_VALUE_NULL = 0xffffffffL;

    /**
     * The mask (in packed position representation) for the child
     */
    private static final long PACKED_POSITION_MASK_CHILD = 0xffffffffL;

    /**
     * The mask (in packed position representation) for the group
     */
    private static final long PACKED_POSITION_MASK_GROUP = 0x7fffffff00000000L;

    /**
     * The mask (in packed position representation) for the type
     */
    private static final long PACKED_POSITION_MASK_TYPE = 0x8000000000000000L;

    /**
     * The shift amount (in packed position representation) for the group
     */
    private static final long PACKED_POSITION_SHIFT_GROUP = 32;

    /**
     * The shift amount (in packed position representation) for the type
     */
    private static final long PACKED_POSITION_SHIFT_TYPE = 63;

    /**
     * The mask (in integer child position representation) for the child
     */
    private static final long PACKED_POSITION_INT_MASK_CHILD = 0xffffffff;

    /**
     * The mask (in integer group position representation) for the group
     */
    private static final long PACKED_POSITION_INT_MASK_GROUP = 0x7fffffff;

    /**
     * Serves as the glue/translator between a ListView and an ExpandableListView
     */
    @android.annotation.UnsupportedAppUsage
    private android.widget.ExpandableListConnector mConnector;

    /**
     * Gives us Views through group+child positions
     */
    private android.widget.ExpandableListAdapter mAdapter;

    /**
     * Left bound for drawing the indicator.
     */
    @android.annotation.UnsupportedAppUsage
    private int mIndicatorLeft;

    /**
     * Right bound for drawing the indicator.
     */
    @android.annotation.UnsupportedAppUsage
    private int mIndicatorRight;

    /**
     * Start bound for drawing the indicator.
     */
    private int mIndicatorStart;

    /**
     * End bound for drawing the indicator.
     */
    private int mIndicatorEnd;

    /**
     * Left bound for drawing the indicator of a child. Value of
     * {@link #CHILD_INDICATOR_INHERIT} means use mIndicatorLeft.
     */
    private int mChildIndicatorLeft;

    /**
     * Right bound for drawing the indicator of a child. Value of
     * {@link #CHILD_INDICATOR_INHERIT} means use mIndicatorRight.
     */
    private int mChildIndicatorRight;

    /**
     * Start bound for drawing the indicator of a child. Value of
     * {@link #CHILD_INDICATOR_INHERIT} means use mIndicatorStart.
     */
    private int mChildIndicatorStart;

    /**
     * End bound for drawing the indicator of a child. Value of
     * {@link #CHILD_INDICATOR_INHERIT} means use mIndicatorEnd.
     */
    private int mChildIndicatorEnd;

    /**
     * Denotes when a child indicator should inherit this bound from the generic
     * indicator bounds
     */
    public static final int CHILD_INDICATOR_INHERIT = -1;

    /**
     * Denotes an undefined value for an indicator
     */
    private static final int INDICATOR_UNDEFINED = -2;

    /**
     * The indicator drawn next to a group.
     */
    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mGroupIndicator;

    /**
     * The indicator drawn next to a child.
     */
    private android.graphics.drawable.Drawable mChildIndicator;

    private static final int[] EMPTY_STATE_SET = new int[]{  };

    /**
     * State indicating the group is expanded.
     */
    private static final int[] GROUP_EXPANDED_STATE_SET = new int[]{ R.attr.state_expanded };

    /**
     * State indicating the group is empty (has no children).
     */
    private static final int[] GROUP_EMPTY_STATE_SET = new int[]{ R.attr.state_empty };

    /**
     * State indicating the group is expanded and empty (has no children).
     */
    private static final int[] GROUP_EXPANDED_EMPTY_STATE_SET = new int[]{ R.attr.state_expanded, R.attr.state_empty };

    /**
     * States for the group where the 0th bit is expanded and 1st bit is empty.
     */
    @android.annotation.UnsupportedAppUsage
    private static final int[][] GROUP_STATE_SETS = new int[][]{ android.widget.ExpandableListView.EMPTY_STATE_SET// 00
    , android.widget.ExpandableListView.GROUP_EXPANDED_STATE_SET// 01
    , android.widget.ExpandableListView.GROUP_EMPTY_STATE_SET// 10
    , android.widget.ExpandableListView.GROUP_EXPANDED_EMPTY_STATE_SET// 11
     };

    /**
     * State indicating the child is the last within its group.
     */
    private static final int[] CHILD_LAST_STATE_SET = new int[]{ R.attr.state_last };

    /**
     * Drawable to be used as a divider when it is adjacent to any children
     */
    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mChildDivider;

    // Bounds of the indicator to be drawn
    private final android.graphics.Rect mIndicatorRect = new android.graphics.Rect();

    public ExpandableListView(android.content.Context context) {
        this(context, null);
    }

    public ExpandableListView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.expandableListViewStyle);
    }

    public ExpandableListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ExpandableListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.ExpandableListView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, android.widget.com.android.internal.R.styleable.ExpandableListView, attrs, a, defStyleAttr, defStyleRes);
        mGroupIndicator = a.getDrawable(android.widget.com.android.internal.R.styleable.ExpandableListView_groupIndicator);
        mChildIndicator = a.getDrawable(android.widget.com.android.internal.R.styleable.ExpandableListView_childIndicator);
        mIndicatorLeft = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_indicatorLeft, 0);
        mIndicatorRight = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_indicatorRight, 0);
        if ((mIndicatorRight == 0) && (mGroupIndicator != null)) {
            mIndicatorRight = mIndicatorLeft + mGroupIndicator.getIntrinsicWidth();
        }
        mChildIndicatorLeft = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_childIndicatorLeft, android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT);
        mChildIndicatorRight = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_childIndicatorRight, android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT);
        mChildDivider = a.getDrawable(android.widget.com.android.internal.R.styleable.ExpandableListView_childDivider);
        if (!isRtlCompatibilityMode()) {
            mIndicatorStart = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_indicatorStart, android.widget.ExpandableListView.INDICATOR_UNDEFINED);
            mIndicatorEnd = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_indicatorEnd, android.widget.ExpandableListView.INDICATOR_UNDEFINED);
            mChildIndicatorStart = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_childIndicatorStart, android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT);
            mChildIndicatorEnd = a.getDimensionPixelSize(android.widget.com.android.internal.R.styleable.ExpandableListView_childIndicatorEnd, android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT);
        }
        a.recycle();
    }

    /**
     * Return true if we are in RTL compatibility mode (either before Jelly Bean MR1 or
     * RTL not supported)
     */
    private boolean isRtlCompatibilityMode() {
        final int targetSdkVersion = mContext.getApplicationInfo().targetSdkVersion;
        return (targetSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) || (!hasRtlSupport());
    }

    /**
     * Return true if the application tag in the AndroidManifest has set "supportRtl" to true
     */
    private boolean hasRtlSupport() {
        return mContext.getApplicationInfo().hasRtlSupport();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        resolveIndicator();
        resolveChildIndicator();
    }

    /**
     * Resolve start/end indicator. start/end indicator always takes precedence over left/right
     * indicator when defined.
     */
    private void resolveIndicator() {
        final boolean isLayoutRtl = isLayoutRtl();
        if (isLayoutRtl) {
            if (mIndicatorStart >= 0) {
                mIndicatorRight = mIndicatorStart;
            }
            if (mIndicatorEnd >= 0) {
                mIndicatorLeft = mIndicatorEnd;
            }
        } else {
            if (mIndicatorStart >= 0) {
                mIndicatorLeft = mIndicatorStart;
            }
            if (mIndicatorEnd >= 0) {
                mIndicatorRight = mIndicatorEnd;
            }
        }
        if ((mIndicatorRight == 0) && (mGroupIndicator != null)) {
            mIndicatorRight = mIndicatorLeft + mGroupIndicator.getIntrinsicWidth();
        }
    }

    /**
     * Resolve start/end child indicator. start/end child indicator always takes precedence over
     * left/right child indicator when defined.
     */
    private void resolveChildIndicator() {
        final boolean isLayoutRtl = isLayoutRtl();
        if (isLayoutRtl) {
            if (mChildIndicatorStart >= android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT) {
                mChildIndicatorRight = mChildIndicatorStart;
            }
            if (mChildIndicatorEnd >= android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT) {
                mChildIndicatorLeft = mChildIndicatorEnd;
            }
        } else {
            if (mChildIndicatorStart >= android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT) {
                mChildIndicatorLeft = mChildIndicatorStart;
            }
            if (mChildIndicatorEnd >= android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT) {
                mChildIndicatorRight = mChildIndicatorEnd;
            }
        }
    }

    @java.lang.Override
    protected void dispatchDraw(android.graphics.Canvas canvas) {
        // Draw children, etc.
        super.dispatchDraw(canvas);
        // If we have any indicators to draw, we do it here
        if ((mChildIndicator == null) && (mGroupIndicator == null)) {
            return;
        }
        int saveCount = 0;
        final boolean clipToPadding = (mGroupFlags & android.view.ViewGroup.CLIP_TO_PADDING_MASK) == android.view.ViewGroup.CLIP_TO_PADDING_MASK;
        if (clipToPadding) {
            saveCount = canvas.save();
            final int scrollX = mScrollX;
            final int scrollY = mScrollY;
            canvas.clipRect(scrollX + mPaddingLeft, scrollY + mPaddingTop, ((scrollX + mRight) - mLeft) - mPaddingRight, ((scrollY + mBottom) - mTop) - mPaddingBottom);
        }
        final int headerViewsCount = getHeaderViewsCount();
        final int lastChildFlPos = ((mItemCount - getFooterViewsCount()) - headerViewsCount) - 1;
        final int myB = mBottom;
        android.widget.ExpandableListConnector.PositionMetadata pos;
        android.view.View item;
        android.graphics.drawable.Drawable indicator;
        int t;
        int b;
        // Start at a value that is neither child nor group
        int lastItemType = ~(android.widget.ExpandableListPosition.CHILD | android.widget.ExpandableListPosition.GROUP);
        final android.graphics.Rect indicatorRect = mIndicatorRect;
        // The "child" mentioned in the following two lines is this
        // View's child, not referring to an expandable list's
        // notion of a child (as opposed to a group)
        final int childCount = getChildCount();
        for (int i = 0, childFlPos = mFirstPosition - headerViewsCount; i < childCount; i++ , childFlPos++) {
            if (childFlPos < 0) {
                // This child is header
                continue;
            } else
                if (childFlPos > lastChildFlPos) {
                    // This child is footer, so are all subsequent children
                    break;
                }

            item = getChildAt(i);
            t = item.getTop();
            b = item.getBottom();
            // This item isn't on the screen
            if ((b < 0) || (t > myB))
                continue;

            // Get more expandable list-related info for this item
            pos = mConnector.getUnflattenedPos(childFlPos);
            final boolean isLayoutRtl = isLayoutRtl();
            final int width = getWidth();
            // If this item type and the previous item type are different, then we need to change
            // the left & right bounds
            if (pos.position.type != lastItemType) {
                if (pos.position.type == android.widget.ExpandableListPosition.CHILD) {
                    indicatorRect.left = (mChildIndicatorLeft == android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT) ? mIndicatorLeft : mChildIndicatorLeft;
                    indicatorRect.right = (mChildIndicatorRight == android.widget.ExpandableListView.CHILD_INDICATOR_INHERIT) ? mIndicatorRight : mChildIndicatorRight;
                } else {
                    indicatorRect.left = mIndicatorLeft;
                    indicatorRect.right = mIndicatorRight;
                }
                if (isLayoutRtl) {
                    final int temp = indicatorRect.left;
                    indicatorRect.left = width - indicatorRect.right;
                    indicatorRect.right = width - temp;
                    indicatorRect.left -= mPaddingRight;
                    indicatorRect.right -= mPaddingRight;
                } else {
                    indicatorRect.left += mPaddingLeft;
                    indicatorRect.right += mPaddingLeft;
                }
                lastItemType = pos.position.type;
            }
            if (indicatorRect.left != indicatorRect.right) {
                // Use item's full height + the divider height
                if (mStackFromBottom) {
                    // See ListView#dispatchDraw
                    indicatorRect.top = t;// - mDividerHeight;

                    indicatorRect.bottom = b;
                } else {
                    indicatorRect.top = t;
                    indicatorRect.bottom = b;// + mDividerHeight;

                }
                // Get the indicator (with its state set to the item's state)
                indicator = getIndicator(pos);
                if (indicator != null) {
                    // Draw the indicator
                    indicator.setBounds(indicatorRect);
                    indicator.draw(canvas);
                }
            }
            pos.recycle();
        }
        if (clipToPadding) {
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * Gets the indicator for the item at the given position. If the indicator
     * is stateful, the state will be given to the indicator.
     *
     * @param pos
     * 		The flat list position of the item whose indicator
     * 		should be returned.
     * @return The indicator in the proper state.
     */
    private android.graphics.drawable.Drawable getIndicator(android.widget.ExpandableListConnector.PositionMetadata pos) {
        android.graphics.drawable.Drawable indicator;
        if (pos.position.type == android.widget.ExpandableListPosition.GROUP) {
            indicator = mGroupIndicator;
            if ((indicator != null) && indicator.isStateful()) {
                // Empty check based on availability of data.  If the groupMetadata isn't null,
                // we do a check on it. Otherwise, the group is collapsed so we consider it
                // empty for performance reasons.
                boolean isEmpty = (pos.groupMetadata == null) || (pos.groupMetadata.lastChildFlPos == pos.groupMetadata.flPos);
                final int stateSetIndex = (pos.isExpanded() ? 1 : 0)// Expanded?
                 | (isEmpty ? 2 : 0);// Empty?

                indicator.setState(android.widget.ExpandableListView.GROUP_STATE_SETS[stateSetIndex]);
            }
        } else {
            indicator = mChildIndicator;
            if ((indicator != null) && indicator.isStateful()) {
                // No need for a state sets array for the child since it only has two states
                final int[] stateSet = (pos.position.flatListPos == pos.groupMetadata.lastChildFlPos) ? android.widget.ExpandableListView.CHILD_LAST_STATE_SET : android.widget.ExpandableListView.EMPTY_STATE_SET;
                indicator.setState(stateSet);
            }
        }
        return indicator;
    }

    /**
     * Sets the drawable that will be drawn adjacent to every child in the list. This will
     * be drawn using the same height as the normal divider ({@link #setDivider(Drawable)}) or
     * if it does not have an intrinsic height, the height set by {@link #setDividerHeight(int)}.
     *
     * @param childDivider
     * 		The drawable to use.
     */
    public void setChildDivider(android.graphics.drawable.Drawable childDivider) {
        mChildDivider = childDivider;
    }

    @java.lang.Override
    void drawDivider(android.graphics.Canvas canvas, android.graphics.Rect bounds, int childIndex) {
        int flatListPosition = childIndex + mFirstPosition;
        // Only proceed as possible child if the divider isn't above all items (if it is above
        // all items, then the item below it has to be a group)
        if (flatListPosition >= 0) {
            final int adjustedPosition = getFlatPositionForConnector(flatListPosition);
            android.widget.ExpandableListConnector.PositionMetadata pos = mConnector.getUnflattenedPos(adjustedPosition);
            // If this item is a child, or it is a non-empty group that is expanded
            if ((pos.position.type == android.widget.ExpandableListPosition.CHILD) || (pos.isExpanded() && (pos.groupMetadata.lastChildFlPos != pos.groupMetadata.flPos))) {
                // These are the cases where we draw the child divider
                final android.graphics.drawable.Drawable divider = mChildDivider;
                divider.setBounds(bounds);
                divider.draw(canvas);
                pos.recycle();
                return;
            }
            pos.recycle();
        }
        // Otherwise draw the default divider
        super.drawDivider(canvas, bounds, flatListPosition);
    }

    /**
     * This overloaded method should not be used, instead use
     * {@link #setAdapter(ExpandableListAdapter)}.
     * <p>
     * {@inheritDoc }
     */
    @java.lang.Override
    public void setAdapter(android.widget.ListAdapter adapter) {
        throw new java.lang.RuntimeException("For ExpandableListView, use setAdapter(ExpandableListAdapter) instead of " + "setAdapter(ListAdapter)");
    }

    /**
     * This method should not be used, use {@link #getExpandableListAdapter()}.
     */
    @java.lang.Override
    public android.widget.ListAdapter getAdapter() {
        /* The developer should never really call this method on an
        ExpandableListView, so it would be nice to throw a RuntimeException,
        but AdapterView calls this
         */
        return super.getAdapter();
    }

    /**
     * Register a callback to be invoked when an item has been clicked and the
     * caller prefers to receive a ListView-style position instead of a group
     * and/or child position. In most cases, the caller should use
     * {@link #setOnGroupClickListener} and/or {@link #setOnChildClickListener}.
     * <p />
     * {@inheritDoc }
     */
    @java.lang.Override
    public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    /**
     * Sets the adapter that provides data to this view.
     *
     * @param adapter
     * 		The adapter that provides data to this view.
     */
    public void setAdapter(android.widget.ExpandableListAdapter adapter) {
        // Set member variable
        mAdapter = adapter;
        if (adapter != null) {
            // Create the connector
            mConnector = new android.widget.ExpandableListConnector(adapter);
        } else {
            mConnector = null;
        }
        // Link the ListView (superclass) to the expandable list data through the connector
        super.setAdapter(mConnector);
    }

    /**
     * Gets the adapter that provides data to this view.
     *
     * @return The adapter that provides data to this view.
     */
    public android.widget.ExpandableListAdapter getExpandableListAdapter() {
        return mAdapter;
    }

    /**
     *
     *
     * @param position
     * 		An absolute (including header and footer) flat list position.
     * @return true if the position corresponds to a header or a footer item.
     */
    private boolean isHeaderOrFooterPosition(int position) {
        final int footerViewsStart = mItemCount - getFooterViewsCount();
        return (position < getHeaderViewsCount()) || (position >= footerViewsStart);
    }

    /**
     * Converts an absolute item flat position into a group/child flat position, shifting according
     * to the number of header items.
     *
     * @param flatListPosition
     * 		The absolute flat position
     * @return A group/child flat position as expected by the connector.
     */
    private int getFlatPositionForConnector(int flatListPosition) {
        return flatListPosition - getHeaderViewsCount();
    }

    /**
     * Converts a group/child flat position into an absolute flat position, that takes into account
     * the possible headers.
     *
     * @param flatListPosition
     * 		The child/group flat position
     * @return An absolute flat position.
     */
    private int getAbsoluteFlatPosition(int flatListPosition) {
        return flatListPosition + getHeaderViewsCount();
    }

    @java.lang.Override
    public boolean performItemClick(android.view.View v, int position, long id) {
        // Ignore clicks in header/footers
        if (isHeaderOrFooterPosition(position)) {
            // Clicked on a header/footer, so ignore pass it on to super
            return super.performItemClick(v, position, id);
        }
        // Internally handle the item click
        final int adjustedPosition = getFlatPositionForConnector(position);
        return handleItemClick(v, adjustedPosition, id);
    }

    /**
     * This will either expand/collapse groups (if a group was clicked) or pass
     * on the click to the proper child (if a child was clicked)
     *
     * @param position
     * 		The flat list position. This has already been factored to
     * 		remove the header/footer.
     * @param id
     * 		The ListAdapter ID, not the group or child ID.
     */
    boolean handleItemClick(android.view.View v, int position, long id) {
        final android.widget.ExpandableListConnector.PositionMetadata posMetadata = mConnector.getUnflattenedPos(position);
        id = getChildOrGroupId(posMetadata.position);
        boolean returnValue;
        if (posMetadata.position.type == android.widget.ExpandableListPosition.GROUP) {
            /* It's a group, so handle collapsing/expanding */
            /* It's a group click, so pass on event */
            if (mOnGroupClickListener != null) {
                if (mOnGroupClickListener.onGroupClick(this, v, posMetadata.position.groupPos, id)) {
                    posMetadata.recycle();
                    return true;
                }
            }
            if (posMetadata.isExpanded()) {
                /* Collapse it */
                mConnector.collapseGroup(posMetadata);
                playSoundEffect(android.view.SoundEffectConstants.CLICK);
                if (mOnGroupCollapseListener != null) {
                    mOnGroupCollapseListener.onGroupCollapse(posMetadata.position.groupPos);
                }
            } else {
                /* Expand it */
                mConnector.expandGroup(posMetadata);
                playSoundEffect(android.view.SoundEffectConstants.CLICK);
                if (mOnGroupExpandListener != null) {
                    mOnGroupExpandListener.onGroupExpand(posMetadata.position.groupPos);
                }
                final int groupPos = posMetadata.position.groupPos;
                final int groupFlatPos = posMetadata.position.flatListPos;
                final int shiftedGroupPosition = groupFlatPos + getHeaderViewsCount();
                smoothScrollToPosition(shiftedGroupPosition + mAdapter.getChildrenCount(groupPos), shiftedGroupPosition);
            }
            returnValue = true;
        } else {
            /* It's a child, so pass on event */
            if (mOnChildClickListener != null) {
                playSoundEffect(android.view.SoundEffectConstants.CLICK);
                return mOnChildClickListener.onChildClick(this, v, posMetadata.position.groupPos, posMetadata.position.childPos, id);
            }
            returnValue = false;
        }
        posMetadata.recycle();
        return returnValue;
    }

    /**
     * Expand a group in the grouped list view
     *
     * @param groupPos
     * 		the group to be expanded
     * @return True if the group was expanded, false otherwise (if the group
    was already expanded, this will return false)
     */
    public boolean expandGroup(int groupPos) {
        return expandGroup(groupPos, false);
    }

    /**
     * Expand a group in the grouped list view
     *
     * @param groupPos
     * 		the group to be expanded
     * @param animate
     * 		true if the expanding group should be animated in
     * @return True if the group was expanded, false otherwise (if the group
    was already expanded, this will return false)
     */
    public boolean expandGroup(int groupPos, boolean animate) {
        android.widget.ExpandableListPosition elGroupPos = android.widget.ExpandableListPosition.obtain(android.widget.ExpandableListPosition.GROUP, groupPos, -1, -1);
        android.widget.ExpandableListConnector.PositionMetadata pm = mConnector.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        boolean retValue = mConnector.expandGroup(pm);
        if (mOnGroupExpandListener != null) {
            mOnGroupExpandListener.onGroupExpand(groupPos);
        }
        if (animate) {
            final int groupFlatPos = pm.position.flatListPos;
            final int shiftedGroupPosition = groupFlatPos + getHeaderViewsCount();
            smoothScrollToPosition(shiftedGroupPosition + mAdapter.getChildrenCount(groupPos), shiftedGroupPosition);
        }
        pm.recycle();
        return retValue;
    }

    /**
     * Collapse a group in the grouped list view
     *
     * @param groupPos
     * 		position of the group to collapse
     * @return True if the group was collapsed, false otherwise (if the group
    was already collapsed, this will return false)
     */
    public boolean collapseGroup(int groupPos) {
        boolean retValue = mConnector.collapseGroup(groupPos);
        if (mOnGroupCollapseListener != null) {
            mOnGroupCollapseListener.onGroupCollapse(groupPos);
        }
        return retValue;
    }

    /**
     * Used for being notified when a group is collapsed
     */
    public interface OnGroupCollapseListener {
        /**
         * Callback method to be invoked when a group in this expandable list has
         * been collapsed.
         *
         * @param groupPosition
         * 		The group position that was collapsed
         */
        void onGroupCollapse(int groupPosition);
    }

    @android.annotation.UnsupportedAppUsage
    private android.widget.ExpandableListView.OnGroupCollapseListener mOnGroupCollapseListener;

    public void setOnGroupCollapseListener(android.widget.ExpandableListView.OnGroupCollapseListener onGroupCollapseListener) {
        mOnGroupCollapseListener = onGroupCollapseListener;
    }

    /**
     * Used for being notified when a group is expanded
     */
    public interface OnGroupExpandListener {
        /**
         * Callback method to be invoked when a group in this expandable list has
         * been expanded.
         *
         * @param groupPosition
         * 		The group position that was expanded
         */
        void onGroupExpand(int groupPosition);
    }

    @android.annotation.UnsupportedAppUsage
    private android.widget.ExpandableListView.OnGroupExpandListener mOnGroupExpandListener;

    public void setOnGroupExpandListener(android.widget.ExpandableListView.OnGroupExpandListener onGroupExpandListener) {
        mOnGroupExpandListener = onGroupExpandListener;
    }

    /**
     * Interface definition for a callback to be invoked when a group in this
     * expandable list has been clicked.
     */
    public interface OnGroupClickListener {
        /**
         * Callback method to be invoked when a group in this expandable list has
         * been clicked.
         *
         * @param parent
         * 		The ExpandableListConnector where the click happened
         * @param v
         * 		The view within the expandable list/ListView that was clicked
         * @param groupPosition
         * 		The group position that was clicked
         * @param id
         * 		The row id of the group that was clicked
         * @return True if the click was handled
         */
        boolean onGroupClick(android.widget.ExpandableListView parent, android.view.View v, int groupPosition, long id);
    }

    @android.annotation.UnsupportedAppUsage
    private android.widget.ExpandableListView.OnGroupClickListener mOnGroupClickListener;

    public void setOnGroupClickListener(android.widget.ExpandableListView.OnGroupClickListener onGroupClickListener) {
        mOnGroupClickListener = onGroupClickListener;
    }

    /**
     * Interface definition for a callback to be invoked when a child in this
     * expandable list has been clicked.
     */
    public interface OnChildClickListener {
        /**
         * Callback method to be invoked when a child in this expandable list has
         * been clicked.
         *
         * @param parent
         * 		The ExpandableListView where the click happened
         * @param v
         * 		The view within the expandable list/ListView that was clicked
         * @param groupPosition
         * 		The group position that contains the child that
         * 		was clicked
         * @param childPosition
         * 		The child position within the group
         * @param id
         * 		The row id of the child that was clicked
         * @return True if the click was handled
         */
        boolean onChildClick(android.widget.ExpandableListView parent, android.view.View v, int groupPosition, int childPosition, long id);
    }

    @android.annotation.UnsupportedAppUsage
    private android.widget.ExpandableListView.OnChildClickListener mOnChildClickListener;

    public void setOnChildClickListener(android.widget.ExpandableListView.OnChildClickListener onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }

    /**
     * Converts a flat list position (the raw position of an item (child or group)
     * in the list) to a group and/or child position (represented in a
     * packed position). This is useful in situations where the caller needs to
     * use the underlying {@link ListView}'s methods. Use
     * {@link ExpandableListView#getPackedPositionType} ,
     * {@link ExpandableListView#getPackedPositionChild},
     * {@link ExpandableListView#getPackedPositionGroup} to unpack.
     *
     * @param flatListPosition
     * 		The flat list position to be converted.
     * @return The group and/or child position for the given flat list position
    in packed position representation. #PACKED_POSITION_VALUE_NULL if
    the position corresponds to a header or a footer item.
     */
    public long getExpandableListPosition(int flatListPosition) {
        if (isHeaderOrFooterPosition(flatListPosition)) {
            return android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL;
        }
        final int adjustedPosition = getFlatPositionForConnector(flatListPosition);
        android.widget.ExpandableListConnector.PositionMetadata pm = mConnector.getUnflattenedPos(adjustedPosition);
        long packedPos = pm.position.getPackedPosition();
        pm.recycle();
        return packedPos;
    }

    /**
     * Converts a group and/or child position to a flat list position. This is
     * useful in situations where the caller needs to use the underlying
     * {@link ListView}'s methods.
     *
     * @param packedPosition
     * 		The group and/or child positions to be converted in
     * 		packed position representation. Use
     * 		{@link #getPackedPositionForChild(int, int)} or
     * 		{@link #getPackedPositionForGroup(int)}.
     * @return The flat list position for the given child or group.
     */
    public int getFlatListPosition(long packedPosition) {
        android.widget.ExpandableListPosition elPackedPos = android.widget.ExpandableListPosition.obtainPosition(packedPosition);
        android.widget.ExpandableListConnector.PositionMetadata pm = mConnector.getFlattenedPos(elPackedPos);
        elPackedPos.recycle();
        final int flatListPosition = pm.position.flatListPos;
        pm.recycle();
        return getAbsoluteFlatPosition(flatListPosition);
    }

    /**
     * Gets the position of the currently selected group or child (along with
     * its type). Can return {@link #PACKED_POSITION_VALUE_NULL} if no selection.
     *
     * @return A packed position containing the currently selected group or
    child's position and type. #PACKED_POSITION_VALUE_NULL if no selection
    or if selection is on a header or a footer item.
     */
    public long getSelectedPosition() {
        final int selectedPos = getSelectedItemPosition();
        // The case where there is no selection (selectedPos == -1) is also handled here.
        return getExpandableListPosition(selectedPos);
    }

    /**
     * Gets the ID of the currently selected group or child. Can return -1 if no
     * selection.
     *
     * @return The ID of the currently selected group or child. -1 if no
    selection.
     */
    public long getSelectedId() {
        long packedPos = getSelectedPosition();
        if (packedPos == android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL)
            return -1;

        int groupPos = android.widget.ExpandableListView.getPackedPositionGroup(packedPos);
        if (android.widget.ExpandableListView.getPackedPositionType(packedPos) == android.widget.ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            // It's a group
            return mAdapter.getGroupId(groupPos);
        } else {
            // It's a child
            return mAdapter.getChildId(groupPos, android.widget.ExpandableListView.getPackedPositionChild(packedPos));
        }
    }

    /**
     * Sets the selection to the specified group.
     *
     * @param groupPosition
     * 		The position of the group that should be selected.
     */
    public void setSelectedGroup(int groupPosition) {
        android.widget.ExpandableListPosition elGroupPos = android.widget.ExpandableListPosition.obtainGroupPosition(groupPosition);
        android.widget.ExpandableListConnector.PositionMetadata pm = mConnector.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        final int absoluteFlatPosition = getAbsoluteFlatPosition(pm.position.flatListPos);
        super.setSelection(absoluteFlatPosition);
        pm.recycle();
    }

    /**
     * Sets the selection to the specified child. If the child is in a collapsed
     * group, the group will only be expanded and child subsequently selected if
     * shouldExpandGroup is set to true, otherwise the method will return false.
     *
     * @param groupPosition
     * 		The position of the group that contains the child.
     * @param childPosition
     * 		The position of the child within the group.
     * @param shouldExpandGroup
     * 		Whether the child's group should be expanded if
     * 		it is collapsed.
     * @return Whether the selection was successfully set on the child.
     */
    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        android.widget.ExpandableListPosition elChildPos = android.widget.ExpandableListPosition.obtainChildPosition(groupPosition, childPosition);
        android.widget.ExpandableListConnector.PositionMetadata flatChildPos = mConnector.getFlattenedPos(elChildPos);
        if (flatChildPos == null) {
            // The child's group isn't expanded
            // Shouldn't expand the group, so return false for we didn't set the selection
            if (!shouldExpandGroup)
                return false;

            expandGroup(groupPosition);
            flatChildPos = mConnector.getFlattenedPos(elChildPos);
            // Sanity check
            if (flatChildPos == null) {
                throw new java.lang.IllegalStateException("Could not find child");
            }
        }
        int absoluteFlatPosition = getAbsoluteFlatPosition(flatChildPos.position.flatListPos);
        super.setSelection(absoluteFlatPosition);
        elChildPos.recycle();
        flatChildPos.recycle();
        return true;
    }

    /**
     * Whether the given group is currently expanded.
     *
     * @param groupPosition
     * 		The group to check.
     * @return Whether the group is currently expanded.
     */
    public boolean isGroupExpanded(int groupPosition) {
        return mConnector.isGroupExpanded(groupPosition);
    }

    /**
     * Gets the type of a packed position. See
     * {@link #getPackedPositionForChild(int, int)}.
     *
     * @param packedPosition
     * 		The packed position for which to return the type.
     * @return The type of the position contained within the packed position,
    either {@link #PACKED_POSITION_TYPE_CHILD}, {@link #PACKED_POSITION_TYPE_GROUP}, or
    {@link #PACKED_POSITION_TYPE_NULL}.
     */
    public static int getPackedPositionType(long packedPosition) {
        if (packedPosition == android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL) {
            return android.widget.ExpandableListView.PACKED_POSITION_TYPE_NULL;
        }
        return (packedPosition & android.widget.ExpandableListView.PACKED_POSITION_MASK_TYPE) == android.widget.ExpandableListView.PACKED_POSITION_MASK_TYPE ? android.widget.ExpandableListView.PACKED_POSITION_TYPE_CHILD : android.widget.ExpandableListView.PACKED_POSITION_TYPE_GROUP;
    }

    /**
     * Gets the group position from a packed position. See
     * {@link #getPackedPositionForChild(int, int)}.
     *
     * @param packedPosition
     * 		The packed position from which the group position
     * 		will be returned.
     * @return The group position portion of the packed position. If this does
    not contain a group, returns -1.
     */
    public static int getPackedPositionGroup(long packedPosition) {
        // Null
        if (packedPosition == android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL)
            return -1;

        return ((int) ((packedPosition & android.widget.ExpandableListView.PACKED_POSITION_MASK_GROUP) >> android.widget.ExpandableListView.PACKED_POSITION_SHIFT_GROUP));
    }

    /**
     * Gets the child position from a packed position that is of
     * {@link #PACKED_POSITION_TYPE_CHILD} type (use {@link #getPackedPositionType(long)}).
     * To get the group that this child belongs to, use
     * {@link #getPackedPositionGroup(long)}. See
     * {@link #getPackedPositionForChild(int, int)}.
     *
     * @param packedPosition
     * 		The packed position from which the child position
     * 		will be returned.
     * @return The child position portion of the packed position. If this does
    not contain a child, returns -1.
     */
    public static int getPackedPositionChild(long packedPosition) {
        // Null
        if (packedPosition == android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL)
            return -1;

        // Group since a group type clears this bit
        if ((packedPosition & android.widget.ExpandableListView.PACKED_POSITION_MASK_TYPE) != android.widget.ExpandableListView.PACKED_POSITION_MASK_TYPE)
            return -1;

        return ((int) (packedPosition & android.widget.ExpandableListView.PACKED_POSITION_MASK_CHILD));
    }

    /**
     * Returns the packed position representation of a child's position.
     * <p>
     * In general, a packed position should be used in
     * situations where the position given to/returned from an
     * {@link ExpandableListAdapter} or {@link ExpandableListView} method can
     * either be a child or group. The two positions are packed into a single
     * long which can be unpacked using
     * {@link #getPackedPositionChild(long)},
     * {@link #getPackedPositionGroup(long)}, and
     * {@link #getPackedPositionType(long)}.
     *
     * @param groupPosition
     * 		The child's parent group's position.
     * @param childPosition
     * 		The child position within the group.
     * @return The packed position representation of the child (and parent group).
     */
    public static long getPackedPositionForChild(int groupPosition, int childPosition) {
        return ((((long) (android.widget.ExpandableListView.PACKED_POSITION_TYPE_CHILD)) << android.widget.ExpandableListView.PACKED_POSITION_SHIFT_TYPE) | ((((long) (groupPosition)) & android.widget.ExpandableListView.PACKED_POSITION_INT_MASK_GROUP) << android.widget.ExpandableListView.PACKED_POSITION_SHIFT_GROUP)) | (childPosition & android.widget.ExpandableListView.PACKED_POSITION_INT_MASK_CHILD);
    }

    /**
     * Returns the packed position representation of a group's position. See
     * {@link #getPackedPositionForChild(int, int)}.
     *
     * @param groupPosition
     * 		The child's parent group's position.
     * @return The packed position representation of the group.
     */
    public static long getPackedPositionForGroup(int groupPosition) {
        // No need to OR a type in because PACKED_POSITION_GROUP == 0
        return (((long) (groupPosition)) & android.widget.ExpandableListView.PACKED_POSITION_INT_MASK_GROUP) << android.widget.ExpandableListView.PACKED_POSITION_SHIFT_GROUP;
    }

    @java.lang.Override
    android.view.ContextMenu.ContextMenuInfo createContextMenuInfo(android.view.View view, int flatListPosition, long id) {
        if (isHeaderOrFooterPosition(flatListPosition)) {
            // Return normal info for header/footer view context menus
            return new android.widget.AdapterView.AdapterContextMenuInfo(view, flatListPosition, id);
        }
        final int adjustedPosition = getFlatPositionForConnector(flatListPosition);
        android.widget.ExpandableListConnector.PositionMetadata pm = mConnector.getUnflattenedPos(adjustedPosition);
        android.widget.ExpandableListPosition pos = pm.position;
        id = getChildOrGroupId(pos);
        long packedPosition = pos.getPackedPosition();
        pm.recycle();
        return new android.widget.ExpandableListView.ExpandableListContextMenuInfo(view, packedPosition, id);
    }

    /**
     * Gets the ID of the group or child at the given <code>position</code>.
     * This is useful since there is no ListAdapter ID -> ExpandableListAdapter
     * ID conversion mechanism (in some cases, it isn't possible).
     *
     * @param position
     * 		The position of the child or group whose ID should be
     * 		returned.
     */
    private long getChildOrGroupId(android.widget.ExpandableListPosition position) {
        if (position.type == android.widget.ExpandableListPosition.CHILD) {
            return mAdapter.getChildId(position.groupPos, position.childPos);
        } else {
            return mAdapter.getGroupId(position.groupPos);
        }
    }

    /**
     * Sets the indicator to be drawn next to a child.
     *
     * @param childIndicator
     * 		The drawable to be used as an indicator. If the
     * 		child is the last child for a group, the state
     * 		{@link android.R.attr#state_last} will be set.
     */
    public void setChildIndicator(android.graphics.drawable.Drawable childIndicator) {
        mChildIndicator = childIndicator;
    }

    /**
     * Sets the drawing bounds for the child indicator. For either, you can
     * specify {@link #CHILD_INDICATOR_INHERIT} to use inherit from the general
     * indicator's bounds.
     *
     * @see #setIndicatorBounds(int, int)
     * @param left
     * 		The left position (relative to the left bounds of this View)
     * 		to start drawing the indicator.
     * @param right
     * 		The right position (relative to the left bounds of this
     * 		View) to end the drawing of the indicator.
     */
    public void setChildIndicatorBounds(int left, int right) {
        mChildIndicatorLeft = left;
        mChildIndicatorRight = right;
        resolveChildIndicator();
    }

    /**
     * Sets the relative drawing bounds for the child indicator. For either, you can
     * specify {@link #CHILD_INDICATOR_INHERIT} to use inherit from the general
     * indicator's bounds.
     *
     * @see #setIndicatorBounds(int, int)
     * @param start
     * 		The start position (relative to the start bounds of this View)
     * 		to start drawing the indicator.
     * @param end
     * 		The end position (relative to the end bounds of this
     * 		View) to end the drawing of the indicator.
     */
    public void setChildIndicatorBoundsRelative(int start, int end) {
        mChildIndicatorStart = start;
        mChildIndicatorEnd = end;
        resolveChildIndicator();
    }

    /**
     * Sets the indicator to be drawn next to a group.
     *
     * @param groupIndicator
     * 		The drawable to be used as an indicator. If the
     * 		group is empty, the state {@link android.R.attr#state_empty} will be
     * 		set. If the group is expanded, the state
     * 		{@link android.R.attr#state_expanded} will be set.
     */
    public void setGroupIndicator(android.graphics.drawable.Drawable groupIndicator) {
        mGroupIndicator = groupIndicator;
        if ((mIndicatorRight == 0) && (mGroupIndicator != null)) {
            mIndicatorRight = mIndicatorLeft + mGroupIndicator.getIntrinsicWidth();
        }
    }

    /**
     * Sets the drawing bounds for the indicators (at minimum, the group indicator
     * is affected by this; the child indicator is affected by this if the
     * child indicator bounds are set to inherit).
     *
     * @see #setChildIndicatorBounds(int, int)
     * @param left
     * 		The left position (relative to the left bounds of this View)
     * 		to start drawing the indicator.
     * @param right
     * 		The right position (relative to the left bounds of this
     * 		View) to end the drawing of the indicator.
     */
    public void setIndicatorBounds(int left, int right) {
        mIndicatorLeft = left;
        mIndicatorRight = right;
        resolveIndicator();
    }

    /**
     * Sets the relative drawing bounds for the indicators (at minimum, the group indicator
     * is affected by this; the child indicator is affected by this if the
     * child indicator bounds are set to inherit).
     *
     * @see #setChildIndicatorBounds(int, int)
     * @param start
     * 		The start position (relative to the start bounds of this View)
     * 		to start drawing the indicator.
     * @param end
     * 		The end position (relative to the end bounds of this
     * 		View) to end the drawing of the indicator.
     */
    public void setIndicatorBoundsRelative(int start, int end) {
        mIndicatorStart = start;
        mIndicatorEnd = end;
        resolveIndicator();
    }

    /**
     * Extra menu information specific to an {@link ExpandableListView} provided
     * to the
     * {@link android.view.View.OnCreateContextMenuListener#onCreateContextMenu(ContextMenu, View, ContextMenuInfo)}
     * callback when a context menu is brought up for this AdapterView.
     */
    public static class ExpandableListContextMenuInfo implements android.view.ContextMenu.ContextMenuInfo {
        public ExpandableListContextMenuInfo(android.view.View targetView, long packedPosition, long id) {
            this.targetView = targetView;
            this.packedPosition = packedPosition;
            this.id = id;
        }

        /**
         * The view for which the context menu is being displayed. This
         * will be one of the children Views of this {@link ExpandableListView}.
         */
        public android.view.View targetView;

        /**
         * The packed position in the list represented by the adapter for which
         * the context menu is being displayed. Use the methods
         * {@link ExpandableListView#getPackedPositionType},
         * {@link ExpandableListView#getPackedPositionChild}, and
         * {@link ExpandableListView#getPackedPositionGroup} to unpack this.
         */
        public long packedPosition;

        /**
         * The ID of the item (group or child) for which the context menu is
         * being displayed.
         */
        public long id;
    }

    static class SavedState extends android.view.View.BaseSavedState {
        java.util.ArrayList<android.widget.ExpandableListConnector.GroupMetadata> expandedGroupMetadataList;

        /**
         * Constructor called from {@link ExpandableListView#onSaveInstanceState()}
         */
        SavedState(android.os.Parcelable superState, java.util.ArrayList<android.widget.ExpandableListConnector.GroupMetadata> expandedGroupMetadataList) {
            super(superState);
            this.expandedGroupMetadataList = expandedGroupMetadataList;
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(android.os.Parcel in) {
            super(in);
            expandedGroupMetadataList = new java.util.ArrayList<android.widget.ExpandableListConnector.GroupMetadata>();
            in.readList(expandedGroupMetadataList, android.widget.ExpandableListConnector.class.getClassLoader());
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeList(expandedGroupMetadataList);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.ExpandableListView.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.ExpandableListView.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        return new android.widget.ExpandableListView.SavedState(superState, mConnector != null ? mConnector.getExpandedGroupMetadataList() : null);
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.widget.ExpandableListView.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.widget.ExpandableListView.SavedState ss = ((android.widget.ExpandableListView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        if ((mConnector != null) && (ss.expandedGroupMetadataList != null)) {
            mConnector.setExpandedGroupMetadataList(ss.expandedGroupMetadataList);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ExpandableListView.class.getName();
    }
}

