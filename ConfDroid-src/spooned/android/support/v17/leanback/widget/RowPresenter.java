/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * An abstract {@link Presenter} that renders an Object in RowsFragment, the object can be
 * subclass {@link Row} or a generic one.  When the object is not {@link Row} class,
 * {@link ViewHolder#getRow()} returns null.
 *
 * <h3>Customize UI widgets</h3>
 * When a subclass of RowPresenter adds UI widgets, it should subclass
 * {@link RowPresenter.ViewHolder} and override {@link #createRowViewHolder(ViewGroup)}
 * and {@link #initializeRowViewHolder(ViewHolder)}. The subclass must use layout id
 * "row_content" for the widget that will be aligned to the title of any {@link HeadersFragment}
 * that may exist in the parent fragment. RowPresenter contains an optional and
 * replaceable {@link RowHeaderPresenter} that renders the header. You can disable
 * the default rendering or replace the Presenter with a new header presenter
 * by calling {@link #setHeaderPresenter(RowHeaderPresenter)}.
 *
 * <h3>UI events from fragments</h3>
 * RowPresenter receives calls from its parent (typically a Fragment) when:
 * <ul>
 * <li>
 * A row is selected via {@link #setRowViewSelected(Presenter.ViewHolder, boolean)}.  The event
 * is triggered immediately when there is a row selection change before the selection
 * animation is started.  Selected status may control activated status of the row (see
 * "Activated status" below).
 * Subclasses of RowPresenter may override {@link #onRowViewSelected(ViewHolder, boolean)}.
 * </li>
 * <li>
 * A row is expanded to full height via {@link #setRowViewExpanded(Presenter.ViewHolder, boolean)}
 * when BrowseFragment hides fast lane on the left.
 * The event is triggered immediately before the expand animation is started.
 * Row title is shown when row is expanded.  Expanded status may control activated status
 * of the row (see "Activated status" below).
 * Subclasses of RowPresenter may override {@link #onRowViewExpanded(ViewHolder, boolean)}.
 * </li>
 * </ul>
 *
 * <h3>Activated status</h3>
 * The activated status of a row is applied to the row view and it's children via
 * {@link View#setActivated(boolean)}.
 * The activated status is typically used to control {@link BaseCardView} info region visibility.
 * The row's activated status can be controlled by selected status and/or expanded status.
 * Call {@link #setSyncActivatePolicy(int)} and choose one of the four policies:
 * <ul>
 * <li>{@link #SYNC_ACTIVATED_TO_EXPANDED} Activated status is synced with row expanded status</li>
 * <li>{@link #SYNC_ACTIVATED_TO_SELECTED} Activated status is synced with row selected status</li>
 * <li>{@link #SYNC_ACTIVATED_TO_EXPANDED_AND_SELECTED} Activated status is set to true
 *     when both expanded and selected status are true</li>
 * <li>{@link #SYNC_ACTIVATED_CUSTOM} Activated status is not controlled by selected status
 *     or expanded status, application can control activated status by its own.
 *     Application should call {@link RowPresenter.ViewHolder#setActivated(boolean)} to change
 *     activated status of row view.
 * </li>
 * </ul>
 *
 * <h3>User events</h3>
 * RowPresenter provides {@link OnItemViewSelectedListener} and {@link OnItemViewClickedListener}.
 * If a subclass wants to add its own {@link View.OnFocusChangeListener} or
 * {@link View.OnClickListener}, it must do that in {@link #createRowViewHolder(ViewGroup)}
 * to be properly chained by the library.  Adding View listeners after
 * {@link #createRowViewHolder(ViewGroup)} is undefined and may result in
 * incorrect behavior by the library's listeners.
 *
 * <h3>Selection animation</h3>
 * <p>
 * When a user scrolls through rows, a fragment will initiate animation and call
 * {@link #setSelectLevel(Presenter.ViewHolder, float)} with float value between
 * 0 and 1.  By default, the RowPresenter draws a dim overlay on top of the row
 * view for views that are not selected. Subclasses may override this default effect
 * by having {@link #isUsingDefaultSelectEffect()} return false and overriding
 * {@link #onSelectLevelChanged(ViewHolder)} to apply a different selection effect.
 * </p>
 * <p>
 * Call {@link #setSelectEffectEnabled(boolean)} to enable/disable the select effect,
 * This will not only enable/disable the default dim effect but also subclasses must
 * respect this flag as well.
 * </p>
 */
public abstract class RowPresenter extends android.support.v17.leanback.widget.Presenter {
    /**
     * Don't synchronize row view activated status with selected status or expanded status,
     * application will do its own through {@link RowPresenter.ViewHolder#setActivated(boolean)}.
     */
    public static final int SYNC_ACTIVATED_CUSTOM = 0;

    /**
     * Synchronizes row view's activated status to expand status of the row view holder.
     */
    public static final int SYNC_ACTIVATED_TO_EXPANDED = 1;

    /**
     * Synchronizes row view's activated status to selected status of the row view holder.
     */
    public static final int SYNC_ACTIVATED_TO_SELECTED = 2;

    /**
     * Sets the row view's activated status to true when both expand and selected are true.
     */
    public static final int SYNC_ACTIVATED_TO_EXPANDED_AND_SELECTED = 3;

    static class ContainerViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        /**
         * wrapped row view holder
         */
        final android.support.v17.leanback.widget.RowPresenter.ViewHolder mRowViewHolder;

        public ContainerViewHolder(android.support.v17.leanback.widget.RowContainerView containerView, android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder) {
            super(containerView);
            containerView.addRowView(rowViewHolder.view);
            if (rowViewHolder.mHeaderViewHolder != null) {
                containerView.addHeaderView(rowViewHolder.mHeaderViewHolder.view);
            }
            mRowViewHolder = rowViewHolder;
            mRowViewHolder.mContainerViewHolder = this;
        }
    }

    /**
     * A ViewHolder for a {@link Row}.
     */
    public static class ViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        private static final int ACTIVATED_NOT_ASSIGNED = 0;

        private static final int ACTIVATED = 1;

        private static final int NOT_ACTIVATED = 2;

        android.support.v17.leanback.widget.RowPresenter.ContainerViewHolder mContainerViewHolder;

        android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder mHeaderViewHolder;

        android.support.v17.leanback.widget.Row mRow;

        java.lang.Object mRowObject;

        int mActivated = android.support.v17.leanback.widget.RowPresenter.ViewHolder.ACTIVATED_NOT_ASSIGNED;

        boolean mSelected;

        boolean mExpanded;

        boolean mInitialzed;

        float mSelectLevel = 0.0F;// initially unselected


        protected final android.support.v17.leanback.graphics.ColorOverlayDimmer mColorDimmer;

        private android.view.View.OnKeyListener mOnKeyListener;

        android.support.v17.leanback.widget.BaseOnItemViewSelectedListener mOnItemViewSelectedListener;

        private android.support.v17.leanback.widget.BaseOnItemViewClickedListener mOnItemViewClickedListener;

        /**
         * Constructor for ViewHolder.
         *
         * @param view
         * 		The View bound to the Row.
         */
        public ViewHolder(android.view.View view) {
            super(view);
            mColorDimmer = android.support.v17.leanback.graphics.ColorOverlayDimmer.createDefault(view.getContext());
        }

        /**
         * Returns the row bound to this ViewHolder. Returns null if the row is not an instance of
         * {@link Row}.
         *
         * @return The row bound to this ViewHolder. Returns null if the row is not an instance of
        {@link Row}.
         */
        public final android.support.v17.leanback.widget.Row getRow() {
            return mRow;
        }

        /**
         * Returns the Row object bound to this ViewHolder.
         *
         * @return The row object bound to this ViewHolder.
         */
        public final java.lang.Object getRowObject() {
            return mRowObject;
        }

        /**
         * Returns whether the Row is in its expanded state.
         *
         * @return true if the Row is expanded, false otherwise.
         */
        public final boolean isExpanded() {
            return mExpanded;
        }

        /**
         * Returns whether the Row is selected.
         *
         * @return true if the Row is selected, false otherwise.
         */
        public final boolean isSelected() {
            return mSelected;
        }

        /**
         * Returns the current selection level of the Row.
         */
        public final float getSelectLevel() {
            return mSelectLevel;
        }

        /**
         * Returns the view holder for the Row header for this Row.
         */
        public final android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder getHeaderViewHolder() {
            return mHeaderViewHolder;
        }

        /**
         * Sets the row view's activated status.  The status will be applied to children through
         * {@link #syncActivatedStatus(View)}.  Application should only call this function
         * when {@link RowPresenter#getSyncActivatePolicy()} is
         * {@link RowPresenter#SYNC_ACTIVATED_CUSTOM}; otherwise the value will
         * be overwritten when expanded or selected status changes.
         */
        public final void setActivated(boolean activated) {
            mActivated = (activated) ? android.support.v17.leanback.widget.RowPresenter.ViewHolder.ACTIVATED : android.support.v17.leanback.widget.RowPresenter.ViewHolder.NOT_ACTIVATED;
        }

        /**
         * Synchronizes the activated status of view to the last value passed through
         * {@link RowPresenter.ViewHolder#setActivated(boolean)}. No operation if
         * {@link RowPresenter.ViewHolder#setActivated(boolean)} is never called.  Normally
         * application does not need to call this method,  {@link ListRowPresenter} automatically
         * calls this method when a child is attached to list row.   However if
         * application writes its own custom RowPresenter, it should call this method
         * when attaches a child to the row view.
         */
        public final void syncActivatedStatus(android.view.View view) {
            if (mActivated == android.support.v17.leanback.widget.RowPresenter.ViewHolder.ACTIVATED) {
                view.setActivated(true);
            } else
                if (mActivated == android.support.v17.leanback.widget.RowPresenter.ViewHolder.NOT_ACTIVATED) {
                    view.setActivated(false);
                }

        }

        /**
         * Sets a key listener.
         */
        public void setOnKeyListener(android.view.View.OnKeyListener keyListener) {
            mOnKeyListener = keyListener;
        }

        /**
         * Returns the key listener.
         */
        public android.view.View.OnKeyListener getOnKeyListener() {
            return mOnKeyListener;
        }

        /**
         * Sets the listener for item or row selection.  RowPresenter fires row selection
         * event with null item.  A subclass of RowPresenter e.g. {@link ListRowPresenter} may
         * fire a selection event with selected item.
         */
        public final void setOnItemViewSelectedListener(android.support.v17.leanback.widget.BaseOnItemViewSelectedListener listener) {
            mOnItemViewSelectedListener = listener;
        }

        /**
         * Returns the listener for item or row selection.
         */
        public final android.support.v17.leanback.widget.BaseOnItemViewSelectedListener getOnItemViewSelectedListener() {
            return mOnItemViewSelectedListener;
        }

        /**
         * Sets the listener for item click event.  RowPresenter does nothing but subclass of
         * RowPresenter may fire item click event if it has the concept of item.
         * OnItemViewClickedListener will override {@link View.OnClickListener} that
         * item presenter sets during {@link Presenter#onCreateViewHolder(ViewGroup)}.
         */
        public final void setOnItemViewClickedListener(android.support.v17.leanback.widget.BaseOnItemViewClickedListener listener) {
            mOnItemViewClickedListener = listener;
        }

        /**
         * Returns the listener for item click event.
         */
        public final android.support.v17.leanback.widget.BaseOnItemViewClickedListener getOnItemViewClickedListener() {
            return mOnItemViewClickedListener;
        }
    }

    private android.support.v17.leanback.widget.RowHeaderPresenter mHeaderPresenter = new android.support.v17.leanback.widget.RowHeaderPresenter();

    boolean mSelectEffectEnabled = true;

    int mSyncActivatePolicy = android.support.v17.leanback.widget.RowPresenter.SYNC_ACTIVATED_TO_EXPANDED;

    /**
     * Constructs a RowPresenter.
     */
    public RowPresenter() {
        mHeaderPresenter.setNullItemVisibilityGone(true);
    }

    @java.lang.Override
    public final android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.support.v17.leanback.widget.RowPresenter.ViewHolder vh = createRowViewHolder(parent);
        vh.mInitialzed = false;
        android.support.v17.leanback.widget.Presenter.ViewHolder result;
        if (needsRowContainerView()) {
            android.support.v17.leanback.widget.RowContainerView containerView = new android.support.v17.leanback.widget.RowContainerView(parent.getContext());
            if (mHeaderPresenter != null) {
                vh.mHeaderViewHolder = ((android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder) (mHeaderPresenter.onCreateViewHolder(((android.view.ViewGroup) (vh.view)))));
            }
            result = new android.support.v17.leanback.widget.RowPresenter.ContainerViewHolder(containerView, vh);
        } else {
            result = vh;
        }
        initializeRowViewHolder(vh);
        if (!vh.mInitialzed) {
            throw new java.lang.RuntimeException("super.initializeRowViewHolder() must be called");
        }
        return result;
    }

    /**
     * Called to create a ViewHolder object for a Row. Subclasses will override
     * this method to return a different concrete ViewHolder object.
     *
     * @param parent
     * 		The parent View for the Row's view holder.
     * @return A ViewHolder for the Row's View.
     */
    protected abstract android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent);

    /**
     * Returns true if the Row view should clip it's children.  The clipChildren
     * flag is set on view in {@link #initializeRowViewHolder(ViewHolder)}.  Note that
     * Slide transition or explode transition need turn off clipChildren.
     * Default value is false.
     */
    protected boolean isClippingChildren() {
        return false;
    }

    /**
     * Called after a {@link RowPresenter.ViewHolder} is created for a Row.
     * Subclasses may override this method and start by calling
     * super.initializeRowViewHolder(ViewHolder).
     *
     * @param vh
     * 		The ViewHolder to initialize for the Row.
     */
    protected void initializeRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        vh.mInitialzed = true;
        if (!isClippingChildren()) {
            // set clip children to false for slide transition
            if (vh.view instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) (vh.view)).setClipChildren(false);
            }
            if (vh.mContainerViewHolder != null) {
                ((android.view.ViewGroup) (vh.mContainerViewHolder.view)).setClipChildren(false);
            }
        }
    }

    /**
     * Sets the Presenter used for rendering the header. Can be null to disable
     * header rendering. The method must be called before creating any Row Views.
     */
    public final void setHeaderPresenter(android.support.v17.leanback.widget.RowHeaderPresenter headerPresenter) {
        mHeaderPresenter = headerPresenter;
    }

    /**
     * Returns the Presenter used for rendering the header, or null if none has been
     * set.
     */
    public final android.support.v17.leanback.widget.RowHeaderPresenter getHeaderPresenter() {
        return mHeaderPresenter;
    }

    /**
     * Returns the {@link RowPresenter.ViewHolder} from the given RowPresenter
     * ViewHolder.
     */
    public final android.support.v17.leanback.widget.RowPresenter.ViewHolder getRowViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        if (holder instanceof android.support.v17.leanback.widget.RowPresenter.ContainerViewHolder) {
            return ((android.support.v17.leanback.widget.RowPresenter.ContainerViewHolder) (holder)).mRowViewHolder;
        } else {
            return ((android.support.v17.leanback.widget.RowPresenter.ViewHolder) (holder));
        }
    }

    /**
     * Sets the expanded state of a Row view.
     *
     * @param holder
     * 		The Row ViewHolder to set expanded state on.
     * @param expanded
     * 		True if the Row is expanded, false otherwise.
     */
    public final void setRowViewExpanded(android.support.v17.leanback.widget.Presenter.ViewHolder holder, boolean expanded) {
        android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder = getRowViewHolder(holder);
        rowViewHolder.mExpanded = expanded;
        onRowViewExpanded(rowViewHolder, expanded);
    }

    /**
     * Sets the selected state of a Row view.
     *
     * @param holder
     * 		The Row ViewHolder to set expanded state on.
     * @param selected
     * 		True if the Row is expanded, false otherwise.
     */
    public final void setRowViewSelected(android.support.v17.leanback.widget.Presenter.ViewHolder holder, boolean selected) {
        android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder = getRowViewHolder(holder);
        rowViewHolder.mSelected = selected;
        onRowViewSelected(rowViewHolder, selected);
    }

    /**
     * Called when the row view's expanded state changes.  A subclass may override this method to
     * respond to expanded state changes of a Row.
     * The default implementation will hide/show the header view. Subclasses may
     * make visual changes to the Row View but must not create animation on the
     * Row view.
     */
    protected void onRowViewExpanded(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, boolean expanded) {
        updateHeaderViewVisibility(vh);
        updateActivateStatus(vh, vh.view);
    }

    /**
     * Updates the view's activate status according to {@link #getSyncActivatePolicy()} and the
     * selected status and expanded status of the RowPresenter ViewHolder.
     */
    private void updateActivateStatus(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, android.view.View view) {
        switch (mSyncActivatePolicy) {
            case android.support.v17.leanback.widget.RowPresenter.SYNC_ACTIVATED_TO_EXPANDED :
                vh.setActivated(vh.isExpanded());
                break;
            case android.support.v17.leanback.widget.RowPresenter.SYNC_ACTIVATED_TO_SELECTED :
                vh.setActivated(vh.isSelected());
                break;
            case android.support.v17.leanback.widget.RowPresenter.SYNC_ACTIVATED_TO_EXPANDED_AND_SELECTED :
                vh.setActivated(vh.isExpanded() && vh.isSelected());
                break;
        }
        vh.syncActivatedStatus(view);
    }

    /**
     * Sets the policy of updating row view activated status.  Can be one of:
     * <li> Default value {@link #SYNC_ACTIVATED_TO_EXPANDED}
     * <li> {@link #SYNC_ACTIVATED_TO_SELECTED}
     * <li> {@link #SYNC_ACTIVATED_TO_EXPANDED_AND_SELECTED}
     * <li> {@link #SYNC_ACTIVATED_CUSTOM}
     */
    public final void setSyncActivatePolicy(int syncActivatePolicy) {
        mSyncActivatePolicy = syncActivatePolicy;
    }

    /**
     * Returns the policy of updating row view activated status.  Can be one of:
     * <li> Default value {@link #SYNC_ACTIVATED_TO_EXPANDED}
     * <li> {@link #SYNC_ACTIVATED_TO_SELECTED}
     * <li> {@link #SYNC_ACTIVATED_TO_EXPANDED_AND_SELECTED}
     * <li> {@link #SYNC_ACTIVATED_CUSTOM}
     */
    public final int getSyncActivatePolicy() {
        return mSyncActivatePolicy;
    }

    /**
     * This method is only called from
     * {@link #onRowViewSelected(ViewHolder, boolean)} onRowViewSelected.
     * The default behavior is to signal row selected events with a null item parameter.
     * A Subclass of RowPresenter having child items should override this method and dispatch
     * events with item information.
     */
    protected void dispatchItemSelectedListener(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, boolean selected) {
        if (selected) {
            if (vh.mOnItemViewSelectedListener != null) {
                vh.mOnItemViewSelectedListener.onItemSelected(null, null, vh, vh.getRowObject());
            }
        }
    }

    /**
     * Called when the given row view changes selection state.  A subclass may override this to
     * respond to selected state changes of a Row.  A subclass may make visual changes to Row view
     * but must not create animation on the Row view.
     */
    protected void onRowViewSelected(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, boolean selected) {
        dispatchItemSelectedListener(vh, selected);
        updateHeaderViewVisibility(vh);
        updateActivateStatus(vh, vh.view);
    }

    private void updateHeaderViewVisibility(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        if ((mHeaderPresenter != null) && (vh.mHeaderViewHolder != null)) {
            android.support.v17.leanback.widget.RowContainerView containerView = ((android.support.v17.leanback.widget.RowContainerView) (vh.mContainerViewHolder.view));
            containerView.showHeader(vh.isExpanded());
        }
    }

    /**
     * Sets the current select level to a value between 0 (unselected) and 1 (selected).
     * Subclasses may override {@link #onSelectLevelChanged(ViewHolder)} to
     * respond to changes in the selected level.
     */
    public final void setSelectLevel(android.support.v17.leanback.widget.Presenter.ViewHolder vh, float level) {
        android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder = getRowViewHolder(vh);
        rowViewHolder.mSelectLevel = level;
        onSelectLevelChanged(rowViewHolder);
    }

    /**
     * Returns the current select level. The value will be between 0 (unselected)
     * and 1 (selected).
     */
    public final float getSelectLevel(android.support.v17.leanback.widget.Presenter.ViewHolder vh) {
        return getRowViewHolder(vh).mSelectLevel;
    }

    /**
     * Callback when the select level changes. The default implementation applies
     * the select level to {@link RowHeaderPresenter#setSelectLevel(RowHeaderPresenter.ViewHolder, float)}
     * when {@link #getSelectEffectEnabled()} is true. Subclasses may override
     * this function and implement a different select effect. In this case,
     * the method {@link #isUsingDefaultSelectEffect()} should also be overridden to disable
     * the default dimming effect.
     */
    protected void onSelectLevelChanged(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        if (getSelectEffectEnabled()) {
            vh.mColorDimmer.setActiveLevel(vh.mSelectLevel);
            if (vh.mHeaderViewHolder != null) {
                mHeaderPresenter.setSelectLevel(vh.mHeaderViewHolder, vh.mSelectLevel);
            }
            if (isUsingDefaultSelectEffect()) {
                ((android.support.v17.leanback.widget.RowContainerView) (vh.mContainerViewHolder.view)).setForegroundColor(vh.mColorDimmer.getPaint().getColor());
            }
        }
    }

    /**
     * Enables or disables the row selection effect.
     * This will not only affect the default dim effect, but subclasses must
     * respect this flag as well.
     */
    public final void setSelectEffectEnabled(boolean applyDimOnSelect) {
        mSelectEffectEnabled = applyDimOnSelect;
    }

    /**
     * Returns true if the row selection effect is enabled.
     * This value not only determines whether the default dim implementation is
     * used, but subclasses must also respect this flag.
     */
    public final boolean getSelectEffectEnabled() {
        return mSelectEffectEnabled;
    }

    /**
     * Returns true if this RowPresenter is using the default dimming effect.
     * A subclass may (most likely) return false and
     * override {@link #onSelectLevelChanged(ViewHolder)}.
     */
    public boolean isUsingDefaultSelectEffect() {
        return true;
    }

    final boolean needsDefaultSelectEffect() {
        return isUsingDefaultSelectEffect() && getSelectEffectEnabled();
    }

    final boolean needsRowContainerView() {
        return (mHeaderPresenter != null) || needsDefaultSelectEffect();
    }

    @java.lang.Override
    public final void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
        onBindRowViewHolder(getRowViewHolder(viewHolder), item);
    }

    /**
     * Binds the given row object to the given ViewHolder.
     * Derived classes of {@link RowPresenter} overriding
     * {@link #onBindRowViewHolder(ViewHolder, Object)} must call through the super class's
     * implementation of this method.
     */
    protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, java.lang.Object item) {
        vh.mRowObject = item;
        vh.mRow = (item instanceof android.support.v17.leanback.widget.Row) ? ((android.support.v17.leanback.widget.Row) (item)) : null;
        if ((vh.mHeaderViewHolder != null) && (vh.getRow() != null)) {
            mHeaderPresenter.onBindViewHolder(vh.mHeaderViewHolder, item);
        }
    }

    @java.lang.Override
    public final void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
        onUnbindRowViewHolder(getRowViewHolder(viewHolder));
    }

    /**
     * Unbinds the given ViewHolder.
     * Derived classes of {@link RowPresenter} overriding {@link #onUnbindRowViewHolder(ViewHolder)}
     * must call through the super class's implementation of this method.
     */
    protected void onUnbindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        if (vh.mHeaderViewHolder != null) {
            mHeaderPresenter.onUnbindViewHolder(vh.mHeaderViewHolder);
        }
        vh.mRow = null;
        vh.mRowObject = null;
    }

    @java.lang.Override
    public final void onViewAttachedToWindow(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        onRowViewAttachedToWindow(getRowViewHolder(holder));
    }

    /**
     * Invoked when the row view is attached to the window.
     */
    protected void onRowViewAttachedToWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        if (vh.mHeaderViewHolder != null) {
            mHeaderPresenter.onViewAttachedToWindow(vh.mHeaderViewHolder);
        }
    }

    @java.lang.Override
    public final void onViewDetachedFromWindow(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        onRowViewDetachedFromWindow(getRowViewHolder(holder));
    }

    /**
     * Invoked when the row view is detached from the window.
     */
    protected void onRowViewDetachedFromWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        if (vh.mHeaderViewHolder != null) {
            mHeaderPresenter.onViewDetachedFromWindow(vh.mHeaderViewHolder);
        }
        android.support.v17.leanback.widget.Presenter.cancelAnimationsRecursive(vh.view);
    }

    /**
     * Freezes/unfreezes the row, typically used when a transition starts/ends.
     * This method is called by the fragment, it should not call it directly by the application.
     */
    public void freeze(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean freeze) {
    }

    /**
     * Changes the visibility of views.  The entrance transition will be run against the views that
     * change visibilities.  A subclass may override and begin with calling
     * super.setEntranceTransitionState().  This method is called by the fragment,
     * it should not be called directly by the application.
     *
     * @param holder
     * 		The ViewHolder of the row.
     * @param afterEntrance
     * 		true if children of row participating in entrance transition
     * 		should be set to visible, false otherwise.
     */
    public void setEntranceTransitionState(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean afterEntrance) {
        if ((holder.mHeaderViewHolder != null) && (holder.mHeaderViewHolder.view.getVisibility() != android.view.View.GONE)) {
            holder.mHeaderViewHolder.view.setVisibility(afterEntrance ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        }
    }
}

