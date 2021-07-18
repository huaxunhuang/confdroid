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
package android.widget;


/**
 * Wrapper class for a ListView. This wrapper can hijack the focus to
 * make sure the list uses the appropriate drawables and states when
 * displayed on screen within a drop down. The focus is never actually
 * passed to the drop down in this mode; the list only looks focused.
 *
 * @unknown 
 */
public class DropDownListView extends android.widget.ListView {
    /* WARNING: This is a workaround for a touch mode issue.

    Touch mode is propagated lazily to windows. This causes problems in
    the following scenario:
    - Type something in the AutoCompleteTextView and get some results
    - Move down with the d-pad to select an item in the list
    - Move up with the d-pad until the selection disappears
    - Type more text in the AutoCompleteTextView *using the soft keyboard*
      and get new results; you are now in touch mode
    - The selection comes back on the first item in the list, even though
      the list is supposed to be in touch mode

    Using the soft keyboard triggers the touch mode change but that change
    is propagated to our window only after the first list layout, therefore
    after the list attempts to resurrect the selection.

    The trick to work around this issue is to pretend the list is in touch
    mode when we know that the selection should not appear, that is when
    we know the user moved the selection away from the list.

    This boolean is set to true whenever we explicitly hide the list's
    selection and reset to false whenever we know the user moved the
    selection back to the list.

    When this boolean is true, isInTouchMode() returns true, otherwise it
    returns super.isInTouchMode().
     */
    private boolean mListSelectionHidden;

    /**
     * True if this wrapper should fake focus.
     */
    private boolean mHijackFocus;

    /**
     * Whether to force drawing of the pressed state selector.
     */
    private boolean mDrawsInPressedState;

    /**
     * Helper for drag-to-open auto scrolling.
     */
    private com.android.internal.widget.AutoScrollHelper.AbsListViewAutoScroller mScrollHelper;

    /**
     * Runnable posted when we are awaiting hover event resolution. When set,
     * drawable state changes are postponed.
     */
    private android.widget.DropDownListView.ResolveHoverRunnable mResolveHoverRunnable;

    /**
     * Creates a new list view wrapper.
     *
     * @param context
     * 		this view's context
     */
    public DropDownListView(@android.annotation.NonNull
    android.content.Context context, boolean hijackFocus) {
        this(context, hijackFocus, com.android.internal.R.attr.dropDownListViewStyle);
    }

    /**
     * Creates a new list view wrapper.
     *
     * @param context
     * 		this view's context
     */
    public DropDownListView(@android.annotation.NonNull
    android.content.Context context, boolean hijackFocus, int defStyleAttr) {
        super(context, null, defStyleAttr);
        mHijackFocus = hijackFocus;
        // TODO: Add an API to control this
        setCacheColorHint(0);// Transparent, since the background drawable could be anything.

    }

    @java.lang.Override
    boolean shouldShowSelector() {
        return isHovered() || super.shouldShowSelector();
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (mResolveHoverRunnable != null) {
            // Resolved hover event as hover => touch transition.
            mResolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(ev);
    }

    @java.lang.Override
    public boolean onHoverEvent(@android.annotation.NonNull
    android.view.MotionEvent ev) {
        final int action = ev.getActionMasked();
        if ((action == android.view.MotionEvent.ACTION_HOVER_EXIT) && (mResolveHoverRunnable == null)) {
            // This may be transitioning to TOUCH_DOWN. Postpone drawable state
            // updates until either the next frame or the next touch event.
            mResolveHoverRunnable = new android.widget.DropDownListView.ResolveHoverRunnable();
            mResolveHoverRunnable.post();
        }
        // Allow the super class to handle hover state management first.
        final boolean handled = super.onHoverEvent(ev);
        if ((action == android.view.MotionEvent.ACTION_HOVER_ENTER) || (action == android.view.MotionEvent.ACTION_HOVER_MOVE)) {
            final int position = pointToPosition(((int) (ev.getX())), ((int) (ev.getY())));
            if ((position != android.widget.AdapterView.INVALID_POSITION) && (position != mSelectedPosition)) {
                final android.view.View hoveredItem = getChildAt(position - getFirstVisiblePosition());
                if (hoveredItem.isEnabled()) {
                    // Force a focus so that the proper selector state gets
                    // used when we update.
                    requestFocus();
                    positionSelector(position, hoveredItem);
                    setSelectedPositionInt(position);
                    setNextSelectedPositionInt(position);
                }
                updateSelectorState();
            }
        } else {
            // Do not cancel the selected position if the selection is visible
            // by other means.
            if (!super.shouldShowSelector()) {
                setSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
                setNextSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
            }
        }
        return handled;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        if (mResolveHoverRunnable == null) {
            super.drawableStateChanged();
        }
    }

    /**
     * Handles forwarded events.
     *
     * @param activePointerId
     * 		id of the pointer that activated forwarding
     * @return whether the event was handled
     */
    public boolean onForwardedEvent(@android.annotation.NonNull
    android.view.MotionEvent event, int activePointerId) {
        boolean handledEvent = true;
        boolean clearPressedItem = false;
        final int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case android.view.MotionEvent.ACTION_CANCEL :
                handledEvent = false;
                break;
            case android.view.MotionEvent.ACTION_UP :
                handledEvent = false;
                // $FALL-THROUGH$
            case android.view.MotionEvent.ACTION_MOVE :
                final int activeIndex = event.findPointerIndex(activePointerId);
                if (activeIndex < 0) {
                    handledEvent = false;
                    break;
                }
                final int x = ((int) (event.getX(activeIndex)));
                final int y = ((int) (event.getY(activeIndex)));
                final int position = pointToPosition(x, y);
                if (position == android.widget.AdapterView.INVALID_POSITION) {
                    clearPressedItem = true;
                    break;
                }
                final android.view.View child = getChildAt(position - getFirstVisiblePosition());
                setPressedItem(child, position, x, y);
                handledEvent = true;
                if (actionMasked == android.view.MotionEvent.ACTION_UP) {
                    final long id = getItemIdAtPosition(position);
                    performItemClick(child, position, id);
                }
                break;
        }
        // Failure to handle the event cancels forwarding.
        if ((!handledEvent) || clearPressedItem) {
            clearPressedItem();
        }
        // Manage automatic scrolling.
        if (handledEvent) {
            if (mScrollHelper == null) {
                mScrollHelper = new com.android.internal.widget.AutoScrollHelper.AbsListViewAutoScroller(this);
            }
            mScrollHelper.setEnabled(true);
            mScrollHelper.onTouch(this, event);
        } else
            if (mScrollHelper != null) {
                mScrollHelper.setEnabled(false);
            }

        return handledEvent;
    }

    /**
     * Sets whether the list selection is hidden, as part of a workaround for a
     * touch mode issue (see the declaration for mListSelectionHidden).
     *
     * @param hideListSelection
     * 		{@code true} to hide list selection,
     * 		{@code false} to show
     */
    public void setListSelectionHidden(boolean hideListSelection) {
        mListSelectionHidden = hideListSelection;
    }

    private void clearPressedItem() {
        mDrawsInPressedState = false;
        setPressed(false);
        updateSelectorState();
        final android.view.View motionView = getChildAt(mMotionPosition - mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
    }

    private void setPressedItem(@android.annotation.NonNull
    android.view.View child, int position, float x, float y) {
        mDrawsInPressedState = true;
        // Ordering is essential. First, update the container's pressed state.
        drawableHotspotChanged(x, y);
        if (!isPressed()) {
            setPressed(true);
        }
        // Next, run layout if we need to stabilize child positions.
        if (mDataChanged) {
            layoutChildren();
        }
        // Manage the pressed view based on motion position. This allows us to
        // play nicely with actual touch and scroll events.
        final android.view.View motionView = getChildAt(mMotionPosition - mFirstPosition);
        if (((motionView != null) && (motionView != child)) && motionView.isPressed()) {
            motionView.setPressed(false);
        }
        mMotionPosition = position;
        // Offset for child coordinates.
        final float childX = x - child.getLeft();
        final float childY = y - child.getTop();
        child.drawableHotspotChanged(childX, childY);
        if (!child.isPressed()) {
            child.setPressed(true);
        }
        // Ensure that keyboard focus starts from the last touched position.
        setSelectedPositionInt(position);
        positionSelectorLikeTouch(position, child, x, y);
        // Refresh the drawable state to reflect the new pressed state,
        // which will also update the selector state.
        refreshDrawableState();
    }

    @java.lang.Override
    boolean touchModeDrawsInPressedState() {
        return mDrawsInPressedState || super.touchModeDrawsInPressedState();
    }

    /**
     * Avoids jarring scrolling effect by ensuring that list elements
     * made of a text view fit on a single line.
     *
     * @param position
     * 		the item index in the list to get a view for
     * @return the view for the specified item
     */
    @java.lang.Override
    android.view.View obtainView(int position, boolean[] isScrap) {
        android.view.View view = super.obtainView(position, isScrap);
        if (view instanceof android.widget.TextView) {
            ((android.widget.TextView) (view)).setHorizontallyScrolling(true);
        }
        return view;
    }

    @java.lang.Override
    public boolean isInTouchMode() {
        // WARNING: Please read the comment where mListSelectionHidden is declared
        return (mHijackFocus && mListSelectionHidden) || super.isInTouchMode();
    }

    /**
     * Returns the focus state in the drop down.
     *
     * @return true always if hijacking focus
     */
    @java.lang.Override
    public boolean hasWindowFocus() {
        return mHijackFocus || super.hasWindowFocus();
    }

    /**
     * Returns the focus state in the drop down.
     *
     * @return true always if hijacking focus
     */
    @java.lang.Override
    public boolean isFocused() {
        return mHijackFocus || super.isFocused();
    }

    /**
     * Returns the focus state in the drop down.
     *
     * @return true always if hijacking focus
     */
    @java.lang.Override
    public boolean hasFocus() {
        return mHijackFocus || super.hasFocus();
    }

    /**
     * Runnable that forces hover event resolution and updates drawable state.
     */
    private class ResolveHoverRunnable implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            // Resolved hover event as standard hover exit.
            mResolveHoverRunnable = null;
            drawableStateChanged();
        }

        public void cancel() {
            mResolveHoverRunnable = null;
            removeCallbacks(this);
        }

        public void post() {
            android.widget.DropDownListView.this.post(this);
        }
    }
}

