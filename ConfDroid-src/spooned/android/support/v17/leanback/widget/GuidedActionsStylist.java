/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * GuidedActionsStylist is used within a {@link android.support.v17.leanback.app.GuidedStepFragment}
 * to supply the right-side panel where users can take actions. It consists of a container for the
 * list of actions, and a stationary selector view that indicates visually the location of focus.
 * GuidedActionsStylist has two different layouts: default is for normal actions including text,
 * radio, checkbox, DatePicker, etc, the other when {@link #setAsButtonActions()} is called is
 * recommended for button actions such as "yes", "no".
 * <p>
 * Many aspects of the base GuidedActionsStylist can be customized through theming; see the
 * theme attributes below. Note that these attributes are not set on individual elements in layout
 * XML, but instead would be set in a custom theme. See
 * <a href="http://developer.android.com/guide/topics/ui/themes.html">Styles and Themes</a>
 * for more information.
 * <p>
 * If these hooks are insufficient, this class may also be subclassed. Subclasses may wish to
 * override the {@link #onProvideLayoutId} method to change the layout used to display the
 * list container and selector; override {@link #onProvideItemLayoutId(int)} and
 * {@link #getItemViewType(GuidedAction)} method to change the layout used to display each action.
 * <p>
 * To support a "click to activate" view similar to DatePicker, app needs:
 * <li> Override {@link #onProvideItemLayoutId(int)} and {@link #getItemViewType(GuidedAction)},
 * provides a layout id for the action.
 * <li> The layout must include a widget with id "guidedactions_activator_item", the widget is
 * toggled edit mode by {@link View#setActivated(boolean)}.
 * <li> Override {@link #onBindActivatorView(ViewHolder, GuidedAction)} to populate values into View.
 * <li> Override {@link #onUpdateActivatorView(ViewHolder, GuidedAction)} to update action.
 * <p>
 * Note: If an alternate list layout is provided, the following view IDs must be supplied:
 * <ul>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_list}</li>
 * </ul><p>
 * These view IDs must be present in order for the stylist to function. The list ID must correspond
 * to a {@link VerticalGridView} or subclass.
 * <p>
 * If an alternate item layout is provided, the following view IDs should be used to refer to base
 * elements:
 * <ul>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_item_content}</li>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_item_title}</li>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_item_description}</li>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_item_icon}</li>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_item_checkmark}</li>
 * <li>{@link android.support.v17.leanback.R.id#guidedactions_item_chevron}</li>
 * </ul><p>
 * These view IDs are allowed to be missing, in which case the corresponding views in {@link GuidedActionsStylist.ViewHolder} will be null.
 * <p>
 * In order to support editable actions, the view associated with guidedactions_item_title should
 * be a subclass of {@link android.widget.EditText}, and should satisfy the {@link ImeKeyMonitor} interface.
 *
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedStepImeAppearingAnimation
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedStepImeDisappearingAnimation
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionsSelectorDrawable
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionsListStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedSubActionsListStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedButtonActionsListStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemContainerStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemCheckmarkStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemIconStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemContentStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemTitleStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemDescriptionStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionItemChevronStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionPressedAnimation
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionUnpressedAnimation
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionEnabledChevronAlpha
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionDisabledChevronAlpha
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionTitleMinLines
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionTitleMaxLines
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionDescriptionMinLines
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackGuidedStepTheme_guidedActionVerticalPadding
 * @see android.R.styleable#Theme_listChoiceIndicatorSingle
 * @see android.R.styleable#Theme_listChoiceIndicatorMultiple
 * @see android.support.v17.leanback.app.GuidedStepFragment
 * @see GuidedAction
 */
public class GuidedActionsStylist implements android.support.v17.leanback.widget.FragmentAnimationProvider {
    /**
     * Default viewType that associated with default layout Id for the action item.
     *
     * @see #getItemViewType(GuidedAction)
     * @see #onProvideItemLayoutId(int)
     * @see #onCreateViewHolder(ViewGroup, int)
     */
    public static final int VIEW_TYPE_DEFAULT = 0;

    /**
     * ViewType for DatePicker.
     */
    public static final int VIEW_TYPE_DATE_PICKER = 1;

    static final android.support.v17.leanback.widget.ItemAlignmentFacet sGuidedActionItemAlignFacet;

    static {
        sGuidedActionItemAlignFacet = new android.support.v17.leanback.widget.ItemAlignmentFacet();
        android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef alignedDef = new android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef();
        alignedDef.setItemAlignmentViewId(R.id.guidedactions_item_title);
        alignedDef.setAlignedToTextViewBaseline(true);
        alignedDef.setItemAlignmentOffset(0);
        alignedDef.setItemAlignmentOffsetWithPadding(true);
        alignedDef.setItemAlignmentOffsetPercent(0);
        android.support.v17.leanback.widget.GuidedActionsStylist.sGuidedActionItemAlignFacet.setAlignmentDefs(new android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef[]{ alignedDef });
    }

    /**
     * ViewHolder caches information about the action item layouts' subviews. Subclasses of {@link GuidedActionsStylist} may also wish to subclass this in order to add fields.
     *
     * @see GuidedAction
     */
    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements android.support.v17.leanback.widget.FacetProvider {
        android.support.v17.leanback.widget.GuidedAction mAction;

        private android.view.View mContentView;

        android.widget.TextView mTitleView;

        android.widget.TextView mDescriptionView;

        android.view.View mActivatorView;

        android.widget.ImageView mIconView;

        android.widget.ImageView mCheckmarkView;

        android.widget.ImageView mChevronView;

        int mEditingMode = android.support.v17.leanback.widget.GuidedAction.EDITING_NONE;

        private final boolean mIsSubAction;

        final android.view.View.AccessibilityDelegate mDelegate = new android.view.View.AccessibilityDelegate() {
            @java.lang.Override
            public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                event.setChecked((mAction != null) && mAction.isChecked());
            }

            @java.lang.Override
            public void onInitializeAccessibilityNodeInfo(android.view.View host, android.view.accessibility.AccessibilityNodeInfo info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCheckable((mAction != null) && (mAction.getCheckSetId() != android.support.v17.leanback.widget.GuidedAction.NO_CHECK_SET));
                info.setChecked((mAction != null) && mAction.isChecked());
            }
        };

        /**
         * Constructs an ViewHolder and caches the relevant subviews.
         */
        public ViewHolder(android.view.View v) {
            this(v, false);
        }

        /**
         * Constructs an ViewHolder for sub action and caches the relevant subviews.
         */
        public ViewHolder(android.view.View v, boolean isSubAction) {
            super(v);
            mContentView = v.findViewById(R.id.guidedactions_item_content);
            mTitleView = ((android.widget.TextView) (v.findViewById(R.id.guidedactions_item_title)));
            mActivatorView = v.findViewById(R.id.guidedactions_activator_item);
            mDescriptionView = ((android.widget.TextView) (v.findViewById(R.id.guidedactions_item_description)));
            mIconView = ((android.widget.ImageView) (v.findViewById(R.id.guidedactions_item_icon)));
            mCheckmarkView = ((android.widget.ImageView) (v.findViewById(R.id.guidedactions_item_checkmark)));
            mChevronView = ((android.widget.ImageView) (v.findViewById(R.id.guidedactions_item_chevron)));
            mIsSubAction = isSubAction;
            v.setAccessibilityDelegate(mDelegate);
        }

        /**
         * Returns the content view within this view holder's view, where title and description are
         * shown.
         */
        public android.view.View getContentView() {
            return mContentView;
        }

        /**
         * Returns the title view within this view holder's view.
         */
        public android.widget.TextView getTitleView() {
            return mTitleView;
        }

        /**
         * Convenience method to return an editable version of the title, if possible,
         * or null if the title view isn't an EditText.
         */
        public android.widget.EditText getEditableTitleView() {
            return mTitleView instanceof android.widget.EditText ? ((android.widget.EditText) (mTitleView)) : null;
        }

        /**
         * Returns the description view within this view holder's view.
         */
        public android.widget.TextView getDescriptionView() {
            return mDescriptionView;
        }

        /**
         * Convenience method to return an editable version of the description, if possible,
         * or null if the description view isn't an EditText.
         */
        public android.widget.EditText getEditableDescriptionView() {
            return mDescriptionView instanceof android.widget.EditText ? ((android.widget.EditText) (mDescriptionView)) : null;
        }

        /**
         * Returns the icon view within this view holder's view.
         */
        public android.widget.ImageView getIconView() {
            return mIconView;
        }

        /**
         * Returns the checkmark view within this view holder's view.
         */
        public android.widget.ImageView getCheckmarkView() {
            return mCheckmarkView;
        }

        /**
         * Returns the chevron view within this view holder's view.
         */
        public android.widget.ImageView getChevronView() {
            return mChevronView;
        }

        /**
         * Returns true if in editing title, description, or activator View, false otherwise.
         */
        public boolean isInEditing() {
            return mEditingMode != android.support.v17.leanback.widget.GuidedAction.EDITING_NONE;
        }

        /**
         * Returns true if in editing title, description, so IME would be open.
         *
         * @return True if in editing title, description, so IME would be open, false otherwise.
         */
        public boolean isInEditingText() {
            return (mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_TITLE) || (mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_DESCRIPTION);
        }

        /**
         * Returns true if the TextView is in editing title, false otherwise.
         */
        public boolean isInEditingTitle() {
            return mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_TITLE;
        }

        /**
         * Returns true if the TextView is in editing description, false otherwise.
         */
        public boolean isInEditingDescription() {
            return mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_DESCRIPTION;
        }

        /**
         * Returns true if is in editing activator view with id guidedactions_activator_item, false
         * otherwise.
         */
        public boolean isInEditingActivatorView() {
            return mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_ACTIVATOR_VIEW;
        }

        /**
         *
         *
         * @return Current editing title view or description view or activator view or null if not
        in editing.
         */
        public android.view.View getEditingView() {
            switch (mEditingMode) {
                case android.support.v17.leanback.widget.GuidedAction.EDITING_TITLE :
                    return mTitleView;
                case android.support.v17.leanback.widget.GuidedAction.EDITING_DESCRIPTION :
                    return mDescriptionView;
                case android.support.v17.leanback.widget.GuidedAction.EDITING_ACTIVATOR_VIEW :
                    return mActivatorView;
                case android.support.v17.leanback.widget.GuidedAction.EDITING_NONE :
                default :
                    return null;
            }
        }

        /**
         *
         *
         * @return True if bound action is inside {@link GuidedAction#getSubActions()}, false
        otherwise.
         */
        public boolean isSubAction() {
            return mIsSubAction;
        }

        /**
         *
         *
         * @return Currently bound action.
         */
        public android.support.v17.leanback.widget.GuidedAction getAction() {
            return mAction;
        }

        void setActivated(boolean activated) {
            mActivatorView.setActivated(activated);
            if (itemView instanceof android.support.v17.leanback.widget.GuidedActionItemContainer) {
                ((android.support.v17.leanback.widget.GuidedActionItemContainer) (itemView)).setFocusOutAllowed(!activated);
            }
        }

        @java.lang.Override
        public java.lang.Object getFacet(java.lang.Class<?> facetClass) {
            if (facetClass == android.support.v17.leanback.widget.ItemAlignmentFacet.class) {
                return android.support.v17.leanback.widget.GuidedActionsStylist.sGuidedActionItemAlignFacet;
            }
            return null;
        }
    }

    private static java.lang.String TAG = "GuidedActionsStylist";

    android.view.ViewGroup mMainView;

    private android.support.v17.leanback.widget.VerticalGridView mActionsGridView;

    android.support.v17.leanback.widget.VerticalGridView mSubActionsGridView;

    private android.view.View mSubActionsBackground;

    private android.view.View mBgView;

    private android.view.View mContentView;

    private boolean mButtonActions;

    // Cached values from resources
    private float mEnabledTextAlpha;

    private float mDisabledTextAlpha;

    private float mEnabledDescriptionAlpha;

    private float mDisabledDescriptionAlpha;

    private float mEnabledChevronAlpha;

    private float mDisabledChevronAlpha;

    private int mTitleMinLines;

    private int mTitleMaxLines;

    private int mDescriptionMinLines;

    private int mVerticalPadding;

    private int mDisplayHeight;

    private android.support.v17.leanback.widget.GuidedActionAdapter.EditListener mEditListener;

    private android.support.v17.leanback.widget.GuidedAction mExpandedAction = null;

    java.lang.Object mExpandTransition;

    /**
     * Creates a view appropriate for displaying a list of GuidedActions, using the provided
     * inflater and container.
     * <p>
     * <i>Note: Does not actually add the created view to the container; the caller should do
     * this.</i>
     *
     * @param inflater
     * 		The layout inflater to be used when constructing the view.
     * @param container
     * 		The view group to be passed in the call to
     * 		<code>LayoutInflater.inflate</code>.
     * @return The view to be added to the caller's view hierarchy.
     */
    public android.view.View onCreateView(android.view.LayoutInflater inflater, final android.view.ViewGroup container) {
        android.content.res.TypedArray ta = inflater.getContext().getTheme().obtainStyledAttributes(R.styleable.LeanbackGuidedStepTheme);
        float keylinePercent = ta.getFloat(R.styleable.LeanbackGuidedStepTheme_guidedStepKeyline, 40);
        mMainView = ((android.view.ViewGroup) (inflater.inflate(onProvideLayoutId(), container, false)));
        mContentView = mMainView.findViewById(mButtonActions ? R.id.guidedactions_content2 : R.id.guidedactions_content);
        mBgView = mMainView.findViewById(mButtonActions ? R.id.guidedactions_list_background2 : R.id.guidedactions_list_background);
        if (mMainView instanceof android.support.v17.leanback.widget.VerticalGridView) {
            mActionsGridView = ((android.support.v17.leanback.widget.VerticalGridView) (mMainView));
        } else {
            mActionsGridView = ((android.support.v17.leanback.widget.VerticalGridView) (mMainView.findViewById(mButtonActions ? R.id.guidedactions_list2 : R.id.guidedactions_list)));
            if (mActionsGridView == null) {
                throw new java.lang.IllegalStateException("No ListView exists.");
            }
            mActionsGridView.setWindowAlignmentOffsetPercent(keylinePercent);
            mActionsGridView.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_NO_EDGE);
            if (!mButtonActions) {
                mSubActionsGridView = ((android.support.v17.leanback.widget.VerticalGridView) (mMainView.findViewById(R.id.guidedactions_sub_list)));
                mSubActionsBackground = mMainView.findViewById(R.id.guidedactions_sub_list_background);
            }
        }
        mActionsGridView.setFocusable(false);
        mActionsGridView.setFocusableInTouchMode(false);
        // Cache widths, chevron alpha values, max and min text lines, etc
        android.content.Context ctx = mMainView.getContext();
        android.util.TypedValue val = new android.util.TypedValue();
        mEnabledChevronAlpha = getFloat(ctx, val, R.attr.guidedActionEnabledChevronAlpha);
        mDisabledChevronAlpha = getFloat(ctx, val, R.attr.guidedActionDisabledChevronAlpha);
        mTitleMinLines = getInteger(ctx, val, R.attr.guidedActionTitleMinLines);
        mTitleMaxLines = getInteger(ctx, val, R.attr.guidedActionTitleMaxLines);
        mDescriptionMinLines = getInteger(ctx, val, R.attr.guidedActionDescriptionMinLines);
        mVerticalPadding = getDimension(ctx, val, R.attr.guidedActionVerticalPadding);
        mDisplayHeight = ((android.view.WindowManager) (ctx.getSystemService(android.content.Context.WINDOW_SERVICE))).getDefaultDisplay().getHeight();
        mEnabledTextAlpha = java.lang.Float.valueOf(ctx.getResources().getString(R.string.lb_guidedactions_item_unselected_text_alpha));
        mDisabledTextAlpha = java.lang.Float.valueOf(ctx.getResources().getString(R.string.lb_guidedactions_item_disabled_text_alpha));
        mEnabledDescriptionAlpha = java.lang.Float.valueOf(ctx.getResources().getString(R.string.lb_guidedactions_item_unselected_description_text_alpha));
        mDisabledDescriptionAlpha = java.lang.Float.valueOf(ctx.getResources().getString(R.string.lb_guidedactions_item_disabled_description_text_alpha));
        return mMainView;
    }

    /**
     * Choose the layout resource for button actions in {@link #onProvideLayoutId()}.
     */
    public void setAsButtonActions() {
        if (mMainView != null) {
            throw new java.lang.IllegalStateException("setAsButtonActions() must be called before creating " + "views");
        }
        mButtonActions = true;
    }

    /**
     * Returns true if it is button actions list, false for normal actions list.
     *
     * @return True if it is button actions list, false for normal actions list.
     */
    public boolean isButtonActions() {
        return mButtonActions;
    }

    /**
     * Called when destroy the View created by GuidedActionsStylist.
     */
    public void onDestroyView() {
        mExpandedAction = null;
        mExpandTransition = null;
        mActionsGridView = null;
        mSubActionsGridView = null;
        mSubActionsBackground = null;
        mContentView = null;
        mBgView = null;
        mMainView = null;
    }

    /**
     * Returns the VerticalGridView that displays the list of GuidedActions.
     *
     * @return The VerticalGridView for this presenter.
     */
    public android.support.v17.leanback.widget.VerticalGridView getActionsGridView() {
        return mActionsGridView;
    }

    /**
     * Returns the VerticalGridView that displays the sub actions list of an expanded action.
     *
     * @return The VerticalGridView that displays the sub actions list of an expanded action.
     */
    public android.support.v17.leanback.widget.VerticalGridView getSubActionsGridView() {
        return mSubActionsGridView;
    }

    /**
     * Provides the resource ID of the layout defining the host view for the list of guided actions.
     * Subclasses may override to provide their own customized layouts. The base implementation
     * returns {@link android.support.v17.leanback.R.layout#lb_guidedactions} or
     * {@link android.support.v17.leanback.R.layout#lb_guidedbuttonactions} if
     * {@link #isButtonActions()} is true. If overridden, the substituted layout should contain
     * matching IDs for any views that should be managed by the base class; this can be achieved by
     * starting with a copy of the base layout file.
     *
     * @return The resource ID of the layout to be inflated to define the host view for the list of
    GuidedActions.
     */
    public int onProvideLayoutId() {
        return mButtonActions ? R.layout.lb_guidedbuttonactions : R.layout.lb_guidedactions;
    }

    /**
     * Return view type of action, each different type can have differently associated layout Id.
     * Default implementation returns {@link #VIEW_TYPE_DEFAULT}.
     *
     * @param action
     * 		The action object.
     * @return View type that used in {@link #onProvideItemLayoutId(int)}.
     */
    public int getItemViewType(android.support.v17.leanback.widget.GuidedAction action) {
        if (action instanceof android.support.v17.leanback.widget.GuidedDatePickerAction) {
            return android.support.v17.leanback.widget.GuidedActionsStylist.VIEW_TYPE_DATE_PICKER;
        }
        return android.support.v17.leanback.widget.GuidedActionsStylist.VIEW_TYPE_DEFAULT;
    }

    /**
     * Provides the resource ID of the layout defining the view for an individual guided actions.
     * Subclasses may override to provide their own customized layouts. The base implementation
     * returns {@link android.support.v17.leanback.R.layout#lb_guidedactions_item}. If overridden,
     * the substituted layout should contain matching IDs for any views that should be managed by
     * the base class; this can be achieved by starting with a copy of the base layout file. Note
     * that in order for the item to support editing, the title view should both subclass {@link android.widget.EditText} and implement {@link ImeKeyMonitor}; see {@link GuidedActionEditText}.  To support different types of Layouts, override {@link #onProvideItemLayoutId(int)}.
     *
     * @return The resource ID of the layout to be inflated to define the view to display an
    individual GuidedAction.
     */
    public int onProvideItemLayoutId() {
        return R.layout.lb_guidedactions_item;
    }

    /**
     * Provides the resource ID of the layout defining the view for an individual guided actions.
     * Subclasses may override to provide their own customized layouts. The base implementation
     * supports:
     * <li>{@link android.support.v17.leanback.R.layout#lb_guidedactions_item}
     * <li>{{@link android.support.v17.leanback.R.layout#lb_guidedactions_datepicker_item}. If
     * overridden, the substituted layout should contain matching IDs for any views that should be
     * managed by the base class; this can be achieved by starting with a copy of the base layout
     * file. Note that in order for the item to support editing, the title view should both subclass
     * {@link android.widget.EditText} and implement {@link ImeKeyMonitor}; see
     * {@link GuidedActionEditText}.
     *
     * @param viewType
     * 		View type returned by {@link #getItemViewType(GuidedAction)}
     * @return The resource ID of the layout to be inflated to define the view to display an
    individual GuidedAction.
     */
    public int onProvideItemLayoutId(int viewType) {
        if (viewType == android.support.v17.leanback.widget.GuidedActionsStylist.VIEW_TYPE_DEFAULT) {
            return onProvideItemLayoutId();
        } else
            if (viewType == android.support.v17.leanback.widget.GuidedActionsStylist.VIEW_TYPE_DATE_PICKER) {
                return R.layout.lb_guidedactions_datepicker_item;
            } else {
                throw new java.lang.RuntimeException(("ViewType " + viewType) + " not supported in GuidedActionsStylist");
            }

    }

    /**
     * Constructs a {@link ViewHolder} capable of representing {@link GuidedAction}s. Subclasses
     * may choose to return a subclass of ViewHolder.  To support different view types, override
     * {@link #onCreateViewHolder(ViewGroup, int)}
     * <p>
     * <i>Note: Should not actually add the created view to the parent; the caller will do
     * this.</i>
     *
     * @param parent
     * 		The view group to be used as the parent of the new view.
     * @return The view to be added to the caller's view hierarchy.
     */
    public android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
        android.view.View v = inflater.inflate(onProvideItemLayoutId(), parent, false);
        return new android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder(v, parent == mSubActionsGridView);
    }

    /**
     * Constructs a {@link ViewHolder} capable of representing {@link GuidedAction}s. Subclasses
     * may choose to return a subclass of ViewHolder.
     * <p>
     * <i>Note: Should not actually add the created view to the parent; the caller will do
     * this.</i>
     *
     * @param parent
     * 		The view group to be used as the parent of the new view.
     * @param viewType
     * 		The viewType returned by {@link #getItemViewType(GuidedAction)}
     * @return The view to be added to the caller's view hierarchy.
     */
    public android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        if (viewType == android.support.v17.leanback.widget.GuidedActionsStylist.VIEW_TYPE_DEFAULT) {
            return onCreateViewHolder(parent);
        }
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
        android.view.View v = inflater.inflate(onProvideItemLayoutId(viewType), parent, false);
        return new android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder(v, parent == mSubActionsGridView);
    }

    /**
     * Binds a {@link ViewHolder} to a particular {@link GuidedAction}.
     *
     * @param vh
     * 		The view holder to be associated with the given action.
     * @param action
     * 		The guided action to be displayed by the view holder's view.
     * @return The view to be added to the caller's view hierarchy.
     */
    public void onBindViewHolder(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action) {
        vh.mAction = action;
        if (vh.mTitleView != null) {
            vh.mTitleView.setText(action.getTitle());
            vh.mTitleView.setAlpha(action.isEnabled() ? mEnabledTextAlpha : mDisabledTextAlpha);
            vh.mTitleView.setFocusable(false);
            vh.mTitleView.setClickable(false);
            vh.mTitleView.setLongClickable(false);
        }
        if (vh.mDescriptionView != null) {
            vh.mDescriptionView.setText(action.getDescription());
            vh.mDescriptionView.setVisibility(android.text.TextUtils.isEmpty(action.getDescription()) ? android.view.View.GONE : android.view.View.VISIBLE);
            vh.mDescriptionView.setAlpha(action.isEnabled() ? mEnabledDescriptionAlpha : mDisabledDescriptionAlpha);
            vh.mDescriptionView.setFocusable(false);
            vh.mDescriptionView.setClickable(false);
            vh.mDescriptionView.setLongClickable(false);
        }
        // Clients might want the check mark view to be gone entirely, in which case, ignore it.
        if (vh.mCheckmarkView != null) {
            onBindCheckMarkView(vh, action);
        }
        setIcon(vh.mIconView, action);
        if (action.hasMultilineDescription()) {
            if (vh.mTitleView != null) {
                android.support.v17.leanback.widget.GuidedActionsStylist.setMaxLines(vh.mTitleView, mTitleMaxLines);
                if (vh.mDescriptionView != null) {
                    vh.mDescriptionView.setMaxHeight(getDescriptionMaxHeight(vh.itemView.getContext(), vh.mTitleView));
                }
            }
        } else {
            if (vh.mTitleView != null) {
                android.support.v17.leanback.widget.GuidedActionsStylist.setMaxLines(vh.mTitleView, mTitleMinLines);
            }
            if (vh.mDescriptionView != null) {
                android.support.v17.leanback.widget.GuidedActionsStylist.setMaxLines(vh.mDescriptionView, mDescriptionMinLines);
            }
        }
        if (vh.mActivatorView != null) {
            onBindActivatorView(vh, action);
        }
        setEditingMode(vh, action, false);
        if (action.isFocusable()) {
            vh.itemView.setFocusable(true);
            ((android.view.ViewGroup) (vh.itemView)).setDescendantFocusability(android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        } else {
            vh.itemView.setFocusable(false);
            ((android.view.ViewGroup) (vh.itemView)).setDescendantFocusability(android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
        setupImeOptions(vh, action);
        updateChevronAndVisibility(vh);
    }

    private static void setMaxLines(android.widget.TextView view, int maxLines) {
        // setSingleLine must be called before setMaxLines because it resets maximum to
        // Integer.MAX_VALUE.
        if (maxLines == 1) {
            view.setSingleLine(true);
        } else {
            view.setSingleLine(false);
            view.setMaxLines(maxLines);
        }
    }

    /**
     * Called by {@link #onBindViewHolder(ViewHolder, GuidedAction)} to setup IME options.  Default
     * implementation assigns {@link EditorInfo#IME_ACTION_DONE}.  Subclass may override.
     *
     * @param vh
     * 		The view holder to be associated with the given action.
     * @param action
     * 		The guided action to be displayed by the view holder's view.
     */
    protected void setupImeOptions(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action) {
        setupNextImeOptions(vh.getEditableTitleView());
        setupNextImeOptions(vh.getEditableDescriptionView());
    }

    private void setupNextImeOptions(android.widget.EditText edit) {
        if (edit != null) {
            edit.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NEXT);
        }
    }

    public void setEditingMode(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action, boolean editing) {
        if ((editing != vh.isInEditing()) && (!isInExpandTransition())) {
            onEditingModeChange(vh, action, editing);
        }
    }

    protected void onEditingModeChange(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action, boolean editing) {
        action = vh.getAction();
        android.widget.TextView titleView = vh.getTitleView();
        android.widget.TextView descriptionView = vh.getDescriptionView();
        if (editing) {
            java.lang.CharSequence editTitle = action.getEditTitle();
            if ((titleView != null) && (editTitle != null)) {
                titleView.setText(editTitle);
            }
            java.lang.CharSequence editDescription = action.getEditDescription();
            if ((descriptionView != null) && (editDescription != null)) {
                descriptionView.setText(editDescription);
            }
            if (action.isDescriptionEditable()) {
                if (descriptionView != null) {
                    descriptionView.setVisibility(android.view.View.VISIBLE);
                    descriptionView.setInputType(action.getDescriptionEditInputType());
                }
                vh.mEditingMode = android.support.v17.leanback.widget.GuidedAction.EDITING_DESCRIPTION;
            } else
                if (action.isEditable()) {
                    if (titleView != null) {
                        titleView.setInputType(action.getEditInputType());
                    }
                    vh.mEditingMode = android.support.v17.leanback.widget.GuidedAction.EDITING_TITLE;
                } else
                    if (vh.mActivatorView != null) {
                        onEditActivatorView(vh, action, editing);
                        vh.mEditingMode = android.support.v17.leanback.widget.GuidedAction.EDITING_ACTIVATOR_VIEW;
                    }


        } else {
            if (titleView != null) {
                titleView.setText(action.getTitle());
            }
            if (descriptionView != null) {
                descriptionView.setText(action.getDescription());
            }
            if (vh.mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_DESCRIPTION) {
                if (descriptionView != null) {
                    descriptionView.setVisibility(android.text.TextUtils.isEmpty(action.getDescription()) ? android.view.View.GONE : android.view.View.VISIBLE);
                    descriptionView.setInputType(action.getDescriptionInputType());
                }
            } else
                if (vh.mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_TITLE) {
                    if (titleView != null) {
                        titleView.setInputType(action.getInputType());
                    }
                } else
                    if (vh.mEditingMode == android.support.v17.leanback.widget.GuidedAction.EDITING_ACTIVATOR_VIEW) {
                        if (vh.mActivatorView != null) {
                            onEditActivatorView(vh, action, editing);
                        }
                    }


            vh.mEditingMode = android.support.v17.leanback.widget.GuidedAction.EDITING_NONE;
        }
    }

    /**
     * Animates the view holder's view (or subviews thereof) when the action has had its focus
     * state changed.
     *
     * @param vh
     * 		The view holder associated with the relevant action.
     * @param focused
     * 		True if the action has become focused, false if it has lost focus.
     */
    public void onAnimateItemFocused(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, boolean focused) {
        // No animations for this, currently, because the animation is done on
        // mSelectorView
    }

    /**
     * Animates the view holder's view (or subviews thereof) when the action has had its press
     * state changed.
     *
     * @param vh
     * 		The view holder associated with the relevant action.
     * @param pressed
     * 		True if the action has been pressed, false if it has been unpressed.
     */
    public void onAnimateItemPressed(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, boolean pressed) {
        int attr = (pressed) ? R.attr.guidedActionPressedAnimation : R.attr.guidedActionUnpressedAnimation;
        android.support.v17.leanback.widget.GuidedActionsStylist.createAnimator(vh.itemView, attr).start();
    }

    /**
     * Resets the view holder's view to unpressed state.
     *
     * @param vh
     * 		The view holder associated with the relevant action.
     */
    public void onAnimateItemPressedCancelled(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh) {
        android.support.v17.leanback.widget.GuidedActionsStylist.createAnimator(vh.itemView, R.attr.guidedActionUnpressedAnimation).end();
    }

    /**
     * Animates the view holder's view (or subviews thereof) when the action has had its check state
     * changed. Default implementation calls setChecked() if {@link ViewHolder#getCheckmarkView()}
     * is instance of {@link Checkable}.
     *
     * @param vh
     * 		The view holder associated with the relevant action.
     * @param checked
     * 		True if the action has become checked, false if it has become unchecked.
     * @see #onBindCheckMarkView(ViewHolder, GuidedAction)
     */
    public void onAnimateItemChecked(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, boolean checked) {
        if (vh.mCheckmarkView instanceof android.widget.Checkable) {
            ((android.widget.Checkable) (vh.mCheckmarkView)).setChecked(checked);
        }
    }

    /**
     * Sets states of check mark view, called by {@link #onBindViewHolder(ViewHolder, GuidedAction)}
     * when action's checkset Id is other than {@link GuidedAction#NO_CHECK_SET}. Default
     * implementation assigns drawable loaded from theme attribute
     * {@link android.R.attr#listChoiceIndicatorMultiple} for checkbox or
     * {@link android.R.attr#listChoiceIndicatorSingle} for radio button. Subclass rarely needs
     * override the method, instead app can provide its own drawable that supports transition
     * animations, change theme attributes {@link android.R.attr#listChoiceIndicatorMultiple} and
     * {@link android.R.attr#listChoiceIndicatorSingle} in {android.support.v17.leanback.R.
     * styleable#LeanbackGuidedStepTheme}.
     *
     * @param vh
     * 		The view holder associated with the relevant action.
     * @param action
     * 		The GuidedAction object to bind to.
     * @see #onAnimateItemChecked(ViewHolder, boolean)
     */
    public void onBindCheckMarkView(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action) {
        if (action.getCheckSetId() != android.support.v17.leanback.widget.GuidedAction.NO_CHECK_SET) {
            vh.mCheckmarkView.setVisibility(android.view.View.VISIBLE);
            int attrId = (action.getCheckSetId() == android.support.v17.leanback.widget.GuidedAction.CHECKBOX_CHECK_SET_ID) ? android.R.attr.listChoiceIndicatorMultiple : android.R.attr.listChoiceIndicatorSingle;
            final android.content.Context context = vh.mCheckmarkView.getContext();
            android.graphics.drawable.Drawable drawable = null;
            android.util.TypedValue typedValue = new android.util.TypedValue();
            if (context.getTheme().resolveAttribute(attrId, typedValue, true)) {
                drawable = android.support.v4.content.ContextCompat.getDrawable(context, typedValue.resourceId);
            }
            vh.mCheckmarkView.setImageDrawable(drawable);
            if (vh.mCheckmarkView instanceof android.widget.Checkable) {
                ((android.widget.Checkable) (vh.mCheckmarkView)).setChecked(action.isChecked());
            }
        } else {
            vh.mCheckmarkView.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Performs binding activator view value to action.  Default implementation supports
     * GuidedDatePickerAction, subclass may override to add support of other views.
     *
     * @param vh
     * 		ViewHolder of activator view.
     * @param action
     * 		GuidedAction to bind.
     */
    public void onBindActivatorView(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action) {
        if (action instanceof android.support.v17.leanback.widget.GuidedDatePickerAction) {
            android.support.v17.leanback.widget.GuidedDatePickerAction dateAction = ((android.support.v17.leanback.widget.GuidedDatePickerAction) (action));
            android.support.v17.leanback.widget.picker.DatePicker dateView = ((android.support.v17.leanback.widget.picker.DatePicker) (vh.mActivatorView));
            dateView.setDatePickerFormat(dateAction.getDatePickerFormat());
            if (dateAction.getMinDate() != java.lang.Long.MIN_VALUE) {
                dateView.setMinDate(dateAction.getMinDate());
            }
            if (dateAction.getMaxDate() != java.lang.Long.MAX_VALUE) {
                dateView.setMaxDate(dateAction.getMaxDate());
            }
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTimeInMillis(dateAction.getDate());
            dateView.updateDate(c.get(java.util.Calendar.YEAR), c.get(java.util.Calendar.MONTH), c.get(java.util.Calendar.DAY_OF_MONTH), false);
        }
    }

    /**
     * Performs updating GuidedAction from activator view.  Default implementation supports
     * GuidedDatePickerAction, subclass may override to add support of other views.
     *
     * @param vh
     * 		ViewHolder of activator view.
     * @param action
     * 		GuidedAction to update.
     * @return True if value has been updated, false otherwise.
     */
    public boolean onUpdateActivatorView(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action) {
        if (action instanceof android.support.v17.leanback.widget.GuidedDatePickerAction) {
            android.support.v17.leanback.widget.GuidedDatePickerAction dateAction = ((android.support.v17.leanback.widget.GuidedDatePickerAction) (action));
            android.support.v17.leanback.widget.picker.DatePicker dateView = ((android.support.v17.leanback.widget.picker.DatePicker) (vh.mActivatorView));
            if (dateAction.getDate() != dateView.getDate()) {
                dateAction.setDate(dateView.getDate());
                return true;
            }
        }
        return false;
    }

    /**
     * Sets listener for reporting view being edited.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setEditListener(android.support.v17.leanback.widget.GuidedActionAdapter.EditListener listener) {
        mEditListener = listener;
    }

    void onEditActivatorView(final android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, final android.support.v17.leanback.widget.GuidedAction action, boolean editing) {
        if (editing) {
            vh.itemView.setFocusable(false);
            vh.mActivatorView.requestFocus();
            setExpandedViewHolder(vh);
            vh.mActivatorView.setOnClickListener(new android.view.View.OnClickListener() {
                @java.lang.Override
                public void onClick(android.view.View v) {
                    if (!isInExpandTransition()) {
                        setEditingMode(vh, action, false);
                    }
                }
            });
        } else {
            if (onUpdateActivatorView(vh, action)) {
                if (mEditListener != null) {
                    mEditListener.onGuidedActionEditedAndProceed(action);
                }
            }
            vh.itemView.setFocusable(true);
            vh.itemView.requestFocus();
            setExpandedViewHolder(null);
            vh.mActivatorView.setOnClickListener(null);
            vh.mActivatorView.setClickable(false);
        }
    }

    /**
     * Sets states of chevron view, called by {@link #onBindViewHolder(ViewHolder, GuidedAction)}.
     * Subclass may override.
     *
     * @param vh
     * 		The view holder associated with the relevant action.
     * @param action
     * 		The GuidedAction object to bind to.
     */
    public void onBindChevronView(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh, android.support.v17.leanback.widget.GuidedAction action) {
        final boolean hasNext = action.hasNext();
        final boolean hasSubActions = action.hasSubActions();
        if (hasNext || hasSubActions) {
            vh.mChevronView.setVisibility(android.view.View.VISIBLE);
            vh.mChevronView.setAlpha(action.isEnabled() ? mEnabledChevronAlpha : mDisabledChevronAlpha);
            if (hasNext) {
                float r = ((mMainView != null) && (mMainView.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL)) ? 180.0F : 0.0F;
                vh.mChevronView.setRotation(r);
            } else
                if (action == mExpandedAction) {
                    vh.mChevronView.setRotation(270);
                } else {
                    vh.mChevronView.setRotation(90);
                }

        } else {
            vh.mChevronView.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Expands or collapse the sub actions list view.
     *
     * @param avh
     * 		When not null, fill sub actions list of this ViewHolder into sub actions list and
     * 		hide the other items in main list.  When null, collapse the sub actions list.
     */
    public void setExpandedViewHolder(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh) {
        if (isInExpandTransition()) {
            return;
        }
        if (isExpandTransitionSupported()) {
            startExpandedTransition(avh);
        } else {
            onUpdateExpandedViewHolder(avh);
        }
    }

    /**
     * Returns true if it is running an expanding or collapsing transition, false otherwise.
     *
     * @return True if it is running an expanding or collapsing transition, false otherwise.
     */
    public boolean isInExpandTransition() {
        return mExpandTransition != null;
    }

    /**
     * Returns if expand/collapse animation is supported.  When this method returns true,
     * {@link #startExpandedTransition(ViewHolder)} will be used.  When this method returns false,
     * {@link #onUpdateExpandedViewHolder(ViewHolder)} will be called.
     *
     * @return True if it is running an expanding or collapsing transition, false otherwise.
     */
    public boolean isExpandTransitionSupported() {
        return android.os.Build.VERSION.SDK_INT >= 21;
    }

    /**
     * Start transition to expand or collapse GuidedActionStylist.
     *
     * @param avh
     * 		When not null, the GuidedActionStylist expands the sub actions of avh.  When null
     * 		the GuidedActionStylist will collapse sub actions.
     */
    public void startExpandedTransition(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh) {
        android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder focusAvh = null;// expand / collapse view holder

        final int count = mActionsGridView.getChildCount();
        for (int i = 0; i < count; i++) {
            android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (mActionsGridView.getChildViewHolder(mActionsGridView.getChildAt(i))));
            if ((avh == null) && (vh.itemView.getVisibility() == android.view.View.VISIBLE)) {
                // going to collapse this one.
                focusAvh = vh;
                break;
            } else
                if ((avh != null) && (vh.getAction() == avh.getAction())) {
                    // going to expand this one.
                    focusAvh = vh;
                    break;
                }

        }
        if (focusAvh == null) {
            // huh?
            onUpdateExpandedViewHolder(avh);
            return;
        }
        boolean isSubActionTransition = focusAvh.getAction().hasSubActions();
        java.lang.Object set = android.support.v17.leanback.transition.TransitionHelper.createTransitionSet(false);
        float slideDistance = (isSubActionTransition) ? focusAvh.itemView.getHeight() : focusAvh.itemView.getHeight() * 0.5F;
        java.lang.Object slideAndFade = android.support.v17.leanback.transition.TransitionHelper.createFadeAndShortSlide(android.view.Gravity.TOP | android.view.Gravity.BOTTOM, slideDistance);
        java.lang.Object changeFocusItemTransform = android.support.v17.leanback.transition.TransitionHelper.createChangeTransform();
        java.lang.Object changeFocusItemBounds = android.support.v17.leanback.transition.TransitionHelper.createChangeBounds(false);
        java.lang.Object fade = android.support.v17.leanback.transition.TransitionHelper.createFadeTransition(android.support.v17.leanback.transition.TransitionHelper.FADE_IN | android.support.v17.leanback.transition.TransitionHelper.FADE_OUT);
        java.lang.Object changeGridBounds = android.support.v17.leanback.transition.TransitionHelper.createChangeBounds(false);
        if (avh == null) {
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(slideAndFade, 150);
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(changeFocusItemTransform, 100);
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(changeFocusItemBounds, 100);
        } else {
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(fade, 100);
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(changeGridBounds, 100);
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(changeFocusItemTransform, 50);
            android.support.v17.leanback.transition.TransitionHelper.setStartDelay(changeFocusItemBounds, 50);
        }
        for (int i = 0; i < count; i++) {
            android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (mActionsGridView.getChildViewHolder(mActionsGridView.getChildAt(i))));
            if (vh == focusAvh) {
                // going to expand/collapse this one.
                if (isSubActionTransition) {
                    android.support.v17.leanback.transition.TransitionHelper.include(changeFocusItemTransform, vh.itemView);
                    android.support.v17.leanback.transition.TransitionHelper.include(changeFocusItemBounds, vh.itemView);
                }
            } else {
                // going to slide this item to top / bottom.
                android.support.v17.leanback.transition.TransitionHelper.include(slideAndFade, vh.itemView);
                android.support.v17.leanback.transition.TransitionHelper.exclude(fade, vh.itemView, true);
            }
        }
        android.support.v17.leanback.transition.TransitionHelper.include(changeGridBounds, mSubActionsGridView);
        android.support.v17.leanback.transition.TransitionHelper.include(changeGridBounds, mSubActionsBackground);
        android.support.v17.leanback.transition.TransitionHelper.addTransition(set, slideAndFade);
        // note that we don't run ChangeBounds for activating view due to the rounding problem
        // of multiple level views ChangeBounds animation causing vertical jittering.
        if (isSubActionTransition) {
            android.support.v17.leanback.transition.TransitionHelper.addTransition(set, changeFocusItemTransform);
            android.support.v17.leanback.transition.TransitionHelper.addTransition(set, changeFocusItemBounds);
        }
        android.support.v17.leanback.transition.TransitionHelper.addTransition(set, fade);
        android.support.v17.leanback.transition.TransitionHelper.addTransition(set, changeGridBounds);
        mExpandTransition = set;
        android.support.v17.leanback.transition.TransitionHelper.addTransitionListener(mExpandTransition, new android.support.v17.leanback.transition.TransitionListener() {
            @java.lang.Override
            public void onTransitionEnd(java.lang.Object transition) {
                mExpandTransition = null;
            }
        });
        if ((avh != null) && (mSubActionsGridView.getTop() != avh.itemView.getTop())) {
            // For expanding, set the initial position of subActionsGridView before running
            // a ChangeBounds on it.
            final android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder toUpdate = avh;
            mSubActionsGridView.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() {
                @java.lang.Override
                public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (mSubActionsGridView == null) {
                        return;
                    }
                    mSubActionsGridView.removeOnLayoutChangeListener(this);
                    mMainView.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            if (mMainView == null) {
                                return;
                            }
                            android.support.v17.leanback.transition.TransitionHelper.beginDelayedTransition(mMainView, mExpandTransition);
                            onUpdateExpandedViewHolder(toUpdate);
                        }
                    });
                }
            });
            android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (mSubActionsGridView.getLayoutParams()));
            lp.topMargin = avh.itemView.getTop();
            lp.height = 0;
            mSubActionsGridView.setLayoutParams(lp);
            return;
        }
        android.support.v17.leanback.transition.TransitionHelper.beginDelayedTransition(mMainView, mExpandTransition);
        onUpdateExpandedViewHolder(avh);
    }

    /**
     *
     *
     * @return True if sub actions list is expanded.
     */
    public boolean isSubActionsExpanded() {
        return mExpandedAction != null;
    }

    /**
     *
     *
     * @return Current expanded GuidedAction or null if not expanded.
     */
    public android.support.v17.leanback.widget.GuidedAction getExpandedAction() {
        return mExpandedAction;
    }

    /**
     * Expand or collapse GuidedActionStylist.
     *
     * @param avh
     * 		When not null, the GuidedActionStylist expands the sub actions of avh.  When null
     * 		the GuidedActionStylist will collapse sub actions.
     */
    public void onUpdateExpandedViewHolder(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh) {
        // Note about setting the prune child flag back & forth here: without this, the actions that
        // go off the screen from the top or bottom become invisible forever. This is because once
        // an action is expanded, it takes more space which in turn kicks out some other actions
        // off of the screen. Once, this action is collapsed (after the second click) and the
        // visibility flag is set back to true for all existing actions,
        // the off-the-screen actions are pruned from the view, thus
        // could not be accessed, had we not disabled pruning prior to this.
        if (avh == null) {
            mExpandedAction = null;
            mActionsGridView.setPruneChild(true);
        } else
            if (avh.getAction() != mExpandedAction) {
                mExpandedAction = avh.getAction();
                mActionsGridView.setPruneChild(false);
            }

        // In expanding mode, notifyItemChange on expanded item will reset the translationY by
        // the default ItemAnimator.  So disable ItemAnimation in expanding mode.
        mActionsGridView.setAnimateChildLayout(false);
        final int count = mActionsGridView.getChildCount();
        for (int i = 0; i < count; i++) {
            android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (mActionsGridView.getChildViewHolder(mActionsGridView.getChildAt(i))));
            updateChevronAndVisibility(vh);
        }
        if (mSubActionsGridView != null) {
            if ((avh != null) && avh.getAction().hasSubActions()) {
                android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (mSubActionsGridView.getLayoutParams()));
                lp.topMargin = avh.itemView.getTop();
                lp.height = android.view.ViewGroup.MarginLayoutParams.MATCH_PARENT;
                mSubActionsGridView.setLayoutParams(lp);
                mSubActionsGridView.setVisibility(android.view.View.VISIBLE);
                mSubActionsBackground.setVisibility(android.view.View.VISIBLE);
                mSubActionsGridView.requestFocus();
                mSubActionsGridView.setSelectedPosition(0);
                ((android.support.v17.leanback.widget.GuidedActionAdapter) (mSubActionsGridView.getAdapter())).setActions(avh.getAction().getSubActions());
            } else
                if (mSubActionsGridView.getVisibility() == android.view.View.VISIBLE) {
                    mSubActionsGridView.setVisibility(android.view.View.INVISIBLE);
                    mSubActionsBackground.setVisibility(android.view.View.INVISIBLE);
                    android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (mSubActionsGridView.getLayoutParams()));
                    lp.height = 0;
                    mSubActionsGridView.setLayoutParams(lp);
                    ((android.support.v17.leanback.widget.GuidedActionAdapter) (mSubActionsGridView.getAdapter())).setActions(java.util.Collections.EMPTY_LIST);
                    mActionsGridView.requestFocus();
                }

        }
    }

    private void updateChevronAndVisibility(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh) {
        if (!vh.isSubAction()) {
            if (mExpandedAction == null) {
                vh.itemView.setVisibility(android.view.View.VISIBLE);
                vh.itemView.setTranslationY(0);
                if (vh.mActivatorView != null) {
                    vh.setActivated(false);
                }
            } else
                if (vh.getAction() == mExpandedAction) {
                    vh.itemView.setVisibility(android.view.View.VISIBLE);
                    if (vh.getAction().hasSubActions()) {
                        vh.itemView.setTranslationY(-vh.itemView.getHeight());
                    } else
                        if (vh.mActivatorView != null) {
                            vh.itemView.setTranslationY(0);
                            vh.setActivated(true);
                        }

                } else {
                    vh.itemView.setVisibility(android.view.View.INVISIBLE);
                    vh.itemView.setTranslationY(0);
                }

        }
        if (vh.mChevronView != null) {
            onBindChevronView(vh, vh.getAction());
        }
    }

    /* ==========================================
    FragmentAnimationProvider overrides
    ==========================================
     */
    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void onImeAppearing(@android.support.annotation.NonNull
    java.util.List<android.animation.Animator> animators) {
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void onImeDisappearing(@android.support.annotation.NonNull
    java.util.List<android.animation.Animator> animators) {
    }

    /* ==========================================
    Private methods
    ==========================================
     */
    private float getFloat(android.content.Context ctx, android.util.TypedValue typedValue, int attrId) {
        ctx.getTheme().resolveAttribute(attrId, typedValue, true);
        // Android resources don't have a native float type, so we have to use strings.
        return java.lang.Float.valueOf(ctx.getResources().getString(typedValue.resourceId));
    }

    private int getInteger(android.content.Context ctx, android.util.TypedValue typedValue, int attrId) {
        ctx.getTheme().resolveAttribute(attrId, typedValue, true);
        return ctx.getResources().getInteger(typedValue.resourceId);
    }

    private int getDimension(android.content.Context ctx, android.util.TypedValue typedValue, int attrId) {
        ctx.getTheme().resolveAttribute(attrId, typedValue, true);
        return ctx.getResources().getDimensionPixelSize(typedValue.resourceId);
    }

    private static android.animation.Animator createAnimator(android.view.View v, int attrId) {
        android.content.Context ctx = v.getContext();
        android.util.TypedValue typedValue = new android.util.TypedValue();
        ctx.getTheme().resolveAttribute(attrId, typedValue, true);
        android.animation.Animator animator = android.animation.AnimatorInflater.loadAnimator(ctx, typedValue.resourceId);
        animator.setTarget(v);
        return animator;
    }

    private boolean setIcon(final android.widget.ImageView iconView, android.support.v17.leanback.widget.GuidedAction action) {
        android.graphics.drawable.Drawable icon = null;
        if (iconView != null) {
            android.content.Context context = iconView.getContext();
            icon = action.getIcon();
            if (icon != null) {
                // setImageDrawable resets the drawable's level unless we set the view level first.
                iconView.setImageLevel(icon.getLevel());
                iconView.setImageDrawable(icon);
                iconView.setVisibility(android.view.View.VISIBLE);
            } else {
                iconView.setVisibility(android.view.View.GONE);
            }
        }
        return icon != null;
    }

    /**
     *
     *
     * @return the max height in pixels the description can be such that the
    action nicely takes up the entire screen.
     */
    private int getDescriptionMaxHeight(android.content.Context context, android.widget.TextView title) {
        // The 2 multiplier on the title height calculation is a
        // conservative estimate for font padding which can not be
        // calculated at this stage since the view hasn't been rendered yet.
        return ((int) ((mDisplayHeight - (2 * mVerticalPadding)) - ((2 * mTitleMaxLines) * title.getLineHeight())));
    }
}

