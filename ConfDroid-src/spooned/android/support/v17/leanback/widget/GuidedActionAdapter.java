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
 * GuidedActionAdapter instantiates views for guided actions, and manages their interactions.
 * Presentation (view creation and state animation) is delegated to a {@link GuidedActionsStylist}, while clients are notified of interactions via
 * {@link GuidedActionAdapter.ClickListener} and {@link GuidedActionAdapter.FocusListener}.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class GuidedActionAdapter extends android.support.v7.widget.RecyclerView.Adapter {
    static final java.lang.String TAG = "GuidedActionAdapter";

    static final boolean DEBUG = false;

    static final java.lang.String TAG_EDIT = "EditableAction";

    static final boolean DEBUG_EDIT = false;

    /**
     * Object listening for click events within a {@link GuidedActionAdapter}.
     */
    public interface ClickListener {
        /**
         * Called when the user clicks on an action.
         */
        public void onGuidedActionClicked(android.support.v17.leanback.widget.GuidedAction action);
    }

    /**
     * Object listening for focus events within a {@link GuidedActionAdapter}.
     */
    public interface FocusListener {
        /**
         * Called when the user focuses on an action.
         */
        public void onGuidedActionFocused(android.support.v17.leanback.widget.GuidedAction action);
    }

    /**
     * Object listening for edit events within a {@link GuidedActionAdapter}.
     */
    public interface EditListener {
        /**
         * Called when the user exits edit mode on an action.
         */
        public void onGuidedActionEditCanceled(android.support.v17.leanback.widget.GuidedAction action);

        /**
         * Called when the user exits edit mode on an action and process confirm button in IME.
         */
        public long onGuidedActionEditedAndProceed(android.support.v17.leanback.widget.GuidedAction action);

        /**
         * Called when Ime Open
         */
        public void onImeOpen();

        /**
         * Called when Ime Close
         */
        public void onImeClose();
    }

    private final boolean mIsSubAdapter;

    private final android.support.v17.leanback.widget.GuidedActionAdapter.ActionOnKeyListener mActionOnKeyListener;

    private final android.support.v17.leanback.widget.GuidedActionAdapter.ActionOnFocusListener mActionOnFocusListener;

    private final android.support.v17.leanback.widget.GuidedActionAdapter.ActionEditListener mActionEditListener;

    private final java.util.List<android.support.v17.leanback.widget.GuidedAction> mActions;

    private android.support.v17.leanback.widget.GuidedActionAdapter.ClickListener mClickListener;

    final android.support.v17.leanback.widget.GuidedActionsStylist mStylist;

    private final android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            if (((v != null) && (v.getWindowToken() != null)) && (getRecyclerView() != null)) {
                android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (getRecyclerView().getChildViewHolder(v)));
                android.support.v17.leanback.widget.GuidedAction action = avh.getAction();
                if (action.hasTextEditable()) {
                    if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG_EDIT)
                        android.util.Log.v(android.support.v17.leanback.widget.GuidedActionAdapter.TAG_EDIT, "openIme by click");

                    mGroup.openIme(android.support.v17.leanback.widget.GuidedActionAdapter.this, avh);
                } else
                    if (action.hasEditableActivatorView()) {
                        if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG_EDIT)
                            android.util.Log.v(android.support.v17.leanback.widget.GuidedActionAdapter.TAG_EDIT, "toggle editing mode by click");

                        getGuidedActionsStylist().setEditingMode(avh, avh.getAction(), !avh.isInEditingActivatorView());
                    } else {
                        handleCheckedActions(avh);
                        if (action.isEnabled() && (!action.infoOnly())) {
                            performOnActionClick(avh);
                        }
                    }

            }
        }
    };

    android.support.v17.leanback.widget.GuidedActionAdapterGroup mGroup;

    /**
     * Constructs a GuidedActionAdapter with the given list of guided actions, the given click and
     * focus listeners, and the given presenter.
     *
     * @param actions
     * 		The list of guided actions this adapter will manage.
     * @param focusListener
     * 		The focus listener for items in this adapter.
     * @param presenter
     * 		The presenter that will manage the display of items in this adapter.
     */
    public GuidedActionAdapter(java.util.List<android.support.v17.leanback.widget.GuidedAction> actions, android.support.v17.leanback.widget.GuidedActionAdapter.ClickListener clickListener, android.support.v17.leanback.widget.GuidedActionAdapter.FocusListener focusListener, android.support.v17.leanback.widget.GuidedActionsStylist presenter, boolean isSubAdapter) {
        super();
        mActions = (actions == null) ? new java.util.ArrayList<android.support.v17.leanback.widget.GuidedAction>() : new java.util.ArrayList<android.support.v17.leanback.widget.GuidedAction>(actions);
        mClickListener = clickListener;
        mStylist = presenter;
        mActionOnKeyListener = new android.support.v17.leanback.widget.GuidedActionAdapter.ActionOnKeyListener();
        mActionOnFocusListener = new android.support.v17.leanback.widget.GuidedActionAdapter.ActionOnFocusListener(focusListener);
        mActionEditListener = new android.support.v17.leanback.widget.GuidedActionAdapter.ActionEditListener();
        mIsSubAdapter = isSubAdapter;
    }

    /**
     * Sets the list of actions managed by this adapter.
     *
     * @param actions
     * 		The list of actions to be managed.
     */
    public void setActions(java.util.List<android.support.v17.leanback.widget.GuidedAction> actions) {
        mActionOnFocusListener.unFocus();
        mActions.clear();
        mActions.addAll(actions);
        notifyDataSetChanged();
    }

    /**
     * Returns the count of actions managed by this adapter.
     *
     * @return The count of actions managed by this adapter.
     */
    public int getCount() {
        return mActions.size();
    }

    /**
     * Returns the GuidedAction at the given position in the managed list.
     *
     * @param position
     * 		The position of the desired GuidedAction.
     * @return The GuidedAction at the given position.
     */
    public android.support.v17.leanback.widget.GuidedAction getItem(int position) {
        return mActions.get(position);
    }

    /**
     * Return index of action in array
     *
     * @param action
     * 		Action to search index.
     * @return Index of Action in array.
     */
    public int indexOf(android.support.v17.leanback.widget.GuidedAction action) {
        return mActions.indexOf(action);
    }

    /**
     *
     *
     * @return GuidedActionsStylist used to build the actions list UI.
     */
    public android.support.v17.leanback.widget.GuidedActionsStylist getGuidedActionsStylist() {
        return mStylist;
    }

    /**
     * Sets the click listener for items managed by this adapter.
     *
     * @param clickListener
     * 		The click listener for this adapter.
     */
    public void setClickListener(android.support.v17.leanback.widget.GuidedActionAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    /**
     * Sets the focus listener for items managed by this adapter.
     *
     * @param focusListener
     * 		The focus listener for this adapter.
     */
    public void setFocusListener(android.support.v17.leanback.widget.GuidedActionAdapter.FocusListener focusListener) {
        mActionOnFocusListener.setFocusListener(focusListener);
    }

    /**
     * Used for serialization only.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public java.util.List<android.support.v17.leanback.widget.GuidedAction> getActions() {
        return new java.util.ArrayList<android.support.v17.leanback.widget.GuidedAction>(mActions);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int getItemViewType(int position) {
        return mStylist.getItemViewType(mActions.get(position));
    }

    android.support.v7.widget.RecyclerView getRecyclerView() {
        return mIsSubAdapter ? mStylist.getSubActionsGridView() : mStylist.getActionsGridView();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh = mStylist.onCreateViewHolder(parent, viewType);
        android.view.View v = vh.itemView;
        v.setOnKeyListener(mActionOnKeyListener);
        v.setOnClickListener(mOnClickListener);
        v.setOnFocusChangeListener(mActionOnFocusListener);
        setupListeners(vh.getEditableTitleView());
        setupListeners(vh.getEditableDescriptionView());
        return vh;
    }

    private void setupListeners(android.widget.EditText edit) {
        if (edit != null) {
            edit.setPrivateImeOptions("EscapeNorth=1;");
            edit.setOnEditorActionListener(mActionEditListener);
            if (edit instanceof android.support.v17.leanback.widget.ImeKeyMonitor) {
                android.support.v17.leanback.widget.ImeKeyMonitor monitor = ((android.support.v17.leanback.widget.ImeKeyMonitor) (edit));
                monitor.setImeKeyListener(mActionEditListener);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
        if (position >= mActions.size()) {
            return;
        }
        final android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (holder));
        android.support.v17.leanback.widget.GuidedAction action = mActions.get(position);
        mStylist.onBindViewHolder(avh, action);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int getItemCount() {
        return mActions.size();
    }

    private class ActionOnFocusListener implements android.view.View.OnFocusChangeListener {
        private android.support.v17.leanback.widget.GuidedActionAdapter.FocusListener mFocusListener;

        private android.view.View mSelectedView;

        ActionOnFocusListener(android.support.v17.leanback.widget.GuidedActionAdapter.FocusListener focusListener) {
            mFocusListener = focusListener;
        }

        public void setFocusListener(android.support.v17.leanback.widget.GuidedActionAdapter.FocusListener focusListener) {
            mFocusListener = focusListener;
        }

        public void unFocus() {
            if ((mSelectedView != null) && (getRecyclerView() != null)) {
                android.support.v7.widget.RecyclerView.ViewHolder vh = getRecyclerView().getChildViewHolder(mSelectedView);
                if (vh != null) {
                    android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (vh));
                    mStylist.onAnimateItemFocused(avh, false);
                } else {
                    android.util.Log.w(android.support.v17.leanback.widget.GuidedActionAdapter.TAG, "RecyclerView returned null view holder", new java.lang.Throwable());
                }
            }
        }

        @java.lang.Override
        public void onFocusChange(android.view.View v, boolean hasFocus) {
            if (getRecyclerView() == null) {
                return;
            }
            android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (getRecyclerView().getChildViewHolder(v)));
            if (hasFocus) {
                mSelectedView = v;
                if (mFocusListener != null) {
                    // We still call onGuidedActionFocused so that listeners can clear
                    // state if they want.
                    mFocusListener.onGuidedActionFocused(avh.getAction());
                }
            } else {
                if (mSelectedView == v) {
                    mStylist.onAnimateItemPressedCancelled(avh);
                    mSelectedView = null;
                }
            }
            mStylist.onAnimateItemFocused(avh, hasFocus);
        }
    }

    public android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder findSubChildViewHolder(android.view.View v) {
        // Needed because RecyclerView.getChildViewHolder does not traverse the hierarchy
        if (getRecyclerView() == null) {
            return null;
        }
        android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder result = null;
        android.view.ViewParent parent = v.getParent();
        while (((parent != getRecyclerView()) && (parent != null)) && (v != null)) {
            v = ((android.view.View) (parent));
            parent = parent.getParent();
        } 
        if ((parent != null) && (v != null)) {
            result = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (getRecyclerView().getChildViewHolder(v)));
        }
        return result;
    }

    public void handleCheckedActions(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh) {
        android.support.v17.leanback.widget.GuidedAction action = avh.getAction();
        int actionCheckSetId = action.getCheckSetId();
        if ((getRecyclerView() != null) && (actionCheckSetId != android.support.v17.leanback.widget.GuidedAction.NO_CHECK_SET)) {
            // Find any actions that are checked and are in the same group
            // as the selected action. Fade their checkmarks out.
            if (actionCheckSetId != android.support.v17.leanback.widget.GuidedAction.CHECKBOX_CHECK_SET_ID) {
                for (int i = 0, size = mActions.size(); i < size; i++) {
                    android.support.v17.leanback.widget.GuidedAction a = mActions.get(i);
                    if (((a != action) && (a.getCheckSetId() == actionCheckSetId)) && a.isChecked()) {
                        a.setChecked(false);
                        android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder vh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (getRecyclerView().findViewHolderForPosition(i)));
                        if (vh != null) {
                            mStylist.onAnimateItemChecked(vh, false);
                        }
                    }
                }
            }
            // If we we'ren't already checked, fade our checkmark in.
            if (!action.isChecked()) {
                action.setChecked(true);
                mStylist.onAnimateItemChecked(avh, true);
            } else {
                if (actionCheckSetId == android.support.v17.leanback.widget.GuidedAction.CHECKBOX_CHECK_SET_ID) {
                    action.setChecked(false);
                    mStylist.onAnimateItemChecked(avh, false);
                }
            }
        }
    }

    public void performOnActionClick(android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh) {
        if (mClickListener != null) {
            mClickListener.onGuidedActionClicked(avh.getAction());
        }
    }

    private class ActionOnKeyListener implements android.view.View.OnKeyListener {
        private boolean mKeyPressed = false;

        ActionOnKeyListener() {
        }

        /**
         * Now only handles KEYCODE_ENTER and KEYCODE_NUMPAD_ENTER key event.
         */
        @java.lang.Override
        public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
            if (((v == null) || (event == null)) || (getRecyclerView() == null)) {
                return false;
            }
            boolean handled = false;
            switch (keyCode) {
                case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                case android.view.KeyEvent.KEYCODE_NUMPAD_ENTER :
                case android.view.KeyEvent.KEYCODE_BUTTON_X :
                case android.view.KeyEvent.KEYCODE_BUTTON_Y :
                case android.view.KeyEvent.KEYCODE_ENTER :
                    android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder avh = ((android.support.v17.leanback.widget.GuidedActionsStylist.ViewHolder) (getRecyclerView().getChildViewHolder(v)));
                    android.support.v17.leanback.widget.GuidedAction action = avh.getAction();
                    if ((!action.isEnabled()) || action.infoOnly()) {
                        if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                            // TODO: requires API 19
                            // playSound(v, AudioManager.FX_KEYPRESS_INVALID);
                        }
                        return true;
                    }
                    switch (event.getAction()) {
                        case android.view.KeyEvent.ACTION_DOWN :
                            if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG) {
                                android.util.Log.d(android.support.v17.leanback.widget.GuidedActionAdapter.TAG, "Enter Key down");
                            }
                            if (!mKeyPressed) {
                                mKeyPressed = true;
                                mStylist.onAnimateItemPressed(avh, mKeyPressed);
                            }
                            break;
                        case android.view.KeyEvent.ACTION_UP :
                            if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG) {
                                android.util.Log.d(android.support.v17.leanback.widget.GuidedActionAdapter.TAG, "Enter Key up");
                            }
                            // Sometimes we are losing ACTION_DOWN for the first ENTER after pressed
                            // Escape in IME.
                            if (mKeyPressed) {
                                mKeyPressed = false;
                                mStylist.onAnimateItemPressed(avh, mKeyPressed);
                            }
                            break;
                        default :
                            break;
                    }
                    break;
                default :
                    break;
            }
            return handled;
        }
    }

    private class ActionEditListener implements android.support.v17.leanback.widget.ImeKeyMonitor.ImeKeyListener , android.widget.TextView.OnEditorActionListener {
        ActionEditListener() {
        }

        @java.lang.Override
        public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
            if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG_EDIT)
                android.util.Log.v(android.support.v17.leanback.widget.GuidedActionAdapter.TAG_EDIT, "IME action: " + actionId);

            boolean handled = false;
            if ((actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) || (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE)) {
                mGroup.fillAndGoNext(android.support.v17.leanback.widget.GuidedActionAdapter.this, v);
                handled = true;
            } else
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NONE) {
                    if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG_EDIT)
                        android.util.Log.v(android.support.v17.leanback.widget.GuidedActionAdapter.TAG_EDIT, "closeIme escape north");

                    // Escape north handling: stay on current item, but close editor
                    handled = true;
                    mGroup.fillAndStay(android.support.v17.leanback.widget.GuidedActionAdapter.this, v);
                }

            return handled;
        }

        @java.lang.Override
        public boolean onKeyPreIme(android.widget.EditText editText, int keyCode, android.view.KeyEvent event) {
            if (android.support.v17.leanback.widget.GuidedActionAdapter.DEBUG_EDIT)
                android.util.Log.v(android.support.v17.leanback.widget.GuidedActionAdapter.TAG_EDIT, "IME key: " + keyCode);

            if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) && (event.getAction() == android.view.KeyEvent.ACTION_UP)) {
                mGroup.fillAndStay(android.support.v17.leanback.widget.GuidedActionAdapter.this, editText);
                return true;
            } else
                if ((keyCode == android.view.KeyEvent.KEYCODE_ENTER) && (event.getAction() == android.view.KeyEvent.ACTION_UP)) {
                    mGroup.fillAndGoNext(android.support.v17.leanback.widget.GuidedActionAdapter.this, editText);
                    return true;
                }

            return false;
        }
    }
}

